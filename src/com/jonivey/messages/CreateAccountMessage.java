package com.jonivey.messages;

public class CreateAccountMessage extends Message
{
	private String username, password;
	
	public CreateAccountMessage(String username, String password)
	{
		this.username = username;
		this.password = password;
	}

	public String getUsername()
	{
		return username;
	}

	public String getPassword()
	{
		return password;
	}
}
