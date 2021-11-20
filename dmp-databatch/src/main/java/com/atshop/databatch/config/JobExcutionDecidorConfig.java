package com.atshop.databatch.config;

import com.atshop.databatch.bean.MyDecider;
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
public class JobExcutionDecidorConfig {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Bean
    public Step jobExcutionDecidor1() {
        return stepBuilderFactory.get("jobExcutionDecidor1")
                .tasklet(new Tasklet() {
                    @Override
                    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
                        System.out.println("jobExcutionDecidor1  " + Thread.currentThread().getName());

                        return RepeatStatus.FINISHED;
                    }
                }).build();
    }

    @Bean
    public Step jobExcutionDecidor2() {
        return stepBuilderFactory.get("splitjobExcutionDecidor2")
                .tasklet(new Tasklet() {
                    @Override
                    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
                        System.out.println("splitjobExcutionDecidor2  " + Thread.currentThread().getName());
                        System.out.println("even");
                        return RepeatStatus.FINISHED;
                    }
                }).build();
    }

    @Bean
    public Step jobExcutionDecidor3() {
        return stepBuilderFactory.get("jobExcutionDecidor3")
                .tasklet(new Tasklet() {
                    @Override
                    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
                        System.out.println("jobExcutionDecidor3  " + Thread.currentThread().getName());
                        System.out.println("odd");
                        return RepeatStatus.FINISHED;
                    }
                }).build();
    }

    //    @Bean
//    public Flow splitflowDemo() {
//        return new FlowBuilder<Flow>("flowDemo")
//                .start(jobExcutionDecidor1())
//                .next(jobExcutionDecidor2())
//                .build();
//    }
//    @Bean
//    public Flow splitflowDemo2() {
//        return new FlowBuilder<Flow>("flowDemo")
//                .start(jobExcutionDecidor1())
//                .next(jobExcutionDecidor2())
//                .build();
//    }
    @Bean
    public MyDecider myDecider() {
        return new MyDecider();
    }

//    @Bean
//    public Job jobExcutionDecidorJob() {
//        return jobBuilderFactory.get("flowJob")
//                .start(jobExcutionDecidor1())
//                .next(myDecider())
//                .from(myDecider()).on("even").to(jobExcutionDecidor2())
//                .from(myDecider()).on("odd").to(jobExcutionDecidor3())
//                .from(jobExcutionDecidor3()).on("*").to(myDecider())
//                .end()
//                .build();
//    }


}
