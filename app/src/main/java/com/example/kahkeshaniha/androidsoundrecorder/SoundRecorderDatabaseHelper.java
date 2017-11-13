package com.example.kahkeshaniha.androidsoundrecorder;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Ehsani on 11/13/2017.
 */

public class SoundRecorderDatabaseHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "soundRecorder";
    private static final int DB_VERSION = 1;

    public SoundRecorderDatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        updateMyDatabase(db, 0, DB_VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        updateMyDatabase(db, oldVersion, newVersion);
    }

    private void updateMyDatabase(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 1) {
            db.execSQL("CREATE TABLE SOUND (_id INTEGER PRIMARY KEY AUTOINCREMENT, NAME TEXT, PATH TEXT, LENGTH TEXT, SIZE INTEGER);");
        }
    }
}