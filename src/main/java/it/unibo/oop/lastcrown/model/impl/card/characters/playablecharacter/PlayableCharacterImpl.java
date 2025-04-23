package it.unibo.oop.lastcrown.model.impl.card.characters.playablecharacter;

import it.unibo.oop.lastcrown.model.api.card.characters.PlayableCharacter;
import it.unibo.oop.lastcrown.model.impl.card.characters.GenericCharacterImpl;

/**
 * A standard implementation of Playable Character interface.
 */
public class PlayableCharacterImpl extends GenericCharacterImpl implements PlayableCharacter {
    private final String type;
    private final int cost;
    private final int copiesPerRound;
    private final int energyToPlay;
    private final int actionRange;

    /**
     * @param name the name of this character
     * @param type can be "melee" or "ranged"
     * @param cost total amount of coins to spend to own this playable character
     * @param attack the attack value of this character
     * @param health the health value of this character
     * @param copiesPerRound number of times this character can be played during a match
     * @param energyToPlay amount of player energy needed to play this character
     * @param speedMultiplier the speed multiplier of this character(ex 1.5 -> standard_speed * 1.5)
     * @param actionRange this character action range (distance form witch this character can spot enemies)
     */
    public PlayableCharacterImpl(final String name, final String type, final int cost,
    final int attack, final int health, final int copiesPerRound,
    final int energyToPlay, final double speedMultiplier, final int actionRange) {
        super(name, attack, health, speedMultiplier);
        this.type = type;
        this.cost = cost;
        this.copiesPerRound = copiesPerRound;
        this.energyToPlay = energyToPlay;
        this.actionRange = actionRange;
    }

    @Override
    public final String getType() {
        return this.type;
    }

     @Override
    public final int getCost() {
        return this.cost;
    }

    @Override
    public final int getCopiesPerMatch() {
        return this.copiesPerRound;
    }

    @Override
    public final int getEnergyToPlay() {
        return this.energyToPlay;
    }

    @Override
    public final int getActionRange() {
        return this.actionRange;
    }
}
