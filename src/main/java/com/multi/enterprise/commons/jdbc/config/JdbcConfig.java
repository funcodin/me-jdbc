/**
 * 
 */
package com.multi.enterprise.commons.jdbc.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;

import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
 * @author Robot
 *
 */
@Configuration
public class JdbcConfig {

	@Value("${jdbc.url}")
	private String jdbcUrl;
	@Value("${jdbc.username}")
	private String jdbcUserName;
	@Value("${jdbc.password}")
	private String jdbcPassword;
	@Value("${jdbc.driver.class:com.mysql.jdbc.Driver}")
	private String jdbcDriverClass;

	@Bean
	public ComboPooledDataSource comboPooledDataSource() throws Exception {
		final ComboPooledDataSource comboPooledDataSource = new ComboPooledDataSource();
		System.out.println("****Testing if the params are right");
		comboPooledDataSource.setDriverClass(this.jdbcDriverClass);
		comboPooledDataSource.setJdbcUrl(this.jdbcUrl);
		comboPooledDataSource.setUser(this.jdbcUserName);
		System.out.println(this.jdbcUserName);
		comboPooledDataSource.setPassword(this.jdbcPassword);
		comboPooledDataSource.setTestConnectionOnCheckout(true);
		comboPooledDataSource.setInitialPoolSize(8);
		comboPooledDataSource.setMaxPoolSize(20);
		comboPooledDataSource.setNumHelperThreads(5);
		comboPooledDataSource.setIdleConnectionTestPeriod(600);

		return comboPooledDataSource;
	}

	@Bean
	public NamedParameterJdbcTemplate namedParameterJdbcTemplate() throws Exception {
		return new NamedParameterJdbcTemplate(this.jdbcTemplate());
	}

	public JdbcTemplate jdbcTemplate() throws Exception {
		final JdbcTemplate template = new JdbcTemplate(this.comboPooledDataSource());
		template.execute("SET time_zone = \'+00:00\'");
		return template;

	}

	@Bean
	public DataSourceTransactionManager transactionManager() throws Exception {
		final DataSourceTransactionManager transactionManager = new DataSourceTransactionManager();
		transactionManager.setDataSource(this.comboPooledDataSource());
		return transactionManager;
	}

	public TransactionTemplate transcationTemplate() throws Exception {

		final TransactionTemplate transcationTemplate = new TransactionTemplate();
		transcationTemplate.setTransactionManager(this.transactionManager());
		transcationTemplate.setIsolationLevel(TransactionDefinition.ISOLATION_READ_COMMITTED);
		transcationTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
		return transcationTemplate;

	}

}
