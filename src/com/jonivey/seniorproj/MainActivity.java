package com.jonivey.seniorproj;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.jonivey.messages.LoginMessage;
import com.jonivey.messages.Message;
import com.jonivey.messages.PlayerMessage;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity
{
	public final static String EXTRA_MESSAGE = "com.jonivey.seniorproj.MESSAGE";
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}
	
	public void login(View view)
	{	
		EditText usernameText = (EditText) findViewById(R.id.username_message);
		EditText passwordText = (EditText) findViewById(R.id.password_message);
		String username = usernameText.getText().toString();
		String password = passwordText.getText().toString();
		LoginMessage loginMsg = new LoginMessage(username, password);
		RetreiveUserTask task = new RetreiveUserTask();
		task.execute(loginMsg);
		
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
		
		if(result instanceof PlayerMessage)
		{
			PlayerMessage castedMsg = (PlayerMessage)result;
			Intent intent = new Intent(this, MenuActivity.class);
			intent.putExtra("player_info", castedMsg);
			startActivity(intent);
		}
		else
		{
			Toast.makeText(this.getBaseContext(), "Failed to log in", Toast.LENGTH_SHORT).show();
		}
	}
	
	public void createAccount(View view)
	{
		Intent intent = new Intent(this, CreateAccountScreen.class);
		startActivity(intent);
	}
	
	public void about(View view)
	{
		Intent intent = new Intent(this, AboutActivity.class);
		startActivity(intent);
	}
	
	private class RetreiveUserTask extends AsyncTask<Message, Void, Message>
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
				
				if(receivedMsg instanceof PlayerMessage)
				{
					returnedMsg = (PlayerMessage)receivedMsg;
				}
				
				else
				{
					returnedMsg = null;
				}
				
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
