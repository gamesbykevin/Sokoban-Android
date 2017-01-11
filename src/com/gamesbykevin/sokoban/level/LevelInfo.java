package com.gamesbykevin.sokoban.level;

/**
 * This class will maintain the information of a level.<br>
 * It will be used to help load a level from a given text file
 * @author GOD
 */
public class LevelInfo 
{
    //the start and end line of a given level
    private final int start, end, cols, rows;
    
    //the text description of the level
    private final String levelDescription;
    
    public LevelInfo(final int start, final int end, final int cols, final String levelDescription)
    {
        this.start = start;
        this.end = end;
        this.cols = cols;
        this.rows = end - start + 1;
        this.levelDescription = levelDescription;
    }
    
    /**
     * The start line number
     * @return The line # in the text file where the level starts
     */
    public int getLineStart()
    {
        return this.start;
    }
    
    /**
     * The end line number
     * @return The line # in the text file where the level ends
     */
    public int getLineEnd()
    {
        return this.end;
    }
    
    /**
     * Get the columns
     * @return The number of columns for this level
     */
    public int getCols()
    {
        return this.cols;
    }
    
    /**
     * Get the rows 
     * @return The number of rows for this level
     */
    public int getRows()
    {
        return this.rows;
    }
    
    /**
     * Get the level description
     * @return The level description text we want to display to the user
     */
    public String getLevelDescription()
    {
    	return this.levelDescription;
    }
}