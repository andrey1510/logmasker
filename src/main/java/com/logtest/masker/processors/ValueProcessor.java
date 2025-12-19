package com.logtest.masker.processors;

import com.logtest.masker.patterns.MaskPatternType;
import com.logtest.masker.patterns.MaskPatterns;
import com.logtest.masker.patterns.MaskPatternsAlt;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.temporal.Temporal;

public class ValueProcessor {

    public static Temporal processTemporalValue(MaskPatternType type, Object value) {
        return switch (type) {
            case LOCAL_DATE -> value instanceof LocalDate date ?
                MaskPatterns.maskLocalDate(date) : (Temporal) value;
            case OFFSET_DATE_TIME -> value instanceof OffsetDateTime dateTime ?
                MaskPatterns.maskOffsetDateTime(dateTime) : (Temporal) value;
            default -> (Temporal) value;
        };
    }

    public static String processStringValue(MaskPatternType type, String value) {
        return switch (type) {
            case AUTH_DATA_ALT -> MaskPatternsAlt.maskAuthData(value);
            case BALANCE -> MaskPatterns.maskBalance(value);
            case CONFIDENTIAL_NUMBER -> MaskPatterns.maskConfidentialNumber(value);
            case EMAIL -> MaskPatterns.maskEmail(value);
            case EMAIL_ALT -> MaskPatternsAlt.maskEmail(value);
            case FULL_ADDRESS_ALT -> MaskPatternsAlt.maskFullAddress(value);
            case FULL_NAME_ALT -> MaskPatternsAlt.maskFullName(value);
            case INN -> MaskPatterns.maskInn(value);
            case ISSUER_NAME -> MaskPatterns.maskIssuerName(value);
            case ISSUER_CODE -> MaskPatterns.maskIssuerCode(value);
            case JWT_TYK_API_KEY_IP_ADDRESS -> MaskPatterns.maskJwtTykApiKeyIpAddress(value);
            case KPP -> MaskPatterns.maskKpp(value);
            case NAME -> MaskPatterns.maskName(value);
            case OGRNUL_OR_OGRNIP -> MaskPatterns.maskOgrnUlOrOgrnIp(value);
            case OKPO -> MaskPatterns.maskOkpo(value);
            case OTHER_DUL_NUMBER -> MaskPatterns.maskOtherDulNumber(value);
            case OTHER_DUL_SERIES -> MaskPatterns.maskOtherDulSeries(value);
            case SNILS -> MaskPatterns.maskSnils(value);
            case SURNAME_ALT -> MaskPatternsAlt.maskSurname(value);
            case PASSPORT -> MaskPatterns.maskPassport(value);
            case PASSPORT_NUMBER -> MaskPatterns.maskPassportNumber(value);
            case PASSPORT_SERIES -> MaskPatterns.maskPassportSeries(value);
            case PASSPORT_SERIES_AND_NUMBER_ALT -> MaskPatternsAlt.maskPassportSeriesAndNumber(value);
            case PAN -> MaskPatterns.maskPan(value);
            case PIN -> MaskPatterns.maskPin(value);
            case PHONE -> MaskPatterns.maskPhoneNumber(value);
            case TEXT_FIELD -> MaskPatterns.maskTextField(value);
            case TEXT_FIELD_ALT -> MaskPatternsAlt.maskTextField(value);
            default -> value;
        };
    }
}