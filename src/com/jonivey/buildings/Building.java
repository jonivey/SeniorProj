package com.jonivey.buildings;

import java.io.Serializable;

/**
 * @author Jon
 * The Building class represents buildings in the game.
 * There are 4 types of buildings:  Capitals, Factories, Ports, and Airports.
 */
public class Building implements Serializable
{
	protected int owner;
	protected BuildingType buildingType;
	
	/**
	 * @author Jon
	 * BuildingType consists of the four types of buildings.
	 */
	public static enum BuildingType {
	/**
	 * Represents a Capital building
	 */
	CAPITAL,
	
	/**
	 * Represents a Factory
	 */
	FACTORY,
	
	/**
	 * Represents an Airport
	 */
	AIRPORT,
	
	/**
	 * Represents a Port
	 */
	PORT; }
	
	/**
	 * No argument constructor for Building.
	 * Sets the owner to 0 (no player) and buildingType to a Capital building.
	 */
	public Building()
	{
		owner = 0;
		buildingType = Building.BuildingType.CAPITAL;
	}
	
	/**
	 * Sets the owner to 0 (no player) and buildingType to the type.
	 * @param type - the specified type
	 */
	public Building(BuildingType type)
	{
		owner = 0;
		buildingType = type;
	}
	
	/**
	 * Sets the owner to player and buildingType to type.
	 * @param type - the specified type
	 * @param player - the specified player (1 or 2)
	 */
	public Building(BuildingType type, int player)
	{
		owner = player;
		buildingType = type;
	}
	
	/**
	 * Sets the Building's owner to player
	 * @param player - the specified player (1 or 2)
	 */
	public void setOwner(int player)
	{
		owner = player;
	}

	/**
	 * @return - the Building's owner
	 */
	public int getOwner()
	{
		return owner;
	}
	
	/**
	 * Sets the buildingType to buildType
	 * @param buildType - the specified building type
	 */
	public void setType(BuildingType buildType)
	{
		buildingType = buildType;
	}
	
	/**
	 * @return - the Building's buildingType
	 */
	public BuildingType getType()
	{
		return buildingType;
	}
}
