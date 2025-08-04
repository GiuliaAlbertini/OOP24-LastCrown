package it.unibo.oop.lastcrown.audio.engine;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import it.unibo.oop.lastcrown.audio.SoundEffect;
import it.unibo.oop.lastcrown.audio.SoundTrack;
import it.unibo.oop.lastcrown.audio.loader.ClipLoader;
import it.unibo.oop.lastcrown.audio.loader.TracksPathLoader;

/**
 * A standard AudioEngine.
 */
public final class AudioEngine {
    private static final Logger LOG = Logger.getLogger(AudioEngine.class.getName());
    private static final int FADE_OUT = 1000;
    private static final ExecutorService EXECUTOR = Executors.newSingleThreadExecutor();
    private static SoundTrack actual;
    private static Clip introClip;
    private static Clip loopClip;
    private AudioEngine() { }
    /**
     * Plays a soundtrack.
     * @param soundTrack the specific soundtrack to play
     */
    public static synchronized void playSoundTrack(final SoundTrack soundTrack) {
        actual = soundTrack;
        final CompletableFuture<Void> task = CompletableFuture.runAsync(() -> {
            try {
                synchronized (AudioEngine.class) {
                    if (introClip != null && introClip.isRunning()) {
                        fadeOutAndStop(introClip);
                    }
                    if (loopClip != null && loopClip.isRunning()) {
                        fadeOutAndStop(loopClip);
                    }
                    introClip = ClipLoader.loadClip(TracksPathLoader.loadIntroPath(soundTrack));
                    loopClip = ClipLoader.loadClip(TracksPathLoader.loadLoopPath(soundTrack));
                }

                if (introClip != null) {
                    introClip.addLineListener(e -> {
                    if (e.getType() == LineEvent.Type.STOP) {
                        introClip.close();
                        loopClip.setFramePosition(0);
                        loopClip.loop(Clip.LOOP_CONTINUOUSLY);
                    }
                    });
                introClip.start();
                } else {
                    loopClip.setFramePosition(0);
                    loopClip.loop(Clip.LOOP_CONTINUOUSLY);
                }
            } catch (final UnsupportedAudioFileException | IOException | LineUnavailableException e) {
                LOG.fine("Exception occurred when playing soundtrack: " + e.getMessage());
            }
        }, EXECUTOR);

        task.whenComplete((result, execution) -> {
            if (execution != null) {
                LOG.fine("Error occurred during the soundtrack task");
            }
        });
    }

    /**
     * Plays a sound effect.
     * @param soundEffect the specific sound effecr to play
     */
    public static synchronized void playEffect(final SoundEffect soundEffect) {
        final CompletableFuture<Void> task = CompletableFuture.runAsync(() -> {
            try {
                stopCurrent();
                final Clip clip = ClipLoader.loadClip(TracksPathLoader.loadSoundEffectPath(soundEffect));
                clip.start();
                clip.addLineListener(e -> {
                    if (e.getType() == LineEvent.Type.STOP) {
                        ClipLoader.closeClip(clip);
                    }
                });
            } catch (final UnsupportedAudioFileException | IOException | LineUnavailableException e) {
                LOG.fine("Exception occurred when playing sound effect: " + e.getMessage());
            } 
        }, EXECUTOR);

        task.whenComplete((result, execution) -> {
            if (execution != null) {
                LOG.fine("Error occurred during the sound effect task");
            }
        });
    }

    private static void stopCurrent() {
        ClipLoader.closeClip(introClip);
        ClipLoader.closeClip(loopClip);
    }

    /**
     * Stops the current playing soundtrack.
     */
    public static void stopTrack() {
        final CompletableFuture<Void> task = CompletableFuture.runAsync(() -> {
                stopCurrent();
        }, EXECUTOR);

        task.whenComplete((result, execution) -> {
            if (execution != null) {
                LOG.fine("Error occurred during the sound effect task");
            }
        });
    }

    private static void fadeOutAndStop(final Clip clip) {
        if (clip != null && clip.isRunning()) {
            try {
                final FloatControl volume = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                final float initialVolume = volume.getValue();
                final float minVolume = volume.getMinimum();
                final int steps = 20;
                final long sleepTime = FADE_OUT / steps;
                final float delta = (initialVolume - minVolume) / steps;

                for (int i = 0; i < steps; i++) {
                    volume.setValue(initialVolume - delta * i);
                    Thread.sleep(sleepTime);
                }
                ClipLoader.closeClip(clip);
            } catch (final IllegalArgumentException | InterruptedException e) {
                LOG.fine("Exception occurred during fade out process: " + e.getMessage());
                ClipLoader.closeClip(clip);
            }
        }
    }

    /**
     * @return the actual playing soundtrack
     */
    public static SoundTrack getActualSoundTrack() {
        return actual;
    }
}
