package com.logtest.masker.patterns.dep2;

import com.logtest.masker.patterns.dep1.MaskPatterns;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Optional;

public class DepTwoProcessor {

    public enum PatternType {
        TEXT_FIELD,
        NAME,
        EMAIL,
        PHONE,
        CONFIDENTIAL_NUMBER,
        PIN,
        PAN,
        BALANCE,
        PASSPORT_SERIES,
        PASSPORT_NUMBER,
        PASSPORT,
        ISSUER_CODE,
        ISSUER_NAME,
        OTHER_DUL_SERIES,
        OTHER_DUL_NUMBER,
        LOCAL_DATE,
        OFFSET_DATE_TIME,
    }

    public static String processString(PatternType type, String value) {
        return Optional.ofNullable(type)
            .map(annotation -> switch (type) {
                case TEXT_FIELD -> MaskUtils.maskedTextField(value);
                case NAME -> MaskUtils.maskedName(value);
                case EMAIL -> MaskUtils.maskedEmail(value);
                case PHONE -> MaskUtils.maskedPhoneNumber(value);
                case CONFIDENTIAL_NUMBER -> MaskUtils.maskedConfidentialNumber(value);
                case PIN -> MaskUtils.maskedPIN(value);
                case PAN -> MaskUtils.maskedPAN(value);
                case BALANCE -> MaskUtils.maskedBalance(value);
                case PASSPORT_SERIES -> MaskUtils.maskedPassportSeries(value);
                case PASSPORT_NUMBER -> MaskUtils.maskedPassportNumber(value);
                case PASSPORT -> MaskUtils.maskedPassport(value);
                case ISSUER_CODE -> MaskUtils.maskedIssuerCode(value);
                case ISSUER_NAME -> MaskUtils.maskedIssuerName(value);
                case OTHER_DUL_SERIES -> MaskUtils.maskedOtherDulSeries(value);
                case OTHER_DUL_NUMBER -> MaskUtils.maskedOtherDulNumber(value);
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
