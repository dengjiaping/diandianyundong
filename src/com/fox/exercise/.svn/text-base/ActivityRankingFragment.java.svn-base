package com.fox.exercise;

import android.app.Dialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.fox.exercise.api.ApiJsonParser;
import com.fox.exercise.api.ApiNetException;
import com.fox.exercise.api.ApiSessionOutException;
import com.fox.exercise.api.entity.ActionRankList;
import com.fox.exercise.view.PullToRefreshBase.OnRefreshListener;
import com.fox.exercise.view.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

import cn.ingenic.indroidsync.SportsApp;

public class ActivityRankingFragment extends Fragment {

    private int activityId;
    //	 private static final String TAG="ActivityRankingFragment";
    private boolean isFrist = true;
    //	 private TextView text;
    private PullToRefreshListView refreshListView;
    private ListView listView;
    private ActivityRankFragmentAdaptre rankAdaptre;
    public Dialog mLoadProgressDialog = null;
    private int times = 0;
    private List<ActionRankList> allActionRankLists = new ArrayList<ActionRankList>();

    public ActivityRankingFragment() {
        super();
    }

    public void setActivityId(int activityId){
        this.activityId = activityId;
    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        allActionRankLists.clear();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activityrankingfragment, null);
        refreshListView = (PullToRefreshListView) view.findViewById(R.id.pull_refresh_list);
        listView = refreshListView.getRefreshableView();
//		text=(TextView)view.findViewById(R.id.text);
        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser && isFrist) {
            waitShow();
            GetActionRankListData();
            isFrist = false;
        }

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        refreshListView.setOnRefreshListener(new OnRefreshListener() {

            @Override
            public void onRefresh(int pullDirection) {

                switch (pullDirection) {
                    case FansAndNear.MODE_DEFAULT_LOAD:
                        times++;
                        GetActionRankListData();
                        break;
                    case FansAndNear.MODE_PULL_DOWN_TO_REFRESH:
                        allActionRankLists.clear();
                        times = 0;
                        GetActionRankListData();
                        break;
                }
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    /**
     * 获取活动排行接口
     */
    private void GetActionRankListData() {

        if (SportsUtilities.checkConnection(getActivity())) {
            waitShow();
            new GetActionRankListTask().execute();
        } else {
            Toast.makeText(getActivity(), getResources().getString(R.string.acess_server_error), Toast.LENGTH_SHORT).show();
        }

    }

    private class GetActionRankListTask extends AsyncTask<Void, Void, List<ActionRankList>> {

        List<ActionRankList> actionRankLists = null;

        @Override
        protected List<ActionRankList> doInBackground(Void... parmas) {

            try {
                actionRankLists = ApiJsonParser.getActionRankList(SportsApp.getInstance().getSessionId(), activityId, times);
            } catch (ApiNetException e) {
                e.printStackTrace();
            } catch (ApiSessionOutException e) {
                e.printStackTrace();
            }
            return actionRankLists;
        }

        @Override
        protected void onPostExecute(List<ActionRankList> result) {
            super.onPostExecute(result);
            if (result.size() > 0) {
                for (ActionRankList actionRankList : result) {
                    allActionRankLists.add(actionRankList);
                }
                if (rankAdaptre == null) {
                    rankAdaptre = new ActivityRankFragmentAdaptre(getActivity(), allActionRankLists);
                    if (listView != null && rankAdaptre != null)
                        listView.setAdapter(rankAdaptre);
                } else {
                    rankAdaptre.notifyDataSetChanged();
                    refreshListView.onRefreshComplete();
                }
            } else if (result.size() == 0) {
                refreshListView.onRefreshComplete();
            }
            waitCloset();
        }
    }

    /**
     * 开启对话框
     */
    public void waitShow() {
        if (mLoadProgressDialog == null) {
            mLoadProgressDialog = new Dialog(getActivity(), R.style.sports_dialog);
            LayoutInflater mInflater = getActivity().getLayoutInflater();
            View v1 = mInflater.inflate(R.layout.bestgirl_progressdialog, null);
            TextView message = (TextView) v1.findViewById(R.id.message);
            message.setText(R.string.bestgirl_wait);
            v1.setMinimumWidth((int) (SportsApp.ScreenWidth * 0.8));
            mLoadProgressDialog.setContentView(v1);
            mLoadProgressDialog.setCanceledOnTouchOutside(false);
        }
        if (mLoadProgressDialog != null)
            if (!mLoadProgressDialog.isShowing() && !getActivity().isFinishing())
                mLoadProgressDialog.show();
    }


    public void waitCloset() {
        if (mLoadProgressDialog != null)
            if (mLoadProgressDialog.isShowing())
                mLoadProgressDialog.dismiss();
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        if (allActionRankLists != null) {
            allActionRankLists.clear();
            allActionRankLists = null;
        }
    }


}
