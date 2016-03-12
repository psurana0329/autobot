/**
 * 
 */
package common;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
/**
 * @author prince
 *
 */
public class ConfigReader {
	public static Properties env  = new Properties();
	public static Logger logger   = LoggerFactory.getLogger();
	
	/**
	 * Gets System Environment variable value
	 * @param varName
	 * @return
	 */
    private static String getProperty(String varName) {
    	return System.getProperty(varName);
    }
    
    /**
     * Gets the environment variable if set, other wise returns the override.
     * 
     * @param envVarName
     * @param override
     * @return
     */
    public static String getConfiguration(String envVarName, 
    								final String override) {
        String retValue = override;
        String value;      	
        String envFile = getProperty(SelConfig.ENV_PROP_FILE);
        //logger.info("ERROR : "+envFile);
        value = getProperty(envVarName);
        if (envFile != null && value == null)
        	value = getENV(envVarName);
        //else
            //value = getProperty(envVarName);
        
        //logger.debug("Evaluating system env " + envVarName + " value = "
           // + value);
        if (value != null && value.length() != 0) {
            retValue = value;
        }
        //logger.debug("Resolved " + envVarName + " = " + retValue);
        //value = getProperty(envVarName);
        //if ( value != null)
        	//return value;
      //  else
        	return retValue;
    }
	/**
	 * 
	 * @param key
	 * @return
	 */
	public static String getENV(String key){
		try {
			env.load(new FileInputStream(getProperty(SelConfig.ENV_PROP_FILE)));
		} catch (IOException e) {
			e.printStackTrace();
		}
		//logger.info(env.getProperty(key));
		return env.getProperty(key);
	}
	/**
	 * Return config Directory location from env or empty string if it is null
	 * @return
	 * @throws InterruptedException 
	 */
	public static String getConfigDir(){
	    String value = getProperty(SelConfig.CONFIG_DIR);
	    if (value != null){
			return value+"/";
		}	
		else
		    return SelConfig.DEFAULT_CONFIG_DIR;
	}
	/**
	 * Returns the Screenshot Directory
	 * @return
	 */
	public static String getScreenDir(){
	    String value = getProperty(SelConfig.SCREEN_DIR);
	    return value;
	}


}
