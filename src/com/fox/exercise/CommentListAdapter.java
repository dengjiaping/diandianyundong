package com.fox.exercise;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.ingenic.indroidsync.SportsApp;

import com.fox.exercise.api.entity.ExpressionItems;
import com.fox.exercise.api.entity.FindMore2;
import com.fox.exercise.util.RoundedImage;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;

public class CommentListAdapter extends BaseAdapter implements ListAdapter {

    private Context mContext = null;
    private List<FindMore2> mListData = null;
    private List<ExpressionItems> imgItems;
    private ImageDownloader mDownload = ImageDownloader.getInstance();
    private ImageView mPic = null;
    private ImageView mGood = null;
    private TextView mName = null;
    private TextView mComment = null;
    private TextView mTime = null;
    private RoundedImage mHeadingImg = null;
    private SportsApp mApp = SportsApp.getInstance();
    private LinearLayout mAudioLayout = null;
    private String mSex = "";
    private LayoutInflater xInflater;

    public CommentListAdapter(Context context, List<FindMore2> data) {
        this.mContext = context;
        this.mListData = data;
        xInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public CommentListAdapter(Context context, List<FindMore2> data,List<ExpressionItems> imgItems) {
        this.mContext = context;
        this.mListData = data;
        this.imgItems = imgItems;
        xInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        if (mListData != null) {
            return mListData.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        if (mListData != null) {
            return mListData.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public List<SpannableString> getExpressionString(Context context, String str) {
        List<SpannableString> list = new ArrayList<SpannableString>();
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
                for (ExpressionItems item : imgItems) {
                    if (item.getName().equals(key)) {
                        resId = item.getId();
                    }
                }
                if (resId == 0) {
                    continue;
                } else if (resId != 0) {
                    Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resId);
                    //Display display =getWindowManager().getDefaultDisplay();
                    int Screenwidth = (int) SportsApp.ScreenWidth;
                    int width = 0;
                    if (Screenwidth > 1000) {
                        width = Screenwidth * 19 / 100;
                    } else {
                        width = Screenwidth * 10 / 100;
                    }
                    bitmap = Bitmap.createScaledBitmap(bitmap, width, width, true);
                    // 通过图片资源id来得到bitmap，用一个ImageSpan来包装
                    ImageSpan imageSpan = new ImageSpan(bitmap);
                    // 计算该图片名字的长度，也就是要替换的字符串的长度
                    int end = matcher.start() + key.length();
                    // 将该图片替换字符串中规定的位置中
                    spanString.setSpan(imageSpan, matcher.start(), end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    list.add(spanString);
                }
            }
            list.add(spanString);
        } catch (Exception e) {
            Log.e("dealExpression", e.getMessage());
        }
        return list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        LayoutInflater inflater = LayoutInflater.from(mContext);
        FindMore2 list_data = mListData.get(position);
        View view = inflater.inflate(R.layout.adapter_comments, null);
        mHeadingImg = (RoundedImage) view.findViewById(R.id.img_headingimg);
        mName = (TextView) view.findViewById(R.id.tv_name);
        mComment = (TextView) view.findViewById(R.id.tv_content);
        mAudioLayout = (LinearLayout) view.findViewById(R.id.audio_layout);
        mGood = (ImageView) view.findViewById(R.id.img_good);
        mTime = (TextView) view.findViewById(R.id.tv_vardate);
        mPic = (ImageView) view.findViewById(R.id.img_pic);

	/*	if(list_data.userImg == null){
			//如果需要默认头像，就需要服务器提供sex字段
			mSex = list_data.getSex();
			if(mSex.equals("man")){
				mPic.setBackgroundResource(R.drawable.sports_residemenu_man);
			}else{
				mPic.setBackgroundResource(R.drawable.sports_residemenu_woman);
			}
		}*/
        mDownload.download(list_data.userImg, mHeadingImg, null);
        long time = System.currentTimeMillis() - Long.parseLong(list_data.inputTime) * 1000;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        if (time <= 60 * 1000)
            //一分钟内显示刚刚
            mTime.setText(mContext.getResources().getString(R.string.sports_time_justnow));
        else if (time <= 60 * 60 * 1000) {
            int h = (int) (time / 1000 / 60);
            //一小时内显示多少分钟前
            mTime.setText("" + h + mContext.getResources().getString(R.string.sports_time_mins_ago));
        } else {
            mTime.setText(format.format(Long.parseLong(list_data.inputTime) * 1000));
        }
        mName.setText(list_data.name);
        //mTime.setText(list_data.inputTime);
        if (list_data.type == 0) {
            mGood.setVisibility(View.GONE);
            if (list_data.wavAddress.equals("null")) {
                if (mListData.get(position).is_delete != 1) {
                    List<SpannableString> list = getExpressionString(mContext, list_data.content);
                    for (SpannableString span : list) {
                        mComment.setText(span);
                    }
                } else {
                    mComment.setText("用户已删除评论");
                }
                mComment.setVisibility(View.VISIBLE);
                mAudioLayout.setVisibility(View.GONE);
            } else {
                mComment.setVisibility(View.GONE);
                mAudioLayout.setVisibility(View.VISIBLE);

                View newView = xInflater.inflate(R.layout.sports_find_talk_wav_detiles_message, null);
                TextView nametoName = (TextView) newView.findViewById(R.id.find_talk_detils_text_name);
                nametoName.setVisibility(View.GONE);
                LinearLayout recordlayout = (LinearLayout) newView.findViewById(R.id.recoding_click_find);
                recordlayout.setVisibility(View.VISIBLE);
                TextView durationtext = (TextView) newView.findViewById(R.id.wav_durations_find);
                durationtext.setText(list_data.wavTime);
                ImageView beginWav = (ImageView) newView.findViewById(R.id.wav_begin_find);
                RoundedImage roundedImage=(RoundedImage) newView.findViewById(R.id.activity_image_icon);
                roundedImage.setVisibility(View.GONE);
                mAudioLayout.addView(newView);
            }
//            mComment.setText(list_data.content);
        } else {
            mComment.setVisibility(View.GONE);
        }
        if ((list_data.getPicPath() != null &&!"".equals(list_data.getPicPath())) && list_data.getPicPath().length() > 0) {
            mDownload.download(list_data.getPicPath(), mPic, null);
        } else {
            mPic.setVisibility(View.GONE);
        }

        return view;
    }

}
