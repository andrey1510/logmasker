package com.logtest.masker.utils;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.regex.Pattern;

public class MaskPatternsAdditional {

    private static final LocalDate LOCALDATE_REPLACEMENT = LocalDate.of(0, 1, 1);
    private static final OffsetDateTime OFFSETDATETIME_REPLACEMENT = OffsetDateTime.of(1, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC);

    private static final String ASTERISK = "*";
    private static final String TWO_ASTERISKS = "**";
    private static final String THREE_ASTERISKS = "***";
    private static final String TEN_ASTERISKS = "**********";
    private static final String AT_SIGN = "@";

    private static final Pattern EMAIL_PATTERN = Pattern.compile("(^[^@]*)@(.*$)");
    private static final Pattern SURNAME_PATTERN = Pattern.compile("^(.)(.*)$");
    private static final Pattern FULL_NAME_PATTERN = Pattern.compile("^(\\S+)(\\s+.*)?$");
    private static final Pattern AUTH_DATA_PATTERN = Pattern.compile("^.+$");

    public static String maskEmail(String source) {
        return EMAIL_PATTERN.matcher(source.trim())
            .replaceAll(mr -> String.format("%s%s%s", ASTERISK.repeat(mr.group(1).length()), AT_SIGN, mr.group(2)));
    }

    public static String maskSurname(String source) {
        return SURNAME_PATTERN.matcher(source.trim())
            .replaceAll(mr -> {
                String firstChar = mr.group(1);
                return mr.group(2).isEmpty() ? firstChar : String.format("%s%s", firstChar, THREE_ASTERISKS);
            });
    }

    public static String maskFullName(String source) {
        return FULL_NAME_PATTERN.matcher(source.trim())
            .replaceAll(mr -> {
                String maskedSurname = maskSurname(mr.group(1));
                return mr.group(2) == null ? maskedSurname : String.format("%s%s", maskedSurname, mr.group(2));
            });
    }

    public static String maskFullAddress(String source) {
        String trimmed = source.trim();
        int length = trimmed.length();

        if (length <= 10)  return ASTERISK.repeat(length);

        return new StringBuilder(length)
            .append(trimmed, 0, length - 10)
            .append(TEN_ASTERISKS)
            .toString();
    }

    public static String maskAuthData(String source) {
        return AUTH_DATA_PATTERN.matcher(source.trim())
            .replaceAll(THREE_ASTERISKS);
    }

    public static LocalDate maskLocalDate(LocalDate date) {
        return LOCALDATE_REPLACEMENT;
    }

    public static OffsetDateTime maskOffsetDateTime(OffsetDateTime dateTime) {
        return OFFSETDATETIME_REPLACEMENT;
    }

    public static String maskPassportSeriesAndNumber(String source) {
        String trimmed = source.trim();

        if (!trimmed.matches("\\d+"))
            return maskTextField(trimmed);

        String prefix = trimmed.substring(0, 2);
        String suffix = trimmed.substring(trimmed.length() - 3);
        String middleMask = ASTERISK.repeat(trimmed.length() - 5);

        return String.format("%s%s%s", prefix, middleMask, suffix);
    }

    public static String maskTextField(String source) {
        String trimmed = source.trim();
        int length = trimmed.length();
        int maskCount = (int) Math.ceil(length * 0.6);

        if (length <= 4) {
            return maskFromOneToFourText(trimmed, length);
        } else if (length <= 9) {
            return maskFromFivetoNine(trimmed, length, maskCount);
        } else if (length <= 15) {
            return maskFromTenToFifteen(trimmed, length, maskCount);
        } else {
            return maskMoreThanFifteen(trimmed, length, maskCount);
        }
    }

    private static String maskFromOneToFourText(String source, int length) {
        return switch (length) {
            case 1 -> ASTERISK;
            case 2 -> source.charAt(0) + ASTERISK;
            case 3 -> source.charAt(0) + TWO_ASTERISKS;
            case 4 -> source.charAt(0) + THREE_ASTERISKS;
            default -> source;
        };
    }

    private static String maskFromFivetoNine(String source, int length, int maskCount) {
        int left = 1;
        int right = length - left - maskCount;

        return buildMaskedText(source, length, maskCount, left, right);
    }

    private static String maskFromTenToFifteen(String source, int length, int maskCount) {
        int left = 2;
        int right = length - left - maskCount;

        return buildMaskedText(source, length, maskCount, left, right);
    }

    private static String maskMoreThanFifteen(String source, int length, int maskCount) {

        int unmasked = length - maskCount;
        int left, right;

        if (unmasked % 2 == 0) {
            left = unmasked / 2;
            right = left;
        } else {
            left = unmasked / 2;
            right = unmasked - left;
        }

        int[] bounds = new int[]{left, right};

        return buildMaskedText(source, length, maskCount, bounds[0], bounds[1]);
    }

    private static String buildMaskedText(String source, int length, int maskCount, int left, int right) {

        StringBuilder result = new StringBuilder(length);

        if (left == 1) {
            result.append(source.charAt(0));
        } else {
            result.append(source, 0, left);
        }

        result.append(ASTERISK.repeat(maskCount));

        if (right > 0) {
            result.append(source.substring(length - right));
        }

        return result.toString();
    }
}
