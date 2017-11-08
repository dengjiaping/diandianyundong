package com.fox.exercise;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.fox.exercise.api.AddCoinsThread;
import com.fox.exercise.api.ApiConstant;
import com.fox.exercise.api.ApiJsonParser;
import com.fox.exercise.http.FunctionStatic;
import com.fox.exercise.http.StatCounts;
import com.fox.exercise.util.SportTaskUtil;
import com.fox.exercise.weibo.sina.InfoHelper;
import com.fox.exercise.weibo.tencent.TencentDataHelper;
import com.fox.exercise.weibo.tencent.UserInfo;
import com.fox.exercise.weibo.tencent.proxy.QQWeiboProxy;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.List;

import cn.ingenic.indroidsync.SportsApp;

public class ShareToTencentWeibo extends AbstractBaseActivity implements
        Button.OnClickListener {
    private static final String TAG = "ShareToTencentWeibo";

    private static final int TEXT_MAX = 140;

    private ImageView mDisplayImage;
    private Bitmap mBitmapSrc = null;
    private EditText mInputText;

    private CharSequence mTempText;
    private int selectionStart;
    private int selectionEnd;
    private ProgressDialog dialog = null;
    private String accessToken, accessSecret;
    private String thisLarge = null, theSmall = null;
//	private MyWeiboSync weibo;

    private TencentDataHelper dataHelper;
    private List<UserInfo> userList;
    private UserInfo currentUser;
    private Context mContext;

    private SportsApp mSportsApp;
    private int tShare = 1;
    private QQWeiboProxy mQqWeiboProxy;

    private String mUserId = "";

    private String clientIP = "210.22.149.18";
    private int shaerTengXunId = -1;//分享记录ID
    private ImageDownloader mDownloader = null;
    private String urls = null;

    public String getIpAddress() {
        String ip;
        WifiManager wifiManager = (WifiManager) mSportsApp.getSystemService(Context.WIFI_SERVICE);
        //判断wifi是否开启
        if (!wifiManager.isWifiEnabled()) {
            ip = getLocalIpAddress();
            Log.v(TAG, "GPRS ip is " + ip);
        } else {
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            int ipAddress = wifiInfo.getIpAddress();
            ip = intToIp(ipAddress);
            Log.v(TAG, "WIFI ip is " + ip);
        }
        return ip;
    }

    private String intToIp(int i) {

        return (i & 0xFF) + "." +
                ((i >> 8) & 0xFF) + "." +
                ((i >> 16) & 0xFF) + "." +
                (i >> 24 & 0xFF);
    }

    public String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
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

    private void initRes() {
        mDisplayImage = (ImageView) findViewById(R.id.share_image);
        findViewById(R.id.button_sendto).setOnClickListener(this);
//		TextView titleText = (TextView) findViewById(R.id.title_text);
//		titleText.setText(getString(R.string.share_to)
//				+ getString(R.string.text_tengxun_weibo));

        mInputText = (EditText) findViewById(R.id.shareto_edittext);
        //mInputText.setText(thisMessage);
        mInputText.addTextChangedListener(mTextWatcher);
    }

    TextWatcher mTextWatcher = new TextWatcher() {
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
            mTempText = s;
        }

        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
        }

        ;

        public void afterTextChanged(Editable s) {
            selectionStart = mInputText.getSelectionStart();
            selectionEnd = mInputText.getSelectionEnd();
            Log.i("gongbiao1", "" + selectionStart);
            if (mTempText.length() > TEXT_MAX) {
                Toast.makeText(ShareToTencentWeibo.this,
                        R.string.edit_content_limit, Toast.LENGTH_SHORT).show();
                s.delete(selectionStart - 1, selectionEnd);
                int tempSelection = selectionStart;
                mInputText.setText(s);
                mInputText.setSelection(tempSelection);
            }

        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_sendto:
                SendIamgeAndText();
                return;
        }

    }

    private void SendIamgeAndText() {
        if (!InfoHelper.checkNetWork(mContext)) {
            Toast.makeText(mContext, getString(R.string.acess_server_error),
                    Toast.LENGTH_LONG).show();
        } else {
            // if( isChecked() )
            {
                dialog.show();

                Thread thread = new Thread(new UpdateStatusThread());
                thread.start();
            }
        }

    }

    Handler handle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (dialog != null) {
                dialog.dismiss();
            }

            if (msg.what > 0) {
                if (mSportsApp.isLogin()
                        && mSportsApp.getSessionId() != null
                        && (!"".endsWith(mSportsApp.getSessionId()))) {
                    Toast.makeText(mContext,
//							getString(R.string.shared_success_add_coins),
                            getString(R.string.shared_success),
                            Toast.LENGTH_SHORT).show();
                    MobclickAgent.onEvent(mContext, "shareto", "tencent");
                    StatCounts.ReportSocialShareInfo(mContext, FunctionStatic.getGameId(mContext),
                            "", mUserId, "", "");
                    /*final String time = mSportsApp
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
                            switch (msg.what) {
                                case ApiConstant.COINS_SUCCESS:
                                    SportTaskUtil.jump2CoinsDialog(ShareToTencentWeibo.this, getString(R.string.shared_success_add_coins));
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
                thisLarge = null;
                mInputText.setText("");
                returnToHome("success");
            } else {
                Toast.makeText(mContext, getString(R.string.shared_failed),
                        Toast.LENGTH_SHORT).show();
            }
        }
    };

    private void returnToHome(String status) {
//		Intent intent=new Intent();
//		intent.putExtra("status", status);
//		setResult(RESULT_OK, intent);
        finish();
    }

    Handler endSessionHandle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            setResult(RESULT_OK, new Intent());
            finish();
        }
    };

    class UpdateStatusThread implements Runnable {
        public void run() {
            int what = -1;
            String msg = mInputText.getText().toString();
            msg = !TextUtils.isEmpty(msg) ? msg : "云狐运动";
            Log.e("share ", "msg is: " + msg.toString());
            String returnStr = null;
            if (thisLarge != null) {
//				returnStr = mQqWeiboProxy.sendPicWeibo(msg, thisLarge);
                returnStr = mQqWeiboProxy.sendPicWeibo(msg, urls);
            }
            if (returnStr != null) {
                Log.e(TAG, "returnStr is " + returnStr);
                try {
                    JSONObject dataObj = new JSONObject(returnStr);
                    if ("ok".equals(dataObj.getString("msg"))) {
                        what = 1;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    what = -1;
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("WeiboPub", "ERROR" + e.getCause());
                }
            } else {
                what = -1;
            }
            handle.sendEmptyMessage(what);

        }
    }

    class EndSessionThread implements Runnable {
        public void run() {
            TencentDataHelper dataHelper = new TencentDataHelper(mContext);
            dataHelper.Close();
            endSessionHandle.sendEmptyMessage(201);
        }
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getRepeatCount() > 0
                && event.getKeyCode() == KeyEvent.KEYCODE_MENU) {
            return true;
        }
        return super.dispatchKeyEvent(event);

    }


    @Override
    public void initIntentParam(Intent intent) {
        title = getResources().getString(R.string.text_tengxun_weibo);
    }


    @Override
    public void initView() {
        showContentView(R.layout.sports_shareto_weibo);
        mContext = getApplicationContext();
        mSportsApp = (SportsApp) getApplication();
        mQqWeiboProxy = QQWeiboProxy.getInstance();
        TencentDataHelper tencent_dataHelper = new TencentDataHelper(this);
        List<UserInfo> TuserInfoList = tencent_dataHelper.GetUserList(true);
        if (!TuserInfoList.isEmpty()) {
            String token = TuserInfoList.get(0).getToken();
            String openID = TuserInfoList.get(0).getOpenID();
            String openKey = TuserInfoList.get(0).getOpenKey();
            Log.d(TAG, "token is " + token);
            Log.d(TAG, "openID is " + openID);
            Log.d(TAG, "openKey is " + openKey);
            mQqWeiboProxy.setAccesToakenString(TuserInfoList.get(0).getToken());
            mQqWeiboProxy.setExpireIn(TuserInfoList.get(0).getExpiresIn());
            mQqWeiboProxy.setOpenid(TuserInfoList.get(0).getOpenID());
            mQqWeiboProxy.setOpenKey(TuserInfoList.get(0).getOpenKey());
            mUserId = TuserInfoList.get(0).getUserId();
        }
        tencent_dataHelper.Close();
        dialog = new ProgressDialog(this);
        dialog.setMessage(getString(R.string.sharing));
        dialog.setIndeterminate(false);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(false);
        Log.d(TAG, "uri" + this.getIntent().getData());

        initRes();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            thisLarge = bundle.containsKey("thisLarge") ? bundle
                    .getString("thisLarge") : "";
            //thisMessage = bundle.containsKey("thisMessage") ? bundle
            //		.getString("thisMessage") : "";
            accessToken = bundle.containsKey("accessToken") ? bundle
                    .getString("accessToken") : "";
            accessSecret = bundle.containsKey("accessSecret") ? bundle
                    .getString("accessSecret") : "";
            Log.d(TAG, "thisLarge" + thisLarge);
            //Log.d(TAG, "thisMessage" + thisMessage);
            Log.d(TAG, "accessSecret" + accessSecret);

            shaerTengXunId = bundle.containsKey("shaerTengXunId") ? bundle
                    .getInt("shaerTengXunId") : -1;
            if (shaerTengXunId > 0) {
                new AsyncTask<Void, Void, String>() {

                    @Override
                    protected String doInBackground(Void... params) {
                        // TODO Auto-generated method stub
                        String imagUrl = ApiJsonParser.getShareIcon(mSportsApp.getSportUser().getUid(), shaerTengXunId);
                        return imagUrl;
                    }

                    protected void onPostExecute(String result) {
                        if (result != null) {
                            Log.i("shaerTengXunId", result);
                            mDownloader = new ImageDownloader(ShareToTencentWeibo.this);
                            mDownloader.setType(ImageDownloader.ICON);
                            mDownloader.download(result, mDisplayImage, null);
                            urls = result;

                        } else {
                            Toast.makeText(ShareToTencentWeibo.this,
                                    getString(R.string.sports_comment_neterror),
                                    Toast.LENGTH_LONG).show();
                        }

                    }

                    ;

                }.execute();
            } else {
                Toast.makeText(ShareToTencentWeibo.this,
                        getString(R.string.sports_comment_neterror),
                        Toast.LENGTH_LONG).show();
            }
        }
//		mBitmapSrc = SportsUtilities.decodeFile(thisLarge);
//		mDisplayImage.setImageBitmap(mBitmapSrc);
        clientIP = getIpAddress();
        Log.v(TAG, "clientIP is " + clientIP);
    }


    @Override
    public void setViewStatus() {
        // TODO Auto-generated method stub

    }


    @Override
    public void onPageResume() {
        MobclickAgent.onPageStart("ShareToTencentWeibo");
    }


    @Override
    public void onPagePause() {
        if (dialog != null) {
            dialog.dismiss();
        }
        MobclickAgent.onPageEnd("ShareToTencentWeibo");
    }


    @Override
    public void onPageDestroy() {
        // TODO Auto-generated method stub

    }
}
