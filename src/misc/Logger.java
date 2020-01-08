package misc;

import static application.AppConfigurator.LoggerTopics;


/**
 * Interface of a simple logger.
 * 
 * @author sgra64
 *
 */
public interface Logger {

	/**
	 * Factory method to create and return a Logger instance.
	 * 
	 * @param clazz a class is used to identify and distinguish logger instances.
	 * @return created logger instance.
	 */
	public static Logger getInstance( final Class<?> clazz ) {
		return LoggerImpl.getInstance( clazz );
	}

	/**
	 * Method to log a message.
	 * 
	 * @param topic logs are categorized by (String) topics.
	 * @param msg log message
	 * @param args further log information
	 */
	public void log( final LoggerTopics topic, final String msg, final Object... args );

}
