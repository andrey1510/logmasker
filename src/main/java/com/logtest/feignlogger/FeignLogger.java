package com.logtest.feignlogger;

import feign.Logger;
import feign.Request;
import feign.Response;
import feign.Util;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class FeignLogger extends Logger {

    private final FeignLoggerProperties feignLoggerProperties;
    private final String MASKED_BODY = "[Request/response body is masked]";

    @Override
    protected void log(String configKey, String format, Object... args) {
        if (log.isDebugEnabled()) {
            log.debug(String.format(methodTag(configKey) + format, args));
        }
    }

    @Override
    protected boolean shouldLogRequestHeader(String header) {
        return !feignLoggerProperties.getShouldNotLogRequestHeaders().contains(header);
    }

    @Override
    protected boolean shouldLogResponseHeader(String header) {
        return !feignLoggerProperties.getShouldNotLogResponseHeaders().contains(header);
    }

    // Variant with removal

//    @Override
//    protected void logRequest(String configKey, Level logLevel, Request request) {
//        String protocolVersion = resolveProtocolVersion(request.protocolVersion());
//        this.log(configKey, "---> %s %s %s", request.httpMethod().name(), request.url(), protocolVersion);
//        if (logLevel.ordinal() >= Logger.Level.HEADERS.ordinal()) {
//            for(String field : request.headers().keySet()) {
//                if (this.shouldLogRequestHeader(field)) {
//                    for(String value : Util.valuesOrEmpty(request.headers(), field)) {
//                        this.log(configKey, "%s: %s", field, value);
//                    }
//                }
//            }
//
//            int bodyLength = 0;
//            if (request.body() != null) {
//                bodyLength = request.length();
//            }
//
//            this.log(configKey, "---> END HTTP (%s-byte body)", bodyLength);
//        }
//    }
//
//    @Override
//    protected Response logAndRebufferResponse(String configKey, Level logLevel, Response response,
//                                              long elapsedTime) throws IOException {
//        String protocolVersion = resolveProtocolVersion(response.protocolVersion());
//        String reason = response.reason() != null && logLevel.compareTo(Logger.Level.NONE) > 0 ? " " + response.reason() : "";
//        int status = response.status();
//        this.log(configKey, "<--- %s %s%s (%sms)", protocolVersion, status, reason, elapsedTime);
//        if (logLevel.ordinal() >= Logger.Level.HEADERS.ordinal()) {
//            for(String field : response.headers().keySet()) {
//                if (this.shouldLogResponseHeader(field)) {
//                    for(String value : Util.valuesOrEmpty(response.headers(), field)) {
//                        this.log(configKey, "%s: %s", field, value);
//                    }
//                }
//            }
//
//            int bodyLength = 0;
//            if (response.body() != null && status != 204 && status != 205) {
//
//                byte[] bodyData = Util.toByteArray(response.body().asInputStream());
//                Util.ensureClosed(response.body());
//                bodyLength = bodyData.length;
//
//                this.log(configKey, "<--- END HTTP (%s-byte body)", bodyLength);
//                return response.toBuilder().body(bodyData).build();
//            }
//
//            this.log(configKey, "<--- END HTTP (%s-byte body)", bodyLength);
//        }
//
//        return response;
//    }

    // Variant with masking

    @Override
    protected void logRequest(String configKey, Level logLevel, Request request) {
        String protocolVersion = resolveProtocolVersion(request.protocolVersion());
        this.log(configKey, "---> %s %s %s", request.httpMethod().name(), request.url(), protocolVersion);
        if (logLevel.ordinal() >= Logger.Level.HEADERS.ordinal()) {
            for(String field : request.headers().keySet()) {
                if (this.shouldLogRequestHeader(field)) {
                    for(String value : Util.valuesOrEmpty(request.headers(), field)) {
                        this.log(configKey, "%s: %s", field, value);
                    }
                }
            }

            int bodyLength = 0;
            if (request.body() != null) {
                bodyLength = request.length();
                if (logLevel.ordinal() >= Logger.Level.FULL.ordinal()) {
                    String bodyText = request.charset() != null ? MASKED_BODY : null;
                    this.log(configKey, "");
                    this.log(configKey, "%s", bodyText != null ? bodyText : "Binary data");
                }
            }

            this.log(configKey, "---> END HTTP (%s-byte body)", bodyLength);
        }
    }

    @Override
    protected Response logAndRebufferResponse(String configKey, Level logLevel, Response response,
                                              long elapsedTime) throws IOException {
        String protocolVersion = resolveProtocolVersion(response.protocolVersion());
        String reason = response.reason() != null && logLevel.compareTo(Logger.Level.NONE) > 0 ? " " + response.reason() : "";
        int status = response.status();
        this.log(configKey, "<--- %s %s%s (%sms)", protocolVersion, status, reason, elapsedTime);
        if (logLevel.ordinal() >= Logger.Level.HEADERS.ordinal()) {
            for(String field : response.headers().keySet()) {
                if (this.shouldLogResponseHeader(field)) {
                    for(String value : Util.valuesOrEmpty(response.headers(), field)) {
                        this.log(configKey, "%s: %s", field, value);
                    }
                }
            }

            int bodyLength = 0;
            if (response.body() != null && status != 204 && status != 205) {
                if (logLevel.ordinal() >= Logger.Level.FULL.ordinal()) {
                    this.log(configKey, "");
                }

                byte[] bodyData = Util.toByteArray(response.body().asInputStream());
                Util.ensureClosed(response.body());
                bodyLength = bodyData.length;
                if (logLevel.ordinal() >= Logger.Level.FULL.ordinal() && bodyLength > 0) {
                    this.log(configKey, "%s", MASKED_BODY);
                }

                this.log(configKey, "<--- END HTTP (%s-byte body)", bodyLength);
                return response.toBuilder().body(bodyData).build();
            }

            this.log(configKey, "<--- END HTTP (%s-byte body)", bodyLength);
        }

        return response;
    }

}
