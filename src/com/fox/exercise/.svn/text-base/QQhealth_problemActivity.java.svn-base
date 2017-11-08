package com.fox.exercise;

import android.content.Intent;
import android.widget.ScrollView;

import com.umeng.analytics.MobclickAgent;

public class QQhealth_problemActivity extends AbstractBaseActivity {
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
        showContentView(R.layout.qqpeoblemexample);
        scroll_yindaoye = (ScrollView) findViewById(R.id.scroll_yindaoye2);
        title_tv.setText(R.string.sports_sendtoqq_me);
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
