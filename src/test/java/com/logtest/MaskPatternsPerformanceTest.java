package com.logtest;

import com.logtest.masker.patterns.MaskPatterns;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;


class MaskPatternsPerformanceTest {

    private List<String> testEmails;
    private static final int NUMBER_OF_ITERATIONS = 1_000_000;

    @BeforeEach
    void setUp() {
        testEmails = generateTestEmails(NUMBER_OF_ITERATIONS);
    }

    private List<String> generateTestEmails(int count) {
        List<String> emails = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            emails.add(String.format("user%d@domain%d.com", i, i % 100));
        }
        return emails;
    }

    @Test
    @Timeout(value = 100, unit = TimeUnit.SECONDS)
    void testMaskEmail() {
        int iterations = 10;
        int batchSize = 1000000;

        long totalTime = 0;
        long minTime = Long.MAX_VALUE;
        long maxTime = Long.MIN_VALUE;

        for (int i = 0; i < iterations; i++) {
            List<String> batch = generateTestEmails(batchSize);

            long start = System.nanoTime();
            for (String email : batch) {
                MaskPatterns.maskEmail(email);
            }
            long end = System.nanoTime();

            long duration = (end - start) / 10000000;
            totalTime += duration;
            minTime = Math.min(minTime, duration);
            maxTime = Math.max(maxTime, duration);

            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        System.out.println("\n=== Статистика производительности ===");
        System.out.println("Итераций: " + iterations + " × " + batchSize + " операций");
        System.out.println("Общее время: " + totalTime + " мс");
        System.out.println("Среднее время: " + (totalTime / iterations) + " мс");
        System.out.println("Минимальное время: " + minTime + " мс");
        System.out.println("Максимальное время: " + maxTime + " мс");
        System.out.println("Среднее время на операцию: " +
            ((double) totalTime / (iterations * batchSize)) + " мс");
    }
}
