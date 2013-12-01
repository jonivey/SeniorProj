package com.jonivey.messages;

public class AcceptChallengeMessage extends Message
{
	private PlayerMessage msg;
	
	public AcceptChallengeMessage(PlayerMessage msg)
	{
		this.msg = msg;
	}
	
	public PlayerMessage getPlayerMessage()
	{
		return msg;
	}
}
