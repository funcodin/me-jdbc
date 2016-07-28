/**
 * 
 */
package com.multi.enterprise.commons.jdbc.dao;

import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import com.multi.enterprise.types.Persistable;

/**
 * @author Robot
 *
 */
public interface JdbcRecordSqlParamaterMapper<T extends Persistable> {

	public OrderedMapSqlParamaterSource mapInsertSqlParams(T persistable);

	public OrderedMapSqlParamaterSource mapUpdateSqlParams(T persistable);

	public SqlParameterSource mapIdParamater(String id);

	public default boolean isGeneratingKeys() {
		return false;
	}

	public String getTableName();

	public String getIdColumnName();

	public default String getPersistableClassColumnName() {
		return "object_class_name";
	}

	public default String getSerializedPersistableColumnName() {
		return "serialized_object";
	}

	public default String getCreatedDateColumnName() {
		return "created_date";
	}

	public default String getModifiedDateColumnName() {
		return "modified_date";
	}

}
