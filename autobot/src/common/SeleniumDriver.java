/**
 * 
 */
package common;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import com.google.common.base.Predicate;

/**
 * @author vuyyus1
 *
 */
public abstract class SeleniumDriver {
	protected WebDriver   driver;
	public    Logger      logger   = LoggerFactory.getLogger();
	protected Util        util     = new Util();
	public String 		  browser = ConfigReader.getConfiguration(SelConfig.BROWSER,
													SelConfig.BROWSER);
	
	/*
	 * Constructor injecting the WebDriver interface
	 * 
	 * @param driver
	 */
	public SeleniumDriver(WebDriver driver) {
		this.driver =	driver;
	}
    /**
     * 
     * @return
     */
	public WebDriver getWebDriver() {
		return driver;
	}
	/**
	 * Do refresh the current browser session
	 */
	public void refresh(){
		driver.navigate().refresh();
		logger.info("The Current Browser location was Refreshed...");
		//util.sleep(3000, "The Current Browser location was Refreshed...");
	}
	public String getBrowserTitle() {
		return driver.getTitle();
	}
	/**
	 * 
	 * @return
	 */
	public String getBrowserURL() {
		return driver.getCurrentUrl();
	}
	/**
	 * 
	 * @param issueType
	 */
	public void screenShot(String issueType) {
		String fileName = System.currentTimeMillis()+"."+issueType+".png";
		File screenshotDir =	new File(ConfigReader.getScreenDir()+"//screencapture");
		screenshotDir.mkdir();
		if ( fileName.contains("/") ) {
			fileName = fileName.replaceAll("/", "-");
			logger.info(fileName);
		}
		try {
			WebDriver augmentedDriver = new Augmenter().augment(DriverSession.getDriver());
			File screenshot = ((TakesScreenshot)augmentedDriver).
                            getScreenshotAs(OutputType.FILE);
			FileUtils.copyFile(screenshot, new File(screenshotDir+"//"+fileName));
			logger.info("Screen Shot Was Strored at : "+screenshotDir+"/"+fileName);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * Click Wrapper for WebDriver with information about Element
	 * @param element - WebElement to perform Click operation
	 * @param info - information about element
	 */
	public Boolean Click(WebElement element, String info){
		return this.Click(element, info, 0);		
	}
	/**
	 * 
	 * @param locator
	 * @param info
	 * @return
	 */
	public Boolean Click(String locator, String info){
		WebElement element = this.getElement(locator);
		return this.Click(element, info, 0);		
	}
	/**
	 * 
	 * @param script
	 * @return
	 */
	public int executeJavaScript(String script){
		long itemNum = -1;
		//Java Script Execution
		try{
			itemNum = (Long) ((JavascriptExecutor)driver).executeScript("{"+script+"}");
		} catch (Throwable t) {
			logger.info("Exception occurred, returning itemNum=-1");
			return (int) itemNum;
		}
		return (int) itemNum;
	}
	/**
	 * 
	 * @param locator
	 * @param info
	 * @param timeToWait
	 * @return
	 */
	public Boolean Click(String locator, String info, long timeToWait) {
		WebElement element = this.getElement(locator);
		return this.Click(element, info, timeToWait);
	}
	public void DoubleClick(WebElement element, String info) {
		Actions action = new Actions(driver);
		action.doubleClick(element);
		logger.debug("Double Clicked On :: "+info);
		action.perform();
	}
	/**
	 * Right Click a WebElement and select the option
	 * @param element
	 * @param Item
	 */
	public void selectItemRightClick(WebElement element, String item) {
		Actions action = new Actions(driver);
		action.contextClick(element).build().perform();
		WebElement itemElement = getElement(item);
		Click(itemElement, "Selected Item");
	}
	/**
	 * 
	 * @param key
	 */
	public void keyPress(Keys key) {
		Actions action = new Actions(driver);
		action.keyDown(key);
		action.perform();
	}
	/**
	 * 
	 * @param script
	 * @return
	 */
	public WebElement executeJavaScriptForWebElement(String script){
		WebElement element = null;
		//Java Script Execution
		try{
			element = (WebElement) ((JavascriptExecutor)driver).executeScript("{"+script+"}");
		} catch (Throwable t) {
			logger.info("Exception occurred, returning WebElement = NULL");
			return element;
		}
		return element;
	}
	public Boolean Click(WebElement element, int xOrd, int yOrd, String info) {
		if (element != null) {
			try {
				Actions action = new Actions(driver);
				Point pt = element.getLocation();
				//action.moveByOffset(pt.getX()-150, pt.getY()-120).click().perform();;
				logger.info("CSS PADDING "+element.getCssValue("padding-left"));
			    //action.moveToElement(element, xOffset, yOffset)
				logger.debug("Clicked On :: "+info+" At Coordinates "+xOrd+", "+yOrd);
				return true;
			} catch (NoSuchElementException e){
				screenShot(Constants.ERROR);
				return false;
			}
		}else {
			return false;
		}
	}
	
	/**
	 * Click Wrapper for WebDriver with information about Element and
	 * time to wait in seconds after click 
	 * @param element - WebElement to perform Click operation
	 * @param info - information about element
	 */
	public Boolean Click(WebElement element, String info, long timeToWait){
		//WebDriverWait wait = new WebDriverWait(driver, 30);
       // wait.until(ExpectedConditions.visibilityOf(element));
		if (element != null) {
			try {
			element.click();
			if(timeToWait == 0) {
				logger.debug("Clicked On :: "+info);
			    util.sleep(1000);
			} else
				util.sleep(timeToWait, "Clicked On :: "+info);
			return true;
			} catch (NoSuchElementException e){
				screenShot(Constants.ERROR);
				return false;
			}
		}else {
			return false;
		}
	}
	
	/**
	 * Click Wrapper for WebDriver with information about Element and
	 * time to wait in seconds after click 
	 * @param element - WebElement to perform Click operation
	 * @param info - information about element
	 */
	public Boolean SendKeys(WebElement element, String data, String info, Boolean clear){
		if (element != null) {
			if(clear)
				element.clear();
			util.sleep(1000, "Waiting Before Entering Data");
			element.sendKeys(data);
			logger.debug("Send Keys On Element :: "+info+" With Data : "+data);
			return true;
		}else
			return false;
	}
	/**
	 * 
	 * @param locator
	 * @param data
	 * @param info
	 * @param clear
	 * @return
	 */
	public Boolean SendKeys(String locator, String data, String info, Boolean clear){
		WebElement element = this.getElement(locator);
		return this.SendKeys(element, data, info, clear);
	}
	/**
	 * 
	 * @param element
	 * @param data
	 * @param info
	 * @return
	 */
	public Boolean SendKeys(WebElement element, String data, String info){
		return this.SendKeys(element, data, info, false);
	}
	/**
	 * 
	 * @param element
	 * @param data
	 * @param info
	 * @return
	 */
	public void SendKeys(WebElement element, Keys key){
		element.sendKeys(key);
	}
	/**
	 * Send keys with element
	 * @param element
	 * @param key
	 */
	public void SendKeys(String locator , Keys key){
		WebElement element = this.getElement(locator);
		element.sendKeys(key);
	}
	/**
	 * 
	 * @param locator
	 * @param data
	 * @param info
	 * @return
	 */
	public Boolean SendKeys(String locator, String data, String info){
		WebElement element = this.getElement(locator);
		return this.SendKeys(element, data, info, false);
	}
	/**
	 * Click Wrapper for WebDriver with information about Element and
	 * time to wait in seconds after click 
	 * @param element - WebElement to perform Click operation
	 * @param info - information about element
	 */
	public String getText(WebElement element, String info){
		String text = null;
		if (element != null) {
			text = element.getText();
			if( text.length() != 0)
				logger.debug("Getting Text On Element :: "+info+" The Text is : "+text);
			else
				text = Constants.NOT_FOUND;
		}else 
			return null;
		return text.trim();
	}
	/**
	 * 
	 * @param element
	 * @param info
	 * @return
	 */
	public String getText(String locator, String info){
		WebElement element = this.getElement(locator);
		return this.getText(element, info);
	}
	/**
	 * 
	 * @param element
	 * @param info
	 * @return
	 */
	public Boolean isEnabled(WebElement element, String info) {
		Boolean enabled = false;
		if (element != null){
			enabled = element.isEnabled();
			if(enabled)
				logger.info("Element :: "+info+" Is Enabled");
			else
				logger.info("Element :: "+info+" Is Disabled");
		}
		return enabled;	
	}
	public Boolean isEnabled(String locator, String info) {
		WebElement element = getElement(locator);
		return this.isEnabled(element, info);
	}
	/**
	 * 
	 * @param element
	 * @param info
	 * @return
	 */
	public Boolean isDisplayed(WebElement element, String info) {
		Boolean displayed = false;
		if (element != null){
			displayed = element.isDisplayed();
			if(displayed)
				logger.info("Element :: "+info+" Is Displayed");
			else
				logger.info("Element :: "+info+" Is NOT Displyed");
		}
		return displayed;	
	}
	public Boolean isDisplayed(String locator, String info) {
		WebElement element = getElement(locator);
		return this.isDisplayed(element, info);
	}
	/**
	 * 
	 * @param element
	 * @param info
	 * @return
	 */
	public Boolean isSelected(WebElement element, String info) {
		Boolean selected = false;
		if (element != null){
			selected = element.isSelected();
			if(selected)
				logger.debug("Element :: "+info+" Is "+selected);
			else
				logger.debug("Element :: "+info+" Is "+selected);
		}
		return selected;	
	}
	/**
	 * Checks if a Checkbox is selected or not
	 * @param element
	 * @return
	 */
	public Boolean isSelected (WebElement element) {
		Boolean selected = false;
		if (element != null) {
			String style = element.getAttribute("style");
			String[] styl = style.split(" ");
			for (String pixel : styl){
				if (pixel.equals("-670px")) {
					selected = true;
					logger.debug ("Element is" +selected);
					break;
				}
			}
		}
		return selected;
			
		}
	
	/**
	 * 
	 * @param locator
	 * @param info
	 * @return
	 */
	public Boolean isSelected(String locator, String info) {
		WebElement element = getElement(locator);
		return isSelected(element, info);
	}
	
	/**
	 * Selects a check box irrespective of its state  
	 * 
	 * @param WebElement
	 * @param info
	 */
	public void Check(WebElement element, String info) {
		if(!isSelected(element, info)){
			Click(element, info);
			logger.debug("Element :: "+info+" Is Checked");
		}else
			logger.debug("Element :: "+info+" Is Already Checked");
	}
	/**
	 * Selects a checkbox
	 * @param element
	 */
	public void Check (WebElement element) {
		if(!isSelected(element)) {
			Click(element, "Click on checkbox");
			logger.debug("Element :: Is Checked");
		} else {
			logger.debug("Element :: Is Already Checked");
		}
	}
	
	/**
	 * Selects a check box irrespective of its state, using locator
	 * 
	 * @param locator
	 * @param info
	 * @return
	 */
	public void Check(String locator, String info) {
		WebElement element = getElement(locator);
		Check(element, info);
	}
	
	/**
	 * UnSelects a check box irrespective of its state
	 * 
	 * @param WebElement
	 * @param info
	 * @return
	 */
	public void UnCheck(WebElement element, String info) {
		if(isSelected(element, info)){
			Click(element, info);
			logger.debug("Element :: "+info+" Is UnChecked");
		}else
			logger.debug("Element :: "+info+" Is Already UnChecked");
	}
	
	/**
	 * UnSelects a check box irrespective of its state, using locator
	 * 
	 * @param locator
	 * @param info
	 * @return
	 */
	public void UnCheck(String locator, String info) {
		WebElement element = getElement(locator);
		UnCheck(element, info);
	}
	
	/**
	 * 
	 * @param element
	 * @param info
	 * @return
	 */
	public Boolean Submit(WebElement element, String info) {
		if (element != null){
			element.submit();
		    logger.info("Element :: "+info+" Was Submitted");
		    return true;
		}else
			return false;	
	}
	/**
	 * Builds The WebElement By given locator strategy
	 * @param locator - locator strategy, id, name=sample, css=#sample, 
	 * 					tag=div, //[@id=sample], link=sample
	 * @return WebElement
	 */
	public WebElement getElement(String locator){
		WebElement e = null;
		try{
				if(locator.startsWith("//")) {	
					e = driver.findElement(By.xpath(locator));				
				}else if( locator.startsWith("css=")) {
					locator=locator.substring(4);
					e = driver.findElement(By.cssSelector(locator));
				}else if( locator.startsWith("class=")) {
					locator = locator.split("\\=")[1];
					e = driver.findElement(By.className(locator));
				}else if( locator.startsWith("name=")) {
					locator = locator.split("\\=")[1];
					e = driver.findElement(By.name(locator));
				}else if( locator.startsWith("link=")) {
					locator = locator.split("\\=")[1];
					e = driver.findElement(By.linkText(locator));
				}else if( locator.startsWith("tag=")) {
					locator = locator.split("\\=")[1];
					e = driver.findElement(By.tagName(locator));
				}else
					e = driver.findElement(By.id(locator));
					logger.develop("ELEMENT FOUND WITH LOCATOR : "+locator);
		}catch(Exception ex) {
				logger.info("ELEMENT NOT FOUND WITH LOCATOR : "+locator);
		        ex.printStackTrace();
		        return e;
		}
		return e;		
	}
	/**
	 * Builds The List of WebElements By given locator strategy
	 * @param locator - locator strategy, id, name=sample, tag=div, link=sample
	 * @return WebElement
	 */
	public List<WebElement> getElements(String locator){
		List<WebElement> e = null;
		try{
				if( locator.startsWith("class=")) {
					locator = locator.split("\\=")[1];
					e = driver.findElements(By.className(locator));
				}else if( locator.startsWith("name=")) {
					locator = locator.split("\\=")[1];
					e = driver.findElements(By.name(locator));
				}else if( locator.startsWith("link=")) {
					locator = locator.split("\\=")[1];
					e = driver.findElements(By.linkText(locator));
				}else if( locator.startsWith("tag=")) {
					locator = locator.split("\\=")[1];
					e = driver.findElements(By.tagName(locator));
			} else if (locator.startsWith("//")) {
				e = driver.findElements(By.xpath(locator));
				}else
					e = driver.findElements(By.id(locator));
				logger.develop("ELEMENT FOUND WITH LOCATOR : "+locator);
		}catch(Exception ex) {
				logger.info("ELEMENT NOT FOUND WITH LOCATOR : "+locator);
		        ex.printStackTrace();
		        return e;
		}
		return e;		
	}
	/**
	 * Returns the WebElement By id
	 * @param id - Element HTML id
	 * @return WebElement whose HTML is is id
	 */
	public WebElement getElementById(String id){
		WebElement e;
		try{
			e = driver.findElement(By.id(id));
			logger.debug("ELEMENT FOUND WITH ID : "+id);
		}catch(Exception ex) {
			logger.info("ELEMENT NOT FOUND WITH ID : "+id);
			//screenShot(Constants.ERROR);
	        ex.printStackTrace();
	        return null;
		}
		return e;
	}
	/**
	 * Returns list of Elements by id
	 * @param id
	 * @return
	 */
	public List<WebElement> getElementsById(String id){
		return driver.findElements(By.id(id));
	}
	/**
	 * Returns the WebElement By Xpath
	 * @param xpath - xpath of the element
	 * @return
	 */
	public WebElement getElementByXpath(String xpath){
		return this.getElementByXpath(null, xpath);
	}
	/**
	 * Returns the WebElement By Xpath and parentId
	 * @param xpath - xpath of the element
	 * @return
	 */
	public WebElement getElementByXpath(String parentId, String xpath){
		WebElement e = null;
		try {
			if (parentId != null) {
				e = this.getElementById(parentId).findElement(By.xpath(xpath));
			} else
				e = driver.findElement(By.xpath(xpath));
			return e;
		} catch (NoSuchElementException ex) {
			screenShot(Constants.ERROR);
			return null;
		}
	}
	/**
	 * Returns the WebElement By Class Name with Parent Id
	 * @param parentId
	 * @param tagName
	 * @return
	 */
	public WebElement getElementByClassName(String parentId, String className){
		WebElement e = null;
		try{
			if (parentId != null){
				e = this.getElementById(parentId).findElement(By.className(className));
			}else
				e = driver.findElement(By.className(className));
			return e;
		}catch(NoSuchElementException ex){
			//screenShot(Constants.ERROR);
			return null;
		}
	}
	/**
	 * 
	 * @param className
	 * @return
	 */
	public WebElement getElementByClassName(String className){
		return this.getElementByClassName(null, className);
	}
	/**
	 * Returns the WebElement By Tag Name with Parent Id
	 * @param parentId
	 * @param tagName
	 * @return
	 */
	public WebElement getElementByTagName(String parentId, String tagName){
		WebElement e = null;
		try{
			if (parentId != null){
				e = this.getElementById(parentId).findElement(By.tagName(tagName));
			}else{
				e = driver.findElement(By.tagName(tagName));
			}
		}catch(Exception ex){
			logger.info("Element Was not presented...");
		}
		return e;
	}
	/**
	 * Returns the WebElement By TagName
	 * @param tagName
	 * @return
	 */
	public WebElement getElementByTagName(String tagName){
		return this.getElementByTagName(null, tagName);
	}
	/**
	 * 
	 * @param tagName
	 * @return
	 */
	public List<WebElement> getElementsByTagName(String parentId, String tagName){
		List<WebElement> elements = null;
		if (parentId !=null)
			elements = this.getElementById(parentId).findElements(By.tagName(tagName));
		else
			elements = driver.findElements(By.tagName(tagName));
		/*for(int i=0; i < elements.size(); i++){
			String text = elements.get(i).getText();
			if(text.length() != 0)
				logger.develop("Element Text "+i+": "+elements.get(i).getText());
		}*/
		return elements;
	}
	/**
	 * 
	 * @param tagName
	 * @return
	 */
	public List<WebElement> getElementsByTagName(String tagName){
		return this.getElementsByTagName(null, tagName);
	}
	/**
	 * 
	 * @param cssSelector
	 * @return
	 */
	public WebElement getElementByCSS(String cssSelector){
		return driver.findElement(By.cssSelector(cssSelector));
	}
	/**
	 * 
	 * @param parentId
	 * @param linkText
	 * @return
	 */
	public WebElement getElementByLink(String parentId, String linkText){
		WebElement e = null;
		if (parentId != null){
			e = this.getElementById(parentId).findElement(By.linkText(linkText));
		}else{
			e = driver.findElement(By.linkText(linkText));
		}
		return e;
	}
	/**
	 * Get WebElement Using Class And Link Text
	 * @param className
	 * @param linkText
	 * @return
	 */
	public WebElement getElementByclassNameAndLink (String className, String linkText) {
		WebElement e = null;
		if (className != null) {
			e = this.getElementByClassName(className).findElement(By.linkText(linkText));
		} else {
			e = driver.findElement(By.linkText(linkText));
		}
		return e;
	}
	/**
	 * 	
	 * @param linkText
	 * @return
	 */
	public WebElement getElementByLink(String linkText){
		return this.getElementByLink(null, linkText);
	}
	/**
	 * 
	 * @param parentId
	 * @param linkText
	 * @return
	 */
	public WebElement getElementByLinkTextContains(String parentId, String linkText){
		WebElement e = null;
		if (parentId != null){
			e = this.getElementById(parentId).findElement(By.partialLinkText(linkText));
		}else{
			e = driver.findElement(By.partialLinkText(linkText));
		}
		return e;
	}
	/**
	 * 	
	 * @param linkText
	 * @return
	 */
	public WebElement getElementByLinkTextContains(String linkText){
		return this.getElementByLinkTextContains(null, linkText);
	}
	/**
	 * 
	 * @param parentId
	 * @param tagName
	 * @param typeValue
	 * @return
	 */
	public List<WebElement> getWebElementsByTagAndType(String parentId, String tagName, String typeValue) {
		List<WebElement> elements = getWebElementsByTagName(parentId, tagName);
		List<WebElement> textList = new ArrayList<WebElement>();
		for (int i=0; i < elements.size(); i++) {
			String typeText = elements.get(i).getAttribute("type");
			if( typeText == typeValue){
				textList.add(elements.get(i));
			}
		}
		return textList;
	}
	/**
	 * 
	 * @param parentId
	 * @param text
	 * @return
	 */
	public WebElement getElementByTagAndText(String parentId, String tagName, String text, Boolean match){
		List<WebElement> elements = getWebElementsByTagName(parentId, tagName);
		int index = -1;
		try{
			for(int i=0; i < elements.size();i++){
				String s = elements.get(i).getText();
				if ( match){
					if ( s.equalsIgnoreCase(text)){
						index=i;
						break;
					}
				}else{
					if ( s.contains(text)){
						index=i;
						break;
					}
				}
			}
		}catch(NoSuchElementException e){
			index = -1;
			screenShot(Constants.ERROR);
		}
		if (index >= 0)
			return elements.get(index);
		else
			return null;
	}
	/**
	 * 
	 * @param parentId
	 * @param tagName
	 * @param title
	 * @param match
	 * @return
	 */
	public WebElement getElementByTagAndTitle(String parentId, String tagName, String title, Boolean match) {
		List<WebElement> elements = getWebElementsByTagName(parentId, tagName);
		int index = -1;
		try {
			for (int i = 0; i < elements.size(); i++) {
				String s = elements.get(i).getAttribute("title");
				if (match) {
					if (s.equalsIgnoreCase(title)) {
						index = i;
						break;
					}
				} else {
					if (s.contains(title)) {
						index = i;
						break;
					}
				}
			}
		} catch (NoSuchElementException e) {
			index = -1;
			screenShot(Constants.ERROR);
		}
		if (index >= 0)
			return elements.get(index);
		else
			return null;
	}

	/**
	 * 
	 * @param parentId
	 * @param text
	 * @return
	 */
	public WebElement getElementButton(String parentId, String text) {
		return getElementByTagAndText(parentId, "button", text, true);
	}

	public WebElement getElementButtonByTitle(String parentId, String title) {
		return getElementByTagAndTitle(parentId, "button", title, true);
	}

	public WebElement getElementByTagAndText(String parentId, String tagName, String text){
		return getElementByTagAndText(parentId, tagName, text, true);
	}
	/**
	 * 
	 * @param parentId
	 * @param text
	 * @return
	 */
	public WebElement getElementByTagAndText(String tagName, String text){
		return this.getElementByTagAndText(null, tagName, text, true);
	}
	public WebElement getElementByTagAndText(String tagName, String text, boolean match){
		return this.getElementByTagAndText(null, tagName, text, match);
	}
	/**
	 * 
	 * @param element
	 * @return
	 */
	public Boolean isElementEditable(WebElement element){
		return element.isEnabled();		
	}
	/**
	 * 
	 * @param parentId
	 * @param buttonText
	 * @param info
	 * @return
	 */
	public boolean isButtonDisabled(String parentId, String buttonText, String info){
		return !isEnabled(getElementButton(parentId, buttonText), info);
	}
	/**
	 * 
	 * @param element
	 * @param info
	 * @return
	 */
	public boolean isButtonDisabled(WebElement element, String info){
		return !isEnabled(element, info);
	}
	/**
	 * Return the list of WebElements By className and parent Id, if the id is null
	 * it gets form the entire DOM
	 * @param id
	 * @param className
	 * @return
	 */
	protected List<WebElement> getWebElementsByClassName(String parentId, String className){		
		List<WebElement> elements = null;
		try{
			if (parentId != null){
				WebElement e = getElementById(parentId);
				//WebElement e = getElement("css=#"+parentId);
				elements = e.findElements(By.className(className));
				//elements = e.findElements(By.cssSelector(".gwt-TreeItem"));
			}else		
				elements = driver.findElements(By.className(className));
		} catch( Exception e ){
			if (parentId != null){
				logger.error("Error finding element list by parentID: " +parentId+ " and className: "+className);
			}else{
				logger.error("Error finding element list by className: "+className);
			}
		}
		return elements;		
	}
	/**
	 * 
	 * @param className
	 * @return
	 */
	protected List<WebElement> getWebElementsByClassName(String className){
		return this.getWebElementsByClassName(null, className);
	}
	/**
	 * 
	 * @param id
	 * @param tagName
	 * @return
	 */
	protected List<WebElement> getWebElementsByTagName(String parentId, String tagName){
		List<WebElement> elements;
		if (parentId != null){
			WebElement e = getElementById(parentId);
			elements = e.findElements(By.tagName(tagName));
		}else		
			elements = driver.findElements(By.tagName(tagName));
		//Printing the Elements Text if there is And Class
		for (int i=0; i < elements.size(); i++) {
			//logger.debug(elements.get(i).getAttribute("class"));
			String text = elements.get(i).getText().trim();
			if(text.length() != 0)
				logger.debug("Element Text : "+text);
		}
		return elements;
	}
	/**
	 * 
	 * @param tagName
	 * @return
	 */
	protected List<WebElement> getWebElementsByTagName(String tagName){
		return this.getWebElementsByTagName(null, tagName);
	}
	
	/**
     * 
     * @param element
     * @param attribute
     * @return
     */
     public String getElementAttributeValue(WebElement element, String attribute){
           return element.getAttribute(attribute);
     }
	
	/**
	 * 
	 * @param parentId
	 * @param className
	 * @param itemText
	 * @return
	 */
	public WebElement getElementByClassNameAndText(String parentId, String className, 
														String itemText, boolean match){
		List<WebElement> elements = getWebElementsByClassName(parentId, className);
		String text;
		int index = -1;
		for(int i=0; i < elements.size(); i++) {
			text = getText(elements.get(i), "index : "+i);
			//Exact Match
			if( match )
				if( verifyTextMatch(text, itemText)) {
					index = i;
					break;
				}
			// Contains	
			if( verifyTextContains(text, itemText)) {
				index = i;
				break;
			}
		}
		if ( index == -1)
			return null;
		else
			return elements.get(index);		
	}
	/**
	 * 
	 * @param className
	 * @param itemText
	 * @return
	 */
	public WebElement getElementByClassNameAndText(String className, String itemText){
		return getElementByClassNameAndText(null, className, itemText, true);
	}
	/**
	 * Get element by class name and text with option to match
	 * @param className
	 * @param itemText
	 * @return
	 */
	public WebElement getElementByClassNameAndText(String className, String itemText, boolean match){
		if (match) {
			return getElementByClassNameAndText(null, className, itemText, true);
		}
		else {
			return getElementByClassNameAndText(null, className, itemText, false);
		}
		
	}
	/**
	 * 
	 * @param parentId
	 * @param className
	 * @param itemText
	 * @return
	 */
	public WebElement getElementByClassNameAndText(String parentId, String className, String itemText){
		return getElementByClassNameAndText(parentId, className, itemText, true);
	}
	/**
	 * 
	 * @param parentId
	 * @param parentClassName
	 * @param childClassName
	 * @return
	 */
	public WebElement getWebElementByClassAndClass(String parentClassName, String childClassName) {
		WebElement parent;
		parent = getElementByClassName(parentClassName);
		return parent.findElement(By.className(childClassName));
	}
	/**
	 * 
	 * @param parentClassName
	 * @param childtagName
	 * @return
	 */
	public WebElement getWebElementByClassAndTagName(String parentId, String className, String tagName) {
		WebElement parent;
		parent = getElementByClassName(parentId, className);
		return parent.findElement(By.tagName( tagName ));
	}
	/**
	 * 
	 * @param parentId
	 * @param className
	 * @param buttonText
	 * @return
	 */
	public WebElement getWebElementButtonByClassAndText(String parentId, String className, 
																		String buttonText) {
		WebElement parent = null;
		WebElement button = null;
		if(parentId != null)
			parent = getElementByClassName(parentId, className);
		else
			parent = getElementByClassName(className);
		List<WebElement> buttonList = parent.findElements(By.tagName("button"));
		List<String> list = new ArrayList<String>();
		for(int i=0; i < buttonList.size(); i++) {
			String text = buttonList.get(i).getText().trim();
			list.add(text);
		}
		if(list.contains(buttonText.trim())) {
			button = buttonList.get(list.indexOf(buttonText.trim()));
		}
		return button;
		
	}
	/**
	 * 
	 * @param className
	 * @param buttonText
	 * @return
	 */
	public WebElement getWebElementButtonByClassAndText(String className, String buttonText) {
		return this.getWebElementButtonByClassAndText(null, className, buttonText);
	}
	/**
	 * Returns the item index from a set of items in a Table
	 * @param id
	 * @param className
	 * @param itemText
	 * @return
	 */
	public int getItemIndexByClassName(String parentId, String className, String itemText, Boolean match){
		List<WebElement> elements;
		int index = -1;
		elements = getWebElementsByClassName( parentId, className );
		for (int i=0; i < elements.size(); i++) {
			String text = elements.get(i).getText().trim();
			if( text.length() != 0 )
				logger.debug("Element Text : "+text);
			if ( match ) {
				if (verifyTextMatch(text, itemText)){
					index=i;
					break;
				}
			} else {
				if ( verifyTextContains(text, itemText) ){
					index=i;
					break;
				}
			}
				
		}
		logger.debug("The Item : "+itemText+" is in location : "+index);
		return index;
	}
	/**
	 * 
	 * @param className
	 * @param itemText
	 * @return
	 */
	public int getItemIndexByClassName(String className, String itemText, Boolean match){
		return this.getItemIndexByClassName(null, className, itemText, match);
	}
	public int getItemIndexByClassName(String className, String itemText) {
		return this.getItemIndexByClassName(null, className, itemText, true);
	}
	public int getItemIndexByClassName(String parentId, String className, String itemText) {
		return this.getItemIndexByClassName(parentId, className, itemText, true);
	}
	/**
	 * 
	 * @param parentId
	 * @param className
	 * @return
	 */
	public int getItemCountByClassName(String parentId, String className){
		List<WebElement> elements;
		int count=0;
		try{
			if(parentId != null)
				elements = getWebElementsByClassName(parentId, className);
			else
				elements = getWebElementsByClassName(className);
			count = elements.size();
			logger.debug("The No. Of Elements For Class Type : "+className+" is : "+count);
			return count;
		}catch(NoSuchElementException e){
			return -1;
		}
	}
	/**
	 * 
	 * @param className
	 * @param itemText
	 * @return
	 */
	public int getItemCountByClassName(String className){
		return this.getItemCountByClassName(null, className);
	}
	/**
	 * Returns the item index from a set of items in a Table
	 * @param id
	 * @param className
	 * @param itemText
	 * @return
	 */
	public int getItemIndexByTagNameAndText(String parentId, String tagName, String itemText){
		List<String> textList;
		int index = -1;
		textList = getWebElementsTextByTagName(parentId, tagName);
		index = textList.indexOf(itemText);
		logger.debug("The Item : "+itemText+" is in location : "+index);
		return index;
	}
	/**
	 * 
	 * @param elements
	 * @param itemText
	 * @return
	 */
	public int getWebElementIndexByText(List<WebElement> elements, String itemText){
		int index = -1;
		for (int i=0; i < elements.size(); i++){
			String text = elements.get(i).getText();
			if( text != null)
				if ( text.contains(itemText) ){
					index = i;
					break;
				}
		}
		logger.debug("The Item : "+itemText+" is in location : "+index);
		return index;
	}
	/**
	 * 
	 * @param parentId
	 * @param className
	 * @return
	 */
	public List<String> getWebElementsTextListByClassName(String parentId, String className){
		List<WebElement> elements=null;
		if (parentId != null){
			elements = getWebElementsByClassName(parentId, className);
		}else		
			elements = getWebElementsByClassName(className);
		List<String> list = new ArrayList<String>();
		for (int i=0; i < elements.size(); i++){
				list.add(elements.get(i).getText());
		}
		return list;
	}
	/**
	 * 
	 * @param className
	 * @return
	 */
	public List<String> getWebElementsTextListByClassName(String className){
		return getWebElementsTextListByClassName(null,	className);
	}
	/**
	 * 
	 * @param parentId
	 * @param className
	 * @return
	 */
	public List<String> getWebElementsTextByTagName(String parentId, String tagName){
		List<WebElement> elements = null;
		List<String> list = new ArrayList<String>();
		if (parentId != null){
			elements = getElementsByTagName(parentId, tagName);
		}else		
			elements = getWebElementsByTagName(tagName);
		//
		for (int i=0; i < elements.size(); i++){
			String text = elements.get(i).getText();
			//if( text.length() != 0)
				//logger.debug("Element : "+i+" Text is : ");
			list.add(text);
		}
		return list;
	}
	/**
	 * 
	 * @param parentId
	 * @param className
	 * @return
	 */
	public List<WebElement> getWebElementsByTagAndText(String parentId, String tagName, String itemText){
		List<WebElement> elements=null;
		List<WebElement> list = new ArrayList<WebElement>();
		if (parentId != null){
			elements = getElementsByTagName(parentId, tagName);
		}else		
			elements = getWebElementsByTagName(tagName);
		//
		for (int i=0; i < elements.size(); i++){
			String text = elements.get(i).getText();
			if( text.equalsIgnoreCase(itemText))
				list.add(elements.get(i));
		}
		return list;
	}
	/**
	 * 
	 * @param className
	 * @return
	 */
	public List<String> getWebElementsTextByTagName(String tagName){
		return getWebElementsTextByTagName(null, tagName);
	}
    /**
     * Returns the Current Page Title
     * @return
     */
	public String getTitle() {
		return driver.getTitle();
	}
	/**
	 * 
	 * @return
	 */
	public String getURL(){
		return driver.getCurrentUrl();
	}
	/**
	 * 
	 */
	public void navigateBrowserback(){
		driver.navigate().back();
	}
 
	//private String executeJavaScript(String domscript) {
	//	String result = (String) ((JavascriptExecutor)
	//			driver).executeScript(domscript); 
	//	return result;

	//}
	/**
	 * Return true false by looking for a button with specified text
	 * @param parentId
	 * @param text
	 * @return
	 */
	public Boolean isButtonPresentByText(String parentId, String text){
		// return getElementByTagAndText(parentId, "button", text, true).isDisplayed();
		return isElementPresent(getElementButton(parentId, text));
	}

	public Boolean isButtonPresentByTitle(String parentId, String title) {
		return isElementPresent(getElementButtonByTitle(parentId, title));
	}

	/**
	   * Checks for the expected string is substring or not in actual String and Prints both in log
	   * @param actualText - actual Text picked up from application under Test
	   * @param expText - expected Text to be contained in actual text
	   * @return
	   */
	public boolean verifyTextContains(String actualText, String expText) {
		if (actualText.toLowerCase().contains(expText.toLowerCase())){
			logger.info(" Actual Text From Application Web UI   --> : "+ actualText);
			logger.info(" Expected Text From Application Web UI --> : "+ expText);
			logger.info("###Verification MATCHED !!!");
			return true;
		}
		else{
			logger.info(" Actual Text From Application Web UI   --> : "+ actualText);
			logger.info(" Expected Text From Application Web UI --> : "+ expText);
			logger.info("###Verification DOES NOT MATCH !!!");
			return false;
		}
			   
	}
	    /**
		 * Checks for the expected string is Matched with actual String and Prints both in log
		 * @param actualText - actual Text picked up from application under Test
		 * @param expText - expected Text to be Matched with actual text
		 * @return
		 */
	public boolean verifyTextMatch(String actualText, String expText) {
			if (actualText.equals(expText)){
				logger.info(" Actual Text From Application Web UI   --> : "+ actualText);
				logger.info(" Expected Text From Application Web UI --> : "+ expText);
				logger.info("###Verification MATCHED !!!");
				return true;
			}else{
				logger.info(" Actual Text From Application Web UI   --> : "+ actualText);
				logger.info(" Expected Text From Application Web UI --> : "+ expText);
				logger.info("###Verification DOES NOT MATCH !!!");
			    return false;}
		}
		/**
		 * Looks for the Exact match for the element text with expected text
		 * @param locator - WebElement to get Text
		 * @param expText - Expected text from element
		 * @return true if the element text is matched with expected text
		 */
		public boolean verifyElementTextMatch(String locator, String expText) {
			WebElement element = getElement(locator);
			String actualText = getText(element, "Text Match");
			if (actualText.equals(expText)){
				logger.info(" Actual Text From Application Web UI   --> : "+ actualText);
				logger.info(" Expected Text From Application Web UI --> : "+ expText);
				logger.debug("###Verification CONTAINS !!!");
				return true;}
			else{
				logger.info(" Actual Text From Application Web UI   --> : "+ actualText);
				logger.info(" Expected Text From Application Web UI --> : "+ expText);
				logger.debug("###Verification DOES NOT CONTAIN !!!");
			    return false;}
			}
		/**
		 * Looks for the substring match for the element text with expected text
		 * @param locator - Element locator strategy to get Text
		 * @param expText - Expected sub string text from element
		 * @return true if the element text has substring of expected text
		 */
		public boolean verifyElementTextContains(String locator, String expText) {
			WebElement element = getElement(locator);
			String actualText = getText(element, "Text Match");
			if (actualText.contains(expText)){
				logger.info(" Actual Text From Application Web UI   --> : "+ actualText);
				logger.info(" Expected Text From Application Web UI --> : "+ expText);
				logger.debug("###Verification MATCHED !!!");
				return true;
			}else{
				logger.info(" Actual Text From Application Web UI   --> : "+ actualText);
				logger.info(" Expected Text From Application Web UI --> : "+ expText);
				logger.debug("###Verification DOES NOT MATCH !!!");
			    return false;}
	}
    /**
     * 
     * @param id
     * @return
     */
    public Boolean isElementPresentById(String id){
    	try {
    			driver.findElement(By.id(id));
                return true;
            } catch (NoSuchElementException e) {
                return false;
            }      
    }
    /**
     * 
     * @param id
     * @return
     */
    public Boolean isElementPresentByXpath(String xpath){
    	try {
    			driver.findElement(By.xpath(xpath));
                return true;
            } catch (NoSuchElementException e) {
                return false;
            }
    }
    //
    /**
	 * 
	 */
	public void waitForLoading(String xpath, long timeOut){
		long startTime = System.currentTimeMillis();
		long timeToQuit = 0;
	   	if( isElementPresent(xpath) ) {
	   		logger.info("AJAX Mask Appeared, Waiting to Disapper...");
		}
		util.sleep(1000, "Waiting After For AJAX Operation");
	    while(isElementPresent(xpath) || timeToQuit < timeOut){
	       	util.sleep(1000);
			timeToQuit = startTime + 4000;
		}
		long endTime = System.currentTimeMillis() - startTime;
		logger.info("AJAX Loader Disappeared In : "+ endTime/1000+" Seconds");
	}
	/**
	 * 
	 * @param xpath
	 */
	public void waitForLoading(String xpath){
		waitForLoading(xpath, 60000);
	}
    /**
     * 
     * @param by
     * @return
     */
    public ExpectedCondition<WebElement> isElementLocated(final WebElement element) {
    	return new ExpectedCondition<WebElement>() {
    		public WebElement apply(WebDriver driver) {
    			return element.isDisplayed() ? element : null;
          }
        };
      }
//    public ExpectedCondition<WebElement> isElementLocated(final By by) {
//    	return new ExpectedCondition<WebElement>() {
//    		public WebElement apply(WebDriver driver) {
//    			WebElement element = driver.findElement(by);
//    			return element.isDisplayed() ? element : null;
//          }
//        };
//      }
    /**
     *  
     * @param seconds
     * @param locator
     * @return
     */
    public Boolean waitForElement(WebElement element, int seconds) {
    	logger.debug("I Will Wait : "+seconds+" : seconds for Element to Appear");
    	WebDriverWait wait = new WebDriverWait(driver, seconds);
        WebElement e = wait.until(isElementLocated(element));
        if (e != null)
        	return true;
        else
        	return false;        
    }
    /**
     *  
     * @param seconds
     * @param locator
     * @return
     */
    public Boolean waitForElement(String locator, int seconds) {
    	WebElement element = getElement(locator);
    	return this.waitForElement(element, seconds);
    }
    /**
     * 
     * @param parentId
     * @param className
     * @param itemText
     */
    public void selectItemByClassNameAndText(String parentId, String className, String itemText, Boolean match){
    	int index=-1;
    	List<WebElement> elements = getWebElementsByClassName(parentId, className);
    	index = getItemIndexByClassName(parentId, className, itemText, match);
    	if(index >= 0)
    		elements.get(index).click();
    	else
    		logger.info("THERE IS NO SUCH ELEMENT IN THE PAGE...???");
    }
    public void selectItemByClassNameAndText(String parentId, String className, String itemText) {
    	selectItemByClassNameAndText(parentId, className, itemText, true);
    }
    /**
     * 
     * @param locator
     * @return
     */
    public By autoLocator(String locator){
    	try{
    		if(locator.startsWith("//")){
    			return By.xpath(locator);
    		}else if ( locator.startsWith("class=")){
    			    locator = locator.split("=")[1];
    		    	return By.className(locator);
    			}
    		else
    			return By.id(locator);
    	} catch (NoSuchElementException e){
    	
    	}
    	return null;
    }
    /**
     * 
     * @return
     */
    public Boolean isChrome(){
    	if(browser.contains("chrome"))
    		return true;
    	else
    		return false;
    }
    /**
     * 
     * @param locator
     * @return
     */
    public Boolean isElementPresent(String locator){    
    	try{
    		// WebElement e = driver.findElement(autoLocator(locator));
    		if (driver.findElement(autoLocator(locator)) != null){
    			logger.develop("Element : "+locator+" : Successfully Appeared On Page");
    			return true;
    		}
    		else{
    			logger.develop("Element : "+locator+" : NOT Appeared On Page");
                return false;
    		}
    	}	catch(Exception e){
    			return false;
    	}
    	/*    	    try {
    	    	driver.findElement(autoLocator(locator));
    	    	logger.develop("Element : "+locator+" : Successfully Appeared On Page");
            } catch (org.openqa.selenium.NoSuchElementException Ex) {
            	logger.develop("Element : "+locator+" : NOT Appeared On Page");
                return false;
             } 
            return true;*/
    }
    /**
     * 
     * @param locator
     * @return
     */
    public Boolean isElementPresent(By by){    
    	    try {
    	    	driver.findElement(by);
    	    	logger.develop("Element Successfully Appeared On Page");
            } catch (org.openqa.selenium.NoSuchElementException Ex) {
            	logger.develop("Element NOT Appeared On Page");
                return false;
             } 
            return true;
    }
    /**
     * 
     * @param element
     * @return
     */
    public boolean isElementPresent(WebElement element){ 
    	if(element == null)
    		return false;
    	else
    		return true;    		
    }
    /**
     * 
     * @param locator
     * @param timeout
     */
//    public void waitForElementPres(final String locator, int timeout) {
//    	new WebDriverWait(driver, timeout) {
//    		    	}.until(new ExpectedCondition<Boolean>() {
//    		               @Override    		 
//    		                public Boolean apply(WebDriver driverObject) {
//    		            	   return isElementPresent(autoLocator(locator));
//    		 
//    		                }
//    		 
//    		            });
//    		 
//    } 
    /**
     * 
     */
    public Boolean waitForElementPresent(String locator, int seconds, int timeOut) {
    	long timeToQuit = 0;
    	long current = System.currentTimeMillis();
    	while( !isElementPresent(locator) ){
    		util.sleep(seconds * 1000, "Waiting For Element");
    		timeToQuit = timeToQuit + seconds;
    		if( timeOut <= timeToQuit)
    			break;
    	}
    	long timeTook = System.currentTimeMillis() - current;
    	if(isElementPresent(locator)){
    		logger.info("Element Is Appeared in " + (timeTook/1000) + " Sec.");
    		return true;
      	} else {
      		logger.info("Element NOT Appeared in " + (timeTook/1000) + " Sec.");
      		return false;
    		}
    }
    /**
     * 
     * @param element
     * @param seconds
     * @param totalSec
     */
    public void pollForElement(final By by, int seconds, int timeOut){
    	FluentWait<By> fluentWait = new FluentWait<By>(by);
    	logger.debug("I Will Wait Total: "+timeOut+" : seconds for Element to Appear");
    	//FluentWait<WebElement> fluentWait = new FluentWait<WebElement>(element);
    	fluentWait.pollingEvery(seconds, TimeUnit.SECONDS);
    	fluentWait.withTimeout(timeOut, TimeUnit.SECONDS);
    	fluentWait.until(new Predicate<By>() {
    		public boolean apply(By by) {
    			try {
    					return isElementPresent(by);
    					
    				} catch (NoSuchElementException e) {
    					e.printStackTrace();
    					return false;
	                }
    		}
    	});
    	
    }
    /**
     * 
     * @param seconds
     * @param interval
     * @return
     */
    protected boolean waitForProcessText(WebElement element, int seconds, int interval) {
		logger.debug("Going to wait for " + seconds
				+ " Seconds To Process Text Appearance !");
		interval = interval * 1000;
		int sec = interval;
		while (sec <= seconds * 1000) {
			util.sleep(interval);
			if (this.waitForElement(element, 3)) {
				return true;
			}
			sec += interval;
		}
		return false;
	}
    /**
     * 
     * @param element
     * @param seconds
     * @param interval
     */
    public void completeProcess(WebElement element, int seconds, int interval) {
		if (waitForProcessText(element, seconds, interval)) {
			Assert.assertTrue(this.verifyTextContains(element.getText(),
					"Expected Text Appeared Successfully."));
		}else{
			Assert.assertTrue(false, "###Prosess Got ERROR ### ERROR ### ERROR");
		}
	}
    
   /**
    * Mouse Overs to a Element
    * @param element
    */
    public void mouseOver(WebElement element){
    	Actions action = new Actions(driver);
 	   	action.moveToElement(element).perform();
 	   	util.sleep(5000);
    }
   /**
    * Verifies the item matches with all the stings in list
    * @param listItems
    * @param item
    * @return
    */
   public Boolean verifyListMatchByItem(List<String> listItems, String item){
		boolean found = false;
		for (int i = 0; i < listItems.size(); i++) {
			found = false;
			if ((verifyTextMatch(listItems.get(i), item))) {
				found = true;
			}
		}
		if (found)
			return true;
		else
			return false;
   }
   /**
    * Verifies the item matches with at least one of the sting in the list
    * @param listItems
    * @param item
    * @return
    */
   public Boolean verifyItemPresentInList(List<String> listItems, String item){
		boolean found = false;
		for (int i = 0; i < listItems.size(); i++) {
			found = false;
			if ((verifyTextMatch(listItems.get(i), item))) {
				found = true;
				break;
			}
		}
		if (found)
			return true;
		else
			return false;
   }
   /**
    * Verifies if the item is contained in at least one of the sting in the list
    * @param listItems
    * @param item
    * @return
    */
   public Boolean verifyListContainsByItem(List<String> listItems, String item){
		boolean found = false;
		for (int i = 0; i < listItems.size(); i++) {
			found = false;
			if ((verifyTextContains(listItems.get(i), item))) {
				found = true;
				break;
			}
		}
		if (found)
			return true;
		else
			return false;
   }
   /**
    * 
    * @param actList
    * @param expList
    * @return
    */
   public Boolean verifyListMatch(List<String> actList, String... expList){
	   boolean found = false;
		for (int i = 0; i < actList.size(); i++) {
			found = false;
			for (int j = 0; j < expList.length; j++) {
				if (verifyTextMatch(actList.get(i), expList[j])) {
					found = true;
					break;
				}
			}
		}
		if (found)
			return true;
		else
			return false;
   }
   /**
    * 
    * @param actList
    * @param expList
    * @return
    */
   public Boolean verifyListMatch(List<String> actList, List<String> expList){
	    boolean found = false;
		for (int i = 0; i < actList.size(); i++) {
			found = false;
			for (int j = 0; j < expList.size(); j++) {
				if (verifyTextMatch(actList.get(i), expList.get(j))) {
					found = true;
					break;
				}
			}
		}
		if (found)
			return true;
		else
			return false;
   }
   /**
    * Selects the New Browser Window
    */
   public void selectBrowserPopUp(){
   	//Get all the window handles in a set
       Set <String> handles =driver.getWindowHandles();
       Iterator<String> it = handles.iterator();
       //iterate through your windows
       while (it.hasNext()){
       	@SuppressWarnings("unused")
			String parent = it.next();
       	String newwin = it.next();
       	driver.switchTo().window(newwin);
       	logger.info("Switched to New Broser Window");
    	logger.info("New Broser Window URL : "+driver.getCurrentUrl());
       }
   }
   /**
    * Closes the Browser Pop up window
    */
   public void closeBrowserPopUp() {
		Set <String> handles =driver.getWindowHandles();
		Iterator<String> it = handles.iterator();
		//iterate through your windows
		while (it.hasNext()){
			String parent = it.next();
			String newwin = it.next();
			driver.switchTo().window(newwin);
			//Close New Window and Switch to Main Window
			driver.close();
			logger.debug("Browser Popup Closed !");
			driver.switchTo().window(parent);
			logger.debug("Handle was Set back to Main Browser");
		}
   }
   /**
    * Selects a given option in list box
    * @param parentId
    * @param optionToSelect
    */
   public void selectOption(String locator, String optionToSelect){
	   WebElement element = getElement(locator);
	   this.selectOption(element, optionToSelect);
   }
   /**
    * 
    * @param element
    * @param optionToSelect
    */
   public void selectOption(WebElement element, String optionToSelect) {
	   Select sel = new Select(element);
	   sel.selectByVisibleText(optionToSelect);
	   logger.info("Selected Option : "+optionToSelect);
   }
   /**
    * 
    * @param element
    * @param optionToSelect
    */
   public boolean isOptionExists(WebElement element, String optionToVerify) {
	   Select sel = new Select(element);
	   boolean exists = false;
	   List<WebElement> optList = sel.getOptions();
	   for(int i = 0; i < optList.size(); i++) {
		   String text = getText(optList.get(i), "Option Text");
		   if(text.matches(optionToVerify)) {
			   exists = true;
			   break;		   
		   }			   
	   }
	   if (exists){
		   logger.info("Selected Option : "+optionToVerify+" exist");
	   }
	   else
	   {
		   logger.info("Selected Option : "+optionToVerify+" does not exist");
	   }
	   return exists;
   }
   
   /**
    * 
    * @param parentId
    * @param option
    * @return
    */
   public boolean verifydefaultSelectedOption(String parentId, String option){
	   WebElement element = getElementByTagAndText(parentId, "option", option, true);
	   return isSelected(element, "option");
   }
   /**
    * 
    * @param parentId
    * @param options
    * @return
    */
   public boolean verifySelectedOptions(String parentId, String... options) {
	   boolean found = true;
	   List<String> actOptions = getWebElementsTextByTagName(parentId, "option");
	   for (int j=0; j<options.length; j++) {
		   if( !actOptions.contains(options[j])	) {
			   found = false;
			   break;
		   }
	   }
	   if (found)
			return true;
		else
			return false;
   }
  
	/**
	 * Get Following Sibling of a Row when ClassName and text are known
	 * @param className
	 * @param text
	 * @return
	 */
	public String getFollowingSibling(String className, String text) {
		WebElement e = getElement("//div[@class='"+className+"']//td[div[text()='"+text+"']]//following-sibling::td");
		return e.getText();
	}
	/**
	 * Get Following Sibling of a Row when ClassName and text are known
	 * @param className
	 * @param text
	 * @param num
	 * @return
	 */
	public String getFollowingSibling(String className, String text, int num) {
		WebElement e = getElement("//div[@class='"+className+"']//td[div[text()='"+text+"']]//following-sibling::td["+num+"]");
		return e.getText();
	}
 
}