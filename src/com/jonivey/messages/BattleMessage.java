package com.jonivey.messages;
import com.jonivey.gameplay.Battle;


public class BattleMessage extends Message
{
	private Battle battle;
	
	public BattleMessage(Battle b)
	{
		battle = b;
	}
	
	public Battle getBattle()
	{
		return battle;
	}
}
