package com.fox.exercise;

import java.util.ArrayList;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import cn.ingenic.indroidsync.SportsApp;

import com.fox.exercise.api.entity.UserNearby;
import com.fox.exercise.newversion.act.PersonalPageMainActivity;
import com.fox.exercise.util.RoundedImage;

public class MyFriendsAdapter extends BaseAdapter {

    private Context mContext = null;
    private ArrayList<UserNearby> mList = null;

    private ImageDownloader mDownloader = null;

    private LayoutInflater mInflater = null;

    // private Dialog loadProgressDialog = null;
    // private TextView message = null;
    private SharedPreferences sp;
    private static final String TAG = "AddFriendActivity";
    private SportsApp mSportsApp;
    private Dialog loadProgressDialog = null;
    private TextView message = null;

    public MyFriendsAdapter(Context context, ArrayList<UserNearby> list,
                            SportsApp sportApp) {
        this.mContext = context;
        mSportsApp = sportApp;
        this.mList = list;
        // mList.add(0, self);
        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mDownloader = new ImageDownloader(context);
        mDownloader.setType(ImageDownloader.ICON);
        // loadProgressDialog = new Dialog(context, R.style.sports_dialog);
        // // LayoutInflater mInflater = getLayoutInflater();
        // View v1 = mInflater.inflate(R.layout.sports_progressdialog, null);
        // message = (TextView) v1.findViewById(R.id.message);
        // message.setText("正在发送约跑私信给ta,请稍后!");
        // v1.setMinimumWidth((int) (SportsApp.ScreenWidth * 0.8));
        // loadProgressDialog.setContentView(v1);

        loadProgressDialog = new Dialog(context, R.style.sports_dialog);
        // LayoutInflater mInflater = getLayoutInflater();
        View v1 = mInflater.inflate(R.layout.sports_progressdialog, null);
        message = (TextView) v1.findViewById(R.id.message);
        // message.setText(R.string.sports_wait);
        v1.setMinimumWidth((int) (SportsApp.ScreenWidth * 0.8));
        loadProgressDialog.setContentView(v1);
        loadProgressDialog.setCanceledOnTouchOutside(false);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup arg2) {
        ViewHolder holder = null;
        Log.i(TAG, "lj position=" + position);
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = (LinearLayout) mInflater.inflate(
                    R.layout.sports_addfriend_list_item, null);
            holder.nameTxt = (TextView) convertView
                    .findViewById(R.id.tx_name);
            holder.iconImg = (RoundedImage) convertView
                    .findViewById(R.id.image_icon);
            holder.distance = (TextView) convertView
                    .findViewById(R.id.tx_distance);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.iconImg.setImageDrawable(null);
        if (mList.get(position).getImg() != null
                && !"".equals(mList.get(position).getImg())) {
            if ("man".equals(mList.get(position).getSex())) {
                holder.iconImg
                        .setBackgroundResource(R.drawable.sports_user_edit_portrait_male);
            } else if ("woman".equals(mList.get(position).getSex())) {
                holder.iconImg
                        .setBackgroundResource(R.drawable.sports_user_edit_portrait);
            }
            if (!SportsApp.DEFAULT_ICON
                    .equals(mList.get(position).getImg())) {
                mDownloader.download(mList.get(position).getImg(),
                        holder.iconImg, null);
            }

            holder.nameTxt.setText(mList.get(position).getName());
            int distance = mList.get(position).getDistance();

            String distance_txt;
            int mi;
            if (distance > 1000) {
                float f = distance / 1000.0f;
                distance_txt = mContext.getString(R.string.friends_away_me) + String.format("%.1f", f) + mContext.getString(R.string.sports_kilometers);
                mi = 2;
            } else {
                distance_txt = mContext.getString(R.string.friends_away_me) + distance + mContext.getString(R.string.sports_meters);
                mi = 1;
            }
            SpannableStringBuilder style = new SpannableStringBuilder(
                    distance_txt);
            style.setSpan(new ForegroundColorSpan(Color.parseColor("#f49000")),
                    mContext.getString(R.string.friends_away_me).length(), distance_txt.length() - mi,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            holder.distance.setText(style);
            convertView.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    // TODO Auto-generated method stub
                    if (!mSportsApp.isOpenNetwork()) {
                        Toast.makeText(mContext, "网络未连接，请检查网络！",
                                Toast.LENGTH_LONG).show();
                        return;
                    }
//					Intent intent = new Intent(mContext,
//							PedometerActivity.class);
                    Intent intent = new Intent(mContext,
                            PersonalPageMainActivity.class);
                    intent.putExtra("ID", mList.get(position).getId());
                    mContext.startActivity(intent);
                }
            });
        }
        return convertView;

    }

    private class ViewHolder {
        private TextView distance;
        private RoundedImage iconImg;
        private TextView nameTxt;
    }
}
