/**
 * 
 */
package com.multi.enterprise.commons.jdbc.dao.mapper;

import java.util.Objects;

import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.oxm.MarshallingException;

import com.multi.enterprise.commons.jdbc.dao.JdbcRecordSqlParameterMapper;
import com.multi.enterprise.commons.jdbc.dao.OrderedMapSqlParameterSource;
import com.multi.enterprise.commons.jdbc.exception.MarshallingDataAccessException;
import com.multi.enterprise.types.Persistable;
import com.multi.enterprise.types.jaxb.JAXBUtils;

/**
 * @author Robot
 *
 */
public abstract class BaseSqlParameterMapper<T extends Persistable> implements JdbcRecordSqlParameterMapper<T> {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.multi.enterprise.commons.jdbc.dao.JdbcRecordSqlParamaterMapper#mapInsertSqlParams(com.multi.enterprise.types
	 * .Persistable)
	 */
	@Override
	public OrderedMapSqlParameterSource mapInsertSqlParams(T persistable) {
		final OrderedMapSqlParameterSource paramSource = new OrderedMapSqlParameterSource();
		if (!this.isGeneratingKeys()) {
			paramSource.addValue(this.getIdColumnName(), persistable.getId());
		}
		this.mapCommonParameters(paramSource, persistable);
		return paramSource;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.multi.enterprise.commons.jdbc.dao.JdbcRecordSqlParamaterMapper#mapUpdateSqlParams(com.multi.enterprise.types
	 * .Persistable)
	 */
	@Override
	public OrderedMapSqlParameterSource mapUpdateSqlParams(T persistable) {
		final OrderedMapSqlParameterSource paramSource = new OrderedMapSqlParameterSource();
		this.mapIdParameter(paramSource, persistable.getId());
		return paramSource;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.multi.enterprise.commons.jdbc.dao.JdbcRecordSqlParamaterMapper#mapIdParamater(java.lang.String)
	 */
	@Override
	public SqlParameterSource mapIdParamater(String id) {
		final OrderedMapSqlParameterSource paramSource = new OrderedMapSqlParameterSource();
		this.mapIdParameter(paramSource, id);
		return paramSource;
	}

	protected void mapIdParameter(final OrderedMapSqlParameterSource paramSource, final String id) {
		paramSource.addValue(this.getIdColumnName(), id);
	}

	protected void mapCommonParameters(final OrderedMapSqlParameterSource paramSource, final T persistable) {
		paramSource.addValue(this.getPersistableClassColumnName(), persistable.getClass().getName());
		paramSource.addValue(this.getSerializedPersistableColumnName(),
				this.serializePersistableWithRecordCData(persistable));
		this.mapRemainingParameters(paramSource, persistable);

	}

	protected void mapRemainingParameters(final OrderedMapSqlParameterSource paramSource, final T persistable) {

	}

	protected String serializePersistableWithRecordCData(final T persistable) throws MarshallingException {
		if (Objects.isNull(persistable)) {
			return null;
		}
		try {
			return JAXBUtils.toXmlString(persistable, true, "record cdata");
		} catch (Exception e) {
			final String errorMsg = String.format("Exception occured while marshalling id %s, className %s, %s",
					persistable.getId(), persistable.getClass().getName(), e.toString());
			throw new MarshallingDataAccessException(errorMsg, e);
		}

	}

}
