package com.gamesbykevin.sokoban;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.gamesbykevin.sokoban.panel.GamePanel;

public class MainActivity extends Activity
{
    //our game panel
    private GamePanel panel;
    
    /**
     * Our web site address where more games can be found
     */
    public static final String WEBPAGE_MORE_GAMES_URL = "http://gamesbykevin.com";

    /**
     * The web address where this game can be rated
     */
    public static final String WEBPAGE_RATE_URL = "https://play.google.com/store/apps/details?id=com.gamesbykevin.sokoban";

    /**
     * The url that contains the instructions for the game
     */
    public static final String WEBPAGE_GAME_INSTRUCTIONS_URL = "http://gamesbykevin.com/2015/10/13/sokoban-2/";
    
    /**
     * The face book url
     */
    public static final String WEBPAGE_FACEBOOK_URL = "http://facebook.com/gamesbykevin";
    
    /**
     * The twitter url
     */
    public static final String WEBPAGE_TWITTER_URL = "http://twitter.com/gamesbykevin";
    
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
        
        //if the panel has not been created
        if (getGamePanel() == null)
        {
            //create the game panel
            setGamePanel(new GamePanel(this));

            //add callback to the game panel to intercept events
            getGamePanel().getHolder().addCallback(getGamePanel());
        }

        //set the content view to our game
        setContentView(getGamePanel());
    }
    
    /**
     * Override the finish call
     */
    @Override
    public void finish()
    {
        //cleanup game panel if it exists
        if (getGamePanel() != null)
        {
            getGamePanel().dispose();
            setGamePanel(null);
        }
        
        //call parent
        super.finish();
    }
    
    /**
     * Part of the activity life cycle
     */
    @Override
    public void onStart()
    {
        //call parent
        super.onStart();
    }
    
    /**
     * Part of the activity life cycle
     */
    @Override
    public void onStop()
    {
        //call parent
        super.onStop();
    }
    
    /**
     * Part of the activity life cycle
     */
    @Override
    public void onDestroy()
    {
        //finish the activity
        this.finish();
        
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
    
    /**
     * Navigate to the desired web page
     * @param url The desired url
     */
    public void openWebpage(final String url)
    {
        //create action view intent
        Intent intent = new Intent(Intent.ACTION_VIEW);
        
        //the content will be the web page
        intent.setData(Uri.parse(url));
        
        //start this new activity
        startActivity(intent);        
    }
    
    /**
     * Get the game panel.
     * @return The object containing our game logic, assets, threads, etc...
     */
    private GamePanel getGamePanel()
    {
        return this.panel;
    }
    
    /**
     * Assign the game panel
     * @param panel The game panel
     */
    private void setGamePanel(final GamePanel panel)
    {
        this.panel = panel;
    }
}