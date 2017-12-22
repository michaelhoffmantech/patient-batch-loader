package com.pluralsight.springbatch.patientbatchloader.repository;

import static com.pluralsight.springbatch.patientbatchloader.repository.CustomAuditEventRepository.EVENT_DATA_COLUMN_MAX_LENGTH;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.audit.AuditEvent;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.pluralsight.springbatch.patientbatchloader.PatientBatchLoaderApp;
import com.pluralsight.springbatch.patientbatchloader.config.Constants;
import com.pluralsight.springbatch.patientbatchloader.config.audit.AuditEventConverter;
import com.pluralsight.springbatch.patientbatchloader.domain.PersistentAuditEvent;

/**
 * Test class for the CustomAuditEventRepository class.
 *
 * @see CustomAuditEventRepository
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PatientBatchLoaderApp.class)
@Transactional
public class CustomAuditEventRepositoryIntTest {

    @Autowired
    private PersistenceAuditEventRepository persistenceAuditEventRepository;

    @Autowired
    private AuditEventConverter auditEventConverter;

    private CustomAuditEventRepository customAuditEventRepository;

    private PersistentAuditEvent testUserEvent;

    private PersistentAuditEvent testOtherUserEvent;

    private PersistentAuditEvent testOldUserEvent;

    @Before
    public void setup() {
        customAuditEventRepository = new CustomAuditEventRepository(persistenceAuditEventRepository, auditEventConverter);
        persistenceAuditEventRepository.deleteAll();
        Instant oneHourAgo = Instant.now().minusSeconds(3600);

        testUserEvent = new PersistentAuditEvent();
        testUserEvent.setUserId(Constants.SYSTEM_ACCOUNT);
        testUserEvent.setAuditEventType("test-type");
        testUserEvent.setAuditEventDate(oneHourAgo);
        Map<String, String> data = new HashMap<>();
        data.put("test-key", "test-value");
        testUserEvent.setData(data);

        testOldUserEvent = new PersistentAuditEvent();
        testOldUserEvent.setUserId(Constants.SYSTEM_ACCOUNT);
        testOldUserEvent.setAuditEventType("test-type");
        testOldUserEvent.setAuditEventDate(oneHourAgo.minusSeconds(10000));

        testOtherUserEvent = new PersistentAuditEvent();
        testOtherUserEvent.setUserId(Constants.SYSTEM_ACCOUNT);
        testOtherUserEvent.setAuditEventType("test-type");
        testOtherUserEvent.setAuditEventDate(oneHourAgo);
    }

    @Test
    public void testFindAfter() {
        persistenceAuditEventRepository.save(testUserEvent);
        persistenceAuditEventRepository.save(testOldUserEvent);

        List<AuditEvent> events =
            customAuditEventRepository.find(Date.from(testUserEvent.getAuditEventDate().minusSeconds(3600)));
        assertThat(events).hasSize(1);
        AuditEvent event = events.get(0);
        assertThat(event.getPrincipal()).isEqualTo(testUserEvent.getUserId());
        assertThat(event.getType()).isEqualTo(testUserEvent.getAuditEventType());
        assertThat(event.getData()).containsKey("test-key");
        assertThat(event.getData().get("test-key").toString()).isEqualTo("test-value");
        assertThat(event.getTimestamp()).isEqualTo(Date.from(testUserEvent.getAuditEventDate()));
    }

    @Test
    public void testFindByPrincipal() {
        persistenceAuditEventRepository.save(testUserEvent);
        persistenceAuditEventRepository.save(testOldUserEvent);
        persistenceAuditEventRepository.save(testOtherUserEvent);

        List<AuditEvent> events = customAuditEventRepository
            .find("test-user", Date.from(testUserEvent.getAuditEventDate().minusSeconds(3600)));
        assertThat(events).hasSize(1);
        AuditEvent event = events.get(0);
        assertThat(event.getPrincipal()).isEqualTo(testUserEvent.getUserId());
        assertThat(event.getType()).isEqualTo(testUserEvent.getAuditEventType());
        assertThat(event.getData()).containsKey("test-key");
        assertThat(event.getData().get("test-key").toString()).isEqualTo("test-value");
        assertThat(event.getTimestamp()).isEqualTo(Date.from(testUserEvent.getAuditEventDate()));
    }

    @Test
    public void testFindByPrincipalNotNullAndAfterIsNull() {
        persistenceAuditEventRepository.save(testUserEvent);
        persistenceAuditEventRepository.save(testOtherUserEvent);

        List<AuditEvent> events = customAuditEventRepository.find("test-user", null);
        assertThat(events).hasSize(1);
        assertThat(events.get(0).getPrincipal()).isEqualTo("test-user");
    }

    @Test
    public void testFindByPrincipalIsNullAndAfterIsNull() {
        persistenceAuditEventRepository.save(testUserEvent);
        persistenceAuditEventRepository.save(testOtherUserEvent);

        List<AuditEvent> events = customAuditEventRepository.find(null, null);
        assertThat(events).hasSize(2);
        assertThat(events).extracting("principal")
            .containsExactlyInAnyOrder("test-user", "other-test-user");
    }

    @Test
    public void findByPrincipalAndType() {
        persistenceAuditEventRepository.save(testUserEvent);
        persistenceAuditEventRepository.save(testOldUserEvent);

        testOtherUserEvent.setAuditEventType(testUserEvent.getAuditEventType());
        persistenceAuditEventRepository.save(testOtherUserEvent);

        PersistentAuditEvent testUserOtherTypeEvent = new PersistentAuditEvent();
        testUserOtherTypeEvent.setUserId(testUserEvent.getUserId());
        testUserOtherTypeEvent.setAuditEventType("test-other-type");
        testUserOtherTypeEvent.setAuditEventDate(testUserEvent.getAuditEventDate());
        persistenceAuditEventRepository.save(testUserOtherTypeEvent);

        List<AuditEvent> events = customAuditEventRepository.find("test-user",
            Date.from(testUserEvent.getAuditEventDate().minusSeconds(3600)), "test-type");
        assertThat(events).hasSize(1);
        AuditEvent event = events.get(0);
        assertThat(event.getPrincipal()).isEqualTo(testUserEvent.getUserId());
        assertThat(event.getType()).isEqualTo(testUserEvent.getAuditEventType());
        assertThat(event.getData()).containsKey("test-key");
        assertThat(event.getData().get("test-key").toString()).isEqualTo("test-value");
        assertThat(event.getTimestamp()).isEqualTo(Date.from(testUserEvent.getAuditEventDate()));
    }

    @Test
    public void addAuditEvent() {
        Map<String, Object> data = new HashMap<>();
        data.put("test-key", "test-value");
        AuditEvent event = new AuditEvent("test-user", "test-type", data);
        customAuditEventRepository.add(event);
        List<PersistentAuditEvent> persistentAuditEvents = persistenceAuditEventRepository.findAll();
        assertThat(persistentAuditEvents).hasSize(1);
        PersistentAuditEvent persistentAuditEvent = persistentAuditEvents.get(0);
        assertThat(persistentAuditEvent.getUserId()).isEqualTo(event.getPrincipal());
        assertThat(persistentAuditEvent.getAuditEventType()).isEqualTo(event.getType());
        assertThat(persistentAuditEvent.getData()).containsKey("test-key");
        assertThat(persistentAuditEvent.getData().get("test-key")).isEqualTo("test-value");
        assertThat(persistentAuditEvent.getAuditEventDate()).isEqualTo(event.getTimestamp().toInstant());
    }

    @Test
    public void addAuditEventTruncateLargeData() {
        Map<String, Object> data = new HashMap<>();
        StringBuilder largeData = new StringBuilder();
        for (int i = 0; i < EVENT_DATA_COLUMN_MAX_LENGTH + 10; i++) {
            largeData.append("a");
        }
        data.put("test-key", largeData);
        AuditEvent event = new AuditEvent("test-user", "test-type", data);
        customAuditEventRepository.add(event);
        List<PersistentAuditEvent> persistentAuditEvents = persistenceAuditEventRepository.findAll();
        assertThat(persistentAuditEvents).hasSize(1);
        PersistentAuditEvent persistentAuditEvent = persistentAuditEvents.get(0);
        assertThat(persistentAuditEvent.getUserId()).isEqualTo(event.getPrincipal());
        assertThat(persistentAuditEvent.getAuditEventType()).isEqualTo(event.getType());
        assertThat(persistentAuditEvent.getData()).containsKey("test-key");
        String actualData = persistentAuditEvent.getData().get("test-key");
        assertThat(actualData.length()).isEqualTo(EVENT_DATA_COLUMN_MAX_LENGTH);
        assertThat(actualData).isSubstringOf(largeData);
        assertThat(persistentAuditEvent.getAuditEventDate()).isEqualTo(event.getTimestamp().toInstant());
    }

    @Test
    public void testAddEventWithNullData() {
        Map<String, Object> data = new HashMap<>();
        data.put("test-key", null);
        AuditEvent event = new AuditEvent("test-user", "test-type", data);
        customAuditEventRepository.add(event);
        List<PersistentAuditEvent> persistentAuditEvents = persistenceAuditEventRepository.findAll();
        assertThat(persistentAuditEvents).hasSize(1);
        PersistentAuditEvent persistentAuditEvent = persistentAuditEvents.get(0);
        assertThat(persistentAuditEvent.getData().get("test-key")).isEqualTo("null");
    }
}
