package com.fox.exercise;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Handler;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import cn.ingenic.indroidsync.SportsApp;

import com.fox.exercise.api.ApiBack;
import com.fox.exercise.api.ApiJsonParser;
import com.fox.exercise.api.ApiNetException;
import com.fox.exercise.api.entity.ExpressionItems;
import com.fox.exercise.api.entity.FindComment;
import com.fox.exercise.api.entity.FindMore;
import com.fox.exercise.api.entity.UserDetail;
import com.fox.exercise.bitmap.util.ImageResizer;
import com.fox.exercise.util.RoundedImage;
import com.fox.exercise.util.ScrollLayout;
import com.fox.exercise.util.ScrollLayout.OnViewChangeListener;

import android.app.Activity;
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
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
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

import com.fox.exercise.TitlePopup.OnItemOnClickListener;

public class CommentsDetailActivity extends Activity implements
        OnItemOnClickListener {

    private final String TAG = "CommentsDetailActivity";
    private Context mContext;
    private LinearLayout mContentView;
    private LayoutInflater mInflater;
    private ViewHolder holder;
    private UserDetail detail;
    private ImageDownloader mDownloader = null;
    private ImageDownloader mDownloader2 = null;
    private ImageResizer mImageWorker;
    private int mItemHeight = 0;
    private SportsApp mSportsApp;
    private FindMore commentItem;
    private Dialog alertDialog;
    private String theFirstName;
    private String theSecondName;
    // private String theSecondId;
    private String theTalkDetils;
    private String thewavComment;
    private String thewavDuration;
    // 文字评论最终要显示的字符串
    private String newMessageString;
    private List<String> firstNameList = null;
    private List<LinearLayout> commentLayout = null;
    private View newView;
    private String[] imgStr;
    private SpannableString spans;
    public static List<ExpressionItems> imgItems;
    private static final float APP_PAGE_SIZE = 21.0f;
    private String toNameStr, commentText, commentWav;
    private String find_Id, to_Id;
    private int toNumber;

    private boolean findBool = true;
    private LinearLayout findUpcommentText;
    private EditText findUpcommentEdittext;
    private Button findUpcommentSend;
    private Button findUnavailable;
    private RelativeLayout findLayout;
    private RelativeLayout rScrollLayout;
    private ScrollLayout scrollLayout;
    private LinearLayout imgLayout;
    private LinearLayout layoutVoice;
    private ImageButton findTextBtn;
    private Button findPressBtn;
    private boolean upTypeText = false;
    private ImageView findExpressBtn;
    private Boolean isShow;
    private ResideMenu resideMenu;
    private int mViewCount;
    private int mCurSel;

    public static int act;
    public static String mItemFindId;

    private int numcom;
    private Button btBack;
    private TitlePopup titlePopup;
    public List<ImageView> imgList2 = new ArrayList<ImageView>();
    public List<ImageView> imgList3 = new ArrayList<ImageView>();
    public List<ImageView> imgList4 = new ArrayList<ImageView>();
    public List<ImageView> imgList5 = new ArrayList<ImageView>();
    public List<ImageView> imgList6 = new ArrayList<ImageView>();
    public List<ImageView> imgList7 = new ArrayList<ImageView>();
    public List<ImageView> imgList8 = new ArrayList<ImageView>();
    public List<ImageView> imgList9 = new ArrayList<ImageView>();
    //录音
    boolean isPause = false;
    private boolean isStart = true;
    MediaPlayer mPlayer = null;
    private RecordHelper mRecorder;
    private int currentDuration;
    private ImageView wavBeginOne;
    private static final int RECORD_LOADING = 6;
    private static final int RECORD_PREPARED = 7;
    private static final int RECORD_FINISH = 8;
    private static final int RECORD_PAUSE = 9;
    private static final int RECORD_ERROR = 10;
    private static final int FLAG_RUNWAV = 11;
    private static final int RESULT_ERROR = 12;

    private class ViewHolder {
        private RoundedImage otherImage;
        private TextView nametext;
        private TextView detils;
        private ImageView img1;
        private ImageView img2;
        private ImageView img3;
        private ImageView img4;
        private ImageView img5;
        private ImageView img6;
        private ImageView img7;
        private ImageView img8;
        private ImageView img9;
        // 发布时间
        private TextView start_time;
        // 删除
        private TextView deleteText;
        // 点赞和评论
        private ImageView goodandtext;
        // 点赞人数text及所在layout
        private TextView goodPeopole;
        private LinearLayout goodLayout;
        // 赞和评论整体
        private LinearLayout commentLayout;
        // 赞和评论下面的横线
        private View textgoodline;
        // 评论人数text及所在layout
        private LinearLayout textLayout;
        private TextView textPeopole;
        // 评论内容
        private LinearLayout talkdetilsLayout;

    }

    //录音播放
    Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                /*case ADD_ITEM:
                    UserPrimsgOne userPrimsgOne = (UserPrimsgOne) msg.obj;
					mAdapter.addItem(userPrimsgOne);
					break;*/
                case RECORD_LOADING:
                    ImageView wavBegin = (ImageView) msg.obj;
                    wavBegin.setImageResource(+R.anim.record_loading);
                    AnimationDrawable ad1 = (AnimationDrawable) wavBegin.getDrawable();
                    ad1.start();
                    break;
                case RECORD_PREPARED:
                    ((ImageView) msg.obj).setImageResource(+R.anim.record_run);
                    //mWavBegin = ((ImageView) msg.obj);
                    AnimationDrawable ad2 = (AnimationDrawable) ((ImageView) msg.obj).getDrawable();
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

            }
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_comments_details);

        mContext = this;
        mInflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        holder = new ViewHolder();
        // 这个数据一定是上一级界面传递进来的，也就是包含了发表的文章以及所有评论的消息
        Intent intent = getIntent();

        detail = SportsApp.getInstance().getSportUser();
        this.mDownloader = new ImageDownloader(this);
        mDownloader.setType(ImageDownloader.ICON);
        this.mDownloader2 = new ImageDownloader(this);
        mDownloader2.setType(ImageDownloader.OnlyOne);
        initExpressionViews();

        mSportsApp = (SportsApp) this.getApplication();
        mItemHeight = Util.getRealPixel_W(this,
                (int) (SportsApp.ScreenWidth * 0.625) / 3);
        mImageWorker = mSportsApp
                .getImageWorker(this, mItemHeight, mItemHeight);
        mImageWorker.setType(1);
        btBack = (Button) this.findViewById(R.id.layout_letf);
        btBack.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                finish();
            }
        });
        titlePopup = new TitlePopup(mContext, mSportsApp.dip2px(165),
                mSportsApp.dip2px(40));
        // 得到数据
        commentItem = mSportsApp.mFindMore;
        mItemFindId = commentItem.getFindId();
        // 根据数据的类型，获取View,Adapter也是这么做的
        View view = initView(commentItem);

        mContentView = (LinearLayout) this
                .findViewById(R.id.linearLayout_comments_show);
        mContentView.addView(view);

//		resideMenu = ((MainFragmentActivity) getActivity()).getResideMenu();
        initView();

        setOnCheckedChangeListener(checkListerner);

    }

    private void setImageVoid(int type, String[] urls, List<ImageView> imgList) {
        for (int i = 0; i < type; i++) {
            LinearLayout.LayoutParams lps = (LinearLayout.LayoutParams) imgList
                    .get(i).getLayoutParams();
            lps.width = (int) (SportsApp.ScreenWidth * 0.625) / 3;
            lps.height = (int) (SportsApp.ScreenWidth * 0.625) / 3;
            imgList.get(i).setLayoutParams(lps);
            mImageWorker.loadImage(urls[i], imgList.get(i), null, null, false);
        }

    }

    public View initView(FindMore item) {
        View convertView = null;
        String[] urls = item.getImgs();
        int type = urls.length;

        switch (type) {
            case 1:
                convertView = (LinearLayout) mInflater.inflate(
                        R.layout.sports_find_other_list_item, null);
                holder.img1 = (ImageView) convertView
                        .findViewById(R.id.detils_img_one);
                break;
            case 2:

                convertView = (LinearLayout) mInflater.inflate(
                        R.layout.sports_find_other_list_item_two, null);
                holder.img1 = (ImageView) convertView
                        .findViewById(R.id.detils_img_one);
                holder.img2 = (ImageView) convertView
                        .findViewById(R.id.detils_img_two);
                break;
            case 3:

                convertView = (LinearLayout) mInflater.inflate(
                        R.layout.sports_find_other_list_item_three, null);
                holder.img1 = (ImageView) convertView
                        .findViewById(R.id.detils_img_one);
                holder.img2 = (ImageView) convertView
                        .findViewById(R.id.detils_img_two);
                holder.img3 = (ImageView) convertView
                        .findViewById(R.id.detils_img_three);
                break;
            case 4:
                convertView = (LinearLayout) mInflater.inflate(
                        R.layout.sports_find_other_list_item_four, null);
                holder.img1 = (ImageView) convertView
                        .findViewById(R.id.detils_img_one);
                holder.img2 = (ImageView) convertView
                        .findViewById(R.id.detils_img_two);
                holder.img3 = (ImageView) convertView
                        .findViewById(R.id.detils_img_three);
                holder.img4 = (ImageView) convertView
                        .findViewById(R.id.detils_img_four);
                break;
            case 5:
                convertView = (LinearLayout) mInflater.inflate(
                        R.layout.sports_find_other_list_item_five, null);
                holder.img1 = (ImageView) convertView
                        .findViewById(R.id.detils_img_one);
                holder.img2 = (ImageView) convertView
                        .findViewById(R.id.detils_img_two);
                holder.img3 = (ImageView) convertView
                        .findViewById(R.id.detils_img_three);
                holder.img4 = (ImageView) convertView
                        .findViewById(R.id.detils_img_four);
                holder.img5 = (ImageView) convertView
                        .findViewById(R.id.detils_img_five);
                break;
            case 6:
                convertView = (LinearLayout) mInflater.inflate(
                        R.layout.sports_find_other_list_item_six, null);
                holder.img1 = (ImageView) convertView
                        .findViewById(R.id.detils_img_one);
                holder.img2 = (ImageView) convertView
                        .findViewById(R.id.detils_img_two);
                holder.img3 = (ImageView) convertView
                        .findViewById(R.id.detils_img_three);
                holder.img4 = (ImageView) convertView
                        .findViewById(R.id.detils_img_four);
                holder.img5 = (ImageView) convertView
                        .findViewById(R.id.detils_img_five);
                holder.img6 = (ImageView) convertView
                        .findViewById(R.id.detils_img_six);
                break;
            case 7:
                convertView = (LinearLayout) mInflater.inflate(
                        R.layout.sports_find_other_list_item_seven, null);
                holder.img1 = (ImageView) convertView
                        .findViewById(R.id.detils_img_one);
                holder.img2 = (ImageView) convertView
                        .findViewById(R.id.detils_img_two);
                holder.img3 = (ImageView) convertView
                        .findViewById(R.id.detils_img_three);
                holder.img4 = (ImageView) convertView
                        .findViewById(R.id.detils_img_four);
                holder.img5 = (ImageView) convertView
                        .findViewById(R.id.detils_img_five);
                holder.img6 = (ImageView) convertView
                        .findViewById(R.id.detils_img_six);
                holder.img7 = (ImageView) convertView
                        .findViewById(R.id.detils_img_seven);
                break;
            case 8:
                convertView = (LinearLayout) mInflater.inflate(
                        R.layout.sports_find_other_list_item_eight, null);
                holder.img1 = (ImageView) convertView
                        .findViewById(R.id.detils_img_one);
                holder.img2 = (ImageView) convertView
                        .findViewById(R.id.detils_img_two);
                holder.img3 = (ImageView) convertView
                        .findViewById(R.id.detils_img_three);
                holder.img4 = (ImageView) convertView
                        .findViewById(R.id.detils_img_four);
                holder.img5 = (ImageView) convertView
                        .findViewById(R.id.detils_img_five);
                holder.img6 = (ImageView) convertView
                        .findViewById(R.id.detils_img_six);
                holder.img7 = (ImageView) convertView
                        .findViewById(R.id.detils_img_seven);
                holder.img8 = (ImageView) convertView
                        .findViewById(R.id.detils_img_eight);
                break;
            case 9:
                convertView = (LinearLayout) mInflater.inflate(
                        R.layout.sports_find_other_list_item_nine, null);
                // 判断图片的是水平还是竖直从而设定此ImageView的长宽
                holder.img1 = (ImageView) convertView
                        .findViewById(R.id.detils_img_one);
                holder.img2 = (ImageView) convertView
                        .findViewById(R.id.detils_img_two);
                holder.img3 = (ImageView) convertView
                        .findViewById(R.id.detils_img_three);
                holder.img4 = (ImageView) convertView
                        .findViewById(R.id.detils_img_four);
                holder.img5 = (ImageView) convertView
                        .findViewById(R.id.detils_img_five);
                holder.img6 = (ImageView) convertView
                        .findViewById(R.id.detils_img_six);
                holder.img7 = (ImageView) convertView
                        .findViewById(R.id.detils_img_seven);
                holder.img8 = (ImageView) convertView
                        .findViewById(R.id.detils_img_eight);
                holder.img9 = (ImageView) convertView
                        .findViewById(R.id.detils_img_nine);
                break;
            default:
                break;
        }
        holder.otherImage = (RoundedImage) convertView
                .findViewById(R.id.image_icon1);
        holder.start_time = (TextView) convertView
                .findViewById(R.id.tx_start_times);
        holder.nametext = (TextView) convertView
                .findViewById(R.id.sports_find_othername1);
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) holder.nametext
                .getLayoutParams();
        lp.width = (int) (SportsApp.ScreenWidth * 0.7);
        holder.nametext.setLayoutParams(lp);
        holder.detils = (TextView) convertView.findViewById(R.id.tx_detils1);
        // 删除
        holder.deleteText = (TextView) convertView
                .findViewById(R.id.tx_delete_find);
        // 赞和评论
        holder.goodandtext = (ImageView) convertView
                .findViewById(R.id.good_and_text);
        // commentLayout
        holder.commentLayout = (LinearLayout) convertView
                .findViewById(R.id.good_text_detils_layout);
        // 赞人数
        holder.goodLayout = (LinearLayout) convertView
                .findViewById(R.id.find_good_num_layout);
        holder.goodPeopole = (TextView) convertView
                .findViewById(R.id.good_people);
        // 线
        holder.textgoodline = (View) convertView
                .findViewById(R.id.text_good_bgline);
        // 评论人数
        holder.textLayout = (LinearLayout) convertView
                .findViewById(R.id.find_text_num_layout);
        holder.textPeopole = (TextView) convertView
                .findViewById(R.id.text_people);
        holder.talkdetilsLayout = (LinearLayout) convertView
                .findViewById(R.id.layout_talk_text);
        convertView.setTag(holder);

        switch (type) {
            case 1:
                int height = item.getHeight();
                int width = item.getWidth();
                LinearLayout.LayoutParams lp1;
                if (width >= height) {
                    Log.e("---", "此图形是横着的---------");
                    lp1 = (LinearLayout.LayoutParams) holder.img1.getLayoutParams();
                    int imgWidth = (int) (SportsApp.ScreenWidth * 0.5);
                    lp1.height = (int) ((imgWidth * height) / width);
                    lp1.width = imgWidth;
                    holder.img1.setLayoutParams(lp1);

                } else if (height > width) {
                    Log.e("---", "此图形是竖直的---------");
                    lp1 = (LinearLayout.LayoutParams) holder.img1.getLayoutParams();
                    int imgHeight = (int) (SportsApp.ScreenHeight * 0.33);
                    lp1.height = imgHeight;
                    lp1.width = (int) ((imgHeight * width) / height);
                    holder.img1.setLayoutParams(lp1);
                }
                mDownloader2.download(urls[0], holder.img1, null);
                break;
            case 2:
                imgList2.clear();
                imgList2.add(holder.img1);
                imgList2.add(holder.img2);
                setImageVoid(type, urls, imgList2);
                break;
            case 3:
                imgList3.clear();
                imgList3.add(holder.img1);
                imgList3.add(holder.img2);
                imgList3.add(holder.img3);
                setImageVoid(type, urls, imgList3);
                break;
            case 4:
                imgList4.clear();
                imgList4.add(holder.img1);
                imgList4.add(holder.img2);
                imgList4.add(holder.img3);
                imgList4.add(holder.img4);
                setImageVoid(type, urls, imgList4);
            /*
			 * for (int i = 0; i < type; i++) { LinearLayout.LayoutParams
			 * lps=(LinearLayout.LayoutParams)imgList4.get(i).getLayoutParams();
			 * lps.width=(int)(SportsApp.ScreenWidth * 0.625)/3;
			 * lps.height=(int)(SportsApp.ScreenWidth * 0.625)/3;
			 * imgList4.get(i).setLayoutParams(lps);
			 * mImageWorker.loadImage(urls[i], imgList4.get(i), null, null,
			 * false); }
			 */
                break;
            case 5:
                imgList5.clear();
                imgList5.add(holder.img1);
                imgList5.add(holder.img2);
                imgList5.add(holder.img3);
                imgList5.add(holder.img4);
                imgList5.add(holder.img5);
                setImageVoid(type, urls, imgList5);
                break;
            case 6:
                imgList6.clear();
                imgList6.add(holder.img1);
                imgList6.add(holder.img2);
                imgList6.add(holder.img3);
                imgList6.add(holder.img4);
                imgList6.add(holder.img5);
                imgList6.add(holder.img6);
                setImageVoid(type, urls, imgList6);
			/*
			 * for (int i = 0; i < type; i++) { LinearLayout.LayoutParams
			 * lps=(LinearLayout.LayoutParams)imgList6.get(i).getLayoutParams();
			 * lps.width=(int)(SportsApp.ScreenWidth * 0.625)/3;
			 * lps.height=(int)(SportsApp.ScreenWidth * 0.625)/3;
			 * imgList6.get(i).setLayoutParams(lps);
			 * mImageWorker.loadImage(urls[i], imgList6.get(i), null, null,
			 * false); }
			 */
                break;
            case 7:
                imgList7.clear();
                imgList7.add(holder.img1);
                imgList7.add(holder.img2);
                imgList7.add(holder.img3);
                imgList7.add(holder.img4);
                imgList7.add(holder.img5);
                imgList7.add(holder.img6);
                imgList7.add(holder.img7);
                setImageVoid(type, urls, imgList7);
			/*
			 * for (int i = 0; i < type; i++) { LinearLayout.LayoutParams
			 * lps=(LinearLayout.LayoutParams)imgList7.get(i).getLayoutParams();
			 * lps.width=(int)(SportsApp.ScreenWidth * 0.625)/3;
			 * lps.height=(int)(SportsApp.ScreenWidth * 0.625)/3;
			 * imgList7.get(i).setLayoutParams(lps);
			 * mImageWorker.loadImage(urls[i], imgList7.get(i), null, null,
			 * false); }
			 */
                break;
            case 8:
                imgList8.clear();
                imgList8.add(holder.img1);
                imgList8.add(holder.img2);
                imgList8.add(holder.img3);
                imgList8.add(holder.img4);
                imgList8.add(holder.img5);
                imgList8.add(holder.img6);
                imgList8.add(holder.img7);
                imgList8.add(holder.img8);
                setImageVoid(type, urls, imgList8);
			/*
			 * for (int i = 0; i < type; i++) { LinearLayout.LayoutParams
			 * lps=(LinearLayout.LayoutParams)imgList8.get(i).getLayoutParams();
			 * lps.width=(int)(SportsApp.ScreenWidth * 0.625)/3;
			 * lps.height=(int)(SportsApp.ScreenWidth * 0.625)/3;
			 * imgList8.get(i).setLayoutParams(lps);
			 * mImageWorker.loadImage(urls[i], imgList8.get(i), null, null,
			 * false); }
			 */
                break;
            case 9:
                imgList9.clear();
                imgList9.add(holder.img1);
                imgList9.add(holder.img2);
                imgList9.add(holder.img3);
                imgList9.add(holder.img4);
                imgList9.add(holder.img5);
                imgList9.add(holder.img6);
                imgList9.add(holder.img7);
                imgList9.add(holder.img8);
                imgList9.add(holder.img9);
                setImageVoid(type, urls, imgList9);
			/*
			 * for (int i = 0; i < type; i++) { LinearLayout.LayoutParams
			 * lps=(LinearLayout.LayoutParams)imgList9.get(i).getLayoutParams();
			 * lps.width=(int)(SportsApp.ScreenWidth * 0.625)/3;
			 * lps.height=(int)(SportsApp.ScreenWidth * 0.625)/3;
			 * imgList9.get(i).setLayoutParams(lps);
			 * imgList9.get(i).setBackgroundDrawable(null);
			 * mImageWorker.loadImage(urls[i], imgList9.get(i), null, null,
			 * false); }
			 */
                break;
		/*
		 * for (int i = 0; i < type; i++) { LinearLayout.LayoutParams
		 * lps=(LinearLayout
		 * .LayoutParams)imgList.get(type-2).get(i).getLayoutParams();
		 * //lps.width=mItemHeight; //lps.height=mItemHeight;
		 * lps.width=(int)(SportsApp.ScreenWidth * 0.625)/3;
		 * lps.height=(int)(SportsApp.ScreenWidth * 0.625)/3;
		 * imgList.get(type-2).get(i).setLayoutParams(lps);
		 * mImageWorker.loadImage(urls[i], imgList.get(type-2).get(i), null,
		 * null, false); //imgList2.get(i).setOnClickListener(new
		 * imgOnClickListener(i+1,mList.get(position).getId())); } break;
		 */

            default:
                break;
        }

        // 这是判断什么时候发的
        long time = System.currentTimeMillis() - item.getTimes() * 1000;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        if (time <= 60 * 1000)
            // 一分钟内显示刚刚
            holder.start_time.setText(this.getResources().getString(
                    R.string.sports_time_justnow));
        else if (time <= 60 * 60 * 1000) {
            int h = (int) (time / 1000 / 60);
            // 一小时内显示多少分钟前
            holder.start_time.setText(""
                    + h
                    + this.getResources().getString(
                    R.string.sports_time_mins_ago));
        } else {
            holder.start_time.setText(format.format(item.getTimes() * 1000));
        }
        holder.otherImage.setImageDrawable(null);
        if ("man".equals(item.getSex())) {
            holder.otherImage
                    .setBackgroundResource(R.drawable.sports_user_edit_portrait_male);
        } else if ("woman".equals(item.getSex())) {
            holder.otherImage
                    .setBackgroundResource(R.drawable.sports_user_edit_portrait);
        }
        mDownloader.download(item.getOtherimg(), holder.otherImage, null);
        // holder.otherImage
        // .setOnClickListener(new personalInformationOnClickListener(item
        // .getOtheruid()));
        // 显示名字
        holder.nametext.setText(item.getOthername());
        // holder.nametext
        // .setOnClickListener(new personalInformationOnClickListener(item
        // .getOtheruid()));

        // 判断该条发现是否是登录人发的或者是否是管理员身份
        if (detail.getUname().equals(item.getOthername()) || mSportsApp.mIsAdmin) {
            holder.deleteText.setVisibility(View.VISIBLE);
            holder.deleteText.setOnClickListener(new deleteListener(item
                    .getFindId()));
        } else {
            holder.deleteText.setVisibility(View.GONE);
        }

        titlePopup.setItemOnClickListener(this);
        holder.goodandtext.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {
                // TODO Auto-generated method stub
                // 点赞和评论
                // 此处应该改为CommentDetailAcitivy自己的handler，而不能用FindOtherFragment的Hanlder
//				Handler findHandler = mSportsApp.getFindHandler();
//				if (findHandler != null) {
//					findHandler.sendMessage(findHandler
//							.obtainMessage(FindOtherFragment.HIDE_EDIT));
//				}
                hideedit();

                titlePopup.setAnimationStyle(R.style.cricleBottomAnimation);
                titlePopup.show(view, 0, commentItem.getFindId(),
                        commentItem.isGood() ? mContext.getResources()
                                .getString(R.string.sports_cancel) : mContext
                                .getResources().getString(R.string.sports_good));
            }
        });

        holder.detils.setText(item.getDetils());
        // 如果评论人数大于0或者点赞人数大于0
        if (item.getGoodpeople() <= 0 && item.getCommentCount() <= 0) {
            holder.commentLayout.setVisibility(View.GONE);
        }
        // if
        // (mList.get(position).getGoodpeople()>0||mList.get(position).getCommentCount()>0)
        // {
        else {
			/*
			 * if (holder.textgoodline.getVisibility()==View.GONE) {
			 * holder.textgoodline.setVisibility(View.VISIBLE); }
			 */
            holder.commentLayout.setVisibility(View.VISIBLE);
            if (item.getGoodpeople() > 0) {
                holder.goodLayout.setVisibility(View.VISIBLE);
                holder.goodPeopole.setText(item.getGoodpeople() + "");
            } else {
                holder.goodLayout.setVisibility(View.GONE);
            }
            if (item.getCommentCount() > 0) {
                holder.textLayout.setVisibility(View.VISIBLE);
                holder.textPeopole.setText(item.getCommentCount() + "");
                // 将评论内容显示出来
                for (int i = 0; i < item.getTalkdetils().size(); i++) {

                    createCommentItemView(item.getTalkdetils().get(i));

                    newView.setOnClickListener(new commentOnClickListener(i,
                            commentItem.getFindId(), commentItem
                            .getTalkdetils().get(i).getCommentId(),
                            theFirstName));

                    holder.talkdetilsLayout.addView(newView);
                }
            } else {
                holder.textLayout.setVisibility(View.GONE);
            }
        }
        return convertView;
    }

    //监听删除整个item
    class deleteListener implements OnClickListener {
        //发现ID
        private String find_id;
        //list列表中位置
        private int position;

        public deleteListener(String numlist) {
            this.find_id = numlist;
            //this.position=position;
        }

        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub
            showdeleteAllDialogs(find_id);
        }

    }

    //确认是否删除整个item
    private void showdeleteAllDialogs(String find_id) {
        final String findID = find_id;

        //final int list_position =position;
        alertDialog = new Dialog(mContext, R.style.sports_dialog);
        LayoutInflater sInflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = sInflater.inflate(R.layout.sports_dialog, null);
        v.findViewById(R.id.bt_ok).setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.sports_slim_btn_click));
        Button ok = (Button) v.findViewById(R.id.bt_ok);
        ok.setText(mContext.getResources().getString(R.string.button_ok));
        ok.findViewById(R.id.bt_ok).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                alertDialog.dismiss();
                //有没有网
                if (mSportsApp.isOpenNetwork()) {
                    new AsyncTask<Void, Void, ApiBack>() {
                        @Override
                        protected ApiBack doInBackground(Void... params) {
                            // TODO Auto-generated method stub
                            ApiBack back = null;
                            try {
                                back = (ApiBack) ApiJsonParser.delFind(mSportsApp.getSessionId(), findID);
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
                                        mContext,
                                        mContext.getResources().getString(
                                                R.string.sports_delete_failed),
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                //删除成功
					/*				FindOtherFragment.mList.remove(list_position);
									notifyDataSetChanged();*/
                                CommentsDetailActivity.this.finish();
                                CommentsDetailActivity.act = 100;
                                Toast.makeText(
                                        mContext,
                                        mContext.getResources().getString(
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
        v.findViewById(R.id.bt_cancel).setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.sports_login_btn_click));
        v.findViewById(R.id.bt_cancel).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        TextView title = (TextView) v.findViewById(R.id.title);
        title.setTextColor(mContext.getResources().getColor(R.color.text_login));
        title.setText(mContext.getResources().getString(R.string.confirm_exit_title));
        TextView message = (TextView) v.findViewById(R.id.message);
        message.setText(mContext.getResources().getString(R.string.make_sure_will_delete));
        v.setMinimumWidth((int) (SportsApp.ScreenWidth * 0.8));
        alertDialog.setCancelable(true);
        alertDialog.setContentView(v);
        alertDialog.show();
    }

    //从每一个findcomment对象创建一个view，然后将这个添加到评论列表的线性布局当中
    public View createCommentItemView(FindComment commentItem) {
        theFirstName = commentItem.getFirstName();
        theSecondName = commentItem.getSecondName();
        thewavComment = commentItem.getCommentWav();
        thewavDuration = commentItem.getWavDuration();
        theTalkDetils = commentItem.getCommentText();
        Log.e(TAG, "theSecond--------------------" + theSecondName);
        // 如果是语音回复
        int firstname, secondname = 0;
        if (thewavComment != null && thewavDuration != null
                && !thewavDuration.equals("null")
                && !thewavDuration.equals("0")) {
            newView = mInflater.inflate(
                    R.layout.sports_find_talk_wav_detiles, null);
            TextView nametoName = (TextView) newView
                    .findViewById(R.id.find_talk_detils_text_name);
            LinearLayout recordlayout = (LinearLayout) newView
                    .findViewById(R.id.recoding_click_find);
            TextView durationtext = (TextView) newView
                    .findViewById(R.id.wav_durations_find);
            ImageView beginWav = (ImageView) newView
                    .findViewById(R.id.wav_begin_find);
            // if
            // (theSecondName.equals(mList.get(position).getOthername()))
            // {
            if (theSecondName == null || theSecondName.equals("")
                    || theSecondName.equals("null")) {
                // newMessageString="<font color=\"#25a7f2\">"+theFirstName+":"+"</font>";
                newMessageString = theFirstName + ":";
                numcom = 1;
                firstname = theFirstName.length();
            } else {
				/*
				 * newMessageString="<font color=\"#25a7f2\">"+
				 * theFirstName+"</font><font color=\"#3a3f47\">"
				 * +mContext.getResources().getString(R.string.
				 * multi_comment_tip_target)
				 * +"</font><font color=\"#25a7f2\">"
				 * +theSecondName+":"+"</font>";
				 */
                newMessageString = theFirstName
                        + mContext.getResources().getString(
                        R.string.multi_comment_tip_target)
                        + theSecondName + ":";
                numcom = 2;
                firstname = theFirstName.length();
                secondname = theSecondName.length();
            }
            durationtext.setText("" + thewavDuration + "″");
            // nametoName.setText(Html.fromHtml(newMessageString));
            String ssString = ToDBC(newMessageString);
            List<SpannableString> list = getExpressionString(
                    mContext, ssString, numcom, firstname,
                    secondname);
            for (SpannableString span : list) {
                nametoName.setText(span);
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
                        if (mediaPath.endsWith("null") || mediaPath == null) {
                            return;
                        }
                        //currentDuration1 = 0;
                        new Thread() {
                            @Override
                            public void run() {
                                if (wavBeginOne != wavBegin && mPlayer != null) {
                                    mHandler.sendMessage(mHandler.obtainMessage(RECORD_PAUSE, wavBeginOne));
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
                                        mPlayer = mRecorder.startPlaybackNet(mContext, mediaPath);
                                    }
                                    if (mPlayer == null) {
                                        Log.e(TAG, "录音播放---3");
                                        Log.d(TAG, "if (mPlayer == null)");
                                        // mHandler.sendMessage(mHandler.obtainMessage(RECORD_PAUSE,
                                        // wavBegin));
                                        mPlayer = mRecorder.startPlaybackNet(mContext, mediaPath);
                                        mHandler.sendMessage(mHandler.obtainMessage(RECORD_LOADING, wavBegin));
                                        // return;
                                    }
                                    mPlayer.setOnErrorListener(new OnErrorListener() {
                                        @Override
                                        public boolean onError(MediaPlayer arg0, int arg1, int arg2) {
                                            Log.e(TAG, "录音播放---4");
                                            isStart = true;
                                            currentDuration = 0;
                                            mRecorder.stopPlayback();
                                            mHandler.sendMessage(mHandler.obtainMessage(RECORD_ERROR, wavBegin));
                                            return true;
                                        }
                                    });
                                    mPlayer.setOnCompletionListener(new OnCompletionListener() {
                                        @Override
                                        public void onCompletion(MediaPlayer arg0) {
                                            Log.e(TAG, "录音播放---5");
                                            isStart = true;
                                            isPause = false;
                                            Log.e("hjtest", "onCompletion");
                                            mHandler.sendMessage(mHandler.obtainMessage(RECORD_FINISH, wavBegin));
                                        }
                                    });
                                    mPlayer.setOnPreparedListener(new OnPreparedListener() {
                                        @Override
                                        public void onPrepared(MediaPlayer arg0) {
                                            Log.e(TAG, "录音播放---6");
                                            Log.e("hjtest", "onPrepared");
                                            if (mPlayer == null) {
                                                mHandler.sendMessage(mHandler.obtainMessage(RECORD_PAUSE, wavBegin));
                                                return;
                                            }
                                            mHandler.sendMessage(mHandler.obtainMessage(RECORD_PREPARED, wavBegin));
                                        }
                                    });
                                    try {
                                        if (!isPause) {
                                            Log.d(TAG, "not isPause");
                                            Log.e(TAG, "录音播放---7");
											/*mPlayer.setOnPreparedListener(new OnPreparedListener() {

												@Override
												public void onPrepared(MediaPlayer mPlayer) {
													// TODO Auto-generated method stub
													mPlayer.start();
												}
											});
											mPlayer.prepareAsync();*/
                                            mPlayer.prepare();
                                            mPlayer.start();

                                        } else {
                                            Log.d(TAG, "isPause");
                                            Log.e(TAG, "录音播放---8");
                                            mPlayer.start();
                                            mHandler.sendMessage(mHandler.obtainMessage(RECORD_PREPARED, wavBegin));
                                        }
                                    } catch (IllegalStateException e) {
                                        Log.e(TAG, "录音播放---9");
                                        mPlayer = null;
                                        isStart = true;
                                        currentDuration = 0;
                                        mHandler.sendMessage(mHandler.obtainMessage(RECORD_PAUSE, wavBegin));
                                    } catch (Exception e) {
                                        Log.e(TAG, "录音播放---10");
                                        mPlayer = null;
                                        isStart = true;
                                        currentDuration = 0;
                                        mHandler.sendMessage(mHandler.obtainMessage(RECORD_PAUSE, wavBegin));
                                    }
                                    if (currentDuration > 0 || isPause) {
                                        Log.e(TAG, "录音播放---11");
                                        isPause = false;
                                        if (mPlayer != null) {
                                            Log.e(TAG, "录音播放---12");
                                            mPlayer.start();
                                        } else {
                                            Log.e(TAG, "录音播放---13");
                                            mPlayer = mRecorder.startPlaybackNet(mContext, mediaPath);
                                            isPause = false;
                                            mHandler.sendMessage(mHandler.obtainMessage(RECORD_FINISH, wavBegin));
                                        }
                                        currentDuration = 0;
                                    }
                                } else if (!isStart) {
                                    Log.e(TAG, "录音播放---14");
                                    Log.d(TAG, "else if (!isStart)");
                                    if (mPlayer != null) {
                                        Log.e(TAG, "录音播放---15");
                                        Log.d(TAG, "if (mPlayer != null)");
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
                                            mHandler.sendMessage(mHandler.obtainMessage(RECORD_PAUSE, wavBegin));
                                        }
                                    }
                                    mHandler.sendMessage(mHandler.obtainMessage(RECORD_PAUSE, wavBegin));
                                }
                            }
                        }.start();
                    }
                }
            });

        } else {
            newView = mInflater.inflate(
                    R.layout.sports_find_talk_detils, null);
            TextView sTextView = (TextView) newView
                    .findViewById(R.id.find_talk_detils_text);
            // 让每个表情前加个空格
            String stringoneString = theTalkDetils.replaceAll(
                    "\\[", " [");
            if (theSecondName == null || theSecondName.equals("")
                    || theSecondName.equals("null")) {
				/*
				 * newMessageString="<font color=\"#25a7f2\">"+
				 * theFirstName
				 * +":"+"</font><font color=\"#3a3f47\">"
				 * +" "+theTalkDetils+"</font>";
				 */

                newMessageString = theFirstName + ":"
                        + stringoneString;
                numcom = 1;
                firstname = theFirstName.length();
            } else {
				/*
				 * newMessageString="<font color=\"#25a7f2\">"+
				 * theFirstName+"</font><font color=\"#3a3f47\">"
				 * +mContext.getResources().getString(R.string.
				 * multi_comment_tip_target)
				 * +"</font><font color=\"#25a7f2\">"
				 * +theSecondName+":"
				 * +"</font><font color=\"#3a3f47\">"
				 * +" "+theTalkDetils+"</font>";
				 */
                newMessageString = theFirstName
                        + mContext.getResources().getString(
                        R.string.multi_comment_tip_target)
                        + theSecondName + ":" + stringoneString;
                numcom = 2;
                firstname = theFirstName.length();
                secondname = theSecondName.length();
            }

            // Log.e(TAG, "长度0--6-----"+newMessageString.length());
            List<SpannableString> list = getExpressionString(
                    mContext, newMessageString, numcom, firstname,
                    secondname);
            for (SpannableString span : list) {
                sTextView.setText(span);
                // Log.e(TAG, "长度2-------"+span.length());
            }
            sTextView.invalidate();

        }

        return newView;
    }

    public void send(final int position, final String theFirstName, final String findID, final String toID,
                     final String commentText, final String commentWav, final String wavDuration) {
        /**
         * 第一个参数是评论所属发现在list中的位置
         * 第二个参数用来当回复的时候显示回复给谁，如果直接评论则null
         * 第三个发现ID
         * 第四个用来当回复的时候传递to_id,如果直接评论则为null
         * 其余三个分别是文本内容，音频，音频时间
         * */
//		showWaitDialog(mContext.getResources().getString(R.string.comment_wait));
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                // TODO Auto-generated method stub
                String commentIdString = null;
                try {
                    commentIdString = ApiJsonParser.addComment(mSportsApp.getSessionId(),
                            findID, commentText, commentWav, wavDuration, toID);
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
                    Toast.makeText(
                            mContext,
                            mContext.getResources().getString(
                                    R.string.upload_failed),
                            Toast.LENGTH_SHORT).show();
                } else {
                    //上传成功
                    FindComment fc = new FindComment();
                    fc.setCommentId(commentId);
                    fc.setFirstName(detail.getUname());
                    if (theFirstName != null && !theFirstName.equals("")) {
                        fc.setSecondName(theFirstName);
                    }
                    fc.setCommentText(commentText);
                    fc.setCommentWav(commentWav);
                    fc.setWavDuration(wavDuration);
					/*fc.setCommentWav("http://mp3.9ku.com/file2/177/176318.mp3");
					fc.setWavDuration("60");*/
                    //Log.e(TAG, "长度1-------"+commentText.length());
                    if (commentItem.getTalkdetils() == null) {
                        ArrayList<FindComment> fList = new ArrayList<FindComment>();
                        commentItem.setTalkdetils(fList);
                    }
                    commentItem.getTalkdetils().add(fc);
                    int beforeCommentNum = commentItem.getCommentCount();
                    commentItem.setCommentCount(beforeCommentNum + 1);
                    //notifyDataSetChanged();

                    createCommentItemView(fc);
                    newView.setOnClickListener(new commentOnClickListener(0,
                            commentItem.getFindId(), fc.getCommentId(),
                            theFirstName));
                    holder.talkdetilsLayout.addView(newView);

                    Toast.makeText(
                            mContext,
                            mContext.getResources().getString(
                                    R.string.upload_success),
                            Toast.LENGTH_SHORT).show();
                }
            }

        }.execute();
    }

    // 监听评论
    class commentOnClickListener implements OnClickListener {
        // 评论所属发现所在list中的位置
        private int position;
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
                showDialogs(position, comment_number, commentID);

            } else {
                /**
                 * (position,findIdString,2,commentID,theFirstName)
                 * 第一个参数所属发现在list中位置 第二个参数发现ID， 第三个参数用来判断评论还是回复1代表直接评论2代表回复
                 * 第四个参数用来当回复的时候传递to_id,如果直接评论则为null 第五个参数用来当回复的时候显示回复给谁，如果直接评论则null
                 */
                if (mSportsApp.mIsAdmin) {
                    showAdminDialogs(comment_number, commentID, findID,
                            theFirstName);
                } else {
                    changeCommentListener.OnCheckedChangeListener(findID, 2,
                            commentID, theFirstName);
                }
            }

        }
    }

    //确认是否删除自己发的评论
    private void showDialogs(int list_position, int comment_number, String commentid) {
        final int position = list_position;
        final int number = comment_number;
        final String commentID = commentid;
        alertDialog = new Dialog(mContext, R.style.sports_dialog);
        LayoutInflater sInflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = sInflater.inflate(R.layout.sports_dialog, null);
        v.findViewById(R.id.bt_ok).setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.sports_slim_btn_click));
        Button ok = (Button) v.findViewById(R.id.bt_ok);
        ok.setText(mContext.getResources().getString(R.string.button_ok));
        ok.findViewById(R.id.bt_ok).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (mSportsApp.isOpenNetwork()) {
                    alertDialog.dismiss();
                    new AsyncTask<Void, Void, ApiBack>() {
                        @Override
                        protected ApiBack doInBackground(Void... params) {
                            // TODO Auto-generated method stub
                            ApiBack back = null;
                            try {
                                back = (ApiBack) ApiJsonParser.delComment(mSportsApp.getSessionId(), commentID);
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
                                        mContext,
                                        mContext.getResources().getString(
                                                R.string.sports_delete_failed),
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                //删除成功
                                commentItem.getTalkdetils().remove(number);
                                int beforeCommentNum = commentItem.getCommentCount();
                                commentItem.setCommentCount(beforeCommentNum - 1);
                                holder.talkdetilsLayout.removeViewAt(number);


                                Toast.makeText(
                                        mContext,
                                        mContext.getResources().getString(
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
        v.findViewById(R.id.bt_cancel).setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.sports_login_btn_click));
        v.findViewById(R.id.bt_cancel).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        TextView title = (TextView) v.findViewById(R.id.title);
        title.setTextColor(mContext.getResources().getColor(R.color.text_login));
        title.setText(mContext.getResources().getString(R.string.confirm_exit_title));
        TextView message = (TextView) v.findViewById(R.id.message);
        message.setText(mContext.getResources().getString(R.string.make_sure_will_delete));
        v.setMinimumWidth((int) (SportsApp.ScreenWidth * 0.8));
        alertDialog.setCancelable(true);
        alertDialog.setContentView(v);
        alertDialog.show();
    }

    // 管理员选择回复还是删除
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
                            // showWaitDialog(mContext.getResources().getString(R.string.sports_dialog_deleteing));
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
                                        // waitProgressDialog.dismiss();
                                        Toast.makeText(
                                                mContext,
                                                mContext.getResources()
                                                        .getString(
                                                                R.string.sports_delete_failed),
                                                Toast.LENGTH_SHORT).show();
                                    } else {
                                        // 删除成功
                                        // waitProgressDialog.dismiss();
                                        commentItem.getTalkdetils().remove(
                                                number);
                                        int beforeCommentNum = commentItem
                                                .getCommentCount();
                                        commentItem
                                                .setCommentCount(beforeCommentNum - 1);
                                        // otifyDataSetChanged();
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
                        changeCommentListener.OnCheckedChangeListener(findID,
                                2, commentID, theFirstName);
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

    public void initExpressionViews() {
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
            }
        }
    }

    @Override
    public void onItemClick(int number, final int list_position,
                            final String findIdString) {
        final int theBeforeGoodPeople = commentItem.getGoodpeople();
        switch (number) {
            case 0:// 点赞
                if (mSportsApp.isOpenNetwork()) {
                    new AsyncTask<Void, Void, ApiBack>() {
                        @Override
                        protected void onPreExecute() {
                        }

                        @Override
                        protected ApiBack doInBackground(Void... params) {
                            ApiBack back = null;
                            try {
                                back = (ApiBack) ApiJsonParser.likeFind(
                                        mSportsApp.getSessionId(), findIdString);
                            } catch (ApiNetException e) {
                                e.printStackTrace();
                            }
                            return back;
                        }

                        @Override
                        protected void onPostExecute(ApiBack result) {
                            super.onPostExecute(result);
                            if (result == null || result.getFlag() != 0) {
                                Toast.makeText(
                                        mContext,
                                        mContext.getResources().getString(
                                                R.string.sports_findgood_error),
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                // 成功
                                if (commentItem.isGood()) {
                                    commentItem.setGood(false);
                                    commentItem
                                            .setGoodpeople(theBeforeGoodPeople - 1);
                                    Toast.makeText(
                                            mContext,
                                            mContext.getResources().getString(
                                                    R.string.praise_cancel_success),
                                            Toast.LENGTH_SHORT).show();
                                } else {
                                    commentItem.setGood(true);
                                    commentItem
                                            .setGoodpeople(theBeforeGoodPeople + 1);
                                    Toast.makeText(
                                            mContext,
                                            mContext.getResources().getString(
                                                    R.string.praise_successed),
                                            Toast.LENGTH_SHORT).show();
                                }
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
            case 1:// 点击评论
			/*
			 * Toast.makeText(mContext, "评论", Toast.LENGTH_LONG).show();
			 */
                if (changeCommentListener != null)
                    changeCommentListener.OnCheckedChangeListener(findIdString, 1,
                            null, null);
                Log.i("", "changeCommentListener" + changeCommentListener);
                // 这里是直接评论
                /**
                 * (list_position,findIdString,1,null,null) 第二个参数发现ID，
                 * 第三个参数用来判断评论还是回复1代表直接评论2代表回复 第四个参数用来当回复的时候传递to_id,如果直接评论则为null
                 * 第五个参数用来当回复的时候显示回复给谁，如果直接评论则为null
                 */
                break;
            default:
                break;
        }

    }

    public interface OnCheckedChangeCommentListener {
        public void OnCheckedChangeListener(String findId, int number,
                                            String to_id, String toName);
    }

    private OnCheckedChangeCommentListener changeCommentListener;

    public void setOnCheckedChangeListener(
            OnCheckedChangeCommentListener changeCommentListener) {
        this.changeCommentListener = changeCommentListener;
    }


    public OnCheckedChangeCommentListener checkListerner = new OnCheckedChangeCommentListener() {

        @Override
        public void OnCheckedChangeListener(String findId, int number,
                                            String to_id, String toName) {

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
                    findUpcommentEdittext.setHint(mContext.getResources()
                            .getString(R.string.sports_private_msg_text_hint));
                    findLayout.setVisibility(View.VISIBLE);
                }
                if (number == 2) {
                    // 回复XX
                    findUpcommentEdittext.setHint(mContext.getResources()
                            .getString(R.string.multi_comment_tip_target)
                            + "  :" + toName);
                    findLayout.setVisibility(View.VISIBLE);
                }
            } else {
                findLayout.setVisibility(View.GONE);
                hideedit();
                findBool = true;
            }
        }
    };

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
                ApplicationInfo appInfo = mContext.getApplicationInfo();
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
            GridView appPage = new GridView(mContext);
            appPage.setAdapter(new ExpressionImgAdapter(mContext,
                    imgItems, i));
            appPage.setNumColumns(7);
            appPage.setHorizontalSpacing(10);
            appPage.setVerticalSpacing(10);
            appPage.setSelector(new ColorDrawable(Color.TRANSPARENT));
            appPage.setOnItemClickListener(listener);
            scrollLayout.addView(appPage);

            ImageView img = new ImageView(mContext);
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


    public void initView() {
        findLayout = (RelativeLayout) this.findViewById(R.id.comment_detail_find_bottom);
        findLayout.setVisibility(View.GONE);
        layoutVoice = (LinearLayout) this.findViewById(R.id.layoutVoice);
        mRecorder = new RecordHelper();
        findPressBtn = (Button) this.findViewById(R.id.find_press_btn);
        findPressBtn.setOnLongClickListener(new findClickListener());
        findPressBtn.setOnTouchListener(new findClickListener());
        findTextBtn = (ImageButton) this.findViewById(R.id.find_text_btn);
        findTextBtn.setOnClickListener(new findClickListener());

        findExpressBtn = (ImageButton) this
                .findViewById((R.id.find_expression_text_btn));

        findExpressBtn.setOnClickListener(new findClickListener());

        rScrollLayout = (RelativeLayout) this.findViewById(R.id.rScrollLayout);
        scrollLayout = (ScrollLayout) this.findViewById(
                R.id.ScrollLayoutTest);
        imgLayout = (LinearLayout) this.findViewById(R.id.imageLayot);

        scrollLayout.SetOnViewChangeListener(new OnViewChangeListener() {

            @Override
            public void OnViewChange(int view) {

                setCurPoint(view);
            }
        });
        initViews();
        mCurSel = 0;
        isShow = false;
        ImageView img = (ImageView) imgLayout.getChildAt(mCurSel);
        img.setBackgroundResource(R.drawable.qita_biaoqing_04);

        findUpcommentText = (LinearLayout) this
                .findViewById(R.id.find_upcomment_text);
        findUpcommentText.setVisibility(View.GONE);
        findUpcommentSend = (Button) this
                .findViewById(R.id.find_upcomment_send);
        findUpcommentSend.setOnClickListener(new findClickListener());
        findUnavailable = (Button) this.findViewById(R.id.find_unavailable);
        findUpcommentEdittext = (EditText) this
                .findViewById(R.id.find_upcomment_edittext);
        findUpcommentEdittext.requestFocus();
        findUpcommentEdittext.setOnClickListener(new findClickListener());
        findUpcommentEdittext.addTextChangedListener(mTextWatcher);
        findUpcommentEdittext
                .setOnFocusChangeListener(new OnFocusChangeListener() {

                    @Override
                    public void onFocusChange(View arg0, boolean arg1) {
                        // TODO Auto-generated method stub
                        // if (isShow == true) {
                        // isShow = false;
                        // rScrollLayout.setVisibility(View.GONE);
                        // }
                    }
                });
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
                Toast.makeText(
                        mContext,
                        mContext.getResources().getString(
                                R.string.does_not_this_input),
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

    private Dialog dialog;

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
                                Toast.makeText(
                                        mContext,
                                        getResources().getString(
                                                R.string.sports_record_fail),
                                        Toast.LENGTH_SHORT).show();
                                break;
                            }

                            Log.e(TAG, "音频-文字---------"
                                    + findUpcommentEdittext.getText().toString());
                            Log.e(TAG, "音频-文件---------" + "/sdcard/Recording/"
                                    + (RecordHelper.mSampleFile).getName());
                            Log.e(TAG, "音频-长度---------"
                                    + RecordHelper.mSampleLength + "");

                            send(0, toNameStr,
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
            if (v == findPressBtn) {
                mRecorder.startRecording(MediaRecorder.OutputFormat.DEFAULT,
                        ".mp3", mContext);
                dialog = new Dialog(mContext, R.style.share_dialog2);
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
                     * 第一个参数是评论所属发现在list中的位置 第二个参数用来当回复的时候显示回复给谁，如果直接评论则null 第三个发现ID
                     * 第四个用来当回复的时候传递to_id,如果直接评论则为null 其余三个分别是文本内容，音频，音频时间
                     * */
                    findLayout.setVisibility(View.GONE);
                    //发送评论
                    send(0, toNameStr, find_Id,
                            to_Id, findUpcommentEdittext.getText().toString(),
                            null, null);

                    findUpcommentEdittext.setText("");
                    findLayout.setVisibility(View.GONE);
                    break;
                default:
                    break;
            }
        }

    }

    // 显示软键盘
    private void showedit() {
        findUpcommentEdittext.setFocusable(true);
        findUpcommentEdittext.setFocusableInTouchMode(true);
        findUpcommentEdittext.requestFocus();
        InputMethodManager inputManager = (InputMethodManager) findUpcommentEdittext
                .getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.showSoftInput(findUpcommentEdittext, 0);
    }

    // 等UI绘制好弹出软键盘
    private void showUIedit() {
        findUpcommentEdittext.setFocusable(true);
        findUpcommentEdittext.setFocusableInTouchMode(true);
        findUpcommentEdittext.requestFocus();
        Timer timer = new Timer();
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
        // findUpcommentEdittext.setText("");
        findUpcommentEdittext.clearFocus();
        // close InputMethodManager
        InputMethodManager imm = (InputMethodManager) this
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(findUpcommentEdittext.getWindowToken(), 0);
    }

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
                    Bitmap bitmap = BitmapFactory.decodeResource(mContext
                            .getResources(), d);
                    DisplayMetrics mDisplayMetrics = new DisplayMetrics();
                    CommentsDetailActivity.this.getWindowManager().getDefaultDisplay()
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
                    // drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
                    // drawable.getIntrinsicHeight());
                    // ImageSpan span = new ImageSpan(drawable,
                    // ImageSpan.ALIGN_BASELINE);
                    ss.setSpan(span, 0, ss.length(),
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    int cursor = findUpcommentEdittext.getSelectionStart();
                    findUpcommentEdittext.getText().insert(cursor, ss);
                }
            }
        }
    };

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

    /**
     *@method 固定字体大小方法
     *@author suhu
     *@time 2016/10/11 13:23
     *@param
     *
     */
    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        Configuration config=new Configuration();
        config.setToDefaults();
        res.updateConfiguration(config,res.getDisplayMetrics() );
        return res;
    }
}
