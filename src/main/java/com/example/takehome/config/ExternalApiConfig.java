package com.example.takehome.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Configuration
@ConfigurationProperties(prefix = "external-api")
@Data
public class ExternalApiConfig {

    private String url;
}
