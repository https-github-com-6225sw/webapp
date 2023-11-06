package com.csye6225.cloudwebapp.config;

import com.timgroup.statsd.NonBlockingStatsDClient;
import com.timgroup.statsd.StatsDClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MetricsConfig {
    @Bean
    public StatsDClient statsDClient() {
        return new NonBlockingStatsDClient("webapp.production", "localhost", 8125);
    }
}
