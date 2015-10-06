package com.gamesbykevin.sokoban.screen;

import android.graphics.Canvas;
import android.view.MotionEvent;
import com.gamesbykevin.androidframework.resources.Disposable;
import com.gamesbykevin.androidframework.screen.Screen;
import com.gamesbykevin.sokoban.assets.Assets;
import com.gamesbykevin.sokoban.game.Game;

/**
 * The game screen that contains the game
 * @author GOD
 */
public class GameScreen implements Screen, Disposable
{
    //our object containing the main game functionality
    private Game game;
    
    //our main screen reference
    private final MainScreen screen;
    
    public GameScreen(final MainScreen screen)
    {
        this.screen = screen;
    }
    
    protected Game getGame()
    {
        return this.game;
    }
    
    /**
     * Create game object
     * @throws Exception
     */
    public void createGame() throws Exception
    {
        if (getGame() == null)
            this.game = new Game(screen);
        
        //reset the game
        getGame().reset();
    }
    
    /**
     * Reset any necessary screen elements here
     */
    @Override
    public void reset()
    {
        
    }
    
    @Override
    public boolean update(final MotionEvent event, final float x, final float y) throws Exception
    {
        if (getGame() != null)
            getGame().updateMotionEvent(event, x, y);
        
        return true;
    }
    
    @Override
    public void update() throws Exception
    {
        if (getGame() != null)
            getGame().update();
    }
    
    @Override
    public void render(final Canvas canvas) throws Exception
    {
        //render game if exists
        if (getGame() != null)
            getGame().render(canvas);
    }
    
    @Override
    public void dispose()
    {
        if (game != null)
        {
            game.dispose();
            game = null;
        }
    }
}