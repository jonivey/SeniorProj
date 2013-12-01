package com.jonivey.seniorproj;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.jonivey.messages.AccountCreatedMessage;
import com.jonivey.messages.CreateAccountMessage;
import com.jonivey.messages.Message;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class CreateAccountScreen extends Activity
{
	private Context context;
	
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_account);

		context = this.getBaseContext();
		
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
		{
			// Show the Up button in the action bar.
			setupActionBar();
		}
	}

	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	private void setupActionBar()
	{
		getActionBar().setDisplayHomeAsUpEnabled(true);
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
	
	public void createAccount(View view)
	{
		EditText usernameText = (EditText) findViewById(R.id.create_username_message);
		EditText passwordText = (EditText) findViewById(R.id.create_password_message);
		EditText confirmPasswordText = (EditText) findViewById(R.id.confirm_password_message);
		String username = usernameText.getText().toString();
		String password = passwordText.getText().toString();
		String confirmPassword = confirmPasswordText.getText().toString();
		
		if(password.equals(confirmPassword))
		{
			CreateAccountMessage createMsg = new CreateAccountMessage(username, password);
			CreateUserTask task = new CreateUserTask();
			task.execute(createMsg);
			
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
			
			if(result instanceof AccountCreatedMessage)
			{
				Toast.makeText(this.getBaseContext(), "Account created", Toast.LENGTH_SHORT).show();
			}
			else
			{
				Toast.makeText(this.getBaseContext(), "Failed to create account", Toast.LENGTH_SHORT).show();
			}
		}
		
		else
		{
			Toast.makeText(context, "Passwords do not match", Toast.LENGTH_SHORT).show();
		}
	}
	
	private class CreateUserTask extends AsyncTask<Message, Void, Message>
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
				
				if(receivedMsg instanceof AccountCreatedMessage)
				{
					returnedMsg = (AccountCreatedMessage)receivedMsg;
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
