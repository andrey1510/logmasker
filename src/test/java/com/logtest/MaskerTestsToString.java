package com.logtest;

import com.logtest.masker.Masker;
import com.logtest.testData.TestDataForToString;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MaskerTestsToString extends TestDataForToString {


    @Test
    void maskToString_test() {
        assertEquals(createUpperLevelDto(), Masker.maskToString(createUpperLevelDto()));
    }

    @Test
    void maskToString_testNoToStringOverride() {
        assertEquals(createUpperLevelDtoNoToStringOverride(),
            Masker.maskToStringWithOverride(createUpperLevelDtoNoToStringOverride()));
    }

}
