package com.atshop.databatch.bean;

import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Table(name = "user")
public class User {
    @Id
    private String id;
    private String name;
    private String address;

}

