package com.fox.exercise;

import android.app.Dialog;
import android.content.ClipboardManager;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.fox.exercise.api.ApiJsonParser;
import com.fox.exercise.api.ApiNetException;
import com.fox.exercise.api.ApiSessionOutException;
import com.fox.exercise.api.entity.SysMsg;
import com.fox.exercise.view.PullToRefreshBase.OnRefreshListener;
import com.fox.exercise.view.PullToRefreshListView;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.HashSet;

import cn.ingenic.indroidsync.SportsApp;

public class SysMessageMyActivity extends AbstractBaseActivity {

    private PullToRefreshListView mPullListView = null;
    private ListView mListView = null;
    private SysMessageMyAdapter mAdapter = null;
    private Dialog loadProgressDialog = null;

    private HashSet<SysMsg> mSet = null;
    private ArrayList<SysMsg> mList = null;

    //private TextView title_name;
    private GiftsHandler mHandler = null;
    private int mTimes = 0;
    private boolean isRefresh;
    // private boolean mIsRefresh=false;

    private static final int FRESH_LIST = 0x0001;

    //	private static final String TAG = "GiftsMyActivity";
    private int userID = 0;
    // private int total;

    private SportsApp mSportsApp;
    private String copyString;

    @Override
    public void initIntentParam(Intent intent) {
        // TODO Auto-generated method stub
        title = getResources().getString(R.string.sports_sys_message);
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
        MobclickAgent.onPageStart("SysMessageMyActivity");
    }

    @Override
    public void onPagePause() {
        MobclickAgent.onPageEnd("SysMessageMyActivity");
    }

    @Override
    public void onPageDestroy() {
        // TODO Auto-generated method stub
        mSportsApp = null;
    }

    private void initViews() {
        loadProgressDialog = new Dialog(SysMessageMyActivity.this, R.style.sports_dialog);
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
                        GetGiftsThread newGiftsThread = new GetGiftsThread(userID);
                        newGiftsThread.start();
                        break;
                    case FansAndNear.MODE_DEFAULT_LOAD:
                        isRefresh = false;
                        GetGiftsThread loadGiftsThread = new GetGiftsThread(userID);
                        loadGiftsThread.start();
                        break;
                }

            }
        });
        mListView = mPullListView.getRefreshableView();
        mListView.setDivider(new ColorDrawable(Color.parseColor("#e5e5e5")));
        mListView.setDividerHeight(12);
        mListView.setOnItemLongClickListener(new OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           int position, long id) {
                // TODO Auto-generated method stub
                copyString = mList.get(position).getContent();
                return false;
            }
        });
    }

    private void initData() {
        mSet = new HashSet<SysMsg>();
        mList = new ArrayList<SysMsg>();
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
            ArrayList<SysMsg> list = new ArrayList<SysMsg>();
            try {
                if (SportsApp.getInstance().getSportUser().getMsgCounts() != null) {
                    SportsApp.getInstance().getSportUser().getMsgCounts().setSysmsgsports(0);
                }
                list = (ArrayList<SysMsg>) ApiJsonParser.getSysmsg(mSportsApp.getSessionId(), mTimes++);
                if (SportsApp.getInstance().getSportUser().getMsgCounts() != null) {
                    SportsApp.getInstance().getSportUser().getMsgCounts().setSysmsgsports(0);
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
                for (SysMsg e : list) {
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
                        mAdapter = new SysMessageMyAdapter(mList, SysMessageMyActivity.this);
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

                    break;
            }
        }

    }

    /* (non-Javadoc)
     * @see android.app.Activity#onContextItemSelected(android.view.MenuItem)
     */
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        ClipboardManager cmb = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        cmb.setText(copyString);
        Toast.makeText(this, R.string.copy_success, Toast.LENGTH_SHORT).show();
        return super.onContextItemSelected(item);
    }

/*
    @Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_back:
			finish();
			break;
		}
	}*/

}
