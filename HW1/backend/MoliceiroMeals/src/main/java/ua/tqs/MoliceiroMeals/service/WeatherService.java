package ua.tqs.MoliceiroMeals.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import ua.tqs.MoliceiroMeals.dto.WeatherForecast;

import java.time.LocalDate;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class WeatherService {

    private final RestTemplate restTemplate = new RestTemplate();

    private final Map<String, Integer> hitCount = new ConcurrentHashMap<>();
    private final Map<String, Integer> missCount = new ConcurrentHashMap<>();

    public WeatherForecast getForecast(String location, LocalDate date) {
        WeatherForecast forecast = getForecastInternal(location, date);

        String key = location.toLowerCase() + "-" + date;
        if (forecast != null) {
            hitCount.put(key, hitCount.getOrDefault(key, 0) + 1);
        } else {
            missCount.put(key, missCount.getOrDefault(key, 0) + 1);
        }

        return forecast;
    }

    @Cacheable(value = "weatherCache", key = "#location + '-' + #date")
    public WeatherForecast getForecastInternal(String location, LocalDate date) {
        log.info("Calling weather API for location: {}", location);

        Map<String, double[]> locations = Map.of(
                "aveiro", new double[]{40.63, -8.65},
                "agueda", new double[]{40.57, -8.45},
                "oiro", new double[]{40.75, -8.57},
                "ilhavo", new double[]{40.40, -8.13}
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
            log.error("Erro ao obter previsão para {}: {}", location, e.getMessage());
            return null;
        }
    }


    public Map<String, Integer> getCacheStats() {
        return Map.of(
                "requests", getTotalRequests(),
                "misses", getCacheMisses(),
                "hits", getCacheHits()
        );
    }

    public int getTotalRequests() {
        return hitCount.values().stream().mapToInt(Integer::intValue).sum() + missCount.values().stream().mapToInt(Integer::intValue).sum();
    }

    public int getCacheHits() {
        return hitCount.values().stream().mapToInt(Integer::intValue).sum();
    }

    public int getCacheMisses() {
        return missCount.values().stream().mapToInt(Integer::intValue).sum();
    }
}