package com.gamesbykevin.sokoban;

import com.gamesbykevin.sokoban.assets.Assets;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

/**
 * Splash loading screen
 * @author GOD
 */
public class Splash extends Activity
{
    /**
     * Time delay to show the splash screen
     */
    private static final long TIME_DELAY = 1000L;
    
    /**
     * Called when the activity is first created
     * @param savedInstanceState 
     */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        //turn the title off
        super.requestWindowFeature(Window.FEATURE_NO_TITLE);

        //set the screen to full screen
        super.getWindow().setFlags(
	        WindowManager.LayoutParams.FLAG_FULLSCREEN, 
	        WindowManager.LayoutParams.FLAG_FULLSCREEN
        );
        
        //call parent create
        super.onCreate(savedInstanceState);
        
        //set our content view to show the image
        super.setContentView(R.layout.splash);
        
        try
        {
            //load the game assets
            Assets.load(this);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    /**
     * Override the finish call
     */
    @Override
    public void finish()
    {
        super.finish();
    }
    
    /**
     * Part of the activity life cycle
     */
    @Override
    public void onStart()
    {
        //call parent functionality
        super.onStart();
        
        //add a delay to show the splash image before starting main activity
        new Handler().postDelayed(new Runnable() {
 
            /**
             * Run will be executed once the time delay is over
             */
            @Override
            public void run() {
                
                //start our main activity
                startActivity(new Intent(Splash.this, MainActivity.class));
 
                //close this activity
                finish();
            }
        }, TIME_DELAY);
    }
    
    /**
     * Part of the activity life cycle
     */
    @Override
    public void onStop()
    {
        super.onStop();
    }
    
    /**
     * Part of the activity life cycle
     */
    @Override
    public void onDestroy()
    {
        //perform final cleanup
        super.onDestroy();
    }
    
    /**
     * Part of the activity life cycle
     */
    @Override
    public void onPause()
    {
        super.onPause();
    }
}