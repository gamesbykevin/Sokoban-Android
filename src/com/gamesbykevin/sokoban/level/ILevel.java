/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gamesbykevin.sokoban.level;

/**
 * Required methods for level
 * @author GOD
 */
public interface ILevel 
{
    /**
     * Load the level with the given line
     * @param line The line containing characters for the layout of our level
     * @throws Exception if problem loading the current line
     */
    public void load(final String line) throws Exception;
    
    /**
     * Logic to update level
     */
    public void update();
}