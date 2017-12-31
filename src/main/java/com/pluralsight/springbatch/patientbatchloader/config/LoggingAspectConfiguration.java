package com.pluralsight.springbatch.patientbatchloader.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.core.env.Environment;

import com.pluralsight.springbatch.patientbatchloader.aop.logging.LoggingAspect;

/**
 * Enables the logging aspect. If eventually using this for production, its
 * recommended to annotate the class for the Spring Profile "dev" only so that
 * the logging aspect isn't applied in production mode.
 */
@Configuration
@EnableAspectJAutoProxy
public class LoggingAspectConfiguration {

	@Bean
	public LoggingAspect loggingAspect(Environment env) {
		return new LoggingAspect(env);
	}
}
