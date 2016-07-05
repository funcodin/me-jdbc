package com.multi.enterprise.commons.jdbc.dao;

import java.util.Objects;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import com.multi.enterprise.types.Persistable;

/**
 * @author Robot
 *
 */
public abstract class BaseJdbcDocumentAccess<T extends Persistable> implements JdbcDocumentAccess<T> {

	private static final Logger log = LoggerFactory.getLogger(BaseJdbcDocumentAccess.class);

	private String INSERT_STATEMENT;
	private String SELECT_STATEMENT;
	private String UPDATE_STATEMENT;
	private String DELETE_STATEMENT;
	private String LOCK_STATEMENT;

	@Autowired
	protected NamedParameterJdbcTemplate jdbcTempalte;

	@Autowired
	protected RowMapper<T> rowMapper;

	@Autowired
	protected JdbcDocumentSqlParamaterMapper<T> parameterMapper;

	@PostConstruct
	public void init() {
		this.SELECT_STATEMENT = JdbcDocumentAccessStatementBuilder.buildSelectStatement(this.parameterMapper);
		this.DELETE_STATEMENT = JdbcDocumentAccessStatementBuilder.buildDeleteStatement(this.parameterMapper);
		this.LOCK_STATEMENT = JdbcDocumentAccessStatementBuilder.buildLockStatement(this.parameterMapper);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.multi.enterprise.commons.jdbc.dao.DocumentAccess#create(com.multi.enterprise.commons.types.Persistable)
	 */
	@Override
	public T create(final T create) {
		final OrderedMapSqlParamaterSource params = this.parameterMapper.mapInsertSqlParams(create);

		if (Objects.isNull(this.INSERT_STATEMENT))
			this.INSERT_STATEMENT = JdbcDocumentAccessStatementBuilder.buildInsertStatement(params,
					this.parameterMapper);

		if (this.parameterMapper.isGeneratingKeys()) {
			final KeyHolder holder = new GeneratedKeyHolder();
			this.jdbcTempalte.update(this.INSERT_STATEMENT, params, holder);
			create.setId(String.valueOf(holder.getKey().intValue()));
		} else {
			this.jdbcTempalte.update(this.INSERT_STATEMENT, params);
		}
		return create;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.multi.enterprise.commons.jdbc.dao.DocumentAccess#getById(java.lang.String)
	 */
	@Override
	public T getById(String id) {
		return this.jdbcTempalte.queryForObject(this.SELECT_STATEMENT, this.parameterMapper.mapIdParamater(id),
				this.rowMapper);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.multi.enterprise.commons.jdbc.dao.DocumentAccess#update(com.multi.enterprise.commons.types.Persistable)
	 */
	@Override
	public T update(T update) {
		final OrderedMapSqlParamaterSource params = this.parameterMapper.mapUpdateSqlParams(update);
		if (Objects.isNull(this.UPDATE_STATEMENT))
			this.UPDATE_STATEMENT = JdbcDocumentAccessStatementBuilder.buildUpdateStatement(params,
					this.parameterMapper);

		this.jdbcTempalte.update(this.UPDATE_STATEMENT, params);
		return update;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.multi.enterprise.commons.jdbc.dao.DocumentAccess#delete(com.multi.enterprise.commons.types.Persistable)
	 */
	@Override
	public void delete(T delete) {
		this.jdbcTempalte.update(this.DELETE_STATEMENT, this.parameterMapper.mapIdParamater(delete.getId()));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.multi.enterprise.commons.jdbc.dao.JdbcDocumentAccess#lockById(java.lang.String)
	 */
	@Override
	public T lockById(String id) throws DataAccessException {
		return this.jdbcTempalte.queryForObject(this.LOCK_STATEMENT, this.parameterMapper.mapIdParamater(id),
				this.rowMapper);
	}

	/**
	 * @return the iNSERT_STATEMENT
	 */
	public String getINSERT_STATEMENT() {
		return INSERT_STATEMENT;
	}

	/**
	 * @return the sELECT_STATEMENT
	 */
	public String getSELECT_STATEMENT() {
		return SELECT_STATEMENT;
	}

	/**
	 * @return the uPDATE_STATEMENT
	 */
	public String getUPDATE_STATEMENT() {
		return UPDATE_STATEMENT;
	}

	/**
	 * @return the dELETE_STATEMENT
	 */
	public String getDELETE_STATEMENT() {
		return DELETE_STATEMENT;
	}

	/**
	 * @return the lOCK_STATEMENT
	 */
	public String getLOCK_STATEMENT() {
		return LOCK_STATEMENT;
	}

}
