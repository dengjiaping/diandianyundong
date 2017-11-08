package com.fox.exercise;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
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
import com.fox.exercise.api.entity.UserSearch;
import com.fox.exercise.http.FunctionStatic;
import com.fox.exercise.login.Tools;
import com.fox.exercise.newversion.act.PersonalPageMainActivity;
import com.fox.exercise.view.PullToRefreshBase.OnRefreshListener;
import com.fox.exercise.view.PullToRefreshListView;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;

import cn.ingenic.indroidsync.SportsApp;

public class FansListActivity extends AbstractBaseActivity {

	private static final String TAG = "FansListActivity";

	private PullToRefreshListView mPullRefreshListView;
	private boolean startAddMore = false;
	private ListView mFanListView;
	private Context context;
	private FansAdapter mListAdapter = null;
	private Dialog loadProgressDialog = null;
	private int mNumber;
	private int type;

	private boolean mIsFresh = false;

	private ArrayList<UserSearch> mBuffList = new ArrayList<UserSearch>();
	private ArrayList<UserSearch> mList = new ArrayList<UserSearch>();
	private int times = 0;

	private FansListHandler mHandler = null;

	private int userID;
	private int total;
	private static final int GET_DATA_FAILED = 0x0000;
	private static final int FRESH_LIST = 0x0001;
	private SportsApp mSportsApp;
	private long preTime = 0;
	@Override
	public void initIntentParam(Intent intent) {
		// TODO Auto-generated method stub
		title=getResources().getString(R.string.new_friends);	
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		showContentView(R.layout.sports_fans_list);
		context = this;
		mSportsApp = (SportsApp) getApplication();
		if (SportsApp.getInstance().getSportUser().getMsgCounts() != null) {
			total = SportsApp.getInstance().getSportUser().getMsgCounts()
					.getTotal();
		}
	}

	@Override
	public void setViewStatus() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPageResume() {
		// TODO Auto-generated method stub
		preTime = FunctionStatic.onResume();
		MobclickAgent.onPageStart("FansListActivity"); 
	}

	@Override
	public void onPagePause() {
		// TODO Auto-generated method stub
		FunctionStatic.onPause(this, FunctionStatic.FUNCTION_NEW_FRIEND,
				preTime);
		MobclickAgent.onPageEnd("FansListActivity"); 
	}

	@Override
	public void onPageDestroy() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		Bundle bundle = this.getIntent().getExtras();
		mNumber = bundle.getInt("number", 0);
		type = bundle.getInt("type");
		userID = bundle.getInt("uid", 0);
		if (mList == null || mList.size() == 0) {
			initViews();
			initdata();
		}
		
		mPullRefreshListView.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh(int pullDirection) {
				switch (pullDirection) {
				case FansAndNear.MODE_DEFAULT_LOAD:
					startAddMore = true;
					GetListThread loadThread = new GetListThread(times++,
							userID);
					loadThread.start();
					break;
				case FansAndNear.MODE_PULL_DOWN_TO_REFRESH:
					mIsFresh = true;
					times = 0;
					GetListThread freshThread = new GetListThread(times++,
							userID);
					freshThread.start();
					break;
				}
			}
		});
	}

	private void initViews() {
		switch (type) {
		case FansAndNear.SELF_FANS:
		case FansAndNear.OTHER_FANS:
			break;
		case FansAndNear.OTHER_FOLLOW:
		case FansAndNear.SELF_FOLLOW:
			break;
		}
		mPullRefreshListView = (PullToRefreshListView) findViewById(R.id.pull_refresh_list);
		mFanListView = mPullRefreshListView.getRefreshableView();
		Drawable  drawable= getResources().getDrawable(R.drawable.sports_bg_line);
		mFanListView.setDivider(drawable);
		mFanListView.setDividerHeight(1);
		mFanListView.setCacheColorHint(0x00000000);
		mFanListView.setOnItemClickListener(listViewClick);

		mHandler = new FansListHandler();
	}

	private void initdata() {
		if (Tools.isNetworkConnected(FansListActivity.this)) {
			loadProgressDialog = new Dialog(FansListActivity.this,
					R.style.sports_dialog);
			LayoutInflater mInflater = getLayoutInflater();
			View v1 = mInflater.inflate(R.layout.sports_progressdialog, null);
			TextView message = (TextView) v1.findViewById(R.id.message);
			message.setText(getResources().getString(R.string.sports_wait));
			v1.setMinimumWidth((int) (SportsApp.ScreenWidth * 0.8));
			loadProgressDialog.setContentView(v1);
			loadProgressDialog.setCanceledOnTouchOutside(false);
			loadProgressDialog.show();
			GetListThread thread = new GetListThread(times++, userID);
			thread.start();
		} else {
			Toast.makeText(FansListActivity.this,
					getResources().getString(R.string.error_cannot_access_net),
					Toast.LENGTH_SHORT).show();
		}
	}

	class GetListThread extends Thread {

		private int times = 0;
		private int uid = 0;

		public GetListThread(int times, int userID) {
			this.times = times;
			this.uid = userID;
		}

		@Override
		public void run() {
			Log.d(TAG, "mSportsApp.getSessionId():" + mSportsApp.getSessionId());
			Log.d(TAG, "uid:" + uid);
			ArrayList<UserSearch> list = null;
			try {
				switch (type) {
				case FansAndNear.SELF_FANS:
				case FansAndNear.OTHER_FANS:
					if (SportsApp.getInstance().getSportUser().getMsgCounts() != null) {
						total = total
								- SportsApp.getInstance().getSportUser()
										.getMsgCounts().getFans();
						SportsApp.getInstance().getSportUser().getMsgCounts()
								.setFans(0);
						SportsApp.getInstance().getSportUser().getMsgCounts()
								.setTotal(total);
					}
					list = (ArrayList<UserSearch>) ApiJsonParser.userFan(
							mSportsApp.getSessionId(), uid, times);
					break;
				case FansAndNear.SELF_FOLLOW:
				case FansAndNear.OTHER_FOLLOW:
					list = (ArrayList<UserSearch>) ApiJsonParser.userFollow(
							mSportsApp.getSessionId(), uid, times);
					break;
				}
			} catch (ApiNetException e) {
				e.printStackTrace();
			} catch (ApiSessionOutException e) {
				e.printStackTrace();
			}

			Message msg = null;

			if (list != null) {
				if (mIsFresh) {
					mBuffList = list;
				} else {
					mBuffList.addAll(list);
				}
				msg = Message.obtain(mHandler, FRESH_LIST);
				msg.sendToTarget();
			} else {
				msg = Message.obtain(mHandler, GET_DATA_FAILED);
				msg.sendToTarget();
			}
		}
	}

	class FansListHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case FRESH_LIST:
				mList.clear();
				for (UserSearch item : mBuffList) {
					mList.add(item);
				}

				if (startAddMore) {
					mListAdapter.notifyDataSetChanged();
					mPullRefreshListView.onRefreshComplete();
					startAddMore = false;
				}
				Log.d(TAG, "mList.size:" + mList.size());
				
				if (mListAdapter == null) {
					mListAdapter = new FansAdapter(context, mList,
							mSportsApp);
					mFanListView.setAdapter(mListAdapter);
				}else if (mIsFresh) {
					mListAdapter.notifyDataSetChanged();
					mPullRefreshListView.onRefreshComplete();
					mIsFresh = false;
				} 

				if (loadProgressDialog != null)
					if (loadProgressDialog.isShowing())
						loadProgressDialog.dismiss();
				break;
			}
		}
	}

	private OnItemClickListener listViewClick = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position,
				long arg3) {
			if(!mSportsApp.isOpenNetwork()){
				Toast.makeText(context, getResources().getString(R.string.newwork_not_connected), Toast.LENGTH_LONG)
				.show();
				return;
			}
//			Intent intent = new Intent(context,
//					PedometerActivity.class);
			Intent intent = new Intent(context,
					PersonalPageMainActivity.class);
			intent.putExtra("ID", mList.get(position).getId());
			startActivity(intent);
		}
	};

}
