package com.interview.esphere.controller;

import com.interview.esphere.domain.model.Operation;
import com.interview.esphere.domain.model.OperationType;
import com.interview.esphere.domain.model.User;
import com.interview.esphere.domain.repository.OperationRepository;
import com.interview.esphere.openweather.OpenWeatherMap;
import com.interview.esphere.service.UserService;
import com.interview.esphere.service.WeatherService;
import com.interview.esphere.service.exception.WeatherException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

@Controller
@Scope("session")
public class SessionScopedController {

    @Autowired
    private UserService userService;

    @Autowired
    private OperationRepository operationRepository;

    @Autowired
    private WeatherService weatherService;

    private User user = null;

    @PostConstruct
    void init() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        user = userService.findUserByEmail(auth.getName());
    }

    @RequestMapping(value = "/admin/home", method = RequestMethod.GET)
    public ModelAndView home() {
        ModelAndView modelAndView = new ModelAndView();

        modelAndView.addObject("userName", "Welcome " + user.getName() + " " + user.getLastName() + " (" + user.getEmail() + ")");
        modelAndView.addObject("geoSelected", true);
        modelAndView.setViewName("admin/home");

        List<Operation> operations = operationRepository.findTop20ByUserOrderByExecutionTimeDesc(user);
        modelAndView.addObject("operations", operations);

        return modelAndView;
    }

    @RequestMapping(value = "/admin/weather-geo", method = RequestMethod.POST)
    public ModelAndView weatherGeo(@RequestParam String lat, @RequestParam String lon) {
        ModelAndView modelAndView = new ModelAndView();

        modelAndView.addObject("userName", "Welcome " + user.getName() + " " + user.getLastName() + " (" + user.getEmail() + ")");
        modelAndView.addObject("adminMessage", "Content Available Only for Users with Admin Role");
        modelAndView.addObject("geoSelected", true);
        modelAndView.addObject("lat", lat);
        modelAndView.addObject("lon", lon);
        modelAndView.setViewName("admin/home");

        if (!isDoubleBetween(lat, -85.05112878, 85.05112878)) {

            String latError = "Valid latitudes are from -85.05112878 to 85.05112878 degrees";
            modelAndView.addObject("latError", latError);

        } else if (!isIntBetween(lon, -180, 180)) {

            String lonError = "Valid longitudes are from -180 to 180 degrees";
            modelAndView.addObject("lonError", lonError);

        } else {

            String weatherResult;
            try {
                OpenWeatherMap byGeo = weatherService.findByGeo(new BigDecimal(lat), new Integer(lon));
                //TODO: here we can use all data from openweathermap but just serialize

                weatherResult = weatherService.serializeWeatherToString(byGeo);
            } catch (WeatherException e) {
                weatherResult = e.getMessage();
            }

            Operation operation = new Operation();
            operation.setExecutionTime(new Timestamp(System.currentTimeMillis()));
            operation.setUser(user);
            operation.setOperationType(OperationType.BY_GEO);
            operation.setParams("Lat: " + lat + " Lon: " + lon);
            operation.setResult(weatherResult);

            operationRepository.save(operation);
        }

        addOperationsToModel(modelAndView);

        return modelAndView;
    }

    private void addOperationsToModel(ModelAndView modelAndView) {
        List<Operation> operations = operationRepository.findTop20ByUserOrderByExecutionTimeDesc(user);
        for (Operation operation : operations) {
            operation.setOpenWeatherMap(weatherService.deserializeWeather(operation.getResult()));
        }

        modelAndView.addObject("operations", operations);
    }

    private boolean isDoubleBetween(String s, double min, double max) {
        try {
            BigDecimal bigDecimal = new BigDecimal(s);
            double val = bigDecimal.doubleValue();
            return val >= min && val <= max;
        } catch (NumberFormatException er) {
            return false;
        }
    }

    private boolean isIntBetween(String s, int min, int max) {
        try {
            int integer = Integer.parseInt(s);
            return integer >= min && integer <= max;
        } catch (NumberFormatException er) {
            return false;
        }
    }

    @RequestMapping(value = "/admin/weather-city", method = RequestMethod.POST)
    public ModelAndView weatherCity(@RequestParam String city) {
        ModelAndView modelAndView = new ModelAndView();

        modelAndView.addObject("userName", "Welcome " + user.getName() + " " + user.getLastName() + " (" + user.getEmail() + ")");
        modelAndView.addObject("geoSelected", false);
        modelAndView.setViewName("admin/home");

        if (StringUtils.isBlank(city)) {

            String cityError = "City must be not empty";
            modelAndView.addObject("cityError", cityError);

        } else {

            String weatherResult;
            try {
                OpenWeatherMap byCity = weatherService.findByCityName(city);
                weatherResult = weatherService.serializeWeatherToString(byCity);
            } catch (WeatherException e) {
                weatherResult = "Error: " + e.getMessage();
            }

            Operation operation = new Operation();
            operation.setExecutionTime(new Timestamp(System.currentTimeMillis()));
            operation.setUser(user);
            operation.setOperationType(OperationType.BY_CITY);
            operation.setParams("City: " + city);
            operation.setResult(weatherResult);

            operationRepository.save(operation);
        }

        addOperationsToModel(modelAndView);

        return modelAndView;
    }

}
