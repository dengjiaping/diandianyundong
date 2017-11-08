package com.fox.exercise.weibo.sina;

import android.provider.BaseColumns;

public class AccessInfoColumn implements BaseColumns {
    public AccessInfoColumn() {
    }


    public static final String USERID = "USERID";
    public static final String ACCESS_TOKEN = "ACCESS_TOKEN";
    public static final String ACCESS_SECRET = "ACCESS_SECRET";
    public static final String NICK_NAME = "NICK_NAME";


    public static final int _ID_ACCESS = 0;
    public static final int USERID_COLUMN = 1;
    public static final int ACCESS_TOKEN_COLUMN = 2;
    public static final int ACCESS_SECRET_COLUMN = 3;
    public static final int NICK_NAME_COLUMN = 4;


    public static final String[] PROJECTION =
            {
                    _ID,                    //0
                    USERID,                    //1
                    ACCESS_TOKEN,            //2
                    ACCESS_SECRET,            //3
                    NICK_NAME,              //4
            };
}