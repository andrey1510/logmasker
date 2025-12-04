package com.logtest.dep2.data;

import com.logtest.dep2.dto.FirstLevelDto;
import com.logtest.dep2.dto.NestedDto;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;

public abstract class TestData {

    private static final String TEXT = "some random text";
    private static final String TEXT_MASKED = "som*****ext";
    private static final String PASSPORT = "6002 66688";
    private static final String PASSPORT_MASKED = "60*****688";
    private static final String PHONE = "79058453312";
    private static final String PHONE_MASKED = "79*******12";
    private static final String EMAIL = "testmail@mail.com";
    private static final String EMAIL_MASKED = "t*******@m***.com";
    private static final String FULL_NAME = "Иванов Иван Иванович";
    private static final String FULL_NAME_MASKED = "И***** Иван Иванович";
    private static final String NOT_MASKED_TEXT = "not to be masked";

    protected NestedDto createNestedDto() {
        return NestedDto.builder()
            .isMasked(false)
            .phone(PHONE)
            .build();
    }

    protected NestedDto createNestedDtoMasked() {
        return NestedDto.builder()
            .isMasked(true)
            .phone(PHONE_MASKED)
            .build();
    }

    protected FirstLevelDto createFirstLevelDto() {
        return FirstLevelDto.builder()
            .isMasked(false)
            .email(EMAIL)
            .textField(TEXT)
            .passport(PASSPORT)
            .notForMaskingField(NOT_MASKED_TEXT)
            .someDate(LocalDate.of(2001, 4, 3))
            .dateTime(OffsetDateTime.of(2023, 4, 4, 4, 4, 4, 4, ZoneOffset.UTC))
            .fullname(FULL_NAME)
            .dtos(List.of(createNestedDto()))
            .nestedDto(createNestedDto())
            .build();
    }

    protected FirstLevelDto createFirstLevelDtoMasked() {
        return FirstLevelDto.builder()
            .isMasked(true)
            .email(EMAIL_MASKED)
            .textField(TEXT_MASKED)
            .passport(PASSPORT_MASKED)
            .notForMaskingField(NOT_MASKED_TEXT)
            .someDate(LocalDate.of(0, 1, 1))
            .dateTime(OffsetDateTime.of(1, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC))
            .fullname(FULL_NAME_MASKED)
            .dtos(List.of(createNestedDtoMasked()))
            .nestedDto(createNestedDtoMasked())
            .build();
    }

}
