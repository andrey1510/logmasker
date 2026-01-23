package com.logtest;

import com.logtest.masker.Masker;
import com.logtest.testData.TestDataForToString;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MaskerMaskToStringTests extends TestDataForToString {

    @Test
    void maskToString_test() {
        assertEquals(DTO_MASKED, Masker.maskToString(createDto()));
    }

    @Test
    void maskToStringWithOverride_test() {
        assertEquals(DTO_MASKED, Masker.maskToStringWithOverride(createDto()));
        assertEquals(DTO_NO_TO_STRING_MASKED, Masker.maskToStringWithOverride(createDtoNoToString()));
    }

    @Test
    void maskToString_testFlaws() {
        assertEquals(DTO_WITH_FLAWS_MASKED, Masker.maskToString(createDtoWithFlaws()));
    }

    @Test
    void maskToStringWithOverride_testFlaws() {
        assertEquals(DTO_WITH_FLAWS_MASKED, Masker.maskToStringWithOverride(createDtoWithFlaws()));
    }

    @Test
    void maskToStringWithOverride_testOverride() {
        assertEquals(DTO_TO_STRING_OVERRIDE, Masker.maskToStringWithOverride(createDtoToStringOverride()));
    }

}
