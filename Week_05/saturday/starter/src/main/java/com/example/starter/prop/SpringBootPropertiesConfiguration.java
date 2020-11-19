package com.example.starter.prop;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Properties;

/**
 * Spring boot properties configuration.
 */
@ConfigurationProperties(prefix = "spring.school")
@Getter
@Setter
public final class SpringBootPropertiesConfiguration {

    private int id;

    private String studentName;
}
