package com.jonivey.messages;

public class BattleCreationErrorMessage extends Message
{
	private String username1, username2;
	
	public BattleCreationErrorMessage(String username1, String username2)
	{
		this.username1 = username1;
		this.username2 = username2;
	}

	public Object getUsernameOne()
	{
		return username1;
	}

	public Object getUsernameTwo()
	{
		return username2;
	}

}
