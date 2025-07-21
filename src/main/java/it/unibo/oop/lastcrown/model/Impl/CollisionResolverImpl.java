package it.unibo.oop.lastcrown.model.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import it.unibo.oop.lastcrown.controller.api.MatchController;
import it.unibo.oop.lastcrown.model.api.CollisionEvent;
import it.unibo.oop.lastcrown.model.api.CollisionResolver;
import it.unibo.oop.lastcrown.model.api.Point2D;
import it.unibo.oop.lastcrown.model.impl.handler.HandleFollowEnemy;
import it.unibo.oop.lastcrown.view.characters.api.Movement;

/**
 * Implements CollisionResolver to handle "follow enemy" collision events.
 * Manages active follow movements and tracks completed ones.
 */
public final class CollisionResolverImpl implements CollisionResolver {
    private final Map<Integer, HandleFollowEnemy> activeFollowMovements = new HashMap<>();
    private Map<Integer, Integer> completedFollows = new HashMap<>(); // nemico-personaggio
    MatchController matchController;
    List<Pair<Integer, Integer>> opponent = new ArrayList<>();
    Set<Pair<Integer, Integer>> opponentRanged = new HashSet<>();

    /**
     * Costruttore vuoto di default per CollisionResolverImpl.
     */
    public CollisionResolverImpl(MatchController controller) {
        this.matchController = controller;
    }

    @Override
    public void notify(final CollisionEvent event) {
        switch (event.getType()) {
            case ENEMY -> handleFollowEnemy(event);
            case BOSS -> opponentBoss(event);
            case RANGED ->opponentRanged(event);
            // default -> System.out.println("[WARN] Evento collisione non gestito: " +
            // event.getType());
        }
    }

    private void handleFollowEnemy(final CollisionEvent event) {
        final int characterId = event.getCollidable1().getCardidentifier().number();
        final HandleFollowEnemy movement = new HandleFollowEnemy(event);
        movement.startFollowing();
        System.out.println("ho chiamato in tempo handle follow enemy?");
        activeFollowMovements.put(characterId, movement);
    }

    public void opponentBoss(final CollisionEvent event) {
        //System.out.println("vediamo se qui entro");
        final int characterId = event.getCollidable1().getCardidentifier().number();
        final int enemyId = event.getCollidable2().getCardidentifier().number();

        opponent.add(new Pair<Integer, Integer>(characterId, enemyId));
    }

    public void opponentRanged(final CollisionEvent event){
        final int characterId = event.getCollidable1().getCardidentifier().number();
        final int enemyId = event.getCollidable2().getCardidentifier().number();
        opponentRanged.add(new Pair<Integer,Integer>(characterId, enemyId));
    }

    public boolean hasOpponentRangedPartner(final int id) {
        for (Pair<Integer, Integer> pair : opponentRanged) {
            if (pair.get1() == id || pair.get2() == id) {
                return true;
            }
        }
        return false;
    }


    public int getOpponentRangedPartner(final int id) {
        for (Pair<Integer, Integer> pair : opponentRanged) {
            if (pair.get1() == id) {

                return pair.get2();
            } else if (pair.get2() == id) {
                return pair.get1();
            }
        }
        return -1; // Valore che indica "non trovato"
    }

    public boolean hasOpponentBossPartner(final int id) {
        for (Pair<Integer, Integer> pair : opponent) {
            if (pair.get1() == id || pair.get2() == id) {
                return true;
            }
        }
        return false;
    }

    public int getOpponentBossPartner(final int id) {
        for (Pair<Integer, Integer> pair : opponent) {
            if (pair.get1() == id) {
                return pair.get2();
            } else if (pair.get2() == id) {
                return pair.get1();
            }
        }
        return -1; // Valore che indica "non trovato"
    }
    /**
     * Rimuove tutte le coppie di combattimento presenti nella lista opponent.
     */
    public void clearAllOpponentPairs() {
        opponent.clear();
    }

    public void clearAllOpponentRangedPairs(){
        opponentRanged.clear();
    }




    public List<Integer> getAllCharacterIdsInBossFight() {
        final List<Integer> result = new ArrayList<>();
        for (Pair<Integer, Integer> pair : opponent) {
            result.add(pair.get1()); // Aggiunge solo il characterId (giocatore)
        }
        return result;
    }

    public void clearBossFightPairById(final int id) {
        Pair<Integer, Integer> toRemove = null;
        for (Pair<Integer, Integer> pair : opponent) {
            if (pair.get1() == id || pair.get2() == id) {
                toRemove = pair;
                break;
            }
        }
        if (toRemove != null) {
            opponent.remove(toRemove);
        }
    }

    @Override
    public Optional<MovementResult> updateMovementFor(final int characterId, final long deltaMs) {
        final HandleFollowEnemy movement = activeFollowMovements.get(characterId);
        System.out.println("movement" + movement);
        final int enemyId = movement.getEnemy().getCardidentifier().number();


        if (movement != null ) {
            final var stillMoving = movement.update(deltaMs);
            // non si sta più muovendo
            if (!stillMoving) {
                activeFollowMovements.remove(characterId);
                completedFollows.put(enemyId, characterId);

            }
            final Point2D delta = movement.getDelta();
            final Movement movementDelta = new Movement((int) delta.x(), (int) delta.y());
            return Optional.of(new MovementResult(
                    movement.getCharacter(),
                    movement.getCurrentPosition(),
                    movementDelta,
                    stillMoving));
        }
        return Optional.empty();
    }

    @Override
    public boolean wasEnemyCollided(final int id) {
        return completedFollows.containsKey(id) || completedFollows.containsValue(id);
    }

    @Override
    public void clearEnemyCollision(final int characterId) {
        if (completedFollows.containsKey(characterId)) {
            int otherId = completedFollows.get(characterId);
            completedFollows.remove(characterId); // Rimuove id -> otherId
            completedFollows.remove(otherId, characterId); // Se esiste anche otherId -> id, rimuove
            return;
        }

        for (Map.Entry<Integer, Integer> entry : completedFollows.entrySet()) {
            if (entry.getValue() == characterId) {
                int otherId = entry.getKey();
                completedFollows.remove(otherId); // Rimuove otherId -> id
                completedFollows.remove(characterId, otherId); // Se esiste anche id -> otherId, rimuove
                return;
            }
        }
    }
}
