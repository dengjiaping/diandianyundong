package com.fox.exercise.map;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.maps.AMap;
import com.amap.api.maps.AMap.OnMapLoadedListener;
import com.amap.api.maps.AMap.OnMapScreenShotListener;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.GroundOverlay;
import com.amap.api.maps.model.GroundOverlayOptions;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.PolylineOptions;
import com.fox.exercise.FindFriendsSendMsg;
import com.fox.exercise.ImageDownloader;
import com.fox.exercise.R;
import com.fox.exercise.SmartBarUtils;
import com.fox.exercise.SportsType;
import com.fox.exercise.SportsUtilities;
import com.fox.exercise.StateActivity;
import com.fox.exercise.api.AddCoinsThread;
import com.fox.exercise.api.ApiBack;
import com.fox.exercise.api.ApiConstant;
import com.fox.exercise.api.ApiJsonParser;
import com.fox.exercise.api.ApiNetException;
import com.fox.exercise.api.ApiSessionOutException;
import com.fox.exercise.api.QQHealthTask.QQHealthResult;
import com.fox.exercise.api.Util;
import com.fox.exercise.api.entity.GetPeisu;
import com.fox.exercise.api.entity.PeisuInfo;
import com.fox.exercise.api.entity.SportMediaFile;
import com.fox.exercise.api.entity.SportTask;
import com.fox.exercise.db.PeisuDB;
import com.fox.exercise.db.SportSubTaskDB;
import com.fox.exercise.http.FunctionStatic;
import com.fox.exercise.login.Tools;
import com.fox.exercise.newversion.act.MyFirstSportFragment;
import com.fox.exercise.newversion.entity.SportsMarkDis;
import com.fox.exercise.newversion.view.LineGraphicView;
import com.fox.exercise.publish.Bimp;
import com.fox.exercise.util.SportTaskUtil;
import com.fox.exercise.util.SportTrajectoryUtilGaode;
import com.fox.exercise.wxapi.WXEntryActivity;
import com.tencent.connect.share.QQShare;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXImageObject;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.umeng.analytics.MobclickAgent;

import java.lang.ref.WeakReference;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import cn.ingenic.indroidsync.SportsApp;
import cn.ingenic.indroidsync.utils.DeviceUuidFactory;

/**
 * @author loujungang 高德地图运动历史详情页面
 */
public class SportTaskDetailActivityGaode extends SWeiboBaseActivity implements
        OnClickListener, OnMapScreenShotListener, IUiListener , PopupWindow.OnDismissListener{
    private AddCoinsHandler addCoinsHandler = new AddCoinsHandler();
    private int coins; // 要上传的金币数
    // 上传成功和更新成功后返回的参数头
    private ApiBack theApiBack = null;
    private int thereturn;
    private MapView mMapView = null;
    private AMap aMap;
    private UiSettings mUiSettings;
    public static final int CLICK_SPORT_IMG = 0;

    private static final int DRAWTRAJECTORY = 1;
    private List<LatLng> mGeoPoints = new ArrayList<LatLng>();

    private IWXAPI api;
    private static final int THUMB_SIZE = 150;

    private ImageButton backBtn, sport_map_finish;

    private TextView disValue, conValue, speedValue, chronometerId;// ,heartValue,timeValue,
    // stepValue,

    private String sessionId;
    private String startTime;
    private String startTimeFrom;
    private int taskID, recLen, uid, temp_taskId;
    private int typeId, deviceId;
    private int typeDetailId = 0;

    private double dis, con, speed, heart;
    private int step;
    private int isUpload;
    private int mapType;

    private String mark_code;// 运动记录唯一标示
    private String pointsStr;

    private static final int UPLOAD_REQUEST_FAIL = 10001;
    private static final int UPLOAD_START = 10002;
    private static final int UPLOAD_REQUEST = 10003;
    private static final int UPLOAD_FAIL = 10004;
    private static final int DELETE_FILE = 10005;
    private static final int UPLOAD_DELETE = 10006;
    private static final int UPLOAD_UPDATE = 10007;
    private static final int UPLOAD_QQ_HEALTH = 10008;
    private Activity mContext;

    private SportSubTaskDB db;
    public static final String SHARE_PATH = SportsUtilities.DOWNLOAD_SAVE_PATH
            + "shareImage.jpg";
    private static final int SHARE_XINLANG = 1;
    protected static final int SHARE_TENGXUN = 2;
    protected static final int SHARE_WEIXIN = 3;
    protected static final int SHARE_FAXIAN = 4;
    protected static final int SHAREPEI_SU = 5;

    private int curUid;
    private ImageButton uploadBtn;
    private RelativeLayout header_layout;
//    private RelativeLayout shareLayout;
    //    private RelativeLayout timeLayout;
//    private ImageView mLine1;
//    private ImageView mLine2;
//    private RelativeLayout sportStateLayout;
//    private LinearLayout shareLinearLayout;
//    private ImageButton unfoldBtn;
    private boolean isGone = false;
    private Dialog mDialog;
    public static ArrayList<SportMediaFile> mediaFilesList;
    private int fromID = 0;
    private long preTime = 0;

    private SportsApp mSportsApp;
    private int shareId;

    // 保存经纬度
    private List<Integer> recordLong = new ArrayList<Integer>();
    private List<Integer> recordLat = new ArrayList<Integer>();

    // private PolylineOptions options;
    private MarkerOptions mMarkerOpStart;
    private MarkerOptions mMarkerOpEnd;

    private float zoomValue = 18;

//    private List<Marker> mMediaMarkerList = new ArrayList<Marker>();

    private double dLat = 0.0f;
    private double dLng = 0.0f;
    private boolean mapLoaded = false;
    private Dialog dialog, shareDialog;
    private String startTimeQQ;
    private boolean qq_health;

    private TextView peiValue;

    private ImageButton sport_detail_share;
    private ImageDownloader mDownloader;
    private ImageView pesimg;
    private Tencent mTencent;

    private Dialog mLoadProgressDialog = null;
    private TextView sport_map_yinshi;
    private boolean isYinshi = false;
    private GroundOverlay groundoverlay;
    private GroundOverlayOptions overlayOptions;
    private LinearLayout ll_sportdetail_type;
    private LineGraphicView sportdetail_LineView;
    private PeisuDB peisuDB;
    private int isUpload_dengshan;
    private ApiBack aback;
    private PeisuInfo psInfo;
    private List<GetPeisu> psList;
    private String send_markCode;// 运动唯一标识
    private TextView sports_maxheight;//登山最高高度
    double distance;//公里数
    //    private final MyDtailHandler myDtailHandler=new MyDtailHandler(this);
    private ArrayList<Integer> colorList = new ArrayList<Integer>();
    private boolean isMapQiehuan = false;//地图是否切换
    private List<String> speedList;//表示速度列表
    private String strSpeedList,strSportsMarkList;
    private ArrayList<MarkerOptions>  everyDisList;
    private ArrayList<Marker>  everyMarkList;
    private boolean isDisShow = false;//地图每公里是否显示
    private TextView sports_typename,sports_starttime;//运动类型名称和运动开始时间
    private TextView speedlow_tx,speedfast_tx;
    private View speed_line;
    private RelativeLayout speedline_layout;
    private LinearLayout sportsinfo_layout,sports_marklayout;
    private ArrayList<SportsMarkDis> sportsMarkDisList=new ArrayList<SportsMarkDis>();

    private PopupWindow myWindow = null;
    private RelativeLayout myView;
    private RelativeLayout mPopMenuBack,rl_sport;
    private Handler pHandler = new Handler();
//    private String grouptype;//标示是否达到勋章的标示
    private Bitmap markBitmap,lineBitmap;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        boolean findMethod = SmartBarUtils.findActionBarTabsShowAtBottom();
        if (!findMethod) {
            getWindow().setUiOptions(0);
            getWindow().requestFeature(android.view.Window.FEATURE_NO_TITLE);
        }

        setContentView(R.layout.sportdetail_map_gaode1);
        peisuDB = PeisuDB.getmInstance(getApplicationContext());

        mContext = this;
        mSportsApp = (SportsApp) getApplication();

        thisLarge = SHARE_PATH;

        mMapView = (MapView) findViewById(R.id.bmapView);
        mMapView.onCreate(savedInstanceState);// 此方法必须重写

        if (aMap == null) {
            aMap = mMapView.getMap();
            mUiSettings = aMap.getUiSettings();
            mUiSettings.setZoomControlsEnabled(false);
            mUiSettings.setZoomGesturesEnabled(true);
            mUiSettings.setMyLocationButtonEnabled(false); // 是否显示默认的定位按钮
            aMap.setOnMapLoadedListener(new OnMapLoadedListener() {
                @Override
                public void onMapLoaded() {
                    mapLoaded = true;
                    if (recordLat != null && recordLat.size() > 1
                            && recordLong != null && recordLong.size() > 1) {
                        LatLng point0 = new LatLng(recordLat.get(0) / 1E6,
                                recordLong.get(0) / 1E6);
                        LatLng point1 = new LatLng(recordLat.get(0) / 1E6,
                                recordLong.get(recordLong.size() - 1) / 1E6);
                        LatLng point2 = new LatLng(recordLat.get(recordLat
                                .size() - 1) / 1E6, recordLong.get(0) / 1E6);
                        LatLng point3 = new LatLng(recordLat.get(recordLat
                                .size() - 1) / 1E6, recordLong.get(recordLong
                                .size() - 1) / 1E6);
                        LatLngBounds bounds = new LatLngBounds.Builder()
                                .include(point0).include(point1).include(point2).include(point3)
                                .build();
                        // 移动地图，所有marker自适应显示。LatLngBounds与地图边缘10像素的填充区域
                        aMap.moveCamera(CameraUpdateFactory.newLatLngBounds(
                                bounds, 100));
                        point0=null;
                        point1=null;
                        point2=null;
                        point3=null;
                        bounds=null;

                    }
                }
            });// 设置amap加载成功事件监听器

//            aMap.setOnMarkerClickListener(new OnMarkerClickListener() {
//                @Override
//                public boolean onMarkerClick(final Marker marker) {
//                    Log.v(TAG, "onMarkerClick");
//                    for (int i = 0; i < mMediaMarkerList.size(); i++) {
//                        if (marker.equals(mMediaMarkerList.get(i))) {
//                            Intent intent = new Intent(
//                                    SportTaskDetailActivityGaode.this,
//                                    SportMediaFileDetailActivity.class);
//                            intent.putExtra("index", i);
//                            SportTaskDetailActivityGaode.this
//                                    .startActivity(intent);
//                            return true;
//                        }
//                    }
//                    return false;
//                }
//            });// 设置点击marker事件监听器


        }


        mMarkerOpStart = new MarkerOptions().icon(BitmapDescriptorFactory
                .fromBitmap(BitmapFactory.decodeResource(getResources(),
                        R.drawable.map_start))).zIndex(11);

        mMarkerOpEnd = new MarkerOptions().icon(BitmapDescriptorFactory
                .fromBitmap(BitmapFactory.decodeResource(getResources(),
                        R.drawable.map_middle))).zIndex(11);

        if (findMethod) {
            getActionBar().setDisplayShowHomeEnabled(false);
            getActionBar().setDisplayShowTitleEnabled(false);
            SmartBarUtils.setActionModeHeaderHidden(getActionBar(), true);
            SmartBarUtils.setActionBarViewCollapsable(getActionBar(), true);
        }
        Bundle bundle = new Bundle();
        bundle.putString("message",
                getResources().getString(R.string.sports_wait));
        mDialog = onCreateDialog(1, bundle, 0);
//        mDialog.show();

        speedList = new ArrayList<String>();
        initView();
    }

    Runnable rs = new Runnable() {

        @Override
        public void run() {
            if (null != SportTaskDetailActivityGaode.this.getWindow().getDecorView()
                    .getWindowToken()) {
                speedLimitPopWindow();
                pHandler.removeCallbacks(this);
            } else {
                pHandler.postDelayed(this, 5);
            }
        }
    };

    private void initView() {
        rl_sport = (RelativeLayout) findViewById(R.id.rl_sport);
        mPopMenuBack = (RelativeLayout) findViewById(R.id.set_menu_background);

        sessionId = ((SportsApp) getApplication()).getSessionId();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            taskID = bundle.getInt("taskid");
            uid = bundle.getInt("uid");
        }
        if(SportsApp.getInstance().getSportUser().getUid()==0){
            curUid=uid;
        }else{
            curUid = SportsApp.getInstance().getSportUser().getUid();
        }
        db = SportSubTaskDB.getInstance(mContext);
        if (curUid == uid) {
            if (bundle != null) {
                startTimeFrom = bundle.getString("startTime");
                mark_code = bundle.getString("mark_code");
            }
            SportTask taskDetail = null;
            if (mark_code != null && !"".equals(mark_code)
                    && startTimeFrom != null && !"".equals(startTimeFrom)) {
                taskDetail = db.getTaskByTimeAndCode(curUid, startTimeFrom,
                        mark_code);
            } else {
                if (startTimeFrom != null && !"".equals(startTimeFrom)) {
                    taskDetail = db.getTaskByStartTime(curUid, startTimeFrom);
                } else {
                    taskDetail = db.getTask(curUid, taskID);
                }
            }

            if (taskDetail != null) {
                if (!SportTaskUtil.getNormalRange(taskDetail.getSport_type_task(),taskDetail.getSport_speed(),1)||taskDetail.getSport_isupload() == 2){
                    pHandler.post(rs);
                }
                if (taskDetail.getSport_speedlsit() != null && !"".equals(taskDetail.getSport_speedlsit())&&!"0".equals(taskDetail.getSport_speedlsit())) {
                    strSpeedList = taskDetail.getSport_speedlsit();
                    speedList = SportTrajectoryUtilGaode.strToList(taskDetail.getSport_speedlsit());
                } else {
                    strSpeedList = "";
                }
                if (taskDetail.getCoordinate_list() != null && !"".equals(taskDetail.getCoordinate_list())&&!"0".equals(taskDetail.getSport_speedlsit())) {
                    strSportsMarkList = taskDetail.getCoordinate_list();
                    sportsMarkDisList = SportTrajectoryUtilGaode.stringToSportsMarkDis(strSportsMarkList);
                } else {
                    strSportsMarkList = "";
                }
                isUpload = taskDetail.getSport_isupload();
                mark_code = taskDetail.getSport_mark_code();
                initFromSportDetail(taskDetail);
                if (mGeoPoints != null && mGeoPoints.size() > 0) {
                    sport_map_yinshi.setVisibility(View.VISIBLE);
                    if(dis>=1){
                        if(typeId != SportsType.TYPE_CLIMBING){
                            if(sportsMarkDisList!=null&&sportsMarkDisList.size()>0){
                                findViewById(R.id.every_disicon).setVisibility(View.VISIBLE);
                                everyDisList=new ArrayList<MarkerOptions>();
                                everyMarkList=new ArrayList<Marker>();
                                setDisIcon();
                            }else{
                                findViewById(R.id.every_disicon).setVisibility(View.GONE);
                            }
                        }else{
                            findViewById(R.id.every_disicon).setVisibility(View.GONE);
                        }
                    }else{
                        findViewById(R.id.every_disicon).setVisibility(View.GONE);
                    }
                } else {
                    sport_map_yinshi.setVisibility(View.GONE);
                    findViewById(R.id.every_disicon).setVisibility(View.GONE);
                    LatLng latLng=new LatLng(39.908797,116.397732);
                    addBgMarker(latLng,R.drawable.sporttails_half_bg);
                }
                int tempId = taskDetail.getSport_taskid();
                temp_taskId = taskDetail.getSport_taskid();
                send_markCode = taskDetail.getSport_mark_code();
                String tempIsUpload = peisuDB.selectisUpload(mark_code);
                if (!"".equals(tempIsUpload) && tempIsUpload != null) {
                    isUpload_dengshan = Integer.parseInt(tempIsUpload);
                } else {
                    isUpload_dengshan = 1;
                    if (isUpload == 1) {
                        if (taskDetail.getSport_type_task() == SportsType.TYPE_CLIMBING) {
                            if (mSportsApp.isOpenNetwork()) {
                                new GetPeisuThread(SportTaskDetailActivityGaode.this, 1).start();
                            }
                        }

                    }
                    if (isUpload == 1 || isUpload == 2) {
                        uploadBtn.setVisibility(View.GONE);
                    }
                }
                tempIsUpload = null;

                fromID = 1;
                startTimeQQ = getSharedPreferences("qq_health_sprots", 0)
                        .getString(startTimeFrom, "");
                if (mContext
                        .getSharedPreferences("user_login_info",
                                Context.MODE_PRIVATE)
                        .getString("weibotype", "").equals("qqzone")
                        && (typeId == SportsType.TYPE_WALK
                        || typeId == SportsType.TYPE_RUN || typeId == SportsType.TYPE_CYCLING)) {
                    qq_health = !TextUtils.isEmpty(startTimeQQ); // 重传qq健康数据
                } else {
                    qq_health = false;
                }
                if (typeId == SportsType.TYPE_CLIMBING) {
                    if (isUpload != 1 || (tempId < 0 && isUpload == 1) || qq_health || isUpload_dengshan != 1) {
//                        SharedPreferences sp = mContext.getSharedPreferences(
//                                "sport_state_" + uid, 0);
//                        int typeId = sp.getInt("typeId", 1);
                        if (!SportTaskUtil.getNormalRange(typeId, speed, recLen)) {
                            uploadBtn.setVisibility(View.GONE);
                        } else {
                            if (isUpload == 2) {
                                uploadBtn.setVisibility(View.GONE);
                            } else {
                                uploadBtn.setVisibility(View.VISIBLE);
                                uploadBtn.setOnClickListener(this);
                            }
                        }
                    }
                } else {
                    if (isUpload != 1 || (tempId < 0 && isUpload == 1) || qq_health || isUpload_dengshan != 1) {
//                        SharedPreferences sp = mContext.getSharedPreferences(
//                                "sport_state_" + uid, 0);
//                        int typeId = sp.getInt("typeId", 1);
                        if (!SportTaskUtil.getNormalRange(typeId, speed, recLen)) {
                            uploadBtn.setVisibility(View.GONE);
                        } else {
                            if (isUpload == 2) {
                                uploadBtn.setVisibility(View.GONE);
                            } else {
                                uploadBtn.setVisibility(View.VISIBLE);
                                uploadBtn.setOnClickListener(this);
                            }
                        }
                    }
                }
                if (mDialog != null && mDialog.isShowing()) {
                    mDialog.dismiss();
                }
            } else {
                SportAsyncTask requestTask = new SportAsyncTask();
                requestTask.execute(UPLOAD_REQUEST);
            }
        } else {
            if(mSportsApp.isOpenNetwork()){
                SportAsyncTask requestTask = new SportAsyncTask();
                requestTask.execute(UPLOAD_REQUEST);
            }

        }
    }

    public void getPeisu() {
        if (psList != null) {
            psInfo = new PeisuInfo();
            psInfo.setListpeis(psList);
        }
    }

    private void init_ll_sportdetail() {
        psList = peisuDB.selectAll(mark_code);
        if (typeId == SportsType.TYPE_CLIMBING) {
            ll_sportdetail_type = (LinearLayout) findViewById(R.id.sportdetail_type2);
            speedline_layout=(RelativeLayout)findViewById(R.id.speedline_layout);
            sportsinfo_layout=(LinearLayout)findViewById(R.id.sportsinfo_layout);
            ll_sportdetail_type.setVisibility(View.VISIBLE);
            findViewById(R.id.sportdetail_type1).setVisibility(View.GONE);
            sportdetail_LineView = (LineGraphicView) findViewById(R.id.sportdetail_LineView);
            sports_maxheight = (TextView) ll_sportdetail_type.findViewById(R.id.sports_maxheight);
            if (psList != null && psList.size() > 0) {
                getPeisu();
                ArrayList<Double> yList = new ArrayList<Double>();
                ArrayList<Double> xList = new ArrayList<Double>();
                ArrayList<String> type_List = new ArrayList<String>();
                for (int i = 0; i < psList.size(); i++) {
                    yList.add(Double.parseDouble(psList.get(i).getSprots_velocity()));
                    xList.add(Double.parseDouble(psList.get(i).getSport_distance()));
                    type_List.add(psList.get(i).getgPS_type());
                }
                int min = getYMinnum(psList);
                int max1 = getYMaxnum(psList, min, 1);
                int max2 = getYMaxnum(psList, min, 0);//表示最高高度
                sports_maxheight.setText(max2 + "m");
                sportdetail_LineView.setData(yList, xList, type_List, max1, min, (max1 - min) / 5, getXMaxnum(psList));
                yList = null;
                xList = null;
                type_List = null;
            }

        } else {
            ll_sportdetail_type = (LinearLayout) findViewById(R.id.sportdetail_type1);
            speedline_layout=(RelativeLayout)findViewById(R.id.speedline_layout);
            sportsinfo_layout=(LinearLayout)findViewById(R.id.sportsinfo_layout);
            findViewById(R.id.sportdetail_type2).setVisibility(View.GONE);
            ll_sportdetail_type.setVisibility(View.VISIBLE);
        }

        sport_map_yinshi = (TextView) findViewById(R.id.sport_map_yinshi);
        sport_map_yinshi.setOnClickListener(this);


        backBtn = (ImageButton) findViewById(R.id.sport_map_back);

        sport_map_finish = (ImageButton) findViewById(R.id.sport_map_finish);
        sport_map_finish.setOnClickListener(this);
        sport_detail_share = (ImageButton) findViewById(R.id.sportdetail_share_btn);
        sport_detail_share.setOnClickListener(this);

        header_layout = (RelativeLayout) findViewById(R.id.sport_header_layout);
        uploadBtn = (ImageButton) findViewById(R.id.sport_map_upload);
        backBtn.setOnClickListener(this);
//        shareLayout = (RelativeLayout) ll_sportdetail_type.findViewById(R.id.shareLayout);
//        sportStateLayout = (RelativeLayout) ll_sportdetail_type.findViewById(R.id.sportStateLayout);
//        shareLinearLayout = (LinearLayout) ll_sportdetail_type.findViewById(R.id.shareLinearLayout);
//        mLine1 = (ImageView) ll_sportdetail_type.findViewById(R.id.line1);
//        mLine2 = (ImageView) findViewById(R.id.line2);
        pesimg = (ImageView) ll_sportdetail_type.findViewById(R.id.pesuicon);
        pesimg.setOnClickListener(this);


//        unfoldBtn = (ImageButton) findViewById(R.id.bigBtn);
//        unfoldBtn.setOnClickListener(this);

//        ll_sportdetail_type.findViewById(R.id.xinlang).setOnClickListener(this);
//        ll_sportdetail_type.findViewById(R.id.tengxun).setOnClickListener(this);
//        ll_sportdetail_type.findViewById(R.id.weixin).setOnClickListener(this);
//        ll_sportdetail_type.findViewById(R.id.faxian).setOnClickListener(this);
        if (curUid == uid) {
            ImageButton delBtn = (ImageButton) findViewById(R.id.sport_map_del);
            delBtn.setOnClickListener(this);
            delBtn.setVisibility(View.VISIBLE);
//            shareLayout.setVisibility(View.VISIBLE);

            sport_detail_share.setVisibility(View.VISIBLE);
        } else {
//            shareLayout.setVisibility(View.GONE);

            sport_detail_share.setVisibility(View.GONE);
        }

        sports_marklayout=(LinearLayout) findViewById(R.id.sports_marklayout);
        findViewById(R.id.maptype_qiehuan).setOnClickListener(this);
        findViewById(R.id.every_disicon).setOnClickListener(this);
        sports_starttime=(TextView)findViewById(R.id.sports_starttime);
        sports_typename=(TextView)findViewById(R.id.sports_typename);
        sports_typename.setText(getString(SportTaskUtil.getTypeName(typeId)));
        DateFormat format2= new SimpleDateFormat("MM月dd日 HH:mm");
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date=formatter.parse(startTime);
            String stime= format2.format(date);
            sports_starttime.setText(stime);
        } catch (ParseException e) {
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }
        speedlow_tx=(TextView)speedline_layout.findViewById(R.id.speedlow_tx);
        speedfast_tx=(TextView)speedline_layout.findViewById(R.id.speedfast_tx);
        speed_line=speedline_layout.findViewById(R.id.speed_line);

    }

    private int getYMaxnum(List<GetPeisu> list, int min, int flag) {
        //flag 0表示数组里的最大值  1表示计算后5的整除的最大值
        int Max = 0;
        for (int i = 0; i < list.size(); i++) {
            if (i == 0) {
                Max = Integer.parseInt(list.get(i).getSprots_velocity());
            }
            if (Integer.parseInt(list.get(i).getSprots_velocity()) >= Max) {
                Max = Integer.parseInt(list.get(i).getSprots_velocity());
            }
        }
        if (flag == 1) {
            if ((Max - min) % 5 == 0) {
                if (Max == min) {
                    Max = Max + 5;
                }
                return Max;
            } else {
                Max = Max + (5 - ((Max - min) % 5));
            }
        }

        return Max;
    }

    private int getYMinnum(List<GetPeisu> list) {
        int Min = 0;
        for (int i = 0; i < list.size(); i++) {
            if (i == 0) {
                Min = Integer.parseInt(list.get(i).getSprots_velocity());
            }
            if (Integer.parseInt(list.get(i).getSprots_velocity()) <= Min) {
                Min = Integer.parseInt(list.get(i).getSprots_velocity());
            }
        }
        return Min;
    }

    private double getXMaxnum(List<GetPeisu> list) {
        double Max = 0;
        for (int i = 0; i < list.size(); i++) {
            if (i == 0) {
                Max = Double.parseDouble(list.get(i).getSport_distance());
            }
            if (Double.parseDouble(list.get(i).getSport_distance()) >= Max) {
                Max = Double.parseDouble(list.get(i).getSport_distance());
            }
        }
        return Max;
    }

    private void initFromSportDetail(SportTask taskDetail) {
        if (taskDetail != null) {
            startTime = taskDetail.getStart_time();
            typeId = taskDetail.getSport_type_task();
            typeDetailId = taskDetail.getSports_swim_type();
            if (typeDetailId < 0) {
                typeDetailId = 0;
            }
            deviceId = taskDetail.getSport_device();
            recLen = taskDetail.getSport_time();
            dis = taskDetail.getSport_distance();
            con = taskDetail.getSport_calories();
            speed = taskDetail.getSport_speed();
            heart = taskDetail.getSport_heart_rate();
            step = taskDetail.getSport_step();
            mapType = taskDetail.getSport_map_type();
            if (taskDetail.getSport_mark_code() == null
                    || "".equals(taskDetail.getSport_mark_code())) {
                mark_code = "";
            } else {
                mark_code = taskDetail.getSport_mark_code();
            }
            init_ll_sportdetail();
            if (mapType == SportsApp.MAP_TYPE_BAIDU) {
                dLat = 0.0060f;
                dLng = 0.0065f;
            }

            pointsStr = taskDetail.getSport_lat_lng();
            if (!TextUtils.isEmpty(pointsStr)) {
                // 获取点
                mGeoPoints = SportTrajectoryUtilGaode.stringToLatLng(pointsStr,
                        dLat, dLng);
                for (LatLng getGeopoints : mGeoPoints) {
                    if (getGeopoints.latitude != SportTrajectoryUtilGaode.INVALID_LATLNG
                            && getGeopoints.longitude != SportTrajectoryUtilGaode.INVALID_LATLNG) {
                        recordLong.add((int) (getGeopoints.longitude * 1E6));
                        recordLat.add((int) (getGeopoints.latitude * 1E6));
                    }
                }
                Collections.sort(recordLong);
                Collections.sort(recordLat);
                if (recordLat != null && recordLat.size() > 1
                        && recordLong != null && recordLong.size() > 1) {
                    LatLng point0 = new LatLng(recordLat.get(0) / 1E6,
                            recordLong.get(0) / 1E6);
                    LatLng point1 = new LatLng(recordLat.get(0) / 1E6,
                            recordLong.get(recordLong.size() - 1) / 1E6);
                    LatLng point2 = new LatLng(
                            recordLat.get(recordLat.size() - 1) / 1E6,
                            recordLong.get(0) / 1E6);
                    LatLng point3 = new LatLng(
                            recordLat.get(recordLat.size() - 1) / 1E6,
                            recordLong.get(recordLong.size() - 1) / 1E6);
                    LatLngBounds bounds = new LatLngBounds.Builder()
                            .include(point0).include(point1).include(point2)
                            .include(point3).build();
                    // 移动地图，所有marker自适应显示。LatLngBounds与地图边缘10像素的填充区域
                    if (mapLoaded){
                        aMap.moveCamera(CameraUpdateFactory.newLatLngBounds(
                                bounds, 200));
                    }
                    point0=null;
                    point1=null;
                    point2=null;
                    point3=null;
                    bounds=null;

                }
            }
            if (mGeoPoints.size() > 0) {
                if (mGeoPoints.size() == 1) {
                    LatLng point = mGeoPoints.get(0);
                    if (point.latitude == SportTrajectoryUtilGaode.INVALID_LATLNG
                            || point.longitude == SportTrajectoryUtilGaode.INVALID_LATLNG) {
                    } else {
                        animateTo(point);
                        aMap.addMarker(mMarkerOpStart.position(point));
                        addBgMarker(point,R.drawable.sporttails_half_bg);
                    }
                } else {
                    LatLng point = mGeoPoints.get(0);
                    aMap.addMarker(mMarkerOpStart.position(point));
                    addBgMarker(point,R.drawable.sporttails_half_bg);
                    LatLng point2 = mGeoPoints.get(mGeoPoints.size() - 1);
                    if (point2.latitude == SportTrajectoryUtilGaode.INVALID_LATLNG
                            || point2.longitude == SportTrajectoryUtilGaode.INVALID_LATLNG)
                        point2 = mGeoPoints.get(mGeoPoints.size() - 2);
                    aMap.addMarker(mMarkerOpEnd.position(point2));
                }
                mDrawTrajectoryHandler.sendEmptyMessage(DRAWTRAJECTORY);// 绘制路径

                //是否显示速度的最大小值
                if (typeId != SportsType.TYPE_CLIMBING) {
                    if(mGeoPoints.size()>=2&&speedList!=null&&speedList.size()>=2){
                        caculateSpeedNum();
                    }else{
                        speedline_layout.setVisibility(View.GONE);
                    }
                }else{
                    speedline_layout.setVisibility(View.GONE);
                }

            } else {
                speedline_layout.setVisibility(View.GONE);
            }
            if (typeId == SportsType.TYPE_CLIMBING) {
                speedline_layout.setVisibility(View.GONE);
            }

//            ImageButton sport_map_back = (ImageButton) findViewById(R.id.sport_map_back);
//            sport_map_back.setBackgroundResource(SportsUtilities
//                    .getStateImgIdsTitle(typeId, typeDetailId));

//            String typeStr = getString(SportTaskUtil.getDetailTypeName(typeId,
//                    typeDetailId));
            chronometerId = (TextView) ll_sportdetail_type.findViewById(R.id.chronometerId);
            String tiemStr = SportTaskUtil.showTimeCount(recLen);
            chronometerId.setText(tiemStr);
            disValue = (TextView) ll_sportdetail_type.findViewById(R.id.disValue);
            String disStr = SportTaskUtil.getDoubleNumber(dis);
            distance = Double.parseDouble(disStr);//获取的公里数
            if (typeId != SportsType.TYPE_CLIMBING) {
                if (psList != null && psList.size() > 0) {
                    getPeisu();
                    if (distance > 1.0) {
                        pesimg.setVisibility(View.VISIBLE);
                    } else {
                        pesimg.setVisibility(View.GONE);
                    }
                } else {
                    if (mSportsApp.isOpenNetwork()) {
                        new GetPeisuThread(SportTaskDetailActivityGaode.this, 2).start();
                    } else {
                        pesimg.setVisibility(View.GONE);
                    }
                }

            }
            if (dis >= 100) {
                if (typeId == SportsType.TYPE_CLIMBING) {
                    disValue.setText((int) dis + "");
                } else {
                    disValue.setText(disStr);
                }
            } else {
                disValue.setText(disStr);
            }
            conValue = (TextView) ll_sportdetail_type.findViewById(R.id.conValue);
//          conValue.setText(String.valueOf(con));
            conValue.setText((int)con+"");
            speedValue = (TextView) ll_sportdetail_type.findViewById(R.id.tv_average_speed);
            String speedStr = SportTaskUtil.getDoubleOneNum(speed);
            speedValue.setText(speedStr);

            peiValue = (TextView) ll_sportdetail_type.findViewById(R.id.peiValue);
            double pace;
            if (speed != 0) {
                pace = 3600 / speed;// 配速：单位 秒/公里
            } else {
                pace = 0;
            }
            String paceStringValue = (int) (pace / 60) + "'"
                    + (int) (pace % 60) + "\"";
            peiValue.setText(paceStringValue);

        } else {
            Toast.makeText(mContext, getString(R.string.sports_get_data_fail),
                    Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
    }


//    private Marker addMediaItem(LatLng point, int typeID) {
//        MarkerOptions marker = null;
//        if (typeID == 1) {
//            marker = new MarkerOptions().icon(BitmapDescriptorFactory
//                    .fromBitmap(BitmapFactory.decodeResource(getResources(),
//                            R.drawable.ic_launcher)));
//        } else if (typeID == 2) {
//            marker = new MarkerOptions().icon(BitmapDescriptorFactory
//                    .fromBitmap(BitmapFactory.decodeResource(getResources(),
//                            R.drawable.ic_launcher)));
//        } else if (typeID == 3) {
//            marker = new MarkerOptions().icon(BitmapDescriptorFactory
//                    .fromBitmap(BitmapFactory.decodeResource(getResources(),
//                            R.drawable.ic_launcher)));
//        }
//        return aMap.addMarker(marker.position(point));
//    }

    //配速更新上传是否成功
    private int updateisSavalocal(String bs, String isupload) {
        int savainsert = 0;
        if (peisuDB == null) {
            peisuDB = PeisuDB.getmInstance(getApplicationContext());
        }
        ContentValues c = new ContentValues();
        c.put(PeisuDB.SPORT_MARKCODE, bs);
        c.put(PeisuDB.SPORT_ISUPLOAD, isupload);
        savainsert = peisuDB.update(c, send_markCode, false);
        return savainsert;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sport_map_finish:
                finish();
                break;
            case R.id.sport_map_del:
                if (mSportsApp.isOpenNetwork()) {
                    Bundle delBundle = new Bundle();
                    delBundle.putString("title",
                            getString(R.string.confirm_exit_title));
                    delBundle.putString("message",
                            getString(R.string.sports_confirm_delete_data));
                    onCreateDialog(2, delBundle, UPLOAD_DELETE);
                } else {
                    Toast.makeText(this, getString(R.string.network_not_avaliable),
                            Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.sport_map_upload:
                if (mSportsApp.LoginOption) {
                    Log.i("uploadBtn", "isUpload:" + isUpload);
                    if (mSportsApp.isOpenNetwork()) {
                        if (isUpload == -1 || isUpload == 0) {
                            Bundle stopBundle = new Bundle();
                            stopBundle.putString("title",
                                    getString(R.string.confirm_exit_title));
                            stopBundle.putString("message",
                                    getString(R.string.sports_confirm_upload_data));
                            if (startTimeFrom != null && taskID > 0) {
                                onCreateDialog(2, stopBundle, UPLOAD_UPDATE);
                            } else {
                                onCreateDialog(2, stopBundle, UPLOAD_START);
                            }
                        } else if (qq_health) {
                            Bundle stopBundle = new Bundle();
                            stopBundle.putString("title",
                                    getString(R.string.confirm_exit_title));
                            stopBundle.putString("message",
                                    getString(R.string.sports_confirm_qq_data));
                            onCreateDialog(2, stopBundle, UPLOAD_QQ_HEALTH);
                        } else if (isUpload_dengshan != 1 && (isUpload == 1 || isUpload == 2) && temp_taskId > 0) {//重新上传配速
                            if (psList != null && psList.size() > 0 && psInfo != null) {
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            aback = ApiJsonParser.uploadPeisu(mSportsApp.getSessionId(), temp_taskId, "android", "z" + getResources().getString(R.string.config_game_id),
                                                    psInfo, typeId);
                                            Message msg = new Message();
                                            msg.what = 20160118;
                                            msg.obj = aback;
                                            sportHandler.sendMessage(msg);
                                        } catch (ApiSessionOutException e) {
                                            e.printStackTrace();
                                        } catch (ApiNetException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }).start();
                            } else {
                                Toast.makeText(this,
                                        "数据解析失败",
                                        Toast.LENGTH_SHORT).show();
                            }

                        }
                    } else {
                        Toast.makeText(this,
                                getString(R.string.network_not_avaliable),
                                Toast.LENGTH_SHORT).show();
                    }
                } else {
                    mSportsApp.TyrLoginAction(SportTaskDetailActivityGaode.this,
                            getString(R.string.sports_love_title),
                            getString(R.string.try_to_login));
                }

                break;

//            case R.id.bigBtn:
//                if (isGone) {
//                    shareLinearLayout.setVisibility(View.VISIBLE);
//                    sportStateLayout.setVisibility(View.VISIBLE);
//                    mLine1.setVisibility(View.VISIBLE);
//                    mLine2.setVisibility(View.VISIBLE);
//                    isGone = false;
//                } else {
//                    shareLinearLayout.setVisibility(View.GONE);
//                    sportStateLayout.setVisibility(View.GONE);
//                    mLine1.setVisibility(View.GONE);
//                    mLine2.setVisibility(View.GONE);
//                    isGone = true;
//                }
//                break;

            case R.id.xinlang:
            case R.id.xinlang_layout:
                if (shareDialog != null && shareDialog.isShowing()) {
                    shareDialog.dismiss();
                }
                goShare(SHARE_XINLANG);
                break;
            case R.id.tengxun:
            case R.id.tengxun_layout:
                if (shareDialog != null && shareDialog.isShowing()) {
                    shareDialog.dismiss();
                }
                goShare(SHARE_TENGXUN);
                break;
            case R.id.weixin:
            case R.id.weixin_layout:
                Log.e("weixinshare", "share to weixin");
                if (shareDialog != null && shareDialog.isShowing()) {
                    shareDialog.dismiss();
                }
                goShare(SHARE_WEIXIN);
                break;
            case R.id.faxian:
            case R.id.faxian_layout:
                if (shareDialog != null && shareDialog.isShowing()) {
                    shareDialog.dismiss();
                }
                goShare(SHARE_FAXIAN);
                break;
            case R.id.sportdetail_share_btn:
                // sharePopWindow();
                onCreateDialog(3, null, 0);
                break;
            case R.id.pesuicon:
                goShare(SHAREPEI_SU);
                break;
            case R.id.sport_map_yinshi:
                if (mGeoPoints != null && mGeoPoints.size() > 1) {
                    if (groundoverlay == null) {
                        overlayOptions = new GroundOverlayOptions()
                                .image(BitmapDescriptorFactory.fromResource(R.drawable.yinshi_icon))
                                .position(mGeoPoints.get(0), 8 * 1000 * 1000, 8 * 1000 * 1000);

                        groundoverlay = aMap.addGroundOverlay(overlayOptions);
                        groundoverlay.setVisible(false);
                        groundoverlay.setTransparency(0);
                        groundoverlay.setZIndex(10);
                    }
                    if (isYinshi) {
                        isYinshi = false;
                        sport_map_yinshi.setBackgroundResource(R.drawable.map_nohide_btn);
                        groundoverlay.setVisible(false);
                        groundoverlay.setTransparency(0);
                        aMap.showMapText(true);
                        drawMap();
                    } else {
                        isYinshi = true;
                        sport_map_yinshi.setBackgroundResource(R.drawable.map_hide_btn);
                        groundoverlay.setVisible(true);
                        groundoverlay.setTransparency(0);
                        aMap.showMapText(false);
                        drawMap();
                    }
                }

                break;
            case R.id.maptype_qiehuan:
                if (aMap != null) {
                    if (isMapQiehuan) {
                        isMapQiehuan = false;
                        aMap.setMapType(AMap.MAP_TYPE_NORMAL);
                        findViewById(R.id.maptype_qiehuan).setBackgroundResource(R.drawable.mapdetailtype_qiehuan_unclick);
                    } else {
                        isMapQiehuan = true;
                        aMap.setMapType(AMap.MAP_TYPE_SATELLITE);
                        findViewById(R.id.maptype_qiehuan).setBackgroundResource(R.drawable.mapdetailtype_qiehuan_click);
                    }
                }

                break;
            case R.id.every_disicon:
                if (aMap != null) {
                    if (isDisShow) {
                        isShowEveryDis();
                        findViewById(R.id.every_disicon).setBackgroundResource(R.drawable.dis_unicon);
                        isDisShow = false;
                    } else {
                        isShowEveryDis();
                        isDisShow = true;
                        findViewById(R.id.every_disicon).setBackgroundResource(R.drawable.dis_icon);
                    }
                }

                break;
        }
    }

    private void goShare(int shareId) {
        this.shareId = shareId;
        if (mSportsApp.isOpenNetwork()) {
            if (mDialog != null && !mDialog.isShowing())
                mDialog.show();
            Tools.delAllFile(SHARE_PATH);
            if (Environment.getExternalStorageState().equals(
                    Environment.MEDIA_MOUNTED)) {
                Log.e("weixinshare", "get map screen shot");
                getMapScreenShot();
            } else {
                if (mDialog != null&&mDialog.isShowing())
                    mDialog.dismiss();
                Toast.makeText(mContext,
                        getString(R.string.sd_card_is_invalid), Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, getString(R.string.network_not_avaliable),
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onPause() {
        if (mMapView != null) {
            mMapView.onPause();
        }
        MobclickAgent.onPageEnd("SportTaskDetailActivityGaode");
        MobclickAgent.onPause(SportTaskDetailActivityGaode.this);
        FunctionStatic.onPause(this, FunctionStatic.FUNCTION_SPORTS_DETAIL,
                preTime);
        super.onPause();
    }

    @Override
    protected void onResume() {
        if (sessionId == null || sessionId.equals("")) {
            sessionId = ((SportsApp) getApplication()).getSessionId();
        }
        if (mGeoPoints != null && mGeoPoints.size() > 1) {
            if (isYinshi) {
                sport_map_yinshi.setBackgroundResource(R.drawable.map_hide_btn);
                aMap.showMapText(false);
                if (groundoverlay != null) {
                    groundoverlay.setVisible(true);
                    groundoverlay.setTransparency(0);
                }
            } else {
                sport_map_yinshi.setBackgroundResource(R.drawable.map_nohide_btn);
                if (groundoverlay != null) {
                    groundoverlay.setVisible(false);
                    groundoverlay.setTransparency(0);
                }
                aMap.showMapText(true);
            }
        }
        if (mMapView != null) {
            mMapView.onResume();
        }
        if (aMap != null) {
            if (isMapQiehuan) {
                aMap.setMapType(AMap.MAP_TYPE_SATELLITE);
            } else {
                aMap.setMapType(AMap.MAP_TYPE_NORMAL);
            }
        }
        MobclickAgent.onPageStart("SportTaskDetailActivityGaode");
        MobclickAgent.onResume(SportTaskDetailActivityGaode.this);
        preTime = FunctionStatic.onResume();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        if (aMap != null) {
            aMap.clear();
            aMap = null;
        }
        if (mMapView != null) {
            mMapView.onDestroy();
            mMapView = null;
        }
        if (mGeoPoints != null) {
            mGeoPoints.clear();
            mGeoPoints = null;
        }
        if (mediaFilesList != null) {
            mediaFilesList.clear();
            mediaFilesList = null;
        }
        if (recordLong != null) {
            recordLong.clear();
            recordLong = null;
        }
        if (recordLat != null) {
            recordLat.clear();
            recordLat = null;
        }
//        if (mMediaMarkerList != null) {
//            mMediaMarkerList.clear();
//            mMediaMarkerList = null;
//        }
        if (curUid == uid && mSportsApp != null
                && mSportsApp.getmHandler() != null) {
            mSportsApp.getmHandler()
                    .sendEmptyMessage(StateActivity.LAST_SPORTS);
        }
//        myDtailHandler.removeCallbacksAndMessages(null);
        if (speedList != null) {
            speedList.clear();
            speedList = null;
        }
        if (colorList != null) {
            colorList.clear();
            colorList = null;
        }
        if (everyDisList != null) {
            everyDisList.clear();
            everyDisList = null;
        }
        if (everyMarkList != null) {
            everyMarkList.clear();
            everyMarkList = null;
        }
        if(sportsMarkDisList!=null){
            sportsMarkDisList.clear();
            sportsMarkDisList=null;
        }
        if(markBitmap!=null){
            markBitmap.recycle();
            markBitmap=null;
        }
        if(lineBitmap!=null){
            lineBitmap.recycle();
            lineBitmap=null;
        }
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
        mDialog=null;
        myWindow=null;
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                finish();
                break;

            default:
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mMapView != null) {
            mMapView.onSaveInstanceState(outState);
        }
    }

    public Dialog onCreateDialog(int id, Bundle bundle, final int type) {
        String message = "";
        if (bundle != null) {
            message = bundle.getString("message");
        }
        switch (id) {
            case 1:
                Dialog loginPregressDialog = new Dialog(this, R.style.sports_dialog);
                LayoutInflater mInflater = getLayoutInflater();
                View v1 = mInflater.inflate(R.layout.sports_progressdialog, null);
                v1.setMinimumWidth((int) (SportsApp.ScreenWidth * 0.8));
                loginPregressDialog.setContentView(v1);
                loginPregressDialog.setCancelable(true);
                loginPregressDialog.setCanceledOnTouchOutside(false);
                return loginPregressDialog;
            case 2:
                dialog = new Dialog(this, R.style.sports_dialog1);
                LayoutInflater inflater = getLayoutInflater();
                // View v = inflater.inflate(R.layout.sports_dialog, null);
                View v = inflater.inflate(R.layout.sport_dialog_for_newtask, null);
                v.setMinimumWidth((int) (SportsApp.ScreenWidth * 0.8));
                TextView msg = (TextView) v.findViewById(R.id.message);
                msg.setText(message);
                Button button = (Button) v.findViewById(R.id.bt_ok);
                button.setText(getResources().getString(R.string.button_ok));
                dialog.setContentView(v);
                button.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        dialog.dismiss();
                        switch (type) {
                            case UPLOAD_START:
                                if (SportTaskUtil.getNormalRange(typeId, speed, recLen)) {
                                    if (mSportsApp.getSessionId() != null && !"".equals(mSportsApp.getSessionId())) {
                                        if (mDialog != null && !mDialog.isShowing()) {
                                            mDialog.show();
                                        }
                                        SportAsyncTask uploadTask = new SportAsyncTask();
                                        uploadTask.execute(UPLOAD_START);
                                    } else {
                                        Toast.makeText(mContext,
                                                getString(R.string.sports_data_load_failed),
                                                Toast.LENGTH_SHORT).show();
                                    }

                                } else {
                                    Toast.makeText(mContext,
                                            getString(R.string.sports_beyond_limit),
                                            Toast.LENGTH_SHORT).show();
                                }
                                if (qq_health)
                                    uploadQQAndCoinsAgain();
                                break;
                            case UPLOAD_UPDATE:
                                if (SportTaskUtil.getNormalRange(typeId, speed, recLen)) {
                                    if (mDialog != null && !mDialog.isShowing()) {
                                        mDialog.show();
                                    }
                                    SportAsyncTask updateTask = new SportAsyncTask();
                                    updateTask.execute(UPLOAD_UPDATE);
                                } else {
                                    Toast.makeText(mContext,
                                            getString(R.string.sports_beyond_limit),
                                            Toast.LENGTH_SHORT).show();
                                }
                                if (qq_health)
                                    uploadQQAndCoinsAgain();
                                break;
                            case UPLOAD_DELETE:
                                if (mDialog != null && !mDialog.isShowing()) {
                                    mDialog.show();
                                }
                                boolean result;
                                if (taskID > 0) {
                                    deleteSportTaskByID();
                                    result = db.delete(taskID);
                                } else {
                                    result = db.deleteSportByStartTime(startTimeFrom);
                                    if (result) {
                                        Toast.makeText(
                                                mContext,
                                                getString(R.string.sports_delete_successed),
                                                Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent();
                                        setResult(RESULT_OK, intent);
                                        finish();
                                    } else {
                                        Toast.makeText(
                                                mContext,
                                                getString(R.string.sports_delete_failed),
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                                break;
                            case UPLOAD_QQ_HEALTH:
                                uploadQQAndCoinsAgain();
                                break;
                        }
                    }
                });
                v.findViewById(R.id.bt_cancel).setOnClickListener(
                        new OnClickListener() {
                            @Override
                            public void onClick(View arg0) {
                                dialog.dismiss();
                            }
                        });
                dialog.setCancelable(true);
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();
                break;
            case 3:
                shareDialog = new Dialog(this, R.style.sports_dialog1);
                LayoutInflater inflater1 = getLayoutInflater();
                View view = inflater1.inflate(R.layout.sports_dialog2, null);
                view.setMinimumWidth((int) (SportsApp.ScreenWidth * 0.9));
                view.findViewById(R.id.faxian_layout).setOnClickListener(this);
                view.findViewById(R.id.weixin_layout).setOnClickListener(this);
                view.findViewById(R.id.tengxun_layout).setOnClickListener(this);
                view.findViewById(R.id.xinlang_layout).setOnClickListener(this);
                view.findViewById(R.id.share_cacle_txt).setOnClickListener(
                        new OnClickListener() {

                            @Override
                            public void onClick(View arg0) {
                                // TODO Auto-generated method stub
                                shareDialog.dismiss();
                            }
                        });
                shareDialog.setContentView(view);
                shareDialog.setCancelable(true);
                shareDialog.setCanceledOnTouchOutside(false);
                shareDialog.show();
        }
        return null;
    }

    private void uploadQQAndCoinsAgain() {
        int sportGoal = getSharedPreferences("sports" + uid, 0).getInt(
                "editDistance", 0);
        SportTaskUtil.send2QQ(mContext, typeId, sportGoal, startTimeQQ,
                SportTaskUtil.date2seconds(startTimeQQ), uid,
                new QQHealthResult() {

                    @Override
                    public void qqResult() {
                        qq_health = !TextUtils.isEmpty(getSharedPreferences(
                                "qq_health_sprots", 0).getString(startTimeFrom,
                                ""));
                        if (!qq_health && (isUpload == 1 || isUpload == 2) && isUpload_dengshan == 1) {
                            uploadBtn.setVisibility(View.GONE);
                        } else {
                            uploadBtn.setVisibility(View.VISIBLE);
                        }
                    }
                }); // 运动数据上传至qq健康平台
    }

    class AddCoinsHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            ApiBack results = (ApiBack) msg.obj;
            //
            switch (msg.what) {
                case ApiConstant.COINS_SUCCESS:
                    int grow_now = Integer.parseInt(results.getMsg());
                    if (coins == grow_now) {

                        SportTaskUtil.jump2CoinsDialog(
                                SportTaskDetailActivityGaode.this,
                                getString(R.string.sports_coins, grow_now));
                    } else {
                        SportTaskUtil.jump2CoinsDialog(
                                SportTaskDetailActivityGaode.this,
                                getString(R.string.sports_coins_limit_sucess,
                                        grow_now));
                    }

                    break;
                case ApiConstant.COINS_LIMIT:
                    Toast.makeText(mContext,
                            getString(R.string.sports_coins_limit_fail),
                            Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
            Message msgs = Message.obtain();
            msgs.what = thereturn;
            msgs.obj = theApiBack;
            sportHandler.sendMessage(msgs);
        }
    }

    private void uploadcoins() {
        new AddCoinsThread(coins, 3, addCoinsHandler, typeId).start();
    }

    private class SportAsyncTask extends AsyncTask<Object, Integer, Object> {
        // doInBackground方法用于在后台运行程序
        @Override
        protected ApiBack doInBackground(Object... params) {
            Message msg = Message.obtain();
            ApiBack apiBack = null;
            if (params == null) {
                msg.what = UPLOAD_FAIL;
                sportHandler.sendMessage(msg);
                return null;
            }
            int action = (Integer) params[0];
            if (action == UPLOAD_START) {
                // 上传
                // String pointString = SportTrajectoryUtilGaode
                // .listLatLngToString(mGeoPoints);
                try {
//                    apiBack = ApiJsonParser.uploadSportTask(0, sessionId,
//                            typeId, typeDetailId, deviceId, startTime, recLen,
//                            dis, con, speed, heart, pointsStr, step, mapType,
//                            DeviceUuidFactory.getDeviceSerial(), mark_code, strSpeedList,strSportsMarkList!=null?strSportsMarkList:"");

                    apiBack = ApiJsonParser.uploadSportsTwoInfo(0,
                            mSportsApp.getSessionId(), typeId, typeDetailId,
                            deviceId, startTime, recLen, dis, con, speed, heart, pointsStr, step,
                            mapType, DeviceUuidFactory.getDeviceSerial(), mark_code,strSpeedList,
                            strSportsMarkList, "z" + getResources().getString(R.string.config_game_id), psInfo,1);
                    if (apiBack != null && (apiBack.getFlag() == 1||apiBack.getFlag() == 1010)) {
                        isUpload = 1;
//                        grouptype=apiBack.getGrouptype();
                        taskID = apiBack.getReg();
                        coins = dis < 1 ? (dis > 0.5 ? 1 : 0) : (int) Math
                                .floor(dis);
                        theApiBack = apiBack;
                        thereturn = UPLOAD_START;
                        if(apiBack.getFlag() == 1010){
                            if (psList != null && psList.size() > 0 && psInfo != null) {
                                if (typeId == SportsType.TYPE_CLIMBING) {
                                    ApiBack aback1 = ApiJsonParser.uploadPeisu(sessionId, apiBack.getReg(), "android", "z" + getResources().getString(R.string.config_game_id),
                                            psInfo, typeId);
                                    if (aback1 != null && aback1.getFlag() == 1) {
                                        int isSuccess = updateisSavalocal(send_markCode, 1 + "");
                                        if (isSuccess > 0) {
                                            isUpload_dengshan = 1;
                                        }

                                    }
                                } else {
                                    if (dis >= 1) {
                                        ApiBack aback1 = ApiJsonParser.uploadPeisu(sessionId, apiBack.getReg(), "android", "z" + getResources().getString(R.string.config_game_id),
                                                psInfo, typeId);
                                        if (aback1 != null && aback1.getFlag() == 1) {
                                            int isSuccess = updateisSavalocal(send_markCode, 1 + "");
                                            if (isSuccess > 0) {
                                                isUpload_dengshan = 1;
                                            }

                                        }
                                    }
                                }
                            }
                        }else{
                            if (psList != null && psList.size() > 0 && psInfo != null) {
                                int isSuccess = updateisSavalocal(send_markCode, 1 + "");
                                if (isSuccess > 0) {
                                    isUpload_dengshan = 1;
                                }
                            }
                        }
                        uploadcoins();
                        // 上传成功更新用户数据信息
                        if (mSportsApp != null) {
                            if (mSportsApp.getmHandler() != null) {
                                mSportsApp.getmHandler().sendEmptyMessage(
                                        MyFirstSportFragment.REFRESH_USER);
                            }

                        }
                    } else if(apiBack != null && (apiBack.getFlag() == -100||apiBack.getFlag() == -56)){
                        Message msgfail = Message.obtain();
                        msgfail.what = apiBack.getFlag();
                        msgfail.obj = apiBack;
                        sportHandler.sendMessage(msgfail);
                    } else {
                        isUpload = -1;
                        Message msgfail = Message.obtain();
                        msgfail.what = UPLOAD_START;
                        msgfail.obj = apiBack;
                        sportHandler.sendMessage(msgfail);

                    }
                } catch (ApiNetException e) {
                    e.printStackTrace();
                    msg.what = UPLOAD_REQUEST_FAIL;
                    msg.arg1 = UPLOAD_START;
                    sportHandler.sendMessage(msg);
                    return null;
                } catch (ApiSessionOutException e) {
                    e.printStackTrace();
                    isUpload = -1;
                    Message msgfail = Message.obtain();
                    msgfail.what = UPLOAD_START;
                    msgfail.obj = apiBack;
                    sportHandler.sendMessage(msgfail);
                }
            } else if (action == UPLOAD_REQUEST) {
                try {
                    SportTask task = ApiJsonParser.getSportsTaskById(sessionId,
                            uid, taskID);
                    msg.what = UPLOAD_REQUEST;
                    msg.obj = task;
                    sportHandler.sendMessage(msg);
                } catch (ApiNetException e) {
                    msg.what = UPLOAD_REQUEST_FAIL;
                    msg.arg1 = UPLOAD_REQUEST;
                    sportHandler.sendMessage(msg);
                    e.printStackTrace();
                    return null;
                } catch (ApiSessionOutException e) {
                    e.printStackTrace();
                }
            } else if (action == UPLOAD_UPDATE) {
                // 更新
                // String pointString = SportTrajectoryUtilGaode
                // .listLatLngToString(mGeoPoints);

                try {
                    apiBack = ApiJsonParser.updateSportTask(taskID, 0,
                            sessionId, typeId, deviceId, startTime, recLen,
                            dis, con, speed, heart, pointsStr, step, mapType);
                    if (apiBack.getFlag() == 1) {
                        isUpload = 1;
                        taskID = apiBack.getReg();
                        coins = dis < 1 ? (dis > 0.5 ? 1 : 0) : (int) Math
                                .floor(dis);
                        theApiBack = apiBack;
                        thereturn = UPLOAD_UPDATE;
                        uploadcoins();
                    } else {
                        isUpload = -1;
                        Message msgfail = Message.obtain();
                        msgfail.what = UPLOAD_UPDATE;
                        msgfail.obj = apiBack;
                        sportHandler.sendMessage(msgfail);
                    }
                } catch (ApiNetException e) {
                    e.printStackTrace();
                    msg.what = UPLOAD_REQUEST_FAIL;
                    msg.arg1 = UPLOAD_UPDATE;
                    sportHandler.sendMessage(msg);
                    return null;
                } catch (ApiSessionOutException e) {
                    e.printStackTrace();
                    isUpload = -1;
                    Message msgfail = Message.obtain();
                    msgfail.what = UPLOAD_UPDATE;
                    msgfail.obj = apiBack;
                    sportHandler.sendMessage(msgfail);
                }
            }
            return apiBack;
        }

        // onPostExecute方法用于在执行完后台任务后更新UI,显示结果
        protected void onPostExecute(final ApiBack result) {
            if (result != null) {

            }
        }
    }

    private void deleteSportTaskByID() {
        new Thread() {
            @Override
            public void run() {
                Message msg = Message.obtain();
                ApiBack apiBack = null;
                try {
                    apiBack = ApiJsonParser.deleteSportsTaskById(sessionId,
                            uid, taskID);
                } catch (ApiNetException e) {
                    e.printStackTrace();
                    msg.what = UPLOAD_REQUEST_FAIL;
                    msg.arg1 = UPLOAD_DELETE;
                    sportHandler.sendMessage(msg);
                    return;
                } catch (ApiSessionOutException e) {
                    e.printStackTrace();
                    msg.what = UPLOAD_FAIL;
                    msg.arg1 = 0;
                    sportHandler.sendMessage(msg);
                }
                msg.what = UPLOAD_DELETE;
                msg.obj = apiBack;
                sportHandler.sendMessage(msg);
            }
        }.start();
    }

    private Handler sportHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case UPLOAD_START:
                    if (mDialog != null && mDialog.isShowing()) {
                        mDialog.dismiss();
                    }
                    ApiBack startBack = (ApiBack) msg.obj;
                    if (startBack != null && startBack.getFlag() == 1) {
                        if (fromID == 1) {
                            update();
                        }
                        Toast.makeText(mContext,
                                getString(R.string.upload_success),
                                Toast.LENGTH_SHORT).show();
                        if (!qq_health && (isUpload == 1 || isUpload == 2) && isUpload_dengshan == 1) {
                            uploadBtn.setVisibility(View.GONE);
                        } else {
                            uploadBtn.setVisibility(View.VISIBLE);
                        }

//                        if(grouptype!=null&&!"".equals(grouptype)){
//                            Intent intent = new Intent(SportTaskDetailActivityGaode.this,
//                                    SportsHonorActivity.class);
//                            intent.putExtra("fromPage", 2);
//                            intent.putExtra("grouptype", grouptype);
//                            startActivity(intent);
//                        }

                    } else {
                        Toast.makeText(mContext, getString(R.string.upload_failed),
                                Toast.LENGTH_SHORT).show();
                    }
                    break;
                case UPLOAD_UPDATE:
                    if (mDialog != null && mDialog.isShowing()) {
                        mDialog.dismiss();
                    }
                    ApiBack updateBack = (ApiBack) msg.obj;
                    if (updateBack != null && updateBack.getFlag() == 1) {
                        if (fromID == 1) {
                            update();
                        }
                        Toast.makeText(mContext,
                                getString(R.string.upload_success),
                                Toast.LENGTH_SHORT).show();
                        if (!qq_health && (isUpload == 1 || isUpload == 2) && isUpload_dengshan == 1) {
                            uploadBtn.setVisibility(View.GONE);
                        } else {
                            uploadBtn.setVisibility(View.VISIBLE);
                        }
                    } else {
                        Toast.makeText(mContext, getString(R.string.upload_failed),
                                Toast.LENGTH_SHORT).show();
                    }
                    break;
                case UPLOAD_REQUEST:
                    if (mDialog != null && mDialog.isShowing()) {
                        mDialog.dismiss();
                    }
                    if (mContext == null)
                        return;
                    SportTask task = (SportTask) msg.obj;
                    if (task != null) {
                        initFromSportDetail(task);
                        fromID = 2;
                    } else {
                        Toast.makeText(mContext,
                                getString(R.string.sports_data_load_failed),
                                Toast.LENGTH_SHORT).show();
                    }
                    break;
                case UPLOAD_FAIL:
                    if (mDialog != null && mDialog.isShowing()) {
                        mDialog.dismiss();
                    }
                    Toast.makeText(mContext,
                            getString(R.string.sports_access_timeout),
                            Toast.LENGTH_SHORT).show();
                    break;
                case UPLOAD_REQUEST_FAIL:
                    if (mDialog != null && mDialog.isShowing()) {
                        mDialog.dismiss();
                    }
                    if (msg.arg1 == UPLOAD_REQUEST) {
                        Toast.makeText(mContext,
                                getString(R.string.sports_get_data_fail),
                                Toast.LENGTH_SHORT).show();
                    } else if (msg.arg1 == UPLOAD_START) {
                        Toast.makeText(mContext, getString(R.string.upload_failed),
                                Toast.LENGTH_SHORT).show();
                    } else if (msg.arg1 == UPLOAD_UPDATE) {
                        Toast.makeText(mContext, getString(R.string.upload_failed),
                                Toast.LENGTH_SHORT).show();
                    } else if (msg.arg1 == UPLOAD_DELETE) {
                        Toast.makeText(mContext,
                                getString(R.string.sports_delete_failed),
                                Toast.LENGTH_SHORT).show();
                    }
                    break;
                case DELETE_FILE:
                    if (Environment.getExternalStorageState().equals(
                            Environment.MEDIA_MOUNTED)) {
                        getMapScreenShot();
                    } else {
                        if (mDialog != null&&mDialog.isShowing())
                            mDialog.dismiss();
                        Toast.makeText(mContext,
                                getString(R.string.sd_card_is_invalid), Toast.LENGTH_SHORT)
                                .show();
                    }
                    break;
                case 1209:
                    switch (shareId) {
                        case SHARE_XINLANG:
                            shareTo();
                            break;
                        case SHARE_TENGXUN:
                            shareTo();
                            break;
                        case SHARE_WEIXIN:
                            shareTo();
                            break;
                        case SHARE_FAXIAN:
                            shareToFaXian();
                            break;
                        case SHAREPEI_SU:
                            Intent intent = new Intent(SportTaskDetailActivityGaode.this, PeiSuActivity.class);
                            intent.putExtra("bs", mark_code);
                            intent.putExtra("taskid", taskID);
                            intent.putExtra("typeId", typeId);
                            intent.putExtra("imgurl", SHARE_PATH);
                            startActivity(intent);
                            break;

                    }
                    if (mDialog != null && mDialog.isShowing()) {
                        mDialog.dismiss();
                    }
                    break;
                case UPLOAD_DELETE:
                    if (mDialog != null && mDialog.isShowing()) {
                        mDialog.dismiss();
                    }
                    ApiBack delBack = (ApiBack) msg.obj;
                    if (delBack != null && delBack.getFlag() == 1) {
                        Toast.makeText(mContext,
                                getString(R.string.sports_delete_successed),
                                Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent();
                        setResult(RESULT_OK, intent);
                        finish();
                    } else {
                        Toast.makeText(mContext,
                                getString(R.string.sports_delete_failed),
                                Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 10009:
                    //表示获取登山信息
                    PeisuInfo peisuInfo = (PeisuInfo) msg.obj;
                    if (peisuInfo != null && peisuInfo.getListpeis() != null && peisuInfo.getListpeis().size() > 0) {
                        savePeisu(peisuInfo.getListpeis(), mark_code);
                        List<GetPeisu> ps = peisuInfo.getListpeis();
                        if (ps != null && ps.size() > 0) {
                            ArrayList<Double> yList = new ArrayList<Double>();
                            ArrayList<Double> xList = new ArrayList<Double>();
                            ArrayList<String> type_List = new ArrayList<String>();
                            for (int i = 0; i < ps.size(); i++) {
                                yList.add(Double.parseDouble(ps.get(i).getSprots_velocity()));
                                xList.add(Double.parseDouble(ps.get(i).getSport_distance()));
                                type_List.add(ps.get(i).getgPS_type());
                            }
                            int min = getYMinnum(ps);
                            int max1 = getYMaxnum(ps, min, 1);
                            int max2 = getYMaxnum(ps, min, 0);//表示最高高度
                            sports_maxheight.setText(max2 + "m");
                            sportdetail_LineView.setData(yList, xList, type_List, max1, min, (max1 - min) / 5, getXMaxnum(ps));
                            yList = null;
                            xList = null;
                            type_List = null;
                        }
                        ps = null;
                    } else {
                        //给一个默认数据
                        ArrayList<Double> yList = new ArrayList<Double>();
                        yList.add(0.0);
                        yList.add(0.0);
                        yList.add(0.0);
                        ArrayList<Double> xList = new ArrayList<Double>();
                        xList.add(0.01);
                        xList.add(dis * 2 / 5);
                        xList.add(dis);
                        ArrayList<String> type_List = new ArrayList<String>();
                        type_List.add("1");
                        type_List.add("1");
                        type_List.add("1");
                        sportdetail_LineView.setData(yList, xList, type_List, 5, 0, 1, dis);
                        yList = null;
                        xList = null;
                        type_List = null;

                    }
                    break;
                case 101:
                    if (psInfo != null) {
                        if (psInfo.getIshave() != -3) {
                            if (distance > 1.0) {
                                pesimg.setVisibility(View.VISIBLE);
                            } else {
                                pesimg.setVisibility(View.GONE);
                            }

                        } else {
                            pesimg.setVisibility(View.GONE);
                        }
                    } else {
                        pesimg.setVisibility(View.GONE);
                    }

                    break;
                case 20160118:
                    aback = (ApiBack) msg.obj;
                    if (aback != null) {
                        if (aback.getFlag() == 1) {
                            updateisSavalocal(send_markCode, 1 + "");
                            Toast.makeText(SportTaskDetailActivityGaode.this, "数据上传成功", Toast.LENGTH_SHORT).show();
                            uploadBtn.setVisibility(View.GONE);
                        } else {
                            Toast.makeText(SportTaskDetailActivityGaode.this, "数据上传失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;
                case -56:
                case -100:
                    Toast.makeText(
                            mContext,
                            getString(R.string.error_cannot_access_net),
                            Toast.LENGTH_SHORT).show();
                    break;
            }
        }

        ;
    };

    private void shareToFaXian() {
        if (Bimp.drr.size() < 9) {
            Bimp.drr.add(SHARE_PATH);
            Log.e(TAG, "SHARE_PATH : " + SHARE_PATH);
        }
        Intent intent = new Intent(this, FindFriendsSendMsg.class);
        startActivity(intent);
    }

    private void shareTo() {
        waitShowDialog();
        new saveDataToServer().execute();
    }

    private class saveDataToServer extends AsyncTask<Void, Void, Integer> {
        @Override
        protected Integer doInBackground(Void... arg0) {
            String img = mSportsApp.getSportUser().getUimg();
            int id = -1;

            if ((img.length() < 40)
                    || (!img.substring(img.lastIndexOf("/"), img.length())
                    .contains("."))) {
                img = "";
            }

            if (!img.equalsIgnoreCase("")) {
                img = img.substring(img.indexOf("/", 10));
            }

            try {
                id = ApiJsonParser.saveDataToServer(mSportsApp.getSportUser()
                                .getUname(), sessionId, String.valueOf(curUid),
                        SportTaskUtil.date2seconds(startTime), String
                                .valueOf(typeId), SportTaskUtil
                                .getDoubleNum(dis), String.valueOf(con),
                        SportTaskUtil.getDoubleNum(speed), SportTaskUtil
                                .showTimeCount(recLen), img, thisLarge,
                        "android");
            } catch (ApiNetException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return id;
        }

        @Override
        protected void onPostExecute(final Integer id) {
            super.onPostExecute(id);

            if (id >= 1) {
                new AsyncTask<Void, Void, String>() {

                    @Override
                    protected String doInBackground(Void... params) {
                        // TODO Auto-generated method stub
                        String imagUrl = ApiJsonParser.getShareIcon(curUid, id);
                        return imagUrl;
                    }

                    protected void onPostExecute(String result) {
                        if (result != null) {
                            new goShare().execute(result);
                        } else {
                            if (mLoadProgressDialog != null)
                                if (mLoadProgressDialog.isShowing())
                                    mLoadProgressDialog.dismiss();
                            Toast.makeText(SportTaskDetailActivityGaode.this,
                                    getString(R.string.sports_comment_neterror),
                                    Toast.LENGTH_LONG).show();
                        }

                    }
                }.execute();

            } else {
                if (mLoadProgressDialog != null)
                    if (mLoadProgressDialog.isShowing())
                        mLoadProgressDialog.dismiss();
                Toast.makeText(SportTaskDetailActivityGaode.this,
                        getString(R.string.sports_comment_neterror),
                        Toast.LENGTH_LONG).show();
            }
        }
    }

    ;

    private class goShare extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {
            // TODO Auto-generated method stub
            boolean saveFile = false;

            if (mSportsApp.isOpenNetwork()) {
                Tools.delAllFile(SHARE_PATH);

                if (Environment.getExternalStorageState().equals(
                        Environment.MEDIA_MOUNTED)) {
                    Log.e(TAG, "get map screen shot");

                    mDownloader = ImageDownloader.getInstance();
                    mDownloader.setType(ImageDownloader.OnlyOne);
                    Bitmap bitmap = mDownloader.downloadBitmap(params[0]);
                    if (bitmap != null) {
                        if (mGeoPoints != null && mGeoPoints.size() == 0) {
                            saveFile = Tools.SaveBitmapAsFile(SHARE_PATH, bitmap,
                                    40);
                        } else {
                            saveFile = Tools.SaveBitmapAsFile(SHARE_PATH, bitmap,
                                    90);
                        }

                    }
                } else {
                    Toast.makeText(SportTaskDetailActivityGaode.this,
                            getString(R.string.sd_card_is_invalid),
                            Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(SportTaskDetailActivityGaode.this,
                        getString(R.string.network_not_avaliable),
                        Toast.LENGTH_SHORT).show();
            }

            return saveFile;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            if (mLoadProgressDialog != null)
                if (mLoadProgressDialog.isShowing())
                    mLoadProgressDialog.dismiss();
            if (result) {
                if (SportTaskDetailActivityGaode.this.shareId == SHARE_XINLANG) {
                    shareToQQ();
                } else if ((SportTaskDetailActivityGaode.this.shareId == SHARE_WEIXIN) ||
                        (SportTaskDetailActivityGaode.this.shareId == SHARE_TENGXUN)) {
                    shareToNewWeixin();
                }
            }
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (null != mTencent)
            mTencent.onActivityResultData(requestCode, resultCode, data, this);
    }

    private void shareToQQ() {
        mTencent = Tencent.createInstance("1101732794", this.getApplicationContext());
        Bundle params = new Bundle();
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_LOCAL_URL, SHARE_PATH);
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_IMAGE);
        mTencent.shareToQQ(this, params, this);
    }

    private void shareToNewWeixin() {
        api = WXAPIFactory.createWXAPI(SportTaskDetailActivityGaode.this,
                WXEntryActivity.APP_ID, true);
        api.registerApp(WXEntryActivity.APP_ID);

        if (!api.isWXAppInstalled()) {
            Toast.makeText(getApplicationContext(),
                    getString(R.string.sports_shareto_weixin_no_weixin),
                    Toast.LENGTH_SHORT).show();
            return;
        } else if (api.getWXAppSupportAPI() < 0x21020001) {
            Toast.makeText(getApplicationContext(),
                    getString(R.string.sports_shareto_weixin_wrong_version),
                    Toast.LENGTH_SHORT).show();
            return;
        }


        WXImageObject imgObj = new WXImageObject();
        imgObj.setImagePath(SHARE_PATH);

        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = imgObj;
        msg.description = "图片描述";
        Bitmap bmp = BitmapFactory.decodeFile(SHARE_PATH);
        Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, 72, 123, true);
        bmp.recycle();
        msg.thumbData = Util.bmpToByteArray(thumbBmp, true);
        msg.title = "云狐运动";
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = "img" + String.valueOf(System.currentTimeMillis());
        req.message = msg;
        if (this.shareId == SHARE_WEIXIN) {
            req.scene = SendMessageToWX.Req.WXSceneSession;
        } else {
            req.scene = SendMessageToWX.Req.WXSceneTimeline;
        }
        api.sendReq(req);
    }

    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis())
                : type + System.currentTimeMillis();
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    // --------------------------------截屏-----------------------------------------//

    public Bitmap shot() {
        View view = getWindow().getDecorView();
        Display display = this.getWindowManager().getDefaultDisplay();
        Rect frame = new Rect();
        getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusheight = frame.top;// 手机状态栏高度
        view.setDrawingCacheEnabled(true);// 允许当前窗口保存缓存信息，这样getDrawingCache()方法才会返回一个Bitmap
        Bitmap bmp = Bitmap
                .createBitmap(
                        view.getDrawingCache(),
                        0,
                        header_layout.getHeight() + statusheight
                                + mMapView.getHeight(), display.getWidth(),
                        sportsinfo_layout.getHeight()
                );
        view.destroyDrawingCache();
        return bmp;
    }

    private Handler mDrawTrajectoryHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case DRAWTRAJECTORY:
                    if (mGeoPoints.size() > 1) {
                        drawMap();
                    }
                    break;
            }
        }
    };

    private void drawMap() {
        int size = mGeoPoints.size();
        List<LatLng> points = new ArrayList<LatLng>();
        if (size > 1) {
            if(speedList!=null&&mGeoPoints.size()!=speedList.size()&&speedList.size()>1){
                if(mGeoPoints.size()>speedList.size()){
                    int difSum=mGeoPoints.size()-speedList.size();
                    for(int i=1;i<=difSum;i++){
                        speedList.add("0");
                    }
                }else{
                    int difSum=speedList.size()-mGeoPoints.size();
                    for(int i=1;i<=difSum;i++){
                        speedList.remove(speedList.size()-1);
                    }
                }

            }

            LatLng point;
            for (int i = 0; i < size; i++) {
                point = mGeoPoints.get(i);
                if (point.latitude == SportTrajectoryUtilGaode.INVALID_LATLNG
                        || point.longitude == SportTrajectoryUtilGaode.INVALID_LATLNG) {
                    //画虚线
                    if (size >= 3) {
                        if (i != size - 1 && i > 0) {
                            if ((mGeoPoints.get(i - 1).latitude != SportTrajectoryUtilGaode.INVALID_LATLNG && mGeoPoints.get(i - 1).longitude != SportTrajectoryUtilGaode.INVALID_LATLNG)
                                    && (mGeoPoints.get(i + 1).latitude != SportTrajectoryUtilGaode.INVALID_LATLNG && mGeoPoints.get(i + 1).longitude != SportTrajectoryUtilGaode.INVALID_LATLNG)) {
                                PolylineOptions options = new PolylineOptions()
                                        .width(16).color(Color.argb(255, 218, 217, 216)).setDottedLine(true);
                                options.add(mGeoPoints.get(i - 1));
                                options.add(mGeoPoints.get(i + 1));
                                options.zIndex(12);
                                aMap.addPolyline(options);
                            }
                        }
                    }
                    if (points.size() >= 2) {
                        PolylineOptions options;
                        if (speedList == null || speedList.size() != mGeoPoints.size()) {
                            options = new PolylineOptions()
                                    .width(18).geodesic(true)
                                    .color(Color.argb(255, 242, 109, 27));
                        } else {
                            if(typeId!=SportsType.TYPE_CLIMBING){
                                options = new PolylineOptions()
                                        .width(18).useGradient(true);
                                options.colorValues(colorList);
                            }else{
                                options = new PolylineOptions()
                                        .width(18).geodesic(true)
                                        .color(Color.argb(255, 255, 174, 0));
                            }

                        }
                        options.addAll(points);
                        options.zIndex(12);
                        aMap.addPolyline(options);
                    }
                    points.clear();
                    colorList.clear();
                    if (i > 0 && i < size - 1) {
//                        MarkerOptions MarkerMiddleEnd = new MarkerOptions()
//                                .icon(BitmapDescriptorFactory
//                                        .fromBitmap(BitmapFactory
//                                                .decodeResource(getResources(),
//                                                        R.drawable.map_middle)));
//                        aMap.addMarker(MarkerMiddleEnd.position(mGeoPoints
//                                .get(i - 1)));
//
//                        MarkerOptions MarkerMiddleStart = new MarkerOptions()
//                                .icon(BitmapDescriptorFactory
//                                        .fromBitmap(BitmapFactory
//                                                .decodeResource(getResources(),
//                                                        R.drawable.map_middle)));
//                        aMap.addMarker(MarkerMiddleStart.position(mGeoPoints
//                                .get(i + 1)));
                    }
                } else {
                    points.add(point);
                    if (speedList != null && speedList.size() == mGeoPoints.size()) {
                        if (speedList.get(i) != null && !"".equals(speedList.get(i))) {
                            if (typeId == SportsType.TYPE_CLIMBING || typeId == SportsType.TYPE_WALK) {
                                if (Float.parseFloat(speedList.get(i)) >= 0 && Float.parseFloat(speedList.get(i)) < 0.42) {
                                    colorList.add(Color.GREEN);
                                }else if (Float.parseFloat(speedList.get(i)) >= 0.42 && Float.parseFloat(speedList.get(i)) < 0.83) {
                                    colorList.add(getResources().getColor(R.color.speed_line_color1));
                                } else if (Float.parseFloat(speedList.get(i)) >=0.83 && Float.parseFloat(speedList.get(i)) < 1.25) {
                                    colorList.add(Color.YELLOW);
                                }else if (Float.parseFloat(speedList.get(i)) >=1.25 && Float.parseFloat(speedList.get(i)) < 1.67) {
                                    colorList.add(getResources().getColor(R.color.speed_line_color2));
                                } else {
                                    colorList.add(Color.RED);
                                }
                            } else if (typeId == SportsType.TYPE_RUN) {
                                if (Float.parseFloat(speedList.get(i)) >= 0 && Float.parseFloat(speedList.get(i)) < 1.11) {
                                    colorList.add(Color.GREEN);
                                }else if (Float.parseFloat(speedList.get(i)) >= 1.11 && Float.parseFloat(speedList.get(i)) < 1.94) {
                                    colorList.add(getResources().getColor(R.color.speed_line_color1));
                                } else if (Float.parseFloat(speedList.get(i)) >=1.94 && Float.parseFloat(speedList.get(i)) < 2.78) {
                                    colorList.add(Color.YELLOW);
                                }else if (Float.parseFloat(speedList.get(i)) >=2.78 && Float.parseFloat(speedList.get(i)) < 3.33) {
                                    colorList.add(getResources().getColor(R.color.speed_line_color2));
                                } else {
                                    colorList.add(Color.RED);
                                }
                            } else {
                                if (Float.parseFloat(speedList.get(i)) >= 0 && Float.parseFloat(speedList.get(i)) < 2.5) {
                                    colorList.add(Color.GREEN);
                                }else if (Float.parseFloat(speedList.get(i)) >= 2.5 && Float.parseFloat(speedList.get(i)) < 5) {
                                    colorList.add(getResources().getColor(R.color.speed_line_color1));
                                } else if (Float.parseFloat(speedList.get(i)) >=5 && Float.parseFloat(speedList.get(i)) < 7.5) {
                                    colorList.add(Color.YELLOW);
                                }else if (Float.parseFloat(speedList.get(i)) >=7.5 && Float.parseFloat(speedList.get(i)) < 9.72) {
                                    colorList.add(getResources().getColor(R.color.speed_line_color2));
                                } else {
                                    colorList.add(Color.RED);
                                }
                            }

                        }
                    }
                    if (i == size - 1 && points.size() >= 2) {
                        PolylineOptions options;
                        if (speedList == null || speedList.size() != mGeoPoints.size()) {
                            options = new PolylineOptions()
                                    .width(18).geodesic(true)
                                    .color(Color.argb(255, 242, 109, 27));
                        } else {
                            if(typeId!=SportsType.TYPE_CLIMBING){
                                options = new PolylineOptions()
                                        .width(18).useGradient(true);
                                options.colorValues(colorList);
                            }else{
                                options = new PolylineOptions()
                                        .width(18).geodesic(true)
                                        .color(Color.argb(255, 255, 174, 0));
                            }

                        }

                        options.addAll(points);
                        options.zIndex(12);
                        aMap.addPolyline(options);
                    }
                }
            }
        }
    }

    // //合成图片,src在上面,mark在src的下面
    private Bitmap syntheticImagesBitmap(Bitmap src, Bitmap mark) {

        int w = src.getWidth();
        int h = src.getHeight();
        int wh = mark.getHeight();
        Bitmap newb = Bitmap.createBitmap(w, h + wh, Config.ARGB_8888);
        Canvas cv = new Canvas(newb);
        markBitmap = BitmapFactory.decodeResource(getResources(),
                R.drawable.sport_mark);
        sports_marklayout.buildDrawingCache();
        sports_marklayout.setDrawingCacheEnabled(true);
        markBitmap = sports_marklayout.getDrawingCache();// 将线性布局中的整个内容转换为位图


        cv.drawBitmap(src, 0, 0, null);
        cv.drawBitmap(markBitmap, SportsApp.dip2px(10), SportsApp.dip2px(15), null);

        if(speedline_layout!=null&&speedline_layout.getVisibility()!=View.GONE){
            speedline_layout.buildDrawingCache();
            speedline_layout.setDrawingCacheEnabled(true);
            lineBitmap = speedline_layout.getDrawingCache();// 将线性布局中的整个内容转换为位图
            cv.drawBitmap(lineBitmap, 0, h-SportsApp.dip2px(50), null);
        }
        cv.drawBitmap(mark, 0, h, null);
        cv.save(Canvas.ALL_SAVE_FLAG);
        cv.restore();
        if (src != null && !src.isRecycled()) {
            src.recycle();
            src = null;
        }
        if (mark != null && !mark.isRecycled()) {
            mark.recycle();
            mark = null;
        }
        return newb;
    }

    // //合成图片,src在上面 view放在src上面
    private Bitmap syntheticImagesBitmap(Bitmap src) {
        Canvas cv = new Canvas(src);
        markBitmap = BitmapFactory.decodeResource(getResources(),
                R.drawable.sport_mark);
        sports_marklayout.buildDrawingCache();
        sports_marklayout.setDrawingCacheEnabled(true);
        markBitmap = sports_marklayout.getDrawingCache();// 将线性布局中的整个内容转换为位图
//        cv.drawBitmap(src, 0, 0, null);
        cv.drawBitmap(markBitmap, SportsApp.dip2px(10), SportsApp.dip2px(15), null);
        cv.save(Canvas.ALL_SAVE_FLAG);
        cv.restore();
//        if (src != null && !src.isRecycled()) {
//            src.recycle();
//            src = null;
//        }
        return src;
    }

    // //合成图片,现在地图是整个页面需要把下面的数据的高度去掉
    private Bitmap cutMapBitmap(Bitmap mapBitMap, int mHeight) {

        int w = mapBitMap.getWidth();
        int h = mapBitMap.getHeight();
        Bitmap newb = Bitmap.createBitmap(w, h -mHeight, Config.ARGB_8888);
        Canvas cv = new Canvas(newb);
        cv.drawBitmap(mapBitMap, 0, 0, null);
        cv.save(Canvas.ALL_SAVE_FLAG);
        cv.restore();
        if (mapBitMap != null && !mapBitMap.isRecycled()) {
            mapBitMap.recycle();
            mapBitMap = null;
        }
        return newb;
    }

    // 更新本地数据库
    private void update() {
        String sportDate = startTime.substring(0, 10);

        db = SportSubTaskDB.getInstance(getApplicationContext());
        ContentValues values = new ContentValues();
        values.put(SportSubTaskDB.UID, uid);
        values.put(SportSubTaskDB.LIMIT, 0);
        values.put(SportSubTaskDB.SPORT_TYPE, typeId);
        values.put(SportSubTaskDB.SPORT_SWIM_TYPE, typeDetailId);
        values.put(SportSubTaskDB.SPORT_DEVICE, deviceId);
        values.put(SportSubTaskDB.SPORT_START_TIME, startTime);
        values.put(SportSubTaskDB.SPORT_TIME, recLen);
        values.put(SportSubTaskDB.SPORT_STEP, step);
        values.put(SportSubTaskDB.SPORT_DISTANCE, dis);
        values.put(SportSubTaskDB.SPORT_CALORIES, con);
        values.put(SportSubTaskDB.SPORT_SPEED, speed);
        values.put(SportSubTaskDB.SPORT_HEART_RATE, heart);
        values.put(SportSubTaskDB.SPORT_ISUPLOAD, isUpload);
        values.put(SportSubTaskDB.SPORT_DATE, sportDate);
        values.put(SportSubTaskDB.SPORT_TASKID, taskID);
        values.put(SportSubTaskDB.SPORT_LAT_LNG, pointsStr);
        values.put(SportSubTaskDB.SPORT_MAPTYPE, mapType);
        values.put(SportSubTaskDB.SPORT_MARKCODE, mark_code);// 新增唯一标识码
        if (mark_code != null && !"".equals(mark_code)) {
            db.newUpdate(values, startTime, mark_code, uid);
        } else {
            db.update(values, startTime, uid);
        }
    }

    private void animateTo(LatLng point) {
        aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(point, zoomValue), null);
    }

    public void getMapScreenShot() {
        aMap.getMapScreenShot(this);
    }

    @Override
    public void onMapScreenShot(Bitmap bitmap) {
        boolean saveFile = false;

        if (bitmap != null) {
            Bitmap wbitmap = null;
//            Bitmap wbitmap1=null;
//            wbitmap1=cutMapBitmap(bitmap,sportsinfo_layout.getHeight());
            if (SHARE_WEIXIN == shareId || SHARE_XINLANG == shareId
                    || SHARE_TENGXUN == shareId) {

                if (typeId == SportsType.TYPE_CLIMBING) {
                    wbitmap = syntheticImagesBitmap(bitmap, shot());
                    saveFile = Tools.SaveBitmapAsFile(SHARE_PATH, wbitmap, 90);
                } else {
                    wbitmap = syntheticImagesBitmap(bitmap);
                    saveFile = Tools.SaveBitmapAsFile(SHARE_PATH, wbitmap, 90);
                }
            } else {
                wbitmap = syntheticImagesBitmap(bitmap, shot());
                if (SHAREPEI_SU == shareId) {
                    saveFile = Tools.SaveBitmapAsFile(SHARE_PATH, wbitmap);
                } else {
                    saveFile = Tools.SaveBitmapAsFile(SHARE_PATH, wbitmap);
                }
            }

            if (saveFile) {
                if (wbitmap != null && !wbitmap.isRecycled()) {
                    wbitmap.recycle();
                    wbitmap = null;
                }
//                if (wbitmap1 != null && !wbitmap1.isRecycled()) {
//                    wbitmap1.recycle();
//                    wbitmap1 = null;
//                }
                if (bitmap != null && !bitmap.isRecycled()) {
                    bitmap.recycle();
                    bitmap = null;
                }
                Message msg = sportHandler.obtainMessage(1209);
                msg.arg1 = shareId;
                sportHandler.sendMessage(msg);
            } else {
                Log.e("weixinshare", "save bitmap as file fail");
            }
        } else {
            Log.e("weixinshare", "bitmap is null");
        }
    }

    @Override
    public void onMapScreenShot(Bitmap bitmap, int i) {

    }

    @Override
    public void onCancel() {
        // TODO Auto-generated method stub

    }

    @Override
    public void onComplete(Object arg0) {
        // TODO Auto-generated method stub
        Toast.makeText(this, "QQ分享成功", Toast.LENGTH_SHORT).show();
        new AddCoinsThread(10, 4, new Handler() {
            @Override
            public void handleMessage(Message msg) {
                // TODO Auto-generated method
                // stub
                switch (msg.what) {
                    case ApiConstant.COINS_SUCCESS:
                        SportTaskUtil.jump2CoinsDialog(SportTaskDetailActivityGaode.this,
                                getString(R.string.shared_success_add_coins));
                        break;
                    case ApiConstant.COINS_LIMIT:
                        Toast.makeText(SportTaskDetailActivityGaode.this,
                                getString(R.string.shared_beyond_10times),
                                Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
            }
        }, -1).start();
    }

    @Override
    public void onError(UiError arg0) {
        // TODO Auto-generated method stub
        Toast.makeText(SportTaskDetailActivityGaode.this, "分享失败", Toast.LENGTH_LONG).show();
    }

    // private void sharePopWindow() {
    // if (shareWindow != null && shareWindow.isShowing())
    // return;
    // LayoutInflater inflater = LayoutInflater.from(this);
    // myView = (LinearLayout) inflater.inflate(R.layout.sports_dialog2, null);
    // myView.findViewById(R.id.faxian_layout).setOnClickListener(this);
    // myView.findViewById(R.id.weixin_layout).setOnClickListener(this);
    // myView.findViewById(R.id.tengxun_layout).setOnClickListener(this);
    // myView.findViewById(R.id.xinlang_layout).setOnClickListener(this);
    // myView.findViewById(R.id.share_cacle_txt).setOnClickListener(this);
    // // myEditCalories
    // // .setText(String.valueOf(getDate.getInt("editCalories", 0)));
    // WindowManager wm = (WindowManager)
    // getSystemService(Context.WINDOW_SERVICE);
    //
    // int width = wm.getDefaultDisplay().getWidth();
    // // int height = wm.getDefaultDisplay().getHeight();
    // shareWindow = new PopupWindow(myView, width - SportsApp.dip2px(20),
    // LinearLayout.LayoutParams.WRAP_CONTENT, true);
    // shareWindow.setAnimationStyle(R.style.AnimationPopup);
    // shareWindow.setOutsideTouchable(true);
    // shareWindow.setBackgroundDrawable(new BitmapDrawable());
    // shareWindow.showAtLocation(sport_map_finish, Gravity.CENTER, 0, 0);
    // // shareWindow.setOnDismissListener(this);
    // // final Animation animation = (Animation) AnimationUtils.loadAnimation(
    // // this, R.anim.slide_in_from_bottom);
    // // myView.startAnimation(animation);
    // // mPopMenuBack.setVisibility(View.VISIBLE);
    // }


    private void waitShowDialog() {
        // TODO Auto-generated method stub
        if (mLoadProgressDialog == null) {
            mLoadProgressDialog = new Dialog(this,
                    R.style.sports_dialog);
            LayoutInflater mInflater = getLayoutInflater();
            View v1 = mInflater.inflate(R.layout.bestgirl_progressdialog, null);
            TextView mDialogMessage = (TextView) v1.findViewById(R.id.message);
            mDialogMessage.setText(R.string.bestgirl_wait);
            v1.setMinimumWidth((int) (SportsApp.ScreenWidth * 0.8));
            mLoadProgressDialog.setContentView(v1);
        }
        if (mLoadProgressDialog != null)
            if (!mLoadProgressDialog.isShowing())
                mLoadProgressDialog.show();
    }

    //从服务器端获取登山信息
    private void getPeisuData(int flag) {
        if (flag == 1) {
            PeisuInfo peisuInfo = ApiJsonParser.getPeisu(mSportsApp.getSessionId(), taskID, typeId);
            Message msg = sportHandler.obtainMessage(10009);
            msg.arg1 = shareId;
            msg.obj = peisuInfo;
            sportHandler.sendMessage(msg);
        } else if (flag == 2) {
            psInfo = ApiJsonParser.getPeisu(mSportsApp.getSessionId(), taskID, typeId);
            sportHandler.sendEmptyMessage(101);
        }

    }


    //配速或登山存本地
    private int savePeisu(List<GetPeisu> listpeis, String bs) {
        int savainsert = 0;
        ArrayList<String> peiSuList1, peiSuList2, peiSuList3, peiSuList4;
        peiSuList1 = new ArrayList<String>();
        peiSuList2 = new ArrayList<String>();
        peiSuList3 = new ArrayList<String>();
        peiSuList4 = new ArrayList<String>();
        for (int i = 0; i < listpeis.size(); i++) {
            peiSuList1.add(listpeis.get(i).getSport_distance());
            peiSuList2.add(listpeis.get(i).getSprots_velocity());
            peiSuList3.add(listpeis.get(i).getSprots_time());
            peiSuList4.add(listpeis.get(i).getgPS_type());
        }
        ContentValues c = new ContentValues();
        c.put(PeisuDB.GongLi, SportTrajectoryUtilGaode.peiListToString(peiSuList1));
        c.put(PeisuDB.Peisu, SportTrajectoryUtilGaode.peiListToString(peiSuList2));
        c.put(PeisuDB.Time, SportTrajectoryUtilGaode.peiListToString(peiSuList3));
        c.put(PeisuDB.GPS_TYPE, SportTrajectoryUtilGaode.peiListToString(peiSuList4));
        c.put(PeisuDB.SPORT_MARKCODE, bs);
        c.put(PeisuDB.SPORT_ISUPLOAD, 1 + "");
        savainsert = peisuDB.insert(c);

        return savainsert;
    }

    //获取配速或者登山的信息
    private static class GetPeisuThread extends Thread {
        WeakReference<SportTaskDetailActivityGaode> mThreadActivityRef;
        private int flag;//1代表登山2配速

        public GetPeisuThread(SportTaskDetailActivityGaode activity, int flag) {
            mThreadActivityRef = new WeakReference<SportTaskDetailActivityGaode>(activity);
            this.flag = flag;
        }

        @Override
        public void run() {
            super.run();
            if (mThreadActivityRef == null)
                return;
            if (mThreadActivityRef.get() != null) {
                mThreadActivityRef.get().getPeisuData(flag);
            }
        }
    }


//    private static class MyDtailHandler extends Handler{
//        private final WeakReference<SportTaskDetailActivityGaode> mActivity;
//        public MyDtailHandler(SportTaskDetailActivityGaode activity){
//            mActivity= new WeakReference<SportTaskDetailActivityGaode>(activity);
//        }
//        @Override
//        public void handleMessage(Message msg) {
//            if (mActivity.get() == null) {
//                return;
//            }
//            mActivity.get().toHandler(msg);
//            super.handleMessage(msg);
//        }
//    }
//
//    private void toHandler(Message msg){
//        switch (msg.what) {
//            case UPLOAD_START:
//                if (mDialog != null && mDialog.isShowing()) {
//                    mDialog.dismiss();
//                }
//                ApiBack startBack = (ApiBack) msg.obj;
//                if (startBack != null && startBack.getFlag() == 1) {
//                    if (fromID == 1) {
//                        update();
//                    }
//                    Toast.makeText(mContext,
//                            getString(R.string.upload_success),
//                            Toast.LENGTH_SHORT).show();
//                    if (!qq_health && (isUpload == 1 || isUpload == 2) && isUpload_dengshan == 1) {
//                        uploadBtn.setVisibility(View.GONE);
//                    } else {
//                        uploadBtn.setVisibility(View.VISIBLE);
//                    }
//                } else {
//                    Toast.makeText(mContext, getString(R.string.upload_failed),
//                            Toast.LENGTH_SHORT).show();
//                }
//                break;
//            case UPLOAD_UPDATE:
//                if (mDialog != null && mDialog.isShowing()) {
//                    mDialog.dismiss();
//                }
//                ApiBack updateBack = (ApiBack) msg.obj;
//                if (updateBack != null && updateBack.getFlag() == 1) {
//                    if (fromID == 1) {
//                        update();
//                    }
//                    Toast.makeText(mContext,
//                            getString(R.string.upload_success),
//                            Toast.LENGTH_SHORT).show();
//                    if (!qq_health && (isUpload == 1 || isUpload == 2) && isUpload_dengshan == 1) {
//                        uploadBtn.setVisibility(View.GONE);
//                    } else {
//                        uploadBtn.setVisibility(View.VISIBLE);
//                    }
//                } else {
//                    Toast.makeText(mContext, getString(R.string.upload_failed),
//                            Toast.LENGTH_SHORT).show();
//                }
//                break;
//            case UPLOAD_REQUEST:
//                if (mDialog != null && mDialog.isShowing()) {
//                    mDialog.dismiss();
//                }
//                if (mContext == null)
//                    return;
//                SportTask task = (SportTask) msg.obj;
//                if (task != null) {
//                    initFromSportDetail(task);
//                    fromID = 2;
//                } else {
//                    Toast.makeText(mContext,
//                            getString(R.string.sports_data_load_failed),
//                            Toast.LENGTH_SHORT).show();
//                }
//                break;
//            case UPLOAD_FAIL:
//                if (mDialog != null && mDialog.isShowing()) {
//                    mDialog.dismiss();
//                }
//                Toast.makeText(mContext,
//                        getString(R.string.sports_access_timeout),
//                        Toast.LENGTH_SHORT).show();
//                break;
//            case UPLOAD_REQUEST_FAIL:
//                if (mDialog != null && mDialog.isShowing()) {
//                    mDialog.dismiss();
//                }
//                if (msg.arg1 == UPLOAD_REQUEST) {
//                    Toast.makeText(mContext,
//                            getString(R.string.sports_get_data_fail),
//                            Toast.LENGTH_SHORT).show();
//                } else if (msg.arg1 == UPLOAD_START) {
//                    Toast.makeText(mContext, getString(R.string.upload_failed),
//                            Toast.LENGTH_SHORT).show();
//                } else if (msg.arg1 == UPLOAD_UPDATE) {
//                    Toast.makeText(mContext, getString(R.string.upload_failed),
//                            Toast.LENGTH_SHORT).show();
//                } else if (msg.arg1 == UPLOAD_DELETE) {
//                    Toast.makeText(mContext,
//                            getString(R.string.sports_delete_failed),
//                            Toast.LENGTH_SHORT).show();
//                }
//                break;
//            case DELETE_FILE:
//                if (Environment.getExternalStorageState().equals(
//                        Environment.MEDIA_MOUNTED)) {
//                    getMapScreenShot();
//                } else {
//                    if (mDialog != null)
//                        mDialog.dismiss();
//                    Toast.makeText(mContext,
//                            getString(R.string.sd_card_is_invalid), Toast.LENGTH_SHORT)
//                            .show();
//                }
//                break;
//            case 1209:
//                switch (shareId) {
//                    case SHARE_XINLANG:
//                        shareTo();
//                        break;
//                    case SHARE_TENGXUN:
//                        shareTo();
//                        break;
//                    case SHARE_WEIXIN:
//                        shareTo();
//                        break;
//                    case SHARE_FAXIAN:
//                        shareToFaXian();
//                        break;
//                    case SHAREPEI_SU:
//                        Intent intent = new Intent(SportTaskDetailActivityGaode.this, PeiSuActivity.class);
//                        intent.putExtra("bs", mark_code);
//                        intent.putExtra("taskid", taskID);
//                        intent.putExtra("typeId", typeId);
//                        intent.putExtra("imgurl", SHARE_PATH);
//                        startActivity(intent);
//                        break;
//
//                }
//                if (mDialog != null && mDialog.isShowing()) {
//                    mDialog.dismiss();
//                }
//                break;
//            case UPLOAD_DELETE:
//                if (mDialog != null && mDialog.isShowing()) {
//                    mDialog.dismiss();
//                }
//                ApiBack delBack = (ApiBack) msg.obj;
//                if (delBack != null && delBack.getFlag() == 1) {
//                    Toast.makeText(mContext,
//                            getString(R.string.sports_delete_successed),
//                            Toast.LENGTH_SHORT).show();
//                    Intent intent = new Intent();
//                    setResult(RESULT_OK, intent);
//                    finish();
//                } else {
//                    Toast.makeText(mContext,
//                            getString(R.string.sports_delete_failed),
//                            Toast.LENGTH_SHORT).show();
//                }
//                break;
//            case 10009:
//                //表示获取登山信息
//                PeisuInfo peisuInfo=(PeisuInfo)msg.obj;
//                if(peisuInfo!=null&&peisuInfo.getListpeis()!=null&&peisuInfo.getListpeis().size()>0){
//                    savePeisu(peisuInfo.getListpeis(),mark_code);
//                    List<GetPeisu> ps = peisuInfo.getListpeis();
//                    if (ps != null && ps.size() > 0) {
//                        ArrayList<Double> yList = new ArrayList<Double>();
//                        ArrayList<Double> xList = new ArrayList<Double>();
//                        ArrayList<String> type_List = new ArrayList<String>();
//                        for (int i = 0; i < ps.size(); i++) {
//                            yList.add(Double.parseDouble(ps.get(i).getSprots_velocity()));
//                            xList.add(Double.parseDouble(ps.get(i).getSport_distance()));
//                            type_List.add(ps.get(i).getgPS_type());
//                        }
//                        int min = getYMinnum(ps);
//                        int max1 = getYMaxnum(ps, min, 1);
//                        int max2 = getYMaxnum(ps, min, 0);//表示最高高度
//                        sports_maxheight.setText(max2 + "m");
//                        sportdetail_LineView.setData(yList, xList,type_List, max1, min, (max1-min) / 5, getXMaxnum(ps));
//                        yList=null;
//                        xList=null;
//                        type_List=null;
//                    }
//                    ps=null;
//                }else{
//                    //给一个默认数据
//                    ArrayList<Double> yList = new ArrayList<Double>();
//                    yList.add(0.0);
//                    yList.add(0.0);
//                    yList.add(0.0);
//                    ArrayList<Double> xList = new ArrayList<Double>();
//                    xList.add(0.01);
//                    xList.add(dis*2/5);
//                    xList.add(dis);
//                    ArrayList<String> type_List = new ArrayList<String>();
//                    type_List.add("1");
//                    type_List.add("1");
//                    type_List.add("1");
//                    sportdetail_LineView.setData(yList, xList,type_List, 5, 0, 1, dis);
//                    yList=null;
//                    xList=null;
//                    type_List=null;
//
//                }
//                break;
//            case 101:
//                if(psInfo!=null){
//                    if (psInfo.getIshave() != -3) {
//                        if (distance > 1.0) {
//                            pesimg.setVisibility(View.VISIBLE);
//                        } else {
//                            pesimg.setVisibility(View.GONE);
//                        }
//
//                    } else {
//                        pesimg.setVisibility(View.GONE);
//                    }
//                }else{
//                    pesimg.setVisibility(View.GONE);
//                }
//
//                break;
//            case 20160118:
//                aback = (ApiBack) msg.obj;
//                if (aback != null) {
//                    if (aback.getFlag() == 1) {
//                        updateisSavalocal(send_markCode, 1+"");
//                        Toast.makeText(SportTaskDetailActivityGaode.this, "数据上传成功", Toast.LENGTH_SHORT).show();
//                        uploadBtn.setVisibility(View.GONE);
//                    } else {
//                        Toast.makeText(SportTaskDetailActivityGaode.this, "数据上传失败", Toast.LENGTH_SHORT).show();
//                    }
//                }
//                break;
//        }
//
//    }

    //添加地图背景
    private void addBgMarker(LatLng endPoint, int icon) {
        GroundOverlayOptions groundOverlayOptions=new GroundOverlayOptions()
                .position(endPoint, 2 * 1000 * 1000, 2 * 1000 * 1000)
                .image(BitmapDescriptorFactory.fromBitmap(BitmapFactory
                        .decodeResource(this.getResources(), icon)));
        groundOverlayOptions.zIndex(10);
        aMap.addGroundOverlay(groundOverlayOptions);
    }



    //自定义每公里的标记icon
    private View getDisIcon(int dis) {
        TextView textView = null;
        textView = new TextView(this);
        textView.setGravity(Gravity.CENTER);
        textView.setText(dis + "");
        textView.setTextColor(Color.WHITE);
        textView.setBackgroundResource(R.drawable.train_sort_img);
        textView.setWidth(SportsApp.dip2px(20));
        textView.setHeight(SportsApp.dip2px(20));
        return textView;
    }

    //计算出每公里的点
    private void setDisIcon() {
        int tempSumDis=0;
        LatLng points;
        for (SportsMarkDis sportsMarkDis:sportsMarkDisList) {
            tempSumDis=sportsMarkDis.dis;
            points=new LatLng(sportsMarkDis.mLat,sportsMarkDis.mLng);
            everyDisList.add(new MarkerOptions().icon(BitmapDescriptorFactory
                    .fromView(getDisIcon(tempSumDis))).position(points).zIndex(10));
        }
    }
    //是否显示每公里的标示
    private void isShowEveryDis(){
        if(everyDisList!=null&&everyDisList.size()>0){
            if(isDisShow){
                if(everyMarkList!=null&&everyMarkList.size()>0){
                    for (Marker marker :everyMarkList){
                        marker.remove();
                    }
                }
            }else{
                for (int i=0;i<everyDisList.size();i++){
                    if(everyMarkList!=null){
                        everyMarkList.add(aMap.addMarker(everyDisList.get(i)));
                    }
                }
            }
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

    /**
     *@method 超速弹出框
     *@author suhu
     *@time 2016/11/16 15:32
     *@param
     *
    */
    private void speedLimitPopWindow() {
        WindowManager.LayoutParams lp = this.getWindow()
                .getAttributes();
        lp.alpha = 0.3f;
        this.getWindow().setAttributes(lp);

        if (myWindow != null && myWindow.isShowing())
            return;
        LayoutInflater inflater = LayoutInflater.from(this);
        myView = (RelativeLayout) inflater.inflate(R.layout.dialog_sport, null);

        myView.findViewById(R.id.button_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (myWindow != null) {
                    myWindow.dismiss();
                }
                myView.setVisibility(View.GONE);
                mPopMenuBack.setVisibility(View.GONE);

            }
        });
        myWindow = new PopupWindow(myView, SportsApp.dip2px(239),
                SportsApp.dip2px(236), true);
        ColorDrawable cd = new ColorDrawable(0x000000);
        myWindow.setBackgroundDrawable(cd);
        myWindow.setTouchable(true);
        myWindow.setOutsideTouchable(true);
        myWindow.setBackgroundDrawable(new BitmapDrawable());
        myWindow.update();
        myWindow.showAtLocation(rl_sport, Gravity.CENTER, 0, 0);
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

    /**
     * 计算速度的最大值和最小值
     */
    private void caculateSpeedNum(){
        double lowNum=1000;
        double fastNum=Float.parseFloat(speedList.get(0));
        for (int i=0;i<speedList.size();i++){
            if(Float.parseFloat(speedList.get(i))>=fastNum){
                fastNum=Float.parseFloat(speedList.get(i));
            }
            if(Float.parseFloat(speedList.get(i))!=0&&Float.parseFloat(speedList.get(i))<=lowNum){
                lowNum=Float.parseFloat(speedList.get(i));
            }

        }
        if(lowNum!=1000&&lowNum!=0&&fastNum!=0){
            double lowPeisuNum =3600/(lowNum*3.6f);
            double fastPeisuNum =3600/(fastNum*3.6f);
            StringBuilder lowStringBuilder=new StringBuilder();
            StringBuilder fastStringBuilder=new StringBuilder();
            lowStringBuilder.append((int) (lowPeisuNum / 60)).append("'").append((int) (lowPeisuNum % 60)).append("\"");
            fastStringBuilder.append((int) (fastPeisuNum / 60)).append("'").append((int) (fastPeisuNum % 60)).append("\"");
            speedlow_tx.setText(getString(R.string.speed_low)+" "+lowStringBuilder.toString());
            speedfast_tx.setText(getString(R.string.speed_fast)+" "+fastStringBuilder.toString());
        }
        GradientDrawable gradientDrawable=new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT,new int[]{Color.GREEN,
                getResources().getColor(R.color.speed_line_color1),Color.YELLOW,getResources().getColor(R.color.speed_line_color2),Color.RED});
        speed_line.setBackgroundDrawable(gradientDrawable);
        gradientDrawable=null;
    }


}
