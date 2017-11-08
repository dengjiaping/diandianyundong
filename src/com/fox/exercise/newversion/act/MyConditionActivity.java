package com.fox.exercise.newversion.act;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.fox.exercise.AbstractBaseActivity;
import com.fox.exercise.FansAndNear;
import com.fox.exercise.FindOtherFragment;
import com.fox.exercise.FindOtherMoreAdapter;
import com.fox.exercise.FindOtherMoreAdapter.DelItem;
import com.fox.exercise.HistoryAllActivity;
import com.fox.exercise.MainFragmentActivity;
import com.fox.exercise.R;
import com.fox.exercise.SportsExceptionHandler;
import com.fox.exercise.SportsPersonalMsg;
import com.fox.exercise.api.ApiBack;
import com.fox.exercise.api.ApiJsonParser;
import com.fox.exercise.api.ApiNetException;
import com.fox.exercise.api.ApiSessionOutException;
import com.fox.exercise.api.entity.ActionList;
import com.fox.exercise.api.entity.ExpressionItems;
import com.fox.exercise.api.entity.SportTask;
import com.fox.exercise.api.entity.UserDetail;
import com.fox.exercise.api.entity.UserPrimsgAll;
import com.fox.exercise.api.entity.UserPrimsgOne;
import com.fox.exercise.bitmap.util.ImageResizer;
import com.fox.exercise.db.SportSubTaskDB;
import com.fox.exercise.db.SportsContent;
import com.fox.exercise.login.LoginActivity;
import com.fox.exercise.login.UserEditActivity;
import com.fox.exercise.newversion.act.FindFriendsFragment.SportsFindMoreHandler;
import com.fox.exercise.newversion.entity.FindGroup;
import com.fox.exercise.pedometer.ImageWorkManager;
import com.fox.exercise.util.RoundedImage;
import com.fox.exercise.util.ScrollLayout;
import com.fox.exercise.util.SportTaskUtil;
import com.fox.exercise.view.PullToRefreshBase.OnRefreshListener;
import com.fox.exercise.view.PullToRefreshListView;
import com.umeng.analytics.MobclickAgent;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.ingenic.indroidsync.SportsApp;

/**
 * Created by zhonghuibin on 2016/8/1.个人主页我的动态主页面
 */
public class MyConditionActivity extends AbstractBaseActivity implements
        OnClickListener, DelItem {

    private boolean isFromInvite;
    private boolean isLoginer = false;
    private int mUid;
    public static boolean mIn = false;
    private SportsApp mSportsApp;
    private Dialog loadProgressDialog;
    private TextView mDialogMessage;
    private PullToRefreshListView mPullSearchListView = null;
    private ListView mListView = null;
    private RoundedImage zhuye_image_icon1;
    private TextView sports_find_othername1, user_money, guanzhu_numbers,
            fensi_nums, personal_address, personal_sport_days,
            personal_sport_nums;
    private ImageView list_user_guanzhu, list_user_sixin_imagview,
            list_user_yuepao_imagview;
    private TextView getxing_qianming;
    private int times = 0;
    private ArrayList<FindGroup> mList = new ArrayList<FindGroup>();
    private FindOtherMoreAdapter sportsfindmoreAdapter = null;
    public PersonalFindHandler mPersonalFindHandler = null;

    private final int FRESH_LIST = 111;// 更新成功
    private final int FRESH_FAILED = 112;// 更新失败
    private final int FRESH_DONE = 113;
    private final int FRESH_NULL = 114;
    public static final int FRESH_VIEW = 115;
    public static final int USEREDITFRESH_VIEW = 116;
    private final static int INIT_PORTRAIT = 1;
    private static final int LEFTBTID = 14;
    private static final int LEFTBTLAYOUT = 16;
    public static final int FRESH_NULL_TOP = 124;
    public static final int FRESH_LIST_TOP = 125;
    public static final int FRESH_FAILED_TOP = 126;
    private ArrayList<FindGroup> mList_top = new ArrayList<FindGroup>();
    private FindGroup findgroup;

    private SportsExceptionHandler mExceptionHandler = null;
    private UserDetail mUserDetail = null;

    private TextView no_jilu_txt;
    // private LinearLayout findbottomLayout;
    private TextView iView;
    private static final int IVEW_ID = 99;// 标题右侧按钮ID

    private List<String> listFinid = null;

    private LinearLayout personal_page_headview;

    private TextView look_history_sportrecord;

    private ImageView is_manorwomen_icon;
    public static List<ExpressionItems> imgItems;
    public  List<ExpressionItems> imgItems2;
    private String[] imgStr;
    private String[] imgStr2;
    private static final float APP_PAGE_SIZE = 21.0f;
    private int mViewCount;
    private ScrollLayout scrollLayout;
    private LinearLayout imgLayout;
    private List<ActionList> actionLists = new ArrayList<ActionList>();

    public SportsFindMoreHandler msportsFindMoreHandler = null;
    private int isMejump;

    @Override
    public void initIntentParam(Intent intent) {
        // TODO Auto-generated method stub
        if (intent != null) {
            isFromInvite = intent.getBooleanExtra("isFromInvite", false);
            isLoginer = intent.getBooleanExtra("isLoginer", false);
            mUid = intent.getIntExtra("ID", 0);
            isMejump = intent.getIntExtra("isMyDongtai",0);
        }
    }

    @Override
    public void initView() {
        // TODO Auto-generated method stub
        // 添加表情数组
        imgStr = getResources().getStringArray(R.array.imageStr_array);
        imgStr2 = getResources().getStringArray(R.array.daka_array);
        imgItems = new ArrayList<ExpressionItems>();
        imgItems2 = new ArrayList<ExpressionItems>();
        int j = 0,k = 0;
        Field[] files1 = R.drawable.class.getDeclaredFields();
        for (Field file : files1) {
            if (file.getName().startsWith("daka")) {
                ApplicationInfo appInfo = getApplicationInfo();
                int resID = getResources().getIdentifier(file.getName(),
                        "drawable", appInfo.packageName);
                ExpressionItems item = new ExpressionItems();
                item.setId(resID);
                item.setName(imgStr2[k].toString());
                item.setIsCanel(false);
                imgItems2.add(item);
                k++;
                // System.out.println(file.getName());
            }
        }
        Field[] files = R.drawable.class.getDeclaredFields();
        for (Field file : files) {
            if (file.getName().startsWith("biaoqing_")) {
                if (((imgItems.size() + 1) % 21) == 0) {
                    ExpressionItems item = new ExpressionItems();
                    item.setId(R.drawable.qita_biaoqing_01);
                    item.setName("itemCanel");
                    item.setIsCanel(true);
                    imgItems.add(item);
                }
                ApplicationInfo appInfo = MyConditionActivity.this
                        .getApplicationInfo();
                int resID = getResources().getIdentifier(file.getName(),
                        "drawable", appInfo.packageName);
                ExpressionItems item = new ExpressionItems();
                item.setId(resID);
                item.setName(imgStr[j].toString());
                item.setIsCanel(false);
                imgItems.add(item);
                j++;
            }
        }

        mIn = true;
        mSportsApp = (SportsApp) getApplication();
        showContentView(R.layout.activity_personal_page_main1);
        left_ayout.setId(LEFTBTLAYOUT);
        left_ayout.setOnClickListener(this);
        leftButton.setId(LEFTBTID);
        leftButton.setOnClickListener(this);
        mPersonalFindHandler = new PersonalFindHandler();
        mSportsApp.setPersonalFindHandler(mPersonalFindHandler);
        if (isLoginer) {
            waitShowDialog();
            init();
            list_user_guanzhu.setVisibility(View.GONE);
            mUserDetail = mSportsApp.getSportUser();
            title_tv.setText(getResources().getString(R.string.user_main_dongtai));
            initPortrait(mUserDetail);

            SportsFindMoreThread sportsFindMoreThread = new SportsFindMoreThread();
            SportsFindMoreThread_top sportsFindMoreThread_top = new SportsFindMoreThread_top();
            if (mSportsApp.isOpenNetwork()) {
                if (mSportsApp.getSessionId() != null
                        && !mSportsApp.getSessionId().equals("")) {
                    sportsFindMoreThread.start();
                    sportsFindMoreThread_top.start();
                }
            } else {
                Toast.makeText(MyConditionActivity.this,
                        getResources().getString(R.string.acess_server_error),
                        Toast.LENGTH_SHORT).show();

                mPullSearchListView.onRefreshComplete();
                if (loadProgressDialog != null)
                    if (loadProgressDialog.isShowing())
                        loadProgressDialog.dismiss();
            }

        } else {
            if (mUid == mSportsApp.getSportUser().getUid()) {
                waitShowDialog();
                init();
                list_user_guanzhu.setVisibility(View.GONE);
                mUserDetail = mSportsApp.getSportUser();
                title_tv.setText(getResources().getString(
                        R.string.user_main_dongtai));
                initPortrait(mUserDetail);
                // 起线程
                SportsFindMoreThread sportsFindMoreThread = new SportsFindMoreThread();
                SportsFindMoreThread_top sportsFindMoreThread_top = new SportsFindMoreThread_top();
                if (mSportsApp.isOpenNetwork()) {
                    if (mSportsApp.getSessionId() != null
                            && !mSportsApp.getSessionId().equals("")) {
                        sportsFindMoreThread.start();
                        sportsFindMoreThread_top.start();
                    }
                } else {
                    Toast.makeText(
                            MyConditionActivity.this,
                            getResources().getString(
                                    R.string.acess_server_error),
                            Toast.LENGTH_SHORT).show();

                    mPullSearchListView.onRefreshComplete();
                    if (loadProgressDialog != null)
                        if (loadProgressDialog.isShowing())
                            loadProgressDialog.dismiss();
                }
            } else {
                waitShowDialog();
                loadPortrait();
            }
        }
    }

    @Override
    public void setViewStatus() {
        // TODO Auto-generated method stub
        mExceptionHandler = mSportsApp.getmExceptionHandler();
        if (!mSportsApp.isOpenNetwork()) {
            Toast.makeText(MyConditionActivity.this,
                    getResources().getString(R.string.newwork_not_connected),
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onPageResume() {
        // TODO Auto-generated method stub
        MobclickAgent.onPageStart("MyConditionActivity");
    }

    @Override
    public void onPagePause() {
        // TODO Auto-generated method stub
        MobclickAgent.onPageEnd("MyConditionActivity");
    }

    @Override
    public void onPageDestroy() {
        // TODO Auto-generated method stub
        mIn = false;
        if (loadProgressDialog != null)
            if (loadProgressDialog.isShowing())
                loadProgressDialog.dismiss();
        isMejump = 0;
//        personal_page_headview.setVisibility(View.VISIBLE);
//		mSportsApp=null;
    }


    private void init() {
        if (mUid == mSportsApp.getSportUser().getUid()) {
            personal_page_headview = (LinearLayout) findViewById(R.id.personal_page_headview);
            personal_page_headview.setVisibility(View.GONE);
        } else {
            personal_page_headview = (LinearLayout) findViewById(R.id.other_personal_page_headview);
            personal_page_headview.setVisibility(View.GONE);
            list_user_sixin_imagview = (ImageView) personal_page_headview
                    .findViewById(R.id.list_user_sixin_imagview);
            list_user_yuepao_imagview = (ImageView) personal_page_headview
                    .findViewById(R.id.list_user_yuepao_imagview);
            look_history_sportrecord = (TextView) personal_page_headview
                    .findViewById(R.id.look_history_sportrecord);
            look_history_sportrecord.setOnClickListener(this);
            list_user_sixin_imagview.setOnClickListener(this);
            list_user_yuepao_imagview.setOnClickListener(this);
        }
        is_manorwomen_icon = (ImageView) personal_page_headview
                .findViewById(R.id.is_manorwomen_icon);
        zhuye_image_icon1 = (RoundedImage) personal_page_headview
                .findViewById(R.id.zhuye_image_icon1);
        sports_find_othername1 = (TextView) personal_page_headview
                .findViewById(R.id.sports_find_othername1);
        user_money = (TextView) personal_page_headview
                .findViewById(R.id.user_money);
        guanzhu_numbers = (TextView) personal_page_headview
                .findViewById(R.id.guanzhu_numbers);
        fensi_nums = (TextView) personal_page_headview
                .findViewById(R.id.fensi_nums);
        getxing_qianming = (TextView) personal_page_headview
                .findViewById(R.id.getxing_qianming);
        personal_address = (TextView) personal_page_headview
                .findViewById(R.id.personal_address);
        personal_sport_days = (TextView) personal_page_headview
                .findViewById(R.id.personal_sport_days);
        personal_sport_nums = (TextView) personal_page_headview
                .findViewById(R.id.personal_sport_nums);
        no_jilu_txt = (TextView) findViewById(R.id.no_jilu_txt);

        list_user_guanzhu = (ImageView) personal_page_headview
                .findViewById(R.id.list_user_guanzhu);
        list_user_guanzhu.setOnClickListener(this);

        personal_page_headview.setVisibility(View.GONE);

        mPullSearchListView = (PullToRefreshListView) findViewById(R.id.personal_page_refresh_list);
        mListView = mPullSearchListView.getRefreshableView();
        mPullSearchListView.setOnRefreshListener(new OnRefreshListener() {

            @Override
            public void onRefresh(int pullDirection) {
                if (mSportsApp.isOpenNetwork()) {
                    switch (pullDirection) {
                        case FansAndNear.MODE_DEFAULT_LOAD:
                            times++;
                            SportsFindMoreThread loadThread = new SportsFindMoreThread();
                            loadThread.start();
                            break;
                        case FansAndNear.MODE_PULL_DOWN_TO_REFRESH:
                            times = 0;
                            SportsFindMoreThread refreshThread = new SportsFindMoreThread();
                            SportsFindMoreThread_top sportsFindMoreThread_top = new SportsFindMoreThread_top();
                            refreshThread.start();
                            sportsFindMoreThread_top.start();
                            break;
                    }
                } else {
                    Toast.makeText(
                            MyConditionActivity.this,
                            getResources().getString(
                                    R.string.acess_server_error),
                            Toast.LENGTH_SHORT).show();
                    mPullSearchListView.onRefreshComplete();
                    if (loadProgressDialog != null)
                        if (loadProgressDialog.isShowing())
                            loadProgressDialog.dismiss();
                }

            }
        });

        new GetActionDataTask().execute();
    }

    class SportsFindMoreThread extends Thread {
        @Override
        public void run() {
            // TODO Auto-generated method stub
            super.run();
            Message msg = null;
            ArrayList<FindGroup> list = new ArrayList<FindGroup>();

            try {
                list = (ArrayList<FindGroup>) ApiJsonParser
                        .getNewPersonalFindList(mSportsApp.getSessionId(),
                                times, mUid + "");
            } catch (ApiNetException e) {
                e.printStackTrace();
                if (loadProgressDialog != null)
                    if (loadProgressDialog.isShowing())
                        loadProgressDialog.dismiss();
            } catch (ApiSessionOutException e) {
                e.printStackTrace();
                if (loadProgressDialog != null)
                    if (loadProgressDialog.isShowing())
                        loadProgressDialog.dismiss();
            }
            if (times == 0) {
                mList.clear();
            }
            if (list != null) {
                if (list.size() == 0) {
                    msg = Message.obtain(mPersonalFindHandler, FRESH_NULL);
                    msg.sendToTarget();
                } else {
                    for (FindGroup e : list) {
                        mList.add(e);
                    }
                    msg = Message.obtain(mPersonalFindHandler, FRESH_LIST);
                    msg.sendToTarget();
                }
            } else {
                if (list == null) {
                    msg = Message.obtain(mPersonalFindHandler, FRESH_FAILED);
                    msg.sendToTarget();
                }
            }
        }

    }

    class PersonalFindHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            if (msg.what != INIT_PORTRAIT) {
                if (mList.size() == 0) {
                    no_jilu_txt.setVisibility(View.VISIBLE);
                } else {
                    no_jilu_txt.setVisibility(View.GONE);
                }
            }
            switch (msg.what) {
                case FRESH_LIST:
                    if (times == 0) {
                        Log.d(TAG, "handleMessage mImgTimes == 0");
                        mListView.setAdapter(null);
                        sportsfindmoreAdapter = new FindOtherMoreAdapter(
                                MyConditionActivity.this, actionLists, mList,
                                mSportsApp, imgItems,imgItems2, 2);

                        // 判断是否为空
                        if (sportsfindmoreAdapter.getDelItem() == null) {
                            sportsfindmoreAdapter
                                    .setDelItem(MyConditionActivity.this);
                            listFinid = new ArrayList<String>();
                        }

                        mListView.setAdapter(sportsfindmoreAdapter);
                        mPullSearchListView.onRefreshComplete();
                    } else {
                        if (sportsfindmoreAdapter == null) {
                            sportsfindmoreAdapter = new FindOtherMoreAdapter(
                                    MyConditionActivity.this, actionLists,
                                    mList, mSportsApp, imgItems,imgItems2, 2);
                        }
                        sportsfindmoreAdapter.notifyDataSetChanged();
                        mPullSearchListView.onRefreshComplete();
                    }
                    if (loadProgressDialog != null)
                        if (loadProgressDialog.isShowing())
                            loadProgressDialog.dismiss();
                    break;

                case FRESH_FAILED:
                    if (!mSportsApp.isOpenNetwork()) {
                        Toast.makeText(
                                MyConditionActivity.this,
                                getResources().getString(
                                        R.string.acess_server_error),
                                Toast.LENGTH_SHORT).show();
                    }
                    if (loadProgressDialog != null)
                        if (loadProgressDialog.isShowing())
                            loadProgressDialog.dismiss();
                    if (sportsfindmoreAdapter != null) {
                        sportsfindmoreAdapter.notifyDataSetChanged();
                        mPullSearchListView.onRefreshComplete();
                    }
                    break;
                case FRESH_NULL:
                    if (mList.size() != 0) {
                        Toast.makeText(
                                MyConditionActivity.this,
                                getResources().getString(
                                        R.string.sports_data_load_more_null),
                                Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(
                                MyConditionActivity.this,
                                getResources().getString(
                                        R.string.sports_upload_find_new),
                                Toast.LENGTH_SHORT).show();
                    }
                    if (loadProgressDialog != null)
                        if (loadProgressDialog.isShowing())
                            loadProgressDialog.dismiss();
                    if (sportsfindmoreAdapter == null) {
                        mListView.setAdapter(null);
                        sportsfindmoreAdapter = new FindOtherMoreAdapter(
                                MyConditionActivity.this, actionLists, mList,
                                mSportsApp, imgItems,imgItems2, 2);
                        mListView.setAdapter(sportsfindmoreAdapter);
                    }
                    sportsfindmoreAdapter.notifyDataSetChanged();
                    mPullSearchListView.onRefreshComplete();
                    break;

                case FRESH_LIST_TOP:
                    if (mList_top != null && mList_top.size() != 0) {
                        int position = mListView.getSelectedItemPosition();
                        FindGroup f3 = mList_top.get(0);
                        f3.setFlog(2);
                        if (mList.size() != 0) {
                            findgroup = mList.get(0);
                        }
                        Collections.reverse(mList_top);
                        for (int i = 0; i < mList_top.size(); i++) {
                            if (findgroup != null
                                    && findgroup.getOtheruid() == mList_top.get(i)
                                    .getOtheruid()) {

                                mList.add(0, mList_top.get(i));
                                mListView.setSelection(position + 1);
                                if (sportsfindmoreAdapter != null) {
                                    sportsfindmoreAdapter.notifyDataSetChanged();
                                }
                                mPullSearchListView.onRefreshComplete();
                            }
                        }
                    }
                    break;
                case FRESH_NULL_TOP:
                    if (MyConditionActivity.this != null) {
                        if (mList_top.size() != 0) {
                            // Toast.makeText(
                            // getActivity(),
                            // getActivity().getResources().getString(
                            // R.string.sports_data_load_more_null),
                            // Toast.LENGTH_SHORT).show();
                        } else {
                            // Toast.makeText(
                            // getActivity(),
                            // getActivity().getResources().getString(
                            // R.string.sports_upload_find_new),
                            // Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;
                case FRESH_FAILED_TOP:
                    if (!mSportsApp.isOpenNetwork()) {
                        Toast.makeText(
                                MyConditionActivity.this,
                                getResources().getString(
                                        R.string.acess_server_error),
                                Toast.LENGTH_SHORT).show();
                    } else if (MyConditionActivity.this != null) {
                        Toast.makeText(
                                MyConditionActivity.this,
                                MyConditionActivity.this.getResources()
                                        .getString(R.string.sports_get_list_failed),
                                Toast.LENGTH_SHORT).show();
                    }


                case FRESH_DONE:
                    if (sportsfindmoreAdapter != null)
                        sportsfindmoreAdapter.notifyDataSetChanged();
                    break;
                case FRESH_VIEW:
                    waitShowDialog();
                    SportsFindMoreThread sportsFindMoreThread_two = new SportsFindMoreThread();
                    SportsFindMoreThread_top sportsFindMoreThread_top = new SportsFindMoreThread_top();
                    if (mSportsApp.isOpenNetwork()) {
                        sportsFindMoreThread_two.start();
                        sportsFindMoreThread_top.start();
                    } else {
                        Toast.makeText(
                                MyConditionActivity.this,
                                getResources().getString(
                                        R.string.acess_server_error),
                                Toast.LENGTH_SHORT).show();

                        mPullSearchListView.onRefreshComplete();
                        if (loadProgressDialog != null)
                            if (loadProgressDialog.isShowing())
                                loadProgressDialog.dismiss();
                    }
                    break;
                case INIT_PORTRAIT:
                    init();
                    initSportPortrait(msg);
                    title_tv.setText(mUserDetail.getUname() + "的主页");
                    // 起线程
                    SportsFindMoreThread sportsFindMoreThread = new SportsFindMoreThread();
                    SportsFindMoreThread_top sportsFindMoreThread_top2 = new SportsFindMoreThread_top();
                    if (mSportsApp.isOpenNetwork()) {
                        if (mSportsApp.getSessionId() != null
                                && !mSportsApp.getSessionId().equals("")) {
                            sportsFindMoreThread.start();
                            sportsFindMoreThread_top2.start();
                        }
                    } else {
                        Toast.makeText(
                                MyConditionActivity.this,
                                getResources().getString(
                                        R.string.acess_server_error),
                                Toast.LENGTH_SHORT).show();

                        mPullSearchListView.onRefreshComplete();
                        if (loadProgressDialog != null)
                            if (loadProgressDialog.isShowing())
                                loadProgressDialog.dismiss();
                    }

                    break;

                case FindOtherFragment.RETURN_MORE:
                    FindGroup findGroup = (FindGroup) msg.obj;
                    for (int i = 0; i < mList.size(); i++) {
                        if (findGroup.getFindId().equals(mList.get(i).getFindId())) {
                            if (findGroup.getIsFriends() != mList.get(i)
                                    .getIsFriends()) {
                                for (int j = 0; j < mList.size(); j++) {
                                    if (findGroup.getOtheruid() == mList.get(j)
                                            .getOtheruid()) {
                                        mList.get(j).setIsFriends(
                                                findGroup.getIsFriends());
                                    }
                                }
                            }
                            mList.set(i, findGroup);
                            sportsfindmoreAdapter.notifyDataSetChanged();
                        }
                    }
                    break;
                case USEREDITFRESH_VIEW:
                    boolean isfacechanged = (Boolean) msg.obj;
                    initPortrait(SportsApp.getInstance().getSportUser());
                    if (isfacechanged) {
                        waitShowDialog();
                        // 起线程
                        SportsFindMoreThread msportsFindMoreThread = new SportsFindMoreThread();
                        SportsFindMoreThread_top sportsFindMoreThread_top3 = new SportsFindMoreThread_top();
                        if (mSportsApp.isOpenNetwork()) {
                            if (mSportsApp.getSessionId() != null
                                    && !mSportsApp.getSessionId().equals("")) {
                                msportsFindMoreThread.start();
                                sportsFindMoreThread_top3.start();
                            }
                        } else {
                            Toast.makeText(
                                    MyConditionActivity.this,
                                    getResources().getString(
                                            R.string.acess_server_error),
                                    Toast.LENGTH_SHORT).show();

                            mPullSearchListView.onRefreshComplete();
                            if (loadProgressDialog != null)
                                if (loadProgressDialog.isShowing())
                                    loadProgressDialog.dismiss();
                        }

                    }
                    break;

                default:
                    break;
            }
        }

    }

    private void loadPortrait() {
        new Thread() {
            @Override
            public void run() {
                UserDetail userDetail = null;
                try {
                    ApiJsonParser
                            .visitorSports(((SportsApp) getApplication())
                                    .getSessionId(), mUid);
                    userDetail = ApiJsonParser
                            .seeUserSimple(((SportsApp) getApplication())
                                    .getSessionId(), mUid);
                } catch (ApiNetException e) {
                    e.printStackTrace();
                    if (loadProgressDialog != null
                            && loadProgressDialog.isShowing())
                        loadProgressDialog.dismiss();
                } catch (ApiSessionOutException e) {
                    e.printStackTrace();
                    if (loadProgressDialog != null
                            && loadProgressDialog.isShowing())
                        loadProgressDialog.dismiss();
                }
                Message msg = mPersonalFindHandler.obtainMessage();
                msg.what = INIT_PORTRAIT;
                msg.obj = userDetail;
                mPersonalFindHandler.sendMessage(msg);
            }
        }.start();
    }

    private void waitShowDialog() {
        if (loadProgressDialog == null) {
            loadProgressDialog = new Dialog(this, R.style.sports_dialog);
            LayoutInflater mInflater = getLayoutInflater();
            View v1 = mInflater.inflate(R.layout.sports_progressdialog, null);
            mDialogMessage = (TextView) v1.findViewById(R.id.message);
            mDialogMessage.setText(R.string.sports_wait);
            v1.setMinimumWidth((int) (SportsApp.ScreenWidth * 0.8));
            loadProgressDialog.setContentView(v1);
            loadProgressDialog.setCanceledOnTouchOutside(false);
        }
        if (loadProgressDialog != null)
            if (!loadProgressDialog.isShowing() && !isFinishing())
                loadProgressDialog.show();

    }

    private void initSportPortrait(Message msg) {
        UserDetail detail = (UserDetail) msg.obj;
        if (detail != null) {
            mUserDetail = detail;
            initPortrait(detail);
        }
    }

    private void initPortrait(UserDetail imageDetail) {
        ImageWorkManager mImageWorkerMan_Icon = new ImageWorkManager(this, 100,
                100);
        ImageResizer mImageWorker_Icon = mImageWorkerMan_Icon.getImageWorker();
        mImageWorker_Icon
                .setLoadingImage("man".equals(imageDetail.getSex()) ? R.drawable.sports_user_edit_portrait_male
                        : R.drawable.sports_user_edit_portrait);

        if (imageDetail.getUimg() != null && !"".equals(imageDetail.getUimg())) {
            mImageWorker_Icon.loadImage(imageDetail.getUimg(), zhuye_image_icon1,
                    null, null, false);
        } else {
            zhuye_image_icon1.setImageResource("man".equals(imageDetail.getSex()) ? R.drawable.sports_user_edit_portrait_male
                    : R.drawable.sports_user_edit_portrait);
        }
        sports_find_othername1.setText(imageDetail.getUname());
        // user_money.setText("金币：" + imageDetail.getCoins());
        setContent(user_money, "金币:" + imageDetail.getCoins(), 3, 0);
        if (imageDetail.getFollowCounts() == 0) {
            // guanzhu_numbers.setText("关注" + "0");
            setContent(guanzhu_numbers, "关注:" + "0", 3, 0);
        } else {
            // guanzhu_numbers.setText("关注" + imageDetail.getFollowCounts());
            setContent(guanzhu_numbers, "关注:" + imageDetail.getFollowCounts(),
                    3, 0);
        }
        if (imageDetail.getFanCounts() == 0) {
            // fensi_nums.setText("粉丝" + "0");
            setContent(fensi_nums, "粉丝:" + "0", 3, 0);
        } else {
            // fensi_nums.setText("粉丝" + imageDetail.getFanCounts());
            setContent(fensi_nums, "粉丝:" + imageDetail.getFanCounts(), 3, 0);
        }
        if (imageDetail.getFollowStatus() == 1) {
            list_user_guanzhu.setVisibility(View.VISIBLE);
            list_user_guanzhu.setImageDrawable(getResources().getDrawable(
                    R.drawable.already_guanzhu_icon));
        } else {
            if (isLoginer) {
                list_user_guanzhu.setVisibility(View.GONE);
            } else {
                if (mUid == mSportsApp.getSportUser().getUid()) {
                    list_user_guanzhu.setVisibility(View.GONE);
                } else {
                    list_user_guanzhu.setVisibility(View.VISIBLE);
                }
            }
        }

        if (imageDetail.getSignature() == null
                || "".equals(imageDetail.getSignature())) {

        } else {
            getxing_qianming.setText(imageDetail.getSignature());
        }
        if (imageDetail.getProvince() != null
                && !"".equals(imageDetail.getProvince())) {
            if (imageDetail.getCity() != null) {
                if ("县".equals(imageDetail.getCity())) {
                    personal_address.setText(imageDetail.getProvince());
                } else {
                    personal_address.setText(imageDetail.getProvince() + " "
                            + imageDetail.getCity());
                }
            } else {
                personal_address.setText(imageDetail.getProvince());
            }
        } else {
            personal_address.setText("");
        }
        String time = imageDetail.getTime() + "";
        String sportsData = imageDetail.getSportsdata() + "";
        setContent(personal_sport_days, "运动第" + imageDetail.getTime() + "天", 3,
                3 + time.length());
        if (mUid == mSportsApp.getSportUser().getUid() || isLoginer) {
            if (imageDetail.getSportsdata() != null
                    && !"".equals(imageDetail.getSportsdata())) {
                double ss = Double.parseDouble(imageDetail.getSportsdata())
                        + getUnUpload();
                String dis_str = SportTaskUtil.getDoubleNumber(ss);
                setContent(personal_sport_nums, "总里数" + dis_str + "公里", 3,
                        3 + sportsData.length());
            }

        } else {
            setContent(personal_sport_nums, "总里数" + imageDetail.getSportsdata()
                    + "公里", 3, 3 + sportsData.length());
        }

        if ("man".equals(imageDetail.getSex())) {
            is_manorwomen_icon
                    .setImageResource(R.drawable.friends_group_sexman);
        } else if ("woman".equals(imageDetail.getSex())) {
            is_manorwomen_icon
                    .setImageResource(R.drawable.friends_group_sexwomen);
        }

    }

    @Override
    public void onClick(View view) {
        // TODO Auto-generated method stub
        switch (view.getId()) {
            case R.id.list_user_guanzhu:
                addFriends();
                break;
            case IVEW_ID:
                if (!mSportsApp.LoginOption) {
                    mSportsApp.TyrLoginAction(this,
                            getString(R.string.sports_love_title),
                            getString(R.string.try_to_login));
                } else if (!mSportsApp.LoginNet) {
                    mSportsApp.NoNetLogin(this);
                } else {
                    startActivityForResult(
                            new Intent(this, UserEditActivity.class), 10);
                }
                break;

            case LEFTBTLAYOUT:
            case LEFTBTID:
                if (mSportsApp.getFindHandler() != null) {
                    Handler handler = mSportsApp.getFindHandler();
                    Message msg = new Message();
                    msg.what = FindOtherFragment.RETURN_DEL_RESULT;
                    msg.obj = listFinid;
                    handler.sendMessage(msg);
                }
                startActivity(new Intent(MyConditionActivity.this, MainFragmentActivity.class));
                finish();
                break;

            case R.id.list_user_sixin_imagview:
                Intent privateMsgIntent = new Intent(MyConditionActivity.this,
                        SportsPersonalMsg.class);
                privateMsgIntent.putExtra("uid", mUserDetail.getUid());
                privateMsgIntent.putExtra("senderIcon", mUserDetail.getUimg());
                privateMsgIntent.putExtra("senderName", mUserDetail.getUname());
                privateMsgIntent.putExtra("birthday", mUserDetail.getBirthday());
                privateMsgIntent.putExtra("sex", mUserDetail.getSex());
                startActivity(privateMsgIntent);
                break;
            case R.id.list_user_yuepao_imagview:

                new AsyncTask<Integer, Integer, ApiBack>() {

                    @Override
                    protected void onPreExecute() {
                        // TODO Auto-generated method stub
                        if (loadProgressDialog != null
                                && !loadProgressDialog.isShowing()) {
                            mDialogMessage.setText(R.string.about_runing);
                            loadProgressDialog.show();
                        }
                    }

                    @Override
                    protected ApiBack doInBackground(Integer... arg0) {
                        ApiBack back = null;
                        try {
                            back = ApiJsonParser.inviteSport(mSportsApp
                                    .getSessionId(), mSportsApp.getSportUser()
                                    .getUid(), mUid);
                        } catch (ApiNetException e) {
                            e.printStackTrace();
                        } catch (ApiSessionOutException e) {
                            e.printStackTrace();
                        }
                        return back;
                    }

                    @Override
                    protected void onPostExecute(ApiBack result) {
                        // mHandler.sendEmptyMessage(0);
                        if (loadProgressDialog != null
                                && loadProgressDialog.isShowing())
                            loadProgressDialog.dismiss();
                        if (result == null || result.getFlag() != 0) {
                            Toast.makeText(MyConditionActivity.this,
                                    getString(R.string.sport_invite_failure),
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MyConditionActivity.this,
                                    getString(R.string.sport_invite_success),
                                    Toast.LENGTH_SHORT).show();
                            MobclickAgent.onEvent(MyConditionActivity.this,
                                    "appointrun");
                        }

                    }
                }.execute();

                break;

            case R.id.look_history_sportrecord:
                Intent intent = new Intent(this, HistoryAllActivity.class);
                intent.putExtra("ID", mUid);
                startActivity(intent);
                break;
            default:
                break;
        }

    }

    private int status;

    /**
     * 添加好友事件
     */
    private void addFriends() {
        final Animation animation = AnimationUtils.loadAnimation(this,
                R.anim.add_one);
        if (mSportsApp.isLogin() == false
                && (mSportsApp.getSessionId() == null || "".equals(mSportsApp
                .getSessionId()))) {
            Intent intent = new Intent(MyConditionActivity.this,
                    LoginActivity.class);
            startActivity(intent);
            return;
        }

        if (mSportsApp.isOpenNetwork()) {
            new AsyncTask<Void, Void, ApiBack>() {
                @Override
                protected void onPreExecute() {
                    // TODO Auto-generated method stub
                    status = mUserDetail.getFollowStatus();
                }

                @Override
                protected ApiBack doInBackground(Void... params) {
                    // TODO Auto-generated method stub
                    ApiBack back = null;
                    try {
                        back = ApiJsonParser.follow(mSportsApp.getSessionId(),
                                mUid, status == 2 ? 1 : 2, 1);
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
                    if (result == null || result.getFlag() != 0) {
                        Toast.makeText(
                                MyConditionActivity.this,
                                getResources().getString(
                                        R.string.sports_findgood_error),
                                Toast.LENGTH_SHORT).show();
                    } else {
                        // 成功
                        list_user_guanzhu.startAnimation(animation);
                        new Handler().postDelayed(new Runnable() {
                            public void run() {
                                if (status == 2) {
                                    mUserDetail.setFollowStatus(1);
                                    mSportsApp.getSportUser().setFollowCounts(
                                            mSportsApp.getSportUser()
                                                    .getFollowCounts() + 1);
                                    list_user_guanzhu
                                            .setImageDrawable(getResources()
                                                    .getDrawable(
                                                            R.drawable.already_guanzhu_icon));
                                    sendmessage();
                                } else {
                                    mUserDetail.setFollowStatus(2);
                                    list_user_guanzhu
                                            .setImageDrawable(getResources()
                                                    .getDrawable(
                                                            R.drawable.per_guanzhu_btn_icon));
                                    mSportsApp.getSportUser().setFollowCounts(
                                            mSportsApp.getSportUser()
                                                    .getFollowCounts() - 1);
                                    Toast.makeText(MyConditionActivity.this, "取消关注成功！", Toast.LENGTH_SHORT)
                                            .show();
                                }

                            }
                        }, 1000);
                    }
                }

            }.execute();
        } else {
            Toast.makeText(MyConditionActivity.this,
                    getResources().getString(R.string.error_cannot_access_net),
                    Toast.LENGTH_SHORT).show();

        }

    }
    private void sendmessage(){
        final String message="Hi，我关注了爱运动的你，一起来运动吧！";
        // 启动异步发送消息
        if (mSportsApp.isOpenNetwork()) {
            // showWaitDialog(mContext.getResources().getString(
            // R.string.sports_dialog_toping));
            new AsyncTask<Void, Void, ApiBack>() {
                @Override
                protected ApiBack doInBackground(Void... params) {
                    // TODO Auto-generated method stub
                    ApiBack back = null;
                    try {
                        try {
                            back = (ApiBack) ApiJsonParser.sendprimsg(
                                    mSportsApp.getSessionId(), mUid,
                                    message
                                    , "", 0);
                        } catch (ApiSessionOutException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    } catch (ApiNetException e) {
                        e.printStackTrace();
                    }
                    return back;
                }

                @Override
                protected void onPostExecute(ApiBack result) {
                    // TODO Auto-generated method stub
                    super.onPostExecute(result);
                    if (result == null || result.getFlag() != 0) {
                        Toast.makeText(
                                MyConditionActivity.this,
                                MyConditionActivity.this.getResources().getString(
                                        R.string.sports_top_failed),
                                Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MyConditionActivity.this, "关注成功！", Toast.LENGTH_SHORT)
                                .show();
                        List<UserPrimsgAll> primsgAllList = new ArrayList<UserPrimsgAll>();
                        final UserPrimsgOne priMsg = new UserPrimsgOne();
                        priMsg.setAddTime(System.currentTimeMillis() / 1000);
                        priMsg.setCommentText(message);
                        priMsg.setCommentWav("");
                        priMsg.setWavDuration(0);
                        priMsg.setUid(mSportsApp.getSportUser().getUid());
                        priMsg.setTouid(mUid);
                        priMsg.setOwnerid(mSportsApp.getSportUser().getUid());
                        long sava = mSportsApp.getSportsDAO().savePrivateMsgInfo(priMsg);
                        Log.i("SSSS", sava + "");
                        UserPrimsgAll userPrimsgAll = new UserPrimsgAll();
                        userPrimsgAll.setAddTime(priMsg.getAddTime());
                        userPrimsgAll.setName(mUserDetail.getUname());
                        userPrimsgAll.setUid(priMsg.getTouid());
                        userPrimsgAll.setTouid(mSportsApp.getSportUser().getUid());
                        userPrimsgAll.setUimg( mUserDetail.getUimg());
                        userPrimsgAll.setBirthday(mUserDetail.getBirthday());
                        userPrimsgAll.setCounts(0);
                        userPrimsgAll.setSex(mUserDetail.getSex());
                        userPrimsgAll.setTouimg(mSportsApp.getSportUser().getUimg());
                        primsgAllList.add(userPrimsgAll);
                        mSportsApp.getSportsDAO().insertPrivateMsgAll(SportsContent.PrivateMessageAllTable.TABLE_NAME,
                                primsgAllList, "read");
                        Log.d("userPrimsgAll", "save msg to PrivateMsgAll :" + userPrimsgAll);
                    }
                }
            }.execute();
        }
    }

//	private void showBinaJiBtn() {
//		iView = new TextView(this);
//		iView.setBackgroundResource(R.drawable.stroke_nocicle_personal_background);
//		iView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
//				LayoutParams.WRAP_CONTENT));
//		showRightBtn(iView);
//		right_btn.setPadding(0, 0, mSportsApp.dip2px(17), 0);
//		iView.setId(IVEW_ID);
//		iView.setPadding(20, 15, 20, 15);
//		iView.setText("编辑");
//		iView.setTextColor(getResources().getColor(R.color.black));
//		iView.setOnClickListener(this);
//	}

    @Override
    public void returnFindid(String findId) {
        // TODO Auto-generated method stub
        // 活动删除的findid
        if (listFinid != null) {
            listFinid.add(findId);
        }
    }

    private void setContent(TextView textView, String content, int num,
                            int nextNum) {
        int tempLength = content.length();
        SpannableString ss = new SpannableString(content);
        ss.setSpan(new ForegroundColorSpan(Color.parseColor("#333333")), 0,
                num, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        if (nextNum == 0) {
            ss.setSpan(new ForegroundColorSpan(Color.parseColor("#FF6003")),
                    num, tempLength, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        } else {
            ss.setSpan(new ForegroundColorSpan(Color.parseColor("#FF6003")),
                    num, nextNum, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            ss.setSpan(new ForegroundColorSpan(Color.parseColor("#333333")),
                    nextNum, tempLength, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        textView.setText(ss);
    }

    // 获取到登录者没有上传的运动记录
    private double getUnUpload() {
        double unNum = 0;
        if (mSportsApp != null && mSportsApp.getSportUser() != null) {
            SportSubTaskDB taskDBhelper = SportSubTaskDB.getInstance(this);
            ArrayList<SportTask> unUploadDatas = taskDBhelper
                    .getTasksByUnOrIngUpload(mSportsApp.getSportUser().getUid());
            if (unUploadDatas != null && unUploadDatas.size() > 0) {
                for (int j = 0; j < unUploadDatas.size(); j++) {
                    if (SportTaskUtil.getNormalRange(unUploadDatas.get(j)
                            .getSport_type_task(), unUploadDatas.get(j)
                            .getSport_speed(), unUploadDatas.get(j)
                            .getSport_time())) {
                        unNum += unUploadDatas.get(j).getSport_distance();
                    }
                }

            }
        }
        return unNum;
    }

    public class ExpressionImgAdapter extends BaseAdapter {
        private List<ExpressionItems> mList;
        private Context mContext;
        public static final int APP_PAGE_SIZE = 21;

        public ExpressionImgAdapter(Context context,
                                    List<ExpressionItems> list, int page) {
            mContext = context;

            mList = new ArrayList<ExpressionItems>();
            int i = page * APP_PAGE_SIZE;
            int iEnd = i + APP_PAGE_SIZE;
            while ((i < list.size()) && (i < iEnd)) {
                mList.add(list.get(i));
                i++;
            }
        }

        public int getCount() {
            return mList.size();
        }

        public Object getItem(int position) {
            return mList.get(position);
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            ExpressionItems map = mList.get(position);
            ImgItem appItem;
            if (convertView == null) {
                View v = LayoutInflater.from(mContext).inflate(
                        R.layout.app_item, null);

                appItem = new ImgItem();
                appItem.mAppIcon = (ImageView) v.findViewById(R.id.ivAppIcon);

                v.setTag(appItem);
                convertView = v;
            } else {
                appItem = (ImgItem) convertView.getTag();
            }
            appItem.mAppIcon.setBackgroundResource((Integer) map.getId());
            return convertView;
        }

        class ImgItem {
            ImageView mAppIcon;
        }
    }

    // 下载活动列表
    private class GetActionDataTask extends
            AsyncTask<Void, Void, List<ActionList>> {

        @Override
        protected List<ActionList> doInBackground(Void... sessionid) {

            List<ActionList> actionLists = null;
            try {
                actionLists = ApiJsonParser.getNewActionList(
                        mSportsApp.getSessionId(), "z" + getResources().getString(R.string.config_game_id), 0);
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
            // waitCloset();
            if (result == null)
                return;
            if (result.size() > 0) {
                for (ActionList actionList : result) {
                    actionLists.add(actionList);
                }
            }
        }

    }

    class SportsFindMoreThread_top extends Thread {
        @Override
        public void run() {
            // TODO Auto-generated method stub
            super.run();
            Message msg = null;
            ArrayList<FindGroup> list = new ArrayList<FindGroup>();
            try {
                list = (ArrayList<FindGroup>) ApiJsonParser
                        .getNewFindList(mSportsApp.getSessionId());
            } catch (ApiNetException e) {
                e.printStackTrace();
            } catch (ApiSessionOutException e) {
                e.printStackTrace();
            }
            if (list != null) {
                if (list.size() == 0) {
                    msg = Message
                            .obtain(mPersonalFindHandler, FRESH_NULL_TOP);
                    msg.sendToTarget();
                } else {
                    mList_top.clear();
                    for (FindGroup e : list) {
                        e.setFlog(2);
                        mList_top.add(e);
                    }
                    msg = Message
                            .obtain(mPersonalFindHandler, FRESH_LIST_TOP);
                    msg.sendToTarget();
                }
            } else {
                if (list == null) {
                    msg = Message.obtain(mPersonalFindHandler,
                            FRESH_FAILED_TOP);
                    msg.sendToTarget();
                }
            }
        }

    }

}
