package com.luke.luketools.testDB;

import jakarta.persistence.*;
import lombok.Data;


@Entity
@Data
@Table(name = "userinfo")
public class UserInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer userId;
    private String address;
    private String name;
    private Integer age;
}
