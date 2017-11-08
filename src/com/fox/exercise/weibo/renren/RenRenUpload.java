package com.fox.exercise.weibo.renren;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import com.fox.exercise.Util;

import android.content.Context;
import android.util.Log;

public class RenRenUpload {

    private Context context = null;

    public RenRenUpload(Context context) {
        this.context = context;
    }

    public void upload(String path, String content) {

        // RenrenDataHelper helper = new RenrenDataHelper(context);
        // String token = helper.getAccessToken();
        // String signature = helper.getSignature();
        // helper.Close();

        String url = "http://api.renren.com/restserver.do";
        String signature = getParams(content);
        String method = "photos.upload";
        String v2 = "1.0";
        String access_token = AccessData.access_token;
        String format = "JSON";
        String contentType = "multipart/form-data";
        String filename = Util.getFileName(path);
        String caption = content;
        byte[] data = getBytes(path);

        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("sig", signature);
        Log.d("upload renren", "signature:" + signature);
        params.put("method", method);
        params.put("v", v2);
        params.put("access_token", access_token);
        Log.d("upload renren", "access_token:" + access_token);
        params.put("format", format);
        params.put("caption", caption);
        String resp;
        try {
            resp = HttpURLUtils.doUploadFile(url, params, "upload",
                    filename, contentType, data);
            Log.d("upload renren", "" + resp);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private String getParams(String content) {
        RenrenDataHelper helper = new RenrenDataHelper(context);
        String token = helper.getAccessToken();
        String secret = AccessData.RENREN_SECRET;
        helper.Close();
        List<String> params = new ArrayList<String>();
        String method = "photos.upload";
        String v1 = "1.0";
        String format = "JSON";
        String caption = content;
        params.add("method=" + method);
        params.add("v=" + v1);
        params.add("access_token=" + token);
        params.add("format=" + format);
        params.add("caption=" + caption);
        return Util.getSignature(params, secret);
    }

    private byte[] getBytes(String path) {
        try {
            FileInputStream in = new FileInputStream(path);
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            for (int i = 0; (i = in.read(buf)) > 0; )
                os.write(buf, 0, i);
            in.close();
            return os.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
