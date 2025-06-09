package it.unibo.oop.lastcrown.model.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.unibo.oop.lastcrown.model.api.Collidable;
import it.unibo.oop.lastcrown.model.api.CollisionEvent;
import it.unibo.oop.lastcrown.model.api.CollisionResolver;
import it.unibo.oop.lastcrown.model.api.Point2D;
import it.unibo.oop.lastcrown.model.impl.Handler.HandleFollowEnemy;
import it.unibo.oop.lastcrown.view.characters.api.CharacterMovementObserver;

public class CollisionResolverImpl implements CollisionResolver {
	private final Map<Integer, HandleFollowEnemy> activeFollowMovements = new HashMap<>();
	CharacterMovementObserver movbs;
	CharacterMovementStop movbstop;
	
	public CollisionResolverImpl(CharacterMovementObserver movbs, CharacterMovementStop movbstop){
		this.movbs=movbs;
		this.movbstop=movbstop;
	}

	@Override
	public void notify(CollisionEvent event) {
		switch (event.getType()) {
			case FOLLOW_ENEMY -> handleFollowEnemy(event);
			
			default -> System.out.println("[WARN] Evento collisione non gestito: " + event.getType());
		}
	}

	private void handleFollowEnemy(CollisionEvent event) {
		int characterId = event.getCollidable1().getCardidentifier().number();
		HandleFollowEnemy movement = new HandleFollowEnemy(event, movbs, movbstop);
		//movement.update(deltaMs);
		activeFollowMovements.put(characterId, movement);
	}

	public List<Collidable> updateAllMovements(long deltaMs) {
		List<Collidable> updatedCharacters = new ArrayList<>();

		for (final var handle : activeFollowMovements.entrySet()) {
			HandleFollowEnemy movement = handle.getValue();
			movement.update(deltaMs);
			updatedCharacters.add(movement.getCharacter());
		}
		return updatedCharacters;
	}

	@Override
	public Point2D getCharacterPosition(int characterId) {
		HandleFollowEnemy movement = activeFollowMovements.get(characterId);
		if (movement != null) {
			return movement.getCurrentPosition();
		} else {
			return null; 
		}	
	}	
}
