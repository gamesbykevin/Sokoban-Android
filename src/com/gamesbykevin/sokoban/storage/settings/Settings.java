package com.gamesbykevin.sokoban.storage.settings;

import android.app.Activity;
import com.gamesbykevin.androidframework.io.storage.Internal;
import com.gamesbykevin.androidframework.resources.Audio;
import com.gamesbykevin.sokoban.screen.OptionsScreen;
import com.gamesbykevin.sokoban.screen.OptionsScreen.ButtonKey;

/**
 * Save the settings to the internal storage
 * @author GOD
 */
public final class Settings extends Internal
{
    //our options screen reference object
    private final OptionsScreen screen;
    
    /**
     * This string will separate each setting
     */
    private static final String SEPARATOR = ";";
    
    public Settings(final OptionsScreen screen, final Activity activity)
    {
        super("Settings", activity);
        
        //store our screen reference object
        this.screen = screen;
        
        //if content exists load it
        if (super.getContent().toString().trim().length() > 0)
        {
            try
            {
                //split the content into an array
                final String[] data = super.getContent().toString().split(SEPARATOR);

                for (int index = 0; index < ButtonKey.values().length; index++)
                {
                	//get the index value in this array element
                	int indexValue = Integer.parseInt(data[index]);
                	
                	//restore settings
                	screen.setIndex(ButtonKey.values()[index], indexValue);
                	
                	//if the sound option, we need to flag the audio enabled/disabled
                	if (ButtonKey.values()[index] == ButtonKey.Sound)
                		Audio.setAudioEnabled(indexValue == 0);
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        
        //make sure the text in the buttons are aligned
        screen.reset();
    }
    
    /**
     * Save the settings to the internal storage
     */
    @Override
    public void save()
    {
        try
        {
            //remove all existing content
            super.getContent().delete(0, super.getContent().length());

            //save every option we have in our options screen
            for (ButtonKey key : ButtonKey.values())
            {
            	//add the data to our string builder
            	super.getContent().append(screen.getButtons().get(key).getIndex());
            	
            	//add delimiter to separate each setting
        		super.getContent().append(SEPARATOR);
            }
            
            //remove the last character on the end
            super.getContent().deleteCharAt(super.getContent().length() - 1);

            //save data
            super.save();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    @Override
    public void dispose()
    {
        super.dispose();
    }
}