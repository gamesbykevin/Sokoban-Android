package com.gamesbykevin.sokoban.game.controller;

import com.gamesbykevin.androidframework.awt.Button;

import android.graphics.Canvas;
import android.view.MotionEvent;

import com.gamesbykevin.androidframework.resources.Audio;
import com.gamesbykevin.androidframework.resources.Images;

import com.gamesbykevin.sokoban.assets.Assets;
import com.gamesbykevin.sokoban.game.Game;
import com.gamesbykevin.sokoban.panel.GamePanel;
import com.gamesbykevin.sokoban.screen.ScreenManager;

import java.util.HashMap;

import java.util.ArrayList;
import java.util.List;

/**
 * This class will be our game controller
 * @author GOD
 */
public class Controller implements IController
{
    //all of the buttons for the player to control
    private HashMap<Assets.ImageGameKey, Button> buttons;
    
    //our game object reference
    private final Game game;
    
    //location of reset button
    private final static int RESET_X = GamePanel.WIDTH - 64;
    private final static int RESET_Y = 0;
    
    /**
     * Default Constructor
     * @param game Object game object reference
     */
    public Controller(final Game game)
    {
        //assign object reference
        this.game = game;
        
        //create temp list
        List<Assets.ImageGameKey> tmp = new ArrayList<Assets.ImageGameKey>();
        tmp.add(Assets.ImageGameKey.SoundOff);
        tmp.add(Assets.ImageGameKey.SoundOn);
        tmp.add(Assets.ImageGameKey.Pause);
        tmp.add(Assets.ImageGameKey.Exit);
        tmp.add(Assets.ImageGameKey.Reset);
        
        //create new list of buttons
        this.buttons = new HashMap<Assets.ImageGameKey, Button>();
        
        //add button controls
        for (Assets.ImageGameKey key : tmp)
        {
            this.buttons.put(key, new Button(Images.getImage(key)));
        }
        
        //reset button
        this.buttons.get(Assets.ImageGameKey.Reset).setX(RESET_X);
        this.buttons.get(Assets.ImageGameKey.Reset).setY(RESET_Y);
        
        int x = 40;
        final int y = 710;
        final int incrementX = 160;
        
        this.buttons.get(Assets.ImageGameKey.SoundOff).setX(x);
        this.buttons.get(Assets.ImageGameKey.SoundOff).setY(y);
        this.buttons.get(Assets.ImageGameKey.SoundOn).setX(x);
        this.buttons.get(Assets.ImageGameKey.SoundOn).setY(y);
        
        x += incrementX;
        this.buttons.get(Assets.ImageGameKey.Pause).setX(x);
        this.buttons.get(Assets.ImageGameKey.Pause).setY(y);
        
        x += incrementX;
        this.buttons.get(Assets.ImageGameKey.Exit).setX(x);
        this.buttons.get(Assets.ImageGameKey.Exit).setY(y);
        
        //assign boundary
        for (Assets.ImageGameKey key : tmp)
        {
            this.buttons.get(key).updateBounds();
        }
    }
    
    /**
     * Get our game object reference
     * @return Our game object reference
     */
    private Game getGame()
    {
        return this.game;
    }
    
    /**
     * Update the controller based on the motion event
     * @param event Motion Event
     * @param x (x-coordinate)
     * @param y (y-coordinate)
     * @return true if motion event was applied, false otherwise
     * @throws Exception
     */
    public boolean update(final MotionEvent event, final float x, final float y) throws Exception
    {
        //check if the touch screen was released
        if (event.getAction() == MotionEvent.ACTION_UP)
        {
            //check if the player hit the controller
            if (buttons.get(Assets.ImageGameKey.Pause).contains(x, y))
            {
                //change the state to paused
                getGame().getScreen().setState(ScreenManager.State.Paused);
                
                //event was applied
                return true;
            }
            else if (buttons.get(Assets.ImageGameKey.Exit).contains(x, y))
            {
                //change to the exit confirm screen
                getGame().getScreen().setState(ScreenManager.State.Exit);
                
                //event was applied
                return true;
            }
            else if (buttons.get(Assets.ImageGameKey.SoundOn).contains(x, y))
            {
                //flip the audio setting
                Audio.setAudioEnabled(!Audio.isAudioEnabled());

                //make sure the correct button is showing
                if (Audio.isAudioEnabled())
                {
                    //play song
                    Audio.play(Assets.AudioGameKey.Music, true);
                }
                else
                {
                    //if audio is not enabled, stop all sound
                    Audio.stop();
                }
                
                //event was applied
                return true;
            }
            else if  (buttons.get(Assets.ImageGameKey.Reset).contains(x, y))
            {
            	//reset current level
            	getGame().flagLevelReset();
                
                //event was applied
                return true;
            }
        }
        
        //no event was applied
        return false;
    }
    
    @Override
    public void reset()
    {
    	if (buttons != null)
    	{
	        //determine which button is displayed
	        buttons.get(Assets.ImageGameKey.SoundOn).setVisible(Audio.isAudioEnabled());
	        buttons.get(Assets.ImageGameKey.SoundOff).setVisible(!Audio.isAudioEnabled());
    	}
    }
    
    /**
     * Recycle objects
     */
    @Override
    public void dispose()
    {
        if (buttons != null)
        {
            for (Button button : buttons.values())
            {
                if (button != null)
                {
                    button.dispose();
                    button = null;
                }
            }
            
            buttons.clear();
            buttons = null;
        }
    }
    
    /**
     * Render the controller
     * @param canvas Write pixel data to this canvas
     * @throws Exception 
     */
    @Override
    public void render(final Canvas canvas) throws Exception
    {
        //draw the buttons
        if (buttons != null)
        {
            buttons.get(Audio.isAudioEnabled() ? Assets.ImageGameKey.SoundOn : Assets.ImageGameKey.SoundOff).render(canvas);
            buttons.get(Assets.ImageGameKey.Pause).render(canvas);
            buttons.get(Assets.ImageGameKey.Exit).render(canvas);
            buttons.get(Assets.ImageGameKey.Reset).render(canvas);
        }
    }
}