package com.themagicofmusic.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.themagicofmusic.model.Journal;

import java.util.ArrayList;
import java.util.List;

/**
 * TheMagicOfMusic - TheraBeat
 * Created by Atharva Kasar
 */

public class JournalDBHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    public static final String DATABASE_NAME = "BeatRecognizer1.db";
    // Contacts table name
    public static final String TABLE_JOURNAL = "Journal";
    // Journals Table Columns names
    public static final String JOURNAL_ID = "JournalID";
    public static final String PERSON_ID = "PersonID";
    public static final String JOURNAL_DATE = "JournalDate";
    public static final String JOURNAL_DETAIL = "JournalDetail";
    public static final String MUSIC_LISTENED = "JournalListened";
    public JournalDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_Journal_TABLE = "CREATE TABLE " + TABLE_JOURNAL + "("
                + JOURNAL_ID + " INTEGER PRIMARY KEY," + PERSON_ID + " TEXT,"
                + JOURNAL_DATE + " TEXT," +  JOURNAL_DETAIL + " TEXT," + MUSIC_LISTENED + " TEXT" + ")";
        db.execSQL(CREATE_Journal_TABLE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
// Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_JOURNAL);
// Creating tables again
        onCreate(db);
    }

    public void DropTable() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_JOURNAL);

    }
    public void CreateTable() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_JOURNAL);
        String CREATE_Journal_TABLE = "CREATE TABLE " + TABLE_JOURNAL + "("
                + JOURNAL_ID + " INTEGER PRIMARY KEY," + PERSON_ID + " TEXT,"
                + JOURNAL_DATE + " TEXT," +  JOURNAL_DETAIL + " TEXT," + MUSIC_LISTENED + " TEXT" + ")";
        db.execSQL(CREATE_Journal_TABLE);
    }
    // Adding new Journal
    public boolean addJournal(Journal Journal) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();

        /*db.execSQL("DROP TABLE IF EXISTS " + TABLE_JOURNAL);
        String CREATE_Journal_TABLE = "CREATE TABLE " + TABLE_JOURNAL + "("
                + JOURNAL_ID + " INTEGER PRIMARY KEY," + PERSON_ID + " TEXT,"
                + JOURNAL_DATE + " TEXT" +  JOURNAL_DETAIL + " TEXT," + MUSIC_LISTENED + " TEXT," + ")";*/

            ContentValues values = new ContentValues();
            //values.put(JOURNAL_ID, Journal.getJournalID()); // Journal code
            values.put(PERSON_ID, Journal.getPersonID()); // Journal code
            values.put(JOURNAL_DATE, Journal.getJournalDate()); // Journal desc
            values.put(JOURNAL_DETAIL, Journal.getJournalDetail()); // Journal desc
            values.put(MUSIC_LISTENED, Journal.getMusicListened()); // Journal desc
// Inserting Row
            db.insertOrThrow(TABLE_JOURNAL, null, values);
            db.close(); // Closing database connection
            return true;
        }
        catch(Exception ex)
        {
            throw ex;
        }
    }


    // Getting one Journal
    public Journal getJournal(int id) {

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_JOURNAL, new String[] { JOURNAL_ID,
                        PERSON_ID, JOURNAL_DATE,JOURNAL_DETAIL, MUSIC_LISTENED }, JOURNAL_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        Journal journal = new Journal();

        journal.setJournalID(cursor.getInt(0));
        journal.setPersonID(cursor.getInt(1));
        journal.setJournalDate(cursor.getString(2));
        journal.setJournalDetail(cursor.getString(3));
        journal.setMusicListened(cursor.getString(4));
// return Journal
        return journal;
    }

    // Getting All Journals
    public List<Journal> getAllJournals(int personID) {


        List<Journal> JournalList = new ArrayList<Journal>();
// Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_JOURNAL + " Where PersonID = " + personID + " Order by JournalID desc";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
// looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Journal journal = new Journal();
                journal.setJournalID(Integer.parseInt(cursor.getString(0)));
                journal.setJournalID(cursor.getInt(0));
                journal.setPersonID(cursor.getInt(1));
                journal.setJournalDate(cursor.getString(2));
                journal.setJournalDetail(cursor.getString(3));
                journal.setMusicListened(cursor.getString(4));
// Adding contact to list
                JournalList.add(journal);
            } while (cursor.moveToNext());
        }
// return contact list
        return JournalList;
    }

    public int getJournalID(int personID, String journalDetail) {

        int journalID = 0;
        String selectQuery = "SELECT JournalID FROM " + TABLE_JOURNAL + " Where PersonID = " + personID + " AND JournalDetail = '" + journalDetail + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
// looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                journalID = cursor.getInt(0);
            } while (cursor.moveToNext());
        }
        return journalID;
    }

    // Getting Journals Count
    public int getJournalsCount(int personID) {
        String countQuery = "SELECT * FROM " + TABLE_JOURNAL + " Where PersonID = " + personID;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();
// return count
        return cursor.getCount();
    }

    // Updating a Journal
    public boolean updateJournal(Journal Journal) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            //values.put(PERSON_ID, Journal.getPersonID());
            //values.put(JOURNAL_DATE, Journal.getJournalDate());
            values.put(JOURNAL_DETAIL, Journal.getJournalDetail());
            values.put(MUSIC_LISTENED, Journal.getMusicListened());
// updating row
            db.update(TABLE_JOURNAL, values, JOURNAL_ID + " = ?",
                    new String[]{String.valueOf(Journal.getJournalID())});
            return true;
        }
        catch(Exception ex)
        {
            throw ex;
        }
    }

    // Deleting a Journal
    public void deleteJournal(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_JOURNAL, JOURNAL_ID + " = ?",
                new String[] {Integer.toString(id)});
        db.close();
    }
}
