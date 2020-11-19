package com.example.starter;

import com.example.starter.pojo.Klass;
import com.example.starter.pojo.Student;
import com.example.starter.prop.SpringBootPropertiesConfiguration;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Configuration
@EnableConfigurationProperties(SpringBootPropertiesConfiguration.class)
@ConditionalOnProperty(prefix = "spring.school", name = "enabled", havingValue = "true")
@RequiredArgsConstructor
public class SpringBootConfiguration {

    private final SpringBootPropertiesConfiguration props;

    @Bean
    public Student configStudent(SpringBootPropertiesConfiguration springBootPropertiesConfiguration) {
        return new Student(springBootPropertiesConfiguration.getId(), springBootPropertiesConfiguration.getStudentName());
    }

}
