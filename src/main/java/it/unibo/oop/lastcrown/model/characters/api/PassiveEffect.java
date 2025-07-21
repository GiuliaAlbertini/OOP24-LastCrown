package it.unibo.oop.lastcrown.model.characters.api;

/**
 * The passive effect of the Hero. It affects the other friendly characters involved in the match.
 * @param category the category the passive effect is related to (ex health or attack)
 * @param percentage the percentage increase of the specified category 
 */
public record PassiveEffect(String category, int percentage) {

}
