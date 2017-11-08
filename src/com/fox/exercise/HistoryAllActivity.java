package com.fox.exercise;

//import java.text.SimpleDateFormat;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fox.exercise.api.ApiJsonParser;
import com.fox.exercise.api.ApiNetException;
import com.fox.exercise.api.ApiSessionOutException;
import com.fox.exercise.api.entity.UserDetail;
import com.fox.exercise.db.SportSubTaskDB;
import com.fox.exercise.http.FunctionStatic;
import com.fox.exercise.map.SportTaskDetailActivityGaode;
import com.fox.exercise.newversion.adapter.NewHistorySportAdapter;
import com.fox.exercise.pedometer.SportContionTaskDetail;
import com.fox.exercise.util.SportTaskUtil;
import com.fox.exercise.view.PullToRefreshBase.OnRefreshListener;
import com.fox.exercise.view.PullToRefreshListView;
import com.umeng.analytics.MobclickAgent;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import cn.ingenic.indroidsync.SportsApp;

import static com.fox.exercise.newversion.adapter.NewHistorySportAdapter.getDistance;


public class HistoryAllActivity extends AbstractBaseActivity implements
        Button.OnClickListener {
    private Dialog mProgressDialog;
    private ListView listView;

    private static final int MODE_PULL_UP_TO_REFRESH = 0x2;

    private TextView no_history;

    private PullToRefreshListView mPullListView = null;

    private HistorySportAdpter mAdapter = null;
    private NewHistorySportAdapter historySportAdapter = null;
    private ArrayList<SportContionTaskDetail> mList = null;

    private HistoryHandler mHandler = null;
    private RelativeLayout history_rl_title;
    private TextView history_title_time, history_title_distance;

    private static final int FRESH_LIST = 1;

    // private SportsExceptionHandler mExceptionHandler = null;
    // private static final int PAGE_NUM = 10;
    // private ViewPager mPager;
    private HashMap<String, Double> distanceMap = new HashMap<String, Double>();
    private static final int BACK = 101;
    private static final int NO_HISTORY = 2;

    public static final int SPORT_DEL = 1;

    private static final String TAG = "HistoryAllActivity";

    private int userID;
    private int curUserID;

    private boolean mIsRefresh = true;

    private SportsApp mSportsApp;
    // private Context mContext;

    // private int mStart = 0;

    private String sessionId;

    private int lastPosition = -1;

    private long preTime = 0;
    private SportSubTaskDB db;

    private View head_view;
    private TextView yundong_cishu;// 共运动次数
    private TextView yundong_laluli;// 消耗卡路里
    private TextView yundong_di_day;// 运动天数

    @Override
    public void initIntentParam(Intent intent) {
        // TODO Auto-generated method stub
        title = getResources().getString(R.string.sports_history);
    }

    @Override
    public void initView() {
        // TODO Auto-generated method stub
        // boolean findMethod = SmartBarUtils.findActionBarTabsShowAtBottom();
        /*
         * if (!findMethod) { // 取消ActionBar拆分，换用TabHost
		 * getWindow().setUiOptions(0);
		 * getWindow().requestFeature(android.view.Window.FEATURE_NO_TITLE); }
		 */
        showContentView(R.layout.sports_all_history);
        // setContentView(R.layout.sports_all_history);
        mSportsApp = (SportsApp) getApplication();
        // mContext = this;
        // mExceptionHandler = mSportsApp.getmExceptionHandler();
        sessionId = mSportsApp.getSessionId();
        userID = getIntent().getIntExtra("ID", 0);
        curUserID = mSportsApp.getSportUser().getUid();
        initProgressDialog();
        InitView();

		/*
         * if(findMethod){ getActionBar().setDisplayShowHomeEnabled(false);
		 * getActionBar().setDisplayShowTitleEnabled(false);
		 * SmartBarUtils.setActionModeHeaderHidden(getActionBar(), true);
		 * SmartBarUtils.setActionBarViewCollapsable(getActionBar(), true); }
		 */
        mList = new ArrayList<SportContionTaskDetail>();
        mHandler = new HistoryHandler();
        initLoading();
        db = SportSubTaskDB.getInstance(this);
    }

    @Override
    public void setViewStatus() {
        // TODO Auto-generated method stub

    }

    @Override
    public void onPageResume() {
        // TODO Auto-generated method stub
        preTime = FunctionStatic.onResume();
        MobclickAgent.onPageStart("HistoryAllActivity");
    }

    @Override
    public void onPagePause() {
        // TODO Auto-generated method stub
        FunctionStatic.onPause(this, FunctionStatic.FUNCTION_HISTORY, preTime);
        MobclickAgent.onPageEnd("HistoryAllActivity");
    }

    @Override
    public void onPageDestroy() {
        // TODO Auto-generated method stub
        if (mProgressDialog != null) {
            if (mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
                mProgressDialog = null;
            }
        }
        if (mList != null) {
            mList.clear();
            mList = null;
        }
        if (db != null) {
            db.close();
            db = null;
        }
//		mSportsApp = null;
    }

    private void initProgressDialog() {
        mProgressDialog = new Dialog(HistoryAllActivity.this,
                R.style.sports_dialog);
        LayoutInflater mInflater = getLayoutInflater();
        View v = mInflater.inflate(R.layout.sports_progressdialog, null);
        TextView message = (TextView) v.findViewById(R.id.message);
        message.setText(R.string.loading);
        v.setMinimumWidth((int) (SportsApp.ScreenWidth * 0.8));
        mProgressDialog.setContentView(v);
        mProgressDialog.setCancelable(true);
        mProgressDialog.setCanceledOnTouchOutside(false);
    }

    private void InitView() {
        no_history = (TextView) findViewById(R.id.no_history);

        head_view = LayoutInflater.from(this).inflate(
                R.layout.history_headview, null);
        yundong_cishu = (TextView) head_view.findViewById(R.id.yundong_cishu);
        yundong_laluli = (TextView) head_view.findViewById(R.id.yundong_laluli);
        yundong_di_day = (TextView) head_view.findViewById(R.id.yundong_di_day);

        history_rl_title = (RelativeLayout) findViewById(R.id.history_rl_title);
        history_title_distance = (TextView) findViewById(R.id.history_title_distance);
        history_title_time = (TextView) findViewById(R.id.history_title_time);

        if (userID != 0 && userID == curUserID) {
            if (getIntent().hasExtra("yundong_cishu")) {
                yundong_cishu.setText(getIntent().getStringExtra(
                        "yundong_cishu"));
            }
            if (getIntent().hasExtra("yundong_laluli")) {
                yundong_laluli.setText(getIntent().getStringExtra(
                        "yundong_laluli"));
            }
            if (getIntent().hasExtra("yundong_di_day")) {
                yundong_di_day.setText(getIntent().getStringExtra(
                        "yundong_di_day"));
            }
        } else {
            if (mSportsApp.isOpenNetwork()) {
                new AsyncTask<Void, Void, UserDetail>() {

                    @Override
                    protected UserDetail doInBackground(Void... params) {
                        // TODO Auto-generated method stub
                        UserDetail userDetail = null;
                        try {
                            userDetail = ApiJsonParser.seeUserSimple(
                                    mSportsApp.getSessionId(), userID);
                        } catch (ApiNetException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        } catch (ApiSessionOutException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        return userDetail;
                    }

                    @Override
                    protected void onPostExecute(UserDetail result) {
                        // TODO Auto-generated method stub
                        if (result != null) {
                            yundong_cishu.setText(result.getCount_num() + "");
                            double s2 = 0.0;// 未上传的总卡路里
                            s2 = result.getSprots_Calorie();
                            DecimalFormat df = new DecimalFormat("0");
                            String numStr = df.format(s2);
                            yundong_laluli.setText(numStr);
                            yundong_di_day.setText(result.getTime() + "");
                        }
                    }

                }.execute();

            }
        }

        mPullListView = (PullToRefreshListView) findViewById(R.id.gifts_pull_refresh_list);
        listView = mPullListView.getRefreshableView();
        listView.setDividerHeight(0);

        //listView.addHeaderView(head_view);
        listView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (!mIsRefresh) {
                    if (mList != null && mList.size() > 0) {
                        lastPosition = position;
                        launchSportDetail(position);
                    }

                }
            }
        });
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem < 0) {
                    history_rl_title.setVisibility(View.GONE);
                } else {
                    try {
                        if (no_history.getVisibility()==View.VISIBLE){
                            history_rl_title.setVisibility(View.GONE);
                        }else {
                            history_rl_title.setVisibility(View.VISIBLE);
                        }
//                        if (firstVisibleItem==0){
//                            history_title_distance.setText(SportTaskUtil.getDoubleNumber(getDistance(mList, distanceMap, firstVisibleItem)) + "km");
//                        }else {
//                            history_title_distance.setText("");
//                        }
                        switch (needShowYears(mList, firstVisibleItem)) {
                            case 0:
                                history_title_time.setText(Integer.parseInt(mList.get(firstVisibleItem).getSportDate().split("-")[1]) + "月");
                                break;
                            case 1:
                                history_title_time.setText(mList.get(firstVisibleItem).getSportDate().split("-")[0] + "年" + mList.get(firstVisibleItem).getSportDate().split("-")[1] + "月");
                                break;
                        }
                    } catch (Exception e) {

                    }

                }
            }
        });
        mPullListView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(int pullDirection) {
                switch (pullDirection) {
                    case MODE_PULL_UP_TO_REFRESH:
                        Log.d(TAG, "****向上拉***");
                        mIsRefresh = true;
                        GetUploadThread loadGiftsThread = new GetUploadThread();
                        loadGiftsThread.start();
                        break;
                }
            }
        });
        //findViewById(R.id.bt_back).setOnClickListener(this);
        setBackImage();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case BACK:
                finish();
                break;
        }
    }

    private void launchSportDetail(int position) {
        Intent intent;
        if (mSportsApp.mCurMapType == SportsApp.MAP_TYPE_GAODE) {
            intent = new Intent(this, SportTaskDetailActivityGaode.class);
            SportContionTaskDetail detail = mList.get(position);
            intent.putExtra("taskid", detail.getTaskid());
            intent.putExtra("uid", userID);
            intent.putExtra("startTime", detail.getStartTime());
            intent.putExtra("mark_code", detail.getSport_markcode());
            startActivityForResult(intent, SPORT_DEL);
        }
    }

    private int mTimes = 0;

    // ///////////////////////////
    // 获取数据的方法（默认本地获取，其次服务器获取）--------手机运动记录
    class GetUploadThread extends Thread {

        public GetUploadThread() {
        }

        @Override
        public void run() {
            List<SportContionTaskDetail> templist = new ArrayList<SportContionTaskDetail>();
            if (mIsRefresh) {
                if (userID == curUserID) {
                    SportSubTaskDB taskDBhelper = SportSubTaskDB
                            .getInstance(getApplicationContext());
                    if (taskDBhelper != null) {
                        templist = taskDBhelper.getTasks(userID, mTimes);
                        Log.d("lou..", templist.size() + "");
                    }
                    if (templist != null && templist.size() > 0) {
                        mTimes++;
                    } else {
                        // 当本地没有了
                        if (mSportsApp.mIsNetWork) {
                            try {
                                templist = ApiJsonParser.getSportsTaskAll(
                                        sessionId, mTimes, userID);
                                if (templist != null) {
                                    for (SportContionTaskDetail sportContionTaskDetail : templist) {
                                        saveDate2DB(sportContionTaskDetail);
                                    }
                                    if (taskDBhelper != null) {
                                        templist = taskDBhelper.getTasks(
                                                userID, mTimes);
                                        Log.d("lou..", templist.size() + "");
                                    }
                                    if (templist != null && templist.size() > 0) {
                                        mTimes++;
                                    }

                                }
                            } catch (ApiNetException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            } catch (ApiSessionOutException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }
                    }

                } else {
                    if (templist != null)
                        templist.clear();
                    if (mSportsApp.mIsNetWork) {
                        try {
                            templist = ApiJsonParser.getSportsTaskAll(
                                    sessionId, mTimes, userID);
                        } catch (ApiNetException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        } catch (ApiSessionOutException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                    if (templist != null && templist.size() > 0) {
                        mTimes++;
                    }
                }

                Message message = new Message();
                message.what = FRESH_LIST;
                message.obj = templist;
                mHandler.sendMessage(message);

            }
        }
    }

    private void saveDate2DB(SportContionTaskDetail detail) {
        if (db != null) {
            // Cursor cursor = db.query(userID, detail.getStartTime());
            Cursor cursor = db.newquery(userID, detail.getStartTime(),
                    detail.getSportTime(), detail.getSportDistance() + "");
            try {
                if (!cursor.moveToFirst()) {
                    ContentValues values = new ContentValues();
                    values.put(SportSubTaskDB.UID, detail.getUserId());
                    values.put(SportSubTaskDB.SPORT_TYPE,
                            detail.getSports_type());
                    values.put(SportSubTaskDB.SPORT_SWIM_TYPE,
                            detail.getSwimType());
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
                    values.put(SportSubTaskDB.SPORT_MAPTYPE,
                            detail.getMapType());

                    values.put(SportSubTaskDB.SPORT_MARKCODE,
                            detail.getSport_markcode());
                    values.put(SportSubTaskDB.SPORT_SPEEDLIST,
                            detail.getSport_speedList());
                    values.put(SportSubTaskDB.SPORT_MARKLIST,
                            detail.getCoordinate_list());
                    db.insert(values, false);
                    Log.v(TAG,
                            "插入date到数据库成功 starttime =" + detail.getStartTime());
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
                // db.close();
            }
        }
    }

    // 保存运动碎片到本地数据库
    // private void saveSportDB2(SportContionTaskDetail detail) {
    // SportSubTaskDB db = SportSubTaskDB.getInstance(this);
    // try {
    // ContentValues values = new ContentValues();
    // values.put(SportSubTaskDB.UID, detail.getUserId());
    // values.put(SportSubTaskDB.SPORT_TYPE, detail.getSports_type());
    // values.put(SportSubTaskDB.SPORT_SWIM_TYPE, detail.getSwimType());
    // values.put(SportSubTaskDB.SPORT_DEVICE,
    // detail.getMonitoringEquipment());
    // values.put(SportSubTaskDB.SPORT_START_TIME, detail.getStartTime());
    // values.put(SportSubTaskDB.SPORT_TIME, detail.getSportTime());
    // values.put(SportSubTaskDB.SPORT_DISTANCE, detail.getSportDistance());
    // values.put(SportSubTaskDB.SPORT_SPEED, detail.getSportVelocity());
    // values.put(SportSubTaskDB.SPORT_CALORIES,
    // detail.getSprots_Calorie());
    // values.put(SportSubTaskDB.SPORT_HEART_RATE, detail.getHeartRate());
    // values.put(SportSubTaskDB.SPORT_LAT_LNG, detail.getLatlng());
    // values.put(SportSubTaskDB.SPORT_ISUPLOAD, 1);
    // values.put(SportSubTaskDB.SPORT_DATE, detail.getSportDate());
    // values.put(SportSubTaskDB.SPORT_TASKID, detail.getTaskid());
    // values.put(SportSubTaskDB.SPORT_STEP, detail.getStepNum());
    // db.insert(values, false);
    // } catch (Exception e) {
    // e.printStackTrace();
    // } finally {
    // db.close();
    // }
    // }

    // 保存运动碎片到本地数据库
    // private void saveDate2DB() {
    // SportsTimeDB db = SportsTimeDB.getInstance(this);
    // try {
    // for (int i = 0; i < mSportsApp.mTimeList.size(); i++) {
    // String time = mSportsApp.mTimeList.get(i).getCurrentTime();
    // SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    // long millionSeconds = sdf.parse(time).getTime();// 毫秒
    //
    // ContentValues values = new ContentValues();
    // values.put(SportsTimeDB.UID, curUserID);
    // values.put(SportsTimeDB.TIME, millionSeconds);
    // values.put(SportsTimeDB.DATE, time);
    // db.insert(values);
    // }
    // } catch (Exception e) {
    // e.printStackTrace();
    // } finally {
    // db.close();
    // }
    // }

    // 获取当前数据的日期
    public String getCurrentTime(int i) {
        String dates = null;
        try {
            SharedPreferences sps = getSharedPreferences("CurrentTimes"
                    + SportsApp.getInstance().getSportUser().getUid(), 0);
            List<CurrentTimeList> lsts = new ArrayList<CurrentTimeList>();
            int size = sps.getInt("size", 0);
            CurrentTimeList a = null;
            for (int j = 0; j < size; j++) {
                a = new CurrentTimeList();
                a.setCurrentTime(sps.getString("time_" + j, ""));
                lsts.add(a);
            }
            if (size == 0) {
                return null;
            } else {
                dates = lsts.get(i).getCurrentTime();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dates;
    }

    class HistoryHandler extends Handler {

        @SuppressWarnings("unchecked")
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case FRESH_LIST:
                    if (mList == null || msg.obj == null)
                        return;
                    for (SportContionTaskDetail sportContionTaskDetail : (ArrayList<SportContionTaskDetail>) msg.obj) {
                        mList.add(sportContionTaskDetail);
                        distanceSum();
                    }
                    if (mList == null || mList.size() == 0) {
                        no_history.setVisibility(View.VISIBLE);
                        history_rl_title.setVisibility(View.GONE);
                    } else {
                        no_history.setVisibility(View.GONE);
                        history_rl_title.setVisibility(View.VISIBLE);
                    }
                    if (historySportAdapter == null) {
//                        if (mAdapter == null) {
//                        mAdapter = new HistorySportAdpter(mList,
//                                HistoryAllActivity.this);
//                        listView.setAdapter(mAdapter);

                        historySportAdapter = new NewHistorySportAdapter(mList, HistoryAllActivity.this, R.layout.item_historical_record,-1);
                        listView.setAdapter(historySportAdapter);
                        distanceSum();
                    } else {
                        listView.requestLayout();
//                        mAdapter.prepareTitles();
//                        mAdapter.notifyDataSetChanged();

                        historySportAdapter.distanceSum();
                        historySportAdapter.notifyDataSetChanged();
                        distanceSum();


                    }
                    if (mProgressDialog.isShowing()) {
                        mProgressDialog.dismiss();
                    }
                    mPullListView.onRefreshComplete();
                    mIsRefresh = false;
                    break;
                case NO_HISTORY:
                    no_history.setVisibility(View.VISIBLE);
                    history_rl_title.setVisibility(View.GONE);
                    if (mProgressDialog.isShowing()) {
                        mProgressDialog.dismiss();
                    }
                    mPullListView.onRefreshComplete();
                    mIsRefresh = false;
                    break;
            }
        }

    }

    // //////////////////////////////////////////////////////////////////////////////////////
    private void initLoading() {
        // SportsTimeDB timeDBhelper = SportsTimeDB
        // .getInstance(getApplicationContext());
        // mSportsApp.mTimeList = timeDBhelper
        // .getTasksList(curUserID);

        GetUploadThread newUploadThread = new GetUploadThread();
        newUploadThread.start();

        if (mProgressDialog != null) {
            if (!mProgressDialog.isShowing())
                mProgressDialog.show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case SPORT_DEL:
                    if (lastPosition >= 0) {
                        mList.remove(lastPosition);
//                        if (mAdapter != null) {
//                            mAdapter.prepareTitles();
//                            mAdapter.notifyDataSetChanged();
//                        }

                        if (historySportAdapter != null) {
                            historySportAdapter.distanceSum();
                            historySportAdapter.notifyDataSetChanged();
                            distanceSum();
                        }


                    }
                    lastPosition = -1;
                    break;
            }
        }
        Log.v(TAG, "resultCode : " + requestCode);
    }


    /**
     * @param
     * @method 更换返回键图片
     * @author suhu
     * @time 2016/11/15 14:02
     */
    private void setBackImage() {
        left_ayout.removeAllViews();
        ImageView imageView = new ImageView(this);
        imageView.setImageResource(R.drawable.sport_title_back_selector);
        left_ayout.addView(imageView);
        left_ayout.setId(BACK);
        left_ayout.setOnClickListener(this);
    }

    /**
     * @param
     * @return null
     * @method 获取每月公里数集合
     * @author suhu
     * @time 2016/11/11 10:57
     */
    public void distanceSum() {
        distanceMap.clear();
        if (mList != null) {
            for (int i = 0; i < mList.size(); i++) {
                String str[] = mList.get(i).getSportDate().split("-");
                String date = "";
                if (str != null && str.length >= 2) {
                    date = str[0] + "-" + str[1];
                }
                if (i == 0) {
                    distanceMap.put(date, mList.get(i).getSportDistance());
                    continue;
                }
                if (distanceMap.get(date) != null) {
                    distanceMap.put(date, distanceMap.get(date) + mList.get(i).getSportDistance());
                } else {
                    distanceMap.put(date, mList.get(i).getSportDistance());
                }
            }
        }

    }


    /**
     * @param list
     * @param position
     * @return 返回值两中状态（0，1）
     * 0：不显示年
     * 1：显示年
     * @method 判断是否显示年
     * @author suhu
     * @time 2016/12/7 9:22
     */
    public static int needShowYears(List<SportContionTaskDetail> list, int position) {
        //当前年
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String nowDate = format.format(new Date());
        String nowYears = nowDate.split("-")[0];
        //当前数据时间
        String date = list.get(position).getSportDate();
        String times[] = date.split("-");
        String thisYears = "";
        if (times != null && (times.length == 3)) {
            thisYears = times[0];
        }
        //判断年份是否相同
        if (nowYears.equals(thisYears)) {
            return 0;
        }
        return 1;
    }

}
