package com.jonivey.messages;

public class UpdateLevelsMessage extends Message
{
	private PlayerMessage msg;
	
	public UpdateLevelsMessage(PlayerMessage msg)
	{
		this.msg = msg;
	}
	
	public PlayerMessage getPlayerMessage()
	{
		return msg;
	}
}
