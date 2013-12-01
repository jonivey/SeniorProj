package com.jonivey.messages;

public class SuccessfulUpdateMessage extends Message
{
	private PlayerMessage player;
	
	public SuccessfulUpdateMessage(PlayerMessage msg)
	{
		player = msg;
	}
	
	public PlayerMessage getPlayerMessage()
	{
		return player;
	}
}
