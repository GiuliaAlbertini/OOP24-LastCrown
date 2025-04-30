package it.unibo.oop.lastcrown.view.characters;

import java.awt.image.BufferedImage;
import java.util.List;
import java.util.logging.Logger;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import it.unibo.oop.lastcrown.view.Keyword;
import it.unibo.oop.lastcrown.view.characters.api.CharacterAnimationPanel;

/**
 * Handle the different animation of a character.
 */
public class AnimationHandler {
    private static final Logger LOG = Logger.getLogger(AnimationHandler.class.getName());
    private static final int JUMPING_SIZE = 3;
    private static final int MOVEMENT_PIXELS = 5;
    private static final int TIME = 100;
    private final CharacterAnimationPanel panel;
    private volatile Double speedMultiplier;
    private volatile boolean stop;

    /**
     * @param panel the character animation panel linked to this handler
     * @param speedMultiplier the speedMultiplier applied to the animations
     */
    @SuppressFBWarnings(
    value = "EI2",
    justification = "The animation handler has to modify the given animation panel"
        + ", for instance it has to modify the horizontal position of the panel"
        + "during a run animation"
)
    public AnimationHandler(final CharacterAnimationPanel panel,
     final Double speedMultiplier) {
        this.speedMultiplier = speedMultiplier;
        this.panel = panel;
    }

    /**
     * Set the animation speed multiplier.
     * @param newValue new speed multiplier
     */
    public void setSpeedMultiplier(final Double newValue) {
        this.speedMultiplier = newValue;
    } 

    /**
     * Start a single animation sequence (to do a loop call this method multiple times).
     * @param frames the frames of the specific animation
     * @param keyword the keyword of the specific animation
     */
    public void startAnimationSequence(final List<BufferedImage> frames, final Keyword keyword) {
        switch (keyword) {
            case Keyword.STOP, Keyword.ATTACK, Keyword.DEATH:
                for (int i = 0; i < frames.size() && !this.stop; i++) {
                    panel.setCharacterImage(frames.get(i));
                    try {
                        Thread.sleep(TIME);
                    } catch (final InterruptedException e) {
                        LOG.fine("Error occured during the static animations");
                    }
                }
                break;

            case Keyword.RUN_RIGHT, Keyword.RETREAT:
                this.startRunAnimation(frames, (int) (MOVEMENT_PIXELS * this.speedMultiplier));
                break;

            case RUN_LEFT:
                this.startRunAnimation(frames, -(int) (MOVEMENT_PIXELS * this.speedMultiplier));
                break;

            case Keyword.JUMPUP:
                this.startJumpAnimation(frames, 0, -(int) (this.speedMultiplier * JUMPING_SIZE));
                break;

            case Keyword.JUMPDOWN:
                this.startJumpAnimation(frames, 0, (int) (this.speedMultiplier * JUMPING_SIZE));
                break;

            case Keyword.JUMPFORWARD:
                final List<BufferedImage> jumpUpFrames = frames.subList(0, frames.size() / 2);
                final List<BufferedImage> jumpDownFrames = frames.subList(frames.size() / 2, frames.size());
                this.startJumpAnimation(jumpUpFrames, (int) (this.speedMultiplier * JUMPING_SIZE),
                -(int) (this.speedMultiplier * JUMPING_SIZE));
                this.startJumpAnimation(jumpDownFrames, (int) (this.speedMultiplier * JUMPING_SIZE),
                 +(int) (this.speedMultiplier * JUMPING_SIZE));
                break;
        }
    }

    /**
     * Start a single run sequence with the specified frames. 
     * The horizontal movement between one frames and the other is decided by variation given.
     * @param frames the specific run animation frames
     * @param variation the horizontal movement between one frame and the other
     */
    private void startRunAnimation(final List<BufferedImage> frames, final int variation) {
        for (int i = 0; i < frames.size() && !this.stop; i++) {
            panel.setCharacterImage(frames.get(i));
            panel.setLocation(panel.getX() + variation, panel.getY());
            panel.setHealthBarPosition();
            try {
                Thread.sleep(TIME);
            } catch (final InterruptedException e) {
                LOG.fine("Error occured during a run animation");
            }
        }
    }

    /**
      * Start a single jump sequence with the specified frames. 
     * The horizontal and vertical movement between one frames and the other is decided by variation given.
     * @param frames the specific jump animation frames
     * @param xVariation the horizontal movement between one frame and the other
     * @param yVariation the vertical movement between one frame and the other
     */
    private void startJumpAnimation(final List<BufferedImage> frames,
     final int xVariation, final int yVariation) {
        for (int i = 0; i < frames.size() && !this.stop; i++) {
            panel.setCharacterImage(frames.get(i));
            panel.setLocation(panel.getX() + xVariation, panel.getY() + yVariation);
            panel.setHealthBarPosition();
            try {
                Thread.sleep(TIME);
            } catch (final InterruptedException e) {
                LOG.fine("Error occured during a jump animation");
            }
        }
    }

    /**
     * stops the execution of the actual animation. Decreases very much the response lag.
     */
    public void stop() {
        this.stop = true;
    }

    /**
     * Communicate to the handler that it can restart doing the animation sequence.
     */
    public void start() {
        this.stop = false;
    }

}
