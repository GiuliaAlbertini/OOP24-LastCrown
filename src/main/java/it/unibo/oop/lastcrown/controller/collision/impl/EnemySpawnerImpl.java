package it.unibo.oop.lastcrown.controller.collision.impl;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

import it.unibo.oop.lastcrown.controller.characters.api.EnemyController;
import it.unibo.oop.lastcrown.controller.characters.impl.boss.BossControllerFactory;
import it.unibo.oop.lastcrown.controller.characters.impl.enemy.EnemyControllerFactory;
import it.unibo.oop.lastcrown.controller.collision.api.EnemySpawner;
import it.unibo.oop.lastcrown.controller.collision.api.HitboxController;
import it.unibo.oop.lastcrown.controller.collision.api.MatchController;
import it.unibo.oop.lastcrown.model.characters.api.Enemy;
import it.unibo.oop.lastcrown.view.dimensioning.DimensionResolver;

/** Implementation for the {@link EnemySpawner}. */
public final class EnemySpawnerImpl implements EnemySpawner {

    private static final int SPAWN_INTERVAL = 5000;
    private static final SecureRandom GENERATOR = new SecureRandom();

    private int spawnTimer;
    private final int roundIndex;
    private int enemyIndexInRound;
    private final List<Integer> usedPositions = new ArrayList<>();
    private boolean roundSpawnComplete;

    private final MatchController matchController;
    private final List<List<Enemy>> enemyList;
    private final int frameWidth;
    private final int frameHeight;

    /**
     * Instantiates an enemy spawner.
     * @param matchController the current match controller.
     * @param enemyList the current enemy list.
     * @param frameWidth the current frame width.
     * @param frameHeight the current frame height.
     * @param initialRoundIndex the index of the current round.
     */
    public EnemySpawnerImpl(
            final MatchController matchController,
            final List<List<Enemy>> enemyList,
            final int frameWidth,
            final int frameHeight,
            final int initialRoundIndex) {
        this.enemyList = enemyList;
        this.matchController = matchController;
        this.frameWidth = frameWidth;
        this.frameHeight = frameHeight;
        this.roundIndex = initialRoundIndex;
    }

    @Override
    public void update(final int deltaTime) {
        spawnTimer += deltaTime;
        final List<List<Enemy>> allEnemies = enemyList;
        if (roundIndex < allEnemies.size()) {
            final List<Enemy> currentRound = allEnemies.get(roundIndex);

            if (spawnTimer >= SPAWN_INTERVAL && enemyIndexInRound < currentRound.size()) {
                this.spawnRandomEnemy(currentRound.get(enemyIndexInRound));
                enemyIndexInRound++;
                spawnTimer = 0;
            }
            if (enemyIndexInRound >= currentRound.size()) {
                roundSpawnComplete = true;
            }
        }
    }

    private void spawnRandomEnemy(final Enemy enemy) {
        final EnemyController enemyController = EnemyControllerFactory.createEnemyController(
                matchController.generateUniqueCharacterId(), enemy);

        enemyController.attachCharacterAnimationPanel(
                (int) (frameWidth * DimensionResolver.CHAR.width()),
                (int) (frameHeight * DimensionResolver.CHAR.height()));

        final int spawnX = frameWidth;
        final int spawnY = generateRandomY(usedPositions, frameHeight);
        usedPositions.add(spawnY);

        final String typeFolder = enemyController.getId().type().name();
        final String name = enemy.getName();

        final HitboxController hitboxController = this.matchController.getMatchView().addEnemyGraphics(
                enemyController.getId().number(),
                enemyController.getGraphicalComponent(),
                spawnX, spawnY,
                typeFolder, name);

        matchController.addCharacter(enemyController.getId().number(), enemyController, hitboxController);
        matchController.updateEventText("Nemico" + name + "è apparso");

    }

    private int generateRandomY(final List<Integer> usedPositions, final int frameHeight) {
        final int marginBottom = 300;
        final int availableHeight = frameHeight - marginBottom;
        final int minDistance = 40;
        int spawnY;
        int attempts = 0;
        do {
            spawnY = GENERATOR.nextInt(availableHeight + 1);
            attempts++;
            if (attempts > 10) {
                break;
            }
        } while (isTooClose(spawnY, usedPositions, minDistance));
        return spawnY;
    }

    private boolean isTooClose(final int candidate, final List<Integer> positions, final int minDistance) {
        return positions.stream().anyMatch(pos -> Math.abs(candidate - pos) < minDistance);
    }

    @Override
    public void spawnBoss() {
        final List<List<Enemy>> allEnemies = enemyList;
        final List<Enemy> bossList = allEnemies.get(0);
        final Enemy boss = bossList.get(GENERATOR.nextInt(bossList.size()));

        final var bossController = BossControllerFactory.createBossController(
                matchController.generateUniqueCharacterId(), boss);

        bossController.attachCharacterAnimationPanel(
                (int) (frameWidth * DimensionResolver.BOSS.width()),
                (int) (frameHeight * DimensionResolver.BOSS.height()));

        final int spawnX = frameWidth;
        final int spawnY = frameHeight / 9;
        final String typeFolder = bossController.getId().type().name();
        final String name = boss.getName();

        final HitboxController hitboxController = matchController.getMatchView().addEnemyGraphics(
                bossController.getId().number(),
                bossController.getGraphicalComponent(),
                spawnX, spawnY,
                typeFolder, name);

        matchController.addCharacter(bossController.getId().number(), bossController, hitboxController);
        matchController.setBossActive();
        matchController.updateEventText("Inizio BossFight");
        matchController.getMatchView().notifyBossFight(true);

        matchController.handleBossMusic();
    }

    @Override
    public boolean isRoundSpawnComplete() {
        return this.roundSpawnComplete;
    }

    @Override
    public int getRoundIndex() {
        return this.roundIndex;
    }

}
