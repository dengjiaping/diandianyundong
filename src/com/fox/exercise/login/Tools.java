package com.fox.exercise.login;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.ingenic.indroidsync.SportsApp;

import com.fox.exercise.R;


import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

/**
 * @author ZhouRongXing
 *         Create at 2012-11-22 上午14:30:40
 */
public class Tools {
    /**
     * check SDCard exist
     *
     * @return
     */
    public static boolean hasSdcard() {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean SaveBitmapAsFile(String filepath, Bitmap bitmap) {
        boolean flag = false;
        String dir = filepath.substring(0, filepath.lastIndexOf("/"));
        Log.e("", filepath);
        Log.e("", dir);
        File mDownloadDir = new File(dir);
        if (!mDownloadDir.exists()) {
            mDownloadDir.mkdirs();
        }
        File newfile = new File(filepath);
        FileOutputStream os = null;
        if (newfile.exists()) {
            newfile.delete();
        }
        try {
            newfile.createNewFile();
            os = new FileOutputStream(newfile);
            flag = bitmap.compress(CompressFormat.JPEG, 100, os);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if (os != null) {
                try {
                    os.flush();
                    os.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        Log.i("flag", "截图成功了" + flag);
        return flag;
    }

    public static boolean SaveBitmapAsFile(String filepath, Bitmap bitmap, int quality) {
        boolean flag = false;
        String dir = filepath.substring(0, filepath.lastIndexOf("/"));
        Log.e("", filepath);
        Log.e("", dir);
        File mDownloadDir = new File(dir);
        if (!mDownloadDir.exists()) {
            mDownloadDir.mkdirs();
        }
        File newfile = new File(filepath);
        FileOutputStream os = null;
        if (newfile.exists()) {
            newfile.delete();
        }
        try {
            newfile.createNewFile();
            os = new FileOutputStream(newfile);
            flag = bitmap.compress(CompressFormat.JPEG, quality, os);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if (os != null) {
                try {
                    os.flush();
                    os.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        Log.i("flag", "截图成功了" + flag);
        return flag;
    }

    /*bad method*/
    public static boolean JudgeEmailFormat(String emailaddress) {

        if (!emailaddress.matches("^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$")) {
            return false;

        } else {
            return true;
        }
    }

    //	\\w+@\\w+\\.(com\\.cn)|\\w+@\\w+\\.(com|cn)
//	\\w+@\\w+\\.(com\\.cn)|\\w+@\\w+\\.(com|cn|net|org)
//	"\\w+@\\w+\\.(com\\.cn)|\\w+@\\w+\\.*+\\w*+\\.(com|cn|net|org)"
    public static boolean Ismail(String emailaddress) {
        String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(emailaddress);

        return m.matches();
    }

    public static int getVersionNum(Context context) {
        int versionCode = 0;
        try {
            versionCode = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;

        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;

    }

    public static String getVersionName(Context context) {
        String versionName = "";
        try {
            versionName = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
            if (versionName == null || versionName.length() <= 0) {
                return "";
            }
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionName;

    }

    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    //
    public static Bitmap getBitmapFromURL(String url) {

        try {
            URL urlo = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) urlo.openConnection();
            conn.setConnectTimeout(5 * 1000);
            conn.setRequestMethod("GET");
            InputStream inStream = conn.getInputStream();

            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = inStream.read(buffer)) != -1) {
                outStream.write(buffer, 0, len);
            }
            outStream.close();
            inStream.close();
            byte[] data = outStream.toByteArray();
            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
            return bitmap;
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }

    /**
     * delete folder
     *
     * @param filePathAndName String c:/fqf
     * @param fileContent     String
     * @return boolean
     */
    public static void delFolder(String folderPath) {
        try {
            Log.e("==================", "" + folderPath);
            delAllFile(folderPath);
            String filePath = folderPath;
            filePath = filePath.toString();
//                    java.io.File myFilePath = new java.io.File(filePath);
//                    myFilePath.delete(); 

        } catch (Exception e) {
            System.out.println("failed to delete folder");
            e.printStackTrace();

        }
    }

    /**
     * delete all files
     *
     * @param path String  c:/fqf
     */
    public static void delAllFile(String path) {
        File file = new File(path);
        if (!file.exists()) {
            return;
        }
        if (!file.isDirectory()) {
            return;
        }
        String[] tempList = file.list();
        File temp = null;
        for (int i = 0; i < tempList.length; i++) {
            if (path.endsWith(File.separator)) {
                temp = new File(path + tempList[i]);
            } else {
                temp = new File(path + File.separator + tempList[i]);
            }
            if (temp.isFile()) {
                Log.e("==================", "" + temp);
                temp.delete();
            }
            if (temp.isDirectory()) {
                delAllFile(path + "/" + tempList[i]);//delete files
                delFolder(path + "/" + tempList[i]);//delete folder
            }
        }
    }

    public static Dialog getprogressDialog(Context con, boolean iscancelabe, String message) {
        Dialog refreshProgressDialog = new Dialog(con, R.style.sports_dialog);
        LayoutInflater mInflater = LayoutInflater.from(con);
        View v = mInflater.inflate(R.layout.sports_progressdialog, null);
        TextView msg = (TextView) v.findViewById(R.id.message);
        msg.setText(message);
        v.setMinimumWidth((int) (SportsApp.ScreenWidth * 0.8));
        refreshProgressDialog.setContentView(v);
        refreshProgressDialog.setCancelable(iscancelabe);
        return refreshProgressDialog;
    }
}
