package it.unibo.oop.lastcrown.model.file_handling.impl;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import it.unibo.oop.lastcrown.model.card.CardIdentifier;
import it.unibo.oop.lastcrown.model.card.CardType;
import it.unibo.oop.lastcrown.model.file_handling.api.Parser;
import it.unibo.oop.lastcrown.model.user.api.Account;
import it.unibo.oop.lastcrown.model.user.api.UserCollection;
import it.unibo.oop.lastcrown.model.user.impl.AccountImpl;

/**
 * Parser for an object {@link Account}.
 */
public class AccountParser implements Parser<Account> {
    private static final int EXPECTED_FIELDS = 6;

    @Override
    public final Account parse(final List<String> lines) {
        if (lines == null || lines.size() < EXPECTED_FIELDS) {
            throw new IllegalArgumentException(
                "Invalid data: expected at least "
                + EXPECTED_FIELDS
                + " lines, but got "
                + (lines == null ? 0 : lines.size())
            );
        }

        final var it = lines.iterator();
        final String username = it.next().trim();
        final int coins = parseIntField(it.next(), "coins");
        final int bosses = parseIntField(it.next(), "bosses defeated");
        final int games = parseIntField(it.next(), "played games");
        final double playtime = parseDoubleField(it.next(), "playtime");
        final String cardsLine = it.next();

        final AccountImpl account = new AccountImpl(username);
        account.removeCoins(account.getCoins());
        account.addCoins(coins);
        IntStream.range(0, bosses).forEach(i -> account.increaseBossesDefeated());
        IntStream.range(0, games).forEach(i -> account.increasePlayedMatches());
        account.addPlaytime(playtime);

        final UserCollection collection = account.getUserCollection();
        parseCards(cardsLine)
            .forEach(collection::addCard);

        return account;
    }

    private int parseIntField(final String token, final String fieldName) {
        try {
            return Integer.parseInt(token.trim());
        } catch (final NumberFormatException e) {
            throw new IllegalArgumentException(
                "Invalid integer for " + fieldName + ": '" + token + "'", e
            );
        }
    }

    private double parseDoubleField(final String token, final String fieldName) {
        try {
            return Double.parseDouble(token.trim());
        } catch (final NumberFormatException e) {
            throw new IllegalArgumentException(
                "Invalid double for " + fieldName + ": '" + token + "'", e
            );
        }
    }

    private List<CardIdentifier> parseCards(final String line) {
        if (line == null || line.isBlank()) {
            return List.of();
        }
        return Arrays.stream(line.split(","))
                     .map(this::toCardIdentifier)
                     .collect(Collectors.toList());
    }

    private CardIdentifier toCardIdentifier(final String token) {
        final String[] parts = token.split(";", 2);
        if (parts.length != 2) {
            throw new IllegalArgumentException("Invalid card token: '" + token + "'");
        }
        final int count = parseIntField(parts[0], "card count");
        final CardType type = getCardType(parts[1].trim());
        return new CardIdentifier(count, type);
    }

    private CardType getCardType(final String type) {
        if (CardType.MELEE.get().equalsIgnoreCase(type)) {
            return CardType.MELEE;
        } else if (CardType.RANGED.get().equalsIgnoreCase(type)) {
            return CardType.RANGED;
        } else if (CardType.HERO.get().equalsIgnoreCase(type)) {
            return CardType.HERO;
        } else {
            return CardType.SPELL;
        }
    }
}
