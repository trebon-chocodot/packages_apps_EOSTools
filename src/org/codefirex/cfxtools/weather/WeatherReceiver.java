package org.codefirex.cfxtools.weather;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

/*
 * this receiver is for direct queries from user pref activity or other places
 * we'll keep this protected to prevent spam
 */

public class WeatherReceiver extends BroadcastReceiver {
	static final String TAG = "WeatherReceiver";
	static final String ACTION = "cfx_query_weather_service";
	static final String QUERY_ENABLED = "cfx_query_weather_service_enabled";
	static final String ENABLED_CHANGED = "cfx_weather_notify_enabled_changed";

	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		if (action.equals(Intent.ACTION_BOOT_COMPLETED)) {
			// not enabled, do nothing on boot
			if (!WeatherPrefs.getEnabled(context))
				return;

			// set flag so connectivity action knows to refresh
			WeatherPrefs.setNeedsUpdate(context, true);
			Log.i(TAG, "Boot received, setting connectivity flag");
			return;
		} else if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
			// service disabled, bail
			if (!WeatherPrefs.getEnabled(context))
				return;

			// service enabled, but we're still fresh, bail
			if (!WeatherPrefs.getNeedsUpdate(context))
				return;

			ConnectivityManager connectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo networkInfo = connectivityManager
					.getActiveNetworkInfo();

			boolean isConnected = (networkInfo != null && networkInfo
					.isConnected());

			if (isConnected) {
				// we owe the user an update, start service immediately
				WeatherPrefs.setNeedsUpdate(context, false);
				startService(context);
				Log.i(TAG, "Connectivity established, starting service");
			}
			return;
		} else if (action.equals(ACTION)) {
			if (intent.getStringExtra(ENABLED_CHANGED) != null) {
				if (WeatherPrefs.getEnabled(context)) {
					startService(context);
					notifyServiceState(context, true);
				} else {
					stopService(context);
					notifyServiceState(context, false);
				}
			} else if (intent.getStringExtra(QUERY_ENABLED) != null) {
				notifyServiceState(context, WeatherPrefs.getEnabled(context));
			}
			return;
		}
	}

	private void notifyServiceState(Context ctx, boolean enabled) {
		ctx.sendBroadcast(new Intent().setAction(WeatherService.WEATHER_ACTION)
				.putExtra(WeatherService.WEATHER_SERVICE_STATE,
						String.valueOf(enabled)));
	}

	private void startService(Context ctx) {
		ctx.startService(new Intent(ctx, WeatherLocation.class));
	}

	private void stopService(Context ctx) {
		ctx.stopService(new Intent(ctx, WeatherLocation.class));
	}
}
