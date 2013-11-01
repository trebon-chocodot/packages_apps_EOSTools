
package org.codefirex.cfxtools.weather;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.ListPreference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;

import org.codefirex.cfxtools.R;

public class WeatherActivity extends PreferenceActivity implements
        Preference.OnPreferenceChangeListener {

    CheckBoxPreference mToggle;
    ListPreference mLocation;
    ListPreference mInterval;
    ListPreference mTempScale;
    Preference mRefresh;

    IntentFilter mWeatherFilter;
    BroadcastReceiver mWeatherReceiver;

    @SuppressWarnings("deprecation")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.weather_prefs);

        mToggle = (CheckBoxPreference) findPreference("weather_toggle_service");
        mLocation = (ListPreference) findPreference("weather_location_mode");
        mInterval = (ListPreference) findPreference("weather_interval");
        mTempScale = (ListPreference) findPreference("weather_temp_scale");
        mRefresh = findPreference("weather_refresh");

        mToggle.setChecked(WeatherPrefs.getEnabled(this));
        mLocation.setValue(String.valueOf(WeatherPrefs.getLocationMode(this)));
        mInterval.setValue(String.valueOf(WeatherPrefs.getInterval(this)));
        mTempScale.setValue(String.valueOf(WeatherPrefs.getDegreeType(this)));

        updateSummary(mLocation,
                getResources().getStringArray(R.array.weather_location_entries),
                getResources().getStringArray(R.array.weather_location_values));
        updateSummary(mInterval,
                getResources().getStringArray(R.array.weather_interval_entries),
                getResources().getStringArray(R.array.weather_interval_values));
        updateSummary(mTempScale,
                getResources().getStringArray(R.array.weather_temp_scale_entries),
                getResources().getStringArray(R.array.weather_temp_scale_values));

        mToggle.setOnPreferenceChangeListener(this);
        mLocation.setOnPreferenceChangeListener(this);
        mInterval.setOnPreferenceChangeListener(this);
        mTempScale.setOnPreferenceChangeListener(this);

        mWeatherFilter = new IntentFilter();
        mWeatherFilter.addAction(WeatherService.WEATHER_ACTION);

        mWeatherReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Bundle b = intent.getBundleExtra(WeatherService.WEATHER_EXTRA);
                updateRefreshPref(b);                
            }            
        };
    }

    @Override
    public void onStart() {
        super.onStart();
        registerReceiver(mWeatherReceiver, mWeatherFilter);    
    }

    @Override 
    public void onStop() {
        super.onStop();
        unregisterReceiver(mWeatherReceiver);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (preference.equals(mToggle)) {
            WeatherPrefs.setEnabled(this, ((Boolean) newValue).booleanValue());
            Intent intent = new Intent()
            .setAction(WeatherReceiver.ACTION)
            .putExtra(WeatherReceiver.ENABLED_CHANGED, "enabled_changed");
            sendBroadcast(intent);
            return true;
        } else if (preference.equals(mLocation)) {
            String val = ((String) newValue).toString();
            WeatherPrefs.setLocationMode(this, val);
            Intent intent = new Intent()
            .setAction(WeatherReceiver.ACTION)
            .putExtra(WeatherLocation.LOCATION_MODE_CHANGED, "interval");
            sendBroadcast(intent);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    updateSummary(mLocation,
                            getResources().getStringArray(R.array.weather_location_entries),
                            getResources().getStringArray(R.array.weather_location_values));
                }
            }, 100);
            return true;
        } else if (preference.equals(mInterval)) {
            String val = ((String) newValue).toString();
            WeatherPrefs.setInterval(this, val);
            Intent intent = new Intent()
            .setAction(WeatherReceiver.ACTION)
            .putExtra(WeatherLocation.INTERVAL_CHANGED, "interval");
            sendBroadcast(intent);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    updateSummary(mInterval,
                            getResources().getStringArray(R.array.weather_interval_entries),
                            getResources().getStringArray(R.array.weather_interval_values));
                }
            }, 100);
            return true;
        } else if (preference.equals(mTempScale)) {
            int val = Integer.parseInt(((String) newValue)
                    .toString());
            WeatherPrefs.setDegreeType(this, val);
            Intent intent = new Intent()
            .setAction(WeatherReceiver.ACTION)
            .putExtra(WeatherLocation.SCALE_CHANGED, "scale");
            sendBroadcast(intent);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    updateSummary(mTempScale,
                            getResources().getStringArray(R.array.weather_temp_scale_entries),
                            getResources().getStringArray(R.array.weather_temp_scale_values));
                }
            }, 100);
            return true;
        }
        return false;
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen,
            Preference preference) {
        if (preference.equals(mRefresh)) {
            Intent intent = new Intent();
            intent.setAction(WeatherReceiver.ACTION);
            intent.putExtra(WeatherLocation.REFRESH_NOW, "refresh");
            sendBroadcast(intent);
        }
        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }

    private void updateRefreshPref(Bundle b) {
        if (b != null) {
            StringBuilder builder = new StringBuilder();
            builder.append(b.getString("title"))
                    .append(" ")
                    .append(b.getString("country"))
                    .append("\n")
                    .append(b.getString("date"))
                    .append("\n")
                    .append(b.getString("weather"))
                    .append(", ")
                    .append(b.getString("temp"));
            mRefresh.setSummary(builder.toString());
        }
    }

    private void updateSummary(ListPreference pref, String[] entries, String[] values) {
        String currentVal = pref.getValue();
        String newEntry = "";
        for (int i = 0; i < values.length; i++) {
            if (values[i].equals(currentVal)) {
                newEntry = entries[i];
                break;
            }
        }
        pref.setSummary(newEntry);
    }
}
