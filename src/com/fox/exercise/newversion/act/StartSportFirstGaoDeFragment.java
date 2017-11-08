package com.fox.exercise.newversion.act;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMap.OnMapLoadedListener;
import com.amap.api.maps.AMap.OnMarkerClickListener;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.fox.exercise.AbstractBaseOtherFragment;
import com.fox.exercise.HistoryAllActivity;
import com.fox.exercise.MainFragmentActivity;
import com.fox.exercise.R;
import com.fox.exercise.SportsType;
import com.fox.exercise.SportsUtilities;
import com.fox.exercise.login.LocationInfo;
import com.fox.exercise.map.SportingMapActivityGaode;
import com.fox.exercise.map.VoicePrompt;
import com.fox.exercise.pedometer.TimingManager;
import com.umeng.analytics.MobclickAgent;

import java.util.Calendar;
import java.util.Iterator;
import java.util.TimeZone;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cn.ingenic.indroidsync.SportsApp;

/**
 * @author loujungang 开始运动首页面
 */
public class StartSportFirstGaoDeFragment extends AbstractBaseOtherFragment
        implements OnClickListener, LocationSource, AMapLocationListener {
    private MapView mMapView = null;
    private UiSettings mUiSettings;
    private AMap aMap;
    private AMapLocationClient mapLocationClient;
    private AMapLocationClientOption mapLocationClientOption;
    private Dialog mLoadDialog = null;
    private Context mContext;
    private Dialog alertDialog;
    private SportsApp mSportsApp;
    private Dialog mUploadDialog = null;
    private static final int UPDATE_STEP = 1;
    private static final int UPDATE_LOCKSCREEN = 2;
    private static final int GO_PAUSE = 3;
    private LocationManager locationManager = null;
    private float zoomValue = 19;
    private TextView startsports_btn;// 开始运动按钮
    private ImageView startsports_type_icon;// 选中哪个运动类项目
    private PopupWindow mSportWindow = null;
    private int[] mSportTypeImages;
    private String[] mSportTypeNames;
    private RelativeLayout timeLayout;
    private int mSelectTypeID;
    private String deviceName;
    private Boolean isLocal;
    private int type = -1;
    private int typeId = 0;
    private int typeDetailId = 0;

    private TextView sport_week, sport_area, sport_weather_degree, sport_pm,
            weather_txt, gps_txt, set_txt;
    private ImageView weather_icon;

    private SharedPreferences spf;
    private ExecutorService cachedThreadExecutor;
    private String mCitycode = null;
    public static final int NO_PM = 201312123;
    public static final int WEATHER_ICON = 20131216;

    private ImageView imageview_gps;

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
    // 语音提示
    private VoicePrompt vp;

    private LatLng locData = null;
    // private List<LatLng> mGeoPoints = new ArrayList<LatLng>();
    // private List<LatLng> mDrawPoints = new ArrayList<LatLng>();
    public double mLat = 0;
    public double mLng = 0;
    private static final int DRAWTRAJECTORY = 1;

    private TextView gps_tishi_tx;
    private View view;

    private TextView sport_type_txt;

    private LinearLayout start_sport_layout;
    private LinearLayout sport_type_txtlayout;
    private String cityname;

    private MainFragmentActivity mainFragmentActivity;
    private Handler pHandler = new Handler();

    Runnable r = new Runnable() {

        @Override
        public void run() {
            if (null != getActivity().getWindow().getDecorView()
                    .getWindowToken()) {
//				showSportWindow();
                pHandler.removeCallbacks(this);
            } else {
                pHandler.postDelayed(this, 5);
            }
        }
    };

    @Override
    public void beforeInitView(Bundle bundles) {
        // TODO Auto-generated method stub
        title = getActivity().getResources().getString(
                R.string.start_sports_msg);
    }

    @Override
    public void setViewStatus(Bundle bundles) {
        // TODO Auto-generated method stub
        // setContentView(R.layout.startsporting_map_gaode);
        mContext = getActivity();
        view = LayoutInflater.from(getActivity()).inflate(
                R.layout.startsporting_map_gaode, null);
        setContentViews(view);
        mSportsApp = (SportsApp) getActivity().getApplication();
        mainFragmentActivity = (MainFragmentActivity) getActivity();
        if ((mSportsApp.getSportUser() == null
                || mSportsApp.getSportUser().equals("") || mSportsApp
                .getSportUser().getUid() == 0) && mSportsApp.LoginOption) {
            Log.v(TAG, "onActivityCreated         no doing");

        } else {
            cachedThreadExecutor = Executors.newCachedThreadPool();
            showRightLayout();
            init();
//			initGaodeWeather();
            // initPM();
            initGPS();
            // initWeather();
            pHandler.postDelayed(r, 100);
        }

        // 打开GPS
        // if (isOPen(mContext)) {
        // toggleGPS(mContext, true);
        // }
        mMapView = (MapView) getActivity().findViewById(R.id.bmapView);
        mMapView.onCreate(bundles);
        if (aMap == null) {
            aMap = mMapView.getMap();
            mUiSettings = aMap.getUiSettings();
            mUiSettings.setZoomControlsEnabled(false);
            mUiSettings.setZoomGesturesEnabled(true);
            mUiSettings.setMyLocationButtonEnabled(false); // 是否显示默认的定位按钮
            aMap.setLocationSource(this);// 设置定位监听
            aMap.setMyLocationEnabled(true);// 是否可触发定位并显示定位层
            aMap.setOnMapLoadedListener(new OnMapLoadedListener() {
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
            });// 设置点击marker事件监听器
        }

        Bundle bundle = new Bundle();
        bundle.putString("message",
                getResources().getString(R.string.sports_wait));
        mLoadDialog = onCreateDialog(1, bundle, 0);
        mLoadDialog.show();
        setView();

    }

    @Override
    public void onPageResume() {
        MobclickAgent.onPageStart("StartSportFirstGaoDeFragment");
        // TODO Auto-generated method stub
        if (mMapView != null) {
            mMapView.onResume();
        }
        if (mainFragmentActivity == null) {
            mainFragmentActivity = (MainFragmentActivity) getActivity();
        }
    }

    @Override
    public void onPagePause() {
        MobclickAgent.onPageEnd("StartSportFirstGaoDeFragment");
        // TODO Auto-generated method stub
        if (mMapView != null) {
            mMapView.onPause();
        }
    }

    @Override
    public void onPageDestroy() {
        // TODO Auto-generated method stub
        if (aMap != null) {
            aMap.setLocationSource(null);
            aMap.setMyLocationEnabled(false);
            aMap.clear();
            aMap = null;
        }
        if (mMapView != null) {
            mMapView.onDestroy();
            mMapView = null;
        }
        if (locationManager != null) {
            locationManager.removeUpdates((LocationListener) this);
        }
        if (mapLocationClient != null) {
            mapLocationClient.onDestroy();
        }
        if (mSportWindow != null) {
            if (mSportWindow.isShowing())
                mSportWindow.dismiss();
            mSportWindow = null;
        }

    }

    @Override
    public void activate(OnLocationChangedListener arg0) {
        // TODO Auto-generated method stub
        if (mapLocationClient == null) {
            mapLocationClient = new AMapLocationClient(getActivity());
            mapLocationClientOption = new AMapLocationClientOption();
            // 设置定位监听
            mapLocationClient.setLocationListener(this);
            // 设置为高精度定位模式
            mapLocationClientOption
                    .setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            // 设置定位参数
            mapLocationClient.setLocationOption(mapLocationClientOption);
            // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
            // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
            // 在定位结束后，在合适的生命周期调用onDestroy()方法
            // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
            mapLocationClient.startLocation();
        }

    }

    @Override
    public void deactivate() {
        // TODO Auto-generated method stub
        if (mapLocationClient != null) {
            mapLocationClient.stopLocation();
            mapLocationClient.onDestroy();
        }
        mapLocationClient = null;
    }

    @Override
    public void onClick(View view) {
        // TODO Auto-generated method stub
        switch (view.getId()) {
            case R.id.sport_type_txt:
            case R.id.sport_type_txtlayout:
                break;
            case R.id.set_txt:
                Intent intent = new Intent(getActivity(),
                        FoxSportsSettingsActivity.class);
                getActivity().startActivity(intent);
                break;
            case R.id.startsports_btn:
            case R.id.start_sport_layout:
                if (isOPen(mContext)) {
                    if ((gpsType >= 0)) {
                        saveAndExit();
                    } else {
                        Toast.makeText(mContext,
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

            default:
                break;
        }

    }

    public Dialog onCreateDialog(int id, Bundle bundle, final int taskid) {
        String message = bundle.getString("message");
        switch (id) {
            case 1:
                Dialog loginPregressDialog = new Dialog(mContext,
                        R.style.sports_dialog);
                LayoutInflater mInflater = getActivity().getLayoutInflater();
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
        }
        return null;
    }


    private void setView() {
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

    }

    private void animateTo(LatLng point) {
        if (aMap != null) {
            aMap.animateCamera(
                    CameraUpdateFactory.newLatLngZoom(point, zoomValue), null);
        }
    }

    private void showRightLayout() {
        View rightView = getActivity().getLayoutInflater().inflate(
                R.layout.startsport_rightbtn_layout, null);
        TextView startsport_record_tx = (TextView) rightView
                .findViewById(R.id.startsport_record_tx);
        startsport_record_tx.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                Intent kintent = new Intent(getActivity(),
                        HistoryAllActivity.class);
                Bundle kbundle = new Bundle();
                kbundle.putInt("ID", SportsApp.getInstance().getSportUser()
                        .getUid());
                kintent.putExtras(kbundle);
                startActivity(kintent);
            }
        });
        showRightBtn(rightView);
    }

    private void init() {
        SharedPreferences sharedPreferences = getActivity()
                .getSharedPreferences(
                        "sports" + mSportsApp.getSportUser().getUid(), 0);
        mSelectTypeID = sharedPreferences.getInt("type", 0);
        if (mSelectTypeID == 0) {
            typeId = 0;
        } else if (mSelectTypeID == 1) {
            typeId = 1;
        } else if (mSelectTypeID == 2) {
            typeId = 6;
        } else if (mSelectTypeID == 3) {
            typeId = 3;
        } else if (mSelectTypeID == 4) {
            typeId = 7;
        } else if (mSelectTypeID == 5) {
            typeId = 8;
        } else if (mSelectTypeID == 6) {
            typeId = 4;
        }
        type = mSelectTypeID;
        deviceName = sharedPreferences.getString("deviceName", "");
        isLocal = sharedPreferences.getBoolean("isLocal", false);
        spf = getActivity().getSharedPreferences("sports", 0);

        startsports_btn = (TextView) view.findViewById(R.id.startsports_btn);
        sport_week = (TextView) view.findViewById(R.id.sport_week);
        sport_area = (TextView) view.findViewById(R.id.sport_area);
        sport_pm = (TextView) view.findViewById(R.id.sport_pm);
        gps_txt = (TextView) view.findViewById(R.id.gps_txt);
//        sport_weather_degree = (TextView) view
//                .findViewById(R.id.weather_txt_num);
//        weather_txt = (TextView) view.findViewById(R.id.weather_txt);
        startsports_type_icon = (ImageView) view
                .findViewById(R.id.startsports_type_icon);
//        weather_icon = (ImageView) view.findViewById(R.id.weather_icon);
        imageview_gps = (ImageView) view.findViewById(R.id.imageview_gps);
        // 倒计时
        djs_vibrator = (Vibrator) getActivity().getSystemService(
                Context.VIBRATOR_SERVICE);
        timeLayout = (RelativeLayout) view.findViewById(R.id.timeLayout);
        djs_layout = (LinearLayout) view.findViewById(R.id.djs_layout);
        djs_image = (ImageView) view.findViewById(R.id.djs_image);
        djs_animation = AnimationUtils.loadAnimation(getActivity(),
                R.anim.animate);
        djs_sp = getActivity().getSharedPreferences("save_djs", 0);
        djs_count = djs_sp.getInt("djs_time", 3);
        djs_zd = djs_sp.getBoolean("djs_remind", true);
        djs_counts = djs_count;
        timingMgr = TimingManager.getInstance(mContext);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter
                .addAction("com.fox.exercise.pedometer.TimerReceiver.alarmclock");
        getActivity().registerReceiver(timeReceiver, intentFilter);

        sport_week.setText(getWeek());
        sport_area.setText(getActivity().getSharedPreferences("sports", 0)
                .getString("cityname", ""));
        startsports_type_icon.setOnClickListener(this);
        startsports_btn.setOnClickListener(this);
        mSportTypeImages = new int[7];
        for (int i = 0; i < 7; i++) {
            mSportTypeImages[i] = SportsUtilities.getNewStateImgIdsTitle(i);
        }
        mSportTypeNames = this.getResources().getStringArray(
                R.array.name_newsports_type);
        // startsports_type_icon
        // .setBackgroundResource(mSportTypeImages[mSelectTypeID]);
        gps_tishi_tx = (TextView) view.findViewById(R.id.gps_content_txt);

        sport_type_txt = (TextView) view.findViewById(R.id.sport_type_txt);

        if (mSelectTypeID >= 0 && mSelectTypeID <= 6) {
            sport_type_txt.setText(mSportTypeNames[mSelectTypeID]);
        } else {
            sport_type_txt.setText(mSportTypeNames[0]);
        }
        set_txt = (TextView) view.findViewById(R.id.set_txt);
        start_sport_layout = (LinearLayout) view
                .findViewById(R.id.start_sport_layout);
        sport_type_txtlayout = (LinearLayout) view
                .findViewById(R.id.sport_type_txtlayout);
        sport_type_txt.setOnClickListener(this);
        sport_type_txtlayout.setOnClickListener(this);
        start_sport_layout.setOnClickListener(this);
        set_txt.setOnClickListener(this);
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

            mitem_text.setText(mSportTypeNames[arg0]);
            mitem_image.setVisibility(View.GONE);
            return myView;
        }
    }

    private class MyItemClick implements OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                long arg3) {
            // TODO Auto-generated method stub
            sport_type_txt.setText(mSportTypeNames[arg2]);
            if (mSportWindow != null) {
                mSportWindow.dismiss();
                mSportWindow.setFocusable(false);
            }
            switch (arg2) {
                case 0:
                    type = 0;
                    typeId = 0;
                    typeDetailId = 0;
                    break;
                case 1:
                    type = 1;
                    typeId = 1;
                    typeDetailId = 0;
                    break;
                case 2:
                    type = 2;
                    typeId = 6;
                    typeDetailId = 0;
                    break;
                case 3:
                    type = 3;
                    typeId = 3;
                    typeDetailId = 0;
                    break;
                case 4:
                    type = 4;
                    typeId = 7;
                    typeDetailId = 0;
                    break;
                case 5:
                    type = 5;
                    typeId = 8;
                    typeDetailId = 0;
                    break;
                case 6:
                    type = 6;
                    typeId = 4;
                    typeDetailId = 0;
                    break;
            }

            mSelectTypeID = type;
        }

    }

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

    private void goStart() {
        djs_zd = djs_sp.getBoolean("djs_remind", true);
        if (djs_zd) {
            long[] pattern = {100, 1000};
            djs_vibrator.vibrate(pattern, 1);
            vp = new VoicePrompt(mContext, "female", "start", null);
            vp.playVoice();
            mHandlerDjs.sendEmptyMessageDelayed(4, 1000);
        }
        SharedPreferences sharedPreferences = getActivity()
                .getSharedPreferences(
                        "sports" + mSportsApp.getSportUser().getUid(), 0);
        Editor editor = sharedPreferences.edit();
        editor.putInt("type", type);
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

        if (typeId == SportsType.TYPE_WALK || typeId == SportsType.TYPE_RUN
                || typeId == SportsType.TYPE_CLIMBING) {
            // 记步模式
            // mainFragmentActivity.unbindStepService();
            // mainFragmentActivity.stopStepService();
        } else {
            // 不需要记步模式
            // SharedPreferences msharedPreferences = getActivity()
            // .getSharedPreferences(
            // "sports" + mSportsApp.getSportUser().getUid(), 0);
            // mContext.stopService(new Intent(getActivity(),
            // FirstStepService.class));
            // Editor edit = msharedPreferences.edit();
            // edit.putBoolean("mservice_running", false);
            // edit.commit();
        }
        getActivity().startActivity(intent);
    }

    private void onCreateDialog() {
        if (alertDialog != null && alertDialog.isShowing())
            return;
        String message = getResources().getString(R.string.fox_swim_protect);
        alertDialog = new Dialog(mContext, R.style.sports_dialog);
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

    private String getWeek() {
        // TODO Auto-generated method stub
        final Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        String mWay = String.valueOf(c.get(Calendar.DAY_OF_WEEK));
        if ("1".equals(mWay)) {
            mWay = getActivity().getResources().getString(R.string.sundy);
        } else if ("2".equals(mWay)) {
            mWay = getActivity().getResources().getString(R.string.monday);
        } else if ("3".equals(mWay)) {
            mWay = getActivity().getResources().getString(R.string.tuesday);
        } else if ("4".equals(mWay)) {
            mWay = getActivity().getResources().getString(R.string.wednesday);
        } else if ("5".equals(mWay)) {
            mWay = getActivity().getResources().getString(R.string.thursday);
        } else if ("6".equals(mWay)) {
            mWay = getActivity().getResources().getString(R.string.friday);
        } else if ("7".equals(mWay)) {
            mWay = getActivity().getResources().getString(R.string.saturday);
        }
        return mWay;
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

                default:
                    super.handleMessage(msg);
            }
        }
    };

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

                    if (gpsType == 0) {
                        gps_txt.setText("GPS 弱");
                        imageview_gps.setImageResource(R.drawable.gps_g01);
                    } else if (gpsType == 1) {
                        gps_txt.setText("GPS 弱");
                        imageview_gps.setImageResource(R.drawable.gps_g02);
                    } else if (gpsType == 2) {
                        gps_txt.setText("GPS 中");
                        imageview_gps.setImageResource(R.drawable.gps_g03);
                    } else if (gpsType == 3) {
                        gps_txt.setText("GPS 强");
                        imageview_gps.setImageResource(R.drawable.gps_g04);
                    } else {
                        gps_txt.setText("GPS 强");
                        imageview_gps.setImageResource(R.drawable.gps_g05);
                    }
                    if (gpsType < 0) {
                        gps_tishi_tx.setVisibility(View.VISIBLE);
                    } else {
                        gps_tishi_tx.setVisibility(View.GONE);
                    }
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
                    // imageview_gps.setImageResource(R.drawable.sportmap_gps_0);
                    break;
            }
        }

        ;
    };

    public int gpsType = 0;

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

    private void initGPS() {
        // 获取系统LocationManager服务
        locationManager = (LocationManager) mContext
                .getSystemService(Context.LOCATION_SERVICE);

        locationManager.addGpsStatusListener(listener);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                1000, 1, (LocationListener) this);
    }

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

    BroadcastReceiver timeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.v(TAG, "onReceive");
            if (intent.getAction().equals(
                    "com.fox.exercise.pedometer.TimerReceiver.alarmclock")) {
            }
        }
    };

    private int netError = 0;

    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (mLoadDialog != null && mLoadDialog.isShowing()) {
            mLoadDialog.dismiss();
        }
        if (amapLocation == null) {
            return;
        }
        String latitude = String.valueOf(amapLocation.getLatitude());
        String longitude = String.valueOf(amapLocation.getLongitude());
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
        if (!mSportsApp.isStartY) {
            locData = new LatLng(amapLocation.getLatitude(),
                    amapLocation.getLongitude());
            animateTo(locData);
        } else {
            if (LocationUpdate(amapLocation)) {

                locData = new LatLng(amapLocation.getLatitude(),
                        amapLocation.getLongitude());
            }
        }
    }

    private boolean LocationUpdate(AMapLocation location) {
        if (location == null)
            return false;
        mLat = location.getLatitude();
        mLng = location.getLongitude();
        return true;

    }


    /**
     * 判断GPS是否开启，GPS或者AGPS开启一个就认为是开启的
     *
     * @param context
     * @return true 表示开启
     */
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

    protected Dialog onCreateDialogs(int id, Bundle args) {
        switch (id) {
            case 2:
                String title = args.getString("title");
                String message = args.getString("message");
                AlertDialog.Builder builder = new Builder(mContext);
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



}
