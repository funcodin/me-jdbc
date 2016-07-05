/**
 * 
 */
package com.multi.enterprise.commons.controllers;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.multi.enterprise.types.exception.ServiceException;

/**
 * @author Robot
 *
 */
public interface CrudController<T> {

	@RequestMapping(value = "", method = RequestMethod.POST)
	public T create(@RequestBody T create) throws ServiceException;

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public T getById(@PathVariable String id) throws ServiceException;

	@RequestMapping(value = "", method = RequestMethod.PUT)
	public T update(@RequestBody T update) throws ServiceException;

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public void delete(@PathVariable String id) throws ServiceException;

}
