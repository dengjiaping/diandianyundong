package com.fox.exercise.db;


import java.util.ArrayList;

import com.fox.exercise.CurrentTimeList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SportsTimeDB extends SQLiteOpenHelper {
    private SQLiteDatabase db = null;
    private static SportsTimeDB mInstance = null;
    public final static String DB_NAME = "sport_time_db.db";
    public final static String TAB_NAME = "sport_time";
    public final static String ID = "_id";
    public final static String UID = "uid";
    public final static String TIME = "time";
    public final static String DATE = "date";
    public String CREATE_TAB = "create table " + TAB_NAME + " (_id integer primary key autoincrement," +
            UID + " integer," +
            TIME + " integer," + DATE + " text)";

    private SportsTimeDB(Context context) {
        super(context, DB_NAME, null, 2);
    }

    public synchronized static SportsTimeDB getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SportsTimeDB(context);
        }
        return mInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        this.db = db;
        db.execSQL(CREATE_TAB);
    }

    @Override
    public synchronized void close() {
        if (db != null) {
            db.close();
            db = null;
        }
        super.close();
    }

    public void update(int uid, String time, ContentValues values) {
        if (db == null) {
            db = getWritableDatabase();
        }
        db.update(TAB_NAME, values, "uid=? and time=?", new String[]{String.valueOf(uid), time});
    }

    public void insert(ContentValues values) {
        if (db == null) {
            db = getWritableDatabase();
        }
        db.insert(TAB_NAME, null, values);
    }

    public Cursor query(int uid, String date) {
        if (db == null) {
            db = getWritableDatabase();
        }
        return db.rawQuery("select * from " + TAB_NAME + " where uid=? and date=?", new String[]{String.valueOf(uid), date});
    }

    @Override
    public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {

    }

    public boolean delete(int uid, String date) {
        boolean result = true;
        if (db == null) {
            db = getWritableDatabase();
        }
        int id = db.delete(TAB_NAME, "uid=? and date=?", new String[]{String.valueOf(uid), date});

        if (id > 0) {
            Log.i("数据库删除", "删除数据成功，id=" + id);
            result = true;
        } else {
            Log.i("数据库删除", "删除数据失败，id=" + id);
            result = false;
        }
        return result;
    }

    public ArrayList<CurrentTimeList> getTasksList(int uid) {
        if (db == null) {
            db = getReadableDatabase();
        }
        ArrayList<CurrentTimeList> times = null;
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("select * from sport_time where uid=? or uid=0 order by date desc",
                    new String[]{Integer.toString(uid)});
            if (cursor.moveToFirst()) {
                times = new ArrayList<CurrentTimeList>();
                do {
                    CurrentTimeList time = new CurrentTimeList();
                    time.setCurrentTime(cursor.getString(cursor.getColumnIndex("date")));
                    times.add(time);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                try {
                    cursor.close();
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
        }
        return times;
    }

}
