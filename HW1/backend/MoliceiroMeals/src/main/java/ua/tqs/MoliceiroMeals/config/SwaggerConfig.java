package ua.tqs.MoliceiroMeals.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI moliceiroMealsOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("MoliceiroMeals API")
                        .description("API para gestão de refeições e reservas")
                        .version("1.0"));
    }
}
