package it.unibo.oop.lastcrown.controller.impl;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import javax.swing.JComponent;
import it.unibo.oop.lastcrown.controller.api.HitboxController;
import it.unibo.oop.lastcrown.controller.api.MainController;
import it.unibo.oop.lastcrown.controller.api.MatchController;
import it.unibo.oop.lastcrown.controller.characters.api.CharacterDeathObserver;
import it.unibo.oop.lastcrown.controller.characters.api.GenericCharacterController;
import it.unibo.oop.lastcrown.model.api.CollisionEvent;
import it.unibo.oop.lastcrown.model.api.CollisionManager;
import it.unibo.oop.lastcrown.model.api.CollisionResolver;
import it.unibo.oop.lastcrown.model.card.CardType;
import it.unibo.oop.lastcrown.model.characters.api.Enemy;
import it.unibo.oop.lastcrown.model.characters.api.GenericCharacter;
import it.unibo.oop.lastcrown.model.characters.api.PlayableCharacter;
import it.unibo.oop.lastcrown.model.characters.impl.enemy.EnemyImpl;
import it.unibo.oop.lastcrown.model.characters.impl.playablecharacter.PlayableCharacterImpl;
import it.unibo.oop.lastcrown.model.impl.CollisionManagerImpl;
import it.unibo.oop.lastcrown.model.impl.CollisionResolverImpl;
import it.unibo.oop.lastcrown.view.GamePanel;
import it.unibo.oop.lastcrown.view.MainView;

public final class MatchControllerimpl implements MatchController {
    //private final MainController controller;
    private final Map<Integer, CharacterFSM> playerFSMs = new HashMap<>();
    private final Map<Integer, GenericCharacterController> charactersController = new HashMap<>();
    private final Map<GenericCharacterController, HitboxController> hitboxControllers = new HashMap<>();
    private final CollisionManager collisionManager = new CollisionManagerImpl();
    private final CollisionResolver collisionResolver;
    private final CharacterSpawnerController spawner;
    private final EnemyRadiusScanner radiusScanner;
    private final int id = 1;
    private final GamePanel gamePanel;
    private final MainView view;

    public MatchControllerimpl(final MainController controller) {
        //this.controller = controller;
        this.view = controller.getMainView();
        this.gamePanel = controller.getMainView().getGamePanel();
        this.spawner = new CharacterSpawnerController(gamePanel);
        this.radiusScanner = new EnemyRadiusScanner(hitboxControllers);
        this.view.setAddCharacterListener(e -> onAddCharacterButtonPressed());
        this.collisionResolver = new CollisionResolverImpl();
        this.collisionManager.addObserver(collisionResolver);

    }

    @Override
    public void addCharacter(final int n, final GenericCharacterController controller,
            final HitboxController hitboxController) {
        charactersController.put(n, controller);
        hitboxControllers.put(controller, hitboxController);
    }

    @Override
    public void onAddCharacterButtonPressed() {
        //final int newId = generateUniqueCharacterId();
        final CharacterDeathObserver obs = id -> System.out.println("Morto: " + id);
        final PlayableCharacter newChar = new PlayableCharacterImpl("Warrior", CardType.MELEE, 20, 23, 100, 2, 100, 0.8, 100);
        final Enemy newEnemy = new EnemyImpl("Bat", 1, CardType.ENEMY, 3, 100, 0.8);
        final Enemy nemico2 = new EnemyImpl("Cthulu", 1, CardType.ENEMY, 3, 200, 0.8);
        spawnAndRegisterCharacter(new SecureRandom().nextInt(), newChar, obs, 100, 200);
        spawnAndRegisterCharacter(new SecureRandom().nextInt(), newEnemy, obs, 500, 100);
        spawnAndRegisterCharacter(new SecureRandom().nextInt(), nemico2, obs, 200, 400);

    }

    private void spawnAndRegisterCharacter(final int id, final Object model, final CharacterDeathObserver observer,
            final int x, final int y) {
        final SpawnedCharacter spawned = spawnCharacter(id, model, observer, x, y);

        addCharacter(id, spawned.controller(), spawned.hitboxController());
        if (model instanceof GenericCharacter) {
            final GenericCharacterController playerController = spawned.controller();
            playerFSMs.put(id, new CharacterFSM(playerController, this, radiusScanner, this.collisionResolver));
        }
        //System.out.println(playerFSMs.size());
    }

    public SpawnedCharacter spawnCharacter(final int id, final Object model, final CharacterDeathObserver observer,
            final int x, final int y) {
        if (model instanceof PlayableCharacter pc) {

            return spawner.spawnPlayableCharacter(id, pc, observer, x, y);
        } else if (model instanceof Enemy e) {
            return spawner.spawnEnemyCharacter(id, e, observer, x, y);
        }
        throw new IllegalArgumentException("Model type non supportato");
    }

    @Override
    public void updateCharacterPosition(final GenericCharacterController controller, final int dx, final int dy) {
        final JComponent component = controller.getGraphicalComponent();
        if (component == null) {
            return;
        }


        final int newX = component.getX() + dx;
        final int newY = component.getY() + dy;
        component.setLocation(newX, newY);

        final HitboxController hitboxController = hitboxControllers.get(controller);
        if (hitboxController != null) {
            hitboxController.setnewPosition(newX, newY);
            hitboxController.updateView();
            hitboxController.setVisibile(true);
        }

        component.repaint();

    }

    /*
    private int generateUniqueCharacterId() {
        return this.id++;
    }
    */

    @Override
    public void notifyCollisionObservers(final CollisionEvent event) {
        collisionManager.notify(event);
    }

    @Override
    public void update(final int deltaTime) {
        for (final CharacterFSM fsm : new ArrayList<>(playerFSMs.values())) {
            fsm.update(deltaTime);
        }
    }

    @Override
    public Optional<GenericCharacterController> getCharacterControllerById(final int id) {
        return Optional.ofNullable(this.charactersController.get(id)); // dove characters è la mappa di tutti i controller
    }

    @Override
    public CollisionResolver getCollisionResolver() {
        return this.collisionResolver;
    }

    @Override
    public Optional<HitboxController> getCharacterHitboxById(final int id) {
        final var charaController = this.getCharacterControllerById(id);
        if (charaController.isPresent()) {
            return Optional.ofNullable(this.hitboxControllers.get(charaController.get()));
        } else {
            return Optional.empty();
        }
    }

    @Override
    public void removeCharacterCompletelyById(final int characterId) {
        // 1. Rimuovi l'FSM
        playerFSMs.remove(characterId);

        // 2. Rimuovi il controller
        final GenericCharacterController controller = charactersController.remove(characterId);
        if (controller == null) {
            //System.out.println("Nessun controller trovato per ID: " + characterId);
            return;
        }

        // 3. Rimuovi l'hitbox e raggio aggiorna visibilità
        final HitboxController hitbox = hitboxControllers.remove(controller);
        if (hitbox != null) {
            hitbox.setVisibile(false);
            hitbox.removeFromPanel();
        }

        // 4. Rimuovi dalla GUI
        final JComponent component = controller.getGraphicalComponent();
        if (component != null) {
            gamePanel.removeCharacterComponent(component);
            gamePanel.repaintGamePanel();
        }
    }

}
