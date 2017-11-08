package com.fox.exercise.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.fox.exercise.R;
import com.fox.exercise.api.AddCoinsThread;
import com.fox.exercise.api.ApiBack;
import com.fox.exercise.api.ApiConstant;
import com.fox.exercise.api.ApiJsonParser;
import com.fox.exercise.api.ApiNetException;
import com.fox.exercise.api.ApiSessionOutException;
import com.fox.exercise.login.LoginActivity;
import com.fox.exercise.util.SportTaskUtil;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import cn.ingenic.indroidsync.SportsApp;

public class WXEntryActivity extends Activity implements IWXAPIEventHandler {
    private static final String TAG = "WXEntryActivity";
    private IWXAPI api;
    public static final String APP_ID = "wxbf77151c2fa30c8a";

    private SportsApp msApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        Log.d(TAG, "微信响应页面");
        msApp = (SportsApp) getApplication();
        api = WXAPIFactory.createWXAPI(this, APP_ID, false);
        api.registerApp(APP_ID);
        api.handleIntent(getIntent(), this);

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq arg0) {
        // TODO Auto-generated method stub
        finish();
    }

    @Override
    public void onResp(BaseResp arg0) {
        // TODO Auto-generated method stub
        switch (arg0.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                if (LoginActivity.isWeiXinLogin) {
                    LoginActivity.WX_CODE = ((SendAuth.Resp) arg0).code;
                    Log.e("WXEntryActivity", LoginActivity.WX_CODE);
                } else {
                    Toast.makeText(this, "微信分享成功", Toast.LENGTH_SHORT).show();
                    healthCount();
                    new AddCoinsThread(10, 4, new Handler() {
                        @Override
                        public void handleMessage(Message msg) {
                            // TODO Auto-generated method
                            // stub
                            switch (msg.what) {
                                case ApiConstant.COINS_SUCCESS:
                                    SportTaskUtil
                                            .jump2CoinsDialog(
                                                    WXEntryActivity.this,
                                                    getString(R.string.shared_success_add_coins));
                                    break;
                                case ApiConstant.COINS_LIMIT:
                                    Toast.makeText(getApplicationContext(),
                                            getString(R.string.shared_beyond_10times),
                                            Toast.LENGTH_SHORT).show();
                                    break;
                                default:
                                    break;
                            }
                        }
                    }, -1).start();
                }
                break;
            default:
                LoginActivity.isWeiXinLogin = false;
                break;
        }
        finish();
    }

    // 后台统计年终盘点微信点击的次数
    private void healthCount() {
        if (msApp != null && msApp.getSessionId() != null
                && msApp.isOpenNetwork()) {
            final SharedPreferences sp = getSharedPreferences("CelebrationSp", 0);
            final String weixindatacount = sp.getString("weixindatacount", "");
            if (weixindatacount != null && !"".equals(weixindatacount)) {
                new AsyncTask<Void, Void, ApiBack>() {
                    @Override
                    protected ApiBack doInBackground(Void... params) {
                        // TODO Auto-generated method stub
                        ApiBack back = null;
                        try {
                            back = ApiJsonParser.healthdatacount(
                                    msApp.getSessionId(), 3, weixindatacount, 0);
                        } catch (ApiNetException e) {
                            e.printStackTrace();
                        } catch (ApiSessionOutException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        return back;
                    }

                    @Override
                    protected void onPostExecute(ApiBack result) {
                        // TODO Auto-generated method stub
                        super.onPostExecute(result);
                        Editor edit = sp.edit();
                        edit.putString("weixindatacount", "");
                        edit.commit();
                    }
                }.execute();

            }

        }

    }

    /**
     *@method 固定字体大小方法
     *@author suhu
     *@time 2016/10/11 13:23
     *@param
     *
     */
    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        Configuration config=new Configuration();
        config.setToDefaults();
        res.updateConfiguration(config,res.getDisplayMetrics() );
        return res;
    }
}