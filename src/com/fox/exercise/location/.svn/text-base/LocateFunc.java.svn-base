/*package com.fox.exercise.location;

import android.content.Context;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.LocationData;
import com.fox.exercise.SporterBundle;


public class LocateFunc {

	private LocationClient mLocClient = null;
	private LocationData locData = null;
	public MyLocationListenner myListener = new MyLocationListenner();

	private Context mContext = null;
	private boolean mGcj02 = false;

//	public static boolean LOCATE_DOING = false;
//	public static boolean LOCATE_FINISH = false;

	private static final String TAG = "LocateFunc";

	public LocateFunc(Context context) {
		mContext = context;
	}
	
	public LocateFunc(Context context, boolean gcj02) {
		mContext = context;
		mGcj02 = gcj02;
	}

	public void start() {
		Log.d(TAG, "locate start");
		 //定位初始化
		mLocClient = new LocationClient(mContext);
		LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);//打开gps
        option.setScanSpan(3000);
		if(mGcj02){
			//设置坐标类型
			option.setCoorType("bd09ll");
		}
        locData = new LocationData();
        mLocClient.registerLocationListener( myListener );
        mLocClient.setLocOption(option);
        mLocClient.start();
	}

	public LocationData getLocation() {
		if(locData != null){
			return locData;
		}
		return null;
	}

//	public class LocateReciveListener implements ReceiveListener {
//
//		@Override
//		public void onReceive(String res) {
//			if (res != null) {
//				Log.d(TAG, res);
//				LocationJsonParse parse = new LocationJsonParse(res);
//				SporterBundle.getInstance().setLocation(parse.parse());
//				SporterBundle.getInstance().setLocationName(parse.getLocateName());
//
////				Toast.makeText(mContext, mContext.getString(R.string.sports_autolocate_success), Toast.LENGTH_SHORT)
////						.show();
////				LOCATE_DOING = false;
////				LOCATE_FINISH = true;
//			}
//			if(mHandler != null){
//				mHandler.sendEmptyMessage(0);
//			}
//		}
//
//	}
//
//	public class LocateChangeListener implements LocationChangedListener {
//
//		@Override
//		public void onLocationChanged() {
//			mClient.getLocation();
//		}
//
//	}
	*//**
 * 定位SDK监听函数
 *//*
    public class MyLocationListenner implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			if (location == null){
				return;
			}
			locData.latitude = location.getLatitude();
			locData.longitude = location.getLongitude();
			Log.i("location", "这里" +locData.latitude+","+locData.longitude);
			// 如果不显示定位精度圈，将accuracy赋值为0即可
			locData.accuracy = location.getRadius();
			locData.direction = location.getDerect();
		}

		public void onReceivePoi(BDLocation poiLocation) {
			if (poiLocation == null) {
				return;
			}
		}
	}

	public void stop() {
		if (mLocClient != null)
			mLocClient.stop();
	}
}*/
