package com.fox.exercise.newversion.newact;

import java.util.ArrayList;
import java.util.List;

import cn.ingenic.indroidsync.SportsApp;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.UiSettings;
import com.baidu.mapapi.map.BaiduMap.OnMapLoadedCallback;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.model.LatLng;
import com.fox.exercise.AbstractBaseOtherActivity;
import com.fox.exercise.FansAndNear;
import com.fox.exercise.ImageDownloader;
import com.fox.exercise.NearByListViewAdapter;
import com.fox.exercise.R;
import com.fox.exercise.SportExceptionHandler;
import com.fox.exercise.SportsExceptionHandler;
import com.fox.exercise.StateActivity;
import com.fox.exercise.api.ApiBack;
import com.fox.exercise.api.ApiConstant;
import com.fox.exercise.api.ApiJsonParser;
import com.fox.exercise.api.ApiNetException;
import com.fox.exercise.api.ApiSessionOutException;
import com.fox.exercise.api.entity.UserDetail;
import com.fox.exercise.api.entity.UserNearby;
import com.fox.exercise.http.FunctionStatic;
import com.fox.exercise.login.LocationInfo;
import com.fox.exercise.newversion.act.PersonalPageMainActivity;
import com.fox.exercise.util.RoundedImage;
import com.fox.exercise.view.PullToRefreshListView;
import com.fox.exercise.view.SwitchView;
import com.fox.exercise.view.PullToRefreshBase.OnRefreshListener;
import com.umeng.analytics.MobclickAgent;

import android.os.Handler;
import android.os.Message;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ZoomControls;
import android.widget.AdapterView.OnItemClickListener;

/**
 * @author loujungang 百度地图约跑activity
 */
public class NewYuePaoBaiDuActivity extends AbstractBaseOtherActivity implements OnClickListener {

    private MapView mMapView = null;
    private BaiduMap aMap;
    private LocationClient mLocClient = null;
    public MyLocationListenner myListener;
    private Marker mMarkerView = null;
    private UiSettings mUiSettings;
    private Context mContext;
    private MarkerOptions mMarkerOpStart;
    //	private Bundle savedInstanceState;
    private int times = 0;
    private PullToRefreshListView mPullListView = null;
    private ListView mListView = null;
    private NearByListViewAdapter mAdapter = null;
    private Dialog loadProgressDialog = null;
    private TextView mDialogMessage = null;
    private View rootView;
    private boolean startAddMore = false;
    private ArrayList<UserNearby> mList = new ArrayList<UserNearby>();
    private List<UserNearby> mNearbayView = new ArrayList<UserNearby>();
    private List<Marker> mNearbayMark = new ArrayList<Marker>();
    // private ArrayList<UserNearby> mListnew = new ArrayList<UserNearby>();

    private NearByHandler mhandler = null;

    private static final int FRESH_LIST_NEW = 0x0005;
    private static final int FRESH_LIST = 0x0001;
    private static final int FRESH_FAILED = 0x0002;
    private static final int LOCATE_FINISH = 0x0003;
    private static final int LOCATE_FAILED = 0x0004;
    private static final int MARKER_LOAD = 0x0006;
    private static final int LEFT_TITLE_VIEW = 111;
    private static final int LEFT_TITLE_BUTTON = 112;
    //	private int  neatbyNum = 0;
    private boolean nearbyBool = false;
    private UserDetail self = null;
    private SportsApp mSportsApp;
    private EditText sports_nearby_edittext;
    private String editname;
    private int mSearchNearByName;
    private final int SEARCH_BASIC = 0;
    private final int SEARCH_NAME = 1;
    private int edittextCnt = 0;
    private long preTime = 0;
    private boolean initSuccess = false;
    private boolean uploadLocSuccess = false;
    //根据性别筛选的下拉菜单
//	private TextView sexTextView;
//	private ImageView  searchBysexButton;
    private int sex;//0-全部，1-男，2-女
    //	private String[] sexshowStrings={"全部","男","女"};
    public PopupWindow mPopWindow = null;
    //	private LayoutInflater mapInflater;
    private SwitchView switch_MAIN;
    private RelativeLayout nearbyMap;//,nearbyGaoDeMap
    int mapNum = 0;
    private SportExceptionHandler mExceptionHandler = null;

    @Override
    public void initIntentParam(Intent intent) {
        // TODO Auto-generated method stub
        title = getResources().getString(R.string.about_run);
    }

    @Override
    public void initView() {
        // TODO Auto-generated method stub

    }

    @Override
    public void setViewStatus() {
        // TODO Auto-generated method stub
        showContentView(R.layout.sports_nearby);
        mContext = this;
        left_ayout.setId(LEFT_TITLE_VIEW);
        left_ayout.setOnClickListener(this);
        leftButton.setId(LEFT_TITLE_BUTTON);
        leftButton.setOnClickListener(this);
//		((MainFragmentActivity) getActivity()).nerActivity = this;
//		MainFragmentActivity parentActivity = (MainFragmentActivity) getActivity();
//		ResideMenu resideMenu = parentActivity.getResideMenu();
//		RelativeLayout ignored_view = (RelativeLayout) parentView
//				.findViewById(R.id.nearbyActivity_baidu);
//		resideMenu.addIgnoredView(ignored_view);
//		mapInflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mSportsApp = (SportsApp) getApplication();
        mExceptionHandler = new SportExceptionHandler(this);
        mSportsApp.mNotificationManager.cancel(100);
        mSportsApp.addActivity(this);
        self = mSportsApp.getSportUser();

        nearbyMap = (RelativeLayout) findViewById(R.id.nearby_map);
        nearbyMap.setVisibility(View.GONE);
        // //加载框
        loadProgressDialog = new Dialog(mContext, R.style.sports_dialog);
        LayoutInflater mInflater = getLayoutInflater();
        View v1 = mInflater.inflate(R.layout.sports_progressdialog, null);
        mDialogMessage = (TextView) v1.findViewById(R.id.message);
        mDialogMessage.setText(R.string.sports_location_wait);
        v1.setMinimumWidth((int) (SportsApp.ScreenWidth * 0.8));
        loadProgressDialog.setContentView(v1);
        loadProgressDialog.setCanceledOnTouchOutside(false);
        Log.i(TAG, "nearby created");
        // handler更新
        mhandler = new NearByHandler();
        init();

        mPullListView = (PullToRefreshListView) findViewById(
                R.id.nearby_pull_refresh_list_baidu);
        // searchEdittext监听
        mListView = mPullListView.getRefreshableView();
        mListView.setCacheColorHint(0x00000000);
        mListView.setDivider(getResources().getDrawable(R.drawable.sports_bg_line));
        View search_view = LayoutInflater.from(mContext).inflate(
                R.layout.friends_list_front_view, null);
        sports_nearby_edittext = (EditText) search_view.findViewById(
                R.id.add_friend_edittext);
        mListView.addHeaderView(search_view);
        sports_nearby_edittext
                .addTextChangedListener(nearby_edittextChangedListener);
        mListView.setOnItemClickListener(listViewClick);
        mPullListView.setOnRefreshListener(new OnRefreshListener() {

            @Override
            public void onRefresh(int pullDirection) {
                if (mSportsApp.isOpenNetwork()) {
                    switch (pullDirection) {
                        case FansAndNear.MODE_DEFAULT_LOAD:
                            times++;
                            GetNearByThread loadThread = new GetNearByThread();
                            loadThread.start();
                            nearbyBool = true;
                            break;
                        case FansAndNear.MODE_PULL_DOWN_TO_REFRESH:
                            times = 0;
                            GetNearByThread refreshThread = new GetNearByThread();
                            refreshThread.start();
                            nearbyBool = true;
                            break;
                    }
                } else {
                    Toast.makeText(mContext,
                            getResources().getString(R.string.acess_server_error), Toast.LENGTH_SHORT).show();
                    mPullListView.onRefreshComplete();
//					if (mLoadProgressDialog != null)
//						if (mLoadProgressDialog.isShowing())
//							mLoadProgressDialog.dismiss();
                }
            }
        });
        //根据性别查询
        //	searchBysexButton=(ImageView)getActivity().findViewById(R.id.search_bysex);
        //searchBysexButton.setOnClickListener(this);
        //sexTextView=(TextView)getActivity().findViewById(R.id.sex_text);
        //sexTextView.setOnClickListener(this);
        //	getActivity().findViewById(R.id.spinner_sex).setOnClickListener(this);
        loadProgressDialog.show();
        initlocation();
    }

    @Override
    public void onPageResume() {
        // TODO Auto-generated method stub
        preTime = FunctionStatic.onResume();
        MobclickAgent.onPageStart("NearbyActivity");
        if (mMapView != null) {
            mMapView.onResume();
        }
    }

    @Override
    public void onPagePause() {
        // TODO Auto-generated method stub
        if (mPopWindow != null) {
            if (mPopWindow.isShowing())
                mPopWindow.dismiss();
            mPopWindow = null;
        }
        StateActivity.unregisterLocation();
        FunctionStatic.onPause(this, FunctionStatic.FUNCTION_INVITE, preTime);
        MobclickAgent.onPageEnd("NearbyActivity");
    }

    @Override
    public void onPageDestroy() {
        // TODO Auto-generated method stub
        if (mList != null) {
            mList.clear();
        }
        mAdapter = null;
        if (mListView != null) {
            mListView.setAdapter(null);
            mListView = null;
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
        if (mNearbayMark != null) {
            mNearbayMark.clear();
            mNearbayMark = null;
        }
        if (mNearbayView != null) {
            mNearbayView.clear();
            mNearbayView = null;
        }

        mSportsApp.removeActivity(this);
//		mSportsApp = null;
    }

    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
        if (mPopWindow != null) {
            if (mPopWindow.isShowing())
                mPopWindow.dismiss();
            mPopWindow = null;
        }
    }

    public void init() {
        switch_MAIN = new SwitchView(mContext);
        showRightBtn(switch_MAIN);
        switch_MAIN.setOnCheckedChangeListener(new com.fox.exercise.view.SwitchView.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(boolean isChecked) {
                // TODO Auto-generated method stub
                if (isChecked) {
                    nearbyMap.setVisibility(View.GONE);
                    mListView.setVisibility(View.VISIBLE);

                } else {
                    if (!mSportsApp.isOpenNetwork()) {
                        Toast.makeText(mContext, R.string.error_cannot_access_net,
                                Toast.LENGTH_SHORT).show();
                    }
                    nearbyMap.setVisibility(View.VISIBLE);
                    mListView.setVisibility(View.GONE);
                    if (mapNum == 0) {
                        onActivityCreatedNearby();
                        mapNum++;
                    }
                    loadMapData();
                }
            }
        });
    }

    class GetUserNameThread extends Thread {
        @Override
        public void run() {
            if (mSportsApp.getSessionId() == null || "".equals(mSportsApp.getSessionId()))
                return;
            try {
                UserDetail detail = ApiJsonParser.refreshRank(mSportsApp
                        .getSessionId());
                if (detail != null) {
                    mSportsApp.setSportUser(detail);
                }
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

            }
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case LEFT_TITLE_VIEW:
            case LEFT_TITLE_BUTTON:
                GetUserNameThread thread = new GetUserNameThread();
                thread.start();
                finish();
                break;

            default:
                break;
        }
    }


    public void initlocation() {
        if (initSuccess)
            return;

        LocationInfo.latitude = "";
        LocationInfo.longitude = "";

        GetNearByThread thread = new GetNearByThread();
        thread.start();
    }

    class GetNearByThread extends Thread {

        private long startTime = System.currentTimeMillis();

        @Override
        public void run() {

            Message msg = null;
            if (!uploadLocSuccess) {
                while ((LocationInfo.latitude == null || LocationInfo.latitude.equals("") || LocationInfo.latitude.equals("4.9E-324"))
                        && (LocationInfo.longitude == null || LocationInfo.longitude.equals("") || LocationInfo.longitude.equals("4.9E-324"))) {

                    if (StateActivity.registerLocation() != 0 && (System.currentTimeMillis() - startTime) < 5 * 1000)
                        continue;

                    if (this == null || NewYuePaoBaiDuActivity.this.isFinishing()) {
                        StateActivity.unregisterLocation();
                        return;
                    } else if ((System.currentTimeMillis() - startTime) < 5 * 1000) {
                        Thread.yield();
                        // Log.d(TAG, "*******检测定位监听@@@@********");
                    } else {
                        msg = Message.obtain(mhandler, LOCATE_FAILED);
                        msg.sendToTarget();
                        StateActivity.unregisterLocation();
                        return;
                    }
                }
                StateActivity.unregisterLocation();
                msg = Message.obtain(mhandler, LOCATE_FINISH);
                msg.sendToTarget();

                ApiBack back = null;
                try {
                    back = ApiJsonParser.uploadLocal(mSportsApp.getSessionId(),
                            LocationInfo.latitude, LocationInfo.longitude);
                } catch (ApiNetException e) {
                    e.printStackTrace();
                } catch (ApiSessionOutException e) {
                    e.printStackTrace();
                }
                if (back != null && back.getFlag() == 1) {
                    uploadLocSuccess = true;
                }
            }
            //根据各种情况从服务器读取信息存在list中
            ArrayList<UserNearby> list = null;
            //是否输入了字符
            if (mSearchNearByName == SEARCH_NAME) {
                try {
                    list = (ArrayList<UserNearby>) ApiJsonParser.getNearbyName(
                            times, editname, self.getUid());
                } catch (ApiNetException e) {
                    e.printStackTrace();
                } catch (ApiSessionOutException e) {
                    e.printStackTrace();
                }
            } else {
                //是否选择了性别
                if (sex != 0) {
                    try {
                        list = (ArrayList<UserNearby>) ApiJsonParser.getNearbyBysex(
                                times, mSportsApp.getSessionId(), sex);
                    } catch (ApiNetException e) {
                        e.printStackTrace();
                    } catch (ApiSessionOutException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        list = (ArrayList<UserNearby>) ApiJsonParser.getNearby(
                                times, mSportsApp.getSessionId());
                    } catch (ApiNetException e) {
                        e.printStackTrace();
                    } catch (ApiSessionOutException e) {
                        e.printStackTrace();
                    }
                }
            }
            if (times == 0)
                if (mList != null) {
                    mList.clear();
                }


            if (list != null && this != null
                    && !NewYuePaoBaiDuActivity.this.isFinishing()) {
                for (UserNearby e : list) {
                    mList.add(e);
                }
                initSuccess = true;
                msg = Message.obtain(mhandler, FRESH_LIST);
                msg.sendToTarget();

            } else {
                if (list == null) {
                    msg = Message.obtain(mhandler, FRESH_FAILED);
                    msg.sendToTarget();
                }
            }
        }
    }

    class NearByHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case FRESH_LIST:
                    if (times == 0) {
                        Log.d(TAG, "handleMessage mImgTimes == 0");
                        mListView.setAdapter(null);
                        mAdapter = new NearByListViewAdapter(
                                mContext, mList, mSportsApp);
                        mListView.setAdapter(mAdapter);
                        mPullListView.onRefreshComplete();
                    } else {
                        if (mAdapter == null) {
                            mAdapter = new NearByListViewAdapter(
                                    mContext, mList, mSportsApp);
                            mListView.setAdapter(mAdapter);
                        }
                        mAdapter.notifyDataSetChanged();
                        mPullListView.onRefreshComplete();
                    }
                    if (loadProgressDialog != null)
                        if (loadProgressDialog.isShowing())
                            loadProgressDialog.dismiss();
                    break;
                case FRESH_LIST_NEW:
                    if (startAddMore) {
                        Log.d(TAG, "*******11更新适配器检测times********" + times + "***edti:"
                                + editname + "****Uid:" + self.getUid());
                        Log.d(TAG, "*******检发送message11 ********");
                        if (mAdapter == null) {
                            mAdapter = new NearByListViewAdapter(
                                    mContext, mList, mSportsApp);
                            mListView.setAdapter(mAdapter);
                        }
                        mAdapter.notifyDataSetChanged();// 更新适配器
                        mPullListView.onRefreshComplete();
                        startAddMore = false;
                    }
                    Log.d(TAG, "*******检发进入 FRESH_LIST_NEW********");
                    Log.d(TAG, "*******检发进入 FRESH_LIST_NEW*的mList有误值*******"
                            + mList.size());
                    if (mAdapter == null) {
                        Log.d(TAG, "*******22放入适配器检测times********" + times + "***edti:"
                                + editname + "****Uid:" + self.getUid());
                        mAdapter = new NearByListViewAdapter(mContext, mList,
                                mSportsApp);
                        mListView.setAdapter(mAdapter);
                    }

                    if (loadProgressDialog != null)
                        if (loadProgressDialog.isShowing())
                            loadProgressDialog.dismiss();
                    break;
                case LOCATE_FAILED:
                    if (loadProgressDialog != null)
                        if (loadProgressDialog.isShowing())
                            loadProgressDialog.dismiss();
                    if (this != null)
                        Toast.makeText(mContext, R.string.location_fail, Toast.LENGTH_SHORT)
                                .show();
                    break;
                case LOCATE_FINISH:
                    mDialogMessage.setText(R.string.sports_wait);
                    break;
                case FRESH_FAILED:
                    if (loadProgressDialog != null)
                        if (loadProgressDialog.isShowing())
                            loadProgressDialog.dismiss();
                    if (this != null)
                        Toast.makeText(mContext, R.string.sports_failed_list, Toast.LENGTH_SHORT)
                                .show();
                    break;
                case ApiConstant.UPDATE_DEFAULTMAP_MSG:
                    int currentMap = msg.arg1;
                    break;
                case MARKER_LOAD:
                    loadProgressDialog.dismiss();
                    break;
            }
        }

    }

    int threadNum = 0;

    class MapNearByThread extends Thread {
        @Override
        public void run() {
            // TODO Auto-generated method stub
            Message message = null;
            if (nearbyBool == true) {
                if (mNearbayMark.size() > 0) {
                    for (Marker marker : mNearbayMark) {
                        marker.remove();
                    }
                    mNearbayMark.clear();
                    Log.i("", "mNearbayView" + mNearbayView.size());
                    for (int i = 0; i < mNearbayView.size(); i++) {
                        mNearbayView.remove(i);
                    }
                    mNearbayView.clear();
                }
                nearbyMarkerOptions();
                nearbyBool = false;
            } else if (threadNum == 0) {
                nearbyMarkerOptions();
                threadNum++;
            }
            message = Message.obtain(mhandler, MARKER_LOAD);
            message.sendToTarget();
            super.run();
        }
    }

    private TextWatcher nearby_edittextChangedListener = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                  int arg3) {
            // TODO Auto-generated method stub

        }

        @Override
        public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {
            // TODO Auto-generated method stub

        }

        @Override
        public void afterTextChanged(Editable arg0) {

            // TODO Auto-generated method stub

            editname = sports_nearby_edittext.getText().toString();
            int txtCnt = editname.length();
            if (txtCnt != edittextCnt && txtCnt != 0) {
                times = 0;
                edittextCnt = txtCnt;
                mSearchNearByName = SEARCH_NAME;
                nearbyBool = true;
                GetNearByThread getnearByThread = new GetNearByThread();
                getnearByThread.start();
            } else if (txtCnt != edittextCnt && txtCnt == 0) {
                times = 0;
                edittextCnt = txtCnt;
                mSearchNearByName = SEARCH_BASIC;
                loadProgressDialog.show();
                nearbyBool = true;
                GetNearByThread getnearByThread = new GetNearByThread();
                getnearByThread.start();
            }
        }
    };

    private OnItemClickListener listViewClick = new OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                long arg3) {
            // /@ Intent intent = new Intent(NearbyActivity.this,
            // SportsUserBrowse.class);
            // /@ intent.putExtra("ID", mList.get(position).getId());
            // /@ startActivity(intent);
        }
    };


//	private void showPopWindow(View parent){
//		if (mPopWindow == null) {
//			LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//			LinearLayout popLayout = (LinearLayout) inflater.inflate(R.layout.sports_sex_select, null);
//			mPopWindow = new PopupWindow(popLayout, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
//			mPopWindow.setBackgroundDrawable(new BitmapDrawable());
//			mPopWindow.setOutsideTouchable(true);
//			mPopWindow.setFocusable(true);
//			LinearLayout all_sexLayout = (LinearLayout) popLayout.findViewById(R.id.all_sex);
//			all_sexLayout.setOnClickListener(new OnClickListener() {
//				@Override
//				public void onClick(View arg0) {
//					sex=0;//0-全部
//					GotoSearchBySex();
//				}
//			});
//			LinearLayout man_sexLayout = (LinearLayout) popLayout.findViewById(R.id.sex_man);
//			man_sexLayout.setOnClickListener(new OnClickListener() {
//				@Override
//				public void onClick(View arg0) {
//					sex=1;//1-男
//					GotoSearchBySex();
//				}
//			});
//			LinearLayout woman_sexLayout = (LinearLayout) popLayout.findViewById(R.id.sex_woman);
//			woman_sexLayout.setOnClickListener(new OnClickListener() {
//				@Override
//				public void onClick(View arg0) {
//					sex=2;//2-女
//					GotoSearchBySex();
//				}
//			});
//		}
//
//		//mPopWindow.showAsDropDown(getActivity().findViewById(R.id.spinner_sex),Gravity.BOTTOM,0);
//	}
    //根据性别去搜索
//	private void GotoSearchBySex(){
//		mSearchNearByName=SEARCH_BASIC;
//    	loadProgressDialog.show();
//    	times=0;
//    	mPopWindow.dismiss();
//    	sexTextView.setText(sexshowStrings[sex]);
//		GetNearByThread getnearByThread = new GetNearByThread();
//		getnearByThread.start();
//	}

    public void onActivityCreatedNearby() {
/*		LinearLayout mapLayout =(LinearLayout)mapInflater.inflate(R.layout.nearby_baidu_map, null);
        nearbyMap.removeAllViews();
		nearbyMap.addView(mapLayout,
		new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT));*/
        loadProgressDialog.show();
        mMapView = (MapView) findViewById(R.id.bmapView);
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
            aMap.setOnMapLoadedCallback(new OnMapLoadedCallback() {
                @Override
                public void onMapLoaded() {

                }
            });// 设置amap加载成功事件监听器
            mUiSettings = aMap.getUiSettings();
            mUiSettings.setZoomGesturesEnabled(true);
            mUiSettings.setCompassEnabled(false);//隐藏指南针
            aMap.setMyLocationEnabled(true);// 是否可触发定位并显示定位层
            aMap.setOnMapLoadedCallback(new OnMapLoadedCallback() {
                @Override
                public void onMapLoaded() {
                    Log.v("", "地图加载成功！");
                }
            });// 设置amap加载成功事件监听器

            aMap.setOnMarkerClickListener(new OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(final Marker marker) {
                    if (!mSportsApp.isOpenNetwork()) {
                        Toast.makeText(mContext, R.string.error_cannot_access_net, Toast.LENGTH_LONG)
                                .show();
                        return false;
                    }
                    for (int i = 0; i < mNearbayMark.size(); i++) {
                        if (marker.equals(mNearbayMark.get(i))) {
                            UserNearby unb = mNearbayView.get(i);
                            Log.i("", "marker.getZIndex()" + marker.getZIndex());
                            Log.i("", "marker.getZIndex()" + rootView.getTag());
//							Intent intent = new Intent(mContext, PedometerActivity.class);
//							intent.putExtra("ID", unb.getId());
//							intent.putExtra("isFromInvite", true);
                            Intent intent = new Intent(mContext, PersonalPageMainActivity.class);
                            intent.putExtra("ID", unb.getId());
                            mContext.startActivity(intent);
                            return true;
                        }
                    }
                    return false;
                }
            });
        }

        myListener = new MyLocationListenner();
        mLocClient = new LocationClient(mContext);
        LocationClientOption option = new LocationClientOption();
        // 优先选择GPS
//		option.setOpenGps(true);
//		option.setPriority(LocationClientOption.GpsFirst);
        option.setLocationMode(LocationMode.Hight_Accuracy);
        // 设置坐标类型
        option.setCoorType("bd09ll");
        mLocClient.registerLocationListener(myListener);
        mLocClient.setLocOption(option);
        mLocClient.start();
    }

    int count = 0;
    int netError = 0;

    public class MyLocationListenner implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            String latitude = String.valueOf(location.getLatitude());
            String longitude = String.valueOf(location.getLongitude());
            if (!mSportsApp.isOpenNetwork()) {
                Toast.makeText(mContext, R.string.sports_comment_not_connected,
                        Toast.LENGTH_SHORT).show();
            }
            if (latitude.equals("") || latitude.equals("0.0")
                    || longitude.equals("") || longitude.equals("0.0")
                    || latitude == null || longitude == null
                    || latitude.equals("4.9E-324") || longitude.equals("4.9E-324")) {
                Log.i("location", "定位失败！");
                if (netError == 1) {
                    Toast.makeText(mContext, R.string.location_fail, Toast.LENGTH_SHORT)
                            .show();
                }
                netError++;
                return;
            }
            LatLng point = new LatLng(location.getLatitude(), location.getLongitude());
            if (count == 0) {
                BitmapDescriptor descriptor = BitmapDescriptorFactory
                        .fromBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.map_start));
                if (descriptor != null) {
                    mMarkerOpStart = new MarkerOptions().icon(descriptor);
                    aMap.addOverlay(mMarkerOpStart.position(point));
                    count++;
                }
            }
            animateTo(point);
//			MapNearByThread byThread = new MapNearByThread();
//			byThread.start();
        }

//		@Override
//		public void onReceivePoi(BDLocation arg0) {
//			// TODO Auto-generated method stub
//
//		}
    }

    private void animateTo(LatLng point) {
        aMap.animateMapStatus(MapStatusUpdateFactory.newLatLngZoom(point, 16), 1000);
    }

    private void loadMapData() {
        loadProgressDialog.show();
        MapNearByThread byThread = new MapNearByThread();
        byThread.start();
    }

    private void nearbyMarkerOptions() {
        MarkerOptions mMarkerStart = new MarkerOptions();
        MarkerOptions mMarkerStartView = new MarkerOptions();
        ImageDownloader mDownloader;
        for (int i = 0; i < mList.size(); i++) {
            if (m_vContentView == null)
                break;
            if (mList.get(i).getLng() == 0.0 || mList.get(i).getLat() == 0.0) {
                continue;
            }
            mDownloader = new ImageDownloader(mContext);
            mDownloader.setType(ImageDownloader.ICON);
            LatLng llA = new LatLng(mList.get(i).getLat(), mList.get(i).getLng());
            LayoutInflater mInflater = getLayoutInflater();
            LinearLayout layout = (LinearLayout) mInflater.inflate(R.layout.sports_nearby_cover, null);

            rootView = layout.findViewById(R.id.lay_nearby_cover);
            rootView.setTag(mList.get(i));
            mNearbayView.add((UserNearby) rootView.getTag());
            TextView tx_distance = (TextView) rootView.findViewById(R.id.tx_cover_name);
            tx_distance.setText(mList.get(i).getName());

            TextView distanceTxt = (TextView) rootView.findViewById(R.id.tx_cover_Distance);
            int Distance = mList.get(i).getDistance();
            String DistanceName;
            if (Distance > 1000) {
                float f = Distance / 1000.0f;
                DistanceName = mContext.getString(R.string.friends_away_me) + String.format("%.1f", f) + mContext.getString(R.string.sports_kilometers);
            } else {
                DistanceName = mContext.getString(R.string.friends_away_me) + Distance + mContext.getString(R.string.sports_meters);
            }
            SpannableStringBuilder style = new SpannableStringBuilder(DistanceName);
            int color = getResources().getColor(R.color.text_moffmap);
            style.setSpan(new ForegroundColorSpan(color), 2, DistanceName.length() - 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            distanceTxt.setText(style);

            //distanceTxt.setText(Html.fromHtml("<font color=\"#33c9ca\">"+Distance+"</font>其它颜色")+"米");
            RoundedImage iconImg = (RoundedImage) rootView.findViewById(R.id.nearby_cover_img);
            iconImg.setImageDrawable(null);
            if (mList.get(i).getImg() != null && !"".equals(mList.get(i).getImg())) {
                if ("man".equals(mList.get(i).getSex())) {
                    iconImg.setBackgroundResource(R.drawable.sports_user_edit_portrait_male);
                } else if ("woman".equals(mList.get(i).getSex())) {
                    iconImg.setBackgroundResource(R.drawable.sports_user_edit_portrait);
                }
                //传头像
                if (!SportsApp.DEFAULT_ICON.equals(mList.get(i).getImg())) {
                    //mDownloader.download(mList.get(1).getImg(), iconImg, null);
                    iconImg.setImageBitmap(mDownloader.getLocalBitmap(mList.get(i).getImg()));
                }
            }
            BitmapDescriptor bdA = BitmapDescriptorFactory.fromView(rootView);
            mMarkerView = (Marker) aMap.addOverlay(mMarkerStartView.icon(bdA).position(llA));
            mNearbayMark.add(mMarkerView);
        }
    }


}
