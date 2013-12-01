package com.jonivey.seniorproj;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.jonivey.messages.Message;
import com.jonivey.messages.PlayerMessage;
import com.jonivey.messages.SuccessfulUpdateLevelsMessage;
import com.jonivey.messages.UpdateLevelsMessage;

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

public class ViewLevelsActivity extends Activity
{
	private Context context;
	private PlayerMessage myPlayerMessage, copy;
	private int expPerLevel = 1000;
	private int origStr, origMob, origVit, origExp;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_levels);
		
		ActionBar actionBar = getActionBar();
	    actionBar.setDisplayHomeAsUpEnabled(false);
		
		Intent intent = getIntent();
		myPlayerMessage = (PlayerMessage) intent.getSerializableExtra("player_info");
		copy = new PlayerMessage(myPlayerMessage.getUsername(), myPlayerMessage.getPassword(), myPlayerMessage.getExperience(), myPlayerMessage.getVitality(), myPlayerMessage.getStrength(), myPlayerMessage.getMobility(), myPlayerMessage.getChallenger(), myPlayerMessage.getBattle());
		
		origStr = myPlayerMessage.getStrength();
		origMob = myPlayerMessage.getMobility();
		origVit = myPlayerMessage.getVitality();
		origExp = myPlayerMessage.getExperience();
		
		TextView strengthLabel = (TextView) findViewById(R.id.strength_level);
		TextView mobilityLabel = (TextView) findViewById(R.id.mobility_level);
		TextView vitalityLabel = (TextView) findViewById(R.id.vitality_level);
		TextView experienceLabel = (TextView) findViewById(R.id.experience_count);
		TextView expPerLevelLabel = (TextView) findViewById(R.id.experience_per_level_count);

		strengthLabel.setText("" + origStr);
		mobilityLabel.setText("" + origMob);
		vitalityLabel.setText("" + origVit);
		experienceLabel.setText("" + origExp);
		expPerLevelLabel.setText("" + expPerLevel);

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
	
	public void incrementStrength(View view)
	{
		if(copy.getExperience() < expPerLevel)
			Toast.makeText(context, "Not enough exp", Toast.LENGTH_SHORT).show();
		else
		{
			copy.incrementStrength();
			copy.removeExperience(expPerLevel);
			
			TextView strengthLabel = (TextView) findViewById(R.id.strength_level);
			strengthLabel.setText("" + copy.getStrength());
			
			TextView experienceLabel = (TextView) findViewById(R.id.experience_count);
			experienceLabel.setText("" + copy.getExperience());
		}
	}
	
	public void incrementMobility(View view)
	{
		if(copy.getExperience() < expPerLevel)
			Toast.makeText(context, "Not enough exp", Toast.LENGTH_SHORT).show();
		else
		{
			copy.incrementMobility();
			copy.removeExperience(expPerLevel);
			
			TextView mobilityLevel = (TextView) findViewById(R.id.mobility_level);
			mobilityLevel.setText("" + copy.getMobility());
			
			TextView experienceLabel = (TextView) findViewById(R.id.experience_count);
			experienceLabel.setText("" + copy.getExperience());
		}
	}
	
	public void incrementVitality(View view)
	{
		if(copy.getExperience() < expPerLevel)
			Toast.makeText(context, "Not enough exp", Toast.LENGTH_SHORT).show();
		else
		{
			copy.incrementVitality();
			copy.removeExperience(expPerLevel);
			
			TextView vitalityLevel = (TextView) findViewById(R.id.vitality_level);
			vitalityLevel.setText("" + copy.getVitality());
			
			TextView experienceLabel = (TextView) findViewById(R.id.experience_count);
			experienceLabel.setText("" + copy.getExperience());
		}
	}
	
	public void submitChanges(View view)
	{
		if(copy.getStrength() == origStr && copy.getMobility() == origMob && copy.getVitality() == origVit)
			Toast.makeText(context, "No changes were made", Toast.LENGTH_SHORT).show();
		else
		{
			UpdateLevelsMessage msg = new UpdateLevelsMessage(copy);
			UpdateLevelsTask task = new UpdateLevelsTask();
			
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
			
			if(result instanceof SuccessfulUpdateLevelsMessage)
			{
				SuccessfulUpdateLevelsMessage castedMsg = (SuccessfulUpdateLevelsMessage)result;
				Toast.makeText(this.getBaseContext(), "Successfully submitted changes", Toast.LENGTH_SHORT).show();
				myPlayerMessage = castedMsg.getPlayerMessage();
			}
			else
			{
				Toast.makeText(this.getBaseContext(), "Error submitting changes", Toast.LENGTH_SHORT).show();
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
	
	private class UpdateLevelsTask extends AsyncTask<Message, Void, Message>
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
