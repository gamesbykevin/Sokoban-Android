package com.gamesbykevin.sokoban.screen;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;

import com.gamesbykevin.androidframework.awt.Button;
import com.gamesbykevin.androidframework.resources.Audio;
import com.gamesbykevin.androidframework.resources.Disposable;
import com.gamesbykevin.androidframework.resources.Images;
import com.gamesbykevin.androidframework.screen.Screen;
import com.gamesbykevin.sokoban.assets.Assets;
import com.gamesbykevin.sokoban.panel.GamePanel;
import java.util.HashMap;

/**
 * The exit screen, when the player wants to go back to the menu
 * @author GOD
 */
public class ExitScreen implements Screen, Disposable
{
    /**
     * Custom message displayed on screen
     */
    private static final String MESSAGE = "Go   back   to   menu?";
    
    //where our message is to be rendered
    private final int messageX, messageY;
    
    //our main screen reference
    private final ScreenManager screen;
    
    //object to paint background
    private Paint paint;
    
    //all of the buttons for the player to control
    private HashMap<Assets.ImageMenuKey, Button> buttons;
    
    /**
     * The dimensions of the buttons
     */
    private static final int BUTTON_DIMENSION = 96;
    
    /**
     * Font size for the message
     */
    private static final float MESSAGE_FONT_SIZE = 28f;
    
    public ExitScreen(final ScreenManager screen)
    {
        //store our parent reference
        this.screen = screen;
        
        //create paint text object
        this.paint = new Paint(screen.getPaint());
        this.paint.setColor(Color.WHITE);
        this.paint.setTextSize(MESSAGE_FONT_SIZE);
        
        //create temporary rectangle
        Rect tmp = new Rect();
        
        //get the rectangle around the message
        paint.getTextBounds(MESSAGE, 0, MESSAGE.length(), tmp);
        
        //calculate where text message is to be rendered
        messageX = (GamePanel.WIDTH / 2) - (tmp.width() / 2);
        messageY = (GamePanel.HEIGHT / 2) - (tmp.height() / 2);
        
        //create buttons
        this.buttons = new HashMap<Assets.ImageMenuKey, Button>();
        this.buttons.put(Assets.ImageMenuKey.Cancel, new Button(Images.getImage(Assets.ImageMenuKey.Cancel)));
        this.buttons.put(Assets.ImageMenuKey.Confirm, new Button(Images.getImage(Assets.ImageMenuKey.Confirm)));
        
        //position the buttons below the message
        final int y = messageY + tmp.height();
        
        //position buttons
        this.buttons.get(Assets.ImageMenuKey.Confirm).setX(messageX);
        this.buttons.get(Assets.ImageMenuKey.Confirm).setY(y);
        this.buttons.get(Assets.ImageMenuKey.Cancel).setX(messageX + tmp.width() - BUTTON_DIMENSION);
        this.buttons.get(Assets.ImageMenuKey.Cancel).setY(y);
        
        //set the bounds of each button
        for (Button button : buttons.values())
        {
            button.setWidth(BUTTON_DIMENSION);
            button.setHeight(BUTTON_DIMENSION);
            button.updateBounds();
        }
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
    public boolean update(final int action, final float x, final float y) throws Exception
    {
        if (action == MotionEvent.ACTION_UP)
        {
            if (buttons.get(Assets.ImageMenuKey.Cancel).contains(x, y))
            {
                //if cancel, go back to game
                screen.setState(ScreenManager.State.Running);
                
                //play sound effect
                Audio.play(Assets.AudioMenuKey.Selection);
                
                //return true;
                return false;
            }
            else if (buttons.get(Assets.ImageMenuKey.Confirm).contains(x, y))
            {
                //if confirm, go back to menu
                screen.setState(ScreenManager.State.Ready);
                
                //play sound effect
                Audio.play(Assets.AudioMenuKey.Selection);
                
                //return false;
                return false;
            }
        }
        
        //yes we want additional motion events
        return true;
    }
    
    @Override
    public void update() throws Exception
    {
        //nothing needed to update here
    }
    
    @Override
    public void render(final Canvas canvas) throws Exception
    {
        if (paint != null)
        {
            //draw text
            canvas.drawText(MESSAGE, messageX, messageY, paint);
        }
        
        buttons.get(Assets.ImageMenuKey.Cancel).render(canvas);
        buttons.get(Assets.ImageMenuKey.Confirm).render(canvas);
    }
    
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
        
        if (paint != null)
            paint = null;
    }
}