package ua.tqs.MoliceiroMeals.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.tqs.MoliceiroMeals.model.Meal;
import ua.tqs.MoliceiroMeals.model.Reservation;
import ua.tqs.MoliceiroMeals.model.Restaurant;
import ua.tqs.MoliceiroMeals.repository.MealRepository;
import ua.tqs.MoliceiroMeals.repository.ReservationRepository;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private MealRepository mealRepository;

    @InjectMocks
    private ReservationService reservationService;

    @Test
    void whenCreateReservation_thenReservationCreated() {
        // Arrange
        String name = "John Doe";
        String studentNumber = "100000";
        Long mealId = 1L;
        
        Restaurant restaurant = new Restaurant();
        restaurant.setId(1L);
        restaurant.setName("Test Restaurant");
        restaurant.setLocation("Test Location");
        
        Meal meal = new Meal();
        meal.setId(mealId);
        meal.setDescription("Test Meal");
        meal.setDate(LocalDate.now());
        meal.setRestaurant(restaurant);
        
        Reservation reservation = new Reservation(name, studentNumber, meal);
        
        when(mealRepository.findById(mealId)).thenReturn(Optional.of(meal));
        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);
        
        // Act
        Reservation result = reservationService.createReservation(name, studentNumber, mealId);
        
        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo(name);
        assertThat(result.getStudentNumber()).isEqualTo(studentNumber);
        assertThat(result.getMeal()).isEqualTo(meal);
        assertThat(result.isUsed()).isFalse();
        assertThat(result.getToken()).isNotNull();
        
        verify(mealRepository).findById(mealId);
        verify(reservationRepository).save(any(Reservation.class));
    }
    
    @Test
    void whenCreateReservationWithInvalidMealId_thenThrowException() {
        // Arrange
        Long invalidMealId = 999L;
        when(mealRepository.findById(invalidMealId)).thenReturn(Optional.empty());
        
        // Act & Assert
        assertThatThrownBy(() -> 
            reservationService.createReservation("John Doe", "100000", invalidMealId)
        ).isInstanceOf(IllegalArgumentException.class)
         .hasMessageContaining("Meal not found");
        
        verify(mealRepository).findById(invalidMealId);
        verify(reservationRepository, never()).save(any());
    }
    
    @Test
    void whenGetReservationByToken_thenReturnReservation() {
        // Arrange
        UUID token = UUID.randomUUID();
        Reservation reservation = new Reservation();
        reservation.setToken(token);
        
        when(reservationRepository.findByToken(token)).thenReturn(Optional.of(reservation));
        
        // Act
        Optional<Reservation> result = reservationService.getReservationByToken(token);
        
        // Assert
        assertThat(result).isPresent();
        assertThat(result.get().getToken()).isEqualTo(token);
        
        verify(reservationRepository).findByToken(token);
    }
    
    @Test
    void whenUseReservation_thenMarkReservationAsUsed() {
        // Arrange
        UUID token = UUID.randomUUID();
        Reservation reservation = new Reservation();
        reservation.setToken(token);
        reservation.setUsed(false);
        
        when(reservationRepository.findByToken(token)).thenReturn(Optional.of(reservation));
        when(reservationRepository.save(any(Reservation.class))).thenAnswer(invocation -> invocation.getArgument(0));
        
        // Act
        boolean result = reservationService.useReservation(token);
        
        // Assert
        assertThat(result).isTrue();
        verify(reservationRepository).findByToken(token);
        verify(reservationRepository).save(any(Reservation.class));
    }
    
    @Test
    void whenUseAlreadyUsedReservation_thenReturnFalse() {
        // Arrange
        UUID token = UUID.randomUUID();
        Reservation reservation = new Reservation();
        reservation.setToken(token);
        reservation.setUsed(true);
        
        when(reservationRepository.findByToken(token)).thenReturn(Optional.of(reservation));
        
        // Act
        boolean result = reservationService.useReservation(token);
        
        // Assert
        assertThat(result).isFalse();
        verify(reservationRepository).findByToken(token);
        verify(reservationRepository, never()).save(any());
    }
    
    @Test
    void whenCancelReservation_thenDeleteReservation() {
        // Arrange
        UUID token = UUID.randomUUID();
        Reservation reservation = new Reservation();
        reservation.setToken(token);
        reservation.setUsed(false);
        
        when(reservationRepository.findByToken(token)).thenReturn(Optional.of(reservation));
        
        // Act
        boolean result = reservationService.cancelReservation(token);
        
        // Assert
        assertThat(result).isTrue();
        verify(reservationRepository).findByToken(token);
        verify(reservationRepository).delete(reservation);
    }
    
    @Test
    void whenCancelUsedReservation_thenReturnFalse() {
        // Arrange
        UUID token = UUID.randomUUID();
        Reservation reservation = new Reservation();
        reservation.setToken(token);
        reservation.setUsed(true);
        
        when(reservationRepository.findByToken(token)).thenReturn(Optional.of(reservation));
        
        // Act
        boolean result = reservationService.cancelReservation(token);
        
        // Assert
        assertThat(result).isFalse();
        verify(reservationRepository).findByToken(token);
        verify(reservationRepository, never()).delete(any());
    }
} 