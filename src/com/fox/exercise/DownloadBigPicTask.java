package com.fox.exercise;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import cn.ingenic.indroidsync.SportsApp;

import com.fox.exercise.bitmap.util.Utils;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore.Images;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class DownloadBigPicTask extends AsyncTask<Integer, Integer, Boolean> {

    private Context mContext = null;
    private String mUrl = "";
    private Dialog mDialog = null;

    private static final int UPDATE = 0x0001;
    private static final String TAG = "DownloadBigPicTask";

    public DownloadBigPicTask(Context context, String url) {
        this.mContext = context;
        if (url.contains("m_")) {
            url = url.replace("m_", "");
        }
        if (url.contains("s_")) {
            url = url.replace("s_", "");
        }
        this.mUrl = url;
    }

    @Override
    protected void onPostExecute(Boolean result) {
        super.onPostExecute(result);
        if (result) {
            mDialog.dismiss();
            Toast.makeText(mContext, mContext.getResources().getString(R.string.sports_detail_download_finish),
                    Toast.LENGTH_SHORT).show();

            //update db
            String store_location = Environment.getExternalStorageDirectory().toString() + "/sports";
            String title = mUrl.substring(mUrl.lastIndexOf("/") + 1, mUrl.lastIndexOf("."));
            String filename = title + ".jpg";
            String mFilePath = store_location + "/" + filename;

            ContentResolver cr = mContext.getContentResolver();
            long size = new File(store_location, filename).length();
            ContentValues values = new ContentValues(5);
            values.put(Images.Media.TITLE, title);
            values.put(Images.Media.DISPLAY_NAME, filename);
            values.put(Images.Media.MIME_TYPE, "image/jpg");
            values.put(Images.Media.DATA, mFilePath);
            values.put(Images.Media.SIZE, size);
            Uri mCurrContentUri = cr.insert(Images.Media.EXTERNAL_CONTENT_URI, values);
            mContext.sendBroadcast(new Intent(
                    "com.android.camera.NEW_PICTURE", mCurrContentUri));

        } else {
            Toast.makeText(mContext, mContext.getResources().getString(R.string.sports_detail_download_fail),
                    Toast.LENGTH_SHORT).show();
            DownloadBigPicTask task = new DownloadBigPicTask(mContext, mUrl);
            task.execute();
        }
    }

    @Override
    protected void onPreExecute() {
        if (mDialog == null) {
            mDialog = new Dialog(mContext, R.style.sports_dialog);
            LayoutInflater mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = mInflater.inflate(R.layout.sports_progressdialog, null);
            TextView message = (TextView) v.findViewById(R.id.message);
            message.setText(R.string.sports_detail_downloading);
            v.setMinimumWidth((int) (SportsApp.ScreenWidth * 0.8));
            mDialog.setContentView(v);
            mDialog.setCancelable(true);
            mDialog.setCanceledOnTouchOutside(false);
        }
        mDialog.show();
    }

    @Override
    protected Boolean doInBackground(Integer... arg0) {
        URL url;
        try {
            url = new URL(mUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            BufferedInputStream bis = new BufferedInputStream(conn.getInputStream(), Utils.IO_BUFFER_SIZE);

            File f = new File(Environment.getExternalStorageDirectory().toString() + "/sports/"
                    + mUrl.substring(mUrl.lastIndexOf("/") + 1, mUrl.lastIndexOf(".")) + ".jpg");
            Log.e(TAG, "mUrl:" + mUrl.toString());
            Log.e(TAG, "path:" + f.toString());
            File dir = new File(f.getParent());
            if (!dir.exists()) {
                dir.mkdirs();
            }
            if (!f.exists()) {
                f.createNewFile();
            } else {
                f.delete();
                f.createNewFile();
            }
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(f), Utils.IO_BUFFER_SIZE);

            int flag = bis.read();
            while (flag != -1) {
                bos.write(flag);
                flag = bis.read();
            }
            bos.flush();
            bos.close();
            bis.close();
            conn.disconnect();

        } catch (MalformedURLException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

}
