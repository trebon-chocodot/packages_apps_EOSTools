
package org.codefirex.cfxtools.weather;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;

/*
    This class currently returns best lastKnownLocation when it is started or
    attempted to be started. Currently will remain running and provide location
    updates as they are received.
 */

public class WeatherLocation extends Service {
	private static final String TAG = "WeatherLocation";

    static final String LOCATION_ACQUIRED_ACTION = "location_acquired";
    static final String LOCATION_UNAVAILABLE_ACTION = "location_unavailable";
    static final String LOCATION_REFRESHING = "location_refreshing";
    static final String LOCATION_EXTRA = "location_extra";

    static final String INTERVAL_CHANGED = "interval_changed";
    static final String LOCATION_MODE_CHANGED = "location_mode_changed";
    static final String SCALE_CHANGED = "scale_changed";
    static final String REFRESH_NOW = "refresh_now";
    static final String ALARM_TICKED = "cfx_weather_alarm_ticked";

    LocationListener mNetworkLocationListener;
    LocationManager mLocationManager;
    BroadcastReceiver mReceiver;
    IntentFilter mFilter;
    PendingIntent mAlarmPending;

    private static final double METERS_PER_MILE = 1609.34;
    private static final float MINIMUM_DISTANCE_THRESHOLD = (float) (METERS_PER_MILE * 5);
    private static final long MINUTE_MULTIPLE = (1000 * 60);

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        getPosition();
        return Service.START_STICKY;
    }

    @Override
    public void onCreate() {
        // create pending intent to fire when alarm is triggered
        mAlarmPending = PendingIntent.getBroadcast(this, 0,
                new Intent(ALARM_TICKED),
                PendingIntent.FLAG_CANCEL_CURRENT);

        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        mNetworkLocationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                sendPosition(location);
            }

            public void onProviderDisabled(String provider) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }
        };

        mFilter = new IntentFilter();
        mFilter.addAction(WeatherReceiver.ACTION);
        mFilter.addAction(ALARM_TICKED);
        mFilter.addAction(Intent.ACTION_SCREEN_ON);
        mFilter.addAction(Intent.ACTION_SCREEN_OFF);

        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (action.equals(WeatherReceiver.ACTION)) {
                    if (intent.getStringExtra(REFRESH_NOW) != null
                            || intent.getStringExtra(SCALE_CHANGED) != null
                            || intent.getStringExtra(LOCATION_MODE_CHANGED) != null) {
                        resetLocationListener();
                    } else if (intent.getStringExtra(INTERVAL_CHANGED) != null) {
                        resetAlarm();
                        resetLocationListener();
                    }
                } else if (action.equals(ALARM_TICKED)) {
                    resetLocationListener();
                } else if (action.equals(Intent.ACTION_SCREEN_OFF)) {
                	cancelAlarm();                	
                } else if (action.equals(Intent.ACTION_SCREEN_ON)) {
                    resetAlarm();
                    resetLocationListener();
                }
            }
        };

        registerReceiver(mReceiver, mFilter);
        resetAlarm();
        resetLocationListener();
    }

    private void resetLocationListener() {
    	Intent intent = new Intent();
    	intent.setAction(LOCATION_REFRESHING);
    	sendBroadcast(intent);

        removeLocationListener();
        mLocationManager.requestLocationUpdates(WeatherPrefs.getLocationMode(this),
                0,
                0,
                mNetworkLocationListener);
    }

    private void removeLocationListener() {
        mLocationManager.removeUpdates(mNetworkLocationListener);
    }

    private void resetAlarm() {
        long interval = Long.valueOf(WeatherPrefs.getInterval(this)) * MINUTE_MULTIPLE;
        long START_TIME = Calendar.getInstance().getTimeInMillis() + interval;
        AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarm.cancel(mAlarmPending);
        alarm.setRepeating(AlarmManager.RTC_WAKEUP, START_TIME, interval, mAlarmPending);
    }

    private void cancelAlarm() {
        AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarm.cancel(mAlarmPending);
    }

    private void getPosition() {
        if (!mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                && !mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
            alert();

        String bestProvider = mLocationManager.getBestProvider(new Criteria(), true);

        Location local = mLocationManager.getLastKnownLocation(bestProvider);
        sendPosition(local);
    }

    private void alert() {
        Intent alertIntent = new Intent(this, WeatherService.class);
        alertIntent.setAction(LOCATION_UNAVAILABLE_ACTION);
        startService(alertIntent);
        removeLocationListener();
    }

    void sendPosition(Location location) {
        Intent bestPosition = new Intent(this, WeatherService.class);
        bestPosition.setAction(LOCATION_ACQUIRED_ACTION);
        bestPosition.putExtra(LOCATION_EXTRA, location);
        startService(bestPosition);
        removeLocationListener();
    }

    @Override
    public void onDestroy() {
        removeLocationListener();
        unregisterReceiver(mReceiver);
        cancelAlarm();
    }
}
