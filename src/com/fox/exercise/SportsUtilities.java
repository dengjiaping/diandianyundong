package com.fox.exercise;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;

import cn.ingenic.indroidsync.SportsApp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.os.StatFs;
import android.provider.MediaStore.Images;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

final public class SportsUtilities {
    private static final String TAG = "SportsUtilities";
    public static String DOWNLOAD_BASE_PATH = Environment
            .getExternalStorageDirectory().toString()
            + "/android/data/"
            + SportsApp.getContext().getPackageName() + "/cache/";
    public static String DOWNLOAD_SAVE_PATH = DOWNLOAD_BASE_PATH + ".download/";
    public static String DOWNLOAD_PHOTOFRAMES_PATH = DOWNLOAD_BASE_PATH
            + ".photoframes/";
    public static String RECYCLE_PATH = DOWNLOAD_BASE_PATH + ".recycle/";
    public static final int NO_STORAGE_ERROR = -1;
    public static final int CANNOT_STAT_ERROR = -2;
    public static int IMAGE_DEFAULT_WIDTH = 640;
    public static int IMAGE_DEFAULT_HEIGHT = 480;
    public static String mPictureFormat = null;
    public static int IMAGE_SPACE = 60;
    private static CompressFormat mOutputFormat = Bitmap.CompressFormat.PNG;
    private static final String CAMERA_MANAGER_BUCKET_NAME = Environment
            .getExternalStorageDirectory().toString();
    public static final String IMAGE_SAVE_PATH = CAMERA_MANAGER_BUCKET_NAME
            + "/DCIM/Camera";
    private static final Uri STORAGE_URI = Images.Media.EXTERNAL_CONTENT_URI;
    public static final String PIXEL_FORMAT_JPEG = "jpeg";
    public static final String PIXEL_FORMAT_PNG = "png";
    public static final int EXIT_OK = 2;
    public static final int BACK_TO_CAMEA = 5;
    public static Uri mCurrContentUri;

    public static final boolean RECYCLE_INPUT = true;
    public static final boolean NO_RECYCLE_INPUT = false;
    public static final int INCLUDE_IMAGES = (1 << 0);
    public static final int INCLUDE_DRM_IMAGES = (1 << 1);

    public static final int IO_BUFFER_SIZE = 8 * 1024;

    public static String getLocalFilePath(String url, String fileName) {
        if (fileName == null) {
            fileName = getUrlFileName(url);
        }
        return DOWNLOAD_SAVE_PATH + fileName;
    }

    public static String getUrlFileName(String url) {
        int l = url.lastIndexOf("/") + 1;
        int r = url.lastIndexOf(".");
        return url.substring(l, r);
    }


    public static Bitmap loadBitmap(String url, String fileName) {
        Bitmap bitmap = null;
        if (fileName == null) {
            //fileName = getUrlFileName(url);
            long time=System.currentTimeMillis();
            fileName =time+"";

        }
        Log.d(TAG, "SSS detail imageurl" + url);

        File dir = new File(DOWNLOAD_SAVE_PATH);
        if (!dir.exists()) {
            Log.d(TAG, "dir is not exist");
            dir.mkdirs();
        }
        String filePath = DOWNLOAD_SAVE_PATH + fileName;
        File file = new File(filePath);
        if (file.exists() && !file.isDirectory() && file.length() > 0) {
            Log.d(TAG, "file exist in local cache: filePath is " + filePath);
            bitmap = SportsUtilities.decodeFile(filePath);
        }
        if (bitmap != null && bitmap.getHeight() > 0 && bitmap.getWidth() > 0) {
            return bitmap;
        }
        bitmap = loadBitmapFromUrl(url, fileName);
        return bitmap;
    }

    private static Bitmap loadBitmapFromFile(String fileName) {
        Bitmap bitmap = null;
        String filePath = DOWNLOAD_SAVE_PATH + fileName;
        File file = new File(filePath);
        if (file.exists()) {
            Log.d(TAG, "file exist in local cache");
            bitmap = SportsUtilities.decodeFile(filePath);
        }
        return bitmap;
    }

    private static Bitmap loadBitmapFromUrl(String previewUrl, String fileName) {

        HttpURLConnection urlConnection = null;
        BufferedOutputStream out = null;
        Bitmap bitmap = null;
        try {
            final URL imageUrl = new URL(previewUrl);
            urlConnection = (HttpURLConnection) imageUrl.openConnection();
            final InputStream in = new BufferedInputStream(
                    urlConnection.getInputStream(),
                    SportsUtilities.IO_BUFFER_SIZE);
            out = new BufferedOutputStream(new FileOutputStream(
                    getFromFileCache(fileName)), SportsUtilities.IO_BUFFER_SIZE);

            int b;
            while ((b = in.read()) != -1) {
                out.write(b);

            }
            out.flush();
            Log.d(TAG, "download log:load image from net");

            String path = DOWNLOAD_SAVE_PATH + fileName;
            Log.v(TAG, "path is " + path);
            bitmap = decodeFile(path);

        } catch (final IOException e) {
            Log.e(TAG, "Error in downloadBitmap - " + e + "," + fileName);
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (out != null) {
                try {
                    out.close();
                } catch (final IOException e) {
                    Log.e(TAG, "Error in downloadBitmap - " + e);
                }
            }
        }
        return bitmap;
    }

    /**
     * @param url
     * @param bitmap
     */
    public static void addToFileCache(String fileName, InputStream inputStream) {
        OutputStream outputStream = null;
        File dir = new File(DOWNLOAD_BASE_PATH);
        if (!dir.exists()) {
            dir.mkdir();
        }
        dir = new File(DOWNLOAD_SAVE_PATH);
        if (!dir.exists()) {
            dir.mkdir();
        }
        File file = getFromFileCache(fileName);
        // if(file.exists()) {
        // return ;
        // }
        Log.d(TAG, "dir path:" + dir.getAbsolutePath());

        try {
            // File file = new File(DOWNLOAD_SAVE_PATH,fileName);
            outputStream = new FileOutputStream(file);
            copyStream(inputStream, outputStream);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }

    public static File getFromFileCache(String fileName) {
        // String fileName = urlToFileName(url);
        File file = new File(DOWNLOAD_SAVE_PATH, fileName);
        Log.d(TAG, "file PATH:" + file.getPath());
        return file;
    }

    private static String urlToFileName(String url) {
        String urlName = url;
        if (urlName != null && urlName.length() != 0) {
            urlName = urlName.substring(urlName.lastIndexOf("/") + 1);
        }
        return urlName;
    }

    private static void copyStream(InputStream is, OutputStream os) {
        final int buffer_size = 1024;
        try {
            byte[] bytes = new byte[buffer_size];
            for (; ; ) {
                int count = is.read(bytes, 0, buffer_size);
                if (count == -1)
                    break;
                os.write(bytes, 0, count);
            }
            Log.d(TAG, "save file to sdcard successfully");
        } catch (Exception ex) {
        }
    }

    // private static Bitmap loadBitmapFromUrl(String previewUrl){
    // Bitmap bitmap = null;
    // InputStream is =null;
    // HttpURLConnection conn = null;
    // try {
    // URL url = new URL(previewUrl.trim());
    // conn = (HttpURLConnection) url.openConnection();
    // conn.setDoInput(true);
    // // conn.setDoOutput(true);
    // conn.setRequestMethod("GET");
    // conn.setConnectTimeout(180000);
    // is = conn.getInputStream();
    // if (conn.getResponseCode() == 200&&is!=null) {
    // BitmapFactory.Options options = new BitmapFactory.Options();
    // options.inJustDecodeBounds = true;
    // bitmap=BitmapFactory.decodeStream(new
    // FlushedInputStream(is),null,options);
    // if (options.outHeight <= 0 || options.outWidth <= 0) {
    // return null;
    // }
    // options.inScaled = true;
    // options.outHeight = 152;
    // options.outWidth = 172;
    // options.inJustDecodeBounds = false;
    // bitmap=BitmapFactory.decodeStream(new FlushedInputStream(is), null,
    // options);
    // } else
    // System.out.println("getImageFromURL error");
    //
    // } catch (MalformedURLException e) {
    // e.printStackTrace();
    // } catch (IOException e) {
    // e.printStackTrace();
    // } finally {
    // if (conn != null)
    // conn.disconnect();
    // try {
    // if (is != null)
    // is.close();
    // } catch (IOException e) {
    // e.printStackTrace();
    // }
    // }
    // // byte[] imageByte = getImageFromURL(url.trim());
    // // if (imageByte == null) {
    // // return null;
    // // }
    // //
    // // bitmap = BitmapFactory.decodeByteArray(imageByte, 0,
    // imageByte.length);
    // return bitmap;
    // }
    /*
     * An InputStream that skips the exact number of bytes provided, unless it
	 * reaches EOF.
	 */
    static class FlushedInputStream extends FilterInputStream {
        public FlushedInputStream(InputStream inputStream) {
            super(inputStream);
        }

        @Override
        public long skip(long n) throws IOException {
            long totalBytesSkipped = 0L;
            while (totalBytesSkipped < n) {
                long bytesSkipped = in.skip(n - totalBytesSkipped);
                if (bytesSkipped == 0L) {
                    int b = read();
                    if (b < 0) {
                        break; // we reached EOF
                    } else {
                        bytesSkipped = 1; // we read one byte
                    }
                }
                totalBytesSkipped += bytesSkipped;
            }
            return totalBytesSkipped;
        }
    } // end of FlushedInputStream

    private static byte[] getImageFromURL(String urlPath) {
        byte[] data = null;
        InputStream is = null;
        HttpURLConnection conn = null;
        try {
            URL url = new URL(urlPath);
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoInput(true);
            // conn.setDoOutput(true);
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(6000);
            is = conn.getInputStream();
            if (conn.getResponseCode() == 200) {
                data = readInputStream(is);
            } else
                System.out.println("getImageFromURL error");

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (conn != null)
                conn.disconnect();
            try {
                if (is != null)
                    is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return data;
    }

    private static byte[] readInputStream(InputStream is) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length = -1;
        try {
            while ((length = is.read(buffer)) != -1) {
                baos.write(buffer, 0, length);
            }
            baos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] data = baos.toByteArray();
        try {
            is.close();
            baos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }

    private static void saveBitmap(String fileName, Bitmap bitmap) {
        if (bitmap == null || bitmap.getWidth() <= 0 || bitmap.getHeight() <= 0) {
            return;
        }
        String filePath = DOWNLOAD_SAVE_PATH + fileName;
        File dir = new File(DOWNLOAD_BASE_PATH);
        if (!dir.exists()) {
            dir.mkdir();
        }
        dir = new File(DOWNLOAD_SAVE_PATH);
        if (!dir.exists()) {
            dir.mkdir();
        }
        File file = new File(filePath);
        if (file.exists()) {
            return;
        }
        try {
            FileOutputStream out = new FileOutputStream(file);
            if (bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)) {
                out.flush();
                out.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static int calculatePicturesRemaining() {
        try {
            if (!hasStorage()) {
                return NO_STORAGE_ERROR;
            } else {
                String storageDirectory = Environment
                        .getExternalStorageDirectory().toString();
                StatFs stat = new StatFs(storageDirectory);
                final int PICTURE_BYTES = 1500000;
                float remaining = ((float) stat.getAvailableBlocks() * (float) stat
                        .getBlockSize()) / PICTURE_BYTES;
                return (int) remaining;
            }
        } catch (Exception ex) {
            // if we can't stat the filesystem then we don't know how many
            // pictures are remaining. it might be zero but just leave it
            // blank since we really don't know.
            Log.e(TAG, "Fail to access sdcard", ex);
            return CANNOT_STAT_ERROR;
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
                Log.d(TAG, "SDCard writable?:" + writable);
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

    private static boolean checkFsWritable() {
        // Create a temporary file to see whether a volume is really writeable.
        // It's important not to put it in the root directory which may have a
        // limit on the number of files.
        String directoryName = Environment.getExternalStorageDirectory()
                .toString() + "/DCIM";
        File directory = new File(directoryName);
        if (!directory.isDirectory()) {
            if (!directory.mkdirs()) {
                return false;
            }
        }
        File f = new File(directoryName, ".probe");
        try {
            // Remove stale file if any
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

    /**
     * rotate
     *
     * @param bmp    source bitmap
     * @param degree rotate angle，negative value--contrarotate， positive
     *               value--clockwise rotate
     */
    public static Bitmap rotateBitmap(Bitmap bmp, float degree) {
        if (bmp == null)
            return null;
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        return Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(),
                matrix, true);
    }

    public static Bitmap byte2Bitmap(byte[] buffer, boolean crop) {
        if (!crop)
            return BitmapFactory.decodeByteArray(buffer, 0, buffer.length);
        else {
            int space = IMAGE_SPACE;
            Rect rect = new Rect();
            Bitmap retVal = BitmapFactory.decodeByteArray(buffer, 0,
                    buffer.length);
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
            Bitmap croppedImage = Bitmap.createBitmap(width, height,
                    Bitmap.Config.ARGB_8888);
            {
                Canvas canvas = new Canvas(croppedImage);
                Rect dstRect = new Rect(0, 0, width, height);
                canvas.drawBitmap(retVal, rect, dstRect, null);
            }
            return croppedImage;
        }
    }

    public static Uri storeUriImage(Context context, byte[] data,
                                    Bitmap bitmap, boolean dataBmp, String mPictureFormat) {
        String ext = null;
        String store_location = null;
        String filename = null;
        String title = null;
        long dateTaken = 0;
        String mFilePath = null;
        int[] degree = new int[1];
        ContentResolver cr = context.getContentResolver();

        try {
            mOutputFormat = Bitmap.CompressFormat.JPEG;
            ext = ".jpg";
            store_location = IMAGE_SAVE_PATH;
            dateTaken = System.currentTimeMillis();
            Date date = new Date(dateTaken);
            SimpleDateFormat dateFormat = new SimpleDateFormat(
                    context.getString(R.string.image_file_name_format));
            title = dateFormat.format(date);
            // store_location=CAMERA_MANAGER_BUCKET_NAME+"/DCIM/Camera";
            // store_location=CAMERA_MANAGER_BUCKET_NAME + "/MTZS/.temp";

            Log.d(TAG, "camera save path:" + store_location);

            filename = title + ext;
            mFilePath = store_location + "/" + filename;
            OutputStream outputStream = null;
            try {
                File dir = new File(store_location);
                if (!dir.exists())
                    dir.mkdirs();
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
        // context.sendBroadcast(new Intent(
        // "com.android.camera.NEW_PICTURE", mCurrContentUri));
        // Log.d(TAG,"storage update broadcast sent");
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

    public static void showFatalErrorAndFinish(final Activity activity,
                                               String title, String message) {
        DialogInterface.OnClickListener buttonListener = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                activity.finish();
            }
        };
        new AlertDialog.Builder(activity).setCancelable(false)
                .setIcon(android.R.drawable.ic_dialog_alert).setTitle(title)
                .setMessage(message)
                .setNeutralButton(R.string.details_ok, buttonListener).show();
    }

    public static Bitmap transform(Matrix scaler, Bitmap source,
                                   int targetWidth, int targetHeight, boolean scaleUp, boolean recycle) {
        int deltaX = source.getWidth() - targetWidth;
        int deltaY = source.getHeight() - targetHeight;
        if (!scaleUp && (deltaX < 0 || deltaY < 0)) {
			/*
			 * In this case the bitmap is smaller, at least in one dimension,
			 * than the target. Transform it by placing as much of the image as
			 * possible into the target and leaving the top/bottom or left/right
			 * (or both) black.
			 */
            Bitmap b2 = Bitmap.createBitmap(targetWidth, targetHeight,
                    Bitmap.Config.ARGB_8888);
            Canvas c = new Canvas(b2);

            int deltaXHalf = Math.max(0, deltaX / 2);
            int deltaYHalf = Math.max(0, deltaY / 2);
            Rect src = new Rect(deltaXHalf, deltaYHalf, deltaXHalf
                    + Math.min(targetWidth, source.getWidth()), deltaYHalf
                    + Math.min(targetHeight, source.getHeight()));
            int dstX = (targetWidth - src.width()) / 2;
            int dstY = (targetHeight - src.height()) / 2;
            Rect dst = new Rect(dstX, dstY, targetWidth - dstX, targetHeight
                    - dstY);
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
            b1 = Bitmap.createBitmap(source, 0, 0, source.getWidth(),
                    source.getHeight(), scaler, true);
        } else {
            b1 = source;
        }

        if (recycle && b1 != source) {
            source.recycle();
        }

        int dx1 = Math.max(0, b1.getWidth() - targetWidth);
        int dy1 = Math.max(0, b1.getHeight() - targetHeight);

        Bitmap b2 = Bitmap.createBitmap(b1, dx1 / 2, dy1 / 2, targetWidth,
                targetHeight);

        if (b2 != b1) {
            if (recycle || b1 != source) {
                b1.recycle();
            }
        }

        return b2;
    }

    public static <T> T checkNotNull(T object) {
        if (object == null)
            throw new NullPointerException();
        return object;
    }

    public static boolean equals(Object a, Object b) {
        return (a == b) || (a == null ? false : a.equals(b));
    }

    public static Bitmap getBitmapByName(String fileName) {
        Bitmap b = null;
        FlushedInputStream fis = null;
        BitmapFactory.Options opt = new BitmapFactory.Options();
        int scale = 1;
        if (opt.outWidth > 640 || opt.outHeight > 480) {
            scale = (int) Math.pow(
                    2.0,
                    (int) Math.round(Math.log(640 / (double) Math.max(
                            opt.outHeight, opt.outWidth)) / Math.log(0.5)));
            // scale = 2;
        }
        opt.inSampleSize = scale;
        try {
            Log.d("utility", "get pic path:" + DOWNLOAD_SAVE_PATH + fileName);
            // fis = new FileInputStream(DOWNLOAD_SAVE_PATH+fileName);
            fis = new FlushedInputStream(new BufferedInputStream(
                    new FileInputStream(DOWNLOAD_SAVE_PATH + fileName),
                    IO_BUFFER_SIZE));
            b = BitmapFactory.decodeStream(fis, null, opt);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

        }
        return b;
    }

    public static Bitmap getBitmapByName(String fileName, int w, int h) {
        try {
            FileInputStream fis = new FileInputStream((fileName));
            BitmapFactory.Options opt = new BitmapFactory.Options();
            opt.inJustDecodeBounds = true;
            Log.d(TAG, "opt.outWidth:" + opt.outWidth);
            Log.d(TAG, "opt.outHeight:" + opt.outHeight);
            BitmapFactory
                    .decodeStream(new FileInputStream(fileName), null, opt);
            Log.d(TAG, "opt.outWidth:" + opt.outWidth);
            Log.d(TAG, "opt.outHeight:" + opt.outHeight);
            Log.e(TAG, "w:" + w + " h:" + h);
            int viewLong = w > h ? w : h;
            int bmpLong = opt.outWidth > opt.outHeight ? opt.outWidth
                    : opt.outHeight;

            int scale = (int) Math.pow(2.0, bmpLong / viewLong);
            if (scale < 1) {
                scale = 1;
                Log.e(TAG, "scale:" + scale);
            }
            Log.e(TAG, "scale:" + scale);
            opt.inSampleSize = scale;
            opt.inJustDecodeBounds = false;

            Bitmap b = BitmapFactory.decodeStream(fis, null, opt);

            fis.close();

            return b;
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }

    public static void transPic(String path, String temp) {
        File oldImg = new File(path);
        File newImg = new File(temp);

        try {
            Bitmap bmp = BitmapFactory
                    .decodeStream(new FileInputStream(oldImg));
            FileOutputStream fos = new FileOutputStream(newImg);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
            bmp.recycle();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void checkSize(String picAddr) {
        File file = new File(picAddr);
        long range = 900 * 1024;
        if (file.length() > range) {
            String tempPath = picAddr.replace(
                    picAddr.substring(0, picAddr.lastIndexOf("/") + 1),
                    SportsUtilities.DOWNLOAD_SAVE_PATH);
            SportsUtilities.resizePic(picAddr, tempPath);
            picAddr = tempPath;
        }
    }

    public static void resizePic(String path, String temp) {
        File oldImg = new File(path);
        File newImg = new File(temp);
        try {
            BitmapFactory.Options opt = new BitmapFactory.Options();
            opt.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(oldImg), null, opt);
            int w = opt.outWidth;
            int h = opt.outHeight;
            Log.e(TAG, "w:" + w + " h:" + h);
            int scale = (int) Math.pow(2.0, Math.max(w, h) / 1024);
            Log.e(TAG, "scale:" + scale);
            opt.inSampleSize = scale;
            opt.inJustDecodeBounds = false;

            Bitmap bmp = BitmapFactory.decodeStream(
                    new FileInputStream(oldImg), null, opt);
            FileOutputStream fos = new FileOutputStream(newImg);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
            bmp.recycle();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static String getResizePic(String picAddr) {
        String tempPath = picAddr.replace(
                picAddr.substring(0, picAddr.lastIndexOf("/") + 1),
                SportsUtilities.DOWNLOAD_SAVE_PATH);
        File oldImg = new File(picAddr);
        File newImg = new File(tempPath);
        try {
            BitmapFactory.Options opt = new BitmapFactory.Options();
            opt.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(oldImg), null, opt);
            int w = opt.outWidth;
            int h = opt.outHeight;
            Log.e(TAG, "w:" + w + " h:" + h);
            int scale = (int) Math.pow(2.0, Math.max(w, h) / 900);
            Log.e(TAG, "scale:" + scale);
            opt.inSampleSize = scale;
            opt.inTempStorage = new byte[4 * 1024];
            opt.inJustDecodeBounds = false;
            opt.inInputShareable = true;
            opt.inPurgeable = true;

            Bitmap bmp = BitmapFactory.decodeStream(
                    new FileInputStream(oldImg), null, opt);
            FileOutputStream fos = new FileOutputStream(newImg);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
            bmp.recycle();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tempPath;
    }

    public static Bitmap decodeFile(String path) {
        int scale = 1;
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, o);
        if (o.outWidth > IMAGE_DEFAULT_WIDTH
                || o.outHeight > IMAGE_DEFAULT_HEIGHT) {
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
        o.inInputShareable = true;
        o.inPurgeable = true;
        o.inPreferredConfig = Bitmap.Config.RGB_565;
        File file = new File(path);
        FlushedInputStream is = null;
        try {
            is = new FlushedInputStream(new BufferedInputStream(
                    new FileInputStream(file), IO_BUFFER_SIZE));
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Bitmap bmp = BitmapFactory.decodeStream(is, null, o);

        return bmp;
    }

    public static Bitmap loadMainBitmap(String url, String fileName) {
        if (fileName == null) {
            fileName = getUrlFileName(url);
        }
        Bitmap bitmap = loadBitmapFromFile(fileName);
        if (bitmap != null && bitmap.getHeight() > 0 && bitmap.getWidth() > 0) {
            return bitmap;
        }
        bitmap = loadBitmapFromUrl(url);
        saveBitmap(fileName, bitmap);
        return bitmap;
    }

    private static Bitmap loadBitmapFromUrl(String url) {
        Bitmap bitmap = null;
        byte[] imageByte = getImageFromURL(url.trim());
        if (imageByte == null) {
            return null;
        }

        bitmap = BitmapFactory.decodeByteArray(imageByte, 0, imageByte.length);
        return bitmap;
    }

    public static final String[] constellationArr = {"水瓶座", "双鱼座", "白羊座",
            "金牛座", "双子座", "巨蟹座", "狮子座", "处女座", "天秤座", "天蝎座", "射手座", "魔羯座"};

    public static final int[] constellationEdgeDay = {20, 19, 21, 21, 21, 22,
            23, 23, 23, 23, 22, 22};

    /**
     * 根据日期获取生肖
     *
     * @return
     */

    /**
     * 根据日期获取星座
     *
     * @param time
     * @return
     */
    // public String date2Constellation(Calendar time) {
    // int month = time.get(Calendar.MONTH);
    // int day = time.get(Calendar.DAY_OF_MONTH);
    // if (day < constellationEdgeDay[month]) {
    // month = month - 1;
    // }
    // if (month >= 0) {
    // return constellationArr[month];
    // }
    // //default to return 魔羯
    // return constellationArr[11];
    // }

    // public static long stringDateToMillions(String date){
    // if(date==null)
    // return null;
    // SimpleDateFormat sDateFormat=new SimpleDateFormat("yyyy-MM-dd");
    // Date formateDate = null;
    // try {
    // formateDate = sDateFormat.parse(date);
    //
    // } catch (ParseException e) {
    // // TODO Auto-generated catch block
    // e.printStackTrace();
    // }
    // }
    @SuppressWarnings("deprecation")
    public static String date2Constellation(String dateString) {
        long timeInMillions = stringDateToMillions(dateString);
        if (timeInMillions <= 0)
            return null;
        Date formateDate = new Date(stringDateToMillions(dateString));

        int month = formateDate.getMonth();
        int day = formateDate.getDay();
        if (day < constellationEdgeDay[month]) {
            month = month - 1;
        }
        if (month >= 0) {
            return constellationArr[month];
        }
        // default to return 魔羯
        return constellationArr[11];

    }

    public static String millionsToStringDate(long dateInMillions) {
        if (dateInMillions == 0)
            return null;
        SimpleDateFormat sDateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss");
        Date formateDate = new Date(dateInMillions * 1000);
        String formatedStringDateString = null;
        formatedStringDateString = sDateFormat.format(formateDate);
        Log.d(TAG, "formateDate:" + formatedStringDateString);
        return formatedStringDateString;
    }

    public static long stringDateToMillions(String date) {
        if (date == null)
            return 0;
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date formateDate = null;
        try {
            formateDate = sDateFormat.parse(date);
            Log.d(TAG, "formateDate:" + formateDate.toString());

        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return formateDate.getTime();
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

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        Bitmap bm = null;
        InputStream stream = null;
        try {
            stream = new FileInputStream(filename);
            bm = BitmapFactory.decodeStream(stream, null, options);
            if (bm == null) {
                // retry Once More for skia decode failed case
                Log.d(TAG,
                        "TODO retry decode Once More for skia decode failed case.");
                if (stream != null) {
                    stream.close();
                }
                stream = new FileInputStream(filename);
                bm = BitmapFactory.decodeStream(stream, null, options);
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
        Display localDisplay = ((Activity) context).getWindowManager()
                .getDefaultDisplay();
        DisplayMetrics localDisplayMetrics = new DisplayMetrics();
        localDisplay.getMetrics(localDisplayMetrics);
        int screenHeight = localDisplay.getHeight();
        int screenWidth = localDisplay.getWidth();
        return pixel * screenWidth / 480;
    }

    /**
     * 释放Imageview占用的图片资源 用于退出时释放资源，调用完成后，请不要刷新界面
     *
     * @param layout 需要释放图片的布局 *
     */
    public static void recycleView(ViewGroup layout, boolean recycleLayoutRes) {

        if (layout != null) {
            for (int i = 0; i < layout.getChildCount(); i++) {
                // 获得该布局的所有子布局
                View subView = layout.getChildAt(i);
                // 判断子布局属性，如果还是ViewGroup类型，递归回收
                if (subView instanceof ViewGroup) {
                    // 递归回收
                    recycleView((ViewGroup) subView, recycleLayoutRes);
                } else {
                    // 是Imageview的子例
                    if (subView instanceof ImageView) {
                        // 回收占用的Bitmap
                        recycleImageViewBitMap((ImageView) subView);
                        // 如果flagWithBackgroud为true,则同时回收背景图
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

    public static void recycleImageViewBitMap(ImageView imageView) {
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

//	private static void unbindDrawables(View view) {
//		if (view.getBackground() != null) {
//			view.getBackground().setCallback(null);
//		}
//		if (view instanceof ViewGroup && !(view instanceof AdapterView)) {
//			for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
//				unbindDrawables(((ViewGroup) view).getChildAt(i));
//			}
//			((ViewGroup) view).removeAllViews();
//		}
//	}

    /**
     * Simple network connection check.
     *
     * @param context
     */
    public static boolean checkConnection(Context context) {
        final ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo == null || !networkInfo.isConnectedOrConnecting()) {

            Log.e(TAG, "checkConnection - no connection found");
            return false;
        }
        return true;
    }

    public static void setAge(Context context, String birthday, TextView txt) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy");
        SimpleDateFormat formatParse = new SimpleDateFormat("yyyy-MM-dd");
        int birthYear;
        try {
            birthYear = Integer.valueOf(format.format(formatParse
                    .parse(birthday)));
            int age = Integer
                    .valueOf(format.format(System.currentTimeMillis()))
                    - birthYear;
            if (age <= 0 || age >= 100) {
                age = 0;
                txt.setText(R.string.sports_age_unkonw);
            } else {
                txt.setText("" + age + context.getString(R.string.tx_years_old_text));
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public static int getStateImgIds(int type) {
        switch (type) {
            default:
            case 0:
                return R.drawable.othersports_type_walking;
            case 1:
                return R.drawable.othersports_type_running;
            case 2:
                return R.drawable.othersports_type_ziyouyong;
            case 3:
                return R.drawable.othersports_type_dieyong;
            case 4:
                return R.drawable.othersports_type_wayong;
            case 5:
                return R.drawable.othersports_type_yangyong;
            case 6:
                return R.drawable.othersports_type_climbing;
            case 7:
                return R.drawable.othersports_type_golf;
            case 8:
                return R.drawable.othersports_type_jingzou;
            case 9:
                return R.drawable.othersports_type_qixing;
            case 10:
                return R.drawable.othersports_type_wangqiu;
            case 11:
                return R.drawable.othersports_type_yumaoqiu;
            case 12:
                return R.drawable.othersports_type_zuqiu;
            case 13:
                return R.drawable.othersports_type_pingpangqiu;
            case 14:
                return R.drawable.othersports_type_huachuan;
            case 15:
                return R.drawable.othersports_type_liubing;
            case 16:
                return R.drawable.othersports_type_lunhua;
        }

    }

    public static int getStateImgIdsMysports(int type, int detailId) {
        switch (type) {
            default:
            case 0:
                return R.drawable.mysports_walk;
            case 1:
                return R.drawable.mysports_running;
            case 2: {
                if (detailId == 0)
                    return R.drawable.mysports_ziyouyong;
                else if (detailId == 1)
                    return R.drawable.mysports_dieyong;
                else if (detailId == 2)
                    return R.drawable.mysports_wayong;
                else if (detailId == 3)
                    return R.drawable.mysports_yangyong;
            }
            case 3:
                return R.drawable.mysports_climbing;
            case 4:
                return R.drawable.mysports_golf;
            case 5:
                return R.drawable.mysports_walk_race;
            case 6:
                return R.drawable.mysports_cycling;
            case 7:
                return R.drawable.mysports_tennis;
            case 8:
                return R.drawable.mysports_badminton;
            case 9:
                return R.drawable.mysports_football;
            case 10:
                return R.drawable.mysports_table_tennis;
            case 11:
                return R.drawable.mysports_rowing;
            case 12:
                return R.drawable.mysports_skating;
            case 13:
                return R.drawable.mysports_roller_skating;
        }

    }

    public static int getStateImgIdsTitle(int type) {
        switch (type) {
            default:
            case 0:
                return R.drawable.title_walk;
            case 1:
                return R.drawable.title_run;
            case 2:
                return R.drawable.title_swim;
            case 3:
                return R.drawable.title_dieyong;
            case 4:
                return R.drawable.title_wayong;
            case 5:
                return R.drawable.title_yangyong;
            case 6:
                return R.drawable.title_climb;
            case 7:
                return R.drawable.title_golf;
            case 8:
                return R.drawable.title_walk_race;
            case 9:
                return R.drawable.title_cycling;
            case 10:
                return R.drawable.title_tennis;
            case 11:
                return R.drawable.title_badminton;
            case 12:
                return R.drawable.title_football;
            case 13:
                return R.drawable.title_table_tennis;
            case 14:
                return R.drawable.title_rowing;
            case 15:
                return R.drawable.title_skating;
            case 16:
                return R.drawable.title_roller_skating;
        }

    }


    public static int getNewStateImgIdsTitle(int type) {
        switch (type) {
            default:
            case 0:
                return R.drawable.title_walk;
            case 1:
                return R.drawable.title_run;
            case 2:
                return R.drawable.title_cycling;
            case 3:
                return R.drawable.title_climb;
            case 4:
                return R.drawable.title_tennis;
            case 5:
                return R.drawable.title_badminton;
            case 6:
                return R.drawable.title_golf;
        }

    }

    public static int getStateImgIdsTitle(int typeId, int detailId) {
        switch (typeId) {
            default:
            case 0:
                return R.drawable.title_walk;
            case 1:
                return R.drawable.title_run;
            case 2: {
                if (detailId == 0)
                    return R.drawable.title_swim;
                else if (detailId == 1)
                    return R.drawable.title_dieyong;
                else if (detailId == 2)
                    return R.drawable.title_wayong;
                else if (detailId == 3)
                    return R.drawable.title_yangyong;
                break;
            }
            case 3:
                return R.drawable.title_climb;
            case 4:
                return R.drawable.title_golf;
            case 5:
                return R.drawable.title_walk_race;
            case 6:
                return R.drawable.title_cycling;
            case 7:
                return R.drawable.title_tennis;
            case 8:
                return R.drawable.title_badminton;
            case 9:
                return R.drawable.title_football;
            case 10:
                return R.drawable.title_table_tennis;
            case 11:
                return R.drawable.title_rowing;
            case 12:
                return R.drawable.title_skating;
            case 13:
                return R.drawable.title_roller_skating;
        }
        return R.drawable.title_walk;

    }

//	public static int getStateImgIdsFocus(int type) {
//		switch (type) {
//		default:
//		case 0:
//			return R.drawable.walk_focus;
//		case 1:
//			return R.drawable.run_focus;
//		case 2:
//			return R.drawable.swim_free_focus;
//		case 3:
//			return R.drawable.swim_fly_focus;
//		case 4:
//			return R.drawable.swim_frog_focus;
//		case 5:
//			return R.drawable.swim_face_focus;
//		case 6:
//			return R.drawable.mountain_focus;
//		case 7:
//			return R.drawable.golf_focus;
//		case 8:
//			return R.drawable.walk_race_focus;
//		case 9:
//			return R.drawable.cycling_focus;
//		case 10:
//			return R.drawable.tennis_focus;
//		case 11:
//			return R.drawable.badminton_focus;
//		case 12:
//			return R.drawable.football_focus;
//		case 13:
//			return R.drawable.table_tennis_focus;
//		case 14:
//			return R.drawable.rowing_focus;
//		case 15:
//			return R.drawable.skating_focus;
//		case 16:
//			return R.drawable.roller_skating_focus;
//		}
//
//	}

//	public static int getTypeMysports(int type) {
//		switch (type) {
//		default:
//		case 0:
//			return R.drawable.mysports_sportstype;
//		case 1:
//			return R.drawable.mysports_sportstype_running;
//		case 2:
//			return R.drawable.mysports_sportstype_ziyouyong;
//		case 3:
//			return R.drawable.mysports_sportstype_dieyong;
//		case 4:
//			return R.drawable.mysports_sportstype_wayong;
//		case 5:
//			return R.drawable.mysports_sportstype_yangyong;
//		case 6:
//			return R.drawable.mysports_sportstype_climbing;
//		case 7:
//			return R.drawable.mysports_sportstype_golf;
//		case 8:
//			return R.drawable.mysports_sportstype_jingzou;
//		case 9:
//			return R.drawable.mysports_sportstype_qixing;
//		case 10:
//			return R.drawable.mysports_sportstype_wangqiu;
//		case 11:
//			return R.drawable.mysports_sportstype_yumaoqiu;
//		case 12:
//			return R.drawable.mysports_sportstype_zuqiu;
//		case 13:
//			return R.drawable.mysports_sportstype_pingpangqiu;
//		case 14:
//			return R.drawable.mysports_sportstype_huachuan;
//		case 15:
//			return R.drawable.mysports_sportstype_liubing;
//		case 16:
//			return R.drawable.mysports_sportstype_lunhua;
//		}
//
//	}
}
