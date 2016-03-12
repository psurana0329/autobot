/**
 * 
 */
package test;

import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.thoughtworks.selenium.webdriven.commands.WaitForPageToLoad;

import common.CheckPoint;
import common.Constants;
import pages.ToutTestSuite;

/**
 * @author prince
 *
 */
public class loginpagetest extends ToutTestSuite{
	@BeforeClass(alwaysRun=true)
	public void setUp() {
	}
	@BeforeMethod(alwaysRun = true)
	public void login() {
	}
	@AfterMethod(alwaysRun=true)
	public void logout() {
	}
	
	@Test(priority=0)
	public void loginTestPositive() throws Exception {
		CheckPoint.mark("loginTestPositive", login.isUserExists(sysuser), "Login Test Positive Successful");
	}
	@Test(priority=1)
	public void loginTestNegative() throws Exception {
		home.logout();
		login.loginAs(sysuser, "kdfjls");
		login.submitLogin();
		CheckPoint.mark("loginTestNegative", login.errorLogin("Incorrect email or password."), "Login Test Negative Successful");
	}
	
}
