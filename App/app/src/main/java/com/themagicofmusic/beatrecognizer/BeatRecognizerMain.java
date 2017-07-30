package com.themagicofmusic.beatrecognizer;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.content.Intent;

import android.widget.EditText;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.themagicofmusic.Algorithms.MusicRecommender;
import com.themagicofmusic.database.*;
import com.themagicofmusic.model.*;
import android.database.sqlite.SQLiteDatabase;
/**
 * TheMagicOfMusic - TheraBeat
 * Created by Atharva Kasar
 */
public class BeatRecognizerMain extends AppCompatActivity {

   /* private TextView mTextMessage;
    private ListView obj;
    DBHelper mydb;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.navigation_dashboard: {
                    mTextMessage.setText(R.string.title_dashboard);
                    //Call Dashboard here
                    //Intent clientStartIntent = new Intent(this, Dashboard.class);
                    //startActivityForResult(clientStartIntent, fileRequestID);
                    //boolean loading = ProgressDialog.show(getContext(),"Please wait...","Loading...",false,false);
                    return true;
                }
                case R.id.navigation_notifications:
                    mTextMessage.setText(R.string.title_notifications);

                    return true;

            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mydb = new DBHelper(this);
        ArrayList array_list = mydb.getAllCotacts();
        ArrayAdapter arrayAdapter=new ArrayAdapter(this,android.R.layout.simple_list_item_1, array_list);

        //obj = (ListView)findViewById(R.id.listView1);
        //obj.setAdapter(arrayAdapter);
        setContentView(R.layout.beat_recognizer_main);

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }*/

    public final static String EXTRA_MESSAGE = "MESSAGE";
    private ListView obj;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
           // this.deleteDatabase("BeatRecognizer1.db");
            //messageBox("Success", "Database deleted successfully");
            //Check database here. If it doesn't exists then create
            ConfigDBHelper configDB = new ConfigDBHelper(this);
            PersonDBHelper personDB = new PersonDBHelper(this);
            MusicDBHelper musicDB = new MusicDBHelper(this);
            BeatTypeDBHelper beatTypeDB = new BeatTypeDBHelper(this);
            MusicRecommendationDBHelper musicRecommendationDB = new MusicRecommendationDBHelper(this);
            JournalDBHelper journalDB = new JournalDBHelper(this);
            FavoriteMusicDBHelper favoriteMusicDB = new FavoriteMusicDBHelper(this);
            SQLiteDatabase db = configDB.GetDatabase();
            boolean dbExists = doesDatabaseExist(this, ConfigDBHelper.DATABASE_NAME);


            if (dbExists = false) {
                messageBox("Success", "Creating new database");
                configDB.DropTable();
                configDB.CreateTable();
                personDB.CreateTable();
               // musicDB.DropTable();
                musicDB.CreateTable();
                beatTypeDB.CreateTable();
                musicRecommendationDB.CreateTable();
                journalDB.CreateTable();
                favoriteMusicDB.CreateTable();
                messageBox("Success", "Tables created successfully");
            }
            else
            {
                //messageBox("Success", "Database already exists. Adding all music");
                int musicCount = 0;
                try {
                     musicCount = musicDB.getMusicsCount();
                    if (musicCount <= 0) {
                        musicDB.addAllMusic();
                    }
                }
                catch(Exception ex )
                {
                    messageBox("Success", "Creating new database");
                    configDB.DropTable();
                    configDB.CreateTable();
                    personDB.CreateTable();
                    // musicDB.DropTable();
                    musicDB.CreateTable();
                    beatTypeDB.CreateTable();
                    musicRecommendationDB.CreateTable();
                    journalDB.CreateTable();
                    favoriteMusicDB.CreateTable();
                    musicCount = musicDB.getMusicsCount();
                    if (musicCount <= 0) {
                        musicDB.addAllMusic();
                    }
                }
                //Teemp
                //SelectedPerson.PersonID = 2;
                //SelectedPerson.PersonName = "Suhas";
                //Get default person
               Config config= configDB.getConfig();
                if(config !=null && config.getCurrentPersonID() !=0)
                {
                    //messageBox("Selected person", "Selected person =" +  config.getCurrentPersonID() );
                    Cursor rs = personDB.getData(config.getCurrentPersonID());
                    if(rs !=null) {
                        rs.moveToFirst();
                        Person person = new Person();
                        String name = rs.getString(rs.getColumnIndex(PersonDBHelper.PERSONS_COLUMN_LASTNAME));
                        String issue = rs.getString(rs.getColumnIndex(PersonDBHelper.PERSONS_COLUMN_ISSUE));
                        SelectedPerson.PersonID = config.getCurrentPersonID();
                        SelectedPerson.PersonName = name;
                        person.setPersonID(SelectedPerson.PersonID);
                        person.setFirstName(name);
                        person.setIssue(issue);

                        MusicRecommender musicRecommender = new MusicRecommender(this);

                        List<Music> recomMusicList = musicRecommender.GetRecommendMusic(person);
                        //messageBox("Success", "Checking recommendation for person ID " + SelectedPerson.PersonID);
                        int musicRecommendationCount = musicRecommendationDB.getMusicRecommendationsCount(SelectedPerson.PersonID);
                        if(musicRecommendationCount <= 1)
                        {
                            //messageBox("Success", "Person doesn't have any recommendated music");
                            List<MusicRecommendation> personMusicRecommendationList = new ArrayList<MusicRecommendation>();

                            for(Music m: recomMusicList)
                            {
                                MusicRecommendation mRec = new MusicRecommendation();
                                mRec.setPersonID(SelectedPerson.PersonID );
                                mRec.setMusicID(m.getMusicID());
                                personMusicRecommendationList.add(mRec);
                            }

                            if(personMusicRecommendationList.size() > 0) {
                                //messageBox("Success", "Person recommended music count " + personMusicRecommendationList.size());
                                musicRecommendationDB.addAllMusicRecommendation(personMusicRecommendationList);
                            }
                        }
                        //messageBox("Selected person", "Selected person Name =" + name);
                    }
                    else {
                        messageBox("NULL", "Person rs is NULL");
                    }
                }


            }
        }
        catch(Exception e)
        {
            messageBox("Error occured", "Error in getdatabase "+ e.getMessage());
        }
        //beatTypeDB.CreateTable();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        super.onOptionsItemSelected(item);

        switch(item.getItemId()) {
            case R.id.item1:Bundle dataBundle = new Bundle();
                dataBundle.putInt("id", 0);

                if(SelectedPerson.PersonID !=0) {
                    Intent intent = new Intent(getApplicationContext(), Dashboard2Activity.class);
                    intent.putExtras(dataBundle);

                    startActivity(intent);
                }
                return true;
            case R.id.item2:Bundle dataBundle1 = new Bundle();
                dataBundle1.putInt("id", 0);

                Intent intent1 = new Intent(getApplicationContext(),AddPerson.class);
                intent1.putExtras(dataBundle1);

                startActivity(intent1);
                return true;
            case R.id.item3:Bundle dataBundle2 = new Bundle();
                dataBundle2.putInt("id", 0);

                Intent intent2 = new Intent(getApplicationContext(),DisplayPersons.class);
                intent2.putExtras(dataBundle2);

                startActivity(intent2);
                return true;
            case R.id.item4:Bundle dataBundle4 = new Bundle();
                dataBundle4.putInt("id", 0);

                Intent intent4 = new Intent(getApplicationContext(),DisplayMusic.class);
                intent4.putExtras(dataBundle4);

                startActivity(intent4);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }

    public boolean onKeyDown(int keycode, KeyEvent event) {
        if (keycode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(true);
        }
        return super.onKeyDown(keycode, event);
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

    private static boolean doesDatabaseExist(Context context, String dbName) {
        boolean dbExists = false;
        try {
            File dbFile = context.getDatabasePath(dbName);
            ConfigDBHelper configDB = new ConfigDBHelper(context);
            SQLiteDatabase db = configDB.GetDatabase();
            dbExists =true;
        }
        catch(Exception ex)
        {
            dbExists = false;
        }
        return dbExists;
    }

}
