package com.logtest.masker.patterns.dep1;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Optional;

public class DepOneProcessor {

    public enum PatternType {
        TEXT_FIELD,
        FULL_NAME,
        EMAIL,
        LOCAL_DATE,
        OFFSET_DATE_TIME,
        FULL_ADDRESS,
        SURNAME,
        AUTH_DATA,
        PASSPORT_SERIES_AND_NUMBER,
    }

    public static String processString(PatternType type, String value) {
        return Optional.ofNullable(type)
            .map(annotation -> switch (type) {
                case TEXT_FIELD -> MaskPatterns.maskTextField(value);
                case FULL_NAME -> MaskPatterns.maskFullName(value);
                case FULL_ADDRESS -> MaskPatterns.maskFullAddress(value);
                case EMAIL -> MaskPatterns.maskEmail(value);
                case SURNAME -> MaskPatterns.maskSurname(value);
                case AUTH_DATA -> MaskPatterns.maskAuthData(value);
                case PASSPORT_SERIES_AND_NUMBER -> MaskPatterns.maskPassportSeriesAndNumber(value);
                default -> value;
            })
            .orElse(value);
    }

    public static Object processTemporalValue(PatternType type, Object value) {
        return Optional.ofNullable(type)
            .map(annotation -> switch (type) {
                case LOCAL_DATE -> value instanceof LocalDate date ?
                    MaskPatterns.maskLocalDate(date) : value;
                case OFFSET_DATE_TIME -> value instanceof OffsetDateTime dateTime ?
                    MaskPatterns.maskOffsetDateTime(dateTime) : value;
                default -> value;
            })
            .orElse(value);
    }

}
