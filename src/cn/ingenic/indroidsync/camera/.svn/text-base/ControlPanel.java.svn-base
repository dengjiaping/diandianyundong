/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.ingenic.indroidsync.camera;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;
import com.fox.exercise.R;

public class ControlPanel extends FrameLayout implements OnClickListener{
    private Listener listener;
    private final ImageView closeView;  
    
    interface Listener {
	void onClosePreviewWindow();
    }
    
    public ControlPanel(Context context) {
	super(context);
	
	LayoutParams wrapContent =
	    new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
	closeView = new ImageView(context);
	closeView.setImageResource(R.drawable.camera_close_preview_window);
	closeView.setScaleType(ScaleType.CENTER);
	closeView.setMinimumHeight(40);
	closeView.setMinimumWidth(40);
	closeView.setFocusable(true);
	closeView.setClickable(true);
	closeView.setOnClickListener(this);
	addView(closeView, wrapContent);

	RelativeLayout.LayoutParams params =
	    new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
	setLayoutParams(params);
    }
    
    public void setListener(Listener listener) {
	this.listener = listener;
    }

    public View getView() {
	return this;
    }
    
    public void onClick(View view) {
	if (listener != null) {
	    if (view == closeView){
		listener.onClosePreviewWindow();
	    } 
	}
    }
    
    @Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
	closeView.layout(r-closeView.getMeasuredWidth(), b - closeView.getMeasuredHeight(), r, b);

	// Needed, otherwise the framework will not re-layout in case only the padding is changed
	closeView.requestLayout();
    }
    
    @Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
	super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	measureChildren(widthMeasureSpec, heightMeasureSpec);
    }
}
