package it.unibo.oop.lastcrown.model.impl.card.characters.hero;

import java.util.Optional;

import it.unibo.oop.lastcrown.model.api.card.Requirement;
import it.unibo.oop.lastcrown.model.api.card.characters.Hero;
import it.unibo.oop.lastcrown.model.api.card.characters.PassiveEffect;

/**
 * Creates a Hero with the specified params.
 */
public class HeroFactory {
    /**
     * @param name the name of this hero
     * @param requirement the requirement to own this hero(ex Coins, 200)
     * @param attack the attack value of this hero
     * @param health the health value of this hero
     * @param atckRecoveryTime the attack recovery time of this hero(time lap between attacks)
     * @param passiveEffect passive effect of this hero
     * @param meleeCards number of melee cards deck can have with this hero
     * @param rangedCards number of ranged cards deck can have with this hero
     * @param spellCards number of spell cards deck can have with this hero
     * @param wallAttack the attack value of the defensive wall associated with this hero
     * @param wallHealth the health value of the defensive wall associated with this hero
     * @return new Hero
     */
    public Hero createHero(final String name, final Requirement requirement, final int attack, final int health, 
    final double atckRecoveryTime, final Optional<PassiveEffect> passiveEffect, 
    final int meleeCards, final int rangedCards, final int spellCards, final int wallAttack, final int wallHealth) {

        return new HeroImpl(name, requirement, attack, health, atckRecoveryTime, passiveEffect,
        meleeCards, rangedCards, spellCards, wallAttack, wallHealth);
    }
}
