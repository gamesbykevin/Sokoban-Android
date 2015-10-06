package com.gamesbykevin.sokoban.game.controller;

import android.graphics.Canvas;
import android.view.MotionEvent;

import com.gamesbykevin.androidframework.resources.Disposable;

/**
 * Each controller needs to have these methods
 * @author GOD
 */
public interface IController extends Disposable
{
    /**
     * Update logic when motion event occurs
     * @param event Motion Event
     * @param x x-coordinate
     * @param y y-coordinate
     * @return true if motion event was applied, false otherwise
     */
    public boolean updateMotionEvent(final MotionEvent event, final float x, final float y);
    
    /**
     * Render our controller
     * @param canvas Object to write pixels to
     * @throws Exception
     */
    public void render(final Canvas canvas) throws Exception;
}
