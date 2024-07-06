package com.clouify.stockservice.configuration;

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.util.List;

public class DataSourceConfig {

    private final DiscoveryClient discoveryClient;

    private final Environment environment;

    public DataSourceConfig(final DiscoveryClient discoveryClient, final Environment environment) {
        this.discoveryClient = discoveryClient;
        this.environment = environment;
    }

    public DataSource dataSource() {
        final List<ServiceInstance> instances = discoveryClient.getInstances("postgres");
        if (instances.isEmpty()) {
            throw new IllegalStateException("No postgres instances found");
        }

        final ServiceInstance instance = instances.getFirst();
        final String url = String.format("jdbc:postgresql://%s:%d/%s",
                instance.getHost(),
                instance.getPort(),
                "stockdb");

        final DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setUrl(url);
        dataSource.setUsername(environment.getProperty("spring.datasource.username"));
        dataSource.setPassword(environment.getProperty("spring.datasource.password"));

        return dataSource;
    }
}
