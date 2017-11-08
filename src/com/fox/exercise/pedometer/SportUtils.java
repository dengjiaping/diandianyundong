package com.fox.exercise.pedometer;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.util.Log;

import cn.ingenic.indroidsync.SportsApp;

import com.fox.exercise.api.ApiBack;
import com.fox.exercise.api.ApiJsonParser;
import com.fox.exercise.api.ApiNetException;
import com.fox.exercise.api.ApiSessionOutException;
import com.fox.exercise.db.SportDb;

public class SportUtils {

    public static String getFormatTime(int diff) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, diff);
        Date date = cal.getTime();
        String time = format(date);
        return time;
    }

    public static String format(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String time = format.format(date);
        return time;
    }

    public static long diffTime() {
        Date date = new Date();
        String time = format(date);
        time = time + " 23:59:00";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long end = 0;
        try {
            end = sdf.parse(time).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return end - date.getTime();
    }

    public static double getBaseCalories(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                "sports" + SportsApp.getInstance().getSportUser().getUid(), 0);
        int type = sharedPreferences.getInt("type", 0);
        int pace = 0;
        boolean isRunning = type != 0;
        if (type == 0) {
            pace = (int) (SportsApp.getInstance().getSportUser().getHeight() * 0.37);
        } else if (type == 1) {
            pace = (int) (SportsApp.getInstance().getSportUser().getHeight() * 0.37);
        } else if (type == 2) {
            pace = (int) (SportsApp.getInstance().getSportUser().getHeight() * 0.45);
        }
        int weight = SportsApp.getInstance().getSportUser().getWeight();
        weight = (weight == 0 ? 65 : weight);
        double baseCalories = ((weight * 2.2) * (isRunning ? IMPERIAL_RUNNING_FACTOR : IMPERIAL_WALKING_FACTOR)) * pace / 100000.0;
        return baseCalories;
    }

    private static double IMPERIAL_WALKING_FACTOR = 0.517;
    private static double IMPERIAL_RUNNING_FACTOR = 0.75031498;

    public static void saveSportDB(Context context, int step) {
        SharedPreferences spf = context.getSharedPreferences(
                "sports" + SportsApp.getInstance().getSportUser().getUid(), 0);
        int number = spf.getInt("number", 0);
        double mBaseCalories = getBaseCalories(context);
        SportDb helper = SportDb.getInstance(context);
        int stepValue = 0;
        int score_step = 0;
        int score_calories = 0;
        int isUpload = 0;
        int uid = SportsApp.getInstance().getSportUser().getUid();
        Cursor cursor = null;
        try {
            cursor = helper.query(uid, number, getFormatTime(0));
            if (cursor != null && cursor.getCount() > 0 && cursor.moveToFirst()) {
                isUpload = cursor.getInt(cursor.getColumnIndex(SportDb.ISUPLOAD));
                stepValue = cursor.getInt(cursor.getColumnIndex(SportDb.COMPLETE_STEP));
                score_step = cursor.getInt(cursor.getColumnIndex(SportDb.SCORE_STEP));
                score_calories = cursor.getInt(cursor.getColumnIndex(SportDb.SCORE_CALORIES));
            }
            stepValue += step;
            String time = getFormatTime(0);
            ContentValues values = new ContentValues();
            Log.v("TAG", "STEP SERVICE MSTEP: " + stepValue + " " + number);
            int calories = (int) (mBaseCalories * stepValue);
            values.put(SportDb.UID, uid);
            values.put(SportDb.COMPLETE_STEP, stepValue);
            values.put(SportDb.COMPLETE_CALORIES, calories);
            values.put(SportDb.SCORE_CALORIES, score_calories);
            values.put(SportDb.SCORE_STEP, score_step);
            values.put(SportDb.TIME, time);
            values.put(SportDb.NUMBER, number);
            values.put(SportDb.ISUPLOAD, isUpload);
            helper.insert(uid, time, number, values);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            helper.close();
        }
    }

    public static void uploadSport(Context context, final String session_id) {
        SharedPreferences sps = context.getSharedPreferences("sprots_uid", 0);
        int sportUid = sps.getInt("sportsUid", 0);
        SharedPreferences spf = context.getSharedPreferences(
                "sports" + sportUid, 0);
        SportDb helper = SportDb.getInstance(context);
        Cursor cursor = null;
        int calories = 0;
        int step = 0;
        int score_step = spf.getInt("editStep", 6189);
        int score_calories = spf.getInt("editCalories", 400);
        ;
        try {
            cursor = helper.query(sportUid, getFormatTime(0));
            if (cursor != null && cursor.getCount() > 0 && cursor.moveToFirst()) {
                do {
                    calories += cursor.getInt(cursor.getColumnIndex(SportDb.COMPLETE_CALORIES));
                    step += cursor.getInt(cursor.getColumnIndex(SportDb.COMPLETE_STEP));
                } while (cursor.moveToFirst());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            helper.close();
        }
        //TODO 运动类型 写死了
        new SportThread(session_id, step, calories, score_step, score_calories, 0).start();
    }

    public static void uploadSport(final String session_id, int step, int calories, int score_step, int score_calories) {
        //TODO 运动类型 写死了
        new SportThread(session_id, step, calories, score_step, score_calories, 0).start();
    }

    public static class SportThread extends Thread {
        String session_id;
        int step, calories, score_step, score_calories, type;

        public SportThread(String sessionId, int step, int calories
                , int score_step, int score_calories, int type) {
            this.session_id = sessionId;
            this.step = step;
            this.calories = calories;
            this.score_calories = score_calories;
            this.score_step = score_step;
            this.type = type;
        }

        @Override
        public void run() {
            //TODO
            Log.v("TAG", "RESULT --> session_id " + session_id + " step " + step +
                    " calories " + calories + " score_calories " + score_calories +
                    " score_step " + score_step + " type " + type);
            try {
                ApiBack back = ApiJsonParser.uploadSportsType(session_id, type, score_step, score_calories, step, calories);
                Log.v("Tag", "api sportThread :" + back.getMsg());
            } catch (ApiNetException e) {
                e.printStackTrace();
            } catch (ApiSessionOutException e) {
                e.printStackTrace();
            }
        }
    }


}
