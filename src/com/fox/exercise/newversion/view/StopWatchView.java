package com.fox.exercise.newversion.view;

import java.util.ArrayList;

import com.fox.exercise.R;
import com.fox.exercise.newversion.ScreenDatas;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class StopWatchView extends View {

    int width, height;
    int mininumWith;
    Paint linePaint, shuLinePaint;
    Paint timeTextPaint;
    Paint bluePaint;
    String[] timeDatas = new String[8];
    int firstPointLeftIndex = 0;
    ArrayList<Integer> arrayList = new ArrayList<Integer>();

    public StopWatchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        init();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        super.onDraw(canvas);
        singleHeight = (float) ((height * 1.0) / 7);
        drawLine(canvas);
        drawTimeText(canvas);
        drawTimeSplitLine(canvas);
        drawVoidLine(canvas);
        postInvalidate();
    }

    float singleHeight = 0;

    private void drawLine(Canvas canvas) {
        for (int i = 0; i < 7; i++) {
            if (i == 3) {
                linePaint.setColor(Color.WHITE);
            } else {
                linePaint
                        .setColor(getResources().getColor(R.color.gray_litter));
            }
            canvas.drawLine(0, singleHeight * i, width, singleHeight * i,
                    linePaint);
        }
        canvas.drawLine(width, 0, width, singleHeight * 6, bluePaint);
        canvas.drawCircle(width - 10, singleHeight * 3, 10, bluePaint);
        // canvas.drawCircle(width / 2, height-5, 10, bluePaint);
    }

    private void drawTimeText(Canvas canvas) {
        for (int i = 0; i < timeDatas.length; i++) {
            canvas.drawText(timeDatas[i], ((width / 120) * firstPointLeftIndex)
                    + (width / 6) * i + 10, height, timeTextPaint);
        }
    }

    private void drawTimeSplitLine(Canvas canvas) {
        for (int i = 0; i < 160; i++) {
            if (i % 5 == 0) {
                if (i % 20 == 0) {
                    canvas.drawLine(((width / 120) * firstPointLeftIndex)
                                    + (width / 120) * i, 6 * singleHeight,
                            ((width / 120) * firstPointLeftIndex)
                                    + (width / 120) * i, 6 * singleHeight + 35,
                            linePaint);
                } else {
                    canvas.drawLine(((width / 120) * firstPointLeftIndex)
                                    + (width / 120) * i, 6 * singleHeight,
                            ((width / 120) * firstPointLeftIndex)
                                    + (width / 120) * i, 6 * singleHeight + 15,
                            linePaint);
                }
            }
        }
    }

    private void drawVoidLine(Canvas canvas) {
        if (arrayList.size() <= 0) {
            return;
        }
//		float bottomHeight = height - 100;
        for (int i = 0; i < arrayList.size(); i++) {
//			float lineHeight = ((float) arrayList.get(i) / 100) * bottomHeight;
            float AddHeight = arrayList.get(i);
            if (AddHeight > 0) {
                shuLinePaint.setStrokeWidth(3);
            } else {
                shuLinePaint.setStrokeWidth(1);
            }
//			float marginTop = (bottomHeight - lineHeight) / 2 + 100;
            canvas.drawLine((120 - arrayList.size() + i)
                    * (float) (width / 120) - 15, 2 * singleHeight + 20
                    - AddHeight, (120 - arrayList.size() + i)
                    * (float) (width / 120) - 15, 4 * singleHeight - 20
                    + AddHeight, shuLinePaint);
        }
        // for (int i = 120; i > 60; i--) {
        // canvas.drawLine((float) (width / 120) * i,
        // (bottomHeight - 20) / 2 + 100, (float) (width / 120) * i,
        // (bottomHeight - 20) / 2 + 100 + 20, linePaint);
        // }
    }

    private void init() {
        for (int i = 0; i < timeDatas.length; i++) {
            timeDatas[i] = "00:0" + i;
        }
        linePaint = new Paint();
        linePaint.setAntiAlias(true);
        linePaint.setColor(getResources().getColor(R.color.gray_litter));
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeWidth(1);

        shuLinePaint = new Paint();
        shuLinePaint.setAntiAlias(true);
        shuLinePaint.setColor(getResources().getColor(R.color.gray_litter));
        shuLinePaint.setStyle(Paint.Style.STROKE);
        shuLinePaint.setStrokeWidth(1);

        timeTextPaint = new Paint();
        timeTextPaint.setAntiAlias(true);
        timeTextPaint.setColor(Color.WHITE);
        timeTextPaint.setStyle(Paint.Style.STROKE);
        timeTextPaint.setTextSize(20);

        bluePaint = new Paint();
        bluePaint.setAntiAlias(true);
        bluePaint.setColor(Color.YELLOW);
        bluePaint.setStrokeWidth(1);

    }

    public void update(String[] timeDatas, int firstPointLeftIndex,
                       ArrayList<Integer> arrayList) {
        this.timeDatas = timeDatas;
        this.firstPointLeftIndex = firstPointLeftIndex;
        this.arrayList = arrayList;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // TODO Auto-generated method stub
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        width = ScreenDatas.screenWidth - (ScreenDatas.screenWidth % 120);
        height = measureHeight(heightMeasureSpec);
        setMeasuredDimension(width, height);
    }

    public int measureHeight(int measureSpec) {
//		int specMode = MeasureSpec.getMode(measureSpec);
//		int specSize = MeasureSpec.getSize(measureSpec);

        int result = 200;
        /*
		 * if (specMode == MeasureSpec.AT_MOST) { result = specSize; } else if
		 * (specMode == MeasureSpec.EXACTLY) { result = specSize; }
		 */
        result = ScreenDatas.screenHeight / 3;
        return result;
    }

}
