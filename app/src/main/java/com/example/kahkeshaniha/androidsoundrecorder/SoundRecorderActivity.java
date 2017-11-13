package com.example.kahkeshaniha.androidsoundrecorder;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.Handler;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class SoundRecorderActivity extends Activity {

    MediaRecorder mediaRecorder;
    String audioSavePathInDevice = null;

    private TextView recordedTimeView1;
    private TextView recordedTimeView2;

    private int counter = 0;
    private boolean isRunning = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sound_recorder);
    }

    public void onStartRecordClicked(View view) throws IOException {

        recordedTimeView1 = (TextView) findViewById(R.id.recordedTimeView1);
        recordedTimeView2 = (TextView) findViewById(R.id.recordedTimeView2);

        LinearLayout startRecordLinearLayout = (LinearLayout) findViewById(R.id.startRecordLinearLayout);
        startRecordLinearLayout.setVisibility(View.INVISIBLE);
        LinearLayout recordingLinearLayout = (LinearLayout) findViewById(R.id.recordingLinearLayout);
        recordingLinearLayout.setVisibility(View.VISIBLE);

        isRunning = true;
        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                int minute = counter / 6000;
                int second = (counter % 6000) / 100;
                int miliSecond = counter % 100;

                if (isRunning) {
                    String result = String.format("%d:%02d:%02d", minute, second, miliSecond);
                    recordedTimeView1.setText(result);
                    counter += 2;
                }
                handler.postDelayed(this, 18);
            }
        });

        String filePath = setFileNameAndPath();
        audioSavePathInDevice = filePath;

        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        mediaRecorder.setOutputFile(filePath);

        try {
            mediaRecorder.prepare();
            mediaRecorder.start();
        } catch (IllegalStateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        Toast toast = Toast.makeText(SoundRecorderActivity.this, "Recording Started", Toast.LENGTH_SHORT);
        toast.show();
    }

    public void onStopRecordClicked(View view) {

        isRunning = false;
        counter = 0;
        recordedTimeView1.setText("00:00:00");
        recordedTimeView2.setText("00:00:00");

        try {
            mediaRecorder.stop();
            mediaRecorder.release();
            mediaRecorder = null;
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }

        LinearLayout recordingLinearLayout = (LinearLayout) findViewById(R.id.recordingLinearLayout);
        recordingLinearLayout.setVisibility(View.INVISIBLE);
        LinearLayout saveLinearLayout = (LinearLayout) findViewById(R.id.saveLinearLayout);
        saveLinearLayout.setVisibility(View.VISIBLE);

        Toast toast = Toast.makeText(SoundRecorderActivity.this, "Recording Completed", Toast.LENGTH_SHORT);
        toast.show();
    }

    public void onPauseRecordClicked(View view) throws IOException {
        isRunning = false;

        mediaRecorder.stop();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        FileOutputStream pausedFile = new FileOutputStream(audioSavePathInDevice);
        mediaRecorder.setOutputFile(pausedFile.getFD());

        recordedTimeView2.setText(recordedTimeView1.getText());

        LinearLayout recordingLinearLayout = (LinearLayout) findViewById(R.id.recordingLinearLayout);
        recordingLinearLayout.setVisibility(View.INVISIBLE);
        LinearLayout pausingLinearLayout = (LinearLayout) findViewById(R.id.pausingLinearLayout);
        pausingLinearLayout.setVisibility(View.VISIBLE);
    }

    public void onResumeRecordClicked(View view) throws IOException {
        isRunning = true;

        mediaRecorder.prepare();
        mediaRecorder.start();

        LinearLayout pausingLinearLayout = (LinearLayout) findViewById(R.id.pausingLinearLayout);
        pausingLinearLayout.setVisibility(View.INVISIBLE);
        LinearLayout recordingLinearLayout = (LinearLayout) findViewById(R.id.recordingLinearLayout);
        recordingLinearLayout.setVisibility(View.VISIBLE);
    }

    public void onDeleteClicked(View view) {
        Toast toast = Toast.makeText(SoundRecorderActivity.this, "Your Voice Deleted!", Toast.LENGTH_SHORT);
        toast.show();

        mediaRecorder = null;
        audioSavePathInDevice = null;

        Intent intent = new Intent(SoundRecorderActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void onSaveClicked(View view) {

        EditText voiceNameText = (EditText) findViewById(R.id.voiceNameText);
        String name = voiceNameText.getText().toString();

        SQLiteOpenHelper soundRecorderDatabaseHelper = new SoundRecorderDatabaseHelper(this);
        SQLiteDatabase db = soundRecorderDatabaseHelper.getWritableDatabase();
        ContentValues soundValues = new ContentValues();
        soundValues.put("NAME", name);
        soundValues.put("PATH", String.valueOf(audioSavePathInDevice));
        soundValues.put("LENGTH", 0);
        soundValues.put("SIZE", 0);
        db.insert("SOUND", null, soundValues);

        audioSavePathInDevice = null;

        Toast toast = Toast.makeText(SoundRecorderActivity.this, "Your Voice Saved!", Toast.LENGTH_SHORT);
        toast.show();

        Intent intent = new Intent(SoundRecorderActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public String setFileNameAndPath() {
        int count = 0;
        File f;
        do {
            count++;
            audioSavePathInDevice = Environment.getExternalStorageDirectory().getAbsolutePath() + "/SoundRecorder/";
            f = new File(audioSavePathInDevice);

            if (!f.exists())
                f.mkdir();

            audioSavePathInDevice += "Voice" + (count) + ".mp3";
            f = new File(audioSavePathInDevice);

        } while (f.exists() && !f.isDirectory());

        File file = new File(audioSavePathInDevice);
        return file.toString();
    }
}
