package com.logtest.feignlogger;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import com.logtest.feignlogger.testconfig.TestConfig;
import com.logtest.feignlogger.testconfig.TestRequestDto;
import com.logtest.feignlogger.testconfig.TestResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    classes = TestConfig.class
)
@AutoConfigureMockMvc
@TestPropertySource(properties = {
    "logging.level.com.logtest.feignlogger=DEBUG",
    "spring.cloud.openfeign.client.config.default.logger-level=FULL",
    "service-name.feign-logger.should-not-log-request-headers=Authorization,jwt",
    "service-name.feign-logger.should-not-log-response-headers=jwt"
})
class FeignLoggerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @LocalServerPort
    private int port;

    private ListAppender<ILoggingEvent> listAppender;

    @BeforeEach
    void setUp() {
        // Получаем логгер FeignLogger и настраиваем appender для перехвата логов
        Logger logger = (Logger) LoggerFactory.getLogger(FeignLogger.class);
        listAppender = new ListAppender<>();
        listAppender.start();
        logger.addAppender(listAppender);
        logger.setLevel(ch.qos.logback.classic.Level.DEBUG);

        // Также настраиваем корневой логгер для перехвата всех логов
        Logger rootLogger = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        rootLogger.setLevel(ch.qos.logback.classic.Level.DEBUG);
    }

    @Test
    void testFeignLoggerMasksRequestBody() throws Exception {
        // Выполнение запроса
        MvcResult result = mockMvc.perform(post("/api/test/test-feign")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"textField\":\"test request\"}")
                .header("Authorization", "Bearer test-token"))
            .andExpect(status().isOk())
            .andReturn();

        // Даем время для асинхронной записи логов
        Thread.sleep(100);

        // Получаем все логи
        String logs = getCapturedLogs();

        // Отладочный вывод для проверки
        System.out.println("=== Captured Logs ===");
        System.out.println(logs);
        System.out.println("====================");

        // Проверяем, что тело запроса маскируется
        assertThat(logs).contains("[Request/response body is masked]");

        // Проверяем, что заголовок Authorization не логируется
        assertThat(logs).doesNotContain("Bearer test-token");

        // Проверяем, что другие заголовки логируются
        assertThat(logs).contains("Content-Type: application/json");
    }

    @Test
    void testFeignLoggerLogsRequestAndResponse() throws Exception {
        // Очищаем предыдущие логи
        listAppender.list.clear();

        // Выполнение запроса без Authorization заголовка
        mockMvc.perform(post("/api/test/test-feign")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"textField\":\"another test\"}"))
            .andExpect(status().isOk());

        // Даем время для асинхронной записи логов
        Thread.sleep(100);

        String logs = getCapturedLogs();

        // Проверяем основные элементы логов
        assertThat(logs).contains("---> POST");
        assertThat(logs).contains("---> END HTTP");
        assertThat(logs).contains("<--- HTTP/1.1");
        assertThat(logs).contains("<--- END HTTP");
        assertThat(logs).contains("[Request/response body is masked]");
    }

    private String getCapturedLogs() {
        StringBuilder sb = new StringBuilder();
        for (ILoggingEvent event : listAppender.list) {
            sb.append(event.getFormattedMessage()).append("\n");
        }
        return sb.toString();
    }
}