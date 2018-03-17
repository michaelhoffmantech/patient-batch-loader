# Demos

Below are the full details of demos for the Pluralsight Spring Batch course. The goal here is to help you follow along with code demonstrations from the course by giving you the ability to copy and paste artifacts from here. I've numbered the demos to correspond with the course segments.

# Demo Contents

[Demo 1]()

[Demo 2](#demo-2-gradle-dependencies)

#Demo 1: Project Download, IDE Setup and Execution

In this demonstration, I'll be showing you how to get the demo project from GitHub. I'll walk you through cloning the project, adding the project to the IDE and then running it from a command line.

##1.1 - Project Download

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

##1.2 - IDE Setup

Next, let's add the project to the Intellij IDEA IDE.

1. I've opened the IntelliJ IDEA new project form. 
2. I'm going to select to open an existing project from source
3. Select the build.gradle file from the project's root directory
4. Make sure that JDK 8 is selected
5. Import the project

Once this is complete, we've successfully loaded the project template. Now, let's go back to the command line and try to run the application to assure it works. 

##1.3 - Project Execution

Now, let's try to run the application to assure its working.

1. I've opened back the command prompt to c:\demo\patient-batch-loader. 
2. Let's run the command to start the application: 
    ```cmd
    .\gradlew bootRun
    ```
3. This will start the application using Spring Boot's runner
4. If you see the message that the application is running and you don't get any exceptions, it means you have successfully set up the project.
5. Note here in the log that we have the Spring Boot and Spring versions.
6. We are using the dev spring profile. That's simple a spring profile used by default to configure for a development environment. 
7. We are using an Undertow embedded web server to run the application
8. Finally, note the port is 8080.

Let's open back up the browser to perform a simple verification.

##1.4 - Runtime Verification

Finally, let's verify runtime. 

1. I've opened up Chrome and I'm going to the application's URL for its health check:
    ```cmd
    http://localhost:8080/actuator/health
    ```
2. This is one of many URLs provided by the actuator dependency to provide you with metrics and observability around the application. I've included it as part of the template to support any issues you may run into along the way with the course.

This completes setup of the template application. Now, let's look at its contents.

#Demo 2 Gradle Dependencies

In this demo, I'll walk you through adding the Spring Batch libraries to the demo application. These properties will be adding the Gradle build script in the dependencies section.

##2.1 - Update Gradle Build Script

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

#Demo 3 - Spring Batch Configuration

In this demo, I'll be covering the steps needed for a configuring Spring Batch to be available in the Spring Container of your project. 

##3.1 - Creating the BatchConfiguration Class

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

##3.2 - Updating the Application Properties

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
            inputPath: c:/input
    ```
5. Note that I'm using the input path as my c drive's input folder. A few things to consider. As this is a demo, I've defined the property in the base file; however, there are also property files for the Spring dev profile and prod profile. It is recommended that you override this property within those property files individually.
6. In order for me to access these properties, I've created an ApplicationProperties class that maps to the property names and values. Let's look at that now. 
7. Open the class ApplicationProperties found in the package com.pluralsight.springbatch.patientbatchloader.config. 
8. This class is annotated with the ConfigurationProperties annotation. Note that the prefix matches the property name we defined in the properties file. I'll define a value for this now, including a static class for the mapping. 
    ```
	private final Batch batch = new Batch(); 
	
	public Batch getBatch() {
		return batch;
	}
	
	public static class Batch {
		private String inputPath = "c:/input";
		
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

#Demo 4 - Spring Batch JobRepository Database Schema Configuration

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
11. Open a command prompt and navigate to the project directory at c:\demo\patient-batch-loader.
12. Run the command to start the application:
    ```
    .\gradlew bootRun
    ```
13. Once the server has started, verify that there were no errors on load. 
14. Open a browser. 
15. Navigate to the following URL:
    ```
    http://localhost:8080/console
    ```
16. Entering the URL should bring up the console for the H2 database. If it doesn't, you may want to check the error logs in case of a failure. Make sure to select/enter the following in the form:
    ```
    Saved Settings: Generic H2 (Server)
    Setting Name: Generic H2 (Server)
    Driver Class: org.h2.Driver
    JDBC URL: jdbc:h2:file:./h2db/db/patientbatchloader-dev;
    User Name: PatientBatchLoader
    Password: <empty>
    ```
17. First, click the test connection button to verify you get a successful connection. Then, click the connect button.
18. If Liquibase was successful, you should see the list of database tables created from the schema file in the console.

This verifies the configuration of the Spring Batch database schema. 

#Demo 5 - Spring Batch Job Configuration

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
6. Next, let's add the defintion for a JobRegistryBeanPostProcessor bean. Again, this will provide support for registering our job with the job repository:
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
11. Open the Java class constants in the same config package as BatchJobConfiguration. 
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

#Demo 6 - 
