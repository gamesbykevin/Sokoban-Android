package com.gamesbykevin.sokoban.level.tile;

import com.gamesbykevin.androidframework.anim.Animation;
import com.gamesbykevin.androidframework.resources.Images;
import com.gamesbykevin.sokoban.assets.Assets;

/**
 * Block
 * @author GOD
 */
public final class Block extends Tile
{
    public enum Style
    {
        Yellow, Red, Beige, Black, Brown, Purple, Blue, Gray
    }
    
    /**
     * Most tiles will only have 1 animation, but the block will have an alternate
     */
    public enum State
    {
        Default, Alternate
    }
    
    /**
     * Create new block
     * @param style The color of the block
     */
    public Block(final Style style)
    {
        super(Tile.Type.Block);
     
        //set animation
        setAnimation(style);
    }
    
    public final void setAnimation(final Style style)
    {
        int x, y, d = TileHelper.ANIMATION_DIMENSION;
        
        //set dimensions
        super.setWidth(d);
        super.setHeight(d);
        
        switch (style)
        {
            case Yellow:
                x = TileHelper.getSpriteSheetX(3);
                y = TileHelper.getSpriteSheetY(0);
                getSpritesheet().add(State.Default, new Animation(Images.getImage(Assets.ImageKey.Sprites), x, y, d, d));

                x = TileHelper.getSpriteSheetX(2);
                y = TileHelper.getSpriteSheetY(4);
                getSpritesheet().add(State.Alternate, new Animation(Images.getImage(Assets.ImageKey.Sprites), x, y, d, d));
                break;
                
            case Red:
                x = TileHelper.getSpriteSheetX(2);
                y = TileHelper.getSpriteSheetY(5);
                getSpritesheet().add(State.Default, new Animation(Images.getImage(Assets.ImageKey.Sprites), x, y, d, d));

                x = TileHelper.getSpriteSheetX(4);
                y = TileHelper.getSpriteSheetY(1);
                getSpritesheet().add(State.Alternate, new Animation(Images.getImage(Assets.ImageKey.Sprites), x, y, d, d));
                break;
                
            case Beige:
                x = TileHelper.getSpriteSheetX(3);
                y = TileHelper.getSpriteSheetY(1);
                getSpritesheet().add(State.Default, new Animation(Images.getImage(Assets.ImageKey.Sprites), x, y, d, d));

                x = TileHelper.getSpriteSheetX(5);
                y = TileHelper.getSpriteSheetY(1);
                getSpritesheet().add(State.Alternate, new Animation(Images.getImage(Assets.ImageKey.Sprites), x, y, d, d));
                break;
                
            case Black:
                x = TileHelper.getSpriteSheetX(3);
                y = TileHelper.getSpriteSheetY(2);
                getSpritesheet().add(State.Default, new Animation(Images.getImage(Assets.ImageKey.Sprites), x, y, d, d));

                x = TileHelper.getSpriteSheetX(5);
                y = TileHelper.getSpriteSheetY(0);
                getSpritesheet().add(State.Alternate, new Animation(Images.getImage(Assets.ImageKey.Sprites), x, y, d, d));
                break;
                
            case Brown:
                x = TileHelper.getSpriteSheetX(3);
                y = TileHelper.getSpriteSheetY(4);
                getSpritesheet().add(State.Default, new Animation(Images.getImage(Assets.ImageKey.Sprites), x, y, d, d));

                x = TileHelper.getSpriteSheetX(4);
                y = TileHelper.getSpriteSheetY(4);
                getSpritesheet().add(State.Alternate, new Animation(Images.getImage(Assets.ImageKey.Sprites), x, y, d, d));
                break;
                
            case Purple:
                x = TileHelper.getSpriteSheetX(4);
                y = TileHelper.getSpriteSheetY(2);
                getSpritesheet().add(State.Default, new Animation(Images.getImage(Assets.ImageKey.Sprites), x, y, d, d));

                x = TileHelper.getSpriteSheetX(4);
                y = TileHelper.getSpriteSheetY(0);
                getSpritesheet().add(State.Alternate, new Animation(Images.getImage(Assets.ImageKey.Sprites), x, y, d, d));
                break;
                
            case Blue:
                x = TileHelper.getSpriteSheetX(3);
                y = TileHelper.getSpriteSheetY(3);
                getSpritesheet().add(State.Default, new Animation(Images.getImage(Assets.ImageKey.Sprites), x, y, d, d));

                x = TileHelper.getSpriteSheetX(4);
                y = TileHelper.getSpriteSheetY(5);
                getSpritesheet().add(State.Alternate, new Animation(Images.getImage(Assets.ImageKey.Sprites), x, y, d, d));
                break;
                
            case Gray:
                x = TileHelper.getSpriteSheetX(3);
                y = TileHelper.getSpriteSheetY(5);
                getSpritesheet().add(State.Default, new Animation(Images.getImage(Assets.ImageKey.Sprites), x, y, d, d));

                x = TileHelper.getSpriteSheetX(4);
                y = TileHelper.getSpriteSheetY(3);
                getSpritesheet().add(State.Alternate, new Animation(Images.getImage(Assets.ImageKey.Sprites), x, y, d, d));
                break;
        }
    }
}