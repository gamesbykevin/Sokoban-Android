package com.gamesbykevin.sokoban.panel;

import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import com.gamesbykevin.androidframework.resources.Audio;
import com.gamesbykevin.androidframework.resources.Disposable;

import com.gamesbykevin.sokoban.MainActivity;
import com.gamesbykevin.sokoban.assets.Assets;
import com.gamesbykevin.sokoban.screen.MainScreen;
import com.gamesbykevin.sokoban.thread.MainThread;

import java.util.Random;

/**
 * Game Panel class
 * @author GOD
 */
public class GamePanel extends SurfaceView implements SurfaceHolder.Callback, Disposable
{
    /**
     * Our random object used to make random decisions
     */
    public static Random RANDOM = new Random(System.nanoTime());
    
    //default dimensions of window for this game
    public static final int WIDTH = 480;
    public static final int HEIGHT = 800;
    
    //the reference to our activity
    private final MainActivity activity;
    
    //the object containing our game screens
    private MainScreen screen;
    
    //our main game thread
    private MainThread thread;
    
    /**
     * Create a new game panel
     * @param activity Our main activity reference
     */
    public GamePanel(final MainActivity activity)
    {
        //call to parent constructor
        super(activity);
        
        //store context
        this.activity = activity;
            
        //make game panel focusable = true so it can handle events
        super.setFocusable(true);
        
        try
        {
            //load game resources
            Assets.load(getActivity());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    @Override
    public void dispose()
    {
        //it could take several attempts to stop the thread
        boolean retry = true;
        
        //count number of attempts to complete thread
        int count = 0;
        
        while (retry && count <= MainThread.COMPLETE_THREAD_ATTEMPTS)
        {
            try
            {
                //increase count
                count++;
                
                if (thread != null)
                {
                    //set running false, to stop the infinite loop
                    thread.setRunning(false);

                    //wait for thread to finish
                    thread.join();
                }
                
                //if we made it here, we were successful
                retry = false;
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
        
        //make thread null
        this.thread = null;
        
        //assign null
        RANDOM = null;
        
        if (screen != null)
        {
            screen.dispose();
            screen = null;
        }
        
        //recycle asset objects
        Assets.recycle();
    }
    
    /**
     * Get the activity
     * @return The activity reference
     */
    public final MainActivity getActivity()
    {
        return this.activity;
    }
    
    /**
     * Now that the surface has been created we can create our game objects
     * @param holder 
     */
    @Override
    public void surfaceCreated(SurfaceHolder holder)
    {
        try
        {
            //create if null
            if (RANDOM == null)
                RANDOM = new Random(System.nanoTime());
            
            //load game resources
            Assets.load(getActivity());
            
            //make sure the screen is created first before the thread starts
            if (this.screen == null)
                this.screen = new MainScreen(this);

            //if the thread does not exist, create it
            if (this.thread == null)
                this.thread = new MainThread(getHolder(), this);

            //if the thread hasn't been started yet
            if (!this.thread.isRunning())
            {
                //start the thread
                this.thread.setRunning(true);
                this.thread.start();
            }
            
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    @Override
    public boolean onTouchEvent(final MotionEvent event)
    {
        try
        {
            if (this.screen != null)
            {
                //calculate the coordinate offset
                final float scaleFactorX = (float)WIDTH / getWidth();
                final float scaleFactorY = (float)HEIGHT / getHeight();

                //adjust the coordinates
                final float x = event.getRawX() * scaleFactorX;
                final float y = event.getRawY() * scaleFactorY;

                //update the events
                return this.screen.update(event, x, y);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        
        return super.onTouchEvent(event);
    }
    
    @Override
    public void surfaceDestroyed(SurfaceHolder holder)
    {
        //pause the game
        if (screen != null)
        {
            //stop all audio while paused
            Audio.stop();
            
            //set the state
            screen.setState(MainScreen.State.Paused);
        }
    }
    
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
    {
        //does anything need to be done here?
    }
    
    /**
     * Update the game state
     */
    public void update()
    {
        try
        {
            if (screen != null)
                screen.update();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    @Override
    public void draw(Canvas canvas)
    {
        if (canvas != null)
        {
            //store the canvas state
            final int savedState = canvas.save();
            
            try
            {
                //make sure the screen object exists
                if (screen != null)
                {
                    //calculate the screen ratio
                    final float scaleFactorX = getWidth() / (float)GamePanel.WIDTH;
                    final float scaleFactorY = getHeight() / (float)GamePanel.HEIGHT;

                    //scale to the screen size
                    canvas.scale(scaleFactorX, scaleFactorY);
            
                    //render the main sreen containing the game and other screens
                    screen.render(canvas);
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            
            //restore previous canvas state
            canvas.restoreToCount(savedState);
        }
    }
}