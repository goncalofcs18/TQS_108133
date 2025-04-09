package ua.tqs.MoliceiroMeals.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReservationRequest {
    private String name;
    private String studentNumber;
    private Long mealId;
}
