package com.gamesbykevin.sokoban.game;

import com.gamesbykevin.androidframework.resources.Disposable;

/**
 * Game interface methods
 * @author GOD
 */
public interface IGame extends Disposable
{
    /**
     * Logic to restart the game with the same settings
     * @throws Exception
     */
    public void reset() throws Exception;
}
