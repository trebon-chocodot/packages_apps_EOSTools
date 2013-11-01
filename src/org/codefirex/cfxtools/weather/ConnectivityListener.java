
package org.codefirex.cfxtools.weather;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class ConnectivityListener extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // service disabled, bail
        if (!WeatherPrefs.getEnabled(context))
            return;

        // service enabled, but we're still fresh, bail
        if (!WeatherPrefs.getNeedsUpdate(context))
            return;

        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        boolean isConnected = (networkInfo != null && networkInfo.isConnected());

        if (isConnected) {
            // we owe the user an update, start service immediately
            WeatherPrefs.setNeedsUpdate(context, false);
            context.startService(new Intent(context, WeatherLocation.class));
        }
    }
}
