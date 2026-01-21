package com.logtest.feignlogger.sample;

import com.logtest.feignlogger.FeignClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class SampleController {

    private final FeignClient feignClient;

    @PostMapping("/test-feign")
    public ResponseEntity<ResponseDto> testFeignClient(
        @RequestBody RequestDto request,
        @RequestHeader(value = "Authorization", required = false) String authHeader
    ) {
        ResponseDto response = feignClient.get(request, authHeader);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/accounts")
    public ResponseEntity<ResponseDto> getAccounts(
        @RequestParam(name = "accountNumber") String accountNumber,
        @RequestParam(name = "status") String status,
        @RequestHeader(value = "Authorization", required = false) String authHeader
    ) {
        log.info("Getting accounts. AccountNumber: {}, Status: {}", accountNumber, status);

        ResponseDto response = feignClient.getAccounts(accountNumber, status, authHeader);
        return ResponseEntity.ok(response);
    }

}
