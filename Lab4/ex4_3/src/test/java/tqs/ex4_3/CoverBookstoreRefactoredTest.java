package tqs.ex4_3;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class CoverBookstoreRefactoredTest {

    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeEach
    public void setUp() {
        driver = new FirefoxDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(25));
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void testSearchHarryPotterWithRobustLocators() {
        driver.get("https://cover-bookstore.onrender.com");


        WebElement searchInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("[data-testid='book-search-input']")
        ));

        // Pesquisar
        searchInput.sendKeys("Harry Potter", Keys.ENTER);


        List<WebElement> bookItems = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(
                By.cssSelector("[data-testid='book-search-item']")
        ));

        // Verifica se algum dos livros corresponde ao título esperado
        boolean found = bookItems.stream()
                .anyMatch(item -> item.getText().contains("Harry Potter and the Sorcerer's Stone"));

        assertTrue(found, "O livro 'Harry Potter and the Sorcerer's Stone' não foi encontrado na pesquisa.");


        bookItems.stream()
                .filter(item -> item.getText().contains("Harry Potter and the Sorcerer's Stone"))
                .findFirst()
                .ifPresent(WebElement::click);
    }
}
