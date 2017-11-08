/**
 *
 */
package com.fox.exercise;

import com.amap.api.maps.AMapException;

import android.content.Context;
import android.widget.Toast;

public class ToastUtil {

    public static void show(Context context, String info) {
        Toast.makeText(context, info, Toast.LENGTH_LONG).show();
    }

    public static void show(Context context, int info) {
        Toast.makeText(context, info, Toast.LENGTH_LONG).show();
    }

    public static void showerror(Context context, int rCode) {
        try {
            switch (rCode) {
                case 21:
                    throw new AMapException(AMapException.ERROR_IO);
                case 22:
                    throw new AMapException(AMapException.ERROR_SOCKET);
                case 23:
                    throw new AMapException(AMapException.ERROR_SOCKE_TIME_OUT);
                case 24:
                    throw new AMapException(AMapException.ERROR_INVALID_PARAMETER);
                case 25:
                    throw new AMapException(AMapException.ERROR_NULL_PARAMETER);
                case 26:
                    throw new AMapException(AMapException.ERROR_URL);
                case 27:
                    throw new AMapException(AMapException.ERROR_UNKNOW_HOST);
                case 28:
                    throw new AMapException(AMapException.ERROR_UNKNOW_SERVICE);
                case 29:
                    throw new AMapException(AMapException.ERROR_PROTOCOL);
                case 30:
                    throw new AMapException(AMapException.ERROR_CONNECTION);
                case 31:
                    throw new AMapException(AMapException.ERROR_UNKNOWN);
                case 32:
                    throw new AMapException(AMapException.ERROR_FAILURE_AUTH);
                case 11:
                    Toast.makeText(context, "两次单次上传的间隔低于 5 秒", Toast.LENGTH_LONG)
                            .show();
                    break;
                case 12:
                    Toast.makeText(context, "Uploadinfo 对象为空", Toast.LENGTH_LONG)
                            .show();
                    break;
                case 14:
                    Toast.makeText(context, "Point 为空，或与前次上传的相同", Toast.LENGTH_LONG)
                            .show();
                    break;
            }
        } catch (Exception e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}
