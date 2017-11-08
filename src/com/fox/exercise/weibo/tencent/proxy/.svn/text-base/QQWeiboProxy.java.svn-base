package com.fox.exercise.weibo.tencent.proxy;


import com.fox.exercise.weibo.tencent.OAuthConstants;
import com.fox.exercise.weibo.tencent.OAuthV2;
import com.fox.exercise.weibo.tencent.OAuthV2Client;
//import com.mobile.tencent.weibo.util.CommonLog;
//import com.mobile.tencent.weibo.util.LogFactory;


public class QQWeiboProxy {

    /*
     * 请将以下三个参数替换成自己的应用值
     */
    public final static String CONSUMER_KEY = "801533900";                // appkey
    public final static String CONSUMER_SECRET = "0624e00f81def2916f53a54746cacb93";            // secret
    public final static String REDIRECT_URL = "http://kupao.mobifox.cn/Beauty//api.php?m=admin&a=index";                // url回调地址


//	private final CommonLog log = LogFactory.createLog();

    public final static String FORMAT = "json";
    public final static String CLIENT_IP = "210.22.149.18";


    private OAuthV2 mAuthV2;

    private static QQWeiboProxy mWeiboManager;


    public static synchronized QQWeiboProxy getInstance() {
        if (mWeiboManager == null) {
            mWeiboManager = new QQWeiboProxy();
        }

        return mWeiboManager;
    }


    private QQWeiboProxy() {

        mAuthV2 = new OAuthV2(REDIRECT_URL);
        mAuthV2.setClientId(CONSUMER_KEY);
        mAuthV2.setClientSecret(CONSUMER_SECRET);

    }

    public String getRedictUrl() {
        return REDIRECT_URL;
    }

    public String getAppKey() {
        return CONSUMER_KEY;
    }


    // 		 获得AUTHO认证URL地址
    public String getAuthoUrl() {

        String urlStr = OAuthV2Client.generateImplicitGrantUrl(mAuthV2);

        return urlStr;
    }

    public void setAccesToakenString(String token) {
        if (token != null) {
            mAuthV2.setAccessToken(token);
        }

    }

    public void setOpenid(String openID) {
        if (openID != null) {
            mAuthV2.setOpenid(openID);
        }
    }

    public void setOpenKey(String openKey) {
        if (openKey != null) {
            mAuthV2.setOpenkey(openKey);
        }
    }

    public void setExpireIn(String expireIn) {
        if (expireIn != null) {
            mAuthV2.setExpiresIn(expireIn);
        }
    }


    public boolean parseAccessTokenAndOpenId(String data) {
        if (data != null) {
            return OAuthV2Client.parseAccessTokenAndOpenId(data, mAuthV2);
        }

        return false;
    }

    public String getAccessToken() {
        return mAuthV2.getAccessToken();
    }

    public String getOpenID() {
        return mAuthV2.getOpenid();
    }

    public String getOpenKey() {
        return mAuthV2.getOpenkey();

    }

    public String getExpireIn() {
        return mAuthV2.getExpiresIn();
    }


    // 获取用户资料
    public String getUserInfo() {
        String reString = null;
        UserAPI mUserAPI = new UserAPI(OAuthConstants.OAUTH_VERSION_2_A);
        try {

            reString = mUserAPI.info(mAuthV2, FORMAT);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        mUserAPI.shutdownConnection();

        return reString;
    }

    // 发送文字微博
    public String sendWeibo(String content) {
        String rString = null;
        TAPI mTapi = new TAPI(OAuthConstants.OAUTH_VERSION_2_A);
        try {

            rString = mTapi.add(mAuthV2, FORMAT, content, CLIENT_IP);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        mTapi.shutdownConnection();

        return rString;
    }

    // 发送带图片微博
    public String sendPicWeibo(String content, String picpath) {
        String rString = null;
        TAPI mTapi = new TAPI(OAuthConstants.OAUTH_VERSION_2_A);
        try {

            rString = mTapi.addPic(mAuthV2, FORMAT, content, CLIENT_IP, picpath);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        mTapi.shutdownConnection();

        return rString;
    }

    // 获取粉丝列表
    public String getFans(String reqnum, String startindex) {
        String reString = null;
        FriendsAPI mFriendsAPI = new FriendsAPI(OAuthConstants.OAUTH_VERSION_2_A);
        try {

            reString = mFriendsAPI.fanslist(mAuthV2, FORMAT, reqnum, startindex, "1", "");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        mFriendsAPI.shutdownConnection();

        return reString;
    }

    // 获取昵称提示列表
    public String getNickTips(String match, String reqnum) {
        String reString = null;
        FriendsAPI mFriendsAPI = new FriendsAPI(OAuthConstants.OAUTH_VERSION_2_A);
        try {

            reString = mFriendsAPI.matchNickTips(mAuthV2, FORMAT, match, reqnum);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        mFriendsAPI.shutdownConnection();

        return reString;
    }
}
