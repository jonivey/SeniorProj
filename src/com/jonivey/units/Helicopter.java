package com.jonivey.units;

public class Helicopter extends Unit
{
	private static final int cost = 1000;
	
	public Helicopter()
	{
		currentHealth = 130;
		maximumHealth = 130;
		attackPower = 70;
		movementRange = 7;
		attackRange = 7;
		unitType = Unit.AIR;
		owner = NO_PLAYER;
		hasAttacked = false;
		hasMoved = false;
	}
	
	public static int getCost()
	{
		return cost;
	}
}
