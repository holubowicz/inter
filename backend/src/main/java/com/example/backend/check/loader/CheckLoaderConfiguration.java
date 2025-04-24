package com.example.backend.check.loader;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

// TODO: when testing make it easier to setup, and more dynamic

@Configuration
@ConfigurationProperties(prefix = "app.loader")
@Data
public class CheckLoaderConfiguration {

    private String checksPath;

}
