package com.hhovhann.jobmanagement.configuration;

import com.hhovhann.jobmanagement.jobs.listener.SchedulerJobListener;
import com.hhovhann.jobmanagement.repository.SchedulerRepository;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Trigger;
import org.springframework.boot.autoconfigure.quartz.QuartzProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * Configure quartz. Also make sure the migration scrip has run before we do that.
 */
@Slf4j
@Configuration
@DependsOn(FlywayConfiguration.FLYWAY)
public class QuartzConfiguration {
    private final ApplicationContext applicationContext;
    private final DataSource dataSource;
    private final QuartzProperties quartzProperties;
    private final SchedulerRepository schedulerRepository;

    public QuartzConfiguration(ApplicationContext applicationContext, DataSource dataSource, QuartzProperties quartzProperties, SchedulerRepository schedulerRepository) {
        this.applicationContext = applicationContext;
        this.dataSource = dataSource;
        this.quartzProperties = quartzProperties;
        this.schedulerRepository = schedulerRepository;
    }


    @Bean
    public SchedulerFactoryBean schedulerFactory(PlatformTransactionManager transactionManager, Trigger... triggers) {

        var jobFactory = new SchedulerJobFactory();
        jobFactory.setApplicationContext(applicationContext);

        var properties = new Properties();
        properties.putAll(quartzProperties.getProperties());

        var factory = new SchedulerFactoryBean();
        factory.setGlobalJobListeners(new SchedulerJobListener(schedulerRepository));
        factory.setOverwriteExistingJobs(true);
        factory.setAutoStartup(true);
        factory.setQuartzProperties(properties);
        factory.setJobFactory(jobFactory);
        factory.setTransactionManager(transactionManager);
        factory.setWaitForJobsToCompleteOnShutdown(true);
        factory.setTriggers(triggers);
        factory.setDataSource(dataSource);
        return factory;
    }
}
