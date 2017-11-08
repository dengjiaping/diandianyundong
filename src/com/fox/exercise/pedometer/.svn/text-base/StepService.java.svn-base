package com.fox.exercise.pedometer;

import cn.ingenic.indroidsync.SportsApp;

import com.fox.exercise.R;
import com.fox.exercise.map.SportingMapActivityGaode;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.preference.PreferenceManager;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

public class StepService extends Service {

    private static final int NOTIFICATION_ID = 10;

    private SharedPreferences mSettings;
    private PedometerSettings mPedometerSettings;
    private SensorManager mSensorManager;
    private StepDetector mStepDetector;
    // private StepBuzzer mStepBuzzer; // used for debugging
    private StepDisplayer mStepDisplayer;
    private PaceNotifier mPaceNotifier;
    private DistanceNotifier mDistanceNotifier;
    private SpeedNotifier mSpeedNotifier;
    private CaloriesNotifier mCaloriesNotifier;
    private SpeakingTimer mSpeakingTimer;

    private PowerManager.WakeLock wakeLock;
    private Sensor mSensor;

    public static int mSteps;
    private int mPace;
    private float mDistance;
    private float mSpeed;
    private float mCalories;
    private SharedPreferences spf;
    //private int tempSteps;
    private SportsApp mSportsApp;

    /**
     * Class for clients to access. Because we know this service always runs in
     * the same process as its clients, we don't need to deal with IPC.
     */
    public class StepBinder extends Binder {
        public StepService getService() {
            return StepService.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        // TimingManager.getInstance(getApplicationContext()).repeatTiming();
//		 IntentFilter filter = new IntentFilter();  
//		 filter.setPriority(1000);  

//		PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
//		wakeLock = pm
//				.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "StepService");
//		wakeLock.acquire();

        // Load settings
//		SharedPreferences sharedPreferences = getSharedPreferences(
//				"sprots_uid", 0);
//		int sportUid = sharedPreferences.getInt("sportsUid", 0);
//		mSettings = getSharedPreferences("sports" + sportUid, 0);
//		spf = getSharedPreferences("sports" + sportUid, 0);
//		//tempSteps = spf.getInt("mmSteps", 0);
//		mPedometerSettings = new PedometerSettings(mSettings);

        // Start detecting | Sensor.TYPE_MAGNETIC_FIELD| Sensor.TYPE_ORIENTATION
//		mStepDetector = new StepDetector();
//		mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
//		registerDetector();

//		mStepDisplayer = new StepDisplayer(mPedometerSettings);
//		mStepDisplayer.addListener(mStepListener);
//		mStepDetector.addStepListener(mStepDisplayer);
//
//		mPaceNotifier = new PaceNotifier(mPedometerSettings);
//		mPaceNotifier.addListener(mPaceListener);
//		mStepDetector.addStepListener(mPaceNotifier);
//
//		mDistanceNotifier = new DistanceNotifier(mDistanceListener,
//				mPedometerSettings);
//		mStepDetector.addStepListener(mDistanceNotifier);
//
//		mSpeedNotifier = new SpeedNotifier(mSpeedListener, mPedometerSettings);
//		mPaceNotifier.addListener(mSpeedNotifier);
//
//		mCaloriesNotifier = new CaloriesNotifier(mCaloriesListener,
//				mPedometerSettings);
//		mStepDetector.addStepListener(mCaloriesNotifier);
//		reloadSettings();
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // TODO Auto-generated method stub
        Log.i("SERVICE", "I have started");
        mSportsApp = (SportsApp) getApplication();
        Notification notification = new Notification(R.drawable.notification_icon, getText(R.string.ticker_text),
                System.currentTimeMillis());
        notification.flags |= Notification.FLAG_ONGOING_EVENT;
        notification.flags |= Notification.FLAG_NO_CLEAR;
        Intent notificationIntent = null;
        if (mSportsApp.mCurMapType == SportsApp.MAP_TYPE_BAIDU) {
//			notificationIntent = new Intent(this, SportingMapActivity.class);
        } else {
            notificationIntent = new Intent(this, SportingMapActivityGaode.class);
        }
        notificationIntent.setAction("android.intent.action.MAIN");
        notificationIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
        notification.setLatestEventInfo(this, getText(R.string.app_name),
                getText(R.string.notification_message), pendingIntent);
        startForeground(NOTIFICATION_ID, notification);
        flags = START_STICKY;
        return super.onStartCommand(intent, flags, startId);
    }

    public void save() {
        // SportUtils.saveSportDB(getApplicationContext(), mSteps);
        resetValues();
        if (mCallback != null) {
            mCallback.saveFinish();
        }
    }

    @Override
    public void onDestroy() {
        // TimingManager.getInstance(getApplicationContext()).cancleRepeatTiming();
//		unregisterDetector();
//		Editor edit = spf.edit();
//		edit.putInt("mmSteps", mSteps + tempSteps);
//		edit.commit();
//		Log.i("save", "save steps" + tempSteps);
//		wakeLock.release();
        super.onDestroy();
//		mSensorManager.unregisterListener(mStepDetector);
        stopForeground(true);
    }

//	private void registerDetector() {
//		mSensor = mSensorManager
//				.getDefaultSensor(Sensor.TYPE_ACCELEROMETER /*
//															 * | Sensor.
//															 * TYPE_MAGNETIC_FIELD
//															 * | Sensor.
//															 * TYPE_ORIENTATION
//															 */);
//		mSensorManager.registerListener(mStepDetector, mSensor,
//				SensorManager.SENSOR_DELAY_FASTEST);
//	}

//	private void unregisterDetector() {
//		mSensorManager.unregisterListener(mStepDetector);
//	}

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    /**
     * Receives messages from activity.
     */
    private final IBinder mBinder = new StepBinder();

    public interface ICallback {
        public void stepsChanged(int value);

        public void paceChanged(int value);

        public void distanceChanged(float value);

        public void speedChanged(float value);

        public void caloriesChanged(float value);

        public void saveFinish();
    }

    private ICallback mCallback;

    public void registerCallback(ICallback cb) {
        mCallback = cb;
        // mStepDisplayer.passValue();
        // mPaceListener.passValue();
    }

    private int mDesiredPace;
    private float mDesiredSpeed;

    /**
     * Called by activity to pass the desired pace value, whenever it is
     * modified by the user.
     *
     * @param desiredPace
     */
    public void setDesiredPace(int desiredPace) {
        mDesiredPace = desiredPace;
        if (mPaceNotifier != null) {
            mPaceNotifier.setDesiredPace(mDesiredPace);
        }
    }

    /**
     * Called by activity to pass the desired speed value, whenever it is
     * modified by the user.
     *
     * @param desiredSpeed
     */
    public void setDesiredSpeed(float desiredSpeed) {
        mDesiredSpeed = desiredSpeed;
        if (mSpeedNotifier != null) {
            mSpeedNotifier.setDesiredSpeed(mDesiredSpeed);
        }
    }

    public void reloadSettings() {
        // mSettings = PreferenceManager.getDefaultSharedPreferences(this);
        mSettings = getSharedPreferences("mySense_type", 0);
        if (mStepDetector != null) {
            mStepDetector.setSensitivity(Float.valueOf(mSettings.getString(
                    "sensitivity", "7.77")));
        }
        if (mStepDisplayer != null)
            mStepDisplayer.reloadSettings();
        if (mPaceNotifier != null)
            mPaceNotifier.reloadSettings();
        if (mDistanceNotifier != null)
            mDistanceNotifier.reloadSettings();
        if (mSpeedNotifier != null)
            mSpeedNotifier.reloadSettings();
        if (mCaloriesNotifier != null)
            mCaloriesNotifier.reloadSettings();
        if (mSpeakingTimer != null)
            mSpeakingTimer.reloadSettings();
    }

    public void resetValues() {
        mStepDisplayer.setSteps(0);
        mPaceNotifier.setPace(0);
        mDistanceNotifier.setDistance(0);
        mSpeedNotifier.setSpeed(0);
        mCaloriesNotifier.setCalories(0);
    }

    /**
     * Forwards pace values from PaceNotifier to the activity.
     */
//	private StepDisplayer.Listener mStepListener = new StepDisplayer.Listener() {
//		public void stepsChanged(int value) {
//			mSteps = value;
//			passValue();
//		}
//
//		public void passValue() {
//			if (mCallback != null) {
//				mCallback.stepsChanged(mSteps); //+ tempSteps);
//				// SharedPreferences sps = getSharedPreferences("sprots_uid",
//				// 0);
//				// int sportUid = sps.getInt("sportsUid", 0);
//				// SharedPreferences sharedPreferences =
//				// getSharedPreferences("sports" + sportUid, 0);
//				// boolean sportsFlag = sharedPreferences.getBoolean("isSports",
//				// true);
//				// if (sportsFlag) {
//				// TimingManager.getInstance(getApplicationContext()).cancleRepeatTimingOneHour();
//				// TimingManager.getInstance(getApplicationContext()).repeatTimingOneHour();
//				// }
//				// else {
//				// TimingManager.getInstance(getApplicationContext()).cancleRepeatTimingOneHour();
//				// }
//			}
//		}
//
//	};
    /**
     * Forwards pace values from PaceNotifier to the activity.
     */
//	private PaceNotifier.Listener mPaceListener = new PaceNotifier.Listener() {
//		public void paceChanged(int value) {
//			mPace = value;
//			passValue();
//		}
//
//		public void passValue() {
//			if (mCallback != null) {
//				mCallback.paceChanged(mPace);
//			}
//		}
//	};
    /**
     * Forwards distance values from DistanceNotifier to the activity.
     */
//	private DistanceNotifier.Listener mDistanceListener = new DistanceNotifier.Listener() {
//		public void valueChanged(float value) {
//			mDistance = value;
//			passValue();
//		}
//
//		public void passValue() {
//			if (mCallback != null) {
//				mCallback.distanceChanged(mDistance);
//			}
//		}
//	};
    /**
     * Forwards speed values from SpeedNotifier to the activity.
     */
//	private SpeedNotifier.Listener mSpeedListener = new SpeedNotifier.Listener() {
//		public void valueChanged(float value) {
//			mSpeed = value;
//			passValue();
//		}
//
//		public void passValue() {
//			if (mCallback != null) {
//				mCallback.speedChanged(mSpeed);
//			}
//		}
//	};
    /**
     * Forwards calories values from CaloriesNotifier to the activity.
     */
//	private CaloriesNotifier.Listener mCaloriesListener = new CaloriesNotifier.Listener() {
//		public void valueChanged(float value) {
//			mCalories = value;
//			passValue();
//		}
//
//		public void passValue() {
//			if (mCallback != null) {
//				mCallback.caloriesChanged(mCalories);
//			}
//		}
//	};

}
