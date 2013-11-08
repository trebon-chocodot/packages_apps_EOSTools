package org.codefirex.cfxtools.weather;

import android.app.ActionBar;
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
import android.view.Menu;
import android.view.MenuItem;

import org.codefirex.cfxtools.R;

public class WeatherActivity extends PreferenceActivity implements
		Preference.OnPreferenceChangeListener {

	MenuItem mMenuItem;
	boolean mShowRefresh = false;

	CheckBoxPreference mToggle;
	ListPreference mLocation;
	ListPreference mInterval;
	ListPreference mTempScale;
	ForecastPreference mForecast;

	IntentFilter mWeatherFilter;
	BroadcastReceiver mWeatherReceiver;

	static final long TIMEOUT = 1000 * 10;

	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.weather_prefs);

		ActionBar actionBar = getActionBar();
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME
				| ActionBar.DISPLAY_SHOW_TITLE | ActionBar.DISPLAY_SHOW_CUSTOM);

		mToggle = (CheckBoxPreference) findPreference("weather_toggle_service");
		mLocation = (ListPreference) findPreference("weather_location_mode");
		mInterval = (ListPreference) findPreference("weather_interval");
		mTempScale = (ListPreference) findPreference("weather_temp_scale");

		mForecast = (ForecastPreference) findPreference("weather_forecast");
		mForecast.setSelectable(false);

		mToggle.setChecked(WeatherPrefs.getEnabled(this));
		mShowRefresh = mToggle.isChecked();

		mLocation.setValue(String.valueOf(WeatherPrefs.getLocationMode(this)));
		mInterval.setValue(String.valueOf(WeatherPrefs.getInterval(this)));
		mTempScale.setValue(String.valueOf(WeatherPrefs.getDegreeType(this)));

		updateSummary(
				mLocation,
				getResources().getStringArray(R.array.weather_location_entries),
				getResources().getStringArray(R.array.weather_location_values));
		updateSummary(
				mInterval,
				getResources().getStringArray(R.array.weather_interval_entries),
				getResources().getStringArray(R.array.weather_interval_values));
		updateSummary(
				mTempScale,
				getResources().getStringArray(
						R.array.weather_temp_scale_entries), getResources()
						.getStringArray(R.array.weather_temp_scale_values));

		mToggle.setOnPreferenceChangeListener(this);
		mLocation.setOnPreferenceChangeListener(this);
		mInterval.setOnPreferenceChangeListener(this);
		mTempScale.setOnPreferenceChangeListener(this);

		mWeatherFilter = new IntentFilter();
		mWeatherFilter.addAction(WeatherService.WEATHER_ACTION);
		mWeatherFilter.addAction(WeatherLocation.LOCATION_REFRESHING);

		mWeatherReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				String action = intent.getAction();
				if (action.equals(WeatherService.WEATHER_ACTION)) {
					if (mMenuItem != null) {
						new Handler().removeCallbacks(mCheckTimeout);
						mMenuItem.collapseActionView();
						mMenuItem.setActionView(null);
						// this is a hacky way to invalidate a preference view
						mForecast.setSummary(mForecast.getSummary() + " ");
						enablePrefs(true);
					}
				} else if (action.equals(WeatherLocation.LOCATION_REFRESHING)) {
					if (mMenuItem != null) {
						mMenuItem.setActionView(R.layout.progress_spinner);
						mMenuItem.expandActionView();
						enablePrefs(false);
						new Handler().postDelayed(mCheckTimeout, TIMEOUT);
					}
				}
			}
		};
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.weather_menu, menu);
		MenuItem refresh = menu.findItem(R.id.action_refresh);
		refresh.setVisible(mShowRefresh);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_refresh:
			mMenuItem = item;
			Intent intent = new Intent();
			intent.setAction(WeatherReceiver.ACTION);
			intent.putExtra(WeatherLocation.REFRESH_NOW, "refresh");
			sendBroadcast(intent);
			break;
		default:
			break;
		}
		return true;
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
			boolean enabled = ((Boolean) newValue).booleanValue();
			WeatherPrefs.setEnabled(this, enabled);
			Intent intent = new Intent().setAction(WeatherReceiver.ACTION)
					.putExtra(WeatherReceiver.ENABLED_CHANGED,
							"enabled_changed");
			sendBroadcast(intent);
			mShowRefresh = enabled;
			invalidateOptionsMenu();
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
					updateSummary(
							mLocation,
							getResources().getStringArray(
									R.array.weather_location_entries),
							getResources().getStringArray(
									R.array.weather_location_values));
				}
			}, 100);
			return true;
		} else if (preference.equals(mInterval)) {
			String val = ((String) newValue).toString();
			WeatherPrefs.setInterval(this, val);
			Intent intent = new Intent().setAction(WeatherReceiver.ACTION)
					.putExtra(WeatherLocation.INTERVAL_CHANGED, "interval");
			sendBroadcast(intent);
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					updateSummary(
							mInterval,
							getResources().getStringArray(
									R.array.weather_interval_entries),
							getResources().getStringArray(
									R.array.weather_interval_values));
				}
			}, 100);
			return true;
		} else if (preference.equals(mTempScale)) {
			int val = Integer.parseInt(((String) newValue).toString());
			WeatherPrefs.setDegreeType(this, val);
			Intent intent = new Intent().setAction(WeatherReceiver.ACTION)
					.putExtra(WeatherLocation.SCALE_CHANGED, "scale");
			sendBroadcast(intent);
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					updateSummary(
							mTempScale,
							getResources().getStringArray(
									R.array.weather_temp_scale_entries),
							getResources().getStringArray(
									R.array.weather_temp_scale_values));
				}
			}, 100);
			return true;
		}
		return false;
	}

	private void updateSummary(ListPreference pref, String[] entries,
			String[] values) {
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

	private void enablePrefs(boolean enabled) {
		mLocation.setEnabled(enabled);
		mInterval.setEnabled(enabled);
		mTempScale.setEnabled(enabled);
	}

	private Runnable mCheckTimeout = new Runnable() {
		@Override
		public void run() {
			mMenuItem.collapseActionView();
			mMenuItem.setActionView(null);
			// this is a hacky way to invalidate a preference view
			mForecast.setSummary(mForecast.getSummary() + " ");
			enablePrefs(true);
		}
	};
}
