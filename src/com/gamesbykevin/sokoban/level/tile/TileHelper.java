package com.gamesbykevin.sokoban.level.tile;

/**
 * TileHelper methods
 * @author GOD
 */
public class TileHelper 
{
    /**
     * The default dimension of a tile
     */
    public static final int DEFAULT_DIMENSION = 64;
    
    /**
     * The default dimension of the goal
     */
    public static final int GOAL_DIMENSION = 32;
    
    /**
     * Get the sprite sheet coordinate
     * @param col Column
     * @return The x coordinate where the animation resides on the sprite sheet
     */
    public static int getSpriteSheetX(final int col)
    {
        return (col * DEFAULT_DIMENSION);
    }
    
    /**
     * Get the sprite sheet coordinate
     * @param row Row
     * @return The y coordinate where the animation resides on the sprite sheet
     */
    public static int getSpriteSheetY(final int row)
    {
        return (row * DEFAULT_DIMENSION);
    }
}