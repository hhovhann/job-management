package com.hhovhann.jobmanagement.controller;

import com.hhovhann.jobmanagement.jobs.states.JobStates;
import com.hhovhann.jobmanagement.model.JobRequestBody;
import com.hhovhann.jobmanagement.model.JobResponseBody;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@Slf4j
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class JobControllerTest {

    @Autowired
    private JobController jobController;
    @Autowired
    private SchedulerFactoryBean schedulerFactoryBean;

    @Test
    @Order(1)
    public void scheduleJobWithBodyTest() throws Exception {
        ResponseEntity<JobResponseBody> responseEntity = jobController.schedule(
                new JobRequestBody(
                        "StoreContentJob",
                        "StoreContentGroup",
                        10,
                        "com.hhovhann.jobmanagement.jobs.content.StoreContentJob",
                        "StoreContent Job description",
                        false,
                        "0 */1 * * * ?"));
        JobResponseBody body = responseEntity.getBody();
        assertNotEquals(body, null);
        assertEquals(body.getJobState(), JobStates.QUEUED);
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
    }

    @Test
    @Order(2)
    public void scheduleJobWithWrongBodyTest() throws Exception {
        ResponseEntity<JobResponseBody> responseEntity = jobController.schedule(
                new JobRequestBody(
                        "StoreContentJob1",
                        "StoreContentGroup1",
                        10,
                        "com.hhovhann.jobmanagement.jobs.content.StoreContentJob1",
                        "StoreContent Job description",
                        false,
                        "0 */1 * * * ?"));
        assertEquals(responseEntity.getBody(), null);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
    }

    @Test
    @Order(3)
    public void unScheduleJobWithJobNameTest() throws Exception {
        var jobName = "StoreContentJob";
        var jobGroup = "StoreContentGroup";

        ResponseEntity<Boolean> responseEntity = jobController.unSchedule(jobName, jobGroup);
        assertEquals(responseEntity.getBody(), true);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    @Order(4)
    public void unScheduleJobWithWrongJobNameTest() throws Exception {
        var jobName = "StoreContentJob1";
        var jobGroup = "StoreContentGroup1";

        ResponseEntity<Boolean> responseEntity = jobController.unSchedule(jobName, jobGroup);
        assertEquals(responseEntity.getBody(), false);
        assertEquals(HttpStatus.NOT_MODIFIED, responseEntity.getStatusCode());

    }

    @Test
    @Order(5)
    public void pauseJobWithJobNameAndJobGroupTest() throws Exception {
        var jobName = "StoreContentJob";
        var jobGroup = "StoreContentGroup";

        ResponseEntity<Boolean> responseEntity = jobController.pause(jobName, jobGroup);
        assertEquals(responseEntity.getBody(), true);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

    }

    @Test
    @Order(6)
    public void resumeJobWithJobNameAndJobGroupTest() throws Exception {
        var jobName = "StoreContentJob";
        var jobGroup = "StoreContentGroup";

        ResponseEntity<Boolean> responseEntity = jobController.resume(jobName, jobGroup);
        assertEquals(responseEntity.getBody(), true);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

    }

    @Test
    @Order(7)
    public void deleteJobWithJobNameAndJobGroupTest() throws Exception {
        var jobName = "StoreContentJob";
        var jobGroup = "StoreContentGroup";

        ResponseEntity<Boolean> responseEntity = jobController.delete(jobName, jobGroup);
        assertEquals(responseEntity.getBody(), true);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

    }
}