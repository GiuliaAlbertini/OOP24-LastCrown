package it.unibo.oop.lastcrown.controller.collision.impl;

import java.util.ArrayList;
import java.util.List;
import it.unibo.oop.lastcrown.controller.collision.api.SpellManager;
import it.unibo.oop.lastcrown.controller.characters.api.GenericCharacterController;
import it.unibo.oop.lastcrown.controller.collision.api.MatchController;
import it.unibo.oop.lastcrown.model.card.CardIdentifier;
import it.unibo.oop.lastcrown.model.card.CardType;
import it.unibo.oop.lastcrown.model.collision.impl.Pair;
import it.unibo.oop.lastcrown.model.spell.api.Spell;
import it.unibo.oop.lastcrown.model.spell.api.SpellEffect;
import it.unibo.oop.lastcrown.model.user.api.CompleteCollection;
import it.unibo.oop.lastcrown.view.dimensioning.DimensionResolver;
import it.unibo.oop.lastcrown.view.spell.api.SpellGUI;
import it.unibo.oop.lastcrown.view.spell.impl.SpellGUIImpl;
import javax.swing.JComponent;

public final class SpellManagerImpl implements SpellManager {

    private final List<Pair<CardIdentifier, SpellGUI>> spellList = new ArrayList<>();
    private final MatchController matchController;
    private final CompleteCollection collection;
    private boolean spellScanner;
    private int spellX;
    private int spellY;
    private final int frameWidth;

    public SpellManagerImpl(final MatchController matchController, final CompleteCollection collection,
            final int frameWidth) {
        this.matchController = matchController;
        this.collection = collection;
        this.frameWidth = frameWidth;
    }

    @Override
    public void handleSpellSelection(final CardIdentifier id) {
        if (id.type() == CardType.SPELL) {
            spellList.clear();
            spellScanner = false;
            final Spell spell = this.collection.getSpell(id).get();
            SpellGUI spellGUI = new SpellGUIImpl(spell.getName(), (int) (frameWidth * DimensionResolver.SPELL.width()));
            spellList.add(new Pair<>(id, spellGUI));
        }
    }

    @Override
    public void castSpell(final int x, final int y) {
        spellX = x;
        spellY = y;
        spellScanner = true;
    }

    @Override
    public void update(final int deltaTime) {
        if (!spellList.isEmpty() && spellScanner) {
            Pair<CardIdentifier, SpellGUI> spellSelected = spellList.get(spellList.size() - 1);
            if (spellSelected != null && spellSelected.get1() != null && spellSelected.get2() != null) {
                final CardIdentifier id = spellSelected.get1();
                final JComponent spellComponent = spellSelected.get2().getGraphicalComponent();
                matchController.getMatchView().addSpellGraphics(id.number(), spellComponent, spellX, spellY);
                spellSelected.get2().startAnimation();
                spellScanner = false;
                spellList.clear();

                final SpellEffect spellEffect = collection.getSpell(id).get().getSpellEffect();
                switch (spellEffect.target()) {
                    case FRIENDLY:
                        handleSpellFriendly(spellEffect);
                        break;
                    case ENEMY:
                        handleSpellEnemy(spellEffect);
                        break;
                    default:
                        break;
                }
            }
        }
    }

    private void handleSpellFriendly(final SpellEffect spellEffect) {
        List<GenericCharacterController> friendlyCharacters = matchController.getCharactersByType(CardType.MELEE);
        if ("health".equals(spellEffect.category())) {
            for (var character : friendlyCharacters) {
                character.heal(spellEffect.amount());
            }
        } else if ("attack".equals(spellEffect.category())) {
            for (var character : friendlyCharacters) {
                character.setAttackValue(spellEffect.amount());
            }
        }
    }

    private void handleSpellEnemy(final SpellEffect spellEffect) {
        List<GenericCharacterController> enemyCharacters = matchController.getCharactersByType(CardType.ENEMY);
        if ("health".equals(spellEffect.category())) {
            for (var character : enemyCharacters) {
                character.takeHit(spellEffect.amount());
            }
        } else if ("attack".equals(spellEffect.category())) {
            for (var character : enemyCharacters) {
                character.setAttackValue((-1) * spellEffect.amount());
            }
        } else if ("speedMultiplier".equals(spellEffect.category())) {
            for (var character : enemyCharacters) {
                character.setSpeedMultiplierValue((-1) * spellEffect.amount());
            }
        }
    }
}