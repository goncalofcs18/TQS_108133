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

import static org.junit.jupiter.api.Assertions.assertTrue;

public class CoverBookstoreTest {

    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeEach
    public void setUp() {
        driver = new FirefoxDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void testSearchHarryPotter() {
        driver.get("https://cover-bookstore.onrender.com");

        // Esperar pelo input de pesquisa
        WebElement searchInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("input[data-testid='book-search-input']")
        ));

        // Escrever e pesquisar
        searchInput.sendKeys("Harry Potter", Keys.ENTER);

        // Esperar pelo título do livro específico
        WebElement bookTitle = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//span[contains(text(), \"Harry Potter and the Sorcerer's Stone\")]")
        ));

        // Validar que o título está visível
        assertTrue(bookTitle.isDisplayed(), "O título do livro não está visível.");

        // Clica no título
        bookTitle.click();

        // Teste acaba aqui — sucesso se chegou até aqui sem exceções
    }
}
