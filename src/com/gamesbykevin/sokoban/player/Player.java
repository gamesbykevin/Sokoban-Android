package com.gamesbykevin.sokoban.player;

import android.graphics.Canvas;

import com.gamesbykevin.androidframework.anim.Animation;
import com.gamesbykevin.androidframework.base.Cell;
import com.gamesbykevin.androidframework.base.Entity;
import com.gamesbykevin.androidframework.resources.Disposable;
import com.gamesbykevin.androidframework.resources.Images;
import com.gamesbykevin.sokoban.assets.Assets;

/**
 * The player that moves the blocks
 * @author GOD
 */
public class Player extends Entity implements Disposable
{
    //the target for the player
    private Cell target;
    
    /**
     * The different animations for the player
     */
    public enum Key
    {
        WalkEast,
        WalkWest,
        WalkNorth,
        WalkSouth,
        IdleEast,
        IdleWest,
        IdleNorth,
        IdleSouth
    }
    
    /**
     * Create a new player
     */
    public Player()
    {
        //create new destination
        this.target = new Cell();
        
        //delay between each frame
        final int delay = 250;
        
        //our animation object
        Animation animation;
        
        //walk east animation
        animation = new Animation(Images.getImage(Assets.ImageKey.Sprites), 320, 120, 42, 59, 2, 1, 2);
        animation.setDelay(delay);
        animation.setLoop(true);
        getSpritesheet().add(Key.WalkEast, animation);
        
        //walk west animation
        animation = new Animation(Images.getImage(Assets.ImageKey.Sprites), 320, 186, 42, 59, 2, 1, 2);
        animation.setDelay(delay);
        animation.setLoop(true);
        getSpritesheet().add(Key.WalkWest, animation);
        
        //walk north animation
        animation = new Animation(Images.getImage(Assets.ImageKey.Sprites), 320, 305, 37, 60, 2, 1, 2);
        animation.setDelay(delay);
        animation.setLoop(true);
        getSpritesheet().add(Key.WalkNorth, animation);
        
        //walk south animation
        animation = new Animation(Images.getImage(Assets.ImageKey.Sprites), 320, 245, 37, 59, 2, 1, 2);
        animation.setDelay(delay);
        animation.setLoop(true);
        getSpritesheet().add(Key.WalkSouth, animation);
        
        //idle facing east animation
        animation = new Animation(Images.getImage(Assets.ImageKey.Sprites), 320, 128, 42, 59);
        getSpritesheet().add(Key.IdleEast, animation);
        
        //idle facing west animation
        animation = new Animation(Images.getImage(Assets.ImageKey.Sprites), 320, 186, 42, 59);
        getSpritesheet().add(Key.IdleWest, animation);
        
        //idle facing north animation
        animation = new Animation(Images.getImage(Assets.ImageKey.Sprites), 384, 0, 37, 60);
        getSpritesheet().add(Key.IdleNorth, animation);
        
        //idle facing south animation
        animation = new Animation(Images.getImage(Assets.ImageKey.Sprites), 384, 65, 37, 59);
        getSpritesheet().add(Key.IdleSouth, animation);
        
        //default animation
        setAnimation(Key.IdleSouth);
    }
    
    /**
     * Assign the animation
     * @param key The key of the animation we want
     */
    public final void setAnimation(final Key key)
    {
        //assign animation
        getSpritesheet().setKey(key);
        
        //assign the dimensions based on the image dimensions
        super.setWidth(getSpritesheet().get().getImage().getWidth());
        super.setHeight(getSpritesheet().get().getImage().getHeight());
    }
    
    /**
     * Get the destination
     * @return The location where the player is headed
     */
    public Cell getTarget()
    {
        return this.target;
    }
    
    /**
     * Set the destination
     * @param col Column
     * @param row Row
     */
    public void setTarget(final double col, final double row)
    {
        this.target.setCol(col);
        this.target.setRow(row);
    }
}