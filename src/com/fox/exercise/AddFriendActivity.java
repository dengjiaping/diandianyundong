package com.fox.exercise;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.fox.exercise.api.ApiJsonParser;
import com.fox.exercise.api.ApiNetException;
import com.fox.exercise.api.ApiSessionOutException;
import com.fox.exercise.api.entity.UserDetail;
import com.fox.exercise.api.entity.UserNearby;
import com.fox.exercise.newversion.newact.NewFansActivity;
import com.fox.exercise.newversion.newact.NewGuanZhuActivity;
import com.fox.exercise.view.PullToRefreshBase.OnRefreshListener;
import com.fox.exercise.view.PullToRefreshListView;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;

import cn.ingenic.indroidsync.SportsApp;

public class AddFriendActivity extends AbstractBaseActivity {
    private Dialog mLoadProgressDialog = null;
    private Context mcontext = this;
    private EditText add_friend_edittext = null;
    //	private Button bt_follow;
    private PullToRefreshListView mPullSearchListView = null;
    private ListView mListView = null;
    private AddFriendListViewAdapter addFriendAdapter = null;
    //	private HashSet<UserNearby> mSet = new HashSet<UserNearby>();
    private ArrayList<UserNearby> mList = new ArrayList<UserNearby>();
    private SportsApp mSportsApp;
    private int mSearchAddFriend = 0;
    private final int SEARCH_BASIC = 0;
    private final int SEARCH_NAME = 1;
    private int times = 0;
    private boolean mIsAddTask;
    private UserDetail self = null;
    private String editname;
    private int edittextCnt = 0;
    private boolean mIsRefreshing = false;
    private AddFriendTask task;
    private int flag;

    @Override
    public void initIntentParam(Intent intent) {
        // TODO Auto-generated method stub
        title = getResources().getString(R.string.add_friend);
        if (intent != null){
            flag = intent.getIntExtra("FromFans",0);
        }
    }

    @Override
    public void initView() {
        // TODO Auto-generated method stub
        showContentView(R.layout.sports_addfriend);
        mSportsApp = (SportsApp) getApplication();
        self = mSportsApp.getSportUser();
        init();
        mListView = mPullSearchListView.getRefreshableView();
    }

    @Override
    public void setViewStatus() {
        // TODO Auto-generated method stub
        Drawable drawable = getResources().getDrawable(
                R.drawable.sports_bg_line);
        waitShow();
        mListView.setDivider(drawable);
        mListView.setDividerHeight(1);
        leftButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (flag != 2){
                    startActivity(new Intent(AddFriendActivity.this, NewGuanZhuActivity.class));
                    finish();
                }else{
                    startActivity(new Intent(AddFriendActivity.this, NewFansActivity.class));
                    finish();
                }
            }
        });
        View search_view = LayoutInflater.from(this).inflate(
                R.layout.friends_list_front_view, null);
        add_friend_edittext = (EditText) search_view.findViewById(
                R.id.add_friend_edittext);
        mListView.addHeaderView(search_view);
        //maddfriendhandler = new AddFriendHandler();// 更新
        startAddTask();
        add_friend_edittext
                .addTextChangedListener(add_friend_editchangelistener);

        mPullSearchListView.setOnRefreshListener(new OnRefreshListener() {

            @Override
            public void onRefresh(int pullDirection) {
                switch (pullDirection) {
                    case FansAndNear.MODE_DEFAULT_LOAD:
                        if (!mIsRefreshing) {
                            mIsRefreshing = true;
                            cancelAndStartTask();
                        } else {
                            mPullSearchListView.onRefreshComplete();
                        }
                        break;
                    case FansAndNear.MODE_PULL_DOWN_TO_REFRESH:
                        if (!mIsRefreshing) {
                            times = 0;
                            mIsRefreshing = true;
                            cancelAndStartTask();
                        } else {
                            mPullSearchListView.onRefreshComplete();
                        }
                        break;
                }
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK: {
                if (flag != 2){
                    startActivity(new Intent(AddFriendActivity.this, NewGuanZhuActivity.class));
                    finish();
                }else{
                    startActivity(new Intent(AddFriendActivity.this, NewFansActivity.class));
                    finish();
                }
            }

        }
        return super.onKeyDown(keyCode, event);
    }

    private void startAddTask() {
        // TODO Auto-generated method stub
        mIsAddTask = true;
        task = new AddFriendTask();
        task.execute();
    }

    private void cancelAndStartTask() {
        // TODO Auto-generated method stub
        if (mIsAddTask)
            task.cancel(true);
        startAddTask();
    }

    @Override
    public void onPageResume() {
        MobclickAgent.onPageStart("AddFriendActivity");
    }

    @Override
    public void onPagePause() {
        MobclickAgent.onPageEnd("AddFriendActivity");
    }

    @Override
    public void onPageDestroy() {
        // TODO Auto-generated method stub
        if (mIsAddTask)
            task.cancel(true);
        if (mList != null) {
            mList.clear();
            mList = null;
        }
    }

    private void init() {
//		bt_follow = (Button) findViewById(R.id.bt_follow);
        mPullSearchListView = (PullToRefreshListView) findViewById(R.id.add_friend_pull_refresh_list);
    }

    private void waitShow() {
        if (mLoadProgressDialog == null) {
            mLoadProgressDialog = new Dialog(this, R.style.sports_dialog);
            LayoutInflater mInflater = getLayoutInflater();
            View v1 = mInflater.inflate(R.layout.bestgirl_progressdialog, null);
            TextView message = (TextView) v1.findViewById(R.id.message);
            message.setText(R.string.bestgirl_wait);
            v1.setMinimumWidth((int) (SportsApp.ScreenWidth * 0.8));
            mLoadProgressDialog.setContentView(v1);
            mLoadProgressDialog.setCanceledOnTouchOutside(false);
        }
        if (mLoadProgressDialog != null)
            if (!mLoadProgressDialog.isShowing()
                    && !((Activity) mcontext).isFinishing())
                mLoadProgressDialog.show();
    }

    // 监听edit
    private TextWatcher add_friend_editchangelistener = new TextWatcher() {

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
            editname = add_friend_edittext.getText().toString();
            int txtCnt = editname.length();
            if (txtCnt != edittextCnt && txtCnt != 0) {
                times = 0;
                edittextCnt = txtCnt;
                mSearchAddFriend = SEARCH_NAME;
            } else if (txtCnt != edittextCnt && txtCnt == 0) {
                times = 0;
                edittextCnt = txtCnt;
                mSearchAddFriend = SEARCH_BASIC;
            }
            cancelAndStartTask();
        }
    };

    private class AddFriendTask extends AsyncTask<Void, Void, ArrayList<UserNearby>> {

        @Override
        protected ArrayList<UserNearby> doInBackground(Void... arg0) {
            // TODO Auto-generated method stub
            ArrayList<UserNearby> list = null;
            if (mSearchAddFriend == SEARCH_BASIC) {
                try {
                    list = (ArrayList<UserNearby>) ApiJsonParser.getNearby(
                            times, mSportsApp.getSessionId());
                } catch (ApiNetException e) {
                    e.printStackTrace();
                } catch (ApiSessionOutException e) {
                    e.printStackTrace();
                }
            } else if (mSearchAddFriend == SEARCH_NAME) {
                try {
                    list = (ArrayList<UserNearby>) ApiJsonParser
                            .getFriendbyName(times, editname, self.getUid());
                } catch (ApiNetException e) {
                    e.printStackTrace();
                } catch (ApiSessionOutException e) {
                    e.printStackTrace();
                }
            }
            return list;
        }

        @Override
        protected void onPostExecute(ArrayList<UserNearby> list) {
            // TODO Auto-generated method stub
            super.onPostExecute(list);
            if (times == 0) {
                mList.clear();
            }
            if (list != null) {
                for (UserNearby e : list) {
                    mList.add(e);
                }
                times++;
                if (times == 1) {
                    if (mListView != null) {
                        mListView.setAdapter(null);
                        addFriendAdapter = new AddFriendListViewAdapter(
                                mcontext, mList, mSportsApp);
                        mListView.setAdapter(addFriendAdapter);
                    }
                    if (mPullSearchListView != null)
                        mPullSearchListView.onRefreshComplete();
                } else {
                    if (addFriendAdapter == null) {
                        addFriendAdapter = new AddFriendListViewAdapter(
                                mcontext, mList, mSportsApp);
                    }
                    if (addFriendAdapter != null)
                        addFriendAdapter.notifyDataSetChanged();
                    if (mPullSearchListView != null)
                        mPullSearchListView.onRefreshComplete();
                }
            } else {
                Toast.makeText(mcontext, getString(R.string.get_list_failure), Toast.LENGTH_SHORT)
                        .show();
            }
            if (mLoadProgressDialog != null)
                if (mLoadProgressDialog.isShowing())
                    mLoadProgressDialog.dismiss();
            mIsRefreshing = false;
            mIsAddTask = false;
            add_friend_edittext.requestFocus();
        }
    }
}
