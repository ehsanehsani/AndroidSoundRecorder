package com.example.kahkeshaniha.androidsoundrecorder;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class MainActivity extends Activity {

    private SQLiteDatabase db;
    private Cursor cursor;

    ListView soundListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        soundListView = (ListView) findViewById(R.id.soundListView);
        try {
            SQLiteOpenHelper soundRecorderDatabaseHelper = new SoundRecorderDatabaseHelper(this);
            db = soundRecorderDatabaseHelper.getReadableDatabase();
            cursor = db.query("SOUND", new String[]{"_id", "NAME"}, null, null, null, null, null);
            CursorAdapter cursorAdapter = new SimpleCursorAdapter(MainActivity.this, android.R.layout.simple_list_item_1,
                    cursor, new String[]{"NAME"}, new int[]{android.R.id.text1}, 0);
            soundListView.setAdapter(cursorAdapter);
        } catch (Exception e) {
            Toast toast = Toast.makeText(this, "Database unavailable", Toast.LENGTH_SHORT);
            toast.show();
        }

        if (soundListView.getCount() == 0) {
            LinearLayout emptyLinearLayout = (LinearLayout) findViewById(R.id.emptyLinearLayout);
            emptyLinearLayout.setVisibility(View.VISIBLE);
            LinearLayout fullLinearLayout = (LinearLayout) findViewById(R.id.fullLinearLayout);
            fullLinearLayout.setVisibility(View.INVISIBLE);
        } else {
            LinearLayout emptyLinearLayout = (LinearLayout) findViewById(R.id.emptyLinearLayout);
            emptyLinearLayout.setVisibility(View.INVISIBLE);
            LinearLayout fullLinearLayout = (LinearLayout) findViewById(R.id.fullLinearLayout);
            fullLinearLayout.setVisibility(View.VISIBLE);
        }

        soundListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, PlaySoundActivity.class);
                intent.putExtra(PlaySoundActivity.EXTRA_SOUNDNO, (int) id);
                startActivity(intent);
            }
        });
    }

    public void onRecordClicked(View view) {
        Intent intent = new Intent(MainActivity.this, SoundRecorderActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cursor.close();
        db.close();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        try {
            SQLiteOpenHelper soundRecorderDatabaseHelper = new SoundRecorderDatabaseHelper(this);
            db = soundRecorderDatabaseHelper.getReadableDatabase();
            Cursor newCursor = db.query("SOUND", new String[]{"_id", "NAME"}, null, null, null, null, null);
            ListView soundListView = (ListView) findViewById(R.id.soundListView);
            CursorAdapter adapter = (CursorAdapter) soundListView.getAdapter();
            adapter.changeCursor(newCursor);
            cursor = newCursor;
        } catch (SQLiteException e) {
            Toast toast = Toast.makeText(this, "Database unavailable", Toast.LENGTH_SHORT);
            toast.show();
        }

        if (soundListView.getCount() == 0) {
            LinearLayout emptyLinearLayout = (LinearLayout) findViewById(R.id.emptyLinearLayout);
            emptyLinearLayout.setVisibility(View.VISIBLE);
            LinearLayout fullLinearLayout = (LinearLayout) findViewById(R.id.fullLinearLayout);
            fullLinearLayout.setVisibility(View.INVISIBLE);
        } else {
            LinearLayout emptyLinearLayout = (LinearLayout) findViewById(R.id.emptyLinearLayout);
            emptyLinearLayout.setVisibility(View.INVISIBLE);
            LinearLayout fullLinearLayout = (LinearLayout) findViewById(R.id.fullLinearLayout);
            fullLinearLayout.setVisibility(View.VISIBLE);
        }
    }
}