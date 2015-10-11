package com.gamesbykevin.sokoban.game;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.SystemClock;
import android.view.MotionEvent;

import com.gamesbykevin.androidframework.io.storage.Internal;
import com.gamesbykevin.androidframework.resources.Audio;
import com.gamesbykevin.androidframework.resources.Disposable;

import com.gamesbykevin.sokoban.assets.Assets;
import com.gamesbykevin.sokoban.game.controller.Controller;
import com.gamesbykevin.sokoban.level.LevelHelper;
import com.gamesbykevin.sokoban.level.Levels;
import com.gamesbykevin.sokoban.level.tile.TileHelper;
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
    
    /**
     * Text delimeter used to parse internal storage data for each level
     */
    private static final String STORAGE_DELIMITER_LEVEL = ",";
    
    /**
     * Text delimeter used to parse internal storage data for each attribute in a level
     */
    private static final String STORAGE_DELIMITER_ATTRIBUTE = ";";
    
    public Game(final MainScreen screen) throws Exception
    {
        //our main screen object reference
        this.screen = screen;
        
        //create new paint object
        this.paint = new Paint();
        this.paint.setTextSize(24f);
        this.paint.setColor(Color.WHITE);
        
        //create new controller
        this.controller = new Controller(this);
        
        //create our storage object
        this.storage = new Internal("test" , screen.getPanel().getActivity());
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
    public void reset() throws Exception
    {
        //create new objects if not exist
        if (getLevels() == null)
        {
            //create new levels object
            levels = new Levels(Assets.TextKey.LevelsEasy);
            
            //load storage data into level trackers
            updateTrackers();
        }
        if (getPlayer() == null)
            player = new Player();
        
        //mark as level not selected
        getLevels().setSelected(false);
        
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
            //parse levels
            final String[] content = getStorage().getContent().toString().split(STORAGE_DELIMITER_LEVEL);
            
            //loop through each value
            for (String tmp : content)
            {
                //separate the attributes
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
                //update personal best info
                if (player.getTime() < getLevels().getLevelTracker(i).getTime())
                    getLevels().getLevelTracker(i).setTime(player.getTime());
                if (player.getMoves() < getLevels().getLevelTracker(i).getMoves())
                    getLevels().getLevelTracker(i).setMoves(player.getMoves());
                
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
                    //reset selected level
                    getLevels().reset();

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
                        //make sure the player is already at the destination before moving again
                        if (getPlayer().hasTarget())
                        {
                            //get the location
                            final int col = (int)LevelHelper.getCol(getLevels().getLevel(), x);
                            final int row = (int)LevelHelper.getRow(getLevels().getLevel(), y);

                            //if the location matches the player, mark selected
                            if (getPlayer().getCol() == col && getPlayer().getRow() == row)
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
                            //get the location
                            final int col = (int)LevelHelper.getCol(getLevels().getLevel(), x);
                            final int row = (int)LevelHelper.getRow(getLevels().getLevel(), y);

                            //diagonal movement is not allowed, as well if the destination is same as current location
                            if (getPlayer().getCol() != col && getPlayer().getRow() != row || getPlayer().getCol() == col && getPlayer().getRow() == row)
                            {
                                //flip flag
                                getPlayer().setSelected(!getPlayer().isSelected());
                            }
                            else
                            {
                                //assign the player destination
                                getPlayer().setTarget(col, row);

                                //calculate the targets
                                PlayerHelper.calculateTargets(getPlayer(), getLevels().getLevel());
                            }
                        }
                    }
                }
            }
        }
    }
    
    /**
     * Update game
     * @throws Exception 
     */
    public void update() throws Exception
    {
        //make sure object exists
        if (getLevels() != null)
        {
            //make sure we have a level selection before updating
            if (getLevels().isSelected())
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
                        
                        //mark completed
                        getLevels().getLevelTracker().setCompleted(true);
                        
                        //save information to internal storage
                        updateStorage();
                        
                        //set game over state
                        screen.setState(MainScreen.State.GameOver);

                        //set display message
                        screen.getScreenGameover().setMessage("Level Complete");

                        //play sound
                        //Audio.play(Assets.AudioKey.GameoverWin);
                        
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
    }
    
    @Override
    public void dispose()
    {
        if (controller != null)
        {
            controller.dispose();
            controller = null;
        }
        
        if (paint != null)
            paint = null;
        
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
                    canvas.drawText("PB: " + getLevels().getLevelTracker().getMoves(), Player.PERSONAL_BEST_INFO_X, Player.PERSONAL_BEST_INFO_Y, screen.getPaint());
                    canvas.drawText("PB: " + PlayerHelper.getTimeDescription(getLevels().getLevelTracker().getTime()), Player.PERSONAL_BEST_INFO_X, Player.PERSONAL_BEST_INFO_Y * 2, screen.getPaint());
                }
                
                //render player
                getPlayer().render(canvas, screen.getPaint());
                
                //draw the game controller
                getController().render(canvas);
            }
        }
    }
}