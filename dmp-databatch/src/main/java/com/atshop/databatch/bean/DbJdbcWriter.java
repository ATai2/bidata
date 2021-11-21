package com.atshop.databatch.bean;

import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Consumer;

@Component
public class DbJdbcWriter implements ItemWriter<User> {
    @Override
    public void write(List<? extends User> list) throws Exception {
        System.out.println("write begin");
        list.forEach(new Consumer<User>() {
            @Override
            public void accept(User user) {
                System.out.println(user.toString());
            }
        });
        System.out.println("write end");
    }
}
