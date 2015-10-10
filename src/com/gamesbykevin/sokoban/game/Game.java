package com.gamesbykevin.sokoban.game;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.SystemClock;
import android.view.MotionEvent;

import com.gamesbykevin.androidframework.resources.Audio;
import com.gamesbykevin.androidframework.resources.Disposable;

import com.gamesbykevin.sokoban.assets.Assets;
import com.gamesbykevin.sokoban.game.controller.Controller;
import com.gamesbykevin.sokoban.level.LevelHelper;
import com.gamesbykevin.sokoban.level.Levels;
import com.gamesbykevin.sokoban.level.tile.TileHelper;
import com.gamesbykevin.sokoban.player.Player;
import com.gamesbykevin.sokoban.player.PlayerHelper;
import com.gamesbykevin.sokoban.screen.MainScreen;

/**
 * The main game logic will happen here
 * @author ABRAHAM
 */
public final class Game implements IGame
{
    //our main screen object reference
    private final MainScreen screen;
    
    //the controller for our game
    private Controller controller;
    
    //paint object to draw text
    private Paint paint;
    
    //our level
    private Levels levels;
    
    //our player
    private Player player;
    
    public Game(final MainScreen screen) throws Exception
    {
        //our main screen object reference
        this.screen = screen;
        
        //create new paint object
        this.paint = new Paint();
        this.paint.setTextSize(24f);
        this.paint.setColor(Color.WHITE);
        
        //create new controller
        this.controller = new Controller(this);
    }
    
    /**
     * Get our levels
     * @return Levels Object container
     */
    public Levels getLevels()
    {
        return this.levels;
    }
    
    /**
     * Get the main screen object reference
     * @return The main screen object reference
     */
    public MainScreen getMainScreen()
    {
        return this.screen;
    }
    
    /**
     * Restart game with assigned settings
     * @throws Exception 
     */
    @Override
    public void reset() throws Exception
    {
        //create new objects if not exist
        if (getLevels() == null)
            levels = new Levels(Assets.TextKey.Levels);
        if (getPlayer() == null)
            player = new Player();
        
        //mark as level not selected
        getLevels().setSelected(false);
        
        //default to 1st page
        getLevels().setPage(0);
    }
    
    /**
     * Get our controller object
     * @return Object containing game controls
     */
    public Controller getController()
    {
        return this.controller;
    }
    
    /**
     * Get the player
     * @return The player
     */
    public Player getPlayer()
    {
        return this.player;
    }
    
    /**
     * Update the game based on the motion event
     * @param event Motion Event
     * @param x (x-coordinate)
     * @param y (y-coordinate)
     * @throws Exception
     */
    public void updateMotionEvent(final MotionEvent event, final float x, final float y) throws Exception
    {
        //only update game if no controller buttons were clicked
        if (!getController().updateMotionEvent(event, x, y))
        {
            if (event.getAction() == MotionEvent.ACTION_DOWN)
            {
                //only pursue if the user already chose a level
                if (getLevels().isSelected())
                {
                    //check that we haven't selected the player yet
                    if (!getPlayer().isSelected())
                    {
                        //make sure the player is already at the destination before moving again
                        if (getPlayer().hasTarget())
                        {
                            //get the location
                            final int col = (int)LevelHelper.getCol(getLevels().getLevel(), x);
                            final int row = (int)LevelHelper.getRow(getLevels().getLevel(), y);

                            //if the location matches the player, mark selected
                            if (getPlayer().getCol() == col && getPlayer().getRow() == row)
                                getPlayer().setSelected(true);
                        }
                    }
                }
            }
            else if (event.getAction() == MotionEvent.ACTION_UP)
            {
                //only pursue if the user already chose a level
                if (getLevels().isSelected())
                {
                    //make sure we have selected the player
                    if (getPlayer().isSelected())
                    {
                        //make sure the player is already at the destination before moving again
                        if (getPlayer().hasTarget())
                        {
                            //get the location
                            final int col = (int)LevelHelper.getCol(getLevels().getLevel(), x);
                            final int row = (int)LevelHelper.getRow(getLevels().getLevel(), y);

                            //diagonal movement is not allowed, as well if the destination is same as current location
                            if (getPlayer().getCol() != col && getPlayer().getRow() != row || getPlayer().getCol() == col && getPlayer().getRow() == row)
                            {
                                //flip flag
                                getPlayer().setSelected(!getPlayer().isSelected());
                            }
                            else
                            {
                                //assign the player destination
                                getPlayer().setTarget(col, row);

                                //calculate the targets
                                PlayerHelper.calculateTargets(getPlayer(), getLevels().getLevel());
                            }
                        }
                    }
                }
                else
                {
                    //mark the selection
                    getLevels().setSelected(x, y);
                    
                    //if the selection was made
                    if (getLevels().isSelected())
                    {
                        //reset selected level
                        getLevels().reset();

                        //reset player
                        getPlayer().reset(getLevels().getLevel());
                    }
                }
            }
        }
    }
    
    /**
     * Update game
     * @throws Exception 
     */
    public void update() throws Exception
    {
        //make sure object exists
        if (getLevels() != null)
        {
            //update current level first
            if (getLevels().getLevel() != null)
            {
                if (LevelHelper.hasCompleted(getLevels().getLevel()))
                {
                    //set game over state
                    screen.setState(MainScreen.State.GameOver);

                    //set display message
                    screen.getScreenGameover().setMessage("Level Complete");

                    //play sound
                    //Audio.play(Assets.AudioKey.GameoverWin);
                }
                else
                {
                    getLevels().getLevel().update();
                }
            }
            
            //if player exists update the player
            if (getPlayer() != null)
                getPlayer().update(getLevels().getLevel());
        }
    }
    
    @Override
    public void dispose()
    {
        if (controller != null)
        {
            controller.dispose();
            controller = null;
        }
        
        if (paint != null)
            paint = null;
        
        if (levels != null)
        {
            levels.dispose();
            levels = null;
        }
        
        if (player != null)
        {
            player.dispose();
            player = null;
        }
    }
    
    /**
     * Render game elements
     * @param canvas Where to write the pixel data
     * @throws Exception 
     */
    public void render(final Canvas canvas) throws Exception
    {
        if (getLevels() != null)
        {
            //render level/seletions
            getLevels().render(canvas, screen.getPaint());
            
            if (getLevels().isSelected() && getPlayer() != null && getController() != null)
            {
                //render player
                getPlayer().render(canvas);
                
                //draw the game controller
                getController().render(canvas);
            }
        }
    }
}