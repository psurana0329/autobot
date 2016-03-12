package common;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.testng.Reporter;

public class Logger implements ILogger {
	private final int defaultLogLevel = Severity.INFO;
	protected static Logger defaultLogger = new Logger();
	protected int severity;

	public class Entry implements IEntry {
		
		protected int severity;

		protected String message;

		protected Throwable exception;

		public Entry(int severity, String message, Throwable exception) {
			this.severity = severity;
			this.message = message;
			this.exception = exception;
		}

		public int getSeverity() {
			return severity;
		}

		public String getMessage() {
			return message;
		}

		public Throwable getException() {
			return exception;
		}

		public String toString() {
			SimpleDateFormat dateFormat = new SimpleDateFormat("[yyyy-MM-dd HH:mm:ss]");
			Date time = Calendar.getInstance().getTime();
			StringBuilder buffer = new StringBuilder(dateFormat.format(time));
			
			buffer.append(severityToString(severity)).append(": ");

			if (message != null) {
				buffer.append(message);
			}

			if (exception != null) {
				if (message != null) {
					buffer.append(", ");
				}
				buffer.append(exception);
			}

			return buffer.toString();
		}
	}

	/*
	 * 
	 */
	public static String severityToString(int severity) {
		switch (severity) {
		case Severity.ERROR:
			return "ERRR";
		case Severity.WARNING:
			return "WARN";
		case Severity.INFO:
			return "INFO";
		case Severity.PERFOMANCE:
			return "PERF";
		case Severity.DEBUG:
			return "DEBG";
		case Severity.DEVELOP:
			return "DEVP";
		case Severity.ALL:
			return "ALL";
		}

		return null;
	}

	public static final String stackTrace(Throwable ex) {
		ByteArrayOutputStream ostr = new ByteArrayOutputStream();
		ex.printStackTrace(new PrintWriter(ostr, true));
		String text = ostr.toString();
		return text;
	}
	/**
	 * 
	 * @return
	 */
	public static Logger getDefault() {
		return Logger.defaultLogger;
	}
	/**
	 * 
	 */
	private Logger() {
		this.severity = defaultLogLevel;
	}
	/**
	 * 
	 */
	public void log(IEntry entry) {		
		Reporter.log(entry.toString());
		System.out.println(entry.toString());
	}
	/**
	 * 
	 * @param severity
	 * @param message
	 * @param exception
	 */
	public void log(int severity, String message, Throwable exception) {
		if (this.severity < severity)
			return;

		log(new Entry(severity, message, exception));
	}

	public void log(int severity, String message) {
		log(severity, message, null);
	}

	public void log(int severity, Throwable exception) {
		log(severity, null, exception);
	}

	public void error(String message, Throwable exception) {
		log(Severity.ERROR, message, exception);
	}

	public void warning(String message, Throwable exception) {
		log(Severity.WARNING, message, exception);
	}

	public void info(String message, Throwable exception) {
		log(Severity.INFO, message, exception);
	}
	public void info(String message, long startTime) {
		double time = System.currentTimeMillis() - startTime;
		log(Severity.INFO, time/1000+","+ " Sec" + " Took to Load $ ---> " + message);
	}
	public void perfomance(String message, long startTime, Throwable exception) {
		double time = System.currentTimeMillis() - startTime;
		log(Severity.PERFOMANCE, time/1000+","+ " Sec" + " Took To Lood $ ---> " + message);
		//util.printPerformaceMessage(time/1000+","+ " Sec" + " takes to " + message);
	}
	public void perfomance(String message, long startTime) {
		perfomance(message, startTime, null);
	}
	
	public void debug(String message, Throwable exception) {
		log(Severity.DEBUG, message, exception);
	}
	public void develop(String message, Throwable exception) {
		log(Severity.DEVELOP, message, exception);
	}

	public void error(String message) {
		error(message, null);
	}

	public void warning(String message) {
		warning(message, null);
	}

	public void info(String message) {
		info(message, null);
	}
	public void debug(String message) {
		debug(message, null);
	}
	public void develop(String message) {
		develop(message, null);
	}

	public void setSeverity(int severity) {
		this.severity = severity;
	}
}
