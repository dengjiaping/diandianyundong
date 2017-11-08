package com.fox.exercise;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
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
import com.fox.exercise.api.entity.UserNearby;
import com.fox.exercise.http.FunctionStatic;
import com.fox.exercise.newversion.act.PersonalPageMainActivity;
import com.fox.exercise.view.PullToRefreshBase.OnRefreshListener;
import com.fox.exercise.view.PullToRefreshListView;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.HashSet;

import cn.ingenic.indroidsync.SportsApp;

public class VisitorMyActivity extends AbstractBaseActivity {

    private PullToRefreshListView mPullListView = null;
    private ListView mListView = null;
    private VisitorMyAdapter mAdapter = null;
    private Dialog loadProgressDialog = null;

    private HashSet<UserNearby> mSet = null;
    private ArrayList<UserNearby> mList = null;

    //private TextView title_name;
    private GiftsHandler mHandler = null;
    private int mTimes = 0;
    private boolean isRefresh;
    // private boolean mIsRefresh=false;
    public static boolean isFlag;
    private static final int FRESH_LIST = 0x0001;

    //	private static final String TAG = "GiftsMyActivity";
    private int userID;
    // private int total;

    private SportsApp mSportsApp;
    private long preTime = 0;

    @Override
    public void initIntentParam(Intent intent) {
        // TODO Auto-generated method stub
        title = getResources().getString(R.string.my_visitor);
    }

    @Override
    public void initView() {
        // TODO Auto-generated method stub
        showContentView(R.layout.sports_fansorme);
        mSportsApp = (SportsApp) getApplication();
        if (SportsApp.getInstance().getSportUser().getMsgCounts() != null) {
            userID = SportsApp.getInstance().getSportUser().getUid();
        }
    }

    @Override
    public void setViewStatus() {
        // TODO Auto-generated method stub
        initViews();
        initData();
    }

    @Override
    public void onPageResume() {
        // TODO Auto-generated method stub
        preTime = FunctionStatic.onResume();
        MobclickAgent.onPageStart("VisitorMyActivity");
    }

    @Override
    public void onPagePause() {
        // TODO Auto-generated method stub
        FunctionStatic.onPause(this, FunctionStatic.FUNCTION_VISITER, preTime);
        MobclickAgent.onPageEnd("VisitorMyActivity");
    }

    @Override
    public void onPageDestroy() {
        // TODO Auto-generated method stub
        if (mList != null) {
            mList.clear();
            mList = null;
        }
        if (mSet != null) {
            mSet.clear();
            mSet = null;
        }
        mSportsApp = null;
    }

    private void initViews() {
        loadProgressDialog = new Dialog(VisitorMyActivity.this, R.style.sports_dialog);
        LayoutInflater mInflater = getLayoutInflater();
        View v1 = mInflater.inflate(R.layout.sports_progressdialog, null);
        TextView message = (TextView) v1.findViewById(R.id.message);
        message.setText(R.string.sports_wait);
        v1.setMinimumWidth((int) (SportsApp.ScreenWidth * 0.8));
        loadProgressDialog.setContentView(v1);
        loadProgressDialog.setCanceledOnTouchOutside(false);
        loadProgressDialog.show();

        mPullListView = (PullToRefreshListView) findViewById(R.id.gifts_pull_refresh_list);
        mPullListView.setOnRefreshListener(new OnRefreshListener() {

            @Override
            public void onRefresh(int pullDirection) {
                switch (pullDirection) {
                    case FansAndNear.MODE_PULL_DOWN_TO_REFRESH:
                        mTimes = 0;
                        isRefresh = true;
                        isFlag = true;
                        GetGiftsThread newGiftsThread = new GetGiftsThread(userID);
                        newGiftsThread.start();
                        break;
                    case FansAndNear.MODE_DEFAULT_LOAD:
                        isRefresh = false;
                        isFlag = true;
                        GetGiftsThread loadGiftsThread = new GetGiftsThread(userID);
                        loadGiftsThread.start();
                        break;
                }

            }
        });
        mListView = mPullListView.getRefreshableView();
        Drawable drawable = getResources().getDrawable(R.drawable.sports_bg_line);
        mListView.setDivider(drawable);
        mListView.setDividerHeight(1);
        mListView.setOnItemClickListener(listViewClick);
    }

    private void initData() {
        mSet = new HashSet<UserNearby>();
        mList = new ArrayList<UserNearby>();
        mHandler = new GiftsHandler();
        GetGiftsThread thread = new GetGiftsThread(userID);
        thread.start();
    }

    class GetGiftsThread extends Thread {
        private int uid = 0;

        public GetGiftsThread(int userID) {
            this.uid = userID;
        }

        @Override
        public void run() {
            ArrayList<UserNearby> list = new ArrayList<UserNearby>();
            try {
                list = (ArrayList<UserNearby>) ApiJsonParser.getSportsVisitor(mSportsApp.getSessionId(), mTimes++);
                if (SportsApp.getInstance().getSportUser().getMsgCounts() != null) {
                    SportsApp.getInstance().getSportUser().getMsgCounts().setSportVisitor(0);
                }
            } catch (ApiNetException e) {
                e.printStackTrace();
            } catch (ApiSessionOutException e) {
                e.printStackTrace();
            }
            Message msg = null;
            if (isRefresh == true) {
                mSet.clear();
                mList.clear();
            }
            if (list != null) {
                for (UserNearby e : list) {
                    if (mSet.add(e))
                        mList.add(e);
                }
                msg = Message.obtain(mHandler, FRESH_LIST);
                msg.sendToTarget();
            }
        }
    }

    class GiftsHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case FRESH_LIST:
                    if (mAdapter == null) {
                        mAdapter = new VisitorMyAdapter(mList, VisitorMyActivity.this);
                        mListView.setAdapter(mAdapter);
                    } else {
                        mAdapter.notifyDataSetChanged();
                    }
                    if (isRefresh == true) {
                        mAdapter.notifyDataSetChanged();
                    }
                    if (loadProgressDialog != null)
                        if (loadProgressDialog.isShowing())
                            loadProgressDialog.dismiss();
                    mPullListView.onRefreshComplete();
                    isFlag = false;
                    break;
            }
        }

    }

    private OnItemClickListener listViewClick = new OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
            if (!VisitorMyActivity.isFlag) {
                if (mAdapter.getIndexGL() == position || mAdapter.getIndexRZ() == position) {
                } else if (mList != null && position < mList.size()) {
                    if (!mSportsApp.isOpenNetwork()) {
                        Toast.makeText(VisitorMyActivity.this, "网络未连接，请检查网络！", Toast.LENGTH_LONG)
                                .show();
                        return;
                    }
//					Intent intent = new Intent(VisitorMyActivity.this, PedometerActivity.class);
                    Intent intent = new Intent(VisitorMyActivity.this, PersonalPageMainActivity.class);
                    intent.putExtra("ID", mList.get(position).getId());
                    startActivity(intent);
                }
            }
        }
    };


}
