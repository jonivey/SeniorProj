package com.jonivey.seniorproj;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.jonivey.messages.AcceptChallengeMessage;
import com.jonivey.messages.BattleCreatedMessage;
import com.jonivey.messages.DeclineChallengeMessage;
import com.jonivey.messages.Message;
import com.jonivey.messages.PlayerHasBattleMessage;
import com.jonivey.messages.PlayerMessage;
import com.jonivey.messages.SuccessfulDeclineMessage;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class ViewChallengeActivity extends Activity
{
	private Context context;
	private PlayerMessage myPlayerMessage;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_challenge);
		
		ActionBar actionBar = getActionBar();
	    actionBar.setDisplayHomeAsUpEnabled(false);
		
		Intent intent = getIntent();
		myPlayerMessage = (PlayerMessage) intent.getSerializableExtra("player_info");
		
		TextView tv = (TextView) findViewById(R.id.challenger_message);
		
		if(myPlayerMessage.getChallenger() == null)
			tv.setText("no challenge");
		else
			tv.setText(myPlayerMessage.getChallenger());
		
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
	
	public void acceptChallenge(View view)
	{
		if(myPlayerMessage.getChallenger() == null)
			Toast.makeText(context, "You do not have a challenge", Toast.LENGTH_SHORT).show();
		
		else
		{
			AcceptChallengeMessage msg = new AcceptChallengeMessage(myPlayerMessage);
			DeclineChallengeTask task = new DeclineChallengeTask();
			task.execute(msg);
			
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
			
			if(result instanceof BattleCreatedMessage)
			{
				BattleCreatedMessage castedMsg = (BattleCreatedMessage)result;
				Toast.makeText(this.getBaseContext(), "Successfully created a battle", Toast.LENGTH_SHORT).show();
				myPlayerMessage = castedMsg.getPlayerMessage();
				TextView tv = (TextView) findViewById(R.id.challenger_message);
				tv.setText("no challenge");
			}
			else if(result instanceof PlayerHasBattleMessage)
			{
				PlayerHasBattleMessage castedMsg = (PlayerHasBattleMessage)result;
				
				if(castedMsg.getUsername().equals(myPlayerMessage.getUsername()))
					Toast.makeText(this.getBaseContext(), "You already have a battle", Toast.LENGTH_SHORT).show();
				else
					Toast.makeText(this.getBaseContext(), castedMsg.getUsername() + " already has a battle", Toast.LENGTH_SHORT).show();
			}
			else
			{
				Toast.makeText(this.getBaseContext(), "Error accepting challenge", Toast.LENGTH_SHORT).show();
			}
		}
	}
	
	public void declineChallenge(View view)
	{
		if(myPlayerMessage.getChallenger() == null)
			Toast.makeText(context, "You do not have a challenge", Toast.LENGTH_SHORT).show();
		
		else
		{
			DeclineChallengeMessage msg = new DeclineChallengeMessage(myPlayerMessage.getUsername());
			DeclineChallengeTask task = new DeclineChallengeTask();
			task.execute(msg);
			
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
			
			if(result instanceof SuccessfulDeclineMessage)
			{
				TextView tv = (TextView) findViewById(R.id.challenger_message);
				tv.setText("no challenge");
				myPlayerMessage.setChallenger(null);
			}
			else
			{
				Toast.makeText(this.getBaseContext(), "Error declining challenge", Toast.LENGTH_SHORT).show();
			}
		}
	}
	
	public void onBackPressed()
	{
		Intent returnIntent = new Intent();
		returnIntent.putExtra("player_info_modified", myPlayerMessage);
		setResult(RESULT_OK, returnIntent);
		finish();
		return;
	}
	
	private class DeclineChallengeTask extends AsyncTask<Message, Void, Message>
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
