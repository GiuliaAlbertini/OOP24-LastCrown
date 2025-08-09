package it.unibo.oop.lastcrown.model.collision;

import it.unibo.oop.lastcrown.model.card.CardIdentifier;
import it.unibo.oop.lastcrown.model.card.CardType;
import it.unibo.oop.lastcrown.model.collision.api.Collidable;
import it.unibo.oop.lastcrown.model.collision.api.CollisionEvent;
import it.unibo.oop.lastcrown.model.collision.api.EventType;
import it.unibo.oop.lastcrown.model.collision.api.Hitbox;
import it.unibo.oop.lastcrown.model.collision.impl.CollisionResolverImpl;
import it.unibo.oop.lastcrown.utility.api.Point2D;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

final class TestCollisionResolverImpl {

    private static final int PLAYER_ID_1 = 1;
    private static final int PLAYER_ID_2 = 2;
    private static final int ENEMY_ID_1 = 10;
    private static final int ENEMY_ON_WALL_ID = 11;
    private static final int BOSS_ID = 50;
    private static final int WALL_ID = 100;
    private static final int UNINVOLVED_ID = 99;

    private CollisionResolverImpl resolver;

    @BeforeEach
    void setUp() {
        resolver = new CollisionResolverImpl();
    }

    @Test
    void testPairEngagementNotification() {
        final Collidable player = new StubCollidable(PLAYER_ID_1, CardType.RANGED);
        final Collidable enemy = new StubCollidable(ENEMY_ID_1, CardType.ENEMY);
        final CollisionEvent event = new StubCollisionEvent(EventType.RANGED, player, enemy);

        resolver.notify(event);

        assertTrue(resolver.hasOpponentPartner(PLAYER_ID_1, EventType.RANGED));
        assertTrue(resolver.hasOpponentPartner(ENEMY_ID_1, EventType.RANGED));

        final Optional<Integer> partnerIdOpt = resolver.getOpponentPartner(PLAYER_ID_1, EventType.RANGED);
        assertTrue(partnerIdOpt.isPresent());
        assertEquals(ENEMY_ID_1, partnerIdOpt.get());

        final Optional<Integer> noPartnerOpt = resolver.getOpponentPartner(UNINVOLVED_ID, EventType.RANGED);
        assertTrue(noPartnerOpt.isEmpty());

        assertFalse(resolver.hasOpponentPartner(PLAYER_ID_1, EventType.BOSS));
    }

    @Test
    void testGetAllEngagedCharacters() {
        final Collidable player1 = new StubCollidable(PLAYER_ID_1, CardType.MELEE);
        final Collidable player2 = new StubCollidable(PLAYER_ID_2, CardType.RANGED);
        final Collidable boss = new StubCollidable(BOSS_ID, CardType.BOSS);
        final Collidable wall = new StubCollidable(WALL_ID, CardType.WALL);
        final Collidable enemyOnWall = new StubCollidable(ENEMY_ON_WALL_ID, CardType.ENEMY);

        resolver.notify(new StubCollisionEvent(EventType.BOSS, player1, boss));
        resolver.notify(new StubCollisionEvent(EventType.BOSS, player2, boss));
        resolver.notify(new StubCollisionEvent(EventType.WALL, wall, enemyOnWall));

        final List<Integer> bossAttackers = resolver.getAllCharacterIdsInBossFight();
        assertTrue(bossAttackers.contains(PLAYER_ID_1));
        assertTrue(bossAttackers.contains(PLAYER_ID_2));
        assertEquals(2, bossAttackers.size());

        final List<Integer> wallHitters = resolver.getAllCharacterIdsInWallFight();
        assertTrue(wallHitters.contains(ENEMY_ON_WALL_ID));
        assertEquals(1, wallHitters.size());
    }

    @Test
    void testClearMethods() {
        final Collidable p1 = new StubCollidable(PLAYER_ID_1, CardType.RANGED);
        final Collidable e1 = new StubCollidable(ENEMY_ID_1, CardType.ENEMY);
        final Collidable p2 = new StubCollidable(PLAYER_ID_2, CardType.MELEE);
        final Collidable boss = new StubCollidable(BOSS_ID, CardType.BOSS);

        resolver.notify(new StubCollisionEvent(EventType.RANGED, p1, e1));
        resolver.notify(new StubCollisionEvent(EventType.BOSS, p2, boss));

        resolver.clearEngagementsById(PLAYER_ID_1, EventType.RANGED);
        assertFalse(resolver.hasOpponentPartner(PLAYER_ID_1, EventType.RANGED));
        assertTrue(resolver.hasOpponentPartner(PLAYER_ID_2, EventType.BOSS));

        resolver.notify(new StubCollisionEvent(EventType.RANGED, p1, e1));
        resolver.clearEngagementsByType(EventType.BOSS);
        assertTrue(resolver.hasOpponentPartner(PLAYER_ID_1, EventType.RANGED));
        assertFalse(resolver.hasOpponentPartner(PLAYER_ID_2, EventType.BOSS));

        resolver.clearAllPairEngagements();
        assertFalse(resolver.hasOpponentPartner(PLAYER_ID_1, EventType.RANGED));
    }

    private static final class StubHitbox implements Hitbox {

        @Override
        public Point2D getPosition() {
            return null;
        }

        @Override
        public void setPosition(final Point2D newPos) {
        }

        @Override
        public int getWidth() {
            return 0;
        }

        @Override
        public int getHeight() {
            return 0;
        }

        @Override
        public void setWidth(final int width) {
        }

        @Override
        public void setHeight(final int height) {
        }

        @Override
        public Point2D getCenter() {
            return null;
        }

        @Override
        public boolean checkCollision(final Hitbox other) {
            return false;
        }
    }

    private static final class StubCollidable implements Collidable {

        private final CardIdentifier id;
        private final Hitbox hitbox = new StubHitbox();

        StubCollidable(final int number, final CardType type) {
            this.id = new CardIdentifier(number, type);
        }

        @Override
        public Hitbox getHitbox() {
            return this.hitbox;
        }

        @Override
        public CardIdentifier getCardIdentifier() {
            return this.id;
        }
    }

    private static final class StubCollisionEvent implements CollisionEvent {

        private final EventType type;
        private final Collidable c1;
        private final Collidable c2;

        StubCollisionEvent(final EventType type, final Collidable c1, final Collidable c2) {
            this.type = type;
            this.c1 = c1;
            this.c2 = c2;
        }

        @Override
        public EventType getType() {
            return type;
        }

        @Override
        public Collidable getCollidable1() {
            return c1;
        }

        @Override
        public Collidable getCollidable2() {
            return c2;
        }
    }
}
