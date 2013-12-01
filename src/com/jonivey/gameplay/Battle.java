package com.jonivey.gameplay;

import java.io.Serializable;

import com.jonivey.units.Battleship;
import com.jonivey.units.Cruiser;
import com.jonivey.units.Helicopter;
import com.jonivey.units.Jet;
import com.jonivey.units.Soldier;
import com.jonivey.units.Tank;
import com.jonivey.units.Unit;

/**
 * @author Jon
 * The Battle class contains the game's Battle information.
 * Every Battle has a playerOne, a playerTwo, and a map.
 * In addition, the Battle keeps track of who's turn it is.
 * 
 * If it's a player's turn, then only he can move his units, attack enemy
 * units, capture buildings, and build new units.
 */
public class Battle implements Serializable
{
	private String whosTurn;
	private Map myMap;
	private Player player1, player2;
	private boolean gameOver;
	private String winner;
	
	/**
	 * @param player1 - player1's username
	 * @param player2 - player2's username
	 * @param map - the Battle's map
	 * @param turn - username of the player who's turn it is
	 */
	public Battle(Player player1, Player player2, Map map, String turn)
	{
		this.player1 = player1;
		this.player2 = player2;
		whosTurn = turn;
		myMap = map;
		gameOver = false;
		winner = null;
	}

	/**
	 * @return - the username of playerOne
	 */
	public Player getPlayerOne()
	{
		return player1;
	}

	/**
	 * @return - the username of playerTwo
	 */
	public Player getPlayerTwo()
	{
		return player2;
	}

	/**
	 * @return - the username of the player who's turn it is
	 */
	public String getTurn()
	{
		return whosTurn;
	}

	/**
	 * @return - the Battle's map
	 */
	public Map getMap()
	{
		return myMap;
	}

	/**
	 * Sets the current turn to player
	 * @param player - the username of the player who's turn it is
	 */
	public void setTurn(String player)
	{
		whosTurn = player;
	}
	
	public void selectCell(int row, int col)
	{
		myMap.selectCell(row, col);
	}

	public Unit getSelectedUnit()
	{
		return myMap.getSelectedUnit();
	}

	public void moveUnit(int row, int col)
	{
		myMap.moveUnit(row, col);
	}

	public boolean attackUnit(int row, int col)
	{
		return myMap.attackUnit(row, col);
	}

	public void captureBuilding()
	{
		gameOver = myMap.captureBuilding();
		
		if(gameOver)
		{
			winner = whosTurn;
		}
		//return myMap.captureBuilding();
	}

	public void buildUnit(String type)
	{
		int currentCoins;
		int unitCost;
		
		if(type.equals("Tank"))
			unitCost = Tank.getCost();
		else if(type.equals("Helicopter"))
			unitCost = Helicopter.getCost();
		else if(type.equals("Jet"))
			unitCost = Jet.getCost();
		else if(type.equals("Cruiser"))
			unitCost = Cruiser.getCost();
		else if(type.equals("Battleship"))
			unitCost = Battleship.getCost();
		else
			unitCost = Soldier.getCost();
		
		if(player1.getName().equals(whosTurn))
		{
			currentCoins = player1.getCoins();
			
			if(currentCoins >= unitCost)
			{
				player1.setCoins(currentCoins - unitCost);
				myMap.buildUnit(type, player1.getStrengthLevel(), player1.getVitalityLevel(), player1.getMobilityLevel());
			}
		}
		else
		{
			currentCoins = player2.getCoins();
			
			if(currentCoins >= unitCost)
			{
				player2.setCoins(currentCoins - unitCost);
				myMap.buildUnit(type, player2.getStrengthLevel(), player2.getVitalityLevel(), player2.getMobilityLevel());
			}
		}
	}
	
	public Cell getSelectedCell()
	{
		return myMap.getSelectedCell();
	}
	
	public void setGameOver(boolean bool)
	{
		gameOver = bool;
	}
	
	public boolean isGameOver()
	{
		return gameOver;
	}
	
	public void setWinner(String win)
	{
		winner = win;
	}
	
	public String getWinner()
	{
		return winner;
	}
}
