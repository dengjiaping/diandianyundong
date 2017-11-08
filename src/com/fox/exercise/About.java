package com.fox.exercise;

import android.content.Intent;

public class About  extends AbstractBaseActivity{
	
	@Override
	public void initIntentParam(Intent intent) {
		title=getResources().getString(R.string.about);
	}
	@Override
	public void initView() {
		// TODO Auto-generated method stub
		showContentView(R.layout.about);
	}
	@Override
	public void setViewStatus() {
		// TODO Auto-generated method stub
		
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
		
}
