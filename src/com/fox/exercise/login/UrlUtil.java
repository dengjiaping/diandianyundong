package com.fox.exercise.login;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.zip.GZIPInputStream;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

public class UrlUtil {
    private static final String TAG = "Jingdong-Utils";

    public static Bundle parseUrl(String url) {
        // 忽略MalformedURLException
        try {
            URL u = new URL(url);
            Bundle b = decodeUrl(u.getQuery());
            b.putAll(decodeUrl(u.getRef()));
            return b;
        } catch (MalformedURLException e) {
            return new Bundle();
        }
    }

    private static Bundle decodeUrl(String s) {
        Bundle params = new Bundle();
        if (s != null) {
            String array[] = s.split("&");
            for (String parameter : array) {
                String v[] = parameter.split("=");
                try {
                    params.putString(URLDecoder.decode(v[0], JDConfigs.DEFAULT_CHARSET),
                            URLDecoder.decode(v[1], JDConfigs.DEFAULT_CHARSET));
                } catch (UnsupportedEncodingException e) {
                    Log.e(TAG, e.getMessage());
                }
            }
        }
        return params;
    }


    public static String encodeUrl(Bundle parameters) {
        if (parameters == null) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (String key : parameters.keySet()) {
            if (first)
                first = false;
            else
                sb.append("&");
            try {
                sb.append(URLEncoder.encode(key, JDConfigs.DEFAULT_CHARSET))
                        .append("=")
                        .append(URLEncoder.encode(parameters.getString(key),
                                JDConfigs.DEFAULT_CHARSET));
            } catch (UnsupportedEncodingException e) {
                Log.e(TAG, e.getMessage());
            }

        }
        return sb.toString();
    }


    public static String openUrl(String url, String method, Bundle params)
            throws MalformedURLException, IOException {
        return openUrl(url, method, params, true);
    }

    private static String buildGetURL(String url, String query) {
        if (TextUtils.isEmpty(query)) {
            return url;
        }

        if (url.contains("?")) {
            if (url.endsWith("?") || url.endsWith("&")) {
                url = url + query;
            } else {
                url = url + "&" + query;
            }
        } else {
            url = url + "?" + query;
        }

        return url;
    }

    public static String openUrl(String url, String method, Bundle params, boolean multipart)
            throws MalformedURLException, IOException {
        // multi-part http请求的随机字符串
        String strBoundary = "5h6ndRfv9rT8iSHsAEouNdArNfORhyTPfgfj8qYf";
        String endLine = "\r\n";

        OutputStream os;

        if (method.equals("GET")) {
            url = buildGetURL(url, encodeUrl(params));
        }
        Log.d(TAG, method + " URL: " + url);

        // 根据http和ssl协议获取不同的HTTPURLConnection，默认通过不安全的SSL证书
        HttpURLConnection conn = getConnection(url);

        conn.setRequestProperty("User-Agent", System.getProperties().
                getProperty("http.agent") + JDConfigs.USAGE_AGENT);
        if (!JDConfigs.GET_METHOD.equals(method)) {
            Bundle dataparams = new Bundle();

            // 设置字节数组形式的数据
            for (String key : params.keySet()) {
                Object object = params.get(key);
                if (object instanceof byte[]) {
                    dataparams.putByteArray(key, params.getByteArray(key));
                }
            }

            if (params.containsKey("access_token")) {
                String decoded_token =
                        URLDecoder.decode(params.getString("access_token"));
                params.putString("access_token", decoded_token);
            }

            conn.setRequestMethod("POST");

            // 认证和API调用设置不同的Conent-Type
            if (multipart) {
                conn.setRequestProperty(
                        "Content-Type",
                        "multipart/form-data;boundary=" + strBoundary);
            } else {
                conn.setRequestProperty("Content-Type",
                        "application/x-www-form-urlencoded;charset=UTF-8");
            }

            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Accept-Encoding", JDConfigs.ACCEPT_ENCODING);
            conn.connect();
            os = new BufferedOutputStream(conn.getOutputStream());

            if (multipart) {
                os.write(("--" + strBoundary + endLine).getBytes(JDConfigs.DEFAULT_CHARSET));
                os.write((encodePostBody(params, strBoundary)).getBytes(JDConfigs.DEFAULT_CHARSET));
                os.write((endLine + "--" + strBoundary + endLine)
                        .getBytes(JDConfigs.DEFAULT_CHARSET));
            } else {
                os.write(buildQuery(params, JDConfigs.DEFAULT_CHARSET).getBytes(
                        JDConfigs.DEFAULT_CHARSET));
            }

            if (multipart && !dataparams.isEmpty()) {
                for (String key : dataparams.keySet()) {
                    os.write(("Content-Disposition: form-data; filename=\"" + key + "\"" + endLine)
                            .getBytes());
                    os.write(("Content-Type: content/unknown" + endLine + endLine).getBytes());
                    os.write(dataparams.getByteArray(key));
                    os.write((endLine + "--" + strBoundary + endLine).getBytes());
                }
            }
            os.flush();
        }

        // 获取响应数据的字符集
        String charset = getResponseCharset(conn.getContentType());
        // 判断是否响应数据是gzip压缩的
        String header = conn.getHeaderField("Content-Encoding");
        boolean isGzip = false;
        if (header != null && header.toLowerCase().contains(JDConfigs.ACCEPT_ENCODING)) {
            isGzip = true;
        }
        String response = "";
        try {
            response = read(conn.getInputStream(), charset, isGzip);
        } catch (FileNotFoundException e) {
            // 响应出错时返回值
            response = read(conn.getErrorStream(), charset, isGzip);
        }
        return response;
    }

    public static String encodePostBody(Bundle parameters, String boundary) {
        if (parameters == null)
            return "";
        StringBuilder sb = new StringBuilder();

        for (String key : parameters.keySet()) {
            if (parameters.getByteArray(key) != null) {
                continue;
            }

            sb.append("Content-Disposition: form-data; name=\"" + key +
                    "\"\r\n\r\n" + parameters.getString(key));
            sb.append("\r\n" + "--" + boundary + "\r\n");
        }

        return sb.toString();
    }

    private static HttpURLConnection getConnection(String url) throws IOException {
        URL requestURL = new URL(url);
        if ("https".equals(requestURL.getProtocol())) {
            SSLContext ctx = null;
            try {
                ctx = SSLContext.getInstance("TLS");
                ctx.init(new KeyManager[0],
                        new TrustManager[]{
                                new X509TrustManager() {
                                    @Override
                                    public void checkClientTrusted(X509Certificate[] chain,
                                                                   String authType) throws CertificateException {

                                    }

                                    @Override
                                    public void checkServerTrusted(X509Certificate[] chain,
                                                                   String authType) throws CertificateException {

                                    }

                                    @Override
                                    public X509Certificate[] getAcceptedIssuers() {
                                        return null;
                                    }
                                }
                        },
                        new SecureRandom());
            } catch (Exception e) {
                throw new IOException(e.getMessage());
            }

            HttpsURLConnection conn = (HttpsURLConnection) requestURL.openConnection();
            conn.setSSLSocketFactory(ctx.getSocketFactory());
            conn.setHostnameVerifier(new HostnameVerifier() {
                public boolean verify(String hostname, SSLSession session) {
                    return true;// 默认都认证通过
                }
            });

            return conn;
        } else {
            HttpURLConnection conn = (HttpURLConnection) requestURL.openConnection();
            return conn;
        }
    }

    public static String buildQuery(Bundle param, String charset) {
        if (param == null || param.isEmpty()) {
            return null;
        }

        if (TextUtils.isEmpty(charset)) {
            charset = JDConfigs.DEFAULT_CHARSET;
        }

        StringBuilder query = new StringBuilder();
        boolean hasParam = false;
        for (String name : param.keySet()) {

            String value = param.getString(name);
            if (!(TextUtils.isEmpty(name) || TextUtils.isEmpty(value))) {
                if (hasParam) {
                    query.append("&");
                } else {
                    hasParam = true;
                }

                try {
                    query.append(name).append("=").append(URLEncoder.encode(value, charset));
                } catch (UnsupportedEncodingException e) {
                    Log.e(TAG, "buildQuery throws UnsupportedEncodingException!");
                    e.printStackTrace();
                }
            }
        }

        return query.toString();
    }

    private static String getResponseCharset(String contentType) {
        String charset = JDConfigs.DEFAULT_CHARSET;
        if (TextUtils.isEmpty(contentType)) {
            return charset;
        }

        String[] params = contentType.split(";");
        for (String param : params) {
            param = param.trim();
            if (!param.startsWith("charset")) {
                continue;
            }

            String[] pair = param.split("=", 2);
            if (pair.length == 2 && !TextUtils.isEmpty(pair[1])) {
                charset = pair[1].trim();
            }
        }

        return charset;
    }

    private static String read(InputStream in, String charset, boolean isGzip) throws IOException {
        StringBuilder sb = new StringBuilder();
        if (isGzip) {
            in = new GZIPInputStream(in);
        }

        BufferedReader r = new BufferedReader(new InputStreamReader(in, charset), 1000);
        for (String line = r.readLine(); line != null; line = r.readLine()) {
            sb.append(line);
        }
        in.close();
        return sb.toString();
    }
}
