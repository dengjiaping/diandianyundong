package com.fox.exercise.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

import android.os.Environment;
import android.util.Log;

import cn.ingenic.indroidsync.SportsApp;

public class SportsForgetPwdUtil {
    private static final String path1 = SportsApp.getContext().getFilesDir()
            .toString()
            + "/sportsNewPwd";

    private static final String path2 = Environment
            .getExternalStorageDirectory()
            + "/android/data/"
            + SportsApp.getContext().getPackageName()
            + ".zuimei"
            + "/.sportsNewPwd";

    /*
     * 存储运动日期和和次数
     */
    public static void saveToatlNumberToFile(String date, int number, String phone_number) {
        Log.e("str", "进入存储---------------");
        try {
            Log.e("str", "方法存储---------------");
            Log.e("str", "---------" + path1);
            Log.e("str", "----------" + path2);
            File file = new File(path1 + phone_number);
            File dir = file.getParentFile();
            if (!dir.exists()) {
                dir.mkdirs();
            }
            if (file.exists()) {
                file.delete();
            }
            if (!file.createNewFile()) {
                return;
            } else {
                FileWriter writer = new FileWriter(file);
                writer.write(date + "#" + number);
                writer.flush();
                writer.close();
            }

            file = new File(path2 + phone_number);
            dir = file.getParentFile();
            if (!dir.exists()) {
                dir.mkdirs();
            }
            if (file.exists()) {
                file.delete();
            }
            if (!file.createNewFile()) {
                return;
            } else {
                FileWriter writer = new FileWriter(file);
                writer.write(date + "#" + number);
                writer.flush();
                writer.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /*
     * 读取保存运动日期和次数
     */
    public static String readTotalNumberFromFile(String phone_number) {
        String dateNumberString = "";
        File f = null;
        FileInputStream fis = null;
        BufferedInputStream bis = null;
        try {
            f = new File(path1 + phone_number);
            fis = new FileInputStream(f);
            bis = new BufferedInputStream(fis);
            byte[] buff = new byte[512];
            bis.read(buff);
            dateNumberString = new String(buff).trim();
            fis.close();
            bis.close();
            if (!"".equals(dateNumberString))
                return dateNumberString;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            f = new File(path2 + phone_number);
            fis = new FileInputStream(f);
            bis = new BufferedInputStream(fis);
            byte[] buff = new byte[512];
            bis.read(buff);
            dateNumberString = new String(buff).trim();
            fis.close();
            bis.close();
            if (!"".equals(dateNumberString))
                return dateNumberString;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return dateNumberString;
    }
}
