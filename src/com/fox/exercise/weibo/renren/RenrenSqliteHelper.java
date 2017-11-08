package com.fox.exercise.weibo.renren;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class RenrenSqliteHelper extends SQLiteOpenHelper {
    public static final String TB_NAME = "userdata";

    public RenrenSqliteHelper(Context context, String name, CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    //create
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + TB_NAME);
        Log.d("renren database", "create table");
        db.execSQL("CREATE TABLE " +
                TB_NAME + "(" +
                UserData.ID + " integer primary key," +
                UserData.USER_ID + " varchar," +
                UserData.USER_NAME + " varchar," +
                //UserData.TINY_URL+" varchar,"+
                //UserData.HEAD_URL+" varchar,"+
                UserData.ACCESS_TOKEN + " varchar," +
                UserData.SIGNATURE + " varchar," +
                "refresh_token varchar" +
                ")"
        );
        Log.e("Database", "onCreate");
    }

    //update
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TB_NAME);
        onCreate(db);
        Log.e("Database", "onUpgrade");
    }

    //update column
    public void updateColumn(SQLiteDatabase db, String oldColumn, String newColumn, String typeColumn) {
        try {
            db.execSQL("ALTER TABLE " +
                    TB_NAME + " CHANGE " +
                    oldColumn + " " + newColumn +
                    " " + typeColumn
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}