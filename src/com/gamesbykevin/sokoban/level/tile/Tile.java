package com.gamesbykevin.sokoban.level.tile;

import com.gamesbykevin.androidframework.base.Entity;

/**
 * A single tile in the game
 * @author GOD
 */
public abstract class Tile extends Entity
{
    /**
     * The different types of tiles in our game
     */
    public enum Type
    {
        Floor,
        Wall,
        Block,
        Goal
    }

    //the type of tile
    private final Type type;
    
    /**
     * Create a new tile
     * @param type The type of tile
     */
    public Tile(final Type type)
    {
        this.type = type;
    }
    
    /**
     * Get the type
     * @return The type of tile this is "Floor, Wall, Goal, Block, etc..."
     */
    public Type getType()
    {
        return this.type;
    }
    
    /**
     * Is this tile a wall?
     * @return true = yes, false = no
     */
    public boolean isWall()
    {
        switch (getType())
        {
            case Wall:
                return true;
                
            default:
                return false;
        }
    }
    
    /**
     * Is this tile the floor?
     * @return true = yes, false = no
     */
    public boolean isFloor()
    {
        switch (getType())
        {
            case Floor:
                return true;
                
            default:
                return false;
        }
    }
    
    /**
     * Is this tile a block?
     * @return true = yes, false = no
     */
    public boolean isBlock()
    {
        switch (getType())
        {
            case Block:
                return true;
                
            default:
                return false;
        }
    }
    
    /**
     * Is this tile the goal?
     * @return true = yes, false = no
     */
    public boolean isGoal()
    {
        switch (getType())
        {
            case Goal:
                return true;
                
            default:
                return false;
        }
    }
}