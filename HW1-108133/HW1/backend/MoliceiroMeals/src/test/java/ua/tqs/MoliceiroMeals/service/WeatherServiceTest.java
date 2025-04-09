package ua.tqs.MoliceiroMeals.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;
import ua.tqs.MoliceiroMeals.dto.WeatherForecast;

import java.time.LocalDate;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class WeatherServiceTest {

    @InjectMocks
    private WeatherService weatherService;

    @Mock
    private RestTemplate restTemplate;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(weatherService, "restTemplate", restTemplate);
        
        // Reset the counters before each test
        ReflectionTestUtils.setField(weatherService, "requestCount", new AtomicInteger(0));
        ReflectionTestUtils.setField(weatherService, "cacheHitCount", new AtomicInteger(0));
        ReflectionTestUtils.setField(weatherService, "cacheMissCount", new AtomicInteger(0));
        ReflectionTestUtils.setField(weatherService, "requestCache", new ConcurrentHashMap<>());
    }

    @Test
    void whenGetForecast_thenReturnCorrectData() {
        // Arrange
        String location = "aveiro";
        LocalDate date = LocalDate.now();
        WeatherForecast mockForecast = new WeatherForecast();
        
        when(restTemplate.getForObject(anyString(), eq(WeatherForecast.class)))
            .thenReturn(mockForecast);
        
        // Act
        WeatherForecast result = weatherService.getForecast(location, date);
        
        // Assert
        assertThat(result).isEqualTo(mockForecast);
        verify(restTemplate, times(1)).getForObject(anyString(), eq(WeatherForecast.class));
        
        // First request should be a cache miss
        Map<String, Integer> stats = weatherService.getCacheStats();
        assertThat(stats.get("hits")).isEqualTo(0);
        assertThat(stats.get("misses")).isEqualTo(1);
        assertThat(stats.get("requests")).isEqualTo(1);
    }

    @Test
    void whenGetForecastCalledTwiceForSameLocation_thenSecondCallIsCacheHit() {
        // Arrange
        String location = "aveiro";
        LocalDate date = LocalDate.now();
        WeatherForecast mockForecast = new WeatherForecast();
        
        when(restTemplate.getForObject(anyString(), eq(WeatherForecast.class)))
            .thenReturn(mockForecast);
        
        // Act - first call (should be cache miss)
        weatherService.getForecast(location, date);
        
        // Act - second call (should be cache hit)
        WeatherForecast result = weatherService.getForecast(location, date);
        
        // Assert
        assertThat(result).isNotNull();
        
        // Check stats - should be 1 hit, 1 miss, 2 total requests
        Map<String, Integer> stats = weatherService.getCacheStats();
        assertThat(stats.get("requests")).isEqualTo(2); // Two total requests
        assertThat(stats.get("hits")).isEqualTo(1);     // Second call was a cache hit
        assertThat(stats.get("misses")).isEqualTo(1);   // First call was a cache miss
    }

    @Test
    void whenGetForecastForDifferentLocations_thenBothAreCacheMisses() {
        // Arrange
        String location1 = "aveiro";
        String location2 = "agueda";
        LocalDate date = LocalDate.now();
        WeatherForecast forecast1 = new WeatherForecast();
        WeatherForecast forecast2 = new WeatherForecast();
        
        when(restTemplate.getForObject(contains("latitude=40.63"), eq(WeatherForecast.class)))
            .thenReturn(forecast1);
        when(restTemplate.getForObject(contains("latitude=40.57"), eq(WeatherForecast.class)))
            .thenReturn(forecast2);
        
        // Act
        WeatherForecast result1 = weatherService.getForecast(location1, date);
        WeatherForecast result2 = weatherService.getForecast(location2, date);
        
        // Assert
        assertThat(result1).isEqualTo(forecast1);
        assertThat(result2).isEqualTo(forecast2);
        
        // Different locations should result in different API calls (2 misses)
        Map<String, Integer> stats = weatherService.getCacheStats();
        assertThat(stats.get("requests")).isEqualTo(2);
        assertThat(stats.get("hits")).isEqualTo(0);
        assertThat(stats.get("misses")).isEqualTo(2);
        
        // Calling the same locations again should result in cache hits
        weatherService.getForecast(location1, date);
        weatherService.getForecast(location2, date);
        
        stats = weatherService.getCacheStats();
        assertThat(stats.get("requests")).isEqualTo(4); // Four total requests
        assertThat(stats.get("hits")).isEqualTo(2);     // Two cache hits
        assertThat(stats.get("misses")).isEqualTo(2);   // Two cache misses
    }

    @Test
    void whenApiCallFails_thenReturnNullAndCountAsMiss() {
        // Arrange
        String location = "aveiro";
        LocalDate date = LocalDate.now();
        
        when(restTemplate.getForObject(anyString(), eq(WeatherForecast.class)))
            .thenThrow(new RuntimeException("API Error"));
        
        // Act
        WeatherForecast result = weatherService.getForecast(location, date);
        
        // Assert
        assertThat(result).isNull();
        
        // Failed calls are counted as API calls (misses)
        Map<String, Integer> stats = weatherService.getCacheStats();
        assertThat(stats.get("requests")).isEqualTo(1);
        assertThat(stats.get("hits")).isEqualTo(0);
        assertThat(stats.get("misses")).isEqualTo(1);
    }
    
    @Test
    void whenCacheIsCleared_thenSubsequentCallsAreMisses() {
        // Arrange
        String location = "aveiro";
        LocalDate date = LocalDate.now();
        WeatherForecast mockForecast = new WeatherForecast();
        
        when(restTemplate.getForObject(anyString(), eq(WeatherForecast.class)))
            .thenReturn(mockForecast);
        
        // First call - should be a miss
        weatherService.getForecast(location, date);
        
        // Second call - should be a hit
        weatherService.getForecast(location, date);
        
        // Clear the cache
        weatherService.clearCache();
        
        // Third call - should be a miss again
        weatherService.getForecast(location, date);
        
        // Assert stats reflect proper cache behavior
        Map<String, Integer> stats = weatherService.getCacheStats();
        assertThat(stats.get("requests")).isEqualTo(3); // Three total requests
        assertThat(stats.get("hits")).isEqualTo(1);    // One hit (2nd call)
        assertThat(stats.get("misses")).isEqualTo(2);  // Two misses (1st and 3rd calls)
    }
} 