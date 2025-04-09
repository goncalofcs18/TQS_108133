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
        return ResponseEntity.ok(new ReservationDTO(reservation.getToken()));
    }

    @GetMapping("/{token}")
    public ResponseEntity<?> getReservation(@PathVariable String token) {
        Optional<Reservation> reservation = reservationService.getReservationByToken(UUID.fromString(token));
        return reservation.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/{token}/check-in")
    public ResponseEntity<?> useReservation(@PathVariable String token) {
        boolean success = reservationService.useReservation(UUID.fromString(token));
        return ResponseEntity.ok(Map.of("used", success));
    }

    @DeleteMapping("/{token}")
    public ResponseEntity<?> cancelReservation(@PathVariable String token) {
        boolean success = reservationService.cancelReservation(UUID.fromString(token));
        return ResponseEntity.ok(Map.of("cancelled", success));
    }
}
