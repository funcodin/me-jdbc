/**
 * 
 */
package com.multi.enterprise.commons.service;

import java.util.Date;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;

import com.multi.enterprise.types.Persistable;
import com.multi.enterprise.types.dao.RecordAccess;
import com.multi.enterprise.types.exception.EntityNotFoundException;
import com.multi.enterprise.types.exception.ServiceException;
import com.multi.enterprise.types.service.RecordService;

/**
 * @author Robot
 *
 */
public abstract class BaseRecordService<T extends Persistable> implements RecordService<T> {

	@Autowired
	protected RecordAccess<T> recordAccess;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.multi.enterprise.commons.service.EntityService#create(com.multi.enterprise.commons.types.Persistable)
	 */
	@Override
	public T create(T entity) throws ServiceException {
		entity.setCasValue(null);

		final Date date = new Date();
		entity.setCreatedDate(date);
		entity.setModifiedDate(date);

		final T newEntityCreated = this.recordAccess.create(entity);

		return newEntityCreated;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.multi.enterprise.commons.service.EntityService#getById(java.lang.String)
	 */
	@Override
	public T getById(String id) throws ServiceException {
		return this.recordAccess.getById(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.multi.enterprise.commons.service.EntityService#update(com.multi.enterprise.commons.types.Persistable)
	 */
	@Override
	public T update(T entity) throws ServiceException {
		final T existingEntity = this.recordAccess.getById(entity.getId());
		if (Objects.isNull(existingEntity)) {
			throw new EntityNotFoundException("Cannot Update. Entity not found for id " + entity.getId());
		}
		entity.setModifiedDate(new Date());
		entity.setCreatedDate(existingEntity.getCreatedDate());
		T updated = this.recordAccess.update(entity);
		return updated;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.multi.enterprise.commons.service.EntityService#delete(java.lang.String)
	 */
	@Override
	public void delete(String id) throws ServiceException {
		final T existingEntity = this.recordAccess.getById(id);
		if (Objects.isNull(existingEntity)) {
			throw new EntityNotFoundException("Cannot Delete. Entity not found for id " + id);
		}
		this.recordAccess.delete(existingEntity);
	}
}
