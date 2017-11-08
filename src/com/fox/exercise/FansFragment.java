package com.fox.exercise;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.fox.exercise.api.ApiBack;
import com.fox.exercise.api.ApiConstant;
import com.fox.exercise.api.ApiJsonParser;
import com.fox.exercise.api.ApiNetException;
import com.fox.exercise.api.ApiSessionOutException;
import com.fox.exercise.api.entity.UserDetail;
import com.fox.exercise.api.entity.UserNearby;
import com.fox.exercise.login.LocationInfo;
import com.fox.exercise.view.PullToRefreshBase.OnRefreshListener;
import com.fox.exercise.view.PullToRefreshListView;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cn.ingenic.indroidsync.SportsApp;

/**
 * Created by zhonghuibin on 2016/7/29. 个人主页粉丝页面
 */

public class FansFragment extends Fragment {

    private Dialog mLoadProgressDialog = null;
    private PullToRefreshListView mPullSearchListView = null;
    private ListView mListView = null;
    private MyFriendsAdapter addFriendAdapter = null;
    private ArrayList<UserNearby> mList = new ArrayList<UserNearby>();
    private SportsApp mSportsApp;
    private int mSearchAddFriend = 0;
    private final int SEARCH_BASIC = 0;
    private int times = 0;
    private final int SEARCH_NAME = 1;
    private static final int LOCATE_FINISH = 0x0003;
    private static final int LOCATE_FAILED = 0x0004;
    private UserDetail self = null;
    private String editname;
    private int edittextCnt = 0;
    private boolean isFirst = true;
    public EditText add_friend_edittext;
    private static final String TAG = "FriendsFragment";
    private TextView mDialogMessage;
    private boolean mIsFriendsTask;
    private FriendsTask task;
    public boolean isLocation;
    public boolean locationFinish;
    private LocationTask lTask;
    private boolean uploadLocSuccess = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        mSportsApp = (SportsApp) getActivity().getApplication();
        self = mSportsApp.getSportUser();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        return inflater.inflate(R.layout.sports_addfriend, null);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
        initData();
    }

    private void initData() {
        // 每次切换fragment时调用的方法
        init();
        mListView = mPullSearchListView.getRefreshableView();
        Drawable drawable = getResources().getDrawable(
                R.drawable.sports_bg_line);
        waitShow();
        mListView.setDivider(drawable);
        mListView.setDividerHeight(1);
        View search_view = LayoutInflater.from(getActivity()).inflate(
                R.layout.friends_list_front_view, null);
        add_friend_edittext = (EditText) search_view
                .findViewById(R.id.add_friend_edittext);
        mSportsApp.setmFriends_editext(add_friend_edittext);
        mListView.addHeaderView(search_view);
        if (mSportsApp.isOpenNetwork()) {
            lTask = new LocationTask();
            lTask.execute();
        } else {
            Toast.makeText(
                    getActivity(),
                    getResources().getString(
                            R.string.error_cannot_access_net),
                    Toast.LENGTH_SHORT).show();
            if (mLoadProgressDialog != null)
                if (mLoadProgressDialog.isShowing())
                    mLoadProgressDialog.dismiss();
            try {

                // 保存
                SharedPreferences preferences = getActivity()
                        .getSharedPreferences("UserNearbylist", 0);
                String contents = preferences.getString("UserNearbylist_info",
                        "");
                if (contents != null && !"".equals(contents)) {
                    JSONArray jsonArray = new JSONObject(contents)
                            .getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = (JSONObject) jsonArray.opt(i);
                        Log.e("hjtest", jsonObject.toString());
                        UserNearby users = new UserNearby();
                        users.setId(jsonObject.getInt("id"));
                        users.setImg(ApiConstant.URL
                                + jsonObject.getString("img"));
                        users.setName(jsonObject.getString("name"));
                        users.setDistance(jsonObject.getInt("distance"));
                        users.setFollowStatus((jsonObject.getString("status") == null || "null"
                                .equals(jsonObject.getString("status"))) ? 2
                                : Integer.parseInt(jsonObject
                                .getString("status")));
                        users.setSex(jsonObject.getInt("sex") == 1 ? "man"
                                : "woman");
                        users.setTime(jsonObject.getLong("local_time"));
                        users.setBirthday(jsonObject.getString("brithday"));
                        mList.add(users);
                    }
                    if (addFriendAdapter != null) {
                        addFriendAdapter.notifyDataSetChanged();
                    } else {
                        mListView.setAdapter(null);
                        addFriendAdapter = new MyFriendsAdapter(getActivity(),
                                mList, mSportsApp);
                        mListView.setAdapter(addFriendAdapter);
                    }


                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        add_friend_edittext
                .addTextChangedListener(add_friend_editchangelistener);
        hideedit();

        mPullSearchListView.setOnRefreshListener(new OnRefreshListener() {

            @Override
            public void onRefresh(int pullDirection) {
                switch (pullDirection) {
                    case FansAndNear.MODE_DEFAULT_LOAD:
                        if (mSportsApp.isOpenNetwork()) {
                            times++;
                            cancelAndStartTask();
                        } else {
                            Toast.makeText(
                                    getActivity(),
                                    getResources().getString(
                                            R.string.error_cannot_access_net),
                                    Toast.LENGTH_SHORT).show();
                            mPullSearchListView.onRefreshComplete();
                        }

                        break;
                    case FansAndNear.MODE_PULL_DOWN_TO_REFRESH:
                        if (mSportsApp.isOpenNetwork()) {
                            if (isLocation) {
                                times = 0;
                                cancelAndStartTask();
                            } else {
                                if (!locationFinish)
                                    lTask.cancel(true);
                                lTask = new LocationTask();
                                lTask.execute();
                            }
                        } else {
                            Toast.makeText(
                                    getActivity(),
                                    getResources().getString(
                                            R.string.error_cannot_access_net),
                                    Toast.LENGTH_SHORT).show();
                            mPullSearchListView.onRefreshComplete();
                        }

                        break;
                }
            }
        });
    }

    private class LocationTask extends AsyncTask<Void, Integer, Boolean> {

        @Override
        protected Boolean doInBackground(Void... arg0) {
            // TODO Auto-generated method stub
            long startTime = System.currentTimeMillis();
            if (!uploadLocSuccess) {
                while ((LocationInfo.latitude == null
                        || LocationInfo.latitude.equals("") || LocationInfo.latitude
                        .equals("4.9E-324"))
                        || (LocationInfo.longitude == null
                        || LocationInfo.longitude.equals("") || LocationInfo.longitude
                        .equals("4.9E-324"))) {
                    Log.v(TAG, "wmh while LocationInfo.latitude="
                            + LocationInfo.latitude
                            + " LocationInfo.longitude="
                            + LocationInfo.longitude);
                    if (StateActivity.registerLocation() != 0)
                        continue;

                    if (getActivity() == null || getActivity().isFinishing()) {
                        StateActivity.unregisterLocation();
                        return false;
                    } else if ((System.currentTimeMillis() - startTime) < 60 * 1500) {
                        Thread.yield();
                        // Log.d(TAG, "*******检测定位监听@@@@********");
                    } else {
                        publishProgress(LOCATE_FAILED);
                        StateActivity.unregisterLocation();
                        return false;
                    }
                }
                publishProgress(LOCATE_FINISH);
                ApiBack back = null;
                Log.v(TAG, "wmh LocationInfo.latitude=" + LocationInfo.latitude
                        + " LocationInfo.longitude=" + LocationInfo.longitude);
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
            return true;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            // TODO Auto-generated method stub
            switch (values[0]) {
                case LOCATE_FAILED:
                    dismissDialog();
                    break;
                case LOCATE_FINISH:
                    mDialogMessage.setText(R.string.sports_wait);
                    break;
                default:
                    break;
            }
            StateActivity.unregisterLocation();
        }

        @Override
        protected void onPostExecute(Boolean back) {
            // TODO Auto-generated method stub
            if (back) {
                isLocation = true;
                startFriendsTask();
            } else {
                dismissDialog();
                Toast.makeText(getActivity(),
                        getString(R.string.location_fail), Toast.LENGTH_SHORT)
                        .show();
            }
            locationFinish = true;
        }

    }

    private class FriendsTask extends
            AsyncTask<Void, Void, ArrayList<UserNearby>> {
        @Override
        protected ArrayList<UserNearby> doInBackground(Void... arg0) {
            // TODO Auto-generated method stub
            ArrayList<UserNearby> list = null;
            if (mSearchAddFriend == SEARCH_BASIC) {
                try {
                    list = (ArrayList<UserNearby>) ApiJsonParser.getFansList(
                            times, mSportsApp.getSessionId(), getActivity());
                } catch (ApiNetException e) {
                    e.printStackTrace();
                } catch (ApiSessionOutException e) {
                    e.printStackTrace();
                }
            } else if (mSearchAddFriend == SEARCH_NAME) {
                try {
                    list = (ArrayList<UserNearby>) ApiJsonParser
                            .getFansbyNameNew(times, editname,
                                    mSportsApp.getSessionId());
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
            super.onPostExecute(list);
            if (times == 0)
                mList.clear();
            if (list != null) {
                for (UserNearby e : list) {
                    mList.add(e);
                }
                if (times == 0) {
                    Log.d(TAG, "handleMessage mImgTimes == 0");
                    mListView.setAdapter(null);
                    addFriendAdapter = new MyFriendsAdapter(getActivity(),
                            mList, mSportsApp);
                    addFriendAdapter.notifyDataSetChanged();
                    mListView.setAdapter(addFriendAdapter);
                    mPullSearchListView.onRefreshComplete();
                } else {
                    if (addFriendAdapter == null) {
                        addFriendAdapter = new MyFriendsAdapter(getActivity(),
                                mList, mSportsApp);
                    }
                    addFriendAdapter.notifyDataSetChanged();
                    mPullSearchListView.onRefreshComplete();
                }
                dismissDialog();
            } else { // 获取失败
                if (list == null) {
                    if (getActivity() != null)
                        Toast.makeText(
                                getActivity(),
                                getActivity().getString(
                                        R.string.get_list_failure),
                                Toast.LENGTH_SHORT).show();
                    dismissDialog();
                }
            }
            add_friend_edittext.requestFocus();
            mIsFriendsTask = false;
        }
    }

    private void startFriendsTask() {
        // TODO Auto-generated method stub
        mIsFriendsTask = true;
        task = new FriendsTask();
        task.execute();
    }

    public void dismissDialog() {
        // TODO Auto-generated method stub
        if (mLoadProgressDialog != null)
            if (mLoadProgressDialog.isShowing())
                mLoadProgressDialog.dismiss();
    }

    private void cancelAndStartTask() {
        // TODO Auto-generated method stub
        if (mIsFriendsTask)
            task.cancel(true);
        startFriendsTask();
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("FriendsFragment");
    }

    @Override
    public void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        MobclickAgent.onPageEnd("FriendsFragment");
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        if (mIsFriendsTask)
            task.cancel(true);
    }

    private void init() {
        mPullSearchListView = (PullToRefreshListView) getActivity()
                .findViewById(R.id.add_friend_pull_refresh_list);
    }

    private void waitShow() {
        if (mLoadProgressDialog == null) {
            mLoadProgressDialog = new Dialog(getActivity(),
                    R.style.sports_dialog);
            LayoutInflater mInflater = getActivity().getLayoutInflater();
            View v1 = mInflater.inflate(R.layout.bestgirl_progressdialog, null);
            mDialogMessage = (TextView) v1.findViewById(R.id.message);
            mDialogMessage.setText(R.string.bestgirl_wait);
            v1.setMinimumWidth((int) (SportsApp.ScreenWidth * 0.8));
            mLoadProgressDialog.setContentView(v1);
            mLoadProgressDialog.setCanceledOnTouchOutside(false);
        }
        if (mLoadProgressDialog != null)
            if (!mLoadProgressDialog.isShowing()
                    && !getActivity().isFinishing())
                mLoadProgressDialog.show();
        Log.i(TAG, "isFirstshow----");
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

    // 软键盘消失
    private void hideedit() {
        add_friend_edittext.clearFocus();
        InputMethodManager imm = (InputMethodManager) getActivity()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(add_friend_edittext.getWindowToken(), 0);
    }

}
