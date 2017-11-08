package com.fox.exercise;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.fox.exercise.api.ApiConstant;
import com.fox.exercise.api.ApiJsonParser;
import com.fox.exercise.api.ApiNetException;
import com.fox.exercise.api.ApiSessionOutException;
import com.fox.exercise.api.entity.UserRank;
import com.fox.exercise.bitmap.util.ImageResizer;
import com.fox.exercise.view.PullToRefreshGridView;
import com.fox.exercise.view.PullToRefreshBase.OnRefreshListener;

import cn.ingenic.indroidsync.SportsApp;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

public class WholeFragment extends Fragment {

    private static final String TAG = "WholeFragment";
    private PullToRefreshGridView wholePulltoListView;
    private GridView wholelListView;
    private ImageResizer mImageWorker;
    private SportsApp sportsApp;
    private WholeGridViewAdapter wholeGridViewAdapter;
    private SportExceptionHandler mExceptionHandler = null;
    private Dialog mProgressDialog;
    private int times = 0;
    private List<UserRank> allUserRanks = new ArrayList<UserRank>();

    private static final int PULL_REFRESH = 0;
    private static final int PULL_UP_REFRESH = 1;//上拉翻页
    private static final int PULL_DOWN_REFRESH = 2;
    private TextView tv_refresh;
//    private boolean isFirst=true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sportsApp = (SportsApp) getActivity().getApplication();
        mImageWorker = sportsApp.getImageWorker(getActivity(), 154, 154);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.sports_popular_whole_gridview, null);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }

    private void initData() {
        wholePulltoListView = (PullToRefreshGridView) getActivity().findViewById(R.id.whole_pull_refresh_grid);
        tv_refresh = (TextView) getActivity().findViewById(R.id.tv_refresh);
        wholelListView = wholePulltoListView.getRefreshableView();


        mExceptionHandler = new SportExceptionHandler(getActivity());
        initProgressDialog();

        if (SportsUtilities.checkConnection(getActivity())) {
            if (mProgressDialog != null) {
                mProgressDialog.show();
            }
            new WholeGetDataTask(PULL_REFRESH).execute("0");
        } else {
            try {

                //保存
                SharedPreferences preferences = getActivity().getSharedPreferences("UserRankList1", 0);
                String content = preferences.getString("UserRankList_info", "");
                if (content != null && !"".equals(content)) {
                    JSONArray jsonArray = new JSONObject(content)
                            .getJSONArray("data");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = (JSONObject) jsonArray.opt(i);
                        Log.e("hjtest", jsonObject.toString());
                        UserRank users = new UserRank();
                        users.setId(jsonObject.getInt("id"));
                        users.setImg(ApiConstant.URL
                                + jsonObject.getString("img"));
                        users.setRankNumber(jsonObject.getInt("ranks"));
                        users.setName(jsonObject.getString("name"));
                        users.setAuthStatus(jsonObject.getInt("authstatus"));
                        users.setSex(jsonObject.getInt("sex"));
                        allUserRanks.add(users);
                    }

                    if (wholeGridViewAdapter != null) {
                        wholeGridViewAdapter.notifyDataSetChanged();
                    } else {
                        wholeGridViewAdapter = new WholeGridViewAdapter(getActivity(), mImageWorker, allUserRanks);
                        wholelListView.setAdapter(wholeGridViewAdapter);
                        wholelListView.setSelector(new ColorDrawable(Color.TRANSPARENT));
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            Toast.makeText(getActivity(), getResources().getString(R.string.error_cannot_access_net), 1).show();
        }

        wholePulltoListView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(int pullDirection) {
                Log.i("TAG", "refresh to loading data");
                Log.i("refresh", "refresh1");

                if (sportsApp.mIsNetWork) {
                    if (pullDirection == FansAndNear.MODE_PULL_DOWN_TO_REFRESH) {
                        // 下拉刷新
                        new WholeGetDataTask(PULL_DOWN_REFRESH).execute("0");
                    } else if (pullDirection == FansAndNear.MODE_DEFAULT_LOAD) {
                        Log.e(TAG, "------------ 上拉刷新----------");
                        new WholeGetDataTask(PULL_UP_REFRESH).execute("0");
                        // 上拉刷新
                        Log.i("refresh", "refresh2_2");
                    } else {
                        Log.i("refresh", "refresh2_3");
                    }
                } else {
                    wholePulltoListView.onRefreshComplete();
                    Log.i("refresh", "refresh_NoNetwork");
                    Toast.makeText(getActivity(), getResources().getString(R.string.error_cannot_access_net), 1).show();
                }
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }


    private void initProgressDialog() {
        mProgressDialog = new Dialog(getActivity(), R.style.sports_dialog);
        LayoutInflater mInflater = getActivity().getLayoutInflater();
        View v = mInflater.inflate(R.layout.sports_progressdialog, null);
        TextView message = (TextView) v.findViewById(R.id.message);
        message.setText(R.string.loading);
        v.setMinimumWidth((int) (SportsApp.ScreenWidth * 0.8));
        mProgressDialog.setContentView(v);
        mProgressDialog.setCancelable(true);
        mProgressDialog.setCanceledOnTouchOutside(false);
    }

    @Override
    public void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
    }

    @Override
    public void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
    }

    /**
     * 为listview准备数据
     */
    private class WholeGetDataTask extends AsyncTask<String, Void, List<UserRank>> {

        private int pullToRefresh = 0;

        public WholeGetDataTask(int pullToRefresh) {
            this.pullToRefresh = pullToRefresh;
        }

        @Override
        protected List<UserRank> doInBackground(String... sessionIds) {

            Log.i("refresh", "refresh4");
            return loadData(sessionIds[0]);
        }

        private List<UserRank> loadData(String sessionId) {
            if (getActivity() == null
                    || !SportsUtilities.checkConnection(getActivity())
                    || SportsApp.getInstance().getSessionId() == null) {
                return null;
            } else {

                if (pullToRefresh == PULL_UP_REFRESH) {

                    times++;
                } else if (pullToRefresh == PULL_DOWN_REFRESH) {
                    times = 0;
                }
                List<UserRank> userRanks = null;
                try {
                    // 全部
                    userRanks = ApiJsonParser.sportsRankNew(1, times, SportsApp.getInstance().getSessionId(), getActivity());

                } catch (ApiNetException e) {
                    e.printStackTrace();
                    Message.obtain(mExceptionHandler,
                            SportExceptionHandler.NET_ERROR).sendToTarget();
                } catch (ApiSessionOutException e) {
                    e.printStackTrace();
                    Message.obtain(mExceptionHandler, SportExceptionHandler.SESSION_OUT).sendToTarget();

                }
                Log.i("refresh", "refresh5");
                return userRanks;
            }
        }

        @Override
        protected void onPostExecute(List<UserRank> userRanks) {

            if (wholelListView == null || userRanks == null) {
                if (wholePulltoListView != null)
                    wholePulltoListView.setVisibility(View.VISIBLE);
                return;
            }
            if (!userRanks.toString().equals("[]")) {

                tv_refresh.setVisibility(View.GONE);

                if (pullToRefresh == PULL_DOWN_REFRESH) {
                    if (allUserRanks != null)
                        allUserRanks.clear();
                }
                for (UserRank userRank : userRanks) {

                    allUserRanks.add(userRank);
                }
                if (pullToRefresh == PULL_UP_REFRESH) {

                    if (wholeGridViewAdapter != null) {
                        wholeGridViewAdapter.notifyDataSetChanged();
                    } else {
                        wholeGridViewAdapter = new WholeGridViewAdapter(getActivity(), mImageWorker, allUserRanks);
                        wholelListView.setAdapter(wholeGridViewAdapter);
                        wholelListView.setSelector(new ColorDrawable(Color.TRANSPARENT));
                    }
                } else if (pullToRefresh == PULL_DOWN_REFRESH) {
                    //下拉
                    if (wholeGridViewAdapter != null) {
                        wholeGridViewAdapter.notifyDataSetChanged();
                    } else {
                        wholeGridViewAdapter = new WholeGridViewAdapter(getActivity(), mImageWorker, allUserRanks);
                        wholelListView.setAdapter(wholeGridViewAdapter);
                        wholelListView.setSelector(new ColorDrawable(Color.TRANSPARENT));
                    }
                } else {
                    wholeGridViewAdapter = new WholeGridViewAdapter(getActivity(), mImageWorker, allUserRanks);
                    wholelListView.setAdapter(wholeGridViewAdapter);
                    wholelListView.setSelector(new ColorDrawable(Color.TRANSPARENT));
                }
            } else if (userRanks.size() == 0) {

                if (wholeGridViewAdapter != null) {
                    wholeGridViewAdapter.notifyDataSetChanged();
                }
                if (pullToRefresh == PULL_REFRESH) {
                    tv_refresh.setVisibility(View.VISIBLE);
                }
            } else {

            }

            if (getActivity() != null && mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }
            if (wholePulltoListView != null)

                wholePulltoListView.onRefreshComplete();
            Log.i("refresh", "refresh6");
            super.onPostExecute(userRanks);
            // initLoading(mPager_sub.getCurrentItem(), sex);
        }
    }


    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        if (allUserRanks != null) {
            allUserRanks.clear();
            allUserRanks = null;
        }
    }

}
