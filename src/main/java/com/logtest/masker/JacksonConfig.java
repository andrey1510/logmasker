package com.logtest.masker;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
@RequiredArgsConstructor
public class JacksonConfig {

    private final MaskingFilterProvider maskingFilterProvider;

    @Bean
    @Primary
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setFilterProvider(maskingFilterProvider);
        return mapper;
    }
}