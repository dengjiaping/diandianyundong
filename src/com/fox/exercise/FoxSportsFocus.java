package com.fox.exercise;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.fox.exercise.api.ApiJsonParser;
import com.fox.exercise.api.ApiNetException;
import com.fox.exercise.api.ApiSessionOutException;
import com.fox.exercise.api.entity.UserFollowMsg;
import com.fox.exercise.http.FunctionStatic;
import com.fox.exercise.map.SportTaskDetailActivityGaode;
import com.fox.exercise.view.PullToRefreshBase.OnRefreshListener;
import com.fox.exercise.view.PullToRefreshListView;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.HashSet;

import cn.ingenic.indroidsync.SportsApp;


public class FoxSportsFocus extends AbstractBaseActivity {
    private Dialog mProgressDialog;
    private ListView listView;

    private PullToRefreshListView mPullListView = null;
    private ListView mListView = null;
    private SlimFrendAdapter mAdapter = null;
    private HashSet<UserFollowMsg> mSet = null;
    private ArrayList<UserFollowMsg> mList = null;

    private UploadHandler mHandler = null;
    private int mTimes = 0;

    private static final int FRESH_LIST = 0x0001;

    private static final String TAG = "GiftsMyActivity";
    private int userID;

    private boolean mIsRefresh = true;

    private SportsApp mSportsApp;
    private Context context;
    private long preTime = 0;

    // private int total;
    @Override
    public void initIntentParam(Intent intent) {
        // TODO Auto-generated method stub
        title = getResources().getString(R.string.sports_action);
    }

    @Override
    public void initView() {
        // TODO Auto-generated method stub
        showContentView(R.layout.sports_focus_list);
        context = this;
        mSportsApp = (SportsApp) getApplication();
        if (mSportsApp.getSportUser().getMsgCounts() != null) {
            userID = mSportsApp.getSportUser().getUid();
        }
        mSet = new HashSet<UserFollowMsg>();
        mList = new ArrayList<UserFollowMsg>();
    }

    @Override
    public void setViewStatus() {
        // TODO Auto-generated method stub
        initProgressDialog();
        initLoading();
    }

    @Override
    public void onPageResume() {
        // TODO Auto-generated method stub
        preTime = FunctionStatic.onResume();
        mHandler = new UploadHandler();
        MobclickAgent.onPageStart("FoxSportsFocus");
    }

    @Override
    public void onPagePause() {
        // TODO Auto-generated method stub
        FunctionStatic.onPause(this, FunctionStatic.FUNCTION_FRIEND_ACTION, preTime);
        MobclickAgent.onPageEnd("FoxSportsFocus");
    }

    @Override
    public void onPageDestroy() {
        if (mProgressDialog != null) {
            if (mProgressDialog.isShowing())
                mProgressDialog.dismiss();
            mProgressDialog = null;
        }

    }

    private void initProgressDialog() {
        mProgressDialog = new Dialog(FoxSportsFocus.this, R.style.sports_dialog);
        LayoutInflater mInflater = getLayoutInflater();
        View v = mInflater.inflate(R.layout.sports_progressdialog, null);
        TextView message = (TextView) v.findViewById(R.id.message);
        message.setText(R.string.loading);
        v.setMinimumWidth((int) (SportsApp.ScreenWidth * 0.8));
        mProgressDialog.setContentView(v);
        mProgressDialog.setCancelable(true);
        mProgressDialog.setCanceledOnTouchOutside(false);
    }


    class GetUploadThread extends Thread {
        private int uid = 0;

        public GetUploadThread(int userID) {
            this.uid = userID;
        }

        @Override
        public void run() {
            ArrayList<UserFollowMsg> list = new ArrayList<UserFollowMsg>();
            try {
                if (mIsRefresh) {
                    list = (ArrayList<UserFollowMsg>) ApiJsonParser.sportsUploadMsg(mSportsApp.getSessionId(), 0);
                    mSportsApp.getSportUser().setFcount(0);
                    mIsRefresh = false;
                    mSet.clear();
                    mList.clear();
                    mAdapter.clearList();
                } else {
                    list = (ArrayList<UserFollowMsg>) ApiJsonParser.sportsUploadMsg(mSportsApp.getSessionId(), ++mTimes);
                    mSportsApp.getSportUser().setFcount(0);
                    Log.e(TAG, "list.size" + list.size());
                    Log.e(TAG, "mlist.size" + mList.size());
                }
            } catch (ApiNetException e) {
                e.printStackTrace();
            } catch (ApiSessionOutException e) {
                e.printStackTrace();
            }
            Message msg = null;
            if (list != null) {
                for (UserFollowMsg e : list) {
                    Log.d(TAG, "UserFollowMsg:" + e);
                    if (mSet.add(e)) {
                        mList.add(e);
//						mAdapter.addItem(e);
                    }
                }
                Log.e(TAG, "mlist.size" + mList.size());
                if (mHandler != null) {
                    msg = Message.obtain(mHandler, FRESH_LIST);
                    msg.sendToTarget();
                }
            }
        }
    }

    class UploadHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case FRESH_LIST:
                    if (mAdapter == null) {
                        mAdapter = new SlimFrendAdapter(mList, FoxSportsFocus.this);
                        mListView.setAdapter(mAdapter);
                    } else {
                        mAdapter.notifyDataSetChanged();
                    }
                    if (mProgressDialog != null && mProgressDialog.isShowing()) {
                        mProgressDialog.dismiss();
                    }
                    if (mPullListView != null) {
                        mPullListView.onRefreshComplete();
                    }
                    break;
            }
        }

    }

    // //////////////////////////////////////////////////////////////////////////////////////
    private void initLoading() {
        mPullListView = (PullToRefreshListView) findViewById(R.id.gifts_pull_refresh_list);
        listView = mPullListView.getRefreshableView();
        Drawable drawable = getResources().getDrawable(R.drawable.sports_bg_line);
        listView.setDivider(drawable);
        listView.setDividerHeight(1);
        mAdapter = new SlimFrendAdapter(mList, FoxSportsFocus.this);
        listView.setAdapter(mAdapter);
        GetUploadThread newUploadThread = new GetUploadThread(userID);
        newUploadThread.start();
        mPullListView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(int pullDirection) {
                switch (pullDirection) {
                    case FansAndNear.MODE_PULL_DOWN_TO_REFRESH:
                        mIsRefresh = true;
                        mTimes = 0;
                        GetUploadThread newUploadThread = new GetUploadThread(userID);
                        newUploadThread.start();
                        break;
                    case FansAndNear.MODE_DEFAULT_LOAD:
                        mIsRefresh = false;
                        GetUploadThread loadGiftsThread = new GetUploadThread(userID);
                        loadGiftsThread.start();
                        break;
                }
            }
        });
        listView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (!mSportsApp.isOpenNetwork()) {
                    //网络未连接，请检查网络
                    Toast.makeText(context, getResources().getString(R.string.newwork_not_connected), Toast.LENGTH_LONG)
                            .show();
                    return;
                }
                    /*Intent intent = new Intent(context,
                            PedometerActivity.class);
					intent.putExtra("ID", mList.get(position).getId());
					startActivity(intent);*/
                Intent intent;
                if (mSportsApp.mCurMapType == SportsApp.MAP_TYPE_GAODE) {
                    intent = new Intent(context, SportTaskDetailActivityGaode.class);

//					else{
//						intent = new Intent(context, SportTaskDetailActivity.class);
//					}
                    intent.putExtra("uid", mList.get(position).getId());
                    intent.putExtra("taskid", mList.get(position).getTaskid());
                    context.startActivity(intent);
                }
            }
        });
        if (mProgressDialog != null) {
            if (!mProgressDialog.isShowing())
                mProgressDialog.show();
        }
    }


}
