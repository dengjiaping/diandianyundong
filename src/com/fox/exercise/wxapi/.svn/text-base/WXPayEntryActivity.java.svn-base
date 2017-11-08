package com.fox.exercise.wxapi;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.fox.exercise.MainFragmentActivity;
import com.fox.exercise.R;
import com.fox.exercise.SportMeWebView;
import com.fox.exercise.YunHuWebViewActivity;
import com.fox.exercise.api.ApiJsonParser;
import com.fox.exercise.api.ApiNetException;
import com.fox.exercise.api.ApiSessionOutException;
import com.fox.exercise.newversion.entity.YeDindanInfo;
import com.fox.exercise.newversion.newact.NightRunWebViewActivity;
import com.fox.exercise.wxutil.Constants;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import cn.ingenic.indroidsync.SportsApp;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

    private static final String TAG = "MicroMsg.SDKSample.WXPayEntryActivity";

    private IWXAPI api;
    private YeDindanInfo ypinfo, shopinfo, sportinfo;// 夜跑订单信息类
    int type, coode, bs, shopbs, sportbs;
    String sessionid, numid, shopnumid, sportsnumid;
    SportsApp mSportsApp;
    private Dialog mLoadProgressDialog = null;

    TextView text;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_activity);
        api = WXAPIFactory.createWXAPI(this, Constants.APP_ID);
        api.handleIntent(getIntent(), this);
        mSportsApp = (SportsApp) getApplication();
        sessionid = mSportsApp.getSessionId();
        numid = NightRunWebViewActivity.numid;
        bs = NightRunWebViewActivity.biaoshi;
        shopbs = YunHuWebViewActivity.biaoshi;
        shopnumid = YunHuWebViewActivity.Numid;
        sportbs = SportMeWebView.biaoshi;
        sportsnumid = SportMeWebView.numid;
        //Log.e("SportsApp", mSportsApp.getSessionId());
        text = (TextView) findViewById(R.id.text_content);
        if (mSportsApp.getmYePaoHandler() != null) {
            mSportsApp.getmYePaoHandler().sendEmptyMessage(400);
        }
        if (mSportsApp.getmShopHandler() != null) {
            mSportsApp.getmShopHandler().sendEmptyMessage(101);
        }

    }

    private void waitShowDialog() {
        // TODO Auto-generated method stub
        if (mLoadProgressDialog == null) {
            mLoadProgressDialog = new Dialog(this, R.style.sports_dialog);
            LayoutInflater mInflater = this.getLayoutInflater();
            View v1 = mInflater.inflate(R.layout.bestgirl_progressdialog, null);
            TextView mDialogMessage = (TextView) v1.findViewById(R.id.message);
            mDialogMessage.setText(R.string.PayInfo);
            v1.setMinimumWidth((int) (SportsApp.ScreenWidth * 0.8));
            mLoadProgressDialog.setCancelable(false);
            mLoadProgressDialog.setContentView(v1);
        }
        if (mLoadProgressDialog != null)
            if (!mLoadProgressDialog.isShowing() && !this.isFinishing())
                mLoadProgressDialog.show();
    }

    public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            mLoadProgressDialog.dismiss();
            Intent mtIntent = new Intent(WXPayEntryActivity.this,
                    MainFragmentActivity.class);
            startActivity(mtIntent);
        }
        return super.onKeyDown(keyCode, event);
    }

    ;

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            mLoadProgressDialog.dismiss();

            switch (msg.what) {
                case 200:
                    if (bs == 1 || sportbs == 3) {
                        if (coode == 0 && ypinfo.getPay_status() == 2 || sportinfo.getPay_status() == 2) {
                            // text.setVisibility(View.VISIBLE);
                            AlertDialog.Builder builder = new Builder(
                                    WXPayEntryActivity.this);
                            builder.setTitle("支付成功!");
                            builder.setMessage("您已成功支付!");
                            builder.setPositiveButton("确定", new OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    // TODO Auto-generated method stub

//                                    Uri uri = Uri.parse(ApiConstant.DATA_URL
//                                            + ApiConstant.SuccessHtml
//                                            + ypinfo.getOrder_nu() + "&session_id="
//                                            + sessionid);
//                                    Intent intent = new Intent(Intent.ACTION_VIEW,
//                                            uri);
//                                    startActivity(intent);
//                                    Intent mintent = new Intent(WXPayEntryActivity.this, NewHuoDongListFrg.class);
//                                    startActivity(mintent);

                                    mSportsApp.getmYePaoHandler();
                                    if (mSportsApp.getmYePaoHandler() != null) {
                                        mSportsApp.getmYePaoHandler()
                                                .sendEmptyMessage(300);
                                    }
                                    finish();

                                }
                            });
                            builder.create().show();
                        } else if (coode == -1 && ypinfo.getPay_status() == 1) {
                            // text.setVisibility(View.VISIBLE);
                            AlertDialog.Builder builder = new Builder(
                                    WXPayEntryActivity.this);
                            builder.setTitle("支付失败");
                            builder.setMessage("您支付失败!");
                            builder.setPositiveButton("确定", new OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    // TODO Auto-generated method stub
//                                    Uri uri = Uri.parse(ApiConstant.DATA_URL
//                                            + ApiConstant.FailHtml
//                                            + ypinfo.getOrder_nu() + "&session_id="
//                                            + sessionid);
//                                    Intent intent = new Intent(Intent.ACTION_VIEW,
//                                            uri);
//                                    startActivity(intent);
                                    mSportsApp.getmYePaoHandler();
                                    if (mSportsApp.getmYePaoHandler() != null) {
                                        mSportsApp.getmYePaoHandler()
                                                .sendEmptyMessage(500);
                                    }
                                }
                            });
                            builder.create().show();
                        } else if (coode == -2) {
                            // text.setVisibility(View.VISIBLE);
                            AlertDialog.Builder builder = new Builder(
                                    WXPayEntryActivity.this);
                            builder.setTitle("支付提示");
                            builder.setMessage("您取消了支付!");
                            builder.setPositiveButton("确定", new OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    // TODO Auto-generated method stub
                                    finish();
                                    mSportsApp.getmYePaoHandler();
                                    if (mSportsApp.getmYePaoHandler() != null) {
                                        mSportsApp.getmYePaoHandler()
                                                .sendEmptyMessage(700);
                                    }
                                }
                            });
                            builder.create().show();
                        }
                    }
                    if (shopbs == 2) {
                        if (coode == 0 && shopinfo.getPay_status() == 2) {
                            AlertDialog.Builder builder = new Builder(
                                    WXPayEntryActivity.this);
                            builder.setTitle("支付成功!");
                            builder.setMessage("您已成功支付!");
                            builder.setPositiveButton("确定", new OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    finish();
                                    Intent intent = new Intent(
                                            WXPayEntryActivity.this,
                                            YunHuWebViewActivity.class);
                                    startActivityForResult(intent, 0);
                                    mSportsApp.getmShopHandler();
                                    if (mSportsApp.getmShopHandler() != null) {
                                        mSportsApp.getmShopHandler()
                                                .sendEmptyMessage(102);
                                    }

                                }
                            });
                            builder.create().show();
                        } else if (coode == -1 && shopinfo.getPay_status() == 1) {
                            // text.setVisibility(View.VISIBLE);
                            AlertDialog.Builder builder = new Builder(
                                    WXPayEntryActivity.this);
                            builder.setTitle("支付失败");
                            builder.setMessage("您支付失败!");
                            builder.setPositiveButton("确定", new OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    // TODO Auto-generated method stub
                                    finish();
                                    Intent intent = new Intent(
                                            WXPayEntryActivity.this,
                                            YunHuWebViewActivity.class);
                                    startActivityForResult(intent, 0);
                                    mSportsApp.getmShopHandler();
                                    if (mSportsApp.getmShopHandler() != null) {
                                        mSportsApp.getmShopHandler()
                                                .sendEmptyMessage(103);
                                    }

                                }
                            });
                            builder.create().show();
                        } else if (coode == -2) {
                            // text.setVisibility(View.VISIBLE);
                            AlertDialog.Builder builder = new Builder(
                                    WXPayEntryActivity.this);
                            builder.setTitle("支付提示");
                            builder.setMessage("您取消了支付!");
                            builder.setPositiveButton("确定", new OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    // TODO Auto-generated method stub
                                    finish();
                                }
                            });
                            builder.create().show();
                        }
                    }

                    break;
                default:
                    break;
            }
            // 金币商城

        }

        ;
    };

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
    }

    // 开启线程获取用户报名信息
    private void GetData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                try {

                    ypinfo = ApiJsonParser.getDianDanInfo(sessionid, numid, 1);
                    shopinfo = ApiJsonParser.getDianDanInfo(sessionid,
                            shopnumid, 2);
                    sportinfo = ApiJsonParser.getDianDanInfo(sessionid,
                            sportsnumid, 1);
                    mHandler.sendEmptyMessage(200);
                } catch (ApiNetException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (ApiSessionOutException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public void onResp(BaseResp resp) {
        // Log.d(TAG, "onPayFinish, errCode = " + resp.errCode);
        // 获取微信返回值判断是否成功
        coode = resp.errCode;
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            if (coode == 0) {
            } else if (coode == -2) {

            } else if (coode == -1) {

            }
            waitShowDialog();
            GetData();
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