package ua.tqs.MoliceiroMeals.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ua.tqs.MoliceiroMeals.dto.WeatherForecast;
import ua.tqs.MoliceiroMeals.service.WeatherService;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(WeatherController.class)
class WeatherControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WeatherService weatherService;

    @Test
    void whenGetWeather_thenReturnForecast() throws Exception {
        // Create sample forecast data
        WeatherForecast forecast = new WeatherForecast();
        WeatherForecast.Daily daily = new WeatherForecast.Daily();
        daily.setTime(List.of("2025-04-09"));
        daily.setTemperature_2m_max(List.of(25.5));
        daily.setTemperature_2m_min(List.of(15.0));
        daily.setPrecipitation_probability_mean(List.of(10.0));
        daily.setWeathercode(List.of(0));
        forecast.setDaily(daily);

       
        when(weatherService.getForecast(eq("aveiro"), any(LocalDate.class)))
                .thenReturn(forecast);

  
        mockMvc.perform(get("/api/weather/aveiro")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.daily.time[0]", is("2025-04-09")))
                .andExpect(jsonPath("$.daily.temperature_2m_max[0]", is(25.5)))
                .andExpect(jsonPath("$.daily.temperature_2m_min[0]", is(15.0)));

  
        verify(weatherService, times(1)).getForecast(eq("aveiro"), any(LocalDate.class));
    }

    @Test
    void whenGetWeatherWithDaysAhead_thenReturnForecast() throws Exception {

        WeatherForecast forecast = new WeatherForecast();
        WeatherForecast.Daily daily = new WeatherForecast.Daily();
        daily.setTime(List.of("2025-04-10"));
        daily.setTemperature_2m_max(List.of(22.0));
        daily.setTemperature_2m_min(List.of(14.0));
        daily.setPrecipitation_probability_mean(List.of(30.0));
        daily.setWeathercode(List.of(3));
        forecast.setDaily(daily);

        
        when(weatherService.getForecast(eq("aveiro"), any(LocalDate.class)))
                .thenReturn(forecast);

      
        mockMvc.perform(get("/api/weather/aveiro?daysAhead=1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.daily.time[0]", is("2025-04-10")));

      
        verify(weatherService, times(1)).getForecast(eq("aveiro"), any(LocalDate.class));
    }

    @Test
    void whenGetCacheStats_thenReturnStats() throws Exception {
   
        Map<String, Integer> cacheStats = Map.of(
                "requests", 10,
                "hits", 7,
                "misses", 3
        );

        when(weatherService.getCacheStats()).thenReturn(cacheStats);

     
        mockMvc.perform(get("/api/weather/cache/stats")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.requests", is(10)))
                .andExpect(jsonPath("$.hits", is(7)))
                .andExpect(jsonPath("$.misses", is(3)));

        
        verify(weatherService, times(1)).getCacheStats();
    }
} 