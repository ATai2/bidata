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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@EnableBatchProcessing
@Configuration
public class ReadWriteDbConfig {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private DataSource datasource;

    @Autowired
    private DbJdbcWriter dbJdbcWriter;
    @Bean
    public Step readStep2() {
      return  stepBuilderFactory.get("reader")
                .<User,User>chunk(10)
                .reader(read2())
                .writer(dbJdbcWriter)
                .build();
    }

    @Bean
    @StepScope
    public  ItemReader<User> read2() {

        JdbcPagingItemReader<User> reader = new JdbcPagingItemReader<User>();
        reader.setDataSource(datasource);
        reader.setFetchSize(1000);
        reader.setRowMapper(new RowMapper<User>() {
            @Override
            public User mapRow(ResultSet resultSet, int i) throws SQLException {
                User user =new User();
                user.setId(resultSet.getString("id"));
                user.setName(resultSet.getString("name"));
                user.setAddress(resultSet.getString("address"));
                return user;
            }
        });

        MySqlPagingQueryProvider provider = new MySqlPagingQueryProvider();
        provider.setSelectClause("id,name,address");
        provider.setFromClause("t_user");
        Map<String, Order> sort = new HashMap<String, Order>();
        sort.put("id", Order.DESCENDING);
        provider.setSortKeys(sort);
        reader.setQueryProvider(provider);


        return reader;

//        return new MyReader2(list);
    }

    @Bean
    public Job readWriteJob2() {
        return jobBuilderFactory.get("readwrite2")
                .start(readStep2())

                .build();


    }


}
