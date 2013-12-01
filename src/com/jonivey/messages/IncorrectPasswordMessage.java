package com.jonivey.messages;

public class IncorrectPasswordMessage extends Message
{
	private String username;
	
	public IncorrectPasswordMessage(String username)
	{
		this.username = username;
	}

	public Object getUsername()
	{
		return username;
	}

}
