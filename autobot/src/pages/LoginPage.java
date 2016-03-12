/**
 * 
 */
package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * @author prince
 *
 */
public class LoginPage extends ToutBaseMoldule {

	/**
	 * @param driver
	 */
	public LoginPage(WebDriver driver)
	{
		super(driver);
	}
	public void setLoginCredentials(String email, String pwd)
	{
		SendKeys(getElementByXpath(loginMap.getLocator("username")), email, "Email/UserName");
		SendKeys(getElementByXpath(loginMap.getLocator("password")), pwd, "Password");
	}
	public void submitLogin()
	{
		Click(loginMap.getLocator("submitlogin"), "Submit Button.");
		waitForLoading();
	}
	public HomePage loginAs(String uid, String pwd){
		this.setLoginCredentials(uid, pwd);
		this.submitLogin();
		logger.debug("Login Successfull !!!");
		return new HomePage(driver);
	}
	
	public boolean isUserExists (String userName) {
		Click(HomePageMap.getLocator("usersigninmenu"), "Button");
		String loginName = getElement(HomePageMap.getLocator("useremail")).getText();
		if (userName.equalsIgnoreCase(loginName)) {
			logger.info("Login Successfull with UserName: "+userName);
			return true;
		}
		else
			return false;
	}
	public boolean errorLogin (String error) {
		String loginerror = getText(loginMap.getLocator("loginerror"), "error message on the main page");
		if (error.equalsIgnoreCase(loginerror))
		{
			logger.info("Login Test Negative was Successfull");
			return true;
		}
		else
			return false;
	}
	
	
	

}

		//"xpathStartFreeTrial:"=.//div/ul[@id='mobile-advanced']/li[@id='menu-item-4190']/a/span[@class='avia-menu-text' and text()='Start FREE Trial']
		//"xpathUsername:"=//input[@id='user_session_email']
		//"xpathPassword:"=//input[@id='user_session_password']
		//"xpathSent:"=//strong[text()='Sent']
		//"xpathClickToCall:"=//div[@id='filter-select-area']/div[@class='cc-filter-select']/div[@class='pane jspScrollable']/div[@class='jspContainer']/div[@class='click-to-call']/div

