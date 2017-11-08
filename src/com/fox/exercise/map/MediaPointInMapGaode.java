package com.fox.exercise.map;

//import java.util.ArrayList;

import android.app.Activity;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MarkerOptions;
import com.fox.exercise.R;
import com.fox.exercise.SmartBarUtils;
import com.fox.exercise.util.SportTrajectoryUtilGaode;
import com.umeng.analytics.MobclickAgent;

import cn.ingenic.indroidsync.SportsApp;

//import android.util.Log;
//import com.fox.exercise.api.entity.SportMediaFile;

public class MediaPointInMapGaode extends Activity implements OnClickListener {

    protected static final String TAG = "MediaPointInMapGaode";

    private MapView mMapView = null;
    private AMap aMap;
    private UiSettings mUiSettings;

    private ImageButton backBtn;

//	public static ArrayList<SportMediaFile> mediaFilesList;

    private float zoomValue = 18;

    private double dLat = 0.0f;
    private double dLng = 0.0f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        boolean findMethod = SmartBarUtils.findActionBarTabsShowAtBottom();
        if (!findMethod) {
            getWindow().setUiOptions(0);
            getWindow().requestFeature(android.view.Window.FEATURE_NO_TITLE);
        }

        setContentView(R.layout.mediapoint_map_gaode);
        mMapView = (MapView) findViewById(R.id.bmapView);
        mMapView.onCreate(savedInstanceState);// 此方法必须重写

        if (aMap == null) {
            aMap = mMapView.getMap();
            mUiSettings = aMap.getUiSettings();
            mUiSettings.setZoomControlsEnabled(false);
            mUiSettings.setZoomGesturesEnabled(true);
            mUiSettings.setMyLocationButtonEnabled(false); // 是否显示默认的定位按钮
        }

        Bundle bundle = new Bundle();
        bundle.putString("message", getResources().getString(R.string.sports_wait));

        initView();
        if (findMethod) {
            getActionBar().setDisplayShowHomeEnabled(false);
            getActionBar().setDisplayShowTitleEnabled(false);
            SmartBarUtils.setActionModeHeaderHidden(getActionBar(), true);
            SmartBarUtils.setActionBarViewCollapsable(getActionBar(), true);
        }
    }

    private void initView() {
        backBtn = (ImageButton) findViewById(R.id.sport_media_back);
        backBtn.setOnClickListener(this);

        Bundle b = getIntent().getExtras();
        String pointStr = b.getString("point");
        int mapType = b.getInt("mapType");
        if (mapType == SportsApp.MAP_TYPE_BAIDU) {
            dLat = 0.0060f;
            dLng = 0.0065f;
        }
        LatLng point = SportTrajectoryUtilGaode.stringToLatLngPoint(pointStr, dLat, dLng);
        aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(point, zoomValue), null);
        int type = b.getInt("mediaType");
        addMediaItem(point, type);

    }

    private void addMediaItem(LatLng point, int typeID) {
        MarkerOptions marker = null;
        if (typeID == 1) {
            marker = new MarkerOptions().icon(BitmapDescriptorFactory
                    .fromBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher)));
        } else if (typeID == 2) {
            marker = new MarkerOptions().icon(BitmapDescriptorFactory
                    .fromBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher)));
        } else if (typeID == 3) {
            marker = new MarkerOptions().icon(BitmapDescriptorFactory
                    .fromBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher)));
        }
        aMap.addMarker(marker.position(point));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sport_media_back:
                finish();
                break;
        }
    }

    @Override
    protected void onPause() {
        if (mMapView != null) {
            mMapView.onPause();
        }
        MobclickAgent.onPageEnd("MediaPointInMapGaode");
        MobclickAgent.onPause(this);
        super.onPause();
    }

    @Override
    protected void onResume() {
        if (mMapView != null) {
            mMapView.onResume();
        }
        super.onResume();
        MobclickAgent.onPageStart("MediaPointInMapGaode");
        MobclickAgent.onResume(this);

    }

    @Override
    protected void onDestroy() {
        if (aMap != null) {
            aMap.clear();
            aMap = null;
        }
        if (mMapView != null) {
            mMapView.onDestroy();
        }
        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mMapView != null) {
            mMapView.onSaveInstanceState(outState);
        }
    }

    @Override
    public void onBackPressed() {
        finish();
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
