package com.gamesbykevin.sokoban.screen;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;

import com.gamesbykevin.androidframework.awt.Button;
import com.gamesbykevin.androidframework.resources.Audio;
import com.gamesbykevin.androidframework.resources.Disposable;
import com.gamesbykevin.androidframework.resources.Images;
import com.gamesbykevin.androidframework.screen.Screen;
import com.gamesbykevin.sokoban.assets.Assets;

import java.util.ArrayList;
import java.util.List;

/**
 * This screen will contain the game options
 * @author GOD
 */
public class OptionsScreen implements Screen, Disposable
{
    //our logo reference
    private final Bitmap logo;
    
    //list of buttons for the sound
    private List<Button> sounds;
    
    //list of difficulties
    private List<Button> difficulty;
    
    //store difficulty selection
    private int indexDifficulty = 0;
    
    //the go back button
    private Button back;
    
    //our main screen reference
    private final MainScreen screen;
    
    //paint object to draw text
    private Paint paint;
    
    public OptionsScreen(final MainScreen screen)
    {
        //our logo reference
        this.logo = Images.getImage(Assets.ImageKey.Logo);
        
        //store our screen reference
        this.screen = screen;
        
        //create new paint object
        this.paint = new Paint();
        
        //set the text size
        this.paint.setTextSize(24f);
        
        //set the color
        this.paint.setColor(Color.WHITE);
        
        //start coordinates
        final int x = 110;
        int y = 175;
        final int incrementY = 100;
        
        //add audio option
        this.sounds = new ArrayList<Button>();
        
        Button button = new Button(Images.getImage(Assets.ImageKey.Button));
        button.addDescription("Sound: Disabled");
        button.setX(x);
        button.setY(y);
        button.updateBounds();
        button.positionText(paint);
        
        this.sounds.add(button);
        
        button = new Button(Images.getImage(Assets.ImageKey.Button));
        button.addDescription("Sound: Enabled");
        button.setX(x);
        button.setY(y);
        button.updateBounds();
        button.positionText(paint);
        
        this.sounds.add(button);
        
        
        //add difficulty options
        this.difficulty = new ArrayList<Button>();
        y += incrementY;
        
        for (Assets.TextKey key : Assets.TextKey.values())
        {
            button = new Button(Images.getImage(Assets.ImageKey.Button));
            button.addDescription("Levels: " + key.toString());
            button.setX(x);
            button.setY(y);
            button.updateBounds();
            button.positionText(paint);
            this.difficulty.add(button);
        }
        
        y += incrementY;
        //the back button
        this.back = new Button(Images.getImage(Assets.ImageKey.Button));
        this.back.addDescription("Go Back");
        this.back.setX(x);
        this.back.setY(y);
        this.back.updateBounds();
        this.back.positionText(paint);
    }
    
    /**
     * Reset any necessary screen elements here
     */
    @Override
    public void reset()
    {
        //do we need anything here
    }
    
    @Override
    public boolean update(final MotionEvent event, final float x, final float y) throws Exception
    {
        if (event.getAction() == MotionEvent.ACTION_UP)
        {
            if (back.contains(x, y))
            {
                //set ready state
                screen.setState(MainScreen.State.Ready);
                
                //play sound effect
                Audio.play(Assets.AudioKey.Selection);
                
                //no need to continue
                return false;
            }
            
            for (Button button : sounds)
            {
                if (button.contains(x, y))
                {
                    //flip setting
                    Audio.setAudioEnabled(!Audio.isAudioEnabled());
                    
                    //play sound effect
                    Audio.play(Assets.AudioKey.Selection);
                    
                    //exit loop
                    return false;
                }
            }
            
            for (Button button : difficulty)
            {
                if (button.contains(x, y))
                {
                    //increase index
                    setIndexDifficulty(getIndexDifficulty() + 1);
                    
                    //play sound effect
                    Audio.play(Assets.AudioKey.Selection);
                    
                    //exit loop
                    return false;
                }
            }
        }
        
        //return true
        return true;
    }
    
    /**
     * Assign the difficulty index
     * @param indexDifficulty The desired selection
     */
    public void setIndexDifficulty(final int indexDifficulty)
    {
        this.indexDifficulty = indexDifficulty;
        
        //keep in range
        if (getIndexDifficulty() >= difficulty.size())
            setIndexDifficulty(0);
    }
    
    /**
     * Get the difficulty index
     * @return The user selection for the difficulty
     */
    public int getIndexDifficulty()
    {
        return this.indexDifficulty;
    }
    
    @Override
    public void update() throws Exception
    {
        //no updates needed here
    }
    
    @Override
    public void render(final Canvas canvas) throws Exception
    {
        //draw main logo
        canvas.drawBitmap(logo, MainScreen.LOGO_X, MainScreen.LOGO_Y, null);
        
        //draw the menu buttons
        sounds.get(Audio.isAudioEnabled() ? 1 : 0).render(canvas, paint);
        
        //draw the level difficulty option
        difficulty.get(getIndexDifficulty()).render(canvas, paint);
        
        //render back button
        back.render(canvas, paint);
    }
    
    @Override
    public void dispose()
    {
        if (back != null)
        {
            back.dispose();
            back = null;
        }
        
        if (sounds != null)
        {
            for (Button button : sounds)
            {
                if (button != null)
                {
                    button.dispose();
                    button = null;
                }
            }
            
            sounds.clear();
            sounds = null;
        }
        
        if (difficulty != null)
        {
            for (Button button : difficulty)
            {
                if (button != null)
                {
                    button.dispose();
                    button = null;
                }
            }
            
            difficulty.clear();
            difficulty = null;
        }
        
        if (paint != null)
            paint = null;
    }
}