package com.hhovhann.jobmanagement.jobs.email;

import lombok.extern.slf4j.Slf4j;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import java.util.stream.IntStream;

@Slf4j
@Component
@DisallowConcurrentExecution
public class EmailJob extends QuartzJobBean {

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        log.info("EmailJob Start................");
        IntStream.range(0, 5).forEach(i -> {
            log.info("This job sending email - {}", i);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                log.error(e.getMessage(), e);
            }
        });
        log.info("EmailJob End................");
    }
}
