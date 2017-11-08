package com.fox.exercise;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.fox.exercise.TitlePopup.OnItemOnClickListener;
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
import com.fox.exercise.newversion.act.PersonalPageMainActivity;
import com.fox.exercise.newversion.act.UserActivityMainActivity;
import com.fox.exercise.newversion.entity.FindGroup;
import com.fox.exercise.newversion.entity.PraiseUsers;
import com.fox.exercise.newversion.entity.SportCircleComments;
import com.fox.exercise.newversion.entity.TopicContent;
import com.fox.exercise.newversion.newact.ShareDialogMainActivity;
import com.fox.exercise.newversion.newact.ZanListUserActivity;
import com.fox.exercise.pedometer.ImageWorkManager;
import com.fox.exercise.util.RoundedImage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.ingenic.indroidsync.SportsApp;

public class FindOtherMoreAdapter extends BaseAdapter implements
        OnItemOnClickListener {
    private PopupWindow mSportWindow = null;
    private String activityTime_2;
    private String topTime;
    private List<ActionList> actionLists = new ArrayList<ActionList>();
    private List<String> activityNameList = new ArrayList<String>();
    private List<String> activityURLList = new ArrayList<String>();
    private List<Integer> activityIdList = new ArrayList<Integer>();
    private List<Integer> activitySendId = new ArrayList<Integer>();
    private List<Integer> activityNameSize = new ArrayList<Integer>();
    private Dialog waitProgressDialog;
    // 针对9种不同的布局
    ViewHolder holder = null;
    private Context mContext = null;
    private ArrayList<FindGroup> mList = null;
    private SportsApp mSportsApp;
    private LayoutInflater mInflater = null;
    private ImageDownloader mDownloader = null;
    private ImageDownloader mDownloader2 = null;
    // 图片正方形
    private ImageResizer mImageWorker;
    private int mItemHeight = 0;
    private Dialog loadProgressDialog = null;
    private TextView message = null;
    // 分别将不同布局的IMG封装，用于赋值和绑定监听事件
    public List<ImageView> imgList2 = new ArrayList<ImageView>();
    public List<ImageView> imgList3 = new ArrayList<ImageView>();
    public List<ImageView> imgList4 = new ArrayList<ImageView>();
    public List<ImageView> imgList5 = new ArrayList<ImageView>();
    public List<ImageView> imgList6 = new ArrayList<ImageView>();
    public List<ImageView> imgList7 = new ArrayList<ImageView>();
    public List<ImageView> imgList8 = new ArrayList<ImageView>();
    public List<ImageView> imgList9 = new ArrayList<ImageView>();
    int type;
    String[] urls;
    private static final String TAG = "CopyFindOtherMoreAdapter";
    private Dialog alertDialog;
    private UserDetail detail;
    private TitlePopup titlePopup;

    // 评论中第一个名字 /第二个名字/文字内容/音频内容/音频长度
    private String theFirstName;
    private String theSecondName;
    private String theTalkDetils;
    private String thewavComment;
    private String thewavDuration;
    private String newNameString;
    // 文字评论最终要显示的字符串
    private String newMessageString;
    private View newView;
    private LayoutInflater xInflater;
    private SpannableString spans;
    private List<ExpressionItems> imgItems;
    private List<ExpressionItems> imgItems2;
    private int numcom;
    // 录音
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

    private Animation animation;
    private boolean isPersonal = false;
    private int toFlag = 2;
    private DelItem delItem = null;// 回调方法
    private Drawable drawable1, drawable2;
    private int adapter_type;// 标注所在的适配器地方。2是个人主页、1是运动秀、0是活动详情页面。
    private ImageResizer mImageWorker_Icon;
    private ImageWorkManager mImageWorkerMan_Icon;
    private int commentsize = 0;
    private int commentid;

    public FindOtherMoreAdapter(Context context, List<ActionList> actionLists,
                                ArrayList<FindGroup> list, SportsApp sportsApp,
                                List<ExpressionItems> imgItem,List<ExpressionItems> imgItem2, int adapter_type) {
        // TODO Auto-generated constructor stub
        this.mContext = context;
        this.actionLists = actionLists;
        mSportsApp = sportsApp;
        this.mList = list;
        this.imgItems = imgItem;
        this.imgItems2 = imgItem2;
        this.adapter_type = adapter_type;
        mImageWorkerMan_Icon = new ImageWorkManager(mContext, 0, 0);
        mImageWorker_Icon = mImageWorkerMan_Icon.getImageWorker();
        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        detail = SportsApp.getInstance().getSportUser();
        // 头像
        this.mDownloader = new ImageDownloader(context);
        mDownloader.setType(ImageDownloader.ICON);
        // 一张图像
        this.mDownloader2 = new ImageDownloader(context);
        mDownloader2.setType(ImageDownloader.OnlyOne);
        // 正方形图像
        mItemHeight = Util.getRealPixel_W(mContext,
                (int) (SportsApp.ScreenWidth * 0.625) / 3);
        mImageWorker = mSportsApp.getImageWorker(mContext, mItemHeight,
                mItemHeight);
        mImageWorker.setType(1);
        loadProgressDialog = new Dialog(context, R.style.sports_dialog);
        View v1 = mInflater.inflate(R.layout.sports_progressdialog, null);
        message = (TextView) v1.findViewById(R.id.message);
        v1.setMinimumWidth((int) (SportsApp.ScreenWidth * 0.8));
        loadProgressDialog.setContentView(v1);
        titlePopup = new TitlePopup(mContext, mSportsApp.dip2px(165),
                mSportsApp.dip2px(40));
        xInflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mRecorder = new RecordHelper();

        drawable1 = mContext.getResources().getDrawable(
                R.drawable.sportshow_zan_black);
        drawable1.setBounds(0, 0, drawable1.getMinimumWidth(),
                drawable1.getMinimumHeight());
        drawable2 = mContext.getResources().getDrawable(
                R.drawable.sportshow_zan);
        drawable2.setBounds(0, 0, drawable2.getMinimumWidth(),
                drawable2.getMinimumHeight());
        if (adapter_type == 2) {
            this.toFlag = 1;
        }
    }

    public FindOtherMoreAdapter(Context context, ArrayList<FindGroup> list,
                                SportsApp sportsApp, boolean isPersonal, int toFlag) {
        // TODO Auto-generated constructor stub
        this.isPersonal = isPersonal;
        this.toFlag = toFlag;
        this.mContext = context;
        mSportsApp = sportsApp;
        this.mList = list;
        // 对集合进行监听、不能重复出现取消置顶的按钮。
        if (list.size() % 2 != 0) {
            for (int i = 0; i < list.size(); i++) {
                list.get(i).setFlog(0);
            }
            list.get(0).setFlog(2);
        }

        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        detail = SportsApp.getInstance().getSportUser();
        // 头像
        this.mDownloader = new ImageDownloader(context);
        mDownloader.setType(ImageDownloader.ICON);
        // 一张图像
        this.mDownloader2 = new ImageDownloader(context);
        mDownloader2.setType(ImageDownloader.OnlyOne);
        // 正方形图像
        mItemHeight = Util.getRealPixel_W(mContext,
                (int) (SportsApp.ScreenWidth * 0.625) / 3);
        mImageWorker = mSportsApp.getImageWorker(mContext, mItemHeight,
                mItemHeight);
        mImageWorker.setType(1);
        loadProgressDialog = new Dialog(context, R.style.sports_dialog);
        View v1 = mInflater.inflate(R.layout.sports_progressdialog, null);
        message = (TextView) v1.findViewById(R.id.message);
        v1.setMinimumWidth((int) (SportsApp.ScreenWidth * 0.8));
        loadProgressDialog.setContentView(v1);
        titlePopup = new TitlePopup(mContext, mSportsApp.dip2px(165),
                mSportsApp.dip2px(40));
        xInflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mRecorder = new RecordHelper();

        drawable1 = mContext.getResources().getDrawable(
                R.drawable.sportshow_zan_black);
        drawable1.setBounds(0, 0, drawable1.getMinimumWidth(),
                drawable1.getMinimumHeight());
        drawable2 = mContext.getResources().getDrawable(
                R.drawable.sportshow_zan);
        drawable2.setBounds(0, 0, drawable2.getMinimumWidth(),
                drawable2.getMinimumHeight());
    }

    // TODO Auto-generated constructor stub

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mList != null?mList.size():0;
    }

    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return mList != null?mList.get(arg0):0;
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return arg0;
    }

    @Override
    public int getItemViewType(int position) {
        if (mList.get(position).getImgs() != null) {
            type = mList.get(position).getImgs().length;
        } else {
            type = 0;
        }
        return type;
    }

    @Override
    public int getViewTypeCount() {
        // TODO Auto-generated method stub
        return 10;
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

    @Override
    public View getView(final int position, View convertView, ViewGroup arg2) {
        // TODO Auto-generated method stub
        if (mList != null && mList.size() != 0) {
        urls = mList.get(position).getImgs();
        getItemViewType(position);
        if (convertView == null) {
            holder = new ViewHolder();
            switch (type) {
                case 0:
                    convertView = mInflater.inflate(
                            R.layout.base_activity_details_zero_layout, null);
                    break;
                case 1:
                    convertView = mInflater.inflate(
                            R.layout.base_activity_details_one_layout, null);
                    holder.img1 = (ImageView) convertView
                            .findViewById(R.id.detils_img_one);
                    break;
                case 2:
                    convertView = mInflater.inflate(
                            R.layout.base_activity_details_two_layout, null);
                    holder.img1 = (ImageView) convertView
                            .findViewById(R.id.detils_img_one);
                    holder.img2 = (ImageView) convertView
                            .findViewById(R.id.detils_img_two);
                    break;
                case 3:
                    convertView = mInflater.inflate(
                            R.layout.base_activity_details_three_1_layout, null);
                    holder.img1 = (ImageView) convertView
                            .findViewById(R.id.detils_img_one);
                    holder.img2 = (ImageView) convertView
                            .findViewById(R.id.detils_img_two);
                    holder.img3 = (ImageView) convertView
                            .findViewById(R.id.detils_img_three);
                    break;
                case 4:
                    convertView = mInflater.inflate(
                            R.layout.base_activity_details_four_layout, null);
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
                    convertView = mInflater.inflate(
                            R.layout.base_activity_details_four_layout, null);
                    holder.img1 = (ImageView) convertView
                            .findViewById(R.id.detils_img_one);
                    holder.img2 = (ImageView) convertView
                            .findViewById(R.id.detils_img_two);
                    holder.img3 = (ImageView) convertView
                            .findViewById(R.id.detils_img_three);
                    holder.img4 = (ImageView) convertView
                            .findViewById(R.id.detils_img_four);
                    break;
                case 6:
                    convertView = mInflater.inflate(
                            R.layout.base_activity_details_four_layout, null);
                    holder.img1 = (ImageView) convertView
                            .findViewById(R.id.detils_img_one);
                    holder.img2 = (ImageView) convertView
                            .findViewById(R.id.detils_img_two);
                    holder.img3 = (ImageView) convertView
                            .findViewById(R.id.detils_img_three);
                    holder.img4 = (ImageView) convertView
                            .findViewById(R.id.detils_img_four);
                    break;
                case 7:
                    convertView = mInflater.inflate(
                            R.layout.base_activity_details_four_layout, null);
                    holder.img1 = (ImageView) convertView
                            .findViewById(R.id.detils_img_one);
                    holder.img2 = (ImageView) convertView
                            .findViewById(R.id.detils_img_two);
                    holder.img3 = (ImageView) convertView
                            .findViewById(R.id.detils_img_three);
                    holder.img4 = (ImageView) convertView
                            .findViewById(R.id.detils_img_four);
                    break;
                case 8:
                    convertView = mInflater.inflate(
                            R.layout.base_activity_details_four_layout, null);
                    holder.img1 = (ImageView) convertView
                            .findViewById(R.id.detils_img_one);
                    holder.img2 = (ImageView) convertView
                            .findViewById(R.id.detils_img_two);
                    holder.img3 = (ImageView) convertView
                            .findViewById(R.id.detils_img_three);
                    holder.img4 = (ImageView) convertView
                            .findViewById(R.id.detils_img_four);
                    break;
                case 9:
                    convertView = mInflater.inflate(
                            R.layout.base_activity_details_four_layout, null);
                    // 判断图片的是水平还是竖直从而设定此ImageView的长宽
                    holder.img1 = (ImageView) convertView
                            .findViewById(R.id.detils_img_one);
                    holder.img2 = (ImageView) convertView
                            .findViewById(R.id.detils_img_two);
                    holder.img3 = (ImageView) convertView
                            .findViewById(R.id.detils_img_three);
                    holder.img4 = (ImageView) convertView
                            .findViewById(R.id.detils_img_four);
                    break;
                default:
                    break;
            }
            holder.otherImage = (RoundedImage) convertView
                    .findViewById(R.id.image_icon1);
            holder.dianzan_one = (RoundedImage) convertView
                    .findViewById(R.id.dianzan_one);
            holder.dianzan_two = (RoundedImage) convertView
                    .findViewById(R.id.dianzan_two);
            holder.dianzan_three = (RoundedImage) convertView
                    .findViewById(R.id.dianzan_three);
            holder.dianzan_four = (RoundedImage) convertView
                    .findViewById(R.id.dianzan_four);
            holder.dianzan_five = (RoundedImage) convertView
                    .findViewById(R.id.dianzan_five);
            holder.dianzan_six = (RoundedImage) convertView
                    .findViewById(R.id.dianzan_six);
            holder.zan_more_icon = (ImageView) convertView
                    .findViewById(R.id.zan_more_icon);
            holder.activity_zan_layout = (RelativeLayout) convertView
                    .findViewById(R.id.activity_zan_layout);
            holder.start_time = (TextView) convertView
                    .findViewById(R.id.tx_start_times);
            holder.nametext = (TextView) convertView
                    .findViewById(R.id.sports_find_othername1);
            holder.detils = (TextView) convertView
                    .findViewById(R.id.tx_detils1);
            // 赞和评论
            holder.goodandtext = (TextView) convertView.findViewById(R.id.good_and_text);
            holder.commentLayout = (LinearLayout) convertView
                    .findViewById(R.id.good_text_detils_layout);
            // 赞人数
            holder.goodPeopole = (TextView) convertView
                    .findViewById(R.id.list_user_zan);
            // 线
            holder.textgoodline = (View) convertView
                    .findViewById(R.id.text_good_bgline);
            // 评论人数
            holder.textLayout = (LinearLayout) convertView
                    .findViewById(R.id.find_text_num_layout);
            holder.textPeopole = (TextView) convertView
                    .findViewById(R.id.list_user_pinglun);
            holder.talkdetilsLayout = (LinearLayout) convertView
                    .findViewById(R.id.layout_talk_text);
            // 实例化评论点赞和分享模块，隐藏起来
            holder.ll_comment_zan_shaer = (LinearLayout) convertView
                    .findViewById(R.id.ll_comment_zan_shaer);
            holder.ll_comment_zan_shaer.setVisibility(View.GONE);

            holder.list_user_pinglun_layout = (TextView) convertView
                    .findViewById(R.id.list_user_pinglun_layout);
            holder.list_user_zan_layout = (RelativeLayout) convertView
                    .findViewById(R.id.list_user_zan_layout);
            holder.list_user_fenxiang_layout = (RelativeLayout) convertView
                    .findViewById(R.id.list_user_fenxiang_layout);

            holder.list_user_guanzhu = (Button) convertView
                    .findViewById(R.id.list_user_guanzhu);
            holder.list_user_guanzhu_addone = (TextView) convertView
                    .findViewById(R.id.list_user_guanzhu_addone);
            holder.list_user_fenxiang = (TextView) convertView
                    .findViewById(R.id.list_user_fenxiang);
            holder.tx_sport_address = (TextView) convertView
                    .findViewById(R.id.tx_sport_address);
            holder.tx_sport_huodong = (TextView) convertView
                    .findViewById(R.id.tx_sport_huodong);
            holder.tx_sport_days = (TextView) convertView
                    .findViewById(R.id.tx_sport_days);
            holder.list_user_zan_addone = (TextView) convertView
                    .findViewById(R.id.list_user_zan_addone);
            holder.is_manorwomen_icon = (ImageView) convertView
                    .findViewById(R.id.is_manorwomen_icon);
            holder.tx_sport_del = (TextView) convertView
                    .findViewById(R.id.tx_sport_del);
            holder.tx_sport_toplist = (TextView) convertView
                    .findViewById(R.id.tx_sport_toplist);
            holder.tx_sport_removetop = (TextView) convertView
                    .findViewById(R.id.tx_sport_removetop);
            holder.tx_xianshiquanbu = (TextView) convertView
                    .findViewById(R.id.tx_xianshiquanbu);
            holder.iv_zhiding = (ImageView) convertView
                    .findViewById(R.id.zhiding_001);
            holder.iv_zhiding.setVisibility(View.GONE);
            holder.sportshow_shaer = (ImageView) convertView
                    .findViewById(R.id.sportshow_shaer);
            holder.ll_dianzanimage = (LinearLayout) convertView
                    .findViewById(R.id.ll_dianzanimage);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
            holder.talkdetilsLayout.removeAllViews();
        }

        switch (type) {
            case 1:
                int height = mList.get(position).getHeight();
                int width = mList.get(position).getWidth();
                if (width >= height) {
                    LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) holder.img1
                            .getLayoutParams();
                    int imgWidth = (int) (SportsApp.ScreenWidth * 0.5);
                    if (width != 0) {
                        lp.height = (int) ((imgWidth * height) / width);
                        lp.width = imgWidth;
                    }else {
                        lp.height = 0;
                        lp.width = 0;
                    }
                    holder.img1.setLayoutParams(lp);
                } else if (height > width ) {
                    LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) holder.img1
                            .getLayoutParams();
                    int imgHeight = (int) (SportsApp.ScreenHeight * 0.33);
                    if (height != 0){
                        lp.height = imgHeight;
                        lp.width = (int) ((imgHeight * width) / height);
                    }else{
                        lp.height = 0;
                        lp.width = 0;
                    }

                    holder.img1.setLayoutParams(lp);
                }
                mDownloader2.download(urls[0], holder.img1, null);
                break;
            case 2:
                imgList2.clear();
                imgList2.add(holder.img1);
                imgList2.add(holder.img2);
                setImageVoid(type, position, imgList2);
                break;
            case 3:
                imgList3.clear();
                imgList3.add(holder.img1);
                imgList3.add(holder.img2);
                imgList3.add(holder.img3);
                setImageVoid(type, position, imgList3);
                break;
            case 4:
                imgList4.clear();
                imgList4.add(holder.img1);
                imgList4.add(holder.img2);
                imgList4.add(holder.img3);
                imgList4.add(holder.img4);
                setImageVoid(type, position, imgList4);
                break;
            case 5:
                imgList5.clear();
                imgList5.add(holder.img1);
                imgList5.add(holder.img2);
                imgList5.add(holder.img3);
                imgList5.add(holder.img4);
                setImageVoid(4, position, imgList5);
                break;
            case 6:
                imgList6.clear();
                imgList6.add(holder.img1);
                imgList6.add(holder.img2);
                imgList6.add(holder.img3);
                imgList6.add(holder.img4);
                setImageVoid(4, position, imgList6);
                break;
            case 7:
                imgList7.clear();
                imgList7.add(holder.img1);
                imgList7.add(holder.img2);
                imgList7.add(holder.img3);
                imgList7.add(holder.img4);
                setImageVoid(4, position, imgList7);
                break;
            case 8:
                imgList8.clear();
                imgList8.add(holder.img1);
                imgList8.add(holder.img2);
                imgList8.add(holder.img3);
                imgList8.add(holder.img4);
                setImageVoid(4, position, imgList8);
                break;
            case 9:
                imgList9.clear();
                imgList9.add(holder.img1);
                imgList9.add(holder.img2);
                imgList9.add(holder.img3);
                imgList9.add(holder.img4);
                setImageVoid(4, position, imgList9);
                break;
            default:
                break;
        }
        if (holder.img1 != null) {
            holder.img1.setOnClickListener(new imgOnClickListener(0, mList.get(
                    position).getBiggerImgs()));
        }
        if (holder.img2 != null) {
            holder.img2.setOnClickListener(new imgOnClickListener(1, mList.get(
                    position).getBiggerImgs()));
        }
        if (holder.img3 != null) {
            holder.img3.setOnClickListener(new imgOnClickListener(2, mList.get(
                    position).getBiggerImgs()));
        }
        if (holder.img4 != null) {
            holder.img4.setOnClickListener(new imgOnClickListener(3, mList.get(
                    position).getBiggerImgs()));
        }
        if (holder.img5 != null) {
            holder.img5.setOnClickListener(new imgOnClickListener(4, mList.get(
                    position).getBiggerImgs()));
        }
        if (holder.img6 != null) {
            holder.img6.setOnClickListener(new imgOnClickListener(5, mList.get(
                    position).getBiggerImgs()));
        }
        if (holder.img7 != null) {
            holder.img7.setOnClickListener(new imgOnClickListener(6, mList.get(
                    position).getBiggerImgs()));
        }
        if (holder.img8 != null) {
            holder.img8.setOnClickListener(new imgOnClickListener(7, mList.get(
                    position).getBiggerImgs()));
        }
        if (holder.img9 != null) {
            holder.img9.setOnClickListener(new imgOnClickListener(8, mList.get(
                    position).getBiggerImgs()));
        }
        // 这是判断什么时候发的
            if (mList.size() != 0){
                long time = System.currentTimeMillis() - mList.get(position).getTimes()
                        * 1000;
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                if (time <= 60 * 1000)
                    // 一分钟内显示刚刚
                    holder.start_time.setText(mContext.getResources().getString(
                            R.string.sports_time_justnow));
                else if (time <= 60 * 60 * 1000) {
                    int h = (int) (time / 1000 / 60);
                    // 一小时内显示多少分钟前
                    holder.start_time.setText(""
                            + h
                            + mContext.getResources().getString(
                            R.string.sports_time_mins_ago));
                } else {
                    holder.start_time.setText(formatDisplayTime(
                            format.format(mList.get(position).getTimes() * 1000),
                            "yyyy-MM-dd HH:mm"));
                }
            }

        holder.otherImage.setImageDrawable(null);
        if (mList.size() != 0 && "man".equals(mList.get(position).getSex())) {
            holder.otherImage
                    .setBackgroundResource(R.drawable.sports_user_edit_portrait_male);
        } else if (mList.size() != 0 && "woman".equals(mList.get(position).getSex())) {
            holder.otherImage
                    .setBackgroundResource(R.drawable.sports_user_edit_portrait);
        }
        if (mList.size() != 0){
            mDownloader.download(mList.get(position).getOtherimg(),
                    holder.otherImage, null);
            if (!isPersonal) {
                holder.otherImage
                        .setOnClickListener(new personalInformationOnClickListener(
                                mList.get(position).getOtheruid()));
            }
            // 显示名字
            holder.nametext.setText(mList.get(position).getOthername());
            holder.nametext
                    .setOnClickListener(new personalInformationOnClickListener(
                            mList.get(position).getOtheruid()));
        }
        titlePopup.setItemOnClickListener(this);
        if (adapter_type == 2 || adapter_type == 0 && mList.size() != 0) {
            holder.goodandtext.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Utils.isFastClick(3000)) {
                        return;
                    }
                    // TODO Auto-generated method stub
                    Intent activityIntent = new Intent(mContext,
                            UserActivityMainActivity.class);
                    activityIntent.putExtra("mFindMore", mList.get(position));
                    activityIntent.putExtra("type", type);
                    activityIntent.putExtra("toFlag", toFlag);
                    mContext.startActivity(activityIntent);
                }
            });
        } else {
            holder.goodandtext.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {

                    // TODO Auto-generated method stub
                    // 点赞和评论
                    if (changeCommentListener != null)
                        changeCommentListener.OnCheckedChangeListener(position,
                                mList.get(position).getFindId(), 1, null, null);
                }
            });
        }

        if (mList.size() != 0 && "man".equals(mList.get(position).getSex())) {
            holder.is_manorwomen_icon
                    .setImageResource(R.drawable.friends_group_sexman);
        } else if (mList.size() != 0 && "woman".equals(mList.get(position).getSex())) {
            holder.is_manorwomen_icon
                    .setImageResource(R.drawable.friends_group_sexwomen);
        }

        // 如果评论人数大于0或者点赞人数大于0
        if (mList.size() != 0 && mList.get(position).getGoodpeople() <= 0
                && mList.get(position).getcCount() <= 0) {
            holder.goodPeopole
                    .setCompoundDrawables(drawable1, null, null, null);
            holder.activity_zan_layout.setVisibility(View.GONE);
            holder.dianzan_one.setVisibility(View.INVISIBLE);
            holder.dianzan_two.setVisibility(View.INVISIBLE);
            holder.dianzan_three.setVisibility(View.INVISIBLE);
            holder.dianzan_four.setVisibility(View.INVISIBLE);
            holder.dianzan_five.setVisibility(View.INVISIBLE);
            holder.dianzan_six.setVisibility(View.INVISIBLE);
            holder.goodPeopole.setText("(0)");
            holder.textPeopole.setText("回复 (0)");
        } else {
            if (mList.size() != 0 && mList.get(position).getGoodpeople() > 0) {
                holder.activity_zan_layout.setVisibility(View.VISIBLE);
                holder.dianzan_one.setVisibility(View.VISIBLE);
                holder.dianzan_two.setVisibility(View.VISIBLE);
                holder.dianzan_three.setVisibility(View.VISIBLE);
                holder.dianzan_four.setVisibility(View.VISIBLE);
                holder.dianzan_five.setVisibility(View.VISIBLE);
                holder.dianzan_six.setVisibility(View.VISIBLE);
                if (mList.size() != 0 && mList.get(position).isGood()) {
                    holder.goodPeopole.setCompoundDrawables(drawable2, null,
                            null, null);
                } else {
                    holder.goodPeopole.setCompoundDrawables(drawable1, null,
                            null, null);
                }
                holder.goodPeopole.setText("("
                        + mList.get(position).getGoodpeople() + ")");
            } else {
                holder.activity_zan_layout.setVisibility(View.GONE);
                holder.dianzan_one.setVisibility(View.INVISIBLE);
                holder.dianzan_two.setVisibility(View.INVISIBLE);
                holder.dianzan_three.setVisibility(View.INVISIBLE);
                holder.dianzan_four.setVisibility(View.INVISIBLE);
                holder.dianzan_five.setVisibility(View.INVISIBLE);
                holder.dianzan_six.setVisibility(View.INVISIBLE);
                holder.goodPeopole.setCompoundDrawables(drawable1, null, null,
                        null);
                holder.goodPeopole.setText("(0)");
            }

            if (mList.size() != 0 && mList.get(position).getcCount() > 0) {
                holder.textPeopole.setText("回复 ("
                        + mList.get(position).getcCount() + ")");
                // 将评论内容显示出来
                if (mList.get(position).getTalkdetils() != null) {
                    for (int i = 0; i < mList.get(position).getTalkdetils()
                            .size(); i++) {
                    }
                }
            } else {
                holder.textPeopole.setText("回复 (0)");
            }
            // 展示评论。
            if (mList.size() != 0 && mList.get(position).getcCount() > 0) {
                holder.textLayout.setVisibility(View.VISIBLE);
                holder.textPeopole
                        .setText(mList.get(position).getcCount() + "");
                // 将评论内容显示出来
                if (mList.get(position).getTalkdetils() != null) {
                    if (mList.get(position).getTalkdetils().size() <= 2) {
                        commentsize = mList.get(position).getTalkdetils()
                                .size();
                    } else {
                        commentsize = 2;
                    }
                    for (int i = 0; i < commentsize; i++) {
                        commentid = i;
                        theFirstName = mList.get(position).getTalkdetils()
                                .get(i).getName();
                        theSecondName = mList.get(position).getTalkdetils()
                                .get(i).getTo_name();
                        thewavComment = mList.get(position).getTalkdetils()
                                .get(i).getWav();
                        thewavDuration = mList.get(position).getTalkdetils()
                                .get(i).getWavtime();
                        theTalkDetils = mList.get(position).getTalkdetils()
                                .get(i).getContent();

                        // 如果是语音回复
                        int firstname, secondname = 0;
                        if (thewavComment != null && thewavDuration != null
                                && !thewavDuration.equals("null")
                                && !thewavDuration.equals("0")) {
                            newView = xInflater
                                    .inflate(
                                            R.layout.sports_find_talk_wav_detiles,
                                            null);
                            TextView nametoName = (TextView) newView
                                    .findViewById(R.id.find_talk_detils_text_name);
                            LinearLayout recordlayout = (LinearLayout) newView
                                    .findViewById(R.id.recoding_click_find);
                            TextView durationtext = (TextView) newView
                                    .findViewById(R.id.wav_durations_find);
                            ImageView beginWav = (ImageView) newView
                                    .findViewById(R.id.wav_begin_find);
                            RoundedImage activity_image_icon = (RoundedImage) newView
                                    .findViewById(R.id.activity_image_icon);
                            TextView imput_time = (TextView) newView
                                    .findViewById(R.id.imput_time);
                            if ("http://dev-kupao.mobifox.cn".equals(mList.get(position).getTalkdetils()
                                    .get(i).getImg())
                                    || "http://kupao.mobifox.cn".equals(mList.get(position).getTalkdetils()
                                    .get(i).getImg()) ||  mList.get(position).getTalkdetils()
                                    .get(i).getImg() == null) {
                                if (mList.get(position).getTalkdetils().get(i)
                                        .getSex().equals("1") || mList.get(position).getTalkdetils().get(i)
                                        .getSex().equals("man")) {
                                    activity_image_icon
                                            .setImageResource(R.drawable.sports_user_edit_portrait_male);
                                } else {
                                    activity_image_icon
                                            .setImageResource(R.drawable.sports_user_edit_portrait);
                                }
                            } else {
                                if (mList.get(position).getTalkdetils().get(i)
                                        .getSex().equals("1") || mList.get(position).getTalkdetils().get(i)
                                        .getSex().equals("man")) {
                                    activity_image_icon
                                            .setImageResource(R.drawable.sports_user_edit_portrait_male);
                                } else {
                                    activity_image_icon
                                            .setImageResource(R.drawable.sports_user_edit_portrait);
                                }
                                mImageWorker_Icon.loadImage(mList.get(position)
                                                .getTalkdetils().get(i).getImg(),
                                        activity_image_icon, null, null, false);
                            }

                            // 评论时间
                            long pinglTm = System.currentTimeMillis()
                                    - Long.valueOf(mList.get(position)
                                    .getTalkdetils().get(i)
                                    .getInputtime()) * 1000;
                            SimpleDateFormat pformat = new SimpleDateFormat(
                                    "yyyy-MM-dd HH:mm");
                            if (pinglTm <= 60 * 1000)
                                // 一分钟内显示刚刚
                                imput_time.setText("刚刚");
                            else if (pinglTm <= 60 * 60 * 1000) {
                                int h = (int) (pinglTm / 1000 / 60);
                                // 一小时内显示多少分钟前
                                imput_time.setText("" + h + "分钟前");
                            } else {
                                imput_time.setText(pformat.format(Long
                                        .valueOf(mList.get(position)
                                                .getTalkdetils().get(i)
                                                .getInputtime()) * 1000));
                            }

                            if (theSecondName == null
                                    || theSecondName.equals("")
                                    || theSecondName.equals("null")) {
                                newMessageString = theFirstName + ":";
                                numcom = 1;
                                firstname = theFirstName.length();
                            } else {
                                newMessageString = theFirstName
                                        + mContext
                                        .getResources()
                                        .getString(
                                                R.string.multi_comment_tip_target)
                                        + theSecondName + ":";
                                numcom = 2;
                                firstname = theFirstName.length();
                                secondname = theSecondName.length();
                            }
                            durationtext.setText("" + thewavDuration + "″");
                            String ssString = ToDBC(newMessageString);
                            List<SpannableString> list = getExpressionString(
                                    mContext, ssString, numcom, firstname,
                                    secondname);
                            for (SpannableString span : list) {
                                nametoName.setText(span);
                            }
                            // 录音播放
                            final Object lock = new Object();
                            final String mediaPath = thewavComment;
                            final ImageView wavBegin = beginWav;
                            recordlayout
                                    .setOnClickListener(new OnClickListener() {
                                        @Override
                                        public void onClick(View arg0) {
                                            // TODO Auto-generated method stub
                                            synchronized (lock) {
                                                if (mediaPath.endsWith("null")
                                                        || mediaPath == null) {
                                                    return;
                                                }
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
                                                        }

                                                        if (isStart) {
                                                            wavBeginOne = wavBegin;
                                                            isStart = false;
                                                            if (!isPause) {
                                                                mPlayer = mRecorder
                                                                        .startPlaybackNet(
                                                                                mContext,
                                                                                mediaPath);
                                                            }
                                                            if (mPlayer == null) {
                                                                mPlayer = mRecorder
                                                                        .startPlaybackNet(
                                                                                mContext,
                                                                                mediaPath);
                                                                mHandler.sendMessage(mHandler
                                                                        .obtainMessage(
                                                                                RECORD_LOADING,
                                                                                wavBegin));
                                                            }
                                                            mPlayer.setOnErrorListener(new OnErrorListener() {
                                                                @Override
                                                                public boolean onError(
                                                                        MediaPlayer arg0,
                                                                        int arg1,
                                                                        int arg2) {
                                                                    isStart = true;
                                                                    currentDuration = 0;
                                                                    mRecorder
                                                                            .stopPlayback();
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
                                                                    isStart = true;
                                                                    isPause = false;
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
                                                                    mPlayer.prepare();
                                                                    mPlayer.start();

                                                                } else {
                                                                    mPlayer.start();
                                                                    mHandler.sendMessage(mHandler
                                                                            .obtainMessage(
                                                                                    RECORD_PREPARED,
                                                                                    wavBegin));
                                                                }
                                                            } catch (IllegalStateException e) {
                                                                mPlayer = null;
                                                                isStart = true;
                                                                currentDuration = 0;
                                                                mHandler.sendMessage(mHandler
                                                                        .obtainMessage(
                                                                                RECORD_PAUSE,
                                                                                wavBegin));
                                                            } catch (Exception e) {
                                                                mPlayer = null;
                                                                isStart = true;
                                                                currentDuration = 0;
                                                                mHandler.sendMessage(mHandler
                                                                        .obtainMessage(
                                                                                RECORD_PAUSE,
                                                                                wavBegin));
                                                            }
                                                            if (currentDuration > 0
                                                                    || isPause) {
                                                                isPause = false;
                                                                if (mPlayer != null) {
                                                                    mPlayer.start();
                                                                } else {
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
                                                            if (mPlayer != null) {
                                                                try {
                                                                    mPlayer.pause();
                                                                    isStart = true;
                                                                    isPause = true;
                                                                } catch (Exception e) {
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
                            newView = xInflater.inflate(
                                    R.layout.sports_find_talk_detils, null);
                            TextView sTextView = (TextView) newView
                                    .findViewById(R.id.find_talk_detils_text);
                            TextView nametoName = (TextView) newView
                                    .findViewById(R.id.find_talk_detils_text_name);
                            // 设置评论头像和评论时间
                            RoundedImage activity_image_icon = (RoundedImage) newView
                                    .findViewById(R.id.activity_image_icon);
                            TextView imput_time = (TextView) newView
                                    .findViewById(R.id.imput_time);
                            if ("http://kupao.mobifox.cn".equals(mList.get(position).getTalkdetils()
                                    .get(i).getImg())
                                    || "http://dev-kupao.mobifox.cn".equals(mList.get(position).getTalkdetils()
                                    .get(i).getImg()) || mList.get(position).getTalkdetils()
                                    .get(i).getImg() == null) {
                                    if (mList.get(position).getTalkdetils().get(i)
                                            .getSex().equals("1") || mList.get(position).getTalkdetils().get(i)
                                            .getSex().equals("man")) {
                                        activity_image_icon
                                                .setImageResource(R.drawable.sports_user_edit_portrait_male);
                                    } else {
                                        activity_image_icon
                                                .setImageResource(R.drawable.sports_user_edit_portrait);
                                    }
                            } else {
                                if (mList.get(position).getTalkdetils().get(i)
                                        .getSex().equals("1") || mList.get(position).getTalkdetils().get(i)
                                        .getSex().equals("man")) {
                                    activity_image_icon
                                            .setImageResource(R.drawable.sports_user_edit_portrait_male);
                                } else {
                                    activity_image_icon
                                            .setImageResource(R.drawable.sports_user_edit_portrait);
                                }
                                mImageWorker_Icon.loadImage(mList.get(position)
                                                .getTalkdetils().get(i).getImg(),
                                        activity_image_icon, null, null, false);
                            }

                            // 评论时间
                            long pinglTm = System.currentTimeMillis()
                                    - Long.valueOf(mList.get(position)
                                    .getTalkdetils().get(i)
                                    .getInputtime()) * 1000;
                            SimpleDateFormat pformat = new SimpleDateFormat(
                                    "yyyy-MM-dd HH:mm");
                            if (pinglTm <= 60 * 1000)
                                // 一分钟内显示刚刚
                                imput_time.setText("刚刚");
                            else if (pinglTm <= 60 * 60 * 1000) {
                                int h = (int) (pinglTm / 1000 / 60);
                                // 一小时内显示多少分钟前
                                imput_time.setText("" + h + "分钟前");
                            } else {
                                imput_time.setText(pformat.format(Long
                                        .valueOf(mList.get(position)
                                                .getTalkdetils().get(i)
                                                .getInputtime()) * 1000));
                            }
                            // 评论内容:
                            String stringoneString = theTalkDetils.replaceAll(
                                    "\\[", " [");
                            if (theSecondName == null
                                    || theSecondName.equals("")
                                    || theSecondName.equals("null")) {
                                newMessageString = stringoneString;
                                newNameString = theFirstName + ":";
                                numcom = 1;
                                firstname = theFirstName.length();
                            } else {

                                newMessageString = stringoneString;
                                newNameString = theFirstName + "回复"
                                        + theSecondName + ":";
                                numcom = 2;
                                firstname = theFirstName.length();
                                secondname = theSecondName.length();
                            }
                            String ssString = ToDBC(newNameString);
                            List<SpannableString> nameString = getNameString(
                                    mContext, ssString, numcom, firstname,
                                    secondname);
                            for (SpannableString span : nameString) {
                                nametoName.setText(span);
                            }
                            List<SpannableString> list = getContentString(
                                    mContext, newMessageString, numcom);
                            for (SpannableString span : list) {
                                sTextView.setText(span);
                            }
                            sTextView.invalidate();
                        }
//                        3.4版本之前的评论监听功能
                        if (adapter_type == 1) {
                            if (mList.get(position).getTalkdetils() != null) {
                                newView.setOnClickListener(new commentOnClickListener(
                                        position, i, mList.get(position).getFindId(),
                                        mList.get(position).getTalkdetils().get(i)
                                                .getId(), theFirstName));
                            }
                        } else {
                            newView.setOnClickListener(new OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    // TODO Auto-generated method stub
                                    Intent activityIntent = new Intent(mContext,
                                            UserActivityMainActivity.class);
                                    activityIntent.putExtra("mFindMore", mList.get(position));
                                    activityIntent.putExtra("type", type);
                                    activityIntent.putExtra("toFlag", toFlag);
                                    mContext.startActivity(activityIntent);
                                }
                            });
                        }
                        holder.talkdetilsLayout.addView(newView);
                    }
                }
            } else {
                holder.textLayout.setVisibility(View.GONE);
            }
            if (mList.size() != 0 && mList.get(position).getcCount() > 2) {
                holder.list_user_pinglun_layout.setVisibility(View.VISIBLE);
                holder.list_user_pinglun_layout.setText("全部" + mList.get(position).getcCount() + "条评论");
            } else {
                holder.list_user_pinglun_layout.setVisibility(View.GONE);
            }
        }

        if (mList.size() != 0 && mList.get(position).getpArrayList() != null
                && mList.get(position).getpArrayList().size() != 0) {
            Collections.reverse(mList.get(position).getpArrayList());
            showZanImage(mList.get(position).getpArrayList(), position);
        } else {
        }
        // 评论跳转到动态详情页面
        holder.list_user_pinglun_layout
                .setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        // TODO Auto-generated method stub
                        // Intent activityIntent = new Intent(mContext,
                        // UserActivityMainActivity.class);
                        Intent activityIntent = new Intent(mContext,
                                UserActivityMainActivity.class);
                        activityIntent.putExtra("mFindMore",
                                mList.get(position));
                        activityIntent.putExtra("type", type);
                        activityIntent.putExtra("toFlag", toFlag);
                        mContext.startActivity(activityIntent);
                    }
                });
        // 点击分享按钮
            if (mList.size() != 0){
                holder.sportshow_shaer.setOnClickListener(new ShareClickListener(mList.get(position)));
            }

        // 点击减1加1效果测试
        animation = AnimationUtils.loadAnimation(mContext, R.anim.add_one);
        holder.list_user_zan_layout.setOnClickListener(new AddAnimian2(
                holder.list_user_zan_addone, position, holder.goodPeopole,
                holder.otherImage, holder.dianzan_one, holder.dianzan_two,
                holder.dianzan_three, holder.dianzan_four, holder.dianzan_five,
                holder.dianzan_six, holder.zan_more_icon,
                holder.activity_zan_layout));
        if (mList.size() != 0 && mList.get(position).getSportRecord() != null
                && !"".equals(mList.get(position).getSportRecord())) {
            holder.tx_sport_days.setText("运动第"
                    + mList.get(position).getSportRecord().getTime() + "天 ");
        }
        if ((adapter_type == 1 || adapter_type == 2) && (mList != null && mList.size() != 0)) {
            if (mList.size() != 0 && mList.get(position).getFlog() != 0 && position <= 2) {
                holder.start_time.setVisibility(View.GONE);
            } else {
                holder.iv_zhiding.setVisibility(View.GONE);
                if (mList.size() >= 3) {
                    for (int j = 0; j < 3; j++) {
                        if (mList.get(j).getFlog() != 0) {
                            for (int i = 0; i < mList.size(); i++) {
                                if (mList.get(position).getFindId()
                                        .equals(mList.get(j).getFindId())) {
                                    holder.start_time.setVisibility(View.GONE);
                                }
                            }
                        } else {
                            holder.start_time.setVisibility(View.VISIBLE);
                        }
                    }
                } else if (mList.size() == 2) {
                    for (int j = 0; j < 2; j++) {
                        if (mList.get(j).getFlog() != 0) {
                            for (int i = 0; i < mList.size(); i++) {
                                if (mList.get(position).getFindId()
                                        .equals(mList.get(j).getFindId())) {
                                    holder.start_time.setVisibility(View.GONE);
                                }
                            }
                        } else {
                            holder.start_time.setVisibility(View.VISIBLE);
                        }

                    }

                } else if (mList.size() == 1) {
                    if (mList.get(0).getFlog() != 0) {
                        for (int i = 0; i < mList.size(); i++) {
                            if (mList.get(position).getFindId()
                                    .equals(mList.get(0).getFindId())) {
                                holder.start_time.setVisibility(View.GONE);
                            }
                        }
                    } else {
                        holder.start_time.setVisibility(View.VISIBLE);
                    }
                }

            }
        }
        if (mList.size() != 0 && adapter_type == 0) {
            if (mList.get(position).getFlog() != 0 && position <= 2) {
                holder.start_time.setVisibility(View.GONE);
            } else {
                holder.iv_zhiding.setVisibility(View.GONE);
            }
        }


        if (mList.size() != 0 && mSportsApp.mIsAdmin) {
            holder.tx_sport_toplist.setOnClickListener(new topListener(mList
                    .get(position).getFindId(), position));
            if (mList.get(position).getFlog() != 0 && position <= 2) {
                holder.tx_sport_toplist.setVisibility(View.GONE);
                notifyDataSetChanged();
                holder.tx_sport_removetop
                        .setOnClickListener(new deltopListener(mList.get(
                                position).getFindId(), position));
            } else {
                holder.tx_sport_removetop.setVisibility(View.GONE);
            }
        } else {
            holder.tx_sport_toplist.setVisibility(View.GONE);
        }

        // 根据isPersonal是从哪个页面进入
        if (isPersonal) {
            // 表示进入的是个人主页
            if (mList.size() != 0 && mList.get(position).getOtheruid() == detail.getUid()) {
                // 表示是登录者登录 显示按钮 把关注按钮改成删除按钮
                holder.tx_sport_del.setVisibility(View.VISIBLE);
                holder.list_user_guanzhu.setVisibility(View.VISIBLE);
                holder.list_user_guanzhu.setBackgroundResource(R.drawable.sportshow_guanzhu);
                holder.tx_sport_del.setOnClickListener(new deleteListener(mList
                        .get(position).getFindId(), position));
            } else {
                // 表示查看其他用户的个人主页面 隐藏关注按钮
                holder.list_user_guanzhu.setVisibility(View.GONE);
                holder.tx_sport_del.setVisibility(View.GONE);
            }
        } else {
            if (mList.size() != 0 && mList.get(position).getIsFriends() == 1) {
                holder.list_user_guanzhu.setVisibility(View.GONE);
                holder.tx_sport_del.setVisibility(View.GONE);
                holder.list_user_guanzhu_addone.setVisibility(View.VISIBLE);
                holder.list_user_guanzhu_addone.setBackgroundColor(Color.parseColor("#FFFFFF"));
            } else {
                holder.list_user_guanzhu.setVisibility(View.VISIBLE);
                holder.list_user_guanzhu.setBackgroundResource(R.drawable.sportshow_guanzhu);
                holder.list_user_guanzhu_addone.setVisibility(View.GONE);
                if (mList.size() != 0 && mList.get(position).getIsFriends() == 0) {
                    holder.list_user_guanzhu.setVisibility(View.GONE);
                    holder.tx_sport_del.setVisibility(View.GONE);
                } else if (mList.size() != 0 && mList.get(position).getIsFriends() == 2) {
                    if (mList.size() != 0 && mList.get(position).getOtheruid() == detail.getUid()) {
                        // 表示是登录者登录 显示按钮 把关注按钮改成删除按钮
                        holder.list_user_guanzhu_addone
                                .setVisibility(View.GONE);
                        holder.tx_sport_del.setVisibility(View.VISIBLE);
                        holder.list_user_guanzhu.setVisibility(View.GONE);
                        holder.tx_sport_del
                                .setOnClickListener(new deleteListener(mList
                                        .get(position).getFindId(), position));
                    } else {
                        holder.list_user_guanzhu_addone
                                .setVisibility(View.GONE);
                        holder.list_user_guanzhu.setVisibility(View.VISIBLE);
                        holder.list_user_guanzhu.setBackgroundResource(R.drawable.sportshow_guanzhu);
                        holder.list_user_guanzhu
                                .setOnClickListener(new AddAnimian(
                                        holder.list_user_guanzhu, position));
                        holder.tx_sport_del.setVisibility(View.GONE);
                    }
                } else {
                    holder.list_user_guanzhu.setVisibility(View.GONE);
                    holder.list_user_guanzhu_addone.setVisibility(View.VISIBLE);
                    holder.list_user_guanzhu_addone.setBackgroundColor(Color.parseColor("#FFFFFF"));
                    holder.tx_sport_del.setVisibility(View.GONE);
                }
            }
        }
        if (mSportsApp.mIsAdmin) {
            holder.tx_sport_del.setVisibility(View.VISIBLE);
            holder.tx_sport_del.setOnClickListener(new deleteListener(mList
                    .get(position).getFindId(), position));
        }
        if (mList.size() != 0 ){
            ArrayList<TopicContent> topicList = mList.get(position).getTopicList();
            if (topicList != null && !"".equals(topicList)) {
                StringBuffer sBuffer = new StringBuffer();
                for (int i = 0; i < topicList.size(); i++) {
                    sBuffer.append(topicList.get(i).getTitle() + "  ");
                }
                if (sBuffer.toString().trim() != null
                        && !"".equals(sBuffer.toString().trim())) {
                    holder.tx_sport_huodong.setText(sBuffer.toString());
                } else {
                    holder.tx_sport_huodong.setVisibility(View.GONE);
                }
            } else {
                holder.tx_sport_huodong.setVisibility(View.GONE);
            }
        }
        if (mList.size() != 0 && mList.get(position).getComefrom() != null
                && !"".equals(mList.get(position).getComefrom())) {
            holder.tx_sport_address.setText(mList.get(position).getComefrom());
        } else {
            holder.tx_sport_address.setText("");
        }

        if (mList.size() != 0 && mList.get(position).getcCount() == 0 || mList.get(position).getcCount() == 1 || mList.get
                (position).getcCount() == 2) {
            holder.list_user_pinglun_layout.setVisibility(View.GONE);
        }
        // 为了点击字段跳转到各自的活动页面详情页面：
        if (mList.size() != 0 && mList.get(position).getDetils() != null
                && !"".equals(mList.get(position).getDetils())) {
            if (mList.get(position).getDetils().lastIndexOf(" ") != 0) {
                boolean flog = mList.get(position).getDetils().contains(" ");
                boolean flog2 = mList.get(position).getDetils().contains("#");
                if (flog && flog2) {
                    String likeUsers = mList
                            .get(position)
                            .getDetils()
                            .substring(
                                    0,
                                    mList.get(position).getDetils()
                                            .lastIndexOf("#") + 1).toString();

                    String listRemove = mList.get(position).getDetils()
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

                                    Intent intent = new Intent(mContext,
                                            ActivityInfoWebView.class);
                                    intent.putExtra("title_name", name
                                            .toString().replace("#", ""));
                                    intent.putExtra("action_url", "");
                                    if (activityNameList.indexOf(name
                                            .toString().replace("#", "")) != -1) {
                                        intent.putExtra(
                                                "activity_id",
                                                activityIdList.get(activityNameList
                                                        .indexOf(name
                                                                .toString()
                                                                .replace("#",
                                                                        ""))));
                                        ActionList action = actionLists
                                                .get(activityNameList
                                                        .indexOf(name
                                                                .toString()
                                                                .replace("#",
                                                                        "")));
                                        activityTime_2 = action
                                                .getActionTime()
                                                .substring(
                                                        action.getActionTime()
                                                                .indexOf("-") + 1,
                                                        action.getActionTime()
                                                                .length())
                                                .replace(".", "-");
                                        activityTime_2 = activityTime_2
                                                .replace("-", "");
                                        int b = Integer.valueOf(activityTime_2)
                                                .intValue() + 1;
                                        activityTime_2 = b + "";
                                        String year = activityTime_2.substring(
                                                0, 4);
                                        String month = activityTime_2
                                                .substring(4, 6);
                                        String day = activityTime_2.substring(
                                                6, 8);
                                        activityTime_2 = year + "-" + month
                                                + "-" + day;
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
                        aaaa.setSpan(greenSpan, aaaa.toString().indexOf("#"),
                                aaaa.toString().lastIndexOf("#") + 1,
                                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                    SpannableString details = getExpressionString2(mContext, aaaa);
                    holder.detils.setText(details);
                    holder.detils.setMovementMethod(LinkMovementMethod
                            .getInstance());
                }
            } else {
                boolean flog = mList.get(position).getDetils().contains(" ");
                boolean flog2 = mList.get(position).getDetils().contains("#");
                if (flog && flog2) {
                    String likeUsers = mList
                            .get(position)
                            .getDetils()
                            .substring(
                                    0,
                                    mList.get(position).getDetils()
                                            .lastIndexOf("#") + 1).toString();

                    String listRemove = mList.get(position).getDetils()
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
                                    Intent intent = new Intent(mContext,
                                            ActivityInfoWebView.class);
                                    intent.putExtra("title_name", name
                                            .toString().replace("#", ""));
                                    intent.putExtra("action_url", "");
                                    if (activityNameList.indexOf(name
                                            .toString().replace("#", "")) != -1) {
                                        intent.putExtra(
                                                "activity_id",
                                                activityIdList.get(activityNameList
                                                        .indexOf(name
                                                                .toString()
                                                                .replace("#",
                                                                        ""))));

                                        ActionList action = actionLists
                                                .get(activityNameList
                                                        .indexOf(name
                                                                .toString()
                                                                .replace("#",
                                                                        "")));
                                        activityTime_2 = action
                                                .getActionTime()
                                                .substring(
                                                        action.getActionTime()
                                                                .indexOf("-") + 1,
                                                        action.getActionTime()
                                                                .length())
                                                .replace(".", "-");
                                        activityTime_2 = activityTime_2
                                                .replace("-", "");
                                        int b = Integer.valueOf(activityTime_2)
                                                .intValue() + 1;
                                        activityTime_2 = b + "";
                                        String year = activityTime_2.substring(
                                                0, 4);
                                        String month = activityTime_2
                                                .substring(4, 6);
                                        String day = activityTime_2.substring(
                                                6, 8);
                                        activityTime_2 = year + "-" + month
                                                + "-" + day;
                                        intent.putExtra("activitytime",
                                                activityTime_2);

                                        mContext.startActivity(intent);
                                    }
                                }

                                @Override
                                public void updateDrawState(TextPaint ds) {
                                    super.updateDrawState(ds);
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
                        aaaa.setSpan(greenSpan, aaaa.toString().indexOf("#"),
                                aaaa.toString().lastIndexOf("#") + 1,
                                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                    SpannableString details = getExpressionString2(mContext, aaaa);
                    holder.detils.setText(details);
                    holder.detils.setMovementMethod(LinkMovementMethod
                            .getInstance());
                }
                holder.detils.setVisibility(View.VISIBLE);
            }
        } else {
            holder.detils.setText(" ");
        }

        if (mList.size() != 0 && mList.get(position).getDetils() != null
                && !"".equals(mList.get(position).getDetils())) {
            mList.get(position).setDetils(mList.get(position).getDetils().replaceAll("\\[", "["));
            if (mList.get(position).getDetils().length() > 100) {
                holder.tx_xianshiquanbu.setVisibility(View.VISIBLE);
                holder.tx_xianshiquanbu.setText("全文");
                SpannableString details = getExpressionString3(mContext, mList.get(position)
                        .getDetils().subSequence(0, 100).toString());
                holder.detils.setText(details + "......");
                if (holder.detils.getText().toString() != null
                        && !"".equals(holder.detils.getText().toString())) {
                    if (holder.detils.getText().toString().lastIndexOf(" ") != 0) {
                        boolean flog = mList.get(position).getDetils()
                                .contains(" ");
                        boolean flog2 = mList.get(position).getDetils()
                                .contains("#");
                        if (flog && flog2) {
                            String likeUsers = holder.detils
                                    .getText()
                                    .toString()
                                    .substring(
                                            0,
                                            holder.detils.getText().toString()
                                                    .lastIndexOf("#") + 1)
                                    .toString();

                            String listRemove = holder.detils.getText()
                                    .toString().replace(likeUsers, "");
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
                                            // Toast.makeText(mContext,
                                            // name,
                                            // Toast.LENGTH_SHORT).show();

                                            Intent intent = new Intent(
                                                    mContext,
                                                    ActivityInfoWebView.class);
                                            intent.putExtra("title_name", name
                                                    .toString()
                                                    .replace("#", ""));
                                            intent.putExtra("action_url", "");
                                            if (activityNameList.indexOf(name
                                                    .toString()
                                                    .replace("#", "")) != -1) {
                                                intent.putExtra(
                                                        "activity_id",
                                                        activityIdList
                                                                .get(activityNameList
                                                                        .indexOf(name
                                                                                .toString()
                                                                                .replace(
                                                                                        "#",
                                                                                        ""))));

                                                ActionList action = actionLists
                                                        .get(activityNameList
                                                                .indexOf(name
                                                                        .toString()
                                                                        .replace(
                                                                                "#",
                                                                                "")));
                                                activityTime_2 = action
                                                        .getActionTime()
                                                        .substring(
                                                                action.getActionTime()
                                                                        .indexOf(
                                                                                "-") + 1,
                                                                action.getActionTime()
                                                                        .length())
                                                        .replace(".", "-");
                                                activityTime_2 = activityTime_2
                                                        .replace("-", "");
                                                int b = Integer.valueOf(
                                                        activityTime_2)
                                                        .intValue() + 1;
                                                activityTime_2 = b + "";
                                                String year = activityTime_2
                                                        .substring(0, 4);
                                                String month = activityTime_2
                                                        .substring(4, 6);
                                                String day = activityTime_2
                                                        .substring(6, 8);
                                                activityTime_2 = year + "-"
                                                        + month + "-" + day;
                                                intent.putExtra("activitytime",
                                                        activityTime_2);
                                                mContext.startActivity(intent);
                                            }
                                        }

                                        @Override
                                        public void updateDrawState(TextPaint ds) {
                                            super.updateDrawState(ds);
                                            // ds.setColor(Color.RED); //
                                            // 设置文本颜色
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
                                    aaaa.setSpan(blackSpan, 0, aaaa.toString()
                                                    .indexOf("#"),
                                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                }
                            }
                            if (aaaa.toString().contains("#")) {
                                aaaa.setSpan(greenSpan, aaaa.toString()
                                                .indexOf("#"), aaaa.toString()
                                                .lastIndexOf("#") + 1,
                                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                            }
                            SpannableString details2 = getExpressionString2(mContext, aaaa);
                            holder.detils.setText(details2);
                            holder.detils.setMovementMethod(LinkMovementMethod
                                    .getInstance());
                        }
                    } else {
                        holder.detils.setVisibility(View.VISIBLE);
                    }
                }

                holder.tx_xianshiquanbu.setOnClickListener(new AllContent(
                        position, holder.tx_xianshiquanbu, holder.detils));
            } else {
                boolean flog = mList.get(position).getDetils().contains(" ");
                boolean flog2 = mList.get(position).getDetils().contains("#");
                if (flog && flog2) {
                    String likeUsers = mList
                            .get(position)
                            .getDetils()
                            .substring(
                                    0,
                                    mList.get(position).getDetils()
                                            .lastIndexOf("#") + 1).toString();

                    String listRemove = mList.get(position).getDetils()
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
                                    Intent intent = new Intent(mContext,
                                            ActivityInfoWebView.class);
                                    intent.putExtra("title_name", name
                                            .toString().replace("#", ""));
                                    intent.putExtra("action_url", "");
                                    if (activityNameList.indexOf(name
                                            .toString().replace("#", "")) != -1) {
                                        intent.putExtra(
                                                "activity_id",
                                                activityIdList.get(activityNameList
                                                        .indexOf(name
                                                                .toString()
                                                                .replace("#",
                                                                        ""))));

                                        ActionList action = actionLists
                                                .get(activityNameList
                                                        .indexOf(name
                                                                .toString()
                                                                .replace("#",
                                                                        "")));
                                        activityTime_2 = action
                                                .getActionTime()
                                                .substring(
                                                        action.getActionTime()
                                                                .indexOf("-") + 1,
                                                        action.getActionTime()
                                                                .length())
                                                .replace(".", "-");
                                        activityTime_2 = activityTime_2
                                                .replace("-", "");
                                        int b = Integer.valueOf(activityTime_2)
                                                .intValue() + 1;
                                        activityTime_2 = b + "";
                                        String year = activityTime_2.substring(
                                                0, 4);
                                        String month = activityTime_2
                                                .substring(4, 6);
                                        String day = activityTime_2.substring(
                                                6, 8);
                                        activityTime_2 = year + "-" + month
                                                + "-" + day;
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
                        aaaa.setSpan(greenSpan, aaaa.toString().indexOf("#"),
                                aaaa.toString().lastIndexOf("#") + 1,
                                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                    SpannableString details = getExpressionString2(mContext, aaaa);
                    holder.detils.setText(details);
                    holder.detils.setMovementMethod(LinkMovementMethod
                            .getInstance());
                } else {
                    SpannableString details = getExpressionString3(mContext, mList.get(position).getDetils());
                    holder.detils.setText(details);
                }
                holder.tx_xianshiquanbu.setVisibility(View.GONE);
            }
        } else {
            holder.tx_xianshiquanbu.setVisibility(View.GONE);
            holder.detils.setText(" ");
        }
        //当文字为空或者是为空格的事就不要显示
        if (mList.size() != 0 && mList.get(position).getDetils().length() == 0 ||
                mList.get(position).getDetils().equals(" ") ||
                mList.get(position).getDetils().equals("  ")) {
            holder.detils.setVisibility(View.GONE);
        } else {
            holder.detils.setVisibility(View.VISIBLE);
        }
        if (adapter_type != 1) {
            holder.list_user_guanzhu.setVisibility(View.GONE);
            holder.list_user_guanzhu_addone.setVisibility(View.GONE);
        }
        return convertView;
    }else{
            return null;
        }
    }


    /**
     * @author loujungang 加1点赞效果
     */
    class AddAnimian implements View.OnClickListener {
        private View view;
        private int position;
        private TextView goodTextView;

        public AddAnimian(View view, int position) {
            this.view = view;
            this.position = position;
        }

        public AddAnimian(View view, int position, TextView goodTextView) {
            this.view = view;
            this.position = position;
            this.goodTextView = goodTextView;
        }

        @Override
        public void onClick(final View v) {
            // TODO Auto-generated method stub
            switch (v.getId()) {
                case R.id.list_user_zan:
                case R.id.list_user_zan_layout:
                    final int theBeforeGoodPeople = mList.get(position)
                            .getGoodpeople();
                    view.setVisibility(View.VISIBLE);
                    final TextView textView = (TextView) view;
                    if (mSportsApp.isOpenNetwork()) {
                        new AsyncTask<Void, Void, ApiBack>() {
                            @Override
                            protected void onPreExecute() {
                                // TODO Auto-generated method stub
                                if (mList.get(position).isGood()) {
                                    textView.setText("-1");

                                } else {
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
                                            mList.get(position).getFindId());
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
                                } else {
                                    ArrayList<PraiseUsers> getpArrayList = mList
                                            .get(position).getpArrayList();
                                    // 成功
                                    if (mList.get(position).isGood()) {
                                        mList.get(position).setGood(false);
                                        mList.get(position).setGoodpeople(
                                                theBeforeGoodPeople - 1);
                                        PraiseUsers pUsers = new PraiseUsers();
                                        pUsers.setUid(detail.getUid() + "");
                                        pUsers.setName(detail.getUname());
                                        pUsers.setImg(detail.getUimg());
                                        pUsers.setSex(detail.getSex());
                                        ArrayList<PraiseUsers> list = mList.get(
                                                position).getpArrayList();
                                        if (pUsers != null) {
                                            list.remove(pUsers);
                                        }
                                        mList.get(position).setpArrayList(list);
                                    } else {
                                        mList.get(position).setGood(true);
                                        mList.get(position).setGoodpeople(
                                                theBeforeGoodPeople + 1);
                                        PraiseUsers pUsers = new PraiseUsers();
                                        pUsers.setUid(detail.getUid() + "");
                                        pUsers.setName(detail.getUname());
                                        pUsers.setImg(detail.getUimg());
                                        pUsers.setSex(detail.getSex());
                                        ArrayList<PraiseUsers> list = mList.get(
                                                position).getpArrayList();
                                        if (pUsers != null) {
                                            list.add(0, pUsers);
                                        }

                                        mList.get(position).setpArrayList(list);
                                    }
                                    view.startAnimation(animation);
                                    new Handler().postDelayed(new Runnable() {
                                        public void run() {
                                            view.setVisibility(View.GONE);

                                            if (mList.get(position).getGoodpeople() > 0) {
                                                if (mList.get(position).isGood()) {
                                                    goodTextView
                                                            .setCompoundDrawables(
                                                                    drawable2,
                                                                    null, null,
                                                                    null);
                                                } else {
                                                    goodTextView
                                                            .setCompoundDrawables(
                                                                    drawable1,
                                                                    null, null,
                                                                    null);
                                                }
                                                goodTextView.setText("("
                                                        + mList.get(position)
                                                        .getGoodpeople()
                                                        + ")");
                                                showZanImage(mList.get(position)
                                                        .getpArrayList(), position);

                                            } else {
                                                goodTextView
                                                        .setCompoundDrawables(
                                                                drawable1, null,
                                                                null, null);
                                                goodTextView.setText("(0)");
                                            }
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
                    if (Utils.isFastClick(3000)) {
                        return;
                    }
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
                                    back = ApiJsonParser.follow(mSportsApp.getSessionId(),
                                                    mList.get(position)
                                                            .getOtheruid(), 1, 1);
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
                                } else {
                                    // 成功
                                    mList.get(position).setIsFriends(1);
                                    final Button button = (Button) view;
                                    //button.setText("已关注");
                                    view.startAnimation(animation);
                                    new Handler().postDelayed(new Runnable() {
                                        public void run() {
                                            v.setVisibility(View.GONE);
                                            for (int i = 0; i < mList.size(); i++) {
                                                if (mList.get(i).getOtheruid() == mList
                                                        .get(position)
                                                        .getOtheruid()) {
                                                    mList.get(i).setIsFriends(1);
                                                }
                                            }
//										button.setText("+关注");
                                            notifyDataSetChanged();
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
                        new AsyncTask<Void, Void, ApiBack>() {
                            @Override
                            protected ApiBack doInBackground(Void... params) {
                                // TODO Auto-generated method stub
                                ApiBack back = null;
                                try {
                                    try {
                                        back = (ApiBack) ApiJsonParser.sendprimsg(
                                                mSportsApp.getSessionId(), mList.get(position).getOtheruid(),
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
                                    priMsg.setTouid(mList.get(position).getOtheruid());
                                    priMsg.setOwnerid(mSportsApp.getSportUser().getUid());
                                    long sava = mSportsApp.getSportsDAO().savePrivateMsgInfo(priMsg);
                                    Log.i("SSSS", sava + "");
                                    UserPrimsgAll userPrimsgAll = new UserPrimsgAll();
                                    userPrimsgAll.setAddTime(priMsg.getAddTime());
                                    userPrimsgAll.setName(mList.get(position).getOthername());
                                    userPrimsgAll.setUid(priMsg.getTouid());
                                    userPrimsgAll.setTouid(mSportsApp.getSportUser().getUid());
                                    userPrimsgAll.setUimg(mList.get(position).getOtherimg());
                                    userPrimsgAll.setBirthday("");//应传出生年月日
                                    userPrimsgAll.setCounts(0);
                                    userPrimsgAll.setSex(mList.get(position).getSex());
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
                    }

                    break;

            }

        }

    }

    class AddAnimian2 implements View.OnClickListener {
        private View view;
        private int position;
        private TextView goodTextView;
        private RoundedImage otherImage;
        private RoundedImage dianzan_one;
        private RoundedImage dianzan_two;
        private RoundedImage dianzan_three;
        private RoundedImage dianzan_four;
        private RoundedImage dianzan_five;
        private RoundedImage dianzan_six;
        private ImageView zan_more_icon;
        private RelativeLayout activity_zan_layout;
        private LinearLayout ll_dianzanimage;

        public AddAnimian2(View view, int position) {
            this.view = view;
            this.position = position;
        }

        public AddAnimian2(View view, int position, TextView goodTextView,
                           RoundedImage otherImage, RoundedImage dianzan_one,
                           RoundedImage dianzan_two, RoundedImage dianzan_three,
                           RoundedImage dianzan_four, RoundedImage dianzan_five,
                           RoundedImage dianzan_six, ImageView zan_more_icon,
                           RelativeLayout activity_zan_layout) {
            this.view = view;
            this.position = position;
            this.goodTextView = goodTextView;
            this.otherImage = otherImage;
            this.dianzan_one = dianzan_one;
            this.dianzan_two = dianzan_two;
            this.dianzan_three = dianzan_three;
            this.dianzan_four = dianzan_four;
            this.dianzan_five = dianzan_five;
            this.dianzan_six = dianzan_six;
            this.zan_more_icon = zan_more_icon;
            this.activity_zan_layout = activity_zan_layout;
        }

        @Override
        public void onClick(final View v) {
            // TODO Auto-generated method stub
            switch (v.getId()) {
                case R.id.list_user_zan_layout:
                    if (Utils.isFastClick(3000)) {
                        return;
                    }
                    final int theBeforeGoodPeople = mList.get(position)
                            .getGoodpeople();
                    view.setVisibility(View.VISIBLE);
                    final TextView textView = (TextView) view;
                    if (mSportsApp.isOpenNetwork()) {
                        new AsyncTask<Void, Void, ApiBack>() {
                            @Override
                            protected void onPreExecute() {
                                // TODO Auto-generated method stub
                                if (mList.get(position).isGood()) {
                                    textView.setText("-1");
                                } else {
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
                                            mList.get(position).getFindId());
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
                                } else {
                                    ArrayList<PraiseUsers> getpArrayList = mList
                                            .get(position).getpArrayList();
                                    // 成功
                                    if (mList.get(position).isGood()) {
                                        mList.get(position).setGood(false);
                                        mList.get(position).setGoodpeople(
                                                theBeforeGoodPeople - 1);
                                        PraiseUsers pUsers = new PraiseUsers();
                                        pUsers.setUid(detail.getUid() + "");
                                        pUsers.setName(detail.getUname());
                                        pUsers.setImg(detail.getUimg());
                                        pUsers.setSex(detail.getSex());
                                        ArrayList<PraiseUsers> list = mList.get(
                                                position).getpArrayList();
                                        if (list != null) {
                                            for (int i = 0; i < list.size(); i++) {
                                                if (list.get(i).getUid()
                                                        .equals(pUsers.getUid())) {
                                                    list.remove(i);
                                                }
                                            }
                                        }
                                        mList.get(position).setpArrayList(list);
                                    } else {
                                        mList.get(position).setGood(true);
                                        mList.get(position).setGoodpeople(
                                                theBeforeGoodPeople + 1);
                                        PraiseUsers pUsers = new PraiseUsers();
                                        pUsers.setUid(detail.getUid() + "");
                                        pUsers.setName(detail.getUname());
                                        pUsers.setImg(detail.getUimg());
                                        pUsers.setSex(detail.getSex());
                                        ArrayList<PraiseUsers> list = mList.get(
                                                position).getpArrayList();
                                        if (list != null) {
                                            list.add(0, pUsers);
                                        } else {
                                            list = new ArrayList<PraiseUsers>();
                                            list.add(0, pUsers);
                                        }

                                        mList.get(position).setpArrayList(list);
                                    }
                                    view.startAnimation(animation);
                                    new Handler().postDelayed(new Runnable() {
                                        public void run() {
                                            view.setVisibility(View.GONE);
                                            if (mList.get(position).getGoodpeople() > 0) {
                                                activity_zan_layout
                                                        .setVisibility(View.VISIBLE);
                                                if (mList.get(position).isGood()) {
                                                    goodTextView
                                                            .setCompoundDrawables(
                                                                    drawable2,
                                                                    null, null,
                                                                    null);
                                                } else {
                                                    goodTextView
                                                            .setCompoundDrawables(
                                                                    drawable1,
                                                                    null, null,
                                                                    null);
                                                }
                                                goodTextView.setText("("
                                                        + mList.get(position)
                                                        .getGoodpeople()
                                                        + ")");
                                                activity_zan_layout
                                                        .setVisibility(View.GONE);
                                                dianzan_one
                                                        .setVisibility(View.GONE);
                                                dianzan_two
                                                        .setVisibility(View.GONE);
                                                dianzan_three
                                                        .setVisibility(View.GONE);
                                                dianzan_four
                                                        .setVisibility(View.GONE);
                                                dianzan_five
                                                        .setVisibility(View.GONE);
                                                dianzan_six
                                                        .setVisibility(View.GONE);
                                                if (mList.get(position).getpArrayList().size() <= 0){
                                                    activity_zan_layout
                                                            .setVisibility(View.GONE);
                                                    dianzan_one
                                                            .setVisibility(View.INVISIBLE);
                                                    dianzan_two
                                                            .setVisibility(View.INVISIBLE);
                                                    dianzan_three
                                                            .setVisibility(View.INVISIBLE);
                                                    dianzan_four
                                                            .setVisibility(View.INVISIBLE);
                                                    dianzan_five
                                                            .setVisibility(View.INVISIBLE);
                                                    dianzan_six
                                                            .setVisibility(View.INVISIBLE);
                                                }else{

                                                if (mList.get(position)
                                                        .getpArrayList().size() == 0) {
                                                    activity_zan_layout
                                                            .setVisibility(View.GONE);
                                                    dianzan_one
                                                            .setVisibility(View.INVISIBLE);
                                                    dianzan_two
                                                            .setVisibility(View.INVISIBLE);
                                                    dianzan_three
                                                            .setVisibility(View.INVISIBLE);
                                                    dianzan_four
                                                            .setVisibility(View.INVISIBLE);
                                                    dianzan_five
                                                            .setVisibility(View.INVISIBLE);
                                                    dianzan_six
                                                            .setVisibility(View.INVISIBLE);
                                                } else if (mList.get(position)
                                                        .getpArrayList().size() == 1) {
                                                    activity_zan_layout
                                                            .setVisibility(View.VISIBLE);
                                                    dianzan_one
                                                            .setVisibility(View.VISIBLE);
                                                    setRoudImage(
                                                            mList.get(position)
                                                                    .getpArrayList()
                                                                    .get(0)
                                                                    .getImg(),
                                                            dianzan_one,
                                                            mList.get(position)
                                                                    .getpArrayList()
                                                                    .get(0)
                                                                    .getSex());
                                                    dianzan_one
                                                            .setOnClickListener(new personalOnClickListener(
                                                                    Integer.parseInt(mList
                                                                            .get(position)
                                                                            .getpArrayList()
                                                                            .get(0)
                                                                            .getUid())));
                                                } else if (mList.get(position)
                                                        .getpArrayList().size() == 2) {
                                                    activity_zan_layout
                                                            .setVisibility(View.VISIBLE);
                                                    dianzan_one
                                                            .setVisibility(View.VISIBLE);
                                                    dianzan_two
                                                            .setVisibility(View.VISIBLE);
                                                    setRoudImage(
                                                            mList.get(position)
                                                                    .getpArrayList()
                                                                    .get(0)
                                                                    .getImg(),
                                                            dianzan_one,
                                                            mList.get(position)
                                                                    .getpArrayList()
                                                                    .get(0)
                                                                    .getSex());
                                                    setRoudImage(
                                                            mList.get(position)
                                                                    .getpArrayList()
                                                                    .get(1)
                                                                    .getImg(),
                                                            dianzan_two,
                                                            mList.get(position)
                                                                    .getpArrayList()
                                                                    .get(1)
                                                                    .getSex());
                                                    dianzan_one
                                                            .setOnClickListener(new personalOnClickListener(
                                                                    Integer.parseInt(mList
                                                                            .get(position)
                                                                            .getpArrayList()
                                                                            .get(0)
                                                                            .getUid())));
                                                    dianzan_two
                                                            .setOnClickListener(new personalOnClickListener(
                                                                    Integer.parseInt(mList
                                                                            .get(position)
                                                                            .getpArrayList()
                                                                            .get(1)
                                                                            .getUid())));
                                                } else if (mList.get(position)
                                                        .getpArrayList().size() == 3) {
                                                    activity_zan_layout
                                                            .setVisibility(View.VISIBLE);
                                                    dianzan_one
                                                            .setVisibility(View.VISIBLE);
                                                    dianzan_two
                                                            .setVisibility(View.VISIBLE);
                                                    dianzan_three
                                                            .setVisibility(View.VISIBLE);
                                                    setRoudImage(
                                                            mList.get(position)
                                                                    .getpArrayList()
                                                                    .get(0)
                                                                    .getImg(),
                                                            dianzan_one,
                                                            mList.get(position)
                                                                    .getpArrayList()
                                                                    .get(0)
                                                                    .getSex());
                                                    setRoudImage(
                                                            mList.get(position)
                                                                    .getpArrayList()
                                                                    .get(1)
                                                                    .getImg(),
                                                            dianzan_two,
                                                            mList.get(position)
                                                                    .getpArrayList()
                                                                    .get(1)
                                                                    .getSex());
                                                    setRoudImage(
                                                            mList.get(position)
                                                                    .getpArrayList()
                                                                    .get(2)
                                                                    .getImg(),
                                                            dianzan_three,
                                                            mList.get(position)
                                                                    .getpArrayList()
                                                                    .get(2)
                                                                    .getSex());
                                                    dianzan_one
                                                            .setOnClickListener(new personalOnClickListener(
                                                                    Integer.parseInt(mList
                                                                            .get(position)
                                                                            .getpArrayList()
                                                                            .get(0)
                                                                            .getUid())));
                                                    dianzan_two
                                                            .setOnClickListener(new personalOnClickListener(
                                                                    Integer.parseInt(mList
                                                                            .get(position)
                                                                            .getpArrayList()
                                                                            .get(1)
                                                                            .getUid())));
                                                    dianzan_three
                                                            .setOnClickListener(new personalOnClickListener(
                                                                    Integer.parseInt(mList
                                                                            .get(position)
                                                                            .getpArrayList()
                                                                            .get(2)
                                                                            .getUid())));
                                                } else if (mList.get(position)
                                                        .getpArrayList().size() == 4) {
                                                    activity_zan_layout
                                                            .setVisibility(View.VISIBLE);
                                                    dianzan_one
                                                            .setVisibility(View.VISIBLE);
                                                    dianzan_two
                                                            .setVisibility(View.VISIBLE);
                                                    dianzan_three
                                                            .setVisibility(View.VISIBLE);
                                                    dianzan_four
                                                            .setVisibility(View.VISIBLE);
                                                    setRoudImage(
                                                            mList.get(position)
                                                                    .getpArrayList()
                                                                    .get(0)
                                                                    .getImg(),
                                                            dianzan_one,
                                                            mList.get(position)
                                                                    .getpArrayList()
                                                                    .get(0)
                                                                    .getSex());
                                                    setRoudImage(
                                                            mList.get(position)
                                                                    .getpArrayList()
                                                                    .get(1)
                                                                    .getImg(),
                                                            dianzan_two,
                                                            mList.get(position)
                                                                    .getpArrayList()
                                                                    .get(1)
                                                                    .getSex());
                                                    setRoudImage(
                                                            mList.get(position)
                                                                    .getpArrayList()
                                                                    .get(2)
                                                                    .getImg(),
                                                            dianzan_three,
                                                            mList.get(position)
                                                                    .getpArrayList()
                                                                    .get(2)
                                                                    .getSex());
                                                    setRoudImage(
                                                            mList.get(position)
                                                                    .getpArrayList()
                                                                    .get(3)
                                                                    .getImg(),
                                                            dianzan_four,
                                                            mList.get(position)
                                                                    .getpArrayList()
                                                                    .get(3)
                                                                    .getSex());
                                                    dianzan_one
                                                            .setOnClickListener(new personalOnClickListener(
                                                                    Integer.parseInt(mList
                                                                            .get(position)
                                                                            .getpArrayList()
                                                                            .get(0)
                                                                            .getUid())));
                                                    dianzan_two
                                                            .setOnClickListener(new personalOnClickListener(
                                                                    Integer.parseInt(mList
                                                                            .get(position)
                                                                            .getpArrayList()
                                                                            .get(1)
                                                                            .getUid())));
                                                    dianzan_three
                                                            .setOnClickListener(new personalOnClickListener(
                                                                    Integer.parseInt(mList
                                                                            .get(position)
                                                                            .getpArrayList()
                                                                            .get(2)
                                                                            .getUid())));
                                                    dianzan_four
                                                            .setOnClickListener(new personalOnClickListener(
                                                                    Integer.parseInt(mList
                                                                            .get(position)
                                                                            .getpArrayList()
                                                                            .get(3)
                                                                            .getUid())));
                                                } else if (mList.get(position)
                                                        .getpArrayList().size() == 5) {
                                                    activity_zan_layout
                                                            .setVisibility(View.VISIBLE);
                                                    dianzan_one
                                                            .setVisibility(View.VISIBLE);
                                                    dianzan_two
                                                            .setVisibility(View.VISIBLE);
                                                    dianzan_three
                                                            .setVisibility(View.VISIBLE);
                                                    dianzan_four
                                                            .setVisibility(View.VISIBLE);
                                                    dianzan_five
                                                            .setVisibility(View.VISIBLE);
                                                    setRoudImage(
                                                            mList.get(position)
                                                                    .getpArrayList()
                                                                    .get(0)
                                                                    .getImg(),
                                                            dianzan_one,
                                                            mList.get(position)
                                                                    .getpArrayList()
                                                                    .get(0)
                                                                    .getSex());
                                                    setRoudImage(
                                                            mList.get(position)
                                                                    .getpArrayList()
                                                                    .get(1)
                                                                    .getImg(),
                                                            dianzan_two,
                                                            mList.get(position)
                                                                    .getpArrayList()
                                                                    .get(1)
                                                                    .getSex());
                                                    setRoudImage(
                                                            mList.get(position)
                                                                    .getpArrayList()
                                                                    .get(2)
                                                                    .getImg(),
                                                            dianzan_three,
                                                            mList.get(position)
                                                                    .getpArrayList()
                                                                    .get(2)
                                                                    .getSex());
                                                    setRoudImage(
                                                            mList.get(position)
                                                                    .getpArrayList()
                                                                    .get(3)
                                                                    .getImg(),
                                                            dianzan_four,
                                                            mList.get(position)
                                                                    .getpArrayList()
                                                                    .get(3)
                                                                    .getSex());
                                                    setRoudImage(
                                                            mList.get(position)
                                                                    .getpArrayList()
                                                                    .get(4)
                                                                    .getImg(),
                                                            dianzan_five,
                                                            mList.get(position)
                                                                    .getpArrayList()
                                                                    .get(4)
                                                                    .getSex());
                                                    dianzan_one
                                                            .setOnClickListener(new personalOnClickListener(
                                                                    Integer.parseInt(mList
                                                                            .get(position)
                                                                            .getpArrayList()
                                                                            .get(0)
                                                                            .getUid())));
                                                    dianzan_two
                                                            .setOnClickListener(new personalOnClickListener(
                                                                    Integer.parseInt(mList
                                                                            .get(position)
                                                                            .getpArrayList()
                                                                            .get(1)
                                                                            .getUid())));
                                                    dianzan_three
                                                            .setOnClickListener(new personalOnClickListener(
                                                                    Integer.parseInt(mList
                                                                            .get(position)
                                                                            .getpArrayList()
                                                                            .get(2)
                                                                            .getUid())));
                                                    dianzan_four
                                                            .setOnClickListener(new personalOnClickListener(
                                                                    Integer.parseInt(mList
                                                                            .get(position)
                                                                            .getpArrayList()
                                                                            .get(3)
                                                                            .getUid())));
                                                    dianzan_five
                                                            .setOnClickListener(new personalOnClickListener(
                                                                    Integer.parseInt(mList
                                                                            .get(position)
                                                                            .getpArrayList()
                                                                            .get(4)
                                                                            .getUid())));
                                                } else if (mList.get(position)
                                                        .getpArrayList().size() >= 6) {
                                                    activity_zan_layout
                                                            .setVisibility(View.VISIBLE);
                                                    dianzan_one
                                                            .setVisibility(View.VISIBLE);
                                                    dianzan_two
                                                            .setVisibility(View.VISIBLE);
                                                    dianzan_three
                                                            .setVisibility(View.VISIBLE);
                                                    dianzan_four
                                                            .setVisibility(View.VISIBLE);
                                                    dianzan_five
                                                            .setVisibility(View.VISIBLE);
                                                    dianzan_six
                                                            .setVisibility(View.VISIBLE);
                                                    setRoudImage(
                                                            mList.get(position)
                                                                    .getpArrayList()
                                                                    .get(0)
                                                                    .getImg(),
                                                            dianzan_one,
                                                            mList.get(position)
                                                                    .getpArrayList()
                                                                    .get(0)
                                                                    .getSex());
                                                    setRoudImage(
                                                            mList.get(position)
                                                                    .getpArrayList()
                                                                    .get(1)
                                                                    .getImg(),
                                                            dianzan_two,
                                                            mList.get(position)
                                                                    .getpArrayList()
                                                                    .get(1)
                                                                    .getSex());
                                                    setRoudImage(
                                                            mList.get(position)
                                                                    .getpArrayList()
                                                                    .get(2)
                                                                    .getImg(),
                                                            dianzan_three,
                                                            mList.get(position)
                                                                    .getpArrayList()
                                                                    .get(2)
                                                                    .getSex());
                                                    setRoudImage(
                                                            mList.get(position)
                                                                    .getpArrayList()
                                                                    .get(3)
                                                                    .getImg(),
                                                            dianzan_four,
                                                            mList.get(position)
                                                                    .getpArrayList()
                                                                    .get(3)
                                                                    .getSex());
                                                    setRoudImage(
                                                            mList.get(position)
                                                                    .getpArrayList()
                                                                    .get(4)
                                                                    .getImg(),
                                                            dianzan_five,
                                                            mList.get(position)
                                                                    .getpArrayList()
                                                                    .get(4)
                                                                    .getSex());
                                                    setRoudImage(
                                                            mList.get(position)
                                                                    .getpArrayList()
                                                                    .get(5)
                                                                    .getImg(),
                                                            dianzan_six,
                                                            mList.get(position)
                                                                    .getpArrayList()
                                                                    .get(5)
                                                                    .getSex());
                                                    dianzan_one
                                                            .setOnClickListener(new personalOnClickListener(
                                                                    Integer.parseInt(mList
                                                                            .get(position)
                                                                            .getpArrayList()
                                                                            .get(0)
                                                                            .getUid())));
                                                    dianzan_two
                                                            .setOnClickListener(new personalOnClickListener(
                                                                    Integer.parseInt(mList
                                                                            .get(position)
                                                                            .getpArrayList()
                                                                            .get(1)
                                                                            .getUid())));
                                                    dianzan_three
                                                            .setOnClickListener(new personalOnClickListener(
                                                                    Integer.parseInt(mList
                                                                            .get(position)
                                                                            .getpArrayList()
                                                                            .get(2)
                                                                            .getUid())));
                                                    dianzan_four
                                                            .setOnClickListener(new personalOnClickListener(
                                                                    Integer.parseInt(mList
                                                                            .get(position)
                                                                            .getpArrayList()
                                                                            .get(3)
                                                                            .getUid())));
                                                    dianzan_five
                                                            .setOnClickListener(new personalOnClickListener(
                                                                    Integer.parseInt(mList
                                                                            .get(position)
                                                                            .getpArrayList()
                                                                            .get(4)
                                                                            .getUid())));
                                                    dianzan_six
                                                            .setOnClickListener(new personalOnClickListener(
                                                                    Integer.parseInt(mList
                                                                            .get(position)
                                                                            .getpArrayList()
                                                                            .get(5)
                                                                            .getUid())));
                                                    if (mList.get(position)
                                                            .getpArrayList().size() > 6) {
                                                        zan_more_icon
                                                                .setVisibility(View.VISIBLE);
                                                        zan_more_icon
                                                                .setOnClickListener(new OnClickListener() {

                                                                    @Override
                                                                    public void onClick(
                                                                            View arg0) {
                                                                        // TODO
                                                                        Intent intent = new Intent(
                                                                                mContext,
                                                                                ZanListUserActivity.class);
                                                                        intent.putExtra(
                                                                                "find_id",
                                                                                Integer.parseInt(mList
                                                                                        .get(position)
                                                                                        .getFindId()));
                                                                        intent.putExtra(
                                                                                "uid",
                                                                                mList.get(
                                                                                        position)
                                                                                        .getOtheruid());
                                                                        mContext.startActivity(intent);
                                                                    }
                                                                });
                                                    }
                                                }else{
                                                    activity_zan_layout.setVisibility(View.GONE);
                                                  }
                                                }

                                            } else {
                                                goodTextView
                                                        .setCompoundDrawables(
                                                                drawable1, null,
                                                                null, null);
                                                goodTextView.setText("(0)");
                                                dianzan_one
                                                        .setVisibility(View.INVISIBLE);
                                                activity_zan_layout
                                                        .setVisibility(View.GONE);
                                            }
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
            }

        }

    }

    private void setImageVoid(int type, int position, List<ImageView> imgList) {
        for (int i = 0; i < type; i++) {
            LinearLayout.LayoutParams lps = (LinearLayout.LayoutParams) imgList
                    .get(i).getLayoutParams();
            lps.width = (int) (SportsApp.ScreenWidth * 0.635) / 2;
            lps.height = (int) (SportsApp.ScreenWidth * 0.635) / 2;
            imgList.get(i).setLayoutParams(lps);
            mImageWorker.loadImage(urls[i], imgList.get(i), null, null, false);
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
                Toast.makeText(mContext, "网络未连接，请检查网络！", Toast.LENGTH_LONG)
                        .show();
                return;
            }
            Intent intent = new Intent(mContext, PersonalPageMainActivity.class);
            intent.putExtra("ID", userId);
            mContext.startActivity(intent);
        }
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

        public commentOnClickListener(int list_position, int i,
                                      String findIdString, String commentIdString, String theFirstName) {
            this.position = list_position;
            this.comment_number = i;
            this.findID = findIdString;
            this.commentID = commentIdString;
            this.theFirstName = theFirstName;
        }

        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub
            //自己发表的评论
            if (detail.getUname().equals(theFirstName)) {
                showDialogs(position, comment_number, commentID);//改变样式去掉取消，直接留下删除。放入长按事件里面。
            } else {
                /**
                 * (position,findIdString,2,commentID,theFirstName)
                 * 第一个参数所属发现在list中位置
                 * 第二个参数发现ID，
                 * 第三个参数用来判断评论还是回复1代表直接评论2代表回复
                 * 第四个参数用来当回复的时候传递to_id,如果直接评论则为null
                 * 第五个参数用来当回复的时候显示回复给谁，如果直接评论则null
                 */
                if (mSportsApp.mIsAdmin) {
                    //管理员有权利删除评论的权限
                    showAdminDialogs(position, comment_number, commentID,
                            findID, theFirstName);
                } else {
                    //既不是管理员也不是自己发表的评论，只能回复或者不回复。
                    changeCommentListener.OnCheckedChangeListener(position,
                            findID, 2, commentID, theFirstName);
                }
            }
        }
    }
    // 3.4版本监听评论的长按点击事件
    class commentOnClickListener2 implements View.OnLongClickListener {
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

        public commentOnClickListener2(int list_position, int i,
                                      String findIdString, String commentIdString, String theFirstName) {
            this.position = list_position;
            this.comment_number = i;
            this.findID = findIdString;
            this.commentID = commentIdString;
            this.theFirstName = theFirstName;
        }

        @Override
        public boolean onLongClick(View arg0) {
            // TODO Auto-generated method stub
            //自己发表的评论
            if (detail.getUname().equals(theFirstName)) {
                showDialogs2(position, comment_number, commentID);
            } else {
                if (mSportsApp.mIsAdmin) {
                    ///弹出删除对话框
                    showDialogs2(position, comment_number, commentID);
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
                changeCommentListener.OnCheckedChangeListener(position,
                        findID, 2, commentID, theFirstName);
            }
        }
    }

    // 上传评论
    public void send(final int position, final String theFirstName,
                     final String findID, final String toID, final String commentText,
                     final String commentWav, final String wavDuration) {
        /**
         * 第一个参数是评论所属发现在list中的位置 第二个参数用来当回复的时候显示回复给谁，如果直接评论则null 第三个发现ID
         * 第四个用来当回复的时候传递to_id,如果直接评论则为null 其余三个分别是文本内容，音频，音频时间
         * */
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
                    waitProgressDialog.dismiss();
                    SportCircleComments fc = new SportCircleComments();
                    fc.setId(commentId);
                    fc.setName(detail.getUname());
                    if (theFirstName != null && !theFirstName.equals("")) {
                        fc.setTo_name(theFirstName);
                    }
                    fc.setContent(commentText);
                    fc.setWav(commentWav);
                    fc.setWavtime(wavDuration);

                    if (mSportsApp.getFindId() != null){
                        SportCircleComments fc3 = new SportCircleComments();
                        fc3.setId(" ");
                        fc3.setInputtime(System.currentTimeMillis() / 1000 + "");
                        if (mSportsApp.getSportUser().getSex().equals("1")) {
                            fc3.setSex("1");
                        } else {
                            fc3.setSex("0");
                        }

                        fc3.setName(detail.getUname());
                        if (mSportsApp.getToName() != null && !mSportsApp.getToName().equals("")) {
                            if (! mSportsApp.getToName().equals(detail.getUname())) {
                                fc3.setTo_name(mSportsApp.getToName());
                                mSportsApp.setToName(null);
                            }
                        }
                        fc3.setImg(detail.getUimg());
                        fc3.setContent(mSportsApp.getTextString());
                        fc3.setWav(mSportsApp.getWav());
                        fc3.setWavtime(mSportsApp.getWavtime());
                        if (fc3 != null && mList.get(position).getTalkdetils() != null) {
                            mList.get(position).getTalkdetils().add(0, fc3);
                        }
                    }


                    SportCircleComments fc2 = new SportCircleComments();
                    fc2.setId(commentId);
                    fc2.setInputtime(System.currentTimeMillis() / 1000 + "");
                    if (mSportsApp.getSportUser().getSex().equals("1") || mSportsApp.getSportUser().getSex().equals("man")) {
                        fc2.setSex("1");
                    } else {
                        fc2.setSex("0");
                    }

                    fc2.setName(detail.getUname());
                    if (theFirstName != null && !theFirstName.equals("")) {
                        fc2.setTo_name(theFirstName);
                    }
                    fc2.setImg(detail.getUimg());
                    fc2.setContent(commentText);
                    fc2.setWav(commentWav);
                    fc2.setWavtime(wavDuration);

                    if (mList.get(position).getTalkdetils() == null) {
                        ArrayList<SportCircleComments> fList = new ArrayList<SportCircleComments>();
                        mList.get(position).setTalkdetils(fList);
                    }
                    mList.get(position).getTalkdetils().add(0, fc2);
                    int beforeCommentNum = mList.get(position).getcCount();
                    mList.get(position).setcCount(beforeCommentNum + 1);
                    notifyDataSetChanged();
                    // Toast.makeText(
                    // mContext,
                    // mContext.getResources().getString(
                    // R.string.upload_success),
                    // Toast.LENGTH_SHORT).show();
                }
            }

        }.execute();
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
            for (int i = 0; i < urlString.length; i++) {
            }
            Intent intent = new Intent(mContext, SportsFoundImgActivity.class);
            Bundle bundle = new Bundle();
            bundle.putStringArray("urlString", urlString);
            bundle.putInt("index", index);
            intent.putExtras(bundle);
            mContext.startActivity(intent);
        }
    }

    private class ViewHolder {
        private RoundedImage otherImage, dianzan_one, dianzan_two,
                dianzan_three, dianzan_four, dianzan_five, dianzan_six;
        private ImageView zan_more_icon;
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
        // private TextView deleteText;
        // 点赞和评论
        private TextView goodandtext;
        // 点赞人数text及所在layout
        private TextView goodPeopole;// 赞的数量
        private TextView textPeopole;

        private LinearLayout goodLayout;
        // 赞和评论整体
        private LinearLayout commentLayout;
        // 赞和评论下面的横线
        private View textgoodline;
        // 评论人数text及所在layout
        private LinearLayout textLayout;// 评论数量
        // 评论内容
        private LinearLayout talkdetilsLayout;

        private Button list_user_guanzhu;// 关注按钮
        private TextView list_user_guanzhu_addone;// 已关注动画按钮
        private TextView list_user_fenxiang;// 分享
        private TextView tx_sport_address;// 发布地址信息
        private TextView tx_sport_huodong;// 发布活动信息
        private TextView tx_sport_days;// 运动天数及公里数
        private TextView list_user_zan_addone;// 赞动画效果加1
        private ImageView is_manorwomen_icon;// 是男是女的图标
        private TextView tx_sport_del;// 删除按钮
        private TextView tx_sport_toplist;// 置顶按钮
        private TextView tx_sport_removetop;// 取消置顶按钮
        private TextView tx_xianshiquanbu;// 显示全部按钮
        private ImageView iv_zhiding;// 置顶标志按钮
        private LinearLayout ll_comment_zan_shaer;// 评论、点赞和分享框架。

        private RelativeLayout activity_zan_layout, list_user_zan_layout,
                list_user_fenxiang_layout;
        private TextView list_user_pinglun_layout;
        private ImageView sportshow_shaer;
        private LinearLayout ll_dianzanimage;
    }

    // 监听删除整个item
    class deleteListener implements OnClickListener {
        // 发现ID
        private String find_id;
        // list列表中位置
        private int position;

        public deleteListener(String numlist, int position) {
            this.find_id = numlist;
            this.position = position;
        }

        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub
            showdeleteAllDialogs(find_id, position);
        }
    }

    // 监听置顶整个item
    class topListener implements OnClickListener {
        // 发现ID
        private String find_id;
        // list列表中位置
        private int position;

        public topListener(String numlist, int position) {
            this.find_id = numlist;
            this.position = position;
        }

        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub
            showtopAllDialogs(find_id, position);
        }
    }

    // 监听取消置顶整个item
    class deltopListener implements OnClickListener {
        // 发现ID
        private String find_id;
        // list列表中位置
        private int position;

        public deltopListener(String numlist, int position) {
            this.find_id = numlist;
            this.position = position;
        }

        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub
            showdeletetopAllDialogs(find_id, position);
        }
    }

    // 确认是否置顶整个item
    private void showtopAllDialogs(String find_id, final int position) {
        final String findID = find_id;
        final int list_position = position;
        alertDialog = new Dialog(mContext, R.style.sports_dialog);
        LayoutInflater sInflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = sInflater.inflate(R.layout.sports_dialog_2, null);
        Spinner sp = (Spinner) v.findViewById(R.id.sp_settime);
        final String aa[] = {"2个小时", "4个小时", "6个小时", "8个小时", "10个小时", "12个小时",
                "14个小时", "16个小时", "18个小时", "20个小时", "22个小时", "24个小时"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext,
                R.layout.auto_item3, R.id.contentTextView, aa);
        sp.setAdapter(adapter);
        sp.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                // TODO Auto-generated method stub
                if (aa[arg2].length() == 4) {
                    topTime = aa[arg2].substring(0, 1);
                } else if (aa[arg2].length() == 5) {
                    topTime = aa[arg2].substring(0, 2);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });

        TextView message = (TextView) v.findViewById(R.id.message);
        message.setText("请设置置顶时间长：");
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
                            R.string.sports_dialog_toping));
                    new AsyncTask<Void, Void, ApiBack>() {
                        @Override
                        protected ApiBack doInBackground(Void... params) {
                            // TODO Auto-generated method stub
                            ApiBack back = null;
                            try {
                                back = (ApiBack) ApiJsonParser.topFind(
                                        mSportsApp.getSessionId(), findID,
                                        topTime);
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
                                // 删除成功
                                waitProgressDialog.dismiss();
                                mList.get(list_position).setFlog(1);
                                if (mList.size() % 2 == 0) {
                                    FindGroup findGroup = mList
                                            .get(list_position);
                                    if (mList.get(0) != findGroup) {
                                        mList.add(0, findGroup);
                                        notifyDataSetChanged();
                                    }
                                } else {
                                    FindGroup findGroup = mList
                                            .get(list_position);
                                    if (mList.get(0) != findGroup) {
                                        mList.set(0, findGroup);
                                        notifyDataSetChanged();
                                    }
                                }
                                Toast.makeText(
                                        mContext,
                                        mContext.getResources().getString(
                                                R.string.sports_top_successed),
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
                                            mSportsApp.getSessionId(), mList
                                                    .get(position)
                                                    .getOtheruid(),
                                            "哎呦，您发的状态不错哦，被云狐运动选为精华置顶状态，置顶时间为"
                                                    + topTime + "小时。", "", 0);
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
                                Toast.makeText(mContext, "发送通知消息成功", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }.execute();
                } else {
                    waitProgressDialog.dismiss();
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
        v.setMinimumWidth((int) (SportsApp.ScreenWidth * 0.8));
        alertDialog.setCancelable(true);
        alertDialog.setContentView(v);
        alertDialog.show();
    }
    class FaceButtonListener implements View.OnTouchListener {
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
    // 确认是否删除自己发的评论
    private void showDialogs(int list_position, int comment_number,
                             String commentid) {
        final int position = list_position;
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
                                waitProgressDialog.dismiss();
                                mList.get(position).getTalkdetils()
                                        .remove(number);
                                int beforeCommentNum = mList.get(position)
                                        .getcCount();
                                mList.get(position).setcCount(
                                        beforeCommentNum - 1);
                                notifyDataSetChanged();
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
    private void showDialogs2(int list_position, int comment_number,
                             String commentid) {
        final int position = list_position;
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
                                mList.get(position).getTalkdetils()
                                        .remove(number);
                                int beforeCommentNum = mList.get(position)
                                        .getcCount();
                                mList.get(position).setcCount(
                                        beforeCommentNum - 1);
                                notifyDataSetChanged();
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

    public void onItemClick(int number, final int list_positon,
                            final String findIdString) {
        // TODO Auto-generated method stub
        // number为判断点赞还是评论
        final int theBeforeGoodPeople = mList.get(list_positon).getGoodpeople();
        switch (number) {
            case 0:
                if (mSportsApp.isOpenNetwork()) {

                    new AsyncTask<Void, Void, ApiBack>() {
                        @Override
                        protected void onPreExecute() {
                            // TODO Auto-generated method stub
                            if (mList.get(list_positon).isGood()) {
                                showWaitDialog(mContext.getResources().getString(
                                        R.string.cancel_wait));
                            } else {
                                showWaitDialog(mContext.getResources().getString(
                                        R.string.praise_wait));
                            }
                        }

                        @Override
                        protected ApiBack doInBackground(Void... params) {
                            // TODO Auto-generated method stub
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
                            // TODO Auto-generated method stub
                            super.onPostExecute(result);
                            if (result == null || result.getFlag() != 0) {
                                waitProgressDialog.dismiss();
                            } else {
                                // 成功
                                waitProgressDialog.dismiss();
                                if (mList.get(list_positon).isGood()) {
                                    mList.get(list_positon).setGood(false);
                                    mList.get(list_positon).setGoodpeople(
                                            theBeforeGoodPeople - 1);
                                    Toast.makeText(
                                            mContext,
                                            mContext.getResources().getString(
                                                    R.string.praise_cancel_success),
                                            Toast.LENGTH_SHORT).show();
                                } else {
                                    mList.get(list_positon).setGood(true);
                                    mList.get(list_positon).setGoodpeople(
                                            theBeforeGoodPeople + 1);
                                    Toast.makeText(
                                            mContext,
                                            mContext.getResources().getString(
                                                    R.string.praise_successed),
                                            Toast.LENGTH_SHORT).show();
                                }
                                notifyDataSetChanged();
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
            case 1:
                if (changeCommentListener != null)
                    changeCommentListener.OnCheckedChangeListener(list_positon,
                            findIdString, 1, null, null);
                // 这里是直接评论
                /**
                 * (position,findIdString,2,commentID,theFirstName)
                 * 第一个参数所属发现在list中位置
                 * 第二个参数发现ID，
                 * 第三个参数用来判断评论还是回复1代表直接评论2代表回复
                 * 第四个参数用来当回复的时候传递to_id,如果直接评论则为null
                 * 第五个参数用来当回复的时候显示回复给谁，如果直接评论则null
                 */
                break;
            default:
                break;
        }
    }

    /**
     * (position,findIdString,2,commentID,theFirstName)
     * 第一个参数所属发现在list中位置
     * 第二个参数发现ID
     * 第三个参数用来判断评论还是回复1代表直接评论2代表回复
     * 第四个参数用来当回复的时候传递to_id,如果直接评论则为null
     * 第五个参数用来当回复的时候显示回复给谁，如果直接评论则null
     */
    public interface OnCheckedChangeCommentListener {
        public void OnCheckedChangeListener(int position, String findId,
                                            int number, String to_id, String toName);
    }

    private OnCheckedChangeCommentListener changeCommentListener;

    public void setOnCheckedChangeListener(
            OnCheckedChangeCommentListener changeCommentListener) {
        this.changeCommentListener = changeCommentListener;
    }

    // 录音播放
    Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case RECORD_LOADING:
                    ImageView wavBegin = (ImageView) msg.obj;
                    wavBegin.setImageResource(+R.anim.record_loading);
                    AnimationDrawable ad1 = (AnimationDrawable) wavBegin
                            .getDrawable();
                    ad1.start();
                    break;
                case RECORD_PREPARED:
                    ((ImageView) msg.obj).setImageResource(+R.anim.record_run);
                    AnimationDrawable ad2 = (AnimationDrawable) ((ImageView) msg.obj)
                            .getDrawable();
                    ad2.start();
                    break;
                case RECORD_PAUSE:
                case RECORD_ERROR:
                case RECORD_FINISH:
                    ((ImageView) msg.obj).setImageResource(R.drawable.audio_ani_1);
                    isStart = true;
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
                    String message = (String) msg.obj;
                    Toast.makeText(mContext, message, Toast.LENGTH_LONG).show();
                    break;

            }
        }

    };

    public List<SpannableString> getExpressionString(Context context,
                                                     String str, int numcom, int firstname, int secondname) {
        List<SpannableString> list = new ArrayList<SpannableString>();
        SpannableString spanString = new SpannableString(str);
        // 正则表达式比配字符串里是否含有表情，如： 我好[开心]啊
        String zhengze = "\\[[^\\]]+\\]";
        // 通过传入的正则表达式来生成一个pattern
        Pattern sinaPatten = Pattern.compile(zhengze, Pattern.CASE_INSENSITIVE);
        try {

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
        }
        return list;
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
        }
        return spanString;
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

    // 管理员选择回复还是删除
    private void showAdminDialogs(int list_position, int comment_number,
                                  String commentid, String findid, String thefirstname) {
        final int position = list_position;
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
                                        waitProgressDialog.dismiss();
                                        mList.get(position).getTalkdetils()
                                                .remove(number);
                                        int beforeCommentNum = mList.get(
                                                position).getcCount();
                                        mList.get(position).setcCount(
                                                beforeCommentNum - 1);
                                        notifyDataSetChanged();
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
                        changeCommentListener.OnCheckedChangeListener(position,
                                findID, 2, commentID, theFirstName);
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

    // 分享功能点击事件
    class ShareClickListener implements OnClickListener {

        private FindGroup findGroup;

        public ShareClickListener(FindGroup findGroup) {
            this.findGroup = findGroup;
        }

        @Override
        public void onClick(View view) {
            if (Utils.isFastClick(2000)) {
                return;
            }
            // TODO Auto-generated method stub
            Intent intent = new Intent(mContext, ShareDialogMainActivity.class);
            intent.putExtra("findGroup", findGroup);
            mContext.startActivity(intent);
        }

    }

    public interface DelItem {
        public void returnFindid(String findId);
    }

    public DelItem getDelItem() {
        return delItem;
    }

    public void setDelItem(DelItem delItem) {
        this.delItem = delItem;
    }

    /**
     * 格式化时间（输出类似于 刚刚, 4分钟前, 一小时前, 昨天这样的时间）
     *
     * @param time    需要格式化的时间 如"2014-07-14 19:01:45"
     * @param pattern 输入参数time的时间格式 如:"yyyy-MM-dd HH:mm:ss"
     *                <p/>
     *                如果为空则默认使用"yyyy-MM-dd HH:mm:ss"格式
     * @return time为null，或者时间格式不匹配，输出空字符""
     */
    public static String formatDisplayTime(String time, String pattern) {
        String display = "";
        int tMin = 60 * 1000;
        int tHour = 60 * tMin;
        int tDay = 24 * tHour;

        if (time != null) {
            try {
                Date tDate = new SimpleDateFormat(pattern).parse(time);
                Date today = new Date();
                SimpleDateFormat thisYearDf = new SimpleDateFormat("yyyy");
                SimpleDateFormat todayDf = new SimpleDateFormat("yyyy-MM-dd");
                Date thisYear = new Date(thisYearDf.parse(
                        thisYearDf.format(today)).getTime());
                Date yesterday = new Date(todayDf.parse(todayDf.format(today))
                        .getTime());
                Date beforeYes = new Date(yesterday.getTime() - tDay);
                if (tDate != null) {
                    SimpleDateFormat halfDf = new SimpleDateFormat(
                            "MM-dd HH:mm");
                    long dTime = today.getTime() - tDate.getTime();
                    if (tDate.before(thisYear)) {
                        display = new SimpleDateFormat("yyyy年MM月dd日")
                                .format(tDate);
                    } else {

                        if (dTime < tMin) {
                            display = "刚刚";
                        } else if (dTime < tHour) {
                            display = (int) (dTime / tMin) + "分钟前";
                        } else if (dTime < tDay && tDate.after(yesterday)) {
                            display = (int) (dTime / tHour) + "小时前";
                        } else if (tDate.after(beforeYes)
                                && tDate.before(yesterday)) {
                            display = "昨天";
                        } else {
                            display = halfDf.format(tDate);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return display;
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

    private void showdeleteAllDialogs(String find_id, int position) {
        final String findID = find_id;
        final int list_position = position;
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
                                // 回调给页面findid
                                if (delItem != null) {
                                    delItem.returnFindid(mList.get(
                                            list_position).getFindId());
                                }
                                mSportsApp.setDongtai_personalceter(3);
                                mList.remove(list_position);
                                notifyDataSetChanged();
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

    // 确认是否置顶整个item
    private void showdeletetopAllDialogs(String find_id, final int position) {
        final String findID = find_id;
        final int list_position = position;
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
                            R.string.sports_dialog_deltoping));
                    new AsyncTask<Void, Void, ApiBack>() {
                        @Override
                        protected ApiBack doInBackground(Void... params) {
                            // TODO Auto-generated method stub
                            ApiBack back = null;
                            try {
                                back = (ApiBack) ApiJsonParser.deltopFind(
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
                                                R.string.sports_deltop_failed),
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                // 删除成功
                                waitProgressDialog.dismiss();
                                FindGroup findGroup = mList.get(list_position);
                                mList.remove(position);
                                notifyDataSetChanged();
                                // mList.remove(list_position+1);
                                // notifyDataSetChanged();
                                Toast.makeText(
                                        mContext,
                                        mContext.getResources()
                                                .getString(
                                                        R.string.sports_deltop_successed),
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
                R.string.make_sure_will_deltop));
        v.setMinimumWidth((int) (SportsApp.ScreenWidth * 0.8));
        alertDialog.setCancelable(true);
        alertDialog.setContentView(v);
        alertDialog.show();
    }

    class AllContent implements OnClickListener {
        private int position;
        private TextView mTextView, mTextView2;

        public AllContent(int position, TextView mTextView, TextView mTextView2) {
            this.position = position;
            this.mTextView = mTextView;
            this.mTextView2 = mTextView2;
        }

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub

            if (mList.get(position).isShow()) {
                mTextView.setText("全文");
                mTextView2.setText(mList.get(position).getDetils()
                        .subSequence(0, 100)
                        + "......");
                mList.get(position).setShow(false);

                if (mTextView2.getText().toString() != null
                        && !"".equals(mTextView2.getText().toString())) {
                    if (mTextView2.getText().toString().lastIndexOf(" ") != 0) {
                        boolean flog = mList.get(position).getDetils()
                                .contains(" ");
                        boolean flog2 = mList.get(position).getDetils()
                                .contains("#");
                        if (flog && flog2) {
                            String likeUsers = mTextView2
                                    .getText()
                                    .toString()
                                    .substring(
                                            0,
                                            mTextView2.getText().toString()
                                                    .lastIndexOf("#") + 1)
                                    .toString();

                            String listRemove = mTextView2.getText().toString()
                                    .replace(likeUsers, "");
                            // final String likeUsers =
                            // mList.get(position).getDetils().toString();
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
                                                    mContext,
                                                    ActivityInfoWebView.class);
                                            intent.putExtra("title_name", name
                                                    .toString()
                                                    .replace("#", ""));
                                            intent.putExtra("action_url", "");
                                            if (activityNameList.indexOf(name
                                                    .toString()
                                                    .replace("#", "")) != -1) {
                                                intent.putExtra(
                                                        "activity_id",
                                                        activityIdList
                                                                .get(activityNameList
                                                                        .indexOf(name
                                                                                .toString()
                                                                                .replace(
                                                                                        "#",
                                                                                        ""))));

                                                ActionList action = actionLists
                                                        .get(activityNameList
                                                                .indexOf(name
                                                                        .toString()
                                                                        .replace(
                                                                                "#",
                                                                                "")));
                                                activityTime_2 = action
                                                        .getActionTime()
                                                        .substring(
                                                                action.getActionTime()
                                                                        .indexOf(
                                                                                "-") + 1,
                                                                action.getActionTime()
                                                                        .length())
                                                        .replace(".", "-");
                                                activityTime_2 = activityTime_2
                                                        .replace("-", "");
                                                int b = Integer.valueOf(
                                                        activityTime_2)
                                                        .intValue() + 1;
                                                activityTime_2 = b + "";
                                                String year = activityTime_2
                                                        .substring(0, 4);
                                                String month = activityTime_2
                                                        .substring(4, 6);
                                                String day = activityTime_2
                                                        .substring(6, 8);
                                                activityTime_2 = year + "-"
                                                        + month + "-" + day;
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
                                    aaaa.setSpan(blackSpan, 0, aaaa.toString()
                                                    .indexOf("#"),
                                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                }
                            }
                            if (aaaa.toString().contains("#")) {
                                aaaa.setSpan(greenSpan, aaaa.toString()
                                                .indexOf("#"), aaaa.toString()
                                                .lastIndexOf("#") + 1,
                                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                            }
                            mTextView2.setText(aaaa);
                            mTextView2.setMovementMethod(LinkMovementMethod
                                    .getInstance());
                        }
                    } else {
                        holder.detils.setVisibility(View.GONE);
                    }
                }

            } else {
                mTextView.setText("收起");
                mTextView2.setText(mList.get(position).getDetils());
                mList.get(position).setShow(true);

                if (mTextView2.getText().toString() != null
                        && !"".equals(mTextView2.getText().toString())) {
                    if (mTextView2.getText().toString().lastIndexOf(" ") != 0) {
                        boolean flog = mList.get(position).getDetils()
                                .contains(" ");
                        boolean flog2 = mList.get(position).getDetils()
                                .contains("#");
                        if (flog && flog2) {
                            String likeUsers = mTextView2
                                    .getText()
                                    .toString()
                                    .substring(
                                            0,
                                            mTextView2.getText().toString()
                                                    .lastIndexOf("#") + 1)
                                    .toString();

                            String listRemove = mTextView2.getText().toString()
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
                                                    mContext,
                                                    ActivityInfoWebView.class);
                                            intent.putExtra("title_name", name
                                                    .toString()
                                                    .replace("#", ""));
                                            intent.putExtra("action_url", "");
                                            if (activityNameList.indexOf(name
                                                    .toString()
                                                    .replace("#", "")) != -1) {
                                                intent.putExtra(
                                                        "activity_id",
                                                        activityIdList
                                                                .get(activityNameList
                                                                        .indexOf(name
                                                                                .toString()
                                                                                .replace(
                                                                                        "#",
                                                                                        ""))));

                                                ActionList action = actionLists
                                                        .get(activityNameList
                                                                .indexOf(name
                                                                        .toString()
                                                                        .replace(
                                                                                "#",
                                                                                "")));
                                                activityTime_2 = action
                                                        .getActionTime()
                                                        .substring(
                                                                action.getActionTime()
                                                                        .indexOf(
                                                                                "-") + 1,
                                                                action.getActionTime()
                                                                        .length())
                                                        .replace(".", "-");
                                                activityTime_2 = activityTime_2
                                                        .replace("-", "");
                                                int b = Integer.valueOf(
                                                        activityTime_2)
                                                        .intValue() + 1;
                                                activityTime_2 = b + "";
                                                String year = activityTime_2
                                                        .substring(0, 4);
                                                String month = activityTime_2
                                                        .substring(4, 6);
                                                String day = activityTime_2
                                                        .substring(6, 8);
                                                activityTime_2 = year + "-"
                                                        + month + "-" + day;
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
                                    aaaa.setSpan(blackSpan, 0, aaaa.toString()
                                                    .indexOf("#"),
                                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                }
                            }
                            if (aaaa.toString().contains("#")) {
                                aaaa.setSpan(greenSpan, aaaa.toString()
                                                .indexOf("#"), aaaa.toString()
                                                .lastIndexOf("#") + 1,
                                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                            }
                            mTextView2.setText(aaaa);
                            mTextView2.setMovementMethod(LinkMovementMethod
                                    .getInstance());
                        }
                    } else {
                        holder.detils.setVisibility(View.GONE);
                    }
                }
            }
        }
    }

    private void showZanImage(ArrayList<PraiseUsers> pArrayList, final int position) {

        holder.activity_zan_layout.setVisibility(View.GONE);
        holder.dianzan_one.setVisibility(View.GONE);
        holder.dianzan_two.setVisibility(View.GONE);
        holder.dianzan_three.setVisibility(View.GONE);
        holder.dianzan_four.setVisibility(View.GONE);
        holder.dianzan_five.setVisibility(View.GONE);
        holder.dianzan_six.setVisibility(View.GONE);
        if (pArrayList.size() == 0) {
            holder.activity_zan_layout.setVisibility(View.GONE);
        } else if (pArrayList.size() == 1) {
            holder.zan_more_icon.setVisibility(View.GONE);
            holder.activity_zan_layout.setVisibility(View.VISIBLE);
            holder.dianzan_one.setVisibility(View.VISIBLE);
            setRoudImage(pArrayList.get(0).getImg(), holder.dianzan_one,
                    pArrayList.get(0).getSex());
            holder.dianzan_one.setOnClickListener(new personalOnClickListener(
                    Integer.parseInt(pArrayList.get(0).getUid())));
        } else if (pArrayList.size() == 2) {
            holder.zan_more_icon.setVisibility(View.GONE);
            holder.activity_zan_layout.setVisibility(View.VISIBLE);
            holder.dianzan_one.setVisibility(View.VISIBLE);
            holder.dianzan_two.setVisibility(View.VISIBLE);
            setRoudImage(pArrayList.get(0).getImg(), holder.dianzan_one,
                    pArrayList.get(0).getSex());
            setRoudImage(pArrayList.get(1).getImg(), holder.dianzan_two,
                    pArrayList.get(1).getSex());
            holder.dianzan_one.setOnClickListener(new personalOnClickListener(
                    Integer.parseInt(pArrayList.get(0).getUid())));
            holder.dianzan_two.setOnClickListener(new personalOnClickListener(
                    Integer.parseInt(pArrayList.get(1).getUid())));
        } else if (pArrayList.size() == 3) {
            holder.zan_more_icon.setVisibility(View.GONE);
            holder.activity_zan_layout.setVisibility(View.VISIBLE);
            holder.dianzan_one.setVisibility(View.VISIBLE);
            holder.dianzan_two.setVisibility(View.VISIBLE);
            holder.dianzan_three.setVisibility(View.VISIBLE);
            setRoudImage(pArrayList.get(0).getImg(), holder.dianzan_one,
                    pArrayList.get(0).getSex());
            setRoudImage(pArrayList.get(1).getImg(), holder.dianzan_two,
                    pArrayList.get(1).getSex());
            setRoudImage(pArrayList.get(2).getImg(), holder.dianzan_three,
                    pArrayList.get(2).getSex());
            holder.dianzan_one.setOnClickListener(new personalOnClickListener(
                    Integer.parseInt(pArrayList.get(0).getUid())));
            holder.dianzan_two.setOnClickListener(new personalOnClickListener(
                    Integer.parseInt(pArrayList.get(1).getUid())));
            holder.dianzan_three
                    .setOnClickListener(new personalOnClickListener(Integer
                            .parseInt(pArrayList.get(2).getUid())));
        } else if (pArrayList.size() == 4) {
            holder.zan_more_icon.setVisibility(View.GONE);
            holder.activity_zan_layout.setVisibility(View.VISIBLE);
            holder.dianzan_one.setVisibility(View.VISIBLE);
            holder.dianzan_two.setVisibility(View.VISIBLE);
            holder.dianzan_three.setVisibility(View.VISIBLE);
            holder.dianzan_four.setVisibility(View.VISIBLE);
            setRoudImage(pArrayList.get(0).getImg(), holder.dianzan_one,
                    pArrayList.get(0).getSex());
            setRoudImage(pArrayList.get(1).getImg(), holder.dianzan_two,
                    pArrayList.get(1).getSex());
            setRoudImage(pArrayList.get(2).getImg(), holder.dianzan_three,
                    pArrayList.get(2).getSex());
            setRoudImage(pArrayList.get(3).getImg(), holder.dianzan_four,
                    pArrayList.get(3).getSex());
            holder.dianzan_one.setOnClickListener(new personalOnClickListener(
                    Integer.parseInt(pArrayList.get(0).getUid())));
            holder.dianzan_two.setOnClickListener(new personalOnClickListener(
                    Integer.parseInt(pArrayList.get(1).getUid())));
            holder.dianzan_three
                    .setOnClickListener(new personalOnClickListener(Integer
                            .parseInt(pArrayList.get(2).getUid())));
            holder.dianzan_four.setOnClickListener(new personalOnClickListener(
                    Integer.parseInt(pArrayList.get(3).getUid())));
        } else if (pArrayList.size() == 5) {
            holder.zan_more_icon.setVisibility(View.GONE);
            holder.activity_zan_layout.setVisibility(View.VISIBLE);
            holder.dianzan_one.setVisibility(View.VISIBLE);
            holder.dianzan_two.setVisibility(View.VISIBLE);
            holder.dianzan_three.setVisibility(View.VISIBLE);
            holder.dianzan_four.setVisibility(View.VISIBLE);
            holder.dianzan_five.setVisibility(View.VISIBLE);
            setRoudImage(pArrayList.get(0).getImg(), holder.dianzan_one,
                    pArrayList.get(0).getSex());
            setRoudImage(pArrayList.get(1).getImg(), holder.dianzan_two,
                    pArrayList.get(1).getSex());
            setRoudImage(pArrayList.get(2).getImg(), holder.dianzan_three,
                    pArrayList.get(2).getSex());
            setRoudImage(pArrayList.get(3).getImg(), holder.dianzan_four,
                    pArrayList.get(3).getSex());
            setRoudImage(pArrayList.get(4).getImg(), holder.dianzan_five,
                    pArrayList.get(4).getSex());
            holder.dianzan_one.setOnClickListener(new personalOnClickListener(
                    Integer.parseInt(pArrayList.get(0).getUid())));
            holder.dianzan_two.setOnClickListener(new personalOnClickListener(
                    Integer.parseInt(pArrayList.get(1).getUid())));
            holder.dianzan_three
                    .setOnClickListener(new personalOnClickListener(Integer
                            .parseInt(pArrayList.get(2).getUid())));
            holder.dianzan_four.setOnClickListener(new personalOnClickListener(
                    Integer.parseInt(pArrayList.get(3).getUid())));
            holder.dianzan_five.setOnClickListener(new personalOnClickListener(
                    Integer.parseInt(pArrayList.get(4).getUid())));
        } else if (pArrayList.size() >= 6) {
            holder.activity_zan_layout.setVisibility(View.VISIBLE);
            holder.dianzan_one.setVisibility(View.VISIBLE);
            holder.dianzan_two.setVisibility(View.VISIBLE);
            holder.dianzan_three.setVisibility(View.VISIBLE);
            holder.dianzan_four.setVisibility(View.VISIBLE);
            holder.dianzan_five.setVisibility(View.VISIBLE);
            holder.dianzan_six.setVisibility(View.VISIBLE);
            setRoudImage(pArrayList.get(0).getImg(), holder.dianzan_one,
                    pArrayList.get(0).getSex());
            setRoudImage(pArrayList.get(1).getImg(), holder.dianzan_two,
                    pArrayList.get(1).getSex());
            setRoudImage(pArrayList.get(2).getImg(), holder.dianzan_three,
                    pArrayList.get(2).getSex());
            setRoudImage(pArrayList.get(3).getImg(), holder.dianzan_four,
                    pArrayList.get(3).getSex());
            setRoudImage(pArrayList.get(4).getImg(), holder.dianzan_five,
                    pArrayList.get(4).getSex());
            setRoudImage(pArrayList.get(5).getImg(), holder.dianzan_six,
                    pArrayList.get(5).getSex());
            holder.dianzan_one.setOnClickListener(new personalOnClickListener(
                    Integer.parseInt(pArrayList.get(0).getUid())));
            holder.dianzan_two.setOnClickListener(new personalOnClickListener(
                    Integer.parseInt(pArrayList.get(1).getUid())));
            holder.dianzan_three
                    .setOnClickListener(new personalOnClickListener(Integer
                            .parseInt(pArrayList.get(2).getUid())));
            holder.dianzan_four.setOnClickListener(new personalOnClickListener(
                    Integer.parseInt(pArrayList.get(3).getUid())));
            holder.dianzan_five.setOnClickListener(new personalOnClickListener(
                    Integer.parseInt(pArrayList.get(4).getUid())));
            holder.dianzan_six.setOnClickListener(new personalOnClickListener(
                    Integer.parseInt(pArrayList.get(5).getUid())));
            if (pArrayList.size() > 6) {
                holder.zan_more_icon.setVisibility(View.VISIBLE);
                holder.zan_more_icon.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        // TODO Auto-generated method stub
                        Intent intent = new Intent(mContext,
                                ZanListUserActivity.class);
                        intent.putExtra("find_id", Integer.parseInt(mList.get(position).getFindId()));
                        intent.putExtra("uid", mList.get(position).getOtheruid());
                        mContext.startActivity(intent);
                    }
                });
            }
        }
    }

    private void setRoudImage(String imagUrl, ImageView imageView, String sex) {
        if (!"http://dev-kupao.mobifox.cn".equals(imagUrl) && !"http://kupao.mobifox.cn".equals(imagUrl)  && imagUrl != null) {
            if (sex.equals("1")) {
                imageView.setImageResource(R.drawable.sports_user_edit_portrait_male);
            } else {
                imageView.setImageResource(R.drawable.sports_user_edit_portrait);
            }
            mImageWorker_Icon.loadImage(imagUrl, imageView, null, null, false);
        } else {
            if (sex.equals("1") || sex.equals("man")) {
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
            Intent intent = new Intent(mContext, PersonalPageMainActivity.class);
            intent.putExtra("ID", userId);
            mContext.startActivity(intent);

        }

    }

    public List<SpannableString> getNameString(Context context, String str,
                                               int numcom, int firstname, int secondname) {
        List<SpannableString> list = new ArrayList<SpannableString>();
        SpannableString spanString = new SpannableString(str);
        // 正则表达式比配字符串里是否含有表情，如： 我好[开心]啊
        try {
            if (numcom == 1) {
                int endone = firstname;
                spanString.setSpan(new ForegroundColorSpan(mContext
                                .getResources().getColor(R.color.remind2)), 0,
                        endone + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
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
            }
            list.add(spanString);
        } catch (Exception e) {
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
            if (numcom == 1) {
                spanString.setSpan(new ForegroundColorSpan(mContext
                        .getResources().getColor(R.color.sports_value)), 0, str
                        .length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            } else {
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
                    int Screenwidth = (int) SportsApp.ScreenWidth;
                    int width = 0;
                    if (Screenwidth > 1000) {
                        width = Screenwidth * 19 / 100;
                    } else {
                        width = Screenwidth * 10 / 100;
                    }
                    bitmap = Bitmap.createScaledBitmap(bitmap, width, width,
                            // 通过图片资源id来得到bitmap，用一个ImageSpan来包装
                            true);
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
        }
        return list;
    }
}
