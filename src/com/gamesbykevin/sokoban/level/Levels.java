package com.gamesbykevin.sokoban.level;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.gamesbykevin.androidframework.awt.Button;
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
    
    //track where all the levels are
    private List<Tracker> trackers;
    
    //the index of the current level we are playing
    private int index = 0;
    
    //store the unique key of the text file used to create the levels
    private final Assets.TextKey key;
    
    //the dimensions of the level select buttons
    private final static int COLS = 3;
    private final static int ROWS = 5;
    
    //did the user choose a level to start?
    private boolean selected = false;
    
    //the buttons
    private Button levelIcon, levelPrevious, levelNext;
    
    //the location of the first level select icon
    private static final int LEVEL_SELECT_START_X = 64;
    private static final int LEVEL_SELECT_START_Y = 64;
    
    //the current page number for the level select
    private int page = 0;
    
    /**
     * Create levels
     * @param key The unique key of the desired text file
     * @throws Exception 
     */
    public Levels(final Assets.TextKey key) throws Exception
    {
        //store text key
        this.key = key;
        
        //create new list of tiles
        this.tiles = new HashMap<Tile.Type, Tile>();
        
        //create new list of levels
        this.trackers = new ArrayList<Tracker>();
        
        //create buttons
        this.levelIcon = new Button(Images.getImage(Assets.ImageKey.LevelIcon));
        this.levelNext = new Button(Images.getImage(Assets.ImageKey.LevelNext));
        this.levelPrevious = new Button(Images.getImage(Assets.ImageKey.LevelPrevious));
        
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
            
            //each line in a level will have a wall somewhere
            if (line.contains(Level.KEY_WALL))
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
                    //add the info for this level
                    this.trackers.add(new Tracker(lineStart, i - 1, length));
                    
                    //unflag start
                    start = false;
                    
                    //reset column size as well
                    length = 0;
                }
            }
        }
    }
    
    /**
     * Get the page
     * @return The page number
     */
    public int getPage()
    {
        return this.page;
    }
    
    /**
     * Assign the page
     * @param page The desired page #
     */
    public void setPage(final int page)
    {
        this.page = page;
        
        //calculate the total number of pages
        final int pageMax = trackers.size() / (COLS * ROWS);
        
        //make sure page doesn't exceed
        if (getPage() > pageMax)
            setPage(0);
        if (getPage() < 0)
            setPage(pageMax);
    }
    
    /**
     * Has a level been selected?
     * @return true=yes, false=no
     */
    public boolean isSelected()
    {
        return this.selected;
    }
    
    /**
     * Flag selected.<br>
     * This determines if the user selected a level
     * @param selected true=yes, false=no
     */
    public void setSelected(final boolean selected)
    {
        this.selected = selected;
    }
    
    /**
     * Set the selected level at the specified (x,y) coordinates.<br>
     * @param x x-coordinate
     * @param y y-coordinate
     */
    public void setSelected(final float x, final float y)
    {
        //set the start index based on the page
        int i = (COLS * ROWS) * page;
        
        //render the level selections
        for (int row = 0; row < ROWS; row++)
        {
            for (int col = 0; col < COLS; col++)
            {
                //exit if at the end
                if (i >= trackers.size())
                    break;
                
                //assign the current location
                levelIcon.setX(LEVEL_SELECT_START_X + (levelIcon.getWidth() * col));
                levelIcon.setY(LEVEL_SELECT_START_X + (levelIcon.getHeight() * row));
                
                //update the boundary
                levelIcon.updateBounds();
                
                //if the user clicked here, we will se the level
                if (levelIcon.getBounds().contains((int)x, (int)y))
                {
                    //mark level selected
                    setSelected(true);
                    
                    //set the selection as well
                    setIndex(i);
                    
                    //no need to continue
                    return;
                }
                
                //increase the index count
                i++;
            }
        }
        
        //level change buttons
        levelNext.setX(LEVEL_SELECT_START_X + (levelNext.getWidth() * (COLS - 1)));
        levelNext.setY(LEVEL_SELECT_START_Y + (levelNext.getHeight() * (ROWS + 2)));
        levelNext.updateBounds();
        levelPrevious.setX(LEVEL_SELECT_START_X + (levelPrevious.getWidth() * 0));
        levelPrevious.setY(LEVEL_SELECT_START_Y + (levelPrevious.getHeight() * (ROWS + 2)));
        levelPrevious.updateBounds();
        
        //change page #'s if the buttons were hit
        if (levelNext.getBounds().contains((int)x, (int)y))
            setPage(getPage() + 1);
        if (levelPrevious.getBounds().contains((int)x, (int)y))
            setPage(getPage() - 1);
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
     * Assign the index.<br>
     * The index will auto-adjust in case the specified value is out of bounds
     * @param index The desired location of the level we want.
     */
    public void setIndex(final int index)
    {
        this.index = index;
        
        //keep the index in range
        if (getIndex() < 0)
            setIndex(trackers.size() - 1);
        if (getIndex() >= trackers.size())
            setIndex(0);
    }
    
    /**
     * Get the index
     * @return The location of the current level
     */
    public int getIndex()
    {
        return this.index;
    }
    
    /**
     * Get the level tracker of the current level
     * @return Object containing level info
     */
    private Tracker getLevelTracker()
    {
        return trackers.get(getIndex());
    }
    
    /**
     * Create a new level
     * @throws Exception 
     */
    public void createLevel() throws Exception
    {
        //create new level
        this.level = new Level(getLevelTracker());
        
        //get the level tracker info
        final int lineStart = trackers.get(getIndex()).getLineStart();
        final int lineEnd = trackers.get(getIndex()).getLineEnd();
        final int cols = trackers.get(getIndex()).getCols();
        final int rows = trackers.get(getIndex()).getRows();
        
        //now load each line into the level
        for (int i = lineStart; i <= lineEnd; i++)
        {
            this.level.load(Files.getText(getKey()).getLines().get(i));
        }
        
        //if the level is small enough, we will display all on sreen
        if (level.canFitWindow())
        {
            //locate middle of screen
            final int middleX = (GamePanel.WIDTH / 2) - (TileHelper.DEFAULT_DIMENSION / 2) + (TileHelper.DEFAULT_DIMENSION / 2);
            final int middleY = (GamePanel.HEIGHT / 2) - (TileHelper.DEFAULT_DIMENSION / 2) + (TileHelper.DEFAULT_DIMENSION / 2);
            
            //set the start location (x,y) so the complete level is displayed
            this.level.setStartLocation(
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
            this.level.setStartLocation(
                middleX - (int)(this.level.getStart().getCol() * TileHelper.DEFAULT_DIMENSION), 
                middleY - (int)(this.level.getStart().getRow() * TileHelper.DEFAULT_DIMENSION)
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
        
        if (levelIcon != null)
        {
            levelIcon.dispose();
            levelIcon = null;
        }
        
        if (levelPrevious != null)
        {
            levelPrevious.dispose();
            levelPrevious = null;
        }
        
        if (levelNext != null)
        {
            levelNext.dispose();
            levelNext = null;
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
        if (isSelected())
        {
            //render the level
            getLevel().render(canvas, tiles);
        }
        else
        {
            //render the level selections
            int i = (COLS * ROWS) * page;

            //render the level selections
            for (int row = 0; row < ROWS; row++)
            {
                for (int col = 0; col < COLS; col++)
                {
                    //exit if at the end
                    if (i >= trackers.size())
                        break;

                    //assign the current location
                    levelIcon.setX(LEVEL_SELECT_START_X + (levelIcon.getWidth() * col));
                    levelIcon.setY(LEVEL_SELECT_START_X + (levelIcon.getHeight() * row));
                    
                    //set the # text
                    levelIcon.setText((i + 1) + "");
                    
                    //center text in middle
                    levelIcon.positionText(paint);
                    
                    //render button
                    levelIcon.render(canvas, paint);
                    
                    //increase index
                    i++;
                }
            }
            
            //render the next level buttons
            this.levelNext.render(canvas);
            this.levelPrevious.render(canvas);
        }
    }
}