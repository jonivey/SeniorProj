package com.jonivey.messages;

public class SuccessfulResignMessage extends Message
{
	private PlayerMessage msg;
	
	public SuccessfulResignMessage(PlayerMessage msg)
	{
		this.msg = msg;
	}
	
	public PlayerMessage getPlayerMessage()
	{
		return msg;
	}
}
