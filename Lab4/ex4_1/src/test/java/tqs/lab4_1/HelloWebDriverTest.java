package tqs.lab4_1;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import io.github.bonigarcia.seljup.SeleniumJupiter;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.firefox.FirefoxDriver;
import io.github.bonigarcia.seljup.SeleniumJupiter;



import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SeleniumJupiter.class)
public class HelloWebDriverTest {

    @Test
    void testNavigateToSlowCalculator(FirefoxDriver driver) {
        driver.get("https://bonigarcia.dev/selenium-webdriver-java/");
        String title = driver.getTitle();
        assertTrue(title.contains("Selenium WebDriver"));

        WebElement slowCalcLink = driver.findElement(By.linkText("Slow calculator"));
        slowCalcLink.click();

        String currentUrl = driver.getCurrentUrl();
        assertTrue(currentUrl.contains("slow-calculator"));
    }

    @Test
    void testHomePageTitle(FirefoxDriver driver) {
        driver.get("https://bonigarcia.dev/selenium-webdriver-java/");
        String title = driver.getTitle();
        assertTrue(title.startsWith("Hands-On Selenium"));
    }
}
