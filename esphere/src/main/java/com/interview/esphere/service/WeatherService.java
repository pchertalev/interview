package com.interview.esphere.service;

import com.interview.esphere.openweather.OpenWeatherMap;
import com.interview.esphere.service.exception.WeatherException;

import java.math.BigDecimal;

public interface WeatherService {
    OpenWeatherMap findByCityName(String cityName) throws WeatherException;
    OpenWeatherMap findByGeo(BigDecimal lat, Integer lon) throws WeatherException;

    String serializeWeatherToString(OpenWeatherMap openWeatherMap) throws WeatherException;

    OpenWeatherMap deserializeWeather(String openWeatherMap);
}
