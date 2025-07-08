package it.unibo.oop.lastcrown.audio.loader;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * Utility class that loads and closes the audio clips of the soundtracks and sound effects.
 */
public final class ClipLoader {
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
            final File file = new File(path);
            final AudioInputStream stream = AudioSystem.getAudioInputStream(file);
            final Clip clip = AudioSystem.getClip();
            clip.open(stream);
            return clip;
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
}
