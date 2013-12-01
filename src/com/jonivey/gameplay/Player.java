package com.jonivey.gameplay;

import java.io.Serializable;

public class Player implements Serializable
{
	private String name;
	private int experience, vitLevel, strLevel, mobLevel, coins;
	public final static int EXP_PER_LEVEL = 5000;
	
	public Player(String username)
	{
		name = username;
		
		experience = 0;
		vitLevel = 1;
		strLevel = 1;
		mobLevel = 1;
		coins = 0;
	}

	public Player(String username, int exp, int vit, int str, int mob,
			int coins)
	{
		name = username;
		experience = exp;
		vitLevel = vit;
		strLevel = str;
		mobLevel = mob;
		this.coins = coins;
	}

	public int getVitalityLevel()
	{
		return vitLevel;
	}

	public int getStrengthLevel()
	{
		return strLevel;
	}

	public int getMobilityLevel()
	{
		return mobLevel;
	}

	public int getExperience()
	{
		return experience;
	}

	public int getCoins()
	{
		return coins;
	}

	public String getName()
	{
		return name;
	}

	public void setExperience(int exp)
	{
		experience = exp;
	}

	public void setCoins(int coins)
	{
		if(coins < 0)
			this.coins = 0;
		else
			this.coins = coins;
	}

	public void increaseStrength()
	{
		if(experience >= Player.EXP_PER_LEVEL)
		{
			experience -= Player.EXP_PER_LEVEL;
			strLevel++;
		}
	}

	public void increaseVitality()
	{
		if(experience >= Player.EXP_PER_LEVEL)
		{
			experience -= Player.EXP_PER_LEVEL;
			vitLevel++;
		}
	}

	public void increaseMobility()
	{
		if(experience >= Player.EXP_PER_LEVEL)
		{
			experience -= Player.EXP_PER_LEVEL;
			mobLevel++;
		}
	}

}
