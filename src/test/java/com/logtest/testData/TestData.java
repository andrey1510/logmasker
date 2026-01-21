package com.logtest.testData;

import com.logtest.dto.AllPatternDto;
import com.logtest.dto.DtoForRecursion;
import com.logtest.dto.DtoWithBoolean;
import com.logtest.dto.DtoWithNoIsMaskedField;
import com.logtest.dto.DtoWithWrongIsMaskedField;
import com.logtest.dto.DtoWithWrongPatternAndType;
import com.logtest.dto.NoFieldsForMaskingDto;
import com.logtest.dto.NoMaskedAnnotationDto;
import com.logtest.dto.ObjectFieldDto;
import com.logtest.dto.SimpleDtoForMasking;
import com.logtest.dto.nestedDto.Account;
import com.logtest.dto.nestedDto.IdDocument;
import com.logtest.dto.nestedDto.Passport;
import com.logtest.dto.nestedDto.Person;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class TestData {

    private static final String TEXT = "some random text";
    private static final String TEXT_MASKED = "som*****ext";
    private static final String AUTH_1 = "1111";
    private static final String AUTH_2 = "222-22";
    private static final String AUTH_1_MASKED = "1*****";
    private static final String AUTH_2_MASKED = "2*****2";
    private static final String SURNAME_1 = "Петров";
    private static final String SURNAME_1_MASKED = "П*****в";
    private static final String SURNAME_2 = "Сидоров";
    private static final String SURNAME_2_MASKED = "С*****в";
    private static final String PASSPORT = "6002 66688";
    private static final String PASSPORT_MASKED = "60*****88";
    private static final String PHONE = "79058453312";
    private static final String PHONE_MASKED = "79*****12";
    private static final String EMAIL = "testmail@mail.com";
    private static final String EMAIL_MASKED = "t*******@m***.com";
    private static final String NOT_MASKED_TEXT = "not to be masked";
    private static final String INN = "642125911472";
    private static final String INN_MASKED = "64*****11472";
    private static final LocalDate DATE_1 = LocalDate.of(2021, 1, 2);
    private static final LocalDate DATE_1_MASKED = LocalDate.of(1, 1, 2);
    private static final LocalDate DATE_2 = LocalDate.of(2001, 4, 3);
    private static final LocalDate DATE_2_MASKED = LocalDate.of(1, 4, 3);
    private static final OffsetDateTime DATE_TIME_1 = OffsetDateTime.of(2023, 4, 4, 4, 4, 4, 4, ZoneOffset.UTC);
    private static final OffsetDateTime DATE_TIME_1_MASKED = OffsetDateTime.of(1, 4, 4, 4, 4, 4, 4, ZoneOffset.UTC);

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
            .someDate(DATE_1)
            .build();
    }

    protected IdDocument createIdDocument1Masked() {
        return IdDocument.builder()
            .isMasked(true)
            .dulNumber(AUTH_1_MASKED)
            .someDate(DATE_1_MASKED)
            .build();
    }

    protected IdDocument createIdDocument2() {
        return IdDocument.builder()
            .isMasked(false)
            .dulNumber(AUTH_2)
            .someDate(DATE_2)
            .build();
    }

    protected IdDocument createIdDocument2Masked() {
        return IdDocument.builder()
            .isMasked(true)
            .dulNumber(AUTH_2_MASKED)
            .someDate(DATE_2_MASKED)
            .build();
    }

    protected Passport createPassport() {
        return Passport.builder()
            .isMasked(false)
            .passportSeriesAndNumber(PASSPORT)
            .issuanceDate(DATE_1)
            .build();
    }

    protected Passport createPassportMasked() {
        return Passport.builder()
            .isMasked(true)
            .passportSeriesAndNumber(PASSPORT_MASKED)
            .issuanceDate(DATE_1_MASKED)
            .build();
    }

    protected Person createPerson() {
        return Person.builder()
            .isMasked(false)
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
            .inn(INN)
            .someDate(DATE_1)
            .dateTime(DATE_TIME_1)
            .notForMaskingField(NOT_MASKED_TEXT)
            .build();
    }

    protected AllPatternDto createAllPatternDtoMasked(){
        return AllPatternDto.builder()
            .isMasked(true)
            .inn(INN_MASKED)
            .someDate(DATE_1_MASKED)
            .dateTime(DATE_TIME_1_MASKED)
            .notForMaskingField(NOT_MASKED_TEXT)
            .build();
    }

    protected DtoForRecursion createDeepRecursionDto(int levels) {
        if (levels <= 0)  return null;

        DtoForRecursion current = null;

        for (int i = levels; i >= 1; i--) {
            current = DtoForRecursion.builder()
                .isMasked(false)
                .text(AUTH_1)
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
            .pin(AUTH_1_MASKED)
            .build();
    }

    protected DtoWithNoIsMaskedField createDtoWithNoIsMaskedField() {
        return DtoWithNoIsMaskedField.builder()
            .pin(AUTH_1)
            .build();
    }

    protected DtoWithNoIsMaskedField createDtoWithNoIsMaskedFieldMasked() {
        return DtoWithNoIsMaskedField.builder()
            .pin(AUTH_1_MASKED)
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
            .correctPatternAndType(AUTH_1_MASKED)
            .wrongPattern("*****")
            .wrongType(1234)
            .nestedDto(DtoWithWrongPatternAndType.builder()
                .isMasked(true)
                .correctPatternAndType(AUTH_1_MASKED)
                .wrongPattern("*****")
                .wrongType(1234)
                .build())
            .build();
    }

    protected DtoWithBoolean createDtoWithBoolean() {
        return DtoWithBoolean.builder()
            .isMasked(false)
            .phoneNumber(PHONE)
            .build();
    }

    protected DtoWithBoolean createDtoWithBooleanMasked() {
        return DtoWithBoolean.builder()
            .isMasked(true)
            .phoneNumber(PHONE_MASKED)
            .build();
    }


    protected ObjectFieldDto createObjectFieldDto() {
        return ObjectFieldDto.builder()
            .isMasked(false)
            .email(EMAIL)
            .textField(TEXT)
            .textFieldMap(new HashMap<>() {{
                put("name", "Иван");
                put("patronymic", "Иванович");
                put("surname", "Иванов");
            }})
            .textFieldMapDto(new HashMap<>() {{
                put("email", new ObjectFieldDto(false, EMAIL, null, null, null ));
            }})
            .build();
    }

    protected ObjectFieldDto createObjectFieldDtoMasked() {
        return ObjectFieldDto.builder()
            .isMasked(true)
            .email(EMAIL_MASKED)
            .textField(TEXT_MASKED)
            .textFieldMap(new HashMap<>() {{
                put("name", "И*****");
                put("patronymic", "И*****ич");
                put("surname", "И*****в");
            }})
            .textFieldMapDto(new HashMap<>() {{
                put("email", new ObjectFieldDto(true, EMAIL_MASKED, null, null, null ));
            }})
            .build();
    }

}



