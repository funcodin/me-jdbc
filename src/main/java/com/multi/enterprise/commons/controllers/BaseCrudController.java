/**
 * 
 */
package com.multi.enterprise.commons.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.multi.enterprise.commons.service.EntityService;
import com.multi.enterprise.types.Persistable;
import com.multi.enterprise.types.exception.ServiceException;

/**
 * @author Robot
 *
 */
public abstract class BaseCrudController<T extends Persistable> implements CrudController<T> {

	@Autowired
	protected EntityService<T> entityService;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.multi.enterprise.commons.controllers.CrudController#create(java.lang.Object)
	 */
	@Override
	@RequestMapping(value = "", method = RequestMethod.POST)
	public T create(@RequestBody final T create) throws ServiceException {
		final T entity = this.entityService.create(create);
		return entity;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.multi.enterprise.commons.controllers.CrudController#getById(java.lang.String)
	 */
	@Override
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public T getById(@PathVariable final String id) throws ServiceException {
		final T entity = this.entityService.getById(id);
		return entity;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.multi.enterprise.commons.controllers.CrudController#update(java.lang.Object, java.lang.String)
	 */
	@Override
	@RequestMapping(value = "", method = RequestMethod.PUT)
	public T update(@RequestBody final T update) throws ServiceException {
		final T entity = this.entityService.update(update);
		return entity;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.multi.enterprise.commons.controllers.CrudController#delete(java.lang.String)
	 */
	@Override
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public void delete(@PathVariable final String id) throws ServiceException {
		this.entityService.delete(id);
	}

}
