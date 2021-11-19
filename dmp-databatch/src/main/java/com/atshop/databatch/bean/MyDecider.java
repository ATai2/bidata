package com.atshop.databatch.bean;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.JobExecutionDecider;

public class MyDecider implements JobExecutionDecider {
    private int counter = 1;

    @Override
    public FlowExecutionStatus decide(JobExecution jobExecution, StepExecution stepExecution) {
        counter++;
        if (counter % 2 == 0) {
            return new FlowExecutionStatus("even");
        }else {
            return new FlowExecutionStatus("odd");
        }
    }
}
