package com.fox.exercise.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.http.util.EncodingUtils;

import android.graphics.Bitmap;
import android.os.Environment;

/**
 * @author 文件的保存与读取
 */
public class FileSaveRead {
    public static final String SDCardPath = Environment
            .getExternalStorageDirectory() + "/fox";

    /**
     * 读取文件内容
     *
     * @param filename 文件名称
     * @return
     * @throws Exception 文件内容
     */
    public static String readFilePath(String filename) {
        int index = filename.lastIndexOf("/");
        String path = filename.substring(0, index);
        String name = filename.substring(index + 1, filename.length());
        File file = new File(Environment.getExternalStorageDirectory() + path,
                name);
        if (!file.exists()) {
            return "";
        }
        return file.getAbsolutePath();
    }

    /**
     * 创建文件夹
     *
     * @param dirName
     * @return
     * @throws IOException
     */
    public static void createDirs() {
        File dir = new File(SDCardPath);
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            if (!dir.exists()) {
                dir.mkdir();
            }
        }
    }

    /**
     * 图片保存到本地
     *
     * @param Bitmap bitmap
     * @param name
     * @param path
     * @return 文件路径
     */
    public static String saveToFile(String name, String path, Bitmap bitmap) {
        BufferedOutputStream os = null;
        File file = new File(Environment.getExternalStorageDirectory() + path,
                name);
        File dir = new File(file.getParent());
        try {
            if (!dir.exists()) {
                dir.mkdir();
            }
            if (file.exists()) {
                file.delete();
            }
            file.createNewFile();

            os = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if (os != null) {
                try {
                    bitmap.recycle();
                    bitmap = null;
                    os.close();
                } catch (IOException e) {

                }
            }
        }
        return file.getAbsolutePath();
    }

    /**
     * 删除文件夹
     *
     * @param filePath
     */
    public static void deletFolder(String filePath) {
        File file = new File(Environment.getExternalStorageDirectory()
                + filePath);

        if (file.isDirectory()) {
            File[] childFiles = file.listFiles();
            for (int i = 0; i < childFiles.length; i++) {
                childFiles[i].delete();
            }
        }
    }

    /**
     * 删除文件
     *
     * @param filePath
     */
    public static void deletFile(String filePath) {
        File file = new File(Environment.getExternalStorageDirectory()
                + filePath);
        if (file.exists()) {
            file.delete();
        }
    }

    /**
     * 下载到本地文件
     *
     * @param fileName
     * @param data
     */
    public static String writeFileToDisk(String fileName, byte[] content,
                                         String filePath) {
        FileOutputStream outputStream = null;
        try {
            File file = new File(Environment.getExternalStorageDirectory()
                    + filePath, fileName);
            File dir = new File(file.getParent());
            if (!dir.exists()) {
                dir.mkdir();
            }
            outputStream = new FileOutputStream(file);
            outputStream.write(content);
            return file.getAbsolutePath();
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (Exception e2) {
                    // TODO: handle exception
                    e2.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * 判断SDCard是否可用
     *
     * @return
     */
    public static boolean isSDCardEnable() {
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);

    }

    /**
     * 获取系统存储路径
     *
     * @return
     */
    public static String getRootDirectoryPath() {
        return Environment.getRootDirectory().getAbsolutePath();
    }

    /**
     * 读取本地训练计划json文件内容
     *
     * @return
     */
    public static String readJsonFile(String filePath) {
        String jsonContent = "";
        try {
            File file=new File(filePath);
            if(file.exists()){
                FileInputStream fin = new FileInputStream(filePath);
                // 用这个就不行了，必须用FileInputStream

                int length = fin.available();

                byte[] buffer = new byte[length];

                fin.read(buffer);

                jsonContent = EncodingUtils.getString(buffer, "GB2312");

                fin.close();
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }

        return jsonContent;
    }

}