package com.fox.exercise.db;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

public class SportDb extends SQLiteOpenHelper {
    private SQLiteDatabase db = null;
    private static SportDb mInstance = null;
    public final static String DB_NAME = "sport_db.db";
    public final static String TAB_NAME = "sport";
    public final static String ID = "_id";
    public final static String UID = "uid";
    public final static String COMPLETE_STEP = "complete_step";
    public final static String COMPLETE_CALORIES = "complete_calories";
    public final static String SCORE_STEP = "score_step";
    public final static String SCORE_CALORIES = "score_calories";
    public final static String TIME = "time";
    public final static String NUMBER = "number"; //切换
    public final static String ISUPLOAD = "isupload"; //0：未上传 1：已经上传
    public String CREATE_TAB = "create table " + TAB_NAME + " (_id integer primary key autoincrement," +
            UID + " integer," + COMPLETE_STEP + " integer," + COMPLETE_CALORIES + "" +
            " integer," + SCORE_STEP + " integer," + SCORE_CALORIES + " integer," +
            NUMBER + " integer," + ISUPLOAD + " integer," + TIME + " text)";

    private SportDb(Context context) {
        super(context, DB_NAME, null, 2);
    }

    public synchronized static SportDb getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SportDb(context);
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

/*	public void insert(String time,ContentValues values){
		if (db == null){
			db = getWritableDatabase();
		}
		db.delete(TAB_NAME, "time=?", new String[]{time});
		db.insert(TAB_NAME, null, values);
	}*/

    public void update(int uid, String time, ContentValues values) {
        if (db == null) {
            db = getWritableDatabase();
        }
        db.update(TAB_NAME, values, "uid=? and time=?", new String[]{String.valueOf(uid), time});
    }

    public void insert(int uid, String time, int number, ContentValues values) {
        if (db == null) {
            db = getWritableDatabase();
        }
        db.delete(TAB_NAME, "time=? and uid=? and number=?", new String[]{time, String.valueOf(uid), String.valueOf(number)});
        db.insert(TAB_NAME, null, values);
    }

    public void insert(ContentValues values) {
        if (db == null) {
            db = getWritableDatabase();
        }
        db.insert(TAB_NAME, null, values);
    }

    public Cursor query(int uid, String time) {
        if (db == null) {
            db = getWritableDatabase();
        }
        return db.rawQuery("select * from " + TAB_NAME + " where uid=? and time=? order by _id desc", new String[]{String.valueOf(uid), time});
    }

    public Cursor query(int uid, int number, String time) {
        if (db == null) {
            db = getWritableDatabase();
        }
        return db.rawQuery("select * from " + TAB_NAME + " where uid=? and time=? and number=?", new String[]{String.valueOf(uid), time, String.valueOf(number)});
    }

    public Cursor query(int uid, int times) {
        if (db == null) {
            db = getWritableDatabase();
        }
        return db.rawQuery("select * from " + TAB_NAME + " where uid=? GROUP BY time order by _id desc limit ?,?", new String[]{String.valueOf(uid), String.valueOf(times), String.valueOf(times + 1)});
    }

    @Override
    public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {

    }

}
