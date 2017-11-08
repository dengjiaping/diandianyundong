package com.fox.exercise.newversion.view;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

@SuppressLint("ResourceAsColor")
public class MyProgressView extends View {
    /**
     * 画笔对象的引用
     */
    private Paint paint, shuLinePaint, txPaint, prPaint;

    // /**
    // * 矩形的颜色
    // */
    // private int recColor;
    //
    // /**
    // * 矩形进度的颜色
    // */
    // private int recProgressColor;
    /**
     * 矩形的宽度
     */
    private float recWidth;

    /**
     * 矩形的高度
     */
    private float recHeight;

    /**
     * 最大进度
     */
    private float max;

    /**
     * 当前进度
     */
    private float progress;

    private List<Float> list = new ArrayList<Float>();

    public MyProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        paint = new Paint();
        paint.setColor(Color.GRAY);// 设置灰色
        paint.setStyle(Paint.Style.STROKE);// 设置填满

        txPaint = new Paint();
        txPaint.setColor(Color.GRAY);// 设置灰色
        txPaint.setStyle(Paint.Style.STROKE);// 设置填满
        txPaint.setTextSize(26);

        prPaint = new Paint();
        prPaint.setColor(Color.parseColor("#FFB400"));// 设置灰色
        prPaint.setStyle(Paint.Style.FILL);// 设置填满

        shuLinePaint = new Paint();
        shuLinePaint.setAntiAlias(true);
        shuLinePaint.setColor(Color.GRAY);
        shuLinePaint.setStyle(Paint.Style.STROKE);
        shuLinePaint.setStrokeWidth(2);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        super.onDraw(canvas);
        recWidth = getWidth();
        recHeight = getHeight();
        float cuPronum = 0;
        if (max > 0) {
            cuPronum = (float) (recWidth * (progress * 1.0 / max));
        }
        if (cuPronum >= recWidth) {
            cuPronum = recWidth;
        }
        canvas.drawRect(0, recHeight / 2 - 15, cuPronum, recHeight / 2 + 15,
                prPaint);
        canvas.drawRect(0, recHeight / 2 - 15, recWidth, recHeight / 2 + 15,
                paint);// 画长方形
        float sun = 0;
        for (int i = 0; i < list.size(); i++) {
            float ss = (float) (recWidth * (list.get(i) * 1.0 / max));
            sun = sun + ss;
            if (i != list.size() - 1) {
                canvas.drawLine(sun, recHeight / 2 - 15, sun,
                        recHeight / 2 + 15, shuLinePaint);
            }

            canvas.drawText(i + 1 + "", sun - ss / 2, recHeight / 2 + 10,
                    txPaint);

        }

    }

    /**
     * 设置进度，此为线程安全控件，由于考虑多线的问题，需要同步 刷新界面调用postInvalidate()能在非UI线程刷新
     *
     * @param progress
     */
    public synchronized void setProgress(float progress) {
        if (progress < 0) {
//			throw new IllegalArgumentException("progress not less than 0");
            progress = 0;
        }
        if (progress > max) {
            progress = max;
        }
        if (progress <= max) {
            this.progress = progress;
            postInvalidate();
        }

    }

    public synchronized void setMax(float max, ArrayList<Float> list) {
        if (max < 0) {
            throw new IllegalArgumentException("max not less than 0");
        }
        this.max = max;
        this.list = list;
        postInvalidate();
    }

    public float getProgress() {
        return progress;
    }

}
