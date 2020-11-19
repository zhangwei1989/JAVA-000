package com.example.starter.service;

import com.example.starter.pojo.ISchool;
import com.example.starter.pojo.School;
import lombok.Data;

/**
 * @ClassName SchoolService
 * @Description TODO
 * @Author zhangwei
 * @Date 2020-11-18 21:16
 * @Version 1.0
 */
@Data
public class SchoolService {

    private School school;

    public void ding() {
        System.out.println("Class1 have " + this.school.getKlass().getStudents().size() + " students and one is " + this.school.getStudent());
    }
}
