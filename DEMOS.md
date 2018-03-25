# Demos

Below are the full details of demos for the Pluralsight Spring Batch course. The goal here is to help you follow along with code demonstrations from the course by giving you the ability to copy and paste artifacts from here. I've numbered the demos to correspond with the course segments.

# Demo Contents

[Demo 1 - Project Download, IDE Setup and Execution](#demo-1-project-download-ide-setup-and-execution)

[Demo 2 - Gradle Dependencies](#demo-2-gradle-dependencies)

[Demo 3 - Spring Batch Configuration](#demo-3-spring-batch-configuration)

[Demo 4 - Spring Batch JobRepository Database Schema Configuration](#demo-4-spring-batch-jobrepository-database-schema-configuration)

[Demo 5 - Spring Batch Job Configuration](#demo-5-spring-batch-job-configuration)

[Demo 6 - Spring Batch Step Configuration](#demo-6-spring-batch-step-configuration)

[Demo 7 - Spring Batch Job Unit Test Creation](#demo-7-spring-batch-job-unit-test-creation)

[Demo 8 - Spring Batch Job Execution](#demo-8-spring-batch-job-execution)

[Demo 9 - Creating the Input File Domain Object](#demo-9-creating-the-input-file-domain-object)

[Demo 10 - Updating the Job Step for Chunking](#demo-10-updating-the-job-step-for-chunking)

[Demo 11 - Implementing a Flat File Item Reader](#demo-11-implementing-a-flat-file-item-reader)

[Demo 12 - Implementing the Stub Processor and Writer](#demo-12-implementing-the-stub-processor-and-writer)

[Demo 13 - Testing the Item Reader](#demo-13-testing-the-item-reader)

[Demo 14 - Executing the Job with the Item Reader](#demo-14-executing-the-job-with-the-item-reader)

[Demo 15 - Creating the Entity Type for Transformation](#demo-15-creating-the-entity-type-for-transformation)

[Demo 16 - Implementing the Item Processor](#demo-16-implementing-the-item-processor)

[Demo 17 - Testing the Item Processor](#demo-17-testing-the-item-processor)

[Demo 18 - Executing the Job with the Item Processor](#demo-18-executing-the-job-with-the-item-processor)

[Demo 19 - Adding the Patient Database Schema to the Liquibase Change Log](#demo-19-adding-the-patient-database-schema-to-the-liquibase-change-log)

[Demo 20 - Implementing the JPA Item Writer](#demo-20-implementing-the-jpa-item-writer)

[Demo 21 - Testing the Item Writer](#demo-21-testing-the-item-writer)

[Demo 22 - Executing the Job with the Item Writer](#demo-22-executing-the-job-with-the-item-writer)

# Demo 1 Project Download, IDE Setup and Execution

In this demonstration, I'll be showing you how to get the demo project from GitHub. I'll walk you through cloning the project, adding the project to the IDE and then running it from a command line.

## 1.1 - Project Download

Let's start with downloading the project.

1. Open a browser to:  
    ```cmd
    https://github.com/michaelhoffmantech/patient-batch-loader
    ```
    1. This is the project we will use for the demo.
    2. Again, you can also use the project available for download from the Pluralsight course
    3. I'm going to use a command window to actually retrieve the project.
2. Open a command prompt 
3. I'll be using the folder c:\demo for the course, so I'll navigate to that folder. 
4. I'm going to run the command below:
    ```cmd
    git clone https://github.com/michaelhoffmantech/patient-batch-loader.git
    ```
5. Now, change to the project folder directory:
    ```cmd
    cd patient-batch-loader
    ```
6. And finally, checkout the start tag for the project. This will put the project in a detached head state: 
    ```cmd
    git checkout tags/start
    ```

Once this is complete, we have the initial copy of the application and can load it into our IDE.

## 1.2 - IDE Setup

Next, let's add the project to the Intellij IDEA IDE.

1. I've opened the IntelliJ IDEA new project form. 
2. I'm going to select to open an existing project from source
3. Select the build.gradle file from the project's root directory
4. Make sure that JDK 8 is selected
5. Import the project. Once this is complete, we've successfully loaded the project template. Now, let's try to run the application to assure it works. 
6. Select the PatientBatchLoaderApp in the toolbar run configurations, then click the icon to run. 
7. This will start the application using Spring Boot's runner
8. If you see the message that the application is running and you don't get any exceptions, it means you have successfully set up the project.
9. Note here in the log that we have the Spring Boot and Spring versions.
10. We are using the dev spring profile. That's simple a spring profile used by default to configure for a development environment. 
11. We are using an Undertow embedded web server to run the application
12. Finally, note the port is 8080.
13. As a last step, let's verify runtime. 
14. Open the rest client in Intellij. You can also use a browser or REST client tool if you wish. 
15. Enter the request below: 
    ```
    host/port = http://localhost:8080
    path = /actuator/health
    ```
16. This is one of many URLs provided by the actuator dependency to provide you with metrics and observability around the application. I've included it as part of the template to support any issues you may run into along the way with the course.

This completes setup of the template application. Now, let's look at its contents.

# Demo 2 Gradle Dependencies

In this demo, I'll walk you through adding the Spring Batch libraries to the demo application. These properties will be adding the Gradle build script in the dependencies section.

I'm going to show you how to add the Spring Batch libraries to the project. 

1. Open up your IDE to the project.
2. Open the file build.gradle in the root folder of the project.
3. In the dependencies declaration, add the following two lines of code:
    ```
    // Starter to include the Spring Batch dependencies via Spring Boot
    compile "org.springframework.boot:spring-boot-starter-batch"
    // Spring Batch testing support
    testCompile "org.springframework.batch:spring-batch-test"
    ```
4. Once you save the build script, the IDE should ask you to import the changes.
5. With the changes imported, you can check the External Libraries and verify you have three dependencies from Spring Batch, including infrastructure, core and test. 

This completes dependency configuration.

# Demo 3 Spring Batch Configuration

In this demo, I'll be covering the steps needed for a configuring Spring Batch to be available in the Spring Container of your project. 

## 3.1 - Creating the BatchConfiguration Class

I'm going to walk you through the creation of the BatchConfiguration class. Again, this configuration class will provide support for the Spring Batch Job Repository, Job Launcher and Job Explorer features. 

1. Open up your IDE to the project.
2. Expand the source directory to the package com.pluralsight.springbatch.patientbatchloader.config. 
3. Right click the config folder and select to create a new Java class.
4. Name the class BatchConfiguration
5. First, let's add the Component and EnableBatchProcessing annotations.
    ```
    import org.springframework.stereotype.Component;
    import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;

    @Component
    @EnableBatchProcessing
    ``` 
6. With these two annotations in place, we have turned on Spring Batch features and will assure this class gets scanned for our configuration strategy. 
7. Next, I want to add reference to the interface this class will implement named BatchConfigurer. Let's change the class definition:
    ```
    import org.springframework.batch.core.configuration.annotation.BatchConfigurer;

    public class BatchConfiguration implements BatchConfigurer {
    ```
8. You will now get an error as we have not implemented the interface methods yet. We can ignore it for now.
9. Next, I need to add attributes to this class for the JobRepository, JobExplorer and JobLauncher. 
    ```
    import org.springframework.batch.core.repository.JobRepository;
    import org.springframework.batch.core.explore.JobExplorer;
    import org.springframework.batch.core.launch.JobLauncher;

    private JobRepository jobRepository;
    private JobExplorer jobExplorer;
    private JobLauncher jobLauncher;
    ```
10. Note that all of these attributes are part of the core Spring Batch dependency. 
11. I also need to autowire two beans for supporting these attributes, namely the data source and transaction manager. Let's add those:
    ```
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.beans.factory.annotation.Qualifier;
    import org.springframework.transaction.PlatformTransactionManager;
    import javax.sql.DataSource;
    
    @Autowired
    @Qualifier(value = "batchTransactionManager")
    private PlatformTransactionManager batchTransactionManager;

    @Autowired
    @Qualifier(value = "batchDataSource")
    private DataSource batchDataSource;
    ```
12. If you remember earlier, I overrode the default Spring Boot database configuration. Here we see one of the drivers for doing so. As I need to leverage the transaction manager and data source for the repository, explorer and jobs, I have control over the naming and configuration of these dependencies. here I've autowired the batch transaction manager and batch data source I defined in the database configuration class.
13. Now that we have the attributes in place, let's define the getters on the interface to resolve our compilation error:
    ```
    @Override
    public JobRepository getJobRepository() throws Exception {
        return this.jobRepository;
    }

    @Override
    public JobLauncher getJobLauncher() throws Exception {
        return this.jobLauncher;
    }

    @Override
    public JobExplorer getJobExplorer() throws Exception {
        return this.jobExplorer;
    }

    @Override
    public PlatformTransactionManager getTransactionManager() {
        return this.batchTransactionManager;
    }
    ```
14. That should resolve the compilation error. Now Spring Batch has access to our batch configuration beans. The last step is to define the strategy for how these beans are defined. First, let's define the job launcher:
    ```
    import org.springframework.batch.core.launch.support.SimpleJobLauncher;
    
    protected JobLauncher createJobLauncher() throws Exception {
        SimpleJobLauncher jobLauncher = new SimpleJobLauncher();
        jobLauncher.setJobRepository(jobRepository);
        jobLauncher.afterPropertiesSet();
        return jobLauncher;
    }    
    ```
15. I've configured the launcher to be of type SimpleJobLauncher. This launcher just executes a job task on-demand. By default, it uses synchronous task execution; however, this is where you can configure other types of task execution, such as asynchronous execution. Note that I've set the job repository on the launcher and called after properties set to ensure the dependencies of the job repository have been set. 
16. Next, let's create the JobRespository:
    ```
    import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
    
    protected JobRepository createJobRepository() throws Exception {
        JobRepositoryFactoryBean factory = new JobRepositoryFactoryBean();
        factory.setDataSource(this.batchDataSource);
        factory.setTransactionManager(getTransactionManager());
        factory.afterPropertiesSet();
        return factory.getObject();
    }
    ```
 17. I've configured the job repository to be created using a JobRepositoryFactoryBean. I set the batch data source and batch transaction manager to be used by the repository. Then, I call after properties set to ensure dependencies have been set. 
 18. The final step is to configure a method to be called after dependency injection has been performed by the Spring container. This method will handle the actual bean configuration:
     ```
    import javax.annotation.PostConstruct;
    import org.springframework.batch.core.explore.support.JobExplorerFactoryBean;

     @PostConstruct
     public void afterPropertiesSet() throws Exception {
         this.jobRepository = createJobRepository();
         JobExplorerFactoryBean jobExplorerFactoryBean = new JobExplorerFactoryBean();
         jobExplorerFactoryBean.setDataSource(this.batchDataSource);
         jobExplorerFactoryBean.afterPropertiesSet();
         this.jobExplorer = jobExplorerFactoryBean.getObject();
         this.jobLauncher = createJobLauncher();
     }
     ```
19. Now as part of initialization, we will set the job repository, job explorer and job launcher. I'll use the job explorer factory bean to set the data source on the job explorer instance. 

That completes the configuration for the Spring Batch job explorer, job repository and job launcher. As part of configuration, there are also a few properties that we need to change. Let's do that now.

## 3.2 - Updating the Application Properties

Spring and Spring Boot provide you with support for defining application properties to configure your project as well as your project dependencies. There are two properties that I want to add for support in the project moving forward.

1. In your IDE, open the file application.yml found in the folder src/main/resources/config. 
2. This properties file contains the base properties for configuring your project and project dependencies. 
3. By default, the annotation EnableBatchProcessing will create a runner by default and execute your jobs on startup. If this is the behavior you want, then you can skip this step. In our case, I want to start jobs on demand; therefore, I'm going to change the configuration spring.batch.job.enabled to false. 
    ```
    spring:
        batch:
            job:
                enabled: false
    ```
4. Next, I want to define the input path for files that will be processed by the batch job. These files will contain patient records that get transformed and saved in a database. I'm going to define the input path as an application specific property. At the bottom of the application.yml file, enter the following:
    ```
    application:
        batch:
            inputPath: c:/demo/patient-batch-loader/data
    ```
5. Note that I'm using the input path as the project's data folder. A few things to consider. As this is a demo, I've defined the property in the base file; however, there are also property files for the Spring dev profile and prod profile. It is recommended that you override this property within those property files individually.
6. In order for me to access these properties, I've created an ApplicationProperties class that maps to the property names and values. Let's look at that now. 
7. Open the class ApplicationProperties found in the package com.pluralsight.springbatch.patientbatchloader.config. 
8. This class is annotated with the ConfigurationProperties annotation. Note that the prefix matches the property name we defined in the properties file. I'll define a value for this now, including a static class for the mapping. 
    ```
	private final Batch batch = new Batch(); 
	
	public Batch getBatch() {
		return batch;
	}
	
	public static class Batch {
		private String inputPath = "c:/demo/patient-batch-loader/data";
		
		public String getInputPath() {
			return this.inputPath;
		}
		
		public void setInputPath(String inputPath) {
			this.inputPath = inputPath; 
		}
	}	
    ```
9. Now at run-time, I'll be able to access the input path for the batch job using this application properties class. Note that I also set the default of the input path in case the property was not defined. 

That completes the initial Spring Batch configuration. 

# Demo 4 Spring Batch JobRepository Database Schema Configuration

I'll be demonstrating the addition of the Spring Batch JobRepository database schema for the required tables. 

1. Open up your IDE to the project.
2. Expand the folder src/main/resources/config/liquibase.
3. This directory contains the schema definitions used by Liquibase to manage the database schema and data. Start by opening the file master.xml.
4. The master.xml file is the main change log definition for the database schema. Currently, I've included an initial, blank schema file. We are going to include a new file for the spring batch schema. Add the following like under the first include element:
    ```
    <include 
        file="config/liquibase/changelog/01012018000000_create_spring_batch_objects.xml"  
        relativeToChangelogFile="false"/>
    ```
5. As this is just a reference to the spring batch schema, I'll need to create the file next. Note that its important to add the creation of this table as the second entry as Liquibase schema changes are additive and sequential. Let's create the spring batch schema file.
6. Expand the changelog folder. Right click the changelog folder and new file with the following name:
    ```
    01012018000000_create_spring_batch_objects.xml
    ```
7. The prefix for the file name is simply a timestamp to ensure ordering. 
8. Next, let's drop in the contents for the schema:
    ```xml
    <?xml version="1.0" encoding="utf-8"?>
    <databaseChangeLog
        xmlns="http://www.liquibase.org/xml/ns/dbchangelog"
        xmlns:ext="http://www.liquibase.org/xml/ns/dbchangelog-ext"
        xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
        xsi:schemaLocation="http://www.liquibase.org/xml/ns/dbchangelog http://www.liquibase.org/xml/ns/dbchangelog/dbchangelog-3.5.xsd
                            http://www.liquibase.org/xml/ns/dbchangelog-ext http://www.liquibase.org/xml/ns/dbchangelog/dbchangelog-ext.xsd">
    
        <property name="now" value="now()" dbms="h2"/>
        <property name="now" value="GETDATE()" dbms="mssql"/>
    
        <changeSet id="01012018000001" author="system">
            <createTable tableName="BATCH_JOB_INSTANCE">
                <column name="JOB_INSTANCE_ID" type="bigint" autoIncrement="${autoIncrement}">
                    <constraints primaryKey="true" nullable="false"/>
                </column>
                <column name="VERSION" type="bigint"/>
                <column name="JOB_NAME" type="VARCHAR(100)">
                    <constraints nullable="false" />
                </column>
                <column name="JOB_KEY" type="VARCHAR(32)">
                    <constraints nullable="false" />
                </column>
            </createTable>
    
            <createIndex indexName="JOB_INST_UN"
                         tableName="BATCH_JOB_INSTANCE"
                         unique="true">
                <column name="JOB_NAME" type="varchar(100)"/>
                <column name="JOB_KEY" type="varchar(32)"/>
            </createIndex>
            
            <createTable tableName="BATCH_JOB_EXECUTION">
                <column name="JOB_EXECUTION_ID" type="bigint" autoIncrement="${autoIncrement}">
                    <constraints primaryKey="true" nullable="false"/>
                </column>
                <column name="VERSION" type="bigint"/>
                <column name="JOB_INSTANCE_ID" type="bigint">
                    <constraints nullable="false" />
                </column>
                <column name="CREATE_TIME" type="timestamp">
                    <constraints nullable="false" />
                </column>
                <column name="START_TIME" type="timestamp" defaultValue="null"/>
                <column name="END_TIME" type="timestamp" defaultValue="null"/>
                <column name="STATUS" type="VARCHAR(10)"/>
                <column name="EXIT_CODE" type="VARCHAR(2500)"/>
                <column name="EXIT_MESSAGE" type="VARCHAR(2500)"/>
                <column name="LAST_UPDATED" type="timestamp"/>
                <column name="JOB_CONFIGURATION_LOCATION" type="VARCHAR(2500)"/>
            </createTable>
    
            <addForeignKeyConstraint baseColumnNames="JOB_INSTANCE_ID"
                                     baseTableName="BATCH_JOB_EXECUTION"
                                     constraintName="JOB_INST_EXEC_FK"
                                     referencedColumnNames="JOB_INSTANCE_ID"
                                     referencedTableName="BATCH_JOB_INSTANCE"/>
            
            <createTable tableName="BATCH_JOB_EXECUTION_PARAMS">
                <column name="JOB_EXECUTION_ID" type="bigint" autoIncrement="${autoIncrement}">
                    <constraints primaryKey="true" nullable="false"/>
                </column>
                <column name="TYPE_CD" type="VARCHAR(6)">
                    <constraints nullable="false" />
                </column>
                <column name="KEY_NAME" type="VARCHAR(100)">
                    <constraints nullable="false" />
                </column>
                <column name="STRING_VAL" type="VARCHAR(250)"/>
                <column name="DATE_VAL" type="timestamp" defaultValue="null"/>
                <column name="LONG_VAL" type="bigint"/>
                <column name="DOUBLE_VAL" type="double precision"/>
                <column name="IDENTIFYING" type="CHAR(1)">
                    <constraints nullable="false" />
                </column>
            </createTable>
    
            <addForeignKeyConstraint baseColumnNames="JOB_EXECUTION_ID"
                                     baseTableName="BATCH_JOB_EXECUTION_PARAMS"
                                     constraintName="JOB_EXEC_PARAMS_FK"
                                     referencedColumnNames="JOB_EXECUTION_ID"
                                     referencedTableName="BATCH_JOB_EXECUTION"/>
    
            <createTable tableName="BATCH_STEP_EXECUTION">
                <column name="STEP_EXECUTION_ID" type="bigint" autoIncrement="${autoIncrement}">
                    <constraints primaryKey="true" nullable="false"/>
                </column>
                <column name="VERSION" type="bigint">
                    <constraints nullable="false" />
                </column>
                <column name="STEP_NAME" type="varchar(100)">
                    <constraints nullable="false" />
                </column>
                <column name="JOB_EXECUTION_ID" type="bigint">
                    <constraints nullable="false" />
                </column>
                <column name="START_TIME" type="timestamp">
                    <constraints nullable="false" />
                </column>
                <column name="END_TIME" type="timestamp" defaultValue="null"/>
                <column name="STATUS" type="varchar(10)"/>
                <column name="COMMIT_COUNT" type="bigint"/>
                <column name="READ_COUNT" type="bigint"/>
                <column name="FILTER_COUNT" type="bigint"/>
                <column name="WRITE_COUNT" type="bigint"/>
                <column name="READ_SKIP_COUNT" type="bigint"/>
                <column name="WRITE_SKIP_COUNT" type="bigint"/>
                <column name="PROCESS_SKIP_COUNT" type="bigint"/>
                <column name="ROLLBACK_COUNT" type="bigint"/>
                <column name="EXIT_CODE" type="varchar(2500)"/>
                <column name="EXIT_MESSAGE" type="varchar(2500)"/>
                <column name="LAST_UPDATED" type="timestamp"/>
        </createTable>		
    
        <addForeignKeyConstraint baseColumnNames="JOB_EXECUTION_ID"
                                 baseTableName="BATCH_STEP_EXECUTION"
                                 constraintName="JOB_EXEC_STEP_FK"
                                 referencedColumnNames="JOB_EXECUTION_ID"
                                 referencedTableName="BATCH_JOB_EXECUTION"/>
        
        <createTable tableName="BATCH_STEP_EXECUTION_CONTEXT">
                <column name="STEP_EXECUTION_ID" type="bigint" autoIncrement="${autoIncrement}">
                    <constraints primaryKey="true" nullable="false"/>
                </column>
                <column name="SHORT_CONTEXT" type="varchar(2500)">
                    <constraints nullable="false" />
                </column>
                <column name="SERIALIZED_CONTEXT" type="LONGVARCHAR"/>
        </createTable>
    
        <addForeignKeyConstraint baseColumnNames="STEP_EXECUTION_ID"
                                 baseTableName="BATCH_STEP_EXECUTION_CONTEXT"
                                 constraintName="STEP_EXEC_CTX_FK"
                                 referencedColumnNames="STEP_EXECUTION_ID"
                                 referencedTableName="BATCH_STEP_EXECUTION"/>
    
        <createTable tableName="BATCH_JOB_EXECUTION_CONTEXT">
                <column name="JOB_EXECUTION_ID" type="bigint" autoIncrement="${autoIncrement}">
                    <constraints primaryKey="true" nullable="false"/>
                </column>
                <column name="SHORT_CONTEXT" type="varchar(2500)">
                    <constraints nullable="false" />
                </column>
                <column name="SERIALIZED_CONTEXT" type="LONGVARCHAR"/>
        </createTable>
    
        <addForeignKeyConstraint baseColumnNames="JOB_EXECUTION_ID"
                                 baseTableName="BATCH_JOB_EXECUTION_CONTEXT"
                                 constraintName="JOB_EXEC_CTX_FK"
                                 referencedColumnNames="JOB_EXECUTION_ID"
                                 referencedTableName="BATCH_JOB_EXECUTION"/>
        
        <createSequence sequenceName="BATCH_STEP_EXECUTION_SEQ" />
        <createSequence sequenceName="BATCH_JOB_EXECUTION_SEQ" />
        <createSequence sequenceName="BATCH_JOB_SEQ" />
        
        </changeSet>
    </databaseChangeLog>
    ```
9. Note that this schema is actually derived from one found in the Spring Batch project. Spring Batch provides you the schema for most types of databases available. Unfortunately, Spring Batch does not provide it in Liquibase format, so I needed to create the file myself. While Spring Batch will auto-generate the database objects for you, I recommend this approach as puts you or your DBA in control of the database creation process. 
10. With the database in place, let's try to run the project and make sure the database is available. 
11. Run the PatientBatchLoaderApp configuration from the IDE. 
12. Once the server has started, verify that there were no errors on load. 
13. Open a browser. 
14. Navigate to the following URL:
    ```
    http://localhost:8080/console
    ```
15. Entering the URL should bring up the console for the H2 database. If it doesn't, you may want to check the error logs in case of a failure. Make sure to select/enter the following in the form:
    ```
    Saved Settings: Generic H2 (Server)
    Setting Name: Generic H2 (Server)
    Driver Class: org.h2.Driver
    JDBC URL: jdbc:h2:file:./h2db/db/patientbatchloader-dev;
    User Name: PatientBatchLoader
    Password: <empty>
    ```
16. First, click the test connection button to verify you get a successful connection. Then, click the connect button.
17. If Liquibase was successful, you should see the list of database tables created from the schema file in the console.

This verifies the configuration of the Spring Batch database schema. 

# Demo 5 Spring Batch Job Configuration

With Spring Batch configuration in place, the next step I'll demonstrate is the configuration of a Spring Batch job. The job will be responsible for loading patient data as input, processing it and then outputting it to a database. 

1. Open up your IDE to the project.
2. Expand the source folder to src/main/java/com/pluralsight/springbatch/patientbatchloader/config. 
3. In this folder, create a new class named BatchJobConfiguration. 
4. As this will be a Spring configuration class, add the @Configuration annotation to the class definition:
    ```
    import org.springframework.context.annotation.Configuration;
    
    @Configuration
    ```
5. To create the job bean, we are going to use a Spring Batch JobBuilderFactory. We simply need to autowire it into the configuration class. Add the JobBuilderFactory as the first member on the class:
    ```
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
    
    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    ```
6. The JobBuilderFactory will provide the DSL for configuring our job. Next, let's add the defintion for a JobRegistryBeanPostProcessor bean. Again, this will provide support for registering our job with the job repository:
    ```
    import org.springframework.context.annotation.Bean;
    import org.springframework.batch.core.configuration.JobRegistry;    
    import org.springframework.batch.core.configuration.support.JobRegistryBeanPostProcessor;
    
    @Bean
    JobRegistryBeanPostProcessor jobRegistryBeanPostProcessor(JobRegistry jobRegistry) {
        JobRegistryBeanPostProcessor postProcessor = new JobRegistryBeanPostProcessor();
        postProcessor.setJobRegistry(jobRegistry);
        return postProcessor;
    }
    ```
7. Now, we can start to define our job bean. Let's start by creating the method shell:
    ```
    import org.springframework.batch.core.Job;
    import org.springframework.batch.core.Step;
    
    @Bean
    public Job job(Step step) throws Exception {
    
    }
    ```
8. This bean will return the definition of our batch job. It accepts a step, which will be created in the next demo. 
9. I'm going to use the DSL for the JobBuilderFactory to define my job. Let's add the code now to the job bean method:
    ```
    return this.jobBuilderFactory
        .get(Constants.JOB_NAME)
        .validator(validator())
        .start(step)
        .build();
    ```
10. First, you will notice I'm using a constants field for the job name. Let's define that now. 
11. Open the Java class Constants in the same config package as BatchJobConfiguration. 
12. Add the following constant:
    ```
	public static final String JOB_NAME = "patient-batch-loader";
    ```
13. Again, this is the name we will use when referring to our job, such as when retreiving information from the job repository or execution. 
14. Open back up the BatchJobConfiguration class. 
15. Next, I've defined a call to the method validator. Again, the purpose of this validator is to validate the job parameters that are input to the job. I want to add a check to assure the file name is valid and the input file it refers to exists. 
16. Let's add the validator method now. Include the following method below the job method:
    ```
    import org.springframework.batch.core.JobParameters;
    import org.springframework.batch.core.JobParametersInvalidException;
    import org.springframework.batch.core.JobParametersValidator;
    import java.io.File;
    import java.nio.file.Files;
    import java.nio.file.Path;
    import java.nio.file.Paths;
    import org.apache.commons.lang3.StringUtils;
    
    @Bean
    public JobParametersValidator validator() {
        return new JobParametersValidator() {
            @Override
            public void validate(JobParameters parameters) throws JobParametersInvalidException {
                String fileName = parameters.getString(Constants.JOB_PARAM_FILE_NAME);
                if (StringUtils.isBlank(fileName)) {
                    throw new JobParametersInvalidException(
                		"The patient-batch-loader.fileName parameter is required.");
                }
                try {
                    Path file = Paths.get(applicationProperties.getBatch().getInputPath() + 
                		File.separator + fileName);
                    if (Files.notExists(file) || !Files.isReadable(file)) {
                        throw new Exception("File did not exist or was not readable");
                    }
                } catch (Exception e) {
                    throw new JobParametersInvalidException(
                        "The input path + patient-batch-loader.fileName parameter needs to " + 
                    		"be a valid file location.");
                }
            }
        };
    }
    ```
17. The method returns a JobParametersValidator instance. In the validate method, I look for the job parameter file name. As I am using a constant for that, let's define it now. 
18. Open the class Constants.java again. Add the following constant:
    ```
	public static final String JOB_PARAM_FILE_NAME = "patient-batch-loader.fileName"; 
    ```
19. Now, open back up the BatchJobConfiguration class. 
20. After obtaining the job parameter for the file name, I first check if its blank. If it is blank, an exception will be thrown when executing the job. Next, I validate the file referred to by the job parameter exists and is readable. Again, if its not, an exception is thrown. Note I'm using the application property that we defined earlier. I'll need to autowire it into the configuration class.
21. Add the following member to the configuration class:
    ```
    @Autowired
    private ApplicationProperties applicationProperties;
    ``` 
22. That covers the validation for our job. Now look back to the job method. After the validator method call, we have the call to the start method. This method accepts a step, which will be defined in the next demo. The start method creates an instance of SimpleJobBuilder for you, which will execute the step. Finally, the build method is called to complete creation of the job based on what was defined. 

That completes the demo for job creation. 

# Demo 6 Spring Batch Step Configuration

With the Spring Batch Job configured, I'll now demonstrate the creation of a job step. The step will define the reader, processor and writer for the job. In the next parts of this course, I'll be implementing the actual reader writer and processor, but for now, I'll just create a testable implementation. 

1. Open up your IDE to the project.
2. Open the class BatchJobConfiguration found in the package com.pluralsight.springbatch.patientbatchloader.config. This is the same class where we defined the job configuration.
3. Add the following member to the class:
    ```
    import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
    
    @Autowired
    private StepBuilderFactory stepBuilderFactory;        
    ```
4. The StepBuilderFactory member I defined will provide us with a DSL for configuring the step. Next, I need to define the actual Step configuration. Drop in the following method:
    ```
    import org.springframework.batch.core.Step;
    import org.springframework.batch.core.step.tasklet.Tasklet;
    import org.springframework.batch.core.StepContribution;
    import org.springframework.batch.repeat.RepeatStatus;
    import org.springframework.batch.core.scope.context.ChunkContext;
        
    @Bean
	public Step step() throws Exception {
		return this.stepBuilderFactory
			.get(Constants.STEP_NAME)
			.tasklet(new Tasklet() {
				@Override
				public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) 
				    throws Exception {
					System.err.println("Hello World!");
					return RepeatStatus.FINISHED; 
				}
			})
			.build();
	}
	```
5. First, you'll notice that I've used a constant to define the step name, similar to what I did with the job name. Let's open the Constants class now. Its in the same package as the BatchJobConfiguration class.
6. Add the following constant:
    ```
	public static final String STEP_NAME = "process-patients-step";
    ```
7. Now, open back up the BatchJobConfiguration class. 
8. Note here the step builder factory will get an instance of step builder using the step name. Next, I define the custom tasklet execution to print out hello world. Finally, the definition of the step is ended by calling the build method. 

That completes the demo for step configuration. 

# Demo 7 Spring Batch Job Unit Test Creation

With the BatchJobConfiguration class defined, we simply want to start unit testing by verifying that the job is created correctly by the configuration. 

1. Open up your IDE to the project.
2. Create a class named BatchJobConfigurationTest in the package com.pluralsight.springbatch.patientbatchloader.config under the src/test/java folder. 
3. Add annotations for the class to run tests with the SpringRunner class and for the class to run as a spring boot test using the main application class of the project.
    ```
    import org.junit.runner.RunWith;
    import org.springframework.boot.test.context.SpringBootTest;
    import org.springframework.test.context.junit4.SpringRunner;
    import com.pluralsight.springbatch.patientbatchloader.PatientBatchLoaderApp;
    
    @RunWith(SpringRunner.class)
    @SpringBootTest(classes = PatientBatchLoaderApp.class)
    ```
4. Next, autowire the configured job bean.
    ```
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.batch.core.Job;
    
    @Autowired
    private Job job; 
    ```
5. Finally, define the test method. I'm going to test that the job was successfully auto-wired and is not null. I'll also verify the job has the correct name.
    ```
    import static org.junit.Assert.*;
    import org.junit.Test;
    
    @Test
	public void test() {
		assertNotNull(job);
		assertEquals(Constants.JOB_NAME, job.getName());
	}
    ```
6. With the test defined, let's try to run it. From the IDE, run the new test method you created. 
7. The test should have run successfully. If you see any issues, double-check your configuration classes and assure that you made the necessary changes for property files. 

That concludes the demo for unit testing the job.

# Demo 8 Spring Batch Job Execution

Now that the job is configured and unit tested, I want to verify job execution. To do this, I need to create a new controller class that will allow me to kick off the job using the job launcher. 

1. Open up your IDE to the project.
2. Create a package named web.rest under the package com.pluralsight.springbatch.patientbatchloader. 
3. Create a new Java class named JobResource in the web.rest package.
4. Add the RestController and RequestMapping annotations to the class:
    ```
    import org.springframework.web.bind.annotation.RestController;
    import org.springframework.web.bind.annotation.RequestMapping;
    
    @RestController
    @RequestMapping("/job")
    ```
5. This tells Spring that the class is to be managed as a rest controller and paths in this class will begin with /job. 
6. Next, add the JobLauncher and Job instances as well as the constructor where the instances will be auto-wired:
    ```
    import org.springframework.batch.core.Job;
    import org.springframework.batch.core.launch.JobLauncher;

    private final JobLauncher jobLauncher;
    private final Job job;

    public JobResource(JobLauncher jobLauncher, Job job) {
        this.jobLauncher = jobLauncher;
        this.job = job;
    }
    ```
 7. Now, let's create the getter method that will use the job launcher to execute the job. 
 8. Add the following method under the controller:
    ```
    import com.pluralsight.springbatch.patientbatchloader.config.Constants;
    import org.springframework.batch.core.JobParameter;
    import org.springframework.batch.core.JobParameters;
    import org.springframework.http.HttpStatus;
    import org.springframework.http.ResponseEntity;
    import org.springframework.web.bind.annotation.GetMapping;
    import org.springframework.web.bind.annotation.PathVariable;
    
    @GetMapping("/{fileName:.+}")
    public ResponseEntity<String> runJob(@PathVariable String fileName) {
        Map<String, JobParameter> parameterMap = new HashMap<>();
        parameterMap.put(Constants.JOB_PARAM_FILE_NAME, new JobParameter(fileName));
        try {
            jobLauncher.run(job, new JobParameters(parameterMap));
        } catch (Exception e) {
            return new ResponseEntity<String>("Failure: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<String>("Success", HttpStatus.OK);
     }
    ``` 
9. Note that I've added a mapping for get requests using the file name as a path variable. In the next section on item readers, this file name will be leveraged; however, for now I've just included it for validation purposes. The file name will be mapped as a job parameter. The job launcher will then run the job using the parameter passed. I'm also including an exception check in case the job were to fail. 
10. With the rest controller in place, let's try to actually run the job.
11. Start the server from the IDE by running the PatientBatchLoaderApp configuration. 
12. Open the test rest client in the IDE. 
13. Enter the following:
    ```
    Host = http://localhost:8080
    Path = /job/test-unit-testing.csv
    ```
14. You should get a Success response in the browser. If not, its recommended that you verify the correct configuration of the job and properties.
15. Let's look at the logs to verify the job executed successfully.
16. If the job executed successfully, you should see three lines of output. The first line should note the job ran with the file name argument passed. The second line should read the Hello World output. The final line should show the success response. 

That verifies the job is correctly configured. Now, I can create the reader, processor and writer for our patient files. 

# Demo 9 Creating the Input File Domain Object

I'll be demonstrating the creation of a domain object that contains attributes directly tied to the fields in our input file record. Let's start by taking a look at the input file. 

1. Open up your IDE to the project. 
2. Open the file test.csv in the folder data. 
3. Note that earlier in the course, I defined an application property to point to the data folder as the input directory. Again, you will likely configure this to be a folder outside of the project and is only meant for demo purposes. The file contains a header row with the field labels for each record. We are going to simply skip this line when reading the file. Next, note that each line represents a single record and that each field in a record is separated by a comma. Now, let's create the class where data records will be mapped to. 
4. Create a new package:
    ```
    com.pluralsight.springbatch.patientbatchloader.domain
    ```
5. Create a class in the package with the name PatientRecord. 
6. As we want this object to be serializable, add the interface to the class declaration:
    ```
    import java.io.Serializable;
    
    public class PatientRecord implements Serializable {
    ```
7. Now, add the attributes to the class:
    ```
	private String sourceId;
	private String firstName;
	private String middleInitial;
	private String lastName;
	private String emailAddress;
	private String phoneNumber;
	private String street;
	private String city;
	private String state;
	private String zip;
	private String birthDate;
	private String action;
	private String ssn;
    ```
8. Right-click on the attributes and select to create the getters and setters. Then, right-click between the attributes and methods to create a constructor with no fields and one with all fields. Finally, right click near the bottom of the class and select to create the toString method for the class. 

That completes the creation of the PatientRecord domain object.  

# Demo 10 Updating the Job Step for Chunking

I'm going to update the existing job step to support chunking. 

1. Open your IDE to the project.
2. Open the class BatchJobConfiguration in the package com.pluralsight.springbatch.patientbatchloader.config
3. Replace the step method with the following code:
    ```
    import com.pluralsight.springbatch.patientbatchloader.domain.PatientRecord;
    import org.springframework.batch.item.ItemReader;
    
    @Bean
    public Step step(ItemReader<PatientRecord> itemReader) throws Exception {
        return this.stepBuilderFactory
            .get(Constants.STEP_NAME)
            .<PatientRecord, PatientRecord>chunk(2)
            .reader(itemReader)
            .processor(processor())
            .writer(writer())
            .build();
    }
    ``` 
4. I've defined an ItemReader bean to be passed as an argument to the method. I've also added a call to chunk with a size of two in the build instructions. Finally, I've added a call for the reader, processor and writer in the chunk processing. You will get a compile error for now until I've implemented a temporary processor and writer later in this section. 

This completes the update for the job step to use chunk-oriented processing. 

# Demo 11 Implementing a Flat File Item Reader

I'm going to implement a flat file item reader. This will take each record from the input file and create a PatientRecord instance from it's data. 

1. Open your IDE to the project.
2. Open the class BatchJobConfiguration in the package com.pluralsight.springbatch.patientbatchloader.config
3. Add the following code under the validation method:
    ```
    import org.springframework.batch.core.configuration.annotation.StepScope;
    import org.springframework.batch.item.file.FlatFileItemReader;
    import org.springframework.beans.factory.annotation.Value;
    import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
    import org.springframework.core.io.PathResource;
    import java.io.File;
    import java.nio.file.Paths;
    
    @Bean
    @StepScope
    public FlatFileItemReader<PatientRecord> reader(
        @Value("#{jobParameters['" + Constants.JOB_PARAM_FILE_NAME + "']}")String fileName) {
        return new FlatFileItemReaderBuilder<PatientRecord>()
            .name(Constants.ITEM_READER_NAME)
            .resource(
                new PathResource(
                    Paths.get(applicationProperties.getBatch().getInputPath() +
                        File.separator + fileName)))
            .linesToSkip(1)
            .lineMapper(lineMapper())
            .build();
    }
    ```
4. You will notice that there are two compilation errors. Let's fix the first by updating the constants class file. 
5. Open the Constants class in the same folder as the configuration class.
6. Add the following constant:
    ```
    public static final String ITEM_READER_NAME = "patient-item-reader";
    ```
7. Similar to the step and the job, this just provides Spring Batch with a name to identify the reader. 
8. Open back up the BatchJobConfiguration class. First, note I've included a step scope annotation. This allows me to inject values from the step context. Next, note that the file name parameter is the example where I use a value from the step context, specifically the job parameter file name. I use a Spring Batch convenience class for flat file item reading. The builder takes a name, the resource as the input file, a number of lines to skip, which in our case is one for the header, and a line mapper that I'll implement next. 
9. Add the following method below the flat file item reader method:
    ```
    import org.springframework.batch.item.file.mapping.DefaultLineMapper;
    import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
    import org.springframework.batch.item.file.LineMapper;

    @Bean
    public LineMapper<PatientRecord> lineMapper() {
        DefaultLineMapper<PatientRecord> mapper = new DefaultLineMapper<>();
        mapper.setFieldSetMapper((fieldSet) -> new PatientRecord(
            fieldSet.readString(0), fieldSet.readString(1),
            fieldSet.readString(2), fieldSet.readString(3),
            fieldSet.readString(4), fieldSet.readString(5),
            fieldSet.readString(6), fieldSet.readString(7),
            fieldSet.readString(8), fieldSet.readString(9),
            fieldSet.readString(10), fieldSet.readString(11),
            fieldSet.readString(12)));
        mapper.setLineTokenizer(new DelimitedLineTokenizer());
        return mapper;
    }
    ```
10. This is the implementation of the line mapper to be used by the flat file item reader. It just simply maps each field to a patient record attribute using the PatientRecord constructor I created earlier. Finally, I include a delimited line tokenizer that accepts a comma as the delimeter by default. 

This completes the implementation of the flat file item reader.

# Demo 12 Implementing the Stub Processor and Writer

In this demo, I'll show you how to implement a stubbed out processor and writer to allow for verification of the full job using the new flat file item reader. 

1. Open the IDE to the project.
2. Open the BatchJobConfiguration class in the package com.pluralsight.springbatch.patientbatchloader.config. 
3. Add the following code to the bottom of the class:
    ```
    import org.springframework.batch.item.support.PassThroughItemProcessor;
    import org.springframework.batch.item.ItemWriter;

    @Bean
    @StepScope
    public PassThroughItemProcessor<PatientRecord> processor() {
        return new PassThroughItemProcessor<>();
    }

    @Bean
    @StepScope
    public ItemWriter<PatientRecord> writer() {
        return new ItemWriter<PatientRecord>() {
            @Override
            public void write(List<? extends PatientRecord> items) throws Exception {
                for (PatientRecord patientRecord : items) {
                    System.err.println("Writing item: " + patientRecord.toString());
                }
            }
        };
    }
    ```
4. In the first bean definition, I've created a method that returns a pass through item processor. This does nothing and passes the argument on to the next part of the chunk processing. In the second bean definition, I have a method that will take the PatientRecord and write it out to the system error log. 

This completes the creation of the stub for processing and writing items. 

# Demo 13 Testing the Item Reader

For this demo, I'll be adding the flat file item reader implementation to the batch configuration test case to verify its configuration. 

1. Open the IDE to the project.
2. Open the BatchJobConfigurationTest class file in the test folder package com.pluralsight.springbatch.patientbatchloader.config.
3. Add the following auto-wired member on the class:
    ```
    import com.pluralsight.springbatch.patientbatchloader.domain.PatientRecord;
    import org.springframework.batch.item.file.FlatFileItemReader;

	@Autowired
	private FlatFileItemReader<PatientRecord> reader;
    ```
4. This will inject the flat file item reader implementation from the configuration class. 
5. To support the reader, we need to create job parameters for testing. Drop in the following class member and setup method:
    ```
    import org.springframework.batch.core.JobParameter;
    import org.springframework.batch.core.JobParameters;
    import java.util.HashMap;
    import java.util.Map;    

    private JobParameters jobParameters;

    @Before
    public void setUp() {
        Map<String, JobParameter> params = new HashMap<>();
        params.put(Constants.JOB_PARAM_FILE_NAME, new JobParameter("test-unit-testing.csv"));
        jobParameters = new JobParameters(params);
    }
    ```
6. Now, before each test the job parameter map will be initialized with a test input file. The input file only contains a limited number of records for the purpose of unit testing. 
7. Next, drop in the test case for the flat file item reader:
    ```
    import org.springframework.batch.test.MetaDataInstanceFactory;
    import org.springframework.batch.core.StepExecution;
    import org.springframework.batch.test.StepScopeTestUtils;
    
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
    ```
8. This test method uses the Spring Batch StepScopeTestUtils class to test the execution of the reader. Just like in a full execution, the parameter for the file name will be passed in to the step and the reader will read from the file. I've added several assertions for the content of the file and count of records read. 
9. Next, try to run the test. If there were any test failures, check the reader configuration is correct. 

That completes the implementation of the test case for the new item reader. 

# Demo 14 Executing the Job with the Item Reader

With the full item reader in place and the processor and writer stubbed, let's try to execute the job. 

1. Open your IDE to the project.
2. Run the PatientBatchLoaderApp configuration from the IDE. Assure that the log notes the server started up correctly. 
3. In a rest client, enter the following request:
    ```
    Host = http://localhost:8080
    Path = /job/test-unit-testing.csv
    ```
4. If the job was successful, you should see a success messsage in the browser. 
5. Next, check the runtime log. Verify that you see a line noting the start of the job, a line that outputs the patient record data and a line that denotes success for the job. 

That completes the execution of the job using the item reader and stubbed out processor and writer. 

# Demo 15 Creating the Entity Type for Transformation

For this demo, I'll show you how to create the PatientEntity JPA entity class that will be used by the item processor transformation. 

1. Open the IDE to the project.
2. Create a class named PatientEntity in the package package com.pluralsight.springbatch.patientbatchloader.domain.
3. Modify the class declaration to implement Serializable and to include JPA annotations:
    ```
    import javax.persistence.Entity;
    import javax.persistence.Table;
    import java.io.Serializable;
    
    @Entity
    @Table(name = "patient")
    public class PatientEntity implements Serializable {
    ```
4. These annotations mark this class as representing a JPA entity. Next, let's add the entity members:
    ```
    import javax.persistence.Column;
    import javax.persistence.Id;
    import javax.persistence.GeneratedValue;
    import javax.persistence.GenerationType;
    import javax.persistence.SequenceGenerator;
    import javax.validation.constraints.NotNull;
    import java.time.LocalDate;


	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
	@SequenceGenerator(name = "sequenceGenerator")
	@Column(name = "patient_id")
	private Long id;

	@NotNull
	@Column(name = "source_id", nullable = false)
	private String sourceId;

	@NotNull
	@Column(name = "first_name", nullable = false)
	private String firstName;

	@Column(name = "middle_initial", nullable = true)
	private String middleInitial;

	@NotNull
	@Column(name = "last_name", nullable = false)
	private String lastName;

	@Column(name = "email_address", nullable = true)
	private String emailAddress;

	@NotNull
	@Column(name = "phone_number", nullable = false)
	private String phoneNumber;

	@NotNull
	@Column(name = "street", nullable = false)
	private String street;

	@NotNull
	@Column(name = "city", nullable = false)
	private String city;

	@NotNull
	@Column(name = "state", nullable = false)
	private String state;

	@NotNull
	@Column(name = "zip_code", nullable = false)
	private String zipCode;

	@NotNull
	@Column(name = "birth_date", nullable = false)
	private LocalDate birthDate;

	@NotNull
	@Column(name = "social_security_number", nullable = false)
	private String socialSecurityNumber;
    ```
5. Each of these attributes map directly to a record from the input file, with the exception of the ID that contains the generated sequence value. Note that unlike the input for birth date, I'll be outputting to the database with the type LocalDate. I'll be adding that logic in the item processor. 
6. Next, let's add the constructors for the entity class:
    ```
	public PatientEntity() {

	}

	public PatientEntity(@NotNull String sourceId, @NotNull String firstName, String middleInitial,
			@NotNull String lastName, String emailAddress, @NotNull String phoneNumber, @NotNull String street,
			@NotNull String city, @NotNull String state, @NotNull String zipCode, @NotNull LocalDate birthDate,
			@NotNull String socialSecurityNumber) {
		super();
		this.sourceId = sourceId;
		this.firstName = firstName;
		this.middleInitial = middleInitial;
		this.lastName = lastName;
		this.emailAddress = emailAddress;
		this.phoneNumber = phoneNumber;
		this.street = street;
		this.city = city;
		this.state = state;
		this.zipCode = zipCode;
		this.birthDate = birthDate;
		this.socialSecurityNumber = socialSecurityNumber;
	}

	public PatientEntity(Long id, @NotNull String sourceId, @NotNull String firstName, String middleInitial,
			@NotNull String lastName, String emailAddress, @NotNull String phoneNumber, @NotNull String street,
			@NotNull String city, @NotNull String state, @NotNull String zipCode, @NotNull LocalDate birthDate,
			@NotNull String socialSecurityNumber) {
		super();
		this.id = id;
		this.sourceId = sourceId;
		this.firstName = firstName;
		this.middleInitial = middleInitial;
		this.lastName = lastName;
		this.emailAddress = emailAddress;
		this.phoneNumber = phoneNumber;
		this.street = street;
		this.city = city;
		this.state = state;
		this.zipCode = zipCode;
		this.birthDate = birthDate;
		this.socialSecurityNumber = socialSecurityNumber;
	}
    ```
7. The second constructor contains all fields with the exception of the generated ID. I'll use this constructor later for item processing convenience. 
8. Next, right click on the class in the IDE and generate the getters and setters for the members. Finally, right click on the class again in the IDE and generate the toString method for the class. 

This completes the demo for creating the PatientEntity type. 

# Demo 16 Implementing the Item Processor

I'm going to implement the full item processor for our demo. To do this, I first need to remove the stub implementation and update the job step configuration.

1. Open your IDE to the project. 
2. Open the BatchJobConfiguration class in the package com.pluralsight.springbatch.patientbatchloader.config. 
3. Remove the existing processor method from the class:
    ````
    public PassThroughItemProcessor<PatientRecord> processor()
    ```
4. Next, add the updated item processor code to the class:
    ```
    import com.pluralsight.springbatch.patientbatchloader.domain.PatientEntity;
    import java.time.LocalDate;
    import java.time.format.DateTimeFormatter;
    import java.util.function.Function;

    @Bean
    @StepScope
    public Function<PatientRecord, PatientEntity> processor() {
        return (patientRecord) ->  {
            return new PatientEntity(
                patientRecord.getSourceId(),
                patientRecord.getFirstName(),
                patientRecord.getMiddleInitial(),
                patientRecord.getLastName(),
                patientRecord.getEmailAddress(),
                patientRecord.getPhoneNumber(),
                patientRecord.getStreet(),
                patientRecord.getCity(),
                patientRecord.getState(),
                patientRecord.getZip(),
                LocalDate.parse(patientRecord.getBirthDate(), DateTimeFormatter.ofPattern("M/dd/yyyy")),
                patientRecord.getSsn());
        };
    }
    ```
5. The item processor simply returns a Java function. I'm mapping each field of the PatientRecord to a PatientEntity attribute. For the birth date, I've converted it into the format expected by the database. 
6. Now, I need to wire the new processor into the job step. Replace the existing job step bean with the following code:
    ```
    @Bean
    public Step step(ItemReader<PatientRecord> itemReader,
        Function<PatientRecord, PatientEntity> processor) throws Exception {
        return this.stepBuilderFactory
            .get(Constants.STEP_NAME)
            .<PatientRecord, PatientEntity>chunk(2)
            .reader(itemReader)
            .processor(processor)
            .writer(writer())
            .build();
    }
    ```
7. I've made a few updates in the step. First, I'm now passing in the processor as an argument to the method. I've also updated the chunk call to use the PatientEntity type in place of PatientRecord. Finally, I've updated the processor call to use the processor passed in to the method. 
8. Since I've changed the type from PatientRecord to PatientEntity, I now have to update the writer. Replace the existing writer method in the configuration class with the code:
    ```
    @Bean
    @StepScope
    public ItemWriter<PatientEntity> writer() {
        return new ItemWriter<PatientEntity>() {
            @Override
            public void write(List<? extends PatientEntity> items) throws Exception {
                for (PatientEntity patientEntity : items) {
                    System.err.println("Writing item: " + patientEntity.toString());
                }
            }
        };
    }
    ```
9. I've simply switched all references to PatientRecord in the writer with PatientEntity. When I execute the job, it will now output the patient entity data on write. 

That completes the demo for creating the item processor. 

# Demo 17 Testing the Item Processor 

With the ItemProcessor implementation in place, I want to now unit test the code. Let's create the unit test.

1. Open the IDE to the project.
2. Open the class BatchJobConfigurationTest found in the test package 
3. Add the following attribute to the class for the item processor:
    ```
    import com.pluralsight.springbatch.patientbatchloader.domain.PatientEntity;
    import java.util.function.Function;


	@Autowired
	private Function<PatientRecord, PatientEntity> processor;
    ```
4. This will auto-wire the processor bean I created into the test class. 
5. Next, let's add the item processor test case:
    ```
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
    ```
6. The item processor test simply calls the Java function apply method to transform the test PatientRecord into a PatientEntity. 
7. While the implementation is simply a Java function, I annotated the bean with the StepScope annotation. Given this, the test will fail as it wasn't executed within a step. To overcome this, I'm going to add an annotation to the class:
    ```
    import org.springframework.test.context.TestExecutionListeners;
    import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
    import org.springframework.batch.test.StepScopeTestExecutionListener;
    
    @TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
        StepScopeTestExecutionListener.class})
    ```
8. The test execution listeners I added will allow my test to succeed. The DependencyInjectionTestExecutionListener and StepScopeTestExecutionListener result in a step-scope context being created for my unit test. 
9. Execute the test and verify that it succeeds. If it fails, make sure to double-check your configuration changes. 

This completes the demo for unit testing the item processor.

# Demo 18 Executing the Job with the Item Processor

With the full item reader and processor in place and the writer stubbed, let's try to execute the job. 

1. Open your IDE to the project.
2. Run the PatientBatchLoaderApp configuration from the IDE. Assure that the log notes the server started up correctly. 
3. In a rest client, enter the following request:
    ```
    Host = http://localhost:8080
    Path = /job/test-unit-testing.csv
    ```
4. If the job was successful, you should see a success messsage in the browser. 
5. Next, check the runtime log. Verify that you see a line noting the start of the job, a line that outputs the patient entity data and a line that denotes success for the job. 

That completes the execution of the job using the item reader, processor and stubbed out writer. 

# Demo 19 Adding the Patient Database Schema to the Liquibase Change Log

To support writing items to the database, I'm going to demonstrate adding the patient database schema to be managed as a Liquibase change log. 

1. Open your IDE to the project. 
2. In the folder resources/config/liquibase/changelog, create a new file with the name: 
    ```
    01022018000000_create_patient_objects.xml
    ```
3. The prefix of the file name is a timestamp. Let's open the file now. Paste in the contents below:
    ```xml
    <?xml version="1.0" encoding="utf-8"?>
    <databaseChangeLog
        xmlns="http://www.liquibase.org/xml/ns/dbchangelog"
        xmlns:ext="http://www.liquibase.org/xml/ns/dbchangelog-ext"
        xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
        xsi:schemaLocation="http://www.liquibase.org/xml/ns/dbchangelog http://www.liquibase.org/xml/ns/dbchangelog/dbchangelog-3.5.xsd
                            http://www.liquibase.org/xml/ns/dbchangelog-ext http://www.liquibase.org/xml/ns/dbchangelog/dbchangelog-ext.xsd">
    
        <property name="now" value="now()" dbms="h2"/>
        <property name="now" value="GETDATE()" dbms="mssql"/>
    
        <changeSet id="01022018000001" author="system">
            <createTable tableName="patient">
                <column name="patient_id" type="bigint" autoIncrement="${autoIncrement}">
                    <constraints primaryKey="true" nullable="false"/>
                </column>
                <column name="source_id" type="VARCHAR(100)">
                    <constraints nullable="false" />
                </column>
                <column name="first_name" type="VARCHAR(100)">
                    <constraints nullable="false" />
                </column>
                <column name="middle_initial" type="VARCHAR(1)">
                    <constraints nullable="false" />
                </column>
                <column name="last_name" type="VARCHAR(100)">
                    <constraints nullable="false" />
                </column>
                <column name="email_address" type="VARCHAR(200)">
                    <constraints nullable="false" />
                </column>
                <column name="phone_number" type="VARCHAR(50)">
                    <constraints nullable="false" />
                </column>
                <column name="street" type="VARCHAR(255)">
                    <constraints nullable="false" />
                </column>
                <column name="city" type="VARCHAR(255)">
                    <constraints nullable="false" />
                </column>
                <column name="state" type="VARCHAR(100)">
                    <constraints nullable="false" />
                </column>
                <column name="zip_code" type="VARCHAR(20)">
                    <constraints nullable="false" />
                </column>
                <column name="birth_date" type="date">
                    <constraints nullable="false" />
                </column>
                <column name="social_security_number" type="VARCHAR(20)">
                    <constraints nullable="false" />
                </column>
            </createTable>	
        </changeSet>
    </databaseChangeLog>
    ```
4. This changelog defines the table and columns for patient data. Note that these columns directly map to the PatientEntity JPA entity we created in the previous section of this course. 
5. Next, I need to add this file path to the master change log. In the resources/config/liquibase folder, open the master.xml file. Add the following entry as the last include:
    ```
    <include file="config/liquibase/changelog/01022018000000_create_patient_objects.xml" relativeToChangelogFile="false"/>
    ```
6. Finally, let's run the application to assure liquibase starts correctly. Select the PatientBatchLoaderApp run configuration and click the run icon in the IDE. 
7. If there are no errors while configuring Liquibase in the logs and the server started successfully, you should now have the patient table loaded in the database. 

That completes the demo for configuring the database schema for Patient data. 

# Demo 20 Implementing the JPA Item Writer

I'll be making the final modifications to the batch job configuration to support writing items to the database. Much like the 

1. Open the IDE to the project.
2. Open the BatchJobConfiguration class in the package com.pluralsight.springbatch.patientbatchloader.config.
3. Remove the existing Item Writer method:
    ```
    public ItemWriter<PatientEntity> writer() {
    ```
4. Add the new JpaItemWriter implementation code:
    ```
    import org.springframework.batch.item.database.JpaItemWriter;
    
    @Bean
    @StepScope
    public JpaItemWriter<PatientEntity> writer() {
        JpaItemWriter<PatientEntity> writer = new JpaItemWriter<>();
        writer.setEntityManagerFactory(batchEntityManagerFactory);
        return writer;
    }
    ```
5. This code just creates an instance of JpaItemWriter for a PatientEntity and sets the entity manager factory configured in the project. As I've yet to auto-wire the entity manager factory, let's do so now.
6. Add the following code to the list of members in the class:
    ```
    import javax.persistence.EntityManagerFactory;
    import org.springframework.beans.factory.annotation.Qualifier;

    @Autowired
    @Qualifier(value="batchEntityManagerFactory")
    private EntityManagerFactory batchEntityManagerFactory;
    ```
7. This autowires the entity manager factory with a qualifier to assure I get the correct bean instance that I configured. 
8. The last change I need to make is to update the job step. Remove the existing step method below from the BatchJobConfiguration class:
    ```
    public Step step()
    ```
9. Then, add the code below as the new step method:
    ```
    @Bean
    public Step step(ItemReader<PatientRecord> itemReader,
        Function<PatientRecord, PatientEntity> processor,
        JpaItemWriter<PatientEntity> writer) throws Exception {
        return this.stepBuilderFactory
            .get(Constants.STEP_NAME)
            .<PatientRecord, PatientEntity>chunk(2)
            .reader(itemReader)
            .processor(processor)
            .writer(writer)
            .build();
    }
    ```
10. I've just added the JpaItemWriter as an argument to the method and changed the writer call to use the writer passed in. 

This completes the coding for the item processor. 

# Demo 21 Testing the Item Writer 

With the ItemWriter implementation in place, I want to now unit test the code. To verify the data saved, I'm also going to implement a repository class for accessing patient data. 

1. Open the IDE to the project.
2. Add a new package to the main source named 
    ```
    com.pluralsight.springbatch.patientbatchloader.repository
    ``` 
3. Next, create a new interface named PatientRepository in the repository package. 
4. Then, update the interface declaration:
    ```
    import org.springframework.data.jpa.repository.JpaRepository;
    import org.springframework.stereotype.Repository;
    import com.pluralsight.springbatch.patientbatchloader.domain.PatientEntity;
    
    @Repository
    public interface PatientRepository extends JpaRepository<PatientEntity, Long> {    
    ```
5. The repository annotation signifies to Spring that this is a repository component to be managed as a bean. The interface extends the Jpa repository class to support data access. I'll only be using the out-of-the-box queries for testing, so no further changes are needed.
6. Now, open the class BatchJobConfigurationTest found in the test package com.pluralsight.springbatch.patientbatchloader.config
7. Add the following attributes to the class for the item writer:
    ```
    import org.springframework.batch.item.database.JpaItemWriter;
    import com.pluralsight.springbatch.patientbatchloader.repository.PatientRepository;
    
    @Autowired
    private JpaItemWriter<PatientEntity> writer;
    
    @Autowired
    private PatientRepository patientRepository;
    ```
8. This will auto-wire the writer and repository beans I created into the test class. 
9. Next, let's add the item processor test case:
    ```
    import java.time.LocalDate;
    import java.util.Arrays;

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
    ```
10. The test case will first create a PatientEntity using the test data passed for construction. Then, within the scope of a step, the writer will be executed against the data. This should persist the patient record to the database. Finally, I'll assert that through the new patient repository class, I can retrieve the patient record back from the database. 
11. If I run the test case now, it will fail. The reason it fails is because I am trying to use the JPA item writer without a transaction. Let's fix this. Add the following annotation to the class:
    ```
    import org.springframework.transaction.annotation.Transactional;
    
    @Transactional
    ```
12. Along with this annotation, I also need to update the list of test execution listeners. Update the TestExecutionListeners annotation to the following:
    ``` 
    import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
    @TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
        StepScopeTestExecutionListener.class,
        TransactionalTestExecutionListener.class })
    ```
13. I now have added a listener for transactional tests and can execute the test succesfully. Run the test now for the item writer. If everything was configured propertly, the test should succeed. 

This completes the demo for unit testing the item writer.

# Demo 22 Executing the Job with the Item Writer

With the item reader, processor and writer in place, let's try to execute the full batch job. 

1. Open your IDE to the project.
2. Run the PatientBatchLoaderApp configuration from the IDE. Assure that the log notes the server started up correctly. 
3. In a rest client, enter the following request:
    ```
    Host = http://localhost:8080
    Path = /job/test.csv
    ```
4. Note that I'm using the full test file for this run. If the job was successful, you should see a success messsage in the browser. 
5. Next, check the runtime log. Verify that you see a line noting the start of the job and a line that denotes success for the job.
6. To complete verification of the job, let's open up a browser. 
7. Navigate to the following URL:
    ```
    http://localhost:8080/console
    ```
8. Entering the URL should bring up the console for the H2 database. Make sure to select/enter the following in the form:
    ```
    Saved Settings: Generic H2 (Server)
    Setting Name: Generic H2 (Server)
    Driver Class: org.h2.Driver
    JDBC URL: jdbc:h2:file:./h2db/db/patientbatchloader-dev;
    User Name: PatientBatchLoader
    Password: <empty>
    ```
9. First, click the test connection button to verify you get a successful connection. Then, click the connect button.
10. In the H2 console, select the Patient table. This should add a query to the sql console to select all patient records. Run this statement now. 
11. If I scroll to the bottom, I see that the job processed successfully and I have a total of 1000 rows in the database table.  

That completes the execution of the full batch job.  
