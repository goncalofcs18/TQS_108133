package ua.tqs.MoliceiroMeals.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ua.tqs.MoliceiroMeals.model.Meal;
import ua.tqs.MoliceiroMeals.service.MealService;

import java.util.List;

@RestController
@RequestMapping("/api/meals")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class MealController {

    private final MealService mealService;

    @GetMapping("/restaurant/{restaurantId}")
    public List<Meal> getMealsByRestaurant(@PathVariable Long restaurantId) {
        return mealService.getMealsForRestaurant(restaurantId);
    }
}