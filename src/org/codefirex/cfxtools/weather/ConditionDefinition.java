/*
 * Copyright (C) 2011 The Android Open Source Project
 * Copyright (C) 2012 Zhenghong Wang
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.codefirex.cfxtools.weather;

import org.codefirex.cfxtools.R;

public class ConditionDefinition {
	
	String[] conditionArray = new String[] {
			"tornado",
			"tropical storm",
			"hurricane",
			"severe thunderstorms",
			"thunderstorms",
			"mixed rain and snow",
			"mixed rain and sleet",
			"mixed snow and sleet",
			"freezing drizzle",
			"drizzle",
			"freezing rain",
			"showers",
			"showers",
			"snow flurries",
			"light snow showers",
			"blowing snow",
			"snow",
			"hail",
			"sleet",
			"dust",
			"foggy",
			"haze",
			"smoky",
			"blustery",
			"windy",
			"cold",
			"cloudy",
			"mostly cloudy (night)",
			"mostly cloudy (day)",
			"partly cloudy (night)",
			"partly cloudy (day)",
			"clear (night)",
			"sunny",
			"fair (night)",
			"fair (day)",
			"mixed rain and hail",
			"hot",
			"isolated thunderstorms",
			"scattered thunderstorms",
			"scattered thunderstorms",
			"scattered showers",
			"heavy snow",
			"scattered snow showers",
			"heavy snow",
			"partly cloudy",
			"thundershowers",
			"snow showers",
			"isolated thundershowers",
			"not available",
	};
	
	int[] conditionIcon = new int[] {
			R.drawable.tornado,
			R.drawable.heavy_rain,
			R.drawable.rain_tornado,
			R.drawable.rain_thunder,
			R.drawable.rain_thunder,
			R.drawable.rain_snow,
			R.drawable.ice,
			R.drawable.ice_snow,
			R.drawable.ice,
			R.drawable.rain,
			R.drawable.ice,
			R.drawable.heavy_rain,
			R.drawable.heavy_rain,
			R.drawable.snow,
			R.drawable.rain_snow,
			R.drawable.heavysnow,
			R.drawable.snow,
			R.drawable.ice,
			R.drawable.ice_snow,
			R.drawable.sunny,
			R.drawable.foggy,
			R.drawable.sunny,
			R.drawable.heat,
			R.drawable.sunny,
			R.drawable.partly_cloudy,
			R.drawable.cold,
			R.drawable.cloudy,
			R.drawable.cloudy_night,
			R.drawable.cloudy,
			R.drawable.cloudy_night,
			R.drawable.partly_cloudy,
			R.drawable.clear_night,
			R.drawable.sunny,
			R.drawable.clear_night,
			R.drawable.sunny,
			R.drawable.clear_night,
			R.drawable.sunny,
			R.drawable.ice_snow,
			R.drawable.heat,
			R.drawable.rain_thunder,
			R.drawable.rain_thunder,
			R.drawable.night_rain_thunder,
			R.drawable.rain,
			R.drawable.rain_snow,
			R.drawable.rain_snow,
			R.drawable.rain_snow,
			R.drawable.partly_cloudy,
			R.drawable.rain_thunder_sun,
			R.drawable.snow_thunder_sun,
			R.drawable.rain_thunder_sun,
			R.drawable.sunny
	};

	public int getIconByCode(int code) {
		if (code == -1) return R.drawable.sunny;
		return conditionIcon[code];
	}

	public String getConditionByCode(int code) {
		return conditionArray[code];
	}
}
