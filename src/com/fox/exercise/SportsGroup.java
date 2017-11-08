package com.fox.exercise;

import java.util.ArrayList;

import cn.ingenic.indroidsync.SportsApp;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.TranslateAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;


public class SportsGroup extends AbstractBaseFragment implements OnClickListener {
    private ArrayList<Fragment> fragmentList;
    private ImageView image;
    private int mMoveSize;
    private ImageButton iView;
    private TextView[] tViews;
    private ViewPager mPager;
    private SportsApp gSportsApp;
    private static final int creat_new_group = 111;
    private int mSelectionPosition = 0;

    @Override
    public void beforeInitView() {
        // TODO Auto-generated method stub
        title = getResources().getString(R.string.sports_group);
    }

    @Override
    public void setViewStatus() {
        // TODO Auto-generated method stub
        top_title_layout.addView(LayoutInflater.from(getActivity()).inflate(R.layout.sports_group_title, null));
        setContentView(R.layout.sports_page_group);
        gSportsApp = SportsApp.getInstance();
        iView = new ImageButton(getActivity());
        iView.setBackgroundDrawable(getResources().getDrawable(
                R.drawable.title_sports_creat));
        iView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        right_btn.setPadding(0, 0, gSportsApp.dip2px(17), 0);
        showRightBtn(iView);
        iView.setId(creat_new_group);
        iView.setOnClickListener(this);
        InitTextView();
        InitImage();
        InitViewPager();
    }

    @Override
    public void onPageResume() {
        // TODO Auto-generated method stub
    }

    @Override
    public void onPagePause() {
        // TODO Auto-generated method stub
    }

    @Override
    public void onPageDestroy() {
        // TODO Auto-generated method stub
    }

    private void InitTextView() {
        // TODO Auto-generated method stub
        TextView view1 = (TextView) getActivity().findViewById(R.id.tab_sports_group);
        TextView view2 = (TextView) getActivity().findViewById(R.id.tab_sports_group_me);
        tViews = new TextView[]{view1, view2};
        view1.setOnClickListener(new txListener(0));
        view2.setOnClickListener(new txListener(1));
    }

    private void InitImage() {
        // TODO Auto-generated method stub
        image = (ImageView) getActivity().findViewById(R.id.cursor_sports_group);
        BitmapDrawable bitmap = (BitmapDrawable) image.getDrawable();
        ;
        mMoveSize = bitmap.getBitmap().getWidth() + gSportsApp.dip2px(2);
    }

    private void InitViewPager() {
        // TODO Auto-generated method stub
        mPager = (ViewPager) getActivity().findViewById(R.id.viewpager_sportsgroup);
        fragmentList = new ArrayList<Fragment>();
        Fragment priFragment = new SportsGroupAll();
        Fragment friendFragment = new SportsGroupMe();
        //Fragment findFragment = new FindFragment();
        fragmentList.add(priFragment);
        fragmentList.add(friendFragment);
        //fragmentList.add(findFragment);
        mPager.setAdapter(new MyFragmentPagerAdapter(getChildFragmentManager(),
                fragmentList));
        mPager.setCurrentItem(0);
        mPager.setOffscreenPageLimit(2);
        mPager.setOnPageChangeListener(new MyOnPageChangeListener());
    }

    class txListener implements View.OnClickListener {
        private int index = 0;

        public txListener(int i) {
            index = i;
        }

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            mPager.setCurrentItem(index);
        }
    }

    class MyFragmentPagerAdapter extends FragmentStatePagerAdapter {
        ArrayList<Fragment> list;

        public MyFragmentPagerAdapter(FragmentManager fm, ArrayList<Fragment> list) {
            super(fm);
            this.list = list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Fragment getItem(int arg0) {
            return list.get(arg0);
        }
    }

    class MyOnPageChangeListener implements OnPageChangeListener {

        public void onPageScrolled(int arg0, float arg1, int arg2) {
            // TODO Auto-generated method stub
        }

        public void onPageScrollStateChanged(int arg0) {
            // TODO Auto-generated method stub
        }

        public void onPageSelected(int position) {
            // TODO Auto-generated method stub
            tViews[position].setTextColor(Color.parseColor("#25a7f2"));
            tViews[mSelectionPosition].setTextColor(Color.parseColor("#ffffffff"));
            TranslateAnimation anim = new TranslateAnimation(mMoveSize
                    * mSelectionPosition, mMoveSize * position, 0, 0);
            anim.setFillAfter(true);
            anim.setDuration(200);
            image.startAnimation(anim);
            mSelectionPosition = position;
        }
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case creat_new_group:
                getActivity().startActivity(new Intent(getActivity(), BuildRunCircle.class));
                break;

            default:
                break;
        }
    }

}
