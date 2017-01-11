package com.gamesbykevin.sokoban.storage.scorecard;

import android.app.Activity;

import com.gamesbykevin.androidframework.io.storage.Internal;
import com.gamesbykevin.sokoban.game.Game;

import java.util.ArrayList;
import java.util.List;

/**
 * Here we will track the completed levels and save it to the internal storage
 * @author GOD
 */
public final class ScoreCard extends Internal
{
    //list of scores
    private List<Score> scores;
    
    /**
     * New score separator string
     */
    public static final String NEW_LEVEL_DELIMITER = ";";
    
    /**
     * This string will separate the data contained within a single score
     */
    public static final String LEVEL_DATA_DELIMITER = "-";
    
    //our game reference object
    private final Game game;
    
    /**
     * Create our score card
     * @param game Our game reference object
     * @param activity Our activity object
     * @param desc The unique name of the score card
     */
    public ScoreCard(final Game game, final Activity activity, final String desc)
    {
    	//there will be a score card for each difficulty
        super("ScoreCard_" + desc, activity);
        
        //store our game reference object
        this.game = game;
        
        //create new score
        this.scores = new ArrayList<Score>();
        
        //make sure content exists before we try to load it
        if (super.getContent().toString().trim().length() > 0)
        {
            //load file with each level on a new line
            final String[] scores = super.getContent().toString().split(NEW_LEVEL_DELIMITER);

            //load each level into our array
            for (int index = 0; index < scores.length; index++)
            {
                //split level data
                String[] data = scores[index].split(LEVEL_DATA_DELIMITER);

                //get the information
                final int level = Integer.parseInt(data[0]);
                final int moves = Integer.parseInt(data[1]);
                long time;
                
                try
                {
                	time = Long.parseLong(data[2]);
                }
                catch (Exception e)
                {
                	time = (long)Double.parseDouble(data[2]);
                }

                //load the score to our list
                update(level, moves, time);
            }
        }
    }
    
    /**
     * Do we have the score?<br>
     * We want the score of the specified level 
     * @param level The level index
     * @return true if the specified score exists, false otherwise
     */
    public boolean hasScore(final int level)
    {
    	return (getScore(level) != null);
    }
    
    /**
     * Do we have the score?<br>
     * We will get the score of the current level to see if exists
     * @return true if the specified score exists, false otherwise
     */
    public boolean hasScore()
    {
    	//use the current level index, and colors
    	return hasScore(game.getLevels().getLevelSelect().getLevelIndex());
    }
    
    /**
     * Get the score object.<br>
     * We will get the score of the current level
     * @return The score object of the current level, if not found null is returned
     */
    public Score getScore()
    {
    	return getScore(game.getLevels().getLevelSelect().getLevelIndex());
    }
    
    /**
     * Get the score object of the specified level
     * @param level The level index
     * @return The score object of the specified level, if not found null is returned
     */
    public Score getScore(final int level)
    {
    	//check our list to see if the specified score is better
    	for (Score score : scores)
    	{
    		//if the level and colors match return the score
    		if (score.getLevel() == level)
    			return score;
    	}
    	
    	//score was not found
    	return null;
    }
    
    /**
     * Update the score for the specified level.<br>
     * If the score does not exist, it will be added.<br>
     * @param level The specified level
     * @param moves The number of moves to solve the level
     * @param time The duration it took to solve the level
     * @return true if updating the score was successful, false otherwise
     */
    public boolean update(final int level, final int moves, final long time)
    {
    	//get the score for the level
    	final Score score = getScore(level);
    	
    	//if the score does not exist, add it
    	if (score == null)
    	{
    		//score does not exist, so add it
    		scores.add(new Score(level, moves, time));
    	}
    	else
    	{
    		//only update if the new score is less time and has <= the existing moves
    		if (moves <= score.getMoves() && time < score.getTime())
    		{
    			//update the moves
    			score.setMoves(moves);
    			
    			//update the time
    			score.setTime(time);
    		}
    		else
    		{
	    		//the score was not better, return false
	    		return false;
    		}
    	}
    	
    	//save the score
    	save();
    	
    	//score was updated
    	return true;
    }
    
    /**
     * Save the scores to the internal storage
     */
    @Override
    public void save()
    {
        //remove all existing content
        super.getContent().delete(0, super.getContent().length());
        
        //save each score
        for (Score score : scores)
        {
            //if content exists, add delimiter to separate each score
            if (super.getContent().length() > 0)
                super.getContent().append(NEW_LEVEL_DELIMITER);
            
            //write level, size, and time
            super.getContent().append(score.getLevel());
            super.getContent().append(LEVEL_DATA_DELIMITER);
            super.getContent().append(score.getMoves());
            super.getContent().append(LEVEL_DATA_DELIMITER);
            super.getContent().append(score.getTime());
        }
        
        //save the content to physical internal storage location
        super.save();
    }
    
    @Override
    public void dispose()
    {
        super.dispose();
        
        if (scores != null)
        {
            scores.clear();
            scores = null;
        }
    }
}