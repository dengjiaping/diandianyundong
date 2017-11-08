package com.fox.exercise.map;

import java.util.ArrayList;
import java.util.List;
import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;

import cn.ingenic.indroidsync.SportsApp;
import cn.ingenic.indroidsync.utils.DeviceUuidFactory;

import com.fox.exercise.R;
import com.fox.exercise.SportsType;
import com.fox.exercise.api.AddCoinsThread;
import com.fox.exercise.api.ApiBack;
import com.fox.exercise.api.ApiJsonParser;
import com.fox.exercise.api.ApiNetException;
import com.fox.exercise.api.ApiSessionOutException;
import com.fox.exercise.api.entity.GetPeisu;
import com.fox.exercise.api.entity.PeisuInfo;
import com.fox.exercise.api.entity.SportTask;
import com.fox.exercise.db.PeisuDB;
import com.fox.exercise.db.SportSubTaskDB;
import com.fox.exercise.util.SportTaskUtil;
import com.fox.exercise.util.SportTrajectoryUtilGaode;

public class SportsTaskUploader {

    private static SportsTaskUploader mIntance = null;
    private boolean mIsUploading = false;
    private int typeId;

    public static SportsTaskUploader getInstance() {
        if (mIntance == null)
            mIntance = new SportsTaskUploader();
        return mIntance;
    }

    public void startBackgroundUpload(Context context, int uid, String sessionId) {
        if (mIsUploading == true)
            return;
        SportSubTaskDB taskDBhelper = SportSubTaskDB.getInstance(context);
        ArrayList<SportTask> unUploadDatas = taskDBhelper
                .getTasksByUnOrIngUpload(uid);
        if (unUploadDatas.size() == 0)
            return;

        mIsUploading = true;

        SportAsyncTask task = new SportAsyncTask();
        task.execute(unUploadDatas, sessionId, context);

    }

    private class SportAsyncTask extends AsyncTask<Object, Integer, Object> {

        private ArrayList<SportTask> unUploadDatas;

        @Override
        protected ApiBack doInBackground(Object... params) {
            unUploadDatas = (ArrayList<SportTask>) params[0];
            Context context = (Context) params[2];
            PeisuInfo peisuInfo;
            for (SportTask taskDetail : unUploadDatas) {

                String sessionId = (String) params[1];

                String startTime = taskDetail.getStart_time();
                typeId = taskDetail.getSport_type_task();
                double dis = taskDetail.getSport_distance();
                String mark_code = taskDetail.getSport_mark_code();
                double con = taskDetail.getSport_calories();
                if (typeId == SportsType.TYPE_CYCLING) {
                    if (dis * 1000 < 500 || con <= 0) {
                        // 骑行的时候小于500米不能保存
                        SportSubTaskDB.getInstance(context)
                                .deleteSportByMarkCode(mark_code);
                        continue;
                    }
                } else {
                    if (dis * 1000 < 100 || con <= 0) {
                        // 其他方式的时候小于100米不能保存
                        SportSubTaskDB.getInstance(context)
                                .deleteSportByMarkCode(mark_code);
                        continue;
                    }
                }
                int typeDetailId = taskDetail.getSports_swim_type();
                if (typeDetailId < 0) {
                    typeDetailId = 0;
                }
                int deviceId = taskDetail.getSport_device();
                int recLen = taskDetail.getSport_time();
                double speed = taskDetail.getSport_speed();
                double heart = taskDetail.getSport_heart_rate();
                double avgSpeed=(dis * 3600) / recLen;
                int step = taskDetail.getSport_step();
                int mapType = taskDetail.getSport_map_type();
                int uid = taskDetail.getUid();
                String pointsStr = taskDetail.getSport_lat_lng();
                String velocity_list=taskDetail.getSport_speedlsit();
                String coordinate_list=taskDetail.getCoordinate_list();
                //上传未上传的配速登山信息
                List<GetPeisu> list=null;
                if(typeId==SportsType.TYPE_CLIMBING){
                    list=PeisuDB.getmInstance(context).selectAll(mark_code);
                }else{
                    if(dis>=1){
                        list=PeisuDB.getmInstance(context).selectAll(mark_code);
                    }
                }
                peisuInfo=new PeisuInfo();
                if(list!=null){
                    peisuInfo.setListpeis(list);
                }
                Boolean normalRange = SportTaskUtil.getNormalRange(
                        typeId, speed, recLen);
                if(!normalRange){
                    continue;
                }
                ApiBack apiBack = null;
                try {
//                    apiBack = ApiJsonParser.uploadSportTask(0, sessionId,
//                            typeId, typeDetailId, deviceId, startTime, recLen,
//                            dis, con, speed, heart, pointsStr, step, mapType,
//                            DeviceUuidFactory.getDeviceSerial(), mark_code,velocity_list);

                    apiBack = ApiJsonParser.uploadSportsTwoInfo(0,
                            sessionId, typeId, typeDetailId,
                            deviceId, startTime, recLen, dis, con, avgSpeed, heart, pointsStr, step,
                            SportsApp.MAP_TYPE_GAODE,
                            DeviceUuidFactory.getDeviceSerial(), mark_code,velocity_list,coordinate_list,
                            "z" + context.getResources().getString(R.string.config_game_id), peisuInfo,1);
                    if (apiBack != null && (apiBack.getFlag() == 1||apiBack.getFlag() == 1010)) {
                        SportSubTaskDB taskDBhelper = SportSubTaskDB
                                .getInstance((Context) params[2]);

                        ContentValues values = new ContentValues();
                        values.put("sport_isupload", 1);
                        values.put("sport_taskid",apiBack.getReg());
                        if (mark_code != null && !"".equals(mark_code)) {
                            taskDBhelper
                                    .newUpdate(values, startTime, mark_code, uid);
                        } else {
                            taskDBhelper.update(values, startTime, uid);
                        }
                       if(list!=null&&list.size()>0&&apiBack.getFlag() == 1){
                           ContentValues c = new ContentValues();
                           c.put(PeisuDB.SPORT_MARKCODE, mark_code);
                           c.put(PeisuDB.SPORT_ISUPLOAD, "1");
                           PeisuDB.getmInstance(context).update(c, mark_code, false);
                       }
                        int coins = 0;
                        // && !speedOverLimit
                        if (normalRange) {
                            coins = dis < 1 ? (dis > 0.5 ? 1 : 0) : (int) Math
                                    .floor(dis);
                            uploadcoins(coins, typeId);
                        }

//                        //上传未上传的配速登山信息
//                        List<GetPeisu> list=PeisuDB.getmInstance(context).selectAll(mark_code);
//                        if(list!=null&&list.size()>0){
//                            peisuInfo=new PeisuInfo();
//                            peisuInfo.setListpeis(list);
//                            ApiBack aback = ApiJsonParser.uploadPeisu(sessionId, apiBack.getReg(), "android", "z" + context.getResources().getString(R.string.config_game_id),
//                                    peisuInfo, taskDetail.getSport_type_task());
//                            if(aback!=null&&aback.getFlag() == 1){
//                                ContentValues c = new ContentValues();
//                                c.put(PeisuDB.SPORT_MARKCODE, mark_code);
//                                c.put(PeisuDB.SPORT_ISUPLOAD, "1");
//                                PeisuDB.getmInstance(context).update(c, mark_code, false);
//                            }
//                            peisuInfo=null;
//                            list=null;
//                        }

                    }else {

                        mIsUploading = false;
                        return null;
                    }
                } catch (ApiNetException e) {
                    e.printStackTrace();
                    return null;
                } catch (ApiSessionOutException e) {
                    e.printStackTrace();
                }
            }
            mIsUploading = false;
            return null;
        }

        protected void onPostExecute(ApiBack result) {
        }
    }

    private void uploadcoins(int coins, int typeId) {
        new AddCoinsThread(coins, 3, null, typeId).start();
    }
}
