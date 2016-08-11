package com.doggybites;

import net.aksingh.owmjapis.CurrentWeather;
import net.aksingh.owmjapis.OpenWeatherMap;
import org.json.JSONException;

import java.io.IOException;
import java.util.Optional;

public class WeatherService {

    private OpenWeatherMap openWeatherMap;

    public WeatherService() {
        openWeatherMap = new OpenWeatherMap(Settings.INSTANCE.getWeather());
        openWeatherMap.setUnits(OpenWeatherMap.Units.METRIC);
    }

    public Optional<Float> getTemp(String city) {
        try {
            CurrentWeather cwd = openWeatherMap.currentWeatherByCityName(city);
            if (cwd.isValid()) {
                if (cwd.getMainInstance().hasTemperature()) {
                    return Optional.of(cwd.getMainInstance().getTemperature());
                }
            }
            return Optional.empty();
        } catch (IOException | JSONException e) {
            throw new RuntimeException(e);
        }
    }
}
