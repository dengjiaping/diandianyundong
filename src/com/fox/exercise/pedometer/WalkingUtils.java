package com.fox.exercise.pedometer;

import cn.ingenic.indroidsync.SportsApp;

public class WalkingUtils {
    public static double getCalories(double dis) {
        int weight = SportsApp.getInstance().getSportUser().getWeight();
        if (weight == 0) {
            weight = 65;
        }
        return cckg(weight, dis / 4.0 * 60);
    }


    private static double cckg(int weight, double mins) {
        return Math.round(3.0 * 3.5 * weight / 200 * mins);
    }
}
