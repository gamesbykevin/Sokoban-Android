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
    
    //has the level been completed
    private boolean completed = false;
    
    //number of moves
    private int moves = 0;
    
    //time elapsed
    private long time;
    
    public Tracker(final int start, final int end, final int cols)
    {
        this.start = start;
        this.end = end;
        this.cols = cols;
        this.rows = end - start + 1;
    }
    
    /**
     * 
     * @param completed 
     */
    public void setCompleted(final boolean completed)
    {
        this.completed = completed;
    }
    
    /**
     * 
     * @return 
     */
    public boolean isCompleted()
    {
        return this.completed;
    }
    
    /**
     * 
     * @param moves 
     */
    public void setMoves(final int moves)
    {
        this.moves = moves;
    }
    
    /**
     * 
     * @return 
     */
    public int getMoves()
    {
        return this.moves;
    }
    
    /**
     * 
     * @param time 
     */
    public void setTime(final long time)
    {
        this.time = time;
    }
    
    /**
     * 
     * @return 
     */
    public long getTime()
    {
        return this.time;
    }
    
    /**
     * 
     * @return 
     */
    public int getLineStart()
    {
        return this.start;
    }
    
    /**
     * 
     * @return 
     */
    public int getLineEnd()
    {
        return this.end;
    }
    
    /**
     * 
     * @return 
     */
    public int getCols()
    {
        return this.cols;
    }
    
    /**
     * 
     * @return 
     */
    public int getRows()
    {
        return this.rows;
    }
}