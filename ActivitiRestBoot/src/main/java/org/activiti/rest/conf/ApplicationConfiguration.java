package org.activiti.rest.conf;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

@Configuration
@PropertySources({
  @PropertySource(value = "classpath:db.properties", ignoreResourceNotFound = true),
  @PropertySource(value = "classpath:engine.properties", ignoreResourceNotFound = true)
})
@ComponentScan(basePackages = {
        "org.activiti.rest.conf","org.activiti.rest.exception", "org.activiti.rest.service.api"})
@EnableAutoConfiguration
public class ApplicationConfiguration {

    private static Logger logger = LoggerFactory
            .getLogger(ApplicationConfiguration.class);

    public static void main(String[] args) {
        logger.debug("starting app...");
        SpringApplication.run(new Object[]{ApplicationConfiguration.class}, args);
        logger.info("application started.");
    }
}
