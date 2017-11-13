package com.example.kahkeshaniha.androidsoundrecorder;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class PlaySoundActivity extends Activity {
    public static final String EXTRA_SOUNDNO = "soundNo";
    private int soundNo;
    private Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_sound);

        soundNo = (Integer) getIntent().getExtras().get(EXTRA_SOUNDNO);

        try {
            SQLiteOpenHelper soundRecorderDatabaseHelper = new SoundRecorderDatabaseHelper(this);
            SQLiteDatabase db = soundRecorderDatabaseHelper.getReadableDatabase();
            cursor = db.query("SOUND", new String[]{"NAME", "PATH", "LENGTH", "SIZE"}, "_id = ?",
                    new String[]{Integer.toString(soundNo)}, null, null, null);

            if (cursor.moveToFirst()) {
                String name = cursor.getString(0);
                String length = cursor.getString(2);
                String size = cursor.getString(3);

                TextView nameText = (TextView) findViewById(R.id.nameView);
                nameText.setText(name);
                TextView lengthText = (TextView) findViewById(R.id.lengthView);
                lengthText.setText(length);
                TextView sizeText = (TextView) findViewById(R.id.sizeView);
                sizeText.setText(size);

                db.close();
                cursor.close();
            }

        } catch (Exception e) {
            Toast toast = Toast.makeText(PlaySoundActivity.this, "Database unavailable", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    public void onBackVoiceClicked(View view) {
        try {
            SQLiteOpenHelper soundRecorderDatabaseHelper = new SoundRecorderDatabaseHelper(this);
            SQLiteDatabase db = soundRecorderDatabaseHelper.getReadableDatabase();
            Cursor cursor = db.query("SOUND", new String[]{"NAME", "LENGTH", "SIZE"}, "_id = ?",
                    new String[]{Integer.toString(soundNo - 1)}, null, null, null);

            if (cursor.moveToFirst()) {
                String name = cursor.getString(0);
                String length = cursor.getString(1);
                String size = cursor.getString(2);

                TextView nameText = (TextView) findViewById(R.id.nameView);
                nameText.setText(name);
                TextView lengthText = (TextView) findViewById(R.id.lengthView);
                lengthText.setText(length);
                TextView sizeText = (TextView) findViewById(R.id.sizeView);
                sizeText.setText(size);

                soundNo--;
                db.close();
                cursor.close();
            } else {
                Toast toast = Toast.makeText(PlaySoundActivity.this, "No Sound Exist", Toast.LENGTH_SHORT);
                toast.show();

                TextView nameText = (TextView) findViewById(R.id.nameView);
                nameText.setText("No Sound Exist");
                TextView lengthText = (TextView) findViewById(R.id.lengthView);
                lengthText.setText(null);
                TextView sizeText = (TextView) findViewById(R.id.sizeView);
                sizeText.setText(null);
            }
        } catch (Exception e) {
            Toast toast = Toast.makeText(PlaySoundActivity.this, "Database unavailable", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    public void onPlayVoiceClicked(View view) {
        LinearLayout playLinearLayout = (LinearLayout) findViewById(R.id.playLinearLayout);
        playLinearLayout.setVisibility(View.INVISIBLE);
        LinearLayout pauseLinearLayout = (LinearLayout) findViewById(R.id.pauseLinearLayout);
        pauseLinearLayout.setVisibility(View.VISIBLE);
    }

    public void onPauseVoiceClicked(View view) {

        LinearLayout pauseLinearLayout = (LinearLayout) findViewById(R.id.pauseLinearLayout);
        pauseLinearLayout.setVisibility(View.INVISIBLE);
        LinearLayout playLinearLayout = (LinearLayout) findViewById(R.id.playLinearLayout);
        playLinearLayout.setVisibility(View.VISIBLE);
    }

    public void onNextVoiceClicked(View view) {
        try {
            SQLiteOpenHelper soundRecorderDatabaseHelper = new SoundRecorderDatabaseHelper(this);
            SQLiteDatabase db = soundRecorderDatabaseHelper.getReadableDatabase();
            Cursor cursor = db.query("SOUND", new String[]{"NAME", "LENGTH", "SIZE"}, "_id = ?",
                    new String[]{Integer.toString(soundNo + 1)}, null, null, null);

            if (cursor.moveToFirst()) {
                String name = cursor.getString(0);
                String length = cursor.getString(1);
                String size = cursor.getString(2);

                TextView nameText = (TextView) findViewById(R.id.nameView);
                nameText.setText(name);
                TextView lengthText = (TextView) findViewById(R.id.lengthView);
                lengthText.setText(length);
                TextView sizeText = (TextView) findViewById(R.id.sizeView);
                sizeText.setText(size);

                soundNo++;
                db.close();
                cursor.close();

            } else {
                Toast toast = Toast.makeText(PlaySoundActivity.this, "No Sound Exist", Toast.LENGTH_SHORT);
                toast.show();

                TextView nameText = (TextView) findViewById(R.id.nameView);
                nameText.setText("No Sound Exist");
                TextView lengthText = (TextView) findViewById(R.id.lengthView);
                lengthText.setText(null);
                TextView sizeText = (TextView) findViewById(R.id.sizeView);
                sizeText.setText(null);
            }
        } catch (Exception e) {
            Toast toast = Toast.makeText(PlaySoundActivity.this, "Database unavailable", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    public void onDeleteVoiceClicked(View view) {
        try {
            SQLiteOpenHelper soundRecorderDatabaseHelper = new SoundRecorderDatabaseHelper(this);
            SQLiteDatabase db = soundRecorderDatabaseHelper.getReadableDatabase();
            db.delete("SOUND", "_id = ?", new String[]{Integer.toString(soundNo)});

            Toast toast = Toast.makeText(PlaySoundActivity.this, "Sound Deleted", Toast.LENGTH_SHORT);
            toast.show();

        } catch (Exception e) {
            Toast toast = Toast.makeText(PlaySoundActivity.this, "Database unavailable", Toast.LENGTH_SHORT);
            toast.show();
        }

        TextView nameText = (TextView) findViewById(R.id.nameView);
        nameText.setText("Voice Deleted");
        TextView lengthText = (TextView) findViewById(R.id.lengthView);
        lengthText.setText(null);
        TextView sizeText = (TextView) findViewById(R.id.sizeView);
        sizeText.setText(null);
    }
}
