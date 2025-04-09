package ua.tqs.MoliceiroMeals;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class MoliceiroMealsApplication {
	public static void main(String[] args) {

		SpringApplication.run(MoliceiroMealsApplication.class, args);
	}
}
