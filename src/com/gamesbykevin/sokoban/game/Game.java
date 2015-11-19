package com.gamesbykevin.sokoban.game;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;

import com.gamesbykevin.androidframework.base.Cell;
import com.gamesbykevin.androidframework.io.storage.Internal;

import com.gamesbykevin.sokoban.assets.Assets;
import com.gamesbykevin.sokoban.game.controller.Controller;
import com.gamesbykevin.sokoban.level.LevelHelper;
import com.gamesbykevin.sokoban.level.Levels;
import com.gamesbykevin.sokoban.player.Player;
import com.gamesbykevin.sokoban.player.PlayerHelper;
import com.gamesbykevin.sokoban.screen.MainScreen;

/**
 * The main game logic will happen here
 * @author ABRAHAM
 */
public final class Game implements IGame
{
    //our main screen object reference
    private final MainScreen screen;
    
    //the controller for our game
    private Controller controller;
    
    //paint object to draw text
    private Paint paint;
    
    //our level
    private Levels levels;
    
    //our player
    private Player player;
    
    //our storage object used to save data
    private Internal storage;
    
    //this is used to move the player
    private Cell start;
    
    /**
     * Text delimiter used to parse internal storage data for each level
     */
    private static final String STORAGE_DELIMITER_LEVEL = ",";
    
    /**
     * Text delimiter used to parse internal storage data for each attribute in a level
     */
    private static final String STORAGE_DELIMITER_ATTRIBUTE = ";";
    
    public Game(final MainScreen screen) throws Exception
    {
        //our main screen object reference
        this.screen = screen;
        
        //create new cell that we reference to move the player
        this.start = new Cell();
        
        //create new paint object
        this.paint = new Paint();
        this.paint.setTextSize(24f);
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
     * Get the main screen object reference
     * @return The main screen object reference
     */
    public MainScreen getMainScreen()
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
        //create new levels object
        this.levels = new Levels(key);

        //create our storage object
        this.storage = new Internal(key.toString(), screen.getPanel().getActivity());
        
        //load storage data into level trackers
        updateTrackers();
        
        if (getPlayer() == null)
            player = new Player();
        
        //mark as level not selected
        getLevels().setSelected(false);
        
        //assign the level description
        getLevels().setLevelDesc(key.toString());
        
        //default to 1st page
        getLevels().setPage(0);
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
     * Update the level trackers.<br>
     * We will check our storage and update the status of the trackers
     */
    public void updateTrackers()
    {
        //only update if storage data exists
        if (getStorage().getContent().length() > 0)
        {
            //parse level data
            final String[] content = getStorage().getContent().toString().split(STORAGE_DELIMITER_LEVEL);
            
            //loop through each value
            for (String tmp : content)
            {
                //separate the attributes for the current level
                final String[] attributes = tmp.split(STORAGE_DELIMITER_ATTRIBUTE);
                
                try
                {
                    //parse index location
                    int i = Integer.parseInt(attributes[0]);
                    
                    //get that tracker and flag completed
                    getLevels().getLevelTracker(i).setCompleted(true);
                    
                    //parse attributes
                    final int moves = Integer.parseInt(attributes[1]);
                    final long time = Long.parseLong(attributes[2]);
                    
                    //load attributes
                    getLevels().getLevelTracker(i).setMoves(moves);
                    getLevels().getLevelTracker(i).setTime(time);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
    }
    
    /**
     * Update the internal storage.<br>
     * We will update the content of the storage with the current list of completed levels
     */
    public void updateStorage()
    {
        //remove all contents
        getStorage().getContent().delete(0, getStorage().getContent().length());
        
        //check each level tracker
        for (int i = 0; i < getLevels().getLevelTrackers().size(); i++)
        {
            if (getLevels().getLevelTracker(i).isCompleted())
            {
                //if content already exists, separate with delimeter
                if (getStorage().getContent().length() > 0)
                    getStorage().getContent().append(STORAGE_DELIMITER_LEVEL);
                
                //add data content to string builder
                getStorage().getContent().append(i);
                getStorage().getContent().append(STORAGE_DELIMITER_ATTRIBUTE);
                getStorage().getContent().append(getLevels().getLevelTracker(i).getMoves());
                getStorage().getContent().append(STORAGE_DELIMITER_ATTRIBUTE);
                getStorage().getContent().append(getLevels().getLevelTracker(i).getTime());
            }
        }
        
        //save data to storage
        getStorage().save();
    }
    
    /**
     * Get storage
     * @return Our internal storage object reference
     */
    private Internal getStorage()
    {
        return this.storage;
    }
    
    /**
     * Update the game based on the motion event
     * @param event Motion Event
     * @param x (x-coordinate)
     * @param y (y-coordinate)
     * @throws Exception
     */
    public void updateMotionEvent(final MotionEvent event, final float x, final float y) throws Exception
    {
        if (!getLevels().isSelected())
        {
            if (event.getAction() == MotionEvent.ACTION_UP)
            {
                //mark the selection
                getLevels().setSelected(x, y);

                //if the selection was made
                if (getLevels().isSelected())
                {
                    //reset player
                    getPlayer().reset(getLevels().getLevel());
                }
            }
        }
        else
        {
            //only update game if no controller buttons were clicked
            if (!getController().updateMotionEvent(event, x, y))
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
        //make sure object exists and we have a level selection before updating
        if (getLevels() != null && getLevels().isSelected())
        {
            //update current level first
            if (getLevels().getLevel() != null)
            {
                if (LevelHelper.hasCompleted(getLevels().getLevel()))
                {
                    //if wasn't completed previously, store best stats
                    if (!getLevels().getLevelTracker().isCompleted())
                    {
                        getLevels().getLevelTracker().setMoves(player.getMoves());
                        getLevels().getLevelTracker().setTime(player.getTime());
                    }
                    else
                    {
                        //check if personal info beat the previous
                        if (player.getTime() < getLevels().getLevelTracker().getTime())
                            getLevels().getLevelTracker().setTime(player.getTime());
                        if (player.getMoves() < getLevels().getLevelTracker().getMoves())
                            getLevels().getLevelTracker().setMoves(player.getMoves());
                    }

                    //mark completed
                    getLevels().getLevelTracker().setCompleted(true);

                    //save information to internal storage
                    updateStorage();

                    //set game over state
                    screen.setState(MainScreen.State.GameOver);

                    //set display message
                    screen.getScreenGameover().setMessage("Level Complete");

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
    }
    
    @Override
    public void dispose()
    {
        if (controller != null)
        {
            controller.dispose();
            controller = null;
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
        storage = null;
        start = null;
    }
    
    /**
     * Render game elements
     * @param canvas Where to write the pixel data
     * @throws Exception 
     */
    public void render(final Canvas canvas) throws Exception
    {
        if (getLevels() != null)
        {
            //render level/seletions
            getLevels().render(canvas, screen.getPaint());
            
            //make sure object exists
            if (getLevels().isSelected() && getPlayer() != null && getController() != null)
            {
                //if level was completed previously, display personal best
                if (getLevels().getLevelTracker().isCompleted())
                {
                    canvas.drawText("Best Move: " + getLevels().getLevelTracker().getMoves(), Player.PERSONAL_BEST_INFO_X, Player.PERSONAL_BEST_INFO_Y, screen.getPaint());
                    canvas.drawText("Best Time: " + PlayerHelper.getTimeDescription(getLevels().getLevelTracker().getTime()), Player.PERSONAL_BEST_INFO_X, Player.PERSONAL_BEST_INFO_Y * 2, screen.getPaint());
                }
                
                //render player
                getPlayer().render(canvas, screen.getPaint());
                
                //draw the game controller
                getController().render(canvas);
            }
        }
    }
}