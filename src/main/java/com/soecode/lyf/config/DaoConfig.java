package com.soecode.lyf.config;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.soecode.lyf.dao.AppointmentDao;
import com.soecode.lyf.dao.BookDao;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.mapper.MapperFactoryBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import javax.sql.DataSource;
import java.beans.PropertyVetoException;

@Configuration
@PropertySource("classpath:jdbc.properties")
@MapperScan("com.soecode.lyf.dao")
public class DaoConfig {

    @Value("${jdbc.driver}")
    private String driver;

    @Value("${jdbc.url}")
    private String url;

    @Value("${jdbc.username}")
    private String username;

    @Value("${jdbc.password}")
    private String password;

    @Bean
    public DataSource dataSource() throws PropertyVetoException {
        ComboPooledDataSource dataSource = new ComboPooledDataSource();
        dataSource.setDriverClass(driver);
        dataSource.setJdbcUrl(url);
        dataSource.setUser(username);
        dataSource.setPassword(password);
        dataSource.setMaxPoolSize(30);
        dataSource.setMinPoolSize(10);
        dataSource.setAutoCommitOnClose(false);
        dataSource.setCheckoutTimeout(10000);
        dataSource.setAcquireRetryAttempts(2);
        return dataSource;
    }

    @Bean
    public SqlSessionFactoryBean sqlSessionFactory(DataSource dataSource) {
        org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
        configuration.setUseColumnLabel(true);
        configuration.setUseGeneratedKeys(true);
        configuration.setMapUnderscoreToCamelCase(true);
        SqlSessionFactoryBean factory = new SqlSessionFactoryBean();
        factory.setTypeAliasesPackage("com.soecode.lyf.entity");
        factory.setDataSource(dataSource);
        factory.setConfiguration(configuration);
        return factory;
    }

    @Bean
    public MapperFactoryBean<BookDao> bookDaoMapper(SqlSessionFactory factory) {
        MapperFactoryBean<BookDao> bean = new MapperFactoryBean<>(BookDao.class);
        bean.setSqlSessionFactory(factory);
        return bean;
    }

    @Bean
    public MapperFactoryBean<AppointmentDao> appointmentDaoMapperFactoryBean(SqlSessionFactory factory) {
        MapperFactoryBean<AppointmentDao> bean = new MapperFactoryBean<>(AppointmentDao.class);
        bean.setSqlSessionFactory(factory);
        return bean;
    }

}
