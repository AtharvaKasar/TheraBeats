package com.themagicofmusic.Algorithms;

import com.themagicofmusic.model.*;
import java.util.*;
import com.themagicofmusic.database.*;
import android.content.*;
/**
 * Atharva - This class with provide music recommendation based on person details
 * Algorithms to implement
 */

public class MusicRecommender {
     private MusicDBHelper myDB;

    public MusicRecommender(Context context)
    {
        myDB = new MusicDBHelper(context);
    }

    public List<Music> GetRecommendMusic(Person person)
    {
        try {
            List<Music> musicList = new ArrayList<Music>();

            //For hyper temsion kids need to recommend soft music
            String issue = person.getIssue();
            String beatTypeRecommendation = null;

            if (issue.equals("Severe")) {
                beatTypeRecommendation = "HARD";
            }
            else if(issue.equals("Medium"))
            {
                beatTypeRecommendation = "MILD";
            }
            else if(issue.equals("Mild"))
            {
                beatTypeRecommendation = "SOFT";
            }
            else {
                beatTypeRecommendation = "SOFT";
            }
            if (beatTypeRecommendation != null) {
                musicList = myDB.getRecommendedMusic(beatTypeRecommendation);
            }
            musicList = myDB.getRecommendedMusic(beatTypeRecommendation);
            return musicList;
        }
        catch(Exception ex)
        {
            throw ex;
        }
    }


}
