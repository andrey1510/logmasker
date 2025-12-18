package com.logtest.feignlogger;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import com.logtest.feignlogger.testconfig.TestRequestDto;
import com.logtest.feignlogger.testconfig.TestResponseDto;
import com.logtest.feignlogger.testconfig.TestFeignClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.slf4j.LoggerFactory;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class FeignLoggerIntegrationTestMock {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private TestFeignClient testFeignClient;

    private ListAppender<ILoggingEvent> listAppender;

    @BeforeEach
    void setUp() {
        // Настраиваем mock
        TestResponseDto mockResponse = new TestResponseDto();
        mockResponse.setTextField("Mock response");

        Mockito.when(testFeignClient.get(any(TestRequestDto.class), eq("Bearer test-token")))
            .thenReturn(mockResponse);

        // Настраиваем логгер
        Logger logger = (Logger) LoggerFactory.getLogger(FeignLogger.class);
        listAppender = new ListAppender<>();
        listAppender.start();
        logger.addAppender(listAppender);
        logger.setLevel(ch.qos.logback.classic.Level.DEBUG);
    }

    @Test
    void testFeignLogger() throws Exception {
        mockMvc.perform(post("/api/test/test-feign")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"textField\":\"test\"}")
                .header("Authorization", "Bearer test-token"))
            .andExpect(status().isOk());

        Thread.sleep(100);

        // Проверяем логи
        boolean hasMaskedBody = false;
        for (ILoggingEvent event : listAppender.list) {
            if (event.getFormattedMessage().contains("[Request/response body is masked]")) {
                hasMaskedBody = true;
                break;
            }
        }

        assert hasMaskedBody : "Should contain masked body";
    }
}
