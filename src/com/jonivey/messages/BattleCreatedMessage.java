package com.jonivey.messages;

public class BattleCreatedMessage extends Message
{
	private PlayerMessage msg;
	
	public BattleCreatedMessage(PlayerMessage msg)
	{
		this.msg = msg;
	}

	public PlayerMessage getPlayerMessage()
	{
		return msg;
	}
}
