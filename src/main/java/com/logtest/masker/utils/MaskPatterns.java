package com.logtest.masker.utils;

public class MaskPatterns {
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

    private static String maskInRange(String str, int start, int end) {
        char[] chars = str.toCharArray();
        for (int i = start; i < end; i++) {
            chars[i] = '*';
        }
        return new String(chars);
    }
}
