package com.logtest.masker;

public enum MaskType {
    CUSTOM, // для указания паттерна вручную в Dto
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
    LOCALDATE_FIELD_TO_STRING_FIELD,
    LOCALDATE_TO_ZERO_DATE
}
