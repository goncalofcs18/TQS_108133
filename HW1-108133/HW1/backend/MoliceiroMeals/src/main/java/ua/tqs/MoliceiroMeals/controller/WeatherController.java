package ua.tqs.MoliceiroMeals.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ua.tqs.MoliceiroMeals.dto.WeatherForecast;
import ua.tqs.MoliceiroMeals.service.WeatherService;

import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/api/weather")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class WeatherController {

    private final WeatherService weatherService;

    @GetMapping("/{location}")
    public WeatherForecast getWeather(@PathVariable String location,
                                      @RequestParam(defaultValue = "0") int daysAhead) {
        return weatherService.getForecast(location, LocalDate.now().plusDays(daysAhead));
    }

    @GetMapping("/cache/stats")
    public Map<String, Integer> getStats() {
        return weatherService.getCacheStats();
    }
}