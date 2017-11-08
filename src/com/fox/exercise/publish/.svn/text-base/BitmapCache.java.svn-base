package com.fox.exercise.publish;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.ref.SoftReference;
import java.util.HashMap;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;

public class BitmapCache extends Activity {

    public Handler h = new Handler();
    public final String TAG = getClass().getSimpleName();
    private HashMap<String, SoftReference<Bitmap>> imageCache = new HashMap<String, SoftReference<Bitmap>>();

    public void put(String path, Bitmap bmp) {
        if (!TextUtils.isEmpty(path) && bmp != null) {
            imageCache.put(path, new SoftReference<Bitmap>(bmp));
        }
    }

    public void displayBmp(final ImageView iv, final String thumbPath,
                           final String sourcePath, final ImageCallback callback) {
        if (TextUtils.isEmpty(thumbPath) && TextUtils.isEmpty(sourcePath)) {
            Log.e(TAG, "no paths pass in");
            return;
        }

        final String path;
        final boolean isThumbPath;
        if (!TextUtils.isEmpty(thumbPath)) {
            path = thumbPath;
            isThumbPath = true;
        } else if (!TextUtils.isEmpty(sourcePath)) {
            path = sourcePath;
            isThumbPath = false;
        } else {
            return;
        }

        if (imageCache.containsKey(path)) {
            SoftReference<Bitmap> reference = imageCache.get(path);
            Bitmap bmp = reference.get();
            if (bmp != null) {
                if (callback != null) {
                    callback.imageLoad(iv, bmp, sourcePath);
                }
                iv.setImageBitmap(bmp);
                Log.d(TAG, "hit cache");
                return;
            }
        }
        iv.setImageBitmap(null);

        new Thread() {
            Bitmap thumb;

            public void run() {

                try {
                    if (isThumbPath) {
                        thumb = BitmapFactory.decodeFile(thumbPath);
                        if (thumb == null) {
                            thumb = revitionImageSize(sourcePath);
                        }
                    } else {
                        thumb = revitionImageSize(sourcePath);
                    }
                } catch (Exception e) {

                }
                if (thumb == null) {
                    thumb = TestPicActivity.bimap;
                }

                int degree = getBitmapDegree(sourcePath);
                if (0 != degree) {
                    Log.v(TAG, "wmh degree is " + degree);
                    thumb = rotateBitmapByDegree(thumb, degree);
                }

                Log.e(TAG, "-------thumb------" + thumb);
                put(path, thumb);

                if (callback != null) {
                    h.post(new Runnable() {
                        @Override
                        public void run() {
                            callback.imageLoad(iv, thumb, sourcePath);
                        }
                    });
                }
            }
        }.start();

    }

    public Bitmap revitionImageSize(String path) throws IOException {
        BufferedInputStream in = new BufferedInputStream(new FileInputStream(
                new File(path)));
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(in, null, options);
        in.close();
        int i = 0;
        Bitmap bitmap = null;
        while (true) {
            if ((options.outWidth >> i <= 256)
                    && (options.outHeight >> i <= 256)) {
                in = new BufferedInputStream(
                        new FileInputStream(new File(path)));
                options.inSampleSize = (int) Math.pow(2.0D, i);
                options.inJustDecodeBounds = false;
                bitmap = BitmapFactory.decodeStream(in, null, options);
                break;
            }
            i += 1;
        }

        return bitmap;
    }

    public interface ImageCallback {
        public void imageLoad(ImageView imageView, Bitmap bitmap,
                              Object... params);
    }

    /**
     * ��ȡͼƬ����ת�ĽǶ�
     *
     * @param path ͼƬ����·��
     * @return ͼƬ����ת�Ƕ�
     */
    public static int getBitmapDegree(String path) {
        int degree = 0;
        try {
            // ��ָ��·���¶�ȡͼƬ������ȡ��EXIF��Ϣ
            ExifInterface exifInterface = new ExifInterface(path);
            // ��ȡͼƬ����ת��Ϣ
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
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
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /**
     * ��ͼƬ����ĳ���ǶȽ�����ת
     *
     * @param bm     ��Ҫ��ת��ͼƬ
     * @param degree ��ת�Ƕ�
     * @return ��ת���ͼƬ
     */
    public static Bitmap rotateBitmapByDegree(Bitmap bm, int degree) {
        Bitmap returnBm = null;

        // ������ת�Ƕȣ�������ת����
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        try {
            // ��ԭʼͼƬ������ת���������ת�����õ��µ�ͼƬ
            returnBm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
        } catch (OutOfMemoryError e) {
        }
        if (returnBm == null) {
            returnBm = bm;
        }
        if (bm != returnBm) {
            bm.recycle();
            bm = null;
        }
        return returnBm;
    }
}
