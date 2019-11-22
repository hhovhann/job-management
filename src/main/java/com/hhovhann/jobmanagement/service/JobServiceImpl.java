package com.hhovhann.jobmanagement.service;

import com.hhovhann.jobmanagement.entity.JobEntity;
import com.hhovhann.jobmanagement.jobs.states.JobStates;
import com.hhovhann.jobmanagement.model.JobRequestBody;
import com.hhovhann.jobmanagement.model.JobResponseBody;
import com.hhovhann.jobmanagement.repository.SchedulerRepository;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobKey;
import org.quartz.SchedulerException;
import org.quartz.TriggerKey;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Slf4j
@Service
public class JobServiceImpl implements JobService {

    @Lazy
    private final SchedulerRepository schedulerRepository;

    @Lazy
    private final SchedulerFactoryBean schedulerFactoryBean;

    public JobServiceImpl(SchedulerRepository schedulerRepository, SchedulerFactoryBean schedulerFactoryBean) {
        this.schedulerRepository = schedulerRepository;
        this.schedulerFactoryBean = schedulerFactoryBean;
    }

    @Override
    public ResponseEntity<JobResponseBody> scheduleJob(JobRequestBody jobRequestBody) {
        var scheduler = schedulerFactoryBean.getScheduler();
        try {
            if (scheduler.checkExists(getJobKey(jobRequestBody))) {
                log.error("scheduleNewJobRequest with job name {} already exists.", jobRequestBody.getJobName());
                return new ResponseEntity<>(null, HttpStatus.CONFLICT);

            } else {
                var jobClass = (Class<QuartzJobBean>) Class.forName(jobRequestBody.getJobClass());
                var jobDetail = buildJobDetail(jobRequestBody, jobClass);
                var trigger = buildTrigger(jobRequestBody);

                scheduler.scheduleJob(jobDetail, trigger);

                var jobEntity = new JobEntity(jobRequestBody.getJobName(), jobRequestBody.getJobGroup(), jobRequestBody.getJobPriority(), JobStates.QUEUED, jobRequestBody.getJobClass());
                if (jobRequestBody.isCronJob()) {
                    jobEntity.setJobCronExpression(jobRequestBody.getCronExpression());
                }
                var storedJob = schedulerRepository.save(jobEntity);
                return new ResponseEntity<>(new JobResponseBody(storedJob.getJobName(), storedJob.getJobGroup(), storedJob.getJobState()), HttpStatus.CREATED);
            }
        } catch (ClassNotFoundException | SchedulerException e) {
            log.error("", e);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<Boolean> unScheduleJob(String jobName, String jobGroup) {
        try {
            if (Objects.nonNull(schedulerRepository.findByJobName(jobName))) {
                return new ResponseEntity<>(schedulerFactoryBean.getScheduler().unscheduleJob(new TriggerKey(jobName, jobGroup)), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(false, HttpStatus.NOT_MODIFIED);
            }
        } catch (SchedulerException e) {
            log.error("Failed to un-schedule job - {}", jobName, e);
            return new ResponseEntity<>(false, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<Boolean> deleteJob(String jobName, String jobGroup) {
        try {
            boolean deleted = schedulerFactoryBean.getScheduler().deleteJob(new JobKey(jobName, jobGroup));
            if (deleted) {
                schedulerRepository.deleteByJobName(jobName);
                return new ResponseEntity<>(true, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(false, HttpStatus.NOT_MODIFIED);
            }

        } catch (SchedulerException e) {
            log.error("Failed to delete job - {} {}", jobName, jobGroup, e);
            return new ResponseEntity<>(false, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<Boolean> pauseJob(String jobName, String jobGroup) {
        try {
            schedulerFactoryBean.getScheduler().pauseJob(new JobKey(jobName, jobGroup));
            return new ResponseEntity<>(true, HttpStatus.OK);
        } catch (SchedulerException e) {
            log.error("Failed to pause job - {}", jobName, e);
            return new ResponseEntity<>(false, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<Boolean> resumeJob(String jobName, String jobGroup) {
        try {
            schedulerFactoryBean.getScheduler().resumeJob(new JobKey(jobName, jobGroup));
            return new ResponseEntity<>(true, HttpStatus.OK);
        } catch (SchedulerException e) {
            log.error("Failed to resume job - {}", jobName, e);
            return new ResponseEntity<>(false, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
