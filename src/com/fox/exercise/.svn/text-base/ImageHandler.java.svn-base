package com.fox.exercise;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import android.net.Uri;
import android.util.Log;

public class ImageHandler {
    public static Uri imageUri = null;
    private static ColorMatrix mLumMatrix;
    private static ColorMatrix mSaturationMatrix;
    private static ColorMatrix mHueMatrix;
    private static float mLumValue = 1F;
    private static float mSaturationValue = 0F;
    private static float mHueValue = 0F;

    public static final int MIN_VALUE = 0;
    public static final int MIDDLE_VALUE = 128;
    public static final int MAX_VALUE = 255;
    public static final int ZOOM_MAX_VALUE = 240;
    public static final int LIQUID_MAX_VALUE = 100;
    public static final int FISHEYE_RANGE_MAX_VALUE = 50;
    public static final int FISHEYE_STRENGTH_MAX_VALUE = 100;

    public static final int FLAG_BETTER_SKIN = 0x0;
    public static final int FLAG_WHITENING = 0x1;
    public static final int FLAG_DETAILS = 0x2;
    public static final int FLAG_TEMPERATURE = 0x3;

    public static final int FLAG_RANGE = 0x0;
    public static final int FLAG_STRENGTH = 0x1;

    public static ArrayList<Uri> mBmpItems = new ArrayList<Uri>();
    public static ArrayList<Uri> mBmpItems_with_border = new ArrayList<Uri>();
    public static ArrayList<Bitmap> mBmpItemsThumb = new ArrayList<Bitmap>();
    public static ArrayList<String> mImagePathList = new ArrayList<String>();
    public static Bitmap mBitmapSrc = null;

    public static final int PICTURE_COUNT = 8;

    private final static float TEMPERATURE_BASE = 4000;
    private final static float TEMPERATURE_SPECIAL = 6300;
    public final static float LIQUID_BASE = 10;
    public final static float FISHEYE_RANGE_BASE = 20;
    public final static float FISHEYE_STRENGTH_BASE = 0.15f;

    public static int mCurProgressSkin = 0;
    public static int mCurProgressWhiten = 0;
    public static int mCurProgressDetails = 0;
    public static int mCurProgressTemperature = MIDDLE_VALUE;

    public static int mPreProgressSkin = mCurProgressSkin;
    public static int mPreProgressWhiten = mCurProgressWhiten;
    public static int mPreProgressDetails = mCurProgressDetails;
    public static int mPreProgressTemperature = mCurProgressTemperature;

    private static float mCurWhiteGain = 0.5f;
    private static float mCurWhiteBias = 0.5f;
    private static float mCurDetailsAmount = 0.0f;
    private static float mCurTemperature = TEMPERATURE_BASE;


    private static float mFinalWhiteGain = 0.5f;
    private static float mFinalWhiteBias = 0.5f;
    private static float mFinalDetailsAmount = 0.0f;
    private static float mFinalTemperature = TEMPERATURE_BASE;

    public static int mCurProgressEyeRange = 20;
    public static int mCurProgressEyeStrength = 0;

    public static int mCurProgressLiquidRange = 40;
    public static int mCurProgressLiquidStrength = 30;

    public static float mCurLiquidRange = 50;
    public static float mCurLiquidStrength = 0.5f;

    public static float mCurFisheyeRange = 50;
    public static float mCurFisheyeStrength = FISHEYE_STRENGTH_BASE;

    public static float[] red_color = new float[]{1.12f, 0f, 0f, 0f, 1f, 0f, 1f, 0f, 0f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 0f, 0f, 1f, 0f};
    // public static ArrayList<Bitmap> mBmpEditedCache = new
    // ArrayList<Bitmap>();

    /**
     * rotate
     *
     * @param bmp    source bitmap
     * @param degree rotate angle��negative value--contrarotate��
     *               positive value--clockwise rotate
     */
    public static Bitmap rotateBitmap(Bitmap bmp, float degree) {
        if (bmp == null)
            return null;
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        return Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(),
                matrix, true);
    }

    /**
     * scale
     *
     * @param bmp   source bitmap
     * @param scale if less than 1f, zoom in��else zoom out
     */
    public static Bitmap resizeBitmap(Bitmap bmp, float scale) {
        if (bmp == null)
            return null;
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);
        return Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(),
                matrix, true);
    }

    /**
     * rotate
     *
     * @param bmp source bitmap
     * @param w   scale width
     * @param h   scale height
     */
    public static Bitmap resizeBitmap(Bitmap bmp, int w, int h) {
        if (bmp == null)
            return null;
//		Bitmap BitmapOrg = bmp;

        int width = bmp.getWidth();
        int height = bmp.getHeight();

        float scaleWidth = ((float) w) / width;
        float scaleHeight = ((float) h) / height;

        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        return Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(),
                matrix, true);

    }

    /**
     * reverse
     *
     * @param bmp  source bitmap
     * @param flag 0--horizontal reversing��1--vertical reversing
     */
    public static Bitmap reverseBitmap(Bitmap bmp, int flag) {
        if (bmp == null)
            return null;
        float[] floats = null;
        switch (flag) {
            case 0:
                floats = new float[]{-1f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 1f};
                break;
            case 1:
                floats = new float[]{1f, 0f, 0f, 0f, -1f, 0f, 0f, 0f, 1f};
                break;
        }

        if (floats != null) {
            Matrix matrix = new Matrix();
            matrix.setValues(floats);
            Bitmap result = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(),
                    bmp.getHeight(), matrix, true);
            if (bmp != null && !bmp.isRecycled()) {
                bmp.recycle();
                bmp = null;
            }
            return result;
        }

        return bmp;
    }

    /***
     * image processing
     *
     * @param bmp         source bitmap
     * @param colorMatrix ColorArray
     */
    public static Bitmap processImageColor(Bitmap bmp, float[] colorArray) {
        if (bmp == null)
            return null;
        int width = bmp.getWidth();
        int height = bmp.getHeight();
        Bitmap bmpDst = Bitmap.createBitmap(width, height,
                Bitmap.Config.ARGB_8888);
        Paint paint = new Paint();
        Canvas canvas = new Canvas(bmpDst);

        ColorMatrix cm = new ColorMatrix();
        // set colorArray
        cm.set(colorArray);
        // ColorFilter, apply the colorArray to the colorFilter
        paint.setColorFilter(new ColorMatrixColorFilter(cm));
        // draw the bitmap with the paint
        canvas.drawBitmap(bmp, 0, 0, paint);

        return bmpDst;
    }

    /**
     * oldRemember
     *
     * @param bmp source bitmap
     */
    public static Bitmap oldRememberImage(Bitmap bmp) {
        if (bmp == null)
            return null;
        int width = bmp.getWidth();
        int height = bmp.getHeight();
        Bitmap bmpDst = Bitmap.createBitmap(width, height,
                Bitmap.Config.ARGB_8888);
        float[] array = null;
        array = new float[]{0.393f, 0.769f, 0.189f, 0, 0, 0.349f, 0.686f,
                0.168f, 0, 0, 0.272f, 0.534f, 0.131f, 0, 0, 0, 0, 0, 1, 0};
        Paint paint = new Paint();
        Canvas canvas = new Canvas(bmpDst);

        ColorMatrix cm = new ColorMatrix();
        // set colorArray
        cm.set(array);
        // ColorFilter, apply the colorArray to the colorFilter
        paint.setColorFilter(new ColorMatrixColorFilter(cm));
        //draw the bitmap with the paint
        canvas.drawBitmap(bmp, 0, 0, paint);

        return bmpDst;
    }

    /**
     * ��Ƭ
     *
     * @param bmp source bitmap
     */
    public static Bitmap negativeImage(Bitmap bmp) {
        if (bmp == null)
            return null;
        int width = bmp.getWidth();
        int height = bmp.getHeight();
        Bitmap bmpDst = Bitmap.createBitmap(width, height,
                Bitmap.Config.ARGB_8888);
        float[] array = null;
        array = new float[]{-1, 0, 0, 0, 255, 0, -1, 0, 0, 255, 0, 0, -1, 0,
                255, 0, 0, 0, 1, 0};
        Paint paint = new Paint();
        Canvas canvas = new Canvas(bmpDst);

        ColorMatrix cm = new ColorMatrix();
        // set colorArray
        cm.set(array);
        // ColorFilter, apply the colorArray to the colorFilter
        paint.setColorFilter(new ColorMatrixColorFilter(cm));
        //draw the bitmap with the paint
        canvas.drawBitmap(bmp, 0, 0, paint);

        return bmpDst;
    }

    /**
     * �ڰ�
     *
     * @param bmp source bitmap
     */
    public static Bitmap blackWhiteImage(Bitmap bmp) {
        if (bmp == null)
            return null;
        int width = bmp.getWidth();
        int height = bmp.getHeight();
        Bitmap bmpDst = Bitmap.createBitmap(width, height,
                Bitmap.Config.ARGB_8888);
        float[] array = null;
        array = new float[]{0.3f, 0.59f, 0.11f, 0, 0, 0.3f, 0.59f, 0.11f, 0,
                0, 0.3f, 0.59f, 0.11f, 0, 0, 0, 0, 0, 1, 0};
        Paint paint = new Paint();
        Canvas canvas = new Canvas(bmpDst);

        ColorMatrix cm = new ColorMatrix();
        // set colorArray
        cm.set(array);
        // ColorFilter, apply the colorArray to the colorFilter
        paint.setColorFilter(new ColorMatrixColorFilter(cm));
        //draw the bitmap with the paint
        canvas.drawBitmap(bmp, 0, 0, paint);

        return bmpDst;
    }

    /**
     * sunshine effect
     *
     * @param bmp source bitmap
     */
    public static Bitmap sunshineImage(Bitmap bmp) {
        if (bmp == null)
            return null;
        final int width = bmp.getWidth();
        final int height = bmp.getHeight();
        Bitmap bitmap = Bitmap.createBitmap(width, height,
                Bitmap.Config.ARGB_8888);

        int pixR = 0;
        int pixG = 0;
        int pixB = 0;

        int pixColor = 0;

        int newR = 0;
        int newG = 0;
        int newB = 0;

        int centerX = width / 2;
        int centerY = height / 2;
        int radius = Math.min(centerX, centerY);

        final float strength = 150F;// sunshine intensity 100~150
        int[] pixels = new int[width * height];
        bmp.getPixels(pixels, 0, width, 0, 0, width, height);
        int pos = 0;
        for (int i = 1, length = height - 1; i < length; i++) {
            for (int k = 1, len = width - 1; k < len; k++) {
                pos = i * width + k;
                pixColor = pixels[pos];

                pixR = Color.red(pixColor);
                pixG = Color.green(pixColor);
                pixB = Color.blue(pixColor);

                newR = pixR;
                newG = pixG;
                newB = pixB;

                int distance = (int) (Math.pow((centerY - i), 2) + Math.pow(
                        (centerX - k), 2));
                if (distance < radius * radius) {
                    int result = (int) (strength * (1.0 - Math.sqrt(distance)
                            / radius));
                    newR = pixR + result;
                    newG = pixG + result;
                    newB = pixB + result;
                }

                newR = Math.min(255, Math.max(0, newR));
                newG = Math.min(255, Math.max(0, newG));
                newB = Math.min(255, Math.max(0, newB));

                pixels[pos] = Color.argb(255, newR, newG, newB);
            }
        }

        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }

    /**
     * sharp
     *
     * @param bmp source bitmap
     */
    public static Bitmap setSharp(Bitmap bmp, int sharp) {
        if (bmp == null)
            return null;
        int[] laplacian = new int[]{-1, -1, -1, -1, 9, -1, -1, -1, -1};

        int width = bmp.getWidth();
        int height = bmp.getHeight();
        Bitmap bmpDst = Bitmap.createBitmap(width, height,
                Bitmap.Config.ARGB_8888);

        int pixR = 0;
        int pixG = 0;
        int pixB = 0;

        int pixColor = 0;

        int newR = 0;
        int newG = 0;
        int newB = 0;

        int idx = 0;
        float alpha = 0.1F * sharp;
        int[] pixels = new int[width * height];
        bmp.getPixels(pixels, 0, width, 0, 0, width, height);
        for (int i = 1, length = height - 1; i < length; i++) {
            for (int k = 1, len = width - 1; k < len; k++) {
                idx = 0;
                for (int m = -1; m <= 1; m++) {
                    for (int n = -1; n <= 1; n++) {
                        pixColor = pixels[(i + m) * width + k + n];
                        pixR = Color.red(pixColor);
                        pixG = Color.green(pixColor);
                        pixB = Color.blue(pixColor);

                        newR = newR + (int) (pixR * laplacian[idx] * alpha);
                        newG = newG + (int) (pixG * laplacian[idx] * alpha);
                        newB = newB + (int) (pixB * laplacian[idx] * alpha);
                        idx++;
                    }
                }

                newR = Math.min(255, Math.max(0, newR));
                newG = Math.min(255, Math.max(0, newG));
                newB = Math.min(255, Math.max(0, newB));

                pixels[i * width + k] = Color.argb(255, newR, newG, newB);

                newR = 0;
                newG = 0;
                newB = 0;
            }
        }

        bmpDst.setPixels(pixels, 0, width, 0, 0, width, height);
        return bmpDst;
    }

    /**
     * blur
     *
     * @param bmp source bitmap
     */
    public static Bitmap blurImage(Bitmap bmp) {
        if (bmp == null)
            return null;
        int[] gauss = new int[]{1, 2, 1, 2, 4, 2, 1, 2, 1};

        int width = bmp.getWidth();
        int height = bmp.getHeight();
        Bitmap bmpDst = Bitmap.createBitmap(width, height,
                Bitmap.Config.ARGB_8888);

        int pixR = 0;
        int pixG = 0;
        int pixB = 0;

        int pixColor = 0;

        int newR = 0;
        int newG = 0;
        int newB = 0;

        int delta = 16;

        int idx = 0;

        int[] pixels = new int[width * height];
        bmp.getPixels(pixels, 0, width, 0, 0, width, height);
        for (int i = 1, length = height - 1; i < length; i++) {
            for (int k = 1, len = width - 1; k < len; k++) {
                idx = 0;
                for (int m = -1; m <= 1; m++) {
                    for (int n = -1; n <= 1; n++) {
                        pixColor = pixels[(i + m) * width + k + n];
                        pixR = Color.red(pixColor);
                        pixG = Color.green(pixColor);
                        pixB = Color.blue(pixColor);

                        newR = newR + (int) (pixR * gauss[idx]);
                        newG = newG + (int) (pixG * gauss[idx]);
                        newB = newB + (int) (pixB * gauss[idx]);
                        idx++;
                    }
                }

                newR /= delta;
                newG /= delta;
                newB /= delta;

                newR = Math.min(255, Math.max(0, newR));
                newG = Math.min(255, Math.max(0, newG));
                newB = Math.min(255, Math.max(0, newB));

                pixels[i * width + k] = Color.argb(255, newR, newG, newB);

                newR = 0;
                newG = 0;
                newB = 0;
            }
        }

        bmpDst.setPixels(pixels, 0, width, 0, 0, width, height);
        return bmpDst;
    }

    /**
     * blur
     *
     * @param bmpSrc source bitmap
     * @blur blur intensity
     */
    public static Bitmap setBlur(Bitmap bmpSrc, int blur) {
        if (bmpSrc == null)
            return null;
        int width = bmpSrc.getWidth();
        int height = bmpSrc.getHeight();
        int pixels[] = new int[width * height];
        int pixelsRawSource[] = new int[width * height * 3];
        int pixelsRawNew[] = new int[width * height * 3];
        bmpSrc.getPixels(pixels, 0, width, 0, 0, width, height);

        for (int k = 1; k <= blur; k++) {
            for (int i = 0; i < pixels.length; i++) {
                pixelsRawSource[i * 3 + 0] = Color.red(pixels[i]);
                pixelsRawSource[i * 3 + 1] = Color.green(pixels[i]);
                pixelsRawSource[i * 3 + 2] = Color.blue(pixels[i]);
            }
            int CurrentPixel = width * 3 + 3;
            for (int i = 0; i < height - 3; i++) {
                for (int j = 0; j < width * 3; j++) {
                    CurrentPixel += 1;
                    int sumColor = 0;
                    sumColor = pixelsRawSource[CurrentPixel - width * 3];
                    sumColor = sumColor + pixelsRawSource[CurrentPixel - 3];
                    sumColor = sumColor + pixelsRawSource[CurrentPixel + 3];
                    sumColor = sumColor
                            + pixelsRawSource[CurrentPixel + width * 3];
                    pixelsRawNew[CurrentPixel] = Math.round(sumColor / 4);
                }
            }
            for (int i = 0; i < pixels.length; i++) {
                pixels[i] = Color.rgb(pixelsRawNew[i * 3 + 0],
                        pixelsRawNew[i * 3 + 1], pixelsRawNew[i * 3 + 2]);
            }
        }
        Bitmap bmpDst = Bitmap.createBitmap(width, height,
                Bitmap.Config.ARGB_8888);
        bmpDst.setPixels(pixels, 0, width, 0, 0, width, height);
        return bmpDst;
    }

    /**
     * set the saturation value
     *
     * @param saturation
     */
    public static void setSaturation(int saturation) {
        mSaturationValue = saturation * 1.0F / MIDDLE_VALUE;
    }

    /**
     * set hue
     *
     * @param hue
     */
    public static void setLum(int lum) {
        mLumValue = lum * 1.0F / MIDDLE_VALUE;
    }

    /**
     * set lum
     *
     * @param lum
     */
    public static void setHue(int hue) {
//		 mHueValue =Math.abs((hue - MIDDLE_VALUE) * 1.0F / MIDDLE_VALUE) * 180;
        mHueValue = 180 - Math.abs(hue - 180);
//		mHueValue = hue;
        Log.d("mHueValue", "mHueValue:" + mHueValue);

    }

    /**
     * saturation
     *
     * @param bmp source bitmap
     */
    public static Bitmap saturationImage(Bitmap bmp) {
        if (bmp == null)
            return null;
        int width = bmp.getWidth();
        int height = bmp.getHeight();
        Bitmap bmpDst = Bitmap.createBitmap(width, height,
                Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bmpDst);
        Paint paint = new Paint();
        paint.setAntiAlias(true);

        if (null == mSaturationMatrix) {
            mSaturationMatrix = new ColorMatrix();
        }

        mSaturationMatrix.reset();
        mSaturationMatrix.setSaturation(mSaturationValue);

        paint.setColorFilter(new ColorMatrixColorFilter(mSaturationMatrix));
        canvas.drawBitmap(bmp, 0, 0, paint);
        return bmpDst;
    }

    /**
     * lum
     *
     * @param bmp source bitmap
     */
    public static Bitmap lumImage(Bitmap bmp) {
        if (bmp == null)
            return null;
        int width = bmp.getWidth();
        int height = bmp.getHeight();
        Bitmap bmpDst = Bitmap.createBitmap(width, height,
                Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bmpDst);
        Paint paint = new Paint();
        paint.setAntiAlias(true);

        if (null == mLumMatrix) {
            mLumMatrix = new ColorMatrix();
        }

        mLumMatrix.reset();
        mLumMatrix.setScale(mLumValue, mLumValue, mLumValue, 1);

        paint.setColorFilter(new ColorMatrixColorFilter(mLumMatrix));
        canvas.drawBitmap(bmp, 0, 0, paint);
        return bmpDst;
    }

    /**
     * hue
     *
     * @param bmp source bitmap
     */
    public static Bitmap hueImage(Bitmap bmp) {
        if (bmp == null)
            return null;
        int width = bmp.getWidth();
        int height = bmp.getHeight();
        Bitmap bmpDst = Bitmap.createBitmap(width, height,
                Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bmpDst);
        Paint paint = new Paint();
        paint.setAntiAlias(true);

        if (null == mHueMatrix) {
            mHueMatrix = new ColorMatrix();
        }
//		adjustHue(mHueMatrix, mHueValue);
        Log.v("iamgeHandler", "FLAG_HUE");
        mHueMatrix.reset();
        mHueMatrix.setRotate(0, mHueValue);
        mHueMatrix.setRotate(1, mHueValue);
        mHueMatrix.setRotate(2, mHueValue);

        paint.setColorFilter(new ColorMatrixColorFilter(mHueMatrix));
        canvas.drawBitmap(bmp, 0, 0, paint);
        return bmpDst;
    }

    public static Bitmap changeHue(Bitmap source) {
        Bitmap result = Bitmap.createBitmap(source.getWidth(),
                source.getHeight(), source.getConfig());
        float[] hsv = new float[3];
        for (int x = 0; x < source.getWidth(); x++) {
            for (int y = 0; y < source.getHeight(); y++) {
                int c = source.getPixel(x, y);
                Color.colorToHSV(c, hsv);
                hsv[0] = (float) ((hsv[0] + 360 * mHueValue) % 360);
                c = (Color.HSVToColor(hsv) & 0x00ffffff) | (c & 0xff000000);
                result.setPixel(x, y, c);
            }
        }
        return result;
    }

    public static void adjustHue(ColorMatrix cm, float value) {
        value = cleanValue(value, 180f) / (20 * 360f) * (float) Math.PI;
        Log.d("value", "value:" + value);
        if (value == 0) {
            return;
        }
        float cosVal = (float) Math.cos(value);
        float sinVal = (float) Math.sin(value);
        float lumR = 0.213f;
        float lumG = 0.715f;
        float lumB = 0.072f;
        float[] mat = new float[]{
                lumR + cosVal * (1 - lumR) + sinVal * (-lumR),
                lumG + cosVal * (-lumG) + sinVal * (-lumG),
                lumB + cosVal * (-lumB) + sinVal * (1 - lumB), 0, 0,
                lumR + cosVal * (-lumR) + sinVal * (0.143f),
                lumG + cosVal * (1 - lumG) + sinVal * (0.140f),
                lumB + cosVal * (-lumB) + sinVal * (-0.283f), 0, 0,
                lumR + cosVal * (-lumR) + sinVal * (-(1 - lumR)),
                lumG + cosVal * (-lumG) + sinVal * (lumG),
                lumB + cosVal * (1 - lumB) + sinVal * (lumB), 0, 0, 0f, 0f, 0f,
                1f, 0f, 0f, 0f, 0f, 0f, 1f};
        cm.postConcat(new ColorMatrix(mat));
    }

    protected static float cleanValue(float p_val, float p_limit) {
        return Math.min(p_limit, Math.max(-p_limit, p_val));
    }

    /**
     * RoundedCorner
     *
     * @param bmp source bitmap
     */
    public static Bitmap roundedCornerImage(Bitmap bmp) {
        if (bmp == null)
            return null;
        int width = bmp.getWidth();
        int height = bmp.getHeight();
        Bitmap bmpDst = Bitmap.createBitmap(width, height,
                Bitmap.Config.ARGB_8888);
        float roundPx = 20;

        Canvas canvas = new Canvas(bmpDst);
        final int color = 0xff424242;
        Paint paint = new Paint();
        Rect rect = new Rect(0, 0, width, height);
        RectF rectF = new RectF(rect);
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bmp, rect, rect, paint);
        return bmpDst;
    }

    /**
     * reflection
     *
     * @param bmp source bitmap
     */
    public static Bitmap reflectionImage(Bitmap bmp) {
        if (bmp == null)
            return null;
        final int reflectionGap = 4;
        int width = bmp.getWidth();
        int height = bmp.getHeight();

        Matrix matrix = new Matrix();
        matrix.preScale(1, -1);

        Bitmap reflectionImage = Bitmap.createBitmap(bmp, 0, height / 2, width,
                height / 2, matrix, false);

        Bitmap bmpDst = Bitmap.createBitmap(width, (height + height / 2),
                Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bmpDst);
        canvas.drawBitmap(bmp, 0, 0, null);
        Paint deafalutPaint = new Paint();
        canvas.drawRect(0, height, width, height + reflectionGap, deafalutPaint);

        canvas.drawBitmap(reflectionImage, 0, height + reflectionGap, null);

        Paint paint = new Paint();
        LinearGradient shader = new LinearGradient(0, height, 0,
                bmpDst.getHeight() + reflectionGap, 0x70ffffff, 0x00ffffff,
                TileMode.CLAMP);
        paint.setShader(shader);
        // Set the Transfer mode to be porter duff and destination in
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        // Draw a rectangle using the paint with our linear gradient
        canvas.drawRect(0, height, width, bmpDst.getHeight() + reflectionGap,
                paint);
        return bmpDst;
    }

    /**
     * tint
     *
     * @param bmp source bitmap
     * @param deg 0~255
     */
    public static Bitmap tintImage(Bitmap bmp, int number) {
        if (bmp == null)
            return null;
        int width = bmp.getWidth();
        int height = bmp.getHeight();
        int[] argb = new int[width * height];

        bmp.getPixels(argb, 0, width, 0, 0, width, height);// get bitmap's ARGB value
        number = number * 255 / 100;
        for (int i = 0; i < argb.length; i++) {
            argb[i] = (number << 24) | (argb[i] & 0x00FFFFFF);// modify the top two position's value
        }
        bmp = Bitmap.createBitmap(argb, width, height, Bitmap.Config.ARGB_8888);
        return bmp;
    }

    /**
     * frame
     *
     * @param bmpSrc   source bitmap
     * @param bmpFrame the frame to add to the bmp
     */
    public static Bitmap addFrame(Bitmap bmpSrc, Bitmap bmpFrame) {
        if (bmpSrc == null || bmpFrame == null)
            return null;
        int width = bmpSrc.getWidth();
        int height = bmpSrc.getHeight();
        Bitmap tmpFrame = bmpFrame;
        /**
         * if(width > height){ tmpFrame = rotateBitmap(bmpFrame, 90); }
         */
        tmpFrame = resizeBitmap(tmpFrame, width, height);
        Paint paint = new Paint();
        paint.setColorFilter(null);
        Bitmap bmpDst = Bitmap.createBitmap(width, height,
                Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bmpDst);
        canvas.drawBitmap(bmpSrc, 0, 0, paint);
        canvas.drawBitmap(tmpFrame, 0, 0, paint);
        tmpFrame.recycle();
        return bmpDst;
    }

    /**
     * card
     *
     * @param bmpSrc  source bitmap
     * @param bmpCard the card to added to the bmp
     *                public static Bitmap addCard(Bitmap bmpSrc, Bitmap bmpCard) {
     *                if (bmpSrc == null || bmpCard == null)
     *                return null;
     *                int width = bmpCard.getWidth();
     *                int height = bmpCard.getHeight();
     *                int h = (bmpSrc.getHeight() * 400) / bmpSrc.getWidth();
     *                Bitmap tmpSrc = resizeBitmap(bmpSrc, 400, h);
     *                Paint paint = new Paint();
     *                paint.setColorFilter(null);
     *                Bitmap bmpDst = Bitmap.createBitmap(width, height,
     *                Bitmap.Config.ARGB_8888);
     *                Canvas canvas = new Canvas(bmpDst);
     *                <p/>
     *                canvas.drawBitmap(tmpSrc, 30, 20, paint);
     *                canvas.drawBitmap(bmpCard, 0, 0, paint);
     *                tmpSrc.recycle();
     *                return bmpDst;
     *                }
     *                <p/>
     *                /**
     *                text
     * @param bmpSrc  source bitmap
     * @param text    the text to draw
     * @param x       X direction coordinate
     * @param y       Y direction coordinate
     */
    public static Bitmap drawtext(Bitmap bmpSrc, String text, float x, float y,
                                  int color) {
        if (bmpSrc == null)
            return null;
        int width = bmpSrc.getWidth();
        int height = bmpSrc.getHeight();
        int len = text.length();
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColorFilter(null);
        paint.setTextSize(30);
        paint.setColor(color);
        Bitmap bmpDst = Bitmap.createBitmap(width, height,
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bmpDst);
        canvas.drawBitmap(bmpSrc, 0, 0, paint);
        if (len > 12) {

            canvas.drawText(text.subSequence(0, 12).toString(), x, y, paint);
            canvas.drawText(text.subSequence(12, len).toString(), x, y + 50,
                    paint);
        } else {
            canvas.drawText(text, x, y, paint);
        }
        return bmpDst;
    }

    /**
     * lable
     *
     * @param bmpSrc   source bitmap
     * @param bmpLabel lable add to the bmp
     * @param x        X direction coordinate
     * @param y        Y direction coordinate
     */
    public static Bitmap addLabel(Bitmap bmpSrc, Bitmap bmpLabel, int x, int y) {
        if (bmpSrc == null || bmpLabel == null)
            return null;
        int width = bmpSrc.getWidth();
        int height = bmpSrc.getHeight();

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColorFilter(null);
        Bitmap bmpDst = Bitmap.createBitmap(width, height,
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bmpDst);
        canvas.drawBitmap(bmpSrc, 0, 0, paint);
        canvas.drawBitmap(bmpLabel, x, y, paint);
        return bmpDst;
    }

    /**
     * halo
     *
     * @param bmpSrc source bitmap
     * @param x      X direction coordinate
     * @param y      Y direction coordinate
     * @param r      radio
     */
    public static Bitmap haloImage(Bitmap bmpSrc, int x, int y, float r) {
        if (bmpSrc == null)
            return null;
        int[] gauss = new int[]{1, 2, 1, 2, 4, 2, 1, 2, 1};

        int width = bmpSrc.getWidth();
        int height = bmpSrc.getHeight();

        Bitmap bmpDst = Bitmap.createBitmap(width, height,
                Bitmap.Config.ARGB_8888);

        int pixR = 0;
        int pixG = 0;
        int pixB = 0;

        int pixColor = 0;

        int newR = 0;
        int newG = 0;
        int newB = 0;

        int delta = 18;

        int idx = 0;
        int[] pixels = new int[width * height];
        bmpSrc.getPixels(pixels, 0, width, 0, 0, width, height);
        for (int i = 1, length = height - 1; i < length; i++) {
            for (int k = 1, len = width - 1; k < len; k++) {
                idx = 0;
                int distance = (int) (Math.pow(k - x, 2) + Math.pow(i - y, 2));
                if (distance > r * r) {
                    for (int m = -1; m <= 1; m++) {
                        for (int n = -1; n <= 1; n++) {
                            pixColor = pixels[(i + m) * width + k + n];
                            pixR = Color.red(pixColor);
                            pixG = Color.green(pixColor);
                            pixB = Color.blue(pixColor);

                            newR = newR + (int) (pixR * gauss[idx]);
                            newG = newG + (int) (pixG * gauss[idx]);
                            newB = newB + (int) (pixB * gauss[idx]);
                            idx++;
                        }
                    }
                    newR /= delta;
                    newG /= delta;
                    newB /= delta;

                    newR = Math.min(255, Math.max(0, newR));
                    newG = Math.min(255, Math.max(0, newG));
                    newB = Math.min(255, Math.max(0, newB));

                    pixels[i * width + k] = Color.argb(255, newR, newG, newB);

                    newR = 0;
                    newG = 0;
                    newB = 0;
                }
            }
        }

        bmpDst.setPixels(pixels, 0, width, 0, 0, width, height);
        return bmpDst;
    }

    /**
     * Relief
     *
     * @param bmpSrc source bitmap
     */
    public static Bitmap changeToRelief(Bitmap mBitmap) {
        if (mBitmap == null)
            return null;
        int width = mBitmap.getWidth();
        int height = mBitmap.getHeight();
        Bitmap bitmap = Bitmap.createBitmap(width, height,
                Bitmap.Config.RGB_565);
        int pixR = 0;
        int pixG = 0;
        int pixB = 0;
        int pixColor = 0;
        int newR = 0;
        int newG = 0;
        int newB = 0;
        int[] pixels = new int[width * height];
        mBitmap.getPixels(pixels, 0, width, 0, 0, width, height);
        int pos = 0;
        for (int i = 0, length = height; i < length; i++) {
            for (int k = 0, len = width; k < len; k++) {
                pos = i * width + k;

                pixColor = pixels[pos];
                pixR = Color.red(pixColor);
                pixG = Color.green(pixColor);
                pixB = Color.blue(pixColor);

                if (k == width - 1 || i == height - 1)
                    pixColor = pixels[pos];
                else
                    pixColor = pixels[pos + 1];
                newR = Color.red(pixColor) - pixR + 127;
                newG = Color.green(pixColor) - pixG + 127;
                newB = Color.blue(pixColor) - pixB + 127;

                newR = Math.min(255, Math.max(0, newR));
                newG = Math.min(255, Math.max(0, newG));
                newB = Math.min(255, Math.max(0, newB));

                pixels[pos] = Color.argb(255, newR, newG, newB);
            }
        }
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;

    }

    /**
     * Oil
     *
     * @param bmpSrc source bitmap
     */
    public static Bitmap changeToOil(Bitmap bitmap) {
        if (bitmap == null)
            return null;
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Bitmap returnBitmap = Bitmap.createBitmap(width, height,
                Bitmap.Config.RGB_565);
        int color = 0;
        Random random = new Random();
        int iModel = 4;
        int i = width - iModel;
        while (i > 1) {
            int j = height - iModel;
            while (j > 1) {
                int iPos = random.nextInt(100000000) % iModel;
                color = bitmap.getPixel(i + iPos, j + iPos);
                returnBitmap.setPixel(i, j, color);
                j--;
            }
            i--;
        }
        return returnBitmap;
    }

    /**
     * Neon
     *
     * @param bmpSrc source bitmap
     */
    public static Bitmap changeToNeon(Bitmap bitmap) {
        if (bitmap == null)
            return null;
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Bitmap returnBitmap = Bitmap.createBitmap(width, height,
                Bitmap.Config.RGB_565);
        int colorArray[] = new int[width * height];
        int r, g, b;
        bitmap.getPixels(colorArray, 0, width, 0, 0, width, height);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int index = y * width + x;
                r = (colorArray[index] >> 16) & 0xff;
                g = (colorArray[index] >> 8) & 0xff;
                b = colorArray[index] & 0xff;
                colorArray[index] = 0xff000000 | (r << 16) | (g << 8) | b;
            }
        }
        boolean[][] mask = null;
        Paint grayMatrix[] = new Paint[256];
        // Init gray matrix
        int outlineCase = 1;
        double rand = Math.random();
        if (rand > 0.33 && rand < 0.66) {
            outlineCase = 2;
        } else if (rand > 0.66) {
            outlineCase = 3;
        }
        for (int i = 255; i >= 0; i--) {
            Paint p = new Paint();
            int red = i, green = i, blue = i;
            if (i > 127) {
                switch (outlineCase) {
                    case 1:
                        red = 255 - i;
                        break;
                    case 2:
                        green = 255 - i;
                        break;
                    case 3:
                        blue = 255 - i;
                        break;
                }
            }
            p.setColor(Color.rgb(red, green, blue));
            grayMatrix[255 - i] = p;
        }
        int[][] luminance = new int[width][height];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (mask != null && !mask[x][y]) {
                    continue;
                }
                luminance[x][y] = (int) luminance(
                        (colorArray[((y * width + x))] & 0x00FF0000) >>> 16,
                        (colorArray[((y * width + x))] & 0x0000FF00) >>> 8,
                        colorArray[((y * width + x))] & 0x000000FF);
            }
        }
        int grayX, grayY;
        int magnitude;
        for (int y = 1; y < height - 1; y++) {
            for (int x = 1; x < width - 1; x++) {
                if (mask != null && !mask[x][y]) {
                    continue;
                }
                grayX = -luminance[x - 1][y - 1] + luminance[x - 1][y - 1 + 2]
                        - 2 * luminance[x - 1 + 1][y - 1] + 2
                        * luminance[x - 1 + 1][y - 1 + 2]
                        - luminance[x - 1 + 2][y - 1]
                        + luminance[x - 1 + 2][y - 1 + 2];
                grayY = luminance[x - 1][y - 1] + 2
                        * luminance[x - 1][y - 1 + 1]
                        + luminance[x - 1][y - 1 + 2]
                        - luminance[x - 1 + 2][y - 1] - 2
                        * luminance[x - 1 + 2][y - 1 + 1]
                        - luminance[x - 1 + 2][y - 1 + 2];
                // Magnitudes sum
                magnitude = 255 - truncate(Math.abs(grayX) + Math.abs(grayY));
                Paint grayscaleColor = grayMatrix[magnitude];
                // Apply the color into a new image
                returnBitmap.setPixel(x, y, grayscaleColor.getColor());
            }
        }
        return returnBitmap;
    }

    private static int luminance(int r, int g, int b) {
        return (int) ((0.299 * r) + (0.58 * g) + (0.11 * b));
    }

    /**
     * Sets the RGB between 0 and 255
     *
     * @param a init value
     * @return
     */
    private static int truncate(int a) {
        if (a < 0)
            return 0;
        else if (a > 255)
            return 255;
        else
            return a;
    }

    /**
     * Pixelate
     *
     * @param bmpSrc source bitmap
     */
    private static int pixelSize = 2;
    private static int colorArray[];
    private static int width;
    private static int height;

    public static final Bitmap changeToPixelate(Bitmap bitmap) {
        if (bitmap == null)
            return null;
        int l_rgb;
        width = bitmap.getWidth();
        height = bitmap.getHeight();
        Bitmap returnBitmap = Bitmap.createBitmap(width, height,
                Bitmap.Config.RGB_565);
        colorArray = new int[width * height];
        int r, g, b;
        bitmap.getPixels(colorArray, 0, width, 0, 0, width, height);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int index = y * width + x;
                r = (colorArray[index] >> 16) & 0xff;
                g = (colorArray[index] >> 8) & 0xff;
                b = colorArray[index] & 0xff;
                colorArray[index] = 0xff000000 | (r << 16) | (g << 8) | b;
            }
        }
        for (int x = 0; x < width; x += pixelSize) {
            for (int y = 0; y < bitmap.getHeight(); y += pixelSize) {
                l_rgb = getPredominantRGB(bitmap, x, y, pixelSize);
                fillRect(returnBitmap, x, y, pixelSize, l_rgb);
            }
        }
        return returnBitmap;
    }

    /**
     * @return the pixelSize
     */
    public int getPixelSize() {
        return pixelSize;
    }

    /**
     * @param pixelSize the pixelSize to set
     */
    public void setPixelSize(int pixelSize) {
        ImageHandler.pixelSize = pixelSize;
    }

    /**
     * Method gets the predominant colour pixels to extrapolate
     * the pixelation from
     *
     * @param imageIn
     * @param a_x
     * @param a_y
     * @param squareSize
     * @return
     */
    private static int getPredominantRGB(Bitmap imageIn, int a_x, int a_y,
                                         int squareSize) {
        if (imageIn == null)
            return -1;
        int red = -1;
        int green = -1;
        int blue = -1;
        for (int x = a_x; x < a_x + squareSize; x++) {
            for (int y = a_y; y < a_y + squareSize; y++) {
                if (x < imageIn.getWidth() && y < imageIn.getHeight()) {
                    if (red == -1) {
                        red = (colorArray[((y * width + x))] & 0x00FF0000) >>> 16;
                    } else {
                        red = ((colorArray[((y * width + x))] & 0x00FF0000) >>> 16) / 2;
                    }
                    if (green == -1) {
                        green = (colorArray[((y * width + x))] & 0x0000FF00) >>> 8;
                    } else {
                        green = ((colorArray[((y * width + x))] & 0x0000FF00) >>> 8) / 2;
                    }
                    if (blue == -1) {
                        blue = (colorArray[((y * width + x))] & 0x000000FF);
                    } else {
                        blue = ((colorArray[((y * width + x))] & 0x000000FF)) / 2;
                    }
                }
            }
        }
        return (255 << 24) + (red << 16) + (green << 8) + blue;
    }

    /**
     * Method to extrapolate out
     *
     * @param imageIn
     * @param a_x
     * @param a_y
     * @param squareSize
     * @param a_rgb
     */
    private static void fillRect(Bitmap imageIn, int a_x, int a_y,
                                 int squareSize, int a_rgb) {
        if (imageIn == null)
            return;
        for (int x = a_x; x < a_x + squareSize; x++) {
            for (int y = a_y; y < a_y + squareSize; y++) {
                if (x < imageIn.getWidth() && y < imageIn.getHeight()) {
                    imageIn.setPixel(x, y, a_rgb);
                }
            }
        }
    }

    /**
     * TV
     *
     * @param bmpSrc source bitmap
     */
    public static final Bitmap changeToTV(Bitmap bitmap) {
        if (bitmap == null)
            return null;
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Bitmap returnBitmap = Bitmap.createBitmap(width, height,
                Bitmap.Config.RGB_565);
        int colorArray[] = new int[width * height];
        int r, g, b;
        bitmap.getPixels(colorArray, 0, width, 0, 0, width, height);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int index = y * width + x;
                r = (colorArray[index] >> 16) & 0xff;
                g = (colorArray[index] >> 8) & 0xff;
                b = colorArray[index] & 0xff;
                colorArray[index] = 0xff000000 | (r << 16) | (g << 8) | b;
            }
        }
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y += 3) {
                r = 0;
                g = 0;
                b = 0;
                for (int w = 0; w < 3; w++) {
                    if (y + w < height) {
                        r += ((colorArray[(((y + w) * width + x))] & 0x00FF0000) >>> 16) / 2;
                        // r += (imageIn.getRComponent(x, y+w))/2;
                        g += ((colorArray[(((y + w) * width + x))] & 0x0000FF00) >>> 8) / 2;
                        // g += (imageIn.getGComponent(x, y+w))/2;
                        b += (colorArray[(((y + w) * width + x))] & 0x000000FF) / 2;
                        // b += (imageIn.getBComponent(x, y+w))/2;
                    }
                }
                r = getValidInterval(r);
                g = getValidInterval(g);
                b = getValidInterval(b);
                for (int w = 0; w < 3; w++) {
                    if (y + w < height) {
                        if (w == 0) {
                            colorArray[(((y + w) * width + x))] = (255 << 24)
                                    + (r << 16) + (0 << 8) + 0;
                            returnBitmap.setPixel(x, y + w,
                                    colorArray[(((y + w) * width + x))]);
                            // returnBitmap.setPixelColour(x,y+w,r,0,0);
                        } else if (w == 1) {
                            colorArray[(((y + w) * width + x))] = (255 << 24)
                                    + (0 << 16) + (g << 8) + 0;
                            returnBitmap.setPixel(x, y + w,
                                    colorArray[(((y + w) * width + x))]);
                            // imageIn.setPixelColour(x,y+w,0,g,0);
                        } else if (w == 2) {
                            colorArray[(((y + w) * width + x))] = (255 << 24)
                                    + (0 << 16) + (0 << 8) + b;
                            returnBitmap.setPixel(x, y + w,
                                    colorArray[(((y + w) * width + x))]);
                            // imageIn.setPixelColour(x,y+w,0,0,b);
                        }
                    }
                }
            }
        }
        return returnBitmap;
    }

    /**
     * * method to calculate an appropriate interval for flicker lines
     *
     * @param a_value
     * @return
     */
    private static int getValidInterval(int a_value) {
        if (a_value < 0)
            return 0;
        if (a_value > 255)
            return 255;
        return a_value;
    }

    /**
     * Brick
     *
     * @param bmpSrc source bitmap
     */
    public static Bitmap changeToBrick(Bitmap mBitmap) {
        if (mBitmap == null)
            return null;
        int mBitmapWidth = 0;
        int mBitmapHeight = 0;
        mBitmapWidth = mBitmap.getWidth();
        mBitmapHeight = mBitmap.getHeight();
        Bitmap bmpReturn = Bitmap.createBitmap(mBitmapWidth, mBitmapHeight,
                Bitmap.Config.ARGB_8888);
        int iPixel = 0;
        for (int i = 0; i < mBitmapWidth; i++) {
            for (int j = 0; j < mBitmapHeight; j++) {
                int curr_color = mBitmap.getPixel(i, j);
                int avg = (Color.red(curr_color) + Color.green(curr_color) + Color
                        .blue(curr_color)) / 3;
                if (avg >= 100) {
                    iPixel = 255;
                } else {
                    iPixel = 0;
                }
                int modif_color = Color.argb(255, iPixel, iPixel, iPixel);
                bmpReturn.setPixel(i, j, modif_color);
            }
        }
        return bmpReturn;
    }

    /**
     * LOMO
     *
     * @param bmpSrc source bitmap
     */
    public static Bitmap changeToLomo(Bitmap bitmap) {
        if (bitmap == null)
            return null;
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Bitmap returnBitmap = Bitmap.createBitmap(width, height,
                Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(returnBitmap);

        Paint paint = new Paint();
        paint.setAntiAlias(true);
        float scaleValue = 95 * 1.0F / 127;
        ColorMatrix scaleMatrix = new ColorMatrix();
        scaleMatrix.reset();
        scaleMatrix.setScale((float) (scaleValue + 0.2),
                (float) (scaleValue + 0.4), (float) (scaleValue + 0.2), 1);
        ColorMatrix satMatrix = new ColorMatrix();
        satMatrix.reset();
        satMatrix.setSaturation(0.85f);
        ColorMatrix hueMatrix = new ColorMatrix();
        hueMatrix.reset();
        hueMatrix.setRotate(0, 5);
        hueMatrix.setRotate(1, 5);
        hueMatrix.setRotate(2, 5);
        ColorMatrix allMatrix = new ColorMatrix();
        allMatrix.reset();
        allMatrix.postConcat(scaleMatrix);
        allMatrix.postConcat(satMatrix);
        paint.setColorFilter(new ColorMatrixColorFilter(allMatrix));
        canvas.drawBitmap(bitmap, 0, 0, paint);

        double radius = (double) (width / 2) * 95 / 100;
        double centerX = width / 2f;
        double centerY = height / 2f;
        int pixels[] = new int[width * height];
        returnBitmap.getPixels(pixels, 0, width, 0, 0, width, height);
        int currentPos;
        double pixelsFalloff = 3.5;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                double dis = Math.sqrt(Math.pow((centerX - x), 2)
                        + Math.pow(centerY - y, 2));
                currentPos = y * width + x;
                if (dis > radius) {
                    int pixR = Color.red(pixels[currentPos]);
                    int pixG = Color.green(pixels[currentPos]);
                    int pixB = Color.blue(pixels[currentPos]);
                    double scaler = scaleFunction(radius, dis, pixelsFalloff);
                    scaler = Math.abs(scaler);

                    int newR = (int) (pixR - scaler);
                    int newG = (int) (pixG - scaler);
                    int newB = (int) (pixB - scaler);

                    newR = Math.min(255, Math.max(0, newR));
                    newG = Math.min(255, Math.max(0, newG));
                    newB = Math.min(255, Math.max(0, newB));

                    pixels[currentPos] = Color.argb(255, newR, newG, newB);
                }
            }
        }
        returnBitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return returnBitmap;
    }

    /**
     * LOMO Camera
     *
     * @param bmpSrc source bitmap
     */
    public static Bitmap cameraLomo(Bitmap bitmap) {
        if (bitmap == null)
            return null;
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Bitmap returnBitmap = Bitmap.createBitmap(width, height,
                Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(returnBitmap);

        Paint paint = new Paint();
        paint.setAntiAlias(true);
        float scaleValue = 95 * 1.0F / 127;
        ColorMatrix scaleMatrix = new ColorMatrix();
        scaleMatrix.reset();
        scaleMatrix.setScale((float) (scaleValue + 0.6),
                (float) (scaleValue + 0.6), (float) (scaleValue + 0.2), 1);
        ColorMatrix satMatrix = new ColorMatrix();
        satMatrix.reset();
        satMatrix.setSaturation(0.85f);
        ColorMatrix hueMatrix = new ColorMatrix();
        hueMatrix.reset();
        hueMatrix.setRotate(0, 5);
        hueMatrix.setRotate(1, 5);
        hueMatrix.setRotate(2, 5);
        ColorMatrix allMatrix = new ColorMatrix();
        allMatrix.reset();
        allMatrix.postConcat(scaleMatrix);
        allMatrix.postConcat(satMatrix);
        paint.setColorFilter(new ColorMatrixColorFilter(allMatrix));
        canvas.drawBitmap(bitmap, 0, 0, paint);

        if (bitmap != null && !bitmap.isRecycled()) {
            bitmap.recycle();
            bitmap = null;
        }
        return returnBitmap;
    }

    private static double scaleFunction(double radius, double dis,
                                        double pixelsFallof) {
        return 1 - Math.pow((double) ((dis - radius) / pixelsFallof), 2);
    }

    public static Bitmap readBitmap(Context context, Integer resId) {
        if (resId == 0 || resId == null) {
            return null;
        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 1;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        options.inPurgeable = true;
        options.inInputShareable = true;
        InputStream is = context.getResources().openRawResource(resId);
        return BitmapFactory.decodeStream(is, null, options);

    }

    public static void bmpRecycle(Bitmap bmp) {
        if (bmp != null && !bmp.isRecycled()) {
            bmp.recycle();
            bmp = null;
            System.gc();
        }
    }

    public static Bitmap GrayScale(Bitmap mBitmap) {
        if (mBitmap == null)
            return null;
        int mBitmapWidth = 0;
        int mBitmapHeight = 0;
        mBitmapWidth = mBitmap.getWidth();
        mBitmapHeight = mBitmap.getHeight();
        Bitmap bmpReturn = Bitmap.createBitmap(mBitmapWidth, mBitmapHeight,
                Bitmap.Config.ARGB_8888);
        for (int i = 0; i < mBitmapWidth; i++) {
            for (int j = 0; j < mBitmapHeight; j++) {
                int curr_color = mBitmap.getPixel(i, j);
                int modif_color = filterRGB(i, j, curr_color);
                bmpReturn.setPixel(i, j, modif_color);
            }
        }
        return bmpReturn;
    }

    private static int filterRGB(int x, int y, int rgb) {
        int a = rgb & 0xff000000;
        int r = (rgb >> 16) & 0xff;
        int g = (rgb >> 8) & 0xff;
        int b = rgb & 0xff;
//		rgb = (r + g + b) / 3;	// simple average
        rgb = (r * 77 + g * 151 + b * 28) >> 8;    // NTSC luma
        return a | (rgb << 16) | (rgb << 8) | rgb;
    }

    public static Bitmap GrayOut(Bitmap mBitmap) {
        if (mBitmap == null)
            return null;
        int mBitmapWidth = 0;
        int mBitmapHeight = 0;
        mBitmapWidth = mBitmap.getWidth();
        mBitmapHeight = mBitmap.getHeight();
        Bitmap bmpReturn = Bitmap.createBitmap(mBitmapWidth, mBitmapHeight,
                Bitmap.Config.ARGB_8888);
        for (int i = 0; i < mBitmapWidth; i++) {
            for (int j = 0; j < mBitmapHeight; j++) {
                int curr_color = mBitmap.getPixel(i, j);
                int modif_color = filterGrayRGB(i, j, curr_color);
                bmpReturn.setPixel(i, j, modif_color);
            }
        }
        return bmpReturn;
    }

    private static int filterGrayRGB(int x, int y, int rgb) {
        int a = rgb & 0xff000000;
        int r = (rgb >> 16) & 0xff;
        int g = (rgb >> 8) & 0xff;
        int b = rgb & 0xff;
        r = (r + 255) / 2;
        g = (g + 255) / 2;
        b = (b + 255) / 2;
        return a | (r << 16) | (g << 8) | b;
    }

    public static void CalculateCustomParameters() {

        mCurWhiteBias = (float) (((float) ImageHandler.mCurProgressWhiten * 0.15f) / 255 + 0.65);
        if (mCurWhiteBias != 0.65f)
            mCurWhiteGain = 0.35f;
        else {
            mCurWhiteGain = 0.5f;
        }
        mCurDetailsAmount = (float) ImageHandler.mCurProgressDetails / 500;
        mCurTemperature = TEMPERATURE_BASE + ((float) ImageHandler.mCurProgressTemperature * 5000) / 255;

        mFinalWhiteGain = mCurWhiteGain;
        if (mCurWhiteBias == 0.65f)
            mFinalWhiteBias = 0.5f;
        else
            mFinalWhiteBias = mCurWhiteBias;
        mFinalDetailsAmount = mCurDetailsAmount;
        mFinalTemperature = mCurTemperature;
    }

}
