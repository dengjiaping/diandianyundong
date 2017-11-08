package com.fox.exercise.login;


import cn.ingenic.indroidsync.SportsApp;

import com.fox.exercise.AllWeiboInfo;
import com.fox.exercise.api.ApiBack;
import com.fox.exercise.api.ApiConstant.WeiboType;
import com.fox.exercise.api.ApiJsonParser;
import com.fox.exercise.api.ApiNetException;


import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class LoginByOtherThread extends Thread {
    private static final String TAG = "LoginByOtherThread";
    private Handler mHandler = null;
    ApiBack back = null;
    private String weiboType, weiboName, token;

    public LoginByOtherThread(Handler handler, String weiboType, String weiboName, String token) {
        mHandler = handler;
        this.weiboType = weiboType;
        this.weiboName = weiboName;
        this.token = token;
    }


    @Override
    public void run() {
        // TODO Auto-generated method stub

        try {

            Log.e(TAG, "WeiboType.QQzone------" + WeiboType.QQzone);
            Log.e(TAG, "AllWeiboInfo.TENCENT_QQZONE_NICK------" + AllWeiboInfo.TENCENT_QQZONE_NICK);
            Log.e(TAG, "AllWeiboInfo.TENCENT_QQZONE_OPEN_ID------" + AllWeiboInfo.TENCENT_QQZONE_OPEN_ID);
            if (weiboType == null || "".equals(weiboType) || weiboName == null || "".equals(weiboName) || token == null
                    || "".equals(token)) {
                Message msg = Message.obtain(mHandler, LoginActivity.GET_LOGIN_PARAMS_FAIL);
                msg.sendToTarget();
            }

            SharedPreferences sp = SportsApp.getContext().getSharedPreferences("UmengDeviceToken", Activity.MODE_PRIVATE);
            back = ApiJsonParser.combineWeibo_New(weiboType, weiboName,
                    token, 1, sp.getString("device_token", ""));

            Log.e(TAG, "------------getFirst:" + back.getFirst());
            Log.e(TAG, "------------getFlag:" + back.getFlag());
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
                msg.setData(bundle);
                msg.sendToTarget();
            } else if (back.getFlag() == -1) {
                Message msg = Message.obtain(mHandler, LoginActivity.QQ_LOGIN_FIALED);
                Bundle bundle = new Bundle();
                bundle.putString("msg", back.getMsg());
                msg.setData(bundle);
                msg.sendToTarget();

            } else if (back.getFlag() == -11) {
                Message msg = Message.obtain(mHandler, LoginActivity.LOGIN_FAIL_DEVICE_DISABLE);
                Bundle bundle = new Bundle();
                bundle.putString("msg", "该设备无法登录，有疑问请与管理员联系");
                msg.setData(bundle);
                msg.sendToTarget();

            } else if (back.getFlag() == -5) {
                //判断是否第三方登录是否在数据库重名
                Message msg = Message.obtain(mHandler, 101);
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
