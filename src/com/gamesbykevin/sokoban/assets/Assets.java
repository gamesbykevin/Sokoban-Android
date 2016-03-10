package com.gamesbykevin.sokoban.assets;

import android.app.Activity;

import com.gamesbykevin.androidframework.resources.*;

/**
 * This class will contain our game assets
 * @author GOD
 */
public class Assets 
{
    /**
     * The directory where audio sound effect resources are kept
     */
    private static final String DIRECTORY_MENU_AUDIO = "audio/menu";
    
    /**
     * The directory where audio sound effect resources are kept
     */
    private static final String DIRECTORY_GAME_AUDIO = "audio/game";
    
    /**
     * The directory where image resources are kept for the menu
     */
    private static final String DIRECTORY_MENU_IMAGE = "image/menu";
    
    /**
     * The directory where image resources are kept for the game
     */
    private static final String DIRECTORY_GAME_IMAGE = "image/game";
    
    /**
     * The directory where font resources are kept
     */
    private static final String DIRECTORY_MENU_FONT = "font/menu";
    
    /**
     * The directory where font resources are kept
     */
    private static final String DIRECTORY_GAME_FONT = "font/game";
    
    /**
     * The directory where our text files are kept
     */
    private static final String DIRECTORY_TEXT = "text";
    
    /**
     * The directory where our text files containing the ai instructions are kept
     */
    private static final String DIRECTORY_TEXT_SOLVED = "solved";
    
    /**
     * The different fonts used in our game.<br>
     * Order these according to the file name in the "font" assets folder.
     */
    public enum FontMenuKey
    {
    	Default
    }
    
    /**
     * The different fonts used in our game.<br>
     * Order these according to the file name in the "font" assets folder.
     */
    public enum FontGameKey
    {
    	Default
    }
    
    /**
     * The different images for our menu items
     */
    public enum ImageMenuKey
    {
        Background,
        Button,
        Facebook,
        Instructions,
        Logo,
        Cancel, 
        Confirm,
        Splash,
        Twitter
    }
    
    /**
     * The different images in our game.<br>
     * Order these according to the file name in the "image" assets folder.
     */
    public enum ImageGameKey
    {
        LevelIconComplete,
        LevelIconIncomplete, 
        LevelNext,
        LevelPrevious, 
        Exit, 
        Pause, 
        Reset,
        SoundOff, 
        SoundOn, 
        Sprites
    }
    
    /**
     * The key of each text file.<br>
     * Order these according to the file name in the "text" assets folder.
     */
    public enum TextKey
    {
        Easy_A("Easy A"),
        Easy_B("Easy B"),
        Easy_C("Easy C"),
        Easy_D("Easy D"),
        Medium_A("Medium A"),
        Medium_B("Medium B"),
        Medium_C("Medium C"),
        Medium_D("Medium D"),
        Hard_A("Hard A"),
        Hard_B("Hard B"),
        Hard_C("Hard C"),
        Hard_D("Hard D");
    	
    	private final String desc;
    	
    	private TextKey(final String desc)
    	{
    		this.desc = desc;
    	}
    	
    	public String getDesc()
    	{
    		return this.desc;
    	}
    }
    
    public enum TextAiInstructionsKey
    {
    	SOLVED_EASY_A_28,
    	SOLVED_EASY_B_170,
    	SOLVED_EASY_C_8
    }
    
    /**
     * The key of each sound in our game.<br>
     * Order these according to the file name in the "audio" assets folder.
     */
    public enum AudioMenuKey
    {
    	Selection
    }
    
    /**
     * The key of each sound in our game.<br>
     * Order these according to the file name in the "audio" assets folder.
     */
    public enum AudioGameKey
    {
        LevelComplete,
        Goal,
        Music
    }
    
    /**
     * Load all assets
     * @param activity Object containing AssetManager needed to load assets
     * @throws Exception 
     */
    public static final void load(final Activity activity) throws Exception
    {
        //load all images for the menu
        Images.load(activity, ImageMenuKey.values(), DIRECTORY_MENU_IMAGE, true);
        
        //load all fonts for the menu
        Font.load(activity, FontMenuKey.values(), DIRECTORY_MENU_FONT, true);
        
        //load all audio for the menu
        Audio.load(activity, AudioMenuKey.values(), DIRECTORY_MENU_AUDIO, true);
        
        //load images for the game
        Images.load(activity, ImageGameKey.values(), DIRECTORY_GAME_IMAGE, true);
        
        //load all audio for the game
        Audio.load(activity, AudioGameKey.values(), DIRECTORY_GAME_AUDIO, true);
        
        //load all fonts for the game
        Font.load(activity, FontGameKey.values(), DIRECTORY_GAME_FONT, true);
        
        //load all text files
        Files.load(activity, TextKey.values(), DIRECTORY_TEXT, true);
        
        //load the text files containing the ai instructions to solve a level
        Files.load(activity, TextAiInstructionsKey.values(), DIRECTORY_TEXT_SOLVED, true);
    }
    
    /**
     * Recycle assets
     */
    public static void recycle()
    {
        try
        {
            Images.dispose();
            Font.dispose();
            Audio.dispose();
            Files.dispose();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}