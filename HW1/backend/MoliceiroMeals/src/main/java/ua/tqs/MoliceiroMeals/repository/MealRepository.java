package ua.tqs.MoliceiroMeals.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.tqs.MoliceiroMeals.model.Meal;
import ua.tqs.MoliceiroMeals.model.Restaurant;

import java.time.LocalDate;
import java.util.List;

public interface MealRepository extends JpaRepository<Meal, Long> {
    List<Meal> findByRestaurantAndDateAfter(Restaurant restaurant, LocalDate date);
}
