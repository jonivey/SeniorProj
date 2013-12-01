package com.jonivey.messages;

public class ResignMessage extends Message
{
	private PlayerMessage msg;
	
	public ResignMessage(PlayerMessage msg)
	{
		this.msg = msg;
	}
	
	public PlayerMessage getPlayerMessage()
	{
		return msg;
	}
}
