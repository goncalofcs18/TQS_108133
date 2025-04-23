package tqs.lab5;

import static org.junit.jupiter.api.Assertions.*;

import io.cucumber.java.en.*;

public class CalculatorSteps {

    private RpnCalculator calculator;
    private Exception thrownException;

    @Given("a calculator I just turned on")
    public void a_calculator_I_just_turned_on() {
        calculator = new RpnCalculator();
        thrownException = null;
    }

    @When("I add {int} and {int}")
    public void i_add_and(Integer a, Integer b) {
        calculator.push(a);
        calculator.push(b);
        calculator.push("+");
    }

    @When("I subtract {int} and {int}")
    public void i_subtract_and(Integer a, Integer b) {
        calculator.push(a);
        calculator.push(b);
        calculator.push("-");
    }

    @When("I multiply {int} and {int}")
    public void i_multiply_and(Integer a, Integer b) {
        calculator.push(a);
        calculator.push(b);
        calculator.push("*");
    }

    @When("I divide {int} and {int}")
    public void i_divide_and(Integer a, Integer b) {
        try {
            calculator.push(a);
            calculator.push(b);
            calculator.push("/");
        } catch (Exception e) {
            thrownException = e;
        }
    }

    @Then("the result is {int}")
    public void the_result_is(Integer expected) {
        Number actual = calculator.value();
        assertEquals(expected, actual.intValue());
    }

    @Then("an error should occur")
    public void an_error_should_occur() {
        assertNotNull(thrownException, "Expected an exception to be thrown");
    }
}
