package com.themagicofmusic.beatrecognizer;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.themagicofmusic.model.*;
import com.themagicofmusic.database.*;

public class AddMusic extends AppCompatActivity {

    int from_Where_I_Am_Coming = 0;
    private MusicDBHelper mydb ;

    TextView id ;
    TextView name;
    TextView desc;
    TextView beatTypeCd;
    TextView location;

    int id_To_Update = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{
        setContentView(R.layout.activity_add_music);

        id = (TextView) findViewById(R.id.editTextMusicID);
        name = (TextView) findViewById(R.id.editTextMusicName);
        desc = (TextView) findViewById(R.id.editTextMusicDetail);
        beatTypeCd = (TextView) findViewById(R.id.editTextBeatType);
        location = (TextView) findViewById(R.id.editTextMusicLocation);

        mydb = new MusicDBHelper(this);

        Bundle extras = getIntent().getExtras();
        if(extras !=null) {
            int Value = extras.getInt("id");

            if(Value>0){
                //means this is the view part not the add contact part.
                Music Music = mydb.getMusic(Value);
                id_To_Update = Value;

                String code = Music.getMusicName();
                String desc = Music.getMusicDetail();
                String beatType = Music.getBeatTypeCode();
                String musiclocation = Music.getMusicLocation();

                Button b = (Button)findViewById(R.id.btnStop);
                b.setVisibility(View.INVISIBLE);

                id.setText(code);
                id.setFocusable(false);
                id.setClickable(false);

                name.setText(desc);
                name.setFocusable(false);
                name.setClickable(false);

                beatTypeCd.setText(beatType);
                beatTypeCd.setFocusable(false);
                beatTypeCd.setClickable(false);

                location.setText(musiclocation);
                location.setFocusable(false);
                location.setClickable(false);
            }
        }
        }
        catch(Exception e)
        {
            messageBox("Error occured", e.getMessage());
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
                getMenuInflater().inflate(R.menu.main_menu, menu);
            }
        }
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch(item.getItemId()) {
           /* case R.id.Edit_Music:
                Button b = (Button)findViewById(R.id.button1);
                b.setVisibility(View.VISIBLE);
                id.setEnabled(true);
                id.setFocusableInTouchMode(true);
                id.setClickable(true);

                name.setEnabled(true);
                name.setFocusableInTouchMode(true);
                name.setClickable(true);

                desc.setEnabled(true);
                desc.setFocusableInTouchMode(true);
                desc.setClickable(true);

                return true;
            case R.id.Delete_Music:

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("deleteMusic")
                        .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                mydb.deleteMusic(id_To_Update);
                                Toast.makeText(getApplicationContext(), "Deleted Successfully",
                                        Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(),BeatRecognizerMain.class);
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
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    public void run(View view) {
        try {
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                int Value = extras.getInt("id");
                if (Value > 0) {
                    Music Music = new Music();
                    Music.setMusicID(id_To_Update);
                    Music.setMusicName(name.getText().toString());
                    Music.setMusicDetail( desc.getText().toString());
                    Music.setBeatTypeCode( beatTypeCd.getText().toString());
                    Music.setMusicLocation( location.getText().toString());
                    if (mydb.updateMusic(Music)) {
                        Toast.makeText(getApplicationContext(), "Updated", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), DisplayMusic.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(), "not Updated", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Music Music = new Music();
                    Music.setMusicID(id_To_Update);
                    Music.setMusicName(name.getText().toString());
                    Music.setMusicDetail( desc.getText().toString());
                    Music.setBeatTypeCode( beatTypeCd.getText().toString());
                    Music.setMusicLocation( location.getText().toString());
                    mydb.addMusic(Music);

                    Toast.makeText(getApplicationContext(), "done",
                            Toast.LENGTH_SHORT).show();

                    messageBox("Successful", "Music added successfully");
                    Intent intent = new Intent(getApplicationContext(), DisplayMusic.class);
                    startActivity(intent);
                }
            }
        }
        catch(Exception e)
        {
            messageBox("Error occured", e.getMessage());
        }
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
