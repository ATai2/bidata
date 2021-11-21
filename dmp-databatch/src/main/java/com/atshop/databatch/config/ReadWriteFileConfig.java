package com.atshop.databatch.config;

import com.atshop.databatch.bean.DbJdbcWriter;
import com.atshop.databatch.bean.User;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.support.MySqlPagingQueryProvider;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.validation.BindException;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@EnableBatchProcessing
@Configuration
public class ReadWriteFileConfig {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private DataSource datasource;

    @Autowired
    private DbJdbcWriter dbJdbcWriter;
    @Bean
    public Step readStepCsv() {
      return  stepBuilderFactory.get("reader")
                .<User,User>chunk(2)
                .reader(readFileCvs())
                .writer(dbJdbcWriter)
                .build();
    }

    @Bean
    @StepScope
    public  ItemReader<User> readFileCvs() {

        FlatFileItemReader<User> reader = new FlatFileItemReader<User>();
        reader.setResource(new ClassPathResource("user.csv"));
        reader.setLinesToSkip(1);

        DelimitedLineTokenizer delimitedLineTokenizer=new DelimitedLineTokenizer();
        delimitedLineTokenizer.setDelimiter(",");
        delimitedLineTokenizer.setNames("id,name,address");
        DefaultLineMapper<User> mapper = new DefaultLineMapper<User>();
        mapper.setLineTokenizer(delimitedLineTokenizer);
        mapper.setFieldSetMapper(fieldSet -> {
            User user = new User();
            user.setId(fieldSet.readString("id"));
            user.setName(fieldSet.readString("name"));
            user.setAddress(fieldSet.readString("address"));
            return user;
        });
        mapper.afterPropertiesSet();
        reader.setLineMapper(mapper);
        return reader;
    }

    @Bean
    public Job readWriteJobCvs() {
        return jobBuilderFactory.get("readwriteCvs")
                .start(readStepCsv())
                .build();
    }



}
