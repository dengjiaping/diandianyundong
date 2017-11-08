package com.fox.exercise;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.os.AsyncTask;
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
import com.fox.exercise.api.entity.ActionList;
import com.fox.exercise.newversion.entity.ExternalActivi;
import com.fox.exercise.newversion.newact.NightRunWebViewActivity;
import com.fox.exercise.view.PullToRefreshBase.OnRefreshListener;
import com.fox.exercise.view.PullToRefreshListView;
import com.umeng.analytics.MobclickAgent;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.ingenic.indroidsync.SportsApp;

/**
 * @author loujungang 新的活动activity
 */
public class NewHuoDongActivity extends AbstractBaseOtherActivity {
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
    private ColorMatrixColorFilter filter;
    private Context mContext;
    public Dialog mLoadProgressDialog = null;

    @Override
    public void initIntentParam(Intent intent) {
        // TODO Auto-generated method stub
        title = getResources().getString(R.string.activity);
        actionLists.clear();
    }

    @Override
    public void initView() {
        // TODO Auto-generated method stub

    }

    @Override
    public void setViewStatus() {
        // TODO Auto-generated method stub
        // 主体
        showContentView(R.layout.activity);
        mSportsApp = SportsApp.getInstance();
        mContext = this;
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
        // TODO Auto-generated method stub
        MobclickAgent.onPageStart("NewHuoDongActivity");
    }

    @Override
    public void onPagePause() {
        // TODO Auto-generated method stub
        MobclickAgent.onPageEnd("NewHuoDongActivity");
    }

    @Override
    public void onPageDestroy() {
        // TODO Auto-generated method stub
        waitCloset();
    }

    private OnItemClickListener listViewClick = new OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                long arg3) {

            if (mSportsApp.isOpenNetwork()) {
                if (actionLists.get(position).getMatch_act() == 1){
                    //跳转第三方外链活动报名位置。
                    ExternalActivi externalActivi = new ExternalActivi();
                    externalActivi.setId(actionLists.get(position).getMatchId());
                    externalActivi.setPrice(actionLists.get(position).getMatchprice());
                    externalActivi.setTitle(actionLists.get(position).getMatchTitle());
                    externalActivi.setUrl(actionLists.get(position).getMatch_url());
                    Intent intent = new Intent(NewHuoDongActivity.this, NightRunWebViewActivity.class);
                    intent.putExtra("ExternalActivi",externalActivi);
                    startActivity(intent);
                }else {
                    ActionList action = actionLists.get(position);
                    activityTime = action
                            .getActionTime()
                            .substring(action.getActionTime().indexOf("-") + 1,
                                    action.getActionTime().length())
                            .replace(".", "-");

                    activityTime = activityTime.replace("-", "");
                    int b = Integer.valueOf(activityTime).intValue() + 1;
                    activityTime = b + "";
                    String year = activityTime.substring(0, 4);
                    String month = activityTime.substring(4, 6);
                    String day = activityTime.substring(6, 8);
                    activityTime = year + "-" + month + "-" + day;

                    try {
                        activityDate = formatter.parse(activityTime);
                        // activityDate.setHours(23);
                        // activityDate.setMinutes(59);
                        // activityDate.setSeconds(59);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

//				if (curDate.after(activityDate)) {
//					Toast.makeText(mContext, getString(R.string.activity_end),
//							Toast.LENGTH_SHORT).show();
//					return;
//				}
                /*
                 * Intent activityIntent=new
				 * Intent(getActivity(),ActivityListDetail.class);
				 * activityIntent.putExtra("actionId", action.getActionId());
				 */
                    Intent activityIntent = new Intent(mContext,
                            ActivityInfoWebView.class);
                    activityIntent.putExtra("title_name", action.getTitle());
                    activityIntent.putExtra("action_url", action.getActionUrl());
                    activityIntent.putExtra("activity_id", action.getActionId());
                    activityIntent.putExtra("thrurl", action.getThuurl());
                    activityIntent.putExtra("activitytime", activityTime);
                    Log.e("qwer", "活动时间列表跳转：" + activityDate.toString());
                    startActivity(activityIntent);
                }
            } else {
                Toast.makeText(mContext,
                        getString(R.string.network_not_avaliable),
                        Toast.LENGTH_SHORT).show();
            }
        }
    };

    /**
     * 初始化控件
     */
    private void Init() {
        activitylistView = (PullToRefreshListView) findViewById(R.id.activity_list);
        mListView = activitylistView.getRefreshableView();
        no_data = (TextView) findViewById(R.id.no_data);

        formatter = new SimpleDateFormat("yyyy-MM-dd");
        curDate = new Date(System.currentTimeMillis());
        Log.i("abcd", "时间比较格式" + curDate);

        matrix = new ColorMatrix();
        matrix.setSaturation(0);
        filter = new ColorMatrixColorFilter(matrix);
    }

    /**
     * 获取活动信息
     */
    private void GetActionData() {

        if (SportsUtilities.checkConnection(mContext)) {
            waitShow();
            new GetActionDataTask().execute();
        } else {
            Toast.makeText(mContext,
                    getResources().getString(R.string.acess_server_error), Toast.LENGTH_SHORT)
                    .show();
        }

    }

    private class GetActionDataTask extends
            AsyncTask<Void, Void, List<ActionList>> {

        @Override
        protected List<ActionList> doInBackground(Void... sessionid) {

            List<ActionList> actionLists = null;
            try {
                actionLists = ApiJsonParser.getNewActionList(
                        ((SportsApp) getApplication()).getSessionId(), "z" + getResources().getString(R.string.config_game_id), times);
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
                    activityAdapter = new ActivityListAdapter(mContext,
                            actionLists);
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
                        activityAdapter = new ActivityListAdapter(mContext,
                                actionLists);
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

    /**
     * 开启对话框
     */
    public void waitShow() {
        if (mLoadProgressDialog == null) {
            mLoadProgressDialog = new Dialog(mContext, R.style.sports_dialog);
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
                    && !NewHuoDongActivity.this.isFinishing())
                mLoadProgressDialog.show();
    }

    public void waitCloset() {
        if (mLoadProgressDialog != null)
            if (mLoadProgressDialog.isShowing())
                mLoadProgressDialog.dismiss();
    }
}
