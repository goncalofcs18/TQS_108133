package tqs.lab4_4;

import tqs.lab4_4.BlazeDemoPage;
import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class BlazeDemoTest {

    private WebDriver driver;
    private BlazeDemoPage page;

    @BeforeEach
    public void setUp() {
        driver = new FirefoxDriver();
        driver.get("https://blazedemo.com");
        page = new BlazeDemoPage(driver);
    }

    @Test
    public void testFlightSearch() {
        page.selectFromPort("Boston");
        page.selectToPort("London");
        page.clickFindFlights();

        assertTrue(driver.getTitle().contains("BlazeDemo - reserve"), "A página de resultados não abriu corretamente.");
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
