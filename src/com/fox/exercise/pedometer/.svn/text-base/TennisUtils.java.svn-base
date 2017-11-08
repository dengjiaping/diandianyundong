package com.fox.exercise.pedometer;

import cn.ingenic.indroidsync.SportsApp;

public class TennisUtils {
    public static double getCalories(int mins) {
        int weight = SportsApp.getInstance().getSportUser().getWeight();
        if (weight == 0) {
            weight = 65;
        }
        return weight * mins * 9.375 / 3600;
    }
}
