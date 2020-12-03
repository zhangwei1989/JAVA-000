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
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;


/**
 * @ClassName FirstDataSourceConfig
 * @Description config db0
 * @Author zhangwei
 * @Date 2020-12-03 10:37
 * @Version 1.0
 */
@Configuration
@MapperScan(basePackages ="com.multi.datasource.dao.firstDataSource.mapper", sqlSessionFactoryRef  = "db0SqlSessionFactory")
public class FirstDataSourceConfig {

    @Bean
    @ConfigurationProperties("spring.datasource.db0")
    public DataSourceConfig firstDataSourceProperties() {
        return new DataSourceConfig();
    }

    @Primary
    @Bean(name = "ds0DataSource")
    public DataSource firstDataSource() {
        return DataSourceUtils.createDataSource(firstDataSourceProperties(), new DriverConfig());
    }

    @Bean(name = "db0TransactionManager")
    @Primary
    public DataSourceTransactionManager firstTransactionManager() {
        return new DataSourceTransactionManager(firstDataSource());
    }

    @Bean(name = "db0SqlSessionFactory")
    @Primary
    public SqlSessionFactory firstSqlSessionFactory(@Qualifier("ds0DataSource") DataSource firstDataSource)
            throws Exception {
        final MybatisSqlSessionFactoryBean sessionFactory = new MybatisSqlSessionFactoryBean();
        sessionFactory.setDataSource(firstDataSource);
        sessionFactory.getObject().getConfiguration().setMapUnderscoreToCamelCase(true);
        return sessionFactory.getObject();
    }
}
