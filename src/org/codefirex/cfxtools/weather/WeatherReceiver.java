
package org.codefirex.cfxtools.weather;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/*
 * this receiver is for direct queries from user pref activity or other places
 * we'll keep this protected to prevent spam
 */

public class WeatherReceiver extends BroadcastReceiver {
    static final String ACTION = "cfx_query_weather_service";
    static final String QUERY_ENABLED = "cfx_query_weather_service_enabled";
    static final String ENABLED_CHANGED = "cfx_weather_notify_enabled_changed";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getStringExtra(ENABLED_CHANGED) != null) {
            if (WeatherPrefs.getEnabled(context)) {
                context.startService(new Intent(context, WeatherLocation.class));
                notifyServiceState(context, true);
            } else {
                context.stopService(new Intent(context, WeatherLocation.class));
                notifyServiceState(context, false);
            }
        } else if (intent.getStringExtra(QUERY_ENABLED) != null) {
            notifyServiceState(context, WeatherPrefs.getEnabled(context));
        }
    }

    private void notifyServiceState(Context ctx, boolean enabled) {
        ctx.sendBroadcast(new Intent().setAction(WeatherService.WEATHER_ACTION)
                .putExtra(WeatherService.WEATHER_SERVICE_STATE, String.valueOf(enabled)));
    }
}
