package com.pluralsight.springbatch.patientbatchloader.config;

import java.sql.Types;

import org.hibernate.dialect.H2Dialect;

/**
 * Resolves an issue with column types in H2, especially around new time apis. 
 */
public class FixedH2Dialect extends H2Dialect {

	public FixedH2Dialect() {
		super();
		registerColumnType(Types.FLOAT, "real");
		registerColumnType(Types.BINARY, "varbinary");
	}
}
