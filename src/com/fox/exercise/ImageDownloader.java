package com.fox.exercise;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

import android.R.integer;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import cn.ingenic.indroidsync.SportsApp;

public class ImageDownloader {

    private static final String TAG = "ImageDownloader";
    private static int IMAGE_DEFAULT_WIDTH = 480;
    private static int IMAGE_DEFAULT_HEIGHT = 640;

    public static final int DEFAULT = 0;
    public static final int ICON = 1;
    public static final int GIFT = 2;
    public static final int OnlyOne = 3;
    public static final int ACTIVITYICON = 4;
    private int type = 0;
    // private ImageCache imageCache;
    // ImageCache imageCache;
    FileCache fileCache;
    private WeakReference<Context> mAppContext;
    private static ImageDownloader mInstance;

    // private ProgressBar mProgressBar;
    public static ImageDownloader getInstance() {
        if (mInstance == null) {
            // TODO need synchronized the ImageDownloader?
            mInstance = new ImageDownloader(SportsApp.getContext());
        }
        return mInstance;

    }

    public ImageDownloader(Context context) {
        // imageCache = new ImageCache();
        fileCache = new FileCache(context);
    }

    public void setSize(int w, int h) {
        IMAGE_DEFAULT_WIDTH = w;
        IMAGE_DEFAULT_HEIGHT = h;
    }

    public void setType(int type) {
        this.type = type;
    }

    /**
     * Download the specified image from the Internet and binds it to the
     * provided ImageView. The binding is immediate if the image is found in the
     * cache and will be done asynchronously otherwise. A null bitmap will be
     * associated to the ImageView if an error occurs.
     *
     * @param url       The URL of the image to download.
     * @param imageView The ImageView to bind the downloaded image to.
     */
    public void download(String url, ImageView imageView,
                         ProgressBar progressBar) {
        // imageCache.resetPurgeTimer();
        // Bitmap bitmap = null;//imageCache.getBitmapFromCache(url);
        // if(bitmap == null) {
        // this.mProgressBar=progressBar;
        forceDownload(url, imageView, progressBar);
        // File file = fileCache.getFromFileCache(url);
        // if(file.exists() && !file.isDirectory() && file.length()>0){
        // try {
        // bitmap = BitmapFactory.decodeStream(new FlushedInputStream(new
        // FileInputStream(file)));
        // } catch (FileNotFoundException e1) {
        // bitmap = null;
        // }
        // }
        // if(bitmap!=null){
        // imageView.setImageBitmap(bitmap);
        // }else{
        // forceDownload(url, imageView);
        // }

        // } else {
        // cancelPotentialDownload(url, imageView);
        // imageView.setImageBitmap(bitmap);
        // }
    }

    /**
     * Same as download but the image is always downloaded and the cache is not
     * used. Kept private at the moment as its interest is not clear.
     */
    private void forceDownload(String url, ImageView imageView,
                               ProgressBar progressBar) {
        if (url == null) {
            Log.d(TAG, "url is null");
//			if (type == DEFAULT){
//				imageView.setBackgroundResource(R.drawable.sports_default_picture);
//			}
//			else if (type == ICON) {
//				// imageView.setImageResource(R.drawable.sports_user_edit_portrait);
//			} else if (type == GIFT){
//				imageView.setImageResource(R.drawable.sports_default_picture);
//			}
            return;
        }
        boolean canceled = cancelPotentialDownload(url, imageView);
        Log.d(TAG, "download log:task canceled:" + canceled);
        if (canceled) {
            BitmapDownloaderTask task = new BitmapDownloaderTask(imageView, progressBar);
            // task = new BitmapDownloaderTask(imageView);
            // imageView.setImageResource(R.drawable.downloadcenter_wallpaper_online_default_image);
            DownloadedDrawable downloadedDrawable = new DownloadedDrawable(task);
            // DownloadedDrawable downloadedDrawable = new
            // DownloadedDrawable(task,imageView);
            imageView.setImageDrawable(downloadedDrawable);
            downloadedDrawable = null;
//			if (type == DEFAULT)
//				imageView
//						.setBackgroundResource(R.drawable.sports_default_picture);
//			else if (type == ICON) {
//				// imageView.setImageResource(R.drawable.sports_user_edit_portrait);
//			} else if (type == GIFT)
//				imageView.setBackgroundResource(R.drawable.sports_default_picture);
            task.execute(url);
        }
    }

    /**
     * @param url
     * @param imageView
     * @return
     */
    private static boolean cancelPotentialDownload(String url,
                                                   ImageView imageView) {
        BitmapDownloaderTask bitmapDownloaderTask = getBitmapDownloaderTask(imageView);

        if (bitmapDownloaderTask != null) {
            String bitmapUrl = bitmapDownloaderTask.url;
            if ((bitmapUrl == null) || (!bitmapUrl.equals(url))) {
                bitmapDownloaderTask.cancel(true);
            } else {
                // The same URL is already being downloaded.
                return false;
            }
        }
        return true;
    }

    public Bitmap getLocalBitmap(String url) {
        Bitmap bitmap = null;
        // try to get image from file cache

        if (fileCache == null)
            return null;

        File file = fileCache.getFromFileCache(url);
        // Log.d(TAG,"file.exists()?"+(file.exists() && !file.isDirectory() &&
        // file.length()>0));
        if (file.exists() && !file.isDirectory() && file.length() > 0) {
            Log.d(TAG, "file.exists()?" + file.exists());
            bitmap = decodeFile(file.getPath());
            // BitmapFactory.Options opt =new BitmapFactory.Options();
            // int scale=2;
            // if (opt.outWidth > 640 || opt.outHeight > 480) {
            // scale = (int) Math.pow(
            // 2.0,
            // (int) Math.round(Math.log(640
            // / (double) Math.max(opt.outHeight, opt.outWidth))
            // / Math.log(0.5)));
            // // scale = 2;
            // }
            // opt.inSampleSize=scale;
            // try {
            // bitmap = BitmapFactory.decodeStream(new FlushedInputStream(new
            // FlushedInputStream(new FileInputStream(file))),null,opt);
            // } catch (FileNotFoundException e) {
            // // TODO Auto-generated catch block
            // e.printStackTrace();
            // }
        }

        if (bitmap != null) {
            Log.d(TAG, "download log:load image from local sdcard cache");
            return bitmap;
        }
        return null;
    }

    /**
     * @param url
     * @return
     */
    public Bitmap downloadBitmap(String url) {
        Bitmap bitmap = null;
        // try to get image from file cache

        if (fileCache == null)
            return null;

        File file = fileCache.getFromFileCache(url);
        // Log.d(TAG,"file.exists()?"+(file.exists() && !file.isDirectory() &&
        // file.length()>0));
        if (file.exists() && !file.isDirectory() && file.length() > 0) {
            Log.d(TAG, "file.exists()?" + file.exists());
            bitmap = decodeFile(file.getPath());
            // BitmapFactory.Options opt =new BitmapFactory.Options();
            // int scale=2;
            // if (opt.outWidth > 640 || opt.outHeight > 480) {
            // scale = (int) Math.pow(
            // 2.0,
            // (int) Math.round(Math.log(640
            // / (double) Math.max(opt.outHeight, opt.outWidth))
            // / Math.log(0.5)));
            // // scale = 2;
            // }
            // opt.inSampleSize=scale;
            // try {
            // bitmap = BitmapFactory.decodeStream(new FlushedInputStream(new
            // FlushedInputStream(new FileInputStream(file))),null,opt);
            // } catch (FileNotFoundException e) {
            // // TODO Auto-generated catch block
            // e.printStackTrace();
            // }
        }

        if (bitmap != null) {
            Log.d(TAG, "download log:load image from local sdcard cache");
            return bitmap;
        }
        // end of try
        HttpURLConnection urlConnection = null;
        BufferedOutputStream out = null;
        // HttpGet httpRequest = new HttpGet(url);
        // HttpClient httpclient = new DefaultHttpClient();
        // HttpResponse response;
        // HttpEntity entity;
        // BufferedHttpEntity bufferedHttpEntity;
        // InputStream is = bufferedHttpEntity.getContent();

        try {
            final URL imageUrl = new URL(url);
            urlConnection = (HttpURLConnection) imageUrl.openConnection();
            // response = (HttpResponse) httpclient.execute(httpRequest);
            // entity = response.getEntity();
            // bufferedHttpEntity = new BufferedHttpEntity(entity);
            final InputStream in = new BufferedInputStream(
                    urlConnection.getInputStream(),
                    SportsUtilities.IO_BUFFER_SIZE);
            out = new BufferedOutputStream(new FileOutputStream(file),
                    SportsUtilities.IO_BUFFER_SIZE);

            int b;
            while ((b = in.read()) != -1) {
                out.write(b);

            }
            in.close();
            out.flush();
            Log.d(TAG, "download log:load image from net");

            Log.v(TAG, "file is " + file.getPath());
            bitmap = decodeFile(file.getPath());
            // bitmap = BitmapFactory.decodeStream(new FlushedInputStream(new
            // FileInputStream(file)));
        } catch (final IOException e) {
            Log.e(TAG, "Error in downloadBitmap - " + e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            // httpRequest.abort();
            // httpclient=null;
            // response=null;
            // entity=null;
            // bufferedHttpEntity=null;
            if (out != null) {
                try {
                    out.close();
                } catch (final IOException e) {
                    Log.e(TAG, "Error in downloadBitmap - " + e);
                }
            }
        }
        return bitmap;
        // final HttpClient httpClient =
        // AndroidHttpClient.newInstance("image_downloader");
        //
        // // final HttpClient httpClient = new DefaultHttpClient();
        // final HttpGet getRequest = new HttpGet(url);
        //
        // try {
        // HttpResponse httpResponse = httpClient.execute(getRequest);
        // final int statusCode = httpResponse.getStatusLine().getStatusCode();
        // Log.d(TAG,"statusCode:"+statusCode);
        // if(statusCode != HttpStatus.SC_OK) {
        // Log.w(TAG, "Error " + statusCode + " while retrieving bitmap from " +
        // url);
        // return null;
        // }
        //
        // final HttpEntity httpEntity = httpResponse.getEntity();
        // if(httpEntity != null) {
        // InputStream inputStream = null;
        // try {
        // inputStream = httpEntity.getContent();
        //
        // //save file to file cache
        // fileCache.addToFileCache(url, inputStream);
        // //end of save
        // //TODO
        // Log.d(TAG,"download log:load iamge from net");
        // return BitmapFactory.decodeStream(new FlushedInputStream(new
        // FlushedInputStream(new FileInputStream(file))));
        // } catch (Exception e) {
        // e.printStackTrace();
        // }finally {
        // if(inputStream != null) {
        // inputStream.close();
        // }
        // httpEntity.consumeContent();
        // }
        // }
        // } catch (IOException e) {
        // getRequest.abort();
        // Log.w(TAG, "I/O error while retrieving bitmap from " + url, e);
        // } catch (IllegalStateException e) {
        // getRequest.abort();
        // Log.w(TAG, "Incorrect URL: " + url);
        // } catch (Exception e) {
        // getRequest.abort();
        // Log.w(TAG, "Error while retrieving bitmap from " + url, e);
        // } finally {
        // if ((httpClient instanceof AndroidHttpClient)) {
        // ((AndroidHttpClient) httpClient).close();
        // }
        // }
        // return null;
    } // end of downloadBitmap

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

    /**
     * @param imageView Any imageView
     * @return Retrieve the currently active download task (if any) associated
     * with this imageView. null if there is no such task.
     */
    private static BitmapDownloaderTask getBitmapDownloaderTask(
            ImageView imageView) {
        if (imageView != null) {
            Drawable drawable = imageView.getDrawable();
            if (drawable instanceof DownloadedDrawable) {
                DownloadedDrawable downloadedDrawable = (DownloadedDrawable) drawable;
                return downloadedDrawable.getBitmapDownloaderTask();
            }
        }
        return null;
    }

    public Bitmap decodeFile(String path) {

        int scale = 1;
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, o);

        //��ͼƬѹ��2��
//		if (o.outWidth > IMAGE_DEFAULT_WIDTH
//				|| o.outHeight > IMAGE_DEFAULT_HEIGHT) {
//			scale = (int) Math.pow(
//					2.0,
//					(int) Math.round(Math.log(IMAGE_DEFAULT_WIDTH
//							/ (double) Math.max(o.outHeight, o.outWidth))
//							/ Math.log(0.5)));
//			// scale = 2;
//		}
//		Log.d(TAG, scale + " scale");

        // 2. File -> Bitmap (Returning a smaller image)
        o.inJustDecodeBounds = false;
        o.inSampleSize = scale;
        // o.inInputShareable=true;
        // o.inPurgeable=true;
        o.inPreferredConfig = Bitmap.Config.RGB_565;
        File file = new File(path);
        InputStream is = null;
        Bitmap bmp = null;
        try {
            is = new FileInputStream(file);
            bmp = BitmapFactory.decodeStream(is, null, o);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    // do nothing here
                }
            }
        }

        return bmp;
    }

    // public Bitmap decodeRFile(int id) {
    //
    // int scale = 1;
    // BitmapFactory.Options o = new BitmapFactory.Options();
    // o.inJustDecodeBounds = true;
    // BitmapFactory.decodeFile(path, o);
    // if (o.outWidth > IMAGE_DEFAULT_WIDTH || o.outHeight >
    // IMAGE_DEFAULT_HEIGHT) {
    // scale = (int) Math.pow(
    // 2.0,
    // (int) Math.round(Math.log(IMAGE_DEFAULT_WIDTH
    // / (double) Math.max(o.outHeight, o.outWidth))
    // / Math.log(0.5)));
    // // scale = 2;
    // }
    // Log.d(TAG, scale + " scale");
    //
    // // 2. File -> Bitmap (Returning a smaller image)
    // o.inJustDecodeBounds = false;
    // o.inSampleSize = scale;
    // o.inInputShareable=true;
    // o.inPurgeable=true;
    // o.inPreferredConfig = Bitmap.Config.RGB_565;
    // File file=new File(path);

    // InputStream is =
    // SportsRanking.mContext.getResources().openRawResource(id);
    // try {
    // is = new FileInputStream(file);
    // } catch (FileNotFoundException e) {
    // // TODO Auto-generated catch block
    // e.printStackTrace();
    // }
    // Bitmap bmp=BitmapFactory.decodeStream(is, null, o);
    // Bitmap bmp=BitmapFactory.decodeStream(is);

    // return bmp;
    // }
    public Bitmap decodeFile(String path, Options o) {

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

    // public void release(){
    // if(fileCache!=null){
    // fileCache = null;
    // }
    // }
    class BitmapDownloaderTask extends AsyncTask<String, Void, Bitmap> {

        private String url;
        private WeakReference<ImageView> imageViewWeakReference;
        private ProgressBar mProgressBar;

        public BitmapDownloaderTask(ImageView imageView, ProgressBar progressBar) {
            imageViewWeakReference = new WeakReference<ImageView>(imageView);
            mProgressBar = progressBar;
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            url = params[0];
            return downloadBitmap(url);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (isCancelled()) {
                Log.d(TAG, "download log:task is cancelled");
                if (bitmap != null && !bitmap.isRecycled()) {
                    bitmap.recycle();
                    bitmap = null;
                }
            }

            // add bitmap to cache
            // imageCache.addBitmapToCache(url, bitmap);

            if (imageViewWeakReference != null) {
                ImageView imageView = imageViewWeakReference.get();
                BitmapDownloaderTask bitmapDownloaderTask = getBitmapDownloaderTask(imageView);
                if (this == bitmapDownloaderTask) {
                    if (bitmap != null) {
                        if (type != OnlyOne) {
                            imageView.setImageBitmap(null);
                            imageView.setBackgroundDrawable(null);
                        }
                        if (type == ACTIVITYICON) {
                            int height = bitmap.getHeight();
                            int width = bitmap.getWidth();
                            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) imageView.getLayoutParams();
                            int imgWidth = (int) (SportsApp.ScreenWidth);
                            lp.height = (int) ((imgWidth * height) / width);
                            lp.width = imgWidth;
                            imageView.setLayoutParams(lp);
                        }
                        imageView.setImageBitmap(bitmap);
                    }
                    // imageView.setImageBitmap(null);
                    //
                    // imageView.setBackgroundDrawable(null);
                    // imageView.setImageBitmap(bitmap);
                    // if(type==ICON)
                    // imageView.setBackgroundResource(R.drawable.sports_user_edit_portrait);
                    //

                    if (mProgressBar != null) {
                        mProgressBar.setVisibility(View.INVISIBLE);
                        mProgressBar = null;
                    }
                }
            }
        }
    } // end of BitmapDownloaderTask

    /**
     * A fake Drawable that will be attached to the imageView while the download
     * is in progress.
     * <p/>
     * <p>
     * Contains a reference to the actual download task, so that a download task
     * can be stopped if a new binding is required, and makes sure that only the
     * last started download process can bind its result, independently of the
     * download finish order.
     * </p>
     */
    static class DownloadedDrawable extends ColorDrawable {
        private final WeakReference<BitmapDownloaderTask> bitmapDownloaderTaskReference;

        public DownloadedDrawable(BitmapDownloaderTask bitmapDownloaderTask) {
            super();
            // super(Color.TRANSPARENT);
            bitmapDownloaderTaskReference = new WeakReference<BitmapDownloaderTask>(
                    bitmapDownloaderTask);
        }

        public BitmapDownloaderTask getBitmapDownloaderTask() {
            return bitmapDownloaderTaskReference.get();
        }

        // @Override
        // public void draw(Canvas canvas) {
        // super.draw(canvas);
        // canvas.drawBitmap(bitmap, null, null);
        // }

    } // end of DownloadedDrawable
    // static class DownloadedDrawable {
    // private final WeakReference<BitmapDownloaderTask>
    // bitmapDownloaderTaskReference;
    // ImageView imageView;
    // public DownloadedDrawable(BitmapDownloaderTask
    // bitmapDownloaderTask,ImageView imageView) {
    // this.imageView=imageView;
    // bitmapDownloaderTaskReference =
    // new WeakReference<BitmapDownloaderTask>(bitmapDownloaderTask);
    // }
    //
    // public BitmapDownloaderTask getBitmapDownloaderTask() {
    // return bitmapDownloaderTaskReference.get();
    // }
    // public ImageView getDefaultImageView(){
    // imageView.setImageResource(R.drawable.downloadcenter_wallpaper_online_default_image);
    // return this.imageView;
    // }
    // } // end of DownloadedDrawable

}
