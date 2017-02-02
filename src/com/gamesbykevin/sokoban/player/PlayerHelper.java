package com.gamesbykevin.sokoban.player;

import com.gamesbykevin.sokoban.level.Level;
import com.gamesbykevin.sokoban.level.tile.TileHelper;
import com.gamesbykevin.sokoban.target.Target;
import com.gamesbykevin.sokoban.thread.MainThread;

/**
 * Player helper methods
 * @author GOD
 */
public class PlayerHelper 
{
    /**
     * Assign the specified player the correct walking animation
     * @param player The player we want to assign animation
     */
    public static void startWalking(final Player player)
    {
        if (player.getCol() != player.getTarget().getCol())
        {
            //if heading east
            if (player.getCol() < player.getTarget().getCol())
            {
                player.setAnimation(Player.Key.WalkEast);
            }
            else
            {
                player.setAnimation(Player.Key.WalkWest);
            }
        }
        else if (player.getRow() != player.getTarget().getRow())
        {
            //if heading south
            if (player.getRow() < player.getTarget().getRow())
            {
                player.setAnimation(Player.Key.WalkSouth);
            }
            else
            {
                player.setAnimation(Player.Key.WalkNorth);
            }
        }
    }
    
    /**
     * Assign the specified player the correct idle animation
     * @param player The player we want to assign animation
     */
    public static void stopWalking(final Player player)
    {
        //set idle animation
        switch (player.getAnimation())
        {
            case WalkEast:
                player.setAnimation(Player.Key.IdleEast);
                break;

            case WalkWest:
                player.setAnimation(Player.Key.IdleWest);
                break;

            case WalkNorth:
                player.setAnimation(Player.Key.IdleNorth);
                break;

            case WalkSouth:
            	default:
                player.setAnimation(Player.Key.IdleSouth);
                break;
        }
    }
    
    /**
     * Manage the specified player's velocity and update (col, row) location
     * @param player The player we want to manage
     */
    public static void manageVelocity(final Player player)
    {
    	//velocity will depend if debugging
    	final double velocity = (MainThread.DEBUG) ? Player.VELOCITY_DEBUG : Player.VELOCITY;
    	
        if (player.getCol() < player.getTarget().getCol())
        {
            if (player.getCol() + velocity >= player.getTarget().getCol())
            {
                player.setCol(player.getTarget().getCol());
            }
            else
            {
                player.setCol(player.getCol() + velocity);
            }
        }
        else if (player.getCol() > player.getTarget().getCol())
        {
            if (player.getCol() - velocity <= player.getTarget().getCol())
            {
                player.setCol(player.getTarget().getCol());
            }
            else
            {
                player.setCol(player.getCol() - velocity);
            }
        }
        else if (player.getRow() < player.getTarget().getRow())
        {
            if (player.getRow() + velocity >= player.getTarget().getRow())
            {
                player.setRow(player.getTarget().getRow());
            }
            else
            {
                player.setRow(player.getRow() + velocity);
            }
        }
        else if (player.getRow() > player.getTarget().getRow())
        {
            if (player.getRow() - velocity <= player.getTarget().getRow())
            {
                player.setRow(player.getTarget().getRow());
            }
            else
            {
                player.setRow(player.getRow() - velocity);
            }
        }
    }
    
    /**
     * Calculate the targets.<br>
     * We will determine the target of the player, as well as the neighboring block (if exists)
     * @param player The player we are checking
     * @param level Current level in play
     */
    public static void calculateTargets(final Player player, final Level level)
    {
    	//make sure all targets in a level have the current destination marked
    	for (Target block : level.getCurrent())
    	{
    		//assign current location so the undo is correct
    		block.setDestination(block.getCol(), block.getRow());
    	}
    	
        //the neighboring locations
        final int col1, row1;
        final int col2, row2;
        
        //determine where the neighbor locations are
        if (player.getCol() < player.getTarget().getCol())
        {
            col1 = (int)player.getCol() + 1;
            row1 = (int)player.getRow();
            col2 = col1 + 1;
            row2 = row1;
        }
        else if (player.getCol() > player.getTarget().getCol())
        {
            col1 = (int)player.getCol() - 1;
            row1 = (int)player.getRow();
            col2 = col1 - 1;
            row2 = row1;
        }
        else if (player.getRow() < player.getTarget().getRow())
        {
            col1 = (int)player.getCol();
            row1 = (int)player.getRow() + 1;
            col2 = col1;
            row2 = row1 + 1;
        }
        else 
        {
            //(player.getRow() > player.getTarget().getRow())
            col1 = (int)player.getCol();
            row1 = (int)player.getRow() - 1;
            col2 = col1;
            row2 = row1 - 1;
        }
            
        //if there is a wall directly next door
        if (TileHelper.isWall(level.getType(col1, row1)))
        {
            //we won't move the target
            player.setTarget(player.getCol(), player.getRow());
        }
        else
        {
            //if there is a block next door, lets see if we can move it
            if (level.getBlock(col1, row1) != null)
            {
                //if there is a wall or block on the other end, the player won't be able to move
                if (TileHelper.isWall(level.getType(col2, row2)) || level.getBlock(col2, row2) != null)
                {
                    //we won't move the target
                    player.setTarget(player.getCol(), player.getRow());
                }
                else
                {
                    //player can move to the next location
                    player.setTarget(col1, row1);
                    
                    //move the block as well
                    level.getBlock(col1, row1).setDestination(col2, row2);
                    
                    //increase move count
                    player.setMoves(player.getMoves() + 1);
                }
            }
            else
            {
                //player can move to the next location
                player.setTarget(col1, row1);
                
                //increase move count
                player.setMoves(player.getMoves() + 1);
            }
        }
    }
}