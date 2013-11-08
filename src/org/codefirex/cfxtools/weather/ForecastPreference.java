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
	TextView mHeaderCity;
	TextView mTemp;
	TextView mCurrentWeatherText;
	TextView mDayOne;
	TextView mDayOneWeather;
	TextView mDayOneHigh;
	TextView mDayOneLow;
	TextView mDayTwo;
	TextView mDayTwoWeather;
	TextView mDayTwoHigh;
	TextView mDayTwoLow;
	TextView mDayThree;
	TextView mDayThreeWeather;
	TextView mDayThreeHigh;
	TextView mDayThreeLow;
	TextView mDayFour;
	TextView mDayFourWeather;
	TextView mDayFourHigh;
	TextView mDayFourLow;

	ImageView mYahooLogo;

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
		setLayoutResource(R.layout.forecast_view);
	}

	@Override
	public void onBindView(View v) {
		super.onBindView(v);
		mHeaderCity = (TextView) v.findViewById(R.id.header_city);
		mYahooLogo = (ImageView) v.findViewById(R.id.header_yahoo_icon);

		mTemp = (TextView) v.findViewById(R.id.tempearature_text);
		mCurrentWeatherText = (TextView) v
				.findViewById(R.id.current_weather_text);

		mDayOne = (TextView) v.findViewById(R.id.day_one_day);
		mDayOneWeather = (TextView) v.findViewById(R.id.day_one_weather);
		mDayOneHigh = (TextView) v.findViewById(R.id.day_one_high_text);
		mDayOneLow = (TextView) v.findViewById(R.id.day_one_low_text);

		mDayTwo = (TextView) v.findViewById(R.id.day_two_day);
		mDayTwoWeather = (TextView) v.findViewById(R.id.day_two_weather);
		mDayTwoHigh = (TextView) v.findViewById(R.id.day_two_high_text);
		mDayTwoLow = (TextView) v.findViewById(R.id.day_two_low_text);

		mDayThree = (TextView) v.findViewById(R.id.day_three_day);
		mDayThreeWeather = (TextView) v.findViewById(R.id.day_three_weather);
		mDayThreeHigh = (TextView) v.findViewById(R.id.day_three_high_text);
		mDayThreeLow = (TextView) v.findViewById(R.id.day_three_low_text);

		mDayFour = (TextView) v.findViewById(R.id.day_four_day);
		mDayFourWeather = (TextView) v.findViewById(R.id.day_four_weather);
		mDayFourHigh = (TextView) v.findViewById(R.id.day_four_high_text);
		mDayFourLow = (TextView) v.findViewById(R.id.day_four_low_text);

		mYahooLogo.setImageResource(R.drawable.yw_logo);

		setWeatherBundle(WeatherPrefs.getLatestWeatherBundle(v.getContext()));

	}

	void setWeatherBundle(Bundle b) {
		mHeaderCity.setText(b.getString("city"));
		mTemp.setText(b.getString("temp"));
		mCurrentWeatherText.setText(b.getString("weather"));

		mDayOne.setText(b.getString("f1_day"));
		//mDayOneWeather.setText(b.getString("f1_weather"));
		mDayOneHigh.setText(b.getString("f1_temp_high"));
		mDayOneLow.setText(b.getString("f1_temp_low"));
		mDayTwo.setText(b.getString("f2_day"));
		//mDayTwoWeather.setText(b.getString("f2_weather"));
		mDayTwoHigh.setText(b.getString("f2_temp_high"));
		mDayTwoLow.setText(b.getString("f2_temp_low"));
		mDayThree.setText(b.getString("f3_day"));
		//mDayThreeWeather.setText(b.getString("f3_weather"));
		mDayThreeHigh.setText(b.getString("f3_temp_high"));
		mDayThreeLow.setText(b.getString("f3_temp_low"));
		mDayFour.setText(b.getString("f4_day"));
		//mDayFourWeather.setText(b.getString("f4_weather"));
		mDayFourHigh.setText(b.getString("f4_temp_high"));
		mDayFourLow.setText(b.getString("f4_temp_low"));

	}

}
