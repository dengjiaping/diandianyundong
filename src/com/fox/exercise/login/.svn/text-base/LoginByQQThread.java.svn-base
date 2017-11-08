package com.fox.exercise.login;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.fox.exercise.AllWeiboInfo;
import com.fox.exercise.api.ApiBack;
import com.fox.exercise.api.ApiConstant.WeiboType;
import com.fox.exercise.api.ApiJsonParser;
import com.fox.exercise.api.ApiNetException;

import cn.ingenic.indroidsync.SportsApp;

public class LoginByQQThread extends Thread {
    private static final String TAG = "LoginByQQThread";
    private Handler mHandler = null;
    private int login_fail = 0;
    //	private Context mContext = null;
//	private String mSex = "";
    ApiBack back = null;

    public LoginByQQThread(Context context, Handler handler) {
        mHandler = handler;
//		mContext = context;

    }

    @Override
    public void run() {
        // TODO Auto-generated method stub

        try {

            Log.e(TAG, "WeiboType.QQzone------" + WeiboType.QQzone);
            Log.e(TAG, "AllWeiboInfo.TENCENT_QQZONE_NICK------"
                    + AllWeiboInfo.TENCENT_QQZONE_NICK);
            Log.e(TAG, "AllWeiboInfo.TENCENT_QQZONE_OPEN_ID------"
                    + AllWeiboInfo.TENCENT_QQZONE_OPEN_ID);
            if (WeiboType.QQzone == null || "".equals(WeiboType.QQzone)
                    || AllWeiboInfo.TENCENT_QQZONE_NICK == null
                    || "".equals(AllWeiboInfo.TENCENT_QQZONE_NICK)
                    || AllWeiboInfo.TENCENT_QQZONE_OPEN_ID == null
                    || "".equals(AllWeiboInfo.TENCENT_QQZONE_OPEN_ID)) {
                Message msg = Message.obtain(mHandler,
                        LoginActivity.GET_LOGIN_PARAMS_FAIL);
                msg.sendToTarget();
            }
            SharedPreferences sp = SportsApp.getContext().getSharedPreferences("UmengDeviceToken", Activity.MODE_PRIVATE);
            back = ApiJsonParser.combineWeibo_New(WeiboType.QQzone,
                    AllWeiboInfo.TENCENT_QQZONE_NICK,
                    AllWeiboInfo.TENCENT_QQZONE_OPEN_ID, 1, sp.getString("device_token", ""));

            Log.e(TAG, "------------getFirst:" + back.getFirst());
            Log.e(TAG, "------------getFlag:" + back.getFlag());
            Log.e(TAG, "------------getMsg1" + back.getMsg());
            if (back.getFirst() == 0)
                LoginActivity.mIsRegister = true;
            else
                LoginActivity.mIsRegister = false;
            if (back.getFlag() == 0 || back.getFlag() == 1) {
                if (back.getFlag() == 1) {
                    SportsApp.mIsAdmin = true;
                } else {
                    SportsApp.mIsAdmin = false;
                }
                Message msg = Message.obtain(mHandler, 2);
                Bundle bundle = new Bundle();
                bundle.putString("msg", back.getMsg());
                Log.e(TAG, "getMsg2------------" + back.getMsg());
                msg.setData(bundle);
                msg.sendToTarget();
            } else if (back.getFlag() == -1) {
                Message msg = Message.obtain(mHandler,
                        LoginActivity.QQ_LOGIN_FIALED);
                Bundle bundle = new Bundle();
                bundle.putString("msg", back.getMsg());
                msg.setData(bundle);
                msg.sendToTarget();
            } else if (back.getFlag() == -101) {
                Message msg = Message.obtain(mHandler,
                        LoginActivity.LOGIN_FAIL_NOCHECK_WIFI_LOGIN);
                Bundle bundle = new Bundle();
                bundle.putString("msg", "WIFI没有认证");
                msg.setData(bundle);
                msg.sendToTarget();
            } else if (back.getFlag() == -56) {
                Message msg = Message.obtain(mHandler,
                        LoginActivity.LOGIN_FAIL_OUTOF_TIME);
                Bundle bundle = new Bundle();
                bundle.putString("msg", "QQ登录，请求服务器超时");
                msg.setData(bundle);
                msg.sendToTarget();
            } else if (back.getFlag() == -11) {
                Message msg = Message.obtain(mHandler,
                        LoginActivity.LOGIN_FAIL_DEVICE_DISABLE);
                Bundle bundle = new Bundle();
                bundle.putString("msg", "该设备无法登录，有疑问请与管理员联系");
                msg.setData(bundle);
                msg.sendToTarget();
            } else if (back.getFlag() == -5) {
                // 判断是否第三方登录是否在数据库重名 表示注册
                Message msg = Message.obtain(mHandler, 101);
                Bundle bundle = new Bundle();
                bundle.putString("msg", back.getMsg());
                msg.setData(bundle);
                msg.sendToTarget();
            } else if (back.getFlag() == -10) {
                // 判断是否第三方登录是否在数据库重名 表示已注册
                Message msg = Message.obtain(mHandler, 102);
                Bundle bundle = new Bundle();
                bundle.putString("msg", back.getMsg());
                msg.setData(bundle);
                msg.sendToTarget();
            } else {
                Message msg = Message.obtain(mHandler,
                        LoginActivity.QQ_LOGIN_FIALED);
                Bundle bundle = new Bundle();
                bundle.putString("msg", back.getMsg());
                msg.setData(bundle);
                msg.sendToTarget();
            }
        } catch (ApiNetException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

}
