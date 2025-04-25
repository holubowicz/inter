package com.example.backend.database.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;

@Configuration
@EnableJpaRepositories(
        basePackages = "com.example.backend.database",
        entityManagerFactoryRef = "internalEntityManagerFactory",
        transactionManagerRef = "internalTransactionManager"
)
public class InternalDatabase {

    private final InternalDatabaseConfiguration internalDatabaseConfiguration;

    @Autowired
    public InternalDatabase(InternalDatabaseConfiguration internalDatabaseConfiguration) {
        this.internalDatabaseConfiguration = internalDatabaseConfiguration;
    }

    @Bean
    @ConfigurationProperties("spring.datasource.internal")
    public DataSourceProperties internalDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean(name = "internalDataSource")
    public DataSource internalDataSource() {
        return internalDataSourceProperties()
                .initializeDataSourceBuilder()
                .build();
    }


    @Bean
    public LocalContainerEntityManagerFactoryBean internalEntityManagerFactory() {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(internalDataSource());
        em.setPackagesToScan("com.example.backend.database.schema");

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);

        HashMap<String, Object> properties = new HashMap<>();
        properties.put("hibernate.hbm2ddl.auto", "update");
        properties.put("javax.persistence.schema-generation.create-source", "script");
        properties.put(
                "javax.persistence.schema-generation.create-script-source",
                internalDatabaseConfiguration.getInitScriptPath());
        em.setJpaPropertyMap(properties);

        return em;
    }

    @Bean
    public PlatformTransactionManager internalTransactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(internalEntityManagerFactory().getObject());
        return transactionManager;
    }

}

