package it.unibo.oop.lastcrown.controller.collision.api;

public interface EnemySpawner {
    void update(int deltaTime);

    void spawnBoss();

    boolean isRoundSpawnComplete();

    int getRoundIndex();
}