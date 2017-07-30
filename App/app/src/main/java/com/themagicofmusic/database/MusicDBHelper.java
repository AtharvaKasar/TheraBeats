package com.themagicofmusic.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.themagicofmusic.model.Music;

import java.util.ArrayList;
import java.util.List;

/**
 * TheMagicOfMusic - TheraBeat
 * Created by Atharva Kasar
 */

public class MusicDBHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    public static final String DATABASE_NAME = "BeatRecognizer1.db";
    // Contacts table name
    public static final String TABLE_MusicS = "Music";
    // Musics Table Columns names
    public static final String MUSIC_ID = "MusicID";
    public static final String MUSIC_NAME = "MusicName";
    public static final String MUSIC_DETAIL = "MusicDetail";
    public static final String BEAT_TYPE_CODE = "BeatTypeCode";
    public static final String MUSIC_LOCATION = "MusicLocation";
    public MusicDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_Music_TABLE = "CREATE TABLE " + TABLE_MusicS + "("
                + MUSIC_ID + " INTEGER PRIMARY KEY," + MUSIC_NAME + " TEXT,"
                + MUSIC_DETAIL + " TEXT," +  BEAT_TYPE_CODE + " TEXT," + MUSIC_LOCATION + " TEXT" + ")";
        db.execSQL(CREATE_Music_TABLE);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
// Drop older table if existed
        //db.execSQL("DROP TABLE IF EXISTS " + TABLE_MusicS);
// Creating tables again
        onCreate(db);
    }

    public void DropTable() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MusicS);

    }
    public void CreateTable() {
        SQLiteDatabase db = this.getWritableDatabase();
        String CREATE_Music_TABLE = "CREATE TABLE " + TABLE_MusicS + "("
                + MUSIC_ID + " INTEGER PRIMARY KEY," + MUSIC_NAME + " TEXT,"
                + MUSIC_DETAIL + " TEXT," +  BEAT_TYPE_CODE + " TEXT," + MUSIC_LOCATION + " TEXT" + ")";
        db.execSQL(CREATE_Music_TABLE);
    }
    // Adding new Music
    public boolean addMusic(Music music) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();

        /*db.execSQL("DROP TABLE IF EXISTS " + TABLE_MusicS);
        String CREATE_Music_TABLE = "CREATE TABLE " + TABLE_MusicS + "("
                + MUSIC_ID + " INTEGER PRIMARY KEY," + MUSIC_NAME + " TEXT,"
                + MUSIC_DETAIL + " TEXT" +  BEAT_TYPE_CODE + " TEXT," + MUSIC_LOCATION + " TEXT," + ")";*/

            ContentValues values = new ContentValues();
            values.put(MUSIC_NAME, music.getMusicName()); // Music code
            values.put(MUSIC_DETAIL, music.getMusicDetail()); // Music desc
            values.put(BEAT_TYPE_CODE, music.getBeatTypeCode()); // Music desc
            values.put(MUSIC_LOCATION, music.getMusicLocation()); // Music desc
// Inserting Row
            db.insert(TABLE_MusicS, null, values);
            db.close(); // Closing database connection
            return true;
        }
        catch(Exception ex)
        {
            throw ex;
        }
    }


    // Getting one Music
    public Music getMusic(int id) {

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_MusicS, new String[] { MUSIC_ID,
                        MUSIC_NAME, MUSIC_DETAIL,BEAT_TYPE_CODE, MUSIC_LOCATION }, MUSIC_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        Music contact = new Music(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4));
// return Music
        return contact;
    }

    public Music getMusicByName(String musicName) {

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_MusicS, new String[] { MUSIC_ID,
                        MUSIC_NAME, MUSIC_DETAIL,BEAT_TYPE_CODE, MUSIC_LOCATION }, MUSIC_NAME + "=?",
                new String[] { musicName }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        Music contact = new Music(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4));
// return Music
        return contact;
    }
    // Getting All Musics
    public List<Music> getAllMusics() {


        List<Music> MusicList = new ArrayList<Music>();
// Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_MusicS;
        SQLiteDatabase db = this.getWritableDatabase();
         Cursor cursor = db.rawQuery(selectQuery, null);
// looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Music Music = new Music();
                Music.setMusicID(Integer.parseInt(cursor.getString(0)));
                Music.setMusicName(cursor.getString(1));
                Music.setMusicDetail(cursor.getString(2));
                Music.setBeatTypeCode(cursor.getString(3));
                Music.setMusicLocation(cursor.getString(4));
// Adding contact to list
                MusicList.add(Music);
            } while (cursor.moveToNext());
        }
// return contact list
        return MusicList;
    }

    public List<Music> getRecommendedMusic(String beatType) {


        List<Music> MusicList = new ArrayList<Music>();
        try {
// Select All Query
            String selectQuery = "SELECT * FROM " + TABLE_MusicS + " Where " + BEAT_TYPE_CODE + "= ?";
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, new String[]{beatType});
// looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    Music Music = new Music();
                    Music.setMusicID(Integer.parseInt(cursor.getString(0)));
                    Music.setMusicName(cursor.getString(1));
                    Music.setMusicDetail(cursor.getString(2));
                    Music.setBeatTypeCode(cursor.getString(3));
                    Music.setMusicLocation(cursor.getString(4));
// Adding contact to list
                    MusicList.add(Music);
                } while (cursor.moveToNext());
            }
// return contact list
            return MusicList;
        }catch(Exception ex)
        {
            throw ex;
        }
    }

    // Getting Musics Count
    public int getMusicsCount() {
        String countQuery = "SELECT * FROM " + TABLE_MusicS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

// return count
        return cursor.getCount();

    }

    // Updating a Music
    public boolean updateMusic(Music Music) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(MUSIC_NAME, Music.getMusicName());
            values.put(MUSIC_DETAIL, Music.getMusicDetail());
            values.put(BEAT_TYPE_CODE, Music.getBeatTypeCode());
            values.put(MUSIC_LOCATION, Music.getMusicLocation());
// updating row
            db.update(TABLE_MusicS, values, MUSIC_ID + " = ?",
                    new String[]{String.valueOf(Music.getMusicID())});
            return true;
        }
        catch(Exception ex)
        {
            throw ex;
        }
    }

    // Deleting a Music
    public void deleteMusic(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_MusicS, MUSIC_ID + " = ?",
                new String[] {Integer.toString(id)});
        db.close();
    }

    public void addAllMusic()
    {
        try {
            List<Music> musicList = new ArrayList<Music>();

            Music softmusic1 = new Music(1, "SOFTMUSIC1", "Soft Music 1", "SOFT", "RAW");
            Music softmusic2 = new Music(2, "SOFTMUSIC2", "Soft Music 2", "SOFT", "RAW");
            Music softmusic3 = new Music(3, "SOFTMUSIC3", "Soft Music 3", "SOFT", "RAW");
            Music softmusic4 = new Music(4, "SOFTMUSIC4", "Soft Music 4", "SOFT", "RAW");

            Music mildmusic1 = new Music(11, "MILDMUSIC1", "Mild Music 1", "MILD", "RAW");
            Music mildmusic2 = new Music(12, "MILDMUSIC2", "Mild Music 2", "MILD", "RAW");
            Music mildmusic3 = new Music(13, "MILDMUSIC3", "Mild Music 3", "MILD", "RAW");
            Music mildmusic4 = new Music(14, "MILDMUSIC4", "Mild Music 4", "MILD", "RAW");

            Music hardmusic1 = new Music(21, "HARDMUSIC1", "Hard Music 1", "HARD", "RAW");
            Music hardmusic2 = new Music(22, "HARDMUSIC2", "Hard Music 2", "HARD", "RAW");
            Music hardmusic3 = new Music(23, "HARDMUSIC3", "Hard Music 3", "HARD", "RAW");
            Music hardmusic4 = new Music(24, "HARDMUSIC4", "Hard Music 4", "HARD", "RAW");
            musicList.add(softmusic1);
            musicList.add(softmusic2);
            musicList.add(softmusic3);
            musicList.add(softmusic4);

            musicList.add(mildmusic1);
            musicList.add(mildmusic2);
            musicList.add(mildmusic3);
            musicList.add(mildmusic4);

            musicList.add(hardmusic1);
            musicList.add(hardmusic2);
            musicList.add(hardmusic3);
            musicList.add(hardmusic4);

            for (Music m : musicList) {
                try {
                    addMusic(m);
                } catch (Exception ex) {
                }
            }

        }
        catch(Exception ex)
        {}
    }
}
