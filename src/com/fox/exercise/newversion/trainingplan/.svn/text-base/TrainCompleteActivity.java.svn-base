package com.fox.exercise.newversion.trainingplan;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.PixelFormat;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.fox.exercise.R;
import com.fox.exercise.api.ApiBack;
import com.fox.exercise.api.ApiJsonParser;
import com.fox.exercise.api.ApiMessage;
import com.fox.exercise.api.ApiNetException;
import com.fox.exercise.api.ApiSessionOutException;
import com.fox.exercise.newversion.entity.TrainResultsInfo;

import java.text.SimpleDateFormat;
import java.util.Date;

import cn.ingenic.indroidsync.SportsApp;

/**
 * @author loujungang 训练完成界面
 */
public class TrainCompleteActivity extends Activity {
    private static final int SAVE_START = 1;
    private static final int SAVE_COMPLETE = 2;
    private Button btn_complete;
    private TrainPlanDataBase db;

    private int uid;
    private String sessionId;
    private int train_id;
    private int traintime;
    private int train_calorie;
    private String train_action;
    private String train_position;
    private int train_completion;
    private String train_starttime;
    private String train_endtime;
    private int is_total;
    private String unique_id;
    private int is_upload;
    private Intent mintent;
    private TrainResultsInfo resultsInfo;
    private TextView train_time, train_posion, train_cal;
    private int mNum;// 动作个数
    private SportsApp msApp;
    private long lastClickTime = 0;//防止连续点击

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);// 隐藏标题
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);// 设置全屏

        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        setContentView(R.layout.activity_train_complete);
        msApp = (SportsApp) getApplication();
        mintent = getIntent();
        if (mintent != null) {
            resultsInfo = (TrainResultsInfo) mintent
                    .getSerializableExtra("TrainResultsInfo");
            mNum = mintent.getIntExtra("train_actionList", 0);
            healthCount();
        }
        btn_complete = (Button) findViewById(R.id.btn_complete);
        btn_complete.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                long currentTime =System.currentTimeMillis();
                if(currentTime - lastClickTime > 2000){
                    lastClickTime = currentTime;
                    if (SportsApp.getInstance().isOpenNetwork()) {
                        uploadTrainTask(sessionId, train_id, traintime,
                                train_calorie, train_action, train_position,
                                train_completion, train_starttime, train_endtime,
                                is_total, unique_id);
                    } else {
                        Message saveMsg = Message.obtain();
                        saveMsg.what = SAVE_START;
                        saveMsg.obj = false;
                        sportHandler.sendMessage(saveMsg);
                    }
                }

            }
        });

        initData();
    }

    private void initData() {
        train_time = (TextView) findViewById(R.id.train_time);
        train_posion = (TextView) findViewById(R.id.train_posion);
        train_cal = (TextView) findViewById(R.id.train_cal);
        uid = SportsApp.getInstance().getSportUser().getUid();
        sessionId = resultsInfo.getSessionId();
        train_id = resultsInfo.getTrain_id();
        traintime = resultsInfo.getTraintime();
        train_calorie = (int) resultsInfo.getTrain_calorie();
        train_action = resultsInfo.getTrain_action();
        train_position = resultsInfo.getTrain_position();
        train_completion = resultsInfo.getTrain_completion();
        train_starttime = resultsInfo.getTrain_starttime();
        train_endtime = resultsInfo.getTrain_endtime();
        is_total = resultsInfo.getIs_total();
        unique_id = resultsInfo.getUnique_id();
        is_upload = 0;
        train_time.setText((traintime / 60) + "分钟");
        train_posion.setText(mNum + "个动作");
        train_cal.setText(train_calorie + "Cal");
    }

    private void uploadTrainTask(final String sessionId, final int train_id,
                                 final int traintime, final double train_calorie,
                                 final String train_action, final String train_position,
                                 final int train_completion, final String train_starttime,
                                 final String train_endtime, final int is_total,
                                 final String unique_id) {

        new Thread() {
            @Override
            public void run() {
                ApiMessage msg = ApiJsonParser.addTrainRecord(sessionId,
                        train_id, traintime, train_calorie, train_action,
                        train_position, train_completion, train_starttime,
                        train_endtime, is_total, unique_id);
                Message msgfail = Message.obtain();
                msgfail.what = SAVE_START;
                msgfail.obj = msg.isFlag();
                sportHandler.sendMessage(msgfail);
            }
        }.start();
    }

    private void saveTrainTask(final int uid, final int train_id,
                               final int traintime, final double train_calorie,
                               final String train_action, final String train_position,
                               final int train_completion, final String train_starttime,
                               final String train_endtime, final int is_total, final int is_upload) {
        new Thread() {
            @Override
            public void run() {
                int saveResult = 0;
                db = TrainPlanDataBase.getInstance(TrainCompleteActivity.this);
                ContentValues values = new ContentValues();
                values.put(TrainPlanDataBase.UID_I, uid);
                values.put(TrainPlanDataBase.TRAIN_ID_I, train_id);
                values.put(TrainPlanDataBase.TRAIN_TIME_I, traintime);
                values.put(TrainPlanDataBase.TRAIN_CALORIE_D, train_calorie);
                values.put(TrainPlanDataBase.TRAIN_ACTION_S, train_action);
                values.put(TrainPlanDataBase.TRAIN_POSITION_S, train_position);
                values.put(TrainPlanDataBase.TRAIN_COMPLETION_I,
                        train_completion);
                values.put(TrainPlanDataBase.TRAIN_STARTTIME_S, train_starttime);
                values.put(TrainPlanDataBase.TRAIN_ENDTIME_S, train_endtime);
                values.put(TrainPlanDataBase.IS_TOTAL_I, is_total);
                values.put(TrainPlanDataBase.IS_UPLOAD_I, is_upload);
                values.put(TrainPlanDataBase.TRAIN_MARKCODE, unique_id);
                // saveResult = db.insert(values,false);

                saveResult = db.update(values, uid, train_starttime, false,
                        unique_id);
                db.close();

                Message saveMsg = Message.obtain();
                saveMsg.what = SAVE_COMPLETE;
                saveMsg.obj = saveResult;
                sportHandler.sendMessage(saveMsg);
            }
        }.start();
    }

    private Handler sportHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SAVE_START:
                    boolean sta = (Boolean) msg.obj;
                    if (sta) {
                        is_upload = 1;
                        Toast.makeText(TrainCompleteActivity.this, "上传成功",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        is_upload = 0;
                        Toast.makeText(TrainCompleteActivity.this, "上传失败",
                                Toast.LENGTH_SHORT).show();
                    }
                    saveTrainTask(uid, train_id, traintime, train_calorie,
                            train_action, train_position, train_completion,
                            train_starttime, train_endtime, is_total, is_upload);
                    break;
                case SAVE_COMPLETE:
                    int com = (Integer) msg.obj;
                    if (com > 0) {
                        Toast.makeText(TrainCompleteActivity.this, "本地保存成功",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(TrainCompleteActivity.this, "本地保存失败",
                                Toast.LENGTH_SHORT).show();
                    }
                    finish();
                    break;
            }
        }

        ;
    };

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
                    back = ApiJsonParser.healthdatacount(
                            msApp.getSessionId(), 8, startTime, 0);
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

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        msApp = null;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (event.getAction() == KeyEvent.ACTION_DOWN
                && event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            return true;//消费掉后退键
        }

        return super.onKeyDown(keyCode, event);
    }
    /**
     *@method 固定字体大小方法
     *@author suhu
     *@time 2016/10/11 13:23
     *@param
     *
     */
    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        Configuration config=new Configuration();
        config.setToDefaults();
        res.updateConfiguration(config,res.getDisplayMetrics() );
        return res;
    }
}
