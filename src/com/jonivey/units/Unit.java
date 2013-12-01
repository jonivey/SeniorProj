package com.jonivey.units;

import java.io.Serializable;

/**
 * @author Jon
 * The Unit class represents a Unit in the game.
 * A Unit has current health, max health, attack power (damage), a range of attack,
 * a movement range, a type, and an owner (player 1 or 2)
 * 
 * unitType represents what unit it is:
 * 0 - Soldier
 * 1 - Tank
 * 2 - Cruiser
 * 3 - Battleship
 * 4 - Helicopter
 * 5 - Jet
 * 
 * Units will be able to move, attack other units, and capture buildings.
 *
 * (NOTE:  MAY CHANGE owner TO AN ENUMERATION AND MAY CHANGE unitType TO AN ENUMERATION.)
 */
public class Unit implements Serializable
{
	protected int currentHealth, maximumHealth, attackPower, attackRange,
				movementRange, unitType, owner;
	protected boolean hasMoved, hasAttacked;
	
	public static final int LAND = 0;
	public static final int WATER = 1;
	public static final int AIR = 2;
	
	public static final int NO_PLAYER = 0;
	public static final int PLAYER_ONE = 1;
	public static final int PLAYER_TWO = 2;

	/**
	 * No argument constructor
	 * Sets currentHealth to 100, maximumHealth to 100, attackPower to 50,
	 * attackRange to 5 squares, movementRange to 5 squares, unitType to 0,
	 * and owner to 0 (no player)
	 */
	public Unit()
	{
		currentHealth = 100;
		maximumHealth = 100;
		attackPower = 50;
		attackRange = 5;
		movementRange = 5;
		unitType = 0;
		owner = 0;
		hasAttacked = false;
		hasMoved = false;
	}

	/**
	 * @return - the Unit's maximumHealth
	 */
	public int getMaximumHealth()
	{
		return maximumHealth;
	}
	
	public String toString()
	{
		return "Current Health:  " + currentHealth
				+"\nMaximum Health:  " + maximumHealth
				+"\nAttack Power:  " + attackPower
				+"\nAttack Range:  " + attackRange
				+"\nMovement Range:  " + movementRange
				+"\nUnit Type:  " + unitType
				+"\nOwner:  " + owner
				+"\nMoved?  " + hasMoved
				+"\nAttacked?  " + hasAttacked;
	}

	/**
	 * @return - the Unit's currentHealth
	 */
	public int getCurrentHealth()
	{
		return currentHealth;
	}

	/**
	 * @return - the Unit's attackPower
	 */
	public int getAttackPower()
	{
		return attackPower;
	}

	/**
	 * @return - the Unit's attackRange
	 */
	public int getAttackRange()
	{
		return attackRange;
	}

	/**
	 * @return - the Unit's movementRange
	 */
	public int getMovementRange()
	{
		return movementRange;
	}

	/**
	 * @return - the Unit's unitType
	 */
	public int getUnitType()
	{
		return unitType;
	}

	/**
	 * @return - the unit's owner
	 */
	public int getOwner()
	{
		return owner;
	}

	/**
	 * Sets the Unit's maximumHealth to max
	 * @param max - the max health of the Unit
	 */
	public void setMaximumHealth(int max)
	{
		maximumHealth = max;
	}

	/**
	 * Sets the Unit's currentHealth to current
	 * @param current - the current health of the Unit
	 */
	public void setCurrentHealth(int current)
	{
		currentHealth = current;
	}

	/**
	 * Sets the Unit's attackPower to attack
	 * @param attack - the attack damage of the Unit
	 */
	public void setAttackPower(int attack)
	{
		attackPower = attack;
	}

	/**
	 * Sets the Unit's attackRange to aRange
	 * @param aRange - the attack range of the Unit
	 */
	public void setAttackRange(int aRange)
	{
		attackRange = aRange;
	}

	/**
	 * Sets the Unit's movementRange to mRange
	 * @param mRange - the movement range of the Unit
	 */
	public void setMovementRange(int mRange)
	{
		movementRange = mRange;
	}

	/**
	 * Sets the Unit's unitType to type
	 * @param type - the specified type of the Unit
	 */
	public void setUnitType(int type)
	{
		unitType = type;
	}

	/**
	 * Sets the Unit's owner to player
	 * @param player - the specified player (player 1 or 2)
	 */
	public void setOwner(int player)
	{
		owner = player;
	}

	/**
	 * @return - whether the Unit has attacked this turn or not
	 */
	public boolean hasAttacked()
	{
		return hasAttacked;
	}

	/**
	 * @return - whether the Unit has moved this turn or not
	 */
	public boolean hasMoved()
	{
		return hasMoved;
	}

	/**
	 * Sets the Unit's hasAttacked boolean to attackBoolean
	 * @param attackBoolean - whether the Unit has attacked this turn or not
	 */
	public void setAttacked(boolean attackBoolean)
	{
		hasAttacked = attackBoolean;
	}

	/**
	 * Sets the Unit's hasMoved boolean to moveBoolean
	 * @param moveBoolean - whether the Unit has moved this turn or not
	 */
	public void setMoved(boolean moveBoolean)
	{
		hasMoved = moveBoolean;
	}

	public void takeDamage(int damage)
	{
		currentHealth -= damage;
	}
}
