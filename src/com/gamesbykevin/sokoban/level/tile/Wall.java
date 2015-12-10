package com.gamesbykevin.sokoban.level.tile;

import com.gamesbykevin.androidframework.anim.Animation;
import com.gamesbykevin.androidframework.resources.Images;
import com.gamesbykevin.sokoban.assets.Assets;

/**
 * Wall
 * @author GOD
 */
public final class Wall extends Tile
{
    public enum Style
    {
        Gray, Brown, Black, Beige
    }
    
    public Wall(final Style style)
    {
        super(Tile.Type.Wall);
        
        setAnimation(style);
    }
    
    public final void setAnimation(final Style style)
    {
        int x = 0, y = 0, d = TileHelper.ANIMATION_DIMENSION;
        
        //set dimensions
        super.setWidth(d);
        super.setHeight(d);
        
        switch (style)
        {
            case Gray:
                x = TileHelper.getSpriteSheetX(0);
                y = TileHelper.getSpriteSheetY(4);
                break;
                
            case Brown:
                x = TileHelper.getSpriteSheetX(0);
                y = TileHelper.getSpriteSheetY(5);
                break;
                
            case Black:
                x = TileHelper.getSpriteSheetX(1);
                y = TileHelper.getSpriteSheetY(0);
                break;
                
            case Beige:
                x = TileHelper.getSpriteSheetX(1);
                y = TileHelper.getSpriteSheetY(1);
                break;
        }
        
        //if animation doesn't exist, add it
        if (getSpritesheet().get(style) == null)
        {
            //map the style
            getSpritesheet().add(style, new Animation(Images.getImage(Assets.ImageGameKey.Sprites), x, y, d, d));
        }
        
        //assign this style as the current
        getSpritesheet().setKey(style);
    }
}