//package com.example.backend.database;
//
//import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.jdbc.datasource.DriverManagerDataSource;
//
//import javax.sql.DataSource;
//
//@Configuration
//public class InternalDatabaseConfiguration {
//
//    @Bean
//    @ConfigurationProperties("spring.datasource.internal")
//    public DataSourceProperties internalDataSourceProperties() {
//        return new DataSourceProperties();
//    }
//
//    @Bean(name = "internalDataSource")
//    public DataSource internalDataSource() {
//        DataSourceProperties dataSourceProperties = internalDataSourceProperties();
//
//        DriverManagerDataSource dataSource = new DriverManagerDataSource();
//        dataSource.setUrl(dataSourceProperties.getUrl());
//        dataSource.setUsername(dataSourceProperties.getUsername());
//        dataSource.setPassword(dataSourceProperties.getPassword());
//
//        return dataSource;
//    }
//
//}
//
