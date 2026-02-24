package com.logtest.feignlogger;

import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.LoggerFactory;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import feign.Request;
import org.springframework.test.util.ReflectionTestUtils;

import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static feign.Logger.Level.HEADERS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FeignLoggerUnitTests {

    private FeignLogger feignLogger;

    @Mock
    private FeignLoggerProperties feignLoggerProperties;

    @Mock
    private Appender<ILoggingEvent> mockAppender;

    @BeforeEach
    void setUp() {
        feignLogger = new FeignLogger(feignLoggerProperties);
    }

    @Test
    @SneakyThrows
    void maskUrl_testAccountNumber() {

        String urlWithAccountNumber = "http://test.com/api?accountNumber=123456&other=param";
        String maskedUrlWithAccountNumber = "http://test.com/api?accountNumber=******&other=param";
        String urlWithoutAccountNumber = "http://test.com/api?other=param";

        java.lang.reflect.Method method;
        method = FeignLogger.class.getDeclaredMethod("maskUrl", String.class);
        method.setAccessible(true);

        assertEquals(maskedUrlWithAccountNumber, method.invoke(feignLogger, urlWithAccountNumber));
        assertEquals(urlWithoutAccountNumber, method.invoke(feignLogger, urlWithoutAccountNumber));

    }

    @Test
    void maskUrl_test() {
        String url = "http://test.com/accounts?accountNumber=1234567890&status=active";
        String expected = "http://test.com/accounts?accountNumber=**********&status=active";

        assertEquals(expected, ReflectionTestUtils.invokeMethod(feignLogger, "maskUrl", url));
    }

    @Test
    void maskUrl_testNoAccountNumber() {
        String url = "http://test.com/api/users";

        assertEquals(url, ReflectionTestUtils.invokeMethod(feignLogger, "maskUrl", url));
    }


    @Test
    void maskUrl_testMultipleAccountNumbers() {
        String url = "http://example.com/accounts?accountNumber=12345&status=active&accountNumber=67890";
        String expected = "http://example.com/accounts?accountNumber=*****&status=active&accountNumber=*****";

        assertEquals(expected, ReflectionTestUtils.<String>invokeMethod(feignLogger, "maskUrl", url));
    }

    @Test
    void maskUrl_testAccountNumberWrongCase() {
        String url = "http://example.com/accounts?ACCOUNTNUMBER=123456&status=active";
        String expected = "http://example.com/accounts?ACCOUNTNUMBER=******&status=active";

        assertEquals(expected, ReflectionTestUtils.invokeMethod(feignLogger, "maskUrl", url));
    }

    @Test
    void maskUrl_testNullUrl() {
        assertNull(ReflectionTestUtils.invokeMethod(feignLogger, "maskUrl", (String) null));
    }

    @Test
    void maskUrl_testEmptyUrl() {
        assertEquals("", ReflectionTestUtils.<String>invokeMethod(feignLogger, "maskUrl", ""));
    }

    @Test
    void logRequest_testExcludedHeaders() {

        Logger rootLogger = (Logger) LoggerFactory.getLogger(FeignLogger.class);
        rootLogger.addAppender(mockAppender);
        rootLogger.setLevel(Level.DEBUG);

        String configKey = "TestClient#test()";
        Map<String, Collection<String>> headers = new HashMap<>();
        headers.put("Authorization", List.of("Bearer token123"));
        headers.put("Content-Type", List.of("application/json"));
        headers.put("X-Secret-Header", List.of("secret-value"));
        headers.put("Accept", List.of("application/json"));

        Request request = Request.create(Request.HttpMethod.POST,"http://example.com/api", headers,
            "test body".getBytes(), StandardCharsets.UTF_8, null);

        feignLogger.logRequest(configKey, HEADERS, request);

        verify(mockAppender, atLeastOnce()).doAppend(argThat(argument -> {
            String message = argument.getMessage();
            return !message.contains("Authorization: Bearer token123") &&
                !message.contains("X-Secret-Header: secret-value") &&
                message.contains("Content-Type: application/json");
        }));
    }

}
