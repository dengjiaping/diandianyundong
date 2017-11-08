package com.fox.exercise.pedometer;

import cn.ingenic.indroidsync.SportsApp;

public class SwimmingUtils {

    public static final int SWIM_FRONT_CRAWL = 1;
    public static final int SWIM_BUTTERFLY = 2;
    public static final int SWIM_BREAST_STROKE = 3;
    public static final int SWIM_BACK_STROKE = 4;
//	public static final int SWIM_SIDE_STROKE = 5;

    public static double getCalories(double dis, int second, int swimType) {
        int weight = SportsApp.getInstance().getSportUser().getWeight();
        double speed = getSpeedFactor(dis, second);    //meters,minutes
        double met = getMet(speed, swimType);
        return cckg(dis, weight, met);
    }

    public static double getMet(double speed_factor, int swimType) {
        double met = -1;
        if (swimType == SWIM_FRONT_CRAWL) {
            if (speed_factor > 2.5) {
                met = 10;
            } else if (speed_factor > 1.5) {
                met = 8.3;
            } else {
                met = 5.8;
            }
        } else if (swimType == SWIM_BREAST_STROKE) {
            if (speed_factor > 2.5) {
                met = 10.3;
            } else if (speed_factor > 1.5) {
                met = 7.8;
            } else {
                met = 5.3;
            }
        } else if (swimType == SWIM_BUTTERFLY) {
            met = 13.8;
        } else if (swimType == SWIM_BACK_STROKE) {
            if (speed_factor > 2.5) {
                met = 9.5;
            } else if (speed_factor > 1.5) {
                met = 7.15;
            } else {
                met = 4.8;
            }
        }
//	 	else if (swimType == SWIM_SIDE_STROKE){	
//	 		met=7;	
//	 	}	
        return met;
    }

    public static double cckg(double dis, int weight, double met) {
        double mins = dis / 6;
        return Math.round(met * 3.5 * (weight) / 200 * mins);
    }

    public static double getSpeedFactor(double distance, int second) {
        double distance_type_factor = 1.0936133;//meter to yard
        double speed_factor = -1;
        speed_factor = ((distance * distance_type_factor) / 25) / second / 60;
        return speed_factor;
    }

//	public static double getSpeed(double distance, int second){
//		return (distance/1000)/(second/3600);
//	}
}