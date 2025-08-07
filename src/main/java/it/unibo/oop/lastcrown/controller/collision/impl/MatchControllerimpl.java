package it.unibo.oop.lastcrown.controller.collision.impl;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.swing.JComponent;

import it.unibo.oop.lastcrown.audio.SoundEffect;
import it.unibo.oop.lastcrown.audio.SoundTrack;
import it.unibo.oop.lastcrown.audio.engine.AudioEngine;
import it.unibo.oop.lastcrown.controller.app_managing.api.MainController;
import it.unibo.oop.lastcrown.controller.characters.api.GenericCharacterController;
import it.unibo.oop.lastcrown.controller.characters.api.HeroController;
import it.unibo.oop.lastcrown.controller.characters.api.PlayableCharacterController;
import it.unibo.oop.lastcrown.controller.characters.api.Wall;
import it.unibo.oop.lastcrown.controller.characters.impl.hero.HeroControllerFactory;
import it.unibo.oop.lastcrown.controller.characters.impl.playablecharacter.PlCharControllerFactory;
import it.unibo.oop.lastcrown.controller.characters.impl.wall.WallFactory;
import it.unibo.oop.lastcrown.controller.collision.api.EntityEngagementManager;
import it.unibo.oop.lastcrown.controller.collision.api.EntityStateManager;
import it.unibo.oop.lastcrown.controller.collision.api.HitboxController;
import it.unibo.oop.lastcrown.controller.collision.api.MatchController;
import it.unibo.oop.lastcrown.controller.collision.api.SpellManager;
import it.unibo.oop.lastcrown.controller.collision.api.EntityTargetingSystem;
import it.unibo.oop.lastcrown.controller.collision.impl.eventcharacters.CharacterState;
import it.unibo.oop.lastcrown.controller.user.api.CollectionController;
import it.unibo.oop.lastcrown.model.card.CardIdentifier;
import it.unibo.oop.lastcrown.model.card.CardType;
import it.unibo.oop.lastcrown.model.characters.api.Hero;
import it.unibo.oop.lastcrown.model.characters.api.PlayableCharacter;
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
import it.unibo.oop.lastcrown.utility.Constant;
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
import it.unibo.oop.lastcrown.view.menu.api.MainView;

/** Implementation for the {@link MatchController}. */
public final class MatchControllerimpl implements MatchController {
    private final List<Pair<String, PlayableCharacterController>> cardList = new ArrayList<>();
    private final CollisionManager collisionManager = new CollisionManagerImpl();
    private final CollisionResolver collisionResolver;
    private int nextId;
    private MatchView matchView;
    private Hero hero;
    private Wall wall;
    private int coins;
    private final int frameWidth;
    private final int frameHeight;
    private CompleteCollection collection;
    private HeroController heroController;
    private final MainController mainController;
    private boolean bossActive;
    private final MainView mainView;
    private boolean alreadyDone;
    private final EntityStateManager entityStateManager;
    private final EntityTargetingSystem radiusScanner;
    private final EnemySpawnerImpl enemySpawner;
    private final EntityEngagementManager engagementManager;
    private final SpellManager spellManager;

    /**
     * Instantiates the match controller. TODO - complete param tags.
     * @param mainController
     * @param frameWidth
     * @param frameHeight
     * @param heroId
     * @param collectionController
     * @param mainView
     * @param enemyList
     */
    public MatchControllerimpl(final MainController mainController, final int frameWidth, final int frameHeight,
            final CardIdentifier heroId, final CollectionController collectionController, final MainView mainView,
            final int enemyList) {
        this.nextId = 1;
        this.frameHeight = frameHeight;
        this.frameWidth = frameWidth;
        this.mainController = mainController;
        this.mainView = mainView;

        initializeHero(heroId, collectionController);
        initializeWall();

        this.collisionResolver = new CollisionResolverImpl();
        this.collisionManager.addObserver(collisionResolver);
        this.engagementManager = new EntityEngagementManagerImpl(this);
        this.entityStateManager = new EntityStateManagerImpl();
        this.radiusScanner = new EntityTargetingSystemImpl(this.entityStateManager.getHitboxControllersMap(), this,
                                                           collisionResolver);
        this.enemySpawner = new EnemySpawnerImpl(this, collectionController.getEnemies(), this.frameWidth,
                                                 this.frameHeight, enemyList);
        this.spellManager = new SpellManagerImpl(this, this.collection, this.frameWidth);
    }

    private void initializeHero(final CardIdentifier heroId, final CollectionController collectionController) {
        this.collection = collectionController.getCompleteCollection();
        this.hero = this.collection.getHero(heroId).get();
        this.heroController = HeroControllerFactory.createHeroController(
            generateUniqueCharacterId(),
            hero
        );

        final int heroWidth = (int) (frameWidth * DimensionResolver.HERO.width());
        final int heroHeight = (int) (frameHeight * DimensionResolver.HERO.height());
        heroController.attachCharacterAnimationPanel(heroWidth, heroHeight);
    }

    private void initializeWall() {
        final int wallX = frameWidth / 2;
        final int wallY = (int) (frameHeight * DimensionResolver.UTILITYZONE.height());
        this.wall = WallFactory.createWall(hero.getWallAttack(), hero.getWallHealth(),
                                        Constant.WALL_ID, wallX, wallY, Optional.empty());
    }

    @Override
    public void newMatchView(final MatchView matchView) {
        this.matchView = matchView;
        updateCoinsDisplay();
        setupHeroInView();
        createWallHitbox(matchView);
    }

    private void setupHeroInView() {
        final HitboxController heroHitbox = this.matchView.addHeroGraphics(
                this.heroController.getId().number(),
                this.heroController.getGraphicalComponent(),
                this.heroController.getId().type().get(),
                this.hero.getName());
        this.heroController.setNextAnimation(Keyword.STOP);
        this.heroController.showNextFrame();
        addCharacter(this.heroController.getId().number(), heroController, heroHitbox);
    }

    private void createWallHitbox(final MatchView matchView) {
        final Point2D pos = new Point2DImpl(matchView.getWallCoordinates().getX(),
                                            matchView.getWallCoordinates().getY());
        final Hitbox wallHitbox = new HitboxImpl(matchView.getWallSize().width, matchView.getWallSize().height, pos);
        final HitboxPanel wallHitboxPanel = new HitboxPanelImpl(wallHitbox);
        final var wallHitboxController = new HitboxControllerImpl(wallHitbox, wallHitboxPanel, null, null);
        this.wall.setHitbox(wallHitbox);
        this.matchView.addWallPanel(wallHitboxController);
    }

    private void updateCoinsDisplay() {
        matchView.setCoins(this.coins);
    }

    @Override
    public void updateEventText(final String text) {
        matchView.setEventText(text);
    }

    @Override
    public int getCurrentCoins() {
        return this.coins;
    }

    @Override
    public Wall getWall() {
        return this.wall;
    }

    @Override
    public boolean isEnemyBeyondFrame(final int enemyId) {
        return this.entityStateManager.isEnemyBeyondFrame(enemyId, this.frameWidth);
    }

    @Override
    public void setRadiusPlayerInMap() {
        this.entityStateManager.setRadiusForAllPlayers(Constant.UPGRADE_RADIUS_MELEE, Constant.UPGRADE_RADIUS_RANGED);
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

        this.entityStateManager.getHitboxForController(controller).ifPresent(hitboxController -> {
            hitboxController.setnewPosition(newX, newY);
            hitboxController.updateView();
            hitboxController.setVisibile(true);
        });

        component.repaint();
    }

    @Override
    public void notifyCollisionObservers(final CollisionEvent event) {
        collisionManager.notify(event);
    }

    @Override
    public void update(final int deltaTime) {
        this.spellManager.update(deltaTime);
        this.enemySpawner.update(deltaTime);
        entityStateManager.updateAll(deltaTime);

        if (this.matchView != null) {
            this.matchView.getPanel().repaint();
        }
    }

    @Override
    public Optional<GenericCharacterController> getCharacterControllerById(final int id) {
        return this.entityStateManager.getCharacterControllerById(id);
    }

    @Override
    public Optional<HitboxController> getCharacterHitboxById(final int id) {
        return this.entityStateManager.getCharacterHitboxById(id);
    }

    @Override
    public void removeCharacterCompletelyById(final int characterId) {
        this.matchView.removeGraphicComponent(characterId);
        this.entityStateManager.removeCharacterById(characterId);

    }

    @Override
    public int generateUniqueCharacterId() {
        this.nextId = this.nextId + 3;
        return this.nextId;
    }

    @Override
    public boolean isEntityEngaged(final int entityId) {
        return this.engagementManager.isEntityEngaged(entityId);
    }

    @Override
    public boolean engageEnemy(final int enemyId, final int playerId) {
        return this.engagementManager.engageEnemy(enemyId, playerId);
    }

    @Override
    public boolean retreat() {
        return !hasEntityTypeInMap(CardType.BOSS) && getWall().getCurrentHealth() <= 0;
    }

    @Override
    public boolean releaseEngagementFor(final int characterId) {
        return this.engagementManager.releaseEngagementFor(characterId);

    }

    @Override
    public Set<EnemyEngagement> getEngagedEnemies() {
        return this.engagementManager.getEngagedEnemies();
    }

    @Override
    public boolean isEngagedWithDead(final int characterId) {
        return this.engagementManager.isEngagedWithDead(characterId);

    }

    @Override
    public int getEngagedCounterpart(final int characterId) {
        return this.engagementManager.getEngagedCounterpart(characterId);

    }

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

    @Override
    public boolean isEnemyDead(final int enemyId) {
        return this.entityStateManager.getCharacterControllerById(enemyId)
                .map(controller -> controller.getId().type() != CardType.BOSS && controller.isDead())
                .orElse(false);
    }

    @Override
    public void notifyClicked(final int x, final int y) {
        if (!cardList.isEmpty() && !hasEntityTypeInMap(CardType.BOSS)) {
            final Pair<String, PlayableCharacterController> selected = cardList.get(cardList.size() - 1);
            final int id = selected.get2().getId().number();
            final PlayableCharacterController playerController = selected.get2();
            final String typeFolder = playerController.getId().type().get();
            final String name = selected.get1();

            playerController.attachCharacterAnimationPanel(
                    (int) (frameWidth * DimensionResolver.CHAR.width()),
                    (int) (frameHeight * DimensionResolver.CHAR.height()));

            final HitboxController hitboxController = this.matchView.addGenericGraphics(id,
                    playerController.getGraphicalComponent(), x, y, typeFolder, name);
            addCharacter(selected.get2().getId().number(), playerController, hitboxController);
            updateEventText(name + "schierato!");
        } else {
            this.spellManager.castSpell(x, y);
        }
    }

    @Override
    public void notifyButtonPressed(final CardIdentifier id) {
        if (id.type() == CardType.MELEE || id.type() == CardType.RANGED) {
            final PlayableCharacter player = this.collection.getPlayableCharacter(id).get();
            final PlayableCharacterController playerController = PlCharControllerFactory.createPlCharController(
                    generateUniqueCharacterId(), player);
            cardList.add(new Pair<>(player.getName(), playerController));
        } else if (id.type() == CardType.SPELL) {
            cardList.clear();
            this.spellManager.handleSpellSelection(id);
        }
    }

    @Override
    public void addCharacter(final int n, final GenericCharacterController controller,
            final HitboxController hitboxController) {
        final int id = controller.getId().number();
        final CharacterFSM fsm = new CharacterFSM(controller, this, radiusScanner, this.collisionResolver);
        this.entityStateManager.addCharacter(id, controller, hitboxController, fsm);
    }

    @Override
    public HitboxController setupCharacter(final JComponent charComp, final String typeFolder, final String name,
        final boolean isPlayable, final int x, final int y) {
        final Dimension size = charComp.getPreferredSize();
        final Hitbox hitbox = new HitboxImpl(Constant.HITBOX_WIDTH, Constant.HITBOX_HEIGHT, new Point2DImpl(x, y));
        final HitboxPanel hitboxPanel = new HitboxPanelImpl(hitbox);

        final String path = CharacterPathLoader.loadHitboxPath(typeFolder, name);

        final BufferedImage image = ImageLoader.getImage(path, (int) size.getWidth(), (int) size.getHeight());
        final HitboxMaskBounds bounds = new HitboxMaskBounds(hitbox, charComp, hitboxPanel);
        bounds.calculateHitboxCenter(image);
        final HitboxController hitboxController = new HitboxControllerImpl(hitbox, hitboxPanel, Optional.of(bounds),
                null);

        if (isPlayable) {
            final double radiusValue = switch (typeFolder) {
                case "melee" -> Constant.DEFAULT_MELEE_RADIUS;
                case "ranged" -> Constant.DEFAULT_RANGED_RADIUS;
                default -> Constant.DEFAULT_BOSS_RADIUS;
            };

            final Radius radius = new RadiusImpl(hitbox, radiusValue);
            hitboxController.setRadius(radius);
            final RadiusPanel radiusPanel = new RadiusPanelImpl(radius, bounds);
            hitboxController.setRadius(radius);
            hitboxController.setRadiusPanel(radiusPanel);
        }
        return hitboxController;
    }

    @Override
    public void notifyPauseStart() {
        mainController.getMatchStartObserver().stopMatchLoop();
    }

    @Override
    public void notifyPauseEnd() {
        mainController.getMatchStartObserver().resumeMatchLoop();
    }

    @Override
    public JComponent getWallHealthBar() {
        return this.wall.getHealthBarComponent();
    }

    @Override
    public void spawnRandomBossFromFirstList() {
        this.enemySpawner.spawnBoss();
    }

    @Override
    public void setAllFSMsToState(final CharacterState newState) {
        this.entityStateManager.setAllFSMsToState(newState);
    }

    @Override
    public MatchView getMatchView() {
        return this.matchView;
    }

    @Override
    public void setBossActive() {
        this.bossActive = true;
    }

    @Override
    public void matchResult() {
        if (!this.alreadyDone) {
            if (!hasEntityTypeInMap(CardType.HERO)) {
                this.alreadyDone = true;
                mainView.updateAccount(this.coins, false);
                AudioEngine.playEffect(SoundEffect.LOSE);
                matchView.disposeDefeat();
                this.matchView.notifyBossFight(false);
                this.mainController.getMatchStartObserver().stopMatchLoop();
            } else if (!hasEntityTypeInMap(CardType.BOSS) && bossActive) {
                this.alreadyDone = true;
                mainView.updateAccount(this.coins, true);
                AudioEngine.playEffect(SoundEffect.WIN);
                matchView.disposeVictory();
                this.matchView.notifyBossFight(false);
                this.mainController.getMatchStartObserver().stopMatchLoop();
            }
        }
    }

    @Override
    public List<GenericCharacterController> getCharactersByType(final CardType cardType) {
        return this.entityStateManager.getCharactersByType(cardType);
    }

    @Override
    public boolean isRoundSpawnComplete() {
        return this.enemySpawner.isRoundSpawnComplete();
    }

    @Override
    public void rewardCoinsForRound(final boolean bossFight) {
        final int baseReward = 50;
        final int reward = baseReward - this.enemySpawner.getRoundIndex() * 10;
        this.coins += reward;
        updateCoinsDisplay();
        updateEventText((bossFight ? "Boss sconfitto! " : "") + "Hai guadagnato " + reward + "!");
    }

    @Override
    public void halveHeroMaxHealth() {
        final int currentHealth = this.heroController.getMaximumHealthValue();
        final int reductionRate = 80;
        final int newHealth = currentHealth - (currentHealth * reductionRate / 100);
        if (currentHealth > newHealth) {
            this.heroController.takeHit(newHealth);
        }
    }

    @Override
    public boolean hasEntityTypeInMap(final CardType type) {
        return this.entityStateManager.hasEntityWithType(type);
    }

    @Override
    public boolean isPlayerInState(final PlayableCharacterController player, final CharacterState stateToCompare) {
        final CharacterFSM fsm = this.entityStateManager.getFSM(player.getId().number());
        return fsm != null && fsm.getCurrentState() == stateToCompare;
    }

    @Override
    public void handleBossMusic() {
        if (!alreadyDone) {
            AudioEngine.playSoundTrack(SoundTrack.BOSSFIGHT);
        }
    }
}
