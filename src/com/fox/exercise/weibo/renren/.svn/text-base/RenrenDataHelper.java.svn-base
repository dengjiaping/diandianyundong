package com.fox.exercise.weibo.renren;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class RenrenDataHelper {
    // database name
    private static String DB_NAME = "renrenweibo.db";
    // db version
    private static int DB_VERSION = 2;
    private SQLiteDatabase db;
    private RenrenSqliteHelper dbHelper;

    public RenrenDataHelper(Context context) {
        dbHelper = new RenrenSqliteHelper(context, DB_NAME, null, DB_VERSION);
        db = dbHelper.getWritableDatabase();
    }

    public void Close() {
        db.close();
        dbHelper.close();
    }

    public void updateRefreshToken(String s) {
        String token = getAccessToken();
        db.execSQL("update " + RenrenSqliteHelper.TB_NAME
                + " set refresh_token='" + s + "' where "
                + UserData.ACCESS_TOKEN + "='" + token + "'");
    }

    public void clear() {
        db.execSQL("delete from " + RenrenSqliteHelper.TB_NAME);
    }

    public String getRefreshToken() {
        String refreshToken = "";

        Cursor cursor = db.rawQuery("select * from "
                + RenrenSqliteHelper.TB_NAME, null);
        while (cursor.moveToNext()) {
            refreshToken = cursor.getString(5);
        }

        return refreshToken;
    }

    public void updateRefreshAndToken(String accessToken, String refreshToken) {
        db.beginTransaction();
        Cursor cursor = db.rawQuery("select * from "
                + RenrenSqliteHelper.TB_NAME, null);
        int id = 0;
        while (cursor.moveToNext()) {
            id = cursor.getInt(0);
        }
        db.execSQL("update " + RenrenSqliteHelper.TB_NAME
                + " set refresh_token='" + refreshToken + "',access_token='"
                + accessToken + "' where " + UserData.ID + "=" + id);
        db.setTransactionSuccessful();
        db.endTransaction();
    }

    public String getAccessToken() {
        String token = "";
        String sql = "select * from userdata";
        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            token = cursor.getString(3);
        }
        return token;
    }

    public String getSignature() {
        String signature = "";
        String sql = "select * from userdata";
        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            signature = cursor.getString(4);
        }
        return signature;
    }

    // get user info from userlist
    public List<UserData> GetUserList() {
        List<UserData> userList = new ArrayList<UserData>();
        Cursor cursor = db.query(RenrenSqliteHelper.TB_NAME, null, null, null,
                null, null, UserData.ID + " DESC");
        cursor.moveToFirst();
        while (!cursor.isAfterLast() && (cursor.getString(1) != null)) {
            UserData user = new UserData();
            user.setId(cursor.getString(0));
            user.setUserId(cursor.getString(1));
            user.setUserName(cursor.getString(2));
            user.setToken(cursor.getString(3));
            AccessData.access_token = cursor.getString(3);
            user.setHeadurl(cursor.getString(4));
            // if (!isSimple) {
            // user.setUserName(cursor.getString(1));
            // ByteArrayInputStream stream = new ByteArrayInputStream(
            // cursor.getBlob(5));
            // Drawable icon = Drawable.createFromStream(stream, "image");
            // user.setUserIcon(icon);
            // }
            userList.add(user);
            cursor.moveToNext();
        }
        cursor.close();
        return userList;
    }

    // if user id in the list
    public Boolean HaveUserInfo(String UserId) {
        Boolean b = false;
        Cursor cursor = db.query(RenrenSqliteHelper.TB_NAME, null,
                UserData.USER_ID + "=?", new String[]{UserId}, null, null,
                null);
        b = cursor.moveToFirst();
        Log.e("HaveUserInfo", b.toString());
        cursor.close();
        return b;
    }

    // update userinfo based on the username and user icon, user id.
    // public int UpdateUserInfo(String userName, Bitmap userIcon, String
    // UserId) {
    // ContentValues values = new ContentValues();
    // values.put(UserInfo.USER_NAME, userName);
    //
    //
    // final ByteArrayOutputStream os = new ByteArrayOutputStream();
    //
    // userIcon.compress(Bitmap.CompressFormat.PNG, 100, os);
    //
    // values.put(UserInfo.USERICON, os.toByteArray());
    // int id = db.update(SqliteHelper.TB_NAME, values, UserInfo.ID + "=?", new
    // String[]{UserId});
    // Log.e("UpdateUserInfo2", id + "");
    // return id;
    // }

    public int UpdateUserInfo(UserData user) {
        ContentValues values = new ContentValues();
        values.put(UserData.ID, user.getId());
        values.put(UserData.USER_ID, user.getUserId());
        values.put(UserData.USER_NAME, user.getUserName());
        // values.put(UserData.TINY_URL, user.getTinyurl());
        // values.put(UserData.HEAD_URL, user.getHeadurl());
        values.put(UserData.ACCESS_TOKEN, user.getToken());
        values.put(UserData.SIGNATURE, user.getSignature());
        int id = db.update(RenrenSqliteHelper.TB_NAME, values, UserData.ID
                + "=" + user.getId(), null);
        Log.e("UpdateUserInfo", id + "");
        return id;
    }

    public Long SaveUserInfo(UserData user) {
        ContentValues values = new ContentValues();
        values.put(UserData.ID, user.getId());
        values.put(UserData.USER_ID, user.getUserId());
        values.put(UserData.USER_NAME, user.getUserName());
        // values.put(UserData.TINY_URL, user.getTinyurl());
        // values.put(UserData.HEAD_URL, user.getHeadurl());
        values.put(UserData.ACCESS_TOKEN, user.getToken());
        values.put(UserData.SIGNATURE, user.getSignature());
        if (!db.isOpen()) {
            db = dbHelper.getWritableDatabase();
        }
        Long uid = db.insert(RenrenSqliteHelper.TB_NAME, UserData.ID, values);
        Log.e("SaveUserInfo", uid + "");
        return uid;
    }

    // public Long SaveUserInfo(UserInfo user,byte[] icon) {
    // ContentValues values = new ContentValues();
    // values.put(UserInfo.ID, user.getId());
    // values.put(UserInfo.USER_NAME, user.getUserName());
    // values.put(UserInfo.TOKEN, user.getToken());
    // values.put(UserInfo.TOKENSECRET, user.getTokenSecret());
    // if(icon!=null){
    // values.put(UserInfo.USERICON, icon);
    // }
    // Long uid = db.insert(SqliteHelper.TB_NAME, UserInfo.id, values);
    // Log.e("SaveUserInfo", uid + "");
    // return uid;
    // }

    public int DelUserInfo(String UserId) {
        int id = db.delete(RenrenSqliteHelper.TB_NAME, UserData.ID + "=?",
                new String[]{UserId});
        Log.e("DelUserInfo", id + "");
        return id;
    }

    public static UserData getUserByName(String userName,
                                         List<UserData> userList) {
        UserData userInfo = null;
        int size = userList.size();
        for (int i = 0; i < size; i++) {
            if (userName.equals(userList.get(i).getUserName())) {
                userInfo = userList.get(i);
                break;
            }
        }
        return userInfo;
    }

    public boolean delete() {
        int ret = db.delete(RenrenSqliteHelper.TB_NAME, null, null);
        return ret > 0 ? true : false;
    }
}