/**
 * 
 */
package com.multi.enterprise.commons.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.multi.enterprise.types.exception.EntityNotFoundException;
import com.multi.enterprise.types.exception.ServiceException;

/**
 * @author Robot
 *
 */
@ControllerAdvice
public class ControllerAdvisor {

	public static final Logger log = LoggerFactory.getLogger(ControllerAdvisor.class);

	@ExceptionHandler(value = { IllegalArgumentException.class })
	@ResponseBody
	public ResponseEntity<String> handleIllegalArugmentException(final ServiceException exception,
			final HttpServletRequest req, final HttpServletResponse res) {
		log.warn("Service error processing {} {} ", req.getMethod(), req.getRequestURI(), exception);
		return new ResponseEntity<String>(exception.getMessage(), HttpStatus.valueOf(exception.getHttpStatus()));
	}

	@ExceptionHandler(value = { EntityNotFoundException.class })
	@ResponseBody
	public ResponseEntity<String> handleEntityNotFoundException(final ServiceException exception,
			final HttpServletRequest req, final HttpServletResponse res) {
		log.warn("Entity not found exception  {} {} ", req.getMethod(), req.getRequestURI(), exception);
		return new ResponseEntity<String>(exception.getMessage(), HttpStatus.valueOf(exception.getHttpStatus()));
	}

	@ExceptionHandler(value = { ServiceException.class })
	@ResponseBody
	public ResponseEntity<String> handleServiceException(final ServiceException exception,
			final HttpServletRequest req, final HttpServletResponse res) {
		log.warn("Service exception  {} {} ", req.getMethod(), req.getRequestURI(), exception);
		return new ResponseEntity<String>(exception.getMessage(), HttpStatus.valueOf(exception.getHttpStatus()));
	}

	@ExceptionHandler(value = { Throwable.class })
	@ResponseBody
	public ResponseEntity<String> handleGenericException(final ServiceException exception,
			final HttpServletRequest req, final HttpServletResponse res) {
		log.warn("Unforseen exception  {} {} ", req.getMethod(), req.getRequestURI(), exception);
		return new ResponseEntity<String>(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(value = { DataAccessException.class })
	@ResponseBody
	public ResponseEntity<String> handleDataAccessException(final ServiceException exception,
			final HttpServletRequest req, final HttpServletResponse res) {
		log.warn("DataAccess exception  {} {} ", req.getMethod(), req.getRequestURI(), exception);
		return new ResponseEntity<String>(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
