package com.logtest;

import com.logtest.masker.utils.MaskPatterns;
import com.logtest.masker.utils.MaskPatternsAdditional;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MailPatternsTests {

    @Test
    void maskInn_test() {

        assertEquals("64*****632", MaskPatterns.maskInn("6454093632"));
        assertEquals("64*****11472", MaskPatterns.maskInn("642125911472"));
        assertEquals("64*****632", MaskPatterns.maskInn(" 6454093632 "));
        assertEquals("64*****11472", MaskPatterns.maskInn(" 642125911472 "));
        assertEquals("64540", MaskPatterns.maskInn("64540"));
        assertEquals("6454011111111111111111111", MaskPatterns.maskInn("6454011111111111111111111"));
        assertEquals("не инн", MaskPatterns.maskInn("не инн"));
        assertEquals("", MaskPatterns.maskInn(""));

    }


    @Test
    void maskKpp_test() {

        assertEquals("64******3", MaskPatterns.maskKpp("645401003"));
        assertEquals("64540", MaskPatterns.maskKpp("64540"));
        assertEquals("6454011111111111111111111", MaskPatterns.maskKpp("6454011111111111111111111"));
        assertEquals("не кпп", MaskPatterns.maskKpp("не кпп"));
        assertEquals("", MaskPatterns.maskKpp(""));

    }

    @Test
    void maskOkpo_test() {

        assertEquals("00*****5", MaskPatterns.maskOkpo("00002335"));
        assertEquals("0002*****9", MaskPatterns.maskOkpo("0002870479"));
        assertEquals("64540", MaskPatterns.maskOkpo("64540"));
        assertEquals("6454011111111111111111111", MaskPatterns.maskOkpo("6454011111111111111111111"));
        assertEquals("не окпо", MaskPatterns.maskOkpo("не окпо"));
        assertEquals("", MaskPatterns.maskOkpo(""));

    }

    @Test
    void maskOgrnulOrOgrnip_test() {

        assertEquals("12*******5220", MaskPatterns.maskOgrnUlOrOgrnIp("1215573935220"));
        assertEquals("12*********2345", MaskPatterns.maskOgrnUlOrOgrnIp("123456789012345"));
        assertEquals("64540", MaskPatterns.maskOgrnUlOrOgrnIp("64540"));
        assertEquals("6454011111111111111111111", MaskPatterns.maskOgrnUlOrOgrnIp("6454011111111111111111111"));
        assertEquals("не огрн", MaskPatterns.maskOgrnUlOrOgrnIp("не огрн"));
        assertEquals("", MaskPatterns.maskOgrnUlOrOgrnIp(""));

    }

    @Test
    void maskSurname_test() {
        assertEquals("П***", MaskPatternsAdditional.maskSurname("Петров"));
        assertEquals("П***", MaskPatternsAdditional.maskSurname("Прокофьев-Северский"));
        assertEquals("д***", MaskPatternsAdditional.maskSurname("д'Альбер"));
        assertEquals("д***", MaskPatternsAdditional.maskSurname("де Ореллана"));
        assertEquals("И", MaskPatternsAdditional.maskSurname("И"));
        assertEquals("", MaskPatternsAdditional.maskSurname(""));
    }

    @Test
    void maskEmail_test() {
        assertEquals("****@mail.com", MaskPatternsAdditional.maskEmail("test@mail.com"));
        assertEquals("@mail.com", MaskPatternsAdditional.maskEmail("@mail.com"));
        assertEquals("******@mail.com", MaskPatternsAdditional.maskEmail("test.m@mail.com"));
        assertEquals("", MaskPatternsAdditional.maskEmail(""));
        assertEquals("test", MaskPatternsAdditional.maskEmail("test"));
    }

    @Test
    void maskFullName_test() {
        assertEquals("И***", MaskPatternsAdditional.maskFullName("Иванов"));
        assertEquals("П*** Иван Иванович", MaskPatternsAdditional.maskFullName("Прокофьев-Северский Иван Иванович"));
        assertEquals("И Ли Сын", MaskPatternsAdditional.maskFullName("И Ли Сын"));
        assertEquals("", MaskPatternsAdditional.maskFullName(""));
    }

    @Test
    void maskFullAddress_test() {
        assertEquals("Москва, ул. Лес**********", MaskPatternsAdditional.maskFullAddress("Москва, ул. Лесная, д. 15"));
        assertEquals("Санкт**********", MaskPatternsAdditional.maskFullAddress("Санкт-Петербург"));
        assertEquals("******", MaskPatternsAdditional.maskFullAddress("Москва"));
        assertEquals("", MaskPatternsAdditional.maskFullAddress(""));
    }

    @Test
    void maskAuthData_test() {
        assertEquals("***", MaskPatternsAdditional.maskAuthData("ASD 452345"));
        assertEquals("***", MaskPatternsAdditional.maskAuthData("52"));
        assertEquals("***", MaskPatternsAdditional.maskAuthData("333-666"));
        assertEquals("", MaskPatternsAdditional.maskAuthData(""));
    }

    @Test
    void maskTextField_test() {

        assertEquals("", MaskPatternsAdditional.maskTextField(""));
        assertEquals("*", MaskPatternsAdditional.maskTextField("1"));
        assertEquals("1*", MaskPatternsAdditional.maskTextField("12"));
        assertEquals("1**", MaskPatternsAdditional.maskTextField("123"));
        assertEquals("1***", MaskPatternsAdditional.maskTextField("1234"));
        assertEquals("1***5", MaskPatternsAdditional.maskTextField("12345"));
        assertEquals("1****6", MaskPatternsAdditional.maskTextField("123456"));
        assertEquals("1*****7", MaskPatternsAdditional.maskTextField("1234567"));
        assertEquals("1*****78", MaskPatternsAdditional.maskTextField("12345678"));
        assertEquals("1******89", MaskPatternsAdditional.maskTextField("123456789"));
        assertEquals("12******90", MaskPatternsAdditional.maskTextField("1234567890"));
        assertEquals("12*******09", MaskPatternsAdditional.maskTextField("12345678909"));
        assertEquals("12********98", MaskPatternsAdditional.maskTextField("123456789098"));
        assertEquals("12********987", MaskPatternsAdditional.maskTextField("1234567890987"));
        assertEquals("12*********876", MaskPatternsAdditional.maskTextField("12345678909876"));
        assertEquals("12*********8765", MaskPatternsAdditional.maskTextField("123456789098765"));
        assertEquals("123**********654", MaskPatternsAdditional.maskTextField("1234567890987654"));
        assertEquals("123***********543", MaskPatternsAdditional.maskTextField("12345678909876543"));
        assertEquals("123***********3234", MaskPatternsAdditional.maskTextField("123456789876543234"));
        assertEquals("123************2123", MaskPatternsAdditional.maskTextField("1234567898765432123"));
        assertEquals("1234************1238", MaskPatternsAdditional.maskTextField("12345678987654321238"));
        assertEquals("1234*************2389", MaskPatternsAdditional.maskTextField("123456789876543212389"));

        assertEquals(
            "У попа была с****************************************, он ее убил.",
            MaskPatternsAdditional.maskTextField("У попа была собака, он ее любил. Она съела кусок мяса, он ее убил.")
        );
    }

    @Test
    void maskPassportSeriesAndNumberField_test() {
        assertEquals("55*******33", MaskPatternsAdditional.maskPassportSeriesAndNumber("5563 456333"));
        assertEquals("55*******33", MaskPatternsAdditional.maskPassportSeriesAndNumber("5563-456333"));
        assertEquals("55*******33", MaskPatternsAdditional.maskPassportSeriesAndNumber("5563.456333"));
        assertEquals("55*****333", MaskPatternsAdditional.maskPassportSeriesAndNumber("5563456333"));
        assertEquals("", MaskPatternsAdditional.maskPassportSeriesAndNumber(""));
    }
}
