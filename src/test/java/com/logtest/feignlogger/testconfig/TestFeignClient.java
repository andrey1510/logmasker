package com.logtest.feignlogger.testconfig;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

public interface TestFeignClient {
    String AUTHORIZATION_HEADER = "Authorization";

    @PostMapping(value = "/test-external-service/get", produces = MediaType.APPLICATION_JSON_VALUE)
    TestResponseDto get(
        @RequestBody TestRequestDto request,
        @RequestHeader(name = AUTHORIZATION_HEADER, required = false) String token
    );
}
