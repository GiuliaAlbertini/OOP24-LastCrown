package it.unibo.oop.lastcrown.audio.engine;

/**
 * Creates a new AudioEngine.
 */
public final class AudioEngineFactory {
    private AudioEngineFactory() { }

    /**
     * @return a new Audio Engine
     */
    public static AudioEngine createAudioEngine() {
        return new AudioEngineImpl();
    }
}
