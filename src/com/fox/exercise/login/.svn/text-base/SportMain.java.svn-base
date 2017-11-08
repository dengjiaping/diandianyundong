package com.fox.exercise.login;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
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
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fox.exercise.AllWeiboInfo;
import com.fox.exercise.CurrentTimeList;
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
import com.tencent.connect.auth.QQToken;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import cn.ingenic.indroidsync.SportsApp;

/**
 * @author loujungang 信息加载页面
 */
public class SportMain extends Activity implements View.OnClickListener,PopupWindow.OnDismissListener {
    String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    private static final String TAG = "SportMain";

    Intent intent = null;
    //	private Toast toastnet = null;
    public static String user_name = "";
    //	private String mBirthday = "";
    private String openId = "";
    private String mSex = "";
    Context mContext;
    SportsApp mSportsApp;
    SportAction mSportAction;

    // 用来区分登陆是否经过主界面跳入
    public static final String FROM_MAIN_KEY = "isFromMain";

    // 初始化屏幕长宽
    public static int ScreenWidth = 0;
    public static int ScreenHeight = 0;

    // 隐藏登陆按钮
    // private LinearLayout mVisibility = null;
    private LinearLayout mLayout = null;
    // private LinearLayout mtongbu;

    // 登陆进度框
    private Dialog loginPregressDialog = null;

    // 第三方auto权限登陆状态（通过网络数据加载登陆，默认状态失败）
    public static boolean isAutoLogin = false;

    public static final int LOGIN = 9;

    public static final int LOGIN_FAIL = 4;
    public static final int LOGIN_FAIL_NOT_NET = 111;
    public static final int LOGIN_FAIL_NOTOTHER_NET = 112;
    public static final int LOGIN_FAIL_OUTTIME = 113;
    public static final int LOGIN_FAIL_NOCHECK_WIFI = 114;
    public static final int LOADING_DIALOG = 5;
    public static final int LOGIN_FAIL_DEVICE_DISABLE = 11;
    public static final int SINA_LOADING = 8;
    public static final int LOGIN_SUCCESS = 6;

    // private Button mLoginBtn = null;

    // 监听网络工作状态
    private BroadcastReceiver mNetworkStateIntentReceiver;

    // 默认没有网络任务
    public static boolean mNoNetwork = false;

    // 微博初始化

    // 应用程序是否停止工作
    public static boolean mIsStop = false;

    // 网络是否链接工作
    private boolean mIsNetWork = true;

    // 加载数据线程
    private LoadDataThread mLoadDataThread = null;

    // 线程SESSION_OUT和NET_ERROR的解决所调用的接口申明
    private SportsExceptionHandler mExceptionHandler = null;

    // 主线程
    private MyHandler handler = null;
    // 加载进度框
    private ProgressBar proBarset;
    private TextView textViewset;
    private SharedPreferences sp;
    private String type;
    private Dialog alertDialog;

    private PopupWindow myWindow = null;
    private LinearLayout myView;
    private RelativeLayout mPopMenuBack, mainLL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boolean findMethod = SmartBarUtils.findActionBarTabsShowAtBottom();
        if (!findMethod) {
            // 取消ActionBar拆分，换用TabHost
            getWindow().setUiOptions(0);
            getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }
        setContentView(R.layout.main);
        mIsStop = false;
        // mtongbu=(LinearLayout)findViewById(R.id.main_tongbu_layout);
        // Log.e(TAG, "时间分割线----------------------1");
        initWeiboInfo();
        mContext = this;
        handler = new MyHandler();
        mSportsApp = (SportsApp) getApplication();
        String openId = getIntent().getStringExtra("openid");
        if (mSportsApp.isHomeKey && TextUtils.isEmpty(openId)) {
            mSportsApp.isHomeKey = false;
            if (!MainFragmentActivity.mErrorExist) {
                finish();
                return;
            }
        }
        proBarset = (ProgressBar) findViewById(R.id.loading_process_dialog_progressBar);
        textViewset = (TextView) findViewById(R.id.loading_title_textview);
        mainLL = (RelativeLayout) findViewById(R.id.rl);
        mPopMenuBack = (RelativeLayout) findViewById(R.id.set_menu_background);
        if (mSportsApp != null) {
            mExceptionHandler = mSportsApp.getmExceptionHandler();
            mSportsApp.setSportMain(SportMain.this);
        }
        // int config_manager =
        // getResources().getInteger(R.integer.config_withsync);
        // if (config_manager==0) {
        // mtongbu.setVisibility(View.GONE);
        // } else {
        // mtongbu.setVisibility(View.VISIBLE);
        // }

        // int config =
        // getResources().getInteger(R.integer.config_enter_sports);
        // if (config == 1) {
        // //开启服务，默认没有sessionid
        // /*Intent service = new Intent(mContext, SportService.class);
        // service.putExtra(SportAction.SEESION_ID_KEY, "");
        // service.putExtra(SportAction.IS_ADMIN_KEY, false);
        // startService(service);*/
        // //版本修改，创建窗口快捷图标
        // boolean isVersonUpdate =
        // StatCounts.checkVersonUpdate(SportMain.this);
        // Log.d(TAG, "isVersonUpdate:" + isVersonUpdate);
        // int shortcut_config =
        // getResources().getInteger(R.integer.config_create_shortcut);
        // if (isVersonUpdate)
        // {
        // if (shortcut_config == 1) {
        // createDeskShortCut();
        // }
        // }
        // CheckToken check = new CheckToken(this);
        // check.start();
        // }
        // UpdateApp();
        // 获取窗体的宽高
        DisplayMetrics mertics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(mertics);
        ScreenHeight = mertics.heightPixels;
        ScreenWidth = mertics.widthPixels;
        SportsApp.ScreenWidth = ScreenWidth;
        SportsApp.ScreenHeight = ScreenHeight;
        sp = getSharedPreferences("user_login_info", Context.MODE_PRIVATE);
        // mVisibility = (LinearLayout) findViewById(R.id.main);
        mLayout = (LinearLayout) findViewById(R.id.main_dialog);
        // mLoginBtn = (Button) findViewById(R.id.main_login);
        // mLoginBtn.setOnClickListener(this);
        // findViewById(R.id.main_xinlang).setOnClickListener(this);
        // findViewById(R.id.main_tengxun).setOnClickListener(this);
        // findViewById(R.id.main_tongbu).setOnClickListener(this);
        if (findMethod) {
            getActionBar().setDisplayShowHomeEnabled(false);
            getActionBar().setDisplayShowTitleEnabled(false);
            SmartBarUtils.setActionModeHeaderHidden(getActionBar(), true);
            SmartBarUtils.setActionBarViewCollapsable(getActionBar(), true);
        }
        type = sp.getString("weibotype", "");
        if (!TextUtils.isEmpty(openId)) {
            Log.i(TAG, "isFromQQ---");
            // String accesstoken=getIntent().getStringExtra("accesstoken");
            // String
            // accesstokenexpiretime=getIntent().getStringExtra("accesstokenexpiretime");
            // AllWeiboInfo.TENCENT_QQZONE_OPEN_ID = openId;
            // AllWeiboInfo.TENCENT_QQZONE_TOKEN_URL_KEY =accesstoken;
            // AllWeiboInfo.TENCENT_QQZONE_EXPIRES_URL_KEY
            // =accesstokenexpiretime;
            if ("".equals(sp.getString("account", ""))) { // 如果未登录过，则主动唤起QQ登录SDK的登录接口，实现自动登录；
                Tencent mTencent = Tencent.createInstance(AllWeiboInfo.APP_ID,
                        getApplicationContext());
                mTencent.login(this, "all", new BaseUiListener(mTencent));
            } else if (WeiboType.QQzone.equals(type)) {
                if (openId.equals(sp.getString(
                        AllWeiboInfo.TENCENT_QQZONE_OPEN_ID_KEY, "")))
                    LoadData();
                else
                    showqqDialog(getString(R.string.exchange),
                            getString(R.string.chang_qq_msg));
            } else
                showqqDialog(getString(R.string.qq_login),
                        getString(R.string.use_qq_msg));
        } else
            LoadData();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mIsStop = false;
        IntentFilter mNetworkStateChangedFilter = new IntentFilter();
        // 监听网络状态
        mNetworkStateChangedFilter
                .addAction(ConnectivityManager.CONNECTIVITY_ACTION);

        mNetworkStateIntentReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(
                        ConnectivityManager.CONNECTIVITY_ACTION)) {
                    // 判断网络是否不工作
                    mNoNetwork = intent.getBooleanExtra(
                            ConnectivityManager.EXTRA_NO_CONNECTIVITY, false);
                    if (mNoNetwork) {
                        // 清处用户访问SessionId
                        // mSportsApp.setSessionId("");
                        mIsNetWork = false;
                        // mVisibility.setVisibility(View.VISIBLE);
                        mLayout.setVisibility(View.GONE);
                    }
                }
            }
        };
        registerReceiver(mNetworkStateIntentReceiver,
                mNetworkStateChangedFilter);
        Log.d(TAG, "mNetworkStateIntentReceiver");
    }

    public void showqqDialog(String btnStr, String content) {
        // TODO Auto-generated method stub
        mLayout.setVisibility(View.GONE);
        alertDialog = new Dialog(this, R.style.sports_coins_dialog);
        LayoutInflater mInflater = getLayoutInflater();
        View v = mInflater.inflate(R.layout.from_qq_dialog, null);
        Button ok = (Button) v.findViewById(R.id.bt_ok);
        ok.setText(btnStr);
        ok.setOnClickListener(this);
        v.findViewById(R.id.bt_cancel).setOnClickListener(this);
        TextView message = (TextView) v.findViewById(R.id.message);
        message.setText(content);
        v.setMinimumWidth((int) (SportsApp.ScreenWidth * 0.8));
        alertDialog.setCancelable(true);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setContentView(v);
        alertDialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("SportMain");
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("SportMain");
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mIsStop = true;
        Log.d(TAG, "onStop");
        if (mNetworkStateIntentReceiver != null)
            unregisterReceiver(mNetworkStateIntentReceiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onDestroy");
        // mVisibility.setVisibility(View.VISIBLE);
        if (mLayout != null)
            mLayout.setVisibility(View.GONE);
        // if (broadcastReceiver != null)
        // unregisterReceiver(broadcastReceiver);
        // stopService(new Intent(this,SportService.class));
        mSportsApp = null;
    }

    // 没有sessionId的情况下自动加载本地数据
    private void LoadData() {
        Log.d(TAG, "load data");
        mLoadDataThread = new LoadDataThread();
        mLoadDataThread.start();

        SharedPreferences autoSp = getSharedPreferences(LocationInfo.spName,
                Context.MODE_PRIVATE);
        if (autoSp.getBoolean(LocationInfo.spAuto, false)) {
            Log.d(TAG, "uploadLocatiton");
            isAutoLogin = true;
        }
    }

    // 登陆方式一：普通用户登录【加载数据，获取登陆信息】
    class LoadDataThread extends Thread {
        @Override
        public void run() {
            // 如果有用户登录数据，则暂停登录按钮触发事件
            if (!"".equals(sp.getString("account", ""))) {
                Message.obtain(handler, LOADING_DIALOG).sendToTarget();
            }
            if (type.equals("")) {
                ApiBack back = null;
                SharedPreferences sp_umeng = getSharedPreferences("UmengDeviceToken", Activity.MODE_PRIVATE);
                try {
                    // 获取登陆的返回值
                    // Log.e(TAG, "时间分割线----------------------3准备登录");
                    back = ApiJsonParser.login(sp.getString("account", ""),
                            sp.getString("pwd", ""), 1, sp_umeng.getString("device_token", ""));
                } catch (ApiNetException e) {
                    e.printStackTrace();
                    Message.obtain(handler, LOGIN_FAIL_NOT_NET).sendToTarget();
                    Log.e(TAG, "从A发出间接没网");
                    return;
                }
                if (back.getMsg() == null) {
                    Message.obtain(handler, LOGIN_FAIL_NOT_NET).sendToTarget();
                    return;
                } else if (back == null || back.getFlag() == -56) {
                    Message.obtain(handler, LOGIN_FAIL_OUTTIME).sendToTarget();
                    return;
                } else if (back == null || back.getFlag() == -1) {
                    Message.obtain(handler, LOGIN_FAIL).sendToTarget();
                    Log.e(TAG, "从B发出间接没网");
                    return;
                }else if (back == null || back.getFlag() == -100) {
                    Message.obtain(handler, LOGIN_FAIL_NOCHECK_WIFI).sendToTarget();
                    Log.e(TAG, "WIFI没有认证");
                    return;
                }  else if (-11 == back.getFlag()) {
                    Message.obtain(handler, LOGIN_FAIL_DEVICE_DISABLE).sendToTarget();
                    return;
                } else {
                    // Log.e(TAG, "时间分割线----------------------4登录完成");
                    if (back.getFlag() == 1) {
                        SportsApp.mIsAdmin = true;
                    } else {
                        SportsApp.mIsAdmin = false;
                    }
                    Log.e(TAG, "loginsuccess");
                    // 获取用户访问ID
                    mSportsApp.setSessionId(back.getMsg().substring(7));
                    mSportsApp.setLogin(true);
                    //永数统计用户中的普通户用登陆,传入登录用户的唯一标识。
                    // Message msg = Message.obtain(handler, LOGIN);
                    // Bundle b = new Bundle();
                    // b.putBoolean("bylogin", true);
                    // msg.setData(b);
                    // msg.sendToTarget();
                }
            } else {
                // 使用第二种登录方式登录
                int flag = tryOthertoLogin(type, sp.getString("account", ""),
                        SportMain.this);
                if (flag == -1) {
                    Message.obtain(handler, LOGIN_FAIL).sendToTarget();
                    return;
                } else if (flag == -10) {
                    // 第三方登录去重副名字
                    if (mSportsApp.getSessionId() == null
                            || "".equals(mSportsApp.getSessionId())) {
                        return;
                    } else {
                        Message.obtain(handler, 103).sendToTarget();
                        return;
                    }

                } else if (flag == -2) {
                    Message.obtain(handler, LOGIN_FAIL_NOTOTHER_NET).sendToTarget();
                    return;
                } else if (flag == -11) {
                    Message.obtain(handler, LOGIN_FAIL_DEVICE_DISABLE).sendToTarget();
                    return;
                } else if (flag == -56) {
                    Message.obtain(handler, LOGIN_FAIL_OUTTIME).sendToTarget();
                    return;
                } else if (flag == -101) {
                    Message.obtain(handler, LOGIN_FAIL_NOCHECK_WIFI).sendToTarget();
                    return;
                }
            }
            if (mSportsApp.getSessionId() == null
                    || "".equals(mSportsApp.getSessionId()))
                return;
            try {
                // Log.e(TAG, "时间分割线----------------------5准备刷新");
                UserDetail detail = ApiJsonParser.refreshRank(mSportsApp
                        .getSessionId());
                if (detail != null) {
//					if (detail.getUname() != null
//							&& !"".equals(detail.getUname())) {
//						if (judgeNum(detail.getUname())) {
////							Intent intent1 = new Intent(SportMain.this,
////									PerfectMainActivity.class);
////							intent1.putExtra("comeFrom", "SportMain");
////							startActivityForResult(intent1, 11);
//							Message.obtain(handler, 103).sendToTarget();
//							return;
//						}
//					}
                    mSportsApp.setSportUser(detail);
                    // Log.e(TAG, "时间分割线----------------------6刷新完成");
                    // Message msg = Message.obtain(handler, LOGIN);
                    // Bundle b = new Bundle();
                    // b.putBoolean("bylogin", true);
                    // msg.setData(b);
                    // msg.sendToTarget();
                } else {
                    Message.obtain(handler, LOGIN_FAIL).sendToTarget();
                    return;
                }
            } catch (ApiNetException e) {
                e.printStackTrace();
                Log.e(TAG, "ApiNetException");
                // Message.obtain(mExceptionHandler,
                // SportsExceptionHandler.NET_ERROR).sendToTarget();
                Message.obtain(handler, LOGIN_FAIL_NOT_NET).sendToTarget();
            } catch (ApiSessionOutException e) {
                e.printStackTrace();
            }
            // List<CurrentTimeList> lst=null;
            // try {
            // Log.e(TAG,
            // "mSportsApp.getSessionId()-->"+mSportsApp.getSessionId()+"mSportsApp.getSportUser().getUid()-->"+mSportsApp.getSportUser().getUid());
            // lst = ApiJsonParser.getCurrentTimeById(mSportsApp.getSessionId(),
            // mSportsApp.getSportUser().getUid());
            // if(lst!=null&&lst.size()>0)
            // {
            // SharedPreferences.Editor
            // editor=getSharedPreferences("CurrentTimes" +
            // SportsApp.getInstance().getSportUser().getUid(),
            // MODE_WORLD_WRITEABLE).edit();
            // editor.clear();
            // if(date.equals(lst.get(0).getCurrentTime())){
            // editor.putInt("size", lst.size());
            // for(int i=0;i<lst.size();i++){
            // editor.putString("time_"+i,lst.get(i).getCurrentTime());
            // }
            // StateActivity.mHasPlan = true;
            // }else{
            // editor.putInt("size", (lst.size()+1));
            // for(int i=0;i<=lst.size();i++){
            // if(i==0){
            // editor.putString("time_"+i,date);
            // }else{
            // editor.putString("time_"+i,lst.get(i-1).getCurrentTime());
            // }
            // }
            // StateActivity.mHasPlan = false;
            // }
            // editor.commit();
            // }
            // } catch (ApiNetException e) {
            // e.printStackTrace();
            // Log.e(TAG, "ApiNetException");
            // Message.obtain(mExceptionHandler,
            // SportsExceptionHandler.NET_ERROR).sendToTarget();
            // Message.obtain(handler, LOGIN_FAIL).sendToTarget();
            // } catch (ApiSessionOutException e) {
            // e.printStackTrace();
            // }
            Message msg = Message.obtain(handler, LOGIN);
            Bundle b = new Bundle();
            b.putBoolean("bylogin", true);
            msg.setData(b);
            msg.sendToTarget();
        }
    }

    // 登陆方式二：微博用户登陆【通过新浪和腾讯来登陆(WEIBO_LOGIN步骤同上)】
    private int tryOthertoLogin(String weiboType, String weiboName, Context con) {
        if (Tools.isNetworkConnected(con)) {
            if (weiboType.equals("") || weiboName.equals("")) {
                return -1;
            }
            try {
                String openid = null;
                SharedPreferences sp = getSharedPreferences(
                        AllWeiboInfo.LOGIN_INFO, Context.MODE_PRIVATE);
                openid = sp.getString(AllWeiboInfo.LOGIN_INFO_OPEN_ID_KEY, "");
                Log.e(TAG, "weiboType:" + weiboType);
                Log.e(TAG, "weiboName:" + weiboName);
                Log.e(TAG, "openid:" + openid);
                if (weiboType == null || weiboName == null || openid == null
                        || "".equals(weiboType) || "".equals(weiboName)
                        || "".equals(openid)) {
                    sp.edit().clear().commit();
                    return -1;
                }
                SharedPreferences sp_umeng = mContext.getSharedPreferences("UmengDeviceToken", Activity.MODE_PRIVATE);
                ApiBack message = ApiJsonParser.combineWeibo_New(weiboType,
                        weiboName, openid, 1, sp_umeng.getString("device_token", ""));
                if (message == null) {
                    return -1;
                } else if (message.getFlag() == 0 || message.getFlag() == 1) {
                    if (message.getFlag() == 1) {
                        SportsApp.mIsAdmin = true;
                    } else {
                        SportsApp.mIsAdmin = false;
                    }
                    if (message.getMsg() != null && !"".equals(message.getMsg())) {
                        mSportsApp.setSessionId(message.getMsg().substring(7));
                        mSportsApp.setLogin(true);
                        return message.getFlag();
                    }  else {
                        //表示虽然链接网络了但是没有真的链接上 无法上网
                        return -2;
                    }

                } else if (message.getFlag() == -10) {
                    mSportsApp.setSessionId(message.getMsg().substring(7));
                    return -10;
                } else if (message.getFlag() == -11) {
                    Log.e("develop_debug", "SportMain.java 598");
//                    Toast.makeText(mContext,
//                                   getResources().getString(R.string.login_fail_device_disable),
//                                   Toast.LENGTH_LONG).show();
                    return -11;
                }  else if (message.getFlag() == -56) {
                    return -56;
                } else if (message.getFlag() == -101) {
                    return -101;
                }  else {
                    Log.e(TAG, message.getMsg());
                    Toast.makeText(mContext, message.getMsg(),
                            Toast.LENGTH_SHORT).show();
                    return -1;
                }
            } catch (ApiNetException e) {
                e.printStackTrace();
                return -1;
            }
        } else {
            return -1;
        }
    }

    // 登陆状态的主线程
    private class MyHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            // 测试当前线程是否已经中断,如果当前线程已经中断，则返回 true；否则返回 false【线程的中断状态
            // 由该方法（isInterrupted()）清除】
            if (!Thread.currentThread().isInterrupted()) {
                switch (msg.what) {
                    case LOGIN:
//                        Toast.makeText(
//                                mContext,
//                                mContext.getResources().getString(
//                                        R.string.sports_toast_login_succes),
//                                Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "LoginActivity.mTabActivityHasStart:"
                                + LoginActivity.mTabActivityHasStart);
                        Log.e(TAG, "mSportsApp.isLogin():" + mSportsApp.isLogin());
                        if (!LoginActivity.mTabActivityHasStart
                                && mSportsApp.isLogin()) {
                            if (mSportsApp.getSessionId() == null
                                    || "".equals(mSportsApp.getSessionId())
                                    || mSportsApp.getSportUser() == null) {
                                mSportsApp.setLogin(false);
                                Message.obtain(handler, LOGIN_FAIL).sendToTarget();
                                return;
                            }
                            Intent broadIntent = new Intent();
                            broadIntent.setAction(SportAction.LOGIN_ACTION);
                            broadIntent.putExtra(SportAction.SEESION_ID_KEY,
                                    mSportsApp.getSessionId());
                            broadIntent.putExtra(SportAction.IS_ADMIN_KEY,
                                    SportsApp.mIsAdmin);
                            sendBroadcast(broadIntent);
                            if (loginPregressDialog != null) {
                                loginPregressDialog.dismiss();
                                // mVisibility.setVisibility(View.VISIBLE);
                                mLayout.setVisibility(View.GONE);
                            }
                            LocalBroadcastManager localBroadcastManager = LocalBroadcastManager
                                    .getInstance(mContext);
                            final IntentFilter intentFilter = new IntentFilter();
                            // intentFilter.addAction(Intent.ACTION_TIME_TICK);
                            // intentFilter.addAction(Intent.ACTION_TIME_CHANGED);
                            intentFilter.addAction(SportAction.DETAIL_ACTION);
                            intentFilter
                                    .addAction(SportAction.UPDATE_TAB_MSG_ACTION);
                            localBroadcastManager.registerReceiver(
                                    SportsLocalBroadcastReceiver.getInstance(),
                                    intentFilter);
                            LoginActivity.mTabActivityHasStart = true;

                            // SharedPreferences sharedPreferences =
                            // getSharedPreferences("sports"
                            // + mSportsApp.getSportUser().getUid(),
                            // Context.MODE_PRIVATE);
                            // if(sharedPreferences.getBoolean("isStorage", false))
                            // {
                            // Log.e(TAG, "时间分割线----------------------7准备跳转");

                            // }else{
                            // startActivity(new Intent(SportMain.this,
                            // FoxSportsNavigation.class));
                            // }
                            Handler mHandler = mSportsApp.getmHandler();
                            if (mHandler != null) {
                                mHandler.sendMessage(mHandler
                                        .obtainMessage(StateActivity.UPDATE_TRYTOLOGIN));
                            }
                        /*
                         * Handler fHandler = mSportsApp.getfHandler(); if
						 * (fHandler!=null) {
						 * fHandler.sendMessage(fHandler.obtainMessage(
						 * FoxSportsSetting.UPDATE_TRYTOLOGIN)); }
						 */
                            Handler mainHandler = mSportsApp.getMainHandler();
                            if (mainHandler != null) {
                                mainHandler
                                        .sendMessage(mainHandler
                                                .obtainMessage(MainFragmentActivity.UPDATE_TRYTOLOGIN));
                            }
                            Message.obtain(handler, LOGIN_SUCCESS).sendToTarget();
                        } else {
                            finish();
                        }
                        break;
                    case LOGIN_FAIL_NOT_NET:
                        Toast.makeText(mContext, R.string.error_cannot_access_net,
                                Toast.LENGTH_SHORT).show();
                        mSportsApp.LoginOption = true;
                        mSportsApp.LoginNet = false;
                        mSportsApp.getLocalData();
                        Log.e(TAG, "直接没网------------");
                        if (mSportsApp.getSessionId() == null
                                || "".equals(mSportsApp.getSessionId())) {
                            SharedPreferences sp = getSharedPreferences(
                                    "user_login_info", Context.MODE_PRIVATE);
                            mSportsApp.setSessionId(sp.getString("sessionid", ""));
                        }

                        startActivity(new Intent(SportMain.this,
                                MainFragmentActivity.class));
                        finish();
                        break;
                    case LOGIN_FAIL_OUTTIME:
//                        Toast.makeText(mContext,
//                                R.string.socket_outof_time,
//                                Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(SportMain.this,LoginActivity.class);
                        intent.putExtra("outTime",1);
                        startActivity(intent);
//                        没网自动登录弹出的弹出框
//                        Runnable mRunnable = new Runnable() {
//                            public void run() {
//                                // 弹出PopupWindow的具体代码
//                                exitPopWindow(mContext.getResources().getString(R.string.socket_outof_time),2);
//                            }
//                        };
                        break;
                    case LOGIN_FAIL_NOCHECK_WIFI:
//                        Toast.makeText(mContext,
//                                R.string.checkyour_wifi_isconnect,
//                                Toast.LENGTH_LONG).show();
                        Intent intent_checkWifi = new Intent(SportMain.this,LoginActivity.class);
                        intent_checkWifi.putExtra("outTime",1);
                        startActivity(intent_checkWifi);
                        finish();
//                        Runnable mRunnable2 = new Runnable() {
//                            public void run() {
//                                // 弹出PopupWindow的具体代码
//                                exitPopWindow(mContext.getResources().getString(R.string.socket_outof_time),2);
//                            }
//                        };
                        break;
                    case LOGIN_FAIL:
                        proBarset.setVisibility(View.INVISIBLE);
                        textViewset.setText(R.string.acess_server_error);
                        if (!mIsNetWork) {
                            Toast.makeText(mContext,
                                    R.string.error_cannot_access_net, Toast.LENGTH_SHORT).show();
                            mSportsApp.LoginOption = true;
                            mSportsApp.LoginNet = false;
                            mSportsApp.getLocalData();
                            if (mSportsApp.getSessionId() == null
                                    || "".equals(mSportsApp.getSessionId())) {
                                SharedPreferences sp = getSharedPreferences(
                                        "user_login_info", Context.MODE_PRIVATE);
                                mSportsApp.setSessionId(sp.getString("sessionid",
                                        ""));
                            }
                            startActivity(new Intent(SportMain.this,
                                    MainFragmentActivity.class));
                            finish();
                            break;
                        }
                        getSharedPreferences(AllWeiboInfo.LOGIN_INFO, MODE_PRIVATE)
                                .edit().clear().commit();
                        Toast.makeText(
                                mContext,
                                getResources().getString(
                                        R.string.sports_auto_login_fail),
                                Toast.LENGTH_SHORT).show();
                        if (loginPregressDialog != null) {
                            loginPregressDialog.dismiss();
                            // mVisibility.setVisibility(View.VISIBLE);
                            mLayout.setVisibility(View.GONE);
                        }
                        // mLoginBtn.setOnClickListener(new OnClickListener()
                        // {
                        // @Override
                        // public void onClick(View arg0)
                        // {
                        // startActivity(new Intent(SportMain.this,
                        // LoginActivity.class));
                        // }
                        // });
                        break;
                    case LOGIN_FAIL_DEVICE_DISABLE:
                        Log.e("develop_debug", "SportMain.java 767");
                        proBarset.setVisibility(View.INVISIBLE);
                        textViewset.setText(getResources().getString(R.string.login_fail_device_disable));
                        getSharedPreferences(AllWeiboInfo.LOGIN_INFO, MODE_PRIVATE)
                                .edit().clear().commit();
                        Toast.makeText(mContext,
                                       getResources().getString(R.string.login_fail_device_disable),
                                       Toast.LENGTH_LONG).show();
                        if (loginPregressDialog != null) {
                            loginPregressDialog.dismiss();
                            // mVisibility.setVisibility(View.VISIBLE);
                            mLayout.setVisibility(View.GONE);
                        }
                        break;
                    case LOGIN_FAIL_NOTOTHER_NET:
                        proBarset.setVisibility(View.INVISIBLE);
                        textViewset.setText(R.string.acess_server_error);
                        Toast.makeText(mContext,
                                R.string.error_cannot_access_net, Toast.LENGTH_LONG).show();
                        mSportsApp.LoginOption = true;
                        mSportsApp.LoginNet = false;
                        mSportsApp.getLocalData();
                        if (mSportsApp.getSessionId() == null
                                || "".equals(mSportsApp.getSessionId())) {
                            SharedPreferences sp = getSharedPreferences(
                                    "user_login_info", Context.MODE_PRIVATE);
                            mSportsApp.setSessionId(sp.getString("sessionid",
                                    ""));
                        }
                        Log.e(TAG, "间接没网-------------");
                        startActivity(new Intent(SportMain.this,
                                MainFragmentActivity.class));
                        finish();
                        break;
                    case SINA_LOADING:
                        String response = (String) msg.obj;
                        Log.e(TAG, "response:" + response.toString());
                        try {
                            JSONObject js = new JSONObject(response);
                            openId = "" + js.getLong("id");
                            user_name = js.getString("screen_name");
                            Log.e(TAG, "user_name" + user_name);
                            Log.e(TAG, "js:" + js);
                            if ("m".equals(js.getString("gender")))
                                mSex = "man";
                            else if ("f".equals(js.getString("gender")))
                                mSex = "woman";
                            Message.obtain(handler, LOADING_DIALOG).sendToTarget();
                            new AsyncTask<Void, Void, ApiBack>() {
                                @Override
                                protected ApiBack doInBackground(Void... params) {
                                    Log.e(TAG, "doInBackground");
                                    ApiBack back = null;
                                    try {
                                        Log.e(TAG, "WeiboType.SinaWeibo:"
                                                + WeiboType.SinaWeibo
                                                + "#user_name:" + user_name
                                                + "#openId:" + openId);
                                        if (user_name == null
                                                || "".equals(user_name)
                                                || openId == null
                                                || "".equals(openId)) {
                                            Message.obtain(handler, LOADING_DIALOG)
                                                    .sendToTarget();
                                            return null;
                                        }
                                        SharedPreferences sp_umeng = mContext.getSharedPreferences("UmengDeviceToken", Activity.MODE_PRIVATE);
                                        back = ApiJsonParser.combineWeibo_New(
                                                ApiConstant.WeiboType.SinaWeibo,
                                                user_name, openId, 1, sp_umeng.getString("device_token", ""));
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
                                        mLayout.setVisibility(View.GONE);
                                        // mVisibility.setVisibility(View.VISIBLE);
                                        Toast.makeText(
                                                SportMain.this,
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
                                            Log.e(TAG, "loginsuccess");
                                            mSportsApp.setSessionId(message
                                                    .getMsg().substring(7));
                                            mSportsApp.setLogin(true);
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

//                                            Toast.makeText(
//                                                    SportMain.this,
//                                                    getResources()
//                                                            .getString(
//                                                                    R.string.sports_toast_login_succes),
//                                                    Toast.LENGTH_SHORT).show();
                                            loginFinish();
                                        } else if (-11 == message.getFlag()) {
                                            Log.e("develop_debug", "SportMain.java 889");
                                            loginPregressDialog.dismiss();
                                            mLayout.setVisibility(View.GONE);
                                            Toast.makeText(SportMain.this,
                                                           getResources().getString(R.string.login_fail_device_disable),
                                                           Toast.LENGTH_LONG).show();
                                        } else {
                                            Log.e(TAG, "loginfailed");
                                            if (message.getMsg().equals(
                                                    "accountOrPwdError")) {
                                                Toast.makeText(
                                                        SportMain.this,
                                                        getResources()
                                                                .getString(
                                                                        R.string.sports_toast_nameOrPwdError),
                                                        Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(
                                                        SportMain.this,
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
                    case LOADING_DIALOG:
                        // mVisibility.setVisibility(View.GONE);
                        mLayout.setVisibility(View.VISIBLE);
                        if (loginPregressDialog != null) {
                            if (!loginPregressDialog.isShowing()) {
                                loginPregressDialog.show();
                            }
                        }
                        break;
                    case LOGIN_SUCCESS:
                        mSportsApp.LoginNet = true;
                        startActivity(new Intent(SportMain.this,
                                MainFragmentActivity.class));
                        // if (!mIsStop) {
                        finish();
                        // }
                        break;
                    case 2:
//                        Toast.makeText(
//                                SportMain.this,
//                                getResources().getString(
//                                        R.string.sports_toast_login_succes),
//                                Toast.LENGTH_SHORT).show();
                        SharedPreferences sp = getSharedPreferences(
                                "user_login_info", Context.MODE_PRIVATE);

                        Editor e = sp.edit();
                        e.putString(AllWeiboInfo.TENCENT_QQZONE_OPEN_ID_KEY,
                                AllWeiboInfo.TENCENT_QQZONE_OPEN_ID);
                        e.putString("account", AllWeiboInfo.TENCENT_QQZONE_NICK);
                        e.putString("weibotype", "qqzone");
                        e.putString("access_token",
                                AllWeiboInfo.TENCENT_QQZONE_TOKEN_URL_KEY);
                        e.putInt("login_way", 1);
                        e.commit();

                        Log.e(TAG, "loginsuccess");
                        String sessionid = msg.getData().getString("msg")
                                .substring(7);
                        Log.e(TAG, "session_id" + sessionid);
                        mSportsApp.setSessionId(sessionid);
                        mSportsApp.setLogin(true);
                        Log.d(TAG,
                                "BestGirlApp.sessionId:"
                                        + mSportsApp.getSessionId());

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
                    case 101:
                    case 102:
                        // 第三方登录重名的问题 修改重名问题
                        SharedPreferences sp1 = getSharedPreferences(
                                "user_login_info", Context.MODE_PRIVATE);

                        Editor e1 = sp1.edit();
                        e1.putString(AllWeiboInfo.TENCENT_QQZONE_OPEN_ID_KEY,
                                AllWeiboInfo.TENCENT_QQZONE_OPEN_ID);
                        e1.putString("account", AllWeiboInfo.TENCENT_QQZONE_NICK);
                        e1.putString("weibotype", "qqzone");
                        e1.putString("access_token",
                                AllWeiboInfo.TENCENT_QQZONE_TOKEN_URL_KEY);
                        e1.putInt("login_way", 1);
                        e1.commit();

                        Log.e(TAG, "loginsuccess");
                        String sessionid1 = msg.getData().getString("msg")
                                .substring(7);
                        Log.e(TAG, "session_id" + sessionid1);
                        mSportsApp.setSessionId(sessionid1);
                        mSportsApp.setLogin(true);
                        Log.d(TAG,
                                "BestGirlApp.sessionId:"
                                        + mSportsApp.getSessionId());

                        SharedPreferences tSp1 = getSharedPreferences(
                                AllWeiboInfo.TENCENT_QQZONE_TOKEN_SP, MODE_PRIVATE);
                        Editor te1 = tSp1.edit();
                        te1.putString(AllWeiboInfo.TENCENT_QQZONE_TOKEN_KEY,
                                AllWeiboInfo.TENCENT_QQZONE_TOKEN);
                        te1.putString(AllWeiboInfo.TENCENT_QQZONE_OPEN_ID_KEY,
                                AllWeiboInfo.TENCENT_QQZONE_OPEN_ID);
                        te1.putLong(AllWeiboInfo.TENCENT_QQZONE_TOKEN_SP_TIME,
                                System.currentTimeMillis());
                        te1.commit();

                        Intent intent2 = new Intent(SportMain.this,
                                PerfectMainActivity.class);
                        intent2.putExtra("comeFrom", "SportMain");
                        startActivityForResult(intent2, 10);

                        break;
                    case 103:
                        //名字过长跳转到修改页面
                        Intent intent3 = new Intent(SportMain.this,
                                PerfectMainActivity.class);
                        intent3.putExtra("comeFrom", "SportMain");
                        startActivityForResult(intent3, 11);
                        break;
                    default:
                        break;
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 10:

                break;
            case 11:
                new LoadDataThread().start();
                break;

            default:
                break;
        }
    }

    // 登陆成功
    private void loginFinish() {
        MainFragmentActivity.mAfterLogin = true;
        checkFirstLogin();
        (new LoginFinishTask()).execute();
    }

    class LoginFinishTask extends AsyncTask<Integer, Integer, UserDetail> {

        @Override
        protected UserDetail doInBackground(Integer... arg0) {
            UserDetail detail = null;
            // while (detail == null) {
            try {
                detail = ApiJsonParser.refreshRank(mSportsApp.getSessionId());
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
                startActivity(new Intent(mContext, SportMain.class));
                return null;
            }
            // }
            return detail;
        }

        @Override
        protected void onPostExecute(UserDetail result) {
            if (result != null) {
                mSportsApp.setSportUser(result);
                if (mSex != null) {
                    mSportsApp.getSportUser().setSex(mSex);
                } else {
                    mSportsApp.getSportUser().setSex("man");
                }
                if (loginPregressDialog != null
                        && !SportMain.this.isFinishing())
                    if (!loginPregressDialog.isShowing())
                        loginPregressDialog.show();
                if (mSportsApp.getSportMain() != null) {
                    mSportsApp.getSportMain().finish();
                }
                // SaveCurrentTimeThread thread = new SaveCurrentTimeThread();
                // thread.start();
                Log.d(TAG, "Uid:" + mSportsApp.getSportUser().getUid());
                // 存写用户ID
                Editor editor = getSharedPreferences("sprots_uid", 0).edit();
                editor.putInt("sportsUid", mSportsApp.getSportUser().getUid());
                editor.commit();
                Log.d(TAG, "mTabActivityHasStart:"
                        + LoginActivity.mTabActivityHasStart);
                Log.d(TAG, "mSportsApp.isLogin():" + mSportsApp.isLogin());
                if (!LoginActivity.mTabActivityHasStart && mSportsApp.isLogin()) {
                    LoginActivity.mTabActivityHasStart = true;
                    if (!UserEditActivity.mIsDoing) {
                        // SharedPreferences sharedPreferences =
                        // getSharedPreferences("sports"
                        // + mSportsApp.getSportUser().getUid(),
                        // Context.MODE_PRIVATE);
                        // if(sharedPreferences.getBoolean("isStorage", false))
                        // {
                        startActivity(new Intent(SportMain.this,
                                MainFragmentActivity.class));
                        // }else{
                        // startActivity(new Intent(SportMain.this,
                        // FoxSportsNavigation.class));
                        // }
                        registMsgBoxReceiver();
                        finish();
                        return;
                    }
                } else {
                    registMsgBoxReceiver();
                    finish();
                }

            }
            // else {
            // loginFinish();
            // }
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
            intentFilter.addAction(Intent.ACTION_TIME_TICK);
            intentFilter.addAction(Intent.ACTION_TIME_CHANGED);
            intentFilter.addAction(SportAction.DETAIL_ACTION);
            intentFilter.addAction(SportAction.UPDATE_TAB_MSG_ACTION);
            localBroadcastManager.registerReceiver(
                    SportsLocalBroadcastReceiver.getInstance(), intentFilter);
        }

    }

    private String getConfigPath() {
        return Environment.getExternalStorageDirectory().toString()
                + "/android/data/" + getPackageName() + "/market";
    }

    // 检查是否第一次登陆
    private void checkFirstLogin() {
        Log.d(TAG, "checkFirstLoginFromMarket");
        File file = new File(getConfigPath());
        boolean flag = false;
        if (!file.getParentFile().exists()) {
            flag = file.getParentFile().mkdirs();
            Log.d(TAG, "file.mkdirs():" + flag);
        }
        if (!file.exists()) {
            try {
                flag = file.createNewFile();
                Log.d(TAG, "file.createNewFile():" + flag);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        } else
            return;
        if (file.exists()) {
            Log.d(TAG, "file exists");
            if (file.length() == 0L
                    && getResources().getInteger(R.integer.coin_gift) == 1) {
                (new MarketTask()).execute();
                return;
            }
        }
    }

    class MarketTask extends AsyncTask<Integer, Integer, Integer> {
        @Override
        protected Integer doInBackground(Integer... arg0) {
            Log.e(TAG, "coinsGiftFromMarket");
            try {
                ApiBack back = ApiJsonParser.activities(
                        mSportsApp.getSessionId(),
                        Integer.parseInt(getResources().getString(
                                R.string.config_game_id)));
                return back.getFlag();
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
                startActivity(new Intent(SportMain.this, SportMain.class));
            }
            return -1;
        }
    }

    // 初始化微博信息
    private void initWeiboInfo() {
        SharedPreferences qqzoneSP = getSharedPreferences(
                AllWeiboInfo.TENCENT_QQZONE_TOKEN_SP, MODE_PRIVATE);
        AllWeiboInfo.TENCENT_QQZONE_OPEN_ID = qqzoneSP.getString(
                AllWeiboInfo.TENCENT_QQZONE_OPEN_ID_KEY, "");
        AllWeiboInfo.TENCENT_QQZONE_TOKEN = qqzoneSP.getString(
                AllWeiboInfo.TENCENT_QQZONE_TOKEN_KEY, "");
        // registerReceiver(broadcastReceiver, new
        // IntentFilter("com.weibo.techface.getTencent_verifier"));
    }

    // 创建桌面快捷图标
    // private void createDeskShortCut() {
    // Log.i("coder", "------createShortCut--------");
    // // 创建快捷方式的Intent
    // Intent shortcutIntent = new
    // Intent("com.android.launcher.action.INSTALL_SHORTCUT");
    // // 不允许重复创建
    // shortcutIntent.putExtra("duplicate", false);
    // // 需要现实的名称
    // shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME,
    // getString(R.string.sports_name));
    //
    // // 快捷图片
    // Parcelable icon = Intent.ShortcutIconResource
    // .fromContext(getApplicationContext(), R.drawable.icon);
    //
    // shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, icon);
    //
    // Intent intent = new Intent(getApplicationContext(), SportMain.class);
    // // 下面两个属性是为了当应用程序卸载时桌面 上的快捷方式会删除
    // intent.setAction("android.intent.action.MAIN");
    // intent.addCategory("android.intent.category.LAUNCHER");
    // // 点击快捷图片，运行的程序主入口
    // shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, intent);
    // // 发送广播。OK
    // sendBroadcast(shortcutIntent);
    // }
    //

    // 新浪微博登陆方法
//	private void gotoXinlangWeibo() {
//		// Oauth2.0
//		// 隐式授权认证方式
//		Weibo weibo = Weibo.getInstance();
//		weibo.setupConsumerConfig(WeiboConstParam.CONSUMER_KEY,
//				WeiboConstParam.CONSUMER_SECRET);
//		// 此处回调页内容应该替换为与appkey对应的应用回调页
//		weibo.setRedirectUrl(WeiboConstParam.REDIRECT_URL);
//		// 启动认证
//		weibo.authorize(SportMain.this, new AuthDialogListener());
//	}

    // 腾讯微博登陆方法
    // private void gotoTenxunWeibo() {
    // weibo = WeiboContext.getInstance();
    // new AsyncTask<Void, Void, Void>() {
    // @Override
    // protected Void doInBackground(Void... params) {
    // weibo.getRequestToken();
    // return null;
    // }
    //
    // @Override
    // protected void onPostExecute(Void void1) {
    // Intent intent = new Intent(SportMain.this,
    // TencentAuthorizeActivity.class);
    // Bundle bundle = new Bundle();
    // bundle.putString("url", weibo.getAuthorizeUrl());
    // intent.putExtras(bundle);
    // startActivity(intent);
    // }
    // }.execute();
    // }

    // 新浪微博登陆要调用的方法
    class AuthDialogListener implements WeiboDialogListener, RequestListener {

        private String uid = "";
        private String tokenString = "";

        @Override
        public void onComplete(Bundle values) {
            String token = values.getString("access_token");

            AuthoSharePreference.putToken(SportMain.this, token);

            tokenString = AuthoSharePreference.getToken(SportMain.this);

            AccessToken accessToken = new AccessToken(tokenString,
                    WeiboConstParam.CONSUMER_SECRET);
            Log.e(TAG, "accessToken:" + accessToken);
            final Weibo weibo = Weibo.getInstance();
            weibo.setAccessToken(accessToken);

            String expires_in = values.getString("expires_in");
            AuthoSharePreference.putExpires(SportMain.this, expires_in);

            String remind_in = values.getString("remind_in");
            AuthoSharePreference.putRemind(SportMain.this, remind_in);

            uid = values.getString("uid");
            Log.e(TAG, uid);
            AuthoSharePreference.putUid(SportMain.this, uid);
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
            Message.obtain(handler, LOADING_DIALOG).sendToTarget();
            WeiboParameters bundle = new WeiboParameters();
            Log.d(TAG, "token.toString():" + token.getRefreshToken());
            Log.d(TAG, "token.toString():" + token.getToken());
            bundle.add("access_token", token.getToken());
            bundle.add("uid", uid);
            String url = Weibo.SERVER + "users/show.json";
            AsyncWeiboRunner weiboRunner = new AsyncWeiboRunner(weibo);
            weiboRunner.request(getApplicationContext(), url, bundle,
                    Utility.HTTPMETHOD_GET, this);
        }

        @Override
        public void onComplete(String response) {
            Message msg = handler.obtainMessage(SINA_LOADING, response);
            handler.sendMessage(msg);

            Log.d(TAG, "response:" + response.toString());
            try {
                JSONObject js = new JSONObject(response);
                user_name = js.getString("screen_name");

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

    // //修改APP版本
    // private void UpdateApp()
    // {
    // Log.v(TAG, "UpdateApp in");
    // UpdataApplication updataApplication = new UpdataApplication(this);
    // try {
    // Log.v(TAG, "UpdateAppBackground begin");
    // updataApplication.UpdateAppBackground(true);
    // Log.v(TAG, "UpdateAppBackground end");
    // } catch (Exception e) {
    // e.printStackTrace();
    // Log.v(TAG, "UpdateAppBackground err");
    // }
    // StatCounts.transferApp(SportMain.this,
    // getString(R.string.config_game_id));
    // }

    // 退出程序
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK: {
                LoginActivity.mTabActivityHasStart = false;
                Intent intent = new Intent();
                intent.setAction("com.fox.exercise.exits");
                sendBroadcast(intent);
                finish();
                break;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View arg0) {
        switch (arg0.getId()) {
            case R.id.bt_ok:
                // mLayout.setVisibility(View.VISIBLE);
                alertDialog.cancel();
                String expiretime = getIntent().getStringExtra(
                        "accesstokenexpiretime");
                String token = getIntent().getStringExtra("accesstoken");
                String openId = getIntent().getStringExtra("openid");
                Tencent mTencent = Tencent.createInstance(AllWeiboInfo.APP_ID,
                        getApplicationContext());
                mTencent.setOpenId(openId);
                mTencent.setAccessToken(token, expiretime);
                mTencent.login(this, "all", new BaseUiListener(mTencent));
                Log.v(TAG, "tencent openId " + openId + " token+" + token);
                break;
            case R.id.bt_cancel:
                finish();
                break;
        }
    }

    private class BaseUiListener implements IUiListener {
        private Tencent mTencent;

        public BaseUiListener(Tencent mTencent) {
            // TODO Auto-generated constructor stub
            this.mTencent = mTencent;
        }

        @Override
        public void onComplete(Object response) {
            // TODO Auto-generated method stub
            mLayout.setVisibility(View.VISIBLE);
            if (loginPregressDialog != null)
                if (!loginPregressDialog.isShowing()) {
                    loginPregressDialog.show();
                }
            Toast.makeText(getApplicationContext(),
                    getResources().getString(R.string.get_power_sucess), Toast.LENGTH_SHORT)
                    .show();
            try {
                AllWeiboInfo.TENCENT_QQZONE_OPEN_ID = ((JSONObject) response)
                        .getString("openid");
                AllWeiboInfo.TENCENT_QQZONE_TOKEN_URL_KEY = ((JSONObject) response)
                        .getString("access_token");
                AllWeiboInfo.TENCENT_QQZONE_EXPIRES_URL_KEY = ((JSONObject) response)
                        .getString("expires_in");
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            Log.e(TAG, "------------" + response.toString());
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
                        Log.e(TAG, "------------" + response.toString());
                        AllWeiboInfo.TENCENT_QQZONE_NICK = ((JSONObject) response)
                                .getString("nickname");
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    if (AllWeiboInfo.TENCENT_QQZONE_NICK == null
                            || "".equals(AllWeiboInfo.TENCENT_QQZONE_NICK)
                            || AllWeiboInfo.TENCENT_QQZONE_OPEN_ID == null
                            || "".equals(AllWeiboInfo.TENCENT_QQZONE_OPEN_ID)) {
                    } else {
                        mTencent.logout(mContext);
                        LoginByQQThread thread = new LoginByQQThread(
                                SportMain.this, handler);
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

        }

        @Override
        public void onError(UiError arg0) {
            // TODO Auto-generated method stub

        }


    }

    // 接受腾讯微博的广播
    // BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
    // @Override
    // public void onReceive(Context context, Intent intent) {
    // Log.e(TAG, "broadcastReceived");
    // if (intent.getAction().equals("com.weibo.techface.getTencent_verifier"))
    // {
    // Message.obtain(handler, LOADING_DIALOG).sendToTarget();
    // weibo = WeiboContext.getInstance();
    // Bundle bundle = intent.getExtras();
    // // 获取从授权页面返回的veryfier
    // final String veryfier = bundle.getString("veryfier");
    // if (veryfier != null) {
    // new AsyncTask<Void, Void, Void>() {
    // String userInfo = null;
    //
    // @Override
    // protected Void doInBackground(Void... params) {
    // weibo.getAccessToken(weibo.getTokenKey(), weibo.getTokenSecrect(),
    // veryfier);//
    // 取得key和secret,这个key和secret非常重要，调腾讯的API全靠它了，神马新浪的，人人网的都一样的，不过还是有点区别，腾讯的OAuth认证是基于1.0的
    // userInfo = weibo.getUserInfo(weibo.getAccessTokenKey(),
    // weibo.getAccessTokenSecrect());
    // return null;
    // }
    //
    // @Override
    // protected void onPreExecute() {
    // if (loginPregressDialog != null && !SportMain.this.isFinishing())
    // loginPregressDialog.show();
    // }
    //
    // @Override
    // protected void onPostExecute(Void void1) {
    //
    // try {
    // JSONObject data = new JSONObject(userInfo).getJSONObject("data");
    // openId = "$" + data.getString("openid");
    // final String userId = data.getString("name");
    // Log.e(TAG, "userId = " + userId);
    // Log.e(TAG, "data88888 = " + data);
    // TencentDataHelper tencent_dataHelper = new TencentDataHelper(mContext);
    // user_name = data.getString("nick");
    // mBirthday = ""
    // + data.getString("birth_year")
    // + "-"
    // + ((data.getString("birth_month").length() == 1) ? ("0" + data
    // .getString("birth_month")) : data.getString("birth_month"))
    // + "-"
    // + ((data.getString("birth_day").length() == 1) ? ("0" + data
    // .getString("birth_day")) : data.getString("birth_day"));
    // Log.d(TAG, "birthday:" + mBirthday);
    // UserInfo user = new UserInfo();
    // user.setUserId(userId);
    // user.setUserName(user_name);
    // user.setToken(weibo.getAccessTokenKey());
    // user.setTokenSecret(weibo.getAccessTokenSecrect());
    // if (data.getInt("sex") == 1) {
    // mSex = "man";
    // } else if (data.getInt("sex") == 2) {
    // mSex = "woman";
    // }
    //
    // Log.v(TAG, "userName = " + user_name);
    //
    // tencent_dataHelper.clearDB();
    // tencent_dataHelper.SaveUserInfo(user);
    // tencent_dataHelper.Close();
    // if (Tools.isNetworkConnected(SportMain.this)) {
    // new AsyncTask<Void, Void, ApiBack>() {
    // @Override
    // protected ApiBack doInBackground(Void... params) {
    // Log.e(TAG, "doInBackground");
    // ApiBack back = null;
    // try {
    // Log.e(TAG, "user_name=" + user_name);
    // Log.e(TAG, "openId=" + openId);
    // Log.e(TAG, "userId=" + userId);
    // if (userId == null || "".equals(userId) || openId == null
    // || "".equals(openId)) {
    // Message.obtain(handler, LOADING_DIALOG).sendToTarget();
    // return null;
    // }
    // back = ApiJsonParser.combineWeibo_New(
    // ApiConstant.WeiboType.TengxunWeiBo, userId, openId, 1);
    // } catch (ApiNetException e) {
    // e.printStackTrace();
    // Message.obtain(mExceptionHandler, SportsExceptionHandler.NET_ERROR)
    // .sendToTarget();
    // }
    // return back;
    // }
    //
    // @Override
    // protected void onPostExecute(ApiBack message) {
    // if (message == null) {
    // loginPregressDialog.dismiss();
    // // mVisibility.setVisibility(View.VISIBLE);
    // mLayout.setVisibility(View.GONE);
    // Toast.makeText(SportMain.this,
    // getResources().getString(R.string.sports_server_error),
    // Toast.LENGTH_SHORT).show();
    // } else {
    // // loginPregressDialog.dismiss();
    // Log.e(TAG, "message.isFlag():" + message.getFlag());
    // Log.e(TAG, message.getMsg());
    // if (message.getFlag() == 0 || message.getFlag() == 1) {
    // if (message.getFlag() == 1) {
    // SportsApp.mIsAdmin = true;
    // } else {
    // SportsApp.mIsAdmin = false;
    // }
    // Log.e(TAG, "loginsuccess");
    // String sessionid = message.getMsg().substring(7);
    // Log.e(TAG, "session_id" + sessionid);
    // mSportsApp.setSessionId(sessionid);
    // mSportsApp.setLogin(true);
    // Log.d(TAG, "SportsApp.sessionId:" + mSportsApp.getSessionId());
    // SharedPreferences sp = getSharedPreferences(
    // AllWeiboInfo.LOGIN_INFO, Context.MODE_PRIVATE);
    // Editor ed = sp.edit();
    // ed.clear();
    // ed.putString("account", userId);
    // ed.putString("weibotype", ApiConstant.WeiboType.TengxunWeiBo);
    // ed.putString(AllWeiboInfo.LOGIN_INFO_OPEN_ID_KEY, openId);
    // ed.commit();
    //
    // Toast.makeText(
    // SportMain.this,
    // getResources()
    // .getString(R.string.sports_toast_login_succes),
    // Toast.LENGTH_SHORT).show();
    // // if (message.getFirst() ==
    // // 0)
    // // LoginActivity.mIsRegister
    // // = true;
    // // else
    // // LoginActivity.mIsRegister
    // // = false;
    // loginFinish();
    // } else {
    // Log.e(TAG, "loginfailed");
    // if (message.getMsg().equals("accountOrPwdError")) {
    // Toast.makeText(
    // SportMain.this,
    // getResources().getString(
    // R.string.sports_toast_nameOrPwdError),
    // Toast.LENGTH_SHORT).show();
    // } else {
    // Toast.makeText(
    // SportMain.this,
    // getResources().getString(
    // R.string.sports_toast_login_failed),
    // Toast.LENGTH_SHORT).show();
    // }
    // }
    // }
    // }
    // }.execute();
    // } else {
    // loginPregressDialog.dismiss();
    // // mVisibility.setVisibility(View.VISIBLE);
    // mLayout.setVisibility(View.GONE);
    // if (toastnet != null) {
    // Log.v(TAG, "cancel");
    // toastnet.cancel();
    // } else {
    // Log.v(TAG, "creat");
    // toastnet = Toast.makeText(SportMain.this,
    // getResources().getString(R.string.error_cannot_access_net),
    // Toast.LENGTH_SHORT);
    // }
    // toastnet.show();
    // }
    // } catch (JSONException e) {
    // e.printStackTrace();
    // }
    // Log.e(TAG, userInfo);
    // }
    // }.execute();
    // }
    // }
    // }
    // };

    // 获取记录的当前时间(StateActivity)
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

    // 判断用户名过长
    private boolean judgeNum(String name) {
        boolean isJudeg = false;

        if (name.length() > 15) {
            isJudeg = true;
            return isJudeg;
        }
        // try {
        // int length = name.getBytes("gbk").length;
        // if (length != 0 && length > 20) {
        // isJudeg = true;
        // return isJudeg;
        // }
        //
        // if (name.length() > 20) {
        // isJudeg = true;
        // return isJudeg;
        // }
        // } catch (UnsupportedEncodingException e) {
        // // TODO Auto-generated catch block
        // e.printStackTrace();
        // }

        return isJudeg;
    }

    /**
     *@method 固定字体大小方法
     *@author suhu
     *@time 2016/10/11 13:29
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
