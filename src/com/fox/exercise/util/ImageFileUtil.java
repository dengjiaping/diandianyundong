package com.fox.exercise.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.Toast;

public class ImageFileUtil {
    private static final String TAG = "ImageFileUtil";

    private String SDPATH;
    private Activity context;

    private Handler handler;

    public final int DLFINISH = 1001;
    public final int DLFAIL = 1002;

    /**
     * 构造函数传入了文件路径
     */
    public ImageFileUtil(Activity context, String dirPath, Handler handler) {
        this.context = context;
        this.handler = handler;

        String sdState = Environment.getExternalStorageState();
        if (!sdState.equals(Environment.MEDIA_MOUNTED)) {
            Log.i(TAG, "sd card unmount");
            Toast.makeText(context, "未检测到可用的SD卡!", Toast.LENGTH_SHORT).show();
            if (dirPath != null) {
                SDPATH = Environment.getDataDirectory() + File.separator
                        + dirPath + File.separator;
            } else {
                SDPATH = Environment.getDataDirectory() + File.separator;
            }
        } else {
            if (dirPath != null) {
                SDPATH = Environment.getExternalStorageDirectory()
                        + File.separator + dirPath + File.separator;
            } else {
                SDPATH = Environment.getExternalStorageDirectory()
                        + File.separator;
            }
        }
    }

    /**
     * 在SD卡上创建文件夹
     *
     * @throws IOException
     */
    public File creatSDDir(String path) throws IOException {
        File sdDir = new File(path);
        if (path != null && !sdDir.exists()) {
            sdDir.mkdirs();
        }
        return sdDir;
    }

    /**
     * 在SD卡上创建文件
     *
     * @throws IOException
     */
    public File creatSDFile(String fileName) throws IOException {
        File files = creatSDDir(SDPATH);
        if (files == null) {
            Log.i("creatSDFile", "文件夹创建失败！");
        }
        File file = new File(SDPATH + fileName);
        Log.v(TAG, "SDPATH + fileName is " + SDPATH + fileName);
        if (fileName == null && !file.exists()) {
            file.createNewFile();
        }
        return file;
    }

    /**
     * 判断SD卡上的文件或者文件夹是否存在
     */
    public boolean isFileExist(String fileName) {
        File file = null;
        if (fileName != null) {
            file = new File(SDPATH + fileName);
        }
        return file.exists();
    }

    /**
     * 删除SD卡上的文件
     *
     * @param fileName
     */
    public boolean deleteSDFile(String fileName) {
        File file = new File(SDPATH + fileName);
        if (file == null || !file.exists() || file.isDirectory()) {
            return false;
        }
        return file.delete();
    }

    /**
     * 下载图片到SD卡中并返回bitmap
     *
     * @throws IOException
     */
    public Bitmap writeToSDFromPhoto(String urlStr, String fileName)
            throws IOException {
        Log.i("writeToSDFromInput", "传入url和filename：" + urlStr + "," + fileName);
        Bitmap bitmap = null;
        OutputStream out = null;
        try {
            byte[] data = getBytes(urlStr);
            File file = creatSDFile(fileName);
            out = new FileOutputStream(file);
            out.write(data);
            out.flush();

            // bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);

            DisplayMetrics dm = new DisplayMetrics();
            // 获取屏幕信息
            context.getWindowManager().getDefaultDisplay().getMetrics(dm);
            int screenWidth = dm.widthPixels;
            int screenHeigh = dm.heightPixels;
            Log.i("屏幕宽高", "宽：" + screenWidth + "，高：" + screenHeigh);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 1;
            Bitmap resizeBmp = BitmapFactory.decodeByteArray(data, 0,
                    data.length, options);

            Matrix matrix = new Matrix();
            matrix.postScale(1.5f, 1.5f); // 长和宽放大缩小的比例
            bitmap = Bitmap.createBitmap(resizeBmp, 0, 0, resizeBmp.getWidth(),
                    resizeBmp.getHeight(), matrix, true);
        } catch (Exception e) {
            Log.i(TAG, e.toString());
            Log.i("", "获取图片失败！");
        } finally {
            if (out != null) {
                out.close();
            }
        }
        return bitmap;
    }

    /**
     * 下载图片到SD卡中并返回path
     *
     * @throws IOException
     */
    public String writeToSDFromNet(String urlStr, String fileName)
            throws IOException {
        Log.i("writeToSDFromInput", "传入url和filename：" + urlStr + "," + fileName);
        OutputStream out = null;
        File rFile = new File(SDPATH + fileName);
        if (rFile.exists()) {
            return SDPATH + fileName;
        }
        try {
            byte[] data = getBytes(urlStr);
            File file = creatSDFile(fileName);
            out = new FileOutputStream(file);
            out.write(data);
            out.flush();

            if (file.exists()) {
                Message msg = new Message();
                msg.what = DLFINISH;
                msg.obj = SDPATH + fileName;
                handler.sendMessage(msg);
            } else {
                Message msg = new Message();
                msg.what = DLFAIL;
                handler.sendMessage(msg);
            }

        } catch (Exception e) {
            Log.i(TAG, e.toString());
            Log.i("", "获取图片失败！");
        } finally {
            if (out != null) {
                out.close();
            }
        }
        return SDPATH + fileName;
    }

    /**
     * 下载音频文件到SD卡中并返回MediaPlayer对象
     *
     * @throws IOException
     */
    public String writeToSDFromVoice(String urlStr, String fileName)
            throws IOException {
        Log.i("writeToSDFromInput", "传入url和filename：" + urlStr + "," + fileName);
        File rFile = new File(SDPATH + fileName);
        if (rFile.exists()) {
            return SDPATH + fileName;
        }
        try {
            byte[] data = getBytes(urlStr);
            File file = creatSDFile(fileName);
            OutputStream out = new FileOutputStream(file);
            out.write(data);
            out.close();

            if (file.exists()) {
                Message msg = new Message();
                msg.what = DLFINISH;
                handler.sendMessage(msg);
            } else {
                Message msg = new Message();
                msg.what = DLFAIL;
                handler.sendMessage(msg);
            }

        } catch (Exception e) {
            Log.i(TAG, e.toString());
            Log.i("", "保存音频失败！");
        }
        return SDPATH + fileName;
    }

    /**
     * 下载视频文件到SD卡中并返回MediaPlayer对象
     *
     * @throws IOException
     */
    public String writeToSDFromVideo(String urlStr, String fileName)
            throws IOException {
        Log.i("writeToSDFromInput", "传入url和filename：" + urlStr + "," + fileName);
        try {
            byte[] data = getBytes(urlStr);
            File file = creatSDFile(fileName);
            OutputStream out = new FileOutputStream(file);
            out.write(data);
            out.close();

            if (file.exists()) {
                Message msg = new Message();
                msg.what = DLFINISH;
                handler.sendMessage(msg);
            } else {
                Message msg = new Message();
                msg.what = DLFAIL;
                handler.sendMessage(msg);
            }

        } catch (Exception e) {
            Log.i(TAG, e.toString());
            Log.i("", "保存视频失败！");
        }
        return SDPATH + fileName;
    }

    public static byte[] getBytes(String path) throws Exception {
        URL url = new URL(path);
        HttpURLConnection httpURLconnection = (HttpURLConnection) url
                .openConnection();
        httpURLconnection.setRequestMethod("GET");
        httpURLconnection.setReadTimeout(5 * 1000);
        InputStream in = null;
        if (httpURLconnection.getResponseCode() == 200) {
            in = httpURLconnection.getInputStream();
            byte[] result = readStream(in);
            in.close();
            return result;
        }
        return null;
    }

    /**
     * 把输入流转化为字节数组
     *
     * @param fileName
     * @return
     */
    public static byte[] readStream(InputStream in) throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = -1;
        while ((len = in.read(buffer)) != -1) {
            outputStream.write(buffer, 0, len);
        }
        outputStream.close();
        in.close();
        return outputStream.toByteArray();
    }

    /**
     * 读取SD卡中图片
     *
     * @param fileName
     * @return
     * @throws IOException
     */
    public Bitmap getImg(String imgName) throws IOException {
        Log.v(TAG, "imgName is " + SDPATH + imgName);
        File file = new File(SDPATH + imgName);
        Bitmap bitmap = null;
        if (file.exists()) {
            bitmap = BitmapFactory.decodeFile(SDPATH + imgName);
            return bitmap;
        }
        Log.i("getImg", "本地未找到图片！");
        return null;
    }

    /**
     * 读取SD卡中文本文件
     *
     * @param fileName
     * @return
     */
    public String readSDFile(String path, String fileName) {
        StringBuffer sb = new StringBuffer();
        File file;
        try {
            file = creatSDFile(fileName);
            FileInputStream fis = new FileInputStream(file);
            int c;
            while ((c = fis.read()) != -1) {
                sb.append((char) c);
            }
            fis.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
}
