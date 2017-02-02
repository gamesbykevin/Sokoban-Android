package com.gamesbykevin.sokoban.level;

import android.graphics.Canvas;

import com.gamesbykevin.androidframework.base.Cell;
import com.gamesbykevin.androidframework.resources.Audio;
import com.gamesbykevin.androidframework.resources.Disposable;
import com.gamesbykevin.sokoban.assets.Assets;
import com.gamesbykevin.sokoban.level.tile.Block;

import com.gamesbykevin.sokoban.level.tile.Tile;
import com.gamesbykevin.sokoban.level.tile.TileHelper;
import com.gamesbykevin.sokoban.panel.GamePanel;
import com.gamesbykevin.sokoban.player.Player;
import com.gamesbykevin.sokoban.target.Target;
import com.gamesbykevin.sokoban.thread.MainThread;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Level containing the layout key
 * @author GOD
 */
public final class Level implements Disposable, ILevel
{
    //the key for this level
    private Tile.Type[][] key;
    
    //the character key values
    protected static final String KEY_WALL = "#";
    protected static final String KEY_PLAYER = "@";
    protected static final String KEY_PLAYER_ON_GOAL = "+";
    protected static final String KEY_BLOCK = "$";
    protected static final String KEY_BLOCK_ON_GOAL = "*";
    protected static final String KEY_GOAL = ".";
    protected static final String KEY_FLOOR = " ";
    
    //the player start location
    private Cell start;
    
    //the starting locations of the blocks
    private List<Cell> blocks;
    
    //the current location/destination of the blocks
    private List<Target> current;
    
    //the start coordinate
    private int startX = 0, startY = 0;
    
    //the dimensions of the level where we could render the entire level on the screen
    protected static final int SINGLE_SCREEN_MAX_COLS = 7;
    
    //the dimensions of the level where we could render the entire level on the screen
    protected static final int SINGLE_SCREEN_MAX_ROW = 12;
    
    /**
     * Create the level
     * @param tracker Object containing level info
     */
    protected Level(final LevelInfo tracker)
    {
        //create new key
        this.key = new Tile.Type[tracker.getRows()][tracker.getCols()];
        
        //create a new list for the blocks
        this.blocks = new ArrayList<Cell>();
        
        //create a new list for the current/destination
        this.current = new ArrayList<Target>();
    }
    
    /**
     * Can this entire level fit inside a single window?
     * @return true if the total (columns, rows) are within range, false otherwise
     */
    public boolean canFitWindow()
    {
        return (getKey().length <= SINGLE_SCREEN_MAX_ROW && getKey()[0].length <= SINGLE_SCREEN_MAX_COLS);
    }
    
    /**
     * Set the starting location of the first tile (0,0)
     * @param startX Starting x-coordinate
     * @param startY Starting y-coordinate
     */
    public void setStartLocation(final int startX, final int startY)
    {
        this.startX = startX;
        this.startY = startY;
    }
    
    public int getStartX()
    {
        return this.startX;
    }
    
    public int getStartY()
    {
        return this.startY;
    }
    
    /**
     * Get the key
     * @return The array representing the layout of the level
     */
    protected Tile.Type[][] getKey()
    {
        return this.key;
    }
    
    /**
     * Get the tile type at the specified location.
     * @param col Column
     * @param row Row
     * @return The tile type of the specified location, if out of bounds null will be returned
     */
    public Tile.Type getType(final int col, final int row)
    {
        //if out of range return null
        if (col < 0 || col >= getKey()[0].length)
            return null;
        if (row < 0 || row >= getKey().length)
            return null;
        
        //return the tile type
        return (getKey()[row][col]);
    }
    
    /**
     * Get the block that matches the specified (col, row)<br>
     * @param col Column
     * @param row Row
     * @return The block that has the matching location, if no match is found null is returned
     */
    public Target getBlock(final int col, final int row)
    {
        //check each block
        for (Target block : getCurrent())
        {
            //if location matches we have a block
            if (block.getCol() == col && block.getRow() == row)
                return block;
        }
        
        //there is no block here
        return null;
    }
    
    /**
     * Get the start position
     * @return The starting position for the player
     */
    public Cell getStart()
    {
        return this.start;
    }
    
    /**
     * Get the current location of the blocks
     * @return The current location/destination of each block
     */
    public List<Target> getCurrent()
    {
        return this.current;
    }
    
    /**
     * Get the list of starting locations for the blocks
     * @return The starting locations of all blocks in the level
     */
    public List<Cell> getBlocks()
    {
        return this.blocks;
    }
    
    /**
     * Do we have the destination?
     * @return true if all blocks are at their assigned place, false otherwise
     */
    public boolean hasDestination()
    {
        //check each block
        for (Target block : getCurrent())
        {
        	//if at least 1 block is not at the destination return false
            if (!block.hasDestination())
            	return false;
        }
    	
        //all blocks are found to be at their destination
        return true;
    }
    
    /**
     * Undo the previous move
     */
    public void undo()
    {
        //check each block
        for (Target block : getCurrent())
        {
        	block.undo();
        	
        	checkGoal(block);
        }
    }
    
    /**
     * Update the location of the blocks if not at their target
     */
    public void update()
    {
    	//velocity will depend if debugging
    	final double velocity = (MainThread.DEBUG) ? Player.VELOCITY_DEBUG : Player.VELOCITY; 
    	
        //check each block location
        for (Target block : getCurrent())
        {
            if (!block.hasDestination())
            {
                if (block.getCol() < block.getDestination().getCol())
                {
                    if (block.getCol() + velocity >= block.getDestination().getCol())
                    {
                        block.setCol(block.getDestination().getCol());
                    }
                    else
                    {
                        block.setCol(block.getCol() + velocity);
                    }
                }
                else if (block.getCol() > block.getDestination().getCol())
                {
                    if (block.getCol() - velocity <= block.getDestination().getCol())
                    {
                        block.setCol(block.getDestination().getCol());
                    }
                    else
                    {
                        block.setCol(block.getCol() - velocity);
                    }
                }
                else if (block.getRow() < block.getDestination().getRow())
                {
                    if (block.getRow() + velocity >= block.getDestination().getRow())
                    {
                        block.setRow(block.getDestination().getRow());
                    }
                    else
                    {
                        block.setRow(block.getRow() + velocity);
                    }
                }
                else if (block.getRow() > block.getDestination().getRow())
                {
                    if (block.getRow() - velocity <= block.getDestination().getRow())
                    {
                        block.setRow(block.getDestination().getRow());
                    }
                    else
                    {
                        block.setRow(block.getRow() - velocity);
                    }
                }
                
                //if the block is now at its destination, check if it is on a goal
                checkGoal(block);
            }
            else
            {
                //if the block is now at its destination, check if it is on a goal
                block.setGoal(TileHelper.isGoal(getType((int)block.getCol(), (int)block.getRow())));
            }
        }
    }
    
    private void checkGoal(final Target block)
    {
        //if the block is now at its destination, check if it is on a goal
        if (block.hasDestination())
        {
            if (TileHelper.isGoal(getType((int)block.getCol(), (int)block.getRow())))
            {
                //flag at goal
                block.setGoal(true);
                
                //play sound effect
                Audio.play(LevelHelper.hasCompleted(this) ? Assets.AudioGameKey.LevelComplete : Assets.AudioGameKey.Goal);
            }
            else
            {
                //this block is not on a goal
                block.setGoal(false);
            }
        }
    }
    
    @Override
    public void dispose()
    {
        if (key != null)
            key = null;
        if (start != null)
            start = null;
        
        if (blocks != null)
        {
            blocks.clear();
            blocks = null;
        }
        
        if (current != null)
        {
            for (Target target : current)
            {
                if (target != null)
                {
                    target.dispose();
                    target = null;
                }
            }
            
            current.clear();
            current = null;
        }
    }
    
    /**
     * Load the level based on the characters in the specified String line.<br>
     * When we check the rows in the level key to determine which row we populate this line to
     * @param line The given line containing characters that will determine the level
     * @throws Exception
     */
    public void load(final String line) throws Exception
    {
        for (int row = 0; row < getKey().length; row++)
        {
            //if this row was already loaded, skip it
            if (!hasEmptyRow(row))
                continue;
            
            boolean begin = false;
            
            //check each character
            for (int index = 0; index < line.length(); index++)
            {
                //check the current character
                String character = line.substring(index, index + 1);
                
                if (character.equals(KEY_WALL))
                {
                    //we hit wall so flag start
                    begin = true;
                    
                    //assign key
                    getKey()[row][index] = Tile.Type.Wall;
                }
                else if (character.equals(KEY_PLAYER))
                {
                    //store start location for player
                    start = new Cell(index, row);
                    
                    //assign key
                    getKey()[row][index] = Tile.Type.Floor;
                }
                else if (character.equals(KEY_PLAYER_ON_GOAL))
                {
                    //store start location
                    start = new Cell(index, row);
                    
                    //assign key
                    getKey()[row][index] = Tile.Type.Goal;
                }
                else if (character.equals(KEY_BLOCK))
                {
                    //store block location
                    blocks.add(new Cell(index, row));
                    
                    //add the current location of this block
                    current.add(new Target(index, row));
                    
                    //assign key
                    getKey()[row][index] = Tile.Type.Floor;
                }
                else if (character.equals(KEY_BLOCK_ON_GOAL))
                {
                    //store block location
                    blocks.add(new Cell(index, row));
                    
                    //add the current location of this block
                    current.add(new Target(index, row));
                    
                    //assign key
                    getKey()[row][index] = Tile.Type.Goal;
                }
                else if (character.equals(KEY_GOAL))
                {
                    //assign key
                    getKey()[row][index] = Tile.Type.Goal;
                }
                else if (character.equals(KEY_FLOOR))
                {
                    //there can't be a floor on the first or last row
                    if (row == 0 || row == getKey().length - 1)
                        continue;
                    
                    //if we already found a wall, then this will be a floor
                    if (begin)
                    {
                        //assign key
                        getKey()[row][index] = Tile.Type.Floor;
                    }
                }
            }
            
            //now that line has loaded, no need to continue
            break;
        }
    }
    
    /**
     * Is this an empty row
     * @param row The specified row to check
     * @return true if all objects are null in the specified row, otherwise false
     */
    private boolean hasEmptyRow(final int row)
    {
        for (int col = 0; col < key[row].length; col++)
        {
            //objet exists return false
            if (key[row][col] != null)
                return false;
        }
        
        //yes all objects are null
        return true;
    }
    
    /**
     * Render the level
     * @param canvas Object used to write pixel data
     * @param tiles List of tiles to render our level
     * @throws Exception 
     */
    public void render(final Canvas canvas, final HashMap<Tile.Type, Tile> tiles) throws Exception
    {
        for (int row = 0; row < getKey().length; row++)
        {
            for (int col = 0; col < getKey()[0].length; col++)
            {
                final int x = (int)LevelHelper.getX(this, col);
                final int y = (int)LevelHelper.getY(this, row);
                
                //skip if null
                if (getKey()[row][col] == null)
                    continue;
                
                //render floor everywhere
                Tile tile = tiles.get(Tile.Type.Floor);
                
                //assign coordinates
                tile.setX(x);
                tile.setY(y);
                
              //make sure that we aren't rendering items that aren't on the screen
                if (tile.getX() < -tile.getWidth() || tile.getX() > GamePanel.WIDTH)
                	continue;
                if (tile.getY() < -tile.getHeight() || tile.getY() > GamePanel.HEIGHT)
                	continue;
                
                //render the floor
                tile.render(canvas);
                
                //get the assigned tile by its type
                tile = tiles.get(getKey()[row][col]);
                
                //render tile accordingly
                switch (tile.getType())
                {
                    case Wall:
                    case Goal:
                    case Block:
                	default:
                        tile.setX(x + (TileHelper.DEFAULT_DIMENSION / 2) - (tile.getWidth() / 2));
                        tile.setY(y + (TileHelper.DEFAULT_DIMENSION / 2) - (tile.getHeight() / 2));
                        tile.render(canvas);
                        break;
                }
            }
        }
        
        //get the block tile
        Tile tile = tiles.get(Tile.Type.Block);
        
        //render blocks for each location
        for (Target cell : getCurrent())
        {
            //get the (x,y) location
            final int x = (int)LevelHelper.getX(this, cell.getCol());
            final int y = (int)LevelHelper.getY(this, cell.getRow());
            
            //assign the location
            tile.setX(x + (TileHelper.DEFAULT_DIMENSION / 2) - (tile.getWidth() / 2));
            tile.setY(y + (TileHelper.DEFAULT_DIMENSION / 2) - (tile.getHeight() / 2));
            
            //make sure that we aren't rendering items that aren't on the screen
            if (tile.getX() < -tile.getWidth() || tile.getX() > GamePanel.WIDTH)
            	continue;
            if (tile.getY() < -tile.getHeight() || tile.getY() > GamePanel.HEIGHT)
            	continue;
            
            //default block animation
            tile.getSpritesheet().setKey(cell.hasGoal() ? Block.State.Alternate : Block.State.Default);
            
            //render the tile
            tile.render(canvas);
        }
    }
}