package com.themagicofmusic.beatrecognizer;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.themagicofmusic.database.FavoriteMusicDBHelper;
import com.themagicofmusic.database.MusicDBHelper;
import com.themagicofmusic.model.FavoriteMusic;
import com.themagicofmusic.model.Music;
import com.themagicofmusic.model.MusicRecommendation;
import com.themagicofmusic.model.SelectedPerson;

import java.util.ArrayList;
import java.util.List;
/**
 * TheMagicOfMusic - ThreBeat
 * Created by Atharva Kasar
 */
public class DisplayFavoriteMusic extends AppCompatActivity {

    public final static String EXTRA_MESSAGE = "MESSAGE";
    private ListView obj;
    FavoriteMusicDBHelper mydb;
    MusicDBHelper musicdb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        setContentView(R.layout.activity_display_favorite_music);

        //Temp
        int personID= SelectedPerson.PersonID;
        try {
            mydb = new FavoriteMusicDBHelper(this);
            musicdb = new MusicDBHelper(this);
            List<FavoriteMusic> FavMusicList = mydb.getAllFavoriteMusics(personID);
            ArrayList<String> MusicRecommendations = new ArrayList<String>();
            if(FavMusicList.size() > 0)
            {
                for(FavoriteMusic Music : FavMusicList)
                {
                    MusicRecommendations.add(String.valueOf( Music.getMusicName()));
                }
            }
            ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, MusicRecommendations);

            obj = (ListView)findViewById(R.id.listView1);
            obj.setAdapter(arrayAdapter);
            obj.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
                    // TODO Auto-generated method stub
                    int id_To_Search = arg2 + 1;
                    Object musicName = arg0.getAdapter().getItem(arg2);
                    if(musicName !=null) {
                        Music music = musicdb.getMusicByName(musicName.toString());

                        if (music != null) {
                            int musicID = music.getMusicID();
                            Bundle dataBundle = new Bundle();
                            dataBundle.putInt("id", musicID);

                            Intent intent = new Intent(getApplicationContext(), MusicDetailActivity.class);

                            intent.putExtras(dataBundle);
                            startActivity(intent);
                        }
                    }
                }
            });
        }
        catch(Exception e)
        {
            messageBox("Error occured", e.getMessage());
        }
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

}
