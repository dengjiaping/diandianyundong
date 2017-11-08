package com.fox.exercise.weibo.tencent;

import android.content.Context;


public class DataBaseContext {

    private static TencentDataHelper dataHelper;

    private static Object INSTANCE_LOCK = new Object();

    public static TencentDataHelper getInstance(Context context) {
        synchronized (INSTANCE_LOCK) {
            if (dataHelper == null) {
                dataHelper = new TencentDataHelper(context);
            }
            return dataHelper;
        }
    }
}
