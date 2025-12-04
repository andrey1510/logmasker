package com.logtest.dep1.data;

import com.logtest.dep1.dto.AllPatternDto;
import com.logtest.dep1.dto.DtoForRecursion;
import com.logtest.dep1.dto.DtoWithNoIsMaskedField;
import com.logtest.dep1.dto.DtoWithWrongIsMaskedField;
import com.logtest.dep1.dto.DtoWithWrongPatternAndType;
import com.logtest.dep1.dto.NoFieldsForMaskingDto;
import com.logtest.dep1.dto.NoMaskedAnnotationDto;
import com.logtest.dep1.dto.SimpleDtoForMasking;
import com.logtest.dep1.dto.nestedDto.Account;
import com.logtest.dep1.dto.nestedDto.IdDocument;
import com.logtest.dep1.dto.nestedDto.Passport;
import com.logtest.dep1.dto.nestedDto.Person;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;

public abstract class TestData {

    private static final String TEXT = "some random text";
    private static final String TEXT_MASKED = "som**********ext";
    private static final String AUTH_1 = "1111";
    private static final String AUTH_2 = "222-22";
    private static final String AUTH_MASKED = "***";
    private static final String SURNAME_1 = "Петров";
    private static final String SURNAME_1_MASKED = "П***";
    private static final String SURNAME_2 = "Сидоров";
    private static final String SURNAME_2_MASKED = "С***";
    private static final String PASSPORT = "6002 66688";
    private static final String PASSPORT_MASKED = "60******88";
    private static final String PHONE = "79058453312";
    private static final String PHONE_MASKED = "79*******12";
    private static final String ADDRESS = "Москва, ул. Лесная, д. 15";
    private static final String ADDRESS_MASKED = "Москва, ул. Лес**********";
    private static final String EMAIL = "testmail@mail.com";
    private static final String EMAIL_MASKED = "********@mail.com";
    private static final String FULL_NAME = "Иванов Иван Иванович";
    private static final String FULL_NAME_MASKED = "И*** Иван Иванович";
    private static final String NOT_MASKED_TEXT = "not to be masked";

    protected Account createAccount1() {
        return Account.builder()
            .isMasked(false)
            .surname(SURNAME_1)
            .build();
    }

    protected Account createAccount1Masked() {
        return Account.builder()
            .isMasked(true)
            .surname(SURNAME_1_MASKED)
            .build();
    }

    protected Account createAccount2() {
        return Account.builder()
            .isMasked(false)
            .surname(SURNAME_2)
            .build();
    }

    protected Account createAccount2Masked() {
        return Account.builder()
            .isMasked(true)
            .surname(SURNAME_2_MASKED)
            .build();
    }

    protected IdDocument createIdDocument1() {
        return IdDocument.builder()
            .isMasked(false)
            .dulNumber(AUTH_1)
            .someDate(LocalDate.of(2021, 1, 2))
            .build();
    }

    protected IdDocument createIdDocument1Masked() {
        return IdDocument.builder()
            .isMasked(true)
            .dulNumber(AUTH_MASKED)
            .someDate(LocalDate.of(0, 1, 1))
            .build();
    }

    protected IdDocument createIdDocument2() {
        return IdDocument.builder()
            .isMasked(false)
            .dulNumber(AUTH_2)
            .someDate(LocalDate.of(2001, 4, 3))
            .build();
    }

    protected IdDocument createIdDocument2Masked() {
        return IdDocument.builder()
            .isMasked(true)
            .dulNumber(AUTH_MASKED)
            .someDate(LocalDate.of(0, 1, 1))
            .build();
    }

    protected Passport createPassport() {
        return Passport.builder()
            .isMasked(false)
            .passportSeriesAndNumber(PASSPORT)
            .issuanceDate(LocalDate.of(2002, 10, 10))
            .build();
    }

    protected Passport createPassportMasked() {
        return Passport.builder()
            .isMasked(true)
            .passportSeriesAndNumber(PASSPORT_MASKED)
            .issuanceDate(LocalDate.of(0, 1, 1))
            .build();
    }

    protected Person createPerson() {
        return Person.builder()
            .isMasked(false)
            .fullname(FULL_NAME)
            .email(EMAIL)
            .textField(TEXT)
            .passport(createPassport())
            .accounts(List.of(createAccount1(),createAccount2()))
            .idDocuments(Map.of("doc1", createIdDocument1(), "doc2", createIdDocument2()))
            .build();
    }

    protected Person createPersonMasked() {
        return Person.builder()
            .isMasked(true)
            .fullname(FULL_NAME_MASKED)
            .email(EMAIL_MASKED)
            .textField(TEXT_MASKED)
            .passport(createPassportMasked())
            .accounts(List.of(createAccount1Masked(),createAccount2Masked()))
            .idDocuments(Map.of("doc1", createIdDocument1Masked(), "doc2", createIdDocument2Masked()))
            .build();
    }

    protected AllPatternDto createAllPatternDto(){
        return AllPatternDto.builder()
            .isMasked(false)
            .surname(SURNAME_1)
            .fullAddress(ADDRESS)
            .email(EMAIL)
            .fullname(FULL_NAME)
            .pan(AUTH_2)
            .passportSeries(PASSPORT)
            .textField(TEXT)
            .someDate(LocalDate.of(2001, 4, 3))
            .dateTime(OffsetDateTime.of(2023, 4, 4, 4, 4, 4, 4, ZoneOffset.UTC))
            .notForMaskingField(NOT_MASKED_TEXT)
            .build();
    }

    protected AllPatternDto createAllPatternDtoMasked(){
        return AllPatternDto.builder()
            .isMasked(true)
            .surname(SURNAME_1_MASKED)
            .fullAddress(ADDRESS_MASKED)
            .email(EMAIL_MASKED)
            .fullname(FULL_NAME_MASKED)
            .pan(AUTH_MASKED)
            .passportSeries(PASSPORT_MASKED)
            .textField(TEXT_MASKED)
            .someDate(LocalDate.of(0, 1, 1))
            .dateTime(OffsetDateTime.of(1, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC))
            .notForMaskingField(NOT_MASKED_TEXT)
            .build();
    }

    protected DtoForRecursion createDeepRecursionDto(int levels) {
        if (levels <= 0)  return null;

        DtoForRecursion current = null;

        for (int i = levels; i >= 1; i--) {
            current = DtoForRecursion.builder()
                .isMasked(false)
                .pin(AUTH_1)
                .dto(current)
                .build();
        }
        return current;
    }

    protected NoFieldsForMaskingDto createNoFieldsForMaskingDto() {
        return NoFieldsForMaskingDto.builder()
            .isMasked(false)
            .someText(TEXT)
            .build();
    }

    protected NoFieldsForMaskingDto createNoFieldsForMaskingDtoMasked() {
        return NoFieldsForMaskingDto.builder()
            .isMasked(true)
            .someText(TEXT)
            .build();
    }

    protected NoMaskedAnnotationDto createNoMaskedAnnotationDto() {
        return NoMaskedAnnotationDto.builder()
            .isMasked(false)
            .pin(AUTH_1)
            .build();
    }

    protected DtoWithWrongIsMaskedField createDtoWithWrongIsMaskedField() {
        return DtoWithWrongIsMaskedField.builder()
            .isMasked("false")
            .pin(AUTH_1)
            .build();
    }

    protected DtoWithWrongIsMaskedField createDtoWithWrongIsMaskedFieldMasked() {
        return DtoWithWrongIsMaskedField.builder()
            .isMasked("false")
            .pin(AUTH_MASKED)
            .build();
    }

    protected DtoWithNoIsMaskedField createDtoWithNoIsMaskedField() {
        return DtoWithNoIsMaskedField.builder()
            .pin(AUTH_1)
            .build();
    }

    protected DtoWithNoIsMaskedField createDtoWithNoIsMaskedFieldMasked() {
        return DtoWithNoIsMaskedField.builder()
            .pin(AUTH_MASKED)
            .build();
    }

    protected SimpleDtoForMasking createSimpleDtoForMaskingIsMaskedTrue() {
        return SimpleDtoForMasking.builder()
            .isMasked(true)
            .phoneNumber(PHONE)
            .build();
    }

    protected SimpleDtoForMasking createSimpleDtoForMaskingIsMaskedNull() {
        return SimpleDtoForMasking.builder()
            .phoneNumber(PHONE)
            .build();
    }

    protected SimpleDtoForMasking createSimpleDtoForMaskingMasked() {
        return SimpleDtoForMasking.builder()
            .isMasked(true)
            .phoneNumber(PHONE_MASKED)
            .build();
    }


    protected DtoWithWrongPatternAndType createDtoWithWrongPatternAndType() {
        return DtoWithWrongPatternAndType.builder()
            .isMasked(false)
            .correctPatternAndType(AUTH_1)
            .wrongPattern(TEXT)
            .wrongType(1234)
            .nestedDto(DtoWithWrongPatternAndType.builder()
                .isMasked(false)
                .correctPatternAndType(AUTH_1)
                .wrongPattern(TEXT)
                .wrongType(1234)
                .build())
            .build();
    }

    protected DtoWithWrongPatternAndType createDtoWithWrongPatternAndTypeMasked() {
        return DtoWithWrongPatternAndType.builder()
            .isMasked(true)
            .correctPatternAndType(AUTH_MASKED)
            .wrongPattern(TEXT)
            .wrongType(1234)
            .nestedDto(DtoWithWrongPatternAndType.builder()
                .isMasked(true)
                .correctPatternAndType(AUTH_MASKED)
                .wrongPattern(TEXT)
                .wrongType(1234)
                .build())
            .build();
    }

}



