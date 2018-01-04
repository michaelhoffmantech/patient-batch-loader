package com.pluralsight.springbatch.patientbatchloader.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

/**
 * Main configuration class for Spring Batch dependencies.
 * 
 * The @EnableBatchProcessing annotation gives you access to a variety of beans
 * related to batch processing. DefaultBatchConfigurer provides a default
 * strategy for the initialization of Spring Batch dependencies.
 */
@EnableBatchProcessing
public class BatchConfiguration extends DefaultBatchConfigurer {

	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	
	@Autowired
	private StepBuilderFactory stepBuilderFactory;
	
	@Bean
	public Step processPatientsStep() throws Exception {
		return this.stepBuilderFactory.get("process-patients-step")
			.chunk(20)
			.build(); 
	}
	
	@Bean
	public Job job(JobExecutionListener jobExecutionListener) throws Exception {
		return this.jobBuilderFactory.get("patient-batch-loader")
			.listener(jobExecutionListener)
			.start(processPatientsStep())
			.build(); 
	}
}
