package com.fox.exercise.util;

/**
 * Created by suhu on 2016/9/22.
 * 只用修改isShow值就可以控制log日志的显示与隐藏
 * （用此工具类就可以不用在代码中一个一个的手动清楚log）
 *
 */

public class LogUtils{
    private static boolean isShow =false;

    public static void v (String tag ,String msg){
        if (isShow){
            android.util.Log.v(tag,msg);
        }
    }

    public static void d (String tag ,String msg){
        if (isShow){
            android.util.Log.d(tag,msg);
        }
    }

    public static void i (String tag ,String msg){
        if (isShow){
            android.util.Log.i(tag,msg);
        }
    }

    public static void w (String tag ,String msg){
        if (isShow){
            android.util.Log.w(tag,msg);
        }
    }

    public static void e (String tag ,String msg){
        if (isShow){
            android.util.Log.e(tag,msg);
        }
    }


}
