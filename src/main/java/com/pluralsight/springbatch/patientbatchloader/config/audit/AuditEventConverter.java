package com.pluralsight.springbatch.patientbatchloader.config.audit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.boot.actuate.audit.AuditEvent;
import org.springframework.stereotype.Component;

import com.pluralsight.springbatch.patientbatchloader.domain.PersistentAuditEvent;

@Component
public class AuditEventConverter {

	/**
	 * Convert a list of PersistentAuditEvent to a list of AuditEvent
	 *
	 * @param persistentAuditEvents
	 *            the list to convert
	 * @return the converted list.
	 */
	public List<AuditEvent> convertToAuditEvent(Iterable<PersistentAuditEvent> persistentAuditEvents) {
		if (persistentAuditEvents == null) {
			return Collections.emptyList();
		}
		List<AuditEvent> auditEvents = new ArrayList<>();
		for (PersistentAuditEvent persistentAuditEvent : persistentAuditEvents) {
			auditEvents.add(convertToAuditEvent(persistentAuditEvent));
		}
		return auditEvents;
	}

	/**
	 * Convert a PersistentAuditEvent to an AuditEvent
	 *
	 * @param persistentAuditEvent.
	 *            the event to convert
	 * @return the converted list.
	 */
	public AuditEvent convertToAuditEvent(PersistentAuditEvent persistentAuditEvent) {
		if (persistentAuditEvent == null) {
			return null;
		}
		return new AuditEvent(
				Date.from(persistentAuditEvent.getAuditEventDate()),
				persistentAuditEvent.getUserId(), persistentAuditEvent.getAuditEventType(),
				convertDataToObjects(persistentAuditEvent.getData()));
	}

	/**
	 * Internal conversion. This is needed to support the current SpringBoot
	 * actuator AuditEventRepository interface
	 *
	 * @param data
	 *            the data to convert
	 * @return a map of String, Object
	 */
	public Map<String, Object> convertDataToObjects(Map<String, String> data) {
		Map<String, Object> results = new HashMap<>();

		if (data != null) {
			for (Map.Entry<String, String> entry : data.entrySet()) {
				results.put(entry.getKey(), entry.getValue());
			}
		}
		return results;
	}

	/**
	 * Internal conversion. This method will allow to save additional data. By
	 * default, it will save the object as string
	 *
	 * @param data
	 *            the data to convert
	 * @return a map of String, String
	 */
	public Map<String, String> convertDataToStrings(Map<String, Object> data) {
		Map<String, String> results = new HashMap<>();

		if (data != null) {
			for (Map.Entry<String, Object> entry : data.entrySet()) {
				Object object = entry.getValue();

				if (object != null) {
					results.put(entry.getKey(), object.toString());
				} else {
					results.put(entry.getKey(), "null");
				}
			}
		}

		return results;
	}
}
