package com.pluralsight.springbatch.patientbatchloader.job;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;

import com.pluralsight.springbatch.patientbatchloader.domain.PatientRecord;

public class PatientRecordItemWriter implements ItemWriter<PatientRecord>{

	private static final Logger log = LoggerFactory.getLogger(PatientRecordItemWriter.class);
	
	public PatientRecordItemWriter() {
	}
	
	@Override
	public void write(List<? extends PatientRecord> items) throws Exception {
		for (PatientRecord patientRecord : items) {
			log.debug("Writing item: " + patientRecord.toString());
		}
	}

}
