/**
 * 
 */
package pages;

import java.sql.Driver;

import org.openqa.selenium.WebDriver;

import common.ConfigReader;
import common.ElementMap;
import common.SelConfig;
import common.SeleniumDriver;

/**
 * @author prince
 *
 */
public class ToutBaseMoldule extends SeleniumDriver {
	public ToutBaseMoldule(WebDriver driver) {
		super(driver);
	}
	public ElementMap loginMap = new ElementMap("login");
	public ElementMap HomePageMap = new ElementMap("home");
	public String browser = ConfigReader.getConfiguration(SelConfig.BROWSER, SelConfig.BROWSER);
	public String touturl = ConfigReader.getConfiguration(SelConfig.TOUTURL, SelConfig.TOUTURL);
	public String toutAutoUser = ConfigReader.getConfiguration(SelConfig.SYS_USER, SelConfig.SYS_USER);
	public String toutAutoPassword = ConfigReader.getConfiguration(SelConfig.SYS_PWD, SelConfig.SYS_PWD);
	public void waitForLoading(long timeOut){
    	long timeToQuit = 0;
       	if( isElementPresent("//div[@class='overlay overlay-progress']") ) {
       		logger.info("AJAX Mask Appeared, Waiting to Disapper...");
    	}
       	long startTime = System.currentTimeMillis();
    	util.sleep(1000, "Waiting After For AJAX Operation");
	    while( isElementPresent("//div[@class='overlay overlay-progress']") || timeToQuit < timeOut ){
	       	util.sleep( 1000 );
    		timeToQuit = startTime + 2000;
    	}
    	long endTime = System.currentTimeMillis() - startTime;
    	logger.info("AJAX Loader Disappeared In : "+ endTime/1000+" Seconds");
    }
    /**
     * 
     */
    public void waitForLoading(){
    	this.waitForLoading(120000);
    }
    /**
     * 
     */
    public String getUserEmail()
    {
    	String email = getText(loginMap.getLocator("useremail"), "User Email on the Main page");
    	return email;
    }
}
