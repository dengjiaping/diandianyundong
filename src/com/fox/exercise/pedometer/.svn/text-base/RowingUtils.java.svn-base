package com.fox.exercise.pedometer;

import cn.ingenic.indroidsync.SportsApp;

public class RowingUtils {
    public static double getCalories(double dis) {
        int weight = SportsApp.getInstance().getSportUser().getWeight();
        if (weight == 0) {
            weight = 65;
        }
        int speed = 5;//km/h
        double hour = dis / speed;
        return weight * hour * 2.675;
    }
}
