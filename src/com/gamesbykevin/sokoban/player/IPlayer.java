package com.gamesbykevin.sokoban.player;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.gamesbykevin.androidframework.resources.Disposable;
import com.gamesbykevin.sokoban.level.Level;

/**
 * Player interface
 * @author GOD
 */
public interface IPlayer extends Disposable
{
    /**
     * Logic to reset player
     * @param level The current level
     */
    public void reset(final Level level);
    
    /**
     * Update the player
     * @param level The current level
     */
    public void update(final Level level);
    
    /**
     * Render the player
     * @param canvas Object used to write pixel data
     * @param paint Object containing font metrics
     * @throws Exception
     */
    public void render(final Canvas canvas, final Paint paint) throws Exception;
}
