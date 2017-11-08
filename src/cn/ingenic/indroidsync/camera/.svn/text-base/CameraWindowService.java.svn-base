package cn.ingenic.indroidsync.camera;

import cn.ingenic.indroidsync.Config;
import cn.ingenic.indroidsync.DefaultSyncManager;
import cn.ingenic.indroidsync.DefaultSyncManager.OnChannelCallBack;
import cn.ingenic.indroidsync.data.DefaultProjo;
import cn.ingenic.indroidsync.data.Projo;
import cn.ingenic.indroidsync.data.ProjoList;
import cn.ingenic.indroidsync.data.ProjoType;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.PreviewCallback;
import android.hardware.Camera.Size;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.View.OnTouchListener;
import android.widget.Toast;
import com.fox.exercise.R;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.UUID;

@SuppressLint("HandlerLeak") 
public class CameraWindowService extends Service 
    implements PreviewCallback, 
	       SurfaceHolder.Callback, 
	       OnChannelCallBack{
    private static final String TAG = "CameraWindowService";
    public static final UUID PREVIEW_UUID = UUID.fromString("010100d5-afac-11de-8a39-0800200c9a66");
    public static final int DEFAULT_SWITCH = -1;
    public static final int expectFrameRate = 5;
    public int defaultAngle = 90;
    
    public static final int NONE = 0;
    public static final int DRAG = 1;
    public static final int ZOOM = 2;

    public static int INIT_HEIGHT = 320;
    public static int INIT_WIDTH = 240;
    public static final int MIN_HEIGHT = 50;
    //***** come from service Events (to window)
    static final int EVENT_INIT_WINDOW = 1;
    static final int EVENT_SCREEN_OFF = 2;
    static final int DISPLAY_WINDOW = 3;

    static final int CREATE_PREVIEW_CHANNEL = 11;
    static final int WINDOW_EVENT_CLOSE = 12;
    static final int CLOSE_SERVICE_NO_NOTIFY = 13;

    private Context mContext;
    private Camera mCameraDevice;
    private int cameraId = 0;  //default back camera.
    private boolean hasOpenedCamera;
    private boolean hasConnectSuccess;
    private boolean takePictureSuccess;
    private Parameters mParameters;
    private SurfaceHolder mainHolder;
    private WindowManager mWinManager;
    public PreviewWindow mWindow;
    private int dropCount = -1;
    private long mDateTaken;
    private String mPath;
    private float sendPictureWidth;
    private volatile boolean isStopping;

    private Handler mServiceHandler = new Handler(){  
	    @Override  
		public void handleMessage(Message msg) {  
	        switch (msg.what) {
	        case CREATE_PREVIEW_CHANNEL:
		    DefaultSyncManager.getDefault().createChannel(CameraModule.CAMERA, PREVIEW_UUID);
		    break;

		case WINDOW_EVENT_CLOSE:  
		    sendExitResponse();
		    stopService();
	            break;

		case CLOSE_SERVICE_NO_NOTIFY:
		    stopService();
		    break;

		default:  
		    Log.e(TAG, "Not Get Message from preview window!");  
	            break;  
		}  
	    } 
	};

    public void logi(String s) { Log.i(TAG, s); }
    public void loge(String s) { Log.e(TAG, s); }

    public void showToast(int message){
	showToast(getString(message));
    }

    public void showToast(String message){
	Toast mToast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
	mToast.setGravity(Gravity.CENTER,0,0);
	mToast.show();
    }

    @Override
	public void onCreate() {
		super.onCreate();
		mContext = getBaseContext();
		mWinManager = (WindowManager) getSystemService("window");
		DisplayMetrics dm = new DisplayMetrics();
		mWinManager.getDefaultDisplay().getMetrics(dm);
		INIT_WIDTH = dm.widthPixels / 2;
		INIT_HEIGHT = dm.heightPixels / 2;
		CameraModule.setChannelCallBack(this);
		if (openCamera(cameraId)){
			logi("camera sensor has been opened successfully.");
			Message msg = mServiceHandler.obtainMessage(CREATE_PREVIEW_CHANNEL);
			mServiceHandler.sendMessage(msg);
		} else {
			Log.w(TAG, "it is failed to open camera sensor!");
			hasOpenedCamera = false;
			sendOpenResult(CameraTransaction.OPEN_RESULT_FAILED_SENSOR);
			showToast(R.string.open_camera_fail);
			stopService();
		}

		if (!CameraUtil.checkSpace()) {
			Toast mStorageToast = Toast.makeText(mContext, R.string.no_storage,
												 Toast.LENGTH_LONG);
			mStorageToast.setGravity(Gravity.CENTER,0,0);
			mStorageToast.show();
		}
		logi("onCreate end");
    }

    @Override
	public IBinder onBind(Intent intent) {
	Log.v(TAG,"onBind");

	return null;
    }

    @Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		logi("onStartCommand ");
		if (!hasOpenedCamera) return START_NOT_STICKY;

		switch (intent.getIntExtra("requestId", -1)){
		case CameraTransaction.OPEN_CAMERA_REQUEST:
			sendPictureWidth = intent.getIntExtra("maxBound", 320);
			initWindow();
			break;
		case CameraTransaction.TAKE_PICTURE_REQUEST:
			takePicture();
			break;
		case CameraTransaction.SWITCH_CAMERA_REQUEST:
			reopenCamera(DEFAULT_SWITCH);
			break;
		case CameraTransaction.EXIT_CAMERA_REQUEST:
			stopService();
			break;
		case CameraModule.HIDE_PREVIEW_WINDOW:
			hidePreviewWindow();
			break;
		default:
	
		}
        return START_NOT_STICKY;
    }

    private void initWindow(){
	if (mWindow != null) return;
	mWindow = new PreviewWindow();
	mWindow.getWindowHandler().removeMessages(EVENT_INIT_WINDOW);
	mWindow.getWindowHandler().sendEmptyMessage(EVENT_INIT_WINDOW);
	CameraModule.setServer(mWindow);
	logi("PreviewWindow start");
    }

    @Override
	public void onDestroy() {
	super.onDestroy();
	Log.v(TAG,"onDestroy");
    }

    public void stopService(){
	logi("stopService---------------------------------");
	closeCamera();
	DefaultSyncManager.getDefault().destoryChannel(CameraModule.CAMERA, PREVIEW_UUID);
	if (mWindow != null && mWindow.hasWindow()){
	    mWindow.closePanel();
	}
	CameraModule.setServer(null);
	CameraModule.setChannelCallBack(null);
	mWindow = null;
	stopSelf();
    }

    public void setDefaultAngle(int rotation){
	defaultAngle = rotation;
    }

    public int getDefaultAngle(){
	return defaultAngle;
    }

    public void recoverAngle(){
	defaultAngle = 90;
    }

    //SurfaceHolder.Callback interface.
    @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        if (holder.getSurface() == null) {
            loge("holder.getSurface() == null");
            return;
        }

        logi("surfaceChanged. w=" + w + ". h=" + h);
        mainHolder = holder;
        if (mCameraDevice == null) return;
        if (hasOpenedCamera) {
            startPreview();
        }
    }

    @Override
	public void surfaceCreated(SurfaceHolder holder) {
    }

    @Override
	public void surfaceDestroyed(SurfaceHolder holder) {
        mainHolder = null;
    }


    //OnChannelCallBack interface.
    @Override
	public void onCreateComplete(boolean success, boolean local){
		if (!local) return;
		hasConnectSuccess = success;
		logi("create camera preview channel ---->" + (success ? "successful" : "failed"));

		if (success){
			sendOpenResult(CameraTransaction.OPEN_RESULT_SUCCESS);
		} else {
			sendOpenResult(CameraTransaction.OPEN_RESULT_FAILED_CHANNEL);
			showToast(R.string.create_preview_channel_fail);
			stopService();
		}
    }
	
    @Override
    public void onRetrive(ProjoList projoList){
    }

    @Override
    public void onDestory(){
	logi("phone-----OnChannelCallBack--->onDestory()");
    }

    //Operate camera hardware.
    public boolean openCamera(int id){
	logi("openCamera");
        if (mCameraDevice == null) {
            try {
            	if (android.os.Build.VERSION.SDK_INT <= android.os.Build.VERSION_CODES.FROYO)
            		mCameraDevice = android.hardware.Camera.open();
            	else
            		mCameraDevice = android.hardware.Camera.open(id);
            } catch (RuntimeException e) {
                Log.e(TAG, "fail to connect Camera "+id, e);
				return false;
            }
        } else {
            try {
                mCameraDevice.reconnect();
            } catch (IOException e) {
                Log.e(TAG, "reconnect failed.");
				return false;
            }
        }
	cameraId = id;
	isStopping = false;

	recoverAngle();
	startPreview();
	return true;
    }

    private synchronized void startPreview(){
	logi("startPreview");
	if (mCameraDevice == null || isStopping) return;

	if (hasOpenedCamera) {
	    stopPreview();
	}

	if (mParameters == null) {
	    mParameters = mCameraDevice.getParameters();

	    List<Size> sizes = mParameters.getSupportedPreviewSizes();
	    Size size = CameraUtil.getOptimalPreviewSize(mParameters.getPictureSize().width,
							 mParameters.getPictureSize().height,
							 sizes);
	    if (size != null) {
		mParameters.setPreviewSize(size.width, size.height);
		logi("server preview size is " + size.width + "x" + size.height);

		double mScale = (double)Math.max(size.height, size.width) / Math.min(size.height, size.width);

		if (INIT_WIDTH > INIT_HEIGHT){
		    INIT_WIDTH = (int)(INIT_HEIGHT * mScale);
		} else {
		    INIT_HEIGHT = (int)(INIT_WIDTH * mScale);
		}
	    }else{
		mParameters.setPreviewSize(640, 480);
		logi("server uses default preview size : " + 640 + "x" + 480);
	    }
	}
	if (!isStopping){
	    try {
		mCameraDevice.setParameters(mParameters);
	    } catch (RuntimeException e) {
		loge("startPreview: set new parameters failed, so use the old one.");
	    }
	}
	int mDisplayRotation = CameraUtil.getDisplayRotation(mWinManager);
        int mDisplayOrientation = CameraUtil.getDisplayOrientation(mDisplayRotation, cameraId);
	logi("mDisplayOrientation--------------"+mDisplayOrientation);
	if (!isStopping){
	    mCameraDevice.setDisplayOrientation(mDisplayOrientation);
	    //	if (cameraId ==1) mCameraDevice.setDisplayOrientation(270);
	    mCameraDevice.setPreviewCallback(this);
	    if (isStopping) return;
	    try {
		mCameraDevice.setPreviewDisplay(mainHolder);
		mCameraDevice.startPreview();
	    } catch (Throwable ex) {
		loge("startPreview failed!!!");
		mCameraDevice.release();
		//throw new RuntimeException("startPreview failed", ex);
	    }
	    hasOpenedCamera = true;
	}
    }

    private void stopPreview(){
	mCameraDevice.setPreviewCallback(null);
	mCameraDevice.stopPreview();
    }

    public void takePicture(){
	if (mCameraDevice == null || !hasOpenedCamera || isStopping) return;
	takePictureSuccess = false;
	//int mJpegRotation = CameraUtil.getJpegRotation(cameraId, mOrientation);
        //mParameters.setRotation(mJpegRotation);
	mParameters.setRotation(getDefaultAngle());
        mCameraDevice.setParameters(mParameters);
	mDateTaken = System.currentTimeMillis();
	try {
	    mCameraDevice.takePicture(null, null, null, new JpegPictureCallback());
	}catch (RuntimeException e) {
	    Log.e(TAG,"fail to capture!!", e);
	    showToast(R.string.take_picture_fail);
	    sendTakeResult(false, null, null);
	    reopenCamera(cameraId);
	}
    }

    public void reopenCamera(int id){
	if (hasOpenedCamera && mCameraDevice != null) {
	    stopPreview();
	    mCameraDevice.release();
            mCameraDevice = null;
	    hasOpenedCamera = false;
        }

	int pendingId = id;
	if (pendingId == DEFAULT_SWITCH){
	    pendingId = (cameraId + 1)%2;
	}
	
	if (openCamera(pendingId)){
	    hasOpenedCamera = true;
	}else{
	    sendExitResponse();
	    stopService();
	}
    }

    public void hidePreviewWindow(){
	closeCamera();
	if (mWindow != null && mWindow.hasWindow()){
	    mWindow.closePanel();
	}
	mWindow = null;
	if (!openCamera(cameraId)){
	    sendExitResponse();
	    stopService();
	} else {
	    initWindow();
	}
    }

    private final class JpegPictureCallback implements PictureCallback {
        @Override
        public void onPictureTaken(
                final byte [] jpegData, final android.hardware.Camera camera) {
	    if (isStopping) return;
	    
	    takePictureSuccess = CameraUtil.saveJpegPicture(mContext, 
							    jpegData, 
							    mDateTaken, 
							    mParameters.getPictureSize().width,
							    mParameters.getPictureSize().height);

	    byte[] sendData = CameraUtil.compressJpegSize(jpegData, sendPictureWidth, sendPictureWidth * 3 / 4);

	    logi("the picture which will be send is " + sendData.length / 1024 + "KB, " + sendPictureWidth + "x" + sendPictureWidth * 3 / 4);

	    sendTakeResult(takePictureSuccess, sendData, mPath);
	    startPreview();
	}
    }

    public void closeCamera(){
	if (mCameraDevice != null) {
	    isStopping = true;
	    stopPreview();
	    mainHolder = null;
	    mCameraDevice.release();
            mCameraDevice = null;
        }

	hasOpenedCamera = false;
    }

    private byte[] compressToJpeg(byte[] data){
	YuvImage yuvImage = new YuvImage(data, ImageFormat.NV21, 
					 mParameters.getPreviewSize().width,
					 mParameters.getPreviewSize().height, 
					 null);
	ByteArrayOutputStream mByteArrayOutputStream = new ByteArrayOutputStream();
	yuvImage.compressToJpeg(new Rect(0, 
					 0, 
					 mParameters.getPreviewSize().width, 
					 mParameters.getPreviewSize().height), 
				80, 
				mByteArrayOutputStream);

	return CameraUtil.compressJpegSize(mByteArrayOutputStream.toByteArray(), sendPictureWidth, sendPictureWidth);
    }

    //PreviewCallback interface.
    @SuppressWarnings("deprecation")
	@Override
        public void onPreviewFrame(byte[] data, Camera camera){
	dropCount++;
	dropCount = dropCount % (mParameters.getPreviewFrameRate() / expectFrameRate);

	if(dropCount == 0) {
	    if (hasConnectSuccess){
		byte[] newData = compressToJpeg(data);
		logi("onPreviewFrame: the frame size is " + newData.length / 1024 +"KB");

		Projo frame = new DefaultProjo(EnumSet.of(CameraColumn.previewData), ProjoType.DATA);
		frame.put(CameraColumn.previewData, newData);

		ArrayList<Projo> mDataBox = new ArrayList<Projo>(1);
		mDataBox.add(frame);
		logi("send a frame to watch side------->start");
		DefaultSyncManager.getDefault().requestChannel(new Config(CameraModule.CAMERA), mDataBox, PREVIEW_UUID);
		logi("send <---------------------------------end");
	    }
	}
    }

    //Send messages to watch side.
    private ArrayList<Projo> getServerProjos(int title){
	ArrayList<Projo> datas = new ArrayList<Projo>(2);
	Projo projoTitle = new DefaultProjo(EnumSet.of(CameraColumn.phoneResponseState), ProjoType.DATA);
	projoTitle.put(CameraColumn.phoneResponseState, title);
	datas.add(projoTitle);
	return datas;
    }

    private ArrayList<Projo> getServerProjos(int title, int count){
	ArrayList<Projo> datas = new ArrayList<Projo>(count);
	Projo projoTitle = new DefaultProjo(EnumSet.of(CameraColumn.phoneResponseState), ProjoType.DATA);
	projoTitle.put(CameraColumn.phoneResponseState, title);
	datas.add(projoTitle);
	return datas;
    }

    private void sendDataToClient(ArrayList<Projo> datas, Projo projoContent){
	logi("send response to watch---------------------------------" + ((Integer) datas.get(0).get(CameraColumn.phoneResponseState)).intValue());
	if (projoContent != null) datas.add(projoContent);
	Config config = new Config(CameraModule.CAMERA);
        DefaultSyncManager.getDefault().request(config, datas);
    }

    public void sendOpenResult(int mode){
		ArrayList<Projo> datas = getServerProjos(CameraTransaction.OPEN_RESULT_RESPONSE);
		Projo projoContent = new DefaultProjo(EnumSet.of(CameraColumn.openCameraResult), ProjoType.DATA);
		projoContent.put(CameraColumn.openCameraResult, mode);
		sendDataToClient(datas,projoContent);
    }

    private void sendTakeResult(boolean r, byte[] picture, String path){
	if (r){
	    ArrayList<Projo> datas = getServerProjos(CameraTransaction.TAKE_RESULT_RESPONSE, 4);
	    //add "success"
	    Projo projoRet = new DefaultProjo(EnumSet.of(CameraColumn.takePictureResult), ProjoType.DATA);
	    projoRet.put(CameraColumn.takePictureResult, r);
	    datas.add(projoRet);

	    //add picture data
	    Projo picData = new DefaultProjo(EnumSet.of(CameraColumn.pictureData), ProjoType.DATA);
	    picData.put(CameraColumn.pictureData, picture);
	    datas.add(picData);

	    //add path
	    Projo projoPath = new DefaultProjo(EnumSet.of(CameraColumn.picturePath), ProjoType.DATA);
	    projoPath.put(CameraColumn.picturePath, path);
	    sendDataToClient(datas,projoPath);
	}else{
	    ArrayList<Projo> datas = getServerProjos(CameraTransaction.TAKE_RESULT_RESPONSE);
	    Projo projoRet = new DefaultProjo(EnumSet.of(CameraColumn.takePictureResult), ProjoType.DATA);
	    projoRet.put(CameraColumn.takePictureResult, r);
	    //send "fail".
	    sendDataToClient(datas,projoRet);
	}
    }

    private void sendExitResponse(){
	ArrayList<Projo> datas = getServerProjos(CameraTransaction.EXIT_CAMERA_RESPONSE);
	Projo projoContent = new DefaultProjo(EnumSet.of(CameraColumn.exit), ProjoType.DATA);
	projoContent.put(CameraColumn.exit, "exit");
	sendDataToClient(datas,projoContent);
    }

    @SuppressLint("HandlerLeak") public class PreviewWindow  implements ControlPanel.Listener{
	private WindowManager.LayoutParams mParams;
	private View mPreviewPanel;
	private SurfaceView mPreviewView;

	private ControlPanel mController;
	private boolean mPanelVisble;
	private double mScale = (double)INIT_WIDTH / INIT_HEIGHT;

	private static final String ACTION_FROM="receiveCloseMessage";

	private int mLastScreenWidth = 0;
	
        public PreviewWindow() {
        }

	private Handler mWindowHandler = new Handler(){  
		@Override public void
		    handleMessage(Message msg) {
		    switch (msg.what) {
		    case EVENT_INIT_WINDOW:
			init();
			break;
		    case EVENT_SCREEN_OFF:
			onClosePreviewWindow();
			break;
		    case DISPLAY_WINDOW:
			displayWindow((Boolean)msg.obj);
			break;
		    default:
			logi("no message in client thread");
		    }
		}
	    };

	public Handler getWindowHandler(){
	    return mWindowHandler;
	}

	private BroadcastReceiver mBcr = new BroadcastReceiver() {        
		@Override        
		    public void onReceive(Context context, Intent intent) {            
		    // TODO Auto-generated method stub   
		    String action = intent.getAction();            
		    if(action.equals(Intent.ACTION_SCREEN_OFF)){                
			Log.v(TAG,"receive Intent.ACTION_SCREEN_OFF  broadcast from system!");            
			PreviewWindow.this.getWindowHandler().sendEmptyMessage(EVENT_SCREEN_OFF);
		    } 
		}    
	    };
	
	public void displayWindow(boolean d){
	    if (mPreviewPanel != null){
		mPanelVisble = d;
		mPreviewPanel.setVisibility(mPanelVisble ? View.VISIBLE : View.INVISIBLE);
	    }
	}

	public void sendDisplayWindowMessage(boolean d){
	    Message msg = mWindow.getWindowHandler().obtainMessage(DISPLAY_WINDOW);
	    msg.obj = d;
	    mWindow.getWindowHandler().sendMessage(msg);
	}

	@SuppressWarnings("deprecation")
	private void init(){
	    
	    IntentFilter inf= new IntentFilter();        
	    inf.addAction(ACTION_FROM);
	    inf.addAction(Intent.ACTION_SCREEN_OFF);
	    registerReceiver(mBcr,inf);
	    
	    mPreviewPanel = LayoutInflater.from(mContext).inflate(R.layout.preview_window, null);
	    mPanelVisble = CameraModule.getWindowSetting();
	    displayWindow(mPanelVisble);
	    mPreviewView = (SurfaceView)mPreviewPanel.findViewById(R.id.surface_view);
	    SurfaceHolder holder = mPreviewView.getHolder();
	    holder.addCallback(CameraWindowService.this);
	    holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	    
	    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB)
	    	mPreviewPanel.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
		    @Override
			public void onLayoutChange(View v, int left, int top, int right, int bottom, 
						   int oldLeft, int oldTop, int oldRight,int oldBottom) {
		    	int sw = mWinManager.getDefaultDisplay().getWidth();
		    	int sh = mWinManager.getDefaultDisplay().getHeight();
		    	if(mLastScreenWidth != sw){
			    //make sure the whole video is visible after orientation changed
			    mLastScreenWidth = sw;
			    if(mParams.width > sw){
				mParams.width = sw;
				mParams.height = (int)(sw / mScale);
			    }
			    if(mParams.height > sh){
				mParams.height = sh;
				mParams.width = (int)(sh * mScale);
			    }
			    if(mParams.x < 0) mParams.x = 0;
			    if(mParams.y < 0) mParams.y = 0;
			    if(mParams.x > (sw-mParams.width)) mParams.x = sw - mParams.width;
			    if(mParams.y > (sh-mParams.height)) mParams.y = sh - mParams.height;
			    if(mPanelVisble) mWinManager.updateViewLayout(mPreviewPanel, mParams);
			}
		    }
		});
	    
	    loadTheme();
	    initFloat();
	    View rootView = (View) mPreviewPanel.findViewById(R.id.root);
	    mController = new ControlPanel(getBaseContext());
	    
	    ((ViewGroup)rootView).addView(mController.getView());
	    mController.setListener(this);
	}
	
	@SuppressWarnings("deprecation")
	private void initFloat() {
	    
	    mLastScreenWidth = mWinManager.getDefaultDisplay().getWidth();
	    
	    //init params
	    mParams = new WindowManager.LayoutParams();
	    mParams.type = WindowManager.LayoutParams.TYPE_PHONE;
	    mParams.format = 1;
	    mParams.flags |= WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
	    mParams.flags |= WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS;   
	    mParams.width = INIT_WIDTH;
	    mParams.height = INIT_HEIGHT;
	    mParams.gravity = Gravity.CENTER;
	    
	    openPanel();
	}
        
	private void loadTheme(){
	    final View header = mPreviewPanel.findViewById(R.id.lay);
	    header.setOnTouchListener(new OnTouchListener(){
		    float startX;
		    float startY;
		    int originX, originY;

		    long downTime, upTime;
		    long pDownTime, pUpTime;
		    float difY,difX,pDifX,pDifY;
		    int mode = NONE;
		    @SuppressWarnings("deprecation")
			public boolean onTouch(View view, MotionEvent event) {	
			switch(event.getAction() & MotionEvent.ACTION_MASK){
			case MotionEvent.ACTION_DOWN:
			    downTime = System.currentTimeMillis();
			    mode = DRAG;
			    mParams.packageName = "mainlittlewindow";
			    if(mPanelVisble)
				mWinManager.updateViewLayout(mPreviewPanel, mParams);
			    mParams.packageName = "littlewindow";
			    
			    startX = event.getRawX();
			    startY = event.getRawY();

			    originX = mParams.x;
			    originY = mParams.y;
			    break;
			case MotionEvent.ACTION_POINTER_DOWN:
			    pDownTime = System.currentTimeMillis();
			    mode = ZOOM;
			    difX = Math.abs(event.getX(0)-event.getX(1));
			    difY = Math.abs(event.getY(0)-event.getY(1));
			    pDifX = difX;
			    pDifY = difY;
			    
			    break;
			case MotionEvent.ACTION_MOVE:
			    System.currentTimeMillis();
			    if(mode == ZOOM){
				WindowManager.LayoutParams params =(WindowManager.LayoutParams)header.getLayoutParams();
				float X = Math.abs(event.getX(0)-event.getX(1));
				float Y = Math.abs(event.getY(0)-event.getY(1));
				if(Math.abs(Y-difY) >= 10 || Math.abs(X-difX) >= 10){
				    
				    if((Y-difY)>(X-difX)){
					params.width += (Y-difY)*mScale;
					params.height += Y-difY;
				    }else{
					params.width += X-difX;
					params.height += (X-difX)/mScale;
				    }
				    if((params.height <= MIN_HEIGHT)){
					params.width = (int)(MIN_HEIGHT*mScale);
					params.height = MIN_HEIGHT;
				    }
				    
				    //set windowsize as max videosize
				    int sw = mWinManager.getDefaultDisplay().getWidth();
				    int sh = mWinManager.getDefaultDisplay().getHeight();
				    if(params.width > sw){
					params.width = sw;
					params.height = (int)(sw/mScale);
				    }	    
				    if(params.height > sh){
					params.height = sh;
					params.width = (int)(sh*mScale);
				    }
				    
				    difX = X;
				    difY = Y;
				    header.setLayoutParams(params);
				    if(mPanelVisble)
					mWinManager.updateViewLayout(mPreviewPanel, mParams);
				}else{
				    if(mPanelVisble)
					mWinManager.updateViewLayout(mPreviewPanel, mParams);
				}
			    }else if(mode == DRAG){
				if( Math.abs(event.getRawX() - startX) > 9 || Math.abs(event.getRawY() - startY) > 9)
				    updateLocation(mPreviewPanel,event.getRawX() - startX,event.getRawY() - startY);
			    }
			    break;
			case MotionEvent.ACTION_UP:
			    if(mode == NONE)
				break;
			    upTime = System.currentTimeMillis();
			    
			    float diffX = event.getRawX()-startX;
			    float diffY = event.getRawY()-startY;
			    
			    int sw = mWinManager.getDefaultDisplay().getWidth();
			    int sh =mWinManager.getDefaultDisplay().getHeight();
			    
			    int leftX = 0 - mParams.width/2;
			    int rightX = sw - mParams.width/2;
			    int upY = 0 - mParams.height/2;
			    int bottomY = sh - mParams.height/2;
			    
			    if((diffX>-9 && diffX<9)&&(diffY>-9 && diffY<9)){
				//changetoClick();	
			    }
			    else{
				if(upTime - downTime > 200l)
				    updateLocation(mPreviewPanel,diffX,diffY);
			    }
			    if(mParams.x < leftX || mParams.x > rightX || mParams.y < upY || mParams.y > bottomY){
				try{
				    if(mParams.x < leftX)mParams.x=leftX;
				    if(mParams.x > rightX)mParams.x = rightX;
				    if(mParams.y < upY)mParams.y = upY;
				    if(mParams.y > bottomY)mParams.y = bottomY;
				    mWinManager.updateViewLayout(mPreviewPanel, mParams);
				} catch (Exception e) {  
				    e.printStackTrace();  
				}  
			    }
			    break;
			case MotionEvent.ACTION_POINTER_UP:
			    mode = NONE;
			    pUpTime = System.currentTimeMillis();
			    long difTime = pUpTime - pDownTime;
			    float UX = Math.abs(event.getX(0)-event.getX(1));
			    float UY = Math.abs(event.getY(0)-event.getY(1));
			    if(Math.abs(UY-pDifY) < 10 && Math.abs(UX-pDifX) < 10 && difTime < 1000l){
				// if(mParams.alpha > 0.7f){
				// 	mParams.alpha = 0.4f;
				// 	mPreviewPanel.setmWindowAlpha(0.5f);
				// }else{
				// 	mParams.alpha = 0.9f;
				// 	mPreviewPanel.setmWindowAlpha(1.0f);
				// }
			    }
			    mWinManager.updateViewLayout(mPreviewPanel, mParams);
			    break;
			    
			}
			return true;
		    }
		    
			private void updateLocation(View view,float x, float y){
			try{
			    mParams.x = (int)x + originX;
			    mParams.y = (int)y + originY;
			    if(mPanelVisble) 
				mWinManager.updateViewLayout(mPreviewPanel, mParams);
			} catch (Exception e) {  
			    e.printStackTrace();  
			}  
			
		    }
		    
		});
	    
	}
	
	private void openPanel(){
	    logi("openPanel---------------------------------");
	    if(mWinManager != null && mPreviewPanel != null && mParams != null){
		try {
		    mWinManager.addView(mPreviewPanel, mParams);
		} catch (Exception e) {
		    e.printStackTrace();
		}
	    }
	}
	
	public void closePanel(){
	    if(mWinManager != null && mPreviewPanel != null){
		logi("close preview window");
		try {
		    mWinManager.removeView(mPreviewPanel);
		    mPreviewPanel = null;
		    unregisterReceiver(mBcr);
		} catch (Exception e) {
		    loge("closePanel Exception");
		    e.printStackTrace();
		}
	    }
	}
	
	public boolean hasWindow(){
	    return mPreviewPanel != null;
	}

	public Context getContext(){
	    return mContext;
	}

	public void onClosePreviewWindow(){
	    logi("onClosePreviewWindow");
	    
	    Message msg = mServiceHandler.obtainMessage(WINDOW_EVENT_CLOSE);
	    mServiceHandler.sendMessageDelayed(msg, 0);
	}
	
	public void onClosePreviewWindowNoNotify(){
	    logi("onClosePreviewWindowNoNotify");
	    
	    closePanel();
	    mServiceHandler.sendMessage(mServiceHandler.obtainMessage(CLOSE_SERVICE_NO_NOTIFY));
	}
    } 
}
