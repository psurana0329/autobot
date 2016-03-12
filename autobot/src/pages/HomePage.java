/**
 * 
 */
package pages;

import org.openqa.selenium.WebDriver;

import com.thoughtworks.selenium.webdriven.commands.WaitForPageToLoad;

/**
 * @author prince
 *
 */
public class HomePage extends ToutBaseMoldule{
	public HomePage(WebDriver driver) {
		super(driver);
		// TODO Auto-generated constructor stub
	}
	public LoginPage logout() {
		util.sleep(5000, "Waiting Before Logout");
		if (isElementPresent(HomePageMap.getLocator("logout")))
			Click(HomePageMap.getLocator("logout"), "Logout link");
		else {
			Click(HomePageMap.getLocator("usersigninmenu"), "Logout link");
			Click(HomePageMap.getLocator("logout"), "Logout link");
		}
		logger.info("Logging Out...");
		util.sleep(2000, "Waiting After Logout");
		waitForElementPresent(loginMap.getLocator("username"), 5, 20);
		return new LoginPage(driver);
	}
	// Navigate to SignIn Page
	public void NavigateToLoginPage(){
		Click(HomePageMap.getLocator("loginpage"), "clicked on the Login button.");
		waitForLoading();
	}
}
