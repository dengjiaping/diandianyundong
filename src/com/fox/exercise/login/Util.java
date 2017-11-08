package com.fox.exercise.login;

import java.io.BufferedInputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.fox.exercise.R;
import com.fox.exercise.Utils;
import com.fox.exercise.weibo.sina.StringUtils;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory.Options;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.Camera;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.os.StatFs;
import android.provider.MediaStore.Images;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class Util {

    private static final String TAG = "Util";

    public static int IMAGE_DEFAULT_WIDTH = 640;
    public static int IMAGE_DEFAULT_HEIGHT = 480;

    public static int IMAGE_SCALE_WIDTH = 640;
    public static int IMAGE_SCALE_HEIGHT = 360;

    public static int IMAGE_SPACE = 60;

    public static final int NO_STORAGE_ERROR = -1;
    public static final int CANNOT_STAT_ERROR = -2;

    public static final int INCLUDE_IMAGES = (1 << 0);
    public static final int INCLUDE_DRM_IMAGES = (1 << 1);

    private static final Uri STORAGE_URI = Images.Media.EXTERNAL_CONTENT_URI;
    public static final String PIXEL_FORMAT_JPEG = "jpeg";
    public static final String PIXEL_FORMAT_PNG = "png";
    public static final String PIXEL_FORMAT_RAW = "raw";
    public static final String CAMERA_MANAGER_BUCKET_NAME =
            Environment.getExternalStorageDirectory().toString();
    public static final String IMAGE_SAVE_PATH = CAMERA_MANAGER_BUCKET_NAME + "/DCIM/Camera";
    public static final String IMAGE_SAVE_PATH_TEMP = CAMERA_MANAGER_BUCKET_NAME + "/MTZS/.temp";
    private static final String IMAGE_TEMP_NAME = "temp";

    public static final String IMAGE_SAVE_PATH_HISTORY = CAMERA_MANAGER_BUCKET_NAME + "/MTZS/.history";
    public static final String IMAGE_HISTORY_NAME = "history";

    //    private static CompressFormat mOutputFormat = Bitmap.CompressFormat.JPEG;
    private static CompressFormat mOutputFormat = Bitmap.CompressFormat.PNG;

    public static String mPictureFormat = null;
    public static String mFilePath = null;
    public static String mHistoryFilePath = null;
    public static String mTFilePath = null;
    public static boolean mImageSaveToTemp = true;

    public static Uri mCurrContentUri;

    public static final boolean RECYCLE_INPUT = true;
    public static final boolean NO_RECYCLE_INPUT = false;

    private static OnScreenHint mStorageHint;

    public static final int EXIT_OK = 2;
    public static final int ADD_TEXT = 3;
    public static final int CUSTOM = 4;
    public static final int BACK_TO_CAMEA = 5;
    public static final int ENLARGE_EYES = 6;
    public static final int LIQUID = 7;
    public static final int PROPS = 8;
    public static final int COSMETIC = 9;
    public static final int EDIT_IMAGE = 10;

    public static final String HORIZONTAL = "horizontal";
    public static final String VERTICAL = "vertical";
    public static final String NONE = "none";
    public static final String CARD = "card";
    public static final String FRAME = "frame";

    public static final String PREF_NAME_FRONT_CAMERA_DEGREE = "pref_front_camera_degree";
    public static final String PREF_I_FRONT_CAMERA_DEGREE = "pref_Int_front_camera_degree";
    public static final String PREF_NAME_FRONT_PICTURE_DEGREE = "pref_front_picture_degree";
    public static final String PREF_I_FRONT_PICTURE_DEGREE = "pref_Int_front_picture_degree";

    public static final String PREF_NAME_BACK_CAMERA_DEGREE = "pref_back_camera_degree";
    public static final String PREF_I_BACK_CAMERA_DEGREE = "pref_Int_back_camera_degree";
    public static final String PREF_NAME_BACK_PICTURE_DEGREE = "pref_back_picture_degree";
    public static final String PREF_I_BACK_PICTURE_DEGREE = "pref_Int_back_picture_degree";

    public static void showFatalErrorAndFinish(
            final Activity activity, String title, String message) {
        DialogInterface.OnClickListener buttonListener =
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        activity.finish();
                    }
                };
        new AlertDialog.Builder(activity)
                .setCancelable(false)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle(title)
                .setMessage(message)
                .setNeutralButton(R.string.details_ok, buttonListener)
                .show();
    }

    public static void confirmAction(Context context, String title,
                                     String message, final Runnable action_p, final Runnable action_n) {
        OnClickListener listener = new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        if (action_p != null) action_p.run();
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        if (action_n != null) action_n.run();
                        break;
                }
            }
        };
        new AlertDialog.Builder(context)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.yes, listener)
                .setNegativeButton(android.R.string.no, listener)
                .create()
                .show();
    }

    public static String storeImage(Context context, byte[] data, Bitmap bitmap, boolean dataBmp, String mPictureFormat) {
        String ext = null;
        String store_location = null;
        String filename = null;
        String title = null;
        long dateTaken = 0;
        int[] degree = new int[1];
        ContentResolver cr = context.getContentResolver();
        try {
//			 if(mPictureFormat == null||PIXEL_FORMAT_PNG.equalsIgnoreCase(mPictureFormat)){
//                 ext = ".png";
//             }
//            if(PIXEL_FORMAT_JPEG.equalsIgnoreCase(mPictureFormat)){
//                ext = ".jpg";
//            }
//
//            if(PIXEL_FORMAT_RAW.equalsIgnoreCase(mPictureFormat)){
//                ext = ".raw";
//            }
            mOutputFormat = Bitmap.CompressFormat.JPEG;
            ext = ".jpg";
            if (mImageSaveToTemp) {
                store_location = IMAGE_SAVE_PATH_TEMP;
                title = IMAGE_TEMP_NAME;
            } else {
                store_location = IMAGE_SAVE_PATH;
                dateTaken = System.currentTimeMillis();
                Date date = new Date(dateTaken);
                SimpleDateFormat dateFormat = new SimpleDateFormat(
                        context.getString(R.string.image_file_name_format));
                title = dateFormat.format(date);
            }

            filename = title + ext;
            mFilePath = store_location + "/" + filename;
            OutputStream outputStream = null;
            try {
                File dir = new File(store_location);
                if (!dir.exists()) dir.mkdirs();
                File file = new File(store_location, filename);
                outputStream = new FileOutputStream(file);
                if (dataBmp && bitmap != null)
                    bitmap.compress(mOutputFormat, 90, outputStream);
                else if (!dataBmp && data != null)
                    outputStream.write(data);
                degree[0] = getExifOrientation(mFilePath);
            } catch (FileNotFoundException ex) {
                Log.w(TAG, ex);
            } catch (IOException ex) {
                Log.w(TAG, ex);
            } finally {
                Utils.closeSilently(outputStream);
            }
        } catch (Exception ex) {
            Log.e(TAG, "Exception while compressing image.", ex);
        }
        data = null;
        bitmap = null;

        if (!mImageSaveToTemp) {
            long size = new File(store_location, filename).length();
            ContentValues values = new ContentValues(7);
            values.put(Images.Media.TITLE, title);
            values.put(Images.Media.DISPLAY_NAME, filename);
            values.put(Images.Media.DATE_TAKEN, dateTaken);
            values.put(Images.Media.MIME_TYPE, "image/jpg");
            values.put(Images.Media.ORIENTATION, degree[0]);
            values.put(Images.Media.DATA, mFilePath);
            values.put(Images.Media.SIZE, size);
            mCurrContentUri = cr.insert(STORAGE_URI, values);
            context.sendBroadcast(new Intent(
                    "com.android.camera.NEW_PICTURE", mCurrContentUri));
        }
        return mFilePath;
    }

    public static Uri storeUriImage(Context context, byte[] data, Bitmap bitmap, boolean dataBmp, String mPictureFormat, boolean recycle) {
        boolean saveSuccess = true;
        String ext = null;
        String store_location = null;
        String filename = null;
        String title = null;
        long dateTaken = 0;
        int[] degree = new int[1];
        ContentResolver cr = context.getContentResolver();
        try {
//			 if(mPictureFormat == null||PIXEL_FORMAT_PNG.equalsIgnoreCase(mPictureFormat)){
//                 ext = ".png";
//             }
//            if(PIXEL_FORMAT_JPEG.equalsIgnoreCase(mPictureFormat)){
//                ext = ".jpg";
//            }
//
//            if(PIXEL_FORMAT_RAW.equalsIgnoreCase(mPictureFormat)){
//                ext = ".raw";
//            }
            mOutputFormat = Bitmap.CompressFormat.JPEG;
            ext = ".jpg";
            if (mImageSaveToTemp) {
                store_location = IMAGE_SAVE_PATH_TEMP;
                title = IMAGE_TEMP_NAME;
            } else {
                store_location = IMAGE_SAVE_PATH;
                dateTaken = System.currentTimeMillis();
                Date date = new Date(dateTaken);
                SimpleDateFormat dateFormat = new SimpleDateFormat(
                        context.getString(R.string.image_file_name_format));
                title = dateFormat.format(date);
            }
//            store_location=CAMERA_MANAGER_BUCKET_NAME+"/DCIM/Camera";
            store_location = CAMERA_MANAGER_BUCKET_NAME + "/MTZS/.temp";

            Log.d(TAG, "camera save path:" + store_location);

            filename = title + ext;
            mFilePath = store_location + "/" + filename;
            OutputStream outputStream = null;
            try {
                File dir = new File(store_location);
                if (!dir.exists()) dir.mkdirs();
                File file = new File(store_location, filename);
                outputStream = new FileOutputStream(file);
                if (dataBmp && bitmap != null)
                    bitmap.compress(mOutputFormat, 90, outputStream);
                else if (!dataBmp && data != null)
                    outputStream.write(data);
                degree[0] = getExifOrientation(mFilePath);

            } catch (FileNotFoundException ex) {
                Log.w(TAG, ex);
                saveSuccess = false;
            } catch (IOException ex) {
                Log.w(TAG, ex);
                saveSuccess = false;
            } finally {
                Utils.closeSilently(outputStream);
            }
        } catch (Exception ex) {
            Log.e(TAG, "Exception while compressing image.", ex);
            saveSuccess = false;
        }
        data = null;
        if (recycle && bitmap != null && !bitmap.isRecycled()) {
            bitmap.recycle();
            bitmap = null;
        }

        if (saveSuccess && !mImageSaveToTemp) {
            long size = new File(store_location, filename).length();
            ContentValues values = new ContentValues(7);
            values.put(Images.Media.TITLE, title);
            values.put(Images.Media.DISPLAY_NAME, filename);
            values.put(Images.Media.DATE_TAKEN, dateTaken);
            values.put(Images.Media.MIME_TYPE, "image/jpg");
            values.put(Images.Media.ORIENTATION, degree[0]);
            values.put(Images.Media.DATA, mFilePath);
            values.put(Images.Media.SIZE, size);
            mCurrContentUri = cr.insert(STORAGE_URI, values);
            Log.d(TAG, "mCurrContentUri:" + mCurrContentUri);
//	        context.sendBroadcast(new Intent(
//	                "com.android.camera.NEW_PICTURE", mCurrContentUri));
//	        Log.d(TAG,"storage update broadcast sent");
        }
        return Uri.parse(mFilePath);
    }

    public static int getExifOrientation(String filepath) {
        int degree = 0;
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(filepath);
        } catch (IOException ex) {
            Log.e(TAG, "cannot read exif", ex);
        }
        if (exif != null) {
            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, -1);
            if (orientation != -1) {
                // We only recognize a subset of orientation tag values.
                switch (orientation) {
                    case ExifInterface.ORIENTATION_ROTATE_90:
                        degree = 90;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_180:
                        degree = 180;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_270:
                        degree = 270;
                        break;
                }

            }
        }
        return degree;
    }

    public static void closeSilently(Closeable c) {
        if (c == null)
            return;
        try {
            c.close();
        } catch (Throwable t) {
            // do nothing
        }
    }

    public static final Bitmap resizeBitmap(Bitmap bitmap, int width, int height) {
        Bitmap retVal = Bitmap.createScaledBitmap(bitmap, width, height, true);
        bitmap.recycle();
        bitmap = null;
        return retVal;
    }

    public static Bitmap byte2Bitmap(byte[] buffer, boolean crop) {
        if (!crop)
            return BitmapFactory.decodeByteArray(buffer, 0, buffer.length);
        else {
            int space = IMAGE_SPACE;
            Rect rect = new Rect();
            Bitmap retVal = BitmapFactory.decodeByteArray(buffer, 0, buffer.length);
            int w = retVal.getWidth();
            int h = retVal.getHeight();
            Log.d(TAG, "retVal.getWidth()" + retVal.getWidth());
            Log.d(TAG, "retVal.getHeight()" + retVal.getHeight());
            if (w < h) {
                rect.left = 0;
                rect.top = space;
                rect.right = w;
                rect.bottom = h - space;
            } else {
                rect.left = space;
                rect.top = 0;
                rect.right = w - space;
                rect.bottom = h;
            }
            int width = rect.width(); // CR: final == happy panda!
            int height = rect.height();
            Bitmap croppedImage = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            {
                Canvas canvas = new Canvas(croppedImage);
                Rect dstRect = new Rect(0, 0, width, height);
                canvas.drawBitmap(retVal, rect, dstRect, null);
            }
            return croppedImage;
        }
    }

    public static int calculatePicturesRemaining() {
        try {
            if (!hasStorage()) {
                return NO_STORAGE_ERROR;
            } else {
                String storageDirectory =
                        Environment.getExternalStorageDirectory().toString();
                StatFs stat = new StatFs(storageDirectory);
                final int PICTURE_BYTES = 1500000;
                float remaining = ((float) stat.getAvailableBlocks()
                        * (float) stat.getBlockSize()) / PICTURE_BYTES;
                return (int) remaining;
            }
        } catch (Exception ex) {
            // if we can't stat the filesystem then we don't know how many
            // pictures are remaining.  it might be zero but just leave it
            // blank since we really don't know.
            Log.e(TAG, "Fail to access sdcard", ex);
            return CANNOT_STAT_ERROR;
        }
    }

    private static boolean checkFsWritable() {
        // Create a temporary file to see whether a volume is really writeable.
        // It's important not to put it in the root directory which may have a
        // limit on the number of files.
        String directoryName =
                Environment.getExternalStorageDirectory().toString() + "/DCIM";
        File directory = new File(directoryName);
        if (!directory.isDirectory()) {
            if (!directory.mkdirs()) {
                return false;
            }
        }
        File f = new File(directoryName, ".probe");
        try {
            //Remove stale file if any
            if (f.exists()) {
                f.delete();
            }
            if (!f.createNewFile()) {
                return false;
            }
            f.delete();
            return true;
        } catch (IOException ex) {
            return false;
        }
    }

    public static boolean hasStorage() {
        return hasStorage(true);
    }

    public static boolean hasStorage(boolean requireWriteAccess) {
        String state = Environment.getExternalStorageState();

        if (Environment.MEDIA_MOUNTED.equals(state)) {
            if (requireWriteAccess) {
                boolean writable = checkFsWritable();
                return writable;
            } else {
                return true;
            }
        } else if (!requireWriteAccess
                && Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

    public static Bitmap transform(Matrix scaler,
                                   Bitmap source,
                                   int targetWidth,
                                   int targetHeight,
                                   boolean scaleUp,
                                   boolean recycle) {
        int deltaX = source.getWidth() - targetWidth;
        int deltaY = source.getHeight() - targetHeight;
        if (!scaleUp && (deltaX < 0 || deltaY < 0)) {
            /*
			* In this case the bitmap is smaller, at least in one dimension,
			* than the target.  Transform it by placing as much of the image
			* as possible into the target and leaving the top/bottom or
			* left/right (or both) black.
			*/
            Bitmap b2 = Bitmap.createBitmap(targetWidth, targetHeight,
                    Bitmap.Config.ARGB_8888);
            Canvas c = new Canvas(b2);

            int deltaXHalf = Math.max(0, deltaX / 2);
            int deltaYHalf = Math.max(0, deltaY / 2);
            Rect src = new Rect(
                    deltaXHalf,
                    deltaYHalf,
                    deltaXHalf + Math.min(targetWidth, source.getWidth()),
                    deltaYHalf + Math.min(targetHeight, source.getHeight()));
            int dstX = (targetWidth - src.width()) / 2;
            int dstY = (targetHeight - src.height()) / 2;
            Rect dst = new Rect(
                    dstX,
                    dstY,
                    targetWidth - dstX,
                    targetHeight - dstY);
            c.drawBitmap(source, src, dst, null);
            if (recycle) {
                source.recycle();
            }
            return b2;
        }
        float bitmapWidthF = source.getWidth();
        float bitmapHeightF = source.getHeight();

        float bitmapAspect = bitmapWidthF / bitmapHeightF;
        float viewAspect = (float) targetWidth / targetHeight;

        if (bitmapAspect > viewAspect) {
            float scale = targetHeight / bitmapHeightF;
            if (scale < .9F || scale > 1F) {
                scaler.setScale(scale, scale);
            } else {
                scaler = null;
            }
        } else {
            float scale = targetWidth / bitmapWidthF;
            if (scale < .9F || scale > 1F) {
                scaler.setScale(scale, scale);
            } else {
                scaler = null;
            }
        }

        Bitmap b1;
        if (scaler != null) {
            // this is used for minithumb and crop, so we want to filter here.
            b1 = Bitmap.createBitmap(source, 0, 0,
                    source.getWidth(), source.getHeight(), scaler, true);
        } else {
            b1 = source;
        }

        if (recycle && b1 != source) {
            source.recycle();
        }

        int dx1 = Math.max(0, b1.getWidth() - targetWidth);
        int dy1 = Math.max(0, b1.getHeight() - targetHeight);

        Bitmap b2 = Bitmap.createBitmap(
                b1,
                dx1 / 2,
                dy1 / 2,
                targetWidth,
                targetHeight);

        if (b2 != b1) {
            if (recycle || b1 != source) {
                b1.recycle();
            }
        }

        return b2;
    }

    public static <T> T checkNotNull(T object) {
        if (object == null) throw new NullPointerException();
        return object;
    }

    public static boolean equals(Object a, Object b) {
        return (a == b) || (a == null ? false : a.equals(b));
    }


    public static Uri storeTempImage(Context context, Bitmap bitmap, String identifer) {
        String ext = null;
        String store_location = null;
        String filename = null;
        String title = null;
        int[] degree = new int[1];
        try {
            Log.d(TAG, "mPictureFormat" + mPictureFormat);
//        	 if(mPictureFormat == null||PIXEL_FORMAT_PNG.equalsIgnoreCase(mPictureFormat)){
//                 ext = ".png";
//             }
//            if(PIXEL_FORMAT_JPEG.equalsIgnoreCase(mPictureFormat)){
//                ext = ".jpg";
//            }
//
//            if(PIXEL_FORMAT_RAW.equalsIgnoreCase(mPictureFormat)){
//                ext = ".raw";
//            }
            mOutputFormat = Bitmap.CompressFormat.PNG;
            ext = ".png";
//        	mOutputFormat=Bitmap.CompressFormat.JPEG;
//        	ext = ".jpg";
            store_location = IMAGE_SAVE_PATH_TEMP;
            title = IMAGE_TEMP_NAME + identifer;

            filename = title + ext;
            mFilePath = store_location + "/" + filename;
            OutputStream outputStream = null;
            try {
                File dir = new File(store_location);
                if (!dir.exists()) dir.mkdirs();
                File file = new File(store_location, filename);
                outputStream = new FileOutputStream(file);
                if (bitmap != null) {
                    bitmap.compress(mOutputFormat, 100, outputStream);
                    Log.d(TAG, "before");
                    degree[0] = getExifOrientation(mFilePath);
                    return Uri.parse(mFilePath);
                }
            } catch (FileNotFoundException ex) {
                Log.w(TAG, ex);
            } finally {
                Utils.closeSilently(outputStream);
            }
        } catch (Exception ex) {
            Log.e(TAG, "Exception while compressing image.", ex);
        }
        bitmap = null;
        return null;
    }


    private static int identifer = 0;

    public static Uri storeHistoryImage(Context context, Bitmap bitmap) {
        String ext = null;
        String store_location = null;
        String filename = null;
        String title = null;
        int[] degree = new int[1];

        try {
//        	 if(mPictureFormat == null||PIXEL_FORMAT_PNG.equalsIgnoreCase(mPictureFormat)){
//                 ext = ".png";
//             }
//            if(PIXEL_FORMAT_JPEG.equalsIgnoreCase(mPictureFormat)){
//                ext = ".jpg";
//            }
//
            if (PIXEL_FORMAT_RAW.equalsIgnoreCase(mPictureFormat)) {
                ext = ".raw";
            }
            mOutputFormat = Bitmap.CompressFormat.PNG;
            ext = ".png";
//            if(ImageProcess.mHistoryBmps_undoList.size()==20){
//           
//            	ImageProcess.mHistoryBmps_undoList.removeFirst();
//            }
            ext = ".png";
            store_location = IMAGE_SAVE_PATH_HISTORY;
            title = IMAGE_HISTORY_NAME + identifer;

            filename = title + ext;


            OutputStream outputStream = null;
            try {
                File dir = new File(store_location);
                if (!dir.exists()) dir.mkdirs();
                File[] fileList = dir.listFiles(new FilenameFilter() {

                    @Override
                    public boolean accept(File dir, String name) {

                        return name.endsWith(".png");
                    }
                });
                for (File file : fileList) {
                    if (filename.equals(file.getName())) {
                        identifer++;
                        title = IMAGE_HISTORY_NAME + identifer;

                        filename = title + ext;
                    }
                }
                mHistoryFilePath = store_location + "/" + filename;
                File file = new File(store_location, filename);

                outputStream = new FileOutputStream(file);
                if (bitmap != null) {
                    bitmap.compress(mOutputFormat, 100, outputStream);
                    degree[0] = getExifOrientation(mHistoryFilePath);
                    if (mHistoryFilePath != null)
                        return Uri.parse(mHistoryFilePath);
                }
            } catch (FileNotFoundException ex) {
                Log.w(TAG, ex);
            } finally {
                Utils.closeSilently(outputStream);
            }
        } catch (Exception ex) {
            Log.e(TAG, "Exception while compressing image.", ex);
        }
        bitmap = null;
        return null;
    }

    public static void displayErrorHint(Context context, String hintText) {

        if (hintText != null) {
            if (mStorageHint == null) {
                mStorageHint = OnScreenHint.makeText(context, hintText);
            } else {
                mStorageHint.setText(hintText);
            }
            mStorageHint.show();
        } else if (mStorageHint != null) {
            mStorageHint.cancel();
            mStorageHint = null;
        }
    }

    private static long lastClickTime;

    public static boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        Log.d(TAG, "double click time:" + (time - lastClickTime));
        if (time - lastClickTime < 2000) {
            return true;
        }
        lastClickTime = time;
        return false;
    }

    public static String getFileName(String filePath) {
        if (StringUtils.isBlank(filePath)) {
            return "";
        }

        return filePath.substring(filePath.lastIndexOf(File.separator) + 1);
    }

    public static String getSignature(List<String> paramList, String secret) {
        Collections.sort(paramList);

        StringBuffer buffer = new StringBuffer();
        for (String param : paramList) {
            buffer.append(param);
        }

        buffer.append(secret);
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            StringBuffer result = new StringBuffer();
            try {
                for (byte b : md.digest(buffer.toString().getBytes("UTF-8"))) {
                    result.append(Integer.toHexString((b & 0xf0) >>> 4));
                    result.append(Integer.toHexString(b & 0x0f));
                }
            } catch (UnsupportedEncodingException e) {
                for (byte b : md.digest(buffer.toString().getBytes())) {
                    result.append(Integer.toHexString((b & 0xf0) >>> 4));
                    result.append(Integer.toHexString(b & 0x0f));
                }
            }

            return result.toString();
        } catch (java.security.NoSuchAlgorithmException ex) {

        }

        return null;
    }


    public static Bitmap decodeFile(String path, Options o) {


        File file = new File(path);
        InputStream is = null;
        try {
            is = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Bitmap bmp = BitmapFactory.decodeStream(is, null, o);

        return bmp;
    }

    public static Bitmap decodeFile(String path) {

        int scale = 1;
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, o);
        if (o.outWidth > IMAGE_DEFAULT_WIDTH || o.outHeight > IMAGE_DEFAULT_HEIGHT) {
            scale = (int) Math.pow(
                    2.0,
                    (int) Math.round(Math.log(IMAGE_DEFAULT_WIDTH
                            / (double) Math.max(o.outHeight, o.outWidth))
                            / Math.log(0.5)));
            // scale = 2;
        }
        Log.d(TAG, scale + " scale");

        // 2. File -> Bitmap (Returning a smaller image)
        o.inJustDecodeBounds = false;
        o.inSampleSize = scale;
        o.inTempStorage = new byte[4 * 1024];
        o.inInputShareable = true;
        o.inPurgeable = true;
        o.inPreferredConfig = Bitmap.Config.RGB_565;
        File file = new File(path);
        InputStream is = null;
        try {
            is = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Bitmap bmp = BitmapFactory.decodeStream(is, null, o);
        return bmp;
    }

    static class FlushedInputStream extends BufferedInputStream {
        public FlushedInputStream(InputStream inputStream) {
            super(inputStream);
        }

        @Override
        public long skip(long n) throws IOException {
            long totalBytesSkipped = 0L;
            while (totalBytesSkipped < n) {
                long bytesSkipped = in.skip(n - totalBytesSkipped);
                if (bytesSkipped == 0L) {
                    int bytes = read();
                    if (bytes < 0) {
                        break;  // we reached EOF
                    } else {
                        bytesSkipped = 1; // we read one byte
                    }
                }
                totalBytesSkipped += bytesSkipped;
            }
            return totalBytesSkipped;
        }
    }

    public static int getDisplayRotation(Activity activity) {
        int rotation = activity.getWindowManager().getDefaultDisplay()
                .getRotation();
        switch (rotation) {
            case Surface.ROTATION_0:
                return 0;
            case Surface.ROTATION_90:
                return 90;
            case Surface.ROTATION_180:
                return 180;
            case Surface.ROTATION_270:
                return 270;
        }
        return 0;
    }

    public static void setCameraDisplayOrientation(Activity activity,
                                                   int cameraId, Camera camera, int adjustDegrees) {
        // See android.hardware.Camera.setCameraDisplayOrientation for
        // documentation.
        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(cameraId, info);
        int degrees = getDisplayRotation(activity);
        int result;
        if (info.orientation < 0)
            info.orientation = 0;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees + 360 - adjustDegrees) % 360;
            result = (360 - result) % 360;  // compensate the mirror
        } else {  // back-facing
            result = (info.orientation - degrees + 360 + adjustDegrees) % 360;
        }
        Log.v(TAG, "info.orientation=" + info.orientation + " degrees=" + degrees + " adjustDegrees=" + adjustDegrees);
        Log.v(TAG, "result is " + result);
        camera.setDisplayOrientation(result);
    }

    public static void recycle(Bitmap bitmap) {

        if ((bitmap == null) || (bitmap.isRecycled()))
            return;

        bitmap.recycle();
        bitmap = null;
        System.gc();
//        long allocNativeHeap = Debug.getNativeHeapAllocatedSize();        
//        Log.e("FlowView", "recycle allocNativeHeap="+ allocNativeHeap/8);

    }

    /**
     * �ͷ�Imageviewռ�õ�ͼƬ��Դ
     * �����˳�ʱ�ͷ���Դ��������ɺ��벻Ҫˢ�½���
     *
     * @param layout ��Ҫ�ͷ�ͼƬ�Ĳ���     *
     */
    public static void recycleView(ViewGroup layout, boolean recycleLayoutRes) {

        if (layout != null) {
            for (int i = 0; i < layout.getChildCount(); i++) {
                //��øò��ֵ������Ӳ���
                View subView = layout.getChildAt(i);
                //�ж��Ӳ������ԣ��������ViewGroup���ͣ��ݹ����
                if (subView instanceof ViewGroup) {
                    //�ݹ����
                    recycleView((ViewGroup) subView, recycleLayoutRes);
                } else {
                    //��Imageview������
                    if (subView instanceof ImageView) {
                        //����ռ�õ�Bitmap
                        recycleImageViewBitMap((ImageView) subView);
                        //���flagWithBackgroudΪtrue,��ͬʱ���ձ���ͼ
                        if (recycleLayoutRes) {

                            recycleBackgroundBitMap((ImageView) subView);
                        }
                    }
                }
            }
            layout = null;
        }

    }

    private static void recycleBackgroundBitMap(ImageView view) {
        if (view != null) {
            BitmapDrawable bd = (BitmapDrawable) view.getBackground();
            rceycleBitmapDrawable(bd);
        }
    }

    private static void recycleImageViewBitMap(ImageView imageView) {
        if (imageView != null) {
            BitmapDrawable bd = (BitmapDrawable) imageView.getDrawable();
            rceycleBitmapDrawable(bd);
        }
    }

    private static void rceycleBitmapDrawable(BitmapDrawable bd) {
        if (bd != null) {
            Bitmap bitmap = bd.getBitmap();
            rceycleBitmap(bitmap);
        }
        bd = null;
    }

    private static void rceycleBitmap(Bitmap bitmap) {
        if (bitmap != null && !bitmap.isRecycled()) {
            Log.e(TAG, "rceycleBitmap");
            bitmap.recycle();
            bitmap = null;
        }
    }

    public static Options retriveBitmapBounds(String filePath) {
        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        return options;
    }

    public static Bitmap decodeSampledBitmapNoSync(String filename,
                                                   int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filename, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        Bitmap bm = null;
        InputStream stream = null;
        try {
            stream = new FileInputStream(filename);
            bm = BitmapFactory.decodeStream(stream, null, options);
            if (bm == null) {
                //retry Once More for skia decode failed case
                Log.d(TAG, "TODO retry decode Once More for skia decode failed case.");
                if (stream != null) {
                    stream.close();
                }
                stream = new FileInputStream(filename);
                bm = BitmapFactory.decodeStream(stream, null, options);
                //for test
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
            /*  do nothing.
                If the exception happened on open, bm will be null.
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
//        return BitmapFactory.decodeFile(filename, options);
    }

    /**
     * Decode and sample down a bitmap from a file to the requested width and height.
     *
     * @param filename  The full path of the file to decode
     * @param reqWidth  The requested width of the resulting bitmap
     * @param reqHeight The requested height of the resulting bitmap
     * @return A bitmap sampled down from the original with the same aspect ratio and dimensions
     * that are equal to or greater than the requested width and height
     */
    public static synchronized Bitmap decodeSampledBitmapFromFile(String filename,
                                                                  int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filename, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        Bitmap bm = null;
        InputStream stream = null;
        try {
            stream = new FileInputStream(filename);
            bm = BitmapFactory.decodeStream(stream, null, options);
            if (bm == null) {
                //retry Once More for skia decode failed case
                Log.d(TAG, "TODO retry decode Once More for skia decode failed case.");
                if (stream != null) {
                    stream.close();
                }
                stream = new FileInputStream(filename);
                bm = BitmapFactory.decodeStream(stream, null, options);
                //for test
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
            /*  do nothing.
                If the exception happened on open, bm will be null.
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
//        return BitmapFactory.decodeFile(filename, options);
    }

    public static synchronized Bitmap decodeBitmapFromFile(String filename,
                                                           int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filename, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize_1(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        Bitmap bm = null;
        InputStream stream = null;
        try {
            stream = new FileInputStream(filename);
            bm = BitmapFactory.decodeStream(stream, null, options);
            if (bm == null) {
                //retry Once More for skia decode failed case
                Log.d(TAG, "TODO retry decode Once More for skia decode failed case.");
                if (stream != null) {
                    stream.close();
                }
                stream = new FileInputStream(filename);
                bm = BitmapFactory.decodeStream(stream, null, options);
                //for test
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
    		/*  do nothing.
                If the exception happened on open, bm will be null.
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
//        return BitmapFactory.decodeFile(filename, options);
    }

    private static int calculateInSampleSize_1(Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        int height = options.outHeight;
        int width = options.outWidth;
        int inSampleSize = 1;

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

    /**
     * Calculate an inSampleSize for use in a {@link BitmapFactory.Options} object when decoding
     * bitmaps using the decode* methods from {@link BitmapFactory}. This implementation calculates
     * the closest inSampleSize that will result in the final decoded bitmap having a width and
     * height equal to or larger than the requested width and height. This implementation does not
     * ensure a power of 2 is returned for inSampleSize which can be faster when decoding but
     * results in a larger bitmap which isn't as useful for caching purposes.
     *
     * @param options   An options object with out* params already populated (run through a decode*
     *                  method with inJustDecodeBounds==true
     * @param reqWidth  The requested width of the resulting bitmap
     * @param reqHeight The requested height of the resulting bitmap
     * @return The value to be used for inSampleSize
     */
    public static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            if (width > height) {
                inSampleSize = Math.round((float) height / (float) reqHeight);
            } else {
                inSampleSize = Math.round((float) width / (float) reqWidth);
            }

            // This offers some additional logic in case the image has a strange
            // aspect ratio. For example, a panorama may have a much larger
            // width than height. In these cases the total pixels might still
            // end up being too large to fit comfortably in memory, so we should
            // be more aggressive with sample down the image (=larger
            // inSampleSize).

            final float totalPixels = width * height;

            // Anything more than 2x the requested pixels we'll sample down
            // further.
            final float totalReqPixelsCap = reqWidth * reqHeight * 2;

            while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
                inSampleSize++;
            }
        }
        return inSampleSize;
    }

    public static void picSortByDate(File[] paramArrayOfFile) {
        Arrays.sort(paramArrayOfFile, new CompratorByLastModified());
    }

    static class CompratorByLastModified implements Comparator<File> {
        public int compare(File paramFile1, File paramFile2) {
            long d1 = paramFile1.lastModified();
            long d2 = paramFile2.lastModified();
            if (d1 > d2) {
                return -1;
            }
            if (d1 < d2) {
                return 1;
            }
            return 0;

        }
    }

    public static int getRealPixel_W(Context context, int pixel) {
        Display localDisplay = ((Activity) context).getWindowManager().getDefaultDisplay();
        DisplayMetrics localDisplayMetrics = new DisplayMetrics();
        localDisplay.getMetrics(localDisplayMetrics);
        int screenHeight = localDisplay.getHeight();
        int screenWidth = localDisplay.getWidth();
        return pixel * screenWidth / 480;
    }
}