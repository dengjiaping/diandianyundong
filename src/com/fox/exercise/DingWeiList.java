package com.fox.exercise;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;

import java.util.List;


public class DingWeiList extends AbstractBaseActivity implements PoiSearch.OnPoiSearchListener,
        AMapLocationListener, LocationSource, AMap.OnMarkerClickListener, AMap.OnMapClickListener {

    ListView dwlist; // 声明一个ListView对象
    private MapView mapView;
    private Marker currentMarker;
    private TextView no_address;
    public static String name;
    LinearLayout lin;
    String city, no_city;
    PoiSearch poiSearch;
    int id = 110;
    private int currentPage = 0;// 当前页面，从0开始计数
    LatLonPoint point;
    //    private OnLocationChangedListener mListener;
    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    //声明AMapLocationClientOption对象
    public AMapLocationClientOption mLocationOption = null;
    private List<PoiItem> poiItems;// poi数据
    private PoiSearch.Query query;// Poi查询条件类
    private PoiResult poiResult; // poi返回的结果
    private AMap aMap;

    @Override
    public void initIntentParam(Intent intent) {
        title = "所在位置";

    }

    @Override
    public void initView() {

    }

    @Override
    public void setViewStatus() {
        showContentView(R.layout.dingwei_listview);
        dwlist = (ListView) findViewById(R.id.dingwei_list);
        mapView = (MapView) findViewById(R.id.map);
        no_address = (TextView) findViewById(R.id.no_dizhi);
//        now_city = (TextView) findViewById(R.id.now_city);
//        now_city.setText(city);
//        now_city.setOnClickListener(new NowAddressClicklister());
        no_city = no_address.getText().toString().trim();
        lin = (LinearLayout) findViewById(R.id.lin_shuaxin);
        no_address.setOnClickListener(new AddressClicklister());
//        centerImageView = (ImageView) findViewById(R.id.centerMarkerImg);
//        centerView = (LinearLayout) findViewById(R.id.centerView);
        mapView.onCreate(bundle);// 此方法必须重写
        initmap();
        leftButton.setId(+id);
        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("onback", "");
                setResult(RESULT_OK, intent);
                finish();
            }
        });

    }

    private void initmap() {
        if (aMap == null) {
            aMap = mapView.getMap();
//            mUiSettings = aMap.getUiSettings();
        }
        aMap.setLocationSource(this);// 设置定位监听
        aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
        // 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        aMap.setMyLocationEnabled(true);
        aMap.setOnMarkerClickListener(this);
    }

    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (amapLocation != null) {
            if (amapLocation.getErrorCode() == 0) {
                city = amapLocation.getCity();
                if (point == null) {
                    point = new LatLonPoint(amapLocation.getLatitude(), amapLocation.getLongitude());
                } else {
                    point.setLatitude(amapLocation.getLatitude());
                    point.setLongitude(amapLocation.getLongitude());
                }
                doSearchQuery();
            } else {
                String errText = "定位失败," + amapLocation.getErrorCode() + ": "
                        + amapLocation.getErrorInfo();
                Log.e("AmapErr", errText);
            }
        }
    }


    @Override
    public void onPoiSearched(PoiResult result, int rcode) {
        if (rcode == 1000) {
            if (result != null && result.getQuery() != null) {// 搜索poi的结果
                if (result.getQuery().equals(query)) {// 是否是同一条
                    poiResult = result;
                    if (null != poiItems) {
                        poiItems.clear();
                    }
                    poiItems = poiResult.getPois();// 取得第一页的poiitem数据，页数从数字0开始
//                    Toast.makeText(this, poiItems.get(0).getTitle(), Toast.LENGTH_SHORT).show();
                    AddressAdapter adapter = new AddressAdapter(
                            getApplicationContext(), poiItems,
                            R.layout.dingwei_item);
                    dwlist.setAdapter(adapter);
                    lin.setVisibility(View.GONE);
                    dwlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                        @Override
                        public void onItemClick(AdapterView<?> parent,
                                                View view, int position, long id) {
                            // TODO Auto-generated method stub
                            TextView text = (TextView) view
                                    .findViewById(R.id.dingwei_name);
                            // Toast.makeText(DingWeiList.this,
                            // text.getText().toString(), 10).show();
                            Intent intent = new Intent();
                            intent.putExtra("address", text.getText()
                                    .toString());
                            setResult(RESULT_OK, intent);
                            finish();

                        }
                    });
                    // List<SuggestionCity> suggestionCities = poiResult
                    // .getSearchSuggestionCitys();
                    // 当搜索不到poiitem数据时，会返回含有搜索关键字的城市信息
                } else {
                    ToastUtil.show(this, R.string.no_result);
                }
            } else {
                ToastUtil.show(this, R.string.no_result);
            }
        } else {
            ToastUtil.show(this, getString(R.string.error_other) + rcode);
        }
    }

    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {

    }


    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        if (mLocationClient == null) {
            //初始化定位
            mLocationClient = new AMapLocationClient(this);
            //设置定位回调监听
            mLocationClient.setLocationListener(this);
            //初始化AMapLocationClientOption对象
            mLocationOption = new AMapLocationClientOption();
            // 设置为高精度定位模式
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            //给定位客户端对象设置定位参数
            mLocationClient.setLocationOption(mLocationOption);
            //启动定位
            mLocationClient.startLocation();
        }
    }

    @Override
    public void deactivate() {
        if (mLocationClient != null) {
            mLocationClient.stopLocation();
            mLocationClient.onDestroy();
        }
        mLocationClient = null;
    }


    /**
     * 开始进行poi搜索
     */
    private void doSearchQuery() {
        currentPage = 0;
        // 第一个参数表示搜索字符串，第二个参数表示poi搜索类型，第三个参数表示poi搜索区域（空字符串代表全国）
        query = new PoiSearch.Query("", "地名地址信息", city);
        query.setPageSize(30);// 设置每页最多返回多少条poiitem
        query.setPageNum(currentPage);// 设置查第一页
        poiSearch = new PoiSearch(this, query);
        poiSearch.setOnPoiSearchListener(this);// 设置回调数据的监听器
        // 所有poi
        query.setCityLimit(false);
        if (point != null) {
            poiSearch = new PoiSearch(this, query);
            poiSearch.setOnPoiSearchListener(this);
            // 设置搜索区域为以lp点为圆心，其周围5000米范围
            poiSearch.setBound(new PoiSearch.SearchBound(point, 5000));
            poiSearch.searchPOIAsyn();// 异步搜索
        }
    }


    @Override
    public boolean onMarkerClick(Marker marker) {
        currentMarker = marker;
        return false;
    }

    @Override
    public void onMapClick(LatLng latLng) {
        if (currentMarker != null) {
            currentMarker.hideInfoWindow();
        }
    }

    class AddressClicklister implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.putExtra("address", no_city);
            setResult(RESULT_OK, intent);
            finish();
        }

    }

    class AddressAdapter extends CommonAdapter<PoiItem> {

        public AddressAdapter(Context context, List<PoiItem> mDatas,
                              int itemLayoutId) {
            super(context, mDatas, itemLayoutId);
        }

        @Override
        public void convert(ViewHolder helper, PoiItem item) {
            helper.setText(R.id.dingwei_name, item.getTitle());
            helper.setText(
                    R.id.dingwei_dizhi,
                    item.getProvinceName() + item.getCityName()
                            + item.getAdName());
        }

    }

    class NowAddressClicklister implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.putExtra("address", city);
            setResult(RESULT_OK, intent);
            finish();
        }

    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent();
            intent.putExtra("onback", "");
            setResult(RESULT_OK, intent);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 此方法必須重写
     *
     * @param outState
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    public void onPageResume() {
        mapView.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    public void onPagePause() {
        mapView.onPause();
    }

    /**
     * 方法必须重写
     */
    @Override
    public void onPageDestroy() {
        mapView.onDestroy();
    }
}
