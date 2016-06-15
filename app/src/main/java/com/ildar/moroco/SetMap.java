package com.ildar.moroco;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class SetMap extends AppCompatActivity {

    private OutputStream outStream = null;
    private InputStream inputStream = null;
    iRobotCreate robot;
    private DrawView drawView;
    private ArrayList<Point> points;
    static final String TAG = "iRobot";
    private boolean correctSizeRoom = false;
    private boolean canPlay = false;
    int heightRoom;
    int weightRoom;
    double alphaHeight;
    double alphaWeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        drawView = new DrawView(this);
        setContentView(drawView);
        getIO();
        robot = new iRobotCreate(outStream, inputStream);
        robot.safeMode();
    }

    public void getIO() {
        this.outStream = MainActivity.getOutStream();
        this.inputStream = MainActivity.getInputStream();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_map, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case (R.id.back):
                    drawView.undoTheLastAction();
                break;
            case (R.id.play):
                if (correctSizeRoom) {
                    drawView.forbidden();
                    canPlay = true;
                    play();
                }else{
                    Toast.makeText(this, "Размеры комнаты не заданы", Toast.LENGTH_LONG).show();
                }
                break;
            case (R.id.setRoom):
                    final View customView = getLayoutInflater().inflate(R.layout.dialog_set_room, null);
                    new AlertDialog.Builder(this)
                            .setView(customView)
                            .setPositiveButton("Применить", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    EditText edWidthRoom = (EditText) customView.findViewById(R.id.widthRoom);
                                    EditText edHeightRoom = (EditText) customView.findViewById(R.id.heightRoom);
                                    if (!edWidthRoom.getText().toString().equals("") && !edHeightRoom.getText().toString().equals("")) {
                                        heightRoom = Integer.parseInt(edHeightRoom.getText().toString());
                                        weightRoom = Integer.parseInt(edWidthRoom.getText().toString());
                                        if (heightRoom == 0 || weightRoom == 0) {
                                            Toast.makeText(getApplicationContext(), "Задан нулевой размер комнаты", Toast.LENGTH_LONG).show();
                                            correctSizeRoom = false;
                                        }else {
                                            correctSizeRoom = true;
                                            Toast.makeText(getApplicationContext(), "Размеры приняты", Toast.LENGTH_LONG).show();
                                            alphaHeight = ((double) heightRoom)/drawView.getHeight();
                                            alphaWeight = ((double)weightRoom)/drawView.getWidth();
                                        }
                                    }else{
                                        Toast.makeText(getApplicationContext(), "Вы не задали размеры комнаты!", Toast.LENGTH_LONG).show();
                                        correctSizeRoom = false;
                                    }

                                }
                            })
                            .setNegativeButton("Закрыть", null)
                            .create()
                            .show();
                break;
            case(R.id.getControl):
                Intent intent = new Intent(this, Control.class);
                startActivity(intent);
                break;
            case (R.id.stop):
                    robot.stop();
                    canPlay = false;
                break;
        }
        return true;
    }

        public void play() {
            points = drawView.getPoints();
            if (points==null || points.size() <= 1) {
                Toast.makeText(this, "Маршрут не задан!", Toast.LENGTH_LONG).show();
                drawView.allowedDraw();
            }else {
                double meters;
                double degrees;
                double cos;
                double deltaX, deltaY;
                char turn;
                long time;
                Point p1, p2, p3;
                p1 = points.get(0);
                p2 = points.get(1);
                meters = lenght1(p1, p2);
                Log.d(TAG, "РАССтОЯНИЕ " + meters);
                robot.addStraight((char) 400, (char)(Math.round(meters*1000)));
                if (points.size() > 1 && canPlay){
                    for (int i = 2; i < points.size() ; i++) {
                        p3 = points.get(i);
                        cos = (Math.pow(lenght2(p3,p2),2) + Math.pow(lenght2(p2,p1),2) - Math.pow(lenght2(p3,p1),2)) / (2 * lenght2(p3,p2) * lenght2(p2,p1));
                        degrees = 180 - Math.toDegrees(Math.acos(cos));
                        deltaX = p2.x - p1.x;
                        deltaY = p2.y - p1.y;
                        if ((deltaX>0 && deltaY<0) || (deltaX>0 && deltaY>0)){
                            //первая четверть и четвертая
                            if (p3.y <= ((p2.y - p1.y)/(p2.x-p1.x) * p3.x + p1.y - (p2.y - p1.y)*p1.x/(p2.x-p1.x))){
                                turn = (char) 1;
                            }else {
                                turn = (char) -1;
                            }
                        }else if ((deltaX<0 && deltaY<0) || (deltaX<0 && deltaY>0)){
                            //вторая четверть и третья
                            if (p3.y >= ((p2.y - p1.y)/(p2.x-p1.x) * p3.x + p1.y - (p2.y - p1.y)*p1.x/(p2.x-p1.x))){
                                turn = (char) 1;
                            }else {
                                turn = (char) -1;
                            }
                        }else {
                            //попали на прямую, параллельную одной из оси координат
                            if ((deltaX == 0) && (deltaY<=0))  {
                                if (p1.x >= p3.x){
                                    turn = (char) 1;
                                }else {
                                    turn = (char) -1;
                                }
                            }else if (deltaX == 0 && deltaY>=0){
                                if (p1.x <= p3.x){
                                    turn = (char) 1;
                                }else {
                                    turn = (char) -1;
                                }
                            }else if (deltaY == 0 && deltaX>=0){
                                if (p1.y >= p3.y){
                                    turn = (char) 1;
                                }else {
                                    turn = (char) -1;
                                }
                            }else {
                                if (p1.y <= p3.y){
                                    turn = (char) 1;
                                }else {
                                    turn = (char) -1;
                                }
                            }
                        }
                        robot.addRotade((char) 400, turn, (char)Math.round(degrees));
                        meters = lenght1(p3, p2);
                        Log.d(TAG, "РАССтОЯНИЕ " + meters);
                        robot.addStraight((char) 400, (char)(Math.round(meters*1000)));
                        p1=p2;
                        p2=p3;
                    }
                }
                robot.startSequenceCommand();
                Toast.makeText(this, "Робот получил последовательность команд", Toast.LENGTH_LONG).show();
                drawView.allowedDraw();
            }

        }

    private double lenght1(Point p1, Point p2){
        //результат в метрах
        return Math.sqrt(Math.pow((p1.x-p2.x)*alphaWeight, 2) + Math.pow((p1.y-p2.y)*alphaHeight,2));
    }

    private double lenght2(Point p1, Point p2){
        return Math.sqrt(Math.pow(p1.x-p2.x, 2) + Math.pow(p1.y-p2.y,2));
    }

}
