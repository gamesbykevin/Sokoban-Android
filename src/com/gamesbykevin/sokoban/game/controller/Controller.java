package com.gamesbykevin.sokoban.game.controller;

import com.gamesbykevin.androidframework.awt.Button;

import android.graphics.Canvas;
import android.view.MotionEvent;

import com.gamesbykevin.androidframework.resources.Audio;
import com.gamesbykevin.androidframework.resources.Images;

import com.gamesbykevin.sokoban.assets.Assets;
import com.gamesbykevin.sokoban.game.Game;
import com.gamesbykevin.sokoban.panel.GamePanel;
import com.gamesbykevin.sokoban.screen.MainScreen;

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
    private HashMap<Assets.ImageKey, Button> buttons;
    
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
        List<Assets.ImageKey> tmp = new ArrayList<Assets.ImageKey>();
        tmp.add(Assets.ImageKey.MenuSoundOff);
        tmp.add(Assets.ImageKey.MenuSoundOn);
        tmp.add(Assets.ImageKey.MenuPause);
        tmp.add(Assets.ImageKey.MenuExit);
        tmp.add(Assets.ImageKey.MenuReset);
        
        //create new list of buttons
        this.buttons = new HashMap<Assets.ImageKey, Button>();
        
        //add button controls
        for (Assets.ImageKey key : tmp)
        {
            this.buttons.put(key, new Button(Images.getImage(key)));
        }
        
        //reset button
        this.buttons.get(Assets.ImageKey.MenuReset).setX(RESET_X);
        this.buttons.get(Assets.ImageKey.MenuReset).setY(RESET_Y);
        
        int x = 100;
        final int y = 710;
        final int incrementX = 100;
        
        this.buttons.get(Assets.ImageKey.MenuSoundOff).setX(x);
        this.buttons.get(Assets.ImageKey.MenuSoundOff).setY(y);
        this.buttons.get(Assets.ImageKey.MenuSoundOn).setX(x);
        this.buttons.get(Assets.ImageKey.MenuSoundOn).setY(y);
        
        x += incrementX;
        this.buttons.get(Assets.ImageKey.MenuPause).setX(x);
        this.buttons.get(Assets.ImageKey.MenuPause).setY(y);
        
        x += incrementX;
        this.buttons.get(Assets.ImageKey.MenuExit).setX(x);
        this.buttons.get(Assets.ImageKey.MenuExit).setY(y);
        
        //assign boundary
        for (Assets.ImageKey key : tmp)
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
     */
    public boolean updateMotionEvent(final MotionEvent event, final float x, final float y) throws Exception
    {
        //check if the touch screen was released
        if (event.getAction() == MotionEvent.ACTION_UP)
        {
            //check if the player hit the controller
            if (buttons.get(Assets.ImageKey.MenuPause).contains(x, y))
            {
                //change the state to paused
                getGame().getMainScreen().setState(MainScreen.State.Paused);
                
                //event was applied
                return true;
            }
            else if (buttons.get(Assets.ImageKey.MenuExit).contains(x, y))
            {
                //change to the exit confirm screen
                getGame().getMainScreen().setState(MainScreen.State.Exit);
                
                //event was applied
                return true;
            }
            else if (buttons.get(Assets.ImageKey.MenuSoundOn).contains(x, y))
            {
                //flip the audio setting
                Audio.setAudioEnabled(!Audio.isAudioEnabled());

                //make sure the correct button is showing
                if (Audio.isAudioEnabled())
                {
                    //play random song
                    //Assets.playSong();
                }
                else
                {
                    //if audio is not enabled, stop all sound
                    Audio.stop();
                }
                
                //event was applied
                return true;
            }
            else if  (buttons.get(Assets.ImageKey.MenuReset).contains(x, y))
            {
                //reset level
                getGame().getLevels().reset();
                
                //reset the player as well
                getGame().getPlayer().reset(getGame().getLevels().getLevel());
            }
        }
        
        //no event was applied
        return false;
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
            buttons.get(Audio.isAudioEnabled() ? Assets.ImageKey.MenuSoundOn : Assets.ImageKey.MenuSoundOff).render(canvas);
            buttons.get(Assets.ImageKey.MenuPause).render(canvas);
            buttons.get(Assets.ImageKey.MenuExit).render(canvas);
            buttons.get(Assets.ImageKey.MenuReset).render(canvas);
        }
    }
}