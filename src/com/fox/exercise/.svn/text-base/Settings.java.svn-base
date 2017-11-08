package com.fox.exercise;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

import com.fox.exercise.login.TencentAuthorizeActivity;
import com.fox.exercise.weibo.renren.AccessData;
import com.fox.exercise.weibo.sina.AccessInfo;
import com.fox.exercise.weibo.sina.AccessInfoHelper;
import com.fox.exercise.weibo.sina.AuthoSharePreference;
import com.fox.exercise.weibo.sina.InfoHelper;
import com.fox.exercise.weibo.sina.WeiboConstParam;
import com.fox.exercise.weibo.sina.oauth2.AccessToken;
import com.fox.exercise.weibo.sina.oauth2.AsyncWeiboRunner;
import com.fox.exercise.weibo.sina.oauth2.AsyncWeiboRunner.RequestListener;
import com.fox.exercise.weibo.sina.oauth2.DialogError;
import com.fox.exercise.weibo.sina.oauth2.Token;
import com.fox.exercise.weibo.sina.oauth2.Utility;
import com.fox.exercise.weibo.sina.oauth2.Weibo;
import com.fox.exercise.weibo.sina.oauth2.WeiboDialogListener;
import com.fox.exercise.weibo.sina.oauth2.WeiboException;
import com.fox.exercise.weibo.sina.oauth2.WeiboParameters;
import com.fox.exercise.weibo.tencent.DataBaseContext;
import com.fox.exercise.weibo.tencent.TencentDataHelper;
import com.fox.exercise.weibo.tencent.UserInfo;
import com.fox.exercise.weibo.tencent.proxy.QQWeiboProxy;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.ingenic.indroidsync.SportsApp;

public class Settings extends PreferenceActivity implements Preference.OnPreferenceChangeListener {
    public final static String BAIDU_TOKEN = "BAIDU_TOKEN";
    public final static String BAIDU_NICK = "BAIDU_NICK";
    final static String HUABAN_NICK = "HUABAN_NICK";
    final static String QQ_TOKEN = "QQ_TOKEN";
    final static String QQ_NICK = "QQ_NICK";
    final static String QQ_OPID = "QQ_OPID";
    public static final String KEY_TENGXUN_WEIBO = "tengxun_weio";
    public static final String KEY_XINLANG_WEIBO = "xinlang_weibo";
    private static final String KEY_RENREN_SHARE = "renren_share";
    private static final String KEY_QQ_ZONE = "qq_zone";
    private static final String KEY_BAIDU_PCS = "baidu_pcs";
    private static final String KEY_HUABAN_SHARE = "huaban_share";
    protected static final String TAG = "Settings";

    private Dialog loginPregressDialog = null;
    public static final int QQ_OAUTH_FINISH = 11;
    private static final int TENCENT = 0x0001;

    // PreferenceScreen mVersion;
    PreferenceScreen mTengxun;
    PreferenceScreen mXinlang;
    // PreferenceScreen mRenren;
    PreferenceScreen mBaidu;
    PreferenceScreen mHuaban;
    //static PreferenceScreen mQQ;

    protected Context mContext;
    private BroadcastReceiver mNetworkStateIntentReceiver;
    private boolean mNoNetwork = false;
    private TencentDataHelper tencent_dataHelper;
    private AccessInfo accessInfo = null;
    private String mTengxunUserId = null;
    private String mTengxunNick = null;
    private String mXinlangUserId = null;
    private String mXinlangNick = null;
    // private String mRenrenUserID = null;
    // private String mRenrenNick = null;
    /*static String mQQToken = null;
    static String mQQOpid = null;
	static String mQQNick = null*/;

    private AlertDialog mDialog;
    private String mCurTitleForDialog;
    private final static String callBackUrl = "weiboandroid://Settings";
    private String user_name = null;
    private SportsApp mSportsApp;

    private static final int REFRESH = 0x0001;
    private SettingHandler mHandler = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boolean findMethod = SmartBarUtils.findActionBarTabsShowAtBottom();
    /*	if (!findMethod) {
		    // 取消ActionBar拆分，换用TabHost
		    getWindow().setUiOptions(0);
		     getWindow().requestFeature(android.view.Window.FEATURE_NO_TITLE);
		}*/
        addPreferencesFromResource(R.xml.sports_settings);
        mSportsApp = (SportsApp) getApplication();
        // getPreferenceScreen().removePreference(findPreference("bind_weibo"));
        mContext = this;

        mHandler = new SettingHandler();
        initWeiboInfo();
        initPreference();
        if (findMethod) {
            getActionBar().setDisplayShowHomeEnabled(false);
            getActionBar().setDisplayShowTitleEnabled(false);
            SmartBarUtils.setActionModeHeaderHidden(getActionBar(), true);
            SmartBarUtils.setActionBarViewCollapsable(getActionBar(), true);
        }
        // PreferenceScreen p = (PreferenceScreen) findPreference("parent");
        // PreferenceCategory pp = (PreferenceCategory)
        // findPreference("bind_pcs");
        // p.removePreference(pp);
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter mNetworkStateChangedFilter = new IntentFilter();
        mNetworkStateChangedFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);

        mNetworkStateIntentReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
                    mNoNetwork = intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false);
                }
            }
        };
        registerReceiver(mNetworkStateIntentReceiver, mNetworkStateChangedFilter);


		/* mQQNick =
		 PreferenceManager.getDefaultSharedPreferences(this).getString(QQ_NICK,
		 null);
		mQQNick = getSharedPreferences(AllWeiboInfo.TENCENT_QQZONE_TOKEN_SP, MODE_PRIVATE).getString(
				AllWeiboInfo.TENCENT_QQZONE_NICK_KEY, null);
		mQQToken = PreferenceManager.getDefaultSharedPreferences(this).getString(QQ_TOKEN, null);
		mQQOpid = PreferenceManager.getDefaultSharedPreferences(this).getString(QQ_OPID, null);
	*/
        updateWeiboSummary();
        MobclickAgent.onPageStart("Settings");
        MobclickAgent.onResume(mContext);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mNetworkStateIntentReceiver);
        MobclickAgent.onPageEnd("Settings");
        MobclickAgent.onPause(mContext);
    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy invoked");
        super.onDestroy();
    }

    class SettingHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case REFRESH:
                    updateWeiboSummary();
                    break;
            }
        }

    }

    private void initPreference() {
        mTengxun = (PreferenceScreen) findPreference(KEY_TENGXUN_WEIBO);
        mXinlang = (PreferenceScreen) findPreference(KEY_XINLANG_WEIBO);
        // mRenren = (PreferenceScreen) findPreference(KEY_RENREN_SHARE);
        mHuaban = (PreferenceScreen) findPreference(KEY_HUABAN_SHARE);
        mBaidu = (PreferenceScreen) findPreference(KEY_BAIDU_PCS);
        //mQQ = (PreferenceScreen) findPreference(KEY_QQ_ZONE);
        // mVersion = (PreferenceScreen) findPreference(KEY_VERSION);
        // PackageManager pm = getPackageManager();
        // try {
        // PackageInfo pi = pm.getPackageInfo(getPackageName(), 0);
        // String versionName = pi.versionName;
        // mVersion.setSummary(versionName);
        // } catch (NameNotFoundException e) {
        // // TODO Auto-generated catch block
        // e.printStackTrace();
        // }
    }

    private void updateWeiboSummary() {
        if (mTengxunNick != null) {
            String summary = getString(R.string.summary_weibo_bound_already) + mTengxunNick
                    + getString(R.string.summary_confirm_to_change);
            mTengxun.setSummary(summary);
        } else {
            mTengxun.setSummary(R.string.confirm_to_binding_tengxun);
        }

        if (mXinlangNick != null) {
            String summary = getString(R.string.summary_weibo_bound_already) + mXinlangNick
                    + getString(R.string.summary_confirm_to_change);
            mXinlang.setSummary(summary);
        } else {
            mXinlang.setSummary(R.string.confirm_to_binding_xinlang);
        }
		/*if (mQQNick != null) {
			String summary = getString(R.string.summary_weibo_bound_already) + mQQNick
					+ getString(R.string.summary_confirm_to_change);
			mQQ.setSummary(summary);
		} else {
			mQQ.setSummary(R.string.confirm_to_binding_qq);
		}*/
    }

    private void initWeiboInfo() {
        tencent_dataHelper = DataBaseContext.getInstance(getApplicationContext());


        // httpOauthConsumer = new
        // CommonsHttpOAuthConsumer(getString(R.string.app_sina_consumer_key),
        // getString(R.string.app_sina_consumer_secret));
        //
        // httpOauthprovider = new DefaultOAuthProvider(
        // "http://api.t.sina.com.cn/oauth/request_token",
        // "http://api.t.sina.com.cn/oauth/access_token",
        // "http://api.t.sina.com.cn/oauth/authorize");

        List<UserInfo> TuserInfoList = tencent_dataHelper.GetUserList(true);
        if (!TuserInfoList.isEmpty()) {
            mTengxunUserId = TuserInfoList.get(0).getUserId();
            mTengxunNick = TuserInfoList.get(0).getUserNick();
            Log.d(TAG, "mTengxunUserId :" + mTengxunUserId);
            Log.d(TAG, "mTengxunNick :" + mTengxunNick);
        }

        accessInfo = InfoHelper.getAccessInfo(mContext);
        Log.d(TAG, "accessInfo " + accessInfo);
        if (accessInfo != null) {
            mXinlangUserId = accessInfo.getUserID();
            mXinlangNick = accessInfo.getNickName();
            Log.d(TAG, "mXinlangNick :" + mXinlangNick);
        }

        // List<UserData> renrenUserList = renren_dataHelper.GetUserList();
        // if (!renrenUserList.isEmpty()) {
        // mRenrenUserID = renrenUserList.get(0).getUserId();
        // mRenrenNick = renrenUserList.get(0).getUserName();
        // Log.d(TAG, "mRenrenNick :" + mRenrenNick);
        // }


    }


    public void qqPCSLogin() {
        // new QQGetUserInfo(mContext);
        // QQGetUserInfo.userInfo();
        bindQQ();
    }

    private void bindQQ() {
        Intent intent = new Intent(Settings.this, QQOAuthActivity.class);
        startActivityForResult(intent, QQ_OAUTH_FINISH);
    }


    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        return false;
    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {

        if (preference == findPreference(KEY_TENGXUN_WEIBO)) {
            if (mTengxunNick != null) {
                gotoUpdateInfo(KEY_TENGXUN_WEIBO);
            } else {
                if (mNoNetwork) {
                    Toast tip = Toast.makeText(this, R.string.error_cannot_access_net, Toast.LENGTH_SHORT);
                    tip.show();
                } else {
                    gotoTenxunWeibo();
                }
            }

        } else if (preference == findPreference(KEY_XINLANG_WEIBO)) {
            if (mXinlangNick != null) {
                gotoUpdateInfo(KEY_XINLANG_WEIBO);
            } else {
                if (mNoNetwork) {
                    Toast tip = Toast.makeText(this, R.string.error_cannot_access_net, Toast.LENGTH_SHORT);
                    tip.show();
                } else {
                    gotoXinlangWeibo();
                }
            }

        }

		/* else if (preference == findPreference(KEY_QQ_ZONE)) {
			if (mQQNick != null) {
				gotoUpdateInfo(KEY_QQ_ZONE);
			} else {
				if (mNoNetwork) {
					Toast tip = Toast.makeText(this, R.string.error_cannot_access_net, 5);
					tip.show();
				} else {
					qqPCSLogin();
				}
			}
		}*/
        return super.onPreferenceTreeClick(preferenceScreen, preference);

    }

    private void gotoUpdateInfo(String curTitle) {
        mCurTitleForDialog = curTitle;
        mDialog = new AlertDialog.Builder(this)
                .setTitle(getString(R.string.setting_weibo_select_title))
                .setItems(
                        new String[]{getString(R.string.setting_weibo_select_change),
                                getString(R.string.setting_weibo_select_delete)}, onselect)
                .setNegativeButton(getString(R.string.button_cancel), null).show();

    }

    OnClickListener onselect = new OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            // TODO Auto-generated method stub
            switch (which) {
                case 0: {
                    mDialog.dismiss();
                    if (mCurTitleForDialog.equals(KEY_TENGXUN_WEIBO)) {
                        gotoTenxunWeibo();
                    } else if (mCurTitleForDialog.equals(KEY_XINLANG_WEIBO)) {
                        gotoXinlangWeibo();
                    } else if (mCurTitleForDialog.equals(KEY_QQ_ZONE)) {
                        qqPCSLogin();
                    }
                    break;
                }
                case 1: {
                    if (mCurTitleForDialog.equals(KEY_TENGXUN_WEIBO)) {
                        TencentDataHelper tencent_dataHelper = new TencentDataHelper(mContext);
                        tencent_dataHelper.DelUserInfo(mTengxunUserId);
                        tencent_dataHelper.delete();
                        tencent_dataHelper.Close();
                        mTengxunNick = null;
                    } else if (mCurTitleForDialog.equals(KEY_XINLANG_WEIBO)) {
                        AccessInfoHelper accessDBHelper = new AccessInfoHelper(mContext);
                        accessDBHelper.open();
                        accessDBHelper.delete();
                        accessDBHelper.close();
                        mXinlangNick = null;
                    }
				/*else if (mCurTitleForDialog.equals(KEY_QQ_ZONE)) {
					mQQToken = null;
					mQQNick = null;
					getSharedPreferences(AllWeiboInfo.TENCENT_QQZONE_TOKEN_SP, MODE_PRIVATE).edit().clear().commit();
					AllWeiboInfo.TENCENT_QQZONE_NICK = "";
					AllWeiboInfo.TENCENT_QQZONE_TOKEN = "";
					AllWeiboInfo.TENCENT_QQZONE_OPEN_ID = "";
				}*/
                    updateWeiboSummary();
                    mDialog.dismiss();
                    break;
                }
            }
        }
    };

    private void gotoTenxunWeibo() {

        Intent intent = new Intent();
        intent.setClass(mContext, TencentAuthorizeActivity.class);
        startActivityForResult(intent, TENCENT);

    }

    private void gotoXinlangWeibo() {
        // Oauth2.0
        // 隐式授权认证方式
        Weibo weibo = Weibo.getInstance();
        weibo.setupConsumerConfig(WeiboConstParam.CONSUMER_KEY, WeiboConstParam.CONSUMER_SECRET);

        // 此处回调页内容应该替换为与appkey对应的应用回调页
        weibo.setRedirectUrl(WeiboConstParam.REDIRECT_URL);

        // 启动认证
        weibo.authorize(Settings.this, new AuthDialogListener());
        // try {
        // String authUrl =
        // httpOauthprovider.retrieveRequestToken(httpOauthConsumer,
        // callBackUrl);
        // Log.d("authUrl","authUrl"+authUrl);
        // Intent intent = new Intent();
        // Bundle bundle = new Bundle();
        // bundle.putString("url", authUrl);
        // intent.putExtras(bundle);
        // intent.setClass(Settings.this , XinlangWebViewActivity.class);
        // startActivity(intent);
        //
        // } catch (Exception e) {
        //
        // }
    }


    // private void updateQQ(){
    // final int REQUEST_PICK_PICTURE = 1001;
    // final String CALLBACK = "auth://tauth.qq.com/";
    //
    // String scope =
    // "get_user_info,get_user_profile,add_share,add_topic,list_album,upload_pic,add_album";//授权范围
    // AuthReceiver receiver;
    //
    // receiver = new AuthReceiver();
    // IntentFilter filter = new IntentFilter();
    // filter.addAction(TencentOpenHost.AUTH_BROADCAST);
    // registerReceiver(receiver, filter);
    //
    // TencentOpenAPI2.logIn(getApplicationContext(), mOpenId, scope, mAppid,
    // "_self", CALLBACK, null, null);
    //
    //
    // }
    //
    // public class AuthReceiver extends BroadcastReceiver {
    //
    // private static final String TAG="AuthReceiver";
    //
    // @Override
    // public void onReceive(Context context, Intent intent) {
    // Bundle exts = intent.getExtras();
    // String raw = exts.getString("raw");
    // String access_token = exts.getString(TencentOpenHost.ACCESS_TOKEN);
    // String expires_in = exts.getString(TencentOpenHost.EXPIRES_IN);
    // String error_ret = exts.getString(TencentOpenHost.ERROR_RET);
    // String error_des = exts.getString(TencentOpenHost.ERROR_DES);
    // Log.i(TAG, String.format("raw: %s, access_token:%s, expires_in:%s", raw,
    // access_token, expires_in));
    //
    // if (access_token != null) {
    // mAccessToken = access_token;
    // // ((TextView)findViewById(R.id.access_token)).setText(access_token);
    // // if(!isFinishing())
    // // {
    // // showDialog(PROGRESS);
    // // }
    //
    // TencentOpenAPI.openid(access_token, new Callback() {
    //
    // public void onCancel(int flag)
    // {
    //
    // }
    //
    // public void onSuccess(final Object obj) {
    // runOnUiThread(new Runnable() {
    // @Override
    // public void run() {
    // mOpenId = ((OpenId)obj).getOpenId();
    // }
    // });
    // }
    // public void onFail(int ret, final String msg) {
    // runOnUiThread(new Runnable() {
    // @Override
    // public void run() {
    // // dismissDialog(PROGRESS);
    // // TDebug.msg(msg, getApplicationContext());
    // }
    // });
    // }
    // });
    // }
    // if (error_ret != null) {
    // // ((TextView)findViewById(R.id.access_token)).setText("获取access token失败"
    // + "\n错误码: " + error_ret + "\n错误信息: " + error_des);
    // }
    // TencentOpenAPI.userInfo(mAccessToken, mAppid, mOpenId, new Callback() {
    //
    // public void onSuccess(final Object obj) {
    // String s = obj.toString();
    // String[] ss = s.split("nickname");
    // mQQNick = ss[1].substring(2,ss[1].indexOf("\n"));
    // }
    //
    //
    // public void onFail(final int ret, final String msg) {
    // }
    //
    //
    // public void onCancel(int flag) {
    // }
    // });
    // }
    //
    //
    // }

    // private void updateUserInfo() {
    // String url = "http://api.renren.com/restserver.do";
    // String signature = getParams();
    // String method = "users.getInfo";
    // String v2 = "1.0";
    // // File upload = new File(thisLarge);
    // String access_token = AccessData.access_token;
    // String format = "JSON";
    // TreeMap<String, String> params = new TreeMap<String, String>();
    // params.put("sig", signature);
    // params.put("method", method);
    // params.put("v", v2);
    // params.put("access_token", access_token);
    // params.put("format", format);
    // String content = HttpURLUtils.doPost(url, params);
    // Log.d(TAG, "content = " + content);
    // try {
    // // JSONObject jb = new JSONObject(content);
    // JSONArray ja = new JSONArray(content);
    // JSONObject jo = ja.getJSONObject(0);
    // String uid = jo.getString("uid");
    // String userName = jo.getString("name");
    // String tinyurl = jo.getString("tinyurl");
    // String headurl = jo.getString("headurl");
    // Log.d("ren ren", "uid:" + uid);
    // Log.d("ren ren", "name:" + userName);
    // Log.d("ren ren", "tinyurl:" + tinyurl);
    // UserData userdata = new UserData();
    // // userdata.setId(uid);
    // userdata.setUserId(uid);
    // userdata.setUserName(userName);
    // userdata.setTinyurl(tinyurl);
    // userdata.setHeadurl(headurl);
    // userdata.setToken(access_token);
    // userdata.setSignature(signature);
    // mRenrenNick = userName;
    //
    // if (renren_dataHelper.HaveUserInfo(uid)) {
    // renren_dataHelper.UpdateUserInfo(userdata);
    // } else {
    // renren_dataHelper.SaveUserInfo(userdata);
    // }
    //
    // } catch (JSONException e) {
    // e.printStackTrace();
    //
    // }
    // }

    @SuppressWarnings("unchecked")
    public String getParams() {
        List<String> params = new ArrayList<String>();
        String method = "users.getInfo";
        String v1 = "1.0";
        String access_token = AccessData.access_token;
        String format = "JSON";
        params.add("method=" + method);
        params.add("v=" + v1);
        params.add("access_token=" + access_token);
        params.add("format=" + format);
        return getSignature(params, AccessData.RENREN_SECRET);
    }

    public static String getSignature(List<String> paramList, String secret) {
        Collections.sort(paramList);

        StringBuffer buffer = new StringBuffer();
        for (String param : paramList) {
            buffer.append(param);
        }

        buffer.append(secret);
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            StringBuffer result = new StringBuffer();
            try {
                for (byte b : md.digest(buffer.toString().getBytes("UTF-8"))) {
                    result.append(Integer.toHexString((b & 0xf0) >>> 4));
                    result.append(Integer.toHexString(b & 0x0f));
                }
            } catch (UnsupportedEncodingException e) {
                for (byte b : md.digest(buffer.toString().getBytes())) {
                    result.append(Integer.toHexString((b & 0xf0) >>> 4));
                    result.append(Integer.toHexString(b & 0x0f));
                }
            }

            return result.toString();
        } catch (java.security.NoSuchAlgorithmException ex) {

        }

        return null;
    }

    // @Override
    // protected void onNewIntent(Intent intent)
    // {
    // super.onNewIntent(intent);
    // Log.d(TAG,"on newIntent invoked");
    // Uri uri = intent.getData();
    // if(uri==null)
    // {
    // return;
    // }
    //
    // String verifier =
    // uri.getQueryParameter(oauth.signpost.OAuth.OAUTH_VERIFIER);
    //
    // try
    // {
    // httpOauthprovider.setOAuth10a(true);
    // httpOauthprovider.retrieveAccessToken(httpOauthConsumer,verifier);
    // }
    // catch (OAuthMessageSignerException ex) {
    // ex.printStackTrace();
    // }
    // catch (OAuthNotAuthorizedException ex) {
    // ex.printStackTrace();
    // }
    // catch (OAuthExpectationFailedException ex) {
    // ex.printStackTrace();
    // }
    // catch (OAuthCommunicationException ex) {
    // ex.printStackTrace();
    // }
    //
    // SortedSet<String> userInfoSet =
    // httpOauthprovider.getResponseParameters().get("user_id");
    // SortedSet<String> nameSet =
    // httpOauthprovider.getResponseParameters().get("screen_name");
    // Log.v(TAG, "nameSet ="+nameSet);
    // if(userInfoSet!=null&&!userInfoSet.isEmpty())
    // {
    // String userID = userInfoSet.first();
    // String accessToken = httpOauthConsumer.getToken();
    // String accessSecret = httpOauthConsumer.getTokenSecret();
    //
    // mXinlangUserId = userID;
    //
    // System.setProperty("weibo4j.oauth.consumerKey",
    // getString(R.string.app_sina_consumer_key));
    // System.setProperty("weibo4j.oauth.consumerSecret",
    // getString(R.string.app_sina_consumer_secret));
    // Weibo weibo = new Weibo();
    // weibo.setToken(accessToken, accessSecret);
    // String nickName = null;
    // try {
    // User user = weibo.showUser(userID);
    // nickName = user.getScreenName();
    // mXinlangNick = nickName;
    // Log.v("settings"," nickName is "+nickName);
    // } catch (WeiboException e) {
    // e.printStackTrace();
    // }
    // if(accessInfo!=null){
    // AccessInfoHelper accessDBHelper = new AccessInfoHelper(mContext);
    // accessDBHelper.open();
    // accessDBHelper.delete();
    // accessDBHelper.close();
    // }
    //
    // AccessInfo accessInfo = new AccessInfo();
    // accessInfo.setUserID(userID);
    // accessInfo.setAccessToken(accessToken);
    // accessInfo.setAccessSecret(accessSecret);
    // accessInfo.setNickName(nickName);
    //
    // AccessInfoHelper accessDBHelper = new AccessInfoHelper(mContext);
    // accessDBHelper.open();
    // accessDBHelper.create(accessInfo);
    // accessDBHelper.close();
    //
    // XinlangWebViewActivity.webInstance.finish();
    // updateWeiboSummary();
    // }
    // }
    class AuthDialogListener implements WeiboDialogListener, RequestListener {

        private String uid = "";
        private String tokenString = "";

        @Override
        public void onComplete(Bundle values) {

            String token = values.getString("access_token");

            AuthoSharePreference.putToken(Settings.this, token);

            tokenString = AuthoSharePreference.getToken(Settings.this);

            AccessToken accessToken = new AccessToken(tokenString, WeiboConstParam.CONSUMER_SECRET);
            Log.d(TAG, "accessToken:" + accessToken);
            final Weibo weibo = Weibo.getInstance();
            weibo.setAccessToken(accessToken);

            String expires_in = values.getString("expires_in");
            AuthoSharePreference.putExpires(Settings.this, expires_in);

            String remind_in = values.getString("remind_in");
            AuthoSharePreference.putRemind(Settings.this, remind_in);

            uid = values.getString("uid");
            Log.d(TAG, uid);
            AuthoSharePreference.putUid(Settings.this, uid);
            // update(weibo, Weibo.getAppKey(), msg, "", "");
            new AsyncTask<Void, Void, Void>() {

                @Override
                protected Void doInBackground(Void... params) {
                    retrieveSinaUserInfo(weibo, Weibo.getAppKey(), weibo.getAccessToken(), uid);
                    return null;
                }

            }.execute();
            // goShareActivity();
        }

        private void retrieveSinaUserInfo(Weibo weibo, String source, Token token, String uid) {
            WeiboParameters bundle = new WeiboParameters();
            Log.d(TAG, "token.toString():" + token.getRefreshToken());
            Log.d(TAG, "token.toString():" + token.getToken());
            // try {
            // Log.d(TAG,"token.toString():"+weibo.generateAccessToken(getApplicationContext(),weibo.getRequestToken(mContext,
            // Weibo.getAppKey(), Weibo.getAppSecret(),
            // weibo.getRedirectUrl())));
            // } catch (WeiboException e) {
            // // TODO Auto-generated catch block
            // e.printStackTrace();
            // }

            // bundle.add("source", source);
            bundle.add("access_token", token.getToken());
            bundle.add("uid", uid);

            String url = Weibo.SERVER + "users/show.json";
            AsyncWeiboRunner weiboRunner = new AsyncWeiboRunner(weibo);
            weiboRunner.request(getApplicationContext(), url, bundle, Utility.HTTPMETHOD_GET, this);
        }

        @Override
        public void onComplete(String response) {
            Log.d(TAG, "response:" + response.toString());
            try {
                JSONObject js = new JSONObject(response);
                user_name = js.getString("screen_name");
                mXinlangNick = user_name;
                Log.d(TAG, "user_name" + user_name);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            AccessInfo accessInfo = new AccessInfo();
            accessInfo.setUserID(uid);
            accessInfo.setAccessToken(tokenString);
            accessInfo.setAccessSecret(WeiboConstParam.CONSUMER_SECRET);
            accessInfo.setNickName(user_name);

            AccessInfoHelper accessDBHelper = new AccessInfoHelper(mContext);
            accessDBHelper.open();
            accessDBHelper.clear();
            accessDBHelper.create(accessInfo);
            accessDBHelper.close();

            Message.obtain(mHandler, REFRESH).sendToTarget();

        }

        @Override
        public void onIOException(IOException e) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onError(WeiboException e) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onWeiboException(WeiboException e) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onError(DialogError e) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onCancel() {
            // TODO Auto-generated method stub

        }
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getRepeatCount() > 0 && event.getKeyCode() == KeyEvent.KEYCODE_MENU) {
            return true;
        }
        return super.dispatchKeyEvent(event);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		if (resultCode == QQ_OAUTH_FINISH) {
//			bindQQTask.execute();
//		}
        switch (requestCode) {
            case TENCENT: {
                if (resultCode == RESULT_OK)
                    onResultForAuthActivity(data);
            }
            break;
        }
    }

//	private AsyncTask<Integer, Integer, String> bindQQTask = new AsyncTask<Integer, Integer, String>() {
//
//		@Override
//		protected void onPostExecute(String result) {
//			//mQQNick = AllWeiboInfo.TENCENT_QQZONE_NICK;
//			updateWeiboSummary();
//			if (loginPregressDialog != null)
//				if (loginPregressDialog.isShowing())
//					loginPregressDialog.dismiss();
//			super.onPostExecute(result);
//		}
//
//		@Override
//		protected void onPreExecute() {
//			loginPregressDialog = new Dialog(Settings.this, R.style.sports_dialog);
//			LayoutInflater mInflater = getLayoutInflater();
//			View v1 = mInflater.inflate(R.layout.sports_progressdialog, null);
//			TextView message = (TextView) v1.findViewById(R.id.message);
//			message.setText(R.string.sports_logining);
//			v1.setMinimumWidth((int) (SportsApp.ScreenWidth * 0.8));
//			loginPregressDialog.setContentView(v1);
//			loginPregressDialog.setCancelable(true);
//			loginPregressDialog.show();
//			super.onPreExecute();
//		}
//
//		@Override
//		protected String doInBackground(Integer... arg0) {
//			saveQQOpenId();
//			saveQQNick();
//			return null;
//		}
//	};

//	private void saveQQOpenId() {
//
////		 SharedPreferences sp = LoginActivity.this.getSharedPreferences(
////		 AllWeiboInfo.TENCENT_QQZONE_TOKEN_SP, MODE_PRIVATE);
////		 String token = sp
////		 .getString(AllWeiboInfo.TENCENT_QQZONE_TOKEN_KEY, null);
//		try {
//			_FakeX509TrustManager.allowAllSSL();
//			URL url = new URL("https://graph.z.qq.com/moc2/me?access_token=" + AllWeiboInfo.TENCENT_QQZONE_TOKEN);
//
//			Log.d(TAG, "url:" + url);
//
//			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//			conn.connect();
//
//			BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//			String line = "";
//			StringBuffer res = new StringBuffer();
//			while ((line = reader.readLine()) != null) {
//				res.append(line);
//			}
//
//			String s = res.toString();
//			int start = s.indexOf("openid") + "openid".length() + 1;
//			s = s.substring(start);
//
//			SharedPreferences sp = getSharedPreferences(AllWeiboInfo.TENCENT_QQZONE_TOKEN_SP, MODE_PRIVATE);
//			Editor e = sp.edit();
//			e.putString(AllWeiboInfo.TENCENT_QQZONE_OPEN_ID_KEY, s);
//			e.commit();
//
//			AllWeiboInfo.TENCENT_QQZONE_OPEN_ID = s;
//
//		} catch (MalformedURLException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//
//	}

//	private void saveQQNick() {
//		Log.d(TAG, "saveQQNick");
//		SharedPreferences sp = this.getSharedPreferences(AllWeiboInfo.TENCENT_QQZONE_TOKEN_SP, MODE_PRIVATE);
//		String token = sp.getString(AllWeiboInfo.TENCENT_QQZONE_TOKEN_KEY, null);
//
//		try {
//			_FakeX509TrustManager.allowAllSSL();
//			URL url = new URL("https://openmobile.qq.com/user/get_simple_userinfo?" + "access_token=" + token
//					+ "&oauth_consumer_key=" + AllWeiboInfo.TENCENT_APPID + "&openid="
//					+ AllWeiboInfo.TENCENT_QQZONE_OPEN_ID);
//
//			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//			conn.connect();
//
//			BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//			String line = "";
//			StringBuffer res = new StringBuffer();
//			while ((line = reader.readLine()) != null) {
//				res.append(line);
//			}
//
//			Log.d(TAG, "saveQQNick res:" + res.toString());
//
//			JSONObject object = new JSONObject(res.toString());
//			String s = object.getString("nickname");
//
//			Log.d(TAG, "nickname:" + s);
//
//			Editor e = sp.edit();
//			e.putString(AllWeiboInfo.TENCENT_QQZONE_NICK_KEY, s);
//			e.commit();
//
//			AllWeiboInfo.TENCENT_QQZONE_NICK = s;
//
//		} catch (MalformedURLException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}
//	}

    private QQWeiboProxy mQqWeiboProxy;
    private String access_token;
    private String expires_in;
    private String openID;
    private String openKey;

    private void onResultForAuthActivity(Intent intent) {
        Bundle bundle = intent.getExtras();
        access_token = bundle.getString("access_token");
        expires_in = bundle.getString("expires_in");
        openID = bundle.getString("openID");
        openKey = bundle.getString("openKey");
        new AsyncTask<Void, Void, Void>() {
            String userInfo = null;

            @Override
            protected Void doInBackground(Void... params) {
                mQqWeiboProxy = QQWeiboProxy.getInstance();
                userInfo = mQqWeiboProxy.getUserInfo();
                return null;
            }

            @Override
            protected void onPreExecute() {
                if (loginPregressDialog != null && !Settings.this.isFinishing())
                    loginPregressDialog.show();
            }

            @Override
            protected void onPostExecute(Void void1) {

                try {
                    JSONObject data = new JSONObject(userInfo).getJSONObject("data");
                    if (data.getString("head") != null && !"".equals(data.getString("head"))) {
                    }
                    if (mTengxunUserId != null)
                        tencent_dataHelper.DelUserInfo(mTengxunUserId);
                    String userId = data.getString("openid");
                    String userName = data.getString("name");
                    String userNick = data.getString("nick");
                    mTengxunUserId = userId;
                    mTengxunNick = userNick;

                    UserInfo user = new UserInfo();
                    user.setUserId(userId);
                    user.setUserName(userName);
                    user.setUserNick(userNick);
                    user.setToken(access_token);
                    user.setExpiresIn(expires_in);
                    user.setOpenID(openID);
                    user.setOpenKey(openKey);

                    Log.v(TAG, "userName = " + userName);

                    if (tencent_dataHelper.HaveUserInfo(userId)) {
                        tencent_dataHelper.UpdateUserInfo(user);
                    } else {
                        tencent_dataHelper.SaveUserInfo(user);
                    }
                    updateWeiboSummary();

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.e(TAG, userInfo);
            }
        }.execute();

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