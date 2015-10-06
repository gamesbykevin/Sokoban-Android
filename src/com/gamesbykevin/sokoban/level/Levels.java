package com.gamesbykevin.sokoban.level;

import android.graphics.Canvas;

import com.gamesbykevin.androidframework.base.Cell;
import com.gamesbykevin.androidframework.resources.Files;

import com.gamesbykevin.sokoban.assets.Assets;
import com.gamesbykevin.sokoban.level.tile.Tile;
import com.gamesbykevin.sokoban.level.tile.TileHelper;
import com.gamesbykevin.sokoban.panel.GamePanel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Manage a collection of levels
 * @author GOD
 */
public final class Levels implements ILevels
{
    //the tiles for the level
    private HashMap<Tile.Type, Tile> tiles;
    
    //the current level
    private Level level;
    
    //the line in the text file to start checking for the next level
    private int nextLevelLine = 0;
    
    /**
     * Create levels
     * @throws Exception 
     */
    public Levels() throws Exception
    {
        //create new list of tiles
        this.tiles = new HashMap<Tile.Type, Tile>();
    }
    
    /**
     * Create the next level
     * @throws Exception 
     */
    public void createLevel() throws Exception
    {
        //did we find the first line
        boolean start = false;
        
        //start and finish of level
        int lineStart = 0;
        int lineEnd = 0;
        
        //longest length (width)
        int length = 0;
        
        //determine where the level is, so it can be loaded
        for (int i = nextLevelLine; i < Files.getText(Assets.TextKey.Levels).getLines().size(); i++)
        {
            //get the current line
            String line = Files.getText(Assets.TextKey.Levels).getLines().get(i);
            
            //each line in a level will have a wall somewhere
            if (line.contains(Level.KEY_WALL))
            {
                //store the longest width
                if (line.length() > length)
                    length = line.length();
                
                //if we didn't start, this is the first line
                if (!start)
                    lineStart = i;
                
                //flag level start
                start = true;
            }
            else
            {
                //if we already started, we reached the end of the level
                if (start)
                {
                    lineEnd = i - 1;
                    break;
                }
            }
        }
        
        //count number of rows
        final int rows = lineEnd - lineStart + 1;
        
        //locate middle of screen
        final int middleX = GamePanel.WIDTH / 2;
        final int middleY = GamePanel.HEIGHT / 2;
        
        //locate start position
        final int startX = middleX - ((length * TileHelper.DEFAULT_DIMENSION) / 2);
        final int startY = middleY - ((rows * TileHelper.DEFAULT_DIMENSION) / 2);
        
        //create new level
        this.level = new Level(length, rows, startX, startY);
        
        //now load each line into the level
        for (int i = lineStart; i <= lineEnd; i++)
        {
            this.level.load(Files.getText(Assets.TextKey.Levels).getLines().get(i));
        }
        
        //assign the location where the next level is
        this.nextLevelLine = lineEnd + 1;        
    }
    
    /**
     * Recycle resources
     */
    @Override
    public void dispose()
    {
        if (tiles != null)
        {
            for (Tile tile : tiles.values())
            {
                if (tile != null)
                {
                    tile.dispose();
                    tile = null;
                }
            }
            
            tiles.clear();
            tiles = null;
        }
        
        if (level != null)
        {
            level.dispose();
            level = null;
        }
    }
    
    /**
     * Get the level
     * @return Get the current level
     */
    public Level getLevel()
    {
        return this.level;
    }
    
    /**
     * Reset the level
     * @throws Exception 
     */
    @Override
    public final void reset() throws Exception
    {
        //generate random tiles
        LevelHelper.createTiles(tiles);
        
        //reset to the first level
        this.nextLevelLine = 0;
        
        //create new level
        createLevel();
    }
    
    /**
     * Render the level
     * @param canvas Object where we write pixel data
     * @throws Exception 
     */
    @Override
    public void render(final Canvas canvas) throws Exception
    {
        for (int row = 0; row < getLevel().getKey().length; row++)
        {
            for (int col = 0; col < getLevel().getKey()[0].length; col++)
            {
                final int x = (int)LevelHelper.getX(getLevel(), col);
                final int y = (int)LevelHelper.getY(getLevel(), row);
                
                //skip if null
                if (getLevel().getKey()[row][col] == null)
                    continue;
                
                //render floor everywhere
                Tile tile = tiles.get(Tile.Type.Floor);
                tile.setX(x);
                tile.setY(y);
                tile.render(canvas);
                
                //get the assigned tile by its type
                tile = tiles.get(getLevel().getKey()[row][col]);
                
                //render tile accordingly
                switch (tile.getType())
                {
                    case Wall:
                    case Goal:
                    case Block:
                        tile.setX(x + (TileHelper.DEFAULT_DIMENSION / 2) - (tile.getWidth() / 2));
                        tile.setY(y + (TileHelper.DEFAULT_DIMENSION / 2) - (tile.getHeight() / 2));
                        tile.render(canvas);
                        break;
                }
            }
        }
        
        //get the block tile
        Tile tile = tiles.get(Tile.Type.Block);
        
        //render at each location
        for (Cell cell : getLevel().getCurrent())
        {
            final int x = (int)LevelHelper.getX(getLevel(), cell.getCol());
            final int y = (int)LevelHelper.getY(getLevel(), cell.getRow());
            
            tile.setX(x + (TileHelper.DEFAULT_DIMENSION / 2) - (tile.getWidth() / 2));
            tile.setY(y + (TileHelper.DEFAULT_DIMENSION / 2) - (tile.getHeight() / 2));
            tile.render(canvas);
        }
    }
}