package com.jonivey.seniorproj;

import com.jonivey.messages.PlayerMessage;

import android.content.Context;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GameSurfaceView extends SurfaceView implements SurfaceHolder.Callback
{
	private ViewBattleActivity myActivity;
	private GameThread thread;
	private SurfaceHolder holder;
	private float downX, downY, upX, upY, offsetX, offsetY;
	private boolean running;
	private PlayerMessage myPlayerMessage;
	
	public GameSurfaceView(Context context)
	{
		super(context);
	}
	
	public GameSurfaceView(Context context, PlayerMessage msg, ViewBattleActivity act)
	{
		super(context);
		
		holder = getHolder();
		holder.addCallback(this);
		myPlayerMessage = msg;
		myActivity = act;
		
		try
		{
			thread = new GameThread(holder, context, myPlayerMessage, myActivity, this);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		if(myPlayerMessage.getBattle().getPlayerOne().getName().equals(myPlayerMessage.getBattle().getTurn()))
		{
			offsetX = 0;
			offsetY = 0;
		}
		else
		{
			offsetX = (myPlayerMessage.getBattle().getMap().getNumCols() * GameThread.CELL_SIZE * thread.scale) - thread.phoneWidth + 84;
			offsetY = (myPlayerMessage.getBattle().getMap().getNumRows() * GameThread.CELL_SIZE * thread.scale) - thread.phoneHeight + 132;
			thread.setOffsetX(offsetX);
			thread.setOffsetY(offsetY);
		}
		
		setFocusable(true);
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
	{
		thread.setCanvasDimensions(width, height);
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder)
	{
		startThread();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder)
	{
		boolean retry = true;
        thread.setRunning(false);
        while (retry)
        {
            try
            {
                thread.join();
                retry = false;
            } catch (InterruptedException e)
            {
            	e.printStackTrace();
            }
        }
	}
	
	public void startThread()
	{
		if(running == false)
		{
			thread.setRunning(true);
			thread.start();
			running = true;
		}
		
		else
		{
			thread.resumeThread();
		}
	}

	public GameThread getThread()
	{
		return thread;
	}
	
	public boolean onTouchEvent(MotionEvent e)
	{
		if(e.getAction() == MotionEvent.ACTION_MOVE)
		{
			upX = e.getX();
			upY = e.getY();
			offsetX += (downX - upX);
			offsetY += (downY - upY);
			
			thread.setOffsetX(offsetX);
			thread.setOffsetY(offsetY);
			downX = e.getX();
			downY = e.getY();
		}
		else if(e.getAction() == MotionEvent.ACTION_DOWN)
		{
			downX = e.getX();
			downY = e.getY();
			thread.testPress(downX, downY);
		}
		
		return true;
	}

	public void setPlayerMessage(PlayerMessage msg)
	{
		myPlayerMessage = msg;
	}
}
