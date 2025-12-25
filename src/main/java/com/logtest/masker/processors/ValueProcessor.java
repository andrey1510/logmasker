package com.logtest.masker.processors;

import com.logtest.masker.patterns.MaskPatternType;
import com.logtest.masker.patterns.MaskPatterns;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.temporal.Temporal;

public class ValueProcessor {

    public static Object processValue(MaskPatternType type, Object value) {
        return switch (type) {
            case LOCAL_DATE -> value instanceof LocalDate date ?
                MaskPatterns.maskLocalDate(date) : (Temporal) value;
            case OFFSET_DATE_TIME -> value instanceof OffsetDateTime dateTime ?
                MaskPatterns.maskOffsetDateTime(dateTime) : (Temporal) value;
            case BALANCE -> MaskPatterns.maskBalance((String) value);
            case CONFIDENTIAL_NUMBER -> MaskPatterns.maskConfidentialNumber((String) value);
            case EMAIL -> MaskPatterns.maskEmail((String) value);
            case FULL_ADDRESS -> MaskPatterns.maskFullAddress((String) value);
            case INN -> MaskPatterns.maskInn((String) value);
            case ISSUER_NAME -> MaskPatterns.maskIssuerName((String) value);
            case ISSUER_CODE -> MaskPatterns.maskIssuerCode((String) value);
            case JWT_TYK_API_KEY_IP_ADDRESS -> MaskPatterns.maskJwtTykApiKeyIpAddress((String) value);
            case KPP -> MaskPatterns.maskKpp((String) value);
            case NAME -> MaskPatterns.maskName((String) value);
            case OGRNUL_OR_OGRNIP -> MaskPatterns.maskOgrnUlOrOgrnIp((String) value);
            case OKPO -> MaskPatterns.maskOkpo((String) value);
            case OTHER_DUL_NUMBER -> MaskPatterns.maskOtherDulNumber((String) value);
            case OTHER_DUL_SERIES -> MaskPatterns.maskOtherDulSeries((String) value);
            case SNILS -> MaskPatterns.maskSnils((String) value);
            case PASSPORT -> MaskPatterns.maskPassport((String) value);
            case PASSPORT_NUMBER -> MaskPatterns.maskPassportNumber((String) value);
            case PASSPORT_SERIES -> MaskPatterns.maskPassportSeries((String) value);
            case PAN -> MaskPatterns.maskPan((String) value);
            case PIN -> MaskPatterns.maskPin((String) value);
            case PHONE -> MaskPatterns.maskPhoneNumber((String) value);
            case TEXT_FIELD -> MaskPatterns.maskTextField((String) value);
            default -> value;
        };
    }
}