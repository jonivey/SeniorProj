package com.jonivey.terrain;

import java.io.Serializable;

/**
 * @author Jon
 * Terrain represents the map's terrain.
 * There are four types of terrain:  Grass, Forest, Mountain, and Water.
 * 
 * Different terrains have different defense values, which allow the Unit
 * on that terrain to take less damage.
 * 
 * In addition, the Water terrain can only be traversed and occupied by water units.
 * Furthermore, Grass, Forest, and Mountain terrains can only be traversed and occupied by land units.
 * Air units can traverse and occupy ANY terrain.
 */
public class Terrain implements Serializable
{
	private int defense;
	private TerrainType terrainType;
	
	/**
	 * @author Jon
	 * TerrainType enumeration represents the four different Terrains
	 */
	public static enum TerrainType {
	/**
	 * Grass terrain, lowest defense value (for land units)
	 */
	GRASS,
	
	/**
	 * Forest terrain, medium defense value (for land units)
	 */
	FOREST,
	
	/**
	 * Mountain terrain, highest defense value (for land units)
	 */
	MOUNTAIN,
	
	/**
	 * Water terrain (for water units)
	 */
	WATER; }
	
	/**
	 * No argument constructor
	 * Sets terrainType to Grass and defense to 1.
	 */
	public Terrain()
	{
		terrainType = TerrainType.GRASS;
		defense = 1;
	}
	
	/**
	 * Sets the terrainType to terrType
	 * @param terrType - the specified TerrainType
	 */
	public Terrain(TerrainType terrType)
	{
		terrainType = terrType;
		
		// Set the defense based on the TerrainType
		switch(terrainType.ordinal())
		{
		case 0:				// Grass
			defense = 1;
			break;
		case 1:				// Forest
			defense = 2;
			break;
		case 2:				// Mountain
			defense = 3;
			break;
		case 3:				// Water
			defense = 1;
			break;
		default:
			defense = 1;
		}
	}
	
	/**
	 * @return - the Terrain's defense value
	 */
	public int getDefense()
	{
		return defense;
	}

	/**
	 * @return - the Terrain's TerrainType
	 */
	public TerrainType getType()
	{
		return terrainType;
	}
}
