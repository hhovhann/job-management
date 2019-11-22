package com.hhovhann.jobmanagement.entity;

import com.hhovhann.jobmanagement.jobs.states.JobStates;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@DynamicUpdate
@Table(catalog = "quartz", name = "JOB_ENTITY")
public class JobEntity {

    @Id
    @Column(name = "JOB_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "JOB_NAME")
    private String jobName;

    @Column(name = "JOB_GROUP")
    private String jobGroup;

    @Column(name = "JOB_PRIORITY")
    private int jobPriority;

    @Column(name = "JOB_STATE")
    private JobStates jobState;

    @Column(name = "JOB_CLASS")
    private String jobClass;

    @Column(name = "JOB_DESCRIPTION")
    private String jobDescription;

    @Column(name = "JOB_CRON_EXPRESSION")
    private String jobCronExpression;

    public JobEntity(String jobName, String jobGroup, int jobPriority, JobStates jobState, String jobClass) {
        this.jobName = jobName;
        this.jobGroup = jobGroup;
        this.jobPriority = jobPriority;
        this.jobState = jobState;
        this.jobClass = jobClass;
    }
}