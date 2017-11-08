package com.fox.exercise.view;

import com.fox.exercise.R;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SwitchViewGaoDe extends LinearLayout implements OnClickListener {

    private Context context; // 上下文对象
    private RelativeLayout sv_container; // SwitchView的外层Layout
    private ImageButton iv_switch_cursor; // 开关邮标的ImageView
    private TextView switch_text_true; // true的文字信息控件
    private TextView switch_text_false; // false的文字信息控件

    private boolean isChecked = true; // 是否已开
    private boolean checkedChange = false; // isChecked是否有改变
    private int mSelectionLeft = 0;
    private int mSelectionRight = 1;
    private int iv_switch_width;
    private Animation animation; // 移动动画
    private OnCheckedChangeListener onCheckedChangeListener;

    public SwitchViewGaoDe(Context context) {
        super(context);
        this.context = context;
        initView();
    }

    public SwitchViewGaoDe(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initView();

    }

    /**
     * 初始化控件
     */
    private void initView() {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.switch_view, this);
        sv_container = (RelativeLayout) view.findViewById(R.id.sv_container);
        switch_text_true = (TextView) view.findViewById(R.id.switch_text_true);
        switch_text_false = (TextView) view
                .findViewById(R.id.switch_text_false);
        switch_text_true.setTextColor(getResources().getColor(R.color.text_moffmap));
        switch_text_false.setTextColor(Color.WHITE);
        switch_text_true.setOnClickListener(this);
        switch_text_false.setOnClickListener(this);
        iv_switch_cursor = (ImageButton) view.findViewById(R.id.iv_switch_cursor);
        mSelectionLeft = sv_container.getLayoutParams().width / 2;
        Log.i("", "mSelectionLeft" + mSelectionLeft + "==iv_switch_cursor:" + iv_switch_cursor.getLayoutParams().width);
//		iv_switch_cursor.setMinimumWidth(mSelectionLeft);
        /*sv_container.setOnClickListener(new OnClickListener() {
            @Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(mSelectionRight==1){
					moveSelection(2);
					changeTextColor();
				}else{
				 moveSelection(1);
				 changeTextColor();
				}
				isChecked = !isChecked;
				if (onCheckedChangeListener != null) {
					onCheckedChangeListener.onCheckedChanged(isChecked);
				}
			}
		});*/
    }

    private void changeTextColor() {
        if (isChecked) {
            switch_text_true.setTextColor(Color.WHITE);
            switch_text_false.setTextColor(getResources().getColor(R.color.text_moffmap));
        } else {
            switch_text_true.setTextColor(getResources().getColor(R.color.text_moffmap));
            switch_text_false.setTextColor(Color.WHITE);
        }
    }


    private void moveSelection(int position) {
        TranslateAnimation anim = new TranslateAnimation(mSelectionLeft
                * (mSelectionRight - 1), mSelectionLeft * (position - 1), 0, 0);
        anim.setFillAfter(true);
        anim.setDuration(300);
        anim.setFillEnabled(true);
        iv_switch_cursor.startAnimation(anim);
        mSelectionRight = position;
    }

    public void setOnCheckedChangeListener(
            OnCheckedChangeListener onCheckedChangeListener) {
        this.onCheckedChangeListener = onCheckedChangeListener;
    }

    public interface OnCheckedChangeListener {
        void onCheckedChanged(boolean isChecked);
    }

    int colorNumA = 0;
    int colorNumB = 0;

    @Override
    public void onClick(View arg0) {
        // TODO Auto-generated method stub
        switch (arg0.getId()) {
            case R.id.switch_text_true:
                if (colorNumA == 0) {
                    moveSelection(1);
                    changeTextColor();
                    colorNumA++;
                    colorNumB = 0;
                    isChecked = !isChecked;
                    if (onCheckedChangeListener != null)
                        onCheckedChangeListener.onCheckedChanged(isChecked);
                    break;
                }

            case R.id.switch_text_false:
                if (colorNumB == 0) {
                    moveSelection(2);
                    changeTextColor();
                    colorNumB++;
                    colorNumA = 0;
                    isChecked = !isChecked;
                    if (onCheckedChangeListener != null)
                        onCheckedChangeListener.onCheckedChanged(isChecked);
                }
                break;
            default:
                break;
        }
    }
}

