package com.themagicofmusic.model;

/**
 * Developed by Atharva Kasar 6/27/2017
 */

public class Music {
    int musicID;
    String musicName;
    String musicDetail;
    String beatTypeCode;
    String musicLocation;

    public Music()
    {

    }
    public Music(int id, String name)
    {
        musicID =id;
        musicName = name;
    }
    public Music(int id, String name, String musicdetail)
    {
        musicID =id;
        musicName = name;
        musicDetail =musicdetail;
    }
    public Music(int id, String name, String detail, String beatTypeCd, String location)
    {
        musicID =id;
        musicName = name;
        musicDetail = detail;
        beatTypeCode = beatTypeCd;
        musicLocation = location;
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

    public String getMusicDetail()
    {
        return musicDetail;
    }

    public void setMusicDetail(String value)
    {
        musicDetail = value;
    }

    public String getBeatTypeCode()
    {
        return beatTypeCode;
    }

    public void setBeatTypeCode(String value)
    {
        beatTypeCode = value;
    }

    public String getMusicLocation()
    {
        return musicLocation;
    }

    public void setMusicLocation(String value)
    {
        musicLocation = value;
    }
}
