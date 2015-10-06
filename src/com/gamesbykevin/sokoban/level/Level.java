package com.gamesbykevin.sokoban.level;

import com.gamesbykevin.androidframework.base.Cell;
import com.gamesbykevin.androidframework.resources.Disposable;

import com.gamesbykevin.sokoban.level.tile.Tile;
import com.gamesbykevin.sokoban.target.Target;

import java.util.ArrayList;
import java.util.List;

/**
 * Level containing the layout key
 * @author GOD
 */
public final class Level implements Disposable, ILevel
{
    //the key for this level
    private Tile.Type[][] key;
    
    //the character values
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
    private final int startX, startY;
    
    /**
     * Create the level
     * @param cols Total column size of level
     * @param rows Total row size of level
     * @param startX Start x-coordinate of (0,0)
     * @param startY Start y-coordinate of (0,0)
     */
    protected Level(final int cols, final int rows, final int startX, final int startY)
    {
        //create new key
        this.key = new Tile.Type[rows][cols];
        
        //create a new list for the blocks
        this.blocks = new ArrayList<Cell>();
        
        //create a new list for the current/destination
        this.current = new ArrayList<Target>();
        
        //store starting coordinate
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
    public Tile.Type[][] getKey()
    {
        return this.key;
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
     * Get the current list of blocks
     * @return The current location/destination of each block
     */
    public List<Target> getCurrent()
    {
        return this.current;
    }
    
    /**
     * Get the blocks
     * @return The starting locations of all blocks in the level
     */
    public List<Cell> getBlocks()
    {
        return this.blocks;
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
     * Load the level based on the characters in the specified line.<br>
     * When we check the rows in the level key to determine which row we populate this line to
     * @param line The given line containing characters
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
                    //store start location
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
}