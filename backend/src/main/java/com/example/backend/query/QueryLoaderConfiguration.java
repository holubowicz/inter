package com.example.backend.query;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "app.loader")
@Data
public class QueryLoaderConfiguration {

    private String queriesPath;

}
