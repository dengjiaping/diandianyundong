package com.fox.exercise.db;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class SportsPrivateMessageDB extends SQLiteOpenHelper {
    private SQLiteDatabase db = null;
    private static SportsPrivateMessageDB mInstance = null;
    private static final String DB_NAME = "privatemessage.db";
    private static final String TAB_NAME = "privatemessage";
    private static final String CREATE_TABLE = "create table "
            + TAB_NAME + "(_id integer primary key autoincrement,sex text," +
            "uimg text,name text,birthday text,uid integer,addTime text)";

    private SportsPrivateMessageDB(Context context) {
        super(context, DB_NAME, null, 2);
    }

    public synchronized static SportsPrivateMessageDB getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SportsPrivateMessageDB(context);
        }
        return mInstance;
    }

    public SportsPrivateMessageDB(Context context, String name, CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        this.db = db;
        db.execSQL(CREATE_TABLE);
    }

    public void insert(ContentValues values) {
        if (db == null) {
            db = getWritableDatabase();
        }
        db.insert(TAB_NAME, null, values);
    }

    public void insert(ContentValues values, SQLiteDatabase db) {
        db.insert(TAB_NAME, null, values);
    }

    // 根据uid查询单条数据
    public Cursor query(int uid) {
        if (db == null) {
            db = getReadableDatabase();
        }
        return db.rawQuery("select * from " + TAB_NAME + " where " + "uid=?",
                new String[]{String.valueOf(uid)});
    }

    // 查询所有的数据
    public Cursor query() {
        if (db == null) {
            db = getReadableDatabase();
        }
        return db.rawQuery("select * from " + TAB_NAME, null);
    }

    public void delete(int uid) {
        if (db == null) {
            db = getReadableDatabase();
        }
        db.delete(TAB_NAME, "uid=?", new String[]{String.valueOf(uid)});
    }

    public void delete() {
        if (db == null) {
            db = getReadableDatabase();
        }
        db.delete(TAB_NAME, null, null);
    }

    @Override
    public synchronized void close() {
        if (db != null) {
            db.close();
            db = null;
        }
        super.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
