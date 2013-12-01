package com.jonivey.messages;

public class SendChallengeMessage extends Message
{
	private String opponent, challenger;
	
	public SendChallengeMessage(String opponent, String challenger)
	{
		this.opponent = opponent;
		this.challenger = challenger;
	}

	public String getOpponent()
	{
		return opponent;
	}
	
	public String getChallenger()
	{
		return challenger;
	}
}
