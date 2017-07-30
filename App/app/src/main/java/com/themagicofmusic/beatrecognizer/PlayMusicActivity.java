package com.themagicofmusic.beatrecognizer;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.SyncStateContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;
import android.media.*;
import java.util.*;

import com.themagicofmusic.model.*;

import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.TagException;
import org.jaudiotagger.tag.id3.AbstractID3v2Frame;
import org.jaudiotagger.tag.id3.AbstractTagFrameBody;
import org.jaudiotagger.tag.id3.ID3v24Tag;
import org.jaudiotagger.tag.id3.framebody.FrameBodyTXXX;
/**
 * TheMagicOfMusic - TheraBeat
 * Created by Atharva Kasar
 */
public class PlayMusicActivity extends AppCompatActivity {
    MediaPlayer mediaPalyer;
    List<Music> musicList = new ArrayList<Music>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            //GetSongs();
            checkPermission();
            setContentView(R.layout.activity_play_music);
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

    private void messageBox(String method, String message)
    {
        AlertDialog.Builder messageBox = new AlertDialog.Builder(this);
        messageBox.setTitle(method);
        messageBox.setMessage(message);
        messageBox.setCancelable(false);
        messageBox.setNeutralButton("OK", null);
        messageBox.show();
    }

    public void btnPlayAutismVideoClick(View v)
    {

        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=ztiA3F6WqG4")));

    }

    public void btnExtractMusicFeatureClick(View v)
    {
        Intent intent = new Intent(getApplicationContext(), MusicFeatureActivity.class);
        startActivity(intent);
    }

    public void btnDisplayPlayStoreMusicClick(View v)
    {
        Intent intent = new Intent(getApplicationContext(), DisplayPlayStoreMusicActivity.class);
        startActivity(intent);
    }
    public void run(View view) {
        String fileName = null;
        //MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.c);
        //mediaPlayer.start(); // no need to call prepare(); create() does that for you
        try {
            /*messageBox("success", "Now playing Youtube music");
            String url = "https://www.youtube.com/watch?v=IiN2d576-ow"; // your URL here
            MediaPlayer mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setDataSource(url);
            mediaPlayer.prepare(); // might take long! (for buffering, etc)
            mediaPlayer.start();*/

            mediaPalyer= MediaPlayer.create(this,R.raw.naina);
            mediaPalyer.start();

        }
        catch(Exception e){
            messageBox("Error occured", e.getMessage());
        }
}

    public void btnPlayMusicClick(View v)
    {
        messageBox("Click", "Click on Play Music");
        String fileName = null;
        //MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.c);
        //mediaPlayer.start(); // no need to call prepare(); create() does that for you
        try {
            /*messageBox("success", "Now playing Youtube music");
            String url = "https://www.youtube.com/watch?v=IiN2d576-ow"; // your URL here
            MediaPlayer mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setDataSource(url);
            mediaPlayer.prepare(); // might take long! (for buffering, etc)
            mediaPlayer.start();*/

            mediaPalyer= MediaPlayer.create(this,R.raw.naina);
            mediaPalyer.start();

        }
        catch(Exception e){  messageBox("Error occured", e.getMessage());   }

    }

  /*  private void updateReplayGain(int mediaid, String data) {
        ContentValues values = new ContentValues();
        MP3File mp3File;
        try {
            mp3File = new MP3File(new File(data));
            ID3v24Tag v24Tag = mp3File.getID3v2TagAsv24();

            Iterator i = v24Tag.getFrameOfType("TXXX");
            while (i.hasNext()) {
                Object obj = i.next();
                if (obj instanceof AbstractID3v2Frame) {
                    AbstractTagFrameBody af = ((AbstractID3v2Frame) obj)
                            .getBody();
                    if (af instanceof FrameBodyTXXX) {
                        FrameBodyTXXX fb = (FrameBodyTXXX) af;
                        if (fb.getDescription().equals("replaygain_track_peak")) {
                            values.put(Loudness.REPLAYGAIN_TRACK_PEAK, Utils
                                    .parseFloat(fb
                                            .getTextWithoutTrailingNulls()));
                        } else if (fb.getDescription().equals(
                                "replaygain_track_gain")) {
                            values.put(Loudness.REPLAYGAIN_TRACK_GAIN, Utils
                                    .parseFloat(fb
                                            .getTextWithoutTrailingNulls()));
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TagException e) {
            e.printStackTrace();
        } catch (ReadOnlyFileException e) {
            e.printStackTrace();
        } catch (InvalidAudioFrameException e) {
            e.printStackTrace();
        }
    }*/

    public void GetSongs(){
        ContentResolver contentResolver = getContentResolver();
        Uri songUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor songCursor = contentResolver.query(songUri, null, null, null, null);

        if(songCursor != null && songCursor.moveToFirst())
        {
            int songId = songCursor.getColumnIndex(MediaStore.Audio.Media._ID);
            int songTitle = songCursor.getColumnIndex(MediaStore.Audio.Media.TITLE);

            do {
                int currentId = songCursor.getInt(songId);
                String currentTitle = songCursor.getString(songTitle);
                musicList.add(new Music(currentId, currentTitle));
            } while(songCursor.moveToNext());
        }
        messageBox("Music count", "Total music "+ musicList.size());
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
