package ua.tqs.MoliceiroMeals.performance;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;
import ua.tqs.MoliceiroMeals.dto.WeatherForecast;
import ua.tqs.MoliceiroMeals.service.WeatherService;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class WeatherServicePerformanceTest {

    @InjectMocks
    private WeatherService weatherService;

    @Mock
    private RestTemplate restTemplate;
    
    private AtomicInteger apiCallCounter = new AtomicInteger(0);

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(weatherService, "restTemplate", restTemplate);
        
        ReflectionTestUtils.setField(weatherService, "requestCount", new AtomicInteger(0));
        ReflectionTestUtils.setField(weatherService, "cacheHitCount", new AtomicInteger(0));
        ReflectionTestUtils.setField(weatherService, "cacheMissCount", new AtomicInteger(0));
        ReflectionTestUtils.setField(weatherService, "requestCache", new ConcurrentHashMap<>());
        
        apiCallCounter.set(0);
        when(restTemplate.getForObject(anyString(), eq(WeatherForecast.class)))
                .thenAnswer(invocation -> {
                    // Count how many times the API is actually called
                    apiCallCounter.incrementAndGet();
                    
                    // Simulate network delay
                    TimeUnit.MILLISECONDS.sleep(100);
                    
                    return new WeatherForecast();
                });
    }

    @Test
    void testCachePerformanceWithSpringCacheable() {
        String location = "aveiro";
        LocalDate date = LocalDate.now();
        int iterations = 5;
        
     
        weatherService.getForecast(location, date);
        
       
        apiCallCounter.set(0); // Reset counter after first call
        
    
        Map<String, Integer> initialStats = weatherService.getCacheStats();
        int initialRequests = initialStats.get("requests");
        
        Instant start = Instant.now();
       
        for (int i = 0; i < iterations; i++) {
            weatherService.getForecast(location, date);
        }
        
        Instant end = Instant.now();
        long sameLocationDuration = Duration.between(start, end).toMillis();
        
     
        
        // Get updated stats
        Map<String, Integer> stats = weatherService.getCacheStats();
        int requestsAfterSameLocation = stats.get("requests") - initialRequests;
        int hits = stats.get("hits");
        
      
        assertThat(hits).isGreaterThan(0);
        
     
        apiCallCounter.set(0);
        start = Instant.now();
        

        for (int i = 0; i < iterations; i++) {
            weatherService.getForecast("location" + i, date);
        }
        
        end = Instant.now();
        long differentLocationsDuration = Duration.between(start, end).toMillis();
        

        assertThat(apiCallCounter.get()).isGreaterThan(0);
        

        System.out.println("Duration for " + iterations + " same location calls: " + sameLocationDuration + "ms");
        System.out.println("Duration for " + iterations + " different location calls: " + differentLocationsDuration + "ms");
        System.out.println("Cache hits: " + hits);
        
  
        stats = weatherService.getCacheStats();
        System.out.println("Final cache stats: " + stats);
        
      
        assertThat(stats.get("misses")).isGreaterThan(0);
    }
} 