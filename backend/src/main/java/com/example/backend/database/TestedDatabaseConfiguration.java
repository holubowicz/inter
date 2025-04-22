package com.example.backend.database;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
public class TestedDatabaseConfiguration {

    @Bean
    @ConfigurationProperties("spring.datasource.tested")
    public DataSourceProperties testedDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean(name = "testedDataSource")
    public DataSource testedDataSource() {
        DataSourceProperties dataSourceProperties = testedDataSourceProperties();

        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setUrl(dataSourceProperties.getUrl());
        dataSource.setUsername(dataSourceProperties.getUsername());
        dataSource.setPassword(dataSourceProperties.getPassword());

        return dataSource;
    }

    @Bean(name = "testedJdbcTemplate")
    public JdbcTemplate testedJdbcTemplate(@Qualifier("testedDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

}