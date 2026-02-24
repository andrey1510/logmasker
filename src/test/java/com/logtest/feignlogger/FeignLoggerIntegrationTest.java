package com.logtest.feignlogger;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.logtest.feignlogger.testData.RequestDto;
import com.logtest.feignlogger.testData.ResponseDto;
import com.logtest.feignlogger.testData.TestFeignClient;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.equalToJson;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles("test")
@ExtendWith(OutputCaptureExtension.class)
class FeignLoggerIntegrationTest {

    private static WireMockServer wireMockServer;

    @Autowired
    private TestFeignClient testFeignClient;

    private static final String RESPONSE_BODY = """
        {
            "textField": "response data"
        }
        """;

    private static final String REQUEST_BODY = """
        {
            "textField": "request data"
        }
        """;


    @BeforeAll
    static void startWireMock() {
        wireMockServer = new WireMockServer(WireMockConfiguration.options().dynamicPort());
        wireMockServer.start();
        WireMock.configureFor("localhost", wireMockServer.port());
    }

    @AfterAll
    static void stopWireMock() {
        if (wireMockServer != null) wireMockServer.stop();
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("external-mock-service.url",
            () -> "http://localhost:" + wireMockServer.port());
    }

    @BeforeEach
    void setUp() {
        WireMock.reset();
        wireMockServer.resetAll();
    }

    @Test
    @SneakyThrows
    void testPost_maskAccountNumber(CapturedOutput output) {

        String externalPostEndpoint = "/external-post-endpoint";
        String accountNumber = "1234567890";
        String accountNumberMasked = "**********";
        String status = "active";

        stubFor(post(urlPathEqualTo(externalPostEndpoint))
            .withQueryParam("accountNumber", equalTo(accountNumber))
            .withQueryParam("status", equalTo(status))
            .withHeader("Content-Type", equalTo("application/json"))
            .withRequestBody(equalToJson(REQUEST_BODY))
            .willReturn(aResponse()
                .withStatus(HttpStatus.OK.value())
                .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .withBody(RESPONSE_BODY)));

        ResponseDto response = testFeignClient.postExternal(
            accountNumber,
            status,
            "Bearer test-token-123",
            "test jwt",
            new RequestDto("request data")
        );

        verify(postRequestedFor(urlPathEqualTo(externalPostEndpoint))
            .withQueryParam("accountNumber", equalTo(accountNumber))
            .withQueryParam("status", equalTo(status)));

        assertNotNull(response);
        assertEquals("response data", response.getTextField());

        String logs = output.getAll();
        assertTrue(logs.contains(String.format("accountNumber=%s", accountNumberMasked)));
        assertFalse(logs.contains(String.format("accountNumber=%s", accountNumber)));
        assertTrue(logs.contains(String.format("status=%s", status)));
    }

    @Test
    @SneakyThrows
    void testPost_noBodyInLogsWhileHeadersLevel(CapturedOutput output) {

        String externalPostEndpoint = "/external-post-endpoint";
        String accountNumber = "1234567890";
        String status = "active";

        stubFor(post(urlPathEqualTo(externalPostEndpoint))
            .withQueryParam("accountNumber", equalTo(accountNumber))
            .withQueryParam("status", equalTo(status))
            .withHeader("Content-Type", equalTo("application/json"))
            .withRequestBody(equalToJson(REQUEST_BODY))
            .willReturn(aResponse()
                .withStatus(HttpStatus.OK.value())
                .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .withBody(RESPONSE_BODY)));

        ResponseDto response = testFeignClient.postExternal(
            accountNumber,
            status,
            "Bearer test-token-123",
            "test jwt",
            new RequestDto("request data")
        );

        verify(postRequestedFor(urlPathEqualTo(externalPostEndpoint))
            .withQueryParam("accountNumber", equalTo(accountNumber))
            .withQueryParam("status", equalTo(status)));

        assertNotNull(response);
        assertEquals("response data", response.getTextField());

        String logs = output.getAll();
        assertFalse(logs.contains("""
            {"textField":"request data"}
            """));
        assertFalse(logs.contains("""
            "textField": "response data"
        """));
    }

    @Test
    @SneakyThrows
    void testPost_noAuthorizationAndJwtInLogs(CapturedOutput output) {

        String externalPostEndpoint = "/external-post-endpoint";
        String accountNumber = "1234567890";
        String status = "active";

        stubFor(post(urlPathEqualTo(externalPostEndpoint))
            .withQueryParam("accountNumber", equalTo(accountNumber))
            .withQueryParam("status", equalTo(status))
            .withHeader("Content-Type", equalTo("application/json"))
            .withHeader("Authorization", equalTo("Bearer test-token-123"))
            .withHeader("jwt", equalTo("test jwt"))
            .withRequestBody(equalToJson(REQUEST_BODY))
            .willReturn(aResponse()
                .withStatus(HttpStatus.OK.value())
                .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .withHeader("Authorization", "Bearer test-token-123")
                .withHeader("jwt", "test jwt")
                .withBody(RESPONSE_BODY)));

        ResponseDto response = testFeignClient.postExternal(
            accountNumber,
            status,
            "Bearer test-token-123",
            "test jwt",
            new RequestDto("request data")
        );

        verify(postRequestedFor(urlPathEqualTo(externalPostEndpoint))
            .withQueryParam("accountNumber", equalTo(accountNumber))
            .withQueryParam("status", equalTo(status))
            .withHeader("Authorization", equalTo("Bearer test-token-123"))
            .withHeader("jwt", equalTo("test jwt")));

        assertNotNull(response);
        assertEquals("response data", response.getTextField());

        String logs = output.getAll();
        assertTrue(logs.contains("Authorization: ***") || logs.contains("authorization: ***"));
        assertFalse(logs.contains("Bearer test-token-123"));
        assertTrue(logs.contains("jwt: ***"));
        assertFalse(logs.contains("test jwt"));

    }

}