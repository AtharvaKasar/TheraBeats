package com.themagicofmusic.model;

/**
 * Created by KasNet14 on 5/9/2017.
 */

public class MusicRecommendation {
    int recommendationID;
    int personID;
    int musicID;
    String musicName;

    public MusicRecommendation()
    {

    }
    public MusicRecommendation(int recommendationid, int personid,int musicid)
    {
        recommendationID =recommendationid;
        personID = personid;
        musicID = musicid;

    }
    public int getRecommendationID()
    {
        return recommendationID;
    }

    public void setRecommendationID(int value)
    {
        recommendationID = value;
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
