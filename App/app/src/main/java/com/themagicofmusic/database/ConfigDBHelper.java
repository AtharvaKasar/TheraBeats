package com.themagicofmusic.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Path;

import com.themagicofmusic.model.Config;

import java.util.ArrayList;
import java.util.List;

/**
 * TheMagicOfMusic - TheraBeat
 * Created by Atharva Kasar
 */

public class ConfigDBHelper extends SQLiteOpenHelper {
    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    public static final String DATABASE_NAME = "BeatRecognizer1.db";
    // Contacts table name
    public static final String TABLE_CONIGS = "ApplicationConfiguration";
    // CONIGs Table Columns names
    public static final String CONIG_ID = "ConfigID";
    public static final String CURRENT_PERSON_ID = "CurrentPersonID";

    public ConfigDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONIG_TABLE = "CREATE TABLE " + TABLE_CONIGS + "("
                + CONIG_ID + " INTEGER PRIMARY KEY," + CURRENT_PERSON_ID + " INTEGER" + ")";
        db.execSQL(CREATE_CONIG_TABLE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
// Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONIGS);
// Creating tables again
        onCreate(db);
    }



    public SQLiteDatabase GetDatabase() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db;

    }
    public void DropTable() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONIGS);

    }
    public void CreateTable() {
        SQLiteDatabase db = this.getWritableDatabase();
        String CREATE_CONIG_TABLE = "CREATE TABLE " + TABLE_CONIGS + "("
                + CONIG_ID + " INTEGER PRIMARY KEY," + CURRENT_PERSON_ID + " INTEGER" + ")";
        db.execSQL(CREATE_CONIG_TABLE);
    }
    // Adding new CONIG
    public void addConfig(Config config) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();

        /*db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONIGS);
        String CREATE_CONIG_TABLE = "CREATE TABLE " + TABLE_CONIGS + "("
                + CONIG_ID + " INTEGER PRIMARY KEY," + CURRENT_PERSON_ID + " TEXT,"
                + CONIG_DETAIL + " TEXT" +  BEAT_TYPE_CODE + " TEXT," + CONIG_LOCATION + " TEXT," + ")";*/

            ContentValues values = new ContentValues();
            values.put(CURRENT_PERSON_ID, config.getCurrentPersonID()); // CONIG code
// Inserting Row
            db.insertOrThrow(TABLE_CONIGS, null, values);
            db.close(); // Closing database connection
        }
        catch(Exception ex)
        {
            throw ex;
        }
    }

    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, TABLE_CONIGS);
        return numRows;
    }

    // Getting one CONIG
    public Config getConfig() {

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_CONIGS, new String[] { CONIG_ID,
                        CURRENT_PERSON_ID}, null, null, null, null, null);
        Config config =null;
        if (cursor != null &&  cursor.moveToFirst()) {
            config = new Config(cursor.getInt(0), cursor.getInt(1));
        }

// return CONIG
        return config;
    }



    // Updating a CONIG
    public int updateConfig(Config config) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(CURRENT_PERSON_ID, config.getCurrentPersonID());

// updating row
            return db.update(TABLE_CONIGS, values, CONIG_ID + " = ?",
                    new String[]{String.valueOf(config.getConfigID())});
        }
        catch(Exception ex)
        {
            throw ex;
        }
    }

    // Deleting a CONIG
    public void deleteCConfig(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CONIGS, CONIG_ID + " = ?",
                new String[] {Integer.toString(id)});
        db.close();
    }
}
