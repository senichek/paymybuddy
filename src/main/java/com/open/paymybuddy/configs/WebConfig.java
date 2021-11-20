package com.open.paymybuddy.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.open.paymybuddy.utils.SecurityUtil;
import com.open.paymybuddy.web.JacksonObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

@Configuration
public class WebConfig {
    
    @Autowired
    private Environment env;
    
    @Bean
    public DataSource dataSource() {
        DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder
        // Getting DB credentials from the application.prperties file.
        .url(env.getProperty("spring.datasource.url"))
        .username(env.getProperty("spring.datasource.username"))
        .password(env.getProperty("spring.datasource.password"));
        return dataSourceBuilder.build();
    }

    @Bean
    public DataSourceInitializer dataSourceInitializer (){
        ResourceDatabasePopulator resourceDatabasePopulator = new ResourceDatabasePopulator();
        resourceDatabasePopulator.addScript(new ClassPathResource("/initDB.sql"));
        DataSourceInitializer dataSourceInitializer = new DataSourceInitializer();
        dataSourceInitializer.setDataSource(dataSource());
        dataSourceInitializer.setDatabasePopulator(resourceDatabasePopulator);
        return dataSourceInitializer;
    }

    @Bean
    public ObjectMapper jacksonObjectMapper() {
        return JacksonObjectMapper.getMapper();
    }

    @Bean
    public SecurityUtil securityUtil() {
        return new SecurityUtil();
    }
}
