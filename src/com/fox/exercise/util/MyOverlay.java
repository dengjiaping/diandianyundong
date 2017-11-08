/*package com.fox.exercise.util;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.baidu.mapapi.map.ItemizedOverlay;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.OverlayItem;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.fox.exercise.map.SportMediaFileDetailActivity;

public class MyOverlay extends ItemizedOverlay{

	private Context context;
	public static OverlayItem mCurItem;
	
	public MyOverlay(Drawable defaultMarker, MapView mapView,Context context) {
		super(defaultMarker, mapView);
		this.context = context;
	}

	@Override
	public boolean onTap(int index){
		OverlayItem item = getItem(index);
		Intent intent = new Intent(context, SportMediaFileDetailActivity.class);
		intent.putExtra("index", index);
		context.startActivity(intent);
		mCurItem = item;
		Log.i("MyOverlay", "## index：" + index);
		
		return true;
	}
	@Override
	public boolean onTap(GeoPoint pt , MapView mMapView){
		return false;
	}
}
*/