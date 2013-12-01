package com.jonivey.messages;

public class LoginMessage extends Message
{
	private String username, password;
	
	public LoginMessage(String username, String password)
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
