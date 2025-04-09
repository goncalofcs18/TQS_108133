Feature: Meal Reservation
  As a student
  I want to reserve meals at university restaurants
  So that I can have guaranteed food service

  Scenario: Successfully make a reservation
    Given I am on the main page
    When I select restaurant "Cantina de Santeodoro"
    And I select a meal from the available options
    And I enter my name "John Doe" and student number "100000"
    And I submit the reservation
    Then I should see a confirmation with a reservation code
    And the reservation should be saved in the system

  Scenario: Try to use an already used reservation
    Given I have a used reservation with token "12345678-1234-1234-1234-123456789012"
    When I try to use the reservation
    Then I should see a message that the reservation is already used 