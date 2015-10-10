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
}