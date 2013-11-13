package org.codefirex.cfxtools.weather;

import org.codefirex.cfxtools.R;
import android.content.Context;
import android.os.Bundle;
import android.preference.Preference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class ForecastPreference extends Preference {
	TextView mCurrentCity;
	ImageView mCurrentWeatherIcon;
	TextView mCurrentWeatherText;
	TextView mCurrentTemp;
	TextView mCurrentTempHigh;
	TextView mCurrentTempLow;

	TextView mWindChill;
	TextView mWindSpeed;
	TextView mWindDirection;

	TextView mHumidity;
	TextView mPressure;
	TextView mVisibility;

	TextView mDayOne;
	ImageView mDayOneWeather;
	TextView mDayOneHigh;
	TextView mDayOneLow;
	TextView mDayTwo;
	ImageView mDayTwoWeather;
	TextView mDayTwoHigh;
	TextView mDayTwoLow;
	TextView mDayThree;
	ImageView mDayThreeWeather;
	TextView mDayThreeHigh;
	TextView mDayThreeLow;
	TextView mDayFour;
	ImageView mDayFourWeather;
	TextView mDayFourHigh;
	TextView mDayFourLow;

	ConditionDefinition mDefs = new ConditionDefinition();
	Context mContext;

	public ForecastPreference(Context context) {
		this(context, null);
		// TODO Auto-generated constructor stub
	}

	public ForecastPreference(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		// TODO Auto-generated constructor stub
	}

	public ForecastPreference(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mContext = context;
		setLayoutResource(R.layout.forecast_view);
	}

	@Override
	public void onBindView(View v) {
		super.onBindView(v);
		mCurrentWeatherIcon = (ImageView) v.findViewById(R.id.current_weather_icon);

		mCurrentWeatherText = (TextView) v
				.findViewById(R.id.current_weather_text);
		
		mCurrentCity = (TextView) v.findViewById(R.id.current_city);
		mCurrentTemp = (TextView) v.findViewById(R.id.current_temperature);
		mCurrentTempHigh = (TextView) v.findViewById(R.id.current_temperature_high);
		mCurrentTempLow = (TextView) v.findViewById(R.id.current_temperature_low);

		mWindChill = (TextView) v.findViewById(R.id.wind_chill);
		mWindSpeed = (TextView) v.findViewById(R.id.wind_speed);
		mWindDirection = (TextView) v.findViewById(R.id.wind_direction);		

		mHumidity = (TextView) v.findViewById(R.id.conditions_humidity);
		mPressure = (TextView) v.findViewById(R.id.conditions_pressure);
		mVisibility = (TextView) v.findViewById(R.id.conditions_visibility);

		mDayOne = (TextView) v.findViewById(R.id.day_one_day);
		mDayOneWeather = (ImageView) v.findViewById(R.id.day_one_weather);
		mDayOneHigh = (TextView) v.findViewById(R.id.day_one_high_text);
		mDayOneLow = (TextView) v.findViewById(R.id.day_one_low_text);

		mDayTwo = (TextView) v.findViewById(R.id.day_two_day);
		mDayTwoWeather = (ImageView) v.findViewById(R.id.day_two_weather);
		mDayTwoHigh = (TextView) v.findViewById(R.id.day_two_high_text);
		mDayTwoLow = (TextView) v.findViewById(R.id.day_two_low_text);

		mDayThree = (TextView) v.findViewById(R.id.day_three_day);
		mDayThreeWeather = (ImageView) v.findViewById(R.id.day_three_weather);
		mDayThreeHigh = (TextView) v.findViewById(R.id.day_three_high_text);
		mDayThreeLow = (TextView) v.findViewById(R.id.day_three_low_text);

		mDayFour = (TextView) v.findViewById(R.id.day_four_day);
		mDayFourWeather = (ImageView) v.findViewById(R.id.day_four_weather);
		mDayFourHigh = (TextView) v.findViewById(R.id.day_four_high_text);
		mDayFourLow = (TextView) v.findViewById(R.id.day_four_low_text);

		setWeatherBundle(WeatherPrefs.getLatestWeatherBundle(v.getContext()));

	}

	void setWeatherBundle(Bundle b) {
		mCurrentWeatherIcon.setImageResource(mDefs.getIconByCode(Integer.parseInt(b
				.getString("current_code"))));

		mCurrentWeatherText.setText(b.getString("weather"));

		mCurrentCity.setText(b.getString("city"));
		mCurrentTemp.setText(b.getString("temp"));
		mCurrentTempHigh.setText(b.getString("f1_temp_high"));
		mCurrentTempLow.setText(b.getString("f1_temp_low"));

		StringBuilder sb = new StringBuilder()
		.append(mContext.getResources().getString(R.string.wind_chill))
		.append(": ")
		.append(b.getString("chill"));
		mWindChill.setText(sb.toString());

		sb = new StringBuilder()
		.append(mContext.getResources().getString(R.string.wind_speed))
		.append(": ")
		.append(b.getString("speed"));
		mWindSpeed.setText(sb.toString());

		 sb = new StringBuilder()
		.append(mContext.getResources().getString(R.string.wind_direction))
		.append(": ")
		.append(b.getString("direction"));
		mWindDirection.setText(sb.toString());

		sb = new StringBuilder()
		.append(mContext.getResources().getString(R.string.humidity))
		.append(": ")
		.append(b.getString("humidity"));
		mHumidity.setText(sb.toString());

		sb = new StringBuilder()
		.append(mContext.getResources().getString(R.string.pressure))
		.append(": ")
		.append(b.getString("pressure"));
		mPressure.setText(sb.toString());

		 sb = new StringBuilder()
		.append(mContext.getResources().getString(R.string.visibility))
		.append(": ")
		.append(b.getString("visibility"));
		mVisibility.setText(sb.toString());

		mDayOne.setText(b.getString("f1_day"));
		mDayOneWeather.setImageResource(mDefs.getIconByCode(Integer.parseInt(b
				.getString("f1_current_code"))));
		mDayOneHigh.setText(b.getString("f1_temp_high"));
		mDayOneLow.setText(b.getString("f1_temp_low"));
		mDayTwo.setText(b.getString("f2_day"));
		mDayTwoWeather.setImageResource(mDefs.getIconByCode(Integer.parseInt(b
				.getString("f2_current_code"))));
		mDayTwoHigh.setText(b.getString("f2_temp_high"));
		mDayTwoLow.setText(b.getString("f2_temp_low"));
		mDayThree.setText(b.getString("f3_day"));
		mDayThreeWeather.setImageResource(mDefs.getIconByCode(Integer
				.parseInt(b.getString("f3_current_code"))));
		mDayThreeHigh.setText(b.getString("f3_temp_high"));
		mDayThreeLow.setText(b.getString("f3_temp_low"));
		mDayFour.setText(b.getString("f4_day"));
		mDayFourWeather.setImageResource(mDefs.getIconByCode(Integer.parseInt(b
				.getString("f4_current_code"))));
		mDayFourHigh.setText(b.getString("f4_temp_high"));
		mDayFourLow.setText(b.getString("f4_temp_low"));
	}
}
