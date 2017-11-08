/*package com.fox.exercise.map;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.baidu.mapapi.map.MapView;

class MyLocationMapView extends MapView{
	SportingMapActivity listener;
	
	public MyLocationMapView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	public MyLocationMapView(Context context, AttributeSet attrs){
		super(context,attrs);
	}
	public MyLocationMapView(Context context, AttributeSet attrs, int defStyle){
		super(context, attrs, defStyle);
	}
	public void setListener(SportingMapActivity l){
		listener =l;
	}
	@Override
    public boolean onTouchEvent(MotionEvent event){
		switch(event.getAction()){ 
		case MotionEvent.ACTION_DOWN: 
		case MotionEvent.ACTION_MOVE: 
			listener.RemoveMSG();
			break;
		case MotionEvent.ACTION_UP: 
			listener.SendMSG();
			break;

	}
	return super.onTouchEvent(event);
	}
}*/
