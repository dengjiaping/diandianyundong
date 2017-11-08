package com.fox.exercise.pedometer;

import java.io.File;

import cn.ingenic.indroidsync.SportsApp;

import com.fox.exercise.SportsUtilities;
import com.fox.exercise.bitmap.util.ImageCache;
import com.fox.exercise.bitmap.util.ImageCache.ImageCacheParams;
import com.fox.exercise.bitmap.util.ImageFetcher;
import com.fox.exercise.bitmap.util.ImageResizer;
import com.fox.exercise.bitmap.util.Utils;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;


public class ImageWorkManager {

    public Context mContext;
    public int width;
    public int height;
    public ImageResizer mReqImageWorker;
    public ImageCacheParams cacheParams;
    public ImageCache imageCache;

    public ImageWorkManager(Context context, int reqWidth, int reqHeight) {
        this.mContext = context;
        this.width = reqWidth;
        this.height = reqHeight;
    }

    public ImageResizer getImageWorker() {
        if (width <= 0 || height <= 0) {
            width = 480;
            height = 640;
        }
        boolean hasSDCard = SportsUtilities.hasStorage();
        File externalDir = null;
        if (hasSDCard) {
            File path = Utils.getExternalCacheDir(mContext);
            if (path != null)
                externalDir = new File(path.getPath());

        }
        SportsUtilities.DOWNLOAD_BASE_PATH = hasSDCard && (externalDir != null) && !(Utils.getUsableSpace(externalDir) < SportsApp.DEFAULT_DISK_CACHE_SIZE) ? Utils.getExternalCacheDir(mContext)
                .getPath() : mContext.getCacheDir().getPath();
        SportsUtilities.DOWNLOAD_SAVE_PATH = SportsUtilities.DOWNLOAD_BASE_PATH + "/" + ".download/";
        SportsUtilities.RECYCLE_PATH = SportsUtilities.DOWNLOAD_BASE_PATH + "/" + ".recycle/";
        SportsUtilities.DOWNLOAD_PHOTOFRAMES_PATH = SportsUtilities.DOWNLOAD_BASE_PATH + "/" + ".photoframes/";
        mReqImageWorker = new ImageFetcher(mContext, width, height, SportsUtilities.DOWNLOAD_SAVE_PATH);
        cacheParams = new ImageCacheParams(SportsUtilities.DOWNLOAD_SAVE_PATH);
        imageCache = new ImageCache(mContext, cacheParams);
        mReqImageWorker.setImageCache(imageCache);
        return mReqImageWorker;

    }

    public void release() {
        mContext = null;
        if (mReqImageWorker != null) {
            mReqImageWorker.setImageCache(null);
            mReqImageWorker = null;
        }
        cacheParams = null;
        if (imageCache != null) {
            imageCache.clearCaches();
            imageCache = null;
        }
    }
}
