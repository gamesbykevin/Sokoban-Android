package com.gamesbykevin.sokoban.level;

/**
 * This class will track the start and end line for a level.<br>
 * We will also store the # of turns, as well as time
 * @author GOD
 */
public class Tracker 
{
    //the start and end line of a given level
    private final int start, end, cols, rows;
    
    public Tracker(final int start, final int end, final int cols)
    {
        this.start = start;
        this.end = end;
        this.cols = cols;
        this.rows = end - start + 1;
    }
    
    public int getLineStart()
    {
        return this.start;
    }
    
    public int getLineEnd()
    {
        return this.end;
    }
    
    public int getCols()
    {
        return this.cols;
    }
    
    public int getRows()
    {
        return this.rows;
    }
}