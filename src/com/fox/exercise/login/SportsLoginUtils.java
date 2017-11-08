package com.fox.exercise.login;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;


import android.util.Log;

public class SportsLoginUtils {
    private static String mName = "";

    public static final String TAG = "SportsLoginUtils";
    private static final int ACCOUNT_MAX = 3;

    protected void saveNameToFile(String name, String path1, String path2) {
        if (!"".equals(mName) && mName.length() > 8) {
            Log.e(TAG, "mName:" + mName);
            return;
        }
        try {
            File file = new File(path1);
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
                writer.write(name);
                writer.flush();
                writer.close();
            }

            file = new File(path2);
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
                writer.write(name);
                writer.flush();
                writer.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    protected void saveIDToFile(int id, String path1, String path2) {
        try {
            File file = new File(path1);
            File dir = file.getParentFile();
            if (!dir.exists()) {
                dir.mkdirs();
            }
            if (file.exists()) {

                FileWriter writer = new FileWriter(file, true);
                writer.write("#" + id);
                writer.flush();
                writer.close();

            }
            file = new File(path2);
            dir = file.getParentFile();
            if (!dir.exists()) {
                dir.mkdirs();
            }
            if (file.exists()) {
                FileWriter writer = new FileWriter(file, true);
                writer.write("#" + id);
                writer.flush();
                writer.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void saveNormalToFile(String name, String path1, String path2) {
        try {
            File file = new File(path1);
            File dir = file.getParentFile();
            if (!dir.exists()) {
                dir.mkdirs();
            }
            if (!file.exists()) {
                file.createNewFile();
            }
            if (file.exists()) {

                FileWriter writer = new FileWriter(file, true);
                writer.write("#" + name);
                writer.flush();
                writer.close();

            }
            file = new File(path2);
            dir = file.getParentFile();
            if (!dir.exists()) {
                dir.mkdirs();
            }
            if (!file.exists()) {
                file.createNewFile();
            }
            if (file.exists()) {
                FileWriter writer = new FileWriter(file, true);
                writer.write("#" + name);
                writer.flush();
                writer.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public boolean existId(String path1, String path2) {
        // Log.e(TAG, "judge existId");

        if (getNumberCount(mName) > 8)
            return true;
        else
            return false;
    }

    /*
     * 存储当天日期(奖励金币，仅限当日登陆一次有效)
     */
    public void saveDateToFile(String date, String path1, String path2) {
        try {
            File file = new File(path1);
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
                writer.write(date);
                writer.flush();
                writer.close();
            }

            file = new File(path2);
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
                writer.write(date);
                writer.flush();
                writer.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    protected String readNameFronFile(File file) {
        String name = "";
        try {
            FileInputStream fis = new FileInputStream(file);
            BufferedInputStream bis = new BufferedInputStream(fis);
            byte[] buff = new byte[128];
            bis.read(buff);
            mName = new String(buff);
            name = mName.trim();
            // Log.e("###", "" + name);
            if (name.contains("#"))
                name = name.split("#")[0];
            // Log.e("###", "" + name);
            fis.close();
            bis.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return name;
    }

    /*
     * 读取保存的日期
     */
    public String readDateFromFile(String path1, String path2) {
        String date = "";
        File f = null;
        FileInputStream fis = null;
        BufferedInputStream bis = null;
        try {
            f = new File(path1);
            fis = new FileInputStream(f);
            bis = new BufferedInputStream(fis);
            byte[] buff = new byte[512];
            bis.read(buff);
            date = new String(buff).trim();
            fis.close();
            bis.close();
            if (!"".equals(date))
                return date;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            f = new File(path2);
            fis = new FileInputStream(f);
            bis = new BufferedInputStream(fis);
            byte[] buff = new byte[512];
            bis.read(buff);
            date = new String(buff).trim();
            fis.close();
            bis.close();
            if (!"".equals(date))
                return date;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return date;
    }

    protected String getRandomName() {
        String s = "";
        while (s.length() < 8) {

            int r = (int) (Math.random() * 10000 % 120);
            if (r >= 0x30 && r <= 0x39) {
                System.out.println("" + r);
                s += (char) r;
            } else if (r >= 0x61 && r <= 0x7A) {
                System.out.println("" + r);
                s += (char) r;
            }
        }
        return s;
    }

    protected int getNumberCount(String s) {
        int cnt = 0;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c >= '0' && c <= '9') {
                cnt++;
            }
        }
        // Log.e(TAG, "number=" + cnt);
        return cnt;
    }

    public boolean canNormalAccount(String path1, String path2) {
        File f = null;
        FileInputStream fis = null;
        BufferedInputStream bis = null;

        try {
            f = new File(path1);
            fis = new FileInputStream(f);
            bis = new BufferedInputStream(fis);
            byte[] buff = new byte[512];
            bis.read(buff);
            String s = new String(buff);
            Log.e(TAG, "s:" + s);
            fis.close();
            bis.close();
            if (getNormalNumberCount(s) >= ACCOUNT_MAX)
                return false;
            else
                return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            f = new File(path2);
            fis = new FileInputStream(f);
            bis = new BufferedInputStream(fis);
            byte[] buff = new byte[512];
            bis.read(buff);
            String s = new String(buff);
            Log.e(TAG, "s:" + s);
            fis.close();
            bis.close();
            if (getNormalNumberCount(s) >= ACCOUNT_MAX)
                return false;
            else
                return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    protected int getNormalNumberCount(String s) {
        int cnt = 0;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '#') {
                cnt++;
            }
        }
        Log.e(TAG, "number=" + cnt);
        return cnt;
    }

}
