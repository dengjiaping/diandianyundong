package com.fox.exercise.weibo.sina;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class SinaDBDao {
    private SQLiteDatabase db = null;
    private DBHelper helper = null;

    public SinaDBDao(Context context) {
        helper = new DBHelper(context);
        db = helper.getWritableDatabase();
    }

    public String getAccessToken() {
        String token = "";
        String sql = "select * from accessinfo";
        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            token = cursor.getString(2);
        }
        cursor.close();
        return token;
    }

    public String getTokenSecret() {
        String secret = "";
        db = helper.getWritableDatabase();
        String sql = "select * from accessinfo";
        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            secret = cursor.getString(3);
        }
        cursor.close();
        return secret;
    }

    public void closeDB() {
        db.close();
        helper.close();
    }
}

