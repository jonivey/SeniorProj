package com.jonivey.seniorproj;

import android.graphics.Rect;

public class Button
{
	private Rect rect;
	private String text;
	private float x, y;
	
	public Button(Rect rect, String text)
	{
		this.rect = rect;
		this.text = text;
		x = this.rect.left;
		y = this.rect.top + ((this.rect.bottom - this.rect.top) / 2);
	}
	
	public Rect getRect()
	{
		return rect;
	}
	
	public String getText()
	{
		return text;
	}
	
	public float getX()
	{
		return x;
	}
	
	public float getY()
	{
		return y;
	}
}
