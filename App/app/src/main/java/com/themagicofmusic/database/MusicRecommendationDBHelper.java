package com.themagicofmusic.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.themagicofmusic.model.Music;
import com.themagicofmusic.model.MusicRecommendation;

import java.util.ArrayList;
import java.util.List;

/**
 * TheMagicOfMusic - TheraBeat
 * Created by Atharva Kasar
 */

public class MusicRecommendationDBHelper extends SQLiteOpenHelper {
    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    public static final String DATABASE_NAME = "BeatRecognizer1.db";
    // Contacts table name
    public static final String TABLE_MusicS = "MusicRecommendation";
    // Musics Table Columns names
    public static final String RECOMMENDATION_ID = "RecommendationID";
    public static final String PERSON_ID = "PersonID";
    public static final String MUSIC_ID = "MusicID";

    public MusicRecommendationDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_Music_TABLE = "CREATE TABLE " + TABLE_MusicS + "("
                + RECOMMENDATION_ID + " INTEGER PRIMARY KEY," + PERSON_ID + " INTEGER,"
                + MUSIC_ID + " INTEGER" + ")";
        db.execSQL(CREATE_Music_TABLE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
// Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MusicS);
// Creating tables again
        onCreate(db);
    }

    public void DropTable() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MusicS);

    }
    public void CreateTable() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MusicS);
        String CREATE_Music_TABLE = "CREATE TABLE " + TABLE_MusicS + "("
                + RECOMMENDATION_ID + " INTEGER PRIMARY KEY," + PERSON_ID + " INTEGER,"
                + MUSIC_ID + " INTEGER" + ")";
        db.execSQL(CREATE_Music_TABLE);
    }
    // Adding new Music
    public void addMusicRecommendation(MusicRecommendation music) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();

        /*db.execSQL("DROP TABLE IF EXISTS " + TABLE_MusicS);
        String CREATE_Music_TABLE = "CREATE TABLE " + TABLE_MusicS + "("
                + RECOMMENDATION_ID + " INTEGER PRIMARY KEY," + PERSON_ID + " TEXT,"
                + MUSIC_ID + " TEXT" +  BEAT_TYPE_CODE + " TEXT," + MUSIC_LOCATION + " TEXT," + ")";*/

            ContentValues values = new ContentValues();
            values.put(PERSON_ID, music.getPersonID()); // Music code
            values.put(MUSIC_ID, music.getMusicID()); // Music desc


            db.insert(TABLE_MusicS, null, values);
            db.close(); // Closing database connection
        }
        catch(Exception ex)
        {
            throw ex;
        }
    }

    public void addAllMusicRecommendation(List<MusicRecommendation> musicRecommendationList) {
        try {
             for(MusicRecommendation musicRec : musicRecommendationList)
             {
                 addMusicRecommendation(musicRec);
             }
        }
        catch(Exception ex)
        {
            throw ex;
        }
    }
    // Getting one Music
    public MusicRecommendation getMusicRecommendation(int id) {

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_MusicS, new String[] { RECOMMENDATION_ID,
                        PERSON_ID, MUSIC_ID }, RECOMMENDATION_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        MusicRecommendation recommendation = new MusicRecommendation(cursor.getInt(0),
                cursor.getInt(1), cursor.getInt(2));
// return Music
        return recommendation;
    }

    // Getting All Musics
    public List<MusicRecommendation> getAllMusicRecommendations(int personId) {


        List<MusicRecommendation> MusicList = new ArrayList<MusicRecommendation>();
// Select All Query
        String selectQuery = "SELECT RecommendationID, PersonID, MusicRecommendation.MusicID, MusicName FROM " + TABLE_MusicS +
                " Inner Join Music on MusicRecommendation.MusicID = Music.MusicID " + " Where MusicRecommendation.PersonID = " + personId ;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
// looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                MusicRecommendation Music = new MusicRecommendation();
                Music.setRecommendationID(cursor.getInt(0));
                Music.setPersonID(cursor.getInt(1));
                Music.setMusicID(cursor.getInt(2));
                Music.setMusicName(cursor.getString(3));
                MusicList.add(Music);
            } while (cursor.moveToNext());
        }

        return MusicList;
    }


    // Getting Musics Count
    public int getMusicRecommendationsCount(int personID) {
        String countQuery = "SELECT * FROM " + TABLE_MusicS + " Where PersonID=" + personID;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        //cursor.close();
// return count
        return cursor.getCount();
    }

    // Updating a Music
    public int updateMusicRecommendation(MusicRecommendation Music) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(PERSON_ID, Music.getPersonID());
        values.put(MUSIC_ID, Music.getMusicID());

// updating row
        return db.update(TABLE_MusicS, values, RECOMMENDATION_ID + " = ?",
                new String[]{String.valueOf(Music.getMusicID())});
    }

    // Deleting a Music
    public void deleteMusicRecommendation(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_MusicS, RECOMMENDATION_ID + " = ?",
                new String[] {Integer.toString(id)});
        db.close();
    }
}
