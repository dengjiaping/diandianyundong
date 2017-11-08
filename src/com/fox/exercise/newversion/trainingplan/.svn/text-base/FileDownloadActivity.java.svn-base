package com.fox.exercise.newversion.trainingplan;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fox.exercise.ImageDownloader;
import com.fox.exercise.R;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import cn.ingenic.indroidsync.SportsApp;

public class FileDownloadActivity extends Activity {
    private TextView download_label;
    private ProgressBar download_bar;
    private String dir = Environment.getExternalStorageDirectory().toString() + "/android/data/"
            + SportsApp.getContext().getPackageName() + "/cache/";
    private String url, downLoadPath, fileName;
    private ImageDownloader bg_Downloader = null;
    private ImageView iv_background;
    private RelativeLayout RelativeLayout1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_file_download);

        RelativeLayout1 = (RelativeLayout) this.findViewById(R.id.RelativeLayout1);
        ViewGroup.LayoutParams params = RelativeLayout1.getLayoutParams();
        params.width = this.getWindowManager().getDefaultDisplay().getWidth();
        params.height = params.width * 916 / 1628;
        RelativeLayout1.setLayoutParams(params);

        download_label = (TextView) this.findViewById(R.id.download_label);
        download_bar = (ProgressBar) this.findViewById(R.id.download_bar);

        iv_background = (ImageView) this.findViewById(R.id.iv_background);
        bg_Downloader = new ImageDownloader(this);
        bg_Downloader.setType(ImageDownloader.OnlyOne);
        bg_Downloader.download("http://dev-kupao.mobifox.cn/Beauty/sportsdata/Train/2016-03-15/56e783256b784.jpg", iv_background, null);

        //获取传递的Intent的Bundle的url键值
        url = getIntent().getExtras().getString("train_file_url");
        downLoadPath = getIntent().getExtras().getString("train_file_path");
        fileName = url.substring(url.lastIndexOf("/") + 1, url.length());

        new Thread(new Runnable() {
            @Override
            public void run() {
                //另起线程执行下载，安卓最新sdk规范，网络操作不能再主线程。
                FileDownload l = new FileDownload(url);
                download_bar.setMax(l.getLength());

                /**
                 * 下载文件到sd卡，虚拟设备必须要开始设置sd卡容量
                 * downhandler是Download的内部类，作为回调接口实时显示下载数据
                 */
                int status = l.down2sd(downLoadPath, fileName, l.new downhandler() {
                    @Override
                    public void setSize(int size) {
                        Message msg = handler.obtainMessage();
                        msg.arg1 = size;
                        msg.sendToTarget();
                        Log.e("develop_debug", Integer.toString(size));
                    }
                });
                //log输出
            }
        }).start();
    }

    final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //这里就一条消息
            int pro = download_bar.getProgress() + msg.arg1;
            download_bar.setProgress(pro);
            download_label.setText(Integer.toString(pro / (download_bar.getMax() / 100)) + "%");
            if (pro >= download_bar.getMax()) {
                try {
                    if (fileName.contains(".zip")) {
                        upZipFile(dir + downLoadPath + fileName, dir + downLoadPath + fileName.substring(0, fileName.length() - 4));
                    }
                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (ZipException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                Intent intent = new Intent();
                intent.putExtra("filePath", dir + downLoadPath + fileName);
                setResult(1, intent);
                finish();
            }
        }
    };

    public int upZipFile(String zipFile, String folderPath) throws ZipException, IOException {
        Log.e("develop_debug", "zipFile : " + zipFile);
        Log.e("develop_debug", "folderPath : " + folderPath);

        File upZipDir = new File(folderPath);
        if (!upZipDir.exists()) {
            upZipDir.mkdirs();
        }

        ZipFile zfile = new ZipFile(zipFile);
        Enumeration zList = zfile.entries();
        ZipEntry ze = null;
        byte[] buf = new byte[1024];
        while (zList.hasMoreElements()) {
            ze = (ZipEntry) zList.nextElement();
            if (ze.isDirectory()) {
                String dirstr = folderPath + ze.getName();
                dirstr = new String(dirstr.getBytes("8859_1"), "GB2312");
                Log.e("develop_debug", "str = " + dirstr);
                File f = new File(dirstr);
                f.mkdir();
                continue;
            }
            OutputStream os = new BufferedOutputStream(new FileOutputStream(getRealFileName(folderPath, ze.getName())));
            InputStream is = new BufferedInputStream(zfile.getInputStream(ze));
            int readLen = 0;
            while ((readLen = is.read(buf, 0, 1024)) != -1) {
                os.write(buf, 0, readLen);
            }
            is.close();
            os.close();
        }
        zfile.close();
        Log.e("develop_debug", "finishssssssssssssssssssss");
        return 0;
    }

    public static File getRealFileName(String baseDir, String absFileName) {
        String[] dirs = absFileName.split("/");
        String lastDir = baseDir;

        if (dirs.length > 1) {
            for (int i = 0; i < dirs.length - 1; i++) {
                lastDir += (dirs[i] + "/");
                File dir = new File(lastDir);

                if (!dir.exists()) {
                    dir.mkdirs();
                    Log.e("develop_debug", "create dir = " + (lastDir + "/" + dirs[i]));
                }
            }
            File ret = new File(lastDir, dirs[dirs.length - 1]);
            Log.e("develop_debug", "2ret = " + ret);
            return ret;
        } else {
            return new File(baseDir, absFileName);
        }
    }

    /**
     *@method 固定字体大小方法
     *@author suhu
     *@time 2016/10/11 13:23
     *@param
     *
     */
    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        Configuration config=new Configuration();
        config.setToDefaults();
        res.updateConfiguration(config,res.getDisplayMetrics() );
        return res;
    }
}