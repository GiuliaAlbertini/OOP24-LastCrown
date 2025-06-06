package it.unibo.oop.lastcrown.controller.impl;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;

import it.unibo.oop.lastcrown.controller.api.HitboxController;
import it.unibo.oop.lastcrown.controller.api.MainController;
import it.unibo.oop.lastcrown.controller.api.MatchController;
import it.unibo.oop.lastcrown.controller.characters.api.CharacterDeathObserver;
import it.unibo.oop.lastcrown.controller.characters.api.EnemyController;
import it.unibo.oop.lastcrown.controller.characters.api.GenericCharacterController;
import it.unibo.oop.lastcrown.controller.characters.api.PlayableCharacterController;
import it.unibo.oop.lastcrown.controller.characters.impl.enemy.EnemyControllerFactory;
import it.unibo.oop.lastcrown.controller.characters.impl.playablecharacter.PlCharControllerFactory;
import it.unibo.oop.lastcrown.model.api.Collidable;
import it.unibo.oop.lastcrown.model.api.CollisionEvent;
import it.unibo.oop.lastcrown.model.api.CollisionManager;
import it.unibo.oop.lastcrown.model.api.CollisionResolver;
import it.unibo.oop.lastcrown.model.api.EventType;
import it.unibo.oop.lastcrown.model.api.Hitbox;
import it.unibo.oop.lastcrown.model.api.Radius;
import it.unibo.oop.lastcrown.model.card.CardType;
import it.unibo.oop.lastcrown.model.characters.api.Enemy;
import it.unibo.oop.lastcrown.model.characters.api.PlayableCharacter;
import it.unibo.oop.lastcrown.model.characters.impl.enemy.EnemyImpl;
import it.unibo.oop.lastcrown.model.characters.impl.playablecharacter.PlayableCharacterImpl;
import it.unibo.oop.lastcrown.model.impl.CharacterMovementStop;
import it.unibo.oop.lastcrown.model.impl.CollidableImpl;
import it.unibo.oop.lastcrown.model.impl.CollisionEventImpl;
import it.unibo.oop.lastcrown.model.impl.CollisionManagerImpl;
import it.unibo.oop.lastcrown.model.impl.CollisionResolverImpl;
import it.unibo.oop.lastcrown.model.impl.HitboxImpl;
import it.unibo.oop.lastcrown.model.impl.Point2DImpl;
import it.unibo.oop.lastcrown.model.impl.RadiusImpl;
import it.unibo.oop.lastcrown.view.GamePanel;
import it.unibo.oop.lastcrown.view.ImageLoader;
import it.unibo.oop.lastcrown.view.MainView;
import it.unibo.oop.lastcrown.view.api.HitboxPanel;
import it.unibo.oop.lastcrown.view.characters.AnimationHandler;
import it.unibo.oop.lastcrown.view.characters.CharacterPathLoader;
import it.unibo.oop.lastcrown.view.characters.api.CharacterMovementObserver;
import it.unibo.oop.lastcrown.view.characters.api.Movement;
import it.unibo.oop.lastcrown.view.impl.HitboxMaskBounds;
import it.unibo.oop.lastcrown.view.impl.HitboxPanelImpl;
import it.unibo.oop.lastcrown.view.impl.RadiusPanel;
import it.unibo.oop.lastcrown.view.impl.RadiusPanelImpl;

public class MatchControllerimpl implements MatchController {
    private final MainController controller;
    private final Map<Integer, GenericCharacterController> charactersController = new HashMap<>();
    private final Map<GenericCharacterController, HitboxController> hitboxControllers = new HashMap<>();
    private final CollisionManager collisionManager = new CollisionManagerImpl();
    private CharacterMovementObserver movb;
    private final CollisionResolver collisionResolver;
    private CharacterMovementStop stop;
    private int id = 1;
    private final GamePanel gamePanel;
    MainView view;
    AnimationHandler animationHandler;
    boolean follow;

    public MatchControllerimpl(MainController controller) {
        this.controller = controller;
        this.view = controller.getMainView();
        this.gamePanel = controller.getMainView().getGamePanel();
        this.view.setAddCharacterListener(e -> onAddCharacterButtonPressed());
        this.movb = (id, dx, dy) -> {
                System.out.println("[DEBUG] Movimento notificato: ID " + id.number() + ", dx " + dx + ", dy " + dy);
                moveSoldierTowards(id.number(), dx, dy);
        };

        this.stop=(characterid, enemyid) -> {
            System.out.println("si deve stoppare");   
            stop(characterid, enemyid);             
        };
        this.collisionResolver = new CollisionResolverImpl(movb,stop);
        this.collisionManager.addObserver(collisionResolver);
    }

    @Override
    public void addCharacter(final int n, GenericCharacterController controller, HitboxController hitboxController) {
        charactersController.put(n, controller);
        hitboxControllers.put(controller, hitboxController);
    }

    @Override
    public void onAddCharacterButtonPressed() {
        final int newId = generateUniqueCharacterId();

        CharacterDeathObserver deathObserver = id -> System.out.println("[DEBUG] Morte personaggio: " + id);
        CharacterMovementObserver movementObserver = (id, dx, dy) -> {
            System.out.println("[DEBUG] Movimento player ID player: " + id.number() + ", dx: " + dx + ", dy: " + dy);
            handleMovement(id.number(), dx, dy);
        };

        PlayableCharacter playableChar = new PlayableCharacterImpl(
                "Warrior", CardType.MELEE, 20, 10, 20, 2, 100, 1, 100);
        PlayableCharacterController soldato = PlCharControllerFactory.createPlCharController(deathObserver,
                movementObserver, newId, playableChar);
        soldato.attachCharacterAnimationPanel(150, 100);
        setupCharacter(newId, soldato, playableChar, 100, 100);
        soldato.stop();
        addEnemyCharacter();
    }

    public void addEnemyCharacter() {
        final int newId = 3;
        CharacterDeathObserver deathObserver = id -> System.out.println("[DEBUG] Morte personaggio: " + id);
        CharacterMovementObserver movnemico = (id, dx, dy) -> {
            handleMovement(id.number(), dx, dy);
        };

        Enemy EnemyChar = new EnemyImpl("Bat", newId, CardType.ENEMY, 20, 50, 0.8);
        EnemyController nemico = EnemyControllerFactory.createEnemyController(deathObserver,
                movnemico, newId, EnemyChar);

        nemico.attachCharacterAnimationPanel(100, 100);
        setupEnemy(newId, nemico, EnemyChar, 500, 50);

        nemico.startRunning();

    }

    private void setupEnemy(int id, EnemyController controller, Enemy character, int x, int y) {
        JComponent charComp = controller.getGraphicalComponent();
        charComp.setLocation(x, y);
        gamePanel.addCharacterComponent(charComp);

        Hitbox hitbox = new HitboxImpl(10, 10, new Point2DImpl(x, y));
        HitboxPanel hitboxPanel = new HitboxPanelImpl(hitbox);
        gamePanel.addCharacterComponent(hitboxPanel.getHitboxPanel());

        HitboxMaskBounds bounds = setupHitboxMaskEnemy(hitbox, charComp, hitboxPanel, character);
        HitboxController hitboxController = new HitboxControllerImpl(hitbox, hitboxPanel, bounds);

        addCharacter(id, controller, hitboxController);
    }

    private void setupCharacter(int id, PlayableCharacterController controller, PlayableCharacter character, int x,
            int y) {
        JComponent charComp = controller.getGraphicalComponent();
        charComp.setLocation(x, y);
        gamePanel.addCharacterComponent(charComp);

        Hitbox hitbox = new HitboxImpl(10, 10, new Point2DImpl(x, y));
        HitboxPanel hitboxPanel = new HitboxPanelImpl(hitbox);
        gamePanel.addCharacterComponent(hitboxPanel.getHitboxPanel());

        HitboxMaskBounds bounds = setupHitboxMask(hitbox, charComp, hitboxPanel, character);
        HitboxController hitboxController = new HitboxControllerImpl(hitbox, hitboxPanel, bounds);
        addRadiusPanel(hitbox, 200, bounds, hitboxController);

        addCharacter(id, controller, hitboxController);
    }

    public HitboxMaskBounds setupHitboxMaskEnemy(Hitbox hitbox, JComponent target, HitboxPanel panel,
            Enemy character) {
        String chartype = character.getEnemyType().name().toLowerCase();
        String name = character.getName();
        String path = CharacterPathLoader.loadHitboxPath(chartype, name);
        BufferedImage image = ImageLoader.getImage(path, 150, 100);
        HitboxMaskBounds hitboxMaskBounds = new HitboxMaskBounds(hitbox, target, panel);
        hitboxMaskBounds.calculateHitboxCenter(image);
        return hitboxMaskBounds;
    }

    public HitboxMaskBounds setupHitboxMask(Hitbox hitbox, JComponent target, HitboxPanel panel,
            PlayableCharacter character) {
        String chartype = character.getType().name().toLowerCase();
        String name = character.getName();
        String path = CharacterPathLoader.loadHitboxPath(chartype, name);
        BufferedImage image = ImageLoader.getImage(path, 150, 100);
        HitboxMaskBounds hitboxMaskBounds = new HitboxMaskBounds(hitbox, target, panel);
        hitboxMaskBounds.calculateHitboxCenter(image);
        return hitboxMaskBounds;
    }

    public void updateCharacterPosition(GenericCharacterController controller, int dx, int dy) {
        SwingUtilities.invokeLater(() -> {
            JComponent component = controller.getGraphicalComponent();
            if (component == null)
                return;

            int newX = component.getX() + dx;
            int newY = component.getY() + dy;
            component.setLocation(newX, newY);

            HitboxController hitboxController = hitboxControllers.get(controller);
            if (hitboxController != null) {
                hitboxController.setnewPosition(newX, newY);
                hitboxController.updateView();
                hitboxController.setVisibile(true);
            }

            component.repaint();
        });
    }



    private void handleMovement(int id, int dx, int dy) {
        GenericCharacterController controller = charactersController.get(id);
        if (controller == null) {
            System.err.println("[ERROR] Nessun controller trovato per ID: " + id);
            return;
        }
        updateCharacterPosition(controller, dx, dy);
    }

    private int generateUniqueCharacterId() {
        return this.id++;
    }

    private void addRadiusPanel(Hitbox origin, double radius, HitboxMaskBounds mask,
            HitboxController hitboxController) {
        Radius radiusCharacter = new RadiusImpl(origin, radius);
        RadiusPanel radiusPanel = new RadiusPanelImpl(radiusCharacter, mask);
        hitboxController.setRadius(radiusCharacter);
        hitboxController.setRadiusPanel(radiusPanel);
        gamePanel.addCharacterComponent(radiusPanel.getRadiusPanel());
    }

    public void checkEnemyInRadius() {
        List<Hitbox> enemies = collectAllEnemies();
        checkRadiusForNonEnemies(enemies);
    }

    private List<Hitbox> collectAllEnemies() {
        List<Hitbox> enemies = new ArrayList<>();
        for (Map.Entry<GenericCharacterController, HitboxController> entry : hitboxControllers.entrySet()) {
            GenericCharacterController controller = entry.getKey();
            if (controller.getId().type() == CardType.ENEMY) {
                enemies.add(entry.getValue().getHitbox());
            }
        }

        return enemies;
    }

    private void checkRadiusForNonEnemies(List<Hitbox> enemies) {
        for (Map.Entry<GenericCharacterController, HitboxController> entry : hitboxControllers.entrySet()) {
            GenericCharacterController controller = entry.getKey();
            HitboxController hitboxController = entry.getValue();

            if (controller.getId().type() != CardType.ENEMY) {
                Optional<Hitbox> enemyInRadius = checkEnemyInRadiusForCharacter(hitboxController, enemies);
                //System.out.println("Nemico più vicino: " + enemyInRadius.orElse(null));

                if (enemyInRadius.isPresent()) {
                    // 1. Trova il controller del nemico corrispondente alla hitbox
                    GenericCharacterController enemyController = findEnemyController(enemyInRadius.get());

                    if (enemyController != null) {
                        // 2. Crea i Collidable
                        Collidable playerCollidable = new CollidableImpl(hitboxController.getHitbox(),
                                controller.getId());
                        Collidable enemyCollidable = new CollidableImpl(enemyInRadius.get(), enemyController.getId());
                        // 3. Crea e notifica l'evento
                        CollisionEvent event = new CollisionEventImpl(EventType.FOLLOW_ENEMY, playerCollidable,
                                enemyCollidable);
                        notifyCollisionObservers(event);
                        follow = true;
                    }
                }
            }
        }
    }

    private void moveSoldierTowards(int id, int dx, int dy) {
        final var character = charactersController.get(id);
        if (character != null && character instanceof PlayableCharacterController) {
            final var soldier = (PlayableCharacterController) character;
            soldier.setManualRunningAnimation();
            Movement mov = new Movement(dx, dy);
            soldier.movePanel(mov);
            soldier.stopManualRunningAnimation();
        }
    }

    private void stop(int characterid, int enemyid){
        
        System.out.println("ma io qui entro?");
        final var character = charactersController.get(characterid);
        final var enemy = charactersController.get(enemyid);
        if (character != null && character instanceof PlayableCharacterController) {
        final var soldier = (PlayableCharacterController) character;
        final var adverse= (EnemyController) enemy;
            soldier.stop();
            adverse.stop();
            soldier.stopManualRunningAnimation();
        }
    }

    private Optional<Hitbox> checkEnemyInRadiusForCharacter(HitboxController hitboxController, List<Hitbox> enemies) {
        var radius = hitboxController.getRadius();
        if (radius != null && radius.hasEnemyInRadius(enemies)) {
            return radius.getClosestEnemyInRadius(enemies);
        }
        return Optional.empty();
    }

    private GenericCharacterController findEnemyController(Hitbox enemyHitbox) {
        for (Map.Entry<GenericCharacterController, HitboxController> entry : hitboxControllers.entrySet()) {
            if (entry.getValue().getHitbox().equals(enemyHitbox)) {
                return entry.getKey();
            }
        }
        return null;
    }

    private void notifyCollisionObservers(CollisionEvent event) {
        collisionManager.notify(event);
    }

    @Override
    public void update(int deltaTime) {
        checkEnemyInRadius();
        
        collisionResolver.updateAllMovements(deltaTime);
    }
}
