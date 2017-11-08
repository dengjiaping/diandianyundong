package com.fox.exercise.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import com.fox.exercise.api.ApiConstant;

import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class MediaFileDownloader {
    private InputStream inputStream;
    FileOutputStream outputStream;
    private URLConnection connection;
    private String urlString;
    private String saveFileDirEx;
    private String saveFileDirIn;
    private String saveFileFullName;
    private String filePath;
    private Handler mHandler;
    private Context mContext;
    private boolean mCancel = false;
    private int mIndex;

    public static final int MSG_START_DOWNLOAD = 100;
    public static final int MSG_DOWNLOAD_CANCEL = 101;
    public static final int MSG_DOWNLOAD_FAILED = 102;
    public static final int MSG_DOWNLOAD_FINISH = 103;

    public static void clearMediaCache(final Context context) {
        Thread thread = new Thread() {
            public void run() {
                for (int i = 1; i <= 3; i++) {
                    File dir = new File(context.getCacheDir().getPath() + "/"
                            + getSubDirFromType(i));
                    if (!dir.exists()) {
                        continue;
                    }
                    File[] files = dir.listFiles();
                    if (files == null)
                        continue;
                    for (File f : files)
                        f.delete();
                }
            }
        };
        thread.start();
    }

    public void cancel() {
        mCancel = true;
    }

    public MediaFileDownloader(Context context, Handler handler, String url,
                               int type, int index) {
        mContext = context;
        mHandler = handler;
        urlString = ApiConstant.URL + url;
        mIndex = index;

        saveFileDirEx = Environment.getExternalStorageDirectory().toString()
                + "/android/data/" + mContext.getPackageName()
                + "/cache/.download/" + getSubDirFromType(type);
        saveFileDirIn = context.getCacheDir().getPath() + "/"
                + getSubDirFromType(type);
        saveFileFullName = urlToFileName(url);

        makeInerDir();
        Log.i("", "###  urlString:" + urlString);
        Log.i("", "###  saveFileDirEx:" + saveFileDirEx);
        Log.i("", "###  saveFileDirIn:" + saveFileDirIn);
        Log.i("", "###  saveFileFullName:" + saveFileFullName);
    }

    private static String getSubDirFromType(int type) {
        switch (type) {
            case 1:
                return "image/";
            case 2:
                return "audio/";
            case 3:
                return "video/";
            default:
                return "";
        }
    }

    private String urlToFileName(String url) {
        String urlName = url;
        if (urlName != null && urlName.length() != 0) {
            urlName = urlName.substring(urlName.lastIndexOf("/") + 1);
        }
        return urlName;
    }

    private boolean fileExit() {
        filePath = saveFileDirEx + saveFileFullName;
        File file = new File(filePath);
        if (file.exists()) {
            return true;
        }

        filePath = saveFileDirIn + saveFileFullName;
        file = new File(filePath);
        if (file.exists()) {
            return true;
        }
        return false;
    }

    private void sendMessage(int what, Object obj) {
        Message msg = new Message();
        msg.what = what;
        msg.obj = obj;
        msg.arg1 = mIndex;
        mHandler.sendMessage(msg);
    }

    private void makeInerDir() {
        File dir = new File(saveFileDirIn);
        if (!dir.exists()) {
            dir.mkdirs();
            try {
                String command = "chmod 777 " + dir.getAbsolutePath();
                Runtime runtime = Runtime.getRuntime();
                runtime.exec(command);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void DownFile() {
        if (fileExit()) {
            // �ļ��Ѿ����� ֱ�ӷ���
            sendMessage(MSG_DOWNLOAD_FINISH, filePath);
            return;
        }

        // ��ʼ���� UIתȦ
        sendMessage(MSG_START_DOWNLOAD, null);

        try {
            URL url = new URL(urlString);
            connection = url.openConnection();
            inputStream = connection.getInputStream();
        } catch (Exception e) {
            sendMessage(MSG_DOWNLOAD_FAILED, null);
            return;
        }

        File dir;
        if (Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED)) {
            filePath = saveFileDirEx + saveFileFullName;
            dir = new File(saveFileDirEx);
            if (!dir.exists()) {
                dir.mkdirs();
            }
        } else {
            filePath = saveFileDirIn + saveFileFullName;
            makeInerDir();
        }

        try {
            connection.setReadTimeout(10000);
            int FileLength = connection.getContentLength();
            if (FileLength == -1) {
                sendMessage(MSG_DOWNLOAD_FAILED, null);
            }

            String filenamepath = filePath.substring(0,
                    filePath.lastIndexOf(File.separator) + 1);
            String filenamewithoutpath = filePath.substring(filePath
                    .lastIndexOf(File.separator) + 1);

            File folder = new File(filenamepath);
            if (!folder.exists()) {
                folder.mkdir();
            }

            File saveFilePath = new File(folder, filenamewithoutpath);

            outputStream = new FileOutputStream(saveFilePath);

            // byte[] buffer = new byte[FileLength];
            byte buffer[] = new byte[512];

            do {
                if (mCancel) {
                    break;
                }
                int numread = inputStream.read(buffer);
                if (numread == -1) {
                    break;
                }
                outputStream.write(buffer, 0, numread);
            } while (true);
            if (!mCancel) {
                outputStream.flush();
                if (filePath.startsWith(mContext.getCacheDir().getPath())) {
                    File dfile = new File(filePath);
                    try {
                        String command = "chmod 777 " + dfile.getAbsolutePath();
                        Runtime runtime = Runtime.getRuntime();
                        runtime.exec(command);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        //wait for runtime.exec()
                        Thread.sleep(300);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
                sendMessage(MSG_DOWNLOAD_FINISH, filePath);
            } else {
                sendMessage(MSG_DOWNLOAD_CANCEL, null);
            }
        } catch (FileNotFoundException e) {
            sendMessage(MSG_DOWNLOAD_FAILED, null);
            e.printStackTrace();
        } catch (IOException e) {
            sendMessage(MSG_DOWNLOAD_FAILED, null);
        } finally {
            try {
                outputStream.close();
                inputStream.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
