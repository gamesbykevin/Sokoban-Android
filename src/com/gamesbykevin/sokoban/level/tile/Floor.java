package com.gamesbykevin.sokoban.level.tile;

import com.gamesbykevin.androidframework.anim.Animation;
import com.gamesbykevin.androidframework.resources.Images;
import com.gamesbykevin.sokoban.assets.Assets;

/**
 * Floor
 * @author GOD
 */
public final class Floor extends Tile
{
    public enum Style
    {
        Beige1, Beige2, 
        Green1, Green2,
        Brown1, Brown2,
        Gray1, Gray2
    }
    
    public Floor(final Style style)
    {
        super(Tile.Type.Floor);
        
        setAnimation(style);
    }
    
    public final void setAnimation(final Style style)
    {
        int x = 0, y = 0, d = TileHelper.DEFAULT_DIMENSION;
        
        //set dimensions
        super.setWidth(d);
        super.setHeight(d);
        
        switch (style)
        {
            case Beige1:
                x = TileHelper.getSpriteSheetX(1);
                y = TileHelper.getSpriteSheetY(2);
                break;
                
            case Beige2:
                x = TileHelper.getSpriteSheetX(2);
                y = TileHelper.getSpriteSheetY(0);
                break;
                
            case Green1:
                x = TileHelper.getSpriteSheetX(1);
                y = TileHelper.getSpriteSheetY(3);
                break;
                
            case Green2:
                x = TileHelper.getSpriteSheetX(2);
                y = TileHelper.getSpriteSheetY(1);
                break;
                
            case Brown1:
                x = TileHelper.getSpriteSheetX(1);
                y = TileHelper.getSpriteSheetY(4);
                break;
                
            case Brown2:
                x = TileHelper.getSpriteSheetX(2);
                y = TileHelper.getSpriteSheetY(2);
                break;
                
            case Gray1:
                x = TileHelper.getSpriteSheetX(1);
                y = TileHelper.getSpriteSheetY(5);
                break;
                
            case Gray2:
                x = TileHelper.getSpriteSheetX(2);
                y = TileHelper.getSpriteSheetY(3);
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