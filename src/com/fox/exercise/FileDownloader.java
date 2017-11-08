package com.fox.exercise;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

public class FileDownloader {
    
    
    private InputStream inputStream;
    private URLConnection connection;
    //private OutputStream outputStream;
    private int FileLength;
    private Handler handler;
    private boolean isCancel = false;
    Context mContext;
    private int DownedFileLength = 0;
    String savePathString = "";
    String mfilename="";
    
    
    
    public int getFileLength()
    {
        return FileLength;
    }
    public int getDownedFileLength()
    {
        return DownedFileLength;
    }
    public void setCancel(boolean b)
    {
        isCancel = b;
    }
    public FileDownloader (Context context, Handler h, String filename)
    {   
        mfilename = filename;
        mContext = context;
        handler = h;
        savePathString = filename;
    }
    /*
    private void MessageBox(int lengthShrort, String string) {
        // TODO Auto-generated method 
        Toast toast = Toast.makeText(mContext, string, lengthShrort);
        toast.show();
    }
    */
    public void DownFile(String urlString) {
        DownedFileLength = 0;
        try {
            URL url = new URL(urlString);
            //URL url = new URL("http://119.147.99.86/down_group223/M00/09/4B/d5NjVk80hGsAAAAAABcKNHPLI604329923/SoapTest_17vee.apk?k=DCZ4C4qzzjlm6e7TFeBeow&t=1329414158&u=114.28.255.242@22140485@be3umvas&file=SoapTest_17vee.apk");
            connection = url.openConnection();
            inputStream = connection.getInputStream();
        } catch (Exception e) {
            e.printStackTrace();
             //MessageBox(Toast.LENGTH_LONG,
             //           mContext.getString(R.string.download_apk_failed));
             Message message3 = new Message();
             message3.what = 3;
             handler.sendMessage(message3);
             
             return;
        }
        // String savePath = Environment.getExternalStorageDirectory()
        // + "/DownFile";
        // File file1 = new File(savePath);
        // if (!file1.exists()) {
        // file1.mkdir();
        // }
        // String savePathString = Environment.getExternalStorageDirectory()
        // + "/DownFile/" + "tmp.apk";

        
        // + urlString.substring((urlString.lastIndexOf("/") + 1));
        File file = new File(savePathString);
        if (file.exists()) {
            file.delete();
        }
        
        // if (!file.exists()) {
        // try {
        // file.createNewFile();
        // } catch (IOException e) {
        // e.printStackTrace();
        // Message message1 = new Message();
        // message1.what = 3;
        // handler.sendMessage(message1);
        // }
        // }

        try {
            connection.setReadTimeout(10000);
            FileLength = connection.getContentLength();
            if(FileLength == -1)
            {
                Message message1 = new Message();
                message1.what = 3;
                handler.sendMessage(message1);
            }

            FileOutputStream outputStream = null;
            if(mfilename.startsWith(mContext.getFilesDir().getPath()))
            {
                String filenamewithoutpath = mfilename.substring(mfilename.lastIndexOf(File.separator)+1);
                outputStream = mContext.openFileOutput(filenamewithoutpath, 
                        Context.MODE_PRIVATE
                        + Context.MODE_WORLD_READABLE);
            }
            else
            {   
                String filenamepath = mfilename.substring(0, mfilename.lastIndexOf(File.separator)+1);
                String filenamewithoutpath = mfilename.substring(mfilename.lastIndexOf(File.separator)+1);
                
                File folder = new File(filenamepath);
                if(!folder.exists())
                {
                    folder.mkdir();
                }
                
                File saveFilePath = new File(folder, filenamewithoutpath);
                
                outputStream = new FileOutputStream(saveFilePath);
            }
            
            
            // byte[] buffer = new byte[FileLength];
            byte buffer[] = new byte[512];

            do {
                if (isCancel)
                {
                    break;
                }
                int numread = inputStream.read(buffer);
                if (numread == -1) {
                    break;
                }
                outputStream.write(buffer, 0, numread);
                DownedFileLength += numread;

                Message message1 = new Message();
                message1.what = 1;
                handler.sendMessage(message1);
            } while (true);
            if (!isCancel)
            {
                outputStream.flush();
                Message message2 = new Message();
                message2.what = 2;
                handler.sendMessage(message2);
            }
            outputStream.close();
            inputStream.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
   
    
}
