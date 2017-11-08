package com.fox.exercise;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.ImageSpan;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fox.exercise.FindOtherMoreAdapter.OnCheckedChangeCommentListener;
import com.fox.exercise.api.ApiBack;
import com.fox.exercise.api.ApiConstant;
import com.fox.exercise.api.ApiJsonParser;
import com.fox.exercise.api.ApiNetException;
import com.fox.exercise.api.ApiSessionOutException;
import com.fox.exercise.api.entity.ActionList;
import com.fox.exercise.api.entity.ExpressionItems;
import com.fox.exercise.api.entity.NewCommentInfo;
import com.fox.exercise.api.entity.UserDetail;
import com.fox.exercise.bitmap.util.ImageResizer;
import com.fox.exercise.login.Tools;
import com.fox.exercise.newversion.entity.FindGroup;
import com.fox.exercise.newversion.entity.PraiseUsers;
import com.fox.exercise.newversion.entity.SportCircleComments;
import com.fox.exercise.newversion.entity.SportRecord;
import com.fox.exercise.newversion.entity.TopicContent;
import com.fox.exercise.newversion.newact.NewCommentsActivity;
import com.fox.exercise.pedometer.ImageWorkManager;
import com.fox.exercise.publish.SendMsgDetail;
import com.fox.exercise.util.RoundedImage;
import com.fox.exercise.util.ScrollLayout;
import com.fox.exercise.util.ScrollLayout.OnViewChangeListener;
import com.fox.exercise.view.PullToRefreshBase.OnRefreshListener;
import com.fox.exercise.view.PullToRefreshListView;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import cn.ingenic.indroidsync.SportsApp;

public class FindOtherFragment extends AbstractBaseFragment implements
        OnClickListener, OnDismissListener {
    private ListView listView_head;
    private List<ActionList> actionLists = new ArrayList<ActionList>();
    private List<String> activityNameList = new ArrayList<String>();
    private List<Integer> activityIdList = new ArrayList<Integer>();
    private SportsApp mSportsApp;
    private PullToRefreshListView mPullSearchListView = null;
    private ListView mListView = null;
    private Dialog mLoadProgressDialog = null;
    private Dialog dialog;
    private static final String TAG = "FindFragment";
    private TextView mDialogMessage;
    private int times = 0;
    private FindOtherMoreAdapter sportsfindmoreAdapter = null;
    private ArrayList<FindGroup> mList = new ArrayList<FindGroup>();
    private ArrayList<FindGroup> mList_top = new ArrayList<FindGroup>();
    private ImageView img_background;
    private LinearLayout roundimg_layout;
    public SportsFindMoreHandler msportsFindMoreHandler = null;
    private TextView myNameText;
    private TextView commentsNotify;
    private ImageView commentsImg;
    private LinearLayout messageLinearLayout;
    private RoundedImage userPhoto;
    private ImageWorkManager mImageWorkerMan_Icon;
    private ImageResizer mImageWorker_Icon;
    private UserDetail detail;
    private ImageButton iView;

    private RecordHelper mRecorder;
    private ImageButton findTextBtn;
    private Button findPressBtn;
    private boolean upTypeText = false;

    private LinearLayout layoutVoice;
    private RelativeLayout findLayout;
    private int findnumber = 0;
    private RelativeLayout rScrollLayout;
    private ScrollLayout scrollLayout;
    private LinearLayout imgLayout;
    private ImageView findExpressBtn;
    public static List<ExpressionItems> imgItems;
    public static List<ExpressionItems> imgItems2;
    private int mViewCount;
    private int mCurSel;
    private String[] imgStr;
    private String[] imgStr2;
    private static final float APP_PAGE_SIZE = 21.0f;
    private Boolean isShow;

    private LinearLayout findUpcommentText;
    private EditText findUpcommentEdittext;
    private Button findUpcommentSend;
    private Button findUnavailable;
    private static final int BACKGROUND_VIEW_ID = 66;
    private static final int TOP_TITLE_LAYOUT_ID = 77;
    private static final int USER_PHOTO_VIEW_ID = 88;
    private static final int IVEW_ID = 99;
    private final int FRESH_LIST = 111;// 更新成功
    private final int FRESH_FAILED = 112;// 更新失败
    private final int FRESH_DONE = 113;
    private final int FRESH_NULL = 114;
    public static final int FRESH_VIEW = 115;
    public static final int FRESH_PHOTO = 116;
    private static final int LEFT_TITLE_VIEW = 117;
    private static final int LEFT_TITLE_BUTTON = 118;
    public static final int HIDE_EDIT = 119;
    public static final int RETURN_MORE = 120;
    public static final int RETURN_DEL_RESULT = 121;
    public static final int RETURN_ADD_RESULT = 122;
    public static final int RETURN_ACTIVITYDEL_RESULT = 123;
    public static final int FRESH_NULL_TOP = 124;
    public static final int FRESH_LIST_TOP = 125;
    public static final int FRESH_FAILED_TOP = 126;
    public static final int FRESH_FIRST = 127;
    private int uid;// the one who send msg to me
    private SportsExceptionHandler mExceptionHandler = null;
    private int listPosition, toNumber;
    private String toNameStr;
    private String find_Id, to_Id;
    private boolean findBool = true;
    private SendMsgDetail self = null;
    private int bg_width;
    private int bg_height;
    private int screen_width;
    private ImageDownloader bg_Downloader = null;
    private String bg_urlString;
    private Dialog waitProgressDialog;
    private View head_view;
    private ResideMenu resideMenu;
    private View view;
    public boolean isFirst = false;//判断是不是第一次进入该页面
    public boolean isconnet;

    @Override
    public void beforeInitView() {
        // TODO Auto-generated method stub
        if(isAdded()){
            title = getResources().getString(R.string.friends_tab_faxian);
        }
    }

    private void showWaitDialog(String str) {
        waitProgressDialog = new Dialog(getActivity(), R.style.sports_dialog);
        LayoutInflater mInflater = (LayoutInflater) getActivity()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v1 = mInflater.inflate(R.layout.sports_progressdialog, null);
        TextView message = (TextView) v1.findViewById(R.id.message);
        message.setText(str);
        v1.setMinimumWidth((int) (SportsApp.ScreenWidth * 0.8));
        waitProgressDialog.setContentView(v1);
        waitProgressDialog.show();
    }

    @Override
    public void setViewStatus() {
        // TODO Auto-generated method stub
        // setContentView(R.layout.sports_find_fragment);
//		view2 = LayoutInflater.from(getActivity()).inflate(
//				R.layout.listview_head, null);
        view = LayoutInflater.from(getActivity()).inflate(
                R.layout.sports_find_fragment, null);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentViews(view);
        mSportsApp = (SportsApp) getActivity().getApplication();
        mExceptionHandler = mSportsApp.getmExceptionHandler();
        resideMenu = ((MainFragmentActivity) getActivity()).getResideMenu();
        // Bundle bundle = intent.getExtras();
        // uid = bundle.getInt("uid", 0);
        left_ayout.setId(LEFT_TITLE_VIEW);
        left_ayout.setOnClickListener(new rightOnClickListener());
        iButton.setId(LEFT_TITLE_BUTTON);
        iButton.setOnClickListener(new rightOnClickListener());
        bg_width = 480;
        bg_height = 294;
        screen_width = (int) (SportsApp.ScreenWidth);
        iView = new ImageButton(getActivity());
        iView.setBackgroundDrawable(getResources().getDrawable(
                R.drawable.sports_title_photo));
        iView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT));
        right_btn.setPadding(0, 0, mSportsApp.dip2px(17), 0);
        iView.setId(IVEW_ID);
        iView.setOnClickListener(new rightOnClickListener());
        showRightBtn(iView);
        this.bg_Downloader = new ImageDownloader(getActivity());
        bg_Downloader.setType(ImageDownloader.OnlyOne);
        init();
        detail = SportsApp.getInstance().getSportUser();
        mListView = mPullSearchListView.getRefreshableView();
        mListView.setDivider(null);
        mListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                // TODO Auto-generated method stub
                arg1.setSelected(true);
                if (findBool == false) {
                    findLayout.setVisibility(View.GONE);
                    mSportsApp.setSport_below_main(0);
                    findBool = true;
                    if (findnumber == 1) {
                        resideMenu.removeIgnoredView(findLayout);
                        findnumber = 0;
                    }
                }
                hideedit();
            }
        });
        mListView.setOnScrollListener(new OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView arg0, int arg1) {
                // TODO Auto-generated method stub
                if (findBool == false) {
                    findLayout.setVisibility(View.GONE);
                    mSportsApp.setSport_below_main(0);
                    findBool = true;
                }
                hideedit();
            }

            @Override
            public void onScroll(AbsListView arg0, int arg1, int arg2, int arg3) {
                // TODO Auto-generated method stub
            }
        });
        initView();
        head_view = LayoutInflater.from(getActivity()).inflate(
                R.layout.sports_find_headview, null);
        // 设置背景图片的宽度根据屏幕大小自适应
        img_background = (ImageView) head_view
                .findViewById(R.id.find_background_img);
        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) img_background
                .getLayoutParams();
        lp.width = screen_width;
        lp.height = (int) ((screen_width * bg_height) / bg_width);
        img_background.setLayoutParams(lp);
        if (SportsApp.getInstance().getSportUser().getFindimg() == null
                || SportsApp.getInstance().getSportUser().getFindimg()
                .equals("")
                || SportsApp.getInstance().getSportUser().getFindimg()
                .equals("null")) {
        } else {
            bg_urlString = ApiConstant.URL
                    + SportsApp.getInstance().getSportUser().getFindimg();
            bg_Downloader.download(bg_urlString, img_background, null);
        }
        img_background.setId(BACKGROUND_VIEW_ID);
        img_background.setOnClickListener(new rightOnClickListener());
        top_title_layout.setId(TOP_TITLE_LAYOUT_ID);
        top_title_layout.setOnClickListener(new rightOnClickListener());

        // 头像针对背景图片的位置根据屏幕大小自适应
        roundimg_layout = (LinearLayout) head_view.findViewById(R.id.mylayout);
        FrameLayout.LayoutParams lps = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT);
        int margin_top = (int) ((SportsApp.ScreenHeight / 3) * 0.6);
        lps.topMargin = margin_top;
        lps.gravity = Gravity.RIGHT;
        roundimg_layout.setLayoutParams(lps);
        // 设置名字长度
        myNameText = (TextView) head_view.findViewById(R.id.find_myname);
        LinearLayout.LayoutParams lp_name = (LinearLayout.LayoutParams) myNameText
                .getLayoutParams();
        lp_name.width = (int) (SportsApp.ScreenWidth * 0.42);
        myNameText.setLayoutParams(lp_name);
        myNameText.setText(detail.getUname());
        userPhoto = (RoundedImage) head_view
                .findViewById(R.id.cover_user_photo2);
        userPhoto.setId(USER_PHOTO_VIEW_ID);
        userPhoto.setOnClickListener(new rightOnClickListener());
        mImageWorkerMan_Icon = new ImageWorkManager(getActivity(), 0, 0);
        mImageWorker_Icon = mImageWorkerMan_Icon.getImageWorker();
        initPortrait();
        messageLinearLayout = (LinearLayout) head_view
                .findViewById(R.id.message_layout);
        messageLinearLayout.setVisibility(View.GONE);
        commentsNotify = (TextView) head_view
                .findViewById(R.id.tv_message_count);
        commentsNotify.setOnClickListener(FindOtherFragment.this);
        commentsImg = (ImageView) head_view.findViewById(R.id.img_newcommenter);
        mListView.addHeaderView(head_view);
        msportsFindMoreHandler = new SportsFindMoreHandler();// 更新
        mSportsApp.setFindHandler(msportsFindMoreHandler);


        mPullSearchListView.setOnRefreshListener(new OnRefreshListener() {

            @Override
            public void onRefresh(int pullDirection) {
                if (mSportsApp.isOpenNetwork()) {
                    switch (pullDirection) {
                        case FansAndNear.MODE_DEFAULT_LOAD:
                            times++;
                            SportsFindMoreThread loadThread = new SportsFindMoreThread();
                            loadThread.start();
                            GetNewCommnetCountTask loadNewCommentCount = new GetNewCommnetCountTask();
                            loadNewCommentCount.execute("");
                            break;
                        case FansAndNear.MODE_PULL_DOWN_TO_REFRESH:
                            times = 0;
                            SportsFindMoreThread refreshThread = new SportsFindMoreThread();
//                            SportsFindMoreThread_top refreshThread2 = new SportsFindMoreThread_top();
                            refreshThread.start();
//                            refreshThread2.start();
                            GetNewCommnetCountTask refreshNewCommentCount = new GetNewCommnetCountTask();
                            refreshNewCommentCount.execute("");
                            break;
                    }
                } else {
                    if(isAdded()){
                        Toast.makeText(
                                getActivity(),
                                getResources().getString(
                                        R.string.acess_server_error),
                                Toast.LENGTH_SHORT).show();
                    }
                    mPullSearchListView.onRefreshComplete();
//                    if (mLoadProgressDialog != null)
//                        if (mLoadProgressDialog.isShowing())
//                            mLoadProgressDialog.dismiss();
                }
            }
        });
        top_title_layout.setVisibility(View.GONE);
    }

    public void initView() {
        findLayout = (RelativeLayout) view.findViewById(
                R.id.find_bottom);
        findLayout.setVisibility(View.GONE);
        mSportsApp.setSport_below_main(0);
        layoutVoice = (LinearLayout) view.findViewById(
                R.id.layoutVoice);
        mRecorder = new RecordHelper();
        findPressBtn = (Button) view.findViewById(R.id.find_press_btn);
        findPressBtn.setVisibility(View.GONE);
        findPressBtn.setOnLongClickListener(new findClickListener());
        findPressBtn.setOnTouchListener(new findClickListener());
        findTextBtn = (ImageButton) view.findViewById(
                R.id.find_text_btn);
        findTextBtn.setOnClickListener(new findClickListener());

        findExpressBtn = (ImageButton) view.findViewById(
                (R.id.find_expression_text_btn_sportshow));

        findExpressBtn.setOnClickListener(new findClickListener());

        rScrollLayout = (RelativeLayout) view.findViewById(
                R.id.rScrollLayout);
        scrollLayout = (ScrollLayout) view.findViewById(
                R.id.ScrollLayoutTest);
        imgLayout = (LinearLayout) view.findViewById(R.id.imageLayot);

        scrollLayout.SetOnViewChangeListener(new OnViewChangeListener() {

            @Override
            public void OnViewChange(int view) {
                // TODO Auto-generated method stub
                setCurPoint(view);
            }
        });
        initViews();
        mCurSel = 0;
        isShow = false;
        ImageView img = (ImageView) imgLayout.getChildAt(mCurSel);
        img.setBackgroundResource(R.drawable.qita_biaoqing_04);

        findUpcommentText = (LinearLayout) view.findViewById(
                R.id.find_upcomment_text);
        findUpcommentSend = (Button) view.findViewById(
                R.id.find_upcomment_send);
        findUpcommentSend.setOnClickListener(new findClickListener());
        findUnavailable = (Button) view.findViewById(
                R.id.find_unavailable);
        findUpcommentEdittext = (EditText) view.findViewById(
                R.id.find_upcomment_edittext);

        findUpcommentEdittext.requestFocus();
        findUpcommentEdittext.setOnClickListener(new findClickListener());
        findUpcommentEdittext.addTextChangedListener(mTextWatcher);
        findUpcommentEdittext
                .setOnFocusChangeListener(new OnFocusChangeListener() {

                    @Override
                    public void onFocusChange(View arg0, boolean arg1) {
                        // TODO Auto-generated method stub
                        if (isShow == true) {
                            isShow = false;
                            rScrollLayout.setVisibility(View.GONE);
                        }
                    }
                });
//        if (mSportsApp.getSessionId() != null
//                && !mSportsApp.getSessionId().equals("")){
            new GetActionDataTask().execute();
            new GetActionDataTask2().execute();
//        }
    }


    // 等UI绘制好弹出软键盘
    private void showUIedit() {
        findUpcommentEdittext.setFocusable(true);
        findUpcommentEdittext.setFocusableInTouchMode(true);
        findUpcommentEdittext.requestFocus();
        Timer timer = new Timer();
        getActivity().findViewById(R.id.sportw_ll_belowmain).setVisibility(View.GONE);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                InputMethodManager inputManager = (InputMethodManager) findUpcommentEdittext
                        .getContext().getSystemService(
                                Context.INPUT_METHOD_SERVICE);
                inputManager.showSoftInput(findUpcommentEdittext, 0);
            }
        }, 300);
    }

    // 软键盘消失
    private void hideedit() {
        findUpcommentEdittext.clearFocus();
        InputMethodManager imm = (InputMethodManager) getActivity()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        getActivity().findViewById(R.id.sportw_ll_belowmain).setVisibility(View.VISIBLE);
        imm.hideSoftInputFromWindow(findUpcommentEdittext.getWindowToken(), 0);
    }

    class findClickListener implements OnClickListener, OnLongClickListener,
            OnTouchListener {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            // TODO Auto-generated method stub
            switch (event.getAction()) {
                case MotionEvent.ACTION_UP:
                    if (v == findPressBtn) {
                        mRecorder.stop();
                        if (dialog != null) {
                            dialog.dismiss();
                            dialog = null;
                            layoutVoice.setVisibility(View.GONE);
                            if (mRecorder.sampleLength() < 1) {
                                if(isAdded()){
                                    Toast.makeText(
                                            getActivity(),
                                            getResources().getString(
                                                    R.string.sports_record_fail),
                                            Toast.LENGTH_SHORT).show();
                                }
                                break;
                            }
                            sportsfindmoreAdapter.send(listPosition, toNameStr,
                                    find_Id, to_Id, findUpcommentEdittext.getText()
                                            .toString(), "/sdcard/Recording/"
                                            + (RecordHelper.mSampleFile).getName(),
                                    RecordHelper.mSampleLength + "");
                        }
                    }
                    break;

                default:
                    break;
            }
            return false;
        }

        @Override
        public boolean onLongClick(View v) {
            // TODO Auto-generated method stub
            if (v == findPressBtn) {
                mRecorder.startRecording(MediaRecorder.OutputFormat.DEFAULT,
                        ".mp3", getActivity());
                dialog = new Dialog(getActivity(), R.style.share_dialog2);
                layoutVoice.setVisibility(View.VISIBLE);
            }
            return true;
        }

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            switch (v.getId()) {
                case R.id.find_expression_text_btn_sportshow:
                    findPressBtn.setVisibility(View.GONE);
                    findUpcommentText.setVisibility(View.VISIBLE);
                    hideedit();
                    if (isShow == false) {
                        isShow = true;
                        rScrollLayout.setVisibility(View.VISIBLE);
                        if (!upTypeText) {
                            upTypeText = true;
                            findTextBtn.setBackgroundResource(R.drawable.sk2text);
                        }
                    } else if (isShow == true) {
                        isShow = false;
                        rScrollLayout.setVisibility(View.GONE);
                    }

                    break;
                case R.id.find_text_btn:
                    isShow = false;
                    rScrollLayout.setVisibility(View.GONE);
                    if (!upTypeText) {
                        upTypeText = true;
                        findPressBtn.setVisibility(View.GONE);
                        findUpcommentText.setVisibility(View.VISIBLE);
                        // showedit();
                        findTextBtn.setBackgroundResource(R.drawable.sk2text);
                    } else {
                        upTypeText = false;
                        findUpcommentText.setVisibility(View.GONE);
                        findTextBtn.setBackgroundResource(R.drawable.sk2voice);
                        findPressBtn.setVisibility(View.VISIBLE);
                        hideedit();
                    }
                    break;
                case R.id.find_upcomment_edittext:

                    if (isShow == true) {
                        isShow = false;
                        rScrollLayout.setVisibility(View.GONE);
                    }
                    break;
                case R.id.find_upcomment_send:
                    /**
                     * 第一个参数是评论所属发现在list中的位置 第二个参数用来当回复的时候显示回复给谁，如果直接评论则null 第三个发现ID
                     * 第四个用来当回复的时候传递to_id,如果直接评论则为null 其余三个分别是文本内容，音频，音频时间
                     * */
                    mSportsApp.settheFirstName("");
                    findLayout.setVisibility(View.GONE);
                    mSportsApp.setSport_below_main(0);
                    if (findnumber == 1) {
                        resideMenu.removeIgnoredView(findLayout);
                        findnumber = 0;
                    }

                    sportsfindmoreAdapter.send(listPosition, toNameStr, find_Id,
                            to_Id, findUpcommentEdittext.getText().toString(),
                            null, null);
                    findUpcommentEdittext.setText("");
                    findLayout.setVisibility(View.GONE);
                    mSportsApp.setSport_below_main(0);
                    break;
                default:
                    break;
            }
        }

    }

    TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
            // TODO Auto-generated method stub
            String editable = findUpcommentEdittext.getText().toString();
            String str = filterEmoji(editable.toString());
            if (!editable.equals(str)) {
                findUpcommentEdittext.setText(str);
                // 设置新的光标所在位置
                findUpcommentEdittext.setSelection(str.length());
                // 暂不支持此类型符号的输入
                if(isAdded()){
                    Toast.makeText(
                            getActivity(),
                            getActivity().getResources().getString(
                                    R.string.does_not_this_input),
                            Toast.LENGTH_SHORT).show();
                }
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
            String editable = findUpcommentEdittext.getText().toString();
            Boolean isTrue = stringFilter(editable.toString());
            int i = 0;
            if (isTrue) {
                // 除空格回车外有其他字符
                findUpcommentSend.setVisibility(View.VISIBLE);
                findUnavailable.setVisibility(View.GONE);
            } else {
                // 只有空格字符
                findUpcommentSend.setVisibility(View.GONE);
                findUnavailable.setVisibility(View.VISIBLE);
            }
        }
    };

    public static Boolean stringFilter(String str)
            throws PatternSyntaxException {
        // 监听是否有除空格外的其他字符
        String regEx = "[\\S*]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        if (m.find()) {
            return true;
        } else {
            return false;
        }
    }

    public static String filterEmoji(String source) {

        if (!containsEmoji(source)) {
            return source;// 如果不包含，直接返回
        }
        // 到这里铁定包含
        StringBuilder buf = null;

        int len = source.length();

        for (int i = 0; i < len; i++) {
            char codePoint = source.charAt(i);

            if (!isEmojiCharacter(codePoint)) {
                if (buf == null) {
                    buf = new StringBuilder(source.length());
                }

                buf.append(codePoint);
            } else {
            }
        }

        if (buf == null) {
            return "";
        } else {
            if (buf.length() == len) {// 这里的意义在于尽可能少的toString，因为会重新生成字符串
                buf = null;
                return source;
            } else {
                return buf.toString();
            }
        }

    }

    /**
     * 检测是否有emoji字符
     *
     * @param source
     * @return 一旦含有就抛出
     */
    public static boolean containsEmoji(String source) {
        if (source == null || source.equals("")) {
            return false;
        }

        int len = source.length();

        for (int i = 0; i < len; i++) {
            char codePoint = source.charAt(i);

            if (isEmojiCharacter(codePoint)) {
                // do nothing，判断到了这里表明，确认有表情字符
                return true;
            }
        }

        return false;
    }

    private static boolean isEmojiCharacter(char codePoint) {
        return !((codePoint == 0x0) || (codePoint == 0x9) || (codePoint == 0xA)
                || (codePoint == 0xD)
                || ((codePoint >= 0x20) && (codePoint <= 0xD7FF))
                || ((codePoint >= 0xE000) && (codePoint <= 0xFFFD)) || ((codePoint >= 0x10000) && (codePoint <= 0x10FFFF)));
    }

    public void initViews() {
        imgStr = getResources().getStringArray(R.array.imageStr_array);
        imgStr2 = getResources().getStringArray(R.array.daka_array);
        imgItems = new ArrayList<ExpressionItems>();
        imgItems2 = new ArrayList<ExpressionItems>();
        int j = 0, k = 0;
        Field[] files1 = R.drawable.class.getDeclaredFields();//打卡的图片列表
        for (Field file : files1) {
            if (file.getName().startsWith("daka")) {
                ApplicationInfo appInfo = getActivity().getApplicationInfo();
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
                ApplicationInfo appInfo = getActivity().getApplicationInfo();
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
        ExpressionItems item = new ExpressionItems();
        item.setId(R.drawable.qita_biaoqing_01);
        item.setName("itemCanel");
        item.setIsCanel(true);
        imgItems.add(item);
        final int Count = (int) Math.ceil(imgItems.size() / APP_PAGE_SIZE);
        mViewCount = Count;

        for (int i = 0; i < Count; i++) {
            GridView appPage = new GridView(getActivity());
            appPage.setAdapter(new ExpressionImgAdapter(getActivity(),
                    imgItems, i));
            appPage.setNumColumns(7);
            appPage.setHorizontalSpacing(10);
            appPage.setVerticalSpacing(10);
            appPage.setSelector(new ColorDrawable(Color.TRANSPARENT));
            appPage.setOnItemClickListener(listener);
            scrollLayout.addView(appPage);

            ImageView img = new ImageView(getActivity());
            img.setBackgroundResource(R.drawable.qita_biaoqing_03);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(10, 0, 10, 0);
            img.setLayoutParams(params);
            img.setEnabled(true);
            img.setTag(appPage.getId());
            imgLayout.addView(img);
        }
    }

    public OnItemClickListener listener = new OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            ExpressionItems item = (ExpressionItems) parent
                    .getItemAtPosition(position);
            if (item.getIsCanel()) {
                delStr(findUpcommentEdittext);
            } else {
                int d = item.getId();
                Drawable drawable = getResources().getDrawable(d);
                CharSequence cs = item.getName();
                SpannableString ss = new SpannableString(cs);
                if (drawable != null) {
                    Bitmap bitmap = BitmapFactory.decodeResource(getActivity()
                            .getResources(), d);
                    DisplayMetrics mDisplayMetrics = new DisplayMetrics();
                    getActivity().getWindowManager().getDefaultDisplay()
                            .getMetrics(mDisplayMetrics);
                    int Screenwidth = mDisplayMetrics.widthPixels;
                    int width = 0;
                    if (Screenwidth > 1000) {
                        width = Screenwidth * 23 / 100;
                    } else {
                        width = Screenwidth * 13 / 100;
                    }
                    bitmap = Bitmap.createScaledBitmap(bitmap, width, width,
                            true);
                    ImageSpan span = new ImageSpan(bitmap);
                    ss.setSpan(span, 0, ss.length(),
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    int cursor = findUpcommentEdittext.getSelectionStart();
                    findUpcommentEdittext.getText().insert(cursor, ss);
                }
            }
        }
    };

    private void delStr(EditText edit) {
        int selectionStart = edit.getSelectionStart();// 获取光标的位置
        if (selectionStart > 0) {
            String body = edit.getText().toString();
            if (!(body.length() == 0)) {
                String tempStr = body.substring(0, selectionStart);
                String zhengze = "\\[[^\\]]+\\]";
                // 通过传入的正则表达式来生成一个pattern
                Pattern sinaPatten = Pattern.compile(zhengze,
                        Pattern.CASE_INSENSITIVE);
                Matcher matcher = sinaPatten.matcher(tempStr);
                ArrayList<String> list = new ArrayList<String>();
                int t = 0;
                while (matcher.find()) {
                    list.add(matcher.group());
                    t++;
                }
                if (t > 0) {
                    String key = list.get(t - 1);
                    // 获取最后一个表情的位置
                    int i = tempStr.lastIndexOf("[");
                    String temp = tempStr.substring(i, selectionStart);
                    if (key.equals(temp)) {
                        edit.getEditableText().delete(i, selectionStart);
                        t--;
                    } else {
                        edit.getEditableText().delete(tempStr.length() - 1,
                                selectionStart);
                    }
                } else {
                    edit.getEditableText().delete(tempStr.length() - 1,
                            selectionStart);
                }
            }
        }
    }

    private void setCurPoint(int index) {
        if (index < 0 || index > mViewCount - 1 || mCurSel == index) {
            return;
        }
        ImageView v1 = (ImageView) imgLayout.getChildAt(mCurSel);
        v1.setBackgroundResource(R.drawable.qita_biaoqing_03);
        ImageView v2 = (ImageView) imgLayout.getChildAt(index);
        v2.setBackgroundResource(R.drawable.qita_biaoqing_04);
        mCurSel = index;
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

    // 跳转拍照发微博
    class rightOnClickListener implements OnClickListener {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            switch (v.getId()) {
                case LEFT_TITLE_VIEW:
                case LEFT_TITLE_BUTTON:
                    findLayout.setVisibility(View.GONE);
                    mSportsApp.setSport_below_main(0);
                    if (findnumber == 1) {
                        resideMenu.removeIgnoredView(findLayout);
                        findnumber = 0;
                    }
                    hideedit();
                    ResideMenu rm = ((MainFragmentActivity) getActivity())
                            .getResideMenu();
                    if (rm.isOpened())
                        rm.closeMenu();
                    else
                        rm.openMenu();
                    break;
                case IVEW_ID:
                    Intent intent = new Intent(getActivity(),
                            FindFriendsSendMsg.class);
                    getActivity().startActivity(intent);

                    break;
                case BACKGROUND_VIEW_ID:
                    findLayout.setVisibility(View.GONE);
                    mSportsApp.setSport_below_main(0);
                    if (findnumber == 1) {
                        resideMenu.removeIgnoredView(findLayout);
                        findnumber = 0;
                    }
                    hideedit();
                    findBool = true;
                    shotSelectImages();
                    break;
                case TOP_TITLE_LAYOUT_ID:
                    times = 0;
                    if (mSportsApp.isOpenNetwork()) {
                            SportsFindMoreThread refreshThread = new SportsFindMoreThread();
                            refreshThread.start();
                    } else {
                        if(isAdded()){
                            Toast.makeText(
                                    getActivity(),
                                    getResources().getString(
                                            R.string.acess_server_error),
                                    Toast.LENGTH_SHORT).show();
                        }
                        mListView.setSelectionFromTop(0, 0);
                        mPullSearchListView.onRefreshComplete();
//                        if (mLoadProgressDialog != null)
//                            if (mLoadProgressDialog.isShowing())
//                                mLoadProgressDialog.dismiss();
                    }
                    break;
                case USER_PHOTO_VIEW_ID:
                    Intent intents = new Intent(getActivity(), FindMeActivity.class);
                    getActivity().startActivity(intents);
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onPageResume() {
        if (sportsfindmoreAdapter != null && mList.size() != 0) {
            sportsfindmoreAdapter.notifyDataSetChanged();
            mPullSearchListView.onRefreshComplete();
        }

        // TODO Auto-generated method stub
        self = mSportsApp.getmSendMsgDetail();
        detail = SportsApp.getInstance().getSportUser();
        if (self != null) {
            int position = mListView.getSelectedItemPosition();
            FindGroup f2 = new FindGroup();
            f2.setOtheruid(detail.getUid());
            f2.setSex(detail.getSex());
            f2.setOthername(detail.getUname());
            f2.setOtherimg(detail.getUimg());
            f2.setDetils(self.getMethod_str());
            String[] imgs = self.getUrls();
            f2.setImgs(imgs);
            f2.setBiggerImgs(self.getBigurls());
            f2.setTimes(self.getTimes() / 1000);
            f2.setFindId(self.getFindId());
            f2.setIsFriends(2);
            if (imgs != null) {
                if (imgs.length == 1) {
                    f2.setWidth(self.getWidth());
                    f2.setHeight(self.getHeight());
                }
            }
            f2.setTopicList(self.getTopicList());
            f2.setComefrom(self.getComeFrom());

            if (mList_top != null) {
                if (mList_top.size() == 0) {
                    mList.add(0, f2);
                } else if (mList_top.size() == 1) {
                    mList.add(1, f2);
                } else if (mList_top.size() == 2) {
                    mList.add(2, f2);
                } else {
                    mList.add(3, f2);
                }
            }


            mListView.setSelection(position + 1);
            if (sportsfindmoreAdapter != null && mList.size() != 0) {
                sportsfindmoreAdapter.notifyDataSetChanged();
                mPullSearchListView.onRefreshComplete();
            }
            mSportsApp.setmSendMsgDetail(null);
        }
        MobclickAgent.onPageStart("FindOtherFragment");
    }

    @Override
    public void onPagePause() {
        MobclickAgent.onPageEnd("FindOtherFragment");
    }

    @Override
    public void onPageDestroy() {
        if (mLoadProgressDialog != null)
            if (mLoadProgressDialog.isShowing())
                mLoadProgressDialog.dismiss();
        drawable = null;
    }

    private void initPortrait() {
        mImageWorker_Icon.setLoadingImage("man".equals(SportsApp.getInstance()
                .getSportUser().getSex()) ? R.drawable.sports_user_edit_portrait_male
                : R.drawable.sports_user_edit_portrait);
        mImageWorker_Icon.loadImage(SportsApp.getInstance().getSportUser()
                .getUimg(), userPhoto, null, null, false);
    }

    private void init() {
        // TODO Auto-generated method stub
        mPullSearchListView = (PullToRefreshListView) view
                .findViewById(R.id.sports_find_refresh_list);
        pop_menu_background = (RelativeLayout) view.findViewById(
                R.id.send_menu_background);
    }

    private void waitShowDialog() {
        // TODO Auto-generated method stub
        if (mLoadProgressDialog == null) {
            mLoadProgressDialog = new Dialog(getActivity(),
                    R.style.sports_dialog);
            LayoutInflater mInflater = getActivity().getLayoutInflater();
            View v1 = mInflater.inflate(R.layout.bestgirl_progressdialog, null);
            mDialogMessage = (TextView) v1.findViewById(R.id.message);
            mDialogMessage.setText(R.string.bestgirl_wait);
            v1.setMinimumWidth((int) (SportsApp.ScreenWidth * 0.8));
            mLoadProgressDialog.setContentView(v1);
        }
//        if (mLoadProgressDialog != null)
//            if (!mLoadProgressDialog.isShowing()
//                    && !getActivity().isFinishing())
//                mLoadProgressDialog.show();
    }

    private View getViewByPosition(int pos, ListView listView) {
        final int firstListItemPosition = listView.getFirstVisiblePosition();
        final int lastListItemPosition = firstListItemPosition
                + listView.getChildCount() - 1;

        if (pos < firstListItemPosition || pos > lastListItemPosition) {
            return listView.getAdapter().getView(pos, null, listView);
        } else {
            final int childIndex = pos - firstListItemPosition;
            return listView.getChildAt(childIndex);
        }
    }

    class SportsFindMoreHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            switch (msg.what) {
                case FRESH_LIST:
                    if (times == 0) {
                        mListView.setAdapter(null);
                        //对集合进行监听、不能重复出现取消置顶的按钮。
                        if (mList.size() != 0) {
                            if (mList.size() % 2 != 0) {
                                for (int i = 0; i < mList.size(); i++) {
                                    mList.get(i).setFlog(0);
                                }
                                if (mList.size() != 0) {
                                    mList.get(0).setFlog(2);
                                }
                            }
                        }
                        if (mList != null && mList.size() != 0) {
                            sportsfindmoreAdapter = new FindOtherMoreAdapter(
                                    getActivity(), actionLists, mList, mSportsApp, imgItems, imgItems2, 1);

                            mListView.setAdapter(sportsfindmoreAdapter);
                            sportsfindmoreAdapter.setOnCheckedChangeListener(new OnCheckedChangeCommentListener() {

                                @Override
                                public void OnCheckedChangeListener(
                                        int position, String findId,
                                        int number, String to_id, String toName) {
                                    // TODO Auto-generated method stub
                                    listPosition = position;
                                    find_Id = findId;
                                    toNumber = number;
                                    to_Id = to_id;
                                    toNameStr = toName;
                                    if (findBool == true) {
                                        findBool = false;
                                        int ss = findUpcommentText
                                                .getVisibility();
                                        if (ss == 0) {
                                            showUIedit();
                                        }
                                        if (number == 1) {
                                            //管理员直接点击我也说一句，这这边需要加”评论“
                                            findUpcommentEdittext.setText(null);
                                            findUpcommentEdittext
                                                    .setHint("评论");
                                            findLayout
                                                    .setVisibility(View.VISIBLE);

                                            upTypeText = true;
                                            findPressBtn.setVisibility(View.GONE);
                                            findUpcommentText.setVisibility(View.VISIBLE);
                                            showUIedit();
                                            findTextBtn.setBackgroundResource(R.drawable.sk2text);

                                            mSportsApp.setSport_below_main(1);
                                            if (findnumber == 0) {
                                                resideMenu
                                                        .addIgnoredView(findLayout);
                                                findnumber = 1;
                                            }
                                        }
                                        if (number == 2) {
                                            // 回复、管理员回复
                                            findUpcommentEdittext.setText(null);
                                            if(isAdded()){
                                                findUpcommentEdittext
                                                        .setHint(getActivity()
                                                                .getResources()
                                                                .getString(
                                                                        R.string.multi_comment_tip_target)
                                                                + toName + ":");
                                            }
                                            findLayout
                                                    .setVisibility(View.VISIBLE);

                                            upTypeText = true;
                                            findPressBtn.setVisibility(View.GONE);
                                            findUpcommentText.setVisibility(View.VISIBLE);
                                            showUIedit();
                                            findTextBtn.setBackgroundResource(R.drawable.sk2text);
                                            mSportsApp.setSport_below_main(1);
                                            if (findnumber == 0) {
                                                resideMenu
                                                        .addIgnoredView(findLayout);
                                                findnumber = 1;
                                            }
                                        }
                                        scrollToComment(position);
                                    } else {
                                        findLayout.setVisibility(View.GONE);
                                        mSportsApp.setSport_below_main(0);
                                        if (findnumber == 1) {
                                            resideMenu
                                                    .removeIgnoredView(findLayout);
                                            findnumber = 0;
                                        }
                                        hideedit();
                                        findBool = true;
                                    }
                                }
                            });
                        }
                        if (sportsfindmoreAdapter != null && mList.size() != 0) {
                            sportsfindmoreAdapter.notifyDataSetChanged();
                            mPullSearchListView.onRefreshComplete();
                        }
                    } else {
                        if (sportsfindmoreAdapter == null && mList.size() != 0) {
                            if (mList != null && mList.size() != 0) {
                                sportsfindmoreAdapter = new FindOtherMoreAdapter(
                                        getActivity(), actionLists, mList, mSportsApp, imgItems, imgItems2, 1);
                            }
                            sportsfindmoreAdapter
                                    .setOnCheckedChangeListener(new OnCheckedChangeCommentListener() {
                                        /**
                                         * (position,findIdString,2,commentID,theFirstName)
                                         * 第一个参数所属发现在list中位置
                                         * 第二个参数发现ID
                                         * 第三个参数用来判断评论还是回复1代表直接评论2代表回复
                                         * 第四个参数用来当回复的时候传递to_id,如果直接评论则为null
                                         * 第五个参数用来当回复的时候显示回复给谁，如果直接评论则null
                                         */
                                        @Override
                                        public void OnCheckedChangeListener(
                                                int position, String findId,
                                                int number, String to_id,
                                                String toName) {
                                            // TODO Auto-generated method stub
                                            listPosition = position;
                                            find_Id = findId;
                                            toNumber = number;
                                            to_Id = to_id;
                                            toNameStr = toName;
                                            if (findBool == true) {
                                                findBool = false;
                                                int ss = findUpcommentText
                                                        .getVisibility();
                                                if (ss == 0) {
                                                    showUIedit();
                                                }
                                                if (number == 1) {
                                                    findUpcommentEdittext
                                                            .setHint("评论");
                                                    findLayout
                                                            .setVisibility(View.VISIBLE);
                                                    upTypeText = true;
                                                    findPressBtn.setVisibility(View.GONE);
                                                    findUpcommentText.setVisibility(View.VISIBLE);
                                                    showUIedit();
                                                    findTextBtn.setBackgroundResource(R.drawable.sk2text);
                                                    mSportsApp.setSport_below_main(1);
                                                    if (findnumber == 0) {
                                                        resideMenu
                                                                .addIgnoredView(findLayout);
                                                        findnumber = 1;
                                                    }
                                                }
                                                if (number == 2) {
                                                    // 回复XX
                                                    if(isAdded()){
                                                        findUpcommentEdittext
                                                                .setHint(getActivity()
                                                                        .getResources()
                                                                        .getString(
                                                                                R.string.multi_comment_tip_target)
                                                                        + toName + ":");
                                                    }
                                                    findLayout
                                                            .setVisibility(View.VISIBLE);
                                                    upTypeText = true;
                                                    findPressBtn.setVisibility(View.GONE);
                                                    findUpcommentText.setVisibility(View.VISIBLE);
                                                    showUIedit();
                                                    findTextBtn.setBackgroundResource(R.drawable.sk2text);
                                                    mSportsApp.setSport_below_main(1);
                                                    if (findnumber == 0) {
                                                        resideMenu
                                                                .addIgnoredView(findLayout);
                                                        findnumber = 1;
                                                    }
                                                }
                                                scrollToComment(position);
                                            } else {
                                                findLayout.setVisibility(View.GONE);
                                                mSportsApp.setSport_below_main(0);
                                                hideedit();
                                                findBool = true;
                                                if (findnumber == 1) {
                                                    resideMenu
                                                            .removeIgnoredView(findLayout);
                                                    findnumber = 0;
                                                }
                                            }
                                        }
                                    });
                        }
                        if (sportsfindmoreAdapter != null && mList.size() != 0) {
                            sportsfindmoreAdapter.notifyDataSetChanged();
                            mPullSearchListView.onRefreshComplete();
                        }
                    }
//                    if (mLoadProgressDialog != null)
//                        if (mLoadProgressDialog.isShowing())
//                            mLoadProgressDialog.dismiss();
                    break;

//                case FRESH_FAILED:
//                    if (times > 0) {
//                        times = times - 1;
//                    }
//                    if (!mSportsApp.isOpenNetwork()) {
//                        Toast.makeText(
//                                getActivity(),
//                                getResources().getString(
//                                        R.string.acess_server_error),
//                                Toast.LENGTH_SHORT).show();
//                    } else if (getActivity() != null) {
//                        Toast.makeText(
//                                getActivity(),
//                                getActivity().getResources().getString(
//                                        R.string.sports_get_list_failed2),
//                                Toast.LENGTH_SHORT).show();
//                    }
//                    if (mLoadProgressDialog != null)
//                        if (mLoadProgressDialog.isShowing())
//                        mPullSearchListView.onRefreshComplete();
//                    break;
                case FRESH_FAILED:
                    if (times > 0) {
                        times = times - 1;
                    }
                    if (!mSportsApp.isOpenNetwork()) {
                        if(isAdded()){
                            Toast.makeText(
                                    getActivity(),
                                    getActivity().getResources().getString(
                                            R.string.acess_server_error),
                                    Toast.LENGTH_SHORT).show();
                        }
                    } else if (getActivity() != null) {
                        if(isAdded()){
                            Toast.makeText(
                                    getActivity(),
                                    getActivity().getResources().getString(
                                            R.string.sports_get_list_failed2),
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                    //弱网刷新没有拉取到数据，直接加载缓存数据
                    mList = getmList();
                    if (sportsfindmoreAdapter != null){
                        sportsfindmoreAdapter.notifyDataSetChanged();
                        mPullSearchListView.onRefreshComplete();
                    }
                    break;
                case FRESH_NULL:
                    if (getActivity() != null) {
                        if (mList.size() != 0) {
                            if(isAdded()){
                                Toast.makeText(
                                        getActivity(),
                                        getActivity().getResources().getString(
                                                R.string.sports_data_load_more_null),
                                        Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            if(isAdded()){
                                Toast.makeText(
                                        getActivity(),
                                        getActivity().getResources().getString(
                                                R.string.sports_upload_find_new),
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
//                    if (mLoadProgressDialog != null)
//                        if (mLoadProgressDialog.isShowing())
//                            mLoadProgressDialog.dismiss();
//                    if (sportsfindmoreAdapter == null && mList.size() != 0) {
//                        mListView.setAdapter(null);
//                        //对集合进行监听、不能重复出现取消置顶的按钮。
//                        if (mList.size() != 0) {
//                            if (mList.size() % 2 != 0) {
//                                for (int i = 0; i < mList.size(); i++) {
//                                    mList.get(i).setFlog(0);
//                                }
//                                mList.get(0).setFlog(2);
//                            }
//                        }
//                        if (mList != null && mList.size() != 0) {
//                            sportsfindmoreAdapter = new FindOtherMoreAdapter(
//                                    getActivity(), actionLists, mList, mSportsApp, imgItems, imgItems2, 1);
//                            mListView.setAdapter(sportsfindmoreAdapter);
//                        }
//                    }
                    if (sportsfindmoreAdapter != null && mList.size() != 0) {
                        sportsfindmoreAdapter.notifyDataSetChanged();
                        mPullSearchListView.onRefreshComplete();
                    }
                    break;

                case FRESH_LIST_TOP:
                    if (mList_top != null && mList_top.size() != 0) {
                        int position = mListView.getSelectedItemPosition();
                        FindGroup f3 = mList_top.get(0);
                        f3.setFlog(2);
                        for (int i = 0; i < mList_top.size(); i++) {
                            mList.add(i, mList_top.get(i));
                            mListView.setSelection(position + 1);
                            if (sportsfindmoreAdapter != null && mList.size() != 0) {
                                sportsfindmoreAdapter.notifyDataSetChanged();
                                mPullSearchListView.onRefreshComplete();
                            }
                        }
                    }
                    break;
                case FRESH_NULL_TOP:
                    break;
                case FRESH_FAILED_TOP:
                    break;

                case FRESH_DONE:
                    GetNewCommnetCountTask newCommentCount = new GetNewCommnetCountTask();
                    newCommentCount.execute("");
                    if (sportsfindmoreAdapter != null && mList.size() != 0)
                        sportsfindmoreAdapter.notifyDataSetChanged();
                    break;
                case FRESH_VIEW:
                    waitShowDialog();
                    SportsFindMoreThread sportsFindMoreThread_two = new SportsFindMoreThread();
//                    SportsFindMoreThread_top sportsFindMoreThread_two2 = new SportsFindMoreThread_top();
                    if (mSportsApp.isOpenNetwork()) {
                        sportsFindMoreThread_two.start();
//                        sportsFindMoreThread_two2.start();
                    } else {
                        if(getActivity()!=null&&isAdded()){
                            Toast.makeText(
                                    getActivity(),
                                    getActivity().getResources().getString(
                                            R.string.acess_server_error),
                                    Toast.LENGTH_SHORT).show();
                        }
                        mPullSearchListView.onRefreshComplete();
//                        if (mLoadProgressDialog != null)
//                            if (mLoadProgressDialog.isShowing())
//                                mLoadProgressDialog.dismiss();
                    }
                    break;
                case FRESH_PHOTO:
                    Intent data = (Intent) msg.obj;
                    Bundle bundle = data.getBundleExtra("useredit");
                    boolean isfacechanged = bundle.getBoolean("isfacechanged");
                    String username = bundle.getString("username");
                    if (isfacechanged) {
                        initPortrait();
                    }
                    myNameText.setText(username);
                    break;
                case HIDE_EDIT:
                    findLayout.setVisibility(View.GONE);
                    mSportsApp.setSport_below_main(0);
                    findBool = true;
                    hideedit();
                    break;

                case RETURN_MORE:
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
                        }
                    }
                    if (sportsfindmoreAdapter != null && mList.size() != 0) {
                        sportsfindmoreAdapter.notifyDataSetChanged();
                    }
                    break;
                case RETURN_DEL_RESULT:
                    List<String> listFinid = (List<String>) msg.obj;
                    if (listFinid != null) {
                        if (listFinid.size() > 0) {
                            for (int i = 0; i < mList.size(); i++) {
                                for (int j = 0; j < listFinid.size(); j++) {
                                    if (mList.get(i).getFindId()
                                            .equals(listFinid.get(j))) {
                                        mList.remove(i);
                                    }
                                }
                            }
                            if (sportsfindmoreAdapter != null  && mList.size() != 0) {
                                sportsfindmoreAdapter.notifyDataSetChanged();
                            }
                        }
                    }
                    break;
                case RETURN_ADD_RESULT:
                    break;

                case RETURN_ACTIVITYDEL_RESULT:
                    // 从动态页面删除动态后返回的结果
                    String findId = (String) msg.obj;
                    if (findId != null && !"".equals(findId)) {
                        for (int i = 0; i < mList.size(); i++) {
                            if (mList.get(i).getFindId().equals(findId)) {
                                mList.remove(i);
                            }
                        }
                        if (sportsfindmoreAdapter != null && mList.size() != 0) {
                            sportsfindmoreAdapter.notifyDataSetChanged();
                        }
                    }
                    break;

                case FRESH_FIRST:
                    if (times == 0) {
                        mListView.setAdapter(null);
                        //对集合进行监听、不能重复出现取消置顶的按钮。
                        if (mList.size() % 2 != 0) {
                            for (int i = 0; i < mList.size(); i++) {
                                mList.get(i).setFlog(0);
                            }
                            if (mList.size() != 0) {
                                mList.get(0).setFlog(2);
                            }
                        }
                        if (mList != null && mList.size() != 0) {
                            sportsfindmoreAdapter = new FindOtherMoreAdapter(
                                    getActivity(), actionLists, mList, mSportsApp, imgItems, imgItems2, 1);
                        }
                        mListView.setAdapter(sportsfindmoreAdapter);
                        sportsfindmoreAdapter.setOnCheckedChangeListener(new OnCheckedChangeCommentListener() {

                            @Override
                            public void OnCheckedChangeListener(
                                    int position, String findId,
                                    int number, String to_id, String toName) {
                                // TODO Auto-generated method stub
                                listPosition = position;
                                find_Id = findId;
                                toNumber = number;
                                to_Id = to_id;
                                toNameStr = toName;
                                if (findBool == true) {
                                    findBool = false;
                                    int ss = findUpcommentText
                                            .getVisibility();
                                    if (ss == 0) {
                                        showUIedit();
                                    }
                                    if (number == 1) {
                                        //管理员直接点击我也说一句，这这边需要加”评论“
                                        findUpcommentEdittext.setText(null);
                                        findUpcommentEdittext
                                                .setHint("评论");
                                        findLayout
                                                .setVisibility(View.VISIBLE);
                                        mSportsApp.setSport_below_main(1);
                                        if (findnumber == 0) {
                                            resideMenu
                                                    .addIgnoredView(findLayout);
                                            findnumber = 1;
                                        }
                                    }
                                    if (number == 2) {
                                        // 回复、管理员回复
                                        findUpcommentEdittext.setText(null);
                                        if(isAdded()){
                                            findUpcommentEdittext
                                                    .setHint(getActivity()
                                                            .getResources()
                                                            .getString(
                                                                    R.string.multi_comment_tip_target)
                                                            + toName + ":");
                                        }
                                        findLayout
                                                .setVisibility(View.VISIBLE);
                                        mSportsApp.setSport_below_main(1);
                                        if (findnumber == 0) {
                                            resideMenu
                                                    .addIgnoredView(findLayout);
                                            findnumber = 1;
                                        }
                                    }
                                    scrollToComment(position);
                                } else {
                                    findLayout.setVisibility(View.GONE);
                                    mSportsApp.setSport_below_main(0);
                                    if (findnumber == 1) {
                                        resideMenu
                                                .removeIgnoredView(findLayout);
                                        findnumber = 0;
                                    }
                                    hideedit();
                                    findBool = true;
                                }
                            }
                        });
                        if (sportsfindmoreAdapter != null && mList.size() != 0) {
                            sportsfindmoreAdapter.notifyDataSetChanged();
                            mPullSearchListView.onRefreshComplete();
                        }
                    } else {
                        if (sportsfindmoreAdapter == null) {
                            if (mList != null && mList.size() != 0) {
                                sportsfindmoreAdapter = new FindOtherMoreAdapter(
                                        getActivity(), actionLists, mList, mSportsApp, imgItems, imgItems2, 1);
                            }
                            sportsfindmoreAdapter
                                    .setOnCheckedChangeListener(new OnCheckedChangeCommentListener() {
                                        /**
                                         * (position,findIdString,2,commentID,theFirstName)
                                         * 第一个参数所属发现在list中位置
                                         * 第二个参数发现ID
                                         * 第三个参数用来判断评论还是回复1代表直接评论2代表回复
                                         * 第四个参数用来当回复的时候传递to_id,如果直接评论则为null
                                         * 第五个参数用来当回复的时候显示回复给谁，如果直接评论则null
                                         */
                                        @Override
                                        public void OnCheckedChangeListener(
                                                int position, String findId,
                                                int number, String to_id,
                                                String toName) {
                                            // TODO Auto-generated method stub
                                            listPosition = position;
                                            find_Id = findId;
                                            toNumber = number;
                                            to_Id = to_id;
                                            toNameStr = toName;
                                            if (findBool == true) {
                                                findBool = false;
                                                int ss = findUpcommentText
                                                        .getVisibility();
                                                if (ss == 0) {
                                                    showUIedit();
                                                }
                                                if (number == 1) {
                                                    findUpcommentEdittext
                                                            .setHint("评论");
                                                    findLayout
                                                            .setVisibility(View.VISIBLE);
                                                    mSportsApp.setSport_below_main(1);
                                                    if (findnumber == 0) {
                                                        resideMenu
                                                                .addIgnoredView(findLayout);
                                                        findnumber = 1;
                                                    }
                                                }
                                                if (number == 2) {
                                                    // 回复XX
                                                    if(isAdded()){
                                                        findUpcommentEdittext
                                                                .setHint(getActivity()
                                                                        .getResources()
                                                                        .getString(
                                                                                R.string.multi_comment_tip_target)
                                                                        + toName + ":");
                                                    }
                                                    findLayout
                                                            .setVisibility(View.VISIBLE);
                                                    mSportsApp.setSport_below_main(1);
                                                    if (findnumber == 0) {
                                                        resideMenu
                                                                .addIgnoredView(findLayout);
                                                        findnumber = 1;
                                                    }
                                                }
                                                scrollToComment(position);
                                            } else {
                                                findLayout.setVisibility(View.GONE);
                                                mSportsApp.setSport_below_main(0);
                                                hideedit();
                                                findBool = true;
                                                if (findnumber == 1) {
                                                    resideMenu
                                                            .removeIgnoredView(findLayout);
                                                    findnumber = 0;
                                                }
                                            }
                                        }
                                    });
                        }
                        if (sportsfindmoreAdapter != null && mList.size() != 0) {
                            sportsfindmoreAdapter.notifyDataSetChanged();
                            mPullSearchListView.onRefreshComplete();
                        }
                    }
//                    if (mLoadProgressDialog != null)
//                        if (mLoadProgressDialog.isShowing())
//                            mLoadProgressDialog.dismiss();
                    if (mSportsApp.isOpenNetwork()) {
                        if (mSportsApp.getSessionId() != null
                                && !mSportsApp.getSessionId().equals("")) {
                            SportsFindMoreThread sportsFindMoreThread = new SportsFindMoreThread();
//                            SportsFindMoreThread_top sportsFindMoreThread2 = new SportsFindMoreThread_top();
                            sportsFindMoreThread.start();
//                            sportsFindMoreThread2.start();

                            GetNewCommnetCountTask newCommentCount1 = new GetNewCommnetCountTask();
                            newCommentCount1.execute("");
                        }
                    }
                    break;
                default:
                    break;
            }
        }

        private void scrollToComment(int position) {
            // TODO Auto-generated method stub
            final Rect mrect = new Rect();
            View item = getViewByPosition(position + 1, mListView);
            item.getGlobalVisibleRect(mrect);
            postDelayed(new Runnable() {
                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    Rect frect = new Rect();
                    findLayout.getGlobalVisibleRect(frect);
                    scrollVertical(mrect.bottom - frect.top);
                }
            }, 500); // 延时500毫秒,等findLayout显示完，并且软键盘弹出后再滑动
        }
    }

    /**
     * scroll Vertical
     *
     * @param y 垂直滑动的距离
     */
    private void scrollVertical(final int y) {
        if (mListView == null)
            return;
        getActivity().runOnUiThread(new Runnable() { // 执行自动化测试的时候模拟滑动需要进入UI线程操作
            @Override
            public void run() {
                invokeMethod(mListView, "trackMotionScroll",
                        new Object[]{-y, -y}, new Class[]{
                                int.class, int.class});
            }
        });
    }

    /**
     * 遍历当前类以及父类去查找方法
     *
     * @param object
     * @param methodName
     * @param params
     * @param paramTypes
     * @return
     */
    private Object invokeMethod(Object object, String methodName,
                                Object[] params, Class[] paramTypes) {
        Object returnObj = null;
        if (object == null) {
            return null;
        }
        Class cls = object.getClass();
        Method method = null;
        for (; cls != Object.class; cls = cls.getSuperclass()) { // 因为取的是父类的默认修饰符的方法，所以需要循环找到该方法
            try {
                method = cls.getDeclaredMethod(methodName, paramTypes);
                break;
            } catch (NoSuchMethodException e) {
            } catch (SecurityException e) {
            }
        }
        if (method != null) {
            method.setAccessible(true);
            try {
                returnObj = method.invoke(object, params);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return returnObj;
    }

    class SportsFindMoreThread extends Thread {
        private long startTime = System.currentTimeMillis();

        @Override
        public void run() {
            // TODO Auto-generated method stub
            super.run();
            Message msg = null;
            ArrayList<FindGroup> list = new ArrayList<FindGroup>();
            try {
                list = (ArrayList<FindGroup>) ApiJsonParser.getNewFindList(
                        mSportsApp.getSessionId(), times, mSportsApp
                                .getSportUser().getUid() + "", false, 0);
            } catch (ApiNetException e) {
                e.printStackTrace();
            } catch (ApiSessionOutException e) {
                e.printStackTrace();
            }
            if (times == 0) {
                mList.clear();
            }
            if (list != null) {
                if (list.size() == 0) {
                    msg = Message.obtain(msportsFindMoreHandler, FRESH_NULL);
                    msg.sendToTarget();
                } else {
                    for (FindGroup e : list) {
                        mList.add(e);
                    }
                    msg = Message.obtain(msportsFindMoreHandler, FRESH_LIST);
                    msg.sendToTarget();
                }
            } else {
                if (list == null || list.size() == 0) {
                    msg = Message.obtain(msportsFindMoreHandler, FRESH_FAILED);
                    msg.sendToTarget();
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
            try {
                SportsFindMoreThread_top.sleep(500);
            } catch (InterruptedException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            ArrayList<FindGroup> list = new ArrayList<FindGroup>();
            try {
                list = (ArrayList<FindGroup>) ApiJsonParser.getNewFindList(
                        mSportsApp.getSessionId());
            } catch (ApiNetException e) {
                e.printStackTrace();
            } catch (ApiSessionOutException e) {
                e.printStackTrace();
            }
            if (list != null) {
                if (list.size() == 0) {
                    msg = Message.obtain(msportsFindMoreHandler, FRESH_NULL_TOP);
                    msg.sendToTarget();
                } else {
                    mList_top.clear();
                    for (FindGroup e : list) {
                        e.setFlog(2);
                        mList_top.add(e);
                    }
                    msg = Message.obtain(msportsFindMoreHandler, FRESH_LIST_TOP);
                    msg.sendToTarget();
                }
            } else {
                if (list == null) {
                    msg = Message.obtain(msportsFindMoreHandler, FRESH_FAILED_TOP);
                    msg.sendToTarget();
                }
            }
        }

    }


    /*----------------------------背景图更换代码如下-----------------------------*/
    private LinearLayout myView;
    private PopupWindow myWindow;
    private RelativeLayout pop_menu_background;

    public void shotSelectImages() {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        myView = (LinearLayout) inflater.inflate(
                R.layout.select_images_from_local, null);

        myView.findViewById(R.id.btn_paizhao).setOnClickListener(
                FindOtherFragment.this);
        myView.findViewById(R.id.btn_select_pic).setOnClickListener(
                FindOtherFragment.this);
        myView.findViewById(R.id.btn_cancle).setOnClickListener(
                FindOtherFragment.this);

        myWindow = new PopupWindow(myView,
                RelativeLayout.LayoutParams.FILL_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT, true);
        myWindow.setAnimationStyle(R.style.AnimationPopup);
        myWindow.setOutsideTouchable(true);
        myWindow.setBackgroundDrawable(new BitmapDrawable());
        myWindow.showAtLocation(right_btn, Gravity.BOTTOM, 0, 0);
        myWindow.setOnDismissListener(FindOtherFragment.this);
        final Animation animation = (Animation) AnimationUtils.loadAnimation(
                getActivity(), R.anim.slide_in_from_bottom);
        myView.startAnimation(animation);
        pop_menu_background.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDismiss() {
        pop_menu_background.setVisibility(View.GONE);
    }

    private static final int CAMERA_REQUEST_CODE = 0;
    private static final int RESULT_REQUEST_CODE = 1;
    private static final int IMAGE_REQUEST_CODE = 2;
    private static final String IMAGE_FILE_NAME = "background.jpg";
    private static final String FACE_PATH = SportsUtilities.DOWNLOAD_SAVE_PATH
            + "/background.jpg";

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_paizhao:
                Intent intentFromCapture = new Intent(
                        MediaStore.ACTION_IMAGE_CAPTURE);
                if (Tools.hasSdcard()) {
                    intentFromCapture
                            .putExtra(MediaStore.EXTRA_OUTPUT, Uri
                                    .fromFile(new File(Environment
                                            .getExternalStorageDirectory(),
                                            IMAGE_FILE_NAME)));
                }
                startActivityForResult(intentFromCapture, CAMERA_REQUEST_CODE);

                pop_menu_background.setVisibility(View.GONE);
                myWindow.dismiss();
                break;
            case R.id.btn_select_pic:
                Intent intentFromGallery = new Intent(Intent.ACTION_PICK, null);
                intentFromGallery.setDataAndType(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intentFromGallery, RESULT_REQUEST_CODE);

                pop_menu_background.setVisibility(View.GONE);
                myWindow.dismiss();
                break;
            case R.id.btn_cancle:
                pop_menu_background.setVisibility(View.GONE);
                myWindow.dismiss();
                break;
            case R.id.tv_message_count:
                Intent intent = new Intent(getActivity(), NewCommentsActivity.class);
                startActivityForResult(intent, 44);
                messageLinearLayout.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Message msg = null;
        switch (requestCode) {
            case CAMERA_REQUEST_CODE:
                if (Tools.hasSdcard()) {
                    File tempFile = new File(
                            Environment.getExternalStorageDirectory(),
                            IMAGE_FILE_NAME);
                    startPhotoZoom(Uri.fromFile(tempFile));
                } else {
                    if(getActivity()!=null&&isAdded()){
                        Toast.makeText(
                                getActivity(),
                                getActivity().getResources().getString(
                                        R.string.sports_toast_nosdcard),
                                Toast.LENGTH_LONG).show();
                    }

                }
                break;
            case RESULT_REQUEST_CODE:
                if (data != null && data.getData() != null) {
                    startPhotoZoom(data.getData());
                }
                break;
            case IMAGE_REQUEST_CODE:
                if (data != null && data.getExtras() != null && mSportsApp.getSessionId() != null
                        && !mSportsApp.getSessionId().equals("")) {
                    getImageToView(data);
                }
                break;
            case 44:
                msg = Message.obtain(msportsFindMoreHandler, FRESH_VIEW);
                msg.sendToTarget();
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    // 裁剪图片方法实现
    public void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 48);
        intent.putExtra("aspectY", 29);
        intent.putExtra("outputX", 480);
        intent.putExtra("outputY", 290);
        intent.putExtra("scale", true);
        intent.putExtra("return-data", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, FACE_PATH);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);
        startActivityForResult(intent, IMAGE_REQUEST_CODE);
    }

    private BitmapDrawable drawable;

    // 保存裁剪之后的图片数据
    private void getImageToView(Intent data) {

        final Bitmap bitmap = decodeUriAsBitmap(data);
        if (Tools.SaveBitmapAsFile(FACE_PATH, bitmap)) {
            if(isAdded()){
                showWaitDialog(getActivity().getResources().getString(
                        R.string.change_photo_background));
            }
            new AsyncTask<Void, Void, ApiBack>() {
                @Override
                protected ApiBack doInBackground(Void... params) {
                    // TODO Auto-generated method stub
                    ApiBack back = null;
                    try {
                        back = (ApiBack) ApiJsonParser.updateFindBg(
                                mSportsApp.getSessionId(), FACE_PATH);
                    } catch (ApiNetException e) {
                        e.printStackTrace();
                    }
                    return back;
                }

                @Override
                protected void onPostExecute(ApiBack result) {
                    // TODO Auto-generated method stub
                    super.onPostExecute(result);
                    if(waitProgressDialog!=null&&waitProgressDialog.isShowing()){
                        waitProgressDialog.dismiss();
                    }
                    if (result == null || result.getFlag() != 0) {
                        // 上传失败
                        if(getActivity()!=null&&isAdded()){
                            Toast.makeText(
                                    getActivity(),
                                    getActivity().getResources().getString(
                                            R.string.upload_failed),
                                    Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        // 上传成功
                        drawable = new BitmapDrawable(bitmap);
                        img_background.setImageDrawable(drawable);
                        if(getActivity()!=null&&isAdded()){
                            Toast.makeText(
                                    getActivity(),
                                    getActivity().getResources().getString(
                                            R.string.upload_success),
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                }

            }.execute();
        }

    }

    private Bitmap decodeUriAsBitmap(Intent data) {
        return data.getExtras().getParcelable("data");
    }

    class GetNewCommnetCountTask extends AsyncTask<String, Integer, Boolean> {
        private NewCommentInfo commentInfo = new NewCommentInfo();

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
                    messageLinearLayout.setVisibility(View.VISIBLE);
                    commentsNotify.setText("您有" + commentInfo.commentCount
                            + "条消息");
                    bg_Downloader.download(commentInfo.headimg, commentsImg,
                            null);
                }
            } else {
            }
        }
    }

    public void update() {
        // TODO Auto-generated method stub
        GetNewCommnetCountTask newCommentCount = new GetNewCommnetCountTask();
        newCommentCount.execute("");
    }

    /**
     * 开始加载数据
     */
    public void loadDate() {
        waitShowDialog();
        // 起线程
        SportsFindMoreThread sportsFindMoreThread = new SportsFindMoreThread();
//        SportsFindMoreThread_top sportsFindMoreThread2 = new SportsFindMoreThread_top();
        if (mSportsApp.isOpenNetwork()) {
            if (mSportsApp.getSessionId() != null
                    && !mSportsApp.getSessionId().equals("")) {
                sportsFindMoreThread.start();
//                sportsFindMoreThread2.start();
            }
        } else {
            if(isAdded()){
                Toast.makeText(getActivity(),
                        getResources().getString(R.string.acess_server_error),
                        Toast.LENGTH_SHORT).show();
            }

            mPullSearchListView.onRefreshComplete();
//            if (mLoadProgressDialog != null)
//                if (mLoadProgressDialog.isShowing())
//                    mLoadProgressDialog.dismiss();
        }

        GetNewCommnetCountTask newCommentCount = new GetNewCommnetCountTask();
        newCommentCount.execute("");
    }


    //下载活动列表
    private class GetActionDataTask extends
            AsyncTask<Void, Void, List<ActionList>> {

        @Override
        protected List<ActionList> doInBackground(Void... sessionid) {

            List<ActionList> actionLists = null;
            try {
                if(isAdded()){
                    actionLists = ApiJsonParser.getNewActionList(
                            mSportsApp.getSessionId(), "z" + getResources().getString(R.string.config_game_id), 0);
                }
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
            if (result == null)
                return;
            if (result.size() > 0) {
                for (ActionList actionList : result) {
                    actionLists.add(actionList);
                }
            }
        }

    }

    private class GetActionDataTask2 extends
            AsyncTask<Void, Void, List<ActionList>> {

        @Override
        protected List<ActionList> doInBackground(Void... sessionid) {

            List<ActionList> actionLists = null;
            try {
                if(isAdded()){
                    actionLists = ApiJsonParser.getNewActionList(
                            mSportsApp.getSessionId(), "z" + getResources().getString(R.string.config_game_id), 1);
                }
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

    //加载数据
    public void newLocalDate() {
        if (getActivity() == null || mSportsApp == null) {
            return;
        }
        mSportsApp = (SportsApp) getActivity().getApplication();
        if (getActivity() != null) {
            if (mSportsApp.LoginOption) {
                if (mSportsApp.isOpenNetwork()) {
                    waitShowDialog();
                }
            }

        }


        if (!mSportsApp.isOpenNetwork()) {
            if(isAdded()){
                Toast.makeText(getActivity(),
                        getResources().getString(R.string.acess_server_error),
                        Toast.LENGTH_SHORT).show();
            }
        }

        // 获取缓存数据
        SharedPreferences preferences = SportsApp.getInstance()
                .getSharedPreferences("FindGroupList", 0);
        String content = preferences.getString("FindGroupList_allinfo", "");
        if (content != null && !"".equals(content)) {
            try {
                FindGroup lists = null;
                JSONArray jsonArray = new JSONObject(content)
                        .getJSONArray("data");
                if (jsonArray != null && jsonArray.length() > 0) {
                    for (int i = 0, j = jsonArray.length(); i < j; i++) {
                        lists = new FindGroup();
                        JSONObject obj = jsonArray.getJSONObject(i);
                        lists.setFindId(obj.getString("id"));
                        lists.setOtheruid(obj.getInt("uid"));
                        if (obj.getInt("sex") == 1) {
                            lists.setSex("man");
                        } else {
                            lists.setSex("woman");
                        }
                        if (obj.has("content")) {
                            lists.setDetils(obj.getString("content"));
                        }
                        lists.setOthername(obj.getString("name"));
                        lists.setOtherimg(obj.getString("img"));
                        if (obj.has("inputtime")) {
                            lists.setTimes(obj.getLong("inputtime"));
                        }
                        if (obj.has("if_fans")) {
                            lists.setIsFriends(obj.getInt("if_fans"));
                        }
                        if (obj.has("comefrom")) {
                            lists.setComefrom(obj.getString("comefrom"));
                        }
                        if (obj.has("pic")) {
                            JSONArray pArray = obj.getJSONArray("pic");
                            int m = pArray.length();
                            String[] imgs = new String[m];
                            String[] biggerImgs = new String[m];
                            for (int k = 0; k < m; k++) {
                                String iurl = (String) pArray.get(k);
                                imgs[k] = iurl;
                                biggerImgs[k] = iurl
                                        .replace("s_", "b_");
                            }
                            lists.setImgs(imgs);
                            lists.setBiggerImgs(biggerImgs);
                            if (m == 1) {
                                lists.setWidth(obj.getInt("pic_width"));
                                lists.setHeight(obj
                                        .getInt("pic_height"));
                            }
                        }

                        if (obj.has("topiccat")) {
                            ArrayList<TopicContent> topicList = new ArrayList<TopicContent>();
                            if (!"".equals(obj.getString("topiccat"))) {
                                JSONArray tArray = obj
                                        .getJSONArray("topiccat");
                                for (int c = 0, b = tArray.length(); c < b; c++) {
                                    JSONObject tObject = tArray
                                            .getJSONObject(c);
                                    TopicContent topicContent = new TopicContent();
                                    topicContent.setId(tObject
                                            .getString("id"));
                                    topicContent.setTitle(tObject
                                            .getString("title"));
                                    topicList.add(topicContent);
                                }
                            }
                            lists.setTopicList(topicList);
                        }

                        if (obj.has("sportsdata")) {
                            if (!"".equals(obj.getString("sportsdata"))) {
                                JSONObject jsonObject = obj
                                        .getJSONObject("sportsdata");
                                SportRecord sportRecord = new SportRecord();
                                sportRecord.setTime(jsonObject
                                        .getString("time"));
                                sportRecord
                                        .setSport_distance(jsonObject
                                                .getString("sport_distance"));
                                lists.setSportRecord(sportRecord);
                            }

                        }

                        if (obj.has("comment")) {
                            ArrayList<SportCircleComments> fList = new ArrayList<SportCircleComments>();
                            JSONArray cArray = obj
                                    .getJSONArray("comment");
                            for (int c = 0, b = cArray.length(); c < b; c++) {
                                JSONObject cObject = cArray
                                        .getJSONObject(c);
                                SportCircleComments fc = new SportCircleComments();
                                fc.setId(cObject.getString("id"));
                                fc.setUid(cObject.getString("uid"));
                                fc.setContent(cObject
                                        .getString("content"));
                                fc.setWav(cObject.getString("wav"));
                                fc.setWavtime(cObject
                                        .getString("wavtime"));
                                fc.setInputtime(cObject
                                        .getString("inputtime"));
                                fc.setName(cObject.getString("name"));
                                fc.setImg(cObject.getString("img"));
                                fc.setSex(cObject.getString("sex"));
                                fc.setTo_name(cObject
                                        .getString("to_name"));
                                fc.setTo_img(cObject
                                        .getString("to_img"));
                                fc.setTo_sex(cObject
                                        .getString("to_sex"));
                                fList.add(fc);
                            }
                            lists.setTalkdetils(fList);
                            lists.setcCount(obj.getInt("comment_count"));
                        }

                        if (obj.has("like")) {
                            ArrayList<PraiseUsers> pArrayList = new ArrayList<PraiseUsers>();
                            if (!"".equals(obj.getString("like"))) {
                                JSONArray sArray = obj
                                        .getJSONArray("like");
                                for (int c = 0, b = sArray.length(); c < b; c++) {
                                    JSONObject tObject = sArray
                                            .getJSONObject(c);
                                    PraiseUsers praiseUsers = new PraiseUsers();
                                    praiseUsers.setUid(tObject
                                            .getString("uid"));
                                    praiseUsers.setName(tObject
                                            .getString("name"));
                                    praiseUsers.setImg(tObject
                                            .getString("img"));
                                    praiseUsers.setSex(tObject
                                            .getString("sex"));
                                    pArrayList.add(praiseUsers);
                                }
                            }
                            lists.setpArrayList(pArrayList);
                        }
                        if (obj.has("like_count")) {
                            lists.setGoodpeople(obj
                                    .getInt("like_count"));
                        }
                        if (obj.has("ilike")) {
                            lists.setGood(obj.getInt("ilike") == 1 ? true
                                    : false);
                        }
                        mList.add(lists);
                    }
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            if (!mSportsApp.isOpenNetwork()){
                if (mList != null && mList.size() > 0) {
                    if (msportsFindMoreHandler == null) {
                        msportsFindMoreHandler = new SportsFindMoreHandler();
                    }

                    Message msg = Message.obtain(msportsFindMoreHandler, FRESH_FIRST);
                    msg.sendToTarget();
                }
            }else{
                loadDate();
            }
        } else {
            if (mSportsApp.isOpenNetwork()) {
                if (mSportsApp.getSessionId() != null
                        && !mSportsApp.getSessionId().equals("")) {
                    SportsFindMoreThread sportsFindMoreThread = new SportsFindMoreThread();
//                    SportsFindMoreThread_top sportsFindMoreThread2 = new SportsFindMoreThread_top();
                    sportsFindMoreThread.start();
//                    sportsFindMoreThread2.start();

                    GetNewCommnetCountTask newCommentCount = new GetNewCommnetCountTask();
                    newCommentCount.execute("");
                }
            } else {
                mPullSearchListView.onRefreshComplete();
//                if (mLoadProgressDialog != null)
//                    if (mLoadProgressDialog.isShowing())
//                        mLoadProgressDialog.dismiss();
            }
        }

        isFirst = true;


    }

    private ArrayList<FindGroup> getmList(){
        ArrayList<FindGroup> list = new ArrayList<FindGroup>();
        SharedPreferences preferences = SportsApp.getInstance()
                .getSharedPreferences("FindGroupList", 0);
        String content = preferences.getString("FindGroupList_allinfo", "");
        if (content != null && !"".equals(content)) {
            try {
                FindGroup lists = null;
                JSONArray jsonArray = new JSONObject(content)
                        .getJSONArray("data");
                if (jsonArray != null && jsonArray.length() > 0) {
                    for (int i = 0, j = jsonArray.length(); i < j; i++) {
                        lists = new FindGroup();
                        JSONObject obj = jsonArray.getJSONObject(i);
                        lists.setFindId(obj.getString("id"));
                        lists.setOtheruid(obj.getInt("uid"));
                        if (obj.getInt("sex") == 1) {
                            lists.setSex("man");
                        } else {
                            lists.setSex("woman");
                        }
                        if (obj.has("content")) {
                            lists.setDetils(obj.getString("content"));
                        }
                        lists.setOthername(obj.getString("name"));
                        lists.setOtherimg(obj.getString("img"));
                        if (obj.has("inputtime")) {
                            lists.setTimes(obj.getLong("inputtime"));
                        }
                        if (obj.has("if_fans")) {
                            lists.setIsFriends(obj.getInt("if_fans"));
                        }
                        if (obj.has("comefrom")) {
                            lists.setComefrom(obj.getString("comefrom"));
                        }
                        if (obj.has("pic")) {
                            JSONArray pArray = obj.getJSONArray("pic");
                            int m = pArray.length();
                            String[] imgs = new String[m];
                            String[] biggerImgs = new String[m];
                            for (int k = 0; k < m; k++) {
                                String iurl = (String) pArray.get(k);
                                imgs[k] = iurl;
                                biggerImgs[k] = iurl
                                        .replace("s_", "b_");
                            }
                            lists.setImgs(imgs);
                            lists.setBiggerImgs(biggerImgs);
                            if (m == 1) {
                                lists.setWidth(obj.getInt("pic_width"));
                                lists.setHeight(obj
                                        .getInt("pic_height"));
                            }
                        }

                        if (obj.has("topiccat")) {
                            ArrayList<TopicContent> topicList = new ArrayList<TopicContent>();
                            if (!"".equals(obj.getString("topiccat"))) {
                                JSONArray tArray = obj
                                        .getJSONArray("topiccat");
                                for (int c = 0, b = tArray.length(); c < b; c++) {
                                    JSONObject tObject = tArray
                                            .getJSONObject(c);
                                    TopicContent topicContent = new TopicContent();
                                    topicContent.setId(tObject
                                            .getString("id"));
                                    topicContent.setTitle(tObject
                                            .getString("title"));
                                    topicList.add(topicContent);
                                }
                            }
                            lists.setTopicList(topicList);
                        }

                        if (obj.has("sportsdata")) {
                            if (!"".equals(obj.getString("sportsdata"))) {
                                JSONObject jsonObject = obj
                                        .getJSONObject("sportsdata");
                                SportRecord sportRecord = new SportRecord();
                                sportRecord.setTime(jsonObject
                                        .getString("time"));
                                sportRecord
                                        .setSport_distance(jsonObject
                                                .getString("sport_distance"));
                                lists.setSportRecord(sportRecord);
                            }

                        }

                        if (obj.has("comment")) {
                            ArrayList<SportCircleComments> fList = new ArrayList<SportCircleComments>();
                            JSONArray cArray = obj
                                    .getJSONArray("comment");
                            for (int c = 0, b = cArray.length(); c < b; c++) {
                                JSONObject cObject = cArray
                                        .getJSONObject(c);
                                SportCircleComments fc = new SportCircleComments();
                                fc.setId(cObject.getString("id"));
                                fc.setUid(cObject.getString("uid"));
                                fc.setContent(cObject
                                        .getString("content"));
                                fc.setWav(cObject.getString("wav"));
                                fc.setWavtime(cObject
                                        .getString("wavtime"));
                                fc.setInputtime(cObject
                                        .getString("inputtime"));
                                fc.setName(cObject.getString("name"));
                                fc.setImg(cObject.getString("img"));
                                fc.setSex(cObject.getString("sex"));
                                fc.setTo_name(cObject
                                        .getString("to_name"));
                                fc.setTo_img(cObject
                                        .getString("to_img"));
                                fc.setTo_sex(cObject
                                        .getString("to_sex"));
                                fList.add(fc);
                            }
                            lists.setTalkdetils(fList);
                            lists.setcCount(obj.getInt("comment_count"));
                        }

                        if (obj.has("like")) {
                            ArrayList<PraiseUsers> pArrayList = new ArrayList<PraiseUsers>();
                            if (!"".equals(obj.getString("like"))) {
                                JSONArray sArray = obj
                                        .getJSONArray("like");
                                for (int c = 0, b = sArray.length(); c < b; c++) {
                                    JSONObject tObject = sArray
                                            .getJSONObject(c);
                                    PraiseUsers praiseUsers = new PraiseUsers();
                                    praiseUsers.setUid(tObject
                                            .getString("uid"));
                                    praiseUsers.setName(tObject
                                            .getString("name"));
                                    praiseUsers.setImg(tObject
                                            .getString("img"));
                                    praiseUsers.setSex(tObject
                                            .getString("sex"));
                                    pArrayList.add(praiseUsers);
                                }
                            }
                            lists.setpArrayList(pArrayList);
                        }
                        if (obj.has("like_count")) {
                            lists.setGoodpeople(obj
                                    .getInt("like_count"));
                        }
                        if (obj.has("ilike")) {
                            lists.setGood(obj.getInt("ilike") == 1 ? true
                                    : false);
                        }
                        list.add(lists);
                    }
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return list;
        }else{
            return  null;
        }
    }

}
