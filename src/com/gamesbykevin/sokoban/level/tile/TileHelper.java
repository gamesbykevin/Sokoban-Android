package com.gamesbykevin.sokoban.level.tile;

/**
 * TileHelper methods
 * @author GOD
 */
public class TileHelper 
{
    /**
     * The default dimension of a single animation for a tile
     */
    public static final int ANIMATION_DIMENSION = 64;
    
    /**
     * The default dimension of a tile
     */
    public static final int DEFAULT_DIMENSION = 64;
    
    /**
     * The default dimension of the goal
     */
    public static final int ANIMATION_GOAL_DIMENSION = 32;
    
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
    
    
    /**
     * Is this tile a wall?
     * @param tile The tile we want to check
     * @return true = yes, false = no
     */
    public static boolean isWall(final Tile tile)
    {
        return isWall(tile.getType());
    }
    
    /**
     * Is this tile a wall?
     * @param type The tile type we want to check
     * @return true = yes, false = no
     */
    public static boolean isWall(final Tile.Type type)
    {
        //if type is null, return false
        if (type == null)
            return false;
        
        switch (type)
        {
            case Wall:
                return true;
                
            default:
                return false;
        }
    }
    
    /**
     * Is this tile the floor?
     * @param tile The tile we want to check
     * @return true = yes, false = no
     */
    public static boolean isFloor(final Tile tile)
    {
        return isFloor(tile.getType());
    }
    
    /**
     * Is this tile the floor?
     * @param type The tile type we want to check
     * @return true = yes, false = no
     */
    public static boolean isFloor(final Tile.Type type)
    {
        //if type is null, return false
        if (type == null)
            return false;
        
        switch (type)
        {
            case Floor:
                return true;
                
            default:
                return false;
        }
    }
    
    
    /**
     * Is this tile a block?
     * @param tile The tile we want to check
     * @return true = yes, false = no
     */
    public static boolean isBlock(final Tile tile)
    {
        return isBlock(tile.getType());
    }
    
    /**
     * Is this tile a block?
     * @param type The tile type we want to check
     * @return true = yes, false = no
     */
    public static boolean isBlock(final Tile.Type type)
    {
        //if type is null, return false
        if (type == null)
            return false;
        
        switch (type)
        {
            case Block:
                return true;
                
            default:
                return false;
        }
    }
    
    /**
     * Is this tile the goal?
     * @param tile The tile we want to check
     * @return true = yes, false = no
     */
    public static boolean isGoal(final Tile tile)
    {
        return isGoal(tile.getType());
    }
    
    /**
     * Is this tile the goal?
     * @param type The tile type we want to check
     * @return true = yes, false = no
     */
    public static boolean isGoal(final Tile.Type type)
    {
        //if type is null, return false
        if (type == null)
            return false;
        
        switch (type)
        {
            case Goal:
                return true;
                
            default:
                return false;
        }
    }
}