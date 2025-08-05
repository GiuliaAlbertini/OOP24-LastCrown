package it.unibo.oop.lastcrown.controller.collision.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JTextArea;

import it.unibo.oop.lastcrown.controller.characters.api.EnemyController;
import it.unibo.oop.lastcrown.controller.characters.impl.boss.BossControllerFactory;
import it.unibo.oop.lastcrown.controller.characters.impl.enemy.EnemyControllerFactory;
import it.unibo.oop.lastcrown.controller.collision.api.EnemySpawner;
import it.unibo.oop.lastcrown.controller.collision.api.HitboxController;
import it.unibo.oop.lastcrown.controller.collision.api.MatchController;
import it.unibo.oop.lastcrown.controller.user.api.CollectionController;
import it.unibo.oop.lastcrown.model.characters.api.Enemy;
import it.unibo.oop.lastcrown.view.dimensioning.DimensionResolver;

public final class EnemySpawnerImpl implements EnemySpawner {

    // I campi che abbiamo spostato
    private int spawnTimer = 0;
    private static final int SPAWN_INTERVAL = 5000;
    private final int roundIndex;
    private int enemyIndexInRound = 0;
    private final List<Integer> usedPositions = new ArrayList<>();
    private boolean roundSpawnComplete = false;

    // Le dipendenze di cui lo Spawner ha bisogno
    private final MatchController matchController;
    private final CollectionController collectionController;
    private final int frameWidth;
    private final int frameHeight;

    public EnemySpawnerImpl(
            final MatchController matchController,
            final CollectionController collectionController,
            final int frameWidth,
            final int frameHeight,
            final int initialRoundIndex) {
        this.matchController = matchController;
        this.collectionController = collectionController;
        this.frameWidth = frameWidth;
        this.frameHeight = frameHeight;
        this.roundIndex = initialRoundIndex;
    }

    @Override
    public void update(final int deltaTime) {
        spawnTimer += deltaTime;
        final List<List<Enemy>> allEnemies = collectionController.getEnemies();
        if (roundIndex < allEnemies.size()) {
            final List<Enemy> currentRound = allEnemies.get(roundIndex);

            if (spawnTimer >= SPAWN_INTERVAL && enemyIndexInRound < currentRound.size()) {
                // Ora chiama il suo stesso metodo privato
                this.spawnRandomEnemy(currentRound.get(enemyIndexInRound), enemyIndexInRound, currentRound.size());
                enemyIndexInRound++;
                spawnTimer = 0;
            }
            if (enemyIndexInRound >= currentRound.size()) {
                roundSpawnComplete = true;
            }
        }
    }

    private void spawnRandomEnemy(final Enemy enemy, int enemyIndex, int totalEnemies) {
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
        ((JTextArea) matchController.getEventWriter()).setText("Nemico " + name + " è apparso!");
    }

    private int generateRandomY(List<Integer> usedPositions, int frameHeight) {
        final int marginBottom = 300;
        final int availableHeight = frameHeight - marginBottom;
        final int minDistance = 40;
        final Random rand = new Random();
        int spawnY;
        int attempts = 0;
        do {
            spawnY = rand.nextInt(availableHeight + 1);
            if (++attempts > 10)
                break;
        } while (isTooClose(spawnY, usedPositions, minDistance));
        return spawnY;
    }

    private boolean isTooClose(int candidate, List<Integer> positions, int minDistance) {
        return positions.stream().anyMatch(pos -> Math.abs(candidate - pos) < minDistance);
    }

    @Override
    public void spawnBoss() {
        final List<List<Enemy>> allEnemies = collectionController.getEnemies();
        final List<Enemy> bossList = allEnemies.get(0);
        final Random random = new Random();
        final Enemy boss = bossList.get(random.nextInt(bossList.size()));

        final var bossController = BossControllerFactory.createBossController(
                matchController.generateUniqueCharacterId(), boss);

        bossController.attachCharacterAnimationPanel(
                (int) (frameWidth * DimensionResolver.BOSS.width()),
                (int) (frameHeight * DimensionResolver.BOSS.height()));

        final int spawnX = frameWidth;
        final int spawnY = 50;
        final String typeFolder = bossController.getId().type().name();
        final String name = boss.getName();

        final HitboxController hitboxController = matchController.getMatchView().addEnemyGraphics(
                bossController.getId().number(),
                bossController.getGraphicalComponent(),
                spawnX, spawnY,
                typeFolder, name);

        matchController.addCharacter(bossController.getId().number(), bossController, hitboxController);
        //matchController.setBossActive();
        ((JTextArea) matchController.getEventWriter()).setText("Inizio BossFight!");
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