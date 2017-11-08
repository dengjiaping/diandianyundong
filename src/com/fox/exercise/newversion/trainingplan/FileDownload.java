package com.fox.exercise.newversion.trainingplan;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import cn.ingenic.indroidsync.SportsApp;

import android.os.Environment;
import android.util.Log;

public class FileDownload {
    private String TAG = "develop_debug";
    private InputStream is;
    private FileOutputStream fos;
    private File file;
    /**
     * 连接url
     */
    private String urlstr;
    /**
     * sd卡目录路径
     */
    private String sdcard;
    /**
     * http连接管理类
     */
    private HttpURLConnection urlcon;
    private boolean cancelDownload;

    public FileDownload(String url) {
        this.urlstr = url;
        //获取设备sd卡目录
        this.sdcard = Environment.getExternalStorageDirectory().toString() + "/android/data/"
                + SportsApp.getContext().getPackageName() + "/cache/";
        urlcon = getConnection();

        cancelDownload = false;
    }

    /*
     * 读取网络文本
     */
    public String downloadAsString() {
        StringBuilder sb = new StringBuilder();
        String temp = null;
        try {
            InputStream is = urlcon.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            while ((temp = br.readLine()) != null) {
                sb.append(temp);
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    /*
     * 获取http连接处理类HttpURLConnection
     */
    private HttpURLConnection getConnection() {
        URL url;
        HttpURLConnection urlcon = null;
        try {
            url = new URL(urlstr);
            urlcon = (HttpURLConnection) url.openConnection();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "Exception : " + e.toString());
        }
        return urlcon;
    }

    /*
     * 获取连接文件长度。
     */
    public int getLength() {
        if (urlcon == null) {
            return 0;
        }

        return urlcon.getContentLength();
    }

    /*
     * 写文件到sd卡 demo
     * 前提需要设置模拟器sd卡容量，否则会引发EACCES异常
     * 先创建文件夹，在创建文件
     */
    public int down2sd(String dir, String filename, downhandler handler) {
        int status = 1;

        StringBuilder sb = new StringBuilder(sdcard).append(dir);
        file = new File(sb.toString());

        if (!file.exists()) {
            file.mkdirs();
            //创建文件夹
        }
        //获取文件全名
        sb.append(filename.trim());
        file = new File(sb.toString());

        try {
            if (urlcon == null) {
                Log.e(TAG, "urlcon is null");
                return -1;
            }

            is = urlcon.getInputStream();
            if (is == null) {
                Log.e(TAG, "is is null");
                return -1;
            }

            //创建文件
            file.createNewFile();
            fos = new FileOutputStream(file);

            byte[] buf = new byte[10240];
            int byteCount = 0;

            while ((byteCount = is.read(buf)) != -1) {
                Log.e(TAG, "byteCount : " + byteCount);
                fos.write(buf, 0, byteCount);
                //同步更新数据
                handler.setSize(byteCount);

                if (cancelDownload) {
                    if ((file != null) && file.exists()) {
                        file.delete();
                    }

                    break;
                }
            }

            if (is != null) {
                is.close();
                is = null;
            }

            if (fos != null) {
                fos.flush();
                fos.close();
                fos = null;
            }
        } catch (Exception e) {
            status = -1;
            Log.e(TAG, "Exception : " + e.toString());
        }

        return status;
    }

    /*
     * 内部回调接口类
     */
    public abstract class downhandler {
        public abstract void setSize(int size);
    }

    public void cancelDownload() {
        cancelDownload = true;
    }
}