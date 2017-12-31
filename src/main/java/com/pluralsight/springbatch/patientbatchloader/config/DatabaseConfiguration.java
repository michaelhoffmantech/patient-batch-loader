package com.pluralsight.springbatch.patientbatchloader.config;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import liquibase.integration.spring.SpringLiquibase;

/**
 * Database configurations for the spring batch application. For the purposes of
 * this course, I'm simply leveraing an H2 database; however, its recommended
 * that you using a real production database server for all non-development
 * implementations. Includes support for JPA auditing.
 *
 */
@Configuration
@EnableJpaRepositories("com.pluralsight.springbatch.patientbatchloader.repository")
@EnableJpaAuditing(auditorAwareRef = "systemAccountAuditorAware")
@EnableTransactionManagement
public class DatabaseConfiguration {

	private final Logger log = LoggerFactory.getLogger(DatabaseConfiguration.class);

	private final Environment env;

	public DatabaseConfiguration(Environment env) {
		this.env = env;
	}

	@Bean
	public SpringLiquibase liquibase(DataSource dataSource, LiquibaseProperties liquibaseProperties) {
		SpringLiquibase liquibase = new SpringLiquibase();
		liquibase.setDataSource(dataSource);
		liquibase.setChangeLog("classpath:config/liquibase/master.xml");
		liquibase.setContexts(liquibaseProperties.getContexts());
		liquibase.setDefaultSchema(liquibaseProperties.getDefaultSchema());
		liquibase.setDropFirst(liquibaseProperties.isDropFirst());
		if (env.acceptsProfiles(Constants.SPRING_PROFILE_NO_LIQUIBASE)) {
			liquibase.setShouldRun(false);
		} else {
			liquibase.setShouldRun(liquibaseProperties.isEnabled());
			log.debug("Configuring Liquibase");
		}
		return liquibase;
	}
}
