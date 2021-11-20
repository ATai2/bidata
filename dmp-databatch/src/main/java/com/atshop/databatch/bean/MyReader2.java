package com.atshop.databatch.bean;

import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

import java.util.Iterator;
import java.util.List;

public class MyReader2 implements ItemReader<String> {

    private final Iterator<String> iterator;
    private List<String> list;

    public MyReader2(List<String> list) {
        this.iterator = list.iterator();
    }

    @Override
    public String read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        if (iterator.hasNext()) {
            String next = iterator.next();
            System.out.println("has element:" + next);
            return next;
        } else {
            System.out.println("no more");
            return null;
        }

    }
}
