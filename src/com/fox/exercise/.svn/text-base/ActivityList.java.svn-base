package com.fox.exercise;

import android.content.Intent;
import android.graphics.ColorMatrix;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.fox.exercise.api.ApiJsonParser;
import com.fox.exercise.api.ApiNetException;
import com.fox.exercise.api.ApiSessionOutException;
import com.fox.exercise.api.entity.ActionList;
import com.fox.exercise.view.PullToRefreshBase.OnRefreshListener;
import com.fox.exercise.view.PullToRefreshListView;
import com.umeng.analytics.MobclickAgent;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.ingenic.indroidsync.SportsApp;


public class ActivityList extends AbstractBaseFragment {

    private PullToRefreshListView activitylistView;
    private ListView mListView;
    private ActivityListAdapter activityAdapter;
    private List<ActionList> actionLists = new ArrayList<ActionList>();
    private int times;
    private TextView no_data;
    private SportsApp mSportsApp;
    private String activityTime;
    private SimpleDateFormat formatter;
    private Date curDate, activityDate;
    private ColorMatrix matrix;
//     private ColorMatrixColorFilter filter;

    public void beforeInitView() {
        title = getResources().getString(R.string.activity);
        actionLists.clear();
    }

    @Override
    public void setViewStatus() {
        //主体
        setContentView(R.layout.activity);
        mSportsApp = SportsApp.getInstance();
        Init();
        GetActionData();


        mListView.setOnItemClickListener(listViewClick);
        activitylistView.setOnRefreshListener(new OnRefreshListener() {

            @Override
            public void onRefresh(int pullDirection) {
                switch (pullDirection) {
                    case FansAndNear.MODE_DEFAULT_LOAD:
                        times++;
                        GetActionData();
                        break;
                    case FansAndNear.MODE_PULL_DOWN_TO_REFRESH:
                        actionLists.clear();
                        times = 0;
                        GetActionData();
                        break;
                }
            }
        });
    }

    @Override
    public void onPageResume() {
        MobclickAgent.onPageStart("ActivityList");
    }

    @Override
    public void onPagePause() {
        MobclickAgent.onPageEnd("ActivityList");
    }

    @Override
    public void onPageDestroy() {
        waitCloset();
    }

    private OnItemClickListener listViewClick = new OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

            if (mSportsApp.isOpenNetwork()) {
                ActionList action = actionLists.get(position);

                activityTime = action.getActionTime().substring(action.getActionTime().indexOf("-") + 1,
                        action.getActionTime().length()).replace(".", "-");
                Log.e("ActivityList", "活动时间：" + activityTime);

                try {
                    activityDate = formatter.parse(activityTime);
                    activityDate.setHours(23);
                    activityDate.setMinutes(59);
                    activityDate.setSeconds(59);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                Log.e("ActivityList", "系统时间：" + curDate.toString());
                Log.e("ActivityList", "活动时间：" + activityDate.toString());

                if (curDate.after(activityDate)) {
                    Toast.makeText(getActivity(),
                            getString(R.string.activity_end),
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                /*Intent activityIntent=new Intent(getActivity(),ActivityListDetail.class);
				activityIntent.putExtra("actionId", action.getActionId());*/
                Intent activityIntent = new Intent(getActivity(), ActivityInfoWebView.class);
                activityIntent.putExtra("title_name", action.getTitle());
                activityIntent.putExtra("action_url", action.getActionUrl());
                activityIntent.putExtra("activity_id", action.getActionId());
                startActivity(activityIntent);
            } else {
                Toast.makeText(getActivity(),
                        getString(R.string.network_not_avaliable),
                        Toast.LENGTH_SHORT).show();
            }

        }
    };

    /**
     * 初始化控件
     */
    private void Init() {
        activitylistView = (PullToRefreshListView) getActivity().findViewById(R.id.activity_list);
        mListView = activitylistView.getRefreshableView();
        no_data = (TextView) getActivity().findViewById(R.id.no_data);

        formatter = new SimpleDateFormat("yyyy-MM-dd");
        curDate = new Date(System.currentTimeMillis());

        matrix = new ColorMatrix();
        matrix.setSaturation(0);
//        filter = new ColorMatrixColorFilter(matrix);
    }

    /**
     * 获取活动信息
     */
    private void GetActionData() {

        if (SportsUtilities.checkConnection(getActivity())) {
            waitShow();
            new GetActionDataTask().execute();
        } else {
            Toast.makeText(getActivity(), getResources().getString(R.string.acess_server_error), Toast.LENGTH_SHORT).show();
        }

    }

    private class GetActionDataTask extends AsyncTask<Void, Void, List<ActionList>> {

        @Override
        protected List<ActionList> doInBackground(Void... sessionid) {

            List<ActionList> actionLists = null;
            try {
                actionLists = ApiJsonParser.getNewActionList(((SportsApp) getActivity().getApplication()).getSessionId(), "z" + getResources().getString(R.string.config_game_id), times);
            } catch (ApiNetException e) {
                e.printStackTrace();
            } catch (ApiSessionOutException e) {
                e.printStackTrace();
            }
            return actionLists;
        }

        @Override
        protected void onPostExecute(List<ActionList> result) {
            super.onPostExecute(result);
            waitCloset();
            if (result == null)
                return;
            if (result.size() > 0) {
                for (ActionList actionList : result) {
                    actionLists.add(actionList);
                }
                if (activityAdapter == null) {
                    activityAdapter = new ActivityListAdapter(getActivity(), actionLists);
                    mListView.setAdapter(activityAdapter);
                } else {
                    activityAdapter.notifyDataSetChanged();
                    activitylistView.onRefreshComplete();
                }
                no_data.setVisibility(View.GONE);
            } else if (result.size() == 0) {

                if (actionLists.size() == 0) {

                    actionLists.clear();
                    if (activityAdapter == null) {
                        activityAdapter = new ActivityListAdapter(getActivity(), actionLists);
                        mListView.setAdapter(activityAdapter);
                    } else {
                        activityAdapter.notifyDataSetChanged();
                        activitylistView.onRefreshComplete();
                    }
                    activitylistView.onRefreshComplete();
                    no_data.setVisibility(View.VISIBLE);
                } else if (actionLists.size() > 0) {
                    activitylistView.onRefreshComplete();

                }
            }
        }
    }

}
