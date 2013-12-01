package com.jonivey.seniorproj;

import com.jonivey.messages.PlayerMessage;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class MenuActivity extends Activity
{
	private Context context;
	private PlayerMessage myPlayerMessage;
	
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu);
		
		Intent intent = getIntent();
		myPlayerMessage = (PlayerMessage) intent.getSerializableExtra("player_info");

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
	
	public void challengeOpponent(View view)
	{
		Intent intent = new Intent(this, ChallengeActivity.class);
		intent.putExtra("player_info", myPlayerMessage);
		startActivity(intent);
	}
	
	public void viewChallenges(View view)
	{
		Intent intent = new Intent(this, ViewChallengeActivity.class);
		intent.putExtra("player_info", myPlayerMessage);
		startActivityForResult(intent, 1);
	}
	
	public void viewBattle(View view)
	{
		if(myPlayerMessage.getBattle() == null)
			Toast.makeText(context, "You do not have a battle.", Toast.LENGTH_SHORT).show();
		else
		{
			Intent intent = new Intent(this, ViewBattleActivity.class);
			intent.putExtra("player_info", myPlayerMessage);
			startActivityForResult(intent, 2);
		}
	}
	
	public void viewLevels(View view)
	{
		Intent intent = new Intent(this, ViewLevelsActivity.class);
		intent.putExtra("player_info", myPlayerMessage);
		startActivityForResult(intent, 3);
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		myPlayerMessage = (PlayerMessage)data.getSerializableExtra("player_info_modified");
	}
}
