package com.jonivey.messages;

public class PasswordTooLongMessage extends Message
{
	private String password;
	
	public PasswordTooLongMessage(String password)
	{
		this.password = password;
	}

	public String getPassword()
	{
		return password;
	}

}
