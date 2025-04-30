package it.unibo.oop.lastcrown.model.api.card;

/**
 * Identifies all the cards of this app (characters or spells).
 * @param number the number of the specific card Id
 * @param type the type of the specific card Id (playableCharacter, hero, enemy, spell)
 */
public record CardIdentifier(int number, String type) {
}
