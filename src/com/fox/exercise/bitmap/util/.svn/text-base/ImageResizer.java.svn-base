/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.fox.exercise.bitmap.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import com.fox.exercise.util.IImage;

//import com.vee.beauty.gallery.IImage;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

/**
 * A simple subclass of {@link ImageWorker} that resizes images from resources
 * given a target width and height. Useful for when the input images might be
 * too large to simply load directly into memory.
 */
public class ImageResizer extends ImageWorker {
    private static final String TAG = "ImageWorker";
    protected int mImageWidth;
    protected int mImageHeight;
    protected String cacheDir;

    /**
     * Initialize providing a single target image size (used for both width and
     * height);
     *
     * @param context
     * @param imageWidth
     * @param imageHeight
     */
    public ImageResizer(Context context, int imageWidth, int imageHeight, String cacheDir) {
        super(context);
        this.cacheDir = cacheDir;
        setImageSize(imageWidth, imageHeight);
    }

    /**
     * Initialize providing a single target image size (used for both width and
     * height);
     *
     * @param context
     * @param imageSize
     */
    public ImageResizer(Context context, int imageSize) {
        super(context);
        setImageSize(imageSize);
    }

    /**
     * Set the target image width and height.
     *
     * @param width
     * @param height
     */
    public void setImageSize(int width, int height) {
        mImageWidth = width;
        mImageHeight = height;
    }

    /**
     * Set the target image size (width and height will be the same).
     *
     * @param size
     */
    public void setImageSize(int size) {
        setImageSize(size, size);
    }

    /**
     * The main processing method. This happens in a background task. In this
     * case we are just sampling down the bitmap and returning it from a
     * resource.
     *
     * @param resId
     * @return
     */
    private Bitmap processBitmap(int resId, boolean filterUrl) {
        Log.d(TAG, "processBitmap - " + resId);
        return decodeSampledBitmapFromResource(mContext.getResources(), resId,
                mImageWidth, mImageHeight);
    }

    @Override
    protected Bitmap processBitmap(String url, boolean filterUrl) {
        return processBitmap(Integer.parseInt(url), filterUrl);
    }

    /**
     * Decode and sample down a bitmap from resources to the requested width and
     * height.
     *
     * @param res       The resources object containing the image data
     * @param resId     The resource id of the image data
     * @param reqWidth  The requested width of the resulting bitmap
     * @param reqHeight The requested height of the resulting bitmap
     * @return A bitmap sampled down from the original with the same aspect
     * ratio and dimensions that are equal to or greater than the
     * requested width and height
     */
    public static Bitmap decodeSampledBitmapFromResource(Resources res,
                                                         int resId, int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth,
                reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    /**
     * Decode and sample down a bitmap from a file to the requested width and
     * height.
     *
     * @param filename  The full path of the file to decode
     * @param reqWidth  The requested width of the resulting bitmap
     * @param reqHeight The requested height of the resulting bitmap
     * @return A bitmap sampled down from the original with the same aspect
     * ratio and dimensions that are equal to or greater than the
     * requested width and height
     */
    public static synchronized Bitmap decodeSampledBitmapFromFile(
            String filename, int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filename, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth,
                reqHeight);
//		options.inSampleSize = computeSampleSize(options,-1, reqWidth*reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        Bitmap bm = null;
        InputStream stream = null;
        try {
            stream = new FileInputStream(filename);
            try {
                bm = BitmapFactory.decodeStream(stream, null, options);
            } catch (OutOfMemoryError e) {
                bm = null;
                Log.d(TAG, "oom");
                e.printStackTrace();
                try {
                    options.inSampleSize = options.inSampleSize * 2;
                    bm = BitmapFactory.decodeStream(stream, null, options);
                } catch (OutOfMemoryError e2) {
                    e2.printStackTrace();
                }
            }
            if (bm == null) {
                // retry Once More for skia decode failed case
                Log.d(TAG,
                        "TODO retry decode Once More for skia decode failed case.");
                if (stream != null) {
                    stream.close();
                }
                stream = new FileInputStream(filename);
                try {
                    bm = BitmapFactory.decodeStream(stream, null, options);
                } catch (OutOfMemoryError e) {
                    bm = null;
                    e.printStackTrace();
                    options.inSampleSize = calculateInSampleSizeByMemory(
                            options, filename);
                    try {
                        bm = BitmapFactory.decodeStream(stream, null, options);
                    } catch (OutOfMemoryError e2) {
                        e2.printStackTrace();
                    }
                }
                // for test
                if (bm == null) {
                    Log.d(TAG, "TODO retry still return null, damn it!");
                } else {
                    Log.d(TAG, "TODO retry success!");
                }

            } else {
                Log.d(TAG, "TODO decode from local file success!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            /*
			 * do nothing. If the exception happened on open, bm will be null.
			 */
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    // do nothing here
                }
            }
        }
        return bm;
        // return BitmapFactory.decodeFile(filename, options);
    }

    /**
     * Calculate an inSampleSize for use in a {@link BitmapFactory.Options}
     * object when decoding bitmaps using the decode* methods from
     * {@link BitmapFactory}. This implementation calculates the closest
     * inSampleSize that will result in the final decoded bitmap having a width
     * and height equal to or larger than the requested width and height. This
     * implementation does not ensure a power of 2 is returned for inSampleSize
     * which can be faster when decoding but results in a larger bitmap which
     * isn't as useful for caching purposes.
     *
     * @param options   An options object with out* params already populated (run
     *                  through a decode* method with inJustDecodeBounds==true
     * @param reqWidth  The requested width of the resulting bitmap
     * @param reqHeight The requested height of the resulting bitmap
     * @return The value to be used for inSampleSize
     */
//	public static int calculateInSampleSize(BitmapFactory.Options options,
//			int reqWidth, int reqHeight) {
//		// Raw height and width of image
//		final int height = options.outHeight;
//		final int width = options.outWidth;
//		Log.d(TAG, "original width:" + width);
//		Log.d(TAG, "original height:" + height);
//		int inSampleSize = 1;
//
//		// if (height > reqHeight || width > reqWidth) {
//		// if (width > height) {
//		// inSampleSize = Math.round((float) height / (float) reqHeight);
//		// } else {
//		// inSampleSize = Math.round((float) width / (float) reqWidth);
//		// }
//		//
//		// // This offers some additional logic in case the image has a strange
//		// // aspect ratio. For example, a panorama may have a much larger
//		// // width than height. In these cases the total pixels might still
//		// // end up being too large to fit comfortably in memory, so we should
//		// // be more aggressive with sample down the image (=larger
//		// // inSampleSize).
//		//
//		//
//		//
//		// final float totalPixels = width * height;
//		//
//		// // Anything more than 2x the requested pixels we'll sample down
//		// // further.
//		// final float totalReqPixelsCap = reqWidth * reqHeight * 2;
//		//
//		// while (totalPixels / (inSampleSize * inSampleSize) >
//		// totalReqPixelsCap) {
//		// inSampleSize++;
//		// }
//		// }
//
//		// test begin
//		if (width > reqWidth || height > reqHeight) {
//			inSampleSize = (int) Math.pow(
//					2.0,
//					(int) Math.round(Math.log(reqWidth
//							/ (double) Math.max(height, width))
//							/ Math.log(0.5)));
//			// scale = 2;
//		}
//		// test end
//		if ((width / 2 >= 400 || height / 2 >= 400) && inSampleSize == 1) {
//			inSampleSize = 2;
//		}
//		
//	
//		Log.d(TAG, "inSampleSize:" + inSampleSize);
//		return inSampleSize;
//	}
    public static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight) {
        // Raw height and width of image
        int height = options.outHeight;
        int width = options.outWidth;
        Log.d(TAG, "original width:" + width);
        Log.d(TAG, "original height:" + height);
        int inSampleSize = 1;
//		int widthScale = width / reqWidth;
//		int heightScale = height / reqHeight;
        // if (height > reqHeight || width > reqWidth) {
        // if (width > height) {
        // inSampleSize = Math.round((float) height / (float) reqHeight);
        // } else {
        // inSampleSize = Math.round((float) width / (float) reqWidth);
        // }
        //
        // // This offers some additional logic in case the image has a strange
        // // aspect ratio. For example, a panorama may have a much larger
        // // width than height. In these cases the total pixels might still
        // // end up being too large to fit comfortably in memory, so we should
        // // be more aggressive with sample down the image (=larger
        // // inSampleSize).
        //
        //
        //
        // final float totalPixels = width * height;
        //
        // // Anything more than 2x the requested pixels we'll sample down
        // // further.
        // final float totalReqPixelsCap = reqWidth * reqHeight * 2;
        //
        // while (totalPixels / (inSampleSize * inSampleSize) >
        // totalReqPixelsCap) {
        // inSampleSize++;
        // }
        // }

//		// test begin
//		if (width > reqWidth || height > reqHeight) {
//			inSampleSize = (int) Math.pow(
//					2.0,
//					(int) Math.round(Math.log(reqWidth
//							/ (double) Math.max(height, width))
//							/ Math.log(0.5)));
//			// scale = 2;
//		}
//		// test end
//		if ((width / 2 >= 400 || height / 2 >= 400) && inSampleSize == 1) {
//			inSampleSize = 2;
//		}
        while (width / 2 >= reqWidth && height / 2 >= reqHeight) { // &&
            width /= 2;
            height /= 2;
            inSampleSize *= 2;
        }
        if (inSampleSize < 1) {
            inSampleSize = 1;
        }
        Log.d(TAG, "inSampleSize:" + inSampleSize);
        return inSampleSize;
    }

    public static int calculateInSampleSizeByMemory(
            BitmapFactory.Options options, String filename) {
        int inSampleSize = 1;

        File f = new File(filename);
        Log.d(TAG, "f.length():" + f.length());
        long k = (f.length() / 8192000L);
        Log.d(TAG, "k:" + k);
        inSampleSize = (int) Math.pow(2.0, k);
        Log.d(TAG, "inSampleSize in calculateInSampleSizeByMemory:" + inSampleSize);

        return inSampleSize;
    }


    /*
     * Compute the sample size as a function of minSideLength
     * and maxNumOfPixels.
     * minSideLength is used to specify that minimal width or height of a
     * bitmap.
     * maxNumOfPixels is used to specify the maximal size in pixels that is
     * tolerable in terms of memory usage.
     *
     * The function returns a sample size based on the constraints.
     * Both size and minSideLength can be passed in as IImage.UNCONSTRAINED,
     * which indicates no care of the corresponding constraint.
     * The functions prefers returning a sample size that
     * generates a smaller bitmap, unless minSideLength = IImage.UNCONSTRAINED.
     *
     * Also, the function rounds up the sample size to a power of 2 or multiple
     * of 8 because BitmapFactory only honors sample size this way.
     * For example, BitmapFactory downsamples an image by 2 even though the
     * request is 3. So we round up the sample size to avoid OOM.
     */
    public static int computeSampleSize(BitmapFactory.Options options,
                                        int minSideLength, int maxNumOfPixels) {
        int initialSize = computeInitialSampleSize(options, minSideLength,
                maxNumOfPixels);
        Log.d(TAG, "initialSize:" + initialSize);
        int roundedSize;
        if (initialSize <= 8) {
            roundedSize = 1;
            while (roundedSize < initialSize) {
                roundedSize <<= 1;
            }
        } else {
            roundedSize = (initialSize + 7) / 8 * 8;
        }
        Log.d(TAG, "roundedSize:" + roundedSize);
        return roundedSize;
    }

    private static int computeInitialSampleSize(BitmapFactory.Options options,
                                                int minSideLength, int maxNumOfPixels) {
        double w = options.outWidth;
        double h = options.outHeight;

        int lowerBound = (maxNumOfPixels == IImage.UNCONSTRAINED) ? 1 :
                (int) Math.ceil(Math.sqrt(w * h / maxNumOfPixels));
        int upperBound = (minSideLength == IImage.UNCONSTRAINED) ? 128 :
                (int) Math.min(Math.floor(w / minSideLength),
                        Math.floor(h / minSideLength));

        if (upperBound < lowerBound) {
            // return the larger one when there is no overlapping zone.
            return lowerBound;
        }

        if ((maxNumOfPixels == IImage.UNCONSTRAINED) &&
                (minSideLength == IImage.UNCONSTRAINED)) {
            return 1;
        } else if (minSideLength == IImage.UNCONSTRAINED) {
            return lowerBound;
        } else {
            return upperBound;
        }
    }
}
