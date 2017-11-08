package com.fox.exercise.newversion.bushutongji;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.fox.exercise.R;
import com.fox.exercise.api.ApiJsonParser;
import com.fox.exercise.api.ApiMessage;
import cn.ingenic.indroidsync.SportsApp;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

public class BuShuTongJiService extends Service {

    private String TAG = "develop_debug";
    private BuShuTongJiReceiver receiver;
    private IntentFilter filter;
    private IntentFilter stopFilter;
    private SensorManager sensorManager;
    private boolean mTroughAppear;
    private int steps;
    private Intent jibuIntent;
    private BuShuTongJiDB db;
    private TimerTask task;
    private Timer timer;
    private final int GET_FROM_LOCAL = 1;
    private final int UPDATE_STEPS = 2;
    private final int SAVE_TO_LOCAL = 3;
    private final int SAVE_TO_NETWORK = 4;
    private int height, weight;
    private int lastUid;
    private boolean normalStop;
    private SharedPreferences spf;
    private SharedPreferences.Editor spf_e;
    private int uploadSteps = 0;

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();

        Log.e(TAG, "BuShuTongJiService onCreate");
        uploadSteps = 0;

        lastUid = SportsApp.getInstance().getSportUser().getUid();
        writeJiBuHistory("BuShuTongJiService.java line 72 onCreate lastUid " + lastUid + "\n");

        filter = new IntentFilter(Intent.ACTION_TIME_TICK);
        receiver = new BuShuTongJiReceiver();
        registerReceiver(receiver, filter);

        stopFilter = new IntentFilter("com.fox.exercise.newversion.bushutongji.STOP_SERVICE");
        registerReceiver(new stopReceiver(), stopFilter);

        jibuIntent = new Intent("com.fox.exercise.newversion.bushutongji.JIBU");

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Sensor ss = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if (ss == null) {
            Log.e(TAG, "BuShuTongJiService Sensor not supported");
        } else {
            Log.e(TAG, "BuShuTongJiService Sensor supported");
            sensorManager.registerListener(JiBuListener,
                    sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                    SensorManager.SENSOR_DELAY_NORMAL);
        }

        task = new TimerTask() {
            public void run() {
                int uid = SportsApp.getInstance().getSportUser().getUid();
                double gongli = height * 0.45 / 100000 * steps;
                double qianka = weight * gongli * 1.036;

                writeJiBuHistory("BuShuTongJiService.java line 100 TimerTask " + steps + " 步\n");

                Date dateSaveToLocal = new Date(System.currentTimeMillis());
                SimpleDateFormat formatterSaveToLocal = new SimpleDateFormat("yyyy-MM-dd");
                String formatDateSaveToLocal = formatterSaveToLocal.format(dateSaveToLocal);

                if (checkAnotherDay()) {
                    BuShuTongJiDetail detail = new BuShuTongJiDetail(uid, -1, steps, gongli,
                                                                     qianka, spf.getString("date", formatDateSaveToLocal),
                                                                     0);
                    saveBuShuTongJiToLocal(detail);
                    writeJiBuHistory("BuShuTongJiService.java line 111 TimerTask " + steps + " 步\n");

                    steps = 0;

                    writeJiBuHistory("BuShuTongJiService.java line 515 " + steps + " 步\n");
                    Message msgSaveToLocal = updateStepsHandler.obtainMessage();
                    msgSaveToLocal.what = SAVE_TO_LOCAL;
                    msgSaveToLocal.sendToTarget();
                }

                Log.e(TAG, "uid : " + uid);
                if (SportsApp.getInstance().isOpenNetwork() && (uid > 0)) {
                    Message message = new Message();
                    message.what = SAVE_TO_NETWORK;
                    updateStepsHandler.sendMessage(message);
                }
            }
        };

        timer = new Timer(true);
        timer.schedule(task, 10000, 600000);

        spf = this.getSharedPreferences("bushutongji.xml", Context.MODE_PRIVATE);
        spf_e = spf.edit();

        mTroughAppear = false;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // TODO Auto-generated method stub
        Log.e(TAG, "BuShuTongJiService onStartCommand");

        height = SportsApp.getInstance().getSportUser().getHeight();
        if (height <= 0) {
            height = 170;
        }
        weight = SportsApp.getInstance().getSportUser().getWeight();
        if (weight <= 0) {
            weight = 65;
        }

        writeJiBuHistory("BuShuTongJiService.java line 154 onStartCommand nowUid " + SportsApp.getInstance().getSportUser().getUid() + "\n");

        if (checkAnotherDay()) {
            steps = 0;

            writeJiBuHistory("BuShuTongJiService.java line 159 " + steps + " 步\n");
            Message msgSaveToLocal = updateStepsHandler.obtainMessage();
            msgSaveToLocal.what = SAVE_TO_LOCAL;
            msgSaveToLocal.sendToTarget();
        }

        if (lastUid != SportsApp.getInstance().getSportUser().getUid()) {
            double gongli = height * 0.45 / 100000 * steps;
            double qianka = weight * gongli * 1.036;

            Date dateSaveToLocal = new Date(System.currentTimeMillis());
            SimpleDateFormat formatterSaveToLocal = new SimpleDateFormat("yyyy-MM-dd");
            String formatDateSaveToLocal = formatterSaveToLocal.format(dateSaveToLocal);

            BuShuTongJiDetail detail = new BuShuTongJiDetail(lastUid, -1, steps, gongli, qianka, formatDateSaveToLocal, 0);
            saveBuShuTongJiToLocal(detail);
            writeJiBuHistory("BuShuTongJiService.java line 175 change uid steps " + steps + " 步\n");
            steps = 0;
            lastUid = SportsApp.getInstance().getSportUser().getUid();
        }

        syncDatabase(SportsApp.getInstance().getSportUser().getUid());
        addMissRecord(SportsApp.getInstance().getSportUser().getUid());

        if (SportsApp.getInstance().isOpenNetwork() && (SportsApp.getInstance().getSportUser().getUid() > 0)) {
            new GetCurrentDayBuShu().execute();
        } else {
            Message msg = updateStepsHandler.obtainMessage();
            msg.what = GET_FROM_LOCAL;
            msg.sendToTarget();
        }

        flags = START_STICKY;
        return super.onStartCommand(intent, flags, startId);
    }

    /* (non-Javadoc)
     * @see android.app.Service#onDestroy()
     */
    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        Log.e(TAG, "WatchDogService onDestroy");

        if (db != null) {
            db.close();
            db = null;
        }

        if (task != null) {
            task.cancel();
        }

        if (timer != null) {
            timer.cancel();
        }

        unregisterReceiver(receiver);
        sensorManager.unregisterListener(JiBuListener);
        if (!normalStop) {
            Intent restart = new Intent(this, BuShuTongJiService.class);
            this.startService(restart);
        }
    }

    private class stopReceiver extends BroadcastReceiver {
        public void onReceive(Context context, Intent intent) {
            Log.e(TAG, "stop intent");

            normalStop = spf.getBoolean("stop_service", true);

            Message msgSaveToLocal = updateStepsHandler.obtainMessage();
            msgSaveToLocal.what = SAVE_TO_LOCAL;
            msgSaveToLocal.sendToTarget();
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        Log.e(TAG, "WatchDogService onBind");
        return null;
    }

    private void addMissRecord(int uid) {
        Log.e(TAG, "addMissRecord uid : " + uid);
        db = BuShuTongJiDB.getInstance(BuShuTongJiService.this);
        String strFirstDate = db.getFirstRecord(uid);
        if (strFirstDate == null) {
            return;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date firstDate;
        Date nowDate = new Date(System.currentTimeMillis());
        Log.e(TAG, "firstDate : " + strFirstDate + ", nowDate : " + sdf.format(nowDate));
        try {
            firstDate = sdf.parse(strFirstDate);
            firstDate.setTime(firstDate.getTime() + 24 * 3600 * 1000);
            if (firstDate.after(nowDate) || (sdf.format(firstDate).equalsIgnoreCase(sdf.format(nowDate)))) {
                return;
            }
            Log.e(TAG, "firstDate : " + sdf.format(firstDate) + ", nowDate : " + sdf.format(nowDate));
            while (true) {
                if (-1 == db.getBuShuByDay(uid, sdf.format(firstDate))) {
                    ContentValues values = new ContentValues();
                    values.put(BuShuTongJiDB.UID_I, uid);
                    values.put(BuShuTongJiDB.ID_I, -1);
                    values.put(BuShuTongJiDB.STEP_NUM_I, 0);
                    values.put(BuShuTongJiDB.DISTANCE_D, 0);
                    values.put(BuShuTongJiDB.STEP_CALORIE_D, 0);
                    values.put(BuShuTongJiDB.DAY_S, sdf.format(firstDate));
                    values.put(BuShuTongJiDB.IS_UPLOAD_I, 0);
                    db.insert(values, false);
                }

                firstDate.setTime(firstDate.getTime() + 24 * 3600 * 1000);
                Log.e(TAG, "firstDate : " + sdf.format(firstDate) + ", nowDate : " + sdf.format(nowDate));
                if (firstDate.after(nowDate) || (sdf.format(firstDate).equalsIgnoreCase(sdf.format(nowDate)))) {
                    return;
                }
            }
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void syncDatabase(int uid) {
        Log.e(TAG, "syncDatabase uid : " + uid);

        if (uid <= 0) {
            return;
        }

        db = BuShuTongJiDB.getInstance(BuShuTongJiService.this);
        BuShuTongJiDetail detail0 = db.getUnLoginRecord();
        BuShuTongJiDetail detail1;

        while (detail0 != null) {
            detail1 = db.getRecordByDay(uid, detail0.getDay());

            if (detail1 != null) {
                ContentValues values1 = new ContentValues();
                values1.put(BuShuTongJiDB.UID_I, detail1.getUid());
                values1.put(BuShuTongJiDB.ID_I, detail1.getId());
                values1.put(BuShuTongJiDB.STEP_NUM_I, detail1.getStep_num() + detail0.getStep_num());
                values1.put(BuShuTongJiDB.DISTANCE_D, detail1.getDistance() + detail0.getDistance());
                values1.put(BuShuTongJiDB.STEP_CALORIE_D, detail1.getStep_Calorie() + detail0.getStep_Calorie());
                values1.put(BuShuTongJiDB.DAY_S, detail1.getDay());
                values1.put(BuShuTongJiDB.IS_UPLOAD_I, detail1.getIs_upload());
                db.update(values1, detail1.getUid(), detail1.getDay(), false);
                writeJiBuHistory("BuShuTongJiService.java line 312 原有 " + detail1.getStep_num() + "步， 新增" + detail0.getStep_num() + " 步\n");
            } else {
                ContentValues values1 = new ContentValues();
                values1.put(BuShuTongJiDB.UID_I, uid);
                values1.put(BuShuTongJiDB.ID_I, -1);
                values1.put(BuShuTongJiDB.STEP_NUM_I, detail0.getStep_num());
                values1.put(BuShuTongJiDB.DISTANCE_D, detail0.getDistance());
                values1.put(BuShuTongJiDB.STEP_CALORIE_D, detail0.getStep_Calorie());
                values1.put(BuShuTongJiDB.DAY_S, detail0.getDay());
                values1.put(BuShuTongJiDB.IS_UPLOAD_I, 0);
                db.insert(values1, false);
                writeJiBuHistory("BuShuTongJiService.java line 323 新增" + detail0.getStep_num() + " 步\n");
            }

            db.deleteRecord(detail0.getUid(), detail0.getDay());
            detail0 = db.getUnLoginRecord();
        }
    }

    private Handler updateStepsHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case GET_FROM_LOCAL:
                    Date date = new Date(System.currentTimeMillis());
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                    String formatDate = formatter.format(date);

                    db = BuShuTongJiDB.getInstance(BuShuTongJiService.this);
                    int localSteps = db.getBuShuByDay(SportsApp.getInstance().getSportUser().getUid(), formatDate);
                    Log.e(TAG, "localSteps : " + localSteps);
                    writeJiBuHistory("BuShuTongJiService.java line 345 steps " + steps + " localSteps" + localSteps + " \n");
                    if (localSteps > steps) {
                        steps = localSteps;
                    }

                    Message msg2 = updateStepsHandler.obtainMessage();
                    msg2.what = UPDATE_STEPS;
                    msg2.sendToTarget();
                    break;
                case UPDATE_STEPS:
                    double gongliUpdate = height * 0.45 / 100000 * steps;
                    double qiankaUpdate = weight * gongliUpdate * 1.036;

                    jibuIntent.putExtra("steps", steps);
                    jibuIntent.putExtra("calories", qiankaUpdate);
                    sendBroadcast(jibuIntent);
                    break;
                case SAVE_TO_LOCAL:
                    int uid = SportsApp.getInstance().getSportUser().getUid();
                    double gongli = height * 0.45 / 100000 * steps;
                    double qianka = weight * gongli * 1.036;

                    Date dateSaveToLocal = new Date(System.currentTimeMillis());
                    SimpleDateFormat formatterSaveToLocal = new SimpleDateFormat("yyyy-MM-dd");
                    String formatDateSaveToLocal = formatterSaveToLocal.format(dateSaveToLocal);

                    spf_e.putInt("steps", steps);
                    spf_e.putFloat("calories", (float) qianka);
                    spf_e.putString("date", formatDateSaveToLocal);
                    spf_e.commit();

                    BuShuTongJiDetail detail = new BuShuTongJiDetail(uid, -1, steps, gongli, qianka, formatDateSaveToLocal, 0);
                    saveBuShuTongJiToLocal(detail);

                    if (normalStop) {
                        BuShuTongJiService.this.stopSelf();
                    }
                    break;
                case SAVE_TO_NETWORK:
                    if (SportsApp.getInstance().isOpenNetwork() && (SportsApp.getInstance().getSportUser().getUid() > 0)) {
                        if (0 == uploadSteps) {
                            new saveBuShuTongJiListToNetWork().execute();
                        } else {
                            new saveBuShuTongJiToNetWork().execute();
                        }
                    }
                    break;
            }
        }
    };

    private class saveBuShuTongJiListToNetWork extends AsyncTask<Void, Void, ApiMessage> {
        private ArrayList<BuShuTongJiDetail> list;

        @Override
        protected ApiMessage doInBackground(Void... params) {
            // TODO Auto-generated method stub
            Date date = new Date(System.currentTimeMillis());
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            String formatDate = formatter.format(date);

            db = BuShuTongJiDB.getInstance(BuShuTongJiService.this);
            list = db.getUnUploadTasksList(SportsApp.getInstance().getSportUser().getUid(), formatDate);
            if (list.size() != 0) {
                return ApiJsonParser.saveBuShuTongJiListToNetWork(SportsApp.getInstance().getSessionId(), list,
                        "android", "z" + getResources().getString(R.string.config_game_id));
            } else {
                uploadSteps = 1;
                return null;
            }
        }

        @Override
        protected void onPostExecute(ApiMessage result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);

            if ((result != null) && (result.getMsg() != null)) {
                Log.e(TAG, "save steplists to network : " + result.getMsg().toString());

                try {
                    JSONObject obj = new JSONObject(result.getMsg()).getJSONObject("data");
                    if ((obj != null) && (obj.getInt("flag") == 0)) {
                        Log.e(TAG, "save steps to network success");
                        BuShuTongJiDetail detail;

                        for (int i = 0; i < list.size(); i++) {
                            detail = list.get(i);
                            detail.setIs_upload(1);
                            saveBuShuTongJiToLocal(detail);
                        }

                        uploadSteps = 1;
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    Log.e(TAG, "saveBuShuTongJiToNetWork JSONException : " + e.toString());
                }
            }
        }
    }

    private class saveBuShuTongJiToNetWork extends AsyncTask<Void, Void, ApiMessage> {
        private BuShuTongJiDetail detail;

        @Override
        protected ApiMessage doInBackground(Void... params) {
            // TODO Auto-generated method stub
            db = BuShuTongJiDB.getInstance(BuShuTongJiService.this);
            detail = db.getUnUploadTask(SportsApp.getInstance().getSportUser().getUid());
            if (detail != null) {
                return ApiJsonParser.saveBuShuTongJiToNetWork(SportsApp.getInstance().getSessionId(),
                        detail.getStep_num(), detail.getDay(),
                        "z" + getResources().getString(R.string.config_game_id));
            } else {
                return null;
            }
        }

        @Override
        protected void onPostExecute(ApiMessage result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);

            if (result != null && result.getMsg() != null) {
                Log.e(TAG, "result : " + result.getMsg().toString());

                try {
                    JSONObject obj = new JSONObject(result.getMsg()).getJSONObject("data");
                    if ((obj != null) && (obj.getInt("flag") == 0)) {
                        Log.e(TAG, "save steps to network success");
                        detail.setIs_upload(1);
                        detail.setId(obj.getInt("id"));
                        saveBuShuTongJiToLocal(detail);
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    Log.e(TAG, "saveBuShuTongJiToNetWork JSONException : " + e.toString());
                }
            }
        }
    }

    private int saveBuShuTongJiToLocal(BuShuTongJiDetail detail) {

        int saveResult = 0;

        if (detail == null) {
            Log.e(TAG, "detail is null");
            return -1;
        }

        db = BuShuTongJiDB.getInstance(BuShuTongJiService.this);

        ContentValues values = new ContentValues();
        values.put(BuShuTongJiDB.UID_I, detail.getUid());
        values.put(BuShuTongJiDB.ID_I, detail.getId());
        values.put(BuShuTongJiDB.STEP_NUM_I, detail.getStep_num());
        values.put(BuShuTongJiDB.DISTANCE_D, detail.getDistance());
        values.put(BuShuTongJiDB.STEP_CALORIE_D, detail.getStep_Calorie());
        values.put(BuShuTongJiDB.DAY_S, detail.getDay());
        values.put(BuShuTongJiDB.IS_UPLOAD_I, detail.getIs_upload());

        Cursor cursor = db.query(detail.getUid(), detail.getDay());

        if (cursor != null) {
            if (!cursor.moveToFirst()) {
                saveResult = db.insert(values, false);
            } else {
                if (detail.getStep_num() >= cursor.getInt(cursor.getColumnIndex(BuShuTongJiDB.STEP_NUM_I))) {
                    saveResult = db.update(values, detail.getUid(), detail.getDay(), false);
                    writeJiBuHistory("BuShuTongJiService.java line 466 " + detail.getStep_num() + " 步\n");
                }
            }
        } else {
            saveResult = db.insert(values, false);
            writeJiBuHistory("BuShuTongJiService.java line 471 " + detail.getStep_num() + " 步\n");
        }

        if (cursor != null) {
            cursor.close();
            cursor = null;
        }

        Log.e(TAG, "save data to local : " + saveResult);
        return saveResult;
    }

    private class GetCurrentDayBuShu extends AsyncTask<Void, Void, ApiMessage> {

        @Override
        protected ApiMessage doInBackground(Void... params) {
            // TODO Auto-generated method stub
            Date date = new Date(System.currentTimeMillis());
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            String formatDate = formatter.format(date);
            return ApiJsonParser.GetCurrentDayBuShu(SportsApp.getInstance().getSessionId(),
                    formatDate);
        }

        @Override
        protected void onPostExecute(ApiMessage result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);

            if (result != null) {
                try {
                    JSONObject obj = new JSONObject(result.getMsg());
                    JSONArray jsonArray = obj.getJSONArray("data");
                    obj = jsonArray.getJSONObject(0);

                    int networkSteps = obj.getInt("step_num");
                    if (networkSteps > steps) {
                        steps = networkSteps;
                        writeJiBuHistory("BuShuTongJiService.java line 513 " + steps + " 步\n");
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    Log.e(TAG, "GetCurrentDayBuShu JSONException : " + e.toString());
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            Message msg1 = updateStepsHandler.obtainMessage();
            msg1.what = GET_FROM_LOCAL;
            msg1.sendToTarget();
        }
    }

    private void CountStep(float x, float y, float z) {
        float accelerometer = (float) Math.sqrt(x * x + y * y + z * z);
        if (accelerometer < 8.2) {
            mTroughAppear = true;
        }

        if (accelerometer > 9.8) {
            if (mTroughAppear) {
                steps++;

                Message msgStep = updateStepsHandler.obtainMessage();
                msgStep.what = UPDATE_STEPS;
                msgStep.sendToTarget();

                Log.e(TAG, "steps : " + steps);

                if ((steps % 10) == 0) {
                    if (checkAnotherDay()) {
                        steps = 0;
                    }
                    writeJiBuHistory("BuShuTongJiService.java line 548 " + steps + " 步\n");
                    Message msgSaveToLocal = updateStepsHandler.obtainMessage();
                    msgSaveToLocal.what = SAVE_TO_LOCAL;
                    msgSaveToLocal.sendToTarget();
                }

                mTroughAppear = false;
            }
        }
    }

    private SensorEventListener JiBuListener = new SensorEventListener() {

        @Override
        public void onSensorChanged(SensorEvent event) {
            // TODO Auto-generated method stub
            if (Sensor.TYPE_ACCELEROMETER == event.sensor.getType()) {
                CountStep(event.values[0], event.values[1], event.values[2]);
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
            // TODO Auto-generated method stub
        }
    };

    private void writeJiBuHistory(String infos) {
//        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
//            Date startDate = new Date(System.currentTimeMillis());
//            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            String startTime = formatter.format(startDate);
//            startTime = startTime + " " + infos;
//
//            String path = Environment.getExternalStorageDirectory()
//                    .getAbsolutePath() + "/addJiBuHistory/";
//            File dir = new File(path);
//            if (!dir.exists()) {
//                dir.mkdirs();
//            }
//            try{
//                FileOutputStream fos = new FileOutputStream(path + "jibuhistory.txt", true);
//                fos.write(startTime.getBytes());
//                fos.close();
//            } catch (IOException e) {
//            }
//        }
    }

    private boolean checkAnotherDay() {
        String saveDate = spf.getString("date", "");
        writeJiBuHistory("BuShuTongJiService.java line 599 " + saveDate + "\n");
        if (saveDate.equalsIgnoreCase("")) {
            return false;
        }

        Date startDate = new Date(System.currentTimeMillis());
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String startTime = formatter.format(startDate);
        writeJiBuHistory("BuShuTongJiService.java line 607 " + startTime + "\n");

        if (saveDate.equalsIgnoreCase(startTime)) {
            return false;
        }

        return true;
    }
}