package com.hhovhann.jobmanagement.repository;

import com.hhovhann.jobmanagement.entity.JobEntity;
import com.hhovhann.jobmanagement.jobs.states.JobStates;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@Slf4j
@SpringBootTest
class SchedulerRepositoryTest {

    @Autowired
    private SchedulerRepository schedulerRepository;

    @BeforeEach
    public void init() {
        log.info("Clean Up database before each test method will be executed.");
        schedulerRepository.deleteAll();
    }

    @Test
    void findByJobName() {
        var jobEntity = new JobEntity("EmailJob", "EmailJobGroup", 10, JobStates.QUEUED, "com.hhovhann.jobmanagement.jobs.email.EmailJob");
        schedulerRepository.save(jobEntity);

        JobEntity currentJob = schedulerRepository.findByJobName(jobEntity.getJobName());
        assertEquals(currentJob.getJobName(), jobEntity.getJobName());
        AssertionsForClassTypes.assertThat(currentJob).isInstanceOf(JobEntity.class);
    }

    @Test
    void deleteByJobName() {
        var jobEntity = new JobEntity("EmailJob", "EmailJobGroup", 10, JobStates.QUEUED, "com.hhovhann.jobmanagement.jobs.email.EmailJob");
        schedulerRepository.save(jobEntity);

        schedulerRepository.deleteByJobName(jobEntity.getJobName());

        assertNull(schedulerRepository.findByJobName(jobEntity.getJobName()));
    }
}