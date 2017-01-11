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
    
    //location of undo button
    private final static int UNDO_X = GamePanel.WIDTH - 64;
    private final static int UNDO_Y = 96;
    
    //is the undo button enabled
    private boolean enabled = false;
    
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
        tmp.add(Assets.ImageGameKey.UndoEnabled);
        tmp.add(Assets.ImageGameKey.UndoDisabled);
        
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
        
        //undo button
        this.buttons.get(Assets.ImageGameKey.UndoEnabled).setX(UNDO_X);
        this.buttons.get(Assets.ImageGameKey.UndoEnabled).setY(UNDO_Y);
        this.buttons.get(Assets.ImageGameKey.UndoDisabled).setX(UNDO_X);
        this.buttons.get(Assets.ImageGameKey.UndoDisabled).setY(UNDO_Y);
        
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
    public boolean update(final int action, final float x, final float y) throws Exception
    {
    	//check if there was a change
    	boolean change = false;
    	
    	//track the motion events
    	if (action == MotionEvent.ACTION_UP)
    	{
    		//check each button in our list
    		for (Button button : buttons.values())
    		{
    			if (button != null && button.isVisible() && button.contains(x, y))
    			{
    				if (button.isPressed())
    				{
	    				//if contained within the coordinates flag released true
						button.setReleased(true);
						
						//flag change true
						change = true;
    				}
    				else
    				{
    					//if this button wasn't pressed previous, reset
    					reset();
    				}
    			}
    			else
    			{
    				//else flag released false
					button.setReleased(false);
    			}
    		}
    	}
    	else if (action == MotionEvent.ACTION_DOWN)
    	{
    		//check each button in our list
    		for (Button button : buttons.values())
    		{
    			if (button != null && button.isVisible() && button.contains(x, y))
    			{
    				//if contained within the coordinates flag pressed true
					button.setPressed(true);
					
					//flag change true
					change = true;
    			}
    			else
    			{
    				//else flag pressed false
    				button.setPressed(false);
    			}
    		}
    	}
    	
    	//return if any change was made
    	return change;
    }
    
    @Override
    public void reset()
    {
    	if (buttons != null)
    	{
	        //determine which button is displayed
	        buttons.get(Assets.ImageGameKey.SoundOn).setVisible(Audio.isAudioEnabled());
	        buttons.get(Assets.ImageGameKey.SoundOff).setVisible(!Audio.isAudioEnabled());
	        
	        //reset all buttons
	        for (Button button : buttons.values())
	        {
	        	if (button != null)
	        	{
	        		button.setPressed(false);
	        		button.setReleased(false);
	        	}
	        }
    	}
    	
    	//disable the undo button
    	setDisabled();
    }
    
    /**
     * Determined what button was pressed
     */
    public void update() throws Exception
    {
    	//we can't continue if the list is null
    	if (buttons == null)
    		return;
    	
    	//check each button to see what changes
    	for (Assets.ImageGameKey key : buttons.keySet())
    	{
    		//get the current button
    		Button button = buttons.get(key);
    		
    		//if this button has been pressed and released
    		if (button.isPressed() && button.isReleased())
    		{
    			//determine next steps
    			switch (key)
    			{
	    			case Pause:
	                    //change the state to paused
	                    getGame().getScreen().setState(ScreenManager.State.Paused);
	    				break;
	    				
	    			case Exit:
	                    //change to the exit confirm screen
	                    getGame().getScreen().setState(ScreenManager.State.Exit);
	    				break;
	    				
	    			case SoundOn:
	    			case SoundOff:
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
	    				break;
	    				
	    			case Reset:
	                	//reset current level
	                	getGame().flagLevelReset();
	    				break;
    			
	    			case UndoEnabled:
	    			case UndoDisabled:
	    				
	    				//make sure the button is enabled
	    				if (this.enabled)
	    				{
	    					//disable button
	    					setDisabled();
	    					
	    					//undo the previous move
	    					getGame().undo();
	    				}
	    				break;
	    				
	    			default:
	    				throw new Exception("Key is not handled here: " + key.toString());
    			}
    			
    			//reset all buttons
    			reset();
    		}
    	}
    }
    
    /**
     * Make the undo button enabled
     */
    public void setEnabled()
    {
    	this.enabled = true;
    }
    
    /**
     * Make the undo button disabled
     */
    public void setDisabled()
    {
    	this.enabled = false;
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
            buttons.get((enabled) ? Assets.ImageGameKey.UndoEnabled : Assets.ImageGameKey.UndoDisabled).render(canvas);
        }
    }
}