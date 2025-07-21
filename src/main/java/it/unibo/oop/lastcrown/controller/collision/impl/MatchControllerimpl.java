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
import it.unibo.oop.lastcrown.controller.collision.api.MainController;
import it.unibo.oop.lastcrown.controller.collision.api.MatchController;
import it.unibo.oop.lastcrown.controller.collision.impl.eventcharacters.CharacterState;
import it.unibo.oop.lastcrown.model.card.CardType;
import it.unibo.oop.lastcrown.model.characters.api.Enemy;
import it.unibo.oop.lastcrown.model.characters.api.GenericCharacter;
import it.unibo.oop.lastcrown.model.characters.api.Hero;
import it.unibo.oop.lastcrown.model.characters.api.PassiveEffect;
import it.unibo.oop.lastcrown.model.characters.api.PlayableCharacter;
import it.unibo.oop.lastcrown.model.characters.api.Requirement;
import it.unibo.oop.lastcrown.model.characters.impl.enemy.EnemyImpl;
import it.unibo.oop.lastcrown.model.characters.impl.playablecharacter.PlayableCharacterImpl;
import it.unibo.oop.lastcrown.model.collision.api.CollisionEvent;
import it.unibo.oop.lastcrown.model.collision.api.CollisionManager;
import it.unibo.oop.lastcrown.model.collision.api.CollisionResolver;
import it.unibo.oop.lastcrown.model.collision.impl.CollisionManagerImpl;
import it.unibo.oop.lastcrown.model.collision.impl.CollisionResolverImpl;
import it.unibo.oop.lastcrown.view.collision.api.GamePanel;
import it.unibo.oop.lastcrown.view.collision.api.MainView;

public final class MatchControllerimpl implements MatchController {
    // private final MainController controller;
    private final Map<Integer, CharacterFSM> playerFSMs = new HashMap<>();
    private final Map<Integer, GenericCharacterController> charactersController = new HashMap<>();
    private final Map<GenericCharacterController, HitboxController> hitboxControllers = new HashMap<>();
    // Aggiungi queste mappe
    private final Set<EnemyEngagement> engagedEnemies = ConcurrentHashMap.newKeySet();
    private final Map<Integer, Object> enemyLocks = new HashMap<>();
    private final CollisionManager collisionManager = new CollisionManagerImpl();
    private final CollisionResolver collisionResolver;
    private final CharacterSpawnerController spawner;
    private final EnemyRadiusScanner radiusScanner;
    private int nextId = 1;
    private final GamePanel gamePanel;
    private final MainView view;
    Requirement require = new Requirement("hero", 2);
    Optional<PassiveEffect> optionalEffect = Optional.of(new PassiveEffect("none", 0));

    public MatchControllerimpl(final MainController controller) {
        // this.controller = controller;
        this.view = controller.getMainView();
        this.gamePanel = controller.getMainView().getGamePanel();
        this.spawner = new CharacterSpawnerController(gamePanel);
        this.view.getGamePanel().setAddCharacterListener(e -> onAddCharacterButtonPressed());
        this.collisionResolver = new CollisionResolverImpl(this);
        this.collisionManager.addObserver(collisionResolver);
        this.radiusScanner = new EnemyRadiusScanner(hitboxControllers, this, collisionResolver);


    }

    @Override
    public void addCharacter(final int n, final GenericCharacterController controller,
            final HitboxController hitboxController) {
        charactersController.put(n, controller);
        hitboxControllers.put(controller, hitboxController);
    }

    @Override
    public void onAddCharacterButtonPressed() {
        final CharacterDeathObserver obs = id -> System.out.println("Morto: " + id);

        final PlayableCharacter Char1 = new PlayableCharacterImpl("Warrior", CardType.MELEE, 20, 25, 100, 2, 100, 0.8,100);
        final PlayableCharacter Char2 = new PlayableCharacterImpl("Knight",CardType.MELEE, 20, 22, 100, 2, 100, 0.8,100);

        final PlayableCharacter ranged = new PlayableCharacterImpl("Archer3",CardType.RANGED, 20, 95, 100, 2, 100, 0.8,100);

        final Enemy pipistrello = new EnemyImpl("Bat", 1, CardType.ENEMY, 3, 100, 0.8);
        final Enemy nemico2 = new EnemyImpl("Cthulu", 1, CardType.ENEMY, 3, 200, 0.8);
        final Enemy nemico3 = new EnemyImpl("Cthulu", 1, CardType.ENEMY, 3, 200, 0.8);

        // final Hero eroe = new HeroImpl("Valandor", new Requirement("Bosses", 80), 20,300, Optional.of(new PassiveEffect("health", 45)),3, 3, 4,8, 400);
        // spawnAndRegisterCharacter(generateUniqueCharacterId(), eroe, obs, 100, 200);

        spawnAndRegisterCharacter(generateUniqueCharacterId(), Char1, obs, 100, 300); /* bro sotto */
        spawnAndRegisterCharacter(generateUniqueCharacterId(), Char2, obs, 70, 200);
        spawnAndRegisterCharacter(generateUniqueCharacterId(), ranged, obs, 220, 110);

        spawnAndRegisterCharacter(generateUniqueCharacterId(), pipistrello, obs, 450, 100);
        spawnAndRegisterCharacter(generateUniqueCharacterId(), nemico2, obs, 200, 400);
        spawnAndRegisterCharacter(generateUniqueCharacterId(), nemico3, obs, 700, 180);

        // BOSS-FIGHT ==
        //final Enemy boss = new EnemyImpl("Cthulu", 1, CardType.BOSS, 33, 300, 0.2);
        //spawnAndRegisterCharacter(generateUniqueCharacterId(), boss, obs, 700, 200);

    }

    private void spawnAndRegisterCharacter(final int id, final Object model, final CharacterDeathObserver observer,
            final int x, final int y) {
        final SpawnedCharacter spawned = spawnCharacter(id, model, observer, x, y);

        addCharacter(id, spawned.controller(), spawned.hitboxController());
        if (model instanceof GenericCharacter) {

            final GenericCharacterController playerController = spawned.controller();
            playerFSMs.put(id, new CharacterFSM(playerController, this, radiusScanner, this.collisionResolver));
        }
        // System.out.println(playerFSMs.size());
    }

    public SpawnedCharacter spawnCharacter(final int id, final Object model, final CharacterDeathObserver observer,
            final int x, final int y) {
        if (model instanceof PlayableCharacter pc) {
            return spawner.spawnPlayableCharacter(id, pc, observer, x, y);
        } else if (model instanceof Hero hero) {
            return spawner.spawnHeroCharacter(id, hero, observer, x, y); // solo se Hero NON implementa
                                                                         // PlayableCharacter
        } else if (model instanceof Enemy enemy) {
            if (enemy.getEnemyType() == CardType.BOSS) {
                return spawner.spawnBossCharacter(id, enemy, observer, x, y);
            } else {
                return spawner.spawnEnemyCharacter(id, enemy, observer, x, y);
            }

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
        return Optional.ofNullable(this.charactersController.get(id)); // dove characters è la mappa di tutti i
                                                                       // controller
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
            // System.out.println("Nessun controller trovato per ID: " + characterId);
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

    private int generateUniqueCharacterId() {
        return nextId++;
    }

    /**
     * Aggiorna lo stato di un nemico e pulisce le mappe se necessario.
     *
     * @param enemyId  ID del nemico
     * @param playerId ID del giocatore (null se rilascio)
     * @return true se lo stato è cambiato
     */
    private boolean updateEnemyState(int enemyId, Integer playerId, boolean engage) {
        final Object lock = enemyLocks.computeIfAbsent(enemyId, k -> new Object());
        synchronized (lock) {
            if (engage) {
                // Controlla se già esiste un engagement per questo enemyId
                for (EnemyEngagement e : engagedEnemies) {
                    if (e.enemyId() == enemyId) {
                        // Già ingaggiato da qualcuno
                        return false;
                    }
                }
                System.out.println("sto aggancianfo il personaggio in matchcontroller");

                // Aggiungi nuovo engagement
                engagedEnemies.add(new EnemyEngagement(enemyId, playerId));

                setEnemyInCombat(enemyId, true);
                System.out.println(engagedEnemies);
                return true;

            } else {
                //System.out.println("sto sganciando il personaggio in matchcontroller");
                // Cerca e rimuovi l'engagement corrispondente
                EnemyEngagement toRemove = null;
                for (EnemyEngagement e : engagedEnemies) {
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

    public boolean isPlayerEngaged(final int playerId) {
        for (EnemyEngagement engagement : engagedEnemies) {
            if (engagement.playerId() == playerId) {
                return true;
            }
        }
        return false;
    }

    public boolean isEnemyEngaged(final int enemyId) {
        for (EnemyEngagement engagement : engagedEnemies) {
            if (engagement.enemyId() == enemyId) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean engageEnemy(int enemyId, int playerId) {
        return updateEnemyState(enemyId, playerId, true);
    }

    /**
     * Rimuove un engagement sia che il personaggio sia un enemy che un player.
     *
     * @param characterId l'ID del personaggio da sganciare.
     * @return true se è stato trovato e rimosso un engagement.
     */
    public boolean releaseEngagementFor(final int characterId) {
        EnemyEngagement toRemove = null;
        synchronized (engagedEnemies) {
            //System.out.println("lista prima di essere eliminata" + engagedEnemies);
            for (EnemyEngagement e : engagedEnemies) {
                if (e.enemyId() == characterId || e.playerId() == characterId) {
                    toRemove = e;
                    break;
                }
            }
            if (toRemove != null) {
                engagedEnemies.remove(toRemove);
                setEnemyInCombat(toRemove.enemyId(), false);
                enemyLocks.remove(toRemove.enemyId());
                //System.out.println("qui è quando li rimuovo" + engagedEnemies);
                //System.out.println("sto sganciando il personaggio in matchcontroller");

                return true;
            }

        }
        return false;
    }

    private void setEnemyInCombat(int enemyId, boolean inCombat) {
        getCharacterControllerById(enemyId).ifPresent(enemy -> {
            if (enemy instanceof EnemyController) {
                Object lock = enemyLocks.computeIfAbsent(enemyId, k -> new Object());
                synchronized (lock) {
                    ((EnemyController) enemy).setInCombat(inCombat);
                }
                //System.out.println("il nemico è in settato in combattimento: " + enemy.isInCombat());
            }
        });
    }



    public Set<EnemyEngagement> getEngagedEnemies() {
        return Collections.unmodifiableSet(engagedEnemies);
    }

    public boolean isEngagedWithDead(final int characterId) {
        // se il personaggio ha un ingaggio che vedo dalla lista
        if (isPlayerEngaged(characterId)) {
            int enemy = getEngagedCounterpart(characterId);
            if (enemy != -1) {
                GenericCharacterController enemycontroller = getCharacterControllerById(enemy).get();
                if (enemycontroller.isDead()) {
                    return true;
                }
                return false;
            }
            return false;
        } else {
            if (isEnemyEngaged(characterId)) {
                int character = getEngagedCounterpart(characterId);
                if (character != -1) {
                    GenericCharacterController charactercontroller = getCharacterControllerById(character).get();
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
     * Restituisce l'ID del personaggio con cui è ingaggiato quello dato.
     * Se non è ingaggiato con nessuno, restituisce -1.
     *
     * @param characterId l'ID del player o nemico
     * @return l'ID del personaggio ingaggiato, oppure -1 se non trovato
     */
    public int getEngagedCounterpart(final int characterId) {
        for (EnemyEngagement engagement : engagedEnemies) {
            if (engagement.playerId() == characterId) {
                return engagement.enemyId();
            } else if (engagement.enemyId() == characterId) {
                return engagement.playerId();
            }
        }
        return -1;
    }

    @Override
    public boolean isPlayerIdle(final PlayableCharacterController player) {
        CharacterFSM fsm = this.playerFSMs.get(player.getId().number());
        if (fsm != null) {
            return fsm.getCurrentState() == CharacterState.IDLE;
        }
        return false;
    }



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


    public boolean isRangedFightPartnerDead(final int id){
        if (collisionResolver.hasOpponentRangedPartner(id)){
            final int partnerId= collisionResolver.getOpponentRangedPartner(id);
            final Optional<GenericCharacterController> controllerOpt = getCharacterControllerById(partnerId);
            if (controllerOpt.isPresent()) {
                return controllerOpt.get().isDead();
            }
        }
        return false;
    }

    public boolean isEnemyDead(int enemyId) {
    GenericCharacterController controller = charactersController.get(enemyId);
    if (controller instanceof EnemyController enemyController) {
        if (enemyController.getId().type() != CardType.BOSS) {
            return controller.isDead();
        }
    }
    return false; // Non è un nemico normale oppure non esiste
}


}
