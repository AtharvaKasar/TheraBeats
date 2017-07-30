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

import com.themagicofmusic.database.JournalDBHelper;
import com.themagicofmusic.model.*;

public class JournalDetailActivity extends AppCompatActivity {

    int from_Where_I_Am_Coming = 0;
    private JournalDBHelper mydb ;

    TextView journalID ;
    TextView journalDetail;
    TextView musicListened;
    TextView hobby;
    TextView issue;
    TextView place;
    int id_To_Update = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_journal_detail);

            journalID = (TextView) findViewById(R.id.editJournalID);
            journalDetail = (TextView) findViewById(R.id.editJournalDetail);
            musicListened = (TextView) findViewById(R.id.editMusicListened);
            mydb = new JournalDBHelper(this);

            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                int Value = extras.getInt("id");
                //messageBox("ID", "Journal detail ID " +Value);
                if (Value > 0) {
                    //means this is the view part not the add contact part.
                    Journal journal = mydb.getJournal(Value);
                    id_To_Update = Value;


                    //Button b = (Button) findViewById(R.id.button1);
                    //b.setVisibility(View.INVISIBLE);

                    journalID.setText( String.valueOf(journal.getJournalID()));
                    journalID.setFocusable(false);
                    journalID.setClickable(false);

                    journalDetail.setText( journal.getJournalDetail());
                    journalDetail.setFocusable(false);
                    journalDetail.setClickable(false);

                    musicListened.setText( journal.getMusicListened());
                    musicListened.setFocusable(false);
                    musicListened.setClickable(false);
                }
            }
        }
        catch(Exception e)
        {
            messageBox("Error occured in Journal detail activity", "Journal detail" +e.getMessage());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        Bundle extras = getIntent().getExtras();

        if(extras !=null) {
            int Value = extras.getInt("id");
            if(Value>0){
                getMenuInflater().inflate(R.menu.menu_journal, menu);
            } else{
                getMenuInflater().inflate(R.menu.main_menu, menu);
            }
        }
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch(item.getItemId()) {
            case R.id.Edit_Journal:
                Button b = (Button)findViewById(R.id.btnStop);
                b.setVisibility(View.VISIBLE);
                journalDetail.setEnabled(true);
                journalDetail.setFocusableInTouchMode(true);
                journalDetail.setClickable(true);

                musicListened.setEnabled(true);
                musicListened.setFocusableInTouchMode(true);
                musicListened.setClickable(true);

                return true;

            case R.id.Delete_Journal:

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("deleteContact")
                        .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                mydb.deleteJournal(id_To_Update);
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

                return true;

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
                Journal updateJournal = new Journal();
                updateJournal.setJournalID(id_To_Update);
                updateJournal.setJournalDetail(journalDetail.getText().toString());
                updateJournal.setMusicListened(musicListened.getText().toString());
                if(mydb.updateJournal(updateJournal)){
                    Toast.makeText(getApplicationContext(), "Updated", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(),Dashboard2Activity.class);
                    startActivity(intent);
                } else{
                    Toast.makeText(getApplicationContext(), "not Updated", Toast.LENGTH_SHORT).show();
                }
            } else{
                Journal newJournal = new Journal();
                //neweMusic.setMusicID(id_To_Update);
                newJournal.setJournalDetail(journalDetail.getText().toString());
                newJournal.setMusicListened(musicListened.getText().toString());
                //newJournal.setBeatTypeCode("XX");
                if(mydb.addJournal(newJournal)){
                    Toast.makeText(getApplicationContext(), "done",
                            Toast.LENGTH_SHORT).show();
                } else{
                    Toast.makeText(getApplicationContext(), "not done",
                            Toast.LENGTH_SHORT).show();
                }
                Intent intent = new Intent(getApplicationContext(),Dashboard2Activity.class);
                startActivity(intent);
            }
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
