package common;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;

public class ElementPresent implements ExpectedCondition<WebElement> {
	private final By locator;

    public ElementPresent(By locator) {
        this.locator = locator;
    }

    public WebElement apply(WebDriver driver) {
        return driver.findElement(locator);
    }
}