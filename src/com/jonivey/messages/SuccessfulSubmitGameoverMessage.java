package com.jonivey.messages;

public class SuccessfulSubmitGameoverMessage extends Message
{
	private PlayerMessage msg;
	
	public SuccessfulSubmitGameoverMessage(PlayerMessage msg)
	{
		this.msg = msg;
	}
	
	public PlayerMessage getPlayerMessage()
	{
		return msg;
	}
}
