package com.fox.exercise.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

public class DrawNewImageView extends ImageView {

    private Paint paint;
    private Context context;
    private int mFilletRadius = 50;
    private int mExcircleWidth = 13;
    private int mState = 0; // 0:正常状态 1:超出目标完成
    private final int NORMAL = 0; // 正常状态
    private final int EXCESS = 1; // 超额
    private int startAngle = 0;
    private int displayWidth = 720;

    public DrawNewImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public DrawNewImageView(Context context) {
        super(context);
        init(context);
    }

    public DrawNewImageView(Context context, AttributeSet attrs) {
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

    public void setState(int state) {
        this.mState = state;
    }

    public void setDisplayWidth(int displayWidth) {
        this.displayWidth = displayWidth;
    }

    public void setAngle(double completed, int score, int displayWidth) {
        // mState = completed > score ? 1 : 0;
        if (displayWidth != 0) {
            this.displayWidth = displayWidth;
        }
        if (displayWidth >= 1080) {
            if (completed != 0 && score != 0) {
                mState = 0;
                int angel;
                if (completed < score) {
                    angel = (int) ((completed * 180) * 1.0 / score);
                } else {
                    angel = 180;
                }
                // this.startAngle = (angel == 0 ? 1 : angel) + 8;
                this.startAngle = (angel == 0 ? 1 : angel);
                if (this.startAngle == 180) {
                    this.startAngle += 12;
                }
                Log.i("startAngle-->", "startAngle:" + this.startAngle);
            } else {
                this.startAngle = 0;
                mState = 1;
            }
        } else {
            if (completed != 0 && score != 0) {
                mState = 0;
                int angel;
                if (completed < score) {
                    angel = (int) ((completed * 180) * 1.0 / score);
                } else {
                    angel = 180;
                }
                // this.startAngle = (angel == 0 ? 1 : angel) + 8;
                this.startAngle = (angel == 0 ? 1 : angel);
                if (this.startAngle == 180) {
                    this.startAngle += 8;
                }
                Log.i("startAngle-->", "startAngle:" + this.startAngle);
            } else {
                this.startAngle = 0;
                mState = 1;
            }
        }

        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float scale = context.getResources().getDisplayMetrics().density;
        int center = getWidth() / 2;
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
        // RectF rect2 = new RectF((float) (center
        // - (innerCircle + 6 + ringWidth / 2) - 0.9), center
        // - (innerCircle - 3 + ringWidth / 2), center
        // + (innerCircle + 5 + ringWidth / 2) + 1, center
        // + (innerCircle + 20 + ringWidth / 2));
        RectF rect2;
        if (displayWidth >= 1080) {
            rect2 = new RectF((float) (center
                    - (innerCircle + 6 + ringWidth / 2) - 4), (float) (center
                    - (innerCircle - 3 + ringWidth / 2) + 3), center
                    + (innerCircle + 6 + ringWidth / 2) + 4, center
                    + (innerCircle + 20 + ringWidth / 2) - 1);
        } else {
            rect2 = new RectF((float) (center
                    - (innerCircle + 6 + ringWidth / 2) - 0.9), center
                    - (innerCircle - 3 + ringWidth / 2), center
                    + (innerCircle + 5 + ringWidth / 2) + 1, center
                    + (innerCircle + 20 + ringWidth / 2));
        }
        if (displayWidth >= 1080) {
            this.paint.setStrokeWidth(ringWidth + 0.5f);
        } else {
            this.paint.setStrokeWidth(ringWidth + 0.1f);
        }
        //this.paint.setStrokeWidth(ringWidth + 0.1f);
        // this.paint.setARGB(255, 217, 217, 217);
        this.paint.setColor(Color.rgb(217, 217, 217));

		/*
         * 画弧
		 */
        // canvas.drawArc(rect2, 2, -(184 - startAngle), false, paint);
        if (displayWidth >= 1080) {
            canvas.drawArc(rect2, 6, -(192 - startAngle), false, paint);
        } else {
            canvas.drawArc(rect2, 4, -(188 - startAngle), false, paint);
        }
        invalidate();

    }

    public void initExcess(Canvas canvas, int center, int innerCircle,
                           int ringWidth) {
        RectF rect2;
        Log.i("displayWidth", "displayWidth3-->" + displayWidth);
        if (displayWidth >= 1080) {
            rect2 = new RectF((float) (center
                    - (innerCircle + 6 + ringWidth / 2) - 4), (float) (center
                    - (innerCircle - 3 + ringWidth / 2) + 3), center
                    + (innerCircle + 6 + ringWidth / 2) + 4, center
                    + (innerCircle + 20 + ringWidth / 2) - 1);
        } else {
            rect2 = new RectF((float) (center
                    - (innerCircle + 6 + ringWidth / 2) - 0.9), center
                    - (innerCircle - 3 + ringWidth / 2), center
                    + (innerCircle + 5 + ringWidth / 2) + 1, center
                    + (innerCircle + 20 + ringWidth / 2));
        }
        if (displayWidth >= 1080) {
            this.paint.setStrokeWidth(ringWidth + 0.5f);
        } else {
            this.paint.setStrokeWidth(ringWidth + 0.1f);
        }
        //this.paint.setStrokeWidth(ringWidth + 0.1f);
        // this.paint.setARGB(255, 217, 217, 217);
        this.paint.setColor(Color.rgb(217, 217, 217));
        /*
		 * 画弧
		 */
        // canvas.drawArc(rect2, 4, -188, false, paint);
        if (displayWidth >= 1080) {
            canvas.drawArc(rect2, 6, -192, false, paint);
        } else {
            canvas.drawArc(rect2, 4, -188, false, paint);
        }

        invalidate();
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

}
