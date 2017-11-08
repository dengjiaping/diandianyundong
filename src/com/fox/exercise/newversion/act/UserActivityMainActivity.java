package com.fox.exercise.newversion.act;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fox.exercise.AbstractBaseActivity;
import com.fox.exercise.ActivityInfoWebView;
import com.fox.exercise.FindOtherFragment;
import com.fox.exercise.FindOtherMoreAdapter;
import com.fox.exercise.ImageDownloader;
import com.fox.exercise.R;
import com.fox.exercise.RecordHelper;
import com.fox.exercise.SportsFoundImgActivity;
import com.fox.exercise.Util;
import com.fox.exercise.api.ApiBack;
import com.fox.exercise.api.ApiJsonParser;
import com.fox.exercise.api.ApiNetException;
import com.fox.exercise.api.ApiSessionOutException;
import com.fox.exercise.api.entity.ActionList;
import com.fox.exercise.api.entity.ExpressionItems;
import com.fox.exercise.api.entity.UserDetail;
import com.fox.exercise.api.entity.UserPrimsgAll;
import com.fox.exercise.api.entity.UserPrimsgOne;
import com.fox.exercise.bitmap.util.ImageResizer;
import com.fox.exercise.db.SportsContent;
import com.fox.exercise.login.LoginActivity;
import com.fox.exercise.newversion.entity.FindGroup;
import com.fox.exercise.newversion.entity.PraiseUsers;
import com.fox.exercise.newversion.entity.TopicContent;
import com.fox.exercise.newversion.newact.ShareDialogMainActivity;
import com.fox.exercise.newversion.newact.ZanListUserActivity;
import com.fox.exercise.pedometer.ImageWorkManager;
import com.fox.exercise.util.RoundedImage;
import com.fox.exercise.util.ScrollLayout;
import com.fox.exercise.util.ScrollLayout.OnViewChangeListener;
import com.umeng.analytics.MobclickAgent;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import cn.ingenic.indroidsync.SportsApp;

/**
 * @author loujungang 用户动态详情页面
 */
public class UserActivityMainActivity extends AbstractBaseActivity implements
        OnClickListener {
    private LinearLayout talklist;
    private String activityTime_2;
    private String sendToName;
    private ImageDownloader mDownloader = null;
    private ImageWorkManager mImageWorkerMan_Icon;
    private ImageResizer mImageWorker_Icon;
    private int a = 0,ba = 0;
    // 图片正方形
    private ImageResizer mImageWorker;
    private SportsApp mSportsApp;
    private int mItemHeight = 0;
    private UserDetail detail;
    private FindGroup mFindMore = null;
    private View activity_title_layout;
    private RoundedImage otherImage, dianzan_one, dianzan_two, dianzan_three,
            dianzan_four, dianzan_five, dianzan_six;
    private TextView nametext;
    private TextView tdetils;
    private TextView good_and_text;
    // 发布时间
    private TextView start_time, tx_sport_days;
    private TextView tx_sport_huodong, tx_sport_address;
    private TextView list_user_pinglun_layout, list_user_zan,
            list_user_zan_addone, list_user_pinglun, list_user_fenxiang,
            list_user_guanzhu_addone;
    private Button list_user_guanzhu;// 关注
    private ImageView list_user_fenxiang_layout;
    private RelativeLayout list_user_zan_layout;

    private LinearLayout activity_pinglun_layout, findbottomLayout, lay,pinglun_fenxiang_dianzan,pinglun_fenxiang_dianzannew;// 评论layout
    private TextView activity_submit_pinglun_num;// 评论个数

    private RelativeLayout activity_zan_layout, find_bottom;// 点赞layout
    private TextView dianzan_nums;// 点赞个数

    private View pinglunDetails;
    private LayoutInflater mInflater;

    private String theFirstName;
    private String theSecondName;
    // private String theSecondId;
    private String theTalkDetils;
    private String thewavComment;
    private String thewavDuration;
    // 评论时间
    private String pinglunTime;
    // 文字评论最终要显示的字符串
    private String newMessageString;
    // 评论者的名字或者回复评论者的名字
    private String newNameString;
    private int numcom;
    private Context mContext;
    int type;

    public List<ImageView> imgList2 = new ArrayList<ImageView>();
    public List<ImageView> imgList3 = new ArrayList<ImageView>();
    public List<ImageView> imgList4 = new ArrayList<ImageView>();
    public List<ImageView> imgList5 = new ArrayList<ImageView>();
    public List<ImageView> imgList6 = new ArrayList<ImageView>();
    public List<ImageView> imgList7 = new ArrayList<ImageView>();
    public List<ImageView> imgList8 = new ArrayList<ImageView>();
    public List<ImageView> imgList9 = new ArrayList<ImageView>();

    private ImageView img1;
    private ImageView img2;
    private ImageView img3;
    private ImageView img4;
    private ImageView img5;
    private ImageView img6;
    private ImageView img7;
    private ImageView img8;
    private ImageView img9;

    String[] urls;

    private ImageButton find_text_btn, find_expression_text_btn;// 切换录音和字体
    // 后面的是表情
    private Button find_press_btn;// 录音按钮
    private LinearLayout find_upcomment_text;// 输入文字layout
    private EditText find_upcomment_edittext;// 输入框
    private Button find_upcomment_send, find_unavailable;// 提交按钮切换

    private RecordHelper mRecorder;

    private LinearLayout layoutVoice;// 录音图标

    private Dialog dialog;

    private Boolean isShow;

    private boolean upTypeText = false;

    private RelativeLayout rScrollLayout;

    // private int findnumber = 0;

    private ScrollLayout scrollLayout;
    private String[] imgStr;
    private String[] imgStr2;
    private LinearLayout imgLayout;
    private List<ExpressionItems> imgItems;
    private List<ExpressionItems> imgItems2;
    private static final float APP_PAGE_SIZE = 21.0f;
    private int mViewCount;
    private int mCurSel;

    private LinearLayout zong_activity_layout;
    private List<ActionList> actionLists = new ArrayList<ActionList>();
    private List<String> activityNameList = new ArrayList<String>();
    private List<String> activityURLList = new ArrayList<String>();
    private List<Integer> activityIdList = new ArrayList<Integer>();
    private List<Integer> activitySendId = new ArrayList<Integer>();
    private List<Integer> activityNameSize = new ArrayList<Integer>();

    private ImageView wavBeginOne;
    MediaPlayer mPlayer = null;
    private static final int RECORD_LOADING = 6;
    private static final int RECORD_PREPARED = 7;
    private static final int RECORD_FINISH = 8;
    private static final int RECORD_PAUSE = 9;
    private static final int RECORD_ERROR = 10;
    private static final int FLAG_RUNWAV = 11;
    private static final int RESULT_ERROR = 12;
    private static final int FINDSINGLE = 13;
    private static final int LEFTBTID = 14;
    private static final int LEFTBTLAYOUT = 16;
    private static final int GETINFOS = 17;
    private boolean isStart = true;
    boolean isPause = false;
    private int currentDuration;
    private Dialog waitProgressDialog;

    private ImageView is_manorwomen_icon;
    private Animation animation;
    private int toFlag;
    private ImageView zan_more_icon;
    private Dialog alertDialog;
    private String toID = null;// 用来当回复的时候传递to_id,如果直接评论则为null 这个id是评论id
    private int isHuifuPinglun = 1;

    private int findId = -1;
    private int tp;//广场跳转标识用于区分加载字段

    private TextView tx_sport_del;// 删除按钮
    private RelativeLayout rl_infowebview;
    private View text_good_bgline;

    @Override
    public void initIntentParam(Intent intent) {
        // TODO Auto-generated method stub
        title = getResources().getString(R.string.user_activity_detail);
        mContext = this;
        mSportsApp = (SportsApp) getApplication();
        if (intent != null) {
            tp = intent.getIntExtra("tp", -1);
            // findid有值表示是从发现页面跳转过来的
            findId = intent.getIntExtra("findId", -1);
            if (findId == -1) {
                mFindMore = (FindGroup) intent
                        .getSerializableExtra("mFindMore");
                type = getIntent().getIntExtra("type", 0);
                if (mFindMore.getImgs() != null) {
                    type = mFindMore.getImgs().length;
                } else {
                    type = 0;
                }

                toFlag = getIntent().getIntExtra("toFlag", 0);
                showWaitDialog(mContext.getResources().getString(
                        R.string.comment_wait));
                GetInfoThread thread = new GetInfoThread(
                        Integer.parseInt(mFindMore.getFindId()));
                thread.start();
            } else {
                if (tp == 101) {
                    showWaitDialog(mContext.getResources().getString(
                            R.string.sports_wait));
                } else {
                    showWaitDialog(mContext.getResources().getString(
                            R.string.comment_wait));
                }
                GetInfoThread thread = new GetInfoThread(findId);
                thread.start();
            }
        }
    }

    @Override
    public void initView() {
        // TODO Auto-generated method stub
        // 主体
        showContentView(R.layout.activity_user_activity_main);
        mDownloader = new ImageDownloader(this);
        mDownloader.setType(ImageDownloader.OnlyOne);
        mImageWorkerMan_Icon = new ImageWorkManager(this, 0, 0);
        mImageWorker_Icon = mImageWorkerMan_Icon.getImageWorker();
        // 正方形图像
        mItemHeight = Util.getRealPixel_W(this,
                (int) (SportsApp.ScreenWidth * 0.625) / 3);
        mImageWorker = mSportsApp
                .getImageWorker(this, mItemHeight, mItemHeight);
        detail = SportsApp.getInstance().getSportUser();
        mRecorder = new RecordHelper();

        left_ayout.setId(+LEFTBTLAYOUT);
        left_ayout.setOnClickListener(this);
        leftButton.setId(+LEFTBTID);
        leftButton.setOnClickListener(this);
        new GetActionDataTask().execute();
        new GetActionDataTask2().execute();
    }

    @Override
    public void setViewStatus() {
        // TODO Auto-generated method stub

    }

    @Override
    public void onPageResume() {
        // TODO Auto-generated method stub
        MobclickAgent.onPageStart("UserActivityMainActivity");
    }

    @Override
    public void onPagePause() {
        // TODO Auto-generated method stub
        MobclickAgent.onPageEnd("UserActivityMainActivity");
    }

    @Override
    public void onPageDestroy() {
        // TODO Auto-generated method stub

    }

    private void init() {
        animation = AnimationUtils.loadAnimation(mContext, R.anim.add_one);
        mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        pinglun_fenxiang_dianzan = (LinearLayout) findViewById(R.id.pinglun_fenxiang_dianzan2);
        pinglun_fenxiang_dianzannew = (LinearLayout) activity_title_layout.findViewById(R.id.pinglun_fenxiang_dianzan);
        pinglun_fenxiang_dianzannew.setVisibility(View.GONE);
        talklist = (LinearLayout) activity_title_layout.findViewById(R.id.layout_talk_text);
        otherImage = (RoundedImage) activity_title_layout.findViewById(R.id.image_icon1);
        dianzan_one = (RoundedImage) activity_title_layout.findViewById(R.id.dianzan_one);
        dianzan_two = (RoundedImage) activity_title_layout.findViewById(R.id.dianzan_two);
        dianzan_three = (RoundedImage) activity_title_layout.findViewById(R.id.dianzan_three);
        dianzan_four = (RoundedImage) activity_title_layout.findViewById(R.id.dianzan_four);
        dianzan_five = (RoundedImage) activity_title_layout.findViewById(R.id.dianzan_five);
        dianzan_six = (RoundedImage) activity_title_layout.findViewById(R.id.dianzan_six);
        zan_more_icon = (ImageView) activity_title_layout.findViewById(R.id.zan_more_icon);
        rl_infowebview = (RelativeLayout) findViewById(R.id.rl_infowebview);
        text_good_bgline = (View) activity_title_layout.findViewById(R.id.text_good_bgline);
        text_good_bgline.setVisibility(View.GONE);
        nametext = (TextView) activity_title_layout
                .findViewById(R.id.sports_find_othername1);
        start_time = (TextView) activity_title_layout
                .findViewById(R.id.tx_start_times);
        tx_sport_days = (TextView) activity_title_layout
                .findViewById(R.id.tx_sport_days);
        if (mFindMore.getSportRecord() != null
                && !"".equals(mFindMore.getSportRecord())) {
            if (mFindMore.getSportRecord().getTime() != null
                    && !"".equals(mFindMore.getSportRecord().getTime())) {
                tx_sport_days.setText("运动第"
                        + mFindMore.getSportRecord().getTime() + "天 ");
            } else {
                tx_sport_days.setText("运动第0" + "天 ");
            }
        }

        tx_sport_address = (TextView) activity_title_layout
                .findViewById(R.id.tx_sport_address);
        tx_sport_address.setText(mFindMore.getComefrom());

        tx_sport_huodong = (TextView) activity_title_layout
                .findViewById(R.id.tx_sport_huodong);
        ArrayList<TopicContent> topicList = mFindMore.getTopicList();
        if (topicList != null && !"".equals(topicList)) {
            StringBuffer sBuffer = new StringBuffer();
            for (int i = 0; i < topicList.size(); i++) {
                sBuffer.append(topicList.get(i).getTitle() + "  ");
            }
            tx_sport_huodong.setText(sBuffer.toString());
        }

        tdetils = (TextView) activity_title_layout
                .findViewById(R.id.tx_detils1);
        good_and_text = (TextView) findViewById(R.id.good_and_text2);

        list_user_zan = (TextView)findViewById(R.id.list_user_zan2);
        list_user_zan_addone = (TextView)findViewById(R.id.list_user_zan_addone2);
        // list_user_zan.setOnClickListener(new
        // AddAnimian(list_user_zan_addone));
        list_user_pinglun = (TextView) activity_title_layout
                .findViewById(R.id.list_user_pinglun);
        list_user_fenxiang = (TextView) activity_title_layout
                .findViewById(R.id.list_user_fenxiang);
        list_user_guanzhu = (Button) activity_title_layout
                .findViewById(R.id.list_user_guanzhu);
        list_user_guanzhu_addone = (TextView) activity_title_layout
                .findViewById(R.id.list_user_guanzhu_addone);
        list_user_pinglun_layout = (TextView) activity_title_layout
                .findViewById(R.id.list_user_pinglun_layout);
        list_user_zan_layout = (RelativeLayout)findViewById(R.id.list_user_zan_layout2);
        list_user_fenxiang_layout = (ImageView) findViewById(R.id.sportshow_shaer2);
        list_user_pinglun.setOnClickListener(this);
        list_user_pinglun_layout.setOnClickListener(this);
        list_user_zan_layout.setOnClickListener(new AddAnimian(
                list_user_zan_addone));
        list_user_fenxiang_layout.setOnClickListener(this);
        good_and_text.setOnClickListener(this);

        if (mFindMore.getIsFriends() == 1) {
            list_user_guanzhu.setVisibility(View.GONE);
            list_user_guanzhu_addone.setVisibility(View.VISIBLE);
        } else {
            if (mFindMore.getOtheruid() == mSportsApp.getSportUser().getUid()) {
                list_user_guanzhu.setVisibility(View.GONE);
            } else {
                list_user_guanzhu.setVisibility(View.VISIBLE);
                list_user_guanzhu_addone.setVisibility(View.GONE);
                list_user_guanzhu.setOnClickListener(new AddAnimian(
                        list_user_guanzhu));
            }
        }
//		good_and_text.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				if (a == 0) {
//					a = 1;
//					findbottomLayout.setVisibility(View.VISIBLE);
//				} else {
//					a = 0;
//					findbottomLayout.setVisibility(View.GONE);
//				}
//
//			}
//		});

        activity_pinglun_layout = (LinearLayout) findViewById(R.id.activity_pinglun_layout);
        findbottomLayout = (LinearLayout) findViewById(R.id.findbottomLayout);
        find_bottom = (RelativeLayout) findViewById(R.id.find_bottom);
        lay = (LinearLayout) findViewById(R.id.lay);
        activity_submit_pinglun_num = (TextView) findViewById(R.id.activity_submit_pinglun_num);
        activity_zan_layout = (RelativeLayout) activity_title_layout.findViewById(R.id.activity_zan_layout);
        dianzan_nums = (TextView) findViewById(R.id.dianzan_nums);
        find_text_btn = (ImageButton) findViewById(R.id.find_text_btn);
        find_expression_text_btn = (ImageButton) findViewById(R.id.find_expression_text_btn);
        find_press_btn = (Button) findViewById(R.id.find_press_btn);
        find_press_btn.setVisibility(View.GONE);
        find_upcomment_text = (LinearLayout) findViewById(R.id.find_upcomment_text);
        layoutVoice = (LinearLayout) findViewById(R.id.layoutVoice);
        find_upcomment_edittext = (EditText) findViewById(R.id.find_upcomment_edittext);
        find_upcomment_send = (Button) findViewById(R.id.find_upcomment_send);
        find_unavailable = (Button) findViewById(R.id.find_unavailable);
        rScrollLayout = (RelativeLayout) findViewById(R.id.rScrollLayout);
        scrollLayout = (ScrollLayout) findViewById(R.id.ScrollLayoutTest);
        imgLayout = (LinearLayout) findViewById(R.id.imageLayot);
        is_manorwomen_icon = (ImageView) findViewById(R.id.is_manorwomen_icon);
        if ("man".equals(mFindMore.getSex())) {
            is_manorwomen_icon
                    .setImageResource(R.drawable.friends_group_sexman);
        } else if ("woman".equals(mFindMore.getSex())) {
            is_manorwomen_icon
                    .setImageResource(R.drawable.friends_group_sexwomen);
        }

        find_press_btn.setOnLongClickListener(new findClickListener());
        find_press_btn.setOnTouchListener(new findClickListener());
        find_text_btn.setOnClickListener(new findClickListener());
        find_expression_text_btn.setOnClickListener(new findClickListener());
        find_upcomment_send.setOnClickListener(new findClickListener());
        // find_upcomment_text.setVisibility(View.GONE);
        scrollLayout.SetOnViewChangeListener(new OnViewChangeListener() {

            @Override
            public void OnViewChange(int view) {
                // TODO Auto-generated method stub
                setCurPoint(view);
            }
        });

        // find_upcomment_edittext.requestFocus();
        find_upcomment_edittext.setOnClickListener(new findClickListener());
        find_upcomment_edittext.addTextChangedListener(mTextWatcher);
        find_upcomment_edittext
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
        zong_activity_layout = (LinearLayout) findViewById(R.id.zong_activity_layout);
        zong_activity_layout.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                rScrollLayout.setVisibility(View.GONE);
                findbottomLayout.setVisibility(View.GONE);
                rl_infowebview.setVisibility(View.VISIBLE);
                hideedit();
            }
        });

        tx_sport_del = (TextView) activity_title_layout
                .findViewById(R.id.tx_sport_del);
        if (mFindMore != null && !"".equals(mFindMore)) {
            if (mSportsApp.getSportUser().getUid() == mFindMore.getOtheruid()) {
                tx_sport_del.setVisibility(View.VISIBLE);
                tx_sport_del.setOnClickListener(new deleteListener(mFindMore
                        .getFindId()));
            } else {
                tx_sport_del.setVisibility(View.GONE);
                if (mSportsApp.mIsAdmin) {
                    tx_sport_del.setVisibility(View.VISIBLE);
                    tx_sport_del.setOnClickListener(new deleteListener(
                            mFindMore.getFindId()));
                }
            }
        } else {
            tx_sport_del.setVisibility(View.GONE);
        }

    }

    // 点击进入个人信息页面
    class personalInformationOnClickListener implements OnClickListener {
        private int userId;

        public personalInformationOnClickListener(int id) {
            this.userId = id;
        }

        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub
            if (!mSportsApp.isOpenNetwork()) {
                Toast.makeText(UserActivityMainActivity.this, "网络未连接，请检查网络！",
                        Toast.LENGTH_LONG).show();
                return;
            }
            // Intent intent = new Intent(UserActivityMainActivity.this,
            // PedometerActivity.class);
            Intent intent = new Intent(UserActivityMainActivity.this,
                    PersonalPageMainActivity.class);
            intent.putExtra("ID", userId);
            startActivity(intent);
        }

    }
    public SpannableString getExpressionString2(Context context, SpannableStringBuilder str) {
        SpannableString spanString = new SpannableString(str);
        // 正则表达式比配字符串里是否含有表情，如： 我好[开心]啊
        String zhengze = "\\[[^\\]]+\\]";
        // 通过传入的正则表达式来生成一个pattern
        Pattern sinaPatten = Pattern.compile(zhengze, Pattern.CASE_INSENSITIVE);
        try {
            Matcher matcher = sinaPatten.matcher(str);
            int resId = 0;
            while (matcher.find()) {
                String key = matcher.group();
                if (matcher.start() < 0) {
                    continue;
                }
                for (ExpressionItems item : imgItems2) {
                    if (item.getName().equals(key)) {
                        resId = item.getId();
                    }
                }
                if (resId == 0) {
                    continue;
                } else if (resId != 0) {
                    Bitmap bitmap = BitmapFactory.decodeResource(
                            context.getResources(), resId);
                    // Display display =getWindowManager().getDefaultDisplay();
                    int Screenwidth = (int) SportsApp.ScreenWidth;
                    int width = 0;
                    if (Screenwidth > 1000) {
                        width = Screenwidth * 19 / 100;
                    } else {
                        width = Screenwidth * 10 / 100;
                    }
                    bitmap = Bitmap.createScaledBitmap(bitmap, width, width,
                            true);
                    // 通过图片资源id来得到bitmap，用一个ImageSpan来包装
                    ImageSpan imageSpan = new ImageSpan(bitmap);
                    // 计算该图片名字的长度，也就是要替换的字符串的长度
                    int end = matcher.start() + key.length();
                    // 将该图片替换字符串中规定的位置中
                    spanString.setSpan(imageSpan, matcher.start(), end,
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            }
        } catch (Exception e) {
            Log.e("dealExpression", e.getMessage());
        }
        return spanString;
    }
    public SpannableString getExpressionString3(Context context, String str) {
        SpannableString spanString = new SpannableString(str);
        // 正则表达式比配字符串里是否含有表情，如： 我好[开心]啊
        String zhengze = "\\[[^\\]]+\\]";
        // 通过传入的正则表达式来生成一个pattern
        Pattern sinaPatten = Pattern.compile(zhengze, Pattern.CASE_INSENSITIVE);
        try {
            Matcher matcher = sinaPatten.matcher(str);
            int resId = 0;
            while (matcher.find()) {

                String key = matcher.group();
                if (matcher.start() < 0) {
                    continue;
                }
                for (ExpressionItems item : imgItems2) {
                    if (item.getName().equals(key)) {
                        resId = item.getId();
                    }
                }
                if (resId == 0) {
                    continue;
                } else if (resId != 0) {
                    Bitmap bitmap = BitmapFactory.decodeResource(
                            context.getResources(), resId);
                    // Display display =getWindowManager().getDefaultDisplay();
                    int Screenwidth = (int) SportsApp.ScreenWidth;
                    int width = 0;
                    if (Screenwidth > 1000) {
                        width = Screenwidth * 19 / 100;
                    } else {
                        width = Screenwidth * 10 / 100;
                    }
                    bitmap = Bitmap.createScaledBitmap(bitmap, width, width,
                            true);
                    // 通过图片资源id来得到bitmap，用一个ImageSpan来包装
                    ImageSpan imageSpan = new ImageSpan(bitmap);
                    // 计算该图片名字的长度，也就是要替换的字符串的长度
                    int end = matcher.start() + key.length();
                    // 将该图片替换字符串中规定的位置中
                    spanString.setSpan(imageSpan, matcher.start(), end,
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            }
        } catch (Exception e) {
            Log.e("dealExpression", e.getMessage());
        }
        return spanString;
    }
    public List<SpannableString> getExpressionString(Context context,
                                                     String str, int numcom, int firstname, int secondname) {
        List<SpannableString> list = new ArrayList<SpannableString>();
        SpannableString spanString = new SpannableString(str);
        // 正则表达式比配字符串里是否含有表情，如： 我好[开心]啊
        String zhengze = "\\[[^\\]]+\\]";
        // 通过传入的正则表达式来生成一个pattern
        Pattern sinaPatten = Pattern.compile(zhengze, Pattern.CASE_INSENSITIVE);
        try {
            // Log.e(TAG, "图片list之前------------"+str);

            if (numcom == 1) {
                int endone = firstname;
                spanString.setSpan(new ForegroundColorSpan(mContext
                                .getResources().getColor(R.color.remind2)), 0,
                        endone + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                spanString.setSpan(new ForegroundColorSpan(mContext
                                .getResources().getColor(R.color.gray_litter)),
                        endone + 1, str.length(),
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            } else {
                int endTwo = firstname;
                int endThree = endTwo + 2 + secondname;
                spanString.setSpan(new ForegroundColorSpan(mContext
                                .getResources().getColor(R.color.remind2)), 0, endTwo,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                spanString.setSpan(new ForegroundColorSpan(mContext
                                .getResources().getColor(R.color.gray_litter)),
                        endTwo, endTwo + 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                spanString.setSpan(new ForegroundColorSpan(mContext
                                .getResources().getColor(R.color.remind2)), endTwo + 2,
                        endThree + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                spanString.setSpan(new ForegroundColorSpan(mContext
                                .getResources().getColor(R.color.gray_litter)),
                        endThree + 1, str.length(),
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            Matcher matcher = sinaPatten.matcher(str);
            int resId = 0;
            while (matcher.find()) {
                String key = matcher.group();
                if (matcher.start() < 0) {
                    continue;
                }
                for (ExpressionItems item : imgItems) {
                    if (item.getName().equals(key)) {
                        resId = item.getId();
                    }
                }
                if (resId == 0) {
                    continue;
                } else if (resId != 0) {
                    Bitmap bitmap = BitmapFactory.decodeResource(
                            context.getResources(), resId);
                    // Display display =getWindowManager().getDefaultDisplay();
                    int Screenwidth = (int) SportsApp.ScreenWidth;
                    int width = 0;
                    if (Screenwidth > 1000) {
                        width = Screenwidth * 19 / 100;
                    } else {
                        width = Screenwidth * 10 / 100;
                    }
                    bitmap = Bitmap.createScaledBitmap(bitmap, width, width,
                            true);
                    // 通过图片资源id来得到bitmap，用一个ImageSpan来包装
                    ImageSpan imageSpan = new ImageSpan(bitmap);
                    // 计算该图片名字的长度，也就是要替换的字符串的长度
                    int end = matcher.start() + key.length();
                    // 将该图片替换字符串中规定的位置中
                    spanString.setSpan(imageSpan, matcher.start(), end,
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            }
            list.add(spanString);
        } catch (Exception e) {
            Log.e("dealExpression", e.getMessage());
        }
        return list;
    }

    public List<SpannableString> getNameString(Context context, String str,
                                               int numcom, int firstname, int secondname) {
        List<SpannableString> list = new ArrayList<SpannableString>();
        SpannableString spanString = new SpannableString(str);
        // 正则表达式比配字符串里是否含有表情，如： 我好[开心]啊
        // String zhengze = "\\[[^\\]]+\\]";
        // 通过传入的正则表达式来生成一个pattern
        // Pattern sinaPatten = Pattern.compile(zhengze,
        // Pattern.CASE_INSENSITIVE);
        try {
            // Log.e(TAG, "图片list之前------------"+str);

            if (numcom == 1) {
                int endone = firstname;
                spanString.setSpan(new ForegroundColorSpan(mContext
                                .getResources().getColor(R.color.remind2)), 0,
                        endone + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                // spanString.setSpan(new ForegroundColorSpan(mContext
                // .getResources().getColor(R.color.sports_value)),
                // endone + 1, str.length(),
                // Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            } else {
                int endTwo = firstname;
                int endThree = endTwo + 2 + secondname;
                spanString.setSpan(new ForegroundColorSpan(mContext
                                .getResources().getColor(R.color.remind2)), 0, endTwo,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                spanString.setSpan(new ForegroundColorSpan(mContext
                                .getResources().getColor(R.color.gray_litter)),
                        endTwo, endTwo + 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                spanString.setSpan(new ForegroundColorSpan(mContext
                                .getResources().getColor(R.color.remind2)), endTwo + 2,
                        endThree + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                // spanString.setSpan(new ForegroundColorSpan(mContext
                // .getResources().getColor(R.color.sports_value)),
                // endThree + 1, str.length(),
                // Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            // Matcher matcher = sinaPatten.matcher(str);
            // int resId = 0;
            // while (matcher.find()) {
            // String key = matcher.group();
            // if (matcher.start() < 0) {
            // continue;
            // }
            // for (ExpressionItems item : FindOtherFragment.imgItems) {
            // if (item.getName().equals(key)) {
            // resId = item.getId();
            // }
            // }
            // if (resId == 0) {
            // continue;
            // } else if (resId != 0) {
            // Bitmap bitmap = BitmapFactory.decodeResource(
            // context.getResources(), resId);
            // // Display display =getWindowManager().getDefaultDisplay();
            // int Screenwidth = (int) SportsApp.ScreenWidth;
            // int width = 0;
            // if (Screenwidth > 1000) {
            // width = Screenwidth * 19 / 100;
            // } else {
            // width = Screenwidth * 10 / 100;
            // }
            // bitmap = Bitmap.createScaledBitmap(bitmap, width, width,
            // true);
            // // 通过图片资源id来得到bitmap，用一个ImageSpan来包装
            // ImageSpan imageSpan = new ImageSpan(bitmap);
            // // 计算该图片名字的长度，也就是要替换的字符串的长度
            // int end = matcher.start() + key.length();
            // // 将该图片替换字符串中规定的位置中
            // spanString.setSpan(imageSpan, matcher.start(), end,
            // Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            // }
            // }
            list.add(spanString);
        } catch (Exception e) {
            Log.e("dealExpression", e.getMessage());
        }
        return list;
    }

    public List<SpannableString> getContentString(Context context, String str,
                                                  int numcom) {
        List<SpannableString> list = new ArrayList<SpannableString>();
        SpannableString spanString = new SpannableString(str);
        // 正则表达式比配字符串里是否含有表情，如： 我好[开心]啊
        String zhengze = "\\[[^\\]]+\\]";
        // 通过传入的正则表达式来生成一个pattern
        Pattern sinaPatten = Pattern.compile(zhengze, Pattern.CASE_INSENSITIVE);
        try {
            // Log.e(TAG, "图片list之前------------"+str);

            if (numcom == 1) {
                // spanString.setSpan(new ForegroundColorSpan(mContext
                // .getResources().getColor(R.color.remind)), 0,
                // endone + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                spanString.setSpan(new ForegroundColorSpan(mContext
                        .getResources().getColor(R.color.sports_value)), 0, str
                        .length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            } else {
                // spanString.setSpan(new ForegroundColorSpan(mContext
                // .getResources().getColor(R.color.remind)), 0, endTwo,
                // Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                // spanString.setSpan(new ForegroundColorSpan(mContext
                // .getResources().getColor(R.color.sports_value)),
                // endTwo, endTwo + 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                // spanString.setSpan(new ForegroundColorSpan(mContext
                // .getResources().getColor(R.color.remind)), endTwo + 2,
                // endThree + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                spanString.setSpan(new ForegroundColorSpan(mContext
                        .getResources().getColor(R.color.sports_value)), 0, str
                        .length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            Matcher matcher = sinaPatten.matcher(str);
            int resId = 0;
            while (matcher.find()) {
                String key = matcher.group();
                if (matcher.start() < 0) {
                    continue;
                }
                for (ExpressionItems item : imgItems) {
                    if (item.getName().equals(key)) {
                        resId = item.getId();
                    }
                }
                if (resId == 0) {
                    continue;
                } else if (resId != 0) {
                    Bitmap bitmap = BitmapFactory.decodeResource(
                            context.getResources(), resId);
                    // Display display =getWindowManager().getDefaultDisplay();
                    int Screenwidth = (int) SportsApp.ScreenWidth;
                    int width = 0;
                    if (Screenwidth > 1000) {
                        width = Screenwidth * 19 / 100;
                    } else {
                        width = Screenwidth * 10 / 100;
                    }
                    bitmap = Bitmap.createScaledBitmap(bitmap, width, width,
                            true);
                    // 通过图片资源id来得到bitmap，用一个ImageSpan来包装
                    ImageSpan imageSpan = new ImageSpan(bitmap);
                    // 计算该图片名字的长度，也就是要替换的字符串的长度
                    int end = matcher.start() + key.length();
                    // 将该图片替换字符串中规定的位置中
                    spanString.setSpan(imageSpan, matcher.start(), end,
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            }
            list.add(spanString);
        } catch (Exception e) {
            Log.e("dealExpression", e.getMessage());
        }
        return list;
    }

    private void checkView() {
        urls = mFindMore.getImgs();
        switch (type) {
            case 0:
                activity_title_layout = findViewById(R.id.activity_title_layout0);
                activity_title_layout.setVisibility(View.VISIBLE);
                break;
            case 1:
                activity_title_layout = findViewById(R.id.activity_title_layout1);
                activity_title_layout.setVisibility(View.VISIBLE);
                img1 = (ImageView) activity_title_layout
                        .findViewById(R.id.detils_img_one);

                // 上传的图片 就一张的时候
                int height = mFindMore.getHeight();
                int width = mFindMore.getWidth();
                if (width >= height) {
                    Log.e("---", "此图形是横着的---------");
                    LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) img1
                            .getLayoutParams();
                    int imgWidth = (int) (SportsApp.ScreenWidth * 0.5);
                    lp.height = (int) ((imgWidth * height) / width);
                    lp.width = imgWidth;
                    img1.setLayoutParams(lp);

                } else if (height > width) {
                    Log.e("---", "此图形是竖直的---------");
                    LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) img1
                            .getLayoutParams();
                    int imgHeight = (int) (SportsApp.ScreenHeight * 0.33);
                    lp.height = imgHeight;
                    lp.width = (int) ((imgHeight * width) / height);
                    img1.setLayoutParams(lp);
                }
                mDownloader.download(mFindMore.getImgs()[0], img1, null);
                break;
            case 2:
                activity_title_layout = findViewById(R.id.activity_title_layout2);
                activity_title_layout.setVisibility(View.VISIBLE);
                img1 = (ImageView) activity_title_layout
                        .findViewById(R.id.detils_img_one);
                img2 = (ImageView) activity_title_layout
                        .findViewById(R.id.detils_img_two);
                imgList2.clear();
                imgList2.add(img1);
                imgList2.add(img2);
                setImageVoid(type, 0, imgList2);
                break;
            case 3:
                activity_title_layout = findViewById(R.id.activity_title_layout3);
                activity_title_layout.setVisibility(View.VISIBLE);
                img1 = (ImageView) activity_title_layout
                        .findViewById(R.id.detils_img_one);
                img2 = (ImageView) activity_title_layout
                        .findViewById(R.id.detils_img_two);
                img3 = (ImageView) activity_title_layout
                        .findViewById(R.id.detils_img_three);
                imgList3.clear();
                imgList3.add(img1);
                imgList3.add(img2);
                imgList3.add(img3);
                setImageVoid(type, 0, imgList3);
                break;
            case 4:
                activity_title_layout = findViewById(R.id.activity_title_layout4);
                activity_title_layout.setVisibility(View.VISIBLE);
                img1 = (ImageView) activity_title_layout
                        .findViewById(R.id.detils_img_one);
                img2 = (ImageView) activity_title_layout
                        .findViewById(R.id.detils_img_two);
                img3 = (ImageView) activity_title_layout
                        .findViewById(R.id.detils_img_three);
                img4 = (ImageView) activity_title_layout
                        .findViewById(R.id.detils_img_four);
                imgList4.clear();
                imgList4.add(img1);
                imgList4.add(img2);
                imgList4.add(img3);
                imgList4.add(img4);
                setImageVoid(type, 0, imgList4);
                break;
            case 5:
                activity_title_layout = findViewById(R.id.activity_title_layout5);
                activity_title_layout.setVisibility(View.VISIBLE);
                img1 = (ImageView) activity_title_layout
                        .findViewById(R.id.detils_img_one);
                img2 = (ImageView) activity_title_layout
                        .findViewById(R.id.detils_img_two);
                img3 = (ImageView) activity_title_layout
                        .findViewById(R.id.detils_img_three);
                img4 = (ImageView) activity_title_layout
                        .findViewById(R.id.detils_img_four);
                // img5 = (ImageView) activity_title_layout
                // .findViewById(R.id.detils_img_five);
                imgList5.clear();
                imgList5.add(img1);
                imgList5.add(img2);
                imgList5.add(img3);
                imgList5.add(img4);
                // imgList5.add(img5);
                setImageVoid(type, 0, imgList5);
                break;
            case 6:
                activity_title_layout = findViewById(R.id.activity_title_layout6);
                activity_title_layout.setVisibility(View.VISIBLE);
                img1 = (ImageView) activity_title_layout
                        .findViewById(R.id.detils_img_one);
                img2 = (ImageView) activity_title_layout
                        .findViewById(R.id.detils_img_two);
                img3 = (ImageView) activity_title_layout
                        .findViewById(R.id.detils_img_three);
                img4 = (ImageView) activity_title_layout
                        .findViewById(R.id.detils_img_four);
                // img5 = (ImageView) activity_title_layout
                // .findViewById(R.id.detils_img_five);
                // img6 = (ImageView) activity_title_layout
                // .findViewById(R.id.detils_img_six);

                imgList6.clear();
                imgList6.add(img1);
                imgList6.add(img2);
                imgList6.add(img3);
                imgList6.add(img4);
                // imgList6.add(img5);
                // imgList6.add(img6);
                setImageVoid(type, 0, imgList6);
                break;
            case 7:
                activity_title_layout = findViewById(R.id.activity_title_layout7);
                activity_title_layout.setVisibility(View.VISIBLE);
                img1 = (ImageView) activity_title_layout
                        .findViewById(R.id.detils_img_one);
                img2 = (ImageView) activity_title_layout
                        .findViewById(R.id.detils_img_two);
                img3 = (ImageView) activity_title_layout
                        .findViewById(R.id.detils_img_three);
                img4 = (ImageView) activity_title_layout
                        .findViewById(R.id.detils_img_four);
                // img5 = (ImageView) activity_title_layout
                // .findViewById(R.id.detils_img_five);
                // img6 = (ImageView) activity_title_layout
                // .findViewById(R.id.detils_img_six);
                // img7 = (ImageView) activity_title_layout
                // .findViewById(R.id.detils_img_seven);

                imgList7.clear();
                imgList7.add(img1);
                imgList7.add(img2);
                imgList7.add(img3);
                imgList7.add(img4);
                // imgList7.add(img5);
                // imgList7.add(img6);
                // imgList7.add(img7);
                setImageVoid(type, 0, imgList7);
                break;
            case 8:
                activity_title_layout = findViewById(R.id.activity_title_layout8);
                activity_title_layout.setVisibility(View.VISIBLE);
                img1 = (ImageView) activity_title_layout
                        .findViewById(R.id.detils_img_one);
                img2 = (ImageView) activity_title_layout
                        .findViewById(R.id.detils_img_two);
                img3 = (ImageView) activity_title_layout
                        .findViewById(R.id.detils_img_three);
                img4 = (ImageView) activity_title_layout
                        .findViewById(R.id.detils_img_four);
                // img5 = (ImageView) activity_title_layout
                // .findViewById(R.id.detils_img_five);
                // img6 = (ImageView) activity_title_layout
                // .findViewById(R.id.detils_img_six);
                // img7 = (ImageView) activity_title_layout
                // .findViewById(R.id.detils_img_seven);
                // img8 = (ImageView) activity_title_layout
                // .findViewById(R.id.detils_img_eight);

                imgList8.clear();
                imgList8.add(img1);
                imgList8.add(img2);
                imgList8.add(img3);
                imgList8.add(img4);
                // imgList8.add(img5);
                // imgList8.add(img6);
                // imgList8.add(img7);
                // imgList8.add(img8);
                setImageVoid(type, 0, imgList8);

                break;
            case 9:
                activity_title_layout = findViewById(R.id.activity_title_layout9);
                activity_title_layout.setVisibility(View.VISIBLE);
                img1 = (ImageView) activity_title_layout
                        .findViewById(R.id.detils_img_one);
                img2 = (ImageView) activity_title_layout
                        .findViewById(R.id.detils_img_two);
                img3 = (ImageView) activity_title_layout
                        .findViewById(R.id.detils_img_three);
                img4 = (ImageView) activity_title_layout
                        .findViewById(R.id.detils_img_four);
                // img5 = (ImageView) activity_title_layout
                // .findViewById(R.id.detils_img_five);
                // img6 = (ImageView) activity_title_layout
                // .findViewById(R.id.detils_img_six);
                // img7 = (ImageView) activity_title_layout
                // .findViewById(R.id.detils_img_seven);
                // img8 = (ImageView) activity_title_layout
                // .findViewById(R.id.detils_img_eight);
                // img9 = (ImageView) activity_title_layout
                // .findViewById(R.id.detils_img_nine);

                imgList9.clear();
                imgList9.add(img1);
                imgList9.add(img2);
                imgList9.add(img3);
                imgList9.add(img4);
                // imgList9.add(img5);
                // imgList9.add(img6);
                // imgList9.add(img7);
                // imgList9.add(img8);
                // imgList9.add(img9);
                setImageVoid(type, 0, imgList9);

                break;

            default:
                break;
        }

        if (img1 != null) {
            img1.setOnClickListener(new imgOnClickListener(0, mFindMore
                    .getBiggerImgs()));
        }
        if (img2 != null) {
            img2.setOnClickListener(new imgOnClickListener(1, mFindMore
                    .getBiggerImgs()));
        }
        if (img3 != null) {
            img3.setOnClickListener(new imgOnClickListener(2, mFindMore
                    .getBiggerImgs()));
        }
        if (img4 != null) {
            img4.setOnClickListener(new imgOnClickListener(3, mFindMore
                    .getBiggerImgs()));
        }
        if (img5 != null) {
            img5.setOnClickListener(new imgOnClickListener(4, mFindMore
                    .getBiggerImgs()));
        }
        if (img6 != null) {
            img6.setOnClickListener(new imgOnClickListener(5, mFindMore
                    .getBiggerImgs()));
        }
        if (img7 != null) {
            img7.setOnClickListener(new imgOnClickListener(6, mFindMore
                    .getBiggerImgs()));
        }
        if (img8 != null) {
            img8.setOnClickListener(new imgOnClickListener(7, mFindMore
                    .getBiggerImgs()));
        }
        if (img9 != null) {
            img9.setOnClickListener(new imgOnClickListener(8, mFindMore
                    .getBiggerImgs()));
        }
    }

    private void setImageVoid(int type, int position, List<ImageView> imgList) {
        for (int i = 0; i < type; i++) {
            LinearLayout.LayoutParams lps = (LinearLayout.LayoutParams) imgList
                    .get(i).getLayoutParams();
            // lps.width = (int) (SportsApp.ScreenWidth * 0.625) / 3;
            // lps.height = (int) (SportsApp.ScreenWidth * 0.625) / 3;
            lps.width = (int) (SportsApp.ScreenWidth * 0.635) / 2;
            lps.height = (int) (SportsApp.ScreenWidth * 0.635) / 2;
            imgList.get(i).setLayoutParams(lps);
            mImageWorker.loadImage(urls[i], imgList.get(i), null, null, false);
        }

    }

    class findClickListener implements OnClickListener, OnLongClickListener,
            OnTouchListener {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            // TODO Auto-generated method stub
            switch (event.getAction()) {
                case MotionEvent.ACTION_UP:
                    if (v == find_press_btn) {
                        mRecorder.stop();
                        if (dialog != null) {
                            dialog.dismiss();
                            dialog = null;
                            layoutVoice.setVisibility(View.GONE);
                            if (mRecorder.sampleLength() < 1) {
                                Toast.makeText(
                                        UserActivityMainActivity.this,
                                        getResources().getString(
                                                R.string.sports_record_fail),
                                        Toast.LENGTH_SHORT).show();
                                break;
                            }
                        /*
                         * UploadThread t = new
						 * UploadThread("/sdcard/Recording/" +
						 * (RecordHelper.mSampleFile).getName(), "",
						 * RecordHelper.mSampleLength); t.start();
						 */
                            Log.e(TAG, "音频-文字---------"
                                    + find_upcomment_edittext.getText().toString());
                            Log.e(TAG, "音频-文件---------" + "/sdcard/Recording/"
                                    + (RecordHelper.mSampleFile).getName());
                            Log.e(TAG, "音频-长度---------"
                                    + RecordHelper.mSampleLength + "");
                            // sportsfindmoreAdapter.send(listPosition, toNameStr,
                            // find_Id, to_Id, findUpcommentEdittext.getText()
                            // .toString(), "/sdcard/Recording/"
                            // + (RecordHelper.mSampleFile).getName(),
                            // RecordHelper.mSampleLength + "");
                            send(mFindMore.getFindId(), toID + "",
                                    find_upcomment_edittext.getText().toString(),
                                    "/sdcard/Recording/"
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
            if (v == find_press_btn) {
                mRecorder.startRecording(MediaRecorder.OutputFormat.DEFAULT,
                        ".mp3", UserActivityMainActivity.this);
                dialog = new Dialog(UserActivityMainActivity.this,
                        R.style.share_dialog2);
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
                    find_press_btn.setVisibility(View.GONE);
                    find_upcomment_text.setVisibility(View.VISIBLE);
                    hideedit();
                    if (isShow == false) {
                        isShow = true;
                        rScrollLayout.setVisibility(View.VISIBLE);
                        if (upTypeText) {
                            upTypeText = false;
                            find_text_btn
                                    .setBackgroundResource(R.drawable.sk2text);
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
                        find_upcomment_text.setVisibility(View.GONE);
                        find_text_btn.setBackgroundResource(R.drawable.sk2voice);
                        find_press_btn.setVisibility(View.VISIBLE);
                        findbottomLayout.setVisibility(View.GONE);
                        rl_infowebview.setVisibility(View.VISIBLE);
                        hideedit();
                    } else {
                        upTypeText = false;
                        find_press_btn.setVisibility(View.GONE);
                        find_upcomment_text.setVisibility(View.VISIBLE);
                        showedit();
                        find_text_btn.setBackgroundResource(R.drawable.sk2text);
                    }
                    break;
                case R.id.find_upcomment_edittext:
                    if (isShow == true) {
                        isShow = false;
                        rScrollLayout.setVisibility(View.GONE);
                        Log.i("", "lalaalla2222");
                    }
                    break;
                case R.id.find_upcomment_send:
                    /**
                     * 第一个参数是评论所属发现在list中的位置 第二个参数用来当回复的时候显示回复给谁，如果直接评论则null 第三个发现ID
                     * 第四个用来当回复的时候传递to_id,如果直接评论则为null 其余三个分别是文本内容，音频，音频时间
                     * */
//                    mSportsApp.setFindId(mFindMore.getFindId());
//                    mSportsApp.setTextString(find_upcomment_edittext.getText().toString());
//                    mSportsApp.setToId(toID + "");
                    if (sendToName != null){
                        mSportsApp.setToName(sendToName);
                    }
                    send(mFindMore.getFindId(), toID + "", find_upcomment_edittext
                            .getText().toString(), null, null);
                    sendToName = null;
                    find_upcomment_edittext.setText("");
                    hideedit();
                    findbottomLayout.setVisibility(View.GONE);
                    rl_infowebview.setVisibility(View.VISIBLE);
                    rScrollLayout.setVisibility(View.GONE);
                    break;
                default:
                    break;
            }
        }

    }

    // 软键盘消失
    private void hideedit() {
        // findUpcommentEdittext.setText("");
        find_upcomment_edittext.clearFocus();
        // close InputMethodManager
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(find_upcomment_edittext.getWindowToken(), 0);
    }

    // 显示软键盘
    private void showedit() {
        find_upcomment_edittext.setFocusable(true);
        find_upcomment_edittext.setFocusableInTouchMode(true);
        find_upcomment_edittext.requestFocus();
        InputMethodManager inputManager = (InputMethodManager) find_upcomment_edittext
                .getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.showSoftInput(find_upcomment_edittext, 0);
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

    public void initViews() {
        imgStr = getResources().getStringArray(R.array.imageStr_array);
        imgStr2 = getResources().getStringArray(R.array.daka_array);
        imgItems = new ArrayList<ExpressionItems>();
        imgItems2 = new ArrayList<ExpressionItems>();
        int j = 0,k = 0;
        Field[] files1 = R.drawable.class.getDeclaredFields();//打卡的图片列表
        for (Field file : files1) {
            if (file.getName().startsWith("daka")) {
                ApplicationInfo appInfo = UserActivityMainActivity.this.getApplicationInfo();
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
                ApplicationInfo appInfo = getApplicationInfo();
                int resID = getResources().getIdentifier(file.getName(),
                        "drawable", appInfo.packageName);
                ExpressionItems item = new ExpressionItems();
                item.setId(resID);
                item.setName(imgStr[j].toString());
                item.setIsCanel(false);
                imgItems.add(item);
                j++;
                // System.out.println(file.getName());
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
                delStr(find_upcomment_edittext);
            } else {
                int d = item.getId();
                Drawable drawable = getResources().getDrawable(d);
                CharSequence cs = item.getName();
                SpannableString ss = new SpannableString(cs);
                if (drawable != null) {
                    Bitmap bitmap = BitmapFactory.decodeResource(
                            getResources(), d);
                    DisplayMetrics mDisplayMetrics = new DisplayMetrics();
                    getWindowManager().getDefaultDisplay().getMetrics(
                            mDisplayMetrics);
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
                    // drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
                    // drawable.getIntrinsicHeight());
                    // ImageSpan span = new ImageSpan(drawable,
                    // ImageSpan.ALIGN_BASELINE);
                    ss.setSpan(span, 0, ss.length(),
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    int cursor = find_upcomment_edittext.getSelectionStart();
                    find_upcomment_edittext.getText().insert(cursor, ss);
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
                        edit.getEditableText().delete(tempStr.length() - 1,
                                selectionStart);
                    }
                } else {
                    edit.getEditableText().delete(tempStr.length() - 1,
                            selectionStart);
                }
                Log.i("删除表情", edit.getEditableText() + "");
            }
        }
    }

    TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
            // TODO Auto-generated method stub
            String editable = find_upcomment_edittext.getText().toString();
            String str = filterEmoji(editable.toString());
            if (!editable.equals(str)) {
                find_upcomment_edittext.setText(str);
                // 设置新的光标所在位置
                find_upcomment_edittext.setSelection(str.length());
                // 暂不支持此类型符号的输入
                Toast.makeText(UserActivityMainActivity.this,
                        getResources().getString(R.string.does_not_this_input),
                        Toast.LENGTH_SHORT).show();
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
            String editable = find_upcomment_edittext.getText().toString();
            Boolean isTrue = stringFilter(editable.toString());
            int i = 0;
            if (isTrue) {
                // 除空格回车外有其他字符
                find_upcomment_send.setVisibility(View.VISIBLE);
                find_unavailable.setVisibility(View.GONE);
            } else {
                // 只有空格字符
                find_upcomment_send.setVisibility(View.GONE);
                find_unavailable.setVisibility(View.VISIBLE);
            }
        }
    };

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

    // 监听图片
    class imgOnClickListener implements OnClickListener {
        private int index;
        private String[] urlString;

        public imgOnClickListener(int num, String[] url) {
            // TODO Auto-generated constructor stub
            this.urlString = url;
            this.index = num;
        }

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            // Toast.makeText(mContext, "-----第"+index
            // +"张-----地址为"+urlString[index],
            // Toast.LENGTH_LONG).show();
            Log.e(TAG, "点击----" + urlString.length + "张");
            for (int i = 0; i < urlString.length; i++) {
                Log.e(TAG, "点击-----第--" + "i" + "---地址----" + urlString[i]);
            }
            Intent intent = new Intent(UserActivityMainActivity.this,
                    SportsFoundImgActivity.class);
            Bundle bundle = new Bundle();
            bundle.putStringArray("urlString", urlString);
            bundle.putInt("index", index);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }

    public static String ToDBC(String input) {
        char[] c = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == 12288) {
                c[i] = (char) 32;
                continue;
            }
            if (c[i] > 65280 && c[i] < 65375)
                c[i] = (char) (c[i] - 65248);
        }
        return new String(c);
    }

    // 录音播放
    Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
            /*
			 * case ADD_ITEM: UserPrimsgOne userPrimsgOne = (UserPrimsgOne)
			 * msg.obj; mAdapter.addItem(userPrimsgOne); break;
			 */
                case RECORD_LOADING:
                    ImageView wavBegin = (ImageView) msg.obj;
                    wavBegin.setImageResource(+R.anim.record_loading);
                    AnimationDrawable ad1 = (AnimationDrawable) wavBegin
                            .getDrawable();
                    ad1.start();
                    break;
                case RECORD_PREPARED:
                    ((ImageView) msg.obj).setImageResource(+R.anim.record_run);
                    // mWavBegin = ((ImageView) msg.obj);
                    AnimationDrawable ad2 = (AnimationDrawable) ((ImageView) msg.obj)
                            .getDrawable();
                    ad2.start();
                    break;
                case RECORD_PAUSE:
                case RECORD_ERROR:
                case RECORD_FINISH:
                    ((ImageView) msg.obj).setImageResource(R.drawable.audio_ani_1);
                    isStart = true;
                    // if (mPlayer != null && mPlayer.isPlaying())
                    // mPlayer.pause();
                    // mRecorder.destory();
                    break;
                case FLAG_RUNWAV:
                    TextView userWavTimes = (TextView) msg.obj;
                    String cont = userWavTimes.getText().toString();
                    if (!"".equals(cont) && cont != null) {
                        int number = Integer.parseInt(cont.split("次播放")[0]) + 1;
                        (userWavTimes).setText(Integer.toString(number) + "次播放");
                    }
                    break;

                case RESULT_ERROR:
                    Log.e(TAG, "msg_ERROR");
                    String message = (String) msg.obj;
                    Toast.makeText(mContext, message, Toast.LENGTH_LONG).show();
                    break;
                case FINDSINGLE:
                    if (waitProgressDialog != null
                            || waitProgressDialog.isShowing()) {
                        waitProgressDialog.dismiss();
                    }

                    FindGroup findGroup = (FindGroup) msg.obj;
                    if (findGroup == null) {
                        Toast.makeText(mContext, R.string.sports_failed_please_checknet, Toast.LENGTH_LONG)
                                .show();
                    } else {
                        mFindMore = findGroup;
                        if (mFindMore.getTalkdetils() != null
                                && mFindMore.getTalkdetils().size() > 0) {
                            activity_pinglun_layout.setVisibility(View.VISIBLE);
                            talklist.removeAllViews();
                            activity_pinglun_layout.removeAllViews();
                        } else {
                            activity_pinglun_layout.setVisibility(View.GONE);
                        }
                        setZanAndPinglun();

                        if (mFindMore.getpArrayList() != null
                                && mFindMore.getpArrayList().size() > 0) {
                            showZanImage(mFindMore.getpArrayList());
                        } else {
                            activity_zan_layout.setVisibility(View.GONE);
                        }

                    }
                    break;

                case GETINFOS:
                    if (waitProgressDialog != null
                            || waitProgressDialog.isShowing()) {
                        waitProgressDialog.dismiss();
                    }

                    FindGroup mfindGroup = (FindGroup) msg.obj;
                    if (mfindGroup == null) {
                        Toast.makeText(mContext, R.string.sports_failed_please_checknet, Toast.LENGTH_LONG)
                                .show();
                    } else {
                        mFindMore = mfindGroup;
                        if (mFindMore.getImgs() != null) {
                            type = mFindMore.getImgs().length;
                        } else {
                            type = 0;
                        }
                        setAllView();
                    }
                    break;

            }
        }

    };

    // 上传评论
    public void send(final String findID, final String toID,
                     final String commentText, final String commentWav,
                     final String wavDuration) {
        mSportsApp.setFindId(findID);
        mSportsApp.setTextString(commentText);
        mSportsApp.setToId(toID + "");
//        mSportsApp.setToName(theSecondName);
        mSportsApp.setWav(commentWav);
        mSportsApp.setWavtime(wavDuration);
        /**
         * 第一个参数是评论所属发现在list中的位置 第二个参数用来当回复的时候显示回复给谁，如果直接评论则null 第三个发现ID
         * 第四个用来当回复的时候传递to_id,如果直接评论则为null 其余三个分别是文本内容，音频，音频时间
         * */
		/*
		 * Log.e(TAG, "音频1-文字---------"+commentText); Log.e(TAG,
		 * "音频1-文件---------"+commentWav); Log.e(TAG,
		 * "音频1-长度---------"+wavDuration);
		 */
        showWaitDialog(mContext.getResources().getString(R.string.comment_wait));
        new AsyncTask<Void, Void, String>() {

            @Override
            protected String doInBackground(Void... params) {
                // TODO Auto-generated method stub
                String commentIdString = null;
                try {
                    commentIdString = ApiJsonParser.addComment(
                            mSportsApp.getSessionId(), findID, commentText,
                            commentWav, wavDuration, toID);
                } catch (ApiNetException e) {
                    e.printStackTrace();
                }
                return commentIdString;
            }

            @Override
            protected void onPostExecute(String commentId) {
                // TODO Auto-generated method stub
                super.onPostExecute(commentId);
                if (commentId == null || commentId.equals("")) {
                    waitProgressDialog.dismiss();
                    Toast.makeText(
                            mContext,
                            mContext.getResources().getString(
                                    R.string.upload_failed), Toast.LENGTH_SHORT)
                            .show();
                } else {
                    // 上传成功
                    // waitProgressDialog.dismiss();
                    // FindComment fc = new FindComment();
                    // SportCircleComments sportCircleComments = new
                    // SportCircleComments();
                    //
                    // fc.setCommentId(commentId);
                    // fc.setFirstName(detail.getUname());
                    // if (theFirstName != null && !theFirstName.equals("")) {
                    // fc.setSecondName(theFirstName);
                    // }
                    // fc.setCommentText(commentText);
                    // fc.setCommentWav(commentWav);
                    // fc.setWavDuration(wavDuration);
                    // /*
                    // *
                    // fc.setCommentWav("http://mp3.9ku.com/file2/177/176318.mp3"
                    // * ); fc.setWavDuration("60");
                    // */
                    // // Log.e(TAG, "长度1-------"+commentText.length());
                    // if (mFindMore.getTalkdetils() == null) {
                    // ArrayList<SportCircleComments> fList = new
                    // ArrayList<SportCircleComments>();
                    // mFindMore.setTalkdetils(fList);
                    // }
                    // mFindMore.getTalkdetils().add(fc);
                    // int beforeCommentNum = mList.get(position)
                    // .getCommentCount();
                    // mList.get(position).setCommentCount(beforeCommentNum +
                    // 1);
                    // notifyDataSetChanged();
                    // Toast.makeText(
                    // mContext,
                    // mContext.getResources().getString(
                    // R.string.upload_success),
                    // Toast.LENGTH_SHORT).show();
                    UserActivityMainActivity.this.toID = null;
                    GetSingleThread thread = new GetSingleThread(
                            Integer.parseInt(mFindMore.getFindId()));
                    thread.start();
                }
            }

        }.execute();
    }

    private void showWaitDialog(String str) {
        waitProgressDialog = new Dialog(mContext, R.style.sports_dialog);
        LayoutInflater mInflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v1 = mInflater.inflate(R.layout.sports_progressdialog, null);
        TextView message = (TextView) v1.findViewById(R.id.message);
        message.setText(str);
        v1.setMinimumWidth((int) (SportsApp.ScreenWidth * 0.8));
        waitProgressDialog.setContentView(v1);
        waitProgressDialog.show();
    }

    /**
     * 设置点赞和评论
     */
    private void setZanAndPinglun() {
        Drawable drawable1 = mContext.getResources().getDrawable(
                R.drawable.sportshow_zan_black);
        drawable1.setBounds(0, 0, drawable1.getMinimumWidth(),
                drawable1.getMinimumHeight());
        Drawable drawable2 = mContext.getResources().getDrawable(
                R.drawable.sportshow_zan);
        drawable2.setBounds(0, 0, drawable2.getMinimumWidth(),
                drawable2.getMinimumHeight());
        if (mFindMore.getGoodpeople() > 0) {
            if (mFindMore.isGood()) {
                list_user_zan.setCompoundDrawables(drawable2, null, null, null);
            } else {
                list_user_zan.setCompoundDrawables(drawable1, null, null, null);
            }
            list_user_zan.setText("(" + mFindMore.getGoodpeople() + ")");
            activity_zan_layout.setVisibility(View.VISIBLE);
            // dianzan_nums.setText(mFindMore.getGoodpeople() + "人点赞");
            if (mFindMore.getpArrayList() != null
                    && !"".equals(mFindMore.getpArrayList())) {
                Collections.reverse(mFindMore.getpArrayList());
                showZanImage(mFindMore.getpArrayList());
            } else {
                list_user_zan.setCompoundDrawables(drawable1, null, null, null);
                list_user_zan.setText("(0)");
                activity_zan_layout.setVisibility(View.GONE);
            }

        } else {
            list_user_zan.setCompoundDrawables(drawable1, null, null, null);
            list_user_zan.setText("(0)");
            activity_zan_layout.setVisibility(View.GONE);
        }
        if (mFindMore.getcCount() > 0) {
            list_user_pinglun.setText("回复 (" + mFindMore.getcCount() + ")");
//            activity_pinglun_layout.setVisibility(View.VISIBLE);
//            activity_submit_pinglun_num.setText(mFindMore.getcCount() + "回复");
            // 将评论内容显示出来
            for (int i = 0; i < mFindMore.getTalkdetils().size(); i++) {
                theFirstName = mFindMore.getTalkdetils().get(i).getName();
                theSecondName = mFindMore.getTalkdetils().get(i).getTo_name();
                theTalkDetils = mFindMore.getTalkdetils().get(i).getContent();
                thewavComment = mFindMore.getTalkdetils().get(i).getWav();
                thewavDuration = mFindMore.getTalkdetils().get(i).getWavtime();
                // 如果是语音回复
                int firstname, secondname = 0;
                if (thewavComment != null && thewavDuration != null
                        && !thewavDuration.equals("null")
                        && !thewavDuration.equals("0")) {
                    pinglunDetails = mInflater.inflate(
                            R.layout.user_activity_yuyin_pinglun_layout, null);
                    RoundedImage activity_image_yuyinicon = (RoundedImage) pinglunDetails
                            .findViewById(R.id.activity_image_yuyinicon);
                    TextView find_talk_detils_text_name = (TextView) pinglunDetails
                            .findViewById(R.id.find_talk_detils_text_name);
                    LinearLayout recordlayout = (LinearLayout) pinglunDetails
                            .findViewById(R.id.recoding_click_find);
                    TextView durationtext = (TextView) pinglunDetails
                            .findViewById(R.id.wav_durations_find);
                    ImageView beginWav = (ImageView) pinglunDetails
                            .findViewById(R.id.wav_begin_find);
                    TextView activity_submit_time = (TextView) pinglunDetails
                            .findViewById(R.id.activity_submit_time);

                    // 评论时间
                    long pinglTm = System.currentTimeMillis()
                            - Long.valueOf(mFindMore.getTalkdetils().get(i)
                            .getInputtime()) * 1000;
                    SimpleDateFormat pformat = new SimpleDateFormat(
                            "yyyy-MM-dd HH:mm");
                    if (pinglTm <= 60 * 1000)
                        // 一分钟内显示刚刚
                        activity_submit_time.setText(getResources().getString(
                                R.string.sports_time_justnow));
                    else if (pinglTm <= 60 * 60 * 1000) {
                        int h = (int) (pinglTm / 1000 / 60);
                        // 一小时内显示多少分钟前
                        activity_submit_time.setText(""
                                + h
                                + getResources().getString(
                                R.string.sports_time_mins_ago));
                    } else {
                        activity_submit_time.setText(pformat.format(Long
                                .valueOf(mFindMore.getTalkdetils().get(i)
                                        .getInputtime()) * 1000));
                    }
                    if ("".equals(mFindMore.getTalkdetils().get(i).getImg())
                            || mFindMore.getTalkdetils().get(i).getImg() == null) {
                        if (mFindMore.getTalkdetils().get(i).getSex()
                                .equals("1")) {
                            activity_image_yuyinicon
                                    .setImageResource(R.drawable.sports_user_edit_portrait_male);
                        } else {
                            activity_image_yuyinicon
                                    .setImageResource(R.drawable.sports_user_edit_portrait);
                        }
                    } else {
                        if (mFindMore.getTalkdetils().get(i).getSex()
                                .equals("1")) {
                            activity_image_yuyinicon
                                    .setImageResource(R.drawable.sports_user_edit_portrait_male);
                        } else {
                            activity_image_yuyinicon
                                    .setImageResource(R.drawable.sports_user_edit_portrait);
                        }
                        mImageWorker_Icon.loadImage(mFindMore.getTalkdetils()
                                        .get(i).getImg(), activity_image_yuyinicon,
                                null, null, false);
                        // mDownloader.download(
                        // mFindMore.getTalkdetils().get(i).getImg(),
                        // activity_image_yuyinicon, null);
                    }

                    if (theSecondName == null || theSecondName.equals("")
                            || theSecondName.equals("null")) {
                        // newMessageString="<font color=\"#25a7f2\">"+theFirstName+":"+"</font>";
                        newMessageString = theFirstName + ":";
                        numcom = 1;
                        firstname = theFirstName.length();

                    } else {
						/*
						 * newMessageString="<font color=\"#25a7f2\">"+theFirstName
						 * +"</font><font color=\"#3a3f47\">"
						 * +mContext.getResources
						 * ().getString(R.string.multi_comment_tip_target)
						 * +"</font><font color=\"#25a7f2\">"
						 * +theSecondName+":"+"</font>";
						 */
                        // if
                        // ("".equals(mFindMore.getTalkdetils().get(i).getImg())
                        // || mFindMore.getTalkdetils().get(i).getImg() == null)
                        // {
                        // if (mFindMore.getTalkdetils().get(i).getSex()
                        // .equals("1")) {
                        // activity_image_yuyinicon
                        // .setImageResource(R.drawable.sports_residemenu_man);
                        // } else {
                        // activity_image_yuyinicon
                        // .setImageResource(R.drawable.sports_residemenu_woman);
                        // }
                        // } else {
                        // mDownloader.download(
                        // mFindMore.getTalkdetils().get(i).getImg(),
                        // activity_image_yuyinicon, null);
                        // }

                        newMessageString = theFirstName
                                + mContext.getResources().getString(
                                R.string.multi_comment_tip_target)
                                + theSecondName + ":";
                        numcom = 2;
                        firstname = theFirstName.length();
                        secondname = theSecondName.length();
                    }
                    durationtext.setText("" + thewavDuration + "″");
                    String ssString = ToDBC(newMessageString);
                    List<SpannableString> list = getExpressionString(mContext,
                            ssString, numcom, firstname, secondname);
                    for (SpannableString span : list) {
                        find_talk_detils_text_name.setText(span);
                        // Log.e(TAG, "长度2-------"+span.length());
                    }
                    // 录音播放
                    final Object lock = new Object();
                    final String mediaPath = thewavComment;
                    final ImageView wavBegin = beginWav;
                    recordlayout.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View arg0) {
                            // TODO Auto-generated method stub
                            synchronized (lock) {
                                if (mediaPath.endsWith("null")
                                        || mediaPath == null) {
                                    return;
                                }
                                // currentDuration1 = 0;
                                new Thread() {
                                    @Override
                                    public void run() {
                                        if (wavBeginOne != wavBegin
                                                && mPlayer != null) {
                                            mHandler.sendMessage(mHandler
                                                    .obtainMessage(
                                                            RECORD_PAUSE,
                                                            wavBeginOne));
                                            isStart = true;
                                            mPlayer.pause();
                                            isPause = false;
                                            wavBeginOne = wavBegin;
                                            Log.i("", "进来了");
                                        }

                                        Log.e(TAG, "录音播放0---");
                                        if (isStart) {
                                            Log.e(TAG, "录音播放---1");
                                            wavBeginOne = wavBegin;
                                            isStart = false;
                                            if (!isPause) {
                                                Log.e(TAG, "录音播放---2");
                                                Log.d(TAG, "if (!isPause)");
                                                mPlayer = mRecorder
                                                        .startPlaybackNet(
                                                                mContext,
                                                                mediaPath);
                                            }
                                            if (mPlayer == null) {
                                                Log.e(TAG, "录音播放---3");
                                                Log.d(TAG,
                                                        "if (mPlayer == null)");
                                                // mHandler.sendMessage(mHandler.obtainMessage(RECORD_PAUSE,
                                                // wavBegin));
                                                mPlayer = mRecorder
                                                        .startPlaybackNet(
                                                                mContext,
                                                                mediaPath);
                                                mHandler.sendMessage(mHandler
                                                        .obtainMessage(
                                                                RECORD_LOADING,
                                                                wavBegin));
                                                // return;
                                            }
                                            mPlayer.setOnErrorListener(new OnErrorListener() {
                                                @Override
                                                public boolean onError(
                                                        MediaPlayer arg0,
                                                        int arg1, int arg2) {
                                                    Log.e(TAG, "录音播放---4");
                                                    isStart = true;
                                                    currentDuration = 0;
                                                    mRecorder.stopPlayback();
                                                    mHandler.sendMessage(mHandler
                                                            .obtainMessage(
                                                                    RECORD_ERROR,
                                                                    wavBegin));
                                                    return true;
                                                }
                                            });
                                            mPlayer.setOnCompletionListener(new OnCompletionListener() {
                                                @Override
                                                public void onCompletion(
                                                        MediaPlayer arg0) {
                                                    Log.e(TAG, "录音播放---5");
                                                    isStart = true;
                                                    isPause = false;
                                                    Log.e("hjtest",
                                                            "onCompletion");
                                                    mHandler.sendMessage(mHandler
                                                            .obtainMessage(
                                                                    RECORD_FINISH,
                                                                    wavBegin));
                                                }
                                            });
                                            mPlayer.setOnPreparedListener(new OnPreparedListener() {
                                                @Override
                                                public void onPrepared(
                                                        MediaPlayer arg0) {
                                                    Log.e(TAG, "录音播放---6");
                                                    Log.e("hjtest",
                                                            "onPrepared");
                                                    if (mPlayer == null) {
                                                        mHandler.sendMessage(mHandler
                                                                .obtainMessage(
                                                                        RECORD_PAUSE,
                                                                        wavBegin));
                                                        return;
                                                    }
                                                    mHandler.sendMessage(mHandler
                                                            .obtainMessage(
                                                                    RECORD_PREPARED,
                                                                    wavBegin));
                                                }
                                            });
                                            try {
                                                if (!isPause) {
                                                    Log.d(TAG, "not isPause");
                                                    Log.e(TAG, "录音播放---7");
													/*
													 * mPlayer.setOnPreparedListener
													 * (new OnPreparedListener()
													 * {
													 *
													 * @Override public void
													 * onPrepared(MediaPlayer
													 * mPlayer) { // TODO
													 * Auto-generated method
													 * stub mPlayer.start(); }
													 * });
													 * mPlayer.prepareAsync();
													 */
                                                    mPlayer.prepare();
                                                    mPlayer.start();

                                                } else {
                                                    Log.d(TAG, "isPause");
                                                    Log.e(TAG, "录音播放---8");
                                                    mPlayer.start();
                                                    mHandler.sendMessage(mHandler
                                                            .obtainMessage(
                                                                    RECORD_PREPARED,
                                                                    wavBegin));
                                                }
                                            } catch (IllegalStateException e) {
                                                Log.e(TAG, "录音播放---9");
                                                mPlayer = null;
                                                isStart = true;
                                                currentDuration = 0;
                                                mHandler.sendMessage(mHandler
                                                        .obtainMessage(
                                                                RECORD_PAUSE,
                                                                wavBegin));
                                            } catch (Exception e) {
                                                Log.e(TAG, "录音播放---10");
                                                mPlayer = null;
                                                isStart = true;
                                                currentDuration = 0;
                                                mHandler.sendMessage(mHandler
                                                        .obtainMessage(
                                                                RECORD_PAUSE,
                                                                wavBegin));
                                            }
                                            if (currentDuration > 0 || isPause) {
                                                Log.e(TAG, "录音播放---11");
                                                isPause = false;
                                                if (mPlayer != null) {
                                                    Log.e(TAG, "录音播放---12");
                                                    mPlayer.start();
                                                } else {
                                                    Log.e(TAG, "录音播放---13");
                                                    mPlayer = mRecorder
                                                            .startPlaybackNet(
                                                                    mContext,
                                                                    mediaPath);
                                                    isPause = false;
                                                    mHandler.sendMessage(mHandler
                                                            .obtainMessage(
                                                                    RECORD_FINISH,
                                                                    wavBegin));
                                                }
                                                currentDuration = 0;
                                            }
                                        } else if (!isStart) {
                                            Log.e(TAG, "录音播放---14");
                                            Log.d(TAG, "else if (!isStart)");
                                            if (mPlayer != null) {
                                                Log.e(TAG, "录音播放---15");
                                                Log.d(TAG,
                                                        "if (mPlayer != null)");
                                                try {
                                                    Log.e(TAG, "录音播放---16");
                                                    mPlayer.pause();
                                                    isStart = true;
                                                    isPause = true;
                                                    // currentDuration1 =
                                                    // mPlayer.getCurrentPosition();
                                                } catch (Exception e) {
                                                    Log.e(TAG, "录音播放---17");
                                                    mPlayer = null;
                                                    isStart = true;
                                                    currentDuration = 0;
                                                    mHandler.sendMessage(mHandler
                                                            .obtainMessage(
                                                                    RECORD_PAUSE,
                                                                    wavBegin));
                                                }
                                            }
                                            mHandler.sendMessage(mHandler
                                                    .obtainMessage(
                                                            RECORD_PAUSE,
                                                            wavBegin));
                                        }
                                    }
                                }.start();
                            }
                        }
                    });

                } else {

                    pinglunDetails = mInflater.inflate(
                            R.layout.user_activity_pinglun_layout, null);
                    RoundedImage activity_image_icon = (RoundedImage) pinglunDetails
                            .findViewById(R.id.activity_image_icon);
                    TextView activity_submit_name = (TextView) pinglunDetails
                            .findViewById(R.id.activity_submit_name);
                    TextView activity_submit_content = (TextView) pinglunDetails
                            .findViewById(R.id.activity_submit_content);
                    TextView activity_submit_time = (TextView) pinglunDetails
                            .findViewById(R.id.activity_submit_time);

                    // 评论时间
                    long pinglTm = System.currentTimeMillis()
                            - Long.valueOf(mFindMore.getTalkdetils().get(i)
                            .getInputtime()) * 1000;
                    SimpleDateFormat pformat = new SimpleDateFormat(
                            "yyyy-MM-dd HH:mm");
                    if (pinglTm <= 60 * 1000)
                        // 一分钟内显示刚刚
                        activity_submit_time.setText(getResources().getString(
                                R.string.sports_time_justnow));
                    else if (pinglTm <= 60 * 60 * 1000) {
                        int h = (int) (pinglTm / 1000 / 60);
                        // 一小时内显示多少分钟前
                        activity_submit_time.setText(""
                                + h
                                + getResources().getString(
                                R.string.sports_time_mins_ago));
                    } else {
                        activity_submit_time.setText(pformat.format(Long
                                .valueOf(mFindMore.getTalkdetils().get(i)
                                        .getInputtime()) * 1000));
                    }

                    if ("".equals(mFindMore.getTalkdetils().get(i).getImg())
                            || mFindMore.getTalkdetils().get(i).getImg() == null) {
                        if (mFindMore.getTalkdetils().get(i).getSex()
                                .equals("1")) {
                            activity_image_icon
                                    .setImageResource(R.drawable.sports_user_edit_portrait_male);
                        } else {
                            activity_image_icon
                                    .setImageResource(R.drawable.sports_user_edit_portrait);
                        }
                    } else {
                        if (mFindMore.getTalkdetils().get(i).getSex()
                                .equals("1")) {
                            activity_image_icon
                                    .setImageResource(R.drawable.sports_user_edit_portrait_male);
                        } else {
                            activity_image_icon
                                    .setImageResource(R.drawable.sports_user_edit_portrait);
                        }
                        mImageWorker_Icon.loadImage(mFindMore.getTalkdetils()
                                        .get(i).getImg(), activity_image_icon, null,
                                null, false);
                        // mDownloader.download(
                        // mFindMore.getTalkdetils().get(i).getImg(),
                        // activity_image_icon, null);
                    }

                    // TextView sTextView = (TextView) newView
                    // .findViewById(R.id.find_talk_detils_text);
                    // 让每个表情前加个空格
                    String stringoneString = theTalkDetils.replaceAll("\\[",
                            " [");
                    if (theSecondName == null || theSecondName.equals("")
                            || theSecondName.equals("null")) {
						/*
						 * newMessageString="<font color=\"#25a7f2\">"+
						 * theFirstName +":"+"</font><font color=\"#3a3f47\">"
						 * +" "+theTalkDetils+"</font>";
						 */

                        // newMessageString = theFirstName + ":" +
                        // stringoneString;
                        newMessageString = stringoneString;
                        newNameString = theFirstName + ":";
                        numcom = 1;
                        firstname = theFirstName.length();
                    } else {
						/*
						 * newMessageString="<font color=\"#25a7f2\">"+
						 * theFirstName+"</font><font color=\"#3a3f47\">"
						 * +mContext.getResources().getString(R.string.
						 * multi_comment_tip_target)
						 * +"</font><font color=\"#25a7f2\">" +theSecondName+":"
						 * +"</font><font color=\"#3a3f47\">"
						 * +" "+theTalkDetils+"</font>";
						 */
                        // newMessageString = theFirstName
                        // + getResources().getString(
                        // R.string.multi_comment_tip_target)
                        // + theSecondName + ":" + stringoneString;
                        // if
                        // ("".equals(mFindMore.getTalkdetils().get(i).getImg())
                        // || mFindMore.getTalkdetils().get(i).getImg() == null)
                        // {
                        // if (mFindMore.getTalkdetils().get(i).getSex()
                        // .equals("1")) {
                        // activity_image_icon
                        // .setImageResource(R.drawable.sports_residemenu_man);
                        // } else {
                        // activity_image_icon
                        // .setImageResource(R.drawable.sports_residemenu_woman);
                        // }
                        // } else {
                        // mDownloader.download(
                        // mFindMore.getTalkdetils().get(i).getImg(),
                        // activity_image_icon, null);
                        // }

                        newMessageString = stringoneString;
                        newNameString = theFirstName
                                + getResources().getString(
                                R.string.multi_comment_tip_target)
                                + theSecondName + ":";
                        numcom = 2;
                        firstname = theFirstName.length();
                        secondname = theSecondName.length();
                    }
                    String ssString = ToDBC(newNameString);
                    List<SpannableString> nameString = getNameString(mContext,
                            ssString, numcom, firstname, secondname);
                    for (SpannableString span : nameString) {
                        activity_submit_name.setText(span);
                    }
                    List<SpannableString> list = getContentString(mContext,
                            newMessageString, numcom);
                    for (SpannableString span : list) {
                        activity_submit_content.setText(span);
                    }
                    activity_submit_content.invalidate();

                }
                ViewGroup p = (ViewGroup) pinglunDetails.getParent();
                if (p != null) {
                    p.removeAllViewsInLayout();
                }
                pinglunDetails.setOnClickListener(new commentOnClickListener(i,
                        mFindMore.getFindId(), mFindMore.getTalkdetils().get(i)
                        .getId(), theFirstName));
                sendToName = theFirstName;
//                pinglunDetails.setOnTouchListener(new FaceButtonListener() );
//                pinglunDetails.setBackgroundColor(Color.parseColor("#FFFFFF"));
//                activity_pinglun_layout.addView(pinglunDetails);
//                pinglunDetails.setOnLongClickListener(new commentOnClickListener2(
//                        i, mFindMore.getFindId(), mFindMore.getTalkdetils().get(i)
//                                .getId(), theFirstName,pinglunDetails));
                talklist.addView(pinglunDetails);
            }
        } else {
            list_user_pinglun.setText("回复 (0)");
            activity_pinglun_layout.setVisibility(View.GONE);
        }
    }

    class FaceButtonListener implements OnTouchListener{
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if(motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                view.setBackgroundColor(Color.parseColor("#333333"));
            }else if(motionEvent.getAction() == MotionEvent.ACTION_UP){
                view.setBackgroundColor(Color.parseColor("#FFFFFF"));
            }
            return false;
        }
    }
    class GetSingleThread extends Thread {
        private int findId;

        public GetSingleThread(int findId) {
            this.findId = findId;
        }

        @Override
        public void run() {
            try {
                FindGroup getFindGroup = ApiJsonParser.getGetFindGroup(
                        mSportsApp.getSessionId(), findId + "");
                Message msg = Message.obtain(mHandler, FINDSINGLE);
                msg.obj = getFindGroup;
                msg.sendToTarget();

            } catch (ApiNetException e) {
                e.printStackTrace();
            } catch (ApiSessionOutException e) {
                e.printStackTrace();
            }
        }

    }

    // 从发现页面进入后获取动态信息
    class GetInfoThread extends Thread {
        private int findId;

        public GetInfoThread(int findId) {
            this.findId = findId;
        }

        @Override
        public void run() {
            try {
                FindGroup getFindGroup = ApiJsonParser.getGetFindGroup(
                        mSportsApp.getSessionId(), findId + "");
                Message msg = Message.obtain(mHandler, GETINFOS);
                msg.obj = getFindGroup;
                msg.sendToTarget();

            } catch (ApiNetException e) {
                e.printStackTrace();
            } catch (ApiSessionOutException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @author loujungang 加1点赞效果
     */
    class AddAnimian implements View.OnClickListener {
        private View view;

        public AddAnimian(View view) {
            this.view = view;
        }

        @Override
        public void onClick(final View v) {
            // TODO Auto-generated method stub
            switch (v.getId()) {
                case R.id.list_user_zan_layout2:
                case R.id.list_user_zan2:
                    final int theBeforeGoodPeople = mFindMore.getGoodpeople();
                    view.setVisibility(View.VISIBLE);
                    final TextView textView = (TextView) view;
                    if (mSportsApp.isOpenNetwork()) {
                        new AsyncTask<Void, Void, ApiBack>() {
                            @Override
                            protected void onPreExecute() {
                                // TODO Auto-generated method stub
                                if (mFindMore.isGood()) {
                                    // showWaitDialog(mContext.getResources()
                                    // .getString(R.string.cancel_wait));
                                    textView.setText("-1");
                                } else {
                                    // showWaitDialog(mContext.getResources()
                                    // .getString(R.string.praise_wait));
                                    textView.setText("+1");
                                }
                            }

                            @Override
                            protected ApiBack doInBackground(Void... params) {
                                // TODO Auto-generated method stub
                                ApiBack back = null;
                                try {
                                    back = (ApiBack) ApiJsonParser.likeFind(
                                            mSportsApp.getSessionId(),
                                            mFindMore.getFindId());
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
                                    // Log.e("--", "result-------"+result);
                                    // waitProgressDialog.dismiss();
                                    Toast.makeText(
                                            mContext,
                                            mContext.getResources().getString(
                                                    R.string.sports_findgood_error),
                                            Toast.LENGTH_SHORT).show();
                                } else {
                                    // 成功
                                    // waitProgressDialog.dismiss();
                                    if (mFindMore.isGood()) {
                                        mFindMore.setGood(false);
                                        mFindMore
                                                .setGoodpeople(theBeforeGoodPeople - 1);
                                        // Toast.makeText(
                                        // mContext,
                                        // mContext.getResources()
                                        // .getString(
                                        // R.string.praise_cancel_success),
                                        // Toast.LENGTH_SHORT).show();

                                    } else {
                                        mFindMore.setGood(true);
                                        mFindMore
                                                .setGoodpeople(theBeforeGoodPeople + 1);
                                        // Toast.makeText(
                                        // mContext,
                                        // mContext.getResources().getString(
                                        // R.string.praise_successed),
                                        // Toast.LENGTH_SHORT).show();
                                    }
                                    view.startAnimation(animation);
                                    new Handler().postDelayed(new Runnable() {
                                        public void run() {
                                            view.setVisibility(View.GONE);
                                            // setZanAndPinglun();
                                            showWaitDialog(mContext.getResources()
                                                    .getString(
                                                            R.string.comment_wait));
                                            GetSingleThread thread = new GetSingleThread(
                                                    Integer.parseInt(mFindMore
                                                            .getFindId()));
                                            thread.start();
                                        }
                                    }, 1000);

                                }
                            }

                        }.execute();
                    } else {
                        Toast.makeText(
                                mContext,
                                mContext.getResources().getString(
                                        R.string.error_cannot_access_net),
                                Toast.LENGTH_SHORT).show();

                    }

                    break;

                case R.id.list_user_guanzhu:
                    if (mSportsApp.isLogin() == false
                            && (mSportsApp.getSessionId() == null || ""
                            .equals(mSportsApp.getSessionId()))) {
                        Intent intent = new Intent(mContext, LoginActivity.class);
                        mContext.startActivity(intent);
                        return;
                    }

                    if (mSportsApp.isOpenNetwork()) {
                        new AsyncTask<Void, Void, ApiBack>() {
                            @Override
                            protected void onPreExecute() {
                                // TODO Auto-generated method stub
                                // addGuanzhu.setText("已关注");
                            }

                            @Override
                            protected ApiBack doInBackground(Void... params) {
                                // TODO Auto-generated method stub
                                ApiBack back = null;
                                try {
                                    back = ApiJsonParser.follow(
                                            mSportsApp.getSessionId(),
                                            mFindMore.getOtheruid(), 1, 1);
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
                                            mContext,
                                            mContext.getResources().getString(
                                                    R.string.sports_findgood_error),
                                            Toast.LENGTH_SHORT).show();
                                } else {
                                    // 成功
                                    mFindMore.setIsFriends(1);
                                    final Button button = (Button) view;
                                   button.setText("关注");
                                    view.startAnimation(animation);
                                    new Handler().postDelayed(new Runnable() {
                                        public void run() {
                                            v.setVisibility(View.GONE);
                                            list_user_guanzhu_addone.setVisibility(View.VISIBLE);
                                        }
                                    }, 1000);

                                }
                            }

                        }.execute();
                    } else {
                        Toast.makeText(
                                mContext,
                                mContext.getResources().getString(
                                        R.string.error_cannot_access_net),
                                Toast.LENGTH_SHORT).show();

                    }
                    final String message = "Hi，我关注了爱运动的你，一起来运动吧！";
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
                                                mSportsApp.getSessionId(), mFindMore.getOtheruid(),
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
                                    waitProgressDialog.dismiss();
                                    Toast.makeText(
                                            mContext,
                                            mContext.getResources().getString(
                                                    R.string.sports_top_failed),
                                            Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(mContext, "关注成功！", Toast.LENGTH_SHORT).show();
                                    List<UserPrimsgAll> primsgAllList = new ArrayList<UserPrimsgAll>();
                                    final UserPrimsgOne priMsg = new UserPrimsgOne();
                                    priMsg.setAddTime(System.currentTimeMillis() / 1000);
                                    priMsg.setCommentText(message);
                                    priMsg.setCommentWav("");
                                    priMsg.setWavDuration(0);
                                    priMsg.setUid(mSportsApp.getSportUser().getUid());
                                    priMsg.setTouid(mFindMore.getOtheruid());
                                    priMsg.setOwnerid(mSportsApp.getSportUser().getUid());
                                    long sava = mSportsApp.getSportsDAO().savePrivateMsgInfo(priMsg);
                                    Log.i("SSSS", sava + "");
                                    UserPrimsgAll userPrimsgAll = new UserPrimsgAll();
                                    userPrimsgAll.setAddTime(priMsg.getAddTime());
                                    userPrimsgAll.setName(mFindMore.getOthername());
                                    userPrimsgAll.setUid(priMsg.getTouid());
                                    userPrimsgAll.setTouid(mSportsApp.getSportUser().getUid());
                                    userPrimsgAll.setUimg(mFindMore.getOtherimg());
                                    userPrimsgAll.setBirthday("");//传生日
                                    userPrimsgAll.setCounts(0);
                                    userPrimsgAll.setSex(mFindMore.getSex());
                                    userPrimsgAll.setTouimg(mSportsApp.getSportUser().getUimg());
                                    primsgAllList.add(userPrimsgAll);
                                    mSportsApp.getSportsDAO().insertPrivateMsgAll(SportsContent.PrivateMessageAllTable.TABLE_NAME,
                                            primsgAllList, "read");
                                    Log.d("userPrimsgAll", "save msg to PrivateMsgAll :" + userPrimsgAll);

                                }
                            }
                        }.execute();
                    } else {
                        waitProgressDialog.dismiss();
                        // Toast.makeText(
                        // mContext,
                        // mContext.getResources().getString(
                        // R.string.error_cannot_access_net),
                        // Toast.LENGTH_SHORT).show();
                    }

                    break;

            }

        }

    }

    /**
     * 刷新界面
     */
    // private void refJresh() {
    // if (pinglunDetails != null) {
    // ViewGroup p = (ViewGroup) pinglunDetails.getParent();
    // if (p != null) {
    // p.removeAllViewsInLayout();
    // }
    // }
    //
    // }
    @Override
    public void onClick(View view) {
        // TODO Auto-generated method stub
        switch (view.getId()) {
            case LEFTBTLAYOUT:
            case LEFTBTID:
                if (findId == -1) {
                    Message msg = new Message();
                    msg.what = FindOtherFragment.RETURN_MORE;
                    msg.obj = mFindMore;
                    Handler handler = null;
                    if (toFlag == 1) {
                        handler = mSportsApp.getPersonalFindHandler();
                        handler.sendMessage(msg);
                        finish();
                    } else if (toFlag == 2) {
                        if (SportCircleMainFragment.indexFlag == 0) {
                            handler = mSportsApp.getFindHandler();

                        } else if (SportCircleMainFragment.indexFlag == 1) {
                            handler = mSportsApp.getGoodfriendsHandler();
                        }
                        if (handler != null) {
                            handler.sendMessage(msg);
                        }
                        finish();
                    } else {
                        finish();
                    }
                } else {
                    Intent mIntent = new Intent();
                    mIntent.putExtra("mFindMore", mFindMore);
                    // 设置结果，并进行传送
                    this.setResult(1, mIntent);
                    finish();
                }

                break;

            case R.id.list_user_pinglun:
            case R.id.list_user_pinglun_layout:
                toID = null;
                isHuifuPinglun = 1;
                find_upcomment_edittext.setHint(getResources().getString(R.string.sports_private_msg_texts_hint));
                break;
            case R.id.sportshow_shaer2:
                // 分享
                Intent intent = new Intent(mContext, ShareDialogMainActivity.class);
                intent.putExtra("findGroup", mFindMore);
                mContext.startActivity(intent);
                break;
            case R.id.good_and_text2:
                sendToName = mSportsApp.getSportUser().getUname();
                if (a == 0) {
                    a = 1;
                    findbottomLayout.setVisibility(View.VISIBLE);
                    rl_infowebview.setVisibility(View.GONE);
                    lay.setVisibility(View.VISIBLE);
                    find_bottom.setVisibility(View.VISIBLE);
                    toID = null;
                    find_upcomment_edittext.setHint("我想对你说：");
                    showedit();
                } else {
                    a = 0;
                    findbottomLayout.setVisibility(View.GONE);
                    rl_infowebview.setVisibility(View.VISIBLE);
                    find_bottom.setVisibility(View.GONE);
                    lay.setVisibility(View.GONE);
                    hideedit();
                }
            default:
                break;
        }

    }

    private void showZanImage(ArrayList<PraiseUsers> pArrayList) {
        dianzan_one.setVisibility(View.GONE);
        dianzan_two.setVisibility(View.GONE);
        dianzan_three.setVisibility(View.GONE);
        dianzan_four.setVisibility(View.GONE);
        dianzan_five.setVisibility(View.GONE);
        dianzan_six.setVisibility(View.GONE);
        if (pArrayList.size() == 0) {
            activity_zan_layout.setVisibility(View.GONE);
        } else if (pArrayList.size() == 1) {
            activity_zan_layout.setVisibility(View.VISIBLE);
            dianzan_one.setVisibility(View.VISIBLE);
            setRoudImage(pArrayList.get(0).getImg(), dianzan_one, pArrayList
                    .get(0).getSex());
            dianzan_one.setOnClickListener(new personalOnClickListener(Integer
                    .parseInt(pArrayList.get(0).getUid())));
        } else if (pArrayList.size() == 2) {
            activity_zan_layout.setVisibility(View.VISIBLE);
            dianzan_one.setVisibility(View.VISIBLE);
            dianzan_two.setVisibility(View.VISIBLE);
            setRoudImage(pArrayList.get(0).getImg(), dianzan_one, pArrayList
                    .get(0).getSex());
            setRoudImage(pArrayList.get(1).getImg(), dianzan_two, pArrayList
                    .get(1).getSex());
            dianzan_one.setOnClickListener(new personalOnClickListener(Integer
                    .parseInt(pArrayList.get(0).getUid())));
            dianzan_two.setOnClickListener(new personalOnClickListener(Integer
                    .parseInt(pArrayList.get(1).getUid())));
        } else if (pArrayList.size() == 3) {
            activity_zan_layout.setVisibility(View.VISIBLE);
            dianzan_one.setVisibility(View.VISIBLE);
            dianzan_two.setVisibility(View.VISIBLE);
            dianzan_three.setVisibility(View.VISIBLE);
            setRoudImage(pArrayList.get(0).getImg(), dianzan_one, pArrayList
                    .get(0).getSex());
            setRoudImage(pArrayList.get(1).getImg(), dianzan_two, pArrayList
                    .get(1).getSex());
            setRoudImage(pArrayList.get(2).getImg(), dianzan_three, pArrayList
                    .get(2).getSex());
            dianzan_one.setOnClickListener(new personalOnClickListener(Integer
                    .parseInt(pArrayList.get(0).getUid())));
            dianzan_two.setOnClickListener(new personalOnClickListener(Integer
                    .parseInt(pArrayList.get(1).getUid())));
            dianzan_three.setOnClickListener(new personalOnClickListener(
                    Integer.parseInt(pArrayList.get(2).getUid())));
        } else if (pArrayList.size() == 4) {
            activity_zan_layout.setVisibility(View.VISIBLE);
            dianzan_one.setVisibility(View.VISIBLE);
            dianzan_two.setVisibility(View.VISIBLE);
            dianzan_three.setVisibility(View.VISIBLE);
            dianzan_four.setVisibility(View.VISIBLE);
            setRoudImage(pArrayList.get(0).getImg(), dianzan_one, pArrayList
                    .get(0).getSex());
            setRoudImage(pArrayList.get(1).getImg(), dianzan_two, pArrayList
                    .get(1).getSex());
            setRoudImage(pArrayList.get(2).getImg(), dianzan_three, pArrayList
                    .get(2).getSex());
            setRoudImage(pArrayList.get(3).getImg(), dianzan_four, pArrayList
                    .get(3).getSex());
            dianzan_one.setOnClickListener(new personalOnClickListener(Integer
                    .parseInt(pArrayList.get(0).getUid())));
            dianzan_two.setOnClickListener(new personalOnClickListener(Integer
                    .parseInt(pArrayList.get(1).getUid())));
            dianzan_three.setOnClickListener(new personalOnClickListener(
                    Integer.parseInt(pArrayList.get(2).getUid())));
            dianzan_four.setOnClickListener(new personalOnClickListener(Integer
                    .parseInt(pArrayList.get(3).getUid())));
        } else if (pArrayList.size() == 5) {
            activity_zan_layout.setVisibility(View.VISIBLE);
            dianzan_one.setVisibility(View.VISIBLE);
            dianzan_two.setVisibility(View.VISIBLE);
            dianzan_three.setVisibility(View.VISIBLE);
            dianzan_four.setVisibility(View.VISIBLE);
            dianzan_five.setVisibility(View.VISIBLE);
            setRoudImage(pArrayList.get(0).getImg(), dianzan_one, pArrayList
                    .get(0).getSex());
            setRoudImage(pArrayList.get(1).getImg(), dianzan_two, pArrayList
                    .get(1).getSex());
            setRoudImage(pArrayList.get(2).getImg(), dianzan_three, pArrayList
                    .get(2).getSex());
            setRoudImage(pArrayList.get(3).getImg(), dianzan_four, pArrayList
                    .get(3).getSex());
            setRoudImage(pArrayList.get(4).getImg(), dianzan_five, pArrayList
                    .get(4).getSex());
            dianzan_one.setOnClickListener(new personalOnClickListener(Integer
                    .parseInt(pArrayList.get(0).getUid())));
            dianzan_two.setOnClickListener(new personalOnClickListener(Integer
                    .parseInt(pArrayList.get(1).getUid())));
            dianzan_three.setOnClickListener(new personalOnClickListener(
                    Integer.parseInt(pArrayList.get(2).getUid())));
            dianzan_four.setOnClickListener(new personalOnClickListener(Integer
                    .parseInt(pArrayList.get(3).getUid())));
            dianzan_five.setOnClickListener(new personalOnClickListener(Integer
                    .parseInt(pArrayList.get(4).getUid())));
        } else if (pArrayList.size() >= 6) {
            activity_zan_layout.setVisibility(View.VISIBLE);
            dianzan_one.setVisibility(View.VISIBLE);
            dianzan_two.setVisibility(View.VISIBLE);
            dianzan_three.setVisibility(View.VISIBLE);
            dianzan_four.setVisibility(View.VISIBLE);
            dianzan_five.setVisibility(View.VISIBLE);
            dianzan_six.setVisibility(View.VISIBLE);
            setRoudImage(pArrayList.get(0).getImg(), dianzan_one, pArrayList
                    .get(0).getSex());
            setRoudImage(pArrayList.get(1).getImg(), dianzan_two, pArrayList
                    .get(1).getSex());
            setRoudImage(pArrayList.get(2).getImg(), dianzan_three, pArrayList
                    .get(2).getSex());
            setRoudImage(pArrayList.get(3).getImg(), dianzan_four, pArrayList
                    .get(3).getSex());
            setRoudImage(pArrayList.get(4).getImg(), dianzan_five, pArrayList
                    .get(4).getSex());
            setRoudImage(pArrayList.get(5).getImg(), dianzan_six, pArrayList
                    .get(5).getSex());
            dianzan_one.setOnClickListener(new personalOnClickListener(Integer
                    .parseInt(pArrayList.get(0).getUid())));
            dianzan_two.setOnClickListener(new personalOnClickListener(Integer
                    .parseInt(pArrayList.get(1).getUid())));
            dianzan_three.setOnClickListener(new personalOnClickListener(
                    Integer.parseInt(pArrayList.get(2).getUid())));
            dianzan_four.setOnClickListener(new personalOnClickListener(Integer
                    .parseInt(pArrayList.get(3).getUid())));
            dianzan_five.setOnClickListener(new personalOnClickListener(Integer
                    .parseInt(pArrayList.get(4).getUid())));
            dianzan_six.setOnClickListener(new personalOnClickListener(Integer
                    .parseInt(pArrayList.get(5).getUid())));
            if (pArrayList.size() > 6) {
                zan_more_icon.setVisibility(View.VISIBLE);
                zan_more_icon.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        // TODO Auto-generated method stub
                        Intent intent = new Intent(
                                UserActivityMainActivity.this,
                                ZanListUserActivity.class);
                        intent.putExtra("find_id",
                                Integer.parseInt(mFindMore.getFindId()));
                        intent.putExtra("uid", mFindMore.getOtheruid());
                        startActivity(intent);
                    }
                });
            }
        }
    }

    private void setRoudImage(String imagUrl, ImageView imageView, String sex) {
        if (!"".equals(imagUrl) && imagUrl != null) {
            // mDownloader.download(imagUrl, imageView, null);
            if (sex.equals("1")) {
                imageView.setImageResource(R.drawable.sports_user_edit_portrait_male);
            } else {
                imageView.setImageResource(R.drawable.sports_user_edit_portrait);
            }
            mImageWorker_Icon.loadImage(imagUrl, imageView, null, null, false);
        } else {
            if (sex.equals("1")) {
                imageView.setImageResource(R.drawable.sports_user_edit_portrait_male);
            } else {
                imageView.setImageResource(R.drawable.sports_user_edit_portrait);
            }
        }
    }

    // 点击进入个人信息页面
    class personalOnClickListener implements OnClickListener {
        private int userId;

        public personalOnClickListener(int id) {
            this.userId = id;
        }

        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub
            if (!mSportsApp.isOpenNetwork()) {
                Toast.makeText(mContext, "网络未连接，请检查网络！", Toast.LENGTH_LONG)
                        .show();
                return;
            }
            // Intent intent = new Intent(mContext, PedometerActivity.class);
            Intent intent = new Intent(mContext, PersonalPageMainActivity.class);
            intent.putExtra("ID", userId);
            mContext.startActivity(intent);

        }
    }

    // 监听评论
    class commentOnClickListener implements OnClickListener {
        // 第几条评论
        private int comment_number;
        // 评论所属发现的ID
        private String findID;
        // 评论的ID
        private String commentID;
        // 回复评论时的名字
        private String theFirstName;

        public commentOnClickListener(int i, String findIdString,
                                      String commentIdString, String theFirstName) {
            this.comment_number = i;
            this.findID = findIdString;
            this.commentID = commentIdString;
            this.theFirstName = theFirstName;
        }

        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub
            if (detail.getUname().equals(theFirstName)) {
                showDialogs(comment_number, commentID);
            }else {
                /**
                 * (position,findIdString,2,commentID,theFirstName)
                 * 第一个参数所属发现在list中位置 第二个参数发现ID， 第三个参数用来判断评论还是回复1代表直接评论2代表回复
                 * 第四个参数用来当回复的时候传递to_id,如果直接评论则为null
                 * 第五个参数用来当回复的时候显示回复给谁，如果直接评论则null
                 */
                if (mSportsApp.mIsAdmin) {
                    showAdminDialogs(comment_number, commentID, findID,
                            theFirstName);
                }  else {
                    /**
                     * (position,findIdString,2,commentID,theFirstName)
                     * 第一个参数所属发现在list中位置 第二个参数发现ID， 第三个参数用来判断评论还是回复1代表直接评论2代表回复
                     * 第四个参数用来当回复的时候传递to_id,如果直接评论则为null
                     * 第五个参数用来当回复的时候显示回复给谁，如果直接评论则null
                     */
//                    if (ba == 0) {
                        ba = 1;
                        findbottomLayout.setVisibility(View.VISIBLE);
                        rl_infowebview.setVisibility(View.GONE);
                        lay.setVisibility(View.VISIBLE);
                        find_bottom.setVisibility(View.VISIBLE);
                        showedit();
//                    } else {
//                        ba = 0;
//                        findbottomLayout.setVisibility(View.GONE);
//                        find_bottom.setVisibility(View.GONE);
//                        lay.setVisibility(View.GONE);
//                        hideedit();
//                    }
                    toID = commentID;
                    isHuifuPinglun = 2;
                    if (isHuifuPinglun == 1) {
                        find_upcomment_edittext
                                .setHint(getResources().getString(
                                        R.string.sports_private_msg_texts_hint));
                    }
                    if (isHuifuPinglun == 2) {
                        // 回复
                        find_upcomment_edittext.setHint(getResources()
                                .getString(R.string.multi_comment_tip_target)
                                + theFirstName+ ":" );
                    }
                }
            }
        }
    }
    // 3.4版本监听评论的长按点击事件
    class commentOnClickListener2 implements View.OnLongClickListener {
        // 第几条评论
        private int comment_number;
        // 评论所属发现的ID
        private String findID;
        // 评论的ID
        private String commentID;
        // 回复评论时的名字
        private String theFirstName;
        private View pinglundetail;
        public commentOnClickListener2( int i,
                                       String findIdString, String commentIdString, String
                                                theFirstName,View pinglundetail) {
            this.comment_number = i;
            this.findID = findIdString;
            this.commentID = commentIdString;
            this.theFirstName = theFirstName;
            this.pinglundetail = pinglundetail;
        }

        @Override
        public boolean onLongClick(View arg0) {
            // TODO Auto-generated method stub
            //自己发表的评论
            if (detail.getUname().equals(theFirstName)) {
                showDialogs2(comment_number, commentID,pinglundetail);//改变样式去掉取消，直接留下删除。放入长按事件里面。
            } else {
                if (mSportsApp.mIsAdmin) {
                    ///弹出删除对话框
                    showDialogs2(comment_number, commentID,pinglundetail);
                }
            }
            return  false;
        }
    }
    // 3.4评论点击事件
    class commentOnClickListener3 implements OnClickListener {
        private int position;
        private int comment_number;
        private String findID;
        private String commentID;
        private String theFirstName;
        public commentOnClickListener3(int list_position, int i,
                                       String findIdString, String commentIdString, String theFirstName) {
            this.position = list_position;
            this.comment_number = i;
            this.findID = findIdString;
            this.commentID = commentIdString;
            this.theFirstName = theFirstName;
        }

        @Override
        public void onClick(View arg0) {
            //既不是管理员也不是自己发表的评论，只能回复或者不回复。
            if (!detail.getUname().equals(theFirstName)) {
//                changeCommentListener.OnCheckedChangeListener(position,
//                        findID, 2, commentID, theFirstName);
            }
        }
    }

    // 确认是否删除自己发的评论
    private void showDialogs(int comment_number, String commentid) {
        final int number = comment_number;
        final String commentID = commentid;
        alertDialog = new Dialog(mContext, R.style.sports_dialog);
        LayoutInflater sInflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = sInflater.inflate(R.layout.sports_dialog, null);
        v.findViewById(R.id.bt_ok).setBackgroundDrawable(
                mContext.getResources().getDrawable(
                        R.drawable.sports_slim_btn_click));
        Button ok = (Button) v.findViewById(R.id.bt_ok);
        ok.setText(mContext.getResources().getString(R.string.button_ok));
        ok.findViewById(R.id.bt_ok).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (mSportsApp.isOpenNetwork()) {
                    alertDialog.dismiss();
                    showWaitDialog(mContext.getResources().getString(
                            R.string.sports_dialog_deleteing));
                    new AsyncTask<Void, Void, ApiBack>() {
                        @Override
                        protected ApiBack doInBackground(Void... params) {
                            // TODO Auto-generated method stub
                            ApiBack back = null;
                            try {
                                back = (ApiBack) ApiJsonParser.delComment(
                                        mSportsApp.getSessionId(), commentID);
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
                                waitProgressDialog.dismiss();
                                Toast.makeText(
                                        mContext,
                                        mContext.getResources().getString(
                                                R.string.sports_delete_failed),
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                // 删除成功
                                // waitProgressDialog.dismiss();
                                // mFindMore.getTalkdetils().remove(number);
                                // int beforeCommentNum = mFindMore.getcCount();
                                // mFindMore.setcCount(beforeCommentNum - 1);
                                GetSingleThread thread = new GetSingleThread(
                                        Integer.parseInt(mFindMore.getFindId()));
                                thread.start();
                                Toast.makeText(
                                        mContext,
                                        mContext.getResources()
                                                .getString(
                                                        R.string.sports_delete_successed),
                                        Toast.LENGTH_SHORT).show();
                            }
                        }

                    }.execute();
                } else {
                    Toast.makeText(
                            mContext,
                            mContext.getResources().getString(
                                    R.string.error_cannot_access_net),
                            Toast.LENGTH_SHORT).show();

                }

            }
        });
        v.findViewById(R.id.bt_cancel).setBackgroundDrawable(
                mContext.getResources().getDrawable(
                        R.drawable.sports_login_btn_click));
        v.findViewById(R.id.bt_cancel).setOnClickListener(
                new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
        TextView title = (TextView) v.findViewById(R.id.title);
        title.setTextColor(mContext.getResources().getColor(R.color.text_login));
        title.setText(mContext.getResources().getString(
                R.string.confirm_exit_title));
        TextView message = (TextView) v.findViewById(R.id.message);
        message.setText(mContext.getResources().getString(
                R.string.make_sure_will_delete));
        v.setMinimumWidth((int) (SportsApp.ScreenWidth * 0.8));
        alertDialog.setCancelable(true);
        alertDialog.setContentView(v);
        alertDialog.show();
    }
    // 新版长按删除评论弹出框
    private void showDialogs2(int comment_number,
                              String commentid,final View pinglundetail) {
        final int number = comment_number;
        final String commentID = commentid;
        alertDialog = new Dialog(mContext, R.style.sports_dialog);
        LayoutInflater sInflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = sInflater.inflate(R.layout.sport_delcomdialog, null);
        v.findViewById(R.id.deletecomment);
        TextView ok = (TextView) v.findViewById(R.id.deletecomment);
        ok.findViewById(R.id.deletecomment).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (mSportsApp.isOpenNetwork()) {
                    alertDialog.dismiss();
                    showWaitDialog(mContext.getResources().getString(
                            R.string.sports_dialog_deleteing));
                    new AsyncTask<Void, Void, ApiBack>() {
                        @Override
                        protected ApiBack doInBackground(Void... params) {
                            // TODO Auto-generated method stub
                            ApiBack back = null;
                            try {
                                back = (ApiBack) ApiJsonParser.delComment(
                                        mSportsApp.getSessionId(), commentID);
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
                                waitProgressDialog.dismiss();
                                Toast.makeText(
                                        mContext,
                                        mContext.getResources().getString(
                                                R.string.sports_delete_failed),
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                // 删除成功
                                waitProgressDialog.dismiss();
                                mFindMore.getTalkdetils()
                                        .remove(number);
                                int beforeCommentNum = mFindMore.getcCount();
                                mFindMore.setcCount(
                                        beforeCommentNum - 1);
                                pinglundetail.startAnimation(animation);
                                new Handler().postDelayed(new Runnable() {
                                    public void run() {
                                        pinglundetail.setVisibility(View.GONE);
                                        showWaitDialog(mContext.getResources()
                                                .getString(
                                                        R.string.comment_wait));
                                        GetSingleThread thread = new GetSingleThread(
                                                Integer.parseInt(mFindMore
                                                        .getFindId()));
                                        thread.start();
                                    }
                                }, 1000);
                                Toast.makeText(
                                        mContext,
                                        mContext.getResources()
                                                .getString(
                                                        R.string.sports_delete_successed),
                                        Toast.LENGTH_SHORT).show();
                            }
                        }

                    }.execute();
                } else {
                    Toast.makeText(
                            mContext,
                            mContext.getResources().getString(
                                    R.string.error_cannot_access_net),
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
        v.setMinimumWidth((int) (SportsApp.ScreenWidth * 0.8));
        alertDialog.setCancelable(true);
        alertDialog.setContentView(v);
        alertDialog.show();
    }

    // 管理员选择回复还是删除
    @SuppressWarnings("deprecation")
    private void showAdminDialogs(int comment_number, String commentid,
                                  String findid, String thefirstname) {
        final int number = comment_number;
        final String commentID = commentid;
        final String findID = findid;
        final String theFirstName = thefirstname;
        alertDialog = new Dialog(mContext, R.style.sports_dialog);
        LayoutInflater sInflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = sInflater.inflate(R.layout.sports_dialog, null);
        v.findViewById(R.id.bt_ok).setBackgroundDrawable(
                mContext.getResources().getDrawable(
                        R.drawable.sports_slim_btn_click));
        Button delete = (Button) v.findViewById(R.id.bt_ok);
        // 删除
        delete.setText(mContext.getResources()
                .getString(R.string.detail_delete));
        delete.findViewById(R.id.bt_ok).setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        if (mSportsApp.isOpenNetwork()) {
                            alertDialog.dismiss();
                            showWaitDialog(mContext.getResources().getString(
                                    R.string.sports_dialog_deleteing));
                            new AsyncTask<Void, Void, ApiBack>() {
                                @Override
                                protected ApiBack doInBackground(Void... params) {
                                    // TODO Auto-generated method stub
                                    ApiBack back = null;
                                    try {
                                        back = (ApiBack) ApiJsonParser
                                                .delComment(mSportsApp
                                                                .getSessionId(),
                                                        commentID);
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
                                        waitProgressDialog.dismiss();
                                        Toast.makeText(
                                                mContext,
                                                mContext.getResources()
                                                        .getString(
                                                                R.string.sports_delete_failed),
                                                Toast.LENGTH_SHORT).show();
                                    } else {
                                        // 删除成功
                                        // waitProgressDialog.dismiss();
                                        // mFindMore.getTalkdetils()
                                        // .remove(number);
                                        // int beforeCommentNum
                                        // =mFindMore.getcCount();
                                        // mFindMore.setcCount(
                                        // beforeCommentNum - 1);
                                        GetSingleThread thread = new GetSingleThread(
                                                Integer.parseInt(mFindMore
                                                        .getFindId()));
                                        thread.start();
                                        Toast.makeText(
                                                mContext,
                                                mContext.getResources()
                                                        .getString(
                                                                R.string.sports_delete_successed),
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }.execute();
                        } else {
                            Toast.makeText(
                                    mContext,
                                    mContext.getResources().getString(
                                            R.string.error_cannot_access_net),
                                    Toast.LENGTH_SHORT).show();

                        }

                    }
                });
        // 回复
        v.findViewById(R.id.bt_cancel).setBackgroundDrawable(
                mContext.getResources().getDrawable(
                        R.drawable.sports_login_btn_click));
        Button reply = (Button) v.findViewById(R.id.bt_cancel);
        reply.setText(mContext.getResources().getString(
                R.string.multi_comment_tip_target));
        v.findViewById(R.id.bt_cancel).setOnClickListener(
                new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
//                        if (ba == 0) {
                            ba = 1;
                            findbottomLayout.setVisibility(View.VISIBLE);
                            rl_infowebview.setVisibility(View.GONE);
                            lay.setVisibility(View.VISIBLE);
                            find_bottom.setVisibility(View.VISIBLE);
                            showedit();
//                        } else {
//                            ba = 0;
//                            findbottomLayout.setVisibility(View.GONE);
//                            find_bottom.setVisibility(View.GONE);
//                            lay.setVisibility(View.GONE);
//                            hideedit();
//                        }
                        toID = commentID;
                        isHuifuPinglun = 2;
                        if (isHuifuPinglun == 1) {
                            find_upcomment_edittext
                                    .setHint(getResources().getString(
                                            R.string.sports_private_msg_texts_hint));
                        }
                        if (isHuifuPinglun == 2) {
                            // 回复
                            find_upcomment_edittext.setHint(getResources()
                                    .getString(R.string.multi_comment_tip_target)
                                    + theFirstName+ ":" );
                        }
//                        toID = commentID;
//                        isHuifuPinglun = 2;
                    }
                });
        TextView title = (TextView) v.findViewById(R.id.title);
        title.setTextColor(mContext.getResources().getColor(R.color.text_login));
        title.setText(mContext.getResources().getString(
                R.string.confirm_exit_title));
        TextView message = (TextView) v.findViewById(R.id.message);
        message.setText(mContext.getResources()
                .getString(R.string.reply_delete));
        v.setMinimumWidth((int) (SportsApp.ScreenWidth * 0.8));
        alertDialog.setCancelable(true);
        alertDialog.setContentView(v);
        alertDialog.show();
    }

    // 全部的设置

    private void setAllView() {
        checkView();
        init();
        initViews();
        mCurSel = 0;
        isShow = false;
        ImageView img = (ImageView) imgLayout.getChildAt(mCurSel);
        img.setBackgroundResource(R.drawable.qita_biaoqing_04);
        // 头像
        otherImage.setImageDrawable(null);
        if ("man".equals(mFindMore.getSex())) {
            otherImage
                    .setBackgroundResource(R.drawable.sports_user_edit_portrait_male);
        } else if ("woman".equals(mFindMore.getSex())) {
            otherImage
                    .setBackgroundResource(R.drawable.sports_user_edit_portrait);
        }
        mDownloader.download(mFindMore.getOtherimg(), otherImage, null);
        otherImage.setOnClickListener(new personalInformationOnClickListener(
                mFindMore.getOtheruid()));
        // 名字
        nametext.setText(mFindMore.getOthername());

        // 这是判断什么时候发的
        long time = System.currentTimeMillis() - mFindMore.getTimes() * 1000;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        if (time <= 60 * 1000)
            // 一分钟内显示刚刚
            start_time.setText(getResources().getString(
                    R.string.sports_time_justnow));
        else if (time <= 60 * 60 * 1000) {
            int h = (int) (time / 1000 / 60);
            // 一小时内显示多少分钟前
            start_time.setText("" + h
                    + getResources().getString(R.string.sports_time_mins_ago));
        } else {
            // start_time.setText(format.format(mFindMore.getTimes() * 1000));
            start_time.setText(FindOtherMoreAdapter.formatDisplayTime(
                    format.format(mFindMore.getTimes() * 1000),
                    "yyyy-MM-dd HH:mm"));
        }


        if (mFindMore.getDetils() != null && !"".equals(mFindMore.getDetils())) {
            boolean flog = mFindMore.getDetils().contains(" ");
            boolean flog2 = mFindMore.getDetils().contains("#");
            if (flog && flog2) {
                String likeUsers = mFindMore
                        .getDetils()
                        .substring(0,
                                mFindMore.getDetils().lastIndexOf("#") + 1)
                        .toString();
                String listRemove = mFindMore.getDetils()
                        .replace(likeUsers, "");
                String[] ary = likeUsers.split("#");
                for (int i = 0; i < ary.length; i++) {
                    int a = getActivityId(ary[i]);
                    if (a != -1) {
                        activitySendId.add(a);
                    }
                }

                SpannableStringBuilder ss = new SpannableStringBuilder(
                        likeUsers);
                String[] ary1 = likeUsers.split("#");
                for (int i = 0; i < ary1.length; i++) {
                    int b = ary1[i].length();
                    activityNameSize.add(b);
                }
                if (ary1.length > 0) {
                    // 最后一个
                    for (int i = 0; i < ary1.length; i++) {
                        final String name = ary1[i];
                        final int start = likeUsers.indexOf(name);
                        ss.setSpan(new ClickableSpan() {

                            @Override
                            public void onClick(View widget) {
                                // Toast.makeText(mContext, name,
                                // Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(
                                        UserActivityMainActivity.this,
                                        ActivityInfoWebView.class);
                                intent.putExtra("title_name", name.toString()
                                        .replace("#", ""));
                                intent.putExtra("action_url", "");
                                for (int j = 0; j < actionLists.size(); j++) {
                                    activityNameList.add(actionLists.get(j).getTitle());
                                    activityIdList.add(actionLists.get(j).getActionId());
                                    activityURLList.add(actionLists.get(j).getThuurl());
                                }
                                if (activityNameList.indexOf(name.toString()
                                        .replace("#", "")) != -1) {
                                    intent.putExtra("activity_id",
                                            activityIdList.get(activityNameList
                                                    .indexOf(name.toString()
                                                            .replace("#", ""))));
                                    ActionList action = actionLists
                                            .get(activityNameList.indexOf(name
                                                    .toString()
                                                    .replace("#", "")));
                                    activityTime_2 = action
                                            .getActionTime()
                                            .substring(
                                                    action.getActionTime()
                                                            .indexOf("-") + 1,
                                                    action.getActionTime()
                                                            .length())
                                            .replace(".", "-");
                                    Log.e("ActivityList", "活动时间："
                                            + activityTime_2);
                                    activityTime_2 = activityTime_2.replace(
                                            "-", "");
                                    int b = Integer.valueOf(activityTime_2)
                                            .intValue() + 1;
                                    activityTime_2 = b + "";
                                    String year = activityTime_2.substring(0, 4);
                                    String month = activityTime_2.substring(4,6);
                                    String day = activityTime_2.substring(6, 8);
                                    activityTime_2 = year + "-" + month + "-"
                                            + day;
                                    intent.putExtra("activitytime",
                                            activityTime_2);
                                    mContext.startActivity(intent);
                                }
                            }

                            @Override
                            public void updateDrawState(TextPaint ds) {
                                super.updateDrawState(ds);
                                // ds.setColor(Color.RED); // 设置文本颜色
                                // 去掉下划线
                                ds.setUnderlineText(false);
                            }
                        }, start, start + name.length(), 0);
                    }
                }
                SpannableStringBuilder aaaa = ss.append(listRemove);
                ForegroundColorSpan greenSpan = new ForegroundColorSpan(
                        Color.parseColor("#5BBBEC"));
                ForegroundColorSpan blackSpan = new ForegroundColorSpan(
                        Color.BLACK);
                if (aaaa.toString().indexOf("#") != 0) {
                    if (aaaa.toString().contains("#")) {
                        aaaa.setSpan(blackSpan, 0,
                                aaaa.toString().indexOf("#"),
                                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                }
                if (aaaa.toString().contains("#")) {
                    aaaa.setSpan(greenSpan, aaaa.toString().indexOf("#"), aaaa
                                    .toString().lastIndexOf("#") + 1,
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
                SpannableString details = getExpressionString2(mContext, aaaa);
                tdetils.setText(details);
                tdetils.setMovementMethod(LinkMovementMethod.getInstance());
            } else {
                SpannableString details = getExpressionString3(mContext,mFindMore.getDetils());
                tdetils.setText(details);
            }
            tdetils.setVisibility(View.VISIBLE);
        } else {
            tdetils.setVisibility(View.GONE);
        }
        setZanAndPinglun();
    }

    // 监听删除整个item
    class deleteListener implements OnClickListener {
        // 发现ID
        private String find_id;

        public deleteListener(String numlist) {
            this.find_id = numlist;
        }

        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub
            showdeleteAllDialogs(find_id);
        }

    }

    // 确认是否删除整个item
    private void showdeleteAllDialogs(String find_id) {
        final String findID = find_id;
        alertDialog = new Dialog(mContext, R.style.sports_dialog);
        LayoutInflater sInflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = sInflater.inflate(R.layout.sports_dialog, null);
        v.findViewById(R.id.bt_ok).setBackgroundDrawable(
                mContext.getResources().getDrawable(
                        R.drawable.sports_slim_btn_click));
        Button ok = (Button) v.findViewById(R.id.bt_ok);
        ok.setText(mContext.getResources().getString(R.string.button_ok));
        ok.findViewById(R.id.bt_ok).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                alertDialog.dismiss();
                // 有没有网
                if (mSportsApp.isOpenNetwork()) {
                    showWaitDialog(mContext.getResources().getString(
                            R.string.sports_dialog_deleteing));
                    new AsyncTask<Void, Void, ApiBack>() {
                        @Override
                        protected ApiBack doInBackground(Void... params) {
                            // TODO Auto-generated method stub
                            ApiBack back = null;
                            try {
                                back = (ApiBack) ApiJsonParser.delFind(
                                        mSportsApp.getSessionId(), findID);
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
                                waitProgressDialog.dismiss();
                                Toast.makeText(
                                        mContext,
                                        mContext.getResources().getString(
                                                R.string.sports_delete_failed),
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                // 删除成功
                                waitProgressDialog.dismiss();

                                Message msg = new Message();
                                msg.what = FindOtherFragment.RETURN_ACTIVITYDEL_RESULT;
                                msg.obj = findID;
                                Handler handler = mSportsApp.getFindHandler();
                                mSportsApp.setDongtai_personalceter(3);
                                if (handler != null) {
                                    handler.sendMessage(msg);
                                }
                                Toast.makeText(
                                        mContext,
                                        mContext.getResources()
                                                .getString(
                                                        R.string.sports_delete_successed),
                                        Toast.LENGTH_SHORT).show();

                                finish();
                            }
                        }
                    }.execute();
                } else {
                    Toast.makeText(
                            mContext,
                            mContext.getResources().getString(
                                    R.string.error_cannot_access_net),
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
        v.findViewById(R.id.bt_cancel).setBackgroundDrawable(
                mContext.getResources().getDrawable(
                        R.drawable.sports_login_btn_click));
        v.findViewById(R.id.bt_cancel).setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
        TextView title = (TextView) v.findViewById(R.id.title);
        title.setTextColor(mContext.getResources().getColor(R.color.text_login));
        title.setText(mContext.getResources().getString(
                R.string.confirm_exit_title));
        TextView message = (TextView) v.findViewById(R.id.message);
        message.setText(mContext.getResources().getString(
                R.string.make_sure_will_delete));
        v.setMinimumWidth((int) (SportsApp.ScreenWidth * 0.8));
        alertDialog.setCancelable(true);
        alertDialog.setContentView(v);
        alertDialog.show();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK: {
                if (findId == -1) {
                    Message msg = new Message();
                    msg.what = FindOtherFragment.RETURN_MORE;
                    msg.obj = mFindMore;
                    Handler handler = null;
                    if (toFlag == 1) {
                        handler = mSportsApp.getPersonalFindHandler();
                        handler.sendMessage(msg);
                        finish();
                    } else if (toFlag == 2) {
                        if (SportCircleMainFragment.indexFlag == 0) {
                            handler = mSportsApp.getFindHandler();

                        } else if (SportCircleMainFragment.indexFlag == 1) {
                            handler = mSportsApp.getGoodfriendsHandler();
                        }
                        if (handler != null) {
                            handler.sendMessage(msg);
                        }
                        finish();
                    } else {
                        finish();
                    }
                } else {
                    Intent mIntent = new Intent();
                    mIntent.putExtra("mFindMore", mFindMore);
                    // 设置结果，并进行传送
                    this.setResult(1, mIntent);
                    finish();
                }

            }
        }
        return super.onKeyDown(keyCode, event);
    }

    // 通过传入一个活动的名字得到活动的ID。若传入的活动名字错误、返回-1；
    public int getActivityId(String activityName) {
        if (activityName != null && !" ".equals(activityName)) {
            if (actionLists != null) {
                for (int i = 0; i < actionLists.size(); i++) {
                    activityNameList.add(actionLists.get(i).getTitle());
                    activityIdList.add(actionLists.get(i).getActionId());
                    activityURLList.add(actionLists.get(i).getThuurl());
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

    private class GetActionDataTask2 extends
            AsyncTask<Void, Void, List<ActionList>> {

        @Override
        protected List<ActionList> doInBackground(Void... sessionid) {

            List<ActionList> actionLists = null;
            try {
                actionLists = ApiJsonParser.getNewActionList(
                        mSportsApp.getSessionId(), "z" + getResources().getString(R.string.config_game_id), 1);
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
}
