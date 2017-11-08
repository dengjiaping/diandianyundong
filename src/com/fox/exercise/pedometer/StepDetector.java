/*
 *  Pedometer - Android App
 *  Copyright (C) 2009 Levente Bagi
 *	Download by http://www.codefans.net
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.fox.exercise.pedometer;

import java.util.ArrayList;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

/**
 * Detects steps and notifies all listeners (that implement StepListener).
 *
 * @author Levente Bagi
 * @todo REFACTOR: SensorListener is deprecated
 */
public class StepDetector implements SensorEventListener {
    private final static String TAG = "StepDetector";
    private static float mSensitiveValue = 8.5f;
    private float mLimit = 0.0f;// 1.97 2.96 4.44 6.66 10.00 15.00 22.50 33.75
    // 50.62
    private float mLastValues[] = new float[3 * 2];
    private float mScale[] = new float[2];
    private float mYOffset;

    private boolean mTroughAppear = false;
    private float mX = 0;
    private float mY = 0;
    private float mZ = 0;
    private long mPrevStepTime = 0;

    private float mLastDirections[] = new float[3 * 2];
    private float mLastExtremes[][] = {new float[3 * 2], new float[3 * 2]};
    private float mLastDiff[] = new float[3 * 2];
    private int mLastMatch = -1;

    private ArrayList<StepListener> mStepListeners = new ArrayList<StepListener>();

    public StepDetector() {
        int h = 480; // TODO: remove this constant
        mYOffset = h * 0.5f;
        mScale[0] = -(h * 0.5f * (1.0f / (SensorManager.STANDARD_GRAVITY * 2)));
        mScale[1] = -(h * 0.5f * (1.0f / (SensorManager.MAGNETIC_FIELD_EARTH_MAX)));
    }

    public void setSensitivity(float sensitivity) {
        mLimit = sensitivity; // 1.97 2.96 4.44 6.66 10.00 15.00 22.50 33.75
        // 50.62
        Log.i("mLimit", "mLimit" + mLimit);
    }

    public void addStepListener(StepListener sl) {
        mStepListeners.add(sl);
    }

    // public void onSensorChanged(int sensor, float[] values) {
    public void onSensorChanged(SensorEvent event) {
        Sensor sensor = event.sensor;
        synchronized (this) {
            if (sensor.getType() == Sensor.TYPE_ORIENTATION) {
            } else {
                stepCounter(sensor, event);
            }
        }
    }

    private Boolean accelLeft = false;

    private float oldValue = 0;

    private void stepCounter(Sensor sensor, SensorEvent event) {
        final int type = event.sensor.getType();
        if (type == Sensor.TYPE_ACCELEROMETER) {
            mX = event.values[0];
            mY = event.values[1];
            mZ = event.values[2];

            float accelerometer = (float) Math.sqrt(mX * mX + mY * mY + mZ * mZ);

            if (accelerometer > mSensitiveValue && oldValue < accelerometer) {
                accelLeft = true;
            }
            if (accelLeft) {

                if (accelerometer < mSensitiveValue && oldValue > accelerometer) {
                    if (System.currentTimeMillis() - mPrevStepTime > 600) {
                        mPrevStepTime = System.currentTimeMillis();
                        for (StepListener stepListener : mStepListeners) {
                            stepListener.onStep();
                        }
                        accelLeft = false;
                    }
                }
            }
            oldValue = accelerometer;
//			File file = new File("/sdcard/", "AccelValue.txt");
//			try {
//				// 后面这个参数代表是不是要接上文件中原来的数据，不进行覆盖
//				FileWriter filerWriter = new FileWriter(file, true);
//				BufferedWriter bufWriter = new BufferedWriter(filerWriter);
//				bufWriter.write(String.valueOf(accelerometer));
//				bufWriter.newLine();
//				bufWriter.close();
//				filerWriter.close();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
            // Log.v("stepCounter", "accelerometer:" + accelerometer);
        }

    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // TODO Auto-generated method stub
    }
}