package it.unibo.oop.lastcrown.model.file_handling.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import it.unibo.oop.lastcrown.model.card.CardIdentifier;
import it.unibo.oop.lastcrown.model.file_handling.api.Serializer;
import it.unibo.oop.lastcrown.model.user.api.Account;

/**
 * Serializer for an object {@link Account}.
 */
public class AccountSerializer implements Serializer<Account> {

    @Override
    public final List<String> serialize(final Account account) {
        validateAccount(account);

        final var lines = getBaseFields(account);
        final String cardsLine = buildCardsLine(account.getUserCollection().getCollection());
        lines.add(cardsLine);
        return lines;
    }

    private void validateAccount(final Account account) {
        if (account == null) {
            throw new IllegalArgumentException("Account to serialize cannot be null");
        }
    }

    private List<String> getBaseFields(final Account account) {
        return new ArrayList<>(List.of(
            account.getUsername(),
            String.valueOf(account.getCoins()),
            String.valueOf(account.getBossesDefeated()),
            String.valueOf(account.getPlayedMatches()),
            String.valueOf(account.getPlaytime())
        ));
    }

    private String buildCardsLine(final Set<CardIdentifier> cards) {
        if (cards == null || cards.isEmpty()) {
            return "";
        }
        return cards.stream()
            .map(this::formatCard)
            .collect(Collectors.joining(","));
    }

    private String formatCard(final CardIdentifier ci) {
        return ci.number() + ";" + ci.type();
    }
}
