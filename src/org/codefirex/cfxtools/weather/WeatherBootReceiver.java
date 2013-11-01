
package org.codefirex.cfxtools.weather;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/*
 * refresh weather on boot if enabled
 */

public class WeatherBootReceiver extends BroadcastReceiver {
    private static final String TAG = "WeatherBootReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "Boot complete");
        boolean isEnabled = WeatherPrefs.getEnabled(context);

        // not enabled, do nothing on boot
        if (!isEnabled)
            return;

        // let ConnectivityListener know we are due for an update
        // when we get connectivity
        WeatherPrefs.setNeedsUpdate(context, true);
    }
}
