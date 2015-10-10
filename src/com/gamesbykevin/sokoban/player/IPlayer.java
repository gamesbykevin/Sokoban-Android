package com.gamesbykevin.sokoban.player;

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
}
