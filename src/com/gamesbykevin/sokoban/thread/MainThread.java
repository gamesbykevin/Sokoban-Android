package com.gamesbykevin.sokoban.thread;

import android.graphics.Canvas;
import android.view.SurfaceHolder;
import com.gamesbykevin.androidframework.anim.Animation;

import static java.lang.Thread.sleep;

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
    
    //the assigned fps for this game
    private static final int FPS = 30;
    
    //our game panel
    private final GamePanel panel;
    
    //area where game play is rendered
    private final SurfaceHolder holder;
    
    //is the thread running
    private boolean running;
    
    //our canvas to render image(s)
    public static Canvas canvas;
    
    /**
     * When the game is terminated and recycling variables, <br>
     * this is the maximum number of attempts to stop the thread
     */
    public static final int COMPLETE_THREAD_ATTEMPTS = 1000;
    
    public MainThread(SurfaceHolder holder, GamePanel panel)
    {
        super();
        
        //assign the necessary references
        this.holder = holder;
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
                //get the start time of this update
                final long startTime = System.nanoTime();

                //assign the canvas null
                canvas = null;

                try 
                {
                    //attempt to lock the canvas to edit the pixels of the surface
                    canvas = holder.lockCanvas();

                    //make sure no other threads are accessing the holder
                    synchronized (holder)
                    {
                        //update our game panel
                        this.panel.update();

                        //if the canvas object was obtained, render
                        if (canvas != null)
                            this.panel.onDraw(canvas);
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                finally 
                {
                    //remove the lock (if possible)
                    if (canvas != null)
                    {
                        try
                        {
                            //render the pixels on the canvas to the screen
                            holder.unlockCanvasAndPost(canvas);
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

        //stop thread
        this.setRunning(false);
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
}