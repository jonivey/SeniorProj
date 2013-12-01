package com.jonivey.messages;

public class PlayerDoesNotExistMessage extends Message
{
	private String username;
	
	public PlayerDoesNotExistMessage(String username)
	{
		this.username = username;
	}

	public Object getUsername()
	{
		return username;
	}

}
