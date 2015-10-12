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
    private static final String DIRECTORY_AUDIO = "audio";
    
    /**
     * The directory where image resources are kept
     */
    private static final String DIRECTORY_IMAGE = "image";
    
    /**
     * The directory where font resources are kept
     */
    private static final String DIRECTORY_FONT = "font";
    
    /**
     * The directory where our text files are kept
     */
    private static final String DIRECTORY_TEXT = "text";
    
    /**
     * The different fonts used in our game.<br>
     * Order these according to the file name in the "font" assets folder.
     */
    public enum FontKey
    {
        Default
    }
    
    /**
     * The different images in our game.<br>
     * Order these according to the file name in the "image" assets folder.
     */
    public enum ImageKey
    {
        Background,
        Button,
        LevelIconComplete,
        LevelIconIncomplete, 
        LevelNext,
        LevelPrevious, 
        Logo,
        MenuCancel, 
        MenuConfirm, 
        MenuExit, 
        MenuPause, 
        MenuReset,
        MenuSoundOff, 
        MenuSoundOn, 
        Sprites, 
    }
    
    /**
     * The key of each text file.<br>
     * Order these according to the file name in the "text" assets folder.
     */
    public enum TextKey
    {
        LevelsEasyA,
        LevelsEasyB,
        LevelsMediumA,
        LevelsMediumB,
        LevelsHardA,
        LevelsHardB
    }
    
    /**
     * The key of each sound in our game.<br>
     * Order these according to the file name in the "audio" assets folder.
     */
    public enum AudioKey
    {
        LevelComplete,
        Goal,
        Music,
        Selection
    }
    
    /**
     * Load all assets
     * @param activity Object containing AssetManager needed to load assets
     * @throws Exception 
     */
    public static final void load(final Activity activity) throws Exception
    {
        //load all images
        Images.load(activity, ImageKey.values(), DIRECTORY_IMAGE);
        
        //load all fonts
        Font.load(activity, FontKey.values(), DIRECTORY_FONT);
        
        //load all audio
        Audio.load(activity, AudioKey.values(), DIRECTORY_AUDIO);
        
        //load all text files
        Files.load(activity, TextKey.values(), DIRECTORY_TEXT);
    }
    
    /**
     * Recycle assets
     */
    public static void recycle()
    {
        Images.dispose();
        Font.dispose();
        Audio.dispose();
        Files.dispose();
    }
}