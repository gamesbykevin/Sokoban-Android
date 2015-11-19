package com.gamesbykevin.sokoban.screen;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;

import com.gamesbykevin.androidframework.awt.Button;
import com.gamesbykevin.androidframework.resources.Audio;
import com.gamesbykevin.androidframework.resources.Disposable;
import com.gamesbykevin.androidframework.resources.Font;
import com.gamesbykevin.androidframework.resources.Images;
import com.gamesbykevin.androidframework.screen.Screen;

import com.gamesbykevin.sokoban.MainActivity;
import com.gamesbykevin.sokoban.assets.Assets;
import com.gamesbykevin.sokoban.panel.GamePanel;

/**
 * The game over screen
 * @author GOD
 */
public class GameoverScreen implements Screen, Disposable
{
    //our main screen reference
    private final MainScreen screen;
    
    //object to paint background
    private Paint paintMessage;
    
    //the message to display
    private String message = "";
    
    //the dimensions of the text message
    private int pixelW;
    
    //buttons
    private Button nextLevel, replayLevel, mainmenu, rateapp;
    
    //time we have displayed text
    private long time;
    
    /**
     * The amount of time to wait until we render the game over menu
     */
    private static final long DELAY_MENU_DISPLAY = 1750;
    
    //do we display the menu
    private boolean display = false;
    
    /**
     * The alpha visibility when the menu is not shown
     */
    private static final int ALPHA_DARK = 25;
    
    /**
     * The alpha visibility when the menu is shown
     */
    private static final int ALPHA_DARK_OTHER = 200;
    
    /**
     * The text to display for the new game
     */
    private static final String BUTTON_TEXT_NEW_GAME = "Next Level";
    
    /**
     * The text to display to replay the new game
     */
    private static final String BUTTON_TEXT_REPLAY = "Replay";
    
    /**
     * The text to display for the menu
     */
    private static final String BUTTON_TEXT_MENU = "Menu";
    
    public GameoverScreen(final MainScreen screen)
    {
        //store our parent reference
        this.screen = screen;
        
        final int x = 110;
        int y = 75;
        final int addY = 100;

        //create our buttons
        y += addY;
        this.nextLevel = new Button(Images.getImage(Assets.ImageKey.Button));
        this.nextLevel.setX(x);
        this.nextLevel.setY(y);
        this.nextLevel.updateBounds();
        this.nextLevel.addDescription(BUTTON_TEXT_NEW_GAME);
        this.nextLevel.positionText(screen.getPaint());
        
        y+= addY;
        this.replayLevel = new Button(Images.getImage(Assets.ImageKey.Button));
        this.replayLevel.setX(x);
        this.replayLevel.setY(y);
        this.replayLevel.updateBounds();
        this.replayLevel.addDescription(BUTTON_TEXT_REPLAY);
        this.replayLevel.positionText(screen.getPaint());
        
        y += addY;
        this.mainmenu = new Button(Images.getImage(Assets.ImageKey.Button));
        this.mainmenu.setX(x);
        this.mainmenu.setY(y);
        this.mainmenu.updateBounds();
        this.mainmenu.addDescription(BUTTON_TEXT_MENU);
        this.mainmenu.positionText(screen.getPaint());
        
        y += addY;
        this.rateapp = new Button(Images.getImage(Assets.ImageKey.Button));
        this.rateapp.setX(x);
        this.rateapp.setY(y);
        this.rateapp.updateBounds();
        this.rateapp.addDescription(MenuScreen.BUTTON_TEXT_RATE_APP);
        this.rateapp.positionText(screen.getPaint());
    }
    
    /**
     * Reset any necessary screen elements here
     */
    @Override
    public void reset()
    {
        //reset timer
        time = System.currentTimeMillis();
        
        //do we display the menu
        display = false;
    }
    
    /**
     * Assign the message
     * @param message The message we want displayed
     */
    public void setMessage(final String message)
    {
        //assign the message
        this.message = message;
        
        //create temporary rectangle
        Rect tmp = new Rect();
        
        //create paint text object for the message
        if (paintMessage == null)
            paintMessage = new Paint();
        
        //assign metrics
        paintMessage.setColor(Color.WHITE);
        paintMessage.setTextSize(30f);
        paintMessage.setTypeface(Font.getFont(Assets.FontKey.Default));
        
        //get the rectangle around the message
        paintMessage.getTextBounds(message, 0, message.length(), tmp);
        
        //store the dimensions
        pixelW = tmp.width();
    }
    
    @Override
    public boolean update(final MotionEvent event, final float x, final float y) throws Exception
    {
        //if we aren't displaying the menu, return false
        if (!display)
            return false;
        
        if (event.getAction() == MotionEvent.ACTION_UP)
        {
            if (nextLevel.contains(x, y))
            {
                //move to next level
                screen.getScreenGame().getGame().getLevels().setIndex(screen.getScreenGame().getGame().getLevels().getIndex() + 1);
                
                //setup next level
                screen.getScreenGame().getGame().getLevels().reset();
                
                //position player at start of next level
                screen.getScreenGame().getGame().getPlayer().reset(screen.getScreenGame().getGame().getLevels().getLevel());
                
                //move back to the game
                screen.setState(MainScreen.State.Running);
                
                //play sound effect
                Audio.play(Assets.AudioKey.Selection);
                
                //we don't request additional motion events
                return false;
            }
            else if (replayLevel.contains(x, y))
            {
                //reset level
                screen.getScreenGame().getGame().getLevels().reset();
                
                //position player at start of level
                screen.getScreenGame().getGame().getPlayer().reset(screen.getScreenGame().getGame().getLevels().getLevel());
                
                //move back to the game
                screen.setState(MainScreen.State.Running);
                
                //play sound effect
                Audio.play(Assets.AudioKey.Selection);
                
                //we don't request additional motion events
                return false;
            }
            else if (mainmenu.contains(x, y))
            {
                //move to the main menu
                screen.setState(MainScreen.State.Ready);
                
                //play sound effect
                Audio.play(Assets.AudioKey.Selection);
                
                //we don't request additional motion events
                return false;
            }
            else if (rateapp.contains(x, y))
            {
                //play sound effect
                Audio.play(Assets.AudioKey.Selection);
                
                //go to rate game page
                screen.getPanel().getActivity().openWebpage(MainActivity.WEBPAGE_RATE_URL);
                
                //we don't request additional motion events
                return false;
            }
        }
        
        //no action was taken here
        return true;
    }
    
    @Override
    public void update() throws Exception
    {
        //if not displaying the menu, track timer
        if (!display)
        {
            //if time has passed display menu
            if (System.currentTimeMillis() - time >= DELAY_MENU_DISPLAY)
                display = true;
        }
    }
    
    @Override
    public void render(final Canvas canvas) throws Exception
    {
        //if the menu is displayed we will darken the background accordingly
        if (display)
        {
            MainScreen.darkenBackground(canvas, ALPHA_DARK_OTHER);
        }
        else
        {
            MainScreen.darkenBackground(canvas, ALPHA_DARK);
        }
        
        if (paintMessage != null)
        {
            //calculate middle
            final int x = (GamePanel.WIDTH / 2) - (pixelW / 2);
            final int y = (int)(GamePanel.HEIGHT * .15);
             
            //draw text
            canvas.drawText(this.message, x, y, paintMessage);
        }
        
        //do we display the menu
        if (display)
        {
            //render buttons
            nextLevel.render(canvas, screen.getPaint());
            replayLevel.render(canvas, screen.getPaint());
            rateapp.render(canvas, screen.getPaint());
            mainmenu.render(canvas, screen.getPaint());
        }
    }
    
    @Override
    public void dispose()
    {
        if (paintMessage != null)
            paintMessage = null;
        
        if (nextLevel != null)
        {
            nextLevel.dispose();
            nextLevel = null;
        }
        
        if (replayLevel != null)
        {
            replayLevel.dispose();
            replayLevel = null;
        }
        
        if (mainmenu != null)
        {
            mainmenu.dispose();
            mainmenu = null;
        }
        
        if (rateapp != null)
        {
            rateapp.dispose();
            rateapp = null;
        }
    }
}