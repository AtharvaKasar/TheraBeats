package com.themagicofmusic.model;

/**
 * TheMagicOfMusic - TheraBeat
 * Created by Atharva Kasar
 */
import java.util.*;

public class Config {

    public Config()
    {

        AddAutismRelaxingVideos();
    }
    public Config(int id, int currentPersonId)
    {
        configID =id;
        currentPersonID = currentPersonId;
        AddAutismRelaxingVideos();
    }

    private int configID;
    public int getConfigID()
    {
        return configID;
    }

    public void setConfigID(int value)
    {
        configID = value;
    }

    private int currentPersonID;
    public int getCurrentPersonID()
    {
        return currentPersonID;
    }

    public void setCurrentPersonID(int value)
    {
        currentPersonID = value;
    }

    static List<String> AutismRelaxingVideo = new ArrayList<String>();

    public static  List<String> AutismRelaxingVideos()
    {
        AddAutismRelaxingVideos();
        return AutismRelaxingVideo;
    }
    private static void AddAutismRelaxingVideos()
    {
        AutismRelaxingVideo.add("https://www.youtube.com/watch?v=ztiA3F6WqG4");

    }

}
