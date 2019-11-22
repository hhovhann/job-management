package com.hhovhann.jobmanagement.model;

import com.hhovhann.jobmanagement.jobs.states.JobStates;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JobResponseBody {
    private String jobName;
    private String jobGroup;
    private JobStates jobState;
}
