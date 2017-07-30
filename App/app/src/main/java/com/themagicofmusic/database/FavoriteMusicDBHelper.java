package com.themagicofmusic.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.themagicofmusic.model.FavoriteMusic;
import com.themagicofmusic.model.Music;
import com.themagicofmusic.model.FavoriteMusic;

import java.util.ArrayList;
import java.util.List;

/**
 * TheMagicOfMusic - TheraBeat
 * Created by Atharva Kasar
 */

public class FavoriteMusicDBHelper extends SQLiteOpenHelper {
    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    public static final String DATABASE_NAME = "BeatRecognizer1.db";
    // Contacts table name
    public static final String TABLE_MusicS = "FavoriteMusic";
    // Musics Table Columns names
    public static final String FAVMUSIC_ID = "FavMusicID";
    public static final String PERSON_ID = "PersonID";
    public static final String MUSIC_ID = "MusicID";

    public FavoriteMusicDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_Music_TABLE = "CREATE TABLE " + TABLE_MusicS + "("
                + FAVMUSIC_ID + " INTEGER PRIMARY KEY," + PERSON_ID + " INTEGER,"
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
                + FAVMUSIC_ID + " INTEGER PRIMARY KEY," + PERSON_ID + " INTEGER,"
                + MUSIC_ID + " INTEGER" + ")";
        db.execSQL(CREATE_Music_TABLE);
    }
    // Adding new Music
    public boolean addFavoriteMusic(FavoriteMusic music) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();

        /*db.execSQL("DROP TABLE IF EXISTS " + TABLE_MusicS);
        String CREATE_Music_TABLE = "CREATE TABLE " + TABLE_MusicS + "("
                + FAVMUSIC_ID + " INTEGER PRIMARY KEY," + PERSON_ID + " TEXT,"
                + MUSIC_ID + " TEXT" +  BEAT_TYPE_CODE + " TEXT," + MUSIC_LOCATION + " TEXT," + ")";*/

            ContentValues values = new ContentValues();
            values.put(PERSON_ID, music.getPersonID()); // Music code
            values.put(MUSIC_ID, music.getMusicID()); // Music desc


            db.insertOrThrow(TABLE_MusicS, null, values);
            db.close(); // Closing database connection
            return true;
        }
        catch(Exception ex)
        {
            throw ex;
        }
    }


    // Getting one Music
    public FavoriteMusic getFavoriteMusic(int id) {

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_MusicS, new String[] { FAVMUSIC_ID,
                        PERSON_ID, MUSIC_ID }, FAVMUSIC_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        FavoriteMusic favMusic = new FavoriteMusic(cursor.getInt(0),
                cursor.getInt(1), cursor.getInt(2));
// return Music
        return favMusic;
    }

    public boolean FavoriteMusicExists(int personID, int musicID) {
        boolean favMusicExists = false;

        String countQuery = "SELECT * FROM " + TABLE_MusicS + " Where PersonID = " + personID + " AND MusicID = " + musicID;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        if(cursor != null && cursor.getCount() > 0)
        {
            favMusicExists = true;
        }
// return Music
        return favMusicExists;
    }
    // Getting All Musics
    public List<FavoriteMusic> getAllFavoriteMusics(int personId) {


        List<FavoriteMusic> MusicList = new ArrayList<FavoriteMusic>();
// Select All Query
        //String selectQuery = "SELECT * FROM " + TABLE_MusicS + " Where PersonID = " + personId ;
        String selectQuery = "SELECT FavMusicID, PersonID, FavoriteMusic.MusicID, MusicName FROM " + TABLE_MusicS +
                " Inner Join Music on FavoriteMusic.MusicID = Music.MusicID " + " Where FavoriteMusic.PersonID = " + personId ;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
// looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                FavoriteMusic Music = new FavoriteMusic();
                Music.setfavMusicID(cursor.getInt(0));
                Music.setPersonID(cursor.getInt(1));
                Music.setMusicID(cursor.getInt(2));
                Music.setMusicName(cursor.getString(3));
                MusicList.add(Music);
            } while (cursor.moveToNext());
        }

        return MusicList;
    }


    // Getting Musics Count
    public int getFavoriteMusicsCount(int personID) {
        String countQuery = "SELECT * FROM " + TABLE_MusicS + " Where PersonID=" + personID;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();
// return count
        return cursor.getCount();
    }

    // Updating a Music
    public int updateFavoriteMusic(FavoriteMusic Music) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(PERSON_ID, Music.getPersonID());
        values.put(MUSIC_ID, Music.getMusicID());

// updating row
        return db.update(TABLE_MusicS, values, FAVMUSIC_ID + " = ?",
                new String[]{String.valueOf(Music.getMusicID())});
    }

    // Deleting a Music
    public void deleteFavoriteMusic(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_MusicS, FAVMUSIC_ID + " = ?",
                new String[] {Integer.toString(id)});
        db.close();
    }
}
