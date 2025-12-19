package com.logtest;

import com.logtest.masker.patterns.MaskPatterns;
import com.logtest.masker.patterns.MaskPatternsAlt;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MailPatternsTests {

    @Test
    void maskInn_test() {

        assertEquals("64*****632", MaskPatterns.maskInn("6454093632"));
        assertEquals("64*****11472", MaskPatterns.maskInn("642125911472"));
        assertEquals("64*****632", MaskPatterns.maskInn(" 6454093632 "));
        assertEquals("64*****11472", MaskPatterns.maskInn(" 642125911472 "));
        assertEquals("*****", MaskPatterns.maskInn("64540"));
        assertEquals("*****", MaskPatterns.maskInn("6454011111111111111111111"));
        assertEquals("   ", MaskPatterns.maskInn("   "));
        assertEquals("", MaskPatterns.maskInn(""));

    }

    @Test
    void maskKpp_test() {

        assertEquals("64******3", MaskPatterns.maskKpp("645401003"));
        assertEquals("*****", MaskPatterns.maskKpp("64540"));
        assertEquals("*****", MaskPatterns.maskKpp("6454011111111111111111111"));
        assertEquals("   ", MaskPatterns.maskKpp("   "));
        assertEquals("", MaskPatterns.maskKpp(""));

    }

    @Test
    void maskOkpo_test() {
        assertEquals("00*****5", MaskPatterns.maskOkpo("00002335"));
        assertEquals("0002*****9", MaskPatterns.maskOkpo("0002870479"));
        assertEquals("*****", MaskPatterns.maskOkpo("64540"));
        assertEquals("*****", MaskPatterns.maskOkpo("6454011111111111111111111"));
        assertEquals("   ", MaskPatterns.maskOkpo("   "));
        assertEquals("", MaskPatterns.maskOkpo(""));

    }

    @Test
    void maskOgrnulOrOgrnip_test() {
        assertEquals("12*******5220", MaskPatterns.maskOgrnUlOrOgrnIp("1215573935220"));
        assertEquals("12*********2345", MaskPatterns.maskOgrnUlOrOgrnIp("123456789012345"));
        assertEquals("*****", MaskPatterns.maskOgrnUlOrOgrnIp("64540"));
        assertEquals("*****", MaskPatterns.maskOgrnUlOrOgrnIp("6454011111111111111111111"));
        assertEquals("   ", MaskPatterns.maskOgrnUlOrOgrnIp("   "));
        assertEquals("", MaskPatterns.maskOgrnUlOrOgrnIp(""));

    }

    @Test
    void maskSurname_test() {
        assertEquals("П***", MaskPatternsAlt.maskSurname("Петров"));
        assertEquals("П***", MaskPatternsAlt.maskSurname("Прокофьев-Северский"));
        assertEquals("д***", MaskPatternsAlt.maskSurname("д'Альбер"));
        assertEquals("д***", MaskPatternsAlt.maskSurname("де Ореллана"));
        assertEquals("И", MaskPatternsAlt.maskSurname("И"));
        assertEquals("", MaskPatternsAlt.maskSurname(""));
    }

    @Test
    void maskEmail_test() {
        assertEquals("t*******@e****.com", MaskPatterns.maskEmail("testmail@email.com"));
        assertEquals("t*****@e****.com", MaskPatterns.maskEmail("test.m@email.com"));
        assertEquals("t*****@e****.co.uk", MaskPatterns.maskEmail("test.m@email.co.uk"));
        assertEquals("*****", MaskPatterns.maskEmail("test.m@"));
        assertEquals("*****", MaskPatterns.maskEmail("@mail.com"));
        assertEquals("*****", MaskPatterns.maskEmail("test.mail.com"));
        assertEquals("   ", MaskPatterns.maskOgrnUlOrOgrnIp("   "));
        assertEquals("", MaskPatterns.maskEmail(""));
    }

    @Test
    void maskEmailAlt_test() {
        assertEquals("****@mail.com", MaskPatternsAlt.maskEmail("test@mail.com"));
        assertEquals("@mail.com", MaskPatternsAlt.maskEmail("@mail.com"));
        assertEquals("******@mail.com", MaskPatternsAlt.maskEmail("test.m@mail.com"));
        assertEquals("", MaskPatternsAlt.maskEmail(""));
        assertEquals("test", MaskPatternsAlt.maskEmail("test"));
    }

    @Test
    void maskFullName_test() {
        assertEquals("И***", MaskPatternsAlt.maskFullName("Иванов"));
        assertEquals("П*** Иван Иванович", MaskPatternsAlt.maskFullName("Прокофьев-Северский Иван Иванович"));
        assertEquals("И Ли Сын", MaskPatternsAlt.maskFullName("И Ли Сын"));
        assertEquals("", MaskPatternsAlt.maskFullName(""));
    }

    @Test
    void maskFullAddress_test() {
        assertEquals("Москва, ул. Лес**********", MaskPatternsAlt.maskFullAddress("Москва, ул. Лесная, д. 15"));
        assertEquals("Санкт**********", MaskPatternsAlt.maskFullAddress("Санкт-Петербург"));
        assertEquals("******", MaskPatternsAlt.maskFullAddress("Москва"));
        assertEquals("", MaskPatternsAlt.maskFullAddress(""));
    }

    @Test
    void maskAuthData_test() {
        assertEquals("***", MaskPatternsAlt.maskAuthData("ASD 452345"));
        assertEquals("***", MaskPatternsAlt.maskAuthData("52"));
        assertEquals("***", MaskPatternsAlt.maskAuthData("333-666"));
        assertEquals("", MaskPatternsAlt.maskAuthData(""));
    }

    @Test
    void maskTextFieldAlt_test() {

        assertEquals("", MaskPatternsAlt.maskTextField(""));
        assertEquals("*", MaskPatternsAlt.maskTextField("1"));
        assertEquals("1*", MaskPatternsAlt.maskTextField("12"));
        assertEquals("1**", MaskPatternsAlt.maskTextField("123"));
        assertEquals("1***", MaskPatternsAlt.maskTextField("1234"));
        assertEquals("1***5", MaskPatternsAlt.maskTextField("12345"));
        assertEquals("1****6", MaskPatternsAlt.maskTextField("123456"));
        assertEquals("1*****7", MaskPatternsAlt.maskTextField("1234567"));
        assertEquals("1*****78", MaskPatternsAlt.maskTextField("12345678"));
        assertEquals("1******89", MaskPatternsAlt.maskTextField("123456789"));
        assertEquals("12******90", MaskPatternsAlt.maskTextField("1234567890"));
        assertEquals("12*******09", MaskPatternsAlt.maskTextField("12345678909"));
        assertEquals("12********98", MaskPatternsAlt.maskTextField("123456789098"));
        assertEquals("12********987", MaskPatternsAlt.maskTextField("1234567890987"));
        assertEquals("12*********876", MaskPatternsAlt.maskTextField("12345678909876"));
        assertEquals("12*********8765", MaskPatternsAlt.maskTextField("123456789098765"));
        assertEquals("123**********654", MaskPatternsAlt.maskTextField("1234567890987654"));
        assertEquals("123***********543", MaskPatternsAlt.maskTextField("12345678909876543"));
        assertEquals("123***********3234", MaskPatternsAlt.maskTextField("123456789876543234"));
        assertEquals("123************2123", MaskPatternsAlt.maskTextField("1234567898765432123"));
        assertEquals("1234************1238", MaskPatternsAlt.maskTextField("12345678987654321238"));
        assertEquals("1234*************2389", MaskPatternsAlt.maskTextField("123456789876543212389"));

        assertEquals(
            "У попа была с****************************************, он ее убил.",
            MaskPatternsAlt.maskTextField("У попа была собака, он ее любил. Она съела кусок мяса, он ее убил.")
        );
    }

    @Test
    void maskPassportSeriesAndNumberField_test() {
        assertEquals("55*******33", MaskPatternsAlt.maskPassportSeriesAndNumber("5563 456333"));
        assertEquals("55*******33", MaskPatternsAlt.maskPassportSeriesAndNumber("5563-456333"));
        assertEquals("55*******33", MaskPatternsAlt.maskPassportSeriesAndNumber("5563.456333"));
        assertEquals("55*****333", MaskPatternsAlt.maskPassportSeriesAndNumber("5563456333"));
        assertEquals("", MaskPatternsAlt.maskPassportSeriesAndNumber(""));
    }

    @Test
    void maskSnils_test() {
        assertEquals("12**********23", MaskPatterns.maskSnils("123-345-678 23"));
        assertEquals("12*******23", MaskPatterns.maskSnils("12334567823"));
        assertEquals("*****", MaskPatterns.maskSnils("1234567"));
        assertEquals("   ", MaskPatterns.maskSnils("   "));
        assertEquals("", MaskPatterns.maskSnils(""));
    }

    @Test
    void maskJwtTykApiKeyIpAddress_test() {
        assertEquals("***", MaskPatterns.maskJwtTykApiKeyIpAddress("66.249.64.110"));
        assertEquals("***", MaskPatterns.maskJwtTykApiKeyIpAddress("I5F53UWYEkNvbLeViF1cesaxKYuzlcMkny3Tsk362GS"));
        assertEquals("   ", MaskPatterns.maskJwtTykApiKeyIpAddress("   "));
        assertEquals("", MaskPatterns.maskJwtTykApiKeyIpAddress(""));
    }

    @Test
    void maskPhone_test() {
        assertEquals("*****", MaskPatterns.maskPhoneNumber("123"));
        assertEquals("1***5", MaskPatterns.maskPhoneNumber("12345"));
        assertEquals("1****6", MaskPatterns.maskPhoneNumber("123456"));
        assertEquals("1*****7", MaskPatterns.maskPhoneNumber("1234567"));
        assertEquals("1******89", MaskPatterns.maskPhoneNumber("123456789"));
        assertEquals("12******91", MaskPatterns.maskPhoneNumber("1234567891"));
        assertEquals("12*******12", MaskPatterns.maskPhoneNumber("12345678912"));
        assertEquals("12********23", MaskPatterns.maskPhoneNumber("123456789123"));
        assertEquals("12********234", MaskPatterns.maskPhoneNumber("1234567891234"));
        assertEquals("12*********345", MaskPatterns.maskPhoneNumber("12345678912345"));
        assertEquals("12**********456", MaskPatterns.maskPhoneNumber("123456789123456"));
        assertEquals("*****", MaskPatterns.maskPhoneNumber("1234567891234567"));
        assertEquals("*****", MaskPatterns.maskPhoneNumber("12345678912345678"));
        assertEquals("+7********-62", MaskPatterns.maskPhoneNumber("+7(901) 22-62"));
        assertEquals("   ", MaskPatterns.maskPhoneNumber("   "));
        assertEquals("", MaskPatterns.maskPhoneNumber(""));
    }

    @Test
    void maskName_test() {
        assertEquals("С***-Т***-Ш*** Пётр Петрович", MaskPatterns.maskName("Семёнов-Тян-Шанский Пётр Петрович"));
        assertEquals("А***** Жан-Поль", MaskPatterns.maskName("Альбер Жан-Поль"));
        assertEquals("П***-С*** Жан-Поль", MaskPatterns.maskName("Прокофьев-Северский Жан-Поль"));
        assertEquals("****", MaskPatterns.maskName("1234"));
        assertEquals("1****", MaskPatterns.maskName("12345"));
        assertEquals("П*****", MaskPatterns.maskName(" Петров "));
        assertEquals("1******", MaskPatterns.maskName("1234567"));
        assertEquals("1***78", MaskPatterns.maskName("12345678"));
        assertEquals("П***-С***", MaskPatterns.maskName("Прокофьев-Северский"));
        assertEquals("П***** Михаил Павлович", MaskPatterns.maskName("Петров Михаил Павлович"));
        assertEquals("П***ий Михаил Павлович", MaskPatterns.maskName("Петровский Михаил Павлович"));
        assertEquals("П***й, Михаил Павлович", MaskPatterns.maskName("Петровский, Михаил Павлович"));
        assertEquals("П***** Михаил Павлович", MaskPatterns.maskName("Петров   Михаил    Павлович"));
        assertEquals("п***** михаил павлович", MaskPatterns.maskName("петров михаил павлович"));
        assertEquals("п***** михаил павлович", MaskPatterns.maskName("п:%,2! михаил павлович"));
        assertEquals("Р***ес Хосе Энрике Гусман", MaskPatterns.maskName("Родригес Хосе Энрике Гусман"));
        assertEquals("П***-С*** Алексей Иванович", MaskPatterns.maskName("Прокофьев-Северский Алексей Иванович"));
        assertEquals("П***-С*** Алексей", MaskPatterns.maskName("Прокофьев-Северский Алексей"));
        assertEquals("П***** Михаил", MaskPatterns.maskName("Петров Михаил"));
        assertEquals("   ", MaskPatterns.maskName("   "));
        assertEquals("", MaskPatterns.maskName(""));
    }

    @Test
    void maskBalance_test() {
        assertEquals("***", MaskPatterns.maskBalance("23131212"));
        assertEquals("   ", MaskPatterns.maskBalance("   "));
        assertEquals("", MaskPatterns.maskBalance(""));
    }

    @Test
    void maskOtherDulNumber_test() {
        assertEquals("****6", MaskPatterns.maskOtherDulNumber("123456"));
        assertEquals("****5", MaskPatterns.maskOtherDulNumber("12345"));
        assertEquals("****4", MaskPatterns.maskOtherDulNumber("1234"));
        assertEquals("****3", MaskPatterns.maskOtherDulNumber("123"));
        assertEquals("****1", MaskPatterns.maskOtherDulNumber("1"));
        assertEquals("   ", MaskPatterns.maskOtherDulNumber("   "));
        assertEquals("", MaskPatterns.maskOtherDulNumber(""));
    }

    @Test
    void maskOtherDulSeries_test() {
        assertEquals("1***", MaskPatterns.maskOtherDulSeries("123456"));
        assertEquals("1***", MaskPatterns.maskOtherDulSeries("12345"));
        assertEquals("1***", MaskPatterns.maskOtherDulSeries("1234"));
        assertEquals("1***", MaskPatterns.maskOtherDulSeries("123"));
        assertEquals("1***", MaskPatterns.maskOtherDulSeries("1"));
        assertEquals("   ", MaskPatterns.maskOtherDulSeries("   "));
        assertEquals("", MaskPatterns.maskOtherDulSeries(""));
    }

    @Test
    void maskIssuerName_test() {
        assertEquals("Изм *****  РОВД", MaskPatterns.maskIssuerName("Измайловский РОВД"));
        assertEquals("Из ***** ким", MaskPatterns.maskIssuerName("Измайловским"));
        assertEquals(" ***** 456", MaskPatterns.maskIssuerName("123456"));
        assertEquals(" ***** 45", MaskPatterns.maskIssuerName("12345"));
        assertEquals("****", MaskPatterns.maskIssuerName("1234"));
        assertEquals("****", MaskPatterns.maskIssuerName("123"));
        assertEquals("   ", MaskPatterns.maskIssuerName("   "));
        assertEquals("", MaskPatterns.maskIssuerName(""));
    }

    @Test
    void maskIssuerCode_test() {
        assertEquals("123-***", MaskPatterns.maskIssuerCode("123-456"));
        assertEquals("12***", MaskPatterns.maskIssuerCode("12 34"));
        assertEquals("123***", MaskPatterns.maskIssuerCode("123456"));
        assertEquals("1***", MaskPatterns.maskIssuerCode("1234"));
        assertEquals("***", MaskPatterns.maskIssuerCode("123"));
        assertEquals("***", MaskPatterns.maskIssuerCode("12"));
        assertEquals("   ", MaskPatterns.maskIssuerCode("   "));
        assertEquals("", MaskPatterns.maskIssuerCode(""));
    }

    @Test
    void maskPassport_test() {
        assertEquals("12********890", MaskPatterns.maskPassport("12 34 567 890"));
        assertEquals("12*****890", MaskPatterns.maskPassport("1234567890"));
        assertEquals("12*456", MaskPatterns.maskPassport("123456"));
        assertEquals("*****", MaskPatterns.maskPassport("12345"));
        assertEquals("*****", MaskPatterns.maskPassport("1234"));
        assertEquals("   ", MaskPatterns.maskPassport("   "));
        assertEquals("", MaskPatterns.maskPassport(""));
    }

    @Test
    void maskPassportNumber_test() {
        assertEquals("*** 456", MaskPatterns.maskPassportNumber("123 456"));
        assertEquals("***4567890", MaskPatterns.maskPassportNumber("1234567890"));
        assertEquals("***456", MaskPatterns.maskPassportNumber("123456"));
        assertEquals("***45", MaskPatterns.maskPassportNumber("12345"));
        assertEquals("***4", MaskPatterns.maskPassportNumber("1234"));
        assertEquals("***", MaskPatterns.maskPassportNumber("123"));
        assertEquals("***", MaskPatterns.maskPassportNumber("23"));
        assertEquals("   ", MaskPatterns.maskPassportNumber("   "));
        assertEquals("", MaskPatterns.maskPassportNumber(""));
    }

    @Test
    void maskPassportSeries_test() {
        assertEquals("12345678**", MaskPatterns.maskPassportSeries("1234567890"));
        assertEquals("12 **", MaskPatterns.maskPassportSeries("12 34"));
        assertEquals("12**", MaskPatterns.maskPassportSeries("1234"));
        assertEquals("1**", MaskPatterns.maskPassportSeries("123"));
        assertEquals("****", MaskPatterns.maskPassportSeries("12"));
        assertEquals("****", MaskPatterns.maskPassportSeries("1"));
        assertEquals("   ", MaskPatterns.maskPassportSeries("   "));
        assertEquals("", MaskPatterns.maskPassportSeries(""));
    }

    @Test
    void maskPin_test() {
        assertEquals("*******", MaskPatterns.maskPin("123 456"));
        assertEquals("******", MaskPatterns.maskPin("123456"));
        assertEquals("*****", MaskPatterns.maskPin("12345"));
        assertEquals("****", MaskPatterns.maskPin("1234"));
        assertEquals("***", MaskPatterns.maskPin("123"));
        assertEquals("**", MaskPatterns.maskPin("12"));
        assertEquals("*", MaskPatterns.maskPin("1"));
        assertEquals("   ", MaskPatterns.maskPin("   "));
        assertEquals("", MaskPatterns.maskPin(""));
    }

    @Test
    void maskPan_test() {
        assertEquals("123456***0123", MaskPatterns.maskPan("1234567890123"));
        assertEquals("123456****0123", MaskPatterns.maskPan("1234567 890123"));
        assertEquals("*****", MaskPatterns.maskPan("1234567890"));
        assertEquals("*****", MaskPatterns.maskPan("1"));
        assertEquals("   ", MaskPatterns.maskPan("   "));
        assertEquals("", MaskPatterns.maskPan(""));
    }

    @Test
    void maskConfidentialNumber_test() {
        assertEquals("123******0123", MaskPatterns.maskConfidentialNumber("1234567890123"));
        assertEquals("1234******0123", MaskPatterns.maskConfidentialNumber("1234567 890123"));
        assertEquals("******7890", MaskPatterns.maskConfidentialNumber("1234567890"));
        assertEquals("*****", MaskPatterns.maskConfidentialNumber("123456789"));
        assertEquals("*****", MaskPatterns.maskConfidentialNumber("123456"));
        assertEquals("*****", MaskPatterns.maskConfidentialNumber("1"));
        assertEquals("   ", MaskPatterns.maskConfidentialNumber("   "));
        assertEquals("", MaskPatterns.maskConfidentialNumber(""));
    }

    @Test
    void maskTextField_test() {
        assertEquals("", MaskPatterns.maskTextField(""));
        assertEquals("  ", MaskPatterns.maskTextField("  "));
        assertEquals("*****", MaskPatterns.maskTextField("1"));
        assertEquals("1*****", MaskPatterns.maskTextField("12"));
        assertEquals("1*****", MaskPatterns.maskTextField("123"));
        assertEquals("1*****", MaskPatterns.maskTextField("1234"));
        assertEquals("1*****5", MaskPatterns.maskTextField("12345"));
        assertEquals("1*****6", MaskPatterns.maskTextField("123456"));
        assertEquals("1*****7", MaskPatterns.maskTextField("1234567"));
        assertEquals("1*****78", MaskPatterns.maskTextField("12345678"));
        assertEquals("1*****89", MaskPatterns.maskTextField("123456789"));
        assertEquals("12*****90", MaskPatterns.maskTextField("1234567890"));
        assertEquals("12*****01", MaskPatterns.maskTextField("12345678901"));
        assertEquals("12*****12", MaskPatterns.maskTextField("123456789012"));
        assertEquals("12*****123", MaskPatterns.maskTextField("1234567890123"));
        assertEquals("12*****234", MaskPatterns.maskTextField("12345678901234"));
        assertEquals("12*****2345", MaskPatterns.maskTextField("123456789012345"));
        assertEquals("123*****456", MaskPatterns.maskTextField("1234567890233456"));
        assertEquals("123*****567", MaskPatterns.maskTextField("12345678901244567"));
        assertEquals("123*****5678", MaskPatterns.maskTextField("123456789012345678"));
        assertEquals("123*****6789", MaskPatterns.maskTextField("1234567890123456789"));
    }

}
