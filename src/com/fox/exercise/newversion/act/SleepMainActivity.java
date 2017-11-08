package com.fox.exercise.newversion.act;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


import cn.ingenic.indroidsync.SportsApp;

import com.fox.exercise.AbstractBaseActivity;
import com.fox.exercise.R;
import com.fox.exercise.api.Api;
import com.fox.exercise.api.ApiBack;
import com.fox.exercise.api.ApiJsonParser;
import com.fox.exercise.api.ApiNetException;
import com.fox.exercise.api.ApiSessionOutException;
import com.fox.exercise.db.SportContentDB;
import com.fox.exercise.newversion.SleepService;
import com.fox.exercise.newversion.Tools;
import com.fox.exercise.newversion.entity.FirstPageContent;
import com.fox.exercise.newversion.entity.SleepEffect;
import com.fox.exercise.newversion.view.StopWatchView;
import com.fox.exercise.util.SportTaskUtil;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author loujungang 睡眠主activity
 */
public class SleepMainActivity extends AbstractBaseActivity implements
        OnClickListener {
    private SensorManager sensorManager;
    private float mX = 0;
    private float mY = 0;
    private float mZ = 0;
    private boolean isStart;// 判断是否开始睡眠
    private boolean isbright;// 判断是否屏幕亮
    private boolean isallbright;// 判断是否屏幕一直亮
    private SportsApp msApp;
    private Context mContext;
    private long mTroughTime = 0;
    private String startTime;
    private String endTime;
    private long startTimeSeconds, endTimeSeconds;// 开始计时时间 结束计时时间
    private long screenStartTimeSeconds, screenEndTimeSeconds;// 开始计时时间 结束计时时间
    private long sensorStartTimeSeconds;// sensor开始计时时间 结束计时时间
    private int flag = 0;
    private TextView starttext, sleep_tip_content;
    private long mTempCount;// 屏幕亮度时间

    private int num;
    private String unique_id;// 唯一标示
    private SportContentDB contentDB;
    private static final int UPLOAD_PARAM_SUCCESS = 10001;
    private static final int UPLOAD_PARAM_ERROR = 10002;
    private Dialog mLoadProgressDialog = null;

    private ImageButton iView;
    private final int IVEW_ID = 100;
    private final int LEFTIVEW_ID = 101;
    private final int LEFTIOUT_ID = 103;
    private final int RIGHT_ID = 102;

    private SleepEffect sleepEffect;
    private TextView mDialogMessage;
    private String[] pageContet;
    private String times;

    // private GraphicalView chart;
    // private XYMultipleSeriesDataset dataset;
    // private XYMultipleSeriesRenderer renderer;
    private Handler handler;
    private TimerTask task;
    private Timer timer = new Timer();
    // private LinearLayout health_sleep_bolayout;
    // private XYSeries xySeries;
    // private double yNum;
    // private int flags=0;
    private int senserFlag = 0;
    //
    // private long max=100;
    // private long min;
    // private TimeSeries series1;
    // private boolean isFirst = false;
    // private TextView timeTx;

    private StopWatchView stopWatchView;
    private TextView stopWatchView_time;
    private boolean isRun = false;

    private void startTask() {
        task = new TimerTask() {
            @Override
            public void run() {
                Message message = new Message();
                message.what = 200;
                handler.sendMessage(message);
            }
        };
        timer.schedule(task, 0, 50);
    }

    // 即时累加task
    // private void startCacuTask() {
    // cacuTask = new TimerTask() {
    // @Override
    // public void run() {
    // Message message = new Message();
    // message.what = 300;
    // handler.sendMessage(message);
    // }
    // };
    // timer.schedule(cacuTask, 0, 1000);
    // }

    @Override
    public void initIntentParam(Intent intent) {
        // TODO Auto-generated method stub
        title = getResources().getString(R.string.sports_health_sleep);
    }

    @Override
    public void initView() {
        // TODO Auto-generated method stub

    }

    @Override
    public void setViewStatus() {
        // TODO Auto-generated method stub
        showContentView(R.layout.activity_sleep_main);
        msApp = (SportsApp) getApplication();
        mContext = this;
        // health_sleep_bolayout = (LinearLayout)
        // findViewById(R.id.health_sleep_bolayout);
        starttext = (TextView) findViewById(R.id.start_btn);
        sleep_tip_content = (TextView) findViewById(R.id.sleep_tip_content);
        starttext.setOnClickListener(this);
        findViewById(R.id.sleep_record).setOnClickListener(this);
        contentDB = SportContentDB.getInstance(getApplicationContext());
        iView = new ImageButton(this);
        iView.setBackgroundDrawable(getResources().getDrawable(
                R.drawable.sleep_righttitle));
        iView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT));
        iView.setId(IVEW_ID);
        iView.setOnClickListener(new rightOnClickListener());
        showRightBtn(iView);
        right_btn.setPadding(0, 0, msApp.dip2px(17), 0);
        right_btn.setId(RIGHT_ID);
        right_btn.setOnClickListener(new rightOnClickListener());

        pageContet = getResources().getStringArray(R.array.sleep_page_content);
        SimpleDateFormat sf1 = new SimpleDateFormat("yyyyMMdd");// 这个就是只有年月日
        times = sf1.format(new Date());
        if (msApp != null && msApp.getSportUser() != null) {
            changeContent();
        } else {
            sleep_tip_content.setText(pageContet[0]);
        }

        leftButton.setId(LEFTIVEW_ID);
        leftButton.setOnClickListener(this);
        left_ayout.setId(LEFTIOUT_ID);
        left_ayout.setOnClickListener(this);

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 200:
                        // 刷新图表
                        // updateChart();
                        updatView();
                        break;
                    case 300:
                        int recLen = (int) ((System.currentTimeMillis() - startTimeSeconds) / 1000L);
                        if (isStart) {
                            stopWatchView_time.setText(SportTaskUtil
                                    .showTimeCount(recLen));
                        }
                        break;
                    default:
                        break;
                }

                super.handleMessage(msg);
            }
        };
        // setLineView();
        // LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT,
        // msApp.dip2px(150));
        // health_sleep_bolayout.addView(chart, params);
        stopWatchView = (StopWatchView) findViewById(R.id.stopWatchView);
        stopWatchView_time = (TextView) findViewById(R.id.stopWatchView_time);

        // timeTx = new TextView(mContext);
        // android.widget.LinearLayout.LayoutParams params2 = new
        // LinearLayout.LayoutParams(
        // LinearLayout.LayoutParams.WRAP_CONTENT,
        // LinearLayout.LayoutParams.WRAP_CONTENT);
        // params2.setMargins(0, msApp.dip2px(5), 0, 0);
        // params2.gravity = Gravity.CENTER_HORIZONTAL;
        // timeTx.setTextColor(getResources().getColor(R.color.white));
        // timeTx.setTextSize(15f);
        // timeTx.setLayoutParams(params2);
        // health_sleep_bolayout.addView(timeTx);
    }

    @Override
    public void onPageResume() {
        // TODO Auto-generated method stub
        sensorManager = (SensorManager) this
                .getSystemService(Context.SENSOR_SERVICE);
    }

    @Override
    public void onPagePause() {
        // TODO Auto-generated method stub

    }

    @Override
    public void onPageDestroy() {
        // TODO Auto-generated method stub
        if (sensorManager != null) {
            sensorManager.unregisterListener(mSensorEventListener);
        }
        num = 0;
        flag = 0;
        mTempCount = 0;
        unique_id = "";
        startTime = "";
        endTime = "";
        isallbright = false;
        sleepEffect = null;
        startTimeSeconds = 0;
        endTimeSeconds = 0;
        screenEndTimeSeconds = 0;
        screenStartTimeSeconds = 0;
        mTroughTime = 0;
        // min=0;
        // max=0;
        // xNum=0;
        // yNum = 0;
        if (timer != null) {
            timer = null;
        }
        if (task != null) {
            task = null;
        }
        // if (cacuTask != null) {
        // cacuTask = null;
        // }
        pageContet = null;
        // isFirst = false;

        arrayList.clear();
        timeCount = 0;
        firstPointLeftIndex = 0;
        if (isRun) {
            Intent stopIntent = new Intent(this, SleepService.class);
            stopService(stopIntent);
        }
//		msApp=null;
    }

    public final SensorEventListener mSensorEventListener = new SensorEventListener() {
        public void onAccuracyChanged(Sensor paramAnonymousSensor,
                                      int paramAnonymousInt) {
        }

        public void onSensorChanged(SensorEvent event) {
            // TODO Auto-generated method stub
            final int type = event.sensor.getType();
            // Log.v("onSensorChanged","type=" + type);

            // Log.v("SensorChanged","type=" + type);
            if (type == Sensor.TYPE_ACCELEROMETER) {
                float accelerometer;
                mX = event.values[0];
                mY = event.values[1];
                mZ = event.values[2];
                accelerometer = (float) Math.sqrt(mX * mX + mY * mY + mZ * mZ);
                Log.v("bed", "accelerometer=" + accelerometer);
                SwingCount(mX, mY, mZ);
            }

        }
    };

    // 说明
    class rightOnClickListener implements OnClickListener {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            switch (v.getId()) {
                case IVEW_ID:
                case RIGHT_ID:
                    startActivity(new Intent(SleepMainActivity.this,
                            SleepExampleActivity.class));
                    break;
            }
        }
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.start_btn:
                if (isStart) {
                    endSleep();
                } else {
                    if (msApp.LoginOption && msApp.getSportUser() != null
                            && msApp.getSportUser().getUid() != 0) {
                        startTask();
                        // startCacuTask();
                        startSleep();
                        Intent startIntent = new Intent(this, SleepService.class);
                        startService(startIntent);
                        isRun = true;
                    } else {
                        Toast.makeText(
                                mContext,
                                getResources().getString(
                                        R.string.sleep_try_to_login),
                                Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case R.id.sleep_record:
                // List<SleepEffect> queryUidSleep = contentDB.queryUidSleep(msApp
                // .getSportUser().getUid() + "");
                // if (queryUidSleep != null) {
                // Toast.makeText(mContext, queryUidSleep.size() + "aaaaaa",
                // Toast.LENGTH_LONG).show();
                // } else {
                // Toast.makeText(mContext, "错误aaaaaa", Toast.LENGTH_LONG).show();
                // }
                if (!isStart) {
                    if (msApp.isOpenNetwork()) {
                        waitShowDialog(getResources().getString(R.string.loading));
                        new AsyncTask<Void, Void, List<SleepEffect>>() {
                            @Override
                            protected List<SleepEffect> doInBackground(
                                    Void... params) {
                                // TODO Auto-generated method stub
                                List<SleepEffect> back = null;
                                try {
                                    back = ApiJsonParser.getSleepDate(
                                            msApp.getSessionId(), 0, true);
                                } catch (ApiNetException e) {
                                    e.printStackTrace();
                                }
                                return back;
                            }

                            @Override
                            protected void onPostExecute(List<SleepEffect> result) {
                                // TODO Auto-generated method stub
                                super.onPostExecute(result);
                                if (result != null) {
                                    if (result.size() <= 0) {
                                        if (mLoadProgressDialog != null)
                                            if (mLoadProgressDialog.isShowing())
                                                mLoadProgressDialog.dismiss();
                                        Intent intent = new Intent(mContext,
                                                SleepHistoryActivity.class);
                                        startActivity(intent);
                                    } else {
                                        for (int i = 0; i < result.size(); i++) {
                                            contentDB.insertSleep(false,
                                                    result.get(i));
                                        }
                                        if (mLoadProgressDialog != null)
                                            if (mLoadProgressDialog.isShowing())
                                                mLoadProgressDialog.dismiss();
                                        Intent intent = new Intent(mContext,
                                                SleepHistoryActivity.class);
                                        startActivity(intent);
                                    }

                                } else {
                                    if (mLoadProgressDialog != null)
                                        if (mLoadProgressDialog.isShowing())
                                            mLoadProgressDialog.dismiss();
                                    Intent intent = new Intent(mContext,
                                            SleepHistoryActivity.class);
                                    startActivity(intent);
                                }

                            }
                        }.execute();
                    } else {
                        Intent intent = new Intent(mContext,
                                SleepHistoryActivity.class);
                        startActivity(intent);
                    }

                } else {
                    Toast.makeText(mContext,
                            getResources().getString(R.string.sleep_loading),
                            Toast.LENGTH_SHORT).show();
                }
                break;
            case LEFTIVEW_ID:
            case LEFTIOUT_ID:
                if (isStart) {
                    Toast.makeText(mContext,
                            getResources().getString(R.string.sleep_loading),
                            Toast.LENGTH_SHORT).show();
                } else {
                    finish();
                }
                break;
            default:
                break;
        }

    }

    // 开始睡眠
    private void startSleep() {
        isStart = true;
        starttext.setText(getResources().getString(R.string.end_sleep));
        sensorManager.registerListener(mSensorEventListener,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_UI);
        startTimeSeconds = System.currentTimeMillis();

        Date startDate = new Date(startTimeSeconds);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        startTime = formatter.format(startDate);

        unique_id = msApp.getSportUser().getUid() + "" + startTimeSeconds;

        isbright = true;
        isallbright = true;
        screenStartTimeSeconds = System.currentTimeMillis();
        registPingmu();
        // 后台统计睡眠点击率
        if (msApp != null && msApp.getSessionId() != null
                && msApp.isOpenNetwork()) {
            healthCount();
        }

        // 点击开始睡眠就启动激活进程  防止用户被踢出
        new AsyncTask<Void, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(Void... params) {
                // TODO Auto-generated method stub
                Api.addUserLoginTime(msApp.getSessionId());
                return true;
            }

            @Override
            protected void onPostExecute(Boolean result) {
                // TODO Auto-generated method stub

            }
        }.execute();
    }

    // 结束睡眠
    private void endSleep() {
        isStart = false;
        starttext.setText(getResources().getString(R.string.start_sleep));
        if (sensorManager != null) {
            sensorManager.unregisterListener(mSensorEventListener);
        }
        endTimeSeconds = System.currentTimeMillis();
        Date endDate = new Date(endTimeSeconds);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        endTime = formatter.format(endDate);
        unregisterReceiver(mBatInfoReceiver);
        isallbright = false;
        isbright = false;
        screenEndTimeSeconds = System.currentTimeMillis();
        mTempCount = (mTempCount + (screenEndTimeSeconds - screenStartTimeSeconds) / 1000L);
        if (timer != null) {
            if (task != null) {
                task.cancel();
            }
            // if (cacuTask != null) {
            // cacuTask.cancel();
            // }
        }
        // if (series1 != null && chart != null) {
        // series1.clear();
        // series1.add(new Date(), 0);
        // chart.invalidate();
        // }

        // 不超过十分钟无法保存记录
        if ((endTimeSeconds - startTimeSeconds) / 1000L < 600) {
            Toast.makeText(mContext,
                    getResources().getString(R.string.sleep_toast),
                    Toast.LENGTH_SHORT).show();
        } else {
            if (isallbright) {
                Toast.makeText(mContext,
                        getResources().getString(R.string.sleep_toast1),
                        Toast.LENGTH_SHORT).show();
            } else {
                sleepEffect = new SleepEffect();
                sleepEffect.setUid(msApp.getSportUser().getUid() + "");
                sleepEffect.setUnique_id(unique_id);
                sleepEffect.setStarttime(startTime);
                sleepEffect.setEndtime(endTime);
                sleepEffect.setHart_rate(calculate(mTempCount));
                sleepEffect.setBright_time(mTempCount + "");
                sleepEffect.setShock_num(num + "");
                contentDB.insertSleep(true, sleepEffect);
                uploadSleep(sleepEffect);
            }
        }

        num = 0;
        flag = 0;
        mTempCount = 0;
        unique_id = "";
        startTime = "";
        endTime = "";
        isallbright = false;
        sleepEffect = null;
        startTimeSeconds = 0;
        endTimeSeconds = 0;
        screenEndTimeSeconds = 0;
        screenStartTimeSeconds = 0;
        mTempCount = 0;
        // min=0;
        // max=0;
        // xNum=0;
        // yNum = 0;
        // isFirst = false;

        arrayList.clear();
        timeCount = 0;
        firstPointLeftIndex = 0;
        isRun = false;
        Intent stopIntent = new Intent(this, SleepService.class);
        stopService(stopIntent);

    }

    public void SwingCount(float x, float y, float z) {
        float accelerometer = (float) Math.sqrt(x * x + y * y + z * z);
        if (System.currentTimeMillis() < mTroughTime + 200) {
            // mTroughTime = System.currentTimeMillis();
            return;
        }

        if (accelerometer <= 9.4 && accelerometer > 8.8) {
            senserFlag = 1;
            mTroughTime = System.currentTimeMillis();
            if (flag == 0) {
                sensorStartTimeSeconds = System.currentTimeMillis();
                flag++;
            } else {
                // 两分钟之内多次振动算一次
                if ((System.currentTimeMillis() - sensorStartTimeSeconds) / 1000L > 120) {
                    num++;
                    sensorStartTimeSeconds = System.currentTimeMillis();
                } else {
                    flag++;
                }
            }

        }
        if (accelerometer <= 8.8 && accelerometer >= 8.5) {
            senserFlag = 2;
            mTroughTime = System.currentTimeMillis();
            if (flag == 0) {
                sensorStartTimeSeconds = System.currentTimeMillis();
                flag++;
            } else {
                // 两分钟之内多次振动算一次
                if ((System.currentTimeMillis() - sensorStartTimeSeconds) / 1000L > 120) {
                    num++;
                    sensorStartTimeSeconds = System.currentTimeMillis();
                } else {
                    flag++;
                }
            }
        }
        // if (accelerometer < 9 && accelerometer >= 8.5) {
        // senserFlag = 3;
        // mTroughTime = System.currentTimeMillis();
        // if (flag == 0) {
        // sensorStartTimeSeconds = System.currentTimeMillis();
        // flag++;
        // } else {
        // // 两分钟之内多次振动算一次
        // if ((System.currentTimeMillis() - sensorStartTimeSeconds) / 1000 >
        // 120) {
        // num++;
        // sensorStartTimeSeconds = System.currentTimeMillis();
        // } else {
        // flag++;
        // }
        // }
        // }
        if (accelerometer < 8.5) {
            senserFlag = 3;
            mTroughTime = System.currentTimeMillis();
            if (flag == 0) {
                sensorStartTimeSeconds = System.currentTimeMillis();
                flag++;
            } else {
                // 两分钟之内多次振动算一次
                if ((System.currentTimeMillis() - sensorStartTimeSeconds) / 1000L > 120) {
                    num++;
                    sensorStartTimeSeconds = System.currentTimeMillis();
                } else {
                    flag++;
                }
            }
        }
        // if (accelerometer < 8) {
        // senserFlag = 5;
        // mTroughTime = System.currentTimeMillis();
        // if (flag == 0) {
        // sensorStartTimeSeconds = System.currentTimeMillis();
        // flag++;
        // } else {
        // // 两分钟之内多次振动算一次
        // if ((System.currentTimeMillis() - sensorStartTimeSeconds) / 1000 >
        // 120) {
        // num++;
        // sensorStartTimeSeconds = System.currentTimeMillis();
        // } else {
        // flag++;
        // }
        // }
        // }

    }

    // 监听屏幕亮
    private void registPingmu() {
        final IntentFilter filter = new IntentFilter();
        // 屏幕灭屏广播
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        // 屏幕亮屏广播
        filter.addAction(Intent.ACTION_SCREEN_ON);
        // 屏幕解锁广播
        // filter.addAction(Intent.ACTION_USER_PRESENT);
        // 当长按电源键弹出“关机”对话或者锁屏时系统会发出这个广播
        // example：有时候会用到系统对话框，权限可能很高，会覆盖在锁屏界面或者“关机”对话框之上，
        // 所以监听这个广播，当收到时就隐藏自己的对话，如点击pad右下角部分弹出的对话框
        // filter.addAction(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        registerReceiver(mBatInfoReceiver, filter);
    }

    BroadcastReceiver mBatInfoReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(final Context context, final Intent intent) {
            Log.d(TAG, "onReceive");
            String action = intent.getAction();

            if (Intent.ACTION_SCREEN_ON.equals(action)) {
                Log.d(TAG, "screen on");
                isbright = true;
                screenStartTimeSeconds = System.currentTimeMillis();
            } else if (Intent.ACTION_SCREEN_OFF.equals(action)) {
                isbright = false;
                isallbright = false;
                screenEndTimeSeconds = System.currentTimeMillis();
                mTempCount = (mTempCount + (screenEndTimeSeconds - screenStartTimeSeconds) / 1000);
                Log.d(TAG, "screen off");
            } else if (Intent.ACTION_USER_PRESENT.equals(action)) {
                Log.d(TAG, "screen unlock");
            } else if (Intent.ACTION_CLOSE_SYSTEM_DIALOGS.equals(intent
                    .getAction())) {
                Log.i(TAG, " receive Intent.ACTION_CLOSE_SYSTEM_DIALOGS");
            }
        }
    };

    // 计算睡眠效率
    private String calculate(long mTempCount) {
        // 睡眠时常
        double allTime = (double) ((endTimeSeconds - startTimeSeconds) / 1000L);
        double efficiency = 0;
        if (allTime <= 2 * 60 * 60) {
            if (num >= 2) {
                efficiency = (allTime - mTempCount - (num - 2) * 120) / allTime;
            } else {
                efficiency = (allTime - mTempCount - num * 120) / allTime;
            }
        } else {
            if (mTempCount < 600) {
                mTempCount = 600;
            }
            efficiency = (allTime - mTempCount - num * 240) / allTime;
        }

        String lvss1 = "0";
        if (efficiency < 0) {
            efficiency = 0.1;
            lvss1 = SportTaskUtil.getDoubleNumber(efficiency);
            lvss1 = "0.1";
        } else {
            if (efficiency > 0.93) {
                efficiency = 0.93;
            }
            lvss1 = SportTaskUtil.getDoubleNumber(efficiency);
        }

        return lvss1;
    }

    private void uploadSleep(final SleepEffect sleepEffect) {
        waitShowDialog(getResources().getString(R.string.uploading));
        new Thread() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                Message msg = Message.obtain();
                ApiBack apiBack = null;
                try {
                    apiBack = ApiJsonParser.uploadSleep(msApp.getSessionId(),
                            sleepEffect, true);
                    if (apiBack != null && apiBack.getFlag() == 1) {
                        msg.what = UPLOAD_PARAM_SUCCESS;
                        msg.obj = apiBack;
                        sleepHandler.sendMessage(msg);
                    } else {
                        msg.what = UPLOAD_PARAM_ERROR;
                        msg.obj = apiBack;
                        sleepHandler.sendMessage(msg);
                    }
                } catch (Exception e) {
                    // TODO: handle exception
                    msg.what = UPLOAD_PARAM_ERROR;
                    msg.obj = apiBack;
                    sleepHandler.sendMessage(msg);
                }
            }
        }.start();
    }

    private Handler sleepHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case UPLOAD_PARAM_SUCCESS:
                    if (mLoadProgressDialog != null)
                        if (mLoadProgressDialog.isShowing())
                            mLoadProgressDialog.dismiss();
                    Toast.makeText(mContext,
                            getResources().getString(R.string.upload_success),
                            Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(mContext, SleepHistoryActivity.class);
                    intent.putExtra("sleepEffect", sleepEffect);
                    startActivity(intent);
                    break;
                case UPLOAD_PARAM_ERROR:
                    if (mLoadProgressDialog != null)
                        if (mLoadProgressDialog.isShowing())
                            mLoadProgressDialog.dismiss();
                    Toast.makeText(mContext,
                            getResources().getString(R.string.upload_failed),
                            Toast.LENGTH_SHORT).show();

                    Intent intent1 = new Intent(mContext,
                            SleepHistoryActivity.class);
                    intent1.putExtra("sleepEffect", sleepEffect);
                    startActivity(intent1);
                    break;

                default:
                    break;
            }
        }

        ;
    };

    private void waitShowDialog(String message) {
        // TODO Auto-generated method stub
        if (mLoadProgressDialog == null) {
            mLoadProgressDialog = new Dialog(mContext, R.style.sports_dialog);
            LayoutInflater mInflater = getLayoutInflater();
            View v1 = mInflater.inflate(R.layout.bestgirl_progressdialog, null);
            mDialogMessage = (TextView) v1.findViewById(R.id.message);
            mDialogMessage.setText(message);
            v1.setMinimumWidth((int) (SportsApp.ScreenWidth * 0.8));
            mLoadProgressDialog.setContentView(v1);
        }
        if (mLoadProgressDialog != null)
            if (!mLoadProgressDialog.isShowing() && !this.isFinishing())
                if (mDialogMessage != null) {
                    mDialogMessage.setText(message);
                }
        mLoadProgressDialog.show();
        Log.i(TAG, "isFirstshow----");
    }

    /**
     * 改变显示的内容 一周期伟11天
     */
    private void changeContent() {
        FirstPageContent queryData2 = contentDB.queryData();
        try {
            if (queryData2 == null) {
                ContentValues contentValues = new ContentValues();
                contentValues.put(SportContentDB.UID, msApp.getSportUser()
                        .getUid());
                contentValues.put(SportContentDB.SPORT_TIME, times);
                contentValues.put(SportContentDB.SPORT_INDEX, 0);
                contentDB.insert(contentValues, false);
                sleep_tip_content.setText(pageContet[0]);
            } else {
                if (!times.equals(queryData2.getTime())) {
                    if (queryData2.getIndex() == 31) {
                        ContentValues contentValues = new ContentValues();
                        contentValues.put(SportContentDB.UID, msApp
                                .getSportUser().getUid());
                        contentValues.put(SportContentDB.SPORT_TIME, times);
                        contentValues.put(SportContentDB.SPORT_INDEX, 0);
                        contentDB.update(contentValues, msApp.getSportUser()
                                .getUid());
                        sleep_tip_content.setText(pageContet[0]);
                    } else {
                        ContentValues contentValues = new ContentValues();
                        contentValues.put(SportContentDB.UID, msApp
                                .getSportUser().getUid());
                        contentValues.put(SportContentDB.SPORT_TIME, times);
                        contentValues.put(SportContentDB.SPORT_INDEX,
                                queryData2.getIndex() + 1);
                        contentDB.update(contentValues, msApp.getSportUser()
                                .getUid());
                        sleep_tip_content.setText(pageContet[queryData2
                                .getIndex() + 1]);
                    }
                } else {
                    sleep_tip_content
                            .setText(pageContet[queryData2.getIndex()]);
                }

            }
        } catch (Exception e) {
            // TODO: handle exception
        }

    }

    // private void setLineView() {
    // // chart = ChartFactory.getCubeLineChartView(mContext,
    // // getDateDemoDataset(), getDemoRenderer(),
    // // 0.5f);
    // chart = ChartFactory.getTimeChartView(this, getDateDemoDataset(),
    // getDemoRenderer(), "hh:mm:ss");
    // chart.setBackgroundResource(R.drawable.sleep_quxian_bg);
    // }

    /**
     * 数据对象
     *
     * @return
     */
    // private XYMultipleSeriesDataset getDateDemoDataset() {
    // dataset = new XYMultipleSeriesDataset();
    // // xySeries=new XYSeries("");
    // series1 = new TimeSeries("睡眠波动");
    // series1.add(new Date(), 0);
    // dataset.addSeries(series1);
    // // xySeries.add(0, 0);
    // // dataset.addSeries(xySeries);
    // return dataset;
    // }

    /**
     * 设定如表样式
     *
     * @return
     */
    // private XYMultipleSeriesRenderer getDemoRenderer() {
    // renderer = new XYMultipleSeriesRenderer();
    // renderer.setShowAxes(true);
    // renderer.setApplyBackgroundColor(true); // 设置背景使能，为true下面的设置背景才有效
    // renderer.setBackgroundColor(getResources().getColor(
    // R.color.achartgine_color));// 设置表格背景色
    // renderer.setMarginsColor(getResources().getColor(
    // R.color.achartgine_color));// 设置周边背景色
    // renderer.setChartTitleTextSize(14);
    // renderer.setLabelsTextSize(12);
    // renderer.setLegendTextSize(16);
    // renderer.setYLabels(6);
    // renderer.setYAxisMax(20);
    // renderer.setYAxisMin(-20);
    // renderer.setXLabels(8);
    // renderer.setShowGrid(true);
    // renderer.setExternalZoomEnabled(false);// 设置是否可以缩放
    // renderer.setPointSize(0);
    // renderer.setXTitle("时间");
    // renderer.setPanEnabled(false, false);
    // renderer.setYLabelsColor(0,
    // getResources().getColor(R.color.achartgine_Ycolor));
    // XYSeriesRenderer r = new XYSeriesRenderer();
    // r.setColor(getResources().getColor(R.color.achartgine_linecolor));
    // r.setPointStyle(PointStyle.DIAMOND);
    // r.setFillPoints(true);
    // r.setChartValuesTextSize(14);
    // r.setDisplayChartValues(false);
    // r.setChartValuesSpacing(3);
    // r.setPointStrokeWidth(0);
    // r.setDisplayChartValuesDistance(0);
    // renderer.addSeriesRenderer(r);
    // return renderer;
    // }

    // private void updateChart() {
    // // xNum=xNum+5;
    // // if (xNum>100) {
    // // max=max+5;
    // // min=min+5;
    // // xySeries.remove(0);
    // // renderer.setXAxisMax(max);
    // // renderer.setXAxisMin(min);
    // // }
    // // if (flags==0) {
    // // if (senserFlag==1) {
    // // yNum=15;
    // // }else if(senserFlag==2){
    // // yNum=20;
    // // }else {
    // // yNum=5;
    // // }
    // // senserFlag=0;
    // // xySeries.add(xNum, yNum);
    // // flags=flags+1;
    // // //曲线更新
    // // chart.invalidate();
    // // return;
    // // }else if (flags==1) {
    // // yNum=0;
    // // xySeries.add(xNum, yNum);
    // // flags=flags+1;
    // // //曲线更新
    // // chart.invalidate();
    // // return;
    // // }else if (flags==2) {
    // // if (senserFlag==1) {
    // // yNum=-15;
    // // }else if(senserFlag==2){
    // // yNum=-20;
    // // }else {
    // // yNum=-5;
    // // }
    // // senserFlag=0;
    // // xySeries.add(xNum, yNum);
    // // flags=0;
    // // //曲线更新
    // // chart.invalidate();
    // // return;
    // // }
    //
    // if (!isFirst) {
    // long value = new Date().getTime();
    // for (int k = 0; k < 18; k++) {
    // series1.add(new Date(value - k * 500), 0);
    // }
    // isFirst = true;
    // chart.invalidate();
    // return;
    // } else {
    // if (senserFlag == 1) {
    // yNum = 13;
    // // timeTx.setText(new SimpleDateFormat("hh:mm:ss")
    // // .format(new Date()));
    // } else if (senserFlag == 2) {
    // yNum = 18;
    // // timeTx.setText(new SimpleDateFormat("hh:mm:ss")
    // // .format(new Date()));
    // } else {
    // yNum = 0;
    // }
    // senserFlag = 0;
    // series1.remove(0);
    // series1.add(new Date(), yNum);
    // yNum = 0;
    // chart.invalidate();
    // }
    //
    // }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK: {
                if (isStart) {
                    Toast.makeText(mContext,
                            getResources().getString(R.string.sleep_loading),
                            Toast.LENGTH_SHORT).show();
                } else {
                    finish();
                }
                return true;
            }

        }
        return super.onKeyDown(keyCode, event);
    }

    long timeCount = 0;
    int sleepTime = 50;
    int firstPointLeftIndex = 0;
    String[] timeDatas = new String[30]; // 这个数组尽量new大一些，不然上面时间动画总是变动
    ArrayList<Integer> arrayList = new ArrayList<Integer>();

    private void updatView() {
        // 每十分钟保存一下本地数据库
        int times = (int) ((System.currentTimeMillis() - startTimeSeconds) / 1000L);
        if ((times % (1 * 60 * 5)) == 0) {
            if (!isallbright) {
                endTimeSeconds = System.currentTimeMillis();
                Date endDate = new Date(endTimeSeconds);
                SimpleDateFormat formatter = new SimpleDateFormat(
                        "yyyy-MM-dd HH:mm:ss");
                endTime = formatter.format(endDate);
                long mTempCounts = 0;
                if (isbright) {
                    mTempCounts = (mTempCount + (System.currentTimeMillis() - screenStartTimeSeconds) / 1000);
                } else {
                    mTempCounts = mTempCount;
                }
                sleepEffect = new SleepEffect();
                sleepEffect.setUid(msApp.getSportUser().getUid() + "");
                sleepEffect.setUnique_id(unique_id);
                sleepEffect.setStarttime(startTime);
                sleepEffect.setEndtime(endTime);
                sleepEffect.setHart_rate(calculate(mTempCounts));
                contentDB.insertSleep(false, sleepEffect);
            }
        }
        if ((times % (1 * 60 * 60)) == 0) {
            if (!isallbright) {
                endTimeSeconds = System.currentTimeMillis();
                Date endDate = new Date(endTimeSeconds);
                SimpleDateFormat formatter = new SimpleDateFormat(
                        "yyyy-MM-dd HH:mm:ss");
                endTime = formatter.format(endDate);
                long mTempCounts = 0;
                if (isbright) {
                    mTempCounts = (mTempCount + (System.currentTimeMillis() - screenStartTimeSeconds) / 1000);
                } else {
                    mTempCounts = mTempCount;
                }
                sleepEffect = new SleepEffect();
                sleepEffect.setUid(msApp.getSportUser().getUid() + "");
                sleepEffect.setUnique_id(unique_id);
                sleepEffect.setStarttime(startTime);
                sleepEffect.setEndtime(endTime);
                sleepEffect.setHart_rate(calculate(mTempCounts));
                sleepEffect.setBright_time(mTempCounts + "");
                sleepEffect.setShock_num(num + "");

                if (msApp.isOpenNetwork()) {

                    new AsyncTask<Void, Void, ApiBack>() {
                        @Override
                        protected ApiBack doInBackground(Void... params) {
                            // TODO Auto-generated method stub
                            ApiBack back = null;
                            try {
                                back = ApiJsonParser
                                        .uploadSleep(msApp.getSessionId(),
                                                sleepEffect, true);
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

                            } else {
                            }

                        }
                    }.execute();
                    // 启动激活进程
                    new AsyncTask<Void, Void, Boolean>() {
                        @Override
                        protected Boolean doInBackground(Void... params) {
                            // TODO Auto-generated method stub
                            Api.addUserLoginTime(msApp.getSessionId());
                            return true;
                        }

                        @Override
                        protected void onPostExecute(Boolean result) {
                            // TODO Auto-generated method stub

                        }
                    }.execute();
                }

            }

        }

        timeCount += sleepTime;
        // stopWatchTextView.setText("已运行：" + timeCount + "毫秒" + ",时间：" +
        // Tools.formatDuring(timeCount));
        // handler.sendEmptyMessageDelayed(UPDATE_TEXT, sleepTime);
        if (isStart) {
            // stopWatchView_time.setText(Tools.formatDuring(timeCount));
            stopWatchView_time.setText(Tools.formatDuring(System
                    .currentTimeMillis() - startTimeSeconds));
        }
        firstPointLeftIndex--;
        if (firstPointLeftIndex <= -20) {
            firstPointLeftIndex = 0;
        }
        for (int i = 0; i < timeDatas.length; i++) {
            timeDatas[i] = Tools.getTime(timeCount + 1000 * i);
        }
        if (arrayList.size() >= 120) {
            arrayList.remove(0);
        }
        // arrayList.add(Tools.getRadomNumber());
        // if (arrayList.size() > 5) {
        if (senserFlag == 1) {
            arrayList.add(55);
            // arrayList.set(arrayList.size() - 2, 35);
            // arrayList.set(arrayList.size() - 3, 45);
            // arrayList.set(arrayList.size() - 4, 55);
            // arrayList.set(arrayList.size() - 5, 65);
        } else if (senserFlag == 2) {
            arrayList.add(45);
            // arrayList.set(arrayList.size() - 2, 30);
            // arrayList.set(arrayList.size() - 3, 40);
            // arrayList.set(arrayList.size() - 4, 45);
            // arrayList.set(arrayList.size() - 5, 50);
        } else if (senserFlag == 3) {
            arrayList.add(35);
            // arrayList.set(arrayList.size() - 2, 25);
            // arrayList.set(arrayList.size() - 3, 30);
            // arrayList.set(arrayList.size() - 4, 35);
            // arrayList.set(arrayList.size() - 5, 45);
        } else if (senserFlag == 4) {
            arrayList.add(25);
            // arrayList.set(arrayList.size() - 2, 20);
            // arrayList.set(arrayList.size() - 3, 25);
            // arrayList.set(arrayList.size() - 4, 30);
            // arrayList.set(arrayList.size() - 5, 40);
        } else if (senserFlag == 5) {
            arrayList.add(15);
            // arrayList.set(arrayList.size() - 2, 10);
            // arrayList.set(arrayList.size() - 3, 20);
            // arrayList.set(arrayList.size() - 4, 30);
            // arrayList.set(arrayList.size() - 5, 35);
        } else {
            arrayList.add(0);
        }
        // }
        // else {
        // arrayList.add(0);
        // }

        senserFlag = 0;
        stopWatchView.update(timeDatas, firstPointLeftIndex, arrayList);
    }

    // 后台统计睡眠点击的次数
    private void healthCount() {
        new AsyncTask<Void, Void, ApiBack>() {
            @Override
            protected ApiBack doInBackground(Void... params) {
                // TODO Auto-generated method stub
                ApiBack back = null;
                try {
                    back = ApiJsonParser.healthdatacount(msApp.getSessionId(),
                            1, startTime, 0);
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

                } else {
                }

            }
        }.execute();
    }

}
