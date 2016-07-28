/**
 * 
 */
package com.multi.enterprise.commons.jdbc.dao.mapper;

import java.io.StringReader;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;

import com.multi.enterprise.commons.jdbc.dao.JdbcRecordSqlParameterMapper;
import com.multi.enterprise.types.Persistable;
import com.multi.enterprise.types.jaxb.JAXBUtils;

/**
 * @author Robot
 *
 */
public class BaseRowMapper<T extends Persistable> implements RowMapper<T> {

	@Autowired
	JdbcRecordSqlParameterMapper<T> paramMapper;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.jdbc.core.RowMapper#mapRow(java.sql.ResultSet, int)
	 */
	@Override
	public T mapRow(ResultSet resultSet, int rowNum) throws SQLException {
		final String id = resultSet.getString(this.paramMapper.getIdColumnName());
		final T persistable = this.unmarshalPersistable(resultSet, id);
		this.populateDates(persistable, resultSet);
		this.populateRemainingFields(persistable, resultSet);
		return persistable;
	}

	protected void populateDates(final T persistable, final ResultSet resultSet) throws SQLException {
		final Date createdDate = resultSet.getTimestamp(this.paramMapper.getCreatedDateColumnName());
		final Date modified = resultSet.getTimestamp(this.paramMapper.getCreatedDateColumnName());
		persistable.setCreatedDate(createdDate);
		persistable.setModifiedDate(modified);
	}

	protected void populateRemainingFields(final T persistable, final ResultSet resultSet) throws SQLException {

	}

	@SuppressWarnings("unchecked")
	protected T unmarshalPersistable(final ResultSet resultSet, final String id) throws SQLException {
		final String className = resultSet.getString(this.paramMapper.getPersistableClassColumnName());
		final String serializedPersistable = resultSet.getString(this.paramMapper.getSerializedPersistableColumnName());

		try {
			final Class<T> clazz = (Class<T>) Class.forName(className);
			final T persistable = JAXBUtils.unmarshal(new StringReader(serializedPersistable), clazz);
			persistable.setId(id);
			return persistable;
		} catch (Exception e) {
			final String errorMsg = String.format("Exception occured while unmarshalling id %s, className %s, %s", id,
					className, e.toString());
			throw new SQLException(errorMsg, e);
		}

	}

}
