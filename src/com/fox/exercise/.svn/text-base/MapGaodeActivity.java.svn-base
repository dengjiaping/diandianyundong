package com.fox.exercise;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.fox.exercise.MofflineGaodeExpand.onCheckedPageGaodeListener;
import com.fox.exercise.view.MoffMapSwitchView;
import com.fox.exercise.view.MoffMapSwitchView.OnCheckedChangeListener;
import com.umeng.message.PushAgent;

import java.util.ArrayList;

import cn.ingenic.indroidsync.SportsApp;

/**
 * AMapV2地图中简单介绍离线地图下载
 */
public class MapGaodeActivity extends FragmentActivity implements OnClickListener {
    private ViewPager mPager;
    private ArrayList<Fragment> fragmentList;
    private MoffMapSwitchView mapSwitchView;
    private boolean isSwith = false;
    private SportsApp mSportsApp;

    @Override
    protected void onCreate(Bundle arg0) {
        // TODO Auto-generated method stub
        super.onCreate(arg0);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.sports_moff_friends);
        TextView textView = (TextView) this.findViewById(R.id.mapText);
        textView.setText(R.string.sports_map_gaode);
        InitViewPager();
        mSportsApp = (SportsApp) getApplication();
        mSportsApp.addActivity(this);
        mSportsApp.setDownloading(true);
        //友盟推送
        PushAgent.getInstance(this).onAppStart();
    }

    @Override
    protected void onStart() {
        super.onStart();
        SportsApp app = (SportsApp) getApplication();
        SharedPreferences sp = getSharedPreferences("user_login_info", Context.MODE_PRIVATE);
        if (!"".equals(sp.getString("account", "")) && app.LoginOption) {
            if (app.getSessionId() == null || app.getSessionId().equals("")) {
                finish();
            }
        } else if ("".equals(sp.getString("account", "")) && app.LoginOption) {
            if (app.getSessionId() == null || app.getSessionId().equals("")) {
                finish();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSportsApp.removeActivity(this);
    }

    private void InitViewPager() {
        // TODO Auto-generated method stub
        findViewById(R.id.bt_back).setOnClickListener(this);
        mPager = (ViewPager) findViewById(R.id.moff_viewpager);
        mapSwitchView = (MoffMapSwitchView) findViewById(R.id.switch_moff);
        fragmentList = new ArrayList<Fragment>();
        MofflineGaodeExpand exPandFragment = new MofflineGaodeExpand();
        MofflineListGaodeView listViewFragment = new MofflineListGaodeView();
        fragmentList.add(exPandFragment);
        fragmentList.add(listViewFragment);
        mPager.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager(),
                fragmentList));
        mPager.setCurrentItem(0);
        mPager.setOffscreenPageLimit(2);
        mPager.setOnPageChangeListener(new MyOnPageChangeListener());
        mapSwitchView.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(boolean isChecked) {
                // TODO Auto-generated method stub
                isSwith = true;
                if (isChecked) {
                    mPager.setCurrentItem(0);
                } else {
                    mPager.setCurrentItem(1);
                }

            }
        });
        exPandFragment.setPageGaodeListener(new onCheckedPageGaodeListener() {

            @Override
            public void onCheckedPage() {
                // TODO Auto-generated method stub
                mPager.setCurrentItem(1);
            }

        });

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK: {
                //地图正在下载中不让直接退出。
                if (!mSportsApp.isDownloading()) {
                    Toast.makeText(MapGaodeActivity.this,
                            getResources().getString(R.string.offmap_downloading),
                            Toast.LENGTH_SHORT).show();
                } else {
                    finish();
                }
                return true;
            }

        }
        return super.onKeyDown(keyCode, event);
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
        public Fragment getItem(final int arg0) {

            return list.get(arg0);
        }

    }

    class MyOnPageChangeListener implements OnPageChangeListener {

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onPageSelected(int position) {
            // TODO Auto-generated method stub
            if (isSwith == true) {
                isSwith = false;
            } else {
                mapSwitchView.onCheckPosition(position + 1);
            }
        }
    }

    @Override
    public void onClick(View arg0) {
        // TODO Auto-generated method stub
        switch (arg0.getId()) {
            case R.id.bt_back:
                if (!mSportsApp.isDownloading()) {
                    Toast.makeText(MapGaodeActivity.this,
                            getResources().getString(R.string.offmap_downloading),
                            Toast.LENGTH_SHORT).show();
                } else {
                    finish();
                }
                break;
        }
    }

    public void onResume() {
        super.onResume();
    }

    public void onPause() {
        super.onPause();
    }

    /**
     *@method 固定字体大小方法
     *@author suhu
     *@time 2016/10/11 13:23
     *@param
     *
     */
    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        Configuration config=new Configuration();
        config.setToDefaults();
        res.updateConfiguration(config,res.getDisplayMetrics() );
        return res;
    }
}
