package com.jonivey.seniorproj;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.jonivey.messages.EndTurnMessage;
import com.jonivey.messages.Message;
import com.jonivey.messages.PlayerMessage;
import com.jonivey.messages.ResignMessage;
import com.jonivey.messages.SuccessfulUpdateMessage;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

public class ViewBattleActivity extends Activity
{
	private GameSurfaceView view;
	private GameThread thread;
	private PlayerMessage myPlayerMessage;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_battle);
		
		ActionBar actionBar = getActionBar();
	    actionBar.setDisplayHomeAsUpEnabled(false);
		
		FrameLayout layout = (FrameLayout) findViewById(R.id.frame_layout);
		
		Intent intent = getIntent();
		myPlayerMessage = (PlayerMessage) intent.getSerializableExtra("player_info");

		view = new GameSurfaceView(this.getBaseContext(), myPlayerMessage, this);
        thread = view.getThread();
        
        try
        {
        	layout.addView(view);
        }
        catch(Exception e)
        {
        	e.printStackTrace();
        }
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.viewbattle, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch(item.getItemId())
		{
			case R.id.end_turn:
				endTurn();
				return true;
			case R.id.resign:
				resign();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	private void endTurn()
	{
		if(thread.getPlayerMessage().getBattle().getTurn().equals(thread.getPlayerMessage().getUsername()))
		{
			if(thread.getPlayerMessage().getBattle().isGameOver() == false)
			{
				EndTurnMessage endTurnMsg = new EndTurnMessage(thread.getPlayerMessage());
				EndTurnTask task = new EndTurnTask();
				
				task.execute(endTurnMsg);
				
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
				
				if(result instanceof SuccessfulUpdateMessage)
				{
					SuccessfulUpdateMessage castedMsg = (SuccessfulUpdateMessage)result;
					myPlayerMessage = castedMsg.getPlayerMessage();
					view.setPlayerMessage(myPlayerMessage);
					thread.setPlayerMessage(myPlayerMessage);
					Toast.makeText(this.getBaseContext(), "Successfully ended turn", Toast.LENGTH_SHORT).show();
				}
				else
				{
					Toast.makeText(this.getBaseContext(), "Error ending turn", Toast.LENGTH_SHORT).show();
				}
			}
			
			else
				Toast.makeText(this.getBaseContext(), "The battle is over", Toast.LENGTH_SHORT).show();
		}
		else
			Toast.makeText(this.getBaseContext(), "It's not your turn", Toast.LENGTH_SHORT).show();
	}
	
	public void resign()
	{
		if(thread.getPlayerMessage().getBattle().isGameOver() == false)
		{
			ResignMessage msg = new ResignMessage(thread.getPlayerMessage());
			EndTurnTask task = new EndTurnTask();
				
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
				
			if(result instanceof SuccessfulUpdateMessage)
			{
				SuccessfulUpdateMessage castedMsg = (SuccessfulUpdateMessage)result;
				myPlayerMessage = castedMsg.getPlayerMessage();
				view.setPlayerMessage(myPlayerMessage);
				thread.setPlayerMessage(myPlayerMessage);
				Toast.makeText(this.getBaseContext(), "You have resigned", Toast.LENGTH_SHORT).show();
			}
			else
			{
				Toast.makeText(this.getBaseContext(), "Error resigning", Toast.LENGTH_SHORT).show();
			}
		}
			
		else
			Toast.makeText(this.getBaseContext(), "The battle is over", Toast.LENGTH_SHORT).show();
	}

	protected void onPause()
	{
		super.onPause();
		thread.pauseThread();
	}
	
	protected void onResume()
	{
		super.onResume();
		thread.resumeThread();
	}
	
	public void setPlayerMessage(PlayerMessage msg)
	{
		myPlayerMessage = msg;
	}
	
	public void onBackPressed()
	{
		Intent returnIntent = new Intent();
		returnIntent.putExtra("player_info_modified", myPlayerMessage);
		setResult(RESULT_OK, returnIntent);
		finish();
		return;
	}
	
	private class EndTurnTask extends AsyncTask<Message, Void, Message>
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
