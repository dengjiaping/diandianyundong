package com.fox.exercise.newversion.newact;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.fox.exercise.AbstractBaseOtherActivity;
import com.fox.exercise.FansAndNear;
import com.fox.exercise.R;
import com.fox.exercise.api.ApiJsonParser;
import com.fox.exercise.api.ApiNetException;
import com.fox.exercise.api.ApiSessionOutException;
import com.fox.exercise.newversion.adapter.ShowLikeListViewAdapter;
import com.fox.exercise.newversion.entity.SportsLike;
import com.fox.exercise.view.PullToRefreshBase.OnRefreshListener;
import com.fox.exercise.view.PullToRefreshListView;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;

import cn.ingenic.indroidsync.SportsApp;

/**
 * @author loujungang 说说点赞列表activity
 */
public class ZanListUserActivity extends AbstractBaseOtherActivity {
    private Dialog mLoadProgressDialog = null;
    private Context mcontext = this;
    // private EditText add_friend_edittext = null;
//	private Button bt_follow;
    private PullToRefreshListView mPullSearchListView = null;
    private ListView mListView = null;
    private ShowLikeListViewAdapter addFriendAdapter = null;
    // private HashSet<UserNearby> mSet = new HashSet<UserNearby>();
    private ArrayList<SportsLike> mList = new ArrayList<SportsLike>();
    private SportsApp mSportsApp;
    //	private int mSearchAddFriend = 0;
//	private final int SEARCH_BASIC = 0;
//	private final int SEARCH_NAME = 1;
    private int times = 0;
    private boolean mIsAddTask;
    //	private UserDetail self = null;
//	private String editname;
//	private int edittextCnt = 0;
    private boolean mIsRefreshing = false;
    private AddFriendTask task;
    private int find_id;// 说说id
    private int uid;// 指谁发表的说说

    @Override
    public void initIntentParam(Intent intent) {
        // TODO Auto-generated method stub
        title = getResources().getString(R.string.sports_dianzan);
        if (intent != null) {
            find_id = intent.getIntExtra("find_id", 0);
            uid = intent.getIntExtra("uid", 0);
        }
    }

    @Override
    public void initView() {
        // TODO Auto-generated method stub
        showContentView(R.layout.sports_addfriend);
        mSportsApp = (SportsApp) getApplication();
//		self = mSportsApp.getSportUser();
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
        // View search_view = LayoutInflater.from(this).inflate(
        // R.layout.friends_list_front_view, null);
        // add_friend_edittext = (EditText)search_view.findViewById(
        // R.id.add_friend_edittext);
        // mListView.addHeaderView(search_view);
        // maddfriendhandler = new AddFriendHandler();// 更新
        startAddTask();
        mPullSearchListView.setOnRefreshListener(new OnRefreshListener() {

            @Override
            public void onRefresh(int pullDirection) {
                switch (pullDirection) {
                    // case FansAndNear.MODE_DEFAULT_LOAD:
                    // if(!mIsRefreshing){
                    // mIsRefreshing = true;
                    // cancelAndStartTask();
                    // }else{
                    // mPullSearchListView.onRefreshComplete();
                    // }
                    // break;
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
    public void onPageResume() {
        // TODO Auto-generated method stub
        MobclickAgent.onPageStart("ZanListUserActivity");
    }

    @Override
    public void onPagePause() {
        // TODO Auto-generated method stub
        MobclickAgent.onPageEnd("ZanListUserActivity");
    }

    @Override
    public void onPageDestroy() {
        // TODO Auto-generated method stub
        if (mIsAddTask)
            task.cancel(true);
        mList = null;
        mSportsApp = null;
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

    private class AddFriendTask extends
            AsyncTask<Void, Void, ArrayList<SportsLike>> {

        @Override
        protected ArrayList<SportsLike> doInBackground(Void... arg0) {
            // TODO Auto-generated method stub
            ArrayList<SportsLike> list = null;
            try {
                list = ApiJsonParser.getSportsLikes(mSportsApp.getSessionId(),
                        find_id, uid);
            } catch (ApiNetException e) {
                e.printStackTrace();
            } catch (ApiSessionOutException e) {
                e.printStackTrace();
            }
            return list;
        }

        @Override
        protected void onPostExecute(ArrayList<SportsLike> list) {
            // TODO Auto-generated method stub
            super.onPostExecute(list);
            if (times == 0) {
                mList.clear();
            }
            if (list != null) {
                for (SportsLike e : list) {
                    mList.add(e);
                }
                if (addFriendAdapter == null) {
                    addFriendAdapter = new ShowLikeListViewAdapter(mcontext,
                            mList, mSportsApp);
                    mListView.setAdapter(addFriendAdapter);
                } else {
                    addFriendAdapter.notifyDataSetChanged();
                }
                if (addFriendAdapter != null)
                    addFriendAdapter.notifyDataSetChanged();
                if (mPullSearchListView != null)
                    mPullSearchListView.onRefreshComplete();
            } else {
                Toast.makeText(mcontext, getString(R.string.get_list_failure),
                        Toast.LENGTH_SHORT).show();
            }
            if (mLoadProgressDialog != null)
                if (mLoadProgressDialog.isShowing())
                    mLoadProgressDialog.dismiss();
            mIsRefreshing = false;
            mIsAddTask = false;
        }
    }

}
