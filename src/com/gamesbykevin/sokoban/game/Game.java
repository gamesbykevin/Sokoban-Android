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
        if (levels == null)
            levels = new Levels();
        
        if (player == null)
            player = new Player();
        
        //reset to first level
        levels.reset();
        
        //assign the start location of the player
        player.setCol(levels.getLevel().getStart());
        player.setRow(levels.getLevel().getStart());
        
        //set the target as the start
        player.setTarget(player.getCol(), player.getRow());
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
     * Update the game based on the motion event
     * @param event Motion Event
     * @param x (x-coordinate)
     * @param y (y-coordinate)
     */
    public void updateMotionEvent(final MotionEvent event, final float x, final float y)
    {
        //only update game if no controller buttons were clicked
        if (!getController().updateMotionEvent(event, x, y))
        {
            //if the board exists and the action is up
            if (event.getAction() == MotionEvent.ACTION_UP)
            {
                //set game over state
                screen.setState(MainScreen.State.GameOver);

                //set display message
                screen.getScreenGameover().setMessage("Game Over, You win");
                    
                //play sound
                //Audio.play(Assets.AudioKey.GameoverWin);
            }
        }
    }
    
    /**
     * Update game
     * @throws Exception 
     */
    public void update() throws Exception
    {
        
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
    }
    
    /**
     * Render game elements
     * @param canvas Where to write the pixel data
     * @throws Exception 
     */
    public void render(final Canvas canvas) throws Exception
    {
        if (levels != null)
            levels.render(canvas);
        
        if (player != null)
        {
            //get start destination
            final double x = LevelHelper.getX(levels.getLevel(), player.getCol());
            final double y = LevelHelper.getY(levels.getLevel(), player.getRow());
            
            //place in the center
            player.setX(x + (TileHelper.DEFAULT_DIMENSION / 2) - (player.getWidth() / 2));
            player.setY(y + (TileHelper.DEFAULT_DIMENSION / 2) - (player.getHeight() / 2));
            
            //render player
            player.render(canvas);
        }
        
        //draw the game controller
        if (getController() != null)
            getController().render(canvas);
    }
}