package com.logtest;

import com.logtest.masker.Masker;
import com.logtest.testData.TestDataForToString;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MaskerMaskToStringTests extends TestDataForToString {

    @Test
    void maskToString_test() {
        assertEquals(createDto(), Masker.maskToString(createDto()));
    }

    @Test
    void maskToString_testFlaws() {
        assertEquals(createDtoWithFlaws(), Masker.mask(createDtoWithFlaws()));
        assertEquals(createDtoWithFlaws(), Masker.maskToString(createDtoWithFlaws()));
    }

    @Test
    void maskToStringWithOverride_test() {
        assertEquals(DTO_NO_TO_STRING_MASKED, Masker.maskToStringWithOverride(createDtoNoToString()));
    }

}
