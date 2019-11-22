package com.hhovhann.jobmanagement.jobs.states;


public enum JobStates {
    QUEUED("QUEUED"), RUNNING("RUNNING"), SUCCESS("SUCCESS"), FAILED("FAILED");

    JobStates(String queued) {
    }
}
