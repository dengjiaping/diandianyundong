package com.fox.exercise.weibo.tencent;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import com.fox.exercise.AllWeiboInfo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.Log;

public class TencentDataHelper {
    // 数据库名称
    private static String DB_NAME = "weibo.db";
    // 数据库版本
    private static int DB_VERSION = 2;
    private SQLiteDatabase db;
    private SqliteHelper dbHelper;

    public TencentDataHelper(Context context) {
        dbHelper = new SqliteHelper(context, DB_NAME, null, DB_VERSION);
        db = dbHelper.getWritableDatabase();
    }

    public void Close() {
        db.close();
        dbHelper.close();
    }

    public void clear() {
        db.execSQL("delete from " + AllWeiboInfo.TENCENT_TOKEN_DB_TABLE);
    }

    public String getToken() {
        String token = "";
        String sql = "select * from users";
        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            token = cursor.getString(2);
        }
        return token;
    }

    public String getSecret() {
        String secret = "";
        String sql = "select * from users";
        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            secret = cursor.getString(3);
        }
        return secret;
    }

    // 获取users表中的UserID、Access Token、Access Secret的记录
    public List<UserInfo> GetUserList(Boolean isSimple) {
        List<UserInfo> userList = new ArrayList<UserInfo>();
        Cursor cursor = db.query(SqliteHelper.TB_NAME, null, null, null, null, null, UserInfo.ID + " DESC");
        cursor.moveToFirst();
        while (!cursor.isAfterLast() && (cursor.getString(1) != null)) {
            UserInfo user = new UserInfo();
            user.setId(cursor.getString(0));
            user.setUserId(cursor.getString(1));
            user.setToken(cursor.getString(2));
            user.setUserName(cursor.getString(4));
            user.setUserNick(cursor.getString(5));
            user.setExpiresIn(cursor.getString(7));
            user.setOpenID(cursor.getString(8));
            user.setOpenKey(cursor.getString(9));
            if (!isSimple) {
                ByteArrayInputStream stream = new ByteArrayInputStream(cursor.getBlob(6));
                Drawable icon = Drawable.createFromStream(stream, "image");
                user.setUserIcon(icon);
            }
            userList.add(user);
            cursor.moveToNext();
        }
        cursor.close();
        return userList;
    }

    // 判断users表中的是否包含某个UserID的记录
    public Boolean HaveUserInfo(String UserId) {
        Boolean b = false;
        Cursor cursor = db.query(SqliteHelper.TB_NAME, null, UserInfo.USERID + "=?", new String[]{UserId}, null,
                null, null);
        b = cursor.moveToFirst();
        Log.e("HaveUserInfo", b.toString());
        cursor.close();
        return b;
    }

    // 更新users表的记录，根据UserId更新用户昵称和用户图标
    public int UpdateUserInfo(String userName, Bitmap userIcon, String UserId) {
        ContentValues values = new ContentValues();
        values.put(UserInfo.USERNAME, userName);
        // BLOB类型
        final ByteArrayOutputStream os = new ByteArrayOutputStream();
        // 将Bitmap压缩成PNG编码，质量为100%存储
        userIcon.compress(Bitmap.CompressFormat.PNG, 100, os);
        // 构造SQLite的Content对象，这里也可以使用raw
        values.put(UserInfo.USERICON, os.toByteArray());
        int id = db.update(SqliteHelper.TB_NAME, values, UserInfo.USERID + "=?", new String[]{UserId});
        Log.e("UpdateUserInfo2", id + "");
        return id;
    }

    // 更新users表的记录
    public int UpdateUserInfo(UserInfo user) {
        ContentValues values = new ContentValues();
        values.put(UserInfo.USERID, user.getUserId());
        values.put(UserInfo.USERNAME, user.getUserName());
        values.put(UserInfo.TOKEN, user.getToken());
        values.put(UserInfo.EXPRIESIN, user.getExpiresIn());
        values.put(UserInfo.USERNICK, user.getUserNick());
        values.put(UserInfo.OPENID, user.getOpenID());
        values.put(UserInfo.OPENKEY, user.getOpenKey());
        int id = db.update(SqliteHelper.TB_NAME, values, UserInfo.USERID + "=" + user.getUserId(), null);
        Log.d("UpdateUserInfo", id + "");
        return id;
    }

    public void clearDB() {
        db.execSQL("delete from " + SqliteHelper.TB_NAME);
    }

    // 添加users表的记录
    public Long SaveUserInfo(UserInfo user) {
        ContentValues values = new ContentValues();
        values.put(UserInfo.USERID, user.getUserId());
        values.put(UserInfo.USERNAME, user.getUserName());
        values.put(UserInfo.TOKEN, user.getToken());
        values.put(UserInfo.EXPRIESIN, user.getExpiresIn());
        values.put(UserInfo.USERNICK, user.getUserNick());
        values.put(UserInfo.OPENID, user.getOpenID());
        values.put(UserInfo.OPENKEY, user.getOpenKey());
        Long uid = db.insert(SqliteHelper.TB_NAME, UserInfo.ID, values);
        Log.d("SaveUserInfo", uid + "");
        return uid;
    }

    // 添加users表的记录
    public Long SaveUserInfo(UserInfo user, byte[] icon) {
        ContentValues values = new ContentValues();
        values.put(UserInfo.USERID, user.getUserId());
        values.put(UserInfo.USERNAME, user.getUserName());
        values.put(UserInfo.USERNICK, user.getUserNick());
        values.put(UserInfo.TOKEN, user.getToken());
        values.put(UserInfo.EXPRIESIN, user.getExpiresIn());
        values.put(UserInfo.OPENID, user.getOpenID());
        values.put(UserInfo.OPENKEY, user.getOpenKey());
        if (icon != null) {
            values.put(UserInfo.USERICON, icon);
        }
        Long uid = db.insert(SqliteHelper.TB_NAME, UserInfo.ID, values);
        Log.e("SaveUserInfo", uid + "");
        return uid;
    }

    // 删除users表的记录
    public int DelUserInfo(String UserId) {
        int id = db.delete(SqliteHelper.TB_NAME, UserInfo.USERID + "=?", new String[]{UserId});
        Log.e("DelUserInfo", id + "");
        return id;
    }

    public static UserInfo getUserByName(String userName, List<UserInfo> userList) {
        UserInfo userInfo = null;
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
        int ret = db.delete(SqliteHelper.TB_NAME, null, null);
        return ret > 0 ? true : false;
    }

}