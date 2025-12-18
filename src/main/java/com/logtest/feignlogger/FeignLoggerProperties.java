package com.logtest.feignlogger;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "service-name.feign-logger")
public class FeignLoggerProperties {

    private Set<String> shouldNotLogRequestHeaders = new HashSet<>();

    private Set<String> shouldNotLogResponseHeaders = new HashSet<>();
}
