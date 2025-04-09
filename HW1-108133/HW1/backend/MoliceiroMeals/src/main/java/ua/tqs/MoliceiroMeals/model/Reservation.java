package ua.tqs.MoliceiroMeals.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String studentNumber;

    private UUID token;

    private boolean used;

    @ManyToOne
    @JoinColumn(name = "meal_id")
    private Meal meal;

    public Reservation(String name, String studentNumber, Meal meal) {
        this.name = name;
        this.studentNumber = studentNumber;
        this.meal = meal;
        this.token = UUID.randomUUID();
        this.used = false;
    }
}
