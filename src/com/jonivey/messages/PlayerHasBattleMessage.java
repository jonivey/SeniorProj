package com.jonivey.messages;

public class PlayerHasBattleMessage extends Message
{
	private String username;
	
	public PlayerHasBattleMessage(String username)
	{
		this.username = username;
	}

	public Object getUsername()
	{
		return username;
	}

}
