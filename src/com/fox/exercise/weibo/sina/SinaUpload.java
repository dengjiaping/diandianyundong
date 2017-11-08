package com.fox.exercise.weibo.sina;

import java.io.IOException;

import com.fox.exercise.weibo.sina.oauth2.AccessToken;
import com.fox.exercise.weibo.sina.oauth2.AsyncWeiboRunner;
import com.fox.exercise.weibo.sina.oauth2.AsyncWeiboRunner.RequestListener;
import com.fox.exercise.weibo.sina.oauth2.Utility;
import com.fox.exercise.weibo.sina.oauth2.Weibo;
import com.fox.exercise.weibo.sina.oauth2.WeiboException;
import com.fox.exercise.weibo.sina.oauth2.WeiboParameters;


import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

public class SinaUpload implements RequestListener {
    private static final String URL = "https://api.weibo.com/2/statuses/update.json";
    private String mAccessToken = "";
    private String mTokenSecret = "";
    private Context context = null;

    public SinaUpload(Context context) {
        this.context = context;
    }

    public void uploadWeibo(String path, String content) {
        SinaDBDao dao = new SinaDBDao(context);
        mAccessToken = dao.getAccessToken();
        Log.d("upload sina", "AccessToken" + mAccessToken);
        mTokenSecret = dao.getTokenSecret();
        Log.d("upload sina", "TokenSecret" + mTokenSecret);
        dao.closeDB();

        // AccessToken accessToken = new AccessToken(mAccessToken,
        // mTokenSecret);
        Weibo weibo = Weibo.getInstance();
        AccessToken accessToken = new AccessToken(mAccessToken, mTokenSecret);
        weibo.setAccessToken(accessToken);
//		try {
//			weibo.share2weibo((Activity) context, mAccessToken, mTokenSecret,
//					content, path);
//		} catch (WeiboException e) {
//			e.printStackTrace();
//		}
        try {
            upload(weibo, Weibo.getAppKey(), path, content, "", "");
        } catch (WeiboException e) {
            e.printStackTrace();
        }
    }

    private String upload(Weibo weibo, String source, String file, String status, String lon,
                          String lat) throws WeiboException {
        WeiboParameters bundle = new WeiboParameters();
        bundle.add("source", source);
        bundle.add("pic", file);
        bundle.add("status", status);
        if (!TextUtils.isEmpty(lon)) {
            bundle.add("lon", lon);
        }
        if (!TextUtils.isEmpty(lat)) {
            bundle.add("lat", lat);
        }
        String rlt = "";
        String url = Weibo.SERVER + "statuses/upload.json";
        AsyncWeiboRunner weiboRunner = new AsyncWeiboRunner(weibo);
        weiboRunner.request((Activity) context, url, bundle, Utility.HTTPMETHOD_POST, this);
        Log.d("upload sina", "" + rlt);
        return rlt;
    }

    @Override
    public void onComplete(String response) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onIOException(IOException e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onError(WeiboException e) {
        // TODO Auto-generated method stub

    }

}
