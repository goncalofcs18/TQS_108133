package ua.tqs.MoliceiroMeals.dto;

import lombok.Data;

import java.util.List;

@Data
public class WeatherForecast {
    private Daily daily;

    @Data
    public static class Daily {
        private List<String> time;
        private List<Double> temperature_2m_max;
        private List<Double> temperature_2m_min;
        private List<Double> precipitation_probability_mean;
        private List<Integer> weathercode; 
    }
}
