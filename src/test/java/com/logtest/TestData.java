package com.logtest;

import com.logtest.dto.AllPatternDto;
import com.logtest.dto.nestedDto.Account;
import com.logtest.dto.nestedDto.Credentials;
import com.logtest.dto.nestedDto.IdDocument;
import com.logtest.dto.nestedDto.Passport;
import com.logtest.dto.nestedDto.Person;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class TestData {

    protected Credentials createCredentials1(){
        return Credentials.builder()
            .isMasked(false)
            .confidentialNumber("333456789333")
            .pin("1111")
            .build();
    }

    protected Credentials createCredentials1Masked(){
        return Credentials.builder()
            .isMasked(true)
            .confidentialNumber("33******9333")
            .pin("****")
            .build();
    }

    protected Credentials createCredentials2(){
        return Credentials.builder()
            .isMasked(false)
            .confidentialNumber("444456789044")
            .pin("222222")
            .build();
    }

    protected Credentials createCredentials2Masked(){
        return Credentials.builder()
            .isMasked(true)
            .confidentialNumber("44******9044")
            .pin("******")
            .build();
    }

    protected Credentials createCredentials3(){
        return Credentials.builder()
            .isMasked(false)
            .confidentialNumber("666656789066")
            .pin("666666")
            .build();
    }

    protected Credentials createCredentials3Masked(){
        return Credentials.builder()
            .isMasked(true)
            .confidentialNumber("66******9066")
            .pin("******")
            .build();
    }

    protected Account createAccount1() {
        return Account.builder()
            .isMasked(false)
            .pan("1000007890123111")
            .balance("10000")
            .credentials(Set.of(createCredentials1()))
            .build();
    }

    protected Account createAccount1Masked() {
        return Account.builder()
            .isMasked(true)
            .pan("100000******3111")
            .balance("***")
            .credentials(Set.of(createCredentials1Masked()))
            .build();
    }

    protected Account createAccount2() {
        return Account.builder()
            .isMasked(false)
            .pan("2000007890123222")
            .balance("202000")
            .credentials(Set.of(createCredentials2(), createCredentials3()))
            .build();
    }

    protected Account createAccount2Masked() {
        return Account.builder()
            .isMasked(true)
            .pan("200000******3222")
            .balance("***")
            .credentials(Set.of(createCredentials2Masked(), createCredentials3Masked()))
            .build();
    }

    protected IdDocument createIdDocument1() {
        return IdDocument.builder()
            .isMasked(false)
            .otherDulSeries("212")
            .otherDulNumber("T-8008")
            .build();
    }

    protected IdDocument createIdDocument1Masked() {
        return IdDocument.builder()
            .isMasked(true)
            .otherDulSeries("2***")
            .otherDulNumber("****8")
            .build();
    }

    protected IdDocument createIdDocument2() {
        return IdDocument.builder()
            .isMasked(false)
            .otherDulSeries("3434")
            .otherDulNumber("S0001")
            .build();
    }

    protected IdDocument createIdDocument2Masked() {
        return IdDocument.builder()
            .isMasked(true)
            .otherDulSeries("3***")
            .otherDulNumber("****1")
            .build();
    }

    protected Passport createPassport() {
        return Passport.builder()
            .isMasked(false)
            .passport("6002400919")
            .passportSeries("6002")
            .passportNumber("400919")
            .issuerCode("770-220")
            .issuerName("Заречным РУВД г. Москвы")
            .issuanceDate(LocalDate.of(2002, 10, 10))
            .issuanceDateMasked(null)
            .build();
    }

    protected Passport createPassportMasked() {
        return Passport.builder()
            .isMasked(true)
            .passport("60*****919")
            .passportSeries("60**")
            .passportNumber("***919")
            .issuerCode("770-***")
            .issuerName("Заре ***** . Москвы")
            .issuanceDate(null)
            .issuanceDateMasked("10.10.****")
            .build();
    }

    protected Person createPerson() {
        return Person.builder()
            .isMasked(false)
            .fullname("Иванов Иван Иванович")
            .email("testmail@mail.com")
            .phoneNumber("79058453312")
            .textField("some random text")
            .passport(createPassport())
            .accounts(List.of(createAccount1(),createAccount2()))
            .idDocuments(Map.of("doc1", createIdDocument1(), "doc2", createIdDocument2()))
            .build();
    }

    protected Person createPersonMasked() {
        return Person.builder()
            .isMasked(true)
            .fullname("И***** Иван Иванович")
            .email("t*******@m***.com")
            .phoneNumber("79*******12")
            .textField("som*****ext")
            .passport(createPassportMasked())
            .accounts(List.of(createAccount1Masked(),createAccount2Masked()))
            .idDocuments(Map.of("doc1", createIdDocument1Masked(), "doc2", createIdDocument2Masked()))
            .build();
    }

    protected AllPatternDto createAllPatternDto(){
        return AllPatternDto.builder()
            .isMasked(false)
            .balance("3300")
            .confidentialNumber("444456789044")
            .email("testmail@mail.com")
            .fullname("Иванов Иван Иванович")
            .pan("2000007890123222")
            .issuanceDate(LocalDate.of(1998, 11, 12))
            .issuanceDateMasked(null)
            .issuerCode("770-220")
            .issuerName("Заречным РУВД г. Москвы")
            .otherDulNumber("S0001")
            .otherDulSeries("3434")
            .passport("6002400919")
            .passportNumber("400919")
            .passportSeries("6002")
            .phoneNumber("79058453312")
            .pin("222222")
            .textField("some random text")
            .notForMaskingField("not to be masked")
            .build();
    }

    protected AllPatternDto createAllPatternDtoMasked(){
        return AllPatternDto.builder()
            .isMasked(true)
            .balance("***")
            .confidentialNumber("44******9044")
            .email("t*******@m***.com")
            .fullname("И***** Иван Иванович")
            .pan("200000******3222")
            .issuanceDate(null)
            .issuanceDateMasked("12.11.****")
            .issuerCode("770-***")
            .issuerName("Заре ***** . Москвы")
            .otherDulNumber("****1")
            .otherDulSeries("3***")
            .passport("60*****919")
            .passportNumber("***919")
            .passportSeries("60**")
            .phoneNumber("79*******12")
            .pin("******")
            .textField("som*****ext")
            .notForMaskingField("not to be masked")
            .build();


    }

}