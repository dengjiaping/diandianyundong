package com.fox.exercise.map;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.Dialog;
import android.app.FragmentTransaction;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ZoomControls;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapLoadedCallback;
import com.baidu.mapapi.map.BaiduMap.OnMapStatusChangeListener;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.Polyline;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.map.UiSettings;
import com.baidu.mapapi.map.offline.MKOfflineMap;
import com.baidu.mapapi.map.offline.MKOfflineMapListener;
import com.baidu.mapapi.model.LatLng;
import com.fox.exercise.R;
import com.fox.exercise.SmartBarUtils;
import com.fox.exercise.SportsType;
import com.fox.exercise.SportsUtilities;
import com.fox.exercise.UploadActivity;
import com.fox.exercise.api.ApiBack;
import com.fox.exercise.bitmap.util.ImageResizer;
import com.fox.exercise.db.SportSubTaskDB;
import com.fox.exercise.http.FunctionStatic;
import com.fox.exercise.lockscreen.SliderRelativeLayout;
import com.fox.exercise.login.LocationInfo;
import com.fox.exercise.newversion.FirstStepService;
import com.fox.exercise.pedometer.FootballUtils;
import com.fox.exercise.pedometer.GolfingUtils;
import com.fox.exercise.pedometer.ImageWorkManager;
import com.fox.exercise.pedometer.MountainUtils;
import com.fox.exercise.pedometer.PedometerSettings;
import com.fox.exercise.pedometer.RidingUtils;
import com.fox.exercise.pedometer.RowingUtils;
import com.fox.exercise.pedometer.RunningUtils;
import com.fox.exercise.pedometer.SwimmingUtils;
import com.fox.exercise.pedometer.TimingManager;
import com.fox.exercise.pedometer.WalkingRaceUtils;
import com.fox.exercise.pedometer.WalkingUtils;
import com.fox.exercise.util.RoundedImage;
import com.fox.exercise.util.SportTaskUtil;
import com.fox.exercise.util.SportTrajectoryUtil;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import cn.ingenic.indroidsync.SportsApp;

public class SportingMapActivity extends Activity implements
        SensorEventListener, OnClickListener, ActionBar.TabListener {

    private Activity mContext;
    private static final String TAG = "SportingMapActivity";

    private MKOfflineMap mOffline;
    private MapView mMapView = null;
    private BaiduMap aMap;
    private UiSettings mUiSettings;
    public static final int CLICK_SPORT_IMG = 0;
    public static final int LOCKSCREEN_DELAY = 10 * 1000;

    private final String SERVICE_NAME = "com.fox.exercise.pedometer.STEPSERVICE";

    private LatLng locData = null;
    private LatLng curLocData = null;
    private LatLng curMediaLoc = null;

    private static final int DRAWTRAJECTORY = 1;
    private static final int SPEEDLIMITNUM = 5;
    public double mLat = 0;
    public double mLng = 0;
    private List<LatLng> mGeoPoints = new ArrayList<LatLng>();
    private List<LatLng> mDrawPoints = new ArrayList<LatLng>();
    private boolean lastPointInValid = false;

    private TextView chron, time_txt;
    private LinearLayout startStopBut;
    private ImageButton backBtn, cameraBtn, videoBtn, voiceBtn;
    private Button startBtn, pauseBtn, stopBtn;
    private Timer timer;
    private TimerTask task;
    private boolean isPause = false;
    private boolean isPauseForClick = false;// 按键暂停
    private boolean isPauseForGPS = false;// GPS信号丢失暂停
    private boolean isRun = false;

    private RelativeLayout unfoldView;
    private ImageButton unfoldBtn;// , resumeBtn;
    private boolean isGone = false;
    private TextView disValue, conValue, speedValue, paceValue, altitudeValue,
            pressureValue;
    private ImageView imageGps, imageview_jibu;
    private String tempAltitude;
    private String startTime;
    private long startTimeSeconds;

    private int limitId, uid;
    private int typeId, deviceId;
    private int typeDetailId = 0;
    private double pace;// 配速

    private int isUpload = 0;

    private double dis, speed;// , heart;
    private double heart = 0.0;
    private double lastSpeed = 0.0;
    private double newSpeed = 0.0;
    private int maxSpeedNum = 0;
    private boolean speedOverLimit = false;
    private int con, stepNum;

    private static final int UPLOAD_PARAM_ERROR = 10001;
    private static final int UPLOAD_START = 10002;
    private static final int UPLOAD_UPDATE = 10003;
    private static final int UPLOAD_MEDIA = 10004;
    // private static final int UPLOAD_FAIL = 10005;
    private static final int UPLOAD_DELETE = 10006;
    // private static final int UPLOAD_NETERROR = 10007;
    private static final int UPLOAD_MEDIA_PHOTO = 10008;
    private static final int SAVE_LOCAL = 10009;

    private static final int UPDATE_STEP = 1;
    private static final int UPDATE_LOCKSCREEN = 2;
    private static final int GO_PAUSE = 3;

    public static int PHOTOFromCAMERA = 0x01;
    public static int VIDEO = 0x02;
    public static int VOICE = 0x03;
    public static int LOCKSCREEN = 0x04;

    private Dialog mLoadDialog = null;
    private Dialog mUploadDialog = null;
    private String mediaFileName;
    private String mediaFilePath;
    private int mediaFileWidth = 0;
    private int mediaFileHight = 0;
    private int mediaFileDuration = 0;
    public int taskID = 0;
    private boolean isMedia = false;

    public int mediaTypeID;
    private Editor ed;
    //	private AddCoinsHandler addCoinsHandler = null;
    private SportSubTaskDB db;

    private MarkerOptions mMarkerOpStart;
    private MarkerOptions mMarkerOpMiddle;

    private Marker mMarkerStart = null;
    private Marker mMarkerMiddle = null;

    private SharedPreferences sp;
    private SharedPreferences sharedPreferences;
    private int isBack;
    private Boolean isStart = false;
    private int sportype = 1;
    private SportsApp mSportsApp;

    // 定义LocationManager对象
    private LocationManager locationManager = null;
    private GpsLocationListener gpsLocationListener = null;

    private Dialog alertDialog;

    private long preTime = 0;

    // 锁屏
    private SliderRelativeLayout sliderLayout = null;
    public static int MSG_LOCK_SUCESS = 1;
    private SharedPreferences foxSportSetting,voiceSportSetting;
    private RelativeLayout lockscreen;
    private Map<String, String> sporttype;
    private WakeLock mWakeLock;

    private Boolean isFirstSave = true;
    private Boolean isEndSave = false;

    private PolylineOptions options;
    private Polyline mPolyline;

    private LocationClient mLocClient = null;
    public MyLocationListenner myListener = new MyLocationListenner();

    private boolean findMethod;

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
    private TimingManager timingMgr;
    private int coins; // 要上传的金币数
    private boolean activityPause = false;
    private boolean canSave = false;
    // 上传成功和更新成功后返回的
    private ApiBack theApiBack = null;
    private int thereturn;
    private boolean is_start_map = true;
    // 语音提示
    private VoicePrompt vp;
    private Map<String, String> hashMap = new HashMap<String, String>();
    private SensorManager sensorManager;
    private Sensor ss;
    private float accelerometer;
    private Boolean mTroughAppear = false;
    ;
    private int mCountNum;
    private long mPrevStepTime;
    private int lastCountNum = 10;

    private PopupWindow mSportWindow = null;
    private RelativeLayout timeLayout;
    private ImageWorkManager mImageWorkerMan_Icon;
    private ImageResizer mImageWorker_Icon;

    private String disStringValue, conStringValue, speedStringValue,
            paceStringValue;
    private TextView pop_chronometerId;

    private TextView sporting_sport_peiPace, sporting_sport_pingjunSpeed,
            sporting_sportxiaohao, time_disValue, zong_li_txt;
    private Button suodingBtn;

    private ImageButton close_yuyin;// 关闭语音
    private Boolean isVoiceON;// 判断语音是否打开关闭

    private TextView textview_stepnum;
    private boolean mIsRunning;// 程序是否运行的标志位
    private boolean mIsFirst = false;// 程序是否运行的标志位
    private PedometerSettings mPedometerSettings;
    private SharedPreferences mSettings;
    private static final int STEPS_MSG = 122;
    private SharedPreferences msharedPreferences, mState;

    private TextView sport_topTitle;
    private int mStepNum1;

    private String markCode;//运动唯一标识

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        findMethod = SmartBarUtils.findActionBarTabsShowAtBottom();
        if (!findMethod) {
            // 取消ActionBar拆分，换用TabHost
            getWindow().setUiOptions(0);
            getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }

        setContentView(R.layout.sporting_map);

        mContext = this;
        mSportsApp = (SportsApp) getApplication();
        mSportsApp.isStartY = false;
        mSportsApp.addActivity(this);
        mImageWorkerMan_Icon = new ImageWorkManager(this, 0, 0);
        mImageWorker_Icon = mImageWorkerMan_Icon.getImageWorker();

        // 友盟推送
        PushAgent.getInstance(this).onAppStart();

        msharedPreferences = getSharedPreferences("sports"
                + mSportsApp.getSportUser().getUid(), 0);

        mMapView = (MapView) mContext.findViewById(R.id.bmapView);

        if (aMap == null) {
            aMap = mMapView.getMap();

            // 隐藏缩放控件
            for (int i = 0; i < mMapView.getChildCount(); i++) {
                View child = mMapView.getChildAt(i);
                if (child instanceof ZoomControls) {
                    child.setVisibility(View.GONE);
                    break;
                }
            }

            mUiSettings = aMap.getUiSettings();
            mUiSettings.setZoomGesturesEnabled(true);
            mUiSettings.setCompassEnabled(false);// 隐藏指南针
            aMap.setMyLocationEnabled(true);// 是否可触发定位并显示定位层
            aMap.setOnMapLoadedCallback(new OnMapLoadedCallback() {
                @Override
                public void onMapLoaded() {
                    Log.v(TAG, "地图加载成功！");
                    if (mLoadDialog.isShowing()) {
                        mLoadDialog.dismiss();
                    }
                }
            });// 设置amap加载成功事件监听器

            aMap.setOnMarkerClickListener(new OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(final Marker marker) {
                    Log.v(TAG, "onMarkerClick");
                    return false;
                }
            });
            aMap.setOnMapStatusChangeListener(new OnMapStatusChangeListener() {

                @Override
                public void onMapStatusChange(MapStatus arg0) {
                    // TODO Auto-generated method stub
                }

                @Override
                public void onMapStatusChangeFinish(MapStatus arg0) {
                    SendLockScreenMSG();

                }

                @Override
                public void onMapStatusChangeStart(MapStatus arg0) {
                    RemoveLockScreenMSG();
                }
            });
        }
//		addCoinsHandler = new AddCoinsHandler();
        // 定位初始化
        mLocClient = new LocationClient(mContext);
        LocationClientOption option = new LocationClientOption();
        // 优先选择GPS
        option.setOpenGps(true);
        // option.setPriority(LocationClientOption.GpsFirst);
        option.setLocationMode(LocationMode.Hight_Accuracy);
        option.setScanSpan(5000);
        // 设置坐标类型
        option.setCoorType("bd09ll");
        mLocClient.registerLocationListener(myListener);
        mLocClient.setLocOption(option);
        mLocClient.start();

        initGPS();

        options = new PolylineOptions().width(12).color(
                Color.argb(255, 242, 109, 27));

        mMarkerOpStart = new MarkerOptions().icon(BitmapDescriptorFactory
                .fromBitmap(BitmapFactory.decodeResource(getResources(),
                        R.drawable.map_start)));
        mMarkerOpMiddle = new MarkerOptions().icon(BitmapDescriptorFactory
                .fromBitmap(BitmapFactory.decodeResource(getResources(),
                        R.drawable.map_middle)));

        Bundle bundle = new Bundle();
        bundle.putString("message",
                getResources().getString(R.string.sports_wait));
        mLoadDialog = onCreateDialog(1, bundle, 0);
        mLoadDialog.show();

        Bundle bundle1 = new Bundle();
        bundle1.putString(
                "message",
                getResources().getString(
                        R.string.sports_authentication_uploading));
        mUploadDialog = onCreateDialog(1, bundle1, 0);

        initView();
        setView();
        if (findMethod) {
            getActionBar().setDisplayShowHomeEnabled(false);
            getActionBar().setDisplayShowTitleEnabled(false);
            final ActionBar bar = getActionBar();
            bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
            bar.addTab(bar.newTab().setIcon(R.drawable.sk_stop)
                    .setTabListener(this));
            bar.addTab(bar.newTab().setIcon(R.drawable.sk_start_or_stop)
                    .setTabListener(this));

            SmartBarUtils.setActionBarTabsShowAtBottom(bar, true);
            SmartBarUtils.setActionModeHeaderHidden(bar, true);
            SmartBarUtils.setActionBarViewCollapsable(bar, true);
        }
        foxSportSetting = getSharedPreferences("sports" + uid, 0);
        voiceSportSetting = getSharedPreferences("voice_sports", 0);
        isOpen = foxSportSetting.getBoolean("lockScreen", false);

        lockisopen = false;
        Editor editor = foxSportSetting.edit();
        editor.putBoolean("lockisopen", lockisopen);
        editor.commit();

        isVoiceON = voiceSportSetting.getBoolean("voiceon", true);
        if (isVoiceON) {
            close_yuyin.setBackgroundResource(R.drawable.title_no_voice);
        } else {
            close_yuyin.setBackgroundResource(R.drawable.title_voice);
        }

        timingMgr = TimingManager.getInstance(getApplicationContext());
        IntentFilter intentFilter = new IntentFilter();
        intentFilter
                .addAction("com.fox.exercise.pedometer.TimerReceiver.alarmclock");
        registerReceiver(timeReceiver, intentFilter);

        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        mWakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, this
                .getClass().getCanonicalName());

        sensorManager = (SensorManager) this
                .getSystemService(Context.SENSOR_SERVICE);
        ss = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        if (is_start_map) {
            if ((locData != null && gpsType > 0) || (null != ss)) {
                // if (djs_count > 0) {
                // djs_counts = djs_count;
                // new Thread(new Runnable() {
                //
                // @Override
                // public void run() {
                // mHandlerDjs.sendEmptyMessage(3);
                // }
                // }).start();
                // } else {
                // goStart();
                // }
                goStart();
                is_start_map = false;
            } else {
                if (gpsType > 0) {
                    Toast.makeText(mContext,
                            getString(R.string.in_gps_locating),
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(mContext,
                            getString(R.string.location_gps_weak),
                            Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void initView() {
        timeLayout = (RelativeLayout) findViewById(R.id.timeLayout);
        backBtn = (ImageButton) mContext.findViewById(R.id.sport_map_back);
        backBtn.setOnClickListener(this);

        cameraBtn = (ImageButton) mContext.findViewById(R.id.cameraBtn);
        cameraBtn.setOnClickListener(this);
        videoBtn = (ImageButton) mContext.findViewById(R.id.videoBtn);
        videoBtn.setOnClickListener(this);
        voiceBtn = (ImageButton) mContext.findViewById(R.id.voiceBtn);
        voiceBtn.setOnClickListener(this);

        unfoldView = (RelativeLayout) mContext
                .findViewById(R.id.startingLayout);

        unfoldBtn = (ImageButton) mContext.findViewById(R.id.bigBtn);
        unfoldBtn.setOnClickListener(this);
        // disValue = (TextView) mContext.findViewById(R.id.disValue);
        // conValue = (TextView) mContext.findViewById(R.id.conValue);
        // speedValue = (TextView) mContext.findViewById(R.id.speedValue);
        // paceValue = (TextView) mContext.findViewById(R.id.paceValue);

        chron = (TextView) mContext.findViewById(R.id.chronometerId);
        time_txt = (TextView) mContext.findViewById(R.id.time_txt);

        pauseBtn = (Button) mContext.findViewById(R.id.pauseBtn);
        pauseBtn.setOnClickListener(this);
        startBtn = (Button) mContext.findViewById(R.id.startBtn);
        // startBtn.setOnClickListener(this);
        stopBtn = (Button) mContext.findViewById(R.id.stopBtn);
        stopBtn.setOnClickListener(this);

        sliderLayout = (SliderRelativeLayout) findViewById(R.id.slider_layout);
        lockscreen = (RelativeLayout) findViewById(R.id.lockscreen);
        startStopBut = (LinearLayout) findViewById(R.id.start_stop_but);
        if (findMethod) {
            startStopBut.setVisibility(View.GONE);
        }
        imageGps = (ImageView) findViewById(R.id.imageview_gps);
        imageview_jibu = (ImageView) findViewById(R.id.imageview_jibu);
        altitudeValue = (TextView) findViewById(R.id.textview_haiba);
        pressureValue = (TextView) findViewById(R.id.textview_qiya);
        /*
         * 倒计时功能熟悉实例化
		 */
        djs_vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        djs_image = (ImageView) findViewById(R.id.djs_image);
        djs_layout = (LinearLayout) findViewById(R.id.djs_layout);
        djs_animation = AnimationUtils.loadAnimation(this, R.anim.animate);
        djs_sp = getSharedPreferences("save_djs", 0);
        djs_count = djs_sp.getInt("djs_time", 3);
        djs_zd = djs_sp.getBoolean("djs_remind", true);
        djs_counts = djs_count;

        sporting_sport_peiPace = (TextView) findViewById(R.id.sporting_sport_peiPace);
        sporting_sport_pingjunSpeed = (TextView) findViewById(R.id.sporting_sport_pingjunSpeed);
        sporting_sportxiaohao = (TextView) findViewById(R.id.sporting_sportxiaohao);
        suodingBtn = (Button) findViewById(R.id.suodingBtn);
        suodingBtn.setOnClickListener(this);
        time_disValue = (TextView) findViewById(R.id.time_disValue);
        zong_li_txt = (TextView) findViewById(R.id.zong_li_txt);

        close_yuyin = (ImageButton) findViewById(R.id.close_yuyin);
        close_yuyin.setOnClickListener(this);

        textview_stepnum = (TextView) findViewById(R.id.textview_stepnum);
        sport_topTitle = (TextView) findViewById(R.id.sport_topTitle);
    }

    private void setSportstype() {
        int type = sharedPreferences.getInt("type", 1);
        ImageButton sport_map_back = (ImageButton) findViewById(R.id.sport_map_back);
        sport_map_back.setBackgroundResource(SportsUtilities
                .getStateImgIdsTitle(type));
    }

    private void setView() {
        uid = mSportsApp.getSportUser().getUid();
        ((SportsApp) mContext.getApplication()).getSessionId();
        sp = mContext.getSharedPreferences("sport_state_" + uid, 0);
        sharedPreferences = getSharedPreferences("sports" + uid, 0);
        ed = sp.edit();

        String latitude = LocationInfo.latitude;
        String longitude = LocationInfo.longitude;
        if (!TextUtils.isEmpty(longitude) || !TextUtils.isEmpty(longitude)) {
            double lat = Double.valueOf(latitude);
            double log = Double.valueOf(longitude);
            if (lat != 0 || log != 0) {
                try {
                    LatLng point = new LatLng(lat, log);
                    animateTo(point);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        typeId = sp.getInt("typeId", 1);
        String typeStr = null;
        if (typeId == SportsType.TYPE_SWIM) {
            typeDetailId = sp.getInt("typeDetailId", 0);
            typeStr = getString(SportTaskUtil.getDetailTypeName(typeId,
                    typeDetailId));
        } else if (typeId == SportsType.TYPE_WALK
                || typeId == SportsType.TYPE_RUN) {
            typeDetailId = sp.getInt("typeDetailId", 0);
            if (typeDetailId == 0) {
                // 打开GPS
                if (isOPen(mContext)) {
                    toggleGPS(mContext, true);
                }
            }
            typeStr = getString(SportTaskUtil.getDetailTypeName(typeId,
                    typeDetailId));
        } else {
            typeStr = getString(SportTaskUtil.getTypeName(typeId));
            // 打开GPS
            if (isOPen(mContext)) {
                toggleGPS(mContext, true);
            }
        }
        if (typeId == SportsType.TYPE_WALK || typeId == SportsType.TYPE_RUN
                || typeId == SportsType.TYPE_CLIMBING) {
            textview_stepnum.setVisibility(View.VISIBLE);
        } else {
            textview_stepnum.setVisibility(View.INVISIBLE);
        }
        sport_topTitle.setText(typeStr + "运动中");
        deviceId = sp.getInt("deviceId", 0);
        setSportstype();
        sporttype = new HashMap<String, String>();
        sporttype.put("type", typeStr);
        sporttype.put("save", "yes");

        if (SportsType.TYPE_CLIMBING == typeId) {
            altitudeValue.setVisibility(View.VISIBLE);
            pressureValue.setVisibility(View.VISIBLE);
        } else {
            altitudeValue.setVisibility(View.GONE);
            pressureValue.setVisibility(View.GONE);
        }
    }

//	private void startSport() {
//		MobclickAgent.onEventBegin(this, "uploadtask", sporttype.toString());
//		startTimeSeconds = System.currentTimeMillis();
//
//		Date startDate = new Date(startTimeSeconds);
//		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		startTime = formatter.format(startDate);
//
//		markCode = SportsApp.getInstance().getSportUser().getUid()+UUIDGenerator.getUUID();//唯一标示
//
//		task = new TimerTask() {
//			public void run() {
//				if (isStart && !isPause) {
//					Message message = new Message();
//					message.what = UPDATE_STEP;
//					handler.sendMessage(message);
//				}
//			}
//		};
//		timer = new Timer(true);
//		// 延时1000ms后执行，1000ms执行一次
//		timer.schedule(task, 1000, 1000);
//		// 设置每5秒获取一次GPS的定位信息
//		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
//				5000, 1, gpsLocationListener);
//		// }
//	}

    private int recLen = 0;
    private int mTempCount = 0;
    private Boolean isOpen = false;
    private Boolean lockisopen = false;
    private Boolean isStepBegin = false;

    final Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATE_STEP:
                    recLen = (int) (mTempCount + (System.currentTimeMillis() - startTimeSeconds) / 1000L);
                    if (!activityPause)
                        chron.setText(SportTaskUtil.showTimeCount(recLen));
                    time_txt.setText(SportTaskUtil.showTimeCount(recLen));

                    if (recLen % (1 * 60) == 0) {
                        save(false);
                    }

                    if (isStepBegin) {
                        updateStepInformation();
                    }
                    break;
                case UPDATE_LOCKSCREEN:
                    Log.v(TAG, "UPDATE_LOCKSCREEN in");
                    lockisopen = foxSportSetting.getBoolean("lockisopen", false);
                    Log.i("lockopen", "isOpen:" + isOpen + "lockisopen:"
                            + lockisopen);

                    if (!lockisopen) {
                        sliderLayout.setMainHandler(mmHandler);
                        lockscreen.setVisibility(View.VISIBLE);
                        // 测试
                        // unfoldView.setVisibility(View.GONE);
                        lockscreen.setOnTouchListener(new View.OnTouchListener() {

                            @Override
                            public boolean onTouch(View arg0, MotionEvent arg1) {
                                // TODO Auto-generated method stub
                                return true;
                            }
                        });
                        Log.v(TAG, "go to lockscreen");
                        lockisopen = true;
                        Editor editor = foxSportSetting.edit();
                        editor.putBoolean("lockisopen", lockisopen);
                        editor.commit();
                    }
                    break;
                case GO_PAUSE:
                    vp = new VoicePrompt(getApplicationContext(), "female",
                            "loseGPS", null);
                    vp.playVoice();
                    goPause();
                    break;
            }
            super.handleMessage(msg);
        }
    };

//	private void addMediaItem(LatLng point, int typeID) {
//		MarkerOptions marker = null;
//		if (typeID == 1) {
//			marker = new MarkerOptions().icon(BitmapDescriptorFactory
//					.fromBitmap(BitmapFactory.decodeResource(getResources(),
//							R.drawable.ic_launcher)));
//		} else if (typeID == 2) {
//			marker = new MarkerOptions().icon(BitmapDescriptorFactory
//					.fromBitmap(BitmapFactory.decodeResource(getResources(),
//							R.drawable.ic_launcher)));
//		} else if (typeID == 3) {
//			marker = new MarkerOptions().icon(BitmapDescriptorFactory
//					.fromBitmap(BitmapFactory.decodeResource(getResources(),
//							R.drawable.ic_launcher)));
//		}
//		aMap.addOverlay(marker.position(point));
//	}

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cameraBtn:
                if (mSportsApp.LoginOption) {
                    if (mSportsApp.isStartY) {
                        if (mSportsApp.isOpenNetwork()) {
                            if (mGeoPoints.size() < 2) {
                                Toast.makeText(mContext,
                                        getString(R.string.spors_media_no_trail),
                                        Toast.LENGTH_SHORT).show();
                                return;
                            }
                            if (recLen > 0) {
                                mediaTypeID = 1;
                                isMedia = true;
                                ed.putInt("mediaTypeID", mediaTypeID);
                                ed.commit();
                                if (taskID > 0) {
                                    getMediaFile(mediaTypeID);
                                } else {
//								uploadSportTask();
                                    getMediaFile(mediaTypeID);
                                }
                            }
                        } else {
                            Toast.makeText(mContext,
                                    getString(R.string.network_not_avaliable),
                                    Toast.LENGTH_SHORT).show();
                        }
                        Log.i("cameraBtn", "taskID =" + taskID + ",mediaTypeID ="
                                + mediaTypeID);
                    } else {
                        Toast.makeText(mContext,
                                getString(R.string.spors_media_disable),
                                Toast.LENGTH_SHORT).show();
                    }
                } else {
                    mSportsApp.TyrLoginAction(SportingMapActivity.this,
                            getString(R.string.sports_love_title),
                            getString(R.string.try_to_login));
                }

                break;
            case R.id.voiceBtn:
                if (mSportsApp.LoginOption) {
                    if (mSportsApp.isStartY) {
                        if (mSportsApp.isOpenNetwork()) {
                            if (mGeoPoints.size() < 2) {
                                Toast.makeText(mContext,
                                        getString(R.string.spors_media_no_trail),
                                        Toast.LENGTH_SHORT).show();
                                return;
                            }
                            if (recLen > 0) {
                                mediaTypeID = 2;
                                isMedia = true;
                                ed.putInt("mediaTypeID", mediaTypeID);
                                ed.commit();
                                if (taskID > 0) {
                                    getMediaFile(mediaTypeID);
                                } else {
//								uploadSportTask();
                                    getMediaFile(mediaTypeID);
                                }
                            }

                        } else {
                            Toast.makeText(mContext,
                                    getString(R.string.network_not_avaliable),
                                    Toast.LENGTH_SHORT).show();
                        }
                        Log.i("voiceBtn", "taskID =" + taskID + ",mediaTypeID ="
                                + mediaTypeID);
                    } else {
                        Toast.makeText(mContext,
                                getString(R.string.spors_media_disable),
                                Toast.LENGTH_SHORT).show();
                    }
                } else {
                    mSportsApp.TyrLoginAction(SportingMapActivity.this,
                            getString(R.string.sports_love_title),
                            getString(R.string.try_to_login));
                }

                break;
            case R.id.videoBtn:
                if (mSportsApp.LoginOption) {
                    if (mSportsApp.isStartY) {
                        if (mSportsApp.isOpenNetwork()) {
                            if (mGeoPoints.size() < 2) {
                                Toast.makeText(mContext,
                                        getString(R.string.spors_media_no_trail),
                                        Toast.LENGTH_SHORT).show();
                                return;
                            }
                            if (recLen > 0) {
                                mediaTypeID = 3;
                                isMedia = true;
                                ed.putInt("mediaTypeID", mediaTypeID);
                                ed.commit();
                                if (taskID > 0) {
                                    getMediaFile(mediaTypeID);
                                } else {
//								uploadSportTask();
                                    getMediaFile(mediaTypeID);
                                }
                            }
                        } else {
                            Toast.makeText(mContext,
                                    getString(R.string.network_not_avaliable),
                                    Toast.LENGTH_SHORT).show();
                        }
                        Log.i("videoBtn", "taskID =" + taskID + ",mediaTypeID ="
                                + mediaTypeID);
                    } else {
                        Toast.makeText(mContext,
                                getString(R.string.spors_media_disable),
                                Toast.LENGTH_SHORT).show();
                    }
                } else {
                    mSportsApp.TyrLoginAction(SportingMapActivity.this,
                            getString(R.string.sports_love_title),
                            getString(R.string.try_to_login));
                }

                break;
            case R.id.bigBtn:
                if (isGone) {
                    unfoldView.setVisibility(View.VISIBLE);
                    unfoldBtn.setBackgroundResource(R.drawable.start_new_shouqi);
                    isGone = false;
                } else {
                    unfoldView.setVisibility(View.GONE);
                    unfoldBtn.setBackgroundResource(R.drawable.start_new_zhankai);
                    isGone = true;
                }
                // showSportWindow();
                // unfoldBtn.setVisibility(View.GONE);
                break;
            case R.id.pauseBtn:
                if (isPauseForGPS) {
                    Toast.makeText(mContext, getString(R.string.location_gps_weak),
                            Toast.LENGTH_SHORT).show();
                } else {
                    if (isPause) {
                        vp = new VoicePrompt(getApplicationContext(), "female",
                                "continue", null);
                        vp.playVoice();
                        goResume();
                        isPauseForClick = false;
                    } else {
                        vp = new VoicePrompt(getApplicationContext(), "female",
                                "pause", null);
                        vp.playVoice();
                        goPause();
                        isPauseForClick = true;
                    }
                }
                break;
            case R.id.startBtn:
                if (is_start_map) {
                    if ((locData != null && gpsType > 0) || (null != ss)) {
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
                        is_start_map = false;
                    } else {
                        if (gpsType > 0) {
                            Toast.makeText(mContext,
                                    getString(R.string.in_gps_locating),
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(mContext,
                                    getString(R.string.location_gps_weak),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                break;
            case R.id.stopBtn:
                if (canSave) {
                    goStop();
                } else {
                    Toast.makeText(mContext,
                            getString(R.string.spors_media_disable),
                            Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.suodingBtn:
                SendBtnLockScreenMSG();
                break;
            case R.id.close_yuyin:
                if (isVoiceON) {
                    isVoiceON = false;
                    close_yuyin.setBackgroundResource(R.drawable.title_voice);
                    Toast.makeText(
                            mContext,
                            getResources()
                                    .getString(R.string.voice_prompt_shutdown),
                            Toast.LENGTH_SHORT).show();
                } else {
                    isVoiceON = true;
                    close_yuyin.setBackgroundResource(R.drawable.title_no_voice);
                    Toast.makeText(mContext,
                            getResources().getString(R.string.voice_prompt_open),
                            Toast.LENGTH_SHORT).show();
                }
                Editor editor1 = voiceSportSetting.edit();
                editor1.putBoolean("voiceon", isVoiceON);
                editor1.commit();
                break;
        }
    }

    @Override
    public void onPause() {
        activityPause = true;
        if (mOffline != null) {
            mOffline.destroy();
            mOffline = null;
        }
        if (mMapView != null) {
            mMapView.onPause();
        }
        if (!isOpen) {
            if (mWakeLock != null && mWakeLock.isHeld())
                mWakeLock.release();
        }
        RemoveLockScreenMSG();
        if (mUploadDialog != null && mUploadDialog.isShowing()) {
            mUploadDialog.dismiss();
        }
        super.onPause();
        Log.i("onPause", "onPause！");
        MobclickAgent.onPageEnd("SportingMapActivity");
        MobclickAgent.onPause(mContext);
        if (mIsRunning) {
            bindStepService();
        }
    }

    @Override
    public void onResume() {
        activityPause = false;
        if (isStart && !isPause) {
            if (!isOpen) {
                if (mWakeLock != null && !mWakeLock.isHeld()) {
                    mWakeLock.acquire();
                }
            }
        }
        if (mMapView != null) {
            mMapView.onResume();
        }
        mOffline = new MKOfflineMap();
        mOffline.init(new MKOfflineMapListener() {

            @Override
            public void onGetOfflineMapState(int arg0, int arg1) {
                // TODO Auto-generated method stub

            }
        });
        if (isStart && !isPause) {
//			mDrawTrajectoryHandler.removeMessages(DRAWTRAJECTORY);
//			mDrawTrajectoryHandler.sendEmptyMessage(DRAWTRAJECTORY);
        }
        SendLockScreenMSG();

        // 测试步数
        mState = getSharedPreferences(
                "UID" + Integer.toString(mSportsApp.getSportUser().getUid()), 0);
        mSettings = PreferenceManager.getDefaultSharedPreferences(this);
        mPedometerSettings = new PedometerSettings(mSettings);
        // mIsRunning = mSettings.getBoolean("service_running", false);
        mIsRunning = msharedPreferences.getBoolean("mservice_running", false);
        uid = mSportsApp.getSportUser().getUid();
        sp = mContext.getSharedPreferences("sport_state_" + uid, 0);
        typeId = sp.getInt("typeId", 1);
        if (typeId == SportsType.TYPE_WALK || typeId == SportsType.TYPE_RUN
                || typeId == SportsType.TYPE_CLIMBING) {
            textview_stepnum.setVisibility(View.VISIBLE);
            if (!mIsRunning && !mIsFirst) {
                if (!mIsFirst) {
//					mStepNum= mState.getInt("steps", 0);
                    Editor edit = mState.edit();
                    edit.putInt("steps", 0);
                    edit.commit();
                }
                startStepService();
                bindStepService();
                mIsFirst = true;
            } else {
                bindStepService();
            }
        } else {
            textview_stepnum.setVisibility(View.INVISIBLE);
            imageview_jibu.setVisibility(View.GONE);
        }

        MobclickAgent.onPageStart("SportingMapActivity");
        MobclickAgent.onResume(mContext);
        Log.i("SportingMapActivity", "onResume()");
        super.onResume();
    }

    private void bindStepService() {
        Log.i(TAG, "[SERVICE] Bind");
        bindService(new Intent(this, FirstStepService.class), mConnection,
                Context.BIND_AUTO_CREATE + Context.BIND_DEBUG_UNBIND);
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
//			sportHandler.sendMessage(sportHandler.obtainMessage(STEPS_MSG,
//					value, 0));
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

    @Override
    public void onDestroy() {
        if (myListener != null && mLocClient != null) {
            mLocClient.unRegisterLocationListener(myListener);
            Log.i("unregisterLocation", "注销定位监听！");
        }
        if (locationManager != null) {
            locationManager.removeUpdates(gpsLocationListener);
            locationManager.removeGpsStatusListener(listener);
        }
        if (timer != null) {
            timer.cancel();
        }
        if (timingMgr != null) {
            timingMgr.cancleRepeatTimingFiveMinutes();
        }
        if (aMap != null) {
            aMap.clear();
            aMap = null;
        }
        if (mMapView != null) {
            mMapView.onDestroy();
            mMapView = null;
        }
        if (mLocClient != null) {
            mLocClient.stop();
        }
        if (mGeoPoints != null) {
            mGeoPoints.clear();
            mGeoPoints = null;
        }
        if (mDrawPoints != null) {
            mDrawPoints.clear();
            mDrawPoints = null;
        }
        if (sporttype != null) {
            sporttype.clear();
            sporttype = null;
        }
        if (db != null) {
            db.close();
        }
        if (isRun) {
            stopService();
            isRun = false;
            Log.i("stopService", "关闭服务！");
        }
        if (!isOpen) {
            if (mWakeLock != null && mWakeLock.isHeld()) {
                mWakeLock.release();
                mWakeLock = null;
            }
        }
        RemoveLockScreenMSG();
        if (mHandlerDjs != null) {
            mHandlerDjs.removeMessages(1);
            mHandlerDjs.removeMessages(2);
            mHandlerDjs.removeMessages(3);
            mHandlerDjs.removeMessages(4);
        }
        unregisterReceiver(timeReceiver);
        sensorManager.unregisterListener(this);
        djs_vibrator.cancel();
        mSportsApp = null;

        if (typeId == SportsType.TYPE_WALK || typeId == SportsType.TYPE_RUN
                || typeId == SportsType.TYPE_CLIMBING) {
            unbindStepService();
            Editor edit = mState.edit();
            edit.putInt("steps", 0);
            edit.commit();
        }
        super.onDestroy();
    }

    private void unbindStepService() {
        Log.i(TAG, "[SERVICE] Unbind");
        unbindService(mConnection);
    }

//	class AddCoinsHandler extends Handler {
//		@Override
//		public void handleMessage(Message msg) {
//			// TODO Auto-generated
//			// method stub
//			ApiBack results = (ApiBack) msg.obj;
//			//
//			switch (msg.what) {
//			case ApiConstant.COINS_SUCCESS:
//				int grow_now = Integer.parseInt(results.getMsg());
//				if (coins == grow_now) {
//					SportTaskUtil.jump2CoinsDialog(SportingMapActivity.this,
//							getString(R.string.sports_coins, grow_now));
//				} else {
//					SportTaskUtil.jump2CoinsDialog(
//							SportingMapActivity.this,
//							getString(R.string.sports_coins_limit_sucess,
//									grow_now));
//				}
//
//				break;
//			case ApiConstant.COINS_LIMIT:
//				Toast.makeText(mContext,
//						getString(R.string.sports_coins_limit_fail),
//						Toast.LENGTH_SHORT).show();
//				break;
//			default:
//				break;
//			}
//			Message msgs = Message.obtain();
//			msgs.what = thereturn;
//			msgs.obj = theApiBack;
////			sportHandler.sendMessage(msgs);
//		}
//	}

//	private void uploadcoins() {
//		new AddCoinsThread(coins, 3, addCoinsHandler, typeId).start();
//	}

    public Dialog onCreateDialog(int id, Bundle bundle, final int taskid) {
        String message = bundle.getString("message");
        switch (id) {
            case 1:
                Dialog loginPregressDialog = new Dialog(mContext,
                        R.style.sports_dialog);
                LayoutInflater mInflater = mContext.getLayoutInflater();
                View v1 = mInflater.inflate(R.layout.sports_progressdialog, null);
                TextView msg = (TextView) v1.findViewById(R.id.message);
                msg.setText(message);
                v1.setMinimumWidth((int) (SportsApp.ScreenWidth * 0.8));
                loginPregressDialog.setContentView(v1);
                loginPregressDialog.setCancelable(false);
                loginPregressDialog.setCanceledOnTouchOutside(false);
                loginPregressDialog.setOnKeyListener(new OnKeyListener() {

                    @Override
                    public boolean onKey(DialogInterface arg0, int keyCode,
                                         KeyEvent event) {
                        // TODO Auto-generated method stub
                        if (keyCode == KeyEvent.KEYCODE_BACK
                                && event.getRepeatCount() == 0) {
                            return true;
                        }
                        return false;
                    }
                });
                return loginPregressDialog;
            case 2:
                alertDialog = new Dialog(SportingMapActivity.this,
                        R.style.sports_dialog1);
                alertDialog.setCanceledOnTouchOutside(false);
                LayoutInflater mInflater1 = getLayoutInflater();
//			View v = mInflater1.inflate(R.layout.sport_dialog_for_task, null);
                View v = mInflater1.inflate(R.layout.sport_dialog_for_newtask, null);
                ((TextView) v.findViewById(R.id.message)).setText(message);
                v.findViewById(R.id.bt_ok).setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View arg0) {
                                FunctionStatic.onPause(SportingMapActivity.this,
                                        FunctionStatic.FUNCTION_START_SPORTS,
                                        preTime);

                                isEndSave = true;
                                if (sensorManager != null) {
                                    sensorManager.unregisterListener(SportingMapActivity.this);
                                }
                                if (dis == 0 || con == 0) {
                                    Toast.makeText(
                                            mContext,
                                            getString(R.string.sports_no_trajectory_to_save),
                                            Toast.LENGTH_SHORT).show();
                                    finish();
                                    return;
                                }

                                if (mSportsApp.isOpenNetwork()
                                        && mSportsApp.LoginOption) {

                                    if (timer != null) {
                                        timer.cancel();
                                    }
                                    isStart = false;
                                    mSportsApp.isStartY = false;
                                    isPause = true;
                                    if (alertDialog.isShowing())
                                        alertDialog.dismiss();
//								if (mUploadDialog != null
//										&& !mUploadDialog.isShowing())
//									mUploadDialog.show();
                                    isMedia = false;

                                    double avgSpeed = (dis * 3600) / recLen;
                                    Boolean normalRange = SportTaskUtil
                                            .getNormalRange(typeId, avgSpeed,
                                                    recLen);
                                    if (normalRange && !speedOverLimit) {
                                        if (mUploadDialog != null
                                                && !mUploadDialog.isShowing())
                                            mUploadDialog.show();
                                        coins = dis < 1 ? (dis > 0.5 ? 1 : 0)
                                                : (int) Math.floor(dis);
                                        if ((coins + randomCoins) > 0) {
                                            hashMap.put(
                                                    "coins",
                                                    String.valueOf(coins
                                                            + randomCoins));
                                            vp = new VoicePrompt(
                                                    getApplicationContext(),
                                                    "female", "end", hashMap);
                                            vp.playVoice();
                                        }
//									if (taskID > 0) {
//										updateSportTask();
//										Log.i("updateSportTask", "启动更新线程！");
//									} else {
//										uploadSportTask();
//										Log.i("uploadSportTask", "启动上传线程！");
//									}
                                        stepNum = (int) (dis * 10000 / 6);
//									uploadSportTask();
                                    } else {
                                        Editor editor = foxSportSetting.edit();
                                        editor.putBoolean("isupload", true);
                                        editor.commit();

                                        Toast.makeText(
                                                mContext,
                                                getString(R.string.sports_beyond_limit),
                                                Toast.LENGTH_SHORT).show();
                                        //超出身体极限的不能上传
                                        isUpload = 1;
//									 saveSportTask();
                                        int res = save(true);
                                        finish();
                                    }

                                } else {
                                    Editor editor = foxSportSetting.edit();
                                    editor.putBoolean("isupload", false);
                                    editor.commit();
                                    Editor qEditor = mContext.getSharedPreferences(
                                            "qq_health_sprots", 0).edit();
                                    qEditor.putString(startTime, startTime);
                                    qEditor.commit();
                                    Log.v(TAG, "startTimeQQ =" + startTime);
                                    if (mSportsApp.LoginOption) {
                                        Toast.makeText(
                                                mContext,
                                                getString(R.string.error_cannot_access_net),
                                                Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(
                                                mContext,
                                                getString(R.string.sports_goto_login),
                                                Toast.LENGTH_SHORT).show();
                                    }
                                    isUpload = -1;
//								saveSportTask();
                                }
                                sporttype.remove("save");
                                sporttype.put("save", "yes");
                                MobclickAgent.onEventEnd(SportingMapActivity.this,
                                        "uploadtask", sporttype.toString());
                            }
                        });

                v.findViewById(R.id.bt_giveup).setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View arg0) {
                                if (timer != null) {
                                    timer.cancel();
                                }

                                db = SportSubTaskDB.getInstance(mContext
                                        .getApplicationContext());
                                try {
                                    db.delete(uid, startTime);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                } finally {
                                    db.close();
                                }

                                isStart = false;
                                mSportsApp.isStartY = false;
                                if (alertDialog.isShowing())
                                    alertDialog.dismiss();
                                isPause = true;
                                if (taskID > 0) {
                                    try {
                                        if (mSportsApp.isOpenNetwork()) {
//										deleteSportTaskByID();
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                                sporttype.remove("save");
                                sporttype.put("save", "no");
                                MobclickAgent.onEventEnd(SportingMapActivity.this,
                                        "uploadtask", sporttype.toString());
                                finish();
                            }
                        });

                v.findViewById(R.id.bt_cancel).setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View arg0) {
                                if (alertDialog.isShowing())
                                    alertDialog.dismiss();
                                if (!isPause) {
                                    isPause = false;
                                    isStart = true;
                                    if (!isOpen) {
                                        if (mWakeLock != null
                                                && !mWakeLock.isHeld())
                                            mWakeLock.acquire();
                                    }
                                    SendLockScreenMSG();
                                    if (timingMgr != null) {
                                        timingMgr.repeatTimingFiveMinutes();
                                    }
                                    startTimeSeconds = System.currentTimeMillis();
                                    mTempCount = recLen;
                                }
                                canSave = true;
                            }
                        });
                alertDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {

                    @Override
                    public boolean onKey(DialogInterface dialog, int keyCode,
                                         KeyEvent event) {
                        if (keyCode == KeyEvent.KEYCODE_BACK
                                && event.getAction() == KeyEvent.ACTION_DOWN) {
                            Log.v(TAG, "KEYCODE_BACK alertDialog");
                            if (alertDialog.isShowing())
                                alertDialog.dismiss();
                            if (!isPause) {
                                isPause = false;
                                isStart = true;
                                if (!isOpen) {
                                    if (mWakeLock != null && !mWakeLock.isHeld())
                                        mWakeLock.acquire();
                                }
                                SendLockScreenMSG();
                                if (timingMgr != null) {
                                    timingMgr.repeatTimingFiveMinutes();
                                }
                                startTimeSeconds = System.currentTimeMillis();
                                mTempCount = recLen;
                            }
                            canSave = true;
                        }
                        return false;
                    }

                });
                v.setMinimumWidth((int) (SportsApp.ScreenWidth * 0.9));
                alertDialog.setCancelable(true);
                alertDialog.setContentView(v);
                alertDialog.show();
                break;
        }
        return null;
    }

//	@Override
//	protected void onStart() {
//		// TODO Auto-generated method stub
//		super.onStart();
//	}

//	@Override
//	protected void onStop() {
//		djs_vibrator.cancel();
//		super.onStop();
//	}

//	private void uploadSportTask() {
//		new Thread() {
//			@Override
//			public void run() {
//				Message msg = Message.obtain();
//				String pointString;
//				if (mGeoPoints != null) {
//					pointString = SportTrajectoryUtil
//							.listLatLngToString(mGeoPoints);
//				} else {
//					pointString = null;
//				}
//				Log.i("pointString", pointString);
//				ApiBack apiBack = null;
//				try {
//					apiBack = ApiJsonParser.uploadSportTask(limitId,
//							mSportsApp.getSessionId(), typeId, typeDetailId,
//							deviceId, startTime, recLen, dis, con, (dis * 3600)
//									/ recLen, heart, pointString, stepNum,
//							SportsApp.MAP_TYPE_BAIDU,
//							DeviceUuidFactory.getDeviceSerial(),markCode);
//					Log.i("uploadSportTask", "taskID =" + taskID + ",recLen="
//							+ recLen + ",dis =" + dis + ",con=" + con
//							+ ",speed=" + speed + ",stepNum=" + stepNum);
//					if (apiBack != null && apiBack.getFlag() == 1) {
//						isUpload = 1;
//						taskID = apiBack.getReg();
//						ed.putInt("taskID", taskID);
//						ed.commit();
//						Log.i("uploadSportTask", "taskID = " + taskID);
//						theApiBack = apiBack;
//						thereturn = UPLOAD_START;
//						uploadcoins();
//					} else {
//						isUpload = -1;
//						Message msgfail = Message.obtain();
//						msgfail.what = UPLOAD_START;
//						msgfail.obj = apiBack;
//						sportHandler.sendMessage(msgfail);
//					}
//				} catch (ApiNetException e) {
//					e.printStackTrace();
//					isUpload = -1;
//					msg.what = UPLOAD_PARAM_ERROR;
//					sportHandler.sendMessage(msg);
//					return;
//				} catch (ApiSessionOutException e) {
//					e.printStackTrace();
//					isUpload = -1;
//					Message msgfail = Message.obtain();
//					msgfail.what = UPLOAD_START;
//					msgfail.obj = apiBack;
//					sportHandler.sendMessage(msgfail);
//				}
//			}
//		}.start();
//	}

//	private void updateSportTask() {
//		new Thread() {
//			@Override
//			public void run() {
//				Message msg = Message.obtain();
//
//				String pointString;
//				if (mGeoPoints != null) {
//					pointString = SportTrajectoryUtil
//							.listLatLngToString(mGeoPoints);
//				} else {
//					pointString = null;
//				}
//
//				Log.i("pointString", pointString);
//				ApiBack apiBack = null;
//				try {
//					apiBack = ApiJsonParser
//							.updateSportTask(taskID, limitId,
//									mSportsApp.getSessionId(), typeId,
//									deviceId, startTime, recLen, dis, con,
//									(dis * 3600) / recLen, heart, pointString,
//									stepNum, mSportsApp.mCurMapType);
//					Log.i("SportingMapActivity.updateSportTask", "taskID ="
//							+ taskID + ",recLen=" + recLen + ",dis =" + dis
//							+ ",con=" + con + ",speed=" + speed + ",stepNum="
//							+ stepNum);
//					if (apiBack != null && apiBack.getFlag() == 1) {
//						isUpload = 1;
//						theApiBack = apiBack;
//						thereturn = UPLOAD_UPDATE;
//						uploadcoins();
//					} else {
//						isUpload = -1;
//						Message msgfail = Message.obtain();
//						msgfail.what = UPLOAD_UPDATE;
//						msgfail.obj = apiBack;
//						sportHandler.sendMessage(msgfail);
//					}
//					Log.i("SportingMapActivity.updateSportTask", "isUpload:"
//							+ isUpload);
//				} catch (ApiNetException e) {
//					e.printStackTrace();
//					msg.what = UPLOAD_PARAM_ERROR;
//					sportHandler.sendMessage(msg);
//					return;
//				} catch (ApiSessionOutException e) {
//					e.printStackTrace();
//					isUpload = -1;
//					Message msgfail = Message.obtain();
//					msgfail.what = UPLOAD_UPDATE;
//					msgfail.obj = apiBack;
//					sportHandler.sendMessage(msgfail);
//				}
//			}
//		}.start();
//	}

//	private void uploadSportMedia() {
//		new Thread() {
//			@Override
//			public void run() {
//				Message msg = Message.obtain();
//				curMediaLoc = curLocData;
//				String pointString = SportTrajectoryUtil
//						.LatLngToString(curMediaLoc);
//				ApiBack apiBack = null;
//				try {
//					apiBack = ApiJsonParser.uploadSportTaskMedia(
//							mSportsApp.getSessionId(), uid, taskID,
//							mediaTypeID, String.valueOf(mediaFileDuration),
//							mediaFilePath, mediaFileName, pointString,
//							mediaFileWidth, mediaFileHight,
//							mSportsApp.mCurMapType);
//
//					Log.i("uploadSportTaskMedia", mSportsApp.getSessionId()
//							+ uid + taskID + mediaTypeID + mediaFilePath
//							+ mediaFileName + pointString + ",时长："
//							+ mediaFileDuration + "----" + mediaFileWidth
//							+ mediaFileHight);
//				} catch (ApiNetException e) {
//					e.printStackTrace();
//				} catch (ApiSessionOutException e) {
//					e.printStackTrace();
//				}
//				msg.what = UPLOAD_MEDIA;
//				msg.obj = apiBack;
//				sportHandler.sendMessage(msg);
//			}
//		}.start();
//	}

//	public void UploadPhoto() {
//		new Thread() {
//			@Override
//			public void run() {
//				Message msg = Message.obtain();
//				curMediaLoc = curLocData;
//				String pointString = SportTrajectoryUtil
//						.LatLngToString(curMediaLoc);
//				ApiBack apiBack = null;
//				try {
//					String picTitle = path.substring(path.lastIndexOf("/") + 1);
//					apiBack = ApiJsonParser.uploadSportTaskMedia(mSportsApp
//							.getSessionId(),
//							mSportsApp.getSportUser().getUid(), taskID,
//							mediaTypeID, String.valueOf(1), path, picTitle,
//							pointString, 0, 0, mSportsApp.mCurMapType);
//
//					Log.i("uploadSportTaskMedia",
//							"sessionId = " + mSportsApp.getSessionId()
//									+ ",uid = "
//									+ mSportsApp.getSportUser().getUid()
//									+ ",taskID = " + taskID + ",mediaTypeID = "
//									+ mediaTypeID + ",mediaFilePath = " + path
//									+ ",mediaFileName = " + picTitle
//									+ ",pointString = " + pointString + "---"
//									+ String.valueOf(1));
//
//				} catch (ApiNetException e) {
//					e.printStackTrace();
//				} catch (ApiSessionOutException e) {
//					e.printStackTrace();
//				}
//				msg.what = UPLOAD_MEDIA_PHOTO;
//				msg.obj = (apiBack != null ? true : false);
//				sportHandler.sendMessage(msg);
//			}
//		}.start();
//	}

//	private void saveSportTask() {
//		new Thread() {
//			@Override
//			public void run() {
//				Message msg = Message.obtain();
//				msg.what = SAVE_LOCAL;
//				sportHandler.sendMessage(msg);
//			}
//		}.start();
//	}

//	private void deleteSportTaskByID() {
//		new Thread() {
//			@Override
//			public void run() {
//				Message msg = Message.obtain();
//				ApiBack apiBack = null;
//				try {
//					apiBack = ApiJsonParser.deleteSportsTaskById(
//							mSportsApp.getSessionId(), uid, taskID);
//				} catch (ApiNetException e) {
//					e.printStackTrace();
//				} catch (ApiSessionOutException e) {
//					e.printStackTrace();
//				}
//				msg.what = UPLOAD_DELETE;
//				msg.obj = apiBack;
//				sportHandler.sendMessage(msg);
//			}
//		}.start();
//	}

//	private Handler sportHandler = new Handler() {
//		@Override
//		public void handleMessage(Message msg) {
//			super.handleMessage(msg);
//			switch (msg.what) {
//			case UPLOAD_START:
//				if (!isMedia) {
//					int res = save(true);
//					if (res > 0) {
//						if (typeDetailId < 0) {
//							typeDetailId = 0;
//						}
//					}
//					uploadDataToQQ();
//				}
//
//				ApiBack back = (ApiBack) msg.obj;
//				if (back != null && back.getFlag() == 1) {
//					if (isMedia == false) {
//						Toast.makeText(mContext,
//								getString(R.string.upload_success),
//								Toast.LENGTH_SHORT).show();
//
//					}
//
//					Log.i("上传数据", "上传数据---->" + back.getMsg());
//				} else {
//					if (isMedia == false) {
//						Editor editor = foxSportSetting.edit();
//						editor.putBoolean("isupload", false);
//						editor.commit();
//						if (mSportsApp.LoginOption) {
//							Toast.makeText(mContext,
//									getString(R.string.upload_failed),
//									Toast.LENGTH_SHORT).show();
//						} else {
//							Toast.makeText(mContext,
//									getString(R.string.sports_goto_login),
//									Toast.LENGTH_SHORT).show();
//						}
//					}
//				}
//				isUpload = -1;
//				if (mUploadDialog != null && mUploadDialog.isShowing()) {
//					mUploadDialog.dismiss();
//				}
//				break;
//			case UPLOAD_UPDATE:
//				int res = save(true);
//				if (res > 0) {
//					if (typeDetailId < 0) {
//						typeDetailId = 0;
//					}
//					uploadDataToQQ();
//				}
//
//				ApiBack upback = (ApiBack) msg.obj;
//				if (upback != null && upback.getFlag() == 1) {
//					Toast.makeText(mContext,
//							getString(R.string.upload_success),
//							Toast.LENGTH_SHORT).show();
//					Log.i("上传数据", "上传数据---->" + upback.getMsg());
//				} else {
//
//					Editor editor = foxSportSetting.edit();
//					editor.putBoolean("isupload", false);
//					editor.commit();
//
//					if (mSportsApp.LoginOption) {
//						Toast.makeText(mContext,
//								getString(R.string.upload_failed),
//								Toast.LENGTH_SHORT).show();
//					} else {
//						Toast.makeText(mContext,
//								getString(R.string.sports_goto_login),
//								Toast.LENGTH_SHORT).show();
//					}
//				}
//				isUpload = -1;
//				if (mUploadDialog != null && mUploadDialog.isShowing()) {
//					mUploadDialog.dismiss();
//				}
//				break;
//			case UPLOAD_MEDIA:
//				mediaFileDuration = 0;
//				mediaFileWidth = 0;
//				mediaFileHight = 0;
//				ApiBack back2 = (ApiBack) msg.obj;
//				if (back2 != null) {
//					Log.i("UPLOAD_MEDIA", back2.toString() + ", isMedia="
//							+ isMedia);
//					if (back2.getFlag() == 1) {
//						int mediaId = back2.getReg();
//						addMediaItem(curMediaLoc, mediaTypeID);
//						Toast.makeText(mContext,
//								getString(R.string.upload_success),
//								Toast.LENGTH_SHORT).show();
//						Log.i("UPLOAD_MEDIA", "mediaID =" + mediaId);
//						MobclickAgent.onEvent(mContext, "uploadmedia", "video");
//					} else {
//						Toast.makeText(mContext,
//								getString(R.string.upload_failed),
//								Toast.LENGTH_SHORT).show();
//					}
//				} else {
//					Toast.makeText(mContext,
//							getString(R.string.sports_upload_fail_error_param),
//							Toast.LENGTH_SHORT).show();
//				}
//
//				if (mUploadDialog != null && mUploadDialog.isShowing()) {
//					mUploadDialog.dismiss();
//				}
//				break;
//			case UPLOAD_PARAM_ERROR:
//				if (!isMedia) {
//					int res1 = save(true);
//					if (res1 > 0) {
//						if (typeDetailId < 0) {
//							typeDetailId = 0;
//						}
//					}
//				}
//				if (mUploadDialog != null && mUploadDialog.isShowing()) {
//					mUploadDialog.dismiss();
//				}
//				canSave = true;
////				Toast.makeText(mContext,
////						getString(R.string.sports_upload_fail_error_param),
////						Toast.LENGTH_SHORT).show();
//				Toast.makeText(mContext,
//						getString(R.string.upload_failed),
//						Toast.LENGTH_SHORT).show();
//				break;
//			case UPLOAD_DELETE:
//				ApiBack back3 = (ApiBack) msg.obj;
//				if (back3 != null && back3.getFlag() == 1) {
//					Log.i("UPLOAD_DELETE", "删除成功！" + back3.getMsg());
//				} else {
//					// Log.i("UPLOAD_DELETE", "删除失败！" + back3.getMsg());
//				}
//				break;
//			case UPLOAD_MEDIA_PHOTO:
//				mediaFileDuration = 0;
//				mediaFileWidth = 0;
//				mediaFileHight = 0;
//				isBack = -1;
//				SharedPreferences sap = mContext.getSharedPreferences(
//						"upload_state_" + uid, Context.MODE_PRIVATE);
//				Editor ed = sap.edit();
//				ed.putInt("isback", isBack);
//				ed.commit();
//				Boolean back4 = (Boolean) msg.obj;
//				if (back4) {
//					Log.i("UPLOAD_MEDIA", back4.toString() + ", isMedia="
//							+ isMedia);
//					addMediaItem(curMediaLoc, mediaTypeID);
//					Toast.makeText(mContext,
//							getString(R.string.upload_success),
//							Toast.LENGTH_SHORT).show();
//					if (mediaTypeID == 1)
//						MobclickAgent.onEvent(mContext, "uploadmedia", "photo");
//					else if (mediaTypeID == 2)
//						MobclickAgent.onEvent(mContext, "uploadmedia", "voice");
//				} else {
//					Toast.makeText(mContext, getString(R.string.upload_failed),
//							Toast.LENGTH_SHORT).show();
//				}
//
//				if (mUploadDialog != null && mUploadDialog.isShowing()) {
//					mUploadDialog.dismiss();
//				}
//				break;
//			case SAVE_LOCAL:
//				int res1 = save(true);
//				if (res1 > 0) {
//					if (typeDetailId < 0) {
//						typeDetailId = 0;
//					}
//				}
//				gotoSportDetailActivity();
//				break;
//			case STEPS_MSG:
//				int step = (int) msg.arg1;
//				textview_stepnum.setText(step + "步数");
//				break;
//			}
//		};
//	};

//	private void uploadDataToQQ() {
//		int sportGoal = foxSportSetting.getInt("editDistance", 0);
//		boolean result = SportTaskUtil.send2QQ(mContext, typeId, sportGoal,
//				startTime, startTimeSeconds / 1000L + "", uid,
//				new QQHealthResult() {
//
//					@Override
//					public void qqResult() {
//						gotoSportDetailActivity();
//					}
//				});// 运动数据上传至qq健康平台
//		if (!result) {
//			gotoSportDetailActivity();
//		}
//	}

//	private void gotoSportDetailActivity() {
//		Intent intent = new Intent(SportingMapActivity.this, SportTaskDetailActivity.class);
//		intent.putExtra("taskid", taskID);
//		if (mSportsApp!=null&&mSportsApp.getSportUser()!=null) {
//			intent.putExtra("uid", mSportsApp.getSportUser().getUid());
//		}
//		intent.putExtra("startTime", startTime);
//		startActivity(intent);
//		finish();
//	}

//	private Handler mDrawTrajectoryHandler = new Handler() {
//		@Override
//		public void handleMessage(Message msg) {
//			super.handleMessage(msg);
//			switch (msg.what) {
//			case DRAWTRAJECTORY:
//				new Thread() {
//					public void run() {
//						drawMap();
//					};
//				}.start();
//				break;
//			}
//		}
//	};

    private void drawMap() {
        int size = mDrawPoints.size();
        if (size > 0) {
            locData = mDrawPoints.get(size - 1);
        }
        if (mGeoPoints.size() == 1) {
            if (mMarkerStart == null) {
                mMarkerStart = (Marker) aMap.addOverlay(mMarkerOpStart
                        .position(locData));
            } else {
                mMarkerStart.setPosition(locData);
            }
        } else if (size == 1) {
            MarkerOptions MarkerMiddle = new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
                            .decodeResource(getResources(),
                                    R.drawable.map_middle)));
            aMap.addOverlay(MarkerMiddle.position(locData));
        } else if (size > 1) {
            if (mMarkerMiddle == null) {
                mMarkerMiddle = (Marker) aMap.addOverlay(mMarkerOpMiddle
                        .position(locData));
            } else {
                mMarkerMiddle.setPosition(locData);
            }

            if (mPolyline == null) {
                options.points(mDrawPoints);
                mPolyline = (Polyline) aMap.addOverlay(options);
            } else {
                mPolyline.setPoints(mDrawPoints);
            }
        }

        try {
            if (mMapView != null) {
                if (locData != null) {
                    animateTo(locData);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private double disVal = 0;
    private int randomCoins;
    private int mRandom = 5;
    private double mKilometre = 1;
    private boolean isgps = false;

//	private boolean LocationUpdate(BDLocation location) {
//		int size = mGeoPoints.size();
//		if (size == 0) {
//			if (location == null)
//				return false;
//			mLat = location.getLatitude();
//			mLng = location.getLongitude();
//			mGeoPoints.add(new LatLng(mLat, mLng));
//			mDrawPoints.add(new LatLng(mLat, mLng));
//			if (dis > 0) {
//				isgps = true;
//			}
//			if (isStart && !isPause)
////				mDrawTrajectoryHandler.sendEmptyMessage(DRAWTRAJECTORY);
//		} else if (size > 0) {
//			if (location == null)
//				return false;
//			if (processResume) {
//				processResume = false;
//				mLat = location.getLatitude();
//				mLng = location.getLongitude();
//				mGeoPoints.add(new LatLng(mLat, mLng));
//				mDrawPoints.add(new LatLng(mLat, mLng));
//				if (isStart && !isPause)
////					mDrawTrajectoryHandler.sendEmptyMessage(DRAWTRAJECTORY);
//				return true;
//			}
//			double dmi = SportTaskUtil.gps2m(mLat, mLng,
//					location.getLatitude(), location.getLongitude());
//			int mi = (int) Math.abs(dmi);
//
//			if (locData.latitude != location.getLatitude()
//					|| locData.longitude != location.getLongitude()) {
//				if ((mi > 0 && mi < 200)) {
//					int lastmi = 0;
//					if (lastPointInValid) {
//						LatLng latlng = mGeoPoints.get(size - 1);
//						if (latlng.latitude != SportTrajectoryUtil.INVALID_LATLNG
//								&& latlng.longitude != SportTrajectoryUtil.INVALID_LATLNG) {
//							lastmi = (int) Math.abs(SportTaskUtil.gps2m(
//									latlng.latitude, latlng.longitude, mLat,
//									mLng));
//						}
//					}
//					disVal = disVal + mi * 1.05;
//					disVal = disVal + lastmi * 1.05;
//					Log.i("disVal", "disVal = " + disVal);
//					// dis = disVal * 1.0 / 1000;
//					if (isgps) {
//						dis = disVal * 1.0 / 1000 + dis;
//						isgps = false;
//					} else {
//						dis = disVal * 1.0 / 1000;
//					}
//					disStringValue = SportTaskUtil.getDoubleNum(dis);
//					if (disValue != null) {
//						disValue.setText(disStringValue);
//					}
//					time_disValue.setText(disStringValue);
//					zong_li_txt.setText(disStringValue);
//					double avgSpeed = (dis * 3600) / recLen;
//					Boolean normalRange = SportTaskUtil.getNormalRange(typeId,
//							avgSpeed, recLen);
//					if (normalRange && !speedOverLimit && dis >= 1
//							&& Math.floor(dis) % mKilometre == 0) {
//						mKilometre += 1;
//						// String pace=String.valueOf(paceValue.getText());
//						String pace = String.valueOf(paceStringValue);
//						hashMap.put("gongli", String.valueOf((int) (dis)));
//						hashMap.put(
//								"yongshi",
//								String.valueOf(System.currentTimeMillis()
//										- startTimeSeconds));
//						hashMap.put("peisu_fen",
//								pace.substring(0, pace.indexOf("'")));
//						hashMap.put(
//								"peisu_miao",
//								pace.substring(pace.indexOf("'") + 1,
//										pace.indexOf("\"")));
//						vp = new VoicePrompt(getApplicationContext(), "female",
//								"moving", hashMap);
//						vp.playVoice();
//					}
//					if (normalRange && !speedOverLimit && dis >= 5
//							&& Math.floor(dis) % mRandom == 0) {
//						mRandom += 5;
//						int randomNum = (int) (Math.random() * 100) + 1;
//						if (randomNum >= 1 && randomNum <= 69)
//							randomCoins = 0;
//						else if (randomNum >= 70 && randomNum <= 79)
//							randomCoins = 1;
//						else if (randomNum >= 80 && randomNum <= 85)
//							randomCoins = 2;
//						else if (randomNum >= 86 && randomNum <= 90)
//							randomCoins = 5;
//						else if (randomNum >= 91 && randomNum <= 94)
//							randomCoins = 10;
//						else if (randomNum >= 95 && randomNum <= 98)
//							randomCoins = 30;
//						else if (randomNum >= 99 && randomNum <= 100)
//							randomCoins = 50;
//						if (mSportsApp.isOpenNetwork()
//								&& mSportsApp.LoginOption) {
//							new AddCoinsThread(randomCoins, 2, new Handler() {
//
//								@Override
//								public void handleMessage(Message msg) {
//									ApiBack apiBack = (ApiBack) msg.obj;
//									if (apiBack != null
//											&& apiBack.getFlag() == 0) {
//										SportTaskUtil.jump2CoinsDialog(
//												SportingMapActivity.this,
//												getString(
//														R.string.random_coins,
//														randomCoins));
//									} else {
////										foxSportSetting
////												.edit()
////												.putInt("randomCoins",
////														foxSportSetting.getInt(
////																"randomCoins",
////																0)
////																+ randomCoins)
////												.commit();
//										//随机金币布累加
//										foxSportSetting
//										.edit()
//										.putInt("randomCoins",
//												randomCoins).commit();
//									}
//								}
//
//							}, -1).start();
//						} else {
////							foxSportSetting
////									.edit()
////									.putInt("randomCoins",
////											foxSportSetting.getInt(
////													"randomCoins", 0)
////													+ randomCoins).commit();
//							//随机金币布累加
//							foxSportSetting
//							.edit()
//							.putInt("randomCoins",
//									randomCoins).commit();
//						}
//					}
//					Log.i(TAG + ".LocationUpdate", "typeId:" + typeId);
//					if (typeId == SportsType.TYPE_WALK) {
//						con = (int) WalkingUtils.getCalories(dis);
//					} else if (typeId == SportsType.TYPE_RUN) {
//						con = (int) RunningUtils.getCalories(dis);
//					} else if (typeId == SportsType.TYPE_CLIMBING) {
//						con = (int) MountainUtils.getCalories(dis);
//					} else if (typeId == SportsType.TYPE_GOLF) {
//						con = (int) GolfingUtils.getCalories(dis);
//					} else if (typeId == SportsType.TYPE_WALKRACE) {
//						con = (int) WalkingRaceUtils.getCalories(dis);
//					} else if (typeId == SportsType.TYPE_CYCLING) {
//						con = (int) RidingUtils.getCalories(dis);
//					} else if (typeId == SportsType.TYPE_FOOTBALL) {
//						con = (int) FootballUtils.getCalories(dis);
//					} else if (typeId == SportsType.TYPE_ROWING) {
//						con = (int) RowingUtils.getCalories(dis);
//					} else if (typeId == SportsType.TYPE_SWIM) {
//						con = (int) SwimmingUtils.getCalories(disVal, recLen,
//								typeDetailId + 1);
//					}
//					conStringValue = con + "";
//					sporting_sportxiaohao.setText(conStringValue + "Cal");
//					if (conValue != null) {
//						conValue.setText(con + "");
//					}
//					if (lastPointInValid) {
//						mGeoPoints.add(new LatLng(mLat, mLng));
//						mDrawPoints.add(new LatLng(mLat, mLng));
//						lastPointInValid = false;
//					}
//					mLat = location.getLatitude();
//					mLng = location.getLongitude();
//					mGeoPoints.add(new LatLng(mLat, mLng));
//					mDrawPoints.add(new LatLng(mLat, mLng));
//					if (!activityPause && isStart && !isPause)
//						mDrawTrajectoryHandler.sendEmptyMessage(DRAWTRAJECTORY);
//				} else {
//					mLat = location.getLatitude();
//					mLng = location.getLongitude();
//					lastPointInValid = true;
//					return false;
//				}
//			} else {
//				return false;
//			}
//		}
//		Log.i("LocationUpdate", "mGeoPoints的大小:" + mGeoPoints.size());
//		return true;
//	}

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
            String provider = Settings.Secure.getString(
                    mContext.getContentResolver(),
                    Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            if (!provider.contains("gps")) { // if gps is disabled
                final Intent poke = new Intent();
                poke.setClassName("com.android.settings",
                        "com.android.settings.widget.SettingsAppWidgetProvider");
                poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
                poke.setData(Uri.parse("3"));
                mContext.sendBroadcast(poke);
            }
            Log.i("toggleGPS", "GPS已开启!");
        } else {
            Log.i("toggleGPS", "GPS未开启!");
        }
    }

    // 保存本地数据库
    private int save(boolean isShow) {
        // 地图定位点
        String pointString;
        if (mGeoPoints != null) {
            pointString = SportTrajectoryUtil.listLatLngToString(mGeoPoints);
        } else {
            pointString = null;
        }

        stepNum = (int) (dis * 10000 / 6);

        // 开始运动时间(年月日)
        Date startDate = new Date(startTimeSeconds);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String sportDate = formatter.format(startDate);
        Log.i("sporttest", "sportDate_0:" + sportDate);

        db = SportSubTaskDB.getInstance(mContext.getApplicationContext());

        ContentValues values = new ContentValues();
        values.put(SportSubTaskDB.UID, uid);
        values.put(SportSubTaskDB.LIMIT, limitId);
        values.put(SportSubTaskDB.SPORT_TYPE, typeId);
        values.put(SportSubTaskDB.SPORT_SWIM_TYPE, typeDetailId);
        values.put(SportSubTaskDB.SPORT_DEVICE, deviceId);
        values.put(SportSubTaskDB.SPORT_START_TIME, startTime);// startTime等同于startTimeSeconds
        values.put(SportSubTaskDB.SPORT_TIME, recLen);
        values.put(SportSubTaskDB.SPORT_STEP, stepNum);
        values.put(SportSubTaskDB.SPORT_DISTANCE, dis);
        values.put(SportSubTaskDB.SPORT_CALORIES, con);
        values.put(SportSubTaskDB.SPORT_SPEED, (dis * 3600) / recLen);
        values.put(SportSubTaskDB.SPORT_HEART_RATE, heart);
        values.put(SportSubTaskDB.SPORT_ISUPLOAD, isUpload);
        values.put(SportSubTaskDB.SPORT_DATE, sportDate);
        values.put(SportSubTaskDB.SPORT_TASKID, taskID);
        values.put(SportSubTaskDB.SPORT_LAT_LNG, pointString);
        values.put(SportSubTaskDB.SPORT_MAPTYPE, mSportsApp.mCurMapType);
        values.put(SportSubTaskDB.SPORT_MARKCODE, markCode);//新增唯一标识码

        int result = 0;
        if (isFirstSave) {
            result = db.insert(values, isShow);
            Log.i("ContentValues0", "values:" + values);
            Log.i("insert0---", "insert id=" + result);
            isFirstSave = false;
        } else {
            result = db.update(values, uid, startTime, isEndSave, markCode);
        }

        return result;
    }

    private void startService() {
        startService(new Intent(SERVICE_NAME));
    }

    private void stopService() {
        stopService(new Intent(SERVICE_NAME));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == LOCKSCREEN && resultCode == RESULT_OK) {
            Log.v(TAG, "onActivityResult back from lockscreen activity");
            return;
        }
        String sdState = Environment.getExternalStorageState();
        if (!sdState.equals(Environment.MEDIA_MOUNTED)) {
            Log.i(TAG, "sd card unmount");
            Toast.makeText(mContext, getString(R.string.sd_card_is_invalid),
                    Toast.LENGTH_SHORT).show();
            return;
        }
        Log.i("onActivityResult", "返回结果！");
        if (requestCode == PHOTOFromCAMERA && resultCode == Activity.RESULT_OK) {
            // && null != data) {
            // SharedPreferences sap = mContext.getSharedPreferences(
            // "upload_state_" + uid, Context.MODE_PRIVATE);
            // isBack = sap.getInt("isback", -1);
            // Log.i("isBack", "isBack = " + isBack);
            // if (isBack > 0) {
            // Boolean isFinish = sap.getBoolean("isfinish", false);
            // Log.i("isfinish", "-------->" + isFinish);
            // Message msg = Message.obtain();
            // msg.what = UPLOAD_MEDIA_PHOTO;
            // msg.obj = isFinish;
            // sportHandler.sendMessage(msg);
            // }
            // Log.i("PHOTOFromCAMERA", "运行到这里！");
            // Log.i("getData", data.toString());
            if (mUploadDialog != null && !mUploadDialog.isShowing())
                mUploadDialog.show();
            path = SportsUtilities.getResizePic(path);
//			UploadPhoto();
        } else if (requestCode == VIDEO && resultCode == Activity.RESULT_OK
                && null != data) {
            Log.i("getData", data.toString());
            Uri uriVideo = data.getData();
            Cursor cursor = mContext.getContentResolver().query(uriVideo, null,
                    null, null, null);
            if (cursor.moveToFirst()) {
                /** _data：文件的绝对路径 ，_display_name：文件名 */
                String strVideoPath = cursor.getString(cursor
                        .getColumnIndex("_data"));
                String fileName = cursor.getString(cursor
                        .getColumnIndex("_display_name"));
                mediaFileName = fileName;
                mediaFilePath = strVideoPath;
                MediaPlayer voicePlay = new MediaPlayer();
                try {
                    voicePlay.setDataSource(strVideoPath);
                    voicePlay.prepare();
                    int dur = (int) (voicePlay.getDuration());
                    Log.i("getDuration", voicePlay.getDuration() + "---" + dur);
                    if ((dur / 1000) < 1) {
                        mediaFileDuration = 0;
                        Toast.makeText(mContext,
                                getString(R.string.sports_video_fail),
                                Toast.LENGTH_SHORT).show();
                    } else {
                        if (mUploadDialog != null && !mUploadDialog.isShowing())
                            mUploadDialog.show();

                        mediaFileWidth = voicePlay.getVideoWidth();
                        mediaFileHight = voicePlay.getVideoHeight();
                        mediaFileDuration = dur;
//						uploadSportMedia();
                    }
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (voicePlay != null) {
                        voicePlay.release();
                    }
                }
                Log.i(TAG, "Video saved to:\n" + strVideoPath + ",时长："
                        + mediaFileDuration);
            }
        } else if (requestCode == VOICE && resultCode == Activity.RESULT_OK
                && null != data) {
            Log.i("getData", data.toString());
            Bundle bundle = data.getExtras();
            Boolean isFinish = bundle.getBoolean("isFinish");
            Log.i("isfinish", "-------->" + isFinish);
            Message msg = Message.obtain();
            msg.what = UPLOAD_MEDIA_PHOTO;
            msg.obj = isFinish;
//			sportHandler.sendMessage(msg);
        } else {
            // if (requestCode == PHOTOFromCAMERA) {
            // SharedPreferences sap = mContext.getSharedPreferences(
            // "upload_state_" + uid, Context.MODE_PRIVATE);
            // isBack = sap.getInt("isback", -1);
            // Log.i("isBack", "isBack = " + isBack);
            // if (isBack > 0) {
            // Boolean isFinish = sap.getBoolean("isfinish", false);
            // Log.i("isfinish", "-------->" + isFinish);
            // Message msg = Message.obtain();
            // msg.what = UPLOAD_MEDIA_PHOTO;
            // msg.obj = isFinish;
            // sportHandler.sendMessage(msg);
            // }
            // } else
            if (requestCode == VOICE) {
                if (resultCode == 3) {
                    Bundle bundle = data.getExtras();
                    Boolean isFinish = bundle.getBoolean("isFinish");
                    Log.i("isfinish", "-------->" + isFinish);
                    Message msg = Message.obtain();
                    msg.what = UPLOAD_MEDIA_PHOTO;
                    msg.obj = isFinish;
//					sportHandler.sendMessage(msg);
                }
            }

            Log.i(TAG, "onActivityResult参数出错！"
                    + "requestCode, resultCode, data" + requestCode
                    + resultCode + data);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private String path = "";

    public void getMediaFile(int typeID) {
        String status = Environment.getExternalStorageState();
        if (status.equals(Environment.MEDIA_MOUNTED)) {
            if (typeID == 1) {
                // curMediaLoc = curLocData;
                // String pointStr = SportTrajectoryUtil
                // .LatLngToString(curMediaLoc);
                // ed.putString("pointStr", pointStr);
                // Log.i("pointStr", pointStr);
                // Intent intent = new Intent(mContext, CameraApp.class);
                // startActivityForResult(intent, PHOTOFromCAMERA);

                try {
                    File file_sys = new File(
                            Environment.getExternalStorageDirectory()
                                    + "/myimage/");
                    if (!file_sys.exists()) {
                        file_sys.mkdirs();
                    }
                    File file = new File(file_sys, String.valueOf(System
                            .currentTimeMillis()) + ".jpg");
                    path = file.getPath();
                    Uri imageUri = Uri.fromFile(file);
                    Intent openCameraIntent = new Intent(
                            MediaStore.ACTION_IMAGE_CAPTURE);
                    openCameraIntent.putExtra(
                            MediaStore.Images.Media.ORIENTATION, 0);
                    openCameraIntent
                            .putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                    startActivityForResult(openCameraIntent, PHOTOFromCAMERA);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(SportingMapActivity.this,
                            getResources().getString(R.string.save_mulu),
                            Toast.LENGTH_LONG).show();
                }
            } else if (typeID == 2) {
                curMediaLoc = curLocData;
                String pointStr = SportTrajectoryUtil
                        .LatLngToString(curMediaLoc);
                Log.i("pointStr", pointStr);
                Intent intent = new Intent(mContext, UploadActivity.class);
                intent.putExtra("pointStr", pointStr);
                intent.putExtra("mediaTypeID", mediaTypeID);
                startActivityForResult(intent, VOICE);
            } else if (typeID == 3) {
                Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                intent.putExtra("autofocus", true); // 自动对焦
                intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0.1);
                intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 30);
                startActivityForResult(intent, VIDEO);
            }
        } else {
            Toast.makeText(mContext, getString(R.string.sd_card_is_invalid),
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration config) {
        super.onConfigurationChanged(config);
    }

    private void initGPS() {
        gpsLocationListener = new GpsLocationListener();
        // 获取系统LocationManager服务
        locationManager = (LocationManager) mContext
                .getSystemService(Context.LOCATION_SERVICE);
        locationManager.addGpsStatusListener(listener);
    }

    private double CalcPressure(double altitude) {
        BigDecimal bd1 = new BigDecimal(Double.toString(44330.0));
        BigDecimal bd2 = new BigDecimal(Double.toString(altitude));
        BigDecimal bd3 = new BigDecimal(Double.toString(44330.0));
        MathContext mc = new MathContext(4, RoundingMode.HALF_UP);
        return Math.pow(bd1.subtract(bd2).divide(bd3, mc).doubleValue(),
                100 / 19) * 101.28;
    }

    private void updateSpeed(Location location) {
        if (activityPause)
            return;
        // String altiStr = "--";
        if (location != null) {
            speed = location.getSpeed() * 3.6f;// 速度
            tempAltitude = SportTaskUtil.getDoubleNum(location.getAltitude());

            altitudeValue.setText("  "
                    + tempAltitude.substring(0, tempAltitude.indexOf('.'))
                    + " m");
            pressureValue.setText("  "
                    + SportTaskUtil.getDoubleNumber(CalcPressure(location
                    .getAltitude())) + " kPa");

            if (speed != 0) {
                pace = 3600 / speed;// 配速：单位 秒/公里
            } else {
                pace = 0;
            }
            // altiStr = SportTaskUtil.getDoubleNum(altitude);
            Log.i("updateSpeed", "location = " + location.toString());
            Log.i("updateSpeed", "pace =" + pace);
        } else {
            speed = 0;
            pace = 0;
        }
        // if (speed > maxSpeed) {
        lastSpeed = newSpeed;
        newSpeed = speed;
        if (!SportTaskUtil.getNormalRange(typeId, newSpeed, recLen)) {
            if (!SportTaskUtil.getNormalRange(typeId, lastSpeed, recLen)) {
                maxSpeedNum++;
            } else {
                maxSpeedNum = 1;
            }
        } else {
            maxSpeedNum = 0;
        }
        if (maxSpeedNum > SPEEDLIMITNUM) {
            speedOverLimit = true;
        }
        // }
        String speedStr = SportTaskUtil.getDoubleOneNum(speed);
        speedStringValue = speedStr;
        sporting_sport_pingjunSpeed.setText(speedStringValue + "km/h");
        if (speedValue != null) {
            speedValue.setText(speedStr);
        }
        String paceStr = (int) (pace / 60) + "'" + (int) (pace % 60) + "\"";
        paceStringValue = paceStr;
        sporting_sport_peiPace.setText(paceStringValue + "/km");
        if (paceValue != null) {
            paceValue.setText(paceStr);
        }
    }

    private class GpsLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(Location location) {
            mCountNum = 0;
            lastCountNum = 10;
            updateSpeed(location);
        }

        @Override
        public void onProviderDisabled(String provider) {
        }

        @Override
        public void onProviderEnabled(String provider) {
            updateSpeed(locationManager.getLastKnownLocation(provider));
        }

        @Override
        public void onStatusChanged(String arg0, int arg1, Bundle arg2) {

        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK: {
                if (!lockisopen) {
                    if (canSave) {
                        Log.v(TAG, "KEYCODE_BACK onKeyDown");
                        canSave = false;
                        isStart = false;
                        Bundle bundle = new Bundle();
                        bundle.putString("title",
                                getString(R.string.confirm_exit_title));
                        bundle.putString("message",
                                getString(R.string.spors_confirm_stopandsave));
                        onCreateDialog(2, bundle, 0);
                        RemoveLockScreenMSG();
                        if (timingMgr != null) {
                            timingMgr.cancleRepeatTimingFiveMinutes();
                        }

                    } else {

                        finish();
                    }
                    return true;
                } else {
                    return true;
                }
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                RemoveLockScreenMSG();
                break;
            case MotionEvent.ACTION_UP:
                SendLockScreenMSG();
                break;

        }
        return super.onTouchEvent(event);
    }

    public void RemoveLockScreenMSG() {
        if (handler != null) {
            handler.removeMessages(UPDATE_LOCKSCREEN);
        }
    }

    public void SendLockScreenMSG() {
        if (isStart && !isPause && handler != null && !lockisopen) {
            handler.sendEmptyMessageDelayed(UPDATE_LOCKSCREEN, LOCKSCREEN_DELAY);
        }
    }

    // 点击锁定按钮锁定
    public void SendBtnLockScreenMSG() {
        if (isStart && !isPause && handler != null && !lockisopen) {
            handler.sendEmptyMessageDelayed(UPDATE_LOCKSCREEN, 0);
        }
    }

    private Handler mmHandler = new Handler() {

        public void handleMessage(Message msg) {

            Log.i(TAG, "handleMessage :  #### ");

            if (MSG_LOCK_SUCESS == msg.what) {

                WindowManager.LayoutParams attr = getWindow().getAttributes();
                attr.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
                getWindow().setAttributes(attr);
                getWindow().clearFlags(
                        WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

                foxSportSetting = getSharedPreferences("sports"
                        + mSportsApp.getSportUser().getUid(), 0);
                lockisopen = false;
                Editor editor = foxSportSetting.edit();
                editor.putBoolean("lockisopen", false);
                editor.commit();
                SendLockScreenMSG();
                lockscreen.setVisibility(View.GONE);// 锁屏成功时，结束我们的Activity界面
                // 测试
                // unfoldView.setVisibility(View.VISIBLE);
            }
        }
    };

    private int netError = 0;

    public class MyLocationListenner implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            if (mLoadDialog != null && mLoadDialog.isShowing()) {
                mLoadDialog.dismiss();
            }
            if (location == null) {
                return;
            }
            String latitude = String.valueOf(location.getLatitude());
            String longitude = String.valueOf(location.getLongitude());
            Log.i("location", "这里" + latitude + "---" + longitude);

            if (latitude == null || longitude == null || latitude.equals("")
                    || latitude.equals("0.0") || longitude.equals("")
                    || longitude.equals("0.0") || latitude.equals("4.9E-324")
                    || longitude.equals("4.9E-324")) {
                Log.i("location", "定位失败！");
                if (netError == 1) {
                    Toast.makeText(mContext, getString(R.string.location_fail),
                            Toast.LENGTH_SHORT).show();
                }
                netError++;
                return;
            }
            curLocData = new LatLng(location.getLatitude(),
                    location.getLongitude());
            if (!mSportsApp.isStartY) {
                locData = new LatLng(location.getLatitude(),
                        location.getLongitude());
                animateTo(locData);
                if (mMarkerStart == null) {
                    mMarkerStart = (Marker) aMap.addOverlay(mMarkerOpStart
                            .position(locData));
                } else {
                    mMarkerStart.setPosition(locData);
                }
            } else if (isStart && !isPause) {
//				if (LocationUpdate(location)) {
//					locData = new LatLng(location.getLatitude(),
//							location.getLongitude());
//				}
            }
        }
    }

    private void animateTo(LatLng point) {
        aMap.animateMapStatus(MapStatusUpdateFactory.newLatLngZoom(point, 18),
                1000);
    }

    // ///////////////////////////////////////////////
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

                    if ((locData != null) || (null != ss)) {
                        goStart();
                    } else {
                        if (gpsType > 0) {
                            Toast.makeText(mContext,
                                    getString(R.string.in_gps_locating),
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(mContext,
                                    getString(R.string.location_gps_weak),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
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

    BroadcastReceiver timeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.v(TAG, "onReceive");
            if (intent.getAction().equals(
                    "com.fox.exercise.pedometer.TimerReceiver.alarmclock")) {
            }
        }
    };

    @Override
    public void onTabReselected(Tab arg0, FragmentTransaction arg1) {
        if (arg0.getPosition() == 0 && sportype != 1) {
            if (canSave) {
                goStop();
            } else {
                Toast.makeText(mContext,
                        getString(R.string.spors_media_disable),
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onTabSelected(Tab arg0, FragmentTransaction arg1) {
        Log.i("", "lulu arg0 getpostion2" + arg0.getPosition() + "--"
                + sportype);
        if (arg0.getPosition() == 0) {

        } else if (arg0.getPosition() == 1 && sportype == 2) {
            if (isPauseForGPS) {
                Toast.makeText(mContext, getString(R.string.location_gps_weak),
                        Toast.LENGTH_SHORT).show();
            } else {
                sportype = 3;
                if (!isPause) {
                    vp = new VoicePrompt(getApplicationContext(), "female",
                            "pause", null);
                    vp.playVoice();
                    goPause();
                    isPauseForClick = true;
                }
            }
        } else if (arg0.getPosition() == 1 && sportype == 1) {
            Log.i("", "lulu access 刚进来");
            if (is_start_map) {
                if ((locData != null && gpsType > 0) || (null != ss)) {
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
                    sportype = 2;
                    is_start_map = false;
                } else {
                    getActionBar().removeAllTabs();
                    ActionBar bar = getActionBar();
                    bar.addTab(bar.newTab().setIcon(R.drawable.sk_stop)
                            .setTabListener(this));
                    bar.addTab(bar.newTab()
                            .setIcon(R.drawable.sk_start_or_stop)
                            .setTabListener(this));
                    if (gpsType > 0) {
                        Toast.makeText(mContext,
                                getString(R.string.in_gps_locating),
                                Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(mContext,
                                getString(R.string.location_gps_weak),
                                Toast.LENGTH_SHORT).show();
                    }
                }
            }
        } else if (arg0.getPosition() == 1 && sportype == 3) {
            if (isPauseForGPS) {
                Toast.makeText(mContext, getString(R.string.location_gps_weak),
                        Toast.LENGTH_SHORT).show();
            } else {
                sportype = 2;
                if (isPause) {
                    Log.i("", "lulu access 暂停3");
                    vp = new VoicePrompt(getApplicationContext(), "female",
                            "continue", null);
                    vp.playVoice();
                    goResume();
                    isPauseForClick = false;
                }
            }
        }
    }

    @Override
    public void onTabUnselected(Tab arg0, FragmentTransaction arg1) {

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }

    ;

    private void goStart() {
        if (findMethod) {
            getActionBar().removeAllTabs();
            ActionBar bar = getActionBar();
            bar.addTab(bar.newTab().setIcon(R.drawable.sk_stop)
                    .setTabListener(this));
            bar.addTab(bar.newTab().setIcon(R.drawable.sk_start)
                    .setTabListener(this));
        } else {
            stopBtn.setEnabled(true);
            startBtn.setVisibility(View.GONE);
            pauseBtn.setVisibility(View.VISIBLE);
        }
        // if (djs_zd) {
        // long[] pattern = { 100, 1000 };
        // djs_vibrator.vibrate(pattern, 1);
        // vp = new VoicePrompt(getApplicationContext(), "female", "start",
        // null);
        // vp.playVoice();
        // mHandlerDjs.sendEmptyMessageDelayed(4, 1000);
        // }
        preTime = FunctionStatic.onResume();
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_UI);
//		startSport();
        startService();
        isRun = true;
        isStart = true;
        mSportsApp.isStartY = true;
        canSave = true;

        if (!isOpen) {
            if (mWakeLock != null && !mWakeLock.isHeld())
                mWakeLock.acquire();
        }
        if (handler != null && !lockisopen) {
            handler.sendEmptyMessage(UPDATE_LOCKSCREEN);
        }
        if (timingMgr != null) {
            timingMgr.repeatTimingFiveMinutes();
        }
    }

    private void goStop() {
        canSave = false;
        isStart = false;
        Bundle bundle = new Bundle();
        bundle.putString("title", getString(R.string.confirm_exit_title));
        bundle.putString("message",
                getString(R.string.spors_confirm_stopandsave));
        onCreateDialog(2, bundle, 0);
        if (!isOpen) {
            if (mWakeLock != null && mWakeLock.isHeld())
                mWakeLock.release();
        }
        RemoveLockScreenMSG();
        if (timingMgr != null) {
            timingMgr.cancleRepeatTimingFiveMinutes();
        }
    }

    private void goResume() {
        if (findMethod) {
            getActionBar().removeAllTabs();
            ActionBar bar = getActionBar();
            bar.addTab(bar.newTab().setIcon(R.drawable.sk_stop)
                    .setTabListener(this));
            bar.addTab(bar.newTab().setIcon(R.drawable.sk_start)
                    .setTabListener(this));
        } else {
            pauseBtn.setText(getString(R.string.pauseBtn_text));
        }
        isPause = false;
        isStart = true;
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_UI);

        if (!isOpen) {
            if (mWakeLock != null && !mWakeLock.isHeld())
                mWakeLock.acquire();
        }
        SendLockScreenMSG();
        if (timingMgr != null) {
            timingMgr.repeatTimingFiveMinutes();
        }
        startTimeSeconds = System.currentTimeMillis();
        mTempCount = recLen;
    }

    private void goPause() {
        if (findMethod) {
            getActionBar().removeAllTabs();
            ActionBar bar = getActionBar();
            bar.addTab(bar.newTab().setIcon(R.drawable.sk_stop)
                    .setTabListener(this));
            bar.addTab(bar.newTab().setIcon(R.drawable.sk_start_or_stop)
                    .setTabListener(this));
        } else {
            pauseBtn.setText(getString(R.string.sports_resume));
        }
        sensorManager.unregisterListener(this);
        isPause = true;
        isStart = false;
        if (!isOpen) {
            if (mWakeLock != null && mWakeLock.isHeld())
                mWakeLock.release();
        }
        RemoveLockScreenMSG();
        if (timingMgr != null) {
            timingMgr.cancleRepeatTimingFiveMinutes();
        }
        processPause();
    }

    private boolean processResume = false;

    private void processPause() {
        int size = mGeoPoints.size();
        if (size > 0) {
            // 起始点暂停，清除起始点记录，重新记录
            if (size == 1) {
                mGeoPoints.clear();
                mDrawPoints.clear();
                return;
            }
            // 上一次暂停后没有运动轨迹，再暂停时不记录状态
            if (mGeoPoints.get(size - 1).latitude == SportTrajectoryUtil.INVALID_LATLNG
                    || mGeoPoints.get(size - 1).longitude == SportTrajectoryUtil.INVALID_LATLNG) {
                return;
            }
            // 继续运动后只获取到一个点，又进入暂停状态，把这个点清除
            else if (mGeoPoints.get(size - 2).latitude == SportTrajectoryUtil.INVALID_LATLNG
                    || mGeoPoints.get(size - 2).longitude == SportTrajectoryUtil.INVALID_LATLNG) {
                mGeoPoints.remove(size - 1);
                mDrawPoints.clear();
                processResume = true;
                return;
            }

            mGeoPoints.add(new LatLng(SportTrajectoryUtil.INVALID_LATLNG,
                    SportTrajectoryUtil.INVALID_LATLNG));
            MarkerOptions MarkerMiddle = new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
                            .decodeResource(getResources(),
                                    R.drawable.map_middle)));
            aMap.addOverlay(MarkerMiddle.position(mDrawPoints.get(mDrawPoints
                    .size() - 1)));
            if (mPolyline != null) {
                PolylineOptions lineOptions = new PolylineOptions().width(12)
                        .color(Color.argb(255, 242, 109, 27));
                lineOptions.points(mDrawPoints);
                aMap.addOverlay(lineOptions);
                mPolyline.remove();
                mPolyline = null;
            }
            mDrawPoints.clear();
            processResume = true;
        }
    }

    private int waitForResume = 5;
    private boolean isWaitForPause = false;
    public static int gpsType = 0;
    private int gpsTypeAdd, gpsTypeOtherAdd = 0;
    private long waitForPauseTime = 0;

    // 状态监听
    GpsStatus.Listener listener = new GpsStatus.Listener() {
        public void onGpsStatusChanged(int event) {
            switch (event) {
                // 第一次定位
                case GpsStatus.GPS_EVENT_FIRST_FIX:
                    Log.i(TAG, "第一次定位");
                    break;
                // 卫星状态改变
                case GpsStatus.GPS_EVENT_SATELLITE_STATUS:
                    // Log.i(TAG, "卫星状态改变");

                    // 获取当前GPS信号强度状态
                    getGPSStatus();
                    if (gpsType > 0) {
                        gpsTypeAdd = 0;
                        if (gpsTypeOtherAdd == 0) {
                            TextView textView = (TextView) LayoutInflater.from(
                                    mContext).inflate(R.layout.toast_layout, null);
                            textView.setText("GPS模式记录中");
                            Toast mToast = new Toast(mContext);
                            mToast.setView(textView);
                            mToast.setDuration(Toast.LENGTH_SHORT);
                            mToast.setGravity(Gravity.TOP, 30, 180);
                            mToast.show();
                        }
                        gpsTypeOtherAdd++;

                    } else {
                        gpsTypeOtherAdd = 0;
                        if (gpsTypeAdd == 0) {
                            if (typeId == SportsType.TYPE_WALK || typeId == SportsType.TYPE_RUN
                                    || typeId == SportsType.TYPE_CLIMBING) {
                                TextView textView = (TextView) LayoutInflater.from(
                                        mContext).inflate(R.layout.toast_layout, null);
                                textView.setText("GPS信号弱，转换为记步模式");
                                Toast mToast = new Toast(mContext);
                                mToast.setView(textView);
                                mToast.setDuration(Toast.LENGTH_SHORT);
                                mToast.setGravity(Gravity.TOP, 30, 180);
                                mToast.show();
                            }

                        }
                        gpsTypeAdd++;
                    }
                    if (gpsType == 0) {
                        imageGps.setVisibility(View.GONE);
                        if (typeId == SportsType.TYPE_WALK || typeId == SportsType.TYPE_RUN
                                || typeId == SportsType.TYPE_CLIMBING) {
                            imageview_jibu.setVisibility(View.VISIBLE);
                        } else {
                            imageview_jibu.setVisibility(View.GONE);
                        }
                    } else if (gpsType == 1) {
                        imageGps.setVisibility(View.VISIBLE);
                        imageview_jibu.setVisibility(View.GONE);
                        // imageGps.setImageResource(R.drawable.sportmap_gps_1);
                        imageGps.setImageResource(R.drawable.gps_g01);
                    } else if (gpsType == 2) {
                        imageGps.setVisibility(View.VISIBLE);
                        imageview_jibu.setVisibility(View.GONE);
                        // imageGps.setImageResource(R.drawable.sportmap_gps_2);
                        imageGps.setImageResource(R.drawable.gps_g02);
                    } else if (gpsType == 3) {
                        imageGps.setVisibility(View.VISIBLE);
                        imageview_jibu.setVisibility(View.GONE);
                        // imageGps.setImageResource(R.drawable.sportmap_gps_3);
                        imageGps.setImageResource(R.drawable.gps_g03);
                    } else {
                        imageGps.setVisibility(View.VISIBLE);
                        imageview_jibu.setVisibility(View.GONE);
                        // imageGps.setImageResource(R.drawable.sportmap_gps_4);
                        imageGps.setImageResource(R.drawable.gps_g04);
                    }

                    // if (gpsType == 0) {
                    // if (isStart && !isPause && !isPauseForGPS
                    // && !isPauseForClick) {
                    // if (isWaitForPause) {
                    // int rec = (int) ((System.currentTimeMillis() -
                    // waitForPauseTime) / 1000L);
                    // // GPS信号丢失30秒后暂停
                    // if (rec > 30) {
                    // vp = new VoicePrompt(getApplicationContext(),
                    // "female", "loseGPS", null);
                    // vp.playVoice();
                    // goPause();
                    // waitForResume = 5;
                    // isPauseForGPS = true;
                    // isWaitForPause = false;
                    // }
                    // } else {
                    // waitForPauseTime = System.currentTimeMillis();
                    // isWaitForPause = true;
                    // }
                    // }
                    // } else {
                    // if (isWaitForPause)
                    // isWaitForPause = false;
                    // if (!isStart && isPause && isPauseForGPS
                    // && !isPauseForClick) {
                    // if (waitForResume == 0) {
                    // vp = new VoicePrompt(getApplicationContext(),
                    // "female", "obtainGPS", null);
                    // vp.playVoice();
                    // goResume();
                    // isPauseForGPS = false;
                    // waitForResume = 5;
                    // } else {
                    // waitForResume--;
                    // }
                    // }
                    // }
                    break;
                // 定位启动
                case GpsStatus.GPS_EVENT_STARTED:
                    Log.i(TAG, "定位启动");
                    // isWaitForPause = false;
                    break;
                // 定位结束
                case GpsStatus.GPS_EVENT_STOPPED:
                    Log.i(TAG, "定位结束");
                    Toast.makeText(mContext, "定位结束", Toast.LENGTH_SHORT).show();
                    imageGps.setImageResource(R.drawable.gps_g01);

                    // if (isStart && !isPause && !isPauseForGPS &&
                    // !isPauseForClick) {
                    // isWaitForPause = true;
                    // new Thread() {
                    // @Override
                    // public void run() {
                    // long starttime = System.currentTimeMillis();
                    // while (isWaitForPause) {
                    // int rec = (int) ((System.currentTimeMillis() - starttime) /
                    // 1000L);
                    // // GPS信号丢失30秒后暂停
                    // if (rec > 30) {
                    // Message message = new Message();
                    // message.what = GO_PAUSE;
                    // handler.sendMessage(message);
                    // isPauseForGPS = true;
                    // isWaitForPause = false;
                    // }
                    // }
                    // }
                    // }.start();
                    // }
                    break;
            }
        }

        ;
    };

    private void getGPSStatus() {
        GpsStatus gpsStatus = locationManager.getGpsStatus(null);
        float f1 = 0;
        float f2 = 0;

        // 获取卫星颗数的默认最大值
        int maxSatellites = gpsStatus.getMaxSatellites();
        // 创建一个迭代器保存所有卫星
        Iterator<GpsSatellite> iters = gpsStatus.getSatellites().iterator();
        int count = 0;
        while (iters.hasNext() && count <= maxSatellites) {
            GpsSatellite s = iters.next();
            if (s.getSnr() > 18.0f) {
                f1 += s.getSnr();
                count++;
            }
        }
        f2 = f1 / count;
        gpsType = 0;
        if ((count > 8) && (f2 > 28.0F)) {
            gpsType = 4;
        } else if ((count > 6) && (f2 > 25.0F)) {
            gpsType = 3;
        } else if ((count > 5) && (f2 > 22.0F)) {
            gpsType = 2;
        } else if ((count > 3) && (f2 > 20.0F)) {
            gpsType = 1;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        // TODO Auto-generated method stub
        if (Sensor.TYPE_ACCELEROMETER == event.sensor.getType()) {
            CountStep(event.values[0], event.values[1], event.values[2]);
        }
    }

    private void CountStep(float x, float y, float z) {
        accelerometer = (float) Math.sqrt(x * x + y * y + z * z);
        if (accelerometer < 8) {
            mTroughAppear = true;
        }

        if (accelerometer > 9.8) {
            if (mTroughAppear) {
                if (System.currentTimeMillis() - mPrevStepTime > 200) {
                    mCountNum++;
                    if (mCountNum > 10) {
                        isStepBegin = true;

                        dis = dis + (6.0 * 1.05 / 10000);
                        disStringValue = SportTaskUtil.getDoubleNum(dis);
                        time_disValue.setText(disStringValue);
                        zong_li_txt.setText(disStringValue);
                        if (disValue != null) {
                            disValue.setText(disStringValue);
                        }

                        if (typeId == SportsType.TYPE_WALK) {
                            con = (int) WalkingUtils.getCalories(dis);
                        } else if (typeId == SportsType.TYPE_RUN) {
                            con = (int) RunningUtils.getCalories(dis);
                        } else if (typeId == SportsType.TYPE_CLIMBING) {
                            con = (int) MountainUtils.getCalories(dis);
                        } else if (typeId == SportsType.TYPE_GOLF) {
                            con = (int) GolfingUtils.getCalories(dis);
                        } else if (typeId == SportsType.TYPE_WALKRACE) {
                            con = (int) WalkingRaceUtils.getCalories(dis);
                        } else if (typeId == SportsType.TYPE_CYCLING) {
                            con = (int) RidingUtils.getCalories(dis);
                        } else if (typeId == SportsType.TYPE_FOOTBALL) {
                            con = (int) FootballUtils.getCalories(dis);
                        } else if (typeId == SportsType.TYPE_ROWING) {
                            con = (int) RowingUtils.getCalories(dis);
                        } else if (typeId == SportsType.TYPE_SWIM) {
                            con = (int) SwimmingUtils.getCalories(disVal,
                                    recLen, typeDetailId + 1);
                        }
                        conStringValue = con + "";
                        sporting_sportxiaohao.setText(conStringValue + "Cal");
                        if (conValue != null) {
                            conValue.setText(con + "");
                        }
                    } else {
                        isStepBegin = false;
                    }
                    Log.e(TAG, "mCountNum : " + mCountNum);
                    mPrevStepTime = System.currentTimeMillis();
                    mTroughAppear = false;
                }
            }
        }
    }

    private void updateStepInformation() {
        // TODO Auto-generated method stub
        if (lastCountNum <= mCountNum) {
            speed = (mCountNum - lastCountNum) * 2.16f;
            String speedStr = SportTaskUtil.getDoubleOneNum(speed);
            speedStringValue = speedStr;
            sporting_sport_pingjunSpeed.setText(speedStringValue + "km/h");
            if (speedValue != null) {
                speedValue.setText(speedStr);
            }

            if (lastCountNum < mCountNum) {
                pace = 3600;
            } else {
                pace = 0;
            }
            String paceStr = (int) (pace / 60) + "'" + (int) (pace % 60) + "\"";
            paceStringValue = paceStr;
            sporting_sport_peiPace.setText(paceStringValue + "/km");
            if (paceValue != null) {
                paceValue.setText(paceStr);
            }

            lastCountNum = mCountNum;
        }
    }

    private void showSportWindow() {
        // if (mSportWindow == null) {
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.sportsing_data_poplayout,
                null);
        RoundedImage sports_user_icon = (RoundedImage) view
                .findViewById(R.id.sports_user_icon);
        if (SportsApp.getInstance().getSportUser().getSex().equals("man")) {
            sports_user_icon.setImageResource(R.drawable.sports_user_edit_portrait_male);
        } else {
            sports_user_icon
                    .setImageResource(R.drawable.sports_user_edit_portrait);
        }
        mImageWorker_Icon.loadImage(SportsApp.getInstance().getSportUser()
                .getUimg(), sports_user_icon, null, null, false);
//        TextView weather_data = (TextView) view.findViewById(R.id.weather_data);
        TextView area_data = (TextView) view.findViewById(R.id.area_data);
        area_data.setText(SportsApp.getInstance().getSportUser().getProvince());
        TextView sports_level = (TextView) view.findViewById(R.id.sports_level);
        ImageButton pbigBtn = (ImageButton) view.findViewById(R.id.bigBtn);
        pop_chronometerId = (TextView) view
                .findViewById(R.id.pop_chronometerId);
        disValue = (TextView) view.findViewById(R.id.disValue);
        if (disStringValue != null && !"".equals(disStringValue)) {
            disValue.setText(disStringValue);
        }
        conValue = (TextView) view.findViewById(R.id.conValue);
        if (conStringValue != null && !"".equals(conStringValue)) {
            conValue.setText(conStringValue);
        }
        speedValue = (TextView) view.findViewById(R.id.speedValue);
        if (speedStringValue != null && !"".equals(speedStringValue)) {
            speedValue.setText(speedStringValue);
        }
        paceValue = (TextView) view.findViewById(R.id.paceValue);
        if (paceStringValue != null && !"".equals(paceStringValue)) {
            paceValue.setText(paceStringValue);
        }
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        int popWith = width * 2 / 3;
        int height = wm.getDefaultDisplay().getHeight();
        mSportWindow = new PopupWindow(view, popWith, LayoutParams.WRAP_CONTENT);
        pbigBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                mSportWindow.dismiss();
                unfoldBtn.setVisibility(View.VISIBLE);
                pop_chronometerId = null;
            }
        });

        // }
        mSportWindow.setTouchable(true);
        // mSportWindow.setOutsideTouchable(true);
        mSportWindow.setTouchInterceptor(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                Log.i("mengdd", "onTouch : ");

                return false;
                // 这里如果返回true的话，touch事件将被拦截
                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
            }
        });
        mSportWindow.setBackgroundDrawable(new BitmapDrawable());
        mSportWindow.showAsDropDown(timeLayout, 1, 0);

    }

    public void startStepService() {
        Log.i(TAG, "[SERVICE] Start");
        startService(new Intent(this, FirstStepService.class));
        // Editor edit = mSettings.edit();
        Editor edit = msharedPreferences.edit();
        edit.putBoolean("mservice_running", true);
        edit.commit();
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
