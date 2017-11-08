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

package com.fox.exercise.util;

import java.lang.ref.WeakReference;
import java.util.List;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.fox.exercise.R;

/**
 * This class wraps up completing some arbitrary long running work when loading
 * a bitmap to an ImageView. It handles things like using a memory and disk
 * cache, running the work in a background thread and setting a placeholder
 * image.
 */
public abstract class ImageWorker {
    private static final String TAG = "ImageWorker";
    private static final int FADE_IN_TIME = 200;

    private Bitmap mLoadingBitmap;
    private boolean mFadeInBitmap = false;
    private boolean mExitTasksEarly = false;
    private ImageCache mImageCache;
    protected Context mContext;
    protected ImageWorkerAdapter mImageWorkerAdapter;
    protected List<?> mImageList;
    private ProgressBar progressBar;

//	private WaitThread mWaitThread = null;
//	private ArrayList<String> mUrlList = new ArrayList<String>();
//	private ArrayList<ImageView> mImgList = new ArrayList<ImageView>();
//	private ArrayList<Boolean> mFilterUrlList = new ArrayList<Boolean>();
//	private WaitHandler mWaitHandler = new WaitHandler();
//	private static final int START_TASK = 0x0001;

    protected ImageWorker(Context context) {
        mContext = context;
        mLoadingBitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.sports_user_edit_portrait_male);
    }

    /**
     * Load an image specified by the data parameter into an ImageView (override
     * {@link ImageWorker#processBitmap(Object)} to define the processing
     * logic). A memory and disk cache will be used if an {@link ImageCache} has
     * been set using {@link ImageWorker#setImageCache(ImageCache)}. If the
     * image is found in the memory cache, it is set immediately, otherwise an
     * {@link AsyncTask} will be created to asynchronously load the bitmap.
     *
     * @param data
     *            The URL of the image to download.
     * @param imageView
     *            The ImageView to bind the downloaded image to.
     */
    // public void loadImage(Object data, ImageView imageView) {
    // Bitmap bitmap = null;
    //
    //
    // if (cancelPotentialWork(data, imageView)) {
    // final BitmapWorkerTask task = new BitmapWorkerTask(imageView);
    // final AsyncDrawable asyncDrawable =
    // new AsyncDrawable(mContext.getResources(), mLoadingBitmap, task);
    // imageView.setImageDrawable(asyncDrawable);
    // task.execute(url);
    // }
    // }

    /**
     * @param url
     * @param imageView
     * @param progressBar show a progressbar while downloading
     * @param mHandler    pass a handler to ensure the progressbar show correctly
     * @param filterUrl   if the pic download from our app server, we need to normal
     *                    filter the url so we can save it in local diskcache with
     *                    format like *.jpg, otherwise save the whole url string as the
     *                    file name in special filter. true: pic not from the app
     *                    server,need filter,save the whole url string as the file name.
     *                    false: pic from the app server, no need special filter.
     */
    public void loadImage(String url, ImageView imageView, ProgressBar progressBar, Handler mHandler, boolean filterUrl) {
        Bitmap bitmap = null;

//		if (url.contains(".jpg")) {
//			url = url.replace(".jpg", "");
//		} else if (url.contains(".png")) {
//			url = url.replace(".png", "");
//		}
        if (cancelPotentialWork(url, imageView)) {
            Log.d(TAG, "load image begin");
            final BitmapWorkerTask task = new BitmapWorkerTask(imageView, progressBar, mHandler, filterUrl);

            final AsyncDrawable asyncDrawable = new AsyncDrawable(mContext.getResources(), mLoadingBitmap, task);
            imageView.setImageDrawable(asyncDrawable);

//			try {
            task.execute(url);
//			} catch (RejectedExecutionException e) {
//				e.printStackTrace();
//				// mTaskList.add(task);
//				Log.d(TAG, "RejectedExecutionException INVOKED");
//				mUrlList.add(url);
//				mImgList.add(imageView);
//				mFilterUrlList.add(filterUrl);
//				if (mWaitThread == null || !mWaitThread.isAlive()) {
//					mWaitThread = new WaitThread();
//					mWaitThread.start();
//				}
//			}
        }
    }

//	class WaitThread extends Thread {
//
//		@Override
//		public void run() {
//			while (mUrlList.size() > 0) {
//				Log.d(TAG, "mUrlList.size:" + mUrlList.size());
//				Thread.yield();
//				Message msg = Message.obtain(mWaitHandler, START_TASK);
//				// Bundle bundle = new Bundle();
//				// bundle.putString("url", mUrlList.get(0));
//				// mTaskList.remove(0);
//				// msg.setData(bundle);
//				msg.sendToTarget();
//			}
//		}
//
//	}

//	class WaitHandler extends Handler {
//
//		@Override
//		public void handleMessage(Message msg) {
//			if (msg.what == START_TASK) {
//				if (mUrlList.size() > 0 && mImgList.size() > 0) {
//					String url = mUrlList.get(0);
//					ImageView imageView = mImgList.get(0);
//					boolean filterUrl = mFilterUrlList.get(0);
//					mUrlList.remove(0);
//					mImgList.remove(0);
//					mFilterUrlList.remove(0);
//					final BitmapWorkerTask task = new BitmapWorkerTask(imageView, null, null, filterUrl);
//
//					final AsyncDrawable asyncDrawable = new AsyncDrawable(mContext.getResources(), mLoadingBitmap, task);
//					imageView.setImageDrawable(asyncDrawable);
//					try {
//						task.execute(url);
//					} catch (Exception e) {
//						e.printStackTrace();
//						mUrlList.add(url);
//						mImgList.add(imageView);
//						mFilterUrlList.add(filterUrl);
//						if (mWaitThread == null || !mWaitThread.isAlive()) {
//							mWaitThread = new WaitThread();
//							mWaitThread.start();
//						}
//					}
//				}
//			}
//		}
//
//	}

    /**
     * Load an image specified from a set adapter into an ImageView (override
     * {@link ImageWorker#processBitmap(Object)} to define the processing
     * logic). A memory and disk cache will be used if an {@link ImageCache} has
     * been set using {@link ImageWorker#setImageCache(ImageCache)}. If the
     * image is found in the memory cache, it is set immediately, otherwise an
     * {@link AsyncTask} will be created to asynchronously load the bitmap.
     * {@link ImageWorker#setAdapter(ImageWorkerAdapter)} must be called before
     * using this method.
     *
     * @param data
     *            The URL of the image to download.
     * @param imageView
     *            The ImageView to bind the downloaded image to.
     */


    /**
     * Set placeholder bitmap that shows when the the background thread is
     * running.
     *
     * @param bitmap
     */
    public void setLoadingImage(Bitmap bitmap) {
        mLoadingBitmap = bitmap;
    }

    /**
     * Set placeholder bitmap that shows when the the background thread is
     * running.
     *
     * @param resId
     */
    public void setLoadingImage(int resId) {
        mLoadingBitmap = BitmapFactory.decodeResource(mContext.getResources(), resId);
    }

    public void setImageCache(ImageCache cacheCallback) {
        mImageCache = cacheCallback;
    }

    public ImageCache getImageCache() {
        return mImageCache;
    }

    /**
     * If set to true, the image will fade-in once it has been loaded by the
     * background thread.
     *
     * @param fadeIn
     */
    public void setImageFadeIn(boolean fadeIn) {
        mFadeInBitmap = fadeIn;
    }

    public void setExitTasksEarly(boolean exitTasksEarly) {
        mExitTasksEarly = exitTasksEarly;
    }

    /**
     * Subclasses should override this to define any processing or work that
     * must happen to produce the final bitmap. This will be executed in a
     * background thread and be long running. For example, you could resize a
     * large bitmap here, or pull down an image from the network.
     *
     * @param data      The data to identify which image to process, as provided by
     *                  {@link ImageWorker#loadImage(Object, ImageView)}
     * @param filterUrl whether need to filter the url string.
     * @return The processed bitmap
     */
    protected abstract Bitmap processBitmap(String url, boolean filterUrl);

    public static void cancelWork(ImageView imageView) {
        final BitmapWorkerTask bitmapWorkerTask = getBitmapWorkerTask(imageView);
        if (bitmapWorkerTask != null) {
            bitmapWorkerTask.cancel(true);
            final Object bitmapData = bitmapWorkerTask.url;
            Log.d(TAG, "cancelWork - cancelled work for " + bitmapData);
        }
    }

    /**
     * Returns true if the current work has been canceled or if there was no
     * work in progress on this image view. Returns false if the work in
     * progress deals with the same data. The work is not stopped in that case.
     */
    public static boolean cancelPotentialWork(String url, ImageView imageView) {
        final BitmapWorkerTask bitmapWorkerTask = getBitmapWorkerTask(imageView);

        if (bitmapWorkerTask != null) {
            final String imageUrl = bitmapWorkerTask.url;
            if (imageUrl == null || !imageUrl.equals(url)) {
                bitmapWorkerTask.cancel(true);
                Log.d(TAG, "TODO cancelPotentialWork - cancelled work for " + url);
            } else {
                // The same work is already in progress.
                return false;
            }
        }
        return true;
    }

    /**
     * @param imageView Any imageView
     * @return Retrieve the currently active work task (if any) associated with
     * this imageView. null if there is no such task.
     */
    private static BitmapWorkerTask getBitmapWorkerTask(ImageView imageView) {
        if (imageView != null) {
            final Drawable drawable = imageView.getDrawable();
            if (drawable instanceof AsyncDrawable) {
                final AsyncDrawable asyncDrawable = (AsyncDrawable) drawable;
                return asyncDrawable.getBitmapWorkerTask();
            }
        }
        return null;
    }

    /**
     * The actual AsyncTask that will asynchronously process the image.
     */
    private class BitmapWorkerTask extends AsyncTask<String, Void, Bitmap> {
        private String url;
        private ProgressBar progressBar;
        private final WeakReference<ImageView> imageViewReference;
        private Handler mHandler;
        private boolean filterUrl;

        public BitmapWorkerTask(ImageView imageView, ProgressBar progressBar, Handler handler, boolean filterUrl) {
            this.progressBar = progressBar;
            this.mHandler = handler;
            this.filterUrl = filterUrl;
            imageViewReference = new WeakReference<ImageView>(imageView);
        }

        /**
         * Background processing.
         */
        @Override
        protected Bitmap doInBackground(String... params) {
            url = params[0];
            // final String dataString = String.valueOf(data);
            Bitmap bitmap = null;

            // If the image cache is available and this task has not been
            // cancelled by another
            // thread and the ImageView that was originally bound to this task
            // is still bound back
            // to this task and our "exit early" flag is not set then try and
            // fetch the bitmap from
            // the cache
            // if (mImageCache != null && !isCancelled() &&
            // getAttachedImageView() != null
            // && !mExitTasksEarly) {
            // bitmap = mImageCache.getBitmapFromDiskCache(url);
            // }
            // If the bitmap was not found in the cache and this task has not
            // been cancelled by
            // another thread and the ImageView that was originally bound to
            // this task is still
            // bound back to this task and our "exit early" flag is not set,
            // then call the main
            // process method (as implemented by a subclass)
            Log.d(TAG, "mExitTasksEarly:" + mExitTasksEarly);
            Log.d(TAG, "isCancelled():" + isCancelled());
            if (bitmap == null && !isCancelled() && getAttachedImageView() != null && !mExitTasksEarly) {
                Log.d(TAG, "processBitmap_____");
                bitmap = processBitmap(params[0], filterUrl);

            }

            // If the bitmap was processed and the image cache is available,
            // then add the processed
            // bitmap to the cache for future use. Note we don't check if the
            // task was cancelled
            // here, if it was, and the thread is still running, we may as well
            // add the processed
            // bitmap to our cache as it might be used again in the future

            // if (bitmap != null && mImageCache != null) {
            // mImageCache.addBitmapToCache(url, bitmap);
            // }
            if (bitmap == null) {
                Log.d(TAG,
                        "TODO:bitmap still return null when we found it exist in local file and retried,might be the skia decode return null or false."
                                + url);
            } else {
                Log.d(TAG, "bitmap:" + bitmap.getWidth());
                Log.d(TAG, "bitmap:" + bitmap.getHeight());
            }

            return bitmap;
        }

        /**
         * Once the image is processed, associates it to the imageView
         */
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            // if cancel was called on this task or the "exit early" flag is set
            // then we're done
            if (isCancelled() || mExitTasksEarly) {
                bitmap = null;
            }

            final ImageView imageView = getAttachedImageView();
            if (bitmap != null && imageView != null) {
                setImageBitmap(imageView, bitmap);
            }
            if (mHandler != null) {
                mHandler.sendEmptyMessage(13);
            }
            if (progressBar != null) {
                progressBar.setVisibility(View.INVISIBLE);
                progressBar = null;
            }
        }

        /**
         * Returns the ImageView associated with this task as long as the
         * ImageView's task still points to this task as well. Returns null
         * otherwise.
         */
        private ImageView getAttachedImageView() {
            final ImageView imageView = imageViewReference.get();
            final BitmapWorkerTask bitmapWorkerTask = getBitmapWorkerTask(imageView);

            if (this == bitmapWorkerTask) {
                return imageView;
            }

            return null;
        }
    }

    /**
     * A custom Drawable that will be attached to the imageView while the work
     * is in progress. Contains a reference to the actual worker task, so that
     * it can be stopped if a new binding is required, and makes sure that only
     * the last started worker process can bind its result, independently of the
     * finish order.
     */
    private static class AsyncDrawable extends BitmapDrawable {
        private final WeakReference<BitmapWorkerTask> bitmapWorkerTaskReference;

        public AsyncDrawable(Resources res, Bitmap bitmap, BitmapWorkerTask bitmapWorkerTask) {
            super(res, bitmap);

            bitmapWorkerTaskReference = new WeakReference<BitmapWorkerTask>(bitmapWorkerTask);
        }

        public BitmapWorkerTask getBitmapWorkerTask() {
            return bitmapWorkerTaskReference.get();
        }
    }

    /**
     * Called when the processing is complete and the final bitmap should be set
     * on the ImageView.
     *
     * @param imageView
     * @param bitmap
     */
    private void setImageBitmap(ImageView imageView, Bitmap bitmap) {
        if (mFadeInBitmap) {
            // Transition drawable with a transparent drwabale and the final
            // bitmap
            final TransitionDrawable td = new TransitionDrawable(
                    new Drawable[]{new ColorDrawable(android.R.color.transparent),
                            new BitmapDrawable(mContext.getResources(), bitmap)});
            // Set background to loading bitmap
            imageView.setBackgroundDrawable(new BitmapDrawable(mContext.getResources(), mLoadingBitmap));

            imageView.setImageDrawable(td);
            td.startTransition(FADE_IN_TIME);
        } else {
            imageView.setImageBitmap(bitmap);
        }
    }

    /**
     * Set the simple adapter which holds the backing data.
     *
     * @param <E>
     * @param adapter
     */
    public void setAdapter(List<?> adapter) {
        mImageList = adapter;
    }

    /**
     * Get the current adapter.
     *
     * @return
     */
    public List<?> getAdapter() {
        return mImageList;
    }

    /**
     * Set the simple adapter which holds the backing data.
     *
     * @param adapter
     */
    // public void setAdapter(ImageWorkerAdapter adapter) {
    // mImageWorkerAdapter = adapter;
    // }

    /**
     * Get the current adapter.
     *
     * @return
     */
    // public ImageWorkerAdapter getAdapter() {
    // return mImageWorkerAdapter;
    // }

    /**
     * A very simple adapter for use with ImageWorker class and subclasses.
     */
    public static abstract class ImageWorkerAdapter {
        public abstract Object getItem(int num);

        public abstract int getSize();
    }
}
