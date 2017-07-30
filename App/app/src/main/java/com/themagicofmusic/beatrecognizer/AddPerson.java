package com.themagicofmusic.beatrecognizer;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.themagicofmusic.database.PersonDBHelper;
import com.themagicofmusic.model.SelectedPerson;
/**
 * TheMagicOfMusic - ThreBeat
 * Created by Atharva Kasar
 */
public class AddPerson extends AppCompatActivity {

    int from_Where_I_Am_Coming = 0;
    private PersonDBHelper mydb ;


    TextView name ;
    TextView age;
    TextView hobby;
    Spinner issue;
    TextView place;
    int id_To_Update = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{
            setContentView(R.layout.activity_add_person);

            name = (TextView) findViewById(R.id.editMusicID);
            age = (TextView) findViewById(R.id.editAge);
            hobby = (TextView) findViewById(R.id.editMusicName);
            issue = (Spinner) findViewById(R.id.spinIssue);
            place = (TextView) findViewById(R.id.editTextCity);

            mydb = new PersonDBHelper(this);

            Bundle extras = getIntent().getExtras();
            if(extras !=null) {
                int Value = extras.getInt("id");

                if(Value>0){
                    //means this is the view part not the add contact part.
                    Cursor rs = mydb.getData(Value);
                    id_To_Update = Value;
                    rs.moveToFirst();

                    String nam = rs.getString(rs.getColumnIndex(PersonDBHelper.PERSONS_COLUMN_LASTNAME));
                    String firstName = rs.getString(rs.getColumnIndex(PersonDBHelper.PERSONS_COLUMN_FIRSTNAME));
                    String age = rs.getString(rs.getColumnIndex(PersonDBHelper.PERSONS_COLUMN_AGE));
                    String hobby = rs.getString(rs.getColumnIndex(PersonDBHelper.PERSONS_COLUMN_HOBBY));
                    String issue = rs.getString(rs.getColumnIndex(PersonDBHelper.PERSONS_COLUMN_ISSUE));


                    if (!rs.isClosed())  {
                        rs.close();
                    }
                    Button b = (Button)findViewById(R.id.btnStop);
                    b.setVisibility(View.INVISIBLE);

                    name.setText((CharSequence)nam);
                    name.setFocusable(false);
                    name.setClickable(false);



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
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
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

    public void run(View view) {
        try {
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                int Value = extras.getInt("id");
                if (Value > 0) {
                    if (mydb.updatePerson(id_To_Update, name.getText().toString(),
                            Integer.parseInt(age.getText().toString()), hobby.getText().toString(),
                            issue.getSelectedItem().toString())) {
                        Toast.makeText(getApplicationContext(), "Updated", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), BeatRecognizerMain.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(), "not Updated", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    //messageBox("Adding new person", "Adding new person " + name.getText().toString() + ", " + age.getText().toString() + ", " + hobby.getText().toString() + ", " + issue.getSelectedItem().toString());
                    if (mydb.insertPerson(name.getText().toString(),name.getText().toString(), Integer.parseInt(age.getText().toString()),
                            hobby.getText().toString(),
                            issue.getSelectedItem().toString())) {
                        Toast.makeText(getApplicationContext(), "done",
                                Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), DisplayPersons.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(), "not done",
                                Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), BeatRecognizerMain.class);
                        startActivity(intent);
                    }

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
