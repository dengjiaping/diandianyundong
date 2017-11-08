package com.fox.exercise;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import cn.ingenic.indroidsync.SportsApp;

import com.fox.exercise.api.entity.UserNearby;
import com.fox.exercise.newversion.act.PersonalPageMainActivity;


public class VisitorMyAdapter extends BaseAdapter {

    private ArrayList<UserNearby> mList = null;
    private Context mContext = null;
    private LayoutInflater mInflater = null;
    public int indexGL = -1;
    public int indexRZ = -1;
    private ImageDownloader mDownloader = null;
    private ImageDownloader mIconDownloader = null;
    private static final String TAG = "VisitorMyAdapter";
    private SportsApp mSportsApp = SportsApp.getInstance();

    public VisitorMyAdapter(ArrayList<UserNearby> list, Context context) {
        this.mContext = context;
        this.mList = list;
        this.mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mDownloader = new ImageDownloader(mContext);
        mDownloader.setType(ImageDownloader.GIFT);
        mIconDownloader = new ImageDownloader(mContext);
        mIconDownloader.setSize(90, 90);
        mIconDownloader.setType(ImageDownloader.ICON);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int arg0) {
        return null;
    }

    @Override
    public long getItemId(int arg0) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup arg2) {
        LinearLayout layout = null;

        if (convertView == null) {
            layout = (LinearLayout) mInflater.inflate(R.layout.sports_fans_my_ltem, null);
        } else {
            layout = (LinearLayout) convertView;
        }
        if (mList.get(position).getId() == 13016) {
            Log.e("indexGL>>>", position + "");
            indexGL = position;
        }
        if (mList.get(position).getId() == 53794) {
            Log.e("indexRZ>>>", position + "");
            indexRZ = position;
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
                if (mList != null && position < mList.size()) {
                    if (!mSportsApp.isOpenNetwork()) {
                        Toast.makeText(mContext, "网络未连接，请检查网络！", Toast.LENGTH_LONG)
                                .show();
                        return;
                    }
//					Intent intent = new Intent(mContext, PedometerActivity.class);
                    Intent intent = new Intent(mContext, PersonalPageMainActivity.class);
                    intent.putExtra("ID", mList.get(position).getId());
                    mContext.startActivity(intent);
                }
            }
        });

        TextView nameTxt = (TextView) layout.findViewById(R.id.focus_name_txt);
        if (position == indexGL || position == indexRZ) {
            nameTxt.setText(mList.get(position).getName());
        } else {
            nameTxt.setText(mList.get(position).getName());
        }

        TextView timeTxt = (TextView) layout.findViewById(R.id.tx_message);
        long time = System.currentTimeMillis() - mList.get(position).getTime() * 1000;
        Log.e(TAG, "此刻时间-------------------" + System.currentTimeMillis());
        Log.e(TAG, "访问时间--------------------" + mList.get(position).getTime() * 1000);

        if (time <= 60 * 1000)
            timeTxt.setText("刚刚");
        else if (time <= 60 * 60 * 1000) {
            int h = (int) (time / 1000 / 60);
            timeTxt.setText("" + h + "分钟前");
        } else {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:MM");
            timeTxt.setText(format.format(mList.get(position).getTime() * 1000));
            Log.e(TAG, "测试时间时间1--------------------" + format.format(1415929123 * 1000L));
            Log.e(TAG, "测试时间时间2--------------------" + format.format(1415929040 * 1000L));
            Log.e(TAG, "测试时间时间3--------------------" + format.format(1415928854 * 1000L));
        }

        return layout;
    }

    public int getIndexGL() {
        return indexGL;
    }

    public void setIndexGL(int indexGL) {
        this.indexGL = indexGL;
    }

    public int getIndexRZ() {
        return indexRZ;
    }

    public void setIndexRZ(int indexRZ) {
        this.indexRZ = indexRZ;
    }

}
