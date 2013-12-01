package com.jonivey.seniorproj;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.jonivey.messages.AlreadyHasChallengeMessage;
import com.jonivey.messages.Message;
import com.jonivey.messages.PlayerDoesNotExistMessage;
import com.jonivey.messages.PlayerMessage;
import com.jonivey.messages.SendChallengeMessage;
import com.jonivey.messages.SuccessfulChallengeMessage;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class ChallengeActivity extends Activity
{
	private Context context;
	private PlayerMessage myPlayerMessage;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_challenge);
		
		ActionBar actionBar = getActionBar();
	    actionBar.setDisplayHomeAsUpEnabled(false);
		
		Intent intent = getIntent();
		myPlayerMessage = (PlayerMessage) intent.getSerializableExtra("player_info");

		context = this.getBaseContext();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void requestBattle(View view)
	{
		EditText opponentText = (EditText) (EditText) findViewById(R.id.opponent_message);
		String opponent = opponentText.getText().toString();
		
		if(opponent == null)
			Toast.makeText(context, "Please enter a valid opponent", Toast.LENGTH_SHORT).show();
		else if(opponent.equals(myPlayerMessage.getUsername()))
			Toast.makeText(context, "You cannot challenge yourself", Toast.LENGTH_SHORT).show();
		else
		{
			SendChallengeMessage challengeMsg = new SendChallengeMessage(opponent, myPlayerMessage.getUsername());
			ChallengeOpponentTask task = new ChallengeOpponentTask();
			task.execute(challengeMsg);
			
			Message result = null;
			
			try
			{
				result = task.get(5000, TimeUnit.MILLISECONDS);
			} catch (InterruptedException e)
			{
				e.printStackTrace();
			} catch (ExecutionException e)
			{
				e.printStackTrace();
			} catch (TimeoutException e)
			{
				e.printStackTrace();
			}
			
			if(result instanceof SuccessfulChallengeMessage)
			{
				Toast.makeText(this.getBaseContext(), "Sent a challenge to " + opponent, Toast.LENGTH_SHORT).show();
			}
			else if(result instanceof AlreadyHasChallengeMessage)
			{
				Toast.makeText(this.getBaseContext(), opponent + " already has a challenge", Toast.LENGTH_SHORT).show();
			}
			else if(result instanceof PlayerDoesNotExistMessage)
			{
				Toast.makeText(this.getBaseContext(), opponent + " does not exist", Toast.LENGTH_SHORT).show();
			}
			else
			{
				Toast.makeText(this.getBaseContext(), "Error sending challenge", Toast.LENGTH_SHORT).show();
			}
		}
	}
	
	private class ChallengeOpponentTask extends AsyncTask<Message, Void, Message>
	{
		
		@Override
		protected Message doInBackground(Message... arg0)
		{
			Message returnedMsg = null;
			Socket sock = null;
			
			try
			{
				sock = new Socket("72.70.187.221", 31037);
				//sock = new Socket("10.0.2.2", 31037);
				OutputStream out = sock.getOutputStream();
				ObjectOutputStream objOut = new ObjectOutputStream(out);
				InputStream in = sock.getInputStream();
				ObjectInputStream objIn = new ObjectInputStream(in);
				
				Message msg = (Message) arg0[0];
				objOut.writeObject(msg);
				objOut.flush();
				
				Message receivedMsg = (Message)objIn.readObject();
				
				returnedMsg = receivedMsg;
				
				objOut.close();
				out.close();
				objIn.close();
				in.close();
				sock.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			return returnedMsg;
		}
	}
}
