package com.jonivey.seniorproj;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.jonivey.buildings.Building;
import com.jonivey.gameplay.Cell;
import com.jonivey.gameplay.Player;
import com.jonivey.messages.Message;
import com.jonivey.messages.PlayerMessage;
import com.jonivey.messages.SubmitGameoverMessage;
import com.jonivey.messages.SuccessfulSubmitGameoverMessage;
import com.jonivey.terrain.Terrain;
import com.jonivey.units.*;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.WindowManager;
import android.widget.Toast;

public class GameThread extends Thread
{
	private ViewBattleActivity myActivity;
	private GameSurfaceView myView;
	private boolean running;
	private SurfaceHolder holder;
	private Context context;
	private Bitmap forest, mountain, water, grass, 
		noFactory, noPort, noAirport,
		p1Capital, p1Factory, p1Port, p1Airport, p1Soldier, p1Tank, p1Helicopter, p1Jet, p1Cruiser, p1Battleship,
		p2Capital, p2Factory, p2Port, p2Airport, p2Soldier, p2Tank, p2Helicopter, p2Jet, p2Cruiser, p2Battleship,
		select, victory, defeat, okButton;
	protected int canvasWidth, canvasHeight, phoneWidth, phoneHeight;
	private float offsetX, offsetY;
	private int myTeam;
	private Rect buildUnitRect;
	private Button soldierButton, tankButton, helicopterButton, jetButton, cruiserButton, battleshipButton, gameOverButton;
	protected static final int CELL_SIZE = 64;
	private int numRows, numCols;
	private PlayerMessage myPlayerMessage;
	private Player me;
	protected float scale = 1.5f;
	
	private Object monitor;
	
	public GameThread(SurfaceHolder holder, Context context, PlayerMessage msg, ViewBattleActivity act, GameSurfaceView view)
	{
		running = false;
		this.holder = holder;
		this.context = context;
		myPlayerMessage = msg;
		myActivity = act;
		myView = view;
		
		 Resources res = context.getResources();
		
		 grass = BitmapFactory.decodeResource(res, R.drawable.grass);
		 forest = BitmapFactory.decodeResource(res, R.drawable.forest);
		 mountain = BitmapFactory.decodeResource(res, R.drawable.mountain);
		 water = BitmapFactory.decodeResource(res, R.drawable.water);
		 
		 noFactory = BitmapFactory.decodeResource(res, R.drawable.factory_unowned);
		 noPort = BitmapFactory.decodeResource(res, R.drawable.port_unowned);
		 noAirport = BitmapFactory.decodeResource(res, R.drawable.airport_unowned);
		 
		 p1Capital = BitmapFactory.decodeResource(res, R.drawable.capital_blue);
		 p1Factory = BitmapFactory.decodeResource(res, R.drawable.factory_blue);
		 p1Port = BitmapFactory.decodeResource(res, R.drawable.port_blue);
		 p1Airport = BitmapFactory.decodeResource(res, R.drawable.airport_blue);
		 
		 p1Soldier = BitmapFactory.decodeResource(res, R.drawable.soldier_blue);
		 p1Tank = BitmapFactory.decodeResource(res, R.drawable.tank_blue);
		 p1Helicopter = BitmapFactory.decodeResource(res, R.drawable.helicopter_blue);
		 p1Jet = BitmapFactory.decodeResource(res, R.drawable.jet_blue);
		 p1Cruiser = BitmapFactory.decodeResource(res, R.drawable.cruiser_blue);
		 p1Battleship = BitmapFactory.decodeResource(res, R.drawable.battleship_blue);
		 
		 p2Capital = BitmapFactory.decodeResource(res, R.drawable.capital_red);
		 p2Factory = BitmapFactory.decodeResource(res, R.drawable.factory_red);
		 p2Port = BitmapFactory.decodeResource(res, R.drawable.port_red);
		 p2Airport = BitmapFactory.decodeResource(res, R.drawable.airport_red);
		 
		 p2Soldier = BitmapFactory.decodeResource(res, R.drawable.soldier_red);
		 p2Tank = BitmapFactory.decodeResource(res, R.drawable.tank_red);
		 p2Helicopter = BitmapFactory.decodeResource(res, R.drawable.helicopter_red);
		 p2Jet = BitmapFactory.decodeResource(res, R.drawable.jet_red);
		 p2Cruiser = BitmapFactory.decodeResource(res, R.drawable.cruiser_red);
		 p2Battleship = BitmapFactory.decodeResource(res, R.drawable.battleship_red);
		 
		 select = BitmapFactory.decodeResource(res, R.drawable.select);
		 victory = BitmapFactory.decodeResource(res, R.drawable.win);
		 defeat = BitmapFactory.decodeResource(res, R.drawable.lose);
		 okButton = BitmapFactory.decodeResource(res, R.drawable.ok_button);
		 
		 canvasWidth = 1;
		 canvasHeight = 1;
		 offsetX = 0;
		 offsetY = 0;
		 
		 WindowManager windowManager = (WindowManager) this.context.getSystemService(Context.WINDOW_SERVICE);
		 Display display = windowManager.getDefaultDisplay();
		 Point dimensions = new Point();
		 display.getRealSize(dimensions);
		 phoneWidth = dimensions.x;
		 phoneHeight = dimensions.y;
		 
		 if(myPlayerMessage.getUsername().equals(myPlayerMessage.getBattle().getPlayerOne().getName()))
		 {
			 myTeam = 1;
			 me = myPlayerMessage.getBattle().getPlayerOne();
		 }
		 else
		 {
			 myTeam = 2;
			 me = myPlayerMessage.getBattle().getPlayerTwo();
		 }
		 
		 numRows = myPlayerMessage.getBattle().getMap().getNumRows();
		 numCols = myPlayerMessage.getBattle().getMap().getNumCols();
		 
		 buildUnitRect = new Rect(phoneWidth - 494, phoneHeight - 442, phoneWidth - 84, phoneHeight);
		 
		 soldierButton = new Button(new Rect(phoneWidth - 489, phoneHeight - 342, phoneWidth - 89, phoneHeight - 242), "Soldier\nCost: "+Soldier.getCost());
		 tankButton = new Button(new Rect(phoneWidth - 489, phoneHeight - 237, phoneWidth - 89, phoneHeight - 137), "Tank\nCost: "+Tank.getCost());
		 helicopterButton = new Button(new Rect(phoneWidth - 489, phoneHeight - 342, phoneWidth - 89, phoneHeight - 242), "Helicopter\nCost: "+Helicopter.getCost());
		 jetButton = new Button(new Rect(phoneWidth - 489, phoneHeight - 237, phoneWidth - 89, phoneHeight - 137), "Jet\nCost: "+Jet.getCost());
		 cruiserButton = new Button(new Rect(phoneWidth - 489, phoneHeight - 342, phoneWidth - 89, phoneHeight - 242), "Cruiser\nCost: "+Cruiser.getCost());
		 battleshipButton = new Button(new Rect(phoneWidth - 489, phoneHeight - 237, phoneWidth - 89, phoneHeight - 137), "Battleship\nCost: "+Battleship.getCost());
		 gameOverButton = new Button(new Rect(((phoneWidth - 84) / 2) - 75, ((phoneHeight - 184) / 2) + 160, ((phoneWidth - 84) / 2) + 75, ((phoneHeight - 184) / 2) + 260), "Ok");
		 
		 monitor = new Object();
	}
	
	@Override
	public void run()
	{
		try
		{
			while(running)
			{
				synchronized (holder)
				{
					render();
				}
				
				try
				{
					Thread.sleep(20);
				}
				catch (InterruptedException e)
				{
					e.printStackTrace();
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	private void render()
	{
		Terrain terr;
		Building build;
		Unit unit;
		Bitmap terrainImage = null, buildingImage = null, unitImage = null;
		Bitmap image = Bitmap.createBitmap((int)(numCols*CELL_SIZE*scale), (int)(numRows*CELL_SIZE*scale), grass.getConfig());
		Canvas c = new Canvas(image);
		c.scale(scale, scale);
		Canvas canvas = null;
		Paint bitmapPaint = new Paint();
		bitmapPaint.setFilterBitmap(true);
		bitmapPaint.setAntiAlias(true);
		bitmapPaint.setDither(true);
		
		try
		{
			canvas = holder.lockCanvas();
			
			if(canvas != null)
			{
				for(int row = 0; row < numRows; row++)
				{
					for(int col = 0; col < numCols; col++)
					{
						terr = myPlayerMessage.getBattle().getMap().getCell(row, col).getTerrain();
						build = myPlayerMessage.getBattle().getMap().getCell(row, col).getBuilding();
						unit = myPlayerMessage.getBattle().getMap().getCell(row, col).getUnit();
						
						terrainImage = null;
						buildingImage = null;
						unitImage = null;
						
						if(terr != null)
						{
							if(terr.getType() == Terrain.TerrainType.GRASS)
								terrainImage = grass;
							else if(terr.getType() == Terrain.TerrainType.FOREST)
								terrainImage = forest;
							else if(terr.getType() == Terrain.TerrainType.MOUNTAIN)
								terrainImage = mountain;
							else if(terr.getType() == Terrain.TerrainType.WATER)
								terrainImage = water;
						}
						
						if(build != null)
						{
							if(build.getOwner() == 1)
							{
								if(build.getType() == Building.BuildingType.CAPITAL)
									buildingImage = p1Capital;
								if(build.getType() == Building.BuildingType.FACTORY)
									buildingImage = p1Factory;
								if(build.getType() == Building.BuildingType.AIRPORT)
									buildingImage = p1Airport;
								if(build.getType() == Building.BuildingType.PORT)
									buildingImage = p1Port;
							}
							
							else if(build.getOwner() == 2)
							{
								if(build.getType() == Building.BuildingType.CAPITAL)
									buildingImage = p2Capital;
								if(build.getType() == Building.BuildingType.FACTORY)
									buildingImage = p2Factory;
								if(build.getType() == Building.BuildingType.AIRPORT)
									buildingImage = p2Airport;
								if(build.getType() == Building.BuildingType.PORT)
									buildingImage = p2Port;
							}
							
							else
							{
								if(build.getType() == Building.BuildingType.FACTORY)
									buildingImage = noFactory;
								if(build.getType() == Building.BuildingType.AIRPORT)
									buildingImage = noAirport;
								if(build.getType() == Building.BuildingType.PORT)
									buildingImage = noPort;
							}
						}
						
						if(unit != null)
						{
							if(unit.getOwner() == 1)
							{
								if(unit instanceof Soldier)
									unitImage = p1Soldier;
								else if(unit instanceof Tank)
									unitImage = p1Tank;
								else if(unit instanceof Helicopter)
									unitImage = p1Helicopter;
								else if(unit instanceof Jet)
									unitImage = p1Jet;
								else if(unit instanceof Cruiser)
									unitImage = p1Cruiser;
								else if(unit instanceof Battleship)
									unitImage = p1Battleship;
							}
							else if(unit.getOwner() == 2)
							{
								if(unit instanceof Soldier)
									unitImage = p2Soldier;
								else if(unit instanceof Tank)
									unitImage = p2Tank;
								else if(unit instanceof Helicopter)
									unitImage = p2Helicopter;
								else if(unit instanceof Jet)
									unitImage = p2Jet;
								else if(unit instanceof Cruiser)
									unitImage = p2Cruiser;
								else if(unit instanceof Battleship)
									unitImage = p2Battleship;
							}
						}
						
						if(terrainImage != null)
							c.drawBitmap(terrainImage, (col * CELL_SIZE), (row * CELL_SIZE), bitmapPaint);
						if(buildingImage != null)
						{
							if(buildingImage == p1Capital || buildingImage == p2Capital)
								c.drawBitmap(buildingImage, (col * CELL_SIZE), ((row-1) * CELL_SIZE), bitmapPaint);
							else
								c.drawBitmap(buildingImage, (col * CELL_SIZE), ((row) * CELL_SIZE), bitmapPaint);
						}
						if(unitImage != null)
						{
							c.drawBitmap(unitImage, (col * CELL_SIZE), (row * CELL_SIZE), bitmapPaint);
							
							Paint unitInfo = new Paint();
							unitInfo.setColor(Color.WHITE);
							c.drawText(""+unit.getCurrentHealth(), (col * CELL_SIZE) + 40, (row * CELL_SIZE) + 60, unitInfo);
							c.drawText(""+unit.getMovementRange(), (col * CELL_SIZE) + 50, (row * CELL_SIZE) + 15, unitInfo);
							c.drawText("" + unit.getAttackRange(), (col * CELL_SIZE) + 5, (row * CELL_SIZE) + 15, unitInfo);
							c.drawText("" + unit.getAttackPower(), (col * CELL_SIZE) + 5, (row * CELL_SIZE) + 60, unitInfo);
						}
					}
				}
				
				for(int row = 0; row < numRows; row++)
				{
					for(int col = 0; col < numCols; col++)
						c.drawLine(col * CELL_SIZE, 0, col * CELL_SIZE, 30 * CELL_SIZE, new Paint());
					c.drawLine(0, row * CELL_SIZE, 30 * CELL_SIZE, row * CELL_SIZE, new Paint());
				}
				
				if(myPlayerMessage.getBattle().getMap().getSelectedCell() != null)
					c.drawBitmap(select, (myPlayerMessage.getBattle().getMap().getSelectedCol() * CELL_SIZE) - 18, (myPlayerMessage.getBattle().getMap().getSelectedRow() * CELL_SIZE) - 18, bitmapPaint);
				
				canvas.drawColor(Color.BLACK);
				canvas.drawBitmap(image, -offsetX, -offsetY, null);
				
				Paint paint = new Paint();
				paint.setTextSize(40);
				paint.setColor(Color.YELLOW);
				
				if(myPlayerMessage.getBattle().getTurn().equals(myPlayerMessage.getBattle().getPlayerOne().getName()))
					canvas.drawRect(0, 0, 250, 96, paint);
				else
					canvas.drawRect(phoneWidth - 334, 0, phoneWidth - 84, 96, paint);
				
				paint.setColor(Color.BLUE);
				canvas.drawRect(0, 0, 240, 86, paint);
				paint.setColor(Color.RED);
				canvas.drawRect(phoneWidth - 324, 0, phoneWidth - 84, 86, paint);
				
				paint.setColor(Color.WHITE);
				canvas.drawText(myPlayerMessage.getBattle().getPlayerOne().getName(), 0, 40, paint);
				canvas.drawText(myPlayerMessage.getBattle().getPlayerTwo().getName(), phoneWidth - 324, 40, paint);
				
				paint.setTextSize(30);
				canvas.drawText("Coins: " + myPlayerMessage.getBattle().getPlayerOne().getCoins(), 0, 80, paint);
				canvas.drawText("Coins: " + myPlayerMessage.getBattle().getPlayerTwo().getCoins(), phoneWidth - 324, 80, paint);
				
				if(myPlayerMessage.getBattle().getMap().getSelectedCell().getBuilding() != null && myPlayerMessage.getBattle().getMap().getSelectedCell().getUnit() == null)
				{
					if(myPlayerMessage.getBattle().getMap().getSelectedCell().getBuilding().getOwner() == myTeam && myPlayerMessage.getBattle().getMap().getSelectedCell().getBuilding().getType() != Building.BuildingType.CAPITAL)
					{
						Paint buildingPaint = new Paint();
						Paint buttonPaint = new Paint();
						Paint textPaint = new Paint();
						buildingPaint.setColor(Color.BLACK);
						buttonPaint.setColor(Color.GRAY);
						textPaint.setColor(Color.BLACK);
						buildUnitRect.set(phoneWidth - 494, phoneHeight - 442, phoneWidth - 84, phoneHeight);
						canvas.drawRect(buildUnitRect, buildingPaint);
						buildingPaint.setColor(Color.WHITE);
						buildingPaint.setTextSize(40);
						textPaint.setTextSize(40);
						canvas.drawText("Build a Unit", phoneWidth - 374, phoneHeight - 402, buildingPaint);
						
						if(myPlayerMessage.getBattle().getMap().getSelectedCell().getBuilding().getType() == Building.BuildingType.FACTORY)
						{
							canvas.drawRect(soldierButton.getRect(), buttonPaint);
							canvas.drawText(soldierButton.getText(), soldierButton.getX() + 5, soldierButton.getY()+10, textPaint);
							canvas.drawRect(tankButton.getRect(), buttonPaint);
							canvas.drawText(tankButton.getText(), tankButton.getX() + 5, tankButton.getY()+10, textPaint);
						}
						
						else if(myPlayerMessage.getBattle().getMap().getSelectedCell().getBuilding().getType() == Building.BuildingType.AIRPORT)
						{
							canvas.drawRect(helicopterButton.getRect(), buttonPaint);
							canvas.drawText(helicopterButton.getText(), helicopterButton.getX() + 5, helicopterButton.getY()+10, textPaint);
							canvas.drawRect(jetButton.getRect(), buttonPaint);
							canvas.drawText(jetButton.getText(), jetButton.getX() + 5, jetButton.getY()+10, textPaint);
						}
						
						else
						{
							canvas.drawRect(cruiserButton.getRect(), buttonPaint);
							canvas.drawText(cruiserButton.getText(), cruiserButton.getX() + 5, cruiserButton.getY()+10, textPaint);
							canvas.drawRect(battleshipButton.getRect(), buttonPaint);
							canvas.drawText(battleshipButton.getText(), battleshipButton.getX() + 5, battleshipButton.getY()+10, textPaint);
						}
					}
				}
				
				if(myPlayerMessage.getBattle().getMap().getSelectedCell().getUnit() != null)
				{
						Paint buildingPaint = new Paint();
						Paint buttonPaint = new Paint();
						Paint textPaint = new Paint();
						buildingPaint.setColor(Color.BLACK);
						buttonPaint.setColor(Color.GRAY);
						textPaint.setColor(Color.BLACK);
						String canMove, canAttack;
						
						if(myPlayerMessage.getBattle().getMap().getSelectedCell().getUnit().hasMoved())
							canMove = "no";
						else
							canMove = "yes";
						
						if(myPlayerMessage.getBattle().getMap().getSelectedCell().getUnit().hasAttacked())
							canAttack = "no";
						else
							canAttack = "yes";
						
						if(myPlayerMessage.getBattle().getMap().getSelectedCol() > (numCols / 2))
							buildUnitRect.set(0, phoneHeight - 442, 410, phoneHeight);
						else
							buildUnitRect.set(phoneWidth - 494, phoneHeight - 442, phoneWidth - 84, phoneHeight);
						
						canvas.drawRect(buildUnitRect, buildingPaint);
						buildingPaint.setColor(Color.WHITE);
						buildingPaint.setTextSize(40);
						canvas.drawText("Movement Range: " + myPlayerMessage.getBattle().getMap().getSelectedCell().getUnit().getMovementRange(), buildUnitRect.left, buildUnitRect.top + 40, buildingPaint);
						canvas.drawText("Attack Range: " + myPlayerMessage.getBattle().getMap().getSelectedCell().getUnit().getAttackRange(), buildUnitRect.left, buildUnitRect.top + 80, buildingPaint);
						canvas.drawText("Attack Damage: " + myPlayerMessage.getBattle().getMap().getSelectedCell().getUnit().getAttackPower(), buildUnitRect.left, buildUnitRect.top + 120, buildingPaint);
						canvas.drawText("Current Health: " + myPlayerMessage.getBattle().getMap().getSelectedCell().getUnit().getCurrentHealth(), buildUnitRect.left, buildUnitRect.top + 160, buildingPaint);
						
						if(myPlayerMessage.getBattle().getMap().getSelectedCell().getUnit().getOwner() == myTeam)
						{
							canvas.drawText("Can Move? " + canMove, buildUnitRect.left, buildUnitRect.top + 200, buildingPaint);
							canvas.drawText("Can Attack? " + canAttack, buildUnitRect.left, buildUnitRect.top + 240, buildingPaint);
						}
				}
				
				if(myPlayerMessage.getBattle().isGameOver())
				{
					if(myPlayerMessage.getBattle().getWinner().equals(myPlayerMessage.getUsername()))
					{
						canvas.drawBitmap(victory, (phoneWidth - 480 - 84) / 2, (phoneHeight - 320 - 184) / 2, null);
					}
					else
					{
						canvas.drawBitmap(defeat, (phoneWidth - 480 - 84) / 2, (phoneHeight - 320 - 184) / 2, null);
					}
					
					Paint gameOverPaint = new Paint();
					gameOverPaint.setColor(Color.RED);
					canvas.drawBitmap(okButton, ((phoneWidth - 84) / 2) - 75, ((phoneHeight - 184) / 2) + 160, null);
					gameOverPaint.setColor(Color.BLACK);
				}
				canvas.restore();
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			if(canvas !=null)
				holder.unlockCanvasAndPost(canvas);
		}
	}

	public void setRunning(boolean bool)
	{
		running = bool;
	}

	public void setCanvasDimensions(int width, int height)
	{
		synchronized (holder)
		{
			canvasWidth = width;
			canvasHeight = height;
		}
	}

	public void setOffsetX(float x)
	{
		if(x <= 0)
			offsetX = 0;
		else if(x >= ((numCols * CELL_SIZE * scale) - phoneWidth) + 84)
			offsetX = (numCols * CELL_SIZE * scale) - phoneWidth + 84;
		else
			offsetX = x;
	}
	
	public void setOffsetY(float y)
	{
		if(y <= 0)
			offsetY = 0;
		else if(y >= ((numRows * CELL_SIZE * scale) - phoneHeight) + 132)
			offsetY = (numRows * CELL_SIZE * scale) - phoneHeight + 132;
		else
			offsetY = y;
	}
	
	public void testPress(float x, float y)
	{
		int row = (int)((y + offsetY) / (CELL_SIZE*scale));
		int col = (int)((x + offsetX) / (CELL_SIZE*scale));
		Cell touchedCell = myPlayerMessage.getBattle().getMap().getCell(row, col);
		
		if(myPlayerMessage.getBattle().getTurn().equals(myPlayerMessage.getUsername()) && !myPlayerMessage.getBattle().isGameOver())
		{
			if(myPlayerMessage.getBattle().getSelectedCell().getUnit() != null)
			{
				if(myPlayerMessage.getBattle().getSelectedCell().getUnit().getOwner() != myTeam)
					myPlayerMessage.getBattle().selectCell(row, col);
				
				else
				{
					if(touchedCell.getUnit() == null)
					{
						if(myPlayerMessage.getBattle().getSelectedCell().getUnit().hasMoved() == false)
						{
							myPlayerMessage.getBattle().moveUnit(row, col);
							myPlayerMessage.getBattle().selectCell(row, col);
						}
						else
							myPlayerMessage.getBattle().selectCell(row, col);
					}
					
					else if(touchedCell.getUnit() != null)
					{
						if(myPlayerMessage.getBattle().getMap().getSelectedRow() == row && myPlayerMessage.getBattle().getMap().getSelectedCol() == col)
						{
							if(touchedCell.getBuilding() != null)
							{
								myPlayerMessage.getBattle().captureBuilding();
							}
						}
						
						if(touchedCell.getUnit().getOwner() == myTeam)
							myPlayerMessage.getBattle().selectCell(row, col);
						
						else
						{
							if(myPlayerMessage.getBattle().getMap().inRange(row, col) && myPlayerMessage.getBattle().getSelectedCell().getUnit().hasAttacked() == false)
							{
								boolean destroyed = myPlayerMessage.getBattle().attackUnit(row, col);
								
								if(destroyed == true)
									myPlayerMessage.addExperience(200);
								
								destroyed = false;
							}
							
							else
								myPlayerMessage.getBattle().selectCell(row, col);
						}
					}
				}
			}
			
			else
			{
				if(myPlayerMessage.getBattle().getSelectedCell().getBuilding() != null)
				{
					if(myPlayerMessage.getBattle().getSelectedCell().getBuilding().getOwner() == myTeam)
					{
						if(myPlayerMessage.getBattle().getSelectedCell().getBuilding().getType() == Building.BuildingType.FACTORY)
						{
							if(soldierButton.getRect().contains((int)x, (int)y))
							{
								if(me.getCoins() >= Soldier.getCost())
									myPlayerMessage.getBattle().buildUnit("Soldier");
								else
									Toast.makeText(context, "Not enough coins", Toast.LENGTH_SHORT).show();
							}
							
							else if(tankButton.getRect().contains((int)x, (int)y))
							{
								if(me.getCoins() >= Tank.getCost())
									myPlayerMessage.getBattle().buildUnit("Tank");
								else
									Toast.makeText(context, "Not enough coins", Toast.LENGTH_SHORT).show();
							}
						}
						
						if(myPlayerMessage.getBattle().getSelectedCell().getBuilding().getType() == Building.BuildingType.AIRPORT)
						{
							if(helicopterButton.getRect().contains((int)x, (int)y))
							{
								if(me.getCoins() >= Helicopter.getCost())
									myPlayerMessage.getBattle().buildUnit("Helicopter");
								else
									Toast.makeText(context, "Not enough coins", Toast.LENGTH_SHORT).show();
							}
							
							else if(jetButton.getRect().contains((int)x, (int)y))
							{
								if(me.getCoins() >= Jet.getCost())
									myPlayerMessage.getBattle().buildUnit("Jet");
								else
									Toast.makeText(context, "Not enough coins", Toast.LENGTH_SHORT).show();
							}
						}
						
						if(myPlayerMessage.getBattle().getSelectedCell().getBuilding().getType() == Building.BuildingType.PORT)
						{
							if(cruiserButton.getRect().contains((int)x, (int)y))
							{
								if(me.getCoins() >= Cruiser.getCost())
									myPlayerMessage.getBattle().buildUnit("Cruiser");
								else
									Toast.makeText(context, "Not enough coins", Toast.LENGTH_SHORT).show();
							}
							
							else if(battleshipButton.getRect().contains((int)x, (int)y))
							{
								if(me.getCoins() >= Battleship.getCost())
									myPlayerMessage.getBattle().buildUnit("Battleship");
								else
									Toast.makeText(context, "Not enough coins", Toast.LENGTH_SHORT).show();
							}
						}	
					}
				}
				myPlayerMessage.getBattle().selectCell(row, col);
			}
		}
		
		else if(myPlayerMessage.getBattle().isGameOver())
		{
			if(gameOverButton.getRect().contains((int)x, (int)y))
			{
				submitGameover();
			}
		}
	}
	
	public void pauseThread()
	{
		synchronized(monitor)
		{
			running = true;
		}
	}
	
	public void resumeThread()
	{
		synchronized(monitor)
		{
			running = false;
			monitor.notifyAll();
		}
	}

	public PlayerMessage getPlayerMessage()
	{
		return myPlayerMessage;
	}

	public void setPlayerMessage(PlayerMessage msg)
	{
		myPlayerMessage = msg;
	}
	
	public void submitGameover()
	{
		SubmitGameoverMessage msg = new SubmitGameoverMessage(myPlayerMessage);
		GameOverTask task = new GameOverTask();
				
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
				
		if(result instanceof SuccessfulSubmitGameoverMessage)
		{
			SuccessfulSubmitGameoverMessage castedMsg = (SuccessfulSubmitGameoverMessage)result;
			myPlayerMessage = castedMsg.getPlayerMessage();
			myView.setPlayerMessage(myPlayerMessage);
			myActivity.setPlayerMessage(myPlayerMessage);
			Toast.makeText(context, "You have ended your battle", Toast.LENGTH_SHORT).show();
		}
		else
		{
			Toast.makeText(context, "Error ending battle", Toast.LENGTH_SHORT).show();
		}
	}
	
	private class GameOverTask extends AsyncTask<Message, Void, Message>
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
