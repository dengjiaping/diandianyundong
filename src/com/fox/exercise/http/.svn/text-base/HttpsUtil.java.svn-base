package com.fox.exercise.http;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;

public class HttpsUtil {
    static TrustManager[] xtmArray = new MytmArray[]{new MytmArray()};
    private final static int CONNENT_TIMEOUT = 15000;
    private final static int READ_TIMEOUT = 15000;
    private static String mCookie;
    public static int httpsResponseCode;

    static HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    };

    private static void trustAllHosts() {
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, xtmArray, new java.security.SecureRandom());
            HttpsURLConnection
                    .setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String HttpsPost(String httpsurl, String data) {
        String result = null;
        HttpURLConnection http = null;
        URL url;
        try {
            url = new URL(httpsurl);
            if (url.getProtocol().toLowerCase().equals("https")) {
                trustAllHosts();
                http = (HttpsURLConnection) url.openConnection();
                ((HttpsURLConnection) http).setHostnameVerifier(DO_NOT_VERIFY);
            } else {
                http = (HttpURLConnection) url.openConnection();
            }

            http.setConnectTimeout(CONNENT_TIMEOUT);
            http.setReadTimeout(READ_TIMEOUT);
            if (data == null) {
                http.setRequestMethod("GET");
                http.setDoInput(true);
                // http.setRequestProperty("Content-Type", "text/xml");
                if (mCookie != null)
                    http.setRequestProperty("Cookie", mCookie);
            } else {
                http.setRequestMethod("POST");
                http.setDoInput(true);
                http.setDoOutput(true);
                // http.setRequestProperty("Content-Type", "text/xml");
                if (mCookie != null && mCookie.trim().length() > 0)
                    http.setRequestProperty("Cookie", mCookie);

                DataOutputStream out = new DataOutputStream(
                        http.getOutputStream());
                out.writeBytes(data);
                out.flush();
                out.close();
            }

            httpsResponseCode = http.getResponseCode();
            BufferedReader in = null;
            if (httpsResponseCode == 200) {
                getCookie(http);
                in = new BufferedReader(new InputStreamReader(
                        http.getInputStream()));
            } else
                in = new BufferedReader(new InputStreamReader(
                        http.getErrorStream()));
            String temp = in.readLine();
            while (temp != null) {
                if (result != null)
                    result += temp;
                else
                    result = temp;
                temp = in.readLine();
            }
            in.close();
            http.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private static void getCookie(HttpURLConnection http) {
        String cookieVal = null;
        String key = null;
        mCookie = "";
        for (int i = 1; (key = http.getHeaderFieldKey(i)) != null; i++) {
            if (key.equalsIgnoreCase("set-cookie")) {
                cookieVal = http.getHeaderField(i);
                cookieVal = cookieVal.substring(0, cookieVal.indexOf(";"));
                mCookie = mCookie + cookieVal + ";";
            }
        }
    }
}