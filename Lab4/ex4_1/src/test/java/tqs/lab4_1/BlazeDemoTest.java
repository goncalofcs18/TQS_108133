package tqs.lab4_2;

import static org.junit.jupiter.api.Assertions.assertEquals;

import io.github.bonigarcia.seljup.SeleniumJupiter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.firefox.FirefoxDriver;


@ExtendWith(SeleniumJupiter.class)
public class BlazeDemoTest {

    @Test
    void testFlightPurchase(FirefoxDriver driver) {
        driver.get("https://blazedemo.com");

        // Selecionar origem e destino
        new Select(driver.findElement(By.name("fromPort"))).selectByVisibleText("Boston");
        new Select(driver.findElement(By.name("toPort"))).selectByVisibleText("London");
        driver.findElement(By.cssSelector("input[type='submit']")).click();

        // Escolher o voo
        driver.findElement(By.cssSelector("input.btn.btn-small")).click();

        // Preencher dados do formulário
        driver.findElement(By.id("inputName")).sendKeys("Goncalo");
        driver.findElement(By.id("address")).sendKeys("1234 Rua");
        driver.findElement(By.id("city")).sendKeys("Aveiro");
        driver.findElement(By.id("state")).sendKeys("Aveiro");
        driver.findElement(By.id("zipCode")).sendKeys("12345");
        new Select(driver.findElement(By.id("cardType"))).selectByVisibleText("Visa");
        driver.findElement(By.id("creditCardNumber")).sendKeys("123456789");
        driver.findElement(By.id("nameOnCard")).sendKeys("Goncalo Sousa");
        driver.findElement(By.cssSelector(".control-group:nth-child(10) > .controls")).click();
        driver.findElement(By.cssSelector(".btn-primary")).click();

        // Verificar o título da página de confirmação
        String confirmationTitle = driver.getTitle();
        assertEquals("BlazeDemo Confirmation", confirmationTitle);
    }
}
