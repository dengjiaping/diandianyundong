package com.fox.exercise.login;

import cn.ingenic.indroidsync.SportsApp;

import com.fox.exercise.SportsExceptionHandler;
import com.fox.exercise.R;
import com.fox.exercise.api.ApiBack;
import com.fox.exercise.api.ApiJsonParser;
import com.fox.exercise.api.ApiNetException;
import com.fox.exercise.api.ApiSessionOutException;
import com.fox.exercise.api.entity.UserDetail;
import com.fox.exercise.util.LogUtils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;


public class OriginalLoginTask extends AsyncTask<Void, Void, ApiBack> {

    private static final String TAG = "OriginalLaoginTask";

    private SportsApp mSportsApp = null;
    private Context mContext = null;
    private Handler mHandler = null;
    private OriginalLoginInfoViews mInfoViews = null;
    private OriginalLoginInfos mInfos = null;

    private SportsExceptionHandler mExceptionHandler = null;

    private Dialog mLoginPregressDialog = null;

    private int login_finish = 0;
    private int login_fail = 0;


    public OriginalLoginTask(Context context, OriginalLoginInfoViews infoViews, Handler handler) {
        mSportsApp = SportsApp.getInstance();
        mContext = context;
        mHandler = handler;
        mInfoViews = infoViews;
        mExceptionHandler = mSportsApp.getmExceptionHandler();
        this.login_finish = infoViews.getLoginFinish();
        this.login_fail = infoViews.getLoginFail();
    }

//	public OriginalLoginTask(Context context, OriginalLoginInfos infos, Dialog loginPregressDialog, Handler handler
//			) {
//		mSportsApp = SportsApp.getInstance();
//		mContext = context;
//		mHandler = handler;
//		mInfos = infos;
//		mExceptionHandler = mSportsApp.getmExceptionHandler();
//		mLoginPregressDialog = loginPregressDialog;
//		this.login_finish = infos.getLoginFinish();
//		this.login_fail = infos.getLoginFail();
//	}

    @Override
    protected ApiBack doInBackground(Void... arg0) {
        LogUtils.e(TAG, "doInBackground");
        ApiBack back = null;
        SharedPreferences sp_umeng = mContext.getSharedPreferences("UmengDeviceToken", Activity.MODE_PRIVATE);
        try {
            if (mInfoViews != null && mInfos == null) {
                back = ApiJsonParser.login(mInfoViews.getName().getText().toString(), mInfoViews.getPassword()
                        .getText().toString(), 1, sp_umeng.getString("device_token", ""));
            } else if (mInfoViews == null && mInfos != null) {
                back = ApiJsonParser.login(mInfos.getName(), mInfos.getPwd(), 1, sp_umeng.getString("device_token", ""));
            }
        } catch (ApiNetException e) {
            SportsApp.eMsg = Message.obtain(mExceptionHandler, SportsExceptionHandler.NET_ERROR);
            SportsApp.eMsg.sendToTarget();
            e.printStackTrace();
        }
        return back;
    }

    @Override
    protected void onPostExecute(ApiBack result) {
        if (result == null) {
            if (mInfoViews != null) {
                if (mInfoViews.getProgressDialog() != null)
                    mInfoViews.getProgressDialog().dismiss();
            } else {
                if (mLoginPregressDialog != null)
                    mLoginPregressDialog.dismiss();
            }
            Message.obtain(mHandler, login_fail).sendToTarget();
            Toast.makeText(mContext, mContext.getResources().getString(R.string.sports_server_error),
                    Toast.LENGTH_SHORT).show();
        } else {
            if (mInfoViews != null)
                if (mInfoViews.getProgressDialog() != null)
                    mInfoViews.getProgressDialog().dismiss();
            LogUtils.e(TAG, "message.isFlag():" + result.getFlag());
            if (result.getFlag() == 0 || result.getFlag() == 1) {
                //LogUtils.e(TAG, "ʱ��ָ���----------------------��һ�ε�¼���");
                if (result.getFlag() == 1) {
                    SportsApp.mIsAdmin = true;
                } else {
                    SportsApp.mIsAdmin = false;
                }
                LogUtils.e(TAG, "loginsuccess");
                if (result.getMsg() == null)
                    return;
                String sessionid = result.getMsg().substring(7);
                LogUtils.e(TAG, "session_id" + sessionid);
                mSportsApp.setSessionId(sessionid);
                mSportsApp.setLogin(true);
                LogUtils.d(TAG, "SportsApp.sessionId:" + mSportsApp.getSessionId());
                LogUtils.d(TAG, "SportsApp.isLogin:" + mSportsApp.isLogin());
                SharedPreferences sp = mContext.getSharedPreferences("user_login_info", Context.MODE_PRIVATE);
                Editor ed = sp.edit();
                ed.clear();
                if (mInfos != null && mInfoViews == null) {
                    ed.putString("account", mInfos.getName());
                    ed.putString("pwd", mInfos.getPwd());
                } else if (mInfos == null && mInfoViews != null) {
                    ed.putString("account", mInfoViews.getName().getText().toString());
                    ed.putString("pwd", mInfoViews.getPassword().getText().toString());
                }
                //�����û���ݱ�ʶ
                ed.putInt("login_way", 0);
                ed.commit();

                if (result.getFirst() == 0)
                    LoginActivity.mIsRegister = true;
                else
                    LoginActivity.mIsRegister = false;
                Message.obtain(mHandler, login_finish).sendToTarget();
            }else if(result.getFlag() == -100){
                Log.e(TAG, "WIFI未验证");
                Message msg = Message.obtain(mHandler, login_fail, "checkyour_WIFI_isconnect");
                msg.sendToTarget();
            } else if (result.getFlag() == -56){
                Log.e(TAG, "请求服务器超时");
                Message msg = Message.obtain(mHandler, login_fail, "socketoutoftime");
                msg.sendToTarget();
            } else if (result.getFlag() == -11) {
                LogUtils.e(TAG, "该设备无法登录，有疑问请与管理员联系");
                Message msg = Message.obtain(mHandler, login_fail, "login_fail_device_disable");
                msg.sendToTarget();
            } else {
                LogUtils.e(TAG, "loginfailed");
                Message msg = Message.obtain(mHandler, login_fail, "accountOrPwdError");
                msg.sendToTarget();
            }
        }
    }

    @Override
    protected void onPreExecute() {
        // TODO Auto-generated method stub
        super.onPreExecute();
    }

}
