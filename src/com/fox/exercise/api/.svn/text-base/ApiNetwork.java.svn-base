package com.fox.exercise.api;

import java.io.UnsupportedEncodingException;
import java.net.SocketTimeoutException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class ApiNetwork {
    public static ApiMessage post(String url, String data) {
        StringEntity se = null;
        try {
            se = new StringEntity("content=" + data, HTTP.UTF_8);
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        se.setContentType("application/x-www-form-urlencoded");
        return postByEntity(url, se);

    }

    public static ApiMessage postByEntity(String url, StringEntity se) {
        // TODO Auto-generated method stub
        String returnContent = "";
        ApiMessage message = new ApiMessage();
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(url);
        httpclient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 30*1000);
        httpclient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 30*1000);
        try {
            httppost.setEntity(se);
            HttpResponse response = httpclient.execute(httppost);
            int status = response.getStatusLine().getStatusCode();

            String returnXML = EntityUtils.toString(response.getEntity());
            returnContent = returnXML;
            switch (status) {
                case 200:
                    message.setFlag(true);
                    message.setMsg(returnContent);
                    break;
                default:
                    Log.e("error", status + returnContent);
                    message.setFlag(false);
                    message.setMsg(status + returnContent);
                    break;
            }
        }catch (ConnectTimeoutException e) {
            e.printStackTrace();
                message.setFlag(false);
                message.setMsg("SocketTimeoutException");
        }catch (SocketTimeoutException e) {
            e.printStackTrace();
                message.setFlag(false);
                message.setMsg("SocketTimeoutException");
        }  catch (Exception e) {
            e.printStackTrace();
        }
        return message;
    }

    public static ApiMessage get(String url) {
        String returnContent = "";
        ApiMessage message = new ApiMessage();
        HttpGet get = new HttpGet(url);
        HttpClient client = new DefaultHttpClient();
        try {
            HttpResponse response = client.execute(get);
            int status = response.getStatusLine().getStatusCode();
            String returnXML = EntityUtils.toString(response.getEntity());
            returnContent = returnXML;
            switch (status) {
                case 200:
                    message.setFlag(true);
                    message.setMsg(returnContent);
                    break;

                default:
                    Log.e("error", status + returnContent);
                    message.setFlag(false);
                    message.setMsg(status + returnContent);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("err", "unknown");
                returnContent = jsonObject.toString();
                message.setFlag(false);
                message.setMsg(returnContent);
            } catch (JSONException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }
        return message;
    }
}
