package com.fox.exercise;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fox.exercise.api.ApiBack;
import com.fox.exercise.api.ApiJsonParser;
import com.fox.exercise.api.ApiNetException;
import com.fox.exercise.api.ApiSessionOutException;
import com.fox.exercise.login.LoginActivity;
import com.umeng.analytics.MobclickAgent;

import cn.ingenic.indroidsync.SportsApp;


public class CapturePreview extends Activity implements View.OnClickListener {

    private static final String TAG = "CapturePreview";
    private ImageView previewImage;
    private ImageView reCaptureImageView;
    private ImageView captureOkImageView;
    private SportsApp mSportsApp;
    private static boolean mSetRegion = false;
    private Bitmap bitmap;
    private LinearLayout bottom_layout;
    private TextView reCaptureTextView;
    private TextView captureOkTextView;
    private static final int BACKTO_SPORTS = 5;
    private String imageUri;
    private String img_uri_server;
    private Context mContext;
    private Dialog mDialog;
    private MainHandler mHandler = new MainHandler();
    private static final int GOTO_IMAGEPROCESS = 1;
    private static final int BACKTO_CAMERA = 0;
    private static final int UPLOAD_FINISH = 5;
    private static final int NOT_LOGIN = 2;
    private static final int UPLOAD_FAILED = 3;
    private static final int UPLOAD_START = 4;
    protected static final int AUTH_RESULT = 15;
    private boolean isAuthentication = false;
    private Dialog uploadProgressDialog;
    private Toast toast = null;
    private boolean isFromCamera;
    private SportsExceptionHandler mExceptionHandler = null;

    String pointString;
    int taskID;
    int mediaTypeID;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // requestWindowFeature(Window.FEATURE_NO_TITLE);
        boolean findMethod = SmartBarUtils.findActionBarTabsShowAtBottom();
        if (!findMethod) {
            // 取消ActionBar拆分，换用TabHost
            getWindow().setUiOptions(0);
            getWindow().requestFeature(android.view.Window.FEATURE_NO_TITLE);
        }
        setContentView(R.layout.capture_preview);
        mContext = this;
//        Bundle extra = getIntent().getExtras();
        mSportsApp = (SportsApp) getApplication();
        mExceptionHandler = mSportsApp.getmExceptionHandler();
        isAuthentication = getIntent().getBooleanExtra("forAuth", false);
        isFromCamera = getIntent().getBooleanExtra("from_camera", false);
        initRes();

        SharedPreferences sp = getSharedPreferences("sport_state_" + mSportsApp.getSportUser().getUid(), MODE_PRIVATE);
        pointString = sp.getString("pointStr", "0,0");
        taskID = sp.getInt("taskID", 0);
        mediaTypeID = sp.getInt("mediaTypeID", 0);
        if (findMethod) {
            getActionBar().setDisplayShowHomeEnabled(false);
            getActionBar().setDisplayShowTitleEnabled(false);
            SmartBarUtils.setActionModeHeaderHidden(getActionBar(), true);
            SmartBarUtils.setActionBarViewCollapsable(getActionBar(), true);
        }
        Log.i("pointString", pointString + "---" + taskID + "---" + mediaTypeID);
    }

    private void initRes() {
        bitmap = mSportsApp.getPreviewBitmap();
        previewImage = (ImageView) findViewById(R.id.preview_image);

        reCaptureImageView = (ImageView) findViewById(R.id.recapture_button);
        reCaptureTextView = (TextView) findViewById(R.id.recapture);
        captureOkImageView = (ImageView) findViewById(R.id.ok_button);
        captureOkTextView = (TextView) findViewById(R.id.ok);
        bottom_layout = (LinearLayout) findViewById(R.id.preview_image_bottom_layout);
        previewImage.setImageBitmap(bitmap);
        reCaptureImageView.setOnClickListener(this);
        reCaptureTextView.setOnClickListener(this);
        captureOkTextView.setOnClickListener(this);
        captureOkImageView.setOnClickListener(this);
        Bundle extra = getIntent().getExtras();
        imageUri = extra.getString("image_url");

//		uploadProgressDialog = new Dialog(mContext, R.style.sports_dialog);
//		LayoutInflater mInflater = getLayoutInflater();
//		View v = mInflater.inflate(R.layout.sports_progressdialog, null);
//		TextView message = (TextView) v.findViewById(R.id.message);
//		message.setText(R.string.sports_authentication_uploading);
//		v.setMinimumWidth((int) (SportsApp.ScreenWidth * 0.8));
//		uploadProgressDialog.setContentView(v);
//		uploadProgressDialog.setCancelable(true);
//		if (!isAuthentication) {
//			if(!PedometerActivity.mIn){
//				popupBeautyDialog();
//			}
//		}

    }
//private void popupBeautyDialog() {
//	mDialog = new Dialog(this, R.style.sports_dialog);
//	LayoutInflater mInflater = getLayoutInflater();
//	View v = mInflater.inflate(R.layout.sports_dialog, null);
//	((TextView) v.findViewById(R.id.message)).setText(getResources().getString(R.string.sports_select_beauty));
//	v.findViewById(R.id.bt_ok).setOnClickListener(new View.OnClickListener() {
//		@Override
//		public void onClick(View arg0) {
//			if (mDialog.isShowing())
//				mDialog.dismiss();
//			mHandler.sendEmptyMessage(GOTO_IMAGEPROCESS);
//		}
//	});
//	v.findViewById(R.id.bt_cancel).setOnClickListener(new View.OnClickListener() {
//		@Override
//		public void onClick(View arg0) {
//			mDialog.dismiss();
//		}
//	});
//	v.setMinimumWidth((int) (SportsApp.ScreenWidth * 0.8));
//	mDialog.setCancelable(true);
//	mDialog.setContentView(v);
//	mDialog.show();
//
//	}


    OnClickListener onselect = new OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            mHandler.sendEmptyMessage(GOTO_IMAGEPROCESS);
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("CapturePreview");
        MobclickAgent.onPause(this);
        Log.d(TAG, "onPause invoked");
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("CapturePreview");
        MobclickAgent.onResume(this);
        Log.d(TAG, "onResume invoked");
    }


    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop invoked");
        CapturePreview.this.finish();
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy invoked");
        super.onDestroy();
        mSetRegion = false;
        mSportsApp = null;
        if (previewImage != null) {
            previewImage.setImageBitmap(null);
            previewImage = null;
        }
        if (reCaptureImageView != null) {
            reCaptureImageView.setImageBitmap(null);
            reCaptureImageView = null;
        }
        if (captureOkImageView != null) {
            captureOkImageView.setImageBitmap(null);
            captureOkImageView = null;
        }
        BmpRecycle();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.recapture_button:
            case R.id.recapture:
                setResult(RESULT_CANCELED, new Intent());
                Log.d(TAG, "reCapture pressed");
//			mHandler.sendEmptyMessage(BACKTO_CAMERA);
                if (isFromCamera) {
                    Intent i = new Intent(CapturePreview.this, CameraApp.class);
                    startActivity(i);
                }
                finish();
                break;
            case R.id.ok_button:
            case R.id.ok:
//			if (isAuthentication) {
//				Message message =Message.obtain(mHandler, UPLOAD_START);
//				message.sendToTarget();
//				SportsUtilities.checkSize(imageUri);
//				UploadThread t = new UploadThread();
//				t.start();
//			}else {
//				Intent uploadIntent=new Intent(this,UploadActivity.class);
//				Log.d(TAG, "imageUri after back from improcess"+imageUri);
//				uploadIntent.putExtra("URI", imageUri);
//				startActivity(uploadIntent);
//				finish();
//			}
                SportsUtilities.checkSize(imageUri);
                UploadThread t = new UploadThread();
                t.start();
                break;
            default:
                break;
        }

    }

    private void BmpRecycle() {
        if (bitmap != null && !bitmap.isRecycled()) {
            bitmap.recycle();
            bitmap = null;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        Log.d(TAG, "onActivityResult invoked," + "requestCode:" + requestCode + "resultCode:" + resultCode);
        switch (requestCode) {
            case Util.EXIT_OK:
                if (resultCode == RESULT_OK)
                    finish();
                break;
            case BACKTO_SPORTS:
                if (resultCode == RESULT_OK) {

//	    			imageUri=intent.getExtras().getString("savedImageUri");
                    Log.d(TAG, "imageUri back from imageProcess:" + imageUri);
                    imageUri = Util.mFilePath;
                    bitmap = Util.decodeFile(Util.mFilePath);
                    mSportsApp.setPreviewBitmap(bitmap);
                    previewImage.setImageBitmap(bitmap);
                }

                break;
            default:
                break;
        }
    }

    class UploadThread extends Thread {

        private int waitCnt = 0;

        @Override
        public void run() {
            ApiBack apiBack = new ApiBack();
            try {

                if ("".equals(mSportsApp.getSessionId()) || mSportsApp.getSessionId() == null) {

                    Message msg = Message.obtain(mHandler, NOT_LOGIN);
                    msg.sendToTarget();
                } else {
                    Message msg = Message.obtain(mHandler, UPLOAD_START);
                    msg.sendToTarget();

//					List<String> lables=new ArrayList<String>();
//					lables.add("1");
//					Log.e(TAG, "picAddr:" + imageUri);
//					apiBack = ApiJsonParser.uploadImg("", imageUri, mSportsApp.getSessionId(), lables,
//							"", 0);
//					apiBack=ApiJsonParser.authentication( mSportsApp.getSessionId(), imageUri);

                    String picTitle = imageUri.substring(imageUri.lastIndexOf("/") + 1);
                    apiBack = ApiJsonParser.uploadSportTaskMedia(mSportsApp.getSessionId(), mSportsApp.getSportUser()
                            .getUid(), taskID, mediaTypeID, String.valueOf(1), imageUri, picTitle, pointString, 0, 0, mSportsApp.mCurMapType);

                    Log.i("uploadSportTaskMedia", "sessionId = " + mSportsApp.getSessionId() + ",uid = "
                            + mSportsApp.getSportUser().getUid() + ",taskID = " + taskID + ",mediaTypeID = "
                            + mediaTypeID + ",mediaFilePath = " + imageUri + ",mediaFileName = " + picTitle
                            + ",pointString = " + pointString + "---" + String.valueOf(1));
//					if (apiBack.getFlag() == 0) {
//						if (apiBack.getMsg() != null) {
//							String img = apiBack.getMsg();
//							img_uri_server = ApiConstant.URL + img.substring(img.indexOf(":") + 1);
//							Log.d(TAG, "img_uri_server:"+img_uri_server);
//							
//							new Thread(){
//								ApiBack priMsgBack=new ApiBack();
//								@Override
//								public void run(){
//								 try {
//									priMsgBack = ApiJsonParser.sendprimsg(mSportsApp.getSessionId(), mSportsApp.managerId, "$$Auth$$"+img_uri_server, "", 0);
//									if (priMsgBack.getFlag()==0) {
//									Message	success_msg = Message.obtain(mHandler, UPLOAD_FINISH);
//									success_msg.sendToTarget();
//									}else {
//									Message	failed_msg = Message.obtain(mHandler, UPLOAD_FAILED);
//										failed_msg.sendToTarget();
//									}
//								} catch (ApiNetException e) {
//									e.printStackTrace();
//								} catch (ApiSessionOutException e) {
//									e.printStackTrace();
//								}
//								}
//							}.start();
//						}
                    msg = Message.obtain(mHandler, UPLOAD_FINISH);
                    msg.arg1 = apiBack.getFlag();
                    msg.sendToTarget();

//					} else {
//						msg = Message.obtain(mHandler, UPLOAD_FAILED);
//						msg.sendToTarget();
//					}


                }
            } catch (ApiNetException e) {
                Message msg2 = Message.obtain(mExceptionHandler, SportsExceptionHandler.NET_ERROR);
                msg2.sendToTarget();
                e.printStackTrace();
            } catch (ApiSessionOutException e) {
                Message msg2 = Message.obtain(mExceptionHandler, SportsExceptionHandler.SESSION_OUT);
                msg2.sendToTarget();
                startActivity(new Intent(CapturePreview.this, LoginActivity.class));
                e.printStackTrace();
            }
        }
    }

    private class MainHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case GOTO_IMAGEPROCESS:
//				mDialog.dismiss();
//            	Intent intent = new Intent(mContext,ImageProcess.class);
//            	intent.putExtra("from_sports", true);
//            	intent.putExtra("imagePath", imageUri);
//            	startActivityForResult(intent, BACKTO_SPORTS);
//            	BmpRecycle();
                    break;
                case BACKTO_CAMERA:
                    finish();
                    Log.d(TAG, "finish invoked");
                    break;
                case UPLOAD_FINISH:
                    if (uploadProgressDialog != null && uploadProgressDialog.isShowing())
                        uploadProgressDialog.dismiss();
//				CapturePreview.this.finish();
                    int flag = msg.arg1;
                    SharedPreferences sp = getSharedPreferences("upload_state_" + mSportsApp.getSportUser().getUid(), MODE_PRIVATE);
                    Editor editor = sp.edit();
                    Boolean isfinish = false;
                    if (flag == 1) {
                        isfinish = true;
                    } else {
                        isfinish = false;
                    }
                    editor.putBoolean("isfinish", isfinish);
                    editor.putInt("isback", 1);
                    editor.commit();
                    Log.i("UPLOAD_FINISH", "isfinish = " + isfinish + ", isBack = " + 1);
                    finish();
                    break;
                case NOT_LOGIN:
                    Intent loginIntent = new Intent(CapturePreview.this, LoginActivity.class);
                    startActivity(loginIntent);
                    break;
                case UPLOAD_FAILED:
                    if (uploadProgressDialog != null && uploadProgressDialog.isShowing())
                        uploadProgressDialog.dismiss();
                    if (toast == null) {
                        toast = Toast.makeText(CapturePreview.this, getString(R.string.upload_failed),
                                Toast.LENGTH_SHORT);
                    } else {
                        toast.cancel();
                        toast.setText(R.string.upload_failed);
                    }
                    toast.show();
                    break;
                case UPLOAD_START:

                    if (uploadProgressDialog == null) {
                        uploadProgressDialog = new Dialog(CapturePreview.this, R.style.sports_dialog);
                        LayoutInflater mInflater = getLayoutInflater();
                        View v = mInflater.inflate(R.layout.sports_progressdialog, null);
                        TextView message = (TextView) v.findViewById(R.id.message);
                        message.setText(R.string.sports_authentication_uploading);
                        v.setMinimumWidth((int) (SportsApp.ScreenWidth * 0.8));
                        uploadProgressDialog.setContentView(v);
                        uploadProgressDialog.setCancelable(true);
                        uploadProgressDialog.setCanceledOnTouchOutside(false);
                    }
                    if (uploadProgressDialog != null && !uploadProgressDialog.isShowing())
                        uploadProgressDialog.show();
                    break;
                default:
                    Log.v(TAG, "Unhandled message: " + msg.what);
                    break;
            }
        }
    }

    /**
     *@method 固定字体大小方法
     *@author suhu
     *@time 2016/10/11 13:23
     *@param
     *
     */
    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        Configuration config=new Configuration();
        config.setToDefaults();
        res.updateConfiguration(config,res.getDisplayMetrics() );
        return res;
    }
}
