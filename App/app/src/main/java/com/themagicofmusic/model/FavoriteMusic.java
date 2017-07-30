package com.themagicofmusic.model;
/**
 * TheMagicOfMusic - TheraBeat
 * Created by Atharva Kasar
 */

public class FavoriteMusic {
    int favMusicID;
    int personID;
    int musicID;
    String musicName;

    public FavoriteMusic()
    {

    }
    public FavoriteMusic(int favMusicID, int personid,int musicid)
    {
        favMusicID =favMusicID;
        personID = personid;
        musicID = musicid;

    }
    public int getfavMusicID()
    {
        return favMusicID;
    }

    public void setfavMusicID(int value)
    {
        favMusicID = value;
    }

    public int getPersonID()
    {
        return personID;
    }

    public void setPersonID(int value)
    {
        personID = value;
    }

    public int getMusicID()
    {
        return musicID;
    }

    public void setMusicID(int value)
    {
        musicID = value;
    }

    public String getMusicName()
    {
        return musicName;
    }

    public void setMusicName(String value)
    {
        musicName = value;
    }
}
