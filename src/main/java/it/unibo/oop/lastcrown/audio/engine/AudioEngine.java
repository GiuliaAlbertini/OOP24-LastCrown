package it.unibo.oop.lastcrown.audio.engine;

import it.unibo.oop.lastcrown.audio.SoundEffect;
import it.unibo.oop.lastcrown.audio.SoundTrack;

/**
 * Interface that provides methods to play a specific soundtrack or sound effect.
 */
public interface AudioEngine {

    /**
     * Plays a soundtrack.
     * @param soundTrack the specific soundtrack to play
     */
    void playSoundTrack(SoundTrack soundTrack);

    /**
     * Plays a sound effect.
     * @param soundEffect the specific sound effecr to play
     */
    void playEffect(SoundEffect soundEffect);
}
