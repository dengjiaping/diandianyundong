package com.fox.exercise.pedometer;

import cn.ingenic.indroidsync.SportsApp;

public class SkatingUtils {
    public static double getCalories(double dis) {
        double hour = dis / 15.0;
        int weight = SportsApp.getInstance().getSportUser().getWeight();
        return weight * hour * 1.3375;
    }
}

