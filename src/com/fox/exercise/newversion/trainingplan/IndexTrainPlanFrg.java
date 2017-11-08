package com.fox.exercise.newversion.trainingplan;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fox.exercise.AbstractBaseFragment;
import com.fox.exercise.FansAndNear;
import com.fox.exercise.FoxSportsState;
import com.fox.exercise.R;
import com.fox.exercise.YunHuWebViewActivity;
import com.fox.exercise.api.ApiBack;
import com.fox.exercise.api.ApiJsonParser;
import com.fox.exercise.api.ApiMessage;
import com.fox.exercise.api.ApiNetException;
import com.fox.exercise.api.ApiSessionOutException;
import com.fox.exercise.api.entity.NewCommentInfo;
import com.fox.exercise.api.entity.UserDetail;
import com.fox.exercise.newversion.adapter.TrainPlanListAdapter;
import com.fox.exercise.newversion.entity.TrainCount;
import com.fox.exercise.newversion.entity.TrainPlanList;
import com.fox.exercise.view.PullToRefreshBase.OnRefreshListener;
import com.fox.exercise.view.PullToRefreshListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import cn.ingenic.indroidsync.SportsApp;

/**
 * @author loujungang 训练计划首页
 */
public class IndexTrainPlanFrg extends AbstractBaseFragment implements OnClickListener {
    private View view;
    private PullToRefreshListView mPullSearchListView = null;
    private ListView mListView = null;
    private SportsApp mSportsApp;
    private int times = 0, count;
    private ArrayList<TrainPlanList> mlist = new ArrayList<TrainPlanList>();
    private final int FRESH_LIST = 111;// 更新成功
    private final int FRESH_FAILED = 112;// 更新失败
    private final int FRESH_NULL = 114;
    private final int HEAD_ID = 115;
    private TrainPlanListAdapter mAdapter;
    private ImageView bind_device_text;// 右边按钮
    private TextView tipNumtx;
    private View head_view;
    private TextView yundong_cishu, yundong_laluli, yundong_di_day, tv_xiaoshi_value, tv_fenzhong_value;
    private NewCommentInfo commentInfo = new NewCommentInfo();

    @Override
    public void beforeInitView() {
        // TODO Auto-generated method stub
        title = getResources().getString(R.string.training);
    }

    @Override
    public void setViewStatus() {
        // TODO Auto-generated method stub
        mSportsApp = (SportsApp) getActivity().getApplication();
        view = LayoutInflater.from(getActivity()).inflate(
                R.layout.trainplan_index_frg, null);
        setContentViews(view);

        RelativeLayout rightRelativeLayout = new RelativeLayout(getActivity());
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                android.widget.RelativeLayout.LayoutParams.WRAP_CONTENT,
                android.widget.RelativeLayout.LayoutParams.WRAP_CONTENT);
        rightRelativeLayout.setLayoutParams(params);
        bind_device_text = new ImageView(getActivity());
        RelativeLayout.LayoutParams lp2 = new RelativeLayout.LayoutParams(
                android.widget.RelativeLayout.LayoutParams.WRAP_CONTENT,
                android.widget.RelativeLayout.LayoutParams.WRAP_CONTENT);
        lp2.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        lp2.setMargins(0, 0, SportsApp.getInstance().dip2px(4), 0);
        bind_device_text.setLayoutParams(lp2);
        bind_device_text.setImageResource(R.drawable.title_right_new_message3);
        rightRelativeLayout.addView(bind_device_text);
        tipNumtx = new TextView(getActivity());
        RelativeLayout.LayoutParams layoutParams1 = new RelativeLayout.LayoutParams(
                SportsApp.getInstance().dip2px(13), SportsApp.getInstance()
                .dip2px(13));
        layoutParams1.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        tipNumtx.setLayoutParams(layoutParams1);
        tipNumtx.setTextColor(getActivity().getResources().getColor(
                R.color.white));
        tipNumtx.setBackgroundResource(R.drawable.tip_bg);
        tipNumtx.setTextSize(10);
        tipNumtx.setId(+1);
        tipNumtx.setGravity(Gravity.CENTER);
        rightRelativeLayout.addView(tipNumtx);
        right_btn.setPadding(0, 0, SportsApp.dip2px(12), 0);
        showRightBtn(rightRelativeLayout);
        bind_device_text.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                if (!mSportsApp.LoginOption) {
                    mSportsApp.TyrLoginAction(getActivity(),
                            getString(R.string.sports_love_title),
                            getString(R.string.try_to_login));
                } else if (!mSportsApp.LoginNet) {
                    mSportsApp.NoNetLogin(getActivity());

                } else {
                    startActivityForResult(new Intent(getActivity(),
                            FoxSportsState.class), 10);
                }
            }
        });
        head_view = LayoutInflater.from(getActivity()).inflate(
                R.layout.train_index_headview, null);
        head_view.setId(+HEAD_ID);
        head_view.setOnClickListener(this);

        tv_xiaoshi_value = (TextView) head_view.findViewById(R.id.tv_xiaoshi_value);
        tv_fenzhong_value = (TextView) head_view.findViewById(R.id.tv_fenzhong_value);
        if(mSportsApp!=null){
            if (mSportsApp.getmCount().getTraintime() == 0) {
                if (SportsApp.getInstance().isOpenNetwork()) {
                    new GetTotalTrainTask().execute();
                } else {
                    SharedPreferences preferences = getActivity().getSharedPreferences("TrainCounts", 0);
                    String content = preferences.getString("TrainCounts_info", "");
                    if (content != null && !"".equals(content)) {
                        JSONObject obj;
                        try {
                            obj = new JSONObject(content).getJSONObject("data");
                            int all = obj.getInt("traintime");
                            int xiaoshi = all / 3600;
                            int fenzhong = (all - xiaoshi * 3600) / 60;
                            tv_xiaoshi_value.setText(Integer.toString(xiaoshi));
                            tv_fenzhong_value.setText(Integer.toString(fenzhong));

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
                    }

                }
            } else {
                int xiaoshi = mSportsApp.getmCount().getTraintime() / 3600;
                tv_xiaoshi_value.setText(Integer.toString(xiaoshi));
                int fenzhong = (mSportsApp.getmCount().getTraintime() - 3600 * xiaoshi) / 60;
                tv_fenzhong_value.setText(Integer.toString(fenzhong));
            }
        }

        yundong_cishu = (TextView) head_view.findViewById(R.id.yundong_cishu);
        yundong_laluli = (TextView) head_view.findViewById(R.id.yundong_laluli);
        yundong_di_day = (TextView) head_view.findViewById(R.id.yundong_di_day);
        yundong_cishu.setText(mSportsApp.getmCount().getCountnum() + "");
        yundong_laluli.setText((int) mSportsApp.getmCount().getTrain_calorie() + "");
        yundong_di_day.setText(mSportsApp.getmCount().getCountday() + "");

        mPullSearchListView = (PullToRefreshListView) view
                .findViewById(R.id.tainplan_index_refresh_list);
        mListView = mPullSearchListView.getRefreshableView();
        mListView.addHeaderView(head_view);
        if (!mSportsApp.LoginOption) {
            mSportsApp.TyrLoginAction(getActivity(),
                    getString(R.string.sports_love_title),
                    getString(R.string.try_to_login));

        } else {
            if (mSportsApp.isOpenNetwork()) {
                if (mSportsApp.getSessionId() != null
                        && !"".equals(mSportsApp.getSessionId())) {
                    new TrainPlanMoreThread().start();
                }
            } else {
                //没有网的时候获取缓存数据
                SharedPreferences preferences = getActivity().getSharedPreferences("CacheIndexTrainlist", 0);
                String Trainlist_info = preferences.getString("CacheTrainlist_info", "");
                try {
                    TrainPlanList trainPlanList = null;
                    JSONArray jsonArray = new JSONObject(Trainlist_info)
                            .getJSONArray("data");
                    if (jsonArray != null && jsonArray.length() > 0) {

                        for (int i = 0, j = jsonArray.length(); i < j; i++) {
                            trainPlanList = new TrainPlanList();
                            JSONObject obj = jsonArray.getJSONObject(i);
                            if (obj.has("id")) {
                                trainPlanList.setId(obj.getInt("id"));
                            }
                            if (obj.has("train_name")) {
                                trainPlanList.setTrain_name(obj
                                        .getString("train_name"));
                            }
                            if (obj.has("thumb")) {
                                trainPlanList.setThumb(obj.getString("thumb"));
                            }
                            if (obj.has("grade")) {
                                trainPlanList.setGrade(obj.getInt("grade"));
                            }
                            if (obj.has("position")) {
                                trainPlanList
                                        .setPosition(obj.getString("position"));
                            }
                            if (obj.has("train_time")) {
                                trainPlanList.setTrain_time(obj
                                        .getInt("train_time"));
                            }
                            if (obj.has("train_calorie")) {
                                trainPlanList.setTrain_calorie(obj
                                        .getDouble("train_calorie"));
                            }
                            if (obj.has("traincount")) {
                                trainPlanList.setTraincount(obj
                                        .getInt("traincount"));
                            }
                            mlist.add(trainPlanList);
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


                if (mlist != null) {
                    mAdapter = new TrainPlanListAdapter(mlist, getActivity());
                    mListView.setAdapter(mAdapter);
                }
            }


            mPullSearchListView.setOnRefreshListener(new OnRefreshListener() {

                @Override
                public void onRefresh(int pullDirection) {
                    if (mSportsApp.isOpenNetwork()) {
                        switch (pullDirection) {
                            case FansAndNear.MODE_DEFAULT_LOAD:
                                mPullSearchListView.onRefreshComplete();
                                break;
                            case FansAndNear.MODE_PULL_DOWN_TO_REFRESH:
                                times = 0;
                                if (SportsApp.getInstance().isOpenNetwork()) {
                                    new GetTotalTrainTask().execute();
                                    new TrainPlanMoreThread().start();
                                }else{
                                    mPullSearchListView.onRefreshComplete();
                                }
                                break;
                        }
                    } else {
                        Toast.makeText(
                                getActivity(),
                                getResources().getString(
                                        R.string.acess_server_error),
                                Toast.LENGTH_SHORT).show();
                        mPullSearchListView.onRefreshComplete();
                    }

                }
            });

        }

        mListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                // TODO Auto-generated method stub
                if (mlist != null && mlist.size() > 0) {
                    //统计点击训练人数
                    healthCount();
                    Intent intent = new Intent(getActivity(), TrainPlanMainActivity.class);
                    intent.putExtra("TrainPlanList", mlist.get(arg2 - 1));
                    startActivity(intent);
                }


            }
        });

        if(mSportsApp!=null){
            if(mSportsApp.getSports_Show()<=0){
                getSportsShow sportsShow = new getSportsShow();
                sportsShow.execute("");
            }else{
                count=mSportsApp.getSports_Show();
            }
        }

    }

    private  class GetTotalTrainTask extends AsyncTask<Void, Void, ApiMessage> {

        @Override
        protected ApiMessage doInBackground(Void... Params) {
            ApiMessage apiMessage=null;
            try{
                apiMessage=ApiJsonParser.getTotalTrainTask(SportsApp.getInstance().getSessionId());
            }catch (Exception e){

            }
            return apiMessage;
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
                    //保存训练总时长
                    if(getActivity()!=null){
                        SharedPreferences preferences = getActivity().getSharedPreferences("TrainCounts", 0);
                        Editor edit = preferences.edit();
                        edit.putString("TrainCounts_info", result.getMsg());
                        edit.commit();
                    }


                    obj = new JSONObject(result.getMsg()).getJSONObject("data");
                    int all = obj.getInt("traintime");
                    int xiaoshi = all / 3600;
                    int fenzhong = (all - xiaoshi * 3600) / 60;
                    tv_xiaoshi_value.setText(Integer.toString(xiaoshi));
                    tv_fenzhong_value.setText(Integer.toString(fenzhong));

                    TrainCount mCount = new TrainCount();
                    mCount.setTraintime(all);
                    mCount.setTrain_calorie(obj.getDouble("train_calorie"));
                    mCount.setCountday(obj.getInt("countday"));
                    mCount.setCountnum(obj.getInt("countnum"));
                    mSportsApp.setmCount(mCount);
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onPageResume() {
        // TODO Auto-generated method stub
        if(mSportsApp==null){
            if(getActivity()!=null){
                mSportsApp=(SportsApp) getActivity().getApplication();
            }
        }
        count=mSportsApp.getSports_Show();
        mSportsApp.setSports_Show(0);
        updateTip();
        yundong_cishu.setText(mSportsApp.getmCount().getCountnum() + "");
        yundong_laluli.setText((int) mSportsApp.getmCount().getTrain_calorie() + "");
        yundong_di_day.setText(mSportsApp.getmCount().getCountday() + "");

        if (mSportsApp.getmCount().getTraintime() != 0) {
            int xiaoshi = mSportsApp.getmCount().getTraintime() / 3600;
            tv_xiaoshi_value.setText(Integer.toString(xiaoshi));
            int fenzhong = (mSportsApp.getmCount().getTraintime() - 3600 * xiaoshi) / 60;
            tv_fenzhong_value.setText(Integer.toString(fenzhong));
        }
    }

    @Override
    public void onPagePause() {
        // TODO Auto-generated method stub

    }

    @Override
    public void onPageDestroy() {
        // TODO Auto-generated method stub
        if (mlist != null) {
            mlist.clear();
            mlist = null;
        }
        commentInfo=null;
    }

    // 获取训练计划的列表
    class TrainPlanMoreThread extends Thread {
        @Override
        public void run() {
            // TODO Auto-generated method stub
            super.run();
            Message msg = null;
            ArrayList<TrainPlanList> lists = null;

            try {
                lists = (ArrayList<TrainPlanList>) ApiJsonParser
                        .getTrainlist(mSportsApp.getSessionId(), getActivity());
            } catch (ApiNetException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (ApiSessionOutException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            if (times == 0) {
                mlist.clear();
            }

            if (lists != null) {
                if (lists.size() == 0) {
                    msg = Message.obtain(mHandler, FRESH_NULL);
                    msg.sendToTarget();
                } else {
                    for (TrainPlanList e : lists) {
                        mlist.add(e);
                    }
                    msg = Message.obtain(mHandler, FRESH_LIST);
                    msg.sendToTarget();
                }
            } else {
                if (lists == null) {
                    msg = Message.obtain(mHandler, FRESH_FAILED);
                    msg.sendToTarget();
                }
            }

        }
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (getActivity() == null)
                return;
            switch (msg.what) {
                case FRESH_LIST:
                    mAdapter = new TrainPlanListAdapter(mlist, getActivity());
                    mListView.setAdapter(mAdapter);
                    mPullSearchListView.onRefreshComplete();
                    break;
                case FRESH_FAILED:
                    mPullSearchListView.onRefreshComplete();
                    break;
                case FRESH_NULL:
                    mPullSearchListView.onRefreshComplete();
                    break;

                default:
                    break;
            }
        }

        ;
    };

    public void updateTip() {
        UserDetail detail = SportsApp.getInstance().getSportUser();
        // setMsgbox(detail.getActmsgs() + detail.getMsgCounts().getFans()
        // + detail.getMsgCounts().getSportVisitor()
        // + detail.getMsgCounts().getInvitesports()
        // + detail.getMsgCounts().getSysmsgsports()
        // + detail.getMsgCounts().getPrimsg());
        //detail.getMsgCounts().getInvitesports()
        setMsgbox(detail.getMsgCounts().getSportVisitor()
                + detail.getMsgCounts().getFans()
                + detail.getMsgCounts().getSysmsgsports()
                + detail.getMsgCounts().getPrimsg() + count);

    }

    private void setMsgbox(int msgNum) {
        if (tipNumtx != null) {
            if (msgNum > 0) {
                tipNumtx.setVisibility(View.VISIBLE);
                tipNumtx.setText(msgNum > 99 ? "99+" : msgNum + "");
            } else {
                tipNumtx.setVisibility(View.GONE);
            }
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10) {
            updateTip();
            count = 0;
        }
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case HEAD_ID:
                Intent intent = new Intent(getActivity(), TrainTaskListActivity.class);
                startActivity(intent);
                break;

            default:
                break;
        }
    }


    // 后台统计训练点击的次数
    private void healthCount() {
        Date startDate = new Date(System.currentTimeMillis());
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        final String startTime = formatter.format(startDate);
        new AsyncTask<Void, Void, ApiBack>() {
            @Override
            protected ApiBack doInBackground(Void... params) {
                // TODO Auto-generated method stub
                ApiBack back = null;
                try {
                    back = ApiJsonParser.healthdatacount(mSportsApp.getSessionId(),
                            7, startTime, 0);
                } catch (ApiNetException e) {
                    e.printStackTrace();
                } catch (ApiSessionOutException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                return back;
            }

            @Override
            protected void onPostExecute(ApiBack result) {
                // TODO Auto-generated method stub
                super.onPostExecute(result);
                if (result != null) {

                }

            }
        }.execute();
    }

    /**
     * 获取点赞和评论的消息条数
     */
    public class getSportsShow extends AsyncTask<String, Integer, Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {
            commentInfo = ApiJsonParser.getNewCommentCount(mSportsApp
                    .getSessionId(), mSportsApp.getSportUser().getUid());
            if (commentInfo == null)
                return false;
            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if (result) {
                if (commentInfo.commentCount > 0) {
                    count = commentInfo.commentCount;
                    mSportsApp.setSports_Show(0);
                }
            } else {

            }
        }
    }

    class clickListener implements OnClickListener {

        @Override
        public void onClick(View v) {
            // 金币商城
            if (mSportsApp.LoginNet) {
                if (mSportsApp.isOpenNetwork()) {
                    startActivity(new Intent(getActivity(), YunHuWebViewActivity.class));
                } else {
                    if(isAdded()){
                        Toast.makeText(
                                getActivity(),
                                getResources().getString(
                                        R.string.error_cannot_access_net),
                                Toast.LENGTH_SHORT);
                    }
                }
            } else {
                mSportsApp.NoNetLogin(getActivity());
            }
        }
    }

}
