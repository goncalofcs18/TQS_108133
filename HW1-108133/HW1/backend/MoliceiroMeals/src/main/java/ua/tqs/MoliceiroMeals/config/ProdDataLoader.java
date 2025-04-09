package ua.tqs.MoliceiroMeals.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import ua.tqs.MoliceiroMeals.model.*;
import ua.tqs.MoliceiroMeals.repository.*;

import java.time.LocalDate;

@Configuration
@Profile("prod")
public class ProdDataLoader {

    @Bean
    CommandLineRunner loadProdData(RestaurantRepository restaurantRepo, MealRepository mealRepo, ReservationRepository reservationRepo) {
        return args -> {
            // Verificar se já existem dados
            if (restaurantRepo.count() == 0) {
                Restaurant r1 = restaurantRepo.save(new Restaurant(null, "Cantina de Santeodoro", "Campus de Santeodoro"));
                Restaurant r2 = restaurantRepo.save(new Restaurant(null, "Cantina do Castro", "Complexo do Castro"));
                Restaurant r3 = restaurantRepo.save(new Restaurant(null, "Restaurante Churrascos", "Campus de Santeodoro"));
                Restaurant r4 = restaurantRepo.save(new Restaurant(null, "Cantina ESGTA", "Campus de Águeda"));

                // Refeições para Santeodoro
                mealRepo.save(new Meal(null, LocalDate.now().plusDays(1), "Arroz de Pato", 50, r1));
                mealRepo.save(new Meal(null, LocalDate.now().plusDays(2), "Massada de Peixe", 50, r1));
                mealRepo.save(new Meal(null, LocalDate.now().plusDays(3), "Frango Assado", 50, r1));
                mealRepo.save(new Meal(null, LocalDate.now().plusDays(4), "Carne de Porco à Alentejana", 50, r1));
                mealRepo.save(new Meal(null, LocalDate.now().plusDays(5), "Feijoada de Seitan", 50, r1));

                // Refeições para Castro
                mealRepo.save(new Meal(null, LocalDate.now().plusDays(1), "Bacalhau à Brás", 50, r2));
                mealRepo.save(new Meal(null, LocalDate.now().plusDays(2), "Empadão de Carne", 50, r2));
                mealRepo.save(new Meal(null, LocalDate.now().plusDays(3), "Sopa de Alho Francês", 50, r2));
                mealRepo.save(new Meal(null, LocalDate.now().plusDays(4), "Grelhada Mista", 50, r2));
                mealRepo.save(new Meal(null, LocalDate.now().plusDays(5), "Massada de Marisco", 50, r2));

                // Refeições para Churrascos
                mealRepo.save(new Meal(null, LocalDate.now().plusDays(1), "Frango de Churrasco", 50, r3));
                mealRepo.save(new Meal(null, LocalDate.now().plusDays(2), "Porco no Espeto", 50, r3));
                mealRepo.save(new Meal(null, LocalDate.now().plusDays(3), "Churrasco Misto (Vaca, Frango, Porco)", 50, r3));
                mealRepo.save(new Meal(null, LocalDate.now().plusDays(4), "Leitão da Bairrada", 50, r3));
                mealRepo.save(new Meal(null, LocalDate.now().plusDays(8), "Dourada Grelhada ao Carvão", 50, r3));

                // Refeições ESGTA
                mealRepo.save(new Meal(null, LocalDate.now().plusDays(1), "Arroz de Frango", 50, r4));
                mealRepo.save(new Meal(null, LocalDate.now().plusDays(2), "Arroz de Marisco", 50, r4));
                mealRepo.save(new Meal(null, LocalDate.now().plusDays(3), "Esparguete à Bolonhesa", 50, r4));
                mealRepo.save(new Meal(null, LocalDate.now().plusDays(4), "Bacalhau à Brás", 50, r4));
                mealRepo.save(new Meal(null, LocalDate.now().plusDays(5), "Espetadas de Porco com Arroz", 50, r4));

                System.out.println("Dados de produção carregados com sucesso!");
            } else {
                System.out.println("Dados já existem no banco de dados de produção.");
            }
        };
    }
} 