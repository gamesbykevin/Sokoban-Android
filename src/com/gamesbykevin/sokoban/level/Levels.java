package com.gamesbykevin.sokoban.level;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.gamesbykevin.androidframework.awt.Button;
import com.gamesbykevin.androidframework.io.storage.Internal;
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
    private final static int COLS = 5;
    private final static int ROWS = 7;
    
    //did the user choose a level to start?
    private boolean selected = false;
    
    //the buttons
    private Button levelIconIncomplete, levelIconComplete, levelPrevious, levelNext;
    
    //the location of the first level select icon
    private static final int LEVEL_SELECT_START_X = 16;
    private static final int LEVEL_SELECT_START_Y = 32;
    
    //the location where we display text when seleting level
    private static final int LEVEL_START_TEXT_X = 176;
    private static final int LEVEL_START_TEXT_Y = 736;
    
    //text to select the level
    private static final String LEVEL_START_TEXT = "Select Level";
    
    //the current page number for the level select
    private int page = 0;
    
    //the level description to display
    private String levelDesc = "";
    
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
        
        //create new list of tiles
        this.tiles = new HashMap<Tile.Type, Tile>();
        
        //create new list of levels
        this.trackers = new ArrayList<Tracker>();
        
        //create buttons
        this.levelIconIncomplete = new Button(Images.getImage(Assets.ImageKey.LevelIconIncomplete));
        this.levelIconComplete = new Button(Images.getImage(Assets.ImageKey.LevelIconComplete));
        
        //next page button
        this.levelNext = new Button(Images.getImage(Assets.ImageKey.LevelNext));
        this.levelNext.setX(getLevelIconX(COLS - 1));
        this.levelNext.setY(getLevelIconY(ROWS));
        this.levelNext.updateBounds();
        
        //previous page button
        this.levelPrevious = new Button(Images.getImage(Assets.ImageKey.LevelPrevious));
        this.levelPrevious.setX(getLevelIconX(0));
        this.levelPrevious.setY(getLevelIconY(ROWS));
        this.levelPrevious.updateBounds();
        
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
                        this.trackers.add(new Tracker(lineStart, i, length));
                    }
                    else
                    {
                        //add the info for this level
                        this.trackers.add(new Tracker(lineStart, i - 1, length));
                    }
                    
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
        
        //if at first page, we won't show previous button
        levelPrevious.setVisible(getPage() != 0);
        
        //if at last page, we won't show next button
        levelNext.setVisible(getPage() != pageMax);
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
     * @throws Exception
     */
    public void setSelected(final float x, final float y) throws Exception
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
                levelIconIncomplete.setX(getLevelIconX(col));
                levelIconIncomplete.setY(getLevelIconY(row));
                
                //update the boundary
                levelIconIncomplete.updateBounds();
                
                //if the user clicked here, we will se the level
                if (levelIconIncomplete.getBounds().contains((int)x, (int)y))
                {
                    //mark level selected
                    setSelected(true);
                    
                    //set the selection as well
                    setIndex(i);
                    
                    //reset the new level
                    reset();
                    
                    //no need to continue
                    return;
                }
                
                //increase the index count
                i++;
            }
        }
        
        //change page #'s if the buttons were hit
        if (levelNext.isVisible() && levelNext.getBounds().contains((int)x, (int)y))
            setPage(getPage() + 1);
        if (levelPrevious.isVisible() && levelPrevious.getBounds().contains((int)x, (int)y))
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
    public Tracker getLevelTracker()
    {
        return getLevelTracker(getIndex());
    }
    
    /**
     * Get the level tracker of the current level
     * @param index The index location of the desired level
     * @return Object containing level info
     */
    public Tracker getLevelTracker(final int index)
    {
        return trackers.get(index);
    }
    
    /**
     * Get the level trackers
     * @return The level trackers of all levels
     */
    public List<Tracker> getLevelTrackers()
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
        
        if (levelIconIncomplete != null)
        {
            levelIconIncomplete.dispose();
            levelIconIncomplete = null;
        }
        
        if (levelIconComplete != null)
        {
            levelIconComplete.dispose();
            levelIconComplete = null;
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
     * Set the level difficulty description
     * @param levelDesc The desired text to be displayed when rendering the level select screen
     */
    public void setLevelDesc(final String levelDesc)
    {
        this.levelDesc = levelDesc;
    }
    
    /**
     * Get the level difficulty description
     * @return The description to be displayed when rendering the level select screen
     */
    public String getLevelDesc()
    {
        return this.levelDesc;
    }
    
    private int getLevelIconX(final int col)
    {
        return LEVEL_SELECT_START_X + (int)((levelIconIncomplete.getWidth() * 1.5) * col);
    }
    
    private int getLevelIconY(final int row)
    {
        return LEVEL_SELECT_START_Y + (int)((levelIconIncomplete.getHeight() * 1.5) * row);
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
            if (getLevel() != null)
            {
                //render the level
                getLevel().render(canvas, tiles);
            }
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

                    //check if a level has already been completed
                    if (!trackers.get(i).isCompleted())
                    {
                        //assign the current location
                        levelIconIncomplete.setX(getLevelIconX(col));
                        levelIconIncomplete.setY(getLevelIconY(row));

                        //set the # text
                        levelIconIncomplete.setText((i + 1) + "");

                        //center text in middle
                        levelIconIncomplete.positionText(paint);

                        //render button
                        levelIconIncomplete.render(canvas, paint);
                    }
                    else
                    {
                        //assign the current location
                        levelIconComplete.setX(getLevelIconX(col));
                        levelIconComplete.setY(getLevelIconY(row));

                        //set the # text
                        levelIconComplete.setText((i + 1) + "");

                        //center text in middle
                        levelIconComplete.positionText(paint);

                        //render button
                        levelIconComplete.render(canvas, paint);
                    }
                    
                    //increase index
                    i++;
                }
            }
            
            //render the next level buttons
            this.levelNext.render(canvas);
            this.levelPrevious.render(canvas);
            
            //draw custom text
            canvas.drawText(LEVEL_START_TEXT, LEVEL_START_TEXT_X, LEVEL_START_TEXT_Y, paint);
            
            //draw the difficulty
            canvas.drawText(getLevelDesc(), LEVEL_START_TEXT_X, LEVEL_START_TEXT_Y + 25, paint);
        }
    }
}