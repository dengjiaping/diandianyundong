package com.fox.exercise.newversion.trainingplan;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.ingenic.indroidsync.SportsApp;

import com.fox.exercise.AbstractBaseActivity;
import com.fox.exercise.R;
import com.fox.exercise.api.ApiJsonParser;
import com.fox.exercise.api.ApiMessage;
import com.fox.exercise.newversion.entity.TrainCount;
import com.fox.exercise.util.SportTaskUtil;
import com.fox.exercise.view.PullToRefreshListView;
import com.fox.exercise.view.PullToRefreshBase.OnRefreshListener;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class TrainTaskListActivity extends AbstractBaseActivity {

    private static final int MODE_PULL_UP_TO_REFRESH = 0x2;
    private String TAG = "develop_debug";
    private TextView no_history;
    private ListView listView;
    private PullToRefreshListView mPullListView = null;
    private TrainTaskListAdapter mAdapter = null;
    private ArrayList<TrainTaskListDetail> mList = null;
    private TextView tv_total_time, tv_train_cal, tv_train_times, tv_train_dates, tv_total_fenzhong;
    private int serverPage;
    private int localPage;
    private TrainPlanDataBase db;
    private SportsApp mSportsApp;

    @Override
    public void initIntentParam(Intent intent) {
        // TODO Auto-generated method stub
        title = getResources().getString(R.string.train_task_list);
    }

    @Override
    public void initView() {
        // TODO Auto-generated method stub
        showContentView(R.layout.activity_train_task_list);
        mSportsApp = (SportsApp)getApplication();

        serverPage = 0;
        localPage = 0;

        mPullListView = (PullToRefreshListView) findViewById(R.id.train_task_list_pull_refresh_list);
        mPullListView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(int pullDirection) {
                switch (pullDirection) {
                    case MODE_PULL_UP_TO_REFRESH:
                        Log.e(TAG, "refresh");
                        if(mSportsApp.isOpenNetwork()){
                            new GetTrainTaskList().execute();
                        }else{
                            mPullListView.onRefreshComplete();
                        }
                        break;
                }
            }
        });

        listView = mPullListView.getRefreshableView();
        listView.setDivider(getResources().getDrawable(R.drawable.sports_bg_line));
        listView.setDividerHeight(1);

        mList = new ArrayList<TrainTaskListDetail>();

        tv_total_time = (TextView) findViewById(R.id.tv_total_xiaoshi);
        tv_total_fenzhong = (TextView) findViewById(R.id.tv_total_fenzhong);
        tv_train_cal = (TextView) findViewById(R.id.tv_train_cal);
        tv_train_times = (TextView) findViewById(R.id.tv_train_times);
        tv_train_dates = (TextView) findViewById(R.id.tv_train_dates);
        no_history = (TextView) findViewById(R.id.no_history);

        if (!mSportsApp.LoginOption) {
            tv_total_time.setText("0");
            tv_total_fenzhong.setText("0");
            tv_train_cal.setText("热量(Cal)\n" + "0");
            tv_train_times.setText("训练次数\n" + "0");
            tv_train_dates.setText("累计天数\n" + "0");
            no_history.setVisibility(View.VISIBLE);
        } else {
            if (SportsApp.getInstance().isOpenNetwork()) {
                new GetTotalTrainTask().execute();
            } else {
                Toast.makeText(TrainTaskListActivity.this, getString(R.string.network_not_avaliable),
                        Toast.LENGTH_SHORT).show();
                //获取缓存数据
                SharedPreferences preferences1 = getSharedPreferences("TotalTrainTask", 0);
                String content1 = preferences1.getString("TotalTrainTask_info", "");
                if(content1!=null&&!"".equals(content1)){
                    JSONObject obj;
                    try {
                        obj = new JSONObject(content1).getJSONObject("data");
                        int all = obj.getInt("traintime");
                        int xiaoshi = all / 3600;
                        int fenzhong = (all - xiaoshi * 3600) / 60;
                        tv_total_time.setText(xiaoshi + "");
                        tv_total_fenzhong.setText(fenzhong + "");
                        tv_train_cal.setText("热量(Cal)\n" + SportTaskUtil.getDoubleOneNum(obj.getDouble("train_calorie")));
                        tv_train_times.setText("训练次数\n" + obj.getString("countnum"));
                        tv_train_dates.setText("累计天数\n" + obj.getString("countday"));

                        TrainCount mCount = new TrainCount();
                        mCount.setTraintime(all);
                        mCount.setTrain_calorie(obj.getDouble("train_calorie"));
                        mCount.setCountday(obj.getInt("countday"));
                        mCount.setCountnum(obj.getInt("countnum"));
                        mSportsApp.setmCount(mCount);
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }else{
                    tv_total_time.setText("0");
                    tv_total_fenzhong.setText("0");
                    tv_train_cal.setText("热量(Cal)\n" + "0");
                    tv_train_times.setText("训练次数\n" + "0");
                    tv_train_dates.setText("累计天数\n" + "0");
                }


                //获取缓存数据
                SharedPreferences preferences2 = getSharedPreferences("TrainTaskLists", 0);
                String content2 = preferences2.getString("TrainTaskLists_info", "");
                if(content2!=null&&!"".equals(content2)){
                    try {
                        JSONObject obj = new JSONObject(content2);
                        JSONArray jsonArray = obj.getJSONArray("data");
                        TrainTaskListDetail detail;
                        for (int i = 0; i < jsonArray.length(); i++) {
                            obj = jsonArray.getJSONObject(i);
                            detail = new TrainTaskListDetail(obj.getInt("uid"), obj.getInt("train_id"),
                                    obj.getInt("traintime"), obj.getDouble("train_calorie"),
                                    obj.getString("train_action"), obj.getString("train_position"),
                                    obj.getInt("train_completion"), obj.getString("train_starttime"),
                                    obj.getString("train_endtime"), obj.getInt("is_total"), 1,
                                    obj.getString("unique_id"));
                            mList.add(detail);

                        }

                        if (jsonArray!=null&&jsonArray.length() == 0) {
                            no_history.setVisibility(View.VISIBLE);
                        }
                        if (mList!=null&&mList.size()>0){
                            mAdapter = new TrainTaskListAdapter(mList, TrainTaskListActivity.this);
                            listView.setAdapter(mAdapter);
                        }
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }else{
                    no_history.setVisibility(View.VISIBLE);
                }


            }
        }


    }

    private class GetTotalTrainTask extends AsyncTask<Void, Void, ApiMessage> {

        @Override
        protected ApiMessage doInBackground(Void... Params) {
            return ApiJsonParser.getTotalTrainTask(SportsApp.getInstance().getSessionId());
        }

        @Override
        protected void onPostExecute(ApiMessage result) {
            super.onPostExecute(result);

            if ((result == null) || (!result.isFlag())) {
                return;
            }

            JSONObject obj;
            try {
                if(result.getMsg()!=null&&!"".equals(result.getMsg())){
                    obj = new JSONObject(result.getMsg()).getJSONObject("data");

                    // 保存训练成绩缓存数据
                    SharedPreferences preferences = getSharedPreferences("TotalTrainTask", 0);
                    SharedPreferences.Editor edit = preferences.edit();
                    edit.putString("TotalTrainTask_info", result.getMsg());
                    edit.commit();

                    int all = obj.getInt("traintime");
                    int xiaoshi = all / 3600;
                    int fenzhong = (all - xiaoshi * 3600) / 60;
                    tv_total_time.setText(xiaoshi + "");
                    tv_total_fenzhong.setText(fenzhong + "");
                    tv_train_cal.setText("热量(Cal)\n" + SportTaskUtil.getDoubleOneNum(obj.getDouble("train_calorie")));
                    tv_train_times.setText("训练次数\n" + obj.getString("countnum"));
                    tv_train_dates.setText("累计天数\n" + obj.getString("countday"));

                    TrainCount mCount = new TrainCount();
                    mCount.setTraintime(all);
                    mCount.setTrain_calorie(obj.getDouble("train_calorie"));
                    mCount.setCountday(obj.getInt("countday"));
                    mCount.setCountnum(obj.getInt("countnum"));
                    mSportsApp.setmCount(mCount);
                }
                if(mSportsApp.isOpenNetwork()){
                    new GetTrainTaskList().execute();
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    private class GetTrainTaskList extends AsyncTask<Void, Void, ApiMessage> {

        @Override
        protected ApiMessage doInBackground(Void... Params) {
            return ApiJsonParser.getTrainTaskList(SportsApp.getInstance().getSessionId(), serverPage);
        }

        @Override
        protected void onPostExecute(ApiMessage result) {
            super.onPostExecute(result);
            if ((result != null) && (result.isFlag())) {
                try {
                    if(result.getMsg()!=null&&!"".equals(result.getMsg())){
                        JSONObject obj = new JSONObject(result.getMsg());
                        JSONArray jsonArray = obj.getJSONArray("data");

                        // 保存训练成绩缓存数据
                        SharedPreferences preferences = getSharedPreferences("TrainTaskLists", 0);
                        SharedPreferences.Editor edit = preferences.edit();
                        edit.putString("TrainTaskLists_info", result.getMsg());
                        edit.commit();

                        TrainTaskListDetail detail;
                        for (int i = 0; i < jsonArray.length(); i++) {
                            obj = jsonArray.getJSONObject(i);
                            detail = new TrainTaskListDetail(obj.getInt("uid"), obj.getInt("train_id"),
                                    obj.getInt("traintime"), obj.getDouble("train_calorie"),
                                    obj.getString("train_action"), obj.getString("train_position"),
                                    obj.getInt("train_completion"), obj.getString("train_starttime"),
                                    obj.getString("train_endtime"), obj.getInt("is_total"), 1,
                                    obj.getString("unique_id"));

                            int saveResult = saveTrainTask(detail);
                        }

                        if (jsonArray.length() == 10) {
                            serverPage++;
                        }
                    }

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            ArrayList<TrainTaskListDetail> lcoalList = new ArrayList<TrainTaskListDetail>();

            db = TrainPlanDataBase.getInstance(TrainTaskListActivity.this);
            if (db != null) {
                Log.e(TAG, "localPage : " + localPage);
                lcoalList = db.getTasksList(SportsApp.getInstance().getSportUser().getUid(), localPage);
            }

            boolean recordExists = false;

            if ((lcoalList != null) && (lcoalList.size() > 0)) {
                for (int i = 0; i < lcoalList.size(); i++) {
                    recordExists = false;

                    for (int j = 0; j < mList.size(); j++) {
                        if (mList.get(j).getUnique_id().equalsIgnoreCase(lcoalList.get(i).getUnique_id())) {
                            recordExists = true;
                            break;
                        }
                    }

                    if (!recordExists) {
                        mList.add(lcoalList.get(i));
                    }
                }
            }

            if (lcoalList.size() == 10) {
                localPage++;
            }

            if (mAdapter == null) {
                mAdapter = new TrainTaskListAdapter(mList, TrainTaskListActivity.this);
                listView.setAdapter(mAdapter);
            } else {
                listView.requestLayout();
                mAdapter.notifyDataSetChanged();
            }
            mPullListView.onRefreshComplete();
        }
    }

    private int saveTrainTask(TrainTaskListDetail detail) {

        int saveResult = 0;

        if (detail == null) {
            return -1;
        }

        db = TrainPlanDataBase.getInstance(TrainTaskListActivity.this);

        Cursor cursor = db.query(detail.getUid(), detail.getTrain_starttime(), detail.getUnique_id());
        if ((cursor != null) && (!cursor.moveToFirst())) {
            ContentValues values = new ContentValues();
            values.put(TrainPlanDataBase.UID_I, detail.getUid());
            values.put(TrainPlanDataBase.TRAIN_ID_I, detail.getTrain_id());
            values.put(TrainPlanDataBase.TRAIN_TIME_I, detail.getTraintime());
            values.put(TrainPlanDataBase.TRAIN_CALORIE_D, detail.getTrain_calorie());
            values.put(TrainPlanDataBase.TRAIN_ACTION_S, detail.getTrain_action());
            values.put(TrainPlanDataBase.TRAIN_POSITION_S, detail.getTrain_position());
            values.put(TrainPlanDataBase.TRAIN_COMPLETION_I, detail.getTrain_completion());
            values.put(TrainPlanDataBase.TRAIN_STARTTIME_S, detail.getTrain_starttime());
            values.put(TrainPlanDataBase.TRAIN_ENDTIME_S, detail.getTrain_endtime());
            values.put(TrainPlanDataBase.IS_TOTAL_I, detail.getIs_total());
            values.put(TrainPlanDataBase.IS_UPLOAD_I, detail.getIs_upload());
            values.put(TrainPlanDataBase.TRAIN_MARKCODE, detail.getUnique_id());
            saveResult = db.insert(values, false);
        }

        if (cursor != null) {
            cursor.close();
            cursor = null;
        }

        return saveResult;
    }

    @Override
    public void setViewStatus() {
        // TODO Auto-generated method stub
    }

    @Override
    public void onPageResume() {
        // TODO Auto-generated method stub
    }

    @Override
    public void onPagePause() {
        // TODO Auto-generated method stub
    }

    @Override
    public void onPageDestroy() {
        // TODO Auto-generated method stub
        if (db != null) {
            db.close();
        }
    }
}