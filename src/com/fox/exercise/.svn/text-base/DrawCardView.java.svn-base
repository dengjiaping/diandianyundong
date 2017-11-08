package com.fox.exercise;

import java.util.ArrayList;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.FloatMath;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;


public class DrawCardView extends View {
    private static final String TAG = "DrawCardView";

    private Bitmap canvasBitmap;//Bitmap.createBitmap(480, 640, Config.RGB_565);
    private BmpItem tempBitmap = null;
    private Canvas canvas;// = new Canvas(canvasBitmap);
    private float X = 0f;
    private float Y = 0f;
    static final int NONE = 0;// init position
    static final int DRAG = 1;// DRAG
    static final int ZOOM = 2;// ZOOM
    int mode = NONE;
    private BmpItem pic;
    private float CX = 0f;
    private float CY = 0f;
    private boolean Begin = true;
    float rotalC[] = new float[2];
    float rotalP[] = new float[2];
    float rotalP_2[] = new float[2];
    int twoPoint = 0;
    public int screenW;
    public int screenH;

    private float preLength = 480.0f;
    private float length = 480.0f;
    private float preCos = 0f;
    private float cos = 0f;
    private static final int TEXT_MAX_INLINE = 12;
    private static final int TEXT_SIZE = 25;
    private static final float TEXT_X = 60;
    private static final float TEXT_Y = 545;
    private static final float TEXT_Y_SPACE = 40;
    private static final int CANVAS_COLOR = Color.WHITE;
    private boolean bool = true;
    private boolean mNoCard = false;
    private boolean mNoFrame = false;
    public boolean canDrawText = false;
    public boolean canDrag = false;
    public boolean initial = true;
    Canvas preCanvas;
    BmpItem bmp;
    private TextUtil textUtil = null;
    Bitmap mBitmapSrc;
    Bitmap mBitmapCard;
    String mText = null;
    private float mDensity = 1.0f;

    private float mPaddingX = 0.0f;
    private float mPaddingY = 0.0f;

    public String orientation = null;

    // Initial the view.
    public DrawCardView(Context context, AttributeSet attribute) {
        this(context, attribute, 0);
    }

    // Initial the view.
    public DrawCardView(Context context, AttributeSet attribute, int style) {
        super(context, attribute, style);
        Log.d("Draw class", "class initilized");

    }

    private void drawCardAndText() {
        Log.d(TAG, "drawCardAndText invoked");
        Log.d(TAG, "mBitmapCard in drawCardAndText:" + mBitmapCard);
        if (mBitmapCard != null) {
//			int frame_width=mBitmapCard.getWidth();
//			int frame_height=mBitmapCard.getHeight();
//			if(frame_width > screenW){
//				frame_height = frame_height*screenW/frame_width;
//				frame_width = screenW;
//				mBitmapCard=Bitmap.createScaledBitmap(mBitmapCard, frame_width, frame_height, true);
//			}
//			Matrix matrix = new Matrix();
//			matrix.postScale(1f, 1f);
            Rect rect = new Rect();
            Log.d(TAG, "this.getLeft():" + this.getLeft());
            Log.d(TAG, "this.getTop():" + this.getTop());
            Log.d(TAG, "this.getRight():" + this.getRight());
            Log.d(TAG, "this.getBottom():" + this.getBottom());
            rect.set(0, 0, canvas.getWidth(), canvas.getHeight());
            canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG | Paint.DITHER_FLAG));
            canvas.drawBitmap(mBitmapCard, null, rect, null);
//			canvas.drawBitmap(mBitmapCard, matrix, null);

        }
//		if (canDrawText == true) {
        if (mText != null && mText.length() > 0) {
            Log.d(TAG, "draw text" + mText);
            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setColorFilter(null);
            paint.setTextSize(TEXT_SIZE);
            paint.setColor(Color.BLACK);
            textUtil = new TextUtil(mText, canvas.getWidth() / 12, screenH * 6 / 7, canvas.getWidth() - canvas.getWidth() * 2 / 12, screenH / 7, 255, TEXT_SIZE);
            textUtil.InitText();
            textUtil.DrawText(canvas);
            /*int len = mText.length();
			if(len >TEXT_MAX_INLINE){
				mText=mText.subSequence(0, TEXT_MAX_INLINE).toString()+"\n"+mText.subSequence(TEXT_MAX_INLINE, len).toString();
			}
			mText.replaceAll("\r\n", "\n");
			String[] breaked=mText.split("\n");
//			ArrayList<String> breakedstring=staticLayout(edit_text_length,mText);
//			Log.d("breakedstring","------------->"+breakedstring);
			for(int i=0;i<breaked.length;i++){
				
				canvas.drawText(breaked[i],screenW/8,screenH*5/6+i*TEXT_Y_SPACE,paint);
			}*/
//			if(len >TEXT_MAX_INLINE){
//				
//				canvas.drawText(mText.subSequence(0, TEXT_MAX_INLINE).toString(), TEXT_X, TEXT_Y, paint);
//				canvas.drawText(mText.subSequence(TEXT_MAX_INLINE, len).toString(), TEXT_X, TEXT_Y+TEXT_Y_SPACE, paint);
//			}
//			else{
//				canvas.drawText(mText, TEXT_X, TEXT_Y, paint);
//			}
        }
//		}
    }

    public ArrayList<String> staticLayout(int width, String text) {
        Log.d("edittextlength", "------------->" + width);
        ArrayList<String> al = new ArrayList<String>();
        CharSequence toMeasure = text;
        Log.d("textlength", "------------->" + text.length());
        int end = toMeasure.length();
        int next = 0;
        Paint paint = new Paint();
        float[] measuredWidth = {0};
        while (next < end) {
            int bPoint = paint.breakText(toMeasure, next, end, true, width, measuredWidth);
            int spacePosition = 0;
            int enterPosition = 0;
            enterPosition = text.substring(next, next + bPoint).indexOf('\n');
            spacePosition = text.substring(next, next + bPoint).lastIndexOf(' ');
            Log.i("mark", "EnterPosition" + "------------->" + enterPosition);
            Log.i("mark", "next" + next + "------------->" + spacePosition);
            if (enterPosition <= 0) {//no enter
                if (spacePosition <= 0) {//no space
                    al.add(text.substring(next, next + bPoint));
                    next += bPoint;
                } else {//new line at first space
                    al.add(text.substring(next, next + spacePosition + 1));
                    next += spacePosition + 1;
                }
            } else {//new line at first return
                al.add(text.substring(next, next + enterPosition));//can not print space
                next += enterPosition + 1;
            }
        }
        return al;
    }

    public void setBmpSrc(Bitmap mBitmapDst) {
        if (mBitmapDst == null)
            return;
//		mBitmapSrc = mBitmapDst;
//        Log.d(TAG, "this.tempBitmap:" + this.tempBitmap);
//		Util.recycle(mBitmapSrc);
        if (this.tempBitmap != null) {
            mBitmapSrc = Bitmap.createScaledBitmap(mBitmapDst, (int) this.tempBitmap.width, (int) this.tempBitmap.height, true);
        } else {
            mBitmapSrc = mBitmapDst;
        }

        int width = mBitmapDst.getWidth();
        int height = mBitmapDst.getHeight();
//        Log.d(TAG, "mBitmapDst.getWidth()" + mBitmapDst.getWidth());
//        Log.d(TAG, "mBitmapDst.getHeight()" + mBitmapDst.getHeight());
        Util.recycle(canvasBitmap);

        if (width > screenW) {
            canvasBitmap = Bitmap.createBitmap(screenW, height * screenW / width, Config.ARGB_8888);
        } else {
            canvasBitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
        }
        mPaddingX = (screenW - canvasBitmap.getWidth()) / 2;
        mPaddingY = (screenH - canvasBitmap.getHeight()) / 2;
        canvas = new Canvas(canvasBitmap);
        preCanvas = canvas;
        this.canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG | Paint.DITHER_FLAG));
//		bmp = new BmpItem(mBitmapDst,(screenW-width)/2,(screenH-height)/2);
        bmp = new BmpItem(mBitmapDst, width / 2, height / 2);
        bmp.width = bmp.getPic().getWidth();
        bmp.height = bmp.getPic().getHeight();

        this.pic = bmp;
        this.canvas.drawColor(CANVAS_COLOR);

        tempBitmap = pic;
//		if(initial){
//			tempBitmap.matrix.preTranslate((screenW-width)/2, (screenH-height)/2);
//			initial=false;
//		}
//        Log.d(TAG, "tempBitmap.pic" + tempBitmap.pic);

//	    this.setPadding((screenW-width)/2, (screenH-height)/2, (screenW-width)/2+width, (screenH-height)/2+height);
        this.canvas.drawBitmap(tempBitmap.pic, tempBitmap.matrix, null);
    }

    public void setBmpSrc(Bitmap mBitmapDst, int card_width, int card_height, String card_orientation) {
        if (mBitmapDst == null)
            return;
        mBitmapSrc = mBitmapDst;
        int width = card_width;
        int height = card_height;
        Log.d(TAG, "card_width:" + card_width);
        Log.d(TAG, "card_height:" + card_height);
//		canvasBitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);	
//		 float mCurScale = Math.max(
//					(float) screenW / width,
//					(float) screenH / height);
//		if(mCurScale<1.0f){
//			canvasBitmap = Bitmap.createBitmap((int)(width*mCurScale), (int)(height*mCurScale), Config.ARGB_8888);	
//		}
        Util.recycle(canvasBitmap);

        if (Util.VERTICAL.equals(card_orientation) || Util.CARD.equals(card_orientation)) {
            canvasBitmap = Bitmap.createBitmap(screenH * 3 / 4, screenH, Config.RGB_565);
        }
        if (Util.HORIZONTAL.equals(card_orientation)) {
            canvasBitmap = Bitmap.createBitmap(screenW, screenW * 3 / 4, Config.RGB_565);
        }
        if (Util.NONE.equals(card_orientation)) {
            if (screenW <= screenH) {
                canvasBitmap = Bitmap.createBitmap(screenW, screenW, Config.RGB_565);
            } else {
                canvasBitmap = Bitmap.createBitmap(screenH, screenH, Config.RGB_565);
            }

        }
        canvas = new Canvas(canvasBitmap);
        mPaddingX = (screenW - canvasBitmap.getWidth()) / 2;
        mPaddingY = (screenH - canvasBitmap.getHeight()) / 2;
        this.canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG | Paint.DITHER_FLAG));
        bmp = new BmpItem(mBitmapDst, width / 2, height / 2);
        bmp.width = bmp.getPic().getWidth();
        bmp.height = bmp.getPic().getHeight();

        this.pic = bmp;
        this.canvas.drawColor(CANVAS_COLOR);

        tempBitmap = pic;
        tempBitmap.matrix.preTranslate((canvas.getWidth() - preCanvas.getWidth()) / 2, (canvas.getHeight() - preCanvas.getHeight()) / 2);
        tempBitmap.preX += (canvas.getWidth() - preCanvas.getWidth()) / 2;
        tempBitmap.preY += (canvas.getHeight() - preCanvas.getHeight()) / 2;
        this.canvas.drawBitmap(tempBitmap.pic, tempBitmap.matrix, null);
        preCanvas = canvas;
    }

    public void setBmpSrc(Bitmap src, boolean noFrame, String bmp_orientation) {
        if (src == null)
            return;
        mNoFrame = noFrame;
        mBitmapSrc = src;
//	this.tempBitmap.pic=src;
//	Util.recycle(this.tempBitmap.pic);
        if (this.tempBitmap != null) {
            this.tempBitmap.pic = Bitmap.createScaledBitmap(src, (int) this.tempBitmap.width, (int) this.tempBitmap.height, true);
        }

        orientation = bmp_orientation;
//        Log.d(TAG, "mNoFrame" + mNoFrame);
//        Log.d(TAG, "mBitmapSrc" + src);
//        Log.d(TAG, "orientation" + orientation);
        int width = src.getWidth();
        int height = src.getHeight();
//        Log.d(TAG, "mBitmapSrc.getWidth()" + src.getWidth());
//        Log.d(TAG, "mBitmapSrc.getHeight()" + src.getHeight());

        Util.recycle(canvasBitmap);
        if (mNoFrame) {
            Log.d(TAG, "mNoFrame invoked:" + mNoFrame);
            canvasBitmap = Bitmap.createBitmap(width, height, Config.RGB_565);
            mPaddingX = (screenW - canvasBitmap.getWidth()) / 2;
            mPaddingY = (screenH - canvasBitmap.getHeight()) / 2;
            canvas = new Canvas(canvasBitmap);
            this.canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG | Paint.DITHER_FLAG));
            this.canvas.drawColor(CANVAS_COLOR);

        }
        if (!mNoFrame) {
            Log.d(TAG, "with Frame:");
            if (Util.VERTICAL.equals(orientation) || Util.CARD.equals(orientation)) {
                canvasBitmap = Bitmap.createBitmap(screenH * 3 / 4, screenH, Config.RGB_565);
            }
            if (Util.HORIZONTAL.equals(orientation)) {
                canvasBitmap = Bitmap.createBitmap(screenW, screenW * 3 / 4, Config.RGB_565);
            }
            if (Util.NONE.equals(orientation)) {
                if (screenW <= screenH) {
                    canvasBitmap = Bitmap.createBitmap(screenW, screenW, Config.RGB_565);
                } else {
                    canvasBitmap = Bitmap.createBitmap(screenH, screenH, Config.RGB_565);
                }

            }

            canvas = new Canvas(canvasBitmap);
            this.canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG | Paint.DITHER_FLAG));
//            Log.d(TAG, "getDensity" + canvasBitmap.getDensity());
//            Log.d(TAG, "getHeight" + canvasBitmap.getHeight());
//            Log.d(TAG, "getScaledWidth" + canvasBitmap.getScaledWidth(canvas));
//            Log.d(TAG, "getScaledWidth" + canvasBitmap.getScaledHeight(canvas));
//            Log.d(TAG, "getWidth" + canvasBitmap.getWidth());

            this.canvas.drawColor(CANVAS_COLOR);

        }
        mPaddingX = (screenW - canvasBitmap.getWidth()) / 2;
        mPaddingY = (screenH - canvasBitmap.getHeight()) / 2;
//	if(initial){
//		initial=false;
//	tempBitmap.matrix.preTranslate((canvas.getWidth()-width)/2, (canvas.getHeight()-height)/2);
//	tempBitmap.preX=canvas.getWidth()/2;
//	tempBitmap.preY=canvas.getHeight()/2;
//	}
        tempBitmap.matrix.preTranslate((canvas.getWidth() - preCanvas.getWidth()) / 2, (canvas.getHeight() - preCanvas.getHeight()) / 2);
        tempBitmap.preX += (canvas.getWidth() - preCanvas.getWidth()) / 2;
        tempBitmap.preY += (canvas.getHeight() - preCanvas.getHeight()) / 2;
        this.canvas.drawBitmap(tempBitmap.pic, tempBitmap.matrix, null);
        preCanvas = canvas;
    }


    public void updateBmpSrc(Bitmap srcBmp) {
        if (srcBmp == null)
            return;
        mBitmapSrc = srcBmp;
        if (this.tempBitmap != null) {
            this.tempBitmap.pic = Bitmap.createScaledBitmap(srcBmp, (int) this.tempBitmap.width, (int) this.tempBitmap.height, true);
        }

        this.canvas.drawColor(Color.WHITE);
        this.canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG | Paint.DITHER_FLAG));
        this.canvas.drawBitmap(tempBitmap.pic, tempBitmap.matrix, null);
        drawCardAndText();
        invalidate();

    }

    public void drawFrame(Bitmap mBitmapFrame) {
        mBitmapCard = mBitmapFrame;


        Log.d(TAG, "mBitmapCard in drawFrame:" + mBitmapCard);
        Log.d(TAG, "tempBitmap in drawFrame:" + tempBitmap);
        this.canvas.drawColor(CANVAS_COLOR);
        this.canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG | Paint.DITHER_FLAG));
        this.canvas.drawBitmap(tempBitmap.pic, tempBitmap.matrix, null);
        if (mBitmapCard != null) {
            int frame_width = mBitmapCard.getWidth();
            int frame_height = mBitmapCard.getHeight();
            if (frame_width > 480) {
                frame_height = frame_height * 480 / frame_width;
                frame_width = 480;
                mBitmapCard = Bitmap.createScaledBitmap(mBitmapCard, frame_width, frame_height, true);
            }
            Matrix matrix = new Matrix();
            matrix.postScale(1f, 1f);
            canvas.drawBitmap(mBitmapCard, matrix, null);
        }

    }

    public void setBmpCardAndText(Bitmap card, String text) {

        mBitmapCard = card;
        mText = text;

        this.canvas.drawColor(CANVAS_COLOR);
        this.canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG | Paint.DITHER_FLAG));
        this.canvas.drawBitmap(tempBitmap.pic, tempBitmap.matrix, null);
        drawCardAndText();
    }


    @Override
    public void onDraw(Canvas canvas) {
        Log.d(TAG, "onDraw invoked");
        canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG | Paint.DITHER_FLAG));
        super.onDraw(canvas);

        if (canvasBitmap != null)
            canvas.drawBitmap(canvasBitmap, (screenW - canvasBitmap.getWidth()) / 2, (screenH - canvasBitmap.getHeight()) / 2, null);

    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (canDrag == false)
            return false;
        initial = false;
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            // main pointer down
            case MotionEvent.ACTION_DOWN:
                Log.d(TAG, "ACTION_DOWN");
                this.X = event.getX() - mPaddingX;
                this.Y = event.getY() - mPaddingY;
//			    		if(!mNoCard && this.Y > 350)
//			    			return super.onTouchEvent(event);
                CX = pic.getXY(1) - (event.getX() - mPaddingX);
                CY = pic.getXY(2) - (event.getY() - mPaddingY);

                Begin = true;
                mode = DRAG;
                break;
            // non-primary pointer down
            case MotionEvent.ACTION_POINTER_DOWN:
//					dist = spacing(event);
                // if the distance between two pointer great than 10, set model to multi pointer down
                if (spacing(event) > 10f) {

                    mode = ZOOM;
                }
                break;

            case MotionEvent.ACTION_MOVE:

                if (mode == DRAG) {
                    Log.d(TAG, "ACTION_MOVE");
                    Log.d(TAG, "ACTION_DRAG");
                    this.X = event.getX() - mPaddingX;
                    this.Y = event.getY() - mPaddingY;
                    Log.d("this.X", "" + this.X);
                    Log.d("this.Y", "" + this.Y);

                    this.canvas.drawColor(CANVAS_COLOR);
                    this.canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG | Paint.DITHER_FLAG));

                    tempBitmap = pic;
                    rotalP = rotalPoint(new float[]{this.X, this.Y}, tempBitmap.preX, tempBitmap.preY, tempBitmap.width / 2, tempBitmap.height / 2, tempBitmap.matrix);

                    Matrix m = new Matrix();
                    m.set(pic.matrix);
                    RectF rect = new RectF(0, 0, pic.getWidth(), pic.getHeight());
                    m.mapRect(rect);
                    Log.d(TAG, "rect" + rect);
                    if ((Math.abs(X - pic.getXY(1)) < pic.getWidth() / 2)
                            && (Math.abs(Y - pic.getXY(2)) < pic.getHeight() / 2)

                            ) {
                        rotalC = this.getT(tempBitmap.width / 2, tempBitmap.height / 2, X + CX, Y + CY, tempBitmap.matrix);

                        canvas.drawBitmap(tempBitmap.getPic(), tempBitmap.matrix, null);

                        tempBitmap.preX = X + CX;
                        tempBitmap.preY = Y + CY;
                    } else {
//							tempBitmap.matrix.preTranslate(0f, 0f);
                        canvas.drawBitmap(tempBitmap.getPic(), tempBitmap.matrix, null);
                    }
                    Log.d(TAG, "drawCardAndText invoked in ACTION_MOVE:");
                    drawCardAndText();
                    Log.d(TAG, "before invalidate invoked:");
                    invalidate();
                } else if (mode == ZOOM) {
                    Log.d(TAG, "ACTION_ZOOM");
                    Log.d("set the background", "aa double point");
                    twoPoint = 2;
                    this.canvas.drawColor(CANVAS_COLOR);
                    this.canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG | Paint.DITHER_FLAG));
//						invalidate();

                    tempBitmap = pic;
                    rotalP = rotalPoint(new float[]{event.getX(0) - mPaddingX, event.getY(0) - mPaddingY}, tempBitmap.preX, tempBitmap.preY, tempBitmap.width / 2, tempBitmap.height / 2, tempBitmap.matrix);
                    rotalP_2 = rotalPoint(new float[]{event.getX(1) - mPaddingX, event.getY(1) - mPaddingY}, tempBitmap.preX, tempBitmap.preY, tempBitmap.width / 2, tempBitmap.height / 2, tempBitmap.matrix);
                    if (
                            (Math.abs(rotalP[0] - pic.getXY(1)) < pic.width / 2)
                                    && (Math.abs(rotalP[1] - pic.getXY(2)) < pic.height / 2)
                                    && (Math.abs(rotalP_2[0] - pic.getXY(1)) < pic.width / 2)
                                    && (Math.abs(rotalP_2[1] - pic.getXY(2)) < pic.height / 2)
                                    || true
                            ) {
                        if (bool) {
                            preLength = spacing(event);
                            preCos = cos(event);
                            Log.d(TAG, "preCos:" + preCos);
                            bool = false;
                        }
                        //Retrieve angle and length
                        length = spacing(event);
                        cos = cos(event);
                        //zoom
                        if (length - preLength != 0) {
                            tempBitmap.savePreHeight(tempBitmap.height);
                            tempBitmap.savePreWidth(tempBitmap.width);
                            tempBitmap.width *= (1.0f + (length - preLength) / length);
                            tempBitmap.height *= (1.0f + (length - preLength) / length);
                            if (tempBitmap.width >= 1.5f * screenW || tempBitmap.height > 1.5f * screenH || tempBitmap.width <= 50f || tempBitmap.height <= 50f) {
//									tempBitmap.width=lp.width;
                                tempBitmap.width = tempBitmap.getPreWidth();
                                tempBitmap.height = tempBitmap.getPreHeight();
                            }
                            //tempBitmap.matrix.postScale(tScale, tScale, tempBitmap.preX, tempBitmap.preY);
//								tempBitmap.pic = Bitmap.createScaledBitmap(BitmapFactory
//										.decodeFile(tempBitmap.url), (int)tempBitmap.width, (int) tempBitmap.height, false);
//								if(tempBitmap.pic!=null && !tempBitmap.pic.isRecycled()){
//									tempBitmap.pic.recycle();
//									tempBitmap.pic = null;
//								}
                            tempBitmap.pic = Bitmap.createScaledBitmap(mBitmapSrc, (int) tempBitmap.width, (int) tempBitmap.height, true);

                            scale(tempBitmap.width / 2, tempBitmap.height / 2, tempBitmap.preX, tempBitmap.preY, tempBitmap.matrix);

                        }


                        //rotate
                        if (Math.abs(cos) > 3 && Math.abs(cos) < 177 && Math.abs(cos - preCos) < 15) {
                            tempBitmap.matrix.postRotate(cos - preCos, tempBitmap.preX, tempBitmap.preY);
                            this.getT2(tempBitmap.width / 2, tempBitmap.height / 2, tempBitmap.preX, tempBitmap.preY, tempBitmap.matrix);
                        }
                        preCos = cos;
                        preLength = length;
//							this.getT(tempBitmap.width / 2, tempBitmap.height / 2 , tempBitmap.preX, tempBitmap.preY, tempBitmap.matrix);


                    }
                    //init position
                    Log.d("draw the scaled bitmap", "aa");
                    canvas.drawBitmap(tempBitmap.getPic(), tempBitmap.matrix, null);

                    drawCardAndText();
                    invalidate();
                }

                break;
            case MotionEvent.ACTION_UP:
                CX = 0f;
                CY = 0f;
                Begin = false;
                mode = NONE;

                break;
            case MotionEvent.ACTION_POINTER_UP:
                bool = true;
                mode = NONE;
                break;
        }


//		invalidate();

        return true;
    }

    public Bitmap getResultBitmap() {
        if (Util.VERTICAL.equals(orientation)) {
            return Bitmap.createScaledBitmap(canvasBitmap, 480, 640, true);
        }
        return canvasBitmap;
    }

    public void setDisplayWindowSize(int displayWidth, int displayHeight) {
        screenW = displayWidth;
        screenH = displayHeight;
        Log.d(TAG, "screenW:" + screenW);
        Log.d(TAG, "screenH:" + screenH);
    }

    public void release() {
        Util.recycle(canvasBitmap);
        if (tempBitmap != null) {
            Util.recycle(tempBitmap.pic);
        }
        if (pic != null) {
            Util.recycle(pic.pic);
        }
        Util.recycle(mBitmapSrc);
        Util.recycle(mBitmapCard);
    }

    public void order(MotionEvent event) {
        rotalP = rotalPoint(new float[]{event.getX(), event.getY()}, pic.preX, pic.preY, pic.width / 2, pic.height / 2, pic.matrix);

    }

    public float[] getT(float preX, float preY, float x, float y, Matrix matrix) {
        float[] re = new float[2];
        float[] matrixArray = new float[9];
        matrix.getValues(matrixArray);
        float a = x - preX * matrixArray[0] - preY * matrixArray[1];
        float b = y - preX * matrixArray[3] - preY * matrixArray[4];
        matrixArray[2] = a;
        matrixArray[5] = b;
        matrix.setValues(matrixArray);
        re[0] = a;
        re[1] = b;
        return re;
    }

    public float[] getT2(float preX, float preY, float x, float y, Matrix matrix) {
        Log.d(TAG, "tempBitmap.matrix before getT:" + tempBitmap.matrix);
        float[] re = new float[2];
        float[] matrixArray = new float[9];
        matrix.getValues(matrixArray);
        float a = x - preX * matrixArray[0] - preY * matrixArray[1];
        float b = y - preX * matrixArray[3] - preY * matrixArray[4];


        matrixArray[2] = a;
        matrixArray[5] = b;
        matrix.setValues(matrixArray);
        re[0] = a;
        re[1] = b;
        Log.d(TAG, "tempBitmap.matrix after getT:" + tempBitmap.matrix);
        return re;
    }

    public void scale(float preX, float preY, float x, float y, Matrix matrix) {
        float[] matrixArray = new float[9];
        matrix.getValues(matrixArray);
        float a = x - preX;
        float b = y - preY;
        matrixArray[2] = a;
        matrixArray[5] = b;
        matrix.setValues(matrixArray);
    }

    public void setToO(Matrix matrix) {
        float[] matrixArray = new float[9];
        matrix.getValues(matrixArray);
        float a = 0f;
        float b = 0f;
        matrixArray[2] = a;
        matrixArray[5] = b;
        matrix.setValues(matrixArray);
    }

    public float[] rotalPoint(float[] p, float X, float Y, float width, float height, Matrix matrix) {
        float re[] = new float[2];
        float matrixArray[] = new float[9];
        matrix.getValues(matrixArray);
        float a = p[0] - X;
        float b = p[1] - Y;
        re[0] = a * matrixArray[0] - b * matrixArray[1] + X;
        re[1] = -a * matrixArray[3] + b * matrixArray[4] + Y;
        return re;
    }

    //	calc length
    private float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return FloatMath.sqrt(x * x + y * y);
    }

    //	calc cos
    private float cos(MotionEvent event) {

        if ((event.getX(0) - event.getX(1)) * (event.getY(0) - event.getY(1)) > 0) {
            return (float) ((float) Math.acos(Math.abs(event.getX(0) - event.getX(1)) / spacing(event)) / Math.PI * 180f);
        }
        if ((event.getX(0) - event.getX(1)) * (event.getY(0) - event.getY(1)) < 0) {
            return (float) ((float) Math.acos(-Math.abs(event.getX(0) - event.getX(1)) / spacing(event)) / Math.PI * 180f);
        }
        if (event.getX(0) - event.getX(1) == 0) {
            return (float) 90f;
        }
        if (event.getY(0) - event.getY(1) == 0) {
            return 0f;
        }
        return 45f;
    }


}


//  @param pic:the Bitmap to draw
//  @param piority: the order to draw picture
//  @param preX,preY: the X and Y 
class BmpItem {

    public BmpItem(Bitmap pic, float preX, float preY) {
//		this(pic, priority);
        this.pic = pic;
        this.preX = preX;
        this.preY = preY;
    }

    //	return X and Y
    public float getXY(int i) {
        if (i == 1) {
            return this.preX;
        } else if (i == 2) {
            return this.preY;
        }
        return (Float) null;
    }


    //	getPicture
    public Bitmap getPic() {
        return this.pic;
    }

    //	getWidth
    public float getWidth() {
        return width;
    }

    //	getHeight
    public float getHeight() {
        return height;
    }

    public void savePreHeight(float height) {
        this.preHeight = height;

    }

    public float getPreHeight() {

        return this.preHeight;
    }

    public void savePreWidth(float width) {
        this.preWidth = width;

    }

    public float getPreWidth() {

        return this.preWidth;
    }


    float preX = 0;
    float preY = 0;
    float preWidth = 0;
    float preHeight = 0;
    float width = 0;
    float height = 0;
    Bitmap pic = null;
    int priority = 0;
    Matrix matrix = new Matrix();
}