package com.fox.exercise.api;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.widget.Toast;

import cn.ingenic.indroidsync.SportsApp;

import com.fox.exercise.R;
import com.fox.exercise.api.QQHealthTask.QQHealthResult;
import com.fox.exercise.db.SportSubTaskDB;

public class QQHealthTask extends AsyncTask<String, Void, ApiBack> {

    private int typeId;
    private int sportGoal;
    private double dis;
    private Context mContext;
    private String startTime;
    private QQHealthResult qResult;

    public QQHealthTask(Context mContext, int typeId, double dis, int sportGoal, String startTime, QQHealthResult qResult) {
        // TODO Auto-generated constructor stub
        this.typeId = typeId;
        this.dis = dis;
        this.sportGoal = sportGoal;
        this.mContext = mContext;
        this.startTime = startTime;
        this.qResult = qResult;
    }

    @Override
    protected ApiBack doInBackground(String... params) {
        // TODO Auto-generated method stub
        ApiBack ab = null;
        try {
            ab = ApiJsonParser.qqHealthTask(typeId, dis, sportGoal, params);
        } catch (ApiNetException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return ab;
    }

    @Override
    protected void onPostExecute(ApiBack result) {
        // TODO Auto-generated method stub
        Editor qEditor = mContext.getSharedPreferences("qq_health_sprots", 0).edit();
        if (result != null && result.getFlag() == 0) {
            Toast.makeText(mContext, mContext.getString(R.string.sports_qqhealth), Toast.LENGTH_SHORT).show();
            ArrayList<String> timeList = SportSubTaskDB.getInstance(mContext).getStartTimeByType(SportsApp.getInstance().getSportUser().getUid(), startTime.substring(0, 10), typeId);
            for (String starttime : timeList) {
                qEditor.remove(starttime);
            }
        } else {
            qEditor.putString(startTime, startTime);
            Toast.makeText(mContext, mContext.getString(R.string.sports_qqhealth_fail), Toast.LENGTH_SHORT).show();
        }
        qEditor.commit();
        if (qResult != null)
            qResult.qqResult();
    }

    public interface QQHealthResult {
        public void qqResult();
    }
}
