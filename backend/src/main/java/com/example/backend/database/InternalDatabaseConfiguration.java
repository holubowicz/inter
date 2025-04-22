//package com.example.backend.database;
//
//import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
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
//        return internalDataSourceProperties()
//                .initializeDataSourceBuilder()
//                .build();
//    }
//
//}
//
