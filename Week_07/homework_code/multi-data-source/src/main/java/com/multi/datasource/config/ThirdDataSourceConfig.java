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
 * @ClassName ThirdDataSourceConfig
 * @Description ThirdDataSourceConfig
 * @Author zhangwei
 * @Date 2020-12-06 16:21
 * @Version 1.0
 */
@Configuration
@MapperScan(basePackages ="com.multi.datasource.dao.thirdDataSource.mapper", sqlSessionFactoryRef  = "db2SqlSessionFactory")
public class ThirdDataSourceConfig {

    @Bean
    @ConfigurationProperties("spring.datasource.db2")
    public DataSourceConfig thirdDataSourceProperties() {
        return new DataSourceConfig();
    }

    @Bean(name = "ds2DataSource")
    public DataSource thirdDataSource() {
        return DataSourceUtils.createDataSource(thirdDataSourceProperties(), new DriverConfig());
    }

    @Bean(name = "db2TransactionManager")
    public DataSourceTransactionManager thirdTransactionManager() {
        return new DataSourceTransactionManager(thirdDataSource());
    }

    @Bean(name = "db2SqlSessionFactory")
    public SqlSessionFactory thirdSqlSessionFactory(@Qualifier("ds2DataSource") DataSource thirdDataSource)
            throws Exception {
        final MybatisSqlSessionFactoryBean sessionFactory = new MybatisSqlSessionFactoryBean();
        sessionFactory.setDataSource(thirdDataSource);
        sessionFactory.getObject().getConfiguration().setMapUnderscoreToCamelCase(true);
        return sessionFactory.getObject();
    }
}
