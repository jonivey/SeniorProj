package com.jonivey.messages;

public class AccountCreationErrorMessage extends Message
{
	private String username;
	
	public AccountCreationErrorMessage(String username)
	{
		this.username = username;
	}

	public String getUsername()
	{
		return username;
	}

}
