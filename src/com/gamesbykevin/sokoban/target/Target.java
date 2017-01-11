package com.gamesbykevin.sokoban.target;

import com.gamesbykevin.androidframework.base.Cell;
import com.gamesbykevin.androidframework.resources.Disposable;

/**
 * The target class contains the current location and a destination<br>
 * Used to move the blocks and the player
 * @author GOD
 */
public class Target extends Cell implements Disposable
{
    //destination location
    private Cell destination;
    
    //is this target at a goal
    private boolean goal = false;
    
    //store the previous location in case we want to undo
    private double previousCol, previousRow;
    
    /**
     * Create a new Target with the specified location
     * @param col Column
     * @param row Row
     */
    public Target(final double col, final double row)
    {
        super(col, row);
        
        //assign current location as the destination
        this.destination = new Cell(col, row);
        
        //assign the previous location
        this.previousCol = col;
        this.previousRow = row;
    }
    
    @Override
    public void dispose()
    {
        this.destination = null;
    }
    
    /**
     * Flag this target located at a goal
     * @param goal true = yes, false = no
     */
    public void setGoal(final boolean goal)
    {
        this.goal = goal;
    }
    
    /**
     * Is this target at a goal?
     * @return true = yes, false = no
     */
    public boolean hasGoal()
    {
        return this.goal;
    }
    
    /**
     * Reset the location to the previous
     */
    public void undo()
    {
    	setCol(this.previousCol);
    	setRow(this.previousRow);
    	
    	setDestination(getCol(), getRow());
    }
    
    /**
     * Assign the destination, as well as the current location
     * @param col Column of our destination
     * @param row Row of our destination
     */
    public void setDestination(final double col, final double row)
    {
    	//assign current in case of undo
    	this.previousCol = getCol();
    	this.previousRow = getRow();
    	    	
        //assign destination location
        this.destination.setCol(col);
        this.destination.setRow(row);
    }
    
    /**
     * Get the destination
     * @return The location of the destination
     */
    public Cell getDestination()
    {
        return this.destination;
    }
    
    /**
     * Do we have the destination?
     * @return true if the current location matches the target location, false otherwise
     */
    public boolean hasDestination()
    {
        return (getCol() == getDestination().getCol() && getRow() == getDestination().getRow());
    }
}