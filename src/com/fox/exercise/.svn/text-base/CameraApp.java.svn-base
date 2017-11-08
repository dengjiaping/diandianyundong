package com.fox.exercise;

import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.Size;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.OrientationEventListener;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.fox.exercise.util.IImageList;
import com.umeng.analytics.MobclickAgent;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import cn.ingenic.indroidsync.SportsApp;

public class CameraApp extends Activity implements Button.OnClickListener,
        ShutterButton.OnShutterButtonListener,
        SurfaceHolder.Callback,
        OnSeekBarChangeListener {
    private static final String TAG = "CameraApp";
    protected static final int AUTH_RESULT = 15;
    private static final String KEY_PICTURE_FORMAT = "picture-format";
    private MyOrientationEventListener mOrientationListener;
    private int mOrientation = OrientationEventListener.ORIENTATION_UNKNOWN;

    private static final int DELAYED_ONRESUME_FUNCTION = 1;
    private static final int SHOW_PROGRESS_FINISH = 2;
    private static final int DELAY_TO_EXIT = 3;

    private static final String FLASH_ON = "on";
    private static final String FLASH_OFF = "off";

    private static final int IDLE = 1;
    private static final int SNAPSHOT_IN_PROGRESS = 2;
    private static final int SWITCH_CAMERA_IN_PROGRESS = 3;
    private static final int THUMB_SIZE = 110;

    private static final int UPDATE_CAMERA_DEGREE = 1;
    private static final int UPDATE_PICTURE_DEGREE = 2;

    public static final int MAX_VALUE = 255;
    public static final int ZOOM_MAX_VALUE = 240;

    private int mStatus = IDLE;
    private Camera mCameraDevice;
    private Parameters mParameters;
    private int mNumberOfCameras;
    private int mCameraId = 0;
    private boolean mPreviewing;
    private boolean mPausing;
    private boolean mStartPreviewFail = false;
    //private boolean mCounting;
    private int mPreviewWidth;
    private int mPreviewHeight;
    public static int mPictureWidth;
    public static int mPictureHeight;
    private SurfaceView mSurfaceView;
    private SurfaceHolder mSurfaceHolder = null;
    private final ErrorCallback mErrorCallback = new ErrorCallback();
    private final ShutterCallback mShutterCallback = new ShutterCallback();
    private ImageButton mCancelButton;
    private ImageButton mFlashmodeButton;
    private ImageButton mSwitchIdButton;
    private ShutterButton mCaptureButton;
    //    private ImageButton mGalleryButton;
    private Button mAdjustOrientation;
    private TextView mTimingCount;
    ImageButton mFinishButton;
    public ArrayList<Uri> mBmpItems;
    public ArrayList<Bitmap> mBmpItemsThumb;
    private int mPictureCount = 0;
    private String mFlashmode = FLASH_OFF;
    private boolean mDidRegister = false;
    private int mPicturesRemaining;
    private ImageView mLOMOCamera;
    private int mTiming = 0;
    private final Handler mHandler = new MainHandler();
    //    private PreferenceGroup mPreGroup=null;
//    private ListPreference mPreCameraType;
//    private ListPreference mPreScale;
//    private ListPreference mPreComposition;
//    private ListPreference mPreTiming;
    private String mCameraTypeValue;
    private String mScaleValue;
    private String mCompositionValue;
    private String mTimingValue;

    //    private PreviewFrameLayout mFrameLayout;
    DisplayMetrics metrics;
    Display display;
    private boolean bCropImage = false;

    private final AutoFocusCallback mAutoFocusCallback =
            new AutoFocusCallback();
    private static final int FOCUS_NOT_STARTED = 0;
    private static final int FOCUSING = 1;
    private static final int FOCUSING_SNAP_ON_FINISH = 2;
    private static final int FOCUS_SUCCESS = 3;
    private static final int FOCUS_FAIL = 4;
    private int mFocusState = FOCUS_NOT_STARTED;

    private static int keypresscount = 0;
    private static int keyup = 0;
    private Dialog mProgressDialog = null;

    private static final int ZOOM_STOPPED = 0;
    private static final int ZOOM_START = 1;
    private static final int ZOOM_STOPPING = 2;

    private static final int EXIT_OK = 0;


    private static final int MIN_VALUE = 0;

    private static final int NO_STORAGE_ERROR = -1;

    public static final int CANNOT_STAT_ERROR = -2;

    private int mZoomState = ZOOM_STOPPED;
    private boolean mSmoothZoomSupported = false;
    private boolean mZoomSupported = false;
    private int mZoomValue = 0;  // The current zoom value.
    private int mZoomMax;
    private int mTargetZoomValue;
    private float[] mInitialZoomRatios;
    private SeekBar mZoomBar;
    private RelativeLayout mZoomBarLayout;
    private int zoom_step = 1;
    private int mCurProgress;
    private IImageList allImages;
    boolean mCameraForGiant = false;
    boolean mCameraForHP = false;
    private final ZoomListener mZoomListener = new ZoomListener();
    private OnScreenHint mStorageHint;
    private PreviewFrameLayout mFrameLayout;
    private SportsApp sportsApp;
    private RelativeLayout mPopupLayout;
    private RelativeLayout mCapturePopupLayout;
    private RelativeLayout mFinishPopupLayout;
    private Dialog alertDialog;
    private int mCameraDegree = 0;
    private int mCaptureDegree = 0;
    private SharedPreferences mCameraDegreeSharedPreferences;
    private SharedPreferences mCaptureDegreeSharedPreferences;
    private boolean isAdjustCaptureOrientation = false;
    private ImageView mCaptureView;
    private Bitmap mCaptureBmp;
    private Context mContext;
    private RelativeLayout sports_camera_hint;
    private static final String PREF_SPORTS_CAMERA_NAME_ISFIRSTRUN = "pref_sports_camera_isfirstrun";
    private static final String PREF_SPORTS_CAMERA_B_ISFIRSTRUN = "pref_sports_camera_Boolean_isfirstrun";
    private boolean isForAuth = false;

    private class MainHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {

                case DELAYED_ONRESUME_FUNCTION: {
                    delayedOnResume();
                    break;
                }
                case SHOW_PROGRESS_FINISH: {
                    closeProgressDialog();
                    break;
                }
                case DELAY_TO_EXIT: {
                    finish();
                    break;
                }
                default:
                    Log.v(TAG, "Unhandled message: " + msg.what);
                    break;
            }
        }
    }

    private synchronized void closeProgressDialog() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate invoked");
        super.onCreate(savedInstanceState);
        // requestWindowFeature(Window.FEATURE_NO_TITLE);
        boolean findMethod = SmartBarUtils.findActionBarTabsShowAtBottom();
        if (!findMethod) {
            // 取消ActionBar拆分，换用TabHost
            getWindow().setUiOptions(0);
            getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }
        setContentView(R.layout.sports_camera);
        mContext = this;
        sportsApp = (SportsApp) getApplication();
        isForAuth = getIntent().getBooleanExtra("forAuth", false);
        initRes();
        if (findMethod) {
            getActionBar().setDisplayShowHomeEnabled(false);
            getActionBar().setDisplayShowTitleEnabled(false);
            SmartBarUtils.setActionModeHeaderHidden(getActionBar(), true);
            SmartBarUtils.setActionBarViewCollapsable(getActionBar(), true);
        }
        mOrientationListener = new MyOrientationEventListener(this);
        int config = getResources().getInteger(R.integer.config_phone_specialchars);
        if (config == 1) {
            //bj_prj  tf_giant
            mCameraForGiant = true;
        } else if (config == 2) {
            //bj_prj  tf_giant
            mCameraForHP = true;
        }
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume invoked");
        super.onResume();
        MobclickAgent.onPageStart("CameraApp");
        MobclickAgent.onResume(this);
        mPausing = false;
        if (mOrientationListener != null)
            mOrientationListener.enable();

        if (!mPreviewing && !mStartPreviewFail) {
            cameraStart();
        }
        initializeZoom();
        keepScreenOn();
        checkImageExist();
//        mPictureCount = ImageHandler.mImageList.size();
        mHandler.sendEmptyMessageDelayed(DELAYED_ONRESUME_FUNCTION, 200);
        registerReceiver(mBatteryInfoReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
    }

    private BroadcastReceiver mBatteryInfoReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (Intent.ACTION_BATTERY_CHANGED.equals(action)) {
                int level = intent.getIntExtra("level", 0);
                int plugged = intent.getIntExtra("plugged", 0);
                if (level < 20 && plugged == 0) {
                    Toast.makeText(CameraApp.this, R.string.battery_low,
                            Toast.LENGTH_LONG).show();
                    mHandler.sendEmptyMessageDelayed(DELAY_TO_EXIT, 2000);
                }
            }
        }
    };

    @Override
    protected void onPause() {
        Log.d(TAG, "onPause invoked");
        MobclickAgent.onPageEnd("CameraApp");
        MobclickAgent.onPause(this);
        mPausing = true;
        mHandler.removeCallbacksAndMessages(null);
//    	mTiming = Integer.parseInt(mTimingValue);
//    	mTimingCount.setVisibility(View.INVISIBLE);
        stopPreview();
        closeCamera();
        resetScreenOn();
        if (mDidRegister) {
            unregisterReceiver(mReceiver);
            mDidRegister = false;
        }
        keypresscount = 0;
        mOrientationListener.disable();
        closeProgressDialog();
        unregisterReceiver(mBatteryInfoReceiver);
        super.onPause();
    }

    @Override
    protected void onStop() {
        Log.d(TAG, "onStop invoked");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy invoked");
        super.onDestroy();
        isAdjustCaptureOrientation = false;
//        ImageHandler.mImageList.clear();
//        ImageHandler.mBmpItemsThumb.clear();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
//        MenuHelper.addBaseMenuItems(this, menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        Log.d(TAG, "onActivityResult invoked," + "requestCode:" + requestCode + "resultCode:" + resultCode);
        switch (requestCode) {
            case EXIT_OK:
                if (resultCode == RESULT_OK) {
                    setResult(RESULT_OK, new Intent());
                    finish();
                }
                break;
            case AUTH_RESULT:
                if (resultCode == RESULT_OK) {
                    setResult(RESULT_OK, new Intent());
                    finish();
                }
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK: {
//            	int visible = mSettingBar.getVisibility();
//            	Log.v(TAG, "visible is "+visible);
//            	if(visible== View.VISIBLE){
//            		mSettingBar.setVisibility(View.INVISIBLE);
//            		if(mZoomSupported)
//            			mZoomBarLayout.setVisibility(View.VISIBLE);
//            		return true;
//            	}else if(mSelectCameraBar.getVisibility() == View.VISIBLE){
//            		mSelectCameraBar.setVisibility(View.INVISIBLE);
//            		if(mZoomSupported)
//            			mZoomBarLayout.setVisibility(View.VISIBLE);
//            		return true;
//            	}
//	            int visible = mPopuoLayout.getVisibility();
                if (mPopupLayout.getVisibility() == View.VISIBLE) {
                    DisplayAdjustPrompt(false);
                    isAdjustCaptureOrientation = false;
                    loadDegreeFromPref();
                    AdjustCameraOrietation();
                    return true;
                } else if (mCapturePopupLayout.getVisibility() == View.VISIBLE) {
                    DisplayCapturePrompt(false);
                    isAdjustCaptureOrientation = false;
                    loadDegreeFromPref();
                    return true;
                } else if (mFinishPopupLayout.getVisibility() == View.VISIBLE) {
                    mFinishPopupLayout.setVisibility(View.GONE);
                    isAdjustCaptureOrientation = false;
                    return true;
                }
                break;
            }
            case KeyEvent.KEYCODE_CAMERA:
            case KeyEvent.KEYCODE_DPAD_CENTER: {
                if (canTakePicture())
                    doSnap();
                else if (mPictureCount == ImageHandler.PICTURE_COUNT) {
                    Toast.makeText(this, R.string.picture_num_limit,
                            Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        }

        return super.onKeyDown(keyCode, event);
    }


    @Override
    public void onBackPressed() {
        if (!isCameraIdle()) {
            // ignore backs while we're taking a picture
            return;
        } else {
            super.onBackPressed();
        }
    }

    private boolean isCameraIdle() {
        return (mStatus == IDLE)
                && (mFocusState == FOCUS_NOT_STARTED);
    }


    private void delayedOnResume() {
        installIntentFilter();
        checkStorage();
    }

    private void loadDegreeFromPref() {
        if (mCameraId == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            mCameraDegreeSharedPreferences = getSharedPreferences(Util.PREF_NAME_FRONT_CAMERA_DEGREE, Context.MODE_WORLD_WRITEABLE);
            mCameraDegree = mCameraDegreeSharedPreferences.getInt(Util.PREF_I_FRONT_CAMERA_DEGREE, 0);
            mCaptureDegreeSharedPreferences = getSharedPreferences(Util.PREF_NAME_FRONT_PICTURE_DEGREE, Context.MODE_WORLD_WRITEABLE);
            mCaptureDegree = mCaptureDegreeSharedPreferences.getInt(Util.PREF_I_FRONT_PICTURE_DEGREE, 0);
        } else {
            mCameraDegreeSharedPreferences = getSharedPreferences(Util.PREF_NAME_BACK_CAMERA_DEGREE, Context.MODE_WORLD_WRITEABLE);
            mCameraDegree = mCameraDegreeSharedPreferences.getInt(Util.PREF_I_BACK_CAMERA_DEGREE, 0);
            mCaptureDegreeSharedPreferences = getSharedPreferences(Util.PREF_NAME_BACK_PICTURE_DEGREE, Context.MODE_WORLD_WRITEABLE);
            mCaptureDegree = mCaptureDegreeSharedPreferences.getInt(Util.PREF_I_BACK_PICTURE_DEGREE, 0);

        }
    }

    private void updateDegreeFromPref(int type) {
        if (type == UPDATE_CAMERA_DEGREE) {
            if (mCameraId == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                SharedPreferences.Editor se = mCameraDegreeSharedPreferences.edit();
                se.putInt(Util.PREF_I_FRONT_CAMERA_DEGREE, mCameraDegree);
                se.commit();
            } else {
                SharedPreferences.Editor se = mCameraDegreeSharedPreferences.edit();
                se.putInt(Util.PREF_I_BACK_CAMERA_DEGREE, mCameraDegree);
                se.commit();
            }
        } else if (type == UPDATE_PICTURE_DEGREE) {
            if (mCameraId == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                SharedPreferences.Editor se = mCaptureDegreeSharedPreferences.edit();
                se.putInt(Util.PREF_I_FRONT_PICTURE_DEGREE, mCaptureDegree);
                se.commit();
            } else {
                SharedPreferences.Editor se = mCaptureDegreeSharedPreferences.edit();
                se.putInt(Util.PREF_I_BACK_PICTURE_DEGREE, mCaptureDegree);
                se.commit();
            }
        }
    }

    private void initRes() {
        loadDegreeFromPref();
        metrics = new DisplayMetrics();
        display = getWindowManager().getDefaultDisplay();
        display.getMetrics(metrics);
        new ComboPreferences(this);
        mSurfaceView = (SurfaceView) findViewById(R.id.camera_preview);
        SurfaceHolder holder = mSurfaceView.getHolder();
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        holder.addCallback(this);
        mFrameLayout = (PreviewFrameLayout) findViewById(R.id.frame_layout);
//        mImageLayoutRoot = new LinearLayout(this);
//        mImageLayoutRoot.setOrientation(LinearLayout.HORIZONTAL);

        mCancelButton = (ImageButton) findViewById(R.id.cancelbutton);
        mFlashmodeButton = (ImageButton) findViewById(R.id.flashmodebutton);
        mSwitchIdButton = (ImageButton) findViewById(R.id.switchcameraidbutton);
        mCaptureButton = (ShutterButton) findViewById(R.id.capturebutton);
//        mGalleryButton = (ImageButton)findViewById(R.id.gallerybutton);
//        if (isForAuth) {
//        	mGalleryButton.setVisibility(View.INVISIBLE);
//		}
        mAdjustOrientation = (Button) findViewById(R.id.adjustorientationbutton);

        mCancelButton.setOnClickListener(this);
        mFlashmodeButton.setOnClickListener(this);
        mSwitchIdButton.setOnClickListener(this);
        mCaptureButton.setOnClickListener(this);
//        mGalleryButton.setOnClickListener(this);
        mAdjustOrientation.setOnClickListener(this);

        findViewById(R.id.button_rotate).setOnClickListener(this);
        findViewById(R.id.button_ok).setOnClickListener(this);
        findViewById(R.id.button_capture_rotate).setOnClickListener(this);
        findViewById(R.id.button_capture_ok).setOnClickListener(this);

//        CameraSettings settings = new CameraSettings(this);
//        mPreGroup = settings.getPreferenceGroup(R.xml.cameramanager_preferences);

//        mTimingCount = (TextView)findViewById(R.id.timing_count);

//        mPreCameraType = mPreGroup.findPreference(CameraSettings.KEY_SELECT_CAMERA);
//        mPreScale = mPreGroup.findPreference(CameraSettings.KEY_SET_SCALE);
//        mPreComposition = mPreGroup.findPreference(CameraSettings.KEY_COMPOSITION);
//        mPreTiming = mPreGroup.findPreference(CameraSettings.KEY_TIMING);


        mZoomBar = (SeekBar) findViewById(R.id.zoom);
        mZoomBar.setMax(ZOOM_MAX_VALUE);
        mZoomBar.setProgress(MIN_VALUE);
        mZoomBar.setOnSeekBarChangeListener(this);

        mZoomBarLayout = (RelativeLayout) findViewById(R.id.zoom_bar_layout);

//        if(mPreCameraType!=null){
//        	mCameraTypeValue = mPreCameraType.getValue();
//        	Log.v(TAG, "mCameraTypeValue is "+mCameraTypeValue);
//        	updateCameraTypeDisplay(mCameraTypeValue);
//        }
        mPopupLayout = (RelativeLayout) findViewById(R.id.popup_layout);
        mCapturePopupLayout = (RelativeLayout) findViewById(R.id.capture_popup_layout);
        mCaptureView = (ImageView) findViewById(R.id.imageView_capture_view);
        mFinishPopupLayout = (RelativeLayout) findViewById(R.id.finish_popup_layout);

//        findViewById(R.id.popup_imageview).setOnClickListener(this);
//        findViewById(R.id.capture_popup_imageview).setOnClickListener(this);
        findViewById(R.id.button_adjust_ok).setOnClickListener(this);

        findViewById(R.id.capture_popup_layout).setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View arg0, MotionEvent arg1) {
                return true;
            }
        });
        findViewById(R.id.popup_layout).setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View arg0, MotionEvent arg1) {
                return true;
            }
        });
        findViewById(R.id.finish_popup_layout).setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View arg0, MotionEvent arg1) {
                return true;
            }
        });
        sports_camera_hint = (RelativeLayout) findViewById(R.id.sports_camera_nav_hint);
        sports_camera_hint.setOnClickListener(this);
    }

    private void initCameraParameters() {
        Size size = mParameters.getPictureSize();
        List<Size> sizes = mParameters.getSupportedPreviewSizes();
        Size optimalSize = getOptimalPreviewSize(
                sizes, (double) size.height / size.width);
        Size original = mParameters.getPreviewSize();
        if (optimalSize != null) {
            mPreviewWidth = optimalSize.height;
            mPreviewHeight = optimalSize.width;
            Log.v(TAG, "optimalSize.width = " + optimalSize.width
                    + " optimalSize.height = " + optimalSize.height);
//            if (!original.equals(optimalSize)) { 
////            	if(mCameraId == Camera.CameraInfo.CAMERA_FACING_FRONT && mCameraForGiant){
//////            		mParameters.setPreviewSize(optimalSize.height, optimalSize.width);
////		        }else{
//////		        	mParameters.setPreviewSize(optimalSize.width, optimalSize.height);
////		        }
//            	mParameters.setPreviewSize(optimalSize.width, optimalSize.height);
//	        }
        } else {
            mPreviewWidth = original.height;
            mPreviewHeight = original.width;
        }
        mParameters.setFlashMode(mFlashmode);

        mCameraDevice.setParameters(mParameters);

    }

    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        if (holder.getSurface() == null) {
            Log.d(TAG, "holder.getSurface() == null");
            return;
        }
        mSurfaceHolder = holder;

        if (mCameraDevice == null) return;

        if (mPausing || isFinishing()) return;

        if (mPreviewing && holder.isCreating()) {
            setPreviewDisplay(holder);
        }
    }

    public void surfaceCreated(SurfaceHolder holder) {
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        stopPreview();
        if (mCameraDevice != null) {
            mCameraDevice.release();
            mCameraDevice = null;
        }
        mSurfaceHolder = null;
    }

    public static int roundOrientation(int orientation) {
        return ((orientation + 45) / 90 * 90) % 360;
    }

    private class MyOrientationEventListener
            extends OrientationEventListener {
        public MyOrientationEventListener(Context context) {
            super(context);
        }

        @Override
        public void onOrientationChanged(int orientation) {
            if (orientation == ORIENTATION_UNKNOWN) return;
            mOrientation = roundOrientation(orientation);

        }
    }

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Intent.ACTION_MEDIA_MOUNTED)
                    || action.equals(Intent.ACTION_MEDIA_UNMOUNTED)
                    || action.equals(Intent.ACTION_MEDIA_CHECKING)) {
                checkStorage();
            } else if (action.equals(Intent.ACTION_MEDIA_SCANNER_FINISHED)) {
                checkStorage();
            }
        }
    };

    private void installIntentFilter() {
        // install an intent filter to receive SD card related events.
        IntentFilter intentFilter =
                new IntentFilter(Intent.ACTION_MEDIA_MOUNTED);
        intentFilter.addAction(Intent.ACTION_MEDIA_UNMOUNTED);
        intentFilter.addAction(Intent.ACTION_MEDIA_SCANNER_FINISHED);
        intentFilter.addAction(Intent.ACTION_MEDIA_CHECKING);
        intentFilter.addDataScheme("file");
        registerReceiver(mReceiver, intentFilter);
        mDidRegister = true;
    }

    @Override
    public void onClick(View v) {
        if (!isCameraIdle())
            return;
        switch (v.getId()) {
            case R.id.cancelbutton:
                finish();
                return;
            case R.id.flashmodebutton:
                switchFlashMode();
                return;
            case R.id.switchcameraidbutton:
                if (mProgressDialog == null && mNumberOfCameras == 2)
                    switchCameraId((mCameraId + 1) % mNumberOfCameras);
                return;
            case R.id.capturebutton:
                if (canTakePicture())
                    doSnap();
                return;


//	        case R.id.gallerybutton:
            //mBmpItems.clear();
            //mBmpItemsThumb.clear();
            //scrollWrap();
//	        	gotoGalleryApp();
//	        	return;

            case R.id.adjustorientationbutton:
                alertDialog = new Dialog(this, R.style.sports_dialog);
                if (alertDialog != null) {
                    LayoutInflater mInflater = getLayoutInflater();
                    View view = mInflater.inflate(R.layout.sports_dialog, null);
                    TextView title = (TextView) view.findViewById(R.id.title);
                    title.setText(R.string.message_camera_adjust_title);
                    TextView message = (TextView) view.findViewById(R.id.message);
                    message.setText(R.string.message_adjust_camera_orientation);
                    Button btOk = (Button) view.findViewById(R.id.bt_ok);
                    btOk.setText(R.string.confirm_to_start_adjut);
                    Button btCancel = (Button) view.findViewById(R.id.bt_cancel);
                    btCancel.setText(R.string.confirm_to_cancle);
                    btOk.setOnClickListener(this);
                    btCancel.setOnClickListener(this);

                    view.setMinimumWidth((int) (SportsApp.ScreenWidth * 0.8));
                    alertDialog.setCancelable(true);
                    alertDialog.setContentView(view);
                    alertDialog.show();
                }

//	        	AlertDialog dlg = new AlertDialog.Builder(this)
//                .setTitle(getString(R.string.message_camera_adjust_title))
//                .setMessage(getString(R.string.message_adjust_camera_orientation))
//                .setPositiveButton(getString(R.string.confirm_to_start_adjut), new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                    	DisplayAdjustPrompt(true);
//                    }
//                }).setNegativeButton(getString(R.string.confirm_to_cancle), new DialogInterface.OnClickListener() {
//
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                }).create();
//	        	dlg.show();

                break;
//	        case R.id.popup_imageview:
//	        	mPopupLayout.setVisibility(View.GONE);
//	        	break;
//	        case R.id.capture_popup_imageview:
//	        	mCapturePopupLayout.setVisibility(View.GONE);
//	        	break;
            case R.id.bt_ok:
                if (alertDialog != null)
                    alertDialog.cancel();
                DisplayAdjustPrompt(true);
                break;
            case R.id.bt_cancel:
                if (alertDialog != null)
                    alertDialog.cancel();
                break;
            case R.id.button_rotate:
                mCameraDegree = (mCameraDegree + 90) % 360;
                AdjustCameraOrietation();
                break;
            case R.id.button_ok:
                DisplayAdjustPrompt(false);
                DisplayCapturePrompt(true);
                updateDegreeFromPref(UPDATE_CAMERA_DEGREE);
                isAdjustCaptureOrientation = true;
                capture();
//                AdjustPictureOrietation();
                break;
            case R.id.button_capture_rotate:

                AdjustPictureOrietation();
                break;
            case R.id.button_capture_ok:
                DisplayCapturePrompt(true);
                FinishAdjustOrientation();
                break;
            case R.id.button_adjust_ok:
                if (mFinishPopupLayout != null)
                    mFinishPopupLayout.setVisibility(View.GONE);
                break;
            case R.id.sports_camera_nav_hint:
                if (sports_camera_hint != null) {
                    sports_camera_hint.setVisibility(View.GONE);
                    sports_camera_hint.removeAllViewsInLayout();
                    sports_camera_hint = null;
                }
                break;
        }

    }

    private void DisplayAdjustPrompt(boolean show) {
        if (show == true)
            mPopupLayout.setVisibility(View.VISIBLE);
        else
            mPopupLayout.setVisibility(View.GONE);
    }

    private void DisplayCapturePrompt(boolean show) {
        if (show == true) {
            mCapturePopupLayout.setVisibility(View.VISIBLE);
        } else {
            mCapturePopupLayout.setVisibility(View.GONE);
            if (mCaptureBmp != null && !mCaptureBmp.isRecycled()) {
                mCaptureBmp.recycle();
                mCaptureBmp = null;
            }
            mCaptureView.setImageBitmap(null);
        }
    }

    private void AdjustCameraOrietation() {

        stopPreview();
        try {
            mStartPreviewFail = false;
            startPreview();

        } catch (CameraHardwareException e) {
            // In eng build, we throw the exception so that test tool
            // can detect it and report it
            if ("eng".equals(Build.TYPE)) {
                throw new RuntimeException(e);
            }
            mStartPreviewFail = true;
        }
    }

    private void AdjustPictureOrietation() {
        mCaptureDegree = (mCaptureDegree + 90) % 360;
        mCaptureBmp = SportsUtilities.rotateBitmap(mCaptureBmp, 90);
        mCaptureView.setImageBitmap(mCaptureBmp);
    }

    private void FinishAdjustOrientation() {
        DisplayCapturePrompt(false);
        updateDegreeFromPref(UPDATE_PICTURE_DEGREE);
        isAdjustCaptureOrientation = false;
        mFinishPopupLayout.setVisibility(View.VISIBLE);
    }

    private void autoFocus() throws RuntimeException {
        // Initiate autofocus only when preview is started and snapshot is not
        // in progress.
//        if (mCameraDevice != null && canTakePicture()) {
        Log.v(TAG, "Start autofocus.");
        mFocusState = FOCUSING;
        mCameraDevice.autoFocus(mAutoFocusCallback);
//        }
    }

    private void cancelAutoFocus() throws RuntimeException {
        // User releases half-pressed focus key.
        if (mFocusState == FOCUSING || mFocusState == FOCUS_SUCCESS
                || mFocusState == FOCUS_FAIL) {
            Log.v(TAG, "Cancel autofocus.");
            mCameraDevice.cancelAutoFocus();
        }
        if (mFocusState != FOCUSING_SNAP_ON_FINISH) {
            clearFocusState();
        }
    }

    private void clearFocusState() {
        mFocusState = FOCUS_NOT_STARTED;
    }

    private void doFocus(boolean pressed) {
        if (pressed) {  // Focus key down.
            try {
                autoFocus();
            } catch (RuntimeException ie) {
                Log.e(TAG, "autoFocus error ");
                cancelAutoFocus();
                clearFocusState();
                doSnap();
            }


        } else {  // Focus key up.
            try {
                cancelAutoFocus();
            } catch (RuntimeException ie) {
                Log.e(TAG, "cancelAutoFocus error ");
                //showErrorToast();
            }
        }
    }

    private final class AutoFocusCallback
            implements android.hardware.Camera.AutoFocusCallback {
        public void onAutoFocus(
                boolean focused, android.hardware.Camera camera) {
            Log.v(TAG, "AutoFocusCallback focused =" + focused);
            if (mFocusState == FOCUSING_SNAP_ON_FINISH) {
                // Take the picture no matter focus succeeds or fails. No need
                // to play the AF sound if we're about to play the shutter
                // sound.
                if (focused) {
                    mFocusState = FOCUS_SUCCESS;
                } else {
                    mFocusState = FOCUS_FAIL;
                }
                capture();
            } else if (mFocusState == FOCUSING) {
                // User is half-pressing the focus key. Play the focus tone.
                // Do not take the picture now.

                if (focused) {
                    mFocusState = FOCUS_SUCCESS;
                } else {
                    mFocusState = FOCUS_FAIL;
                }
                capture();
            } else if (mFocusState == FOCUS_NOT_STARTED) {
                // User has released the focus key before focus completes.
                // Do nothing.
            }
            //updateFocusIndicator();
        }
    }


    public void doSnap() {
        mStatus = SNAPSHOT_IN_PROGRESS;
//    	if(mCameraId==1)
//    		mTiming =2;
//    	updateTimerView();
        if (mCameraId == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            capture();
        } else {
            doFocus(true);
        }
    }

    public void cameraStart() {
        try {
            mStartPreviewFail = false;
            mNumberOfCameras = Camera.getNumberOfCameras();
            Log.d(TAG, "mNumberOfCameras:" + mNumberOfCameras);
            if (mNumberOfCameras <= 0)
                return;
            if (mNumberOfCameras > 2)
                mNumberOfCameras = 2;
            if (mNumberOfCameras == 1) {
                mCameraId = 0;
                mFlashmodeButton.setVisibility(View.VISIBLE);
            }
            ensureCameraDevice();
            startPreview();
            if (!mCameraForGiant)
                updateHint();

        } catch (CameraHardwareException e) {
            // In eng build, we throw the exception so that test tool
            // can detect it and report it
            if ("eng".equals(Build.TYPE)) {
                throw new RuntimeException(e);
            }
            mStartPreviewFail = true;
        }
    }

    private void ensureCameraDevice() throws CameraHardwareException {
        if (mCameraDevice == null) {
            mCameraDevice = Camera.open(mCameraId);
            mParameters = mCameraDevice.getParameters();
        }
    }

    private void setPreviewDisplay(SurfaceHolder holder) {
        try {
            mCameraDevice.setPreviewDisplay(holder);
        } catch (Throwable ex) {
            closeCamera();
            throw new RuntimeException("setPreviewDisplay failed", ex);
        }
    }

    private void startPreview() throws CameraHardwareException {
        if (mPausing || isFinishing()) return;

        ensureCameraDevice();
//    	Size size = mParameters.getPictureSize();
//        List<Size> sizes = mParameters.getSupportedPreviewSizes();
//        Size optimalSize = getOptimalPreviewSize(
//                sizes, (double) size.height / size.width);
////        Size original = mParameters.getPreviewSize();
//        if (optimalSize != null) {
////            if (!original.equals(optimalSize)) { 
//            	if(mCameraId == Camera.CameraInfo.CAMERA_FACING_FRONT && mCameraForGiant){
////            		mParameters.setPreviewSize(optimalSize.height, optimalSize.width);
//		        }else{
////		        	mParameters.setPreviewSize(optimalSize.width, optimalSize.height);
//		        }
////            	mCameraDevice.setParameters(mParameters);
////	        }
//        }
        // If we're previewing already, stop the preview first (this will blank
        // the screen).
        if (mPreviewing) stopPreview();

        setPreviewDisplay(mSurfaceHolder);
        if (mCameraId == Camera.CameraInfo.CAMERA_FACING_FRONT && mCameraForGiant) {
            mCameraDevice.setDisplayOrientation(0);
        }
//        else if(mCameraId == Camera.CameraInfo.CAMERA_FACING_FRONT && mCameraForHP){
//        	mCameraDevice.setDisplayOrientation(180);
//        }
        else {
            Util.setCameraDisplayOrientation(this, mCameraId, mCameraDevice, mCameraDegree);
        }

        mCameraDevice.setErrorCallback(mErrorCallback);
        initCameraParameters();
        try {
            Log.v(TAG, "startPreview");
            mCameraDevice.startPreview();
        } catch (Throwable ex) {
            closeCamera();
            throw new RuntimeException("startPreview failed", ex);
        }

        mFrameLayout.setAspectRatio((double) mPreviewWidth / mPreviewHeight);
        mPreviewing = true;
        mStatus = IDLE;
    }

    private void stopPreview() {
        if (mCameraDevice != null) {
            Log.v(TAG, "stopPreview");
            if (mPreviewing)
                mCameraDevice.stopPreview();
        }
        mPreviewing = false;
    }

    private void closeCamera() {
        if (mCameraDevice != null) {
            mCameraDevice.stopPreview();
            mCameraDevice.release();
            mCameraDevice = null;
            mPreviewing = false;
        }
    }

    private Size getOptimalPreviewSize(List<Size> sizes, double targetRatio) {
        final double ASPECT_TOLERANCE = 0.05;
        if (sizes == null) return null;

        Size optimalSize = null;
        double minDiff = Double.MAX_VALUE;

        Display display = getWindowManager().getDefaultDisplay();
        int targetHeight = Math.max(display.getHeight(), display.getWidth());
        Log.v(TAG, "display..getWidth() = " + display.getWidth()
                + " display.getHeight() = " + display.getHeight());

        if (targetHeight <= 0) {
            // We don't know the size of SurefaceView, use screen height
            WindowManager windowManager = (WindowManager)
                    getSystemService(Context.WINDOW_SERVICE);
            targetHeight = windowManager.getDefaultDisplay().getHeight();
        }

        // Try to find an size match aspect ratio and size
        for (Size size : sizes) {
            double ratio = (double) size.width / size.height;
            if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE) continue;
            if (Math.abs(size.height - targetHeight) < minDiff) {
                optimalSize = size;
                minDiff = Math.abs(size.height - targetHeight);
            }
        }

        // Cannot find the one match the aspect ratio, ignore the requirement
        if (optimalSize == null) {
            Log.v(TAG, "No preview size match the aspect ratio");
            minDiff = Double.MAX_VALUE;
            for (Size size : sizes) {
                if (Math.abs(size.height - targetHeight) < minDiff) {
                    optimalSize = size;
                    minDiff = Math.abs(size.height - targetHeight);
                }
            }
        }
        return optimalSize;
    }

    private void switchCameraId(int cameraId) {
        if (mPausing || !mPreviewing) return;

        mStatus = SWITCH_CAMERA_IN_PROGRESS;
        //closeProgressDialog();
        CharSequence c = getText(R.string.progress_switching);
//        mProgressDialog = ProgressDialog.show(this, "", c, true, false);
        mProgressDialog = new Dialog(this, R.style.sports_dialog);
        LayoutInflater mInflater = getLayoutInflater();
        View v = mInflater.inflate(R.layout.sports_progressdialog, null);
        TextView message = (TextView) v.findViewById(R.id.message);
        message.setText(c);
        v.setMinimumWidth((int) (SportsApp.ScreenWidth * 0.8));
        mProgressDialog.setContentView(v);
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
        mCameraId = cameraId;
        loadDegreeFromPref();
        Log.v(TAG, "switchCameraId mCameraId =" + mCameraId);
        stopPreview();
        closeCamera();

        try {
            mStartPreviewFail = false;
            startPreview();

        } catch (CameraHardwareException e) {
            // In eng build, we throw the exception so that test tool
            // can detect it and report it
            if ("eng".equals(Build.TYPE)) {
                throw new RuntimeException(e);
            }
            mStartPreviewFail = true;
        }
        if (mCameraId == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            mFlashmodeButton.setVisibility(View.GONE);
        } else {
            mFlashmodeButton.setVisibility(View.VISIBLE);
            mParameters.setFlashMode(mFlashmode);
            mCameraDevice.setParameters(mParameters);
        }
        initializeZoom();

        mHandler.sendEmptyMessageDelayed(SHOW_PROGRESS_FINISH, 500);
    }

    private void checkStorage() {
        calculatePicturesRemaining();
        updateStorageHint(mPicturesRemaining);
    }

    private int calculatePicturesRemaining() {
        mPicturesRemaining = SportsUtilities.calculatePicturesRemaining();
        return mPicturesRemaining;
    }

    private void updateStorageHint(int remaining) {
        String noStorageText = null;

        if (remaining == NO_STORAGE_ERROR) {
            String state = Environment.getExternalStorageState();
            if (state == Environment.MEDIA_CHECKING) {
                noStorageText = getString(R.string.preparing_sd);
            } else {
                noStorageText = getString(R.string.no_storage);
            }
        } else if (remaining == CANNOT_STAT_ERROR) {
            noStorageText = getString(R.string.access_sd_fail);
        } else if (remaining < 1) {
            noStorageText = getString(R.string.not_enough_space);
        }

        if (noStorageText != null) {
            if (mStorageHint == null) {
                mStorageHint = OnScreenHint.makeText(this, noStorageText);
            } else {
                mStorageHint.setText(noStorageText);
            }
            mStorageHint.show();
        } else if (mStorageHint != null) {
            mStorageHint.cancel();
            mStorageHint = null;
        }
    }

    private void capture() {
//    	mTimingCount.setVisibility(View.INVISIBLE);
        CharSequence c = getText(R.string.progress_capturing);
//        mProgressDialog = ProgressDialog.show(this, "", c, true, false);
        mProgressDialog = new Dialog(this, R.style.sports_dialog);
        LayoutInflater mInflater = getLayoutInflater();
        View v = mInflater.inflate(R.layout.sports_progressdialog, null);
        TextView message = (TextView) v.findViewById(R.id.message);
        message.setText(c);
        v.setMinimumWidth((int) (SportsApp.ScreenWidth * 0.8));
        mProgressDialog.setContentView(v);
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
        int rotation = 0;
        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(mCameraId, info);
        if (info.orientation < 0)
            info.orientation = 0;
        int degrees = Util.getDisplayRotation(this);
        if (mOrientation != OrientationEventListener.ORIENTATION_UNKNOWN) {
            if (mCameraId == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                if (mCameraForGiant) {
                    if (mOrientation == 0) {
                        rotation = (mOrientation + 180) % 360;
                    } else if (mOrientation == 90) {
                        rotation = (mOrientation) % 360;
                    } else if (mOrientation == 180) {
                        rotation = (mOrientation - 180) % 360;
                    } else if (mOrientation == 270) {
                        rotation = (mOrientation) % 360;
                    }
                } else {
                    if (mOrientation == 0) {
                        rotation = (mOrientation + info.orientation + degrees) % 360;
                    } else if (mOrientation == 90) {
                        rotation = (mOrientation + info.orientation + degrees + 180) % 360;
                    } else if (mOrientation == 180) {
                        rotation = (mOrientation + info.orientation + degrees) % 360;
                    } else if (mOrientation == 270) {
                        rotation = (mOrientation + info.orientation + degrees - 180) % 360;
                    }
                }

            } else {
                rotation = (info.orientation + mOrientation - degrees) % 360;
            }
            if (rotation < 0)
                rotation += 360;
            if (mParameters != null)
                mParameters.setRotation(rotation);
            Log.v(TAG, "capture rotation = " + rotation + " mOrientation =" + mOrientation);
        }

//        List<Size> supported = mParameters.getSupportedPictureSizes();
//        for(Size size:supported){
//        	if(size.width == SportsUtilities.IMAGE_DEFAULT_WIDTH){
//            	Log.v(TAG, "size.width = "+size.width+" size.height = "+size.height);
//            	if(mCameraId == Camera.CameraInfo.CAMERA_FACING_FRONT && mCameraForGiant){
//            		mParameters.setPictureSize(size.height, size.width);
//            	}else{
//            		mParameters.setPictureSize(size.width, size.height);
//            	}
//        		break;
//        	}
//        }
        if (mCameraDevice != null)
            mCameraDevice.setParameters(mParameters);

        incrementkeypress();
        SportsUtilities.mPictureFormat = mParameters.get(KEY_PICTURE_FORMAT);
        if (mCameraDevice != null)
            mCameraDevice.takePicture(mShutterCallback, null,
                    null, new JpegPictureCallback());
        mPreviewing = false;
        mFocusState = FOCUS_NOT_STARTED;
    }

    private final class ShutterCallback implements
            android.hardware.Camera.ShutterCallback {
        public void onShutter() {
//			mShutterCallbackTime = System.currentTimeMillis();
//			mShutterLag = mShutterCallbackTime - mCaptureStartTime;
//			Log.v(TAG, "mShutterLag = " + mShutterLag + "ms");
//			mFocusManager.onShutter();
//			mModeActor.onShutter();
        }
    }

    private Bitmap justfyImage(String path, boolean adjustFrontCamera) {
        int scale = 1;
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, o);
        Display display = getWindowManager().getDefaultDisplay();
        int dest_w = 640;
        int dest_h = 800;
//    	while (o.outWidth / 2 >= dest_w && o.outHeight / 2 >= dest_h) { // &&
//    		o.outWidth /= 2;
//    		o.outHeight /= 2;
//    		scale *= 2;
//		}
//		if (scale < 1) {
//			scale = 1;
//		}
        if (o.outWidth > dest_w || o.outHeight > dest_h) {
            scale = (int) Math.pow(
                    2.0,
                    (int) Math.round(Math.log(dest_w
                            / (double) Math.max(o.outHeight, o.outWidth))
                            / Math.log(0.5)));
            // scale = 2;
        }
        Log.d(TAG, scale + " scale");

        // 2. File -> Bitmap (Returning a smaller image)
        o.inJustDecodeBounds = false;
        o.inSampleSize = scale;
        o.inTempStorage = new byte[4 * 1024];
        o.inInputShareable = true;
        o.inPurgeable = true;
        o.inPreferredConfig = Bitmap.Config.RGB_565;
        File file = new File(path);
        InputStream is = null;
        try {
            is = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Bitmap bmp = BitmapFactory.decodeStream(is, null, o);
        bmp = Utils.rotate(bmp, mCaptureDegree);
        if (adjustFrontCamera && mCameraId == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            return ImageHandler.reverseBitmap(bmp, 0);
        }
        return bmp;
    }

    private final class JpegPictureCallback implements PictureCallback {

        public void onPictureTaken(byte[] jpegData, final Camera camera) {
            if (mPausing) {
                mHandler.sendEmptyMessage(SHOW_PROGRESS_FINISH);
                return;
            }

            if (jpegData != null) {
                if (isAdjustCaptureOrientation) {
                    mHandler.sendEmptyMessage(SHOW_PROGRESS_FINISH);
                    Uri tempUri = Util.storeUriImage(CameraApp.this, jpegData, null, false, Util.PIXEL_FORMAT_JPEG, false);
                    if (tempUri == null)
                        return;
                    mCaptureBmp = justfyImage(tempUri.toString(), false);
                    mCaptureView.setImageBitmap(mCaptureBmp);
                    jpegData = null;
                    try {
                        mStartPreviewFail = false;
                        startPreview();
                    } catch (CameraHardwareException e) {
                        if ("eng".equals(Build.TYPE)) {
                            throw new RuntimeException(e);
                        }
                        mStartPreviewFail = true;
                    }
                    return;
                }
//            	Bitmap bmpTemp1 = Util.byte2Bitmap(jpegData, bCropImage);
                Uri tempUri = Util.storeUriImage(CameraApp.this, jpegData, null, false, Util.PIXEL_FORMAT_JPEG, false);
                if (tempUri == null)
                    return;
                Bitmap bmpTemp = justfyImage(tempUri.toString(), true);
                jpegData = null;
                if (bmpTemp != null)
                    sportsApp.setPreviewBitmap(bmpTemp);
                Util.mImageSaveToTemp = false;
                final Uri imageUri = Util.storeUriImage(CameraApp.this, null, bmpTemp, true, Util.PIXEL_FORMAT_JPEG, false);
                Log.d(TAG, "image path saved in capture" + imageUri);
                ImageHandler.imageUri = Util.mCurrContentUri;
                new Thread() {
                    public void run() {

                        ImageHandler.mImagePathList.add(imageUri.toString());
                        Log.d(TAG, "ImageHandler.mImageList.size():" + ImageHandler.mImagePathList.size());
                    }

                    ;
                }.start();

                if (imageUri != null) {
                    gotoCapturePreview(imageUri);
                }
            }
        }
    }


    private void checkImageExist() {
//    	int count = ImageHandler.mImageList.size();
//    	Log.d(TAG,"ImageHandler.mImageList.size() in checkImageExist:"+ImageHandler.mImageList.size());
//    	Log.d(TAG,"ImageHandler.mBmpItemsThumb.size() in checkImageExist:"+ImageHandler.mBmpItemsThumb.size());
//    	for(int i =0; i<count; i++){
//			File file = new File(ImageHandler.mBmpItems.get(i).toString());
//    		Log.d(TAG,"ImageHandler.mImageList.get(i).isFileExist():"+ImageHandler.mImageList.get(i).isFileExist());
//			if(!ImageHandler.mImageList.get(i).isFileExist()){
//				ImageHandler.mImageList.remove(i);
//				ImageHandler.mBmpItemsThumb.remove(i);
//				mPictureCount--;
//			}
//    	}
//    	scrollWrap();
    }


    private void gotoCapturePreview(Uri imageUri) {
        Intent intent = new Intent(this, CapturePreview.class);
        intent.putExtra("image_url", imageUri.toString());
        intent.putExtra("from_camera", true);
        intent.putExtra("forAuth", getIntent().getBooleanExtra("forAuth", false));
        try {
//        	startActivityForResult(intent, EXIT_OK);
            startActivityForResult(intent, AUTH_RESULT);
            if (!isForAuth)
                finish();
        } catch (ActivityNotFoundException e) {
            Log.e(TAG, "Could not start ImageProcess activity", e);
        }

    }


    private final class ErrorCallback
            implements android.hardware.Camera.ErrorCallback {
        public void onError(int error, android.hardware.Camera camera) {
            switch (error) {
                case Camera.CAMERA_ERROR_SERVER_DIED:
                    Log.v(TAG, "media server died");
                    break;
                case Camera.CAMERA_ERROR_UNKNOWN:
                    Log.d(TAG, "Camera Driver Error");
                    showCameraStoppedAndFinish();
                    break;
            }
        }
    }

    private void showCameraStoppedAndFinish() {
        Resources ress = getResources();
        SportsUtilities.showFatalErrorAndFinish(this,
                ress.getString(R.string.camera_application_stopped),
                ress.getString(R.string.camera_driver_needs_reset));
    }

    private void resetScreenOn() {
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    private void keepScreenOn() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    private void switchFlashMode() {
        if (mFlashmode.equals(FLASH_ON)) {
            mFlashmode = FLASH_OFF;
            mFlashmodeButton.setImageResource(R.drawable.sports_capture_title_noflash);
        } else {
            mFlashmode = FLASH_ON;
            mFlashmodeButton.setImageResource(R.drawable.sports_capture_title_flash);
        }
        mParameters.setFlashMode(mFlashmode);
        mCameraDevice.setParameters(mParameters);
    }

    private boolean canTakePicture() {
        return mPreviewing && (mPicturesRemaining > 0) && isCameraIdle();
    }


//    private void gotoGalleryApp(){
//    	Intent intent = new Intent(this, AlbumsLists.class);
//    	intent.putExtra("goback_camera", true);
//        try{
//        	startActivity(intent);
//        	finish();
//        }catch(ActivityNotFoundException e){
//        	Log.e(TAG,"Could not start AlbumsLists activity",e);
//        }
//    }


    private synchronized void incrementkeypress() {
        if (keypresscount == 0)
            keypresscount++;
    }

    //    private synchronized void decrementkeypress() {
//        if(keypresscount > 0)
//            keypresscount--;
//    }
    private synchronized int keypressvalue() {
        return keypresscount;
    }


    public void onShutterButtonFocus(ShutterButton button, boolean pressed) {
//    	updateSettingMenuDisplay(false);
        if (mPausing) {
            return;
        }
        int keydown = keypressvalue();
        if (keydown == 0 && pressed) {
            keyup = 1;
            Log.v(TAG, "the keydown is  pressed first time");
        } else if (keyup == 1 && !pressed) {
            Log.v(TAG, "the keyup is pressed first time ");
            keyup = 0;
        }
        switch (button.getId()) {
            case R.id.capturebutton:
                doFocus(pressed);
                break;
        }
    }

    public void onShutterButtonClick(ShutterButton button) {
//    	updateSettingMenuDisplay(false);
        if (mPausing) {
            return;
        }
        switch (button.getId()) {
            case R.id.capturebutton:
                if (canTakePicture())
                    doSnap();
                break;
        }
    }

    private void initializeZoom() {
        if (mParameters == null || mCameraDevice == null
                || mZoomBarLayout == null || mZoomBar == null)
            return;
        mZoomSupported = mParameters.isZoomSupported();

        if (!mZoomSupported) {
            mZoomBarLayout.setVisibility(View.GONE);
            return;
        } else {
            mZoomBarLayout.setVisibility(View.VISIBLE);
        }

        mZoomMax = mParameters.getMaxZoom();
        mSmoothZoomSupported = mParameters.isSmoothZoomSupported();

        mCameraDevice.setZoomChangeListener(mZoomListener);

        mInitialZoomRatios = getZoomRatios();
        if (mInitialZoomRatios.length - 1 > 0)
            zoom_step = ZOOM_MAX_VALUE / (mInitialZoomRatios.length - 1);

        mZoomValue = 0;
        mZoomBar.setProgress(mZoomValue);
    }

    private float[] getZoomRatios() {

        List<Integer> zoomRatios = mParameters.getZoomRatios();
        float result[] = new float[zoomRatios.size()];
        Log.v(TAG, " result.length=" + result.length);
        for (int i = 0, n = result.length; i < n; ++i) {
            result[i] = (float) zoomRatios.get(i) / 100f;
//            Log.v(TAG, "i="+i+" result[i]="+result[i]);
        }
        return result;
    }


    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        mCurProgress = progress;
        int index = mCurProgress / zoom_step;
        index = zoomClamp(index, 0, mInitialZoomRatios.length - 1);
        onZoomValueChanged(index);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    private void onZoomValueChanged(int index) {
        if (mSmoothZoomSupported) {
            if (mTargetZoomValue != index && mZoomState != ZOOM_STOPPED) {
                mTargetZoomValue = index;
                if (mZoomState == ZOOM_START) {
                    mZoomState = ZOOM_STOPPING;
                    if (mCameraDevice != null)
                        mCameraDevice.stopSmoothZoom();
                }
            } else if (mZoomState == ZOOM_STOPPED && mZoomValue != index) {
                mTargetZoomValue = index;
                if (mCameraDevice != null)
                    mCameraDevice.startSmoothZoom(index);
                mZoomState = ZOOM_START;
            }
        } else {
            mZoomValue = index;
            updateCameraParametersZoom();
        }
    }

    private void updateCameraParametersZoom() {
        // Set zoom.
        if (mParameters.isZoomSupported()) {
            mParameters.setZoom(mZoomValue);
            mCameraDevice.setParameters(mParameters);
        }
    }

    private int zoomClamp(int x, int min, int max) {
        if (x > max) return max;
        if (x < min) return min;
        return x;
    }

    private final class ZoomListener
            implements android.hardware.Camera.OnZoomChangeListener {

        public void onZoomChange(
                int value, boolean stopped, android.hardware.Camera camera) {
//		    Log.v(TAG, "Zoom changed: value=" + value + ". stopped="+ stopped);
            mZoomValue = value;
            // Keep mParameters up to date. We do not getParameter again in
            // takePicture. If we do not do this, wrong zoom value will be set.
            mParameters.setZoom(value);
            // We only care if the zoom is stopped. mZooming is set to true when
            // we start smooth zoom.
            if (stopped && mZoomState != ZOOM_STOPPED) {
                if (value != mTargetZoomValue) {
                    mCameraDevice.startSmoothZoom(mTargetZoomValue);
                    mZoomState = ZOOM_START;
                } else {
                    mZoomState = ZOOM_STOPPED;
                }
            }
        }
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (clearHint()) {
            return true;
        } else {
            if (event.getRepeatCount() > 0 && event.getKeyCode() == KeyEvent.KEYCODE_MENU) {
                return true;
            }
        }
        return super.dispatchKeyEvent(event);

    }

    private void updateHint() {
        String curPref = PREF_SPORTS_CAMERA_NAME_ISFIRSTRUN;
        String curValue = PREF_SPORTS_CAMERA_B_ISFIRSTRUN;
        SharedPreferences misFirstRunSharedPreferences = getSharedPreferences(curPref, Context.MODE_WORLD_WRITEABLE);
        boolean mIsfirstrun = misFirstRunSharedPreferences.getBoolean(curValue, true);
        SharedPreferences.Editor se = misFirstRunSharedPreferences.edit();

        if (mIsfirstrun) {
            mIsfirstrun = false;
            se.putBoolean(curValue, mIsfirstrun);
            se.commit();
            sports_camera_hint.setVisibility(View.VISIBLE);
        }

    }

    public boolean clearHint() {
        if (sports_camera_hint != null && sports_camera_hint.getVisibility() == View.VISIBLE) {
            sports_camera_hint.setVisibility(View.GONE);
            return true;
        }
        return false;
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
