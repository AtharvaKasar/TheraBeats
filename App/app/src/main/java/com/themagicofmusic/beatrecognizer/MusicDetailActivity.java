package com.themagicofmusic.beatrecognizer;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.themagicofmusic.database.ConfigDBHelper;
import com.themagicofmusic.database.*;
import com.themagicofmusic.model.*;
import com.themagicofmusic.model.Music;
import com.themagicofmusic.model.SelectedPerson;
/**
 * TheMagicOfMusic - TheraBeat
 * Created by Atharva Kasar
 */
public class MusicDetailActivity extends AppCompatActivity {

    int from_Where_I_Am_Coming = 0;
    private MusicDBHelper mydb ;
    private FavoriteMusicDBHelper favMusicdb ;
    TextView musicID ;
    TextView musicName;
    TextView hobby;
    TextView issue;
    TextView place;
    int id_To_Update = 0;
    MediaPlayer mediaPalyer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_music_detail);

            musicID = (TextView) findViewById(R.id.editMusicID);
            musicName = (TextView) findViewById(R.id.editMusicName);


            mydb = new MusicDBHelper(this);
            favMusicdb = new FavoriteMusicDBHelper(this);

            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                int Value = extras.getInt("id");
                //messageBox("ID", "Music detail ID " +Value);
                if (Value > 0) {
                    //means this is the view part not the add contact part.
                    Music music = mydb.getMusic(Value);
                    id_To_Update = Value;


                    //Button b = (Button) findViewById(R.id.button1);
                    //b.setVisibility(View.INVISIBLE);

                    musicID.setText( String.valueOf(id_To_Update));
                    musicID.setFocusable(false);
                    musicID.setClickable(false);

                    musicName.setText( (CharSequence)music.getMusicName());
                    musicName.setFocusable(false);
                    musicName.setClickable(false);

                }
            }
        }
        catch(Exception e)
        {
            messageBox("Error occured in music detail activity", "Music detail" +e.getMessage());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        Bundle extras = getIntent().getExtras();

        if(extras !=null) {
            int Value = extras.getInt("id");
            if(Value>0){
                getMenuInflater().inflate(R.menu.menu_music, menu);
            } else{
                getMenuInflater().inflate(R.menu.menu_music, menu);
            }
        }
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch(item.getItemId()) {
            /*case R.id.Edit_Music:
                Button b = (Button)findViewById(R.id.button1);
                b.setVisibility(View.VISIBLE);
                musicID.setEnabled(true);
                musicID.setFocusableInTouchMode(true);
                musicID.setClickable(true);



                return true;*/
            case R.id.Default_Person:
                messageBox("Default person", "Setting default " + id_To_Update);
                ConfigDBHelper configDB = new ConfigDBHelper(this);
                Config updateConfig = configDB.getConfig();

                updateConfig.setCurrentPersonID(id_To_Update);
                configDB.updateConfig(updateConfig);
                SelectedPerson.PersonID = id_To_Update;
                Toast.makeText(getApplicationContext(), "Default person set Successfully",
                        Toast.LENGTH_SHORT).show();
                return true;
            /*case R.id.Delete_Music:

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("delete Music")
                        .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                mydb.deleteMusic(id_To_Update);
                                Toast.makeText(getApplicationContext(), "Deleted Successfully",
                                        Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(),Dashboard2Activity.class);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("no", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User cancelled the dialog
                            }
                        });

                AlertDialog d = builder.create();
                d.setTitle("Are you sure");
                d.show();

                return true;*/

            case R.id.Main:

                Intent intent6 = new Intent(getApplicationContext(),Dashboard2Activity.class);
                //intent5.putExtras(dataBundle5);

                startActivity(intent6);
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    public void run(View view) {
        Bundle extras = getIntent().getExtras();
        if(extras !=null) {
            int Value = extras.getInt("id");
            if(Value>0){
                Music updateMusic = new Music();
                updateMusic.setMusicID(id_To_Update);
                updateMusic.setMusicName(musicID.getText().toString());
                if(mydb.updateMusic(updateMusic)){
                    Toast.makeText(getApplicationContext(), "Updated", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(),BeatRecognizerMain.class);
                    startActivity(intent);
                } else{
                    Toast.makeText(getApplicationContext(), "not Updated", Toast.LENGTH_SHORT).show();
                }
            } else{
                Music newMusic = new Music();
                //neweMusic.setMusicID(id_To_Update);
                newMusic.setMusicName(musicID.getText().toString());
                newMusic.setBeatTypeCode("XX");
                if(mydb.addMusic(newMusic)){
                    Toast.makeText(getApplicationContext(), "done",
                            Toast.LENGTH_SHORT).show();
                } else{
                    Toast.makeText(getApplicationContext(), "not done",
                            Toast.LENGTH_SHORT).show();
                }
                Intent intent = new Intent(getApplicationContext(),BeatRecognizerMain.class);
                startActivity(intent);
            }
        }
    }

    public void btnAddFavoriteMusicClick(View v)
    {
        try {
            //messageBox("Error occured in music detail activity", "Adding Fav music " + id_To_Update + " For personID " +SelectedPerson.PersonID );

            boolean favMusicExists = favMusicdb.FavoriteMusicExists(SelectedPerson.PersonID, id_To_Update);
            if(favMusicExists == false) {
                FavoriteMusic favMusic = new FavoriteMusic();
                favMusic.setMusicID(id_To_Update);
                favMusic.setPersonID(SelectedPerson.PersonID);
                if (favMusicdb.addFavoriteMusic(favMusic)) {
                    Toast.makeText(getApplicationContext(), "Updated", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), DisplayFavoriteMusic.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "not Updated", Toast.LENGTH_SHORT).show();
                }
            }
        }
        catch(Exception ex)
        {
            messageBox("Error occured in music detail activity", ex.getMessage());
        }

    }

    public void btnPlayMusicClick(View view) {
        String fileName = null;
        //MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.c);
        //mediaPlayer.start(); // no need to call prepare(); create() does that for you
        try {
             String musicCode = musicName.getText().toString();
             //messageBox("Success", "Music Code " + musicCode);
             playMusic(musicCode);

        } catch (Exception e) {
            messageBox("Error occured", e.getMessage());
        }
    }

    public void btnStopMusicClick(View view) {
        String fileName = null;
        //MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.c);
        //mediaPlayer.start(); // no need to call prepare(); create() does that for you
        try {
            if (mediaPalyer.isPlaying()) {

                mediaPalyer.stop();
            }

        } catch (Exception e) {
            messageBox("Error occured", e.getMessage());
        }
    }
    public void playMusic(String musicName)
    {
        if (musicName.equals("SOFTMUSIC1")){
            mediaPalyer= MediaPlayer.create(this, R.raw.mildmusic1);
        }
        else if (musicName.equals("SOFTMUSIC2")){
            mediaPalyer= MediaPlayer.create(this, R.raw.mildmusic2);
        }
        else if (musicName.equals("SOFTMUSIC3")){
            mediaPalyer= MediaPlayer.create(this, R.raw.mildmusic3);
        }
        else if (musicName.equals("SOFTMUSIC4")){
            mediaPalyer= MediaPlayer.create(this, R.raw.mildmusic3);
        }
        else if (musicName.equals("MILDMUSIC1")){
            mediaPalyer= MediaPlayer.create(this, R.raw.mediummusic1);
        }
        else if (musicName.equals("MILDMUSIC2")){
            mediaPalyer= MediaPlayer.create(this, R.raw.mediummusic2);
        }
        else if (musicName.equals("MILDMUSIC3")){
            mediaPalyer= MediaPlayer.create(this, R.raw.mediummusic3);
        }
        else if (musicName.equals("MILDMUSIC4")){
            mediaPalyer= MediaPlayer.create(this, R.raw.mediummusic3);
        }
        else if (musicName.equals("HARDMUSIC1")){
            mediaPalyer= MediaPlayer.create(this, R.raw.mediummusic3);
        }
        else if (musicName.equals("HARDMUSIC2")){
            mediaPalyer= MediaPlayer.create(this, R.raw.mediummusic3);
        }
        else if (musicName.equals("HARDMUSIC3")){
            mediaPalyer= MediaPlayer.create(this, R.raw.mediummusic3);
          }
        else if (musicName.equals("HARDMUSIC4")){
            mediaPalyer= MediaPlayer.create(this, R.raw.mediummusic3);

        }
        mediaPalyer.start();
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
