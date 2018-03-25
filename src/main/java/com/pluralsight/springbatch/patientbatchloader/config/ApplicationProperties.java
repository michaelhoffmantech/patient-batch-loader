package com.pluralsight.springbatch.patientbatchloader.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Properties specific to Patient Batch Loader.
 * <p>
 * Properties are configured in the application.yml file.
 */
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
public class ApplicationProperties {


}
