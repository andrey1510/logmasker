package com.logtest.masker.utils;

public enum MaskPatternType {
    CUSTOM, // для указания паттерна вручную в Dto
    TEXT_FIELD,
    FULL_NAME,
    EMAIL,
    LOCALDATE,
    OFFSETDATETIME,
    FULL_ADDRESS,
    SURNAME,
    AUTH_DATA,
    PASSPORT_SERIES_AND_NUMBER,
}
