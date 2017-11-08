package com.fox.exercise.newversion.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.VideoView;

public class TrainVideoView extends VideoView {
	private int width, height;

	public TrainVideoView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public TrainVideoView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public TrainVideoView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		// 默认高度，为了自动获取到focus
		int width = MeasureSpec.getSize(widthMeasureSpec);
		int height = width;
		// 这个之前是默认的拉伸图像
		if (this.width > 0 && this.height > 0) {
			width = this.width;
			height = this.height;
		}
		setMeasuredDimension(width, height);
	}

	public void setMeasure(int width, int height) {
		this.width = width;
		this.height = height;
	}
}
