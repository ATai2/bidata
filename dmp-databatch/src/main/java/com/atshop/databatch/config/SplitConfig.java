package com.atshop.databatch.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;

@Configuration
@EnableBatchProcessing
public class SplitConfig {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Bean
    public Step splitflowstep1() {
        return stepBuilderFactory.get("splitflowstep1")
                .tasklet(new Tasklet() {
                    @Override
                    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
                        System.out.println("splitflowstep1  "+Thread.currentThread().getName());
                        return RepeatStatus.FINISHED;
                    }
                }).build();
    }

    @Bean
    public Step splitflowstep2() {
        return stepBuilderFactory.get("splitsplitflowstep2")
                .tasklet(new Tasklet() {
                    @Override
                    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
                        System.out.println("splitsplitflowstep2  "+Thread.currentThread().getName());
                        return RepeatStatus.FINISHED;
                    }
                }).build();
    }    @Bean
    public Step splitflowstep3() {
        return stepBuilderFactory.get("splitflowstep3")
                .tasklet(new Tasklet() {
                    @Override
                    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
                        System.out.println("splitflowstep3  "+Thread.currentThread().getName());
                        return RepeatStatus.FINISHED;
                    }
                }).build();
    }

    @Bean
    public Flow splitflowDemo() {
        return new FlowBuilder<Flow>("flowDemo")
                .start(splitflowstep1())
                .next(splitflowstep2())
                .build();
    }
    @Bean
    public Flow splitflowDemo2() {
        return new FlowBuilder<Flow>("flowDemo")
                .start(splitflowstep1())
                .next(splitflowstep2())
                .build();
    }

    @Bean
    public Job splitflowJob() {
        return jobBuilderFactory.get("flowJob")
                .start(splitflowDemo())
                .next(splitflowDemo2())
                .end()
                .build();
    }
    @Bean
    public Job splitflowJobConcu() {
        return jobBuilderFactory.get("splitflowJobConcu")
                .start(splitflowDemo())
                .split(new SimpleAsyncTaskExecutor()).add(splitflowDemo2())
                .end()
                .build();
    }


}
