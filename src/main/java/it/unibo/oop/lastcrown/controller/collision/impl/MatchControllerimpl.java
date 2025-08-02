package it.unibo.oop.lastcrown.controller.collision.impl;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.swing.JComponent;
import javax.swing.JTextArea;
import it.unibo.oop.lastcrown.controller.app_managing.api.MainController;
import it.unibo.oop.lastcrown.controller.characters.api.BossController;
import it.unibo.oop.lastcrown.controller.characters.api.CharacterDeathObserver;
import it.unibo.oop.lastcrown.controller.characters.api.EnemyController;
import it.unibo.oop.lastcrown.controller.characters.api.GenericCharacterController;
import it.unibo.oop.lastcrown.controller.characters.api.HeroController;
import it.unibo.oop.lastcrown.controller.characters.api.PlayableCharacterController;
import it.unibo.oop.lastcrown.controller.characters.api.Wall;
import it.unibo.oop.lastcrown.controller.characters.impl.boss.BossControllerFactory;
import it.unibo.oop.lastcrown.controller.characters.impl.enemy.EnemyControllerFactory;
import it.unibo.oop.lastcrown.controller.characters.impl.hero.HeroControllerFactory;
import it.unibo.oop.lastcrown.controller.characters.impl.playablecharacter.PlCharControllerFactory;
import it.unibo.oop.lastcrown.controller.characters.impl.wall.WallFactory;
import it.unibo.oop.lastcrown.controller.collision.api.HitboxController;
import it.unibo.oop.lastcrown.controller.collision.api.MatchController;
import it.unibo.oop.lastcrown.controller.collision.impl.eventcharacters.CharacterState;
import it.unibo.oop.lastcrown.controller.user.api.CollectionController;
import it.unibo.oop.lastcrown.model.card.CardIdentifier;
import it.unibo.oop.lastcrown.model.card.CardType;
import it.unibo.oop.lastcrown.model.characters.api.Enemy;
import it.unibo.oop.lastcrown.model.characters.api.Hero;
import it.unibo.oop.lastcrown.model.characters.api.PassiveEffect;
import it.unibo.oop.lastcrown.model.characters.api.PlayableCharacter;
import it.unibo.oop.lastcrown.model.characters.api.Requirement;
import it.unibo.oop.lastcrown.model.characters.impl.enemy.EnemyImpl;
import it.unibo.oop.lastcrown.model.collision.api.CollisionEvent;
import it.unibo.oop.lastcrown.model.collision.api.CollisionManager;
import it.unibo.oop.lastcrown.model.collision.api.CollisionResolver;
import it.unibo.oop.lastcrown.model.collision.api.Hitbox;
import it.unibo.oop.lastcrown.model.collision.api.Point2D;
import it.unibo.oop.lastcrown.model.collision.api.Radius;
import it.unibo.oop.lastcrown.model.collision.impl.CollisionManagerImpl;
import it.unibo.oop.lastcrown.model.collision.impl.CollisionResolverImpl;
import it.unibo.oop.lastcrown.model.collision.impl.HitboxImpl;
import it.unibo.oop.lastcrown.model.collision.impl.Pair;
import it.unibo.oop.lastcrown.model.collision.impl.Point2DImpl;
import it.unibo.oop.lastcrown.model.collision.impl.RadiusImpl;
import it.unibo.oop.lastcrown.model.user.api.CompleteCollection;
import it.unibo.oop.lastcrown.view.ImageLoader;
import it.unibo.oop.lastcrown.view.characters.CharacterPathLoader;
import it.unibo.oop.lastcrown.view.characters.Keyword;
import it.unibo.oop.lastcrown.view.collision.api.HitboxPanel;
import it.unibo.oop.lastcrown.view.collision.api.RadiusPanel;
import it.unibo.oop.lastcrown.view.collision.impl.HitboxMaskBounds;
import it.unibo.oop.lastcrown.view.collision.impl.HitboxPanelImpl;
import it.unibo.oop.lastcrown.view.collision.impl.RadiusPanelImpl;
import it.unibo.oop.lastcrown.view.dimensioning.DimensionResolver;
import it.unibo.oop.lastcrown.view.map.MatchView;

public final class MatchControllerimpl implements MatchController {
    // private final MainController controller;
    private final Map<Integer, CharacterFSM> playerFSMs = new HashMap<>();
    private final Map<Integer, GenericCharacterController> charactersController = new HashMap<>();
    private final Map<GenericCharacterController, HitboxController> hitboxControllers = new HashMap<>();
    final List<Pair<String, PlayableCharacterController>> listCard = new ArrayList<>();
    // Aggiungi queste mappe
    private final Set<EnemyEngagement> engagedEnemies = ConcurrentHashMap.newKeySet();
    private final Map<Integer, Object> enemyLocks = new HashMap<>();
    private final CollisionManager collisionManager = new CollisionManagerImpl();
    private final CollisionResolver collisionResolver;
    private int nextId = 1;
    private MatchView matchView;
    private final Hero hero;
    private Wall wall;
    private final JTextArea eventWriter;
    private final JTextArea coinsWriter;
    private int coins;
    private int frameWidth;
    private int frameHeight;
    private final CompleteCollection collection;
    private final CollectionController collectionController;
    private final HeroController heroController;
    private static final int HITBOX_WIDTH = 10;
    private static final int HITBOX_HEIGHT = 10;
    private static final int DEFAULT_RADIUS = 250;
    private static final int UPGRADE_RADIUS = 400;

    private HitboxController wallHitboxController = null;
    /* spawner */
    private int spawnTimer = 0;
    private static final int SPAWN_INTERVAL = 5000;
    private int roundIndex = 1;
    private int enemyIndexInRound = 0;
    private List<Integer> usedPositions = new ArrayList<>();

    private boolean isBoosPresent = false;


    // set di carte passate che l'utente gioca -> crea il complete collection e con
    // i getter prendi le carte
    private final EnemyRadiusScanner radiusScanner;
    Requirement require = new Requirement("hero", 2);
    Optional<PassiveEffect> optionalEffect = Optional.of(new PassiveEffect("none", 0));

    public MatchControllerimpl(final MainController controller,
            final int frameWidth,
            final int frameHeight,
            CardIdentifier heroId,
            CollectionController collectionController) {
        this.collectionController = collectionController;
        this.frameHeight = frameHeight;
        this.frameWidth = frameWidth;
        final CharacterDeathObserver obs = id -> System.out.println("Morto: " + id);
        this.collection = collectionController.getCompleteCollection();
        this.hero = this.collection.getHero(heroId).get();
        this.heroController = HeroControllerFactory.createHeroController(obs, generateUniqueCharacterId(), hero);
        heroController.attachCharacterAnimationPanel((int) (frameWidth * DimensionResolver.HERO.width()),
                (int) (frameHeight * DimensionResolver.HERO.height()));

        this.wall = WallFactory.createWall(hero.getWallAttack(), hero.getWallHealth(), 10000, frameWidth / 2,
                (int) (frameHeight * DimensionResolver.UTILITYZONE.height()), Optional.empty());

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
        this.collisionResolver = new CollisionResolverImpl(this);
        this.collisionManager.addObserver(collisionResolver);
        this.radiusScanner = new EnemyRadiusScanner(hitboxControllers, this, collisionResolver);
    }

    public void newMatchView(final MatchView matchView) {
        this.matchView = matchView;
        this.matchView.addHeroGraphics(this.heroController.getId().number(), this.heroController.getGraphicalComponent());
        this.heroController.setNextAnimation(Keyword.STOP);
        this.heroController.showNextFrame();
        //spawnRandomEnemy(3);
        //spawn();
        createWallHitbox(matchView);
    }


    public void printEngagedEnemies() {
        if (engagedEnemies.isEmpty()) {
            System.out.println("Nessun nemico ingaggiato.");
            return;
        }

        System.out.println("Nemici ingaggiati:");
        for (EnemyEngagement engagement : engagedEnemies) {
            System.out.printf("- Nemico ID: %d, Giocatore ID: %d%n",
                    engagement.enemyId(), engagement.playerId());
        }
}

    private void createWallHitbox(final MatchView matchView) {
        final Point2D pos = new Point2DImpl(matchView.getWallCoordinates().getX(),
                matchView.getWallCoordinates().getY());
        final Hitbox wallHitbox = new HitboxImpl(matchView.getWallSize().width, matchView.getWallSize().height, pos);
        final HitboxPanel wallHitboxPanel = new HitboxPanelImpl(wallHitbox);
        this.wallHitboxController = new HitboxControllerImpl(wallHitbox, wallHitboxPanel, null, null);
        this.wall.setHitbox(wallHitbox);
        this.matchView.addWallPanel(wallHitboxController);
    }

    public Wall getWall() {
        return this.wall;
    }

    public HitboxController getWallHitboxController() {
        return this.wallHitboxController;
    }

    //controllo se i nemici hanno superato la linea
    public boolean isEnemyBeyondFrame(final int enemyId) {
        for (var entry : hitboxControllers.entrySet()) {
            final GenericCharacterController character = entry.getKey();
            final HitboxController hitbox = entry.getValue();

            if (character.getId().type() == CardType.ENEMY && character.getId().number() == enemyId) {
                return hitbox.getHitbox().getPosition().x() > frameWidth;
            }
        }
        return false;
    }

    // controllo se ci sono nemici nella mappa
    public boolean hasAnyEnemiesInMap() {
        for (var character : hitboxControllers.keySet()) {
            if (character.getId().type() == CardType.ENEMY) {
                return true; // Almeno un nemico trovato
            }
        }
        return false; // Nessun nemico trovato
    }


    public void setRadiusPlayerInMap() {
        for (Map.Entry<GenericCharacterController, HitboxController> entry : hitboxControllers.entrySet()) {
            final GenericCharacterController character = entry.getKey();
            final HitboxController hitboxController = entry.getValue();

            if (character.getId().type() == CardType.MELEE) {
                final Radius radius= hitboxController.getRadius().get();
                radius.setRadius(UPGRADE_RADIUS);
            }
        }
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

        spawnTimer += deltaTime;
        List<List<Enemy>> allEnemies = collectionController.getEnemies();
        if (roundIndex < allEnemies.size()) {
            List<Enemy> currentRound = allEnemies.get(roundIndex);

            if (spawnTimer >= SPAWN_INTERVAL && enemyIndexInRound < currentRound.size()) {
                spawnRandomEnemy(currentRound.get(enemyIndexInRound), enemyIndexInRound, currentRound.size() );
                enemyIndexInRound++;
                spawnTimer = 0;
            }

        }

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
        final GenericCharacterController controller = getCharacterControllerById(characterId).get();
        this.matchView.removeGraphicComponent(characterId);
        final HitboxController hitboxController= getCharacterHitboxById(characterId).get();
        hitboxControllers.remove(controller,hitboxController);
        charactersController.remove(characterId, controller);
        playerFSMs.remove(characterId);
    }

    private int generateUniqueCharacterId() {
        return nextId++;
    }

    // ========================================================
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
                Optional<GenericCharacterController> enemycontroller = getCharacterControllerById(enemy);
                if (enemycontroller.isPresent()) {
                    if (enemycontroller.get().isDead()) {
                        return true;
                    }
                }
                return false;
            }
            return false;
        } else {
            if (isEnemyEngaged(characterId)) {
                int character = getEngagedCounterpart(characterId);
                if (character != -1) {
                    Optional<GenericCharacterController> charactercontroller = getCharacterControllerById(character);
                    if(charactercontroller.isPresent()){
                        if (charactercontroller.get().isDead()) {
                        return true;
                    }
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

    @Override
    public boolean isPlayerStopped(final PlayableCharacterController player) {
        CharacterFSM fsm = this.playerFSMs.get(player.getId().number());
        if (fsm != null) {
            return fsm.getCurrentState() == CharacterState.STOPPED;
        }
        return false;
    }

    public CharacterState getCharacterState(final GenericCharacterController character) {
        for (Map.Entry<Integer, CharacterFSM> entry : playerFSMs.entrySet()) {
            final int id = entry.getKey();
            final CharacterFSM fsm = entry.getValue();
            final CharacterState state = fsm.getCurrentState();

            System.out.println("Player ID: " + id +  "| tipo: " + character.getId().type() + " | Stato: " + state);
        }

        final int id = character.getId().number();
        final CharacterFSM fsm = this.playerFSMs.get(id); // o enemyFSMs, a seconda dei casi

        if (fsm == null) {
            throw new IllegalStateException("FSM non trovato per il personaggio con ID: " + id);
        }

        return fsm.getCurrentState();
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
                //System.out.println("matchController" + enemyController.isDead() + "id: " +  enemyController.getId().number());
                return controller.isDead();
            }
        }
        return false;
    }

    @Override
    public void notifyClicked(int x, int y) {
        if (!listCard.isEmpty() && !hasBossInMap()) {
            final Pair<String, PlayableCharacterController> selected = listCard.get(listCard.size() - 1);
            final int id = selected.get2().getId().number();
            final PlayableCharacterController playerController = selected.get2();
            final String typeFolder = playerController.getId().type().name();
            final String name = selected.get1();

            playerController.attachCharacterAnimationPanel(
                    (int) (frameWidth * DimensionResolver.CHAR.width()),
                    (int) (frameHeight * DimensionResolver.CHAR.height()));

            final HitboxController hitboxController = this.matchView.addGenericGraphics(id,
                    playerController.getGraphicalComponent(), x, y, typeFolder, name);
            addCharacter(selected.get2().getId().number(), playerController, hitboxController);
            this.eventWriter.setText(name + " schierato in campo!");
        }
        listCard.clear();
    }

    @Override
    public void addCharacter(final int n, final GenericCharacterController controller,
            final HitboxController hitboxController) {
        int id = controller.getId().number();
        charactersController.put(id, controller);
        hitboxControllers.put(controller, hitboxController);
        playerFSMs.put(id, new CharacterFSM(controller, this, radiusScanner, this.collisionResolver));
        //System.out.println("id inserito in player" + id);
        // charactersController.put(n, controller);
        // hitboxControllers.put(controller, hitboxController);
        // playerFSMs.put(n, new CharacterFSM(controller, this, radiusScanner,
        // this.collisionResolver));

    }

    @Override
    public void notifyButtonPressed(CardIdentifier id) {
        final CharacterDeathObserver obs = idc -> System.out.println("Morto: " + idc);
        if (id.type() == CardType.MELEE || id.type() == CardType.RANGED) {
            final PlayableCharacter player = this.collection.getPlayableCharacter(id).get();
            final PlayableCharacterController playerController = PlCharControllerFactory.createPlCharController(obs,
                    generateUniqueCharacterId(), player);
            listCard.add(new Pair<String, PlayableCharacterController>(player.getName(), playerController));
            this.eventWriter.setText("Personaggio selezionato: " + player.getName().toString());
        }
    }

    public HitboxController setupCharacter(final JComponent charComp, final String typeFolder, final String name,
            final boolean isPlayable, int x, int y) {
        final Dimension size = charComp.getPreferredSize();
        System.out.println("vediamo se i parametri in setup vanno bene"+  typeFolder + name + charComp);
        final Hitbox hitbox = new HitboxImpl(HITBOX_WIDTH, HITBOX_HEIGHT, new Point2DImpl(x, y));
        final HitboxPanel hitboxPanel = new HitboxPanelImpl(hitbox);

        final String path = CharacterPathLoader.loadHitboxPath(typeFolder, name);
        System.out.println("vediamo il percorso" + path);
        final BufferedImage image = ImageLoader.getImage(path, (int) size.getWidth(), (int) size.getHeight());
        final HitboxMaskBounds bounds = new HitboxMaskBounds(hitbox, charComp, hitboxPanel);
        bounds.calculateHitboxCenter(image);
        final HitboxController hitboxController = new HitboxControllerImpl(hitbox, hitboxPanel, Optional.of(bounds),null );

        if (isPlayable) {
            final Radius radius = new RadiusImpl(hitbox, DEFAULT_RADIUS);
            hitboxController.setRadius(radius);
            final RadiusPanel radiusPanel = new RadiusPanelImpl(radius, bounds);
            hitboxController.setRadius(radius);
            hitboxController.setRadiusPanel(radiusPanel);
        }
        return hitboxController;
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

    public void spawnRandomEnemy(final Enemy enemy, int enemyIndex, int totalEnemies) {
        final CharacterDeathObserver obs = id -> System.out.println("Nemico morto: " + id);

        final EnemyController enemyController = EnemyControllerFactory.createEnemyController(
                obs, generateUniqueCharacterId(), enemy);

        enemyController.attachCharacterAnimationPanel(
                (int) (frameWidth * DimensionResolver.CHAR.width()),
                (int) (frameHeight * DimensionResolver.CHAR.height()));

        final int spawnX = frameWidth; // Fuori dallo schermo a destra
        int spawnY = generateRandomY(usedPositions, frameHeight);
        usedPositions.add(spawnY); // registra la posizione usata

        final int enemyId = enemyController.getId().number();
        final String typeFolder = enemyController.getId().type().name();
        final String name = enemy.getName();

        final HitboxController hitboxController = this.matchView.addEnemyGraphics(
                enemyId,
                enemyController.getGraphicalComponent(),
                spawnX, spawnY,
                typeFolder, name);
        System.out.println("personaggio aggiunto" + enemyId + enemy.getName());
        addCharacter(enemyId, enemyController, hitboxController);
        this.eventWriter.setText("Nemico " + name + " è apparso!");
    }


    public void getRandomEnemyFromFirstList() {
        final CharacterDeathObserver obs = id -> System.out.println("Nemico morto: " + id);

        List<List<Enemy>> allEnemies = collectionController.getEnemies();

        // if (allEnemies.isEmpty() || allEnemies.get(0).isEmpty()) {
        //     return null; // oppure lancia un'eccezione, a seconda del tuo caso
        // }


        List<Enemy> bossList = allEnemies.get(0);
        Random random = new Random();
        int randomIndex = random.nextInt(bossList.size());

        final Enemy boss = bossList.get(randomIndex);
        final BossController bossController = BossControllerFactory.createBossController(obs, generateUniqueCharacterId(), boss);
        //BossControllerFactory.createEnemyController(obs, generateUniqueCharacterId(), boss);
        bossController.attachCharacterAnimationPanel(
                (int) (frameWidth * DimensionResolver.BOSS.width()),
                (int) (frameHeight * DimensionResolver.BOSS.height()));

        final int spawnX = frameWidth; // Fuori dallo schermo a destra
        int spawnY = frameHeight/8;

        final int bossId = bossController.getId().number();
        final String typeFolder = bossController.getId().type().name();
        final String name = boss.getName();

        final HitboxController hitboxController = this.matchView.addEnemyGraphics(
                bossId,
                bossController.getGraphicalComponent(),
                spawnX, spawnY,
                typeFolder, name);
        addCharacter(bossId, bossController, hitboxController);

        this.eventWriter.setText("Inizio BossFight!");
    }


    public int generateRandomY(List<Integer> usedPositions, int frameHeight) {
        final int marginBottom = 300;
        final int availableHeight = frameHeight - marginBottom;
        final int minDistance = 40;
        final Random rand = new Random();

        int spawnY = 0;
        int attempts = 0;

        do {
            spawnY = rand.nextInt(availableHeight + 1);
            attempts++;
            if (attempts > 10)
                break; // evita loop infinito
        } while (isTooClose(spawnY, usedPositions, minDistance));

        return spawnY;
    }

    private boolean isTooClose(int candidate, List<Integer> positions, int minDistance) {
        for (int pos : positions) {
            if (Math.abs(candidate - pos) < minDistance)
                return true;
        }
        return false;
    }

    //setto tutti i personaggi allo stato che voglio
    public void setAllFSMsToState(final CharacterState newState) {
        for (CharacterFSM fsm : playerFSMs.values()) {
            fsm.setState(newState);
        }
    }


    public MatchView getMatchView() {
        return this.matchView;
    }


    public int getHitboxControllersCount() {
        return hitboxControllers.size();
    }


    public boolean hasBossInMap() {
        for (GenericCharacterController controller : hitboxControllers.keySet()) {
            if (controller.getId().type() == CardType.BOSS) {
                return true; // Trovato almeno un boss
            }
        }
        return false; // Nessun boss trovato
    }

}
