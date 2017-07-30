package com.themagicofmusic.beatrecognizer;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import com.themagicofmusic.database.BeatTypeDBHelper;

/**
 * TheMagicOfMusic
 * Created by Atharva Kasar on 5/29/2017.
 */
public class AddBeatType extends AppCompatActivity {

    int from_Where_I_Am_Coming = 0;
    private BeatTypeDBHelper mydb ;

    TextView id ;
    TextView code;
    TextView desc;

    int id_To_Update = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{
        setContentView(R.layout.activity_add_beat_type);

        id = (TextView) findViewById(R.id.editMusicID);
        code = (TextView) findViewById(R.id.editMusicName);
        desc = (TextView) findViewById(R.id.editMusicName);


        mydb = new BeatTypeDBHelper(this);

        Bundle extras = getIntent().getExtras();
        if(extras !=null) {
            int Value = extras.getInt("id");

            if(Value>0){
                //means this is the view part not the add contact part.
                BeatType beatType = mydb.getBeatType(Value);
                id_To_Update = Value;

                String code = beatType.getBeatTypeCode();
                String desc = beatType.getBeatTypeDesc();


                Button b = (Button)findViewById(R.id.btnStop);
                b.setVisibility(View.INVISIBLE);

                id.setText(code);
                id.setFocusable(false);
                id.setClickable(false);

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
                getMenuInflater().inflate(R.menu.display, menu);
            } else{
                getMenuInflater().inflate(R.menu.main_menu, menu);
            }
        }
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch(item.getItemId()) {
            case R.id.Edit_Beat:
                Button b = (Button)findViewById(R.id.btnStop);
                b.setVisibility(View.VISIBLE);
                id.setEnabled(true);
                id.setFocusableInTouchMode(true);
                id.setClickable(true);

                code.setEnabled(true);
                code.setFocusableInTouchMode(true);
                code.setClickable(true);

                desc.setEnabled(true);
                desc.setFocusableInTouchMode(true);
                desc.setClickable(true);

                return true;
            case R.id.Delete_Beat:

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("deleteContact")
                        .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                mydb.deleteBeatType(id_To_Update);
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

                return true;
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
                    BeatType beatType = new BeatType();
                    beatType.setBeatTypeID(id_To_Update);
                    beatType.setBeatTypeCode(code.getText().toString());
                    beatType.setBeatTypeDesc( desc.getText().toString());
                    if (mydb.updateBeatType(beatType) == 1) {
                        Toast.makeText(getApplicationContext(), "Updated", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), DisplayBeats.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(), "not Updated", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    BeatType beatType = new BeatType();
                    beatType.setBeatTypeID(id_To_Update);
                    beatType.setBeatTypeCode(code.getText().toString());
                    beatType.setBeatTypeDesc( desc.getText().toString());
                    mydb.addBeatType(beatType);

                    Toast.makeText(getApplicationContext(), "done",
                            Toast.LENGTH_SHORT).show();

                    messageBox("Successful", "BeatType added successfully");
                    Intent intent = new Intent(getApplicationContext(), DisplayBeats.class);
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
