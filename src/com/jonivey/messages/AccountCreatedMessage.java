package com.jonivey.messages;

public class AccountCreatedMessage extends Message
{
	private String username;
	
	public AccountCreatedMessage(String username)
	{
		this.username = username;
	}
	
	public String getUsername()
	{
		return username;
	}
}
