package com.themagicofmusic.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by KasNet14 on 5/3/2017.
 */

public class PersonDBHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "BeatRecognizer1.db";
    public static final String PERSONS_TABLE_NAME = "persons";
    public static final String PERSONS_COLUMN_ID = "id";
    public static final String PERSONS_COLUMN_LASTNAME = "LastName";
    public static final String PERSONS_COLUMN_FIRSTNAME = "FirstName";
    public static final String PERSONS_COLUMN_AGE = "age";
    public static final String PERSONS_COLUMN_HOBBY = "hobby";
    public static final String PERSONS_COLUMN_ISSUE = "issue";
    private HashMap hp;

    public PersonDBHelper(Context context) {
        super(context, DATABASE_NAME , null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        try {
            db.execSQL("DROP TABLE IF EXISTS " + PERSONS_TABLE_NAME);
            String CREATE_CONIG_TABLE = "CREATE TABLE " + PERSONS_TABLE_NAME + "("
                    + PERSONS_COLUMN_ID + " INTEGER PRIMARY KEY," + PERSONS_COLUMN_LASTNAME + " TEXT,"
                    + PERSONS_COLUMN_FIRSTNAME + " TEXT," +  PERSONS_COLUMN_AGE + " INTEGER," + PERSONS_COLUMN_HOBBY + " TEXT,"
                    + PERSONS_COLUMN_ISSUE + " TEXT" +")";
            db.execSQL(CREATE_CONIG_TABLE);
        }
        catch(Exception ex)
        {}
    }

    public void DropTable() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + PERSONS_TABLE_NAME);

    }
    public void CreateTable() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + PERSONS_TABLE_NAME);
        String CREATE_CONIG_TABLE = "CREATE TABLE " + PERSONS_TABLE_NAME + "("
                + PERSONS_COLUMN_ID + " INTEGER PRIMARY KEY," + PERSONS_COLUMN_LASTNAME + " TEXT,"
                + PERSONS_COLUMN_FIRSTNAME + " TEXT," +  PERSONS_COLUMN_AGE + " INTEGER," + PERSONS_COLUMN_HOBBY + " TEXT,"
                + PERSONS_COLUMN_ISSUE + " TEXT" +")";
        db.execSQL(CREATE_CONIG_TABLE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS " + PERSONS_TABLE_NAME);
        onCreate(db);
    }

    public boolean insertPerson (String lastName, String firstName, int age, String hobby, String issue) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(PERSONS_COLUMN_LASTNAME, lastName);
            contentValues.put(PERSONS_COLUMN_FIRSTNAME, firstName);
            contentValues.put(PERSONS_COLUMN_AGE, age);
            contentValues.put(PERSONS_COLUMN_HOBBY, hobby);
            contentValues.put(PERSONS_COLUMN_ISSUE, issue);
            db.insertOrThrow(PERSONS_TABLE_NAME, null, contentValues);
            db.close(); // Closing database connection
            return true;
        }
        catch(Exception ex)
        {
            throw ex;
        }

    }

    public Cursor getData(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from " + PERSONS_TABLE_NAME + " where id=" + id + "", null );
        return res;
    }

    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, PERSONS_TABLE_NAME);
        return numRows;
    }

    public boolean updatePerson (Integer id, String name, int age, String hobby, String issue) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(PERSONS_COLUMN_LASTNAME, name);
        contentValues.put(PERSONS_COLUMN_AGE, age);
        contentValues.put(PERSONS_COLUMN_HOBBY, hobby);
        contentValues.put(PERSONS_COLUMN_ISSUE, issue);
        db.update(PERSONS_TABLE_NAME, contentValues, "id = ? ", new String[] { Integer.toString(id) } );
        return true;
    }

    public Integer deletePerson (Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(PERSONS_TABLE_NAME,
                "id = ? ",
                new String[] { Integer.toString(id) });
    }

    public ArrayList<String> getAllPersons() {

        ArrayList<String> array_list = new ArrayList<String>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();

       /* db.execSQL("DROP TABLE IF EXISTS contacts");
        db.execSQL(
                "create table persons " +
                        "(id integer primary key, name text,age integer,hobby text, issue text)");*/
        Cursor res =  db.rawQuery( "select * from " + PERSONS_TABLE_NAME, null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            array_list.add(res.getString(res.getColumnIndex(PERSONS_COLUMN_LASTNAME)));
            res.moveToNext();
        }
        return array_list;
    }


}


