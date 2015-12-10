package com.gamesbykevin.sokoban.game;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Vibrator;
import android.view.MotionEvent;

import com.gamesbykevin.androidframework.base.Cell;
import com.gamesbykevin.androidframework.resources.Font;
import com.gamesbykevin.androidframework.text.TimeFormat;
import com.gamesbykevin.sokoban.assets.Assets;
import com.gamesbykevin.sokoban.game.controller.Controller;
import com.gamesbykevin.sokoban.level.LevelHelper;
import com.gamesbykevin.sokoban.level.Levels;
import com.gamesbykevin.sokoban.player.Player;
import com.gamesbykevin.sokoban.player.PlayerHelper;
import com.gamesbykevin.sokoban.screen.ScreenManager;
import com.gamesbykevin.sokoban.storage.scorecard.ScoreCard;

/**
 * The main game logic will happen here
 * @author ABRAHAM
 */
public final class Game implements IGame
{
    //our main screen object reference
    private final ScreenManager screen;
    
    //the controller for our game
    private Controller controller;
    
    //paint object to draw text
    private Paint paint;
    
    //our level
    private Levels levels;
    
    //the game score card to track best score etc..
    private ScoreCard scoreCard;
    
    //our player
    private Player player;
    
    //this is used to move the player
    private Cell start;
    
    //the location where we display text when selecting level
    private static final int LEVEL_START_TEXT_X = 96;
    private static final int LEVEL_START_TEXT_Y = 761;
    
    //default size of the moves and timer
    private static final float DEFAULT_TEXT_SIZE = 14f;

    //the length to vibrate the phone
    private static final long VIBRATE_DURATION = 500;
    
    //do we reset the current level
    private boolean levelReset = false;
    
    public Game(final ScreenManager screen) throws Exception
    {
        //our main screen object reference
        this.screen = screen;
        
        //create new cell that we reference to move the player
        this.start = new Cell();
        
        //create new paint object
        this.paint = new Paint();
        this.paint.setTypeface(Font.getFont(Assets.FontGameKey.Default));
        this.paint.setTextSize(DEFAULT_TEXT_SIZE);
        this.paint.setColor(Color.WHITE);
        
        //create new controller
        this.controller = new Controller(this);
    }
    
    /**
     * Get our levels
     * @return Levels Object container
     */
    public Levels getLevels()
    {
        return this.levels;
    }
    
    /**
     * Get the score card
     * @return The object tracking the best scores for each level
     */
    public ScoreCard getScorecard()
    {
    	return this.scoreCard;
    }
    
    /**
     * Get the main screen object reference
     * @return The main screen object reference
     */
    public ScreenManager getScreen()
    {
        return this.screen;
    }
    
    /**
     * Restart game with assigned settings
     * @throws Exception 
     */
    @Override
    public void reset(final Assets.TextKey key) throws Exception
    {
    	//create the player if not exists
        if (getPlayer() == null)
            player = new Player();
        
        //create score card
    	scoreCard = new ScoreCard(this, getScreen().getPanel().getActivity(), key.toString() + " ");
        
        //create new levels object
        this.levels = new Levels(key);
        
        //check the storage and update the level select screen
        updateLevelSelect();
        
        //reset the level select
        getLevels().getLevelSelect().reset();
        
        //update the description for the level select
        getLevels().getLevelSelect().setDescription(
        	key.getDesc() + "  ", 
        	LEVEL_START_TEXT_X, 
        	LEVEL_START_TEXT_Y
        );
    }
    
    /**
     * Flag the level to reset to its original state
     */
    public void flagLevelReset()
    {
    	this.levelReset = true;
    }
    
    /**
     * Are we resetting the current level
     * @return true = yes, false = no
     */
    public boolean hasLevelReset()
    {
    	return this.levelReset;
    }
    
    /**
     * Get our controller object
     * @return Object containing game controls
     */
    public Controller getController()
    {
        return this.controller;
    }
    
    /**
     * Get the player
     * @return The player
     */
    public Player getPlayer()
    {
        return this.player;
    }
    
    /**
     * Update the level select screen.<br>
     * We will check our storage and flag the completed levels
     */
    public void updateLevelSelect()
    {
        //only update if storage data exists
        if (getScorecard().getContent().length() > 0)
        {
            //parse level data
            final String[] content = getScorecard().getContent().toString().split(ScoreCard.NEW_LEVEL_DELIMITER);
            
            //loop through each value
            for (String tmp : content)
            {
                //separate the attributes for the current level
                final String[] attributes = tmp.split(ScoreCard.LEVEL_DATA_DELIMITER);
                
                try
                {
                    //parse level index
                    int i = Integer.parseInt(attributes[0]);
                    
                    //update the level select object and flag completed
                    getLevels().getLevelSelect().setCompleted(i, true);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
    }
    
    /**
     * Update the game based on the motion event
     * @param event Motion Event
     * @param x (x-coordinate)
     * @param y (y-coordinate)
     * @throws Exception
     */
    public void update(final MotionEvent event, final float x, final float y) throws Exception
    {
    	//if we are resetting the level we can't continue
    	if (hasLevelReset())
    		return;
    	
        if (!getLevels().getLevelSelect().hasSelection())
        {
            if (event.getAction() == MotionEvent.ACTION_UP)
            {
                //mark the selection to be checked
                getLevels().getLevelSelect().setCheck((int)x, (int)y);
            }
        }
        else
        {
            //only update game if no controller buttons were clicked
            if (!getController().update(event, x, y))
            {
                if (event.getAction() == MotionEvent.ACTION_DOWN)
                {
                    //check that we haven't selected the player yet
                    if (!getPlayer().isSelected())
                    {
                        //make sure the player is at their destination, before it can be moved
                        if (getPlayer().hasTarget())
                        {
                            start.setCol(LevelHelper.getCol(getLevels().getLevel(), x));
                            start.setRow(LevelHelper.getRow(getLevels().getLevel(), y));
                            
                            //flag player as selected
                            getPlayer().setSelected(true);
                        }
                    }
                }
                else if (event.getAction() == MotionEvent.ACTION_UP)
                {
                    //make sure we have selected the player
                    if (getPlayer().isSelected())
                    {
                        //make sure the player is already at the destination before moving again
                        if (getPlayer().hasTarget())
                        {
                            //locate new location
                            double col = LevelHelper.getCol(getLevels().getLevel(), x);
                            double row = LevelHelper.getRow(getLevels().getLevel(), y);
                            
                            //calculate the difference from the previous location
                            double differenceHorizontal = (start.getCol() > col) ? start.getCol() - col : col - start.getCol();
                            double differenceVertical = (start.getRow() > row) ? start.getRow() - row : row - start.getRow();
                            
                            //the greater different will determine the direction
                            if (differenceHorizontal > differenceVertical)
                            {
                                if (start.getCol() > col)
                                {
                                    //assign the player destination
                                    getPlayer().setTarget(getPlayer().getCol() - 1, getPlayer().getRow());
                                }
                                else
                                {
                                    //assign the player destination
                                    getPlayer().setTarget(getPlayer().getCol() + 1, getPlayer().getRow());
                                }
                            }
                            else
                            {
                                if (start.getRow() > row)
                                {
                                    //assign the player destination
                                    getPlayer().setTarget(getPlayer().getCol(), getPlayer().getRow() - 1);
                                }
                                else
                                {
                                    //assign the player destination
                                    getPlayer().setTarget(getPlayer().getCol(), getPlayer().getRow() + 1);
                                }
                            }
                            
                            //calculate the targets
                            PlayerHelper.calculateTargets(getPlayer(), getLevels().getLevel());
                        }
                    }
                }
            }
            else
            {
                //the controller was updated, so we will un-select the player
                getPlayer().setSelected(false);
            }
        }
    }
    
    /**
     * Update game
     * @throws Exception 
     */
    public void update() throws Exception
    {
    	//we can't continue if the level does not exist
    	if (getLevels() == null)
    		return;
    	
    	//if we want to reset the level
    	if (hasLevelReset())
    	{
    		//flag reset false
    		this.levelReset = false;
    		
    		//reset the current level to its original state
    		getLevels().reset();
    		
    		//reset the player to the start position
    		getPlayer().reset(getLevels().getLevel());
    		
    		//no need to continue
    		return;
    	}
    	
        //make sure the level selection was made
        if (getLevels().getLevelSelect().hasSelection())
        {
            //update current level first
            if (getLevels().getLevel() != null)
            {
                if (LevelHelper.hasCompleted(getLevels().getLevel()))
                {
                	//save the time (if better time found or not exist)
                	final boolean result = getScorecard().update(
                		getLevels().getLevelSelect().getLevelIndex(), 
                		getPlayer().getMoves(), 
                		getPlayer().getTime()
                	);

                	//if an update was made update the level select screen
                	if (result)
                		updateLevelSelect();
                	
                	//mark the level as completed
                	getLevels().getLevelSelect().setCompleted(
                		getLevels().getLevelSelect().getLevelIndex(), 
                		true
                	);
                	
            		//make sure vibrate is enabled
            		if (getScreen().getScreenOptions().getIndex(com.gamesbykevin.sokoban.screen.OptionsScreen.ButtonKey.Vibrate) == 0)
            		{
    	        		//get our vibrate object
    	        		Vibrator v = (Vibrator) getScreen().getPanel().getActivity().getSystemService(Context.VIBRATOR_SERVICE);
    	        		 
    					//vibrate for a specified amount of milliseconds
    					v.vibrate(VIBRATE_DURATION);
            		}
                	
                    //set game over state
                	getScreen().setState(ScreenManager.State.GameOver);

                    //set display message
                    getScreen().getScreenGameover().setMessage("Level Complete");

                    //no need to continue
                    return;
                }
                else
                {
                    getLevels().getLevel().update();
                }
            }

            //if player exists update the player
            if (getPlayer() != null)
                getPlayer().update(getLevels().getLevel());
        }
        else
        {
        	//update the level selection
        	getLevels().getLevelSelect().update();
        	
        	//if we now have a selection, reset the player and create the level
        	if (getLevels().getLevelSelect().hasSelection())
        	{
        		//reset the levels
        		getLevels().reset();
        		
        		//reset the player position
                getPlayer().reset(getLevels().getLevel());
        	}
        }
    }
    
    @Override
    public void dispose()
    {
        if (controller != null)
        {
            controller.dispose();
            controller = null;
        }
        
        if (scoreCard != null)
        {
        	scoreCard.dispose();
        	scoreCard = null;
        }
        
        if (levels != null)
        {
            levels.dispose();
            levels = null;
        }
        
        if (player != null)
        {
            player.dispose();
            player = null;
        }
        
        paint = null;
        start = null;
    }
    
    /**
     * Render game elements
     * @param canvas Where to write the pixel data
     * @throws Exception 
     */
    @Override
    public void render(final Canvas canvas) throws Exception
    {
        if (getLevels() != null)
        {
            //render level and/or selections
            getLevels().render(canvas, getScreen().getPaint());
            
            //make sure object exists
            if (getLevels().getLevelSelect().hasSelection() && getPlayer() != null && getController() != null)
            {
                //if level was completed previously, display personal best
                if (getScorecard().hasScore())
                {
                    canvas.drawText(
                    	"Best Move - " + getScorecard().getScore().getMoves(), 
                    	Player.PERSONAL_BEST_INFO_X, 
                    	Player.PERSONAL_BEST_INFO_Y, 
                    	paint
                    );
                    
                    canvas.drawText(
                    	"Best Time - " + TimeFormat.getDescription(TimeFormat.FORMAT_1, getScorecard().getScore().getTime()), 
                    	Player.PERSONAL_BEST_INFO_X, 
                    	Player.PERSONAL_BEST_INFO_Y * 2, 
                    	paint
                    );
                }
                
                //render player
                getPlayer().render(canvas, getScreen().getPaint());
                
                //draw the game controller
                getController().render(canvas);
            }
        }
    }
}