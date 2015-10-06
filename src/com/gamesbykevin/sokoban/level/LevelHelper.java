package com.gamesbykevin.sokoban.level;

import com.gamesbykevin.androidframework.anim.Animation;
import com.gamesbykevin.androidframework.resources.Images;
import com.gamesbykevin.sokoban.assets.Assets;

import com.gamesbykevin.sokoban.level.tile.*;
import com.gamesbykevin.sokoban.panel.GamePanel;

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
     * @throws Exception
     */
    public static void createTiles(final HashMap<Tile.Type, Tile> tiles) throws Exception
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
                styles.add(Block.Style.Black);
                styles.add(Block.Style.Gray);
                styles.add(Block.Style.Brown);
                break;
                
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
                
            default:
                throw new Exception("Random number not handled here - " + random);
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
                
            case Gray:
                tiles.put(Tile.Type.Goal, new Goal(Goal.Style.Gray));
                break;
                
            default:
                throw new Exception("Block style not handled here " + styles.get(index));
        }
        
        //add block to hashmap
        tiles.put(Tile.Type.Block, new Block(styles.get(index)));
    }
    
    public static final double getX(final Level level, final double col)
    {
        return level.getStartX() + (col * TileHelper.DEFAULT_DIMENSION);
    }
    
    public static final double getY(final Level level, final double row)
    {
        return level.getStartY() + (row * TileHelper.DEFAULT_DIMENSION);
    }
}