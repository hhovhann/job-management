package com.hhovhann.jobmanagement.repository;

import com.hhovhann.jobmanagement.entity.JobEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface SchedulerRepository extends JpaRepository<JobEntity, Long> {
    JobEntity findByJobName(String jobName);
    @Transactional
    Long deleteByJobName(String jobName);
}
