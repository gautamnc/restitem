/**
 * 
 */
package com.restitem.common.exception;

/**
 * This is a generic exception class defined for the whole Rest item
 * application. Please define/use any co existing specialized exceptions for any
 * scenario specific exception definitions.
 * 
 * @author gautamnc
 * 
 */
public class StandardException extends RuntimeException {

	/**
	 * Custom constructor with errorid and error message.
	 * 
	 * @param errorId
	 * @param errorMessage
	 */
	public StandardException(String errorId, String errorMessage) {
		super();
		this.errorId = errorId;
		this.errorMessage = errorMessage;
	}

	private String errorId;
	private String errorMessage;

	/**
	 * UID in case we plan to serialize the bean between producers/consumers
	 */
	private static final long serialVersionUID = 1606342479419626427L;

	/**
	 * @return the errorId
	 */
	public String getErrorId() {
		return errorId;
	}

	/**
	 * @param errorId
	 *            the errorId to set
	 */
	public void setErrorId(String errorId) {
		this.errorId = errorId;
	}

	/**
	 * @return the errorMessage
	 */
	public String getErrorMessage() {
		return errorMessage;
	}

	/**
	 * @param errorMessage
	 *            the errorMessage to set
	 */
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

}
