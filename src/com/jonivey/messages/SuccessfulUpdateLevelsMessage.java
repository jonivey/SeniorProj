package com.jonivey.messages;

public class SuccessfulUpdateLevelsMessage extends Message
{
	private PlayerMessage msg;
	
	public SuccessfulUpdateLevelsMessage(PlayerMessage msg)
	{
		this.msg = msg;
	}

	public PlayerMessage getPlayerMessage()
	{
		return msg;
	}
}
