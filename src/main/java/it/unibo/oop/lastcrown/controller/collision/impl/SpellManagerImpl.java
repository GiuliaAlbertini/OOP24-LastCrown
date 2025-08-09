package it.unibo.oop.lastcrown.controller.collision.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import javax.swing.JComponent;

import it.unibo.oop.lastcrown.controller.characters.api.GenericCharacterController;
import it.unibo.oop.lastcrown.controller.collision.api.MatchController;
import it.unibo.oop.lastcrown.controller.collision.api.SpellManager;
import it.unibo.oop.lastcrown.model.card.CardIdentifier;
import it.unibo.oop.lastcrown.model.card.CardType;
import it.unibo.oop.lastcrown.model.spell.api.Spell;
import it.unibo.oop.lastcrown.model.spell.api.SpellEffect;
import it.unibo.oop.lastcrown.utility.Constant;
import it.unibo.oop.lastcrown.utility.Pair;
import it.unibo.oop.lastcrown.view.dimensioning.DimensionResolver;
import it.unibo.oop.lastcrown.view.spell.api.SpellGUI;
import it.unibo.oop.lastcrown.view.spell.impl.SpellGUIImpl;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * Implementation of the SpellManager interface.
 *
 * This class is responsible for managing spell selection, casting, animation,
 * and applying the corresponding SpellEffect to the correct targets
 * (friendly or enemy characters) in the game.
 */
@SuppressFBWarnings(
    value = "EI_EXPOSE_REP2",
    justification = """
            The spell manager keeps reference to the match controller to access live data on the
            characters' positions, in order to apply the spell's effect.
            """
)
public final class SpellManagerImpl implements SpellManager {

    private final List<Pair<CardIdentifier, SpellGUI>> spellList = new ArrayList<>();
    private final MatchController matchController;
    private final Function<CardIdentifier, Optional<Spell>> spellProvider;
    private final int frameWidth;

    private boolean shouldCastSpell;
    private int castX;
    private int castY;

    /**
     * Creates a new instance of SpellManagerImpl.
     *
     * @param matchController the controller managing the current match, used to
     *                        access character data and game state
     * @param spellProvider   a function that, given a CardIdentifier, provides an
     *                        Optional containing the corresponding Spell if
     *                        available
     * @param frameWidth      the width of the game frame, used for sizing spell
     *                        graphical components
     */
    public SpellManagerImpl(final MatchController matchController,
            final Function<CardIdentifier, Optional<Spell>> spellProvider,
            final int frameWidth) {
        this.matchController = matchController;
        this.spellProvider = spellProvider;
        this.frameWidth = frameWidth;
        this.shouldCastSpell = false;
    }

    @Override
    public void handleSpellSelection(final CardIdentifier id) {
        if (id.type() != CardType.SPELL) {
            return;
        }

        resetSpellState();
        this.spellProvider.apply(id).ifPresent(spell -> {
            final SpellGUI spellGUI = new SpellGUIImpl(spell.getName(),
                    (int) (frameWidth * DimensionResolver.SPELL.width()));
            spellList.add(new Pair<>(id, spellGUI));
        });
    }

    @Override
    public void castSpell(final int x, final int y) {
        this.castX = x;
        this.castY = y;
        this.shouldCastSpell = true;
    }

    @Override
    public void update(final int deltaTime) {
        if (spellList.isEmpty() || !shouldCastSpell) {
            return;
        }

        final Pair<CardIdentifier, SpellGUI> spellSelected = spellList.get(spellList.size() - 1);
        castSelectedSpell(spellSelected);
    }

    private void resetSpellState() {
        spellList.clear();
        shouldCastSpell = false;
    }

    private void castSelectedSpell(final Pair<CardIdentifier, SpellGUI> spellSelected) {
        final CardIdentifier id = spellSelected.get1();
        final JComponent spellComponent = spellSelected.get2().getGraphicalComponent();

        matchController.getMatchView().addSpellGraphics(id.number(), spellComponent, castX, castY);
        spellSelected.get2().startAnimation();

        resetSpellState();

        this.spellProvider.apply(id).ifPresent(spell -> {
            final SpellEffect spellEffect = spell.getSpellEffect();
            applySpellEffect(spellEffect);
        });
    }

    private void applySpellEffect(final SpellEffect spellEffect) {
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

    private void handleSpellFriendly(final SpellEffect spellEffect) {
        final List<GenericCharacterController> friendlyCharacters = getFriendlyCharacters();
        applyEffectToCharacters(friendlyCharacters, spellEffect, false);
    }

    private void handleSpellEnemy(final SpellEffect spellEffect) {
        final List<GenericCharacterController> enemyCharacters = getEnemyCharacters();
        applyEffectToCharacters(enemyCharacters, spellEffect, true);
    }

    private List<GenericCharacterController> getFriendlyCharacters() {
        final List<GenericCharacterController> characters = new ArrayList<>();
        characters.addAll(matchController.getCharactersByType(CardType.MELEE));
        characters.addAll(matchController.getCharactersByType(CardType.RANGED));
        characters.addAll(matchController.getCharactersByType(CardType.HERO));
        return characters;
    }

    private List<GenericCharacterController> getEnemyCharacters() {
        final List<GenericCharacterController> characters = new ArrayList<>();
        characters.addAll(matchController.getCharactersByType(CardType.ENEMY));
        characters.addAll(matchController.getCharactersByType(CardType.BOSS));
        return characters;
    }

    private void applyEffectToCharacters(final List<GenericCharacterController> characters,
            final SpellEffect spellEffect, final boolean isTargetingEnemy) {

        final String category = spellEffect.category();
        final int amount = spellEffect.amount();

        if (Constant.HEALTH_CATEGORY.equals(category)) {
            applyHealthEffect(characters, amount, isTargetingEnemy);
        } else if (Constant.ATTACK_CATEGORY.equals(category)) {
            applyAttackEffect(characters, amount, isTargetingEnemy);
        } else if (Constant.SPEED_CATEGORY.equals(category) && isTargetingEnemy) {
            applySpeedEffect(characters, amount);
        }
    }

    private void applyHealthEffect(final List<GenericCharacterController> characters,
            final int amount, final boolean isTargetingEnemy) {
        for (final var character : characters) {
            if (isTargetingEnemy) {
                character.takeHit(amount);
            } else {
                character.heal(amount);
            }
        }
    }

    private void applyAttackEffect(final List<GenericCharacterController> characters,
            final int amount, final boolean isTargetingEnemy) {
        final int effectiveAmount = isTargetingEnemy ? Constant.ENEMY_EFFECT_MULTIPLIER * amount : amount;
        for (final var character : characters) {
            character.setAttackValue(effectiveAmount);
        }
    }

    private void applySpeedEffect(final List<GenericCharacterController> characters, final int amount) {
        for (final var character : characters) {
            character.setSpeedMultiplierValue(Constant.ENEMY_EFFECT_MULTIPLIER * amount);
        }
    }
}
