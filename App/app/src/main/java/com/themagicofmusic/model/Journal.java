package com.themagicofmusic.model;

/**
 * Created by Atharva on 5/8/2017.
 */

public class Journal {

    int personID;
    int journalID;
    String journalDate;
    String journalDetail;
    String musicListened;

    public int getPersonID()
    {
        return personID;
    }

    public void setPersonID(int value)
    {
        personID = value;
    }

    public int getJournalID()
    {
        return journalID;
    }

    public void setJournalID(int value)
    {
        journalID = value;
    }

    public String getJournalDate()
    {
        return journalDate;
    }

    public void setJournalDate(String value)
    {
        journalDate = value;
    }

    public String getJournalDetail()
    {
        return journalDetail;
    }

    public void setJournalDetail(String value)
    {
        journalDetail = value;
    }

    public String getMusicListened()
    {
        return musicListened;
    }

    public void setMusicListened( String value)
    {
        musicListened = value;
    }


}
