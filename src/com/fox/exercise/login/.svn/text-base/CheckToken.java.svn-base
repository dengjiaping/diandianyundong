package com.fox.exercise.login;

import org.json.JSONException;
import org.json.JSONObject;

import com.fox.exercise.AllWeiboInfo;
import com.fox.exercise.weibo.sina.AccessInfo;
import com.fox.exercise.weibo.sina.InfoHelper;
import com.fox.exercise.weibo.sina.oauth2.AccessToken;
import com.fox.exercise.weibo.sina.oauth2.Weibo;
import com.fox.exercise.weibo.sina.oauth2.WeiboException;
import com.fox.exercise.weibo.sina.oauth2.WeiboParameters;
import com.fox.exercise.weibo.tencent.TencentDataHelper;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

public class CheckToken {

    private static final String TAG = "CheckToken";

    private Context mContext = null;

    private boolean sinaToastFlag = false;
    private boolean tencentToastFlag = false;
    private boolean qqzoneToastFlag = false;

    public CheckToken(Context context) {
        mContext = context;//TecfaceManagerActivity.mContext.getApplicationContext();
    }

    public void start() {
        SinaCheckThread sinaThread = new SinaCheckThread();
        sinaThread.start();

        TencentCheckThread tencentThread = new TencentCheckThread();
        tencentThread.start();

        QQzoneChechThread qqzoneThread = new QQzoneChechThread();
        qqzoneThread.start();
    }

    class SaveRefreshTokenThread extends Thread {
        @Override
        public void run() {

        }
    }

    class QQzoneChechThread extends Thread {

        @Override
        public void run() {
            SharedPreferences sp = mContext.getSharedPreferences(
                    AllWeiboInfo.TENCENT_QQZONE_TOKEN_SP, Context.MODE_PRIVATE);
            long oldTime = sp.getLong(
                    AllWeiboInfo.TENCENT_QQZONE_TOKEN_SP_TIME, 0L);
            if (oldTime == 0L) {
                Log.d(TAG, "no qqzone weibo info");
                return;
            }
            Log.d(TAG, "qqzone oldtime:" + oldTime);
            long now = System.currentTimeMillis();
            Log.d(TAG, "now:" + now);
            long maxTime = oldTime + 2592000000L;
            Log.d(TAG, "maxTime:" + maxTime);
//			mContext.getSharedPreferences(AllWeiboInfo.TENCENT_QQZONE_TOKEN_SP, Context.MODE_PRIVATE).edit().putString(AllWeiboInfo.TENCENT_QQZONE_NICK_KEY, null).commit();
            if (now > maxTime) {
                Log.d(TAG, "clear qqzone user");
                qqzoneToastFlag = true;

                SharedPreferences spZone = mContext.getSharedPreferences(
                        "user_login_info", Context.MODE_PRIVATE);

                Editor e = sp.edit();
                e.clear();
                e.commit();

                e = spZone.edit();
                e.clear();
                e.commit();
            }
        }

    }

    class TencentCheckThread extends Thread {

        @Override
        public void run() {
            SharedPreferences sp = mContext.getSharedPreferences(
                    AllWeiboInfo.TENCENT_TOKEN_SP, Context.MODE_PRIVATE);
            long oldTime = sp.getLong(AllWeiboInfo.TENCENT_TOKEN_SP_TIME, 0L);
            if (oldTime == 0) {
                Log.d(TAG, "no tecent weibo info");
                return;
            }
            long deltaTime = System.currentTimeMillis() - oldTime;
            Log.d(TAG, "tencent weibo deltaTime:" + String.valueOf(deltaTime));
            if (deltaTime > ((15 * 24 - 1) * 3600 * 1000) && oldTime != 0) {
                Log.d(TAG, "clear tecent weibo user");
                tencentToastFlag = true;
                TencentDataHelper dbHelper = new TencentDataHelper(mContext);
                dbHelper.clear();
                dbHelper.Close();
                Editor e = sp.edit();
                e.clear();
                e.commit();
            }
        }
    }

    class SinaCheckThread extends Thread {

        @Override
        public void run() {
            AccessInfo info = InfoHelper.getAccessInfo(mContext);
            String res = "";
            if (info != null) {
                AccessToken accessToken = new AccessToken(
                        info.getAccessToken(), info.getAccessSecret());
                accessToken.getToken();

                Weibo weibo = Weibo.getInstance();
                weibo.setAccessToken(accessToken);

                WeiboParameters params = new WeiboParameters();
                params.add("access_token", weibo.getAccessToken().getToken());
                Log.d(TAG, weibo.getAccessToken().getToken());

                try {
                    res = weibo.request(mContext, AllWeiboInfo.SINA_TOKEN_URL,
                            params, "POST", weibo.getAccessToken());
                    Log.d(TAG, "res:" + res);
                } catch (WeiboException e) {
                    e.printStackTrace();
                }
                int Expires = sinaJsonParse(res);
                if (Expires < 3600) {
                    sinaToastFlag = true;
                    Log.d(TAG, "clear sina accessinfo");
                    InfoHelper.clearAccessInfo(mContext);
                }
            }
        }
    }

    private int sinaJsonParse(String buff) {
        int Expires = 0;

        try {
            JSONObject object = new JSONObject(buff);
            Expires = object.getInt("expire_in");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return Expires;
    }
}
