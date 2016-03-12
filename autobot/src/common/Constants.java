/**
 * 
 */
package common;

/**
 * @author prince
 *
 */
public class Constants {
	public final static String configDir             = ConfigReader.getConfigDir();
	public final static String testconfigFile        = configDir+"config/TestConfig/TestEnv.properties";
	public final static String baseTestDataFile      = configDir+"config/TestData/BaseData.properties";
	public final static String sceenShot             = configDir+"config//log//screenCapture//";
					
	public final static String chromedriver          = configDir+"config/TestConfig/chromedriver";
	public final static String elementMapFile        = configDir+"config/ElementRepo/UIMap.xml";
	//Strings
	public final static String NOT_FOUND           	 = "NOT FOUND";
	public final static String ERROR           	 	 = "ERROR";
}
