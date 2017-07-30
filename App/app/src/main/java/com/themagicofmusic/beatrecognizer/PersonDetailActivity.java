package com.themagicofmusic.beatrecognizer;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.themagicofmusic.Algorithms.MusicRecommender;
import com.themagicofmusic.database.*;
import com.themagicofmusic.model.*;

import java.util.ArrayList;
import java.util.List;

public class PersonDetailActivity extends AppCompatActivity {

    int from_Where_I_Am_Coming = 0;
    private PersonDBHelper mydb ;

    TextView name ;
    TextView age;
    TextView hobby;
    TextView issue;
    TextView place;
    int id_To_Update = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_person_detail);

            name = (TextView) findViewById(R.id.editName);
            age = (TextView) findViewById(R.id.editMusicName);
            hobby = (TextView) findViewById(R.id.editHobby);
            issue = (TextView) findViewById(R.id.editIssue);


            mydb = new PersonDBHelper(this);

            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                int Value = extras.getInt("id");

                if (Value > 0) {
                    //means this is the view part not the add contact part.
                    Cursor rs = mydb.getData(Value);
                    id_To_Update = Value;
                    rs.moveToFirst();

                    String nam = rs.getString(rs.getColumnIndex(PersonDBHelper.PERSONS_COLUMN_LASTNAME));
                    String age = rs.getString(rs.getColumnIndex(PersonDBHelper.PERSONS_COLUMN_AGE));
                    String hob = rs.getString(rs.getColumnIndex(PersonDBHelper.PERSONS_COLUMN_HOBBY));
                    String iss = rs.getString(rs.getColumnIndex(PersonDBHelper.PERSONS_COLUMN_ISSUE));


                    if (!rs.isClosed()) {
                        rs.close();
                    }

                    //Button b = (Button) findViewById(R.id.button1);
                    //b.setVisibility(View.INVISIBLE);

                    name.setText((CharSequence) nam);
                    name.setFocusable(false);
                    name.setClickable(false);

                    hobby.setText((CharSequence) hob);
                    hobby.setFocusable(false);
                    hobby.setClickable(false);

                    issue.setText((CharSequence) iss);
                    issue.setFocusable(false);
                    issue.setClickable(false);
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
        try {
            super.onOptionsItemSelected(item);
            switch (item.getItemId()) {
                case R.id.Edit_Person:
                    Button b = (Button) findViewById(R.id.btnStop);
                    b.setVisibility(View.VISIBLE);
                    name.setEnabled(true);
                    name.setFocusableInTouchMode(true);
                    name.setClickable(true);

                    age.setEnabled(true);
                    age.setFocusableInTouchMode(true);
                    age.setClickable(true);

                    hobby.setEnabled(true);
                    hobby.setFocusableInTouchMode(true);
                    hobby.setClickable(true);

                    issue.setEnabled(true);
                    issue.setFocusableInTouchMode(true);
                    issue.setClickable(true);

                    place.setEnabled(true);
                    place.setFocusableInTouchMode(true);
                    place.setClickable(true);

                    return true;
                case R.id.Default_Person: {
                   // messageBox("Default person", "Setting default " + id_To_Update);
                    ConfigDBHelper configDB = new ConfigDBHelper(this);
                    Config updateConfig = configDB.getConfig();

                    if(updateConfig == null)
                    {
                        updateConfig = new Config(id_To_Update, id_To_Update);
                    }
                    updateConfig.setCurrentPersonID(id_To_Update);
                    int count = configDB.numberOfRows();
                    if (count == 0) {
                        //messageBox("Insert", "Adding config record");
                        configDB.addConfig(updateConfig);
                        SelectedPerson.PersonID = id_To_Update;
                    } else {
                        //messageBox("Update", "Updating config record");
                        configDB.updateConfig(updateConfig);
                        SelectedPerson.PersonID = id_To_Update;
                    }

                    PersonDBHelper personDB = new PersonDBHelper(this);
                    Cursor rs = personDB.getData(id_To_Update);
                    if(rs !=null) {
                       // messageBox("Insert", "Adding recommendation");
                        Person person = new Person();
                        String name = rs.getString(rs.getColumnIndex(PersonDBHelper.PERSONS_COLUMN_LASTNAME));
                        String issue = rs.getString(rs.getColumnIndex(PersonDBHelper.PERSONS_COLUMN_ISSUE));
                        SelectedPerson.PersonName = name;
                        person.setPersonID(SelectedPerson.PersonID);
                        person.setFirstName(name);
                        person.setIssue(issue);

                        MusicRecommendationDBHelper musicRecommendationDB = new MusicRecommendationDBHelper(this);
                        MusicRecommender musicRecommender = new MusicRecommender(this);

                        List<Music> recomMusicList = musicRecommender.GetRecommendMusic(person);
                        int musicRecommendationCount = musicRecommendationDB.getMusicRecommendationsCount(SelectedPerson.PersonID);
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
                        }
                    }
                    else{
                        messageBox("Failure", "Person not found");
                    }
                    Toast.makeText(getApplicationContext(), "Default person set Successfully",
                            Toast.LENGTH_SHORT).show();
                    return true;
                }
                case R.id.Delete_Person:

                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage("deleteContact")
                            .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    mydb.deletePerson(id_To_Update);
                                    Toast.makeText(getApplicationContext(), "Deleted Successfully",
                                            Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getApplicationContext(), BeatRecognizerMain.class);
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

                    Intent intent6 = new Intent(getApplicationContext(), Dashboard2Activity.class);
                    //intent5.putExtras(dataBundle5);

                    startActivity(intent6);
                    return true;
                default:
                    return super.onOptionsItemSelected(item);

            }
        }
        catch(Exception ex)
        {
            messageBox("Error occured", ex.getMessage());
            throw ex;
        }
    }

    public void run(View view) {
        Bundle extras = getIntent().getExtras();
        if(extras !=null) {
            int Value = extras.getInt("id");
            if(Value>0){
                if(mydb.updatePerson(id_To_Update,name.getText().toString(),
                        Integer.parseInt( age.getText().toString()), hobby.getText().toString(),
                        issue.getText().toString())){
                    Toast.makeText(getApplicationContext(), "Updated", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(),BeatRecognizerMain.class);
                    startActivity(intent);
                } else{
                    Toast.makeText(getApplicationContext(), "not Updated", Toast.LENGTH_SHORT).show();
                }
            } else{
                if(mydb.insertPerson(name.getText().toString(),name.getText().toString(),Integer.parseInt( age.getText().toString()),
                        hobby.getText().toString(),
                        issue.getText().toString())){
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
