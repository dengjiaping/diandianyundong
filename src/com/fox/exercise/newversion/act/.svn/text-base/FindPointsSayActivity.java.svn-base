package com.fox.exercise.newversion.act;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.ingenic.indroidsync.SportsApp;

import com.fox.exercise.AbstractBaseOtherActivity;
import com.fox.exercise.FindOtherFragment;
import com.fox.exercise.ImageDownloader;
import com.fox.exercise.R;
import com.fox.exercise.api.ApiBack;
import com.fox.exercise.api.ApiJsonParser;
import com.fox.exercise.api.ApiNetException;
import com.fox.exercise.api.ApiSessionOutException;
import com.fox.exercise.api.entity.ExpressionItems;
import com.fox.exercise.api.entity.UserDetail;
import com.fox.exercise.bitmap.util.ImageResizer;
import com.fox.exercise.newversion.entity.CircleFindsLists;
import com.fox.exercise.newversion.entity.PointsSay;
import com.fox.exercise.newversion.entity.SysSportCircleComments;
import com.fox.exercise.pedometer.ImageWorkManager;
import com.fox.exercise.util.RoundedImage;
import com.fox.exercise.util.ScrollLayout;
import com.fox.exercise.util.ScrollLayout.OnViewChangeListener;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.WindowManager;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
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
import android.widget.AdapterView.OnItemClickListener;

/**
 * @author loujungang 点点说 详情页面
 */
public class FindPointsSayActivity extends AbstractBaseOtherActivity implements
        OnClickListener {
    private TextView iView;
    private SportsApp mSportsApp;
    private Context mContext;
    // 图片正方形
    private ImageResizer mImageWorker;
    private ImageDownloader mDownloader2 = null;
    int width, height;
    private CircleFindsLists circleFindsLists;
    private Dialog mLoadProgressDialog = null;
    private TextView mDialogMessage;
    private int times = 0;
    private final int FRESH_LIST = 111;// 更新成功
    private final int FRESH_FAILED = 112;// 更新失败
    private final int FRESH_NULL = 114;
    private SportsFindMoreHandler mSportsFindMoreHandler;

    private TextView points_say_title, points_say_time, points_say_content;
    private ImageView points_say_icon;

    private RelativeLayout rScrollLayout;
    private ScrollLayout scrollLayout;
    private LinearLayout find_upcomment_text;// 输入文字layout
    private EditText find_upcomment_edittext;// 输入框
    private Button find_upcomment_send, find_unavailable;// 提交按钮切换
    private LinearLayout activity_pinglun_layout;// 评论linearlayout
    private TextView activity_submit_pinglun;// 有评论的时候隐藏没有的时候显示
    private ImageButton find_expression_text_btn;// 后面的是表情
    private LinearLayout imgLayout;
    private int mViewCount;
    private int mCurSel;
    private Boolean isShow;

    private LinearLayout zong_activity_layout;

    private List<ExpressionItems> imgItems;
    private String[] imgStr;
    private static final float APP_PAGE_SIZE = 21.0f;

    private View pinglunDetails;
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
    private LayoutInflater mInflater;
    private int numcom;

    private ImageWorkManager mImageWorkerMan_Icon;
    private ImageResizer mImageWorker_Icon;

    private Dialog alertDialog;
    private String toID = null;// 用来当回复的时候传递to_id,如果直接评论则为null 这个id是评论id
    private int isHuifuPinglun = 1;
    private Dialog waitProgressDialog;

    private UserDetail detail;

    private PointsSay mPointsSay;

    @Override
    public void initIntentParam(Intent intent) {
        // TODO Auto-generated method stub
        title = getResources().getString(R.string.points_say);
        if (intent != null) {
            circleFindsLists = (CircleFindsLists) intent
                    .getSerializableExtra("CircleFindsLists");
        }
    }

    @Override
    public void initView() {
        // TODO Auto-generated method stub
        showContentView(R.layout.activity_points_say_main);
        mSportsApp = (SportsApp) getApplication();
        detail = SportsApp.getInstance().getSportUser();
        mContext = this;
        mDownloader2 = new ImageDownloader(mContext);
        mDownloader2.setType(ImageDownloader.OnlyOne);
        mImageWorkerMan_Icon = new ImageWorkManager(this, 0, 0);
        mImageWorker_Icon = mImageWorkerMan_Icon.getImageWorker();
        WindowManager wm = getWindowManager();
        width = wm.getDefaultDisplay().getWidth();
        mImageWorker = mSportsApp.getImageWorker(mContext, width, 0);
        iView = new TextView(this);
        iView.setText("分享");
        iView.setTextColor(getResources().getColor(R.color.black));
        iView.setBackgroundResource(R.color.white);
        iView.setLayoutParams(new LinearLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        iView.setPadding(20, 20, 20, 20);
        showRightBtn(iView);
        init();
        initViews();
        isShow = false;
        mSportsFindMoreHandler = new SportsFindMoreHandler();
        waitShowDialog();
        SportsFindMoreThread mFindMoreThread = new SportsFindMoreThread();
        mFindMoreThread.start();
    }

    @Override
    public void setViewStatus() {
        // TODO Auto-generated method stub
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
        mSportsApp = null;
    }

    private void waitShowDialog() {
        // TODO Auto-generated method stub
        if (mLoadProgressDialog == null) {
            mLoadProgressDialog = new Dialog(this, R.style.sports_dialog);
            LayoutInflater mInflater = getLayoutInflater();
            View v1 = mInflater.inflate(R.layout.bestgirl_progressdialog, null);
            mDialogMessage = (TextView) v1.findViewById(R.id.message);
            mDialogMessage.setText(R.string.bestgirl_wait);
            v1.setMinimumWidth((int) (SportsApp.ScreenWidth * 0.8));
            mLoadProgressDialog.setContentView(v1);
        }
        if (mLoadProgressDialog != null)
            if (!mLoadProgressDialog.isShowing() && !this.isFinishing())
                mLoadProgressDialog.show();
    }

    class SportsFindMoreThread extends Thread {
        @Override
        public void run() {
            // TODO Auto-generated method stub
            super.run();
            Message msg = null;
            PointsSay pointsSay = null;

            try {
                pointsSay = ApiJsonParser.getCirclePointsSayContent(
                        mSportsApp.getSessionId(), times,
                        circleFindsLists.getId(), 2, 0);
            } catch (ApiNetException e) {
                e.printStackTrace();
            } catch (ApiSessionOutException e) {
                e.printStackTrace();
            }
            if (pointsSay != null) {
                msg = Message.obtain(mSportsFindMoreHandler, FRESH_LIST);
                msg.obj = pointsSay;
                msg.sendToTarget();
            } else {
                msg = Message.obtain(mSportsFindMoreHandler, FRESH_FAILED);
                msg.sendToTarget();
            }
        }

    }

    class SportsFindMoreHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            switch (msg.what) {
                case FRESH_LIST:
                    PointsSay pointsSay = (PointsSay) msg.obj;
                    mPointsSay = pointsSay;
                    points_say_title.setText(pointsSay.getTitle());
                    points_say_time.setText(pointsSay.getStart_time());
                    points_say_content.setText(pointsSay.getConnent());
                    mImageWorker.loadImage(pointsSay.getImg(), points_say_icon,
                            null, null, false);
                    ArrayList<SysSportCircleComments> getfList = pointsSay
                            .getfList();
                    int childCount = activity_pinglun_layout.getChildCount();
                    if (getfList != null && getfList.size() > 0) {
                        activity_submit_pinglun.setVisibility(View.GONE);
                        if (childCount > 2) {
                            activity_pinglun_layout.removeViewsInLayout(2, childCount - 2);
                        }
                        setZanAndPinglun();
                    } else {
                        activity_submit_pinglun.setVisibility(View.VISIBLE);
                        if (childCount > 2) {
                            activity_pinglun_layout.removeViewsInLayout(2, childCount - 2);
                        }
                    }
                    if (mLoadProgressDialog != null)
                        if (mLoadProgressDialog.isShowing())
                            mLoadProgressDialog.dismiss();
                    if (waitProgressDialog != null)
                        if (waitProgressDialog.isShowing())
                            waitProgressDialog.dismiss();
                    break;

                case FRESH_FAILED:
                    if (!mSportsApp.isOpenNetwork()) {
                        Toast.makeText(
                                mContext,
                                getResources().getString(
                                        R.string.acess_server_error),
                                Toast.LENGTH_SHORT).show();
                    } else if (this != null) {
                        Toast.makeText(
                                mContext,
                                getResources().getString(
                                        R.string.sports_get_list_failed),
                                Toast.LENGTH_SHORT).show();
                    }
                    if (mLoadProgressDialog != null)
                        if (mLoadProgressDialog.isShowing())
                            mLoadProgressDialog.dismiss();
                    if (waitProgressDialog != null)
                        if (waitProgressDialog.isShowing())
                            waitProgressDialog.dismiss();
                    // sportsfindmoreAdapter.notifyDataSetChanged();
                    break;
                default:
                    break;
            }
        }

    }

    private void init() {
        mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        points_say_title = (TextView) findViewById(R.id.points_say_title);
        points_say_time = (TextView) findViewById(R.id.points_say_time);
        points_say_content = (TextView) findViewById(R.id.points_say_content);
        points_say_icon = (ImageView) findViewById(R.id.points_say_icon);
        rScrollLayout = (RelativeLayout) findViewById(R.id.rScrollLayout);
        scrollLayout = (ScrollLayout) findViewById(R.id.ScrollLayoutTest);
        find_upcomment_text = (LinearLayout) findViewById(R.id.find_upcomment_text);
        activity_pinglun_layout = (LinearLayout) findViewById(R.id.activity_pinglun_layout);
        find_upcomment_edittext = (EditText) findViewById(R.id.find_upcomment_edittext);
        find_upcomment_send = (Button) findViewById(R.id.find_upcomment_send);
        find_unavailable = (Button) findViewById(R.id.find_unavailable);
        activity_submit_pinglun = (TextView) findViewById(R.id.activity_submit_pinglun);
        find_expression_text_btn = (ImageButton) findViewById(R.id.find_expression_text_btn);
        imgLayout = (LinearLayout) findViewById(R.id.imageLayot);

        find_upcomment_edittext.addTextChangedListener(mTextWatcher);
        find_upcomment_edittext.setOnClickListener(this);
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
                hideedit();
            }
        });

        find_expression_text_btn.setOnClickListener(this);

        scrollLayout.SetOnViewChangeListener(new OnViewChangeListener() {

            @Override
            public void OnViewChange(int view) {
                // TODO Auto-generated method stub
                setCurPoint(view);
            }
        });

        find_upcomment_send.setOnClickListener(this);

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

    TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
            // TODO Auto-generated method stub
            String editable = find_upcomment_edittext.getText().toString();
            String str = UserActivityMainActivity.filterEmoji(editable
                    .toString());
            if (!editable.equals(str)) {
                find_upcomment_edittext.setText(str);
                // 设置新的光标所在位置
                find_upcomment_edittext.setSelection(str.length());
                // 暂不支持此类型符号的输入
                Toast.makeText(mContext,
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
            Boolean isTrue = UserActivityMainActivity.stringFilter(editable
                    .toString());
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

    @Override
    public void onClick(View view) {
        // TODO Auto-generated method stub
        switch (view.getId()) {
            case R.id.find_expression_text_btn:
                hideedit();
                if (isShow == false) {
                    isShow = true;
                    rScrollLayout.setVisibility(View.VISIBLE);
                } else if (isShow == true) {
                    isShow = false;
                    rScrollLayout.setVisibility(View.GONE);
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
                if (mSportsApp.mIsNetWork) {
                    send(mPointsSay.getId() + "", toID + "", find_upcomment_edittext
                            .getText().toString(), null, null);
                    find_upcomment_edittext.setText("");
                    hideedit();
                    rScrollLayout.setVisibility(View.GONE);
                } else {
                    Toast.makeText(mContext,
                            getResources().getString(R.string.newwork_not_connected),
                            Toast.LENGTH_SHORT).show();
                }


                break;

            default:
                break;
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
//	private void showedit() {
//		find_upcomment_edittext.setFocusable(true);
//		find_upcomment_edittext.setFocusableInTouchMode(true);
//		find_upcomment_edittext.requestFocus();
//		InputMethodManager inputManager = (InputMethodManager) find_upcomment_edittext
//				.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
//		inputManager.showSoftInput(find_upcomment_edittext, 0);
//	}

    public void initViews() {
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

    /**
     * 设置点赞和评论
     */
    private void setZanAndPinglun() {
        ArrayList<SysSportCircleComments> getfList = mPointsSay.getfList();
        if (getfList.size() > 0) {
            // 将评论内容显示出来
            for (int i = 0; i < getfList.size(); i++) {
                theFirstName = getfList.get(i).getName();
                theSecondName = getfList.get(i).getTo_name();
                theTalkDetils = getfList.get(i).getContent();
                thewavComment = getfList.get(i).getWav();
                thewavDuration = getfList.get(i).getWavtime();

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
                            - Long.valueOf(getfList.get(i).getInputtime())
                            * 1000;
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
                        activity_submit_time
                                .setText(pformat.format(Long.valueOf(getfList
                                        .get(i).getInputtime()) * 1000));
                    }

                    if ("".equals(getfList.get(i).getImg())
                            || getfList.get(i).getImg() == null) {
                        if (getfList.get(i).getSex().equals("1")) {
                            activity_image_yuyinicon
                                    .setImageResource(R.drawable.sports_user_edit_portrait_male);
                        } else {
                            activity_image_yuyinicon
                                    .setImageResource(R.drawable.sports_user_edit_portrait);
                        }
                    } else {
                        if (getfList.get(i).getSex().equals("1")) {
                            activity_image_yuyinicon
                                    .setImageResource(R.drawable.sports_user_edit_portrait_male);
                        } else {
                            activity_image_yuyinicon
                                    .setImageResource(R.drawable.sports_user_edit_portrait);
                        }
                        mImageWorker_Icon.loadImage(getfList.get(i).getImg(),
                                activity_image_yuyinicon, null, null, false);
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

                } else {
                    pinglunDetails = mInflater.inflate(
                            R.layout.user_activity_sys_pinglun_layout, null);
                    RoundedImage activity_image_icon = (RoundedImage) pinglunDetails
                            .findViewById(R.id.activity_image_icon);
                    TextView activity_submit_name = (TextView) pinglunDetails
                            .findViewById(R.id.activity_submit_name);
                    TextView activity_submit_content = (TextView) pinglunDetails
                            .findViewById(R.id.activity_submit_content);
                    TextView activity_submit_time = (TextView) pinglunDetails
                            .findViewById(R.id.activity_submit_time);
                    TextView activity_submit_address = (TextView) pinglunDetails
                            .findViewById(R.id.activity_submit_address);
                    //地址

                    if (getfList.get(i).getProvince() != null && !"".equals(getfList.get(i).getProvince())) {
                        activity_submit_address.setText(getfList.get(i).getProvince());
                    } else {
                        activity_submit_address.setVisibility(View.GONE);
                    }
                    // 评论时间
                    long pinglTm = System.currentTimeMillis()
                            - Long.valueOf(getfList.get(i).getInputtime())
                            * 1000;
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
                        activity_submit_time
                                .setText(pformat.format(Long.valueOf(getfList
                                        .get(i).getInputtime()) * 1000));
                    }

                    if ("".equals(getfList.get(i).getImg())
                            || getfList.get(i).getImg() == null) {
                        if (getfList.get(i).getSex().equals("1")) {
                            activity_image_icon
                                    .setImageResource(R.drawable.sports_user_edit_portrait_male);
                        } else {
                            activity_image_icon
                                    .setImageResource(R.drawable.sports_user_edit_portrait);
                        }
                    } else {
                        if (getfList.get(i).getSex().equals("1")) {
                            activity_image_icon
                                    .setImageResource(R.drawable.sports_user_edit_portrait_male);
                        } else {
                            activity_image_icon
                                    .setImageResource(R.drawable.sports_user_edit_portrait);
                        }
                        mImageWorker_Icon.loadImage(getfList.get(i).getImg(),
                                activity_image_icon, null, null, false);
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
                    // Log.e(TAG, "长度0--6-----"+newMessageString.length());
                    List<SpannableString> list = getContentString(mContext,
                            newMessageString, numcom);
                    for (SpannableString span : list) {
                        activity_submit_content.setText(span);
                        // Log.e(TAG, "长度2-------"+span.length());
                    }
                    activity_submit_content.invalidate();

                }
                ViewGroup p = (ViewGroup) pinglunDetails.getParent();
                if (p != null) {
                    p.removeAllViewsInLayout();
                }
                pinglunDetails.setOnClickListener(new commentOnClickListener(i,
                        mPointsSay.getId() + "", getfList.get(i).getId(),
                        theFirstName));
                activity_pinglun_layout.addView(pinglunDetails);

            }

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
                                .getResources().getColor(R.color.remind)), 0,
                        endone + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                spanString.setSpan(new ForegroundColorSpan(mContext
                                .getResources().getColor(R.color.sports_value)),
                        endone + 1, str.length(),
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            } else {
                int endTwo = firstname;
                int endThree = endTwo + 2 + secondname;
                spanString.setSpan(new ForegroundColorSpan(mContext
                                .getResources().getColor(R.color.remind)), 0, endTwo,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                spanString.setSpan(new ForegroundColorSpan(mContext
                                .getResources().getColor(R.color.sports_value)),
                        endTwo, endTwo + 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                spanString.setSpan(new ForegroundColorSpan(mContext
                                .getResources().getColor(R.color.remind)), endTwo + 2,
                        endThree + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                spanString.setSpan(new ForegroundColorSpan(mContext
                                .getResources().getColor(R.color.sports_value)),
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
                for (ExpressionItems item : FindOtherFragment.imgItems) {
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
                                .getResources().getColor(R.color.remind)), 0,
                        endone + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                // spanString.setSpan(new ForegroundColorSpan(mContext
                // .getResources().getColor(R.color.sports_value)),
                // endone + 1, str.length(),
                // Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            } else {
                int endTwo = firstname;
                int endThree = endTwo + 2 + secondname;
                spanString.setSpan(new ForegroundColorSpan(mContext
                                .getResources().getColor(R.color.remind)), 0, endTwo,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                spanString.setSpan(new ForegroundColorSpan(mContext
                                .getResources().getColor(R.color.sports_value)),
                        endTwo, endTwo + 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                spanString.setSpan(new ForegroundColorSpan(mContext
                                .getResources().getColor(R.color.remind)), endTwo + 2,
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
                for (ExpressionItems item : FindOtherFragment.imgItems) {
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

            } else {
                /**
                 * (position,findIdString,2,commentID,theFirstName)
                 * 第一个参数所属发现在list中位置 第二个参数发现ID， 第三个参数用来判断评论还是回复1代表直接评论2代表回复
                 * 第四个参数用来当回复的时候传递to_id,如果直接评论则为null
                 * 第五个参数用来当回复的时候显示回复给谁，如果直接评论则null
                 */
                if (mSportsApp.mIsAdmin) {
                    showAdminDialogs(comment_number, commentID, findID,
                            theFirstName);
                } else {
                    // changeCommentListener.OnCheckedChangeListener(position,
                    // findID, 2, commentID, theFirstName);
                    // send(findID, toID, commentText, commentWav, wavDuration)
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
                                + "  :" + theFirstName);
                    }
                }
            }
        }
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
                                        SportsFindMoreThread sportsFindMoreThread = new SportsFindMoreThread();
                                        sportsFindMoreThread.start();
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
                        // changeCommentListener.OnCheckedChangeListener(position,
                        // findID, 2, commentID, theFirstName);
                        toID = commentID;
                        isHuifuPinglun = 2;
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
                                SportsFindMoreThread sportsFindMoreThread = new SportsFindMoreThread();
                                sportsFindMoreThread.start();
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

    // 上传评论
    public void send(final String findID, final String toID,
                     final String commentText, final String commentWav,
                     final String wavDuration) {
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
                    commentIdString = ApiJsonParser.addSysComment(
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
                    FindPointsSayActivity.this.toID = null;
                    SportsFindMoreThread sportsFindMoreThread = new SportsFindMoreThread();
                    sportsFindMoreThread.start();
                }
            }

        }.execute();
    }

}
