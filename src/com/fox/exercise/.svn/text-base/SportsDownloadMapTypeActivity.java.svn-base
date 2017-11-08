package com.fox.exercise;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.View;
import android.view.View.OnClickListener;

import com.umeng.analytics.MobclickAgent;

public class SportsDownloadMapTypeActivity extends AbstractBaseActivity implements OnClickListener {
    private Editor editor;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.baidu_map:
                startActivity(new Intent(this, MofflineMapPage.class));
                editor.putString("downloadMapType", "baidu");
                editor.commit();
                break;
            case R.id.gaode_map:
                startActivity(new Intent(this, MapGaodeActivity.class));
                editor.putString("downloadMapType", "gaode");
                editor.commit();
                break;
            default:
                break;
        }
    }

    @Override
    public void initIntentParam(Intent intent) {
        SharedPreferences mSharedPreferences = getSharedPreferences("sports_download", Context.MODE_PRIVATE);
        editor = mSharedPreferences.edit();
        title = getResources().getString(R.string.sports_download_map);
    }

    @Override
    public void initView() {
        showContentView(R.layout.sports_download_map_type);

    }

    @Override
    public void setViewStatus() {
        findViewById(R.id.baidu_map).setOnClickListener(this);
        findViewById(R.id.gaode_map).setOnClickListener(this);
    }

    @Override
    public void onPageResume() {
        MobclickAgent.onPageStart("SportsDownloadMapTypeActivity");
    }

    @Override
    public void onPagePause() {
        MobclickAgent.onPageEnd("SportsDownloadMapTypeActivity");
    }

    @Override
    public void onPageDestroy() {
        // TODO Auto-generated method stub

    }
}
