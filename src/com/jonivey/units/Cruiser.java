package com.jonivey.units;

public class Cruiser extends Unit
{
	private static final int cost = 1500;
	
	public Cruiser()
	{
		currentHealth = 125;
		maximumHealth = 125;
		attackPower = 70;
		movementRange = 5;
		attackRange = 14;
		unitType = Unit.WATER;
		owner = NO_PLAYER;
		hasAttacked = false;
		hasMoved = false;
	}
	
	public static int getCost()
	{
		return cost;
	}
}
