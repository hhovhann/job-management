package com.hhovhann.jobmanagement.jobs.listener;

import com.hhovhann.jobmanagement.entity.JobEntity;
import com.hhovhann.jobmanagement.jobs.states.JobStates;
import com.hhovhann.jobmanagement.repository.SchedulerRepository;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobListener;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Slf4j
@Component
public class SchedulerJobListener implements JobListener {
    public static final String LISTENER_NAME = "job-management-demo-app-listener";

    private final SchedulerRepository schedulerRepository;

    public SchedulerJobListener(SchedulerRepository schedulerRepository) {
        this.schedulerRepository = schedulerRepository;
    }

    @Override
    public String getName() {
        return LISTENER_NAME;
    }

    @Override
    public void jobToBeExecuted(JobExecutionContext context) {
        var jobName = context.getJobDetail().getKey().getName();
        log.debug("Job : " + jobName + " is going to start...");
        JobEntity currentJob = schedulerRepository.findByJobName(jobName);
        currentJob.setJobState(JobStates.RUNNING);
        schedulerRepository.save(currentJob);
    }

    @Override
    public void jobExecutionVetoed(JobExecutionContext context) {
        var jobName = context.getJobDetail().getKey().getName();
        log.debug("Job : " + jobName + " associated Trigger has occurred ...");
    }

    @Override
    public void jobWasExecuted(JobExecutionContext context, JobExecutionException jobException) {
        var jobName = context.getJobDetail().getKey().getName();
        var currentJob = schedulerRepository.findByJobName(jobName);
        if (Objects.nonNull(jobException) && !Objects.equals(jobException.getMessage(), "")) {
            log.error("Exception thrown by: " + jobName + " Exception: " + jobException.getMessage());
            currentJob.setJobState(JobStates.FAILED);
        } else {
            currentJob.setJobState(JobStates.SUCCESS);
        }
        schedulerRepository.save(currentJob);
        log.debug("Job : " + jobName + " is finished...");
    }
}
