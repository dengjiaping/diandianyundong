package com.fox.exercise.newversion.trainingplan;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.ingenic.indroidsync.SportsApp;

import com.fox.exercise.AbstractBaseOtherActivity;
import com.fox.exercise.FindOtherMoreAdapter;
import com.fox.exercise.ImageDownloader;
import com.fox.exercise.R;
import com.fox.exercise.Train_webViewClass;
import com.fox.exercise.api.ApiConstant;
import com.fox.exercise.api.ApiJsonParser;
import com.fox.exercise.api.ApiNetException;
import com.fox.exercise.api.ApiSessionOutException;
import com.fox.exercise.newversion.entity.TrainAction;
import com.fox.exercise.newversion.entity.TrainInfo;
import com.fox.exercise.newversion.entity.TrainPlanList;
import com.fox.exercise.newversion.entity.TrainUserInfo;
import com.fox.exercise.util.RoundedImage;
import com.fox.exercise.util.SportTaskUtil;

import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.TextAppearanceSpan;
import android.text.style.URLSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author loujungang 训练计划信息展示页面
 */
public class TrainPlanMainActivity extends AbstractBaseOtherActivity implements
        OnClickListener {

    private Dialog dialog;
    private FileDownload l;
    private TrainPlanList trainPlanList;
    private SportsApp mApp;
    private static final int FRESH_LIST = 1;
    private static final int LEFTBTID = 14;
    private static final int LEFTBTLAYOUT = 16;
    private TrainInfo mTrainInfo;
    private GridView mGridView, train_usergrid;
    private ImageView train_planimg;
    private Button ib_download_cancel;
    private ImageDownloader mDownloader = null;
    private TrainUserGridAdapter trainUserGridAdapter;
    private LinearLayout trainicon_gridlayout;
    private TextView no_trainuser_txt, play_detailinfo;
    private ListView tarinuser_infolist;
    private TrainUserListAdapter trainUserListAdapter;
    private Button startplay_btn;
    private DownloadRoundProgressBar startplay_probar;
    private String url, downLoadPath, fileName;
    private String dir = Environment.getExternalStorageDirectory().toString()
            + "/android/data/" + SportsApp.getContext().getPackageName()
            + "/cache/";

    private TextView num_train, time_train, train_userNum;
    private boolean isDownLoad = false;
    private int max;

    @Override
    public void initIntentParam(Intent intent) {
        // TODO Auto-generated method stub
        if (intent != null) {
            trainPlanList = (TrainPlanList) intent
                    .getSerializableExtra("TrainPlanList");
            title = trainPlanList.getTrain_name();
        }
    }

    @Override
    public void initView() {
        // TODO Auto-generated method stub
        showContentView(R.layout.activity_train_plan_main);
        left_ayout.setId(LEFTBTLAYOUT);
        left_ayout.setOnClickListener(this);
        leftButton.setId(LEFTBTID);
        leftButton.setOnClickListener(this);
        mDownloader = new ImageDownloader(this);
        mDownloader.setType(ImageDownloader.OnlyOne);
        mApp = (SportsApp) getApplication();
        mGridView = (GridView) findViewById(R.id.grid);
        mGridView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                // TODO Auto-generated method stub
                if(mTrainInfo!=null){
                    Intent intent = new Intent(TrainPlanMainActivity.this,
                            TrainingActionActivity.class);
                    intent.putExtra("action_list",
                            (Serializable) mTrainInfo.getActionlist());
                    intent.putExtra("arg2", arg2);
                    startActivity(intent);
                }
            }
        });
        train_usergrid = (GridView) findViewById(R.id.train_usergrid);
        train_planimg = (ImageView) findViewById(R.id.train_planimg);
        trainicon_gridlayout = (LinearLayout) findViewById(R.id.trainicon_gridlayout);
        no_trainuser_txt = (TextView) findViewById(R.id.no_trainuser_txt);
        tarinuser_infolist = (ListView) findViewById(R.id.tarinuser_infolist);
        startplay_btn = (Button) findViewById(R.id.startplay_btn);
        startplay_btn.setOnClickListener(this);
        ib_download_cancel = (Button) findViewById(R.id.btn_cancel_download);
        ib_download_cancel.setOnClickListener(this);
        startplay_probar = (DownloadRoundProgressBar) findViewById(R.id.startplay_probar);
        num_train = (TextView) findViewById(R.id.num_train);
        time_train = (TextView) findViewById(R.id.time_train);
        train_userNum = (TextView) findViewById(R.id.train_userNum);
        if (mApp != null && mApp.getSessionId() != null
                && !"".equals(mApp.getSessionId()) && trainPlanList != null) {
            if (mApp.isOpenNetwork()) {
                new LoadThread().start();
            } else {
                // 获取播放主页面的缓存页面信息
                SharedPreferences preferences = getSharedPreferences(
                        "CacheTrainInfo", 0);
                String Trainlist_info = preferences.getString(
                        trainPlanList.getId() + "CacheTrainInfo_info", "");
                if (Trainlist_info != null && !"".equals(Trainlist_info)) {
                    TrainInfo trainInfo = null;
                    try {
                        JSONObject jsonObject = new JSONObject(Trainlist_info)
                                .getJSONObject("data");
                        if (jsonObject != null && !"".equals(jsonObject)) {

                            trainInfo = new TrainInfo();
                            if (jsonObject.has("id")) {
                                trainInfo.setId(jsonObject.getInt("id"));
                            }
                            if (jsonObject.has("train_name")) {
                                trainInfo.setTrain_name(jsonObject
                                        .getString("train_name"));
                            }
                            if (jsonObject.has("thumb")) {
                                trainInfo.setThumb(jsonObject
                                        .getString("thumb"));
                            }
                            if (jsonObject.has("grade")) {
                                trainInfo.setGrade(jsonObject.getInt("grade"));
                            }
                            if (jsonObject.has("position")) {
                                trainInfo.setPosition(jsonObject
                                        .getString("position"));
                            }
                            if (jsonObject.has("train_time")) {
                                trainInfo.setTrain_time(jsonObject
                                        .getInt("train_time"));
                            }
                            if (jsonObject.has("train_calorie")) {
                                trainInfo.setTrain_calorie(jsonObject
                                        .getDouble("train_calorie"));
                            }
                            if (jsonObject.has("train_fileurl")) {
                                trainInfo.setTrain_fileurl(jsonObject
                                        .getString("train_fileurl"));
                            }
                            if (jsonObject.has("action_num")) {
                                trainInfo.setAction_num(jsonObject
                                        .getInt("action_num"));
                            }
                            if (jsonObject.has("traincount")) {
                                trainInfo.setTraincount(jsonObject
                                        .getInt("traincount"));
                            }
                            if (jsonObject.has("userlist")) {

                                String str = jsonObject.getString("userlist");
                                if (str != null && !"null".equals(str)) {
                                    JSONArray jsonArray = jsonObject
                                            .getJSONArray("userlist");
                                    if (jsonArray != null
                                            && jsonArray.length() > 0) {
                                        ArrayList<TrainUserInfo> tUserList = new ArrayList<TrainUserInfo>();
                                        TrainUserInfo trainUserInfo = null;
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject obj = jsonArray
                                                    .getJSONObject(i);
                                            trainUserInfo = new TrainUserInfo();
                                            if (obj.has("name")) {
                                                trainUserInfo.setName(obj
                                                        .getString("name"));
                                            }
                                            if (obj.has("img")) {
                                                trainUserInfo.setImg(obj
                                                        .getString("img"));
                                            }
                                            if (obj.has("id")) {
                                                trainUserInfo.setId(obj
                                                        .getInt("id"));
                                            }
                                            if (obj.has("uid")) {
                                                trainUserInfo.setUid(obj
                                                        .getInt("uid"));
                                            }
                                            if (obj.has("train_starttime")) {
                                                trainUserInfo
                                                        .setTrain_starttime(obj
                                                                .getString("train_starttime"));
                                            }
                                            if (obj.has("train_endtime")) {
                                                trainUserInfo
                                                        .setTrain_endtime(obj
                                                                .getString("train_endtime"));
                                            }
                                            if (obj.has("train_calorie")) {
                                                trainUserInfo
                                                        .setTrain_calorie(obj
                                                                .getDouble("train_calorie"));
                                            }
                                            tUserList.add(trainUserInfo);
                                        }
                                        trainInfo.settUserList(tUserList);
                                    }
                                }

                            }
                            if (jsonObject.has("actionlist")) {
                                JSONArray jsonArray = jsonObject
                                        .getJSONArray("actionlist");
                                if (jsonArray != null && jsonArray.length() > 0) {
                                    ArrayList<TrainAction> actionlist = new ArrayList<TrainAction>();
                                    TrainAction trainAction;
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject obj = jsonArray
                                                .getJSONObject(i);
                                        trainAction = new TrainAction();
                                        if (obj.has("id")) {
                                            trainAction.setId(obj.getInt("id"));
                                        }
                                        if (obj.has("action_name")) {
                                            trainAction.setName(obj
                                                    .getString("action_name"));
                                        }
                                        if (obj.has("thumb")) {
                                            trainAction.setThumb(obj
                                                    .getString("thumb"));
                                        }
                                        if (obj.has("listorder")) {
                                            trainAction.setListorder(obj
                                                    .getInt("listorder"));
                                        }
                                        if (obj.has("resttime")) {
                                            trainAction.setResttime(obj
                                                    .getInt("resttime"));
                                        }
                                        if (obj.has("actnum")) {
                                            trainAction.setActnum(obj
                                                    .getInt("actnum"));
                                        }
                                        if (obj.has("videotime")) {
                                            trainAction.setVideotime(obj
                                                    .getInt("videotime"));
                                        }
                                        actionlist.add(trainAction);
                                    }

                                    trainInfo.setActionlist(actionlist);
                                }

                            }
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        // return trainInfo;
                    }

                    if (trainInfo != null) {
                        mTrainInfo = trainInfo;
                        if (mTrainInfo != null) {
                            mDownloader.download(mTrainInfo.getThumb(),
                                    train_planimg, null);
                            time_train.setText("耗时"
                                    + mTrainInfo.getTrain_time() + "分钟");
                            if (mTrainInfo.getActionlist() != null) {
                                setGridView();
                                num_train.setText("共"
                                        + mTrainInfo.getActionlist().size()
                                        + "健身");
                            }
                            if (mTrainInfo.gettUserList() != null) {
                                if (mTrainInfo.gettUserList().size() == 0) {
                                    trainicon_gridlayout
                                            .setVisibility(View.GONE);
                                    tarinuser_infolist.setVisibility(View.GONE);
                                    no_trainuser_txt
                                            .setVisibility(View.VISIBLE);
                                } else {
                                    trainicon_gridlayout
                                            .setVisibility(View.VISIBLE);
                                    tarinuser_infolist
                                            .setVisibility(View.VISIBLE);
                                    no_trainuser_txt.setVisibility(View.GONE);
                                    if(mTrainInfo.getTraincount()>=10000){
                                        //如果人数大于一万就显示万人
                                        double c=mTrainInfo.getTraincount()*1.0/10000;
                                        String ss = "第"+ SportTaskUtil.getDoubleOneNum(c)
                                                + "万人";

                                        SpannableString styledText = new SpannableString(
                                                ss);
                                        styledText.setSpan(new TextAppearanceSpan(
                                                        TrainPlanMainActivity.this,
                                                        R.style.train_playnum_style1), 0, 1,
                                                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                        styledText.setSpan(new TextAppearanceSpan(
                                                        TrainPlanMainActivity.this,
                                                        R.style.train_playnum_style), 1, ss
                                                        .length() - 2,
                                                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                        styledText.setSpan(new TextAppearanceSpan(
                                                        TrainPlanMainActivity.this,
                                                        R.style.train_playnum_style1), ss
                                                        .length() - 2, ss.length(),
                                                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                                        train_userNum.setText(styledText,
                                                TextView.BufferType.SPANNABLE);

                                    }else {
                                        String ss = "第"+mTrainInfo.getTraincount()
                                                + "人";

                                        SpannableString styledText = new SpannableString(
                                                ss);
                                        styledText.setSpan(new TextAppearanceSpan(
                                                        TrainPlanMainActivity.this,
                                                        R.style.train_playnum_style1), 0, 1,
                                                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                        styledText.setSpan(new TextAppearanceSpan(
                                                        TrainPlanMainActivity.this,
                                                        R.style.train_playnum_style), 1, ss
                                                        .length() - 1,
                                                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                        styledText.setSpan(new TextAppearanceSpan(
                                                        TrainPlanMainActivity.this,
                                                        R.style.train_playnum_style1), ss
                                                        .length() - 1, ss.length(),
                                                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                                        train_userNum.setText(styledText,
                                                TextView.BufferType.SPANNABLE);

                                    }
                                }
                                trainUserGridAdapter = new TrainUserGridAdapter(
                                        TrainPlanMainActivity.this,
                                        mTrainInfo.gettUserList());
                                train_usergrid.setAdapter(trainUserGridAdapter);
                                trainUserListAdapter = new TrainUserListAdapter(
                                        TrainPlanMainActivity.this,
                                        mTrainInfo.gettUserList());
                                tarinuser_infolist
                                        .setAdapter(trainUserListAdapter);
                            } else {
                                trainicon_gridlayout.setVisibility(View.GONE);
                                tarinuser_infolist.setVisibility(View.GONE);
                                no_trainuser_txt.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                }

            }
        } else {
            // 获取播放主页面的缓存页面信息
            if (trainPlanList != null) {
                SharedPreferences preferences = getSharedPreferences(
                        "CacheTrainInfo", 0);
                String Trainlist_info = preferences.getString(
                        trainPlanList.getId() + "CacheTrainInfo_info", "");
                if (Trainlist_info != null && !"".equals(Trainlist_info)) {
                    TrainInfo trainInfo = null;
                    try {
                        JSONObject jsonObject = new JSONObject(Trainlist_info)
                                .getJSONObject("data");
                        if (jsonObject != null && !"".equals(jsonObject)) {

                            trainInfo = new TrainInfo();
                            if (jsonObject.has("id")) {
                                trainInfo.setId(jsonObject.getInt("id"));
                            }
                            if (jsonObject.has("train_name")) {
                                trainInfo.setTrain_name(jsonObject
                                        .getString("train_name"));
                            }
                            if (jsonObject.has("thumb")) {
                                trainInfo.setThumb(jsonObject
                                        .getString("thumb"));
                            }
                            if (jsonObject.has("grade")) {
                                trainInfo.setGrade(jsonObject.getInt("grade"));
                            }
                            if (jsonObject.has("position")) {
                                trainInfo.setPosition(jsonObject
                                        .getString("position"));
                            }
                            if (jsonObject.has("train_time")) {
                                trainInfo.setTrain_time(jsonObject
                                        .getInt("train_time"));
                            }
                            if (jsonObject.has("train_calorie")) {
                                trainInfo.setTrain_calorie(jsonObject
                                        .getDouble("train_calorie"));
                            }
                            if (jsonObject.has("train_fileurl")) {
                                trainInfo.setTrain_fileurl(jsonObject
                                        .getString("train_fileurl"));
                            }
                            if (jsonObject.has("action_num")) {
                                trainInfo.setAction_num(jsonObject
                                        .getInt("action_num"));
                            }
                            if (jsonObject.has("traincount")) {
                                trainInfo.setTraincount(jsonObject
                                        .getInt("traincount"));
                            }
                            if (jsonObject.has("userlist")) {

                                String str = jsonObject.getString("userlist");
                                if (str != null && !"null".equals(str)) {
                                    JSONArray jsonArray = jsonObject
                                            .getJSONArray("userlist");
                                    if (jsonArray != null
                                            && jsonArray.length() > 0) {
                                        ArrayList<TrainUserInfo> tUserList = new ArrayList<TrainUserInfo>();
                                        TrainUserInfo trainUserInfo = null;
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject obj = jsonArray
                                                    .getJSONObject(i);
                                            trainUserInfo = new TrainUserInfo();
                                            if (obj.has("name")) {
                                                trainUserInfo.setName(obj
                                                        .getString("name"));
                                            }
                                            if (obj.has("img")) {
                                                trainUserInfo.setImg(obj
                                                        .getString("img"));
                                            }
                                            if (obj.has("id")) {
                                                trainUserInfo.setId(obj
                                                        .getInt("id"));
                                            }
                                            if (obj.has("uid")) {
                                                trainUserInfo.setUid(obj
                                                        .getInt("uid"));
                                            }
                                            if (obj.has("train_starttime")) {
                                                trainUserInfo
                                                        .setTrain_starttime(obj
                                                                .getString("train_starttime"));
                                            }
                                            if (obj.has("train_endtime")) {
                                                trainUserInfo
                                                        .setTrain_endtime(obj
                                                                .getString("train_endtime"));
                                            }
                                            if (obj.has("train_calorie")) {
                                                trainUserInfo
                                                        .setTrain_calorie(obj
                                                                .getDouble("train_calorie"));
                                            }
                                            tUserList.add(trainUserInfo);
                                        }
                                        trainInfo.settUserList(tUserList);
                                    }
                                }

                            }
                            if (jsonObject.has("actionlist")) {
                                JSONArray jsonArray = jsonObject
                                        .getJSONArray("actionlist");
                                if (jsonArray != null && jsonArray.length() > 0) {
                                    ArrayList<TrainAction> actionlist = new ArrayList<TrainAction>();
                                    TrainAction trainAction;
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject obj = jsonArray
                                                .getJSONObject(i);
                                        trainAction = new TrainAction();
                                        if (obj.has("id")) {
                                            trainAction.setId(obj.getInt("id"));
                                        }
                                        if (obj.has("action_name")) {
                                            trainAction.setName(obj
                                                    .getString("action_name"));
                                        }
                                        if (obj.has("thumb")) {
                                            trainAction.setThumb(obj
                                                    .getString("thumb"));
                                        }
                                        if (obj.has("listorder")) {
                                            trainAction.setListorder(obj
                                                    .getInt("listorder"));
                                        }
                                        if (obj.has("resttime")) {
                                            trainAction.setResttime(obj
                                                    .getInt("resttime"));
                                        }
                                        if (obj.has("actnum")) {
                                            trainAction.setActnum(obj
                                                    .getInt("actnum"));
                                        }
                                        if (obj.has("videotime")) {
                                            trainAction.setVideotime(obj
                                                    .getInt("videotime"));
                                        }
                                        actionlist.add(trainAction);
                                    }

                                    trainInfo.setActionlist(actionlist);
                                }

                            }
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        // return trainInfo;
                    }
                    if (trainInfo != null) {
                        mTrainInfo = trainInfo;
                        if (mTrainInfo != null) {
                            mDownloader.download(mTrainInfo.getThumb(),
                                    train_planimg, null);
                            time_train.setText("耗时"
                                    + mTrainInfo.getTrain_time() + "分钟");
                            if (mTrainInfo.getActionlist() != null) {
                                setGridView();
                                num_train.setText("共"
                                        + mTrainInfo.getActionlist().size()
                                        + "组健身");
                            }
                            if (mTrainInfo.gettUserList() != null) {
                                if (mTrainInfo.gettUserList().size() == 0) {
                                    trainicon_gridlayout
                                            .setVisibility(View.GONE);
                                    tarinuser_infolist.setVisibility(View.GONE);
                                    no_trainuser_txt
                                            .setVisibility(View.VISIBLE);
                                } else {
                                    trainicon_gridlayout
                                            .setVisibility(View.VISIBLE);
                                    tarinuser_infolist
                                            .setVisibility(View.VISIBLE);
                                    no_trainuser_txt.setVisibility(View.GONE);
                                    if(mTrainInfo.getTraincount()>=10000){
                                        //如果人数大于一万就显示万人
                                        double c=mTrainInfo.getTraincount()*1.0/10000;
                                        String ss = "第"+ SportTaskUtil.getDoubleOneNum(c)
                                                + "万人";

                                        SpannableString styledText = new SpannableString(
                                                ss);
                                        styledText.setSpan(new TextAppearanceSpan(
                                                        TrainPlanMainActivity.this,
                                                        R.style.train_playnum_style1), 0, 1,
                                                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                        styledText.setSpan(new TextAppearanceSpan(
                                                        TrainPlanMainActivity.this,
                                                        R.style.train_playnum_style), 1, ss
                                                        .length() - 2,
                                                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                        styledText.setSpan(new TextAppearanceSpan(
                                                        TrainPlanMainActivity.this,
                                                        R.style.train_playnum_style1), ss
                                                        .length() - 2, ss.length(),
                                                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                                        train_userNum.setText(styledText,
                                                TextView.BufferType.SPANNABLE);

                                    }else {
                                        String ss = "第"+mTrainInfo.getTraincount()
                                                + "人";

                                        SpannableString styledText = new SpannableString(
                                                ss);
                                        styledText.setSpan(new TextAppearanceSpan(
                                                        TrainPlanMainActivity.this,
                                                        R.style.train_playnum_style1), 0, 1,
                                                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                        styledText.setSpan(new TextAppearanceSpan(
                                                        TrainPlanMainActivity.this,
                                                        R.style.train_playnum_style), 1, ss
                                                        .length() - 1,
                                                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                        styledText.setSpan(new TextAppearanceSpan(
                                                        TrainPlanMainActivity.this,
                                                        R.style.train_playnum_style1), ss
                                                        .length() - 1, ss.length(),
                                                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                                        train_userNum.setText(styledText,
                                                TextView.BufferType.SPANNABLE);

                                    }
                                }
                                trainUserGridAdapter = new TrainUserGridAdapter(
                                        TrainPlanMainActivity.this,
                                        mTrainInfo.gettUserList());
                                train_usergrid.setAdapter(trainUserGridAdapter);
                                trainUserListAdapter = new TrainUserListAdapter(
                                        TrainPlanMainActivity.this,
                                        mTrainInfo.gettUserList());
                                tarinuser_infolist
                                        .setAdapter(trainUserListAdapter);
                            } else {
                                trainicon_gridlayout.setVisibility(View.GONE);
                                tarinuser_infolist.setVisibility(View.GONE);
                                no_trainuser_txt.setVisibility(View.VISIBLE);
                            }
                        }
                    }

                }
            }

        }

        play_detailinfo = (TextView) findViewById(R.id.play_detailinfo);
        play_detailinfo.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if(trainPlanList!=null&&mApp!=null){
                    Intent intent = new Intent(TrainPlanMainActivity.this,
                            Train_webViewClass.class);
                    intent.putExtra(
                            "webUrl",
                            ApiConstant.DATA_URL
                                    + "m=Train&a=train_details&trainid="
                                    + trainPlanList.getId() + "&sessionId="
                                    + mApp.getSessionId());
                    startActivity(intent);
                }

            }
        });

    }

    @Override
    public void setViewStatus() {
        // TODO Auto-generated method stub

    }

    @Override
    public void onPageResume() {
        // TODO Auto-generated method stub
        if(mApp==null){
            mApp = (SportsApp) getApplication();
        }

    }

    @Override
    public void onPagePause() {
        // TODO Auto-generated method stub

    }

    @Override
    public void onPageDestroy() {
        // TODO Auto-generated method stub
        mApp = null;
    }

    class LoadThread extends Thread {
        @Override
        public void run() {
            // TODO Auto-generated method stub
            super.run();
            TrainInfo trainInfo = null;
            try {
                trainInfo = ApiJsonParser.getTraininfo(mApp.getSessionId(),
                        trainPlanList.getId(), TrainPlanMainActivity.this);
            } catch (ApiNetException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (ApiSessionOutException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            Message message = new Message();
            message.what = FRESH_LIST;
            message.obj = trainInfo;
            mHandler.sendMessage(message);
        }
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {

            switch (msg.what) {
                case FRESH_LIST:
                    mTrainInfo = (TrainInfo) msg.obj;
                    if (mTrainInfo != null) {
                        mDownloader.download(mTrainInfo.getThumb(), train_planimg,
                                null);
                        time_train
                                .setText("耗时" + mTrainInfo.getTrain_time() + "分钟");
                        if (mTrainInfo.getActionlist() != null) {
                            setGridView();
                            num_train.setText("共"
                                    + mTrainInfo.getActionlist().size() + "组健身");
                        }
                        if (mTrainInfo.gettUserList() != null) {
                            if (mTrainInfo.gettUserList().size() == 0) {
                                trainicon_gridlayout.setVisibility(View.GONE);
                                tarinuser_infolist.setVisibility(View.GONE);
                                no_trainuser_txt.setVisibility(View.VISIBLE);
                            } else {
                                trainicon_gridlayout.setVisibility(View.VISIBLE);
                                tarinuser_infolist.setVisibility(View.VISIBLE);
                                no_trainuser_txt.setVisibility(View.GONE);
                                if(mTrainInfo.getTraincount()>=10000){
                                    //如果人数大于一万就显示万人
                                    double c=mTrainInfo.getTraincount()*1.0/10000;
                                    String ss = "第"+ SportTaskUtil.getDoubleOneNum(c)
                                            + "万人";

                                    SpannableString styledText = new SpannableString(
                                            ss);
                                    styledText.setSpan(new TextAppearanceSpan(
                                                    TrainPlanMainActivity.this,
                                                    R.style.train_playnum_style1), 0, 1,
                                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                    styledText.setSpan(new TextAppearanceSpan(
                                                    TrainPlanMainActivity.this,
                                                    R.style.train_playnum_style), 1, ss
                                                    .length() - 2,
                                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                    styledText.setSpan(new TextAppearanceSpan(
                                                    TrainPlanMainActivity.this,
                                                    R.style.train_playnum_style1), ss
                                                    .length() - 2, ss.length(),
                                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                                    train_userNum.setText(styledText,
                                            TextView.BufferType.SPANNABLE);

                                }else {
                                    String ss = "第"+mTrainInfo.getTraincount()
                                            + "人";

                                    SpannableString styledText = new SpannableString(
                                            ss);
                                    styledText.setSpan(new TextAppearanceSpan(
                                                    TrainPlanMainActivity.this,
                                                    R.style.train_playnum_style1), 0, 1,
                                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                    styledText.setSpan(new TextAppearanceSpan(
                                                    TrainPlanMainActivity.this,
                                                    R.style.train_playnum_style), 1, ss
                                                    .length() - 1,
                                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                    styledText.setSpan(new TextAppearanceSpan(
                                                    TrainPlanMainActivity.this,
                                                    R.style.train_playnum_style1), ss
                                                    .length() - 1, ss.length(),
                                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                                    train_userNum.setText(styledText,
                                            TextView.BufferType.SPANNABLE);

                                }
                            }
                            trainUserGridAdapter = new TrainUserGridAdapter(
                                    TrainPlanMainActivity.this,
                                    mTrainInfo.gettUserList());
                            train_usergrid.setAdapter(trainUserGridAdapter);
                            trainUserListAdapter = new TrainUserListAdapter(
                                    TrainPlanMainActivity.this,
                                    mTrainInfo.gettUserList());
                            tarinuser_infolist.setAdapter(trainUserListAdapter);
                        } else {
                            trainicon_gridlayout.setVisibility(View.GONE);
                            tarinuser_infolist.setVisibility(View.GONE);
                            no_trainuser_txt.setVisibility(View.VISIBLE);
                        }
                    } else {

                    }
                    break;

                default:
                    break;
            }
        }

        ;
    };

    /**
     * GirdView 数据适配器
     */
    public class GridViewAdapter extends BaseAdapter {
        Context context;
        ArrayList<TrainAction> actionlist;

        public GridViewAdapter(Context _context,
                               ArrayList<TrainAction> actionlist) {
            this.context = _context;
            this.actionlist = actionlist;
        }

        @Override
        public int getCount() {
            return actionlist.size();
        }

        @Override
        public Object getItem(int position) {
            return actionlist.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
            if (convertView == null) {
                LayoutInflater layoutInflater = LayoutInflater.from(context);
                convertView = layoutInflater.inflate(
                        R.layout.activity_index_gallery_item, null);
                viewHolder = new ViewHolder();
                viewHolder.mImg = (ImageView) convertView
                        .findViewById(R.id.id_index_gallery_item_image);
                viewHolder.traincount = (TextView) convertView
                        .findViewById(R.id.traincount);
                viewHolder.trainplan_time = (TextView) convertView
                        .findViewById(R.id.trainplan_time);
                viewHolder.train_playname = (TextView) convertView
                        .findViewById(R.id.train_playname);
                viewHolder.id_index_gallery_item_sort = (TextView) convertView
                        .findViewById(R.id.id_index_gallery_item_sort);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            mDownloader.download(actionlist.get(position).getThumb(),
                    viewHolder.mImg, null);
            viewHolder.train_playname.setText(actionlist.get(position)
                    .getName());
            viewHolder.traincount.setText(actionlist.get(position).getActnum()
                    + "次" + " X " + actionlist.get(position).getVideotime()
                    + "s");
            viewHolder.trainplan_time.setText("休息"
                    + actionlist.get(position).getResttime() + "s");
            viewHolder.id_index_gallery_item_sort.setText(actionlist.get(position).getListorder()+"");
            return convertView;
        }

        private class ViewHolder {
            ImageView mImg;
            TextView traincount, trainplan_time, train_playname,id_index_gallery_item_sort;
        }

    }

    /**
     * 训练过的用户GirdView 数据适配器
     */
    public class TrainUserGridAdapter extends BaseAdapter {
        Context context;
        ArrayList<TrainUserInfo> userlist;

        public TrainUserGridAdapter(Context _context,
                                    ArrayList<TrainUserInfo> userlist) {
            this.context = _context;
            this.userlist = userlist;
        }

        @Override
        public int getCount() {
            if (userlist.size() > 7) {
                return 7;
            }
            return userlist.size();
        }

        @Override
        public Object getItem(int position) {
            return userlist.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
            if (convertView == null) {
                LayoutInflater layoutInflater = LayoutInflater.from(context);
                convertView = layoutInflater.inflate(
                        R.layout.activity_index_traingride_item, null);
                viewHolder = new ViewHolder();
                viewHolder.mImg = (RoundedImage) convertView
                        .findViewById(R.id.roundimage_icon);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            if (userlist.get(position).getImg() != null
                    && !"".equals(userlist.get(position).getImg())) {
                mDownloader.download(userlist.get(position).getImg(),
                        viewHolder.mImg, null);
            } else {
                viewHolder.mImg
                        .setImageResource(R.drawable.sports_user_edit_portrait_male);
            }

            return convertView;
        }

        private class ViewHolder {
            RoundedImage mImg;
        }

    }

    /**
     * 训练过的用户ListView 数据适配器
     */
    public class TrainUserListAdapter extends BaseAdapter {
        Context context;
        ArrayList<TrainUserInfo> userlist;

        public TrainUserListAdapter(Context _context,
                                    ArrayList<TrainUserInfo> userlist) {
            this.context = _context;
            this.userlist = userlist;
        }

        @Override
        public int getCount() {
            return userlist.size();
        }

        @Override
        public Object getItem(int position) {
            return userlist.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
            if (convertView == null) {
                LayoutInflater layoutInflater = LayoutInflater.from(context);
                convertView = layoutInflater.inflate(
                        R.layout.activity_index_trainlist_item, null);
                viewHolder = new ViewHolder();
                viewHolder.mImg = (RoundedImage) convertView
                        .findViewById(R.id.roundimage_icon);
                viewHolder.user_name = (TextView) convertView
                        .findViewById(R.id.user_name);
                viewHolder.complete_time = (TextView) convertView
                        .findViewById(R.id.complete_time);
                viewHolder.complete_cal = (TextView) convertView
                        .findViewById(R.id.complete_cal);
                viewHolder.submit_content = (TextView) convertView
                        .findViewById(R.id.submit_content);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            if (userlist.get(position).getImg() != null
                    && !"".equals(userlist.get(position).getImg())) {
                mDownloader.download(userlist.get(position).getImg(),
                        viewHolder.mImg, null);
            } else {
                viewHolder.mImg
                        .setImageResource(R.drawable.sports_user_edit_portrait_male);
            }
            viewHolder.user_name.setText(userlist.get(position).getName());
            // 这是判断什么时候发的
            SimpleDateFormat format = new SimpleDateFormat(
                    "yyyy-MM-dd HH:mm:ss");
            Date date = null;
            if (userlist.get(position).getTrain_endtime() != null) {
                try {
                    date = format.parse(userlist.get(position)
                            .getTrain_endtime());
                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                long time = System.currentTimeMillis() - date.getTime();
                if (time <= 60 * 1000)
                    // 一分钟内显示刚刚
                    viewHolder.complete_time.setText(getResources().getString(
                            R.string.sports_time_justnow));
                else if (time <= 60 * 60 * 1000) {
                    int h = (int) (time / 1000 / 60);
                    // 一小时内显示多少分钟前
                    viewHolder.complete_time.setText(""
                            + h
                            + getResources().getString(
                            R.string.sports_time_mins_ago));
                } else {
                    viewHolder.complete_time
                            .setText(FindOtherMoreAdapter.formatDisplayTime(
                                    userlist.get(position).getTrain_endtime(),
                                    "yyyy-MM-dd HH:mm:ss"));

                }
            } else {
                viewHolder.complete_time.setText("");
            }

            viewHolder.complete_cal.setText("消耗热量"
                    + (int) userlist.get(position).getTrain_calorie() + "kcal");
            return convertView;
        }

        private class ViewHolder {
            RoundedImage mImg;
            TextView user_name, complete_time, complete_cal, submit_content;
        }

    }

    /**
     * 设置GirdView参数，绑定数据
     */
    private void setGridView() {
        int size = mTrainInfo.getActionlist().size();
        int length = SportsApp.dip2px(120);
        // DisplayMetrics dm = new DisplayMetrics();
        // getWindowManager().getDefaultDisplay().getMetrics(dm);
        // float density = dm.density;
        // int gridviewWidth = (int) (size * (length + 4) * density);
        // int itemWidth = (int) (length * density);
        int gridviewWidth = size * (length + 10) + 30;
        int itemWidth = length;

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                gridviewWidth, LinearLayout.LayoutParams.FILL_PARENT);
        mGridView.setLayoutParams(params); // 设置GirdView布局参数,横向布局的关键
        mGridView.setColumnWidth(itemWidth); // 设置列表项宽
        mGridView.setHorizontalSpacing(10); // 设置列表项水平间距
        mGridView.setStretchMode(GridView.NO_STRETCH);
        mGridView.setNumColumns(size); // 设置列数量=列表集合数

        GridViewAdapter adapter = new GridViewAdapter(getApplicationContext(),
                mTrainInfo.getActionlist());
        mGridView.setAdapter(adapter);
    }

    @Override
    public void onClick(View view) {
        // TODO Auto-generated method stub
        switch (view.getId()) {
            case R.id.btn_cancel_download:
                ib_download_cancel.setVisibility(View.GONE);

                LayoutInflater inflater = (LayoutInflater) TrainPlanMainActivity.this
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                dialog = new Dialog(TrainPlanMainActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                View layout = inflater.inflate(R.layout.dialog_download_cancel,
                        null);
                WindowManager wm = (WindowManager)getSystemService(
                        Context.WINDOW_SERVICE);

                int width = wm.getDefaultDisplay().getWidth();
                dialog.addContentView(layout, new LayoutParams(
                        width-100, LayoutParams.WRAP_CONTENT));
                ((TextView) layout.findViewById(R.id.bt_ok))
                        .setOnClickListener(new OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                // TODO Auto-generated method stub
                                ib_download_cancel.setVisibility(View.VISIBLE);

                                dialog.cancel();
                                dialog = null;
                            }
                        });
                ((TextView) layout.findViewById(R.id.bt_cancel))
                        .setOnClickListener(new OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                // TODO Auto-generated method stub
                                // 表示取消下载可以点击返回按钮
                                isDownLoad = false;

                                startplay_probar.setProgress(0, "", "");
                                startplay_probar.setVisibility(View.GONE);
                                ib_download_cancel.setVisibility(View.GONE);
                                startplay_btn.setVisibility(View.VISIBLE);
                                play_detailinfo.setVisibility(View.VISIBLE);

                                l.cancelDownload();

                                if (dialog != null) {
                                    dialog.cancel();
                                    dialog = null;
                                }
                            }
                        });
                dialog.setCanceledOnTouchOutside(false);
                dialog.setCancelable(false);
                dialog.show();

                break;
            case R.id.startplay_btn:
                if (mTrainInfo != null) {
                    url = mTrainInfo.getTrain_fileurl();
                    downLoadPath = "video/";
                    fileName = url
                            .substring(url.lastIndexOf("/") + 1, url.length())
                            .trim();

                    File f = new File(dir + downLoadPath + fileName);
                    int zipStatus = -1;
                    if (f.exists()) {
                        try {
                            if (fileName.contains(".zip")) {
                                zipStatus = upZipFile(
                                        dir + downLoadPath + fileName,
                                        dir
                                                + downLoadPath
                                                + fileName.substring(0,
                                                fileName.length() - 4));
                            }
                        } catch (FileNotFoundException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        } catch (ZipException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        } finally {
                            if (zipStatus == -1) {
                                Log.e(TAG, "file exists delete : " + f.delete());
                                Toast.makeText(TrainPlanMainActivity.this,
                                        "文件内容解析错误，请重新下载", Toast.LENGTH_SHORT)
                                        .show();
                                return;
                            }
                        }

                        Intent intent = new Intent(TrainPlanMainActivity.this,
                                VideoViewMainActivity.class);
                        intent.putExtra(
                                "videoDir",
                                dir
                                        + downLoadPath
                                        + fileName.substring(0,
                                        fileName.length() - 4));
                        intent.putExtra("train_id", trainPlanList.getId());
                        intent.putExtra("train_name", trainPlanList.getTrain_name());
                        startActivity(intent);
                    } else {
                        if (mSportsApp.isOpenNetwork()) {
                            // 表示正在下载
                            if (!isDownLoad) {
                                isDownLoad = true;
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        // 另起线程执行下载，安卓最新sdk规范，网络操作不能再主线程。
                                        l = new FileDownload(url);
                                        max = l.getLength();

                                        Log.e("develop_debug", "max : " + max);

                                        if (max <= 0) {
                                            Message msg = downloadHandler
                                                    .obtainMessage();
                                            msg.what = 0;
                                            msg.sendToTarget();
                                            return;
                                        }

                                        Message msg = downloadHandler
                                                .obtainMessage();
                                        msg.what = 1;
                                        msg.sendToTarget();

                                        /**
                                         * 下载文件到sd卡，虚拟设备必须要开始设置sd卡容量
                                         * downhandler是Download的内部类，作为回调接口实时显示下载数据
                                         */
                                        Log.e("develop_debug", "downLoadPath : "
                                                + downLoadPath + ", fileName : "
                                                + fileName);
                                        l.down2sd(downLoadPath, fileName,
                                                l.new downhandler() {
                                                    @Override
                                                    public void setSize(int size) {
                                                        Message msg = downloadHandler
                                                                .obtainMessage();
                                                        msg.what = 2;
                                                        msg.arg1 = size;
                                                        msg.sendToTarget();
                                                    }
                                                });
                                    }
                                }).start();
                            } else {
                                Toast.makeText(TrainPlanMainActivity.this,
                                        "正在下载视频", Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            Toast.makeText(TrainPlanMainActivity.this, getString(R.string.sports_get_list_failed2),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    Toast.makeText(TrainPlanMainActivity.this, "正在加载数据请稍后。。。", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            case LEFTBTLAYOUT:
            case LEFTBTID:
                if (isDownLoad) {
                    Toast.makeText(TrainPlanMainActivity.this, "正在下载视频", Toast.LENGTH_SHORT).show();
                } else {
                    finish();
                }

                break;

            default:
                break;
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK: {
                if (isDownLoad) {
                    Toast.makeText(TrainPlanMainActivity.this, "正在下载视频", Toast.LENGTH_SHORT).show();
                    return false;
                } else {
                    finish();
                }
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    Handler downloadHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case 0:
                    isDownLoad = false;
                    Toast.makeText(TrainPlanMainActivity.this, "网络错误，请重试",
                            Toast.LENGTH_SHORT).show();
                    break;
                case 1:
                    startplay_probar.setProgress(0, "", "");
                    startplay_probar.setMax(l.getLength());

                    startplay_probar.setVisibility(View.VISIBLE);
                    ib_download_cancel.setVisibility(View.VISIBLE);
                    startplay_btn.setVisibility(View.GONE);
                    play_detailinfo.setVisibility(View.GONE);
                    break;
                case 2:
                    int pro = startplay_probar.getProgress() + msg.arg1;
                    startplay_probar.setProgress(pro, "", "");

                    if (pro >= startplay_probar.getMax()) {
                        // 表示下载已完成
                        isDownLoad = false;

                        if (dialog != null) {
                            dialog.cancel();
                            dialog = null;
                        }

                        startplay_probar.setProgress(0, "", "");
                        startplay_probar.setVisibility(View.GONE);
                        ib_download_cancel.setVisibility(View.GONE);
                        startplay_btn.setVisibility(View.VISIBLE);
                        play_detailinfo.setVisibility(View.VISIBLE);

                        int zipStatus = -1;

                        try {
                            if (fileName.contains(".zip")) {
                                zipStatus = upZipFile(
                                        dir + downLoadPath + fileName.trim(),
                                        dir
                                                + downLoadPath
                                                + fileName.substring(0,
                                                fileName.length() - 4));
                            }
                        } catch (FileNotFoundException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        } catch (ZipException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        } finally {
                            if (zipStatus == -1) {
                                File f = new File(dir + downLoadPath
                                        + fileName.trim(), dir
                                        + downLoadPath
                                        + fileName.substring(0,
                                        fileName.length() - 4));
                                if(f.exists()){
                                    Log.e(TAG, "download finish delete : " + f.delete());
                                }
                                Toast.makeText(TrainPlanMainActivity.this,
                                        "文件内容解析错误，请重新下载", Toast.LENGTH_SHORT)
                                        .show();
                                return;
                            }else{
                                //解析完删除压缩包
//                                File file=new File(dir + downLoadPath
//                                        + fileName.trim());
//                                if(file.exists()){
//                                    file.delete();
//                                }
                            }
                        }

                        Intent intent = new Intent(TrainPlanMainActivity.this,
                                VideoViewMainActivity.class);
                        intent.putExtra(
                                "videoDir",
                                dir
                                        + downLoadPath
                                        + fileName.substring(0,
                                        fileName.length() - 4));
                        intent.putExtra("train_id", trainPlanList.getId());
                        intent.putExtra("train_name", trainPlanList.getTrain_name());
                        startActivity(intent);
                    }
                    break;
            }
        }
    };

    public int upZipFile(String zipFile, String folderPath)
            throws ZipException, IOException {
        Log.e("develop_debug", "zipFile : " + zipFile);
        Log.e("develop_debug", "folderPath : " + folderPath);
        File upZipDir = new File(folderPath);
        if (!upZipDir.exists()) {
            upZipDir.mkdirs();
        }

        ZipFile zfile = new ZipFile(zipFile);
        Enumeration zList = zfile.entries();
        ZipEntry ze = null;
        byte[] buf = new byte[1024];
        while (zList.hasMoreElements()) {
            ze = (ZipEntry) zList.nextElement();
            if (ze.isDirectory()) {
                String dirstr = folderPath + ze.getName();
                dirstr = new String(dirstr.getBytes("8859_1"), "GB2312");
                File f = new File(dirstr);
                f.mkdir();
                continue;
            }

            OutputStream os = new BufferedOutputStream(new FileOutputStream(
                    getRealFileName(folderPath, ze.getName())));
            InputStream is = new BufferedInputStream(zfile.getInputStream(ze));
            int readLen = 0;
            while ((readLen = is.read(buf, 0, 1024)) != -1) {
                os.write(buf, 0, readLen);
            }
            is.close();
            os.close();
        }
        zfile.close();
        return 1;
    }

    public static File getRealFileName(String baseDir, String absFileName) {
        String[] dirs = absFileName.split("/");
        String lastDir = baseDir;

        if (dirs.length > 1) {
            for (int i = 0; i < dirs.length - 1; i++) {
                lastDir += (dirs[i] + "/");
                File dir = new File(lastDir);

                if (!dir.exists()) {
                    dir.mkdirs();
                }
            }
            File ret = new File(lastDir, dirs[dirs.length - 1]);
            return ret;
        } else {
            return new File(baseDir, absFileName);
        }
    }

    class URLSpanNoUnderline extends URLSpan {

        public URLSpanNoUnderline(String url) {
            super(url);
            // TODO Auto-generated constructor stub
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            super.updateDrawState(ds);
            ds.setUnderlineText(false);
            ds.setColor(Color.WHITE);
        }

    }
}
