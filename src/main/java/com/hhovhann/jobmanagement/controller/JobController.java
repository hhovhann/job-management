package com.hhovhann.jobmanagement.controller;

import com.hhovhann.jobmanagement.model.JobRequestBody;
import com.hhovhann.jobmanagement.model.JobResponseBody;
import com.hhovhann.jobmanagement.service.JobService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/scheduler/")
public class JobController {

    @Lazy
    private final JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    @PostMapping(value = "/schedule", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<JobResponseBody> schedule(@RequestBody JobRequestBody requestBody) {
        return jobService.scheduleJob(requestBody);
    }

    @PostMapping(value = "/unschedule/{jobName}/{jobGroup}", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Boolean> unSchedule(@PathVariable("jobName") String jobName, @PathVariable("jobGroup") String jobGroup) {
        return jobService.unScheduleJob(jobName, jobGroup);
    }

    @PostMapping(value = "/pause/{jobName}/{jobGroup}", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Boolean> pause(@PathVariable("jobName") String jobName, @PathVariable("jobGroup") String jobGroup) {
        return jobService.pauseJob(jobName, jobGroup);
    }

    @PostMapping(value = "/resume/{jobName}/{jobGroup}", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Boolean> resume(@PathVariable("jobName") String jobName, @PathVariable("jobGroup") String jobGroup) {
        return jobService.resumeJob(jobName, jobGroup);
    }

    @DeleteMapping(value = "/delete/{jobName}/{jobGroup}", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Boolean> delete(@PathVariable("jobName") String jobName, @PathVariable("jobGroup") String jobGroup) {
        return jobService.deleteJob(jobName, jobGroup);
    }
}