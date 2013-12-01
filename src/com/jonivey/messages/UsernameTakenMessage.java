package com.jonivey.messages;

public class UsernameTakenMessage extends Message
{
	private String username;
	
	public UsernameTakenMessage(String username)
	{
		this.username = username;
	}

	public String getUsername()
	{
		return username;
	}

}
