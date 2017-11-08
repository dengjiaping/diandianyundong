package com.fox.exercise;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import cn.ingenic.indroidsync.SportsApp;

import com.fox.exercise.api.entity.UserFollowMsg;
import com.fox.exercise.newversion.act.PersonalPageMainActivity;


import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class FocusFocusAdapter extends BaseAdapter {

    private ArrayList<UserFollowMsg> mList = new ArrayList<UserFollowMsg>();
    private Context mContext = null;
    private LayoutInflater mInflater = null;

    private ImageDownloader mDownloader = null;
    private ImageDownloader mIconDownloader = null;
    private SportsApp mSportsApp = SportsApp.getInstance();

    public FocusFocusAdapter(Context context) {
        this.mContext = context;
//		this.mList = list;
        this.mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mDownloader = new ImageDownloader(mContext);
        mDownloader.setType(ImageDownloader.GIFT);
        mIconDownloader = new ImageDownloader(mContext);
        mIconDownloader.setSize(90, 90);
        mIconDownloader.setType(ImageDownloader.ICON);
    }

    public FocusFocusAdapter(Context context, ArrayList<UserFollowMsg> list) {
        this.mContext = context;
//		this.mList = list;
        this.mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mDownloader = new ImageDownloader(mContext);
        mDownloader.setType(ImageDownloader.GIFT);
        mIconDownloader = new ImageDownloader(mContext);
        mIconDownloader.setSize(90, 90);
        mIconDownloader.setType(ImageDownloader.ICON);
        this.mList = list;
    }

    public void clearList() {
        mList.clear();
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int arg0) {
        return null;
    }

    public void addItem(UserFollowMsg um) {
        mList.add(um);
    }

    @Override
    public long getItemId(int arg0) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup arg2) {
        RelativeLayout layout = null;

        Log.d("GiftsMyAdapter", mList.get(position).toString());
        if (convertView == null) {
            layout = (RelativeLayout) mInflater.inflate(
                    R.layout.sports_gz_upload, null);
        } else {
            layout = (RelativeLayout) convertView;
        }

        ImageView icon = (ImageView) layout.findViewById(R.id.focus_image_icon);
        icon.setImageDrawable(null);
        if ("man".equals(mList.get(position).getSex())) {
            icon.setBackgroundResource(R.drawable.sports_user_edit_portrait_male);
        } else {
            icon.setBackgroundResource(R.drawable.sports_user_edit_portrait);
        }
        mIconDownloader.download(mList.get(position).getImg(), icon, null);
        icon.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (!mSportsApp.isOpenNetwork()) {
                    Toast.makeText(mContext, "网络未连接，请检查网络！", Toast.LENGTH_LONG)
                            .show();
                    return;
                }
                if (mList != null && position < mList.size()) {
                    Intent intent = new Intent(mContext, PersonalPageMainActivity.class);
                    intent.putExtra("ID", mList.get(position).getId());
                    mContext.startActivity(intent);
                }
            }
        });

        TextView nameTxt = (TextView) layout.findViewById(R.id.focus_name_txt);
        nameTxt.setText(mList.get(position).getName());

        ImageView sexImg = (ImageView) layout.findViewById(R.id.img_sex);
        if (mList.get(position).getSex().equals("man")) {
            sexImg.setImageBitmap(BitmapFactory.decodeStream(mContext
                    .getResources().openRawResource(+R.drawable.sex_boy)));
        } else {
            sexImg.setImageBitmap(BitmapFactory.decodeStream(mContext
                    .getResources().openRawResource(+R.drawable.sex_girl)));
        }

        TextView ageTxt = (TextView) layout.findViewById(R.id.tx_age);
        SportsUtilities.setAge(mContext, mList
                .get(position).getBirthday(), ageTxt);

        TextView timeTxt = (TextView) layout.findViewById(R.id.tx_message);
        long time = System.currentTimeMillis()
                - mList.get(position).getAddTime() * 1000;
        if (time <= 60 * 1000)
            timeTxt.setText("刚刚关注了");
        else if (time <= 60 * 60 * 1000) {
            int h = (int) (time / 1000 / 60);
            timeTxt.setText("" + h + "分钟前关注了");
        } else {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            timeTxt.setText(format
                    .format(mList.get(position).getAddTime() * 1000) + "  关注了");
        }

        ImageView uploadIcon = (ImageView) layout.findViewById(R.id.upload_image_icon);
        uploadIcon.setImageDrawable(null);
        if ("man".equals(mList.get(position).getOsex())) {
            uploadIcon.setBackgroundResource(R.drawable.sports_user_edit_portrait_male);
        } else {
            uploadIcon.setBackgroundResource(R.drawable.sports_user_edit_portrait);
        }
        mIconDownloader.download(mList.get(position).getOimg(), uploadIcon, null);
        uploadIcon.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (!mSportsApp.isOpenNetwork()) {
                    Toast.makeText(mContext, "网络未连接，请检查网络！", Toast.LENGTH_LONG)
                            .show();
                    return;
                }
                if (mList != null && position < mList.size()) {
                    Intent intent = new Intent(mContext, PersonalPageMainActivity.class);
                    intent.putExtra("ID", mList.get(position).getOid());
                    mContext.startActivity(intent);
                }
            }
        });
        return layout;
    }


}
