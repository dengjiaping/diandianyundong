package com.fox.exercise;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.fox.exercise.api.AddCoinsThread;
import com.fox.exercise.api.ApiConstant;
import com.fox.exercise.util.SportTaskUtil;
import com.fox.exercise.weibo._FakeX509TrustManager;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import cn.ingenic.indroidsync.SportsApp;

public class ShareToQQzone extends Activity implements Button.OnClickListener {
    private ImageView mDisplayImage;
    private Bitmap mBitmapSrc = null;
    private EditText mInputText;
    private static String mAppid = "801268715";
    private ProgressDialog dialog = null;
    private String thisLarge = null;
    private String thisMessage = null;
    private String thisUrl = null;
    private String zoneToken;
    private String opid;
    private Context mContext;
    private SportsApp mSportsApp;
    private int tShare = 1;
    private static final String TAG = "ShareToQQzone";

    private Dialog loginPregressDialog = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boolean findMethod = SmartBarUtils.findActionBarTabsShowAtBottom();
        if (!findMethod) {
            // 取消ActionBar拆分，换用TabHost
            getWindow().setUiOptions(0);
            getWindow().requestFeature(android.view.Window.FEATURE_NO_TITLE);
        }
        setContentView(R.layout.sports_shareto_weibo);

        Log.d(TAG, "ShareToQQzone onCreate");

        mContext = getApplicationContext();
        mSportsApp = (SportsApp) getApplication();
        dialog = new ProgressDialog(this);
        dialog.setMessage(getString(R.string.sharing));
        dialog.setIndeterminate(false);
        dialog.setCancelable(true);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            thisLarge = bundle.containsKey("thisLarge") ? bundle.getString("thisLarge") : "";
            thisMessage = bundle.containsKey("thisMessage") ? bundle.getString("thisMessage") : "";
            thisUrl = bundle.containsKey("thisUrl") ? bundle.getString("thisUrl") : "";
            zoneToken = bundle.containsKey("zoneToken") ? bundle.getString("zoneToken") : "";
            opid = bundle.containsKey("opid") ? bundle.getString("opid") : "";
        }
        initRes();
        if (findMethod) {
            getActionBar().setDisplayShowHomeEnabled(false);
            getActionBar().setDisplayShowTitleEnabled(false);
            SmartBarUtils.setActionModeHeaderHidden(getActionBar(), true);
            SmartBarUtils.setActionBarViewCollapsable(getActionBar(), true);
        }
        // mBitmapSrc = BitmapFactory.decodeFile(thisLarge);
        mBitmapSrc = Util.decodeFile(thisLarge);
        mDisplayImage.setImageBitmap(mBitmapSrc);

        loginPregressDialog = new Dialog(this, R.style.sports_dialog);
        LayoutInflater mInflater = getLayoutInflater();
        View v1 = mInflater.inflate(R.layout.sports_progressdialog, null);
        TextView message = (TextView) v1.findViewById(R.id.message);
        message.setText(R.string.sports_shareing);
        v1.setMinimumWidth((int) (SportsApp.ScreenWidth * 0.8));
        loginPregressDialog.setContentView(v1);
        loginPregressDialog.setCanceledOnTouchOutside(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
//        MobclickAgent.onResume(mContext);
    }

    @Override
    protected void onPause() {
        super.onPause();
//		MobclickAgent.onPause(mContext);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (loginPregressDialog != null) {
            loginPregressDialog.dismiss();
        }
    }

    private void initRes() {
        mInputText = (EditText) findViewById(R.id.shareto_edittext);
        mInputText.setText(thisMessage);
        // mInputText.setVisibility(View.GONE);
        mDisplayImage = (ImageView) findViewById(R.id.share_image);
        findViewById(R.id.button_sendto).setOnClickListener(this);
//		TextView titleText = (TextView) findViewById(R.id.title_text);
//		titleText.setText(getString(R.string.share_to) + getString(R.string.text_qq_zone));
        findViewById(R.id.bt_back).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_sendto:
                // SendIamgeAndText();

                // Bundle parmas = new Bundle();
                // parmas.putString("title", "美美相机");
                // parmas.putString("url",
                // "http://cdn.17vee.com/lmstation/Beauty/17veeBeauty.apk");
                //
                // parmas.putString("comment", thisMessage);
                // parmas.putString("summary", "最美女孩");
                //
                // parmas.putString("images",
                // thisUrl);
                // parmas.putString("type", "4");// 分享内容的类型。
                // parmas.putString(
                // "playurl",
                // "http://player.youku.com/player.php/Type/Folder/Fid/15442464/Ob/1/Pt/0/sid/XMzA0NDM2NTUy/v.swf");//
                // 长度限制为256字节。仅在type=5的时候有效。

                if (loginPregressDialog != null && !loginPregressDialog.isShowing())
                    loginPregressDialog.show();
                // AllWeiboInfo.mTencent.requestAsync(Constants.GRAPH_ADD_SHARE,
                // parmas,
                // Constants.HTTP_POST, new BaseApiListener(), null);
                ShareToQQzoneThread t = new ShareToQQzoneThread();
                t.start();

                break;
            case R.id.bt_back:
                finish();
                break;
        }
    }

    class ShareToQQzoneThread extends Thread {

        @Override
        public void run() {
            try {

                _FakeX509TrustManager.allowAllSSL();// 调用
                URL postUrl = new URL("https://graph.qq.com/share/add_share");
                HttpURLConnection conn = (HttpURLConnection) postUrl.openConnection();
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setRequestMethod("POST");

                StringBuffer params = new StringBuffer();
                SharedPreferences sp = getSharedPreferences(AllWeiboInfo.TENCENT_QQZONE_TOKEN_SP, MODE_PRIVATE);
                AllWeiboInfo.TENCENT_QQZONE_TOKEN = sp.getString(AllWeiboInfo.TENCENT_QQZONE_TOKEN_KEY, "");
                AllWeiboInfo.TENCENT_QQZONE_OPEN_ID = sp.getString(AllWeiboInfo.TENCENT_QQZONE_OPEN_ID_KEY, "");

                params.append(URLEncoder.encode("access_token", "utf-8") + "="
                        + URLEncoder.encode(AllWeiboInfo.TENCENT_QQZONE_TOKEN, "utf-8"));
                params.append("&" + URLEncoder.encode("oauth_consumer_key", "utf-8") + "="
                        + URLEncoder.encode(AllWeiboInfo.TENCENT_APPID, "utf-8"));
                params.append("&" + URLEncoder.encode("openid", "utf-8") + "="
                        + URLEncoder.encode(AllWeiboInfo.TENCENT_QQZONE_OPEN_ID, "utf-8"));
                params.append("&" + URLEncoder.encode("title", "utf-8") + "=" + URLEncoder.encode("云狐运动", "utf-8"));
                params.append("&" + URLEncoder.encode("url", "utf-8") + "="
                        + URLEncoder.encode("http://cdn.17vee.com/lmstation/Beauty/17veeBeauty.apk", "utf-8"));
                params.append("&" + URLEncoder.encode("comment", "utf-8") + "="
                        + URLEncoder.encode(thisMessage, "utf-8"));
                params.append("&" + URLEncoder.encode("summary", "utf-8") + "=" + URLEncoder.encode("云狐运动", "utf-8"));
                params.append("&" + URLEncoder.encode("images", "utf-8") + "=" + URLEncoder.encode(thisUrl, "utf-8"));
                params.append("&" + URLEncoder.encode("type", "utf-8") + "=" + URLEncoder.encode("4", "utf-8"));
                params.append("&" + URLEncoder.encode("site", "utf-8") + "=" + URLEncoder.encode("云狐运动", "utf-8"));
                params.append("&" + URLEncoder.encode("fromurl", "utf-8") + "="
                        + URLEncoder.encode("http://cdn.17vee.com/lmstation/Beauty/17veeBeauty.apk", "utf-8"));
                // params.append("&"+URLEncoder.encode("title","utf-8")+"="+URLEncoder.encode("美美相机","utf-8"));
                // params.append("&"+URLEncoder.encode("url","utf-8")+"="+URLEncoder.encode("http://cdn.17vee.com/lmstation/Beauty/17veeBeauty.apk","utf-8"));
                // params.append("&"+URLEncoder.encode("images","utf-8")+"="+URLEncoder.encode(thisLarge,"utf-8"));
                // params.append("&"+URLEncoder.encode("site","utf-8")+"="+URLEncoder.encode("美美相机","utf-8"));
                // params.append("&"+URLEncoder.encode("fromurl","utf-8")+"="+URLEncoder.encode("http://cdn.17vee.com/lmstation/Beauty/17veeBeauty.apk","utf-8"));

                Log.d(TAG, "params:" + params.toString());

                DataOutputStream out = new DataOutputStream(conn.getOutputStream());
                out.write(params.toString().getBytes());

                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String line = "";
                StringBuffer res = new StringBuffer();
                while ((line = reader.readLine()) != null) {
                    res.append(line);
                }
                Log.d(TAG, res.toString());

                Message msg = Message.obtain(handle, 1);
                msg.sendToTarget();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    Handler handle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (dialog != null) {
                dialog.dismiss();
            }

            // thisLarge = null;
            if (msg.what > 0) {
                if (mSportsApp.isLogin() && mSportsApp.getSessionId() != null
                        && (!"".endsWith(mSportsApp.getSessionId()))) {
                    /*Toast.makeText(mContext, getString(R.string.shared_success_add_coins), Toast.LENGTH_SHORT).show();
//					MobclickAgent.onEvent(mContext,"shareto","qqzone");
					final String time = mSportsApp
							.getCurrentDate();
					String shareCoins = SportTaskUtil
							.readShareCoinsFromFile(mSportsApp.getSportUser().getUid());
					boolean isShared=true;
					if (TextUtils.isEmpty(shareCoins)) { // 第一次使用
					} else {
						String[] sCoinString = shareCoins
								.split("#");
						if(sCoinString.length == 2){
						final String date = sCoinString[0];
							if (date.equals(time)) { // 如果是同一天要加上之前的分享次数
								tShare = Integer
										.parseInt(sCoinString[1])
										+ 1;
								if (tShare >10){
									isShared=false;
									Toast.makeText(mContext, getString(R.string.shared_beyond_10times), Toast.LENGTH_SHORT).show();
								}
							}
						}
					}
					if(isShared){*/
                    new AddCoinsThread(10, 4, new Handler() {

                        @Override
                        public void handleMessage(
                                Message msg) {
                            // TODO Auto-generated method
                            // stub
                                /**/
                            switch (msg.what) {
                                case ApiConstant.COINS_SUCCESS:
                                    SportTaskUtil.jump2CoinsDialog(ShareToQQzone.this, getString(R.string.shared_success_add_coins));
                                    break;
                                case ApiConstant.COINS_LIMIT:
                                    Toast.makeText(getApplicationContext(), getString(R.string.shared_beyond_10times), Toast.LENGTH_SHORT).show();
                                    break;
                                default:
                                    break;
                            }

                        }

                    }, -1).start();
                    //}
                }
                returnToHome();
            } else {
                Toast.makeText(mContext, getString(R.string.shared_failed), Toast.LENGTH_SHORT).show();
            }
        }
    };

    // public void SendIamgeAndText() {
    // Bundle bundle = null;
    // bundle = new Bundle();
    // bundle.putString("richtype", "1");
    // bundle.putString("richval", "url=" + thisUrl + "&width=25&height=21");
    // bundle.putString("con", mInputText.getText().toString());
    // Log.i("QQzone", "## SendIamgeAndText");
    // Log.i("QQzone:", "## thisUrl" + thisUrl);
    // final Bundle bundle1 = bundle;
    // new Thread() {
    // @Override
    // public void run() {
    // TencentOpenAPI.addTopic(zoneToken, mAppid, opid, bundle1,
    // new Callback() {
    //
    // @Override
    // public void onSuccess(final Object obj) {
    // Log.i("QQzone", "## onSuccess");
    // handle.sendEmptyMessage(1);
    // }
    //
    // @Override
    // public void onFail(final int ret, final String msg) {
    // Log.i("QQzone", "## onFail msg : " + msg
    // + " , ret : " + ret);
    // if (ret != 0)
    // handle.sendEmptyMessage(0);
    // }
    //
    // @Override
    // public void onCancel(int flag) {
    // Log.i("QQzone", "## onCancel");
    // }
    // });
    // }
    // }.start();
    // }

    private void returnToHome() {
        if (loginPregressDialog != null)
            if (loginPregressDialog.isShowing())
                loginPregressDialog.dismiss();

        setResult(RESULT_OK, new Intent());
        finish();
    }

    // private class BaseApiListener implements IRequestListener {
    //
    // @Override
    // public void onComplete(JSONObject response, Object state) {
    // doComplete(response, state);
    // }
    //
    // protected void doComplete(JSONObject response, Object state) {
    // handle.sendEmptyMessage(1);
    // if(loginPregressDialog!=null)
    // if(loginPregressDialog.isShowing())
    // loginPregressDialog.dismiss();
    // }
    //
    // @Override
    // public void onConnectTimeoutException(ConnectTimeoutException arg0,
    // Object arg1) {
    // // TODO Auto-generated method stub
    //
    // }
    //
    // @Override
    // public void onHttpStatusException(HttpStatusException arg0, Object arg1)
    // {
    // // TODO Auto-generated method stub
    //
    // }
    //
    // @Override
    // public void onIOException(IOException arg0, Object arg1) {
    // // TODO Auto-generated method stub
    //
    // }
    //
    // @Override
    // public void onJSONException(JSONException arg0, Object arg1) {
    // // TODO Auto-generated method stub
    //
    // }
    //
    // @Override
    // public void onMalformedURLException(MalformedURLException arg0,
    // Object arg1) {
    // // TODO Auto-generated method stub
    //
    // }
    //
    // @Override
    // public void onNetworkUnavailableException(
    // NetworkUnavailableException arg0, Object arg1) {
    // // TODO Auto-generated method stub
    //
    // }
    //
    // @Override
    // public void onSocketTimeoutException(SocketTimeoutException arg0,
    // Object arg1) {
    // // TODO Auto-generated method stub
    //
    // }
    //
    // @Override
    // public void onUnknowException(Exception arg0, Object arg1) {
    // // TODO Auto-generated method stub
    //
    // }
    //
    // }

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
