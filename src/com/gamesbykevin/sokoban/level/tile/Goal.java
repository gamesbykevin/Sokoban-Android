package com.gamesbykevin.sokoban.level.tile;

import com.gamesbykevin.androidframework.anim.Animation;
import com.gamesbykevin.androidframework.resources.Images;
import com.gamesbykevin.sokoban.assets.Assets;

/**
 * Goal
 * @author GOD
 */
public final class Goal extends Tile
{
    public enum Style
    {
        Brown, Beige, Gray, Purple,
        Blue, Black, Red, Yellow,
    }
    
    public Goal(final Style style)
    {
        super(Tile.Type.Goal);
        
        setAnimation(style);
    }
    
    public final void setAnimation(final Style style)
    {
        int x = 0, y = 384, d = TileHelper.GOAL_DIMENSION;
        
        //set dimensions
        super.setWidth(d);
        super.setHeight(d);
        
        switch (style)
        {
            case Brown:
                x = (0 * TileHelper.GOAL_DIMENSION);
                break;
                
            case Beige:
                x = (1 * TileHelper.GOAL_DIMENSION);
                break;
                
            case Gray:
                x = (2 * TileHelper.GOAL_DIMENSION);
                break;
                
            case Purple:
                x = (3 * TileHelper.GOAL_DIMENSION);
                break;
                
            case Blue:
                x = (4 * TileHelper.GOAL_DIMENSION);
                break;
                
            case Black:
                x = (5 * TileHelper.GOAL_DIMENSION);
                break;
                
            case Red:
                x = (6 * TileHelper.GOAL_DIMENSION);
                break;
                
            case Yellow:
                x = (7 * TileHelper.GOAL_DIMENSION);
                break;
        }
        
        //if animation doesn't exist, add it
        if (getSpritesheet().get(style) == null)
        {
            //map the style
            getSpritesheet().add(style, new Animation(Images.getImage(Assets.ImageKey.Sprites), x, y, d, d));
        }
        
        //assign this style as the current
        getSpritesheet().setKey(style);
    }
}