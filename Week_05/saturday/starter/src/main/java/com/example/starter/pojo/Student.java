package com.example.starter.pojo;


import lombok.*;

import java.io.Serializable;


@AllArgsConstructor
@NoArgsConstructor
@ToString
/**
 * @org.apache.xbean.XBean
 */
public class Student implements Serializable {

    @Getter
    @Setter
    private int id;

    @Getter
    @Setter
    private String studentName;

    public void init() {
        System.out.println("hello...........");
    }

    public Student create() {
        return new Student(101, "KK101");
    }
}
