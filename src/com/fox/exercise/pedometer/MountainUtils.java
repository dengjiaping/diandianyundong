package com.fox.exercise.pedometer;

import android.util.Log;

import cn.ingenic.indroidsync.SportsApp;

public class MountainUtils {

    public static double getCalories(double dis) {
        int weight = SportsApp.getInstance().getSportUser().getWeight();
        if (weight == 0) {
            weight = 65;
        }
        int speed = 4;//km/h
        double hour = dis / speed;
        double mets = 8.0;
        double expenditure = ((mets * 3.5 * weight) / 200) * hour * 60;
        Log.i("expenditure", "expenditure = " + expenditure);
        return expenditure;
    }
}