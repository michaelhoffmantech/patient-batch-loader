package com.pluralsight.springbatch.patientbatchloader.config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.test.MetaDataInstanceFactory;
import org.springframework.batch.test.StepScopeTestExecutionListener;
import org.springframework.batch.test.StepScopeTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;

import com.pluralsight.springbatch.patientbatchloader.PatientBatchLoaderApp;
import com.pluralsight.springbatch.patientbatchloader.domain.PatientEntity;
import com.pluralsight.springbatch.patientbatchloader.domain.PatientRecord;
import com.pluralsight.springbatch.patientbatchloader.repository.PatientRepository;

@ContextConfiguration
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
	StepScopeTestExecutionListener.class,
	TransactionalTestExecutionListener.class })
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PatientBatchLoaderApp.class)
@ActiveProfiles("dev")
@Transactional
public class BatchJobConfigurationTest {

	@Autowired
	private Job job;

	@Autowired
	private FlatFileItemReader<PatientRecord> reader;

	@Autowired
	private Function<PatientRecord, PatientEntity> processor;

	@Autowired
	private JpaItemWriter<PatientEntity> writer;
	
	@Autowired
	private PatientRepository patientRepository;

	private JobParameters jobParameters;

	@Before
	public void setUp() {
		Map<String, JobParameter> params = new HashMap<>();
		params.put(Constants.JOB_PARAM_FILE_NAME, new JobParameter("test-unit-testing.csv"));
		jobParameters = new JobParameters(params);
	}

	@Test
	public void testJobConfiguration() {
		assertNotNull(job);
		assertEquals(Constants.JOB_NAME, job.getName());
	}

	@Test
	public void testReader() throws Exception {
		StepExecution stepExecution = MetaDataInstanceFactory.createStepExecution(jobParameters);
		int count = 0;
		try {
			count = StepScopeTestUtils.doInStepScope(stepExecution, () -> {
				int numPatients = 0;
				PatientRecord patient;
				try {
					reader.open(stepExecution.getExecutionContext());
					while ((patient = reader.read()) != null) {
						assertNotNull(patient);
						assertEquals("72739d22-3c12-539b-b3c2-13d9d4224d40", patient.getSourceId());
						assertEquals("Hettie", patient.getFirstName());
						assertEquals("P", patient.getMiddleInitial());
						assertEquals("Schmidt", patient.getLastName());
						assertEquals("rodo@uge.li", patient.getEmailAddress());
						assertEquals("(805) 384-3727", patient.getPhoneNumber());
						assertEquals("Hutij Terrace", patient.getStreet());
						assertEquals("Kahgepu", patient.getCity());
						assertEquals("ID", patient.getState());
						assertEquals("40239", patient.getZip());
						assertEquals("6/14/1961", patient.getBirthDate());
						assertEquals("I", patient.getAction());
						assertEquals("071-81-2500", patient.getSsn());
						numPatients++;
					}
				} finally {
					try { reader.close(); } catch (Exception e) { fail(e.toString()); }
				}
				return numPatients;
			});
		} catch (Exception e) {
			fail(e.toString());
		}
		assertEquals(1, count);
	}

	@Test
	public void testProcessor() throws Exception {
		PatientRecord patientRecord = new PatientRecord(
			"72739d22-3c12-539b-b3c2-13d9d4224d40",
			"Hettie",
			"P",
			"Schmidt",
			"rodo@uge.li",
			"(805) 384-3727",
			"Hutij Terrace",
			"Kahgepu",
			"ID",
			"40239",
			"6/14/1961",
			"I",
			"071-81-2500");
		PatientEntity entity = processor.apply(patientRecord);
		assertNotNull(entity);
		assertEquals("72739d22-3c12-539b-b3c2-13d9d4224d40", entity.getSourceId());
		assertEquals("Hettie", entity.getFirstName());
		assertEquals("P", entity.getMiddleInitial());
		assertEquals("Schmidt", entity.getLastName());
		assertEquals("rodo@uge.li", entity.getEmailAddress());
		assertEquals("(805) 384-3727", entity.getPhoneNumber());
		assertEquals("Hutij Terrace", entity.getStreet());
		assertEquals("Kahgepu", entity.getCity());
		assertEquals("ID", entity.getState());
		assertEquals("40239", entity.getZipCode());
		assertEquals(14, entity.getBirthDate().getDayOfMonth());
		assertEquals(6, entity.getBirthDate().getMonthValue());
		assertEquals(1961, entity.getBirthDate().getYear());
		assertEquals("071-81-2500", entity.getSocialSecurityNumber());
	}

	@Test
	public void testWriter() throws Exception {
		PatientEntity entity = new PatientEntity("72739d22-3c12-539b-b3c2-13d9d4224d40",
			"Hettie",
			"P",
			"Schmidt",
			"rodo@uge.li",
			"(805) 384-3727",
			"Hutij Terrace",
			"Kahgepu",
			"ID",
			"40239",
			LocalDate.of(1961, 6, 14),
			"071-81-2500");
		StepExecution execution = MetaDataInstanceFactory.createStepExecution();
        StepScopeTestUtils.doInStepScope(execution, () -> {
            writer.write(Arrays.asList(entity));
            return null;
        });
        assertTrue(patientRepository.findAll().size() > 0);
    }
}
