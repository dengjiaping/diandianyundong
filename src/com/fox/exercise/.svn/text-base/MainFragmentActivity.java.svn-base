package com.fox.exercise;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.LocationClient;
import com.baidu.mapapi.SDKInitializer;
import com.fox.exercise.api.AddCoinsThread;
import com.fox.exercise.api.ApiBack;
import com.fox.exercise.api.ApiConstant;
import com.fox.exercise.api.ApiJsonParser;
import com.fox.exercise.api.ApiNetException;
import com.fox.exercise.api.ApiSessionOutException;
import com.fox.exercise.api.ShowCoinsDialog;
import com.fox.exercise.api.WatchService;
import com.fox.exercise.api.entity.UserDetail;
import com.fox.exercise.bitmap.util.ImageResizer;
import com.fox.exercise.db.SportSubTaskDB;
import com.fox.exercise.login.LoginActivity;
import com.fox.exercise.login.SportMain;
import com.fox.exercise.login.UpdataApplication;
import com.fox.exercise.newversion.FirstStepService;
import com.fox.exercise.newversion.act.FoxSportsSettingsActivity;
import com.fox.exercise.newversion.act.IndexHealthFrg;
import com.fox.exercise.newversion.act.IndexMeFragment;
import com.fox.exercise.newversion.act.MyFirstSportFragment;
import com.fox.exercise.newversion.act.PersonalPageMainActivity;
import com.fox.exercise.newversion.act.SportCircleMainFragment;
import com.fox.exercise.newversion.act.StartSportFirstGaoDeFragment;
import com.fox.exercise.newversion.newact.NewGuanZhuActivity;
import com.fox.exercise.newversion.newact.NewRanksActivity;
import com.fox.exercise.newversion.newact.NewYuePaoGaoDeActivity;
import com.fox.exercise.newversion.trainingplan.IndexTrainPlanFrg;
import com.fox.exercise.pedometer.ImageWorkManager;
import com.fox.exercise.pedometer.SportContionTaskDetail;
import com.fox.exercise.util.MediaFileDownloader;
import com.fox.exercise.util.RoundedImage;
import com.fox.exercise.util.SportTaskUtil;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.ingenic.indroidsync.SportsApp;

/**
 * @author loujungang 主activity
 */
public class MainFragmentActivity extends FragmentActivity implements
        View.OnClickListener, OnDismissListener {
    private Toast toastnet = null;
    private UserDetail detail;
    private ResideMenu resideMenu;
    private MainFragmentActivity mContext;
    // 我的运动
    private ResideMenuItem itemMySports;
    // 排行
    private ResideMenuItem itemRank;
    // 约跑
    private ResideMenuItem itemNear;
    // 好友
    private ResideMenuItem itemFriend;
    // 点点运动圈
    // private ResideMenuItem itemSportsGroup;
    // 发现
    private ResideMenuItem itemFindOther;
    // 健康测试
//	private ResideMenuItem itemHealthTest;
    // 活动
    private ResideMenuItem itemPlan;
    // 配件
    private ResideMenuItem itemPeiJian;
    // 金币商城
    private ResideMenuItem itemCollMall;
    // 给菜单创建点击效果
    public List<View> itemList = new ArrayList<View>();
    // 创建FragmentManager管理器
    private FragmentManager fragmentManager;
    private LinearLayout ssLayout;
    private LinearLayout ruleLayout;
    private TextView nameText;
    private ImageView userSex;
    private TextView sports_money;
    private TextView photo_msg_count;
    // 金币规则
    private TextView ruleTextView;
    private RoundedImage userPhoto;
    private ImageWorkManager mImageWorkerMan_Icon;
    private ImageResizer mImageWorker_Icon;
    private final int photo_view = 0;
    private final int itemMySports_view = 1;
    private final int itemRank_view = 2;
    private final int itemNear_view = 3;
    // private final int itemSportsGroup_view=4;
    private final int itemFriend_view = 5;
    private final int itemHealthTest_view = 6;
    private final int itemPlan_view = 7;
    private final int itemCollMall_view = 8;
    private final int itemRule_view = 9;
    private final int itemFindOther_view = 11;
    private final int itemPeiJian_view = 12;
    private final int rule_layout_view = 90;
    CoolCurrencyRules coolCurrencyRules;
    //	NearbyActivity nerActivity;
//	NearbyActivityGaode nerActivityGaode;
//	RankActivity rankActivity;
    StateActivity stateActivity;
    FoxSportsSetting foxSportsSetting;
    YunHuWebViewActivity sportsShopMall;
    ActivityList activityList;
    SportsGroup sportsGroup;
    FindOtherFragment findOtherFragment;
    private static final String TAG = "MainFragmentActivity";
    private String mSessionID;
    private int mUid;
    private SportsApp app;
    //	private int local_map_number = 2;
    private NotificationManager mNotifMan;
    public static boolean m_bKeyRight = true;
    public static final String strKey = "5qia1VhHhshy0NDGc6bRRQOA";
    public static final int UPDATE_TRYTOLOGIN = 19;
    public static final int HIDE_VIEW = 119;
    public static final int UPDATE_SPORTSGOAL = 120;
    public static final int UPDATE_HEADER = 0x4;
    public static final int FOXSPORTSETTINGRESULT = 125;
    public static final int UNLOGINRESULT = 126;
    private Dialog alertDialog;
    public static boolean mErrorExist = false;
    //	private SportsExceptionHandler mExceptionHandler = null;
    public static boolean mAfterLogin = false;
    public List<ImageButton> itemLists = new ArrayList<ImageButton>();
    private SDKReceiver mReceiver;
    private int mMap;
    //	private static final String NORMAL_PATH1 = SportsApp.getContext()
//			.getFilesDir().toString()
//			+ "/loginCoins";
//	private static final String NORMAL_PATH2 = Environment
//			.getExternalStorageDirectory()
//			+ "/android/data/"
//			+ SportsApp.getContext().getPackageName()
//			+ ".zuimei"
//			+ "/.loginCoins";
//	private boolean baiduInitSuccess = true;
    // 地图用到
    private SharedPreferences foxSportSettingMap;
    private int coins;
    private boolean isRegist = false;

    private LinearLayout mysport_layout, start_sport_layout,
            sports_group_layout, sports_health_layout,sports_me_layout;// 测试底部导航栏
    private ImageView mysport_image, start_sport_image, sports_group_image,
            sports_health_image,sports_me_image;
    private TextView mysport_text, start_sport_text, sports_group_text,
            sports_health_text,sports_me_text;

    // 新的我的运动页面
    MyFirstSportFragment myFirstSportFragment;
    // 新的运动圈
    SportCircleMainFragment sportCircleMainFragment;
    // 开始运动
    StartSportFirstGaoDeFragment startSportFirstGaoDeFragment;
    //	StartSportFirstBaiDuFragment sportFirstBaiDuFragment;
    public int isIndexTouch = 0;

    //	private PopupWindow window;
    private RelativeLayout mPopMenuBack, mainLL, rl_xinshouyindao;

    private SharedPreferences msharedPreferences;

    // 测试 步数
    // private boolean mIsRunning;// 程序是否运行的标志位
    // private PedometerSettings mPedometerSettings;
//	private SharedPreferences mSettings;

    private Dialog mLoadProgressDialog = null;
    private TextView mDialogMessage;

    private PopupWindow myWindow = null;
    private LinearLayout myView;

    private IndexHealthFrg indexHealthFragment;
    private IndexMeFragment indexMeFragment;
    private IndexTrainPlanFrg indexTrainPlanFragment;// 测试训练计划

    public class SDKReceiver extends BroadcastReceiver {
        public void onReceive(Context context, Intent intent) {
            String s = intent.getAction();
            Log.d(TAG, "action: " + s);
            if (s.equals(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR)) {
                Log.v(TAG, "key 验证出错! 请在 AndroidManifest.xml 文件中检查 key 设置");
//				baiduInitSuccess = false;
            } else if (s
                    .equals(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR)) {
//				baiduInitSuccess = false;
                Log.v(TAG, "网络出错");
            }
        }
    }

    // 侧边栏上的item，先清除其他item的点击背景色，再给当前item加上点击效果
    private void setOtherViewBackground(View view) {
        if (itemList != null) {
            for (int i = 0; i < itemList.size(); i++) {
                itemList.get(i).setBackgroundColor(
                        getResources().getColor(android.R.color.transparent));
            }
            itemList.clear();
        }
        // view.setBackgroundColor(getResources().getColor(R.color.w1));
//		view.setBackgroundDrawable(getResources().getDrawable(
//				R.drawable.residemenu_onclick));
        view.setBackgroundResource(R.drawable.residemenu_onclick);
        itemList.add(view);

    }

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        Log.v(TAG, "wmh onCreate()");
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.sport_main_fragment_layout);
        mNotifMan = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mContext = this;
        // 获取全局对象
        app = (SportsApp) getApplication();
        app.addActivity(this);
//        if (StateActivity.mClient == null) {
//            StateActivity.mClient = new LocationClient(this);
//        }

        // 友盟推送
        PushAgent.getInstance(this).onAppStart();

        // 初始化菜单
        setUpMenu();
        fragmentManager = getSupportFragmentManager();
        app.mNotificationManager.cancel(100);
        app.setMainHandler(mainHandler);
        // Log.e(TAG,
        // "------------------------11--"+app.getSessionId().toString());
        mSessionID = app.getSessionId();
        if (mSessionID != null) {

        }
        mUid = app.getSportUser().getUid();

        // 测试
        mysport_layout = (LinearLayout) findViewById(R.id.mysport_layout);
        start_sport_layout = (LinearLayout) findViewById(R.id.start_sport_layout);
        sports_group_layout = (LinearLayout) findViewById(R.id.sports_group_layout);
        sports_health_layout = (LinearLayout) findViewById(R.id.sports_health_layout);
        sports_me_layout = (LinearLayout) findViewById(R.id.sports_me_layout);
        mysport_layout.setOnClickListener(this);
        start_sport_layout.setOnClickListener(this);
        sports_group_layout.setOnClickListener(this);
        sports_health_layout.setOnClickListener(this);
        sports_me_layout.setOnClickListener(this);
        mysport_image = (ImageView) findViewById(R.id.mysport_image);
        start_sport_image = (ImageView) findViewById(R.id.start_sport_image);
        sports_group_image = (ImageView) findViewById(R.id.sports_group_image);
        sports_health_image = (ImageView) findViewById(R.id.sports_health_image);
        sports_me_image = (ImageView) findViewById(R.id.sports_me_image);
        mysport_text = (TextView) findViewById(R.id.mysport_text);
        start_sport_text = (TextView) findViewById(R.id.start_sport_text);
        sports_group_text = (TextView) findViewById(R.id.sports_group_text);
        sports_health_text = (TextView) findViewById(R.id.sports_health_text);
        sports_me_text = (TextView) findViewById(R.id.sports_me_text);


        msharedPreferences = getSharedPreferences("sports" + mUid, 0);
        clearSelection();

        // GetTimeListThread newTimeListThread = new GetTimeListThread();
        // newTimeListThread.start()
        // 判断正常登录且数据异常的时候打印Log NO Doing
        if ((app.getSportUser() == null || app.getSportUser().equals("") || app
                .getSportUser().getUid() == 0) && app.LoginOption) {
            Log.v(TAG, "onCreated()   No Doing");
        } else {
            // if (app.isOpenNetwork()) {
            // waitShowDialog();
            // (new GetTempTask()).execute();
            // }
            initFragments();
            if (app.config == 1) {
                bindService(new Intent(this, WatchService.class), wConnection,
                        Context.BIND_AUTO_CREATE);
            }
            if (app.LoginOption && app.LoginNet) {
                /*
                 * String time = app.getCurrentDate(); SportsLoginUtils util =
				 * new SportsLoginUtils();
				 * if(!util.readDateFromFile(NORMAL_PATH1
				 * +mUid,NORMAL_PATH2+mUid).equals(time)){
				 * util.saveDateToFile(time, NORMAL_PATH1+mUid,
				 * NORMAL_PATH2+mUid); Log.i(TAG, "award coins---"); new
				 * AddCoinsThread(10,1,mainHandler).start(); }
				 */
                new AddCoinsThread(10, 1, mainHandler, -1).start();
            }
            // else {
            // initFragments();
            // }
        }
        app.setMainFragmentActivity(this);
//		mExceptionHandler = app.getmExceptionHandler();
        /*
		 * Handler fHandler = app.getfHandler();
		 * fHandler.sendMessage(fHandler.obtainMessage(
		 * FoxSportsSetting.UPDATE_TRYTOLOGIN));
		 */

        // 注册 SDK 广播监听者
        IntentFilter iFilter = new IntentFilter();
        iFilter.addAction(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR);
        iFilter.addAction(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR);
        mReceiver = new SDKReceiver();
        registerReceiver(mReceiver, iFilter);
        // 注册设置退出时的广播
        // IntentFilter filter = new
        // IntentFilter(FoxSportsSettingsActivity.action);
        // registerReceiver(broadcastReceiver, filter);

        foxSportSettingMap = getSharedPreferences("sports"
                + app.getSportUser().getUid(), 0);
        // 现在只要高德地图
        Editor editor = foxSportSettingMap.edit();
        editor.putInt("map", 0);
        editor.commit();

        mMap = foxSportSettingMap.getInt("map", 0);
        app.mCurMapType = mMap;
        mErrorExist = false;
        UpdateApp();
    }

    private Handler mainHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            switch (msg.what) {
                case ApiConstant.UPDATE_MSG:
                    // 提示现有消息数
//				int totalMsg = msg.arg1;
                    if (photo_msg_count != null) {
                        // photo_msg_count.setVisibility(View.VISIBLE);
                        // photo_msg_count.setText(totalMsg > 99 ? "99+" : totalMsg
                        // + "");
                    }
                    break;
                case ApiConstant.UPDATE_FRIENDS_MSG:
                    // 显示好友消息数
//				int friendsMsg = msg.arg1;
                    if (itemFriend.countMsg != null) {
                        // itemFriend.countMsg.setVisibility(View.VISIBLE);
                        // itemFriend.countMsg.setText(friendsMsg > 99 ? "99+"
                        // : friendsMsg + "");
                    }
                    break;
                case ApiConstant.CLEAR_PRI_MSG:
                    // 清除私信数
				/*
				 * int priMsg=friendsMsg-msg.arg1; if (priMsg>0)
				 * itemFriend.countMsg.setText(priMsg > 99 ? "99+" : priMsg +
				 * ""); else
				 */
                    itemFriend.countMsg.setVisibility(View.GONE);
                    break;
                case ApiConstant.UPDATE_COINS_MSG:
                    coins += msg.arg1;
                    sports_money.setText("" + coins);
                    Log.i("", "award coins--- update coins");
                    // setUpMenu();
                    break;
                case UPDATE_HEADER:
                    Log.i(TAG, "Shandle receive");
                    Intent data = (Intent) msg.obj;
                    Bundle bundle = data.getBundleExtra("useredit");
                    boolean isfacechanged = bundle.getBoolean("isfacechanged");
                    String username = bundle.getString("username");
                    if (isfacechanged) {
                        initPortrait();
                    }
                    nameText.setText(username);
                    break;
                case ApiConstant.UPDATE_DEFAULTMAP_MSG:
                    break;
                case UPDATE_TRYTOLOGIN:
                    detail = SportsApp.getInstance().getSportUser();
                    initPortrait();
                    nameText.setText(detail.getUname());
                    if (detail.getSex().equals("man")) {
                        userSex.setImageResource(R.drawable.cebian_sex_man);
                    } else {
                        userSex.setImageResource(R.drawable.cebian_sex_women);
                    }
                    coins = detail.getCoins();
                    sports_money.setText(" " + coins);
                    isRegist = true;
                    break;
                case ApiConstant.COINS_SUCCESS:
                    Intent cIntent = new Intent(MainFragmentActivity.this,
                            ShowCoinsDialog.class);
                    cIntent.putExtra("message", getString(R.string.login_coins));
                    startActivity(cIntent);
                    // startActivityForResult(cIntent, 3);
                    break;
                case ApiConstant.COINS_FAIL:
                case ApiConstant.COINS_LIMIT:
                    initFragments();
                    break;
                case ApiConstant.UPDATE_COINS_NOW:
                    int n = 0;
                    n++;
//				Log.e(TAG, "进来金币-----" + detail.getCoins()
//						+ "-------------------" + n);
                    detail = SportsApp.getInstance().getSportUser();
                    coins = detail.getCoins();
                    sports_money.setText(" " + coins);
                    break;
                // 隐藏键盘
                case HIDE_VIEW:
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

                    if (imm.isActive() && getCurrentFocus() != null) {
                        if (getCurrentFocus().getWindowToken() != null) {
                            imm.hideSoftInputFromWindow(getCurrentFocus()
                                            .getWindowToken(),
                                    InputMethodManager.HIDE_NOT_ALWAYS);
                        }
                    }
                    break;
                case UPDATE_SPORTSGOAL:
                    // 更新今日运动目标
//                    if (myFirstSportFragment != null) {
//                        myFirstSportFragment.setSportsgoal();
//                    }
                    break;
                case FOXSPORTSETTINGRESULT:
                    setSelection(12);
                    break;
                case UNLOGINRESULT:
                    myFirstSportFragment = null;
                    sportCircleMainFragment = null;
                    indexHealthFragment = null;
                    indexMeFragment = null;
                    indexTrainPlanFragment = null;
                    initFragments();
                    break;
                default:
                    break;
            }
        }

    };
    private WatchService wService;
    private ServiceConnection wConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            wService = ((WatchService.WBinder) service).getService();
            if (wService != null)
                wService.appOnForeground();
        }

        public void onServiceDisconnected(ComponentName className) {
            wService = null;
        }
    };
//	private SportsFriends foxSportsFriends;

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        Log.v("MainFragmentActivity", "StatActivity MainFragmentActivity ondes");
        super.onDestroy();
        LoginActivity.mTabActivityHasStart = false;
        if (itemList != null) {
            itemList.clear();
            itemList = null;
        }
        if (itemLists != null) {
            itemLists.clear();
            itemLists = null;
        }
        if (app.config == 1) {
            if (wService != null) {
                unbindService(wConnection);
                wService = null;
            }
        }/*
		 * else stopService(new Intent(this,MessageService.class));
		 */
        SportsApp.getInstance().setMainFragmentActivity(null);
        app.setMainHandler(null);
        app.removeActivity(this);
        MediaFileDownloader.clearMediaCache(this);
        unregisterReceiver(mReceiver);

        // unregisterReceiver(broadcastReceiver);

        if (alertDialog != null && alertDialog.isShowing())
            alertDialog.dismiss();
        coolCurrencyRules = null;
//		nerActivity = null;
//		nerActivityGaode = null;
//		rankActivity = null;
        stateActivity = null;
        foxSportsSetting = null;
//		mapActivity = null;
        sportsShopMall = null;
        activityList = null;
        sportsGroup = null;
        findOtherFragment = null;
        myFirstSportFragment = null;
        if (mLoadProgressDialog != null)
            if (mLoadProgressDialog.isShowing())
                mLoadProgressDialog.dismiss();
        mLoadProgressDialog = null;
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        MobclickAgent.onResume(MainFragmentActivity.this);
        Log.e(TAG, "1isR--------" + isRegist);
        if (isRegist && app.LoginOption && app.LoginNet) {
            // 检测是否是当天第一次登陆-赠送酷比
			/*
			 * String time = app.getCurrentDate(); SportsLoginUtils util = new
			 * SportsLoginUtils(); mUid=app.getSportUser().getUid();
			 * if(!util.readDateFromFile
			 * (NORMAL_PATH1+mUid,NORMAL_PATH2+mUid).equals(time)){
			 * util.saveDateToFile(time, NORMAL_PATH1+mUid, NORMAL_PATH2+mUid);
			 * Log.e(TAG, "2isR--------"+isRegist);
			 *
			 * }
			 */
            new AddCoinsThread(10, 1, mainHandler, -1).start();
            isRegist = false;
        }

//		mSettings = PreferenceManager.getDefaultSharedPreferences(this);
        // mPedometerSettings = new PedometerSettings(mSettings);
        // mIsRunning = mSettings.getBoolean("service_running", false);
        // mIsRunning = msharedPreferences.getBoolean("mservice_running",
        // false);
        // if (!mIsRunning) {
        // startStepService();
        // bindStepService();
        // } else {
        // bindStepService();
        // }

//        if (app != null && app.getSportUser() != null) {
//            if (("man").equals(SportsApp.getInstance().getSportUser().getSex())) {
//                userPhoto.setImageResource(R.drawable.sports_user_edit_portrait_male);
//            } else {
//                userPhoto.setImageResource(R.drawable.sports_user_edit_portrait);
//            }
//
//            mImageWorker_Icon.loadImage(SportsApp.getInstance().getSportUser()
//                    .getUimg(), userPhoto, null, null, false);
//        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        app.mNotificationManager.cancel(100);
        Log.v(TAG, "app.getSessionId() is " + app.getSessionId());
        SharedPreferences sp = getSharedPreferences("user_login_info",
                Context.MODE_PRIVATE);
        if (!"".equals(sp.getString("account", "")) && app.LoginOption) {
            if (app.getSessionId() == null || app.getSessionId().equals("")) {
                if (!mErrorExist) {
//                    Toast.makeText(
//                            this,
//                            getResources().getString(
//                                    R.string.sports_except_data_one),
//                            Toast.LENGTH_SHORT).show();
                    LoginActivity.mTabActivityHasStart = false;
                    mErrorExist = true;
                    SportMain.mIsStop = true;
                    app.setLogin(false);
                    app.setSessionId("");
                    app.removeAllActivity();
                    finish();
                    startActivity(new Intent(this, SportMain.class));
                }
            }
        } else if ("".equals(sp.getString("account", "")) && app.LoginOption) {
            if (app.getSessionId() == null || app.getSessionId().equals("")) {
                if (!mErrorExist) {
                    // Toast.makeText(
                    // this,
                    // getResources().getString(
                    // R.string.sports_except_data_two),
                    // Toast.LENGTH_SHORT).show();
                    mErrorExist = true;
                    finish();
                    startActivity(new Intent(this, LoginActivity.class));
                }
            }
        }
        // if (mAfterLogin && !LoginActivity.mIsFromMain) {
        // mAfterLogin = false;
        // }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // unbindStepService();
        MobclickAgent.onPause(MainFragmentActivity.this);
    }

    private void initFragments() {
        // setOtherViewBackground(itemMySports);

        setSelection(1);
        mysport_text.setTextColor(Color.parseColor("#ffae00"));
        mysport_image.setImageResource(R.drawable.sk_startsport_focus);
    }

    private void setUpMenu() {

        // attach to current activity;
        // LinearLayout fl=(LinearLayout)findViewById(R.id.main_fragment);
        resideMenu = new ResideMenu(this);
        // resideMenu.setBackground(R.drawable.sports_bg_menu);
        resideMenu.setBackground(R.drawable.cebian_new_bg);
        resideMenu.attachToActivity(this);
        // resideMenu.setMenuListener(menuListener);
        resideMenu.setScaleValue(0.6f);
        // 获得头像、名字及布局的对象
        mImageWorkerMan_Icon = new ImageWorkManager(this, 0, 0);
        mImageWorker_Icon = mImageWorkerMan_Icon.getImageWorker();
        ssLayout = resideMenu.getPhotoView();
        ruleLayout = resideMenu.getruleLinearLayout();
        nameText = resideMenu.getNameTextView();
        userSex = resideMenu.getUserSexView();
        userPhoto = resideMenu.getUserImage();
        ruleTextView = resideMenu.getRuleTextView();
        sports_money = resideMenu.getMoneyTextView();
        photo_msg_count = resideMenu.getphotomsgcount();
        detail = SportsApp.getInstance().getSportUser();
        if (app.LoginOption) {
            initPortrait();
            nameText.setText(detail.getUname());
            if (("man").equals(detail.getSex())) {
                userSex.setImageResource(R.drawable.friends_group_sexman);
            } else {
                userSex.setImageResource(R.drawable.friends_group_sexwomen);
            }
            coins = detail.getCoins();
            sports_money.setText(" " + coins);
        } else {
            nameText.setText(getResources().getString(R.string.anonymous));
            userSex.setImageResource(R.drawable.cebian_sex_man);
            sports_money.setText(" " + 0);
        }

        // create menu items;
        // 我的运动
        itemMySports = new ResideMenuItem(this, R.drawable.sports_sk_mysport,
                getResources().getString(R.string.sport_condition));
        // 约跑
        // itemNear = new ResideMenuItem(this, R.drawable.sports_sk_invite,
        // getResources().getString(R.string.about_run));
        itemNear = new ResideMenuItem(this, R.drawable.cebian_yuepao,
                getResources().getString(R.string.about_run));
        // 排行
        // itemRank = new ResideMenuItem(this, R.drawable.sports_sk_rank,
        // getResources().getString(R.string.slimgirl_sort));
        itemRank = new ResideMenuItem(this, R.drawable.cebian_paihang,
                getResources().getString(R.string.slimgirl_sort));
		/*
		 * itemSportsGroup = new ResideMenuItem(this,
		 * R.drawable.sports_sk_sportsgroup, "点点运动圈");
		 */
        // 好友
        // itemFriend = new ResideMenuItem(this, R.drawable.sports_sk_friend,
        // getResources().getString(R.string.rankboard_friend));
        // itemFriend = new ResideMenuItem(this, R.drawable.cebian_friends,
        // getResources().getString(R.string.rankboard_friend));
        itemFriend = new ResideMenuItem(this, R.drawable.cebian_friends,
                getResources().getString(R.string.guanzhu_people));
		/*
		 * itemHealthTest = new ResideMenuItem(this,
		 * R.drawable.sports_healthy_test, "健康测试");
		 */
        // 活动
        // itemPlan = new ResideMenuItem(this,
        // R.drawable.sports_sk_playwithother,
        // getResources()
        // .getString(R.string.download_tab_title_activities));
//		itemPlan = new ResideMenuItem(this, R.drawable.cebian_huodong,
//				getResources()
//						.getString(R.string.download_tab_title_activities));
        // 配件
        itemPeiJian = new ResideMenuItem(this, R.drawable.smart_equipment_icon,
                getResources().getString(R.string.conn_smart_device));
        // 金币商城
        // itemCollMall = new ResideMenuItem(this,
        // R.drawable.sports_sk_coolmall,
        // getResources().getString(R.string.sports_coolmall));
        itemCollMall = new ResideMenuItem(this, R.drawable.cebian_shangcheng,
                getResources().getString(R.string.sports_coolmall));
        // 发现
        itemFindOther = new ResideMenuItem(this, R.drawable.sports_sk_faxian,
                getResources().getString(R.string.friends_tab_faxian));

        ruleLayout.setId(rule_layout_view);
        ruleLayout.setOnClickListener(this);
        ssLayout.setId(photo_view);
        ssLayout.setOnClickListener(this);
        itemMySports.setId(itemMySports_view);
        itemMySports.setOnClickListener(this);
        itemRank.setId(itemRank_view);
        itemRank.setOnClickListener(this);
        itemNear.setId(itemNear_view);
        itemNear.setOnClickListener(this);
		/*
		 * itemSportsGroup.setId(itemSportsGroup_view);
		 * itemSportsGroup.setOnClickListener(this);
		 */
        itemFindOther.setId(itemFindOther_view);
        itemFindOther.setOnClickListener(this);
        itemFriend.setId(itemFriend_view);
        itemFriend.setOnClickListener(this);
		/*
		 * itemHealthTest.setId(itemHealthTest_view);
		 * itemHealthTest.setOnClickListener(this);
		 */
//		itemPlan.setId(itemPlan_view);
//		itemPlan.setOnClickListener(this);
        itemPeiJian.setId(itemPeiJian_view);
        itemPeiJian.setOnClickListener(this);
        itemCollMall.setId(itemCollMall_view);
        itemCollMall.setOnClickListener(this);
        ruleTextView.setId(itemRule_view);
        ruleTextView.setOnClickListener(this);
        // resideMenu.addMenuItem(itemMySports);
        resideMenu.addMenuItem(itemRank);
        // resideMenu.addMenuItem(itemSportsGroup);
        // resideMenu.addMenuItem(itemFindOther);
        resideMenu.addMenuItem(itemFriend);
        resideMenu.addMenuItem(itemNear);
        // resideMenu.addMenuItem(itemHealthTest);
//		resideMenu.addMenuItem(itemPlan);
        resideMenu.addMenuItem(itemPeiJian);
        resideMenu.addMenuItem(itemCollMall);
        // View view=new View(this);
        // LayoutParams layoutParams=new
        // LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 1);
        // view.setBackgroundResource(R.drawable.fenge_line);
        // layoutParams.setMargins(0, 0, 10, 0);
        // view.setLayoutParams(layoutParams);

        itemNear.setVisibility(View.VISIBLE);

        mPopMenuBack = (RelativeLayout) findViewById(R.id.set_menu_background);
        mainLL = (RelativeLayout) findViewById(R.id.ll);
        rl_xinshouyindao = (RelativeLayout) findViewById(R.id.rl_xinshouyindao);
        SharedPreferences sp_xinshou = mContext.getSharedPreferences("xinshouyindao", Context.MODE_PRIVATE);
        if (0 == sp_xinshou.getInt("flag_on", 0)) {
            rl_xinshouyindao.setVisibility(View.VISIBLE);
            SharedPreferences.Editor spe_xinshou = sp_xinshou.edit();
            spe_xinshou.putInt("flag_on", 1);
            spe_xinshou.commit();
        }
        rl_xinshouyindao.setOnClickListener(this);
    }

    private void initPortrait() {
		/*
		 * mImageWorker_Icon
		 * .setLoadingImage("man".equals(SportsApp.getInstance()
		 * .getSportUser().getSex()) ? R.drawable.sports_residemenu_man :
		 * R.drawable.sports_residemenu_woman);
		 */
        if (("man").equals(SportsApp.getInstance().getSportUser().getSex())) {
            userPhoto.setImageResource(R.drawable.sports_user_edit_portrait_male);
        } else {
            userPhoto.setImageResource(R.drawable.sports_user_edit_portrait);
        }

        mImageWorker_Icon.loadImage(SportsApp.getInstance().getSportUser()
                .getUimg(), userPhoto, null, null, false);

    }

//    @Override
//    public boolean dispatchTouchEvent(MotionEvent ev) {
////		try {
////			return resideMenu.dispatchTouchEvent(ev);
////		} catch (Exception e) {
////			// TODO: handle exception
////		}
////		return false;
//        return resideMenu.dispatchTouchEvent(ev);
//    }

    // stateTxt rankTxt nearbyTxt moreTxt
    @Override
    public void onClick(View v) {
        // t=1代表是未登录状态，不需要关闭侧滑栏
        int t = 0;
        // 如果点击了头像和金币规则不需要加点击效果
        switch (v.getId()) {
            case photo_view:
                // String device_token =
                // UmengRegistrar.getRegistrationId(getApplicationContext());
                // Log.v(TAG,"umeng device_token is "+device_token);
                // setSelection(0);
                // new --新的跳转 跳转到个人主页
                t = 1;
                Intent intent = new Intent(mContext, PersonalPageMainActivity.class);
                intent.putExtra("ID", detail.getUid());
                intent.putExtra("isLoginer", true);
                startActivity(intent);
                if (photo_msg_count != null
                        && photo_msg_count.VISIBLE == View.VISIBLE) {
                    photo_msg_count.setVisibility(View.GONE);
                }
                if (app.config == 1 && wService != null)
                    wService.setMe(false);
                break;
            case itemMySports_view:
                // 我的运动
                setSelection(1);
                break;
            case itemRank_view:
                // 排行
                if (!app.LoginOption) {
                    t = 1;
                    app.TyrLoginAction(MainFragmentActivity.this,
                            getString(R.string.sports_love_title),
                            getString(R.string.try_to_login));
                } else if (!app.LoginNet) {
                    t = 1;
                    app.NoNetLogin(MainFragmentActivity.this);

                } else {
                    // setSelection(2);
                    t = 1;
                    Intent intentmap = new Intent(mContext, NewRanksActivity.class);
                    startActivity(intentmap);
                    if (app.config == 1 && wService != null)
                        wService.setMe(true);
                }
                // setOtherViewBackground(view);
                break;
            case itemNear_view:
                // 约跑
                if (!app.LoginOption) {
                    t = 1;
                    app.TyrLoginAction(MainFragmentActivity.this,
                            getString(R.string.sports_love_title),
                            getString(R.string.try_to_login));
                } else if (!app.LoginNet) {
                    t = 1;
                    app.NoNetLogin(MainFragmentActivity.this);

                } else {
                    // setSelection(3);
                    t = 1;
//				if (app.mCurMapType == SportsApp.MAP_TYPE_GAODE) {
                    Intent intentmap = new Intent(mContext,
                            NewYuePaoGaoDeActivity.class);
                    // intent.putExtra("ID", detail.getUid());
                    // intent.putExtra("isLoginer", true);
                    startActivity(intentmap);
//				} else {
//					Intent intentmap = new Intent(mContext,
//							NewYuePaoBaiDuActivity.class);
//					// intent.putExtra("ID", detail.getUid());
//					// intent.putExtra("isLoginer", true);
//					startActivity(intentmap);
//				}

                    // nerActivity.initlocation();
                    if (app.config == 1 && wService != null)
                        wService.setMe(true);
                }
                // setOtherViewBackground(view);
                break;

		/*
		 * case itemSportsGroup_view: //点点运动圈 if (app.LoginOption) {
		 * setSelection(4); if (app.config == 1 && wService != null)
		 * wService.setMe(true); }else { t=1;
		 * app.TyrLoginAction(MainFragmentActivity
		 * .this,getString(R.string.sports_love_title),
		 * getString(R.string.try_to_login)); } break;
		 */
            case itemFriend_view:
                // 好友
                if (!app.LoginOption) {
                    t = 1;
                    app.TyrLoginAction(MainFragmentActivity.this,
                            getString(R.string.sports_love_title),
                            getString(R.string.try_to_login));
                } else if (!app.LoginNet) {
                    t = 1;
                    app.NoNetLogin(MainFragmentActivity.this);

                } else {
                    // setSelection(5);
                    Intent intentmap = new Intent(mContext,
                            NewGuanZhuActivity.class);
                    startActivity(intentmap);
                    if (app.config == 1 && wService != null)
                        wService.setMe(true);
                    t = 1;
                }

                // setOtherViewBackground(view);
                break;
            case itemHealthTest_view:
                // 健康测试
                if (!app.LoginOption) {
                    t = 1;
                    app.TyrLoginAction(MainFragmentActivity.this,
                            getString(R.string.sports_love_title),
                            getString(R.string.try_to_login));
                } else if (!app.LoginNet) {
                    t = 1;
                    app.NoNetLogin(MainFragmentActivity.this);

                } else {
                    setSelection(6);
                    if (app.config == 1 && wService != null)
                        wService.setMe(true);
                }
                break;
            case itemPlan_view:
                // 活动
                if (!app.LoginOption) {
                    t = 1;
                    app.TyrLoginAction(MainFragmentActivity.this,
                            getString(R.string.sports_love_title),
                            getString(R.string.try_to_login));
                } else if (!app.LoginNet) {
                    t = 1;
                    app.NoNetLogin(MainFragmentActivity.this);

                } else {
                    t = 1;
                    // setSelection(7);
                    Intent intentmap = new Intent(mContext,
                            NewHuoDongActivity.class);
                    startActivity(intentmap);
                    if (app.config == 1 && wService != null)
                        wService.setMe(true);
                }

                break;
            case itemPeiJian_view:
                // 配件
                if (!app.LoginOption) {
                    t = 1;
                    app.TyrLoginAction(MainFragmentActivity.this,
                            getString(R.string.sports_love_title),
                            getString(R.string.try_to_login));
                } else if (!app.LoginNet) {
                    t = 1;
//				app.NoNetLogin(MainFragmentActivity.this);

                    Intent intentmap = new Intent(mContext, BindingDevice.class);
                    startActivity(intentmap);
                    if (app.config == 1 && wService != null)
                        wService.setMe(true);

                } else {
                    t = 1;
                    // setSelection(7);
                    Intent intentmap = new Intent(mContext, BindingDevice.class);
                    startActivity(intentmap);
                    if (app.config == 1 && wService != null)
                        wService.setMe(true);
                }
                break;
            case itemFindOther_view:
                // 发现
                if (!app.LoginOption) {
                    t = 1;
                    app.TyrLoginAction(MainFragmentActivity.this,
                            getString(R.string.sports_love_title),
                            getString(R.string.try_to_login));
//				Toast.makeText(MainFragmentActivity.this, getResources().getString(R.string.sports_no_login_play), Toast.LENGTH_SHORT)
//				.show();
                } else if (!app.LoginNet) {
                    t = 1;
                    app.NoNetLogin(MainFragmentActivity.this);

                } else {
                    setSelection(11);
                    if (app.config == 1 && wService != null)
                        wService.setMe(true);
                }
                break;
            case itemCollMall_view:
                // 金币商城
                if (app.LoginNet) {
                    if (app.isOpenNetwork()) {
                        startActivity(new Intent(this, YunHuWebViewActivity.class));
                    } else {
                        toastnet = Toast.makeText(
                                MainFragmentActivity.this,
                                getResources().getString(
                                        R.string.error_cannot_access_net),
                                Toast.LENGTH_SHORT);
                        if (toastnet != null) {
                            toastnet.show();
                        }
                    }
                } else {
                    app.NoNetLogin(MainFragmentActivity.this);
                }

                t = 1;
                break;
            case itemRule_view:
            case rule_layout_view:
                // setSelection(9);
                // Intent userIntent = new Intent(this, UserSetMainActivity.class);
                Intent userIntent = new Intent(this,
                        FoxSportsSettingsActivity.class);
                // startActivity(userIntent);
                startActivityForResult(userIntent, 2);
                t = 1;
                break;

            // 底部导航
            case R.id.mysport_layout:
                setSelection(1);
                break;
            case R.id.start_sport_layout:
                // if (isOPen(this)) {
                // start_sport_text.setTextColor(Color.parseColor("#ffae00"));
                // start_sport_image
                // .setImageResource(R.drawable.sk_startsport_focus);
                // setSelection(12);
                // } else {
                // Bundle bundle = new Bundle();
                // bundle.putString("title", "提示");
                // bundle.putString("message", "您需要开启GPS定位,是否开启？");
                // onCreateDialogs(2, bundle);
                // }
                start_sport_text.setTextColor(Color.parseColor("#ffae00"));
                start_sport_image.setImageResource(R.drawable.sk_trainplay_focus);
                setSelection(12);
                break;
            case R.id.sports_group_layout:
                sports_group_text.setTextColor(Color.parseColor("#ffae00"));
                sports_group_image
                        .setImageResource(R.drawable.sk_sportcircle_focus);
                setSelection(11);
                break;
            case R.id.sports_health_layout:
                sports_health_text.setTextColor(Color.parseColor("#ffae00"));
                sports_health_image.setImageResource(R.drawable.sk_health_focus);
                setSelection(13);
                break;
            case R.id.sports_me_layout:
                sports_me_text.setTextColor(Color.parseColor("#ffae00"));
                sports_me_image.setImageResource(R.drawable.sk_personal_focus);
                setSelection(14);
                break;
            case R.id.rl_xinshouyindao:
                rl_xinshouyindao.setVisibility(View.GONE);
                break;
            default:
                break;
        }
        if (t == 0) {
            resideMenu.closeMenu();
        }
        if (v.getId() == photo_view || v.getId() == rule_layout_view
                || v.getId() == itemRule_view) {
            if (itemList != null) {
                for (int i = 0; i < itemList.size(); i++) {
                    itemList.get(i).setBackgroundColor(
                            getResources()
                                    .getColor(android.R.color.transparent));
                }
                itemList.clear();
            }
        } else {
            if (t == 0) {
                setOtherViewBackground(v);
            }

        }
        t = 0;

    }

    @Override
    public void onAttachedToWindow() {
        // this.getWindow().setType(WindowManager.LayoutParams.TYPE_KEYGUARD);
        super.onAttachedToWindow();
    }

    private void setSelection(int index) {
        resideMenu.clearIgnoredViewList();
        clearSelection();
        // 每次选中之前先清楚掉上次的选中状态
        // clearSelection();
        // 开启一个Fragment事务
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        // 先隐藏掉所有的Fragment，以防止有多个Fragment显示在界面上的情况
        hideFragments(transaction);
        switch (index) {
            case 0:
                // 点击头像
                if (foxSportsSetting == null) {
                    // 如果ContactsFragment为空，则创建一个并添加到界面上
                    foxSportsSetting = new FoxSportsSetting();
                    transaction.add(R.id.main_fragment, foxSportsSetting);
                } else {
                    // 如果ContactsFragment不为空，则直接将它显示出来
                    transaction.show(foxSportsSetting);
                }
                break;
            case 1:
                isIndexTouch = 1;
                // 我的运动
                // if ( stateActivity== null) {
                // // 如果MessageFragment为空，则创建一个并添加到界面上
                // Log.v(TAG, "wmh new StateActivity()");
                // stateActivity = new StateActivity();
                // transaction.add(R.id.main_fragment, stateActivity);
                // } else {
                // // 如果MessageFragment不为空，则直接将它显示出来
                // Log.v(TAG, "wmh show StateActivity()");
                // transaction.show(stateActivity);
                // RelativeLayout ignored_view =
                // (RelativeLayout)findViewById(R.id.state_activity_layout_id);
                // resideMenu.addIgnoredView(ignored_view);
                // }
                mysport_text.setTextColor(Color.parseColor("#ffae00"));
                mysport_image.setImageResource(R.drawable.sk_startsport_focus);
                if (myFirstSportFragment == null) {
                    // 如果MessageFragment为空，则创建一个并添加到界面上
                    Log.v(TAG, "wmh new StateActivity()");
                    myFirstSportFragment = new MyFirstSportFragment();
                    transaction.add(R.id.main_fragment, myFirstSportFragment);
                } else {
                    // 如果MessageFragment不为空，则直接将它显示出来
                    Log.v(TAG, "wmh show StateActivity()");
                    transaction.show(myFirstSportFragment);
                    RelativeLayout ignored_view = (RelativeLayout) findViewById(R.id.mysport_zong_layout);
                    resideMenu.addIgnoredView(ignored_view);
                    myFirstSportFragment.updateTip();

                }
                break;
            case 2:
//			// 排行
//			if (rankActivity == null) {
//				// 如果ContactsFragment为空，则创建一个并添加到界面上
//				rankActivity = new RankActivity();
//				transaction.add(R.id.main_fragment, rankActivity);
//			} else {
//				// 如果ContactsFragment不为空，则直接将它显示出来
//				transaction.show(rankActivity);
//			}
//			break;
            case 3:
                // 约跑
//			if (local_map_number != app.mCurMapType) {
//				Log.i("", "nerActivity" + nerActivity);
//				if (local_map_number == SportsApp.MAP_TYPE_BAIDU
//						&& nerActivity != null) {
//					transaction.remove(nerActivity);
//					nerActivity = null;
//				}
//				if (local_map_number == SportsApp.MAP_TYPE_GAODE
//						&& nerActivityGaode != null) {
//					transaction.remove(nerActivityGaode);
//					nerActivityGaode = null;
//				}
//			}
//			if (app.mCurMapType == SportsApp.MAP_TYPE_GAODE) {
//				if (nerActivityGaode == null) {
//					local_map_number = app.mCurMapType;
//					nerActivityGaode = new NearbyActivityGaode();
//					transaction.add(R.id.main_fragment, nerActivityGaode);
//				} else {
//					// 如果ContactsFragment不为空，则直接将它显示出来
//					transaction.show(nerActivityGaode);
//					RelativeLayout ignored_view = (RelativeLayout) findViewById(R.id.nearbyActivity_gaode);
//					resideMenu.addIgnoredView(ignored_view);
//				}
//
//			} else {
//				if (nerActivity == null) {
//					local_map_number = app.mCurMapType;
//					// 如果ContactsFragment为空，则创建一个并添加到界面上
//					nerActivity = new NearbyActivity();
//					transaction.add(R.id.main_fragment, nerActivity);
//				} else {
//					// 如果ContactsFragment不为空，则直接将它显示出来
//					transaction.show(nerActivity);
//					RelativeLayout ignored_view = (RelativeLayout) findViewById(R.id.nearbyActivity_baidu);
//					resideMenu.addIgnoredView(ignored_view);
//				}
//			}
                break;
            case 4:
                // 点点运动圈
                if (sportsGroup == null) {
                    // 如果ContactsFragment为空，则创建一个并添加到界面上
                    sportsGroup = new SportsGroup();
                    transaction.add(R.id.main_fragment, sportsGroup);
                } else {
                    // 如果ContactsFragment不为空，则直接将它显示出来
                    transaction.show(sportsGroup);
                }
                break;
            case 5:
                // 好友
//			if (foxSportsFriends == null) {
//				// 如果ContactsFragment为空，则创建一个并添加到界面上
//				foxSportsFriends = new SportsFriends();
//				transaction.add(R.id.main_fragment, foxSportsFriends);
//			} else {
//				// 如果ContactsFragment不为空，则直接将它显示出来
//				transaction.show(foxSportsFriends);
//			}
                break;
            case 6:
                // 健康测试
                break;
            case 7:
                // 活动
                if (activityList == null) {
                    // 如果ContactsFragment为空，则创建一个并添加到界面上
                    activityList = new ActivityList();
                    transaction.add(R.id.main_fragment, activityList);
                } else {
                    // 如果ContactsFragment不为空，则直接将它显示出来
                    transaction.show(activityList);
                }

                break;

            case 9:
                // 金币规则
                // if (coolCurrencyRules == null) {
                // // 如果ContactsFragment为空，则创建一个并添加到界面上
                // coolCurrencyRules= new CoolCurrencyRules();
                // transaction.add(R.id.main_fragment,coolCurrencyRules);
                // } else {
                // // 如果ContactsFragment不为空，则直接将它显示出来
                // transaction.show(coolCurrencyRules);
                // }
                // 新版本 改成设置页面
                // Intent userIntent = new Intent(this, UserSetMainActivity.class);
                // startActivity(userIntent);
                break;
            case 11:
                isIndexTouch = 3;
                // 发现
                // if (findOtherFragment == null) {
                // // 如果ContactsFragment为空，则创建一个并添加到界面上
                // findOtherFragment = new FindOtherFragment();
                // transaction.add(R.id.main_fragment, findOtherFragment);
                // } else {
                // // 如果ContactsFragment不为空，则直接将它显示出来
                // transaction.show(findOtherFragment);
                // findOtherFragment.update();
                // }
                // 运动圈
                sports_group_text.setTextColor(Color.parseColor("#ffae00"));
                sports_group_image
                        .setImageResource(R.drawable.sk_sportcircle_focus);
                if (sportCircleMainFragment == null) {
                    // 如果ContactsFragment为空，则创建一个并添加到界面上
                    sportCircleMainFragment = new SportCircleMainFragment();
                    transaction.add(R.id.main_fragment, sportCircleMainFragment);
                } else {
                    // 如果ContactsFragment不为空，则直接将它显示出来
                    transaction.show(sportCircleMainFragment);
                    // sportCircleMainFragment.update();
                }

                break;
            case 12:
                // 我的运动
                isIndexTouch = 2;
                start_sport_text.setTextColor(Color.parseColor("#ffae00"));
                start_sport_image.setImageResource(R.drawable.sk_trainplay_focus);
//			if (app.mCurMapType == SportsApp.MAP_TYPE_GAODE) {
//				if (startSportFirstGaoDeFragment == null) {
//					// 如果MessageFragment为空，则创建一个并添加到界面上
//					Log.v(TAG, "wmh new StateActivity()");
//					startSportFirstGaoDeFragment = new StartSportFirstGaoDeFragment();
//					transaction.add(R.id.main_fragment,
//							startSportFirstGaoDeFragment);
//				} else {
//					// 如果MessageFragment不为空，则直接将它显示出来
//					Log.v(TAG, "wmh show StateActivity()");
//					transaction.show(startSportFirstGaoDeFragment);
//					LinearLayout ignored_view = (LinearLayout) findViewById(R.id.startsport_gaode_layout);
//					resideMenu.addIgnoredView(ignored_view);
//				}
//			}
                if (indexTrainPlanFragment == null) {
                    // 如果ContactsFragment为空，则创建一个并添加到界面上
                    indexTrainPlanFragment = new IndexTrainPlanFrg();
                    transaction.add(R.id.main_fragment, indexTrainPlanFragment);
                } else {
                    // 如果ContactsFragment不为空，则直接将它显示出来
                    transaction.show(indexTrainPlanFragment);
                    indexTrainPlanFragment.updateTip();
                }
//			else {
//				if (sportFirstBaiDuFragment == null) {
//					// 如果MessageFragment为空，则创建一个并添加到界面上
//					Log.v(TAG, "wmh new StateActivity()");
//					sportFirstBaiDuFragment = new StartSportFirstBaiDuFragment();
//					transaction
//							.add(R.id.main_fragment, sportFirstBaiDuFragment);
//				} else {
//					// 如果MessageFragment不为空，则直接将它显示出来
//					Log.v(TAG, "wmh show StateActivity()");
//					transaction.show(sportFirstBaiDuFragment);
//					LinearLayout ignored_view = (LinearLayout) findViewById(R.id.startsport_baidu_layout);
//					resideMenu.addIgnoredView(ignored_view);
//				}
//			}

                break;
            case 13:
                isIndexTouch = 4;
                // 健康
                sports_health_text.setTextColor(Color.parseColor("#ffae00"));
                sports_health_image.setImageResource(R.drawable.sk_health_focus);
                if (indexHealthFragment == null) {
                    // 如果ContactsFragment为空，则创建一个并添加到界面上
                    indexHealthFragment = new IndexHealthFrg();
                    transaction.add(R.id.main_fragment, indexHealthFragment);
                } else {
                    // 如果ContactsFragment不为空，则直接将它显示出来
                    transaction.show(indexHealthFragment);
                }
                break;

            case 14:
                isIndexTouch = 5;
                // 健康
                sports_me_text.setTextColor(Color.parseColor("#ffae00"));
                sports_me_image.setImageResource(R.drawable.sk_personal_focus);
                if (indexMeFragment == null) {
                    // 如果ContactsFragment为空，则创建一个并添加到界面上
                    indexMeFragment = new IndexMeFragment();
                    transaction.add(R.id.main_fragment, indexMeFragment);
                } else {
                    // 如果ContactsFragment不为空，则直接将它显示出来
                    transaction.show(indexMeFragment);
                }
                break;

            // 下面是之前点击开始运动
		/*
		 * case R.id.start_txt: SharedPreferences sp =
		 * getSharedPreferences("sport_state_" + app.getSportUser().getUid(),
		 * 0); int typeId = sp.getInt("typeId", 1); int typeDetailId =
		 * sp.getInt("typeDetailId", 0); if (typeId == 0 && typeDetailId > 0 ||
		 * typeId == 1 && typeDetailId > 0 || typeId == 2) { Intent intent;
		 * if(app.mCurMapType == SportsApp.MAP_TYPE_GAODE){ intent = new
		 * Intent(this, SportingMapActivityGaode.class); }else{ intent = new
		 * Intent(this, SportingMapActivity.class); } startActivity(intent); }
		 * else { if (isOPen(this)) { // if (isOpenLocation == false) { //
		 * Toast.makeText(this, "GPS信号弱，定位失败，请检查！", //
		 * Toast.LENGTH_SHORT).show(); // } else { Intent intent;
		 * if(app.mCurMapType == SportsApp.MAP_TYPE_GAODE){ intent = new
		 * Intent(this, SportingMapActivityGaode.class); }else{ intent = new
		 * Intent(this, SportingMapActivity.class); } startActivity(intent); //
		 * } } else { Bundle bundle = new Bundle(); bundle.putString("title",
		 * "提示"); bundle.putString("message", "您需要开启GPS定位,是否开启？");
		 * onCreateDialog(2, bundle); } } if (app.config == 1 && wService !=
		 * null) wService.setMe(true); break;
		 */
        }
        transaction
                .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        transaction.commitAllowingStateLoss();
    }

    private void hideFragments(FragmentTransaction transaction) {
        if (stateActivity != null) {
            transaction.hide(stateActivity);
        }
//		if (rankActivity != null) {
//			transaction.hide(rankActivity);
//		}
//		if (nerActivity != null) {
//			transaction.hide(nerActivity);
//		}
        if (foxSportsSetting != null) {
            transaction.hide(foxSportsSetting);
        }
        if (coolCurrencyRules != null) {
            transaction.hide(coolCurrencyRules);
        }
//		if (nerActivityGaode != null) {
//			transaction.hide(nerActivityGaode);
//		}

//		if (foxSportsFriends != null) {
//			transaction.hide(foxSportsFriends);
//		}
        if (activityList != null) {
            transaction.hide(activityList);
        }
        if (sportsGroup != null) {
            transaction.hide(sportsGroup);
        }
        if (findOtherFragment != null) {
            transaction.hide(findOtherFragment);
        }
        if (myFirstSportFragment != null) {
            transaction.hide(myFirstSportFragment);
        }
        if (sportCircleMainFragment != null) {
            transaction.hide(sportCircleMainFragment);
        }
        if (startSportFirstGaoDeFragment != null) {
            transaction.hide(startSportFirstGaoDeFragment);
        }
        if (indexHealthFragment != null) {
            transaction.hide(indexHealthFragment);
        }
        if (indexMeFragment != null){
            transaction.hide(indexMeFragment);
        }
        if (indexTrainPlanFragment != null) {
            transaction.hide(indexTrainPlanFragment);
        }
    }

    // What good method is to access resideMenu
    public ResideMenu getResideMenu() {
        return resideMenu;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK: {
                if (resideMenu.isOpened()) {
                    resideMenu.closeMenu();
                } else {
                    // ConfirmAction(this, getString(R.string.confirm_exit_title),
                    // getString(R.string.confirm_exit));
                    if (View.VISIBLE == rl_xinshouyindao.getVisibility()) {
                        rl_xinshouyindao.setVisibility(View.GONE);
                    } else {
                        exitPopWindow(getString(R.string.confirm_exit));
                    }
                }

                // ConfirmAction(this, getString(R.string.confirm_exit_title),
                // getString(R.string.confirm_exit));
                return true;
            }
            case KeyEvent.KEYCODE_HOME: {
                SharedPreferences sharedPreferences = getSharedPreferences(
                        "sports", Context.MODE_PRIVATE);
                Editor edit = sharedPreferences.edit();
                edit.putBoolean("isHome", true);
                edit.commit();
                return true;
            }
            case KeyEvent.KEYCODE_MENU: {
//                if (!resideMenu.isOpened()) {
//                    resideMenu.openMenu();
//                }
                // if (resideMenu.isOpened()) {
                // resideMenu.closeMenu();
                // }
                // stopService(new Intent(this, MessageService.class));
                // showExitPop();
//                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

//	private void ConfirmtoExit() {
//		Runnable runnable = new Runnable() {
//			public void run() {
//				LoginActivity.mTabActivityHasStart = false;
//				finish();
//			}
//		};
//		ConfirmAction(this, getString(R.string.confirm_exit_title),
//				getString(R.string.confirm_exit), runnable);
//	}

//	private void ConfirmAction(Context context, String title, String message,
//			final Runnable action) {
//		OnClickListener listener = new OnClickListener() {
//			public void onClick(DialogInterface dialog, int which) {
//				switch (which) {
//				case DialogInterface.BUTTON_POSITIVE:
//					if (action != null)
//						action.run();
//				}
//			}
//		};
//		new AlertDialog.Builder(context)
//				.setIcon(android.R.drawable.ic_dialog_alert).setTitle(title)
//				.setMessage(message)
//				.setPositiveButton(android.R.string.ok, listener)
//				.setNegativeButton(android.R.string.cancel, listener).create()
//				.show();
//	}

//	private void ConfirmAction(Context context, String title, String message) {
//		alertDialog = new Dialog(MainFragmentActivity.this,
//				R.style.sports_dialog);
//		LayoutInflater mInflater = getLayoutInflater();
//		View v = mInflater.inflate(R.layout.sports_dialog, null);
//		// View v = mInflater.inflate(R.layout.sports_dialog1, null);
//		((TextView) v.findViewById(R.id.message)).setText(message);
//		v.findViewById(R.id.bt_ok).setOnClickListener(
//				new View.OnClickListener() {
//					@Override
//					public void onClick(View arg0) {
//						if (alertDialog.isShowing())
//							alertDialog.dismiss();
//						LoginActivity.mTabActivityHasStart = false;
//						Intent intent = new Intent();
//						intent.setAction("com.fox.exercise.exits");
//						sendBroadcast(intent);
//						// 关闭服务器
//						unbindStepService();
//						stopService(new Intent(MainFragmentActivity.this,
//								FirstStepService.class));
//						Editor edit2 = msharedPreferences.edit();
//						edit2.putBoolean("mservice_running", false);
//						edit2.commit();
//
//						// finish();
//						waitShowDialog();
//						(new UpLoadTempTask()).execute();
//						app.LoginOption = true;
//						app.StartMessageTask();
//						app.setLogin(false);
//						app.setDataToLocal(detail);
//					}
//				});
//		v.findViewById(R.id.bt_cancel).setOnClickListener(
//				new View.OnClickListener() {
//					@Override
//					public void onClick(View arg0) {
//						alertDialog.dismiss();
//					}
//				});
//		v.setMinimumWidth((int) (SportsApp.ScreenWidth * 0.9));
//		alertDialog.setCancelable(true);
//		alertDialog.setCanceledOnTouchOutside(false);
//		alertDialog.setContentView(v);
//		alertDialog.show();
//	}

    private void exitPopWindow(String message) {
        if (myWindow != null && myWindow.isShowing())
            return;
        LayoutInflater inflater = LayoutInflater.from(this);
        myView = (LinearLayout) inflater.inflate(R.layout.sports_dialog1, null);
        ((TextView) myView.findViewById(R.id.message)).setText(message);

        myView.findViewById(R.id.bt_ok).setOnClickListener(new ExitClick());
        myView.findViewById(R.id.bt_cancel).setOnClickListener(new ExitClick());
        // myEditCalories
        // .setText(String.valueOf(getDate.getInt("editCalories", 0)));
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);

        int width = wm.getDefaultDisplay().getWidth();
        // int height = wm.getDefaultDisplay().getHeight();
        myWindow = new PopupWindow(myView, width - SportsApp.dip2px(20),
                RelativeLayout.LayoutParams.WRAP_CONTENT, true);
        myWindow.setAnimationStyle(R.style.AnimationPopup);
        myWindow.setOutsideTouchable(true);
        myWindow.setBackgroundDrawable(new BitmapDrawable());
//		myWindow.setBackgroundDrawable(null);
        myWindow.showAtLocation(mainLL, Gravity.CENTER, 0, 0);
        myWindow.setOnDismissListener(this);
        // final Animation animation = (Animation) AnimationUtils.loadAnimation(
        // this, R.anim.slide_in_from_bottom);
        // myView.startAnimation(animation);
        mPopMenuBack.setVisibility(View.VISIBLE);
    }

    // 发送今日步数到服务器
    class UpLoadTempTask extends AsyncTask<Integer, Integer, ApiBack> {

        @Override
        protected ApiBack doInBackground(Integer... arg0) {
            // TODO Auto-generated method stub
            ApiBack apiBack = null;
            try {
                if (!"".equals(app.getSessionId())
                        && app.getSessionId() != null) {
                    // SharedPreferences mState = getSharedPreferences("UID"
                    // + Integer.toString(app.getSportUser().getUid()), 0);
                    // int mint = mState.getInt("steps", 0);
                    apiBack = ApiJsonParser.uploadSportTemp(app.getSessionId(),
                            CalcStepCounter());
                } else {
                    apiBack = null;
                }

            } catch (ApiNetException e) {
                e.printStackTrace();
            } catch (ApiSessionOutException e) {
                e.printStackTrace();
            }
            return apiBack;
        }

        @Override
        protected void onPostExecute(ApiBack result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            if (result != null) {
            }
            if (mLoadProgressDialog != null)
                if (mLoadProgressDialog.isShowing())
                    mLoadProgressDialog.dismiss();
            app.LoginOption = true;
            app.StartMessageTask();
            app.setLogin(false);
            app.setDataToLocal(detail);
            LocationInfo.latitude = "";
            LocationInfo.longitude = "";
            finish();

        }

    }

    // 从数据库查询到今日运动公里数再得到今日步数
    private int CalcStepCounter() {
        double sports_juli = 0;
        int sports_cishu = 0;

        List<SportContionTaskDetail> list = null;
        SportSubTaskDB taskDBhelper = SportSubTaskDB.getInstance(this);

        // list = taskDBhelper.getTasksByDate(mUid, getStringDate(0));
        list = taskDBhelper.getTasksByDate(app.getSportUser().getUid(),
                getTodayDate());
        sports_cishu = list.size();

        if (sports_cishu > 0) {
            for (int i = 0; i < sports_cishu; i++) {
                // 只有登山 走路 跑步才计算步数
                if (list.get(i).getSports_type() == SportsType.TYPE_WALK
                        || list.get(i).getSports_type() == SportsType.TYPE_RUN
                        || list.get(i).getSports_type() == SportsType.TYPE_CLIMBING) {
                    if (SportTaskUtil.getNormalRange(list.get(i)
                                    .getSports_type(), list.get(i).getSportVelocity(),
                            Integer.parseInt(list.get(i).getSportTime()))) {
                        sports_juli += list.get(i).getSportDistance();
                    }
                }
            }
        } else {
            sports_juli = 0;
        }

        sports_cishu = (int) (sports_juli * 10000 / 6);
        return sports_cishu;
    }

    private String getTodayDate() {
        String todayDate = null;
        Long todayTime = System.currentTimeMillis();
        Date date = new Date(todayTime);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        todayDate = sdf.format(date);
        Log.i("sport_sub", "todayDate:" + todayDate);
        return todayDate;
    }

    @Override
    protected Dialog onCreateDialog(int id, Bundle args) {

        switch (id) {
            case 1:
                SharedPreferences sharedPreferences = getSharedPreferences(
                        "sports", Context.MODE_PRIVATE);
                final Editor edit = sharedPreferences.edit();
                Dialog dialog = new AlertDialog.Builder(this)
                        .setTitle("计步需要运行在后台吗？")
                        .setMessage("提示：后台计步会有些耗电哦!")
                        .setPositiveButton("需要",
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        // 调用HOME键
                                        Intent notificationIntent = null;
                                        Notification n = new Notification();
                                        n.flags |= Notification.FLAG_SHOW_LIGHTS;
                                        n.flags |= Notification.FLAG_AUTO_CANCEL;
                                        n.icon = R.drawable.icon;
                                        n.when = System.currentTimeMillis();
                                        notificationIntent = new Intent(
                                                getApplication(),
                                                MainFragmentActivity.class);
                                        notificationIntent
                                                .addCategory(Intent.CATEGORY_LAUNCHER);
                                        PendingIntent pi = PendingIntent
                                                .getActivity(
                                                        getApplication(),
                                                        0,
                                                        notificationIntent,
                                                        PendingIntent.FLAG_UPDATE_CURRENT);
                                        n.setLatestEventInfo(getApplication(),
                                                "云狐运动", "正在运行···", pi);
                                        mNotifMan.notify(1, n);

                                        edit.putBoolean("isHome", true);
                                        edit.commit();
                                        Intent intent = new Intent(
                                                Intent.ACTION_MAIN);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        intent.addCategory(Intent.CATEGORY_HOME);
                                        getApplication().startActivity(intent);
                                    }
                                })
                        .setNegativeButton("不需要",
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        edit.putBoolean("isHome", false);
                                        edit.commit();
                                        Intent intent = new Intent();
                                        intent.setAction("com.fox.exercise.exits");
                                        sendBroadcast(intent);
                                        finish();
                                        // stopService(new
                                        // Intent(MainFragmentActivity.this,
                                        // FirstStepService.class));
                                        // if(isOPen(getApplication())){
                                        // toggleGPS(getApplication(), false);
                                        // }
                                        // System.exit(0);
                                    }
                                }).create();
                return dialog;
            case 2:
                String title = args.getString("title");
                String message = args.getString("message");
                AlertDialog.Builder builder = new Builder(this);
                builder.setMessage(message);
                builder.setTitle(title);
                builder.setPositiveButton(getString(R.string.button_ok),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                Intent intent = new Intent(
                                        Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivityForResult(intent, 1);
                            }
                        });
                builder.setNegativeButton(getString(R.string.button_cancel),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                Dialog dialog1 = builder.create();
                dialog1.setCanceledOnTouchOutside(false);
                dialog1.show();
                break;
        }
        return super.onCreateDialog(id, args);
    }

    /**
     * 判断GPS是否开启，GPS或者AGPS开启一个就认为是开启的
     *
     * @param context
     * @return true 表示开启
     */
    public static final boolean isOPen(final Context context) {
        LocationManager locationManager = (LocationManager) context
                .getSystemService(Context.LOCATION_SERVICE);
        // 通过GPS卫星定位，定位级别可以精确到街（通过24颗卫星定位，在室外和空旷的地方定位准确、速度快）
        boolean gps = locationManager
                .isProviderEnabled(LocationManager.GPS_PROVIDER);
        // 通过WLAN或移动网络(3G/2G)确定的位置（也称作AGPS，辅助GPS定位。主要用于在室内或遮盖物（建筑群或茂密的深林等）密集的地方定位）
        // boolean network =
        // locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (gps) {
            return true;
        }
        return false;
    }

    /**
     * GPS开关管理
     *
     * @param context
     */
    public void toggleGPS(Context context, Boolean isOn) {
        Intent intent = new Intent("android.location.GPS_ENABLED_CHANGE");
        intent.putExtra("enabled", isOn);
        sendBroadcast(intent);
        if (isOPen(context) == true && isOn == false) {
            String provider = Settings.Secure.getString(getContentResolver(),
                    Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            if (!provider.contains("gps")) { // if gps is disabled
                final Intent poke = new Intent();
                poke.setClassName("com.android.settings",
                        "com.android.settings.widget.SettingsAppWidgetProvider");
                poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
                poke.setData(Uri.parse("3"));
                sendBroadcast(poke);
            }
            Log.i("toggleGPS", "GPS已开启!");
        } else {
            Log.i("toggleGPS", "GPS未开启!");
        }
    }

	/*
	 * @Override public boolean onCreateOptionsMenu(Menu menu) {
	 * if(!hasSmartBar()){ return false; }else{ MenuInflater inflater =
	 * getMenuInflater(); inflater.inflate(R.menu.meizu_bar, menu); return true;
	 * } } private boolean hasSmartBar() { try { Method method =
	 * Class.forName("android.os.Build").getMethod("hasSmartBar"); return
	 * ((Boolean) method.invoke(null)).booleanValue(); } catch (Exception e) { }
	 * if (Build.DEVICE.equals("mx2")) { return true; } else if
	 * (Build.DEVICE.equals("mx") || Build.DEVICE.equals("m9")) {
	 *
	 * return false; } return false; } boolean boolBar=false;
	 *
	 * @Override public boolean onOptionsItemSelected(MenuItem item) { // TODO
	 * Auto-generated method stub Log.i("", "lulu item"+item.getItemId());
	 * switch (item.getItemId()) { case R.id.nearby_menu: if (app.LoginOption) {
	 * nerActivity.initlocation(); mTabHost.setCurrentTabByTag("nearby");
	 * moveSelection(4); if (app.config == 1 && wService != null)
	 * wService.setMe(true); }else {
	 * app.TyrLoginAction(MainFragmentActivity.this
	 * ,getString(R.string.sports_love_title),
	 * getString(R.string.try_to_login)); }
	 *
	 * break; case R.id.more_menu: mTabHost.setCurrentTabByTag("more");
	 * moveSelection(5); if (mMsgTxt != null && mMsgTxt.VISIBLE == View.VISIBLE)
	 * { mMsgTxt.setVisibility(View.GONE); } if (app.config == 1 && wService !=
	 * null) wService.setMe(false); break; default: break; } boolBar=true;
	 * return super.onOptionsItemSelected(item); }
	 */
	/*
	 * @Override public void onTabReselected(Tab arg0, FragmentTransaction arg1)
	 * { // TODO Auto-generated method stub
	 * Log.i("","lulu arg0"+arg0.getPosition()); Intent intent;
	 * tabListen(arg0.getPosition()); }
	 *
	 * @Override public void onTabSelected(Tab arg0, FragmentTransaction arg1) {
	 * // TODO Auto-generated method stub Log.i("",
	 * "lulu arg0"+arg0.getPosition()); Intent intent;
	 * tabListen(arg0.getPosition()); } int positions;
	 *
	 * @Override public void onTabUnselected(Tab arg0, FragmentTransaction arg1)
	 * { // TODO Auto-generated method stub
	 * Log.i("","lulu arg0"+arg0.getPosition()); }
	 *
	 * public void tabListen(int position){ if(mTabHost==null) return; Intent
	 * intent; switch (position) { case 0: mTabHost.setCurrentTabByTag("state");
	 * moveSelection(1); if (app.config == 1 && wService != null)
	 * wService.setMe(true); break; case 1: if (app.LoginOption) {
	 * mTabHost.setCurrentTabByTag("rank"); moveSelection(2); if (app.config ==
	 * 1 && wService != null) wService.setMe(true); }else {
	 * app.TyrLoginAction(MainFragmentActivity
	 * .this,getString(R.string.sports_love_title),
	 * getString(R.string.try_to_login)); } break; case 2: SharedPreferences sp
	 * = getSharedPreferences("sport_state_" + app.getSportUser().getUid(), 0);
	 * int typeId = sp.getInt("typeId", 1); int typeDetailId =
	 * sp.getInt("typeDetailId", 0); if (typeId == 0 && typeDetailId > 0 ||
	 * typeId == 1 && typeDetailId > 0 || typeId == 2) { Intent intent1;
	 * if(app.mCurMapType == SportsApp.MAP_TYPE_GAODE){ intent1 = new
	 * Intent(this, SportingMapActivityGaode.class); }else{ intent1 = new
	 * Intent(this, SportingMapActivity.class); } startActivity(intent1); } else
	 * { if (isOPen(this)) { // if (isOpenLocation == false) { //
	 * Toast.makeText(this, "GPS信号弱，定位失败，请检查！", // Toast.LENGTH_SHORT).show();
	 * // } else { Intent intent2; if(app.mCurMapType ==
	 * SportsApp.MAP_TYPE_GAODE){ intent2 = new Intent(this,
	 * SportingMapActivityGaode.class); }else{ intent2 = new Intent(this,
	 * SportingMapActivity.class); } startActivity(intent2); // } } else {
	 * Bundle bundle = new Bundle(); bundle.putString("title", "提示");
	 * bundle.putString("message", "您需要开启GPS定位,是否开启？"); onCreateDialog(2,
	 * bundle); } } if (app.config == 1 && wService != null)
	 * wService.setMe(true); break; default: break; } }
	 */

//	class GetTimeListThread extends Thread {
//
//		public GetTimeListThread() {
//		}
//
//		@Override
//		public void run() {
//			try {
//				List<CurrentTimeList> list = ApiJsonParser.getTaskTimeById(
//						mSessionID, mUid);
//				if (list != null && list.size() > 0) {
//					Log.v(TAG, "wmh list!=null");
//					SportsTimeDB db = SportsTimeDB
//							.getInstance(MainFragmentActivity.this);
//					for (int i = 0; i < list.size(); i++) {
//						String time = list.get(i).getCurrentTime();
//						Log.v(TAG, "wmh time=" + time);
//						Cursor cursor = db.query(mUid, time);
//						try {
//							if (!cursor.moveToFirst()) {
//								Log.v(TAG, "wmh time not exist " + time);
//								SimpleDateFormat sdf = new SimpleDateFormat(
//										"yyyy-MM-dd");
//								long millionSeconds = sdf.parse(time).getTime();// 毫秒
//
//								ContentValues values = new ContentValues();
//								values.put(SportsTimeDB.UID, mUid);
//								values.put(SportsTimeDB.TIME, millionSeconds);
//								values.put(SportsTimeDB.DATE, time);
//								db.insert(values);
//							}
//						} catch (Exception e) {
//							e.printStackTrace();
//						} finally {
//							if (cursor != null) {
//								try {
//									cursor.close();
//								} catch (Exception e2) {
//									e2.printStackTrace();
//								}
//							}
//						}
//
//					}
//					db.close();
//				}
//			} catch (ApiNetException e) {
//				SportsApp.eMsg = Message.obtain(mExceptionHandler,
//						SportsExceptionHandler.NET_ERROR);
//				SportsApp.eMsg.sendToTarget();
//				e.printStackTrace();
//			} catch (ApiSessionOutException e) {
//				e.printStackTrace();
//				SportsApp.eMsg = Message.obtain(mExceptionHandler,
//						SportsExceptionHandler.SESSION_OUT);
//				SportsApp.eMsg.sendToTarget();
//			}
//		}
//	}

    // 修改APP版本
    private void UpdateApp() {
        Log.v(TAG, "UpdateApp in");
        UpdataApplication updataApplication = new UpdataApplication(this);
        try {
            Log.v(TAG, "UpdateAppBackground begin");
            updataApplication.UpdateAppBackground(true);
            Log.v(TAG, "UpdateAppBackground end");
        } catch (Exception e) {
            e.printStackTrace();
            Log.v(TAG, "UpdateAppBackground err");
        }
    }

    /**
     * 清除掉所有的选中状态。
     */
    private void clearSelection() {
        mysport_text.setTextColor(Color.parseColor("#333333"));
        start_sport_text.setTextColor(Color.parseColor("#333333"));
        sports_group_text.setTextColor(Color.parseColor("#333333"));
        sports_health_text.setTextColor(Color.parseColor("#333333"));
        sports_me_text.setTextColor(Color.parseColor("#333333"));
        mysport_image.setImageResource(R.drawable.sk_startsport_normal);
        start_sport_image.setImageResource(R.drawable.sk_trainplay_normal);
        sports_group_image.setImageResource(R.drawable.sk_sportcircle_normal);
        sports_health_image.setImageResource(R.drawable.sk_health_normal);
        sports_me_image.setImageResource(R.drawable.sk_personal_normal);
    }

    protected Dialog onCreateDialogs(int id, Bundle args) {
        switch (id) {
            case 2:
                String title = args.getString("title");
                String message = args.getString("message");
                AlertDialog.Builder builder = new Builder(this);
                builder.setMessage(message);
                builder.setTitle(title);
                builder.setPositiveButton(getString(R.string.button_ok),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                Intent intent = new Intent(
                                        Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivityForResult(intent, 1);
                            }
                        });
                builder.setNegativeButton(getString(R.string.button_cancel),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                Dialog dialog = builder.create();
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();
                break;
        }
        return super.onCreateDialog(id, args);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                if (isOPen(this)) {
                    setSelection(12);
                } else {
                    // 不做操作
                }
                break;
            case 2:
                if (isIndexTouch == 2) {
                    if (resideMenu.isOpened()) {
                        resideMenu.closeMenu();
                    }
                    setSelection(12);
                }
                break;
            case 3:
                initFragments();
                break;
            // case FOXSPORTSETTINGRESULT:
            // setSelection(12);
            // resideMenu.closeMenu();
            // if (itemList != null) {
            // for (int i = 0; i < itemList.size(); i++) {
            // itemList.get(i).setBackgroundColor(
            // getResources().getColor(android.R.color.transparent));
            // }
            // itemList.clear();
            // }
            // break;
        }
    }

    /**
     * 弹出退出框PopupWindow
     */
//	public void showExitPop() {
//		LayoutInflater inflater = LayoutInflater.from(this);
//		LinearLayout mview = (LinearLayout) inflater.inflate(
//				R.layout.exit_login, null);
//		mview.findViewById(R.id.exit_login).setOnClickListener(
//				new ExitListener());
//		mview.findViewById(R.id.exit_cancel).setOnClickListener(
//				new ExitListener());
//		window = new PopupWindow(mview, LinearLayout.LayoutParams.FILL_PARENT,
//				LinearLayout.LayoutParams.WRAP_CONTENT, true);
//		// 设置弹出框大小
//		// window.setHeight(300);
//		// 设置出场动画：
//		window.setAnimationStyle(R.style.AnimationPopup);
//		window.setOutsideTouchable(true);// 设置PopupWindow外部区域是否可触摸
//		window.setBackgroundDrawable(new BitmapDrawable());
//		// relativelayout_four personal_settings userImage
//		window.showAtLocation(mainLL, Gravity.BOTTOM, 0, 0);
//		// window.showAtLocation(, Gravity.BOTTOM, 0, 0);
//		// window.showAsDropDown(relativelayout_four);
//		window.setOnDismissListener(this);
//		// 设置进场动画
//		final Animation animation = (Animation) AnimationUtils.loadAnimation(
//				this, R.anim.slide_in_from_bottom);
//		mview.startAnimation(animation);
//		// 点击退出设置阴影
//		mPopMenuBack.setVisibility(View.VISIBLE);
//	}
    @Override
    public void onDismiss() {
        // TODO Auto-generated method stub
        mPopMenuBack.setVisibility(View.GONE);
    }

//	class ExitListener implements android.view.View.OnClickListener {
//
//		@Override
//		public void onClick(View view) {
//			// TODO Auto-generated method stub
//			switch (view.getId()) {
//			case R.id.exit_login:
//				if (window != null && window.isShowing()) {
//					window.dismiss();
//				}
//
//				SharedPreferences sp = getSharedPreferences("user_login_info",
//						Context.MODE_PRIVATE);
//				Editor edit = sp.edit();
//				// edit.clear();
//				// edit.commit();
//				edit.remove("account").commit();
//				stopService(new Intent(MainFragmentActivity.this,
//						MessageService.class));
//				stopService(new Intent(MainFragmentActivity.this,
//						FirstStepService.class));
//				Editor edit2 = msharedPreferences.edit();
//				edit2.putBoolean("mservice_running", false);
//				edit2.commit();
//
//				startActivity(new Intent(MainFragmentActivity.this,
//						LoginActivity.class));
//				System.exit(0);
//				break;
//			case R.id.exit_cancel:
//				if (window != null && window.isShowing()) {
//					window.dismiss();
//				}
//				break;
//			}
//
//		}
//
//	}

    public void startStepService() {
        Log.i(TAG, "[SERVICE] Start");
        startService(new Intent(this, FirstStepService.class));
        // mIsRunning = true;
        // Editor edit = mSettings.edit();
        Editor edit = msharedPreferences.edit();
        edit.putBoolean("mservice_running", true);
        edit.commit();
    }

    public void bindStepService() {
        Log.i(TAG, "[SERVICE] Bind");
        bindService(new Intent(this, FirstStepService.class), mConnection,
                Context.BIND_AUTO_CREATE + Context.BIND_DEBUG_UNBIND);
    }

    public void unbindStepService() {
        Log.i(TAG, "[SERVICE] Unbind");
        unbindService(mConnection);
    }

    public void stopStepService() {
        Log.i(TAG, "[SERVICE] Stop");
        Log.i(TAG, "[SERVICE] stopService");
        stopService(new Intent(this, FirstStepService.class));
        Editor edit = msharedPreferences.edit();
        edit.putBoolean("mservice_running", false);
        edit.commit();
    }

    private FirstStepService mService;

    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            mService = ((FirstStepService.StepBinder) service).getService();
            mService.registerCallback(mCallback);
            mService.reloadSettings();

        }

        public void onServiceDisconnected(ComponentName className) {
            mService = null;
        }
    };

    private FirstStepService.ICallback mCallback = new FirstStepService.ICallback() {
        public void stepsChanged(int value) {
            Handler getmHandler = app.getmHandler();
            if (getmHandler != null) {
                getmHandler.sendMessage(getmHandler.obtainMessage(
                        MyFirstSportFragment.STEPS_MSG, value, 0));
            }
        }

        public void paceChanged(int value) {
            // mHandler.sendMessage(mHandler.obtainMessage(PACE_MSG, value, 0));
        }

        public void distanceChanged(float value) {
            // mHandler.sendMessage(mHandler.obtainMessage(DISTANCE_MSG,
            // (int)(value*1000), 0));
        }

        public void speedChanged(float value) {
            // mHandler.sendMessage(mHandler.obtainMessage(SPEED_MSG,
            // (int)(value*1000), 0));
        }

        public void caloriesChanged(float value) {
            // mHandler.sendMessage(mHandler.obtainMessage(CALORIES_MSG,
            // (int)(value), 0));
        }

    };

    private void waitShowDialog() {
        // TODO Auto-generated method stub
        if (mLoadProgressDialog == null) {
            mLoadProgressDialog = new Dialog(mContext, R.style.sports_dialog);
            LayoutInflater mInflater = getLayoutInflater();
            View v1 = mInflater.inflate(R.layout.bestgirl_progressdialog, null);
            mDialogMessage = (TextView) v1.findViewById(R.id.message);
            mDialogMessage.setText(R.string.bestgirl_wait);
            v1.setMinimumWidth((int) (SportsApp.ScreenWidth * 0.8));
            mLoadProgressDialog.setContentView(v1);
        }
        if (mLoadProgressDialog != null)
            if (!mLoadProgressDialog.isShowing() && !this.isFinishing())
                mLoadProgressDialog.show();
        Log.i(TAG, "isFirstshow----");
    }



    // 得到今日步数服务器
//	class GetTempTask extends AsyncTask<Integer, Integer, TodayStepBean> {
//
//		@Override
//		protected TodayStepBean doInBackground(Integer... arg0) {
//			// TODO Auto-generated method stub
//			TodayStepBean todayStep = null;
//			try {
//				if (!"".equals(app.getSessionId())
//						&& app.getSessionId() != null) {
//					todayStep = ApiJsonParser.getTodaySportsTemp(app
//							.getSessionId());
//				} else {
//					todayStep = null;
//				}
//
//			} catch (ApiNetException e) {
//				e.printStackTrace();
//			} catch (ApiSessionOutException e) {
//				e.printStackTrace();
//			}
//			return todayStep;
//		}
//
//		@Override
//		protected void onPostExecute(TodayStepBean result) {
//			// TODO Auto-generated method stub
//			super.onPostExecute(result);
//			if (result != null) {
//				SharedPreferences mState = getSharedPreferences(
//						"UID" + Integer.toString(app.getSportUser().getUid()),
//						0);
//				int mint = mState.getInt("steps", 0);
//				if (result.getStep_num() > mint) {
//					Editor edit = mState.edit();
//					edit.putInt("steps", result.getStep_num());
//					edit.commit();
//				}
//			}
//			if (mLoadProgressDialog != null)
//				if (mLoadProgressDialog.isShowing())
//					mLoadProgressDialog.dismiss();
//
//			// mIsRunning = msharedPreferences.getBoolean("mservice_running",
//			// false);
//			// if (!mIsRunning) {
//			// startStepService();
//			// bindStepService();
//			// } else {
//			// bindStepService();
//			// }
//
//		}
//
//	}

    class ExitClick implements android.view.View.OnClickListener {

        @Override
        public void onClick(View view) {
            // TODO Auto-generated method stub
            switch (view.getId()) {
                case R.id.bt_ok:
                    myWindow.dismiss();
                    myView.setVisibility(View.GONE);
                    mPopMenuBack.setVisibility(View.GONE);
                    LoginActivity.mTabActivityHasStart = false;
                    Intent intent = new Intent();
                    intent.setAction("com.fox.exercise.exits");
                    sendBroadcast(intent);
                    // 关闭服务器
                    // unbindStepService();
                    // stopService(new Intent(MainFragmentActivity.this,
                    // FirstStepService.class));
                    // Editor edit2 = msharedPreferences.edit();
                    // edit2.putBoolean("mservice_running", false);
                    // edit2.commit();

                    // finish();

//                    if (app.isOpenNetwork()) {
//                        waitShowDialog();
//                        (new UpLoadTempTask()).execute();
//                    } else {
                        app.LoginOption = true;
                        app.StartMessageTask();
                        app.setLogin(false);
                        app.setDataToLocal(detail);
                        LocationInfo.latitude = "";
                        LocationInfo.longitude = "";
                        finish();
//                    }
                    break;
                case R.id.bt_cancel:
                    if (myWindow != null) {
                        myWindow.dismiss();
                    }
                    myView.setVisibility(View.GONE);
                    mPopMenuBack.setVisibility(View.GONE);

                    break;
                default:
                    break;
            }
        }

    }

    // BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
    //
    // @Override
    // public void onReceive(Context context, Intent intent) {
    // // TODO Auto-generated method stub
    // unbindStepService();
    // }
    // };

    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        Configuration config=new Configuration();
        config.setToDefaults();
        res.updateConfiguration(config,res.getDisplayMetrics() );
        return res;
    }

}
