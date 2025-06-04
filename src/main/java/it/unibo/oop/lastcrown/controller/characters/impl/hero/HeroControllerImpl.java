package it.unibo.oop.lastcrown.controller.characters.impl.hero;

import it.unibo.oop.lastcrown.controller.characters.api.CharacterDeathObserver;
import it.unibo.oop.lastcrown.controller.characters.api.HeroController;
import it.unibo.oop.lastcrown.controller.characters.impl.GenericCharacterControllerImpl;
import it.unibo.oop.lastcrown.model.card.CardType;
import it.unibo.oop.lastcrown.model.characters.api.Hero;
import it.unibo.oop.lastcrown.view.characters.api.CharacterMovementObserver;
import it.unibo.oop.lastcrown.view.characters.api.GenericCharacterGUI;
import it.unibo.oop.lastcrown.view.characters.api.HeroGUI;
import it.unibo.oop.lastcrown.view.characters.impl.HeroGUIImpl;

/**
 * A standard implementation of HeroController interface.
 */
public class HeroControllerImpl extends GenericCharacterControllerImpl implements HeroController {
    private HeroGUI view;
    private final CharacterMovementObserver movObs;
    private final String charName;
    private final double speedMultiplier;

    /**
     * @param deathObs the Character Death observer
     * @param movObs the character movement observer
     * @param id this hero controller id
     * @param hero the ehero linked to this controller
     */
    public HeroControllerImpl(final CharacterDeathObserver deathObs, final CharacterMovementObserver movObs,
    final int id, final Hero hero) {
        super(deathObs, id, hero, CardType.HERO);
        this.movObs = movObs;
        this.view = null;
        this.charName = hero.getName();
        this.speedMultiplier = hero.getSpeedMultiplier();
    }

    @Override
    public final GenericCharacterGUI createView(final int width, final int height) {
        final HeroGUI newView = new HeroGUIImpl(this, this.getId(), movObs, this.charName,
        this.speedMultiplier, width, height);
        this.view = newView;
        return newView;
    }

    @Override
    public final void setHeroInGame() {
        this.view.useInGameFrames();
    }

    @Override
    public final void setHeroInShop() {
        this.view.useShopFrames();
    }

    @Override
    public final void runLeft() {
        this.view.startRunLeftLoop();
    }

    @Override
    public final void stopLeft() {
        this.view.startStopLeftLoop();
    }
}

