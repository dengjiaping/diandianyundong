package com.fox.exercise.login;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONObject;

import com.fox.exercise.R;


import android.content.Context;

public class UpdateMessage {

    private String versionCode;
    private String versionName;
    private String download_url;
    private String update_info;
    private String filesize;
    private String update_date;
    private String force_update;
    //private static final String PATH = "http://cdn.17vee.com/lmstation/Home/ver.json";
    private String PATH = "http://cdn.17vee.com/lmstation/picturetool/";

    //= "http://cdn.17vee.com/17VeeAPK/ver.json";
    //"http://119.147.99.61/down_group198/M00/0E/FC/d5NjPU80hOYAAAAAAAABqzOx0ng0629479/ver.json?k=KwRsjTKVBTI1ucb-OcZTRQ&t=1329414094&u=114.28.255.242@22140485@be3um1to&file=ver.json";
    UpdateMessage(Context context) throws Exception {
        PATH = context.getResources().getString(R.string.config_version_url);
        URL url = new URL(PATH);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(5000);
        if (conn != null) {
            InputStream instream = conn.getInputStream();
            InputStreamReader in = new InputStreamReader(instream);
            BufferedReader buffer = new BufferedReader(in);
            String s = null;
            StringBuilder sb = new StringBuilder();
            while ((s = buffer.readLine()) != null) {
                sb.append(s + "\n");
            }
            in.close();
            conn.disconnect();

            JSONObject jsonObject = new JSONObject(sb.toString());
            setVersionCode(jsonObject.getString("ver_code"));
            setVersionName(jsonObject.getString("ver_name"));
            setDownload_url(jsonObject.getString("download_url"));
            setUpdate_date(jsonObject.getString("update_date"));
            setUpdate_info(jsonObject.getString("update_info"));
            setFilesize(jsonObject.getString("filesize"));
            setForce_update(jsonObject.getString("force_update"));
        }
    }

    public int getVersionCode() {
        return Integer.parseInt(versionCode);
    }

    public void setVersionCode(String versionCode) {
        this.versionCode = versionCode;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public String getDownload_url() {
        return download_url;
    }

    public void setDownload_url(String downloadUrl) {
        download_url = downloadUrl;
    }

    public String getUpdate_info() {
        return update_info;
    }

    public void setUpdate_info(String updateInfo) {
        update_info = updateInfo;
    }

    public String getFilesize() {
        return filesize;
    }

    public void setFilesize(String filesize) {
        this.filesize = filesize;
    }

    public String getUpdate_date() {
        return update_date;
    }

    public void setUpdate_date(String updateDate) {
        update_date = updateDate;
    }

    public String getForce_update() {
        return force_update;
    }

    public void setForce_update(String forceUpdate) {
        force_update = forceUpdate;
    }

}
