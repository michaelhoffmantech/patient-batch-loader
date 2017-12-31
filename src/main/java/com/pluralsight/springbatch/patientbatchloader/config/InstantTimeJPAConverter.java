package com.pluralsight.springbatch.patientbatchloader.config;

import java.sql.Timestamp;
import java.time.Instant;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * Converter for supporting new time apis with H2. Provides conversion between
 * Instant and Timestamp.
 */
@Converter(autoApply = true)
public class InstantTimeJPAConverter implements AttributeConverter<Instant, Timestamp> {

	@Override
	public Timestamp convertToDatabaseColumn(Instant instant) {
		return (instant == null ? null : Timestamp.from(instant));
	}

	@Override
	public Instant convertToEntityAttribute(Timestamp timestamp) {
		return (timestamp == null ? null : timestamp.toInstant());
	}

}
