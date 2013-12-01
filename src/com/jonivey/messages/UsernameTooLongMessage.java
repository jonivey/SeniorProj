package com.jonivey.messages;

public class UsernameTooLongMessage extends Message
{
	private String username;

	public UsernameTooLongMessage(String username)
	{
		this.username = username;
	}

	public String getUsername()
	{
		return username;
	}

}
