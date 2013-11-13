
package org.codefirex.cfxtools.weather;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.os.Bundle;

public class WeatherPrefs {
    private static final String WEATHER_PREFS = "weather_prefs";
    private static final String INTERVAL = "interval";
    private static final String ENABLED = "enabled";
    private static final String NEEDS_UPDATE_FROM_CONN_FAIL = "needs_update_from_conn_fail";
    private static final String LOCATION_MODE = "location_mode";

    private static final String DEF_INTERVAL = "15";
    private static final String DEGREE_TYPE = "degree_type";

    static final int DEGREE_F = 1;
    static final int DEGREE_C = 2;

    private static SharedPreferences getPrefs(Context ctx) {
        return ctx.getSharedPreferences(WEATHER_PREFS, Context.MODE_PRIVATE);
    }

    private static SharedPreferences.Editor getEdit(Context ctx) {
        return getPrefs(ctx).edit();
    }

    static boolean getEnabled(Context ctx) {
        return getPrefs(ctx).getBoolean(ENABLED, false);
    }

    static void setEnabled(Context ctx, boolean enabled) {
        getEdit(ctx).putBoolean(ENABLED, enabled).commit();
    }

    static boolean getNeedsUpdate(Context ctx) {
        return getPrefs(ctx).getBoolean(NEEDS_UPDATE_FROM_CONN_FAIL, false);
    }

    static void setNeedsUpdate(Context ctx, boolean enabled) {
        getEdit(ctx).putBoolean(NEEDS_UPDATE_FROM_CONN_FAIL, enabled).commit();
    }

    static String getInterval(Context ctx) {
        return getPrefs(ctx).getString(INTERVAL, DEF_INTERVAL);
    }

    static void setInterval(Context ctx, String interval) {
        getEdit(ctx).putString(INTERVAL, interval).commit();
    }

    static String getLocationMode(Context ctx) {
        return getPrefs(ctx).getString(LOCATION_MODE, LocationManager.FUSED_PROVIDER);
    }

    static void setLocationMode(Context ctx, String mode) {
        getEdit(ctx).putString(LOCATION_MODE, mode).commit();
    }

    static int getDegreeType(Context ctx) {
        return getPrefs(ctx).getInt(DEGREE_TYPE, DEGREE_F);
    }

    static void setDegreeType(Context ctx, int type) {
        getEdit(ctx).putInt(DEGREE_TYPE, type).commit();
    }
    
    static void setLatestWeatherBundle(Context ctx, Bundle b) {
        getEdit(ctx).putString("title", b.getString("title"))
        .putString("city", b.getString("city"))
        .putString("country", b.getString("country"))
        .putString("date", b.getString("date"))
        .putString("weather", b.getString("weather"))
        .putString("temp", b.getString("temp"))
        .putString("chill", b.getString("chill"))
        .putString("direction", b.getString("direction"))
        .putString("speed", b.getString("speed"))
        .putString("humidity", b.getString("humidity"))
        .putString("pressure", b.getString("pressure"))
        .putString("visibility", b.getString("visibility"))
        .putString("current_code", b.getString("current_code"))
        .putString("current_url", b.getString("current_url"))

        .putString("f1_day", b.getString("f1_day"))
        .putString("f1_date", b.getString("f1_date"))
        .putString("f1_weather", b.getString("f1_weather"))
        .putString("f1_temp_low", b.getString("f1_temp_low"))
        .putString("f1_temp_high", b.getString("f1_temp_high"))
        .putString("f1_current_code", b.getString("f1_current_code"))
        .putString("f1_url", b.getString("f1_url"))

        .putString("f2_day", b.getString("f2_day"))
        .putString("f2_date", b.getString("f2_date"))
        .putString("f2_weather", b.getString("f2_weather"))
        .putString("f2_temp_low", b.getString("f2_temp_low"))
        .putString("f2_temp_high", b.getString("f2_temp_high"))
        .putString("f2_current_code", b.getString("f2_current_code"))
        .putString("f2_url", b.getString("f2_url"))

        .putString("f3_day", b.getString("f3_day"))
        .putString("f3_date", b.getString("f3_date"))
        .putString("f3_weather", b.getString("f3_weather"))
        .putString("f3_temp_low", b.getString("f3_temp_low"))
        .putString("f3_temp_high", b.getString("f3_temp_high"))
        .putString("f3_current_code", b.getString("f3_current_code"))
        .putString("f3_url", b.getString("f3_url"))

        .putString("f4_day", b.getString("f4_day"))
        .putString("f4_date", b.getString("f4_date"))
        .putString("f4_weather", b.getString("f4_weather"))
        .putString("f4_temp_low", b.getString("f4_temp_low"))
        .putString("f4_temp_high", b.getString("f4_temp_high"))
        .putString("f4_current_code", b.getString("f4_current_code"))
        .putString("f4_url", b.getString("f4_url"))

        .commit();        
    }
    
    static Bundle getLatestWeatherBundle(Context ctx) {
        Bundle b = new Bundle();
        b.putString("title", getPrefs(ctx).getString("title", "unknown"));
        b.putString("city", getPrefs(ctx).getString("city", "unknown"));
        b.putString("country", getPrefs(ctx).getString("country", "unknown"));
        b.putString("date", getPrefs(ctx).getString("date", "unknown"));
        b.putString("weather", getPrefs(ctx).getString("weather", "unknown"));
        b.putString("temp", getPrefs(ctx).getString("temp", "unknown"));
        b.putString("chill", getPrefs(ctx).getString("chill",  "unknown"));
        b.putString("direction", getPrefs(ctx).getString("direction",  "unknown"));
        b.putString("speed", getPrefs(ctx).getString("speed", "unknown"));
        b.putString("humidity", getPrefs(ctx).getString("humidity", "unknown"));
        b.putString("pressure", getPrefs(ctx).getString("pressure", "unknown"));
        b.putString("visibility", getPrefs(ctx).getString("visibility", "unknown"));
        b.putString("current_code", getPrefs(ctx).getString("current_code", "-1"));
        b.putString("current_url", getPrefs(ctx).getString("current_url", "unknown"));

        b.putString("f1_day",getPrefs(ctx).getString("f1_day", "unknown"));
        b.putString("f1_date",getPrefs(ctx).getString("f1_date", "unknown"));
        b.putString("f1_weather",getPrefs(ctx).getString("f1_weather","unknown"));
        b.putString("f1_temp_low",getPrefs(ctx).getString("f1_temp_low", "unknown"));
        b.putString("f1_temp_high",getPrefs(ctx).getString("f1_temp_high", "unknown"));
        b.putString("f1_current_code", getPrefs(ctx).getString("f1_current_code", "-1"));
        b.putString("f1_url",getPrefs(ctx).getString("f1_url",  "unknown"));

        b.putString("f2_day",getPrefs(ctx).getString("f2_day", "unknown"));
        b.putString("f2_date",getPrefs(ctx).getString("f2_date", "unknown"));
        b.putString("f2_weather",getPrefs(ctx).getString("f2_weather","unknown"));
        b.putString("f2_temp_low",getPrefs(ctx).getString("f2_temp_low", "unknown"));
        b.putString("f2_temp_high",getPrefs(ctx).getString("f2_temp_high", "unknown"));
        b.putString("f2_current_code", getPrefs(ctx).getString("f2_current_code", "-1"));
        b.putString("f2_url",getPrefs(ctx).getString("f2_url", "unknown"));

        b.putString("f3_day",getPrefs(ctx).getString("f3_day", "unknown"));
        b.putString("f3_date",getPrefs(ctx).getString("f3_date", "unknown"));
        b.putString("f3_weather",getPrefs(ctx).getString("f3_weather","unknown"));
        b.putString("f3_temp_low",getPrefs(ctx).getString("f3_temp_low", "unknown"));
        b.putString("f3_temp_high",getPrefs(ctx).getString("f3_temp_high", "unknown"));
        b.putString("f3_current_code", getPrefs(ctx).getString("f3_current_code", "-1"));
        b.putString("f3_url",getPrefs(ctx).getString("f3_url", "unknown"));

        b.putString("f4_day",getPrefs(ctx).getString("f4_day", "unknown"));
        b.putString("f4_date",getPrefs(ctx).getString("f4_date", "unknown"));
        b.putString("f4_weather",getPrefs(ctx).getString("f4_weather","unknown"));
        b.putString("f4_temp_low",getPrefs(ctx).getString("f4_temp_low", "unknown"));
        b.putString("f4_temp_high",getPrefs(ctx).getString("f4_temp_high", "unknown"));
        b.putString("f4_current_code", getPrefs(ctx).getString("f4_current_code", "-1"));
        b.putString("f4_url",getPrefs(ctx).getString("f4_url",  "unknown"));
        return b;
    }
}
