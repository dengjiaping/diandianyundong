package com.fox.exercise.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.ImageView;

public class DrawImageView extends ImageView {

    private Paint paint;
    private Context context;
    private int mFilletRadius = 40;
    private int mExcircleWidth = 6;
    private int mState = 0; // 0:正常状态 1:超出目标完成
    private final int NORMAL = 0; // 正常状态
    private final int EXCESS = 1; // 超额
    private int startAngle = 0;

    private int mProgressBarA, mProgressBarR, mProgressBarG, mProgressBarB;

    public DrawImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public DrawImageView(Context context) {
        super(context);
        init(context);
    }

    public DrawImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        this.paint = new Paint();
        this.paint.setAntiAlias(true); // 消除锯齿
        this.paint.setStyle(Style.STROKE); // 绘制空心圆或 空心矩形
    }

    // 设置 内圆 和 外圆的 半径
    public void setRadius(int filletRadius, int excircleWidth) {
        this.mFilletRadius = filletRadius;
        this.mExcircleWidth = excircleWidth;
    }

    public void setProgressBarColor(int a, int r, int g, int b) {
        this.mProgressBarA = a;
        this.mProgressBarR = r;
        this.mProgressBarG = g;
        this.mProgressBarB = b;
    }

    public void setState(int state) {
        this.mState = state;
    }

    public void setAngle(int completed, int score) {
        mState = completed > score ? 1 : 0;
        if (completed != 0 && score != 0) {
            int angel = (completed * 360) / score;
            this.startAngle = (angel == 0 ? 1 : angel);
        } else {
            this.startAngle = 0;
        }
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float scale = context.getResources().getDisplayMetrics().density;
        int center = getWidth() / 2;
        // mFilletRadius = (int) (center - mExcircleWidth * scale - 4);
        int innerCircle = (int) (dip2px(context, mFilletRadius)); // 内圆半径
        int ringWidth = (int) (dip2px(context, mExcircleWidth)); // 圆环宽度

        switch (mState) {
            case NORMAL:
                initNormal(canvas, center, innerCircle, ringWidth);
                break;
            case EXCESS:
                initExcess(canvas, center, innerCircle, ringWidth);
                break;
        }
    }

    public void initNormal(Canvas canvas, int center, int innerCircle,
                           int ringWidth) {
        RectF rect2 = new RectF(center - (innerCircle + 1 + ringWidth / 2),
                center - (innerCircle + 1 + ringWidth / 2), center
                + (innerCircle + 1 + ringWidth / 2), center
                + (innerCircle + 1 + ringWidth / 2));
        this.paint.setStrokeWidth(ringWidth);
        this.paint.setARGB(mProgressBarA, mProgressBarR, mProgressBarG,
                mProgressBarB);
        canvas.drawArc(rect2, -(270 - 32 / 2), startAngle * (360 - 32) / 360,
                false, paint);
    }

    public void initExcess(Canvas canvas, int center, int innerCircle,
                           int ringWidth) {
        RectF rect2 = new RectF(center - (innerCircle + 1 + ringWidth / 2),
                center - (innerCircle + 1 + ringWidth / 2), center
                + (innerCircle + 1 + ringWidth / 2), center
                + (innerCircle + 1 + ringWidth / 2));
        this.paint.setStrokeWidth(ringWidth);
        this.paint.setARGB(255, 255, 0, 0);
        canvas.drawArc(rect2, -(270 - 32 / 2), 360 * (360 - 32) / 360, false,
                paint);
    }

    /* 根据手机的分辨率从 dp 的单位 转成为 px(像素) */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

}
