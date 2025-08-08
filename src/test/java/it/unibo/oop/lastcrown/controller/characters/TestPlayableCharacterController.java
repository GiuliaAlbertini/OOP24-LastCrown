package it.unibo.oop.lastcrown.controller.characters;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import it.unibo.oop.lastcrown.controller.characters.api.PlayableCharacterController;
import it.unibo.oop.lastcrown.controller.characters.impl.playablecharacter.PlCharControllerFactory;
import it.unibo.oop.lastcrown.model.characters.api.PlayableCharacter;
import it.unibo.oop.lastcrown.model.characters.impl.playablecharacter.PlayableCharacterFactory;
import it.unibo.oop.lastcrown.model.card.CardIdentifier;
import it.unibo.oop.lastcrown.model.card.CardType;

/**
 * A simple test of Playable Character Controller.
 */
final class TestPlayableCharacterController {
    private static final String NAME = "Mario";
    private static final CardType TYPE = CardType.MELEE;
    private static final int COST = 10;
    private static final int ATTACK = 10;
    private static final int HEALTH = 200;
    private static final int COPIES = 3;
    private static final int ENERGY = 5;
    private static final double SPEED_MUL = 1.8;
    private static final int ACTION_RANGE = 10;

    private static final int CONTR_ID = 1;

    private final PlayableCharacter character = PlayableCharacterFactory.createPlayableCharacter(NAME,
     TYPE, COST, ATTACK, HEALTH, COPIES, ENERGY, SPEED_MUL, ACTION_RANGE);
    private final PlayableCharacterController contr = PlCharControllerFactory.createPlCharController(
        CONTR_ID, character);

    /**
     * Tests the getters of Playable Character model.
     */
    @Test
    void testModelGetters() {
        assertEquals(NAME, this.character.getName());
        assertEquals(TYPE, this.character.getType());
        assertEquals(COST, this.character.getCost());
        assertEquals(ATTACK, this.character.getAttackValue());
        assertEquals(HEALTH, this.character.getHealthValue());
        assertEquals(COPIES, this.character.getCopiesPerMatch());
        assertEquals(ENERGY, this.character.getEnergyToPlay());
        assertEquals(SPEED_MUL, this.character.getSpeedMultiplier());
        assertEquals(ACTION_RANGE, this.character.getActionRange());
    }

    /**
     * Tests the getters of Playable Character Controller.
     */
    @Test
    void testControllerGetters() {
        assertEquals(CONTR_ID, this.contr.getObserverId());
        assertEquals(new CardIdentifier(CONTR_ID, TYPE), this.contr.getId());
        assertEquals(ACTION_RANGE, this.contr.getActionRange());
    }
}
