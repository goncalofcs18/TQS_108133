package tqs.lab4_4;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class BlazeDemoPage {

    private WebDriver driver;

    public BlazeDemoPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    @FindBy(name = "fromPort")
    private WebElement fromPort;

    @FindBy(name = "toPort")
    private WebElement toPort;

    @FindBy(css = "input[type='submit']")
    private WebElement findFlightsButton;

    public void selectFromPort(String city) {
        fromPort.sendKeys(city);
    }

    public void selectToPort(String city) {
        toPort.sendKeys(city);
    }

    public void clickFindFlights() {
        findFlightsButton.click();
    }
}
