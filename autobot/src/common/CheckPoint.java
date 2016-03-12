/**
 * 
 */
package common;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.StringTokenizer;

import org.testng.Assert;
/**
 * @author prince
 *
 */
public class CheckPoint {
	public static Logger logger = LoggerFactory.getLogger();
	public static HashMap<String, String> hm = new HashMap<String, String>();
	protected static Util        util     = new Util();
	private static String PASS  = "PASS";
	private static String FAIL  = "FAIL";
	/**
	 * 
	 * Clears The hash Map
	 */
	public static void clearHashMap() {
		logger.info("Clearing Hash Map!");
		hm.clear();
	}
	private static void setStatus(String status, String mapKey) {
		hm.put(mapKey, ":### " + status);
		logger.info(mapKey+" :-> "+hm.get(mapKey));
	}
	/**
	 * 
	 * Keeps the verification point status with testId, Result and Verification point Message in hash
	 * Hash Map, captures the Screen Shot
	 * @param testId - The test case id
	 * @param result - Verification Result from test method in tetNg test Suite
	 * @param resultMessage - Message tagged with verification
	 */
	public static void mark(String testName, boolean result, String resultMessage){
		testName = testName.toLowerCase();
		String mapKey = testName + "." + resultMessage;
		logger.info("Starting Test "+testName);
		try {
			if (result) {
			    setStatus(PASS, mapKey);
			    util.screenShot(PASS + "." + mapKey);
			} else {
				setStatus(FAIL, mapKey);
		        util.screenShot(FAIL + "." + mapKey);
			}
		} catch(Exception e) {
			logger.info("Exception Occured...");
			setStatus(FAIL, mapKey);
			e.printStackTrace();
		}
	}
	/**
	 * 
	 * Keeps the verification point status with testName, Result and Verification point Message in hash
	 * Hash Map, captures the Screen Shot, It asserts all the verifications in a test, any verification
	 * in a test is failed then the test case is failed
	 * @param testName - The test case name
	 * @param result - Verification Result from test method in tetNg test Suite
	 * @param resultMessage - Message tagged with verification
	 */
	public static void markFatal(String testName, boolean result, String resultMessage) {
		testName = testName.toLowerCase();
		String mapKey = testName + "." + resultMessage;
		logger.info("Starting Test " + testName);
		if (result){
			 setStatus(PASS, mapKey);
		    //Util.screenShot(status+"."+key);
		    }
		else{
			 setStatus(FAIL, mapKey);
			//Util.screenShot(status+"."+key);
		  }		
		Iterator<String> iterator    = hm.values().iterator();
		ArrayList<String> list = new  ArrayList<String>();
	
		while (iterator.hasNext()) {
			String    entry = getResultPart(iterator.next());
			list.add(entry);
			logger.debug("#####"+entry);
		}
		for(int i =0; i<list.size();i++){
			if(list.contains("### FAIL")) {
				logger.debug("YES I AM FAILED");
				Assert.assertTrue(false);				
			} else {
				logger.debug("YES I AM PASSED");
				Assert.assertTrue(true);
			}
		}
	}
	
	/**
	 * 
	 * @param result
	 * @return
	 */
	private static String getResultPart(String result) {
		StringTokenizer st = new StringTokenizer(result,":");
		while(st.hasMoreTokens())
			return st.nextToken();
		return null;
	}
	
	/**
	 *
	 * @return
	 */
	private static Iterator<String> getTestIds() {
		return hm.keySet().iterator();
	}

	public static void main(String args[]) {
		//CheckPoint.mark(11, true, "result112");
		//CheckPoint.mark(12, true, "result12");
		//CheckPoint.mark(11, true, "result113");
		//CheckPoint.markFatal(12, true, "result14");

		//System.out.println("result->"+ result);
		//System.out.println("test id->"+ getTestIds());
		//System.out.println("values->"+ hm.values());
		//System.out.println("keys->"+ hm.keySet());
	}
}