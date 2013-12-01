package com.jonivey.units;

public class Battleship extends Unit
{
	private static final int cost = 2000;
	
	public Battleship()
	{
		currentHealth = 160;
		maximumHealth = 160;
		attackPower = 90;
		movementRange = 6;
		attackRange = 17;
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
