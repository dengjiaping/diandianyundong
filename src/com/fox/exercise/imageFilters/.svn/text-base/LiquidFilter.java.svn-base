package com.fox.exercise.imageFilters;

import com.fox.exercise.Coordinate;

import android.graphics.Bitmap;
import android.util.Log;


public class LiquidFilter {


    private static final String TAG = "LiquidFilter";

    public Bitmap liquidImage(Bitmap bmpSrc, Coordinate vC, Coordinate vM, float r, double s) {

        int height = bmpSrc.getHeight();
        int width = bmpSrc.getWidth();

        Bitmap bmpDst = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        Coordinate vX = vC;

        Coordinate zz = new Coordinate(vC.x - vM.x, vC.y - vM.y);
        float z2 = (float) (Math.sqrt(Math.pow(zz.x, 2) + Math.pow(zz.y, 2)));
        Coordinate danwei = new Coordinate(zz.x / z2, zz.y / z2);

        int x1 = 1;
        int x2 = 1;

        if (danwei.x < 0) x1 = -1;
        if (danwei.y < 0) x2 = -1;

        int[] pixels = new int[width * height];
        bmpSrc.getPixels(pixels, 0, width, 0, 0, width, height);

        int widthMax = (int) Math.floor(Math.min(vX.x + r, width));
        int widthMin = (int) Math.floor(Math.max(1, vX.x - r));
        int heightMax = (int) Math.floor(Math.min(vX.y + r, height));
        int heightMin = (int) Math.floor(Math.max(1, vX.y - r));
        int widthStart = widthMin;
        int widthEnd = widthMax;
        int heightStart = heightMin;
        int heightEnd = heightMax;
        int ii = 1;
        int jj = 1;

        if (x1 < 0) {
            widthStart = widthMax;
            widthEnd = widthMin;
            ii = -1;
        }
        if (x2 < 0) {
            heightStart = heightMax;
            heightEnd = heightMin;
            jj = -1;
        }

        Log.v(TAG, "widthStart=" + widthStart + " widthEnd=" + widthEnd);
        Log.v(TAG, "heightStart=" + heightStart + " heightEnd=" + heightEnd);
        for (int i = widthStart; (widthStart > widthEnd) ? i > (widthEnd + 1) : i < (widthEnd - 1); ) {
            for (int j = heightStart; (heightStart > heightEnd) ? j > (heightEnd + 1) : j < (heightEnd - 1); ) {
                int d2 = (int) (Math.pow((vX.x - i), 2) + Math.pow((vX.y - j), 2));
                if (d2 <= r * r) {
                    Coordinate ab = new Coordinate(vX.x - i, vX.y - j);
                    float py = Math.abs(ab.x * danwei.x + ab.y * danwei.y);
                    float px = (float) Math.sqrt(Math.abs(d2 - Math.pow(py, 2)));

                    float ccc = (float) ((1 - (1 / r) * Math.sqrt(s * Math.pow(px, 2) + (1 - s) * Math.pow(py, 2))));
                    ccc = (float) (4 * Math.pow(ccc, 3));
                    Coordinate fmn = new Coordinate(ccc * danwei.x + i, ccc * danwei.y + j);

                    float fm = fmn.x;
                    float fn = fmn.y;
                    if (fm < 1 || fm > width - 1 || fn < 1 || fn > height - 1) {

                    } else {
                        float km = (float) (fm - Math.floor(fm));
                        float kn = (float) (fn - Math.floor(fn));

                        int index = j * width + i;
                        if (index < width * height) {
                            int aA = ((pixels[index] >> 24) & 0xff);
                            int rR = ((pixels[index] >> 16) & 0xff);
                            int gG = ((pixels[index] >> 8) & 0xff);
                            int bB = (pixels[index] & 0xff);

                            int index1 = (int) (Math.floor(fn) * width + Math.floor(fm) + 1);
                            int aA1 = ((pixels[index1] >> 24) & 0xff);
                            int rR1 = ((pixels[index1] >> 16) & 0xff);
                            int gG1 = ((pixels[index1] >> 8) & 0xff);
                            int bB1 = (pixels[index1] & 0xff);

                            int index2 = (int) (Math.floor(fn) * width + Math.floor(fm));
                            int aA2 = ((pixels[index2] >> 24) & 0xff);
                            int rR2 = ((pixels[index2] >> 16) & 0xff);
                            int gG2 = ((pixels[index2] >> 8) & 0xff);
                            int bB2 = (pixels[index2] & 0xff);

                            int index3 = (int) ((Math.floor(fn) + 1) * width + Math.floor(fm) + 1);
                            int aA3 = ((pixels[index3] >> 24) & 0xff);
                            int rR3 = ((pixels[index3] >> 16) & 0xff);
                            int gG3 = ((pixels[index3] >> 8) & 0xff);
                            int bB3 = (pixels[index3] & 0xff);

                            int index4 = (int) ((Math.floor(fn) + 1) * width + Math.floor(fm));
                            int aA4 = ((pixels[index4] >> 24) & 0xff);
                            int rR4 = ((pixels[index4] >> 16) & 0xff);
                            int gG4 = ((pixels[index4] >> 8) & 0xff);
                            int bB4 = (pixels[index4] & 0xff);

                            aA = (int) ((1 - kn) * (km * aA1 + (1 - km) * aA2) + kn * (km * aA3 + (1 - km) * aA4));
                            rR = (int) ((1 - kn) * (km * rR1 + (1 - km) * rR2) + kn * (km * rR3 + (1 - km) * rR4));
                            gG = (int) ((1 - kn) * (km * gG1 + (1 - km) * gG2) + kn * (km * gG3 + (1 - km) * gG4));
                            bB = (int) ((1 - kn) * (km * bB1 + (1 - km) * bB2) + kn * (km * bB3 + (1 - km) * bB4));

                            pixels[index] = (255 << 24) | (rR << 16) | (gG << 8) | bB;
                        }
                    }
                }
                j = j + jj;
            }
            i = i + ii;
        }

        bmpDst.setPixels(pixels, 0, width, 0, 0, width, height);
        return bmpDst;

    }
}
