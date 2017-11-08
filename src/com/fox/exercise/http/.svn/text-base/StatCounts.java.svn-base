package com.fox.exercise.http;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import cn.ingenic.indroidsync.SportsApp;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.telephony.TelephonyManager;
import android.text.format.DateFormat;
import android.util.Log;

public class StatCounts {
    static final String TAG = "17foxSport";

    public static int mreportintall;
    public static int mreportstartup;

    public static String MD5KEY = "K1G0aeC4weNaScY5xdQ8711";

    //add for index if this is the frist run
    public static final String PREF_NAME_ISFIRSTRUN = "pref_isfirstrun";
    public static final String PREF_B_ISFIRSTRUN = "pref_Boolean_isfirstrun";
    public static final String PREF_CURRENT_VERSON = "pref_current_verson";
    public static final String PREF_S_CURVERSON = "pref_String_curVerson";
    public static final String PREF_B_VERSON_UPDATE = "pref_Boolean_verson_update";


    public static final String PREF_NAME_UPDATETIME = "pref_updatetime";
    public static final String PREF_L_UPDATETIME = "pref_int_updatetime";

    public static final String INSTALLCOUNT = "http://api-st.spacestats.com/installcount.php";
    public static final String STARTCOUNT = "http://api-st.spacestats.com/startcount.php";
    public static final String STARTTIMECOUNT = "http://shujutongji.mobifox.cn/userstatnew/starttimekeycount.php";
    public static final String COMMENTS = "http://shujutongji.mobifox.cn/userstatnew/comments.php";
    public static final String SOCIALSHARECOUNT = "http://api-st.spacestats.com/socialsharecount.php";
    public static final String FUNCTIONIDCOUNT = "http://api-st.spacestats.com/functionidcount.php";
    public static final String REPORTBUG = "http://api-st.spacestats.com/reportbug.php";
    //    		"http://124.207.66.136/userstatnew/comments.php";
    public static final String PREF_NAME_REPORT = "pref_report";
    public static final String PREF_INT_REPORTINSTALL = "pref_reportinstall";
    public static final String PREF_INT_REPORTSTARTUP = "pref_reportstartup";

    public static final int REPORTINSTALL_STATE_NONE = -1;
    public static final int REPORTINSTALL_STATE_RECORDED = 0;
    public static final int REPORTINSTALL_STATE_REPORTED = 1;

    public static void ReportStatisticInfo(Context context, String appId) {
        SharedPreferences misFirstRunSharedPreferences = context.getSharedPreferences(PREF_NAME_ISFIRSTRUN, Context.MODE_WORLD_WRITEABLE);
        final boolean mIsfirstrun = misFirstRunSharedPreferences.getBoolean(PREF_B_ISFIRSTRUN, true);

        mreportintall = misFirstRunSharedPreferences.getInt(PREF_INT_REPORTINSTALL, REPORTINSTALL_STATE_NONE);
        mreportstartup = misFirstRunSharedPreferences.getInt(PREF_INT_REPORTSTARTUP, 0);

        SharedPreferences misReportSharedPreferences = context.getSharedPreferences(PREF_NAME_REPORT, Context.MODE_WORLD_WRITEABLE);
        Editor editor = misReportSharedPreferences.edit();

        if (mIsfirstrun) {
            mreportintall = REPORTINSTALL_STATE_RECORDED; // recored, but not reported
            editor.putInt(PREF_INT_REPORTINSTALL, mreportintall);
        } else {
            mreportstartup += 1;
            editor.putInt(PREF_INT_REPORTSTARTUP, mreportstartup);
        }
        //String ipaddr = ThemeUtils.getLocalIpAddress();
        //if (ipaddr == null) {
        //     return;
        // }

        final String phoneModel = android.os.Build.MODEL;
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        final String imei = telephonyManager.getDeviceId();
        final String gameid = appId;
        Log.e(TAG, "GAMEID:" + gameid);
        final String os = "Android";
        final String osversion = android.os.Build.VERSION.RELEASE;
        final String vesion = getAppVersionName(context.getApplicationContext());
        final String mobilenumber = telephonyManager.getLine1Number();
        final Context context1 = context;
        final String tmsi = telephonyManager.getSubscriberId();
        final String vendor = android.os.Build.MANUFACTURER;

        SharedPreferences sp = context.getSharedPreferences(PREF_NAME_REPORT, Context.MODE_WORLD_WRITEABLE);
        String uuidTemp = sp.getString("uuid", null);
        if (uuidTemp == null) {
            uuidTemp = UUID.randomUUID().toString();
            SharedPreferences.Editor se = sp.edit();
            se.putString("uuid", uuidTemp);
            se.commit();
        }
        final String uuid = uuidTemp;
        final String key = MD5(uuid + gameid + MD5KEY);

        Thread httpsend = new Thread() {

            @Override
            public void run() {
                HttpClient hc = new HttpClient();
                ArrayList<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
                params.add(new BasicNameValuePair("uuid", uuid));
                params.add(new BasicNameValuePair("imei", imei));
                params.add(new BasicNameValuePair("tmsi", tmsi));
                params.add(new BasicNameValuePair("appid", gameid));
                params.add(new BasicNameValuePair("channelid", gameid));
                params.add(new BasicNameValuePair("os", os));
                params.add(new BasicNameValuePair("osversion", osversion));
                params.add(new BasicNameValuePair("clientversion", vesion));
                params.add(new BasicNameValuePair("mobilemodel", phoneModel));
                params.add(new BasicNameValuePair("mobilenumber", mobilenumber));
                params.add(new BasicNameValuePair("vendor", vendor));
                params.add(new BasicNameValuePair("key", key));
                Log.d("hejiantest", uuid + ":" + imei + ":" + tmsi + ":" + gameid + ":" + vesion + ":" + phoneModel + ":" + mobilenumber + ":" + vendor);
                boolean issendstart = false;
                boolean issendinstall = false;
                try {
                    if (mreportintall == REPORTINSTALL_STATE_RECORDED) {
                        Log.d(TAG, "report install");
                        hc.post(INSTALLCOUNT, params);
                        issendinstall = true;
                    } else if (mreportstartup > 0) {
                        Log.e(TAG, "report start mreportstartup " + mreportstartup);
                        for (int i = 0; i < mreportstartup; i++) {
                            hc.post(STARTCOUNT, params);
                        }
                        issendstart = true;
                    }
                } catch (HttpException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    return;
                }
                SharedPreferences misReportSharedPreferences = context1.getSharedPreferences(PREF_NAME_REPORT, Context.MODE_WORLD_WRITEABLE);
                Editor editor = misReportSharedPreferences.edit();
                if (issendinstall) {
                    mreportintall = REPORTINSTALL_STATE_REPORTED;
                    editor.putInt(PREF_INT_REPORTINSTALL, mreportintall);

                }
                if (issendstart) {
                    mreportstartup = 0;
                    editor.putInt(PREF_INT_REPORTSTARTUP, 0);
                }
                editor.commit();

            }
        };
        httpsend.start();
    }

    public static void ReportSocialShareInfo(Context context, String appId, String sinaId,
                                             String tencentId, String weixinId, String qzoneId) {
        final String userid = Integer.toString(SportsApp.getInstance().getSportUser().getUid());

        SharedPreferences sp = context.getSharedPreferences(PREF_NAME_REPORT, Context.MODE_WORLD_WRITEABLE);
        String uuidTemp = sp.getString("uuid", null);
        if (uuidTemp == null) {
            uuidTemp = UUID.randomUUID().toString();
            SharedPreferences.Editor se = sp.edit();
            se.putString("uuid", uuidTemp);
            se.commit();
        }
        final String uuid = uuidTemp;
        final String key = MD5(userid + MD5KEY);

        final String sina_weibo_id = sinaId;
        final String tencent_weibo_id = tencentId;
        final String weixin_id = weixinId;
        final String qzone_id = qzoneId;

        Thread httpsend = new Thread() {

            @Override
            public void run() {
                HttpClient hc = new HttpClient();
                ArrayList<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
                params.add(new BasicNameValuePair("userid", userid));
                params.add(new BasicNameValuePair("contentid", "0"));
                params.add(new BasicNameValuePair("sina_weibo_id", sina_weibo_id));
                params.add(new BasicNameValuePair("tencent_weibo_id", tencent_weibo_id));
                params.add(new BasicNameValuePair("weixin_id", weixin_id));
                params.add(new BasicNameValuePair("qzone_id", qzone_id));
                params.add(new BasicNameValuePair("key", key));
                try {
                    hc.post(SOCIALSHARECOUNT, params);
                } catch (HttpException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    return;
                }
            }
        };
        httpsend.start();
    }

    public static void ReportFunctionIdCount(Context context, String appId, String functionId,
                                             String functionName, String functionTime) {
        final String userid = Integer.toString(SportsApp.getInstance().getSportUser().getUid());

        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        final String imei = telephonyManager.getDeviceId();
        final String gameid = appId;
        Log.e(TAG, "ReportFunctionIdCount GAMEID:" + gameid);
        final String os = "Android";
        final String osversion = android.os.Build.VERSION.RELEASE;
        final String vesion = getAppVersionName(context.getApplicationContext());
        final String tmsi = telephonyManager.getSubscriberId();

        SharedPreferences sp = context.getSharedPreferences(PREF_NAME_REPORT, Context.MODE_WORLD_WRITEABLE);
        String uuidTemp = sp.getString("uuid", null);
        if (uuidTemp == null) {
            uuidTemp = UUID.randomUUID().toString();
            SharedPreferences.Editor se = sp.edit();
            se.putString("uuid", uuidTemp);
            se.commit();
        }
        final String uuid = uuidTemp;
        final String key = MD5(userid + MD5KEY);

        final String functionid = functionId;
        final String function_name = functionName;
        final String function_time_interval = functionTime;

        Log.e(TAG, "userid =" + userid + " functionid=" + functionid + " function_name=" + function_name
                + " function_time_interval=" + function_time_interval + " uuid=" + uuid + " imei=" + imei
                + " tmsi=" + tmsi + " appid=" + gameid + " os=" + os + " osversion=" + osversion
                + " clientversion=" + vesion + " key=" + key);

        Thread httpsend = new Thread() {

            @Override
            public void run() {
                HttpClient hc = new HttpClient();
                ArrayList<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
                params.add(new BasicNameValuePair("userid", userid));
                params.add(new BasicNameValuePair("functionid", functionid));
                params.add(new BasicNameValuePair("function_name", function_name));
                params.add(new BasicNameValuePair("function_time_interval", function_time_interval));
                params.add(new BasicNameValuePair("uuid", uuid));
                params.add(new BasicNameValuePair("imei", imei));
                params.add(new BasicNameValuePair("tmsi", tmsi));
                params.add(new BasicNameValuePair("appid", gameid));
                params.add(new BasicNameValuePair("channelid", gameid));
                params.add(new BasicNameValuePair("os", os));
                params.add(new BasicNameValuePair("osversion", osversion));
                params.add(new BasicNameValuePair("clientversion", vesion));
                params.add(new BasicNameValuePair("key", key));
                try {
                    hc.post(FUNCTIONIDCOUNT, params);
                } catch (HttpException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    return;
                }
            }
        };
        httpsend.start();
    }

    public static String MD5(String string) {
        byte[] hash;
        try {
            hash = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Huh, MD5 should be supported?", e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Huh, UTF-8 should be supported?", e);
        }

        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10) hex.append("0");
            hex.append(Integer.toHexString(b & 0xFF));
        }
        return hex.toString();
    }

    public static String getAppVersionName(Context context) {

        String versionName = "";

        try {

            PackageManager pm = context.getPackageManager();

            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);

            versionName = pi.versionName;

            if (versionName == null || versionName.length() <= 0) {

                return "";

            }

        } catch (Exception e) {

        }

        return versionName;

    }

    public static boolean checkVersonUpdate(Context context) {
        String versionName = "";
        boolean result = false;
        try {
            versionName = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;

            SharedPreferences mCurVersonSharedPreferences = context.getSharedPreferences(PREF_CURRENT_VERSON, Context.MODE_WORLD_WRITEABLE);
            String mCurVerson = mCurVersonSharedPreferences.getString(PREF_S_CURVERSON, "");
            Editor versonE = mCurVersonSharedPreferences.edit();
            if (!mCurVerson.equals(versionName)) {
                mCurVerson = versionName;

                versonE.putString(PREF_S_CURVERSON, mCurVerson);
                versonE.putBoolean(PREF_B_VERSON_UPDATE, true);
                result = true;
            } else {
                versonE.putBoolean(PREF_B_VERSON_UPDATE, false);
                result = false;
            }
            versonE.commit();
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static void transferApp(Context context, String appId) {
        SharedPreferences misFirstRunSharedPreferences = context.getSharedPreferences(PREF_NAME_ISFIRSTRUN, Context.MODE_WORLD_WRITEABLE);
        boolean mIsfirstrun = misFirstRunSharedPreferences.getBoolean(PREF_B_ISFIRSTRUN, true);
        SharedPreferences.Editor se = misFirstRunSharedPreferences.edit();
        if (checkVersonUpdate(context)) {
            mIsfirstrun = true;
            se.putBoolean(PREF_B_ISFIRSTRUN, mIsfirstrun);
            se.commit();
        }
        ReportStatisticInfo(context, appId);
        if (mIsfirstrun) {
            mIsfirstrun = false;
            se.putBoolean(PREF_B_ISFIRSTRUN, mIsfirstrun);
            se.commit();
        }
    }

    public static void ReportTimeCount(Context context, String timeKey, String appId) {
        final String timeKey2 = timeKey;
        final String phoneModel = android.os.Build.MODEL;
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        final String imei = telephonyManager.getDeviceId();
        final String gameid = appId;
        final String vesion = getAppVersionName(context.getApplicationContext());
        final String mobilenumber = telephonyManager.getLine1Number();
        final String tmsi = telephonyManager.getSubscriberId();
        final String vendor = android.os.Build.MANUFACTURER;

        SharedPreferences sp = context.getSharedPreferences(PREF_NAME_REPORT, Context.MODE_WORLD_WRITEABLE);
        String uuidTemp = sp.getString("uuid", null);
        if (uuidTemp == null) {
            uuidTemp = UUID.randomUUID().toString();
            SharedPreferences.Editor se = sp.edit();
            se.putString("uuid", uuidTemp);
            se.commit();
        }
        final String uuid = uuidTemp;

        Thread httpsend = new Thread() {
            @Override
            public void run() {
                HttpClient hc = new HttpClient();
                ArrayList<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
                params.add(new BasicNameValuePair("uuid", uuid));
                params.add(new BasicNameValuePair("imei", imei));
                params.add(new BasicNameValuePair("tmsi", tmsi));
                params.add(new BasicNameValuePair("gameid", gameid));
                params.add(new BasicNameValuePair("clientversion", vesion));
                params.add(new BasicNameValuePair("mobilemodel", phoneModel));
                params.add(new BasicNameValuePair("mobilenumber", mobilenumber));
                params.add(new BasicNameValuePair("vendor", vendor));
                params.add(new BasicNameValuePair("timekey", timeKey2));

                //Response response;
                try {
                    hc.post(STARTTIMECOUNT, params);
                } catch (HttpException e) {
                    e.printStackTrace();
                    return;
                }
            }
        };

        httpsend.start();
    }

    public static void ReportComments(Context context, String msg, String appId) {
        final String phoneModel = android.os.Build.MODEL;
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        final String imei = telephonyManager.getDeviceId();
        final String vesion = getAppVersionName(context.getApplicationContext());
        final String mobilenumber = telephonyManager.getLine1Number();
        final String tmsi = telephonyManager.getSubscriberId();

        SharedPreferences sp = context.getSharedPreferences(PREF_NAME_REPORT, Context.MODE_WORLD_WRITEABLE);
        String uuidTemp = sp.getString("uuid", null);
        if (uuidTemp == null) {
            uuidTemp = UUID.randomUUID().toString();
            SharedPreferences.Editor se = sp.edit();
            se.putString("uuid", uuidTemp);
            se.commit();
        }
        final String uuid = uuidTemp;
        final String comments = msg;
        final String vendor = android.os.Build.MANUFACTURER;
        final String gameID = appId;

        Thread httpsend = new Thread() {
            @Override
            public void run() {
                HttpClient hc = new HttpClient();
                ArrayList<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
                params.add(new BasicNameValuePair("uuid", uuid));
                params.add(new BasicNameValuePair("imei", imei));
                params.add(new BasicNameValuePair("tmsi", tmsi));
                params.add(new BasicNameValuePair("gameid", gameID));
                params.add(new BasicNameValuePair("clientversion", vesion));
                params.add(new BasicNameValuePair("mobilemodel", phoneModel));
                params.add(new BasicNameValuePair("mobilenumber", mobilenumber));
                params.add(new BasicNameValuePair("vendor", vendor));
                params.add(new BasicNameValuePair("comments", comments));
//				Log.i(TAG,"## "+ uuid + "||" + imei + "||" + tmsi + "||"
//						+ GAMEID + "||" + vesion + "||" + phoneModel + "||"
//						+ mobilenumber + "||" + vendor + "||" + comments);
                try {
                    hc.post(COMMENTS, params);
                } catch (HttpException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    return;
                }
            }
        };

        httpsend.start();
    }


    public static void ReportBug(Context context, String error) {
        final String userid = Integer.toString(SportsApp.getInstance().getSportUser().getUid());
        final String gameid = FunctionStatic.getGameId(context);
        final String os = "Android";
        final String osversion = android.os.Build.VERSION.RELEASE;
        final String vesion = getAppVersionName(context.getApplicationContext());
        final String phoneModel = android.os.Build.MODEL;
        final String vendor = android.os.Build.MANUFACTURER;
        final String content = error;
        final String key = MD5(userid + gameid + MD5KEY);

        Log.e(TAG, "ReportFunctionIdCount GAMEID:" + gameid);
//		Log.e(TAG, "-----------------userid ="+userid );
//		Log.e(TAG, "-----------------content="+content );
//		Log.e(TAG, "-----------------os="+os );
//		Log.e(TAG, "-----------------osversion="+osversion );
//		Log.e(TAG, "-----------------clientversion="+vesion );
//		Log.e(TAG, "-----------------appid"+gameid );
//		Log.e(TAG, "-----------------mobilemodel"+phoneModel );
//		Log.e(TAG, "-----------------vendor"+vendor );
//		Log.e(TAG, "-----------------key="+key );

        Thread httpsend = new Thread() {

            @Override
            public void run() {
//	        	Log.e(TAG, "------------------------------------Error");
                HttpClient hc = new HttpClient();
                ArrayList<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
                params.add(new BasicNameValuePair("userid", userid));
                params.add(new BasicNameValuePair("appid", gameid));
                params.add(new BasicNameValuePair("channelid", gameid));
                params.add(new BasicNameValuePair("os", os));
                params.add(new BasicNameValuePair("osversion", osversion));
                params.add(new BasicNameValuePair("clientversion", vesion));
                params.add(new BasicNameValuePair("mobilemodel", phoneModel));
                params.add(new BasicNameValuePair("vendor", vendor));
                params.add(new BasicNameValuePair("content", content));
                params.add(new BasicNameValuePair("key", key));
                try {
                    hc.post(REPORTBUG, params);
                } catch (HttpException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    return;
                }
            }
        };
        httpsend.start();
    }
}
