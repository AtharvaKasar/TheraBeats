package com.themagicofmusic.model;

/**
 * TheMagicOfMusic - TheraBeat
 * Created by Atharva Kasar
 */

public class BeatType {
    int beatTypeID;
    String beatTypeCode;
    String beatTypeDesc;

    public BeatType()
    {

    }
    public BeatType(int id, String code, String desc)
    {
        beatTypeID =id;
        beatTypeCode = code;
        beatTypeDesc = desc;
    }
    public int getBeatTypeID()
    {
        return beatTypeID;
    }

    public void setBeatTypeID(int value)
    {
        beatTypeID = value;
    }

    public String getBeatTypeCode()
    {
        return beatTypeCode;
    }

    public void setBeatTypeCode(String value)
    {
        beatTypeCode = value;
    }

    public String getBeatTypeDesc()
    {
        return beatTypeDesc;
    }

    public void setBeatTypeDesc(String value)
    {
        beatTypeDesc = value;
    }
}


