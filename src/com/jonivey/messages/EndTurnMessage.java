package com.jonivey.messages;

public class EndTurnMessage extends Message
{
	private PlayerMessage msg;
	
	public EndTurnMessage(PlayerMessage msg)
	{
		this.msg = msg;
	}
	
	public PlayerMessage getPlayerMessage()
	{
		return msg;
	}
}
