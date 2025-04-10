package it.unibo.oop.lastcrown.model.api.card.characters;

import java.util.Optional;

/**
 * He represents the player during a match. He must survive in order to achieve victory.
 */
public interface Hero extends GenericCharacter {
    /**
     * @return if it's present, this Hero's passive effect
     */
    Optional<PassiveEffect> getPassiveEffect();

    /**
     * @return the requirement to own this hero
     */
    Requirement getRequirement();
    
    /**
     * @return total number of melee cards that player deck can have
     */
    int getMeleeCards();

    /**
     * @return total number of ranged cards that player deck can have
     */
    int getRangedCards();

    /**
     * @return total number of spell cards that player deck can have
     */
    int getSpellCards();

    /**
     * @return attack value of the defensive wall associated with this hero
     */
    int getWallAttack();

    /**
     * @return health value of the defensive wall associated with this hero
     */
    int getWallHealth();
}
