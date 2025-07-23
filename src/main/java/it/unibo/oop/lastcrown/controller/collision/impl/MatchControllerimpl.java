package it.unibo.oop.lastcrown.controller.collision.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.swing.JComponent;

import it.unibo.oop.lastcrown.controller.characters.api.CharacterDeathObserver;
import it.unibo.oop.lastcrown.controller.characters.api.EnemyController;
import it.unibo.oop.lastcrown.controller.characters.api.GenericCharacterController;
import it.unibo.oop.lastcrown.controller.characters.api.PlayableCharacterController;
import it.unibo.oop.lastcrown.controller.collision.api.HitboxController;
import it.unibo.oop.lastcrown.controller.collision.api.MainControllerExample;
import it.unibo.oop.lastcrown.controller.collision.api.MatchController;
import it.unibo.oop.lastcrown.controller.collision.impl.eventcharacters.CharacterState;
import it.unibo.oop.lastcrown.model.card.CardType;
import it.unibo.oop.lastcrown.model.characters.api.Enemy;
import it.unibo.oop.lastcrown.model.characters.api.GenericCharacter;
import it.unibo.oop.lastcrown.model.characters.api.Hero;
import it.unibo.oop.lastcrown.model.characters.api.PlayableCharacter;
import it.unibo.oop.lastcrown.model.characters.impl.enemy.EnemyImpl;
import it.unibo.oop.lastcrown.model.characters.impl.playablecharacter.PlayableCharacterImpl;
import it.unibo.oop.lastcrown.model.collision.api.CollisionEvent;
import it.unibo.oop.lastcrown.model.collision.api.CollisionManager;
import it.unibo.oop.lastcrown.model.collision.api.CollisionResolver;
import it.unibo.oop.lastcrown.model.collision.impl.CollisionManagerImpl;
import it.unibo.oop.lastcrown.model.collision.impl.CollisionResolverImpl;
import it.unibo.oop.lastcrown.view.collision.api.GamePanel;
import it.unibo.oop.lastcrown.view.collision.api.MainViewExample;
//import it.unibo.oop.lastcrown.model.characters.api.Requirement;
//import it.unibo.oop.lastcrown.model.characters.api.PassiveEffect;

/**
 * Implements the MatchController interface and coordinates the logic
 * for character spawning, updating, and collision resolution during a match.
 */
public final class MatchControllerimpl implements MatchController {
    // private final MainController controller;
    private final Map<Integer, CharacterFSM> playerFSMs = new HashMap<>();
    private final Map<Integer, GenericCharacterController> charactersController = new HashMap<>();
    private final Map<GenericCharacterController, HitboxController> hitboxControllers = new HashMap<>();
    private final Set<EnemyEngagement> engagedEnemies = ConcurrentHashMap.newKeySet();
    private final Map<Integer, Object> enemyLocks = new HashMap<>();
    private final CollisionManager collisionManager = new CollisionManagerImpl();
    private final CollisionResolver collisionResolver;
    private final CharacterSpawnerController spawner;
    private final EnemyRadiusScanner radiusScanner;
    private int nextId = 1;
    private final GamePanel gamePanel;
    private final MainViewExample view;
    //private final Requirement require = new Requirement("hero", 2);
    //private final Optional<PassiveEffect> optionalEffect = Optional.of(new PassiveEffect("none", 0));

    /**
     * Constructs a MatchControllerimpl and initializes its dependencies.
     *
     * @param controller the main controller used to access the view and panel
     */
    public MatchControllerimpl(final MainControllerExample controller) {
        // this.controller = controller;
        this.view = controller.getMainView();
        this.gamePanel = controller.getMainView().getGamePanel();
        this.spawner = new CharacterSpawnerController(gamePanel);
        this.view.getGamePanel().setAddCharacterListener(e -> onAddCharacterButtonPressed());
        this.collisionResolver = new CollisionResolverImpl(this);
        this.collisionManager.addObserver(collisionResolver);
        this.radiusScanner = new EnemyRadiusScanner(hitboxControllers, this, collisionResolver);
    }

    /**
     * Registers a character and its hitbox controller in the match.
     *
     * @param n the character ID
     * @param controller the character controller
     * @param hitboxController the hitbox controller for the character
     */
    @Override
    public void addCharacter(final int n, final GenericCharacterController controller,
            final HitboxController hitboxController) {
        charactersController.put(n, controller);
        hitboxControllers.put(controller, hitboxController);
    }

    /**
     * Called when the "add character" button is pressed. Spawns and registers characters
     * including melee, ranged, and boss types.
     */
    @Override
    public void onAddCharacterButtonPressed() {
        final CharacterDeathObserver obs = id -> System.out.println("Morto: " + id);

        //final PlayableCharacter char1 = new PlayableCharacterImpl("Warrior", CardType.MELEE, 20, 5, 100, 2, 100, 0.8,100);
        //final PlayableCharacter char2 = new PlayableCharacterImpl("Knight",CardType.MELEE, 20, 2, 100, 2, 100, 0.8,100);
        final PlayableCharacter ranged = new PlayableCharacterImpl("Archer3", CardType.RANGED, 20, 20, 100, 2, 100, 0.8, 100);

        final Enemy pipistrello = new EnemyImpl("Bat", 1, CardType.ENEMY, 23, 100, 0.8);
        //final Enemy nemico2 = new EnemyImpl("Cthulu", 1, CardType.ENEMY, 23, 200, 0.8);
        //final Enemy nemico3 = new EnemyImpl("Cthulu", 1, CardType.ENEMY, 23, 200, 0.8);

        // final Hero eroe = new HeroImpl("Valandor", new Requirement("Bosses", 80), 20,300,
        // Optional.of(new PassiveEffect("health", 45)),3, 3, 4,8, 400);
        // spawnAndRegisterCharacter(generateUniqueCharacterId(), eroe, obs, 100, 200);

        //spawnAndRegisterCharacter(generateUniqueCharacterId(), char1, obs, 100, 300);
        //spawnAndRegisterCharacter(generateUniqueCharacterId(), char2, obs, 50, 200);// /*CASISTICHE 75-97-180 */
        //spawnAndRegisterCharacter(generateUniqueCharacterId(), ranged, obs, 220, 110);

        //spawnAndRegisterCharacter(generateUniqueCharacterId(), pipistrello, obs, 450,100);
        //spawnAndRegisterCharacter(generateUniqueCharacterId(), nemico2, obs, 200, 400);
        //spawnAndRegisterCharacter(generateUniqueCharacterId(), nemico3, obs, 700,180);

        // BOSS-FIGHT ==
        //final Enemy boss = new EnemyImpl("Cthulu", 1, CardType.BOSS, 33, 100, 0.2);
        //spawnAndRegisterCharacter(generateUniqueCharacterId(), boss, obs, 400, 200);

    }

    /**
     * Spawns and registers a character, adding it to the controller and FSM system.
     *
     * @param id the unique character ID
     * @param model the character model (PlayableCharacter, Hero, or Enemy)
     * @param observer an observer for character death
     * @param x the spawn x-coordinate
     * @param y the spawn y-coordinate
     */
    private void spawnAndRegisterCharacter(final int id, final Object model, final CharacterDeathObserver observer,
            final int x, final int y) {
        final SpawnedCharacter spawned = spawnCharacter(id, model, observer, x, y);

        addCharacter(id, spawned.controller(), spawned.hitboxController());
        if (model instanceof GenericCharacter) {

            final GenericCharacterController playerController = spawned.controller();
            playerFSMs.put(id, new CharacterFSM(playerController, this, radiusScanner, this.collisionResolver));
        }
    }

    /**
     * Spawns a character and returns its wrapped representation.
     *
     * @param id the unique ID for the character
     * @param model the character instance
     * @param observer observer for character death
     * @param x spawn x-coordinate
     * @param y spawn y-coordinate
     * @return the spawned character wrapper
     */
    public SpawnedCharacter spawnCharacter(final int id, final Object model, final CharacterDeathObserver observer,
            final int x, final int y) {
        if (model instanceof PlayableCharacter pc) {
            return spawner.spawnPlayableCharacter(id, pc, observer, x, y);
        } else if (model instanceof Hero hero) {
            return spawner.spawnHeroCharacter(id, hero, observer, x, y);

        } else if (model instanceof Enemy enemy) {
            if (enemy.getEnemyType() == CardType.BOSS) {
                return spawner.spawnBossCharacter(id, enemy, observer, x, y);
            } else {
                return spawner.spawnEnemyCharacter(id, enemy, observer, x, y);
            }

        }
        throw new IllegalArgumentException("Model type non supportato");
    }

    /**
     * Updates the position of a character and its hitbox based on the delta values.
     *
     * @param controller the character controller
     * @param dx horizontal movement
     * @param dy vertical movement
     */
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

    /**
     * Notifies observers of a collision event.
     *
     * @param event the collision event to notify
     */
    @Override
    public void notifyCollisionObservers(final CollisionEvent event) {
        collisionManager.notify(event);
    }


     /**
     * Updates the internal FSMs for all characters with the time delta.
     *
     * @param deltaTime the time since last update in milliseconds
     */
    @Override
    public void update(final int deltaTime) {
        for (final CharacterFSM fsm : new ArrayList<>(playerFSMs.values())) {
            fsm.update(deltaTime);
        }
    }

    /**
     * Gets a character controller by its ID.
     *
     * @param id the character ID
     * @return an Optional containing the controller, if found
     */
    @Override
    public Optional<GenericCharacterController> getCharacterControllerById(final int id) {
        return Optional.ofNullable(this.charactersController.get(id));

    }

    /**
     * Gets the hitbox controller associated with a character ID.
     *
     * @param id the character ID
     * @return an Optional with the hitbox controller if found
     */
    @Override
    public Optional<HitboxController> getCharacterHitboxById(final int id) {
        final var charaController = this.getCharacterControllerById(id);
        if (charaController.isPresent()) {
            return Optional.ofNullable(this.hitboxControllers.get(charaController.get()));
        } else {
            return Optional.empty();
        }
    }

    /**
     * Completely removes a character from the match, including FSM, controller,
     * hitbox, and graphical component.
     *
     * @param characterId the ID of the character to remove
     */
    @Override
    public void removeCharacterCompletelyById(final int characterId) {
        playerFSMs.remove(characterId);

        final GenericCharacterController controller = charactersController.remove(characterId);
        if (controller == null) {
            return;
        }

        final HitboxController hitbox = hitboxControllers.remove(controller);
        if (hitbox != null) {
            hitbox.setVisibile(false);
            hitbox.removeFromPanel();
        }

        final JComponent component = controller.getGraphicalComponent();
        if (component != null) {
            gamePanel.removeCharacterComponent(component);
            gamePanel.repaintGamePanel();
        }
    }


    private int generateUniqueCharacterId() {
        return nextId++;
    }


    private boolean updateEnemyState(final int enemyId, final int playerId, final boolean engage) {
        final Object lock = enemyLocks.computeIfAbsent(enemyId, k -> new Object());
        synchronized (lock) {
            if (engage) {
                for (final EnemyEngagement e : engagedEnemies) {
                    if (e.enemyId() == enemyId) {
                        return false;
                    }
                }
                engagedEnemies.add(new EnemyEngagement(enemyId, playerId));
                setEnemyInCombat(enemyId, true);
                System.out.println(engagedEnemies);
                return true;

            } else {
                EnemyEngagement toRemove = null;
                for (final EnemyEngagement e : engagedEnemies) {
                    if (e.enemyId() == enemyId && e.playerId() == playerId) {
                        toRemove = e;
                        break;
                    }
                }

                if (toRemove != null) {
                    engagedEnemies.remove(toRemove);
                    setEnemyInCombat(enemyId, false);
                    enemyLocks.remove(enemyId);
                    return true;
                }
                return false;
            }
        }
    }

    /**
     * Checks if the given player is currently engaged in combat.
     *
     * @param playerId the player's ID
     * @return true if engaged
     */
    @Override
    public boolean isPlayerEngaged(final int playerId) {
        for (final EnemyEngagement engagement : engagedEnemies) {
            if (engagement.playerId() == playerId) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if the given enemy is currently engaged in combat.
     *
     * @param enemyId the enemy's ID
     * @return true if engaged
     */
    @Override
    public boolean isEnemyEngaged(final int enemyId) {
        for (final EnemyEngagement engagement : engagedEnemies) {
            if (engagement.enemyId() == enemyId) {
                return true;
            }
        }
        return false;
    }

    /**
     * Engages an enemy with a player.
     *
     * @param enemyId  the enemy's ID
     * @param playerId the player's ID
     * @return true if engagement was successful
     */
    @Override
    public boolean engageEnemy(final int enemyId, final int playerId) {
        return updateEnemyState(enemyId, playerId, true);
    }

    /**
     * Releases any engagement involving the given character ID.
     *
     * @param characterId the ID of the character
     * @return true if an engagement was found and removed
     */
    @Override
    public boolean releaseEngagementFor(final int characterId) {
        EnemyEngagement toRemove = null;
        synchronized (engagedEnemies) {
            for (final EnemyEngagement e : engagedEnemies) {
                if (e.enemyId() == characterId || e.playerId() == characterId) {
                    toRemove = e;
                    break;
                }
            }
            if (toRemove != null) {
                engagedEnemies.remove(toRemove);
                setEnemyInCombat(toRemove.enemyId(), false);
                enemyLocks.remove(toRemove.enemyId());
                return true;
            }

        }
        return false;
    }

    private void setEnemyInCombat(final int enemyId, final boolean inCombat) {
        getCharacterControllerById(enemyId).ifPresent(enemy -> {
            if (enemy instanceof EnemyController) {
                final Object lock = enemyLocks.computeIfAbsent(enemyId, k -> new Object());
                synchronized (lock) {
                    ((EnemyController) enemy).setInCombat(inCombat);
                }
            }
        });
    }

    /**
     * Returns an unmodifiable view of currently engaged enemies.
     *
     * @return set of engaged enemies
     */
    @Override
    public Set<EnemyEngagement> getEngagedEnemies() {
        return Collections.unmodifiableSet(engagedEnemies);
    }

    /**
     * Checks if a character is engaged with a dead counterpart.
     * @param characterId the character ID
     * @return true if the opponent is dead
     */
    @Override
    public boolean isEngagedWithDead(final int characterId) {
        if (isPlayerEngaged(characterId)) {
            final int enemy = getEngagedCounterpart(characterId);
            if (enemy != -1) {
                final GenericCharacterController enemycontroller = getCharacterControllerById(enemy).get();
                if (enemycontroller.isDead()) {
                    return true;
                }
                return false;
            }
            return false;
        } else {
            if (isEnemyEngaged(characterId)) {
                final int character = getEngagedCounterpart(characterId);
                if (character != -1) {
                    final GenericCharacterController charactercontroller = getCharacterControllerById(character).get();
                    if (charactercontroller.isDead()) {
                        return true;
                    }
                    return false;
                }
            }
            return false;
        }
    }

    /**
     * Gets the ID of the opponent engaged with a given character.
     *
     * @param characterId the character's ID
     * @return opponent's ID or -1 if not engaged
     */
    public int getEngagedCounterpart(final int characterId) {
        for (final EnemyEngagement engagement : engagedEnemies) {
            if (engagement.playerId() == characterId) {
                return engagement.enemyId();
            } else if (engagement.enemyId() == characterId) {
                return engagement.playerId();
            }
        }
        return -1;
    }

    /**
     * Checks if a player's FSM is currently in the IDLE state.
     *
     * @param player the player controller
     * @return true if idle
     */
    @Override
    public boolean isPlayerIdle(final PlayableCharacterController player) {
        final CharacterFSM fsm = this.playerFSMs.get(player.getId().number());
        if (fsm != null) {
            return fsm.getCurrentState() == CharacterState.IDLE;
        }
        return false;
    }

    /**
     * Checks if the player's boss fight partner is dead.
     *
     * @param id the player's ID
     * @return true if the partner is dead
     */
    @Override
    public boolean isBossFightPartnerDead(final int id) {
        if (collisionResolver.hasOpponentBossPartner(id)) {
            final int partnerId = collisionResolver.getOpponentBossPartner(id);
            final Optional<GenericCharacterController> controllerOpt = getCharacterControllerById(partnerId);
            if (controllerOpt.isPresent()) {
                return controllerOpt.get().isDead();
            }
        }
        return false;
    }

    /**
     * Checks if the player's ranged fight partner is dead.
     *
     * @param id the player's ID
     * @return true if the partner is dead
     */
    @Override
    public boolean isRangedFightPartnerDead(final int id) {
        if (collisionResolver.hasOpponentRangedPartner(id)) {
            final int partnerId = collisionResolver.getOpponentRangedPartner(id);
            final Optional<GenericCharacterController> controllerOpt = getCharacterControllerById(partnerId);
            if (controllerOpt.isPresent()) {
                return controllerOpt.get().isDead();
            }
        }
        return false;
    }

    /**
     * Checks if an enemy (non-boss) is dead.
     *
     * @param enemyId the enemy ID
     * @return true if dead
     */
    @Override
    public boolean isEnemyDead(final int enemyId) {
        final GenericCharacterController controller = charactersController.get(enemyId);
        if (controller instanceof EnemyController enemyController) {
            if (enemyController.getId().type() != CardType.BOSS) {
                return controller.isDead();
            }
        }
        return false;
    }
}
