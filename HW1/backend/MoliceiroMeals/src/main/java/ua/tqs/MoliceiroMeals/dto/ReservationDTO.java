package ua.tqs.MoliceiroMeals.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class ReservationDTO {
    private UUID token;
}
