package com.fox.exercise.newversion.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;

import com.fox.exercise.R;
import com.fox.exercise.util.SportTaskUtil;

import java.util.ArrayList;

/**********************************************************
 * @文件名称：LineGraphicView.java
 * @文件作者：loujungang
 * @文件描述：自定义简单曲线图
 **********************************************************/
public class LineGraphicView extends View
{
	/**
	 * 公共部分
	 */
	private static final int CIRCLE_SIZE = 10;

	private static enum Linestyle
	{
		Line, Curve
	}

	private Context mContext;
	private Paint mPaint,xPaint;
	private Resources res;
	private DisplayMetrics dm;

	/**
	 * data
	 */
	private Linestyle mStyle = Linestyle.Curve;

	private int canvasHeight;
	private int canvasWidth;
	private int bheight = 0;
	private int blwidh,yPaddingWith;
	private boolean isMeasure = true;
	/**
	 * Y轴最大值
	 */
	private int yMaxValue;
	/**
	 * Y轴最小值
	 */
	private int yMinValue;
	/**
	 * X轴最大值
	 */
	private double xMaxValue;
	/**
	 * Y轴间距值
	 */
	private int yAverageValue;
	/**
	 * X轴间距值
	 */
	private double xAverageValue;
	private int marginTop;
	private int marginBottom ;

	/**
	 * 曲线上总点数
	 */
	private Point[] mPoints;
	/**
	 * 纵坐标值
	 */
	private ArrayList<Double> yRawData;
	/**
	 * 横坐标值
	 */
	private ArrayList<Double> xRawDatas;
	private ArrayList<Integer> xList = new ArrayList<Integer>();// 记录每个x的值
	private int spacingHeight;
	private ArrayList<String> type_list;

	public LineGraphicView(Context context)
	{
		this(context, null);
	}

	public LineGraphicView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		this.mContext = context;
		initView();
	}

	private void initView()
	{
		this.res = mContext.getResources();
		this.mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

		dm = new DisplayMetrics();
		WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
		wm.getDefaultDisplay().getMetrics(dm);

		xPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		xPaint.setStyle(Paint.Style.STROKE);//空心
		xPaint.setColor(res.getColor(R.color.color_ff4631));
		xPaint.setStrokeWidth(dip2px(2f));
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh)
	{
		if (isMeasure)
		{
			this.canvasHeight = getHeight();
			this.canvasWidth = getWidth();
			if (bheight == 0)
//				bheight = (int) (canvasHeight - marginBottom);
				bheight = (int) (canvasHeight);
			blwidh = dip2px(30);
			yPaddingWith=dip2px(30);
			marginBottom=dip2px(30);
			marginTop=dip2px(15);
			isMeasure = false;
		}
	}

	@Override
	protected void onDraw(Canvas canvas)
	{
		if(yRawData!=null&&yRawData.size()>0){
			mPaint.setColor(res.getColor(R.color.color_f2f2f2));
			mPaint.setStrokeWidth(dip2px(1f));

			drawAllXLine(canvas);
			// 画直线（纵向）
			drawAllYLine(canvas);
			// 点的操作设置
			mPoints = getPoints();

			mPaint.setColor(res.getColor(R.color.color_ff4631));
			mPaint.setStrokeWidth(dip2px(2f));
			mPaint.setStyle(Style.STROKE);
			if (mStyle == Linestyle.Curve)
			{
				drawScrollLine(canvas);
				drawXuXianLine(canvas);
			}
			else
			{
				drawLine(canvas);
			}

			mPaint.setStyle(Style.FILL);
			//画点的
		//	for (int i = 0; i < mPoints.length; i++)
		//	{
		//		canvas.drawCircle(mPoints[i].x, mPoints[i].y, CIRCLE_SIZE / 3, mPaint);
		//	}


		}
	}

	/**
	 *  画所有横向表格，包括X轴
	 */
	private void drawAllXLine(Canvas canvas)
	{
		Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
		p.setTextSize(dip2px(12));
		float textWidth=p.measureText(yMaxValue+"");
		yPaddingWith=dip2px(18)+(int)textWidth+1;
		p=null;
		for (int i = 0; i < spacingHeight + 1; i++)
		{
			canvas.drawLine(yPaddingWith+15, bheight - ((bheight-marginBottom-marginTop) / spacingHeight) * i - marginBottom, (canvasWidth - blwidh),
						bheight - ((bheight-marginBottom-marginTop) / spacingHeight) * i - marginBottom, mPaint);// Y坐标

			drawText(String.valueOf(yAverageValue * i+yMinValue), dip2px(18), bheight - ((bheight-marginBottom-marginTop) / spacingHeight) * i - marginBottom+10,
					canvas);
		}
	}

	/**
	 * 画所有纵向表格，包括Y轴
	 */
	private void drawAllYLine(Canvas canvas)
	{
		Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
		p.setTextSize(dip2px(12));
		float textWidth=p.measureText(yMaxValue+"");
		yPaddingWith=dip2px(18)+(int)textWidth+1;
		p=null;
		for (int i = 0; i < yRawData.size(); i++)
		{
			xList.add(yPaddingWith+15+(int)((canvasWidth - blwidh-yPaddingWith-15) * (xRawDatas.get(i)/xMaxValue )));
//			canvas.drawLine(blwidh + (canvasWidth - blwidh) / yRawData.size() * i, marginTop, blwidh
//					+ (canvasWidth - blwidh) / yRawData.size() * i, bheight + marginTop, mPaint);
//			drawText(xRawDatas.get(i), blwidh + (canvasWidth - blwidh) / yRawData.size() * i, bheight+30,
//					canvas);// X坐标

		}
		for (int i = 0; i < spacingHeight + 1; i++)
		{
			if(i!=0){
				float textWidth3 = mPaint.measureText(SportTaskUtil.getDoubleNumber(xAverageValue * i)+"km");
				drawText(SportTaskUtil.getDoubleNumber(xAverageValue * i)+"km", yPaddingWith+ ((canvasWidth - blwidh-yPaddingWith-15) / spacingHeight) * i-(int)(2*textWidth3), bheight-marginBottom/3,
						canvas);
			}
		}
	}

	private void drawScrollLine(Canvas canvas)
	{
		Point startp = new Point();
		Point endp = new Point();
		for (int i = 0; i < mPoints.length - 1; i++)
		{
			if("2".equals(type_list.get(i))&&"2".equals(type_list.get(i+1))){
				continue;
			}
			startp = mPoints[i];
			endp = mPoints[i + 1];
			int wt = (startp.x + endp.x) / 2;
			Point p3 = new Point();
			Point p4 = new Point();
			p3.y = startp.y;
			p3.x = wt;
			p4.y = endp.y;
			p4.x = wt;

			Path path = new Path();
			path.moveTo(startp.x, startp.y);
			path.cubicTo(p3.x, p3.y, p4.x, p4.y, endp.x, endp.y);
			canvas.drawPath(path, mPaint);
		}
	}

    //画虚线
	private void drawXuXianLine(Canvas canvas)
	{
		Point startp = new Point();
		Point endp = new Point();
		for (int i = 0; i < mPoints.length - 1; i++)
		{
			if("2".equals(type_list.get(i))&&"2".equals(type_list.get(i+1))){
				startp = mPoints[i];
				endp = mPoints[i + 1];
				int wt = (startp.x + endp.x) / 2;
				Point p3 = new Point();
				Point p4 = new Point();
				p3.y = startp.y;
				p3.x = wt;
				p4.y = endp.y;
				p4.x = wt;

				Path path = new Path();
				path.moveTo(startp.x, startp.y);
				path.cubicTo(p3.x, p3.y, p4.x, p4.y, endp.x, endp.y);
				PathEffect pathEffect=new DashPathEffect(new float[]{10,10,10,10},1);
				xPaint.setPathEffect(pathEffect);
				canvas.drawPath(path, xPaint);
			}

		}
	}

	private void drawLine(Canvas canvas)
	{
		Point startp = new Point();
		Point endp = new Point();
		for (int i = 0; i < mPoints.length - 1; i++)
		{
			startp = mPoints[i];
			endp = mPoints[i + 1];
			canvas.drawLine(startp.x, startp.y, endp.x, endp.y, mPaint);
		}
	}

	private void drawText(String text, int x, int y, Canvas canvas)
	{
		Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
		p.setTextSize(dip2px(12));
		p.setColor(res.getColor(R.color.color_999999));
		p.setTextAlign(Paint.Align.LEFT);
		canvas.drawText(text, x, y, p);
	}

	private Point[] getPoints()
	{
		Point[] points = new Point[yRawData.size()];
		for (int i = 0; i < yRawData.size(); i++)
		{
			int ph = bheight - (int) ((bheight-marginBottom-marginTop )* ((yRawData.get(i)-yMinValue) / (yMaxValue-yMinValue)))-marginBottom;

			points[i] = new Point(xList.get(i), ph);
		}
		return points;
	}

	public void setData(ArrayList<Double> yRawData, ArrayList<Double> xRawData,ArrayList<String> type_list, int maxValue,int yMinValue, int averageValue,double xMaxValue)
	{
		this.yMaxValue = maxValue;
		this.yMinValue=yMinValue;
		this.yAverageValue = averageValue;
		this.mPoints = new Point[yRawData.size()];
		this.xRawDatas = xRawData;
		this.yRawData = yRawData;
		this.spacingHeight = 5;
		this.xMaxValue=xMaxValue;
		this.xAverageValue=xMaxValue/spacingHeight;
		this.type_list=type_list;
		postInvalidate();
	}

	public void setTotalvalue(int maxValue)
	{
		this.yMaxValue = maxValue;
	}

	public void setPjvalue(int averageValue)
	{
		this.yAverageValue = averageValue;
	}

//	public void setMargint(int marginTop)
//	{
//		this.marginTop = marginTop;
//	}

	public void setMarginb(int marginBottom)
	{
		this.marginBottom = marginBottom;
	}

	public void setMstyle(Linestyle mStyle)
	{
		this.mStyle = mStyle;
	}

	public void setBheight(int bheight)
	{
		this.bheight = bheight;
	}

	/**
	 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
	 */
	private int dip2px(float dpValue)
	{
		return (int) (dpValue * dm.density + 0.5f);
	}

}