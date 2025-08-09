package it.unibo.oop.lastcrown.audio.loader;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * Utility class that loads and closes the audio clips of the soundtracks and sound effects.
 */
public final class ClipLoader {
    private static final float VOLUME = 0.3f;
    private ClipLoader() { }

    /**
     * Load the audio clip corresponding to the given audio path.
     * @param path the folder path of the soundtrack or sound effect.
     * @return the corresponding clip audio
     * @throws UnsupportedAudioFileException
     * @throws IOException
     * @throws LineUnavailableException
     */
    public static Clip loadClip(final String path) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        if (path != null) {
            final InputStream inputStream = ClipLoader.class.getResourceAsStream(path.replace(File.separator, "/"));
            if (inputStream != null) {
                final BufferedInputStream bufInputStream = new BufferedInputStream(inputStream);
                final AudioInputStream stream = AudioSystem.getAudioInputStream(bufInputStream);
                final Clip clip = AudioSystem.getClip();
                clip.open(stream);
                setVolume(clip, VOLUME);
                return clip;
            }
        }
        return null;
    }

    /**
     * Stops and closes the given clip audio.
     * @param clip the clip audio to stop and close
     */
    public static void closeClip(final Clip clip) {
        if (clip != null) {
            if (clip.isRunning()) {
                clip.stop();
            }
            clip.flush();
            clip.close();
        }
    }

     private static void setVolume(final Clip clip, final float volume) {
        if (volume < 0f || volume > 1f) {
            throw new IllegalArgumentException("Volume must be beetween 0.0 and 1.0");
        }

        if (clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
            final float volumeSetter = 20f;
            final FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            final float dB = volumeSetter * (float) Math.log10(volume);
            gainControl.setValue(dB);
        }
    }
}
