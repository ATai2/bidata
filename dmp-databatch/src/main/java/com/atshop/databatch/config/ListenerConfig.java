package com.atshop.databatch.config;

import com.atshop.databatch.bean.MyChunkListner;
import com.atshop.databatch.bean.MyJobListner;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableBatchProcessing
public class ListenerConfig {
    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job listenerJob() {
        return jobBuilderFactory.get("listnerJob")
                .start(listenerstep1())
                .listener(new MyJobListner())
                .build();
    }

    @Bean
    public Step listenerstep1() {
        return stepBuilderFactory.get("step")
                .<String,String>chunk(2)
                .faultTolerant()
                .listener(new MyChunkListner())
                .reader(read())
                .writer(write())
//                .tasklet((stepContribution, chunkContext) -> {
//                    System.out.println("hello");
//                    return RepeatStatus.FINISHED;
//                })
                .build();
    }

    private ItemWriter<String> write() {
        return new ItemWriter<String>() {
            @Override
            public void write(List<? extends String> list) throws Exception {
                System.out.println(Arrays.toString(list.toArray()));
            }
        };
    }


    public ItemReader<String> read() {

        return new ListItemReader<String>(Arrays.asList("a","b","c","d","e","f"));

    }
}
