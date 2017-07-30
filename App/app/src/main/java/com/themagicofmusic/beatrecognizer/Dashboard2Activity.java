package com.themagicofmusic.beatrecognizer;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.themagicofmusic.Algorithms.*;
import com.themagicofmusic.database.*;
import com.themagicofmusic.model.Music;
import com.themagicofmusic.model.*;
import com.themagicofmusic.model.SelectedPerson;

import java.util.ArrayList;
import java.util.List;

//Atharva - This is a main dashboard
public class Dashboard2Activity extends AppCompatActivity {
 Person selectedPerson = null;
    TextView name ;
    PersonDBHelper personDB = new PersonDBHelper(this);
    MusicRecommendationDBHelper recDB = new MusicRecommendationDBHelper(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_dashboard2);
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            personDB = new PersonDBHelper(this);
            recDB = new MusicRecommendationDBHelper(this);


            name = (TextView) findViewById(R.id.textPersonName);
            if (SelectedPerson.PersonID != 0) {

                Cursor rs = personDB.getData(SelectedPerson.PersonID);
                rs.moveToFirst();
                String nam = rs.getString(rs.getColumnIndex(PersonDBHelper.PERSONS_COLUMN_LASTNAME));
                String age = rs.getString(rs.getColumnIndex(PersonDBHelper.PERSONS_COLUMN_AGE));
                String hobby = rs.getString(rs.getColumnIndex(PersonDBHelper.PERSONS_COLUMN_HOBBY));
                String issue = rs.getString(rs.getColumnIndex(PersonDBHelper.PERSONS_COLUMN_ISSUE));
                selectedPerson = new Person();
                selectedPerson.setPersonID(SelectedPerson.PersonID);
                selectedPerson.setFirstName(nam);
                selectedPerson.setIssue(issue);
                selectedPerson.setHobby(hobby);
                String dashboardname = "Dashboard for : " + nam;
                name.setText((CharSequence) dashboardname);
                name.setFocusable(false);
                name.setClickable(false);
            }
        }
        catch(Exception e)
        {
            messageBox("Error occured", "Error in Dashboard "+ e.getMessage());
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dashboard_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        super.onOptionsItemSelected(item);

        switch(item.getItemId()) {

            case R.id.favmusic:Bundle dataBundle = new Bundle();
                dataBundle.putInt("id", 0);

                if(SelectedPerson.PersonID !=0) {
                    Intent intent = new Intent(getApplicationContext(), DisplayFavoriteMusic.class);
                    intent.putExtras(dataBundle);

                    startActivity(intent);
                }
                return true;
            case R.id.recommendation:Bundle dataBundle1 = new Bundle();
                dataBundle1.putInt("id", 0);

                Intent intent1 = new Intent(getApplicationContext(),DisplayMusicRecommendation.class);
                intent1.putExtras(dataBundle1);

                startActivity(intent1);
                return true;
            case R.id.journal:Bundle dataBundle2 = new Bundle();
                dataBundle2.putInt("id", 0);

                Intent intent2 = new Intent(getApplicationContext(),DisplayJournal.class);
                intent2.putExtras(dataBundle2);

                startActivity(intent2);
                return true;
            case R.id.main:Bundle dataBundle3 = new Bundle();
                dataBundle3.putInt("id", 0);

                Intent intent3 = new Intent(getApplicationContext(),BeatRecognizerMain.class);
                intent3.putExtras(dataBundle3);

                startActivity(intent3);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }

    /**
     * Feedback button click handler
     * @param v
     */
    public void btnMusicClick(View v)
    {
        Intent intent = new Intent(getApplicationContext(), DisplayMusic.class);
        startActivity(intent);
    }

    public void btnPlayStoreMusicClick(View v)
    {
        Intent intent = new Intent(getApplicationContext(), DisplayPlayStoreMusicActivity.class);
        //intent.setFlags (Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);

    }
    public void btnExtractMusicFeatureClick(View v)
    {
        Intent intent = new Intent(getApplicationContext(), MusicFeatureActivity.class);
        startActivity(intent);
    }

    public void btnJournalClick(View v)
    {
        Intent intent = new Intent(getApplicationContext(), AddJournal.class);
        startActivity(intent);
    }
    public void btnDisplayJournalClick(View v)
    {
        Intent intent = new Intent(getApplicationContext(), DisplayJournal.class);
        startActivity(intent);
    }

    public void btnDisplayFavoriteMusicClick(View v)
    {
        Intent intent = new Intent(getApplicationContext(), DisplayFavoriteMusic.class);
        startActivity(intent);
    }

    public void btnSuggestMusicRecommendationClick(View v)
    {
        try {

            if (selectedPerson != null) {

                    //messageBox("Insert", "Adding recommendation for person ID " + selectedPerson.getPersonID());
                    MusicRecommendationDBHelper musicRecommendationDB = new MusicRecommendationDBHelper(this);
                    MusicRecommender musicRecommender = new MusicRecommender(this);

                    List<Music> recomMusicList = musicRecommender.GetRecommendMusic(selectedPerson);
                    //messageBox("Success", "Recommended music list count " + recomMusicList.size());
                    int musicRecommendationCount = musicRecommendationDB.getMusicRecommendationsCount(selectedPerson.getPersonID());
                    // messageBox("Success", "Music recommendation count " + musicRecommendationCount);
                    if (musicRecommendationCount <= 1) {
                        //messageBox("Success", "Person doesn't have any recommendated music");
                        List<MusicRecommendation> personMusicRecommendationList = new ArrayList<MusicRecommendation>();

                        for (Music m : recomMusicList) {
                            MusicRecommendation mRec = new MusicRecommendation();
                            mRec.setPersonID(SelectedPerson.PersonID);
                            mRec.setMusicID(m.getMusicID());
                            personMusicRecommendationList.add(mRec);
                        }

                        if (personMusicRecommendationList.size() > 0) {
                            //messageBox("Success", "Person recommended music count " + personMusicRecommendationList.size());
                            musicRecommendationDB.addAllMusicRecommendation(personMusicRecommendationList);
                        }
                        Intent intent = new Intent(getApplicationContext(), DisplayMusicRecommendation.class);
                        startActivity(intent);
                }
                else
                {
                    //messageBox("Success", "Person has recommendated music" + musicRecommendationCount);
                    Intent intent = new Intent(getApplicationContext(), DisplayMusicRecommendation.class);
                    startActivity(intent);
                }

            } else {
                messageBox("Success", "Selected person not set");
            }
        }
        catch(Exception ex)
        {
            //messageBox("Error", ex.getMessage());
        }

    }

    public void btnDisplayMusicRecommendationClick(View v)
    {
        Intent intent = new Intent(getApplicationContext(), DisplayMusicRecommendation.class);
        startActivity(intent);
    }

    public void btnDisplayVideosClick(View v)
    {
        Intent intent = new Intent(getApplicationContext(), DisplayVideos.class);
        startActivity(intent);
    }

    private void messageBox(String method, String message)
    {

        AlertDialog.Builder messageBox = new AlertDialog.Builder(this);
        messageBox.setTitle(method);
        messageBox.setMessage(message);
        messageBox.setCancelable(false);
        messageBox.setNeutralButton("OK", null);
        messageBox.show();
    }
}
