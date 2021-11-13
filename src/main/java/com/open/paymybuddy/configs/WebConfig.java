package com.open.paymybuddy.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.open.paymybuddy.web.JacksonObjectMapper;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

@Configuration
public class WebConfig {
    // TODO move credentials to a file.
    @Bean
    public DataSource dataSource() {
        DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder
        .url("jdbc:mysql://localhost:3306/paymybuddy")
        .username("root")
        .password("rootroot");
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
}
