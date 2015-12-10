package com.gamesbykevin.sokoban.level;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.gamesbykevin.androidframework.awt.Button;
import com.gamesbykevin.androidframework.level.Select;
import com.gamesbykevin.androidframework.resources.Files;
import com.gamesbykevin.androidframework.resources.Images;
import com.gamesbykevin.sokoban.assets.Assets;
import com.gamesbykevin.sokoban.level.tile.Tile;
import com.gamesbykevin.sokoban.level.tile.TileHelper;
import com.gamesbykevin.sokoban.panel.GamePanel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Manage a collection of levels
 * @author GOD
 */
public final class Levels implements ILevels
{
    //the tiles for the level
    private HashMap<Tile.Type, Tile> tiles;
    
    //the current level
    private Level level;
    
    //store the unique key of the text file used to create the levels
    private final Assets.TextKey key;
    
    //keep the list where all the levels are located in the text file
    private List<LevelInfo> trackers;
    
    //our level select object
    private Select levelSelect;
    
    //level select information
    private static final int LEVEL_SELECT_COLS = 5;
    private static final int LEVEL_SELECT_ROWS = 7;
    private static final int LEVEL_SELECT_DIMENSION = 64;
    private static final int LEVEL_SELECT_PADDING = (LEVEL_SELECT_DIMENSION / 2);
    private static final int LEVEL_SELECT_START_X = (GamePanel.WIDTH / 2) - (((LEVEL_SELECT_COLS * LEVEL_SELECT_DIMENSION) + ((LEVEL_SELECT_COLS - 1) * LEVEL_SELECT_PADDING)) / 2);
    private static final int LEVEL_SELECT_START_Y = 32;
    
    /**
     * Create levels
     * @param key The unique key of the desired text file
     * @throws Exception 
     */
    public Levels(final Assets.TextKey key) throws Exception
    {
        //store text key
        this.key = key;
        
        //current level is null
        this.level = null;
        
        //create new list to track where each level is in a given text file
        this.trackers = new ArrayList<LevelInfo>();
        
        //load the location of all levels in the specified text file
        loadLevels();
        
        //create the level select object
        createLevelSelect();
        
        //create new list of tiles
        this.tiles = new HashMap<Tile.Type, Tile>();
    }
    
    /**
     * Create the level select object
     */
    private void createLevelSelect()
    {
        //create the level select screen
        this.levelSelect = new Select();
        getLevelSelect().setButtonNext(new Button(Images.getImage(Assets.ImageGameKey.LevelNext)));
        getLevelSelect().setButtonOpen(new Button(Images.getImage(Assets.ImageGameKey.LevelIconIncomplete)));
        getLevelSelect().setButtonPrevious(new Button(Images.getImage(Assets.ImageGameKey.LevelPrevious)));
        getLevelSelect().setButtonSolved(new Button(Images.getImage(Assets.ImageGameKey.LevelIconComplete)));
        getLevelSelect().setCols(LEVEL_SELECT_COLS);
        getLevelSelect().setRows(LEVEL_SELECT_ROWS);
        getLevelSelect().setDimension(LEVEL_SELECT_DIMENSION);
        getLevelSelect().setPadding(LEVEL_SELECT_PADDING);
        getLevelSelect().setStartX(LEVEL_SELECT_START_X);
        getLevelSelect().setStartY(LEVEL_SELECT_START_Y);
        getLevelSelect().setTotal(getLevelTrackers().size());
    }
    
    /**
     * Load the line # for each level so we will know how to load a specified level 
     */
    private void loadLevels()
    {
        //did we find the first line
        boolean start = false;
        
        //start and finish of level
        int lineStart = 0;
        
        //longest length (width)
        int length = 0;
        
        //check each line in our text file to determine where the levels are
        for (int i = 0; i < Files.getText(getKey()).getLines().size(); i++)
        {
            //get the current line
            String line = Files.getText(getKey()).getLines().get(i);
            
            //each line in a level will have a wall somewhere, and make sure not at last line
            if (line.contains(Level.KEY_WALL) && i < Files.getText(getKey()).getLines().size() - 1)
            {
                //store the column width
                if (line.length() > length)
                    length = line.length();
                
                //if we didn't start, this is the first line of the level
                if (!start)
                    lineStart = i;
                
                //flag level start
                start = true;
            }
            else
            {
                //if we already started, we reached the end of the level
                if (start)
                {
                    if (i == Files.getText(getKey()).getLines().size() - 1)
                    {
                        //add the info for this level
                    	getLevelTrackers().add(new LevelInfo(lineStart, i, length));
                    }
                    else
                    {
                        //add the info for this level
                    	getLevelTrackers().add(new LevelInfo(lineStart, i - 1, length));
                    }
                    
                    //flag start false
                    start = false;
                    
                    //reset column size as well
                    length = 0;
                }
            }
        }
    }
    
    /**
     * Get the level select
     * @return The level select object
     */
    public Select getLevelSelect()
    {
    	return this.levelSelect;
    }
    
    /**
     * Get the unique text resource key
     * @return The unique key of the text resource used to create the levels 
     */
    private Assets.TextKey getKey()
    {
        return this.key;
    }
    
    /**
     * Get the level tracker of the current level
     * @return Object containing level info
     */
    public LevelInfo getLevelTracker()
    {
        return getLevelTracker(getLevelSelect().getLevelIndex());
    }
    
    /**
     * Get the level tracker of the current level
     * @param index The index location of the desired level
     * @return Object containing level info
     */
    public LevelInfo getLevelTracker(final int index)
    {
        return trackers.get(index);
    }
    
    /**
     * Get the level trackers
     * @return The level trackers of all levels
     */
    public List<LevelInfo> getLevelTrackers()
    {
        return this.trackers;
    }
    
    /**
     * Create a new level with the current level tracker
     * @throws Exception 
     */
    public void createLevel() throws Exception
    {
        //create new level
        this.level = new Level(getLevelTracker());
        
        //get the size from the level tracker info
        final int cols = getLevelTracker().getCols();
        final int rows = getLevelTracker().getRows();
        
        //now load each line into the level
        for (int i = getLevelTracker().getLineStart(); i <= getLevelTracker().getLineEnd(); i++)
        {
        	getLevel().load(Files.getText(getKey()).getLines().get(i));
        }
        
        //if the level is small enough, we will display all on screen
        if (getLevel().canFitWindow())
        {
            //locate middle of screen
            final int middleX = (GamePanel.WIDTH / 2) - (TileHelper.DEFAULT_DIMENSION / 2) + (TileHelper.DEFAULT_DIMENSION / 2);
            final int middleY = (GamePanel.HEIGHT / 2) - (TileHelper.DEFAULT_DIMENSION / 2) + (TileHelper.DEFAULT_DIMENSION / 2);
            
            //set the start location (x,y) so the complete level is displayed
            getLevel().setStartLocation(
                middleX - (int)((cols * TileHelper.DEFAULT_DIMENSION) / 2), 
                middleY - (int)((rows * TileHelper.DEFAULT_DIMENSION) / 2)
            );
        }
        else
        {
            //locate middle of screen
            final int middleX = (GamePanel.WIDTH / 2) - (TileHelper.DEFAULT_DIMENSION / 2);
            final int middleY = (GamePanel.HEIGHT / 2) - (TileHelper.DEFAULT_DIMENSION / 2);
            
            //set the start location (x,y) relative to where the player start is
            getLevel().setStartLocation(
                middleX - (int)(getLevel().getStart().getCol() * TileHelper.DEFAULT_DIMENSION), 
                middleY - (int)(getLevel().getStart().getRow() * TileHelper.DEFAULT_DIMENSION)
            );
        }
    }
    
    /**
     * Recycle resources
     */
    @Override
    public void dispose()
    {
        if (tiles != null)
        {
            for (Tile tile : tiles.values())
            {
                if (tile != null)
                {
                    tile.dispose();
                    tile = null;
                }
            }
            
            tiles.clear();
            tiles = null;
        }
        
        if (level != null)
        {
            level.dispose();
            level = null;
        }
        
        if (trackers != null)
        {
            trackers.clear();
            trackers = null;
        }
        
        if (levelSelect != null)
        {
        	levelSelect.dispose();
        	levelSelect = null;
        }
    }
    
    /**
     * Get the current level
     * @return Get the current level
     */
    public Level getLevel()
    {
        return this.level;
    }
    
    /**
     * Reset the level.<br>
     * A new style of tiles will be selected.<br>
     * A new level will be created.
     * @throws Exception 
     */
    @Override
    public final void reset() throws Exception
    {
        //generate random tiles
        LevelHelper.createTiles(tiles);
        
        //create new level
        createLevel();
    }
    
    /**
     * Render the level
     * @param canvas Object where we write pixel data
     * @param paint Paint object
     * @throws Exception 
     */
    @Override
    public void render(final Canvas canvas, final Paint paint) throws Exception
    {
    	//if a level has already been selected, render the level
    	if (getLevelSelect().hasSelection())
    	{
            if (getLevel() != null)
            {
                //render the level
                getLevel().render(canvas, tiles);
            }
    	}
        else
        {
        	//draw the level select screen
        	getLevelSelect().render(canvas, paint);
        }
    }
}