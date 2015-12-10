package com.gamesbykevin.sokoban.level;

import com.gamesbykevin.sokoban.level.tile.*;
import com.gamesbykevin.sokoban.panel.GamePanel;
import com.gamesbykevin.sokoban.target.Target;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;

/**
 * Level Helper methods
 * @author GOD
 */
public class LevelHelper 
{
    private static final int WALL_COLOR_GRAY = 0;
    private static final int WALL_COLOR_BROWN = 1;
    private static final int WALL_COLOR_BLACK = 2;
    private static final int WALL_COLOR_BEIGE = 3;
    
    /**
     * Create the tiles and select animations
     * @param tiles The list of tiles for our level
     */
    public static void createTiles(final HashMap<Tile.Type, Tile> tiles)
    {
        //create new list of possible block styles
        List<Block.Style> styles = new ArrayList<Block.Style>();
        
        //pick random wall
        final int random = GamePanel.RANDOM.nextInt(4);
        
        //handle the animation accordingly
        switch (random)
        {
            case WALL_COLOR_GRAY:
                
                //create wall
                tiles.put(Tile.Type.Wall, new Wall(Wall.Style.Gray));
                
                //create floor
                tiles.put(Tile.Type.Floor, new Floor(GamePanel.RANDOM.nextBoolean() ? Floor.Style.Gray1 : Floor.Style.Gray2));
                
                //add possible styles
                styles.add(Block.Style.Red);
                styles.add(Block.Style.Yellow);
                styles.add(Block.Style.Blue);
                styles.add(Block.Style.Purple);
                styles.add(Block.Style.Black);
                styles.add(Block.Style.Brown);
                break;
                
            case WALL_COLOR_BROWN:
                
                //create wall
                tiles.put(Tile.Type.Wall, new Wall(Wall.Style.Brown));
                
                //create floor
                tiles.put(Tile.Type.Floor, new Floor(GamePanel.RANDOM.nextBoolean() ? Floor.Style.Brown1 : Floor.Style.Brown2));
                
                //add possible styles
                styles.add(Block.Style.Red);
                styles.add(Block.Style.Yellow);
                styles.add(Block.Style.Blue);
                styles.add(Block.Style.Purple);
                styles.add(Block.Style.Gray);
                styles.add(Block.Style.Black);
                break;
                
            case WALL_COLOR_BLACK:
                
                //create wall
                tiles.put(Tile.Type.Wall, new Wall(Wall.Style.Black));
                
                //create floor
                tiles.put(Tile.Type.Floor, new Floor(GamePanel.RANDOM.nextBoolean() ? Floor.Style.Green1 : Floor.Style.Green2));
                
                //add possible styles
                styles.add(Block.Style.Red);
                styles.add(Block.Style.Yellow);
                styles.add(Block.Style.Blue);
                styles.add(Block.Style.Purple);
                styles.add(Block.Style.Beige);
                styles.add(Block.Style.Gray);
                styles.add(Block.Style.Brown);
                break;
                
            default:
            case WALL_COLOR_BEIGE:
                
                //create wall
                tiles.put(Tile.Type.Wall, new Wall(Wall.Style.Beige));
                
                //create floor
                tiles.put(Tile.Type.Floor, new Floor(GamePanel.RANDOM.nextBoolean() ? Floor.Style.Beige1 : Floor.Style.Beige2));
                
                //add possible styles
                styles.add(Block.Style.Red);
                styles.add(Block.Style.Yellow);
                styles.add(Block.Style.Blue);
                styles.add(Block.Style.Purple);
                styles.add(Block.Style.Brown);
                styles.add(Block.Style.Black);
                break;
        }
        
        //pick random index
        final int index = GamePanel.RANDOM.nextInt(styles.size());
        
        //assign the goal color based on the random chosen block color
        switch (styles.get(index))
        {
            case Red:
                tiles.put(Tile.Type.Goal, new Goal(Goal.Style.Red));
                break;
                
            case Yellow:
                tiles.put(Tile.Type.Goal, new Goal(Goal.Style.Yellow));
                break;
                
            case Blue:
                tiles.put(Tile.Type.Goal, new Goal(Goal.Style.Blue));
                break;
                
            case Purple:
                tiles.put(Tile.Type.Goal, new Goal(Goal.Style.Purple));
                break;
                
            case Brown:
                tiles.put(Tile.Type.Goal, new Goal(Goal.Style.Brown));
                break;
                
            case Black:
                tiles.put(Tile.Type.Goal, new Goal(Goal.Style.Black));
                break;
                
            case Beige:
                tiles.put(Tile.Type.Goal, new Goal(Goal.Style.Beige));
                break;
     
            default:
            case Gray:
                tiles.put(Tile.Type.Goal, new Goal(Goal.Style.Gray));
                break;
        }
        
        //add block to hash map
        tiles.put(Tile.Type.Block, new Block(styles.get(index)));
    }
    
    /**
     * Get the x-coordinate of the specified column
     * @param level Current level of play
     * @param col column location
     * @return coordinate of specified location
     */
    public static final double getX(final Level level, final double col)
    {
        return level.getStartX() + (col * TileHelper.DEFAULT_DIMENSION);
    }
    
    /**
     * Get the y-coordinate of the specified row
     * @param level Current level of play
     * @param row row location
     * @return coordinate of specified location
     */
    public static final double getY(final Level level, final double row)
    {
        return level.getStartY() + (row * TileHelper.DEFAULT_DIMENSION);
    }
    
    /**
     * Get the column
     * @param level Current level of game play
     * @param x x-coordinate
     * @return The columns of the specified coordinate
     */
    public static final double getCol(final Level level, final double x)
    {
        if (x < level.getStartX())
            return -1;
        
        return ((x - level.getStartX()) / TileHelper.DEFAULT_DIMENSION);
    }
    
    /**
     * Get the row
     * @param level Current level of game play
     * @param y y-coordinate
     * @return The row of the specified coordinate
     */
    public static final double getRow(final Level level, final double y)
    {
        if (y < level.getStartY())
            return -1;
        
        return ((y - level.getStartY()) / TileHelper.DEFAULT_DIMENSION);
    }
    
    /**
     * Has the level been completed?
     * @param level The current level we want to check
     * @return true if all blocks are at a goal, false otherwise
     */
    public static final boolean hasCompleted(final Level level)
    {
        //check all targets
        for (Target target : level.getCurrent())
        {
            //if 1 target is not at the goal, the level is not complete
            if (!target.hasGoal())
                return false;
        }
        
        //all targets are at a goal, return true
        return true;
    }
}