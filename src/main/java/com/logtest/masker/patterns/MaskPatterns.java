package com.logtest.masker.patterns;

import java.time.LocalDate;
import java.time.OffsetDateTime;

public class MaskPatterns {

    private static final String NONSTANDARD_VALUE_MASK = "*****";
    private static final String ASTERISK = "*";
    private static final String AT_SIGN = "@";
    private static final String TWO_ASTERISKS = "**";
    private static final String THREE_ASTERISKS = "***";
    private static final String FOUR_ASTERISKS = "****";
    private static final String FIVE_ASTERISKS = "*****";
    private static final String TEN_ASTERISKS = "**********";
    private static final String SPACE = " ";
    private static final String HYPHEN = "-";

    public static LocalDate maskLocalDate(LocalDate date) {
        return date.withYear(1);
    }

    public static OffsetDateTime maskOffsetDateTime(OffsetDateTime dateTime) {
        return dateTime.withYear(1);
    }
    /**
     * Перед началом маскировки метод обрабатывает строку, убирая пробелы из начала и конца строки. Если на входе пустая строка, или строка из пробелов, то она вернется в неизменном виде.
     * Метод вернет обработанную строку с маскировкой, при этом:
     *  1) если в обработанной строке меньше 10 символов, он заменится на звездочки по числу символов;
     *  2) если в обработанной строке  10 или больше символов, в нем 10 последних символов заменятся на звездочки.
     * Метод не валидирует символы и считает разделители (пробелы, дефисы и т.д.) обычными символами для замены.
     */
    public static String maskFullAddress(String source) {
        String trimmed = source.trim();

        if (trimmed.isEmpty()) {
            return source;
        } else if (trimmed.length() <= 10) {
            return ASTERISK.repeat(trimmed.length());
        } else {
            return trimmed.substring(0, trimmed.length() - 10) + TEN_ASTERISKS;
        }
    }

    /**
     * Перед началом маскировки метод обрабатывает строку, убирая пробелы из начала и конца строки. Если на входе пустая строка, или строка из пробелов, то она вернется в неизменном виде.
     * Метод вернет обработанную строку с маскировкой, при этом:
     *  1) если в обработанной строке 1 символ, он заменится на "*****";
     *  2) если в обработанной строке 2-4 символа, то останется первый символ, а остальные заменятся на "*****";
     *  3) если в обработанной строке 5-9 символов, то 60% символов, начиная со 2-го, заменятся на "*****";
     *  4) если в обработанной строке 10-15 символов, то 60% символов, начиная с 3-го, заменятся на "*****";
     *  5) если в обработанной строке больше 16 символов, то 60% символов, начиная с символа определяемого по формуле (length - charactersToMask)/2, заменятся на "*****";
     * Метод не валидирует символы и считает разделители (пробелы, дефисы и т.д.) обычными символами для замены.
     */
    public static String maskTextField(String source) {
        String trimmed = source.trim();
        int length = trimmed.length();

        int quantityToMask = (int) Math.ceil(length * 0.6);

        return switch (length) {
            case 0 -> source;
            case 1 -> FIVE_ASTERISKS;
            case 2, 3, 4 -> trimmed.charAt(0) + FIVE_ASTERISKS;
            case 5, 6, 7, 8, 9 -> trimmed.charAt(0) + FIVE_ASTERISKS + trimmed.substring(1 + quantityToMask);
            case 10, 11, 12, 13, 14, 15 -> trimmed.substring(0, 2) + FIVE_ASTERISKS
                + trimmed.substring(2 + quantityToMask);
            default -> trimmed.substring(0, (length - quantityToMask) / 2) + FIVE_ASTERISKS
                + trimmed.substring((length - quantityToMask) / 2 + quantityToMask);
        };
    };

    /**
     * Перед началом маскировки метод обрабатывает строку, убирая пробелы из начала и конца строки. Если на входе пустая строка, или строка из пробелов, то она вернется в неизменном виде.
     * Метод вернет обработанную строку с маскировкой, при этом:
     *  1) если в обработанной строке было больше 10 символов, 6 символов начиная с 5 символа справа заменятся звездочками.
     *  2) если в обработанной строке было меньше 10 символов, вернется "*****".
     * Метод не валидирует символы и считает разделители (пробелы, дефисы и т.д.) обычными символами для замены.
     */
    public static String maskConfidentialNumber(String source) {
        String trimmed = source.trim();
        if (trimmed.isEmpty()) {
            return source;
        } else if (trimmed.length() < 10) {
            return NONSTANDARD_VALUE_MASK;
        } else {
            return maskInRange(trimmed, trimmed.length() - 10, trimmed.length() - 4);
        }
    }

    /**
     * Перед началом маскировки метод обрабатывает строку, убирая пробелы из начала и конца строки. Если на входе пустая строка, или строка из пробелов, то она вернется в неизменном виде.
     * Метод вернет обработанную строку с маскировкой, при этом все символы заменятся звездочками.
     * Метод не валидирует символы и считает разделители (пробелы, дефисы и т.д.) обычными символами для замены.
     */
    public static String maskPin(String source) {
        String trimmed = source.trim();
        return switch (trimmed.length()) {
            case 0 -> source;
            default -> ASTERISK.repeat(trimmed.length());
        };
    }

    /**
     * Перед началом маскировки метод обрабатывает строку, убирая пробелы из начала и конца строки. Если на входе пустая строка, или строка из пробелов, то она вернется в неизменном виде.
     * Метод вернет обработанную строку с маскировкой, при этом:
     *  1) останутся первые 6 и последние 4 символа, остальные заменятся на звездочки,
     *  2) если в обработанной строке было меньше 11 символов, вернется "*****".
     * Метод не валидирует символы и считает разделители (пробелы, дефисы и т.д.) обычными символами для замены.
     */
    public static String maskPan(String source) {
        String trimmed = source.trim();
        if (trimmed.isEmpty()) {
            return source;
        } else if (trimmed.length() < 11) {
            return NONSTANDARD_VALUE_MASK;
        } else {
            return maskInRange(trimmed, 6, trimmed.length() - 4);
        }
    }

    /**
     * Перед началом маскировки метод обрабатывает строку, убирая пробелы из начала и конца строки. Если на входе пустая строка, или строка из пробелов, то она вернется в неизменном виде.
     * Метод вернет обработанную строку с маскировкой, при этом:
     *  1) последние 2 символа заменятся на звездочки, остальные останутся,
     *  2) если в обработанной строке было меньше 3 символов, вернется "****".
     * Метод не валидирует символы и считает разделители (пробелы, дефисы и т.д.) обычными символами для замены.
     */
    public static String maskPassportSeries(String source) {
        String trimmed = source.trim();
        return switch (trimmed.length()) {
            case 0 -> source;
            case 1, 2 -> FOUR_ASTERISKS;
            default -> trimmed.substring(0, trimmed.length() - 2) + TWO_ASTERISKS;
        };
    }

    /**
     * Перед началом маскировки метод обрабатывает строку, убирая пробелы из начала и конца строки. Если на входе пустая строка, или строка из пробелов, то она вернется в неизменном виде.
     * Метод вернет обработанную строку с маскировкой, при этом:
     *  1) первые 3 символа заменятся на звездочки, остальные останутся,
     *  2) если в обработанной строке было меньше 4 символов, вернется "***".
     * Метод не валидирует символы и считает разделители (пробелы, дефисы и т.д.) обычными символами для замены.
     */
    public static String maskPassportNumber(String source) {
        String trimmed = source.trim();
        return switch (trimmed.length()) {
            case 0 -> source;
            case 1, 2, 3 -> THREE_ASTERISKS;
            default -> THREE_ASTERISKS + trimmed.substring(3);
        };
    }

    /**
     * Перед началом маскировки метод обрабатывает строку, убирая пробелы из начала и конца строки. Если на входе пустая строка, или строка из пробелов, то она вернется в неизменном виде.
     * Метод вернет обработанную строку с маскировкой, при этом:
     *  1) все кроме 2 первых и 3 последних символов заменится на звездочки,
     *  2) если в обработанной строке было меньше 6 символов, вернется "*****".
     * Метод не валидирует символы и считает разделители (пробелы, дефисы и т.д.) обычными символами для замены.
     */
    public static String maskPassport(String source) {
        String trimmed = source.trim();
        return switch (trimmed.length()) {
            case 0 -> source;
            case 1, 2, 3, 4, 5 -> NONSTANDARD_VALUE_MASK;
            default -> maskInRange(trimmed, 2, trimmed.length() - 3);
        };
    }

    /**
     * Перед началом маскировки метод обрабатывает строку, убирая пробелы из начала и конца строки. Если на входе пустая строка, или строка из пробелов, то она вернется в неизменном виде.
     * Метод вернет обработанную строку с маскировкой, при этом:
     *  1) если в обработанной строке было 4 символа или больше, последние 3 символа заменятся на звездочки,
     *  2) если в обработанной строке было меньше 4 символов, вернется "***".
     */
    public static String maskIssuerCode(String source) {
        String trimmed = source.trim();
        return switch (trimmed.length()) {
            case 0 -> source;
            case 1, 2, 3 -> THREE_ASTERISKS;
            default -> trimmed.substring(0, trimmed.length() - 3) + THREE_ASTERISKS;
        };
    }

    /**
     * Перед началом маскировки метод обрабатывает строку, убирая пробелы из начала и конца строки. Если на входе пустая строка, или строка из пробелов, то она вернется в неизменном виде.
     * Метод вернет обработанную строку с маскировкой, при этом:
     *  1) если в обработанной строке было 4 символа или меньше, вернется "****",
     *  2) если в обработанной строке было больше 4 символов, середина строки будет заменена "*****".
     */
    public static String maskIssuerName(String source) {
        String trimmed = source.trim();
        return switch (trimmed.length()) {
            case 0 -> source;
            case 1, 2, 3, 4 -> FOUR_ASTERISKS;
            default -> trimmed.substring(0, trimmed.length() / 4 - 1) + SPACE + FIVE_ASTERISKS + SPACE
                + trimmed.substring(trimmed.length() / 4 * 3);
        };
    }

    /**
     * Перед началом маскировки метод обрабатывает строку, убирая пробелы из начала и конца строки. Если на входе пустая строка, или строка из пробелов, то она вернется в неизменном виде.
     * Если в строке имеются символы помимо пробелов, то метод вернет первый символ и "***" (даже если в обработанной строке 1 символ).
     */
    public static String maskOtherDulSeries(String source) {
        String trimmed = source.trim();
        return switch (trimmed.length()) {
            case 0 -> source;
            default -> trimmed.charAt(0) + THREE_ASTERISKS;
        };
    }

    /**
     * Перед началом маскировки метод обрабатывает строку, убирая пробелы из начала и конца строки. Если на входе пустая строка, или строка из пробелов, то она вернется в неизменном виде.
     * Если в строке имеются символы помимо пробелов, то метод вернет "****" и последний символ (даже если в обработанной строке 1 символ).
     */
    public static String maskOtherDulNumber(String source) {
        String trimmed = source.trim();
        return switch (trimmed.length()) {
            case 0 -> source;
            default -> FOUR_ASTERISKS + trimmed.charAt(trimmed.length() - 1);
        };
    }

    /**
     * Перед началом маскировки метод обрабатывает строку, убирая пробелы из начала и конца строки. Если на входе пустая строка, или строка из пробелов, то она вернется в неизменном виде.
     * Если в строке имеются символы помимо пробелов, то метод вернет "***".
     */
    public static String maskBalance(String source) {
        String trimmed = source.trim();
        return switch (trimmed.length()) {
            case 0 -> source;
            default -> THREE_ASTERISKS;
        };
    }

    /**
     * Перед началом маскировки метод обрабатывает строку, убирая пробелы из начала и конца строки. Если на входе пустая строка, или строка из пробелов, то она вернется в неизменном виде.
     * Если в строке имеются символы помимо пробелов, то метод вернет "***".
     */
    public static String maskJwtTykApiKeyIpAddress(String source) {
        String trimmed = source.trim();
        return switch (trimmed.length()) {
            case 0 -> source;
            default -> THREE_ASTERISKS;
        };
    }

    /**
     * СНИЛС - уникальный номер из 11 чисел.
     * Перед началом маскировки метод обрабатывает строку, убирая пробелы из начала и конца строки. Если на входе пустая строка, или строка из пробелов, то она вернется в неизменном виде.
     * Если количество символов в обработанной строке правильное (т.е. 11 цифр или 14 цифр и разделителей), то метод вернет номер в котором все, кроме первых 2 и последних 2 символов, будет заменено звездочками.
     * Если количество символов в обработанной строке неправильное, то метод вернет "*****".
     */
    public static String maskSnils(String source) {
        String trimmed = source.trim();
        return switch (trimmed.length()) {
            case 0 -> source;
            case 11 -> maskInRange(trimmed, 2, 9);
            case 14 -> maskInRange(trimmed, 2, 12);
            default -> NONSTANDARD_VALUE_MASK;
        };
    }

    /**
     * Перед началом маскировки метод обрабатывает строку, убирая пробелы из начала и конца строки. Если на входе пустая строка, или строка из пробелов, то она вернется в неизменном виде.
     * Метод разделяет обработанную строку по словам (под словом понимается последовательность любых символы кроме пробела) с пробелом в качестве разделителя. Несколько пробелов подряд учитываются корректно и преобразуются в один.
     * Первое слово маскируется следующим образом:
     *  1) У составного слова через "-" у каждой из составных частей остается первый символ, остальное заменяется на "***". Поддерживаются слова из нескольких составных частей.
     *  2) У слова из 4 символов и меньше все символы заменяются звездочками.
     *  3) У слова из 5-7 (включительно) символов остается первый символ, остальное заменяется звездочками.
     *  4) У слова из больше 7 символов остается первый символ и 2 последних, остальное заменяется на "***".
     * Если в строке больше одного слова, остальные слова не маскируются.
     */
    public static String maskName(String source) {
        String trimmed = source.trim();
        if (trimmed.isEmpty()) return source;

        return buildResult(maskFirstName(trimmed.split("\\s+")[0]), trimmed.split("\\s+"));
    }

    private static String maskFirstName(String firstName) {
        if (firstName.contains(HYPHEN)) {
            return String.join(HYPHEN, maskCompoundParts(firstName.split(HYPHEN, -1)));
        } else {
            return maskSingleWord(firstName);
        }
    }

    private static String[] maskCompoundParts(String[] parts) {
        String[] maskedParts = new String[parts.length];
        for (int i = 0; i < parts.length; i++) {
            maskedParts[i] = parts[i].charAt(0) + ASTERISK.repeat(3);
        }
        return maskedParts;
    }

    private static String maskSingleWord(String word) {
        int length = word.length();
        return switch (length) {
            case 0, 1, 2, 3, 4 -> ASTERISK.repeat(length);
            case 5, 6, 7 -> word.charAt(0) + ASTERISK.repeat(length - 1);
            default -> word.charAt(0) + THREE_ASTERISKS + word.substring(length - 2);
        };
    }

    private static String buildResult(String maskedFirstName, String[] words) {
        if (words.length == 1) return maskedFirstName;

        StringBuilder result = new StringBuilder(maskedFirstName);
        for (int i = 1; i < words.length; i++) {
            result.append(SPACE).append(words[i]);
        }
        return result.toString();
    }

    /**
     * ИНН - уникальный номер из 10 (для юрлиц) или 12 (для физлиц) чисел.
     * Перед началом маскировки метод обрабатывает строку, убирая пробелы из начала и конца строки. Если на входе пустая строка, или строка из пробелов, то она вернется в неизменном виде.
     * Если количество символов в обработанной строке правильное (т.е. 10 или 12), то метод вернет обработанную строку с маскировкой, при этом:
     * 1) в случае 10 символов все, кроме первых 2 и последних 3 символов, будет заменено звездочками.
     * 2) в случае 12 символов все, кроме первых 4 и последних 5 символов, будет заменено звездочками.
     * Если количество символов в обработанной строке неправильное, то метод вернет "*****".
     */
    public static String maskInn(String source) {
        String trimmed = source.trim();
        return switch (trimmed.length()) {
            case 0 -> source;
            case 10, 12 -> maskInRange(trimmed, 2, 7);
            default -> NONSTANDARD_VALUE_MASK;
        };
    }

    /**
     * КПП - код из 9 чисел.
     * Перед началом маскировки метод обрабатывает строку, убирая пробелы из начала и конца строки. Если на входе пустая строка, или строка из пробелов, то она вернется в неизменном виде.
     * Если количество символов в обработанной строке правильное, то метод вернет обработанную строку с маскировкой, в которой:
     * все, кроме первых 2 и последнего символа, будет заменено звездочками.
     * Если количество символов в обработанной строке неправильное, то метод вернет "*****".
     */
    public static String maskKpp(String source) {
        String trimmed = source.trim();
        return switch (trimmed.length()) {
            case 0 -> source;
            case 9 -> maskInRange(trimmed, 2, 8);
            default -> NONSTANDARD_VALUE_MASK;
        };
    }

    /**
     * ОКПО - уникальный номер из 8 (для юрлиц) или 10 (для ИП) чисел.
     * Перед началом маскировки метод обрабатывает строку, убирая пробелы из начала и конца строки. Если на входе пустая строка, или строка из пробелов, то она вернется в неизменном виде.
     * Если количество символов в обработанной строке правильное (т.е. 8 или 10), то метод вернет обработанную строку с маскировкой, в которой:
     * 1) в случае 8 символов все, кроме первых 2 и последнего символа, будет заменено звездочками.
     * 2) в случае 10 символов все, кроме первых 4 и последнего символа, будет заменено звездочками.
     * Если количество символов в обработанной строке неправильное, то метод вернет "*****".
     */
    public static String maskOkpo(String source) {
        String trimmed = source.trim();
        return switch (trimmed.length()) {
            case 0 -> source;
            case 8 -> maskInRange(trimmed, 2, 7);
            case 10 -> maskInRange(trimmed, 4, 9);
            default -> NONSTANDARD_VALUE_MASK;
        };
    }

    /**
     * ОГРН - уникальный номер из 13 (для юрлиц - ОГРН) или 15 (для ИП - ОГРНИП) чисел.
     * Перед началом маскировки метод обрабатывает строку, убирая пробелы из начала и конца строки. Если на входе пустая строка, или строка из пробелов, то она вернется в неизменном виде.
     * Если количество символов в обработанной строке правильное (т.е. 13 или 15), то метод вернет обработанную строку с маскировкой, в которой:
     * все, кроме первых 2 и последних 4 символов, будет заменено звездочками.
     * Если количество символов в обработанной строке неправильное, то метод вернет "*****".
     */
    public static String maskOgrnUlOrOgrnIp(String source) {
        String trimmed = source.trim();
        return switch (trimmed.length()) {
            case 0 -> source;
            case 13 -> maskInRange(trimmed, 2, 9);
            case 15 -> maskInRange(trimmed, 2, 11);
            default -> NONSTANDARD_VALUE_MASK;
        };
    }

    /**
     * Перед началом маскировки метод обрабатывает строку, убирая пробелы из начала и конца строки. Если на входе пустая строка, или строка из пробелов, то она вернется в неизменном виде.
     * Если количество символов в обработанной строке 5-15 (включительно) символов, то метод вернет обработанную строку с маскировкой, в которой:
     * 1) когда 5-7 символов - все, кроме первого и последнего символа, будет заменено звездочками.
     * 2) когда 8-9 символов - все, кроме первого и 2 последних символов, будет заменено звездочками.
     * 3) когда 10-12 символов - все, кроме 2 первых и 2 последних символов, будет заменено звездочками.
     * 4) когда 13-15 символов - все, кроме 2 первых и 3 последних символов, будет заменено звездочками.
     * Если в обработанной строке другое количество символов, то метод вернет "*****".
     * Метод учитывает как символы любые символы (т.е. +, -, пробел и т.д.).
     */
    public static String maskPhoneNumber(String source) {
        String trimmed = source.trim();
        int length = trimmed.length();

        return switch (length) {
            case 0 -> source;
            case 5, 6, 7 -> maskInRange(trimmed, 1, length - 1);
            case 8, 9 -> maskInRange(trimmed, 1, length - 2);
            case 10, 11, 12 -> maskInRange(trimmed, 2, length - 2);
            case 13, 14, 15 -> maskInRange(trimmed, 2, length - 3);
            default -> NONSTANDARD_VALUE_MASK;
        };
    }

    /**
     * Перед началом маскировки метод обрабатывает строку, убирая пробелы из начала и конца строки. Если на входе пустая строка, или строка из пробелов, то она вернется в неизменном виде.
     * Если формат правильный (есть @ и доменная часть), то  метод вернет обработанную строку с маскировкой, в которой:
     * 1) в почтовой части все, кроме первого символа, будет заменено звездочками,
     * 2) в доменной части у почтового домена все кроме первого символа, будет заменено звездочками, остальные домены останутся.
     * Если формат неправильный, то метод вернет "*****".
     */
    public static String maskEmail(String source) {
        String trimmed = source.trim();
        if (trimmed.isEmpty()) return source;

        int atIndex = trimmed.indexOf('@');
        if (atIndex <= 1 || atIndex == trimmed.length() - 1)
            return NONSTANDARD_VALUE_MASK;

        int dotIndex = trimmed.substring(atIndex + 1).indexOf('.');
        if (dotIndex <= 0 || dotIndex == trimmed.substring(atIndex + 1).length() - 1)
            return NONSTANDARD_VALUE_MASK;

        return maskSequence(trimmed.substring(0, atIndex)) + AT_SIGN
            + maskSequence(trimmed.substring(atIndex + 1).substring(0, dotIndex))
            + trimmed.substring(atIndex + 1).substring(dotIndex);
    }

    private static String maskInRange(String str, int start, int end) {
        char[] chars = str.toCharArray();
        for (int i = start; i < end; i++) {
            chars[i] = '*';
        }
        return new String(chars);
    }

    private static String maskSequence(String str) {
        if (str.length() <= 1) {
            return str;
        } else {
            return str.charAt(0) + ASTERISK.repeat(str.length() - 1);
        }
    }
}
