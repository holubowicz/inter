package com.example.backend.database;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
public class TestedDatabase {

    @Bean
    @ConfigurationProperties("spring.datasource.tested")
    public DataSourceProperties testedDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean(name = "testedDataSource")
    public DataSource testedDataSource() {
        return testedDataSourceProperties()
                .initializeDataSourceBuilder()
                .build();
    }

    @Bean(name = "testedJdbcTemplate")
    public JdbcTemplate testedJdbcTemplate(@Qualifier("testedDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

}