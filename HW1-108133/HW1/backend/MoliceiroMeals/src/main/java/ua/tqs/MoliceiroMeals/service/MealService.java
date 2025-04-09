package ua.tqs.MoliceiroMeals.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ua.tqs.MoliceiroMeals.model.Meal;
import ua.tqs.MoliceiroMeals.model.Restaurant;
import ua.tqs.MoliceiroMeals.repository.MealRepository;
import ua.tqs.MoliceiroMeals.repository.RestaurantRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MealService {

    private final MealRepository mealRepo;
    private final RestaurantRepository restaurantRepo;

    public List<Meal> getMealsForRestaurant(Long restaurantId) {
        Optional<Restaurant> restaurantOpt = restaurantRepo.findById(restaurantId);
        if (restaurantOpt.isEmpty()) {
            throw new IllegalArgumentException("Restaurante não encontrado com ID: " + restaurantId);
        }
        return mealRepo.findByRestaurantAndDateAfter(restaurantOpt.get(), LocalDate.now().minusDays(1));
    }
}
