package com.logtest.masker.processors;

import com.logtest.masker.patterns.MaskPatternType;
import com.logtest.masker.patterns.MaskPatterns;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.temporal.Temporal;
import java.util.EnumMap;
import java.util.Map;
import java.util.function.Function;

import static java.util.Map.entry;

public class ValueProcessor {

    private static final Map<MaskPatternType, Function<String, String>> STRING_MASKERS;
    private static final Map<MaskPatternType, Function<Temporal, Temporal>> TEMPORAL_MASKERS;

    static {
        STRING_MASKERS = new EnumMap<>(Map.ofEntries(
            entry(MaskPatternType.BALANCE, MaskPatterns::maskBalance),
            entry(MaskPatternType.CONFIDENTIAL_NUMBER, MaskPatterns::maskConfidentialNumber),
            entry(MaskPatternType.EMAIL, MaskPatterns::maskEmail),
            entry(MaskPatternType.FULL_ADDRESS, MaskPatterns::maskFullAddress),
            entry(MaskPatternType.INN, MaskPatterns::maskInn),
            entry(MaskPatternType.ISSUER_NAME, MaskPatterns::maskIssuerName),
            entry(MaskPatternType.ISSUER_CODE, MaskPatterns::maskIssuerCode),
            entry(MaskPatternType.JWT_TYK_API_KEY_IP_ADDRESS, MaskPatterns::maskJwtTykApiKeyIpAddress),
            entry(MaskPatternType.KPP, MaskPatterns::maskKpp),
            entry(MaskPatternType.NAME, MaskPatterns::maskName),
            entry(MaskPatternType.OGRNUL_OR_OGRNIP, MaskPatterns::maskOgrnUlOrOgrnIp),
            entry(MaskPatternType.OKPO, MaskPatterns::maskOkpo),
            entry(MaskPatternType.OTHER_DUL_NUMBER, MaskPatterns::maskOtherDulNumber),
            entry(MaskPatternType.OTHER_DUL_SERIES, MaskPatterns::maskOtherDulSeries),
            entry(MaskPatternType.SNILS, MaskPatterns::maskSnils),
            entry(MaskPatternType.PASSPORT, MaskPatterns::maskPassport),
            entry(MaskPatternType.PASSPORT_NUMBER, MaskPatterns::maskPassportNumber),
            entry(MaskPatternType.PASSPORT_SERIES, MaskPatterns::maskPassportSeries),
            entry(MaskPatternType.PAN, MaskPatterns::maskPan),
            entry(MaskPatternType.PIN, MaskPatterns::maskPin),
            entry(MaskPatternType.PHONE, MaskPatterns::maskPhoneNumber),
            entry(MaskPatternType.TEXT_FIELD, MaskPatterns::maskTextField)
        ));

        TEMPORAL_MASKERS = new EnumMap<>(Map.ofEntries(
            entry(MaskPatternType.LOCAL_DATE,
                temporal -> temporal instanceof LocalDate date
                    ? MaskPatterns.maskLocalDate(date)
                    : temporal),

            entry(MaskPatternType.OFFSET_DATE_TIME,
                temporal -> temporal instanceof OffsetDateTime dateTime
                    ? MaskPatterns.maskOffsetDateTime(dateTime)
                    : temporal)
        ));
    }

    public static Object processValue(MaskPatternType type, Object value) {

        if (value instanceof String str) {
            Function<String, String> masker = STRING_MASKERS.get(type);
            if (masker != null) return masker.apply(str);
            return value;
        } else if (value instanceof Temporal temp) {
            Function<Temporal, Temporal> masker = TEMPORAL_MASKERS.get(type);
            if (masker != null) return masker.apply(temp);
            return value;
        } else {
            return value;
        }
    }

}