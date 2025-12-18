package com.logtest.feignlogger.testconfig;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/test-external-service")
public class TestSampleExternalServiceController {

    @PostMapping("/get")
    public TestResponseDto mockExternalService(
        @RequestBody TestRequestDto request,
        @RequestHeader(value = "Authorization", required = false) String authHeader
    ) {
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        return TestResponseDto.builder()
            .textField("Ответ от mock сервиса на запрос: " + request.getTextField())
            .build();
    }
}
