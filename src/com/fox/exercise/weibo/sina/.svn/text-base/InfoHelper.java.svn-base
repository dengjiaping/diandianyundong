package com.fox.exercise.weibo.sina;

import java.io.File;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

public class InfoHelper {
    public final static String SDCARD_MNT = "/mnt/sdcard";
    public final static String SDCARD = "/sdcard";

    public static String getFileName() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss_SS");
        String fileName = format.format(new Timestamp(System
                .currentTimeMillis()));
        return fileName;
    }

    public static String getCamerPath() {
        return Environment.getExternalStorageDirectory() + File.separator
                + "FounderNews" + File.separator;
    }

    public static String getAbsolutePathFromNoStandardUri(Uri mUri) {
        String filePath = null;

        String mUriString = mUri.toString();
        mUriString = Uri.decode(mUriString);

        String pre1 = "file://" + SDCARD + File.separator;
        String pre2 = "file://" + SDCARD_MNT + File.separator;

        if (mUriString.startsWith(pre1)) {
            filePath = Environment.getExternalStorageDirectory().getPath()
                    + File.separator + mUriString.substring(pre1.length());
        } else if (mUriString.startsWith(pre2)) {
            filePath = Environment.getExternalStorageDirectory().getPath()
                    + File.separator + mUriString.substring(pre2.length());
        }
        return filePath;
    }

    public static boolean checkNetWork(Context context) {
        boolean newWorkOK = false;
        ConnectivityManager connectManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectManager.getActiveNetworkInfo() != null) {
            newWorkOK = true;
        }
        return newWorkOK;
    }

    public static Bitmap getScaleBitmap(Context context, String filePath) {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inSampleSize = 4;

        return com.fox.exercise.login.Util.decodeFile(filePath, opts);
    }

    public static void clearAccessInfo(Context mContext) {
        AccessInfoHelper accessDBHelper = new AccessInfoHelper(mContext);
        accessDBHelper.open();
        accessDBHelper.clear();
        accessDBHelper.close();
    }

    public static AccessInfo getAccessInfo(Context mContext) {
        ArrayList<AccessInfo> list = null;
        AccessInfoHelper accessDBHelper = new AccessInfoHelper(mContext);
        accessDBHelper.open();

        try {
            list = accessDBHelper.getAccessInfos();
            Log.d("InfoHelper", "list.size:" + list.size());
            if (list.size() > 0) {
//				Log.d("InfoHelper", list.get(0).getAccessToken());
//				Log.d("InfoHelper", list.get(0).getAccessSecret());
                accessDBHelper.close();
                return list.get(0);
            }
        } finally {
            accessDBHelper.close();
        }
        return (list != null && list.size() != 0) ? list.get(0) : null;
    }
}