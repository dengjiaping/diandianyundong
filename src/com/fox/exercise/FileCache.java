package com.fox.exercise;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.util.Log;


public class FileCache {
	
	private static final String LOG_TAG = FileCache.class.getSimpleName();
	
	private File cacheDir;
	 public static final String DOWNLOAD_TEMP_SAVE_PATH = "/sports";

	private static final String TAG = "FileCache";
	/**
	 * @param context
	 */
	public FileCache(Context context) {
		Log.d(TAG,"sdcard mounted?"+android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED));
		if(android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
			cacheDir = new File(SportsUtilities.DOWNLOAD_SAVE_PATH);
			Log.d(TAG, "cacheDir:"+cacheDir);
			Log.d(TAG, "cacheDir EXIST?:"+cacheDir.exists());
		} else {
			cacheDir = context.getCacheDir();
		}
		if(!cacheDir.exists()) {
			cacheDir.mkdirs();
			Log.d(TAG, "cacheDir EXIST AFTER MAKE?:"+cacheDir.exists());
		}
	
	}
	
	/**
	 * @param url
	 * @param
	 */
	public void addToFileCache(String url, InputStream inputStream) {
		OutputStream outputStream = null;
		try {
			outputStream = new FileOutputStream(getFromFileCache(url));
			copyStream(inputStream, outputStream);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if(outputStream != null) {
				try {
					outputStream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	public File getFromFileCache(String url) {
		String fileName = urlToFileName(url);
		File file = new File(cacheDir, fileName);
		Log.d(TAG,"download log:try to get file from cache,file PATH:"+file.getAbsolutePath());
		return file;
	}
	
	/**
	 * clear cache
	 */
	public void clearCache() {
		File[] files=cacheDir.listFiles();
        if(files==null)
            return;
        for(File f:files)
            f.delete();
	}

	/**
	 * clear cache
	 */
	public void clearUrlCache(String url) {
		String fileName = urlToFileName(url);
		File file = new File(cacheDir, fileName);
		if(file==null){
			return;
		}
		file.delete();
	}
	
	
	/**
	 * 
	 * @param url
	 * @return
	 */
	private String urlToFileName(String url) {
      String urlName = url;
//		if (urlName != null && urlName.length() != 0) {
//			urlName = urlName.substring(urlName.lastIndexOf("/") + 1);
//		}
      int i = url.lastIndexOf("/");
		int j = url.lastIndexOf(".");
		urlName = url.substring(i + 1, j);
//		urlName += ".jpg";
		return urlName;
	}
	
	private void copyStream(InputStream is, OutputStream os){
        final int buffer_size=1024;
        try
        {
            byte[] bytes=new byte[buffer_size];
            for(;;)
            {
              int count=is.read(bytes, 0, buffer_size);
              if(count==-1)
                  break;
              os.write(bytes, 0, count);
            }
        }
        catch(Exception ex){}
    }


}
