package com.example;

import com.interview.esphere.openweather.OpenWeatherMap;
import com.interview.esphere.service.WeatherService;
import com.interview.esphere.service.WeatherServiceImpl;
import com.interview.esphere.service.exception.WeatherException;
import com.interview.esphere.utils.RestCallException;
import com.interview.esphere.utils.RestCallUtil;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TestWeatherApi {

    @Mock
    private RestCallUtil restCallUtil;

    @Test
    public void test() throws WeatherException, RestCallException {
        String req = "{\"coord\":{\"lon\":-0.13,\"lat\":51.51},\"weather\":[{\"id\":500,\"main\":\"Rain\",\"" +
                "description\":\"light rain\",\"icon\":\"10n\"},{\"id\":310,\"main\":\"Drizzle\",\"" +
                "description\":\"light intensity drizzle rain\",\"icon\":\"09n\"},{\"id\":701,\"main\":\"Mist\",\"" +
                "description\":\"mist\",\"icon\":\"50n\"}],\"base\":\"stations\",\"main\":{\"temp\":280.55,\"" +
                "pressure\":1017,\"humidity\":87,\"temp_min\":279.15,\"temp_max\":282.15},\"visibility\":10000,\"" +
                "wind\":{\"speed\":4.6,\"deg\":220},\"clouds\":{\"all\":90},\"dt\":1517348100,\"sys\":{\"" +
                "type\":1,\"id\":5091,\"message\":0.0114,\"country\":\"GB\",\"sunrise\":1517298036,\"" +
                "sunset\":1517330875},\"id\":2643743,\"name\":\"London\",\"cod\":200}";

        WeatherService weatherService = new WeatherServiceImpl(restCallUtil);
        when(restCallUtil.restCall(anyString())).thenReturn(req);
        OpenWeatherMap res = weatherService.findByCityName("London,uk");

        Assert.assertNotNull(res);
        Assert.assertNotNull(res.getSys());
        Assert.assertEquals(res.getName(), "London");
        Assert.assertEquals(res.getSys().getCountry(), "GB");

    }
}
