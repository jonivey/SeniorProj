package com.jonivey.units;

public class Soldier extends Unit
{
	private static final int cost = 400;
	
	public Soldier()
	{
		currentHealth = 100;
		maximumHealth = 100;
		attackPower = 50;
		movementRange = 7;
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
