/**
 * 
 */
package com.multi.enterprise.commons.jdbc.dao;

import java.util.List;
import java.util.Objects;

import org.apache.commons.collections4.OrderedMap;
import org.apache.commons.collections4.OrderedMapIterator;
import org.apache.commons.collections4.map.LinkedMap;
import org.springframework.jdbc.core.SqlParameterValue;
import org.springframework.jdbc.core.namedparam.AbstractSqlParameterSource;
import org.springframework.util.Assert;

/**
 * @author Robot
 *
 */
public class OrderedMapSqlParamaterSource extends AbstractSqlParameterSource {

	private final LinkedMap<String, Object> values = new LinkedMap<String, Object>();

	public OrderedMapSqlParamaterSource() {
	}

	public OrderedMapSqlParamaterSource(final String paramName, final Object value) {
		this.addValue(paramName, value);
	}

	public List<String> getKeys() {
		return this.values.asList();
	}

	public Object getValue(final String paramName) throws IllegalArgumentException {
		if (this.hasValue(paramName)) {
			return this.values.get(paramName);
		}
		throw new IllegalArgumentException("Value not found for key " + paramName);
	}

	public boolean hasValue(final String paramName) {
		return this.values.containsKey(paramName);
	}

	public OrderedMapSqlParamaterSource addValues(final OrderedMap<String, ?> values) {
		if (Objects.nonNull(values)) {
			for (final OrderedMapIterator<String, ?> iterator = values.mapIterator(); iterator.hasNext();) {
				final String key = iterator.next();
				final Object value = iterator.getValue();
				this.values.put(key, value);
				if (value instanceof SqlParameterValue) {
					final SqlParameterValue paramValue = (SqlParameterValue) value;
					this.registerSqlType(key, paramValue.getSqlType());
				}
			}
		}
		return this;
	}

	public OrderedMapSqlParamaterSource addValue(final String paramName, final Object value, final int sqlType,
			final String typeName) {

		Assert.notNull(paramName, "Param name is required ");
		this.values.put(paramName, value);
		this.registerSqlType(paramName, sqlType);
		this.registerTypeName(paramName, typeName);
		return this;
	}

	public OrderedMapSqlParamaterSource addValue(final String paramName, final Object value, final int sqlType) {
		Assert.notNull(paramName, "Param name is required");
		this.values.put(paramName, value);
		this.registerSqlType(paramName, sqlType);
		return this;
	}

	public OrderedMapSqlParamaterSource addValue(final String paramName, final Object value) {
		Assert.notNull(paramName, " Param name is required ");
		this.values.put(paramName, value);
		if (value instanceof SqlParameterValue)
			this.registerSqlType(paramName, ((SqlParameterValue) value).getSqlType());
		return this;
	}

}
