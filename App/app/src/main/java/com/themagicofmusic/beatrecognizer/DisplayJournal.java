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
import android.widget.TextView;
import android.widget.Toast;

import com.themagicofmusic.database.JournalDBHelper;
import com.themagicofmusic.model.Journal;
import com.themagicofmusic.model.SelectedPerson;


import java.util.ArrayList;
import java.util.List;
/**
 * TheMagicOfMusic - TheraBeat
 * Created by Atharva Kasar
 */
public class DisplayJournal extends AppCompatActivity {

    public final static String EXTRA_MESSAGE = "MESSAGE";
    private ListView obj;
    JournalDBHelper mydb;
    TextView selectedPersonName ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        setContentView(R.layout.activity_display_journal);

        //Temp
        int personID= SelectedPerson.PersonID;
        selectedPersonName = (TextView) findViewById(R.id.textPersonName);
        if (selectedPersonName ==null)
        {
            selectedPersonName.setText(SelectedPerson.PersonName);
        }
        try {
            //messageBox("Journal List", "JournalList for personID " + SelectedPerson.PersonID);
            mydb = new JournalDBHelper(this);
            List<Journal> journalList = mydb.getAllJournals(SelectedPerson.PersonID);
            ArrayList<String> Journals = new ArrayList<String>();
            //messageBox("Journal List", "JournalList size " + journalList.size());
            if(journalList.size() > 0)
            {
                for(Journal journal : journalList)
                {
                    Journals.add(journal.getJournalDetail());
                }
            }
            ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, Journals);

            if(arrayAdapter == null)
            {
                messageBox("Adpater", "Adater is NULL");
            }
            obj = (ListView)findViewById(R.id.listView1);
            obj.setAdapter(arrayAdapter);
            obj.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
                    // TODO Auto-generated method stub
                    String journalDetail = (String) arg0.getAdapter().getItem(arg2);
                    //Toast.makeText(this, item + " selected", Toast.LENGTH_LONG).show();
                    int journalID = mydb.getJournalID(SelectedPerson.PersonID,journalDetail);

                    if(journalID !=0) {
                        int id_To_Search = arg2 + 1;

                        Bundle dataBundle = new Bundle();
                        dataBundle.putInt("id", journalID);

                        Intent intent = new Intent(getApplicationContext(), JournalDetailActivity.class);

                        intent.putExtras(dataBundle);
                        startActivity(intent);
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
        getMenuInflater().inflate(R.menu.menu_journal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        super.onOptionsItemSelected(item);

        switch(item.getItemId()) {
            case R.id.Add_Journal:Bundle dataBundle = new Bundle();
                dataBundle.putInt("id", 0);

                Intent intent = new Intent(getApplicationContext(),AddJournal.class);
                intent.putExtras(dataBundle);

                startActivity(intent);
                return true;
            case R.id.Main:Bundle dataBundle1 = new Bundle();
                dataBundle1.putInt("id", 0);

                if(SelectedPerson.PersonID !=0) {
                    Intent intent1 = new Intent(getApplicationContext(), Dashboard2Activity.class);
                    intent1.putExtras(dataBundle1);

                    startActivity(intent1);
                }
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
