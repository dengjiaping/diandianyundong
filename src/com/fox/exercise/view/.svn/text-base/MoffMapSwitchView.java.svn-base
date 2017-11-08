package com.fox.exercise.view;

import cn.ingenic.indroidsync.SportsApp;

import com.fox.exercise.R;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MoffMapSwitchView extends LinearLayout implements OnClickListener {

    private Context context; // 上下文对象
    //	private LinearLayout sv_moff_container; // SwitchView的外层Layout
    private ImageView iv_switch_cursor; // 开关邮标的ImageView
    private TextView switch_moff_true; // true的文字信息控件
    private TextView switch_moff_false; // false的文字信息控件

    private boolean isChecked = true; // 是否已开
    //	private boolean checkedChange = false; // isChecked是否有改变
    private int mSelectionLeft = 0;
    private int mSelectionRight = 1;
    //    private int iv_switch_width ;
//	private Animation animation; // 移动动画
    private OnCheckedChangeListener onCheckedChangeListener;

    public MoffMapSwitchView(Context context) {
        super(context);
        this.context = context;
        initView();
    }

    public MoffMapSwitchView(Context context, AttributeSet attrs) {
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
        View view = inflater.inflate(R.layout.switch_moff_map, this);
//		sv_moff_container = (LinearLayout) view.findViewById(R.id.sv_moff_container);
        switch_moff_true = (TextView) view.findViewById(R.id.moff_cityButton);
        switch_moff_false = (TextView) view.findViewById(R.id.moff_localButton);
        switch_moff_true.setOnClickListener(this);
        switch_moff_false.setOnClickListener(this);
        switch_moff_true.setTextColor(getResources().getColor(R.color.text_moffmap));
        switch_moff_false.setTextColor(Color.WHITE);

        iv_switch_cursor = (ImageView) view.findViewById(R.id.tab_moff_focus);
        int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        iv_switch_cursor.measure(w, h);
        mSelectionLeft = iv_switch_cursor.getMeasuredWidth() + SportsApp.getInstance().dip2px(2);
    }

    private void changeTextColor() {
        if (isChecked) {
            switch_moff_true.setTextColor(Color.WHITE);
            switch_moff_false.setTextColor(getResources().getColor(R.color.text_moffmap));
            //iv_switch_cursor.setBackgroundResource(R.drawable.sports_title_bg);
        } else {
            switch_moff_true.setTextColor(getResources().getColor(R.color.text_moffmap));
            switch_moff_false.setTextColor(Color.WHITE);
            //iv_switch_cursor.setBackgroundResource(R.drawable.sports_bg);
        }
    }


    private void moveSelection(int position) {
        TranslateAnimation anim = new TranslateAnimation((mSelectionLeft)
                * (mSelectionRight - 1), ((mSelectionLeft) * (position - 1)), 0, 0);
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

    public void onCheckPosition(int position) {
        moveSelection(position);
        changeTextColor();
        isChecked = !isChecked;
    }

    @Override
    public void onClick(View arg0) {
        // TODO Auto-generated method stub
        switch (arg0.getId()) {
            case R.id.moff_cityButton:
                if (mSelectionRight != 1) {
                    moveSelection(1);
                    changeTextColor();
                    isChecked = !isChecked;
                    if (onCheckedChangeListener != null)
                        onCheckedChangeListener.onCheckedChanged(isChecked);
                }
                break;
            case R.id.moff_localButton:
                if (mSelectionRight != 2) {
                    moveSelection(2);
                    changeTextColor();
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

