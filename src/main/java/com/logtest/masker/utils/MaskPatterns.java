package com.logtest.masker.utils;

public class MaskPatterns {

    private static final String NONSTANDARD_VALUE_MASK_REPLACEMENT = "*****";
    private static final String ASTERISK = "*";
    private static final String AT_SIGN = "@";
    private static final String THREE_ASTERISKS = "***";
    private static final String SPACE = " ";
    private static final String HYPHEN = "-";

    public static String maskJwtTykApiKeyIpAddress(String source) {
        return THREE_ASTERISKS;
    }

    public static String maskName(String source) {
        String trimmed = source.trim();

        if (trimmed.contains(HYPHEN)) {
            return maskCompoundNameWithDelimiter(trimmed, HYPHEN);
        } else if (trimmed.contains(SPACE)) {
            String[] parts = trimmed.split(SPACE);
            return parts.length > 2 ? maskFio(parts) : maskCompoundNameWithDelimiter(trimmed, SPACE);
        } else {
            return maskSimpleName(trimmed);
        }
    }

    private static String maskSimpleName(String name) {
        return switch (name.length()) {
            case 0, 1, 2, 3, 4 -> ASTERISK.repeat(name.length());
            case 5, 6, 7 -> maskInRange(name, 1, name.length());
            default -> String.format("%s%s%s",
                name.charAt(0),
                THREE_ASTERISKS,
                name.substring(name.length() - 2));
        };
    }

    private static String maskCompoundNameWithDelimiter(String name, String delimiter) {
        String[] parts = name.split(delimiter.equals(HYPHEN) ? HYPHEN : SPACE);

        if (parts.length != 2) return NONSTANDARD_VALUE_MASK_REPLACEMENT;

        return String.format("%s%s%s",
            maskSimpleNameCompound(parts[0]),
            delimiter,
            maskSimpleNameCompound(parts[1]));
    }

    private static String maskSimpleNameCompound(String part) {
        return switch (part.length()) {
            case 0 -> "";
            case 1 -> String.format("%s%s", part, THREE_ASTERISKS);
            default -> String.format("%s%s", part.charAt(0), THREE_ASTERISKS);
        };
    }

    private static String maskFio(String[] parts) {
        if (parts.length < 2) return NONSTANDARD_VALUE_MASK_REPLACEMENT;

        StringBuilder result = new StringBuilder(maskSimpleName(parts[0]));

        for (int i = 1; i < parts.length; i++) {
            result.append(SPACE).append(parts[i]);
        }

        return result.toString();
    }

    public static String maskInn(String source) {
        String trimmed = source.trim();
        return switch (trimmed.length()) {
            case 10, 12 -> maskInRange(trimmed, 2, 7);
            default -> source;
        };
    }

    public static String maskKpp(String source) {
        String trimmed = source.trim();
        return switch (trimmed.length()) {
            case 9 -> maskInRange(trimmed, 2, 8);
            default -> source;
        };
    }

    public static String maskOkpo(String source) {
        String trimmed = source.trim();
        return switch (trimmed.length()) {
            case 8 -> maskInRange(trimmed, 2, 7);
            case 10 -> maskInRange(trimmed, 4, 9);
            default -> source;
        };
    }

    public static String maskOgrnUlOrOgrnIp(String source) {
        String trimmed = source.trim();
        return switch (trimmed.length()) {
            case 13 -> maskInRange(trimmed, 2, 9);
            case 15 -> maskInRange(trimmed, 2, 11);
            default -> source;
        };
    }

    public static String maskPhoneNumber(String source) {
        String trimmed = source.trim();
        int length = trimmed.length();

        return switch (length) {
            case 5, 6, 7 -> maskInRange(trimmed, 1, length - 1);
            case 8, 9 -> maskInRange(trimmed, 1, length - 2);
            case 10, 11, 12 -> maskInRange(trimmed, 2, length - 2);
            case 13, 14, 15 -> maskInRange(trimmed, 2, length - 3);
            default -> NONSTANDARD_VALUE_MASK_REPLACEMENT;
        };
    }

    public static String maskEmail(String source) {
        String trimmed = source.trim();

        int atIndex = trimmed.indexOf('@');
        if (atIndex <= 1 || atIndex == trimmed.length() - 1)
            return NONSTANDARD_VALUE_MASK_REPLACEMENT;

        int dotIndex = trimmed.substring(atIndex + 1).indexOf('.');
        if (dotIndex <= 0 || dotIndex == trimmed.substring(atIndex + 1).length() - 1)
            return NONSTANDARD_VALUE_MASK_REPLACEMENT;

        return String.format("%s%s%s%s",
            maskSequence(trimmed.substring(0, atIndex)),
            AT_SIGN,
            maskSequence(trimmed.substring(atIndex + 1).substring(0, dotIndex)),
            trimmed.substring(atIndex + 1).substring(dotIndex)
        );
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
