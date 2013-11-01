package org.codefirex.cfxtools.weather;

import android.os.Bundle;

/**
 * Created by williamwebb on 10/26/13.
 */
public class WeatherUtils {
    private static char degree = '\u00B0';

    protected static String makeTempString(boolean isF, String temp_f, String temp_c) {
        String theTemp = isF ? temp_f : temp_c;
        String theLetter = isF ? "F" : "C";
        StringBuilder b = new StringBuilder()
                .append(theTemp)
                .append(degree)
                .append(theLetter);
        return b.toString();
    }

    protected static Bundle makeWeatherBundle(WeatherInfo info, boolean isF) {
        Bundle b = new Bundle();
        
        b.putString("title", info.getTitle());
        b.putString("city", info.getLocationCity());
        b.putString("country", info.getLocationCountry());
        b.putString("date", info.getCurrentConditionDate());
        b.putString("weather", info.getCurrentText());
//        b.putString("temp_f", String.valueOf(info.getCurrentTempF()));
//        b.putString("temp_c", String.valueOf(info.getCurrentTempC()));
        b.putString("temp", makeTempString(isF,
                String.valueOf(info.getCurrentTempF()),
                String.valueOf(info.getCurrentTempC())));
//        b.putString("chill_f", info.getWindChillF());
//        b.putString("chill_c", info.getWindChillC());
        b.putString("chill", makeTempString(isF,
                String.valueOf(info.getWindChillF()),
                String.valueOf(info.getWindChillC())));
        b.putString("direction", info.getWindDirection());
        b.putString("speed", info.getWindSpeed());
        b.putString("humidity", info.getAtmosphereHumidity());
        b.putString("pressure", info.getAtmospherePressure());
        b.putString("visibility", info.getAtmosphereVisibility());
        b.putString("current_url", info.getCurrentConditionIconURL());

        WeatherInfo.ForecastInfo forecastInfo = info.getForecastInfo1();
        b.putString("f1_day", forecastInfo.getForecastDay());
        b.putString("f1_date", forecastInfo.getForecastDate());
        b.putString("f1_weather", forecastInfo.getForecastText());
//        b.putString("f1_temp_low_c", String.valueOf(forecastInfo.getForecastTempLowC()));
//        b.putString("f1_temp_high_c", String.valueOf(forecastInfo.getForecastTempHighC()));
//        b.putString("f1_temp_low_f", String.valueOf(forecastInfo.getForecastTempLowF()));
//        b.putString("f1_temp_high_f", String.valueOf(forecastInfo.getForecastTempHighF()));
        b.putString("f1_temp_low", makeTempString(isF,
                String.valueOf(forecastInfo.getForecastTempLowF()),
                String.valueOf(forecastInfo.getForecastTempLowC())));
        b.putString("f1_temp_high", makeTempString(isF,
                String.valueOf(forecastInfo.getForecastTempHighF()),
                String.valueOf(forecastInfo.getForecastTempHighC())));
        b.putString("f1_url", forecastInfo.getForecastConditionIconURL());

        forecastInfo = info.getForecastInfo2();
        b.putString("f2_day", forecastInfo.getForecastDay());
        b.putString("f2_date", forecastInfo.getForecastDate());
        b.putString("f2_weather", forecastInfo.getForecastText());
//        b.putString("f2_temp_low_c", String.valueOf(forecastInfo.getForecastTempLowC()));
//        b.putString("f2_temp_high_c", String.valueOf(forecastInfo.getForecastTempHighC()));
//        b.putString("f2_temp_low_f", String.valueOf(forecastInfo.getForecastTempLowF()));
//        b.putString("f2_temp_high_f", String.valueOf(forecastInfo.getForecastTempHighF()));
        b.putString("f2_temp_low", makeTempString(isF,
                String.valueOf(forecastInfo.getForecastTempLowF()),
                String.valueOf(forecastInfo.getForecastTempLowC())));
        b.putString("f2_temp_high", makeTempString(isF,
                String.valueOf(forecastInfo.getForecastTempHighF()),
                String.valueOf(forecastInfo.getForecastTempHighC())));
        b.putString("f2_url", forecastInfo.getForecastConditionIconURL());

        forecastInfo = info.getForecastInfo3();
        b.putString("f3_day", forecastInfo.getForecastDay());
        b.putString("f3_date", forecastInfo.getForecastDate());
        b.putString("f3_weather", forecastInfo.getForecastText());
//        b.putString("f3_temp_low_c", String.valueOf(forecastInfo.getForecastTempLowC()));
//        b.putString("f3_temp_high_c", String.valueOf(forecastInfo.getForecastTempHighC()));
//        b.putString("f3_temp_low_f", String.valueOf(forecastInfo.getForecastTempLowF()));
//        b.putString("f3_temp_high_f", String.valueOf(forecastInfo.getForecastTempHighF()));
        b.putString("f3_temp_low", makeTempString(isF,
                String.valueOf(forecastInfo.getForecastTempLowF()),
                String.valueOf(forecastInfo.getForecastTempLowC())));
        b.putString("f3_temp_high", makeTempString(isF,
                String.valueOf(forecastInfo.getForecastTempHighF()),
                String.valueOf(forecastInfo.getForecastTempHighC())));
        b.putString("f3_url", forecastInfo.getForecastConditionIconURL());

        forecastInfo = info.getForecastInfo4();
        b.putString("f4_day", forecastInfo.getForecastDay());
        b.putString("f4_date", forecastInfo.getForecastDate());
        b.putString("f4_weather", forecastInfo.getForecastText());
//        b.putString("f4_temp_low_c", String.valueOf(forecastInfo.getForecastTempLowC()));
//        b.putString("f4_temp_high_c", String.valueOf(forecastInfo.getForecastTempHighC()));
//        b.putString("f4_temp_low_f", String.valueOf(forecastInfo.getForecastTempLowF()));
//        b.putString("f4_temp_high_f", String.valueOf(forecastInfo.getForecastTempHighF()));
        b.putString("f4_temp_low", makeTempString(isF,
                String.valueOf(forecastInfo.getForecastTempLowF()),
                String.valueOf(forecastInfo.getForecastTempLowC())));
        b.putString("f4_temp_high", makeTempString(isF,
                String.valueOf(forecastInfo.getForecastTempHighF()),
                String.valueOf(forecastInfo.getForecastTempHighC())));
        b.putString("f4_url", forecastInfo.getForecastConditionIconURL());

        return b;
    }
}
