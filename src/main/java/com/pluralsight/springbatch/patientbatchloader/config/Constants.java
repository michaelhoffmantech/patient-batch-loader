package com.pluralsight.springbatch.patientbatchloader.config;

/**
 * Application constants.
 */
public final class Constants {

	/**
	 * Default system account used for all database interaction by the batch
	 * processor
	 */
	public static final String SYSTEM_ACCOUNT = "system";

	/**
	 * Assure locale-specific instances leverage the same default
	 */
	public static final String DEFAULT_LANG_KEY = "en";

	/**
	 * Spring profile for running in "development mode"
	 */
	public static final String SPRING_PROFILE_DEVELOPMENT = "dev";

	/**
	 * Spring profile for running in "production mode"
	 */
	public static final String SPRING_PROFILE_PRODUCTION = "prod";

	/**
	 * Spring profile for not including liquibase database schema management at
	 * run-time.
	 */
	public static final String SPRING_PROFILE_NO_LIQUIBASE = "no-liquibase";

	private Constants() {
	}
}
