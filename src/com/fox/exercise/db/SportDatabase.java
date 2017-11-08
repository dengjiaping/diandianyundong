package com.fox.exercise.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SportDatabase extends SQLiteOpenHelper {

    private SQLiteDatabase datebase = null;
    private static SportDatabase mInstance = null;
    public static String DB_NAME = "watch_db";
    public static String TAB_NAME_ONE = "watch_one";
    public static String TAB_NAME_TWO = "watch_two";
    public static String TAB_NAME_THREE = "watch_three";

    public String CREATE_TAB_ONE = "create table " + TAB_NAME_ONE
            + " (_id integer primary key autoincrement," + "date text,"
            + "type integer)";
    public String CREATE_TAB_TWO = "create table " + TAB_NAME_TWO
            + " (_id integer primary key autoincrement," + "timer text)";
    public String CREATE_TAB_THREE = "create table " + TAB_NAME_THREE
            + " (_id integer primary key autoincrement,"
            + "step text,dis text,cal text)";
    private Context mContext;

    private SportDatabase(Context context) {
        super(context, DB_NAME, null, 2);
        mContext = context;
    }

    public synchronized static SportDatabase getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SportDatabase(context);
        }
        return mInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        this.datebase = db;
        datebase.execSQL(CREATE_TAB_ONE);
        datebase.execSQL(CREATE_TAB_TWO);
        datebase.execSQL(CREATE_TAB_THREE);
        // ContentValues values = new ContentValues();
        // values.put("timer", "00:00:00");
        // values.put("step", "0");
        // values.put("dis", "0.000");
        // values.put("cal", "0");
        // values.put("date", "0");
        // values.put("type", 0);
        // mInstance.insert(values);
    }

    @Override
    public synchronized void close() {
        if (datebase != null) {
            datebase.close();
            datebase = null;
        }
        super.close();
    }

    // 插入数据(日期和类型)
    public void insertDateType(ContentValues values) {
        if (datebase == null) {
            datebase = getWritableDatabase();
        }
        int one = (int) datebase.insert(TAB_NAME_ONE, null, values);
        Log.i("datebase", "one:" + one);
    }

    // 插入数据(时间)
    public void insertData(ContentValues values) {
        if (datebase == null) {
            datebase = getWritableDatabase();
        }
        int two = (int) datebase.insert(TAB_NAME_TWO, null, values);
        Log.i("datebase", "two:" + two);
    }

    // 插入数据(路程，卡路里和步数)
    public void insertOther(ContentValues values) {
        if (datebase == null) {
            datebase = getWritableDatabase();
        }
        int three = (int) datebase.insert(TAB_NAME_THREE, null, values);
        Log.i("datebase", "three:" + three);
    }

    // 更新日期和类型
    public void updateTypeDate(ContentValues values) {
        if (datebase == null) {
            datebase = getWritableDatabase();
        }
        // long id = datebase.update(TAB_NAME_ONE, values, "_id=",
        // new String[] { Integer.toString(0) });
        long id = datebase.update(TAB_NAME_ONE, values, null, null);
        Log.i("datebase", "id_1:" + id);
    }

    // 更新时间
    public void updateTime(ContentValues values) {
        if (datebase == null) {
            datebase = getWritableDatabase();
        }
        // long id = datebase.update(TAB_NAME_TWO, values, "_id=",
        // new String[] { Integer.toString(0) });
        long id = datebase.update(TAB_NAME_TWO, values, null, null);
        Log.i("datebase", "id_2:" + id);
    }

    // 更新步数,距离，卡路里
    public void updateOther(ContentValues values) {
        if (datebase == null) {
            datebase = getWritableDatabase();
        }
        // long id = datebase.update(TAB_NAME_THREE, values, "_id=",
        // new String[] { Integer.toString(0) });
        long id = datebase.update(TAB_NAME_THREE, values, null, null);
        Log.i("datebase", "id_1:" + id);
    }

    // 查询数据(日期和类型)
    public Cursor queryOne() {
        if (datebase == null) {
            datebase = getWritableDatabase();
        }
        return datebase.rawQuery("select * from " + TAB_NAME_ONE, null);
    }

    // 查询数据(时间)
    public Cursor queryTwo() {
        if (datebase == null) {
            datebase = getWritableDatabase();
        }
        return datebase.rawQuery("select * from " + TAB_NAME_TWO, null);
    }

    // 查询数据(步数,距离，卡路里)
    public Cursor queryThree() {
        if (datebase == null) {
            datebase = getWritableDatabase();
        }
        return datebase.rawQuery("select * from " + TAB_NAME_THREE, null);
    }

    // 删除数据(日期和类型)
    public void deleteOne() {
        if (datebase == null) {
            datebase = getWritableDatabase();
        }
        datebase.delete(TAB_NAME_ONE, null, null);
    }

    // 删除数据(时间)
    public void deleteTwo() {
        if (datebase == null) {
            datebase = getWritableDatabase();
        }
        datebase.delete(TAB_NAME_TWO, null, null);
    }

    // 删除数据(步数,距离，卡路里)
    public void deleteThree() {
        if (datebase == null) {
            datebase = getWritableDatabase();
        }
        datebase.delete(TAB_NAME_THREE, null, null);
    }

    @Override
    public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {

    }

}
