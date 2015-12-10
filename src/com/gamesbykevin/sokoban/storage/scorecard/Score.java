package com.gamesbykevin.sokoban.storage.scorecard;

/**
 * The score for a level
 * @author GOD
 */
public final class Score 
{
    //the level this score is for
    private final int level;
    
    //the number of moves
    private int moves;
    
    //the time duration
    private long time;
    
    protected Score(final int level, final int moves, final long time)
    {
    	//assign default values
    	this.level = level;
    	this.moves = moves;
    	this.time = time;
    }
    
    /**
     * Get the level index
     * @return The level this score is for
     */
    public int getLevel()
    {
        return this.level;
    }
    
    /**
     * Get the moves
     * @return The number of moves for this score
     */
    public int getMoves()
    {
        return this.moves;
    }
    
    /**
     * Get the time duration
     * @return The duration for this score (milliseconds)
     */
    public long getTime()
    {
    	return this.time;
    }
    
    /**
     * Set the number of moves
     * @param moves The number of moves to complete the level
     */
    protected void setMoves(final int moves)
    {
    	this.moves = moves;
    }
    
    /**
     * Set the time
     * @param time The duration to complete the level
     */
    protected void setTime(final long time)
    {
    	this.time = time;
    }
}