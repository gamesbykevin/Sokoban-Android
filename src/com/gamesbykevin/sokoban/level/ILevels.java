package com.gamesbykevin.sokoban.level;

import com.gamesbykevin.androidframework.resources.Disposable;

import android.graphics.Canvas;

/**
 * Required methods for a level
 * @author GOD
 */
public interface ILevels extends Disposable
{
    /**
     * Logic to reset the level
     * @throws Exception
     */
    public void reset() throws Exception;
    
    /**
     * Render the level
     * @param canvas Object where we write pixel data
     * @throws Exception 
     */
    public void render(final Canvas canvas) throws Exception;
}