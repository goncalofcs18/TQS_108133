package ua.tqs.MoliceiroMeals.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.tqs.MoliceiroMeals.model.Reservation;

import java.util.Optional;
import java.util.UUID;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    Optional<Reservation> findByToken(UUID token);
}
