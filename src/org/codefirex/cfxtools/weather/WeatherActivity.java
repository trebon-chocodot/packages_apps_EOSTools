package org.codefirex.cfxtools.weather;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.preference.Preference;
import android.preference.ListPreference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.preference.PreferenceScreen;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Switch;
import android.widget.TextView;

import org.codefirex.cfxtools.R;

public class WeatherActivity extends PreferenceActivity implements
		Preference.OnPreferenceChangeListener {

	static final String SETTINGS_CATEGORY = "settings_category";

	MenuItem mRefreshItem;

	PreferenceCategory mSettings;
	ListPreference mLocation;
	ListPreference mInterval;
	ListPreference mTempScale;
	ForecastPreference mForecast;
	Preference mCredits;

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
		
		mSettings = (PreferenceCategory) findPreference(SETTINGS_CATEGORY);

		Boolean isEnabled = WeatherPrefs.getEnabled(this);

		mLocation = (ListPreference) mSettings.findPreference("weather_location_mode");
		mInterval = (ListPreference) mSettings.findPreference("weather_interval");
		mTempScale = (ListPreference) mSettings.findPreference("weather_temp_scale");

		mCredits = mSettings.findPreference("credits_pref");
		mCredits.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {

			@Override
			public boolean onPreferenceClick(Preference preference) {
				showCreditDialog();
				return true;
			}
		});

		mForecast = (ForecastPreference) findPreference("weather_forecast");
		mForecast.setSelectable(false);
		toggleForecast(isEnabled);

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

		mLocation.setOnPreferenceChangeListener(this);
		mInterval.setOnPreferenceChangeListener(this);
		mTempScale.setOnPreferenceChangeListener(this);

		enablePrefs(isEnabled);

		mWeatherFilter = new IntentFilter();
		mWeatherFilter.addAction(WeatherService.WEATHER_ACTION);
		mWeatherFilter.addAction(WeatherLocation.LOCATION_REFRESHING);

		mWeatherReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				String action = intent.getAction();
				if (action.equals(WeatherService.WEATHER_ACTION)) {
					if (mRefreshItem != null) {
						new Handler().removeCallbacks(mCheckTimeout);
						mRefreshItem.collapseActionView();
						mRefreshItem.setActionView(null);
						// this is a hacky way to invalidate a preference view
						mForecast.setSummary(mForecast.getSummary() + " ");
						enablePrefs(WeatherPrefs.getEnabled(WeatherActivity.this));
					}
				} else if (action.equals(WeatherLocation.LOCATION_REFRESHING)) {
					if (mRefreshItem != null) {
						mRefreshItem.setActionView(R.layout.progress_spinner);
						mRefreshItem.expandActionView();
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

		MenuItem mSwitchItem = menu.findItem(R.id.action_switch);
		Switch s = (Switch)mSwitchItem.getActionView().findViewById(
				R.id.switchForActionBar);
		s.setChecked(WeatherPrefs.getEnabled(this));
		s.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				WeatherPrefs.setEnabled(WeatherActivity.this, isChecked);
				Intent intent = new Intent().setAction(WeatherReceiver.ACTION)
						.putExtra(WeatherReceiver.ENABLED_CHANGED,
								"enabled_changed");
				sendBroadcast(intent);
				toggleForecast(isChecked);
				enablePrefs(isChecked);
				invalidateOptionsMenu();
			}
		});

		mRefreshItem = menu.findItem(R.id.action_refresh);
		mRefreshItem.setEnabled(s.isChecked());
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_refresh:
			mRefreshItem = item;
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
		if (preference.equals(mLocation)) {
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

	private void toggleForecast(boolean enabled) {
		PreferenceScreen screen = getPreferenceScreen();
		if (enabled) {
			screen.addPreference(mForecast);
		} else {
			screen.removePreference(mForecast);
		}
	}

	private void enablePrefs(boolean enabled) {
		mLocation.setEnabled(enabled);
		mInterval.setEnabled(enabled);
		mTempScale.setEnabled(enabled);
	}

	private Runnable mCheckTimeout = new Runnable() {
		@Override
		public void run() {
			mRefreshItem.collapseActionView();
			mRefreshItem.setActionView(null);
			// this is a hacky way to invalidate a preference view
			mForecast.setSummary(mForecast.getSummary() + " ");
			enablePrefs(WeatherPrefs.getEnabled(WeatherActivity.this));
		}
	};

	private void showCreditDialog() {
		AlertDialog.Builder b = new AlertDialog.Builder(this);
		b.setTitle(R.string.credits);
		final AlertDialog dialog = b.create();

		View v = getLayoutInflater().inflate(R.layout.credits_view, null);

		// this could all be xml but got wonk in compiling the string
		TextView linkView = (TextView) v.findViewById(R.id.weather_icon_url);
		String str_links = "<a href='http://d3stroy.deviantart.com/art/SILq-Weather-Icons-356609017'>Icon set on DeviantART</a>";
		linkView.setLinksClickable(true);
		linkView.setMovementMethod(LinkMovementMethod.getInstance());
		linkView.setText(Html.fromHtml(str_links));

		// have to maunally create buttons when AlertDialog has a custom view
		Button ok = (Button) v.findViewById(R.id.button_ok);
		ok.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				dialog.dismiss();				
			}
		});

		dialog.setView(v);
		dialog.show();
	}
}
