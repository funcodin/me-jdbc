/**
 * 
 */
package com.multi.enterprise.commons.jdbc.exception;

import org.springframework.dao.DataAccessException;

/**
 * @author Robot
 *
 */
@SuppressWarnings("serial")
public class MarshallingDataAccessException extends DataAccessException {

	/**
	 * @param msg
	 */
	public MarshallingDataAccessException(final String msg) {
		super(msg);
	}

	public MarshallingDataAccessException(final String msg, final Throwable throwable) {
		super(msg, throwable);
	}
}
