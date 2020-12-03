package com.multi.datasource.config;

import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import com.zctj.autoconfigure.jpa.mysql.DataSourceUtils;
import com.zctj.autoconfigure.jpa.mysql.config.DataSourceConfig;
import com.zctj.autoconfigure.jpa.mysql.config.DriverConfig;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

/**
 * @ClassName SecondDataSourceConfig
 * @Description config db1
 * @Author zhangwei
 * @Date 2020-12-03 15:32
 * @Version 1.0
 */
@Configuration
@MapperScan(basePackages ="com.multi.datasource.dao.secondDataSource.mapper", sqlSessionFactoryRef  = "db1SqlSessionFactory")
public class SecondDataSourceConfig {

    @Bean
    @ConfigurationProperties("spring.datasource.db1")
    public DataSourceConfig secondDataSourceProperties() {
        return new DataSourceConfig();
    }

    @Bean(name = "ds1DataSource")
    public DataSource secondDataSource() {
        return DataSourceUtils.createDataSource(secondDataSourceProperties(), new DriverConfig());
    }

    @Bean(name = "db1TransactionManager")
    public DataSourceTransactionManager secondTransactionManager() {
        return new DataSourceTransactionManager(secondDataSource());
    }

    @Bean(name = "db1SqlSessionFactory")
    public SqlSessionFactory secondSqlSessionFactory(@Qualifier("ds1DataSource") DataSource secondDataSource)
            throws Exception {
        final MybatisSqlSessionFactoryBean sessionFactory = new MybatisSqlSessionFactoryBean();
        sessionFactory.setDataSource(secondDataSource);
        sessionFactory.getObject().getConfiguration().setMapUnderscoreToCamelCase(true);
        return sessionFactory.getObject();
    }
}
