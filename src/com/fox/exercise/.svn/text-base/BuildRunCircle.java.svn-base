package com.fox.exercise;

import android.content.Intent;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;

public class BuildRunCircle extends AbstractBaseActivity {
    private GridView gridview_image;

    @Override
    public void setViewStatus() {
        // TODO Auto-generated method stub
    }

    @Override
    public void onPageResume() {
        MobclickAgent.onPageStart("BuildRunCircle");
    }

    @Override
    public void onPagePause() {
        MobclickAgent.onPageEnd("BuildRunCircle");
    }

    @Override
    public void onPageDestroy() {
        // TODO Auto-generated method stub

    }

    @Override
    public void initIntentParam(Intent intent) {
        // TODO Auto-generated method stub
        title = "创建云狐运动圈";
    }

    @Override
    public void initView() {
        // TODO Auto-generated method stub
        left_ayout.removeAllViews();
        TextView tv_cancle = new TextView(this);
        tv_cancle.setText("取消");
        tv_cancle.setTextColor(getResources().getColor(
                R.color.sports_popular_title_normal));
        tv_cancle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        left_ayout.addView(tv_cancle);
        showContentView(R.layout.runcircle);
        init();
    }

    private void init() {
        gridview_image = (GridView) findViewById(R.id.gridview_image);
        gridview_image.setAdapter(new MyBaseAdapter());
    }

    class MyBaseAdapter extends BaseAdapter {

        public MyBaseAdapter() {
            super();
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            // 在此计算添加好友的人数，记得+1，第一个是添加
            return 1;
        }

        @Override
        public Object getItem(int postion) {
            // TODO Auto-generated method stub
            return postion;
        }

        @Override
        public long getItemId(int postion) {
            // TODO Auto-generated method stub
            return postion;
        }

        @Override
        public View getView(int position, View contentView, ViewGroup parent) {
            // TODO Auto-generated method stub
            LayoutInflater inflater = getLayoutInflater();
            ImageView mImageView = null;
            if (contentView == null) {
                contentView = inflater.inflate(R.layout.runcircle_image, null);
            }
            mImageView = (ImageView) contentView
                    .findViewById(R.id.image_run_circle);
            if (position == 0) {
                mImageView.setBackgroundDrawable(getResources().getDrawable(
                        R.drawable.add_new_friends));
            } else {
                // 在此设置添加好友的头像
            }
            return contentView;
        }
    }

    class MyOnItemLisenter implements OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> adapter, View view,
                                int position, long arg3) {
            // TODO Auto-generated method stub
            switch (position) {
                case 0:
                    // 添加好友
                    break;
                // 在此添加点击事件响应后的操作
            }
        }

    }

}
