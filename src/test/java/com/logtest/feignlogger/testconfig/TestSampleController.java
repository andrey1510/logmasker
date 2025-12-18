package com.logtest.feignlogger.testconfig;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/test")
public class TestSampleController {

    private final TestFeignClient testFeignClient;

    @PostMapping("/test-feign")
    public ResponseEntity<TestResponseDto> testFeignClient(
        @RequestBody TestRequestDto request,
        @RequestHeader(value = "Authorization", required = false) String authHeader
    ) {
        TestResponseDto response = testFeignClient.get(request, authHeader);
        return ResponseEntity.ok(response);
    }
}
