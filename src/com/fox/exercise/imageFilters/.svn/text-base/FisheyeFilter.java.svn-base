package com.fox.exercise.imageFilters;

import android.graphics.Bitmap;
import android.util.Log;

public class FisheyeFilter {


    private static final String TAG = null;

    public Bitmap fisheyeFilter(Bitmap bmp, int centerX, int centerY, double radius, float factor) {
        if (bmp == null)
            return null;

        int width = bmp.getWidth();
        int height = bmp.getHeight();

        Bitmap bmpDst = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        int[] pixels = new int[width * height];
        int[] outPixels = new int[width * height];
        bmp.getPixels(pixels, 0, width, 0, 0, width, height);
        Log.v(TAG, "factor*100=" + factor * 100);
        for (int y = 0; y < height; ++y) {
            int ycoord = y - centerY;
            for (int x = 0; x < width; ++x) {
                int xcoord = x - centerX;
                double d = Math.sqrt(xcoord * xcoord + ycoord * ycoord);
                if (d < radius) {
                    double t = d == 0 ? 0 : Math.pow(Math.sin(Math.PI / 2 * d / radius), 0.8);
                    double dx = xcoord * (t - 1) / (radius * 2);
                    double dy = ycoord * (t - 1) / (radius * 2);

                    int blue = (int) (0x80 + dx * 0xff);
                    int green = (int) (0x80 + dy * 0xff);

                    int xx = (int) (x + (blue - 128) * (factor * 100) / 256);
                    int yy = (int) (y + (green - 128) * (factor * 100) / 256);
                    if ((yy * width + xx) >= 0 && (yy * width + xx) < width * height) {
                        outPixels[y * width + x] = pixels[(int) (yy * width + xx)];
                    }

                } else
                    outPixels[y * width + x] = pixels[y * width + x];
            }
        }

        bmpDst.setPixels(outPixels, 0, width, 0, 0, width, height);
        return bmpDst;
    }
}