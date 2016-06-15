package com.ildar.moroco;

import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

/**
 * Created by ildar on 09.06.2016.
 */
public class iRobotCreate implements RobotControl {
    private static final String TAG = "iRobot";
    private OutputStream outStream = null;
    private InputStream inputStream = null;
    private ArrayList<Character> sequenceCom;

    public iRobotCreate(OutputStream outStream, InputStream inputStream) {
        this.outStream = outStream;
        this.inputStream = inputStream;
        sequenceCom = new ArrayList<>();
        char charmsg = 128;
        sendCommandtoiRobot(charmsg);
    }

    public void addStraight(char speed, char distance){
        sequenceCom.add((char) 137);
        sequenceCom.add(toHighBytes(speed));
        sequenceCom.add(toLowBytes(speed));
        sequenceCom.add((char) 128);
        sequenceCom.add((char) 0);
        sequenceCom.add((char) 156);
        sequenceCom.add(toHighBytes(distance));
        sequenceCom.add(toLowBytes(distance));
    }

    public void addRotade(char speed, char rotatespeed, int angle){
        sequenceCom.add((char) 137);
        sequenceCom.add(toHighBytes(speed));
        sequenceCom.add(toLowBytes(speed));
        sequenceCom.add(toHighBytes(rotatespeed));
        sequenceCom.add(toLowBytes(rotatespeed));
        sequenceCom.add((char) 157);
        if (rotatespeed == '1'){
            sequenceCom.add(toHighBytes((char) angle));
            sequenceCom.add(toLowBytes((char) angle));
        }else{
            sequenceCom.add(toHighBytes((char) -angle));
            sequenceCom.add(toLowBytes((char) -angle));
        }

    }

    public void startSequenceCommand(){
        sequenceCom.add((char) 137);
        sequenceCom.add((char) 0);
        sequenceCom.add((char) 0);
        sequenceCom.add((char) 0);
        sequenceCom.add((char) 0);
        char charmsg = 152;
        sendCommandtoiRobot(charmsg);
        charmsg = (char) sequenceCom.size();
        sendCommandtoiRobot(charmsg);
        for(Character command:sequenceCom){
            sendCommandtoiRobot(command);
        }
        sequenceCom.clear();
    }

    public void safeMode(){
        char charmsg = 131;
        sendCommandtoiRobot(charmsg);
    }

    public void fullMode(){
        char charmsg = 132;
        sendCommandtoiRobot(charmsg);
    }

    @Override
    public void move(char velocity) {
        drive(velocity,(char) 32768);
    }

    @Override
    public void rotade(char velocity, char rotatespeed) {
        drive(velocity, rotatespeed);
    }

    @Override
    public void stop() {
        drive((char) 0, (char) 0);
    }

    private void drive(char velocity, char rotatespeed) {
        sendCommandtoiRobot((char)137);
        sendCommandtoiRobot(toHighBytes(velocity));
        sendCommandtoiRobot(toLowBytes(velocity));
        sendCommandtoiRobot(toHighBytes(rotatespeed));
        sendCommandtoiRobot(toLowBytes(rotatespeed));
    }

    private void sendCommandtoiRobot(char msg){
        try {
            outStream.write(msg);
        } catch (Exception e) {
            Log.e(TAG, "Exception during write.", e);
        }
    }

    private char toHighBytes(char value ){
        char eqBitVal;
        if (value >= 0)
            eqBitVal = value;
        else
            eqBitVal = (char) ((1<<16) + value);
        char charreturn = (char)( (eqBitVal >> 8) & 0xFF );
        return charreturn;
    }

    private char toLowBytes(char value ){
        char eqBitVal;
        if (value >= 0)
            eqBitVal = value;
        else
            eqBitVal = (char) ((1<<16) + value);
        char charreturn = (char)( eqBitVal & 0xFF  );
        return charreturn;
    }
}
