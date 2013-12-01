package com.jonivey.messages;

public class SubmitGameoverMessage extends Message
{
	private PlayerMessage msg;
	
	public SubmitGameoverMessage(PlayerMessage msg)
	{
		this.msg = msg;
	}
	
	public PlayerMessage getPlayerMessage()
	{
		return msg;
	}
}
