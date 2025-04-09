package ua.tqs.MoliceiroMeals.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import ua.tqs.MoliceiroMeals.dto.WeatherForecast;

import java.time.LocalDate;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Service
public class WeatherService {

    private final RestTemplate restTemplate = new RestTemplate();
    
    // Use atomic counters for thread safety
    private final AtomicInteger requestCount = new AtomicInteger(0);
    private final AtomicInteger cacheHitCount = new AtomicInteger(0);
    private final AtomicInteger cacheMissCount = new AtomicInteger(0);
    
    // Track what requests have been made for cache analysis
    private final ConcurrentHashMap<String, Boolean> requestCache = new ConcurrentHashMap<>();

    /**
     * Get weather forecast for a specific location and date
     * Utilizes caching for improved performance
     */
    public WeatherForecast getForecast(String location, LocalDate date) {
        String cacheKey = location.toLowerCase() + "-" + date;
        
        // Track if this request has been made before - used for cache hit/miss stats
        boolean existsInCache = requestCache.containsKey(cacheKey);
        
        // Increment total request count
        requestCount.incrementAndGet();
        
        // Get forecast (potentially from cache due to @Cacheable on internal method)
        WeatherForecast forecast = getForecastInternal(location, date);
        
        // Update cache statistics based on whether this was a cache hit or miss
        if (existsInCache) {
            // Cache hit - already requested this combination before
            cacheHitCount.incrementAndGet();
            log.debug("Cache HIT for {}", cacheKey);
        } else {
            // Cache miss - first time requesting this combination
            cacheMissCount.incrementAndGet();
            requestCache.put(cacheKey, true);
            log.debug("Cache MISS for {}", cacheKey);
        }
        
        return forecast;
    }

    /**
     * Internal method for fetching weather forecast
     * Uses Spring's @Cacheable for automatic caching
     */
    @Cacheable(value = "weatherCache", key = "#location.toLowerCase() + '-' + #date")
    protected WeatherForecast getForecastInternal(String location, LocalDate date) {
        log.info("Calling weather API for location: {} on date: {}", location, date);

        Map<String, double[]> locations = Map.of(
                "aveiro", new double[]{40.63, -8.65},
                "agueda", new double[]{40.57, -8.45},
                "oiro", new double[]{40.75, -8.57},
                "ilhavo", new double[]{60.40, -3.13}
        );

        double[] coords = locations.getOrDefault(location.toLowerCase(), new double[]{40.63, -8.65});

        String uri = UriComponentsBuilder.fromHttpUrl("https://api.open-meteo.com/v1/forecast")
                .queryParam("latitude", coords[0])
                .queryParam("longitude", coords[1])
                .queryParam("daily", "temperature_2m_max,temperature_2m_min,precipitation_probability_mean,weathercode")
                .queryParam("timezone", "Europe/Lisbon")
                .queryParam("start_date", date)
                .queryParam("end_date", date)
                .toUriString();

        try {
            return restTemplate.getForObject(uri, WeatherForecast.class);
        } catch (Exception e) {
            log.error("Error getting forecast for {}: {}", location, e.getMessage());
            return null;
        }
    }

    /**
     * Clear the weather cache
     */
    @CacheEvict(value = "weatherCache", allEntries = true)
    public void clearCache() {
        log.info("Clearing weather cache");
        requestCache.clear();
    }

    /**
     * Get cache statistics
     */
    public Map<String, Integer> getCacheStats() {
        return Map.of(
                "requests", requestCount.get(),
                "hits", cacheHitCount.get(),
                "misses", cacheMissCount.get()
        );
    }

    public int getTotalRequests() {
        return requestCount.get();
    }

    public int getCacheHits() {
        return cacheHitCount.get();
    }

    public int getCacheMisses() {
        return cacheMissCount.get();
    }
}