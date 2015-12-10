package com.gamesbykevin.sokoban.game;

import com.gamesbykevin.androidframework.resources.Disposable;
import com.gamesbykevin.sokoban.assets.Assets;

import android.graphics.Canvas;

/**
 * Game interface methods
 * @author GOD
 */
public interface IGame extends Disposable
{
    /**
     * Logic to restart the game with the same settings
     * @param key Unique key of text file we want to load
     * @throws Exception
     */
    public void reset(final Assets.TextKey key) throws Exception;
    
    /**
     * Logic to render the game
     * @param canvas
     * @throws Exception
     */
    public void render(final Canvas canvas) throws Exception;
}
