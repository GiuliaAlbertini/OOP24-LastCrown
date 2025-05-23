package it.unibo.oop.lastcrown.controller.impl;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JComponent;
import it.unibo.oop.lastcrown.controller.api.MainController;
import it.unibo.oop.lastcrown.controller.api.MatchController;
import it.unibo.oop.lastcrown.controller.characters.api.CharacterDeathObserver;
import it.unibo.oop.lastcrown.controller.characters.api.GenericCharacterController;
import it.unibo.oop.lastcrown.controller.characters.api.PlayableCharacterController;
import it.unibo.oop.lastcrown.controller.characters.impl.playablecharacter.PlCharControllerFactory;
import it.unibo.oop.lastcrown.model.api.Hitbox;
import it.unibo.oop.lastcrown.model.api.Point2D;
import it.unibo.oop.lastcrown.model.card.CardIdentifier;
import it.unibo.oop.lastcrown.model.card.CardType;
import it.unibo.oop.lastcrown.model.characters.api.PlayableCharacter;
import it.unibo.oop.lastcrown.model.characters.impl.playablecharacter.PlayableCharacterImpl;
import it.unibo.oop.lastcrown.model.impl.HitboxImpl;
import it.unibo.oop.lastcrown.model.impl.Point2DImpl;
import it.unibo.oop.lastcrown.view.GamePanel;
import it.unibo.oop.lastcrown.view.ImageLoader;
import it.unibo.oop.lastcrown.view.MainView;
import it.unibo.oop.lastcrown.view.characters.AnimationHandler;
import it.unibo.oop.lastcrown.view.characters.CharacterPathLoader;
import it.unibo.oop.lastcrown.view.characters.api.CharacterMovementObserver;
import it.unibo.oop.lastcrown.view.characters.api.GenericCharacterGUI;
import it.unibo.oop.lastcrown.view.impl.HitboxMaskBounds;
import it.unibo.oop.lastcrown.view.impl.HitboxPanelImpl;

public class MatchControllerimpl implements MatchController {
    private final MainController controller;
    private final Map<Integer, GenericCharacterController> characters = new HashMap<>();    
    private final Map<GenericCharacterController, Hitbox> gcHitboxes = new HashMap<>();
    private final Map<Integer, GenericCharacterGUI> characterGUIs = new HashMap<>();
    private final Point2D position = new Point2DImpl(100, 80);
    private int id = 1;
    private CharacterDeathObserver obs;
    private final GamePanel gamePanel;
    MainView view;
    AnimationHandler animationHandler;

    public MatchControllerimpl(MainController controller) { 
        this.controller = controller;
        this.view=controller.getMainView();   
        this.gamePanel = controller.getMainView().getGamePanel();
        this.view.setAddCharacterListener(e -> onAddCharacterButtonPressed());

    }

    public void addCharacter(final int n, GenericCharacterController controller, Hitbox hitbox) {
        characters.put(n, controller);
        gcHitboxes.put(controller, hitbox);

    }

    @Override
    public void onAddCharacterButtonPressed() {        
        CharacterDeathObserver dummyDeathObs = new CharacterDeathObserver() {

            @Override
            public void notifyDeath(CardIdentifier id) {
                    System.out.println("[DEBUG] Movimento ricevuto: dx=" + id);
            }
        };

        CharacterMovementObserver movObs = new CharacterMovementObserver() {

            @Override
            public void notifyMovement(int deltaX, int deltaY) {
                // TODO Auto-generated method stub
                throw new UnsupportedOperationException("Unimplemented method 'notifyMovement'");
            }
            
        };

        PlayableCharacter playableChar = new PlayableCharacterImpl(
            "Knight", CardType.MELEE, 20, 10, 20, 2, 100, 2, 100);
        int newId = characters.size();
        PlayableCharacterController soldato= PlCharControllerFactory.createPlCharController(dummyDeathObs, newId, playableChar);
        soldato.attachCharacterAnimationPanel(150, 100);
        JComponent charComp = soldato.getGraphicalComponent();
        charComp.setLocation(100, 100);
        gamePanel.addCharacterComponent(charComp);
        

        
        Hitbox hitbox = new HitboxImpl(10, 10, new Point2DImpl(charComp.getX(), charComp.getY())); // inizializza come vuoi
        HitboxPanelImpl hitboxPanel = new HitboxPanelImpl(hitbox);
        // Assumi che charComponent sia il componente che disegna il personaggio
        String path=CharacterPathLoader.loadHitboxPath("melee", "Knight");
        BufferedImage image=ImageLoader.getImage(path, 150, 100);
        HitboxMaskBounds hitboxMaskBounds = new HitboxMaskBounds(hitbox, charComp, hitboxPanel);
        hitboxMaskBounds.calculateHitboxCenter(image);
        gamePanel.addCharacterComponent(hitboxPanel.getHitboxPanel());
        soldato.stop();
    }
}