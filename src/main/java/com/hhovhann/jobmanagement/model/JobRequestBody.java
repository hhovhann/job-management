package com.hhovhann.jobmanagement.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class JobRequestBody {

    @NotNull
    private String jobName;

    @NotNull
    private String jobGroup;

    @NotNull
    private int jobPriority;

    @NotNull
    private String jobClass;

    @NotNull
    private String jobDescription;

    @NotNull
    private boolean cronJob;

    private String cronExpression;
}