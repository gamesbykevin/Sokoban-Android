package com.gamesbykevin.sokoban.player;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.gamesbykevin.androidframework.anim.Animation;
import com.gamesbykevin.androidframework.base.Cell;
import com.gamesbykevin.androidframework.base.Entity;
import com.gamesbykevin.androidframework.resources.Images;

import com.gamesbykevin.sokoban.assets.Assets;
import com.gamesbykevin.sokoban.level.Level;
import com.gamesbykevin.sokoban.level.LevelHelper;
import com.gamesbykevin.sokoban.level.tile.TileHelper;
import com.gamesbykevin.sokoban.panel.GamePanel;

/**
 * The player that moves the blocks
 * @author GOD
 */
public class Player extends Entity implements IPlayer
{
    //the target for the player when it is moved
    private Cell target;
    
    //did the user select the player
    private boolean selected = false;
    
    //speed to move
    public static final double VELOCITY = 0.1;
    
    //the number of moves the player has made
    public int moves = 0;
    
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
    
    //track the time (milliseconds)
    private long totalTime = 0L;
    
    //track the previous time (milliseconds)
    private long previousTime = 0L;
    
    //do we stop the timer
    private boolean stopTimer = true;
    
    /**
     * Location where player stat's are rendered
     */
    public static final int INFO_X = 5;
    
    /**
     * Location where player stats are rendered
     */
    public static final int INFO_Y = 25;
    
    /**
     * Location where a player's personal best stats are rendered
     */
    public static final int PERSONAL_BEST_INFO_X = 165;
    
    /**
     * Location where a player's personal best stats are rendered
     */
    public static final int PERSONAL_BEST_INFO_Y = 25;
    
    /**
     * Create a new player
     */
    public Player()
    {
        //create new destination
        this.target = new Cell();
        
        //delay between each frame
        final int delay = 125;
        
        //sprite key
        final Assets.ImageGameKey key = Assets.ImageGameKey.Sprites;
        
        //our animation object
        Animation animation;
        
        //walk east animation
        animation = new Animation(Images.getImage(key), 320, 128, 42, 58, 2, 1, 2);
        animation.setDelay(delay);
        animation.setLoop(true);
        getSpritesheet().add(Key.WalkEast, animation);
        
        //walk west animation
        animation = new Animation(Images.getImage(key), 320, 187, 42, 58, 2, 1, 2);
        animation.setDelay(delay);
        animation.setLoop(true);
        getSpritesheet().add(Key.WalkWest, animation);
        
        //walk north animation
        animation = new Animation(Images.getImage(key), 320, 305, 37, 60, 2, 1, 2);
        animation.setDelay(delay);
        animation.setLoop(true);
        getSpritesheet().add(Key.WalkNorth, animation);
        
        //walk south animation
        animation = new Animation(Images.getImage(key), 320, 245, 37, 59, 2, 1, 2);
        animation.setDelay(delay);
        animation.setLoop(true);
        getSpritesheet().add(Key.WalkSouth, animation);
        
        //idle facing east animation
        animation = new Animation(Images.getImage(key), 320, 128, 42, 58);
        getSpritesheet().add(Key.IdleEast, animation);
        
        //idle facing west animation
        animation = new Animation(Images.getImage(key), 320, 187, 42, 58);
        getSpritesheet().add(Key.IdleWest, animation);
        
        //idle facing north animation
        animation = new Animation(Images.getImage(key), 384, 0, 37, 60);
        getSpritesheet().add(Key.IdleNorth, animation);
        
        //idle facing south animation
        animation = new Animation(Images.getImage(key), 384, 65, 37, 59);
        getSpritesheet().add(Key.IdleSouth, animation);
        
        //default animation
        setAnimation(Key.IdleSouth);
    }
    
    /**
     * Stop the timer
     */
    public void stopTimer()
    {
        this.stopTimer = true;
    }
    
    @Override
    public void reset(final Level level)
    {
        //default animation
        setAnimation(Key.IdleSouth);
        
        //player is not selected
        setSelected(false);
        
        //set start location
        setCol(level.getStart());
        setRow(level.getStart());
        
        //set target
        setTarget(getCol(), getRow());
        
        //reset location
        updateXY(level);
        
        //reset moves back to 0
        setMoves(0);
        
        //reset timer
        stopTimer();
        this.totalTime = 0;
        
    }
    
    /**
     * Get the time
     * @return The total time elapsed
     */
    public long getTime()
    {
        return this.totalTime;
    }
    
    /**
     * Get the moves count
     * @return The total number of moves
     */
    public int getMoves()
    {
        return this.moves;
    }
    
    /**
     * Set the moves
     * @param moves The moves count
     */
    public void setMoves(final int moves)
    {
        this.moves = moves;
    }
    
    /**
     * Is the player selected
     * @return true=yes, false=no
     */
    public boolean isSelected()
    {
        return this.selected;
    }
    
    /**
     * Flag the player as selected
     * @param selected true=yes, false=no
     */
    public void setSelected(final boolean selected)
    {
        this.selected = selected;
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
     * Get the animation
     * @return The animation of the current key, if not set null is returned
     */
    public final Key getAnimation()
    {
        if (getSpritesheet().getKey() == null)
            return null;
        
        //return result
        return (Key)getSpritesheet().getKey();
    }
    
    /**
     * Do we have the target?
     * @return true if the player location matches the target location, false otherwise
     */
    public boolean hasTarget()
    {
        return (getCol() == getTarget().getCol() && getRow() == getTarget().getRow());
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
    
    /**
     * Update the (x,y) location for the player
     * @param level The level we are interacting with
     */
    public void updateXY(final Level level)
    {
        //get start destination
        final double x = LevelHelper.getX(level, getCol());
        final double y = LevelHelper.getY(level, getRow());

        //place in the center
        setX(x + (TileHelper.DEFAULT_DIMENSION / 2) - (getWidth() / 2));
        setY(y + (TileHelper.DEFAULT_DIMENSION / 2) - (getHeight() / 2));
    }
    
    /**
     * Update the player
     * @param level The current level
     */
    public void update(final Level level)
    {
        //if we stopped the timer, record the previous time
        if (this.stopTimer)
        {
            this.stopTimer = false;
            this.previousTime = System.currentTimeMillis();
        }
        
        //get the current time
        final long current = System.currentTimeMillis();
        
        //add the difference to the total time
        this.totalTime += (current - previousTime);
        
        //update the previous
        this.previousTime = current;
        
        if (!hasTarget())
        {
            //update the current animation
            getSpritesheet().get().update();

            //assign appropriate walking animation
            PlayerHelper.startWalking(this);
            
            //else the player can move, so update velocity and (col, row) since we can move
            PlayerHelper.manageVelocity(this);
            
            //if we made it to the target
            if (hasTarget())
            {
                //player is no longer selected
                setSelected(false);
                
                //stop the walking animation
                PlayerHelper.stopWalking(this);
            }
            
            if (level.canFitWindow())
            {
                //update (x,y) render coordinates
                updateXY(level);
            }
            else
            {
                //locate middle of screen
                final int middleX = (GamePanel.WIDTH / 2) - (TileHelper.DEFAULT_DIMENSION / 2);
                final int middleY = (GamePanel.HEIGHT / 2) - (TileHelper.DEFAULT_DIMENSION / 2);
                
                //set the start location (x,y) relative to where the player start is
                level.setStartLocation(
                middleX - (int)(getCol() * TileHelper.DEFAULT_DIMENSION), 
                middleY - (int)(getRow() * TileHelper.DEFAULT_DIMENSION));
                
                //update (x,y) render coordinates
                updateXY(level);
            }
        }
    }
    
    @Override
    public void dispose()
    {
        super.dispose();
        
        target = null;
    }
    
    @Override
    public void render(final Canvas canvas, final Paint paint) throws Exception
    {
        //render player animation
        super.render(canvas);
        
        //render current move count
        canvas.drawText("" + getMoves(), INFO_X, INFO_Y, paint);
        
        //draw timer
        canvas.drawText(PlayerHelper.getTimeDescription(totalTime), INFO_X, INFO_Y * 2, paint);
    }
}