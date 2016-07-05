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
public interface JdbcDocumentAccess<T extends Persistable> extends DocumentAccess<T> {

	public T lockById(String id) throws DataAccessException;

}
