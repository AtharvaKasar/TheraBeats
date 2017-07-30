package com.themagicofmusic.model;

/**
 * TheMagicOfMusic - TheraBeat
 * Created by Atharva Kasar
 */

public class Video {

    int videoID;
    String videoName;
    String videoDetail;

    public Video()
    {

    }
    public Video(int id, String name)
    {
        videoID =id;
        videoName = name;
    }
    public Video(int id, String name, String videodetail)
    {
        videoID =id;
        videoName = name;
        videoDetail =videodetail;
    }

    public int getvideoID()
    {
        return videoID;
    }

    public void setvideoID(int value)
    {
        videoID = value;
    }

    public String getvideoName()
    {
        return videoName;
    }

    public void setvideoName(String value)
    {
        videoName = value;
    }

    public String getvideoDetail()
    {
        return videoDetail;
    }

    public void setvideoDetail(String value)
    {
        videoDetail = value;
    }

}
