package com.example;

import com.interview.esphere.openweather.OpenWeatherMap;
import com.interview.esphere.service.WeatherService;
import com.interview.esphere.service.WeatherServiceImpl;
import com.interview.esphere.service.exception.WeatherException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TestWeatherApi {

    @Test
    public void test() throws WeatherException {
        WeatherService weatherService = new WeatherServiceImpl();
        OpenWeatherMap res = weatherService.findByCityName("Moscow,ru");

        System.out.println(weatherService.serializeWeatherToString(res));

    }
}
