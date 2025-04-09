package ua.tqs.MoliceiroMeals.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ua.tqs.MoliceiroMeals.model.Restaurant;
import ua.tqs.MoliceiroMeals.repository.RestaurantRepository;

import java.util.List;

@RestController
@RequestMapping("/api/restaurants")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class RestaurantController {

    private final RestaurantRepository restaurantRepo;

    @GetMapping
    public List<Restaurant> getAllRestaurants() {
        return restaurantRepo.findAll();
    }
}