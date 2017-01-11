package com.gamesbykevin.sokoban.screen;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.MotionEvent;

import com.gamesbykevin.androidframework.awt.Button;
import com.gamesbykevin.androidframework.resources.Audio;
import com.gamesbykevin.androidframework.resources.Disposable;
import com.gamesbykevin.androidframework.resources.Images;
import com.gamesbykevin.androidframework.screen.Screen;
import com.gamesbykevin.sokoban.MainActivity;
import com.gamesbykevin.sokoban.screen.ScreenManager;
import com.gamesbykevin.sokoban.screen.MenuScreen;
import com.gamesbykevin.sokoban.storage.settings.Settings;
import com.gamesbykevin.sokoban.assets.Assets;

import java.util.HashMap;

/**
 * This screen will contain the game options
 * @author GOD
 */
public class OptionsScreen implements Screen, Disposable
{
    //our logo reference
    private final Bitmap logo;
    
    //list of buttons
    private HashMap<ButtonKey, Button> buttons;
    
    //our main screen reference
    private final ScreenManager screen;
    
    //our storage settings object
    private Settings settings;
    
    //Different buttons on the screen
    public enum ButtonKey
    {
    	Back, Sound, Vibrate, Difficulty, Instructions, Twitter, Facebook, Youtube
    }
    
    public OptionsScreen(final ScreenManager screen)
    {
        //our logo reference
        this.logo = Images.getImage(Assets.ImageMenuKey.Logo);
        
        //create buttons hash map
        this.buttons = new HashMap<ButtonKey, Button>();

        //store our screen reference
        this.screen = screen;
        
        //start coordinates
        int y = ScreenManager.BUTTON_Y;
        int x = ScreenManager.BUTTON_X;
        
        //add sound option
        addButtonSound(x, y);
        
        //add vibrate option
        y += ScreenManager.BUTTON_Y_INCREMENT;
        addButtonVibrate(x, y);
        
        //add colors option
        y += ScreenManager.BUTTON_Y_INCREMENT;
        addButtonDifficulty(x, y);
        
        //the back button
        y += ScreenManager.BUTTON_Y_INCREMENT;
        addButtonBack(x, y);
        
        //add social media icons after the above, because the dimensions are different
        addIcons();
        
        //set the size and bounds of the buttons
        for (ButtonKey key : ButtonKey.values())
        {
        	//get the current button
        	Button button = buttons.get(key);
        	
        	switch (key)
        	{
	        	case Instructions:
	        	case Facebook:
	        	case Twitter:
	        	case Youtube:
                	button.setWidth(MenuScreen.ICON_DIMENSION);
                	button.setHeight(MenuScreen.ICON_DIMENSION);
                	button.updateBounds();
	        		break;
        		
        		default:
                	button.setWidth(MenuScreen.BUTTON_WIDTH);
                	button.setHeight(MenuScreen.BUTTON_HEIGHT);
                    button.updateBounds();
                    button.positionText(screen.getPaint());
        			break;
        	}
        }
        
        //create our settings object last, which will load the previous settings
        this.settings = new Settings(this, screen.getPanel().getActivity());
    }
    
    /**
     * Add icons, including links to social media
     */
    private void addIcons()
    {
        Button tmp = new Button(Images.getImage(Assets.ImageMenuKey.Instructions));
        tmp.setX(MenuScreen.ICON_X_INSTRUCTIONS);
        tmp.setY(MenuScreen.ICON_Y);
        this.buttons.put(ButtonKey.Instructions, tmp);
        
        tmp = new Button(Images.getImage(Assets.ImageMenuKey.Facebook));
        tmp.setX(MenuScreen.ICON_X_FACEBOOK);
        tmp.setY(MenuScreen.ICON_Y);
        this.buttons.put(ButtonKey.Facebook, tmp);
        
        tmp = new Button(Images.getImage(Assets.ImageMenuKey.Twitter));
        tmp.setX(MenuScreen.ICON_X_TWITTER);
        tmp.setY(MenuScreen.ICON_Y);
        this.buttons.put(ButtonKey.Twitter, tmp);
        
        tmp = new Button(Images.getImage(Assets.ImageMenuKey.Youtube));
        tmp.setX(MenuScreen.ICON_X_YOUTUBE);
        tmp.setY(MenuScreen.ICON_Y);
        this.buttons.put(ButtonKey.Youtube, tmp);
    }
    
    /**
     * Get the list of buttons.<br>
     * We typically use this list to help load/set the settings based on the index of each button.
     * @return The list of buttons on the options screen
     */
    public HashMap<ButtonKey, Button> getButtons()
    {
    	return this.buttons;
    }
    
    private void addButtonDifficulty(final int x, final int y)
    {
        Button button = new Button(Images.getImage(Assets.ImageMenuKey.Button));
        
        for (Assets.TextKey key : Assets.TextKey.values())
        {
            button.addDescription("Levels: " + key.getDesc());
        }
        
        button.setX(x);
        button.setY(y);
        this.buttons.put(ButtonKey.Difficulty, button);
    }
    
    private void addButtonBack(final int x, final int y)
    {
        Button button = new Button(Images.getImage(Assets.ImageMenuKey.Button));
        button.addDescription("Back");
        button.setX(x);
        button.setY(y);
        this.buttons.put(ButtonKey.Back, button);
    }
    
    private void addButtonSound(final int x, final int y)
    {
        Button button = new Button(Images.getImage(Assets.ImageMenuKey.Button));
        button.addDescription("Sound: On");
        button.addDescription("Sound: Off");
        button.setX(x);
        button.setY(y);
        this.buttons.put(ButtonKey.Sound, button);
    }

    private void addButtonVibrate(final int x, final int y)
    {
        Button button = new Button(Images.getImage(Assets.ImageMenuKey.Button));
        button.addDescription("Vibrate: On");
        button.addDescription("Vibrate: Off");
        button.setX(x);
        button.setY(y);
    	this.buttons.put(ButtonKey.Vibrate, button);
    }
    
    /**
     * Assign the index.
     * @param key The unique key of the button we want to change
     * @param index The desired index
     */
    public void setIndex(final ButtonKey key, final int index)
    {
    	buttons.get(key).setIndex(index);
    }
    
    /**
     * Get the index selection of the specified button
     * @param key The unique key of the button we want to check
     * @return The current selection for the specified button key
     */
    public int getIndex(final ButtonKey key)
    {
    	return buttons.get(key).getIndex();
    }
    
    /**
     * Reset any necessary screen elements here
     */
    @Override
    public void reset()
    {
        if (buttons != null)
        {
        	for (ButtonKey key : ButtonKey.values())
        	{
        		//get the current button
        		Button button = buttons.get(key);
        		
        		try
        		{
	        		switch (key)
	        		{
						case Back:
						case Sound:
						case Difficulty:
						case Vibrate:
							button.positionText(screen.getPaint());
							break;
							
						//do nothing for these
						case Instructions:
						case Twitter:
						case Facebook:
						case Youtube:
							break;
							
						default:
							throw new Exception("Key not handled here: " + key);
	        		}
        		}
        		catch (Exception e)
        		{
        			e.printStackTrace();
        		}
        	}
        }
    }
    
    @Override
    public boolean update(final int action, final float x, final float y) throws Exception
    {
    	//we only want motion event up
    	if (action != MotionEvent.ACTION_UP)
    		return true;
    	
        if (buttons != null)
        {
        	for (ButtonKey key : ButtonKey.values())
        	{
        		//get the current button
        		Button button = buttons.get(key);
        		
        		//if the button does not exist skip to the next
        		if (button == null)
        			continue;
        		
    			//if we did not select this button, skip to the next
    			if (!button.contains(x, y))
    				continue;
				
				//determine which button
				switch (key)
				{
    				case Back:
    					
    					//change index
    					button.setIndex(button.getIndex() + 1);
    					
    	                //store our settings
    	                settings.save();
    	                
    	                //set ready state
    	                screen.setState(ScreenManager.State.Ready);
    	                
    	                //play sound effect
    	                Audio.play(Assets.AudioMenuKey.Selection);
    	                
    	                //no need to continue
    	                return false;
    	                
    				case Vibrate:
    					
    					//change index
    					button.setIndex(button.getIndex() + 1);
    					
    					//position the text
    			        button.positionText(screen.getPaint());
    					
    	                //play sound effect
    	                Audio.play(Assets.AudioMenuKey.Selection);
    	                
                        //no need to continue
                        return false;
    					
    				case Sound:
    	    			
    					//change index
    					button.setIndex(button.getIndex() + 1);
    					
    					//position the text
    			        button.positionText(screen.getPaint());
    			        
                        //flip setting
                        Audio.setAudioEnabled(!Audio.isAudioEnabled());
                        
                        //we also want to update the audio button in the controller so the correct is displayed
                        if (screen.getScreenGame() != null && screen.getScreenGame().getGame() != null)
                        {
                        	//make sure the controller exists
                    		if (screen.getScreenGame().getGame().getController() != null)
                    			screen.getScreenGame().getGame().getController().reset();
                        }
                        
                        //play sound effect
                        Audio.play(Assets.AudioMenuKey.Selection);
                        
                        //exit loop
                        return false;
                        
    				case Difficulty:
    					
    					//change index
    					button.setIndex(button.getIndex() + 1);
    					
    					//position the text
    			        button.positionText(screen.getPaint());
    					
                        //play sound effect
                        Audio.play(Assets.AudioMenuKey.Selection);
                        
                        //no need to continue
    					return false;
                        
    				case Instructions:
    					
    	                //play sound effect
    	                Audio.play(Assets.AudioMenuKey.Selection);
    	                
    	                //go to instructions
    	                this.screen.getPanel().getActivity().openWebpage(MainActivity.WEBPAGE_GAME_INSTRUCTIONS_URL);
    	                
    	                //we do not request any additional events
    	                return false;
    					
    				case Facebook:
    					
    	                //play sound effect
    	                Audio.play(Assets.AudioMenuKey.Selection);
    	                
    	                //go to instructions
    	                this.screen.getPanel().getActivity().openWebpage(MainActivity.WEBPAGE_FACEBOOK_URL);
    	                
    	                //we do not request any additional events
    	                return false;
    					
    				case Twitter:
    					
    	                //play sound effect
    	                Audio.play(Assets.AudioMenuKey.Selection);
    	                
    	                //go to twitter
    	                this.screen.getPanel().getActivity().openWebpage(MainActivity.WEBPAGE_TWITTER_URL);
    	                
    	                //we do not request any additional events
    	                return false;
    				
    				case Youtube:
    					
    	                //play sound effect
    	                Audio.play(Assets.AudioMenuKey.Selection);
    	                
    	                //go to youtube
    	                this.screen.getPanel().getActivity().openWebpage(MainActivity.WEBPAGE_YOUTUBE_URL);
    	                
    	                //we do not request any additional events
    	                return false;
    	                
    				default:
                    	throw new Exception("Key not setup here: " + key);
				}
        	}
        }
    	
        //return true
        return true;
    }
    
    @Override
    public void update() throws Exception
    {
        //no updates needed here
    }
    
    @Override
    public void render(final Canvas canvas) throws Exception
    {
        //draw our main logo
        canvas.drawBitmap(logo, ScreenManager.LOGO_X, ScreenManager.LOGO_Y, null);
        
        //draw the menu buttons
    	for (ButtonKey key : ButtonKey.values())
    	{
    		if (buttons.get(key) != null)
    		{
    			switch (key)
    			{
	    			case Back:
	    			case Sound:
	    			case Difficulty:
	    			case Vibrate:
	    				buttons.get(key).render(canvas, screen.getPaint());
	    				break;
	    				
	    			case Instructions:
	    			case Facebook:
	    			case Twitter:
	    			case Youtube:
	    				buttons.get(key).render(canvas);
	    				break;
	    				
	    			default:
	    				throw new Exception("Button with index not setup here: " + key);
    			}
    		}
    			
    	}
    }
    
    @Override
    public void dispose()
    {
        if (settings != null)
        {
            settings.dispose();
            settings = null;
        }
        
        if (buttons != null)
        {
        	for (ButtonKey key : ButtonKey.values())
        	{
        		if (buttons.get(key) != null)
        		{
        			buttons.get(key).dispose();
        			buttons.put(key, null);
        		}
        	}
        	
        	buttons.clear();
        	buttons = null;
        }    
    }
}