package com.fox.exercise.newversion.act;

import java.io.File;
import java.lang.reflect.Field;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import cn.ingenic.indroidsync.SportsApp;
import com.fox.exercise.AbstractBaseActivity;
import com.fox.exercise.AddFriendActivity;
import com.fox.exercise.FansAndNear;
import com.fox.exercise.FindOtherFragment;
import com.fox.exercise.FindOtherMoreAdapter;
import com.fox.exercise.FindOtherMoreAdapter.DelItem;
import com.fox.exercise.HistorySportAdpter;
import com.fox.exercise.R;
import com.fox.exercise.SportsExceptionHandler;
import com.fox.exercise.SportsPersonalMsg;
import com.fox.exercise.Utils;
import com.fox.exercise.api.ApiBack;
import com.fox.exercise.api.ApiConstant;
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
import com.fox.exercise.newversion.adapter.NewHistorySportAdapter;
import com.fox.exercise.newversion.entity.FindGroup;
import com.fox.exercise.newversion.newact.NewFansActivity;
import com.fox.exercise.newversion.newact.NewGuanZhuActivity;
import com.fox.exercise.pedometer.ImageWorkManager;
import com.fox.exercise.pedometer.SportContionTaskDetail;
import com.fox.exercise.util.RoundedImage;
import com.fox.exercise.util.SportTaskUtil;
import com.fox.exercise.view.PullToRefreshListView;
import com.fox.exercise.view.PullToRefreshBase.OnRefreshListener;
import android.content.ContentValues;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.TextAppearanceSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import static com.fox.exercise.R.id.add;
import static com.fox.exercise.R.id.loading_layout;
import static com.fox.exercise.R.id.web_train;

/**
 * @author loujungang 个人主页
 */
public class PersonalPageMainActivity extends AbstractBaseActivity implements
        OnClickListener, DelItem {

    private boolean isLoginer = false;
    private int mUid;
    public static boolean mIn = false;
    private SportsApp mSportsApp;
    private Dialog loadProgressDialog;
    private TextView mDialogMessage;
    private PullToRefreshListView mPullSearchListView = null;

    private PullToRefreshListView gifts_pull_refresh_list = null;
    private ListView mListView = null;
    private ListView mYundongjiluListView = null;
    private HistorySportAdpter mYundongjiluAdapter = null;
    private NewHistorySportAdapter historySportAdapter = null;
    private ArrayList<SportContionTaskDetail> mYundongjiluList = null;
    private HistoryHandler mHandler = null;
    private Dialog mProgressDialog;
    private SportSubTaskDB db;
    private int lastPosition = -1;
    private boolean mIsRefresh = true;
    private static final int FRESH_LIST_JILU = 127;
    private static final int NO_HISTORY = 128;
    public static final int SPORT_DEL = 129;
    private int mTimes = 0;
    private ImageView dongtai_selected, history_selected, iv_xiugai, iv_fengexian,xunzhang_selected;
    private TextView tv_jinbi, tv_guanzhu_num, tv_fensi_num;
    private RelativeLayout rl_header;
    private LinearLayout ll_qianming;

    private RoundedImage zhuye_image_icon1;
    private TextView sports_find_othername1, personal_address, personal_sport_nums;
    private ImageView list_user_guanzhu, list_user_sixin_imagview;
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
    private TextView no_jilu_txt, no_history;
    private TextView iView;
    private static final int IVEW_ID = 99;// 标题右侧按钮ID
    private List<String> listFinid = null;
    private LinearLayout personal_page_headview;
    private TextView dongtai, look_history_sportrecord,xunzhang;
    public static List<ExpressionItems> imgItems;
    public  List<ExpressionItems> imgItems2;
    private String[] imgStr;
    private String[] imgStr2;
    private static final float APP_PAGE_SIZE = 21.0f;
    private List<ActionList> actionLists = new ArrayList<ActionList>();
    private int isFromaddFriend;


    private boolean other = false;
    private RelativeLayout rl_webView;
    private WebView web_train;
    private LinearLayout loading_layout;
    private String medal_url = ApiConstant.DATA_URL+ApiConstant.medalWeb;
    private String url;
    private String APP_CACAHE_DIRNAME = "/webCache";


    @Override
    public void initIntentParam(Intent intent) {
        // TODO Auto-generated method stub
        if (intent != null) {
            isLoginer = intent.getBooleanExtra("isLoginer", false);
            isFromaddFriend = intent.getIntExtra("fromactivity",0);
            mUid = intent.getIntExtra("ID", 0);
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
        Field[] files1 = R.drawable.class.getDeclaredFields();//打卡的图片列表
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
                ApplicationInfo appInfo = PersonalPageMainActivity.this
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
        showContentView(R.layout.activity_personal_page_main);
        left_ayout.setId(LEFTBTLAYOUT);
        left_ayout.setOnClickListener(this);
        leftButton.setId(LEFTBTID);
        leftButton.setOnClickListener(this);
        mPersonalFindHandler = new PersonalFindHandler();
        mSportsApp.setPersonalFindHandler(mPersonalFindHandler);
        if (isLoginer) {
            if (!mSportsApp.LoginOption) {
                mSportsApp.TyrLoginAction(PersonalPageMainActivity.this,
                        getString(R.string.sports_love_title),
                        getString(R.string.try_to_login));

            } else {
                waitShowDialog();
                init();
                //list_user_guanzhu.setVisibility(View.GONE);
                mUserDetail = mSportsApp.getSportUser();
                title_tv.setText(getResources().getString(R.string.user_main_page));
                initPortrait(mUserDetail);
                // 起线程
                SportsFindMoreThread sportsFindMoreThread = new SportsFindMoreThread();
//            SportsFindMoreThread_top sportsFindMoreThread_top = new SportsFindMoreThread_top();
                if (!mSportsApp.LoginOption) {
                    mSportsApp.TyrLoginAction(PersonalPageMainActivity.this,
                            getString(R.string.sports_love_title),
                            getString(R.string.try_to_login));

                } else{
                    if (mSportsApp.isOpenNetwork()) {
                        if (mSportsApp.getSessionId() != null
                                && !mSportsApp.getSessionId().equals("")) {
                            sportsFindMoreThread.start();
//                    sportsFindMoreThread_top.start();
                        }
                    } else {
                        Toast.makeText(PersonalPageMainActivity.this,
                                getResources().getString(R.string.acess_server_error),
                                Toast.LENGTH_SHORT).show();

                        mPullSearchListView.onRefreshComplete();
                        if (loadProgressDialog != null)
                            if (loadProgressDialog.isShowing())
                                loadProgressDialog.dismiss();
                    }
                }
            }
        } else {
            if (!mSportsApp.LoginOption) {
                mSportsApp.TyrLoginAction(PersonalPageMainActivity.this,
                        getString(R.string.sports_love_title),
                        getString(R.string.try_to_login));

            }else{
                if (mUid == mSportsApp.getSportUser().getUid()) {
                    waitShowDialog();
                    init();
                    //list_user_guanzhu.setVisibility(View.GONE);
                    mUserDetail = mSportsApp.getSportUser();
                    title_tv.setText(getResources().getString(
                            R.string.user_main_page));
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
                                PersonalPageMainActivity.this,
                                getResources().getString(
                                        R.string.acess_server_error),
                                Toast.LENGTH_SHORT).show();

                        mPullSearchListView.onRefreshComplete();
                        if (loadProgressDialog != null)
                            if (loadProgressDialog.isShowing())
                                loadProgressDialog.dismiss();
                    }
                } else {
                    if (!mSportsApp.LoginOption) {
                        mSportsApp.TyrLoginAction(PersonalPageMainActivity.this,
                                getString(R.string.sports_love_title),
                                getString(R.string.try_to_login));

                    }else {
                        waitShowDialog();
                        loadPortrait();
                    }
                }
            }
        }
    }

    @Override
    public void setViewStatus() {
        // TODO Auto-generated method stub
        mExceptionHandler = mSportsApp.getmExceptionHandler();
        if (!mSportsApp.isOpenNetwork()) {
            Toast.makeText(PersonalPageMainActivity.this,
                    getResources().getString(R.string.newwork_not_connected),
                    Toast.LENGTH_SHORT).show();
        }
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
        mIn = false;
        if (loadProgressDialog != null)
            if (loadProgressDialog.isShowing())
                loadProgressDialog.dismiss();

        if (other){
            clearWebViewCache();
            CookieSyncManager.createInstance(this);
            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.removeAllCookie();
            web_train.removeAllViews();
            web_train.destroy();
            web_train = null;
        }


    }

    private void init() {
        if (mUid == mSportsApp.getSportUser().getUid()) {
            personal_page_headview = (LinearLayout) findViewById(R.id.personal_page_headview);
            personal_page_headview.setVisibility(View.VISIBLE);
            tv_jinbi = (TextView) personal_page_headview
                    .findViewById(R.id.tv_jinbi);
            tv_guanzhu_num = (TextView) personal_page_headview
                    .findViewById(R.id.tv_guanzhu_num);
            tv_guanzhu_num.setOnClickListener(this);
            tv_fensi_num = (TextView) personal_page_headview
                    .findViewById(R.id.tv_fensi_num);
            tv_fensi_num.setOnClickListener(this);
            iv_xiugai = (ImageView) personal_page_headview.findViewById(R.id.iv_xiugai);
            iv_xiugai.setOnClickListener(this);
        } else {
            other = true;
            url = medal_url+mUid;
            personal_page_headview = (LinearLayout) findViewById(R.id.other_personal_page_headview);
            personal_page_headview.setVisibility(View.VISIBLE);
            list_user_guanzhu = (ImageView) personal_page_headview
                    .findViewById(R.id.list_user_guanzhu);
            list_user_guanzhu.setOnClickListener(this);
            list_user_sixin_imagview = (ImageView) personal_page_headview
                    .findViewById(R.id.list_user_sixin_imagview);
            list_user_sixin_imagview.setOnClickListener(this);
            personal_sport_nums = (TextView) personal_page_headview
                    .findViewById(R.id.personal_sport_nums);
            ll_qianming = (LinearLayout) personal_page_headview.findViewById(R.id.ll_qianming);
            //勋章
            xunzhang = (TextView) findViewById(R.id.xunzhang);
            xunzhang_selected = (ImageView) findViewById(R.id.xunzhang_selected);
            xunzhang.setOnClickListener(this);

            rl_webView = (RelativeLayout) findViewById(R.id.rl_webView);
            web_train = (WebView) findViewById(R.id.web_train);
            loading_layout = (LinearLayout) findViewById(R.id.loading_layout);


        }
        iv_fengexian = (ImageView) personal_page_headview.findViewById(R.id.iv_fengexian);
        rl_header = (RelativeLayout) personal_page_headview.findViewById(R.id.rl_header);
        look_history_sportrecord = (TextView) personal_page_headview
                .findViewById(R.id.look_history_sportrecord);
        look_history_sportrecord.setOnClickListener(this);
        dongtai = (TextView) personal_page_headview.findViewById(R.id.dongtai);
        dongtai.setOnClickListener(this);
        zhuye_image_icon1 = (RoundedImage) personal_page_headview
                .findViewById(R.id.zhuye_image_icon1);
        sports_find_othername1 = (TextView) personal_page_headview
                .findViewById(R.id.sports_find_othername1);
        getxing_qianming = (TextView) personal_page_headview
                .findViewById(R.id.getxing_qianming);
		personal_address = (TextView) personal_page_headview
                .findViewById(R.id.personal_address);
        no_jilu_txt = (TextView) findViewById(R.id.no_jilu_txt);
        no_history = (TextView) findViewById(R.id.no_history);


        dongtai_selected = (ImageView) personal_page_headview.findViewById(R.id.dongtai_selected);
        history_selected = (ImageView) personal_page_headview.findViewById(R.id.history_selected);

        gifts_pull_refresh_list = (PullToRefreshListView) findViewById(R.id.gifts_pull_refresh_list);
        gifts_pull_refresh_list.setVisibility(View.GONE);
        mYundongjiluListView = gifts_pull_refresh_list.getRefreshableView();
        gifts_pull_refresh_list.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(int pullDirection) {
                switch (pullDirection) {
                    case FansAndNear.MODE_DEFAULT_LOAD:
                        Log.d(TAG, "****向上拉***");
                        mIsRefresh = true;
                        GetUploadThread loadGiftsThread = new GetUploadThread();
                        loadGiftsThread.start();
                        break;
                }
            }
        });
        gifts_pull_refresh_list.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (mYundongjiluListView.getFirstVisiblePosition() == 0) {
                    if (scrollState == 0) {
                        rl_header.setVisibility(View.VISIBLE);
                        iv_fengexian.setVisibility(View.VISIBLE);
                        if (mUid != mSportsApp.getSportUser().getUid()) {
                            ll_qianming.setVisibility(View.VISIBLE);
                        }
                    }
                } else {
                    rl_header.setVisibility(View.GONE);
                    iv_fengexian.setVisibility(View.GONE);
                    if (mUid != mSportsApp.getSportUser().getUid()) {
                        ll_qianming.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            }
        });
        mYundongjiluList = new ArrayList<SportContionTaskDetail>();
        mHandler = new HistoryHandler();
        db = SportSubTaskDB.getInstance(this);

        GetUploadThread newUploadThread = new GetUploadThread();
        newUploadThread.start();

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
                            PersonalPageMainActivity.this,
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
        mPullSearchListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (mListView.getFirstVisiblePosition() == 0) {
                    if (scrollState == 0) {
                        rl_header.setVisibility(View.VISIBLE);
                        iv_fengexian.setVisibility(View.VISIBLE);
                        if (mUid != mSportsApp.getSportUser().getUid()) {
                            ll_qianming.setVisibility(View.VISIBLE);
                        }
                    }
                } else {
                    rl_header.setVisibility(View.GONE);
                    iv_fengexian.setVisibility(View.GONE);
                    if (mUid != mSportsApp.getSportUser().getUid()) {
                        ll_qianming.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
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
                Log.e(TAG, "length-------------" + list.size());
                if (list.size() == 0) {
                    msg = Message.obtain(mPersonalFindHandler, FRESH_NULL);
                    msg.sendToTarget();
                } else {
                    for (FindGroup e : list) {
                        mList.add(e);
                    }
                    Log.i("personalzhong", "mList------------" + mList.size());
                    msg = Message.obtain(mPersonalFindHandler, FRESH_LIST);
                    msg.sendToTarget();
                }
            } else {
                if (list == null) {
                    Log.d(TAG, "*******检z4********");
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
                    gifts_pull_refresh_list.setVisibility(View.GONE);
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
                                PersonalPageMainActivity.this, actionLists, mList,
                                mSportsApp, imgItems,imgItems2, 2);

                        // 判断是否为空
                        if (sportsfindmoreAdapter.getDelItem() == null) {
                            sportsfindmoreAdapter
                                    .setDelItem(PersonalPageMainActivity.this);
                            listFinid = new ArrayList<String>();
                        }

                        mListView.setAdapter(sportsfindmoreAdapter);
                        mPullSearchListView.onRefreshComplete();
                    } else {
                        if (sportsfindmoreAdapter == null) {
                            sportsfindmoreAdapter = new FindOtherMoreAdapter(
                                    PersonalPageMainActivity.this, actionLists,
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
                                PersonalPageMainActivity.this,
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
                                PersonalPageMainActivity.this,
                                getResources().getString(
                                        R.string.sports_data_load_more_null),
                                Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(
                                PersonalPageMainActivity.this,
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
                                PersonalPageMainActivity.this, actionLists, mList,
                                mSportsApp, imgItems,imgItems2, 2);
                        mListView.setAdapter(sportsfindmoreAdapter);
                    }
                    sportsfindmoreAdapter.notifyDataSetChanged();
                    mPullSearchListView.onRefreshComplete();
                    break;

                case FRESH_LIST_TOP:
                    if (mList_top != null && mList_top.size() != 0) {
                        Log.i("zhiding", "置顶数组的长度：" + mList_top.size());
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
                case FRESH_FAILED_TOP:
                    if (!mSportsApp.isOpenNetwork()) {
                        Toast.makeText(
                                PersonalPageMainActivity.this,
                                getResources().getString(
                                        R.string.acess_server_error),
                                Toast.LENGTH_SHORT).show();
                    } else if (PersonalPageMainActivity.this != null) {
                        Toast.makeText(
                                PersonalPageMainActivity.this,
                                PersonalPageMainActivity.this.getResources()
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
                                PersonalPageMainActivity.this,
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
                    if (null != mUserDetail  && null != mUserDetail.getUname()) {
                        title_tv.setText(mUserDetail.getUname() + "的主页");
                    }
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
                                PersonalPageMainActivity.this,
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
//                        SportsFindMoreThread_top sportsFindMoreThread_top3 = new SportsFindMoreThread_top();
                        if (mSportsApp.isOpenNetwork()) {
                            if (mSportsApp.getSessionId() != null
                                    && !mSportsApp.getSessionId().equals("")) {
                                msportsFindMoreThread.start();
//                                sportsFindMoreThread_top3.start();
                            }
                        } else {
                            Toast.makeText(
                                    PersonalPageMainActivity.this,
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
                .setLoadingImage("woman".equals(imageDetail.getSex()) ? R.drawable.sports_user_edit_portrait
                        : R.drawable.sports_user_edit_portrait_male);

        if (imageDetail.getUimg() != null && !"http://dev-kupao.mobifox.cn".equals(imageDetail.getUimg())
                && !"http://kupao.mobifox.cn".equals(imageDetail.getUimg())) {
            mImageWorker_Icon.loadImage(imageDetail.getUimg(), zhuye_image_icon1,
                    null, null, false);
        } else {
            zhuye_image_icon1.setImageResource("woman".equals(imageDetail.getSex()) ? R.drawable.sports_user_edit_portrait
                    : R.drawable.sports_user_edit_portrait_male);
        }
        sports_find_othername1.setText(imageDetail.getUname());

        if (mUid == mSportsApp.getSportUser().getUid()) {
            NumberFormat number_format= NumberFormat.getInstance(Locale.CHINA);
            tv_jinbi.setText(getString(R.string.jibu_kongge1) + number_format.format(imageDetail.getCoins()));

            String str = "关注" + getString(R.string.jibu_kongge1) + imageDetail.getFollowCounts();
            int tempLength = str.length();
            SpannableString ss = new SpannableString(str);
            ss.setSpan(new TextAppearanceSpan(PersonalPageMainActivity.this, R.style.jibu_style12),
                       0, 3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            ss.setSpan(new TextAppearanceSpan(PersonalPageMainActivity.this, R.style.jibu_style18_f6),
                       3, tempLength, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            tv_guanzhu_num.setText(ss);

            str = "粉丝" + getString(R.string.jibu_kongge1) + imageDetail.getFanCounts();
            tempLength = str.length();
            ss = new SpannableString(str);
            ss.setSpan(new TextAppearanceSpan(PersonalPageMainActivity.this, R.style.jibu_style12),
                       0, 3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            ss.setSpan(new TextAppearanceSpan(PersonalPageMainActivity.this, R.style.jibu_style18_f6),
                    3, tempLength, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            tv_fensi_num.setText(ss);
        }

        if (mUid != mSportsApp.getSportUser().getUid()) {
            if (imageDetail.getFollowStatus() == 1) {
                list_user_guanzhu.setBackgroundResource(R.drawable.quxiaoguanzhu);
                list_user_guanzhu.setVisibility(View.VISIBLE);
            } else {
                list_user_guanzhu.setBackgroundResource(R.drawable.guanzhu);
                list_user_guanzhu.setVisibility(View.VISIBLE);
            }
        }

        if (imageDetail.getSignature() == null
                || "".equals(imageDetail.getSignature())) {

        } else {
            getxing_qianming.setText(imageDetail.getSignature());
        }
        Log.e("develop_debug", "province:" + imageDetail.getProvince() + ",city:" + imageDetail.getCity()
              + ",area:" + imageDetail.getArea());
        String strAddress = "";
        if ((imageDetail.getProvince() != null) &&
            (!"".equals(imageDetail.getProvince()))) {
            strAddress = strAddress + imageDetail.getProvince();

            if (!imageDetail.getProvince().contains("市") && !imageDetail.getProvince().contains("自治区")) {
                if ((imageDetail.getCity() != null) &&
                        (!"".equals(imageDetail.getCity())) &&
                        (!"县".equals(imageDetail.getCity())) &&
                        (!"市辖区".equals(imageDetail.getCity()))) {
                    strAddress = strAddress + " " + imageDetail.getCity();
                }
            }
        }
        personal_address.setText(strAddress);

        String str = "";
        int tempLength = 0;
        SpannableString ss;

        if (mUid != mSportsApp.getSportUser().getUid()) {
            str = imageDetail.getSportsdata() + "Km";
            tempLength = str.length();
            ss = new SpannableString(str);
            ss.setSpan(new TextAppearanceSpan(PersonalPageMainActivity.this, R.style.jibu_style45),
                    0, tempLength - 5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            ss.setSpan(new TextAppearanceSpan(PersonalPageMainActivity.this, R.style.jibu_style22),
                    tempLength - 5, tempLength, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            personal_sport_nums.setText(ss);
        }

        str = "动态（" + imageDetail.getFind_count_num() + "）";
        tempLength = str.length();
        ss = new SpannableString(str);
        ss.setSpan(new TextAppearanceSpan(PersonalPageMainActivity.this, R.style.jibu_style18_33),
                   0, 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(new TextAppearanceSpan(PersonalPageMainActivity.this, R.style.jibu_style15_33),
                2, tempLength, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        dongtai.setText(ss);

        str = "运动记录（" + imageDetail.getCount_num() + "）";
        tempLength = str.length();
        ss = new SpannableString(str);
        ss.setSpan(new TextAppearanceSpan(PersonalPageMainActivity.this, R.style.jibu_style18_99),
                   0, 4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(new TextAppearanceSpan(PersonalPageMainActivity.this, R.style.jibu_style15_99),
                   4, tempLength, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        look_history_sportrecord.setText(ss);

        //添加勋章
        addMedal(imageDetail);

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK: {
                if (isFromaddFriend != 2) {
                    finish();
                }else {
                    startActivity(new Intent(PersonalPageMainActivity.this, AddFriendActivity.class));
                    finish();
                }
            }

        }
        return super.onKeyDown(keyCode, event);
    }
    @Override
    public void onClick(View view) {
        // TODO Auto-generated method stub
        switch (view.getId()) {
            case R.id.list_user_guanzhu:
                if (Utils.isFastClick(3000)) {
                    return;
                }
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
                if (isFromaddFriend != 2) {
                    finish();
                }else {
                    startActivity(new Intent(PersonalPageMainActivity.this, AddFriendActivity.class));
                    finish();
                }

                break;

            case R.id.list_user_sixin_imagview:
                Intent privateMsgIntent = new Intent(PersonalPageMainActivity.this,
                        SportsPersonalMsg.class);
                privateMsgIntent.putExtra("uid", mUserDetail.getUid());
                privateMsgIntent.putExtra("senderIcon", mUserDetail.getUimg());
                privateMsgIntent.putExtra("senderName", mUserDetail.getUname());
                privateMsgIntent.putExtra("birthday", mUserDetail.getBirthday());
                privateMsgIntent.putExtra("sex", mUserDetail.getSex());
                startActivity(privateMsgIntent);
                break;
            case R.id.iv_xiugai:
                Intent intent = new Intent(PersonalPageMainActivity.this, UserEditActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_guanzhu_num:
                Intent guanzhu_intent = new Intent(PersonalPageMainActivity.this, NewGuanZhuActivity.class);
                startActivity(guanzhu_intent);
                break;
            case R.id.tv_fensi_num:
                Intent fensi_intent = new Intent(PersonalPageMainActivity.this, NewFansActivity.class);
                startActivity(fensi_intent);
                break;
            case R.id.dongtai:
                String str = "动态（" + mUserDetail.getFind_count_num() + "）";
                int tempLength = str.length();
                SpannableString ss = new SpannableString(str);
                ss.setSpan(new TextAppearanceSpan(PersonalPageMainActivity.this, R.style.jibu_style18_33),
                           0, 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                ss.setSpan(new TextAppearanceSpan(PersonalPageMainActivity.this, R.style.jibu_style15_33),
                           2, tempLength, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                dongtai.setText(ss);

                str = "运动记录（" + mUserDetail.getCount_num() + "）";
                tempLength = str.length();
                ss = new SpannableString(str);
                ss.setSpan(new TextAppearanceSpan(PersonalPageMainActivity.this, R.style.jibu_style18_99),
                           0, 4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                ss.setSpan(new TextAppearanceSpan(PersonalPageMainActivity.this, R.style.jibu_style15_99),
                           4, tempLength, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                look_history_sportrecord.setText(ss);

                history_selected.setVisibility(View.GONE);
                dongtai_selected.setVisibility(View.VISIBLE);
                gifts_pull_refresh_list.setVisibility(View.GONE);
                no_history.setVisibility(View.GONE);

                //添加勋章
                addMedal(mUserDetail);
                if (other){
                    xunzhang_selected.setVisibility(View.GONE);
                    rl_webView.setVisibility(View.GONE);
                }

                if (mList.size() == 0) {
                    mPullSearchListView.setVisibility(View.GONE);
                    no_jilu_txt.setVisibility(View.VISIBLE);
                } else {
                    mPullSearchListView.setVisibility(View.VISIBLE);
                    no_jilu_txt.setVisibility(View.GONE);
                }
                break;
            case R.id.look_history_sportrecord:
                String str1 = "动态（" + mUserDetail.getFind_count_num() + "）";
                int tempLength1 = str1.length();
                SpannableString ss1 = new SpannableString(str1);
                ss1.setSpan(new TextAppearanceSpan(PersonalPageMainActivity.this, R.style.jibu_style18_99),
                            0, 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                ss1.setSpan(new TextAppearanceSpan(PersonalPageMainActivity.this, R.style.jibu_style15_99),
                            2, tempLength1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                dongtai.setText(ss1);

                str1 = "运动记录（" + mUserDetail.getCount_num() + "）";
                tempLength1 = str1.length();
                ss1 = new SpannableString(str1);
                ss1.setSpan(new TextAppearanceSpan(PersonalPageMainActivity.this, R.style.jibu_style18_33),
                            0, 4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                ss1.setSpan(new TextAppearanceSpan(PersonalPageMainActivity.this, R.style.jibu_style15_33),
                            4, tempLength1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                look_history_sportrecord.setText(ss1);

                history_selected.setVisibility(View.VISIBLE);
                dongtai_selected.setVisibility(View.GONE);
                mPullSearchListView.setVisibility(View.GONE);
                no_jilu_txt.setVisibility(View.GONE);

                addMedal(mUserDetail);
                if (other){
                    xunzhang_selected.setVisibility(View.GONE);
                    rl_webView.setVisibility(View.GONE);
                }
                gifts_pull_refresh_list.setVisibility(View.VISIBLE);
                if (mYundongjiluList == null || mYundongjiluList.size() == 0) {
                    gifts_pull_refresh_list.setVisibility(View.GONE);
                    no_history.setVisibility(View.VISIBLE);
                } else {
                    gifts_pull_refresh_list.setVisibility(View.VISIBLE);
                    no_history.setVisibility(View.GONE);
                }
                break;
            case R.id.xunzhang:
                String str2 = "勋章（" + mUserDetail.getMedal_num() + "）";
                int tempLength2 = str2.length();
                SpannableString ss2 = new SpannableString(str2);
                ss2.setSpan(new TextAppearanceSpan(PersonalPageMainActivity.this, R.style.jibu_style18_33),
                        0, 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                ss2.setSpan(new TextAppearanceSpan(PersonalPageMainActivity.this, R.style.jibu_style15_33),
                        2, tempLength2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                xunzhang.setText(ss2);

                str2 = "动态（" + mUserDetail.getFind_count_num() + "）";
                tempLength2 = str2.length();
                ss2 = new SpannableString(str2);
                ss2.setSpan(new TextAppearanceSpan(PersonalPageMainActivity.this, R.style.jibu_style18_99),
                        0, 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                ss2.setSpan(new TextAppearanceSpan(PersonalPageMainActivity.this, R.style.jibu_style15_99),
                        2, tempLength2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                dongtai.setText(ss2);

                str2 = "运动记录（" + mUserDetail.getCount_num() + "）";
                tempLength2 = str2.length();
                ss = new SpannableString(str2);
                ss.setSpan(new TextAppearanceSpan(PersonalPageMainActivity.this, R.style.jibu_style18_99),
                        0, 4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                ss.setSpan(new TextAppearanceSpan(PersonalPageMainActivity.this, R.style.jibu_style15_99),
                        4, tempLength2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                look_history_sportrecord.setText(ss);

                xunzhang_selected.setVisibility(View.VISIBLE);
                rl_webView.setVisibility(View.VISIBLE);
                dongtai_selected.setVisibility(View.GONE);
                history_selected.setVisibility(View.GONE);
                mPullSearchListView.setVisibility(View.GONE);
                no_jilu_txt.setVisibility(View.GONE);
                gifts_pull_refresh_list.setVisibility(View.GONE);
                no_history.setVisibility(View.GONE);
                loadWebView();
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
        if (mSportsApp.isLogin() == false
                && (mSportsApp.getSessionId() == null || "".equals(mSportsApp
                .getSessionId()))) {
            Intent intent = new Intent(PersonalPageMainActivity.this,
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
                        // Log.e("--", "result-------"+result);
                        // waitProgressDialog.dismiss();
                        Toast.makeText(
                                PersonalPageMainActivity.this,
                                getResources().getString(
                                        R.string.sports_findgood_error),
                                Toast.LENGTH_SHORT).show();
                    } else {
                        // 成功
                        new Handler().postDelayed(new Runnable() {
                            public void run() {
                                if (status == 2) {
                                    mUserDetail.setFollowStatus(1);
                                    mSportsApp.getSportUser().setFollowCounts(
                                            mSportsApp.getSportUser()
                                                    .getFollowCounts() + 1);
                                    list_user_guanzhu.setBackgroundResource(R.drawable.quxiaoguanzhu);
                                    sendmessage();
                                } else {
                                    mUserDetail.setFollowStatus(2);
                                    list_user_guanzhu.setBackgroundResource(R.drawable.guanzhu);
                                    mSportsApp.getSportUser().setFollowCounts(
                                            mSportsApp.getSportUser()
                                                    .getFollowCounts() - 1);
                                    Toast.makeText(PersonalPageMainActivity.this, "取消关注成功！", Toast.LENGTH_SHORT)
                                            .show();
                                }

                            }
                        }, 1000);
                    }
                }

            }.execute();
        } else {
            Toast.makeText(PersonalPageMainActivity.this,
                    getResources().getString(R.string.error_cannot_access_net),
                    Toast.LENGTH_SHORT).show();
        }

    }

    private void sendmessage() {
        final String message = "Hi，我关注了爱运动的你，一起来运动吧！";
        // 启动异步发送消息
        if (mSportsApp.isOpenNetwork()) {
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
                                PersonalPageMainActivity.this,
                                PersonalPageMainActivity.this.getResources().getString(
                                        R.string.sports_top_failed),
                                Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(PersonalPageMainActivity.this, "关注成功！", Toast.LENGTH_SHORT)
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

    @Override
    public void returnFindid(String findId) {
        // TODO Auto-generated method stub
        // 活动删除的findid
        if (listFinid != null) {
            listFinid.add(findId);
        }
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
                Log.e(TAG, "length-------------" + list.size());
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
                    Log.e(TAG, "mList------------" + mList.size());
                    Log.i("zhonghuibin", "mList_top的长度：" + mList_top.size());
                    msg = Message
                            .obtain(mPersonalFindHandler, FRESH_LIST_TOP);
                    msg.sendToTarget();
                }
            } else {
                if (list == null) {
                    Log.d(TAG, "*******检z4********");
                    msg = Message.obtain(mPersonalFindHandler,
                            FRESH_FAILED_TOP);
                    msg.sendToTarget();
                }
            }
        }
    }

    class HistoryHandler extends Handler {

        @SuppressWarnings("unchecked")
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case FRESH_LIST_JILU:
                    if (mYundongjiluList == null || msg.obj == null)
                        return;
                    for (SportContionTaskDetail sportContionTaskDetail : (ArrayList<SportContionTaskDetail>) msg.obj) {
                        mYundongjiluList.add(sportContionTaskDetail);
                    }

//                    if (mYundongjiluAdapter == null) {
//                        mYundongjiluAdapter = new HistorySportAdpter(mYundongjiluList,
//                                PersonalPageMainActivity.this);
//                        mYundongjiluListView.setAdapter(mYundongjiluAdapter);
//                    }else {
//                        mYundongjiluAdapter.prepareTitles();
//                        mYundongjiluAdapter.notifyDataSetChanged();
//                    }
                    if(historySportAdapter==null){
                        historySportAdapter = new NewHistorySportAdapter(mYundongjiluList,PersonalPageMainActivity.this,R.layout.item_historical_record,0);
                        mYundongjiluListView.setAdapter(historySportAdapter);
                    } else {
                        mYundongjiluListView.requestLayout();
                        historySportAdapter.distanceSum();
                        historySportAdapter.notifyDataSetChanged();
                    }
//                    if (mProgressDialog.isShowing()) {
//                        mProgressDialog.dismiss();
//                    }
                    gifts_pull_refresh_list.onRefreshComplete();
                    mIsRefresh = false;
                    break;
                case NO_HISTORY:
                    no_history.setVisibility(View.VISIBLE);
                    if (mProgressDialog.isShowing()) {
                        mProgressDialog.dismiss();
                    }
                    gifts_pull_refresh_list.onRefreshComplete();
                    mIsRefresh = false;
                    break;
            }
        }

    }

    class GetUploadThread extends Thread {

        public GetUploadThread() {
        }

        @Override
        public void run() {
            List<SportContionTaskDetail> templist = new ArrayList<SportContionTaskDetail>();
            if (mIsRefresh) {
                if (mUid == mSportsApp.getSportUser().getUid()) {
                    SportSubTaskDB taskDBhelper = SportSubTaskDB
                            .getInstance(getApplicationContext());
                    if (taskDBhelper != null) {
                        templist = taskDBhelper.getTasks(mUid, mTimes);
                        Log.d("lou..", templist.size() + "");
                    }
                    if (templist != null && templist.size() > 0) {
                        mTimes++;
                    } else {
                        // 当本地没有了
                        if (mSportsApp.mIsNetWork) {
                            try {
                                templist = ApiJsonParser.getSportsTaskAll(
                                        mSportsApp.getSessionId(), mTimes, mUid);
                                if (templist != null) {
                                    for (SportContionTaskDetail sportContionTaskDetail : templist) {
                                        saveDate2DB(sportContionTaskDetail);
                                    }
                                    if (taskDBhelper != null) {
                                        templist = taskDBhelper.getTasks(
                                                mUid, mTimes);
                                        Log.d("lou..", templist.size() + "");
                                    }
                                    if (templist != null && templist.size() > 0) {
                                        mTimes++;
                                    }

                                }
                            } catch (ApiNetException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            } catch (ApiSessionOutException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }
                    }

                } else {
                    if (templist != null)
                        templist.clear();
                    if (mSportsApp.mIsNetWork) {
                        try {
                            templist = ApiJsonParser.getSportsTaskAll(
                                    mSportsApp.getSessionId(), mTimes, mUid);
                            Log.e("develop_debug", "templist.size : " + templist.size());
                        } catch (ApiNetException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        } catch (ApiSessionOutException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                    if (templist != null && templist.size() > 0) {
                        mTimes++;
                    }
                }

                Message message = new Message();
                message.what = FRESH_LIST_JILU;
                message.obj = templist;
                mHandler.sendMessage(message);
            }
        }
    }

    private void saveDate2DB(SportContionTaskDetail detail) {
        if (db != null) {
            Cursor cursor = db.newquery(mUid, detail.getStartTime(),
                    detail.getSportTime(), detail.getSportDistance() + "");
            try {
                if (!cursor.moveToFirst()) {
                    ContentValues values = new ContentValues();
                    values.put(SportSubTaskDB.UID, detail.getUserId());
                    values.put(SportSubTaskDB.SPORT_TYPE,
                            detail.getSports_type());
                    values.put(SportSubTaskDB.SPORT_SWIM_TYPE,
                            detail.getSwimType());
                    values.put(SportSubTaskDB.SPORT_DEVICE,
                            detail.getMonitoringEquipment());
                    values.put(SportSubTaskDB.SPORT_START_TIME,
                            detail.getStartTime());
                    values.put(SportSubTaskDB.SPORT_TIME, detail.getSportTime());
                    values.put(SportSubTaskDB.SPORT_DISTANCE,
                            detail.getSportDistance());
                    values.put(SportSubTaskDB.SPORT_SPEED,
                            detail.getSportVelocity());
                    values.put(SportSubTaskDB.SPORT_CALORIES,
                            detail.getSprots_Calorie());
                    values.put(SportSubTaskDB.SPORT_HEART_RATE,
                            detail.getHeartRate());
                    values.put(SportSubTaskDB.SPORT_LAT_LNG, detail.getLatlng());
                    values.put(SportSubTaskDB.SPORT_ISUPLOAD, 1);
                    values.put(SportSubTaskDB.SPORT_DATE, detail.getSportDate());
                    values.put(SportSubTaskDB.SPORT_TASKID, detail.getTaskid());
                    values.put(SportSubTaskDB.SPORT_STEP, detail.getStepNum());
                    values.put(SportSubTaskDB.SPORT_MAPTYPE,
                            detail.getMapType());

                    values.put(SportSubTaskDB.SPORT_MARKCODE,
                            detail.getSport_markcode());
                    db.insert(values, false);
                    Log.v(TAG,
                            "插入date到数据库成功 starttime =" + detail.getStartTime());
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (cursor != null) {
                    try {
                        cursor.close();
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                }
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case SPORT_DEL:
                    if (lastPosition >= 0) {
                        mYundongjiluList.remove(lastPosition);
//                        if (mYundongjiluAdapter != null) {
//                            mYundongjiluAdapter.prepareTitles();
//                            mYundongjiluAdapter.notifyDataSetChanged();
//                        }
                        if (historySportAdapter != null) {
                            historySportAdapter.distanceSum();
                            historySportAdapter.notifyDataSetChanged();
                        }
                    }
                    lastPosition = -1;
                    break;
            }
        }
        Log.v(TAG, "resultCode : " + requestCode);
    }

    /**
     *@method 是否显示勋章列表
     *@author suhu
     *@time 2016/11/21 14:53
     *@param imageDetail
     *other:两种状态
     *      true：别人的页面
     *      false：自己的页面
    */
    private boolean addMedal(UserDetail imageDetail){
        if (other){
            String str = "";
            int tempLength = 0;
            SpannableString ss;
            str = "勋章（" + imageDetail.getMedal_num() + "）";
            tempLength = str.length();
            ss = new SpannableString(str);
            ss.setSpan(new TextAppearanceSpan(PersonalPageMainActivity.this, R.style.jibu_style18_99),
                    0, 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            ss.setSpan(new TextAppearanceSpan(PersonalPageMainActivity.this, R.style.jibu_style15_99),
                    2, tempLength, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            xunzhang.setText(ss);
            return true;
        }else {
            return false;
        }
    }


    /**
     *@method 加载web信息
     *@author suhu
     *@time 2016/11/21 15:27
     *@param
     *
    */
    private void loadWebView() {
        if (!mSportsApp.isOpenNetwork()){
            medal_url = "file:///android_asset/offline/offline.html";
        }else {
            medal_url = url;
        }
        WebSettings webSettings = web_train.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT); // 设置 缓存模式
        // 开启 DOM storage API 功能
        webSettings.setDomStorageEnabled(true);
        // 开启 database storage API 功能
        webSettings.setDatabaseEnabled(true);
        String cacheDirPath = getFilesDir().getAbsolutePath()
                + APP_CACAHE_DIRNAME;
        Log.i(TAG, "cacheDirPath=" + cacheDirPath);
        // 设置数据库缓存路径
        webSettings.setDatabasePath(cacheDirPath);
        // 设置 Application Caches 缓存目录
        webSettings.setAppCachePath(cacheDirPath);
        // 开启 Application Caches 功能
        webSettings.setAppCacheEnabled(true);
        webSettings.setUserAgentString("mfox");
        web_train.setWebViewClient(new WebViewClient() {

            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }
        });
        web_train.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                if (progress == 100)
                    loading_layout.setVisibility(View.GONE);
            }
        });
        web_train.loadUrl(medal_url);
    }


    /**
     * 清除WebView缓存
     */
    public void clearWebViewCache() {
        // WebView 缓存文件
        File appCacheDir = new File(getFilesDir().getAbsolutePath()
                + APP_CACAHE_DIRNAME);
        // 删除webview 缓存目录
        if (appCacheDir.exists()) {
            deleteFile(appCacheDir);
        }
    }

    /**
     * 递归删除 文件/文件夹
     * @param file
     */
    public void deleteFile(File file) {
        if (file.exists()) {
            if (file.isFile()) {
                file.delete();
            } else if (file.isDirectory()) {
                File files[] = file.listFiles();
                for (int i = 0; i < files.length; i++) {
                    deleteFile(files[i]);
                }
            }
            file.delete();
        } else {
        }
    }


}
