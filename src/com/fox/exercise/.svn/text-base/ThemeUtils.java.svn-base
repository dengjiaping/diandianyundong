package com.fox.exercise;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class ThemeUtils {
    private static final String LAUNCHER_PREFERENCES_17VEE = "launcher.preferences.17vee";
    private static final String[] restart_keys = {"themePackageName"};
    private static boolean isThemeChange = false;

    public static boolean isThemeChange() {
        return isThemeChange;
    }

    public static boolean needsRestart(String key) {
        for (int i = 0; i < restart_keys.length; i++) {
            if (restart_keys[i].equals(key))
                return true;
        }
        return false;
    }

    public static String getThemePackageName(Context context,
                                             String default_theme) {
        SharedPreferences sp = context.getSharedPreferences(
                LAUNCHER_PREFERENCES_17VEE, Context.MODE_PRIVATE);
        return sp.getString("themePackageName", default_theme);
    }


    public static void setThemePackageName(Context context,
                                           String packageName) {
        SharedPreferences sp = context.getSharedPreferences(
                LAUNCHER_PREFERENCES_17VEE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("themePackageName", packageName);
        editor.putInt("isThemePackageNameChange", 1);
        editor.commit();
    }


    public static String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface
                    .getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf
                        .getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException ex) {
            Log.e("WifiPreference IpAddress", ex.toString());
        }
        return null;
    }

    public static String getOnlineThemePackageName(Context context,
                                                   String default_theme) {
        SharedPreferences sp = context.getSharedPreferences(
                LAUNCHER_PREFERENCES_17VEE, Context.MODE_PRIVATE);
        return sp.getString("onlinethemePackageName", default_theme);
    }

    public static void setOnlineThemePackageName(Context context,
                                                 String packageName) {
        SharedPreferences sp = context.getSharedPreferences(
                LAUNCHER_PREFERENCES_17VEE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("onlinethemePackageName", packageName);
        editor.commit();
    }

}
