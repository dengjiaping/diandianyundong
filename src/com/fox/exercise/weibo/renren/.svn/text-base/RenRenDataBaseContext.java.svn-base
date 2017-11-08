package com.fox.exercise.weibo.renren;

import android.content.Context;


public class RenRenDataBaseContext {

    private static RenrenDataHelper dataHelper;

    private static Object INSTANCE_LOCK = new Object();

    public static RenrenDataHelper getInstance(Context context) {
        synchronized (INSTANCE_LOCK) {
            if (dataHelper == null) {
                dataHelper = new RenrenDataHelper(context);
            }
            return dataHelper;
        }
    }
}
