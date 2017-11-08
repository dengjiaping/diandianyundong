package com.fox.exercise.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

/**
 * Bitmap转化为Base64
 *
 * @param imgPath
 * @param bitmap
 * @param imgFormat 图片格式
 * @author 赵少雷
 * @return
 */

public class BitmapToBase64 {
    private static final String TAG = "BitmapToBase64";

    public static String imgToBase64(Bitmap bit) {

        if (bit == null) {
            // bitmap not found!!
//			LogUtil.i(TAG, "获取的bitmap为null。。。");
        }
        ByteArrayOutputStream out = null;
        try {
            out = new ByteArrayOutputStream();
            bit.compress(Bitmap.CompressFormat.JPEG, 10, out);

            out.flush();
            out.close();

            byte[] imgBytes = out.toByteArray();
            return Base64.encodeToString(imgBytes, Base64.DEFAULT);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            return null;
        } finally {
            try {
                out.flush();
                out.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    /**
     * Base64转化为Bitmap
     *
     * @param base64Data
     * @param imgName
     * @param imgFormat  图片格式
     */
    public static Bitmap base64ToBitmap(String base64Data) {
        byte[] bytes = Base64.decode(base64Data, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        if (bitmap != null && !"".equals(bitmap)) {
            return bitmap;
        } else {
            return null;
        }

    }

}
