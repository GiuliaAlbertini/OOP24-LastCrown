package it.unibo.oop.lastcrown.controller.impl;

import java.util.HashMap;
import java.util.Map;
import javax.swing.JPanel;
import it.unibo.oop.lastcrown.controller.api.MainController;
import it.unibo.oop.lastcrown.controller.api.MatchController;
import it.unibo.oop.lastcrown.controller.characters.api.CharacterDeathObserver;
import it.unibo.oop.lastcrown.controller.characters.api.GenericCharacterController;
import it.unibo.oop.lastcrown.controller.characters.impl.GenericCharacterControllerImpl;
import it.unibo.oop.lastcrown.controller.characters.impl.playablecharacter.PlayableCharacterControllerImpl;
import it.unibo.oop.lastcrown.model.api.Hitbox;
import it.unibo.oop.lastcrown.model.api.Point2D;
import it.unibo.oop.lastcrown.model.card.CardType;
import it.unibo.oop.lastcrown.model.characters.api.PlayableCharacter;
import it.unibo.oop.lastcrown.model.characters.impl.playablecharacter.PlayableCharacterImpl;
import it.unibo.oop.lastcrown.model.impl.HitboxImpl;
import it.unibo.oop.lastcrown.model.impl.Point2DImpl;
import it.unibo.oop.lastcrown.view.MainView;
import it.unibo.oop.lastcrown.view.characters.api.CharacterAttackObserver;
import it.unibo.oop.lastcrown.view.characters.api.CharacterMovementObserver;
import it.unibo.oop.lastcrown.view.characters.api.GenericCharacterGUI;
import it.unibo.oop.lastcrown.view.characters.impl.GenericCharacterGUIImpl;
import it.unibo.oop.lastcrown.view.impl.HitboxPanel;

public class MatchControllerimpl implements MatchController {
    private final MainController mcontroller;
    private final Map<Integer, GenericCharacterController> characters = new HashMap<>();    
    private final Map<GenericCharacterController, Hitbox> gcHitboxes = new HashMap<>();
    private final Map<Integer, GenericCharacterGUI> characterGUIs = new HashMap<>();
    private final Point2D position = new Point2DImpl(50, 70);
    private int id = 1;
    private CharacterDeathObserver obs;
    private final JPanel gamePanel;
    MainView view;

    public MatchControllerimpl(MainController mcontroller) { 
        this.mcontroller = mcontroller;   
        this.gamePanel = mcontroller.getMainView().getPanel(GameState.GAME);
    }

    public void addCharacter(final int n, GenericCharacterController controller, Hitbox hitbox) {
        characters.put(n, controller);
        gcHitboxes.put(controller, hitbox);
    }

    @Override
    public void onAddCharacterButtonPressed() {
        //creo il personaggio
        PlayableCharacter playableChar = new PlayableCharacterImpl(
            "Knight", CardType.MELEE, 20, 10, 20, 2, 100, 200, 100);

        //lo metto come generico
        GenericCharacterController character = new PlayableCharacterControllerImpl(obs, id++, playableChar);
        Hitbox hitbox = new HitboxImpl(50, 50, position);
        
        int newId = characters.size();
        //addCharacter(newId, character, hitbox);
        
        CharacterAttackObserver dummyAtkObs = new CharacterAttackObserver() {
            @Override
            public void doAttack() {
                System.out.println("[DEBUG] Attacco eseguito");
            }
        };

        CharacterMovementObserver dummyMovObs = new CharacterMovementObserver() {
            @Override
            public void notifyMovement(int deltaX, int deltaY) {
                System.out.println("[DEBUG] Movimento ricevuto: dx=" + deltaX + ", dy=" + deltaY);
            }
        };

        GenericCharacterGUI soldato= new GenericCharacterGUIImpl(dummyAtkObs, dummyMovObs,"melee", "Knight", 2.0, 64, 64);
        //NUOVA VERSIONE MARCO DAI CONTROLLER
        soldato.createAnimationPanel();
        soldato.setAnimationPanelSize(50,70);
        soldato.setAnimationPanelPosition(gamePanel, 200, 200);
        soldato.startAttackLoop();
        Hitbox newHitbox = new HitboxImpl(30, 40, new Point2DImpl(500, 200));
        HitboxPanel hitboxPanel = new HitboxPanel(newHitbox);
        gamePanel.add(hitboxPanel.getHitboxPanel()); // <-- QUESTO MANCA
        hitboxPanel.updatePanel(); // aggiorna posizione (opzionale)
       
        System.out.println("[DEBUG] GamePanel children: " + gamePanel.getComponentCount());
        gamePanel.revalidate();
        gamePanel.repaint();
    }
}