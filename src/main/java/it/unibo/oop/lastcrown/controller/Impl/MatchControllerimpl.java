package it.unibo.oop.lastcrown.controller.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import it.unibo.oop.lastcrown.controller.api.HitboxController;
import it.unibo.oop.lastcrown.controller.api.MainController;
import it.unibo.oop.lastcrown.controller.api.MatchController;
import it.unibo.oop.lastcrown.controller.characters.api.CharacterDeathObserver;
import it.unibo.oop.lastcrown.controller.characters.api.EnemyController;
import it.unibo.oop.lastcrown.controller.characters.api.GenericCharacterController;
import it.unibo.oop.lastcrown.controller.characters.api.PlayableCharacterController;
import it.unibo.oop.lastcrown.model.api.CollisionEvent;
import it.unibo.oop.lastcrown.model.api.CollisionManager;
import it.unibo.oop.lastcrown.model.api.CollisionResolver;
import it.unibo.oop.lastcrown.model.card.CardType;
import it.unibo.oop.lastcrown.model.characters.api.Enemy;
import it.unibo.oop.lastcrown.model.characters.api.PlayableCharacter;
import it.unibo.oop.lastcrown.model.characters.impl.enemy.EnemyImpl;
import it.unibo.oop.lastcrown.model.characters.impl.playablecharacter.PlayableCharacterImpl;
import it.unibo.oop.lastcrown.model.impl.CharacterMovementStop;
import it.unibo.oop.lastcrown.model.impl.CollisionManagerImpl;
import it.unibo.oop.lastcrown.model.impl.CollisionResolverImpl;
import it.unibo.oop.lastcrown.view.GamePanel;
import it.unibo.oop.lastcrown.view.MainView;
import it.unibo.oop.lastcrown.view.characters.AnimationHandler;
import it.unibo.oop.lastcrown.view.characters.Keyword;
import it.unibo.oop.lastcrown.view.characters.api.CharacterMovementObserver;
import it.unibo.oop.lastcrown.view.characters.api.Movement;

public class MatchControllerimpl implements MatchController {
    private final MainController controller;
    private final Map<Integer, GenericCharacterController> charactersController = new HashMap<>();
    private final Map<GenericCharacterController, HitboxController> hitboxControllers = new HashMap<>();
    private final CollisionManager collisionManager = new CollisionManagerImpl();
    private final Set<Integer> stoppedCharacters = new HashSet<>();
    private CharacterMovementObserver movb;
    private final CollisionResolver collisionResolver;
    private final CharacterSpawnerController spawner;
    private final EnemyRadiusScanner radiusScanner;
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
        this.spawner = new CharacterSpawnerController(gamePanel);
        this.radiusScanner = new EnemyRadiusScanner(hitboxControllers);
        this.view.setAddCharacterListener(e -> onAddCharacterButtonPressed());
        this.movb = (id, dx, dy) -> {
            moveSoldierTowards(id.number(), dx, dy);
        };

        this.stop = (characterid, enemyid) -> {
            stop(characterid, enemyid);
        };
        this.collisionResolver = new CollisionResolverImpl(movb, stop);
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
        CharacterDeathObserver obs = id -> System.out.println("Morto: " + id);
        PlayableCharacter newChar = new PlayableCharacterImpl("Warrior", CardType.MELEE, 20, 10, 20, 2, 100, 2, 100);
        Enemy newEnemy = new EnemyImpl("Bat", 1, CardType.ENEMY, 20, 30, 2);
        spawnAndRegisterCharacter(1, newChar, obs, 100, 200);
        spawnAndRegisterCharacter(2, newEnemy, obs, 500, 100);
    }

    private void spawnAndRegisterCharacter(int id, Object model, CharacterDeathObserver observer, int x, int y) {
        SpawnedCharacter spawned = spawnCharacter(id, model, observer, x, y);
        addCharacter(id, spawned.controller(), spawned.hitboxController());
    }

    public SpawnedCharacter spawnCharacter(int id, Object model, CharacterDeathObserver observer, int x, int y) {
        if (model instanceof PlayableCharacter pc) {
            return spawner.spawnPlayableCharacter(id, pc, observer, x, y);
        } else if (model instanceof Enemy e) {
            return spawner.spawnEnemyCharacter(id, e, observer, x, y);
        }
        throw new IllegalArgumentException("Model type non supportato");
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

    private int generateUniqueCharacterId() {
        return this.id++;
    }

    public void checkEnemyInRadius() {
        for (CollisionEvent event : radiusScanner.scanForFollowEvents()) {
            notifyCollisionObservers(event);
            follow = true;
        }
    }

    private void moveSoldierTowards(int id, int dx, int dy) {
        final var character = charactersController.get(id);
        if (character != null && character instanceof PlayableCharacterController) {
            final var soldier = (PlayableCharacterController) character;
            Movement mov = new Movement(dx, dy);
            soldier.showNextFrame();
            soldier.showNextFrameAndMove(mov);
        }
    }

    private void stop(int characterid, int enemyid) {
            // Evita di rieseguire il blocco se già fermi
        if (stoppedCharacters.contains(characterid) && stoppedCharacters.contains(enemyid)) {
            return;
        }
        final var character = charactersController.get(characterid);
        final var enemy = charactersController.get(enemyid);
        if (character != null && enemy != null && character instanceof GenericCharacterController) {
            final var soldier = (PlayableCharacterController) character;
            final var adverse = (EnemyController) enemy;
            soldier.setNextAnimation(Keyword.STOP);
            adverse.setNextAnimation(Keyword.STOP);
            soldier.showNextFrame();
            adverse.showNextFrame();
            stoppedCharacters.add(characterid); // blocca aggiornamenti successivi
            stoppedCharacters.add(enemyid); // blocca aggiornamenti successivi
        }
    }

    private void notifyCollisionObservers(CollisionEvent event) {
        collisionManager.notify(event);
    }

    @Override
    public void update(int deltaTime) {
        final var character = charactersController.get(1);
        final var enemy = charactersController.get(2);

        if (character != null && character instanceof GenericCharacterController) {
            final var soldier = (PlayableCharacterController) character;
            final var adverse = (EnemyController) enemy;
            if (!stoppedCharacters.contains(1)){
                // personaggio
                soldier.setNextAnimation(Keyword.RUN_RIGHT);
                Movement movement = new Movement(2, 0);
                soldier.showNextFrameAndMove(movement);

                if (adverse != null) {
                    adverse.setNextAnimation(Keyword.RUN_LEFT);
                    Movement movementEnemy = new Movement(-2, 0);
                    adverse.showNextFrameAndMove(movementEnemy);
                    updateCharacterPosition(soldier, movement.x(), movement.y());
                    updateCharacterPosition(adverse, movementEnemy.x(), movementEnemy.y());
                } else {
                    System.out.println("[DEBUG] Adverse null quando si tenta di settare l'animazione.");
                }
            }

            checkEnemyInRadius();
            collisionResolver.updateAllMovements(deltaTime);

        }
    }
}
