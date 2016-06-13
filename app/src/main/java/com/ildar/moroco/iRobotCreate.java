package com.ildar.moroco;

import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by ildar on 09.06.2016.
 */
public class iRobotCreate implements RobotControl {
    private static final String TAG = "iRobot";
    private OutputStream outStream = null;
    private InputStream inputStream = null;

    public iRobotCreate(OutputStream outStream, InputStream inputStream) {
        this.outStream = outStream;
        this.inputStream = inputStream;

        char charmsg = 128;
        sendCommandtoiRobot(charmsg);
        charmsg = 132;
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
//	    # if positive or zero, it's OK
        if (value >= 0)
            eqBitVal = value;
//	    # if it's negative, I think it is this
        else
            eqBitVal = (char) ((1<<16) + value);
        char charreturn = (char)( (eqBitVal >> 8) & 0xFF );
        return charreturn;
    }

    private char toLowBytes(char value ){
        char eqBitVal;
//	    # if positive or zero, it's OK
        if (value >= 0)
            eqBitVal = value;
//	    # if it's negative, I think it is this
        else
            eqBitVal = (char) ((1<<16) + value);
        char charreturn = (char)( eqBitVal & 0xFF  );
        return charreturn;
    }
}
