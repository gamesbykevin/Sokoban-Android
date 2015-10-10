package com.gamesbykevin.sokoban.screen;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import com.gamesbykevin.androidframework.anim.Animation;
import com.gamesbykevin.androidframework.base.Entity;
import com.gamesbykevin.androidframework.resources.Audio;

import com.gamesbykevin.androidframework.resources.Disposable;
import com.gamesbykevin.androidframework.resources.Images;
import com.gamesbykevin.androidframework.screen.Screen;

import com.gamesbykevin.sokoban.assets.Assets;
import com.gamesbykevin.sokoban.game.Game;
import com.gamesbykevin.sokoban.panel.GamePanel;

import java.util.HashMap;

/**
 * This class will contain the game screens
 * @author ABRAHAM
 */
public final class MainScreen implements Screen, Disposable
{
    //the background image
    private Entity background;
    
    /**
     * These are the different states in our game
     */
    public enum State 
    {
        Ready, Running, Paused, Options, Exit, GameOver
    }
    
    //the current state of the game
    private State state;
    
    //our game panel
    private final GamePanel panel;
    
    //the screens in our main screen
    private HashMap<State, Screen> screens;
    
    //the object representing the button text
    private Paint paintButton;
    
    /**
     * The x-coordinate where we want the logo to be displayed
     */
    public static final int LOGO_X = 40;
    
    /**
     * The y-coordinate where we want the logo to be displayed
     */
    public static final int LOGO_Y = 40;
    
    /**
     * The alpha visibility to apply when darkening the background
     */
    private static final int ALPHA_DARK = 175;
    
    /**
     * Create our main screen
     * @param panel The reference to our game panel
     */
    public MainScreen(final GamePanel panel)
    {
        //create a new background
        this.background = new Entity();
        
        //assign position, size
        this.background.setX(0);
        this.background.setY(0);
        this.background.setWidth(GamePanel.WIDTH);
        this.background.setHeight(GamePanel.HEIGHT);

        //add animation to spritesheet
        this.background.getSpritesheet().add(Assets.ImageKey.Background, new Animation(Images.getImage(Assets.ImageKey.Background)));
        
        //store our game panel reference
        this.panel = panel;
        
        //default to the ready state
        this.state = State.Ready;
        
        //create new hashmap
        this.screens = new HashMap<State, Screen>();
        this.screens.put(State.Ready, new MenuScreen(this));
        this.screens.put(State.Paused, new PauseScreen(this));
        this.screens.put(State.Exit, new ExitScreen(this));
        this.screens.put(State.Options, new OptionsScreen(this));
        this.screens.put(State.GameOver, new GameoverScreen(this));
        this.screens.put(State.Running, new GameScreen(this));
    }
    
    @Override
    public boolean update(final MotionEvent event, final float x, final float y) throws Exception
    {
        return getScreen(getState()).update(event, x, y);
    }
    
    /**
     * Get the paint object
     * @return The object controlling the text on the buttons
     */
    public Paint getPaint()
    {
        if (paintButton == null)
        {
            //create new paint object
            this.paintButton = new Paint();

            //set the text size
            this.paintButton.setTextSize(24f);

            //set the color
            this.paintButton.setColor(Color.WHITE);
        }
        
        //return ojbect
        return this.paintButton;
    }
    
    /**
     * Update runtime logic here (if needed)
     * @throws Exception 
     */
    @Override
    public void update() throws Exception
    {
        getScreen(getState()).update();
    }
    
    protected GamePanel getPanel()
    {
        return this.panel;
    }
    
    public State getState()
    {
        return this.state;
    }
    
    public Screen getScreen(final State state)
    {
        return screens.get(state);
    }
    
    public GameoverScreen getScreenGameover()
    {
        return (GameoverScreen)screens.get(State.GameOver);
    }
    
    public PauseScreen getScreenPaused()
    {
        return (PauseScreen)screens.get(State.Paused);
    }
    
    public GameScreen getScreenGame()
    {
        return (GameScreen)screens.get(State.Running);
    }
    
    public OptionsScreen getScreenOptions()
    {
        return (OptionsScreen)screens.get(State.Options);
    }
    
    /**
     * Change the state
     * @param state The state of the game. Running, Paused, Ready, Game Over, etc..
     */
    public void setState(final State state)
    {
        //if pausing store the previous state
        if (state == State.Paused)
        {
            //set the previous state
            getScreenPaused().setStatePrevious(getState());
        }
        else if (state == State.GameOver && getState() != State.Paused)
        {
            //reset screen
            getScreen(state).reset();
        }
        
        //if not in the running state
        if (state != State.Running)
        {
            //if we were previously running stop audio
            if (getState() == State.Running)
                Audio.stop();
        }
        else
        {
            //play random song
            //Assets.playSong();
        }
        
        //assign the state
        this.state = state;
    }
    
    public void render(final Canvas canvas) throws Exception
    {
        if (canvas != null)
        {
            //fill background
            canvas.drawColor(Color.BLACK);
            
            //draw the background
            background.render(canvas);
            
            //render game screen
            getScreen(State.Running).render(canvas);
            
            //render the appropriate screen
            switch (getState())
            {
                case Ready:
                    //darken background
                    darkenBackground(canvas);
                    
                    //draw menu
                    if (getScreen(getState()) != null)
                        getScreen(getState()).render(canvas);
                    break;

                case Running:
                    //we are already rendering the game
                    break;

                case Paused:
                    if (getScreenPaused().getStatePrevious() != State.Running)
                        getScreen(getScreenPaused().getStatePrevious()).render(canvas);
                    
                    //darken background
                    darkenBackground(canvas);
                    
                    if (getScreen(getState()) != null)
                        getScreen(getState()).render(canvas);
                    break;

                case Options:
                    //darken background
                    darkenBackground(canvas);
                    
                    if (getScreen(getState()) != null)
                        getScreen(getState()).render(canvas);
                    break;
                    
                case Exit:
                    //darken background
                    darkenBackground(canvas);
                    
                    if (getScreen(getState()) != null)
                        getScreen(getState()).render(canvas);
                    break;
                    
                case GameOver:
                    //render game over info
                    getScreen(getState()).render(canvas);
                    break;

                //this shouldn't happen
                default:
                    throw new Exception("Undefined state " + state.toString());
            }
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
    
    /**
     * Draw an overlay over the background
     * @param canvas Object we are writing pixel data to
     * @param alpha The visibility of the overlay range from 0 (0% visible) - 255 (100% visible)
     */
    public static final void darkenBackground(final Canvas canvas, int alpha)
    {
        //keep in range
        if (alpha < 0)
            alpha = 0;
        if (alpha > 255)
            alpha = 255;
        
        //darken background
        canvas.drawARGB(alpha, 0, 0, 0);
    }
    
    /**
     * Draw an overlay over the background
     * @param canvas Object we are writing pixel data to
     */
    public static final void darkenBackground(final Canvas canvas)
    {
        //darken background
        darkenBackground(canvas, ALPHA_DARK);
    }
    
    @Override
    public void dispose()
    {
        if (background != null)
        {
            background.dispose();
            background = null;
        }
        
        if (paintButton != null)
            paintButton = null;
        
        if (screens != null)
        {
            for (Screen screen : screens.values())
            {
                if (screen != null)
                {
                    screen.dispose();
                    screen = null;
                }
            }
            
            screens.clear();
            screens = null;
        }
    }
}