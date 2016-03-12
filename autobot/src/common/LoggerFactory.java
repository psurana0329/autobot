/**
 * 
 */
package common;

/**
 * @author prince
 *
 */
public class LoggerFactory {
	public static Logger getLogger() {
		Logger logger = Logger.getDefault();
		logger.setSeverity(Integer.parseInt(ConfigReader
				.getConfiguration(SelConfig.LOG_LEVEL, SelConfig.DEFAULT_LOG_LEVEL)));
		return logger;
	}

}
