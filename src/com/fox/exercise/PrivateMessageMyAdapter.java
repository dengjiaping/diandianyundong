package com.fox.exercise;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import cn.ingenic.indroidsync.SportsApp;

import com.fox.exercise.api.entity.PrivateMsgStatus;
import com.fox.exercise.api.entity.UserPrimsgAll;
import com.fox.exercise.newversion.act.PersonalPageMainActivity;

public class PrivateMessageMyAdapter extends BaseAdapter {

    private ArrayList<PrivateMsgStatus> mList = null;
    private Context mContext = null;
    private LayoutInflater mInflater = null;
    private int indexGL = -1;
    private int indexRZ = -1;

    private ImageDownloader mDownloader = null;
    private ImageDownloader mIconDownloader = null;
    private SportsApp mSportsApp = SportsApp.getInstance();

    public PrivateMessageMyAdapter(ArrayList<PrivateMsgStatus> list, Context context) {
        this.mContext = context;
        this.mList = list;
        this.mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mDownloader = new ImageDownloader(mContext);
        mDownloader.setType(ImageDownloader.GIFT);
        mIconDownloader = new ImageDownloader(mContext);
        mIconDownloader.setType(ImageDownloader.ICON);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public PrivateMsgStatus getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        if (mList != null && position < mList.size()) {
            return mList.get(position).getUserPrimsgAll().getUid();
        }
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup arg2) {
        LinearLayout layout = null;
        final UserPrimsgAll userPrimsgAll = mList.get(position).getUserPrimsgAll();
        if (convertView == null) {
            layout = (LinearLayout) mInflater.inflate(R.layout.sports_privatemsg_my_item, null);
        } else {
            layout = (LinearLayout) convertView;
        }
        if (userPrimsgAll.getUid() == 13016) {
            indexGL = position;
        }
        if (userPrimsgAll.getUid() == 53794) {
            indexRZ = position;
        }
        ImageView icon = (ImageView) layout.findViewById(R.id.focus_image_icon);
        icon.setImageDrawable(null);
        if ("man".equals(userPrimsgAll.getSex())) {
            icon.setBackgroundResource(R.drawable.sports_user_edit_portrait_male);
        } else {
            icon.setBackgroundResource(R.drawable.sports_user_edit_portrait);
        }
        mIconDownloader.download(userPrimsgAll.getUimg(), icon, null);
        if (position == indexGL || position == indexRZ) {
            icon.setOnClickListener(null);
        } else {
            icon.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    if (mList != null && position < mList.size()) {
                        if (!mSportsApp.isOpenNetwork()) {
                            Toast.makeText(mContext, "网络未连接，请检查网络！", Toast.LENGTH_LONG)
                                    .show();
                            return;
                        }
//						Intent intent = new Intent(mContext, PedometerActivity.class);
                        Intent intent = new Intent(mContext, PersonalPageMainActivity.class);
                        intent.putExtra("ID", userPrimsgAll.getUid());
                        mContext.startActivity(intent);
                    }
                }
            });
        }

        TextView nameTxt = (TextView) layout.findViewById(R.id.focus_name_txt);
        if (position == indexGL || position == indexRZ) {
            nameTxt.setText(userPrimsgAll.getName());
        } else {
            nameTxt.setText(userPrimsgAll.getName());
        }

        ImageView sexImg = (ImageView) layout.findViewById(R.id.img_sex);
        if (userPrimsgAll.getSex().equals("man")) {
            sexImg.setImageBitmap(BitmapFactory.decodeStream(mContext.getResources()
                    .openRawResource(+R.drawable.sex_boy)));
        } else {
            sexImg.setImageBitmap(BitmapFactory.decodeStream(mContext.getResources().openRawResource(
                    +R.drawable.sex_girl)));
        }
        ImageView imgStatus = (ImageView) layout.findViewById(R.id.img_status);
        Button tiptxt = (Button) layout.findViewById(R.id.focusText);
        Log.e("STATUS", "STATUS:" + mList.get(position).getMsgStatus());
        if ("unread".equals(mList.get(position).getMsgStatus())) {
            Log.e("STATUS", "unread:" + mList.get(position).getMsgStatus());
            imgStatus.setBackgroundResource(R.drawable.private_msg_unread);
        } else {
            Log.e("STATUS", "read:" + mList.get(position).getMsgStatus());
            imgStatus.setBackgroundResource(R.drawable.private_msg_read);
        }

        if (mList.get(position).getUserPrimsgAll().getCounts() == 0) {
            tiptxt.setVisibility(View.GONE);
        } else {
            tiptxt.setVisibility(View.VISIBLE);
            tiptxt.setText(mList.get(position).getUserPrimsgAll().getCounts() + "");
        }

        TextView ageTxt = (TextView) layout.findViewById(R.id.tx_age);
        SportsUtilities.setAge(mContext, userPrimsgAll.getBirthday(), ageTxt);

        TextView timeTxt = (TextView) layout.findViewById(R.id.tx_message);
        long time = System.currentTimeMillis() - userPrimsgAll.getAddTime() * 1000;
        if (time <= 60 * 1000)
            timeTxt.setText("刚刚");
        else if (time <= 60 * 60 * 1000) {
            int h = (int) (time / 1000 / 60);
            timeTxt.setText("" + h + "分钟前");
        } else {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            timeTxt.setText(format.format(userPrimsgAll.getAddTime() * 1000));
        }
        return layout;
    }

}
