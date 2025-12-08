package com.logtest;

import com.logtest.masker.utils.MaskPatterns;
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

        assertEquals("12*******5220", MaskPatterns.maskOgrnulOrOgrnip("1215573935220"));
        assertEquals("12*********2345", MaskPatterns.maskOgrnulOrOgrnip("123456789012345"));
        assertEquals("64540", MaskPatterns.maskOgrnulOrOgrnip("64540"));
        assertEquals("6454011111111111111111111", MaskPatterns.maskOgrnulOrOgrnip("6454011111111111111111111"));
        assertEquals("не огрн", MaskPatterns.maskOgrnulOrOgrnip("не огрн"));
        assertEquals("", MaskPatterns.maskOgrnulOrOgrnip(""));

    }

    @Test
    void maskSurname_test() {
        assertEquals("П***", MaskPatterns.maskSurname("Петров"));
        assertEquals("П***", MaskPatterns.maskSurname("Прокофьев-Северский"));
        assertEquals("д***", MaskPatterns.maskSurname("д'Альбер"));
        assertEquals("д***", MaskPatterns.maskSurname("де Ореллана"));
        assertEquals("И", MaskPatterns.maskSurname("И"));
        assertEquals("", MaskPatterns.maskSurname(""));
    }

    @Test
    void maskEmail_test() {
        assertEquals("****@mail.com", MaskPatterns.maskEmail("test@mail.com"));
        assertEquals("@mail.com", MaskPatterns.maskEmail("@mail.com"));
        assertEquals("******@mail.com", MaskPatterns.maskEmail("test.m@mail.com"));
        assertEquals("", MaskPatterns.maskEmail(""));
        assertEquals("test", MaskPatterns.maskEmail("test"));
    }

    @Test
    void maskFullName_test() {
        assertEquals("И***", MaskPatterns.maskFullName("Иванов"));
        assertEquals("П*** Иван Иванович", MaskPatterns.maskFullName("Прокофьев-Северский Иван Иванович"));
        assertEquals("И Ли Сын", MaskPatterns.maskFullName("И Ли Сын"));
        assertEquals("", MaskPatterns.maskFullName(""));
    }

    @Test
    void maskFullAddress_test() {
        assertEquals("Москва, ул. Лес**********", MaskPatterns.maskFullAddress("Москва, ул. Лесная, д. 15"));
        assertEquals("Санкт**********", MaskPatterns.maskFullAddress("Санкт-Петербург"));
        assertEquals("******", MaskPatterns.maskFullAddress("Москва"));
        assertEquals("", MaskPatterns.maskFullAddress(""));
    }

    @Test
    void maskAuthData_test() {
        assertEquals("***", MaskPatterns.maskAuthData("ASD 452345"));
        assertEquals("***", MaskPatterns.maskAuthData("52"));
        assertEquals("***", MaskPatterns.maskAuthData("333-666"));
        assertEquals("", MaskPatterns.maskAuthData(""));
    }

    @Test
    void maskTextField_test() {

        assertEquals("", MaskPatterns.maskTextField(""));
        assertEquals("*", MaskPatterns.maskTextField("1"));
        assertEquals("1*", MaskPatterns.maskTextField("12"));
        assertEquals("1**", MaskPatterns.maskTextField("123"));
        assertEquals("1***", MaskPatterns.maskTextField("1234"));
        assertEquals("1***5", MaskPatterns.maskTextField("12345"));
        assertEquals("1****6", MaskPatterns.maskTextField("123456"));
        assertEquals("1*****7", MaskPatterns.maskTextField("1234567"));
        assertEquals("1*****78", MaskPatterns.maskTextField("12345678"));
        assertEquals("1******89", MaskPatterns.maskTextField("123456789"));
        assertEquals("12******90", MaskPatterns.maskTextField("1234567890"));
        assertEquals("12*******09", MaskPatterns.maskTextField("12345678909"));
        assertEquals("12********98", MaskPatterns.maskTextField("123456789098"));
        assertEquals("12********987", MaskPatterns.maskTextField("1234567890987"));
        assertEquals("12*********876", MaskPatterns.maskTextField("12345678909876"));
        assertEquals("12*********8765", MaskPatterns.maskTextField("123456789098765"));
        assertEquals("123**********654", MaskPatterns.maskTextField("1234567890987654"));
        assertEquals("123***********543", MaskPatterns.maskTextField("12345678909876543"));
        assertEquals("123***********3234", MaskPatterns.maskTextField("123456789876543234"));
        assertEquals("123************2123", MaskPatterns.maskTextField("1234567898765432123"));
        assertEquals("1234************1238", MaskPatterns.maskTextField("12345678987654321238"));
        assertEquals("1234*************2389", MaskPatterns.maskTextField("123456789876543212389"));

        assertEquals(
            "У попа была с****************************************, он ее убил.",
            MaskPatterns.maskTextField("У попа была собака, он ее любил. Она съела кусок мяса, он ее убил.")
        );
    }

    @Test
    void maskPassportSeriesAndNumberField_test() {
        assertEquals("55*******33", MaskPatterns.maskPassportSeriesAndNumber("5563 456333"));
        assertEquals("55*******33", MaskPatterns.maskPassportSeriesAndNumber("5563-456333"));
        assertEquals("55*******33", MaskPatterns.maskPassportSeriesAndNumber("5563.456333"));
        assertEquals("55*****333", MaskPatterns.maskPassportSeriesAndNumber("5563456333"));
        assertEquals("", MaskPatterns.maskPassportSeriesAndNumber(""));
    }
}
