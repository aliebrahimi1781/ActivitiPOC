/**
 * 
 */
package org.hekmatof.activiti;

import javax.sql.DataSource;

import org.activiti.spring.ProcessEngineFactoryBean;
import org.activiti.spring.SpringProcessEngineConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

/**
 * @author esa
 *
 */
@Configuration
@EnableAutoConfiguration
@ComponentScan
public class ApplicationSpring {
	private static Logger logger = LoggerFactory
			.getLogger(ApplicationSpring.class);

	public static void main(String[] args) {
		logger.debug("starting app...");
		SpringApplication.run(ApplicationSpring.class, args);
		logger.info("application started.");
	}

	@Bean
	DataSource dataSource() {
		return DataSourceBuilder.create().url("jdbc:h2:mem:mydb")
				.username("sa").build();
	}

	@Bean
	DataSourceTransactionManager transactionManager() {
		return new DataSourceTransactionManager(dataSource());
	}

	@Bean
	SpringProcessEngineConfiguration processEngineConfiguration() {
		SpringProcessEngineConfiguration engine = new SpringProcessEngineConfiguration();
		engine.setDataSource(dataSource());
		engine.setTransactionManager(transactionManager());
		engine.setDatabaseSchemaUpdate("true");
		return engine;
	}

	@Bean
	ProcessEngineFactoryBean processEngine(
			SpringProcessEngineConfiguration processEngineConfiguration) {
		ProcessEngineFactoryBean processEngine = new ProcessEngineFactoryBean();
		processEngine.setProcessEngineConfiguration(processEngineConfiguration);
		return processEngine;
	}

}
