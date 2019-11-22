package com.hhovhann.jobmanagement.service;

import com.hhovhann.jobmanagement.model.JobRequestBody;
import com.hhovhann.jobmanagement.model.JobResponseBody;
import org.quartz.*;
import org.springframework.http.ResponseEntity;

public interface JobService {

    default JobDetail buildJobDetail(JobRequestBody jobRequestBody, Class<? extends Job> jobClass) {
        return JobBuilder.newJob(jobClass)
                .withIdentity(getJobKey(jobRequestBody))
                .withDescription(jobRequestBody.getJobDescription())
                .requestRecovery()
                .storeDurably()
                .build();
    }

    default Trigger buildTrigger(JobRequestBody job) {
        var triggerBuilder = TriggerBuilder.newTrigger()
                .withIdentity(job.getJobName(), job.getJobGroup())
                .withPriority(job.getJobPriority());
        if (job.isCronJob()) {
            return triggerBuilder
                    .withSchedule(CronScheduleBuilder.cronSchedule(job.getCronExpression())).build();
        } else {
            return triggerBuilder.build();
        }
    }

    default JobKey getJobKey(JobRequestBody job) {
        return JobKey.jobKey(job.getJobName(), job.getJobGroup());
    }

    ResponseEntity<JobResponseBody> scheduleJob(JobRequestBody jobEntity);

    ResponseEntity<Boolean> unScheduleJob(String jobName, String jobGroup);

    ResponseEntity<Boolean> deleteJob(String jobName, String jobGroup);

    ResponseEntity<Boolean> pauseJob(String jobName, String jobGroup);

    ResponseEntity<Boolean> resumeJob(String jobName, String jobGroup);

}
