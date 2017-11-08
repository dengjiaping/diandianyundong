package com.fox.exercise.newversion.act;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.provider.Settings;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.TextAppearanceSpan;
import android.text.style.TypefaceSpan;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.fox.exercise.AbstractBaseFragment;
import com.fox.exercise.ActivityInfoWebView;
import com.fox.exercise.FoxSportsState;
import com.fox.exercise.HistoryAllActivity;
import com.fox.exercise.ImageDownloader;
import com.fox.exercise.R;
import com.fox.exercise.SportsType;
import com.fox.exercise.Utils;
import com.fox.exercise.YunHuWebViewActivity;
import com.fox.exercise.api.ApiBack;
import com.fox.exercise.api.ApiConstant;
import com.fox.exercise.api.ApiJsonParser;
import com.fox.exercise.api.ApiNetException;
import com.fox.exercise.api.ApiSessionOutException;
import com.fox.exercise.api.ShowCoinsDialog;
import com.fox.exercise.api.entity.ActivityInfo;
import com.fox.exercise.api.entity.NewCommentInfo;
import com.fox.exercise.api.entity.SportTask;
import com.fox.exercise.api.entity.UserDetail;
import com.fox.exercise.bitmap.util.ImageResizer;
import com.fox.exercise.db.SportSubTaskDB;
import com.fox.exercise.http.FunctionStatic;
import com.fox.exercise.login.BiaoZhuScrollLayout.OnViewChangeListener;
import com.fox.exercise.login.LocationInfo;
import com.fox.exercise.login.SportMain;
import com.fox.exercise.map.SportingMapActivityGaode;
import com.fox.exercise.map.VoicePrompt;
import com.fox.exercise.newversion.Tools;
import com.fox.exercise.newversion.bushutongji.BuShuTongJiBarChart;
import com.fox.exercise.newversion.entity.ExternalActivi;
import com.fox.exercise.newversion.newact.NightRunWebViewActivity;
import com.fox.exercise.pedometer.ImageWorkManager;
import com.fox.exercise.pedometer.SportContionTaskDetail;
import com.fox.exercise.util.SportTaskUtil;
import com.umeng.analytics.MobclickAgent;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cn.ingenic.indroidsync.SportsApp;

/**
 * @author loujungang 我的运动页面 应用首先进入的页面
 */
@SuppressLint("ResourceAsColor")
public class MyFirstSportFragment extends AbstractBaseFragment implements
        OnClickListener, OnDismissListener, OnViewChangeListener,
        OnTouchListener, LocationListener, AMapLocationListener {
    private static final String TAG = "name.bagi.levente.pedometer.StepService";
    private SportsApp mSportsApp;
    private static Context context;

    private int mUid;
    private int mSelectTypeID = 1;
    private Dialog alertDialog;
    private SharedPreferences sharedPreferences;
    private SharedPreferences spf;
    private ExecutorService cachedThreadExecutor;
//    public static LocationClient mClient = null;
//    public static MyLocationListenner myListener;
    private static int type = 0;
    private boolean ischangeSportType = false;

    private ImageView bind_device_text;// 右边按钮


    private TextView user_allsport_licheng;// 历史总行程
    private TextView dis_xiao_1;
    private TextView dis_xiao_2;
    private ImageView iv_historyallbtn;// 跳转到运动记录
    private ImageView iv_sportstyle;// 弹出运动类型菜单
    private RelativeLayout rl_startsport_first;// 开始运动

    private TextView yundong_cishu;// 共运动次数
    private TextView yundong_laluli;// 消耗卡路里
    private TextView yundong_di_day;// 运动天数
    private ImageView image_sports_complete_rate;
    //private ImageView  image_sports_complete_point;
    private ImageView itemCollMall;
    /*
     * 倒计时功能属性
	 */
    private Vibrator djs_vibrator;
    private LinearLayout djs_layout;
    private ImageView djs_image;
    private Animation djs_animation;
    private int djs_count = 0;
    private int djs_counts = 0;
    private Boolean djs_zd = true;
    private SharedPreferences djs_sp;
    private int[] myDjsImage = new int[]{R.drawable.djs_go, R.drawable.djs_1,
            R.drawable.djs_2, R.drawable.djs_3, R.drawable.djs_4,
            R.drawable.djs_5, R.drawable.djs_6, R.drawable.djs_7,
            R.drawable.djs_8, R.drawable.djs_9, R.drawable.djs_10};
    // 语音提示
    private VoicePrompt vp;
    private LocationManager locationManager = null;
    private NewCommentInfo commentInfo = new NewCommentInfo();
    // private int type2 = -1;
    private int typeId = 0;
    private int typeDetailId = 0;
    private PopupWindow mSportWindow = null;
    private String[] mSportTypeNames;
    private MyAdapter myAdapter;
    private int[] mSporTypeImage = new int[]{R.drawable.zoulu_black,
            R.drawable.paobu_black, R.drawable.qixing_black,
            R.drawable.dengshan_black};
    private int[] mSporTypeImage_yellow = new int[]{R.drawable.zoulu_black2,
            R.drawable.paobu_black2, R.drawable.qixing_black2,
            R.drawable.dengshan_black2};
    private int[] mFirstpagetype = new int[]{R.drawable.sportstyle_jianzou,
            R.drawable.sportstyle_paobu, R.drawable.sportstyle_qixing,
            R.drawable.sportstyle_dengshan};

    public static final int SPORT_GOAL = 0;
    public static final int SPORT_STATE = 1;
    public static final int SPORT_DEVICE = 2;
    public static final int SPORT_MAP = 3;
    public static final int SPORT_DEL = 4;

    protected static final int READSTEPS_MSG = 1;
    public static final int LAST_SPORTS = 0x3;
    public static final int UPDATE_HEADER = 0x4;
    public static final int UPDATE_INVITE = 0x6;
    public static final int UPDATE_TRYTOLOGIN = 0x9;
    public static final int UPDATE_ANIMATION = 0x10;
    public static final int STOP_ANIMATION = 0x11;
    public static final int UPDATE_ANIMATION_TODAY = 0x12;
    public static final int STOP_ANIMATION_TODAY = 0x13;

    public static final int ANIMATION_TOTAL_DIS = 1;
    public static final int ANIMATION_TODAY_DIS = 2;
    public static final int REFRESHPHOTO = 121;
    public static final int STEPS_MSG = 122;
    public static final int REFRESH_USER = 123;

    private SportSubTaskDB mdb;
    public static boolean mIn = false;

    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    //声明AMapLocationClientOption对象
    public AMapLocationClientOption mLocationOption = null;
    private SportSubTaskDB histDB;
    private boolean history_see = false;
    private int mTimes = 0;
    private TextView tipNumtx;

    AnimationSet animationSet1;
    AnimationSet animationSet2;
    TranslateAnimation translateAnimation1;
    TranslateAnimation translateAnimation2;

    AnimationSet animationSetDis1;
    AnimationSet animationSetDis2;
    TranslateAnimation translateAnimationDis1;
    TranslateAnimation translateAnimationDis2;

    AnimationSet animationSetCal1;
    AnimationSet animationSetCal2;
    TranslateAnimation translateAnimationCal1;
    TranslateAnimation translateAnimationCal2;

    private long preTime = 0;

    private AnimationDrawable mAnimation;
    private String times;

    private RelativeLayout sports_goal_layout;
    private TextView mubiao_content, mubiao_content_title;

    private String cityname;
    private double todayDis = 0.00f;
    private View view;


    private LinearLayout image_sports_complete_layout;// 移动的目标箭头layout
    private TextView image_sports_complete_content;

    private PopupWindow myWindow, tanChuPop = null;
    private RelativeLayout myView;
    private EditText edit_step;
    private RelativeLayout mPopMenuBack;


    private List<SportContionTaskDetail> list_one,list_visitor;

    private ExternalActivi exActivi;// 活动对象
    private ActivityInfo activityDetailInfo = null;// 活动详情信息

    private MsgReceiver msgReceiver;
    private RelativeLayout rl_bushu;
    private TextView tv_jinri, tv_xiaohao, iv_beishu;
    private ImageView iv_tubiao, tv_yuedengyu;
    private RadioGroup radioGroup_goal;
    private RadioButton bushu,gongli;
    private TextView tv_bushuorgongli;
    private int selectgoal;//运动目标标识 0代表步数 1代表公里

    private SharedPreferences data;
    private SharedPreferences.Editor editor;

    private SharedPreferences hSpf;
    private int steps;
    private double calories;
    private int yangRouChuan = 40;
    private int hanBao = 248;
    private int zhiShiDanGao = 300;
    private int jiTui = 400;
    private int count;
    UserDetail detail;

    @Override
    public void beforeInitView() {
        // TODO Auto-generated method stub
        if(isAdded()){
            title = getActivity().getResources().getString(
                    R.string.start_sports_msg);
        }
    }

    @Override
    public void setViewStatus() {
        // TODO Auto-generated method stub
        context = getActivity();
        view = LayoutInflater.from(getActivity()).inflate(
                R.layout.mysport_first_frag2, null);
        setContentViews(view);
        mSportsApp = (SportsApp) getActivity().getApplication();
        mSportsApp.addActivity(getActivity());
        mUid = SportsApp.getInstance().getSportUser().getUid();
        sharedPreferences = getActivity().getSharedPreferences("sports" + mUid,
                0);
        spf = getActivity().getSharedPreferences("sports", 0);
        hSpf = getActivity().getSharedPreferences("user_login_info", Context.MODE_PRIVATE);
        mdb = SportSubTaskDB.getInstance(getActivity());

        left_ayout.setOnClickListener(new clickListener());
        iButton = new ImageButton(getActivity());
        iButton.setBackgroundDrawable(getResources().getDrawable(
                R.drawable.fristpage_jinbimall));
        iButton.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        left_ayout.setPadding(0, 0, mSportsApp.dip2px(17), 0);
        iButton.setOnClickListener(new clickListener());
        if (left_ayout != null) {
            left_ayout.removeAllViews();
            left_ayout.addView(iButton);
        }

        // 设置日期格式 获取当天的日期
        SimpleDateFormat sf1 = new SimpleDateFormat("yyyyMMdd");// 这个就是只有年月日
        times = sf1.format(new Date());

        if ((mSportsApp == null || mSportsApp.getSportUser() == null
                || mSportsApp.getSportUser().equals("") || mSportsApp
                .getSportUser().getUid() == 0) && mSportsApp.LoginOption) {

        } else {
            initView();
            cachedThreadExecutor = Executors.newCachedThreadPool();
            SportsApp.getInstance().setmHandler(mHandler);
        }

//        if (mClient == null) {
//            mClient = new LocationClient(getActivity());
//        }
//        registerLocation();

        getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        if (mSportsApp.getSports_Show() <= 0) {
            getSportsShow sportsShow = new getSportsShow();
            sportsShow.execute("");
        } else {
            count = mSportsApp.getSports_Show();
        }
        initm();

    }

    public void initm() {
        if (mLocationClient == null) {
            //初始化定位
            mLocationClient = new AMapLocationClient(getActivity());
            //设置定位回调监听
            mLocationClient.setLocationListener(this);
            //初始化AMapLocationClientOption对象
            mLocationOption = new AMapLocationClientOption();            // 设置为高精度定位模式
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            //给定位客户端对象设置定位参数
            mLocationClient.setLocationOption(mLocationOption);
            //获取一次定位结果：
            mLocationOption.setOnceLocation(true);
            //获取最近3s内精度最高的一次定位结果：
            //设置setOnceLocationLatest(boolean b)接口为true，启动定位时SDK会返回最近3s内精度最高的一次定位结果。如果设置其为true，
            // setOnceLocation(boolean b)接口也会被设置为true，反之不会，默认为false。
            mLocationOption.setOnceLocationLatest(true);
            //启动定位
            mLocationClient.startLocation();
        }
    }

    @Override
    public void onPageResume() {
        // TODO Auto-generated method stub
        MobclickAgent.onPageStart("MyFirstSportFragment");
        if (mSportsApp == null) {
            mSportsApp = (SportsApp) getActivity().getApplication();
        }
        if (mSportsApp.getSportUser() != null) {
            mUid = mSportsApp.getSportUser().getUid();
            sharedPreferences = getActivity().getSharedPreferences(
                    "sports" + mUid, 0);
        }
        allSportsData();
        count = mSportsApp.getSports_Show();
        mSportsApp.setSports_Show(0);
        updateTip();
        String tempStep = CalcStepCounter();
        setTempNum(tempStep);

        preTime = FunctionStatic.onResume();
        if (mAnimation != null) {
            if (!mAnimation.isRunning()) {
                mAnimation.start();
            }
        }

        SharedPreferences stop_data = context.getSharedPreferences("bushutongji.xml", Context.MODE_PRIVATE);
        SharedPreferences.Editor stop_editor = stop_data.edit();
        stop_editor.putBoolean("stop_service", false);
        stop_editor.commit();

        Intent intent = new Intent("com.fox.exercise.newversion.bushutongji.RESTART_SERVICE");
        context.sendBroadcast(intent);
        SharedPreferences getDate = context.getSharedPreferences("sports"
                + mSportsApp.getSportUser().getUid(), 0);
        if (getDate.getInt("select_goalType", 0)==1) {
            setSportsgoal_gongli();
        }
    }

    @Override
    public void onPagePause() {
        // TODO Auto-generated method stub
        MobclickAgent.onPageEnd("MyFirstSportFragment");
        FunctionStatic.onPause(getActivity(), FunctionStatic.FUNCTION_MYSPORTS,
                preTime);

        list_one = null;
    }

    @Override
    public void onPageDestroy() {
        // TODO Auto-generated method stub
        if (histDB != null) {
            histDB.close();
            histDB = null;
        }
        if (locationManager != null) {
            locationManager.removeUpdates(this);
        }
        mSportsApp.removeActivity(getActivity());
//        if (mClient != null) {
//            unregisterLocation();
//            mClient = null;
//        }
        type = 0;
        if (mAnimation != null) {
            if (mAnimation.isRunning()) {
                mAnimation.stop();
            }
            mAnimation = null;
        }

        if (msgReceiver != null) {
            context.unregisterReceiver(msgReceiver);
        }
        if (mLocationClient != null) {
			mLocationClient.stopLocation();
            mLocationClient.onDestroy();
        }


    }

//    public static int registerLocation() {
//        int result = 1;
//        if (type == 0) {
//            myListener = new MyLocationListenner();
//            if (mClient == null) {
//                Looper.prepare();
//                mClient = new LocationClient(context);
//                Looper.loop();
//            }
//            if (mClient != null) {
//                mClient.registerLocationListener(myListener);
//                LocationClientOption option = new LocationClientOption();
//                option.setLocationMode(LocationMode.Hight_Accuracy);
//                option.setCoorType("bd0911");
//                option.setScanSpan(5000);
//                mClient.setLocOption(option);
////                mClient.start();
//                type = 1;
//            }
//        }
//        if (mClient != null)
//            result = mClient.requestLocation();
//        return result;
//    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null) {
            if (aMapLocation.getErrorCode() == 0) {
                if (aMapLocation == null) {
                    LocationInfo.latitude = "";
                    LocationInfo.longitude = "";
                } else {
                    LocationInfo.latitude = Double.toString(aMapLocation.getLatitude());
                    LocationInfo.longitude = Double.toString(aMapLocation
                            .getLongitude());
                    if (SportMain.isAutoLogin)
                        SportMain.isAutoLogin = false;
                }
                String city = aMapLocation.getCity();// 城市
                if (!TextUtils.isEmpty(city)) {
                    String cityStr = city.substring(0, city.length() - 1);
                    cityname = cityStr;
                    mSportsApp.curCity = cityStr;
                    if (!cityStr.equals(spf.getString("cityname", ""))) {
                        Editor editor = spf.edit();
                        editor.putString("cityname", cityStr);
                        editor.commit();
                    }
                }
                if (mLocationClient != null) {

                    mLocationClient.stopLocation();
                }
            }
        }
    }

    public class MsgReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            steps = intent.getIntExtra("steps", 0);
            calories = intent.getDoubleExtra("calories", 0);

            String temp = "今日" + getString(R.string.jibu_kongge1) + steps + "步";
            int tempLength = temp.length();
            SpannableString ss = new SpannableString(temp);
            ss.setSpan(new TextAppearanceSpan(context, R.style.jibu_style16), 0, 3,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            ss.setSpan(new TextAppearanceSpan(context, R.style.jibu_style34), 3, tempLength - 1,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            ss.setSpan(new TypefaceSpan("DIN Medium"), 3, tempLength - 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            ss.setSpan(new TextAppearanceSpan(context, R.style.jibu_style16), tempLength - 1,
                    tempLength, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            tv_jinri.setText(ss);

            if (calories < 40) {
                String tempXH = "消耗" + getString(R.string.jibu_kongge2) +
                        (int) calories + getString(R.string.jibu_kongge1) + "Cal";
                int tempLengthXH = tempXH.length();
                SpannableString ssXH = new SpannableString(tempXH);
                ssXH.setSpan(new TextAppearanceSpan(context, R.style.jibu_style16), 0, 4,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                ssXH.setSpan(new TextAppearanceSpan(context, R.style.jibu_style34), 4, tempLengthXH - 4,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                ssXH.setSpan(new TypefaceSpan("DIN Medium"), 4, tempLengthXH - 4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                ssXH.setSpan(new TextAppearanceSpan(context, R.style.jibu_style13), tempLengthXH - 4,
                        tempLengthXH, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                tv_xiaohao.setText(ssXH);
                iv_tubiao.setVisibility(View.GONE);
                iv_beishu.setVisibility(View.GONE);
            } else {
                tv_xiaohao.setText("消耗");

                if ((40 <= calories) && (calories <= 200)) {
                    iv_tubiao.setBackgroundResource(R.drawable.yangrouchuan);
                    iv_tubiao.setVisibility(View.VISIBLE);
                    iv_beishu.setVisibility(View.VISIBLE);
                    iv_beishu.setText(String.format("x%.1f", calories / 40));
                } else if ((201 <= calories) && (calories <= 400)) {
                    iv_tubiao.setBackgroundResource(R.drawable.hanbao);
                    iv_tubiao.setVisibility(View.VISIBLE);
                    iv_beishu.setVisibility(View.VISIBLE);
                    iv_beishu.setText(String.format("x%.1f", calories / 240));
                } else if (calories > 400) {
                    iv_tubiao.setBackgroundResource(R.drawable.jitui);
                    iv_tubiao.setVisibility(View.VISIBLE);
                    iv_beishu.setVisibility(View.VISIBLE);
                    iv_beishu.setText(String.format("x%.1f", calories / 400));
                }

                if (View.VISIBLE == iv_tubiao.getVisibility()) {
                    String tempXH = "消耗" + getString(R.string.jibu_kongge6) + iv_beishu.getText().toString();
                    int tempLengthXH = tempXH.length();
                    SpannableString ssXH = new SpannableString(tempXH);
                    ssXH.setSpan(new TextAppearanceSpan(context, R.style.jibu_style16), 0, 2,
                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    ssXH.setSpan(new TextAppearanceSpan(context, R.style.jibu_style34), 2, 8,
                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    ssXH.setSpan(new TypefaceSpan("DIN Medium"), 2, 8, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    ssXH.setSpan(new TextAppearanceSpan(context, R.style.jibu_style23), 8,
                            tempLengthXH, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    ssXH.setSpan(new TypefaceSpan("DIN Medium"), 8, tempLengthXH, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    tv_xiaohao.setText(ssXH);
                    iv_beishu.setVisibility(View.GONE);
                }
            }
            SharedPreferences getDate = context.getSharedPreferences("sports"
                    + mSportsApp.getSportUser().getUid(), 0);
            if (getDate.getInt("select_goalType", 0)!=1) {
                setSportsgoal_bushu(steps);
            }
//            setSportsgoal_bushu(steps);
        }
    }

    private void initView() {
        rl_bushu = (RelativeLayout) view.findViewById(R.id.rl_bushu);
        rl_bushu.setOnClickListener(this);
        tv_jinri = (TextView) view.findViewById(R.id.tv_jinri);
        tv_jinri.setOnClickListener(this);
        tv_yuedengyu = (ImageView) view.findViewById(R.id.tv_yuedengyu);
        tv_yuedengyu.setOnClickListener(this);
        tv_xiaohao = (TextView) view.findViewById(R.id.tv_xiaohao);
        tv_xiaohao.setOnClickListener(this);
        iv_tubiao = (ImageView) view.findViewById(R.id.iv_tubiao);
        iv_tubiao.setOnClickListener(this);
        iv_beishu = (TextView) view.findViewById(R.id.iv_beishu);
        iv_beishu.setOnClickListener(this);

        msgReceiver = new MsgReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.fox.exercise.newversion.bushutongji.JIBU");
        context.registerReceiver(msgReceiver, intentFilter);

        SharedPreferences jibu_data = context.getSharedPreferences("bushutongji.xml", Context.MODE_PRIVATE);
        Intent jibuIntent = new Intent("com.fox.exercise.newversion.bushutongji.JIBU");
        jibuIntent.putExtra("steps", jibu_data.getInt("steps", 0));
        jibuIntent.putExtra("calories", jibu_data.getFloat("calories", 0));
        context.sendBroadcast(jibuIntent);

        SharedPreferences sharedPreferences2 = getActivity()
                .getSharedPreferences(
                        "sports" + mSportsApp.getSportUser().getUid(), 0);
        mSelectTypeID = sharedPreferences2.getInt("type", 1);
        if (mSelectTypeID == 0) {
            typeId = 0;
        } else if (mSelectTypeID == 1) {
            typeId = 1;
        } else if (mSelectTypeID == 2) {
            typeId = 6;
        } else if (mSelectTypeID == 3) {
            typeId = 3;
        } else if (mSelectTypeID == 4) {
            //表示自驾
            typeId = 14;
        }
        mSportTypeNames = this.getResources().getStringArray(
                R.array.name_newsports_type);

        // 倒计时
        djs_vibrator = (Vibrator) getActivity().getSystemService(
                Context.VIBRATOR_SERVICE);
        djs_layout = (LinearLayout) view.findViewById(R.id.djs_layout);
        djs_image = (ImageView) view.findViewById(R.id.djs_image);
        djs_animation = AnimationUtils.loadAnimation(getActivity(),
                R.anim.animate);
        djs_sp = getActivity().getSharedPreferences("save_djs", 0);
        djs_count = djs_sp.getInt("djs_time", 3);
        djs_zd = djs_sp.getBoolean("djs_remind", true);
        djs_counts = djs_count;

        // 临时的
        RelativeLayout rightRelativeLayout = new RelativeLayout(getActivity());
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                android.widget.RelativeLayout.LayoutParams.WRAP_CONTENT,
                android.widget.RelativeLayout.LayoutParams.WRAP_CONTENT);
        rightRelativeLayout.setLayoutParams(params);
        bind_device_text = new ImageView(getActivity());
        RelativeLayout.LayoutParams lp2 = new RelativeLayout.LayoutParams(
                android.widget.RelativeLayout.LayoutParams.WRAP_CONTENT,
                android.widget.RelativeLayout.LayoutParams.WRAP_CONTENT);
        lp2.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        lp2.setMargins(0, 0, SportsApp.getInstance().dip2px(4), 0);
        bind_device_text.setLayoutParams(lp2);
        bind_device_text.setImageResource(R.drawable.title_right_new_message3);
        rightRelativeLayout.addView(bind_device_text);
        tipNumtx = new TextView(getActivity());
        RelativeLayout.LayoutParams layoutParams1 = new RelativeLayout.LayoutParams(
                SportsApp.getInstance().dip2px(13), SportsApp.getInstance()
                .dip2px(13));
        layoutParams1.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        layoutParams1.setMargins(0, 0, 0, 0);
        tipNumtx.setLayoutParams(layoutParams1);
        tipNumtx.setTextColor(getActivity().getResources().getColor(
                R.color.white));
        tipNumtx.setPadding(0, 0, 0, 0);
        tipNumtx.setBackgroundResource(R.drawable.tip_bg);
        tipNumtx.setTextSize(10);
        tipNumtx.setId(+1);
        tipNumtx.setGravity(Gravity.CENTER);
        rightRelativeLayout.addView(tipNumtx);
        right_btn.setPadding(0, 0, SportsApp.dip2px(12), 0);
        showRightBtn(rightRelativeLayout);
        bind_device_text.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                if (!mSportsApp.LoginOption) {
                    mSportsApp.TyrLoginAction(getActivity(),
                            getString(R.string.sports_love_title),
                            getString(R.string.try_to_login));
                } else if (!mSportsApp.LoginNet) {
                    mSportsApp.NoNetLogin(getActivity());

                } else {
                    startActivityForResult(new Intent(getActivity(),
                            FoxSportsState.class), 10);
                }
            }
        });
        updateTip();

        sports_goal_layout = (RelativeLayout) view
                .findViewById(R.id.sports_goal_layout);

        mubiao_content = (TextView) view.findViewById(R.id.mubiao_content);
        mubiao_content_title = (TextView) view
                .findViewById(R.id.mubiao_content_title);

        user_allsport_licheng = (TextView) view
                .findViewById(R.id.user_allsport_licheng);
        Typeface typeFace = Typeface.createFromAsset(getActivity().getAssets(),
                "DIN Medium.ttf");
        // 应用字体
        user_allsport_licheng.setTypeface(typeFace);
        dis_xiao_1 = (TextView) view.findViewById(R.id.dis_xiao_1);
        dis_xiao_2 = (TextView) view.findViewById(R.id.dis_xiao_2);

        SpannableString styledText = new SpannableString("0.00");
        styledText.setSpan(new TextAppearanceSpan(context,
                        R.style.alldistance_style1), 0, 1,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        styledText.setSpan(new TextAppearanceSpan(context,
                        R.style.alldistance_style2), 1, 4,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        user_allsport_licheng
                .setText(styledText, TextView.BufferType.SPANNABLE);

        dis_xiao_1.setText("00");
        iv_historyallbtn = (ImageView) view.findViewById(R.id.iv_historyallbtn);
        iv_historyallbtn.setOnClickListener(this);
        iv_sportstyle = (ImageView) view.findViewById(R.id.iv_sportstyle);
        iv_sportstyle.setOnClickListener(this);
        if (mSelectTypeID >= 0 && mSelectTypeID <= 3) {
            iv_sportstyle.setImageResource(mFirstpagetype[mSelectTypeID]);
        } else {
            iv_sportstyle.setImageResource(mFirstpagetype[1]);
            mSelectTypeID = 1;
        }
        rl_startsport_first = (RelativeLayout) view
                .findViewById(R.id.rl_startsport_first);
        rl_startsport_first.setOnClickListener(this);

        yundong_cishu = (TextView) view.findViewById(R.id.yundong_cishu);
        yundong_laluli = (TextView) view.findViewById(R.id.yundong_laluli);
        yundong_di_day = (TextView) view.findViewById(R.id.yundong_di_day);
        image_sports_complete_rate = (ImageView) view
                .findViewById(R.id.image_sports_complete_rate);
//        image_sports_complete_point = (ImageView) view
//                .findViewById(R.id.image_sports_complete_point);
        image_sports_complete_layout = (LinearLayout) view
                .findViewById(R.id.image_sports_complete_layout);
        image_sports_complete_content = (TextView) view
                .findViewById(R.id.image_sports_complete_content);
        image_sports_complete_content.setOnClickListener(this);

        mPopMenuBack = (RelativeLayout) view
                .findViewById(R.id.set_menu_background);

        animationSet1 = new AnimationSet(true);
        animationSet2 = new AnimationSet(true);
        translateAnimation1 = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, 1, Animation.RELATIVE_TO_SELF, 0);
        translateAnimation2 = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, -1);
        animationSet1.setDuration(800);
        animationSet2.setDuration(800);
        animationSet1.addAnimation(translateAnimation1);
        animationSet1.setRepeatMode(Animation.REVERSE);
        animationSet2.addAnimation(translateAnimation2);
        animationSet2.setRepeatMode(Animation.REVERSE);

        animationSetDis1 = new AnimationSet(true);
        animationSetDis2 = new AnimationSet(true);
        translateAnimationDis1 = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, 1, Animation.RELATIVE_TO_SELF, 0);
        translateAnimationDis2 = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, -1);
        animationSetDis1.setDuration(800);
        animationSetDis2.setDuration(800);
        animationSetDis1.addAnimation(translateAnimationDis1);
        animationSetDis1.setRepeatMode(Animation.REVERSE);
        animationSetDis2.addAnimation(translateAnimationDis2);
        animationSetDis2.setRepeatMode(Animation.REVERSE);

        animationSetCal1 = new AnimationSet(true);
        animationSetCal2 = new AnimationSet(true);
        translateAnimationCal1 = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, 1, Animation.RELATIVE_TO_SELF, 0);
        translateAnimationCal2 = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, -1);
        animationSetCal1.setDuration(800);
        animationSetCal2.setDuration(800);
        animationSetCal1.addAnimation(translateAnimationCal1);
        animationSetCal1.setRepeatMode(Animation.REVERSE);
        animationSetCal2.addAnimation(translateAnimationCal2);
        animationSetCal2.setRepeatMode(Animation.REVERSE);
        SharedPreferences getDate = context.getSharedPreferences("sports"
                + mSportsApp.getSportUser().getUid(), 0);
        if (getDate.getInt("select_goalType", 0)==1) {
            setSportsgoal_gongli();
        }

        SportSubTaskDB taskDBhelper = SportSubTaskDB.getInstance(getActivity()
                .getApplicationContext());
        list_one = new ArrayList<SportContionTaskDetail>();
        list_visitor = new ArrayList<SportContionTaskDetail>();
        list_one = taskDBhelper.getNewAllTasks(mUid);
        list_visitor = taskDBhelper.getNewAllTasks(0);

        if (list_one.size() > list_visitor.size()) {
            // 表示本地有记录
            // new AddCoinsThread(10, 1, mHandler, -1).start();
        } else {
            // 表示本地没有记录
            if (list_one == null) {
                // new AddCoinsThread(10, 1, mHandler, -1).start();
            } else {
                if (mSportsApp.isOpenNetwork()) {
                    new Thread(new Runnable() {

                        @Override
                        public void run() {
                            // TODO Auto-generated method stub
                            List<SportContionTaskDetail> list = null;
                            // history_see = false;
                            histDB = SportSubTaskDB.getInstance(getActivity());
                            if (mSportsApp != null
                                    && mSportsApp.getSessionId() != null) {
                                try {
                                    list = ApiJsonParser.getSportsTaskAll(
                                            mSportsApp.getSessionId(), mTimes,
                                            mUid);
                                    if (list != null && list.size() > 0) {
                                        for (SportContionTaskDetail sportContionTaskDetail : list) {
                                            if (getActivity() != null) {
                                                if (sportContionTaskDetail != null) {
                                                    saveDate2DB(sportContionTaskDetail);
                                                }
                                            } else {
                                                break;
                                            }
                                        }
                                        Message msg = mHandler.obtainMessage();
                                        msg.what = 20141106;
                                        mHandler.sendMessage(msg);
                                    } else {
                                    }
                                } catch (ApiNetException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                } catch (ApiSessionOutException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }

                            } else {
                            }
                        }
                    }).start();
                }

            }
        }

        // 拉去活动信息在首页上显示
        if (mSportsApp.isOpenNetwork()) {
            new AsyncTask<Void, Void, ExternalActivi>() {

                @Override
                protected ExternalActivi doInBackground(Void... params) {
                    // TODO Auto-generated method stub
                    ExternalActivi externalActivi = null;
                    try {
                        if(isAdded()){
                            externalActivi = ApiJsonParser.getExternalActive(
                                    mSportsApp.getSessionId(),
                                    1,
                                    "z"
                                            + getResources().getString(
                                            R.string.config_game_id));
                        }
                    } catch (ApiNetException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (ApiSessionOutException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    return externalActivi;
                }

                protected void onPostExecute(ExternalActivi result) {
                    if (result != null) {
                        exActivi = result;
                        long time = System.currentTimeMillis();
                        Date startDate = new Date(time);
                        try {
                            if (result.getStart_time() != null
                                    && result.getEnd_time() != null) {
                                if (Tools.isInDate(startDate,
                                        result.getStart_time(),
                                        result.getEnd_time())) {
                                    if (result.getId() == sharedPreferences
                                            .getInt("huoDongId", 0)) {
                                        // 表示是同一个活动弹出框不需要弹出来
                                        if (result.getFrequency() > 0) {
                                            SimpleDateFormat sdfs = new SimpleDateFormat("yyyy-MM-dd");
                                            if (!"".equals(sharedPreferences.getString("huoDongtime", "")) && !sharedPreferences.getString("huoDongtime", "").equals(sdfs.format(new Date()))) {
                                                String starttime1 = "";
                                                String starttime2 = "";

                                                String endtime1 = "";
                                                String endtime2 = "";
                                                starttime1 = result.getStart_time() + " 00:00:00";
                                                endtime1 = result.getEnd_time() + " 00:00:00";

                                                starttime2 = result.getStart_time() + " 23:59:59";
                                                endtime2 = result.getEnd_time() + " 23:59:59";
                                                if (Tools.isTanchu(starttime1, starttime2, endtime1, endtime2, new Date(), result.getFrequency())) {

                                                    starttime1 = null;
                                                    starttime2 = null;
                                                    endtime1 = null;
                                                    endtime2 = null;

                                                    if (exActivi.getHref_id() == 1) {
                                                        // 第三方外部链接
                                                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                                        Editor edit = sharedPreferences
                                                                .edit();
                                                        edit.putInt("huoDongId",
                                                                result.getId());
                                                        edit.putString("huoDongtime",
                                                                sdf.format(new Date()));
                                                        edit.commit();
                                                        sdf = null;
                                                        edit = null;
                                                        shotOutHuodongPop();

                                                    } else if (exActivi.getHref_id() == 2) {
                                                        // 活动详情跳转
                                                        new AsyncTask<Void, Void, ActivityInfo>() {

                                                            @Override
                                                            protected ActivityInfo doInBackground(
                                                                    Void... params) {
                                                                // TODO Auto-generated
                                                                ActivityInfo activityInfos = null;
                                                                try {
                                                                    activityInfos = ApiJsonParser
                                                                            .getActivityDetailInfo(
                                                                                    mSportsApp
                                                                                            .getSessionId(),
                                                                                    exActivi.getWeb_id(),
                                                                                    0);
                                                                } catch (ApiNetException e) {
                                                                    // TODO Auto-generated
                                                                    e.printStackTrace();
                                                                } catch (ApiSessionOutException e) {
                                                                    // TODO Auto-generated
                                                                    e.printStackTrace();
                                                                }
                                                                return activityInfos;
                                                            }

                                                            protected void onPostExecute(
                                                                    ActivityInfo result) {
                                                                if (result != null) {
                                                                    activityDetailInfo = result;
                                                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                                                    Editor edit = sharedPreferences
                                                                            .edit();
                                                                    edit.putInt(
                                                                            "huoDongId",
                                                                            exActivi.getId());
                                                                    edit.putString("huoDongtime",
                                                                            sdf.format(new Date()));
                                                                    edit.commit();
                                                                    sdf = null;
                                                                    edit = null;
                                                                    shotOutHuodongPop();
                                                                }

                                                            }

                                                            ;

                                                        }.execute();

                                                    } else if (exActivi.getHref_id() == 4) {
                                                        // 商城商品详情
                                                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                                        Editor edit = sharedPreferences
                                                                .edit();
                                                        edit.putInt("huoDongId",
                                                                exActivi.getId());
                                                        edit.putString("huoDongtime",
                                                                sdf.format(new Date()));
                                                        edit.commit();
                                                        sdf = null;
                                                        edit = null;
                                                        shotOutHuodongPop();
                                                    } else if (exActivi.getHref_id() == 3) {
                                                        //运动秀详情
                                                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                                        Editor edit = sharedPreferences
                                                                .edit();
                                                        edit.putInt("huoDongId",
                                                                exActivi.getId());
                                                        edit.putString("huoDongtime",
                                                                sdf.format(new Date()));
                                                        edit.commit();
                                                        sdf = null;
                                                        edit = null;
                                                        shotOutHuodongPop();
                                                    } else if (exActivi.getHref_id() == 5) {
                                                        //赛事跳转
                                                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                                        Editor edit = sharedPreferences
                                                                .edit();
                                                        edit.putInt("huoDongId",
                                                                result.getId());
                                                        edit.putString("huoDongtime",
                                                                sdf.format(new Date()));
                                                        edit.commit();
                                                        sdf = null;
                                                        edit = null;
                                                        shotOutHuodongPop();
                                                    }


                                                }

                                            }
                                            sdfs = null;
                                        }
                                    } else {
                                        // 表示不是一个活动 要弹出来
                                        String starttime1 = "";
                                        String starttime2 = "";

                                        String endtime1 = "";
                                        String endtime2 = "";
                                        starttime1 = result.getStart_time() + " 00:00:00";
                                        endtime1 = result.getEnd_time() + " 00:00:00";

                                        starttime2 = result.getStart_time() + " 23:59:59";
                                        endtime2 = result.getEnd_time() + " 23:59:59";
                                        if (exActivi.getFrequency() == 0 || Tools.isTanchu(starttime1, starttime2, endtime1, endtime2, new Date(), result.getFrequency())) {

                                            starttime1 = null;
                                            starttime2 = null;
                                            endtime1 = null;
                                            endtime2 = null;
                                            if (exActivi.getHref_id() == 1) {
                                                // 第三方外部链接
                                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                                Editor edit = sharedPreferences
                                                        .edit();
                                                edit.putInt("huoDongId",
                                                        result.getId());
                                                edit.putString("huoDongtime",
                                                        sdf.format(new Date()));
                                                edit.commit();
                                                sdf = null;
                                                edit = null;
                                                shotOutHuodongPop();

                                            } else if (exActivi.getHref_id() == 2) {
                                                // 活动详情跳转
                                                new AsyncTask<Void, Void, ActivityInfo>() {

                                                    @Override
                                                    protected ActivityInfo doInBackground(
                                                            Void... params) {
                                                        // TODO Auto-generated
                                                        // method stub
                                                        ActivityInfo activityInfos = null;
                                                        try {
                                                            activityInfos = ApiJsonParser
                                                                    .getActivityDetailInfo(
                                                                            mSportsApp
                                                                                    .getSessionId(),
                                                                            exActivi.getWeb_id(),
                                                                            0);
                                                        } catch (ApiNetException e) {
                                                            // TODO Auto-generated
                                                            // catch block
                                                            e.printStackTrace();
                                                        } catch (ApiSessionOutException e) {
                                                            // TODO Auto-generated
                                                            // catch block
                                                            e.printStackTrace();
                                                        }
                                                        return activityInfos;
                                                    }

                                                    protected void onPostExecute(
                                                            ActivityInfo result) {
                                                        if (result != null) {
                                                            activityDetailInfo = result;
                                                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                                            Editor edit = sharedPreferences
                                                                    .edit();
                                                            edit.putInt(
                                                                    "huoDongId",
                                                                    exActivi.getId());
                                                            edit.putString("huoDongtime",
                                                                    sdf.format(new Date()));
                                                            edit.commit();
                                                            sdf = null;
                                                            edit = null;
                                                            shotOutHuodongPop();
                                                        }

                                                    }

                                                    ;

                                                }.execute();

                                            } else if (exActivi.getHref_id() == 4) {
                                                // 商城商品详情
                                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                                Editor edit = sharedPreferences
                                                        .edit();
                                                edit.putInt("huoDongId",
                                                        exActivi.getId());
                                                edit.putString("huoDongtime",
                                                        sdf.format(new Date()));
                                                edit.commit();
                                                sdf = null;
                                                edit = null;
                                                shotOutHuodongPop();
                                            } else if (exActivi.getHref_id() == 3) {
                                                //运动秀详情
                                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                                Editor edit = sharedPreferences
                                                        .edit();
                                                edit.putInt("huoDongId",
                                                        exActivi.getId());
                                                edit.putString("huoDongtime",
                                                        sdf.format(new Date()));
                                                edit.commit();
                                                sdf = null;
                                                edit = null;
                                                shotOutHuodongPop();
                                            } else if (exActivi.getHref_id() == 5) {
                                                //赛事跳转
                                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                                Editor edit = sharedPreferences
                                                        .edit();
                                                edit.putInt("huoDongId",
                                                        result.getId());
                                                edit.putString("huoDongtime",
                                                        sdf.format(new Date()));
                                                edit.commit();
                                                sdf = null;
                                                edit = null;
                                                shotOutHuodongPop();
                                            }

                                        }


                                    }
                                } else {
                                    // nightrun_layout.setVisibility(View.GONE);
                                }
                            } else {

                            }

                        } catch (Exception e) {
                            // TODO: handle exception
                        }

                    } else {
                        // nightrun_layout.setVisibility(View.GONE);
                    }

                }

                ;

            }.execute();
        } else {
            // nightrun_layout.setVisibility(View.GONE);
        }


        if (list_one != null && list_one.size() > 0) {
            allSportsData();
        }
        //setSportsgoal_bushu();
        String tempStep = CalcStepCounter();
        setTempNum(tempStep);
    }


    // private void waitShowDialog() {
    // // TODO Auto-generated method stub
    // if (mLoadProgressDialog == null) {
    // mLoadProgressDialog = new Dialog(getActivity(),
    // R.style.sports_dialog);
    // LayoutInflater mInflater = getActivity().getLayoutInflater();
    // View v1 = mInflater.inflate(R.layout.bestgirl_progressdialog, null);
    // TextView mDialogMessage = (TextView) v1.findViewById(R.id.message);
    // mDialogMessage.setText(R.string.bestgirl_wait);
    // v1.setMinimumWidth((int) (SportsApp.ScreenWidth * 0.8));
    // mLoadProgressDialog.setContentView(v1);
    // }
    // if (mLoadProgressDialog != null)
    // if (!mLoadProgressDialog.isShowing()
    // && !getActivity().isFinishing())
    // mLoadProgressDialog.show();
    // Log.i(TAG, "isFirstshow----");
    // }

    // 跳转至心率检测界面
    // private void image_heart_rateEvent() {
    // // TODO Auto-generated method stub
    // image_heart_rate.setOnClickListener(new OnClickListener() {
    //
    // @Override
    // public void onClick(View v) {
    // // TODO Auto-generated method stub
    // Intent intent = new Intent(getActivity(), XinlvActivity.class);
    // startActivity(intent);
    // }
    // });
    // }
    // 进入Activity直接弹出对话框
    // private Runnable mRunnable = new Runnable() {
    // public void run() {
    // // 弹出PopupWindow的具体代码
    // if (isFirstload == 0) {
    // showSportWindow();
    // if (myAdapter != null) {
    // myAdapter.notifyDataSetChanged();
    // }
    //
    // isFirstload = 1;
    // SharedPreferences sharedPreferences = getActivity()
    // .getSharedPreferences(
    // "sports" + mSportsApp.getSportUser().getUid(), 0);
    // Editor editor_First = sharedPreferences.edit();
    // editor_First.putInt("isFirstload", isFirstload);
    // editor_First.commit();
    // }
    //
    //
    // }
    // };

    // 计算步数
    private void setTempNum(String tempStep) {
        // today_temp_num.setBackgroundResource(R.drawable.today_step_num);
        // today_temp_num.setBackgroundResource(R.drawable.halfstroke_cicle_yellow_background);
        int tempLength = tempStep.length();
        SpannableString ss = new SpannableString(" 今日步数 ：" + tempStep + " 步 ");
        // ss.setSpan(new ForegroundColorSpan(Color.parseColor("#8B8686")), 0,
        // 6, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        // ss.setSpan(new ForegroundColorSpan(Color.parseColor("#F49000")), 6, 6
        // + tempLength, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        // ss.setSpan(new ForegroundColorSpan(Color.parseColor("#8B8686")), 6 +
        // tempLength, 9 + tempLength, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(new AbsoluteSizeSpan(20, true), 7, 7 + tempLength,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        // today_temp_num.setText(ss);
    }

    @Override
    public void onClick(View view) {
        // TODO Auto-generated method stub
        switch (view.getId()) {
            case R.id.rl_bushu:
            case R.id.tv_jinri:
            case R.id.tv_yuedengyu:
            case R.id.tv_xiaohao:
            case R.id.iv_tubiao:
            case R.id.iv_beishu:
                Intent intentBuShu = new Intent(context, BuShuTongJiBarChart.class);
                intentBuShu.putExtra("steps", steps);
                intentBuShu.putExtra("calories", calories);
                startActivity(intentBuShu);
                break;
            case R.id.iv_historyallbtn:
                Intent kintent = new Intent(getActivity(), HistoryAllActivity.class);
                Bundle kbundle = new Bundle();
                kbundle.putInt("ID", mUid);
                kintent.putExtra("ID", mUid);
                String ss1 = yundong_cishu.getText().toString().trim();
                String ss2 = yundong_laluli.getText().toString().trim();
                String ss3 = yundong_di_day.getText().toString().trim();
                kbundle.putString("yundong_cishu", ss1);
                kbundle.putString("yundong_laluli", ss2);
                kbundle.putString("yundong_di_day", ss3);

                kintent.putExtra("yundong_cishu", ss1);
                kintent.putExtra("yundong_laluli", ss2);
                kintent.putExtra("yundong_di_day", ss3);
                kintent.putExtras(kbundle);
                startActivity(kintent);
                break;
            case R.id.iv_sportstyle:
                showSportWindow();
                if (myAdapter != null) {
                    myAdapter.notifyDataSetChanged();
                }
                break;
            case R.id.rl_startsport_first:
                if (Utils.isFastClick(4000)) {
                    return;
                }
                if (isOPen(getActivity())) {
                    if ((gpsType >= 0)) {
                        saveAndExit();
                    } else {
                        Toast.makeText(getActivity(),
                                getString(R.string.location_gps_weak),
                                Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putString("title", "提示");
                    bundle.putString("message", "您需要开启GPS定位,是否开启？");
                    onCreateDialogs(2, bundle);
                }

                break;

            case R.id.sports_goal_back:
                myWindow.dismiss();
                myView.setVisibility(View.GONE);
                mPopMenuBack.setVisibility(View.GONE);
                break;
            case R.id.sports_goal_ok:
                if (edit_step.getText().toString().trim().equals("")) {
                    if(isAdded()){
                        Toast.makeText(context,
                                getResources().getString(R.string.juli_noshuru),
                                Toast.LENGTH_SHORT).show();
                    }
                } else if (selectgoal == 1 && Integer.parseInt(edit_step.getText().toString()) > 700 ){
                    if(isAdded()){
                        Toast.makeText(context,
                                getResources().getString(R.string.belowto_700km),
                                Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // 运动目标myNumberStep可能是步数也可能是公里数
                    int myNumberStep = Integer.parseInt(edit_step.getText()
                            .toString());
                    SharedPreferences sharedPreferences = context
                            .getSharedPreferences("sports"
                                    + mSportsApp.getSportUser().getUid(), 0);
                    Editor editors = sharedPreferences.edit();
                    editors.putInt("editDistance", myNumberStep);
                    editors.putInt("select_goalType", selectgoal);
                    editors.commit();

                    myWindow.dismiss();
                    myView.setVisibility(View.GONE);
                    mPopMenuBack.setVisibility(View.GONE);
                    // setSportsgoal_bushu(steps);
                    if (selectgoal==1) {
                        setSportsgoal_gongli();
                    }else{
                        setSportsgoal_bushu(steps);
                    }
                }
                break;

            case R.id.image_sports_complete_content:
                shotSportsGoal();
                break;


            case R.id.tanchu_img:
                // 弹出框图片点击跳转
                if (exActivi != null) {
                    if (exActivi.getHref_id() == 1) {
                        tanOutCount(exActivi.getId());//统计点击量
                        // 第三方外部链接
                        if (exActivi.getUrl() != null
                                && !"".equals(exActivi.getUrl())) {
                            Uri uri = Uri.parse(exActivi.getUrl());
                            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                            startActivity(intent);
                            if (tanChuPop != null && tanChuPop.isShowing()) {
                                tanChuPop.dismiss();
                                mPopMenuBack.setVisibility(View.GONE);
                            }
                        } else {
                            if(isAdded()){
                                Toast.makeText(
                                        context,
                                        getResources().getString(
                                                R.string.sports_get_data_fail), Toast.LENGTH_SHORT)
                                        .show();
                            }
                        }

                    } else if (exActivi.getHref_id() == 2) {
                        tanOutCount(exActivi.getId());//统计点击量
                        // 活动详情跳转
                        Intent activityIntent = new Intent(getActivity(),
                                ActivityInfoWebView.class);
                        activityIntent.putExtra("title_name",
                                activityDetailInfo.getTitle());
                        activityIntent.putExtra("action_url",
                                activityDetailInfo.getActivity_URl());
                        activityIntent.putExtra("activity_id",
                                activityDetailInfo.getActionId());
                        activityIntent.putExtra("thrurl",
                                activityDetailInfo.getActivity_URl());
                        SimpleDateFormat formatter1 = new SimpleDateFormat(
                                "yyyy-MM-dd");
                        Date endDate = new Date(activityDetailInfo.getEnd_time() * 1000);
                        String endTime = formatter1.format(endDate);
                        endTime = endTime.replace("-", "");
                        int b = Integer.valueOf(endTime).intValue() + 1;
                        endTime = b + "";
                        String year = endTime.substring(0, 4);
                        String month = endTime.substring(4, 6);
                        String day = endTime.substring(6, 8);
                        endTime = year + "-" + month + "-" + day;
                        activityIntent.putExtra("activitytime", endTime);
                        startActivity(activityIntent);
                        if (tanChuPop != null && tanChuPop.isShowing()) {
                            tanChuPop.dismiss();
                            mPopMenuBack.setVisibility(View.GONE);
                        }

                    } else if (exActivi.getHref_id() == 4) {
                        tanOutCount(exActivi.getId());//统计点击量
                        // 商城商品详情
                        Intent yunIntent = new Intent(getActivity(),
                                YunHuWebViewActivity.class);
                        yunIntent.putExtra("web_id", exActivi.getWeb_id());
                        startActivity(yunIntent);
                        if (tanChuPop != null && tanChuPop.isShowing()) {
                            tanChuPop.dismiss();
                            mPopMenuBack.setVisibility(View.GONE);
                        }

                    } else if (exActivi.getHref_id() == 3) {
                        tanOutCount(exActivi.getId());//统计点击量
                        // 运动秀详情
                        Intent yunIntent = new Intent(getActivity(),
                                UserActivityMainActivity.class);
                        yunIntent.putExtra("findId", exActivi.getWeb_id());
                        startActivity(yunIntent);
                        if (tanChuPop != null && tanChuPop.isShowing()) {
                            tanChuPop.dismiss();
                            mPopMenuBack.setVisibility(View.GONE);
                        }
                    } else if (exActivi.getHref_id() == 5) {
                        tanOutCount(exActivi.getId());//统计点击量
                        // 赛事跳转
                        if (exActivi.getUrl() != null
                                && !"".equals(exActivi.getUrl())) {
                            Intent mIntent = new Intent(getActivity(),
                                    NightRunWebViewActivity.class);
                            mIntent.putExtra("isfrom", 1);//1表示从弹出框跳转到赛事
                            mIntent.putExtra("ExternalActivi", exActivi);
                            startActivity(mIntent);
                            if (tanChuPop != null && tanChuPop.isShowing()) {
                                tanChuPop.dismiss();
                                mPopMenuBack.setVisibility(View.GONE);
                            }
                        } else {
                            if(isAdded()){
                                Toast.makeText(
                                        context,
                                        getResources().getString(
                                                R.string.sports_get_data_fail), Toast.LENGTH_SHORT)
                                        .show();
                            }
                        }

                    }
                }
                break;
            case R.id.close_tanchu_btn:
                if (tanChuPop != null && tanChuPop.isShowing()) {
                    tanChuPop.dismiss();
                    mPopMenuBack.setVisibility(View.GONE);
                }
                break;
        }
    }

    protected Dialog onCreateDialogs(int id, Bundle args) {
        switch (id) {
            case 2:
                String title = args.getString("title");
                String message = args.getString("message");
                AlertDialog.Builder builder = new Builder(context);
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

    public boolean isOPen(Context context) {
        LocationManager locationManager = (LocationManager) context
                .getSystemService(Context.LOCATION_SERVICE);
        // 通过GPS卫星定位，定位级别可以精确到街（通过24颗卫星定位，在室外和空旷的地方定位准确、速度快）
        boolean gps = locationManager
                .isProviderEnabled(LocationManager.GPS_PROVIDER);
        // 通过WLAN或移动网络(3G/2G)确定的位置（也称作AGPS，辅助GPS定位。主要用于在室内或遮盖物（建筑群或茂密的深林等）密集的地方定位）
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
        if (isOPen(context) == true) {
            String provider = Settings.Secure.getString(getActivity()
                            .getContentResolver(),
                    Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            if (!provider.contains("gps")) { // if gps is disabled
                final Intent poke = new Intent();
                poke.setClassName("com.android.settings",
                        "com.android.settings.widget.SettingsAppWidgetProvider");
                poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
                poke.setData(Uri.parse("3"));
                getActivity().sendBroadcast(poke);
            }
        } else {
        }
    }

    private Handler mHandler = new Handler() {
        SharedPreferences spf;

        @Override
        public void handleMessage(Message msg) {
            if (getActivity() == null)
                return;
            try {
                spf = getActivity().getSharedPreferences("sports", 0);
            } catch (Exception e) {
                e.printStackTrace();
            }
            switch (msg.what) {

                case 20141106:
                    allSportsData();
                    String tempStep = CalcStepCounter();
                    setTempNum(tempStep);
                    break;

                case LAST_SPORTS:
                    SharedPreferences getDate = context.getSharedPreferences("sports"
                            + mSportsApp.getSportUser().getUid(), 0);
                    if (getDate.getInt("select_goalType", 0)==1) {
                        setSportsgoal_gongli();
                    }
                    break;
                case UPDATE_HEADER:
                    Intent data = (Intent) msg.obj;
                    Bundle bundle = data.getBundleExtra("useredit");
                    boolean isfacechanged = bundle.getBoolean("isfacechanged");
                    String username = bundle.getString("username");
                    if (isfacechanged) {
                    }
                    break;
                case UPDATE_INVITE:
                    int invite = msg.arg1;
                    break;
                case UPDATE_TRYTOLOGIN:
                    initView();
                    break;
                case UPDATE_ANIMATION:
                    startTextAnimation(ANIMATION_TOTAL_DIS);
                    break;
                case STOP_ANIMATION:
                    dis_xiao_1.setText("");
                    break;
                case UPDATE_ANIMATION_TODAY:
                    break;
                case STOP_ANIMATION_TODAY:
                    break;
                case REFRESHPHOTO:
                    boolean isfacechange = (Boolean) msg.obj;
                    if (isfacechange) {
                        ImageWorkManager mImageWorkerMan_Icon = new ImageWorkManager(
                                getActivity(), 0, 0);
                        ImageResizer mImageWorker_Icon = mImageWorkerMan_Icon
                                .getImageWorker();
                        if (!SportsApp.getInstance().LoginOption) {
                            userPhoto
                                    .setImageResource(R.drawable.sports_user_edit_portrait_male);
                        } else {
                            if (SportsApp.getInstance().getSportUser().getSex()
                                    .equals("man")) {
                                userPhoto
                                        .setImageResource(R.drawable.sports_user_edit_portrait_male);
                            } else {
                                userPhoto
                                        .setImageResource(R.drawable.sports_user_edit_portrait);
                            }

                            mImageWorker_Icon.loadImage(SportsApp.getInstance()
                                            .getSportUser().getUimg(), userPhoto, null,
                                    null, false);
                        }
                    }
                    break;
                case STEPS_MSG:
                    int mStepValue = (int) msg.arg1;
                    setTempNum(mStepValue + "");
                    break;
                case ApiConstant.COINS_SUCCESS:
                    Intent cIntent = new Intent(getActivity(),
                            ShowCoinsDialog.class);
                    cIntent.putExtra("message", getString(R.string.login_coins));
                    startActivity(cIntent);
                    break;
                case REFRESH_USER:
                    // 刷新用户信息
                    getUserInfo();
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    };

    private void startTextAnimation(int animIndex) {
        switch (animIndex) {
            case ANIMATION_TOTAL_DIS:
                if (countdown > 0 && history_see) {
                    int d1 = juli_float - countdown;
                    countdown--;
                    int d2 = juli_float - countdown;
                    dis_xiao_1.setText(String.format("%02d", d1));
                    dis_xiao_2.setText(String.format("%02d", d2));
                    dis_xiao_1.startAnimation(animationSet2);
                    dis_xiao_2.startAnimation(animationSet1);
                    if (countdown == 0)
                        mHandler.sendEmptyMessageDelayed(STOP_ANIMATION, 800);
                    mHandler.sendEmptyMessageDelayed(UPDATE_ANIMATION, 800);
                }
                break;
        }

    }


    @Override
    public boolean onTouch(View arg0, MotionEvent arg1) {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        view.setOnTouchListener(this);
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void OnViewChange(int view) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onDismiss() {
        // TODO Auto-generated method stub
        if (myWindow != null) {
            if (mPopMenuBack != null) {
                mPopMenuBack.setVisibility(View.GONE);
            }
        }
        if (tanChuPop != null) {
            if (mPopMenuBack != null) {
                mPopMenuBack.setVisibility(View.GONE);
            }
        }

    }

    private void saveDate2DB(SportContionTaskDetail detail) {

        Cursor cursor = histDB.newquery(mUid, detail.getStartTime(),
                detail.getSportTime(), detail.getSportDistance() + "");
        try {
            if (!cursor.moveToFirst()) {
                ContentValues values = new ContentValues();
                values.put(SportSubTaskDB.UID, detail.getUserId());
                values.put(SportSubTaskDB.SPORT_TYPE, detail.getSports_type());
                values.put(SportSubTaskDB.SPORT_SWIM_TYPE, detail.getSwimType());
                values.put(SportSubTaskDB.SPORT_DEVICE,
                        detail.getMonitoringEquipment());
                values.put(SportSubTaskDB.SPORT_START_TIME,
                        detail.getStartTime());
                values.put(SportSubTaskDB.SPORT_TIME, detail.getSportTime());
                values.put(SportSubTaskDB.SPORT_DISTANCE,
                        detail.getSportDistance());
                values.put(SportSubTaskDB.SPORT_SPEED,
                        detail.getSportVelocity());
                values.put(SportSubTaskDB.SPORT_CALORIES,
                        detail.getSprots_Calorie());
                values.put(SportSubTaskDB.SPORT_HEART_RATE,
                        detail.getHeartRate());
                values.put(SportSubTaskDB.SPORT_LAT_LNG, detail.getLatlng());
                values.put(SportSubTaskDB.SPORT_ISUPLOAD, 1);
                values.put(SportSubTaskDB.SPORT_DATE, detail.getSportDate());
                values.put(SportSubTaskDB.SPORT_TASKID, detail.getTaskid());
                values.put(SportSubTaskDB.SPORT_STEP, detail.getStepNum());
                values.put(SportSubTaskDB.SPORT_MAPTYPE, detail.getMapType());
                values.put(SportSubTaskDB.SPORT_MARKCODE,
                        detail.getSport_markcode());
                values.put(SportSubTaskDB.SPORT_SPEEDLIST,
                        detail.getSport_speedList());
                values.put(SportSubTaskDB.SPORT_MARKLIST,
                        detail.getCoordinate_list());
                histDB.insert(values, false);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                try {
                    cursor.close();
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
        }
    }

    /*
     * 运动总路程，运动总次数，平均速度，运动消耗热量
	 */
    private double sports_juli = 0.00;
    private int sports_cishu = 0;
    private double sports_sudu = 0.00;
    private int sports_reliang = 0;
    private int juli_float = 0;
    int countdown = 0;

    private void allSportsData() {
        sports_juli = 0.00;
        sports_cishu = 0;
        sports_sudu = 0.00;
        sports_reliang = 0;
        if (mSportsApp != null && mSportsApp.getSportUser() != null) {

            if (mSportsApp.getSportUser().getSportsdata() != null
                    && !"".equals(mSportsApp.getSportUser().getSportsdata())) {
                double s1 = 0.0;// 未上传的总距离
                double s2 = 0.0;// 未上传的总卡路里
                ArrayList<SportTask> unUpload = getUnUpload();
                for (int i = 0; i < unUpload.size(); i++) {
                    s1 += unUpload.get(i).getSport_distance();
                    s2 += unUpload.get(i).getSport_calories();
                }
                s1 = Double.parseDouble(mSportsApp.getSportUser()
                        .getSportsdata()) + s1;
                s2 = mSportsApp.getSportUser().getSprots_Calorie() + s2;
                String dis_str = SportTaskUtil.getDoubleNumber(s1);

                Editor edit = hSpf.edit();
                edit.putString("dis_str", dis_str);
                edit.commit();

                SpannableString styledText = new SpannableString(dis_str);
                int a = dis_str.indexOf(".");
                if (dis_str.length() >= 8) {
                    styledText.setSpan(new TextAppearanceSpan(context,
                                    R.style.alldistance_style3), 0, a,
                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    styledText.setSpan(new TextAppearanceSpan(context,
                                    R.style.alldistance_style4), a, dis_str.length(),
                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                } else {
                    styledText.setSpan(new TextAppearanceSpan(context,
                                    R.style.alldistance_style1), 0, a,
                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    styledText.setSpan(new TextAppearanceSpan(context,
                                    R.style.alldistance_style2), a, dis_str.length(),
                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }

                user_allsport_licheng.setText(styledText,
                        TextView.BufferType.SPANNABLE);

                int s3 = mSportsApp.getSportUser().getCount_num()
                        + unUpload.size();
                yundong_cishu.setText(s3 + "");
                DecimalFormat df = new DecimalFormat("0");
                String numStr = df.format(s2);
                yundong_laluli.setText(numStr);

                yundong_di_day
                        .setText(mSportsApp.getSportUser().getTime() + "");

            } else {
                SpannableString styledText = new SpannableString("0.00");
                styledText.setSpan(new TextAppearanceSpan(context,
                                R.style.alldistance_style1), 0, 1,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                styledText.setSpan(new TextAppearanceSpan(context,
                                R.style.alldistance_style2), 1, 4,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                user_allsport_licheng.setText(styledText,
                        TextView.BufferType.SPANNABLE);
                yundong_laluli.setText("0");
                yundong_cishu.setText("0");
                yundong_di_day.setText("0");
            }

        } else {
            if (hSpf.contains("dis_str")) {
                String dis_str1 = hSpf.getString("dis_str", "0.00");
                SpannableString styledText1 = new SpannableString(dis_str1);
                int a = dis_str1.indexOf(".");
                if (dis_str1.length() >= 8) {
                    styledText1.setSpan(new TextAppearanceSpan(context,
                                    R.style.alldistance_style3), 0, a,
                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    styledText1.setSpan(new TextAppearanceSpan(context,
                                    R.style.alldistance_style4), a, dis_str1.length(),
                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                } else {
                    styledText1.setSpan(new TextAppearanceSpan(context,
                                    R.style.alldistance_style1), 0, a,
                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    styledText1.setSpan(new TextAppearanceSpan(context,
                                    R.style.alldistance_style2), a, dis_str1.length(),
                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
                user_allsport_licheng.setText(styledText1,
                        TextView.BufferType.SPANNABLE);
            } else {
                SpannableString styledText = new SpannableString("0.00");
                styledText.setSpan(new TextAppearanceSpan(context,
                                R.style.alldistance_style1), 0, 1,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                styledText.setSpan(new TextAppearanceSpan(context,
                                R.style.alldistance_style2), 1, 4,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                user_allsport_licheng.setText(styledText,
                        TextView.BufferType.SPANNABLE);
            }


            yundong_laluli.setText("0");
            yundong_cishu.setText("0");
            yundong_di_day.setText("0");
        }

        if (!mSportsApp.isOpenNetwork()) {
            if (hSpf.contains("dis_str")) {
                String dis_str1 = hSpf.getString("dis_str", "0.00");
                SpannableString styledText1 = new SpannableString(dis_str1);
                int a = dis_str1.indexOf(".");
                if (dis_str1.length() >= 8) {
                    styledText1.setSpan(new TextAppearanceSpan(context,
                                    R.style.alldistance_style3), 0, a,
                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    styledText1.setSpan(new TextAppearanceSpan(context,
                                    R.style.alldistance_style4), a, dis_str1.length(),
                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                } else {
                    styledText1.setSpan(new TextAppearanceSpan(context,
                                    R.style.alldistance_style1), 0, a,
                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    styledText1.setSpan(new TextAppearanceSpan(context,
                                    R.style.alldistance_style2), a, dis_str1.length(),
                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
                user_allsport_licheng.setText(styledText1,
                        TextView.BufferType.SPANNABLE);
            }
        }

    }

    // 获取到登录者没有上传的运动记录
    private ArrayList<SportTask> getUnUpload() {
        ArrayList<SportTask> unloadDatas = new ArrayList<SportTask>();
        if (mSportsApp != null && mSportsApp.getSportUser() != null) {
            SportSubTaskDB taskDBhelper = SportSubTaskDB
                    .getInstance(getActivity());
            ArrayList<SportTask> unUploadDatas = taskDBhelper
                    .getTasksByUnOrIngUpload(mSportsApp.getSportUser().getUid());
            if (unUploadDatas != null && unUploadDatas.size() > 0) {
                for (int j = 0; j < unUploadDatas.size(); j++) {
                    if (SportTaskUtil.getNormalRange(unUploadDatas.get(j)
                            .getSport_type_task(), unUploadDatas.get(j)
                            .getSport_speed(), unUploadDatas.get(j)
                            .getSport_time())) {
                        unloadDatas.add(unUploadDatas.get(j));
                    }
                }

            }
        }
        return unloadDatas;
    }


//    public static void unregisterLocation() {
//        if (type == 1 && mClient != null) {
//            mClient.unRegisterLocationListener(myListener);
//            mClient.stop();
//            type = 0;
//        }
//    }

    /**
     * 定位SDK监听函数
     */
//    public static class MyLocationListenner implements BDLocationListener {
//
//        @Override
//        public void onReceiveLocation(BDLocation location) {
//
//            if (location == null) {
//                LocationInfo.latitude = "";
//                LocationInfo.longitude = "";
//            } else {
//                LocationInfo.latitude = Double.toString(location.getLatitude());
//                LocationInfo.longitude = Double.toString(location
//                        .getLongitude());
//                if (SportMain.isAutoLogin)
//                    SportMain.isAutoLogin = false;
//            }
//
//        }
//    }

    // 从数据库查询到今日运动公里数再得到今日步数
    private String CalcStepCounter() {
        double sports_juli = 0;
        int sports_cishu = 0;

        List<SportContionTaskDetail> list = null;
        SportSubTaskDB taskDBhelper = SportSubTaskDB.getInstance(getActivity()
                .getApplicationContext());

        list = taskDBhelper.getTasksByDate(mUid, getTodayDate());
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

        sports_cishu = (int) (sports_juli * 10000 / 7);
        return String.valueOf(sports_cishu);
    }


    public void updateTip() {
        detail = SportsApp.getInstance().getSportUser();
        setMsgbox(detail.getMsgCounts().getSportVisitor()
                + detail.getMsgCounts().getFans()
                + detail.getMsgCounts().getSysmsgsports()
                + detail.getMsgCounts().getPrimsg() + count);

    }


    private void setMsgbox(int msgNum) {
        if (tipNumtx != null) {
            if (msgNum > 0) {
                tipNumtx.setVisibility(View.VISIBLE);
                tipNumtx.setText(msgNum > 99 ? "99+" : msgNum + "");
            } else {
                tipNumtx.setVisibility(View.GONE);
            }
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10) {
            updateTip();
            count = 0;
        }
    }

    public void setSportsgoal_bushu(int bushu) {
//        LinearLayout.LayoutParams layoutParams2 = (android.widget.LinearLayout.LayoutParams) image_sports_complete_point
//                .getLayoutParams();
        LinearLayout.LayoutParams layoutParams3 = (android.widget.LinearLayout.LayoutParams) image_sports_complete_content
                .getLayoutParams();
        SharedPreferences foxSportSetting = getActivity().getSharedPreferences(
                "sports" + mSportsApp.getSportUser().getUid(), 0);
        int goals = foxSportSetting.getInt("editDistance", 0);
        if (goals == 0) {
            mubiao_content.setVisibility(View.GONE);
            mubiao_content_title.setVisibility(View.GONE);
            image_sports_complete_layout.setVisibility(View.VISIBLE);
//            layoutParams2.setMargins(3, 0, 0, 2);//左上右下四个参数
            layoutParams3.setMargins(3, 0, 0, 2);
            String temp = "运动目标未设置";
            int tempLength = temp.length();
            SpannableString ss = new SpannableString(temp);
            ss.setSpan(new ForegroundColorSpan(Color.parseColor("#333333")), 0,
                    4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            ss.setSpan(new ForegroundColorSpan(Color.parseColor("#D91600")), 4,
                    tempLength, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            image_sports_complete_content.setText(ss);
//            image_sports_complete_point.setLayoutParams(layoutParams2);
            image_sports_complete_content.setLayoutParams(layoutParams3);
            LayoutParams rateparams = image_sports_complete_rate
                    .getLayoutParams();
            rateparams.width = 0;
            image_sports_complete_rate.setLayoutParams(rateparams);

        } else {
            sports_goal_layout.setVisibility(View.VISIBLE);
            if (bushu != 0) {
                if (bushu >= goals) {
                    image_sports_complete_layout
                            .setVisibility(View.VISIBLE);
                    mubiao_content.setVisibility(View.GONE);
//                    layoutParams2.setMargins(getActivity()
//                            .getWindowManager().getDefaultDisplay()
//                            .getWidth() - 15, 0, 0, 2);
                    TextPaint textPaint = image_sports_complete_content
                            .getPaint();
                    float textPaintWidth = textPaint.measureText("已完成今日目标"
                            + goals + "步");
                    layoutParams3.setMargins((int) (getActivity()
                            .getWindowManager().getDefaultDisplay()
                            .getWidth()
                            - textPaintWidth - 6), 0, 0, 2);
                    image_sports_complete_content.setText("已完成今日目标" + goals
                            + "步");
//                    image_sports_complete_point
//                            .setLayoutParams(layoutParams2);
                    image_sports_complete_content
                            .setLayoutParams(layoutParams3);

                    LayoutParams rateparams = image_sports_complete_rate
                            .getLayoutParams();
                    rateparams.width = getActivity().getWindowManager()
                            .getDefaultDisplay().getWidth();
                    image_sports_complete_rate.setLayoutParams(rateparams);
                } else {
                    String temp = "目标完成" + bushu + "步/" + goals + "步";
                    int tempLength = temp.length();
                    SpannableString ss = new SpannableString(temp);
                    ss.setSpan(
                            new ForegroundColorSpan(Color
                                    .parseColor("#333333")), 0, 4,
                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    ss.setSpan(
                            new ForegroundColorSpan(Color
                                    .parseColor("#ff5a00")), 4, tempLength,
                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    image_sports_complete_content.setText(ss);
                    image_sports_complete_layout
                            .setVisibility(View.VISIBLE);
                    mubiao_content_title.setVisibility(View.GONE);
                    mubiao_content.setVisibility(View.GONE);
                    int pwidth = getActivity().getWindowManager()
                            .getDefaultDisplay().getWidth();
                    int hwidth = getActivity().getWindowManager()
                            .getDefaultDisplay().getWidth() / 2;
                    LayoutParams params = image_sports_complete_rate
                            .getLayoutParams();
                    int moveLenth = (int) (getActivity().getWindowManager()
                            .getDefaultDisplay().getWidth()
                            * bushu / goals);
                    params.width = moveLenth;
                    image_sports_complete_rate.setLayoutParams(params);

                    TextPaint textPaint = image_sports_complete_content
                            .getPaint();
                    float textPaintWidth = textPaint.measureText(temp);

//                    layoutParams2.setMargins(moveLenth, 0, 0, 2);
                    if (moveLenth > hwidth) {
                        if (pwidth - moveLenth > textPaintWidth) {
                            layoutParams3.setMargins(
                                    (int) (moveLenth - textPaintWidth / 2),
                                    0, 0, 0);
                        } else {
                            layoutParams3.setMargins((int) (pwidth
                                    - textPaintWidth - 10), 0, 0, 2);
                        }

                    } else {
                        if (textPaintWidth < moveLenth) {
                            layoutParams3.setMargins(
                                    (int) (moveLenth - textPaintWidth / 2),
                                    0, 0, 0);
                        } else {
                            layoutParams3.setMargins(10, 0, 0, 2);
                        }
                    }
//                    image_sports_complete_point
//                            .setLayoutParams(layoutParams2);
                    image_sports_complete_content
                            .setLayoutParams(layoutParams3);

                }
            } else {
                image_sports_complete_layout.setVisibility(View.VISIBLE);
                mubiao_content.setVisibility(View.GONE);
//                layoutParams2.setMargins(3, 0, 0, 2);
                layoutParams3.setMargins(3, 0, 0, 2);
                String temp = "目标完成0步/" + goals + "步";
                int tempLength = temp.length();
                SpannableString ss = new SpannableString(temp);
                ss.setSpan(
                        new ForegroundColorSpan(Color.parseColor("#333333")),
                        0, 4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                ss.setSpan(
                        new ForegroundColorSpan(Color.parseColor("#ff5a00")),
                        4, tempLength, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                image_sports_complete_content.setText(ss);
//                image_sports_complete_point.setLayoutParams(layoutParams2);
                image_sports_complete_content
                        .setLayoutParams(layoutParams3);
                LayoutParams rateparams = image_sports_complete_rate
                        .getLayoutParams();
                rateparams.width = 0;
                image_sports_complete_rate.setLayoutParams(rateparams);
            }


        }
    }

    public void setSportsgoal_gongli() {
        LinearLayout.LayoutParams layoutParams3 = (android.widget.LinearLayout.LayoutParams) image_sports_complete_content
                .getLayoutParams();
        SharedPreferences foxSportSetting = getActivity().getSharedPreferences(
                "sports" + mSportsApp.getSportUser().getUid(), 0);
        int goals = foxSportSetting.getInt("editDistance", 0);
        if (goals == 0) {
            mubiao_content.setVisibility(View.GONE);
            mubiao_content_title.setVisibility(View.GONE);
            image_sports_complete_layout.setVisibility(View.VISIBLE);
            layoutParams3.setMargins(3, 0, 0, 2);
            String temp = "运动目标未设置";
            int tempLength = temp.length();
            SpannableString ss = new SpannableString(temp);
            ss.setSpan(new ForegroundColorSpan(Color.parseColor("#333333")), 0,
                    4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            ss.setSpan(new ForegroundColorSpan(Color.parseColor("#D91600")), 4,
                    tempLength, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            image_sports_complete_content.setText(ss);
            image_sports_complete_content.setLayoutParams(layoutParams3);
            LayoutParams rateparams = image_sports_complete_rate
                    .getLayoutParams();
            rateparams.width = 0;
            image_sports_complete_rate.setLayoutParams(rateparams);
        } else {
            sports_goal_layout.setVisibility(View.VISIBLE);
            SportContionTaskDetail st = mdb.getToadyHistorySports(mUid,
                    getTodayDate());
            if (st != null) {
                todayDis = st.getSportDistance();
                if (todayDis != 0.00) {
                    String dis_str = SportTaskUtil.getDoubleNumber(todayDis);
                    if ((int)todayDis >= goals) {
                        image_sports_complete_layout
                                .setVisibility(View.VISIBLE);
                        mubiao_content.setVisibility(View.GONE);
                        // mubiao_content.setText("已完成今日目标" + goals + "公里");
                        TextPaint textPaint = image_sports_complete_content
                                .getPaint();
                        float textPaintWidth = textPaint.measureText("已完成今日目标"
                                + goals + "公里");
                        layoutParams3.setMargins((int) (getActivity()
                                .getWindowManager().getDefaultDisplay()
                                .getWidth()
                                - textPaintWidth - 6), 0, 0, 2);
                        image_sports_complete_content.setText("已完成今日目标" + goals
                                + "公里");
                        image_sports_complete_content
                                .setLayoutParams(layoutParams3);

                        LayoutParams rateparams = image_sports_complete_rate
                                .getLayoutParams();
                        rateparams.width = getActivity().getWindowManager()
                                .getDefaultDisplay().getWidth();
                        image_sports_complete_rate.setLayoutParams(rateparams);
                    } else {
                        String temp = "目标完成" + dis_str + "公里/" + goals + "公里";
                        int tempLength = temp.length();
                        SpannableString ss = new SpannableString(temp);
                        ss.setSpan(
                                new ForegroundColorSpan(Color
                                        .parseColor("#333333")), 0, 4,
                                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        ss.setSpan(
                                new ForegroundColorSpan(Color
                                        .parseColor("#ff5a00")), 4, tempLength,
                                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        image_sports_complete_content.setText(ss);
                        image_sports_complete_layout
                                .setVisibility(View.VISIBLE);
                        mubiao_content_title.setVisibility(View.GONE);
                        mubiao_content.setVisibility(View.GONE);
                        int pwidth = getActivity().getWindowManager()
                                .getDefaultDisplay().getWidth();
                        int hwidth = getActivity().getWindowManager()
                                .getDefaultDisplay().getWidth() / 2;
                        LayoutParams params = image_sports_complete_rate
                                .getLayoutParams();
                        int moveLenth = (int) (getActivity().getWindowManager()
                                .getDefaultDisplay().getWidth()
                                * todayDis / goals);
                        params.width = moveLenth;
                        image_sports_complete_rate.setLayoutParams(params);

                        TextPaint textPaint = image_sports_complete_content
                                .getPaint();
                        float textPaintWidth = textPaint.measureText(temp);

                        if (moveLenth > hwidth) {
                            if (pwidth - moveLenth > textPaintWidth) {
                                layoutParams3.setMargins(
                                        (int) (moveLenth - textPaintWidth / 2),
                                        0, 0, 0);
                            } else {
                                layoutParams3.setMargins((int) (pwidth
                                        - textPaintWidth - 10), 0, 0, 2);
                            }

                        } else {
                            if (textPaintWidth < moveLenth) {
                                layoutParams3.setMargins(
                                        (int) (moveLenth - textPaintWidth / 2),
                                        0, 0, 0);
                            } else {
                                layoutParams3.setMargins(10, 0, 0, 2);
                            }
                        }
                        image_sports_complete_content
                                .setLayoutParams(layoutParams3);

                    }
                } else {
                    image_sports_complete_layout.setVisibility(View.VISIBLE);
                    mubiao_content.setVisibility(View.GONE);
                    layoutParams3.setMargins(3, 0, 0, 2);
                    String temp = "目标完成0公里/" + goals + "公里";
                    int tempLength = temp.length();
                    SpannableString ss = new SpannableString(temp);
                    ss.setSpan(
                            new ForegroundColorSpan(Color.parseColor("#333333")),
                            0, 4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    ss.setSpan(
                            new ForegroundColorSpan(Color.parseColor("#ff5a00")),
                            4, tempLength, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    image_sports_complete_content.setText(ss);
                    image_sports_complete_content
                            .setLayoutParams(layoutParams3);
                    LayoutParams rateparams = image_sports_complete_rate
                            .getLayoutParams();
                    rateparams.width = 0;
                    image_sports_complete_rate.setLayoutParams(rateparams);
                }

            } else {
                image_sports_complete_layout.setVisibility(View.VISIBLE);
                mubiao_content.setVisibility(View.GONE);
                // mubiao_content.setText("0公里/" + goals + "公里");
                // sports_goal_progressbar.setProgress(0);
                layoutParams3.setMargins(3, 0, 0, 2);
                String temp = "目标完成0公里/" + goals + "公里";
                int tempLength = temp.length();
                SpannableString ss = new SpannableString(temp);
                ss.setSpan(
                        new ForegroundColorSpan(Color.parseColor("#333333")),
                        0, 4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                ss.setSpan(
                        new ForegroundColorSpan(Color.parseColor("#ff5a00")),
                        4, tempLength, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                image_sports_complete_content.setText(ss);
                image_sports_complete_content.setLayoutParams(layoutParams3);
                LayoutParams rateparams = image_sports_complete_rate
                        .getLayoutParams();
                rateparams.width = 0;
                image_sports_complete_rate.setLayoutParams(rateparams);
            }

        }
    }


    private String getTodayDate() {
        String todayDate = null;
        Long todayTime = System.currentTimeMillis();
        Date date = new Date(todayTime);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        todayDate = sdf.format(date);
        return todayDate;
    }

    // 设置运动目标、弹出框
    public void shotSportsGoal() {
        if (myWindow != null && myWindow.isShowing())
            return;
        LayoutInflater inflater = LayoutInflater.from(context);
        myView = (RelativeLayout) inflater.inflate(
                R.layout.sports_goal_newlayout, null);
        tv_bushuorgongli = (TextView) myView.findViewById(R.id.tv_bushuorgongli);
        bushu = (RadioButton) myView.findViewById(R.id.bushu);
        gongli = (RadioButton) myView.findViewById(R.id.gongli);
        radioGroup_goal = (RadioGroup) myView.findViewById(R.id.radioGroup_goal);
        edit_step = (EditText) myView.findViewById(R.id.edit_step);
        final SharedPreferences getDate = context.getSharedPreferences("sports"
                + mSportsApp.getSportUser().getUid(), 0);
        edit_step.setText(String.valueOf(getDate.getInt("editDistance", 0)));
        if ("0".equals(edit_step.getText().toString())) {
            edit_step.setText("0");
            edit_step.setSelection(1);
        } else {
            edit_step.setSelection(String.valueOf(getDate.getInt("editDistance", 0)).length());
        }
        if (getDate.getInt("select_goalType",0) == 1){
            selectgoal = 1;
        }else{
            selectgoal = 0;
        }

        if (getDate.getInt("select_goalType", 0) == 1){
            radioGroup_goal.check(gongli.getId());
            if(isAdded()){
                tv_bushuorgongli.setText(getResources().getString(R.string.km));
            }
        }else{
            radioGroup_goal.check(bushu.getId());
            if(isAdded()){
                tv_bushuorgongli.setText(getResources().getString(R.string.steps));
            }
        }
        radioGroup_goal.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == bushu.getId()){
                    if(isAdded()){
                        tv_bushuorgongli.setText(getResources().getString(R.string.steps));
                    }
                    if (getDate.getInt("select_goalType",0) == 0 && getDate.getInt("editDistance", 0) != 0 ){
                        edit_step.setText(String.valueOf(getDate.getInt("editDistance", 0)));
                        edit_step.setSelection(String.valueOf(getDate.getInt("editDistance", 0)).length());
                    }else{
                        edit_step.setText("0");
                        edit_step.setSelection(1);

                    }
                    selectgoal = 0;
                }else if(i == gongli.getId()){
                    if(isAdded()){
                        tv_bushuorgongli.setText(getResources().getString(R.string.km));
                    }
                    if (getDate.getInt("select_goalType",0) == 1 && getDate.getInt("editDistance", 0) != 0){
                        edit_step.setText(String.valueOf(getDate.getInt("editDistance", 0)));
                        edit_step.setSelection(String.valueOf(getDate.getInt("editDistance", 0)).length());
                    }else{
                        edit_step.setText("0");
                        edit_step.setSelection(1);
                    }
                    selectgoal = 1;
                }
            }
        });
        myView.findViewById(R.id.sports_goal_back).setOnClickListener(this);
        myView.findViewById(R.id.sports_goal_ok).setOnClickListener(this);
        WindowManager wm = (WindowManager) getActivity().getSystemService(
                Context.WINDOW_SERVICE);

        int width = wm.getDefaultDisplay().getWidth();
        myWindow = new PopupWindow(myView, width - SportsApp.dip2px(40),
                RelativeLayout.LayoutParams.WRAP_CONTENT, true);
        myWindow.setAnimationStyle(R.style.AnimationPopup);
        myWindow.setOutsideTouchable(true);
        myWindow.setBackgroundDrawable(new BitmapDrawable());
        myWindow.showAtLocation(bind_device_text, Gravity.CENTER, 0, 0);
        myWindow.setOnDismissListener(this);
        mPopMenuBack.setVisibility(View.VISIBLE);
    }

    // 弹出框
    public void shotOutHuodongPop() {
        // 到时候要统计点击事件
        if (tanChuPop != null && tanChuPop.isShowing())
            return;
        WindowManager wm = (WindowManager) getActivity().getSystemService(
                Context.WINDOW_SERVICE);

        int width = wm.getDefaultDisplay().getWidth();

        LayoutInflater inflater = LayoutInflater.from(context);
        RelativeLayout tanChuView = (RelativeLayout) inflater.inflate(
                R.layout.index_tanchu_layout, null);
        ImageView tanchu_img = (ImageView) tanChuView.findViewById(R.id.tanchu_img);
        RelativeLayout.LayoutParams params = (android.widget.RelativeLayout.LayoutParams) tanchu_img.getLayoutParams();
        params.height = width * 32 / 29;
        tanchu_img.setLayoutParams(params);
        tanchu_img.setMaxHeight(width * 32 / 29); //这里其实可以根据需求而定，我这里测试为最大宽度的5倍
        tanchu_img.setMinimumHeight(width * 32 / 29);
        ImageDownloader mDownloader = new ImageDownloader(getActivity());
        mDownloader.setType(ImageDownloader.OnlyOne);
        if (exActivi != null) {
            mDownloader.download(exActivi.getThumb(), tanchu_img, null);
        }
        tanChuView.findViewById(R.id.close_tanchu_btn).setOnClickListener(this);
        tanchu_img.setOnClickListener(this);

        tanChuPop = new PopupWindow(tanChuView, width - SportsApp.dip2px(35),
                RelativeLayout.LayoutParams.WRAP_CONTENT, true);
        tanChuPop.setAnimationStyle(R.style.AnimationPopup);
        tanChuPop.setFocusable(true);
        tanChuPop.setTouchable(true);
        tanChuPop.showAtLocation(bind_device_text, Gravity.CENTER, 0, 0);
        tanChuPop.setOnDismissListener(this);
        mPopMenuBack.setVisibility(View.VISIBLE);
    }

    private void getUserInfo() {
        if (mSportsApp != null && mSportsApp.getSessionId() != null
                && !"".equals(mSportsApp.getSessionId()) && mSportsApp.isOpenNetwork()) {
            new AsyncTask<Void, Void, UserDetail>() {

                @Override
                protected UserDetail doInBackground(Void... arg0) {
                    // TODO Auto-generated method stub
                    UserDetail detail = null;
                    try {
                        detail = ApiJsonParser.refreshRank(mSportsApp
                                .getSessionId());
                    } catch (ApiNetException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (ApiSessionOutException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    return detail;
                }

                protected void onPostExecute(UserDetail result) {
                    if (result != null) {
                        mSportsApp.setSportUser(result);
                    }

                }

                ;

            }.execute();
        }

    }

    // --------------------------------------------------------------------------------
    private void showSportWindow() {
        WindowManager.LayoutParams lp = getActivity().getWindow()
                .getAttributes();
        lp.alpha = 0.3f;
        getActivity().getWindow().setAttributes(lp);
        if (mSportWindow == null) {
            LayoutInflater layoutInflater = (LayoutInflater) getActivity()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view1 = layoutInflater.inflate(R.layout.pop_sport_list, null);
            GridView mlist = (GridView) view1.findViewById(R.id.pop_listview);
            WindowManager wm = (WindowManager) getActivity().getSystemService(
                    Context.WINDOW_SERVICE);

            int width = wm.getDefaultDisplay().getWidth();
            mSportWindow = new PopupWindow(view1, width - 100,
                    LayoutParams.WRAP_CONTENT, true);

            ColorDrawable cd = new ColorDrawable(0x000000);
            mSportWindow.setBackgroundDrawable(cd);
            mSportWindow.setTouchable(true);
            mSportWindow.setOutsideTouchable(true);
            mSportWindow.update();
            mSportWindow.setTouchInterceptor(new OnTouchListener() {

                @Override
                public boolean onTouch(View v, MotionEvent event) {

                    return false;
                    // 这里如果返回true的话，touch事件将被拦截
                    // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
                }
            });
            mSportWindow.setOnDismissListener(new OnDismissListener() {

                @Override
                public void onDismiss() {
                    WindowManager.LayoutParams lp = getActivity().getWindow()
                            .getAttributes();
                    lp.alpha = 1f;
                    getActivity().getWindow().setAttributes(lp);
                }
            });

            myAdapter = new MyAdapter(getActivity());
            mlist.setAdapter(myAdapter);
            mlist.setOnItemClickListener(new MyItemClick());
            mlist.setOnKeyListener(new View.OnKeyListener() {

                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    // TODO Auto-generated method stub
                    mSportWindow.dismiss();
                    return true;
                }
            });
        }
        mSportWindow.setFocusable(true);
        mSportWindow.getContentView().setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                mSportWindow.setFocusable(false);
                mSportWindow.dismiss();
                return true;
            }

        });
        mSportWindow.setBackgroundDrawable(new BitmapDrawable());
        mSportWindow.showAtLocation(right_btn, Gravity.CENTER, 0, 0);
    }

    class MyAdapter extends BaseAdapter {
        private Context myContext;

        public MyAdapter(Context context) {
            super();
            myContext = context;
        }

        @Override
        public int getCount() {
            return mSportTypeNames.length;
        }

        @Override
        public Object getItem(int arg0) {
            return arg0;
        }

        @Override
        public long getItemId(int arg0) {
            return arg0;
        }

        @Override
        public View getView(int arg0, View arg1, ViewGroup arg2) {
            LayoutInflater myInflater = LayoutInflater.from(myContext);
            View myView = myInflater.inflate(R.layout.pop_sport_listitem, null);

            ImageView mitem_image = (ImageView) myView
                    .findViewById(R.id.sport_type_icon);
            TextView mitem_text = (TextView) myView
                    .findViewById(R.id.sport_type_txt);
            if (arg0 == mSelectTypeID) {
                mitem_text.setText(mSportTypeNames[arg0]);
                mitem_text.setTextColor(+R.color.yellow);
                mitem_image.setBackgroundResource(mSporTypeImage_yellow[arg0]);
            } else {
                mitem_text.setText(mSportTypeNames[arg0]);
                if(isAdded()){
                    mitem_text.setTextColor(getResources().getColor(R.color.black));
                }
                mitem_image.setBackgroundResource(mSporTypeImage[arg0]);
            }
            return myView;
        }
    }

    private class MyItemClick implements OnItemClickListener {

        @SuppressLint("ResourceAsColor")
        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                long arg3) {
            // TODO Auto-generated method stub
            iv_sportstyle.setImageResource(mFirstpagetype[arg2]);
            // 运动类型的设定
            if (mSportWindow != null) {
                mSportWindow.dismiss();
                mSportWindow.setFocusable(false);
            }
            switch (arg2) {
                case 0:
                    type = 0;
                    typeId = 0;
                    typeDetailId = 0;
                    ischangeSportType = true;
                    break;
                case 1:
                    type = 1;
                    typeId = 1;
                    typeDetailId = 0;
                    ischangeSportType = true;
                    break;
                case 2:
                    type = 2;
                    typeId = 6;
                    typeDetailId = 0;
                    ischangeSportType = true;
                    break;
                case 3:
                    type = 3;
                    typeId = 3;
                    typeDetailId = 0;
                    ischangeSportType = true;
                    break;
                case 4:
                    //表示自驾
                    type = 4;
                    typeId = 14;
                    typeDetailId = 0;
                    ischangeSportType = true;
                    break;
                case 5:
                    type = 5;
                    typeId = 8;
                    typeDetailId = 0;
                    ischangeSportType = true;
                    break;
                case 6:
                    type = 6;
                    typeId = 4;
                    typeDetailId = 0;
                    ischangeSportType = true;
                    break;
            }
            mSelectTypeID = type;
        }

    }

    private void goStart() {
        if (getActivity() != null){
            djs_zd = djs_sp.getBoolean("djs_remind", true);
            if (djs_zd) {
                long[] pattern = {100, 1000};
                djs_vibrator.vibrate(pattern, 1);
                try {
                        vp = new VoicePrompt(getActivity(), "female", "start", null);
                        vp.playVoice();
                } catch (Exception e) {
                    // TODO: handle exception
                }

                mHandlerDjs.sendEmptyMessageDelayed(4, 1000);
            }
            SharedPreferences sharedPreferences = getActivity()
                    .getSharedPreferences(
                            "sports" + mSportsApp.getSportUser().getUid(), 0);
            Editor editor = sharedPreferences.edit();
            //是否改变运动类型
            if (ischangeSportType) {
                editor.putInt("type", type);
            }
            editor.commit();

            SharedPreferences sp = getActivity().getSharedPreferences(
                    "sport_state_" + mSportsApp.getSportUser().getUid(), 0);
            Editor ed = sp.edit();
            ed.putInt("typeId", typeId);
            ed.putInt("typeDetailId", typeDetailId);
            ed.commit();
            Intent intent = null;
            if (mSportsApp.mCurMapType == SportsApp.MAP_TYPE_GAODE) {
                intent = new Intent(getActivity(), SportingMapActivityGaode.class);
            } else {
                // intent = new Intent(getActivity(), SportingMapActivity.class);
            }

            SharedPreferences stop_data = context.getSharedPreferences("bushutongji.xml", Context.MODE_PRIVATE);
            SharedPreferences.Editor stop_editor = stop_data.edit();
            stop_editor.putBoolean("stop_service", true);
            stop_editor.commit();

            Intent stop_intent = new Intent("com.fox.exercise.newversion.bushutongji.STOP_SERVICE");
            context.sendBroadcast(stop_intent);

            getActivity().startActivity(intent);
        }
    }

    private void onCreateDialog() {
        if (alertDialog != null && alertDialog.isShowing())
            return;
        String message = getResources().getString(R.string.fox_swim_protect);
        alertDialog = new Dialog(getActivity(), R.style.sports_dialog);
        alertDialog.setCanceledOnTouchOutside(false);
        LayoutInflater mInflater1 = getActivity().getLayoutInflater();
        View v = mInflater1.inflate(R.layout.sport_dialog_for_task, null);
        ((TextView) v.findViewById(R.id.message)).setText(message);
        ((Button) v.findViewById(R.id.bt_ok)).setText(getResources().getString(
                R.string.button_ok)
                + "");
        v.findViewById(R.id.bt_giveup).setVisibility(View.GONE);
        v.findViewById(R.id.bt_ok).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        if (alertDialog.isShowing())
                            alertDialog.dismiss();
                        djs_count = djs_sp.getInt("djs_time", 3);
                        if (djs_count > 0) {
                            djs_counts = djs_count;
                            new Thread(new Runnable() {

                                @Override
                                public void run() {
                                    mHandlerDjs.sendEmptyMessage(3);
                                }
                            }).start();
                        } else {
                            goStart();
                        }
                    }
                });
        v.findViewById(R.id.bt_cancel).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        if (alertDialog.isShowing())
                            alertDialog.dismiss();
                    }
                });
        alertDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {

            @Override
            public boolean onKey(DialogInterface dialog, int keyCode,
                                 KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK
                        && event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (alertDialog.isShowing())
                        alertDialog.dismiss();
                }
                return false;
            }

        });
        v.setMinimumWidth((int) (SportsApp.ScreenWidth * 0.8));
        alertDialog.setCancelable(true);
        alertDialog.setContentView(v);
        alertDialog.show();
    }


    public int gpsType = 0;


    /*
     * 倒计时
	 */
    private Handler mHandlerDjs = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    djs_image.setBackgroundResource(myDjsImage[djs_counts]);
                    djs_image.startAnimation(djs_animation);
                    if (djs_counts > 0) {
                        djs_counts--;
                        mHandlerDjs.sendEmptyMessageDelayed(1, 1000);
                    } else {
                        mHandlerDjs.sendEmptyMessageDelayed(2, 1000);
                    }
                    break;
                case 2:
                    if (djs_layout.getVisibility() == View.VISIBLE) {
                        djs_layout.setVisibility(View.GONE);
                    }
                    if (djs_image.getVisibility() == View.VISIBLE) {
                        djs_image.setVisibility(View.GONE);
                    }
                    goStart();
                    break;
                case 3:
                    if (djs_layout.getVisibility() != View.VISIBLE
                            && djs_image.getVisibility() != View.VISIBLE) {
                        djs_layout.setVisibility(View.VISIBLE);
                        djs_image.setVisibility(View.VISIBLE);
                        djs_image.setBackgroundResource(myDjsImage[djs_counts]);
                    }
                    djs_counts--;
                    mHandlerDjs.sendEmptyMessageDelayed(1, 1000);
                    djs_image.setAnimation(djs_animation);
                    break;
                case 4:
                    if (djs_vibrator.hasVibrator()) {
                        djs_vibrator.cancel();
                    }
                    break;
            }
        }
    };

    private void saveAndExit() {
        if (mSelectTypeID != -1) {
            if (djs_sp != null) {
                djs_count = djs_sp.getInt("djs_time", 3);
                if (djs_count > 0) {
                    djs_counts = djs_count;
                    new Thread(new Runnable() {

                        @Override
                        public void run() {
                            mHandlerDjs.sendEmptyMessage(3);
                        }
                    }).start();
                } else {
                    goStart();
                }
            }

        } else {
            onCreateDialog();
        }

    }

    @Override
    public void onLocationChanged(Location location) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub

    }


    // 弹出框点击的次数
    private void tanOutCount(final int activity_id) {
        new AsyncTask<Void, Void, ApiBack>() {
            @Override
            protected ApiBack doInBackground(Void... params) {
                // TODO Auto-generated method stub
                ApiBack back = null;
                try {
                    Date startDate = new Date(System.currentTimeMillis());
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String startTime = formatter.format(startDate);
                    back = ApiJsonParser.healthdatacount(mSportsApp.getSessionId(),
                            11, startTime, activity_id);
                    startTime = null;
                    formatter = null;
                    startDate = null;
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
                if (result != null) {

                } else {
                }

            }
        }.execute();
    }

    /**
     * 获取点赞和评论的消息条数
     */
    public class getSportsShow extends AsyncTask<String, Integer, Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {
            commentInfo = ApiJsonParser.getNewCommentCount(mSportsApp
                    .getSessionId(), mSportsApp.getSportUser().getUid());
            if (commentInfo == null)
                return false;
            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if (result) {
                if (commentInfo.commentCount > 0) {
                    count = commentInfo.commentCount;
                    mSportsApp.setSports_Show(0);
                }
            } else {

            }
        }
    }


    class clickListener implements OnClickListener {

        @Override
        public void onClick(View v) {
            if(!mSportsApp.LoginOption){
                mSportsApp.TyrLoginAction(getActivity(),
                        getString(R.string.sports_love_title),
                        getString(R.string.try_to_login));
            }else{
                // 金币商城
                if (mSportsApp.LoginNet) {
                    if (mSportsApp.isOpenNetwork()) {
                        startActivity(new Intent(getActivity(), YunHuWebViewActivity.class));
                    } else {
                        if(isAdded()){
                            Toast.makeText(
                                    getActivity(),
                                    getResources().getString(
                                            R.string.error_cannot_access_net),
                                    Toast.LENGTH_SHORT);
                        }
                    }
                } else {
                    mSportsApp.NoNetLogin(getActivity());
                }
            }
        }
    }


}
