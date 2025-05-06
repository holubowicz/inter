package com.example.backend.database;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "app.db.internal")
@Data
public class InternalDatabaseConfiguration {

    private String initScriptPath;

}
