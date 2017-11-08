package com.fox.exercise;

import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.GridLayout.LayoutParams;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MultiAutoCompleteTextView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fox.exercise.api.AddCoinsThread;
import com.fox.exercise.api.ApiConstant;
import com.fox.exercise.api.ApiJsonParser;
import com.fox.exercise.api.ApiNetException;
import com.fox.exercise.api.ApiSessionOutException;
import com.fox.exercise.api.entity.ActionList;
import com.fox.exercise.api.entity.AddFindItem;
import com.fox.exercise.newversion.entity.CircleFindsCat;
import com.fox.exercise.newversion.entity.TopicContent;
import com.fox.exercise.publish.Bimp;
import com.fox.exercise.publish.BitmapCache;
import com.fox.exercise.publish.FileUtils;
import com.fox.exercise.publish.PhotoActivity;
import com.fox.exercise.publish.SendMsgDetail;
import com.fox.exercise.publish.TestPicActivity;
import com.fox.exercise.util.SportTaskUtil;
import com.fox.exercise.wxapi.WXEntryActivity;
import com.tencent.connect.share.QQShare;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.umeng.analytics.MobclickAgent;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.ingenic.indroidsync.SportsApp;

public class FindFriendsSendMsg extends AbstractBaseActivity implements
        OnClickListener, OnDismissListener, IUiListener {
    private List<OnlineAction> actionLists = new ArrayList<OnlineAction>();
    private List<ActionList> actionLists_all = new ArrayList<ActionList>();
    private List<String> activityNameList = new ArrayList<String>();
    private List<Integer> activitySendId = new ArrayList<Integer>();
    private List<Integer> activityIdList = new ArrayList<Integer>();
    private List<String> title_action = new ArrayList<String>();
    private StringBuilder sb = new StringBuilder();
    private StringBuilder sb5 = new StringBuilder();
    private String sb_1 = null;
    private String sb_5 = null;

    private SportsApp mSportsApp;
    private Activity mContext;

    private GridView gridview_send_image;
    private MultiAutoCompleteTextView et_send_mean;
    private TextView tv_city_send;

    private SharedPreferences spf;
    private LinearLayout myView;
    private PopupWindow myWindow;
    private RelativeLayout pop_menu_background;
    // private RelativeLayout rl_findfriends;
    private LinearLayout ll_auto;

    // 上传图片地址
    private ArrayList<String> list_bitmap_path_upload = new ArrayList<String>();
    // 说说+地名
    private String method_str, city_name;

    private GridAdapter adapter;

    private int width, height;
    private GridView mGridView;

    private final int FRESH_LIST = 111;// 更新成功
    private final int FRESH_FAILED = 112;// 更新失败
    private final int FRESH_NULL = 114;
    private List<CircleFindsCat> lCats = new ArrayList<CircleFindsCat>();
    private MyAdapter myAdapter;

    private Map<Integer, String> topiccatMap = new HashMap<Integer, String>();

    private TextView message;

    // private LinearLayout send_share_layout;
    private RadioButton qq_share_checkbox, weixin_share_checkbox,
            weixinfriends_share_checkbox;
    // private String title=null;
    private static final String URL = ApiConstant.DATA_URL
            + "m=sports&a=FindRecordHtml&id=";
    private Tencent mTencent;
    private IWXAPI api;
    private ImageDownloader mDownloader = null;
    private AddFindItem back;
//    String[] result_about_a = {"andexplorer", "astro"};

    private Handler pHandler = new Handler();
    private String title_name;
    private boolean isUpLoad = false;// 表示正在发表动态
//    public static String name;
    Intent mIntent;

    @Override
    public void initIntentParam(Intent intent) {
        if (intent != null) {
            title_name = intent.getStringExtra("title_name");
        }
    }

    @Override
    public void initView() {
        new GetActionDataTask().execute();
        left_ayout.removeAllViews();
        TextView tv_cancle = new TextView(this);
        tv_cancle.setText(getResources().getString(R.string.button_cancel));
        tv_cancle.setTextColor(getResources().getColor(
                R.color.sports_popular_title_normal));
        tv_cancle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        left_ayout.addView(tv_cancle);
        left_ayout.setOnClickListener(this);
        TextView tv_send = new TextView(this);
        tv_send.setText(getResources().getString(R.string.button_send));
        tv_send.setTextColor(getResources().getColor(
                R.color.sports_popular_title_normal));
        tv_send.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        showRightBtn(tv_send);

        showContentView(R.layout.findfriends_sendmsg);

        WindowManager wm = getWindowManager();
        width = wm.getDefaultDisplay().getWidth();
        width = width / 2;
        height = wm.getDefaultDisplay().getHeight();
        mGridView = (GridView) findViewById(R.id.huati_gridview);
        myAdapter = new MyAdapter(this);
        mGridView.setAdapter(myAdapter);

        init();
        if (Bimp.drr.size() == 0
                && (title_name == null || "".equals(title_name))) {
            pHandler.post(r);
        }
        mTencent = Tencent.createInstance("1101732794", mContext);
        mDownloader = new ImageDownloader(mContext);
        new GetActionDataTask2().execute();
        new GetActionDataTask3().execute();
    }

    Runnable r = new Runnable() {

        @Override
        public void run() {
            if (null != FindFriendsSendMsg.this.getWindow().getDecorView()
                    .getWindowToken()) {
                shotSelectImages();
                pHandler.removeCallbacks(this);
            } else {
                pHandler.postDelayed(this, 5);
            }
        }
    };

    private class GetActionDataTask extends
            AsyncTask<Void, Void, List<OnlineAction>> {

        @Override
        protected List<OnlineAction> doInBackground(Void... sessionid) {

            List<OnlineAction> actionLists = null;
            try {
                actionLists = ApiJsonParser
                        .getOnlineActionList(((SportsApp) getApplication()).getSessionId());
            } catch (ApiNetException e) {
                e.printStackTrace();
            } catch (ApiSessionOutException e) {
                e.printStackTrace();
            }
            return actionLists;
        }

        @Override
        protected void onPostExecute(List<OnlineAction> result) {
            super.onPostExecute(result);
            // waitCloset();
            if (result == null)
                return;
            if (actionLists!=null&&title_action!=null&&result.size() > 0) {
                for (OnlineAction actionList : result) {
                    actionLists.add(actionList);
                    title_action.add("#" + actionList.getTitle() + "# ");
                }
            }
        }

    }

    // 下载活动列表
    private class GetActionDataTask3 extends
            AsyncTask<Void, Void, List<ActionList>> {

        @Override
        protected List<ActionList> doInBackground(Void... sessionid) {

            List<ActionList> actionLists_all = null;
            try {
                if(mSportsApp!=null){
                    actionLists_all = ApiJsonParser
                            .getNewActionList(
                                    mSportsApp.getSessionId(),
                                    "z"
                                            + getResources().getString(
                                            R.string.config_game_id), 0);
                }
            } catch (ApiNetException e) {
                e.printStackTrace();
            } catch (ApiSessionOutException e) {
                e.printStackTrace();
            }
            return actionLists_all;
        }

        @Override
        protected void onPostExecute(List<ActionList> result) {
            super.onPostExecute(result);
            // waitCloset();
            if (result == null)
                return;
            if (actionLists_all!=null&&result.size() > 0) {
                for (ActionList actionList : result) {
                    actionLists_all.add(actionList);
                }
            }
        }

    }

    private class GetActionDataTask2 extends
            AsyncTask<Void, Void, List<ActionList>> {

        @Override
        protected List<ActionList> doInBackground(Void... sessionid) {

            List<ActionList> actionLists_all = null;
            try {
                if(mSportsApp!=null){
                    actionLists_all = ApiJsonParser
                            .getNewActionList(
                                    mSportsApp.getSessionId(),
                                    "z"
                                            + getResources().getString(
                                            R.string.config_game_id), 1);
                }

            } catch (ApiNetException e) {
                e.printStackTrace();
            } catch (ApiSessionOutException e) {
                e.printStackTrace();
            }
            return actionLists_all;
        }

        @Override
        protected void onPostExecute(List<ActionList> result) {
            super.onPostExecute(result);
            // waitCloset();
            if (result == null)
                return;
            if (actionLists_all!=null&&result.size() > 0) {
                for (ActionList actionList : result) {
                    actionLists_all.add(actionList);
                }
            }
        }

    }

    @Override
    public void setViewStatus() {

    }

    @Override
    public void onPageResume() {
        MobclickAgent.onPageStart("FindFriendsSendMsg");
    }

    @Override
    public void onPagePause() {
        MobclickAgent.onPageEnd("FindFriendsSendMsg");
    }

    @Override
    public void onPageDestroy() {
        mHandler.removeCallbacksAndMessages(null);
        Bimp.bmp.clear();
        Bimp.drr.clear();
        if(list_bitmap_path_upload!=null){
            list_bitmap_path_upload.clear();
            list_bitmap_path_upload=null;
        }
        Bimp.max = 0;
        if (gridview_send_image != null)
            gridview_send_image.setAdapter(null);
        adapter = null;
        topiccatMap = null;
        myWindow = null;
        isUpLoad = false;
        if(actionLists!=null){
            actionLists.clear();
            actionLists=null;
        }
        if(actionLists_all!=null){
            actionLists_all.clear();
            actionLists_all=null;
        }
        if(activityNameList!=null){
            activityNameList.clear();
            activityNameList=null;
        }
        if(activitySendId!=null){
            activitySendId.clear();
            activitySendId=null;
        }
        if(activityIdList!=null){
            activityIdList.clear();
            activityIdList=null;
        }
        if(title_action!=null){
            title_action.clear();
            title_action=null;
        }
        if(lCats!=null){
            lCats.clear();
            lCats=null;
        }
        sb =null;
        sb5=null;
    }

    private void init() {
        mSportsApp = (SportsApp) getApplication();
        mContext = this;
        gridview_send_image = (GridView) findViewById(R.id.gridview_send_image);
        adapter = new GridAdapter(this);
        adapter.update();
        gridview_send_image.setAdapter(adapter);
        gridview_send_image.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                if (arg2 == Bimp.bmp.size()) {
                    // 隐藏输入框
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm.isActive() && getCurrentFocus() != null) {
                        if (getCurrentFocus().getWindowToken() != null) {
                            imm.hideSoftInputFromWindow(getCurrentFocus()
                                            .getWindowToken(),
                                    InputMethodManager.HIDE_NOT_ALWAYS);
                        }
                    }
                    shotSelectImages();
                } else {
                    Intent intent = new Intent(FindFriendsSendMsg.this,
                            PhotoActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("ID", arg2);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        });
        // rl_findfriends = (RelativeLayout) findViewById(R.id.rl_findfriends);
        et_send_mean = (MultiAutoCompleteTextView) findViewById(R.id.et_send_mean);
        tv_city_send = (TextView) findViewById(R.id.tv_city_send);
        spf = getSharedPreferences("sports", 0);
        pop_menu_background = (RelativeLayout) findViewById(R.id.send_menu_background);
        right_btn.setOnClickListener(this);
        ll_auto = (LinearLayout) findViewById(R.id.ll_auto);
        city_name = spf.getString("cityname", "");
        tv_city_send.setText(city_name);
        ll_auto.setOnClickListener(new DingWeiLister());
        // send_share_layout = (LinearLayout)
        // findViewById(R.id.send_share_layout);
        qq_share_checkbox = (RadioButton) findViewById(R.id.qq_share_checkbox);
        weixin_share_checkbox = (RadioButton) findViewById(R.id.weixin_share_checkbox);
        weixinfriends_share_checkbox = (RadioButton) findViewById(R.id.weixinfriends_share_checkbox);
        if (title_action != null) {
            ArrayAdapter<String> adapt = new ArrayAdapter<String>(this,
                    R.layout.auto_item2, R.id.contentTextView, title_action);
            et_send_mean.setAdapter(adapt);
            et_send_mean.setTokenizer(new SemicolonTokenizer(' '));
        }
        if (title_name != null) {
            et_send_mean.setText("#" + title_name + "# ");
            et_send_mean.setSelection(title_name.length() + 3);
        }
        et_send_mean.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                // TODO Auto-generated method stub
                if (s.toString().length() > 0) {
                    // rl_findfriends.setBackgroundColor(Color.parseColor("#DCDCDC"));
                } else {
                    // rl_findfriends.setBackgroundColor(Color.parseColor("#D8D8BF"));
                    // rl_findfriends.setBackgroundColor(R.drawable.sports_bg);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

            }
        });
    }

    // 定位单击事件
    class DingWeiLister implements OnClickListener {

        @Override
        public void onClick(View v) {
            if (!"".equals(city_name) && city_name != null) {
                // TODO Auto-generated method stub
                Intent mIntent = new Intent(FindFriendsSendMsg.this,
                        DingWeiList.class);
                mIntent.putExtra("city", city_name);
                startActivityForResult(mIntent, 11);
            } else {
                Toast.makeText(getApplication(), "当前城市暂无", Toast.LENGTH_SHORT).show();
            }
        }

    }

    @Override
    public void onClick(View view) {
        if (view == left_ayout) {
            // 隐藏输入框
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm.isActive() && getCurrentFocus() != null) {
                if (getCurrentFocus().getWindowToken() != null) {
                    imm.hideSoftInputFromWindow(getCurrentFocus()
                                    .getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
            if (isUpLoad) {
                Toast.makeText(mContext,
                        getResources().getString(R.string.uploading), Toast.LENGTH_SHORT)
                        .show();
                return;
            } else {
                finish();
            }

        }
        // 发送说说和图片
        if (view == right_btn) {
            method_str = et_send_mean.getText().toString();
            if (method_str.length() == 0 || method_str == null) {
                method_str = " ";
            }
            // if (method_str.contains("#")) {
            // int a = method_str.lastIndexOf("#");
            // int b = method_str.indexOf("#");
            // if (a!=b) {
            // String str0 = method_str.substring(0,b + 1);
            // String str1 = method_str.substring(b+1, a+1);
            // String str2 = method_str.substring(a + 1, method_str.length());
            // method_str = str0+ " " +str1 + " " + str2;
            // }else {
            // String str1 = method_str.substring(0, a + 1);
            // String str2 = method_str.substring(a + 1, method_str.length());
            // method_str = str1 + " " + str2;
            // }
            // }
            if (method_str.contains("#")) {
                int a = method_str.lastIndexOf("#");
                String str1 = method_str.substring(0, a + 1);
                String str2 = method_str.substring(a + 1, method_str.length());
                method_str = str1 + " " + str2;
            }

            if ((method_str == null || "".equals(method_str) || " "
                    .equals(method_str)) && (Bimp.drr.size() == 0)) {
                Toast.makeText(FindFriendsSendMsg.this,
                        getResources().getString(R.string.sendshuoshuo1),
                        Toast.LENGTH_SHORT).show();
                return;
            }
            right_btn.setEnabled(false);
            // et_send_mean.setSelection(et_send_mean.getText().toString().length()+1);
            // 通过对输入的文字的截取进行和活动名字对比判断是否正在参加活动。
            if (method_str != null && !"".equals(method_str)) {
                boolean flog = method_str.contains(" ");
                boolean flog2 = method_str.contains("#");
                int aa = 0, bb = 0;

                // if (flog2) {
                // aa = method_str.indexOf("#");
                // bb = method_str.lastIndexOf("#");
                // }
                // if (aa == bb) {
                // flog3 = true;
                // } else {
                // flog3 = false;
                // }

                if (flog && flog2) {
                    String likeUsers = method_str.substring(0,
                            method_str.lastIndexOf("#")).toString();
                    String listRemove = method_str.replace(likeUsers, "");
                    String[] ary = likeUsers.split("#");
                    for (int i = 0; i < ary.length; i++) {
                        int a = getActivityId(ary[i]);
                        if (a != -1) {
                            activitySendId.add(a);
                        }
                    }
                    if (activitySendId != null && !"".equals(activitySendId)) {
                        for (int i = 0; i < activitySendId.size(); i++) {
                            sb.append("," + activitySendId.get(i));
                            sb5.append(",#"
                                    + activityNameList.get(activityIdList
                                    .indexOf(activitySendId.get(i)))
                                    + "#");
                            sb_1 = sb.toString();
                            sb_5 = sb5.toString();
                        }
                    }
                }
            }

            city_name = spf.getString("cityname", "");
            if (city_name != null && !"".equals(city_name)) {
                if (tv_city_send.getText().toString().trim() != null
                        && !"".equals(tv_city_send.getText().toString().trim())) {
                    if ("不显示位置"
                            .equals(tv_city_send.getText().toString().trim())) {
                        city_name = "";
                    } else {
                        city_name = tv_city_send.getText().toString().trim();
                    }

                } else {
                    city_name = "";
                }
            } else {
                city_name = "";
            }
            final StringBuffer topiccat = new StringBuffer();
            int add = 0;
            if (!topiccatMap.isEmpty()) {
                for (String v : topiccatMap.values()) {
                    System.out.println("value= " + v);
                    if (add == 0) {
                        topiccat.append(v);
                    } else {
                        topiccat.append("," + v);
                    }
                    add++;
                }
            }
            for (int i = 0; i < Bimp.drr.size(); i++) {
                String Str = Bimp.drr.get(i).substring(
                        Bimp.drr.get(i).lastIndexOf("/") + 1,
                        Bimp.drr.get(i).lastIndexOf("."));
                list_bitmap_path_upload.add(FileUtils.SDPATH + Str + ".JPEG");
            }
            // 从活动页面跳转过来的标示，不为空表示是从活动页面过来
            if (title_name != null) {
                if (mSportsApp!=null&&mSportsApp.isOpenNetwork()) {
                    showDialog(getResources().getString(
                            R.string.bestgirl_wait_upload));
                    isUpLoad = true;
                    new Thread(new Runnable() {

                        @Override
                        public void run() {
                            try {
                                if (sb_1 != null && !"".equals(sb_1)) {
                                    String sb_2 = sb_1.substring(1,
                                            sb_1.length());
                                    String sb_6 = sb_5.substring(1,
                                            sb_5.length());
                                    AddFindItem back = ApiJsonParser.addFind(
                                            mSportsApp.getSessionId(),
                                            method_str, city_name,
                                            list_bitmap_path_upload,
                                            topiccat.toString(), "", sb_2, sb_6);
                                    Message msg = new Message();
                                    msg.what = 20141112;
                                    msg.obj = back;
                                    mHandler.sendMessage(msg);
                                } else {
                                    AddFindItem back = ApiJsonParser.addFind(
                                            mSportsApp.getSessionId(),
                                            method_str, city_name,
                                            list_bitmap_path_upload,
                                            topiccat.toString(), "", "", "");
                                    Message msg = new Message();
                                    msg.what = 20141112;
                                    msg.obj = back;
                                    mHandler.sendMessage(msg);
                                }
                            } catch (ApiNetException e) {
                                Message msg = new Message();
                                msg.what = 20141113;
                                msg.obj = e.exceMsg();
                                mHandler.sendMessage(msg);
                            }
                        }
                    }).start();
                } else {
                    right_btn.setEnabled(true);
                    Toast.makeText(
                            FindFriendsSendMsg.this,
                            getResources().getString(
                                    R.string.error_cannot_access_net),
                            Toast.LENGTH_SHORT).show();
                }

                // deleteFolderFile(Environment.getExternalStorageDirectory()
                // + "/myimage/", true);
                // File file = new
                // File(Environment.getExternalStorageDirectory()
                // + "/myimage/");
                // if (file.exists() && file.isFile()) {
                // file.delete();
                // }
            } else {
                if (list_bitmap_path_upload.size() == 0) {
                    // Toast.makeText(FindFriendsSendMsg.this,
                    // getResources().getString(R.string.sendshuoshuo),
                    // Toast.LENGTH_SHORT).show();
                    // right_btn.setEnabled(true);
                    // 添加的代码 就是在没有图片的时候也能发表运动秀

                    if (mSportsApp!=null&&mSportsApp.isOpenNetwork()) {
                        showDialog(getResources().getString(
                                R.string.bestgirl_wait_upload));
                        isUpLoad = true;
                        new Thread(new Runnable() {

                            @Override
                            public void run() {
                                try {
                                    if (method_str == null) {
                                        method_str = " ";
                                    }
                                    if (sb_1 != null && !"".equals(sb_1)) {
                                        String sb_2 = sb_1.substring(1,
                                                sb_1.length());
                                        String sb_6 = sb_5.substring(1,
                                                sb_5.length());
                                        AddFindItem back = ApiJsonParser.addFind(
                                                mSportsApp.getSessionId(),
                                                method_str, city_name,
                                                list_bitmap_path_upload,
                                                topiccat.toString(), "", sb_2,
                                                sb_6);
                                        Message msg = new Message();
                                        msg.what = 20141112;
                                        msg.obj = back;
                                        mHandler.sendMessage(msg);
                                    } else {
                                        AddFindItem back = ApiJsonParser.addFind(
                                                mSportsApp.getSessionId(),
                                                method_str, city_name,
                                                list_bitmap_path_upload,
                                                topiccat.toString(), "", "", "");
                                        Message msg = new Message();
                                        msg.what = 20141112;
                                        msg.obj = back;
                                        mHandler.sendMessage(msg);
                                    }
                                } catch (ApiNetException e) {
                                    Message msg = new Message();
                                    msg.what = 20141113;
                                    msg.obj = e.exceMsg();
                                    mHandler.sendMessage(msg);
                                }
                            }
                        }).start();
                    } else {
                        right_btn.setEnabled(true);
                        Toast.makeText(
                                FindFriendsSendMsg.this,
                                getResources().getString(
                                        R.string.error_cannot_access_net),
                                Toast.LENGTH_SHORT).show();
                    }

                    // deleteFolderFile(Environment.getExternalStorageDirectory()
                    // + "/myimage/", true);
                    // File file = new
                    // File(Environment.getExternalStorageDirectory()
                    // + "/myimage/");
                    // if (file.exists() && file.isFile()) {
                    // file.delete();
                    // }

                } else {
                    if (mSportsApp!=null&&mSportsApp.isOpenNetwork()) {
                        showDialog(getResources().getString(
                                R.string.bestgirl_wait_upload));
                        new Thread(new Runnable() {

                            @Override
                            public void run() {
                                try {
                                    if (sb_1 != null && !"".equals(sb_1)) {
                                        String sb_2 = sb_1.substring(1,
                                                sb_1.length());
                                        String sb_6 = sb_5.substring(1,
                                                sb_5.length());
                                        AddFindItem back = ApiJsonParser.addFind(
                                                mSportsApp.getSessionId(),
                                                method_str, city_name,
                                                list_bitmap_path_upload,
                                                topiccat.toString(), "", sb_2,
                                                sb_6);
                                        Message msg = new Message();
                                        msg.what = 20141112;
                                        msg.obj = back;
                                        mHandler.sendMessage(msg);
                                    } else {
                                        AddFindItem back = ApiJsonParser.addFind(
                                                mSportsApp.getSessionId(),
                                                method_str, city_name,
                                                list_bitmap_path_upload,
                                                topiccat.toString(), "", "", "");
                                        Message msg = new Message();
                                        msg.what = 20141112;
                                        msg.obj = back;
                                        mHandler.sendMessage(msg);
                                    }
                                } catch (ApiNetException e) {
                                    Message msg = new Message();
                                    msg.what = 20141113;
                                    msg.obj = e.exceMsg();
                                    mHandler.sendMessage(msg);
                                }
                            }
                        }).start();
                    } else {
                        right_btn.setEnabled(true);
                        Toast.makeText(
                                FindFriendsSendMsg.this,
                                getResources().getString(
                                        R.string.error_cannot_access_net),
                                Toast.LENGTH_SHORT).show();
                    }

                    // deleteFolderFile(Environment.getExternalStorageDirectory()
                    // + "/myimage/", true);
                    // File file = new
                    // File(Environment.getExternalStorageDirectory()
                    // + "/myimage/");
                    // if (file.exists() && file.isFile()) {
                    // file.delete();
                    // }
                }
            }

        }

        switch (view.getId()) {
            case R.id.btn_paizhao:
                photo();
                pop_menu_background.setVisibility(View.GONE);
                myWindow.dismiss();
                break;
            case R.id.btn_select_pic:
                String status = Environment.getExternalStorageState();
                if (status.equals(Environment.MEDIA_MOUNTED)) {
                    Intent intentFromGallery = new Intent(FindFriendsSendMsg.this,
                            TestPicActivity.class);
                    startActivity(intentFromGallery);
                }
                pop_menu_background.setVisibility(View.GONE);
                myWindow.dismiss();
                break;
            case R.id.btn_cancle:
                pop_menu_background.setVisibility(View.GONE);
                myWindow.dismiss();
                break;
        }
    }

    public void shotSelectImages() {
        LayoutInflater inflater = LayoutInflater.from(this);
        myView = (LinearLayout) inflater.inflate(
                R.layout.select_images_from_local1, null);

        myView.findViewById(R.id.btn_paizhao).setOnClickListener(this);
        myView.findViewById(R.id.btn_select_pic).setOnClickListener(this);
        // myView.findViewById(R.id.btn_cancle).setOnClickListener(this);

        WindowManager wm = getWindowManager();
        int width = wm.getDefaultDisplay().getWidth();
        myWindow = new PopupWindow(myView, width - SportsApp.dip2px(40),
                RelativeLayout.LayoutParams.WRAP_CONTENT, true);
        // myWindow.setAnimationStyle(R.style.AnimationPopup);
        myWindow.setOutsideTouchable(true);
        myWindow.setBackgroundDrawable(new BitmapDrawable());
        myWindow.showAtLocation(left_ayout, Gravity.CENTER, 0, 0);
        myWindow.setOnDismissListener(this);
        // final Animation animation = (Animation) AnimationUtils.loadAnimation(
        // this, R.anim.slide_in_from_bottom);
        // myView.startAnimation(animation);
        pop_menu_background.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDismiss() {
        pop_menu_background.setVisibility(View.GONE);
    }

//    FileOutputStream sssssb;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // 结果码不等于取消时候
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case TAKE_PICTURE:
                    // if (Bimp.drr.size() < 9 && resultCode == -1) {
                    // Bimp.drr.add(path);
                    // Log.i("path", "path--path--path-->" + path);
                    // }
                    if (Bimp.drr.size() < 4 && resultCode == -1) {
                        Bimp.drr.add(path);
                    }
                    break;
            }
        }
        if ((requestCode == 11103) || (requestCode == 10103)) {
            if (null != mTencent) {
                mTencent.onActivityResultData(requestCode, resultCode, data, this);
            }
        }
        if (requestCode == 11) {
            tv_city_send.setText(data.getStringExtra("address"));
        }
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 20141112:
                    isUpLoad = false;
                    back = (AddFindItem) msg.obj;
                    if (mLoadProgressDialog != null) {
                        mLoadProgressDialog.dismiss();
                    }
                    if (back != null) {
                        if (back.flag == 0
                                && (back.findId != null && !"".equals(back.findId))) {
                            right_btn.setEnabled(true);
                            SendMsgDetail mSendMsgDetail = new SendMsgDetail();
                            mSendMsgDetail.setMethod_str(method_str);
                            mSendMsgDetail.setTimes(System.currentTimeMillis());
                            mSendMsgDetail.setFindId(back.findId);
                            mSendMsgDetail.setUrls(back.urls);
                            mSendMsgDetail.setBigurls(back.bigurls);
                            if (back.urls != null) {
                                if (back.urls.length == 1) {
                                    mSendMsgDetail.setWidth(back.width);
                                    mSendMsgDetail.setHeight(back.height);
                                }

                            }
                            if (!topiccatMap.isEmpty()) {
                                ArrayList<TopicContent> topicList = new ArrayList<TopicContent>();
                                TopicContent topicContent = null;
                                for (String k : topiccatMap.values()) {
                                    topicContent = new TopicContent();
                                    topicContent.setId(k);
                                    for (int i = 0; i < lCats.size(); i++) {
                                        if (k.equals(lCats.get(i).getId() + "")) {
                                            topicContent.setTitle(lCats.get(i)
                                                    .getTitle());
                                        }
                                    }
                                    topicList.add(topicContent);
                                }
                                mSendMsgDetail.setTopicList(topicList);
                            }

                            mSendMsgDetail.setComeFrom(tv_city_send.getText()
                                    .toString());
                            // if (mSportsApp.getFindHandler() != null) {
                            if(mSportsApp!=null){
                                mSportsApp.setmSendMsgDetail(mSendMsgDetail);
                                mSportsApp.setDongtai_personalceter(1);
                            }
                            // }

                            // FileUtils.deleteDir();
                            Toast.makeText(
                                    FindFriendsSendMsg.this,
                                    getResources()
                                            .getString(R.string.sendchenggong),
                                    Toast.LENGTH_SHORT).show();
                            Bimp.bmp.clear();
                            Bimp.drr.clear();
                            list_bitmap_path_upload.clear();
                            Bimp.max = 0;
                            // finish();
                            Looper.getMainLooper();
                            ToShare(back);
                        } else if (back.flag == -56){
                            Toast.makeText(FindFriendsSendMsg.this,
                                    getResources().getString(R.string.sports_get_list_failed2),
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            if (back.flag == -1) {
                                Toast.makeText(FindFriendsSendMsg.this,
                                        getResources().getString(R.string.sendshibai),
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    } else {
                            Toast.makeText(FindFriendsSendMsg.this,
                                    getResources().getString(R.string.sendshibai),
                                    Toast.LENGTH_SHORT).show();
                    }

                    break;

                case FRESH_LIST:
                    lCats = (List<CircleFindsCat>) msg.obj;
                    myAdapter.notifyDataSetChanged();
                    if (mLoadProgressDialog != null)
                        if (mLoadProgressDialog.isShowing())
                            mLoadProgressDialog.dismiss();
                    break;

                case FRESH_FAILED:
                    if (mSportsApp!=null&&!mSportsApp.isOpenNetwork()) {
                        Toast.makeText(
                                FindFriendsSendMsg.this,
                                getResources().getString(
                                        R.string.acess_server_error),
                                Toast.LENGTH_SHORT).show();
                    } else if (FindFriendsSendMsg.this != null) {
                        Toast.makeText(
                                FindFriendsSendMsg.this,
                                getResources().getString(
                                        R.string.sports_get_list_failed),
                                Toast.LENGTH_SHORT).show();
                    }
                    if (mLoadProgressDialog != null)
                        if (mLoadProgressDialog.isShowing())
                            mLoadProgressDialog.dismiss();
                    // sportsfindmoreAdapter.notifyDataSetChanged();
                    break;
                case FRESH_NULL:
                    if (FindFriendsSendMsg.this != null) {
                        Toast.makeText(
                                FindFriendsSendMsg.this,
                                getResources().getString(
                                        R.string.sports_data_load_more_null),
                                Toast.LENGTH_SHORT).show();
                    }
                    if (mLoadProgressDialog != null)
                        if (mLoadProgressDialog.isShowing())
                            mLoadProgressDialog.dismiss();
                    break;
                case 20141113:
                    isUpLoad = false;
                    if (mLoadProgressDialog != null) {
                        if (mLoadProgressDialog.isShowing()) {
                            mLoadProgressDialog.dismiss();
                        }
                    }
                    Toast.makeText(mContext, R.string.sports_get_list_failed2, Toast.LENGTH_SHORT).show();
                    break;
            }
            super.handleMessage(msg);
        }
    };

    Dialog mLoadProgressDialog;

    private void showDialog(String messages) {
        if (mLoadProgressDialog == null) {
            mLoadProgressDialog = new Dialog(FindFriendsSendMsg.this,
                    R.style.sports_dialog);
            LayoutInflater mInflater = getLayoutInflater();
            View v1 = mInflater.inflate(R.layout.bestgirl_progressdialog, null);
            message = (TextView) v1.findViewById(R.id.message);
            message.setText(messages);
            v1.setMinimumWidth((int) (SportsApp.ScreenWidth * 0.8));
            mLoadProgressDialog.setContentView(v1);
            mLoadProgressDialog.setCanceledOnTouchOutside(false);
        }
        message.setText(messages);
        mLoadProgressDialog.show();
    }

    public class GridAdapter extends BaseAdapter {
        private LayoutInflater inflater; // 视图容器
        private int selectedPosition = -1;// 选中的位置
        private boolean shape;

        public boolean isShape() {
            return shape;
        }

        public void setShape(boolean shape) {
            this.shape = shape;
        }

        public GridAdapter(Context context) {
            inflater = LayoutInflater.from(context);
        }

        public void update() {
            loading();
        }

        public int getCount() {
            // if (Bimp.bmp.size() >= 9) {
            // return Bimp.bmp.size();
            // }
            if (Bimp.bmp.size() >= 4) {
                return Bimp.bmp.size();
            }
            return (Bimp.bmp.size() + 1);
        }

        public Object getItem(int arg0) {

            return null;
        }

        public long getItemId(int arg0) {

            return 0;
        }

        public void setSelectedPosition(int position) {
            selectedPosition = position;
        }

        public int getSelectedPosition() {
            return selectedPosition;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            final int coord = position;
            ViewHolder holder = null;
            if (convertView == null) {

                convertView = inflater.inflate(R.layout.sendmsg_image, parent,
                        false);
                holder = new ViewHolder();
                holder.image = (ImageView) convertView
                        .findViewById(R.id.image_run_circle);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            if (position == Bimp.bmp.size()) {
                holder.image.setImageBitmap(BitmapFactory.decodeResource(
                        getResources(), R.drawable.addimages));
                // if (position == 9) {
                // holder.image.setVisibility(View.GONE);
                // }
            } else {
                holder.image.setImageBitmap(Bimp.bmp.get(position));
            }

            return convertView;
        }

        public class ViewHolder {
            public ImageView image;
        }

        Handler handler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 1:
                        if (adapter != null)
                            adapter.notifyDataSetChanged();
                        break;
                }
                super.handleMessage(msg);
            }
        };

        public void loading() {
            new Thread(new Runnable() {
                public void run() {
                    while (true) {
                        if (Bimp.max == Bimp.drr.size()) {
                            Message message = new Message();
                            message.what = 1;
                            handler.sendMessage(message);
                            break;
                        } else {
                            try {
                                String path = Bimp.drr.get(Bimp.max);
                                System.out.println(path);
                                Bitmap bm = Bimp.revitionImageSize(path);
                                int degree = BitmapCache.getBitmapDegree(path);
                                if (0 != degree) {
                                    bm = BitmapCache.rotateBitmapByDegree(bm,
                                            degree);
                                }
                                Bimp.bmp.add(bm);
                                String newStr = path.substring(
                                        path.lastIndexOf("/") + 1,
                                        path.lastIndexOf("."));
                                FileUtils.saveBitmap(bm, "" + newStr);
                                Bimp.max += 1;
                                Message message = new Message();
                                message.what = 1;
                                handler.sendMessage(message);
                            } catch (IOException e) {

                                e.printStackTrace();
                            }
                        }
                    }
                }
            }).start();
        }
    }

    @Override
    protected void onRestart() {
        if (adapter != null)
            adapter.update();
        super.onRestart();
    }

    private static final int TAKE_PICTURE = 0x000000;
    private String path = "";

    public void photo() {
        String status = Environment.getExternalStorageState();
        if (status.equals(Environment.MEDIA_MOUNTED)) {
            try {
                File file_sys = new File(
                        Environment.getExternalStorageDirectory() + "/myimage/");
                if (!file_sys.exists()) {
                    file_sys.mkdirs();
                }
                File file = new File(file_sys, String.valueOf(System
                        .currentTimeMillis()) + ".jpg");
                path = file.getPath();
                Uri imageUri = Uri.fromFile(file);
                Intent openCameraIntent = new Intent(
                        MediaStore.ACTION_IMAGE_CAPTURE);
                openCameraIntent.putExtra(MediaStore.Images.Media.ORIENTATION,
                        0);
                openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(openCameraIntent, TAKE_PICTURE);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(FindFriendsSendMsg.this,
                        getResources().getString(R.string.save_mulu),
                        Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(FindFriendsSendMsg.this,
                    getResources().getString(R.string.save_sd),
                    Toast.LENGTH_LONG).show();
        }

    }

    // 删除文件和目录
    // private void deleteFolderFile(String filePath, boolean deleteThisPath) {
    // if (!TextUtils.isEmpty(filePath)) {
    // try {
    // File file = new File(filePath);
    // if (file.isDirectory()) {
    // // 处理目录
    // File files[] = file.listFiles();
    // for (int i = 0; i < files.length; i++) {
    // deleteFolderFile(files[i].getAbsolutePath(), true);
    // }
    // }
    // if (deleteThisPath) {
    // if (!file.isDirectory()) {
    // // 如果是文件，删除
    // file.delete();
    // } else {
    // // 目录
    // if (file.listFiles().length == 0) {
    // // 目录下没有文件或者目录，删除
    // file.delete();
    // }
    // }
    // }
    // } catch (Exception e) {
    // // TODO Auto-generated catch block
    // e.printStackTrace();
    // }
    // }
    // }

    // 自定义适配器
    class MyAdapter extends BaseAdapter {
        // 上下文对象
        private Context context;

        MyAdapter(Context context) {
            this.context = context;
        }

        public int getCount() {
            return lCats.size();
        }

        public Object getItem(int item) {
            return lCats.get(item);
        }

        public long getItemId(int id) {
            return id;
        }

        // 创建View方法
        public View getView(int position, View convertView, ViewGroup parent) {
            CheckBox checkBox;
            if (convertView == null) {
                checkBox = new CheckBox(context);
                checkBox.setLayoutParams(new GridView.LayoutParams(width - 45,
                        LayoutParams.WRAP_CONTENT));// 设置ImageView对象布局
                checkBox.setBackgroundResource(R.drawable.biaoqian_checkbox_selector);
                checkBox.setGravity(Gravity.CENTER);
                checkBox.setPadding(0, 12, 0, 12);// 设置间距
                checkBox.setButtonDrawable(R.drawable.biaoqian_checkbox_selector);
                checkBox.setTextColor(getResources().getColor(R.color.black));
            } else {
                checkBox = (CheckBox) convertView;
            }
            checkBox.setText(lCats.get(position).getTitle());
            checkBox.setOnCheckedChangeListener(new MyCheckedChangeListener(
                    position, lCats.get(position).getId() + ""));
            return checkBox;
        }
    }

    class MyCheckedChangeListener implements OnCheckedChangeListener {
        int position;
        String content;

        public MyCheckedChangeListener(int position, String content) {
            this.position = position;
            this.content = content;
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView,
                                     boolean isChecked) {
            // TODO Auto-generated method stub
            if (isChecked) {
                topiccatMap.put(position, content);
            } else {
                if (topiccatMap.containsKey(position)) {
                    topiccatMap.remove(position);
                }
            }
        }

    }

    class SportsFindMoreThread extends Thread {
        @Override
        public void run() {
            // TODO Auto-generated method stub
            super.run();
            Message msg = null;
            List<CircleFindsCat> lCats = null;

            try {
                if(mSportsApp!=null){
                    lCats = ApiJsonParser.getBiaoQianLists(mSportsApp
                                    .getSessionId(), 0, mSportsApp.getSportUser().getUid(), 2);
                }
            } catch (ApiNetException e) {
                e.printStackTrace();
            } catch (ApiSessionOutException e) {
                e.printStackTrace();
            }
            if (lCats != null) {
                if (lCats.size() == 0) {
                    msg = Message.obtain(mHandler, FRESH_NULL);
                    msg.sendToTarget();
                } else {
                    msg = Message.obtain(mHandler, FRESH_LIST);
                    msg.obj = lCats;
                    msg.sendToTarget();
                }
            } else {
                msg = Message.obtain(mHandler, FRESH_FAILED);
                msg.sendToTarget();
            }
        }

    }

    private void onClickQQShare(AddFindItem back) {
        final Bundle params = new Bundle();
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE,
                QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
        // 自己
        params.putString(QQShare.SHARE_TO_QQ_TITLE,
                "这是我的运动秀 #云狐运动# 帅哥、美女最多的运动APP");// 要分享的标题
        params.putString(QQShare.SHARE_TO_QQ_SUMMARY, et_send_mean.getText()
                .toString());// 要分享的摘要
        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, URL + back.findId);
        if (back.urls != null) {
            params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, back.urls[0]);// 要分享的图片
        }
        params.putString(QQShare.SHARE_TO_QQ_APP_NAME, "1101732794");
        params.putInt(QQShare.SHARE_TO_QQ_EXT_INT, 0);
        mTencent.shareToQQ(mContext, params, this);
    }

    // 分享到微信里边的内容，其中flag 0是朋友圈，1是好友
    private void shareToWeixin(int flag, AddFindItem back) {
        if (api == null) {
            api = WXAPIFactory.createWXAPI(mContext, WXEntryActivity.APP_ID,
                    true);
            api.registerApp(WXEntryActivity.APP_ID);
        }
        if (!api.isWXAppInstalled()) {
            Toast.makeText(getApplicationContext(),
                    getString(R.string.sports_shareto_weixin_no_weixin),
                    Toast.LENGTH_SHORT).show();
            return;
        } else if (api.getWXAppSupportAPI() < 0x21020001) {
            Toast.makeText(getApplicationContext(),
                    getString(R.string.sports_shareto_weixin_wrong_version),
                    Toast.LENGTH_SHORT).show();
            return;
        }
        share2weixin(flag, back);
        // new saveDataToServer().execute();
    }

    private void share2weixin(int flag, AddFindItem back) {
        if (api == null) {
            api = WXAPIFactory.createWXAPI(mContext, WXEntryActivity.APP_ID,
                    true);
            api.registerApp(WXEntryActivity.APP_ID);
        }

        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = URL + back.findId;
        WXMediaMessage msg = new WXMediaMessage(webpage);
        // 自己
        msg.title = "这是我的运动秀 #云狐运动# 帅哥、美女最多的运动APP";// title
        msg.description = et_send_mean.getText().toString();
        // Bitmap thumb = getBitmap(findGroup.getImgs()[0]);
        // BitmapDrawable draw = (BitmapDrawable) getResources().getDrawable(
        // R.drawable.indexpage_sport_step_icon);
        // Bitmap m = draw.getBitmap();
        if (back.urls != null) {
            msg.setThumbImage(mDownloader.downloadBitmap(back.urls[0]));
        }

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = String.valueOf(System.currentTimeMillis());
        req.message = msg;
        req.scene = flag;
        api.sendReq(req);
    }

    @Override
    public void onCancel() {
        // TODO Auto-generated method stub

    }

    @Override
    public void onComplete(Object arg0) {
        // TODO Auto-generated method stub
        finish();
        Toast.makeText(this, "QQ分享成功", Toast.LENGTH_SHORT).show();
        new AddCoinsThread(10, 4, new Handler() {
            @Override
            public void handleMessage(Message msg) {
                // TODO Auto-generated method
                // stub
                switch (msg.what) {
                    case ApiConstant.COINS_SUCCESS:
                        SportTaskUtil.jump2CoinsDialog(FindFriendsSendMsg.this,
                                getString(R.string.shared_success_add_coins));
                        break;
                    case ApiConstant.COINS_LIMIT:
                        Toast.makeText(getApplicationContext(),
                                getString(R.string.shared_beyond_10times),
                                Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
            }
        }, -1).start();
    }

    @Override
    public void onError(UiError arg0) {
        // TODO Auto-generated method stub

    }

    // 显示分享图标
    private void ToShare(final AddFindItem back) {
        if (!qq_share_checkbox.isChecked()
                && !weixin_share_checkbox.isChecked()
                && !weixinfriends_share_checkbox.isChecked()) {
            finish();

        } else {
            if (qq_share_checkbox.isChecked()) {
                onClickQQShare(back);
            }
            if (weixin_share_checkbox.isChecked()) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        shareToWeixin(0, back);
                    }
                }).start();
                finish();
            }
            if (weixinfriends_share_checkbox.isChecked()) {
                new Thread(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        shareToWeixin(1, back);
                    }
                }).start();
                finish();
            }
        }

    }

    // 通过传入一个活动的名字得到活动的ID。若传入的活动名字错误、返回-1；
    public int getActivityId(String activityName) {
        if (activityName != null && !" ".equals(activityName)) {
            if (actionLists_all != null) {
                for (int i = 0; i < actionLists_all.size(); i++) {
                    activityNameList.add(actionLists_all.get(i).getTitle());
                    activityIdList.add(actionLists_all.get(i).getActionId());
                }
                boolean flog = activityNameList.contains(activityName);
                if (flog) {
                    int index = activityNameList.indexOf(activityName);
                    int activityId = activityIdList.get(index);
                    return activityId;
                } else {
                    return -1;
                }
            }
            return -1;
        }
        return -1;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK: {
                if (isUpLoad) {
                    Toast.makeText(mContext,
                            getResources().getString(R.string.uploading), Toast.LENGTH_SHORT)
                            .show();
                    return true;
                }
            }
        }
        return super.onKeyDown(keyCode, event);
    }

}
