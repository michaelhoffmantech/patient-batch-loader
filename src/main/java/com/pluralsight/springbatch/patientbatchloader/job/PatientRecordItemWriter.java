package com.pluralsight.springbatch.patientbatchloader.job;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.pluralsight.springbatch.patientbatchloader.domain.PatientEntity;

@Component
@Transactional
public class PatientRecordItemWriter extends JpaItemWriter<PatientEntity> {

	private static final Logger log = LoggerFactory.getLogger(PatientRecordItemWriter.class);
	
	public PatientRecordItemWriter() {
	}	

}
