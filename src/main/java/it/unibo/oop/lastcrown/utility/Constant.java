package it.unibo.oop.lastcrown.utility;

/** Class containing game constants. */
public final class Constant {

    /**The number one. */
    public static final int ONENEG = -1;

    /** The base width of the hitbox. */
    public static final int HITBOX_WIDTH = 10;

    /** The base height of the hitbox. */
    public static final int HITBOX_HEIGHT = 10;

    /** The default melee character radius. */
    public static final int DEFAULT_MELEE_RADIUS = 250;

    /** The default ranged character radius. */
    public static final int DEFAULT_RANGED_RADIUS = 475;

    /** The default boss radius. */
    public static final int DEFAULT_BOSS_RADIUS = 400;

    /** The default upgraded melee character radius. */
    public static final int UPGRADE_RADIUS_MELEE = 400;

    /** The default upgraded ranged character radius. */
    public static final int UPGRADE_RADIUS_RANGED = 900;

    /** The default wall in-game ID. */
    public static final int WALL_ID = 1000;

    /** The default speed in-game. */
    public static final double SPEED = 20.0;

    /** The time divider for delta time calculations. */
    public static final double TIME_DIVIDER = 1000.0;

    /** The divider for Bezier curve calculations. */
    public static final double BEZIER_DIVIDER = 10.0;

    /** The limit for Bezier curve calculations. */
    public static final double BEZIER_LIMIT = 1.0;

    /** The limit for the number of steps in Bezier curve calculations. */
    public static final int STEP_LIMIT = 10;

    /** The starting step for Bezier curve calculations. */
    public static final int STEP_START = 1;

    /** The size of each step in Bezier curve calculations. */
    public static final int STEP_SIZE = 1;

    /** The movement speed of the player character. */
    public static final int PLAYER_SPEED = 2;

    /** The movement speed of enemy characters. */
    public static final int ENEMY_SPEED = 2;

    /** The rounding amount applied when adjusting positions or values. */
    public static final int ROUNDING_AMOUNT = 5;

    /** The time interval (in milliseconds) between enemy spawns. */
    public static final int SPAWN_INTERVAL = 5000;

    /** The category key used for health-related effects or upgrades. */
    public static final String HEALTH_CATEGORY = "health";

    /** The category key used for attack-related effects or upgrades. */
    public static final String ATTACK_CATEGORY = "attack";

    /** The category key used for speed multiplier effects or upgrades. */
    public static final String SPEED_CATEGORY = "speedMultiplier";

    /** The multiplier value applied to effects targeting enemies. */
    public static final int ENEMY_EFFECT_MULTIPLIER = -1;

    private Constant() {
    }
}
