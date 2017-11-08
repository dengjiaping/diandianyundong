package com.fox.exercise.pedometer;

import cn.ingenic.indroidsync.SportsApp;

public class RunningUtils {

    /* met for diferent speed
    speed 4*1.6093(km/h):		met=6.0;
    speed 5*1.609(km/h):		met=8.3;
    speed 5.2*1.609(km/h):	met=9;
    speed 6*1.609(km/h):		met=9.8;
    speed 6.7*1.609(km/h):	met=10.5;
    speed 7*1.609(km/h):		met=11;
    speed 7.5*1.609(km/h):	met=11.4;
    speed 8*1.609(km/h):		met=11.8;
    speed 8.6*1.609(km/h):	met=12.3;
    speed 9*1.609(km/h):		met=12.8;
    speed 10*1.609(km/h):		met=14.5;
    speed 11*1.609(km/h):		met=16;
    speed 12*1.609(km/h):		met=19;
    speed 13*1.609(km/h):		met=19.8;
    speed 14*1.609(km/h):		met=23;
    */
    static double speed = 8.0;//km/h

    public static double getCalories(double dis) {//dis--km
        int weight = SportsApp.getInstance().getSportUser().getWeight();
        if (weight == 0) {
            weight = 65;
        }
        double mins = dis * 60 / speed;
        double met = 8.3;    //speed is 8km/h
        return cckg(weight, met, mins);
    }


    private static double cckg(int weight, double met, double mins) {
        return Math.round(met * 3.5 * weight / 200 * mins);
    }
}
