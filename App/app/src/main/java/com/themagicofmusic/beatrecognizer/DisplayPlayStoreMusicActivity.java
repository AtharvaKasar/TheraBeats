package com.themagicofmusic.beatrecognizer;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.themagicofmusic.database.MusicDBHelper;
import com.themagicofmusic.model.Constants;
import com.themagicofmusic.model.Music;

import java.util.ArrayList;
import java.util.List;
/**
 * TheMagicOfMusic - TheraBeat
 * Created by Atharva Kasar
 */
public class DisplayPlayStoreMusicActivity extends AppCompatActivity {
    public final static String EXTRA_MESSAGE = "MESSAGE";
    private ListView obj;
    MusicDBHelper mydb;
    List<Music> MusicList = new ArrayList<Music>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        //Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        checkPermission();
        setContentView(R.layout.activity_display_play_store_music);

        try {

            ArrayList<String> Musics = new ArrayList<String>();
            if(MusicList.size() > 0)
            {
                for(Music Music : MusicList)
                {
                    Musics.add( Music.getMusicName());
                }
            }
            ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, Musics);

            obj = (ListView)findViewById(R.id.listView1);
            obj.setAdapter(arrayAdapter);
            obj.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
                    // TODO Auto-generated method stub
                    Object musicName = arg0.getAdapter().getItem(arg2);
                    int id_To_Search = arg2 + 1;
                    messageBox("Click on music", "Click on music " + musicName);
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + musicName.toString())));
                   // MediaPlayer mediaPalyer= MediaPlayer.create(this,R.raw.naina);
                   // mediaPalyer.start();
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
        getMenuInflater().inflate(R.menu.menu_playstoremusic, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        super.onOptionsItemSelected(item);

        switch(item.getItemId()) {

            case R.id.Play_Music:Bundle dataBundle4 = new Bundle();
                dataBundle4.putInt("id", 0);

                Intent intent4 = new Intent(getApplicationContext(),DisplayMusic.class);
                intent4.putExtras(dataBundle4);

                startActivity(intent4);
                return true;

            case R.id.Main:

                Intent intent6 = new Intent(getApplicationContext(), Dashboard2Activity.class);
                //intent5.putExtras(dataBundle5);

                startActivity(intent6);
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

    public void GetSongs(){
        ContentResolver contentResolver = getContentResolver();
        Uri songUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor songCursor = contentResolver.query(songUri, null, null, null, null);

        if(songCursor != null && songCursor.moveToFirst())
        {
            int songId = songCursor.getColumnIndex(MediaStore.Audio.Media._ID);
            int songTitle = songCursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int songArtist = songCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media.ARTIST);

            do {
                int currentId = songCursor.getInt(songId);
                String currentTitle = songCursor.getString(songTitle);
                String currentArtist = songCursor.getString(songArtist);
                MusicList.add(new Music(currentId, currentTitle, currentArtist));
            } while(songCursor.moveToNext());
        }

    }

    private void checkPermission(){
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    Constants.WRITE_EXTERNAL_STORAGE);
        } else {
            GetSongs();

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {

            case Constants.WRITE_EXTERNAL_STORAGE:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    GetSongs();
                }
                break;
            case Constants.READ_EXTERNAL_STORAGE:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    GetSongs();
                }
                break;
            default:
                break;
        }
    }
}
