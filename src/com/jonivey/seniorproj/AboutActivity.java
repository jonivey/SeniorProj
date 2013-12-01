package com.jonivey.seniorproj;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.view.View;

public class AboutActivity extends Activity
{	
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
		
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
	
	public void viewUnitsInfo(View view)
	{
		Intent intent = new Intent(this, UnitsInfoActivity.class);
		startActivity(intent);
	}
	
	public void viewBuildingsInfo(View view)
	{
		Intent intent = new Intent(this, BuildingsInfoActivity.class);
		startActivity(intent);
	}
	
	public void viewGameplayInfo(View view)
	{
		Intent intent = new Intent(this, GameplayInfoActivity.class);
		startActivity(intent);
	}
	
	public void viewLevelsInfo(View view)
	{
		Intent intent = new Intent(this, LevelsInfoActivity.class);
		startActivity(intent);
	}
	
	public void viewInfo(View view)
	{
		Intent intent = new Intent(this, InformationActivity.class);
		startActivity(intent);
	}
}
