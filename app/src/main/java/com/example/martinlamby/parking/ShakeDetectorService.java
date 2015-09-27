package com.example.martinlamby.parking;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.CountDownTimer;
import android.os.IBinder;

//TODO: SECOND INSPECTION DONE HEAD COMMENT IS MISSING

public class ShakeDetectorService extends Service {

    private static final int SHAKE_INTERVAL = 1000;
    private CountDownTimer shakeIntervalSeperaterTimer;
    private boolean shakeAlreadyHappened;
    private SensorManager mSensorManager;
    private float mAccel;
    private float mAccelCurrent;
    private float mAccelLast;

    @Override
    public void onCreate() {
        super.onCreate();

        //init components for shake detection
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensorManager.registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_UI);
        mAccel = (float) 0.00;
        mAccelCurrent = SensorManager.GRAVITY_EARTH;
        mAccelLast = SensorManager.GRAVITY_EARTH;
    }

    private final SensorEventListener mSensorListener = new SensorEventListener() {

        public void onSensorChanged(SensorEvent event) {
            //calculate normalized acceleration
            float xAcc = event.values[0];
            float yAcc = event.values[1];
            float zAcc = event.values[2];
            mAccelLast = mAccelCurrent;
            mAccelCurrent = (float) Math.sqrt((double) (xAcc * xAcc + yAcc * yAcc + zAcc * zAcc));
            float delta = mAccelCurrent - mAccelLast;
            mAccel = mAccel * (float) 0.9 + delta;


            shakeSeparationTimer();

            if (shakeAlreadyHappened == false && mAccel > 12) {
                onShakeEvent();
            }

        }

        //called when shake is detected
        public void onShakeEvent(){
            shakeIntervalSeperaterTimer.start();
            shakeAlreadyHappened = true;

            double latitude = GeoLocationService.getLastLocationLatitude();
            double longitude = GeoLocationService.getLastLocationLongitude();
            if(latitude == GeoLocationService.ERROR_LOCATION_VALUE | longitude == GeoLocationService.ERROR_LOCATION_VALUE){
                SignUpActivity.showErrorToast(getApplicationContext(),getString(R.string.location_not_found));
            }else {
                ParseController.saveParkedCarPositionToParse(latitude, longitude);
                SignUpActivity.showErrorToast(getApplicationContext(), getString(R.string.position_saved));
            }
        }

        //sets the minimum time required to pass between to shakes
        public void shakeSeparationTimer(){
            shakeIntervalSeperaterTimer = new CountDownTimer(SHAKE_INTERVAL, SHAKE_INTERVAL) {
                @Override
                public void onTick(long millisUntilFinished) {
                }

                @Override
                public void onFinish() {
                    shakeAlreadyHappened = false;
                }
            };
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        System.out.println("ShakeDetectorService started");
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        System.out.println("ShakeDetectorService stopped");
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not implemented");
    }

}
