package com.fox.exercise;

import android.app.Activity;
import android.os.Bundle;

public class QQzoneActivity extends Activity {
    private static String mAccessToken, mOpenId;
    // private static Context mContext;
    // static String nickName;
    public String mAppid = "801268715";
    //	private AuthReceiver receiver;
    final String CALLBACK = "auth://tauth.qq.com/";
    String scope = "get_user_info,add_share,check_page_fans,add_topic,add_one_blog,upload_pic";// ��Ȩ��Χ

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//		registerIntentReceivers();
//		Log.e("qqzone", "oncreate");
//		TencentOpenAPI2.logIn(this, mOpenId, scope, mAppid, "_self", CALLBACK,
//				null, null);
    }

//	@Override
//	protected void onDestroy() {
//		super.onDestroy();
//		if (receiver != null) {
//			unregisterIntentReceivers();
//		}
//	}
//
//	private void registerIntentReceivers() {
//		receiver = new AuthReceiver();
//		IntentFilter filter = new IntentFilter();
//		filter.addAction(TencentOpenHost.AUTH_BROADCAST);
//		registerReceiver(receiver, filter);
//	}
//
//	private void unregisterIntentReceivers() {
//		unregisterReceiver(receiver);
//	}
//
//	@Override
//	protected void onPause() {
//		Log.e("qqzone", "onpause");
//		super.onPause();
//	}
//
//	@Override
//	protected void onRestart() {
//		Log.e("qqzone", "onrestart");
//		super.onRestart();
//		finish();
//	}
//
//	@Override
//	protected void onResume() {
//		Log.e("qqzone", "onresume");
//		super.onResume();
//	}
//
//	public class AuthReceiver extends BroadcastReceiver {
//
//		private static final String TAG = "AuthReceiver";
//
//		@Override
//		public void onReceive(Context context, Intent intent) {
//			Bundle exts = intent.getExtras();
//			String raw = exts.getString("raw");
//			mAccessToken = exts.getString(TencentOpenHost.ACCESS_TOKEN);
//			String expires_in = exts.getString(TencentOpenHost.EXPIRES_IN);
//
//			SharedPreferences sp = TecfaceManagerActivity.mContext
//					.getSharedPreferences(AllWeiboInfo.QQZONE_TOKEN_SP,
//							Context.MODE_PRIVATE);
//			Editor e = sp.edit();
//			e.putLong(AllWeiboInfo.QQZONE_TOKEN_SP_TIME,
//					System.currentTimeMillis());
//			e.commit();
//
//			String error_ret = exts.getString(TencentOpenHost.ERROR_RET);
//			Log.i(TAG, String.format("raw: %s, access_token:%s, expires_in:%s",
//					raw, mAccessToken, expires_in));
//			if (mAccessToken != null) {
//				TencentOpenAPI.openid(mAccessToken, new Callback() {
//
//					public void onCancel(int flag) {
//
//					}
//
//					public void onSuccess(final Object obj) {
//						runOnUiThread(new Runnable() {
//							@Override
//							public void run() {
//								mOpenId = ((OpenId) obj).getOpenId();
//								// TencentOpenAPI.userInfo(mAccessToken, mAppid,
//								// mOpenId, new Callback() {
//								// public void onSuccess(final Object obj) {
//								// String s = obj.toString();
//								// String[] ss = s.split("nickname");
//								// nickName =
//								// ss[1].substring(2,ss[1].indexOf("\n"));
//								Bundle bundle = getIntent().getExtras();
//								String data1 = null;
//								String data2 = "";
//								String data3 = "";
//								if (bundle != null) {
//									data1 = bundle.containsKey("thisLarge") ? bundle
//											.getString("thisLarge") : null;
//									data2 = bundle.containsKey("thisMessage") ? bundle
//											.getString("thisMessage") : null;
//									data3 = bundle.containsKey("thisUrl") ? bundle
//											.getString("thisUrl") : null;
//								}
//								final String thisLarge = data1;
//								final String thisMessage = data2;
//								final String thisUrl = data3;
//
//								if (thisLarge != null) {
//									Intent intent = new Intent();
//									Bundle bundles = new Bundle();
//									bundles.putString("thisLarge", thisLarge);
//									bundles.putString("thisMessage",
//											thisMessage);
//									bundles.putString("thisUrl", thisUrl);
//									// bundles.putString("nickname", nickName);
//									bundles.putString("zoneToken", mAccessToken);
//									bundles.putString("opid", mOpenId);
//									intent.putExtras(bundles);
//									intent.setClass(QQzoneActivity.this,
//											ShareToQQzone.class);
//									startActivity(intent);
//								}
//								finish();
//								// }
//
//								// public void onFail(final int ret, final
//								// String msg) {
//								// }
//								// public void onCancel(int flag) {
//								// }
//								// });
//							}
//						});
//					}
//
//					public void onFail(int ret, final String msg) {
//						runOnUiThread(new Runnable() {
//							@Override
//							public void run() {
//								finish();
//							}
//						});
//					}
//				});
//			}
//			if (error_ret != null) {
//			}
//		}
//	}

}
