package com.pluralsight.springbatch.patientbatchloader.config;

import javax.sql.DataSource;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jmx.export.MBeanExporter;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import liquibase.integration.spring.SpringLiquibase;

import java.util.Properties;

/**
 * Database configurations for the spring batch application. For the purposes of
 * this course, I'm simply leveraging an H2 database; however, its recommended
 * that you using a real production database server for all non-development
 * implementations. Includes support for JPA auditing.
 *
 */
@Configuration
@EnableJpaRepositories(
	value = "com.pluralsight.springbatch.patientbatchloader",
	entityManagerFactoryRef = "batchEntityManagerFactory")
@EnableTransactionManagement
public class DatabaseConfiguration {

	private final Logger log = LoggerFactory.getLogger(DatabaseConfiguration.class);

	private final Environment env;

	public DatabaseConfiguration(Environment env) {
		this.env = env;
	}

    @Bean(name = "batchDataSource")
    public DataSource batchDataSource() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(env.getRequiredProperty("spring.datasource.url"));
        config.setUsername(env.getProperty("spring.datasource.username"));
        config.setPassword(env.getProperty("spring.datasource.password"));
        config.setMinimumIdle(env.getProperty("spring.datasource.min-idle",
            Integer.class, 2));
        config.setMaximumPoolSize(env.getProperty("spring.datasource.max-active",
            Integer.class, 100));
        config.setTransactionIsolation("TRANSACTION_READ_COMMITTED");
        config.setRegisterMbeans(true);
        return new HikariDataSource(config);
    }

    @Bean(name = "batchJpaVendorAdapter")
    public JpaVendorAdapter batchJpaVendorAdapter() {
        return new HibernateJpaVendorAdapter();
    }

    @Bean(name = "batchEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean batchEntityManagerFactory() {
        LocalContainerEntityManagerFactoryBean emfBean =
            new LocalContainerEntityManagerFactoryBean();
        emfBean.setDataSource(batchDataSource());
        emfBean.setPackagesToScan("com.pluralsight.springbatch.patientbatchloader");
        emfBean.setBeanName("batchEntityManagerFactory");
        emfBean.setJpaVendorAdapter(batchJpaVendorAdapter());

        Properties jpaProps = new Properties();
        jpaProps.put("hibernate.physical_naming_strategy",
            env.getProperty("spring.jpa.hibernate.naming.physical-strategy"));
        jpaProps.put("hibernate.hbm2ddl.auto", env.getProperty(
            "spring.jpa.hibernate.ddl-auto", "none"));
        jpaProps.put("hibernate.jdbc.fetch_size", env.getProperty(
            "spring.jpa.properties.hibernate.jdbc.fetch_size",
            "200"));

        Integer batchSize = env.getProperty(
            "spring.jpa.properties.hibernate.jdbc.batch_size",
            Integer.class, 100);
        if (batchSize > 0) {
            jpaProps.put("hibernate.jdbc.batch_size", batchSize);
            jpaProps.put("hibernate.order_inserts", "true");
            jpaProps.put("hibernate.order_updates", "true");
        }

        jpaProps.put("hibernate.show_sql", env.getProperty(
            "spring.jpa.properties.hibernate.show_sql", "false"));
        jpaProps.put("hibernate.format_sql",env.getProperty(
            "spring.jpa.properties.hibernate.format_sql", "false"));

        emfBean.setJpaProperties(jpaProps);
        return emfBean;
    }

    @Bean(name = "batchTransactionManager")
    public PlatformTransactionManager transactionManager() {
        return new JpaTransactionManager(batchEntityManagerFactory().getObject());
    }

    @Bean
    public MBeanExporter exporter() {
        final MBeanExporter exporter = new MBeanExporter();
        exporter.setExcludedBeans("batchDataSource");
        return exporter;
    }

    @Bean
	public SpringLiquibase liquibase(LiquibaseProperties liquibaseProperties) {
		SpringLiquibase liquibase = new SpringLiquibase();
		liquibase.setDataSource(batchDataSource());
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
