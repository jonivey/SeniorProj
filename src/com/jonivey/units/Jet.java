package com.jonivey.units;

public class Jet extends Unit
{
	private static final int cost = 1500;
	
	public Jet()
	{
		currentHealth = 110;
		maximumHealth = 110;
		attackPower = 60;
		movementRange = 9;
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
