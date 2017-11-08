package com.fox.exercise.login;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.multidex.MultiDex;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fox.exercise.AllWeiboInfo;
import com.fox.exercise.CurrentTimeList;
import com.fox.exercise.FoxSportsSetting;
import com.fox.exercise.MainFragmentActivity;
import com.fox.exercise.R;
import com.fox.exercise.SmartBarUtils;
import com.fox.exercise.SportsExceptionHandler;
import com.fox.exercise.StateActivity;
import com.fox.exercise.api.ApiBack;
import com.fox.exercise.api.ApiConstant;
import com.fox.exercise.api.ApiConstant.WeiboType;
import com.fox.exercise.api.ApiJsonParser;
import com.fox.exercise.api.ApiNetException;
import com.fox.exercise.api.ApiSessionOutException;
import com.fox.exercise.api.entity.UserDetail;
import com.fox.exercise.http.FunctionStatic;
import com.fox.exercise.http.StatCounts;
import com.fox.exercise.newversion.act.PerfectMainActivity;
import com.fox.exercise.weibo.sina.AccessInfo;
import com.fox.exercise.weibo.sina.AccessInfoHelper;
import com.fox.exercise.weibo.sina.AuthoSharePreference;
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
import com.fox.exercise.weibo.tencent.TencentDataHelper;
import com.fox.exercise.weibo.tencent.UserInfo;
import com.fox.exercise.weibo.tencent.proxy.QQWeiboProxy;
import com.tencent.connect.auth.QQAuth;
import com.tencent.connect.auth.QQToken;
import com.tencent.connect.common.Constants;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cn.ingenic.indroidsync.SportsApp;

/**
 * @author loujungang 登录页面
 */
public class LoginActivity extends Activity implements View.OnClickListener,PopupWindow.OnDismissListener {
    String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    private static final String TAG = "LoginActivity";
    private EditText name;
    private EditText password;
    private TextView forget;
    private TextView trytologintext;
    private Dialog loginPregressDialog = null;
    private Dialog login2PregressDialog = null;
    private Toast toastnet = null;
    private static Context mContext;
    //	private static Context qqContext;
    private SportsApp mSportsApp;
    public static Tencent mTencent;
    public static QQAuth mQQAuth;
    public static String mAppid;
    public static int intcols;
    //	private UserInfo mInfo;
    public static boolean mIsFromMain = false;
    public static boolean mTabActivityHasStart = false;
    private int qqconfig;
    public static boolean mIsRegister = false;
    private SportsExceptionHandler mExceptionHandler = null;
    private IWXAPI api;
    private static final String WX_APP_ID = "wxbf77151c2fa30c8a";
    private static final String WX_AppSecret = "46b144b5909f0f7cac216272c1cb3df8";
    private Dialog alertDialog = null;
    private Dialog alertQQDialog = null;
    public static String user_name = "";
    private String mBirthday = "";
    private String openId = "";
    private String mSex = "";
    private static final int LOGIN_FINISH = 0x0011;
    private static final int LOGIN_FAIL = 0x0012;
    public static final int SINA_LOADING = 0x0013;
    public static final int LOADING_DIALOG = 0x0014;
    public static final int GET_LOGIN_PARAMS_FAIL = 0x0005;
    private static final int TENCENT = 0x0001;
    public static final int QQ_LOGIN_FIALED = 0x0003;
    public static final int LOGIN_FAIL_DEVICE_DISABLE = 0x0004;
    public static final int LOGIN_FAIL_OUTOF_TIME = 0x0007;
    public static final int LOGIN_FAIL_NOCHECK_WIFI_LOGIN = 0x0008;
    public static final int QQ_OAUTH_FINISH = 11;
    public static final int JDOPTION_login = 10;
    public static final String NORMAL_PATH1 = SportsApp.getContext()
            .getFilesDir().toString()
            + "/normalregist";
    public static final String NORMAL_PATH2 = Environment
            .getExternalStorageDirectory()
            + "/android/data/"
            + SportsApp.getContext().getPackageName()
            + ".zuimei"
            + "/.normalregist";

    private String access_token;
    private String expires_in;
    private String openID;
    private String openKey;
    private boolean isFristTry = true;
    public static boolean isWeiXinLogin = false;
    public static LoginActivity instance = null;
    private String nickName;
    //	private String sex;
//	private String country;
    public static String WX_CODE = "";
    public static String WX_TOKEN = "";
    public static String WX_OPENID = "";
    public boolean startPause = false;
    private int qq_toast_number = 0;
    private TimerTask device_disable_task;
    private Timer device_disable_timer;
    private String userId;// 腾讯微博用户名
    private int isFromSportMain;
    private int login_fail = 0;
    private boolean passwordisshow = true;
    private ImageView isshow_password;

    private PopupWindow myWindow = null;
    private LinearLayout myView;
    private RelativeLayout mPopMenuBack, mainLL;
    private boolean notLogin;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case LOGIN_FINISH:
//                    Toast.makeText(mContext, R.string.sports_toast_login_succes,
//                            Toast.LENGTH_SHORT).show();
                    loginFinish();
                    break;
                case LOGIN_FAIL:
                    if ("accountOrPwdError".equals((String) msg.obj)) {
//                        Toast.makeText(mContext,
//                                R.string.sports_toast_nameOrPwdError,
//                                Toast.LENGTH_SHORT).show();
                        exitPopWindow(mContext.getResources().getString(R.string.sports_toast_nameOrPwdError),1);
                    }else if ("login_fail_device_disable".equals((String) msg.obj)) {
//                        Toast.makeText(mContext,
//                                R.string.login_fail_device_disable,
//                                Toast.LENGTH_LONG).show();
                        exitPopWindow(mContext.getResources().getString(R.string.login_fail_device_disable),1);
                        //clearSaveLoginInfo();
                    }else if ("checkyour_WIFI_isconnect".equals((String) msg.obj)) {
//                        Toast.makeText(mContext,
//                                R.string.checkyour_wifi_isconnect,
//                                Toast.LENGTH_LONG).show();
                        exitPopWindow(mContext.getResources().getString(R.string.checkyour_wifi_isconnect),1);
                        //clearSaveLoginInfo();
                    }else if ("socketoutoftime".equals((String) msg.obj)) {
//                        Toast.makeText(mContext,
//                                R.string.socket_outof_time,
//                                Toast.LENGTH_LONG).show();
                        if (loginPregressDialog != null) {
                            if (loginPregressDialog.isShowing()) {
                                loginPregressDialog.dismiss();
                            }
                        }
                        exitPopWindow(mContext.getResources().getString(R.string.socket_outof_time),1);
                        //clearSaveLoginInfo();
                    } else{
//                        Toast.makeText(mContext,
//                                R.string.sports_toast_login_failed,
//                                Toast.LENGTH_SHORT).show();
                        exitPopWindow(mContext.getResources().getString(R.string.sports_toast_login_failed),1);

                    }
                    break;
                case LOGIN_FAIL_OUTOF_TIME:
//                    Toast.makeText(mContext,
//                            R.string.socket_outof_time,
//                            Toast.LENGTH_LONG).show();
//                    Intent intent_outof_time = new Intent(mContext, LoginActivity.class);
//                    intent_outof_time.putExtra("outTime",1);
//                    startActivity(intent_outof_time);
//                    finish();
                    if (loginPregressDialog != null) {
                    if (loginPregressDialog.isShowing()) {
                        loginPregressDialog.dismiss();
                    }
                }
                    exitPopWindow(mContext.getResources().getString(R.string.socket_outof_time),1);
                    break;
                case LOGIN_FAIL_NOCHECK_WIFI_LOGIN:
//                    Toast.makeText(mContext,
//                            R.string.checkyour_wifi_isconnect,
//                            Toast.LENGTH_LONG).show();
//                    Intent intent_checkWifi = new Intent(mContext,LoginActivity.class);
//                    intent_checkWifi.putExtra("outTime",1);
//                    startActivity(intent_checkWifi);
//                    finish();
                    exitPopWindow(mContext.getResources().getString(R.string.checkyour_wifi_isconnect),2);
                    break;
                case LOGIN_FAIL_DEVICE_DISABLE:
                    Log.e("develop_debug", "LoginActivity.java 197");

                    SharedPreferences sp_device_disable = getSharedPreferences("user_login_info",
                                                                                Context.MODE_PRIVATE);
                    Editor edit_device_disable = sp_device_disable.edit();
                    edit_device_disable.remove("account").commit();

                    Intent intent_device_disable = new Intent(mContext, LoginActivity.class);
                    intent_device_disable.putExtra("restart_reson", "device_disable");
                    startActivity(intent_device_disable);
                case SINA_LOADING:
                    String response = (String) msg.obj;
                    try {
                        JSONObject js = new JSONObject(response);
                        openId = "" + js.getLong("id");
                        user_name = js.getString("screen_name");
                        if ("m".equals(js.getString("gender")))
                            mSex = "man";
                        else if ("f".equals(js.getString("gender")))
                            mSex = "woman";
                        Message.obtain(mHandler, LOADING_DIALOG).sendToTarget();
                        new AsyncTask<Void, Void, ApiBack>() {
                            @Override
                            protected ApiBack doInBackground(Void... params) {
                                ApiBack back = null;
                                try {
                                    if (user_name == null || "".equals(user_name)
                                            || openId == null || "".equals(openId)) {
                                        Message.obtain(mHandler, LOADING_DIALOG)
                                                .sendToTarget();
                                        return null;
                                    }
                                    SharedPreferences sp = mContext.getSharedPreferences("UmengDeviceToken", Activity.MODE_PRIVATE);
                                    back = ApiJsonParser.combineWeibo_New(
                                            ApiConstant.WeiboType.SinaWeibo,
                                            user_name, openId, 1, sp.getString("device_token", ""));
                                } catch (ApiNetException e) {
                                    e.printStackTrace();
                                    Message.obtain(mExceptionHandler,
                                            SportsExceptionHandler.NET_ERROR)
                                            .sendToTarget();
                                }
                                return back;
                            }

                            @Override
                            protected void onPostExecute(ApiBack message) {
                                if (message == null) {
                                    if (loginPregressDialog != null
                                            && loginPregressDialog.isShowing())
                                        loginPregressDialog.dismiss();
                                    // mLayout.setVisibility(View.GONE);
                                    // mVisibility.setVisibility(View.VISIBLE);
                                    Toast.makeText(
                                            mContext,
                                            getResources().getString(
                                                    R.string.sports_server_error),
                                            Toast.LENGTH_SHORT).show();
                                } else {
                                    if (message.getFlag() == 0
                                            || message.getFlag() == 1) {
                                        if (message.getFlag() == 1) {
                                            SportsApp.mIsAdmin = true;
                                        } else {
                                            SportsApp.mIsAdmin = false;
                                        }
                                        // Log.e(TAG, "loginsuccess");
                                        mSportsApp.setSessionId(message.getMsg()
                                                .substring(7));
                                        mSportsApp.setLogin(true);
                                        Log.i("Firstload", "新浪微博第一次登陆");
                                        // 清除微博用户登陆信息，重写新浪微博用户登录信息
                                        SharedPreferences sp = getSharedPreferences(
                                                AllWeiboInfo.LOGIN_INFO,
                                                Context.MODE_PRIVATE);
                                        Editor ed = sp.edit();
                                        ed.clear();
                                        ed.putString("account", user_name);
                                        ed.putString("weibotype",
                                                ApiConstant.WeiboType.SinaWeibo);
                                        ed.putString(
                                                AllWeiboInfo.LOGIN_INFO_OPEN_ID_KEY,
                                                openId);
                                        ed.commit();
                                        // 保存用户登录身份标识
                                        SharedPreferences sps = getSharedPreferences(
                                                "user_login_info",
                                                Context.MODE_PRIVATE);
                                        Editor eds = sps.edit();
                                        eds.putInt("login_way", 1);
                                        eds.commit();
//                                        Toast.makeText(
//                                                mContext,
//                                                getResources()
//                                                        .getString(
//                                                                R.string.sports_toast_login_succes),
//                                                Toast.LENGTH_SHORT).show();
                                        loginFinish();
                                    } else if (message.getFlag() == -56) {
                                        Message msg = Message.obtain(mHandler,
                                                LoginActivity.LOGIN_FAIL_OUTOF_TIME);
                                        Bundle bundle = new Bundle();
                                        bundle.putString("msg", "新浪微博登录，请求服务器超时");
                                        msg.setData(bundle);
                                        msg.sendToTarget();
                                    } else if (message.getFlag() == -101) {
                                        Message msg = Message.obtain(mHandler,
                                                LoginActivity.LOGIN_FAIL_NOCHECK_WIFI_LOGIN);
                                        Bundle bundle = new Bundle();
                                        bundle.putString("msg", "WIFI没有认证");
                                        msg.setData(bundle);
                                        msg.sendToTarget();
                                    }  else if (message.getFlag() == -5) {
                                        // 新浪微博登录 重命名问题
                                        Toast.makeText(
                                                LoginActivity.this,
                                                getResources()
                                                        .getString(
                                                                R.string.modified_failed_message),
                                                Toast.LENGTH_SHORT).show();

                                        Intent intent = new Intent(
                                                LoginActivity.this,
                                                PerfectMainActivity.class);
                                        intent.putExtra("comeFrom", "LoginActivity");
                                        intent.putExtra("weiboType",
                                                ApiConstant.WeiboType.SinaWeibo);
                                        intent.putExtra("weiboName", user_name);
                                        intent.putExtra("token", openId);
                                        startActivityForResult(intent, 12);

                                    } else if (message.getFlag() == -10) {
                                        if (message.getMsg() != null
                                                && !"".equals(message.getMsg())) {
                                            String sessionid1 = message.getMsg()
                                                    .substring(7);
                                            // Log.e(TAG, "session_id" +
                                            // sessionid1);
                                            if (mSportsApp != null
                                                    && sessionid1 != null) {
                                                mSportsApp.setSessionId(sessionid1);
                                                Intent intent1 = new Intent(
                                                        LoginActivity.this,
                                                        PerfectMainActivity.class);
                                                intent1.putExtra("comeFrom",
                                                        "SportMain");
                                                startActivityForResult(intent1, 12);
                                            } else {
                                                mSportsApp.setSessionId("");
                                                Toast.makeText(
                                                        LoginActivity.this,
                                                        getResources()
                                                                .getString(
                                                                        R.string.sports_toast_login_failed),
                                                        Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                    } else if (-11 == message.getFlag()) {
                                        Log.e("develop_debug", "LoginActivity.java 337");
                                        if (loginPregressDialog != null
                                                && loginPregressDialog.isShowing())
                                            loginPregressDialog.dismiss();
                                        Toast.makeText(mContext,
                                                getResources().getString(R.string.login_fail_device_disable),
                                                Toast.LENGTH_LONG).show();
                                        clearSaveLoginInfo();
                                    } else {
                                        // Log.e(TAG, "loginfailed");
                                        if (message.getMsg().equals(
                                                "accountOrPwdError")) {
                                            Toast.makeText(
                                                    mContext,
                                                    getResources()
                                                            .getString(
                                                                    R.string.sports_toast_nameOrPwdError),
                                                    Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(
                                                    mContext,
                                                    getResources()
                                                            .getString(
                                                                    R.string.sports_toast_login_failed),
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                            }
                        }.execute();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 2:
//                    Toast.makeText(
//                            LoginActivity.this,
//                            getResources().getString(
//                                    R.string.sports_toast_login_succes),
//                            Toast.LENGTH_SHORT).show();
                    SharedPreferences sp = getSharedPreferences("user_login_info",
                            Context.MODE_PRIVATE);

                    Editor e = sp.edit();
                    e.putString(AllWeiboInfo.TENCENT_QQZONE_OPEN_ID_KEY,
                            AllWeiboInfo.TENCENT_QQZONE_OPEN_ID);
                    e.putString("account", AllWeiboInfo.TENCENT_QQZONE_NICK);
                    e.putString("weibotype", "qqzone");
                    e.putString("access_token",
                            AllWeiboInfo.TENCENT_QQZONE_TOKEN_URL_KEY);
                    e.putInt("login_way", 1);
                    e.commit();

                    // Log.e(TAG, "loginsuccess");
                    String sessionid = "";
                    if (msg != null) {
                        if (msg.getData().getString("msg") != null && !"".equals(msg.getData().getString("msg"))) {
                            sessionid = msg.getData().getString("msg").substring(7);
                        }
                    }
                    Log.i("Firstload", "QQ第一次登陆");
                    // Log.e(TAG, "session_id" + sessionid);
                    if (mSportsApp != null && sessionid != null) {
                        mSportsApp.setSessionId(sessionid);
                    } else {
                        mSportsApp.setSessionId("");
                    }

                    mSportsApp.setLogin(true);

                    SharedPreferences tSp = getSharedPreferences(
                            AllWeiboInfo.TENCENT_QQZONE_TOKEN_SP, MODE_PRIVATE);
                    Editor te = tSp.edit();
                    te.putString(AllWeiboInfo.TENCENT_QQZONE_TOKEN_KEY,
                            AllWeiboInfo.TENCENT_QQZONE_TOKEN);
                    te.putString(AllWeiboInfo.TENCENT_QQZONE_OPEN_ID_KEY,
                            AllWeiboInfo.TENCENT_QQZONE_OPEN_ID);
                    te.putLong(AllWeiboInfo.TENCENT_QQZONE_TOKEN_SP_TIME,
                            System.currentTimeMillis());
                    te.commit();

                    loginFinish();

                    break;
                case JDOPTION_login:
                    SharedPreferences JDsp = getSharedPreferences(
                            "user_login_info", Context.MODE_PRIVATE);

                    Editor JDe = JDsp.edit();
                    JDe.putString(AllWeiboInfo.JDOption_OPEN_ID_KEY,
                            AllWeiboInfo.JDOption_OPEN_ID);
                    JDe.putString("account", AllWeiboInfo.JDOption_NICK);
                    JDe.putString("weibotype", "jdy");
                    JDe.putInt("login_way", 1);
                    JDe.commit();

                    String jdsessionid = msg.getData().getString("msg")
                            .substring(7);
                    mSportsApp.setSessionId(jdsessionid);
                    mSportsApp.setLogin(true);

                    SharedPreferences jdtSp = getSharedPreferences(
                            AllWeiboInfo.JDOption_TOKEN_SP, MODE_PRIVATE);
                    Editor jdte = jdtSp.edit();
                    jdte.putString(AllWeiboInfo.JDOption_TOKEN_KEY,
                            AllWeiboInfo.JDOption_TOKEN);
                    jdte.putString(AllWeiboInfo.JDOption_OPEN_ID_KEY,
                            AllWeiboInfo.JDOption_OPEN_ID);
                    jdte.putLong(AllWeiboInfo.JDOption_TOKEN_SP_TIME,
                            System.currentTimeMillis());
                    jdte.commit();

                    loginFinish();

                    break;
                case LOADING_DIALOG:
                    if (loginPregressDialog == null) {
                        initLoginProgressDialog();
                    }
                    if (loginPregressDialog != null)
                        loginPregressDialog.show();
                    break;
                case QQ_LOGIN_FIALED:
                    if (loginPregressDialog != null) {
                        loginPregressDialog.dismiss();
                    }
                    Toast.makeText(mContext, (String) msg.getData().get("msg"),
                            Toast.LENGTH_LONG).show();
                    break;
                case GET_LOGIN_PARAMS_FAIL:
                    if (loginPregressDialog != null) {
                        if (!loginPregressDialog.isShowing()) {
                            loginPregressDialog.show();
                        }
                    }
                    break;
                case 101:
                    // 重命名 第三方登录名字相同问题
                    Intent intent = new Intent(LoginActivity.this,
                            PerfectMainActivity.class);
                    intent.putExtra("comeFrom", "LoginActivity");
                    intent.putExtra("weiboType", WeiboType.QQzone);
                    intent.putExtra("weiboName", AllWeiboInfo.TENCENT_QQZONE_NICK);
                    intent.putExtra("token", AllWeiboInfo.TENCENT_QQZONE_OPEN_ID);
                    startActivityForResult(intent, 10);
                    break;
                case 102:
                    if (msg != null && msg.getData().getString("msg") != null) {
                        String sessionid1 = msg.getData().getString("msg")
                                .substring(7);
                        // Log.e(TAG, "session_id" + sessionid1);
                        if (mSportsApp != null && sessionid1 != null
                                && !"".equals(sessionid1)) {
                            mSportsApp.setSessionId(sessionid1);
                            Intent intent1 = new Intent(LoginActivity.this,
                                    PerfectMainActivity.class);
                            intent1.putExtra("comeFrom", "SportMain");
                            startActivityForResult(intent1, 10);
                        } else {
                            mSportsApp.setSessionId("");
                            Toast.makeText(mContext,
                                    R.string.sports_findgood_error,
                                    Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(mContext, R.string.sports_data_load_failed,
                                Toast.LENGTH_SHORT).show();
                    }
                    break;
                default:
                    break;
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        MultiDex.install(this);  // 新添加代码
        super.onCreate(savedInstanceState);
        // requestWindowFeature(Window.FEATURE_NO_TITLE);
        boolean findMethod = SmartBarUtils.findActionBarTabsShowAtBottom();
        if (!findMethod) {
            // 取消ActionBar拆分，换用TabHost
            getWindow().setUiOptions(0);
            getWindow().requestFeature(android.view.Window.FEATURE_NO_TITLE);
        }
        if (getIntent() != null){
            notLogin = getIntent().getBooleanExtra("notLogin", false);
        }
        if (!notLogin){
        if (!isTaskRoot()) {
            finish();
            return;
        } else {
            setContentView(R.layout.sports_login);
        }}else{
            setContentView(R.layout.sports_login);
        }
        instance = this;
        // 友盟统计
        MobclickAgent.updateOnlineConfig(getApplicationContext());
        MobclickAgent.openActivityDurationTrack(false);
        // MobclickAgent.setDebugMode( true );
        // 友盟推送
        PushAgent mPushAgent = PushAgent.getInstance(getApplicationContext());
        mPushAgent.register(new IUmengRegisterCallback() {

            @Override
            public void onSuccess(final String registrationId) {
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        //onRegistered方法的参数registrationId即是device_token
                        if ((registrationId != null) && (registrationId.length() == 44)) {
                            Log.e("develop_debug", "callback umeng device token : " + registrationId);
                            SharedPreferences sp = getSharedPreferences("UmengDeviceToken", Activity.MODE_PRIVATE);
                            SharedPreferences.Editor spe = sp.edit();
                            spe.putString("device_token", registrationId);
                            spe.commit();
                        }
                    }
                });
            }

            @Override
            public void onFailure(String s, String s1) {

            }

        });

        String device_token = mPushAgent.getRegistrationId();
        if ((device_token != null) && (device_token.length() == 44)) {
            Log.e("develop_debug", "get umeng device token : " + device_token);
            SharedPreferences sp = getSharedPreferences("UmengDeviceToken", Activity.MODE_PRIVATE);
            SharedPreferences.Editor spe = sp.edit();
            spe.putString("device_token", device_token);
            spe.commit();
        }
        // mPushAgent.setDebugMode(true);
        PushAgent.getInstance(this).onAppStart();

        mIsFromMain = getIntent().getBooleanExtra(SportMain.FROM_MAIN_KEY,
                false);
        if (getIntent() != null){
            isFromSportMain = getIntent().getIntExtra("outTime",-1);
        }
        mContext = this;
        mSportsApp = (SportsApp) getApplication();
        mSportsApp.setLoginActivity(LoginActivity.this);
        mExceptionHandler = mSportsApp.getmExceptionHandler();

        name = (EditText) findViewById(R.id.chaomo_zhanghao);
        password = (EditText) findViewById(R.id.chaomo_password);
        forget = (TextView) findViewById(R.id.chaomo_tx_forget);
        mainLL = (RelativeLayout) findViewById(R.id.rl);
        mPopMenuBack = (RelativeLayout) findViewById(R.id.set_menu_background);
//        forget.setText(Html
//                .fromHtml("<u>"
//                        + getResources().getString(R.string.sports_forget_pwd)
//                        + "</u>"));
        forget.setOnClickListener(this);
        // findViewById(R.id.chaomo_bt_back).setOnClickListener(this);
        findViewById(R.id.chaomo_bt_login).setOnClickListener(this);
        findViewById(R.id.bt_reg).setOnClickListener(this);
        findViewById(R.id.bt_xinlang).setOnClickListener(this);
        findViewById(R.id.bt_txweibo).setOnClickListener(this);
        findViewById(R.id.bt_qqzone).setOnClickListener(this);
        findViewById(R.id.bt_jdoption).setOnClickListener(this);
        findViewById(R.id.bt_weixin).setOnClickListener(this);
        isshow_password = (ImageView) findViewById(R.id.isshow_password);
        isshow_password.setOnClickListener(this);
        isFristTry = getIntent().getBooleanExtra("isfirst_try", true);
        if (isFristTry) {
            trytologintext = (TextView) findViewById(R.id.try_to_login);
            trytologintext.setVisibility(View.VISIBLE);
//            trytologintext.setText(Html.fromHtml("<u>"
//                    + getResources().getString(R.string.test_play) + "</u>"));
        }
        qqconfig = mContext.getResources().getInteger(
                R.integer.config_qqhealthy_on);
        if (qqconfig == 1) {
            findViewById(R.id.qq_healthy_toast).setVisibility(View.VISIBLE);
        }
        findViewById(R.id.try_to_login).setOnClickListener(this);

        DisplayMetrics mertics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(mertics);
        SportsApp.ScreenWidth = mertics.widthPixels;
        SportsApp.ScreenHeight = mertics.heightPixels;
        if (findMethod) {
            getActionBar().setDisplayShowHomeEnabled(false);
            getActionBar().setDisplayShowTitleEnabled(false);
            SmartBarUtils.setActionModeHeaderHidden(getActionBar(), true);
            SmartBarUtils.setActionBarViewCollapsable(getActionBar(), true);
        }
        int config = getResources().getInteger(R.integer.config_enter_sports);
        if (config == 1) {
            // 版本修改，创建窗口快捷图标
            boolean isVersonUpdate = StatCounts
                    .checkVersonUpdate(LoginActivity.this);
            // Log.d(TAG, "isVersonUpdate:" + isVersonUpdate);
            int shortcut_config = getResources().getInteger(
                    R.integer.config_create_shortcut);
            if (isVersonUpdate) {
                if (shortcut_config == 1) {
                    deleteShortCut();
                    createDeskShortCut();
                }

                qq_toast_number = 1;
                startPause = true;
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.setClassName("com.fox.exercise",
                        "com.fox.exercise.login.FirstLoginView");
                startActivityForResult(intent, 0);
            } else {
                if (mSportsApp.isOpenNetwork()) {
                    startPause = true;
                    Intent intent = new Intent(Intent.ACTION_MAIN);
                    intent.setClassName("com.fox.exercise",
                            "com.fox.exercise.login.AdShowActivity");
                    startActivityForResult(intent, 0);
                }
            }
            CheckToken check = new CheckToken(this);
            check.start();
        }
        StatCounts.transferApp(LoginActivity.this,
                FunctionStatic.getGameId(this));
        // String deviceId = getDeviceInfo(this);
        // Log.v(TAG,"deviceId ="+deviceId);
        if ("device_disable".equals(getIntent().getStringExtra("restart_reson"))) {
            Toast.makeText(mContext,
                            R.string.login_fail_device_disable,
                            Toast.LENGTH_LONG).show();
        }

        mAppid = AllWeiboInfo.APP_ID;
        if (mTencent == null) {
            mTencent = Tencent.createInstance(mAppid, this);
        }
    }

    private String loadWXUserInfo(String url) {

        HttpClient client = new DefaultHttpClient();
        HttpGet get = new HttpGet(url);

        try {
            HttpResponse res = client.execute(get);
            String responseContent = null; // 响应内容
            HttpEntity entity = res.getEntity();
            responseContent = EntityUtils.toString(entity, "UTF-8");
            // Log.e("develop_debug", responseContent);
            return responseContent;

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 关闭连接 ,释放资源
            client.getConnectionManager().shutdown();
        }

        isWeiXinLogin = false;
        return null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("LoginActivity");
        MobclickAgent.onResume(LoginActivity.this);
        // Log.e(TAG, "startPause----" + startPause);
        if(mSportsApp==null){
            mSportsApp = (SportsApp) getApplication();
        }
        if (!startPause) {
            SharedPreferences sp = getSharedPreferences("user_login_info",
                    Context.MODE_PRIVATE);
            String openId = getIntent().getStringExtra("openid");
            if (!TextUtils.isEmpty(openId)) { // 从手机QQ登录
                if (isFromSportMain != 1) {
                    Intent intent = new Intent(LoginActivity.this, SportMain.class);
                    intent.putExtra("openid", openId);
                    intent.putExtra("accesstoken",
                            getIntent().getStringExtra("accesstoken"));
                    intent.putExtra("accesstokenexpiretime", getIntent()
                            .getStringExtra("accesstokenexpiretime"));
                    startActivity(intent);
                    stopService(new Intent("com.fox.exercise.pedometer.STEPSERVICE"));
                    mSportsApp.removeAllActivity();
                    finish();
                }
            } else if (isWeiXinLogin) {
                new AsyncTask<Void, Void, String>() {
                    @Override
                    protected String doInBackground(Void... arg0) {
                        // 获取token
                        String accessTokenUrl = "https://api.weixin.qq.com/sns/oauth2/access_token?appid="
                                + WX_APP_ID
                                + "&secret="
                                + WX_AppSecret
                                + "&code="
                                + WX_CODE
                                + "&grant_type="
                                + "authorization_code";
                        String back = loadWXUserInfo(accessTokenUrl);
                        return back;
                    }

                    protected void onPostExecute(String result) {
                        if (result != null) {
                            JSONObject json;
                            try {
                                json = new JSONObject(result);
                                if (null == json.get("access_token")) {
                                    return;
                                }
                                WX_TOKEN = json.get("access_token").toString();
                                WX_OPENID = json.get("openid").toString();
                            } catch (JSONException e1) {
                                // TODO Auto-generated catch block
                                e1.printStackTrace();
                            }
                            new AsyncTask<Void, Void, String>() {

                                @Override
                                protected String doInBackground(Void... arg0) {
                                    // 获取用户信息
                                    String userUrl = "https://api.weixin.qq.com/sns/userinfo?access_token="
                                            + WX_TOKEN + "&openid=" + WX_OPENID;
                                    return loadWXUserInfo(userUrl);
                                }

                                protected void onPostExecute(String result) {

                                    if (result != null) {
                                        JSONObject json;
                                        try {
                                            json = new JSONObject(result);
                                            nickName = json.get("nickname")
                                                    .toString();
                                        } catch (JSONException e1) {
                                            // TODO Auto-generated catch block
                                            e1.printStackTrace();
                                        }
                                        if (loginPregressDialog == null) {
                                            initLoginProgressDialog();
                                        }
                                        if (loginPregressDialog != null
                                                && !loginPregressDialog
                                                .isShowing()
                                                && !LoginActivity.this
                                                .isFinishing())
                                            loginPregressDialog.show();

                                        new AsyncTask<Void, Void, ApiBack>() {
                                            ApiBack back = null;

                                            @Override
                                            protected ApiBack doInBackground(
                                                    Void... arg0) {
                                                try {
                                                    SharedPreferences sp = mContext.getSharedPreferences("UmengDeviceToken", Activity.MODE_PRIVATE);
                                                    back = ApiJsonParser
                                                            .combineWeibo_New(
                                                                    WeiboType.WeiXin,
                                                                    nickName,
                                                                    WX_OPENID,
                                                                    1, sp.getString("device_token", ""));
                                                } catch (ApiNetException e) {
                                                    // TODO Auto-generated catch
                                                    // block
                                                    e.printStackTrace();
                                                }
                                                return back;
                                            }

                                            protected void onPostExecute(
                                                    ApiBack message) {

                                                if (message == null) {
                                                    if (loginPregressDialog != null
                                                            && loginPregressDialog
                                                            .isShowing())
                                                        loginPregressDialog
                                                                .dismiss();
                                                    Toast.makeText(
                                                            LoginActivity.this,
                                                            getResources()
                                                                    .getString(
                                                                            R.string.sports_server_error),
                                                            Toast.LENGTH_SHORT)
                                                            .show();
                                                } else {
                                                    if (loginPregressDialog != null
                                                            && loginPregressDialog
                                                            .isShowing())
                                                        loginPregressDialog
                                                                .dismiss();
                                                    if (message.getFlag() == 0
                                                            || message
                                                            .getFlag() == 1) {
                                                        if (message.getFlag() == 1) {
                                                            SportsApp.mIsAdmin = true;
                                                        } else {
                                                            SportsApp.mIsAdmin = false;
                                                        }
                                                        String sessionid = "";
                                                        if (message != null && !"".equals(message
                                                                .getMsg())) {
                                                            sessionid = message
                                                                    .getMsg()
                                                                    .substring(7);
                                                        }
                                                        Log.i("Firstload", "微信第一次登陆");
                                                        Log.e(TAG,
                                                                "session_id"
                                                                        + sessionid);
                                                        mSportsApp.setSessionId(sessionid);
                                                        mSportsApp
                                                                .setLogin(true);
                                                        SharedPreferences sp = getSharedPreferences(
                                                                AllWeiboInfo.LOGIN_INFO,
                                                                Context.MODE_PRIVATE);
                                                        Editor ed = sp.edit();
                                                        ed.clear();
                                                        ed.putString("account",
                                                                nickName);
                                                        ed.putString(
                                                                "weibotype",
                                                                ApiConstant.WeiboType.WeiXin);
                                                        ed.putString(
                                                                AllWeiboInfo.LOGIN_INFO_OPEN_ID_KEY,
                                                                WX_OPENID);
                                                        ed.commit();
//                                                        Toast.makeText(
//                                                                LoginActivity.this,
//                                                                getResources()
//                                                                        .getString(
//                                                                                R.string.sports_toast_login_succes),
//                                                                Toast.LENGTH_SHORT)
//                                                                .show();
                                                        loginFinish();
                                                        isWeiXinLogin = false;
                                                    } else if (message.getFlag() == -56) {
                                                        Message msg = Message.obtain(mHandler,
                                                                LoginActivity.LOGIN_FAIL_OUTOF_TIME);
                                                        Bundle bundle = new Bundle();
                                                        bundle.putString("msg", "微信登录，请求服务器超时");
                                                        msg.setData(bundle);
                                                        msg.sendToTarget();
                                                    }  else if (message.getFlag() == -101) {
                                                        Message msg = Message.obtain(mHandler,
                                                                LoginActivity.LOGIN_FAIL_NOCHECK_WIFI_LOGIN);
                                                        Bundle bundle = new Bundle();
                                                        bundle.putString("msg", "WIFI没有认证");
                                                        msg.setData(bundle);
                                                        msg.sendToTarget();
                                                    }  else if (message
                                                            .getFlag() == -5) {
                                                        // 微信登录注册重命名问题
                                                        if (loginPregressDialog != null
                                                                && loginPregressDialog
                                                                .isShowing())
                                                            loginPregressDialog
                                                                    .dismiss();
                                                        Intent intent = new Intent(
                                                                LoginActivity.this,
                                                                PerfectMainActivity.class);
                                                        intent.putExtra(
                                                                "comeFrom",
                                                                "LoginActivity");
                                                        intent.putExtra(
                                                                "weiboType",
                                                                WeiboType.WeiXin);
                                                        intent.putExtra(
                                                                "weiboName",
                                                                nickName);
                                                        intent.putExtra(
                                                                "token",
                                                                WX_OPENID);
                                                        startActivityForResult(
                                                                intent, 11);

                                                    } else if (message
                                                            .getFlag() == -10) {
                                                        if (loginPregressDialog != null
                                                                && loginPregressDialog
                                                                .isShowing())
                                                            loginPregressDialog
                                                                    .dismiss();
                                                        if (message.getMsg() != null
                                                                && !"".equals(message
                                                                .getMsg())) {
                                                            String sessionid1 = message
                                                                    .getMsg()
                                                                    .substring(
                                                                            7);
//															Log.e(TAG,
//																	"session_id"
//																			+ sessionid1);
                                                            if (mSportsApp != null
                                                                    && sessionid1 != null) {
                                                                mSportsApp
                                                                        .setSessionId(sessionid1);
                                                                Intent intent1 = new Intent(
                                                                        LoginActivity.this,
                                                                        PerfectMainActivity.class);
                                                                intent1.putExtra(
                                                                        "comeFrom",
                                                                        "SportMain");
                                                                startActivityForResult(
                                                                        intent1,
                                                                        11);
                                                            } else {
                                                                mSportsApp
                                                                        .setSessionId("");
                                                                Toast.makeText(
                                                                        LoginActivity.this,
                                                                        getResources()
                                                                                .getString(
                                                                                        R.string.sports_toast_login_failed),
                                                                        Toast.LENGTH_SHORT)
                                                                        .show();
                                                            }
                                                        }

                                                    } else if (-11 == message.getFlag()) {
                                                        Log.e("develop_debug", "LoginActivity.java 977");
                                                        Toast.makeText(LoginActivity.this,
                                                                getResources().getString(R.string.login_fail_device_disable),
                                                                Toast.LENGTH_SHORT).show();
                                                        clearSaveLoginInfo();
                                                    } else {
//														Log.e(TAG,
//																"loginfailed");
                                                        if (message
                                                                .getMsg()
                                                                .equals("accountOrPwdError")) {
                                                            Toast.makeText(
                                                                    LoginActivity.this,
                                                                    getResources()
                                                                            .getString(
                                                                                    R.string.sports_toast_nameOrPwdError),
                                                                    Toast.LENGTH_SHORT)
                                                                    .show();
                                                        } else {
                                                            Toast.makeText(
                                                                    LoginActivity.this,
                                                                    getResources()
                                                                            .getString(
                                                                                    R.string.sports_toast_login_failed),
                                                                    Toast.LENGTH_SHORT)
                                                                    .show();
                                                        }
                                                    }
                                                }

                                            }

                                            ;

                                        }.execute();

                                    }
                                }

                                ;
                            }.execute();
                        }

                    }

                    ;

                }.execute();
            } else if (!"".equals(sp.getString("account", ""))) {
                if (isFromSportMain != 1) {
                    Intent intent = new Intent(LoginActivity.this, SportMain.class);
                    startActivity(intent);
                    finish();
                }
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (login2PregressDialog != null && login2PregressDialog.isShowing())
            login2PregressDialog.dismiss();
        MobclickAgent.onPageEnd("LoginActivity");
        MobclickAgent.onPause(LoginActivity.this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (loginPregressDialog == null) {
            initLoginProgressDialog();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    public void resetStartState() {
        startPause = false;
    }

    private void loginFinish() {
        MainFragmentActivity.mAfterLogin = true;
        if (login2PregressDialog == null) {
            initLoginProgressDialog2();
        }
        if (!"".equals(mSportsApp.getSessionId())
                && mSportsApp.getSessionId() != null) {
            (new LoginFinishTask()).execute();
        }

    }

    class LoginFinishTask extends AsyncTask<Integer, Integer, UserDetail> {

        @Override
        protected void onPreExecute() {
            if (login2PregressDialog != null
                    && !LoginActivity.this.isFinishing())
                if (!login2PregressDialog.isShowing())
                    login2PregressDialog.show();
        }

        @Override
        protected UserDetail doInBackground(Integer... arg0) {
            UserDetail detail = null;
            // while (detail == null) {
            try {
                detail = ApiJsonParser.refreshRank(mSportsApp.getSessionId());
                // return detail;
            } catch (ApiNetException e) {
                e.printStackTrace();
                SportsApp.eMsg = Message.obtain(mExceptionHandler,
                        SportsExceptionHandler.NET_ERROR);
                SportsApp.eMsg.sendToTarget();
                return null;
            } catch (ApiSessionOutException e) {
                e.printStackTrace();
                SportsApp.eMsg = Message.obtain(mExceptionHandler,
                        SportsExceptionHandler.SESSION_OUT);
                SportsApp.eMsg.sendToTarget();
                startActivity(new Intent(mContext, LoginActivity.class));
                return null;
            }
            // }
            return detail;
        }

        @Override
        protected void onPostExecute(UserDetail result) {
            if (result != null) {
                if(mSportsApp==null){
                    mSportsApp = (SportsApp) getApplication();
                }
                mSportsApp.setSportUser(result);
                SharedPreferences sp = getSharedPreferences("user_login_info",
                        Context.MODE_PRIVATE);
                String type = sp.getString("weibotype", "");
                // SaveCurrentTimeThread thread = new SaveCurrentTimeThread();
                // thread.start();
                SharedPreferences sps = getSharedPreferences("sprots_uid", 0);
                Editor editor = sps.edit();
                editor.putInt("sportsUid", result.getUid());
                editor.commit();
                if (mBirthday != null && !mBirthday.equals("")) {
                    mSportsApp.getSportUser().setBirthday(mBirthday);
                }
                if (mSex != null && !mSex.equals("")) {
                    mSportsApp.getSportUser().setSex(mSex);

                }
                if (login2PregressDialog != null
                        && !LoginActivity.this.isFinishing())
                    if (!login2PregressDialog.isShowing())
                        login2PregressDialog.show();
                if (!mIsRegister) {
                    // if (!mIsFromMain)
                    // setResult(1);
                    if (!mTabActivityHasStart && mSportsApp.isLogin()) {
//						Log.e(TAG, "login success after main , goto plazza");
                        mTabActivityHasStart = true;
                        // SharedPreferences sharedPreferences =
                        // getSharedPreferences("sports"
                        // + mSportsApp.getSportUser().getUid(),
                        // Context.MODE_PRIVATE);
                        // if (sharedPreferences.getBoolean("isStorage", false))
                        // {
                        if (mSportsApp.LoginOption) {
                            // Log.e(TAG, "时间分割线----------------------第一次准备跳转");
                            startActivity(new Intent(LoginActivity.this,
                                    MainFragmentActivity.class));
                        } else {
                            Handler mHandler = mSportsApp.getmHandler();
                            mHandler.sendMessage(mHandler
                                    .obtainMessage(StateActivity.UPDATE_TRYTOLOGIN));
                            Handler fHandler = mSportsApp.getfHandler();
                            if (fHandler != null) {
                                fHandler.sendMessage(fHandler
                                        .obtainMessage(FoxSportsSetting.UPDATE_TRYTOLOGIN));
                            }
                            Handler mainHandler = mSportsApp.getMainHandler();
                            if (mainHandler != null) {
                                mainHandler
                                        .sendMessage(mainHandler
                                                .obtainMessage(MainFragmentActivity.UPDATE_TRYTOLOGIN));
                            }
                        }
                        mSportsApp.LoginOption = true;
                        registMsgBoxReceiver();
                        LoginActivity.this.finish();

                    } else {
                        registMsgBoxReceiver();
                        finish();
                    }
                } else {
                    if (login2PregressDialog != null
                            && !LoginActivity.this.isFinishing())
                        if (!login2PregressDialog.isShowing())
                            login2PregressDialog.show();
                    // MobclickAgent.onEvent(LoginActivity.this, "regist");
                    checkFirstLogin();
                    (new DetailTask()).execute();
                    registMsgBoxReceiver();
                }
            } else {
                if (login2PregressDialog != null
                        && login2PregressDialog.isShowing())
                    login2PregressDialog.dismiss();
                // loginFinish();
            }
        }
    }

    ;

    private void registMsgBoxReceiver() {

        Intent intent = new Intent();
        intent.setAction(SportAction.LOGIN_ACTION);
        intent.putExtra(SportAction.SEESION_ID_KEY, mSportsApp.getSessionId());
        intent.putExtra(SportAction.IS_ADMIN_KEY, SportsApp.mIsAdmin);
        sendBroadcast(intent);
        if (mContext != null) {
            LocalBroadcastManager localBroadcastManager = LocalBroadcastManager
                    .getInstance(mContext);
            final IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(SportAction.DETAIL_ACTION);
            intentFilter.addAction(SportAction.UPDATE_TAB_MSG_ACTION);
            localBroadcastManager.registerReceiver(
                    SportsLocalBroadcastReceiver.getInstance(), intentFilter);
        }
    }

    class DetailTask extends AsyncTask<Integer, Integer, UserDetail> {

        @Override
        protected UserDetail doInBackground(Integer... arg0) {
            UserDetail detail = null;
            try {
                return detail = ApiJsonParser.refreshRank(mSportsApp
                        .getSessionId());
            } catch (ApiNetException e) {
                e.printStackTrace();
                mSportsApp.eMsg = Message.obtain(mExceptionHandler,
                        SportsExceptionHandler.NET_ERROR);
                mSportsApp.eMsg.sendToTarget();
            } catch (ApiSessionOutException e) {
                e.printStackTrace();
            }
            return detail;
        }

        @Override
        protected void onPostExecute(UserDetail result) {
            if (loginPregressDialog != null)
                if (loginPregressDialog.isShowing())
                    loginPregressDialog.dismiss();
            if (result != null) {
                Intent intent = new Intent(LoginActivity.this,
                        UserEditActivity.class);
                intent.putExtra("nickname", result.getUname());
                intent.putExtra("face", result.getUimg());
                intent.putExtra("sex", mSportsApp.getSportUser().getSex());
                intent.putExtra("birthday", mSportsApp.getSportUser()
                        .getBirthday());
                intent.putExtra("phone", result.getPhoneno());
                startActivity(intent);
                LoginActivity.this.finish();
            }

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
    }

    class SaveCurrentTimeThread extends Thread {
        List<CurrentTimeList> lst;

        @Override
        public void run() {
            try {
                lst = ApiJsonParser.getCurrentTimeById(mSportsApp
                        .getSessionId(), mSportsApp.getSportUser().getUid());
                if (lst != null && lst.size() > 0) {
                    SharedPreferences.Editor editor = getSharedPreferences(
                            "CurrentTimes"
                                    + SportsApp.getInstance().getSportUser()
                                    .getUid(), MODE_WORLD_WRITEABLE)
                            .edit();
                    editor.clear();
                    if (date.equals(lst.get(0).getCurrentTime())) {
                        editor.putInt("size", lst.size());
                        for (int i = 0; i < lst.size(); i++) {
                            editor.putString("time_" + i, lst.get(i)
                                    .getCurrentTime());
                        }
                        StateActivity.mHasPlan = true;
                    } else {
                        editor.putInt("size", (lst.size() + 1));
                        for (int i = 0; i <= lst.size(); i++) {
                            if (i == 0) {
                                editor.putString("time_" + i, date);
                            } else {
                                editor.putString("time_" + i, lst.get(i - 1)
                                        .getCurrentTime());
                            }
                        }
                        StateActivity.mHasPlan = false;
                    }
                    editor.commit();
                }
            } catch (ApiNetException e) {
                SportsApp.eMsg = Message.obtain(mExceptionHandler,
                        SportsExceptionHandler.NET_ERROR);
                SportsApp.eMsg.sendToTarget();
                e.printStackTrace();
            } catch (ApiSessionOutException e) {
                e.printStackTrace();
                SportsApp.eMsg = Message.obtain(mExceptionHandler,
                        SportsExceptionHandler.SESSION_OUT);
                SportsApp.eMsg.sendToTarget();
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.chaomo_tx_forget:
                startActivity(new Intent(LoginActivity.this,
                        ForgetPwdActivity.class));
                // finish();
                break;
            case R.id.bt_reg:
                SportsLoginUtils util = new SportsLoginUtils();
                if (!util.canNormalAccount(NORMAL_PATH1, NORMAL_PATH2)) {
                    Toast.makeText(LoginActivity.this, R.string.normal_account_max,
                            Toast.LENGTH_LONG).show();
                    return;
                }
                startActivity(new Intent(LoginActivity.this, RegistActivity.class));
                // finish();
                break;
            case R.id.chaomo_bt_login:
                // Log.e(TAG, "时间分割线----------------------第一次准备登录");
                if (name.getText().toString().equals("")) {
//                    Toast.makeText(
//                            LoginActivity.this,
//                            getResources().getString(
//                                    R.string.sports_toast_input_account),
//                            Toast.LENGTH_SHORT).show();
                    exitPopWindow(mContext.getResources().getString(R.string.sports_toast_input_account),1);
                } else if (password.getText().toString().equals("")) {
//                    Toast.makeText(
//                            LoginActivity.this,
//                            getResources().getString(
//                                    R.string.sports_toast_input_pwd),
//                            Toast.LENGTH_SHORT).show();
                    exitPopWindow(mContext.getResources().getString(R.string.sports_toast_input_pwd),1);
                } else {
                    if (loginPregressDialog == null) {
                        initLoginProgressDialog();
                    }
                    loginPregressDialog.show();
                    if (Tools.isNetworkConnected(LoginActivity.this)) {
                        (new OriginalLoginTask(LoginActivity.this,
                                new OriginalLoginInfoViews(name, password,
                                        loginPregressDialog, LOGIN_FINISH,
                                        LOGIN_FAIL), mHandler)).execute();
                    } else {
                        loginPregressDialog.dismiss();
                        if (toastnet != null) {
                            toastnet.cancel();
                        } else {
                            toastnet = Toast.makeText(
                                    LoginActivity.this,
                                    getResources().getString(
                                            R.string.error_cannot_access_net),
                                    Toast.LENGTH_SHORT);
                        }
                        toastnet.show();
                    }
                }
                break;
            case R.id.bt_xinlang:
                if (Tools.isNetworkConnected(LoginActivity.this)) {
                    gotoXinlangWeibo();
                } else {
                    Toast.makeText(
                            LoginActivity.this,
                            getResources().getString(
                                    R.string.error_cannot_access_net),
                            Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.bt_txweibo:
                if (Tools.isNetworkConnected(LoginActivity.this)) {
                    gotoTenxunWeibo();
                } else {
                    Toast.makeText(
                            LoginActivity.this,
                            getResources().getString(
                                    R.string.error_cannot_access_net),
                            Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.bt_qqzone:
                if (Tools.isNetworkConnected(LoginActivity.this)) {
                    LoginQQ();
                } else {
                    Toast.makeText(
                            LoginActivity.this,
                            getResources().getString(
                                    R.string.error_cannot_access_net),
                            Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.bt_jdoption:
                if (Tools.isNetworkConnected(LoginActivity.this)) {
                    gotoJDOption();
                } else {
                    Toast.makeText(
                            LoginActivity.this,
                            getResources().getString(
                                    R.string.error_cannot_access_net),
                            Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.bt_weixin:
                if (Tools.isNetworkConnected(LoginActivity.this)) {
                    gotoWeiXin();
                } else {
                    Toast.makeText(
                            LoginActivity.this,
                            getResources().getString(
                                    R.string.error_cannot_access_net),
                            Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.try_to_login:
                MobclickAgent.onEvent(mContext, "freeuser");
                mSportsApp.LoginOption = false;
                UserDetail userDetail = new UserDetail();
                userDetail.setUid(0);
                userDetail.setWeight(65);
                mSportsApp.setSportUser(userDetail);
                startActivity(new Intent(LoginActivity.this,
                        MainFragmentActivity.class));
                LoginActivity.this.finish();
                break;
            case R.id.bt_ok:
                alertQQDialog.cancel();
                if (Tools.isNetworkConnected(LoginActivity.this)) {
                    LoginQQ();
                } else {
                    Toast.makeText(
                            LoginActivity.this,
                            getResources().getString(
                                    R.string.error_cannot_access_net),
                            Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.bt_cancel:
                alertQQDialog.cancel();
                break;
            case R.id.isshow_password:
                if (passwordisshow) {
                    passwordisshow = false;
                    password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    isshow_password.setBackgroundResource(R.drawable.according_psw);
                }else{
                    passwordisshow = true;
                    password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    isshow_password.setBackgroundResource(R.drawable.hidden_psw);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // mSportsApp.isFirst=true;
        instance = null;
        mSportsApp = null;
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (alertDialog != null) {
            alertDialog.cancel();
            alertDialog = null;
        }
        if (loginPregressDialog != null) {
            if (loginPregressDialog.isShowing()) {
                loginPregressDialog.dismiss();
            }
            loginPregressDialog = null;
        }
    }

    class GetUserNameThread extends Thread {
        @Override
        public void run() {
            if (mSportsApp.getSessionId() == null
                    || "".equals(mSportsApp.getSessionId()))
                return;
            try {
                UserDetail detail = ApiJsonParser.refreshRank(mSportsApp
                        .getSessionId());
                if (detail == null)
                    detail = ApiJsonParser.refreshRank(mSportsApp
                            .getSessionId());
                SportMain.user_name = detail.getUname();
            } catch (ApiNetException e) {
                e.printStackTrace();
                SportsApp.eMsg = Message.obtain(mExceptionHandler,
                        SportsExceptionHandler.NET_ERROR);
                SportsApp.eMsg.sendToTarget();
            } catch (ApiSessionOutException e) {
                e.printStackTrace();
                SportsApp.eMsg = Message.obtain(mExceptionHandler,
                        SportsExceptionHandler.SESSION_OUT);
                SportsApp.eMsg.sendToTarget();
                startActivity(new Intent(LoginActivity.this,
                        LoginActivity.class));
            }
        }
    }


    // 检查是否第一次登陆
    private void checkFirstLogin() {
//		Log.d(TAG, "checkFirstLoginFromMarket");
        // File file = new File(getConfigPath());
        // boolean flag = false;
        // if (!file.getParentFile().exists()) {
        // flag = file.getParentFile().mkdirs();
        // Log.d(TAG, "file.mkdirs():" + flag);
        // }
        // if (!file.exists()) {
        // try {
        // flag = file.createNewFile();
        // Log.d(TAG, "file.createNewFile():" + flag);
        // } catch (IOException e1) {
        // e1.printStackTrace();
        // }
        // } else
        // return;
        // if (file.exists()) {
        // Log.d(TAG, "file exists");
        // if (file.length() == 0L
        // && getResources().getInteger(R.integer.coin_gift) == 1) {
        (new MarketTask()).execute();
        // return;
        // }
        // }
    }

    class MarketTask extends AsyncTask<Integer, Integer, ApiBack> {
        @Override
        protected ApiBack doInBackground(Integer... arg0) {
//			Log.e(TAG, "coinsGiftFromMarket");
            try {
                ApiBack back = ApiJsonParser.activities(
                        mSportsApp.getSessionId(),
                        Integer.parseInt(getResources().getString(
                                R.string.config_game_id)));
                return back;
            } catch (NotFoundException e) {
                e.printStackTrace();
            } catch (ApiNetException e) {
                SportsApp.eMsg = Message.obtain(mExceptionHandler,
                        SportsExceptionHandler.NET_ERROR);
                SportsApp.eMsg.sendToTarget();
                e.printStackTrace();
            } catch (ApiSessionOutException e) {
                e.printStackTrace();
                SportsApp.eMsg = Message.obtain(mExceptionHandler,
                        SportsExceptionHandler.SESSION_OUT);
                SportsApp.eMsg.sendToTarget();
                startActivity(new Intent(LoginActivity.this,
                        LoginActivity.class));
            }
            return null;
        }

        @Override
        protected void onPostExecute(ApiBack result) {
            if (result == null || result.getFlag() != 0) {
                return;
            }
            String format = getResources().getString(
                    R.string.sports_market_gift);
            String s = String.format(format,
                    FunctionStatic.getGameNameById(LoginActivity.this) + "渠道",
                    result.getMsg());
            Toast.makeText(SportsApp.getContext(), s, Toast.LENGTH_SHORT)
                    .show();
        }
    }

    private void initLoginProgressDialog() {
        loginPregressDialog = new Dialog(LoginActivity.this,
                R.style.sports_dialog);
        LayoutInflater mInflater = getLayoutInflater();
        View v1 = mInflater.inflate(R.layout.sports_progressdialog, null);
        TextView message = (TextView) v1.findViewById(R.id.message);
        message.setText(R.string.sports_logining);
        v1.setMinimumWidth((int) (SportsApp.ScreenWidth * 0.8));
        loginPregressDialog.setContentView(v1);
        loginPregressDialog.setCancelable(true);
        loginPregressDialog.setCanceledOnTouchOutside(false);
    }

    private void initLoginProgressDialog2() {
        login2PregressDialog = new Dialog(LoginActivity.this,
                R.style.sports_dialog);
        LayoutInflater mInflater = getLayoutInflater();
        View v1 = mInflater.inflate(R.layout.sports_progressdialog, null);
        TextView message = (TextView) v1.findViewById(R.id.message);
        message.setText(R.string.sports_second_loading);
        v1.setMinimumWidth((int) (SportsApp.ScreenWidth * 0.8));
        login2PregressDialog.setContentView(v1);
        login2PregressDialog.setCancelable(true);
        login2PregressDialog.setCanceledOnTouchOutside(false);
    }

    // 新浪微博登陆方法
    private void gotoXinlangWeibo() {
        // Oauth2.0
        // 隐式授权认证方式
        Weibo weibo = Weibo.getInstance();
        weibo.setupConsumerConfig(WeiboConstParam.CONSUMER_KEY,
                WeiboConstParam.CONSUMER_SECRET);
        // 此处回调页内容应该替换为与appkey对应的应用回调页
        weibo.setRedirectUrl(WeiboConstParam.REDIRECT_URL);
        // 启动认证
        weibo.authorize(LoginActivity.this, new AuthDialogListener());
    }

    // 腾讯微博登陆方法
    private void gotoTenxunWeibo() {
        Intent intent = new Intent();
        intent.setClass(mContext, TencentAuthorizeActivity.class);
        startActivityForResult(intent, TENCENT);
    }

    // 京东云登录要调用的方法
    private void gotoJDOption() {
        Intent intent = new Intent(LoginActivity.this, JDAuthActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("JDOptionAppKey", AllWeiboInfo.JDOption_AppKey);
        bundle.putString("JDOptionAppSecret", AllWeiboInfo.JDOption_AppSecret);
        bundle.putString("JDOptionAppRedirectUri",
                AllWeiboInfo.JDOption_AppRedirectUri);
        bundle.putInt("NavaigationColor", R.color.red);
        intent.putExtra("JDAuth", bundle);
        startActivityForResult(intent, JDConfigs.JD_RESULT);
    }

    private void gotoWeiXin() {
        // 通过WXAPIFactory工厂，获取IWXAPI的实例
        api = WXAPIFactory.createWXAPI(this, WX_APP_ID, true);
        // 将应用的appid注册到微信
        api.registerApp(WX_APP_ID);

        if (!api.isWXAppInstalled()) {
            Toast.makeText(getApplicationContext(),
                    getString(R.string.sports_shareto_weixin_no_weixin),
                    Toast.LENGTH_SHORT).show();
            return;
        }

        /**
         * 第三方发起微信授权登录请求，微信用户允许授权第三方应用后， 微信会拉起应用或重定向到第三方网站，并且带上授权临时票据code参数
         *
         */
        isWeiXinLogin = true;
        SendAuth.Req loginReq = new SendAuth.Req();
        loginReq.scope = "snsapi_userinfo";
        loginReq.state = "wechat_sdk_demo_test";
        // BestGirlApp.isWeixinLogin = true;
        api.sendReq(loginReq);
    }

    // 新浪微博登陆要调用的方法
    class AuthDialogListener implements WeiboDialogListener, RequestListener {

        private String uid = "";
        private String tokenString = "";

        @Override
        public void onComplete(Bundle values) {
            String token = values.getString("access_token");
            AuthoSharePreference.putToken(LoginActivity.this, token);
            tokenString = AuthoSharePreference.getToken(LoginActivity.this);
            AccessToken accessToken = new AccessToken(tokenString,
                    WeiboConstParam.CONSUMER_SECRET);
            final Weibo weibo = Weibo.getInstance();
            weibo.setAccessToken(accessToken);

            String expires_in = values.getString("expires_in");
            AuthoSharePreference.putExpires(LoginActivity.this, expires_in);

            String remind_in = values.getString("remind_in");
            AuthoSharePreference.putRemind(LoginActivity.this, remind_in);
            uid = values.getString("uid");
            AuthoSharePreference.putUid(LoginActivity.this, uid);
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... params) {
                    retrieveSinaUserInfo(weibo, Weibo.getAppKey(),
                            weibo.getAccessToken(), uid);
                    return null;
                }
            }.execute();
        }

        private void retrieveSinaUserInfo(Weibo weibo, String source,
                                          Token token, String uid) {
            Message.obtain(mHandler, LOADING_DIALOG).sendToTarget();
            WeiboParameters bundle = new WeiboParameters();
            bundle.add("access_token", token.getToken());
            bundle.add("uid", uid);
            String url = Weibo.SERVER + "users/show.json";
            AsyncWeiboRunner weiboRunner = new AsyncWeiboRunner(weibo);
            weiboRunner.request(getApplicationContext(), url, bundle,
                    Utility.HTTPMETHOD_GET, this);
        }

        @Override
        public void onComplete(String response) {
            Message msg = mHandler.obtainMessage(SINA_LOADING, response);
            mHandler.sendMessage(msg);

            try {
                JSONObject js = new JSONObject(response);
                user_name = js.getString("screen_name");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            AccessInfo accessInfo = new AccessInfo();
            accessInfo.setUserID(uid);
            accessInfo.setAccessToken(tokenString);
            accessInfo.setAccessSecret(WeiboConstParam.CONSUMER_SECRET);
            accessInfo.setNickName(user_name);

            AccessInfoHelper accessDBHelper = new AccessInfoHelper(mContext);
            if (accessDBHelper.open() != null) {
                accessDBHelper.clear();
                accessDBHelper.create(accessInfo);
                accessDBHelper.close();
            }
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

    private QQWeiboProxy mQqWeiboProxy;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case Constants.REQUEST_LOGIN:
            case Constants.REQUEST_APPBAR:
                Tencent.onActivityResultData(requestCode,resultCode,data,loginListener);
                break;
            case TENCENT: {
                if (resultCode == RESULT_OK)
                    onResultForAuthActivity(data);
            }
            break;
            case JDConfigs.JD_RESULT:
                if (data != null) {
                    onResultJDForAuthActivity(data);
                } else {
                    Toast.makeText(getApplicationContext(),
                            getResources().getString(R.string.get_failed_tryother),
                            Toast.LENGTH_LONG).show();
                }
                break;
            case 0:
                SharedPreferences sp = getSharedPreferences("user_login_info",
                        Context.MODE_PRIVATE);
                if (qq_toast_number == 1) {
                    qq_toast_number = 0;
                    if (!"".equals(sp.getString("account", ""))) {
                        if (isFromSportMain != 1){
                            Intent intent = new Intent(LoginActivity.this,
                                    SportMain.class);
                            startActivity(intent);
                            finish();
                        }
                    } else {
                        Intent intent = new Intent(LoginActivity.this,
                                SplashScreenActivity.class);
                        startActivity(intent);

//                        if (qqconfig == 1) {
                            showqqDialog(getString(R.string.qq_login),
                                    getString(R.string.healthy_qq_toast));
//                        }
                    }
                } else {
                    String openId = getIntent().getStringExtra("openid");
                    if (!TextUtils.isEmpty(openId)) { // 从手机QQ登录
                        if (isFromSportMain != 1){
                            Intent intent = new Intent(LoginActivity.this,
                                    SportMain.class);
                            intent.putExtra("openid", openId);
                            intent.putExtra("accesstoken",
                                    getIntent().getStringExtra("accesstoken"));
                            intent.putExtra("accesstokenexpiretime", getIntent()
                                    .getStringExtra("accesstokenexpiretime"));
                            stopService(new Intent(
                                    "com.fox.exercise.pedometer.STEPSERVICE"));
                            startActivity(intent);
                            finish();
                        }
                    } else if (!"".equals(sp.getString("account", ""))) {
                        if (isFromSportMain != 1){
                            Intent intent = new Intent(LoginActivity.this,
                                    SportMain.class);
                            startActivity(intent);
                            finish();
                        }
                    } else {
                        Intent intent = new Intent(LoginActivity.this,
                                SplashScreenActivity.class);
                        startActivity(intent);
                    }
                }
                break;
            case 10:
                // qq登录，名字重命名修改后返回的   
                SharedPreferences sp1 = getSharedPreferences("user_login_info",
                        Context.MODE_PRIVATE);

                Editor e = sp1.edit();
                e.putString(AllWeiboInfo.TENCENT_QQZONE_OPEN_ID_KEY,
                        AllWeiboInfo.TENCENT_QQZONE_OPEN_ID);
                e.putString("account", AllWeiboInfo.TENCENT_QQZONE_NICK);
                e.putString("weibotype", "qqzone");
                e.putString("access_token",
                        AllWeiboInfo.TENCENT_QQZONE_TOKEN_URL_KEY);
                e.putInt("login_way", 1);
                e.commit();
                if(mSportsApp==null){
                    mSportsApp = (SportsApp) getApplication();
                }
                mSportsApp.setLogin(true);

                SharedPreferences tSp = getSharedPreferences(
                        AllWeiboInfo.TENCENT_QQZONE_TOKEN_SP, MODE_PRIVATE);
                Editor te = tSp.edit();
                te.putString(AllWeiboInfo.TENCENT_QQZONE_TOKEN_KEY,
                        AllWeiboInfo.TENCENT_QQZONE_TOKEN);
                te.putString(AllWeiboInfo.TENCENT_QQZONE_OPEN_ID_KEY,
                        AllWeiboInfo.TENCENT_QQZONE_OPEN_ID);
                te.putLong(AllWeiboInfo.TENCENT_QQZONE_TOKEN_SP_TIME,
                        System.currentTimeMillis());
                te.commit();

                loginFinish();

                break;
            case 11:
                if(mSportsApp==null){
                    mSportsApp = (SportsApp) getApplication();
                }
                mSportsApp.setLogin(true);
                SharedPreferences sp2 = getSharedPreferences(
                        AllWeiboInfo.LOGIN_INFO, Context.MODE_PRIVATE);
                Editor ed = sp2.edit();
                ed.clear();
                ed.putString("account", nickName);
                ed.putString("weibotype", ApiConstant.WeiboType.WeiXin);
                ed.putString(AllWeiboInfo.LOGIN_INFO_OPEN_ID_KEY, WX_OPENID);
                ed.commit();
//                Toast.makeText(
//                        LoginActivity.this,
//                        getResources()
//                                .getString(R.string.sports_toast_login_succes),
//                        Toast.LENGTH_SHORT).show();
                loginFinish();
                isWeiXinLogin = false;
                break;
            case 12:
                if(mSportsApp==null){
                    mSportsApp = (SportsApp) getApplication();
                }
                mSportsApp.setLogin(true);
                // 清除微博用户登陆信息，重写新浪微博用户登录信息
                SharedPreferences sp3 = getSharedPreferences(
                        AllWeiboInfo.LOGIN_INFO, Context.MODE_PRIVATE);
                Editor ed3 = sp3.edit();
                ed3.clear();
                ed3.putString("account", user_name);
                ed3.putString("weibotype", ApiConstant.WeiboType.SinaWeibo);
                ed3.putString(AllWeiboInfo.LOGIN_INFO_OPEN_ID_KEY, openId);
                ed3.commit();
                // 保存用户登录身份标识
                SharedPreferences sps = getSharedPreferences("user_login_info",
                        Context.MODE_PRIVATE);
                Editor eds = sps.edit();
                eds.putInt("login_way", 1);
                eds.commit();
                loginFinish();
                break;
            case 13:
                if(mSportsApp==null){
                    mSportsApp = (SportsApp) getApplication();
                }
                mSportsApp.setLogin(true);
                SharedPreferences sp4 = getSharedPreferences(
                        AllWeiboInfo.LOGIN_INFO, Context.MODE_PRIVATE);
                Editor ed4 = sp4.edit();
                ed4.clear();
                ed4.putString("account", userId);
                ed4.putString("weibotype", ApiConstant.WeiboType.TengxunWeiBo);
                ed4.putString(AllWeiboInfo.LOGIN_INFO_OPEN_ID_KEY, openId);
                ed4.commit();
                // 保存用户登录身份标识
                SharedPreferences sp5 = getSharedPreferences("user_login_info",
                        Context.MODE_PRIVATE);
                Editor ed5 = sp5.edit();
                ed5.putInt("login_way", 1);
                ed5.commit();
                loginFinish();
                break;
            case 14:
                // 只是判断昵称超过15个字符的
                loginFinish();
                break;
        }
    }

    // 首次启动 提示QQ登录可传数据
    public void showqqDialog(String btnStr, String content) {
        // TODO Auto-generated method stub
        alertQQDialog = new Dialog(this, R.style.sports_coins_dialog);
        LayoutInflater mInflater = getLayoutInflater();
        View v = mInflater.inflate(R.layout.from_qq_dialog, null);
        Button ok = (Button) v.findViewById(R.id.bt_ok);
        ok.setText(btnStr);
        ok.setOnClickListener(this);
        v.findViewById(R.id.bt_cancel).setOnClickListener(this);
        TextView message = (TextView) v.findViewById(R.id.message);
        message.setText(content);
        v.setMinimumWidth((int) (SportsApp.ScreenWidth * 0.8));
        alertQQDialog.setCancelable(true);
        alertQQDialog.setCanceledOnTouchOutside(false);
        alertQQDialog.setContentView(v);
        alertQQDialog.show();
    }

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
                if (loginPregressDialog != null
                        && !LoginActivity.this.isFinishing())
                    loginPregressDialog.show();
            }

            @Override
            protected void onPostExecute(Void void1) {

                try {
                    JSONObject data = new JSONObject(userInfo)
                            .getJSONObject("data");
                    openId = "$" + data.getString("openid");
                    userId = data.getString("name");
//					Log.e(TAG, "userId = " + userId);
//					Log.e(TAG, "data88888 = " + data);
                    TencentDataHelper tencent_dataHelper = new TencentDataHelper(
                            mContext);
                    user_name = data.getString("nick");
                    mBirthday = ""
                            + data.getString("birth_year")
                            + "-"
                            + ((data.getString("birth_month").length() == 1) ? ("0" + data
                            .getString("birth_month")) : data
                            .getString("birth_month"))
                            + "-"
                            + ((data.getString("birth_day").length() == 1) ? ("0" + data
                            .getString("birth_day")) : data
                            .getString("birth_day"));
//					Log.d(TAG, "birthday:" + mBirthday);
                    UserInfo user = new UserInfo();
                    user.setUserId(data.getString("openid"));
                    user.setUserName(userId);
                    user.setUserNick(user_name);
                    user.setToken(access_token);
                    user.setExpiresIn(expires_in);
                    user.setOpenID(openID);
                    user.setOpenKey(openKey);
                    if (data.getInt("sex") == 1) {
                        mSex = "man";
                    } else if (data.getInt("sex") == 2) {
                        mSex = "woman";
                    }

//					Log.v(TAG, "userName = " + user_name);

                    tencent_dataHelper.clearDB();
                    tencent_dataHelper.SaveUserInfo(user);
                    tencent_dataHelper.Close();
                    if (Tools.isNetworkConnected(LoginActivity.this)) {
                        new AsyncTask<Void, Void, ApiBack>() {
                            @Override
                            protected ApiBack doInBackground(Void... params) {
//								Log.e(TAG, "doInBackground");
                                ApiBack back = null;
                                try {
                                    if (userId == null || "".equals(userId)
                                            || openId == null
                                            || "".equals(openId)) {
                                        Message.obtain(mHandler, LOADING_DIALOG)
                                                .sendToTarget();
                                        return null;
                                    }
                                    SharedPreferences sp = mContext.getSharedPreferences("UmengDeviceToken", Activity.MODE_PRIVATE);
                                    back = ApiJsonParser.combineWeibo_New(
                                            ApiConstant.WeiboType.TengxunWeiBo,
                                            userId, openId, 1, sp.getString("device_token", ""));
                                } catch (ApiNetException e) {
                                    e.printStackTrace();
                                    Message.obtain(mExceptionHandler,
                                            SportsExceptionHandler.NET_ERROR)
                                            .sendToTarget();
                                }
                                return back;
                            }

                            @Override
                            protected void onPostExecute(ApiBack message) {
                                if (message == null) {
                                    loginPregressDialog.dismiss();
                                    Toast.makeText(
                                            LoginActivity.this,
                                            getResources()
                                                    .getString(
                                                            R.string.sports_server_error),
                                            Toast.LENGTH_SHORT).show();
                                } else {
                                    if (message.getFlag() == 0
                                            || message.getFlag() == 1) {
                                        if (message.getFlag() == 1) {
                                            SportsApp.mIsAdmin = true;
                                        } else {
                                            SportsApp.mIsAdmin = false;
                                        }
                                        String sessionid = message.getMsg()
                                                .substring(7);
                                        mSportsApp.setSessionId(sessionid);
                                        mSportsApp.setLogin(true);
                                        SharedPreferences sp = getSharedPreferences(
                                                AllWeiboInfo.LOGIN_INFO,
                                                Context.MODE_PRIVATE);
                                        Editor ed = sp.edit();
                                        ed.clear();
                                        ed.putString("account", userId);
                                        ed.putString(
                                                "weibotype",
                                                ApiConstant.WeiboType.TengxunWeiBo);
                                        ed.putString(
                                                AllWeiboInfo.LOGIN_INFO_OPEN_ID_KEY,
                                                openId);
                                        ed.commit();
                                        // 保存用户登录身份标识
                                        SharedPreferences sps = getSharedPreferences(
                                                "user_login_info",
                                                Context.MODE_PRIVATE);
                                        Editor eds = sps.edit();
                                        eds.putInt("login_way", 1);
                                        eds.commit();
//                                        Toast.makeText(
//                                                LoginActivity.this,
//                                                getResources()
//                                                        .getString(
//                                                                R.string.sports_toast_login_succes),
//                                                Toast.LENGTH_SHORT).show();
                                        loginFinish();
                                    } else if (message.getFlag() == -56) {
                                        Message msg = Message.obtain(mHandler,
                                                LoginActivity.LOGIN_FAIL_OUTOF_TIME);
                                        Bundle bundle = new Bundle();
                                        bundle.putString("msg", "腾讯微博登录，请求服务器超时");
                                        msg.setData(bundle);
                                        msg.sendToTarget();
                                    }   else if (message.getFlag() == -5) {
                                        // 腾讯微博注册登录重名名问题
                                        Intent intent = new Intent(
                                                LoginActivity.this,
                                                PerfectMainActivity.class);
                                        intent.putExtra("comeFrom",
                                                "LoginActivity");
                                        intent.putExtra(
                                                "weiboType",
                                                ApiConstant.WeiboType.TengxunWeiBo);
                                        intent.putExtra("weiboName", userId);
                                        intent.putExtra("token", openId);
                                        startActivityForResult(intent, 13);
                                    } else if (message.getFlag() == -10) {
                                        if (message.getMsg() != null
                                                && !"".equals(message.getMsg())) {
                                            String sessionid1 = message
                                                    .getMsg().substring(7);
                                            Log.i("Firstload", "腾讯微博第一次登陆");
                                            if (mSportsApp != null
                                                    && sessionid1 != null) {
                                                mSportsApp
                                                        .setSessionId(sessionid1);
                                                Intent intent1 = new Intent(
                                                        LoginActivity.this,
                                                        PerfectMainActivity.class);
                                                intent1.putExtra("comeFrom",
                                                        "SportMain");
                                                startActivityForResult(intent1,
                                                        13);
                                            } else {
                                                mSportsApp.setSessionId("");
                                                Toast.makeText(
                                                        LoginActivity.this,
                                                        getResources()
                                                                .getString(
                                                                        R.string.sports_toast_login_failed),
                                                        Toast.LENGTH_SHORT)
                                                        .show();
                                            }
                                        }

                                    } else if (-11 == message.getFlag()) {
                                        Log.e("develop_debug", "LoginActivity.java 2269");
                                        loginPregressDialog.dismiss();
                                        Toast.makeText(LoginActivity.this,
                                                getResources().getString(R.string.login_fail_device_disable),
                                                Toast.LENGTH_LONG).show();
                                        clearSaveLoginInfo();
                                    } else {
                                        if (message.getMsg().equals(
                                                "accountOrPwdError")) {
                                            Toast.makeText(
                                                    LoginActivity.this,
                                                    getResources()
                                                            .getString(
                                                                    R.string.sports_toast_nameOrPwdError),
                                                    Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(
                                                    LoginActivity.this,
                                                    getResources()
                                                            .getString(
                                                                    R.string.sports_toast_login_failed),
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                            }
                        }.execute();
                    } else {
                        loginPregressDialog.dismiss();
                        if (toastnet != null) {
                            toastnet.cancel();
                        } else {
                            toastnet = Toast.makeText(
                                    LoginActivity.this,
                                    getResources().getString(
                                            R.string.error_cannot_access_net),
                                    Toast.LENGTH_SHORT);
                        }
                        toastnet.show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }.execute();

    }

    // 创建桌面快捷图标
    private void createDeskShortCut() {
        // 创建快捷方式的Intent
        Intent shortcutIntent = new Intent(
                "com.android.launcher.action.INSTALL_SHORTCUT");
        // 不允许重复创建
        shortcutIntent.putExtra("duplicate", false);
        // 需要现实的名称
        shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME,
                getString(R.string.sports_name));

        // 快捷图片
        Parcelable icon = Intent.ShortcutIconResource.fromContext(
                getApplicationContext(), R.drawable.icon);

        shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, icon);

        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        // 下面两个属性是为了当应用程序卸载时桌面 上的快捷方式会删除
        intent.setAction("android.intent.action.MAIN");
        intent.addCategory("android.intent.category.LAUNCHER");
        // 点击快捷图片，运行的程序主入口
        shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, intent);
        // 发送广播。OK
        sendBroadcast(shortcutIntent);
    }

    /**
     * 删除程序的快捷方式
     */
    /**
     * 删除快捷方式
     */
    public static void deleteShortCut() {

        Intent shortcut = new Intent(
                "com.android.launcher.action.UNINSTALL_SHORTCUT");
        // 快捷方式的名称
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, "点点运动");
        /** 删除和创建需要对应才能找到快捷方式并成功删除 **/
        Intent intent = new Intent();
        intent.setClass(mContext, mContext.getClass());
        intent.setAction("android.intent.action.MAIN");
        intent.addCategory("android.intent.category.LAUNCHER");

        shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, intent);
        mContext.sendBroadcast(shortcut);
    }

    // 修改APP版本
    private void UpdateApp() {
//		Log.v(TAG, "UpdateApp in");
        UpdataApplication updataApplication = new UpdataApplication(this);
        try {
//			Log.v(TAG, "UpdateAppBackground begin");
            updataApplication.UpdateAppBackground(true);
//			Log.v(TAG, "UpdateAppBackground end");
        } catch (Exception e) {
            e.printStackTrace();
//			Log.v(TAG, "UpdateAppBackground err");
        }
        StatCounts.transferApp(LoginActivity.this,
                FunctionStatic.getGameId(this));
    }

    // -------------------------------------qq登录要调用的方法
    public void LoginQQ() {
        // qqContext = this.getApplicationContext();
        if (mTencent.isSessionValid()) {
            mTencent.logout(LoginActivity.this);
        }
        mTencent.login(LoginActivity.this, "all", loginListener);

    }

    IUiListener loginListener = new BaseUiListener() {
        @Override
        protected void doComplete(JSONObject values) {
            Log.e("develop_debug", "doComplete : " + values.toString());
        }
    };

    private class BaseUiListener implements IUiListener {
        @Override
        public void onComplete(Object response) {
            // TODO Auto-generated method stub
            Log.e("develop_debug", "onComplete : " + response.toString());
            if (loginPregressDialog != null)
                if (!loginPregressDialog.isShowing()) {
                    loginPregressDialog.show();
                }
            try {
                AllWeiboInfo.TENCENT_QQZONE_OPEN_ID = ((JSONObject) response)
                        .getString(Constants.PARAM_OPEN_ID);
                AllWeiboInfo.TENCENT_QQZONE_TOKEN_URL_KEY = ((JSONObject) response)
                        .getString(Constants.PARAM_ACCESS_TOKEN);
                AllWeiboInfo.TENCENT_QQZONE_EXPIRES_URL_KEY = ((JSONObject) response)
                        .getString(Constants.PARAM_EXPIRES_IN);
                if (!TextUtils.isEmpty(AllWeiboInfo.TENCENT_QQZONE_TOKEN_URL_KEY) && !TextUtils.isEmpty(AllWeiboInfo.TENCENT_QQZONE_EXPIRES_URL_KEY)
                        && !TextUtils.isEmpty(AllWeiboInfo.TENCENT_QQZONE_OPEN_ID)) {
                    mTencent.setAccessToken(AllWeiboInfo.TENCENT_QQZONE_TOKEN_URL_KEY, AllWeiboInfo.TENCENT_QQZONE_EXPIRES_URL_KEY);
                    mTencent.setOpenId(AllWeiboInfo.TENCENT_QQZONE_OPEN_ID);
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
			Log.e("develop_debug", "------------" + response.toString());
            QQToken qqToken = mTencent.getQQToken();
            com.tencent.connect.UserInfo info = new com.tencent.connect.UserInfo(
                    getApplicationContext(), qqToken);
            info.getUserInfo(new IUiListener() {
                @Override
                public void onError(UiError arg0) {
                    // TODO Auto-generated method stub
                }

                @Override
                public void onComplete(Object response) {
                    // TODO Auto-generated method stub
                    try {
						Log.e("develop_debug", "------------" + response.toString());
                        int ret = ((JSONObject) response).getInt("ret");
                        if (ret == 100030) {
                            Toast.makeText(
                                    getApplicationContext(),
                                    getResources().getString(
                                            R.string.get_power_failed), Toast.LENGTH_LONG)
                                    .show();
                            // 这里进行增量授权的操作
                            // if(mNeedReAuth){
                            Runnable r = new Runnable() {
                                public void run() {
                                    mTencent.reAuth(LoginActivity.this, "all",
                                            new BaseUiListener());
                                }
                            };
                            LoginActivity.this.runOnUiThread(r);
                            // }
                        } else {
                            Toast.makeText(
                                    getApplicationContext(),
                                    getResources().getString(
                                            R.string.get_power_sucess), Toast.LENGTH_LONG)
                                    .show();
                            AllWeiboInfo.TENCENT_QQZONE_NICK = ((JSONObject) response)
                                    .getString("nickname");
                            AllWeiboInfo.TENCENT_QQZONE_PHOTO = ((JSONObject) response)
                                    .getString("figureurl_qq_2");
                        }
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    if (AllWeiboInfo.TENCENT_QQZONE_NICK == null
                            || "".equals(AllWeiboInfo.TENCENT_QQZONE_NICK)
                            || AllWeiboInfo.TENCENT_QQZONE_OPEN_ID == null
                            || "".equals(AllWeiboInfo.TENCENT_QQZONE_OPEN_ID)) {
                        intcols = 1;
                    } else {
                        intcols = 2;
                    }
                    if (intcols == 2) {
                        mTencent.logout(mContext);
                        LoginByQQThread thread = new LoginByQQThread(
                                LoginActivity.this, mHandler);
                        thread.start();
                    }
                }

                @Override
                public void onCancel() {
                    // TODO Auto-generated method stub
                }
            });
        }

        @Override
        public void onCancel() {
            // TODO Auto-generated method stub
            Log.e("develop_debug", "onCancel");
        }

        @Override
        public void onError(UiError arg0) {
            // TODO Auto-generated method stub
            Log.e("develop_debug", "onError : " + arg0.toString());
        }

        protected void doComplete(JSONObject values) {

        }
    }

    private void onResultJDForAuthActivity(Intent data) {
        // TODO Auto-generated method stub
        if (loginPregressDialog != null && !LoginActivity.this.isFinishing())
            loginPregressDialog.show();
        Toast.makeText(getApplicationContext(),
                getResources().getString(R.string.get_power_sucess), Toast.LENGTH_LONG).show();
        String result = data.getStringExtra("result");
        try {
            JSONObject jsonObject = new JSONObject(result);
            AllWeiboInfo.JDOption_OPEN_ID = jsonObject.getString("uid");
            AllWeiboInfo.JDOption_NICK = jsonObject.getString("user_nick");
            AllWeiboInfo.JDOption_EXPIRES_URL = jsonObject
                    .getString("expires_in");
            AllWeiboInfo.JDOption_TOKEN_URL = jsonObject
                    .getString("access_token");
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (WeiboType.JDOption == null || "".equals(WeiboType.JDOption)
                || AllWeiboInfo.JDOption_NICK == null
                || "".equals(AllWeiboInfo.JDOption_NICK)
                || AllWeiboInfo.JDOption_OPEN_ID == null
                || "".equals(AllWeiboInfo.JDOption_OPEN_ID)) {
            Message msg = Message.obtain(mHandler,
                    LoginActivity.GET_LOGIN_PARAMS_FAIL);
            msg.sendToTarget();
        } else {
            new AsyncTask<Void, Void, ApiBack>() {
                ApiBack back = null;

                @Override
                protected ApiBack doInBackground(Void... arg0) {
                    // TODO Auto-generated method stub
                    try {
                        SharedPreferences sp = mContext.getSharedPreferences("UmengDeviceToken", Activity.MODE_PRIVATE);
                        back = ApiJsonParser.combineWeibo_New(
                                WeiboType.JDOption, AllWeiboInfo.JDOption_NICK,
                                AllWeiboInfo.JDOption_OPEN_ID, 1, sp.getString("device_token", ""));
                    } catch (ApiNetException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    return back;
                }

                @Override
                protected void onPostExecute(ApiBack back) {
                    // TODO Auto-generated method stub
                    super.onPostExecute(back);
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
                        Message msg = Message.obtain(mHandler, JDOPTION_login);
                        Bundle bundle = new Bundle();
                        bundle.putString("msg", back.getMsg());
                        msg.setData(bundle);
                        msg.sendToTarget();
//                        Toast.makeText(
//                                LoginActivity.this,
//                                getResources().getString(
//                                        R.string.sports_toast_login_succes),
//                                Toast.LENGTH_SHORT).show();
                    } else if (back.getFlag() == -1) {
                        Message msg = Message.obtain(mHandler,
                                LoginActivity.QQ_LOGIN_FIALED);
                        Bundle bundle = new Bundle();
                        bundle.putString("msg", back.getMsg());
                        msg.setData(bundle);
                        msg.sendToTarget();
                    } else if (-11 == back.getFlag()) {
                        Message msg = Message.obtain(mHandler,
                                LoginActivity.LOGIN_FAIL_DEVICE_DISABLE);
                        Bundle bundle = new Bundle();
                        bundle.putString("msg", getResources().getString(R.string.login_fail_device_disable));
                        msg.setData(bundle);
                        msg.sendToTarget();
                    }
                }

            }.execute();
        }
    }

    private void clearSaveLoginInfo() {
        device_disable_task = new TimerTask() {
            public void run() {
                SharedPreferences sp = getSharedPreferences("user_login_info", Context.MODE_PRIVATE);
                Editor edit = sp.edit();
                edit.remove("account").commit();

                Intent intent = new Intent(LoginActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                LoginActivity.this.startActivity(intent);
                android.os.Process.killProcess(android.os.Process.myPid());
            }
        };

        device_disable_timer = new Timer(true);
        device_disable_timer.schedule(device_disable_task, 3000);
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


    private void exitPopWindow(String message,final int position) {
        WindowManager.LayoutParams lp = this.getWindow()
                .getAttributes();
        lp.alpha = 0.3f;
        this.getWindow().setAttributes(lp);

        if (myWindow != null && myWindow.isShowing())
            return;
        LayoutInflater inflater = LayoutInflater.from(this);
        myView = (LinearLayout) inflater.inflate(R.layout.sports_dialog5, null);
        ((TextView) myView.findViewById(R.id.message)).setText(message);

        myView.findViewById(R.id.bt_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if(position == 1){
                    if (myWindow != null) {
                        myWindow.dismiss();
                    }
                    myView.setVisibility(View.GONE);
                    mPopMenuBack.setVisibility(View.GONE);
                }else{
                    Intent intent_outof_time = new Intent(mContext, LoginActivity.class);
                    intent_outof_time.putExtra("outTime",1);
                    startActivity(intent_outof_time);
                    finish();
                }
            }
        });
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        myWindow = new PopupWindow(myView, width - SportsApp.dip2px(70),
                RelativeLayout.LayoutParams.WRAP_CONTENT, true);
        ColorDrawable cd = new ColorDrawable(0x000000);
        myWindow.setBackgroundDrawable(cd);
        myWindow.setTouchable(true);
        myWindow.setOutsideTouchable(true);
        myWindow.setBackgroundDrawable(new BitmapDrawable());
        myWindow.update();
        myWindow.showAtLocation(mainLL, Gravity.CENTER, 0, 0);
        myWindow.setOnDismissListener(this);
        mPopMenuBack.setVisibility(View.VISIBLE);
    }
    @Override
    public void onDismiss() {
        // TODO Auto-generated method stub
        mPopMenuBack.setVisibility(View.GONE);
        WindowManager.LayoutParams lp = this.getWindow()
                .getAttributes();
        lp.alpha = 1f;
        this.getWindow().setAttributes(lp);
    }

}
