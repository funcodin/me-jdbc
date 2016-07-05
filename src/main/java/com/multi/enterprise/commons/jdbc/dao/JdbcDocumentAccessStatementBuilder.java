package com.multi.enterprise.commons.jdbc.dao;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import com.multi.enterprise.types.Persistable;

/**
 * @author Robot
 *
 */
public class JdbcDocumentAccessStatementBuilder {

	private static final String INSERT_STATEMENT_TEMPLATE = "INSERT INTO %s (%s) VALUES (%s)";
	private static final String SELECT_STATEMENT_TEMPLATE = "SELECT * FROM %s WHERE %s = :%s";
	private static final String UPDATE_STATEMENT_TEMPLATE = "UPDATE %s SET %s WHERE %s = :%s";
	private static final String DELETE_STATEMENT_TEMPLATE = "DELETE FROM %s WHERE %s = :%s";
	private static final String LOCK_STATEMENT_TEMPLATE = "SELECT * FROM %s WHERE %s = :%s FOR UPDATE";

	public static <T extends Persistable> String buildInsertStatement(final OrderedMapSqlParamaterSource params,
			final JdbcDocumentSqlParamaterMapper<T> paramMapper) {
		final List<String> keys = params.getKeys();
		final String columns = StringUtils.join(keys, ", ");
		final List<String> bindVariableList = keys.stream().map(e -> ":" + e).collect(Collectors.toList());
		final String bindVariables = StringUtils.join(bindVariableList, ", ");

		return String.format(INSERT_STATEMENT_TEMPLATE, paramMapper.getTableName(), columns, bindVariables);
	}

	// CRUD

	public static <T extends Persistable> String buildSelectStatement(
			final JdbcDocumentSqlParamaterMapper<T> paramMapper) {
		return String.format(SELECT_STATEMENT_TEMPLATE, paramMapper.getTableName(), paramMapper.getIdColumnName(),
				paramMapper.getIdColumnName());
	}

	public static <T extends Persistable> String buildUpdateStatement(final OrderedMapSqlParamaterSource params,
			final JdbcDocumentSqlParamaterMapper<T> paramMapper) {
		final List<String> updateKeys = params.getKeys();
		final List<String> updateClauseList = updateKeys.stream().map(e -> e + "= :" + e).collect(Collectors.toList());
		final String updateClauses = StringUtils.join(updateClauseList, ", ");
		return String.format(UPDATE_STATEMENT_TEMPLATE, paramMapper.getTableName(), updateClauses,
				paramMapper.getIdColumnName(), paramMapper.getIdColumnName());

	}

	public static <T extends Persistable> String buildDeleteStatement(
			final JdbcDocumentSqlParamaterMapper<T> paramMapper) {
		return String.format(DELETE_STATEMENT_TEMPLATE, paramMapper.getTableName(), paramMapper.getIdColumnName(),
				paramMapper.getIdColumnName());
	}

	public static <T extends Persistable> String buildLockStatement(final JdbcDocumentSqlParamaterMapper<T> paramMapper) {
		return String.format(LOCK_STATEMENT_TEMPLATE, paramMapper.getTableName(), paramMapper.getIdColumnName(),
				paramMapper.getIdColumnName());
	}

}
