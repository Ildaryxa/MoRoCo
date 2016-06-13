package com.ildar.moroco;

/**
 * Created by ildar on 09.06.2016.
 */
public interface RobotControl {
    void move(char velocity);  //вперед
    void rotade(char velocity, char rotatespeed);  //поворот
    void stop();
}
