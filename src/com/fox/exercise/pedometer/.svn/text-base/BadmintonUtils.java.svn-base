package com.fox.exercise.pedometer;

import cn.ingenic.indroidsync.SportsApp;

public class BadmintonUtils {
    public static double getCalories(int mins) {
        int weight = SportsApp.getInstance().getSportUser().getWeight();
        if (weight == 0) {
            weight = 65;
        }
        return weight * mins * 8.0375 / 3600;
    }
}
