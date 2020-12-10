package com.shardingsphere.proxy.proxymiddleware.config;

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
@MapperScan(basePackages ="com.shardingsphere.proxy.proxymiddleware.dao.mapper", sqlSessionFactoryRef  = "sqlSessionFactory")
public class DataConfig {

    @Bean
    @ConfigurationProperties("spring.datasource.db")
    public DataSourceConfig dataSourceProperties() {
        return new DataSourceConfig();
    }

    @Primary
    @Bean(name = "dataSource")
    public DataSource dataSource() {
        return DataSourceUtils.createDataSource(dataSourceProperties(), new DriverConfig());
    }

    @Bean(name = "transactionManager")
    @Primary
    public DataSourceTransactionManager transactionManager() {
        return new DataSourceTransactionManager(dataSource());
    }

    @Bean(name = "sqlSessionFactory")
    @Primary
    public SqlSessionFactory sqlSessionFactory(@Qualifier("dataSource") DataSource dataSource)
            throws Exception {
        final MybatisSqlSessionFactoryBean sessionFactory = new MybatisSqlSessionFactoryBean();
        sessionFactory.setDataSource(dataSource);
        sessionFactory.getObject().getConfiguration().setMapUnderscoreToCamelCase(true);
        return sessionFactory.getObject();
    }
}
