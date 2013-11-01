
package org.codefirex.cfxtools.weather;

import android.annotation.TargetApi;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;

@TargetApi(Build.VERSION_CODES.CUPCAKE)
public class WeatherService extends IntentService {
    private static final String TAG = "WeatherService";

    public static final String WEATHER_ACTION = "cfx_weather_update";
    public static final String WEATHER_EXTRA = "weather";
    public static final String WEATHER_SERVICE_STATE = "cfx_weather_service_state";

    private YahooWeatherUtils yahooWeatherUtils = YahooWeatherUtils.getInstance();

    public WeatherService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (!NetworkUtils.isConnected(this)) {
            // we are enabled, but don't have connectivity
            // set a flag to let ConnListener know to act
            // when connectivity comes back
            WeatherPrefs.setNeedsUpdate(this, true);
            return;
        }

        // see if service started from WeatherLocation
        // we have fresh incoming coordinates or location fail

        String action = intent.getAction();

        if(WeatherLocation.LOCATION_ACQUIRED_ACTION.equals(action)){
            Location location = (Location) intent
                    .getParcelableExtra(WeatherLocation.LOCATION_EXTRA);
            // shouldn't be null here
            if (location != null) {
                String lat = String.valueOf(location.getLatitude());
                String lon = String.valueOf(location.getLongitude());
                WeatherInfo weatherInfo = yahooWeatherUtils.queryYahooWeather(this, lat, lon);
                sendWeatherBroadcast(this,weatherInfo);
            } else {
                handleLocationServiceFail();
            }
        } else if(WeatherLocation.LOCATION_UNAVAILABLE_ACTION.equals(action)){
            handleLocationServiceFail();
        }
    }

    private void sendWeatherBroadcast(Context context, WeatherInfo weatherInfo){
        if (weatherInfo == null)
            return;
        Bundle b = WeatherUtils.makeWeatherBundle(weatherInfo,
                WeatherPrefs.getDegreeType(this) == WeatherPrefs.DEGREE_F);

        WeatherPrefs.setLatestWeatherBundle(this, b);
        Intent weatherIntent = new Intent();
        weatherIntent.setAction(WEATHER_ACTION);
        weatherIntent.putExtra(WEATHER_EXTRA, b);
        context.sendStickyBroadcast(weatherIntent);
    }

    private void handleLocationServiceFail() {
        Intent weatherIntent = new Intent();
        weatherIntent.setAction(WEATHER_ACTION);
        weatherIntent.putExtra(WEATHER_EXTRA, WeatherPrefs.getLatestWeatherBundle(this));
        sendStickyBroadcast(weatherIntent);
    }
}
