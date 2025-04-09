package ua.tqs.MoliceiroMeals.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.tqs.MoliceiroMeals.model.Restaurant;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
}
