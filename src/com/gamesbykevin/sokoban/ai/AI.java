package com.gamesbykevin.sokoban.ai;

import java.util.List;

import com.gamesbykevin.androidframework.resources.Files;
import com.gamesbykevin.sokoban.assets.Assets;
import com.gamesbykevin.sokoban.level.Level;
import com.gamesbykevin.sokoban.player.Player;
import com.gamesbykevin.sokoban.player.PlayerHelper;
import com.gamesbykevin.sokoban.thread.MainThread;

/**
 * What he ai class will do is analyze a text file and move the player according to those instructions
 */
public class AI 
{
	//list of instructions to follow
	private String instructions = "";

	//the position of the instructions String
	private int index = 0;
	
	/**
	 * The string representing the instruction to move left
	 */
	private static final String LEFT = "L";
	
	/**
	 * The string representing the instruction to move right
	 */
	private static final String RIGHT = "R";
	
	/**
	 * The string representing the instruction to move down
	 */
	private static final String DOWN = "D";
	
	/**
	 * The string representing the instruction to move up
	 */
	private static final String UP = "U";
	
	//the list of solutions for each level
	private List<String> levels;
	
	/**
	 * Create the ai to solve the level
	 */
	public AI() 
	{
		//set our list of levels
		this.setLevels(Assets.TextAiInstructionsKey.SOLVED_EASY_A);
		
		//start at the beginning
		this.reset(0);
	}

	/**
	 * Assign the levels we want to solve
	 * @param key The unique identifier of the list of levels we want to solve
	 */
	public final void setLevels(Assets.TextAiInstructionsKey key)
	{
		//get all the lines in the text file
		this.levels = Files.getText(key).getLines();
	}
	
	/**
	 * Reset the ai for the specified level
	 * @param levelIndex The desired level to solve
	 */
	public final void reset(final int levelIndex)
	{
		//set the text position back at 0
		this.index = 0;
		
		//get the data for this level
		String[] data = this.levels.get(levelIndex).split(" ");
		
		//the instructions are always the last piece of data
		this.instructions = data[data.length - 1].trim();
		
		if (MainThread.DEBUG)
			System.out.println("Index=" + index + ",Steps=" + instructions);
	}
	
	public void update(final Player player, final Level level)
	{
		//stay inbounds
		if (index >= instructions.length())
			return;
		
		//make sure we have not yet selected the player
		if (!player.isSelected())
		{
			//make sure the player is at the current target as well
			if (player.hasTarget())
			{
				//make sure blocks aren't moving
				if (!level.hasDestination())
					return;
				
				//flag selected true
				player.setSelected(true);
				
				//determine which direction we are moving
				if (instructions.substring(index, index + 1).equalsIgnoreCase(DOWN))
				{
					player.setTarget(player.getCol(), player.getRow() + 1);
				}
				else if (instructions.substring(index, index + 1).equalsIgnoreCase(UP))
				{
					player.setTarget(player.getCol(), player.getRow() - 1);
				}
				else if (instructions.substring(index, index + 1).equalsIgnoreCase(LEFT))
				{
					player.setTarget(player.getCol() - 1, player.getRow());
				}
				else if (instructions.substring(index, index + 1).equalsIgnoreCase(RIGHT))
				{
					player.setTarget(player.getCol() + 1, player.getRow());
				}
				
                //calculate the targets
                PlayerHelper.calculateTargets(player, level);
                
                //move to the next instruction
                this.index++;
			}
		}
	}
}