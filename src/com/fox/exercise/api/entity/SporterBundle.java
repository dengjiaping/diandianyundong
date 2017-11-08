package com.fox.exercise.api.entity;

import com.fox.exercise.location.SportLocationInfo;

public class SporterBundle {

    private static SporterBundle mInstance = null;

    private static Object lock = new Object();

    public static SporterBundle getInstance() {
        if (mInstance == null) {
            synchronized (lock) {
                if (mInstance == null) {
                    mInstance = new SporterBundle();
                }
                return mInstance;
            }
        }
        return mInstance;
    }

    private String sessionId = null;
    private SportLocationInfo location = null;
    private int steps = 0;
    private int calories = 0;
    private int scoreStep = 0;
    private int scoreCalories = 0;
    private int sportsType = 0;
    private String name;

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public SportLocationInfo getLocation() {
        return location;
    }

    public void setLocation(SportLocationInfo location) {
        this.location = location;
    }

    public void setLocationName(String n) {
        this.name = n;
    }

    public String getLocationName() {
        return name;
    }

    public int getSteps() {
        return steps;
    }

    public void setSteps(int steps) {
        this.steps = steps;
    }

    public int getCalories() {
        return calories;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public int getScoreStep() {
        return scoreStep;
    }

    public void setScoreStep(int scoreStep) {
        this.scoreStep = scoreStep;
    }

    public int getScoreCalories() {
        return scoreCalories;
    }

    public void setScoreCalories(int scoreCalories) {
        this.scoreCalories = scoreCalories;
    }

    public int getSportsType() {
        return sportsType;
    }

    public void setSportsType(int sportsType) {
        this.sportsType = sportsType;
    }

}
