package com.jonivey.messages;

public class DeclineChallengeMessage extends Message
{
	private String username;
	
	public DeclineChallengeMessage(String name)
	{
		username = name;
	}
	
	public String getUsername()
	{
		return username;
	}
}
