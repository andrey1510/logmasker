package com.logtest.feignlogger.sample;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/external-service")
public class SampleExternalServiceController {

    @PostMapping("/get")
    public ResponseDto mockExternalService(
        @RequestBody RequestDto request,
        @RequestHeader(value = "Authorization", required = false) String authHeader
    ) {

        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        return ResponseDto.builder()
            .textField("Ответ от mock сервиса на запрос: " + request.getTextField())
            .build();
    }

    @GetMapping("/accounts")
    public ResponseDto mockGetAccounts(
        @RequestParam String accountNumber,
        @RequestParam String status,
        @RequestHeader(value = "Authorization", required = false) String authHeader
    ) {
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        return ResponseDto.builder()
            .textField(String.format("Accounts for number: %s, status: %s", accountNumber, status))
            .build();
    }
}
