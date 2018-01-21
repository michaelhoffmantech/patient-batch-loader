package com.pluralsight.springbatch.patientbatchloader.job;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import com.pluralsight.springbatch.patientbatchloader.domain.PatientEntity;

@Component
public class PatientRecordItemWriter implements ItemWriter<PatientEntity> {

	private static final Logger log = LoggerFactory.getLogger(PatientRecordItemWriter.class);
	
	public PatientRecordItemWriter() {
	}
	
	@Override
	public void write(List<? extends PatientEntity> items) throws Exception {
		for (PatientEntity patientEntity : items) {
			log.debug("Writing item: " + patientEntity.toString());
		}
	}

}
