package com.atshop.databatch.config;

import com.atshop.databatch.bean.MyReader2;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@EnableBatchProcessing
@Configuration
public class ReadWriteConfig {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Bean
    public Step readStep() {
      return  stepBuilderFactory.get("reader")
                .<String,String>chunk(10)
                .reader(read2())
                .processor(new ItemProcessor<String, String>() {
                    @Override
                    public String process(String s) throws Exception {
                        System.out.println("process:" + s);
                        return s.replace("string", "step");
                    }
                })
                .writer(new ItemWriter<String>() {
                    @Override
                    public void write(List<? extends String> list) throws Exception {
                        list.stream().forEach(new Consumer<String>() {
                            @Override
                            public void accept(String s) {
                                System.out.println(s);
                            }
                        });
                    }
                })
                .build();
    }

    private ItemReader<String> read2() {
        List<String> list = new ArrayList<String>();
        for (int i = 0; i < 100; i++) {
            list.add("string_" + i);
        }
        return new MyReader2(list);
    }

//    @Bean
//    public Job readWriteJob() {
//        return jobBuilderFactory.get("readwrite")
//                .start(readStep())
//                .build();
//
//
//    }


}
