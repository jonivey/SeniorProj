package com.jonivey.gameplay;

import java.io.Serializable;

import com.jonivey.buildings.Building;
import com.jonivey.terrain.Terrain;
import com.jonivey.units.Unit;

/**
 * @author Jon
 * The Cell class contains up to one Terrain, one Building, and one Unit.
 * The game will have a two dimensional array of Cells as the map.
 */
public class Cell implements Serializable
{
	private Terrain myTerrain;
	private Building myBuilding;
	private Unit myUnit;
	
	/**
	 * No argument constructor.
	 * Sets myTerrain to a new Grass Terrain; sets myBuilding to null;
	 * and sets myUnit to null.
	 */
	public Cell()
	{
		myTerrain = new Terrain(Terrain.TerrainType.GRASS);
		myBuilding = null;
		myUnit = null;
	}
	
	/**
	 * Sets myTerrain to terrain.
	 * @param terrain - the specified Terrain
	 */
	public Cell(Terrain terrain)
	{
		myTerrain = terrain;
		myBuilding = null;
		myUnit = null;
	}
	
	/**
	 * Sets myTerrain to terrain and myBuilding to building.
	 * @param terrain - the specified terrain
	 * @param building - the specified building
	 */
	public Cell(Terrain terrain, Building building)
	{
		myTerrain = terrain;
		myBuilding = building;
		myUnit = null;
	}
	
	/**
	 * Sets myTerrain to terrain and myUnit to unit.
	 * @param terrain - the specified terrain
	 * @param unit - the specified unit
	 */
	public Cell(Terrain terrain, Unit unit)
	{
		myTerrain = terrain;
		myBuilding = null;
		myUnit = unit;
	}
	
	/**
	 * Sets myTerrain to terrain, myBuilding to building, and myUnit to unit.
	 * @param terrain - the specified terrain
	 * @param building - the specified building
	 * @param unit - the specified unit
	 */
	public Cell(Terrain terrain, Building building, Unit unit)
	{
		myTerrain = terrain;
		myBuilding = building;
		myUnit = unit;
	}
	
	/**
	 * Sets the Unit to unit.
	 * @param unit - the specified unit
	 */
	public void setUnit(Unit unit)
	{
		myUnit = unit;
	}
	
	/**
	 * @return - the unit that this Cell contains
	 */
	public Unit getUnit()
	{
		return myUnit;
	}
	
	/**
	 * @return - the terrain that this Cell contains
	 */
	public Terrain getTerrain()
	{
		return myTerrain;
	}
	
	/**
	 * Sets the Building to building.
	 * @param building - the specified building
	 */
	public void setBuilding(Building building)
	{
		myBuilding = building;
	}
	
	/**
	 * @return - the building that this Cell contains
	 */
	public Building getBuilding()
	{
		return myBuilding;
	}
}
