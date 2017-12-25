package com.pluralsight.springbatch.patientbatchloader.repository;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.actuate.audit.AuditEvent;
import org.springframework.boot.actuate.audit.AuditEventRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.pluralsight.springbatch.patientbatchloader.config.Constants;
import com.pluralsight.springbatch.patientbatchloader.config.audit.AuditEventConverter;
import com.pluralsight.springbatch.patientbatchloader.domain.PersistentAuditEvent;

/**
 * An implementation of Spring Boot's AuditEventRepository.
 */
@Repository
public class CustomAuditEventRepository implements AuditEventRepository {

	/**
	 * Should be the same as in Liquibase migration.
	 */
	protected static final int EVENT_DATA_COLUMN_MAX_LENGTH = 255;

	private final PersistenceAuditEventRepository persistenceAuditEventRepository;

	private final AuditEventConverter auditEventConverter;

	private final Logger log = LoggerFactory.getLogger(getClass());

	public CustomAuditEventRepository(PersistenceAuditEventRepository persistenceAuditEventRepository,
			AuditEventConverter auditEventConverter) {
		this.persistenceAuditEventRepository = persistenceAuditEventRepository;
		this.auditEventConverter = auditEventConverter;
	}

	@Override
	public List<AuditEvent> find(Date after) {
		Iterable<PersistentAuditEvent> persistentAuditEvents = persistenceAuditEventRepository
				.findByAuditEventDateAfter(after.toInstant());
		return auditEventConverter.convertToAuditEvent(persistentAuditEvents);
	}

	@Override
	public List<AuditEvent> find(String principal, Date after) {
		Iterable<PersistentAuditEvent> persistentAuditEvents;
		if (after == null) {
			persistentAuditEvents = persistenceAuditEventRepository.findByUserId(Constants.SYSTEM_ACCOUNT);
		} else {
			persistentAuditEvents = persistenceAuditEventRepository
					.findByUserIdAndAuditEventDateAfter(Constants.SYSTEM_ACCOUNT, after.toInstant());
		}
		return auditEventConverter.convertToAuditEvent(persistentAuditEvents);
	}

	@Override
	public List<AuditEvent> find(String principal, Date after, String type) {
		Iterable<PersistentAuditEvent> persistentAuditEvents = persistenceAuditEventRepository
				.findByUserIdAndAuditEventDateAfterAndAuditEventType(Constants.SYSTEM_ACCOUNT, after.toInstant(), type);
		return auditEventConverter.convertToAuditEvent(persistentAuditEvents);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void add(AuditEvent event) {
		PersistentAuditEvent persistentAuditEvent = new PersistentAuditEvent();
		persistentAuditEvent.setUserId(Constants.SYSTEM_ACCOUNT);
		persistentAuditEvent.setAuditEventType(event.getType());
		persistentAuditEvent
				.setAuditEventDate(event.getTimestamp().toInstant());
		Map<String, String> eventData = auditEventConverter.convertDataToStrings(event.getData());
		persistentAuditEvent.setData(truncate(eventData));
		persistenceAuditEventRepository.save(persistentAuditEvent);
	}

	/**
	 * Truncate event data that might exceed column length.
	 */
	private Map<String, String> truncate(Map<String, String> data) {
		Map<String, String> results = new HashMap<>();

		if (data != null) {
			for (Map.Entry<String, String> entry : data.entrySet()) {
				String value = entry.getValue();
				if (value != null) {
					int length = value.length();
					if (length > EVENT_DATA_COLUMN_MAX_LENGTH) {
						value = value.substring(0, EVENT_DATA_COLUMN_MAX_LENGTH);
						log.warn(
								"Event data for {} too long ({}) has been truncated to {}. Consider increasing column width.",
								entry.getKey(), length, EVENT_DATA_COLUMN_MAX_LENGTH);
					}
				}
				results.put(entry.getKey(), value);
			}
		}
		return results;
	}
}
