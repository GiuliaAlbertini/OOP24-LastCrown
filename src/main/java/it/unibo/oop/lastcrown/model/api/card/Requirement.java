package it.unibo.oop.lastcrown.model.api.card;

/**
 * the Requirement to own a specific Card.
 *
 * @param category the category the requirement is referring to
 * @param amount the amount of the specific category required.
 */
public record Requirement(String category, int amount) {

}
