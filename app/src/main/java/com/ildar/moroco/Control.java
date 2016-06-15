package com.ildar.moroco;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.InputStream;
import java.io.OutputStream;

public class Control extends AppCompatActivity {

    private OutputStream outStream = null;
    private InputStream inputStream = null;

    TextView tvSpeed;
    TextView tvRotade;
    SeekBar sbSpeed;
    SeekBar sbRotade;

    int speed=0;
    int rotade=0;

    iRobotCreate robot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control);
        setupSeekBar();
        getIO();
        robot = new iRobotCreate(outStream, inputStream);
        robot.fullMode();
    }


    public void getIO() {
        this.outStream = MainActivity.getOutStream();
        this.inputStream = MainActivity.getInputStream();
    }

    void setupSeekBar(){
        tvSpeed = (TextView) findViewById(R.id.textView_speed);
        tvRotade = (TextView) findViewById(R.id.textView_rotade);
        sbSpeed = (SeekBar) findViewById(R.id.seekBar_speed);
        sbRotade = (SeekBar) findViewById(R.id.seekBar_Rotade);

        sbSpeed.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvSpeed.setText("" + progress);
                speed = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        sbRotade.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvRotade.setText("" + progress);
                rotade = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_control, menu);
        return true;
    }

    public void getMapping(MenuItem item) {
        Intent intent = new Intent(this, SetMap.class);
        startActivity(intent);
    }

    public void onForward(View view) {
        robot.move((char)Math.abs(speed));
    }

    public void onStop(View view) {
        robot.stop();
    }

    public void onRight(View view) {
        robot.rotade((char) Math.abs(speed),(char) -1);
    }

    public void onLeft(View view) {
        robot.rotade((char) Math.abs(speed),(char) 1);
    }

    public void onDown(View view) {
        robot.move((char) -Math.abs(speed));
    }

    public void onRotade(View view) {
        robot.rotade((char) speed,(char) rotade);
    }
}
