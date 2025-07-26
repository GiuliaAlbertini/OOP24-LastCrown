package it.unibo.oop.lastcrown.controller.collision.impl;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.swing.JComponent;
import javax.swing.JTextArea;

import it.unibo.oop.lastcrown.controller.app_managing.api.MainController;
import it.unibo.oop.lastcrown.controller.characters.api.CharacterDeathObserver;
import it.unibo.oop.lastcrown.controller.characters.api.EnemyController;
import it.unibo.oop.lastcrown.controller.characters.api.GenericCharacterController;
import it.unibo.oop.lastcrown.controller.characters.api.HeroController;
import it.unibo.oop.lastcrown.controller.characters.api.PlayableCharacterController;
import it.unibo.oop.lastcrown.controller.characters.api.Wall;
import it.unibo.oop.lastcrown.controller.characters.impl.hero.HeroControllerFactory;
import it.unibo.oop.lastcrown.controller.characters.impl.wall.WallFactory;
import it.unibo.oop.lastcrown.controller.characters.impl.wall.WallImpl;
import it.unibo.oop.lastcrown.controller.collision.api.HitboxController;
import it.unibo.oop.lastcrown.controller.collision.api.MatchController;
import it.unibo.oop.lastcrown.controller.collision.impl.eventcharacters.CharacterState;
import it.unibo.oop.lastcrown.controller.user.api.CollectionController;
import it.unibo.oop.lastcrown.model.card.CardIdentifier;
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
import it.unibo.oop.lastcrown.model.user.api.CompleteCollection;
import it.unibo.oop.lastcrown.view.characters.Keyword;
import it.unibo.oop.lastcrown.view.collision.api.GamePanel;
import it.unibo.oop.lastcrown.view.dimensioning.DimensionResolver;
import it.unibo.oop.lastcrown.view.map.MatchView;
import it.unibo.oop.lastcrown.view.map.MatchViewImpl;
import it.unibo.oop.lastcrown.view.menu.api.MainView;

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
    private int nextId = 1;
    private MatchView matchView;
    private final Wall wall;
    private final JTextArea eventWriter;
    private final JTextArea coinsWriter;
    private int coins;
    private final CompleteCollection collection;
    private final HeroController heroController;

    //set di carte passate che l'utente gioca -> crea il complete collection e con i getter prendi le carte

    // private final CharacterSpawnerController spawner
    private final EnemyRadiusScanner radiusScanner;
    // private final MatchView view;
    // private final MainView mainview;
    Requirement require = new Requirement("hero", 2);
    Optional<PassiveEffect> optionalEffect = Optional.of(new PassiveEffect("none", 0));

    public MatchControllerimpl(final MainController controller,
                                final int frameWidth,
                                final int frameHeight,
                                CardIdentifier heroId,
                                CollectionController collectionController) {

        //quando clicco nella zona -> mi viene passato l'id e instanzio il personaggio dal suo cardidentifier
        //collectionController permette di ottenere tutti i personaggi dal cardidentifier
        final CharacterDeathObserver obs = id -> System.out.println("Morto: " + id);

        this.collection = collectionController.getCompleteCollection();
        final Hero hero = this.collection.getHero(heroId).get();
        this.heroController = HeroControllerFactory.createHeroController(obs, generateUniqueCharacterId(), hero);
        heroController.attachCharacterAnimationPanel((int) (frameWidth * DimensionResolver.HERO.width()), (int) (frameHeight * DimensionResolver.HERO.height()));

        wall = WallFactory.createWall(hero.getWallAttack(), hero.getWallHealth(), 10, frameWidth / 2, (int) (frameHeight * DimensionResolver.UTILITYZONE.height()));

        final Font font = new Font("Calibri", Font.CENTER_BASELINE, 20);
        this.eventWriter = new JTextArea();
        this.eventWriter.setEditable(false);
        this.eventWriter.setFocusable(false);
        this.eventWriter.setFont(font);
        this.coinsWriter = new JTextArea();
        this.coinsWriter.setEditable(false);
        this.coinsWriter.setFocusable(false);
        this.coinsWriter.setFont(font);
        this.coinsWriter.setText(this.coins + "coins");
        /*
         * this.mainview= controller.getMainView();
         * this.controller = controller;
         * this.view = view.getMatchView();
         */
        // this.gamePanel = controller.getMainView().getGamePanel();
        // this.spawner = new CharacterSpawnerController(gamePanel);
        // this.view.getGamePanel().setAddCharacterListener(e ->
        // onAddCharacterButtonPressed());
        this.collisionResolver = new CollisionResolverImpl(this);
        this.collisionManager.addObserver(collisionResolver);
        this.radiusScanner = new EnemyRadiusScanner(hitboxControllers, this, collisionResolver);

    }

    public void newMatchView(final MatchView matchView) {
        this.matchView = matchView;
        this.matchView.addHeroGraphics(this.heroController.getGraphicalComponent());
        this.heroController.setNextAnimation(Keyword.STOP);
        this.heroController.showNextFrame();
    }

    @Override
    public void addCharacter(final int n, final GenericCharacterController controller,
            final HitboxController hitboxController) {
        charactersController.put(n, controller);
        hitboxControllers.put(controller, hitboxController);
    }

    // @Override
    // public void onAddCharacterButtonPressed() {
    // final CharacterDeathObserver obs = id -> System.out.println("Morto: " + id);

    // final PlayableCharacter Char1 = new PlayableCharacterImpl("Warrior",
    // CardType.MELEE, 20, 5, 100, 2, 100, 0.8,100);
    // final PlayableCharacter Char2 = new
    // PlayableCharacterImpl("Knight",CardType.MELEE, 20, 2, 100, 2, 100, 0.8,100);

    // final PlayableCharacter ranged = new PlayableCharacterImpl("Archer3",
    // CardType.RANGED, 20, 20, 100, 2, 100, 0.8, 100);

    // //final Enemy pipistrello = new EnemyImpl("Bat", 1, CardType.ENEMY, 23, 100,
    // 0.8);
    // //final Enemy nemico2 = new EnemyImpl("Cthulu", 1, CardType.ENEMY, 23, 200,
    // 0.8);
    // //final Enemy nemico3 = new EnemyImpl("Cthulu", 1, CardType.ENEMY, 23, 200,
    // 0.8);

    // // final Hero eroe = new HeroImpl("Valandor", new Requirement("Bosses", 80),
    // 20,300, Optional.of(new PassiveEffect("health", 45)),3, 3, 4,8, 400);
    // // spawnAndRegisterCharacter(generateUniqueCharacterId(), eroe, obs, 100,
    // 200);

    // spawnAndRegisterCharacter(generateUniqueCharacterId(), Char1, obs, 100, 300);
    // // /* bro sotto */
    // spawnAndRegisterCharacter(generateUniqueCharacterId(), Char2, obs, 50, 200);
    // // /*CASISTICHE 75-97-180 */
    // spawnAndRegisterCharacter(generateUniqueCharacterId(), ranged, obs, 220,
    // 110);

    // //spawnAndRegisterCharacter(generateUniqueCharacterId(), pipistrello, obs,
    // 450,100);
    // //spawnAndRegisterCharacter(generateUniqueCharacterId(), nemico2, obs, 200,
    // 400);
    // //spawnAndRegisterCharacter(generateUniqueCharacterId(), nemico3, obs,
    // 700,180);

    // // BOSS-FIGHT ==
    // final Enemy boss = new EnemyImpl("Cthulu", 1, CardType.BOSS, 33, 100, 0.2);
    // spawnAndRegisterCharacter(generateUniqueCharacterId(), boss, obs, 400, 200);

    // }

    // private void spawnAndRegisterCharacter(final int id, final Object model,
    //         final CharacterDeathObserver observer,
    //         final int x, final int y) {
    //     final SpawnedCharacter spawned = spawnCharacter(id, model, observer, x, y);

    //     addCharacter(id, spawned.controller(), spawned.hitboxController());
    //     if (model instanceof GenericCharacter) {

    //         final GenericCharacterController playerController = spawned.controller();
    //         playerFSMs.put(id, new CharacterFSM(playerController, this, radiusScanner,
    //                 this.collisionResolver));
    //     }
    // }

    // public SpawnedCharacter spawnCharacter(final int id, final Object model,
    //         final CharacterDeathObserver observer,
    //         final int x, final int y) {
    //     if (model instanceof PlayableCharacter pc) {
    //         return spawner.spawnPlayableCharacter(id, pc, observer, x, y);
    //     } else if (model instanceof Hero hero) {
    //         return spawner.spawnHeroCharacter(id, hero, observer, x, y);

    //     } else if (model instanceof Enemy enemy) {
    //         if (enemy.getEnemyType() == CardType.BOSS) {
    //             return spawner.spawnBossCharacter(id, enemy, observer, x, y);
    //         } else {
    //             return spawner.spawnEnemyCharacter(id, enemy, observer, x, y);
    //         }

    //     }
    //     throw new IllegalArgumentException("Model type non supportato");
    // }

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
        return Optional.ofNullable(this.charactersController.get(id));

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

        // final JComponent component = controller.getGraphicalComponent();
        // if (component != null) {
        // gamePanel.removeCharacterComponent(component);
        // gamePanel.repaintGamePanel();
        // }
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
                for (EnemyEngagement e : engagedEnemies) {
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
            }
        });
    }

    public Set<EnemyEngagement> getEngagedEnemies() {
        return Collections.unmodifiableSet(engagedEnemies);
    }

    public boolean isEngagedWithDead(final int characterId) {
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

    public boolean isEnemyDead(int enemyId) {
        GenericCharacterController controller = charactersController.get(enemyId);
        if (controller instanceof EnemyController enemyController) {
            if (enemyController.getId().type() != CardType.BOSS) {
                return controller.isDead();
            }
        }
        return false;
    }

    @Override
    public void notifyClicked(int x, int y) {
        this.eventWriter.setText("Click received");
    }

    @Override
    public void notifyButtonPressed(CardIdentifier id) {

    }

    @Override
    public void notifyPauseEnd() {

    }

    @Override
    public JComponent getWallHealthBar() {
        return this.wall.getHealthBarComponent();
    }

    @Override
    public JComponent getCoinsWriter() {
        return this.coinsWriter;
    }

    @Override
    public JComponent getEventWriter() {
        return this.eventWriter;
    }
}
