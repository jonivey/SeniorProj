package com.jonivey.gameplay;

import java.io.Serializable;
import java.util.Stack;

import android.graphics.Point;

import com.jonivey.buildings.Building;
import com.jonivey.terrain.Terrain;
import com.jonivey.units.*;

/**
 * @author Jon
 * Map contains a two dimensional array of Cells, which
 * represents the game's map.
 */
public class Map implements Serializable
{
	private Cell[][] cells;
	private int selectedRow, selectedCol, numRows, numCols;
	
	/**
	 * No argument constructor
	 * sets the map's cells to a 10 by 10 array of new Cells
	 */
	public Map()
	{
		cells = new Cell[10][10];
		
		for(int row = 0; row < 10; row++)
		{
			for(int col = 0; col < 10; col++)
			{
				cells[row][col] = new Cell();
			}
		}
		
		numRows = 10;
		numCols = 10;
	}

	/**
	 * Sets cells to myCells
	 * @param myCells - the specified Cells (used by Server code to create map)
	 */
	public Map(Cell[][] myCells, int rows, int cols)
	{
		cells = myCells;
		numRows = rows;
		numCols = cols;
	}

	/**
	 * @param row - specified row
	 * @param col - specified column
	 * @return - the cell at the specified row and column of the map
	 */
	public Cell getCell(int row, int col)
	{
		return cells[row][col];
	}

	public void selectCell(int row, int col)
	{
			selectedRow = row;
			selectedCol = col;
	}

	public Unit getSelectedUnit()
	{
		return cells[selectedRow][selectedCol].getUnit();
	}

	public void moveUnit(int row, int col)
	{	
		if(cells[selectedRow][selectedCol].getUnit() != null)
		{
			if(cells[row][col].getUnit() == null && cells[selectedRow][selectedCol].getUnit().hasMoved() == false)
			{
				if(canMoveTo(row, col))
				{
					Unit u = cells[selectedRow][selectedCol].getUnit();
					u.setMoved(true);
					cells[row][col].setUnit(u);
					cells[selectedRow][selectedCol].setUnit(null);
				}
			}
		}
	}

	public boolean attackUnit(int row, int col)
	{
		if(cells[selectedRow][selectedCol].getUnit() != null && cells[row][col].getUnit() != null)
		{
			if(cells[row][col].getUnit().getOwner() != cells[selectedRow][selectedCol].getUnit().getOwner() && (cells[selectedRow][selectedCol].getUnit().hasAttacked() == false) && inRange(row, col))
			{
				Unit u = cells[selectedRow][selectedCol].getUnit();
				u.setAttacked(true);
				cells[row][col].getUnit().takeDamage((int)((double)cells[selectedRow][selectedCol].getUnit().getAttackPower() * ((double)(10 - (cells[row][col].getTerrain().getDefense() - 1)) / 10)));
				
				if(cells[row][col].getUnit().getCurrentHealth() <= 0)
				{
					cells[row][col].setUnit(null);
					return true;
				}
			}
		}
		
		return false;
	}

	public boolean captureBuilding()
	{
		if(cells[selectedRow][selectedCol].getUnit() != null)
		{
			if(cells[selectedRow][selectedCol].getBuilding() != null)
			{
				if(cells[selectedRow][selectedCol].getBuilding().getType() == Building.BuildingType.CAPITAL && cells[selectedRow][selectedCol].getBuilding().getOwner() != cells[selectedRow][selectedCol].getUnit().getOwner())
				{
					cells[selectedRow][selectedCol].getBuilding().setOwner(cells[selectedRow][selectedCol].getUnit().getOwner());
					return true;
				}
				
				else
				{
					cells[selectedRow][selectedCol].getBuilding().setOwner(cells[selectedRow][selectedCol].getUnit().getOwner());
				}
			}
		}
		
		return false;
	}

	public void buildUnit(String type, int str, int vit, int mob)
	{
		if(cells[selectedRow][selectedCol].getBuilding() != null)
		{
			if(cells[selectedRow][selectedCol].getUnit() == null)
			{
				Unit builtUnit;
				
				if(type.equals("Tank"))
					builtUnit = new Tank();
				else if(type.equals("Helicopter"))
					builtUnit = new Helicopter();
				else if(type.equals("Jet"))
					builtUnit = new Jet();
				else if(type.equals("Cruiser"))
					builtUnit = new Cruiser();
				else if(type.equals("Battleship"))
					builtUnit = new Battleship();
				else
					builtUnit = new Soldier();
				
				builtUnit.setOwner(cells[selectedRow][selectedCol].getBuilding().getOwner());
				builtUnit.setAttacked(true);
				builtUnit.setMoved(true);
				
				// Set the attributes based on the player's levels
				builtUnit.setAttackPower(builtUnit.getAttackPower() + ((str - 1) * 5));
				builtUnit.setCurrentHealth(builtUnit.getCurrentHealth() + ((vit - 1) * 5));
				builtUnit.setMaximumHealth(builtUnit.getMaximumHealth() + ((vit - 1) * 5));
				builtUnit.setMovementRange(builtUnit.getMovementRange() + (mob / 5));
				
				cells[selectedRow][selectedCol].setUnit(builtUnit);
			}
		}
	}
	
	public Cell getSelectedCell()
	{
		return cells[selectedRow][selectedCol];
	}
	
	public int getSelectedRow()
	{
		return selectedRow;
	}
	
	public int getSelectedCol()
	{
		return selectedCol;
	}
	
	public boolean inRange(int row, int col)
	{
		if(cells[selectedRow][selectedCol].getUnit() == null)
			return false;
		
		else
		{
			int distance = Math.abs(selectedRow - row) + Math.abs(selectedCol - col);
			
			if(distance <= cells[selectedRow][selectedCol].getUnit().getAttackRange())
				return true;
			else
				return false;
		}
	}
	
	public boolean canMoveTo(int row, int col)
	{
		int numOptions = 0, distance = 0;
	    Point previous = new Point(), current = new Point(), next = new Point();
	    Stack<Point> visited = new Stack<Point>(), options = new Stack<Point>(), alternatives = new Stack<Point>();
	    
	    current.x = selectedRow;
	    current.y = selectedCol;
	    
	    previous.x = -1;
	    previous.y = -1;
	    
	    // Air units can move on any type of terrain, just has to be within
	    // movement range
	    if(cells[selectedRow][selectedCol].getUnit().getUnitType() == Unit.AIR)
	    {
	    	int cellDistance = Math.abs(selectedRow - row) + Math.abs(selectedCol - col);
			
			if(cellDistance <= cells[selectedRow][selectedCol].getUnit().getMovementRange())
				return true;
			else
				return false;
	    }
	    
	    // Determine whether the land unit can move to the cell
	    if(cells[selectedRow][selectedCol].getUnit().getUnitType() == Unit.LAND)
	    {
		    while(current.x != row || current.y != col)
		    {
		        visited.push(new Point(current.x, current.y));
		        
		        // Check to see if the adjacent spaces are available or not
		        if(current.x-1 >= 0)
			        if(cells[current.x - 1][current.y].getTerrain().getType() != Terrain.TerrainType.WATER && (current.x - 1) != previous.x && distance < cells[selectedRow][selectedCol].getUnit().getMovementRange())
			        {
			            options.push(new Point(current.x - 1, current.y));
			            numOptions++;
			        }
		        
		        if(current.x+1 < numRows)
			        if(cells[current.x + 1][current.y].getTerrain().getType() != Terrain.TerrainType.WATER && (current.x + 1) != previous.x && distance < cells[selectedRow][selectedCol].getUnit().getMovementRange())
			        {
			            options.push(new Point(current.x + 1, current.y));
			            numOptions++;
			        }
		        
		        if(current.y-1 >= 0)
			        if(cells[current.x][current.y - 1].getTerrain().getType() != Terrain.TerrainType.WATER && (current.y - 1) != previous.y && distance < cells[selectedRow][selectedCol].getUnit().getMovementRange())
			        {
			            options.push(new Point(current.x, current.y - 1));
			            numOptions++;
			        }
		        
		        if(current.y+1 < numCols)
			        if(cells[current.x][current.y + 1].getTerrain().getType() != Terrain.TerrainType.WATER && (current.y + 1) != previous.y && distance < cells[selectedRow][selectedCol].getUnit().getMovementRange())
			        {
			            options.push(new Point(current.x, current.y + 1));
			            numOptions++;
			        }
		        
		        
		        // No available neighboring cells, or the unit is at it's max movement distance
		        if(numOptions == 0)
		        {
		            next = visited.pop();
		            
		            // Back track until a decision token is found
		            while(next.x != -1 && visited.isEmpty() == false)
		            {
		                next = visited.pop();
		                
		                if(next.x != -1)
		                {
		                	distance--;
		                }
		            }
		            
		            // The unit was not able to move to the cell
		            if(visited.isEmpty() == true)
		            {
		                return false;
		            }
		            
		            // There are alternative paths that are possible
		            else
		            {
		                next = alternatives.pop();
		            }
		        }
		        
		        // Continuing
		        else if(numOptions == 1)
		        {
		            next = options.pop();
		        }
		        
		        // Intersection
		        else
		        {  
		            next = options.pop();
		            
		            // Push the remaining options onto the alternatives stack
		            // Also push a decision token
		            while(options.isEmpty() != true)
		            {
		                visited.push(new Point(-1, -1));
		                visited.push(new Point(current.x, current.y));
		                alternatives.push(options.pop());
		            }
		        }
		        
		        numOptions = 0;
		        
		        previous.x = current.x;
		        previous.y = current.y;
		        
		        current.x = next.x;
		        current.y = next.y;
		        distance++;
		    }
	    }
	    
	    // Determine whether a water unit can move to the cell
	    else
	    {
		    while(current.x != row || current.y != col)
		    {
		        visited.push(new Point(current.x, current.y));
		        
		        // Check to see if the adjacent spaces are available or not
		        if(current.x-1 >= 0)
			        if(cells[current.x - 1][current.y].getTerrain().getType() == Terrain.TerrainType.WATER && (current.x - 1) != previous.x && distance < cells[selectedRow][selectedCol].getUnit().getMovementRange())
			        {
			            options.push(new Point(current.x - 1, current.y));
			            numOptions++;
			        }
		        
		        if(current.x+1 < numRows)
			        if(cells[current.x + 1][current.y].getTerrain().getType() == Terrain.TerrainType.WATER && (current.x + 1) != previous.x && distance < cells[selectedRow][selectedCol].getUnit().getMovementRange())
			        {
			            options.push(new Point(current.x + 1, current.y));
			            numOptions++;
			        }
		        
		        if(current.y-1 >= 0)
			        if(cells[current.x][current.y - 1].getTerrain().getType() == Terrain.TerrainType.WATER && (current.y - 1) != previous.y && distance < cells[selectedRow][selectedCol].getUnit().getMovementRange())
			        {
			            options.push(new Point(current.x, current.y - 1));
			            numOptions++;
			        }
		        
		        if(current.y+1 < numCols)
			        if(cells[current.x][current.y + 1].getTerrain().getType() == Terrain.TerrainType.WATER && (current.y + 1) != previous.y && distance < cells[selectedRow][selectedCol].getUnit().getMovementRange())
			        {
			            options.push(new Point(current.x, current.y + 1));
			            numOptions++;
			        }
		        
		        
		        // There are not available neighboring cells, or the unit has reached its maximum movement distance
		        if(numOptions == 0)
		        {
		            next = visited.pop();
		            
		            // Back track until a decision token is found
		            while(next.x != -1 && visited.isEmpty() == false)
		            {
		                next = visited.pop();
		                
		                if(next.x != -1)
		                {
		                	distance--;
		                }
		            }
		            
		            // The unit cannot move to the cell
		            if(visited.isEmpty() == true)
		            {
		                return false;
		            }
		            
		            // There are alternative paths for the unit to try
		            else
		            {
		                next = alternatives.pop();
		            }
		        }
		        
		        // Continuing
		        else if(numOptions == 1)
		        {
		            next = options.pop();
		        }
		        
		        // Intersection
		        else
		        {  
		            next = options.pop();
		            
		            // Push the remaining options onto the alternatives stack
		            // Also push a decision token
		            while(options.isEmpty() != true)
		            {
		                visited.push(new Point(-1, -1));
		                visited.push(new Point(current.x, current.y));
		                alternatives.push(options.pop());
		            }
		        }
		        
		        numOptions = 0;
		        
		        previous.x = current.x;
		        previous.y = current.y;
		        
		        current.x = next.x;
		        current.y = next.y;
		        distance++;
		    }
	    }
	    
	    return true;
	}
	
	public int getNumRows()
	{
		return numRows;
	}
	
	public int getNumCols()
	{
		return numCols;
	}
}
