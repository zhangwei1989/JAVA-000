package com.example.starter.pojo;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Data
public class School implements ISchool {

    private Klass klass;

    private Student student;
}
