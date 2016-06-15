package com.ildar.moroco;

/**
 * Created by ildar on 09.06.2016.
 */
public interface RobotControl {
    void move(char velocity);  //вперед
    void move(char velocity, double meters);
    void rotade(char velocity, char rotatespeed);  //поворот
    void rotade(char velocity, char rotatespeed, double degrees);
    void stop();
}
