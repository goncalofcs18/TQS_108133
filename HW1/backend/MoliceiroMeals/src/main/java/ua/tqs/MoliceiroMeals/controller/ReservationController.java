package ua.tqs.MoliceiroMeals.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.tqs.MoliceiroMeals.dto.ReservationDTO;
import ua.tqs.MoliceiroMeals.dto.ReservationRequest;
import ua.tqs.MoliceiroMeals.model.Reservation;
import ua.tqs.MoliceiroMeals.service.ReservationService;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ReservationController {

    private final ReservationService reservationService;

    @PostMapping
    public ResponseEntity<ReservationDTO> createReservation(@RequestBody ReservationRequest request) {
        Reservation reservation = reservationService.createReservation(
                request.getName(), request.getStudentNumber(), request.getMealId());
        
        ReservationDTO dto = new ReservationDTO();
        dto.setToken(reservation.getToken());
        dto.setName(reservation.getName());
        dto.setStudentNumber(reservation.getStudentNumber());
        dto.setUsed(reservation.isUsed());
        dto.setMealId(reservation.getMeal().getId());
        dto.setMealDescription(reservation.getMeal().getDescription());
        dto.setRestaurantName(reservation.getMeal().getRestaurant().getName());
        
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/{token}")
    public ResponseEntity<?> getReservation(@PathVariable String token) {
        Optional<Reservation> reservationOpt = reservationService.getReservationByToken(UUID.fromString(token));
        
        if (reservationOpt.isPresent()) {
            Reservation reservation = reservationOpt.get();
            ReservationDTO dto = new ReservationDTO();
            dto.setToken(reservation.getToken());
            dto.setName(reservation.getName());
            dto.setStudentNumber(reservation.getStudentNumber());
            dto.setUsed(reservation.isUsed());
            dto.setMealId(reservation.getMeal().getId());
            dto.setMealDescription(reservation.getMeal().getDescription());
            dto.setRestaurantName(reservation.getMeal().getRestaurant().getName());
            
            return ResponseEntity.ok(dto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{token}/use")
    public ResponseEntity<?> useReservation(@PathVariable String token) {
        boolean success = reservationService.useReservation(UUID.fromString(token));
        return ResponseEntity.ok(Map.of("success", success));
    }

    @DeleteMapping("/{token}")
    public ResponseEntity<?> cancelReservation(@PathVariable String token) {
        boolean success = reservationService.cancelReservation(UUID.fromString(token));
        return ResponseEntity.ok(Map.of("success", success));
    }
}
