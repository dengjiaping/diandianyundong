package com.fox.exercise.api;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import com.fox.exercise.AllWeiboInfo;
import com.fox.exercise.R;
import com.fox.exercise.SportsType;
import com.fox.exercise.api.entity.PeisuInfo;
import com.fox.exercise.newversion.bushutongji.BuShuTongJiDetail;
import com.fox.exercise.newversion.entity.SleepEffect;
import com.fox.exercise.util.SportTaskUtil;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import cn.ingenic.indroidsync.SportsApp;
import cn.ingenic.indroidsync.utils.DeviceUuidFactory;

public class Api {
    private static String end = "\r\n";
    private static String twoHyphens = "--";
    private static String boundary = "******";

    public static ApiMessage likesAndWav(int picId) {
        JSONObject localJSONObject = new JSONObject();
        ApiMessage info = new ApiMessage();
        try {
            String url = ApiConstant.DATA_URL + ApiConstant.picLikesWav;
            localJSONObject.put("picid", picId);
            info = ApiNetwork.post(url, localJSONObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }

    public static ApiMessage sendDeviceId(String deviceId) {
        JSONObject localJSONObject = new JSONObject();
        ApiMessage info = new ApiMessage();
        try {
            String url = ApiConstant.DATA_URL + ApiConstant.deviceId;
            localJSONObject.put("deviceid", deviceId);
            info = ApiNetwork.post(url, localJSONObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }

    // forget password
    public static ApiMessage forgetPwd(String email) {
        JSONObject localJSONObject = new JSONObject();
        ApiMessage info = new ApiMessage();
        try {
            String url = ApiConstant.DATA_URL + ApiConstant.userPwdForget;
            localJSONObject.put("email", email);
            info = ApiNetwork.post(url, localJSONObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }

    // 发送短信验证码
    public static ApiMessage sendPhoneMessage(String phone, String type) {
        JSONObject localJSONObject = new JSONObject();
        ApiMessage info = new ApiMessage();
        try {
            String url = ApiConstant.DATA_URL + ApiConstant.sendPhoneMessage;
            localJSONObject.put("phone", phone);
            localJSONObject.put("type", type);
            info = ApiNetwork.post(url, localJSONObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }

    public static ApiMessage getActDetail() {
        JSONObject localJSONObject = new JSONObject();
        ApiMessage info = new ApiMessage();
        try {
            String url = ApiConstant.DATA_URL + ApiConstant.homeDetail;
            info = ApiNetwork.post(url, localJSONObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }

    public static ApiMessage getHomeImg() {
        JSONObject localJSONObject = new JSONObject();
        ApiMessage info = new ApiMessage();
        try {
            String url = ApiConstant.DATA_URL + ApiConstant.homeImg;
            info = ApiNetwork.post(url, localJSONObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }

    // show labels
    public static ApiMessage getLabelAndImgs() {
        JSONObject localJSONObject = new JSONObject();
        ApiMessage info = new ApiMessage();
        try {
            String url = ApiConstant.DATA_URL + ApiConstant.homeShow;
            info = ApiNetwork.post(url, localJSONObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }

    // weibo register
    public static ApiMessage combineWeibo(String weiboType, String weiboName) {
        JSONObject localJSONObject = new JSONObject();
        ApiMessage info = new ApiMessage();
        try {
            String url = ApiConstant.DATA_URL + ApiConstant.weiboLogin;
            localJSONObject.put("weiboname", weiboName);
            localJSONObject.put("weibotype", weiboType);
            info = ApiNetwork.post(url, localJSONObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }

    // register
    public static ApiMessage register(String username, String pwd,
                                      String phone, String picAddress, String sex, int regfromtypeId) {
        String result = new String();
        ApiMessage message = new ApiMessage();
        try {
            URL url = new URL(ApiConstant.DATA_URL
                    + ApiConstant.userCreatebysport);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url
                    .openConnection();
            httpURLConnection.setChunkedStreamingMode(128 * 1024);// 128K
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setUseCaches(false);
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setRequestProperty("Connection", "Keep-Alive");
            httpURLConnection.setRequestProperty("Charset", "UTF-8");
            httpURLConnection.setRequestProperty("Content-Type",
                    "multipart/form-data;boundary=" + boundary);
            httpURLConnection.setConnectTimeout(30*1000);
            httpURLConnection.setReadTimeout(30*1000);
            DataOutputStream dos = new DataOutputStream(
                    httpURLConnection.getOutputStream());
            dos.writeBytes(twoHyphens + boundary + end);
            dos.writeBytes("Content-Disposition: form-data; name=\"name\""
                    + end);
            dos.writeBytes(end);
            dos.write(username.getBytes());
            dos.writeBytes(end);
            dos.writeBytes(twoHyphens + boundary + end);
            dos.writeBytes("Content-Disposition: form-data; name=\"passwd\""
                    + end);
            dos.writeBytes(end);
            dos.writeBytes(pwd);
            dos.writeBytes(end);
            dos.writeBytes(twoHyphens + boundary + end);
            dos.writeBytes("Content-Disposition: form-data; name=\"email\""
                    + end);
            dos.writeBytes(end);
            dos.writeBytes("");
            dos.writeBytes(end);
            dos.writeBytes(twoHyphens + boundary + end);
            dos.writeBytes("Content-Disposition: form-data; name=\"phoneno\""
                    + end);
            dos.writeBytes(end);
            dos.writeBytes(phone);
            dos.writeBytes(end);
            if (!"".equals(sex) || sex != null) {
                dos.writeBytes(twoHyphens + boundary + end);
                dos.writeBytes("Content-Disposition: form-data; name=\"sex\""
                        + end);
                dos.writeBytes(end);
                dos.writeBytes(sex);
                dos.writeBytes(end);
            }
            String regfromtype = Integer.toString(regfromtypeId);
            if (!"".equals(regfromtype) || regfromtype != null) {
                dos.writeBytes(twoHyphens + boundary + end);
                dos.writeBytes("Content-Disposition: form-data; name=\"regfromtype\""
                        + end);
                dos.writeBytes(end);
                dos.writeBytes(regfromtype);
                dos.writeBytes(end);
            }
            if (!"".equals(picAddress)) {
                dos.writeBytes(twoHyphens + boundary + end);

                dos.writeBytes("Content-Disposition: form-data; name=\"img\"; filename=\""
                        + picAddress.substring(picAddress.lastIndexOf("/") + 1)
                        + "\"" + end);
                dos.writeBytes(end);

                FileInputStream fis = new FileInputStream(picAddress);
                byte[] buffer = new byte[8192]; // 8k
                int count = 0;
                while ((count = fis.read(buffer)) != -1) {
                    dos.write(buffer, 0, count);
                }
                fis.close();

                dos.writeBytes(end);
            }

            dos.writeBytes(twoHyphens + boundary + end);
            dos.writeBytes("Content-Disposition: form-data; name=\"datasource\""
                    + end);
            dos.writeBytes(end);
            dos.writeBytes("z"
                    + SportsApp.getInstance().getResources().getString(
                    R.string.config_game_id));
            dos.writeBytes(end);

            dos.writeBytes(twoHyphens + boundary + end);
            dos.writeBytes("Content-Disposition: form-data; name=\"serial\"" + end);
            dos.writeBytes(end);
            dos.writeBytes(DeviceUuidFactory.getDeviceSerial());
            dos.writeBytes(end);

            dos.writeBytes(twoHyphens + boundary + twoHyphens + end);
            dos.flush();

            InputStream is = httpURLConnection.getInputStream();
            InputStreamReader isr = new InputStreamReader(is, "utf-8");
            BufferedReader br = new BufferedReader(isr);
            result = br.readLine();
            if (result != null && !"".equals(result)) {
                message.setFlag(true);
                message.setMsg(result);
                // back = ApiJsonParser.ApiParserBack(result);
            } else {
                message.setFlag(false);
                message.setMsg("error");
            }
            dos.close();
            is.close();

        }catch (ConnectTimeoutException e) {
            e.printStackTrace();
                message.setFlag(false);
                message.setMsg("SocketTimeoutException");
        }catch (SocketTimeoutException e) {
            e.printStackTrace();
                message.setFlag(false);
                message.setMsg("SocketTimeoutException");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return message;
    }

    // login
    public static ApiMessage login(String account, String pwd, int wrlogin, String umengDeviceToken) {
        Log.e("develop_debug", "from login umengDeviceToken : " + umengDeviceToken);
        JSONObject localJSONObject = new JSONObject();
        ApiMessage info = new ApiMessage();
        try {
            String url = ApiConstant.DATA_URL + ApiConstant.userLogin;
            localJSONObject.put("name", account);
            localJSONObject.put("passwd", pwd);
            localJSONObject.put("wrlogin", wrlogin);
            localJSONObject.put("firststatus", 1);
            localJSONObject.put("datasource", "android");
            localJSONObject.put("serial", DeviceUuidFactory.getDeviceSerial());
            localJSONObject.put("device_token", umengDeviceToken);
            info = ApiNetwork.post(url, localJSONObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;

    }

    // uploadpic
    public static ApiMessage uploadImg(String picTitle, String picAddress,
                                       String sessionId, List<String> labels) {
        return uploadImg(picTitle, picAddress, sessionId, labels, "", 0);
    }

    // uploadpic
    public static ApiMessage uploadImg(String picTitle, String picAddress,
                                       String sessionId, List<String> labels, String wav, int wavDurations) {
        String result = new String();
        ApiMessage message = new ApiMessage();
        String label = new String();
        for (String s : labels) {
            label += s + ":";
        }
        label = label.substring(0, label.length() - 1);
        try {
            URL url = new URL(ApiConstant.DATA_URL + ApiConstant.uploadImg);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url
                    .openConnection();
            httpURLConnection.setChunkedStreamingMode(128 * 1024);// 128K
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setUseCaches(false);
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setRequestProperty("Connection", "Keep-Alive");
            httpURLConnection.setRequestProperty("Charset", HTTP.UTF_8);
            httpURLConnection.setRequestProperty("Content-Type",
                    "multipart/form-data;boundary=" + boundary);

            DataOutputStream dos = new DataOutputStream(
                    httpURLConnection.getOutputStream());
            dos.writeBytes(twoHyphens + boundary + end);
            dos.writeBytes("Content-Disposition: form-data; name=\"title\""
                    + end);
            dos.writeBytes(end);
            dos.write(picTitle.getBytes());
            // dos.writeBytes(picTitle.getBytes());
            dos.writeBytes(end);
            if (!"".equals(wav)) {
                dos.writeBytes(twoHyphens + boundary + end);

                dos.writeBytes("Content-Disposition: form-data; name=\"wav\"; filename=\""
                        + wav.substring(wav.lastIndexOf(".")) + "\"" + end);
                dos.writeBytes(end);

                FileInputStream fis = new FileInputStream(wav);
                byte[] buffer = new byte[8192]; // 8k
                int count = 0;
                while ((count = fis.read(buffer)) != -1) {
                    dos.write(buffer, 0, count);
                }
                fis.close();

                dos.writeBytes(end);
            }
            dos.writeBytes(twoHyphens + boundary + end);
            dos.writeBytes("Content-Disposition: form-data; name=\"uid\"" + end);
            dos.writeBytes(end);
            dos.writeBytes(sessionId);
            dos.writeBytes(end);
            dos.writeBytes(twoHyphens + boundary + end);
            dos.writeBytes("Content-Disposition: form-data; name=\"wavdurations\""
                    + end);
            dos.writeBytes(end);
            dos.writeBytes(Integer.toString(wavDurations));
            dos.writeBytes(end);
            dos.writeBytes(twoHyphens + boundary + end);
            dos.writeBytes("Content-Disposition: form-data; name=\"labels\""
                    + end);
            dos.writeBytes(end);
            dos.writeBytes(label);
            dos.writeBytes(end);
            dos.writeBytes(twoHyphens + boundary + end);

            dos.writeBytes("Content-Disposition: form-data; name=\"img\"; filename=\""
                    + picAddress.substring(picAddress.lastIndexOf("/") + 1)
                    + "\"" + end);
            dos.writeBytes(end);

            FileInputStream fis = new FileInputStream(picAddress);
            byte[] buffer = new byte[8192]; // 8k
            int count = 0;
            while ((count = fis.read(buffer)) != -1) {
                dos.write(buffer, 0, count);
            }
            fis.close();

            dos.writeBytes(end);
            dos.writeBytes(twoHyphens + boundary + twoHyphens + end);
            dos.flush();

            InputStream is = httpURLConnection.getInputStream();
            InputStreamReader isr = new InputStreamReader(is, "utf-8");
            BufferedReader br = new BufferedReader(isr);
            result = br.readLine();
            if (result != null && !"".equals(result)) {
                message.setFlag(true);
                message.setMsg(result);
                // back = ApiJsonParser.ApiParserBack(result);
            } else {
                message.setFlag(false);
                message.setMsg("error");
            }
            dos.close();
            is.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return message;
    }

    // ads msgs
    public static ApiMessage getAds() {
        JSONObject localJSONObject = new JSONObject();
        ApiMessage info = new ApiMessage();
        try {
            String url = ApiConstant.DATA_URL + ApiConstant.squareAds;
            info = ApiNetwork.post(url, localJSONObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }

    // rank week
    public static ApiMessage rankWeek(String sessionId, int times) {
        JSONObject localJSONObject = new JSONObject();
        ApiMessage info = new ApiMessage();
        try {
            String url = ApiConstant.DATA_URL + ApiConstant.rankWeek;
            localJSONObject.put("uid", sessionId);
            localJSONObject.put("times", times);
            info = ApiNetwork.post(url, localJSONObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }

    // rank total
    public static ApiMessage rankTotal(String sessionId, int times) {
        JSONObject localJSONObject = new JSONObject();
        ApiMessage info = new ApiMessage();
        try {
            String url = ApiConstant.DATA_URL + ApiConstant.rankTotal;
            localJSONObject.put("uid", sessionId);
            localJSONObject.put("times", times);
            info = ApiNetwork.post(url, localJSONObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }

    // rank month
    public static ApiMessage rankMonth(String sessionId, int times) {
        JSONObject localJSONObject = new JSONObject();
        ApiMessage info = new ApiMessage();
        try {
            String url = ApiConstant.DATA_URL + ApiConstant.rankMonth;
            localJSONObject.put("uid", sessionId);
            localJSONObject.put("times", times);
            info = ApiNetwork.post(url, localJSONObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }

    // rank quarter
    public static ApiMessage rankQuarter(String sessionId, int times) {
        JSONObject localJSONObject = new JSONObject();
        ApiMessage info = new ApiMessage();
        try {
            String url = ApiConstant.DATA_URL + ApiConstant.rankQuarter;
            localJSONObject.put("uid", sessionId);
            localJSONObject.put("times", times);
            info = ApiNetwork.post(url, localJSONObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }

    // squres hots
    public static ApiMessage getHots(String sessionId, int times) {
        JSONObject localJSONObject = new JSONObject();
        ApiMessage info = new ApiMessage();
        try {
            String url = ApiConstant.DATA_URL + ApiConstant.squareHots;
            localJSONObject.put("uid", sessionId);
            localJSONObject.put("times", times);
            info = ApiNetwork.post(url, localJSONObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }

    // squres news
    public static ApiMessage getNews(String sessionId, int times) {
        JSONObject localJSONObject = new JSONObject();
        ApiMessage info = new ApiMessage();
        try {
            String url = ApiConstant.DATA_URL + ApiConstant.squareNews;
            localJSONObject.put("uid", sessionId);
            localJSONObject.put("times", times);
            info = ApiNetwork.post(url, localJSONObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }

    // squres label
    public static ApiMessage getLabelImgs(String sessionId, int times,
                                          int labelId) {
        JSONObject localJSONObject = new JSONObject();
        ApiMessage info = new ApiMessage();
        try {
            String url = ApiConstant.DATA_URL + ApiConstant.squareLabel;
            localJSONObject.put("uid", sessionId);
            localJSONObject.put("times", times);
            localJSONObject.put("cid", labelId);
            info = ApiNetwork.post(url, localJSONObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }

    // image detail
    public static ApiMessage getImgDetail(String sessionId, int picId) {
        JSONObject localJSONObject = new JSONObject();
        ApiMessage info = new ApiMessage();
        try {
            String url = ApiConstant.DATA_URL + ApiConstant.picDetail;
            localJSONObject.put("uid", sessionId);
            localJSONObject.put("picid", picId);
            info = ApiNetwork.post(url, localJSONObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }

    // img like
    public static ApiMessage likeImg(String sessionId, int picId) {
        JSONObject localJSONObject = new JSONObject();
        ApiMessage info = new ApiMessage();
        try {
            String url = ApiConstant.DATA_URL + ApiConstant.picLikeimg;
            localJSONObject.put("uid", sessionId);
            localJSONObject.put("picid", picId);
            info = ApiNetwork.post(url, localJSONObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }

    public static ApiMessage getLikes(int picId) {
        JSONObject localJSONObject = new JSONObject();
        ApiMessage info = new ApiMessage();
        try {
            String url = ApiConstant.DATA_URL + ApiConstant.picLikes;
            localJSONObject.put("picid", picId);
            info = ApiNetwork.post(url, localJSONObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }

    public static ApiMessage likeStatus(String sessionId, int picId) {
        JSONObject localJSONObject = new JSONObject();
        ApiMessage info = new ApiMessage();
        try {
            String url = ApiConstant.DATA_URL + ApiConstant.picStatus;
            localJSONObject.put("uid", sessionId);
            localJSONObject.put("picid", picId);
            info = ApiNetwork.post(url, localJSONObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }

    // see user details
    public static ApiMessage seeUser(String sessionId, int uid) {
        JSONObject localJSONObject = new JSONObject();
        ApiMessage info = new ApiMessage();
        try {
            String url = ApiConstant.DATA_URL + ApiConstant.userDetail;
            localJSONObject.put("sessionid", sessionId);
            localJSONObject.put("uid", uid);
            info = ApiNetwork.post(url, localJSONObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }

    // user searchuser
    public static ApiMessage getUsers(int times) {
        JSONObject localJSONObject = new JSONObject();
        ApiMessage info = new ApiMessage();
        try {
            String url = ApiConstant.DATA_URL + ApiConstant.searchUser;// m=user&a=searchuser
            localJSONObject.put("times", times);
            info = ApiNetwork.post(url, localJSONObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }

    // 搜索好友
    public static ApiMessage getSearchFriendsByName(int times, String name) {
        JSONObject localJSONObject = new JSONObject();
        ApiMessage info = new ApiMessage();
        try {
            String url = ApiConstant.DATA_URL + ApiConstant.getNearbyName;
            localJSONObject.put("times", times);
            localJSONObject.put("names", name);
            info = ApiNetwork.post(url, localJSONObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }

    // user searchbyname 附近人
    public static ApiMessage getUserByName(int times, String name, int uid) {
        JSONObject localJSONObject = new JSONObject();
        ApiMessage info = new ApiMessage();
        try {
            String url = ApiConstant.DATA_URL + ApiConstant.searchByName;// m=user&a=searchbyname
            localJSONObject.put("times", times); // m=user&a=searchbynameforsport
            localJSONObject.put("names", name);
            localJSONObject.put("uid", uid);
            info = ApiNetwork.post(url, localJSONObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }

    // user searchbyname 附近人
    public static ApiMessage getFriendByName(int times, String name, int uid) {
        JSONObject localJSONObject = new JSONObject();
        ApiMessage info = new ApiMessage();
        try {
            String url = ApiConstant.DATA_URL + ApiConstant.searchFriendByName;// m=user&a=searchbyname
            localJSONObject.put("times", times); // m=user&a=searchbynameforsport
            localJSONObject.put("names", name);
            localJSONObject.put("uid", uid);
            info = ApiNetwork.post(url, localJSONObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }

    // 搜索好友
    public static ApiMessage getFriendByNameNew(int times, String name,
                                                String sessionId) {
        JSONObject localJSONObject = new JSONObject();
        ApiMessage info = new ApiMessage();
        try {
            String url = ApiConstant.DATA_URL + ApiConstant.getFriendByName;// m=user&a=searchbyname
            localJSONObject.put("times", times); // m=user&a=searchbynameforsport
            localJSONObject.put("names", name);
            localJSONObject.put("sessionid", sessionId);
            info = ApiNetwork.post(url, localJSONObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }
    // 搜索粉丝
    public static ApiMessage getFansByNameNew(int times, String name,
                                                String sessionId) {
        JSONObject localJSONObject = new JSONObject();
        ApiMessage info = new ApiMessage();
        try {
            String url = ApiConstant.DATA_URL + ApiConstant.getFansByName;// m=user&a=searchbyname
            localJSONObject.put("times", times); // m=user&a=searchbynameforsport
            localJSONObject.put("names", name);
            localJSONObject.put("sessionid", sessionId);
            info = ApiNetwork.post(url, localJSONObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }


    // 约跑
    public static ApiMessage inviteSport(String sessionID, int uid, int ouid) {
        JSONObject localJSONObject = new JSONObject();
        ApiMessage info = new ApiMessage();
        try {
            String url = ApiConstant.DATA_URL + ApiConstant.invitesport;// m=user&a=searchbyname
            localJSONObject.put("sessionId", sessionID); // m=user&a=searchbynameforsport
            localJSONObject.put("uid", uid);
            localJSONObject.put("oppouid", ouid);
            info = ApiNetwork.post(url, localJSONObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }

    // user searchbypic
    public static ApiMessage getPicByPic(int times, String name) {
        JSONObject localJSONObject = new JSONObject();
        ApiMessage info = new ApiMessage();
        try {
            String url = ApiConstant.DATA_URL + ApiConstant.searchByPic;
            localJSONObject.put("times", times);
            localJSONObject.put("names", name);
            info = ApiNetwork.post(url, localJSONObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }

    // user searchpic
    public static ApiMessage getPics(int times) {
        JSONObject localJSONObject = new JSONObject();
        ApiMessage info = new ApiMessage();
        try {
            String url = ApiConstant.DATA_URL + ApiConstant.searchPic;
            localJSONObject.put("times", times);
            info = ApiNetwork.post(url, localJSONObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }

    // more refresh rank
    public static ApiMessage refreshRank(String sessionId) {
        JSONObject localJSONObject = new JSONObject();
        ApiMessage info = new ApiMessage();
        try {
            String url = ApiConstant.DATA_URL + ApiConstant.refreshrankbysport;
            localJSONObject.put("uid", sessionId);
            info = ApiNetwork.post(url, localJSONObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }

    // more modifymsg By sport llp
    public static ApiMessage modifymsg(String sessionId, String username,
                                       String oldPwd, String newPwd, String picAddress, String sex,
                                       String birthday, String phoneno, String email, String height,
                                       String weight, String province, String city, String area,
                                       String signature, String BMI) {
        String end = "\r\n";
        String twoHyphens = "--";
        String boundary = "******";
        String result = new String();
        ApiMessage message = new ApiMessage();
        try {
            URL url = new URL(ApiConstant.DATA_URL
                    + ApiConstant.userModifybysport);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url
                    .openConnection();
            //设置网络请求超时
            //httpURLConnection.setConnectTimeout(30*1000);
            httpURLConnection.setChunkedStreamingMode(128 * 1024);// 128K
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setUseCaches(false);
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setRequestProperty("Connection", "Keep-Alive");
            httpURLConnection.setRequestProperty("Charset", "UTF-8");
            httpURLConnection.setRequestProperty("Content-Type",
                    "multipart/form-data;boundary=" + boundary);

            DataOutputStream dos = new DataOutputStream(
                    httpURLConnection.getOutputStream());
            dos.writeBytes(twoHyphens + boundary + end);
            dos.writeBytes("Content-Disposition: form-data; name=\"sessionid\""
                    + end);
            dos.writeBytes(end);
            dos.writeBytes(sessionId);
            dos.writeBytes(end);
            if (!"".equals(username) || username != null) {
                dos.writeBytes(twoHyphens + boundary + end);
                dos.writeBytes("Content-Disposition: form-data; name=\"name\""
                        + end);
                dos.writeBytes(end);
                dos.write(username.getBytes());
                dos.writeBytes(end);
            }
            if (!"".equals(email) || email != null) {
                dos.writeBytes(twoHyphens + boundary + end);
                dos.writeBytes("Content-Disposition: form-data; name=\"email\""
                        + end);
                dos.writeBytes(end);
                dos.write(email.getBytes());
                dos.writeBytes(end);
            }
            if (!"".equals(birthday) || birthday != null) {
                dos.writeBytes(twoHyphens + boundary + end);
                dos.writeBytes("Content-Disposition: form-data; name=\"birthday\""
                        + end);
                dos.writeBytes(end);
                dos.writeBytes(birthday);
                dos.writeBytes(end);
            }
            if (!"".equals(sex) || sex != null) {
                dos.writeBytes(twoHyphens + boundary + end);
                dos.writeBytes("Content-Disposition: form-data; name=\"sex\""
                        + end);
                dos.writeBytes(end);
                dos.writeBytes(sex);
                dos.writeBytes(end);
            }
            if (!"".equals(oldPwd) || oldPwd != null) {
                dos.writeBytes(twoHyphens + boundary + end);
                dos.writeBytes("Content-Disposition: form-data; name=\"oldpwd\""
                        + end);
                dos.writeBytes(end);
                dos.writeBytes(oldPwd);
                dos.writeBytes(end);
            }
            if (!"".equals(newPwd) || newPwd != null) {
                dos.writeBytes(twoHyphens + boundary + end);
                dos.writeBytes("Content-Disposition: form-data; name=\"newpwd\""
                        + end);
                dos.writeBytes(end);
                dos.writeBytes(newPwd);
                dos.writeBytes(end);
            }
            if (!"".equals(phoneno) || phoneno != null) {
                dos.writeBytes(twoHyphens + boundary + end);
                dos.writeBytes("Content-Disposition: form-data; name=\"phoneno\""
                        + end);
                dos.writeBytes(end);
                dos.writeBytes(phoneno);
                dos.writeBytes(end);
            }
            if (!"".equals(height) || height != null) {
                dos.writeBytes(twoHyphens + boundary + end);
                dos.writeBytes("Content-Disposition: form-data; name=\"height\""
                        + end);
                dos.writeBytes(end);
                dos.writeBytes(height);
                dos.writeBytes(end);
            }
            if (!"".equals(weight) || weight != null) {
                dos.writeBytes(twoHyphens + boundary + end);
                dos.writeBytes("Content-Disposition: form-data; name=\"weight\""
                        + end);
                dos.writeBytes(end);
                dos.writeBytes(weight);
                dos.writeBytes(end);
            }
            if (!"".equals(province) || province != null) {
                dos.writeBytes(twoHyphens + boundary + end);
                dos.writeBytes("Content-Disposition: form-data; name=\"province\""
                        + end);
                dos.writeBytes(end);
                dos.write(province.getBytes());
                dos.writeBytes(end);
            }
            if (!"".equals(city) || city != null) {
                dos.writeBytes(twoHyphens + boundary + end);
                dos.writeBytes("Content-Disposition: form-data; name=\"city\""
                        + end);
                dos.writeBytes(end);
                dos.write(city.getBytes());
                dos.writeBytes(end);
            }
            if (!"".equals(area) || area != null) {
                dos.writeBytes(twoHyphens + boundary + end);
                dos.writeBytes("Content-Disposition: form-data; name=\"area\""
                        + end);
                dos.writeBytes(end);
                dos.write(area.getBytes());
                dos.writeBytes(end);
            }
            if (!"".equals(signature) || signature != null) {
                dos.writeBytes(twoHyphens + boundary + end);
                dos.writeBytes("Content-Disposition: form-data; name=\"signature\""
                        + end);
                dos.writeBytes(end);
                dos.write(signature.getBytes());
                dos.writeBytes(end);
            }
            if (!"".equals(BMI) || BMI != null) {
                dos.writeBytes(twoHyphens + boundary + end);
                dos.writeBytes("Content-Disposition: form-data; name=\"BMI\""
                        + end);
                dos.writeBytes(end);
                dos.writeBytes(BMI);
                dos.writeBytes(end);
            }
            if (!"".equals(picAddress)) {
                File f = new File(picAddress);
                if (f.exists()) {
                    dos.writeBytes(twoHyphens + boundary + end);

                    dos.writeBytes("Content-Disposition: form-data; name=\"img\"; filename=\""
                            + picAddress.substring(picAddress.lastIndexOf("/") + 1)
                            + "\"" + end);
                    dos.writeBytes(end);

                    FileInputStream fis = new FileInputStream(picAddress);
                    byte[] buffer = new byte[8192]; // 8k
                    int count = 0;
                    while ((count = fis.read(buffer)) != -1) {
                        dos.write(buffer, 0, count);
                    }
                    fis.close();

                    dos.writeBytes(end);
                } else {
                    dos.writeBytes(twoHyphens + boundary + end);

                    dos.writeBytes("Content-Disposition: form-data; name=\"img\"; filename=\""
                            + picAddress.substring(picAddress.lastIndexOf("/") + 1)
                            + "\"" + end);
                    dos.writeBytes(end);
                    // byte[] buffer = new byte[8192]; // 8k
                    // dos.write(buffer);
                    dos.writeBytes("");
                    dos.writeBytes(end);
                }

            }
            dos.writeBytes(twoHyphens + boundary + twoHyphens + end);
            dos.flush();

            InputStream is = httpURLConnection.getInputStream();
            InputStreamReader isr = new InputStreamReader(is, "utf-8");
            BufferedReader br = new BufferedReader(isr);
            result = br.readLine();
            Log.e("API", "result" + result);
            if (result != null && !"".equals(result)) {
                message.setFlag(true);
                message.setMsg(result);
                // back = ApiJsonParser.ApiParserBack(result);
            } else {
                message.setFlag(false);
                message.setMsg("error");
            }
            dos.close();
            is.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return message;
    }

    // more deleteimg
    public static ApiMessage deleteImg(String sessionId, List<Integer> picIds) {
        JSONObject localJSONObject = new JSONObject();
        ApiMessage info = new ApiMessage();
        JSONArray localJSONArray = new JSONArray();
        try {
            String url = ApiConstant.DATA_URL + ApiConstant.deleteimg;
            localJSONObject.put("uid", sessionId);
            for (int i = 0; i < picIds.size(); i++) {
                localJSONArray.put(i, picIds.get(i));
            }
            localJSONObject.put("pic", localJSONArray);
            info = ApiNetwork.post(url, localJSONObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }

    // more userpics
    public static ApiMessage getUserPics(String sessionId, int times) {
        JSONObject localJSONObject = new JSONObject();
        ApiMessage info = new ApiMessage();
        try {
            String url = ApiConstant.DATA_URL + ApiConstant.userpics;
            localJSONObject.put("uid", sessionId);
            localJSONObject.put("times", times);
            info = ApiNetwork.post(url, localJSONObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }

    // submit comments
    public static ApiMessage upComments(String sessionId, int picId,
                                        String comments, String wav, int wavDurations, int touid) {
        String end = "\r\n";
        String twoHyphens = "--";
        String boundary = "******";
        String result = new String();
        ApiMessage message = new ApiMessage();
        try {
            URL url = new URL(ApiConstant.DATA_URL + ApiConstant.uploadComment);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url
                    .openConnection();
            httpURLConnection.setChunkedStreamingMode(128 * 1024);// 128K
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setUseCaches(false);
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setRequestProperty("Connection", "Keep-Alive");
            httpURLConnection.setRequestProperty("Charset", "UTF-8");
            httpURLConnection.setRequestProperty("Content-Type",
                    "multipart/form-data;boundary=" + boundary);

            DataOutputStream dos = new DataOutputStream(
                    httpURLConnection.getOutputStream());
            dos.writeBytes(twoHyphens + boundary + end);
            dos.writeBytes("Content-Disposition: form-data; name=\"sessionid\""
                    + end);
            dos.writeBytes(end);
            dos.writeBytes(sessionId);
            dos.writeBytes(end);
            if (!"".equals(comments) || comments != null) {
                dos.writeBytes(twoHyphens + boundary + end);
                dos.writeBytes("Content-Disposition: form-data; name=\"comment\""
                        + end);
                dos.writeBytes(end);
                dos.write(comments.getBytes());
                dos.writeBytes(end);
            }
            String pic = Integer.toString(picId);
            if (!"".equals(pic) || pic != null) {
                dos.writeBytes(twoHyphens + boundary + end);
                dos.writeBytes("Content-Disposition: form-data; name=\"pid\""
                        + end);
                dos.writeBytes(end);
                dos.writeBytes(pic);
                dos.writeBytes(end);
            }
            String toUid = Integer.toString(touid);
            if (!"".equals(toUid) || toUid != null) {
                dos.writeBytes(twoHyphens + boundary + end);
                dos.writeBytes("Content-Disposition: form-data; name=\"touid\""
                        + end);
                dos.writeBytes(end);
                dos.writeBytes(toUid);
                dos.writeBytes(end);
            }
            String durations = Integer.toString(wavDurations);
            if (!"".equals(durations) || durations != null) {
                dos.writeBytes(twoHyphens + boundary + end);
                dos.writeBytes("Content-Disposition: form-data; name=\"wavdurations\""
                        + end);
                dos.writeBytes(end);
                dos.writeBytes(durations);
                dos.writeBytes(end);
            }

            if (!"".equals(wav)) {
                dos.writeBytes(twoHyphens + boundary + end);

                dos.writeBytes("Content-Disposition: form-data; name=\"wav\"; filename=\""
                        + wav.substring(wav.lastIndexOf(".")) + "\"" + end);
                dos.writeBytes(end);

                FileInputStream fis = new FileInputStream(wav);
                byte[] buffer = new byte[8192]; // 8k
                int count = 0;
                while ((count = fis.read(buffer)) != -1) {
                    dos.write(buffer, 0, count);
                }
                fis.close();

                dos.writeBytes(end);
            }
            dos.writeBytes(twoHyphens + boundary + twoHyphens + end);
            dos.flush();

            InputStream is = httpURLConnection.getInputStream();
            InputStreamReader isr = new InputStreamReader(is, "utf-8");
            BufferedReader br = new BufferedReader(isr);
            result = br.readLine();
            if (result != null && !"".equals(result)) {
                message.setFlag(true);
                message.setMsg(result);
                // back = ApiJsonParser.ApiParserBack(result);
            } else {
                message.setFlag(false);
                message.setMsg("error");
            }
            dos.close();
            is.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return message;
    }

    public static ApiMessage getNewComment(int picId) {
        JSONObject localJSONObject = new JSONObject();
        ApiMessage info = new ApiMessage();
        try {
            String url = ApiConstant.DATA_URL + ApiConstant.newComment;
            localJSONObject.put("pid", picId);
            info = ApiNetwork.post(url, localJSONObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }

    // get comments
    public static ApiMessage getComments(int picId, int times, int id) {
        JSONObject localJSONObject = new JSONObject();
        ApiMessage info = new ApiMessage();
        try {
            String url = ApiConstant.DATA_URL + ApiConstant.getComment;
            localJSONObject.put("pid", picId);
            localJSONObject.put("times", times);
            localJSONObject.put("id", id);
            info = ApiNetwork.post(url, localJSONObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }

    // run wav
    public static ApiMessage runWav(String sessionId, int picId) {
        JSONObject localJSONObject = new JSONObject();
        ApiMessage info = new ApiMessage();
        try {
            String url = ApiConstant.DATA_URL + ApiConstant.runWav;
            localJSONObject.put("uid", sessionId);
            localJSONObject.put("picid", picId);
            info = ApiNetwork.post(url, localJSONObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }

    // follow person
    // 1——关注 2——取消关注
    public static ApiMessage follow(String sessionId, int uid, int status,
                                    int issports) {
        JSONObject localJSONObject = new JSONObject();
        ApiMessage info = new ApiMessage();
        try {
            String url = ApiConstant.DATA_URL + ApiConstant.followPerson;
            localJSONObject.put("sessionId", sessionId);
            localJSONObject.put("uid", uid);
            localJSONObject.put("status", status);
            localJSONObject.put("issports", issports);
            info = ApiNetwork.post(url, localJSONObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }

    // give gift 送礼物
    public static ApiMessage giveGift(String sessionId, int uid, int giftId,
                                      int golds) {
        JSONObject localJSONObject = new JSONObject();
        ApiMessage info = new ApiMessage();
        try {
            String url = ApiConstant.DATA_URL + ApiConstant.giveGift;
            localJSONObject.put("sessionId", sessionId);
            localJSONObject.put("uid", uid);
            localJSONObject.put("giftId", giftId);
            localJSONObject.put("golds", golds);
            info = ApiNetwork.post(url, localJSONObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }

    // get gifts 查询所有礼物
    public static ApiMessage getGifts() {
        JSONObject localJSONObject = new JSONObject();
        ApiMessage info = new ApiMessage();
        try {
            String url = ApiConstant.DATA_URL + ApiConstant.getGifts;
            info = ApiNetwork.post(url, localJSONObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }

    // gold coin change
    // coinStatus、goldStatus 1——增加,2——减少、0——不变
    // coins、golds为增加或减少的数量
    public static ApiMessage coingoldChange(String sessionId, int coinStatus,
                                            int coins, int goldStatus, int golds, int actionStyle,
                                            int sportsType) {
        JSONObject localJSONObject = new JSONObject();
        ApiMessage info = new ApiMessage();
        try {
            String url = ApiConstant.DATA_URL + ApiConstant.coingoldChange;
            localJSONObject.put("sessionId", sessionId);
            localJSONObject.put("coinStatus", coinStatus);
            localJSONObject.put("coins", coins);
            localJSONObject.put("goldStatus", goldStatus);
            localJSONObject.put("golds", golds);
            localJSONObject.put("log", actionStyle);
            localJSONObject.put("datasource", "android");
            if (sportsType != -1) {
                localJSONObject.put("Sports_type_task", sportsType);
            }
            info = ApiNetwork.post(url, localJSONObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }

    // upload local 上传位置信息
    // lat纬度 lng经度 保留小数点后6位
    public static ApiMessage uploadLocal(String sessionId, String lat,
                                         String lng) {
        JSONObject localJSONObject = new JSONObject();
        ApiMessage info = new ApiMessage();
        try {
            String url = ApiConstant.DATA_URL + ApiConstant.uploadLocal;
            localJSONObject.put("sessionId", sessionId);
            localJSONObject.put("lat", lat);
            localJSONObject.put("lng", lng);
            info = ApiNetwork.post(url, localJSONObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }

    private static final String TAG = "NearbyActivity";

    // getNearby 获得最近的人
    public static ApiMessage getNearby(int times, String sessionId) {
        JSONObject localJSONObject = new JSONObject();
        ApiMessage info = new ApiMessage();
        try {
            String url = ApiConstant.DATA_URL + ApiConstant.getNearby;// m=third&a=getnearbysports
            localJSONObject.put("times", times);
            localJSONObject.put("sessionId", sessionId);
            Log.d(TAG, "*******检测(*******" + localJSONObject.toString());
            info = ApiNetwork.post(url, localJSONObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }

    // getNearbyNew 获得最近的人
    public static ApiMessage getNearbyNew(String sessionId) {
        JSONObject localJSONObject = new JSONObject();
        ApiMessage info = new ApiMessage();
        try {
            String url = ApiConstant.DATA_URL + ApiConstant.getNearbyNew;// m=third&a=getnearbysports
            localJSONObject.put("sessionId", sessionId);
            Log.d(TAG, "*******检测(*******" + localJSONObject.toString());
            info = ApiNetwork.post(url, localJSONObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }

    // getNearbyBysex 获得最近的人，区分性别
    public static ApiMessage getNearbyBysex(int times, String sessionId, int sex) {
        JSONObject localJSONObject = new JSONObject();
        ApiMessage info = new ApiMessage();
        try {
            String url = ApiConstant.DATA_URL + ApiConstant.getNearbyBysex;// m=third&a=getnearbysports
            localJSONObject.put("times", times);
            localJSONObject.put("sessionId", sessionId);
            localJSONObject.put("sex", sex);
            Log.d(TAG, "*******检测(*******" + localJSONObject.toString());
            info = ApiNetwork.post(url, localJSONObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }

    // getNearbyName 获得最近的人-------搜索使用
    public static ApiMessage getNearbyName(int times, String name) {// searchuser/searchbyname
        JSONObject localJSONObject = new JSONObject();
        ApiMessage info = new ApiMessage();
        try {
            String url = ApiConstant.DATA_URL + ApiConstant.getNearby;// m=user&a=searchbyname
            localJSONObject.put("times", times);
            localJSONObject.put("names", name);
            info = ApiNetwork.post(url, localJSONObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }

    // getFriend 获得朋友列表
    public static ApiMessage getFriend(String sessionId) {
        JSONObject localJSONObject = new JSONObject();
        ApiMessage info = new ApiMessage();
        try {
            String url = ApiConstant.DATA_URL + ApiConstant.getFriend;// m=third&a=getnearbysports
            localJSONObject.put("sessionId", sessionId);
            Log.d(TAG, "*******检测(*******" + localJSONObject.toString());
            info = ApiNetwork.post(url, localJSONObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }

    // getFriend 获得朋友列表
    public static ApiMessage getFriendList(int times, String sessionId) {
        JSONObject localJSONObject = new JSONObject();
        ApiMessage info = new ApiMessage();
        try {
            String url = ApiConstant.DATA_URL + ApiConstant.getFriend;// m=third&a=getnearbysports
            localJSONObject.put("times", times);
            localJSONObject.put("sessionId", sessionId);
            Log.d(TAG, "*******检测(*******" + localJSONObject.toString());
            info = ApiNetwork.post(url, localJSONObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }

    // getFriend 获得粉丝列表
    public static ApiMessage getFansList(int times, String sessionId) {
        JSONObject localJSONObject = new JSONObject();
        ApiMessage info = new ApiMessage();
        try {
            String url = ApiConstant.DATA_URL + ApiConstant.getFans;
            localJSONObject.put("times", times);
            localJSONObject.put("sessionId", sessionId);
            info = ApiNetwork.post(url, localJSONObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }

    // 查看关注的人
    public static ApiMessage userFollow(String sessionId, int uid, int times) {
        JSONObject localJSONObject = new JSONObject();
        ApiMessage info = new ApiMessage();
        try {
            String url = ApiConstant.DATA_URL + ApiConstant.userfollow;
            localJSONObject.put("sessionId", sessionId);
            localJSONObject.put("uid", uid);
            localJSONObject.put("times", times);
            info = ApiNetwork.post(url, localJSONObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }

    // 查看我的粉丝
    public static ApiMessage userFan(String sessionId, int uid, int times) {
        JSONObject localJSONObject = new JSONObject();
        ApiMessage info = new ApiMessage();
        try {
            String url = ApiConstant.DATA_URL + ApiConstant.userFan;
            localJSONObject.put("sessionId", sessionId);
            localJSONObject.put("uid", uid);
            localJSONObject.put("times", times);
            info = ApiNetwork.post(url, localJSONObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }

    //
    // 查看收到的礼物
    public static ApiMessage userGift(String sessionId, int uid, int times) {
        JSONObject localJSONObject = new JSONObject();
        ApiMessage info = new ApiMessage();
        try {
            String url = ApiConstant.DATA_URL + ApiConstant.userGift;
            localJSONObject.put("sessionId", sessionId);
            localJSONObject.put("uid", uid);
            localJSONObject.put("times", times);
            info = ApiNetwork.post(url, localJSONObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }

    public static ApiMessage modifyLauncher(String sessionId, String username,
                                            String phoneno, String sex, String birthday) {
        JSONObject localJSONObject = new JSONObject();
        ApiMessage info = new ApiMessage();
        try {
            String url = ApiConstant.DATA_URL + ApiConstant.modifyLauncher;
            localJSONObject.put("sessionid", sessionId);
            localJSONObject.put("name", username);
            localJSONObject.put("phoneno", phoneno);
            localJSONObject.put("birthday", birthday);
            localJSONObject.put("sex", sex);
            info = ApiNetwork.post(url, localJSONObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }

    public static ApiMessage getMeimeiMsg(String sessionId) {
        JSONObject localJSONObject = new JSONObject();
        ApiMessage info = new ApiMessage();
        try {
            String url = ApiConstant.DATA_URL + ApiConstant.weiboMeimei;
            localJSONObject.put("sessionid", sessionId);
            info = ApiNetwork.post(url, localJSONObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }

    public static ApiMessage getLauncherMsg(String sessionId) {
        JSONObject localJSONObject = new JSONObject();
        ApiMessage info = new ApiMessage();
        try {
            String url = ApiConstant.DATA_URL + ApiConstant.weiboLauncher;
            localJSONObject.put("sessionid", sessionId);
            info = ApiNetwork.post(url, localJSONObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }

    public static ApiMessage combineLauncherWeibo(String weiboType,
                                                  String weiboName) {
        JSONObject localJSONObject = new JSONObject();
        ApiMessage info = new ApiMessage();
        try {
            String url = ApiConstant.DATA_URL + ApiConstant.weiboLauncherLogin;
            localJSONObject.put("weiboname", weiboName);
            localJSONObject.put("weibotype", weiboType);
            info = ApiNetwork.post(url, localJSONObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }

    public static ApiMessage refreshLauncherRank(String sessionId) {
        JSONObject localJSONObject = new JSONObject();
        ApiMessage info = new ApiMessage();
        try {
            String url = ApiConstant.DATA_URL + ApiConstant.refreshLauncherRank;
            localJSONObject.put("uid", sessionId);
            info = ApiNetwork.post(url, localJSONObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }

    public static ApiMessage combineWeibo_New(String weiboType,
                                              String weiboName, String token, int wrlogin,
                                              String umengDeviceToken) {
        Log.e("develop_debug", "from combineWeibo_New umengDeviceToken : " + umengDeviceToken);
        JSONObject localJSONObject = new JSONObject();
        ApiMessage info = new ApiMessage();
        try {
            String url = ApiConstant.DATA_URL + ApiConstant.weiboNewLogin;
            localJSONObject.put("weiboname", weiboName);
            localJSONObject.put("weibotype", weiboType);
            localJSONObject.put("wrlogin", wrlogin);
            localJSONObject.put("firststatus", 1);
            localJSONObject.put("weibono", token);
            localJSONObject.put("datasource", "z"
                    + SportsApp.getInstance().getResources().getString(
                    R.string.config_game_id));
            localJSONObject.put("serial", DeviceUuidFactory.getDeviceSerial());
            localJSONObject.put("device_token", umengDeviceToken);
            info = ApiNetwork.post(url, localJSONObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }

    // every day golds
    public static ApiMessage getdaygolds(String sessionId) {
        JSONObject localJSONObject = new JSONObject();
        ApiMessage info = new ApiMessage();
        try {
            String url = ApiConstant.DATA_URL + ApiConstant.getDayGolds;
            localJSONObject.put("sessionid", sessionId);
            info = ApiNetwork.post(url, localJSONObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }

    public static ApiMessage judgePic(String sessionId, int uid) {
        JSONObject localJSONObject = new JSONObject();
        ApiMessage info = new ApiMessage();
        try {
            String url = ApiConstant.DATA_URL + ApiConstant.judgePic;
            localJSONObject.put("sessionid", sessionId);
            localJSONObject.put("uid", uid);
            info = ApiNetwork.post(url, localJSONObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }

    // charmrank
    // sex 1——boy 2——girl
    // type:1——wealthrank，2——kindrank，3——newrank，4——charmrank
    // 5,upwealthsrank,6,upkindrank,7,upcharmsrank
    public static ApiMessage charmrank(int type, int times, int sex) {
        JSONObject localJSONObject = new JSONObject();
        ApiMessage info = new ApiMessage();
        try {
            String url = ApiConstant.DATA_URL + ApiConstant.charmrank;
            localJSONObject.put("type", type);
            localJSONObject.put("times", times);
            localJSONObject.put("sex", sex);
            info = ApiNetwork.post(url, localJSONObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }

    // visitor
    public static ApiMessage userVisitor(String sessionId, int times) {
        JSONObject localJSONObject = new JSONObject();
        ApiMessage info = new ApiMessage();
        try {
            String url = ApiConstant.DATA_URL + ApiConstant.userVisitor;
            localJSONObject.put("sessionId", sessionId);
            localJSONObject.put("times", times);
            info = ApiNetwork.post(url, localJSONObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }

    // follow person
    // 1——关注 2——取消关注
    public static ApiMessage visitor(String sessionId, int uid) {
        JSONObject localJSONObject = new JSONObject();
        ApiMessage info = new ApiMessage();
        try {
            String url = ApiConstant.DATA_URL + ApiConstant.visitor;
            localJSONObject.put("sessionId", sessionId);
            localJSONObject.put("uid", uid);
            info = ApiNetwork.post(url, localJSONObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }

    public static ApiMessage getCommentsMe(String sessionId, int times) {
        JSONObject localJSONObject = new JSONObject();
        ApiMessage info = new ApiMessage();
        try {
            String url = ApiConstant.DATA_URL + ApiConstant.getCommentsMe;
            localJSONObject.put("times", times);
            localJSONObject.put("sessionid", sessionId);
            info = ApiNetwork.post(url, localJSONObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }

    public static ApiMessage delComments(String sessionId, int commentId) {
        JSONObject localJSONObject = new JSONObject();
        ApiMessage info = new ApiMessage();
        try {
            String url = ApiConstant.DATA_URL + ApiConstant.delComments;
            localJSONObject.put("sessionid", sessionId);
            localJSONObject.put("commentid", commentId);
            info = ApiNetwork.post(url, localJSONObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }

    public static ApiMessage getMyLikes(String sessionId, int times) {
        JSONObject localJSONObject = new JSONObject();
        ApiMessage info = new ApiMessage();
        try {
            String url = ApiConstant.DATA_URL + ApiConstant.getMyLikes;
            localJSONObject.put("times", times);
            localJSONObject.put("sessionid", sessionId);
            info = ApiNetwork.post(url, localJSONObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }

    public static ApiMessage sendprimsg(String sessionId, int touid,
                                        String comments, String wav, int wavDurations) {
        String end = "\r\n";
        String twoHyphens = "--";
        String boundary = "******";
        String result = new String();
        ApiMessage message = new ApiMessage();
        try {
            URL url = new URL(ApiConstant.DATA_URL + ApiConstant.sendprimsg);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url
                    .openConnection();
            httpURLConnection.setChunkedStreamingMode(128 * 1024);// 128K
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setUseCaches(false);
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setRequestProperty("Connection", "Keep-Alive");
            httpURLConnection.setRequestProperty("Charset", "UTF-8");
            httpURLConnection.setRequestProperty("Content-Type",
                    "multipart/form-data;boundary=" + boundary);

            DataOutputStream dos = new DataOutputStream(
                    httpURLConnection.getOutputStream());
            dos.writeBytes(twoHyphens + boundary + end);
            dos.writeBytes("Content-Disposition: form-data; name=\"sessionid\""
                    + end);
            dos.writeBytes(end);
            dos.writeBytes(sessionId);
            dos.writeBytes(end);
            if (!"".equals(comments) || comments != null) {
                dos.writeBytes(twoHyphens + boundary + end);
                dos.writeBytes("Content-Disposition: form-data; name=\"comment\""
                        + end);
                dos.writeBytes(end);
                dos.write(comments.getBytes());
                dos.writeBytes(end);
            }
            String toid = Integer.toString(touid);
            if (!"".equals(toid) || toid != null) {
                dos.writeBytes(twoHyphens + boundary + end);
                dos.writeBytes("Content-Disposition: form-data; name=\"touid\""
                        + end);
                dos.writeBytes(end);
                dos.writeBytes(toid);
                dos.writeBytes(end);
            }
            String durations = Integer.toString(wavDurations);
            if (!"".equals(durations) || durations != null) {
                dos.writeBytes(twoHyphens + boundary + end);
                dos.writeBytes("Content-Disposition: form-data; name=\"wavdurations\""
                        + end);
                dos.writeBytes(end);
                dos.writeBytes(durations);
                dos.writeBytes(end);
            }

            if (!"".equals(wav)) {
                dos.writeBytes(twoHyphens + boundary + end);

                dos.writeBytes("Content-Disposition: form-data; name=\"wav\"; filename=\""
                        + wav.substring(wav.lastIndexOf(".")) + "\"" + end);
                dos.writeBytes(end);

                FileInputStream fis = new FileInputStream(wav);
                byte[] buffer = new byte[8192]; // 8k
                int count = 0;
                while ((count = fis.read(buffer)) != -1) {
                    dos.write(buffer, 0, count);
                }
                fis.close();

                dos.writeBytes(end);
            }
            dos.writeBytes(twoHyphens + boundary + twoHyphens + end);
            dos.flush();

            InputStream is = httpURLConnection.getInputStream();
            InputStreamReader isr = new InputStreamReader(is, "utf-8");
            BufferedReader br = new BufferedReader(isr);
            result = br.readLine();
            if (result != null && !"".equals(result)) {
                message.setFlag(true);
                message.setMsg(result);
                // back = ApiJsonParser.ApiParserBack(result);
            } else {
                message.setFlag(false);
                message.setMsg("error");
            }
            dos.close();
            is.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return message;
    }

    // msgbox getprimsgall
    public static ApiMessage getPrimsgAll(String sessionId, int times) {
        JSONObject localJSONObject = new JSONObject();
        ApiMessage info = new ApiMessage();
        try {
            String url = ApiConstant.DATA_URL + ApiConstant.getPrimsgAll;
            localJSONObject.put("times", times);
            localJSONObject.put("sessionid", sessionId);
            info = ApiNetwork.post(url, localJSONObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }

    // msgbox getprimsgone
    public static ApiMessage getPrimsgOne(String sessionId, int uid, int times) {
        JSONObject localJSONObject = new JSONObject();
        ApiMessage info = new ApiMessage();
        try {
            String url = ApiConstant.DATA_URL + ApiConstant.getPrimsgOne;
            localJSONObject.put("times", times);
            localJSONObject.put("uid", uid);
            localJSONObject.put("sessionid", sessionId);
            info = ApiNetwork.post(url, localJSONObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }

    // msgbox black people
    // status 1——拉黑 2——取消拉黑
    public static ApiMessage blackpeople(String sessionId, int uid, int status) {
        JSONObject localJSONObject = new JSONObject();
        ApiMessage info = new ApiMessage();
        try {
            String url = ApiConstant.DATA_URL + ApiConstant.blackPeople;
            localJSONObject.put("uid", uid);
            localJSONObject.put("sessionid", sessionId);
            localJSONObject.put("status", status);
            info = ApiNetwork.post(url, localJSONObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }

    // msgbox getblacklist
    public static ApiMessage getBlackList(String sessionId, int times) {
        JSONObject localJSONObject = new JSONObject();
        ApiMessage info = new ApiMessage();
        try {
            String url = ApiConstant.DATA_URL + ApiConstant.getBlackList;
            localJSONObject.put("times", times);
            localJSONObject.put("sessionid", sessionId);
            info = ApiNetwork.post(url, localJSONObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }

    // msgbox sunshine
    // level 1、2、3
    public static ApiMessage sunshine(String sessionId, int level) {
        JSONObject localJSONObject = new JSONObject();
        ApiMessage info = new ApiMessage();
        try {
            String url = ApiConstant.DATA_URL + ApiConstant.sunshine;
            localJSONObject.put("sessionid", sessionId);
            localJSONObject.put("level", level);
            info = ApiNetwork.post(url, localJSONObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }

    // msgbox getsunshine
    public static ApiMessage getSunshine(String sessionId, int times) {
        JSONObject localJSONObject = new JSONObject();
        ApiMessage info = new ApiMessage();
        try {
            String url = ApiConstant.DATA_URL + ApiConstant.getSunshine;
            localJSONObject.put("times", times);
            localJSONObject.put("sessionid", sessionId);
            info = ApiNetwork.post(url, localJSONObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }

    // msgbox sysmsg
    public static ApiMessage getSysmsg(String sessionId, int times) {
        JSONObject localJSONObject = new JSONObject();
        ApiMessage info = new ApiMessage();
        try {
            String url = ApiConstant.DATA_URL + ApiConstant.getSysmsg;
            localJSONObject.put("times", times);
            localJSONObject.put("sessionid", sessionId);
            info = ApiNetwork.post(url, localJSONObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }

    // msgbox getsunshine
    public static ApiMessage ffollow(String sessionId, int times) {
        JSONObject localJSONObject = new JSONObject();
        ApiMessage info = new ApiMessage();
        try {
            String url = ApiConstant.DATA_URL + ApiConstant.sportfollow;
            localJSONObject.put("times", times);
            localJSONObject.put("sessionid", sessionId);
            info = ApiNetwork.post(url, localJSONObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }

    // msgbox getsunshine
    public static ApiMessage fupload(String sessionId, int times) {
        JSONObject localJSONObject = new JSONObject();
        ApiMessage info = new ApiMessage();
        try {
            String url = ApiConstant.DATA_URL + ApiConstant.fupload;
            localJSONObject.put("times", times);
            localJSONObject.put("sessionid", sessionId);
            info = ApiNetwork.post(url, localJSONObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }

    // msgbox getsunshine
    public static ApiMessage fgift(String sessionId, int times) {
        JSONObject localJSONObject = new JSONObject();
        ApiMessage info = new ApiMessage();
        try {
            String url = ApiConstant.DATA_URL + ApiConstant.fgift;
            localJSONObject.put("times", times);
            localJSONObject.put("sessionid", sessionId);
            info = ApiNetwork.post(url, localJSONObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }

    // down pic
    public static ApiMessage downImg(String sessionId, int uid) {
        JSONObject localJSONObject = new JSONObject();
        ApiMessage info = new ApiMessage();
        try {
            String url = ApiConstant.DATA_URL + ApiConstant.downimg;
            localJSONObject.put("sessionId", sessionId);
            localJSONObject.put("uid", uid);
            info = ApiNetwork.post(url, localJSONObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }

    // activities
    public static ApiMessage activities(String sessionId, int channel) {
        JSONObject localJSONObject = new JSONObject();
        ApiMessage info = new ApiMessage();
        try {
            String url = ApiConstant.DATA_URL + ApiConstant.activities;
            localJSONObject.put("sessionId", sessionId);
            localJSONObject.put("channel", channel);
            localJSONObject.put("datasource", "android");
            info = ApiNetwork.post(url, localJSONObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }

    public static ApiMessage getMsgCounts(String sessionId) {
        JSONObject localJSONObject = new JSONObject();
        ApiMessage info = new ApiMessage();
        try {
            String url = ApiConstant.DATA_URL + ApiConstant.getmsgcounts;
            localJSONObject.put("sessionid", sessionId);
            info = ApiNetwork.post(url, localJSONObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }

    // get tradeno
    public static ApiMessage getTradeNo(String sessionId, int price) {
        JSONObject localJSONObject = new JSONObject();
        ApiMessage info = new ApiMessage();
        try {
            String url = ApiConstant.DATA_URL + ApiConstant.getTradeNo;
            localJSONObject.put("sessionId", sessionId);
            localJSONObject.put("price", price);
            info = ApiNetwork.post(url, localJSONObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }

    public static ApiMessage navigation() {
        JSONObject localJSONObject = new JSONObject();
        ApiMessage info = new ApiMessage();
        try {
            String url = ApiConstant.DATA_URL + ApiConstant.navigation;
            info = ApiNetwork.post(url, localJSONObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;

    }

    public static ApiMessage naviReplace(int id, String imgUrl) {
        JSONObject localJSONObject = new JSONObject();
        ApiMessage info = new ApiMessage();
        try {
            String url = ApiConstant.DATA_URL + ApiConstant.naviReplace;
            localJSONObject.put("id", id);
            localJSONObject.put("imgurl", imgUrl.split(ApiConstant.URL)[1]);
            info = ApiNetwork.post(url, localJSONObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }

    public static ApiMessage authentication(String sessionId, String pic) {
        String end = "\r\n";
        String twoHyphens = "--";
        String boundary = "******";
        String result = new String();
        ApiMessage message = new ApiMessage();
        try {
            URL url = new URL(ApiConstant.DATA_URL + ApiConstant.authentication);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url
                    .openConnection();
            httpURLConnection.setChunkedStreamingMode(128 * 1024);// 128K
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setUseCaches(false);
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setRequestProperty("Connection", "Keep-Alive");
            httpURLConnection.setRequestProperty("Charset", HTTP.UTF_8);
            httpURLConnection.setRequestProperty("Content-Type",
                    "multipart/form-data;boundary=" + boundary);

            DataOutputStream dos = new DataOutputStream(
                    httpURLConnection.getOutputStream());
            dos.writeBytes(twoHyphens + boundary + end);
            dos.writeBytes("Content-Disposition: form-data; name=\"sessionid\""
                    + end);
            dos.writeBytes(end);
            dos.writeBytes(sessionId);
            dos.writeBytes(end);
            dos.writeBytes(twoHyphens + boundary + end);

            dos.writeBytes("Content-Disposition: form-data; name=\"img\"; filename=\""
                    + pic.substring(pic.lastIndexOf("/") + 1) + "\"" + end);
            dos.writeBytes(end);

            FileInputStream fis = new FileInputStream(pic);
            byte[] buffer = new byte[8192]; // 8k
            int count = 0;
            while ((count = fis.read(buffer)) != -1) {
                dos.write(buffer, 0, count);
            }
            fis.close();

            dos.writeBytes(end);
            dos.writeBytes(twoHyphens + boundary + twoHyphens + end);
            dos.flush();

            InputStream is = httpURLConnection.getInputStream();
            InputStreamReader isr = new InputStreamReader(is, "utf-8");
            BufferedReader br = new BufferedReader(isr);
            result = br.readLine();
            if (result != null && !"".equals(result)) {
                message.setFlag(true);
                message.setMsg(result);
                // back = ApiJsonParser.ApiParserBack(result);
            } else {
                message.setFlag(false);
                message.setMsg("error");
            }
            dos.close();
            is.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return message;
    }

    // status 1——认证通过，0——认证失败
    public static ApiMessage verify(int uid, int status) {
        JSONObject localJSONObject = new JSONObject();
        ApiMessage info = new ApiMessage();
        try {
            String url = ApiConstant.DATA_URL + ApiConstant.verify;
            localJSONObject.put("uid", uid);
            localJSONObject.put("status", status);
            info = ApiNetwork.post(url, localJSONObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }

    public static ApiMessage forbid(int uid) {
        JSONObject localJSONObject = new JSONObject();
        ApiMessage info = new ApiMessage();
        try {
            String url = ApiConstant.DATA_URL + ApiConstant.forbid;
            localJSONObject.put("uid", uid);
            info = ApiNetwork.post(url, localJSONObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }

    public static ApiMessage examine(int picid, int status) {
        JSONObject localJSONObject = new JSONObject();
        ApiMessage info = new ApiMessage();
        try {
            String url = ApiConstant.DATA_URL + ApiConstant.examine;
            localJSONObject.put("picid", picid);
            localJSONObject.put("status", status);
            info = ApiNetwork.post(url, localJSONObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }

    public static ApiMessage getExamine() {
        JSONObject localJSONObject = new JSONObject();
        ApiMessage info = new ApiMessage();
        try {
            String url = ApiConstant.DATA_URL + ApiConstant.getExamine;
            info = ApiNetwork.post(url, localJSONObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }

    public static ApiMessage getNotification() {
        JSONObject localJSONObject = new JSONObject();
        ApiMessage info = new ApiMessage();
        try {
            String url = ApiConstant.DATA_URL + ApiConstant.getNotification;
            info = ApiNetwork.post(url, localJSONObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }

    public static ApiMessage getHelp() {
        JSONObject localJSONObject = new JSONObject();
        ApiMessage info = new ApiMessage();
        try {
            String url = ApiConstant.DATA_URL + ApiConstant.getHelp;
            info = ApiNetwork.post(url, localJSONObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }

    public static ApiMessage cmcc(String sessionId, int price) {
        JSONObject localJSONObject = new JSONObject();
        ApiMessage info = new ApiMessage();
        try {
            String url = ApiConstant.DATA_URL + ApiConstant.cmcc;
            localJSONObject.put("sessionId", sessionId);
            localJSONObject.put("money", price);
            info = ApiNetwork.post(url, localJSONObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }

    public static ApiMessage seeUserSimple(String sessionId, int uid) {
        JSONObject localJSONObject = new JSONObject();
        ApiMessage info = new ApiMessage();
        try {
            String url = ApiConstant.DATA_URL + ApiConstant.userSimple;
            localJSONObject.put("sessionid", sessionId);
            localJSONObject.put("uid", uid);
            info = ApiNetwork.post(url, localJSONObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }

    public static ApiMessage seeUserDetail(String sessionId, int uid) {
        JSONObject localJSONObject = new JSONObject();
        ApiMessage info = new ApiMessage();
        try {
            String url = ApiConstant.DATA_URL + ApiConstant.userDetailMsg;
            localJSONObject.put("sessionid", sessionId);
            localJSONObject.put("uid", uid);
            info = ApiNetwork.post(url, localJSONObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }

    public static ApiMessage getPhotoFrames() {
        JSONObject localJSONObject = new JSONObject();
        ApiMessage info = new ApiMessage();
        try {
            String url = ApiConstant.DATA_URL + ApiConstant.getphotoframes;
            info = ApiNetwork.post(url, localJSONObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }

    public static ApiMessage getFramesById(int id) {
        JSONObject localJSONObject = new JSONObject();
        ApiMessage info = new ApiMessage();
        try {
            String url = ApiConstant.DATA_URL + ApiConstant.getFramesById;
            localJSONObject.put("id", id);
            info = ApiNetwork.post(url, localJSONObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }

    public static ApiMessage wmqrank(int type, int times) {
        JSONObject localJSONObject = new JSONObject();
        ApiMessage info = new ApiMessage();
        try {
            String url = ApiConstant.DATA_URL + ApiConstant.wmqrank;
            localJSONObject.put("type", type);
            localJSONObject.put("times", times);
            info = ApiNetwork.post(url, localJSONObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }

    // 修改身高体重
    public static ApiMessage instWeightAndHeight(String sessionId, int weight,
                                                 int height) {
        JSONObject localJSONObject = new JSONObject();
        ApiMessage info = new ApiMessage();
        try {
            String url = ApiConstant.DATA_URL + ApiConstant.instWeightAndHeight;
            localJSONObject.put("sessionId", sessionId);
            localJSONObject.put("weight", weight);
            localJSONObject.put("height", height);
            info = ApiNetwork.post(url, localJSONObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }

    // 上传位置(包含图片)
    public static ApiMessage uploadSportsHistoryWithImg(String picTitle,
                                                        String picAddress, String sessionId, String wav, int wavDurations,
                                                        String lat, String lng, int steps, int calories, int scoreStep,
                                                        int scoreCalories, int sportsType) {
        String end = "\r\n";
        String twoHyphens = "--";
        String boundary = "******";
        String result = new String();
        ApiMessage message = new ApiMessage();
        try {
            URL url = new URL(ApiConstant.DATA_URL
                    + ApiConstant.uploadSportsHistoryWithImg);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url
                    .openConnection();
            httpURLConnection.setChunkedStreamingMode(128 * 1024);// 128K
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setUseCaches(false);
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setRequestProperty("Connection", "Keep-Alive");
            httpURLConnection.setRequestProperty("Charset", HTTP.UTF_8);
            httpURLConnection.setRequestProperty("Content-Type",
                    "multipart/form-data;boundary=" + boundary);

            DataOutputStream dos = new DataOutputStream(
                    httpURLConnection.getOutputStream());
            dos.writeBytes(twoHyphens + boundary + end);
            dos.writeBytes("Content-Disposition: form-data; name=\"title\""
                    + end);
            dos.writeBytes(end);
            dos.write(picTitle.getBytes());
            dos.writeBytes(end);
            if (!"".equals(wav)) {
                dos.writeBytes(twoHyphens + boundary + end);

                dos.writeBytes("Content-Disposition: form-data; name=\"wav\"; filename=\""
                        + wav.substring(wav.lastIndexOf(".")) + "\"" + end);
                dos.writeBytes(end);

                FileInputStream fis = new FileInputStream(wav);
                byte[] buffer = new byte[8192]; // 8k
                int count = 0;
                while ((count = fis.read(buffer)) != -1) {
                    dos.write(buffer, 0, count);
                }
                fis.close();

                dos.writeBytes(end);
            }
            dos.writeBytes(twoHyphens + boundary + end);
            dos.writeBytes("Content-Disposition: form-data; name=\"uid\"" + end);
            dos.writeBytes(end);
            dos.writeBytes(sessionId);
            dos.writeBytes(end);

            dos.writeBytes(twoHyphens + boundary + end);
            dos.writeBytes("Content-Disposition: form-data; name=\"lng\"" + end);
            dos.writeBytes(end);
            dos.writeBytes(lng);
            dos.writeBytes(end);

            dos.writeBytes(twoHyphens + boundary + end);
            dos.writeBytes("Content-Disposition: form-data; name=\"steps\""
                    + end);
            dos.writeBytes(end);
            dos.writeBytes(Integer.toString(steps));
            dos.writeBytes(end);

            dos.writeBytes(twoHyphens + boundary + end);
            dos.writeBytes("Content-Disposition: form-data; name=\"score_step\""
                    + end);
            dos.writeBytes(end);
            dos.writeBytes(Integer.toString(scoreStep));
            dos.writeBytes(end);

            dos.writeBytes(twoHyphens + boundary + end);
            dos.writeBytes("Content-Disposition: form-data; name=\"score_calorie\""
                    + end);
            dos.writeBytes(end);
            dos.writeBytes(Integer.toString(scoreCalories));
            dos.writeBytes(end);

            dos.writeBytes(twoHyphens + boundary + end);
            dos.writeBytes("Content-Disposition: form-data; name=\"sports_type\""
                    + end);
            dos.writeBytes(end);
            dos.writeBytes(Integer.toString(sportsType));
            dos.writeBytes(end);

            dos.writeBytes(twoHyphens + boundary + end);
            dos.writeBytes("Content-Disposition: form-data; name=\"steps\""
                    + end);
            dos.writeBytes(end);
            dos.writeBytes(Integer.toString(steps));
            dos.writeBytes(end);

            dos.writeBytes(twoHyphens + boundary + end);
            dos.writeBytes("Content-Disposition: form-data; name=\"calories\""
                    + end);
            dos.writeBytes(end);
            dos.writeBytes(Integer.toString(calories));
            dos.writeBytes(end);

            dos.writeBytes(twoHyphens + boundary + end);
            dos.writeBytes("Content-Disposition: form-data; name=\"wavdurations\""
                    + end);
            dos.writeBytes(end);
            dos.writeBytes(Integer.toString(wavDurations));
            dos.writeBytes(end);
            dos.writeBytes(twoHyphens + boundary + end);
            dos.writeBytes("Content-Disposition: form-data; name=\"lat\"" + end);
            dos.writeBytes(end);
            dos.writeBytes(lat);
            dos.writeBytes(end);
            if (!"".equals(picAddress)) {
                dos.writeBytes(twoHyphens + boundary + end);

                dos.writeBytes("Content-Disposition: form-data; name=\"img\"; filename=\""
                        + picAddress.substring(picAddress.lastIndexOf("/") + 1)
                        + "\"" + end);
                dos.writeBytes(end);

                FileInputStream fis = new FileInputStream(picAddress);
                byte[] buffer = new byte[8192]; // 8k
                int count = 0;
                while ((count = fis.read(buffer)) != -1) {
                    dos.write(buffer, 0, count);
                }
                fis.close();

                dos.writeBytes(end);
            }
            dos.writeBytes(twoHyphens + boundary + twoHyphens + end);
            dos.flush();

            InputStream is = httpURLConnection.getInputStream();
            InputStreamReader isr = new InputStreamReader(is, "utf-8");
            BufferedReader br = new BufferedReader(isr);
            result = br.readLine();
            if (result != null && !"".equals(result)) {
                message.setFlag(true);
                message.setMsg(result);
                // back = ApiJsonParser.ApiParserBack(result);
            } else {
                message.setFlag(false);
                message.setMsg("error");
            }
            dos.close();
            is.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return message;
    }

    // 上传数据
    public static ApiMessage uploadSportsType(String sessionId, int sportsType,
                                              int scoreStep, int scoreCalories, int steps, int calories) {
        String end = "\r\n";
        String twoHyphens = "--";
        String boundary = "******";
        String result = new String();
        ApiMessage message = new ApiMessage();
        try {
            URL url = new URL(ApiConstant.DATA_URL
                    + ApiConstant.uploadSportType);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url
                    .openConnection();
            httpURLConnection.setChunkedStreamingMode(128 * 1024);// 128K
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setUseCaches(false);
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setRequestProperty("Connection", "Keep-Alive");
            httpURLConnection.setRequestProperty("Charset", HTTP.UTF_8);
            httpURLConnection.setRequestProperty("Content-Type",
                    "multipart/form-data;boundary=" + boundary);

            DataOutputStream dos = new DataOutputStream(
                    httpURLConnection.getOutputStream());

            dos.writeBytes(twoHyphens + boundary + end);
            dos.writeBytes("Content-Disposition: form-data; name=\"uid\"" + end);
            dos.writeBytes(end);
            dos.writeBytes(sessionId);
            dos.writeBytes(end);

            dos.writeBytes(twoHyphens + boundary + end);
            dos.writeBytes("Content-Disposition: form-data; name=\"sports_type\""
                    + end);
            dos.writeBytes(end);
            dos.writeBytes(Integer.toString(sportsType));
            dos.writeBytes(end);

            dos.writeBytes(twoHyphens + boundary + end);
            dos.writeBytes("Content-Disposition: form-data; name=\"score_step\""
                    + end);
            dos.writeBytes(end);
            dos.writeBytes(Integer.toString(scoreStep));
            dos.writeBytes(end);

            dos.writeBytes(twoHyphens + boundary + end);
            dos.writeBytes("Content-Disposition: form-data; name=\"score_calorie\""
                    + end);
            dos.writeBytes(end);
            dos.writeBytes(Integer.toString(scoreCalories));
            dos.writeBytes(end);

            dos.writeBytes(twoHyphens + boundary + end);
            dos.writeBytes("Content-Disposition: form-data; name=\"steps\""
                    + end);
            dos.writeBytes(end);
            dos.writeBytes(Integer.toString(steps));
            dos.writeBytes(end);

            dos.writeBytes(twoHyphens + boundary + end);
            dos.writeBytes("Content-Disposition: form-data; name=\"calories\""
                    + end);
            dos.writeBytes(end);
            dos.writeBytes(Integer.toString(calories));
            dos.writeBytes(end);

            dos.writeBytes(twoHyphens + boundary + twoHyphens + end);
            dos.flush();

            InputStream is = httpURLConnection.getInputStream();
            InputStreamReader isr = new InputStreamReader(is, "utf-8");
            BufferedReader br = new BufferedReader(isr);
            result = br.readLine();
            if (result != null && !"".equals(result)) {
                message.setFlag(true);
                message.setMsg(result);
                // back = ApiJsonParser.ApiParserBack(result);
            } else {
                message.setFlag(false);
                message.setMsg("error");
            }
            dos.close();
            is.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return message;
    }

    public static ApiMessage sportsExamine(int picid, int status) {
        JSONObject localJSONObject = new JSONObject();
        ApiMessage info = new ApiMessage();
        try {
            String url = ApiConstant.DATA_URL + ApiConstant.sportsexamine;
            localJSONObject.put("picid", picid);
            localJSONObject.put("status", status);
            info = ApiNetwork.post(url, localJSONObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }

    public static ApiMessage getSportsExamine() {
        JSONObject localJSONObject = new JSONObject();
        ApiMessage info = new ApiMessage();
        try {
            String url = ApiConstant.DATA_URL + ApiConstant.getsportsExamine;
            info = ApiNetwork.post(url, localJSONObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }

    // type类型 1——全部排行 2——好友排行
    public static ApiMessage sportsRank(int type, int times, String sessionId) {
        JSONObject localJSONObject = new JSONObject();
        ApiMessage info = new ApiMessage();
        try {
            String url = ApiConstant.DATA_URL + ApiConstant.sportsRank;
            localJSONObject.put("type", type);
            localJSONObject.put("times", times);
            localJSONObject.put("sessionId", sessionId);
            info = ApiNetwork.post(url, localJSONObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }

    // type类型 1——全部排行 2——好友排行
    public static ApiMessage sportsRankNew(int type, int times, String sessionId) {
        JSONObject localJSONObject = new JSONObject();
        ApiMessage info = new ApiMessage();
        try {
            String url = ApiConstant.DATA_URL + ApiConstant.sportsRankNew;
            localJSONObject.put("type", type);
            localJSONObject.put("times", times);
            localJSONObject.put("sessionId", sessionId);
            info = ApiNetwork.post(url, localJSONObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }

    // 获取运动好友动态数目
    public static ApiMessage getSportsActs(String sessionId) {
        JSONObject localJSONObject = new JSONObject();
        ApiMessage info = new ApiMessage();
        try {
            String url = ApiConstant.DATA_URL + ApiConstant.getSportsActs;
            localJSONObject.put("sessionid", sessionId);
            info = ApiNetwork.post(url, localJSONObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }

    public static ApiMessage sportsActs(String sessionId, int times) {
        JSONObject localJSONObject = new JSONObject();
        ApiMessage info = new ApiMessage();
        try {
            String url = ApiConstant.DATA_URL + ApiConstant.sportsActs;
            localJSONObject.put("times", times);
            localJSONObject.put("sessionid", sessionId);
            info = ApiNetwork.post(url, localJSONObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }

    public static ApiMessage getSportsDetails(String sessionId,
                                              String startTime, String endTime, int uid) {
        JSONObject localJSONObject = new JSONObject();
        ApiMessage info = new ApiMessage();
        try {
            String url = ApiConstant.DATA_URL + ApiConstant.getSportsDetails;
            localJSONObject.put("uid", uid);
            localJSONObject.put("starttime", startTime);
            localJSONObject.put("endtime", endTime);
            localJSONObject.put("sessionid", sessionId);
            info = ApiNetwork.post(url, localJSONObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }

    public static ApiMessage getdayimgs(String sessionId, String startTime,
                                        String endTime, int uid) {
        JSONObject localJSONObject = new JSONObject();
        ApiMessage info = new ApiMessage();
        try {
            String url = ApiConstant.DATA_URL + ApiConstant.getDayImgs;
            localJSONObject.put("uid", uid);
            localJSONObject.put("starttime", startTime);
            localJSONObject.put("endtime", endTime);
            localJSONObject.put("sessionid", sessionId);
            info = ApiNetwork.post(url, localJSONObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }

    public static ApiMessage getSportsImgDetail(String sessionId, int pid) {
        JSONObject localJSONObject = new JSONObject();
        ApiMessage info = new ApiMessage();
        try {
            String url = ApiConstant.DATA_URL + ApiConstant.getSportsImgDetail;
            localJSONObject.put("pid", pid);
            localJSONObject.put("sessionid", sessionId);
            info = ApiNetwork.post(url, localJSONObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }

    public static ApiMessage getTime() {
        JSONObject localJSONObject = new JSONObject();
        ApiMessage info = new ApiMessage();
        try {
            String url = ApiConstant.DATA_URL + ApiConstant.getTime;
            info = ApiNetwork.post(url, localJSONObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }

    public static ApiMessage modifyTypeorName(String sessionId,
                                              String deviceName, int sportsType) {
        JSONObject localJSONObject = new JSONObject();
        ApiMessage info = new ApiMessage();
        try {
            String url = ApiConstant.DATA_URL + ApiConstant.savesportInfo;
            localJSONObject.put("sessionid", sessionId);
            localJSONObject.put("deviceName", deviceName);
            localJSONObject.put("sportsType", sportsType);
            info = ApiNetwork.post(url, localJSONObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }

    public static ApiMessage uploadCount(String sessionId) {
        JSONObject localJSONObject = new JSONObject();
        ApiMessage info = new ApiMessage();
        try {
            String url = ApiConstant.DATA_URL + ApiConstant.uploadCount;
            localJSONObject.put("sessionid", sessionId);
            info = ApiNetwork.post(url, localJSONObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }

    public static ApiMessage runWavSprots(String sessionId, int picId) {
        JSONObject localJSONObject = new JSONObject();
        ApiMessage info = new ApiMessage();
        try {
            String url = ApiConstant.DATA_URL + ApiConstant.runWavSports;
            localJSONObject.put("uid", sessionId);
            localJSONObject.put("picid", picId);
            info = ApiNetwork.post(url, localJSONObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }

    //上传配速记录
    public static ApiMessage uploadPeisu(String sessionId, int sports_id, String datasource,
                                         String application_name, PeisuInfo content, int typeid) {
        JSONObject localJSONObject = new JSONObject();
        ApiMessage info = new ApiMessage();
        JSONObject object = null;
        try {
            String url = ApiConstant.DATA_URL + ApiConstant.uploadPeisu;
            localJSONObject.put("sessionId", sessionId);
            localJSONObject.put("sports_id", sports_id);
            localJSONObject.put("datasource", datasource);
            localJSONObject.put("application_name", application_name);
            //初始化JSONArray对象，并添加数据
            JSONArray array = new JSONArray();
            for (int i = 0; i < content.getListpeis().size(); i++) {
                object = new JSONObject();
                object.put("sport_distance", content.getListpeis().get(i).getSport_distance());
                if (typeid == SportsType.TYPE_CLIMBING) {
                    object.put("sprots_height", content.getListpeis().get(i).getSprots_velocity());
                    object.put("GPS_type", content.getListpeis().get(i).getgPS_type());
                } else {

                    object.put("sprots_velocity", content.getListpeis().get(i).getSprots_velocity());
                }
                object.put("sprots_time", content.getListpeis().get(i).getSprots_time());
                array.put(object);
            }
            localJSONObject.put("content", array.toString());
            info = ApiNetwork.post(url, localJSONObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            object=null;
        }
        return info;

    }

    // 上传运动任务
    public static ApiMessage uploadSportTask(int Limt, String sessionId,
                                             int Sports_type_task, int sports_swim_type,
                                             int monitoring_equipment, String start_time, int sprts_time,
                                             double sport_distance, double sprots_Calorie,
                                             double sprots_velocity, double Heart_rate,
                                             String longitude_latitude, int step_num, int maptype,
                                             String serial, String app_id,String velocity_list,String coordinate_list) {
        String end = "\r\n";
        String twoHyphens = "--";
        String boundary = "******";
        String result = new String();
        ApiMessage message = new ApiMessage();
        try {
            URL url = new URL(ApiConstant.DATA_URL
                    + ApiConstant.uploadSportTask);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url
                    .openConnection();
            httpURLConnection.setConnectTimeout(30*1000);
            httpURLConnection.setReadTimeout(30*1000);
            httpURLConnection.setChunkedStreamingMode(128 * 1024);// 128K
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setUseCaches(false);
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setRequestProperty("Connection", "Keep-Alive");
            httpURLConnection.setRequestProperty("Charset", HTTP.UTF_8);
            httpURLConnection.setRequestProperty("Content-Type",
                    "multipart/form-data;boundary=" + boundary);

            DataOutputStream dos = new DataOutputStream(
                    httpURLConnection.getOutputStream());
            dos.writeBytes(twoHyphens + boundary + end);
            dos.writeBytes("Content-Disposition: form-data; name=\"Limt\""
                    + end);
            dos.writeBytes(end);
            dos.writeBytes(Integer.toString(Limt));
            dos.writeBytes(end);

            dos.writeBytes(twoHyphens + boundary + end);
            dos.writeBytes("Content-Disposition: form-data; name=\"sessionId\""
                    + end);
            dos.writeBytes(end);
            dos.writeBytes(sessionId);
            dos.writeBytes(end);

            dos.writeBytes(twoHyphens + boundary + end);
            dos.writeBytes("Content-Disposition: form-data; name=\"step_num\""
                    + end);
            dos.writeBytes(end);
            dos.writeBytes(Integer.toString(step_num));
            dos.writeBytes(end);

            dos.writeBytes(twoHyphens + boundary + end);
            dos.writeBytes("Content-Disposition: form-data; name=\"Sports_type_task\""
                    + end);
            dos.writeBytes(end);
            dos.writeBytes(Integer.toString(Sports_type_task));
            dos.writeBytes(end);

            dos.writeBytes(twoHyphens + boundary + end);
            dos.writeBytes("Content-Disposition: form-data; name=\"sports_swim_type\""
                    + end);
            dos.writeBytes(end);
            dos.writeBytes(Integer.toString(sports_swim_type));
            dos.writeBytes(end);

            dos.writeBytes(twoHyphens + boundary + end);
            dos.writeBytes("Content-Disposition: form-data; name=\"monitoring_equipment\""
                    + end);
            dos.writeBytes(end);
            dos.writeBytes(Integer.toString(monitoring_equipment));
            dos.writeBytes(end);

            dos.writeBytes(twoHyphens + boundary + end);
            dos.writeBytes("Content-Disposition: form-data; name=\"start_time\""
                    + end);
            dos.writeBytes(end);
            dos.writeBytes(start_time);
            dos.writeBytes(end);

            dos.writeBytes(twoHyphens + boundary + end);
            dos.writeBytes("Content-Disposition: form-data; name=\"sprts_time\""
                    + end);
            dos.writeBytes(end);
            dos.writeBytes(Integer.toString(sprts_time));
            dos.writeBytes(end);

            dos.writeBytes(twoHyphens + boundary + end);
            dos.writeBytes("Content-Disposition: form-data; name=\"sport_distance\""
                    + end);
            dos.writeBytes(end);
            dos.writeBytes(Double.toString(sport_distance));
            dos.writeBytes(end);

            dos.writeBytes(twoHyphens + boundary + end);
            dos.writeBytes("Content-Disposition: form-data; name=\"sprots_Calorie\""
                    + end);
            dos.writeBytes(end);
            dos.writeBytes(Double.toString(sprots_Calorie));
            dos.writeBytes(end);

            dos.writeBytes(twoHyphens + boundary + end);
            dos.writeBytes("Content-Disposition: form-data; name=\"sprots_velocity\""
                    + end);
            dos.writeBytes(end);
            dos.writeBytes(Double.toString(sprots_velocity));
            dos.writeBytes(end);

            dos.writeBytes(twoHyphens + boundary + end);
            dos.writeBytes("Content-Disposition: form-data; name=\"Heart_rate\""
                    + end);
            dos.writeBytes(end);
            dos.writeBytes(Double.toString(Heart_rate));
            dos.writeBytes(end);

            dos.writeBytes(twoHyphens + boundary + end);
            dos.writeBytes("Content-Disposition: form-data; name=\"maptype\""
                    + end);
            dos.writeBytes(end);
            dos.writeBytes(Double.toString(maptype));
            dos.writeBytes(end);

            dos.writeBytes(twoHyphens + boundary + end);
            dos.writeBytes("Content-Disposition: form-data; name=\"serial\""
                    + end);
            dos.writeBytes(end);
            dos.writeBytes(serial);
            dos.writeBytes(end);

            dos.writeBytes(twoHyphens + boundary + end);
            dos.writeBytes("Content-Disposition: form-data; name=\"app_id\""
                    + end);
            dos.writeBytes(end);
            dos.writeBytes(app_id);
            dos.writeBytes(end);


            dos.writeBytes(twoHyphens + boundary + end);
            dos.writeBytes("Content-Disposition: form-data; name=\"velocity_list\""
                    + end);
            dos.writeBytes(end);
            dos.writeBytes(velocity_list);
            dos.writeBytes(end);

            dos.writeBytes(twoHyphens + boundary + end);
            dos.writeBytes("Content-Disposition: form-data; name=\"coordinate_list\""
                    + end);
            dos.writeBytes(end);
            dos.writeBytes(coordinate_list);
            dos.writeBytes(end);

            dos.writeBytes(twoHyphens + boundary + end);
            dos.writeBytes("Content-Disposition: form-data; name=\"datasource\""
                    + end);
            dos.writeBytes(end);
            dos.writeBytes("android");
            dos.writeBytes(end);

            dos.writeBytes(twoHyphens + boundary + end);
            dos.writeBytes("Content-Disposition: form-data; name=\"longitude_latitude\""
                    + end);
            dos.writeBytes(end);
            dos.writeBytes(longitude_latitude);
            dos.writeBytes(end);
            dos.writeBytes(twoHyphens + boundary + twoHyphens + end);
            dos.flush();

            InputStream is = httpURLConnection.getInputStream();
            InputStreamReader isr = new InputStreamReader(is, "utf-8");
            BufferedReader br = new BufferedReader(isr);
            result = br.readLine();
            if (result != null && !"".equals(result)) {
                message.setFlag(true);
                message.setMsg(result);
                // back = ApiJsonParser.ApiParserBack(result);
            } else {
                message.setFlag(false);
                message.setMsg("error");
            }
            dos.close();
            is.close();

        } catch (ConnectTimeoutException e) {
            e.printStackTrace();
                message.setFlag(false);
                message.setMsg("SocketTimeoutException");
        } catch (SocketTimeoutException e) {
            e.printStackTrace();
                message.setFlag(false);
                message.setMsg("SocketTimeoutException");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return message;
    }

    // 更新运动任务
    public static ApiMessage updateSportTask(int id, int Limt,
                                             String sessionId, int Sports_type_task, int monitoring_equipment,
                                             String start_time, int sprts_time, double sport_distance,
                                             double sprots_Calorie, double sprots_velocity, double Heart_rate,
                                             String longitude_latitude, int step_num, int maptype) {
        String end = "\r\n";
        String twoHyphens = "--";
        String boundary = "******";
        String result = new String();
        ApiMessage message = new ApiMessage();
        try {
            URL url = new URL(ApiConstant.DATA_URL
                    + ApiConstant.updateSportTask);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url
                    .openConnection();
            httpURLConnection.setChunkedStreamingMode(128 * 1024);// 128K
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setUseCaches(false);
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setRequestProperty("Connection", "Keep-Alive");
            httpURLConnection.setRequestProperty("Charset", HTTP.UTF_8);
            httpURLConnection.setRequestProperty("Content-Type",
                    "multipart/form-data;boundary=" + boundary);

            DataOutputStream dos = new DataOutputStream(
                    httpURLConnection.getOutputStream());
            dos.writeBytes(twoHyphens + boundary + end);
            dos.writeBytes("Content-Disposition: form-data; name=\"Limt\""
                    + end);
            dos.writeBytes(end);
            dos.writeBytes(Integer.toString(Limt));
            dos.writeBytes(end);

            dos.writeBytes(twoHyphens + boundary + end);
            dos.writeBytes("Content-Disposition: form-data; name=\"id\"" + end);
            dos.writeBytes(end);
            dos.writeBytes(Integer.toString(id));
            dos.writeBytes(end);

            dos.writeBytes(twoHyphens + boundary + end);
            dos.writeBytes("Content-Disposition: form-data; name=\"sessionid\""
                    + end);
            dos.writeBytes(end);
            dos.writeBytes(sessionId);
            dos.writeBytes(end);

            dos.writeBytes(twoHyphens + boundary + end);
            dos.writeBytes("Content-Disposition: form-data; name=\"step_num\""
                    + end);
            dos.writeBytes(end);
            dos.writeBytes(Integer.toString(step_num));
            dos.writeBytes(end);

            dos.writeBytes(twoHyphens + boundary + end);
            dos.writeBytes("Content-Disposition: form-data; name=\"Sports_type_task\""
                    + end);
            dos.writeBytes(end);
            dos.writeBytes(Integer.toString(Sports_type_task));
            dos.writeBytes(end);

            dos.writeBytes(twoHyphens + boundary + end);
            dos.writeBytes("Content-Disposition: form-data; name=\"monitoring_equipment\""
                    + end);
            dos.writeBytes(end);
            dos.writeBytes(Integer.toString(monitoring_equipment));
            dos.writeBytes(end);

            dos.writeBytes(twoHyphens + boundary + end);
            dos.writeBytes("Content-Disposition: form-data; name=\"start_time\""
                    + end);
            dos.writeBytes(end);
            dos.writeBytes(start_time);
            dos.writeBytes(end);

            dos.writeBytes(twoHyphens + boundary + end);
            dos.writeBytes("Content-Disposition: form-data; name=\"sprts_time\""
                    + end);
            dos.writeBytes(end);
            dos.writeBytes(Integer.toString(sprts_time));
            dos.writeBytes(end);

            dos.writeBytes(twoHyphens + boundary + end);
            dos.writeBytes("Content-Disposition: form-data; name=\"sport_distance\""
                    + end);
            dos.writeBytes(end);
            dos.writeBytes(Double.toString(sport_distance));
            dos.writeBytes(end);

            dos.writeBytes(twoHyphens + boundary + end);
            dos.writeBytes("Content-Disposition: form-data; name=\"sprots_Calorie\""
                    + end);
            dos.writeBytes(end);
            dos.writeBytes(Double.toString(sprots_Calorie));
            dos.writeBytes(end);

            dos.writeBytes(twoHyphens + boundary + end);
            dos.writeBytes("Content-Disposition: form-data; name=\"sprots_velocity\""
                    + end);
            dos.writeBytes(end);
            dos.writeBytes(Double.toString(sprots_velocity));
            dos.writeBytes(end);

            dos.writeBytes(twoHyphens + boundary + end);
            dos.writeBytes("Content-Disposition: form-data; name=\"Heart_rate\""
                    + end);
            dos.writeBytes(end);
            dos.writeBytes(Double.toString(Heart_rate));
            dos.writeBytes(end);

            dos.writeBytes(twoHyphens + boundary + end);
            dos.writeBytes("Content-Disposition: form-data; name=\"maptype\""
                    + end);
            dos.writeBytes(end);
            dos.writeBytes(Double.toString(maptype));
            dos.writeBytes(end);

            dos.writeBytes(twoHyphens + boundary + end);
            dos.writeBytes("Content-Disposition: form-data; name=\"longitude_latitude\""
                    + end);
            dos.writeBytes(end);
            dos.writeBytes(longitude_latitude);
            dos.writeBytes(end);
            dos.writeBytes(twoHyphens + boundary + twoHyphens + end);
            dos.flush();

            InputStream is = httpURLConnection.getInputStream();
            InputStreamReader isr = new InputStreamReader(is, "utf-8");
            BufferedReader br = new BufferedReader(isr);
            result = br.readLine();
            if (result != null && !"".equals(result)) {
                message.setFlag(true);
                message.setMsg(result);
                // back = ApiJsonParser.ApiParserBack(result);
            } else {
                message.setFlag(false);
                message.setMsg("error");
            }
            dos.close();
            is.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return message;
    }

    // 上传运动任务中媒体文件
    public static ApiMessage uploadSportTaskMedia(String sessionId, int uid,
                                                  int task_id, int type, String durations, String path, String title,
                                                  String location, int width, int height, int maptype) {
        String end = "\r\n";
        String twoHyphens = "--";
        String boundary = "******";
        String result = new String();
        ApiMessage message = new ApiMessage();
        try {
            URL url = new URL(ApiConstant.DATA_URL
                    + ApiConstant.uploadSportTaskMedia);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url
                    .openConnection();
            httpURLConnection.setChunkedStreamingMode(128 * 1024);// 128K
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setUseCaches(false);
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setRequestProperty("Connection", "Keep-Alive");
            httpURLConnection.setRequestProperty("Charset", "UTF-8");
            httpURLConnection.setRequestProperty("Content-Type",
                    "multipart/form-data;boundary=" + boundary);

            DataOutputStream dos = new DataOutputStream(
                    httpURLConnection.getOutputStream());
            dos.writeBytes(twoHyphens + boundary + end);
            dos.writeBytes("Content-Disposition: form-data; name=\"sessionid\""
                    + end);
            dos.writeBytes(end);
            dos.writeBytes(sessionId);
            dos.writeBytes(end);

            dos.writeBytes(twoHyphens + boundary + end);
            dos.writeBytes("Content-Disposition: form-data; name=\"uid\"" + end);
            dos.writeBytes(end);
            dos.writeBytes(Integer.toString(uid));
            dos.writeBytes(end);

            dos.writeBytes(twoHyphens + boundary + end);
            dos.writeBytes("Content-Disposition: form-data; name=\"task_id\""
                    + end);
            dos.writeBytes(end);
            dos.writeBytes(Integer.toString(task_id));
            dos.writeBytes(end);

            dos.writeBytes(twoHyphens + boundary + end);
            dos.writeBytes("Content-Disposition: form-data; name=\"type\""
                    + end);
            dos.writeBytes(end);
            dos.writeBytes(Integer.toString(type));
            dos.writeBytes(end);

            dos.writeBytes(twoHyphens + boundary + end);
            dos.writeBytes("Content-Disposition: form-data; name=\"durations\""
                    + end);
            dos.writeBytes(end);
            dos.writeBytes(durations);
            dos.writeBytes(end);

            if (type == 1) {
                dos.writeBytes(twoHyphens + boundary + end);

                dos.writeBytes("Content-Disposition: form-data; name=\"img\"; filename=\""
                        + path.substring(path.lastIndexOf("/") + 1)
                        + "\""
                        + end);
                dos.writeBytes(end);

                FileInputStream fis = new FileInputStream(path);
                byte[] buffer = new byte[8192]; // 8k
                Log.i("上传路径", "path = " + path);
                int count = 0;
                while ((count = fis.read(buffer)) != -1) {
                    dos.write(buffer, 0, count);
                }
                fis.close();
                dos.writeBytes(end);
            } else if (type == 2) {
                dos.writeBytes(twoHyphens + boundary + end);
                dos.writeBytes("Content-Disposition: form-data; name=\"wav\"; filename=\""
                        + path.substring(path.lastIndexOf(".")) + "\"" + end);
                dos.writeBytes(end);
                Log.i("上传路径", "path = " + path);
                FileInputStream fis = new FileInputStream(path);
                byte[] buffer = new byte[8192]; // 8k
                int count = 0;
                while ((count = fis.read(buffer)) != -1) {
                    dos.write(buffer, 0, count);
                }
                fis.close();
                dos.writeBytes(end);
            } else if (type == 3) {
                dos.writeBytes(twoHyphens + boundary + end);
                dos.writeBytes("Content-Disposition: form-data; name=\"video\"; filename=\""
                        + path.substring(path.lastIndexOf(".")) + "\"" + end);
                dos.writeBytes(end);
                Log.i("上传路径", "path = " + path);
                FileInputStream fis = new FileInputStream(path);
                byte[] buffer = new byte[8192]; // 8k
                int count = 0;
                while ((count = fis.read(buffer)) != -1) {
                    dos.write(buffer, 0, count);
                }
                fis.close();
                dos.writeBytes(end);
            }

            dos.writeBytes(twoHyphens + boundary + end);
            dos.writeBytes("Content-Disposition: form-data; name=\"title\""
                    + end);
            dos.writeBytes(end);
            dos.writeBytes(title);
            dos.writeBytes(end);

            dos.writeBytes(twoHyphens + boundary + end);
            dos.writeBytes("Content-Disposition: form-data; name=\"location\""
                    + end);
            dos.writeBytes(end);
            dos.writeBytes(location);
            dos.writeBytes(end);

            dos.writeBytes(twoHyphens + boundary + end);
            dos.writeBytes("Content-Disposition: form-data; name=\"width\""
                    + end);
            dos.writeBytes(end);
            dos.writeBytes(Integer.toString(width));
            dos.writeBytes(end);

            dos.writeBytes(twoHyphens + boundary + end);
            dos.writeBytes("Content-Disposition: form-data; name=\"height\""
                    + end);
            dos.writeBytes(end);
            dos.writeBytes(Integer.toString(height));
            dos.writeBytes(end);

            dos.writeBytes(twoHyphens + boundary + end);
            dos.writeBytes("Content-Disposition: form-data; name=\"maptype\""
                    + end);
            dos.writeBytes(end);
            dos.writeBytes(Integer.toString(maptype));
            dos.writeBytes(end);

            dos.writeBytes(twoHyphens + boundary + twoHyphens + end);
            dos.flush();

            InputStream is = httpURLConnection.getInputStream();
            InputStreamReader isr = new InputStreamReader(is, "utf-8");
            BufferedReader br = new BufferedReader(isr);
            result = br.readLine();
            if (result != null && !"".equals(result)) {
                message.setFlag(true);
                message.setMsg(result);
                // back = ApiJsonParser.ApiParserBack(result);
                Log.e("result", "result" + result);
            } else {
                message.setFlag(false);
                message.setMsg("error");
            }
            dos.close();
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return message;
    }

    // visitor sports
    public static ApiMessage getSportsVisitor(String sessionId, int times) {
        JSONObject localJSONObject = new JSONObject();
        ApiMessage info = new ApiMessage();
        try {
            String url = ApiConstant.DATA_URL + ApiConstant.getSportsVisitor;
            localJSONObject.put("sessionId", sessionId);
            localJSONObject.put("times", times);
            info = ApiNetwork.post(url, localJSONObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }

    public static ApiMessage visitorSports(String sessionId, int uid) {
        JSONObject localJSONObject = new JSONObject();
        ApiMessage info = new ApiMessage();
        try {
            String url = ApiConstant.DATA_URL + ApiConstant.visitorSports;
            localJSONObject.put("sessionId", sessionId);
            localJSONObject.put("uid", uid);
            info = ApiNetwork.post(url, localJSONObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }

    public static ApiMessage getSportsTaskByDate(String uid, String date,
                                                 int userid) {
        JSONObject localJSONObject = new JSONObject();
        ApiMessage info = new ApiMessage();
        try {
            String url = ApiConstant.DATA_URL + ApiConstant.getSportsTaskByDate;
            localJSONObject.put("uid", uid);
            localJSONObject.put("date", date);
            localJSONObject.put("userid", userid);
            info = ApiNetwork.post(url, localJSONObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }

    public static ApiMessage getSportsInfo(String uid, int limit, int userid) {
        JSONObject localJSONObject = new JSONObject();
        ApiMessage info = new ApiMessage();
        try {
            String url = ApiConstant.DATA_URL + ApiConstant.getSportsInfo;
            localJSONObject.put("uid", uid);
            localJSONObject.put("limit", limit);
            localJSONObject.put("userid", userid);
            info = ApiNetwork.post(url, localJSONObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }

    public static ApiMessage getSportsTaskAll(String uid, int times, int userid) {
        JSONObject localJSONObject = new JSONObject();
        ApiMessage info = new ApiMessage();
        try {
            String url = ApiConstant.DATA_URL + ApiConstant.getSportsTaskAll;
            localJSONObject.put("uid", uid);
            localJSONObject.put("times", times);
            localJSONObject.put("userid", userid);
            info = ApiNetwork.post(url, localJSONObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }

    public static ApiMessage getSportsTaskPhone(String uid, int times,
                                                int userid) {
        JSONObject localJSONObject = new JSONObject();
        ApiMessage info = new ApiMessage();
        try {
            String url = ApiConstant.DATA_URL + ApiConstant.getSportsTaskPhone;
            localJSONObject.put("uid", uid);
            localJSONObject.put("times", times);
            localJSONObject.put("userid", userid);
            info = ApiNetwork.post(url, localJSONObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }

    public static ApiMessage getSportsTaskWatch(String uid, int times,
                                                int userid) {
        JSONObject localJSONObject = new JSONObject();
        ApiMessage info = new ApiMessage();
        try {
            String url = ApiConstant.DATA_URL + ApiConstant.getSportsTaskWatch;
            localJSONObject.put("uid", uid);
            localJSONObject.put("times", times);
            localJSONObject.put("userid", userid);
            info = ApiNetwork.post(url, localJSONObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }

    // 根据TaskID获取运动片段详情
    public static ApiMessage getSportsTaskById(String sessionId, int uid,
                                               int taskid) {
        JSONObject localJSONObject = new JSONObject();
        ApiMessage info = new ApiMessage();
        try {
            String url = ApiConstant.DATA_URL + ApiConstant.getSportsTaskById;
            localJSONObject.put("sessionId", sessionId);
            localJSONObject.put("uid", uid);
            localJSONObject.put("taskid", taskid);
            info = ApiNetwork.post(url, localJSONObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }

    public static ApiMessage sportsUploadMsg(String sessionId, int times) {
        JSONObject localJSONObject = new JSONObject();
        ApiMessage info = new ApiMessage();
        try {
            String url = ApiConstant.DATA_URL + ApiConstant.sportsUploadMsg;
            localJSONObject.put("times", times);
            localJSONObject.put("sessionid", sessionId);
            info = ApiNetwork.post(url, localJSONObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }

    // 根据TaskID获取媒体文件列表
    public static ApiMessage getMediaListByTaskid(String sessionId, int taskid) {
        JSONObject localJSONObject = new JSONObject();
        ApiMessage info = new ApiMessage();
        try {
            String url = ApiConstant.DATA_URL
                    + ApiConstant.getMediaListByTaskid;
            localJSONObject.put("sessionid", sessionId);
            localJSONObject.put("taskid", taskid);
            info = ApiNetwork.post(url, localJSONObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }

    // 根据媒体ID获取媒体文件
    public static ApiMessage getSportsMediaById(String sessionId, int mediaId) {
        JSONObject localJSONObject = new JSONObject();
        ApiMessage info = new ApiMessage();
        try {
            String url = ApiConstant.DATA_URL + ApiConstant.getSportsMediaById;
            localJSONObject.put("sessionid", sessionId);
            localJSONObject.put("id", mediaId);
            info = ApiNetwork.post(url, localJSONObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }

//    // 根据url从国家气象局获取天气信息
//    public static ApiMessage getSportsWeatherInfo(String url) {
//        ApiMessage info = new ApiMessage();
//        try {
//            info = ApiNetwork.get(url);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return info;
//    }

    // 根据TaskID删除服务器上的运动记录
    public static ApiMessage deleteSportsTaskById(String sessionid, int uid,
                                                  int taskid) {
        JSONObject localJSONObject = new JSONObject();
        ApiMessage info = new ApiMessage();
        try {
            String url = ApiConstant.DATA_URL
                    + ApiConstant.deleteSportsTaskById;
            localJSONObject.put("sessionid", sessionid);
            localJSONObject.put("uid", uid);
            localJSONObject.put("taskid", taskid);
            info = ApiNetwork.post(url, localJSONObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }

    // 获取约炮你的人
    // msgbox getprimsgall
    public static ApiMessage getInviteSports(String sessionId, int times) {
        JSONObject localJSONObject = new JSONObject();
        ApiMessage info = new ApiMessage();
        try {
            String url = ApiConstant.DATA_URL + ApiConstant.getinvitesports;
            localJSONObject.put("times", times);
            localJSONObject.put("sessionid", sessionId);
            info = ApiNetwork.post(url, localJSONObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }

    // forget password use phone
    public static ApiMessage forgetPwdPhone(String phone) {
        JSONObject localJSONObject = new JSONObject();
        ApiMessage info = new ApiMessage();
        try {
            String url = ApiConstant.DATA_URL + ApiConstant.userPwdForgetnew;
            localJSONObject.put("email", phone);
            info = ApiNetwork.post(url, localJSONObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }

    // 根据MediaID删除服务器上的运动媒体文件
    public static ApiMessage deleteSportMediaById(String sessionid, int uid,
                                                  int mediaid) {
        JSONObject localJSONObject = new JSONObject();
        ApiMessage info = new ApiMessage();
        try {
            String url = ApiConstant.DATA_URL
                    + ApiConstant.deleteSportMediaById;
            localJSONObject.put("sessionid", sessionid);
            localJSONObject.put("uid", uid);
            localJSONObject.put("id", mediaid);
            info = ApiNetwork.post(url, localJSONObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }

    // 获取时间列表
    public static ApiMessage getCurrentTimeById(String sessionId, int uid) {
        JSONObject localJSONObject = new JSONObject();
        ApiMessage api = new ApiMessage();
        try {
            String url = ApiConstant.DATA_URL + ApiConstant.getCurrentTimeById;
            localJSONObject.put("sessionid", sessionId);
            localJSONObject.put("uid", uid);
            api = ApiNetwork.post(url, localJSONObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return api;
    }

    // 获取task时间列表
    public static ApiMessage getTaskTimeById(String sessionId, int uid) {
        JSONObject localJSONObject = new JSONObject();
        ApiMessage api = new ApiMessage();
        try {
            String url = ApiConstant.DATA_URL + ApiConstant.getTaskTimeById;
            localJSONObject.put("sessionid", sessionId);
            localJSONObject.put("uid", uid);
            api = ApiNetwork.post(url, localJSONObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return api;
    }

    public static ApiMessage getLastTaskInfo(String sessionid, int userid) {
        JSONObject localJSONObject = new JSONObject();
        ApiMessage info = new ApiMessage();
        try {
            String url = ApiConstant.DATA_URL + ApiConstant.getLastTaskInfo;
            localJSONObject.put("sessionid", sessionid);
            localJSONObject.put("userid", userid);
            info = ApiNetwork.post(url, localJSONObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }

    public static ApiMessage getActionList(String sessionId, int times) {
        // TODO Auto-generated method stub
        JSONObject localJSONObject = new JSONObject();
        ApiMessage info = new ApiMessage();
        try {
            String url = ApiConstant.DATA_URL + ApiConstant.getActionList;
            localJSONObject.put("session_id", sessionId);
            localJSONObject.put("times", times);
            info = ApiNetwork.post(url, localJSONObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }

    static ApiMessage actionPost(String sessionId, int actionId,
                                 String methodUrl) {
        // TODO Auto-generated method stub
        JSONObject localJSONObject = new JSONObject();
        ApiMessage info = new ApiMessage();
        try {
            String url = ApiConstant.DATA_URL + methodUrl;
            localJSONObject.put("session_id", sessionId);
            localJSONObject.put("activity_id", actionId);
            info = ApiNetwork.post(url, localJSONObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }

    static ApiMessage activityPost(String sessionId, int actionId, int times,
                                   String methodUrl) {
        // TODO Auto-generated method stub
        JSONObject localJSONObject = new JSONObject();
        ApiMessage info = new ApiMessage();
        try {
            String url = ApiConstant.DATA_URL + methodUrl;
            localJSONObject.put("session_id", sessionId);
            localJSONObject.put("id", actionId);
            localJSONObject.put("times", times);
            info = ApiNetwork.post(url, localJSONObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }

    // 报名参加活动
    public static ApiMessage signupAction(String sessionId, int uid,
                                          int actionId) {
        // TODO Auto-generated method stub
        JSONObject localJSONObject = new JSONObject();
        ApiMessage info = new ApiMessage();
        try {
            String url = ApiConstant.DATA_URL + ApiConstant.signupAction;
            localJSONObject.put("session_id", sessionId);
            localJSONObject.put("uid", uid);
            localJSONObject.put("id", actionId);
            info = ApiNetwork.post(url, localJSONObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }

    public static ApiMessage getPayInfo(String sessionId, int actionId) {
        // TODO Auto-generated method stub
        JSONObject localJSONObject = new JSONObject();
        ApiMessage info = new ApiMessage();
        try {
            String url = ApiConstant.DATA_URL + ApiConstant.getPayInfo;
            localJSONObject.put("session_id", sessionId);
            localJSONObject.put("activity_id", actionId);
            info = ApiNetwork.post(url, localJSONObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }

    public static ApiMessage getApplyList(String sessionId, int actionId,
                                          int times) {
        // TODO Auto-generated method stub
        JSONObject localJSONObject = new JSONObject();
        ApiMessage info = new ApiMessage();
        try {
            String url = ApiConstant.DATA_URL + ApiConstant.getApplyInfo;
            localJSONObject.put("session_id", sessionId);
            localJSONObject.put("activity_id", actionId);
            localJSONObject.put("times", times);
            info = ApiNetwork.post(url, localJSONObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }

    public static ApiMessage getActionRankList(String sessionId, int actionId,
                                               int index) {
        // TODO Auto-generated method stub
        JSONObject localJSONObject = new JSONObject();
        ApiMessage info = new ApiMessage();
        try {
            String url = ApiConstant.DATA_URL + ApiConstant.getActionRankList;
            localJSONObject.put("session_id", sessionId);
            localJSONObject.put("activity_id", actionId);
            localJSONObject.put("times", index);
            info = ApiNetwork.post(url, localJSONObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }

    public static ApiMessage goApply(String sessionId, int actionId) {
        // TODO Auto-generated method stub
        JSONObject localJSONObject = new JSONObject();
        ApiMessage info = new ApiMessage();
        try {
            String url = ApiConstant.DATA_URL + ApiConstant.goApply;
            localJSONObject.put("session_id", sessionId);
            localJSONObject.put("activity_id", actionId);
            info = ApiNetwork.post(url, localJSONObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }

    // 获取最新的评论列表
    public static ApiMessage getNewCommentCount(String sessionId, int uid) {
        // TODO Auto-generated method stub
        String result = new String();
        ApiMessage message = new ApiMessage();
        HttpURLConnection httpURLConnection = initHttpUrlConnection(ApiConstant.getNewCommentInfo);
        try {
            DataOutputStream dos = new DataOutputStream(
                    httpURLConnection.getOutputStream());
            createWriteBytes(dos, sessionId, "session_id");
            createWriteBytes(dos, uid + "", "uid");
            dos.writeBytes(twoHyphens + boundary + twoHyphens + end);
            dos.flush();
            InputStream is = httpURLConnection.getInputStream();
            InputStreamReader isr = new InputStreamReader(is, "utf-8");
            BufferedReader br = new BufferedReader(isr);
            result = br.readLine();
            if (result != null && !"".equals(result)) {
                message.setFlag(true);
                message.setMsg(result);
            } else {
                message.setFlag(false);
                message.setMsg("error");
            }
            dos.close();
            is.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return message;
    }

    // 获取未读的评论内容
    public static ApiMessage getNewCommentLists(String sessionId, int uid) {
        String result = new String();
        ApiMessage message = new ApiMessage();
        HttpURLConnection httpURLConnection = initHttpUrlConnection(ApiConstant.getNewCommentLists);
        try {
            DataOutputStream dos = new DataOutputStream(
                    httpURLConnection.getOutputStream());
            createWriteBytes(dos, sessionId, "session_id");
            createWriteBytes(dos, uid + "", "uid");
            dos.writeBytes(twoHyphens + boundary + twoHyphens + end);
            dos.flush();
            InputStream is = httpURLConnection.getInputStream();
            InputStreamReader isr = new InputStreamReader(is, "utf-8");
            BufferedReader br = new BufferedReader(isr);
            result = br.readLine();
            if (result != null && !"".equals(result)) {
                message.setFlag(true);
                message.setMsg(result);
            } else {
                message.setFlag(false);
                message.setMsg("error");
            }
            dos.close();
            is.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return message;
    }

    // 新的获取活动的接口
    public static ApiMessage getNewActionList(String sessionId,
                                              String channelnum, int times) {
        // TODO Auto-generated method stub
        // String result = new String();
        // ApiMessage message = new ApiMessage();
        // HttpURLConnection httpURLConnection =
        // initHttpUrlConnection(ApiConstant.getNewActionList);
        // try {
        // DataOutputStream dos = new DataOutputStream(
        // httpURLConnection.getOutputStream());
        // createWriteBytes(dos, sessionId, "session_id");
        // createWriteBytes(dos, times + "", "times");
        // dos.writeBytes(twoHyphens + boundary + twoHyphens + end);
        // dos.flush();
        // InputStream is = httpURLConnection.getInputStream();
        // InputStreamReader isr = new InputStreamReader(is, "utf-8");
        // BufferedReader br = new BufferedReader(isr);
        // result = br.readLine();
        // if (result != null && !"".equals(result)) {
        // message.setFlag(true);
        // message.setMsg(result);
        // } else {
        // message.setFlag(false);
        // message.setMsg("error");
        // }
        // dos.close();
        // is.close();

        JSONObject localJSONObject = new JSONObject();
        ApiMessage info = new ApiMessage();
        try {
            String url = ApiConstant.DATA_URL + ApiConstant.getNewActionList;
            localJSONObject.put("session_id", sessionId);
            localJSONObject.put("channelnum", channelnum);
            localJSONObject.put("times", times + "");
            info = ApiNetwork.post(url, localJSONObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;

        // } catch (Exception e) {
        // e.printStackTrace();
        // }
        // return message;
    }

    // 获取提供用户选择的活动
    public static ApiMessage getOnlineActionList(String sessionId) {
        // TODO Auto-generated method stub
        String result = new String();
        ApiMessage message = new ApiMessage();
        HttpURLConnection httpURLConnection = initHttpUrlConnection(ApiConstant.getOnlineAction);
        // HttpURLConnection httpURLConnection =
        // initHttpUrlConnection(ApiConstant.getNewActionList);
        try {
            DataOutputStream dos = new DataOutputStream(
                    httpURLConnection.getOutputStream());
            createWriteBytes(dos, sessionId, "session_id");
            dos.writeBytes(twoHyphens + boundary + twoHyphens + end);
            dos.flush();
            InputStream is = httpURLConnection.getInputStream();
            InputStreamReader isr = new InputStreamReader(is, "utf-8");
            BufferedReader br = new BufferedReader(isr);
            result = br.readLine();
            if (result != null && !"".equals(result)) {
                message.setFlag(true);
                message.setMsg(result);
            } else {
                message.setFlag(false);
                message.setMsg("error");
            }
            dos.close();
            is.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return message;
    }

    public static ApiMessage getFindMore(String sessionId, String find_id,
                                         String uid) {
        // TODO Auto-generated method stub
        String result = new String();
        ApiMessage message = new ApiMessage();
        HttpURLConnection httpURLConnection = initHttpUrlConnection(ApiConstant.getFindMore);
        try {
            DataOutputStream dos = new DataOutputStream(
                    httpURLConnection.getOutputStream());
            createWriteBytes(dos, sessionId, "session_id");
            createWriteBytes(dos, find_id + "", "find_id");
            if (!TextUtils.isEmpty(uid))
                createWriteBytes(dos, uid, "uid");
            dos.writeBytes(twoHyphens + boundary + twoHyphens + end);
            dos.flush();
            InputStream is = httpURLConnection.getInputStream();
            InputStreamReader isr = new InputStreamReader(is, "utf-8");
            BufferedReader br = new BufferedReader(isr);
            result = br.readLine();
            if (result != null && !"".equals(result)) {
                message.setFlag(true);
                message.setMsg(result);
            } else {
                message.setFlag(false);
                message.setMsg("error");
            }
            dos.close();
            is.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return message;
    }

    // 获取单个发现信息
    public static ApiMessage getFindSingleMore(String sessionId,
                                               String find_id, String uid) {
        // TODO Auto-generated method stub
        String result = new String();
        ApiMessage message = new ApiMessage();
        HttpURLConnection httpURLConnection = initHttpUrlConnection(ApiConstant.getFindList);
        try {
            DataOutputStream dos = new DataOutputStream(
                    httpURLConnection.getOutputStream());
            createWriteBytes(dos, sessionId, "session_id");
            createWriteBytes(dos, find_id + "", "find_id");
            if (!TextUtils.isEmpty(uid))
                createWriteBytes(dos, uid, "uid");
            dos.writeBytes(twoHyphens + boundary + twoHyphens + end);
            dos.flush();
            InputStream is = httpURLConnection.getInputStream();
            InputStreamReader isr = new InputStreamReader(is, "utf-8");
            BufferedReader br = new BufferedReader(isr);
            result = br.readLine();
            if (result != null && !"".equals(result)) {
                message.setFlag(true);
                message.setMsg(result);
            } else {
                message.setFlag(false);
                message.setMsg("error");
            }
            dos.close();
            is.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return message;
    }

    public static ApiMessage getFindList(String sessionId, int times,
                                         String uid, boolean isFriends, int activity_Id) {
        // TODO Auto-generated method stub
        String result = new String();
        ApiMessage message = new ApiMessage();
        HttpURLConnection httpURLConnection = null;
        if (isFriends) {
            httpURLConnection = initHttpUrlConnection(ApiConstant.getGoodFriendsList);
        } else {
            httpURLConnection = initHttpUrlConnection(ApiConstant.getFindList);
        }
        try {
            DataOutputStream dos = new DataOutputStream(
                    httpURLConnection.getOutputStream());
            createWriteBytes(dos, sessionId, "session_id");
            createWriteBytes(dos, times + "", "times");
            if (!TextUtils.isEmpty(uid))
                createWriteBytes(dos, uid, "uid");
            if (!TextUtils.isEmpty(activity_Id + ""))
                createWriteBytes(dos, activity_Id + "", "activity_id");
            dos.writeBytes(twoHyphens + boundary + twoHyphens + end);
            dos.flush();
            InputStream is = httpURLConnection.getInputStream();
            InputStreamReader isr = new InputStreamReader(is, "utf-8");
            BufferedReader br = new BufferedReader(isr);
            result = br.readLine();
            if (result != null && !"".equals(result)) {
                message.setFlag(true);
                message.setMsg(result);
            } else {
                message.setFlag(false);
                message.setMsg("error");
            }
            dos.close();
            is.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return message;
    }

    public static ApiMessage getFindList(String sessionId) {
        // TODO Auto-generated method stub
        String result = new String();
        ApiMessage message = new ApiMessage();
        HttpURLConnection httpURLConnection = null;
        httpURLConnection = initHttpUrlConnection(ApiConstant.getTopList);
        try {
            DataOutputStream dos = new DataOutputStream(
                    httpURLConnection.getOutputStream());
            createWriteBytes(dos, sessionId, "session_id");
            dos.writeBytes(twoHyphens + boundary + twoHyphens + end);
            dos.flush();
            InputStream is = httpURLConnection.getInputStream();
            InputStreamReader isr = new InputStreamReader(is, "utf-8");
            BufferedReader br = new BufferedReader(isr);
            result = br.readLine();
            if (result != null && !"".equals(result)) {
                message.setFlag(true);
                message.setMsg(result);
            } else {
                message.setFlag(false);
                message.setMsg("error");
            }
            dos.close();
            is.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return message;
    }

    // 获取个人主页动态信息
    public static ApiMessage getPersonalFindList(String sessionId, int times,
                                                 String uid) {
        // TODO Auto-generated method stub
        String result = new String();
        ApiMessage message = new ApiMessage();
        HttpURLConnection httpURLConnection = null;
        httpURLConnection = initHttpUrlConnection(ApiConstant.getFindList);
        try {
            DataOutputStream dos = new DataOutputStream(
                    httpURLConnection.getOutputStream());
            createWriteBytes(dos, sessionId, "session_id");
            createWriteBytes(dos, times + "", "times");
            if (!TextUtils.isEmpty(uid))
                createWriteBytes(dos, uid, "uid");
            dos.writeBytes(twoHyphens + boundary + twoHyphens + end);
            dos.flush();
            InputStream is = httpURLConnection.getInputStream();
            InputStreamReader isr = new InputStreamReader(is, "utf-8");
            BufferedReader br = new BufferedReader(isr);
            result = br.readLine();
            if (result != null && !"".equals(result)) {
                message.setFlag(true);
                message.setMsg(result);
            } else {
                message.setFlag(false);
                message.setMsg("error");
            }
            dos.close();
            is.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return message;
    }

    public static ApiMessage addFind(String sessionId, String fcontent,
                                     String comefrom, ArrayList<String> pics, String topiccat,
                                     String datasource, String activity_id, String title_name) {
        // TODO Auto-generated method stub
        String result = new String();
        ApiMessage message = new ApiMessage();
        HttpURLConnection httpURLConnection = initHttpUrlConnection(ApiConstant.addFind);
        httpURLConnection.setConnectTimeout(20*1000);
        httpURLConnection.setReadTimeout(20*1000);
        try {
            DataOutputStream dos = new DataOutputStream(
                    httpURLConnection.getOutputStream());
            createWriteBytes(dos, sessionId, "session_id");
            createChineseWriteBytes(dos, fcontent, "content");
            createChineseWriteBytes(dos, comefrom, "comefrom");
            createChineseWriteBytes(dos, topiccat, "topiccat");
            createChineseWriteBytes(dos, "android", "datasource");
            createChineseWriteBytes(dos, activity_id, "activity_id");
            createChineseWriteBytes(dos, title_name, "title_name");
            for (int i = 0, j = pics.size(); i < j; i++) {
                dos.writeBytes(twoHyphens + boundary + end);
                String picAddress = pics.get(i);
                dos.writeBytes("Content-Disposition: form-data; name=\"thumb[]\"; filename=\""
                        + picAddress.substring(picAddress.lastIndexOf("/") + 1)
                        + "\"" + end);
                dos.writeBytes(end);
                FileInputStream fis = new FileInputStream(picAddress);
                byte[] buffer = new byte[8192]; // 8k
                int count = 0;
                while ((count = fis.read(buffer)) != -1) {
                    dos.write(buffer, 0, count);
                }
                fis.close();
                dos.writeBytes(end);
            }
            dos.writeBytes(twoHyphens + boundary + twoHyphens + end);
            dos.flush();
            InputStream is = httpURLConnection.getInputStream();
            InputStreamReader isr = new InputStreamReader(is, "utf-8");
            BufferedReader br = new BufferedReader(isr);
            result = br.readLine();
            if (result != null && !"".equals(result)) {
                message.setFlag(true);
                message.setMsg(result);
            } else {
                message.setFlag(false);
                message.setMsg("error");
            }
            dos.close();
            is.close();

        } catch (ConnectTimeoutException e) {
            e.printStackTrace();
                message.setFlag(false);
                message.setMsg("SocketTimeoutException");
        } catch (SocketTimeoutException e) {
            e.printStackTrace();
                message.setFlag(false);
                message.setMsg("SocketTimeoutException");
        }  catch (Exception e) {
            e.printStackTrace();
        }
        return message;
    }

    public static ApiMessage delFind(String sessionId, String findId) {
        // TODO Auto-generated method stub
        String result = new String();
        ApiMessage message = new ApiMessage();
        HttpURLConnection httpURLConnection = initHttpUrlConnection(ApiConstant.delFind);
        try {
            DataOutputStream dos = new DataOutputStream(
                    httpURLConnection.getOutputStream());
            createWriteBytes(dos, sessionId, "session_id");
            createWriteBytes(dos, findId, "find_id");
            dos.writeBytes(twoHyphens + boundary + twoHyphens + end);
            dos.flush();
            InputStream is = httpURLConnection.getInputStream();
            InputStreamReader isr = new InputStreamReader(is, "utf-8");
            BufferedReader br = new BufferedReader(isr);
            result = br.readLine();
            if (result != null && !"".equals(result)) {
                message.setFlag(true);
                message.setMsg(result);
            } else {
                message.setFlag(false);
                message.setMsg("error");
            }
            dos.close();
            is.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return message;
    }

    public static ApiMessage TopFind(String sessionId, String findId,
                                     String toptime) {
        // TODO Auto-generated method stub
        String result = new String();
        ApiMessage message = new ApiMessage();
        HttpURLConnection httpURLConnection = initHttpUrlConnection(ApiConstant.topFind);
        try {
            DataOutputStream dos = new DataOutputStream(
                    httpURLConnection.getOutputStream());
            createWriteBytes(dos, sessionId, "session_id");
            createWriteBytes(dos, findId, "find_id");
            createWriteBytes(dos, toptime, "top_time");
            dos.writeBytes(twoHyphens + boundary + twoHyphens + end);
            dos.flush();
            InputStream is = httpURLConnection.getInputStream();
            InputStreamReader isr = new InputStreamReader(is, "utf-8");
            BufferedReader br = new BufferedReader(isr);
            result = br.readLine();
            if (result != null && !"".equals(result)) {
                message.setFlag(true);
                message.setMsg(result);
            } else {
                message.setFlag(false);
                message.setMsg("error");
            }
            dos.close();
            is.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return message;
    }

    public static ApiMessage likeFind(String sessionId, String findId) {
        // TODO Auto-generated method stub
        String result = new String();
        ApiMessage message = new ApiMessage();
        HttpURLConnection httpURLConnection = initHttpUrlConnection(ApiConstant.likeFind);
        try {
            DataOutputStream dos = new DataOutputStream(
                    httpURLConnection.getOutputStream());
            createWriteBytes(dos, sessionId, "session_id");
            createWriteBytes(dos, findId, "find_id");
            createWriteBytes(dos, "android", "datasource");
            dos.writeBytes(twoHyphens + boundary + twoHyphens + end);
            dos.flush();
            InputStream is = httpURLConnection.getInputStream();
            InputStreamReader isr = new InputStreamReader(is, "utf-8");
            BufferedReader br = new BufferedReader(isr);
            result = br.readLine();
            if (result != null && !"".equals(result)) {
                message.setFlag(true);
                message.setMsg(result);
            } else {
                message.setFlag(false);
                message.setMsg("error");
            }
            dos.close();
            is.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return message;
    }

    public static ApiMessage DelTopFind(String sessionId, String findId) {
        // TODO Auto-generated method stub
        String result = new String();
        ApiMessage message = new ApiMessage();
        HttpURLConnection httpURLConnection = initHttpUrlConnection(ApiConstant.deltopFind);
        try {
            DataOutputStream dos = new DataOutputStream(
                    httpURLConnection.getOutputStream());
            createWriteBytes(dos, sessionId, "session_id");
            createWriteBytes(dos, findId, "find_id");
            dos.writeBytes(twoHyphens + boundary + twoHyphens + end);
            dos.flush();
            InputStream is = httpURLConnection.getInputStream();
            InputStreamReader isr = new InputStreamReader(is, "utf-8");
            BufferedReader br = new BufferedReader(isr);
            result = br.readLine();
            if (result != null && !"".equals(result)) {
                message.setFlag(true);
                message.setMsg(result);
            } else {
                message.setFlag(false);
                message.setMsg("error");
            }
            dos.close();
            is.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return message;
    }

    public static ApiMessage addComment(String sessionId, String findId,
                                        String comment, String wav, String wavtime, String toId) {
        // TODO Auto-generated method stub
        String result = new String();
        ApiMessage message = new ApiMessage();
        HttpURLConnection httpURLConnection = initHttpUrlConnection(ApiConstant.addComment);
        try {
            DataOutputStream dos = new DataOutputStream(
                    httpURLConnection.getOutputStream());
            createWriteBytes(dos, sessionId, "session_id");
            createWriteBytes(dos, findId, "find_id");
            createWriteBytes(dos, "android", "datasource");
            if (!TextUtils.isEmpty(comment))
                createChineseWriteBytes(dos, comment, "content");
            if (!TextUtils.isEmpty(wav)) {
                dos.writeBytes(twoHyphens + boundary + end);
                dos.writeBytes("Content-Disposition: form-data; name=\"wav\"; filename=\""
                        + wav.substring(wav.lastIndexOf("/") + 1) + "\"" + end);
                dos.writeBytes(end);
                FileInputStream fis = new FileInputStream(wav);
                byte[] buffer = new byte[8192]; // 8k
                int count = 0;
                while ((count = fis.read(buffer)) != -1) {
                    dos.write(buffer, 0, count);
                }
                fis.close();
                dos.writeBytes(end);
            }
            if (!TextUtils.isEmpty(wavtime))
                createWriteBytes(dos, wavtime, "wavtime");
            if (toId != null)
                createWriteBytes(dos, toId, "to_id");
            dos.writeBytes(end);
            dos.writeBytes(twoHyphens + boundary + twoHyphens + end);
            dos.flush();
            InputStream is = httpURLConnection.getInputStream();
            InputStreamReader isr = new InputStreamReader(is, "utf-8");
            BufferedReader br = new BufferedReader(isr);
            result = br.readLine();
            if (result != null && !"".equals(result)) {
                message.setFlag(true);
                message.setMsg(result);
            } else {
                message.setFlag(false);
                message.setMsg("error");
            }
            dos.close();
            is.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return message;
    }

    /**
     * 添加系统发现
     *
     * @param sessionId
     * @param sysfindId
     * @param comment
     * @param wav
     * @param wavtime
     * @param toId
     * @return
     */
    public static ApiMessage addSysComment(String sessionId, String sysfindId,
                                           String comment, String wav, String wavtime, String toId) {
        // TODO Auto-generated method stub
        String result = new String();
        ApiMessage message = new ApiMessage();
        HttpURLConnection httpURLConnection = initHttpUrlConnection(ApiConstant.addComment);
        try {
            DataOutputStream dos = new DataOutputStream(
                    httpURLConnection.getOutputStream());
            createWriteBytes(dos, sessionId, "session_id");
            createWriteBytes(dos, sysfindId, "sfind_id");
            if (!TextUtils.isEmpty(comment))
                createChineseWriteBytes(dos, comment, "content");
            if (!TextUtils.isEmpty(wav)) {
                dos.writeBytes(twoHyphens + boundary + end);
                dos.writeBytes("Content-Disposition: form-data; name=\"wav\"; filename=\""
                        + wav.substring(wav.lastIndexOf("/") + 1) + "\"" + end);
                dos.writeBytes(end);
                FileInputStream fis = new FileInputStream(wav);
                byte[] buffer = new byte[8192]; // 8k
                int count = 0;
                while ((count = fis.read(buffer)) != -1) {
                    dos.write(buffer, 0, count);
                }
                fis.close();
                dos.writeBytes(end);
            }
            if (!TextUtils.isEmpty(wavtime))
                createWriteBytes(dos, wavtime, "wavtime");
            if (toId != null)
                createWriteBytes(dos, toId, "to_id");
            dos.writeBytes(end);
            dos.writeBytes(twoHyphens + boundary + twoHyphens + end);
            dos.flush();
            InputStream is = httpURLConnection.getInputStream();
            InputStreamReader isr = new InputStreamReader(is, "utf-8");
            BufferedReader br = new BufferedReader(isr);
            result = br.readLine();
            if (result != null && !"".equals(result)) {
                message.setFlag(true);
                message.setMsg(result);
            } else {
                message.setFlag(false);
                message.setMsg("error");
            }
            dos.close();
            is.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return message;
    }

    public static ApiMessage updateFindBg(String sessionId, String findimg) {
        // TODO Auto-generated method stub
        String result = new String();
        ApiMessage message = new ApiMessage();
        HttpURLConnection httpURLConnection = initHttpUrlConnection(ApiConstant.updateFindBg);
        try {
            DataOutputStream dos = new DataOutputStream(
                    httpURLConnection.getOutputStream());
            createWriteBytes(dos, sessionId, "session_id");
            dos.writeBytes(twoHyphens + boundary + end);
            dos.writeBytes("Content-Disposition: form-data; name=\"findimg\"; filename=\""
                    + findimg.substring(findimg.lastIndexOf("/") + 1)
                    + "\""
                    + end);
            dos.writeBytes(end);
            FileInputStream fis = new FileInputStream(findimg);
            byte[] buffer = new byte[8192]; // 8k
            int count = 0;
            while ((count = fis.read(buffer)) != -1) {
                dos.write(buffer, 0, count);
            }
            fis.close();
            dos.writeBytes(end);
            dos.writeBytes(twoHyphens + boundary + twoHyphens + end);
            dos.flush();
            InputStream is = httpURLConnection.getInputStream();
            InputStreamReader isr = new InputStreamReader(is, "utf-8");
            BufferedReader br = new BufferedReader(isr);
            result = br.readLine();
            if (result != null && !"".equals(result)) {
                message.setFlag(true);
                message.setMsg(result);
            } else {
                message.setFlag(false);
                message.setMsg("error");
            }
            dos.close();
            is.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return message;
    }

    public static ApiMessage updatePwNew(String verifyCode, String phoneno,
                                         String passwd) {
        // TODO Auto-generated method stub
        String result = new String();
        ApiMessage message = new ApiMessage();
        HttpURLConnection httpURLConnection = initHttpUrlConnection(ApiConstant.updatePwNew);
        try {
            DataOutputStream dos = new DataOutputStream(
                    httpURLConnection.getOutputStream());
            createWriteBytes(dos, verifyCode, "code");
            createWriteBytes(dos, phoneno, "phoneno");
            createWriteBytes(dos, passwd, "passwd");
            dos.writeBytes(twoHyphens + boundary + twoHyphens + end);
            dos.flush();
            InputStream is = httpURLConnection.getInputStream();
            InputStreamReader isr = new InputStreamReader(is, "utf-8");
            BufferedReader br = new BufferedReader(isr);
            result = br.readLine();
            if (result != null && !"".equals(result)) {
                message.setFlag(true);
                message.setMsg(result);
            } else {
                message.setFlag(false);
                message.setMsg("error");
            }
            dos.close();
            is.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return message;
    }

    public static ApiMessage adShow(String channelnum) {
        // TODO Auto-generated method stub
        String result = new String();
        ApiMessage message = new ApiMessage();
        HttpURLConnection httpURLConnection = initHttpUrlConnection(ApiConstant.adShow);
        try {
            DataOutputStream dos = new DataOutputStream(
                    httpURLConnection.getOutputStream());
            createWriteBytes(dos, channelnum, "channelnum");
            dos.writeBytes(twoHyphens + boundary + twoHyphens + end);
            dos.flush();
            InputStream is = httpURLConnection.getInputStream();
            InputStreamReader isr = new InputStreamReader(is, "utf-8");
            BufferedReader br = new BufferedReader(isr);
            result = br.readLine();
            if (result != null && !"".equals(result)) {
                message.setFlag(true);
                message.setMsg(result);
            } else {
                message.setFlag(false);
                message.setMsg("error");
            }
            is.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return message;
    }

    public static ApiMessage delComment(String sessionId, String commentId) {
        // TODO Auto-generated method stub
        String result = new String();
        ApiMessage message = new ApiMessage();
        HttpURLConnection httpURLConnection = initHttpUrlConnection(ApiConstant.delComment);
        try {
            DataOutputStream dos = new DataOutputStream(
                    httpURLConnection.getOutputStream());
            createWriteBytes(dos, sessionId, "session_id");
            createWriteBytes(dos, commentId, "comment_id");
            dos.writeBytes(twoHyphens + boundary + twoHyphens + end);
            dos.flush();
            InputStream is = httpURLConnection.getInputStream();
            InputStreamReader isr = new InputStreamReader(is, "utf-8");
            BufferedReader br = new BufferedReader(isr);
            result = br.readLine();
            if (result != null && !"".equals(result)) {
                message.setFlag(true);
                message.setMsg(result);
            } else {
                message.setFlag(false);
                message.setMsg("error");
            }
            dos.close();
            is.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return message;
    }

    /*
     * params[0]运动开始时间 (时间戳） params[1]运动持续时间 单位秒 params[2]消耗热量 千卡 params[3]运动速度
	 * 米/秒
	 */
    public static ApiMessage qqHealthTask(int typeId, double dis,
                                          int sportGoal, String[] params) {
        // TODO Auto-generated method stub
        ApiMessage info = new ApiMessage();
        String healthUrl = null;
        switch (typeId) {
            case SportsType.TYPE_WALK:
                healthUrl = "report_steps";
                break;
            case SportsType.TYPE_RUN:
                healthUrl = "report_running";
                break;
            case SportsType.TYPE_CYCLING:
                healthUrl = "report_riding";
                break;
            default:
                return info;
        }
        String url = ApiConstant.qqHealthBase + healthUrl;
        List<NameValuePair> localJSONObject = new ArrayList<NameValuePair>();
        SharedPreferences sp = SportsApp.getContext().getSharedPreferences(
                "user_login_info", Context.MODE_PRIVATE);
        localJSONObject.add(new BasicNameValuePair("access_token", sp
                .getString("access_token", "")));
        localJSONObject.add(new BasicNameValuePair("oauth_consumer_key",
                AllWeiboInfo.APP_ID));
        localJSONObject.add(new BasicNameValuePair("openid", sp.getString(
                AllWeiboInfo.TENCENT_QQZONE_OPEN_ID_KEY, "")));
        localJSONObject.add(new BasicNameValuePair("pf", "qzone"));
        localJSONObject.add(new BasicNameValuePair("time", params[0]));
        localJSONObject.add(new BasicNameValuePair("distance", dis + ""));
        localJSONObject.add(new BasicNameValuePair("duration", params[1]));
        localJSONObject.add(new BasicNameValuePair("calories", params[2]));
        switch (typeId) {
            case SportsType.TYPE_WALK:
                if (sportGoal > 0) {
                    localJSONObject.add(new BasicNameValuePair("target",
                            SportTaskUtil.dis2step(sportGoal) + "")); // 单位步
                    localJSONObject.add(new BasicNameValuePair("achieve", dis
                            / sportGoal + ""));
                }
                localJSONObject.add(new BasicNameValuePair("steps", SportTaskUtil
                        .dis2step(dis) + ""));
                break;
            case SportsType.TYPE_RUN:
                if (sportGoal > 0) {
                    localJSONObject.add(new BasicNameValuePair("target",
                            SportTaskUtil.dis2step(sportGoal) + "")); // 单位步
                    localJSONObject.add(new BasicNameValuePair("achieve", dis
                            / sportGoal + ""));
                }
                localJSONObject.add(new BasicNameValuePair("steps", SportTaskUtil
                        .dis2step(dis) + ""));
                localJSONObject.add(new BasicNameValuePair("speed", params[3]));
                break;
            case SportsType.TYPE_CYCLING:
                if (sportGoal > 0) {
                    localJSONObject.add(new BasicNameValuePair("target", sportGoal
                            + "")); // 单位步
                    localJSONObject.add(new BasicNameValuePair("achieve", dis
                            / sportGoal + ""));
                }
                localJSONObject.add(new BasicNameValuePair("speed", params[3]));
                break;
            default:
                return info;
        }
        try {
            info = ApiNetwork.postByEntity(url, new UrlEncodedFormEntity(
                    localJSONObject, HTTP.UTF_8));
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return info;
    }

    private static void createWriteBytes(DataOutputStream dos, String parame,
                                         String name) {
        // TODO Auto-generated method stub
        try {
            dos.writeBytes(twoHyphens + boundary + end);
            dos.writeBytes("Content-Disposition: form-data; name=\"" + name
                    + "\"" + end);
            dos.writeBytes(end);
            dos.writeBytes(parame);
            dos.writeBytes(end);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private static void createChineseWriteBytes(DataOutputStream dos,
                                                String parame, String name) {
        // TODO Auto-generated method stub
        try {
            dos.writeBytes(twoHyphens + boundary + end);
            dos.writeBytes("Content-Disposition: form-data; name=\"" + name
                    + "\"" + end);
            dos.writeBytes(end);
            dos.write(parame.getBytes());
            dos.writeBytes(end);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private static HttpURLConnection initHttpUrlConnection(String getfindlist) {
        // TODO Auto-generated method stub
        URL url = null;
        try {
            url = new URL(ApiConstant.DATA_URL + getfindlist);
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        HttpURLConnection httpURLConnection = null;
        try {
            httpURLConnection = (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        httpURLConnection.setChunkedStreamingMode(128 * 1024);// 128K
        httpURLConnection.setDoInput(true);
        httpURLConnection.setDoOutput(true);
        httpURLConnection.setUseCaches(false);
        httpURLConnection.setConnectTimeout(3000);
        httpURLConnection.setReadTimeout(3000);
        try {
            httpURLConnection.setRequestMethod("POST");
        } catch (ProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        httpURLConnection.setRequestProperty("Connection", "Keep-Alive");
        httpURLConnection.setRequestProperty("Charset", "UTF-8");
        httpURLConnection.setRequestProperty("Content-Type",
                "multipart/form-data;boundary=" + boundary);
        return httpURLConnection;
    }

    public static ApiMessage getSportsRecordInfo(String userName,
                                                 String sessionId, String uId, String sportsDate, String sportsType,
                                                 String sportsDistance, String sportsCalorie, String sportsVelocity,
                                                 String sportsTime, String img, String map, String datasource) {
        String result = new String();
        ApiMessage message = new ApiMessage();
        try {
            URL url = new URL(ApiConstant.DATA_URL
                    + ApiConstant.getSportsRecordInfo);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url
                    .openConnection();
            httpURLConnection.setChunkedStreamingMode(128 * 1024);// 128K
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setUseCaches(false);
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setRequestProperty("Connection", "Keep-Alive");
            httpURLConnection.setRequestProperty("Charset", "UTF-8");
            httpURLConnection.setRequestProperty("Content-Type",
                    "multipart/form-data;boundary=" + boundary);

            DataOutputStream dos = new DataOutputStream(
                    httpURLConnection.getOutputStream());

            dos.writeBytes(twoHyphens + boundary + end);
            dos.writeBytes("Content-Disposition: form-data; name=\"user_name\""
                    + end);
            dos.writeBytes(end);
            dos.write(userName.getBytes());
            dos.writeBytes(end);

            dos.writeBytes(twoHyphens + boundary + end);
            dos.writeBytes("Content-Disposition: form-data; name=\"sessionId\""
                    + end);
            dos.writeBytes(end);
            dos.writeBytes(sessionId);
            dos.writeBytes(end);

            dos.writeBytes(twoHyphens + boundary + end);
            dos.writeBytes("Content-Disposition: form-data; name=\"uid\"" + end);
            dos.writeBytes(end);
            dos.writeBytes(uId);
            dos.writeBytes(end);

            dos.writeBytes(twoHyphens + boundary + end);
            dos.writeBytes("Content-Disposition: form-data; name=\"sportsdate\""
                    + end);
            dos.writeBytes(end);
            dos.writeBytes(sportsDate);
            dos.writeBytes(end);

            dos.writeBytes(twoHyphens + boundary + end);
            dos.writeBytes("Content-Disposition: form-data; name=\"sports_type\""
                    + end);
            dos.writeBytes(end);
            dos.writeBytes(sportsType);
            dos.writeBytes(end);

            dos.writeBytes(twoHyphens + boundary + end);
            dos.writeBytes("Content-Disposition: form-data; name=\"sports_distance\""
                    + end);
            dos.writeBytes(end);
            dos.writeBytes(sportsDistance);
            dos.writeBytes(end);

            dos.writeBytes(twoHyphens + boundary + end);
            dos.writeBytes("Content-Disposition: form-data; name=\"sports_calorie\""
                    + end);
            dos.writeBytes(end);
            dos.writeBytes(sportsCalorie);
            dos.writeBytes(end);

            dos.writeBytes(twoHyphens + boundary + end);
            dos.writeBytes("Content-Disposition: form-data; name=\"sports_velocity\""
                    + end);
            dos.writeBytes(end);
            dos.writeBytes(sportsVelocity);
            dos.writeBytes(end);

            dos.writeBytes(twoHyphens + boundary + end);
            dos.writeBytes("Content-Disposition: form-data; name=\"sports_time\""
                    + end);
            dos.writeBytes(end);
            dos.writeBytes(sportsTime);
            dos.writeBytes(end);

            dos.writeBytes(twoHyphens + boundary + end);
            dos.writeBytes("Content-Disposition: form-data; name=\"img\"" + end);
            dos.writeBytes(end);
            dos.writeBytes(img);
            dos.writeBytes(end);

            dos.writeBytes(twoHyphens + boundary + end);
            dos.writeBytes("Content-Disposition: form-data; name=\"map\"; filename=\""
                    + map.substring(map.lastIndexOf("/") + 1) + "\"" + end);
            dos.writeBytes(end);

            FileInputStream fis = new FileInputStream(map);
            byte[] buffer = new byte[8192]; // 8k
            Log.e("weixinshare", "path = " + map);
            int count = 0;
            while ((count = fis.read(buffer)) != -1) {
                dos.write(buffer, 0, count);
            }
            fis.close();
            dos.writeBytes(end);

            dos.writeBytes(twoHyphens + boundary + end);
            dos.writeBytes("Content-Disposition: form-data; name=\"datasource\""
                    + end);
            dos.writeBytes(end);
            dos.writeBytes("android");
            dos.writeBytes(end);

            dos.writeBytes(twoHyphens + boundary + twoHyphens + end);
            dos.flush();

            InputStream is = httpURLConnection.getInputStream();
            InputStreamReader isr = new InputStreamReader(is, "utf-8");
            BufferedReader br = new BufferedReader(isr);
            result = br.readLine();
            if (result != null && !"".equals(result)) {
                message.setFlag(true);
                message.setMsg(result);
            } else {
                message.setFlag(false);
                message.setMsg("error");
            }
            dos.close();
            is.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return message;
    }

    // openequipment
    public static ApiMessage openequipment(String serial, String model) {
        JSONObject localJSONObject = new JSONObject();
        ApiMessage info = new ApiMessage();
        try {
            String url = ApiConstant.DATA_URL + ApiConstant.openequipment;
            localJSONObject.put("serial", serial);
            localJSONObject.put("model", model);
            info = ApiNetwork.post(url, localJSONObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }

    // 得到点赞列表
    public static ApiMessage getLikeLists(String session_id, int find_id,
                                          int uid) {
        // TODO Auto-generated method stub
        String result = new String();
        ApiMessage message = new ApiMessage();
        HttpURLConnection httpURLConnection = initHttpUrlConnection(ApiConstant.getHuoDongLikesList);
        try {
            DataOutputStream dos = new DataOutputStream(
                    httpURLConnection.getOutputStream());
            createWriteBytes(dos, session_id, "session_id");
            createWriteBytes(dos, find_id + "", "find_id");
            createWriteBytes(dos, uid + "", "uid");
            dos.writeBytes(twoHyphens + boundary + twoHyphens + end);
            dos.flush();
            InputStream is = httpURLConnection.getInputStream();
            InputStreamReader isr = new InputStreamReader(is, "utf-8");
            BufferedReader br = new BufferedReader(isr);
            result = br.readLine();
            if (result != null && !"".equals(result)) {
                message.setFlag(true);
                message.setMsg(result);
            } else {
                message.setFlag(false);
                message.setMsg("error");
            }
            dos.close();
            is.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return message;
    }

    // 得到运动圈发现列表
    public static ApiMessage getCircleFindLists(String session_id, int times,
                                                int uid, int type) {
        // TODO Auto-generated method stub
        String result = new String();
        ApiMessage message = new ApiMessage();
        HttpURLConnection httpURLConnection = initHttpUrlConnection(ApiConstant.getCircleFindList);
        try {
            DataOutputStream dos = new DataOutputStream(
                    httpURLConnection.getOutputStream());
            createWriteBytes(dos, session_id, "session_id");
            createWriteBytes(dos, times + "", "times");
            createWriteBytes(dos, uid + "", "uid");
            createWriteBytes(dos, type + "", "type");
            dos.writeBytes(twoHyphens + boundary + twoHyphens + end);
            dos.flush();
            InputStream is = httpURLConnection.getInputStream();
            InputStreamReader isr = new InputStreamReader(is, "utf-8");
            BufferedReader br = new BufferedReader(isr);
            result = br.readLine();
            if (result != null && !"".equals(result)) {
                message.setFlag(true);
                message.setMsg(result);
            } else {
                message.setFlag(false);
                message.setMsg("error");
            }
            dos.close();
            is.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return message;
    }

    // 得到运动圈发现列表
    public static ApiMessage getCircleFindListsContent(String session_id,
                                                       int times, int id, int type, int list) {
        // TODO Auto-generated method stub
        String result = new String();
        ApiMessage message = new ApiMessage();
        HttpURLConnection httpURLConnection = initHttpUrlConnection(ApiConstant.getCircleFindListContent);
        try {
            DataOutputStream dos = new DataOutputStream(
                    httpURLConnection.getOutputStream());
            createWriteBytes(dos, session_id, "session_id");
            createWriteBytes(dos, times + "", "times");
            createWriteBytes(dos, id + "", "id");
            createWriteBytes(dos, type + "", "type");
            createWriteBytes(dos, list + "", "list");
            dos.writeBytes(twoHyphens + boundary + twoHyphens + end);
            dos.flush();
            InputStream is = httpURLConnection.getInputStream();
            InputStreamReader isr = new InputStreamReader(is, "utf-8");
            BufferedReader br = new BufferedReader(isr);
            result = br.readLine();
            if (result != null && !"".equals(result)) {
                message.setFlag(true);
                message.setMsg(result);
            } else {
                message.setFlag(false);
                message.setMsg("error");
            }
            dos.close();
            is.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return message;
    }

    // 上传今日步数
    public static ApiMessage uploadTepe(String sessionId, int step_num) {
        String end = "\r\n";
        String twoHyphens = "--";
        String boundary = "******";
        String result = new String();
        ApiMessage message = new ApiMessage();
        try {
            URL url = new URL(ApiConstant.DATA_URL
                    + ApiConstant.upDateSportsTemp);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url
                    .openConnection();
            httpURLConnection.setChunkedStreamingMode(128 * 1024);// 128K
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setUseCaches(false);
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setRequestProperty("Connection", "Keep-Alive");
            httpURLConnection.setRequestProperty("Charset", HTTP.UTF_8);
            httpURLConnection.setRequestProperty("Content-Type",
                    "multipart/form-data;boundary=" + boundary);

            DataOutputStream dos = new DataOutputStream(
                    httpURLConnection.getOutputStream());

            dos.writeBytes(twoHyphens + boundary + end);
            dos.writeBytes("Content-Disposition: form-data; name=\"sessionId\""
                    + end);
            dos.writeBytes(end);
            dos.writeBytes(sessionId);
            dos.writeBytes(end);

            dos.writeBytes(twoHyphens + boundary + end);
            dos.writeBytes("Content-Disposition: form-data; name=\"step_num\""
                    + end);
            dos.writeBytes(end);
            dos.writeBytes(Integer.toString(step_num));
            dos.writeBytes(end);

            dos.flush();

            InputStream is = httpURLConnection.getInputStream();
            InputStreamReader isr = new InputStreamReader(is, "utf-8");
            BufferedReader br = new BufferedReader(isr);
            result = br.readLine();
            if (result != null && !"".equals(result)) {
                message.setFlag(true);
                message.setMsg(result);
                // back = ApiJsonParser.ApiParserBack(result);
            } else {
                message.setFlag(false);
                message.setMsg("error");
            }
            dos.close();
            is.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return message;
    }

    // 第一次登录得到当前用户的今日步数
    public static ApiMessage getTodaySportsTemp(String session_id) {
        // TODO Auto-generated method stub
        String result = new String();
        ApiMessage message = new ApiMessage();
        HttpURLConnection httpURLConnection = initHttpUrlConnection(ApiConstant.uploadSportsTemp);
        try {
            DataOutputStream dos = new DataOutputStream(
                    httpURLConnection.getOutputStream());
            createWriteBytes(dos, session_id, "sessionId");
            dos.writeBytes(twoHyphens + boundary + twoHyphens + end);
            dos.flush();
            InputStream is = httpURLConnection.getInputStream();
            InputStreamReader isr = new InputStreamReader(is, "utf-8");
            BufferedReader br = new BufferedReader(isr);
            result = br.readLine();
            if (result != null && !"".equals(result)) {
                message.setFlag(true);
                message.setMsg(result);
            } else {
                message.setFlag(false);
                message.setMsg("error");
            }
            dos.close();
            is.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return message;
    }

    // 上传睡眠数据 isSleepOrXlv 是true时表示睡眠 false表示心率
    public static ApiMessage uploadSleepData(String sessionId,
                                             SleepEffect sleepEffect, boolean isSleepOrXlv) {
        String end = "\r\n";
        String twoHyphens = "--";
        String boundary = "******";
        String result = new String();
        ApiMessage message = new ApiMessage();
        try {
            URL url = null;
            if (isSleepOrXlv) {
                url = new URL(ApiConstant.DATA_URL
                        + ApiConstant.uploadSleepData);
            } else {
                url = new URL(ApiConstant.DATA_URL
                        + ApiConstant.uploadXinlvData);
            }
            HttpURLConnection httpURLConnection = (HttpURLConnection) url
                    .openConnection();
            httpURLConnection.setChunkedStreamingMode(128 * 1024);// 128K
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setUseCaches(false);
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setRequestProperty("Connection", "Keep-Alive");
            httpURLConnection.setRequestProperty("Charset", HTTP.UTF_8);
            httpURLConnection.setRequestProperty("Content-Type",
                    "multipart/form-data;boundary=" + boundary);

            DataOutputStream dos = new DataOutputStream(
                    httpURLConnection.getOutputStream());

            dos.writeBytes(twoHyphens + boundary + end);
            dos.writeBytes("Content-Disposition: form-data; name=\"sessionId\""
                    + end);
            dos.writeBytes(end);
            dos.writeBytes(sessionId);
            dos.writeBytes(end);

            dos.writeBytes(twoHyphens + boundary + end);
            dos.writeBytes("Content-Disposition: form-data; name=\"starttime\""
                    + end);
            dos.writeBytes(end);
            dos.writeBytes(sleepEffect.getStarttime());
            dos.writeBytes(end);

            dos.writeBytes(twoHyphens + boundary + end);
            dos.writeBytes("Content-Disposition: form-data; name=\"endtime\""
                    + end);
            dos.writeBytes(end);
            dos.writeBytes(sleepEffect.getEndtime());
            dos.writeBytes(end);

            if (isSleepOrXlv) {
                dos.writeBytes(twoHyphens + boundary + end);
                dos.writeBytes("Content-Disposition: form-data; name=\"sleep_effect\""
                        + end);
                dos.writeBytes(end);
                dos.writeBytes(sleepEffect.getHart_rate());
                dos.writeBytes(end);

                dos.writeBytes(twoHyphens + boundary + end);
                dos.writeBytes("Content-Disposition: form-data; name=\"shock_num\""
                        + end);
                dos.writeBytes(end);
                dos.writeBytes(sleepEffect.getShock_num());
                dos.writeBytes(end);

                dos.writeBytes(twoHyphens + boundary + end);
                dos.writeBytes("Content-Disposition: form-data; name=\"bright_time\""
                        + end);
                dos.writeBytes(end);
                dos.writeBytes(sleepEffect.getBright_time());
                dos.writeBytes(end);
            } else {
                dos.writeBytes(twoHyphens + boundary + end);
                dos.writeBytes("Content-Disposition: form-data; name=\"hart_rate\""
                        + end);
                dos.writeBytes(end);
                dos.writeBytes(sleepEffect.getHart_rate());
                dos.writeBytes(end);
            }

            dos.writeBytes(twoHyphens + boundary + end);
            dos.writeBytes("Content-Disposition: form-data; name=\"unique_id\""
                    + end);
            dos.writeBytes(end);
            dos.writeBytes(sleepEffect.getUnique_id());
            dos.writeBytes(end);

            dos.flush();

            InputStream is = httpURLConnection.getInputStream();
            InputStreamReader isr = new InputStreamReader(is, "utf-8");
            BufferedReader br = new BufferedReader(isr);
            result = br.readLine();
            if (result != null && !"".equals(result)) {
                message.setFlag(true);
                message.setMsg(result);
                // back = ApiJsonParser.ApiParserBack(result);
            } else {
                message.setFlag(false);
                message.setMsg("error");
            }
            dos.close();
            is.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return message;
    }

    // 获取运动总记录条数
    public static ApiMessage getSportscountNum(String sessionId, String userid) {
        // TODO Auto-generated method stub
        JSONObject localJSONObject = new JSONObject();
        ApiMessage info = new ApiMessage();
        try {
            String url = ApiConstant.DATA_URL + ApiConstant.SportscountNum;
            localJSONObject.put("uid", sessionId);
            localJSONObject.put("userid", userid);
            info = ApiNetwork.post(url, localJSONObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }

    // 得到健康心率睡眠数据
    public static ApiMessage getSleepDate(String sessionId, int times,
                                          boolean isXinlvOrsleep) {
        // TODO Auto-generated method stub
        String result = new String();
        ApiMessage message = new ApiMessage();
        HttpURLConnection httpURLConnection = null;
        if (isXinlvOrsleep) {
            httpURLConnection = initHttpUrlConnection(ApiConstant.getSleepDate);
        } else {
            httpURLConnection = initHttpUrlConnection(ApiConstant.getXinlvDate);
        }
        try {
            DataOutputStream dos = new DataOutputStream(
                    httpURLConnection.getOutputStream());
            createWriteBytes(dos, sessionId, "sessionId");
            createWriteBytes(dos, times + "", "page");
            dos.writeBytes(twoHyphens + boundary + twoHyphens + end);
            dos.flush();
            InputStream is = httpURLConnection.getInputStream();
            InputStreamReader isr = new InputStreamReader(is, "utf-8");
            BufferedReader br = new BufferedReader(isr);
            result = br.readLine();
            if (result != null && !"".equals(result)) {
                message.setFlag(true);
                message.setMsg(result);
            } else {
                message.setFlag(false);
                message.setMsg("error");
            }
            dos.close();
            is.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return message;
    }

    // 删除个别健康心律
    public static ApiMessage deleteXinlv(String sessionid, String unique_id) {
        JSONObject localJSONObject = new JSONObject();
        ApiMessage info = new ApiMessage();
        try {
            String url = ApiConstant.DATA_URL + ApiConstant.deleteXinlv;
            localJSONObject.put("sessionId", sessionid);
            localJSONObject.put("unique_id", unique_id);
            info = ApiNetwork.post(url, localJSONObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }

    // 整体删除心律
    public static ApiMessage deleteXinlv(String sessionid) {
        JSONObject localJSONObject = new JSONObject();
        ApiMessage info = new ApiMessage();
        try {
            String url = ApiConstant.DATA_URL + ApiConstant.deleteXinlv;
            localJSONObject.put("sessionId", sessionid);
            info = ApiNetwork.post(url, localJSONObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }

    // 盘点最后一张图片生成接口
    public static ApiMessage panDianGetImage(String userid) {
        // TODO Auto-generated method stub
        JSONObject localJSONObject = new JSONObject();
        ApiMessage info = new ApiMessage();
        try {
            String url = ApiConstant.DATA_URL + ApiConstant.panDianGetImage;
            localJSONObject.put("uid", userid);
            info = ApiNetwork.post(url, localJSONObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }

    // 健康统计的添加 type_id 1表示睡眠，2表示心率 activity_id是11 12的时候才添加属性
    public static ApiMessage healthdatacount(String sessionid, int type_id,
                                             String click_time, int activity_id) {
        // TODO Auto-generated method stub
        JSONObject localJSONObject = new JSONObject();
        ApiMessage info = new ApiMessage();
        try {
            String url = ApiConstant.DATA_URL + ApiConstant.healthdatacount;
            localJSONObject.put("sessionId", sessionid);
            localJSONObject.put("type_id", type_id);
            localJSONObject.put("click_time", click_time);
            if (type_id == 11 || type_id == 11) {
                localJSONObject.put("activity_id", activity_id);
            }
            info = ApiNetwork.post(url, localJSONObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }

    // 增加用户登陆时长
    public static ApiMessage addUserLoginTime(String sessionid) {
        // TODO Auto-generated method stub
        JSONObject localJSONObject = new JSONObject();
        ApiMessage info = new ApiMessage();
        try {
            String url = ApiConstant.DATA_URL + ApiConstant.addUserLoginTime;
            localJSONObject.put("sessionId", sessionid);
            info = ApiNetwork.post(url, localJSONObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }

    // 获取训练计划的列表
    public static ApiMessage getTrainlist(String sessionid) {
        // TODO Auto-generated method stub
        JSONObject localJSONObject = new JSONObject();
        ApiMessage info = new ApiMessage();
        try {
            String url = ApiConstant.DATA_URL + ApiConstant.getTrainlist;
            localJSONObject.put("sessionId", sessionid);
            info = ApiNetwork.post(url, localJSONObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }

    // 获取训练计划的详情
    public static ApiMessage getTraininfo(String sessionid, int id) {
        // TODO Auto-generated method stub
        JSONObject localJSONObject = new JSONObject();
        ApiMessage info = new ApiMessage();
        try {
            String url = ApiConstant.DATA_URL + ApiConstant.getTraininfo;
            localJSONObject.put("sessionId", sessionid);
            localJSONObject.put("id", id);
            info = ApiNetwork.post(url, localJSONObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }

    public static ApiMessage getTrainActionInfo(String sessionId,
                                                int trainActionId) {
        // TODO Auto-generated method stub
        JSONObject localJSONObject = new JSONObject();
        ApiMessage info = new ApiMessage();
        try {
            String url = ApiConstant.DATA_URL + ApiConstant.getTrainActionInfo;
            localJSONObject.put("sessionId", sessionId);
            localJSONObject.put("id", trainActionId);
            info = ApiNetwork.post(url, localJSONObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }

    public static ApiMessage getTrainTaskList(String sessionId, int page) {
        // TODO Auto-generated method stub
        JSONObject localJSONObject = new JSONObject();
        ApiMessage info = new ApiMessage();
        try {
            String url = ApiConstant.DATA_URL + ApiConstant.getTrainTaskList;
            localJSONObject.put("sessionId", sessionId);
            localJSONObject.put("page", page);
            info = ApiNetwork.post(url, localJSONObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }

    public static ApiMessage getTotalTrainTask(String sessionId) {
        // TODO Auto-generated method stub
        JSONObject localJSONObject = new JSONObject();
        ApiMessage info = new ApiMessage();
        try {
            String url = ApiConstant.DATA_URL + ApiConstant.getTotalTrainTask;
            localJSONObject.put("sessionId", sessionId);
            info = ApiNetwork.post(url, localJSONObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }

    public static ApiMessage addTrainRecord(String sessionId, int train_id,
                                            int traintime, double train_calorie, String train_action,
                                            String train_position, int train_completion,
                                            String train_starttime, String train_endtime, int is_total,
                                            String unique_id) {
        // TODO Auto-generated method stub
        JSONObject localJSONObject = new JSONObject();
        ApiMessage info = new ApiMessage();
        try {
            String url = ApiConstant.DATA_URL + ApiConstant.addTrainRecord;
            localJSONObject.put("sessionId", sessionId);
            localJSONObject.put("train_id", train_id);
            localJSONObject.put("traintime", traintime);
            localJSONObject.put("train_calorie", train_calorie);
            localJSONObject.put("train_action", train_action);
            localJSONObject.put("train_position", train_position);
            localJSONObject.put("train_completion", train_completion);
            localJSONObject.put("train_starttime", train_starttime);
            localJSONObject.put("train_endtime", train_endtime);
            localJSONObject.put("is_total", is_total);
            localJSONObject.put("unique_id", unique_id);
            localJSONObject.put("datasource", "android");
            Log.e("develop_debug", localJSONObject.toString());
            info = ApiNetwork.post(url, localJSONObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }

    public static ApiMessage GetCurrentDayBuShu(String sessionId, String day) {
        // TODO Auto-generated method stub
        JSONObject localJSONObject = new JSONObject();
        ApiMessage info = new ApiMessage();
        try {
            String url = ApiConstant.DATA_URL + ApiConstant.getCurrentDayBuShu;
            localJSONObject.put("sessionId", sessionId);
            localJSONObject.put("day", day);
            info = ApiNetwork.post(url, localJSONObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }

    public static ApiMessage GetAllDayBuShu(String sessionId, int page) {
        // TODO Auto-generated method stub
        JSONObject localJSONObject = new JSONObject();
        ApiMessage info = new ApiMessage();
        try {
            String url = ApiConstant.DATA_URL + ApiConstant.getAllDayBuShu;
            localJSONObject.put("sessionId", sessionId);
            localJSONObject.put("page", page);
            info = ApiNetwork.post(url, localJSONObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }

    public static ApiMessage saveBuShuTongJiToNetWork(String sessionId,
                                                      int step_num, String day, String appName) {
        // TODO Auto-generated method stub
        JSONObject localJSONObject = new JSONObject();
        ApiMessage info = new ApiMessage();
        try {
            String url = ApiConstant.DATA_URL
                    + ApiConstant.saveBuShuTongJiToNetWork;
            localJSONObject.put("sessionId", sessionId);
            localJSONObject.put("step_num", step_num);
            localJSONObject.put("day", day);
            localJSONObject.put("datasource", "android");
            localJSONObject.put("application_name", appName);
            info = ApiNetwork.post(url, localJSONObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }

    public static ApiMessage saveBuShuTongJiListToNetWork(String sessionId, ArrayList<BuShuTongJiDetail> list,
                                                           String datasource, String application_name) {
        // TODO Auto-generated method stub
        JSONObject localJSONObject = new JSONObject();
        JSONObject object;
        ApiMessage info = new ApiMessage();

        try {
            String url = ApiConstant.DATA_URL + ApiConstant.uploadBuShuList;
            localJSONObject.put("sessionId", sessionId);
            //初始化JSONArray对象，并添加数据
            JSONArray array = new JSONArray();
            for (int i = 0; i < list.size(); i++) {
                object = new JSONObject();
                object.put("step_num", list.get(i).getStep_num());
                object.put("day", list.get(i).getDay());
                object.put("datasource", datasource);
                object.put("application_name", application_name);
                array.put(object);
            }
            localJSONObject.put("content1", array.toString());
            info = ApiNetwork.post(url, localJSONObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }

    public static ApiMessage GetShareData(int id) {
        // TODO Auto-generated method stub
        JSONObject localJSONObject = new JSONObject();
        ApiMessage info = new ApiMessage();
        try {
            String url = ApiConstant.DATA_URL + ApiConstant.getShareData;
            localJSONObject.put("id", id);
            info = ApiNetwork.post(url, localJSONObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }

    // 外部活动最新的提取
    public static ApiMessage getExternalActive(String sessionId, int typeid,
                                               String channelnum) {
        // TODO Auto-generated method stub
        JSONObject localJSONObject = new JSONObject();
        ApiMessage info = new ApiMessage();
        try {
            String url = ApiConstant.DATA_URL + ApiConstant.activein;
            localJSONObject.put("sessionId", sessionId);
            localJSONObject.put("typeid", typeid);
            localJSONObject.put("channelnum", channelnum);
            info = ApiNetwork.post(url, localJSONObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }

    // 广场推荐列表
    public static ApiMessage getActInfo(String sessionId, String channelnum) {
        JSONObject locaJsonObject = new JSONObject();
        ApiMessage info = new ApiMessage();
        String url = ApiConstant.DATA_URL + ApiConstant.getActInfo;
        try {
            locaJsonObject.put("sessionId", sessionId);
            locaJsonObject.put("channelnum", channelnum);
            info = ApiNetwork.post(url, locaJsonObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }

    // 广场推荐列表信息
    public static ApiMessage getActInfo(String sessionId, String channelnum,
                                        int catid, int page) {

        JSONObject locaJsonObject = new JSONObject();
        ApiMessage info = new ApiMessage();
        String url = ApiConstant.DATA_URL + ApiConstant.getActInfos;
        try {
            locaJsonObject.put("sessionId", sessionId);
            locaJsonObject.put("channelnum", channelnum);
            locaJsonObject.put("catid", catid);
            locaJsonObject.put("page", page);
            info = ApiNetwork.post(url, locaJsonObject.toString());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return info;
    }

    // 获取统一下单信息
    public static ApiMessage getWXxiadanlist(String appid, String body,
                                             String mch_id, String nonce_str, String notify_url,
                                             String out_trade_no, String spbill_create_ip, int total_fee,
                                             String trade_type) {
        JSONObject localJSONObject = new JSONObject();
        ApiMessage info = new ApiMessage();
        try {
            String url = ApiConstant.DATA_URL + ApiConstant.getwxInfo;
            localJSONObject.put("appid", appid);
            localJSONObject.put("body", body);
            localJSONObject.put("mch_id", mch_id);
            localJSONObject.put("nonce_str", nonce_str);
            localJSONObject.put("notify_url", notify_url);
            localJSONObject.put("out_trade_no", out_trade_no);
            localJSONObject.put("spbill_create_ip", spbill_create_ip);
            localJSONObject.put("total_fee", total_fee);
            localJSONObject.put("trade_type", trade_type);
            info = ApiNetwork.post(url, localJSONObject.toString());

        } catch (Exception e) {
            // TODO: handle exception
        }
        return info;
    }

    // 获取预支付id
    public static ApiMessage getPaysing(String data, int flg) {
        // JSONObject localJSONObject = new JSONObject();
        ApiMessage info = new ApiMessage();
        String url = null;
        if (flg == 1) {

            url = ApiConstant.DATA_URL + ApiConstant.YeList;
        } else if (flg == 2) {
            url = ApiConstant.DATA_URL + ApiConstant.Payid;
        }
        try {
            // localJSONObject.put("data", data);
            info = ApiNetwork.post(url, data);

        } catch (Exception e) {
            // TODO: handle exception
        }
        return info;
    }

    public static ApiMessage getYePao(String sessionId, int id, int flg) {
        JSONObject localJSONObject = new JSONObject();
        ApiMessage info = new ApiMessage();
        String url = null;
        if (flg == 1) {
            // 夜跑活动下单
            url = ApiConstant.DATA_URL + ApiConstant.YePao;
        } else if (flg == 2) {
            // 金币商城下单
            url = ApiConstant.DATA_URL + ApiConstant.getwxData;
        }
        try {
            localJSONObject.put("sessionId", sessionId);
            localJSONObject.put("id", id);
            info = ApiNetwork.post(url, localJSONObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }

    // 商品详情
    public static ApiMessage getGoodsInfo(String sessionId, int id) {
        JSONObject localJSONObject = new JSONObject();
        ApiMessage info = new ApiMessage();
        String url = null;
        try {
            url = ApiConstant.DATA_URL + ApiConstant.getGoodsInfo;
            localJSONObject.put("sessionId", sessionId);
            localJSONObject.put("id", id);
            info = ApiNetwork.post(url, localJSONObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }

    // 获取支付状态
    public static ApiMessage getDindanId(String sessionId, String id, int flg) {
        JSONObject localJSONObject = new JSONObject();
        ApiMessage info = new ApiMessage();
        String url = null;
        if (flg == 1) {
            url = ApiConstant.DATA_URL + ApiConstant.YeDindanInfo;
        } else if (flg == 2) {
            url = ApiConstant.DATA_URL + ApiConstant.ShoppingMall;
        }
        try {
            localJSONObject.put("sessionId", sessionId);
            localJSONObject.put("id", id);
            info = ApiNetwork.post(url, localJSONObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }

    // 获取服务器时间
    public static ApiMessage getServertime() {
        JSONObject localJSONObject = new JSONObject();
        ApiMessage info = new ApiMessage();
        String url = null;
        url = ApiConstant.DATA_URL + ApiConstant.ServerTime;
        info = ApiNetwork.post(url, localJSONObject.toString());
        return info;
    }

    // openequipment
    public static ApiMessage getShareIcon(int uid, int id) {
        JSONObject localJSONObject = new JSONObject();
        ApiMessage info = new ApiMessage();
        try {
            String url = ApiConstant.DATA_URL + ApiConstant.getShareIcon;
            localJSONObject.put("id", id);
            localJSONObject.put("uid", uid);
            info = ApiNetwork.post(url, localJSONObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }

    public static ApiMessage getTitle(String sid, String id) {
        JSONObject localJSONObject = new JSONObject();
        ApiMessage info = new ApiMessage();
        try {
            String url = ApiConstant.DATA_URL + ApiConstant.getActionTitle;
            localJSONObject.put("sessionId", sid);
            localJSONObject.put("id", id);
            info = ApiNetwork.post(url, localJSONObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }

    //获取配速的数据
    public static ApiMessage getPeisu(String sessionId, int sports_id) {
        JSONObject localJSONObject = new JSONObject();
        ApiMessage info = new ApiMessage();
        try {
            String url = ApiConstant.DATA_URL + ApiConstant.getpeissu;
            localJSONObject.put("sessionId", sessionId);
            localJSONObject.put("sports_id", sports_id);
            info = ApiNetwork.post(url, localJSONObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }


    //上传运动记录加配速
    public static ApiMessage uploadSportsTwoInfo(int Limt, String sessionId,
                                                 int Sports_type_task, int sports_swim_type,
                                                 int monitoring_equipment, String start_time, int sprts_time,
                                                 double sport_distance, double sprots_Calorie,
                                                 double sprots_velocity,double Heart_rate,
                                                 String longitude_latitude, int step_num, int maptype,
                                                 String serial, String app_id,String velocity_list,String coordinate_list,
                                         String application_name, PeisuInfo content,int datatype) {
        JSONObject localJSONObject = new JSONObject();
        ApiMessage info = new ApiMessage();
        JSONObject object = null;
        try {
            String url = ApiConstant.DATA_URL + ApiConstant.uploadSportsTwoInfo;
            localJSONObject.put("sessionId", sessionId);
            localJSONObject.put("datatype",datatype);

            JSONObject sportsJsonObj=new JSONObject();
            sportsJsonObj.put("Limt",Limt);
            sportsJsonObj.put("sessionId",sessionId);
            sportsJsonObj.put("step_num",step_num);
            sportsJsonObj.put("Sports_type_task",Sports_type_task);
            sportsJsonObj.put("sports_swim_type",sports_swim_type);
            sportsJsonObj.put("monitoring_equipment",monitoring_equipment);
            sportsJsonObj.put("start_time",start_time);
            sportsJsonObj.put("sprts_time",sprts_time);
            sportsJsonObj.put("sport_distance",sport_distance);
            sportsJsonObj.put("sprots_Calorie",sprots_Calorie);
            sportsJsonObj.put("sprots_velocity",sprots_velocity);
            sportsJsonObj.put("Heart_rate",Heart_rate);
            sportsJsonObj.put("maptype",maptype);
            sportsJsonObj.put("longitude_latitude",longitude_latitude);
            if(coordinate_list==null){
                sportsJsonObj.put("velocity_list","");
            }else{
                sportsJsonObj.put("velocity_list",velocity_list);
            }
            if(coordinate_list==null){
                sportsJsonObj.put("coordinate_list","");
            }else{
                sportsJsonObj.put("coordinate_list",coordinate_list);
            }
            sportsJsonObj.put("datasource","android");
            sportsJsonObj.put("serial",serial);
            sportsJsonObj.put("app_id",app_id);
            localJSONObject.put("list1" ,sportsJsonObj.toString());


            JSONObject peisuJsonObj=new JSONObject();
            peisuJsonObj.put("datasource", "android");
            peisuJsonObj.put("application_name", application_name);
            //初始化JSONArray对象，并添加数据
            if(content!=null&&content.getListpeis()!=null&&content.getListpeis().size()>0){
                JSONArray array = new JSONArray();
                for (int i = 0; i < content.getListpeis().size(); i++) {
                    object = new JSONObject();
                    object.put("sport_distance", content.getListpeis().get(i).getSport_distance());
                    if (Sports_type_task == SportsType.TYPE_CLIMBING) {
                        object.put("sprots_height", content.getListpeis().get(i).getSprots_velocity());
                        object.put("GPS_type", content.getListpeis().get(i).getgPS_type());
                    } else {
                        object.put("sprots_velocity", content.getListpeis().get(i).getSprots_velocity());
                    }
                    object.put("sprots_time", content.getListpeis().get(i).getSprots_time());
                    array.put(object);
                }
                peisuJsonObj.put("content", array.toString());
            }else{
                peisuJsonObj.put("content", "");
            }
            localJSONObject.put("list2" ,peisuJsonObj.toString());

            info = ApiNetwork.post(url, localJSONObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            object=null;
        }
        return info;

    }


    //上传奖励和运动秀的总接口
    public static ApiMessage uploadShowAndRewardInfo(String sessionId, int coinStatus,
                                                     int coins, int log,String dakaContent) {
        JSONObject localJSONObject = new JSONObject();
        ApiMessage info = new ApiMessage();
        try {
            String url = ApiConstant.DATA_URL + ApiConstant.uploadShowAndRewardInfo;
            localJSONObject.put("sessionId", sessionId);

            JSONObject sportsJsonObj=new JSONObject();
            sportsJsonObj.put("coinStatus",coinStatus);
            sportsJsonObj.put("log",log);
            sportsJsonObj.put("coins",coins);
            sportsJsonObj.put("datasource","android");
            localJSONObject.put("list1" ,sportsJsonObj.toString());


            JSONObject peisuJsonObj=new JSONObject();
            peisuJsonObj.put("datasource", "android");
            peisuJsonObj.put("content", dakaContent);
            localJSONObject.put("list2" ,peisuJsonObj.toString());

            info = ApiNetwork.post(url, localJSONObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;

    }

    public static ApiMessage getMedalInfo(String sid, String id) {
        JSONObject localJSONObject = new JSONObject();
        ApiMessage info = new ApiMessage();
        try {
            String url = ApiConstant.DATA_URL + ApiConstant.getMedalInfo;
            localJSONObject.put("sessionId", sid);
            localJSONObject.put("id", id);
            info = ApiNetwork.post(url, localJSONObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }
}