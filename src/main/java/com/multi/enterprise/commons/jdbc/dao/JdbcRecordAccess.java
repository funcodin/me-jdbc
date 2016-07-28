/**
 * 
 */
package com.multi.enterprise.commons.jdbc.dao;

import org.springframework.dao.DataAccessException;

import com.multi.enterprise.types.Persistable;

/**
 * @author Robot
 *
 */
public interface JdbcRecordAccess<T extends Persistable> extends RecordAccess<T> {

	public T lockById(String id) throws DataAccessException;

}
