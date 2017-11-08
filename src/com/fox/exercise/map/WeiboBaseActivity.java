package com.fox.exercise.map;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import cn.ingenic.indroidsync.SportsApp;

import com.fox.exercise.AllWeiboInfo;
import com.fox.exercise.QQOAuthActivity;
import com.fox.exercise.QQzoneActivity;
import com.fox.exercise.R;
import com.fox.exercise.ShareToQQzone;
import com.fox.exercise.ShareToTencentWeibo;
import com.fox.exercise.ShareToXinlangWeibo;
import com.fox.exercise.SportsUtilities;
import com.fox.exercise.ThemeUtils;
import com.fox.exercise.api.ApiBack;
import com.fox.exercise.api.ApiConstant.WeiboType;
import com.fox.exercise.api.ApiJsonParser;
import com.fox.exercise.api.ApiNetException;
import com.fox.exercise.api.MeimeiWeibo;
import com.fox.exercise.api.Util;
import com.fox.exercise.login.TencentAuthorizeActivity;
import com.fox.exercise.weibo._FakeX509TrustManager;
import com.fox.exercise.weibo.renren.AccessData;
import com.fox.exercise.weibo.renren.RenRenDataBaseContext;
import com.fox.exercise.weibo.renren.RenrenDataHelper;
import com.fox.exercise.weibo.sina.AccessInfo;
import com.fox.exercise.weibo.sina.AccessInfoHelper;
import com.fox.exercise.weibo.sina.AuthoSharePreference;
import com.fox.exercise.weibo.sina.InfoHelper;
import com.fox.exercise.weibo.sina.WeiboConstParam;
import com.fox.exercise.weibo.sina.oauth2.AccessToken;
import com.fox.exercise.weibo.sina.oauth2.AsyncWeiboRunner;
import com.fox.exercise.weibo.sina.oauth2.AsyncWeiboRunner.RequestListener;
import com.fox.exercise.weibo.sina.oauth2.DialogError;
import com.fox.exercise.weibo.sina.oauth2.Token;
import com.fox.exercise.weibo.sina.oauth2.Utility;
import com.fox.exercise.weibo.sina.oauth2.Weibo;
import com.fox.exercise.weibo.sina.oauth2.WeiboDialogListener;
import com.fox.exercise.weibo.sina.oauth2.WeiboException;
import com.fox.exercise.weibo.sina.oauth2.WeiboParameters;
import com.fox.exercise.weibo.tencent.DataBaseContext;
import com.fox.exercise.weibo.tencent.TencentDataHelper;
import com.fox.exercise.weibo.tencent.UserInfo;
import com.fox.exercise.weibo.tencent.proxy.QQWeiboProxy;
import com.fox.exercise.wxapi.WXEntryActivity;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXImageObject;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.umeng.analytics.MobclickAgent;

public class WeiboBaseActivity extends Fragment {
    protected static final String TAG = "WeiboBaseActivity";

    protected static Context mContext;
    protected String thisLarge;
    protected String thisMessage;
    protected String thisUrl;
    final static String QQ_NICK = "QQ_NICK";
    private static final int HUABAN_SHARE = 10;
    final static String QQ_TOKEN = "QQ_TOKEN";
    final static String QQ_OPID = "QQ_OPID";
    static String mQQToken = null;
    static String mQQOpid = null;
    private Dialog loginPregressDialog = null;
    private static final int QQ_OAUTH_FINISH = 11;
    private static final int QQZONE_SHARE = 12;
    protected boolean isForShare;
    private SportsApp mSportsApp;

    private static final int TENCENT = 0x0001;

    private boolean isFromShareImg = false;
    private QQWeiboProxy mQqWeiboProxy;
    private String access_token;
    private String expires_in;
    private String openID;
    private String openKey;
    private IWXAPI api;
    private static final int THUMB_SIZE = 150;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        mSportsApp = (SportsApp) getActivity().getApplication();
        initweibo();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//		unregisterReceiver(broadcastReceiver);
    }


    private AccessInfo xinlang_AccessInfo = null;
    private String user_name = null;

    private RenrenDataHelper renren_dataHelper;

    protected void shareToXinlangWeibo() {
        isForShare = true;
        if (ThemeUtils.getLocalIpAddress() == null) {
            Toast tip = Toast.makeText(mContext, R.string.error_cannot_access_net, Toast.LENGTH_SHORT);
            tip.show();
            return;
        }
        xinlang_AccessInfo = InfoHelper.getAccessInfo(mContext);
        Log.d(TAG, "accessInfo" + InfoHelper.getAccessInfo(mContext));
        if (xinlang_AccessInfo != null) {
            Log.d(TAG, "accessInfo is not null" + InfoHelper.getAccessInfo(mContext));
            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putString("thisLarge", thisLarge);
            Log.d(TAG, "thisLarge" + thisLarge);
            SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
            String time = format.format(System.currentTimeMillis());
            //thisMessage.replace(SportsDetails.userName, "[玫瑰]" + SportsDetails.userName + "[玫瑰]");
            bundle.putString("thisMessage", thisMessage + "@" + MeimeiWeibo.SINA_MEIMEI + " " + time);
            // bundle.putString("accessToken",
            // xinlang_AccessInfo.getAccessToken());
            // bundle.putString("accessSecret",
            // xinlang_AccessInfo.getAccessSecret());
            bundle.putSerializable("access_info", xinlang_AccessInfo);
            intent.putExtras(bundle);
            intent.setClass(mContext, ShareToXinlangWeibo.class);
            startActivityForResult(intent, 2);
            // finish();

        } else {
            // Oauth2.0
            // 隐式授权认证方式
            Weibo weibo = Weibo.getInstance();
            weibo.setupConsumerConfig(WeiboConstParam.CONSUMER_KEY, WeiboConstParam.CONSUMER_SECRET);

            // 此处回调页内容应该替换为与appkey对应的应用回调页
            weibo.setRedirectUrl(WeiboConstParam.REDIRECT_URL);

            // 启动认证
            weibo.authorize(getActivity(), new AuthDialogListener());

        }
    }

    public void goShareActivity(AccessInfo accessInfo) {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString("thisLarge", thisLarge);
        bundle.putString("thisMessage", thisMessage + "@" + MeimeiWeibo.SINA_MEIMEI);
        bundle.putSerializable("access_info", accessInfo);
        intent.putExtras(bundle);
        intent.setClass(mContext, ShareToXinlangWeibo.class);
        startActivityForResult(intent, 2);
        // startActivity(intent);
        // finish();
    }

    class AuthDialogListener implements WeiboDialogListener, RequestListener {

        private String uid = "";
        private String tokenString = "";

        @Override
        public void onComplete(Bundle values) {

            String token = values.getString("access_token");

            AuthoSharePreference.putToken(WeiboBaseActivity.mContext, token);

            tokenString = AuthoSharePreference.getToken(WeiboBaseActivity.mContext);

            AccessToken accessToken = new AccessToken(tokenString, WeiboConstParam.CONSUMER_SECRET);
            Log.d(TAG, "accessToken:" + accessToken);
            final Weibo weibo = Weibo.getInstance();
            weibo.setAccessToken(accessToken);

            String expires_in = values.getString("expires_in");
            AuthoSharePreference.putExpires(WeiboBaseActivity.mContext, expires_in);

            String remind_in = values.getString("remind_in");
            AuthoSharePreference.putRemind(WeiboBaseActivity.mContext, remind_in);

            uid = values.getString("uid");
            Log.d(TAG, uid);
            AuthoSharePreference.putUid(WeiboBaseActivity.mContext, uid);
            // update(weibo, Weibo.getAppKey(), msg, "", "");
            new AsyncTask<Void, Void, Void>() {

                @Override
                protected Void doInBackground(Void... params) {
                    retrieveSinaUserInfo(weibo, Weibo.getAppKey(), weibo.getAccessToken(), uid);
                    return null;
                }

            }.execute();
        }

        private void retrieveSinaUserInfo(Weibo weibo, String source, Token token, String uid) {
            WeiboParameters bundle = new WeiboParameters();
            Log.d(TAG, "token.toString():" + token.getRefreshToken());
            Log.d(TAG, "token.toString():" + token.getToken());
            // try {
            // Log.d(TAG,"token.toString():"+weibo.generateAccessToken(mContext,weibo.getRequestToken(mContext,
            // Weibo.getAppKey(), Weibo.getAppSecret(),
            // weibo.getRedirectUrl())));
            // } catch (WeiboException e) {
            // // TODO Auto-generated catch block
            // e.printStackTrace();
            // }

            // bundle.add("source", source);
            bundle.add("access_token", token.getToken());
            bundle.add("uid", uid);

            String url = Weibo.SERVER + "users/show.json";
            AsyncWeiboRunner weiboRunner = new AsyncWeiboRunner(weibo);
            weiboRunner.request(mContext, url, bundle, Utility.HTTPMETHOD_GET, this);
        }

        @Override
        public void onCancel() {
            // TODO Auto-generated method stub

        }

        @Override
        public void onError(DialogError arg0) {
            arg0.printStackTrace();
            Log.d(TAG, "aaerror" + arg0.getStackTrace());
        }

        @Override
        public void onWeiboException(WeiboException arg0) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onComplete(String response) {
            Log.d(TAG, "response:" + response.toString());
            try {
                JSONObject js = new JSONObject(response);
                user_name = js.getString("screen_name");
                Log.d(TAG, "user_name" + user_name);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            AccessInfo accessInfo = new AccessInfo();
            accessInfo.setUserID(uid);
            accessInfo.setAccessToken(tokenString);
            accessInfo.setAccessSecret(WeiboConstParam.CONSUMER_SECRET);
            accessInfo.setNickName(user_name);

            AccessInfoHelper accessDBHelper = new AccessInfoHelper(mContext);
            accessDBHelper.open();
            accessDBHelper.create(accessInfo);
            accessDBHelper.close();

            goShareActivity(accessInfo);

        }

        @Override
        public void onIOException(IOException e) {
            // TODO Auto-generated method stub
            Log.d(TAG, "IOE:" + e.toString());
        }

        @Override
        public void onError(WeiboException e) {
            e.printStackTrace();

            Log.d(TAG, "errddddor:" + e);
        }
    }

    private void initweibo() {
        renren_dataHelper = RenRenDataBaseContext.getInstance(mContext);
        url = "https://graph.renren.com/oauth/authorize?"
                + "client_id="
                + AccessData.RENREN_API_KEY
                + "&redirect_uri="
                + AccessData.CALLBACK_URL
                + "&response_type=token"
                + "&scope=status_update publish_blog read_user_feed photo_upload publish_feed read_user_album publish_checkin";

        tencent_dataHelper = DataBaseContext.getInstance(mContext);

        //registerReceiver(broadcastReceiver, new IntentFilter("com.weibo.techface.getRenren_access_token"));
        // tencent init
//		registerReceiver(broadcastReceiver, new IntentFilter("com.weibo.techface.getTencent_verifier"));// register
        // the
        // receiver
        // to
        // get
        // the
        // verifier.

        // TODO move the initial action to new class that can share with
        // Settings
        // initHuaban();

    }

    public static Context getInstance() {
        return mContext;
    }


    // @Override
    // protected void onActivityResult(int requestCode, int resultCode, Intent
    // intent){
    // switch(requestCode){
    // case HUABAN_SHARE:
    // if (resultCode==RESULT_OK) {
    // mContext.finish();
    // }
    // }
    // }

    private String url;

    private TencentDataHelper tencent_dataHelper;

    protected void shareToTenxunWeibo() {
        isForShare = true;
        if (ThemeUtils.getLocalIpAddress() == null) {
            Toast tip = Toast.makeText(mContext, R.string.error_cannot_access_net, Toast.LENGTH_SHORT);
            tip.show();
            return;
        }
        if (tencent_dataHelper == null) {
            tencent_dataHelper = DataBaseContext.getInstance(mContext);
        }
        List<UserInfo> TuserInfoList = tencent_dataHelper.GetUserList(true);
        Log.d(TAG, "user list :" + TuserInfoList.size());
        for (UserInfo userInfor : TuserInfoList) {
            Log.d(TAG, "user " + userInfor.getUserId());
        }
        // Log.d(TAG, "user already exist" );
        if (!TuserInfoList.isEmpty()) {
            Log.d(TAG, "user already exist");
            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putString("thisLarge", thisLarge);
            bundle.putString("thisMessage", thisMessage);
            //bundle.putString("thisMessage", thisMessage + "@" + MeimeiWeibo.TX_MEIMEI);
            Log.d(TAG, "thisLarge" + thisLarge);
            Log.d(TAG, "accessToken for tencent" + TuserInfoList.get(0).getToken());
            Log.d(TAG, "accessSecret for tencent" + TuserInfoList.get(0).getTokenSecret());
            bundle.putString("accessToken", TuserInfoList.get(0).getToken());
            bundle.putString("accessSecret", TuserInfoList.get(0).getTokenSecret());

            intent.putExtras(bundle);
            intent.setClass(mContext, ShareToTencentWeibo.class);
            // startActivity(intent);
            Log.d(TAG, "intent started success" + intent);
            startActivityForResult(intent, 2);
            // finish();
        } else {
            Log.d(TAG, "go to tencent authorize");

            Intent intent = new Intent();
            intent.setClass(mContext, TencentAuthorizeActivity.class);
            startActivityForResult(intent, TENCENT);

        }

    }

    protected void shareToWeixin() {
        api = WXAPIFactory.createWXAPI(mContext, WXEntryActivity.APP_ID, true);
        api.registerApp(WXEntryActivity.APP_ID);

        if (!api.isWXAppInstalled()) {
            Toast.makeText(mContext, "亲，你还没有安装微信呢", Toast.LENGTH_SHORT).show();
            return;
        } else if (api.getWXAppSupportAPI() < 0x21020001) {
            Toast.makeText(mContext, "亲，你的微信版本不支持发送朋友圈", Toast.LENGTH_SHORT).show();
            return;
        }

        Bitmap bmp = BitmapFactory.decodeFile(thisLarge);
        WXImageObject imgObj = new WXImageObject(bmp);

        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = imgObj;

        Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, THUMB_SIZE, THUMB_SIZE, true);
        bmp.recycle();
        msg.thumbData = Util.bmpToByteArray(thumbBmp, true); // 设置缩略图

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("img");
        req.message = msg;
        req.scene = SendMessageToWX.Req.WXSceneTimeline;
        if (!SportsUtilities.checkConnection(mContext)) {
            Toast.makeText(mContext, getString(R.string.sports_comment_neterror), Toast.LENGTH_LONG)
                    .show();
        } else {
            api.sendReq(req);
            MobclickAgent.onEvent(mContext, "shareto", "weixin");
        }
    }

    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }

    int max_Time = 10;
    Thread retrive_authUrl;
    private final Handler mHandler = new MainHandler();
    // private static final int PROGRESS_BAR_DISPLAY = 1;
    private static final int SHOW_PROGRESS = 2;
    private static final int SHOW_PROGRESS_FINISH = 3;
    // private static final int DELAYED_TO_EXIT = 4;
    private static final int FAILED = 5;
    private static final int LOGIN_FAIL_DEVICE_DISABLE = 11;
    // private boolean mInProcessing = false;
    private String tenxun_authUrl = null;
    private String tenxun_tokenKey = null;

    private ProgressDialog mProgressDialog;

    private void closeProgressDialog() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }

    private class MainHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {

                case SHOW_PROGRESS: {
                    CharSequence c = getText(R.string.progress_searching_server);
                    mProgressDialog = ProgressDialog.show(WeiboBaseActivity.mContext, "", c, true, false);

                    // mHandler.sendEmptyMessageDelayed(SHOW_PROGRESS_FINISH, 200);
                    break;
                }
                case SHOW_PROGRESS_FINISH:
                    closeProgressDialog();
                    // mHandler.sendEmptyMessage(DELAYED_TO_EXIT);
                    break;
                case FAILED:
                    Toast.makeText(WeiboBaseActivity.mContext, R.string.progress_server_connecting_error, Toast.LENGTH_SHORT)
                            .show();

                    break;
                case LOGIN_FAIL_DEVICE_DISABLE:
                    Log.e("develop_debug", "WeiboBaseActivity.java 492");
                    Toast.makeText(WeiboBaseActivity.mContext,
                                   R.string.login_fail_device_disable,
                                   Toast.LENGTH_LONG).show();
                    break;
                case QQZONE_SHARE:
                    isFromShareImg = false;
                    shareToQQzoneNEWSDK();
                    break;
                default:
                    Log.v(TAG, "Unhandled message: " + msg.what);
                    break;
            }
        }
    }

    private void retriveAuthUrl(Runnable runner, Runnable countDown) {
        max_Time = 10;
        Log.d(TAG, "get_Url");
        initRunnerThread(runner);
        // new Thread(runner).start();
        Log.d(TAG, "countDown");
        countDown.run();
        // mHandler.post(get_Url);
        // mHandler.post(countDown);
    }

    private void initRunnerThread(Runnable runner) {
        // TODO Auto-generated method stub
        retrive_authUrl = new Thread(runner);
        retrive_authUrl.start();
    }

    private void loadWebView(String authUrl, Class<?> cls) {
        Log.d("authUrl", "authUrl" + authUrl);
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString("url", authUrl);
        intent.putExtras(bundle);
        intent.setClass(mContext, cls);
        startActivity(intent);
    }

//	BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
//		@Override
//		public void onReceive(Context context, Intent intent) {
//			Log.d(TAG, "broadcastReceived");
//			if (!isForShare) {
//				return;
//			}
//			if (intent.getAction().equals("com.weibo.techface.getTencent_verifier")) {
//				tencent_weibo = WeiboContext.getInstance();
//				Bundle bundle = intent.getExtras();
//				final String veryfier = bundle.getString("veryfier");// get the
//				// verifier from
//				// the authorize
//				// webView
//				if (veryfier != null) {
//					// unregisterReceiver(broadcastReceiver);
//					new AsyncTask<Void, Void, Void>() {
//						String userInfo = null;
//
//						@Override
//						protected Void doInBackground(Void... params) {
//							Log.d(TAG, "TokenKey:"+tencent_weibo.getTokenKey());
//							Log.d(TAG, "TokenSecrect:"+tencent_weibo.getTokenSecrect());
//							tencent_weibo.getAccessToken(tencent_weibo.getTokenKey(), tencent_weibo.getTokenSecrect(),
//									veryfier);
//							Log.d(TAG, "tAccessTokenKey:"+tencent_weibo.getAccessTokenKey());
//							Log.d(TAG, "AccessTokenSecrect:"+tencent_weibo.getAccessTokenSecrect());
//							userInfo = tencent_weibo.getUserInfo(tencent_weibo.getAccessTokenKey(),
//									tencent_weibo.getAccessTokenSecrect());
//							return null;
//						}
//
//						@Override
//						protected void onPostExecute(Void void1) {
//							try {
//								JSONObject data = new JSONObject(userInfo).getJSONObject("data");
//								String headUrl = null;
//								if (data.getString("head") != null && !"".equals(data.getString("head"))) {
//									headUrl = data.getString("head") + "/100";
//								}
//								String userId = data.getString("openid");
//								String userName = data.getString("name");
//								String userNick = data.getString("nick");
//                                
//								UserInfo user = new UserInfo();
//								user.setUserId(userId);
//								user.setUserName(userName);
//								user.setUserNick(userNick);
//								user.setToken(tencent_weibo.getAccessTokenKey());
//								user.setTokenSecret(tencent_weibo.getAccessTokenSecrect());
//
//								Long insertId = 0L;
//
//								if (tencent_dataHelper.HaveUserInfo(userId)) {// db
//																				// already
//																				// has
//																				// the
//																				// user
//																				// info
//									tencent_dataHelper.UpdateUserInfo(user);
//
//								} else {
//									insertId = tencent_dataHelper.SaveUserInfo(user);
//
//								}
//
//								Log.d(TAG + "the insert ID is:", insertId.toString());
//
//								// userList =
//								// tencent_dataHelper.GetUserList(false);
//
//								Intent intent3 = new Intent();
//								Bundle bundle3 = new Bundle();
//								bundle3.putString("thisLarge", thisLarge);
//								bundle3.putString("thisMessage", thisMessage + "@" + MeimeiWeibo.TX_MEIMEI);
//								bundle3.putString("accessToken", user.getToken());
//								Log.d(TAG, "accessToken" + user.getToken());
//								Log.d(TAG, "accessSecret" + user.getTokenSecret());
//								bundle3.putString("accessSecret", user.getTokenSecret());
//								intent3.putExtras(bundle3);
//								intent3.setClass(mContext, ShareToTencentWeibo.class);
//								startActivity(intent3);
//								TencentAuthorizeActivity.authInstance.finish();
//								finish();
//								// startActivityForResult(intent, Util.EXIT_OK);
//								// finish();
//
//							} catch (JSONException e) {
//								e.printStackTrace();
//								Toast.makeText(WeiboBaseActivity.mContext, R.string.progress_server_connecting_error,
//										Toast.LENGTH_SHORT).show();
//							} catch (Exception e) {
//								e.printStackTrace();
//							}
//							Log.e(TAG, userInfo);
//						}
//					}.execute();
//				}
//				Log.e(TAG, veryfier);
//			}

			/*if (intent.getAction().equals("com.weibo.techface.getRenren_access_token")) {
                new AsyncTask<Void, Void, Void>() {

					@Override
					protected Void doInBackground(Void... params) {
						saveUserInfo();
						return null;
					}

					@Override
					protected void onPostExecute(Void void1) {
						Intent intent4 = new Intent();
						Bundle bundle4 = new Bundle();
						bundle4.putString("thisLarge", thisLarge);
						bundle4.putString("thisMessage", thisMessage);

						intent4.putExtras(bundle4);
						intent4.setClass(mContext, ShareToRenren.class);
						startActivity(intent4);
					}
				}.execute();

			}*/
//		}
//
//	};

	/*private void saveUserInfo() {
        String url = "http://api.renren.com/restserver.do";
		String signature = getParams();
		String method = "users.getInfo";
		String v2 = "1.0";
		// File upload = new File(thisLarge);
		String access_token = AccessData.access_token;
		String format = "JSON";
		TreeMap<String, String> params = new TreeMap<String, String>();
		params.put("sig", signature);
		params.put("method", method);
		params.put("v", v2);
		params.put("access_token", access_token);
		params.put("format", format);
		String content = HttpURLUtils.doPost(url, params);
		Log.d(TAG, "content = " + content);
		try {
			// JSONObject jb = new JSONObject(content);
			JSONArray ja = new JSONArray(content);
			JSONObject jo = ja.getJSONObject(0);
			String uid = jo.getString("uid");
			String userName = jo.getString("name");
			String tinyurl = jo.getString("tinyurl");
			String headurl = jo.getString("headurl");
			Log.d("ren ren", "uid:" + uid);
			Log.d("ren ren", "name:" + userName);
			Log.d("ren ren", "tinyurl:" + tinyurl);
			UserData userdata = new UserData();
			// userdata.setId(uid);
			userdata.setUserId(uid);
			userdata.setUserName(userName);
			userdata.setTinyurl(tinyurl);
			userdata.setHeadurl(headurl);

			if (renren_dataHelper.HaveUserInfo(uid)) {
				renren_dataHelper.UpdateUserInfo(userdata);
			} else {
				renren_dataHelper.SaveUserInfo(userdata);
			}
		} catch (JSONException e) {
			e.printStackTrace();

		}
	}*/

    public String getParams() {
        List<String> params = new ArrayList<String>();
        String method = "users.getInfo";
        String v1 = "1.0";
        String access_token = AccessData.access_token;
        String format = "JSON";
        params.add("method=" + method);
        params.add("v=" + v1);
        params.add("access_token=" + access_token);
        params.add("format=" + format);
        return getSignature(params, AccessData.RENREN_SECRET);
    }

    public String getSignature(List<String> paramList, String secret) {
        Collections.sort(paramList);

        StringBuffer buffer = new StringBuffer();
        for (String param : paramList) {
            buffer.append(param);
        }

        buffer.append(secret);
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            StringBuffer result = new StringBuffer();
            try {
                for (byte b : md.digest(buffer.toString().getBytes("UTF-8"))) {
                    result.append(Integer.toHexString((b & 0xf0) >>> 4));
                    result.append(Integer.toHexString(b & 0x0f));
                }
            } catch (UnsupportedEncodingException e) {
                for (byte b : md.digest(buffer.toString().getBytes())) {
                    result.append(Integer.toHexString((b & 0xf0) >>> 4));
                    result.append(Integer.toHexString(b & 0x0f));
                }
            }

            return result.toString();
        } catch (java.security.NoSuchAlgorithmException ex) {

        }

        return null;
    }

    public void shareToQQzone() {
        if (ThemeUtils.getLocalIpAddress() == null) {
            Toast tip = Toast.makeText(mContext, R.string.error_cannot_access_net, Toast.LENGTH_SHORT);
            tip.show();
            return;
        }
        String nick = PreferenceManager.getDefaultSharedPreferences(mContext).getString(QQ_NICK, null);
        if (nick != null) {
            startShareToQQzone();
        } else {
            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putString("thisLarge", thisLarge);
            bundle.putString("thisUrl", thisUrl);
            bundle.putString("thisMessage", thisMessage);
            intent.putExtras(bundle);
            intent.setClass(mContext, QQzoneActivity.class);
            startActivity(intent);
        }
    }

    public void startShareToQQzone() {
        mQQToken = PreferenceManager.getDefaultSharedPreferences(mContext).getString(QQ_TOKEN, null);
        mQQOpid = PreferenceManager.getDefaultSharedPreferences(mContext).getString(QQ_OPID, null);
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString("thisLarge", thisLarge);
        bundle.putString("thisMessage", thisMessage);
        bundle.putString("thisUrl", thisUrl);
        bundle.putString("zoneToken", mQQToken);
        bundle.putString("opid", mQQOpid);
        intent.putExtras(bundle);
        intent.setClass(mContext, ShareToQQzone.class);
        startActivityForResult(intent, 2);
    }

    public void shareToQQzoneNEWSDK() {
        isFromShareImg = true;
        if (AllWeiboInfo.TENCENT_QQZONE_OPEN_ID == null || "".equals(AllWeiboInfo.TENCENT_QQZONE_OPEN_ID)) {
            Log.d(TAG, "AllWeiboInfo.TENCENT_QQZONE_OPEN_ID == null");
            login();
        } else {
            Log.d(TAG, "AllWeiboInfo.TENCENT_QQZONE_OPEN_ID != null");
            Intent intent = new Intent(WeiboBaseActivity.mContext, ShareToQQzone.class);
            Bundle bundle = new Bundle();
            bundle.putString("thisUrl", thisUrl);
            bundle.putString("thisLarge", thisLarge);
            bundle.putString("thisMessage", thisMessage);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }

    private void login() {
        Intent intent = new Intent(mContext, QQOAuthActivity.class);
        startActivityForResult(intent, QQ_OAUTH_FINISH);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case TENCENT:
                if (resultCode == getActivity().RESULT_OK)
                    onResultForAuthActivity(data);
                break;

            case QQ_OAUTH_FINISH:
                loginPregressDialog = new Dialog(mContext, R.style.sports_dialog);
                LayoutInflater mInflater = getActivity().getLayoutInflater();
                View v1 = mInflater.inflate(R.layout.sports_progressdialog, null);
                TextView message = (TextView) v1.findViewById(R.id.message);
                message.setText(R.string.sports_logining);
                v1.setMinimumWidth((int) (SportsApp.ScreenWidth * 0.8));
                loginPregressDialog.setContentView(v1);
                loginPregressDialog.setCancelable(true);

                loginPregressDialog.show();

                if (!isFromShareImg) {
                    LoginByQQThread thread = new LoginByQQThread();
                    thread.start();
                } else {

                    if (loginPregressDialog != null)
                        if (loginPregressDialog.isShowing())
                            loginPregressDialog.dismiss();
                    shareToQQThread thread = new shareToQQThread();
                    thread.start();
                    // saveQQOpenId();
                    // saveQQNick();
                    // shareToQQzoneNEWSDK();
                }
                break;
        }
    }

    private void onResultForAuthActivity(Intent intent) {
        Bundle bundle = intent.getExtras();
        access_token = bundle.getString("access_token");
        expires_in = bundle.getString("expires_in");
        openID = bundle.getString("openID");
        openKey = bundle.getString("openKey");

        new AsyncTask<Void, Void, Void>() {
            String userInfo = null;

            @Override
            protected Void doInBackground(Void... params) {
                mQqWeiboProxy = QQWeiboProxy.getInstance();
                userInfo = mQqWeiboProxy.getUserInfo();
                return null;
            }

            @Override
            protected void onPostExecute(Void void1) {
                try {
                    JSONObject data = new JSONObject(userInfo).getJSONObject("data");
                    String headUrl = null;
                    if (data.getString("head") != null && !"".equals(data.getString("head"))) {
                        headUrl = data.getString("head") + "/100";
                    }
                    String userId = data.getString("openid");
                    String userName = data.getString("name");
                    String userNick = data.getString("nick");

                    UserInfo user = new UserInfo();
                    user.setUserId(userId);
                    user.setUserName(userName);
                    user.setUserNick(userNick);
                    user.setToken(access_token);
                    user.setExpiresIn(expires_in);
                    user.setOpenID(openID);
                    user.setOpenKey(openKey);

                    Long insertId = 0L;

                    if (tencent_dataHelper.HaveUserInfo(userId)) {// db
                        // already
                        // has
                        // the
                        // user
                        // info
                        tencent_dataHelper.UpdateUserInfo(user);

                    } else {
                        insertId = tencent_dataHelper.SaveUserInfo(user);

                    }


                    // userList =
                    // tencent_dataHelper.GetUserList(false);

                    Intent intent3 = new Intent();
                    Bundle bundle3 = new Bundle();
                    bundle3.putString("thisLarge", thisLarge);
                    bundle3.putString("thisMessage", thisMessage + "@" + MeimeiWeibo.TX_MEIMEI);
                    bundle3.putString("accessToken", user.getToken());
                    Log.d(TAG, "accessToken" + user.getToken());
                    Log.d(TAG, "accessSecret" + user.getTokenSecret());
                    bundle3.putString("accessSecret", user.getTokenSecret());
                    intent3.putExtras(bundle3);
                    intent3.setClass(mContext, ShareToTencentWeibo.class);
                    startActivity(intent3);
                    TencentAuthorizeActivity.authInstance.finish();
                    getActivity().finish();
                    // startActivityForResult(intent, Util.EXIT_OK);
                    // finish();

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(WeiboBaseActivity.mContext, R.string.progress_server_connecting_error,
                            Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.e(TAG, userInfo);
            }
        }.execute();
    }

    class shareToQQThread extends Thread {

        @Override
        public void run() {
            saveQQOpenId();
            saveQQNick();
        }

    }

    class LoginByQQThread extends Thread {

        @Override
        public void run() {
            try {

                saveQQOpenId();
                saveQQNick();

                Log.d(TAG, "LoginByQQThread.run");
                SharedPreferences sp = mContext.getSharedPreferences("UmengDeviceToken", Activity.MODE_PRIVATE);
                ApiBack back = ApiJsonParser.combineWeibo_New(WeiboType.QQzone, AllWeiboInfo.TENCENT_QQZONE_NICK,
                        AllWeiboInfo.TENCENT_QQZONE_OPEN_ID, 1, sp.getString("device_token", ""));
                if (-11 == back.getFlag()) {
                    Message msg = Message.obtain(mHandler, 11);
                    Bundle bundle = new Bundle();
                    bundle.putString("msg", getResources().getString(R.string.login_fail_device_disable));
                    msg.setData(bundle);
                    msg.sendToTarget();
                } else {
                    Message msg = Message.obtain(mHandler, 2);
                    Bundle bundle = new Bundle();
                    bundle.putString("msg", back.getMsg());
                    msg.setData(bundle);
                    msg.sendToTarget();
                }

            } catch (ApiNetException e) {
                e.printStackTrace();
            }
        }

    }

    private void saveQQOpenId() {

        SharedPreferences sp = mContext.getSharedPreferences(AllWeiboInfo.TENCENT_QQZONE_TOKEN_SP, mContext.MODE_PRIVATE);
        String token = sp.getString(AllWeiboInfo.TENCENT_QQZONE_TOKEN_KEY, null);
        try {
            _FakeX509TrustManager.allowAllSSL();
            URL url = new URL("https://graph.z.qq.com/moc2/me?access_token=" + token);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.connect();

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = "";
            StringBuffer res = new StringBuffer();
            while ((line = reader.readLine()) != null) {
                res.append(line);
            }

            String s = res.toString();
            int start = s.indexOf("openid") + "openid".length() + 1;
            s = s.substring(start);

            Editor e = sp.edit();
            e.putString(AllWeiboInfo.TENCENT_QQZONE_OPEN_ID_KEY, s);
            e.commit();

            AllWeiboInfo.TENCENT_QQZONE_OPEN_ID = s;

            if (isFromShareImg) {
                Message msg = Message.obtain(mHandler, QQZONE_SHARE);
                msg.sendToTarget();
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void saveQQNick() {
        SharedPreferences sp = mContext.getSharedPreferences(AllWeiboInfo.TENCENT_QQZONE_TOKEN_SP, mContext.MODE_PRIVATE);
        String token = sp.getString(AllWeiboInfo.TENCENT_QQZONE_TOKEN_KEY, null);

        try {
            _FakeX509TrustManager.allowAllSSL();
            URL url = new URL("https://openmobile.qq.com/user/get_simple_userinfo?" + "access_token=" + token
                    + "&oauth_consumer_key=" + AllWeiboInfo.TENCENT_APPID + "&openid="
                    + AllWeiboInfo.TENCENT_QQZONE_OPEN_ID);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.connect();

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = "";
            StringBuffer res = new StringBuffer();
            while ((line = reader.readLine()) != null) {
                res.append(line);
            }

            JSONObject object = new JSONObject(res.toString());
            String s = object.getString("nickname");

            Editor e = sp.edit();
            e.putString(AllWeiboInfo.TENCENT_QQZONE_NICK_KEY, s);
            e.commit();

            AllWeiboInfo.TENCENT_QQZONE_NICK = s;

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // private void login() {
    //
    // if (AllWeiboInfo.mTencent.getOpenId() == null) {
    // IUiListener listener = new BaseUiListener() {
    // @Override
    // protected void doComplete(JSONObject values) {
    // Intent intent = new Intent(WeiboBaseActivity.mContext,
    // ShareToQQzone.class);
    // Bundle bundle = new Bundle();
    // bundle.putString("thisUrl", thisUrl);
    // bundle.putString("thisLarge", thisLarge);
    // bundle.putString("thisMessage", thisMessage);
    // intent.putExtras(bundle);
    // startActivity(intent);
    // }
    // };
    // AllWeiboInfo.mTencent.login(mContext, AllWeiboInfo.TENCENT_SCOPE,
    // listener);
    // } else {
    // Intent intent = new Intent(WeiboBaseActivity.mContext,
    // ShareToQQzone.class);
    // Bundle bundle = new Bundle();
    // bundle.putString("thisUrl", thisUrl);
    // bundle.putString("thisLarge", thisLarge);
    // bundle.putString("thisMessage", thisMessage);
    // intent.putExtras(bundle);
    // startActivity(intent);
    // }
    //
    // }
    //
    // private class BaseUiListener implements IUiListener {
    //
    // @Override
    // public void onComplete(JSONObject response) {
    // doComplete(response);
    // }
    //
    // protected void doComplete(JSONObject values) {
    //
    // }
    //
    // @Override
    // public void onError(UiError e) {
    // }
    //
    // @Override
    // public void onCancel() {
    // }
    // }
}