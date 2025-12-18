package com.logtest.feignlogger.testconfig;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;

@SpringBootConfiguration
@EnableAutoConfiguration
@EnableFeignClients(basePackages = "com.logtest.feignlogger.testconfig")
@ComponentScan(basePackages = {
    "com.logtest.feignlogger",
    "com.logtest.feignlogger.testconfig"
})
@EnableConfigurationProperties
@PropertySource("classpath:application-test.yml")
public class TestConfig {
}