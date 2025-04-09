package ua.tqs.MoliceiroMeals.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationDTO {
    private String name;
    private String studentNumber;
    private Long mealId;
    private UUID token;
    private boolean used;
    private String mealDescription;
    private String restaurantName;
}
