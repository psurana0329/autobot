package common;
import org.openqa.selenium.WebDriver;
public class DriverSession {
	private static WebDriver driver;
	/**
	 * 
	 * @return
	 */
	public  static WebDriver getDriver() {
		return driver;
	}
	public  static void setDriver(WebDriver driver) {
		DriverSession.driver = driver;
	}
}
