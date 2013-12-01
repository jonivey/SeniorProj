package com.jonivey.messages;
import com.jonivey.gameplay.Battle;

public class PlayerMessage extends Message
{
	private String username, password, challenger;
	private int exp, vit, str, mob;
	private Battle battle;
	
	public PlayerMessage(String username, String password, int exp, int vit,
			int str, int mob, String challenge, Battle b)
	{
		this.username = username;
		this.password = password;
		this.exp = exp;
		this.vit = vit;
		this.str = str;
		this.mob = mob;
		this.challenger = challenge;
		this.battle = b;
	}

	public String getUsername()
	{
		return username;
	}

	public String getPassword()
	{
		return password;
	}

	public String getChallenger()
	{
		return challenger;
	}

	public int getExperience()
	{
		return exp;
	}

	public int getVitality()
	{
		return vit;
	}

	public int getStrength()
	{
		return str;
	}

	public int getMobility()
	{
		return mob;
	}
	
	public Battle getBattle()
	{
		return battle;
	}
	
	public void incrementStrength()
	{
		str++;
	}
	
	public void incrementMobility()
	{
		mob++;
	}
	
	public void incrementVitality()
	{
		vit++;
	}
	
	public void removeExperience(int amount)
	{
		if(exp >= amount)
			exp -= amount;
		else
			exp = 0;
	}
	
	public void addExperience(int amount)
	{
		exp += amount;
	}
	
	public void setBattle(Battle b)
	{
		battle = b;
	}
	
	public void setChallenger(String s)
	{
		challenger = s;
	}

	public void setStrength(int origStr)
	{
		str = origStr;
	}
	
	public void setMobility(int origMob)
	{
		mob = origMob;
	}
	
	public void setVitality(int origVit)
	{
		vit = origVit;
	}
	
	public void setExperience(int origExp)
	{
		exp = origExp;
	}
}
