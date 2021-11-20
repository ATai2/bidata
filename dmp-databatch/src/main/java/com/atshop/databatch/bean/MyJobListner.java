package com.atshop.databatch.bean;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

public class MyJobListner implements JobExecutionListener {
    @Override
    public void beforeJob(JobExecution jobExecution) {
        System.out.println("job start");
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        System.out.println("job end");
    }
}
