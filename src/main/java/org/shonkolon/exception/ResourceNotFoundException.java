package org.shonkolon.exception;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

public class ResourceNotFoundException extends WebApplicationException {

	/**
	 * Create a HTTP 404 (Not Found) exception.
	 */
	public ResourceNotFoundException() {
		super("Resource not found", 404);
	}

	/**
	 * Create a HTTP 404 (Not Found) exception.
	 * 
	 * @param message
	 *            the String that is the entity of the 404 response.
	 */
	public ResourceNotFoundException(String message) {
		super(message, 404);
	}
}