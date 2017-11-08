package com.fox.exercise;

import java.util.ArrayList;
import java.util.HashSet;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import cn.ingenic.indroidsync.SportsApp;

import com.fox.exercise.api.ApiJsonParser;
import com.fox.exercise.api.ApiNetException;
import com.fox.exercise.api.ApiSessionOutException;
import com.fox.exercise.api.entity.UserFollowMsg;
import com.fox.exercise.view.PullToRefreshListView;
import com.fox.exercise.view.PullToRefreshBase.OnRefreshListener;

public class SlimFriendState extends Fragment implements OnClickListener {

    private PullToRefreshListView mPullListView = null;
    private ListView mListView = null;
    private SlimFrendAdapter mAdapter = null;
    private Dialog loadProgressDialog = null;

    private HashSet<UserFollowMsg> mSet = null;
    private ArrayList<UserFollowMsg> mList = null;

    private TextView title_name;
    private GiftsHandler mHandler = null;
    private int mTimes = 0;
    private boolean isRefresh;
    public static boolean isFlag;

    private static final int FRESH_LIST = 0x0001;

    private int userID;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.slim_friend_state, null);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//		getActivity().requestWindowFeature(Window.FEATURE_NO_TITLE);
        mSportsApp = (SportsApp) getActivity().getApplication();
///@		if (SportsApp.getInstance().getSportsUser().getMsgCounts() != null) {
///@			userID = SportsApp.getInstance().getSportsUser().getUid();
///@		}
        title_name = (TextView) getActivity().findViewById(R.id.gifts_nums);
        title_name.setText("好友动态̬");
        initViews();
        initData();
    }

    private SportsApp mSportsApp;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    private void initViews() {
        loadProgressDialog = new Dialog(getActivity(),
                R.style.sports_dialog);
        LayoutInflater mInflater = getActivity().getLayoutInflater();
        View v1 = mInflater.inflate(R.layout.sports_progressdialog, null);
        TextView message = (TextView) v1.findViewById(R.id.message);
        message.setText(R.string.sports_wait);
        v1.setMinimumWidth((int) (SportsApp.ScreenWidth * 0.8));
        loadProgressDialog.setContentView(v1);
        loadProgressDialog.show();

        mPullListView = (PullToRefreshListView) getActivity().findViewById(R.id.gifts_pull_refresh_list);
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
        mListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                // TODO Auto-generated method stub
                if (mList != null && mList.size() > arg2) {
//					UserFollowMsg item = mList.get(arg2);
//					Intent intent = new Intent(getActivity(), SportsUserBrowse.class);
//					intent.putExtra("ID", item.getId());
//					getActivity().startActivity(intent);
                }
            }
        });
    }

    private void initData() {
        mSet = new HashSet<UserFollowMsg>();
        mList = new ArrayList<UserFollowMsg>();
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
            ArrayList<UserFollowMsg> list = new ArrayList<UserFollowMsg>();
            try {
                list = (ArrayList<UserFollowMsg>) ApiJsonParser.sportsActs(
                        mSportsApp.getSessionId(), mTimes++);
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
                for (UserFollowMsg e : list) {
                    if (mSet.add(e))
                        mList.add(e);
                }
            }
            msg = Message.obtain(mHandler, FRESH_LIST);
            msg.sendToTarget();
        }
    }

    class GiftsHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case FRESH_LIST:
                    if (getActivity() == null) {
                        return;
                    }
                    if (mAdapter == null) {
                        mAdapter = new SlimFrendAdapter(mList, getActivity());
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//		case R.id.bt_back:
//			getActivity().finish();
//			break;
        }
    }
}
