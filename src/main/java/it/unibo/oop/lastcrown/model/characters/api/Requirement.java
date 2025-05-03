package it.unibo.oop.lastcrown.model.characters.api;

/**
 * the Requirement to own a specific Hero.
 *
 * @param category the category the requirement is referring to
 * @param amount the amount of the specific category required.
 */
public record Requirement(String category, int amount) {

}
