package it.unibo.oop.lastcrown.controller.characters.impl;

import javax.swing.JPanel;

import it.unibo.oop.lastcrown.controller.characters.api.CharacterDeathObserver;
import it.unibo.oop.lastcrown.controller.characters.api.HeroController;
import it.unibo.oop.lastcrown.model.card.CardType;
import it.unibo.oop.lastcrown.model.characters.api.Hero;
import it.unibo.oop.lastcrown.view.characters.api.GenericCharacterGUI;
import it.unibo.oop.lastcrown.view.characters.api.HeroGUI;
import it.unibo.oop.lastcrown.view.characters.impl.HeroGUIImpl;

/**
 * A standard implementation of HeroController interface.
 */
public class HeroControllerImpl extends GenericCharacterControllerImpl implements HeroController {
    private HeroGUI view;
    private final String charName;
    private final double speedMultiplier;

    /**
     * @param obs the Character Death observer
     * @param id this hero controller id
     * @param hero the ehero linked to this controller
     */
    public HeroControllerImpl(final CharacterDeathObserver obs, final int id, final Hero hero) {
        super(obs, id, hero, CardType.HERO.get());
        this.view = null;
        this.charName = hero.getName();
        this.speedMultiplier = hero.getSpeedMultiplier();
    }

    @Override
    public final GenericCharacterGUI createView(final int width, final int height) {
        final HeroGUI newView = new HeroGUIImpl(this, this.charName,
        this.speedMultiplier, width, height);
        this.view = newView;
        return newView;
    }

    @Override
    public final void setHeroInGame(final JPanel matchPanel, final int initialX, final int initialY) {
        this.view.useInGameFrames();
        this.setCharacterPanelPosition(matchPanel, initialX, initialY);
    }

    @Override
    public final void setHeroInShop(final JPanel shopPanel, final int initialX, final int initialY) {
        this.view.useShopFrames();
        this.setCharacterPanelPosition(shopPanel, initialX, initialY);
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

