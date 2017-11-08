package com.fox.exercise;

import java.io.File;
import java.io.FileNotFoundException;
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


import com.fox.exercise.FindMeAdapter.OnCheckedChangeCommentListener;
import com.fox.exercise.api.ApiBack;
import com.fox.exercise.api.ApiConstant;
import com.fox.exercise.api.ApiJsonParser;
import com.fox.exercise.api.ApiNetException;
import com.fox.exercise.api.ApiSessionOutException;
import com.fox.exercise.api.entity.ExpressionItems;
import com.fox.exercise.api.entity.FindMore;
import com.fox.exercise.api.entity.UserDetail;
import com.fox.exercise.bitmap.util.ImageResizer;
import com.fox.exercise.login.Tools;
import com.fox.exercise.pedometer.ImageWorkManager;
import com.fox.exercise.publish.SendMsgDetail;
import com.fox.exercise.util.RoundedImage;
import com.fox.exercise.util.ScrollLayout;
import com.fox.exercise.util.ScrollLayout.OnViewChangeListener;
import com.fox.exercise.view.PullToRefreshListView;
import com.fox.exercise.view.PullToRefreshBase.OnRefreshListener;
import com.umeng.analytics.MobclickAgent;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
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
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.PopupWindow.OnDismissListener;

public class FindMeActivity extends AbstractBaseActivity implements
        OnClickListener, OnDismissListener {
    private SportsApp mSportsApp;
    private PullToRefreshListView mPullSearchListView = null;
    private ListView mListView = null;
    private Dialog mLoadProgressDialog = null;
    private Dialog dialog;
    private static final String TAG = "FindMeFragment";
    private TextView mDialogMessage;
    private final int FRESH_LIST = 1;// 更新成功
    private final int FRESH_FAILED = 2;// 更新失败
    private final int FRESH_DONE = 3;
    private final int FRESH_NULL = 4;
    private int times = 0;
    private FindMeAdapter sportsfindmeAdapter = null;
    private ArrayList<FindMore> mList = new ArrayList<FindMore>();
    private ArrayList<FindMore> mNewList = new ArrayList<FindMore>();
    private ImageView img_background;
    private LinearLayout roundimg_layout;
    public SportsFindMeHandler msportsFindMeHandler = null;
    private TextView myNameText;
    private RoundedImage userPhoto;
    private ImageWorkManager mImageWorkerMan_Icon;
    private ImageResizer mImageWorker_Icon;
    private UserDetail detail;
    private ImageButton iView;
    private int iViewId = 99;

    private RecordHelper mRecorder;
    private ImageButton findTextBtn;
    private Button findPressBtn;
    private boolean upTypeText = false;
    ;

    private LinearLayout layoutVoice;
    private RelativeLayout findLayout;
    private RelativeLayout rScrollLayout;
    private ScrollLayout scrollLayout;
    private LinearLayout imgLayout;
    private ImageView findExpressBtn;
    private List<ExpressionItems> imgItems;
    private int mViewCount;
    private int mCurSel;
    private String defaultMesg;
    private String[] imgStr;
    private static final float APP_PAGE_SIZE = 21.0f;
    private Boolean isShow;

    private LinearLayout findUpcommentText;
    private EditText findUpcommentEdittext;
    private Button findUpcommentSend;
    private Button findUnavailable;
    private static final int UPLOAD_FINISH = 1;
    private static final int NOT_LOGIN = 2;
    private static final int UPLOAD_FAILED = 3;
    private static final int UPLOAD_START = 4;
    private static final int SAVE_TO_DB = 5;
    private static final int RECORD_LOADING = 6;
    private static final int RECORD_PREPARED = 7;
    private static final int RECORD_FINISH = 8;
    private static final int RECORD_PAUSE = 9;
    private static final int RECORD_ERROR = 10;
    private static final int FLAG_RUNWAV = 11;
    private static final int RESULT_ERROR = 12;
    private static final int ADD_ITEM = 13;
    private static final int GETDETAIL_FAIL = 14;
    private static final int BACKGROUND_VIEW_ID = 66;
    private static final int TOP_TITLE_LAYOUT_ID = 77;
    private static final int USER_PHOTO_VIEW_ID = 88;
    private static final int BACK_BUTTON = 112;
    private static final int BACK_LAYOUT = 113;
    public static final int HIDE_EDIT = 119;
    private int uid;// the one who send msg to me
    private SportsExceptionHandler mExceptionHandler = null;
    private int listPosition, toNumber, wavDuration;
    private String toNameStr, commentText, commentWav;
    private String find_Id, to_Id;
    private boolean findBool = true;
    private SendMsgDetail self = null;
    private int bg_width;
    private int bg_height;
    private int screen_width;
    private int screen_height;
    private ImageDownloader bg_Downloader = null;
    private String bg_urlString;
    private Dialog waitProgressDialog;
    private int marknumber = 0;
    private Context context;
    //换背景图片
    private static final int CAMERA_REQUEST_CODE = 0;
    private static final int RESULT_REQUEST_CODE = 1;
    private static final int IMAGE_REQUEST_CODE = 2;
    private static final String IMAGE_FILE_NAME = "background.jpg";
    private static final String FACE_PATH = SportsUtilities.DOWNLOAD_SAVE_PATH
            + "/background.jpg";
    private LinearLayout myView;
    private PopupWindow myWindow;
    private RelativeLayout pop_menu_background;
    private LinearLayout messageLinearLayout;

    @Override
    public void initIntentParam(Intent intent) {
        // TODO Auto-generated method stub
        title = getResources().getString(R.string.friends_tab_my_faxian);
        mSportsApp = (SportsApp) getApplication();
        mExceptionHandler = mSportsApp.getmExceptionHandler();
        detail = SportsApp.getInstance().getSportUser();

    }

    @Override
    public void initView() {
        // TODO Auto-generated method stub
        showContentView(R.layout.sports_find_fragment);
        left_ayout.setId(BACK_LAYOUT);
        left_ayout.setOnClickListener(
                this);
        leftButton.setId(BACK_BUTTON);
        leftButton.setOnClickListener(this);
        context = this;
        iView = new ImageButton(this);
        iView.setBackgroundDrawable(getResources().getDrawable(
                R.drawable.sports_title_photo));
        iView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        iView.setId(iViewId);
        iView.setOnClickListener(this);
        showRightBtn(iView);
        right_btn.setPadding(0, 0, mSportsApp.dip2px(17), 0);
        this.bg_Downloader = new ImageDownloader(this);
        bg_Downloader.setType(ImageDownloader.OnlyOne);
        init();
        mListView = mPullSearchListView.getRefreshableView();
    }

    private void init() {
        // TODO Auto-generated method stub
        mPullSearchListView = (PullToRefreshListView) findViewById(R.id.sports_find_refresh_list);
        pop_menu_background = (RelativeLayout) findViewById(R.id.send_menu_background);
    }

    private void initPortrait() {
        mImageWorker_Icon
                .setLoadingImage("man".equals(SportsApp.getInstance()
                        .getSportUser().getSex()) ? R.drawable.sports_user_edit_portrait_male
                        : R.drawable.sports_user_edit_portrait);
        mImageWorker_Icon.loadImage(SportsApp.getInstance().getSportUser()
                .getUimg(), userPhoto, null, null, false);
    }

    @Override
    public void setViewStatus() {
        // TODO Auto-generated method stub
        mListView.setDivider(null);
        mListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                // TODO Auto-generated method stub
                arg1.setSelected(true);
                if (findBool == false) {
                    findLayout.setVisibility(View.GONE);
                    findBool = true;
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
                    findBool = true;
                }
                hideedit();
            }

            @Override
            public void onScroll(AbsListView arg0, int arg1, int arg2, int arg3) {
                // TODO Auto-generated method stub
            }
        });
        waitShowDialog();
        initViews();
        View head_view = LayoutInflater.from(this).inflate(
                R.layout.sports_find_headview, null);
        messageLinearLayout = (LinearLayout) head_view.findViewById(R.id.message_layout);
        messageLinearLayout.setVisibility(View.GONE);
        //设置背景图片的宽度根据屏幕大小自适应
        img_background = (ImageView) head_view.findViewById(R.id.find_background_img);
        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) img_background.getLayoutParams();
        bg_width = 480;
        bg_height = 294;
        screen_width = (int) (SportsApp.ScreenWidth);
        screen_height = (int) (SportsApp.ScreenHeight);
        lp.width = screen_width;
        lp.height = (int) ((screen_width * bg_height) / bg_width);
        img_background.setLayoutParams(lp);
        if (SportsApp.getInstance() != null && SportsApp.getInstance().getSportUser() != null) {
            if (SportsApp.getInstance().getSportUser().getFindimg() == null ||
                    SportsApp.getInstance().getSportUser().getFindimg().equals("") ||
                    SportsApp.getInstance().getSportUser().getFindimg().equals("null")) {
//				img_background.setBackgroundDrawable(getResources().getDrawable(R.drawable.sports_default_cover));
            } else {
                bg_urlString = ApiConstant.URL + SportsApp.getInstance().getSportUser().getFindimg();
                bg_Downloader.download(bg_urlString, img_background, null);
            }
        }
        img_background.setId(BACKGROUND_VIEW_ID);
        img_background.setOnClickListener(this);
        top_title_layout.setId(TOP_TITLE_LAYOUT_ID);
        top_title_layout.setOnClickListener(this);

        //头像针对背景图片的位置根据屏幕大小自适应
        roundimg_layout = (LinearLayout) head_view.findViewById(R.id.mylayout);
        FrameLayout.LayoutParams lps = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        int margin_top = (int) ((SportsApp.ScreenHeight / 3) * 0.6);
        lps.topMargin = margin_top;
        lps.gravity = Gravity.RIGHT;
        roundimg_layout.setLayoutParams(lps);
        //设置名字长度
        myNameText = (TextView) head_view.findViewById(R.id.find_myname);
        LinearLayout.LayoutParams lp_name = (LinearLayout.LayoutParams) myNameText.getLayoutParams();
        lp_name.width = (int) (SportsApp.ScreenWidth * 0.42);
        myNameText.setLayoutParams(lp_name);
        myNameText.setText(detail.getUname());
        userPhoto = (RoundedImage) head_view.findViewById(R.id.cover_user_photo2);
        userPhoto.setId(USER_PHOTO_VIEW_ID);
        userPhoto.setOnClickListener(this);
        mImageWorkerMan_Icon = new ImageWorkManager(this, 0, 0);
        mImageWorker_Icon = mImageWorkerMan_Icon.getImageWorker();
        initPortrait();
        mListView.addHeaderView(head_view);
        msportsFindMeHandler = new SportsFindMeHandler();// 更新
        // 起线程
        SportsFindMeThread sportsFindMeThread = new SportsFindMeThread();
        if (mSportsApp.isOpenNetwork()) {
            if (mSportsApp.getSessionId() != null && !mSportsApp.getSessionId().equals("")) {
                sportsFindMeThread.start();
            }
        } else {
            Toast.makeText(getApplication(),
                    getResources().getString(R.string.acess_server_error), Toast.LENGTH_SHORT).show();
            mPullSearchListView.onRefreshComplete();
            if (mLoadProgressDialog != null)
                if (mLoadProgressDialog.isShowing())
                    mLoadProgressDialog.dismiss();
        }
        mPullSearchListView.setOnRefreshListener(new OnRefreshListener() {

            @Override
            public void onRefresh(int pullDirection) {
                if (mSportsApp.isOpenNetwork()) {
                    switch (pullDirection) {
                        case FansAndNear.MODE_DEFAULT_LOAD:
                            times++;
                            SportsFindMeThread loadThread = new SportsFindMeThread();
                            loadThread.start();
                            break;
                        case FansAndNear.MODE_PULL_DOWN_TO_REFRESH:
                            times = 0;
                            SportsFindMeThread refreshThread = new SportsFindMeThread();
                            refreshThread.start();
                            break;
                    }
                } else {
                    Toast.makeText(getApplication(),
                            getResources().getString(R.string.acess_server_error), Toast.LENGTH_SHORT).show();
                    mPullSearchListView.onRefreshComplete();
                }

            }
        });
    }

    //显示软键盘
    private void showedit() {
        findUpcommentEdittext.setFocusable(true);
        findUpcommentEdittext.setFocusableInTouchMode(true);
        findUpcommentEdittext.requestFocus();
        InputMethodManager inputManager = (InputMethodManager) findUpcommentEdittext.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.showSoftInput(findUpcommentEdittext, 0);
    }

    //等UI绘制好弹出软键盘
    private void showUIedit() {
        findUpcommentEdittext.setFocusable(true);
        findUpcommentEdittext.setFocusableInTouchMode(true);
        findUpcommentEdittext.requestFocus();
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                InputMethodManager inputManager = (InputMethodManager) findUpcommentEdittext.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.showSoftInput(findUpcommentEdittext, 0);
            }
        }, 300);
    }

    //软键盘消失
    private void hideedit() {
        //findUpcommentEdittext.setText("");
        findUpcommentEdittext.clearFocus();
        //close InputMethodManager
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(findUpcommentEdittext.getWindowToken(), 0);
    }

    class findClickListener implements OnClickListener, OnLongClickListener, OnTouchListener {

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
                                Toast.makeText(getApplication(),
                                        getResources().getString(R.string.sports_record_fail), Toast.LENGTH_SHORT).show();
                                break;
                            }
                        /*UploadThread t = new UploadThread("/sdcard/Recording/" + (RecordHelper.mSampleFile).getName(),
								"", RecordHelper.mSampleLength);
						t.start();*/
                            Log.e(TAG, "音频-文字---------" + findUpcommentEdittext.getText().toString());
                            Log.e(TAG, "音频-文件---------" + "/sdcard/Recording/" + (RecordHelper.mSampleFile).getName());
                            Log.e(TAG, "音频-长度---------" + RecordHelper.mSampleLength + "");
                            sportsfindmeAdapter.send(listPosition, toNameStr, find_Id, to_Id, findUpcommentEdittext.getText().toString(), "/sdcard/Recording/" + (RecordHelper.mSampleFile).getName(), RecordHelper.mSampleLength + "");
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
                mRecorder.startRecording(MediaRecorder.OutputFormat.DEFAULT, ".mp3", getApplication());
                dialog = new Dialog(getApplication(), R.style.share_dialog2);
                // dialog.show();
                layoutVoice.setVisibility(View.VISIBLE);
            }
            return true;
        }

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            switch (v.getId()) {
                case R.id.find_expression_text_btn:
                    findPressBtn.setVisibility(View.GONE);
                    findUpcommentText.setVisibility(View.VISIBLE);
                    hideedit();
                    if (isShow == false) {
                        isShow = true;
                        rScrollLayout.setVisibility(View.VISIBLE);
                        if (!upTypeText) {
                            upTypeText = true;
                            findTextBtn.setBackgroundResource(R.drawable.sk2voice);
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
                        showedit();
                        findTextBtn.setBackgroundResource(R.drawable.sk2voice);
                    } else {
                        upTypeText = false;
                        findUpcommentText.setVisibility(View.GONE);
                        findTextBtn.setBackgroundResource(R.drawable.sk2text);
                        findPressBtn.setVisibility(View.VISIBLE);
                        hideedit();
                    }
                    break;
                case R.id.find_upcomment_edittext:
                    Log.i("", "lalaalla1");
                    if (isShow == true) {
                        isShow = false;
                        rScrollLayout.setVisibility(View.GONE);
                        Log.i("", "lalaalla2222");
                    }
                    break;
                case R.id.find_upcomment_send:
                    /**
                     * 第一个参数是评论所属发现在list中的位置
                     * 第二个参数用来当回复的时候显示回复给谁，如果直接评论则null
                     * 第三个发现ID
                     * 第四个用来当回复的时候传递to_id,如果直接评论则为null
                     * 其余三个分别是文本内容，音频，音频时间
                     * */
                    findLayout.setVisibility(View.GONE);

                    sportsfindmeAdapter.send(listPosition, toNameStr, find_Id, to_Id, findUpcommentEdittext.getText().toString(), null, null);

                    findUpcommentEdittext.setText("");
                    findLayout.setVisibility(View.GONE);
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
                //设置新的光标所在位置
                findUpcommentEdittext.setSelection(str.length());
                //暂不支持此类型符号的输入
                Toast.makeText(getApplication(),
                        getResources().getString(R.string.does_not_this_input), Toast.LENGTH_SHORT).show();
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
                //除空格回车外有其他字符
                findUpcommentSend.setVisibility(View.VISIBLE);
                findUnavailable.setVisibility(View.GONE);
            } else {
                //只有空格字符
                findUpcommentSend.setVisibility(View.GONE);
                findUnavailable.setVisibility(View.VISIBLE);
            }
        }
    };

    public static Boolean stringFilter(String str) throws PatternSyntaxException {
        //监听是否有除空格外的其他字符
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
            return source;//如果不包含，直接返回
        }
        //到这里铁定包含
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
            if (buf.length() == len) {//这里的意义在于尽可能少的toString，因为会重新生成字符串
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
                //do nothing，判断到了这里表明，确认有表情字符
                return true;
            }
        }

        return false;
    }

    private static boolean isEmojiCharacter(char codePoint) {
        return !((codePoint == 0x0) ||
                (codePoint == 0x9) ||
                (codePoint == 0xA) ||
                (codePoint == 0xD) ||
                ((codePoint >= 0x20) && (codePoint <= 0xD7FF)) ||
                ((codePoint >= 0xE000) && (codePoint <= 0xFFFD)) ||
                ((codePoint >= 0x10000) && (codePoint <= 0x10FFFF)));
    }

    public void initViews() {
        findLayout = (RelativeLayout) findViewById(R.id.find_bottom);
        findLayout.setVisibility(View.GONE);

        layoutVoice = (LinearLayout) findViewById(R.id.layoutVoice);
        mRecorder = new RecordHelper();
        findPressBtn = (Button) findViewById(R.id.find_press_btn);
        findPressBtn.setOnLongClickListener(new findClickListener());
        findPressBtn.setOnTouchListener(new findClickListener());
        findTextBtn = (ImageButton) findViewById(R.id.find_text_btn);
        findTextBtn.setOnClickListener(new findClickListener());

        findExpressBtn = (ImageButton) findViewById((R.id.find_expression_text_btn));
        findExpressBtn.setOnClickListener(new findClickListener());

        rScrollLayout = (RelativeLayout) findViewById(R.id.rScrollLayout);
        scrollLayout = (ScrollLayout) findViewById(R.id.ScrollLayoutTest);
        imgLayout = (LinearLayout) findViewById(R.id.imageLayot);

        scrollLayout.SetOnViewChangeListener(new OnViewChangeListener() {

            @Override
            public void OnViewChange(int view) {
                // TODO Auto-generated method stub
                setCurPoint(view);
            }
        });
        initViewExpression();
        mCurSel = 0;
        isShow = false;
        ImageView img = (ImageView) imgLayout.getChildAt(mCurSel);
        img.setBackgroundResource(R.drawable.qita_biaoqing_04);

        findUpcommentText = (LinearLayout) findViewById(R.id.find_upcomment_text);
        findUpcommentText.setVisibility(View.GONE);
        findUpcommentSend = (Button) findViewById(R.id.find_upcomment_send);
        findUpcommentSend.setOnClickListener(new findClickListener());
        findUnavailable = (Button) findViewById(R.id.find_unavailable);
        findUpcommentEdittext = (EditText) findViewById(R.id.find_upcomment_edittext);
        findUpcommentEdittext.setOnClickListener(new findClickListener());
        findUpcommentEdittext.addTextChangedListener(mTextWatcher);
        findUpcommentEdittext.setOnFocusChangeListener(new OnFocusChangeListener() {

            @Override
            public void onFocusChange(View arg0, boolean arg1) {
                // TODO Auto-generated method stub
                if (isShow == true) {
                    isShow = false;
                    rScrollLayout.setVisibility(View.GONE);
                }
            }
        });

    }

    public void initViewExpression() {
        imgStr = getResources().getStringArray(R.array.imageStr_array);
        imgItems = new ArrayList<ExpressionItems>();
        Field[] files = R.drawable.class.getDeclaredFields();
        int j = 0;
        for (Field file : files) {
            if (file.getName().startsWith("biaoqing_")) {
                if (((imgItems.size() + 1) % 21) == 0) {
                    ExpressionItems item = new ExpressionItems();
                    item.setId(R.drawable.qita_biaoqing_01);
                    item.setName("itemCanel");
                    item.setIsCanel(true);
                    imgItems.add(item);
                }
                ApplicationInfo appInfo = getApplicationInfo();
                int resID = getResources().getIdentifier(file.getName(), "drawable", appInfo.packageName);
                ExpressionItems item = new ExpressionItems();
                item.setId(resID);
                item.setName(imgStr[j].toString());
                item.setIsCanel(false);
                imgItems.add(item);
                j++;
                System.out.println(file.getName());
            }
        }
        ExpressionItems item = new ExpressionItems();
        item.setId(R.drawable.qita_biaoqing_01);
        item.setName("itemCanel");
        item.setIsCanel(true);
        imgItems.add(item);
        final int Count = (int) Math.ceil(imgItems.size() / APP_PAGE_SIZE);
        Log.e(TAG, "size:" + imgItems.size() + " page:" + Count);
        mViewCount = Count;

        for (int i = 0; i < Count; i++) {
            GridView appPage = new GridView(this);
            appPage.setAdapter(new ExpressionImgAdapter(this, imgItems, i));
            appPage.setNumColumns(7);
            appPage.setHorizontalSpacing(10);
            appPage.setVerticalSpacing(10);
            appPage.setSelector(new ColorDrawable(Color.TRANSPARENT));
            appPage.setOnItemClickListener(listener);
            scrollLayout.addView(appPage);

            ImageView img = new ImageView(this);
            img.setBackgroundResource(R.drawable.qita_biaoqing_03);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(10, 0, 10, 0);
            img.setLayoutParams(params);
            img.setEnabled(true);
            img.setTag(appPage.getId());
            imgLayout.addView(img);
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

    public OnItemClickListener listener = new OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            ExpressionItems item = (ExpressionItems) parent.getItemAtPosition(position);
            if (item.getIsCanel()) {
                delStr(findUpcommentEdittext);
            } else {
                int d = item.getId();
                Drawable drawable = getResources().getDrawable(d);
                CharSequence cs = item.getName();
                SpannableString ss = new SpannableString(cs);
                if (drawable != null) {
                    Bitmap bitmap = BitmapFactory.decodeResource(getResources(), d);
                    DisplayMetrics mDisplayMetrics = new DisplayMetrics();
                    getWindowManager().getDefaultDisplay().getMetrics(mDisplayMetrics);
                    int Screenwidth = mDisplayMetrics.widthPixels;
                    int width = 0;
                    if (Screenwidth > 1000) {
                        width = Screenwidth * 23 / 100;
                    } else {
                        width = Screenwidth * 13 / 100;
                    }
                    bitmap = Bitmap.createScaledBitmap(bitmap, width, width, true);
                    ImageSpan span = new ImageSpan(bitmap);
                    // drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
                    // drawable.getIntrinsicHeight());
                    // ImageSpan span = new ImageSpan(drawable,
                    // ImageSpan.ALIGN_BASELINE);
                    ss.setSpan(span, 0, ss.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
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
                Pattern sinaPatten = Pattern.compile(zhengze, Pattern.CASE_INSENSITIVE);
                Matcher matcher = sinaPatten.matcher(tempStr);
                ArrayList<String> list = new ArrayList<String>();
                int t = 0;
                while (matcher.find()) {
                    list.add(matcher.group());
                    t++;
                }
                Log.i("t", t + "");
                if (t > 0) {
                    String key = list.get(t - 1);
                    Log.i("key", key);
                    // 获取最后一个表情的位置
                    int i = tempStr.lastIndexOf("[");
                    String temp = tempStr.substring(i, selectionStart);
                    Log.i("temp", temp);
                    if (key.equals(temp)) {
                        edit.getEditableText().delete(i, selectionStart);
                        t--;
                    } else {
                        edit.getEditableText().delete(tempStr.length() - 1, selectionStart);
                    }
                } else {
                    edit.getEditableText().delete(tempStr.length() - 1, selectionStart);
                }
                Log.i("删除表情", edit.getEditableText() + "");
            }
        }
    }

    public class ExpressionImgAdapter extends BaseAdapter {
        private List<ExpressionItems> mList;
        private Context mContext;
        public static final int APP_PAGE_SIZE = 21;

        public ExpressionImgAdapter(Context context, List<ExpressionItems> list, int page) {
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
                View v = LayoutInflater.from(mContext).inflate(R.layout.app_item, null);

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

    private void showWaitDialog(String str) {
        waitProgressDialog = new Dialog(this,
                R.style.sports_dialog);
        LayoutInflater mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v1 = mInflater.inflate(R.layout.sports_progressdialog, null);
        TextView message = (TextView) v1.findViewById(R.id.message);
        message.setText(str);
        v1.setMinimumWidth((int) (SportsApp.ScreenWidth * 0.8));
        waitProgressDialog.setContentView(v1);
        waitProgressDialog.show();
    }

    @Override
    public void onPageResume() {
        // TODO Auto-generated method stub
        self = mSportsApp.getmSendMsgDetail();
        Log.e(TAG, "self-------------" + self);
        if (self != null) {
            int position = mListView.getSelectedItemPosition();
            FindMore f2 = new FindMore();
            f2.setSex(detail.getSex());
            f2.setOthername(detail.getUname());
            f2.setOtherimg(detail.getUimg());
            f2.setDetils(self.getMethod_str());
            String[] imgs = self.getUrls();
            f2.setImgs(imgs);
            f2.setBiggerImgs(self.getBigurls());
            f2.setTimes(self.getTimes() / 1000);
            f2.setFindId(self.getFindId());
            if (imgs.length == 1) {
                f2.setWidth(self.getWidth());
                f2.setHeight(self.getHeight());
            }
			/*mNewList.clear();
			mNewList.add(f2);
			for (int i = 0; i < mList.size(); i++) {
				mNewList.add(mList.get(i));
			}
			mList.clear();
			for (int i = 0; i <mNewList.size(); i++) {
				mList.add(mNewList.get(i));
			}*/
            mList.add(0, f2);
            mListView.setSelection(position + 1);
            sportsfindmeAdapter.notifyDataSetChanged();
            mPullSearchListView.onRefreshComplete();
            mSportsApp.setmSendMsgDetail(null);
        }
        MobclickAgent.onPageStart("FindMeActivity");
    }

    @Override
    public void onPagePause() {
        MobclickAgent.onPageEnd("FindMeActivity");
    }

    @Override
    public void onPageDestroy() {
        // TODO Auto-generated method stub

    }


    class SportsFindMeHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            switch (msg.what) {
                case FRESH_LIST:
                    if (times == 0) {
                        Log.d(TAG, "handleMessage mImgTimes == 0");

                        mListView.setAdapter(null);
                        sportsfindmeAdapter = new FindMeAdapter(context, mList, mSportsApp, imgItems, msportsFindMeHandler);
                        mListView.setAdapter(sportsfindmeAdapter);
                        sportsfindmeAdapter.setOnCheckedChangeListener(new OnCheckedChangeCommentListener() {

                            @Override
                            public void OnCheckedChangeListener(int position, String findId, int number, String to_id, String toName) {
                                // TODO Auto-generated method stub
                                listPosition = position;
                                find_Id = findId;
                                toNumber = number;
                                to_Id = to_id;
                                toNameStr = toName;
                                if (findBool == true) {
                                    findBool = false;
                                    int ss = findUpcommentText.getVisibility();
                                    Log.e(TAG, "ss------------" + ss);
                                    if (ss == 0) {
                                        showUIedit();
                                    }
                                    if (number == 1) {
                                        findUpcommentEdittext.setHint(getResources().getString(R.string.sports_private_msg_text_hint));
                                        findLayout.setVisibility(View.VISIBLE);
                                    }
                                    if (number == 2) {
                                        //回复
                                        findUpcommentEdittext.setHint(getResources()
                                                .getString(R.string.multi_comment_tip_target) + "  :" + toName);
                                        findLayout.setVisibility(View.VISIBLE);
                                    }
                                    scrollToComment(position);
                                } else {
                                    findLayout.setVisibility(View.GONE);
                                    findBool = true;
                                    hideedit();
                                }
                            }
                        });
                        mPullSearchListView.onRefreshComplete();
                    } else {
                        if (sportsfindmeAdapter == null) {
                            sportsfindmeAdapter = new FindMeAdapter(
                                    context, mList, mSportsApp, imgItems, msportsFindMeHandler);
                            sportsfindmeAdapter.setOnCheckedChangeListener(new OnCheckedChangeCommentListener() {
                                /**(position,findIdString,2,commentID,theFirstName)
                                 * 第一个参数所属发现在list中位置
                                 * 第二个参数发现ID，
                                 * 第三个参数用来判断评论还是回复1代表直接评论2代表回复
                                 * 第四个参数用来当回复的时候传递to_id,如果直接评论则为null
                                 * 第五个参数用来当回复的时候显示回复给谁，如果直接评论则null
                                 */
                                @Override
                                public void OnCheckedChangeListener(int position, String findId, int number, String to_id, String toName) {
                                    // TODO Auto-generated method stub
                                    listPosition = position;
                                    find_Id = findId;
                                    toNumber = number;
                                    to_Id = to_id;
                                    toNameStr = toName;
                                    if (findBool == true) {
                                        findBool = false;
                                        int ss = findUpcommentText.getVisibility();
                                        Log.e(TAG, "ss------------" + ss);
                                        if (ss == 0) {
                                            showUIedit();
                                        }
                                        if (number == 1) {
                                            findUpcommentEdittext.setHint(getResources().getString(R.string.sports_private_msg_text_hint));
                                            findLayout.setVisibility(View.VISIBLE);
                                        }
                                        if (number == 2) {
                                            //回复XX
                                            findUpcommentEdittext.setHint(getResources()
                                                    .getString(R.string.multi_comment_tip_target) + "  :" + toName);
                                            findLayout.setVisibility(View.VISIBLE);
                                        }
                                        scrollToComment(position);
                                    } else {
                                        findLayout.setVisibility(View.GONE);
                                        findBool = true;
                                        hideedit();
                                    }
                                }
                            });
                        }
                        sportsfindmeAdapter.notifyDataSetChanged();
                        mPullSearchListView.onRefreshComplete();
                    }
                    if (mLoadProgressDialog != null)
                        if (mLoadProgressDialog.isShowing())
                            mLoadProgressDialog.dismiss();
                    break;

                case FRESH_FAILED:
                    if (!mSportsApp.isOpenNetwork()) {
                        Toast.makeText(getApplication(),
                                getResources().getString(R.string.acess_server_error), Toast.LENGTH_SHORT).show();
                    } else if (context != null) {
                        Toast.makeText(getApplication(), getResources().getString(R.string.sports_get_list_failed), Toast.LENGTH_SHORT)
                                .show();
                    }
                    if (mLoadProgressDialog != null)
                        if (mLoadProgressDialog.isShowing())
                            mLoadProgressDialog.dismiss();
                    //sportsfindmeAdapter.notifyDataSetChanged();
                    mPullSearchListView.onRefreshComplete();

                    break;
                case FRESH_NULL:
                    if (context != null) {
                        if (mList.size() != 0) {
                            Toast.makeText(getApplication(), getResources().getString(R.string.sports_data_load_more_null), Toast.LENGTH_SHORT)
                                    .show();
                        } else {
                            Toast.makeText(getApplication(), getResources().getString(R.string.sports_upload_find_new_me), Toast.LENGTH_SHORT)
                                    .show();
                        }
                    }
                    if (mLoadProgressDialog != null)
                        if (mLoadProgressDialog.isShowing())
                            mLoadProgressDialog.dismiss();
                    if (sportsfindmeAdapter == null) {
                        mListView.setAdapter(null);
                        sportsfindmeAdapter = new FindMeAdapter(context, mList, mSportsApp, imgItems, msportsFindMeHandler);
                        mListView.setAdapter(sportsfindmeAdapter);
                    }
                    sportsfindmeAdapter.notifyDataSetChanged();
                    mPullSearchListView.onRefreshComplete();
                    break;
                case FRESH_DONE:
                    sportsfindmeAdapter.notifyDataSetChanged();
                    break;
                case HIDE_EDIT:
                    findLayout.setVisibility(View.GONE);
                    findBool = true;
                    hideedit();
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
            }, 500); //延时500毫秒,等findLayout显示完，并且软键盘弹出后再滑动
        }
    }

    private View getViewByPosition(int pos, ListView listView) {
        final int firstListItemPosition = listView.getFirstVisiblePosition();
        final int lastListItemPosition = firstListItemPosition + listView.getChildCount() - 1;
        if (pos < firstListItemPosition || pos > lastListItemPosition) {
            return listView.getAdapter().getView(pos, null, listView);
        } else {
            final int childIndex = pos - firstListItemPosition;
            return listView.getChildAt(childIndex);
        }
    }

    /**
     * scroll Vertical
     *
     * @param
     * @param y    垂直滑动的距离
     */
    private void scrollVertical(final int y) {
        if (mListView == null)
            return;
        runOnUiThread(new Runnable() { //执行自动化测试的时候模拟滑动需要进入UI线程操作
            @Override
            public void run() {
                invokeMethod(mListView, "trackMotionScroll", new Object[]{-y, -y}, new Class[]{int.class, int.class});
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
    private Object invokeMethod(Object object, String methodName, Object[] params, Class[] paramTypes) {
        Object returnObj = null;
        if (object == null) {
            return null;
        }
        Class cls = object.getClass();
        Method method = null;
        for (; cls != Object.class; cls = cls.getSuperclass()) { //因为取的是父类的默认修饰符的方法，所以需要循环找到该方法
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

    class SportsFindMeThread extends Thread {
        private long startTime = System.currentTimeMillis();

        @Override
        public void run() {
            // TODO Auto-generated method stub
            super.run();
            Message msg = null;
            ArrayList<FindMore> list = new ArrayList<FindMore>();

            try {
                list = (ArrayList<FindMore>) ApiJsonParser
                        .getFindListByUid(mSportsApp.getSessionId(), times, detail.getUid() + "", false, 0);

            } catch (ApiNetException e) {
                e.printStackTrace();
            } catch (ApiSessionOutException e) {
                e.printStackTrace();
            }
            if (times == 0) {
                mList.clear();
            }
            if (list != null) {
                Log.e(TAG, "length-------------" + list.size());
                Log.e(TAG, "length--------------" + list);
                if (list.size() == 0) {
                    msg = Message.obtain(msportsFindMeHandler, FRESH_NULL);
                    msg.sendToTarget();
                } else {
                    for (FindMore e : list) {
                        mList.add(e);
                    }
                    Log.e(TAG, "mList------------" + mList.size());
                    msg = Message.obtain(msportsFindMeHandler, FRESH_LIST);
                    msg.sendToTarget();
                }

            } else {
                if (list == null) {
                    Log.d(TAG, "*******检z4********");
                    msg = Message.obtain(msportsFindMeHandler, FRESH_FAILED);
                    msg.sendToTarget();
                }
            }
        }

    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            //发新说说
            case 99:
                Intent intent = new Intent(this, FindFriendsSendMsg.class);
                startActivity(intent);

                break;
            //更换背景图片
            case BACKGROUND_VIEW_ID:
                findLayout.setVisibility(View.GONE);
                hideedit();
                findBool = true;
                shotSelectImages();
                break;
            //回到开始位置
            case TOP_TITLE_LAYOUT_ID:
                times = 0;
                times = 0;
                if (mSportsApp.isOpenNetwork()) {
                    if (mSportsApp.getSessionId() != null && !mSportsApp.getSessionId().equals("")) {
                        SportsFindMeThread refreshThread = new SportsFindMeThread();
                        refreshThread.start();
                    }
                } else {
                    Toast.makeText(getApplicationContext(),
                            getResources().getString(R.string.acess_server_error), Toast.LENGTH_SHORT).show();
                    mListView.setSelectionFromTop(0, 0);
                    //sportsfindmeAdapter.notifyDataSetChanged();
                    mPullSearchListView.onRefreshComplete();
                    if (mLoadProgressDialog != null)
                        if (mLoadProgressDialog.isShowing())
                            mLoadProgressDialog.dismiss();
                }
                break;
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

            case USER_PHOTO_VIEW_ID:
                times = 0;
                SportsFindMeThread refreshThreads = new SportsFindMeThread();
                refreshThreads.start();
                break;
            case BACK_BUTTON:
            case BACK_LAYOUT:
                Handler findHandler = mSportsApp.getFindHandler();
                if (findHandler != null) {
                    findHandler.sendMessage(findHandler.obtainMessage(
                            FindOtherFragment.FRESH_VIEW));
                }
                finish();
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
                Handler findHandler = mSportsApp.getFindHandler();
                if (findHandler != null) {
                    findHandler.sendMessage(findHandler.obtainMessage(
                            FindOtherFragment.FRESH_VIEW));
                }
                finish();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
	/*----------------------------背景图更换代码如下-----------------------------*/


    public void shotSelectImages() {
        LayoutInflater inflater = LayoutInflater.from(this);
        myView = (LinearLayout) inflater.inflate(
                R.layout.select_images_from_local, null);

        myView.findViewById(R.id.btn_paizhao).setOnClickListener(
                this);
        myView.findViewById(R.id.btn_select_pic).setOnClickListener(
                this);
        myView.findViewById(R.id.btn_cancle).setOnClickListener(
                this);

        myWindow = new PopupWindow(myView,
                RelativeLayout.LayoutParams.FILL_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT, true);
        myWindow.setAnimationStyle(R.style.AnimationPopup);
        myWindow.setOutsideTouchable(true);
        myWindow.setBackgroundDrawable(new BitmapDrawable());
        myWindow.showAtLocation(right_btn, Gravity.BOTTOM, 0, 0);
        myWindow.setOnDismissListener(this);
        final Animation animation = (Animation) AnimationUtils.loadAnimation(
                this, R.anim.slide_in_from_bottom);
        myView.startAnimation(animation);
        pop_menu_background.setVisibility(View.VISIBLE);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case CAMERA_REQUEST_CODE:
                if (Tools.hasSdcard()) {
                    if (data != null && data.getData() != null) {
                        File tempFile = new File(
                                Environment.getExternalStorageDirectory()
                                        + File.separator + IMAGE_FILE_NAME);
                        startPhotoZoom(Uri.fromFile(tempFile));
                    }
                } else {
                    Toast.makeText(
                            getApplication(),
                            getResources()
                                    .getString(R.string.sports_toast_nosdcard),
                            Toast.LENGTH_LONG).show();
                }
                break;
            case RESULT_REQUEST_CODE:
                if (data != null) {
                    startPhotoZoom(data.getData());
                }
                break;
            case IMAGE_REQUEST_CODE:
                if (data != null) {
                    Log.i("data != null", "data != null" + data.getData());
                    getImageToView(data);
                }
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
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, FACE_PATH);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);
        startActivityForResult(intent, IMAGE_REQUEST_CODE);
    }

    private BitmapDrawable drawable;

    // 保存裁剪之后的图片数据
    private void getImageToView(Intent data) {

        final Bitmap bitmap = decodeUriAsBitmap(data.getData());
        if (Tools.SaveBitmapAsFile(FACE_PATH, bitmap)) {
            showWaitDialog(getResources().getString(R.string.change_photo_background));
            new AsyncTask<Void, Void, ApiBack>() {
                @Override
                protected ApiBack doInBackground(Void... params) {
                    // TODO Auto-generated method stub
                    ApiBack back = null;
                    try {
                        back = (ApiBack) ApiJsonParser.updateFindBg(mSportsApp.getSessionId(), FACE_PATH);
                    } catch (ApiNetException e) {
                        e.printStackTrace();
                    }
                    return back;
                }

                @Override
                protected void onPostExecute(ApiBack result) {
                    // TODO Auto-generated method stub
                    super.onPostExecute(result);
                    waitProgressDialog.dismiss();
                    if (result == null || result.getFlag() != 0) {
                        //上传失败
                        Toast.makeText(
                                getApplication(),
                                getResources().getString(
                                        R.string.upload_failed),
                                Toast.LENGTH_SHORT).show();
                    } else {
                        //上传成功
                        drawable = new BitmapDrawable(bitmap);
                        img_background.setImageDrawable(drawable);
                        Toast.makeText(
                                getApplication(),
                                getResources().getString(
                                        R.string.upload_success),
                                Toast.LENGTH_SHORT).show();
                    }
                }

            }.execute();
        }


    }

    private Bitmap decodeUriAsBitmap(Uri uri) {
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        return bitmap;
    }

    private void waitShowDialog() {
        // TODO Auto-generated method stub
        if (mLoadProgressDialog == null) {
            mLoadProgressDialog = new Dialog(this,
                    R.style.sports_dialog);
            LayoutInflater mInflater = getLayoutInflater();
            View v1 = mInflater.inflate(R.layout.bestgirl_progressdialog, null);
            mDialogMessage = (TextView) v1.findViewById(R.id.message);
            mDialogMessage.setText(R.string.bestgirl_wait);
            v1.setMinimumWidth((int) (SportsApp.ScreenWidth * 0.8));
            mLoadProgressDialog.setContentView(v1);
        }
        if (mLoadProgressDialog != null)
            if (!mLoadProgressDialog.isShowing()
                    && !isFinishing())
                mLoadProgressDialog.show();
        Log.i(TAG, "isFirstshow----");
    }

    @Override
    public void onDismiss() {
        // TODO Auto-generated method stub
        pop_menu_background.setVisibility(View.GONE);
    }
}
