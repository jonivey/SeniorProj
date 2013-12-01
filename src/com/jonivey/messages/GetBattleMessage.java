package com.jonivey.messages;

public class GetBattleMessage extends Message
{
	private String username, password;
	
	public GetBattleMessage(String name, String pass)
	{
		username = name;
		password = pass;
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
