package it.unibo.oop.lastcrown.model.impl;

import it.unibo.oop.lastcrown.model.api.Collidable;
import it.unibo.oop.lastcrown.model.api.Hitbox;
import it.unibo.oop.lastcrown.model.card.CardIdentifier;

public class CollidableImpl implements Collidable {
	public Hitbox hitbox;
	public CardIdentifier cardIdentifier;

	public CollidableImpl(Hitbox hitbox,  CardIdentifier cardIdentifier){
		this.hitbox= hitbox;
		this.cardIdentifier= cardIdentifier;
	}

	@Override
	public Hitbox getHitbox() {
		return this.hitbox;
	}

	@Override
	public CardIdentifier getCardidentifier() {
		return this.cardIdentifier;
	}
	
}
