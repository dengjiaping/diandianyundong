package com.fox.exercise.weibo.sina;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class AccessInfoHelper {
    private DBHelper dbHelper;
    private SQLiteDatabase newsDB;
    private Context context;

    private static int mutext = 0;

    public AccessInfoHelper(Context context) {
        this.context = context;
    }

    public AccessInfoHelper open() {
        if (mutext == 0) {
            mutext++;
            dbHelper = new DBHelper(this.context);

            newsDB = dbHelper.getWritableDatabase();
            return this;
        } else {
            return null;
        }

    }

    public void close() {
        if (newsDB != null)
            if (newsDB.isOpen())
                newsDB.close();
        if (dbHelper != null) {
            dbHelper.close();
        }
        mutext = 0;
    }

    public long create(AccessInfo accessInfo) {
        ContentValues values = new ContentValues();

        values.put(AccessInfoColumn.USERID, accessInfo.getUserID());
        values.put(AccessInfoColumn.ACCESS_TOKEN, accessInfo.getAccessToken());
        values.put(AccessInfoColumn.ACCESS_SECRET, accessInfo.getAccessSecret());
        values.put(AccessInfoColumn.NICK_NAME, accessInfo.getNickName());

        return newsDB.insert(DBHelper.ACCESSLIB_TABLE, null, values);
    }

    public boolean update(AccessInfo accessInfo) {
        ContentValues values = new ContentValues();

        values.put(AccessInfoColumn.USERID, accessInfo.getUserID());
        values.put(AccessInfoColumn.ACCESS_TOKEN, accessInfo.getAccessToken());
        values.put(AccessInfoColumn.ACCESS_SECRET, accessInfo.getAccessSecret());
        values.put(AccessInfoColumn.NICK_NAME, accessInfo.getNickName());

        String whereClause = AccessInfoColumn.USERID + "=" + accessInfo.getUserID();

        return newsDB.update(DBHelper.ACCESSLIB_TABLE, values, whereClause, null) > 0;
    }

    public ArrayList<AccessInfo> getAccessInfos() {
        ArrayList<AccessInfo> list = new ArrayList<AccessInfo>();

        AccessInfo accessInfo = null;
        Cursor cursor = newsDB.query(DBHelper.ACCESSLIB_TABLE, AccessInfoColumn.PROJECTION, null, null, null, null,
                null);

        if (cursor.getCount() > 0) {
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                accessInfo = new AccessInfo();

                accessInfo.setUserID(cursor.getString(AccessInfoColumn.USERID_COLUMN));
                accessInfo.setAccessToken(cursor.getString(AccessInfoColumn.ACCESS_TOKEN_COLUMN));
                accessInfo.setAccessSecret(cursor.getString(AccessInfoColumn.ACCESS_SECRET_COLUMN));
                accessInfo.setNickName(cursor.getString(AccessInfoColumn.NICK_NAME_COLUMN));
                list.add(accessInfo);
            }
        }

        if(cursor!=null){
            cursor.close();
            cursor = null;
        }


        return list;
    }

    public void clear() {
        newsDB.execSQL("delete from " + DBHelper.ACCESSLIB_TABLE);
    }

    public AccessInfo getAccessInfo(String userID) {
        AccessInfo accessInfo = null;
        String selection = AccessInfoColumn.USERID + "=" + userID;

        Cursor cursor = newsDB.query(DBHelper.ACCESSLIB_TABLE, AccessInfoColumn.PROJECTION, selection, null, null,
                null, null);

        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            accessInfo = new AccessInfo();
            accessInfo.setUserID(cursor.getString(AccessInfoColumn.USERID_COLUMN));
            accessInfo.setAccessToken(cursor.getString(AccessInfoColumn.ACCESS_TOKEN_COLUMN));
            accessInfo.setAccessSecret(cursor.getString(AccessInfoColumn.ACCESS_SECRET_COLUMN));
            accessInfo.setNickName(cursor.getString(AccessInfoColumn.NICK_NAME_COLUMN));
        }
        if(cursor!=null){
            cursor.close();
            cursor=null;
        }
        return accessInfo;
    }

    public boolean delete() {
        int ret = newsDB.delete(DBHelper.ACCESSLIB_TABLE, null, null);
        return ret > 0 ? true : false;
    }
}