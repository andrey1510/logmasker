package com.logtest.masker.utils;

import com.logtest.masker.patterns.MaskPatternType;
import com.logtest.masker.patterns.MaskPatterns;
import com.logtest.masker.patterns.MaskPatternsAlt;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.temporal.Temporal;

public class RegularTypesProcessor {

    public static Temporal processTemporalValue(MaskPatternType type, Object value) {
        return switch (type) {
            case LOCAL_DATE -> value instanceof LocalDate date ?
                MaskPatternsAlt.maskLocalDate(date) : (Temporal) value;
            case OFFSET_DATE_TIME -> value instanceof OffsetDateTime dateTime ?
                MaskPatternsAlt.maskOffsetDateTime(dateTime) : (Temporal) value;
            default -> (Temporal) value;
        };
    }

    public static String processStringValue(MaskPatternType type, String value) {
        return switch (type) {
            case EMAIL -> MaskPatterns.maskEmail(value);
            case INN -> MaskPatterns.maskInn(value);
            case KPP -> MaskPatterns.maskKpp(value);
            case OKPO -> MaskPatterns.maskOkpo(value);
            case OGRNUL_OR_OGRNIP -> MaskPatterns.maskOgrnUlOrOgrnIp(value);
            case TEXT_FIELD_ALT -> MaskPatternsAlt.maskTextField(value);
            case FULL_NAME_ALT -> MaskPatternsAlt.maskFullName(value);
            case FULL_ADDRESS_ALT -> MaskPatternsAlt.maskFullAddress(value);
            case EMAIL_ALT -> MaskPatternsAlt.maskEmail(value);
            case SURNAME_ALT -> MaskPatternsAlt.maskSurname(value);
            case AUTH_DATA_ALT -> MaskPatternsAlt.maskAuthData(value);
            case PASSPORT_SERIES_AND_NUMBER_ALT -> MaskPatternsAlt.maskPassportSeriesAndNumber(value);
            case JWT_TYK_API_KEY_IP_ADDRESS -> MaskPatterns.maskJwtTykApiKeyIpAddress(value);
            case SNILS -> MaskPatterns.maskSnils(value);
            case PHONE -> MaskPatterns.maskPhoneNumber(value);
            default -> value;
        };
    }
}