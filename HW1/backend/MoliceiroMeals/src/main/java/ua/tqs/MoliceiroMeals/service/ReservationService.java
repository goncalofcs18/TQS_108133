package ua.tqs.MoliceiroMeals.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ua.tqs.MoliceiroMeals.model.Meal;
import ua.tqs.MoliceiroMeals.model.Reservation;
import ua.tqs.MoliceiroMeals.repository.MealRepository;
import ua.tqs.MoliceiroMeals.repository.ReservationRepository;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final MealRepository mealRepository;

    public Reservation createReservation(String name, String studentNumber, Long mealId) {
        Meal meal = mealRepository.findById(mealId)
                .orElseThrow(() -> new IllegalArgumentException("Meal not found"));
        Reservation reservation = new Reservation(name, studentNumber, meal);
        return reservationRepository.save(reservation);
    }

    public Optional<Reservation> getReservationByToken(UUID token) {
        return reservationRepository.findByToken(token);
    }

    public boolean useReservation(UUID token) {
        Optional<Reservation> optional = reservationRepository.findByToken(token);
        if (optional.isPresent()) {
            Reservation r = optional.get();
            if (!r.isUsed()) {
                r.setUsed(true);
                reservationRepository.save(r);
                return true;
            }
        }
        return false;
    }

    public boolean cancelReservation(UUID token) {
        Optional<Reservation> optional = reservationRepository.findByToken(token);
        if (optional.isPresent() && !optional.get().isUsed()) {
            reservationRepository.delete(optional.get());
            return true;
        }
        return false;
    }
}
