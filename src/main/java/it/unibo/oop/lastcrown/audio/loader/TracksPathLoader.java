package it.unibo.oop.lastcrown.audio.loader;

import it.unibo.oop.lastcrown.audio.SoundEffect;
import it.unibo.oop.lastcrown.audio.SoundTrack;

/**
 * Utility class that builds the folder paths of the soundtracks and sound effects.
 */
public final class TracksPathLoader {
    private static final String SEP = System.getProperty("file.separator");
    private static final String EFFECT = "effect";
    private static final String INTRO = "intro";
    private static final String LOOP = "loop";
    private static final String FORMAT = ".wav";
    private TracksPathLoader() { }

    /**
     * @param soundTrack the specific soundtrack to find
     * @return the path of the given soundtrack intro or null if the path doesn't exist
     */
    public static String loadIntroPath(final SoundTrack soundTrack) {
        return loadGenericPath(soundTrack, INTRO);
    }

    /**
     * @param soundTrack the specific soundtrack to find
     * @return the path of the given soundtrack loop or null if the path doesn't exist
     */
    public static String loadLoopPath(final SoundTrack soundTrack) {
        return loadGenericPath(soundTrack, LOOP);
    }

    private static String loadGenericPath(final SoundTrack soundTrack, final String type) {
        final String path = SEP + "tracks" + SEP + soundTrack.get() + SEP + type + FORMAT;
        return path;
    }

    /**
     * @param soundEffect the specific sound effect to find
     * @return the path of the given sound effect or null if the path doesn't exist
     */
    public static String loadSoundEffectPath(final SoundEffect soundEffect) {
         return SEP + "tracks" + SEP + EFFECT + SEP + soundEffect.get() + FORMAT;
    }
}
