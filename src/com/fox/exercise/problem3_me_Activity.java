package com.fox.exercise;

import android.content.Intent;
import android.widget.ScrollView;

import com.umeng.analytics.MobclickAgent;

public class problem3_me_Activity extends AbstractBaseActivity {
    private ScrollView scroll_yindaoye;

    @Override
    public void initIntentParam(Intent intent) {
        // TODO Auto-generated method stub

    }

    @Override
    public void initView() {
        // TODO Auto-generated method stub

    }

    @Override
    public void setViewStatus() {
        // TODO Auto-generated method stub
        showContentView(R.layout.peoblemexample3);
        scroll_yindaoye = (ScrollView) findViewById(R.id.scroll_yindaoye3);
        title_tv.setText(getResources().getString(R.string.phone_no_record));
    }

    @Override
    public void onPageResume() {
        // TODO Auto-generated method stub
        MobclickAgent.onPageStart("QQhealth_problemActivity");
    }

    @Override
    public void onPagePause() {
        // TODO Auto-generated method stub
        MobclickAgent.onPageEnd("QQhealth_problemActivity");
    }

    @Override
    public void onPageDestroy() {
        // TODO Auto-generated method stub

    }

}
