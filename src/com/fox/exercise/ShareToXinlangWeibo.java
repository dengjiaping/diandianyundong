package com.fox.exercise;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.fox.exercise.api.AddCoinsThread;
import com.fox.exercise.api.ApiConstant;
import com.fox.exercise.api.ApiJsonParser;
import com.fox.exercise.http.FunctionStatic;
import com.fox.exercise.http.StatCounts;
import com.fox.exercise.login.Tools;
import com.fox.exercise.util.SportTaskUtil;
import com.fox.exercise.weibo.sina.AccessInfo;
import com.fox.exercise.weibo.sina.AccessInfoHelper;
import com.fox.exercise.weibo.sina.InfoHelper;
import com.fox.exercise.weibo.sina.oauth2.AccessToken;
import com.fox.exercise.weibo.sina.oauth2.AsyncWeiboRunner;
import com.fox.exercise.weibo.sina.oauth2.AsyncWeiboRunner.RequestListener;
import com.fox.exercise.weibo.sina.oauth2.Oauth2AccessTokenHeader;
import com.fox.exercise.weibo.sina.oauth2.Utility;
import com.fox.exercise.weibo.sina.oauth2.Weibo;
import com.fox.exercise.weibo.sina.oauth2.WeiboException;
import com.fox.exercise.weibo.sina.oauth2.WeiboParameters;
import com.umeng.analytics.MobclickAgent;

import java.io.File;
import java.io.IOException;

import cn.ingenic.indroidsync.SportsApp;

public class ShareToXinlangWeibo extends AbstractBaseActivity implements
        Button.OnClickListener, RequestListener {
    private static final String TAG = "beauty ShareToXinlangWeibo";

    private static final int TEXT_MAX = 140;

    private ImageView mDisplayImage;
    private Bitmap mBitmapSrc = null;
    private EditText mInputText;

    private CharSequence mTempText;
    private int selectionStart;
    private int selectionEnd;
    private ProgressDialog dialog = null;
    private String accessToken;
    private String accessSecret;
    private String thisLarge = null;
    private DisplayMetrics metrics;

    private Display display;
    private static final String SHARE_PATH = SportsUtilities.DOWNLOAD_SAVE_PATH + "shareImage.jpg";
    private AccessInfo mAccessInfo = new AccessInfo();
    private SportsApp mSportsApp;
    protected Context mContext;
    protected Activity instance;
    private int tShare = 1;
    private int shareXinLangId = -1;//分享记录ID
    private ImageDownloader mDownloader = null;
    private String urls = null;

    private void initRes() {
//		metrics = new DisplayMetrics();
//		display = getWindowManager().getDefaultDisplay();
//		display.getMetrics(metrics);
//
//		float mDensity = metrics.density;
//		Log.d(TAG, "mDensity:" + mDensity);
        mDisplayImage = (ImageView) findViewById(R.id.share_image);
        mInputText = (EditText) findViewById(R.id.shareto_edittext);
    }

    TextWatcher mTextWatcher = new TextWatcher() {
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
            mTempText = s;
        }

        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
            // check if playlist with current name exists already, and warn the
            // user if so.
            // setSaveButton();
        }

        ;

        public void afterTextChanged(Editable s) {
            selectionStart = mInputText.getSelectionStart();
            selectionEnd = mInputText.getSelectionEnd();
            Log.i("gongbiao1", "" + selectionStart);
            if (mTempText.length() > TEXT_MAX) {
                Toast.makeText(ShareToXinlangWeibo.this,
                        R.string.edit_content_limit, Toast.LENGTH_SHORT).show();
                s.delete(selectionStart - 1, selectionEnd);
                int tempSelection = selectionStart;
                mInputText.setText(s);
                mInputText.setSelection(tempSelection);
            }

        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_sendto:
                SendIamgeAndText();
                // finish();
                return;
        }

    }

    private void SendIamgeAndText() {
        if (!InfoHelper.checkNetWork(mContext)) {
            Toast.makeText(mContext, getString(R.string.acess_server_error),
                    Toast.LENGTH_LONG).show();
        } else {
            // if( isChecked() )
            {
                if (dialog == null) {
                    dialog = new ProgressDialog(instance);
                    dialog.setMessage(getString(R.string.sharing));
                    dialog.setIndeterminate(false);
                    dialog.setCancelable(true);
                    dialog.setCanceledOnTouchOutside(false);
                }
                dialog.show();

                Thread thread = new Thread(new UpdateStatusThread());
                thread.start();
            }
        }

    }

    Handler handle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (dialog != null) {
                dialog.dismiss();
            }

            thisLarge = null;
            mInputText.setText("");
            // MyImage.setBackgroundDrawable(null);

            if (msg.what > 0) {
                MobclickAgent.onEvent(mContext, "shareto", "sina");
                StatCounts.ReportSocialShareInfo(mContext, FunctionStatic.getGameId(mContext),
                        mAccessInfo.getUserID(), "", "", "");
                Log.d(TAG, "share success");
                Toast.makeText(mContext,
                        getString(R.string.shared_success),
//					getString(R.string.shared_success_add_coins),
                        Toast.LENGTH_SHORT).show();
                final String time = mSportsApp
                        .getCurrentDate();
                String shareCoins = SportTaskUtil
                        .readShareCoinsFromFile(mSportsApp.getSportUser().getUid());
                boolean isShared = true;
                if (TextUtils.isEmpty(shareCoins)) { // 第一次使用
                } else {
                    String[] sCoinString = shareCoins
                            .split("#");
                    if (sCoinString.length == 2) {
                        final String date = sCoinString[0];
                        if (date.equals(time)) { // 如果是同一天要加上之前的分享次数
                            tShare = Integer
                                    .parseInt(sCoinString[1])
                                    + 1;
                            if (tShare > 5) {
                                isShared = false;
                                Toast.makeText(mContext, getString(R.string.shared_beyond_10times), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
                if (isShared) {
                    new AddCoinsThread(10, 4, new Handler() {

                        @Override
                        public void handleMessage(
                                Message msg) {
                            // TODO Auto-generated method
                            // stub
                            switch (msg.what) {
                                case ApiConstant.COINS_SUCCESS:
                                    SportTaskUtil.jump2CoinsDialog(ShareToXinlangWeibo.this, getString(R.string.shared_success_add_coins));
                                    break;
                                case ApiConstant.COINS_LIMIT:
                                    Toast.makeText(getApplicationContext(), getString(R.string.shared_beyond_10times), Toast.LENGTH_SHORT).show();
                                    break;
                                default:
                                    break;
                            }

                        }

                    }, -1).start();
                }
                returnToHome();
            } else {
                Toast.makeText(mContext, getString(R.string.shared_failed),
                        Toast.LENGTH_SHORT).show();
            }
        }
    };

    private void returnToHome() {
        //setResult(RESULT_OK, new Intent());
        finish();
    }

    Handler endSessionHandle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            setResult(RESULT_OK, new Intent());
            finish();
        }
    };

    class UpdateStatusThread implements Runnable {
        public void run() {
            int what = -1;
            // Weibo weibo = OAuthConstant.getInstance().getWeibo();
            Weibo weibo = Weibo.getInstance();
            // Weibo weibo=new Weibo();
            // weibo.setToken(accessToken, accessSecret);
            try {

                Tools.SaveBitmapAsFile(SHARE_PATH, Tools.getBitmapFromURL(urls), 90);
                String msg = mInputText.getText().toString();
                msg += " " + getString(R.string.message_from);
                // if (msg.getBytes().length != msg.length()) {
                // msg = URLEncoder.encode(msg, "UTF-8");
                // }
                Log.d(TAG, "msg:" + msg);
                // update(weibo, Weibo.getAppKey(), msg, "", "");
                // Status status = null;

                // if (StringUtils.isBlank(thisLarge)) {
                // // status = weibo.updateStatus(msg);
                // update(weibo, Weibo.getAppKey(), msg, "", "");
                // } else {
                File file = new File(thisLarge);
                Log.v(TAG, "thisLarge = " + thisLarge);

                if (mAccessInfo != null) {
                    weibo.setAccessToken(new AccessToken(mAccessInfo
                            .getAccessToken(), mAccessInfo.getAccessSecret()));
                }
                if (urls != null) {
                    upload(weibo, Weibo.getAppKey(), thisLarge, msg, "", "", false);
                } else {
                    upload(weibo, Weibo.getAppKey(), thisLarge, msg, "", "", true);
                }

                // }

                // if(result!=null)
                // {
                // what=1;
                // }
            } catch (Exception e) {
                e.printStackTrace();
                // Log.e("WeiboPub", e.getMessage());
            }
        }
    }

    private void upload(Weibo weibo, String source, String file, String status,
                        String lon, String lat, boolean isFile) throws WeiboException {
        Log.d(TAG, "upload pic+txt");
        WeiboParameters bundle = new WeiboParameters();
        Utility.setAuthorization(new Oauth2AccessTokenHeader());// =加了这一句

        bundle.add("source", source);
        if (isFile) {
            bundle.add("pic", file);
        } else {
            bundle.add("pic", file);
        }

        bundle.add("status", status);
        if (!TextUtils.isEmpty(lon)) {
            bundle.add("lon", lon);
        }
        if (!TextUtils.isEmpty(lat)) {
            bundle.add("lat", lat);
        }
        // String rlt = "";
        String url = Weibo.SERVER + "statuses/upload.json";
        AsyncWeiboRunner weiboRunner = new AsyncWeiboRunner(weibo);
        weiboRunner.request(this, url, bundle, Utility.HTTPMETHOD_POST, this);

        // return rlt;
    }

    // private void update(Weibo weibo, String source, String status, String
    // lon,
    // String lat) throws MalformedURLException, IOException,
    // WeiboException {
    // WeiboParameters bundle = new WeiboParameters();
    // Utility.setAuthorization(new Oauth2AccessTokenHeader());// =加了这一句
    //
    // bundle.add("source", source);
    // bundle.add("status", status);
    // if (!TextUtils.isEmpty(lon)) {
    // bundle.add("lon", lon);
    // }
    // if (!TextUtils.isEmpty(lat)) {
    // bundle.add("lat", lat);
    // }
    // String url = Weibo.SERVER + "statuses/update.json";
    // AsyncWeiboRunner weiboRunner = new AsyncWeiboRunner(weibo);
    // weiboRunner.request(this, url, bundle, Utility.HTTPMETHOD_POST, this);
    //
    // }

    class EndSessionThread implements Runnable {
        public void run() {
            AccessInfoHelper accessDBHelper = new AccessInfoHelper(mContext);
            accessDBHelper.open();
            accessDBHelper.delete();
            accessDBHelper.close();
            // Weibo weibo = Weibo.getInstance();
            // try
            // {
            // weibo.endSession();
            // } catch (WeiboException e) {
            // e.printStackTrace();
            // }
            endSessionHandle.sendEmptyMessage(201);
        }
    }

    @Override
    public void onComplete(final String response) {
        Log.d(TAG, "response" + response.toString());
        runOnUiThread(new Runnable() {

            @Override
            public void run() {

                handle.sendEmptyMessage(1);
                Intent intent = new Intent();
                intent.putExtra("response", response);
                intent.setAction("com.weibo.techface.getSina_Response");
                sendBroadcast(intent);
            }
        });

    }

    @Override
    public void onError(WeiboException arg0) {
        Log.d(TAG, "response" + arg0.toString());
        runOnUiThread(new Runnable() {

            @Override
            public void run() {

                handle.sendEmptyMessage(-1);
            }
        });

    }

    @Override
    public void onIOException(IOException arg0) {
        Log.d(TAG, "response" + arg0.toString());
        runOnUiThread(new Runnable() {

            @Override
            public void run() {

                handle.sendEmptyMessage(-1);
            }
        });

    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getRepeatCount() > 0
                && event.getKeyCode() == KeyEvent.KEYCODE_MENU) {
            return true;
        }
        return super.dispatchKeyEvent(event);

    }

    @Override
    public void initIntentParam(Intent intent) {
        title = getResources().getString(R.string.text_xinlang_weibo);
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            Log.d(TAG, "get bundle");
            thisLarge = bundle.containsKey("thisLarge") ? bundle
                    .getString("thisLarge") : "";
            mAccessInfo = (AccessInfo) bundle.getSerializable("access_info");

            shareXinLangId = bundle.containsKey("shareXinLangId") ? bundle
                    .getInt("shareXinLangId") : -1;
        }
    }

    @Override
    public void initView() {
        showContentView(R.layout.sports_shareto_weibo);
        mContext = this;
        mSportsApp = (SportsApp) getApplication();
        initRes();
        instance = this;
        dialog = new ProgressDialog(instance);
        dialog.setMessage(getString(R.string.sharing));
        dialog.setIndeterminate(false);
        dialog.setCancelable(true);
        Log.d(TAG, "uri" + this.getIntent().getData());
        Uri uri = this.getIntent().getData();

        if (shareXinLangId > 0) {
            new AsyncTask<Void, Void, String>() {

                @Override
                protected String doInBackground(Void... params) {
                    // TODO Auto-generated method stub
                    String imagUrl = ApiJsonParser.getShareIcon(mSportsApp.getSportUser().getUid(), shareXinLangId);
                    return imagUrl;
                }

                protected void onPostExecute(String result) {
                    if (result != null) {
                        Log.i("shaerTengXunId", result);
                        mDownloader = new ImageDownloader(ShareToXinlangWeibo.this);
                        mDownloader.setType(ImageDownloader.ICON);
                        mDownloader.download(result, mDisplayImage, null);
                        urls = result;


                    } else {
                        Toast.makeText(ShareToXinlangWeibo.this,
                                getString(R.string.sports_comment_neterror),
                                Toast.LENGTH_LONG).show();
                    }

                }

                ;

            }.execute();
        } else {
            Toast.makeText(ShareToXinlangWeibo.this,
                    getString(R.string.sports_comment_neterror),
                    Toast.LENGTH_LONG).show();
        }
//		mBitmapSrc = Util.decodeFile(SHARE_PATH);
//		mDisplayImage.setImageBitmap(mBitmapSrc);
        thisLarge = SHARE_PATH;
    }

    @Override
    public void setViewStatus() {
        findViewById(R.id.button_sendto).setOnClickListener(this);
        mInputText.addTextChangedListener(mTextWatcher);
    }

    @Override
    public void onPageResume() {
        MobclickAgent.onPageStart("ShareToXinlangWeibo");
    }

    @Override
    public void onPagePause() {
        MobclickAgent.onPageEnd("ShareToXinlangWeibo");
    }

    @Override
    public void onPageDestroy() {
        if (mBitmapSrc != null && !mBitmapSrc.isRecycled()) {
            mBitmapSrc.recycle();
            mBitmapSrc = null;
        }
    }
}
