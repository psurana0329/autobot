package common;

public interface ILogger {
	public static interface Severity {
		final static int ERROR = 1;
		final static int WARNING = 2;
		final static int INFO = 3;
		final static int PERFOMANCE = 4;
		final static int DEBUG = 5;
		final static int DEVELOP = 6;
		final static int ALL = Integer.MAX_VALUE;
	}
	/**
	 * 
	 * @author prince
	 *
	 */
	public interface IEntry {
		int getSeverity();
		String getMessage();
		Throwable getException();
	}
	/**
	 * 
	 * @param severity
	 */
	void setSeverity(int severity);
	/**
	 * 
	 * @param entry
	 */
	void log(IEntry entry);
}

