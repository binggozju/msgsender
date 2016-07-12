package org.binggo.msgsender.service;

/**
 * <p>A sender exception is raised whenever a sender operation fails.</p>
 */
public class SenderException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	/**
	 * @param message the exception message
	 */
	public SenderException(String message) {
		super(message);
	}
	
	/**
	 * @param ex the causal exception
	 */
	public SenderException(Throwable ex) {
		super(ex);
	}
	
	/**
	 * @param message exception message
	 * @param ex the causal exception
	 */
	public SenderException(String message, Throwable ex) {
		super(message, ex);
	}

}
