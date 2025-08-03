package it.unibo.oop.lastcrown.controller.characters.impl.hero;

import it.unibo.oop.lastcrown.controller.characters.api.CharacterDeathObserver;
import it.unibo.oop.lastcrown.controller.characters.api.HeroController;
import it.unibo.oop.lastcrown.controller.characters.impl.GenericCharacterControllerImpl;
import it.unibo.oop.lastcrown.model.card.CardType;
import it.unibo.oop.lastcrown.model.characters.api.Hero;
import it.unibo.oop.lastcrown.view.characters.api.GenericCharacterGUI;
import it.unibo.oop.lastcrown.view.characters.impl.HeroGUI;

/**
 * A standard implementation of HeroController interface.
 */
public class HeroControllerImpl extends GenericCharacterControllerImpl implements HeroController {
    private final String charName;
    private final int maximumHealth;

    /**
     * @param deathObs the Character Death observer
     * @param id this hero controller id
     * @param hero the ehero linked to this controller
     */
    public HeroControllerImpl(final CharacterDeathObserver deathObs, final int id, final Hero hero) {
        super(deathObs, id, hero, CardType.HERO);
        this.charName = hero.getName();
        this.maximumHealth = hero.getHealthValue();
    }

    @Override
    public final GenericCharacterGUI createView(final int width, final int height) {
        final var view = new HeroGUI(this, this.charName, width, height);
        view.setSupportedAnimation();
        return view;
    }

    @Override
    public int getMaximumHealthValue() {
        return this.maximumHealth;
    }
}

