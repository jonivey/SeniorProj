package com.jonivey.units;

public class Tank extends Unit
{
	private static final int cost = 1000;
	
	public Tank()
	{
		currentHealth = 150;
		maximumHealth = 150;
		attackPower = 75;
		movementRange = 5;
		attackRange = 5;
		unitType = Unit.LAND;
		owner = NO_PLAYER;
		hasAttacked = false;
		hasMoved = false;
	}
	
	public static int getCost()
	{
		return cost;
	}
}
