package com.gamesbykevin.sokoban.panel;

import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import com.gamesbykevin.androidframework.resources.Audio;
import com.gamesbykevin.androidframework.resources.Disposable;
import com.gamesbykevin.sokoban.screen.ScreenManager.State;
import com.gamesbykevin.sokoban.MainActivity;
import com.gamesbykevin.sokoban.assets.Assets;
import com.gamesbykevin.sokoban.screen.ScreenManager;
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
    private ScreenManager screen;
    
    //our main game thread
    private MainThread thread;
    
    //did motion event down happen
    private boolean down = false;
    
    //get the ratio of the users screen compared to the default dimensions for the motion event
    private float scaleMotionX, scaleMotionY;

    //get the ratio of the users screen compared to the default dimensions for the render
    private float scaleRenderX, scaleRenderY;
    
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
    }
    
    /**
     * Get the main game thread.<br>
     * If the main thread does not exist, it will be created
     * @return The main game thread
     */
    private MainThread getThread()
    {
    	return this.thread;
    }
    
    /**
     * Get the screen manager 
     * @return The screen manager containing all our screens
     */
    private ScreenManager getScreen()
    {
    	return this.screen;
    }
    
    @Override
    public void dispose()
    {
        //it could take several attempts to stop the thread
        boolean retry = true;
        
        //count number of attempts to complete thread
        int count = 0;
        
        //here we will attempt to stop the thread
        while (retry && count <= MainThread.COMPLETE_THREAD_ATTEMPTS)
        {
            try
            {
                //increase count
                count++;
                
                if (getThread() != null)
                {
                    //set running false, to stop the infinite loop
                	getThread().setRunning(false);

                    //wait for thread to finish
                	getThread().join();
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
        
        //recycle all asset objects
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
    
    @Override
    public boolean performClick() 
    {
        //call parent
        super.performClick();
        
        //return true
        return true;
    }
    
    @Override
    public boolean onTouchEvent(final MotionEvent event)
    {
        try
        {
            if (getScreen() != null)
            {
                //adjust the coordinates
                final float x = event.getRawX() * getScaleMotionX();
                final float y = event.getRawY() * getScaleMotionY();

                switch (event.getAction())
                {
	                case MotionEvent.ACTION_DOWN:
	                	
	                	//flag motion down occurred
		            	down = true;
		            	break;
		            	
	                case MotionEvent.ACTION_UP:
	                	
	                	//if we have previously action down
	                	if (down)
	                	{
	                		//flag false
	                		down = false;
	                		
	                    	//perform click
	                    	performClick();
	                	}
	                	break;
                }
                
                //update the screen/game etc.. with the specified motion events
                return getScreen().update(event, x, y);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        
        return super.onTouchEvent(event);
    }
    
    /**
     * Now that the surface has been created we can create our game objects
     * @param holder Object used to track events
     */
    @Override
    public void surfaceCreated(SurfaceHolder holder)
    {
        try
        {
            //load assets
            Assets.load(getActivity());
            
            //create if null
            if (RANDOM == null)
                RANDOM = new Random(System.nanoTime());
            
            //create the thread if it doesn't exist
            if (getThread() == null)
        		this.thread = new MainThread(getHolder(), this);
            
            //if the thread hasn't been started yet
            if (!getThread().isRunning())
            {
                //start the thread
            	getThread().setRunning(true);
            	getThread().start();
            }
            
            //flag the thread as not paused
            getThread().setPause(false);
            
            //store the ratio for the motion event
            this.scaleMotionX = (float)GamePanel.WIDTH / getWidth();
            this.scaleMotionY = (float)GamePanel.HEIGHT / getHeight();
            
            //store the ratio for the render
            this.scaleRenderX = getWidth() / (float)GamePanel.WIDTH;
            this.scaleRenderY = getHeight() / (float)GamePanel.HEIGHT;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    @Override
    public void surfaceDestroyed(SurfaceHolder holder)
    {
        //pause the game
        if (getScreen() != null)
        {
            //stop all audio while paused
            Audio.stop();
            
            //flag the thread as paused
            getThread().setPause(false);
            
            //set the state
            getScreen().setState(State.Paused);
        }
        else
        {
        	//if the screen does not exist, just exit the game
        	getActivity().finish();
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
            //make sure the screen is created first before the thread starts
            if (getScreen() == null)
            {
                //load all assets
                Assets.load(getActivity());

                //create new screen manager
                this.screen = new ScreenManager(this);
            }
            else
            {
            	getScreen().update();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    /**
     * Get the x scale factor for the motion event
     * @return The x ratio of the user's width compared to the default width
     */
    private float getScaleMotionX()
    {
    	return this.scaleMotionX;
    }
    
    /**
     * Get the y scale factor for the motion event
     * @return The y ratio of the user's height compared to the default height
     */
    private float getScaleMotionY()
    {
    	return this.scaleMotionY;
    }

    /**
     * Get the x scale factor for the render
     * @return The x ratio of the user's width compared to the default width
     */
    private float getScaleRenderX()
    {
    	return this.scaleRenderX;
    }
    
    /**
     * Get the y scale factor for the render
     * @return The y ratio of the user's height compared to the default height
     */
    private float getScaleRenderY()
    {
    	return this.scaleRenderY;
    }
    
    @Override
    public void onDraw(Canvas canvas)
    {
    	draw(canvas);
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
                if (getScreen() != null)
                {
                    //scale to the screen size
                    canvas.scale(getScaleRenderX(), getScaleRenderY());
                
                    //render the main screen containing the game and other screens
                    getScreen().render(canvas);
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