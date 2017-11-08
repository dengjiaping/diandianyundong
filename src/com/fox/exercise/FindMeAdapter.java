package com.fox.exercise;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
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
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import cn.ingenic.indroidsync.SportsApp;

import com.fox.exercise.TitlePopup.OnItemOnClickListener;
import com.fox.exercise.api.ApiBack;
import com.fox.exercise.api.ApiJsonParser;
import com.fox.exercise.api.ApiNetException;
import com.fox.exercise.api.entity.ExpressionItems;
import com.fox.exercise.api.entity.FindComment;
import com.fox.exercise.api.entity.FindMore;
import com.fox.exercise.api.entity.UserDetail;
import com.fox.exercise.bitmap.util.ImageResizer;
import com.fox.exercise.newversion.act.PersonalPageMainActivity;
import com.fox.exercise.util.RoundedImage;

public class FindMeAdapter extends BaseAdapter implements OnItemOnClickListener {
    private Dialog waitProgressDialog;
    // 针对9种不同的布局
    ViewHolder holder = null;
    private Context mContext = null;
    private Handler findmHandler = null;
    private ArrayList<FindMore> mList = null;
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
    // public List<List<ImageView>> imgList=new ArrayList<List<ImageView>>();
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
    private static final String TAG = "FindMeAdapter";
    private ArrayList<String[]> talklist;
    private Dialog alertDialog;
    private UserDetail detail;
    private String masterName;
    public PopupWindow mPopWindow = null;
    private int popwindowWidth;
    private int popwindowHeight;
    private TitlePopup titlePopup;
    private ImageView wavBeginOne;
    // 评论中第一个名字 /第二个名字/文字内容/音频内容/音频长度
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
    private LayoutInflater xInflater;
    private String[] imgStr;
    private SpannableString spans;
    private List<ExpressionItems> imgItems;
    private int numcom;
    // 录音
    boolean isPause = false;
    private boolean isStart = true;
    MediaPlayer mPlayer = null;
    private RecordHelper mRecorder;
    private int currentDuration;
    private static final int RECORD_LOADING = 6;
    private static final int RECORD_PREPARED = 7;
    private static final int RECORD_FINISH = 8;
    private static final int RECORD_PAUSE = 9;
    private static final int RECORD_ERROR = 10;
    private static final int FLAG_RUNWAV = 11;
    private static final int RESULT_ERROR = 12;

    public FindMeAdapter(Context context, ArrayList<FindMore> list,
                         SportsApp sportsApp, List<ExpressionItems> imgItem, Handler findmeHandler) {
        // TODO Auto-generated constructor stub
        this.mContext = context;
        mSportsApp = sportsApp;
        findmHandler = findmeHandler;
        this.mList = list;
        this.imgItems = imgItem;
        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        detail = SportsApp.getInstance().getSportUser();
        masterName = detail.getUname();
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
        // initViews();
    }

    // TODO Auto-generated constructor stub

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mList.size();
    }

    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return mList.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return arg0;
    }

    @Override
    public int getItemViewType(int position) {
        // TODO Auto-generated method stub
        // Log.e(TAG, "position------------------"+position);
        // Log.e(TAG,
        // "position------------------"+mList.get(position).getImgs());
        type = mList.get(position).getImgs().length;
        // Log.e("---", "type0--------"+type);
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
        // final TitlePopup titlePopup= new
        // TitlePopup(mContext,SportsApp.dip2px(165), SportsApp.dip2px(40));
        urls = mList.get(position).getImgs();
        getItemViewType(position);
        if (convertView == null) {
            holder = new ViewHolder();
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
            holder.detils = (TextView) convertView
                    .findViewById(R.id.tx_detils1);
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
        } else {
            holder = (ViewHolder) convertView.getTag();
            holder.talkdetilsLayout.removeAllViews();
        }

        switch (type) {
            case 1:
                int height = mList.get(position).getHeight();
                int width = mList.get(position).getWidth();
                if (width >= height) {
                    // Log.e("---", "此图形是横着的---------");
                    LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) holder.img1
                            .getLayoutParams();
                    int imgWidth = (int) (SportsApp.ScreenWidth * 0.5);
                    lp.height = (int) ((imgWidth * height) / width);
                    lp.width = imgWidth;
                    holder.img1.setLayoutParams(lp);

                } else if (height > width) {
                    // Log.e("---", "此图形是竖直的---------");
                    LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) holder.img1
                            .getLayoutParams();
                    int imgHeight = (int) (SportsApp.ScreenHeight * 0.33);
                    lp.height = imgHeight;
                    lp.width = (int) ((imgHeight * width) / height);
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
                imgList5.add(holder.img5);
                setImageVoid(type, position, imgList5);
                break;
            case 6:
                imgList6.clear();
                imgList6.add(holder.img1);
                imgList6.add(holder.img2);
                imgList6.add(holder.img3);
                imgList6.add(holder.img4);
                imgList6.add(holder.img5);
                imgList6.add(holder.img6);
                setImageVoid(type, position, imgList6);
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
                setImageVoid(type, position, imgList7);
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
                setImageVoid(type, position, imgList8);
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
                setImageVoid(type, position, imgList9);
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
            holder.start_time.setText(format.format(mList.get(position)
                    .getTimes() * 1000));
        }
        holder.otherImage.setImageDrawable(null);
        if ("man".equals(mList.get(position).getSex())) {
            holder.otherImage
                    .setBackgroundResource(R.drawable.sports_user_edit_portrait_male);
        } else if ("woman".equals(mList.get(position).getSex())) {
            holder.otherImage
                    .setBackgroundResource(R.drawable.sports_user_edit_portrait);
        }
        mDownloader.download(mList.get(position).getOtherimg(),
                holder.otherImage, null);
        // 显示名字
        holder.otherImage.setOnClickListener(new personalInformationOnClickListener(mList.get(position).getOtheruid()));
        //显示名字
        holder.nametext.setText(mList.get(position).getOthername());
        holder.nametext.setOnClickListener(new personalInformationOnClickListener(mList.get(position).getOtheruid()));
        // 判断该条发现是否是登录人发的
        if (detail.getUname().equals(mList.get(position).getOthername())) {
            holder.deleteText.setVisibility(View.VISIBLE);
            holder.deleteText.setOnClickListener(new deleteListener(mList.get(
                    position).getFindId(), position));
        } else {
            holder.deleteText.setVisibility(View.GONE);
        }
        titlePopup.setItemOnClickListener(this);
        holder.goodandtext.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {
                // TODO Auto-generated method stub
                if (findmHandler != null) {
                    findmHandler.sendMessage(findmHandler.obtainMessage(
                            FindMeActivity.HIDE_EDIT));
                }
                // 点赞和评论
                titlePopup.setAnimationStyle(R.style.cricleBottomAnimation);
                titlePopup
                        .show(view,
                                position,
                                mList.get(position).getFindId(),
                                mList.get(position).isGood() ? mContext
                                        .getResources().getString(
                                                R.string.sports_cancel)
                                        : mContext.getResources().getString(
                                        R.string.sports_good));
            }
        });
        holder.detils.setText(mList.get(position).getDetils());
        // 如果评论人数大于0或者点赞人数大于0
        if (mList.get(position).getGoodpeople() <= 0
                && mList.get(position).getCommentCount() <= 0) {
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
            if (mList.get(position).getGoodpeople() > 0) {
                holder.goodLayout.setVisibility(View.VISIBLE);
                holder.goodPeopole.setText(mList.get(position).getGoodpeople()
                        + "");
            } else {
                holder.goodLayout.setVisibility(View.GONE);
            }
            if (mList.get(position).getCommentCount() > 0) {
                holder.textLayout.setVisibility(View.VISIBLE);
                holder.textPeopole.setText(mList.get(position)
                        .getCommentCount() + "");
                // 将评论内容显示出来
                for (int i = 0; i < mList.get(position).getTalkdetils().size(); i++) {
                    theFirstName = mList.get(position).getTalkdetils().get(i)
                            .getFirstName();
                    theSecondName = mList.get(position).getTalkdetils().get(i)
                            .getSecondName();
                    thewavComment = mList.get(position).getTalkdetils().get(i)
                            .getCommentWav();
                    thewavDuration = mList.get(position).getTalkdetils().get(i)
                            .getWavDuration();
                    theTalkDetils = mList.get(position).getTalkdetils().get(i)
                            .getCommentText();
                    Log.e(TAG, "theSecond--------------------" + theSecondName);
                    // 记录名字的长度
                    int firstname, secondname = 0;
                    // 如果是语音回复
                    if (thewavComment != null && thewavDuration != null
                            && !thewavDuration.equals("null")
                            && !thewavDuration.equals("0")) {
                        newView = xInflater.inflate(
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
                        List<SpannableString> list = getExpressionString(
                                mContext, newMessageString, numcom, firstname,
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
                                    if (mediaPath.endsWith("null")
                                            || mediaPath == null) {
                                        return;
                                    }
                                    // currentDuration1 = 0;
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
                                                        Log.d(TAG,
                                                                "not isPause");
                                                        Log.e(TAG, "录音播放---7");
														/*
														 * mPlayer.
														 * setOnPreparedListener
														 * (new
														 * OnPreparedListener()
														 * {
														 *
														 * @Override public void
														 * onPrepared
														 * (MediaPlayer mPlayer)
														 * { // TODO
														 * Auto-generated method
														 * stub mPlayer.start();
														 * } });
														 * mPlayer.prepareAsync
														 * ();
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
                                                if (currentDuration > 0
                                                        || isPause) {
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
                        newView = xInflater.inflate(
                                R.layout.sports_find_talk_detils, null);
                        TextView sTextView = (TextView) newView
                                .findViewById(R.id.find_talk_detils_text);
                        String stringoneString = theTalkDetils.replaceAll("\\[", " [");
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

                    }
                    newView.setOnClickListener(new commentOnClickListener(
                            position, i, mList.get(position).getFindId(), mList
                            .get(position).getTalkdetils().get(i)
                            .getCommentId(), theFirstName));
                    holder.talkdetilsLayout.addView(newView);
                }
            } else {
                holder.textLayout.setVisibility(View.GONE);
            }
        }

        return convertView;
    }

    private void setImageVoid(int type, int position, List<ImageView> imgList) {
        for (int i = 0; i < type; i++) {
            LinearLayout.LayoutParams lps = (LinearLayout.LayoutParams) imgList
                    .get(i).getLayoutParams();
            lps.width = (int) (SportsApp.ScreenWidth * 0.625) / 3;
            lps.height = (int) (SportsApp.ScreenWidth * 0.625) / 3;
            imgList.get(i).setLayoutParams(lps);
            mImageWorker.loadImage(urls[i], imgList.get(i), null, null, false);
        }

    }

    //点击进入个人信息页面
    class personalInformationOnClickListener implements OnClickListener {
        private int userId;

        public personalInformationOnClickListener(int id) {
            this.userId = id;
        }

        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub
            if (!mSportsApp.isOpenNetwork()) {
                Toast.makeText(mContext, "网络未连接，请检查网络！",
                        Toast.LENGTH_LONG).show();
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
            if (detail.getUname().equals(theFirstName)) {
                showDialogs(position, comment_number, commentID);

            } else {
                /**
                 * (position,findIdString,2,commentID,theFirstName)
                 * 第一个参数所属发现在list中位置 第二个参数发现ID， 第三个参数用来判断评论还是回复1代表直接评论2代表回复
                 * 第四个参数用来当回复的时候传递to_id,如果直接评论则为null
                 * 第五个参数用来当回复的时候显示回复给谁，如果直接评论则null
                 */
                changeCommentListener.OnCheckedChangeListener(position, findID,
                        2, commentID, theFirstName);
            }
            Toast.makeText(mContext, "id-----"
                    + mList.get(position).getFindId(), Toast.LENGTH_SHORT);

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
                    waitProgressDialog.dismiss();
                    FindComment fc = new FindComment();
                    fc.setCommentId(commentId);
                    fc.setFirstName(detail.getUname());
                    if (theFirstName != null && !theFirstName.equals("")) {
                        fc.setSecondName(theFirstName);
                    }
                    fc.setCommentText(commentText);
                    fc.setCommentWav(commentWav);
                    fc.setWavDuration(wavDuration);
					/*
					 * fc.setCommentWav("http://mp3.9ku.com/file2/177/176318.mp3"
					 * ); fc.setWavDuration("60");
					 */
                    // Log.e(TAG, "长度1-------"+commentText.length());
                    if (mList.get(position).getTalkdetils() == null) {
                        ArrayList<FindComment> fList = new ArrayList<FindComment>();
                        mList.get(position).setTalkdetils(fList);
                    }
                    mList.get(position).getTalkdetils().add(fc);
                    int beforeCommentNum = mList.get(position)
                            .getCommentCount();
                    mList.get(position).setCommentCount(beforeCommentNum + 1);
                    notifyDataSetChanged();
                    Toast.makeText(
                            mContext,
                            mContext.getResources().getString(
                                    R.string.upload_success),
                            Toast.LENGTH_SHORT).show();
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
			/*
			 * Toast.makeText(mContext, "-----第"+index
			 * +"张-----地址为"+urlString[index], Toast.LENGTH_LONG).show();
			 */
            Intent intent = new Intent(mContext, SportsFoundImgActivity.class);
            Bundle bundle = new Bundle();
            bundle.putStringArray("urlString", urlString);
            bundle.putInt("index", index);
            intent.putExtras(bundle);
            mContext.startActivity(intent);
        }
    }

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

    // 确认是否删除整个item
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
                                        .getCommentCount();
                                mList.get(position).setCommentCount(
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
                                // Log.e("--", "result-------"+result);
                                waitProgressDialog.dismiss();
                                Toast.makeText(
                                        mContext,
                                        mContext.getResources().getString(
                                                R.string.sports_findgood_error),
                                        Toast.LENGTH_SHORT).show();
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
			/*
			 * Toast.makeText(mContext, "评论", Toast.LENGTH_LONG).show();
			 */

                if (changeCommentListener != null)
                    changeCommentListener.OnCheckedChangeListener(list_positon,
                            findIdString, 1, null, null);
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

    /**
     * (position,findIdString,2,commentID,theFirstName) 第一个参数所属发现在list中位置
     * 第二个参数发现ID， 第三个参数用来判断评论还是回复1代表直接评论2代表回复 第四个参数用来当回复的时候传递to_id,如果直接评论则为null
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
}
