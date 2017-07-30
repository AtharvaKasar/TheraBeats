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

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * TheMagicOfMusic - ThreBeat
 * Created by Atharva Kasar
 */
public class AddJournal extends AppCompatActivity {

    int from_Where_I_Am_Coming = 0;
    private JournalDBHelper mydb ;

    TextView journalDate ;
    TextView musicListened;
    TextView journalDetail;
    int id_To_Update = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try
        {
            setContentView(R.layout.activity_add_journal);

            journalDate = (TextView) findViewById(R.id.editJournalDate);
            musicListened = (TextView) findViewById(R.id.editMusicName);
            journalDetail = (TextView) findViewById(R.id.editJournalDetail);

            mydb = new JournalDBHelper(this);

            Bundle extras = getIntent().getExtras();
            if(extras !=null) {
                int Value = extras.getInt("id");

                if(Value>0){
                    //means this is the view part not the add contact part.
                    Journal journal = mydb.getJournal(Value);
                    id_To_Update = Value;


                    String jDate = journal.getJournalDate();
                    String jDetail = journal.getJournalDetail();
                    String jMusic = journal.getMusicListened();

                    Button b = (Button)findViewById(R.id.btnStop);
                    b.setVisibility(View.INVISIBLE);

                    journalDate.setText((CharSequence)jDate);
                    journalDate.setFocusable(false);
                    journalDate.setClickable(false);

                }
                else
                {
                    Calendar c = Calendar.getInstance();
                    System.out.println("Current time => " + c.getTime());

                    SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
                    String jDate = df.format(c.getTime());

                    journalDate.setText((CharSequence)jDate);
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
            case R.id.Edit_Journal:
                Button b = (Button)findViewById(R.id.btnStop);
                b.setVisibility(View.VISIBLE);
                journalDate.setEnabled(true);
                journalDate.setFocusableInTouchMode(true);
                journalDate.setClickable(true);

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
                    messageBox("Update Journal", "Updating journal for a person " + SelectedPerson.PersonID);
                    Journal updJournal = new Journal();
                    updJournal.setJournalID(id_To_Update);
                    updJournal.setPersonID(SelectedPerson.PersonID);
                    updJournal.setJournalDate(journalDate.getText().toString());
                    updJournal.setJournalDetail(journalDetail.getText().toString());
                    updJournal.setMusicListened(musicListened.getText().toString());
                    mydb.updateJournal(updJournal);
                    Toast.makeText(getApplicationContext(), "Updated", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), Dashboard2Activity.class);
                    startActivity(intent);

                } else {
                    messageBox("Add Journal", "Adding new journal for a person " + SelectedPerson.PersonID + "Journal Date " + journalDate.getText());
                    Journal newJournal = new Journal();
                    newJournal.setPersonID(SelectedPerson.PersonID);
                    newJournal.setJournalDate(journalDate.getText().toString());
                    newJournal.setJournalDetail(journalDetail.getText().toString());
                    newJournal.setMusicListened(musicListened.getText().toString());
                    mydb.addJournal(newJournal);
                    Toast.makeText(getApplicationContext(), "done",
                            Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(getApplicationContext(), Dashboard2Activity.class);
                    startActivity(intent);
                }
            }
            else
            {
                messageBox("Add Journal", "Adding new journal for a person " + SelectedPerson.PersonID);
                Journal newJournal = new Journal();
                newJournal.setPersonID(SelectedPerson.PersonID);
                newJournal.setJournalDate(journalDate.getText().toString());
                newJournal.setJournalDetail(journalDetail.getText().toString());
                newJournal.setMusicListened(musicListened.getText().toString());
                mydb.addJournal(newJournal);
                Toast.makeText(getApplicationContext(), "done",
                        Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getApplicationContext(), Dashboard2Activity.class);
                startActivity(intent);
            }
            messageBox("Add Journal", "Add journal successful");
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
