package com.gamesbykevin.sokoban.thread;

import android.graphics.Canvas;
import android.view.SurfaceHolder;
import com.gamesbykevin.androidframework.anim.Animation;

import com.gamesbykevin.sokoban.panel.GamePanel;

/**
 * Our main thread containing the game loop
 * @author ABRAHAM
 */
public class MainThread extends Thread
{
    /**
     * Is debug mode enabled?
     */
    public static final boolean DEBUG = false;
    
    //the assigned frames per second for this game
    public static final int FPS = 30;
    
    //our game panel
    private final GamePanel panel;
    
    //area where game play is rendered
    private final SurfaceHolder holder;
    
    //is the thread running
    private boolean running;
    
    //our canvas to render image(s)
    private Canvas canvas;
    
    /**
     * When the game is terminated and recycling variables, <br>
     * this is the maximum number of attempts to stop the thread
     */
    public static final int COMPLETE_THREAD_ATTEMPTS = 1000;
    
    //do we pause the update/render
    private boolean pause = false;
    
    public MainThread(SurfaceHolder holder, GamePanel panel)
    {
    	//call parent constructor
        super();
        
        //assign surface holder reference object
        this.holder = holder;
        
        //assign game panel reference object
        this.panel = panel;
    }
    
    @Override
    public void run()
    {
        //track total time elapsed to calculate fps
        long totalTime = 0;
        
        //the frame count
        int frames = 0;
        
        //the expected amount of time per each update
        final long targetTime = (Animation.MILLISECONDS_PER_SECOND / FPS);
        
        try
        {
            //continue to loop while the thread is running
            while (isRunning())
            {
            	//if the game is paused we won't continue
            	if (isPaused())
            		continue;
            	
                //get the start time of this update
                final long startTime = System.nanoTime();

                //assign the canvas null
                setCanvas(null);

                try 
                {
                    //attempt to lock the canvas to edit the pixels of the surface
                	setCanvas(getHolder().lockCanvas());

                    //make sure no other threads are accessing the holder
                    synchronized (getHolder())
                    {
                        //update our game panel
                        getPanel().update();

                        //if the canvas object was obtained and we did not pause, render
                        if (getCanvas() != null && !isPaused())
                            getPanel().draw(getCanvas());
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                finally 
                {
                    //remove the lock (if possible)
                    if (getCanvas() != null)
                    {
                        try
                        {
                            //render the pixels on the canvas to the screen
                        	getHolder().unlockCanvasAndPost(getCanvas());
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                }

                //calculate the number of milliseconds elapsed
                final long timeMillis = (System.nanoTime() - startTime) / Animation.NANO_SECONDS_PER_MILLISECOND;

                //determine the amount of time to sleep
                long waitTime = targetTime - timeMillis;

                //make sure the wait time is at least 1 millisecond
                if (waitTime < 1)
                    waitTime = 1;
                
                try
                {
                    //sleep the thread
                    sleep(waitTime);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }

                //if we are debugging, print the fps
                if (DEBUG)
                {
                    //calculate the total time passed
                    totalTime += System.nanoTime() - startTime;

                    //increase the frame count
                    frames++;

                    //if the frame count = the assigned fps
                    if (frames == FPS)
                    {
                        //calculate the average fps
                        final double fpsAverage = (double)Animation.MILLISECONDS_PER_SECOND / ((double)(totalTime / frames) / Animation.NANO_SECONDS_PER_MILLISECOND);

                        //reset these values
                        frames = 0;
                        totalTime = 0;

                        //display the average
                        System.out.println("Average FPS " + fpsAverage);
                    }
                }
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        finally 
        {
	        //stop thread
	        this.setRunning(false);
        }
    }
    
    /**
     * Set the pause flag.<br>
     * If we are pausing the game we will make sure the canvas is not locked
     * @param pause true if you want to prevent the game panel update and render, false otherwise
     */
    public void setPause(final boolean pause)
    {
    	this.pause = pause;
    }
    
    /**
     * Is the thread paused?<br>
     * @return true = we will not update or render the game panel object, false = otherwise
     */
    public boolean isPaused()
    {
    	return this.pause;
    }
    
    /**
     * Assign the thread to run.
     * @param running true the thread will continue to loop, false the thread will finish
     */
    public void setRunning(final boolean running)
    {
        this.running = running;
    }
    
    /**
     * Is the thread set to run?
     * @return true = yes, false = no
     */
    public boolean isRunning()
    {
        return this.running;
    }
    
    /**
     * Assign the canvas
     * @param canvas The desired canvas object
     */
    private void setCanvas(final Canvas canvas)
    {
    	this.canvas = canvas;
    }
    
    /**
     * Get the canvas
     * @return The object we want to write pixel data to
     */
    private Canvas getCanvas()
    {
    	return this.canvas;
    }
    
    /**
     * Get the game panel
     * @return The game panel object reference
     */
    private GamePanel getPanel()
    {
    	return this.panel;
    }
    
    /**
     * Get the surface holder
     * @return The surface holder reference object
     */
    private SurfaceHolder getHolder()
    {
    	return this.holder;
    }
}