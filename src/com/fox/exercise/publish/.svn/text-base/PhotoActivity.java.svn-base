package com.fox.exercise.publish;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;

import com.fox.exercise.AbstractBaseActivity;
import com.fox.exercise.R;

import java.util.ArrayList;
import java.util.List;

public class PhotoActivity extends AbstractBaseActivity {

    private ArrayList<View> listViews = null;
    private ViewPager pager;
    private MyPageAdapter adapter;
    private int count;

    public List<Bitmap> bmp = new ArrayList<Bitmap>();
    public List<String> drr = new ArrayList<String>();
    public List<String> del = new ArrayList<String>();
    public int max;

    private int index = 0;
    private Boolean is_delete = false;

    @Override
    public void initIntentParam(Intent intent) {
        index = intent.getExtras().getInt("ID");
        title = (index + 1) + "/" + Bimp.bmp.size();
    }

    @Override
    public void initView() {
        left_ayout.removeAllViews();
        TextView tv_cancle = new TextView(this);
        tv_cancle.setText(getResources().getString(R.string.button_cancel));
        tv_cancle.setTextColor(getResources().getColor(
                R.color.sports_popular_title_normal));
        tv_cancle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        left_ayout.addView(tv_cancle);
        left_ayout.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (is_delete) {
                    Bimp.bmp = bmp;
                    Bimp.drr = drr;
                    Bimp.max = max;
                    for (int i = 0; i < del.size(); i++) {
                        FileUtils.delFile(del.get(i) + ".JPEG");
                    }
                }
                finish();
            }
        });

        for (int i = 0; i < Bimp.bmp.size(); i++) {
            bmp.add(Bimp.bmp.get(i));
        }
        for (int i = 0; i < Bimp.drr.size(); i++) {
            drr.add(Bimp.drr.get(i));
        }
        max = Bimp.max;

        TextView tv_send = new TextView(this);
        tv_send.setText(getResources().getString(R.string.detail_delete));
        tv_send.setTextColor(getResources().getColor(
                R.color.sports_popular_title_normal));
        tv_send.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        showRightBtn(tv_send);
        tv_send.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (listViews.size() == 1) {
                    Bimp.bmp.clear();
                    Bimp.drr.clear();
                    Bimp.max = 0;
                    FileUtils.deleteDir();
                    finish();
                } else {
                    String newStr = drr.get(count).substring(
                            drr.get(count).lastIndexOf("/") + 1,
                            drr.get(count).lastIndexOf("."));
                    bmp.remove(count);
                    drr.remove(count);
                    del.add(newStr);
                    max--;

                    title = (count + 1) + "/" + max;
                    title_tv.setText(title);

                    pager.removeAllViews();
                    listViews.remove(count);
                    adapter.setListViews(listViews);
                    adapter.notifyDataSetChanged();
                }
                is_delete = true;
            }

        });

        showContentView(R.layout.activity_photo);

        pager = (ViewPager) findViewById(R.id.viewpager);
        pager.setOnPageChangeListener(pageChangeListener);
        for (int i = 0; i < bmp.size(); i++) {
            initListViews(bmp.get(i));
        }

        adapter = new MyPageAdapter(listViews);
        pager.setAdapter(adapter);
        Intent intent = getIntent();
        int id = intent.getExtras().getInt("ID");// intent.getIntExtra("ID", 0);
        pager.setCurrentItem(id);
    }

    @Override
    public void setViewStatus() {
    }

    @Override
    public void onPageResume() {
    }

    @Override
    public void onPagePause() {
    }

    @Override
    public void onPageDestroy() {
    }

    private void initListViews(Bitmap bm) {
        if (listViews == null) {
            listViews = new ArrayList<View>();
        }
        ImageView img = new ImageView(this);
        // img.setBackgroundColor(0xff000000);
        img.setImageBitmap(bm);
        img.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT));
        listViews.add(img);
    }

    private OnPageChangeListener pageChangeListener = new OnPageChangeListener() {

        public void onPageSelected(int arg0) {
            title = (arg0 + 1) + "/" + bmp.size();
            title_tv.setText(title);

            count = arg0;
        }

        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        public void onPageScrollStateChanged(int arg0) {
        }
    };

    class MyPageAdapter extends PagerAdapter {

        private ArrayList<View> listViews;
        private int size;

        public MyPageAdapter(ArrayList<View> listViews) {
            this.listViews = listViews;
            size = listViews == null ? 0 : listViews.size();
        }

        public void setListViews(ArrayList<View> listViews) {
            this.listViews = listViews;
            size = listViews == null ? 0 : listViews.size();
        }

        public int getCount() {
            return size;
        }

        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        public void destroyItem(View arg0, int arg1, Object arg2) {
            ((ViewPager) arg0).removeView(listViews.get(arg1 % size));
        }

        public void finishUpdate(View arg0) {
        }

        public Object instantiateItem(View arg0, int arg1) {
            try {
                ((ViewPager) arg0).addView(listViews.get(arg1 % size), 0);

            } catch (Exception e) {
            }
            return listViews.get(arg1 % size);
        }

        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                if (is_delete) {
                    Bimp.bmp = bmp;
                    Bimp.drr = drr;
                    Bimp.max = max;
                    for (int i = 0; i < del.size(); i++) {
                        FileUtils.delFile(del.get(i) + ".JPEG");
                    }
                }
                finish();
                break;
        }
        return super.onKeyDown(keyCode, event);
    }
}
