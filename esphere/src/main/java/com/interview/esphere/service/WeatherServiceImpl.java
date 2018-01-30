package com.interview.esphere.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.interview.esphere.openweather.OpenWeatherMap;
import com.interview.esphere.service.exception.WeatherException;
import com.interview.esphere.utils.RestCallUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.concurrent.Semaphore;

@Service("weatherService")
public class WeatherServiceImpl implements WeatherService {

    private final static Logger LOGGER = LoggerFactory.getLogger(WeatherServiceImpl.class);

    private ThreadLocal<ObjectMapper> objectMapperThreadLocal = ThreadLocal.withInitial(ObjectMapper::new);

    /**
     * barrier for single thread working (by task requirements)
     */
    private Semaphore semaphore = new Semaphore(1);

    private final static String REQUEST_BY_CITY = "http://api.openweathermap.org/data/2.5/weather?q=%s&appid=%s";
    private final static String REQUEST_BY_GEO = "http://api.openweathermap.org/data/2.5/weather?lat=%s&lon=%d&appid=%s";

    private final static String APPLID = "b3083359aa3c3fdb833ee97d7e669b1b";

    @Override
    public OpenWeatherMap findByCityName(String cityName) throws WeatherException {
        return getOpenWeatherMap(String.format(REQUEST_BY_CITY, cityName, APPLID));
    }

    @Override
    public OpenWeatherMap findByGeo(BigDecimal lat, Integer lon) throws WeatherException {
        return getOpenWeatherMap(String.format(REQUEST_BY_GEO, lat.toString(), lon, APPLID));
    }

    private OpenWeatherMap getOpenWeatherMap(String formattedString) throws WeatherException {

        try {

            semaphore.acquire();
            LOGGER.debug("Request rest call format: {}", formattedString);

            String response = RestCallUtil.restCall(formattedString);
            LOGGER.info("OpenWeatherMap returned response: {}", response);

            return objectMapperThreadLocal.get().readValue(response, OpenWeatherMap.class);

        } catch (Exception e ) {
            throw new WeatherException(e.getMessage());
        } finally {
            semaphore.release();
        }
    }

    @Override
    public String serializeWeatherToString(OpenWeatherMap openWeatherMap) throws WeatherException {
        try {
            return objectMapperThreadLocal.get().writerWithDefaultPrettyPrinter().writeValueAsString(openWeatherMap);
        } catch (JsonProcessingException e) {
            LOGGER.error("OpenWeatherMap can be serialized: {}", e.getMessage());
            throw new WeatherException(e.getMessage());
        }
    }

    @Override
    public OpenWeatherMap deserializeWeather(String openWeatherMapString) {
        try {
            return objectMapperThreadLocal.get().readValue(openWeatherMapString, OpenWeatherMap.class);
        } catch (IOException e) {
            return null;
        }
    }
}
