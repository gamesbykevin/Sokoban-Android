package com.gamesbykevin.sokoban.game.controller;

import android.graphics.Canvas;
import com.gamesbykevin.androidframework.resources.Disposable;

/**
 * Each controller needs to have these methods
 * @author GOD
 */
public interface IController extends Disposable
{
	public void reset();
	
    /**
     * Update logic when motion event occurs
     * @param event Motion Event
     * @param x x-coordinate
     * @param y y-coordinate
     * @throws Exception
     * @return true if motion event was applied, false otherwise
     */
    public boolean update(final int action, final float x, final float y) throws Exception;
    
    /**
     * Common logic to update the controller
     */
    public void update() throws Exception;
    
    /**
     * Render our controller
     * @param canvas Object to write pixels to
     * @throws Exception
     */
    public void render(final Canvas canvas) throws Exception;
}
